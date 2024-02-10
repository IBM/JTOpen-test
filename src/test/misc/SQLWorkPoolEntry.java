//////////////////////////////////////////////////////////////////////
//
//
// OCO Source Materials
//
// The Source code for this program is not published or otherwise
// divested of its trade secrets, irrespective of what has been
// deposited with the U.S. Copyright Office
//
// 5770-SS1
// (C) Copyright IBM Corp. 2018,2018
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    SQLWorkPool.java
//
// Classes:      SQLWorkPool.java
//
// Class to create a poool of work that will be run by threads.
// The number of running threads will adapt due to the load on the system. 
// Errors from the requests will be ignored.
// The primary purpose is to be able to delete libraries quicker. 
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
// See changeActivity field below. 
//
//-------------------------------------------------------------

package test.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SQLWorkPoolEntry {
    long id;
    String sql; 

    public SQLWorkPoolEntry(long id, String sql) {
	this.id = id;
	this.sql = sql; 
    } 
 
}
