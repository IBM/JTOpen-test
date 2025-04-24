///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementGetGeneratedKeys2.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementGetGeneratedKeys2.java
//
// Classes:      JDStatementGetGeneratedKeys2
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDStatementGetGeneratedKeys.  This tests the following methods
of the JDBC Statement class:

<ul>
<li>execute(...., RETURN_GENERATED_KEYS)
<li>executeUpdate(...., RETURN_GENERATED_KEYS)
<li>getGeneratedKeys()</li>
</ul>

The following methods of the JDBC Connection class are also tested
<ul>
<li>prepare(...., RETURN_GENERATED_KEYS) 
</ul>
The following methods of the JDBC PreparedStatement class are also tested
<ul>
<li>execute(..., RETURN_GENERATED_KEYS)
<li>exectueUpdate(..., RETURN_GENERATED_KEYS)
</ul> 
**/
public class JDStatementGetGeneratedKeys2
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementGetGeneratedKeys2";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }


    // Private data.

    private static  String tableGenId_        = JDStatementTest.COLLECTION + ".JDGENK2A";
    // private static  String systemTableGenId_  = JDStatementTest.COLLECTION + "/JDGENK2A";
    private static  String tableRowId_        = JDStatementTest.COLLECTION + ".JDGENK2B";
    // private static  String systemTableRowId_  = JDStatementTest.COLLECTION + "/JDGENK2B";
    private static  String tableNoId_         = JDStatementTest.COLLECTION + ".JDGENK2C";
    // private static  String systemTableNoId_   = JDStatementTest.COLLECTION + "/JDGENK2C";


    private Connection      connectionReuseObjects_;
    private Connection      connectionSysNaming_;

    
    private static String added20100910 = " -- Added by native 9/10/2010 to test CPS 795G9R"; 

/**
Constructor.
**/
    public JDStatementGetGeneratedKeys2 (AS400 systemObject,
                                        Hashtable<String,Vector<String>> namesAndVars,
                                        int runMode,
                                        FileOutputStream fileOutputStream,
                                        
                                        String password)
    {
        super (systemObject, "JDStatementGetGeneratedKeys2",
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

	//
	// reset table names to pick up new collection is specified later
	//

	tableGenId_        = JDStatementTest.COLLECTION + ".JDGENK2A";
	// systemTableGenId_  = JDStatementTest.COLLECTION + "/JDGENK2A";
	tableRowId_        = JDStatementTest.COLLECTION + ".JDGENK2B";
	// systemTableRowId_  = JDStatementTest.COLLECTION + "/JDGENK2B";
	tableNoId_         = JDStatementTest.COLLECTION + ".JDGENK2C";
	// systemTableNoId_   = JDStatementTest.COLLECTION + "/JDGENK2C";

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC doesn't have these properties

	    connection_ = testDriver_.getConnection (baseURL_ , 
						     userId_, encryptedPassword_);

	    connectionReuseObjects_ = testDriver_.getConnection (baseURL_ ,
						      userId_, encryptedPassword_);


	} else { 

	    connection_ = testDriver_.getConnection (baseURL_ + ";errors=full", 
						     userId_, encryptedPassword_);

	    connectionReuseObjects_ = testDriver_.getConnection (baseURL_ + ";errors=full;reuse objects=false", 
						      userId_, encryptedPassword_);
	}

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          // jcc doesn't support system naming
          connectionSysNaming_ = testDriver_.getConnection (baseURL_ , 
              userId_, encryptedPassword_);
        } else { 
        connectionSysNaming_ = testDriver_.getConnection (baseURL_ + ";errors=full;naming=system", 
                                                 userId_, encryptedPassword_);
        }


        Statement s = connection_.createStatement ();

	initTable(s,   tableGenId_, 
                          " (NAME VARCHAR(10), GENID INT GENERATED ALWAYS AS IDENTITY)");



        if (JDTestDriver.isLUW()) {
	    // LUW requires AS IDENTITY .. Also ROWID not working
	    initTable(s,  tableRowId_,
				      " (NAME VARCHAR(10), GENERATEID INT GENERATED ALWAYS AS IDENTITY)");
	} else { 
	    initTable(s,  tableRowId_
                         , " (NAME VARCHAR(10), GENERATEID ROWID GENERATED ALWAYS)");
	}

        s.executeUpdate ("INSERT INTO " + tableRowId_
                         + " (NAME) VALUES ('terry')");



	
        initTable(s,  tableNoId_
                         , " (NAME VARCHAR(10), ID INT )");


             JDSetupProcedure.create (systemObject_,connection_,
                                     JDSetupProcedure.STP_RS0, supportedFeatures_, collection_);
 

	if (getDriver() != JDTestDriver.DRIVER_JCC) {
	    connection_.commit(); // for xa
	}

        s.close ();   
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        Statement s = connection_.createStatement ();
        s.executeUpdate ("DROP TABLE " + tableGenId_);
        s.executeUpdate ("DROP TABLE " + tableRowId_);
        s.executeUpdate ("DROP TABLE " + tableNoId_);
        s.close ();


	System.gc(); 
    }


    public void cleanupConnections() throws Exception  {
      if (getDriver() != JDTestDriver.DRIVER_JCC) {
        connection_.commit(); // for xa
    }
          connection_.close ();

          connectionReuseObjects_.close ();

          connectionSysNaming_.close ();
      
    }

/**
getGeneratedKeys() - Insert into table without autogeneration and then run delete statement requesting autogenerated keys.
**/
    public void Var001()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableNoId_ +" WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null)  value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		  if (rs2 != null) value2 =  rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with gen id and then run delete statement requesting autogenerated keys.
**/
    public void Var002()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableGenId_ +" WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==key1 && value2==key2,
				"Expected value1="+key1+" for ("+value1+") "+
				"Expected value2="+key2+" for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run delete statement requesting autogenerated keys.
**/
    public void Var003()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		byte[] key2 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.next();
		key2 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableRowId_ +" WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setBytes(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		      if (rs1 != null) value1 = rs1.getBytes(1); 
		} 
		ps.setBytes(1,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }



/**
getGeneratedKeys() - Insert into table without autogeneration and then run delete statement requesting autogenerated keys but delete not found for second delete. 
**/
    public void Var004()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableNoId_ +" WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with gen id and then run delete statement requesting autogenerated keys  but delete not found for second delete. 
**/
    public void Var005()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableGenId_ +" WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==key1 && value2==0,
				"Expected value1="+key1+" for ("+value1+") "+
				"key2="+key2+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run delete statement requesting autogenerated keys  but delete not found for second delete. 
**/
    public void Var006()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableRowId_ +" WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setBytes(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist={(byte) 0xef}; 
		ps.setBytes(1,keyNotExist);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}


    }



/**
getGeneratedKeys() - Insert into table without autogeneration and then run delete statement requesting autogenerated keys but delete not found for both deletes. 
**/
    public void Var007()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableNoId_ +" WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run delete statement requesting autogenerated keys  but delete not found for both deletes. 
**/
    public void Var008()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");




		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableGenId_ +" WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		} 
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run delete statement requesting autogenerated keys  but delete not found for both deletes. 
**/
    public void Var009()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");


		PreparedStatement ps = connection_.prepareStatement("DELETE FROM " + tableRowId_ +" WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		byte[] keyNotExist1={(byte) 0xea}; 
		ps.setBytes(1,keyNotExist1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist2={(byte) 0xef}; 
		ps.setBytes(1,keyNotExist2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }

    public void Var010() {
	notApplicable("Placeholder"); 
    }


/**
getGeneratedKeys() - Insert into table without autogeneration and then run update statement requesting autogenerated keys.
**/
    public void Var011()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableNoId_ +" SET NAME= ? WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"Ann"); 
		ps.setInt(2,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1,"John"); 
		ps.setInt(2,2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run update statement requesting autogenerated keys.
**/
    public void Var012()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableGenId_ +" SET NAME=? WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setInt(2,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1,"Ann"); 
		ps.setInt(2,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close();
	    /* No result set available for update */ 
		key1 = 0;
		key2 = 0;
		assertCondition(value1==key1 && value2==key2,
				"Expected value1="+key1+" for ("+value1+") "+
				"Expected value2="+key2+" for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }

/**
getGeneratedKeys() - Insert into table with rowid and then run update statement requesting autogenerated keys.
**/
    public void Var013()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		byte[] key2 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.next();
		key2 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableRowId_ +" SET NAME=? WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"Ann"); 
		ps.setBytes(2,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		ps.setString(1,"John"); 
		ps.setBytes(2,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }



/**
getGeneratedKeys() - Insert into table without autogeneration and then run update statement requesting autogenerated keys but update not found for second update. 
**/
    public void Var014()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableNoId_ +" SET NAME=? WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setInt(2,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1,"Ann"); 
		ps.setInt(2,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run update statement requesting autogenerated keys  but update not found for second update. 
**/
    public void Var015()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		// int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		// key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableGenId_ +" SET NAME=? WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setInt(2,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1,"Ann"); 
		ps.setInt(2,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close();

	    /* No result set available for update */ 
		key1 = 0;
		// key2 = 0;

		assertCondition(value1==key1 && value2==0,
				"Expected value1="+key1+" for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run update statement requesting autogenerated keys  but update not found for second update. 
**/
    public void Var016()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableRowId_ +" SET NAME=? WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setBytes(2,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist={(byte) 0xef};
		ps.setString(1,"Ann"); 
		ps.setBytes(2,keyNotExist);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }




/**
getGeneratedKeys() - Insert into table without autogeneration and then run update statement requesting autogenerated keys but update not found for both updates. 
**/
    public void Var017()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableNoId_ +" SET NAME = ? WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setInt(2,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1,"Ann"); 
		ps.setInt(2,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }
/**
getGeneratedKeys() - Insert into table with gen id and then run update statement requesting autogenerated keys  but update not found for both updates. 
**/
    public void Var018()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");




		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableGenId_ +" SET NAME=? WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setString(1,"John"); 
		ps.setInt(2,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setString(1, "Ann"); 
		ps.setInt(2,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run update statement requesting autogenerated keys  but update not found for both updates. 
**/
    public void Var019()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");


		PreparedStatement ps = connection_.prepareStatement("UPDATE " + tableRowId_ +" SET NAME=? WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		byte[] keyNotExist1={(byte) 0xea};
		ps.setString(1,"John"); 
		ps.setBytes(2,keyNotExist1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist2={(byte) 0xef};
		ps.setString(1,"Ann"); 
		ps.setBytes(2,keyNotExist2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Check that if the user calls getGeneratedKeys() on a CALL statement,
getGeneratedKeys() returns an empty result set as CALLs do not return generated keys.
Make sure that CALL is called twice.. 
**/
    public void Var020()
    {
	if (checkJdbc30 ()) {

	    try
	    {
		PreparedStatement ps = connection_.prepareStatement ("CALL "
								     + JDSetupProcedure.STP_RS0,
								     Statement.RETURN_GENERATED_KEYS);
		ps.execute ();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean check1 = (rs1!=null) && rs1.next();
		ps.execute ();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean check2 = (rs2!=null) && rs2.next();
		if (rs1 == null) {
		    assertCondition(false, "Error:  getGeneratedKeys 1 returned null."); 
		} else if (rs2 == null) {
		    assertCondition(false, "Error:  getGeneratedKeys 2 returned null."); 
		} else { 
		    assertCondition(!check1 && !check2, "check1="+check1+" check2="+check2+" rs.next returned true instead of false");
		}
		ps.close ();
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}

    }
/* Do select through autogenerated key  (3 types of tables) */

/**
getGeneratedKeys() - Insert into table without autogeneration and then run select statement requesting autogenerated keys.
**/
    public void Var021()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("select * from " + tableNoId_ +" WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run select statement requesting autogenerated keys.
**/
    public void Var022()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("select * from " + tableGenId_ +"   WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close();
	    /* No result set available for select */ 
		key1 = 0;
		key2 = 0;
		assertCondition(value1==key1 && value2==key2,
				"Expected value1="+key1+" for ("+value1+") "+
				"Expected value2="+key2+" for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }

/**
getGeneratedKeys() - Insert into table with rowid and then run select statement requesting autogenerated keys.
**/
	public void Var023() {
	if (checkJdbc30 ()) {

	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		byte[] key2 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.next();
		key2 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("select * from " + tableRowId_ +"   WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setBytes(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		ps.setBytes(1,key2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
	}



/**
getGeneratedKeys() - Insert into table without autogeneration and then run select statement requesting autogenerated keys but select not found for second select. 
**/
    public void Var024()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("select * from " + tableNoId_ +"   WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run select statement requesting autogenerated keys  but select not found for second select. 
**/
    public void Var025()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");

		int key1 = 0;
		// int key2 = 0;
		ResultSet rs = stmt.executeQuery("Select GENID from "+tableGenId_);
		rs.next();
		key1 = rs.getInt(1);
		rs.next();
		// key2 = rs.getInt(1);
		rs.close(); 



		PreparedStatement ps = connection_.prepareStatement("select * from " + tableGenId_ +"   WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close();

	    /* No result set available for select */ 
		key1 = 0;
		// key2 = 0;

		assertCondition(value1==key1 && value2==0,
				"Expected value1="+key1+" for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }

/**
getGeneratedKeys() - Insert into table with rowid and then run select statement requesting autogenerated keys  but select not found for second select. 
**/
    public void Var026()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");

		byte[] key1 = null;
		ResultSet rs = stmt.executeQuery("Select GENERATEID from "+tableRowId_);
		rs.next();
		key1 = rs.getBytes(1);
		rs.close(); 

		PreparedStatement ps = connection_.prepareStatement("select * from " + tableRowId_ +"   WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setBytes(1,key1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist={(byte) 0xef};
		ps.setBytes(1,keyNotExist);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }





/**
getGeneratedKeys() - Insert into table without autogeneration and then run select statement requesting autogenerated keys but select not found for both selects. 
**/
    public void Var027()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableNoId_);
		stmt.executeUpdate("Insert into "+tableNoId_+" values('nathaniel', 1)");
		stmt.executeUpdate("Insert into "+tableNoId_+" values('michael', 2)");

		PreparedStatement ps = connection_.prepareStatement("select * from " + tableNoId_ +"   WHERE ID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }

/**
getGeneratedKeys() - Insert into table with gen id and then run select statement requesting autogenerated keys  but select not found for both selects. 
**/
    public void Var028()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableGenId_);
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableGenId_+" (NAME) values('michelle')");




		PreparedStatement ps = connection_.prepareStatement("select * from " + tableGenId_ +"   WHERE GENID=?", Statement.RETURN_GENERATED_KEYS);

		ps.setInt(1,998); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		int value1 = 0;
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getInt(1); 
		}
		ps.setInt(1,999);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		int value2 = 0;
		if (resultAvailable2) {
		    if (rs2 != null) value2 = rs2.getInt(1); 
		} 
		ps.close();
		stmt.close(); 
		assertCondition(value1==0 && value2==0,
				"Expected value1=0 for ("+value1+") "+
				"Expected value2=0 for ("+value2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}
    }


/**
getGeneratedKeys() - Insert into table with rowid and then run select statement requesting autogenerated keys  but select not found for both selects. 
**/
    public void Var029()
    {
	if (checkJdbc30 ()) {
	    try
	    {
		Statement stmt = connection_.createStatement();
		stmt.executeUpdate("Delete from "+tableRowId_);
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('katie')");
		stmt.executeUpdate("Insert into "+tableRowId_+" (NAME) values('michelle')");


		PreparedStatement ps = connection_.prepareStatement("select * from " + tableRowId_ +"   WHERE GENERATEID=?", Statement.RETURN_GENERATED_KEYS);

		byte[] keyNotExist1={(byte) 0xea};
		ps.setBytes(1,keyNotExist1); 
		ps.execute();
		ResultSet rs1 = ps.getGeneratedKeys();
		boolean resultAvailable1 = (rs1 != null) && rs1.next();
		byte[] value1 = {(byte)0xeb};
		if (resultAvailable1) {
		    if (rs1 != null) value1 = rs1.getBytes(1); 
		}
		byte[] keyNotExist2={(byte) 0xef};
		ps.setBytes(1,keyNotExist2);
		ps.execute();
		ResultSet rs2 = ps.getGeneratedKeys();
		boolean resultAvailable2 = (rs2 != null) && rs2.next();
		byte[] value2 = {(byte)0xeb};
		if (resultAvailable2) {
		    if (rs2 != null ) value2 = rs2.getBytes(1); 
		} 
		ps.close();
		stmt.close(); 
		String stringValue1 = bytesToString(value1); 
		String stringValue2 = bytesToString(value2);
	    // Both toolbox and native driver do not return anything. 
		String stringKey1   = "EB"; 
		String stringKey2   = "EB"; 

		assertCondition(
				stringValue1.equals(stringKey1) && 
				stringValue2.equals(stringKey2) ,
				"Expected value1="+stringKey1+" for ("+stringValue1+") "+
				"Expected value2="+stringKey2+" for ("+stringValue2+") "+
				"resultAvailable1="+resultAvailable1+" "+
				"resultAvailable2="+resultAvailable1+" "+
				added20100910); 

	    }
	    catch (Throwable e)
	    {
		e.printStackTrace();
		failed(e); 
	    }
	}

    }

    // Test for CPS B63UC2 SFI fails to return generated value
    public void Var030() { 
       StringBuffer sb = new StringBuffer();
       boolean passed = true;
       String sql; 
       try {
	   String tablename = collection_+".EDIFICIO";
	   String tablename2= collection_+".situacionite";
	   String tablename3= collection_+".EdificioInspeccion";

	   Statement stmt = connection_.createStatement();

	   try {
	     sql = "DROP TABLE "+tablename; 
	     sb.append("Executing "+sql); 
	     stmt.execute(sql); 
	   } catch (Exception e) { 
	     sb.append("Caught Warning Exception"); 
	     printStackTraceToStringBuffer(e, sb); 
	   }

	   try {
	     sql = "DROP TABLE "+tablename2; 
	     sb.append("Executing "+sql); 
	     stmt.execute(sql); 
	   } catch (Exception e) { 
	     sb.append("Caught Warning Exception"); 
	     printStackTraceToStringBuffer(e, sb); 
	   }

	   try {
	     sql = "DROP TABLE "+tablename3; 
	     sb.append("Executing "+sql); 
	     stmt.execute(sql); 
	   } catch (Exception e) { 
	     sb.append("Caught Warning Exception"); 
	     printStackTraceToStringBuffer(e, sb); 
	   }


	   sql = "CREATE TABLE " +tablename + "("+
	       "Cod_Edificio BIGINT GENERATED ALWAYS AS IDENTITY, "+
	       "Accesible INTEGER, "+
	       "Ascensor INTEGER, "+
	       "Calificacion_Cultural_ID BIGINT,"+
	       "codigo_unificado VARCHAR(12) CCSID 1145,"+
	      "codigo_ute BIGINT,"+
	      "expediente_vigente_id BIGINT,"+
	       " fecha_alta TIMESTAMP,"+
	       " fecha_baja TIMESTAMP ,"+
	       " FechaCatalogado TIMESTAMP,"+ 
	       " FechaConstruccion VARCHAR(50) CCSID 284,"+
	      " FechaFinRehabilitacion TIMESTAMP,"+
	      " fecha_modificacion TIMESTAMP,"+
	      " ItinerarioAccesible INTEGER,"+
	   " observaciones_es VARCHAR(200) CCSID 284,"+ 
	      " observaciones_eu VARCHAR(200) CCSID 284,"+ 
	       " origen VARCHAR(10) CCSID 284,"+
	       " PortalAcesible INTEGER ,"+
	       " ReferenciaCatastral VARCHAR(50) CCSID 284,"+ 
	       " RegimenProteccion VARCHAR(255) CCSID 284,"+
	    " situacionITE_ID BIGINT ,"+
	       " DatoAdministrativo_ID BIGINT,"+
	       " UTM1  VARCHAR(50) CCSID 284,"+
	       " UTM2  VARCHAR(50) CCSID 284 ,"+
	      " usuario_alta VARCHAR(10) CCSID 284,"+ 
	       " usuario_baja VARCHAR(10) CCSID 284 ,"+
	       " usuario_modificacion VARCHAR(10) CCSID 284"+


         ")"; 
     sb.append("Creating table using "+sql+"\n"); 
     stmt.execute(sql); 


	   sql = "CREATE TABLE " +tablename2 + "("+
	       "id BIGINT GENERATED ALWAYS AS IDENTITY, "+
	       "edificio_id BIGINT,"+
	       " fecha_alta TIMESTAMP,"+
	       " fecha_baja TIMESTAMP ,"+
	     " fecha_tipo_situacionite TIMESTAMP, "+
             " tipo_situacionite_id BIGINT," +
	     " usuario_alta VARCHAR(10) CCSID 284,"+ 
	     " usuario_baja VARCHAR(10) CCSID 284 )";

     sb.append("Creating table using "+sql+"\n"); 
     stmt.execute(sql); 


     sql = "CREATE TABLE " +tablename3 + "("+
       "Edificio_Inspeccion_ID BIGINT GENERATED ALWAYS AS IDENTITY, "+
       " calificacion_energetica_id BIGINT,"+
       " condicion_accesibilidad_id BIGINT,"+ /* 2*/ 
     " dictamen_final_id BIGINT,"+
     "Cod_Edificio BIGINT," +
     "Edificio_ID_XML VARCHAR(255) CCSID 284, "+ /*5 */ 
     "EncargoConcepto  VARCHAR(250) CCSID 284, "+
     "EncargoDatos VARCHAR(250) CCSID 284, "+
     "EncargoNombre VARCHAR(250) CCSID 284, "+
     "expedienteite_id BIGINT, "+
     " fecha_alta TIMESTAMP,"+
     " fecha_baja TIMESTAMP ,"+
     " FechaEmision TIMESTAMP,"+
     " fecha_modificacion TIMESTAMP," + 
     " FechaVisita TIMESTAMP,"+
     "MotivoInspeccion VARCHAR(250) CCSID 284, "+   /* 15 */ 
     " observaciones_es VARCHAR(200) CCSID 284,"+ 
     " observaciones_eu VARCHAR(200) CCSID 284,"+ 
     " origen VARCHAR(10) CCSID 284,"+
     " REGISTRO_VIGENTE BIGINT, "+
     " RESULTADO VARCHAR(100) CCSID 284, "+
     " usuario_alta VARCHAR(10) CCSID 284,"+ 
     " usuario_baja VARCHAR(10) CCSID 284,"+
     "usuario_modificacion VARCHAR(10) CCSID 284,"+
     "valoracion_final_accesibilidad_id BIGINT " +
     ")"; 




     sb.append("Creating table using "+sql+"\n"); 
     stmt.execute(sql); 



     Connection connection2 = testDriver_.getConnection (baseURL_ , 
					      userId_, encryptedPassword_);

// Loop -- re-preparing and executing
	   for (int i = 0; i < 400; i++) {
	     sql = "INSERT INTO "+tablename+"(Cod_Edificio, " +
	     		"Accesible, " +
	     		"Ascensor, " +
	     		"Calificacion_Cultural_ID, " +
	     		"codigo_unificado, " +
	     		"codigo_ute, " +
	     		"expediente_vigente_id, " +
	     		"fecha_alta, " +
	     		"fecha_baja, " +
	     		"FechaCatalogado, " +
	     		"FechaConstruccion, " +
	     		"FechaFinRehabilitacion, " +
	     		"fecha_modificacion, " +
	     		"ItinerarioAccesible, " +
	     		"observaciones_es, " +
	     		"observaciones_eu, " +
	     		"origen, " +
	     		"PortalAcesible, " +
	     		"ReferenciaCatastral, " +
	     		"RegimenProteccion, " +
	     		"situacionITE_ID, " +
	     		"DatoAdministrativo_ID, " +
	     		"UTM1, " +
	     		"UTM2, " +
	     		"usuario_alta, " +
	     		"usuario_baja, " +
	     		"usuario_modificacion)  " +
	     		"VALUES(default,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	     sb.append("i="+i+" Preparing "+sql+"\n"); 
	     PreparedStatement ps ;
	     if (i%2 == 0) {
             ps = connection_.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     } else {
		 ps = connection2.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     }
	     // Set parameters
	     ps.setInt(1, 0);
	     ps.setInt(2, 0);
	     ps.setLong(3,3);
	     ps.setString(4, "430702000001");
	     
	     ps.setLong(5,16627);
	     ps.setNull(6,Types.BIGINT);
	     ps.setString(7,"2018-10-24 14:19:00.601");
	     ps.setNull(8, Types.TIMESTAMP);
	     ps.setNull(9, Types.TIMESTAMP);
	     ps.setString(10,""); 
	     ps.setNull(11, Types.TIMESTAMP);
	     ps.setNull(12, Types.TIMESTAMP);
	     ps.setInt(13,0);
	     ps.setString(14,""); 
	     ps.setString(15,"");
	     ps.setObject(16, "MANUAL");
	     ps.setInt(17,0);
	     ps.setString(18,"");
	     ps.setString(19,"");
	     ps.setNull(20, Types.BIGINT);
	     ps.setLong(21,11);
	     ps.setString(22,"");
	     ps.setString(23,"");
	     ps.setString(24,"U13D"); 
	     ps.setString(25,""); 
	     ps.setString(26,""); 
	     
	     long edificioKey = 0; 
	     ps.execute(); 
	     ResultSet rs = ps.getGeneratedKeys();
	     if (rs.next()) { 
	       edificioKey = rs.getLong(1); 
	       sb.append("..Generated key was "+edificioKey+"\n"); 
	     } else {
	       passed = false; 
	       sb.append("No generated keys available\n"); 
	     }
	     ps.close(); 


	     sql = "INSERT INTO "+tablename2+"(id, edificio_id, fecha_alta, fecha_baja, fecha_tipo_situacionite, tipo_situacionite_id, usuario_alta, usuario_baja) values (default, ?, ?, ?, ?, ?, ?, ?)";
	     sb.append("i="+i+" Preparing "+sql+"\n"); 
	     
	     if (i%2 == 0) {
             ps = connection_.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     } else {
		 ps = connection2.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     }
	     // Set parameters
	     
	     ps.setLong(1,14310);
	     ps.setString(2,"2018-10-24 14:19:00.601");
	     ps.setNull(3, Types.TIMESTAMP);
	     ps.setString(4,"2018-10-24 14:19:00.601");
	     ps.setLong(5,3);
	     ps.setString(6,"U13D"); 
	     ps.setString(7,""); 


	     ps.execute(); 
	      rs = ps.getGeneratedKeys();
	     if (rs.next()) { 
	       long key = rs.getLong(1); 
	       sb.append("..Generated key was "+key+"\n"); 
	     } else {
	       passed = false; 
	       sb.append("No generated keys available\n"); 
	     }
	     ps.close(); 
	     

	     sql = "INSERT INTO "+tablename3+"(Edificio_Inspeccion_ID, calificacion_energetica_id, condicion_accesibilidad_id, dictamen_final_id, Cod_Edificio, Edificio_ID_XML, EncargoConcepto, EncargoDatos, EncargoNombre, expedienteite_id, fecha_alta, fecha_baja, FechaEmision, fecha_modificacion, FechaVisita, MotivoInspeccion, observaciones_es, observaciones_eu, origen, REGISTRO_VIGENTE, RESULTADO, usuario_alta, usuario_baja, usuario_modificacion, valoracion_final_accesibilidad_id) values (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	     sb.append("i="+i+" Preparing "+sql+"\n"); 
	     
	     if (i%2 == 0) {
             ps = connection_.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     } else {
		 ps = connection2.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	     }
	     // Set parameters

	     ps.setInt(1, 0);
	     ps.setInt(2, 0);
	     ps.setLong(3,3);
	     ps.setString(4, "430702000001");
	     ps.setNull(1,Types.BIGINT );
	     ps.setNull(2,Types.BIGINT );
	     ps.setNull(3,Types.BIGINT );
	     ps.setLong(4,14310);
	     ps.setString(5,"XXXXXXXXXX");
	     ps.setString(6,"Carga");
	     ps.setString(7,"Carga");
	     ps.setString(8,"Carga");
	     ps.setNull(9, Types.BIGINT );
	     ps.setString(10,"2018-10-24 14:19:01.495");
	     ps.setNull(11, Types.TIMESTAMP);
	     ps.setString(12,"2018-10-24 00:00:00.0");
	     ps.setNull(13, Types.TIMESTAMP );
	     ps.setString(14,"2018-10-24 00:00:00.0");
	     ps.setString(15,"Carga");
	     ps.setString(16,"");
	     ps.setString(17,"");
	     ps.setString(18,"MANUAL");
	     ps.setNull(19, Types.BIGINT);
	     ps.setString(20,"");
	     ps.setString(21,"U13D");
	     ps.setString(22,"");
	     ps.setString(23,"");
	     ps.setNull(24, Types.BIGINT );

	     
 
	     ps.execute(); 
	     rs = ps.getGeneratedKeys();
	     if (rs.next()) { 
	       long key = rs.getLong(1); 
	       sb.append("..Generated key was "+key+"\n"); 
	     } else {
	       passed = false; 
	       sb.append("No generated keys available\n"); 
	     }
	     ps.close(); 


	     
	     sql = "UPDATE "+tablename+" set Accesible=?, Ascensor=?, Calificacion_Cultural_ID=?, codigo_unificado=?, codigo_ute=?,  " +
	     		" expediente_vigente_id=?, fecha_alta=?, fecha_baja=?, FechaCatalogado=?, FechaConstruccion=?, FechaFinRehabilitacion=?, " +
	     		" fecha_modificacion=?, ItinerarioAccesible=?, observaciones_es=?, observaciones_eu=?, origen=?, PortalAcesible=?, " +
	     		"      ReferenciaCatastral=?, RegimenProteccion=?, situacionITE_ID=?, DatoAdministrativo_ID=?, UTM1=?, UTM2=?, usuario_alta=?, " +
	     		" usuario_baja=?, usuario_modificacion=? where Cod_Edificio=?"; 

       sb.append("i="+i+" Preparing "+sql+"\n"); 

         if (i%2 == 0) {
           ps = connection_.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
     } else {
   ps = connection2.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
     }
   
  	
 ps.setInt(1,0);
 ps.setInt(2,0);
 ps.setLong(3,3);
 ps.setString(4,"430702000001"); 
 ps.setLong(5,16627);
 ps.setNull(6,Types.VARCHAR); 
 ps.setString(7,"2018-10-24 14:19:00.601"); 
 ps.setNull(8,Types.VARCHAR); 
 ps.setNull(9, Types.VARCHAR); 
 ps.setString(10,""); 
 ps.setNull(11, Types.VARCHAR); 
 ps.setString(12,"2018-10-24 14:19:02.157"); 
 ps.setInt(13,0); 
 ps.setString(14, ""); 
 ps.setString(15, ""); 
 ps.setString(16,"MANUAL"); 
 ps.setInt(17,0); 
 ps.setString(18,""); 
 ps.setString(19,""); 
 ps.setLong(20,15241); 
 ps.setLong(21,11); 
 ps.setString(22,""); 
 ps.setString(23,""); 
 ps.setString(24,"U13D"); 
 ps.setString(25,""); 
 ps.setString(26,"U13D"); 
 ps.setLong(27, edificioKey); 
        
 ps.execute(); 
 ps.close(); 
         
	     
       sql = "INSERT INTO "+tablename2+"(id, edificio_id, fecha_alta, fecha_baja, fecha_tipo_situacionite," +
       		" tipo_situacionite_id, usuario_alta, usuario_baja) values (default, ?, ?, ?, ?, ?, ?, ?)";
       sb.append("i="+i+" Preparing "+sql+"\n"); 
       
       if (i%2 == 0) {
             ps = connection_.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
       } else {
     ps = connection2.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
       }
       // Set parameters
       
       ps.setLong(1,14310);
       ps.setString(2,"2018-10-24 14:19:00.601");
       ps.setNull(3, Types.TIMESTAMP);
       ps.setString(4,"2018-10-24 14:19:00.601");
       ps.setLong(5,3);
       ps.setString(6,"U13D"); 
       ps.setString(7,""); 


       ps.execute(); 
        rs = ps.getGeneratedKeys();
       if (rs.next()) { 
         long key = rs.getLong(1); 
         sb.append("..Generated key was "+key+"\n"); 
       } else {
         passed = false; 
         sb.append("No generated keys available\n"); 
       }
       ps.close(); 
       




	     
	   }
	   


	   
	   
	       
	   connection2.close(); 
	   stmt.close();

	   assertCondition(passed, sb);

       } catch (Exception e) {
	   failed(e, sb.toString());

       }
    }



/* Call stored procedure that does update (with passed parameters) (3 types of tables) */



/* Repeat all tests for connectionReuseObject and connectionSystemNaming */




}


