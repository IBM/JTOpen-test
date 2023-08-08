///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSGetStringBIDI.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test;


import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;

/**
 * Testcase JDRSGetStringBIB. This tests the following method of the JDBC
 * ResultSet class with BIDI data
 * 
 * <ul>
 * <li>getString()
 * </ul>
 **/
public class JDRSGetStringBIDI extends JDTestcase {

  // Private data.
  private String properties_ = "NOTSET";
  private static String table_ = JDRSTest.COLLECTION + ".JDRSGSBIDI";

  /**
   * Constructor.
   **/
  public JDRSGetStringBIDI(AS400 systemObject, Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password) {
    super(systemObject, "JDRSGetStringBIDI", namesAndVars, runMode,
        fileOutputStream, password);
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    reconnect("");
    table_ = JDRSTest.COLLECTION + ".JDRSGSBIDI";
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

  public void testCcsid(int var, String properties,  String ccsid, String dataType, String[][] values) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    try {
      
      reconnect(properties); 
      
      String sql;
      String table = JDRSTest.COLLECTION + ".JDRSGSB" +var+"C"+ ccsid;
      Statement stmt = connection_.createStatement();
      initTable(stmt, table , "(KEY int, C1 " + dataType + ")", sb); 
      
      for (int i = 0; i < values.length; i++) {
        sql = "INSERT INTO " + table + " VALUES(" + i + "," + values[i][0]
            + ")";
        sb.append("Executing " + sql + "\n");
        stmt.executeUpdate(sql);
      }
      sql = "SELECT C1, HEX(C1), CAST(C1 AS DBCLOB(1G) CCSID 1200) FROM " + table + " order by KEY";
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
            compareOk = false; 
        }
        if (!expectedString.equals(retrievedUnicode)) {
          sb.append("TESTCASE ERROR: Expected != retrievedUnicode\n" ); 
          compareOk = false; 
      }
        
        if (!compareOk) {   
          passed = false;
          sb.append("["+i+"]       Got  "
              + JDTestUtilities.getMixedString(retrievedString) + "\n");
          sb.append("["+i+"]   expected "
              + JDTestUtilities.getMixedString(expectedString) + "\n");
          sb.append("["+i+"]       from "
              + JDTestUtilities.getMixedString(values[i][0]) + "\n");
          sb.append("["+i+"]       hex  "
              + retrievedHex + "\n");
          sb.append("["+i+"]       1200 "
              + JDTestUtilities.getMixedString(retrievedUnicode) + "\n");
        }
        i++;
      }

      stmt.close();
      assertCondition(passed,sb);

    } catch (Exception e) {
      failed(e, "Unexpected Exception == " + sb.toString());
    }

  }

  /**
   * getString() - Test CCSID 424
   **/
  
  private static String[][] ccsid424varchar = {
    //  Note:  Unicode is contextual, so it if starts with ABC
    //  the translation to 424 with begin with ABC on the left (instead of on the right)
    //  When transforming back the ABC is not less signicant. 
    //  We make sure everything starts with a bidi character
    //check simple  
    {"UX'05D0004100420043002005D005D105D2'","\u05D0ABC \u05D0\u05D1\u05D2"},
    {"UX'05D005D105D20020004100420043'","\u05D0\u05D1\u05D2 ABC",},
    
    //check round trip
    {"UX'05D000200041004200430020003100320033002005D005D105D2'", "\u05D0 123 ABC \u05D0\u05D1\u05D2",}, /* did not round trip */ 
    {"UX'05D00020004100420043002005D005D105D20020003100320033'", "\u05D0 ABC \u05D0\u05D1\u05D2 123",},
    {"UX'05D005D105D200200041004200430020003100320033'", "\u05D0\u05D1\u05D2 123 ABC",},  /* did not round trip */
    {"UX'05D005D105D200200031003200330020004100420043'", "\u05D0\u05D1\u05D2 123 ABC",},
    {"UX'05D0003100320033002005D005D105D20020004100420043'", "\u05D0123 \u05D0\u05D1\u05D2 ABC",},
    {"UX'05D00031003200330020004100420043002005D005D105D2'", "\u05D0123 ABC \u05D0\u05D1\u05D2",},
    //check apostrophes
    {"UX'00410027002005D005D105D20020003100320033'", "\u05D0\u05D1\u05D2 123 'A",},  /* did not round trip */ 
    {"UX'05D0002705D200200031003200330027'", "\u05D0'\u05D2 123'",},
    {"UX'004105D00030003100320033'", "\u05D0"+"A0123",},  /* did not round trip */ 
    
    //check neutrals
    {"UX'0026002A002500240023'", "&*%$#",},
    
    //check contextual
    {"UX'05D005D105D20020003100320033002005D305D405D5'", "\u05D0\u05D1\u05D2 123 \u05D3\u05D4\u05D5",},
    {"'ABC 123 DEF'", "ABC 123 DEF",},
    {"UX'00410042004300200031003200330020004400450046'", "ABC 123 DEF",},


  }; 

  public void Var001() {
    testCcsid(1, "bidi string type=10","424",  "VARCHAR(80) CCSID 424 ", ccsid424varchar); 
  }

  static String[][] ccsid424char23 = {
/*0*/ {"UX'002005d405d505e605d005d505ea002005d305d905e805e705d805d505e805d905dd002d05d005d705e805d905dd'",
    "\u0020\u05d4\u05d5\u05e6\u05d0\u05d5\u05ea\u0020\u05d3\u05d9\u05e8\u05e7\u05d8\u05d5\u05e8\u05d9\u05dd\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",},
    {"UX'0020002000200020002005d405d505e605d005d505ea002005d105d905d805d505d7002d05d005d705e805d905dd'",
    "\u0020\u0020\u0020\u0020\u0020\u05d4\u05d5\u05e6\u05d0\u05d5\u05ea\u0020\u05d1\u05d9\u05d8\u05d5\u05d7\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",},
    {"UX'05e105d505db05df002005dc05dc05d0002005e905d905d505da002005d705ea05dd002d05d005d705e805d905dd'",
    "\u05e1\u05d5\u05db\u05df\u0020\u05dc\u05dc\u05d0\u0020\u05e9\u05d9\u05d5\u05da\u0020\u05d7\u05ea\u05dd\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",},
/*3*/ {"UX'05d105d905d805d505d7002005de05d505e005d905d505ea002d05d005d705e805d905dd'",
    "\u0020\u0020\u0020\u0020\u0020\u05d1\u05d9\u05d8\u05d5\u05d7\u0020\u05de\u05d5\u05e0\u05d9\u05d5\u05ea\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",}, /* insert into fixed width puts padding on wrong size */ 
/*4*/ {"UX'05e205e805d105d505d905d505ea002005d705d505e7002005de05db05e8002d05d005d705e805d905dd'",
    "\u0020\u0020\u05e2\u05e8\u05d1\u05d5\u05d9\u05d5\u05ea\u0020\u05d7\u05d5\u05e7\u0020\u05de\u05db\u05e8\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",},
/*5*/ {"UX'05de05d105d805d705d905dd002d05d305de05d9002005de05d705dc05d4002d05d005d705e805d905dd'",
    "  \u05de\u05d1\u05d8\u05d7\u05d9\u05dd\u002d\u05d3\u05de\u05d9\u0020\u05de\u05d7\u05dc\u05d4\u002d\u05d0\u05d7\u05e8\u05d9\u05dd",},
    {"UX'00200020002000200020002000200020002000200020002005d905e905d905e805d905dd002d05de05e805db05d6'",
    "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u05d9\u05e9\u05d9\u05e8\u05d9\u05dd\u002d\u05de\u05e8\u05db\u05d6",},
    {"UX'00200020002000200020002000200020002005dc05d0002005e405e205d905dc05d905dd002d05de05e805db05d6'",
    "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u05dc\u05d0\u0020\u05e4\u05e2\u05d9\u05dc\u05d9\u05dd\u002d\u05de\u05e8\u05db\u05d6",},
/*8*/ {"UX'05e205e805d105d505d905d505ea0020002d002005d005d705e805d905dd'",
    "        \u05e2\u05e8\u05d1\u05d5\u05d9\u05d5\u05ea\u0020\u002d\u0020\u05d0\u05d7\u05e8\u05d9\u05dd",},
      
  };
  
  public void Var002() {
    testCcsid(2, "bidi string type=10", "424", "CHAR(23) CCSID 424 ", ccsid424char23); 
  }

  private static String[][] ccsid62211varchar = {
    //  Note:  Unicode is contextual, so it if starts with ABC
    //  the translation to 424 with begin with ABC on the left (instead of on the right)
    //  When transforming back the ABC is not less signicant. 
    //  We make sure everything starts with a bidi character
    //check simple  
    {"UX'05D0004100420043002005D005D105D2'","\u05D0ABC \u05D0\u05D1\u05D2"},
    {"UX'05D005D105D20020004100420043'","\u05D0\u05D1\u05D2 ABC",},
    
    //check round trip
    {"UX'05D000200041004200430020003100320033002005D005D105D2'", "\u05D0 ABC 123 \u05D0\u05D1\u05D2",},  
    {"UX'05D00020004100420043002005D005D105D20020003100320033'", "\u05D0 ABC \u05D0\u05D1\u05D2 123",},
    {"UX'05D005D105D200200041004200430020003100320033'", "\u05D0\u05D1\u05D2 ABC 123",},  
    {"UX'05D005D105D200200031003200330020004100420043'", "\u05D0\u05D1\u05D2 123 ABC",},
    {"UX'05D0003100320033002005D005D105D20020004100420043'", "\u05D0123 \u05D0\u05D1\u05D2 ABC",},
    {"UX'05D00031003200330020004100420043002005D005D105D2'", "\u05D0123 ABC \u05D0\u05D1\u05D2",},
    //check apostrophes
    {"UX'00410027002005D005D105D20020003100320033'", "A' \u05D0\u05D1\u05D2 123",},  
    {"UX'05D0002705D200200031003200330027'", "\u05D0'\u05D2 123'",},
    {"UX'004105D00030003100320033'", "A\u05D0"+"0123",},   
    
    //check neutrals
    {"UX'0026002A002500240023'", "&*%$#",},
    
    //check contextual
    {"UX'05D005D105D20020003100320033002005D305D405D5'", "\u05D0\u05D1\u05D2 123 \u05D3\u05D4\u05D5",},
    {"'ABC 123 DEF'", "ABC 123 DEF",},
    {"UX'00410042004300200031003200330020004400450046'", "ABC 123 DEF",},


  }; 

    private static String[][] ccsid62235varchar = {
    //  Note:  Unicode is contextual, so it if starts with ABC
    //  the translation to 424 with begin with ABC on the left (instead of on the right)
    //  When transforming back the ABC is not less signicant. 
    //  We make sure everything starts with a bidi character
    //check simple  
    {"UX'05D0004100420043002005D005D105D2'","\u05D0ABC \u05D0\u05D1\u05D2"},
    {"UX'05D005D105D20020004100420043'","\u05D0\u05D1\u05D2 ABC",},
    
    //check round trip
    {"UX'05D000200041004200430020003100320033002005D005D105D2'", "\u05D0 ABC 123 \u05D0\u05D1\u05D2",},  
    {"UX'05D00020004100420043002005D005D105D20020003100320033'", "\u05D0 ABC \u05D0\u05D1\u05D2 123",},
    {"UX'05D005D105D200200041004200430020003100320033'", "\u05D0\u05D1\u05D2 ABC 123",},  
    {"UX'05D005D105D200200031003200330020004100420043'", "\u05D0\u05D1\u05D2 123 ABC",},
    {"UX'05D0003100320033002005D005D105D20020004100420043'", "\u05D0123 \u05D0\u05D1\u05D2 ABC",},
    {"UX'05D00031003200330020004100420043002005D005D105D2'", "\u05D0123 ABC \u05D0\u05D1\u05D2",},
    //check apostrophes
    {"UX'00410027002005D005D105D20020003100320033'", "A' \u05D0\u05D1\u05D2 123",},  
    {"UX'05D0002705D200200031003200330027'", "\u05D0'\u05D2 123'",},
    {"UX'004105D00030003100320033'", "A\u05D0"+"0123",},   
    
    //check neutrals
    {"UX'0026002A002500240023'", "&*%$#",},
    
    //check contextual
    {"UX'05D005D105D20020003100320033002005D305D405D5'", "\u05D0\u05D1\u05D2 123 \u05D3\u05D4\u05D5",},
    {"'ABC 123 DEF'", "ABC 123 DEF",},
    {"UX'00410042004300200031003200330020004400450046'", "ABC 123 DEF",},


  }; 

  
  public void Var003() {
    testCcsid(3, "bidi string type=5","62211", "VARCHAR(80) CCSID 62211 ", ccsid62211varchar); 
  }

  public void Var004() {
      /* Neutrals are reversed for  CCSID 62235 for toolbox */
      /* need to set string type = 6 for unicode to match */
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
	  testCcsid(4,"bidi string type=6","62235", "VARCHAR(80) CCSID 62235 ", ccsid62235varchar);
      } else {
	  testCcsid(4,"bidi string type=10","62235", "VARCHAR(80) CCSID 62235 ", ccsid62211varchar);

      } 

  }

  
  public void Var005() {
    testCcsid(5,"bidi string type=10","62245", "VARCHAR(80) CCSID 62245 ", ccsid62211varchar); 
  }
  
  /* Larger types */ 

  public void Var006() {
    testCcsid(6,"bidi string type=10","424", "VARCHAR(16000) CCSID 424 ", ccsid424varchar); 
  }
  public void Var007() {
    testCcsid(7,"bidi string type=5","62211", "VARCHAR(16000) CCSID 62211 ", ccsid62211varchar); 
  }

  public void Var008() {
      /* Neutrals are reversed for  CCSID 62235 for toolbox */
      /* need to set string type = 6 for unicode to match */
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
    testCcsid(8,"bidi string type=6","62235", "VARCHAR(16000) CCSID 62235 ", ccsid62235varchar);
      } else {
    testCcsid(8,"bidi string type=10","62235", "VARCHAR(16000) CCSID 62235 ", ccsid62211varchar);

      } 

  }

  
  public void Var009() {
    testCcsid(9,"bidi string type=10","62245", "VARCHAR(16000) CCSID 62245 ", ccsid62211varchar); 
  }


  
  public void Var010() {
    testCcsid(10,"bidi string type=10","424", "VARCHAR(32000) CCSID 424 ", ccsid424varchar); 
  }
  public void Var011() {
    testCcsid(11,"bidi string type=5","62211", "VARCHAR(32000) CCSID 62211 ", ccsid62211varchar); 
  }

  public void Var012() {
      /* Neutrals are reversed for  CCSID 62235 for toolbox */
      /* need to set string type = 6 for unicode to match */
      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) { 
    testCcsid(12,"bidi string type=6","62235", "VARCHAR(32000) CCSID 62235 ", ccsid62235varchar);
      } else {
    testCcsid(12,"bidi string type=10","62235", "VARCHAR(32000) CCSID 62235 ", ccsid62211varchar);

      } 

  }

  
  public void Var013() {
    testCcsid(13,"bidi string type=10","62245", "VARCHAR(32000) CCSID 62245 ", ccsid62211varchar); 
  }


  
  
}
