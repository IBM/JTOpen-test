///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionLibraries.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionLibraries.java
//
// Classes:      JDConnectionLibraries
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;  
import com.ibm.as400.access.SecureAS400;

import test.JDConnectionTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JVMInfo;
import test.PasswordVault;
import test.JD.JDSetupCollection;
import test.misc.TimeoutThread;
import test.misc.TimeoutThreadCallback;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.Hashtable; import java.util.Vector;
import java.util.Properties;  
import java.util.StringTokenizer;
import com.ibm.as400.access.CommandCall;


/**
Testcase JDConnectionLibraries.  This tests the following
properties with respect to the JDBC Connection class:

<ul>
<li>default schema (specified in URL)
<li>libraries
</ul>
**/
public class JDConnectionLibraries 
extends JDTestcase implements TimeoutThreadCallback {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionLibraries";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }

  static  boolean newChecklibraryList = true; 


	// Private data.
	public static  String COLLECTION2    = "JDTESTCON2";

	String iniLibrary = null; 
	int jdk_;
	/** 
	Constructor.
	**/
	public JDConnectionLibraries (AS400 systemObject,
								  Hashtable<String,Vector<String>> namesAndVars,
								  int runMode,
								  FileOutputStream fileOutputStream,
								  
								  String password,
                                                                  String pwrUserId,
                                                                  String pwrPwd)                //@C1C
	{
    super(systemObject, "JDConnectionLibraries", namesAndVars, runMode,
        fileOutputStream, password);
    pwrSysUserID_ = pwrUserId; // @C1A
    pwrSysEncryptedPassword_ = PasswordVault.getEncryptedPassword(pwrPwd);

	}



	/**
	Performs setup needed before running variations.

	@exception Exception If an exception occurs.
	**/
	protected void setup ()
	throws Exception
	{

	    jdk_ = JVMInfo.getJDK(); 
		Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
		if (collection_ != null) {
			COLLECTION2 = collection_+"2";
		}
		JDSetupCollection.create (systemObject_,  c,
								  COLLECTION2, output_);
		c.close ();

		String piece = COLLECTION2;
		int length=piece.length();
		if (length > 5) {
		    piece = piece.substring(length-5);
		}
		iniLibrary = "JDQAQ"+piece; 
	}



	/**
	Checks that a schema is in the library list.
	@param  c       The connection.
	@param  schema  The schema.
	@return         true if the schema is in the library list,
					false otherwise.
	**/

  private boolean checkLibraryList(Connection c, String schema, StringBuffer sb)
      throws Exception {

    boolean found = false;
    if (newChecklibraryList) {
      Statement stmt = c.createStatement();

      String sql;
      sql = "CALL QSYS2.QCMDEXC('dltf file(qtemp/x)') ";
      try {
        stmt.executeUpdate(sql);
      } catch (Exception e) {
        // Ignore any error 
      }
      sql = "CALL QSYS2.QCMDEXC('crtpf file(qtemp/x) rcdlen(200)  SIZE(10000 1000 30000)')";
      stmt.executeUpdate(sql);
      sql = "CALL QSYS2.QCMDEXC('ovrdbf file(QPRTLIBL) tofile(qtemp/x) lvlchk(*NO)  OVRSCOPE(*JOB) ')";
      stmt.executeUpdate(sql);
      sql = "CALL QSYS2.QCMDEXC('DSPLIBL OUTPUT(*PRINT)')";
      stmt.executeUpdate(sql);

      sql = "select CAST(X AS VARCHAR(200) CCSID 37) from qtemp.x";
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String schema2 = rs.getString(1);
        if (schema2.indexOf("USR ") == 10 ) {
          schema2 = schema2.substring(0, 10).trim();
          sb.append("looking at " + schema2 + "\n");
          if (schema2.equalsIgnoreCase(schema))
            found = true;
        } else {
          sb.append("not USR library "+schema2.trim()+"\n"); 
        }
      }

    } else {

      DatabaseMetaData dmd = c.getMetaData();
      ResultSet rs = dmd.getSchemas();
      while (rs.next()) {
        String schema2 = rs.getString("TABLE_SCHEM");
        sb.append("looking at " + schema2 + "\n");
        if (schema2.equalsIgnoreCase(schema))
          found = true;
      }
    }
    if (!found) {
      sb.append("Did not find " + schema + "\n");
    }
    return found;
  }


	/**
	Checks that a schema is the default schema.

	@param  c       The connection.
	@param  schema  The schema.
	@return         true if the schema is the default schema,
					false otherwise.

	SQL400 - The native JDBC driver gets different error messages from
			 the Toolbox and therefore must examine the error messages
			 for different values.
	**/
	String getDefaultSchemaErrorMessage = "";
	private String getDefaultSchema (Connection c)
	throws Exception
	{
		String defaultSchema = "";
		getDefaultSchemaErrorMessage = "";
		Statement s = c.createStatement ();
		try {
			s.executeQuery ("SELECT * FROM NOT_EXIST");
		}
		catch (SQLException e) {
			String message = e.getMessage ();
			getDefaultSchemaErrorMessage = message;
			String token = "";
			int word;
			if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
				// There are 2 possible messages that you can get natively...
				// 1) <lib> in QSYS type *LIB not found.  -> library doesn't exist
				// 2) <file> in <lib> type *file not found     -> file doesn't exist, library does

				// Tokenize the message on word boundaries, ignore spaces.
				StringTokenizer tokenizer = new StringTokenizer (message, " ", false);

				// There should be at least 3 words in the message.
				if (tokenizer.countTokens () < 5)
					return null;

				// Get words 1, 3, and 5 from the tokenizer.
				String word1 = tokenizer.nextToken();
				tokenizer.nextToken();
				String word3 = tokenizer.nextToken();
				tokenizer.nextToken();
				String word5 = tokenizer.nextToken();

				if (word5.equals("*LIB"))
					defaultSchema = word1;
				else
					if (!word3.equals("*LIBL"))
					defaultSchema = word3;
			}
			else {
				StringTokenizer tokenizer = new StringTokenizer (message);
				for (word = 0; tokenizer.hasMoreTokens () && word < 4;token = tokenizer.nextToken (), ++word);
				{
					if (word == 4) {
						if (! token.equals ("*LIBL"))
							defaultSchema = token;
					}
				}
			}
		}

		return defaultSchema;
	}



	/**
	default schema - No default schema is specified in the URL,
	no libraries are specified, and sql naming.  Should use the schema with
	the same name as the userId as the default.
	**/
	public void Var001()
	{
		try {
			Connection c = testDriver_.getConnection (baseURL_ + ";naming=sql",
													  userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_), "Default schema is '"+defaultSchema+"' should be '"+userId_+"' URL was "+baseURL_ + ";naming=sql schema retrieved from '"+getDefaultSchemaErrorMessage+"'");
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	default schema - No default schema is specified in the URL,
	no libraries are specified, and system naming.  Should have no default.
	**/
	public void Var002()
	{
		try {
			Connection c = testDriver_.getConnection (baseURL_ + ";naming=system",
													  userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();

			assertCondition (defaultSchema.equalsIgnoreCase (""), "defaultSchema is '"+defaultSchema+"' sb '' URL was "+baseURL_);

		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	default schema - Specifies an invalid default schema in the URL.
	Should throw an exception.

	SQL400 - the Native JDBC driver will not validate that the library
	passed in is a valid library that currently exists on the system.
	To do so is error prone due to the fact that DRDA has no mechanism
	to check and return information about a default library at connection
	time.  The native driver will only check that the length of the string
	passed in is valid.

	This testcase now obsolete.  The Toolbox used to have code that verified each
	character in the name was a letter or a number.  This was bad code.  For
	example, customers with underscores in their schema names would not work.
	The code was removed.  Now we send whatever we get to the server and let them
	deal with it.


	**/
	public void Var003()
	{
	   succeeded();
	   return;
	   /*
		try {
			Connection c = testDriver_.getConnection (baseURL_ + "/BAD_SCHEMA",
													  userId_, encryptedPassword_);

			if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
				assertCondition(true);
				c.close();
			}
			else
				failed ("Did not throw exception.");

		}
		catch (Exception e) {
			if (getDriver() == JDTestDriver.DRIVER_NATIVE)
				failed(e, "Unexpected exception");
			else
				assertExceptionIsInstanceOf	(e, "java.sql.SQLException");
		}
		*/
	}




	/**
	default schema - Specifies an default schema that is longer than
	10 characters in the URL.  Should throw an exception.
	**/
	public void Var004()
	{

	    Connection c =  null;

	    try {

        //@128len
	    //now max is 128
		c = DriverManager.getConnection (baseURL_ + "/" + JDConnectionTest.SCHEMAS_LEN128 + "EXTRA", userId_, PasswordVault.decryptPasswordLeak(encryptedPassword_));

		//@B6 since we are unable to validate the schema when we creat the connection,
		// we do not bother to validate that it is not too long either.
		      if (isToolboxDriver())
		      {
			  failed ("Did not throw exception. for "+c);
		      }
		      else
		      {
			      succeeded();
		      }

	    }
	    catch (Exception e) {
		if(isToolboxDriver())
		{
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
		else
		{
			failed(e, "Unexpected exception");
		}
	    } finally {
		try {
		    if (c != null) {
			c.close(); 
		    }
		} catch (Exception e) {}
	    }
	}



	/**
	default schema - Specifies a default schema in the URL.
	Verifies that this schema is now the default schema.
	**/
	public void Var005()
	{
		try {
			Connection c = testDriver_.getConnection (baseURL_ + "/"
													  + JDConnectionTest.COLLECTION, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION),
					 "defaultSchema="+defaultSchema+" sb "+JDConnectionTest.COLLECTION);
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces),
	and sql naming.
	Should use the first library as the default.
	**/
	public void Var006()
	{
		try
		{
			Connection c = testDriver_.getConnection (baseURL_
													  + ";naming=sql;libraries=" + JDConnectionTest.COLLECTION
													  + " " + COLLECTION2, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by commas),
	and sql naming.
	Should use the first library as the default.
	**/
	public void Var007()
	{
		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + ";libraries=" + COLLECTION2
													  + "," + JDConnectionTest.COLLECTION, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (COLLECTION2));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces),
	and system naming. Should have no default.
	@P9951698 default added back in v5r2
	@P9A03018 changed so works like v5r1 so there is no behavior change.
	**/
	public void Var008()
	{
		try {
		    String thisURL=baseURL_
		      + ";naming=system;libraries=" + JDConnectionTest.COLLECTION
		      + " " + COLLECTION2;

			Connection c = testDriver_.getConnection (thisURL, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
                        boolean condition = defaultSchema.equalsIgnoreCase ("");
			if (!condition) {
			    output_.println("defaultSchema is "+defaultSchema+" URL is "+thisURL);
			}
			// assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
			assertCondition (condition);

		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - Specifies a default schema and some libraries.
	Verify that the default schema comes from the URL.
	**/
	public void Var009()
	{
		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + "/" + JDConnectionTest.COLLECTION
													  + ";libraries="
													  + "," + COLLECTION2, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - No default schema is specified in the URL,
	no libraries are specified.  The schema with the same name
	as the userId should the current schema. 
	**/
	public void Var010()
	{
	    StringBuffer sb = new StringBuffer();
		try {
		  boolean success = true; 
			Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			if (!defaultSchema.equalsIgnoreCase(userId_)) {
			  sb.append("defaultSchema is "+defaultSchema+" sb "+userId_+"\n");
			  success = false; 
			}
			assertCondition (success,sb);
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	libraries - Specifies a default schema in the URL.
	Verifies that this schema is in the library list.
	**/
	public void Var011()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_ + "/"
													  + JDConnectionTest.COLLECTION+";naming=system", userId_, encryptedPassword_);
			boolean success = checkLibraryList (c, JDConnectionTest.COLLECTION, sb);
			c.close ();
			assertCondition (success,sb);
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	libraries - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces).
	Those libraries should be in the library list.
	**/
	public void Var012()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + ";naming=system;libraries=" + JDConnectionTest.COLLECTION
													  + " " + COLLECTION2, userId_, encryptedPassword_);
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION, sb);
			boolean successB = checkLibraryList (c, COLLECTION2, sb);
			c.close ();
			assertCondition (successA && successB, sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - No default schema is specified in
	the URL, but libraries are specified (delimited by commas).
	Those libraries should be in the library list.
	**/
	public void Var013()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + ";naming=system;libraries=" + COLLECTION2
													  + "," + JDConnectionTest.COLLECTION, userId_, encryptedPassword_);
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			c.close ();
			assertCondition (successA && successB,sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specifies a default schema and some libraries.
	Verify that the default schema and the libraries are
	in the library list.
	**/
	public void Var014()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + "/" + JDConnectionTest.COLLECTION
													  + ";libraries="
													  + "," + COLLECTION2+";naming=system", userId_, encryptedPassword_);
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			c.close ();
			assertCondition (successA && successB,sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}




	/**
	libraries - Specifies a bad library in the library list.
	Should throw an exception.

	In v5r2 the Toolbox driver was changed to post a warning if a library
	does not exist or is already in the list.
	**/
	public void Var015()
	{
		try
		{
		  //Toolbox: in v7r1, if default schema (first in libraries) is greater than length 10, then it is not even added to lib list, and will not get warning....
			Connection c = testDriver_.getConnection (baseURL_
								  + ";libraries=BAD_LIB", userId_, encryptedPassword_);
			if (getDriver() == JDTestDriver.DRIVER_NATIVE)
			{
				c.close();
				/** The native driver doen't care about the libraries property unless system naming is used */
				succeeded();
			}
			else
			{
				if (verifyWarning(c.getWarnings(), 1301))
					succeeded();
				else
					failed ("Did not post correct warning.");
			}
			c.close();

		}
		catch (Exception e)
		{
			failed(e, "Unexpected Exception");
		}
	}




	/**
	libraries - Specify library list with *LIBL only.
	**/
	public void Var016()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + ";libraries=*LIBL", userId_, encryptedPassword_);
			boolean successA = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA,sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with 2 libraries, but not *LIBL.
	**/
	public void Var017()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c = testDriver_.getConnection (baseURL_
													  + ";naming=system;libraries=" + JDConnectionTest.COLLECTION
													  + "," + COLLECTION2, userId_, encryptedPassword_);
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC;
			if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
			  // If *LIBL is not specified then QGPL will not exist
		     successC = ! checkLibraryList (c, "QGPL",sb);

			} else { 
			  successC = checkLibraryList (c, "QGPL",sb);
			}
			c.close ();
			assertCondition (successA && successB && successC,sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL before 2 other libraries.
	**/
	public void Var018()
	{
	    StringBuffer sb = new StringBuffer();

		try {
			Connection c;
			if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
			  // Native must use system naming to get into the library list. 
	      c = testDriver_.getConnection (baseURL_
            + ";naming=system;libraries=*LIBL," + JDConnectionTest.COLLECTION
            + "," + COLLECTION2, userId_, encryptedPassword_);
			  
			} else { 
			c = testDriver_.getConnection (baseURL_
													  + ";libraries=*LIBL," + JDConnectionTest.COLLECTION
													  + "," + COLLECTION2, userId_, encryptedPassword_);
			}
			
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION, sb);
			boolean successB = checkLibraryList (c, COLLECTION2, sb);
			boolean successC = checkLibraryList (c, "QGPL", sb);
			c.close ();
			assertCondition (successA && successB && successC, sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL between 2 other libraries.
	**/
	public void Var019()
	{
    StringBuffer sb = new StringBuffer();
		try {
			Connection c ;
	     if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	       c = testDriver_.getConnection (baseURL_
             + ";naming=system;libraries=" + JDConnectionTest.COLLECTION
             + ",*LIBL," + COLLECTION2, userId_, encryptedPassword_);
	     } else { 
			c = testDriver_.getConnection (baseURL_
													  + ";libraries=" + JDConnectionTest.COLLECTION
													  + ",*LIBL," + COLLECTION2, userId_, encryptedPassword_);
	     }
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC, sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL after 2 other libraries.
	**/
	public void Var020()
	{
    StringBuffer sb = new StringBuffer();
		try {
			Connection c;
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        c = testDriver_.getConnection (baseURL_
            + ";naming=system;libraries=" + JDConnectionTest.COLLECTION
            + "," + COLLECTION2 + ",*LIBL", userId_, encryptedPassword_);
      } else { 
			c = testDriver_.getConnection (baseURL_
													  + ";libraries=" + JDConnectionTest.COLLECTION
													  + "," + COLLECTION2 + ",*LIBL", userId_, encryptedPassword_);
      }
			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC, sb);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}


	/**
default schema - No default schema is specified in the URL,
no libraries are specified, and sql naming (the default).  Should use the schema with
the same name as the userId as the default.
**/
	public void Var021()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			AS400JDBCDriver d = new AS400JDBCDriver();
			
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			
			Connection c = d.connect (o);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}


	/**
	default schema - No default schema is specified in the URL,
	no libraries are specified, and sql naming.  Should use the schema with
	the same name as the userId as the default.
	**/
	public void Var022()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("naming", "sql");
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}


	/**
	default schema - No default schema is specified in the URL,
	no libraries are specified, and system naming.  Should have no default.
	**/
	public void Var023()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("naming", "system");
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (""));
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}


	/**
	default schema - Specifies an invalid default schema in the URL.
	Should throw an exception.
	**/
	public void Var024()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = "BAD_SCHEMA";
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			/* Connection c = */ d.connect (o, p, schema);
                        succeeded();                   //Toolbox allows underscores in schema names         //@K1A
                        //@K1D failed("Did not throw expected Exception");
		}
		catch (Exception e) {
			//@K1D assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                        failed(e, "Unexpected Exception");                                                  //@K1A
		}
	}



	/**
	default schema - Specifies an default schema that is longer than
	10 characters in the URL.  Should throw an exception.
	**/
	public void Var025()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
		    //@128len
			String schema = JDConnectionTest.SCHEMAS_LEN128 + "EXTRA";
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);
			failed ("Did not throw exception for "+c);
		}
		catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	}



	/**
	default schema - Specifies a default schema in the URL.
	Verifies that this schema is now the default schema.
	**/
	public void Var026()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = JDConnectionTest.COLLECTION;
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}


	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces),
	and sql naming.
	Should use the first library as the default.
	**/
	public void Var027()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", JDConnectionTest.COLLECTION + " " + COLLECTION2);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by commas),
	and sql naming.
	Should use the first library as the default.
	**/
	public void Var028()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", COLLECTION2 + "," + JDConnectionTest.COLLECTION);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (COLLECTION2));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces),
	and system naming. Should have no default.
   @P9951698 default added back in v5r2
	@P9A03018 changed so works like v5r1 so there is no behavior change.
	**/
	public void Var029()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("naming", "system");
			p.put("libraries", JDConnectionTest.COLLECTION + " " + COLLECTION2);

			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (""));
			// assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - Specifies a default schema and some libraries.
	Verify that the default schema comes from the URL.
	**/
	public void Var030()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = JDConnectionTest.COLLECTION;
			Properties p = new Properties();
			p.put("libraries", "," + COLLECTION2);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (JDConnectionTest.COLLECTION));
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - No default schema is specified in the URL,
	no libraries are specified.  The schema with the same name
	as the userId should be in the library list.
	**/
	public void Var031()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}else  if (isToolboxDriver())
		{
		    notApplicable("TODO: figure out good way to check library list");//checklibraryList() was hardcoded to return true!!
            return;
		}
    StringBuffer sb = new StringBuffer();
		try {
			String schema = null;
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean success = checkLibraryList (c, userId_,sb);
			c.close ();
			assertCondition (success);
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	libraries - Specifies a default schema in the URL.
	Verifies that this schema is in the library list.
	**/
	public void Var032()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {     StringBuffer sb = new StringBuffer();

			String schema = JDConnectionTest.COLLECTION;
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean success = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			c.close ();
			assertCondition (success);
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	libraries - No default schema is specified in
	the URL, but libraries are specified (delimited by spaces).
	Those libraries should be in the library list.
	**/
	public void Var033()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", JDConnectionTest.COLLECTION + " " + COLLECTION2);
			p.put("naming", "system"); 
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			c.close ();
			assertCondition (successA && successB);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - No default schema is specified in
	the URL, but libraries are specified (delimited by commas).
	Those libraries should be in the library list.
	**/
	public void Var034()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", COLLECTION2 + "," + JDConnectionTest.COLLECTION);
      p.put("naming", "system"); 
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			c.close ();
			assertCondition (successA && successB);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specifies a default schema and some libraries.
	Verify that the default schema and the libraries are
	in the library list.
	**/
	public void Var035()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = JDConnectionTest.COLLECTION;
			Properties p = new Properties();
			p.put("libraries", "," + COLLECTION2);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			c.close ();
			assertCondition (successA && successB);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specifies a bad library in the library list.
	Should throw an exception.


	In v5r2 the Toolbox driver was changed to post a warning if a library
	does not exist or is already in the list.
	**/
	public void Var036()
	{
		if ((!isToolboxDriver()))
		{
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", "BAD_LIB"); //@128sch tb does not set liblist if default schema is longer than 10
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);
			if (verifyWarning(c.getWarnings(), 1301))
				succeeded();
			else
				failed ("Did not post correct warning.");

			c.close ();
		}
		catch (Exception e)
		{
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	}



	/**
	libraries - Specify library list with *LIBL only.
	**/
	public void Var037()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", "*LIBL");
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA);
		}
		catch (Exception e)
		{
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with 2 libraries, but not *LIBL.
	**/
	public void Var038()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", JDConnectionTest.COLLECTION + "," + COLLECTION2);
			p.put("naming", "system");
			
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = !checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL before 2 other libraries.
	**/
	public void Var039()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", "*LIBL," + JDConnectionTest.COLLECTION + "," + COLLECTION2);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL between 2 other libraries.
	**/
	public void Var040()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", JDConnectionTest.COLLECTION + ",*LIBL," + COLLECTION2);
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC);
		}
		catch (Exception e) {
			failed(e,"Unexpected Exception");
		}
	}



	/**
	libraries - Specify library list with *LIBL after 2 other libraries.
	**/
	public void Var041()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
    StringBuffer sb = new StringBuffer();

		try {
			String schema = null;
			Properties p = new Properties();
			p.put("libraries", JDConnectionTest.COLLECTION + "," + COLLECTION2 + ",*LIBL");
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
			boolean successB = checkLibraryList (c, COLLECTION2,sb);
			boolean successC = checkLibraryList (c, "QGPL",sb);
			c.close ();
			assertCondition (successA && successB && successC);
		}
		catch (Exception e)
		{
			failed(e,"Unexpected Exception");
		}
	}



	/**
	default schema - No default schema is specified in the URL,
	no libraries are specified, and sql naming (the default).  Should use the schema with
	the same name as the userId as the default.
	**/
	public void Var042()
	{
	    if (checkNotGroupTest())  { 
		if ((!isToolboxDriver())) {
		    notApplicable("TOOLBOX ONLY VARIATION");
		    return;
		}



		try {
		    AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		    SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
		    Connection c = d.connect (o);

		    String defaultSchema = getDefaultSchema (c);
		    c.close ();
		    assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e)
		{
		    failed(e,"SSL not configured.\n"+
			   "If you get a certificate chaining error, you need "+
			   "to get the CA certificate and install it using "+
			   "keytool -import -keystore /QOpenSys/QIBM/ProdData/JavaVM/jdk60/32bit/jre/lib/security/cacerts -file /tmp/import.ca2 -storepass changeit");
		}
	    }


	}
	/**
libraries - Specify library list with *LIBL before 2 other libraries.
**/
	public void Var043()
	{
	    if (checkNotGroupTest()) { 
		if ((!isToolboxDriver())) {
		    notApplicable("TOOLBOX ONLY VARIATION");
		    return;
		}
		StringBuffer sb = new StringBuffer();

		try {
		    String schema = null;
		    Properties p = new Properties();
		    p.put("libraries", "*LIBL," + JDConnectionTest.COLLECTION + "," + COLLECTION2);
		    AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
		    SecureAS400 o = new SecureAS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
		    Connection c = d.connect (o, p, schema);

		    boolean successA = checkLibraryList (c, JDConnectionTest.COLLECTION,sb);
		    boolean successB = checkLibraryList (c, COLLECTION2,sb);
		    boolean successC = checkLibraryList (c, "QGPL",sb);
		    c.close ();
		    assertCondition (successA && successB && successC);
		}
		catch (Exception e)
		{
		    if (exceptionIs(e, "java.lang.NoClassDefFoundError"))
			notApplicable("SSL not configured");
		    else
			failed(e,"SSL not configured.");
		}
	    }

	}

	/*
	default schema - No default schema is specified in the URL,
	no libraries are specified, and sql naming (default).  Should use the schema with
	the same name as the userId as the default.
	**/
	public void Var044()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try {
			String schema = null;
			Properties p = new Properties();
			AS400JDBCDriver d = new AS400JDBCDriver();
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
			AS400 o = new AS400(systemObject_.getSystemName(), userId_, charPassword);
   PasswordVault.clearPassword(charPassword);
			Connection c = d.connect (o, p, schema);

			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e) {
			failed(e, "Unexpected exception");
		}
	}



	/**
	libraries - Specifies a library already in the library list.
	Should post a warning.

	In v5r2 the Toolbox driver was changed to post a warning if a library
	does not exist or is already in the list.
	**/
	public void Var045()
	{
		try
		{
			Connection c = testDriver_.getConnection (baseURL_
													  + ";naming=system;libraries=QSYS,QGPL", userId_, encryptedPassword_);
			if (getDriver() == JDTestDriver.DRIVER_NATIVE)
			{
                                /* The native driver doesnt care about warnings */
				c.close();
                                succeeded();
			}
			else
			{
				if (verifyWarning(c.getWarnings(), 1301))
					succeeded();
				else
					failed ("Did not post correct warning, is QGPL in user library list?");
			}
			c.close();

		}
		catch (Exception e)
		{
			failed(e, "Unexpected Exception");
		}
	}


	/*
	default schema - No default schema is specified in the URL,
	libary list starts with common.  Result is no default schema.
	(sql naming (default) so should use the schema with
	the same name as the userId as the default.)
	**/
	public void Var046()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{
			Connection c = testDriver_.getConnection (baseURL_
									  + ";naming=sql;libraries=," + JDConnectionTest.COLLECTION
   								  + " " + COLLECTION2, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}
	}

	/*
	default schema - No default schema is specified in the URL,
	libary list starts with spaces then a common.  Result is no default schema.
	(sql naming (default) so should use the schema with
	the same name as the userId as the default.)
	**/
	public void Var047()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{
			Connection c = testDriver_.getConnection (baseURL_
									  + ";naming=sql;libraries=   ," + JDConnectionTest.COLLECTION
   								  + " " + COLLECTION2, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (userId_));
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}
	}



	/*
	default schema - No default schema is specified in the URL,
	libary list starts with common.  Result is no default schema.
   System naming so no default.
	**/
	public void Var048()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{
		    String thisURL =baseURL_
		      + ";naming=system;libraries=," + JDConnectionTest.COLLECTION
		      + " " + COLLECTION2;
			Connection c = testDriver_.getConnection (thisURL, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (""), "default schema="+defaultSchema+" sb '' URL="+thisURL);
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}
	}

	/*
	default schema - No default schema is specified in the URL,
	libary list starts with spaces then a common.  Result is no default schema.
	System naming.
	**/
	public void Var049()
	{
		if ((!isToolboxDriver())) {
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{
		    String thisURL=baseURL_
		      + ";naming=system;libraries=   ," + JDConnectionTest.COLLECTION
		      + " " + COLLECTION2;
		    Connection c = testDriver_.getConnection (thisURL, userId_, encryptedPassword_);
			String defaultSchema = getDefaultSchema (c);
			c.close ();
			assertCondition (defaultSchema.equalsIgnoreCase (""), "defaultSchema="+defaultSchema+" sb '' URL="+thisURL);
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}
	}


	/*
	Make sure default library is added to library list.  Starting with v5r1 the server
	no longer automatically add the default library to the library list.  This broke
	many ODBC customers and Dave Dilling says it will only be time before we get APARs.
	So, code was added to add the default library to the library list.  We test this by
	loading a stored procedure.  SPs are found by looking in the libary list, not by
	using the default lib.  So, put a SP in the default lib and call it using an
	unqualified name.  If the default lib is not in the library list we will get an
	error (SP not found).  If it is in the library list we will get a result set back.
	**/
	public void Var050()
	{
	   Connection setupConnection = null;
	   Statement  setupStatement  = null;

	   Connection connection = null;
	   //  Statement  statement  = null;
	   ResultSet  rs         = null;
	   CallableStatement callableStatement  = null;

		String collectionWithSP = "JDCONNLIBL";
		String table = JDConnectionTest.COLLECTION + ".JDCONNTABLE";
		String unqualifiedProc = "JDCONNSP";
		String proc = collectionWithSP + "." + unqualifiedProc;
		String SP = "CREATE PROCEDURE " + proc + " (parm1 VARCHAR(20)) "
                   + "RESULT SET 1 LANGUAGE SQL "
                   + "BEGIN  "
                   + "   DECLARE C3 CURSOR FOR SELECT * FROM " + table + "  ; "
                   + "   OPEN C3 ; "
                   + "   SET RESULT SETS CURSOR C3 ; "
                   + "END  ";


		String setupURL = baseURL_ + ";libraries=QGPL";
		String testURL = baseURL_ + "/" + collectionWithSP + ";libraries=QGPL";

		if ((!isToolboxDriver()))
		{
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{

		   try
		   {
            setupConnection = testDriver_.getConnection (setupURL, userId_, encryptedPassword_);
            setupStatement  = setupConnection.createStatement();
            try{
                JDSetupCollection.create(setupConnection, collectionWithSP, output_);
            }catch(Exception e){}//ignore warning
            try { setupStatement.executeUpdate("CREATE TABLE " + table + " (p1_int integer)" ); } catch (Exception e) { }
            try { setupStatement.executeUpdate("INSERT INTO " + table + " VALUES (98765)" ); } catch (Exception e) { }
            try { setupStatement.executeUpdate( SP ); } catch (Exception e) { output_.println(e); }
		   }
		   catch (Exception e)
		   {
		      output_.println("warning, setup failed " + e.getMessage());
		   }

			connection = testDriver_.getConnection (testURL, userId_, encryptedPassword_);
         callableStatement = connection.prepareCall( "{CALL " + unqualifiedProc + "(?) }" );
         callableStatement.setString(1, "Hi Mom");
         rs = callableStatement.executeQuery();
         rs.next();
      // output_.println(rs.getString(1));
         succeeded();
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}

      if (rs != null)
         try { rs.close(); } catch (Exception e) { }
      if (callableStatement != null)
         try { callableStatement.close(); } catch (Exception e) { }
//      if (statement != null)
//         try { statement.close(); } catch (Exception e) { }
      if (connection != null)
         try { connection.close(); } catch (Exception e) { }

      if (setupStatement != null)
      {
         try { setupStatement.executeUpdate("DROP TABLE " + table); } catch (Exception e) { }
         try { setupStatement.executeUpdate("DROP PROCEDURE " + proc); } catch (Exception e) { }
      // try { setupStatement.executeUpdate("DROP COLLECTION " + collectionWithSP); } catch (Exception e) { }
         try { setupStatement.close();} catch (Exception e) { }
      }

      if (setupConnection != null)
         try { setupConnection.close(); } catch (Exception e) { }

	}



	/*
	Make sure default library is added to library list.  Starting with v5r1 the server
	no longer automatically add the default library to the library list.  This broke
	many ODBC customers and Dave Dilling says it will only be time before we get APARs.
	So, code was added to add the default library to the library list.  We test this by
	loading a stored procedure.  SPs are found by looking in the libary list, not by
	using the default lib.  So, put a SP in the default lib and call it using an
	unqualified name.  If the default lib is not in the library list we will get an
	error (SP not found).  If it is in the library list we will get a result set back.
	**/
	public void Var051()
	{
	   Connection setupConnection = null;
	   Statement  setupStatement  = null;

	   Connection connection = null;
//	   Statement  statement  = null;
	   ResultSet  rs         = null;
	   CallableStatement callableStatement  = null;

		String collectionWithSP = "JDCONNLIBL";
		String table = JDConnectionTest.COLLECTION + "/JDCONNTABLE";
		String unqualifiedProc = "JDCONNSP";
		String proc = collectionWithSP + "/" + unqualifiedProc;
		String SP = "CREATE PROCEDURE " + proc + " (parm1 VARCHAR(20)) "
                   + "RESULT SET 1 LANGUAGE SQL "
                   + "BEGIN  "
                   + "   DECLARE C3 CURSOR FOR SELECT * FROM " + table + "  ; "
                   + "   OPEN C3 ; "
                   + "   SET RESULT SETS CURSOR C3 ; "
                   + "END  ";


		String setupURL = baseURL_ + ";libraries=QGPL;naming=system;";
		String testURL = baseURL_ + "/" + collectionWithSP + ";libraries=QGPL;naming=system";

		if ((!isToolboxDriver()))
		{
			notApplicable("TOOLBOX ONLY VARIATION");
			return;
		}
		try
		{

		   try
		   {
            setupConnection = testDriver_.getConnection (setupURL, userId_, encryptedPassword_);
            setupStatement  = setupConnection.createStatement();
            JDSetupCollection.create(setupConnection,  collectionWithSP, output_);
            try { setupStatement.executeUpdate("CREATE TABLE " + table + " (p1_int integer)" ); } catch (Exception e) { }
            try { setupStatement.executeUpdate("INSERT INTO " + table + " VALUES (98765)" ); } catch (Exception e) { }
            try { setupStatement.executeUpdate( SP ); } catch (Exception e) { }
		   }
		   catch (Exception e)
		   {
		      output_.println("warning, setup failed " + e.getMessage());
		   }

			connection = testDriver_.getConnection (testURL, userId_, encryptedPassword_);
         callableStatement = connection.prepareCall( "{CALL " + unqualifiedProc + "(?) }" );
         callableStatement.setString(1, "Hi Mom");
         rs = callableStatement.executeQuery();
         rs.next();
     //  output_.println(rs.getString(1));
         succeeded();
		}
		catch (Exception e)
		{
			failed(e, "Unexpected exception");
		}

      if (rs != null)
         try { rs.close(); } catch (Exception e) { }
      if (callableStatement != null)
         try { callableStatement.close(); } catch (Exception e) { }
//      if (statement != null)
//         try { statement.close(); } catch (Exception e) { }
      if (connection != null)
         try { connection.close(); } catch (Exception e) { }

      if (setupStatement != null)
      {
         try { setupStatement.executeUpdate("DROP TABLE " + table); } catch (Exception e) { }
         try { setupStatement.executeUpdate("DROP PROCEDURE " + proc); } catch (Exception e) { }
      // try { setupStatement.executeUpdate("DROP COLLECTION " + collectionWithSP); } catch (Exception e) { }
         try { setupStatement.close();} catch (Exception e) { }
      }

      if (setupConnection != null)
         try { setupConnection.close(); } catch (Exception e) { }

	}

//@C1A
//Test to see if qaqqinlib connection property works
//If we set the QUERY_TIME_LIMIT to 1 in a qaqqini file, then trying to execute a long query should throw an exception.
//Note:  you need SECOFR authority to issue a change query library command
       public void Var052()
       {
           if ( (isToolboxDriver()) ||
                ( getDriver() == JDTestDriver.DRIVER_NATIVE ))
           {
               boolean setupFailed = false;
               int setup = 0;
               try
               {
                 if (getDriver() == JDTestDriver.DRIVER_NATIVE ){
                   Connection c = testDriver_.getConnection (baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
                   Statement s2 = c.createStatement();
		   String command = null;

		   try {
		       command = "CALL QSYS.QCMDEXC("+
					"'CRTLIB "+iniLibrary+"                                     ',"+
					"0000000040.00000 )";
		       s2.executeUpdate(command);
		   } catch (Exception e) {
		       output_.println("Exception creating library using "+command);
		       e.printStackTrace(output_); 
		   }
		   try {
		       command = "CALL QSYS.QCMDEXC('CRTDUPOBJ OBJ(QAQQINI) FROMLIB(QSYS) OBJTYPE(*FILE) TOLIB("+iniLibrary+") DATA(*YES)    ',0000000080.00000 )"; 
		       s2.executeUpdate(command);
		   } catch (Exception e) {
		       output_.println("Exception creating ini using "+command);
		       e.printStackTrace(output_); 

		   }
		   c.commit();
		   c.close(); 
                 } else {
		   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
                   AS400 s = new AS400(systemObject_.getSystemName(), pwrSysUserID_, charPassword);
		   PasswordVault.clearPassword(charPassword);
                   CommandCall cc = new CommandCall(s);
                   cc.run("QSYS/CRTLIB "+iniLibrary);
                   cc.run("QSYS/CRTDUPOBJ OBJ(QAQQINI) FROMLIB(QSYS) OBJTYPE(*FILE) TOLIB("+iniLibrary+") DATA(*YES)");
                 }
                   //AS400Message[] messageList = cc.getMessageList();
                   //for (int i = 0; i < messageList.length; ++i)
                   //{
                       // Show each message.
                   //    output_.println(messageList[i].getText());
                       // Load additional message information.
                   //    messageList[i].load();
                       //Show help text.
                   //    output_.println(messageList[i].getHelp());
                   //}
                   Connection c = testDriver_.getConnection (baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);
                   Statement s2 = c.createStatement();
                   setup = s2.executeUpdate("DELETE FROM "+iniLibrary+".QAQQINI WHERE QQPARM='QUERY_TIME_LIMIT'");
                   setup += s2.executeUpdate("INSERT INTO "+iniLibrary+".QAQQINI VALUES('QUERY_TIME_LIMIT', '1', default)");
                   if(setup != 2)
                       setupFailed = true;
                   s2.close();
                   c.close();
               }
               catch(Exception e){
                   e.printStackTrace();
                   setupFailed = true;
               }
      Connection conn = null;
      TimeoutThread timeoutThread = null; 
      try {
        if (setupFailed == true)
          failed("Variation setup failed.");
        else {
          conn = testDriver_.getConnection(
              baseURL_ + ";qaqqinilib=" + iniLibrary, pwrSysUserID_, pwrSysEncryptedPassword_);
          Statement s1 = conn.createStatement();

          // Make sure the job doesn't use the system reply list. Otherwise an
          // entry for
          // CPA4269 with I will cause this test to fail.
          s1.executeUpdate(
              "CALL QSYS.QCMDEXC('CHGJOB INQMSGRPY(*RQD)                ',0000000030.00000 )");

          ResultSet rs;
          Object [] args = new Object[1]; 
          args[0] = s1; 
          timeoutThread = new TimeoutThread(this, 10, args);
          timeoutThread.start(); 
          rs = s1.executeQuery(
              "SELECT sum(a.column_size), sum(length(b.table_name)) FROM SYSIBM.SQLCOLUMNS a, SYSIBM.SQLTABLES b where a.table_name = b.table_name and a.column_size > 1");

          s1.executeUpdate(
              "CALL QSYS.QCMDEXC(' DSPJOBLOG OUTPUT(*PRINT)             ',0000000030.00000 )");
          timeoutThread.abort(); 
          timeoutThread.join(); 
          failed(
              "Didn't throw SQLException after setting qaqqinilib property for query time limit=1:  Got result set"
                  + rs);
        }
      } catch (Exception e) {
        if (timeoutThread != null) { 
          timeoutThread.abort(); 
          try {
            timeoutThread.join();
          } catch (InterruptedException e1) {
            e1.printStackTrace(output_);
          } 
        }
        if ((timeoutThread != null) && (timeoutThread.getCallbackFired()))  {
           failed("QAQQINI Timeout did not work and manual timeout was triggered"); 
        } else { 
           assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        }
        
      } finally {
        try {
          if (conn != null) {
            conn.close();
          }
          if (getDriver() == JDTestDriver.DRIVER_NATIVE
              ) {
            Connection c = testDriver_.getConnection(baseURL_, pwrSysUserID_,
                pwrSysEncryptedPassword_);
            Statement s2 = c.createStatement();
                       s2.executeUpdate("CALL QSYS.QCMDEXC("+
                           "'CHGJOB INQMSGRPY(*SYSRPYL)                                   ',"+
                       "0000000040.00000 )");

                       s2.executeUpdate("CALL QSYS.QCMDEXC("+
                           "'DLTLIB "+iniLibrary+"                                     ',"+
                       "0000000040.00000 )");
		       c.close(); 
                     } else {

		       char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
			 AS400 s = new AS400(systemObject_.getSystemName(), pwrSysUserID_, charPassword);
		       PasswordVault.clearPassword(charPassword);

                       CommandCall cc = new CommandCall(s);
		       deleteLibrary(cc, iniLibrary);
                     }
                   }
                   catch(Exception e)
                   {
		       output_.println("Warning: Exception during testcase cleanup");
		       e.printStackTrace(output_); 
                   }
               }
	   }
       }






       //@128len
       /**
       default schema - Specifies a default schema in the URL.
       Verifies that this schema is now the default schema.
       **/
       public void Var053()
       {
            try {

               Connection c = testDriver_.getConnection (baseURL_ + "/"
                                                         + JDConnectionTest.SCHEMAS_LEN128, userId_, encryptedPassword_);
               String defaultSchema = getDefaultSchema (c);
               c.close ();
               assertCondition (compareLongSchemas(JDConnectionTest.SCHEMAS_LEN128, defaultSchema), "future host support required."); //note only returned as 10 chars
           }
           catch (Exception e) {
               failed(e, "Unexpected exception");
           }
       }

       //returns true if first 5 chars are same
       //hostserver returns something like JDTES00001 in message back for long schema like JDTESTCOTHISISAREALLY
       boolean compareLongSchemas(String longSch, String shortSch)
       {
           if( shortSch.length()>= 5 && longSch.indexOf(shortSch.substring(0,5)) == 0)
               return true;
           else
               return false;

       }

     //@128len
       /**
       default schema - No default schema is specified in
       the URL, but libraries are specified (delimited by spaces),
       and sql naming.
       Should use the first library as the default.
       **/
       public void Var054()
       {
            try
           {
               Connection c = testDriver_.getConnection (baseURL_
                                                         + ";naming=sql;libraries=" + JDConnectionTest.SCHEMAS_LEN128
                                                         + " " + COLLECTION2, userId_, encryptedPassword_);
               String defaultSchema = getDefaultSchema (c);
               c.close ();
               assertCondition (compareLongSchemas(JDConnectionTest.SCHEMAS_LEN128, defaultSchema), "future host support required."); //note only returned as 10 chars
           }
           catch (Exception e) {
               failed(e,"Unexpected Exception");
           }
       }


     //@128len
       /**
       default schema - No default schema is specified in
       the URL, but libraries are specified (delimited by commas),
       and sql naming.
       Should use the first library as the default.
       **/
       public void Var055()
       {
           try {
               Connection c = testDriver_.getConnection (baseURL_
                                                         + ";libraries=" + JDConnectionTest.SCHEMAS_LEN128
                                                         + "," + JDConnectionTest.COLLECTION, userId_, encryptedPassword_);
               String defaultSchema = getDefaultSchema (c);
               c.close ();
               assertCondition (compareLongSchemas(defaultSchema, JDConnectionTest.SCHEMAS_LEN128), "future host support required."); //note only returned as 10 chars
           }
           catch (Exception e) {
               failed(e,"Unexpected Exception");
           }
       }


     //@128len
       /**
       default schema - No default schema is specified in
       the URL, but libraries are specified (delimited by spaces),
       and system naming. Should have no default.
       @P9951698 default added back in v5r2
       @P9A03018 changed so works like v5r1 so there is no behavior change.
       **/
       public void Var056()
       {
           try {
               Connection c = testDriver_.getConnection (baseURL_
                                                         + ";naming=system;libraries=" + JDConnectionTest.SCHEMAS_LEN128
                                                         + " " + COLLECTION2, userId_, encryptedPassword_);
               String defaultSchema = getDefaultSchema (c);
               c.close ();
               boolean condition = defaultSchema.equalsIgnoreCase ("");
               if (!condition) {
                   output_.println("defaultSchema is "+defaultSchema);
               }

               assertCondition (condition, "future host support required.");

           }
           catch (Exception e) {
               failed(e,"Unexpected Exception");
           }
       }


     //@128len
       /**
       default schema - Specifies a default schema and some libraries.
       Verify that the default schema comes from the URL.
       **/
       public void Var057()
       {
           try {
               Connection c = testDriver_.getConnection (baseURL_
                                                         + "/" + JDConnectionTest.SCHEMAS_LEN128
                                                         + ";libraries="
                                                         + "," + COLLECTION2, userId_, encryptedPassword_);
               String defaultSchema = getDefaultSchema (c);
               c.close ();
               assertCondition (compareLongSchemas(defaultSchema, JDConnectionTest.SCHEMAS_LEN128), "future host support required."); //note only returned as 10 chars
           }
           catch (Exception e) {
               failed(e,"Unexpected Exception");
           }
       }





     //@128len
       /**
       libraries - Specifies a default schema in the URL.
       Verifies that this schema is in the library list for system naming
       **/
       public void Var058()
       {
         StringBuffer sb = new StringBuffer();

           try {
               Connection c = testDriver_.getConnection (baseURL_ + "/"
                                                         + JDConnectionTest.SCHEMAS_LEN128+";naming=system", userId_, encryptedPassword_);
               String shortName = getShortName(c, JDConnectionTest.SCHEMAS_LEN128 );  
         
               String defaultSchema = getDefaultSchema(c); 
               sb.append("\ndefaultSchema is '"+defaultSchema+"'\n"); 
               boolean success = checkLibraryList (c, shortName,sb);
               c.close ();
               assertCondition (success, "Longer schema name not found in library list - future host support required."+sb);
           }
           catch (Exception e) {
               failed(e, "Unexpected exception");
           }
       }



	private String getShortName(Connection c, String schema) throws SQLException {
	  String shortName = null; 
	  PreparedStatement ps  = c.prepareStatement("Select SYSTEM_SCHEMA_NAME from QSYS2.SYSSCHEMAS WHERE SCHEMA_NAME=?"); 
	  ps.setString(1, schema); 
	  ResultSet rs = ps.executeQuery();
	  if (rs.next()) {
	    shortName = rs.getString(1); 
	  }
	  
	  return shortName; 
	}



  boolean verifyWarning(SQLWarning w, int expectedErrorCode)
	{
		if (w == null)
			return false;

		while (w != null)
		{
			if (w.getErrorCode() == expectedErrorCode)
				return true;
			else
				w = w.getNextWarning();
		}

		return false;
	}



  public void doCallback(Object[] args) {
    try { 
     Statement s = (Statement) args[0]; 
     s.cancel(); 
    } catch (Exception e) { 
      synchronized (output_)  {
        output_.println("doCallback failed with "); 
        e.printStackTrace(output_); 
      }
    }
  }
}












