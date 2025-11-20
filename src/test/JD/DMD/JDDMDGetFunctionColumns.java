///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetFunctionColumns.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 //////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDDMDGetFunctionColumns.java
 //
 // Classes:      JDDMDGetFunctionColumns
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDGetFunctionColumns.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>GetFunctionColumns()
</ul>
**/
public class JDDMDGetFunctionColumns
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetFunctionColumns";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private String              connectionCatalog_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    StringBuffer message=new StringBuffer();
    String errorMessage="";
    String parameterName="";
    String schemasUnderscore = "";
    int functionColumnResult = 5;
    int functionColumnReturn = 4;
    int functionColumnIn     = 1;
    int functionColumnOut    = 3;

/**
Constructor.
**/
    public JDDMDGetFunctionColumns (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetFunctionColumns",
            namesAndVars, runMode, fileOutputStream,
            password);
    }


    protected String getCatalogFromURL(String url) {
      // System.out.println("BaseURL is "+baseURL_);
      // must be running JCC, set to a valid value.
      // jdbc:db2://y0551p2:446/*LOCAL

      int lastColon = url.indexOf(":446");
      if (lastColon > 0) {
        String part1 = url.substring(0,lastColon);
        int lastSlash = part1.lastIndexOf('/',lastColon);
        if (lastSlash > 0) {
          return part1.substring(lastSlash+1).toUpperCase();

        }
      }
      return null;

    }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);


        connectionCatalog_ = connection_.getCatalog();
        if (connectionCatalog_ == null) {
           connectionCatalog_ = getCatalogFromURL(baseURL_);
           System.out.println("Warning:  connection.getCatalog() returned null setting to "+connectionCatalog_);
        }
        dmd_ = connection_.getMetaData ();

        //create functions
        createFunction(connection_,  JDDMDTest.COLLECTION + ".JDMDSMALLINT(JDMDSIP1 smallint)", "returns smallint language java parameter style db2general  deterministic not fenced null call no sql external action  external name 'quickudf!q1short'");

        //createFunction(globaldbConnection_, "globaldb.q1NFsmallint(x smallint )", "returns smallint language sql deterministic not fenced returns null on null input begin return x; end  ");



	schemasUnderscore = JDDMDTest.COLLECTION.substring(0, JDDMDTest.COLLECTION.length()-1) + "_";
        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();



    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        connection_.close ();
        connection_ = null; 

    }



    /**
     * Uses a connection to create a function
     *
     * @param connection
     *            Connection used to create the function
     * @param function
     *            Name of the function (as passed to drop function)
     * @param procedureParms
     *            Remainder of create function statement
     * @exception java.lang.Exception
     *                Exception is thrown if functionis not created.
     */
    public static void createFunction(Connection connection, String function, String functionSpec) throws Exception
    {
        Statement s = null;
        try
        {

            //
            // make sure it is gone
            //
            dropFunction(connection, function, functionSpec);

            s = connection.createStatement();

            s.executeUpdate("create function " + function + functionSpec);

        } catch (Exception e)
        {
            throw e;
        } finally
        {
            try
            {
                if (s != null)
                    s.close();
            } catch (Exception ex)
            {}
        }

    }

    /**
     * Uses a connection to drop a function
     */
    public static void dropFunction(Connection connection, String function, String functionSpec) throws Exception
    {
        Statement s = null;
        try
        {

            s = connection.createStatement();
            //
            // make sure it is gone
            //
	    String noArgs = function;

            try
            {
                // replace any arguments
                int argindex = noArgs.indexOf("JDMDSIP1 ");
                while (argindex > 0)
                {
                    // if (debug) System.out.println("RTest: noArgs = " + noArgs
                    // + "argindex = "+argindex );
                    noArgs = noArgs.substring(0, argindex) + noArgs.substring(argindex + 9);
                    argindex = noArgs.indexOf("JDMDSIP1 ");
                }

                s.executeUpdate("drop  function " + noArgs);
            } catch (Exception e)
            {
                //
                // The only exception we should silently tolerate is a not found
                // -- the rest will print messages
                //
                if (e.toString().indexOf("not found") < 0)
                {
                    //
                    // Try more 60 times if something in use
                    //
                    int count = 0;
                    while (count < 60 && e != null && e.toString().indexOf("in use") > 0)
                    {
                        try
                        {

                            s.executeUpdate("drop function " + noArgs);
                            e = null;
                        } catch (Exception e2)
                        {
                            count++;
                            e = e2;
                        }
                    }
                    if (e != null)
                    {
                        System.out.println("JDDMDGetFunctionColumns.debug:  warning:  unable to drop function using 'drop function "+noArgs+"'");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e)
        {
            throw e;
        } finally
        {
            try
            {
                if (s != null)
                    s.close();
            } catch (Exception ex)
            {}
        }

    }


/**
GetFunctionColumns() - Check the result set format.
**/
    public void Var001()
    {
        if ( checkJdbc40()) {

        try {
            message.setLength(0);
            /*  RETURN COLS:
             * <LI><B>FUNCTION_CAT</B> String => function catalog (may be <code>null</code>)
             *  <LI><B>FUNCTION_SCHEM</B> String => function schema (may be <code>null</code>)
             *  <LI><B>FUNCTION_NAME</B> String => function name.  This is the name
             * used to invoke the function
             *  <LI><B>COLUMN_NAME</B> String => column/parameter name
             *  <LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
             *      <UL>
             *      <LI> functionColumnUnknown - nobody knows
             *      <LI> functionColumnIn - IN parameter
             *      <LI> functionColumnInOut - INOUT parameter
             *      <LI> functionColumnOut - OUT parameter
             *      <LI> functionColumnReturn - function return value
             *      <LI> functionColumnResult - Indicates that the parameter or column
             *  is a column in the <code>ResultSet</code>
             *      </UL>
             *  <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
             *  <LI><B>TYPE_NAME</B> String => SQL type name, for a UDT type the
             *  type name is fully qualified
             *  <LI><B>PRECISION</B> int => precision
             *  <LI><B>LENGTH</B> int => length in bytes of data
             *  <LI><B>SCALE</B> short => scale -  null is returned for data types where
             * SCALE is not applicable.
             *  <LI><B>RADIX</B> short => radix
             *  <LI><B>NULLABLE</B> short => can it contain NULL.
             *      <UL>
             *      <LI> functionNoNulls - does not allow NULL values
             *      <LI> functionNullable - allows NULL values
             *      <LI> functionNullableUnknown - nullability unknown
             *      </UL>
             *  <LI><B>REMARKS</B> String => comment describing column/parameter
             *  <LI><B>CHAR_OCTET_LENGTH</B> int  => the maximum length of binary
             * and character based parameters or columns.  For any other datatype the returned value
             * is a NULL
             *  <LI><B>ORDINAL_POSITION</B> int  => the ordinal position, starting
             *  <LI><B>IS_NULLABLE</B> String
             *  <LI><B>SPECIFIC_NAME</B> String*/

            //ResultSet rs = dmd_.getFunctionColumns (null, null, null);
            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getFunctionColumns", new Class[]{String.class, String.class, String.class, String.class} , null, null, null, null);

            ResultSetMetaData rsmd = rs.getMetaData ();

            String[] expectedNames = { "FUNCTION_CAT", "FUNCTION_SCHEM", "FUNCTION_NAME",
                    "COLUMN_NAME", "COLUMN_TYPE", "DATA_TYPE",
                    "TYPE_NAME",   "PRECISION",   "LENGTH",
                    "SCALE",       "RADIX",       "NULLABLE",
                    "REMARKS",     "CHAR_OCTET_LENGTH", "ORDINAL_POSITION",
                    "IS_NULLABLE", "SPECIFIC_NAME"};

		    String usingExpectedTypes = "expectedTypes";
            int[] expectedTypes = {
		Types.VARCHAR,
		Types.VARCHAR,
		Types.VARCHAR,
		Types.VARCHAR,
		Types.SMALLINT,
		Types.INTEGER,
		Types.VARCHAR,
		Types.INTEGER,
		Types.INTEGER,
		Types.SMALLINT,
		Types.SMALLINT,
		Types.SMALLINT,
		-9,
		Types.INTEGER,
		Types.INTEGER,
		Types.VARCHAR,
		Types.VARCHAR};




            int count = rsmd.getColumnCount ();
            boolean namesCheck = JDDMDTest.checkColumnNames (rsmd, expectedNames, message);

	    // Results name same for both drivers 11/17/2016
            boolean typesCheck = JDDMDTest.checkColumnTypes (rsmd, expectedTypes, message);

            rs.close ();
            assertCondition ((count == 17) && (namesCheck) && (typesCheck),
                "count is "+count+"sb 17\n"+
                "namesCheck = "+namesCheck+"\n"+
                "typesCheck = "+typesCheck+"\n"+
			     "usingExpectedTypes="+usingExpectedTypes+"\n"+
                message);
        }
        catch (Exception e)  {
            failed (e, "01/25/07 New jdbc 40 TC.  Unexpected Exception");
        }
        }
    }



/**
GetFunctionColumns() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).
**/
    public void Var002()
    {
        if (checkJdbc40()) {
        message.setLength(0);
        try {

            /*  RETURN COLS:
             * <LI><B>FUNCTION_CAT</B> String => function catalog (may be <code>null</code>)
             *  <LI><B>FUNCTION_SCHEM</B> String => function schema (may be <code>null</code>)
             *  <LI><B>FUNCTION_NAME</B> String => function name.  This is the name
             * used to invoke the function
             *  <LI><B>COLUMN_NAME</B> String => column/parameter name
             *  <LI><B>COLUMN_TYPE</B> Short => kind of column/parameter:
             *      <UL>
             *      <LI> functionColumnUnknown - nobody knows
             *      <LI> functionColumnIn - IN parameter
             *      <LI> functionColumnInOut - INOUT parameter
             *      <LI> functionColumnOut - OUT parameter
             *      <LI> functionColumnReturn - function return value
             *      <LI> functionColumnResult - Indicates that the parameter or column
             *  is a column in the <code>ResultSet</code>
             *      </UL>
             *  <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
             *  <LI><B>TYPE_NAME</B> String => SQL type name, for a UDT type the
             *  type name is fully qualified
             *  <LI><B>PRECISION</B> int => precision
             *  <LI><B>LENGTH</B> int => length in bytes of data
             *  <LI><B>SCALE</B> short => scale -  null is returned for data types where
             * SCALE is not applicable.
             *  <LI><B>RADIX</B> short => radix
             *  <LI><B>NULLABLE</B> short => can it contain NULL.
             *      <UL>
             *      <LI> functionNoNulls - does not allow NULL values
             *      <LI> functionNullable - allows NULL values
             *      <LI> functionNullableUnknown - nullability unknown
             *      </UL>
             *  <LI><B>REMARKS</B> String => comment describing column/parameter
             *  <LI><B>CHAR_OCTET_LENGTH</B> int  => the maximum length of binary
             * and character based parameters or columns.  For any other datatype the returned value
             * is a NULL
             *  <LI><B>ORDINAL_POSITION</B> int  => the ordinal position, starting
             *  <LI><B>IS_NULLABLE</B> String
             *  <LI><B>SPECIFIC_NAME</B> String*/


            ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,
                  "getFunctionColumns",
                  null,
                  JDDMDTest.COLLECTION,
                  "JDMDSMALLINT",
                  null);


            boolean success = true;

            int rows = 0;
            while (rs.next()) {
                ++rows;


                success = JDDMDTest.check(rs.getString("FUNCTION_CAT"), connectionCatalog_, "FUNCTION_CAT", "NULL", message, success) ;
                success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"), JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION, message, success);
                success = JDDMDTest.check(rs.getString("FUNCTION_NAME"), "JDMDSMALLINT", "FUNCTION_NAME", "NULL", message, success);
		if (rows == 1) {
		    success = JDDMDTest.check(rs.getString("COLUMN_NAME"), null, "COLUMN_NAME", "For row "+rows, message, success);
		} else {
		    success = JDDMDTest.check(rs.getString("COLUMN_NAME"), "JDMDSIP1", "COLUMN_NAME", "For row "+rows, message, success);
		}
                //success = JDDMDTest.check(rs.getString("COLUMN_TYPE"), "1", "COLUMN_TYPE", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("DATA_TYPE"), "5", "DATA_TYPE", "NULL", message, success);

                success = JDDMDTest.check(rs.getString("TYPE_NAME"), "SMALLINT", "TYPE_NAME", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("PRECISION"), "5", "PRECISION", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("LENGTH"), "2", "LENGTH", "NULL", message, success);

                success = JDDMDTest.check(rs.getString("SCALE"), "0", "SCALE", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("RADIX"), "10", "RADIX", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("NULLABLE"), "1", "NULLABLE", "NULL", message, success);

                success = JDDMDTest.check(rs.getString("REMARKS"), null, "REMARKS", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("CHAR_OCTET_LENGTH"), null, "CHAR_OCTET_LENGTH", "NULL", message, success);
                //success = JDDMDTest.check(rs.getString("ORDINAL_POSITION"), "1", "ORDINAL_POSITION", "NULL", message, success);

                success = JDDMDTest.check(rs.getString("IS_NULLABLE"), "YES", "IS_NULLABLE", "NULL", message, success);
                success = JDDMDTest.check(rs.getString("SPECIFIC_NAME"), "JDMDSMALLINT", "SPECIFIC_NAME", "NULL", message, success);

            }

            rs.close();
            if (rows != 2)
                message.append("rows = " + rows + " sb 2 ");
            assertCondition((rows == 2) && success,  "\n"+message.toString() );

        }
        catch (Exception e)  {
            failed (e, "01/25/07 New jdbc 40 TC.  Unexpected Exception");
        }
        }
    }


    String expectRows="EXPECT ROWS";
    String expectZeroRows="EXPECT ZERO ROWS";

    void testPattern(String catalog, String schemaPattern,
      String functionNamePattern, String columnNamePattern,
      String expectedResults) {
    message.setLength(0);
    message.append("Testing '" + catalog + "', '" + schemaPattern + "', '"
        + functionNamePattern + "', '" + columnNamePattern + "', "
        + expectedResults + "\n");
    if (checkJdbc40()) {
      try {
        ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,
            "getFunctionColumns", catalog, schemaPattern, functionNamePattern,
            columnNamePattern);
        boolean foundRows = rs.next();

        if (expectedResults == expectRows) {
          // Dont forget to close the rs -- otherwise they will pile up and
          // cause a SQL7049
          rs.close();
          assertCondition(foundRows, "No rows found for " + message.toString());
        } else if (expectedResults == expectZeroRows) {
          if (foundRows) {
            message.append("Found a row when not expected \n");
            int columns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++) {
              message.append(rs.getString(i));
              if (i < columns)
                message.append(",");
            }
            message.append("\n");
          }
          rs.close();
          assertCondition(!foundRows, "Rows found for " + message.toString());
        } else {
          failed("expectedResults '" + expectedResults + "' not valid");
        }

      } catch (Exception e) {
        System.out.println("******* Exception Caught ******\n");
        failed(e, message.toString());
      }
    }

  }

    /**
     * Valid ENTRY is  connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "P1"
     * Testing all combinations of catalog, schemaPattern, functionNamePattern, columnNamePattern
       catalog -- NULL                -- return rows
               -- ""                  -- return 0 rows
               -- connectionCatalog_  -- return rows ,
               -- "%"                 -- return 0 rows
     * schemaPattern -- NULL                         -- return rows
     *               -- ""                           -- return 0 rows
     *               -- JDDMDTest.COLLECTION         -- return rows
     *               -- JDDMDTest.SCHEMAS_PERCENT    -- return rows
     *               -- JDDMDTest.SCHEAMS_UNDERSCORE -- return rows
     *               -- "%X"                         -- return 0 rows
     * functionNamePattern -- NULL                   -- return rows
     *                     -- ""                     -- return 0 rows
     *                     -- "JDMDSMALLINT"         -- return 1 rows
     *                     -- "JDMDSM%LLINT"         -- return 1 rows
     *                     -- "JDMDSM_LLINT"         -- return 1 rows
     *                     -- "%X23531%"             -- return 0 rows
     * columnNamePatter    -- NULL                   -- return rows
     *                     -- ""                     -- return 0 rows
     *                     -- "JDMDSIP1"             -- return rows
     *                     -- "JDM%P1"               -- return rows
     *                     -- "JDMD_IP1"             -- return rows
     *                     -- "%X23531%"             -- return 0 rows
     *
     */

    /* Testing columnNamePattern only */
    /* NOTE:  For DB2 empty string for column name is same as null.  */
    /*        This is the same behavior as SQLProcedureCols */
    public void Var003() { testPattern(null, null, null, "",         expectRows); }
    public void Var004() { testPattern(null, null, null, "JDMDSIP1", expectRows); }
    public void Var005() { testPattern(null, null, null, "JDMD%P1",  expectRows); }
    public void Var006() { testPattern(null, null, null, "JDMD_IP1", expectRows); }
    public void Var007() { testPattern(null, null, null, "%X23531%", expectZeroRows); }


    /* Testing functionNamePattern and ColumnNamePattern */
    public void Var008() { testPattern(null, null, "",   null,       expectZeroRows); }
    public void Var009() { testPattern(null, null, "",   "",         expectZeroRows); }
    public void Var010() { testPattern(null, null, "",   "JDMDSIP1", expectZeroRows); }
    public void Var011() { testPattern(null, null, "",   "JDMD%P1",  expectZeroRows); }
    public void Var012() { testPattern(null, null, "",   "JDMD_IP1", expectZeroRows); }
    public void Var013() { testPattern(null, null, "",   "%X23531%", expectZeroRows); }

    public void Var014() { testPattern(null, null, "JDMDSMALLINT", null,       expectRows); }
    public void Var015() { testPattern(null, null, "JDMDSMALLINT", "",         expectRows); }
    public void Var016() { testPattern(null, null, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var017() { testPattern(null, null, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var018() { testPattern(null, null, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var019() { testPattern(null, null, "JDMDSMALLINT", "%X23531%", expectZeroRows); }

    public void Var020() { testPattern(null, null, "JDMDSM%NT",  null,       expectRows); }
    public void Var021() { testPattern(null, null, "JDMDSM%INT", "",         expectRows); }
    public void Var022() { testPattern(null, null, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var023() { testPattern(null, null, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var024() { testPattern(null, null, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var025() { testPattern(null, null, "JDMD%INT",   "%X23531%", expectZeroRows); }

    public void Var026() { testPattern(null, null, "_DMDSMALLINT", null,       expectRows); }
    public void Var027() { testPattern(null, null, "_DMDSMALLINT", "",         expectRows); }
    public void Var028() { testPattern(null, null, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var029() { testPattern(null, null, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var030() { testPattern(null, null, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var031() { testPattern(null, null, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }

    public void Var032() { testPattern(null, null, "%X23531%",   null,       expectZeroRows); }
    public void Var033() { testPattern(null, null, "%X23531%",   "",         expectZeroRows); }
    public void Var034() { testPattern(null, null, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var035() { testPattern(null, null, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var036() { testPattern(null, null, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var037() { testPattern(null, null, "%X23531%",   "%X23531%", expectZeroRows); }



    /* Testing schemaNamePattern, ..... */
    public void Var038() { testPattern(null, "", null, "",         expectZeroRows); }
    public void Var039() { testPattern(null, "", null, "JDMDSIP1", expectZeroRows); }
    public void Var040() { testPattern(null, "", null, "JDMD%P1",  expectZeroRows); }
    public void Var041() { testPattern(null, "", null, "JDMD_IP1", expectZeroRows); }
    public void Var042() { testPattern(null, "", null, "%X23531%", expectZeroRows); }
    public void Var043() { testPattern(null, "", "",   null,       expectZeroRows); }
    public void Var044() { testPattern(null, "", "",   "",         expectZeroRows); }
    public void Var045() { testPattern(null, "", "",   "JDMDSIP1", expectZeroRows); }
    public void Var046() { testPattern(null, "", "",   "JDMD%P1",  expectZeroRows); }
    public void Var047() { testPattern(null, "", "",   "JDMD_IP1", expectZeroRows); }
    public void Var048() { testPattern(null, "", "",   "%X23531%", expectZeroRows); }
    public void Var049() { testPattern(null, "", "JDMDSMALLINT", null,       expectZeroRows); }
    public void Var050() { testPattern(null, "", "JDMDSMALLINT", "",         expectZeroRows); }
    public void Var051() { testPattern(null, "", "JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var052() { testPattern(null, "", "JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var053() { testPattern(null, "", "JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var054() { testPattern(null, "", "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var055() { testPattern(null, "", "JDMDSM%NT",  null,       expectZeroRows); }
    public void Var056() { testPattern(null, "", "JDMDSM%INT", "",         expectZeroRows); }
    public void Var057() { testPattern(null, "", "JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var058() { testPattern(null, "", "%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var059() { testPattern(null, "", "JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var060() { testPattern(null, "", "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var061() { testPattern(null, "", "_DMDSMALLINT", null,       expectZeroRows); }
    public void Var062() { testPattern(null, "", "_DMDSMALLINT", "",         expectZeroRows); }
    public void Var063() { testPattern(null, "", "_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var064() { testPattern(null, "", "JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var065() { testPattern(null, "", "JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var066() { testPattern(null, "", "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var067() { testPattern(null, "", "%X23531%",   null,       expectZeroRows); }
    public void Var068() { testPattern(null, "", "%X23531%",   "",         expectZeroRows); }
    public void Var069() { testPattern(null, "", "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var070() { testPattern(null, "", "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var071() { testPattern(null, "", "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var072() { testPattern(null, "", "%X23531%",   "%X23531%", expectZeroRows); }


    public void Var073() { testPattern(null, JDDMDTest.COLLECTION, null, "",         expectRows); }
    public void Var074() { testPattern(null, JDDMDTest.COLLECTION, null, "JDMDSIP1", expectRows); }
    public void Var075() { testPattern(null, JDDMDTest.COLLECTION, null, "JDMD%P1",  expectRows); }
    public void Var076() { testPattern(null, JDDMDTest.COLLECTION, null, "JDMD_IP1", expectRows); }
    public void Var077() { testPattern(null, JDDMDTest.COLLECTION, null, "%X23531%", expectZeroRows); }
    public void Var078() { testPattern(null, JDDMDTest.COLLECTION, "",   null,       expectZeroRows); }
    public void Var079() { testPattern(null, JDDMDTest.COLLECTION, "",   "",         expectZeroRows); }
    public void Var080() { testPattern(null, JDDMDTest.COLLECTION, "",   "JDMDSIP1", expectZeroRows); }
    public void Var081() { testPattern(null, JDDMDTest.COLLECTION, "",   "JDMD%P1",  expectZeroRows); }
    public void Var082() { testPattern(null, JDDMDTest.COLLECTION, "",   "JDMD_IP1", expectZeroRows); }
    public void Var083() { testPattern(null, JDDMDTest.COLLECTION, "",   "%X23531%", expectZeroRows); }
    public void Var084() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", null,       expectRows); }
    public void Var085() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", "",         expectRows); }
    public void Var086() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var087() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var088() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var089() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var090() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSM%NT",  null,       expectRows); }
    public void Var091() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSM%INT", "",         expectRows); }
    public void Var092() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var093() { testPattern(null, JDDMDTest.COLLECTION, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var094() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var095() { testPattern(null, JDDMDTest.COLLECTION, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var096() { testPattern(null, JDDMDTest.COLLECTION, "_DMDSMALLINT", null,       expectRows); }
    public void Var097() { testPattern(null, JDDMDTest.COLLECTION, "_DMDSMALLINT", "",         expectRows); }
    public void Var098() { testPattern(null, JDDMDTest.COLLECTION, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var099() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var100() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var101() { testPattern(null, JDDMDTest.COLLECTION, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var102() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   null,       expectZeroRows); }
    public void Var103() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   "",         expectZeroRows); }
    public void Var104() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var105() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var106() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var107() { testPattern(null, JDDMDTest.COLLECTION, "%X23531%",   "%X23531%", expectZeroRows); }


    public void Var108() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,null, "",         expectRows); }
    public void Var109() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,null, "JDMDSIP1", expectRows); }
    public void Var110() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,null, "JDMD%P1",  expectRows); }
    public void Var111() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,null, "JDMD_IP1", expectRows); }
    public void Var112() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,null, "%X23531%", expectZeroRows); }
    public void Var113() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   null,       expectZeroRows); }
    public void Var114() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   "",         expectZeroRows); }
    public void Var115() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMDSIP1", expectZeroRows); }
    public void Var116() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD%P1",  expectZeroRows); }
    public void Var117() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD_IP1", expectZeroRows); }
    public void Var118() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"",   "%X23531%", expectZeroRows); }
    public void Var119() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", null,       expectRows); }
    public void Var120() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "",         expectRows); }
    public void Var121() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var122() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var123() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var124() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var125() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%NT",  null,       expectRows); }
    public void Var126() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "",         expectRows); }
    public void Var127() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var128() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var129() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var130() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var131() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", null,       expectRows); }
    public void Var132() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "",         expectRows); }
    public void Var133() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var134() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var135() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var136() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var137() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   null,       expectZeroRows); }
    public void Var138() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "",         expectZeroRows); }
    public void Var139() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var140() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var141() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var142() { testPattern(null, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "%X23531%", expectZeroRows); }


    public void Var143() { testPattern(null, schemasUnderscore,null, "",         expectRows); }
    public void Var144() { testPattern(null, schemasUnderscore,null, "JDMDSIP1", expectRows); }
    public void Var145() { testPattern(null, schemasUnderscore,null, "JDMD%P1",  expectRows); }
    public void Var146() { testPattern(null, schemasUnderscore,null, "JDMD_IP1", expectRows); }
    public void Var147() { testPattern(null, schemasUnderscore,null, "%X23531%", expectZeroRows); }
    public void Var148() { testPattern(null, schemasUnderscore,"",   null,       expectZeroRows); }
    public void Var149() { testPattern(null, schemasUnderscore,"",   "",         expectZeroRows); }
    public void Var150() { testPattern(null, schemasUnderscore,"",   "JDMDSIP1", expectZeroRows); }
    public void Var151() { testPattern(null, schemasUnderscore,"",   "JDMD%P1",  expectZeroRows); }
    public void Var152() { testPattern(null, schemasUnderscore,"",   "JDMD_IP1", expectZeroRows); }
    public void Var153() { testPattern(null, schemasUnderscore,"",   "%X23531%", expectZeroRows); }
    public void Var154() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", null,       expectRows); }
    public void Var155() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", "",         expectRows); }
    public void Var156() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var157() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var158() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var159() { testPattern(null, schemasUnderscore,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var160() { testPattern(null, schemasUnderscore,"JDMDSM%NT",  null,       expectRows); }
    public void Var161() { testPattern(null, schemasUnderscore,"JDMDSM%INT", "",         expectRows); }
    public void Var162() { testPattern(null, schemasUnderscore,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var163() { testPattern(null, schemasUnderscore,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var164() { testPattern(null, schemasUnderscore,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var165() { testPattern(null, schemasUnderscore,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var166() { testPattern(null, schemasUnderscore,"_DMDSMALLINT", null,       expectRows); }
    public void Var167() { testPattern(null, schemasUnderscore,"_DMDSMALLINT", "",         expectRows); }
    public void Var168() { testPattern(null, schemasUnderscore,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var169() { testPattern(null, schemasUnderscore,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var170() { testPattern(null, schemasUnderscore,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var171() { testPattern(null, schemasUnderscore,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var172() { testPattern(null, schemasUnderscore,"%X23531%",   null,       expectZeroRows); }
    public void Var173() { testPattern(null, schemasUnderscore,"%X23531%",   "",         expectZeroRows); }
    public void Var174() { testPattern(null, schemasUnderscore,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var175() { testPattern(null, schemasUnderscore,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var176() { testPattern(null, schemasUnderscore,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var177() { testPattern(null, schemasUnderscore,"%X23531%",   "%X23531%", expectZeroRows); }


    public void Var178() { testPattern(null, "%X235353%",null, "",         expectZeroRows); }
    public void Var179() { testPattern(null, "%X235353%",null, "JDMDSIP1", expectZeroRows); }
    public void Var180() { testPattern(null, "%X235353%",null, "JDMD%P1",  expectZeroRows); }
    public void Var181() { testPattern(null, "%X235353%",null, "JDMD_IP1", expectZeroRows); }
    public void Var182() { testPattern(null, "%X235353%",null, "%X23531%", expectZeroRows); }
    public void Var183() { testPattern(null, "%X235353%","",   null,       expectZeroRows); }
    public void Var184() { testPattern(null, "%X235353%","",   "",         expectZeroRows); }
    public void Var185() { testPattern(null, "%X235353%","",   "JDMDSIP1", expectZeroRows); }
    public void Var186() { testPattern(null, "%X235353%","",   "JDMD%P1",  expectZeroRows); }
    public void Var187() { testPattern(null, "%X235353%","",   "JDMD_IP1", expectZeroRows); }
    public void Var188() { testPattern(null, "%X235353%","",   "%X23531%", expectZeroRows); }
    public void Var189() { testPattern(null, "%X235353%","JDMDSMALLINT", null,       expectZeroRows); }
    public void Var190() { testPattern(null, "%X235353%","JDMDSMALLINT", "",         expectZeroRows); }
    public void Var191() { testPattern(null, "%X235353%","JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var192() { testPattern(null, "%X235353%","JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var193() { testPattern(null, "%X235353%","JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var194() { testPattern(null, "%X235353%","JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var195() { testPattern(null, "%X235353%","JDMDSM%NT",  null,       expectZeroRows); }
    public void Var196() { testPattern(null, "%X235353%","JDMDSM%INT", "",         expectZeroRows); }
    public void Var197() { testPattern(null, "%X235353%","JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var198() { testPattern(null, "%X235353%","%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var199() { testPattern(null, "%X235353%","JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var200() { testPattern(null, "%X235353%","JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var201() { testPattern(null, "%X235353%","_DMDSMALLINT", null,       expectZeroRows); }
    public void Var202() { testPattern(null, "%X235353%","_DMDSMALLINT", "",         expectZeroRows); }
    public void Var203() { testPattern(null, "%X235353%","_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var204() { testPattern(null, "%X235353%","JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var205() { testPattern(null, "%X235353%","JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var206() { testPattern(null, "%X235353%","JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var207() { testPattern(null, "%X235353%","%X23531%",   null,       expectZeroRows); }
    public void Var208() { testPattern(null, "%X235353%","%X23531%",   "",         expectZeroRows); }
    public void Var209() { testPattern(null, "%X235353%","%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var210() { testPattern(null, "%X235353%","%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var211() { testPattern(null, "%X235353%","%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var212() { testPattern(null, "%X235353%","%X23531%",   "%X23531%", expectZeroRows); }



    /*
     * Now switch catalog
     */


    public void Var213() { testPattern(connectionCatalog_, null, null, "",         expectRows); }
    public void Var214() { testPattern(connectionCatalog_, null, null, "JDMDSIP1", expectRows); }
    public void Var215() { testPattern(connectionCatalog_, null, null, "JDMD%P1",  expectRows); }
    public void Var216() { testPattern(connectionCatalog_, null, null, "JDMD_IP1", expectRows); }
    public void Var217() { testPattern(connectionCatalog_, null, null, "%X23531%", expectZeroRows); }
    public void Var218() { testPattern(connectionCatalog_, null, "",   null,       expectZeroRows); }
    public void Var219() { testPattern(connectionCatalog_, null, "",   "",         expectZeroRows); }
    public void Var220() { testPattern(connectionCatalog_, null, "",   "JDMDSIP1", expectZeroRows); }
    public void Var221() { testPattern(connectionCatalog_, null, "",   "JDMD%P1",  expectZeroRows); }
    public void Var222() { testPattern(connectionCatalog_, null, "",   "JDMD_IP1", expectZeroRows); }
    public void Var223() { testPattern(connectionCatalog_, null, "",   "%X23531%", expectZeroRows); }
    public void Var224() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", null,       expectRows); }
    public void Var225() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", "",         expectRows); }
    public void Var226() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var227() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var228() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var229() { testPattern(connectionCatalog_, null, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var230() { testPattern(connectionCatalog_, null, "JDMDSM%NT",  null,       expectRows); }
    public void Var231() { testPattern(connectionCatalog_, null, "JDMDSM%INT", "",         expectRows); }
    public void Var232() { testPattern(connectionCatalog_, null, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var233() { testPattern(connectionCatalog_, null, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var234() { testPattern(connectionCatalog_, null, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var235() { testPattern(connectionCatalog_, null, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var236() { testPattern(connectionCatalog_, null, "_DMDSMALLINT", null,       expectRows); }
    public void Var237() { testPattern(connectionCatalog_, null, "_DMDSMALLINT", "",         expectRows); }
    public void Var238() { testPattern(connectionCatalog_, null, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var239() { testPattern(connectionCatalog_, null, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var240() { testPattern(connectionCatalog_, null, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var241() { testPattern(connectionCatalog_, null, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var242() { testPattern(connectionCatalog_, null, "%X23531%",   null,       expectZeroRows); }
    public void Var243() { testPattern(connectionCatalog_, null, "%X23531%",   "",         expectZeroRows); }
    public void Var244() { testPattern(connectionCatalog_, null, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var245() { testPattern(connectionCatalog_, null, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var246() { testPattern(connectionCatalog_, null, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var247() { testPattern(connectionCatalog_, null, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var248() { testPattern(connectionCatalog_, "", null, "",         expectZeroRows); }
    public void Var249() { testPattern(connectionCatalog_, "", null, "JDMDSIP1", expectZeroRows); }
    public void Var250() { testPattern(connectionCatalog_, "", null, "JDMD%P1",  expectZeroRows); }
    public void Var251() { testPattern(connectionCatalog_, "", null, "JDMD_IP1", expectZeroRows); }
    public void Var252() { testPattern(connectionCatalog_, "", null, "%X23531%", expectZeroRows); }
    public void Var253() { testPattern(connectionCatalog_, "", "",   null,       expectZeroRows); }
    public void Var254() { testPattern(connectionCatalog_, "", "",   "",         expectZeroRows); }
    public void Var255() { testPattern(connectionCatalog_, "", "",   "JDMDSIP1", expectZeroRows); }
    public void Var256() { testPattern(connectionCatalog_, "", "",   "JDMD%P1",  expectZeroRows); }
    public void Var257() { testPattern(connectionCatalog_, "", "",   "JDMD_IP1", expectZeroRows); }
    public void Var258() { testPattern(connectionCatalog_, "", "",   "%X23531%", expectZeroRows); }
    public void Var259() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", null,       expectZeroRows); }
    public void Var260() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", "",         expectZeroRows); }
    public void Var261() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var262() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var263() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var264() { testPattern(connectionCatalog_, "", "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var265() { testPattern(connectionCatalog_, "", "JDMDSM%NT",  null,       expectZeroRows); }
    public void Var266() { testPattern(connectionCatalog_, "", "JDMDSM%INT", "",         expectZeroRows); }
    public void Var267() { testPattern(connectionCatalog_, "", "JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var268() { testPattern(connectionCatalog_, "", "%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var269() { testPattern(connectionCatalog_, "", "JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var270() { testPattern(connectionCatalog_, "", "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var271() { testPattern(connectionCatalog_, "", "_DMDSMALLINT", null,       expectZeroRows); }
    public void Var272() { testPattern(connectionCatalog_, "", "_DMDSMALLINT", "",         expectZeroRows); }
    public void Var273() { testPattern(connectionCatalog_, "", "_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var274() { testPattern(connectionCatalog_, "", "JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var275() { testPattern(connectionCatalog_, "", "JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var276() { testPattern(connectionCatalog_, "", "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var277() { testPattern(connectionCatalog_, "", "%X23531%",   null,       expectZeroRows); }
    public void Var278() { testPattern(connectionCatalog_, "", "%X23531%",   "",         expectZeroRows); }
    public void Var279() { testPattern(connectionCatalog_, "", "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var280() { testPattern(connectionCatalog_, "", "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var281() { testPattern(connectionCatalog_, "", "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var282() { testPattern(connectionCatalog_, "", "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var283() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null, "",         expectRows); }
    public void Var284() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null, "JDMDSIP1", expectRows); }
    public void Var285() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null, "JDMD%P1",  expectRows); }
    public void Var286() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null, "JDMD_IP1", expectRows); }
    public void Var287() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, null, "%X23531%", expectZeroRows); }
    public void Var288() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   null,       expectZeroRows); }
    public void Var289() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   "",         expectZeroRows); }
    public void Var290() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   "JDMDSIP1", expectZeroRows); }
    public void Var291() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   "JDMD%P1",  expectZeroRows); }
    public void Var292() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   "JDMD_IP1", expectZeroRows); }
    public void Var293() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "",   "%X23531%", expectZeroRows); }
    public void Var294() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", null,       expectRows); }
    public void Var295() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "",         expectRows); }
    public void Var296() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var297() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var298() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var299() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var300() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSM%NT",  null,       expectRows); }
    public void Var301() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSM%INT", "",         expectRows); }
    public void Var302() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var303() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var304() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var305() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var306() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "_DMDSMALLINT", null,       expectRows); }
    public void Var307() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "_DMDSMALLINT", "",         expectRows); }
    public void Var308() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var309() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var310() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var311() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var312() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   null,       expectZeroRows); }
    public void Var313() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   "",         expectZeroRows); }
    public void Var314() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var315() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var316() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var317() { testPattern(connectionCatalog_, JDDMDTest.COLLECTION, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var318() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,null, "",         expectRows); }
    public void Var319() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,null, "JDMDSIP1", expectRows); }
    public void Var320() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,null, "JDMD%P1",  expectRows); }
    public void Var321() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,null, "JDMD_IP1", expectRows); }
    public void Var322() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,null, "%X23531%", expectZeroRows); }
    public void Var323() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   null,       expectZeroRows); }
    public void Var324() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   "",         expectZeroRows); }
    public void Var325() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMDSIP1", expectZeroRows); }
    public void Var326() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD%P1",  expectZeroRows); }
    public void Var327() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD_IP1", expectZeroRows); }
    public void Var328() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"",   "%X23531%", expectZeroRows); }
    public void Var329() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", null,       expectRows); }
    public void Var330() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "",         expectRows); }
    public void Var331() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var332() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var333() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var334() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var335() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%NT",  null,       expectRows); }
    public void Var336() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "",         expectRows); }
    public void Var337() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var338() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var339() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var340() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var341() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", null,       expectRows); }
    public void Var342() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "",         expectRows); }
    public void Var343() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var344() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var345() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var346() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var347() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   null,       expectZeroRows); }
    public void Var348() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "",         expectZeroRows); }
    public void Var349() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var350() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var351() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var352() { testPattern(connectionCatalog_, JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var353() { testPattern(connectionCatalog_, schemasUnderscore,null, "",         expectRows); }
    public void Var354() { testPattern(connectionCatalog_, schemasUnderscore,null, "JDMDSIP1", expectRows); }
    public void Var355() { testPattern(connectionCatalog_, schemasUnderscore,null, "JDMD%P1",  expectRows); }
    public void Var356() { testPattern(connectionCatalog_, schemasUnderscore,null, "JDMD_IP1", expectRows); }
    public void Var357() { testPattern(connectionCatalog_, schemasUnderscore,null, "%X23531%", expectZeroRows); }
    public void Var358() { testPattern(connectionCatalog_, schemasUnderscore,"",   null,       expectZeroRows); }
    public void Var359() { testPattern(connectionCatalog_, schemasUnderscore,"",   "",         expectZeroRows); }
    public void Var360() { testPattern(connectionCatalog_, schemasUnderscore,"",   "JDMDSIP1", expectZeroRows); }
    public void Var361() { testPattern(connectionCatalog_, schemasUnderscore,"",   "JDMD%P1",  expectZeroRows); }
    public void Var362() { testPattern(connectionCatalog_, schemasUnderscore,"",   "JDMD_IP1", expectZeroRows); }
    public void Var363() { testPattern(connectionCatalog_, schemasUnderscore,"",   "%X23531%", expectZeroRows); }
    public void Var364() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", null,       expectRows); }
    public void Var365() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", "",         expectRows); }
    public void Var366() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var367() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var368() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var369() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var370() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSM%NT",  null,       expectRows); }
    public void Var371() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSM%INT", "",         expectRows); }
    public void Var372() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var373() { testPattern(connectionCatalog_, schemasUnderscore,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var374() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var375() { testPattern(connectionCatalog_, schemasUnderscore,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var376() { testPattern(connectionCatalog_, schemasUnderscore,"_DMDSMALLINT", null,       expectRows); }
    public void Var377() { testPattern(connectionCatalog_, schemasUnderscore,"_DMDSMALLINT", "",         expectRows); }
    public void Var378() { testPattern(connectionCatalog_, schemasUnderscore,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var379() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var380() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var381() { testPattern(connectionCatalog_, schemasUnderscore,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var382() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   null,       expectZeroRows); }
    public void Var383() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   "",         expectZeroRows); }
    public void Var384() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var385() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var386() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var387() { testPattern(connectionCatalog_, schemasUnderscore,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var388() { testPattern(connectionCatalog_, "%X235353%",null, "",         expectZeroRows); }
    public void Var389() { testPattern(connectionCatalog_, "%X235353%",null, "JDMDSIP1", expectZeroRows); }
    public void Var390() { testPattern(connectionCatalog_, "%X235353%",null, "JDMD%P1",  expectZeroRows); }
    public void Var391() { testPattern(connectionCatalog_, "%X235353%",null, "JDMD_IP1", expectZeroRows); }
    public void Var392() { testPattern(connectionCatalog_, "%X235353%",null, "%X23531%", expectZeroRows); }
    public void Var393() { testPattern(connectionCatalog_, "%X235353%","",   null,       expectZeroRows); }
    public void Var394() { testPattern(connectionCatalog_, "%X235353%","",   "",         expectZeroRows); }
    public void Var395() { testPattern(connectionCatalog_, "%X235353%","",   "JDMDSIP1", expectZeroRows); }
    public void Var396() { testPattern(connectionCatalog_, "%X235353%","",   "JDMD%P1",  expectZeroRows); }
    public void Var397() { testPattern(connectionCatalog_, "%X235353%","",   "JDMD_IP1", expectZeroRows); }
    public void Var398() { testPattern(connectionCatalog_, "%X235353%","",   "%X23531%", expectZeroRows); }
    public void Var399() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", null,       expectZeroRows); }
    public void Var400() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", "",         expectZeroRows); }
    public void Var401() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var402() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var403() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var404() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var405() { testPattern(connectionCatalog_, "%X235353%","JDMDSM%NT",  null,       expectZeroRows); }
    public void Var406() { testPattern(connectionCatalog_, "%X235353%","JDMDSM%INT", "",         expectZeroRows); }
    public void Var407() { testPattern(connectionCatalog_, "%X235353%","JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var408() { testPattern(connectionCatalog_, "%X235353%","%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var409() { testPattern(connectionCatalog_, "%X235353%","JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var410() { testPattern(connectionCatalog_, "%X235353%","JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var411() { testPattern(connectionCatalog_, "%X235353%","_DMDSMALLINT", null,       expectZeroRows); }
    public void Var412() { testPattern(connectionCatalog_, "%X235353%","_DMDSMALLINT", "",         expectZeroRows); }
    public void Var413() { testPattern(connectionCatalog_, "%X235353%","_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var414() { testPattern(connectionCatalog_, "%X235353%","JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var415() { testPattern(connectionCatalog_, "%X235353%","JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var416() { testPattern(connectionCatalog_, "%X235353%","JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var417() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   null,       expectZeroRows); }
    public void Var418() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   "",         expectZeroRows); }
    public void Var419() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var420() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var421() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var422() { testPattern(connectionCatalog_, "%X235353%","%X23531%",   "%X23531%", expectZeroRows); }


    //
    // Note.. LUW returns rsults for first parameter as '%' for SQLPROCEDUREColumns
    //
    public void Var423() { testPattern("%", null, null, "",         expectRows); }
    public void Var424() { testPattern("%", null, null, "JDMDSIP1", expectRows); }
    public void Var425() { testPattern("%", null, null, "JDMD%P1",  expectRows); }
    public void Var426() { testPattern("%", null, null, "JDMD_IP1", expectRows); }
    public void Var427() { testPattern("%", null, null, "%X23531%", expectZeroRows); }
    public void Var428() { testPattern("%", null, "",   null,       expectZeroRows); }
    public void Var429() { testPattern("%", null, "",   "",         expectZeroRows); }
    public void Var430() { testPattern("%", null, "",   "JDMDSIP1", expectZeroRows); }
    public void Var431() { testPattern("%", null, "",   "JDMD%P1",  expectZeroRows); }
    public void Var432() { testPattern("%", null, "",   "JDMD_IP1", expectZeroRows); }
    public void Var433() { testPattern("%", null, "",   "%X23531%", expectZeroRows); }
    public void Var434() { testPattern("%", null, "JDMDSMALLINT", null,       expectRows); }
    public void Var435() { testPattern("%", null, "JDMDSMALLINT", "",         expectRows); }
    public void Var436() { testPattern("%", null, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var437() { testPattern("%", null, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var438() { testPattern("%", null, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var439() { testPattern("%", null, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var440() { testPattern("%", null, "JDMDSM%NT",  null,       expectRows); }
    public void Var441() { testPattern("%", null, "JDMDSM%INT", "",         expectRows); }
    public void Var442() { testPattern("%", null, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var443() { testPattern("%", null, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var444() { testPattern("%", null, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var445() { testPattern("%", null, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var446() { testPattern("%", null, "_DMDSMALLINT", null,       expectRows); }
    public void Var447() { testPattern("%", null, "_DMDSMALLINT", "",         expectRows); }
    public void Var448() { testPattern("%", null, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var449() { testPattern("%", null, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var450() { testPattern("%", null, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var451() { testPattern("%", null, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var452() { testPattern("%", null, "%X23531%",   null,       expectZeroRows); }
    public void Var453() { testPattern("%", null, "%X23531%",   "",         expectZeroRows); }
    public void Var454() { testPattern("%", null, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var455() { testPattern("%", null, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var456() { testPattern("%", null, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var457() { testPattern("%", null, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var458() { testPattern("%", "", null, "",         expectZeroRows); }
    public void Var459() { testPattern("%", "", null, "JDMDSIP1", expectZeroRows); }
    public void Var460() { testPattern("%", "", null, "JDMD%P1",  expectZeroRows); }
    public void Var461() { testPattern("%", "", null, "JDMD_IP1", expectZeroRows); }
    public void Var462() { testPattern("%", "", null, "%X23531%", expectZeroRows); }
    public void Var463() { testPattern("%", "", "",   null,       expectZeroRows); }
    public void Var464() { testPattern("%", "", "",   "",         expectZeroRows); }
    public void Var465() { testPattern("%", "", "",   "JDMDSIP1", expectZeroRows); }
    public void Var466() { testPattern("%", "", "",   "JDMD%P1",  expectZeroRows); }
    public void Var467() { testPattern("%", "", "",   "JDMD_IP1", expectZeroRows); }
    public void Var468() { testPattern("%", "", "",   "%X23531%", expectZeroRows); }
    public void Var469() { testPattern("%", "", "JDMDSMALLINT", null,       expectZeroRows); }
    public void Var470() { testPattern("%", "", "JDMDSMALLINT", "",         expectZeroRows); }
    public void Var471() { testPattern("%", "", "JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var472() { testPattern("%", "", "JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var473() { testPattern("%", "", "JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var474() { testPattern("%", "", "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var475() { testPattern("%", "", "JDMDSM%NT",  null,       expectZeroRows); }
    public void Var476() { testPattern("%", "", "JDMDSM%INT", "",         expectZeroRows); }
    public void Var477() { testPattern("%", "", "JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var478() { testPattern("%", "", "%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var479() { testPattern("%", "", "JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var480() { testPattern("%", "", "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var481() { testPattern("%", "", "_DMDSMALLINT", null,       expectZeroRows); }
    public void Var482() { testPattern("%", "", "_DMDSMALLINT", "",         expectZeroRows); }
    public void Var483() { testPattern("%", "", "_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var484() { testPattern("%", "", "JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var485() { testPattern("%", "", "JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var486() { testPattern("%", "", "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var487() { testPattern("%", "", "%X23531%",   null,       expectZeroRows); }
    public void Var488() { testPattern("%", "", "%X23531%",   "",         expectZeroRows); }
    public void Var489() { testPattern("%", "", "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var490() { testPattern("%", "", "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var491() { testPattern("%", "", "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var492() { testPattern("%", "", "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var493() { testPattern("%", JDDMDTest.COLLECTION, null, "",         expectRows); }
    public void Var494() { testPattern("%", JDDMDTest.COLLECTION, null, "JDMDSIP1", expectRows); }
    public void Var495() { testPattern("%", JDDMDTest.COLLECTION, null, "JDMD%P1",  expectRows); }
    public void Var496() { testPattern("%", JDDMDTest.COLLECTION, null, "JDMD_IP1", expectRows); }
    public void Var497() { testPattern("%", JDDMDTest.COLLECTION, null, "%X23531%", expectZeroRows); }
    public void Var498() { testPattern("%", JDDMDTest.COLLECTION, "",   null,       expectZeroRows); }
    public void Var499() { testPattern("%", JDDMDTest.COLLECTION, "",   "",         expectZeroRows); }
    public void Var500() { testPattern("%", JDDMDTest.COLLECTION, "",   "JDMDSIP1", expectZeroRows); }
    public void Var501() { testPattern("%", JDDMDTest.COLLECTION, "",   "JDMD%P1",  expectZeroRows); }
    public void Var502() { testPattern("%", JDDMDTest.COLLECTION, "",   "JDMD_IP1", expectZeroRows); }
    public void Var503() { testPattern("%", JDDMDTest.COLLECTION, "",   "%X23531%", expectZeroRows); }
    public void Var504() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", null,       expectRows); }
    public void Var505() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", "",         expectRows); }
    public void Var506() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var507() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var508() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var509() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var510() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSM%NT",  null,       expectRows); }
    public void Var511() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSM%INT", "",         expectRows); }
    public void Var512() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var513() { testPattern("%", JDDMDTest.COLLECTION, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var514() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var515() { testPattern("%", JDDMDTest.COLLECTION, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var516() { testPattern("%", JDDMDTest.COLLECTION, "_DMDSMALLINT", null,       expectRows); }
    public void Var517() { testPattern("%", JDDMDTest.COLLECTION, "_DMDSMALLINT", "",         expectRows); }
    public void Var518() { testPattern("%", JDDMDTest.COLLECTION, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var519() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var520() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var521() { testPattern("%", JDDMDTest.COLLECTION, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var522() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   null,       expectZeroRows); }
    public void Var523() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   "",         expectZeroRows); }
    public void Var524() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var525() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var526() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var527() { testPattern("%", JDDMDTest.COLLECTION, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var528() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,null, "",         expectRows); }
    public void Var529() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,null, "JDMDSIP1", expectRows); }
    public void Var530() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,null, "JDMD%P1",  expectRows); }
    public void Var531() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,null, "JDMD_IP1", expectRows); }
    public void Var532() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,null, "%X23531%", expectZeroRows); }
    public void Var533() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   null,       expectZeroRows); }
    public void Var534() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   "",         expectZeroRows); }
    public void Var535() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMDSIP1", expectZeroRows); }
    public void Var536() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD%P1",  expectZeroRows); }
    public void Var537() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD_IP1", expectZeroRows); }
    public void Var538() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"",   "%X23531%", expectZeroRows); }
    public void Var539() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", null,       expectRows); }
    public void Var540() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "",         expectRows); }
    public void Var541() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var542() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var543() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var544() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var545() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%NT",  null,       expectRows); }
    public void Var546() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "",         expectRows); }
    public void Var547() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var548() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var549() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var550() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var551() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", null,       expectRows); }
    public void Var552() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "",         expectRows); }
    public void Var553() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var554() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var555() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var556() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var557() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   null,       expectZeroRows); }
    public void Var558() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "",         expectZeroRows); }
    public void Var559() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var560() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var561() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var562() { testPattern("%", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var563() { testPattern("%", schemasUnderscore,null, "",         expectRows); }
    public void Var564() { testPattern("%", schemasUnderscore,null, "JDMDSIP1", expectRows); }
    public void Var565() { testPattern("%", schemasUnderscore,null, "JDMD%P1",  expectRows); }
    public void Var566() { testPattern("%", schemasUnderscore,null, "JDMD_IP1", expectRows); }
    public void Var567() { testPattern("%", schemasUnderscore,null, "%X23531%", expectZeroRows); }
    public void Var568() { testPattern("%", schemasUnderscore,"",   null,       expectZeroRows); }
    public void Var569() { testPattern("%", schemasUnderscore,"",   "",         expectZeroRows); }
    public void Var570() { testPattern("%", schemasUnderscore,"",   "JDMDSIP1", expectZeroRows); }
    public void Var571() { testPattern("%", schemasUnderscore,"",   "JDMD%P1",  expectZeroRows); }
    public void Var572() { testPattern("%", schemasUnderscore,"",   "JDMD_IP1", expectZeroRows); }
    public void Var573() { testPattern("%", schemasUnderscore,"",   "%X23531%", expectZeroRows); }
    public void Var574() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", null,       expectRows); }
    public void Var575() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", "",         expectRows); }
    public void Var576() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var577() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var578() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var579() { testPattern("%", schemasUnderscore,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var580() { testPattern("%", schemasUnderscore,"JDMDSM%NT",  null,       expectRows); }
    public void Var581() { testPattern("%", schemasUnderscore,"JDMDSM%INT", "",         expectRows); }
    public void Var582() { testPattern("%", schemasUnderscore,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var583() { testPattern("%", schemasUnderscore,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var584() { testPattern("%", schemasUnderscore,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var585() { testPattern("%", schemasUnderscore,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var586() { testPattern("%", schemasUnderscore,"_DMDSMALLINT", null,       expectRows); }
    public void Var587() { testPattern("%", schemasUnderscore,"_DMDSMALLINT", "",         expectRows); }
    public void Var588() { testPattern("%", schemasUnderscore,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var589() { testPattern("%", schemasUnderscore,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var590() { testPattern("%", schemasUnderscore,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var591() { testPattern("%", schemasUnderscore,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var592() { testPattern("%", schemasUnderscore,"%X23531%",   null,       expectZeroRows); }
    public void Var593() { testPattern("%", schemasUnderscore,"%X23531%",   "",         expectZeroRows); }
    public void Var594() { testPattern("%", schemasUnderscore,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var595() { testPattern("%", schemasUnderscore,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var596() { testPattern("%", schemasUnderscore,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var597() { testPattern("%", schemasUnderscore,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var598() { testPattern("%", "%X235353%",null, "",         expectZeroRows); }
    public void Var599() { testPattern("%", "%X235353%",null, "JDMDSIP1", expectZeroRows); }
    public void Var600() { testPattern("%", "%X235353%",null, "JDMD%P1",  expectZeroRows); }
    public void Var601() { testPattern("%", "%X235353%",null, "JDMD_IP1", expectZeroRows); }
    public void Var602() { testPattern("%", "%X235353%",null, "%X23531%", expectZeroRows); }
    public void Var603() { testPattern("%", "%X235353%","",   null,       expectZeroRows); }
    public void Var604() { testPattern("%", "%X235353%","",   "",         expectZeroRows); }
    public void Var605() { testPattern("%", "%X235353%","",   "JDMDSIP1", expectZeroRows); }
    public void Var606() { testPattern("%", "%X235353%","",   "JDMD%P1",  expectZeroRows); }
    public void Var607() { testPattern("%", "%X235353%","",   "JDMD_IP1", expectZeroRows); }
    public void Var608() { testPattern("%", "%X235353%","",   "%X23531%", expectZeroRows); }
    public void Var609() { testPattern("%", "%X235353%","JDMDSMALLINT", null,       expectZeroRows); }
    public void Var610() { testPattern("%", "%X235353%","JDMDSMALLINT", "",         expectZeroRows); }
    public void Var611() { testPattern("%", "%X235353%","JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var612() { testPattern("%", "%X235353%","JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var613() { testPattern("%", "%X235353%","JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var614() { testPattern("%", "%X235353%","JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var615() { testPattern("%", "%X235353%","JDMDSM%NT",  null,       expectZeroRows); }
    public void Var616() { testPattern("%", "%X235353%","JDMDSM%INT", "",         expectZeroRows); }
    public void Var617() { testPattern("%", "%X235353%","JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var618() { testPattern("%", "%X235353%","%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var619() { testPattern("%", "%X235353%","JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var620() { testPattern("%", "%X235353%","JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var621() { testPattern("%", "%X235353%","_DMDSMALLINT", null,       expectZeroRows); }
    public void Var622() { testPattern("%", "%X235353%","_DMDSMALLINT", "",         expectZeroRows); }
    public void Var623() { testPattern("%", "%X235353%","_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var624() { testPattern("%", "%X235353%","JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var625() { testPattern("%", "%X235353%","JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var626() { testPattern("%", "%X235353%","JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var627() { testPattern("%", "%X235353%","%X23531%",   null,       expectZeroRows); }
    public void Var628() { testPattern("%", "%X235353%","%X23531%",   "",         expectZeroRows); }
    public void Var629() { testPattern("%", "%X235353%","%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var630() { testPattern("%", "%X235353%","%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var631() { testPattern("%", "%X235353%","%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var632() { testPattern("%", "%X235353%","%X23531%",   "%X23531%", expectZeroRows); }



    //
    // LUW treats empty space for catalog the same as "%"
    //

    public void Var633() { testPattern("", null, null, "",         expectRows); }
    public void Var634() { testPattern("", null, null, "JDMDSIP1", expectRows); }
    public void Var635() { testPattern("", null, null, "JDMD%P1",  expectRows); }
    public void Var636() { testPattern("", null, null, "JDMD_IP1", expectRows); }
    public void Var637() { testPattern("", null, null, "%X23531%", expectZeroRows); }
    public void Var638() { testPattern("", null, "",   null,       expectZeroRows); }
    public void Var639() { testPattern("", null, "",   "",         expectZeroRows); }
    public void Var640() { testPattern("", null, "",   "JDMDSIP1", expectZeroRows); }
    public void Var641() { testPattern("", null, "",   "JDMD%P1",  expectZeroRows); }
    public void Var642() { testPattern("", null, "",   "JDMD_IP1", expectZeroRows); }
    public void Var643() { testPattern("", null, "",   "%X23531%", expectZeroRows); }
    public void Var644() { testPattern("", null, "JDMDSMALLINT", null,       expectRows); }
    public void Var645() { testPattern("", null, "JDMDSMALLINT", "",         expectRows); }
    public void Var646() { testPattern("", null, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var647() { testPattern("", null, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var648() { testPattern("", null, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var649() { testPattern("", null, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var650() { testPattern("", null, "JDMDSM%NT",  null,       expectRows); }
    public void Var651() { testPattern("", null, "JDMDSM%INT", "",         expectRows); }
    public void Var652() { testPattern("", null, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var653() { testPattern("", null, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var654() { testPattern("", null, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var655() { testPattern("", null, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var656() { testPattern("", null, "_DMDSMALLINT", null,       expectRows); }
    public void Var657() { testPattern("", null, "_DMDSMALLINT", "",         expectRows); }
    public void Var658() { testPattern("", null, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var659() { testPattern("", null, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var660() { testPattern("", null, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var661() { testPattern("", null, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var662() { testPattern("", null, "%X23531%",   null,       expectZeroRows); }
    public void Var663() { testPattern("", null, "%X23531%",   "",         expectZeroRows); }
    public void Var664() { testPattern("", null, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var665() { testPattern("", null, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var666() { testPattern("", null, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var667() { testPattern("", null, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var668() { testPattern("", "", null, "",         expectZeroRows); }
    public void Var669() { testPattern("", "", null, "JDMDSIP1", expectZeroRows); }
    public void Var670() { testPattern("", "", null, "JDMD%P1",  expectZeroRows); }
    public void Var671() { testPattern("", "", null, "JDMD_IP1", expectZeroRows); }
    public void Var672() { testPattern("", "", null, "%X23531%", expectZeroRows); }
    public void Var673() { testPattern("", "", "",   null,       expectZeroRows); }
    public void Var674() { testPattern("", "", "",   "",         expectZeroRows); }
    public void Var675() { testPattern("", "", "",   "JDMDSIP1", expectZeroRows); }
    public void Var676() { testPattern("", "", "",   "JDMD%P1",  expectZeroRows); }
    public void Var677() { testPattern("", "", "",   "JDMD_IP1", expectZeroRows); }
    public void Var678() { testPattern("", "", "",   "%X23531%", expectZeroRows); }
    public void Var679() { testPattern("", "", "JDMDSMALLINT", null,       expectZeroRows); }
    public void Var680() { testPattern("", "", "JDMDSMALLINT", "",         expectZeroRows); }
    public void Var681() { testPattern("", "", "JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var682() { testPattern("", "", "JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var683() { testPattern("", "", "JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var684() { testPattern("", "", "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var685() { testPattern("", "", "JDMDSM%NT",  null,       expectZeroRows); }
    public void Var686() { testPattern("", "", "JDMDSM%INT", "",         expectZeroRows); }
    public void Var687() { testPattern("", "", "JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var688() { testPattern("", "", "%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var689() { testPattern("", "", "JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var690() { testPattern("", "", "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var691() { testPattern("", "", "_DMDSMALLINT", null,       expectZeroRows); }
    public void Var692() { testPattern("", "", "_DMDSMALLINT", "",         expectZeroRows); }
    public void Var693() { testPattern("", "", "_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var694() { testPattern("", "", "JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var695() { testPattern("", "", "JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var696() { testPattern("", "", "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var697() { testPattern("", "", "%X23531%",   null,       expectZeroRows); }
    public void Var698() { testPattern("", "", "%X23531%",   "",         expectZeroRows); }
    public void Var699() { testPattern("", "", "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var700() { testPattern("", "", "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var701() { testPattern("", "", "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var702() { testPattern("", "", "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var703() { testPattern("", JDDMDTest.COLLECTION, null, "",         expectRows); }
    public void Var704() { testPattern("", JDDMDTest.COLLECTION, null, "JDMDSIP1", expectRows); }
    public void Var705() { testPattern("", JDDMDTest.COLLECTION, null, "JDMD%P1",  expectRows); }
    public void Var706() { testPattern("", JDDMDTest.COLLECTION, null, "JDMD_IP1", expectRows); }
    public void Var707() { testPattern("", JDDMDTest.COLLECTION, null, "%X23531%", expectZeroRows); }
    public void Var708() { testPattern("", JDDMDTest.COLLECTION, "",   null,       expectZeroRows); }
    public void Var709() { testPattern("", JDDMDTest.COLLECTION, "",   "",         expectZeroRows); }
    public void Var710() { testPattern("", JDDMDTest.COLLECTION, "",   "JDMDSIP1", expectZeroRows); }
    public void Var711() { testPattern("", JDDMDTest.COLLECTION, "",   "JDMD%P1",  expectZeroRows); }
    public void Var712() { testPattern("", JDDMDTest.COLLECTION, "",   "JDMD_IP1", expectZeroRows); }
    public void Var713() { testPattern("", JDDMDTest.COLLECTION, "",   "%X23531%", expectZeroRows); }
    public void Var714() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", null,       expectRows); }
    public void Var715() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", "",         expectRows); }
    public void Var716() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var717() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var718() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var719() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var720() { testPattern("", JDDMDTest.COLLECTION, "JDMDSM%NT",  null,       expectRows); }
    public void Var721() { testPattern("", JDDMDTest.COLLECTION, "JDMDSM%INT", "",         expectRows); }
    public void Var722() { testPattern("", JDDMDTest.COLLECTION, "JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var723() { testPattern("", JDDMDTest.COLLECTION, "%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var724() { testPattern("", JDDMDTest.COLLECTION, "JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var725() { testPattern("", JDDMDTest.COLLECTION, "JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var726() { testPattern("", JDDMDTest.COLLECTION, "_DMDSMALLINT", null,       expectRows); }
    public void Var727() { testPattern("", JDDMDTest.COLLECTION, "_DMDSMALLINT", "",         expectRows); }
    public void Var728() { testPattern("", JDDMDTest.COLLECTION, "_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var729() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var730() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var731() { testPattern("", JDDMDTest.COLLECTION, "JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var732() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   null,       expectZeroRows); }
    public void Var733() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   "",         expectZeroRows); }
    public void Var734() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var735() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var736() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var737() { testPattern("", JDDMDTest.COLLECTION, "%X23531%",   "%X23531%", expectZeroRows); }
    public void Var738() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,null, "",         expectRows); }
    public void Var739() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,null, "JDMDSIP1", expectRows); }
    public void Var740() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,null, "JDMD%P1",  expectRows); }
    public void Var741() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,null, "JDMD_IP1", expectRows); }
    public void Var742() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,null, "%X23531%", expectZeroRows); }
    public void Var743() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   null,       expectZeroRows); }
    public void Var744() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   "",         expectZeroRows); }
    public void Var745() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMDSIP1", expectZeroRows); }
    public void Var746() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD%P1",  expectZeroRows); }
    public void Var747() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   "JDMD_IP1", expectZeroRows); }
    public void Var748() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"",   "%X23531%", expectZeroRows); }
    public void Var749() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", null,       expectRows); }
    public void Var750() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "",         expectRows); }
    public void Var751() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var752() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var753() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var754() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var755() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%NT",  null,       expectRows); }
    public void Var756() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "",         expectRows); }
    public void Var757() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var758() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var759() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var760() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var761() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", null,       expectRows); }
    public void Var762() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "",         expectRows); }
    public void Var763() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var764() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var765() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var766() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var767() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   null,       expectZeroRows); }
    public void Var768() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "",         expectZeroRows); }
    public void Var769() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var770() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var771() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var772() { testPattern("", JDDMDTest.SCHEMAS_PERCENT,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var773() { testPattern("", schemasUnderscore,null, "",         expectRows); }
    public void Var774() { testPattern("", schemasUnderscore,null, "JDMDSIP1", expectRows); }
    public void Var775() { testPattern("", schemasUnderscore,null, "JDMD%P1",  expectRows); }
    public void Var776() { testPattern("", schemasUnderscore,null, "JDMD_IP1", expectRows); }
    public void Var777() { testPattern("", schemasUnderscore,null, "%X23531%", expectZeroRows); }
    public void Var778() { testPattern("", schemasUnderscore,"",   null,       expectZeroRows); }
    public void Var779() { testPattern("", schemasUnderscore,"",   "",         expectZeroRows); }
    public void Var780() { testPattern("", schemasUnderscore,"",   "JDMDSIP1", expectZeroRows); }
    public void Var781() { testPattern("", schemasUnderscore,"",   "JDMD%P1",  expectZeroRows); }
    public void Var782() { testPattern("", schemasUnderscore,"",   "JDMD_IP1", expectZeroRows); }
    public void Var783() { testPattern("", schemasUnderscore,"",   "%X23531%", expectZeroRows); }
    public void Var784() { testPattern("", schemasUnderscore,"JDMDSMALLINT", null,       expectRows); }
    public void Var785() { testPattern("", schemasUnderscore,"JDMDSMALLINT", "",         expectRows); }
    public void Var786() { testPattern("", schemasUnderscore,"JDMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var787() { testPattern("", schemasUnderscore,"JDMDSMALLINT", "JDMD%P1",  expectRows); }
    public void Var788() { testPattern("", schemasUnderscore,"JDMDSMALLINT", "JDMD_IP1", expectRows); }
    public void Var789() { testPattern("", schemasUnderscore,"JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var790() { testPattern("", schemasUnderscore,"JDMDSM%NT",  null,       expectRows); }
    public void Var791() { testPattern("", schemasUnderscore,"JDMDSM%INT", "",         expectRows); }
    public void Var792() { testPattern("", schemasUnderscore,"JDMDSM%INT", "JDMDSIP1", expectRows); }
    public void Var793() { testPattern("", schemasUnderscore,"%SMALLINT",  "JDMD%P1",  expectRows); }
    public void Var794() { testPattern("", schemasUnderscore,"JDMDSM%",    "JDMD_IP1", expectRows); }
    public void Var795() { testPattern("", schemasUnderscore,"JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var796() { testPattern("", schemasUnderscore,"_DMDSMALLINT", null,       expectRows); }
    public void Var797() { testPattern("", schemasUnderscore,"_DMDSMALLINT", "",         expectRows); }
    public void Var798() { testPattern("", schemasUnderscore,"_DMDSMALLINT", "JDMDSIP1", expectRows); }
    public void Var799() { testPattern("", schemasUnderscore,"JDMDSMAL_INT", "JDMD%P1",  expectRows); }
    public void Var800() { testPattern("", schemasUnderscore,"JDMDSMALLIN_", "JDMD_IP1", expectRows); }
    public void Var801() { testPattern("", schemasUnderscore,"JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var802() { testPattern("", schemasUnderscore,"%X23531%",   null,       expectZeroRows); }
    public void Var803() { testPattern("", schemasUnderscore,"%X23531%",   "",         expectZeroRows); }
    public void Var804() { testPattern("", schemasUnderscore,"%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var805() { testPattern("", schemasUnderscore,"%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var806() { testPattern("", schemasUnderscore,"%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var807() { testPattern("", schemasUnderscore,"%X23531%",   "%X23531%", expectZeroRows); }
    public void Var808() { testPattern("", "%X235353%",null, "",         expectZeroRows); }
    public void Var809() { testPattern("", "%X235353%",null, "JDMDSIP1", expectZeroRows); }
    public void Var810() { testPattern("", "%X235353%",null, "JDMD%P1",  expectZeroRows); }
    public void Var811() { testPattern("", "%X235353%",null, "JDMD_IP1", expectZeroRows); }
    public void Var812() { testPattern("", "%X235353%",null, "%X23531%", expectZeroRows); }
    public void Var813() { testPattern("", "%X235353%","",   null,       expectZeroRows); }
    public void Var814() { testPattern("", "%X235353%","",   "",         expectZeroRows); }
    public void Var815() { testPattern("", "%X235353%","",   "JDMDSIP1", expectZeroRows); }
    public void Var816() { testPattern("", "%X235353%","",   "JDMD%P1",  expectZeroRows); }
    public void Var817() { testPattern("", "%X235353%","",   "JDMD_IP1", expectZeroRows); }
    public void Var818() { testPattern("", "%X235353%","",   "%X23531%", expectZeroRows); }
    public void Var819() { testPattern("", "%X235353%","JDMDSMALLINT", null,       expectZeroRows); }
    public void Var820() { testPattern("", "%X235353%","JDMDSMALLINT", "",         expectZeroRows); }
    public void Var821() { testPattern("", "%X235353%","JDMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var822() { testPattern("", "%X235353%","JDMDSMALLINT", "JDMD%P1",  expectZeroRows); }
    public void Var823() { testPattern("", "%X235353%","JDMDSMALLINT", "JDMD_IP1", expectZeroRows); }
    public void Var824() { testPattern("", "%X235353%","JDMDSMALLINT", "%X23531%", expectZeroRows); }
    public void Var825() { testPattern("", "%X235353%","JDMDSM%NT",  null,       expectZeroRows); }
    public void Var826() { testPattern("", "%X235353%","JDMDSM%INT", "",         expectZeroRows); }
    public void Var827() { testPattern("", "%X235353%","JDMDSM%INT", "JDMDSIP1", expectZeroRows); }
    public void Var828() { testPattern("", "%X235353%","%SMALLINT",  "JDMD%P1",  expectZeroRows); }
    public void Var829() { testPattern("", "%X235353%","JDMDSM%",    "JDMD_IP1", expectZeroRows); }
    public void Var830() { testPattern("", "%X235353%","JDMD%INT",   "%X23531%", expectZeroRows); }
    public void Var831() { testPattern("", "%X235353%","_DMDSMALLINT", null,       expectZeroRows); }
    public void Var832() { testPattern("", "%X235353%","_DMDSMALLINT", "",         expectZeroRows); }
    public void Var833() { testPattern("", "%X235353%","_DMDSMALLINT", "JDMDSIP1", expectZeroRows); }
    public void Var834() { testPattern("", "%X235353%","JDMDSMAL_INT", "JDMD%P1",  expectZeroRows); }
    public void Var835() { testPattern("", "%X235353%","JDMDSMALLIN_", "JDMD_IP1", expectZeroRows); }
    public void Var836() { testPattern("", "%X235353%","JDMDSMA_LINT", "%X23531%", expectZeroRows); }
    public void Var837() { testPattern("", "%X235353%","%X23531%",   null,       expectZeroRows); }
    public void Var838() { testPattern("", "%X235353%","%X23531%",   "",         expectZeroRows); }
    public void Var839() { testPattern("", "%X235353%","%X23531%",   "JDMDSIP1", expectZeroRows); }
    public void Var840() { testPattern("", "%X235353%","%X23531%",   "JDMD%P1",  expectZeroRows); }
    public void Var841() { testPattern("", "%X235353%","%X23531%",   "JDMD_IP1", expectZeroRows); }
    public void Var842() { testPattern("", "%X235353%","%X23531%",   "%X23531%", expectZeroRows); }


    public void checkExpectedRows(
				  String[] createStatements,
				  String[] dropStatements,
				  String[][] expectedRows,
				  String catalog,
				  String schemaPattern,
				  String functionNamePattern,
				  String columnNamePattern,
				  String description) {

	String sql="";

	if (checkJdbc40()) {
	    StringBuffer sb = new StringBuffer();
	    try {

        Statement stmt = connection_.createStatement();
        for (int i = 0; i < dropStatements.length; i++) {
          try {
            sql = dropStatements[i];
            sb.append("setup.drop sql=" + sql + "\n");
            stmt.executeUpdate(sql);
          } catch (Exception e) {
          }
        }

		for (int i = 0; i < createStatements.length; i++) {
		    sql = createStatements[i];
		    sb.append("setup Sql="+sql+"\n"); 
		    stmt.executeUpdate(sql);
		}


		sql = "getFunctionColumns("+catalog+","+
		  schemaPattern+","+
                  functionNamePattern+","+
                  columnNamePattern+ ")";
		sb.append("Calling "+sql+"\n"); 
		ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd_,
                  "getFunctionColumns",
                  catalog,
                  schemaPattern,
                  functionNamePattern,
                  columnNamePattern);


		  boolean success = true;

	    String[] columnNames = expectedRows[0];

            int rows = 0;
            while (rs.next()) {
                ++rows;
		if (rows < expectedRows.length) { 
		    String[] expectedValues = expectedRows[rows];
		    for (int i = 0; i < expectedValues.length; i++) {
			String value = rs.getString(columnNames[i]);
			if (value == null) {
			    if (expectedValues[i] != null) {
				sb.append("For row "+rows+" column "+
					  columnNames[i]+" got '"+value+
					  "' sb '"+expectedValues[i]+"'\n");
				success = false;
			    }
			} else {
			    if (!value.equals(expectedValues[i])) {
				sb.append("For row "+rows+" column "+
					  columnNames[i]+" got '"+value+
					  "' sb '"+expectedValues[i]+"'\n");
				success = false;
			    }
			}
		    }
		} else {
		    sb.append("Extra row found row="+rows+" :: ");
		    for (int i = 0; i < expectedRows[0].length; i++) {
			String value = rs.getString(columnNames[i]);

			sb.append(value+","); 
		    }
		    sb.append("\n"); 
		    success = false; 
		} 

            }
            rs.close();

            for (int i = 0; i < dropStatements.length; i++) {
              sql = dropStatements[i];
	      sb.append("Drop sql="+sql); 
              stmt.executeUpdate(sql);
            }

            stmt.close();

	    if (rows != (expectedRows.length - 1)) {
                sb.append("rows = " + rows + " sb "+(expectedRows.length - 1));
		success = false;
	    }
	    assertCondition(success, "\n"+sb.toString()+"\n"+description  );

        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception sql="+sql+ "\n"+sb.toString()+"\n"+ description );
        }
        }
    }





/**
GetFunctionColumns() - Get a list of those created in this testcase and
verify all columns with system remarks (the default).
**/
      public void Var843()
      {

  	String description="Validate the ordering of a scalar UDF with no parameters";
  	String catalog = null;
  	String schemaPattern = JDDMDTest.COLLECTION;
  	String functionNamePattern = "JDDMDV843";
  	String columnNamePattern = null;
  	String[] createStatements = {
  	    " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV843() RETURNS INTEGER SPECIFIC "+JDDMDTest.COLLECTION+". JDDMDV843 LANGUAGE JAVA PARAMETER STYLE JAVA EXTERNAL NAME 'bogus.name'"
  	};
  	String[]  dropStatements = {
  	    "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV843"
  	}  ;
  	String[][] expectedRows = {
  	    {"FUNCTION_NAME", "SPECIFIC_NAME", "COLUMN_TYPE", "ORDINAL_POSITION" },
  	    {"JDDMDV843",     "JDDMDV843",   ""+functionColumnReturn, "0"}
  	};

  	checkExpectedRows(createStatements, dropStatements,
  			  expectedRows,
  			  catalog, schemaPattern, functionNamePattern, columnNamePattern,
  			  description);
      }

      public void Var844()
      {

        String description="Validate the ordering of a scalar UDF with 5 parameters";
        String catalog = null;
        String schemaPattern = JDDMDTest.COLLECTION;
        String functionNamePattern = "JDDMDV844";
        String columnNamePattern = null;
        String[] createStatements = {
            " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV844(Z INT,Y INT,X INT,W INT, V INT)"+
            " RETURNS INTEGER SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV844 LANGUAGE JAVA PARAMETER STYLE JAVA EXTERNAL NAME 'bogus.name'"
        };
        String[]  dropStatements = {
            "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV844"
        }  ;
        String[][] expectedRows = {
            {"FUNCTION_NAME", "SPECIFIC_NAME", "COLUMN_TYPE",         "COLUMN_NAME", "ORDINAL_POSITION" },
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnReturn, null,            "0"},
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnIn,     "Z",           "1"},
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnIn,     "Y",           "2"},
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnIn,     "X",           "3"},
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnIn,     "W",           "4"},
            {"JDDMDV844",     "JDDMDV844",   ""+functionColumnIn,     "V",           "5"},
        };

        checkExpectedRows(createStatements, dropStatements,
                          expectedRows,
                          catalog, schemaPattern, functionNamePattern, columnNamePattern,
                          description);
      }


     public void Var845()
     {
       String description="Validate the ordering of a table UDF with 5 parameters and 3 result set columns ";
       String catalog = null;
       String schemaPattern = JDDMDTest.COLLECTION;
       String functionNamePattern = "JDDMDV845";
       String columnNamePattern = null;
       String[] createStatements = {
           " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV845(Z INT,Y INT,X INT,W INT, V INT)"+
           " RETURNS TABLE(C INT, B INT, A INT) SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV845 LANGUAGE JAVA PARAMETER STYLE DB2GENERAL DISALLOW PARALLEL EXTERNAL NAME 'bogus.name'"
       };
       String[]  dropStatements = {
           "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV845"
       }  ;
       String[][] expectedRows = {
           {"FUNCTION_NAME", "SPECIFIC_NAME", "COLUMN_TYPE",         "COLUMN_NAME", "ORDINAL_POSITION" },
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnIn,     "Z",           "1"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnIn,     "Y",           "2"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnIn,     "X",           "3"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnIn,     "W",           "4"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnIn,     "V",           "5"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnResult, "C",           "1"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnResult, "B",           "2"},
           {"JDDMDV845",     "JDDMDV845",   ""+functionColumnResult, "A",           "3"},
       };

       checkExpectedRows(createStatements, dropStatements,
                         expectedRows,
                         catalog, schemaPattern, functionNamePattern, columnNamePattern,
                         description);

     }

     public void Var846()
     {
       String description="Validate the ordering of a functions with same name but different specific names ";
       String catalog = null;
       String schemaPattern = JDDMDTest.COLLECTION;
       String functionNamePattern = "JDDMDV846%";
       String columnNamePattern = null;
       String[] createStatements = {
           " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV846(Z INT,Y INT)"+
           " SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV8462"+
           " RETURNS INT LANGUAGE JAVA PARAMETER STYLE DB2GENERAL EXTERNAL NAME 'bogus.name'",

           " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV846(Z INT,Y INT,X INT,W INT)"+
           " SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV8464"+
           " RETURNS INT LANGUAGE JAVA PARAMETER STYLE DB2GENERAL EXTERNAL NAME 'bogus.name'",

           " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV846(Z INT)"+
           " SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV8461"+
           " RETURNS INT LANGUAGE JAVA PARAMETER STYLE DB2GENERAL EXTERNAL NAME 'bogus.name'",

           " CREATE FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV846(Z INT,Y INT,X INT)"+
           " SPECIFIC "+JDDMDTest.COLLECTION+".JDDMDV8463"+
           " RETURNS INT LANGUAGE JAVA PARAMETER STYLE DB2GENERAL EXTERNAL NAME 'bogus.name'",


       };
       String[]  dropStatements = {
           "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV8461",
           "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV8462",
           "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV8463",
           "DROP SPECIFIC FUNCTION "+JDDMDTest.COLLECTION+".JDDMDV8464",
       }  ;

       String[][] expectedRows = {
           {"FUNCTION_NAME", "SPECIFIC_NAME", "COLUMN_TYPE",         "COLUMN_NAME", "ORDINAL_POSITION" },
           {"JDDMDV846",     "JDDMDV8461",   ""+functionColumnReturn, null,            "0"},
           {"JDDMDV846",     "JDDMDV8461",   ""+functionColumnIn,     "Z",           "1"},

           {"JDDMDV846",     "JDDMDV8462",   ""+functionColumnReturn, null,            "0"},
           {"JDDMDV846",     "JDDMDV8462",   ""+functionColumnIn,     "Z",           "1"},
           {"JDDMDV846",     "JDDMDV8462",   ""+functionColumnIn,     "Y",           "2"},

           {"JDDMDV846",     "JDDMDV8463",   ""+functionColumnReturn, null,            "0"},
           {"JDDMDV846",     "JDDMDV8463",   ""+functionColumnIn,     "Z",           "1"},
           {"JDDMDV846",     "JDDMDV8463",   ""+functionColumnIn,     "Y",           "2"},
           {"JDDMDV846",     "JDDMDV8463",   ""+functionColumnIn,     "X",           "3"},

           {"JDDMDV846",     "JDDMDV8464",   ""+functionColumnReturn, null,            "0"},
           {"JDDMDV846",     "JDDMDV8464",   ""+functionColumnIn,     "Z",           "1"},
           {"JDDMDV846",     "JDDMDV8464",   ""+functionColumnIn,     "Y",           "2"},
           {"JDDMDV846",     "JDDMDV8464",   ""+functionColumnIn,     "X",           "3"},
           {"JDDMDV846",     "JDDMDV8464",   ""+functionColumnIn,     "W",           "4"},
       };

       checkExpectedRows(createStatements, dropStatements,
                         expectedRows,
                         catalog, schemaPattern, functionNamePattern, columnNamePattern,
                         description);

     }


     /*
      * Test a data type
      */


  class ExpectedParameterFormat {
    String functionName; /* 3 */
    String parametersAndBody;
    String columnName; /* 4 */
    String columnType; /* 5 */
    String dataType; /* 6 */
    String typeName; /* 7 */
    String precision; /* 8 */
    String length; /* 9 */
    String scale; /* 10 */
    String radix; /* 11 */
    String nullable; /* 12 */
    String remarks; /* 13 */
    String charOctetLength; /* 14 */
    String ordinalPosition; /* 15 */
    String isNullable; /* 16 */
    String specificName; /* 17 */


    public ExpectedParameterFormat(String functionName,
        String parametersAndBody, String columnName, String columnType,
        String dataType, String typeName, String precision, String length, String scale,
        String radix, String nullable, String remarks, String charOctetLength,
        String ordinalPosition, String isNullable, String specificName) {
      this.functionName = functionName;
      this.parametersAndBody = parametersAndBody;
      this.columnName = columnName;
      this.columnType = columnType;
      this.dataType = dataType;
      this.typeName = typeName;
      this.precision = precision;
      this.length = length;
      this.scale = scale;
      this.radix = radix;
      this.nullable = nullable;
      this.remarks = remarks;
      this.charOctetLength = charOctetLength;
      this.ordinalPosition = ordinalPosition;
      this.isNullable = isNullable;
      this.specificName = specificName;
    }


    public String getColumnValue(int column) {
      switch (column) {
      case 3:
        return functionName;

      case 4:
        return columnName;

      case 5:
        return columnType;

      case 6:
        return dataType;

      case 7:
        return typeName;

      case 8:
        return precision;

      case 9:
        return length;

      case 10:
        return scale;

      case 11:
        return radix;

      case 12:
        return nullable;

      case 13:
        return remarks;

      case 14:
        return charOctetLength;

      case 15:
        return ordinalPosition;

      case 16:
        return isNullable;

      case 17:
        return specificName;

      default:
        return "UNKNOWN";
      }
    }

    public String getColumnName(int column) {
      switch (column) {
      case 3:
        return "procedureName";

      case 4:
        return "columnName";

      case 5:
        return "columnType";

      case 6:
        return "dataType";

      case 7:
        return "typeName";

      case 8:
        return "precision";

      case 9:
        return "length";

      case 10:
        return "scale";

      case 11:
        return "radix";

      case 12:
        return "nullable";

      case 13:
        return "remarks";

      case 14:
        return "charOctetLength";

      case 15:
        return "ordinalPosition";

      case 16:
        return "isNullable";

      case 17:
        return "specificName";

      default:
        return "UNKNOWN";
      }

    }
  }

  public boolean checkString(String name, String value, String expected) {
    if (value == null) {
        if (expected == null) {
            return true;
        } else {
            errorMessage+= "\n"+parameterName+":"+name+" = "+value+" sb "+expected;
            return false;
        }
    } else {
        if (value.equals(expected)) {
            return true;
        } else {
            errorMessage+= "\n"+ parameterName+":"+name+" = "+value+" sb "+expected;
            return false;
        }
    }
}


    /**
     * Test a datatype in the catalog
     */

    public void testDataType(ExpectedParameterFormat ex) {
        int column = 0;
        errorMessage="";
        parameterName="";
        try {
          int maxColumn = 17;
          boolean success = true;
          boolean rowFound = false;
          Statement s = connection_.createStatement();
          try {
            s.executeUpdate("DROP FUNCTION " + JDDMDTest.COLLECTION+ "."+ex.functionName);
          } catch (Exception e) {
          }
          s.executeUpdate("CREATE FUNCTION " + JDDMDTest.COLLECTION+"."+ex.functionName+ex.parametersAndBody);

          ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_O(dmd_, "getFunctionColumns",
              new Class[]{String.class, String.class,         String.class,          String.class} ,
                          null,         JDDMDTest.COLLECTION, ex.functionName+"%" , "%");
          boolean more = rs.next();
          while (more) {
            String columnName = rs.getString(4);
            if (ex.columnName.equals(columnName)) {
              rowFound = true;
              for (column = 3; column <= maxColumn; column++) {
                success=checkString(ex.getColumnName(column), rs.getString(column), ex.getColumnValue(column)) && success ;
              }
            } else {
              errorMessage+="\nFound unexpected row ";
              for (column = 3; column <= maxColumn; column++) {
                errorMessage+=rs.getString(column)+",";
              }
            }
            more = rs.next();
          }

          s.executeUpdate("DROP function "   + JDDMDTest.COLLECTION+ "."+ex.functionName);

          assertCondition(rowFound && success, "rowFound="+rowFound+" Test for "+ex.typeName+" added by native driver 1/26/09 "+errorMessage);

        } catch (Exception e) {
          failed (e, "Unexpected Exception Added by native driver 4/27/06 column="+column);

        }

      }
    


        /**
         * Test the ROWID data type as a parameter
        **/

       public void Var847()
        {
            if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)   {
		notApplicable("ptf test - non toolbox");
		return;
	     }
            if (checkJdbc40()) {

          ExpectedParameterFormat ex = new ExpectedParameterFormat(
              /* name */     "JDDMDGFCROWID",
                             "(C1 ROWID) RETURNS INT LANGUAGE SQL P847: BEGIN DECLARE DUMMY ROWID; SET DUMMY = C1; RETURN 7; END P847",
              /*columnName*/ "C1",
              /*columnType*/ "1", /* function column in */
              /*dataType*/   "-8",
              /*typeName*/   "ROWID",
              /*precision*/  "40",
              /*length */    "40",
              /*scale */     null,
              /*radix */     null,
              /*nullable*/   "1",
              /*remarks*/    null,
              /*octetLength*/ "40",
              /*ordinalPos*/  "1",
              /*isNullable*/  "YES",
              /*specificName*/ "JDDMDGFCROWID"
          );

          // Now fix older releases with the wrong answers
          // Now fix older releases with the wrong answers
          
              ex.length = "42";
          

	  testDataType(ex);
            }
        }

  public void Var848() {
	  checkRSMD(false);
  }
  public void Var849() {
	  checkRSMD(true);
  }

    public void checkRSMD(boolean extendedMetadata)
    {

	Connection connection = connection_;
	DatabaseMetaData dmd = dmd_;
	int TESTSIZE=16;
	StringBuffer prime = new StringBuffer();
	int j=0;
	int col=0;
	String added=" -- Reworked 03/23/2012";
	boolean passed=true;
	message.setLength(0);

	/* PRIMED using 546CN */

	String [][] methodTests = {

	    {"isAutoIncrement","1","false"},
	    {"isCaseSensitive","1","true"},
	    {"isSearchable","1","true"},
	    {"isCurrency","1","false"},
	    {"isNullable","1","0"},
	    {"isSigned","1","false"},
	    {"getColumnDisplaySize","1","128"},
	    {"getColumnLabel","1","FUNCTION_CAT"},
	    {"getColumnName","1","FUNCTION_CAT"},
	    {"getPrecision","1","128"},
	    {"getScale","1","0"},
	    {"getCatalogName","1","LOCALHOST"},
	    {"getColumnType","1","12"},
	    {"getColumnTypeName","1","VARCHAR"},
	    {"isReadOnly","1","true"},
	    {"isWritable","1","false"},
	    {"isDefinitelyWritable","1","false"},
	    {"getColumnClassName","1","java.lang.String"},
	    {"isAutoIncrement","2","false"},
	    {"isCaseSensitive","2","true"},
	    {"isSearchable","2","true"},
	    {"isCurrency","2","false"},
	    {"isNullable","2","0"},
	    {"isSigned","2","false"},
	    {"getColumnDisplaySize","2","128"},
	    {"getColumnLabel","2","FUNCTION_SCHEM"},
	    {"getColumnName","2","FUNCTION_SCHEM"},
	    {"getPrecision","2","128"},
	    {"getScale","2","0"},
	    {"getCatalogName","2","LOCALHOST"},
	    {"getColumnType","2","12"},
	    {"getColumnTypeName","2","VARCHAR"},
	    {"isReadOnly","2","true"},
	    {"isWritable","2","false"},
	    {"isDefinitelyWritable","2","false"},
	    {"getColumnClassName","2","java.lang.String"},
	    {"isAutoIncrement","3","false"},
	    {"isCaseSensitive","3","true"},
	    {"isSearchable","3","true"},
	    {"isCurrency","3","false"},
	    {"isNullable","3","0"},
	    {"isSigned","3","false"},
	    {"getColumnDisplaySize","3","128"},
	    {"getColumnLabel","3","FUNCTION_NAME"},
	    {"getColumnName","3","FUNCTION_NAME"},
	    {"getPrecision","3","128"},
	    {"getScale","3","0"},
	    {"getCatalogName","3","LOCALHOST"},
	    {"getColumnType","3","12"},
	    {"getColumnTypeName","3","VARCHAR"},
	    {"isReadOnly","3","true"},
	    {"isWritable","3","false"},
	    {"isDefinitelyWritable","3","false"},
	    {"getColumnClassName","3","java.lang.String"},
	    {"isAutoIncrement","4","false"},
	    {"isCaseSensitive","4","true"},
	    {"isSearchable","4","true"},
	    {"isCurrency","4","false"},
	    {"isNullable","4","1"},
	    {"isSigned","4","false"},
	    {"getColumnDisplaySize","4","128"},
	    {"getColumnLabel","4","COLUMN_NAME"},
	    {"getColumnName","4","COLUMN_NAME"},
	    {"getPrecision","4","128"},
	    {"getScale","4","0"},
	    {"getCatalogName","4","LOCALHOST"},
	    {"getColumnType","4","12"},
	    {"getColumnTypeName","4","VARCHAR"},
	    {"isReadOnly","4","true"},
	    {"isWritable","4","false"},
	    {"isDefinitelyWritable","4","false"},
	    {"getColumnClassName","4","java.lang.String"},
	    {"isAutoIncrement","5","false"},
	    {"isCaseSensitive","5","false"},
	    {"isSearchable","5","true"},
	    {"isCurrency","5","false"},
	    {"isNullable","5","0"},
	    {"isSigned","5","true"},
	    {"getColumnDisplaySize","5","6"},
	    {"getColumnLabel","5","COLUMN_TYPE"},
	    {"getColumnName","5","COLUMN_TYPE"},
	    {"getPrecision","5","5"},
	    {"getScale","5","0"},
	    {"getCatalogName","5","LOCALHOST"},
	    {"getColumnType","5","5"},
	    {"getColumnTypeName","5","SMALLINT"},
	    {"isReadOnly","5","true"},
	    {"isWritable","5","false"},
	    {"isDefinitelyWritable","5","false"},
	    {"getColumnClassName","5","java.lang.Integer"},
	    {"isAutoIncrement","6","false"},
	    {"isCaseSensitive","6","false"},
	    {"isSearchable","6","true"},
	    {"isCurrency","6","false"},
	    {"isNullable","6","0"},
	    {"isSigned","6","true"},
	    {"getColumnDisplaySize","6","11"},
	    {"getColumnLabel","6","DATA_TYPE"},
	    {"getColumnName","6","DATA_TYPE"},
	    {"getPrecision","6","10"},
	    {"getScale","6","0"},
	    {"getCatalogName","6","LOCALHOST"},
	    {"getColumnType","6","4"},
	    {"getColumnTypeName","6","INTEGER"},
	    {"isReadOnly","6","true"},
	    {"isWritable","6","false"},
	    {"isDefinitelyWritable","6","false"},
	    {"getColumnClassName","6","java.lang.Integer"},
	    {"isAutoIncrement","7","false"},
	    {"isCaseSensitive","7","true"},
	    {"isSearchable","7","true"},
	    {"isCurrency","7","false"},
	    {"isNullable","7","0"},
	    {"isSigned","7","false"},
	    {"getColumnDisplaySize","7","261"},
	    {"getColumnLabel","7","TYPE_NAME"},
	    {"getColumnName","7","TYPE_NAME"},
	    {"getPrecision","7","261"},
	    {"getScale","7","0"},
	    {"getCatalogName","7","LOCALHOST"},
	    {"getColumnType","7","12"},
	    {"getColumnTypeName","7","VARCHAR"},
	    {"isReadOnly","7","true"},
	    {"isWritable","7","false"},
	    {"isDefinitelyWritable","7","false"},
	    {"getColumnClassName","7","java.lang.String"},
	    {"isAutoIncrement","8","false"},
	    {"isCaseSensitive","8","false"},
	    {"isSearchable","8","true"},
	    {"isCurrency","8","false"},
	    {"isNullable","8","0"},
	    {"isSigned","8","true"},
	    {"getColumnDisplaySize","8","11"},
	    {"getColumnLabel","8","PRECISION"},
	    {"getColumnName","8","PRECISION"},
	    {"getPrecision","8","10"},
	    {"getScale","8","0"},
	    {"getCatalogName","8","LOCALHOST"},
	    {"getColumnType","8","4"},
	    {"getColumnTypeName","8","INTEGER"},
	    {"isReadOnly","8","true"},
	    {"isWritable","8","false"},
	    {"isDefinitelyWritable","8","false"},
	    {"getColumnClassName","8","java.lang.Integer"},
	    {"isAutoIncrement","9","false"},
	    {"isCaseSensitive","9","false"},
	    {"isSearchable","9","true"},
	    {"isCurrency","9","false"},
	    {"isNullable","9","0"},
	    {"isSigned","9","true"},
	    {"getColumnDisplaySize","9","11"},
	    {"getColumnLabel","9","LENGTH"},
	    {"getColumnName","9","LENGTH"},
	    {"getPrecision","9","10"},
	    {"getScale","9","0"},
	    {"getCatalogName","9","LOCALHOST"},
	    {"getColumnType","9","4"},
	    {"getColumnTypeName","9","INTEGER"},
	    {"isReadOnly","9","true"},
	    {"isWritable","9","false"},
	    {"isDefinitelyWritable","9","false"},
	    {"getColumnClassName","9","java.lang.Integer"},
	    {"isAutoIncrement","10","false"},
	    {"isCaseSensitive","10","false"},
	    {"isSearchable","10","true"},
	    {"isCurrency","10","false"},
	    {"isNullable","10","1"},
	    {"isSigned","10","true"},
	    {"getColumnDisplaySize","10","6"},
	    {"getColumnLabel","10","SCALE"},
	    {"getColumnName","10","SCALE"},
	    {"getPrecision","10","5"},
	    {"getScale","10","0"},
	    {"getCatalogName","10","LOCALHOST"},
	    {"getColumnType","10","5"},
	    {"getColumnTypeName","10","SMALLINT"},
	    {"isReadOnly","10","true"},
	    {"isWritable","10","false"},
	    {"isDefinitelyWritable","10","false"},
	    {"getColumnClassName","10","java.lang.Integer"},
	    {"isAutoIncrement","11","false"},
	    {"isCaseSensitive","11","false"},
	    {"isSearchable","11","true"},
	    {"isCurrency","11","false"},
	    {"isNullable","11","1"},
	    {"isSigned","11","true"},
	    {"getColumnDisplaySize","11","6"},
	    {"getColumnLabel","11","RADIX"},
	    {"getColumnName","11","RADIX"},
	    {"getPrecision","11","5"},
	    {"getScale","11","0"},
	    {"getCatalogName","11","LOCALHOST"},
	    {"getColumnType","11","5"},
	    {"getColumnTypeName","11","SMALLINT"},
	    {"isReadOnly","11","true"},
	    {"isWritable","11","false"},
	    {"isDefinitelyWritable","11","false"},
	    {"getColumnClassName","11","java.lang.Integer"},
	    {"isAutoIncrement","12","false"},
	    {"isCaseSensitive","12","false"},
	    {"isSearchable","12","true"},
	    {"isCurrency","12","false"},
	    {"isNullable","12","0"},
	    {"isSigned","12","true"},
	    {"getColumnDisplaySize","12","6"},
	    {"getColumnLabel","12","NULLABLE"},
	    {"getColumnName","12","NULLABLE"},
	    {"getPrecision","12","5"},
	    {"getScale","12","0"},
	    {"getCatalogName","12","LOCALHOST"},
	    {"getColumnType","12","5"},
	    {"getColumnTypeName","12","SMALLINT"},
	    {"isReadOnly","12","true"},
	    {"isWritable","12","false"},
	    {"isDefinitelyWritable","12","false"},
	    {"getColumnClassName","12","java.lang.Integer"},
	    {"isAutoIncrement","13","false"},
	    {"isCaseSensitive","13","true"},
	    {"isSearchable","13","true"},
	    {"isCurrency","13","false"},
	    {"isNullable","13","1"},
	    {"isSigned","13","false"},
	    {"getColumnDisplaySize","13","2000"},
	    {"getColumnLabel","13","REMARKS"},
	    {"getColumnName","13","REMARKS"},
	    {"getPrecision","13","2000"},
	    {"getScale","13","0"},
	    {"getCatalogName","13","LOCALHOST"},
	    {"getColumnType","13","12"},
	    {"getColumnTypeName","13","VARCHAR"},
	    {"isReadOnly","13","true"},
	    {"isWritable","13","false"},
	    {"isDefinitelyWritable","13","false"},
	    {"getColumnClassName","13","java.lang.String"},
	    {"isAutoIncrement","14","false"},
	    {"isCaseSensitive","14","false"},
	    {"isSearchable","14","true"},
	    {"isCurrency","14","false"},
	    {"isNullable","14","1"},
	    {"isSigned","14","true"},
	    {"getColumnDisplaySize","14","11"},
	    {"getColumnLabel","14","CHAR_OCTET_LENGTH"},
	    {"getColumnName","14","CHAR_OCTET_LENGTH"},
	    {"getPrecision","14","10"},
	    {"getScale","14","0"},
	    {"getCatalogName","14","LOCALHOST"},
	    {"getColumnType","14","4"},
	    {"getColumnTypeName","14","INTEGER"},
	    {"isReadOnly","14","true"},
	    {"isWritable","14","false"},
	    {"isDefinitelyWritable","14","false"},
	    {"getColumnClassName","14","java.lang.Integer"},
	    {"isAutoIncrement","15","false"},
	    {"isCaseSensitive","15","false"},
	    {"isSearchable","15","true"},
	    {"isCurrency","15","false"},
	    {"isNullable","15","0"},
	    {"isSigned","15","true"},
	    {"getColumnDisplaySize","15","11"},
	    {"getColumnLabel","15","ORDINAL_POSITION"},
	    {"getColumnName","15","ORDINAL_POSITION"},
	    {"getPrecision","15","10"},
	    {"getScale","15","0"},
	    {"getCatalogName","15","LOCALHOST"},
	    {"getColumnType","15","4"},
	    {"getColumnTypeName","15","INTEGER"},
	    {"isReadOnly","15","true"},
	    {"isWritable","15","false"},
	    {"isDefinitelyWritable","15","false"},
	    {"getColumnClassName","15","java.lang.Integer"},
	    {"isAutoIncrement","16","false"},
	    {"isCaseSensitive","16","true"},
	    {"isSearchable","16","true"},
	    {"isCurrency","16","false"},
	    {"isNullable","16","0"},
	    {"isSigned","16","false"},
	    {"getColumnDisplaySize","16","3"},
	    {"getColumnLabel","16","IS_NULLABLE"},
	    {"getColumnName","16","IS_NULLABLE"},
	    {"getPrecision","16","3"},
	    {"getScale","16","0"},
	    {"getCatalogName","16","LOCALHOST"},
	    {"getColumnType","16","12"},
	    {"getColumnTypeName","16","VARCHAR"},
	    {"isReadOnly","16","true"},
	    {"isWritable","16","false"},
	    {"isDefinitelyWritable","16","false"},
	    {"getColumnClassName","16","java.lang.String"},
	    {"isAutoIncrement","17","false"},
	    {"isCaseSensitive","17","true"},
	    {"isSearchable","17","true"},
	    {"isCurrency","17","false"},
	    {"isNullable","17","0"},
	    {"isSigned","17","false"},
	    {"getColumnDisplaySize","17","128"},
	    {"getColumnLabel","17","SPECIFIC_NAME"},
	    {"getColumnName","17","SPECIFIC_NAME"},
	    {"getPrecision","17","128"},
	    {"getScale","17","0"},
	    {"getCatalogName","17","LOCALHOST"},
	    {"getColumnType","17","12"},
	    {"getColumnTypeName","17","VARCHAR"},
	    {"isReadOnly","17","true"},
	    {"isWritable","17","false"},
	    {"isDefinitelyWritable","17","false"},
	    {"getColumnClassName","17","java.lang.String"},
	};







	String [][] fixup = {};

	String[][] fixup714NS = {
	    {"getColumnTypeName","13","NVARCHAR"},
	};

	String[][] fixup716NS = {
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"},
	};

	String[][] fixup715TS = {
	    {"getColumnTypeName","13","NVARCHAR"},
	};

	String[][] fixupExtended716N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"isSearchable","13","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},




	};

	String[][] fixupExtended736N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"isSearchable","13","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnType","13","-9"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},

/* April 2019 label fixes */
      {"getColumnLabel","1","Function            Cat"},
      {"getColumnLabel","2","Function            Schem"},
      {"getColumnLabel","3","Function            Name"},
      {"getColumnLabel","4","Column              Name"},
      {"getColumnLabel","5","Column              Type"},
      {"getColumnLabel","12","Nullable"},
      {"getColumnLabel","13","Remarks"},
      {"getColumnLabel","14","Char                Octet               Length"},
      {"getColumnLabel","15","Ordinal             Position"},
      {"getColumnLabel","16","Is                  Nullable"},
      {"getColumnLabel","17","Specific            Name"},



	};



	String[][] fixupExtended714N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"isSearchable","13","false"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},
	};
	String [][] fixupExtended546N = {
	    {"isSearchable","1","false"},
	    {"isSearchable","2","false"},
	    {"isSearchable","3","false"},
	    {"isSearchable","4","false"},
	    {"isSearchable","5","false"},
	    {"isSearchable","6","false"},
	    {"isSearchable","7","false"},
	    {"isSearchable","8","false"},
	    {"isSearchable","9","false"},
	    {"isSearchable","10","false"},
	    {"isSearchable","11","false"},
	    {"isSearchable","12","false"},
	    {"isSearchable","13","false"},
	    {"isSearchable","14","false"},
	    {"isSearchable","15","false"},
	    {"isSearchable","16","false"},
	    {"isSearchable","17","false"},

	};

	
	String[][] fixupExtended544T = {

	};

	String[][] fixupExtended614T = {

	};


	String [][]  fixupExtended715T = {

	    {"getColumnTypeName","13","NVARCHAR"},
	};


	String [][] fixup716T = {
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	};


	String [][] fixup736T = {
	    {"getColumnType","13","-9"},
	    {"getColumnTypeName","13","NVARCHAR"},
	    {"getColumnLabel","1","Function Cat"},
	    {"getColumnLabel","2","Function Schem"},
	    {"getColumnLabel","3","Function Name"},
	    {"getColumnLabel","4","Column Name"},
	    {"getColumnLabel","5","Column Type"},
	    {"getColumnLabel","12","Nullable"},
	    {"getColumnLabel","13","Remarks"},
	    {"getColumnLabel","14","Char Octet Length"},
	    {"getColumnLabel","15","Ordinal Position"},
	    {"getColumnLabel","16","Is Nullable"},
	    {"getColumnLabel","17","Specific Name"},
	};



	String[][] fixup726LS = {
	    {"getColumnTypeName","7","VARCHAR"},
	    {"getColumnTypeName","13","VARGRAPHIC"},
	};

	String[][] noFixup = {
	};


	Object[][] fixupArrayExtended = {
	    {"543T", fixupExtended544T},
	    {"544T", fixupExtended544T},
	    {"545T", fixupExtended544T},
	    {"546T", fixupExtended544T},

	    {"614T", fixupExtended614T},
	    {"615T", fixupExtended614T},
	    {"616T", noFixup, "No fixup needed.  Need to use this to prevent 54 version from being used"}, 

	    {"714T", fixupExtended715T},
	    {"715T", fixupExtended715T},
	    {"716T", fixup716T, "09/06/2012 -- primed" },
	    {"717T", fixup716T, "09/06/2012 -- guess from 716T" },
	    {"718T", fixup716T, "09/06/2012 -- guess from 716T" },
	    {"719T", fixup716T, "09/06/2012 -- guess from 716T" },


	    {"736T", fixup736T, "5/29/2019 updated for label change"}, 
	    {"737T", fixup736T, "5/29/2019 updated for label change"}, 
	    {"738T", fixup736T, "5/29/2019 updated for label change"}, 
	    {"739T", fixup736T, "5/29/2019 updated for label change"}, 


	    {"544N", fixupExtended546N},
	    {"545N", fixupExtended546N},
	    {"546N", fixupExtended546N},
	    {"614N", fixupExtended546N},
	    {"615N", fixupExtended546N},
	    {"616N", fixupExtended546N},
	    {"714N", fixupExtended714N, "08/09/2012 -- actual primed"},
	    {"715N", fixupExtended714N, "08/09/2012 -- guess from 717N"},
	    {"716N", fixupExtended716N, "08/09/2012 -- guess from 717N"},
	    {"717N", fixupExtended716N, "08/09/2012 -- actual primed" },
	    {"718N", fixupExtended716N, "08/09/2012 -- actual primed" },
	    {"719N", fixupExtended716N, "08/09/2012 -- actual primed" },
	    {"726N", fixupExtended716N, "09/17/2013 -- guess from 714N" },
	    {"727N", fixupExtended716N, "09/17/2013 -- guess from 714N"},
	    {"736N", fixupExtended736N, "Update 4/23/2018"}, 
	    {"737N", fixupExtended736N, "Update 4/23/2018"}, 
	    {"738N", fixupExtended736N, "Update 4/23/2018"}, 

	    { "546L", fixup726LS},
	};


	Object[][] fixupArray = {

	    { "714TS", fixup715TS},
	    { "715TS", fixup715TS},
	    { "716TS", fixup716T, "09/06/2012 -- primed" },
	    { "717TS", fixup716T, "09/06/2012 -- guess from 716T" },
	    { "718TS", fixup716T, "09/06/2012 -- guess from 716T" },
	    { "719TS", fixup716T, "09/06/2012 -- guess from 716T" },


	    { "714NS", fixup714NS},
	    { "715NS", fixup714NS},
	    { "716NS", fixup716NS, "prime 08/23/2012"},
	    { "717NS", fixup716NS, "guess 08/23/2012"},
	    { "718NS", fixup716NS, "guess 08/23/2012"},
	    { "719NS", fixup716NS, "guess 08/23/2012"},


	    { "546LS", fixup726LS},
	};



	if (checkJdbc30()) {
	    try {

		if (extendedMetadata) {
		    String url = baseURL_
		      + ";extended metadata=true";
		    if (getDriver() == JDTestDriver.DRIVER_JCC) {
			url = baseURL_;
		    }
		    connection = testDriver_.getConnection (url,
							     userId_, encryptedPassword_);
		    dmd= connection.getMetaData ();

		    fixup = getFixup(fixupArrayExtended, null, "fixupArrayExtended", message);



		} else {
		    String extra="X";
		    if (isSysibmMetadata()) {
		      extra = "S";
		    }

		    fixup = getFixup(fixupArray, extra, "fixupArray", message);


		}

		if (fixup != null) {

		    for (j = 0; j < fixup.length; j++) {
			boolean found = false;
			for (int k = 0; !found &&  k < methodTests.length; k++) {
			    if (fixup[j][0].equals(methodTests[k][0]) &&
				fixup[j][1].equals(methodTests[k][1])) {
				methodTests[k] = fixup[j];
				found = true;
			    }
			}
		    }
		}



		String catalog1 = connection.getCatalog();
		if (catalog1 == null) {
		    catalog1 = system_.toUpperCase();
		}

		ResultSet[] rsA =  new ResultSet[TESTSIZE];
		ResultSetMetaData[] rsmdA = new ResultSetMetaData[TESTSIZE];
		for (j = 0; j < TESTSIZE; j++) {
		    rsA[j] =  (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd,
									    "getFunctionColumns",
									    null,
									    JDDMDTest.COLLECTION,
									    "JDMDSMALLINT",
									    null);

		    rsmdA[j] = rsA[j].getMetaData ();

		    ResultSetMetaData rsmd = rsmdA[j];
		    passed = verifyRsmd(rsmd, methodTests, catalog1, j, message, prime) && passed;
		    
		} /* for j */
		assertCondition(passed, message.toString()+added+"\nPRIME=\n"+prime.toString());

	    } catch (Exception e) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    } catch (Error e ) {
		failed (e, "Unexpected Error on loop "+j+" col = "+col+" "+added);
	    }
	}
    }

  // Test readonly connection
  public void Var850() {

    if (checkJdbc40()) {

      message.setLength(0);
      try {

        Connection c = testDriver_.getConnection(
            baseURL_ + ";access=read only", userId_, encryptedPassword_);
        DatabaseMetaData dmd = c.getMetaData();

        ResultSet rs = (ResultSet) JDReflectionUtil.callMethod_OSSSS(dmd,
            "getFunctionColumns", null, JDDMDTest.COLLECTION, "JDMDSMALLINT",
            null);

        boolean success = true;

        int rows = 0;
        while (rs.next()) {
          ++rows;

          success = JDDMDTest.check(rs.getString("FUNCTION_CAT"),
              connectionCatalog_, "FUNCTION_CAT", "NULL", message, success);
          success = JDDMDTest.check(rs.getString("FUNCTION_SCHEM"),
              JDDMDTest.COLLECTION, "FUNCTION_SCHEMA", JDDMDTest.COLLECTION,
              message, success);
          success = JDDMDTest.check(rs.getString("FUNCTION_NAME"),
              "JDMDSMALLINT", "FUNCTION_NAME", "NULL", message, success);
          if (rows == 1) {
            success = JDDMDTest.check(rs.getString("COLUMN_NAME"), null,
                "COLUMN_NAME", "For row " + rows, message, success);
          } else {
            success = JDDMDTest.check(rs.getString("COLUMN_NAME"), "JDMDSIP1",
                "COLUMN_NAME", "For row " + rows, message, success);
          }
          // success = JDDMDTest.check(rs.getString("COLUMN_TYPE"), "1",
          // "COLUMN_TYPE", "NULL", message, success);
          success = JDDMDTest.check(rs.getString("DATA_TYPE"), "5",
              "DATA_TYPE", "NULL", message, success);

          success = JDDMDTest.check(rs.getString("TYPE_NAME"), "SMALLINT",
              "TYPE_NAME", "NULL", message, success);
          success = JDDMDTest.check(rs.getString("PRECISION"), "5",
              "PRECISION", "NULL", message, success);
          success = JDDMDTest.check(rs.getString("LENGTH"), "2", "LENGTH",
              "NULL", message, success);

          success = JDDMDTest.check(rs.getString("SCALE"), "0", "SCALE",
              "NULL", message, success);
          success = JDDMDTest.check(rs.getString("RADIX"), "10", "RADIX",
              "NULL", message, success);
          success = JDDMDTest.check(rs.getString("NULLABLE"), "1", "NULLABLE",
              "NULL", message, success);

          success = JDDMDTest.check(rs.getString("REMARKS"), null, "REMARKS",
              "NULL", message, success);
          success = JDDMDTest.check(rs.getString("CHAR_OCTET_LENGTH"), null,
              "CHAR_OCTET_LENGTH", "NULL", message, success);
          // success = JDDMDTest.check(rs.getString("ORDINAL_POSITION"), "1",
          // "ORDINAL_POSITION", "NULL", message, success);

          success = JDDMDTest.check(rs.getString("IS_NULLABLE"), "YES",
              "IS_NULLABLE", "NULL", message, success);
          success = JDDMDTest.check(rs.getString("SPECIFIC_NAME"),
              "JDMDSMALLINT", "SPECIFIC_NAME", "NULL", message, success);

        }

        rs.close();
        if (rows != 2)
          message.append("rows = " + rows + " sb 2 ");
        assertCondition((rows == 2) && success, "\n" + message.toString());

      } catch (Exception e) {
        failed(e, "01/25/07 New jdbc 40 TC.  Unexpected Exception");
      }
    }
  }

  
  /**
   * Test the BIGINT data type as a parameter
  **/

 public void Var851()
  {

    ExpectedParameterFormat ex = new ExpectedParameterFormat(
        /* name */     "JDDMDGFCBIGINT",
                       "(C1 BIGINT) RETURNS INT LANGUAGE SQL P847: BEGIN DECLARE DUMMY BIGINT; SET DUMMY = C1; RETURN 7; END P847",
        /*columnName*/ "C1",
        /*columnType*/ "1", /* function column in */
        /*dataType*/   "-5",
        /*typeName*/   "BIGINT",
        /*precision*/  "19",
        /*length */    "8",
        /*scale */     "0",
        /*radix */     "10",
        /*nullable*/   "1",
        /*remarks*/    null,
        /*octetLength*/ null,
        /*ordinalPos*/  "1",
        /*isNullable*/  "YES",
        /*specificName*/ "JDDMDGFCBIGINT"
    );

 
testDataType(ex);

  }

 
 
 /**
  * Test the BOOLEAN data type as a parameter
 **/

public void Var852()
 {
    if (checkBooleanSupport()) { 
   ExpectedParameterFormat ex = new ExpectedParameterFormat(
       /* name */     "JDDMDGFCBOOLEAN",
                      "(C1 BOOLEAN) RETURNS INT LANGUAGE SQL P852: BEGIN DECLARE DUMMY BOOLEAN; SET DUMMY = C1; RETURN DUMMY; END P852",
       /*columnName*/ "C1",
       /*columnType*/ "1", /* function column in */
       /*dataType*/   "16",
       /*typeName*/   "BOOLEAN",
       /*precision*/  "1",
       /*length */    "1",
       /*scale */     null,
       /*radix */     null,
       /*nullable*/   "1",
       /*remarks*/    null,
       /*octetLength*/ null,
       /*ordinalPos*/  "1",
       /*isNullable*/  "YES",
       /*specificName*/ "JDDMDGFCBOOLEAN"
   );


testDataType(ex);
    }

 }

}
