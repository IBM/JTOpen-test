///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetStringUnsupported.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.RS;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

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
public class JDRSGetStringUnsupported extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSGetStringUnsupported";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }

  // Private data.
  private String properties_ = "NOTSET";
  private static String table_ = JDRSTest.COLLECTION + ".JDRSGSUNSP";

  /**
   * Constructor.
   **/
  public JDRSGetStringUnsupported(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetStringUnsupported", namesAndVars, runMode,
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
    table_ = JDRSTest.COLLECTION + ".JDRSGSUNSP";
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

      String url = baseURL_ ;
      if (getDriver() != JDTestDriver.DRIVER_JCC) {
        url += ";" + properties_;
      }
      connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
    }
  }

  public void testCcsid( int var, String ccsid,  String dataType, String[][] values) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    try {
      
      reconnect(""); 
      
      String sql;
      String table = JDRSTest.COLLECTION + ".JDRSGSU"+ var +"C"+ ccsid;
      Statement stmt = connection_.createStatement();
      initTable(stmt, table,  "(KEY int, C1 " + dataType + ")", sb); 
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
          sb.append("TESTCASE ERROR: Expected != retrievedUnicode\n" ); 
          compareOk = false; 
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
   * getString() - Test CCSID 1047
   **/
  /* Note:  found all chars using the following query */
  /*  call qsys2.qcmdexc('chgjob ccsid(1047)')            */
  /* select  jdjstpq730.allchars() from sysibm.sysdummy1  */

/* 
002000a000e200e400e000e100e300e500e700f100a2002e003c0028002b007c
002600e900ea00eb00e800ed00ee00ef00ec00df00210024002a0029003b005e
002d002f00c200c400c000c100c300c500c700d100a6002c0025005f003e003f
00f800c900ca00cb00c800cd00ce00cf00cc0060003a002300400027003d0022
00d800610062006300640065006600670068006900ab00bb00f000fd00fe00b1
00b0006a006b006c006d006e006f00700071007200aa00ba00e600b800c600a4
00b5007e0073007400750076007700780079007a00a100bf00d0005b00de00ae
00ac00a300a500b700a900a700b600bc00bd00be00dd00a800af005d00b400d7
007b00410042004300440045004600470048004900ad00f400f600f200f300f5
007d004a004b004c004d004e004f00500051005200b900fb00fc00f900fa00ff
005c00f70053005400550056005700580059005a00b200d400d600d200d300d5
003000310032003300340035003600370038003900b300db00dc00d900da009f
*/

  private static String[][] ccsid1047char16 = {

      {"UX'002000a000e200e400e000e100e300e500e700f100a2002e003c0028002b007c'",
      "\u0020\u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2\u002e\u003c\u0028\u002b\u007c"}, 
      {"UX'002600e900ea00eb00e800ed00ee00ef00ec00df00210024002a0029003b005e'",
      "\u0026\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u0021\u0024\u002a\u0029\u003b\u005e"}, 
      {"UX'002d002f00c200c400c000c100c300c500c700d100a6002c0025005f003e003f'",
      "\u002d\u002f\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6\u002c\u0025\u005f\u003e\u003f"}, 
      {"UX'00f800c900ca00cb00c800cd00ce00cf00cc0060003a002300400027003d0022'",
      "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u0060\u003a\u0023\u0040\u0027\u003d\""}, 
      {"UX'00d800610062006300640065006600670068006900ab00bb00f000fd00fe00b1'",
      "\u00d8\u0061\u0062\u0063\u0064\u0065\u0066\u0067\u0068\u0069\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"}, 
      {"UX'00b0006a006b006c006d006e006f00700071007200aa00ba00e600b800c600a4'",
      "\u00b0\u006a\u006b\u006c\u006d\u006e\u006f\u0070\u0071\u0072\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"}, 
      {"UX'00b5007e0073007400750076007700780079007a00a100bf00d0005b00de00ae'",
      "\u00b5\u007e\u0073\u0074\u0075\u0076\u0077\u0078\u0079\u007a\u00a1\u00bf\u00d0\u005b\u00de\u00ae"}, 
      {"UX'00ac00a300a500b700a900a700b600bc00bd00be00dd00a800af005d00b400d7'",
      "\u00ac\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00dd\u00a8\u00af\u005d\u00b4\u00d7"}, 
      {"UX'007b00410042004300440045004600470048004900ad00f400f600f200f300f5'",
      "\u007b\u0041\u0042\u0043\u0044\u0045\u0046\u0047\u0048\u0049\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"}, 
      {"UX'007d004a004b004c004d004e004f00500051005200b900fb00fc00f900fa00ff'",
      "\u007d\u004a\u004b\u004c\u004d\u004e\u004f\u0050\u0051\u0052\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"}, 
      {"UX'005c00f70053005400550056005700580059005a00b200d400d600d200d300d5'",
      "\\\u00f7\u0053\u0054\u0055\u0056\u0057\u0058\u0059\u005a\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"}, 
      {"UX'003000310032003300340035003600370038003900b300db00dc00d900da009f'",
      "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037\u0038\u0039\u00b3\u00db\u00dc\u00d9\u00da\u009f"}, 


  }; 

  public void Var001() {
    testCcsid(1,"1047", "VARCHAR(80) CCSID 1047 ", ccsid1047char16); 
  }

  public void Var002() {
    testCcsid(2, "1047", "CHAR(16) CCSID 1047 ", ccsid1047char16); 
  }
 
  public void Var003() {
    testCcsid(3,"1047", "VARCHAR(16368) CCSID 1047 ", ccsid1047char16); 
  }

  public void Var004() {
    testCcsid(4,"1047", "VARCHAR(16369) CCSID 1047 ", ccsid1047char16); 
  }

  public void Var005() {
    testCcsid(5,"1047", "VARCHAR(16370) CCSID 1047 ", ccsid1047char16); 
  }

  public void Var006() {
    testCcsid(6,"1047", "VARCHAR(32000) CCSID 1047 ", ccsid1047char16); 
  }
 

  public void Var007() {
    testCcsid(7, "1047", "CHAR(23) CCSID 1047 ", padExpected(ccsid1047char16, 23-16)); 
  }

  
  public void Var008() {
    testCcsid(8, "1047", "CHAR(103) CCSID 1047 ", padExpected(ccsid1047char16, 103-16)); 
  }

  public void Var009() {
    testCcsid(9, "1047", "CHAR(16368) CCSID 1047 ",  padExpected(ccsid1047char16, 16368-16)); 
  }

  public void Var010() {
    testCcsid(10, "1047", "CHAR(16369) CCSID 1047 ",  padExpected(ccsid1047char16, 16369-16)); 
  }

  public void Var011() {
    testCcsid(11, "1047", "CHAR(16370) CCSID 1047 ",  padExpected(ccsid1047char16, 16370-16)); 
  }

  public void Var012() {
    testCcsid(12, "1047", "CHAR(32000) CCSID 1047 ",  padExpected(ccsid1047char16, 32000-16)); 
  }


  
}
