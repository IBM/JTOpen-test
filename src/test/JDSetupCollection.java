///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSetupCollection.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectLockListEntry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData; 



/**
The JDSetupCollection class creates collections for the JDBC test
drivers and testcases.
**/
public class JDSetupCollection {



/**
Creates a collection and sets the authority such that
other test userids can use it.

@param  system      The system.
@param  password    The password.
@param  connection  The connection.
@param  collection  The collection name.
 @exception  Exception  If an exception occurs.
**/
    static public void create (AS400 system,	   
	                             Connection connection,
                               String collection  ) throws Exception
    {
	// The default behavior is not to drop the collection if it
        // exists.  If the collection is dropped, this
        // will cause problems because JDSetupProcedure calls
        // this for each stored procedure that it creates.
	//
	// If the testcase must be deleted by a testcase, it
	// should be deleted in the very beginning of the
	// testcase using the form of the create that does the delete. 
	//
	create(system,connection,collection, false); 
    } /* create */ 

    
    static void dropCollection(Statement s, String collection) {
	try {
	    s
	      .executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)')");
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	try {
	    System.out.println("JDSetupCollection:  Dropping COLLECTION "+collection);
	    // Exception e  = new Exception("Location");
	    // e.printStackTrace(System.out);

	    s.executeUpdate("DROP COLLECTION " + collection);
	} catch (SQLException e) {
	    System.out.println("Exception caught dropping collection");
	    e.printStackTrace(System.out); 
	}

    }
    
    /** 
     * Should the collection be dropped because of problems
     */
    
    static boolean shouldCollectionBeDropped(Connection conn, String collection ) {
      boolean dropCollection = false; 
      
      try {

	  Statement s= conn.createStatement(); 
	//
	// If not IBM database, don't attempt to run this
	//
	DatabaseMetaData dmd = conn.getMetaData();
	String productName = dmd.getDatabaseProductName();

	if ( productName.indexOf("AS") < 0) { 
	    // Not IBM i -- return
	    return false; 
	}

        String shortCollection = collection; 
        if (shortCollection.length() > 10) {
	  String sqlQuery = "select SYSTEM_SCHEMA_NAME, SCHEMA_NAME from qsys2.sysschemas where SCHEMA_NAME=?";
	  // Use systables -- sysschemas is too slow
	  sqlQuery = "select distinct SYSTEM_TABLE_SCHEMA, TABLE_SCHEMA  from qsys2.systables where TABLE_SCHEMA = ?"; 
          PreparedStatement ps = conn.prepareStatement(sqlQuery);
          ps.setString(1,collection); 
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
            shortCollection = rs.getString(1); 
          } else {
            /* Couldn't look up long name.  Drop it */
	    System.out.println("WARNING:  JDSetupCollection.shouldCollectionBeDropped:  not found by ("+collection+") "+sqlQuery); 
            dropCollection = true; 
            return true; 
          }
        }
        
          s.executeUpdate("CALL QSYS.QCMDEXC('DSPOBJD OBJ("+shortCollection+"/*ALL) OBJTYPE(*ALL) OUTPUT(*OUTFILE) OUTFILE(QTEMP/CONTENTS)                                                       ',000000120.00000)");

          ResultSet rs = null;
          try {
              rs = s.executeQuery(" SELECT * FROM qtemp.contents WHERE ODOBNM = 'QSQJRN' ");
              if (!rs.next()) {
                  System.out.println("WARNING:  JDSetupCollection.shouldCollectionBeDropped: QSQJRN not found in collection:  forcing recreate");
                  dropCollection = true; 
              }
              rs.close(); 
          } catch (SQLException e) {
              // Ignore not found exception
	      String exceptionMessage = e.toString().toUpperCase(); 
              if (exceptionMessage.indexOf("NOT FOUND") < 0) {
                  throw e; 
              } 
          } 

      } catch (Exception e) {
	  // Ignore not found exceptin 
	  if (e.toString().toUpperCase().indexOf("NOT FOUND") < 0) {

	      System.out.println("Warning:  JDSetupCollection: Exception trying to validate collection "+collection+"\n");
	      e.printStackTrace();
	  }
      } 
 
      return dropCollection; 
    }
/**
Creates a collection and sets the authority such that
other test userids can use it.

@param  system      The system.
@param  password    The password.
@param  connection  The connection.
@param  collection  The collection name.
 @exception  Exception  If an exception occurs.
**/
    static public void create(AS400 system, 
        Connection connection, String collection, boolean dropCollection)
    throws Exception {

      create(connection, collection, dropCollection);
    }


      static public void create(Connection conn, String collection, boolean dropCollection) throws SQLException {



       if  ( (collection.indexOf("\"") > 0) || (collection.indexOf("'") > 0) ) {
	   SQLException sqlex =  new SQLException("WARNING:  Cannot quotes in collection '"+collection+"'");
	   sqlex.printStackTrace(System.out);
	   return; 
       }
 

      // need authority for CHGJRN
      AS400 pwrSys = TestDriver.getPwrSys();
      
      Statement s = conn.createStatement(); 

      if (!dropCollection) dropCollection = shouldCollectionBeDropped( conn, collection); 

      //
      // drop the collection if the dropCollection flag is specified.
      // Note: This cannot always be true because JDSetupCollection.create
      // is called from JDSetupProcedure
      //

      if (dropCollection) {
        dropCollection(s, collection); 
      } 


      // 
      // Create the collection.
      // 

      boolean created = false;
      boolean retry = true;
      while (retry) {
	  retry = false; 
	  try {
	      s.executeUpdate ("CREATE COLLECTION " + collection);
	      created = true;
	  }
	  catch (SQLException e) {
	      int code = e.getErrorCode();
	      if (code == -901) {
		  System.out
		    .println("SQL0901 found -- trying to recover using RCLDBXREF");
		  s.executeUpdate("CALL QSYS2.QCMDEXC(' RCLDBXREF OPTION(*FIX) ')");
		  retry = true; 
	      } else {

		  if (e.toString().indexOf("-104") >= 0) {
		      try {
			  s.executeUpdate ("CREATE SCHEMA " + collection);
			  created = true;

		      } catch (Exception e2) {
			  if (e.toString().indexOf("-601") < 0) {
			      System.out.println("Error.  Unexpected exception creating collection "+collection);
			      e.printStackTrace(System.out); 
			  } 
		     // The collection already exists.
			  created = false;

		      } 
		  } else { 
		      if ((e.toString().indexOf("already exists") < 0) &&
			  (e.toString().indexOf("-601") < 0)){
			  System.out.println("Error.  Unexpected exception creating collection "+collection);
			  e.printStackTrace(System.out); 
		      } 
	    // The collection already exists.
		      created = false;
		  }
	      }
	  }
      } /* while retry */ 

        // We need to grant object authority to the
        // collection and the collection's journal so that
        // other users may create tables in it.
        if (created) {

	    String sql = "CALL QSYS.QCMDEXC('CHGJRN JRN("+collection+"/QSQJRN) MNGRCV(*SYSTEM) DLTRCV(*YES)  JRNRCV(*GEN) THRESHOLD(100000)                          ',0000000100.00000)";

	    try {
		s.executeUpdate(sql); 
	    } catch (Exception e) {
		System.out.println("Warning: exception on "+sql);
		e.printStackTrace(System.out); 
	    } 


	    try { 
		CommandCall cmd = new CommandCall (pwrSys);
		cmd.run ("GRTOBJAUT OBJ(QSYS/" + collection + ") OBJTYPE(*LIB) "
			 + "USER(*PUBLIC) AUT(*ALL)");
		cmd.run ("GRTOBJAUT OBJ(" + collection + "/QSQJRN) OBJTYPE(*JRN) "
			 + "USER(*PUBLIC) AUT(*ALL)");
		cmd.run("CHGJRN JRN("+collection+"/QSQJRN) MNGRCV(*SYSTEM) DLTRCV(*YES)");  
	    } catch (Throwable t) {
		System.err.println("Warning:  Exception occurred");
		t.printStackTrace();
		System.err.println("Warning:  retrying with connection");
		// If the server isn't up then an exception may occur.
		// If so, run the commands using the connection
		Statement stmt = conn.createStatement ();
		stmt.executeUpdate("CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(QSYS/" + collection + ") OBJTYPE(*LIB) "
			 + "USER(*PUBLIC) AUT(*ALL)                                   ',0000000070.00000)");
		stmt.executeUpdate("CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(" + collection + "/QSQJRN) OBJTYPE(*JRN) "
				+ "USER(*PUBLIC) AUT(*ALL)                                   ',0000000070.00000)"); 

		stmt.executeUpdate("CALL QSYS.QCMDEXC('CHGJRN JRN("+collection+"/QSQJRN) MNGRCV(*SYSTEM)      "
			        + "DLTRCV(*YES)                                              ',0000000070.00000)");               
		stmt.close(); 
		System.err.println("Warning:  retry successful");
	    } 
        }

	// Make sure that the atom table exists
	int retryCount = 5;
    while (retryCount > 0) {
      try {
        s.executeUpdate("drop table " + collection + ".atom");
        retryCount = 0;
      } catch (java.sql.SQLException jex) {
        String jexMessage = jex.toString();
        if ((jexMessage.indexOf("not found") >= 0) || (jexMessage.indexOf("-204") >= 0) ) {
          // OK. We don't care if it was not found.
          retryCount = 0;
        } else {
          if (jexMessage.indexOf("in use") >= 0) {
            try {
              System.out
                  .println("Warning:  object in use -- trying to end jobs");
              ObjectDescription objectDescription = new ObjectDescription(
                  pwrSys, collection, "ATOM", "FILE");
              ObjectLockListEntry[] objectLockList = objectDescription
                  .getObjectLockList();
              for (int i = 0; i < objectLockList.length; i++) {
                ObjectLockListEntry lock = objectLockList[i];
                String jobname = lock.getJobName();
                String jobnumber = lock.getJobNumber();
                String jobusername = lock.getJobUserName();
                System.out.println("Warning: ending job " + jobnumber + "/"
                    + jobusername + "/" + jobname + " holding lock");
                // Now end the job immediately to release the lock
                Job job = new Job(pwrSys, jobname, jobusername, jobnumber);
                job.end(0);

              }
              // Sleeping 10 seconds for the job to catch up.
              Thread.sleep(10);
            } catch (Exception endException) {
              System.out.println("Exception trying to end jobs holding locks");
              endException.printStackTrace(System.out);

            }
          } else if (jexMessage.indexOf("Not authorized") >= 0) {
            CommandCall cmd = new CommandCall(pwrSys);
            System.out.println(
                "Warning... Attempted to authorize *PUBLIC to table atom");
            try {
              cmd.run("GRTOBJAUT OBJ(" + collection
                  + "/ATOM) OBJTYPE(*FILE) USER(*PUBLIC) AUT(*ALL)");
            } catch (Exception e) {
              System.out
                  .println("Warning... could not grant permissions table atom");
              e.printStackTrace(System.out);

            }
            retryCount--;
          } else {
            System.out.println("Warning... could not drop table atom");
            jex.printStackTrace(System.out);
          }
          retryCount--;
        }
      }
    } /* while retryCount > 0 */
    try {
    	s.executeUpdate("create table "+collection+".atom(nothing int)");
	s.executeUpdate("grant all on table "+collection+".atom to public with grant option");
       //insert into atom values(null);
	s.executeUpdate("insert into "+collection+".atom values(null)");
    } catch (Exception e) {
	System.out.println("Warning.  unable to create atom");
	e.printStackTrace(System.out);

    } 

        s.close ();

  }

  /**
   * Creates a collection and sets the authority such that other test userids
   * can use it.
   * 
   * @param connection
   *          The connection.
   * @param collection
   *          The collection name.
   * @exception Exception
   *              If an exception occurs.
   **/
  static public void create(Connection connection, String collection)
      throws Exception {
      create(connection,collection,JDSupportedFeatures.getDefault()); 
  }

      
      public static void create(Connection connection, String collection,
      JDSupportedFeatures supportedFeatures) throws Exception {
    // Create the collection.
    Statement s = connection.createStatement();

    boolean forceDrop = shouldCollectionBeDropped(connection, collection);

    //
    // drop the collection if the dropCollection flag is specified.
    // Note: This cannot always be true because JDSetupCollection.create
    // is called from JDSetupProcedure
    //

    if (forceDrop) {
      dropCollection(s, collection);
    }

    boolean created = false;
    try {
      s.executeUpdate("CREATE COLLECTION " + collection);
      created = true;
    } catch (SQLException e) {
      // The collection already exists.
      created = false;
    }

    
    // We need to grant object authority to the
    // collection and the collection's journal so that
    // other users may create tables in it.
    if (created) {

      String sql = null;
      try {
        String shortCollection = collection;
        if (shortCollection.length() > 10) {
          sql = "select SYSTEM_TABLE_SCHEMA FROM SYSIBM.SQLSCHEMAS where TABLE_SCHEM='"
              + shortCollection + "'";
          ResultSet rs = s.executeQuery(sql);
          rs.next();
          shortCollection = rs.getString(1);
        }

        sql = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ(QSYS/"
            + shortCollection
            + ") OBJTYPE(*LIB) "
            + "USER(*PUBLIC) AUT(*ALL)                                   ',0000000070.00000)";
        s.executeUpdate(sql);
        sql = "CALL QSYS.QCMDEXC('GRTOBJAUT OBJ("
            + shortCollection
            + "/QSQJRN) OBJTYPE(*JRN) "
            + "USER(*PUBLIC) AUT(*ALL)                                   ',0000000070.00000)";
        s.executeUpdate(sql);

        sql = "CALL QSYS.QCMDEXC('CHGJRN JRN("
            + shortCollection
            + "/QSQJRN) MNGRCV(*SYSTEM)      "
            + "DLTRCV(*YES)                                              ',0000000070.00000)";
        s.executeUpdate(sql);
      } catch (Exception e) {
        System.out.println("Exception on " + sql
            + " while granting permissions");
        e.printStackTrace(System.out);
      }

    }
    
    
    if (supportedFeatures.booleanSupport) { 
        // Create the default boolean table
        String sql=""; 
        try { 
          sql="CREATE TABLE "+collection+".BOOLTABLE (C1 BOOLEAN)";
          s.executeUpdate(sql); 
          sql="INSERT INTO "+collection+".BOOLTABLE VALUES(true)";
          s.executeUpdate(sql);
	  sql="GRANT ALL ON "+collection+".BOOLTABLE TO PUBLIC";
          s.executeUpdate(sql);
        } catch (SQLException sqlex) { 
          if (sqlex.getErrorCode() != -601) { 
            System.out.println("ERROR on SQL "+sql); 
            throw sqlex; 
          }
          
        }
    }
    
    s.close();

  }






}


