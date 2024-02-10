///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobNClobLocator8.java
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
import test.JDReflectionUtil;
import test.JDSQL400;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.IOException; 
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobNClobLocator8.  This tests the following method
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
public class JDLobNClobLocator8
extends JDLobNClobLocator
{




    private boolean LONGRUNNING = true;
    boolean toolboxNative = false; 



/**
Constructor.
**/
    public JDLobNClobLocator8 (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             String password)
    {
        super (systemObject, "JDLobNClobLocator8",
               namesAndVars, runMode, fileOutputStream,
               password);
    }


    public JDLobNClobLocator8 (AS400 systemObject,
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
	    TABLE_  = JDLobTest.COLLECTION + ".CLOBLU8";
	    TABLE2_ = JDLobTest.COLLECTION + ".CLOBLU82";
	    TABLE3_ = JDLobTest.COLLECTION + ".CLOBLU83"; 
	    TABLE4_ = JDLobTest.COLLECTION + ".CLOBLU84";
	    TABLE5_ = JDLobTest.COLLECTION + ".CLOBLU85"; 
	    TABLE6_ = JDLobTest.COLLECTION + ".CLOBLU86";
	    TABLE120_= JDLobTest.COLLECTION + ".CLOBLU8120";
	    TABLE121_= JDLobTest.COLLECTION + ".CLOBLU8121";
	    TABLEHUGE_ = JDLobTest.COLLECTION + ".CLOBLU8H";
  }

  void setupTestStringValues() {

      SMALL_ = "\u0391 really =\u03b2ig object\u02f3";
      MEDIUM_         = "\u0391 really \u03b2ig object\u02f3";
      WIDTH_ = 30000; 


      System.out.println(this+" setting up  LARGE with "+WIDTH_); 
      StringBuffer buffer = new StringBuffer (WIDTH_);
      int actualLength = WIDTH_ - 2;
      for (int i = 1; i <= actualLength; ++i)
	  buffer.append ("&");
      LARGE_ = buffer.toString ();
      DBCLOB_LARGE_ = LARGE_; 

      CCSID_ = 1208;

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

      DBCLOB_MEDIUM_ =  "\u3042\u3044\u3046\u3048\u304a\u3041\u3043\u3045\u3047\u3049\u3051\u3052\u3053\u3054\u3055";

      DBCLOB_MEDIUM_SET_ = "\u3041\u3048\u3046\u3044\u3042\u304a\u3048\u3046\u3044\u3042\u3049\u3047\u3045\u3043\u3041"; 
 
      DBCLOB_SMALL_ =  "\u3041\u3042\u3043\u3044\u3045\u3046\u3047\u3048\u3049\u3050\u3051\u3052\u3053\u3054\u3055\u3056\u3057\u3058\u3059\u3060"; 



      System.out.println(this+" setting up HUGE with width"+HUGE_WIDTH_); 
      buffer = new StringBuffer (HUGE_WIDTH_/3);
      actualLength = HUGE_WIDTH_/3 - 2;
      for (int i = 1; i <= actualLength; ++i)
	  buffer.append ((char)(0xFF21+i%26));
      HUGE_ = buffer.toString ();
      buffer.setLength(0); 
      buffer = null;
      System.gc();

      CCSID_ = 1208; 
      DBCLOB_CCSID_ = 1200;
    SMALL_CLOB_APPEND_=" \u03b1bcdefgh\n";
    MEDIUM_CLOB_APPEND_=" \u00b92345678\n";
    MEDIUM2_CLOB_APPEND_=" \u03f3jklmnop\n";
    LARGE_CLOB_APPEND_=" \u03d5rstuvwx\n";


      ASSIGNMENT1_ = "Really \u3042\u3044\u3046\u3048\u304a objects";

      ASSIGNMENT_DBCHAR4_ = "\u3042\u3044\u3046\u3048";
      ASSIGNMENT_DBCHAR7_="\u3041\u3042\u3043\u3044\u3045\u3046\u3047";
      ASSIGNMENT_DBCHAR20_="\u3041\u3042\u3043\u3044\u3045"+
                           "\u3046\u3047\u3048\u3049\u3050"+
                           "\u3051\u3052\u3053\u3054\u3055"+
                           "\u3056\u3057\u3058\u3059\u3060"; 



      skipAsciiTests = true; 

  }
/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
	try { 
	super.setup();


	// Testcase fails in V6R1 for U testcases
	String initials = "";
	int l = JDLobTest.COLLECTION.length();
	if (l > 5) {
	    initials =  JDLobTest.COLLECTION.substring(l - 5); 
	}
	System.out.println("initials are "+initials); 
	if (initials.equals("614CU") ||
	    initials.equals("615CU") ||
	    initials.equals("616CU")    ) {
	    toolboxNative = true; 
	}



	    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
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

	} catch (Throwable t) {
	    System.out.println("Error caught during setup");
	    Runtime runtime = Runtime.getRuntime();
	    System.out.println("runtime.freeMemory()="+runtime.freeMemory()); 
	    System.out.println("runtime.totalMemory()="+runtime.totalMemory());
	    try { 
		System.out.println("runtime.maxMemory()="+runtime.maxMemory());
	    } catch (java.lang.NoSuchMethodError e) {
		System.out.println("runtime.maxMemory()=UNKNOWN:no maxMemory method");
	    } 
	    System.out.println("---------BEGIN STACK--------------"); 
	    t.printStackTrace(System.out);
	    System.out.println("---------END STACK--------------"); 
	    System.out.flush(); 


	} 
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
 **
 ** TESTCASES inherited from superclass JDlobClobLocator
 **/


/**
 ** Inherited testcases Var240-300
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
	if (requireJdbc40 && (! isJdbc40())) {
	    notApplicable("JDBC 4.0 testcase");
	    return; 
	}
	    try {
		Statement stmt = connection_.createStatement();


		try {
		    stmt.executeUpdate("DROP TABLE "+TABLE120_);
		} catch (Exception e) {
		}

		stmt.executeUpdate("CREATE TABLE "+TABLE120_+" (c1 clob(2000) CCSID 1208, c2 char(3000), c3 char(3000), c4 char(2000), c5 char(10000), c6 char(10000))");

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
		assertCondition(true, "Retrieved "+s); 

	    } catch (Throwable e) {
		failed (e, "Unexpected Exception");
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

		stmt.executeUpdate("CREATE TABLE "+TABLE121_+" (c1 clob(2000) CCSID 1208, c2 char(3000), c3 char(3000), c4 char(2000), c5 char(10000), c6 char(10000))");

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

		assertCondition(true, "retrieved "+s); 
	    } catch (Throwable e) {
		failed (e, "Unexpected Exception");
	    }	
    }

/**
getAsciiStream() - When the lob is full and huge .
**/
    public void Var303()
    {
	if (requireJdbc40 && (! isJdbc40())) {
	    notApplicable("JDBC 4.0 testcase");
	    return; 
	}

	if (runningJ9 && getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("J9 test not working in V5R4"); return; 
	} 
	if (getRelease() == JDTestDriver.RELEASE_V5R3M0) {
	    notApplicable("Large LOB truncated to 1000000 -- added 11/2005 Fixed in V54 by SQ"); return; 
	} 

        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {

		    Statement stmt = connection_.createStatement();
		    ResultSet rs = stmt.executeQuery("Select * from "+TABLEHUGE_);
		    boolean rowAvailable = rs.next();
		    if (!rowAvailable) {
			failed("Row not available in "+TABLEHUGE_);
			
		    } else { 
			Object clob = JDReflectionUtil.callMethod_O(rs,getMethod,"C_CLOB");
			InputStream v = (InputStream) JDReflectionUtil.callMethod_O(clob,"getAsciiStream");
			assertCondition (compare (v, HUGE_, "8859_1"), "extracted stream not equal to expected String -- clob.length = "+JDReflectionUtil.callMethod_L(clob,"length")+" sb "+HUGE_.length() +" -- Added 11/30/2005 by native");
			stmt.close();
		    }
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Added 11/30/2005 by native");
                }
            }
        }
    }



/**
getCharacterStream() - When the lob is full.
**/
    public void Var304()
    {
	if (requireJdbc40 && (! isJdbc40())) {
	    notApplicable("JDBC 4.0 testcase");
	    return; 
	}

	if (runningJ9 && getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("J9 test not working in V5R4"); return; 
	} 

	if (getRelease() == JDTestDriver.RELEASE_V5R3M0) {
	    notApplicable("Large LOB truncated to 1000000 -- added 11/2005 Fixed in V54 by SQ"); return; 
	} 

        if (checkJdbc20 ()) {
            if (checkLobSupport ()) {
                try {

		    Statement stmt = connection_.createStatement();
		    ResultSet rs = stmt.executeQuery("Select * from "+TABLEHUGE_); 
		    boolean rowAvailable = rs.next();
		    if (!rowAvailable) {
			stmt.close();
			failed("Row not present in "+TABLEHUGE_);
		    } else { 
			Object clob = JDReflectionUtil.callMethod_O(rs,getMethod,"C_CLOB");
			Reader v = (Reader) JDReflectionUtil.callMethod_O(clob,"getCharacterStream");
			boolean comparison = compare (v, HUGE_);
			stmt.close();
			assertCondition (comparison, " Comparison failed -- Added 11/30/2005 by native");
		    }
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception -- Added 11/30/2005 by native");
                }
            }
        }
    }


    public void Var305()
    {
	if (requireJdbc40 && (! isJdbc40())) {
	    notApplicable("JDBC 4.0 testcase");
	    return; 
	}
	if (runningJ9 && getRelease() == JDTestDriver.RELEASE_V5R4M0) {
	    notApplicable("J9 test not working in V5R4"); return; 
	} 

	    /*
	     * Note:  This takes a while to run on V5R3
             * Also takes a long time on V5R4. I'm not sure why?
             * seems to be stuck in getSubString
	     * getSubString is padding to the maximum amount.
	     * I'm not sure that this is right. 
	     * Hmm.. 32 meg should be this bad? 
             */

		if (!LONGRUNNING ) {
		    assertCondition(false, "FIXME -- takes a long time to run in V5R4 using native driver. "); 
		} else { 
		    try {
			
			Statement stmt = connection_.createStatement();
			ResultSet rs = stmt.executeQuery("Select * from "+TABLEHUGE_); 
			boolean rowAvailable = rs.next();
			if (!rowAvailable) {
			    stmt.close();
			    failed("Row not present in "+TABLEHUGE_);
			} else { 
			    Object clob = JDReflectionUtil.callMethod_O(rs,getMethod,"C_CLOB");
			    String v = null;

			    v = (String) JDReflectionUtil.callMethod_O(clob,"getSubString", (long) 1, (int) HUGE_WIDTH_ - 2);

			    stmt.close(); 
			    assertCondition (compare(v, HUGE_), "Comparison failed -- Added 11/30/2005 by native");
			}
		    }
		    catch (Exception e) {
			failed (e, "Unexpected Exception -- Added 11/30/2005 by native");
		    }
		}
            }



    public void Var306() {
	char [] chars1208 = {'\u00c0','\u35c0','\ub5a0','\u3055','\u31ff','\u3066'};
	testUtf8Clob(chars1208, 3000000); 
    }

    public void Var307() {
	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

	char [] chars1208 = {'\u00c0','\u35c0','\ub5a0','\u3055','\u31ff','\u3066'};
	testUtf8Clob(chars1208, 6000000); 
    }

    public void Var308() {
	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

	char [] chars1208 = {'a', '\u00c0','\ub5a0','\u3055','\u31ff','\u3066'};
	testUtf8Clob(chars1208, 6000000); 
    }

    public void Var309() {
	if (toolboxNative) {
	    notApplicable("Toolbox native fails with Out of Memory");
	    return; 
	} 

	char [] chars1208 = {'a', '\u00c0','\ub5a0','\u31ff','\u3066'};
	testUtf8Clob(chars1208, 6000000); 
    }


    public void testUtf8Clob(char[] chars1208, int specifiedLength) { 

	if (requireJdbc40 && (! isJdbc40())) {
	    notApplicable("JDBC 4.0 testcase");
	    return; 
	}

	if (checkJdbc20 ()) {
		try {
		    PreparedStatement pstmt = connection_.prepareStatement("select cast(? AS CLOB(2G) CCSID 1208) from sysibm.sysdummy1");

		    char[] stuff = new char[specifiedLength];
		    int inputLength = chars1208.length; 
		    for(int i = 0; i < specifiedLength; i++)  {
			stuff[i] = (char) chars1208[i % inputLength];
		    }
		    String inString = new String(stuff);
		    String inCrc32 = JDSQL400.getCRC32(inString);
		    int    inLength = inString.length(); 

		    pstmt.setString(1,inString);

		    ResultSet rs = pstmt.executeQuery(); 
		    boolean rowAvailable = rs.next();
		    if (!rowAvailable) {
			pstmt.close();
			failed("Row not present ");
		    } else { 
			Clob clob = rs.getClob (1);
			int outLength = (int) clob.length(); 
			String outString = clob.getSubString (1, outLength);
			String outCrc32 = JDSQL400.getCRC32(outString);
			pstmt.close(); 
			assertCondition ( (inLength == outLength) &&
					  (inCrc32.equals(outCrc32)),
					  " -- Failed inLength="+inLength+
					  "  outClob.length="+outLength+
					  " outString.length="+outString.length()+
					  " inCrc32="+inCrc32+
					  " outCrc32="+outCrc32+
					  " -- added 2/27/2013 by native" ); 

		    }
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception -- Added 2/27/2013 by native");
		}
	    }
    }






    /**
    Compares an InputStream with a String.
    **/
    protected boolean compare (InputStream i, String s, String encoding)
    {
        try
        {
            return(compare (i, s.getBytes (encoding)));
        }
        catch(java.io.UnsupportedEncodingException e)
        {
            return false;
        }
    }


   /**
    Compares an InputStream with a byte[].
    **/
    protected static boolean compare (InputStream i, byte[] b)
    {
        try
        {
            byte[] buf = new byte[b.length];
            int num = i.read(buf, 0, buf.length);
            if(num == -1) return b.length == 0;
            int total = num;
            while(num > -1 && total < buf.length)
            {
                num = i.read(buf, total, buf.length-total);
                total += num;
            }
            if(num == -1) --total;

	    if ( total != b.length) {
		System.out.println("  Actual Bytes("+total+")="+dumpBytes(buf));
		System.out.println("Expected Bytes("+b.length+")="+dumpBytes(b));
	    }
	    boolean allDone = (i.available() == 0); 
	    if ( !allDone ) {
		System.out.println("Bytes still available");
		System.out.println("  Actual Bytes("+total+")="+dumpBytes(buf));
		System.out.println("Expected Bytes("+b.length+")="+dumpBytes(b));
		int restCount = i.available();
		buf=new byte[restCount];
		i.read(buf, 0, restCount);
		System.out.println("  Extra bytes("+restCount+")="+dumpBytes(buf));
	    } 
            return total == b.length && allDone  && isEqual(b, buf);	

        }
        catch(IOException e)
        {
	    System.out.println("Exception thrown");
	    e.printStackTrace(); 
            return false;
        }
    }


    public static boolean isEqual(byte[] expected, byte[] actual) {
	int offset = 0; 
        boolean answer = expected.length == actual.length;
        if (answer)
        {
            for (int i = 0; i < expected.length; ++i)
            {
                if (expected[i] != actual[i])
                {
                    answer = false;
		    offset=i; 
                    break;
                }
            }
        }
	if (answer == false) {
	    System.out.println("Different at offset "+offset); 
	    System.out.println("  Actual Bytes("+actual.length+")="+dumpBytes(actual));
	    System.out.println("Expected Bytes("+expected.length+")="+dumpBytes(expected));
	} 
        return answer;
    }

    protected static String dumpBytes(byte[] array) {
	StringBuffer sb = new StringBuffer();
	int length = array.length;
	if (array.length > 8000) {
	    length = 4000; 
	} 
	for (int i = 0; i< length; i++) {
	    int x = 0xFF & ((int) array[i]); 
	    sb.append(Integer.toHexString(x));
	    if ( (i % 200 == 0) && (i > 0)) {
		sb.append("\n"); 
	    }

	}

	if (array.length > 4000) {
	    sb.append("\n...\n");
	    for (int i = array.length-2000; i< array.length; i++) {
		int x = 0xFF & ((int) array[i]); 
		sb.append(Integer.toHexString(x));
		if ( (i % 200 == 0) && (i > 0)) {
		    sb.append("\n"); 
		}

	    }

	} 

	return sb.toString(); 
    } 


    protected static String dumpChars(String s) {
	StringBuffer sb = new StringBuffer();
	int length = s.length(); 
	if (s.length()  > 8000) {
	    length = 4000; 
	} 
	
	for (int i = 0; i< length; i++) {
	    int x = 0xFFFF & ((int) s.charAt(i)); 
	    if (x < 0x10) {
		sb.append(" 000"); 
		sb.append(Integer.toHexString(x));
	    } else if (x < 0x100) {
		sb.append(" 00"); 
		sb.append(Integer.toHexString(x));
	    } else if (x < 0x1000) {
		sb.append(" 0"); 
		sb.append(Integer.toHexString(x));
	    } else {
		sb.append(" "); 
		sb.append(Integer.toHexString(x));
	    }
	    if ( (i % 200 == 0) && (i > 0)) {
		sb.append("\n"); 
	    }
	}

	if (s.length() > 8000) {
	    sb.append("\n................\n");
	    for (int i = s.length() - 4000; i< s.length(); i++) {
		int x = 0xFFFF & ((int) s.charAt(i)); 
		if (x < 0x10) {
		    sb.append(" 000"); 
		    sb.append(Integer.toHexString(x));
		} else if (x < 0x100) {
		    sb.append(" 00"); 
		    sb.append(Integer.toHexString(x));
		} else if (x < 0x1000) {
		    sb.append(" 0"); 
		    sb.append(Integer.toHexString(x));
		} else {
		    sb.append(" "); 
		    sb.append(Integer.toHexString(x));
		}
		if ( (i % 200 == 0) && (i > 0)) {
		    sb.append("\n"); 
		}
	    }

	} 

	return sb.toString(); 
    } 



    /**
    Compares a Reader with a String.
    **/
    protected static boolean compare (Reader r, String s)	// @K2
    {
        try
        {
            int slength = s.length ();
            if(s.length() == 0)
            {
                int lastRead = r.read ();
		boolean answer = (lastRead == -1) || (lastRead == 0);
		if (!answer) {
		    System.out.println("String.length=0 but lastRead="+lastRead); 
		}
                return(answer);
            }
            char[] c2 = new char[slength];
            StringBuffer buffer = new StringBuffer (slength);
            int lastRead = 0;
            while(true)
            {
                lastRead = r.read (c2);
                if((lastRead == -1) || (lastRead == 0))
                    break;
                buffer.append (new String (c2, 0, lastRead));
            }
            String s2 = buffer.toString ();
            boolean answer=s2.equals (s);
	    if ( ! answer ) {
		   System.out.println("hex(actual)   = "+dumpChars(s2));
		   System.out.println("hex(expected) = "+dumpChars(s));
	    } 
            return(answer );
        }
        catch(IOException e)
        {
	    e.printStackTrace(); 
            return false;
        }
    }

    /**
    Compares a String with a String.
    **/
    protected static boolean compare (String s2, String s)	
    {
    	boolean answer =s2.equals (s); 
	    if ( ! answer  ) {
	      System.out.println("actual.length="+s2.length());
		  System.out.println("hex(actual)   = "+dumpChars(s2.substring(1,100)));
	      System.out.println("expected.length="+s.length());
		  System.out.println("hex(expected) = "+dumpChars( s.substring(1,100)));
	    }
        return(answer);
        
    }





}
