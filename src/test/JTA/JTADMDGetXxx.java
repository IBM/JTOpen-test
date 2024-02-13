///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTADMDGetXxx.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JTA;

import com.ibm.as400.access.*;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JTATest;
import test.PasswordVault;
import test.JD.JDTestUtilities;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

import javax.sql.XADataSource ; 

/**
Testcase JDDMDGetXxx.  This tests the following method(s) of the JDBC DatabaseMetaData class:

<ul>
<li>getAttributes()
<li>getCatalogSeparator()
<li>getCatalogTerm()
<li>getConnection()
<li>getDatabaseMajorVersion()
<li>getDatabaseMinorVersion()
<li>getDatabaseProductName()
<li>getDatabaseProductVersion()
<li>getDefaultTransactionIsolation()
<li>getDriverMajorVersion()
<li>getDriverMinorVersion()
<li>getDriverName()
<li>getDriverVersion()
<li>getExtraNameCharacters()
<li>getIdentifierQuoteString()
<li>getJDBCDatabaseVersion()
<li>getJDBCMajorVersion()
<li>getJDBCMinorVersion()
<li>getMaxBinaryLiteralLength()
<li>getMaxCatalogNameLength()
<li>getMaxCharLiteralLength()
<li>getMaxColumnNameLength()
<li>getMaxColumnsInGroupBy()
<li>getMaxColumnsInIndex()
<li>getMaxColumnsInOrderBy()
<li>getMaxColumnsInSelect()
<li>getMaxColumnsInTable()
<li>getMaxConnections()
<li>getMaxCursorNameLength()
<li>getMaxIndexLength()
<li>getMaxProcedureNameLength()
<li>getMaxRowSize()
<li>getMaxSchemaNameLength()
<li>getMaxStatementLength()
<li>getMaxStatements()
<li>getMaxTableNameLength()
<li>getMaxTablesInSelect()
<li>getMaxUserNameLength()
<li>getNumericFunctions()
<li>getProcedureTerm()
<li>getResultSetHoldability() 
<li>getSchemaTerm()
<li>getSearchStringEscape()
<li>getSQLKeywords()
<li>getSQLStateType()
<li>getStringFunctions()
<li>getSuperTables()
<li>getSuperTypes()
<li>getSystemFunctions()
<li>getURL()
<li>getUserName()
</ul>

**/

public class JTADMDGetXxx extends JTATestcase
{
    
   // Private data.
    private Connection         connection_;
    private Object       xaConn;
    private DatabaseMetaData   dmd_;
    private Driver	       driver_;
    private String	       url_;
    
    private	         int	        vrm_;
    private              boolean        native520_ = false;  
    static       int            VRM_450 = AS400.generateVRM(4,5,0);
    static       int            VRM_510 = AS400.generateVRM(5,1,0);
    static       int            VRM_520 = AS400.generateVRM(5,2,0);
    static       int            VRM_530 = AS400.generateVRM(5,3,0);
    
    private static final String[] numericFunctionspre510_ = { "abs","acos","asin","atan","atan2", "ceiling", "cos", "cot", "degrees", "exp", "floor", "log", "log10", "mod", "pi", "power", "round", "sin", "sign", "sqrt", "tan", "truncate"};
    
    private static final String[] numericFunctions510_ = { "abs", "acos", "asin", "atan", "atan2", "ceiling", "cos", "cot", "degrees", "exp", "floor", "log", "log10", "mod", "pi", "power", "radians", "rand", "round", "sin", "sign", "sqrt", "tan", "truncate"};
    
    private static final String[] numericFunctionsForNative_ = { "abs", "acos", "asin", "atan", "atan2", "ceiling", "cos", "cot", "degrees", "exp", "floor", "log", "log10", "mod", "pi", "power", "radians", "rand", "round", "sign", "sin", "sqrt", "tan", "truncate"};
    
    private static final String[] numericFunctionsForNative530_ = { "abs", "acos", "asin", "atan", "atan2", "ceiling", "cos", "cot", "degrees", "exp", "floor", "log", "log10", "mod", "pi", "power", "radians", "rand", "round", "sign", "sin", "sqrt", "tan", "truncate"};
    
    private static final String[] sqlKeywords_native520_ = { "CCSID", "COLLECTION", "CONCAT", "DATABASE", "PACKAGE", "PROGRAM", "RESET", "ROW", "RUN", "VARIABLE"};

    private static final String[] sqlKeywords_ = { "AFTER", "ALIAS", "ALLOW", "APPLICATION", "ASSOCIATE", "ASUTIME", "AUDIT", "AUX", "AUXILIARY", "BEFORE", "BINARY", "BUFFERPOOL", "CACHE", "CALL", "CALLED", "CAPTURE", "CARDINALITY", "CCSID", "CLUSTER", "COLLECTION", "COLLID", "COMMENT", "CONCAT", "CONDITION", "CONTAINS", "COUNT_BIG", "CURRENT_LC_CTYPE", "CURRENT_PATH", "CURRENT_SERVER", "CURRENT_TIMEZONE", "CYCLE", "DATA", "DATABASE", "DAYS", "DB2GENERAL", "DB2GENRL", "DB2SQL", "DBINFO", "DEFAULTS", "DEFINITION", "DETERMINISTIC", "DISALLOW", "DO", "DSNHATTR", "DSSIZE", "DYNAMIC", "EACH", "EDITPROC", "ELSEIF", "ENCODING", "END-EXEC1", "ERASE", "EXCLUDING", "EXIT", "FENCED", "FIELDPROC", "FILE", "FINAL", "FREE", "FUNCTION", "GENERAL", "GENERATED", "GRAPHIC", "HANDLER", "HOLD", "HOURS", "IF", "INCLUDING", "INCREMENT", "INDEX", "INHERIT", "INOUT", "INTEGRITY", "ISOBID", "ITERATE", "JAR", "JAVA", "LABEL", "LC_CTYPE", "LEAVE", "LINKTYPE", "LOCALE", "LOCATOR", "LOCATORS", "LOCK", "LOCKMAX", "LOCKSIZE", "LONG", "LOOP", "MAXVALUE", "MICROSECOND", "MICROSECONDS", "MINUTES",       "MINVALUE", "MODE", "MODIFIES", "MONTHS", "NEW", "NEW_TABLE", "NOCACHE", "NOCYCLE", "NODENAME", "NODENUMBER", "NOMAXVALUE", "NOMINVALUE", "NOORDER", "NULLS", "NUMPARTS", "OBID", "OLD", "OLD_TABLE", "OPTIMIZATION", "OPTIMIZE", "OUT", "OVERRIDING", "PACKAGE", "PARAMETER", "PART", "PARTITION", "PATH", "PIECESIZE", "PLAN", "PRIQTY", "PROGRAM", "PSID", "QUERYNO", "READS", "RECOVERY", "REFERENCING", "RELEASE", "RENAME", "REPEAT", "RESET", "RESIGNAL", "RESTART", "RESULT", "RESULT_SET_LOCATOR", "RETURN", "RETURNS", "ROUTINE", "ROW", "RRN", "RUN", "SAVEPOINT", "SCRATCHPAD", "SECONDS", "SECQTY", "SECURITY", "SENSITIVE", "SIGNAL", "SIMPLE", "SOURCE", "SPECIFIC", "SQLID", "STANDARD", "START", "STATIC", "STAY", "STOGROUP", "STORES", "STYLE", "SUBPAGES", "SYNONYM", "SYSFUN", "SYSIBM", "SYSPROC", "SYSTEM", "TABLESPACE", "TRIGGER", "TYPE", "UNDO", "UNTIL", "VALIDPROC", "VARIABLE", "VARIANT", "VCAT", "VOLUMES", "WHILE", "WLM", "YEARS"};

    private static final String[] stringFunctionsForNative_ = { "concat", "difference", "insert", "lcase", "left", "length", "locate", "ltrim", "right", "rtrim", "soundex", "space", "substring", "ucase"};

    private static final String[] stringFunctionsForNative530_ = { "concat", "difference", "insert", "lcase", "left", "length", "locate", "ltrim", "repeat", "replace", "right", "rtrim", "soundex", "space", "substring", "ucase"};

    private static final String[] stringFunctionspre510_ = { "concat", "insert", "left", "length", "locate", "ltrim", "right", "rtrim", "substring", "ucase"};

    private static final String[] stringFunctionspre520_ = { "concat", "difference", "insert", "left", "length", "locate", "ltrim", "right", "rtrim", "soundex", "space", "substring", "ucase"};
    
    private static final String[] stringFunctionspre530_ = { "concat", "difference", "insert", "lcase", "left", "length", "locate", "ltrim", "right", "rtrim", "soundex", "space", "substring", "ucase"};
    
    private static final String[] stringFunctions530_ = { "concat", "difference", "insert", "lcase", "left", "length", "locate", "ltrim", "repeat", "replace", "right", "rtrim", "soundex", "space", "substring", "ucase"};

    private static final String[] systemFunctions_ = {  "database", "ifnull", "user"};

    private static final String[] timeDateFunctionspre510_ = { "curdate", "curtime", "dayofmonth", "dayofweek", "dayofyear", "hour", "minute", "month", "now", "quarter", "second", "week", "year"};
 
    private static final String[] timeDateFunctionspre530_ = { "curdate", "curtime", "dayofmonth", "dayofweek", "dayofyear", "hour", "minute", "month", "now", "quarter", "second", "timestampdiff", "week", "year"};

    private static final String[] timeDateFunctions530_ = { "curdate", "curtime", "dayname", "dayofmonth", "dayofweek", "dayofyear", "hour", "minute", "month", "monthname", "now", "quarter", "second", "timestampdiff", "week", "year"};

    private static final String[] timeDateFunctionsForNative_ = { "curdate", "curtime", "dayofmonth", "dayofweek", "dayofyear", "hour", "minute", "month", "now", "quarter", "second", "week", "year"};

    private static final String[] timeDateFunctionsForNative530_ = { "curdate", "curtime", "dayname", "dayofmonth", "dayofweek", "dayofyear", "hour", "minute", "month", "monthname", "now", "quarter", "second", "timestampdiff", "week", "year"};


/**
Constructor.
**/
   public JTADMDGetXxx (AS400 systemObject, Hashtable namesAndVars,
                       int runMode, FileOutputStream fileOutputStream,
                        String password) {
      super (systemObject, "JTADMDGetXxx", namesAndVars,
	     runMode, fileOutputStream, password);
   }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
   protected void setup () throws Exception {
       lockSystem("JTATEST",600);
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) { 
	   url_ = "jdbc:as400://" + systemObject_.getSystemName()
	     + ";user=" + systemObject_.getUserId()
	     + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_);
	   driver_ = DriverManager.getDriver (url_);

       } else { 
	   url_ = "jdbc:db2://" + systemObject_.getSystemName()
	     + ";user=" + systemObject_.getUserId()
	     + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_);

	   driver_ = DriverManager.getDriver (url_);

	   javax.sql.XADataSource ds  = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2XADataSource");
	   JDReflectionUtil.callMethod_V(ds,"setUser",systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
	   JDReflectionUtil.callMethod_V(ds, "setPassword", charPassword); 
   PasswordVault.clearPassword(charPassword);
	   
	   JTATest.verboseOut(Thread.currentThread().getName() + ": Get an XAConnection");

       // Get the XAConnection.
	   xaConn = JDReflectionUtil.callMethod_O(ds,"getXAConnection");
	   JTATest.verboseOut(Thread.currentThread().getName() + ": Get the XAResource");

       // Get the real connection object
	   JTATest.verboseOut(Thread.currentThread().getName() + ": Get the Connection");
	   connection_ = (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

	   isIasp = JDTestUtilities.isIasp(connection_); 
	   dmd_ = connection_.getMetaData ();

       }
       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) 
	   vrm_ = testDriver_.getRelease();
       else {
	   vrm_ = testDriver_.getRelease();
	   System.out.println("vrm = "+vrm_);
	   // Set the VRM constants to match driver
	   VRM_450 = 450;
	   VRM_510 = 510;
	   VRM_520 = 520;
	   VRM_530 = 530;

	   native520_ = (vrm_ == 520);
      } 
   }


/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
   protected void cleanup () throws Exception {
       unlockSystem(); 
       if (connection_ != null) { 
	   connection_.close ();
       }
   }

/**
Compares a comma delimited string with an array of strings.
**/
   private boolean compare (String[] array, String cds) {
       boolean[] found = new boolean[array.length];
       StringTokenizer tokenizer = new StringTokenizer (cds, ", ");
       int count;
       for( count=0 ;tokenizer.hasMoreTokens ();count++) {
	   String token = tokenizer.nextToken ();
	   for (int i = 0; i < array.length; ++i) 
	       if (token.equals (array[i])) {
		   found[i] = true;
		   break;
	       }
       }
      for (int i = 0; i < found.length; ++i)
         if (! found[i])
            return false;
      return (count == array.length);
   }

/**
Returns the release as a string.
**/
   private String getReleaseAsString ()
   {
      String release;
      switch (getRelease ()) {
      default:
         release = "";
         break;
      }
      return release;
   }

/**
getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) - The iSeries does not  support structured types at this time, an empty ResultSet will always be returned for calls to this method.
**/
    public void Var001()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }

	if (checkNative ()) {

	    if (checkJdbc30 ()) {
		try {
		    ResultSet rs = dmd_.getAttributes("","","","");
		    assertCondition(!rs.next(),"New test added by Native driver 07/2003");   //should be an empty set
		    rs.close();
		}
		catch (Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }


/**
getCatalogSeparator() - Should return "/" or "." for naming convention "system"/"sql" respectively
**/
   public void Var002()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       String separator = dmd_.getCatalogSeparator();
	       assertCondition ( separator.equals("/") || separator.equals(".") );
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getCatalogTerm() - Should return the correct term.
**/
   public void Var003()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition ( dmd_.getCatalogTerm().equals("System"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getConnection() - Should return the connection on a connection.
**/
   public void Var004()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   if (checkJdbc20 ()) {
	       try {

	       /*
		Native drivers' XAConnection returns a ConnectionHandle when u try to retrieve
		a real connection object from it (this is done in the setup method) !!
		And so we call toString() method on it, which essentially calls the toString()
		method of connection object encapsulated in the connectionHandle.
	       */
                 Connection tConnection = (Connection)JDReflectionUtil.callMethod_O(dmd_,"getConnection");
		   assertCondition ( tConnection.toString().
				     equals(connection_.toString() ) );
	       } catch (Exception e)  {
		   failed (e, "Unexpected Exception");
	       }	
	   }
       }
   }


/**
getDatabaseMinorVersion() - Should return the DatabaseMinorVersion on a open connection
**/
    public void Var005()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {

	    if (checkJdbc30 ()) {

		try {
		    if(getDriver() == JDTestDriver.DRIVER_NATIVE) {
			int expectedMinorVersion = (getRelease()%100)/10;
			if (getRelease() == 550) expectedMinorVersion = 1; 
			assertCondition(dmd_.getDatabaseMinorVersion() == expectedMinorVersion,
                            "New test added by Native driver 07/2003 -> reported minor version = "+
                            dmd_.getDatabaseMinorVersion()+ " expected = "+expectedMinorVersion);  
		    } else
		    {
			if(dmd_.getDatabaseMinorVersion() == 0)         //Toolbox currently returns 0 for getDatabaseMinorVersion
			    succeeded();
			else
			    failed("Unexpected Results:  Expected 0 for getDatabaseMinorVersion() recieved " + dmd_.getDatabaseMinorVersion());
		    }
		}                                                          
		catch(Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }

/**
getDatabaseMajorVersion() - Should return the DatabaseMajorVersion on a open connection
**/
    public void Var006()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {

	    if (checkJdbc30 ()) {

		try {
		    int expectedMajorVersion = getRelease()/100;
		    if (getRelease() == 550) expectedMajorVersion = 6; 
		    assertCondition(dmd_.getDatabaseMajorVersion() == expectedMajorVersion,
                        "New test added by Native driver 07/2003");
		}                                                          
		catch(Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }

/**
getDatabaseProductName() - Should return the correct name on a connection.
**/
   public void Var007()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getDatabaseProductName().equals ("DB2 UDB for AS/400"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getDatabaseProductVersion() - Should return the correct version on a connection.

  Note: There is a difference in the layout of the string returned by the 
        Toolbox JDBC driver and the native JDBC Driver.
**/
   public void Var008()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)     
	       {
		   assertCondition (dmd_.getDatabaseProductVersion().equals (getReleaseAsString ()));
	       }   
	       else { 
		   assertCondition (dmd_.getDatabaseProductVersion().equals(System.getProperty("os.version")), "databaseProductVersion="+dmd_.getDatabaseProductVersion()+" expected "+System.getProperty("os.version"));
	       }
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getDefaultTransactionIsolation() - Should return the correct value when the "transaction isolation" property is not specified.
**/
   public void Var009()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getDefaultTransactionIsolation()
				== Connection.TRANSACTION_READ_UNCOMMITTED);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getDriverMajorVersion() - Should return the same value as Driver.getMajorVersion().
**/
   public void Var010()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getDriverMajorVersion ()
				== driver_.getMajorVersion ());
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getDriverMinorVersion() - Should return the same value as Driver.getMinorVersion().
**/
   public void Var011()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getDriverMinorVersion ()
				== driver_.getMinorVersion ());
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getDriverName() - Should return the correct name on a connection.
**/
   public void Var012()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
		   assertCondition(dmd_.getDriverName().equals("AS/400 Toolbox for Java JDBC Driver"));
	       else
		   assertCondition (dmd_.getDriverName().equals ("DB2 for OS/400 JDBC Driver"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getDriverVersion() - Should return the same value as reflected by Driver.getMajorVersion() and Driver.getMinorVersion().
**/
   public void Var013()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       String expected = driver_.getMajorVersion ()
		 + "." + driver_.getMinorVersion ();
	       assertCondition (dmd_.getDriverVersion ().equals (expected));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getExtraNameCharacters() - Should return the correct characters on a connection.
**/
   public void Var014()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getExtraNameCharacters().equals ("$@#"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getIdentifierQuoteString() - Should return the correct string on a connection.
**/
   public void Var015()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getIdentifierQuoteString().equals ("\""));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getJDBCDatabaseVersion() - Should return the DatabaseMinorVersion on a closed connection
**/
    public void Var016()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {

	    if (checkJdbc30 ()) {

		try {
		    if(getDriver() == JDTestDriver.DRIVER_NATIVE) {

			int expectedMinorVersion = (getRelease()%100)/10;
			if (getRelease() == 550) expectedMinorVersion = 1; 
			assertCondition(dmd_.getDatabaseMinorVersion() == expectedMinorVersion,
                            "New test added by Native driver 07/2003 -> reported minor version = "+
                            dmd_.getDatabaseMinorVersion()+ " expected = "+expectedMinorVersion);  
                    } else
		    {
			if(dmd_.getDatabaseMinorVersion() == 0)          //Toolbox currently returns 0 for getDatabaseMinorVersion
			    succeeded();
			else
			    failed("Unexpected Results:  Expected 0 for getDatabaseMinorVersion() recieved " + dmd_.getDatabaseMinorVersion());
		    }
		}                                                           
		catch(Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }


/**
getJDBCMajorVersion() - Should return the JDBCMajorVersion on a open connection
**/
    public void Var017()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {
	    if (checkJdbc30 ()) {

		try {
		    assertCondition(dmd_.getJDBCMajorVersion() == getJdbcLevel(),"New test added by Native driver 07/2003");
		}
		catch(Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }

/**
getJDBCMinorVersion() - Should return the JDBCMinorVersion on a open connection
**/
    public void Var018()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {

	    if (checkJdbc30 ()) {

		try {
		    assertCondition(dmd_.getJDBCMinorVersion() == 0,"New test added by Native driver 07/2003"); //We will need a better way to do 
		}                                                      //this in the future
		catch(Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}

    }




/**
getMaxBinaryLiteralLength() - Should return the correct value on a connection.
**/
   public void Var019()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxBinaryLiteralLength() == 32739);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxCatalogNameLength() - Should return the correct value on a connection.
**/
   public void Var020()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxCatalogNameLength() == 10);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxCharLiteralLength() - Should return the correct value on a connection.
**/
   public void Var021()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxCharLiteralLength() == 32739);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxColumnNameLength() - Should return the correct value on a connection.
**/
   public void Var022()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if(getRelease() <= JDTestDriver.RELEASE_V7R1M0)               //@F1A
		   assertCondition (dmd_.getMaxColumnNameLength() == 30);
	       else                                                          //@F1A
		   assertCondition (dmd_.getMaxColumnNameLength() == 128);   //@F1A
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxColumnsInGroupBy() - Should return the correct value on a connection.
**/
   public void Var023()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)     {     
		   int maxColumnsInGroupBy = dmd_.getMaxColumnsInGroupBy(); 
		   assertCondition( maxColumnsInGroupBy == 8000,
				    "maxColumnsInGroupBy is "+maxColumnsInGroupBy+" sb 8000" );
	       } else                                                     

		   assertCondition (dmd_.getMaxColumnsInGroupBy() == 120);


	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxColumnsInIndex() - Should return the correct value on a connection.
**/
   public void Var024()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxColumnsInIndex() == 120);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxColumnsInOrderBy() - Should return the correct value on a connection.
**/
   public void Var025()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxColumnsInOrderBy() == 10000);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxColumnsInSelect() - Should return the correct value on a connection.
**/
   public void Var026()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxColumnsInSelect() == 8000);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxColumnsInTable() - Should return the correct value on a connection.
**/
   public void Var027()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxColumnsInTable() == 8000);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxConnections() - Should return the correct value on a connection.
**/
   public void Var028()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxConnections() == 0);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxCursorNameLength() - Should return the correct value on a connection.
**/
   public void Var029()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if(getRelease() < JDTestDriver.RELEASE_V7R1M0)          
		   assertCondition (dmd_.getMaxCursorNameLength() == 18);
	       else 
		   assertCondition (dmd_.getMaxCursorNameLength() == 128);   
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }



/**
getMaxIndexLength() - Should return the correct value on a connection.
**/
   public void Var030()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxIndexLength() == 2000);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxProcedureNameLength() - Should return the correct value on a connection.
**/
   public void Var031()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxProcedureNameLength() == 128);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getMaxRowSize() - Should return the correct value on a connection.
**/
   public void Var032()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxRowSize() == 32766);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxSchemaNameLength() - Should return the correct value on a connection.
**/
   public void Var033()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxSchemaNameLength() == 10);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxStatementLength() - Should return the correct value on a connection.
**/
   public void Var034()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) {                         //@F2A
		   assertCondition(dmd_.getMaxStatementLength() == 1048576,
				   "Max statement length is "+dmd_.getMaxStatementLength()+" sb  1048576"); 
	       } else {                                                                    //@F2A
		   assertCondition (dmd_.getMaxStatementLength() == 32767,
				    "Max statement lenght is "+dmd_.getMaxStatementLength()+"sb  32767"); 
	       }
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxStatements() - Should return the correct value on a connection.

  Note:  The handle limit for the native JDBC driver differs
         from that of the toolbox JDBC driver.  The native
         driver does not have a hard limit on the number of
         statements and will always return a 0.
**/
   public void Var035()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
		   assertCondition (dmd_.getMaxStatements() == 9999); // @C1C
	       else
		   assertCondition (dmd_.getMaxStatements() == 0); // @D3C
	   }
	   catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxTableNameLength() - Should return the correct value on a connection.
**/
   public void Var036()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxTableNameLength() == 128);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxTablesInSelect() - Should return the correct value on a connection.
**/
   public void Var037()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
		   assertCondition (dmd_.getMaxTablesInSelect() == 1000);
	       } else { 
		   assertCondition (dmd_.getMaxTablesInSelect() == 32);
	       }
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getMaxUserNameLength() - Should return the correct value on a connection.
**/
   public void Var038()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getMaxUserNameLength() == 10);
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getNumericFunctions() - Should return the correct value on a connection.

  Note:  The native JDBC driver supports a couple numeric functions that
         the Toolbox does not support.  The return values are pretty close
         to the same.
**/
   public void Var039()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       String[]  expected; 
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
		   if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       expected = numericFunctionspre510_;
		   } else {
		       expected = numericFunctions510_; 
		   }
	       } else {
		   if (vrm_ < VRM_530) { 
		       expected = numericFunctionsForNative_;
		   } else {
		       expected = numericFunctionsForNative530_;
		   } 
	       } 
	       boolean condition = compare (expected, dmd_.getNumericFunctions ());
	       if (!condition) {
		   System.out.println("numeric functions do not match");
		   System.out.println("actual  : " + dmd_.getNumericFunctions() );
		   System.out.print("expected: ");
		   for (int i = 0; i < expected.length; i++) System.out.print(expected[i]+" ");
		   System.out.println(); 
	       } 

	       assertCondition (condition, "condition = " + condition + " and should be true");
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getProcedureTerm() - Should return the correct term on a connection.
**/
   public void Var040()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getProcedureTerm().equals ("Procedure"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }



/**
getResultSetHoldability() - Should return the holdability on a connection.
**/
   public void Var041()                                                                             
   {                
     if     (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
    if ((checkNative ())) {

       if (areCursorHoldabilitySupported ()) {                                                       
	   try {                                                                                      
	       assertCondition (dmd_.getResultSetHoldability() == ResultSet.HOLD_CURSORS_OVER_COMMIT); 
	   } catch (Exception e)  {                                                                   
	       failed (e, "Unexpected Exception");                                                     
	   }                                                                                          
       }
       else
       {
	   notApplicable("Cursor holdability (need v5r2 and 1.4)");
       }
   }
   }                  

/**
getSchemaTerm() - Should return the correct term on a connection.
**/
   public void Var042()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getSchemaTerm().equals ("Library"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getSearchStringEscape() - Should return the correct value on a connection.
**/
   public void Var043()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {

	   try {
	       assertCondition (dmd_.getSearchStringEscape().equals ("\\"));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getSQLKeywords() - Should return the correct value on a connection.
**/
   public void Var044() {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       String keywords =  dmd_.getSQLKeywords (); 
	       if (native520_) 
		   assertCondition(compare (sqlKeywords_native520_, keywords));
	       else 
		   assertCondition(compare (sqlKeywords_, keywords));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getSQLStateType() - for the native driver we always return sqlState99, I am not sure if toolbox does the same
**/
    public void Var045()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {
	    if (checkJdbc30 ()) {

		try {
		    assertCondition(dmd_.getSQLStateType() == 2,"New test added by Native driver 07/2003"); //sqlStateSQL99 == 2
		}
		catch (Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }


/**
getStringFunctions() - Should return the correct value on a connection.
**/
   public void Var046()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
	       {
		   if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       assertCondition(compare(stringFunctionspre510_, dmd_.getStringFunctions()));
		   } else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       assertCondition(compare(stringFunctionspre520_, dmd_.getStringFunctions()));
		   } else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       assertCondition(compare(stringFunctionspre530_, dmd_.getStringFunctions()));
		   } else {
		       assertCondition(compare(stringFunctions530_, dmd_.getStringFunctions()));
		   }
	       } else {
		   String[] expected;
		   if (vrm_ < VRM_530) { 
		       expected = stringFunctionsForNative_;
		   } else {
		       expected = stringFunctionsForNative530_;
		   } 
		   boolean condition;
		   String s = dmd_.getStringFunctions();
		   condition = compare(expected, s);

		   if (!condition) {
		       System.out.println("StringFunctions do not match");
		       System.out.println("actual  : "+s);
		       System.out.print("expected: ");
		       for (int i = 0; i < expected.length; i++)
			   System.out.print(expected[i]+" ");
		       System.out.println(); 
		   } 
		   assertCondition (condition, "condition = " + condition + " and should be true");
	       }
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }   

/**
getSuperTables(String catalog, String schemaPattern, String tableNamePattern) - The iSeries has no concept of a table hierarchy so the results of executing this method will always be an empty ResultSet.
**/
    public void Var047()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {
	    if (checkJdbc30 ()) {

		try {
		    ResultSet rs = dmd_.getSuperTables("","","");
		    assertCondition(!rs.next(),"New test added by Native driver 07/2003");   //should be an empty set
		    rs.close();
		}
		catch (Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }

/**
getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) -  The iSeries has no concept of a type hierarchy so the results of executing this method will always be an empty ResultSet.
**/
    public void Var048()
    {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
	if (checkNative ()) {
	    if (checkJdbc30 ()) {

		try {
		    ResultSet rs = dmd_.getSuperTypes("","","");
		    assertCondition(!rs.next(),"New test added by Native driver 07/2003");   //should be an empty set
		    rs.close();
		}
		catch (Exception e){
		    failed(e,"Unexpected Exception - New test added by Native driver 07/2003");
		}
	    }
	}
    }



/**
getSystemFunctions() - Should return the correct value on a connection.
**/
   public void Var049()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (compare (systemFunctions_, dmd_.getSystemFunctions ()));
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getTimeDateFunctions() - Should return the correct value on a connection.

  Note:  The native JDBC driver supports a couple timedate functions that
         the Toolbox does not support.  The return values are pretty close
         to the same.
**/
   public void Var050()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       String expected[]; 
	       if (getDriver () == JDTestDriver.DRIVER_TOOLBOX) {
		   if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       expected = timeDateFunctionspre510_;
		   } else if (getRelease() < JDTestDriver.RELEASE_V7R1M0) {
		       expected = timeDateFunctionspre530_;
		   } else {
		       expected = timeDateFunctions530_; 
		   }
	       } else  {
		   if (vrm_ < VRM_530) { 
		       expected = timeDateFunctionsForNative_;
		   } else {
		       expected = timeDateFunctionsForNative530_;
		   } 
	       }

	       boolean condition = compare (expected, dmd_.getTimeDateFunctions ());
	       if (!condition) {
		   System.out.println("time date functions do not match");
		   System.out.println("actual  : " + dmd_.getTimeDateFunctions() );
		   System.out.print("expected: ");
		   for (int i = 0; i < expected.length; i++) System.out.print(expected[i]+" ");
		   System.out.println(); 
	       } 

	       assertCondition (condition, "condition = " + condition + " and should be true");

	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

/**
getURL() - Should return the correct value on a connection.
**/
   public void Var051()
   { 
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	  /*
	   TODO: We would need to figure out a way to pass the url_ value while creating
	   XAConnection so that it returns a proper value. As of now, the following check should suffice !
	  */
	       if(getDriver() == JDTestDriver.DRIVER_NATIVE)
		   assertCondition (dmd_.getURL ().toString().indexOf("jdbc:db2:")!=-1);
	       else
		   assertCondition (dmd_.getURL ().toString().indexOf("jdbc:as400:")!=-1);

	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }



/**
getUserName() - Should return the correct value on a connection.
**/
   public void Var052()
   {
     if (isIasp && (! isNTS)) {
       notApplicable("Not applicable for IASP and not NTS");
       return; 
     }
       if (checkNative ()) {
	   try {
	       assertCondition (dmd_.getUserName ().equals (systemObject_.getUserId ()),
				"dmd_.getUserName="+dmd_.getUserName()+
				" systemObject_.getUserId ()="+systemObject_.getUserId ()); 
	   } catch (Exception e)  {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

}
