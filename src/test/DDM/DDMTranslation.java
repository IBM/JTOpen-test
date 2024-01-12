///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMTranslation.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.ExtendedIllegalStateException;

/**
 *Testcase DDMTranslation.  This test class verifies that CCSID
 *translation is correct.
**/
public class DDMTranslation extends Testcase
{

  String testLib_ = null;
  Connection conn_ = null;
  Statement stmt_ = null; 

  String[][] ccsid424test= {
      { "\u0020\u05d7\u05d9\u05d5\u05d1\u0020\u05d7\u05d5\u05d3\u05e9\u05d9",
      "4051694446484042465148"}
  };


  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMTranslation(AS400            systemObject,
                    Hashtable        variationsToRun,
                    int              runMode,
                    FileOutputStream fileOutputStream,
                    String testLib)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMTranslation", 
          variationsToRun, runMode, fileOutputStream);
    testLib_ = testLib;
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    try
    {
      // Delete and recreate library DDMTEST
      CommandCall c = new CommandCall(systemObject_);
      deleteLibrary(c, testLib_);
      c.run("CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      // com.ibm.as400.access.AS400JDBCDriver driver; 
       
      AS400JDBCDriver driver = new AS400JDBCDriver();
      conn_ = driver.connect(systemObject_); 

      stmt_ = conn_.createStatement();
      
    }
    catch(Exception e)
    {
	System.out.println("Warning:   exception thrown during setup");
	System.out.flush(); 
      e.printStackTrace(output_);
      System.err.flush();
      System.out.flush(); 
      throw e;
    }
  }


  private static String getUnicodeString( String outString) {
    StringBuffer sb = new StringBuffer(); 
    //
    // See if all the characters are 7 bit ASCII.. If so just print
    //
    if (outString != null) { 
        char chars[] = outString.toCharArray();
        boolean nonAsciiFound = false; 
        for (int i = 0; !nonAsciiFound && i < chars.length; i++) {
            if ( chars[i] != 0x0d && chars[i] != 0x0a && chars[i] != 0x09 && (chars[i] >= 0x7F || chars[i] < 0x20) ) {
                nonAsciiFound = true; 
            } 
        }
        if (! nonAsciiFound) {
            sb.append(outString);
        } else {
          sb.append("U'");
          for     (int i = 0; i < chars.length; i++) {
            int showInt = chars[i] & 0xFFFF;
            
            String showString = Integer.toHexString(showInt); 
            if (showInt >= 0x1000) {
              sb.append(showString); 
            } else if (showInt >= 0x0100) {
              sb.append("0"+showString);
            } else if (showInt >= 0x0010) {
              sb.append("00"+showString);
            } else {
              sb.append("000"+showString);
            }
          }
          sb.append("'");
        }
    } else {
        sb.append(outString); 
    } 
    return sb.toString(); 
}



  

  public boolean checkHexValues(String sql, String info, String[][] testStrings, StringBuffer sb ) throws SQLException  {
    boolean passed = true; 
    ResultSet rs = stmt_.executeQuery(sql);
    for (int i = 0; i < testStrings.length; i++) {
        if (rs.next()) {
            String hexString = rs.getString(1); 
            if (!testStrings[i][1].equals(hexString)) {
                passed = false;
                sb.append(info+i+
                        "\nexpected '"+testStrings[i][1]+
                       "'\n     got '"+hexString+"'\n"); 
            } 
        } else {
            passed = false; 
            sb.append("Row "+i+" not found when querying hex\n"); 
        } 
    }
    rs.close(); 
    return passed; 
  }

  /**
   * Test a translation.
   */

  public void testTranslation(String ccsid, String[][] testStrings) {

      String tablename = "DDMC"+ccsid;
      String fullTablename = testLib_+"."+tablename; 
      String sql = ""; 
      boolean passed = true;
      StringBuffer sb = new StringBuffer("\n"); 
      
      try {
	  sql = "DROP TABLE "+fullTablename;
	  try {
	      stmt_.executeUpdate(sql); 
	  } catch(Exception e) {
	      String msg = e.toString();
	      String expected = "not found"; 
	      if (msg.indexOf(expected) > 0) {
	      } else {
		  System.out.println("Warning:  exception missing "+expected);
		  e.printStackTrace(); 
	      } 
	  } 
	  sql = "CREATE TABLE "+fullTablename+" (C1 VARCHAR(100) CCSID "+ccsid+")";
	  stmt_.executeUpdate(sql);


	  //
	  // Test the read path first
	  //
	  sql = "INSERT INTO "+fullTablename+" VALUES(?)"; 
	  PreparedStatement pstmt = conn_.prepareStatement(sql);
	  for (int i = 0; i < testStrings.length; i++) { 
	      pstmt.setString(1, testStrings[i][0]);
	      pstmt.executeUpdate();
	  }
	  pstmt.close(); 

	  sql = "SELECT HEX(C1) FROM "+fullTablename;
	  if (!checkHexValues(sql, "For read record ", testStrings, sb)) {
	    passed = false; 
	  }
	  sql = "" ; 

	  QSYSObjectPathName fileName = new QSYSObjectPathName(testLib_, tablename, "FILE");
	  SequentialFile file = new SequentialFile(systemObject_, fileName.getPath());
	  AS400FileRecordDescription recordDescription = new AS400FileRecordDescription(systemObject_, fileName.getPath());
	  Record record = null;
	  RecordFormat recordFormat =recordDescription.retrieveRecordFormat()[0];  
	  file.setRecordFormat(recordFormat);
	  file.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
	  for (int i = 0; i < testStrings.length; i++) { 
	      record = file.readNext();
	      Object[] fields = record.getFields();
	      String bidiString = fields[0].toString();
	      if (!testStrings[i][0].equals(bidiString)) {
		  passed = false;
		  sb.append("\nFor read record "+i+
		            "\n expected '"+getUnicodeString(testStrings[i][0])+
		           "'\n      got '"+getUnicodeString(bidiString)+"'\n"); 
	      } 
	  } 
	  file.close(); 
	  
	  //
	  // Now test the write path
	  //
	  sql = "delete from "+fullTablename; 
	  stmt_.executeUpdate(sql); 
	  sql=""; 
	  
          file.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_ALL);
          record = new Record(recordFormat);
          for (int i = 0; i < testStrings.length; i++) {
            record.setField(0, testStrings[i][0] ); 
            file.write(record); 
           
          } 
          file.close(); 
          
          
          sql = "SELECT HEX(C1) FROM "+fullTablename;
          if (!checkHexValues(sql, "For write record ", testStrings, sb)) {
            passed = false; 
          }
          sql = "" ; 
	  
	  
	  
	  assertCondition(passed, sb.toString()); 

	  
      } catch(Exception e) {
	  failed(e, "Unexpected exception sql='"+sql+"'");
      }

  } 
  



  /**
   * Test CCSID 424
   *
  **/
  public void Var001()  {
      testTranslation("424", ccsid424test);
  }

}


