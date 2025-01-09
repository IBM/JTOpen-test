///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetStringMixed.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;


import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDTestUtilities;

/**
 * Testcase JDRSGetStringBIB. This tests the following method of the JDBC
 * ResultSet class with BIDI data
 * 
 * <ul>
 * <li>getString()
 * </ul>
 **/
public class JDRSGetStringMixed extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetStringMixed";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }

  // Private data.
  private String properties_ = "NOTSET";
  // private static String table_ = JDRSTest.COLLECTION + ".JDRSGSUNSP";

  /**
   * Constructor.
   **/
  public JDRSGetStringMixed(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetStringMixed", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    reconnect("errors=full");
    // table_ = JDRSTest.COLLECTION + ".JDRSGSUNSP";
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    connection_.commit(); // @E1A
    connection_.close();
    connection_ = null;
  }

  /**
   * Reconnects with different properties, if needed.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  private void reconnect(String properties) throws Exception {
    if (!properties_.equals(properties)) {
      properties_ = properties;
      if (connection_ != null)
        cleanup();

      String url = baseURL_;
      if (getDriver() != JDTestDriver.DRIVER_JCC) {
        url += ";" + properties_;
      }
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }
  }

  public void testCcsid( String ccsid, int var , String dataType, String[][] values) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    try {
      
      reconnect(""); 
      
      String sql;
      String table = JDRSTest.COLLECTION + ".JDRSGSM"+var+"C" + ccsid;
      Statement stmt = connection_.createStatement();
      initTable(stmt,  table ,  "(KEY int, C1 " + dataType + ")", sb); 
     
      for (int i = 0; i < values.length; i++) {
        sql = "INSERT INTO " + table + " VALUES(" + i + "," + values[i][0]
            + ")";
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      }
      sql = "SELECT C1, HEX(RTRIM(SUBSTR(C1,1,16366))), CAST(C1 AS DBCLOB(1G) CCSID 1200) FROM " + table + " order by KEY";
      sb.append("Executing " + sql + "\n");
      ResultSet rs = stmt.executeQuery(sql);
      int i = 0;
      while (rs.next()) {
        String retrievedString = rs.getString(1);
        String retrievedHex = rs.getString(2);
        String retrievedUnicode = rs.getString(3); 
        String expectedString = values[i][1];
        
        boolean compareOk = true; 
        
        if (!expectedString.equals(retrievedString)) {
            sb.append("ERROR: Got != Expected\n" );
	    sb.append("expected  length is "+expectedString.length()+"\n");
	    sb.append("retrieved length is "+retrievedString.length()+"\n"); 
            compareOk = false; 
        }
        if (!expectedString.equals(retrievedUnicode)) {
	    // In 7.1 the CAST to DBCLOB is adding extra characters.
	    // Do not check for this release
	    if (getRelease()  != JDTestDriver.RELEASE_V7R1M0) { 
		sb.append("TESTCASE ERROR: Expected != retrievedUnicode\n" ); 
		compareOk = false;
	    }
      }
        
        if (!compareOk) {   
          passed = false;
          sb.append("["+i+"]       Got  \""
              + JDTestUtilities.getMixedString(retrievedString) + "\"\n");
          sb.append("["+i+"]   expected \""
              + JDTestUtilities.getMixedString(expectedString) + "\"\n");
          sb.append("["+i+"]       from \""
              + JDTestUtilities.getMixedString(values[i][0]) + "\"\n");
          sb.append("["+i+"]       hex  "
              + retrievedHex + "\n");
          sb.append("["+i+"]       1200 \""
              + JDTestUtilities.getMixedString(retrievedUnicode) + "\"\n");
        }
        i++;
      }

      stmt.close();
      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception == " + sb.toString());
    }

  }


  String[][] padExpected(String[][] original, int pad) {
      String[][] newExpected = new String[original.length][];

      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < pad; i++) {
	  sb.append(' '); 
      }
      String padString = sb.toString(); 
      for (int i = 0; i < original.length; i++) {
	  newExpected[i] = new String[2];
	  newExpected[i][0] = original[i][0];
	  newExpected[i][1] = original[i][1]+padString; 
      } 
      return newExpected; 
  } 

  /**
   * getString() - Test CCSID 937
   **/
  
  private static String[][] ccsid937varchar = {
    //check simple  
    {"UX'04192169ffe52162'",                 "\u0419\u2169\uffe5\u2162"},
    {"UX'0031003204192169ffe5216200390030'", "12\u0419\u2169\uffe5\u216290",},
    {"UX'ff21ff10222c30f100b121030020'",     "\uff21\uff10\u222c\u30f1\u00b1\u2103\u0020" },
    {"UX'5e025e035f17672a672b537859cb'",     "\u5e02\u5e03\u5f17\u672a\u672b\u5378\u59cb"},
    {"UX'8679500c89f4985382718f44977e52f1'", "\u8679\u500c\u89f4\u9853\u8271\u8f44\u977e\u52f1"},

  }; 

  
  static String[][] ccsid937char23 = {

      {"UX'04192169ffe52162'","\u0419\u2169\uffe5\u2162             "},
      {"UX'0031003204192169ffe5216200390030'","12\u0419\u2169\uffe5\u216290         ",},
      {"UX'ff21ff10222c30f100b121030020'",    "\uff21\uff10\u222c\u30f1\u00b1\u2103        " },
      {"UX'00205e0200205e0300205f170020672a'", " \u5e02 \u5e03 \u5f17 \u672a   "},
      {"UX'002086790020500c002089f400209853'", " \u8679 \u500c \u89f4 \u9853   "},


  };

  public void Var001() {
    testCcsid("937", 1, "VARCHAR(80) CCSID 937 ", ccsid937varchar); 
  }

  public void Var002() {
    testCcsid("937", 2, "VARCHAR(16368) CCSID 937 ", ccsid937varchar); 
  }

  public void Var003() {
    testCcsid("937", 3, "VARCHAR(16369) CCSID 937 ", ccsid937varchar); 
  }

  public void Var004() {
    testCcsid("937", 4, "VARCHAR(16370) CCSID 937 ", ccsid937varchar); 
  }

  public void Var005() {
    testCcsid("937", 5, "VARCHAR(32000) CCSID 937 ", ccsid937varchar); 
  }
  
  public void Var006() {
    testCcsid( "937", 6, "CHAR(23) CCSID 937 ", ccsid937char23); 
  }

  public void Var007() {
    testCcsid( "937", 7, "CHAR(103) CCSID 937 ", padExpected(ccsid937char23, 80)); 
  }

  public void Var008() {
    testCcsid("937", 8, "CHAR(16368) CCSID 937 ",  padExpected(ccsid937char23, 16368-23)); 
  }

  public void Var009() {
    testCcsid("937", 9, "CHAR(16369) CCSID 937 ",  padExpected(ccsid937char23, 16369-23)); 
  }

  public void Var010() {
    testCcsid("937", 10, "CHAR(16370) CCSID 937 ",  padExpected(ccsid937char23, 16370-23)); 
  }

  public void Var011() {
    testCcsid("937", 11, "CHAR(32000) CCSID 937 ",  padExpected(ccsid937char23, 32000-23)); 
  }


  private static String[][] ccsid930varchar = {
    //check simple  
    {"UX'04192169ffe52162'",                 "\u0419\u2169\uffe5\u2162"},
    {"UX'0031003204192169ffe5216200390030'", "12\u0419\u2169\uffe5\u216290",},
    {"UX'ff21ff10222c30f100b121030020'",     "\uff21\uff10\u222c\u30f1\u00b1\u2103\u0020" },
    {"UX'5e025e035f17672a672b537859cb'",     "\u5e02\u5e03\u5f17\u672a\u672b\u5378\u59cb"},
    {"UX'8679867989f4827182718f44'", "\u8679\u8679\u89f4\u8271\u8271\u8f44"},

  }; 
 static String[][] ccsid930char23 = {

      {"UX'04192169ffe52162'","\u0419\u2169\uffe5\u2162             "},
      {"UX'0031003204192169ffe5216200390030'","12\u0419\u2169\uffe5\u216290         ",},
      {"UX'ff21ff10222c30f100b121030020'",    "\uff21\uff10\u222c\u30f1\u00b1\u2103         " },
      {"UX'00205e0200205e0300205f170020672a'", " \u5e02 \u5e03 \u5f17 \u672a   "},
      {"UX'0020867900208679002089f400208271'", " \u8679 \u8679 \u89f4 \u8271   "},

 
  };

  public void Var012() {
    testCcsid("930", 12, "VARCHAR(80) CCSID 930 ", ccsid930varchar); 
  }

  /* Test the larger types */ 
  public void Var013() {
    testCcsid("930", 13, "VARCHAR(16368) CCSID 930 ", ccsid930varchar); 
  }

  public void Var014() {
    testCcsid("930", 14, "VARCHAR(16369) CCSID 930 ", ccsid930varchar); 
  }

  public void Var015() {
    testCcsid("930", 15, "VARCHAR(16370) CCSID 930 ", ccsid930varchar); 
  }

  public void Var016() {
    testCcsid("930", 16, "VARCHAR(32000) CCSID 930 ", ccsid930varchar); 
  }

  
  public void Var017() {
    testCcsid( "930", 17, "CHAR(23) CCSID 930 ", ccsid930char23); 
  }


  public void Var018() {
    testCcsid( "930", 18,  "CHAR(103) CCSID 930 ", padExpected(ccsid930char23, 80)); 
  }

  public void Var019() {
    testCcsid("930", 19, "CHAR(16368) CCSID 930 ",  padExpected(ccsid930char23, 16368-23)); 
  }

  public void Var020() {
    testCcsid("930", 20, "CHAR(16369) CCSID 930 ",  padExpected(ccsid930char23, 16369-23)); 
  }

  public void Var021() {
    testCcsid("930", 21, "CHAR(16370) CCSID 930 ",  padExpected(ccsid930char23, 16370-23)); 
  }

  public void Var022() {
    testCcsid("930", 22, "CHAR(32000) CCSID 930 ",  padExpected(ccsid930char23, 32000-23)); 
  }



  private static String[][] ccsid935varchar = {
    //check simple  
    {"UX'04192169ffe52162'",                 "\u0419\u2169\uffe5\u2162"},
    {"UX'0031003204192169ffe5216200390030'", "12\u0419\u2169\uffe5\u216290",},
    {"UX'ff21ff10ff1030f100b121030020'",     "\uff21\uff10\uff10\u30f1\u00b1\u2103\u0020" },
    {"UX'5e025e035f17672a672b537859cb'",     "\u5e02\u5e03\u5f17\u672a\u672b\u5378\u59cb"},


  }; 

  
  static String[][] ccsid935char23 = {

      {"UX'04192169ffe52162'","\u0419\u2169\uffe5\u2162             "},
      {"UX'0031003204192169ffe5216200390030'","12\u0419\u2169\uffe5\u216290         ",},
      {"UX'ff21ff10ff1030f100b121030020'",    "\uff21\uff10\uff10\u30f1\u00b1\u2103         " },
      {"UX'00205e0200205e0300205f170020672a'", " \u5e02 \u5e03 \u5f17 \u672a   "},
      {"UX'002086790020500c002086790020500c'", " \u8679 \u500c \u8679 \u500c   "},


  };


  public void Var023() {
    testCcsid("935", 23, "VARCHAR(80) CCSID 935 ", ccsid935varchar); 
  }

  public void Var024() {
    testCcsid("935", 24, "VARCHAR(16368) CCSID 935 ", ccsid935varchar); 
  }

  public void Var025() {
    testCcsid("935", 25, "VARCHAR(16369) CCSID 935 ", ccsid935varchar); 
  }

  public void Var026() {
    testCcsid("935", 26, "VARCHAR(16370) CCSID 935 ", ccsid935varchar); 
  }

  public void Var027() {
    testCcsid("935", 27, "VARCHAR(32000) CCSID 935 ", ccsid935varchar); 
  }
  
  public void Var028() {
    testCcsid( "935", 28, "CHAR(23) CCSID 935 ", ccsid935char23); 
  }

  public void Var029() {
    testCcsid( "935", 29, "CHAR(103) CCSID 935 ", padExpected(ccsid935char23, 80)); 
  }

  public void Var030() {
    testCcsid("935", 30, "CHAR(16368) CCSID 935 ",  padExpected(ccsid935char23, 16368-23)); 
  }

  public void Var031() {
    testCcsid("935", 31, "CHAR(16369) CCSID 935 ",  padExpected(ccsid935char23, 16369-23)); 
  }

  public void Var032() {
    testCcsid("935", 32, "CHAR(16370) CCSID 935 ",  padExpected(ccsid935char23, 16370-23)); 
  }

  public void Var033() {
    testCcsid("935", 33, "CHAR(32000) CCSID 935 ",  padExpected(ccsid935char23, 32000-23)); 
  }



  public void Var034() {
    testCcsid("939", 34, "VARCHAR(80) CCSID 939 ", ccsid930varchar); 
  }

  /* Test the larger types */ 
  public void Var035() {
    testCcsid("939", 35, "VARCHAR(16368) CCSID 939 ", ccsid930varchar); 
  }

  public void Var036() {
    testCcsid("939", 36, "VARCHAR(16369) CCSID 939 ", ccsid930varchar); 
  }

  public void Var037() {
    testCcsid("939", 37, "VARCHAR(16370) CCSID 939 ", ccsid930varchar); 
  }

  public void Var038() {
    testCcsid("939", 38, "VARCHAR(32000) CCSID 939 ", ccsid930varchar); 
  }

  
  public void Var039() {
    testCcsid( "939", 39, "CHAR(23) CCSID 939 ", ccsid930char23); 
  }


  public void Var040() {
    testCcsid( "939", 40, "CHAR(103) CCSID 939 ", padExpected(ccsid930char23, 80)); 
  }

  public void Var041() {
    testCcsid("939", 41, "CHAR(16368) CCSID 939 ",  padExpected(ccsid930char23, 16368-23)); 
  }

  public void Var042() {
    testCcsid("939", 42, "CHAR(16369) CCSID 939 ",  padExpected(ccsid930char23, 16369-23)); 
  }

  public void Var043() {
    testCcsid("939", 43, "CHAR(16370) CCSID 939 ",  padExpected(ccsid930char23, 16370-23)); 
  }

  public void Var044() {
    testCcsid("939", 44,  "CHAR(32000) CCSID 939 ",  padExpected(ccsid930char23, 32000-23)); 
  }



  public void Var045() {
    testCcsid("5026", 45, "VARCHAR(80) CCSID 5026 ", ccsid930varchar); 
  }

  /* Test the larger types */ 
  public void Var046() {
    testCcsid("5026", 46, "VARCHAR(16368) CCSID 5026 ", ccsid930varchar); 
  }

  public void Var047() {
    testCcsid("5026", 47, "VARCHAR(16369) CCSID 5026 ", ccsid930varchar); 
  }

  public void Var048() {
    testCcsid("5026", 48, "VARCHAR(16370) CCSID 5026 ", ccsid930varchar); 
  }

  public void Var049() {
    testCcsid("5026", 49, "VARCHAR(32000) CCSID 5026 ", ccsid930varchar); 
  }

  
  public void Var050() {
    testCcsid( "5026", 50, "CHAR(23) CCSID 5026 ", ccsid930char23); 
  }


  public void Var051() {
    testCcsid( "5026", 51, "CHAR(103) CCSID 5026 ", padExpected(ccsid930char23, 80)); 
  }

  public void Var052() {
    testCcsid("5026", 52, "CHAR(16368) CCSID 5026 ",  padExpected(ccsid930char23, 16368-23)); 
  }

  public void Var053() {
    testCcsid("5026", 53, "CHAR(16369) CCSID 5026 ",  padExpected(ccsid930char23, 16369-23)); 
  }

  public void Var054() {
    testCcsid("5026", 54, "CHAR(16370) CCSID 5026 ",  padExpected(ccsid930char23, 16370-23)); 
  }

  public void Var055() {
    testCcsid("5026", 55, "CHAR(32000) CCSID 5026 ",  padExpected(ccsid930char23, 32000-23)); 
  }




  public void Var056() {
    testCcsid("5035", 56, "VARCHAR(80) CCSID 5035 ", ccsid930varchar); 
  }

  /* Test the larger types */ 
  public void Var057() {
    testCcsid("5035", 57, "VARCHAR(16368) CCSID 5035 ", ccsid930varchar); 
  }

  public void Var058() {
    testCcsid("5035", 58, "VARCHAR(16369) CCSID 5035 ", ccsid930varchar); 
  }

  public void Var059() {
    testCcsid("5035", 59, "VARCHAR(16370) CCSID 5035 ", ccsid930varchar); 
  }

  public void Var060() {
    testCcsid("5035", 60, "VARCHAR(32000) CCSID 5035 ", ccsid930varchar); 
  }

  
  public void Var061() {
    testCcsid( "5035", 61, "CHAR(23) CCSID 5035 ", ccsid930char23); 
  }


  public void Var062() {
    testCcsid( "5035", 62, "CHAR(103) CCSID 5035 ", padExpected(ccsid930char23, 80)); 
  }

  public void Var063() {
    testCcsid("5035", 63, "CHAR(16368) CCSID 5035 ",  padExpected(ccsid930char23, 16368-23)); 
  }

  public void Var064() {
    testCcsid("5035", 64, "CHAR(16369) CCSID 5035 ",  padExpected(ccsid930char23, 16369-23)); 
  }

  public void Var065() {
    testCcsid("5035", 65, "CHAR(16370) CCSID 5035 ",  padExpected(ccsid930char23, 16370-23)); 
  }

  public void Var066() {
    testCcsid("5035", 66, "CHAR(32000) CCSID 5035 ",  padExpected(ccsid930char23, 32000-23)); 
  }


  
  private static String[][] ccsid1371varchar = {
    //check simple  
    {"UX'04192169691e2162'",                 "\u0419\u2169\u691e\u2162"},
    {"UX'0031003204192169691e216200390030'", "12\u0419\u2169\u691e\u216290",},
    {"UX'04192169222c30f100b121030020'",     "\u0419\u2169\u222c\u30f1\u00b1\u2103\u0020" },
    {"UX'5e025e035f17672a672b537859cb'",     "\u5e02\u5e03\u5f17\u672a\u672b\u5378\u59cb"},
    {"UX'8679500c89f4985382718f44977e52f1'", "\u8679\u500c\u89f4\u9853\u8271\u8f44\u977e\u52f1"},

  }; 

  
  static String[][] ccsid1371char23 = {

      {"UX'0419216961932162'","\u0419\u2169\u6193\u2162             "},
      {"UX'0031003204192169691e216200390030'","12\u0419\u2169\u691e\u216290         ",},
      {"UX'04192169222c30f100b121030020'",    "\u0419\u2169\u222c\u30f1\u00b1\u2103        " },
      {"UX'00205e0200205e0300205f170020672a'", " \u5e02 \u5e03 \u5f17 \u672a   "},
      {"UX'002086790020500c002089f400209853'", " \u8679 \u500c \u89f4 \u9853   "},


  };



  public void Var067() {
    testCcsid("1371", 67, "VARCHAR(80) CCSID 1371 ", ccsid1371varchar); 
  }

  public void Var068() {
    testCcsid("1371", 68, "VARCHAR(16368) CCSID 1371 ", ccsid1371varchar); 
  }

  public void Var069() {
    testCcsid("1371", 69, "VARCHAR(16369) CCSID 1371 ", ccsid1371varchar); 
  }

  public void Var070() {
    testCcsid("1371", 70, "VARCHAR(16370) CCSID 1371 ", ccsid1371varchar); 
  }

  public void Var071() {
    testCcsid("1371", 71, "VARCHAR(32000) CCSID 1371 ", ccsid1371varchar); 
  }
  
  public void Var072() {
    testCcsid( "1371", 72, "CHAR(23) CCSID 1371 ", ccsid1371char23); 
  }

  public void Var073() {
    testCcsid( "1371", 73, "CHAR(103) CCSID 1371 ", padExpected(ccsid1371char23, 80)); 
  }

  public void Var074() {
    testCcsid("1371", 74, "CHAR(16368) CCSID 1371 ",  padExpected(ccsid1371char23, 16368-23)); 
  }

  public void Var075() {
    testCcsid("1371", 75, "CHAR(16369) CCSID 1371 ",  padExpected(ccsid1371char23, 16369-23)); 
  }

  public void Var076() {
    testCcsid("1371", 76, "CHAR(16370) CCSID 1371 ",  padExpected(ccsid1371char23, 16370-23)); 
  }

  public void Var077() {
    testCcsid("1371", 77, "CHAR(32000) CCSID 1371 ",  padExpected(ccsid1371char23, 32000-23)); 
  }

  /* CCSId 933 Korean */


  private static String[][] ccsid933varchar = {
    //check simple  
    {"UX'04192169ffe52162'",                 "\u0419\u2169\uffe5\u2162"},
    {"UX'0031003204192169ffe5216200390030'", "12\u0419\u2169\uffe5\u216290",},
    {"UX'ff21ff10222c30f100b121030020'",     "\uff21\uff10\u222c\u30f1\u00b1\u2103\u0020" },
    {"UX'5e025e035f17672a672b59cb59cb'",     "\u5e02\u5e03\u5f17\u672a\u672b\u59cb\u59cb"},
    {"UX'8679867989f4827182718f44'", "\u8679\u8679\u89f4\u8271\u8271\u8f44"},

  }; 
 static String[][] ccsid933char23 = {

      {"UX'04192169ffe52162'","\u0419\u2169\uffe5\u2162             "},
      {"UX'0031003204192169ffe5216200390030'","12\u0419\u2169\uffe5\u216290         ",},
      {"UX'ff21ff10222c30f100b121030020'",    "\uff21\uff10\u222c\u30f1\u00b1\u2103         " },
      {"UX'00205e0200205e0300205f170020672a'", " \u5e02 \u5e03 \u5f17 \u672a   "},
      {"UX'0020867900208679002089f400208271'", " \u8679 \u8679 \u89f4 \u8271   "},

 
  };

  public void Var078() {
    testCcsid("933", 78, "VARCHAR(80) CCSID 933 ", ccsid933varchar); 
  }

  public void Var079() {
    testCcsid("933", 79, "VARCHAR(16368) CCSID 933 ", ccsid933varchar); 
  }

  public void Var080() {
    testCcsid("933", 80, "VARCHAR(16369) CCSID 933 ", ccsid933varchar); 
  }

  public void Var081() {
    testCcsid("933", 81, "VARCHAR(16370) CCSID 933 ", ccsid933varchar); 
  }

  public void Var082() {
    testCcsid("933", 82, "VARCHAR(32000) CCSID 933 ", ccsid933varchar); 
  }
  
  public void Var083() {
    testCcsid( "933", 83, "CHAR(23) CCSID 933 ", ccsid933char23); 
  }

  public void Var084() {
    testCcsid( "933", 84, "CHAR(103) CCSID 933 ", padExpected(ccsid933char23, 80)); 
  }

  public void Var085() {
    testCcsid("933", 85,"CHAR(16368) CCSID 933 ",  padExpected(ccsid933char23, 16368-23)); 
  }

  public void Var086() {
    testCcsid("933", 86, "CHAR(16369) CCSID 933 ",  padExpected(ccsid933char23, 16369-23)); 
  }

  public void Var087() {
    testCcsid("933", 87, "CHAR(16370) CCSID 933 ",  padExpected(ccsid933char23, 16370-23)); 
  }

  public void Var088() {
    testCcsid("933", 88, "CHAR(32000) CCSID 933 ",  padExpected(ccsid933char23, 32000-23)); 
  }


}
