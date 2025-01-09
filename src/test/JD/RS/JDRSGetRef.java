///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetRef.java
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
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Ref;

import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDRSGetRef.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>getRef()
</ul>
**/
public class JDRSGetRef
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetRef";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private ResultSet           rs_;



/**
Constructor.
**/
    public JDRSGetRef (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDRSGetRef",
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
        if (isJdbc20 ()) {
            // SQL400 - driver neutral...
            String url = baseURL_
            // String url = "jdbc:as400://" + systemObject_.getSystemName()
                
                
	        + ";errors=full"    ;
            connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
	    setAutoCommit(connection_, false); // @C1A

            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
            statement_.executeUpdate ("INSERT INTO " + JDRSTest.RSTEST_GET
                + " (C_KEY) VALUES ('DUMMY_ROW')");
            rs_ = statement_.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET + " FOR UPDATE");
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
            connection_.commit(); // @C1A
            connection_.close ();
        }
    }



/**
getRef() - Should throw exception when the result set is
closed.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        try {
            Statement s = connection_.createStatement ();
            ResultSet rs = s.executeQuery ("SELECT * FROM "
                + JDRSTest.RSTEST_GET);
            rs.next ();
            rs.close ();
            rs.getRef (1);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw exception when cursor is not pointing
to a row.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, null);
            Ref v = rs_.getRef (1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column
is an invalid index.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef (100);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column
is 0.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef (0);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column
is -1.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef (-1);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column index is valid.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef (3);
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column
name is null.
**/
    public void Var007()
    {
      if (checkJdbc20 ()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          notApplicable("JCC throws null pointer exception when column name is null "); 
        } else { 
          try {
            JDRSTest.position (rs_, "NUMBER_POS");
            rs_.getRef (null);
            failed ("Didn't throw SQLException");
          }
          catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
          }
        }
      }
    }
    


/**
getRef() - Should throw an exception when the column
name is an empty string.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            rs_.getRef ("");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column
name is invalid.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            rs_.getRef ("INVALID");
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the column name is valid.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when an update is pending.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", 19222228);
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when an update has been done.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "UPDATE_SANDBOX");
            rs_.updateLong ("C_INTEGER", -1111222334);
            rs_.updateRow ();
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the current row is the insert
row, when an insert is pending.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            rs_.moveToInsertRow ();
            rs_.updateLong ("C_INTEGER", 1893);
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception when the current row is the insert
row, when an insert has been done.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            notApplicable("JCC does not support moveToInserRow"); 
            return; 
          }
        try {
            rs_.moveToInsertRow ();
            rs_.updateLong ("C_INTEGER", 21027);
            rs_.insertRow ();
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Should throw an exception on a deleted row.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
          if (getDriver() == JDTestDriver.DRIVER_JCC) { 
            notApplicable("JCC doesn't throw exception for get on deleted row");
            return; 
          }
        try {
            JDRSTest.position (rs_, "DUMMY_ROW");
            rs_.deleteRow ();
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }


/**
getRef() - Should throw an exception when the column is NULL.
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_NULL");
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a SMALLINT.
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_NEG");
            Ref v = rs_.getRef ("C_SMALLINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a INTEGER.
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef ("C_INTEGER");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a REAL.
**/
    public void Var019 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_NEG");
            Ref v = rs_.getRef ("C_REAL");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a FLOAT.
**/
    public void Var020 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef ("C_FLOAT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a DOUBLE.
**/
    public void Var021 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_NEG");
            Ref v = rs_.getRef ("C_DOUBLE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a DECIMAL.
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef ("C_DECIMAL_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a NUMERIC.
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "NUMBER_NEG");
            Ref v = rs_.getRef ("C_NUMERIC_105");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a CHAR, where the value does not translate
to an int.
**/
    public void Var024 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_FULL");
            Ref v = rs_.getRef ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a CHAR, where the value does translate
to an int.
**/
    public void Var025 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_INT");
            Ref v = rs_.getRef ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a CHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var026 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_FLOAT");
            Ref v = rs_.getRef ("C_CHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a VARCHAR, where the value does not translate
to an int.
**/
    public void Var027 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_FULL");
            Ref v = rs_.getRef ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a VARCHAR, where the value does translate
to an int.
**/
    public void Var028 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_INT");
            Ref v = rs_.getRef ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a VARCHAR, where the value does translate
to an int, except that it has a decimal point
**/
    public void Var029 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "CHAR_FLOAT");
            Ref v = rs_.getRef ("C_VARCHAR_50");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a BINARY.
**/
    public void Var030 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "BINARY_TRANS");
            Ref v = rs_.getRef ("C_BINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a VARBINARY.
**/
    public void Var031 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "BINARY_TRANS");
            Ref v = rs_.getRef ("C_VARBINARY_20");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a CLOB.
**/
    public void Var032 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, "LOB_FULL");
                Ref v = rs_.getRef ("C_CLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getRef() - Get from a DBCLOB.
**/
    public void Var033 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, "LOB_FULL");
                Ref v = rs_.getRef ("C_DBCLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getRef() - Get from a BLOB.
**/
    public void Var034 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, "LOB_FULL");
                Ref v = rs_.getRef ("C_BLOB");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getRef() - Get from a DATE.
**/
    public void Var035 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DATE_1998");
            Ref v = rs_.getRef ("C_DATE");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a TIME.
**/
    public void Var036 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DATE_1998");
            Ref v = rs_.getRef ("C_TIME");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a TIMESTAMP.
**/
    public void Var037 ()
    {
        if (checkJdbc20 ()) {
        try {
            JDRSTest.position (rs_, "DATE_1998");
            Ref v = rs_.getRef ("C_TIMESTAMP");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
    }



/**
getRef() - Get from a DATALINK.
**/
    public void Var038 ()
    {
        if (checkJdbc20 ()) {
        if (checkDatalinkSupport ()) {
            try {
                Statement s = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = s.executeQuery ("SELECT * FROM "
                    + JDRSTest.RSTEST_GETDL);
                JDRSTest.position (rs, "LOB_FULL");
                Ref v = rs.getRef ("C_DATALINK");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getRef() - Get from a DISTINCT.
**/
    public void Var039 ()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                JDRSTest.position (rs_, "LOB_EMPTY");
                Ref v = rs_.getRef ("C_DISTINCT");
                failed ("Didn't throw SQLException"+v);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }



/**
getRef() - Get from a BIGINT.
**/
    public void Var040()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
        try {
            JDRSTest.position (rs_, "NUMBER_POS");
            Ref v = rs_.getRef ("C_BIGINT");
            failed ("Didn't throw SQLException"+v);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
        }
        }
    }

    /**
    getRef() - Get from DFP16:
    **/
   public void Var041 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP16);
         rs.next(); 
         Ref v = rs.getRef (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
   
   /**
    getRef() - Get from DFP34:
    **/
   public void Var042 ()
   {
     if (checkDecFloatSupport()) {
       try {
         Statement s = connection_.createStatement ();
         ResultSet rs = s.executeQuery ("SELECT * FROM "
             + JDRSTest.RSTEST_DFP34);
         rs.next(); 
         Ref v = rs.getRef (1);
         failed ("Didn't throw SQLException "+v);
       }
       catch (Exception e) {
         assertExceptionIsInstanceOf (e, "java.sql.SQLException");
       }
     }
   }
   
   
        /**
         * getRef() - Get from a TIMESTAMP(12).
         **/
        public void Var043() {
            if (checkTimestamp12Support()) {
              testGet(statement_,
                  "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(12)) from SYSIBM.SYSDUMMY1",
                  "getRef", 
                  "EXCEPTION:Data type mismatch.", 
                  " -- added 11/19/2012"); 

            }
        }


        /**
         * getRef() - Get from a TIMESTAMP(0).
         **/
        public void Var044() {
            if (checkTimestamp12Support()) {
          testGet(statement_,
            "SELECT CAST('1998-11-18 03:13:42.987654' AS TIMESTAMP(0)) from SYSIBM.SYSDUMMY1",
            "getRef",
		  "EXCEPTION:Data type mismatch.", 
            " -- added 11/19/2012"); 
            
            
            }
        }



/**
getRef() - Get from a BOOLEAN.
**/
	public void Var045 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_TRUE");
		    Ref v = rs.getRef ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}


/**
getRef() - Get from a BOOLEAN.
**/
	public void Var046 ()
	{
	    if (checkBooleanSupport()) {
		try {
		    ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							     + JDRSTest.RSTEST_BOOLEAN);
		    JDRSTest.position0 (rs, "BOOLEAN_FALSE");
		    Ref v = rs.getRef ("C_BOOLEAN");
		    failed ("Didn't throw SQLException"+v);
		}
		catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}

/**
getRef() - Get from a BOOLEAN.
**/
    public void Var047()
    {
 
	if (checkBooleanSupport()) {
	    try {
		ResultSet rs = statement_.executeQuery ("SELECT * FROM "
							+ JDRSTest.RSTEST_BOOLEAN);
		JDRSTest.position0 (rs, "BOOLEAN_NULL");
		Ref v = rs.getRef ("C_BOOLEAN");
		failed ("Didn't throw SQLException"+v);
	    }
	    catch (Exception e) {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }




}



