///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmStringLarge.java
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
// File Name:    JDParmStringLarge.java
//
// Classes:      JDParmStringLarge
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test.JD.Parm;

import com.ibm.as400.access.AS400;

import test.JDParmTest;
import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDParmStringLarge.  This tests the following method
of the JDBC PreparedStatement class:

<ul>
<li>setString()
</ul>
**/
public class JDParmStringLarge
extends JDTestcase {

    public final static String message = " -- Added by native driver 03/01/2006"; 

    // Private data.
    public Connection connection = null;
    public PreparedStatement cchar     = null; 
    public PreparedStatement cvarchar  = null; 
    public PreparedStatement cclob     = null;
    public PreparedStatement cdbclob     = null; 
    public PreparedStatement cwchar  = null; 
    public PreparedStatement cwvarchar  = null; 
    public PreparedStatement cdecimal = null;
    public PreparedStatement cnumeric = null;
    public PreparedStatement cgraphic  = null; 
    public PreparedStatement cvargraphic  = null; 

   boolean runningJ9 = false;


    public String chartable = "";
    public String varchartable = ""; 
    public String clobtable = "";
    public String dbclobtable = ""; 
    public String wchartable = ""; 
    public String wvarchartable = ""; 
    public String decimaltable = "";
    public String numerictable = "";
    public String graphictable = "";
    public String vargraphictable = ""; 

    public String largeValue = "";
    public String hugeValue = "";

    public String largeGraphicValue = "";
    public String hugeGraphicValue = "";


    public String largeSql = "";
    public String hugeSql  = "";

    int vrm = 0 ; 

    

/**
Constructor.
**/
    public JDParmStringLarge (AS400 systemObject,
                          Hashtable namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDParmStringLarge",
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
        try {

	   vrm = testDriver_.getRelease();
           // Get a global connection - choose how you which
           // to get the connection.
           connection = testDriver_.getConnection(baseURL_+";errors=full", userId_, encryptedPassword_); 

           Statement s = connection.createStatement();

	   chartable =     JDParmTest.COLLECTION+".jdparmlrg1";
	   varchartable =  JDParmTest.COLLECTION+".jdparmlrg2";
	   clobtable =     JDParmTest.COLLECTION+".jdparmlrg3";
	   dbclobtable =   JDParmTest.COLLECTION+".jdparmlrg4";
	   wchartable =    JDParmTest.COLLECTION+".jdparmlrg5";
	   wvarchartable = JDParmTest.COLLECTION+".jdparmlgv6";
	   numerictable =  JDParmTest.COLLECTION+".jdparmlrg7";
	   decimaltable =  JDParmTest.COLLECTION+".jdparmlrg8";
	   graphictable =  JDParmTest.COLLECTION+".jdparmlrg9";
	   vargraphictable =  JDParmTest.COLLECTION+".jdparmlrgA";

           try { s.executeUpdate("drop table "+ chartable );    } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
}
           try { s.executeUpdate("drop table "+ varchartable ); } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
 }
           try { s.executeUpdate("drop table "+ clobtable );    } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
 }
	   try { s.executeUpdate("drop table "+ dbclobtable );  } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
}
           try { s.executeUpdate("drop table "+ wchartable );   } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
 }
           try { s.executeUpdate("drop table "+ wvarchartable );} catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
}
	   try { s.executeUpdate("drop table "+ numerictable ); } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
 }
           try { s.executeUpdate("drop table "+ decimaltable ); } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
}
           try { s.executeUpdate("drop table "+ graphictable );   } catch (SQLException e) {
	       JDParmHelper.handleDropException(e);
 }
           try { s.executeUpdate("drop table "+ vargraphictable );} catch (SQLException e) {
JDParmHelper.handleDropException(e);

 }


           // Create with all types of columns 

           s.executeUpdate("create table "+chartable+    "(cchar    char(32000))");
           s.executeUpdate("create table "+varchartable+ "(cvarchar varchar(32000)) "); 
           s.executeUpdate("create table "+clobtable+    "(cclob    clob (40000000))"); 
           s.executeUpdate("create table "+dbclobtable+  "(cdbclob    dbclob (40000000) CCSID 13488)"); 
           s.executeUpdate("create table "+wchartable+   "(cwchar graphic(16000) CCSID 13488) ");
           s.executeUpdate("create table "+wvarchartable+ "(cwvarchar vargraphic(16000) CCSID 13488 )");
           s.executeUpdate("create table "+numerictable+  "(cnumeric    numeric(10,10))");
           s.executeUpdate("create table "+decimaltable+  "(cdecimal    decimal(10,10))");
           s.executeUpdate("create table "+graphictable+   "(cgraphic graphic(16000) CCSID 835 ) ");
           s.executeUpdate("create table "+vargraphictable+ "(cvargraphic vargraphic(16000) CCSID 835 )");


           s.close();


           cchar       = connection.prepareStatement(
          	         "insert into "+chartable+" (cchar) values(?)");
           cvarchar    = connection.prepareStatement(
                         "insert into "+varchartable+" (cvarchar) values(?)");
           cclob       = connection.prepareStatement(
                         "insert into "+clobtable+" (cclob) values(?)");
           cdbclob       = connection.prepareStatement(
                         "insert into "+dbclobtable+" (cdbclob) values(?)");
           cwchar    = connection.prepareStatement(
                         "insert into "+wchartable+"(cwchar) values(?)");
           cwvarchar = connection.prepareStatement(
                         "insert into "+wvarchartable+" (cwvarchar) values(?)");

           cnumeric       = connection.prepareStatement(
          	         "insert into "+numerictable+" (cnumeric) values(?)");

           cdecimal       = connection.prepareStatement(
          	         "insert into "+decimaltable+" (cdecimal) values(?)");

           cgraphic       = connection.prepareStatement(
          	         "insert into "+graphictable+" (cgraphic) values(?)");

           cvargraphic       = connection.prepareStatement(
          	         "insert into "+vargraphictable+" (cvargraphic) values(?)");



           String shortValue = "abcdefghijklmnopqrstuvwxy";

           StringBuffer largeValueSB = new StringBuffer(shortValue);

           while (largeValueSB.length() < 20000000) {
               largeValueSB.append(shortValue);
           }

	       largeValue = largeValueSB.substring(0, 9000000);
	       hugeValue = largeValueSB.substring(0, 20000000);

           String shortGraphicValue = "\u5e03\u5f17\u672b\u5378\u59cb\u8679\u500c\u89f4\u9853\u8271\u8f44\u52f1";

           StringBuffer largeGraphicValueSB = new StringBuffer(shortGraphicValue);

           while (largeGraphicValueSB.length() < 20000000) {
               largeGraphicValueSB.append(shortGraphicValue);
           }

	       largeGraphicValue = largeGraphicValueSB.substring(0, 9000000);
	       hugeGraphicValue = largeGraphicValueSB.substring(0, 20000000);


	   largeGraphicValueSB.setLength(0);
	   largeGraphicValueSB= null; 
	   largeValueSB.setLength(0);
	   largeValueSB = null; 

	   largeSql= "insert into dummy values('"+largeValue+"')";
	   hugeSql= "insert into dummy values('"+hugeValue+"')"; 

           
	   if (getDriver() == JDTestDriver.DRIVER_NATIVE || getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	       String vmName = System.getProperty("java.vm.name");
	       if (vmName ==  null) {
	           runningJ9 = false; 
	       } else { 
	           if (vmName.indexOf("Classic VM") >= 0) {
	               runningJ9 = false;
	           } else {
	               runningJ9 = true;
	           }
	       }
	   }

       } catch (Exception e) {
           System.out.println("Caught exception: " + e.getMessage());
           e.printStackTrace();
       }
    }



/**
This is the place to put all cleanup work for the testcase.
**/   
   public void cleanup() {
      try {
	 // Drop all the tables that were created.
	  Statement s = connection.createStatement();

	  try { s.executeUpdate("drop table "+ chartable );    } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ varchartable ); } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ clobtable );    } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ dbclobtable );  } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ wchartable );   } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ wvarchartable );} catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ numerictable ); } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ decimaltable ); } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ graphictable );   } catch (SQLException e) {
	      JDParmHelper.handleDropException(e);
	  }
	  try { s.executeUpdate("drop table "+ vargraphictable );} catch (SQLException e) {
	      JDParmHelper.handleDropException(e);

	  }




         // Close the global connection opened in setup().
         connection.close();

      } catch (Exception e) {
         System.out.println("Caught exception: ");
         e.printStackTrace();
      }
   }



/**
Test:  char large 
**/
    public void Var001() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cchar.setString(1, largeValue);
		int count = cchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, chartable);
	    }
	}
    }

/**
Test:  char huge 
**/
    public void Var002() {

	if (runningJ9 && getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("Not working in V5R4 and J9P");
	    return; 
	} 
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 

	    try {
		cchar.setString(1, hugeValue);
		int count = cchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, chartable);
	    }
	}
    }


/**
Test:  clob large 
**/
    public void Var003() {

	// This should work now.  Copy fixes from V6R1 jdbcUtil/UTF8ReadLocator
	/* 
	if (runningJ9 && getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("Not working in V5R4 and J9");
	    return; 
	}
	 */ 

	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cclob.setString(1, largeValue);
		int count = cclob.executeUpdate();
		if (count == 1)
		    assertCondition(JDParmHelper.verifyString("cclob", largeValue, connection, clobtable), message);
		else
		    failed ("invalid update count"+message);

	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);

	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);

	    } finally {
		JDParmHelper.purgeStringsTable(connection, clobtable);
	    }
	}
    }




/**
Test:  clob huge 
**/
    public void Var004() {
	// This should work now.  Copy fixes from V6R1 jdbcUtil/UTF8ReadLocator
	

	if (runningJ9 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
	    notApplicable("Out of memory in J9");
	    return; 
	} 

	if (JDParmTest.COLLECTION.indexOf("CU") > 0) {
	    notApplicable("OOM error with native sockets and classic JVM");
	    return; 
	}
       
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cclob.setString(1, hugeValue);
		int count = cclob.executeUpdate();
		if (count == 1)
		    assertCondition(JDParmHelper.verifyString("cclob", hugeValue, connection, clobtable),message);
		else
		    failed ("invalid update count"+message);

	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);

	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);

	    } finally {
		JDParmHelper.purgeStringsTable(connection, clobtable);
	    }
	}
    }


/**
Test:  varchar large 
**/
    public void Var005() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cvarchar.setString(1, largeValue);
		int count = cvarchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, varchartable);
	    }
	}
    }

/**
Test:  varchar huge 
**/
    public void Var006() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cvarchar.setString(1, hugeValue);
		int count = cvarchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, varchartable);
	    }
	}
    }

/**
Test:  wchar large 
**/
    public void Var007() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cwchar.setString(1, largeValue);
		int count = cwchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, wchartable);
	    }
	}
    }

/**
Test:  wchar huge 
**/
    public void Var008() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cwchar.setString(1, hugeValue);
		int count = cwchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, wchartable);
	    }
	}
    }

/**
Test:  dbclob large 
**/
    public void Var009() {
        if (runningJ9 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            notApplicable("Out of memory in J9");
            return; 
        }

	if (JDParmTest.COLLECTION.indexOf("CU") > 0) {
	    notApplicable("OOM error with native sockets and classic JVM");
	    return; 
	}

	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cdbclob.setString(1, largeValue);
		int count = cdbclob.executeUpdate();
		if (count == 1)
		    assertCondition(JDParmHelper.verifyString("cdbclob", largeValue, connection, dbclobtable),message);
		else
		    failed ("invalid update count"+message);

	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);

	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);

	    } finally {
		JDParmHelper.purgeStringsTable(connection, dbclobtable);
	    }
	}
    }



/**
Test:  dbclob huge 
**/
    public void Var010() {
        if (runningJ9 && getDriver() == JDTestDriver.DRIVER_TOOLBOX) {
            notApplicable("Out of memory in J9");
            return; 
        }
	if (JDParmTest.COLLECTION.indexOf("CU") > 0) {
	    notApplicable("OOM error with native sockets and classic JVM");
	    return; 
	}

	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cdbclob.setString(1, hugeValue);
		int count = cdbclob.executeUpdate();
		if (count == 1)
		    assertCondition(JDParmHelper.verifyString("cdbclob", hugeValue, connection, dbclobtable),message);
		else
		    failed ("invalid update count"+message);

	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);

	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);

	    } finally {
		JDParmHelper.purgeStringsTable(connection, dbclobtable);
	    }
	}
    }


/**
Test:  wvarchar large 
**/
    public void Var011() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cwvarchar.setString(1, largeValue);
		int count = cwvarchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, wvarchartable);
	    }
	}
    }

/**
Test:  wvarchar huge 
**/
    public void Var012() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cwvarchar.setString(1, hugeValue);
		int count = cwvarchar.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, wvarchartable);
	    }
	}
    }

/**
Test:  numeric large 
**/
    public void Var013() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cnumeric.setString(1, largeValue);
		int count = cnumeric.executeUpdate();
	    //
	    // We expect an exception since it is too large.
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw exception  "+message+" count="+count);

	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, numerictable);
	    }
	}
    }

/**
Test:  numeric huge 
**/
    public void Var014() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cnumeric.setString(1, hugeValue);
		int count = cnumeric.executeUpdate();
	    //
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw exception  "+message+" count="+count);

	    } catch (SQLException e) {
		assertCondition(true); 

	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, numerictable);
	    }
	}
    }

/**
Test:  decimal large 
**/
    public void Var015() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cdecimal.setString(1, largeValue);
		int count = cdecimal.executeUpdate();
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw exception  "+message+" count="+count);

	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, decimaltable);
	    }
	}
    }

/**
Test:  decimal huge 
**/
    public void Var016() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cdecimal.setString(1, hugeValue);
		int count = cdecimal.executeUpdate();
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw exception  "+message+" count="+count);

	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, decimaltable);
	    }
	}
    }

/**
Test:  graphic large 
**/
    public void Var017() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cgraphic.setString(1, largeGraphicValue);
		int count = cgraphic.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, graphictable);
	    }
	}
    }
/**
Test:  graphic huge 
**/
    public void Var018() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cgraphic.setString(1, hugeGraphicValue);
		int count = cgraphic.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, graphictable);
	    }
	}
    }


/**
Test:  vargraphic large 
**/
    public void Var019() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cvargraphic.setString(1, largeGraphicValue);
		int count = cvargraphic.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }

/**
Test:  vargraphic huge 
**/
    public void Var020() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		cvargraphic.setString(1, hugeGraphicValue);
		int count = cvargraphic.executeUpdate();
	    //
	    // We expect truncation since string is too large
	    // This test is to verify that the native JDBC driver
	    // doesn't hit a fatal error
	    //
		failed ("Didn't throw truncation "+message+" count="+count);

	    } catch (DataTruncation dt) {
		assertCondition ((dt.getIndex() == 1), "truncation thrown but index is wrong"+message); 
	    } catch (SQLException e) {
		failed (e, "Unexpected Exception"+message);
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }
/**
 * Test a large sql statement.  This should not cause a fatal error
 */ 
    public void Var021() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else {
	    Statement stmt = null; 
	    try {
		stmt = connection.createStatement();
		stmt.execute(largeSql); 
		failed ("Didn't hit exception "+message); 
	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		try { 
		    if (stmt != null) stmt.close();
		} catch (Exception e) {
		}
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }

/**
 * Test a huge sql statement.  This should not cause a fatal error
 */ 
    public void Var022() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    Statement stmt = null;	
	    try {
		// Clean up as much space as possible 
		System.gc(); 
		stmt = connection.createStatement();
		stmt.execute(hugeSql); 
		failed ("Didn't hit exception "+message); 
	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		try { 
		    if (stmt != null) stmt.close();
		} catch (Exception e) {
		}
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }


/**
 * Test a large sql statement.  This should not cause a fatal error
 */ 
    public void Var023() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		connection.prepareStatement(largeSql);
		failed ("Didn't hit exception "+message); 
	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }
/**
 * Test a huge sql statement.  This should not cause a fatal error
 */ 
    public void Var024() {
	if (vrm <= 520) {
	    notApplicable("Native problem with huge strings not fixed in V5R2"); 
	} else { 
	    try {
		// Clear up as much memory as possible
		System.gc(); 
		connection.prepareStatement(hugeSql); 
		failed ("Didn't hit exception "+message); 
	    } catch (SQLException e) {
		assertCondition(true); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Throwable"+message);
	    } finally {
		JDParmHelper.purgeStringsTable(connection, vargraphictable);
	    }
	}
    }





}



