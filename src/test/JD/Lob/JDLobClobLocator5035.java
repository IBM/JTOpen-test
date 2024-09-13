///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobClobLocator5035.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;

import test.JDLobTest;

import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobClobLocator5035.  This tests the following method
of the JDBC Clob class:

<ul>
<li>getAsciiStream()
<li>getCharacterStream()
<li>getSubString()
<li>length()
<li>position()
<li>setString() //@C2A
<li>setAsciiStream() //@C2A
<li>setCharacterStream() //@C2A
<li>truncate() //@C2A
<li>free() //@pda
</ul>
**/
public class JDLobClobLocator5035
extends JDLobClobLocator
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobClobLocator5035";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }






/**
Constructor.
**/
    public JDLobClobLocator5035 (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobClobLocator5035",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    public JDLobClobLocator5035 (AS400 systemObject,
			     String testname, 	 
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, testname,
               namesAndVars, runMode, fileOutputStream,
               password);
	lobThreshold = "1"; 

    }

  void setupTableNames() {
        TABLE_  = JDLobTest.COLLECTION + ".CL5035";
        TABLE2_ = JDLobTest.COLLECTION + ".CL50352";
	TABLE3_ = JDLobTest.COLLECTION + ".CL50353"; 
        TABLE4_ = JDLobTest.COLLECTION + ".CL50354";
	TABLE5_ = JDLobTest.COLLECTION + ".CL50355"; 
	TABLE6_ = JDLobTest.COLLECTION + ".CL50356";
	TABLE120_= JDLobTest.COLLECTION + ".CL5035120";
	TABLE121_= JDLobTest.COLLECTION + ".CL5035121";
	TABLEHUGE_ = JDLobTest.COLLECTION + ".CL5035H";


  }

  void setupTestStringValues() {

      // Note:  This must end with a charcter that does not exist elsewhere in the string 
      SMALL_ = "A \u3044\u3046\u3048\u304a\u3045\u3047\u3049 small obj.";
      MEDIUM_    = "A real object \u3042\u3044\u3046\u3048\u304a."; 
      LARGE_          = null; // final
      WIDTH_          = 30000;
      int LARGE_CHAR_COUNT_ = 30000 / 3; 

      DBCLOB_MEDIUM_ =  "\u3042\u3044\u3046\u3048\u304a\u3041\u3043\u3045\u3047\u3049\u3051\u3052\u3053\u3054\u3055";

      DBCLOB_MEDIUM_SET_ = "\u3041\u3048\u3046\u3044\u3042\u304a\u3048\u3046\u3044\u3042\u3049\u3047\u3045\u3043\u3041"; 
 
    
      DBCLOB_SMALL_ =  "\u3041\u3042\u3043\u3044\u3045\u3046\u3047\u3048\u3049\u3050\u3051\u3052\u3053\u3054\u3055\u3056\u3057\u3058\u3059\u3060"; 
	// Note.. This takes 5 bytes for evey 2 character so just do WIDTH_ /3 characters 
        StringBuffer buffer = new StringBuffer (LARGE_CHAR_COUNT_);
        int actualLength = LARGE_CHAR_COUNT_ ;
	for (int i = 1; i <= actualLength; ++i) {
	    if ((i%2)==0) { 
		buffer.append ("\u3042");
	    } else {
		buffer.append ("\uff16"); 
	    } 
	}
        LARGE_ = buffer.toString ();
	DBCLOB_LARGE_ = LARGE_;

      if (System.getProperty("java.home").indexOf("32bit") > 0) {
	  HUGE_WIDTH_     = 1000000;
      } else {

	  String jvmName = System.getProperty("java.vm.name");
	  String classpath = System.getProperty("java.class.path");
	  if ((jvmName.indexOf("Classic") >=  0)  && (classpath.indexOf("jt400native.jar") >= 0)) {
	      HUGE_WIDTH_     = 1000000;
	  } else { 
	      HUGE_WIDTH_     = 32000000;
	  }
      }


      System.out.println(this+" setting up HUGE with width"+HUGE_WIDTH_); 
      buffer = new StringBuffer (HUGE_WIDTH_/3);
      actualLength = HUGE_WIDTH_/3 - 2;
      for (int i = 1; i <= actualLength; ++i)
	  buffer.append ((char)(0xFF21+i%26));
      HUGE_ = buffer.toString ();
      buffer.setLength(0); 
      buffer = null;
      System.gc();

      CCSID_ = 5035; 
      DBCLOB_CCSID_ = 835;
    SMALL_CLOB_APPEND_=" \u3042efgh\n";
    MEDIUM_CLOB_APPEND_=" \u30435678\n";
    MEDIUM2_CLOB_APPEND_=" \u3044mnop\n";
    LARGE_CLOB_APPEND_=" \u3045uvwx\n";


      skipAsciiTests = true;


      ASSIGNMENT1_ = "Really \u3042\u3044\u3046\u3048\u304a objects";

      ASSIGNMENT_DBCHAR1_ = "\u3042";
      ASSIGNMENT_DBCHAR4_ = "\u3042\u3044\u3046\u3048";
      ASSIGNMENT_DBCHAR7_="\u3041\u3042\u3043\u3044\u3045\u3046\u3047";
      ASSIGNMENT_DBCHAR9_="\u3041\u3042\u3043\u3044\u3045"+
                           "\u3046\u3047\u3048\u3049";
      ASSIGNMENT_DBCHAR17_="\u3041\u3042\u3043\u3044\u3045"+
                           "\u3046\u3047\u3048\u3049\u3050"+
                           "\u3051\u3052\u3053\u3054\u3055"+
                           "\u3056\u3057";
      ASSIGNMENT_DBCHAR20_="\u3041\u3042\u3043\u3044\u3045"+
                           "\u3046\u3047\u3048\u3049\u3050"+
                           "\u3051\u3052\u3053\u3054\u3055"+
                           "\u3056\u3057\u3058\u3059\u3060";


  }
/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
	super.setup(); 
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	super.cleanup(); 
    }




/**
 ** TESTCASES inherited from superclass JDlobClobLocator
 **/


    public void Var240() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }
    public void Var241() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }
    public void Var242() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }
    public void Var243() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }
    public void Var256() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }
    public void Var257() { notApplicable("Locator testcase -- UTF8 lobs are converted to CLOB"); }



   /**
     Find native bug when closing statement after UTF-8 result sets have been used.
    **/
    public void Var301()
    {
	    try {
		Statement stmt = connection_.createStatement();


		try {
		    stmt.executeUpdate("DROP TABLE "+TABLE120_);
		} catch (Exception e) {
		}

		stmt.executeUpdate("CREATE TABLE "+TABLE120_+" (c1 clob(2000) CCSID 5035, c2 char(3000), c3 char(3000), c4 char(2000), c5 char(10000), c6 char(10000))");

		stmt.executeUpdate("INSERT INTO "+TABLE120_+" VALUES('ABC','2','3','4','5','6')");

		ResultSet rs = stmt.executeQuery("SELECT * from "+TABLE120_);
		rs.next();
		// System.out.println("Getting clob");

	    /* Clob clob = rs.getClob(1); */ 
		String s = rs.getString(1); 
		// System.out.println("Clob retrieved");

		stmt.close(); 

		stmt = connection_.createStatement();
		stmt.executeUpdate("DROP TABLE "+TABLE120_);
		assertCondition(true, "s="+s); 

	    } catch (Throwable e) {
		failed (e, "Unexpected Exception -- added by native driver 10/26/2005");
	    }	
    }

   /**
     Find native bug when reusing prepared statement after UTF-8 result sets have been used.
    **/
    public void Var302()
    {
	    try {
		Statement stmt = connection_.createStatement();


		try {
		    stmt.executeUpdate("DROP TABLE "+TABLE121_);
		} catch (Exception e) {
		}

		stmt.executeUpdate("CREATE TABLE "+TABLE121_+" (c1 clob(2000) CCSID 5035, c2 char(3000), c3 char(3000), c4 char(2000), c5 char(10000), c6 char(10000))");

		stmt.executeUpdate("INSERT INTO "+TABLE121_+" VALUES('ABC','2','3','4','5','6')");

		PreparedStatement pstmt = connection_.prepareStatement("SELECT * from "+TABLE121_);

		ResultSet rs = pstmt.executeQuery(); 
		rs.next();
		// System.out.println("Getting clob");

		String s = rs.getString(1); 
		// System.out.println("Clob retrieved");

		rs = pstmt.executeQuery();
		rs.next();
		// System.out.println("Getting clob");

		s = rs.getString(1); 
		// System.out.println("Clob retrieved");

		pstmt.close(); 

		stmt = connection_.createStatement();
		stmt.executeUpdate("DROP TABLE "+TABLE121_);

		assertCondition(true, "s = "+s+" -- added by native driver 10/26/2005"); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Exception -- added by native driver 10/26/2005");
	    }	
    }




}
