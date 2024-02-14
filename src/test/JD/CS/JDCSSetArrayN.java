///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSSetArrayN.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDLobTest.JDTestBlob;
import test.JDLobTest.JDTestClob;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Map;
/**
 * Testcase JDCSSetArray. This tests the following method of the JDBC
 * CallableStatement class:
 *
 * <ul>
 * <li>setObject(parmName) -- since there is no setArray with parameter name (as of JDK 1.8). 
 * </ul>
 */
public class JDCSSetArrayN extends JDCSSetTestcase
{

    
    Hashtable createdProceduresHashtable = new Hashtable();

    private CallableStatement cstmt;

    /**
     * Constructor.
     */
    public JDCSSetArrayN(AS400 systemObject, Hashtable namesAndVars,
            int runMode, FileOutputStream fileOutputStream,
             String password)
    {
        super(systemObject, "JDCSSetArrayN", namesAndVars, runMode,
                fileOutputStream, password);
    }

    /**
     * Performs setup needed before running variations.
     *
     * @exception Exception
     *                If an exception occurs.
     */
    protected void setup() throws Exception
    {

	JDSetupProcedure.resetCollection(collection_);

	  super.setup();


    }

    public void assureProcedureExists(String procedure) throws Exception  {
	if (createdProceduresHashtable.get(procedure) == null) {
	    JDSetupProcedure.create (systemObject_, 
				 connection_, procedure, supportedFeatures_);
	    createdProceduresHashtable.put(procedure, procedure);
	}
    }

    public void reconnect()throws Exception
    {
        connection_ = testDriver_.getConnection(baseURL_ + ";errors=full",
                userId_, encryptedPassword_);

    }


    public void checkConnection() {
	try {
	    Statement stmt = connection_.createStatement();
	    ResultSet rs = stmt.executeQuery("select * from sysibm.sysdummy1");
	    rs.close();
	    stmt.close();
	} catch (Exception e ) {
	    System.out.println("Warning:  Connnection not available");
	    System.out.flush();
	    System.err.flush();
	    e.printStackTrace();
	    System.out.flush();
	    System.err.flush();

	    try {
		reconnect();
	    } catch (Exception e2) {
		System.out.println("Warning:  reconnect failed ");
		System.out.flush();
		System.err.flush();
		e2.printStackTrace();
		System.out.flush();
		System.err.flush();

	    }
	}
    }


    /**
     * reconnect with connection properties.  Properties must start with
     * a semicolon
     */

    public void reconnect(String connectionProperties)throws Exception
    {
	if (connectionProperties == null || connectionProperties.length() == 0) {
	    connection_ = testDriver_.getConnection(baseURL_ + ";errors=full",
						    userId_, encryptedPassword_);
	} else {
	    if (connectionProperties.charAt(0) != ';') {
		throw new SQLException("TESTCASE ERROR PROPERTIES '"+connectionProperties+"' does not begin with a ;");
	    } else {
	    connection_ = testDriver_.getConnection(baseURL_ + ";errors=full"+connectionProperties,
						    userId_, encryptedPassword_);

	    }
	}

    }


    /**
     * Performs cleanup needed after running variations.
     *
     * @exception Exception
     *                If an exception occurs.
     */
    protected void cleanup() throws Exception
    {
	try {
	    reconnect();
	////v7r1 array SPs
	    if(areArraysSupported() )
	    {
	      String[] procedures = {
	           JDSetupProcedure.STP_CSARRSUM,
	           JDSetupProcedure.STP_CSARRINT3,
	           JDSetupProcedure.STP_CSARRINTN,
	           JDSetupProcedure.STP_CSARRINT,
	           JDSetupProcedure.STP_CSARRVCH3,

	           JDSetupProcedure.STP_CSARRINT2,
	           JDSetupProcedure.STP_CSARRINT4,
	           JDSetupProcedure.STP_CSARRSIN,
	           JDSetupProcedure.STP_CSARRIN,
	           JDSetupProcedure.STP_CSARRIN2,
	           JDSetupProcedure.STP_CSARRBIN,
	           JDSetupProcedure.STP_CSARRREA,
	           JDSetupProcedure.STP_CSARRFLO,
	           JDSetupProcedure.STP_CSARRDOU,
	           JDSetupProcedure.STP_CSARRDEC,
	           JDSetupProcedure.STP_CSARRNUM,
	           JDSetupProcedure.STP_CSARRCH1,
	           JDSetupProcedure.STP_CSARRCH50,
	           JDSetupProcedure.STP_CSARRVCH,
	           JDSetupProcedure.STP_CSARRVCH2,
	           JDSetupProcedure.STP_CSARRGR,
	           JDSetupProcedure.STP_CSARRVGR,
	           JDSetupProcedure.STP_CSARRCLO,
	           JDSetupProcedure.STP_CSARRBLO,
	           JDSetupProcedure.STP_CSARRDAT,
	           JDSetupProcedure.STP_CSARRTIM,
	           JDSetupProcedure.STP_CSARRTS,
	           JDSetupProcedure.STP_CSARRBY,
	           JDSetupProcedure.STP_CSARRVBY,
	           JDSetupProcedure.STP_CSARRXML,
	      };
	      for (int i = 0; i < procedures.length; i++) {
	        if (createdProceduresHashtable.get(procedures[i]) != null ) {
	          JDSetupProcedure.dropProcedure(connection_, procedures[i]);
	        }
	      }

		Statement st = connection_.createStatement();
		try{

		//THESE ARE CREATEED IN JDSetupProcedures right before procedure create
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".JDINTARRAY ");
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".JDVCHARRAY ");
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRSIN");     //smallint
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRIN ");    //int
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRIN2 ");    //int and non-array type
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRBIN ");   //bigint
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRREA ");   //read
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRFLO "); //float
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRDOU "); //double
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRDEC "); // decimal
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRNUM "); //numberic
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRCH1 "); //char(1)
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRCH50 "); //char(50)
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRVCH "); //varchar(50)
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRVCH2 "); //varchar(50) and non-array type
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRGR "); //graphic
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRVGR "); //vargraphvi
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRCLO "); //clob
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRBLO "); //blob
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRDAT "); //date
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRTIM "); //time
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRTS "); //timestamp
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRBY "); //binary
		    st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRVBY "); //varbinary
		    if(isJdbc40())
			st.execute("drop type " + JDSetupProcedure.COLLECTION + ".ARRXML "); //xml

		} catch(Exception e){  }

		st.close();
	    }



	    super.cleanup(); 
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Array getDummyArray(){

        java.sql.Array a = null;
	Integer[] ia = new Integer[3];
	ia[0] = new Integer(0);
	ia[1] = new Integer(100);
	ia[2] = new Integer(200);
        try{


            if(isJdbc40() || isToolboxDriver() || getDriver() == JDTestDriver.DRIVER_NATIVE ) //TB put createArrayOf into JDBC30 for testing
            {
                a = (Array)JDReflectionUtil.callMethod_OSA(connection_, "createArrayOf", "INTEGER", ia); // c.createArrayOf("INTEGER", ia);
            }else
            {
                a = new JDCSSetArrayArray(ia);
            }
            return a;
        } catch (Exception e)
        {

	    a = new JDCSSetArrayArray(ia);
            return a;
        }
    }
    /**
     * SetArray() - Set parameter PARAM_1 which doesn't exist. - Using an
     * ordinal parameter
     */
    public void Var001()
    {

        try
        {

            java.sql.Array a = getDummyArray();

            JDReflectionUtil.callMethod_V(cs,"setObject","NOPARM", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
          assertExceptionContains(e, parameterDoesNotExistExceptions, null); 
        }

    }

    /**
     * SetArray() - Should throw an exception when the callable statment is
     * closed. - Using an ordinal parameter
     */
    public void Var002()
    {

        try
        {
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            cstmt.close();
            JDReflectionUtil.callMethod_V(cstmt,"setObject","PARM", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
            assertExceptionContains(e, statementClosedExceptions, null);  
        }

    }

    /**
     * SetArray() - Should fail
     */
    public void Var003()
    {

        try
        {

	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();

            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_CHAR_50", a);
            failed("Didn't throw SQLException");

        } catch (Exception e)
        {
          assertExceptionContains(e,dataTypeMismatchExceptions, null);  
        }

    }

    /**
     * SetArray() - Should throw exception when the parameter is not an input
     * parameter. - Using an ordinal parameter
     */
    public void Var004()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSPARMS);
            java.sql.Array a = getDummyArray();
	    CallableStatement cs1 = connection_.prepareCall("CALL "
                    + JDSetupProcedure.STP_CSPARMS + "( ?, ?, ?)");
            JDReflectionUtil.callMethod_V(cs1,"setObject", "P2", a);
            cs1.close();
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
          assertExceptionContains(e, parameterTypeNotValidExceptions, null);  
        }

    }

    /**
     * SetArray() - should fail
     */
    public void Var005()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_CHAR_50", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  
        }
    }

    /**
     * SetArray() - Set a SMALLINT parameter. - Using an ordinal parameter
     */
    public void Var006()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject","P_SMALLINT", a);
            cstmt.execute();
            failed("Didn't throw SQLException setting smallint parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a INTEGER parameter. - Using an ordinal parameter
     */
    public void Var007()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_INTEGER", a);
            cstmt.execute();
            failed("Didn't throw SQLException setting integer parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a REAL parameter. - Using an ordinal parameter
     */
    public void Var008()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject","P_REAL", a);
            failed("Didn't throw SQLException setting real parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a FLOAT parameter. - Using an ordinal parameter
     */
    public void Var009()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_FLOAT", a);
            failed("Didn't throw SQLException setting float parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a DOUBLE parameter. - Using an ordinal parameter
     */
    public void Var010()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_DOUBLE", a);
            failed("Didn't throw SQLException setting double parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a DECIMAL parameter. - Using an ordinal parameter
     */
    public void Var011()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_DECIMAL_50", a);
            failed("Didn't throw SQLException seeting decimal parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a NUMERIC parameter - Using an ordinal parameter
     */
    public void Var012()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_NUMERIC_50", a);
            failed("Didn't throw SQLException setting numeric parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a CHAR(1) parameter. - Using an ordinal parameter
     */
    public void Var013()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_CHAR_1", a);
            failed("Didn't throw SQLException setting char(1) parameter");
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  
        }

    }

    /**
     * SetArray() - Set a CHAR(50) parameter. - Using an ordinal parameter
     */
    public void Var014()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject","P_CHAR_50", a);
            failed("Didn't throw SQLException setting char(50) parameter");
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  
        }

    }

    /**
     * SetArray() - Set a VARCHAR(50) parameter. - Using an ordinal parameter
     */
    public void Var015()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_VARCHAR_50", a);
            failed("Didn't throw SQLException setting varchar(50) parameter");
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  
        }

    }

    /**
     * SetArray() - Set a CLOB parameter. - Using an ordinal parameter
     */
    public void Var016()
    {

        if (checkLobSupport())
        {
            try
            {
		assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
                java.sql.Array a = getDummyArray();
                cstmt = connection_.prepareCall(callSql);
                JDSetupProcedure.setTypesParameters(cstmt,
                        JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                        );
                JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                        supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_CLOB", a);
                failed("Didn't throw SQLException setting clob parameter");
            } catch (Exception e)
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
            }
        }

    }

    /**
     * SetArray() - Set a BLOB parameter. - Using an ordinal parameter
     */
    public void Var017()
    {
        java.sql.Array a = getDummyArray();
        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_BLOB", a);
            failed("Didn't throw SQLException setting blob parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a DATE parameter to a value that is not a valid date.
     * This should throw an exception. - Using an ordinal parameter
     */
    public void Var018()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_DATE", a);
            failed("Didn't throw SQLException setting date parameter");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a TIME parameter to a value that is not a valid time.
     * This should throw an exception. - Using an ordinal parameter
     */
    public void Var019()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_TIME", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a TIMESTAMP parameter to a value that is not valid. This
     * should throw an exception. - Using an ordinal parameter
     */
    public void Var020()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_TIMESTAMP", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
            try
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                cstmt.close();
            } catch (SQLException s)
            {
                failed(s, "Unexpected Exception");
            }
        }

    }

    /**
     * SetArray() - Set a DATALINK parameter. - Using an ordinal parameter
     */
    public void Var021()
    {

        if (checkLobSupport())
        {
            try
            {
		assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
                java.sql.Array a = getDummyArray();
                cstmt = connection_.prepareCall(callSql);
                JDSetupProcedure.setTypesParameters(cstmt,
                        JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                        );
                JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                        supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_DATALINK", a);
                failed("Didn't throw SQLException");
            } catch (Exception e)
            {
              assertExceptionContains(e, dataTypeMismatchExceptions, null);  
            }
        }

    }

    /**
     * SetArray() - Set a BIGINT parameter. - Using an ordinal parameter
     */
    public void Var022()
    {

        if (checkBigintSupport())
        {
            try
            {
		assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
                java.sql.Array a = getDummyArray();
                cstmt = connection_.prepareCall(callSql);
                JDSetupProcedure.setTypesParameters(cstmt,
                        JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                        );
                JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                        supportedFeatures_, getDriver());
                JDReflectionUtil.callMethod_V(cstmt,"setObject", "P_BIGINT", a);
                failed("Didn't throw SQLException");
            } catch (Exception e)
            {
                try
                {
                  assertExceptionContains(e, dataTypeMismatchExceptions, null);  
                    cstmt.close();
                } catch (SQLException s)
                {
                    failed(s, "Unexpected Exception");
                }
            }
        }

    }

    /**
     * SetArray() - Set a BINARY parameter. - Using an ordinal parameter
     */
    public void Var023()
    {
	if (checkJdbc40()) {
	    try
	    {
		assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
		java.sql.Array a = getDummyArray();
		cstmt = connection_.prepareCall(callSql);
		JDSetupProcedure.setTypesParameters(cstmt,
						    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
						    );
		JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
					  supportedFeatures_, getDriver());
		JDReflectionUtil.callMethod_V(cstmt,"setObject",  "P_BINARY_20", a);
		cstmt.execute();
		String check = (String) JDReflectionUtil.callMethod_O(cstmt,
								      "getNString", "P_BINARY_20");

		failed("Didn't throw SQLException " + check);

	    } catch (Exception e)
	    {
		try
		{
      assertExceptionContains(e, dataTypeMismatchExceptions, null);  
		    cstmt.close();
		} catch (SQLException s)
		{
		    failed(s, "Unexpected Exception");
		}
	    }

	}
    }

    /**
     * SetArray() - Set a VARBINARY parameter. - Using an ordinal parameter
     */
    public void Var024()
    {

	if (checkJdbc40()) {
	    try
	    {
		assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
		java.sql.Array a = getDummyArray();
		cstmt = connection_.prepareCall(callSql);
		JDSetupProcedure.setTypesParameters(cstmt,
						    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
						    );
		JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
					  supportedFeatures_, getDriver());
		JDReflectionUtil.callMethod_V(cstmt,"setObject",  "P_VARBINARY_20", a);
		cstmt.execute();
		String check = (String) JDReflectionUtil.callMethod_O(cstmt,
								      "getNString", "P_VARBINARY_20");

		failed("Didn't throw SQLException " + check);

	    } catch (Exception e)
	    {
		try
		{
      assertExceptionContains(e, dataTypeMismatchExceptions, null);  
		    cstmt.close();
		} catch (SQLException s)
		{
		    failed(s, "Unexpected Exception");
		}
	    }

	}
    }

    /**
     * SetArray() - Set more than one parameter. - Using an ordinal parameter
     */
    public void Var025()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject",  "P_CHAR_1", a);
            JDReflectionUtil.callMethod_V(cstmt,"setObject",  "P_CHAR_50", a);
            failed("Didn't throw SQLException ");
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  

        }

    }

    /**
     * SetArray() - Should set to SQL NULL when the value is null. - Using an
     * ordinal parameter
     */
    public void Var026()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);

            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            cstmt.setObject("P_VARCHAR_50", (Object) null); 
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		failed("Didn't throw SQLException ");
	    } else if (areArraysSupported() ||  isToolboxDriver() ) { //toolbox supports setting an any i5.  Let host return error.
                //with array support in TB this is okay
                assertCondition(true);
	    } else if (getDriver() == JDTestDriver.DRIVER_NATIVE && getRelease()  == JDTestDriver.RELEASE_V7R1M0) {
		// To set as null is not an error
                assertCondition(true);
            } else {
                failed("Didn't throw SQLException ");
            }
        } catch (Exception e)
        {
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		// JCC does not allow null when registered type is 2003
        assertExceptionContains(e, "TBD", null);  
	    } else if (areArraysSupported()) {
		e.printStackTrace();
                failed("Unexpected Exception -changed by TB 4/22/09");
            } else {
              assertExceptionContains(e, "TBD", null);  
            }
        }

    }

    /**
     * SetArray() - Setting parameters with ordinals and with names in the same
     * statement, should throw an SQLException. - Using an ordinal parameter -
     * Using a named parameter
     */
    public void Var027()
    {

        try
        {
	    assureProcedureExists(JDSetupProcedure.STP_CSINOUT);
            java.sql.Array a = getDummyArray();
            cstmt = connection_.prepareCall(callSql);
            JDSetupProcedure.setTypesParameters(cstmt,
                    JDSetupProcedure.STP_CSINOUT, supportedFeatures_
                    );
            JDSetupProcedure.register(cstmt, JDSetupProcedure.STP_CSINOUT,
                    supportedFeatures_, getDriver());
            JDReflectionUtil.callMethod_V(cstmt,"setObject","P_CHAR_1", a);// "P_CHAR_1", a);
            failed("Didn't throw SQLException " );
        } catch (Exception e)
        {
          assertExceptionContains(e, dataTypeMismatchExceptions, null);  
        }
    }

    /**
     * SetArray() - Set parameter PARAM_1 which doesn't exist. - Using a named
     * parameter
     */
    public void Var028()
    {

        try
        {
            java.sql.Array a = getDummyArray();
            JDReflectionUtil.callMethod_V(cstmt,"setObject", "PARAM_1", a); // "PARAM_1", a);
            failed("Didn't throw SQLException");
        } catch (Exception e)
        {
          assertExceptionContains(e, parameterDoesNotExistExceptions, null);  
        }

    }


    /**
     * getArray() - Set an ARRAY of ints (input only for Rakesh)
     */
    public void Var029()
    {
	if(checkArraySupport()) {

	    try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );


	    ////////create array and set/get data
	    //check local copy of array and arrayResultSet
		Integer[] ia = new Integer[4];
		ia[0] = new Integer(0);
		ia[1] = new Integer(100);
		ia[2] = new Integer(200);
		ia[3] = new Integer(300);
		java.sql.Array a = createArray(connection_,  "INTEGER", ia);


	    //call proc with one input array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","numListIn", a);
		cs.execute();

		succeeded();

	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception set array of INTEGER");
	    }
	}
    }

    /**
     * Set an ARRAY of ints and get array out reversed
     * and sanity check
     */
    public void Var030()
    {
        if(checkArraySupport())
        try{

		assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  )
                    check1 = false;

                if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","numListIn", a);
            cs.registerOutParameter(2, java.sql.Types.ARRAY);
            cs.execute();

            Array b = cs.getArray(2);
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)b).getBaseTypeName().equals("INTEGER") )
                check2 = true;




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val

                //check column in resultset is index into array
                if(iaCheck[c1-1].intValue() != c2) //@arrayrs
                    check3 = false;
            }

            assertCondition(check1 & check2 & check3);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRINT2));
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed
     */
    public void Var031()
    {
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


	    StringBuffer sb = new StringBuffer();
	    sb.append(" V7R1 Test for Arrays added by tb 2/26/09 chg 12/05/2011"); 
            ////////create array and set/get data
            Object[] ia = new Object[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("SMALLINT") )  {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("Base type is "+((Array)out).getBaseTypeName()+" sb SMALLINT");
	    }
            /* smallints are Integers from jdbc 4.0 spec:
            Note : JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
	    Object[] outObjectArray = (Object[]) out.getArray();


	    Object[] outa = outObjectArray;
	    if (outObjectArray[0] instanceof Integer) {
		// No change neede
	    } else if (outObjectArray[0] instanceof Short) {
		System.out.println("Output is Short.. Converting input");
		Integer[] intArray =  (Integer[]) ia;
		ia = new Short[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
		    ia[i] = new Short(intArray[i].shortValue());
		}

	    } else {
		throw new Exception("Unexpected array type for "+outObjectArray);
	    }

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa,sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09  chg 12/05/2011");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     */
    public void Var032()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("INTEGER") ) {
                  check1 = true;
	    } else {

	    }


            Integer[] outa =  null ;
            ResultSet rsa = null; 
            if (out != null) {
              outa = (Integer[])out.getArray();
              rsa = out.getResultSet();
            }

            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     */
    public void Var033()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = new Long( 0);
            ia[1] = new Long( 100);
            ia[2] = new Long( 200);
            ia[3] = new Long( 300);
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BIGINT") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out is null");
		} else {
		    sb.append("base type ("+((Array)out).getBaseTypeName()+") is not BIGINT");
		}
	    }
	    if (out != null) {

		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed
     */
    public void Var034()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 2/26/09 "); 
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = new Float((float)0);
            ia[1] = new Float((float)100.1);
            ia[2] = new Float((float)200.2);
            ia[3] = new Float((float)300.3);
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("REAL") ) {
                check1 = true;
	    } else {
		sb.append("Got base type of "+((Array)out).getBaseTypeName()+" expected REAL\n");
		check1 = false;
	    }


            Float[] outa = (Float[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3,sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed
     */
    public void Var035()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double((double)((float)100.1));
            ia[2] = new Double((double)((float)200.2));
            ia[3] = new Double((double)((float)300.3));
	    String baseTypeName = "FLOAT";
            java.sql.Array a;
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName="DOUBLE";
	    }
	    a = createArray(connection_, baseTypeName, ia);
            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

            if( ((Array)out).getBaseTypeName().equals("DOUBLE") || ((Array)out).getBaseTypeName().equals("FLOAT")) {
                check1 = true;
            } else {
                sb.append("getBaseTypeName returned "+((Array)out).getBaseTypeName()+" should be 'DOUBLE' or 'FLOAT'\n");
                check1 = false;
            }
            ///luw returns array of doubles..
            //jdbc spec says jdbc float  is a double ...turns out the TB is coded this way
            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed
     */
    public void Var036()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double(100.1);
            ia[2] = new Double(200.2);
            ia[3] = new Double(300.3);
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals("DOUBLE")) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DOUBLE'\n");
          check1 = false;
        }


            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed
     */
    public void Var037()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals("DECIMAL")) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DECIMAL'\n");
          check1 = false;
        }


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 \n"+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed
     */
    public void Var038()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/

            java.sql.Array a;
	    String baseTypeName = "NUMERIC";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    baseTypeName = "DECIMAL";
	    }
	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals(baseTypeName)) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'NUMERIC'\n");
          check1 = false;
        }


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed
     */
    public void Var039()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1);


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") ) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'CHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed
     */
    public void Var040()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'CHAR'\n");
          check1 = false;
        }


            String[] outa = (String[])out.getArray();
            for(int x = 0; x<outa.length; x++)
                ia[x] = ia[x] + "                                               ";

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed
     */
    public void Var041()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH);
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") ) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'VARCHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed
     */
    public void Var042()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())

        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a;
	    String baseTypeName = "NCHAR";
	    String outputTypeName = "NCHAR";           // Added 02/13/2012
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName="GRAPHIC";
		outputTypeName = baseTypeName;
	    } else if (isToolboxDriver()) {
		if ( ! isJdbc40()) {
		    outputTypeName="CHAR";
		}
	    }
	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals(outputTypeName)) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be '"+baseTypeName+"'\n");
          check1 = false;
        }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed
     */
    public void Var043()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")
                || ((Array)out).getBaseTypeName().equals("NVARCHAR")
		|| ((Array)out).getBaseTypeName().equals("VARCHAR")) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'VARGRAPHIC' or 'NVARCHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed
     */
    public void Var044()
    {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  new JDLobTest.JDTestClob("bbbb");
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'CLOB'\n");
          check1 = false;
        }


            Clob[] outa = (Clob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed
     */
    public void Var045()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] =  new JDLobTest.JDTestBlob("bbbb".getBytes());
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'BLOB'\n");
          check1 = false;
        }


            Blob[] outa = (Blob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var046()
    {
	if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed
     */

    public void testDateCase1(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );
	    sb.append("Connection properties="+connectionProperties+" ");
            reconnect(connectionProperties);
            ////////create array and set/get data
            Date[] ia = new Date[4];
            ia[0] = Date.valueOf("1999-01-01"); 
            ia[1] = Date.valueOf("2001-03-01");
            ia[2] = Date.valueOf("2011-01-01");
            ia[3] = Date.valueOf("2010-02-01");
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DATE") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DATE'\n");
          check1 = false;
        }


            Date[] outa = (Date[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 "+sb);
        }

    }
    /**
     * Set an ARRAY of date and get array out reversed
     */
    public void Var047()
    {
	testDateCase1("");
    }



    /**
     * testTime1
     *
     * Set an ARRAY of time and get array out reversed
     */

     public void testTimeCase1(String connectionProperties) {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
            reconnect(connectionProperties);
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );

            ////////create array and set/get data
            Time[] ia = new Time[4];
	    if (connectionProperties.indexOf("usa") > 0) {
		// usa does not have seconds
		ia[0] = Time.valueOf("01:01:00");
		ia[1] = Time.valueOf("11:01:00");
		ia[2] = Time.valueOf("01:11:00");
		ia[3] = Time.valueOf("23:13:00");
	    } else {
		ia[0] = Time.valueOf("01:01:01");
		ia[1] = Time.valueOf("11:01:00");
		ia[2] = Time.valueOf("01:11:01");
		ia[3] = Time.valueOf("23:13:11");
	    }
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIME") )
                check1 = true;


            Time[] outa = (Time[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/26/11 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - properties="+connectionProperties+" V7R1 Test for Arrays updated  2/26/11 ");
        }

     }

    /**
     * Set an ARRAY of time and get array out reversed
     */
    public void Var048()
    {
	testTimeCase1("");
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed
     */
    public void Var049()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = Timestamp.valueOf("2001-01-01 01:01:01.000001");
            ia[1] = Timestamp.valueOf("2001-02-03 04:05:06.000007");
            ia[2] = Timestamp.valueOf("2010-11-12 13:14:15.000016");
            ia[3] = Timestamp.valueOf("2000-05-16 07:08:09.000010");
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


            Timestamp[] outa = (Timestamp[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed
     */
    public void Var050()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb".getBytes();
            ia[2] = "cccccccccccccccccccccccccccccccccccccccccccccccccc".getBytes();
            ia[3] = "dddddddddddddddddddddddddddddddddddddddddddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BINARY") )
                check1 = true;


            byte[][] outa = ( byte[][])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed
     */
    public void Var051()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbb".getBytes();
            ia[2] = "cccccccccc".getBytes();
            ia[3] = "dddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
                check1 = true;


            byte[][] outa = ( byte[][])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed
     */
    public void Var052()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    /* moved to JDVariationSkip 
            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }

	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }
*/	
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );
            reconnect();
            ////////create array and set/get data
            Object sqlxml1  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml1, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>aaaa</name>");
            Object sqlxml2  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml2, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>bbbb</name>");
            Object sqlxml3  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml3, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>cccc</name>");
            Object sqlxml4  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml4, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>dddd</name>");

            Object[] ia = new Object[4];
            ia[0] = sqlxml1;
            ia[1] =  sqlxml2;
            ia[2] = sqlxml3;
            ia[3] =  sqlxml4;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }

    /**
     * Set an ARRAY of integer in java STP get array out reversed
     */
    public void Var053()
    {
	if (checkArraySupport())
        try{


            succeeded();
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of int  and non-array int
     */
    public void Var054()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 2/26/09 "); 
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN2 );

            reconnect();
            ////////create array and set/get data
            Integer[] ia1 = new Integer[4];
            ia1[0] = new Integer( 0);
            ia1[1] = new Integer(1);
            ia1[2] = new Integer(2);
            ia1[3] = new Integer(3);
            java.sql.Array a1 = createArray(connection_, "INTEGER", ia1);

            Integer[] ia2 = new Integer[4];
            ia2[0] = new Integer( 0);
            ia2[1] = new Integer(100);
            ia2[2] = new Integer(200);
            ia2[3] = new Integer(300);
            java.sql.Array a2 = createArray(connection_, "INTEGER", ia2);

            //call proc   (in array, in int, inout array, inout int, out array, out int)
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN2 + " ( ?,?,?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setInt(2, 222);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P3", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P4", java.sql.Types.INTEGER);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setInt("P4", 444);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P5", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P6", java.sql.Types.INTEGER);
            cs.execute();

            Array outa1 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","P3");
            Array outa2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","P5");
            int outi1 = cs.getInt("P4"); //222
            int outi2 = cs.getInt("P6"); //444

            boolean check1 = false;
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)outa1).getBaseTypeName().equals("INTEGER") )
                check1 = true;
            if( ((Array)outa2).getBaseTypeName().equals("INTEGER") )
                check2 = true;


            Integer[] outa1a = (Integer[])outa1.getArray();
            Integer[] outa2a = (Integer[])outa2.getArray();

            ResultSet rsa1 = outa1.getResultSet();
            ResultSet rsa2 = outa2.getResultSet();
            boolean check3 = checkArrayOutput(ia1, outa1a, sb);
            boolean check4 = checkArrayResultSetOutput(ia1, rsa1, sb);
            boolean check5 = checkArrayOutput(ia2, outa2a, sb);
            boolean check6 = checkArrayResultSetOutput(ia2, rsa2, sb);



            assertCondition(outi1 == 222 && outi2 == 444 && check1 && check2 && check3 &&check4 && check5 && check6, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varchar and non-array varchar
     */
    public void Var055()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 2/26/09 "); 
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH2 );
            reconnect();

            ////////create array and set/get data
            String[] ia1 = new String[4];
            ia1[0] = "aaa";
            ia1[1] = "bbb";
            ia1[2] = "ccc";
            ia1[3] = "ddd";
            java.sql.Array a1 = createArray(connection_, "VARCHAR", ia1);

            String[] ia2 = new String[4];
            ia2[0] = "a2";
            ia2[1] = "bb2";
            ia2[2] = "ccc2";
            ia2[3] = "dddd2";
            java.sql.Array a2 = createArray(connection_, "VARCHAR", ia2);



            //call proc   (in array, in int, inout array, inout int, out array, out int)
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH2 + " ( ?,?,?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setString("P2", "222");
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P3", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P4", java.sql.Types.VARCHAR);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setString("P4", "444");
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P5", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P6", java.sql.Types.VARCHAR);
            cs.execute();

            Array outa1 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray",3);
            Array outa2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray",5);
            String outi1 = cs.getString(4); //222
            String outi2 = cs.getString(6); //444

            boolean check1 = false;
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)outa1).getBaseTypeName().equals("VARCHAR") )
                check1 = true;
            if( ((Array)outa2).getBaseTypeName().equals("VARCHAR") )
                check2 = true;


            String[] outa1a = (String[])outa1.getArray();
            String[] outa2a = (String[])outa2.getArray();

            ResultSet rsa1 = outa1.getResultSet();
            ResultSet rsa2 = outa2.getResultSet();
            boolean check3 = checkArrayOutput(ia1, outa1a, sb);
            boolean check4 = checkArrayResultSetOutput(ia1, rsa1, sb);
            boolean check5 = checkArrayOutput(ia2, outa2a, sb);
            boolean check6 = checkArrayResultSetOutput(ia2, rsa2, sb);



            assertCondition(outi1.equals("222") && outi2.equals("444") && check1 && check2 && check3 &&check4 && check5 && check6, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of INTEGERS converted to VARCHAR and get array out reversed
     */
    public void Var056()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??
            reconnect();
            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            String[] ias = new String[4];
            ias[0] = "0";
            ias[1] = "100";
            ias[2] = "200";
            ias[3] = "300";
            java.sql.Array a = createArray(connection_, "INTEGER", ia);


            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) "); //proc is array of VARCHAR(50) -pass in INT array
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a); //pass in array of INTs
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ias, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ias, rsa, sb);

            cs.close();
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09  INTEGER converted to VARCHAR "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09  INTEGER converted to VARCHAR ");
        }
    }


    /**
     * Set an ARRAY of Strings passed into proc of array ints and get array out reversed
     */
    public void Var057()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN);

            reconnect();
            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            String[] ias = new String[4];
            ias[0] = "0";
            ias[1] = "100";
            ias[2] = "200";
            ias[3] = "300";
            java.sql.Array a = createArray(connection_, "VARCHAR", ias);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 VARCHAR to Int conversion "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 VARCHAR to Int conversion  ");
        }
    }

    /**
     * getArray() - Set an ARRAY of VARCHARS and nonarray VARCHAR (input only for Rakesh)
     */
    public void Var058()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH3 );
            reconnect();

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            String[] ias = new String[4];
            ias[0] = "aaaaaaaa";
            ias[1] = "bbbbbbbb";
            ias[2] = "cccccccc";
            ias[3] = "dddddddd";
            java.sql.Array a = createArray(connection_, "VARCHAR", ias);

            //call proc with one input array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH3 + " (?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);
            cs.setString("P2", "yyyyyyyy");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "zzzzzzzz");
            cs.execute();

            assertCondition(true, sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * getArray() - Set an ARRAY of INTS in proc of array VARCHARS (input only for Rakesh)
     *
     */
    public void Var059()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH3 );
            reconnect();
            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_,  "INTEGER", ia);


            //call proc with one input array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH3 + " (?, ?,?,?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);//!!!pass in array of INTS which will convert to STRINGS
            cs.setString("P2", "aaa");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "bbb");
            cs.execute();

            assertCondition(true, sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception CONVERT INT TO VARCHAR ");
        }
    }

    /**
     * getArray() - Set an ARRAY of VARCHARS into proc of array-INTS (input only for Rakesh)
     */
    public void Var060()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport()) {
	    try {
		assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
	        reconnect();
	    ////////create array and set/get data
	    //check local copy of array and arrayResultSet
		String[] ias = new String[4];
		ias[0] = "0";
		ias[1] = "1";
		ias[2] = "2";
		ias[3] = "3";
		java.sql.Array a = createArray(connection_, "VARCHAR", ias);


	    //call proc with one input array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
		cs.execute();

               assertCondition(true, sb);

	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception CONVERT VARCHAR TO INT");
	    }
	}
    }

    /**
     * getArray() - Set an ARRAY of all null values
     */
    public void Var061()
    {
    if (checkArraySupport()) {
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
            reconnect();
        ////////create array and set/get data
        //check local copy of array and arrayResultSet
        String[] ias = new String[4];
        ias[0] = null;
        ias[1] = null;
        ias[2] = null;
        ias[3] = null;
        java.sql.Array a = createArray(connection_, "VARCHAR", ias);


        //call proc with one input array
        CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();

        succeeded();

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception CONVERT VARCHAR TO INT");
        }
    }
    }

    /**
     * getArray() - Set an ARRAY of some null values
     */
    public void Var062()
    {
    if (checkArraySupport()) {
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
            reconnect();
        ////////create array and set/get data
        //check local copy of array and arrayResultSet
        String[] ias = new String[4];
        ias[0] = "0";
        ias[1] = null;
        ias[2] = "2";
        ias[3] = null;
        java.sql.Array a = createArray(connection_, "VARCHAR", ias);


        //call proc with one input array
        CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();

        succeeded();

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception  CONVERT VARCHAR TO INT");
        }
    }
    }

    /**
     * getArray() - Set/get a null int ARRAY
     */
    public void Var063()
    {
    if (checkArraySupport()) {
	String sql1 = ""; 
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINTN );
            reconnect();
        //call proc with one input array
	    sql1 = "call " + JDSetupProcedure.STP_CSARRINTN + " (?,?) "; 
        CallableStatement cs = connection_.prepareCall(sql1);
        cs.setObject("NUMLISTIN", null);
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
        cs.execute();
        Array aout = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
        boolean check1 = false;
        if(cs.wasNull() && aout == null)
            check1 = true;
        cs = connection_.prepareCall(sql1);
        cs.setNull(1, java.sql.Types.ARRAY);
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
        cs.execute();
        Array aout2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
        boolean check2 = false;
        if(cs.wasNull() && aout2 == null)
            check2 = true;

        assertCondition(check1 && check2, "null array parms failed");

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception sql="+sql1);
        }
    }
    }


    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed
     */

    public void testDateCase2(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    sb.append("ConnectionProperties="+connectionProperties+" ");
            reconnect(connectionProperties);

            ////////create array and set/get data
            JDCSSetArrayDate[] ia = new JDCSSetArrayDate[4];
            ia[0] = new JDCSSetArrayDate( Date.valueOf("1999-01-01").getTime()); 
            ia[1] =  new JDCSSetArrayDate(Date.valueOf("2001-03-01").getTime());
            ia[2] =  new JDCSSetArrayDate(Date.valueOf("2011-01-01").getTime());
            ia[3] =  new JDCSSetArrayDate(Date.valueOf("2010-02-01").getTime());
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DATE") )
                check1 = true;


            Date[] outa = (Date[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays updated 2/25/2011 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays updated 2/25/2011 "+sb);
        }

    }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed
     */
    public void Var064()
    {
	testDateCase2("");

    }


    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed
     */

    public void testTimeCase2(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );

            reconnect(connectionProperties);

            ////////create array and set/get data
            JDCSSetArrayTime[] ia = new JDCSSetArrayTime[4];
            ia[0] = new JDCSSetArrayTime(1,1,1);
            ia[1] = new JDCSSetArrayTime(11,1,0);
            ia[2] = new JDCSSetArrayTime(1,11,1);
	    if (connectionProperties.indexOf("usa") > 0) {
		// Use does not have seconds
		ia[0] = new JDCSSetArrayTime(1,1,0);
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = new JDCSSetArrayTime(1,11,0);
		ia[3] = new JDCSSetArrayTime(23,13,00);
	    } else {
		ia[0] = new JDCSSetArrayTime(1,1,1);
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = new JDCSSetArrayTime(1,11,1);
		ia[3] = new JDCSSetArrayTime(23,13,11);
	    }
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIME") )
                check1 = true;


            Time[] outa = (Time[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/25/2011 ");




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - connectionProperties="+connectionProperties+" V7R1 Test for Arrays updated 2/25/2011 ");
        }

    }

    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed
     */
    public void Var065()
    {
	testTimeCase2("");
    }



    /**
     * Set an ARRAY of JDCSSetArrayTimestamp and get array out reversed
     */
    public void Var066()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );

            reconnect();
            ////////create array and set/get data
            JDCSSetArrayTimestamp[] ia = new JDCSSetArrayTimestamp[4];
            ia[0] = new JDCSSetArrayTimestamp(1,1,1,1,1,1,1);
            ia[1] = new JDCSSetArrayTimestamp(1,2,3,4,5,6,7);
            ia[2] = new JDCSSetArrayTimestamp(10,11,12,13,14,15,16);
            ia[3] = new JDCSSetArrayTimestamp(100,5,6,7,8,9,10);
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


            Timestamp[] outa = (Timestamp[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by native 5/4/09 ");




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by native 5/4/09 ");
        }
    }



    /**
     * Set an ARRAY of JDCSSetArrayBigDecimal and get array out reversed
     */
    public void Var067()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );

            reconnect();
            ////////create array and set/get data
            JDCSSetArrayBigDecimal[] ia = new JDCSSetArrayBigDecimal[4];
            ia[0] = new JDCSSetArrayBigDecimal("0.00000");
            ia[1] = new JDCSSetArrayBigDecimal("100.10000");
            ia[2] = new JDCSSetArrayBigDecimal("200.20000");
            ia[3] = new JDCSSetArrayBigDecimal("300.30000");
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of JDTestClob and get array out reversed
     */
    public void Var068()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );

            reconnect();
            ////////create array and set/get data
            JDLobTest.JDTestClob[] ia = new JDLobTest.JDTestClob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  new JDLobTest.JDTestClob("bbbb");
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") )
                check1 = true;


            Clob[] outa = (Clob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of JDLobTest.JDTestBlob and get array out reversed
     */
    public void Var069()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            JDLobTest.JDTestBlob[] ia = new JDLobTest.JDTestBlob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] =  new JDLobTest.JDTestBlob("bbbb".getBytes());
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") )
                check1 = true;


            Blob[] outa = (Blob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int input and  array out
     */
    public void Var070()
    {
        StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 8/26/09 "); 
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT4 );
            reconnect();
            Integer[] ia = { new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6)};

            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT4 + " ( ?, ?) ");
            cs.setInt(1, 1);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLIST", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLIST");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutput(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutput(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 8/26/09 ");
        }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (null elements)
     * and sanity check
     */
    public void Var071()
    {
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );


            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = null;
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1] == null){
                    if( ia[retRS.getInt(1)-1] != iaCheck[retRS.getInt(1)-1] ) //check nulls
                    check1 = false;
                }
                else if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  )
                    check1 = false;

                if(iaCheck[retRS.getInt(1)-1] == null){
                    retRS.getInt(2);
                    if(  retRS.wasNull() == false ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
                 }
                else if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();

            Array b = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)b).getBaseTypeName().equals("INTEGER") )
                check2 = true;




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val

                //check column in resultset is index into array
                if(iaCheck[c1-1] == null) //@arrayrs
                {
                    if(retRS.wasNull() == false)
                        check3 = false;
                }
                else if(iaCheck[c1-1].intValue() != c2) //@arrayrs
                    check3 = false;
            }

            assertCondition(check1 & check2 & check3);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (null elements)
     */
    public void Var072()
    {
      StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = null;
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
                check1 = true;
            /* smallints are Integers from jdbc 4.0 spec:
            Note  The JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (null elements)
     */
    public void Var073()
    {
      StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = null;
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (null elements)
     */
    public void Var074()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = new Long( 0);
            ia[1] = null;
            ia[2] = new Long( 200);
            ia[3] = new Long( 300);
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BIGINT") )
                check1 = true;


            Long[] outa = (Long[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (null elements)
     */
    public void Var075()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = null;
            ia[1] = new Float((float)100.1);
            ia[2] = new Float((float)200.2);
            ia[3] = new Float((float)300.3);
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("REAL") )
                check1 = true;


            Float[] outa = (Float[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (null elements)
     */
    public void Var076()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );

            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = new Double((double)((float)100.1));
            ia[2] = new Double((double)((float)200.2));
            ia[3] = new Double((double)((float)300.3));
            java.sql.Array a;
	    String baseTypeName = "FLOAT";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "DOUBLE";
	    }

	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

            if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
                check1 = true;

            ///luw returns array of doubles..
            //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way
            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (null elements)
     */
    public void Var077()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double(100.1);
            ia[2] = new Double(200.2);
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
                check1 = true;


            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (null elements)
     */
    public void Var078()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (null elements)
     */
    public void Var079()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );

            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/
	    String baseTypeName = "NUMERIC";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "DECIMAL";
	    }
            java.sql.Array a = createArray(connection_, baseTypeName, ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals(baseTypeName) )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (null elements)
     */
    public void Var080()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (null elements)
     */
    public void Var081()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = null;
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            for(int x = 0; x<ia.length; x++)
                if(ia[x] != null)
                    ia[x] = ia[x] + "                                               ";


            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (null elements)
     */
    public void Var082()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
        boolean check3 = false;
        if (out != null) {
          // System.out.println( ((Array)b).getBaseTypeName());
          if (((Array) out).getBaseTypeName().equals("VARCHAR"))
            check1 = true;

          String[] outa = (String[]) out.getArray();

          ResultSet rsa = out.getResultSet();
          check2 = checkArrayOutputReversed(ia, outa, sb);
          check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
        }
        assertCondition(check1 && check2 && check3,
            " V7R1 Test for Arrays added by tb 2/26/09 " + sb);



        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (null elements)
     */
    public void Var083()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = null;
	    String baseTypeName = "NCHAR";
	    String outputTypeName = baseTypeName;
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "GRAPHIC";
	    } else 	    if (isToolboxDriver()) {
		if ( ! isJdbc40()) {
		    outputTypeName="CHAR";
		} else {
		    outputTypeName="NCHAR";
		}
	    }
            java.sql.Array a;
            a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals(outputTypeName) ) {
		check1 = true;
	    }


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (null elements)
     */
    public void Var084()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = null;
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR")  || ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (null elements)
     */
    public void Var085()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  null;
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") )
                check1 = true;


		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (null elements)
     */
    public void Var086()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] = null;
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") )
                check1 = true;


		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var087()
    {
    if (checkArraySupport())
        succeeded();
    }

    public void testDateCase3(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
	    try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );
		sb.append("ConnectionProperties="+connectionProperties+" ");
		reconnect(connectionProperties);
	    ////////create array and set/get data
		Date[] ia = new Date[4];
		ia[0] = new Date(917848800000L);
		ia[1] =  null;
		ia[2] =  new Date(1293861600000L);
		ia[3] =  new Date(1265004000000L);
		java.sql.Array a = createArray(connection_, "DATE", ia);

	    //call proc to revers 4 elements in array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
		JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
		cs.execute();

		Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
		boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;
    if (out != null) {
		if( ((Array)out).getBaseTypeName().equals("DATE") )
		    check1 = true;


		    Date[] outa = (Date[])out.getArray();

		    ResultSet rsa = out.getResultSet();
		    check2 = checkArrayOutputReversed(ia, outa, sb);
		    check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
		}
		assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays update 2/26/2011 "+sb);




	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays updated 2/26/2011 "+sb);
	    }

    }
    /**
     * Set an ARRAY of date and get array out reversed (null elements)
     */
    public void Var088()
    {
	testDateCase3("");
    }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)
     */

    public void testTimeCase3(String connectionProperties) {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
	    try{
		reconnect(connectionProperties);
		assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );

	    ////////create array and set/get data
		Time[] ia = new Time[4];
		ia[0] = null;
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = null;
		if (connectionProperties.indexOf("usa") > 0) {
		    // Usa does not have seconds
		    ia[3] = new JDCSSetArrayTime(23,13,00);
		} else {
		    ia[3] = new JDCSSetArrayTime(23,13,11);
		}
		java.sql.Array a = createArray(connection_, "TIME", ia);

	    //call proc to revers 4 elements in array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
		JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
		cs.execute();

		Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
		boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;
    if (out != null) {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIME") )
		    check1 = true;


		    Time[] outa = (Time[])out.getArray();

		    ResultSet rsa = out.getResultSet();
		    check2 = checkArrayOutputReversed(ia, outa, sb);
		    check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
		}
		assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/26/11 "+sb);


	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception - connectionProperties="+connectionProperties+" V7R1 Test for Arrays updated  2/26/11 ");
	    }

    }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)
     */
    public void Var089()
    {
	testTimeCase3("");

    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (null elements)
     */
    public void Var090()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = null;
            ia[1] = new JDCSSetArrayTimestamp(1,2,3,4,5,6,7);
            ia[2] = new JDCSSetArrayTimestamp(10,11,12,13,14,15,16);
            ia[3] = new JDCSSetArrayTimestamp(100,5,6,7,8,9,10);
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (null elements)
     */
    public void Var091()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
            ia[1] = null;
            ia[2] = "cccccccccccccccccccccccccccccccccccccccccccccccccc".getBytes();
            ia[3] = "dddddddddddddddddddddddddddddddddddddddddddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BINARY") )
                check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (null elements)
     */
    public void Var092()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbb".getBytes();
            ia[2] = null;
            ia[3] = "dddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
                check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (null elements)
     */
    public void Var093()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );

/*
            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		     return;
	    }
*/

            reconnect();
            ////////create array and set/get data
            Object sqlxml1  = null;
            Object sqlxml2  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml2, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>bbbb</name>");
            Object sqlxml3  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml3, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>cccc</name>");
            Object sqlxml4  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml4, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>dddd</name>");

            Object[] ia = new Object[4];
            ia[0] = sqlxml1;
            ia[1] =  sqlxml2;
            ia[2] = sqlxml3;
            ia[3] =  sqlxml4;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


		Object[] outa = (Object[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }


    /**
     * Set an ARRAY of ints and get array out reversed (All null elements)
     * and sanity check
     */
    public void Var094()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1] == null){
		    if( ia[retRS.getInt(1)-1] != iaCheck[retRS.getInt(1)-1] ) { //check nulls
			check1 = false;
			sb.append("null values not matching for row (1-based) "+retRS.getInt(1)+"\n");
		    }
		} else {

		    if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  ) {
			sb.append("ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  "+ia[retRS.getInt(1)-1].intValue() +" !=  "+iaCheck[retRS.getInt(1)-1].intValue()+" \n");
			check1 = false;
		    }
		    if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
		    {
			check1 = false;
			sb.append("iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2) "+iaCheck[retRS.getInt(1)-1].intValue()+"!="+retRS.getInt(2)+"\n") ;
		    }
		}
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();

            Array b = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)b).getBaseTypeName().equals("INTEGER") ) {
                check2 = true;
            } else {
                check2 = false;
                sb.append("Got baseTypeName of "+((Array)b).getBaseTypeName()+ " expected INTEGER\n");
            }




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val
		boolean wasNull = retRS.wasNull();

                //check column in resultset is index into array
		if (iaCheck[c1-1] == null) {
		    if (!wasNull) {
   		      sb.append("iaCheck[c1-1] == null when c1="+c1+" and c2="+c2);
		      check3 = false;
		    }
		} else {
		    if(iaCheck[c1-1].intValue() != c2) //@arrayrs
			check3 = false;
		}
            }

            assertCondition(check1 & check2 & check3, sb);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (ALL NULL elements)
     */
    public void Var095()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
                check1 = true;
            /* smallints are Integers from jdbc 4.0 spec:
            Note  The JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (ALL NULL elements)
     */
    public void Var096()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (ALL NULL elements)
     */
    public void Var097()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BIGINT") )
                check1 = true;



		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (ALL NULL elements)
     */
    public void Var098()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("REAL") )
                check1 = true;



		Float[] outa = (Float[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (ALL NULL elements)
     */
    public void Var099()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "FLOAT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());

	    if( ((Array)out).getBaseTypeName().equals("DOUBLE") ) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("BASE type is "+((Array)out).getBaseTypeName()+" sb DOUBLE\n");
	    }

            ///luw returns array of doubles..
            //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way

		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (ALL NULL elements)
     */
    public void Var100()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("DOUBLE") ) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("BASE type is "+((Array)out).getBaseTypeName()+" sb DOUBLE\n");
	    }

		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (ALL NULL elements)
     */
    public void Var101()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;


		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (ALL NULL elements)
     */
    public void Var102()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/
            java.sql.Array a = createArray(connection_, "NUMERIC", ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NUMERIC") )
                check1 = true;

		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (ALL NULL elements)
     */
    public void Var103()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (ALL NULL elements)
     */
    public void Var104()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();



		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (ALL NULL elements)
     */
    public void Var105()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (ALL NULL elements)
     */
    public void Var106()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "GRAPHIC", ia);
            a = createArray(connection_, "NCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NCHAR")  || ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (ALL NULL elements)
     */
    public void Var107()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR") || ((Array)out).getBaseTypeName().equals("VARCHAR") )) {
                 check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out is null");
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb VARGRAPHIC or NVARCHAR");

		}
	    }


	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (ALL NULL elements)
     */
    public void Var108()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	   // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("CLOB") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCLO));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb CLOB");

		}
	    }
	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null)  {
		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (ALL NULL elements)
     */
    public void Var109()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null &&  ((Array)out).getBaseTypeName().equals("BLOB") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBLO));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb BLOB");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var110()
    {
    if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed (ALL NULL elements)
     */
    public void Var111()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );

            reconnect();
            ////////create array and set/get data
            Date[] ia = new Date[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	   // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null &&  ((Array)out).getBaseTypeName().equals("DATE") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDAT));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb DATE");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		Date[] outa = (Date[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of time and get array out reversed  (ALL NULL elements)
     */
    public void Var112()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );
            reconnect();

            ////////create array and set/get data
            Time[] ia = new Time[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("TIME") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTIM));
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb TIME");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {
		Time[] outa = (Time[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (ALL NULL elements)
     */
    public void Var113()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if(out != null &&  ((Array)out).getBaseTypeName().equals("TIMESTAMP") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTS));
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb TIMESTAMP");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;
	    if (out != null) {
		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (ALL NULL elements)
     */
    public void Var114()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BINARY")) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBY));
		} else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb BINARY");
		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;


	    if (out != null) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (ALL NULL elements)
     */
    public void Var115()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("VARBINARY") )  {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
		} else {
		    sb.append("baseType="+((Array)out).getBaseTypeName()+" expected VARBINARY");
		}
	    }
	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (ALL NULL elements)
     */
    public void Var116()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRXML );

/*
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }

            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
*/
            reconnect();
            ////////create array and set/get data


            Object[] ia = new Object[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (0 len array)
     * and sanity check
     */
    public void Var117()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  ) {
                    check1 = false;
                    sb.append("check1 failed comparing intValue");
                }

                if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute(); //expect error since proc access array index


            failed("Did not get error as expected check1="+check1);
        }catch(Exception e){
	    String message = e.getMessage();
	    if (message == null) message = "No Message:  class is "+e.getClass().getName();
	    boolean passed = message.indexOf("not valid for array subscript") != -1;
	    if (!passed) {
		passed = message.indexOf("20439") != -1;
	    }
	    if (!passed) {
		e.printStackTrace();
	    }
            assertCondition(passed, "Incorrect error.  Expected error since access past array length");
        }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (0 len array)
     * and sanity check
     */
    public void Var118()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  )
                    check1 = false;

                if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();

            failed("Did not get error as expected check1="+check1+" "+sb.toString());
        }catch(Exception e){


	    String message = e.getMessage();
	    if (message == null) message = "No Message:  class is "+e.getClass().getName();
	    boolean passed = message.indexOf("not valid for array subscript") != -1;
	    if (!passed) {
		passed = message.indexOf("20439") != -1;
	    }

	    if (!passed) {
		e.printStackTrace();
	    }
            assertCondition(passed,  "Incorrect Error.  Expected error since access past array length");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (0 len array)
     */
    public void Var119()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Object[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRSIN));

	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
		    check1 = true;
	    /* smallints are Integers from jdbc 4.0 spec:
	    Note  The JDBC 1.0 specification defined the Java object mapping for the
	    SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
	    include the Byte and Short data types when the JDBC 1.0 specification was
	    finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
	    preserve backwards compatibility.*/
		Object[] outObjectArray = (Object[]) out.getArray();
	    Object [] outa = outObjectArray;
	    String arrayClassName = outObjectArray.getClass().getComponentType().getName();
	    if (arrayClassName.equals("java.lang.Integer")) {
		// No fix needed
	    } else if (arrayClassName.equals("java.lang.Short")) {
		System.out.println("Output is Short.. Converting input");
		Integer[] intArray =  (Integer[]) ia;
		ia = new Short[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
		    ia[i] = new Short(intArray[i].shortValue());
		}

	    } else {
		throw new Exception("Unexpected array type for "+arrayClassName);
	    }



		ResultSet rsa = out.getResultSet();
		 check2 = checkArrayOutputReversed(ia, outa, sb);
		 check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (0 len array)
     */
    public void Var120()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRIN));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("INTEGER") )
		    check1 = true;


		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (0 len array)
     */
    public void Var121()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[0];

            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
 	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBIN));

	    } else {
	  // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("BIGINT") )
		    check1 = true;


		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (0 len array)
     */
    public void Var122()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[0];

            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRREA));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("REAL") )
		    check1 = true;


		Float[] outa = (Float[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (0 len array)
     */
    public void Var123()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );


            ////////create array and set/get data
            Double[] ia = new Double[0];

            java.sql.Array a = createArray(connection_, "FLOAT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRFLO));

	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());

		if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
		    check1 = true;

	    ///luw returns array of doubles..
	    //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way
		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (0 len array)
     */
    public void Var124()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[0];

            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDOU));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
		    check1 = true;


		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (0 len array)
     */
    public void Var125()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[0];

            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;

	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDEC));
	    } else {

       // System.out.println( ((Array)b).getBaseTypeName());

		if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
		    check1 = true;


		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (0 len array)
     */
    public void Var126()
    {
	checkConnection();

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[0];


            java.sql.Array a = createArray(connection_, "NUMERIC", ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRNUM));
	    } else {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NUMERIC") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
             check2 = checkArrayOutputReversed(ia, outa, sb);
             check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (0 len array)
     */
    public void Var127()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCH1));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("CHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (0 len array)
     */
    public void Var128()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCH50));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("CHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		for(int x = 0; x<ia.length; x++)
		    ia[x] = ia[x] + "                                               ";

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		 check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (0 len array)
     */
    public void Var129()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVCH));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (0 len array)
     */
    public void Var130()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "GRAPHIC", ia);
            a = createArray(connection_, "NCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRGR));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("NCHAR") ||
		    ((Array)out).getBaseTypeName().equals("CHAR")  )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (0 len array)
     */
    public void Var131()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVGR));
	    } else {

	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR") || ((Array)out).getBaseTypeName().equals("VARCHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (0 len array)
     */
    public void Var132()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[0];

            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCLO));
	    } else {

		if( ((Array)out).getBaseTypeName().equals("CLOB") )
		    check1 = true;


		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (0 len array)
     */
    public void Var133()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[0];

            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if (out == null) {
		sb.append("output array is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBLO));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("BLOB") )
		    check1 = true;


		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var134()
    {
    if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed (0 len array)
     */
    public void Var135()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );

            reconnect();
            ////////create array and set/get data
            Date[] ia = new Date[0];

            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;

           // System.out.println( ((Array)b).getBaseTypeName());
	    if (out == null) {
		sb.append("output array is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDAT));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("DATE") )
		    check1 = true;


		Date[] outa = (Date[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length date added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of time and get array out reversed  (0 len array)
     */
    public void Var136()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );
            reconnect();

            ////////create array and set/get data
            Time[] ia = new Time[0];

            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
	    boolean check3 = false;

	    if (out == null) {
		sb.append("output array is null "+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTIM));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIME") )
		    check1 = true;


		Time[] outa = (Time[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length time Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (0 len array)
     */
    public void Var137()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[0];

            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

	    if (out == null) {
		check1=false;
		sb.append("output array is null " +" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTS));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
		    check1 = true;


		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length byte array added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (0 len array)
     */
    public void Var138()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[2][];

	    ia[0]=new byte[4];
	    ia[1]=new byte[3];
	    ia[0][0]=0;
	    ia[0][1]=1;
	    ia[0][2]=2;
	    ia[0][3]=3;
	    ia[1][0]=70;
	    ia[1][1]=71;
	    ia[1][2]=72;

	    byte[][] expectedArray = new byte[2][];

	    expectedArray[0]=new byte[50];
	    expectedArray[1]=new byte[50];
	    expectedArray[0][0]=0;
	    expectedArray[0][1]=1;
	    expectedArray[0][2]=2;
	    expectedArray[0][3]=3;
	    expectedArray[1][0]=70;
	    expectedArray[1][1]=71;
	    expectedArray[1][2]=72;

	    //
	    // Because this is a binary field, the size of the output
	    // must match the size of the declared array.  In this
	    // case the size is 50
	    //

            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BINARY")) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out == null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
		} else {
		    sb.append("Base type is "+((Array)out).getBaseTypeName()+" expected BINARY");
		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null ) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(expectedArray, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(expectedArray, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (0 len array)
     */
    public void Var139()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[0][0];

            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

	    if (out == null) {
		check1 = false;
		sb.append("output parameter is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
		    check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length byte array added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (0 len array)
     */
    public void Var140()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
/*
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }

            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
*/
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );
            reconnect();
            ////////create array and set/get data


            Object[] ia = new Object[0];

            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }



    //
    // Test various null types
    //
    public void Var141() { testNullArray("JDCSNARINT", "INTEGER"); }
    public void Var142() { testNullArray("JDCSNARSI",  "SMALLINT"); }
    public void Var143() { testNullArray("JDCSNARBIN",  "BIGINT"); }

    public void Var144() { testNullArray( "JDCSNARREA", "REAL"); }
    public void Var145() { testNullArray( "JDCSNARFLO", "FLOAT"); }
    public void Var146() { testNullArray( "JDCSNARDOU", "DOUBLE"); }
    public void Var147() { testNullArray( "JDCSNARDEC", "DECIMAL(10,2)"); }
    public void Var148() { testNullArray( "JDCSNARNUM", "NUMERIC(10,2)"); }
    public void Var149() { testNullArray( "JDCSNARCH1", "CHAR(1)"); }
    public void Var150() { testNullArray( "JDCSNARCH50", "CHAR(50)"); }
    public void Var151() { testNullArray( "JDCSNARVCH", "VARCHAR(80)"); }
    public void Var152() { testNullArray( "JDCSNARGR", "GRAPHIC(50)"); }
    public void Var153() { testNullArray( "JDCSNARVGR", "VARGRAPHIC(50)"); }
    public void Var154() {
/*
	if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
	    notApplicable("CLOB arrays cannot be support in JDBC (too large)");
	    return;
	}
*/

	testNullArray( "JDCSNARCLO", "CLOB(1M)");
    }
    public void Var155() {
/*
	if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
	    notApplicable("BLOB arrays cannot be support in JDBC (too large)");
	    return;
	}
*/
	testNullArray( "JDCSNARBLO", "BLOB(1M)");
    }
    public void Var156() { testNullArray( "JDCSNARDAT", "DATE"); }
    public void Var157() { testNullArray( "JDCSNARTIM", "TIME"); }
    public void Var158() { testNullArray( "JDCSNARTS", "TIMESTAMP"); }
    public void Var159() { testNullArray( "JDCSNARBY", "BINARY(50)"); }
    public void Var160() { testNullArray( "JDCSNARVBY", "VARBINARY(50)"); }
    public void Var161() {
	if (checkJdbc40()) {
/*
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }
*/
	    testNullArray( "JDCSNARXML", "XML");
	}
    }



    /**
     * Set an ARRAY of time and get array out reversed with all time format properties
     */
    public void Var162() { testTimeCase1(";time format=hms"); }
    public void Var163() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testTimeCase1(";time format=usa"); }
    public void Var164() { testTimeCase1(";time format=iso"); }
    public void Var165() { testTimeCase1(";time format=eur"); }
    public void Var166() { testTimeCase1(";time format=jis"); }
    public void Var167() { testTimeCase1(";time format=hms;time separator=:"); }
    public void Var168() { testTimeCase1(";time format=hms;time separator=."); }
    public void Var169() { testTimeCase1(";time format=hms;time separator=,"); }
    public void Var170() { testTimeCase1(";time format=hms;time separator=b"); }


    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed  with all time format properties
     */

    public void Var171() { testTimeCase2(";time format=hms"); }
    public void Var172() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testTimeCase2(";time format=usa"); }
    public void Var173() { testTimeCase2(";time format=iso"); }
    public void Var174() { testTimeCase2(";time format=eur"); }
    public void Var175() { testTimeCase2(";time format=jis"); }
    public void Var176() { testTimeCase2(";time format=hms;time separator=:"); }
    public void Var177() { testTimeCase2(";time format=hms;time separator=."); }
    public void Var178() { testTimeCase2(";time format=hms;time separator=,"); }
    public void Var179() { testTimeCase2(";time format=hms;time separator=b"); }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)  with all time format properties
     */


    public void Var180() { testTimeCase3(";time format=hms"); }
    public void Var181() { /* if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } */  testTimeCase3(";time format=usa"); }
    public void Var182() { testTimeCase3(";time format=iso"); }
    public void Var183() { testTimeCase3(";time format=eur"); }
    public void Var184() { testTimeCase3(";time format=jis"); }
    public void Var185() { testTimeCase3(";time format=hms;time separator=:"); }
    public void Var186() { testTimeCase3(";time format=hms;time separator=."); }
    public void Var187() { testTimeCase3(";time format=hms;time separator=,"); }
    public void Var188() { testTimeCase3(";time format=hms;time separator=b"); }



    /**
     * Set an ARRAY of date and get array out reversed with all date format properties
     */
    public void Var189() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testDateCase1(";date format=mdy");  }
    public void Var190() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy");  }
    public void Var191() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd");  }
    public void Var192() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=usa");  }
    public void Var193() { testDateCase1(";date format=iso");  }
    public void Var194() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=eur");  }
    public void Var195() { testDateCase1(";date format=jis");  }
    public void Var196() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian");  }
    public void Var197() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=mdy;date separator=/");  }
    public void Var198() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=mdy;date separator=-");  }
    public void Var199() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=mdy;date separator=.");  }
    public void Var200() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=mdy;date separator=,");  }
    public void Var201() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=mdy;date separator=b");  }
    public void Var202() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy;date separator=/");  }
    public void Var203() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy;date separator=-");  }
    public void Var204() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy;date separator=.");  }
    public void Var205() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy;date separator=,");  }
    public void Var206() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=dmy;date separator=b");  }
    public void Var207() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd;date separator=/");  }
    public void Var208() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd;date separator=-");  }
    public void Var209() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd;date separator=.");  }
    public void Var210() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd;date separator=,");  }
    public void Var211() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=ymd;date separator=b");  }
    public void Var212() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian;date separator=/");  }
    public void Var213() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian;date separator=-");  }
    public void Var214() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian;date separator=.");  }
    public void Var215() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian;date separator=,");  }
    public void Var216() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1(";date format=julian;date separator=b");  }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed  with all date format properties
     */

    public void Var217() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy");  }
    public void Var218() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy");  }
    public void Var219() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd");  }
    public void Var220() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=usa");  }
    public void Var221() { testDateCase2(";date format=iso");  }
    public void Var222() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=eur");  }
    public void Var223() { testDateCase2(";date format=jis");  }
    public void Var224() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian");  }
    public void Var225() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy;date separator=/");  }
    public void Var226() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy;date separator=-");  }
    public void Var227() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy;date separator=.");  }
    public void Var228() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy;date separator=,");  }
    public void Var229() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=mdy;date separator=b");  }
    public void Var230() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy;date separator=/");  }
    public void Var231() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy;date separator=-");  }
    public void Var232() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy;date separator=.");  }
    public void Var233() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy;date separator=,");  }
    public void Var234() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=dmy;date separator=b");  }
    public void Var235() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=/");  }
    public void Var236() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=-");  }
    public void Var237() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=.");  }
    public void Var238() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=,");  }
    public void Var239() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=b");  }
    public void Var240() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=/");  }
    public void Var241() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=-");  }
    public void Var242() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=.");  }
    public void Var243() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=,");  }
    public void Var244() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=b");  }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed  with all date format properties
     */

    public void Var245() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy");  }
    public void Var246() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy");  }
    public void Var247() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=ymd");  }
    public void Var248() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=usa");  }
    public void Var249() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=iso");  }
    public void Var250() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=eur");  }
    public void Var251() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=jis");  }
    public void Var252() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=julian");  }
    public void Var253() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy;date separator=/");  }
    public void Var254() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy;date separator=-");  }
    public void Var255() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy;date separator=.");  }
    public void Var256() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy;date separator=,");  }
    public void Var257() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=mdy;date separator=b");  }
    public void Var258() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy;date separator=/");  }
    public void Var259() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy;date separator=-");  }
    public void Var260() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy;date separator=.");  }
    public void Var261() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy;date separator=,");  }
    public void Var262() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=dmy;date separator=b");  }
    public void Var263() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=ymd;date separator=/");  }
    public void Var264() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=ymd;date separator=-");  }
    public void Var265() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=ymd;date separator=.");  }
    public void Var266() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3(";date format=ymd;date separator=,");  }
    public void Var267() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=ymd;date separator=b");  }
    public void Var268() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=/");  }
    public void Var269() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=-");  }
    public void Var270() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=.");  }
    public void Var271() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=,");  }
    public void Var272() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2(";date format=julian;date separator=b");  }

    /* Repeat tests, reusing callable statement  */

    public void Var273() { notApplicable(); }
    public void Var274() { notApplicable(); }
    public void Var275() { notApplicable(); }
    public void Var276() { notApplicable(); }
    public void Var277() { notApplicable(); }
    public void Var278() { notApplicable(); }
    /**
     * getArray() - Set an ARRAY of ints (input only for Rakesh)
     */
    public void Var279()
    {
	if(checkArraySupport()) {

	    try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );


	    ////////create array and set/get data
	    //check local copy of array and arrayResultSet
		Integer[] ia = new Integer[4];
		ia[0] = new Integer(0);
		ia[1] = new Integer(100);
		ia[2] = new Integer(200);
		ia[3] = new Integer(300);
		java.sql.Array a = createArray(connection_,  "INTEGER", ia);


	    //call proc with one input array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
		cs.execute();
		JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
		cs.execute();

		succeeded();

	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception");
	    }
	}
    }

    /**
     * Set an ARRAY of ints and get array out reversed
     * and sanity check
     */
    public void Var280()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );
            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
		if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  ) {
		    sb.append("check1a=false "+ia[retRS.getInt(1)-1].intValue()+" != "+iaCheck[retRS.getInt(1)-1].intValue() +"\n");
		    check1 = false;

		}
		if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) { //column 1 is index (1-based) and column 2 of RS contains actual data
		    sb.append("check1b=false "+iaCheck[retRS.getInt(1)-1].intValue()+" != "+ retRS.getInt(2) +"\n");

		    check1 = false;
		}
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();

            Array b = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)b).getBaseTypeName().equals("INTEGER") ) {
		sb.append("check2=false baseTypeName="+((Array)b).getBaseTypeName()+"\n");
                check2 = true;
	    }




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val

                //check column in resultset is index into array
		if(iaCheck[c1-1].intValue() != c2) { //@arrayrs
		    sb.append("check3=false "+iaCheck[c1-1].intValue() +" != "+c2+"\n");
                    check3 = false;
		}
            }

            assertCondition(check1 & check2 & check3, sb);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRINT2));
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed
     */
    public void Var281()
    {
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


	    StringBuffer sb = new StringBuffer();
	    sb.append(" V7R1 Test for Arrays added by tb 2/26/09 chg 12/05/2011"); 
            ////////create array and set/get data
            Object[] ia = new Object[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();


            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("SMALLINT") )  {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("Base type is "+((Array)out).getBaseTypeName()+" sb SMALLINT");
	    }
            /* smallints are Integers from jdbc 4.0 spec:
            Note : JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
	    Object[] outObjectArray = (Object[]) out.getArray();


	    Object[] outa = outObjectArray;
	    if (outObjectArray[0] instanceof Integer) {
		// No change neede
	    } else if (outObjectArray[0] instanceof Short) {
		System.out.println("Output is Short.. Converting input");
		Integer[] intArray =  (Integer[]) ia;
		ia = new Short[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
		    ia[i] = new Short(intArray[i].shortValue());
		}

	    } else {
		throw new Exception("Unexpected array type for "+outObjectArray);
	    }

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa,sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09  chg 12/05/2011");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     */
    public void Var282()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false; 
            boolean check3 = false; 
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("INTEGER") ) {
                  check1 = true;
	    } else {

        }

        if (out != null) {
          Integer[] outa = (Integer[]) out.getArray();

          ResultSet rsa = out.getResultSet();
          check2 = checkArrayOutputReversed(ia, outa, sb);
          check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
        }
        assertCondition(check1 && check2 && check3,
            " V7R1 Test for Arrays added by tb 2/26/09 " + sb);

      } catch (Exception e) {
        failed(connection_, e,
            "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
      }
  }

   /**
     * Set an ARRAY of BIGint and get array out reversed
     */
    public void Var283()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = new Long( 0);
            ia[1] = new Long( 100);
            ia[2] = new Long( 200);
            ia[3] = new Long( 300);
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BIGINT") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out is null");
		} else {
		    sb.append("base type ("+((Array)out).getBaseTypeName()+") is not BIGINT");
		}
	    }
	    if (out != null) {

		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed
     */
    public void Var284()
    {
	StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 2/26/09 "); 
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = new Float((float)0);
            ia[1] = new Float((float)100.1);
            ia[2] = new Float((float)200.2);
            ia[3] = new Float((float)300.3);
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("REAL") ) {
                check1 = true;
	    } else {
		sb.append("Got base type of "+((Array)out).getBaseTypeName()+" expected REAL\n");
		check1 = false;
	    }


            Float[] outa = (Float[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed
     */
    public void Var285()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );

            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double((double)((float)100.1));
            ia[2] = new Double((double)((float)200.2));
            ia[3] = new Double((double)((float)300.3));
	    String baseTypeName = "FLOAT";
            java.sql.Array a;
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName="DOUBLE";
	    }
	    a = createArray(connection_, baseTypeName, ia);
            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

            if( ((Array)out).getBaseTypeName().equals("DOUBLE") || ((Array)out).getBaseTypeName().equals("FLOAT")) {
                check1 = true;
            } else {
                sb.append("getBaseTypeName returned "+((Array)out).getBaseTypeName()+" should be 'DOUBLE' or 'FLOAT'\n");
                check1 = false;
            }
            ///luw returns array of doubles..
            //jdbc spec says jdbc float  is a double ...turns out the TB is coded this way
            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed
     */
    public void Var286()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double(100.1);
            ia[2] = new Double(200.2);
            ia[3] = new Double(300.3);
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals("DOUBLE")) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DOUBLE'\n");
          check1 = false;
        }


            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed
     */
    public void Var287()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals("DECIMAL")) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DECIMAL'\n");
          check1 = false;
        }


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 \n"+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed
     */
    public void Var288()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );

            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/

            java.sql.Array a;
	    String baseTypeName = "NUMERIC";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		    baseTypeName = "DECIMAL";
	    }
	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals(baseTypeName)) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'NUMERIC'\n");
          check1 = false;
        }


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed
     */
    public void Var289()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") ) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'CHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed
     */
    public void Var290()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'CHAR'\n");
          check1 = false;
        }


            String[] outa = (String[])out.getArray();
            for(int x = 0; x<outa.length; x++)
                ia[x] = ia[x] + "                                               ";

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed
     */
    public void Var291()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") ) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'VARCHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed
     */
    public void Var292()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())

        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a;
	    String baseTypeName = "NCHAR";
	    String outputTypeName = "NCHAR";           // Added 02/13/2012
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName="GRAPHIC";
		outputTypeName = baseTypeName;
	    } else if (isToolboxDriver()) {
		if ( ! isJdbc40()) {
		    outputTypeName="CHAR";
		} else {
		    outputTypeName="NCHAR";
		}
	    }
	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
        if (((Array) out).getBaseTypeName().equals(outputTypeName)) {
          check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be '"+baseTypeName+"'\n");
          check1 = false;
        }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed
     */
    public void Var293()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")
                || ((Array)out).getBaseTypeName().equals("NVARCHAR")
		|| ((Array)out).getBaseTypeName().equals("VARCHAR")) {
                check1 = true;
            } else {
              sb.append("getBaseTypeName returned "
                  + ((Array) out).getBaseTypeName() + " should be 'VARGRAPHIC' or 'NVARCHAR'\n");
              check1 = false;
            }


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed
     */
    public void Var294()
    {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  new JDLobTest.JDTestClob("bbbb");
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'CLOB'\n");
          check1 = false;
        }


            Clob[] outa = (Clob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed
     */
    public void Var295()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] =  new JDLobTest.JDTestBlob("bbbb".getBytes());
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'BLOB'\n");
          check1 = false;
        }


            Blob[] outa = (Blob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var296()
    {
	if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed
     */

    public void testDateCase1reuse(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );
	    sb.append("Connection properties="+connectionProperties+" ");
            reconnect(connectionProperties);
            ////////create array and set/get data
            Date[] ia = new Date[4];
            ia[0] = Date.valueOf("1999-01-01");
            ia[1] =  Date.valueOf("2001-01-29");
            ia[2] =  Date.valueOf("2010-12-01");
            ia[3] =  Date.valueOf("2010-01-01");
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DATE") ) {
                check1 = true;
        } else {
          sb.append("getBaseTypeName returned "
              + ((Array) out).getBaseTypeName() + " should be 'DATE'\n");
          check1 = false;
        }


            Date[] outa = (Date[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 "+sb);
        }

    }
    /**
     * Set an ARRAY of date and get array out reversed
     */
    public void Var297()
    {
	testDateCase1reuse("");
    }



    /**
     * testTime1
     *
     * Set an ARRAY of time and get array out reversed
     */

     public void testTimeCase1reuse(String connectionProperties) {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
            reconnect(connectionProperties);

		assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );

            ////////create array and set/get data
            Time[] ia = new Time[4];
	    if (connectionProperties.indexOf("usa") > 0) {
		// usa does not have seconds
		ia[0] = Time.valueOf("01:01:00");
		ia[1] = Time.valueOf("11:01:00");
		ia[2] = Time.valueOf("01:11:00");
		ia[3] = Time.valueOf("23:13:00");
	    } else {
		ia[0] = Time.valueOf("01:01:01");
		ia[1] = Time.valueOf("11:01:00");
		ia[2] = Time.valueOf("01:11:01");
		ia[3] = Time.valueOf("23:13:11");
	    }
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIME") )
                check1 = true;


            Time[] outa = (Time[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/26/11 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - properties="+connectionProperties+" V7R1 Test for Arrays updated  2/26/11 ");
        }

     }

    /**
     * Set an ARRAY of time and get array out reversed
     */
    public void Var298()
    {
	testTimeCase1reuse("");
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed
     */
    public void Var299()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = Timestamp.valueOf("2001-01-01 01:01:01.000001");
            ia[1] = Timestamp.valueOf("2001-02-03 04:05:06.000007");
            ia[2] = Timestamp.valueOf("2010-11-12 13:14:15.000016");
            ia[3] = Timestamp.valueOf("2000-05-16 07:08:09.000010");
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


            Timestamp[] outa = (Timestamp[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed
     */
    public void Var300()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb".getBytes();
            ia[2] = "cccccccccccccccccccccccccccccccccccccccccccccccccc".getBytes();
            ia[3] = "dddddddddddddddddddddddddddddddddddddddddddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BINARY") )
                check1 = true;


            byte[][] outa = ( byte[][])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed
     */
    public void Var301()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbb".getBytes();
            ia[2] = "cccccccccc".getBytes();
            ia[3] = "dddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
                check1 = true;


            byte[][] outa = ( byte[][])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed
     */
    public void Var302()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );
            reconnect();
            ////////create array and set/get data
            Object sqlxml1  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml1, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>aaaa</name>");
            Object sqlxml2  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml2, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>bbbb</name>");
            Object sqlxml3  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml3, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>cccc</name>");
            Object sqlxml4  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml4, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>dddd</name>");

            Object[] ia = new Object[4];
            ia[0] = sqlxml1;
            ia[1] =  sqlxml2;
            ia[2] = sqlxml3;
            ia[3] =  sqlxml4;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }

    /**
     * Set an ARRAY of integer in java STP get array out reversed
     */
    public void Var303()
    {
	if (checkArraySupport())
        try{


            succeeded();
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of int  and non-array int
     */
    public void Var304()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN2 );

            reconnect();
            ////////create array and set/get data
            Integer[] ia1 = new Integer[4];
            ia1[0] = new Integer( 0);
            ia1[1] = new Integer(1);
            ia1[2] = new Integer(2);
            ia1[3] = new Integer(3);
            java.sql.Array a1 = createArray(connection_, "INTEGER", ia1);

            Integer[] ia2 = new Integer[4];
            ia2[0] = new Integer( 0);
            ia2[1] = new Integer(100);
            ia2[2] = new Integer(200);
            ia2[3] = new Integer(300);
            java.sql.Array a2 = createArray(connection_, "INTEGER", ia2);

            //call proc   (in array, in int, inout array, inout int, out array, out int)
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN2 + " ( ?,?,?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setInt(2, 222);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P3", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P4", java.sql.Types.INTEGER);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setInt(4, 444);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P5", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P6", java.sql.Types.INTEGER);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setInt(2, 222);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setInt(4, 444);

            cs.execute();

            Array outa1 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","P3");
            Array outa2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","P5");
            int outi1 = cs.getInt("P4"); //222
            int outi2 = cs.getInt("P6"); //444

            boolean check1 = false;
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)outa1).getBaseTypeName().equals("INTEGER") ) {
                check1 = true;
	    } else {
		check1 = false;
		sb.append("check1 is false getBastTypeName() = "+((Array)outa1).getBaseTypeName()+"\n");
	    }
	    if( ((Array)outa2).getBaseTypeName().equals("INTEGER") ) {
                check2 = true;
	    } else {
		check2 = false;
		sb.append("check2 is false getBastTypeName() = "+((Array)outa2).getBaseTypeName()+"\n");
	    }


            Integer[] outa1a = (Integer[])outa1.getArray();
            Integer[] outa2a = (Integer[])outa2.getArray();

            ResultSet rsa1 = outa1.getResultSet();
            ResultSet rsa2 = outa2.getResultSet();
            boolean check3 = checkArrayOutput(ia1, outa1a, sb);
            boolean check4 = checkArrayResultSetOutput(ia1, rsa1, sb);
            boolean check5 = checkArrayOutput(ia2, outa2a, sb);
            boolean check6 = checkArrayResultSetOutput(ia2, rsa2, sb);



            assertCondition(outi1 == 222 && outi2 == 444 && check1 && check2 && check3 &&check4 && check5 && check6, " V7R1 Test for Arrays added by tb 2/26/09\n outi1="+outi1+" sb 222\n"+
			    "outi2 = "+outi2+" sb 444\n"+sb.toString());


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varchar and non-array varchar
     */
    public void Var305()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH2 );
            reconnect();

            ////////create array and set/get data
            String[] ia1 = new String[4];
            ia1[0] = "aaa";
            ia1[1] = "bbb";
            ia1[2] = "ccc";
            ia1[3] = "ddd";
            java.sql.Array a1 = createArray(connection_, "VARCHAR", ia1);

            String[] ia2 = new String[4];
            ia2[0] = "a2";
            ia2[1] = "bb2";
            ia2[2] = "ccc2";
            ia2[3] = "dddd2";
            java.sql.Array a2 = createArray(connection_, "VARCHAR", ia2);



            //call proc   (in array, in int, inout array, inout int, out array, out int)
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH2 + " ( ?,?,?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setString("P2", "222");
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P3", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P4", java.sql.Types.VARCHAR);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setString("P4", "444");
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P5", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P6", java.sql.Types.VARCHAR);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            cs.setString("P2", "222");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setString("P4", "444");
            cs.execute();



            Array outa1 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray",3);
            Array outa2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray",5);
            String outi1 = cs.getString(4); //222
            String outi2 = cs.getString(6); //444

            boolean check1 = false;
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)outa1).getBaseTypeName().equals("VARCHAR") ) {
                check1 = true;
	    } else {
		check1 = false;
		sb.append("check1 = false getBaseTypeName="+((Array)outa1).getBaseTypeName());
	    }
	    if( ((Array)outa2).getBaseTypeName().equals("VARCHAR") ) {
                check2 = true;
	    } else {
		check2 = false;
		sb.append("check2 = false getBaseTypeName="+((Array)outa2).getBaseTypeName());
	    }


            String[] outa1a = (String[])outa1.getArray();
            String[] outa2a = (String[])outa2.getArray();

            ResultSet rsa1 = outa1.getResultSet();
            ResultSet rsa2 = outa2.getResultSet();
            boolean check3 = checkArrayOutput(ia1, outa1a, sb);
            boolean check4 = checkArrayResultSetOutput(ia1, rsa1, sb);
            boolean check5 = checkArrayOutput(ia2, outa2a, sb);
            boolean check6 = checkArrayResultSetOutput(ia2, rsa2, sb);



            assertCondition(outi1.equals("222") && outi2.equals("444") && check1 && check2 && check3 &&check4 && check5 && check6, " V7R1 Test for Arrays added by tb 2/26/09 \n"+
			    "outi1 = "+outi1+" sb 222\n"+
			    "outi2 = "+outi2+" sb 444\n"+
			    sb.toString());


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of INTEGERS converted to VARCHAR and get array out reversed
     */
    public void Var306()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??
            reconnect();
            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            String[] ias = new String[4];
            ias[0] = "0";
            ias[1] = "100";
            ias[2] = "200";
            ias[3] = "300";
            java.sql.Array a = createArray(connection_, "INTEGER", ia);


            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) "); //proc is array of VARCHAR(50) -pass in INT array
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a); //pass in array of INTs
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a); //pass in array of INTs
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ias, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ias, rsa, sb);

            cs.close();
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09  INTEGER converted to VARCHAR "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09  INTEGER converted to VARCHAR ");
        }
    }


    /**
     * Set an ARRAY of Strings passed into proc of array ints and get array out reversed
     */
    public void Var307()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN);

            reconnect();
            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            String[] ias = new String[4];
            ias[0] = "0";
            ias[1] = "100";
            ias[2] = "200";
            ias[3] = "300";
            java.sql.Array a = createArray(connection_, "VARCHAR", ias);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a); //pass in array of INTs
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 VARCHAR to Int conversion "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 VARCHAR to Int conversion  ");
        }
    }

    /**
     * getArray() - Set an ARRAY of VARCHARS and nonarray VARCHAR (input only for Rakesh)
     */
    public void Var308()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH3 );
            reconnect();

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            String[] ias = new String[4];
            ias[0] = "aaaaaaaa";
            ias[1] = "bbbbbbbb";
            ias[2] = "cccccccc";
            ias[3] = "dddddddd";
            java.sql.Array a = createArray(connection_, "VARCHAR", ias);

            //call proc with one input array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH3 + " (?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);
            cs.setString("P2", "yyyyyyyy");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "zzzzzzzz");
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);
            cs.setString("P2", "yyyyyyyy");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "zzzzzzzz");
            cs.execute();


            assertCondition(true, sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * getArray() - Set an ARRAY of INTS in proc of array VARCHARS (input only for Rakesh)
     *
     */
    public void Var309()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH3 );
            reconnect();
            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_,  "INTEGER", ia);


            //call proc with one input array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH3 + " (?, ?,?,?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);//!!!pass in array of INTS which will convert to STRINGS
            cs.setString("P2", "aaa");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "bbb");
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a);//!!!pass in array of INTS which will convert to STRINGS
            cs.setString("P2", "aaa");
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a);
            cs.setString("P4", "bbb");
            cs.execute();

            assertCondition(true, sb);

        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception CONVERT INT TO VARCHAR ");
        }
    }


    /**
     * getArray() - Set an ARRAY of VARCHARS into proc of array-INTS (input only for Rakesh)
     */
    public void Var310()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport()) {
	    try {
		assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
	        reconnect();
	    ////////create array and set/get data
	    //check local copy of array and arrayResultSet
		String[] ias = new String[4];
		ias[0] = "0";
		ias[1] = "1";
		ias[2] = "2";
		ias[3] = "3";
		java.sql.Array a = createArray(connection_, "VARCHAR", ias);


	    //call proc with one input array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
		cs.execute();

		JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
		cs.execute();

               assertCondition(true, sb);

	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception CONVERT VARCHAR TO INT");
	    }
	}
    }

    /**
     * getArray() - Set an ARRAY of all null values
     */
    public void Var311()
    {
    if (checkArraySupport()) {
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
            reconnect();
        ////////create array and set/get data
        //check local copy of array and arrayResultSet
        String[] ias = new String[4];
        ias[0] = null;
        ias[1] = null;
        ias[2] = null;
        ias[3] = null;
        java.sql.Array a = createArray(connection_, "VARCHAR", ias);


        //call proc with one input array
        CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();

        succeeded();

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception CONVERT VARCHAR TO INT");
        }
    }
    }

    /**
     * getArray() - Set an ARRAY of some null values
     */
    public void Var312()
    {
    if (checkArraySupport()) {
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT3 );
            reconnect();
        ////////create array and set/get data
        //check local copy of array and arrayResultSet
        String[] ias = new String[4];
        ias[0] = "0";
        ias[1] = null;
        ias[2] = "2";
        ias[3] = null;
        java.sql.Array a = createArray(connection_, "VARCHAR", ias);


        //call proc with one input array
        CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT3 + " (?) ");
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();
        JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
        cs.execute();

        succeeded();

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception  CONVERT VARCHAR TO INT");
        }
    }
    }

    /**
     * getArray() - Set/get a null int ARRAY
     */
    public void Var313()
    {
    if (checkArraySupport()) {
        try {
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINTN );
            reconnect();
        //call proc with one input array
        CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINTN + " (?,?) ");
        cs.setObject("NUMLISTIN", null);
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
        cs.execute();
        Array aout = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
        boolean check1 = false;
        if(cs.wasNull() && aout == null)
            check1 = true;

        cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINTN + " (?,?) ");
        cs.setNull(1, java.sql.Types.ARRAY);
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
        cs.execute();

        cs.setNull("NUMLISTIN", java.sql.Types.ARRAY);
        cs.execute();

        Array aout2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
        boolean check2 = false;
        if(cs.wasNull() && aout2 == null)
            check2 = true;

        assertCondition(check1 && check2, "null array parms failed");

        }catch(Exception e){
        failed(connection_, e, "Unexpected Exception");
        }
    }
    }


    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed
     */

    public void testDateCase2repeat(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT);
	    sb.append("ConnectionProperties="+connectionProperties+" ");
            reconnect(connectionProperties);

            ////////create array and set/get data
            JDCSSetArrayDate[] ia = new JDCSSetArrayDate[4];
            ia[0] = new JDCSSetArrayDate(1999, 1, 1);
            ia[1] =  new JDCSSetArrayDate(2001, 1, 29);
            ia[2] =  new JDCSSetArrayDate(2010, 12, 1);
            ia[3] =  new JDCSSetArrayDate(2010, 1, 1);
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DATE") )
                check1 = true;


            Date[] outa = (Date[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays updated 2/25/2011 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays updated 2/25/2011 "+sb);
        }

    }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed
     */
    public void Var314()
    {
	testDateCase2repeat("");

    }


    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed
     */

    public void testTimeCase2repeat(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );
            reconnect(connectionProperties);

            ////////create array and set/get data
            JDCSSetArrayTime[] ia = new JDCSSetArrayTime[4];
            ia[0] = new JDCSSetArrayTime(1,1,1);
            ia[1] = new JDCSSetArrayTime(11,1,0);
            ia[2] = new JDCSSetArrayTime(1,11,1);
	    if (connectionProperties.indexOf("usa") > 0) {
		// Use does not have seconds
		ia[0] = new JDCSSetArrayTime(1,1,0);
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = new JDCSSetArrayTime(1,11,0);
		ia[3] = new JDCSSetArrayTime(23,13,00);
	    } else {
		ia[0] = new JDCSSetArrayTime(1,1,1);
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = new JDCSSetArrayTime(1,11,1);
		ia[3] = new JDCSSetArrayTime(23,13,11);
	    }
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIME") )
                check1 = true;


            Time[] outa = (Time[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/25/2011 ");




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - connectionProperties="+connectionProperties+" V7R1 Test for Arrays updated 2/25/2011 ");
        }

    }

    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed
     */
    public void Var315()
    {
	testTimeCase2repeat("");
    }



    /**
     * Set an ARRAY of JDCSSetArrayTimestamp and get array out reversed
     */
    public void Var316()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );

            reconnect();
            ////////create array and set/get data
            JDCSSetArrayTimestamp[] ia = new JDCSSetArrayTimestamp[4];
            ia[0] = new JDCSSetArrayTimestamp(1,1,1,1,1,1,1);
            ia[1] = new JDCSSetArrayTimestamp(1,2,3,4,5,6,7);
            ia[2] = new JDCSSetArrayTimestamp(10,11,12,13,14,15,16);
            ia[3] = new JDCSSetArrayTimestamp(100,5,6,7,8,9,10);
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


            Timestamp[] outa = (Timestamp[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by native 5/4/09 ");




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by native 5/4/09 ");
        }
    }



    /**
     * Set an ARRAY of JDCSSetArrayBigDecimal and get array out reversed
     */
    public void Var317()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );

            reconnect();
            ////////create array and set/get data
            JDCSSetArrayBigDecimal[] ia = new JDCSSetArrayBigDecimal[4];
            ia[0] = new JDCSSetArrayBigDecimal("0.00000");
            ia[1] = new JDCSSetArrayBigDecimal("100.10000");
            ia[2] = new JDCSSetArrayBigDecimal("200.20000");
            ia[3] = new JDCSSetArrayBigDecimal("300.30000");
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of JDTestClob and get array out reversed
     */
    public void Var318()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );

            reconnect();
            ////////create array and set/get data
            JDLobTest.JDTestClob[] ia = new JDLobTest.JDTestClob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  new JDLobTest.JDTestClob("bbbb");
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") )
                check1 = true;


            Clob[] outa = (Clob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of JDLobTest.JDTestBlob and get array out reversed
     */
    public void Var319()
    {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            JDLobTest.JDTestBlob[] ia = new JDLobTest.JDTestBlob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] =  new JDLobTest.JDTestBlob("bbbb".getBytes());
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") )
                check1 = true;


            Blob[] outa = (Blob[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int input and  array out
     */
    public void Var320()
    {
        StringBuffer sb = new StringBuffer();
	sb.append(" V7R1 Test for Arrays added by tb 8/26/09 "); 
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT4 );
            reconnect();
            Integer[] ia = {
		new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6)};

            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT4 + " ( ?, ?) ");
            cs.setInt(1, 2);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLIST", java.sql.Types.ARRAY);
            cs.execute();

            cs.setInt(1, 1);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLIST");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutput(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutput(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 8/26/09 ");
        }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (null elements)
     * and sanity check
     */
    public void Var321()
    {
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = null;
            ia[2] = new Integer(200);
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1] == null){
                    if( ia[retRS.getInt(1)-1] != iaCheck[retRS.getInt(1)-1] ) //check nulls
                    check1 = false;
                }
                else if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  )
                    check1 = false;

                if(iaCheck[retRS.getInt(1)-1] == null){
                    retRS.getInt(2);
                    if(  retRS.wasNull() == false ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
                 }
                else if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();

            Array b = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)b).getBaseTypeName().equals("INTEGER") )
                check2 = true;




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val

                //check column in resultset is index into array
                if(iaCheck[c1-1] == null) //@arrayrs
                {
                    if(retRS.wasNull() == false)
                        check3 = false;
                }
                else if(iaCheck[c1-1].intValue() != c2) //@arrayrs
                    check3 = false;
            }

            assertCondition(check1 & check2 & check3);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (null elements)
     */
    public void Var322()
    {
      StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer(0);
            ia[1] = new Integer(100);
            ia[2] = null;
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();


            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
                check1 = true;
            /* smallints are Integers from jdbc 4.0 spec:
            Note  The JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (null elements)
     */
    public void Var323()
    {
      StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = new Integer( 0);
            ia[1] = new Integer(100);
            ia[2] = null;
            ia[3] = new Integer(300);
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();


            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


            Integer[] outa = (Integer[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (null elements)
     */
    public void Var324()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = new Long( 0);
            ia[1] = null;
            ia[2] = new Long( 200);
            ia[3] = new Long( 300);
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BIGINT") )
                check1 = true;


            Long[] outa = (Long[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (null elements)
     */
    public void Var325()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = null;
            ia[1] = new Float((float)100.1);
            ia[2] = new Float((float)200.2);
            ia[3] = new Float((float)300.3);
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("REAL") )
                check1 = true;


            Float[] outa = (Float[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (null elements)
     */
    public void Var326()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );

            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = new Double((double)((float)100.1));
            ia[2] = new Double((double)((float)200.2));
            ia[3] = new Double((double)((float)300.3));
            java.sql.Array a;
	    String baseTypeName = "FLOAT";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "DOUBLE";
	    }

	    a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

            if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
                check1 = true;

            ///luw returns array of doubles..
            //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way
            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (null elements)
     */
    public void Var327()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = new Double(0);
            ia[1] = new Double(100.1);
            ia[2] = new Double(200.2);
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
                check1 = true;


            Double[] outa = (Double[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    assertCondition(check1 && check2 && check3,
			    " check 1 = "+check1 +
			    " check 2 = "+check2 +
			    " check 3 = "+check3 +
			    " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (null elements)
     */
    public void Var328()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = new BigDecimal("0.00000");
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (null elements)
     */
    public void Var329()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );

            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = new BigDecimal("100.10000");
            ia[2] = new BigDecimal("200.20000");
            ia[3] = new BigDecimal("300.30000");
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/
	    String baseTypeName = "NUMERIC";
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "DECIMAL";
	    }
            java.sql.Array a = createArray(connection_, baseTypeName, ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals(baseTypeName) )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (null elements)
     */
    public void Var330()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = "d";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (null elements)
     */
    public void Var331()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = null;
            ia[2] = "ccc";
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;


            String[] outa = (String[])out.getArray();

            for(int x = 0; x<ia.length; x++)
                if(ia[x] != null)
                    ia[x] = ia[x] + "                                               ";


            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (null elements)
     */
    public void Var332()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = "ccc";
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (null elements)
     */
    public void Var333()
    {
      StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "a";
            ia[1] = "b";
            ia[2] = "c";
            ia[3] = null;
	    String baseTypeName = "NCHAR";
	    String outputTypeName = baseTypeName;
	    if (getDriver() == JDTestDriver.DRIVER_JCC) {
		baseTypeName = "GRAPHIC";
	    } else 	    if (isToolboxDriver()) {
		if ( ! isJdbc40()) {
		    outputTypeName="CHAR";
		} else {
		    outputTypeName="NCHAR";
		}
	    }
            java.sql.Array a;
            a = createArray(connection_, baseTypeName, ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals(outputTypeName) ) {
		check1 = true;
	    }


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (null elements)
     */
    public void Var334()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = "aaa";
            ia[1] = "bbb";
            ia[2] = null;
            ia[3] = "ddd";
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR")  || ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (null elements)
     */
    public void Var335()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = new JDLobTest.JDTestClob("aaaa");
            ia[1] =  null;
            ia[2] =  new JDLobTest.JDTestClob("cccc");
            ia[3] =  new JDLobTest.JDTestClob("dddd");
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CLOB") )
                check1 = true;


		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (null elements)
     */
    public void Var336()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = new JDLobTest.JDTestBlob("aaaa".getBytes());
            ia[1] = null;
            ia[2] =  new JDLobTest.JDTestBlob("cccc".getBytes());
            ia[3] =  new JDLobTest.JDTestBlob("dddd".getBytes());
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BLOB") )
                check1 = true;


		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var337()
    {
    if (checkArraySupport())
        succeeded();
    }

    public void testDateCase3repeat(String connectionProperties) {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
	    try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );
		sb.append("ConnectionProperties="+connectionProperties+" ");
		reconnect(connectionProperties);
	    ////////create array and set/get data
		Date[] ia = new Date[4];
		ia[0] = Date.valueOf("1999-01-01");
		ia[1] =  null;
		ia[2] =  Date.valueOf("2010-12-01");
		ia[3] =  Date.valueOf("2010-01-01");
		java.sql.Array a = createArray(connection_, "DATE", ia);

	    //call proc to revers 4 elements in array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
		JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
		cs.execute();
		JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
		cs.execute();

		Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
		boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;
    if (out != null) {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("DATE") )
		    check1 = true;


		    Date[] outa = (Date[])out.getArray();

		    ResultSet rsa = out.getResultSet();
		    check2 = checkArrayOutputReversed(ia, outa, sb);
		    check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
		}
		assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays update 2/26/2011 "+sb);




	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays updated 2/26/2011 "+sb);
	    }

    }
    /**
     * Set an ARRAY of date and get array out reversed (null elements)
     */
    public void Var338()
    {
	testDateCase3repeat("");
    }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)
     */

    public void testTimeCase3repeat(String connectionProperties) {

	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
	    try{
		assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );
		reconnect(connectionProperties);

	    ////////create array and set/get data
		Time[] ia = new Time[4];
		ia[0] = null;
		ia[1] = new JDCSSetArrayTime(11,1,0);
		ia[2] = null;
		if (connectionProperties.indexOf("usa") > 0) {
		    // Usa does not have seconds
		    ia[3] = new JDCSSetArrayTime(23,13,00);
		} else {
		    ia[3] = new JDCSSetArrayTime(23,13,11);
		}
		java.sql.Array a = createArray(connection_, "TIME", ia);

	    //call proc to revers 4 elements in array
		CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
		JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
		JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
		cs.execute();

		Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
		boolean check1 = false;
    boolean check2 = false;
    boolean check3 = false;
    if (out != null) {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIME") )
		    check1 = true;


		    Time[] outa = (Time[])out.getArray();

		    ResultSet rsa = out.getResultSet();
		    check2 = checkArrayOutputReversed(ia, outa, sb);
		    check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
		}
		assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays connectionProperties="+connectionProperties+" updated 2/26/11 "+sb);


	    }catch(Exception e){
		failed(connection_, e, "Unexpected Exception - connectionProperties="+connectionProperties+" V7R1 Test for Arrays updated  2/26/11 ");
	    }

    }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)
     */
    public void Var339()
    {
	testTimeCase3repeat("");

    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (null elements)
     */
    public void Var340()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = null;
            ia[1] = new JDCSSetArrayTimestamp(1,2,3,4,5,6,7);
            ia[2] = new JDCSSetArrayTimestamp(10,11,12,13,14,15,16);
            ia[3] = new JDCSSetArrayTimestamp(100,5,6,7,8,9,10);
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
                check1 = true;


		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (null elements)
     */
    public void Var341()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa".getBytes();
            ia[1] = null;
            ia[2] = "cccccccccccccccccccccccccccccccccccccccccccccccccc".getBytes();
            ia[3] = "dddddddddddddddddddddddddddddddddddddddddddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BINARY") )
                check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (null elements)
     */
    public void Var342()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = "aaaaaaaaaa".getBytes();
            ia[1] = "bbbbbbbbbb".getBytes();
            ia[2] = null;
            ia[3] = "dddddddddd".getBytes();
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
                check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (null elements)
     */
    public void Var343()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		     return;
	    }
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );

            reconnect();
            ////////create array and set/get data
            Object sqlxml1  = null;
            Object sqlxml2  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml2, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>bbbb</name>");
            Object sqlxml3  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml3, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>cccc</name>");
            Object sqlxml4  =  JDReflectionUtil.callMethod_O(connection_, "createSQLXML");
            JDReflectionUtil.callMethod_V(sqlxml4, "setString", "<?xml version=\"1.0\" ?>\n"+
                                      "<name>dddd</name>");

            Object[] ia = new Object[4];
            ia[0] = sqlxml1;
            ia[1] =  sqlxml2;
            ia[2] = sqlxml3;
            ia[3] =  sqlxml4;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


		Object[] outa = (Object[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }


    /**
     * Set an ARRAY of ints and get array out reversed (All null elements)
     * and sanity check
     */
    public void Var344()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1] == null){
		    if( ia[retRS.getInt(1)-1] != iaCheck[retRS.getInt(1)-1] ) { //check nulls
			check1 = false;
			sb.append("null values not matching for row (1-based) "+retRS.getInt(1)+"\n");
		    }
		} else {

		    if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  ) {
			sb.append("ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  "+ia[retRS.getInt(1)-1].intValue() +" !=  "+iaCheck[retRS.getInt(1)-1].intValue()+" \n");
			check1 = false;
		    }
		    if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
		    {
			check1 = false;
			sb.append("iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2) "+iaCheck[retRS.getInt(1)-1].intValue()+"!="+retRS.getInt(2)+"\n") ;
		    }
		}
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();

            Array b = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)b).getBaseTypeName().equals("INTEGER") ) {
                check2 = true;
            } else {
                check2 = false;
                sb.append("Got baseTypeName of "+((Array)b).getBaseTypeName()+ " expected INTEGER\n");
            }




            //reverse input array and verify that proc reversed also
            Integer tmp0 = iaCheck[0];
            Integer tmp1 = iaCheck[1];
            iaCheck[0] = iaCheck[3];
            iaCheck[1] = iaCheck[2];
            iaCheck[2] = tmp1;
            iaCheck[3] = tmp0;

            boolean check3 = true;
            retRS = b.getResultSet();
            while(retRS.next()){
                int c1 = retRS.getInt(1); //index
                int c2 = retRS.getInt(2); //val
		boolean wasNull = retRS.wasNull();

                //check column in resultset is index into array
		if (iaCheck[c1-1] == null) {
		    if (!wasNull) {
   		      sb.append("iaCheck[c1-1] == null when c1="+c1+" and c2="+c2);
		      check3 = false;
		    }
		} else {
		    if(iaCheck[c1-1].intValue() != c2) //@arrayrs
			check3 = false;
		}
            }

            assertCondition(check1 & check2 & check3, sb);
        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (ALL NULL elements)
     */
    public void Var345()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
                check1 = true;
            /* smallints are Integers from jdbc 4.0 spec:
            Note  The JDBC 1.0 specification defined the Java object mapping for the
            SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
            include the Byte and Short data types when the JDBC 1.0 specification was
            finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
            preserve backwards compatibility.*/
		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (ALL NULL elements)
     */
    public void Var346()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("INTEGER") )
                check1 = true;


		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (ALL NULL elements)
     */
    public void Var347()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("BIGINT") )
                check1 = true;



		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (ALL NULL elements)
     */
    public void Var348()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            if (out != null) {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("REAL") )
                check1 = true;



		Float[] outa = (Float[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (ALL NULL elements)
     */
    public void Var349()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "FLOAT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

	    if( ((Array)out).getBaseTypeName().equals("DOUBLE") ) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("BASE type is "+((Array)out).getBaseTypeName()+" sb DOUBLE\n");
	    }

            ///luw returns array of doubles..
            //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way
	    boolean check2 = false;
	    boolean check3 = false;

		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (ALL NULL elements)
     */
    public void Var350()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)out).getBaseTypeName().equals("DOUBLE") ) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("BASE type is "+((Array)out).getBaseTypeName()+" sb DOUBLE\n");
	    }
	    boolean check2 = false;
	    boolean check3 = false;

		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (ALL NULL elements)
     */
    public void Var351()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;

		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (ALL NULL elements)
     */
    public void Var352()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            /*
            ia[0] = new BigDecimal(0.00000);
            ia[1] = new BigDecimal(100.12345);
            ia[2] = new BigDecimal(200.00000);
            ia[3] = new BigDecimal(300.99999);*/
            java.sql.Array a = createArray(connection_, "NUMERIC", ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NUMERIC") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;
		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (ALL NULL elements)
     */
    public void Var353()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (ALL NULL elements)
     */
    public void Var354()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;


		String[] outa = (String[])out.getArray();



		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (ALL NULL elements)
     */
    public void Var355()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (ALL NULL elements)
     */
    public void Var356()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "GRAPHIC", ia);
            a = createArray(connection_, "NCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NCHAR")  || ((Array)out).getBaseTypeName().equals("CHAR") )
                check1 = true;

	    boolean check2 = false;
	    boolean check3 = false;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (ALL NULL elements)
     */
    public void Var357()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR );


            ////////create array and set/get data
            String[] ia = new String[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR") || ((Array)out).getBaseTypeName().equals("VARCHAR") )) {
                 check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out is null");
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb VARGRAPHIC or NVARCHAR");

		}
	    }


	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (ALL NULL elements)
     */
    public void Var358()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO );
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	   // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("CLOB") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCLO));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb CLOB");

		}
	    }
	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null)  {
		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (ALL NULL elements)
     */
    public void Var359()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO );

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null &&  ((Array)out).getBaseTypeName().equals("BLOB") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBLO));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb BLOB");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var360()
    {
    if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed (ALL NULL elements)
     */
    public void Var361()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT );

            reconnect();
            ////////create array and set/get data
            Date[] ia = new Date[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	   // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null &&  ((Array)out).getBaseTypeName().equals("DATE") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDAT));

		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb DATE");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {

		Date[] outa = (Date[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of time and get array out reversed  (ALL NULL elements)
     */
    public void Var362()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM );
            reconnect();

            ////////create array and set/get data
            Time[] ia = new Time[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("TIME") ) {
		check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTIM));
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb TIME");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {
		Time[] outa = (Time[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (ALL NULL elements)
     */
    public void Var363()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS );
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if(out != null &&  ((Array)out).getBaseTypeName().equals("TIMESTAMP") ) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTS));
		}  else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb TIMESTAMP");

		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;
	    if (out != null) {
		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (ALL NULL elements)
     */
    public void Var364()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BINARY")) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBY));
		} else {
		    sb.append("baseTypeName is "+((Array)out).getBaseTypeName()+" sb BINARY");
		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;


	    if (out != null) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (ALL NULL elements)
     */
    public void Var365()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[4][10];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("VARBINARY") )  {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
		} else {
		    sb.append("baseType="+((Array)out).getBaseTypeName()+" expected VARBINARY");
		}
	    }
	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (ALL NULL elements)
     */
    public void Var366()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML );
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }

            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
            reconnect();
            ////////create array and set/get data


            Object[] ia = new Object[4];
            ia[0] = null;
            ia[1] = null;
            ia[2] = null;
            ia[3] = null;
            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (0 len array)
     * and sanity check
     */
    public void Var367()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  ) {
                    check1 = false;
                    sb.append("check1 failed comparing intValue");
                }

                if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute(); //expect error since proc access array index
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();


            failed("Did not get error as expected check1="+check1);
        }catch(Exception e){
	    String message = e.getMessage();
	    if (message == null) message = "No Message:  class is "+e.getClass().getName();
	    boolean passed = message.indexOf("not valid for array subscript") != -1;
	    if (!passed) {
		passed = message.indexOf("20439") != -1;
	    }
	    if (!passed) {
		e.printStackTrace();
	    }
            assertCondition(passed, "Incorrect error.  Expected error since access past array length");
        }
    }

    /**
     * Set an ARRAY of ints and get array out reversed (0 len array)
     * and sanity check
     */
    public void Var368()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{

	    assureProcedureExists(JDSetupProcedure.STP_CSARRINT2 );

            ////////create array and set/get data
            //check local copy of array and arrayResultSet
            boolean check1 = true;
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            java.sql.ResultSet retRS = a.getResultSet();
            Integer[] iaCheck = (Integer[]) a.getArray();
            //iaCheck and ia should contian same here

            //do a couple of pre-sanity checks to see if input array values are still same out output from Array.getResultSet values
            while(retRS.next()){
                //first column in resultset is index into array
                if(ia[retRS.getInt(1)-1].intValue() != iaCheck[retRS.getInt(1)-1].intValue()  )
                    check1 = false;

                if(iaCheck[retRS.getInt(1)-1].intValue() != retRS.getInt(2)  ) //column 1 is index (1-based) and column 2 of RS contains actual data
                    check1 = false;
            }

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRINT2 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();

            failed("Did not get error as expected check1="+check1+" "+sb.toString());
        }catch(Exception e){


	    String message = e.getMessage();
	    if (message == null) message = "No Message:  class is "+e.getClass().getName();
	    boolean passed = message.indexOf("not valid for array subscript") != -1;
	    if (!passed) {
		passed = message.indexOf("20439") != -1;
	    }

	    if (!passed) {
		e.printStackTrace();
	    }
            assertCondition(passed,  "Incorrect Error.  Expected error since access past array length");
        }
    }

    /**
     * Set an ARRAY of smallint and get array out reversed (0 len array)
     */
    public void Var369()
    {
	StringBuffer sb = new StringBuffer();
        if(checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRSIN );


            ////////create array and set/get data
            Object[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "SMALLINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRSIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRSIN));

	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("SMALLINT") )
		    check1 = true;
	    /* smallints are Integers from jdbc 4.0 spec:
	    Note  The JDBC 1.0 specification defined the Java object mapping for the
	    SMALLINT and TINYINT JDBC types to be Integer. The Java language did not
	    include the Byte and Short data types when the JDBC 1.0 specification was
	    finalized. The mapping of SMALLINT and TINYINT to Integer is maintained to
	    preserve backwards compatibility.*/
		Object[] outObjectArray = (Object[]) out.getArray();
	    Object [] outa = outObjectArray;
	    String arrayClassName = outObjectArray.getClass().getComponentType().getName();
	    if (arrayClassName.equals("java.lang.Integer")) {
		// No fix needed
	    } else if (arrayClassName.equals("java.lang.Short")) {
		System.out.println("Output is Short.. Converting input");
		Integer[] intArray =  (Integer[]) ia;
		ia = new Short[intArray.length];
		for (int i = 0; i < intArray.length; i++) {
		    ia[i] = new Short(intArray[i].shortValue());
		}

	    } else {
		throw new Exception("Unexpected array type for "+arrayClassName);
	    }



		ResultSet rsa = out.getResultSet();
		 check2 = checkArrayOutputReversed(ia, outa, sb);
		 check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of int and get array out reversed
     * (0 len array)
     */
    public void Var370()
    {
	StringBuffer sb = new StringBuffer();
        if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN );


            ////////create array and set/get data
            Integer[] ia = new Integer[0];

            java.sql.Array a = createArray(connection_, "INTEGER", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();


            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRIN));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("INTEGER") )
		    check1 = true;


		Integer[] outa = (Integer[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of BIGint and get array out reversed
     * (0 len array)
     */
    public void Var371()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBIN );


            ////////create array and set/get data
            Long[] ia = new Long[0];

            java.sql.Array a = createArray(connection_, "BIGINT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBIN + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
 	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBIN));

	    } else {
	  // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("BIGINT") )
		    check1 = true;


		Long[] outa = (Long[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of real and get array out reversed (0 len array)
     */
    public void Var372()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRREA );


            ////////create array and set/get data
            Float[] ia = new Float[0];

            //???real is valid??(it is a i5 type, but luw does not seem to like it)
            //C.createArrayOf("REAL) returns "INTEGER" as type flag (no big deal)
            //but when selected, the type is "REAL"   probably luw bug
            java.sql.Array a = createArray(connection_, "REAL", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRREA + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRREA));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("REAL") )
		    check1 = true;


		Float[] outa = (Float[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of float and get array out reversed (0 len array)
     */
    public void Var373()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRFLO );


            ////////create array and set/get data
            Double[] ia = new Double[0];

            java.sql.Array a = createArray(connection_, "FLOAT", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRFLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRFLO));

	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());

		if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
		    check1 = true;

	    ///luw returns array of doubles..
	    //jdbc spec says jdbc floate is a double ...turns out the TB is coded this way
		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of double and get array out reversed (0 len array)
     */
    public void Var374()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDOU );


            ////////create array and set/get data
            Double[] ia = new Double[0];

            java.sql.Array a = createArray(connection_, "DOUBLE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDOU + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDOU));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("DOUBLE") )
		    check1 = true;


		Double[] outa = (Double[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of decimal(5,2) and get array out reversed (0 len array)
     */
    public void Var375()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDEC );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[0];

            java.sql.Array a = createArray(connection_, "DECIMAL", ia);//??ok

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDEC + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;

	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDEC));
	    } else {

       // System.out.println( ((Array)b).getBaseTypeName());

		if( ((Array)out).getBaseTypeName().equals("DECIMAL") )
		    check1 = true;


		BigDecimal[] outa = (BigDecimal[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of numeric and get array out reversed  (0 len array)
     */
    public void Var376()
    {
	checkConnection();

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRNUM );


            ////////create array and set/get data
            BigDecimal[] ia = new BigDecimal[0];


            java.sql.Array a = createArray(connection_, "NUMERIC", ia);//?

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRNUM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRNUM));
	    } else {
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("NUMERIC") )
                check1 = true;


            BigDecimal[] outa = (BigDecimal[])out.getArray();

            ResultSet rsa = out.getResultSet();
             check2 = checkArrayOutputReversed(ia, outa, sb);
             check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char(1) and get array out reversed  (0 len array)
     */
    public void Var377()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH1 );


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH1 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCH1));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("CHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }



    /**
     * Set an ARRAY of char50 and get array out reversed  (0 len array)
     */
    public void Var378()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCH50 );


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "CHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCH50 + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCH50));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("CHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		for(int x = 0; x<ia.length; x++)
		    ia[x] = ia[x] + "                                               ";

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		 check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of varchar and get array out reversed (0 len array)
     */
    public void Var379()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVCH );
           //??LUW does not like varchar(50) as a type either..??

            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "VARCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVCH + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVCH));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("VARCHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of  graphic and get array out reversed (0 len array)
     */
    public void Var380()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRGR);


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "GRAPHIC", ia);
            a = createArray(connection_, "NCHAR", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRGR));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("NCHAR") ||
		    ((Array)out).getBaseTypeName().equals("CHAR")  )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of vargraphic and get array out reversed  (0 len array)
     */
    public void Var381()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVGR);


            ////////create array and set/get data
            String[] ia = new String[0];

            java.sql.Array a = createArray(connection_, "VARGRAPHIC", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVGR + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVGR));
	    } else {

	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("VARGRAPHIC")   || ((Array)out).getBaseTypeName().equals("NVARCHAR") || ((Array)out).getBaseTypeName().equals("VARCHAR") )
		    check1 = true;


		String[] outa = (String[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of clob and get array out reversed  (0 len array)
     */
    public void Var382()
    {

	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRCLO);
            reconnect();

            ////////create array and set/get data
            Clob[] ia = new Clob[0];

            java.sql.Array a = createArray(connection_, "CLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRCLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
	    if (out == null) {
		sb.append("output is null:  Procedure definition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRCLO));
	    } else {

		if( ((Array)out).getBaseTypeName().equals("CLOB") )
		    check1 = true;


		Clob[] outa = (Clob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of blob and get array out reversed  (0 len array)
     */
    public void Var383()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRBLO);

            reconnect();
            ////////create array and set/get data
            Blob[] ia = new Blob[0];

            java.sql.Array a = createArray(connection_, "BLOB", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBLO + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if (out == null) {
		sb.append("output array is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRBLO));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("BLOB") )
		    check1 = true;


		Blob[] outa = (Blob[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    public void Var384()
    {
    if (checkArraySupport())
        succeeded();
    }

    /**
     * Set an ARRAY of date and get array out reversed (0 len array)
     */
    public void Var385()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRDAT);

            reconnect();
            ////////create array and set/get data
            Date[] ia = new Date[0];

            java.sql.Array a = createArray(connection_, "DATE", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRDAT + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;

           // System.out.println( ((Array)b).getBaseTypeName());
	    if (out == null) {
		sb.append("output array is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRDAT));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("DATE") )
		    check1 = true;


		Date[] outa = (Date[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length date added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of time and get array out reversed  (0 len array)
     */
    public void Var386()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTIM);
            reconnect();

            ////////create array and set/get data
            Time[] ia = new Time[0];

            java.sql.Array a = createArray(connection_, "TIME", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTIM + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
	    boolean check3 = false;

	    if (out == null) {
		sb.append("output array is null "+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTIM));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIME") )
		    check1 = true;


		Time[] outa = (Time[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }

            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length time Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * Set an ARRAY of timestamp and get array out reversed  (0 len array)
     */
    public void Var387()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRTS);
            reconnect();

            ////////create array and set/get data
            Timestamp[] ia = new Timestamp[0];

            java.sql.Array a = createArray(connection_, "TIMESTAMP", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRTS + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;

	    if (out == null) {
		check1=false;
		sb.append("output array is null " +" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRTS));
	    } else {
	   // System.out.println( ((Array)b).getBaseTypeName());
		if( ((Array)out).getBaseTypeName().equals("TIMESTAMP") )
		    check1 = true;


		Timestamp[] outa = (Timestamp[])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length byte array added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of binary and get array out reversed (0 len array)
     */
    public void Var388()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[2][];

	    ia[0]=new byte[4];
	    ia[1]=new byte[3];
	    ia[0][0]=0;
	    ia[0][1]=1;
	    ia[0][2]=2;
	    ia[0][3]=3;
	    ia[1][0]=70;
	    ia[1][1]=71;
	    ia[1][2]=72;

	    byte[][] expectedArray = new byte[2][];

	    expectedArray[0]=new byte[50];
	    expectedArray[1]=new byte[50];
	    expectedArray[0][0]=0;
	    expectedArray[0][1]=1;
	    expectedArray[0][2]=2;
	    expectedArray[0][3]=3;
	    expectedArray[1][0]=70;
	    expectedArray[1][1]=71;
	    expectedArray[1][2]=72;

	    //
	    // Because this is a binary field, the size of the output
	    // must match the size of the declared array.  In this
	    // case the size is 50
	    //

            java.sql.Array a = createArray(connection_, "BINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( out != null && ((Array)out).getBaseTypeName().equals("BINARY")) {
                check1 = true;
	    } else {
		if (out == null) {
		    sb.append("out == null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
		} else {
		    sb.append("Base type is "+((Array)out).getBaseTypeName()+" expected BINARY");
		}
	    }

	    boolean check2 = false;
	    boolean check3 = false;

	    if (out != null ) {
		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(expectedArray, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(expectedArray, rsa, sb);
	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of varbinary and get array out reversed  (0 len array)
     */
    public void Var389()
    {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRVBY);

            reconnect();
            ////////create array and set/get data
            byte[][] ia = new byte[0][0];

            java.sql.Array a = createArray(connection_, "VARBINARY", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRVBY+ " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
	    boolean check2 = false;
	    boolean check3 = false;
           // System.out.println( ((Array)b).getBaseTypeName());

	    if (out == null) {
		check1 = false;
		sb.append("output parameter is null"+" procedureDefinition is "+JDSetupProcedure.getProcedureDefinition(JDSetupProcedure.STP_CSARRVBY));
	    } else {
		if( ((Array)out).getBaseTypeName().equals("VARBINARY") )
		    check1 = true;


		byte[][] outa = ( byte[][])out.getArray();

		ResultSet rsa = out.getResultSet();
		check2 = checkArrayOutputReversed(ia, outa, sb);//add code to compare bytearrays to method
		check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

	    }
            assertCondition(check1 && check2 && check3, " V7R1 Test for zero length byte array added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }

    /**
     * Set an ARRAY of SQLXML and get array out reversed (0 len array)
     */
    public void Var390()
    {
	if (checkJdbc40()) {
	StringBuffer sb = new StringBuffer();
    if (checkArraySupport())
        try{
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }

            if(isToolboxDriver()){
                notApplicable("No XML (locators) arrays supported in TB"); //host limitation on locators in arrays.  XML is a locator
                return;
            }
	    assureProcedureExists(JDSetupProcedure.STP_CSARRXML);
            reconnect();

            ////////create array and set/get data


            Object[] ia = new Object[0];

            java.sql.Array a = createArray(connection_, "SQLXML", ia);

            //call proc to revers 4 elements in array
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRXML + " ( ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","arrOut", java.sql.Types.ARRAY);
            cs.execute();
            JDReflectionUtil.callMethod_V(cs,"setObject","arrIn", a);
            cs.execute();

            Array out = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","arrOut");
            boolean check1 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
            if( ((Array)out).getBaseTypeName().equals("SQLXML") )  //correct? or XML
                check1 = true;


            Object[] outa = (Object[])out.getArray();

            ResultSet rsa = out.getResultSet();
            boolean check2 = checkArrayOutputReversed(ia, outa, sb);
            boolean check3 = checkArrayResultSetOutputReversed(ia, rsa, sb);

            assertCondition(check1 && check2 && check3, " V7R1 Test for Arrays added by tb 2/26/09 "+sb);




        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
	}
    }



    //
    // Test various null types
    //
    public void Var391() { testNullArrayReuse("JDCSNARINT", "INTEGER"); }
    public void Var392() { testNullArrayReuse("JDCSNARSI",  "SMALLINT"); }
    public void Var393() { testNullArrayReuse("JDCSNARBIN",  "BIGINT"); }

    public void Var394() { testNullArrayReuse( "JDCSNARREA", "REAL"); }
    public void Var395() { testNullArrayReuse( "JDCSNARFLO", "FLOAT"); }
    public void Var396() { testNullArrayReuse( "JDCSNARDOU", "DOUBLE"); }
    public void Var397() { testNullArrayReuse( "JDCSNARDEC", "DECIMAL(10,2)"); }
    public void Var398() { testNullArrayReuse( "JDCSNARNUM", "NUMERIC(10,2)"); }
    public void Var399() { testNullArrayReuse( "JDCSNARCH1", "CHAR(1)"); }
    public void Var400() { testNullArrayReuse( "JDCSNARCH50", "CHAR(50)"); }
    public void Var401() { testNullArrayReuse( "JDCSNARVCH", "VARCHAR(80)"); }
    public void Var402() { testNullArrayReuse( "JDCSNARGR", "GRAPHIC(50)"); }
    public void Var403() { testNullArrayReuse( "JDCSNARVGR", "VARGRAPHIC(50)"); }
    public void Var404() {
	if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
	    notApplicable("CLOB arrays cannot be support in JDBC (too large)");
	    return;
	}

	testNullArrayReuse( "JDCSNARCLO", "CLOB(1M)");
    }
    public void Var405() {
	if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
	    notApplicable("BLOB arrays cannot be support in JDBC (too large)");
	    return;
	}
	testNullArrayReuse( "JDCSNARBLO", "BLOB(1M)");
    }
    public void Var406() { testNullArrayReuse( "JDCSNARDAT", "DATE"); }
    public void Var407() { testNullArrayReuse( "JDCSNARTIM", "TIME"); }
    public void Var408() { testNullArrayReuse( "JDCSNARTS", "TIMESTAMP"); }
    public void Var409() { testNullArrayReuse( "JDCSNARBY", "BINARY(50)"); }
    public void Var410() { testNullArrayReuse( "JDCSNARVBY", "VARBINARY(50)"); }
    public void Var411() {
	if (checkJdbc40()) {
	    if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) {
		notApplicable("XML arrays cannot be support in JDBC (too large)");
		return;
	    }
	    testNullArrayReuse( "JDCSNARXML", "XML");
	}
    }



    /**
     * Set an ARRAY of time and get array out reversed with all time format properties
     */
    public void Var412() { testTimeCase1reuse(";time format=hms"); }
    public void Var413() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testTimeCase1reuse(";time format=usa"); }
    public void Var414() { testTimeCase1reuse(";time format=iso"); }
    public void Var415() { testTimeCase1reuse(";time format=eur"); }
    public void Var416() { testTimeCase1reuse(";time format=jis"); }
    public void Var417() { testTimeCase1reuse(";time format=hms;time separator=:"); }
    public void Var418() { testTimeCase1reuse(";time format=hms;time separator=."); }
    public void Var419() { testTimeCase1reuse(";time format=hms;time separator=,"); }
    public void Var420() { testTimeCase1reuse(";time format=hms;time separator=b"); }


    /**
     * Set an ARRAY of JDCSSetArrayTime and get array out reversed  with all time format properties
     */

    public void Var421() { testTimeCase2repeat(";time format=hms"); }
    public void Var422() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testTimeCase2repeat(";time format=usa"); }
    public void Var423() { testTimeCase2repeat(";time format=iso"); }
    public void Var424() { testTimeCase2repeat(";time format=eur"); }
    public void Var425() { testTimeCase2repeat(";time format=jis"); }
    public void Var426() { testTimeCase2repeat(";time format=hms;time separator=:"); }
    public void Var427() { testTimeCase2repeat(";time format=hms;time separator=."); }
    public void Var428() { testTimeCase2repeat(";time format=hms;time separator=,"); }
    public void Var429() { testTimeCase2repeat(";time format=hms;time separator=b"); }

    /**
     * Set an ARRAY of time and get array out reversed  (null elements)  with all time format properties
     */


    public void Var430() { testTimeCase3repeat(";time format=hms"); }
    public void Var431() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testTimeCase3repeat(";time format=usa"); }
    public void Var432() { testTimeCase3repeat(";time format=iso"); }
    public void Var433() { testTimeCase3repeat(";time format=eur"); }
    public void Var434() { testTimeCase3repeat(";time format=jis"); }
    public void Var435() { testTimeCase3repeat(";time format=hms;time separator=:"); }
    public void Var436() { testTimeCase3repeat(";time format=hms;time separator=."); }
    public void Var437() { testTimeCase3repeat(";time format=hms;time separator=,"); }
    public void Var438() { testTimeCase3repeat(";time format=hms;time separator=b"); }



    /**
     * Set an ARRAY of date and get array out reversed with all date format properties
     */
    public void Var439() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; } testDateCase1reuse(";date format=mdy");  }
    public void Var440() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy");  }
    public void Var441() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd");  }
    public void Var442() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=usa");  }
    public void Var443() { testDateCase1reuse(";date format=iso");  }
    public void Var444() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=eur");  }
    public void Var445() { testDateCase1reuse(";date format=jis");  }
    public void Var446() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian");  }
    public void Var447() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=mdy;date separator=/");  }
    public void Var448() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=mdy;date separator=-");  }
    public void Var449() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=mdy;date separator=.");  }
    public void Var450() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=mdy;date separator=,");  }
    public void Var451() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=mdy;date separator=b");  }
    public void Var452() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy;date separator=/");  }
    public void Var453() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy;date separator=-");  }
    public void Var454() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy;date separator=.");  }
    public void Var455() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy;date separator=,");  }
    public void Var456() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=dmy;date separator=b");  }
    public void Var457() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd;date separator=/");  }
    public void Var458() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd;date separator=-");  }
    public void Var459() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd;date separator=.");  }
    public void Var460() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd;date separator=,");  }
    public void Var461() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=ymd;date separator=b");  }
    public void Var462() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian;date separator=/");  }
    public void Var463() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian;date separator=-");  }
    public void Var464() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian;date separator=.");  }
    public void Var465() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian;date separator=,");  }
    public void Var466() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase1reuse(";date format=julian;date separator=b");  }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed  with all date format properties
     */

    public void Var467() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy");  }
    public void Var468() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy");  }
    public void Var469() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd");  }
    public void Var470() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=usa");  }
    public void Var471() { testDateCase2repeat(";date format=iso");  }
    public void Var472() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=eur");  }
    public void Var473() { testDateCase2repeat(";date format=jis");  }
    public void Var474() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian");  }
    public void Var475() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy;date separator=/");  }
    public void Var476() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy;date separator=-");  }
    public void Var477() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy;date separator=.");  }
    public void Var478() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy;date separator=,");  }
    public void Var479() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=mdy;date separator=b");  }
    public void Var480() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy;date separator=/");  }
    public void Var481() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy;date separator=-");  }
    public void Var482() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy;date separator=.");  }
    public void Var483() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy;date separator=,");  }
    public void Var484() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=dmy;date separator=b");  }
    public void Var485() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=/");  }
    public void Var486() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=-");  }
    public void Var487() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=.");  }
    public void Var488() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=,");  }
    public void Var489() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=b");  }
    public void Var490() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=/");  }
    public void Var491() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=-");  }
    public void Var492() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=.");  }
    public void Var493() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=,");  }
    public void Var494() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=b");  }

    /**
     * Set an ARRAY of JDCSSetArrayDate and get array out reversed  with all date format properties
     */

    public void Var495() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy");  }
    public void Var496() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy");  }
    public void Var497() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=ymd");  }
    public void Var498() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=usa");  }
    public void Var499() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=iso");  }
    public void Var500() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=eur");  }
    public void Var501() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=jis");  }
    public void Var502() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=julian");  }
    public void Var503() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy;date separator=/");  }
    public void Var504() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy;date separator=-");  }
    public void Var505() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy;date separator=.");  }
    public void Var506() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy;date separator=,");  }
    public void Var507() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=mdy;date separator=b");  }
    public void Var508() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy;date separator=/");  }
    public void Var509() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy;date separator=-");  }
    public void Var510() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy;date separator=.");  }
    public void Var511() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy;date separator=,");  }
    public void Var512() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=dmy;date separator=b");  }
    public void Var513() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=ymd;date separator=/");  }
    public void Var514() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=ymd;date separator=-");  }
    public void Var515() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=ymd;date separator=.");  }
    public void Var516() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase3repeat(";date format=ymd;date separator=,");  }
    public void Var517() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=ymd;date separator=b");  }
    public void Var518() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=/");  }
    public void Var519() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=-");  }
    public void Var520() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=.");  }
    public void Var521() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=,");  }
    public void Var522() { if (getRelease() <= JDTestDriver.RELEASE_V7R2M0) { notApplicable("Time format change not working in V7R1"); return; }testDateCase2repeat(";date format=julian;date separator=b");  }



    /*
     * Extra vars
     */
    /**
     * Set an ARRAY of int  and non-array int with a literal as seond parameter.
     * See issue 48043
     */

    public void Var523() {
	StringBuffer sb = new StringBuffer();
	if (checkArraySupport())
        try{
	    assureProcedureExists(JDSetupProcedure.STP_CSARRIN2);
            reconnect();
            ////////create array and set/get data
            Integer[] ia1 = new Integer[4];
            ia1[0] = new Integer( 0);
            ia1[1] = new Integer(1);
            ia1[2] = new Integer(2);
            ia1[3] = new Integer(3);
            java.sql.Array a1 = createArray(connection_, "INTEGER", ia1);

            Integer[] ia2 = new Integer[4];
            ia2[0] = new Integer( 0);
            ia2[1] = new Integer(100);
            ia2[2] = new Integer(200);
            ia2[3] = new Integer(300);
            java.sql.Array a2 = createArray(connection_, "INTEGER", ia2);

            //call proc   (in array, in int, inout array, inout int, out array, out int)
            CallableStatement cs = connection_.prepareCall("call " + JDSetupProcedure.STP_CSARRIN2 + " ( ?,222,?, ?, ?, ?) ");
            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P3", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P4", java.sql.Types.INTEGER);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setInt(3, 444);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P5", java.sql.Types.ARRAY);
            JDReflectionUtil.callMethod_V(cs,"registerOutParameter","P6", java.sql.Types.INTEGER);
            cs.execute();

            JDReflectionUtil.callMethod_V(cs,"setObject","P1", a1);
            JDReflectionUtil.callMethod_V(cs,"setObject","P3", a2);
            cs.setInt(3, 444);

            cs.execute();

            Array outa1 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","P3");
            Array outa2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray",4);
            int outi1 = cs.getInt(3); //222
            int outi2 = cs.getInt(5); //444

            boolean check1 = false;
            boolean check2 = false;
           // System.out.println( ((Array)b).getBaseTypeName());
	    if( ((Array)outa1).getBaseTypeName().equals("INTEGER") ) {
                check1 = true;
	    } else {
		check1 = false;
		sb.append("check1 is false getBastTypeName() = "+((Array)outa1).getBaseTypeName()+"\n");
	    }
	    if( ((Array)outa2).getBaseTypeName().equals("INTEGER") ) {
                check2 = true;
	    } else {
		check2 = false;
		sb.append("check2 is false getBastTypeName() = "+((Array)outa2).getBaseTypeName()+"\n");
	    }


            Integer[] outa1a = (Integer[])outa1.getArray();
            Integer[] outa2a = (Integer[])outa2.getArray();

            ResultSet rsa1 = outa1.getResultSet();
            ResultSet rsa2 = outa2.getResultSet();
            boolean check3 = checkArrayOutput(ia1, outa1a, sb);
            boolean check4 = checkArrayResultSetOutput(ia1, rsa1, sb);
            boolean check5 = checkArrayOutput(ia2, outa2a, sb);
            boolean check6 = checkArrayResultSetOutput(ia2, rsa2, sb);



            assertCondition(outi1 == 222 && outi2 == 444 && check1 && check2 && check3 &&check4 && check5 && check6, " V7R1 Test for Arrays added by tb 2/26/09\n outi1="+outi1+" sb 222\n"+
			    "outi2 = "+outi2+" sb 444\n"+sb.toString());


        }catch(Exception e){
            failed(connection_, e, "Unexpected Exception - V7R1 Test for Arrays added by tb 2/26/09 ");
        }
    }


    /**
     * getArray() - Set an ARRAY of ints where error occurs.
     * Make sure that the error is the expected one.
     * Then reuse the array to verify that things will still work.
     * Added 08/09/2012 for issue 48051.
     *
     */
    public void Var524() {
    if (checkArraySupport()) {
      StringBuffer sb = new StringBuffer(" -- added 08/09/2012 to test issue 48051");
      boolean passed = true;
      try {
        assureProcedureExists(JDSetupProcedure.STP_CSARRINT2R);
        String expectedException = "Data type mismatch";
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expectedException = "Cardinality 101 of source array greater";
        }
        CallableStatement cs = connection_.prepareCall("call "
            + JDSetupProcedure.STP_CSARRINT2R + " (?,?) ");
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter",1, Types.ARRAY);

        // //////create array and set/get data
        // check local copy of array and arrayResultSet
        Integer[] ia;
        java.sql.Array a;

        for (int loops = 0; loops < 0; loops++) {
          // //////create array and set/get data
          // check local copy of array and arrayResultSet
          ia = new Integer[101];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }
          a = createArray(connection_, "INTEGER", ia);

          // call proc with toobig input array
          sb.append("Calling with array of length " + ia.length + "\n");
          try {
            JDReflectionUtil.callMethod_V(cs,"setObject",2, a);
            cs.execute();
          } catch (Exception e) {
            String eMessage = e.toString();
            if (eMessage.indexOf(expectedException) < 0) {
              passed = false;
              sb.append("Got eMessage : " + eMessage + "\n");
              sb.append("Expected     : " + expectedException + "\n");
              printStackTraceToStringBuffer(e, sb);
            }
          }

          ia = new Integer[5];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }
          a = createArray(connection_, "INTEGER", ia);

          sb.append("Calling with array of length " + ia.length + "\n");
          JDReflectionUtil.callMethod_V(cs,"setObject",2, a);
          cs.execute();

          // Call again with toobig input array
          sb.append("Calling again with array of length " + ia.length + "\n");
          try {
            JDReflectionUtil.callMethod_V(cs,"setObject",2, a);
            cs.execute();
          } catch (Exception e) {
            String eMessage = e.toString();
            if (eMessage.indexOf(expectedException) < 0) {
              passed = false;
              sb.append("Got eMessage : " + eMessage + "\n");
              sb.append("Expected     : " + expectedException + "\n");
              printStackTraceToStringBuffer(e, sb);
            }
          }

          ia = new Integer[100];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }

          a = createArray(connection_, "INTEGER", ia);
          sb.append("Calling with array of length " + ia.length + "\n");
          JDReflectionUtil.callMethod_V(cs,"setObject",2, a);
          cs.execute();
        }
        cs.close();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception  " + sb.toString());
      }
    }
  }

    /**
     * getArray() - Set an ARRAY of ints where error occurs.
     * Make sure that the error is the expected one.
     * Then reuse the array to verify that things will still work.
     * Added 08/09/2012 for issue 48051.
     *
     */




    public void Var525() {
    if (checkArraySupport()) {
      StringBuffer sb = new StringBuffer(" -- added 08/09/2012 to test issue 48051");
      boolean passed = true;
      try {
        assureProcedureExists(JDSetupProcedure.STP_CSARRINT2);
        String expectedException = "Data type mismatch";
        if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
          expectedException = "Cardinality 101 of source array greater";
        }
        CallableStatement cs = connection_.prepareCall("call "
            + JDSetupProcedure.STP_CSARRINT2 + " (?,?) ");
        JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", Types.ARRAY);

        // //////create array and set/get data
        // check local copy of array and arrayResultSet
        Integer[] ia;
        java.sql.Array a;

        for (int loops = 0; loops < 10; loops++) {
          // //////create array and set/get data
          // check local copy of array and arrayResultSet
          ia = new Integer[101];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }
          a = createArray(connection_, "INTEGER", ia);

          // call proc with toobig input array
          sb.append("Calling with array of length " + ia.length + "\n");
          try {
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();
          } catch (Exception e) {
            String eMessage = e.toString();
            if (eMessage.indexOf(expectedException) < 0) {
              passed = false;
              sb.append("Got eMessage : " + eMessage + "\n");
              sb.append("Expected     : " + expectedException + "\n");
              printStackTraceToStringBuffer(e, sb);
            }
          }

          ia = new Integer[5];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }
          a = createArray(connection_, "INTEGER", ia);

          sb.append("Calling with array of length " + ia.length + "\n");
          JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
          cs.execute();

          // Call again with toobig input array
          sb.append("Calling again with array of length " + ia.length + "\n");
          try {
            JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
            cs.execute();
          } catch (Exception e) {
            String eMessage = e.toString();
            if (eMessage.indexOf(expectedException) < 0) {
              passed = false;
              sb.append("Got eMessage : " + eMessage + "\n");
              sb.append("Expected     : " + expectedException + "\n");
              printStackTraceToStringBuffer(e, sb);
            }
          }

          ia = new Integer[100];
          for (int i = 0; i < ia.length; i++) {
            ia[i] = new Integer(i);
          }

          a = createArray(connection_, "INTEGER", ia);
          sb.append("Calling with array of length " + ia.length + "\n");
          JDReflectionUtil.callMethod_V(cs,"setObject","NUMLISTIN", a);
          cs.execute();
        }
        cs.close();
        assertCondition(passed, sb);

      } catch (Exception e) {
        failed(connection_, e, "Unexpected Exception  " + sb.toString());
      }
    }
  }



    /**
     * Test setting and retreiving a null array for the specified type.
     */
    public void testNullArray(String testName, String elementType)
    {
	StringBuffer sb = new StringBuffer();

	sb.append("null array parms failed \n"); 
	callSql = null;
	if (checkArraySupport())
        try{
	    String typeName = JDSetupProcedure.COLLECTION +"."+testName+"TYPE";
	    String procedureName = JDSetupProcedure.COLLECTION +"."+testName+"PROC";


            Statement st = connection_.createStatement();

	    // Drop the procedure if it exists
            callSql = "drop procedure " + procedureName ;
	    sb.append(callSql+"\n");
	    try {
		st.executeUpdate(callSql);
	    } catch (Exception e) {
		printStackTraceToStringBuffer(e, sb);
		String message = e.toString();
		if ((message.indexOf("not found") < 0) &&
		    (message.indexOf("-240") < 0 )) {
		    sb.append("WARNING:  Unable to drop procedure using "+callSql+"\n");
		    printStackTraceToStringBuffer(e, sb);
		}
	    }


	    // Create the type
            try{

		callSql = "create type " + typeName+ " as "+elementType+" array[100] ";
		sb.append(callSql+"\n");
                st.execute(callSql);

            } catch(Exception e){
		String exInfo = e.toString();
		if ((exInfo.indexOf("already exists") > 0) ||
		    (exInfo.indexOf("-601") > 0)) {
		    sb.append("Warning: "+typeName+" already exists\n");
		} else {
		    sb.append("Unexpected exception\n");
		    sb.append("exInfo="+exInfo+"\n");
		}
		printStackTraceToStringBuffer(e, sb);
	    }

	    // Create the procedure

            callSql = "create procedure " + procedureName + "  (in numListIn " + typeName +" , out numListOut " + typeName  + ")  " +
                        " begin  " +
                        " set numListOut = numListIn; " +
                  " end ";

	    sb.append(callSql+"\n");
	    st.executeUpdate(callSql);


	    connection_.commit();

            //call proc with one input array
	    callSql = "call " + procedureName + " (?,?) ";
	    sb.append(callSql+"\n");
	    CallableStatement cs = connection_.prepareCall(callSql);
	    sb.append("Setting parameter 1 to null\n"); 
	    cs.setObject("NUMLISTIN", null);
	    JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
	    sb.append("Setting execute\n"); 
	    cs.execute();
	    Array aout = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
	    boolean check1 = false;
	    if(cs.wasNull() && aout == null) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("setArray(null) getArray did not return null\n");
	    }

	    callSql = "call " + procedureName+ " (?,?) ";
	    sb.append(callSql+"\n");
	    cs = connection_.prepareCall(callSql);
	    sb.append("Setting parameter 1 to null\n"); 	    
	    cs.setNull(1, java.sql.Types.ARRAY);
	    JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
	    sb.append("execute\n"); 
	    cs.execute();
	    Array aout2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
	    boolean check2 = false;
	    if(cs.wasNull() && aout2 == null) {
		check2 = true;
	    } else {
		check2 = false;
		sb.append("stNull() getArray did not return null\n");
	    }

            callSql = "drop procedure " + procedureName ;
	    st.executeUpdate(callSql);

            callSql = "drop type " + typeName;
	    st.executeUpdate(callSql);


	    st.close();
	    assertCondition(check1 && check2, sb);



        }catch(Exception e){
	    sb.append("Unexpect exception");
	    printStackTraceToStringBuffer(e, sb);
            failed(connection_, e, "Unexpected Exception sql='"+callSql+" '- V7R1 Test for Null Arrays added by nativeb 12/23/09 "+sb.toString());
        }
    }


    /**
     * Test setting and retreiving a null array for the specified type.
     * Reuse the callable statement 
     */
    public void testNullArrayReuse(String testName, String elementType)
    {
	StringBuffer sb = new StringBuffer();
	sb.append("null array parms failed \n");
	callSql = null;
	if (checkArraySupport())
        try{
	    String typeName = JDSetupProcedure.COLLECTION +"."+testName+"TYPE";
	    String procedureName = JDSetupProcedure.COLLECTION +"."+testName+"PROC";


            Statement st = connection_.createStatement();

	    // Drop the procedure if it exists
            callSql = "drop procedure " + procedureName ;
	    sb.append(callSql+"\n");
	    try {
		st.executeUpdate(callSql);
	    } catch (Exception e) {
		printStackTraceToStringBuffer(e, sb);
		String message = e.toString();
		if ((message.indexOf("not found") < 0) &&
		    (message.indexOf("-240") < 0 )) {
		    sb.append("WARNING:  Unable to drop procedure using "+callSql+"\n");
		    printStackTraceToStringBuffer(e, sb);
		}
	    }


	    // Create the type
            try{

		callSql = "create type " + typeName+ " as "+elementType+" array[100] ";
		sb.append(callSql+"\n");
                st.execute(callSql);

            } catch(Exception e){
		String exInfo = e.toString();
		if ((exInfo.indexOf("already exists") > 0) ||
		    (exInfo.indexOf("-601") > 0)) {
		    sb.append("Warning: "+typeName+" already exists\n");
		} else {
		    sb.append("Unexpected exception\n");
		    sb.append("exInfo="+exInfo+"\n");
		}
		printStackTraceToStringBuffer(e, sb);
	    }

	    // Create the procedure

            callSql = "create procedure " + procedureName + "  (in numListIn " + typeName +" , out numListOut " + typeName  + ")  " +
                        " begin  " +
                        " set numListOut = numListIn; " +
                  " end ";

	    sb.append(callSql+"\n");
	    st.executeUpdate(callSql);


	    connection_.commit();

            //call proc with one input array
	    callSql = "call " + procedureName + " (?,?) ";
	    sb.append(callSql+"\n");
	    CallableStatement cs = connection_.prepareCall(callSql);
	    cs.setObject("NUMLISTIN", null);
	    JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
	    cs.execute();
	    cs.setObject("NUMLISTIN", null);
	    cs.execute();

	    Array aout = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
	    boolean check1 = false;
	    if(cs.wasNull() && aout == null) {
		check1 = true;
	    } else {
		check1 = false;
		sb.append("setArray(null) getArray did not return null\n");
	    }

	    callSql = "call " + procedureName+ " (?,?) ";
	    cs = connection_.prepareCall(callSql);
	    cs.setNull(1, java.sql.Types.ARRAY);
	    JDReflectionUtil.callMethod_V(cs,"registerOutParameter","NUMLISTOUT", java.sql.Types.ARRAY);
	    cs.execute();
	    cs.setNull(1, java.sql.Types.ARRAY);
	    cs.execute();

	    Array aout2 = (Array) JDReflectionUtil.callMethod_O(cs,"getArray","NUMLISTOUT");
	    boolean check2 = false;
	    if(cs.wasNull() && aout2 == null) {
		check2 = true;
	    } else {
		check2 = false;
		sb.append("stNull() getArray did not return null\n");
	    }

            callSql = "drop procedure " + procedureName ;
	    st.executeUpdate(callSql);

            callSql = "drop type " + typeName;
	    st.executeUpdate(callSql);


	    st.close();
	    assertCondition(check1 && check2, sb);



        }catch(Exception e){
	    sb.append("Unexpect exception");
	    printStackTraceToStringBuffer(e, sb);
            failed(connection_, e, "Unexpected Exception sql='"+callSql+" '- V7R1 Test for Null Arrays added by nativeb 12/23/09 "+sb.toString());
        }
    }


    //check if in and out are same
    static boolean checkArrayOutput(Object[] in, Object[] out, StringBuffer sb )throws SQLException{
        boolean passed = true;
        int lenIn = in.length;
        int lenOut = out.length;
        if(lenIn != lenOut) {
            sb.append("lenIn("+lenIn+") != lenOut("+lenOut+")\n");
        }

        for(int i = 0; i<lenIn && i < lenOut; i++){
            if(!in[i].equals(out[i])) {
                sb.append("in["+i+"]("+in[i]+" != out["+i+"]("+out[i]+")\n");
                passed=false;
            }
        }
        return passed;
    }

    //check if in and out are reversed order
    static boolean checkArrayOutputReversed(Object[] in, Object[] out, StringBuffer sb)throws SQLException{
      boolean passed = true;
        int lenIn = in.length;
        int lenOut = out.length;
        if(lenIn != lenOut) {
          sb.append("lenIn("+lenIn+") != lenOut("+lenOut+")\n");
          passed= false;
        }
        int j = lenOut-1;
        for(int i = 0; i<lenIn && j >= 0; i++){
            //if byte[][] then check each byte in the array[][]
            if(in[i] instanceof byte[])
            {
                byte[] inB = (byte[])in[i];
                byte[] outB = (byte[])out[j];
                if (inB.length != outB.length) {
                  sb.append("for in["+i+"] inB.length("+inB.length+") != outB.length("+outB.length+"\n");
                  passed = false;
                }
                for (int x = 0; x< inB.length && x < outB.length; x++)
                {
                    if(inB[x] != outB[x]) {
                      sb.append("for int["+i+"] inB["+x+"]("+inB[x]+") != outB["+x+"]("+outB[x]+")\n");
                      passed = false;
                    }
                }
                j--;
            }
            else if(in[i] instanceof Clob)
            {

                    Clob iC = (Clob)in[i];
                    Clob oC = (Clob)out[j--];
                    String iS = null;
                    if(iC != null)
                        iS =  iC.getSubString((long)1, (int)iC.length());
                    String oS = null;
                    if (oC != null)
                        oS = oC.getSubString((long)1, (int)oC.length());

                    if(iS == null || oS==null){
                        if(iS != oS) {
                            sb.append("for in["+i+"] iS ="+iS+" oS="+oS+"\n" );
                            passed = false;
                        }
                    }
                    else if(!iS.equals(oS)) {
                      sb.append("for in["+i+"] iS ="+iS+" oS="+oS+"\n" );
                      passed = false;
                    }

            } else if (in[i] instanceof Blob) {

        Blob iC = (Blob) in[i];
        Blob oC = (Blob) out[j--];
        byte[] iS = null;
        if (iC != null)
          iS = iC.getBytes(1L, (int) (iC.length()));
        byte[] oS = null;
        if (oC != null)
          oS = oC.getBytes(1L, (int) (oC.length()));

        if (iS == null || oS == null) {
          if (iS != oS) {
            sb.append("for in[" + i + "] iS =" + iS + " oS=" + oS + "\n");
            passed = false;
          }
        } else {

          for (int x = 0; x < in.length; x++) {

            if (iS[x] != oS[x]) {
              sb.append("for int[" + i + "] iS[" + x + "](" + iS[x]
                  + ") != oS[" + x + "](" + oS[x] + ")\n");
              passed = false;

            }

          }
        }
            }else if(in[i] instanceof Timestamp)
            {
                String inStr = in[i].toString();
                String outStr = out[j].toString();
                if(inStr != null)
                    inStr = inStr.substring(0, 19); //trim off nano seconds
                if(outStr != null)
                    outStr = outStr.substring(0, 19); //trim off nano seconds
                if(inStr == null || outStr == null){
                    j--;
                    if(inStr != outStr ) {
                        sb.append("for in["+i+"] iS ="+inStr+" oS="+outStr+"\n" );
                        passed = false ;
                    }
                }else
                {
                    if(!inStr.equals(outStr))
                    {
                        j--;
                        sb.append("for in["+i+"] iS ="+inStr+" oS="+outStr+"\n" );
                        passed = false ;
                    }
                    j--;
                }
            }else
	    {
                if(in[i] == null || out[j] == null){
                    if(in[i] != out[j--]) {
                        sb.append("in["+i+"]("+in[i]+") != out["+(j+1)+"]("+out[j+1]+")\n");
                        passed = false;
                    }
                }
                else if(!in[i].equals(out[j--])) {
                  sb.append("in["+i+"]("+in[i].getClass().getName()+")("+in[i]+") != out["+(j+1)+"]("+out[(j+1)].getClass().getName()+")("+out[j+1]+")\n");
                  passed = false;
                }
            }
        }
        return passed;
    }



    //check if in and out are same
    static boolean checkArrayResultSetOutput(Object[] in, ResultSet out, StringBuffer sb)throws SQLException{

        int lenIn = in.length;
        Object[] outArray = new Object[lenIn];

        for(int x = 0; x<lenIn ; x++){
            out.next();
            outArray[x] = out.getObject(2);
	    if (outArray[x] instanceof Short) {
		outArray[x] = new Integer(((Short)outArray[x]).intValue());
	    }
        }
        if(out.next()) {
          sb.append("checkArrayResultSetOutput:  lengths are different");
            return false; //diff lengths
        }

        return checkArrayOutput(in, outArray, sb);

    }

    //check if in and out are reversed order
    static boolean checkArrayResultSetOutputReversed(Object[] in, ResultSet out, StringBuffer sb)throws SQLException{

        int lenIn = in.length;
        Object[] outArray = new Object[lenIn];

        for(int x = 0; x<lenIn ; x++){
            out.next();
            outArray[x] = out.getObject(2);
        }
        if(out.next()) {
            sb.append("checkArrayResultSetOutputReversed:  lengths are different");
            return false; //diff lengths
        }
        sb.append("checkArrayResultSetOutputReversed calling checkArrayOutputReversed\n");
        return checkArrayOutputReversed(in, outArray, sb);

    }


    //helper to create an ARRAY
    Array createArray(Connection c, String typeName, Object[] data) throws Exception{

        java.sql.Array a;


        if(isJdbc40()
	   || isToolboxDriver()
	   || getDriver() == JDTestDriver.DRIVER_NATIVE ) //TB put createArrayOf into JDBC30 for testing
        {
            a = (Array)JDReflectionUtil.callMethod_OSA(c , "createArrayOf", typeName, data); // c.createArrayOf("INTEGER", ia);
        }else
        {
            //get array the old-fashioned way
            a = null;//add code
        }
        return a;
    }

    class JDCSSetArrayDate extends java.sql.Date {
	    private static final long serialVersionUID = 1L;

  public JDCSSetArrayDate(long date) {
	    super(date);
	}

	public JDCSSetArrayDate(int year, int month, int day) {
	  super(Date.valueOf(pad0(year,4)+"-"+pad0(month,2)+"-"+pad0(day,2)).getTime()); 
	   
	}
    }


    class JDCSSetArrayTime extends java.sql.Time {
      private static final long serialVersionUID = 1L;
	public JDCSSetArrayTime(long date) {
	    super(date);
	}
	public JDCSSetArrayTime(int hour, int minute, int second)  {
	    super(Time.valueOf(hour+":"+minute+":"+second).getTime());
	}
    }

    class JDCSSetArrayTimestamp extends java.sql.Timestamp {
      private static final long serialVersionUID = 1L;
	public JDCSSetArrayTimestamp(long date) {
	    super(date);
	}

	public JDCSSetArrayTimestamp(int year, int month, int date, int hour, int minute, int second, int nano) {
	    super(Timestamp.valueOf(pad0(year,4)+"-"+pad0(month,2)+"-"+pad0(date,2)+" "+pad0(hour,2)+":"+pad0(minute,2)+":"+pad0(second,2)+".0000"+nano).getTime());
	}

 
    }


    private static String pad0(int year, int l) {
      String answer = ""+year;
      while (answer.length() < l) {
        answer = "0"+answer; 
      }
      return answer; 
    }
    class JDCSSetArrayBigDecimal extends java.math.BigDecimal {
      private static final long serialVersionUID = 1L;
	public JDCSSetArrayBigDecimal (double val) {
	    super(val);
	}
	public JDCSSetArrayBigDecimal (String val) {
        super(val);
    }
    }



    class JDCSSetArrayArray implements java.sql.Array {
	Object[] stuff_;
	public JDCSSetArrayArray(Object[] stuff) {
	    stuff_=stuff;
	}
	public Object 	getArray() {
	    return stuff_;
	}
	public Object 	getArray(long index, int count) {
	    return stuff_;
	}
	public  Object 	getArray(long index, int count, Map map) {
	    return stuff_;
	}
	public  Object 	getArray(Map map) {
	    return stuff_;
	}

	public int 	getBaseType() {
	    return Types.JAVA_OBJECT;
	}

	public String 	getBaseTypeName() {
	    return "JAVA OBJECT";
	}

	public ResultSet getResultSet() throws SQLException {
	    throw new SQLException("not implemented");
	}

	public ResultSet 	getResultSet(long index, int count)  throws SQLException {
	    throw new SQLException("not implemented");
	}


	  public ResultSet 	getResultSet(long index, int count, Map map)  throws SQLException  {
	    throw new SQLException("not implemented");
	}


	  public ResultSet 	getResultSet(Map map)  throws SQLException  {
      throw new SQLException("not implemented");
	}

    public void free() throws SQLException {
      throw new SQLException("not implemented");
      
    }
    }



}
