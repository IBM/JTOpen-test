///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementStressCCSID.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementStressTest.java
//
// Classes:      JDStatementStressTest
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.StringTokenizer;



/**
Testcase JDStatementStressTest.  This tests multithreaded
use of parts of the JDBC driver.

This also stresses the use of different CCSIDs.. 

**/
public class JDStatementStressCCSID
extends JDTestcase
{

  

    class CCSIDTestData {
	public int ccsid;
	public String data;
	public CCSIDTestData(int ccsid, String data) {
	    this.ccsid = ccsid;
	    this.data = data; 
	} 
    }


    private static final int PROBABILITY = 3;
    private static final int AGE = 1;


    // Private data.
    protected Connection          connection_;
    protected Statement           statement_;
    
    



/**
Constructor.
**/
    public JDStatementStressCCSID (AS400 systemObject,
                        Hashtable namesAndVars,
                        int runMode,
                        FileOutputStream fileOutputStream,
                        
                        String password,
                        String miscParm)
    {
        super (systemObject, "JDStatementStressTest",
            namesAndVars, runMode, fileOutputStream,
            password);

    }

  public JDStatementStressCCSID(AS400 systemObject, String testname,  Hashtable namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String miscParm) {
    super(systemObject, testname, namesAndVars, runMode,
        fileOutputStream, password);

  }

/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    public void setup ()
        throws Exception
    {

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          // JCC doesn't have these properties
          connection_ = DriverManager.getConnection (baseURL_ ,
              systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_)));

        } else { 
        connection_ = DriverManager.getConnection (baseURL_ + ";errors=full;lazy close=true",
                                                   systemObject_.getUserId(), new String(PasswordVault.decryptPassword(encryptedPassword_)));
        }
        
        statement_ = connection_.createStatement();

	String QIWS = JDSetupProcedure.setupQIWS(systemObject_,  connection_);
	

        connection_.commit();
        statement_.close();




    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    public void cleanup ()
        throws Exception
    {
        statement_ = connection_.createStatement();
    
        statement_.close ();
        connection_.close ();
    }


  public boolean testUnicodeRange(Connection connection, 
      String tableName, String columnDefinition, int unicodeStart, int unicodeEnd, int charsPerRow, boolean doubleByteCcsid, StringBuffer sb) throws SQLException {
      String sql; 
      boolean passed = true; 
      
      Hashtable insertHash = new Hashtable();
      
      Statement stmt = connection.createStatement(); 
      
      initTable(stmt, tableName, "(startRange int, endRange int, unicodeLit "+columnDefinition+", parameterMarker "+columnDefinition+")",sb);

      StringBuffer sqlBuffer = new StringBuffer();  
      StringBuffer valueBuffer = new StringBuffer(); 
      for (int currentStart = unicodeStart; currentStart <= unicodeEnd; currentStart += charsPerRow) { 
        int currentEnd = currentStart + charsPerRow-1; 
        if (currentEnd > unicodeEnd) {
          currentEnd = unicodeEnd; 
        }
        
        PreparedStatement pstmt; 
        sqlBuffer.setLength(0); 
        valueBuffer.setLength(0); 
        
        sqlBuffer.append("INSERT INTO ");
        sqlBuffer.append(tableName); 
        sqlBuffer.append(" VALUES(?,?,UX'");
        for (int i = currentStart; i <= currentEnd; i++) {
          valueBuffer.append((char)i); 
          String hexValue = Integer.toHexString(i);
          int length = hexValue.length(); 
          while (length < 4) {
            sqlBuffer.append('0'); 
            length++; 
          }
          sqlBuffer.append(hexValue); 
        }
        
        sqlBuffer.append("',?)");
        sql = sqlBuffer.toString();
        // Don't trace these -- it is excessive 
        // sb.append("Running "+sql+"\n");
        try { 
        insertHash.put(""+currentStart, sql); 
        pstmt = connection.prepareStatement(sql); 
        pstmt.setInt(1,currentStart);
        pstmt.setInt(2,currentEnd); 
        pstmt.setString(3, valueBuffer.toString()); 
        pstmt.executeUpdate();        
        pstmt.close(); 
        } catch (SQLException e) {
          sb.append("Running "+sql+"\n");
          sb.append("  Parameter 1 was "+currentStart+"\n"); 
          sb.append("  Parameter 2 was "+currentEnd+"\n");
      
          sb.append("  Parameter 3 was UX'"+ showStringAsHex(valueBuffer.toString())+"'\n");
            
          throw e; 
        }
      } /* currentStart */ 
    sb.append("-----------------------------------------------------------------\n");
    sb.append("--  Checking INPUT path -----------------------------------------\n");
    sb.append("-----------------------------------------------------------------\n");

    sql = "select startRange, endRange, "
        + "cast(unicodeLit as VARGRAPHIC(10000) CCSID 1200), "
        + "cast(parameterMarker as VARGRAPHIC(10000) CCSID 1200), "
        + "hex(unicodeLit), hex(parameterMarker) " + "from " + tableName
        + " where unicodeLit <> parameterMarker";
    sb.append("Running " + sql + "\n");
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {
      String unicodeString = rs.getString(3);
      String parameterString = rs.getString(4);
      int rangeStart = rs.getInt(1); 
      int rangeEnd   = rs.getInt(2);
      // Check for case where 0x0110 is being mapped to 0x00d0 for
      // parameter marker case. 
      // This happens with the native JDBC driver and the PASE
      // translation does not do this in 7.2 and 7.3 (and maybe later). 
      if (rangeStart == 0x101) {
        char uPart = unicodeString.charAt(0x110-0x101);
        char pPart = parameterString.charAt(0x110-0x101); 
        if (uPart == '\u00d0' &&
            pPart == '\u001a') {
          unicodeString = unicodeString.replace('\u00d0','\u001a'); 
        }
      }

      if (!isSameIgnoringSubstitution(rangeStart, unicodeString, parameterString,columnDefinition)) {

        String unicodeDumpString = JDTestUtilities.dumpBytes(unicodeString);
        String parameterDumpString = JDTestUtilities.dumpBytes(parameterString);
        
        
        
        sb.append(" For "+insertHash.get(""+rangeStart)+"\n"); 
        passed = false;
        sb.append(" Failed for 0x" + Integer.toHexString(rangeStart)
            + " - 0x" + Integer.toHexString(rangeEnd) + "\n");
        sb.append("  origUTF16   = " );
        dumpHexRange(sb, rangeStart, rangeEnd); 
        sb.append("\n"); 
        sb.append("  literal     = " + unicodeDumpString + "\n");
        sb.append("    diff      = "
            + markDifferences(unicodeDumpString, parameterDumpString) + "\n");
        sb.append("  pMarker     = " + parameterDumpString + "\n");
        sb.append("  literalHex  = "); 
        padSingleByteHex(sb, rs.getString(5)); 
        sb.append("\n");
        // sb.append("    differences      = "+markDifferences(rs.getString(5),
        // rs.getString(6))+"\n");
        sb.append("  pMarkerHex  = ");
        padSingleByteHex(sb, rs.getString(6)); 
        sb.append("\n");
      }
    }
    rs.close(); 
    
    sb.append("-----------------------------------------------------------------\n");
    sb.append("--  Checking OUTPUT path ----------------------------------------\n");
    sb.append("-----------------------------------------------------------------\n");

    sql = "select startRange, endRange, "
        + "cast(unicodeLit as VARGRAPHIC(16000) CCSID 1200), "
        + "unicodeLit, "
        + "hex(unicodeLit) " + "from " + tableName;
    sb.append("Running " + sql + "\n");
    rs = stmt.executeQuery(sql);
    while (rs.next()) {
      String systemTranslateString = rs.getString(3);
      String localTranslateString = rs.getString(4);
      int rangeStart = rs.getInt(1); 
      if (!isSameIgnoringSubstitution(rangeStart, systemTranslateString, localTranslateString, columnDefinition)) {

	      String systemTranslateDumpString = JDTestUtilities.dumpBytes(systemTranslateString);
	      String localTranslateDumpString = JDTestUtilities.dumpBytes(localTranslateString);


	      sb.append(" For "+insertHash.get(""+rangeStart)+"\n");

	      passed = false;
	      sb.append(" Failed for 0x" + Integer.toHexString(rangeStart)
			+ " - 0x" + Integer.toHexString(rs.getInt(2)) + "\n");
	      sb.append(" system  = " + systemTranslateDumpString + "\n");
	      sb.append(" diff    = "
			+ markDifferences(systemTranslateDumpString, localTranslateDumpString) + "\n");
	      sb.append(" local   = " + localTranslateDumpString + "\n");
	      sb.append(" litHex  = ");
	      appendFormattedHexString(sb, rs.getString(5), doubleByteCcsid);
	      sb.append("\n");
	
      }
    }
    rs.close(); 
    
    
    stmt.close();   
      return passed; 
  }
  
  private void padSingleByteHex(StringBuffer sb, String string) {
    char[] chars = string.toCharArray(); 
    for (int i = 0; i < chars.length; i+=2) {
      sb.append("  "); 
      sb.append(chars[i]); 
      sb.append(chars[i+1]); 
    }
    
  }

  private void dumpHexRange(StringBuffer sb, int rangeStart, int rangeEnd) {
    for (int i = rangeStart; i <= rangeEnd; i++) {
      if (i < 0x10) {
        sb.append("000"); 
      } else if (i < 0x100) {
        sb.append("00"); 
      } else if (i < 0x1000) {
        sb.append("0"); 
      }
      sb.append(Integer.toHexString(i)); 
    }
  }

  private void appendFormattedHexString(StringBuffer sb, String string,
      boolean doubleByteCcsid) {
    /* make sure that the string has an even length */
    /* format the hex representing a mixed CCSID string */
    char[] chars = string.toCharArray();
    int i = 0;
    boolean singleByte = true;
    if (doubleByteCcsid) {
      singleByte = false;
    }
    while (i < chars.length) {
      if (!doubleByteCcsid) {
        if (singleByte) {
          if (chars[i] == '0' && (i + 1 < chars.length)
              && (chars[i + 1] == 'E')) {
            i += 2;
            singleByte = false;
          }
        } else {
          if (chars[i] == '0' && (i + 1 < chars.length)
              && (chars[i + 1] == 'F')) {
            singleByte = true;
            i += 2;
          }

        }
      }
      if (singleByte) {
          if (i < chars.length) { 
            sb.append("  "); 
            sb.append(chars[i]);
          }
          i++; 
          if (i < chars.length) { 
           sb.append(chars[i]);
           i++;
          }
        } else { 
          sb.append(chars[i]);     i++; 
          if (i < chars.length) { sb.append(chars[i]);    i++;    }
          if (i < chars.length) { sb.append(chars[i]);    i++;    }
          if (i < chars.length) { sb.append(chars[i]);    i++;    }
        }
    }
    
  }

  private boolean isSameIgnoringSubstitution(int rangeStart, String aString, String bString,
      String columnDefinition) {

    int ccsid = 0;
    if (columnDefinition.indexOf("61175") > 0) {    /* CCSID 61175 not support in DBTables -- see issue 66267 */ 
      ccsid = 61175;
    } else if (columnDefinition.indexOf("1132") > 0) {
      ccsid = 1132;
    } else if (columnDefinition.indexOf("16684") > 0) {
      ccsid = 16684;
    } else if (columnDefinition.indexOf("1377") > 0) {
      ccsid = 1377;
    } else if (columnDefinition.indexOf("937") > 0) {
      ccsid = 937;
   }

    char[] aChars = aString.toCharArray();
    char[] bChars = bString.toCharArray();
    int aLength = aChars.length;
    int bLength = bChars.length;
    if (aLength != bLength) {
      return false;
    }

    char[] rangeChars = new char[aLength];
    
    for (int i = 0; i < aLength; i++) {
      rangeChars[i] = (char) (rangeStart+i); 
      if (aChars[i] != bChars[i]) {
        if ((aChars[i] == 0x001a)) {
          if ((ccsid == 937) && (bChars[i] == (char)0x00d0)) {
            // ignore mismatch for CCSID 937
          } else  if ((ccsid == 61175) &&  (bChars[i]  == (char) 0x20ba)) {
            // ignore this mismatch for ccsid 16684/         
          } else  if ((ccsid == 61175) &&  (bChars[i]  == (char) 0x20ac)) {
            // ignore this mismatch for ccsid 16684/         
          } else { 
            
          if (bChars[i] != (char) 0xFFFD) {
            return false;
          } else {
            /* this is good so we continue */
          }
          }
        } else if (aChars[i] == 0xFFFD) {
          if (bChars[i] != (char) 0x001a) {
            if (((ccsid == 16684) ) &&  (bChars[i]  == (char) 0x20AC)) {
              // ignore this mismatch for ccsid 16684
            } else  if ((ccsid == 1377) &&  (bChars[i]  == (char) 0x00d0)) {
              // ignore this mismatch
	    } else if (rangeChars[i] == bChars[i]) {
	      // ignore if round trip
            } else { 
                return false;
            }
          } else {
            /* this is good so we continue */
          }
        } else {
          if (ccsid == 61175) {
            if (((aChars[i] == 0x20ac) && (bChars[i] == 0x00a4))
                || ((aChars[i] == 0x20ba) && (bChars[i] == 0x00aa))) {
              // This is ok.
            } else {
              return false;
            }
          } else if (ccsid == 1132) {
            if (((aChars[i] == 0x006b) && (bChars[i] == 0x20ad))
                || ((aChars[i] == 0x0000) && (bChars[i] == 0x0000))) {
              // This is ok.
            } else {
              return false;
            }
          } else {
            return false;
          }
        }

      } /* if not matching */

    }
    return true;
  }

  private String markDifferences(String aString, String bString) {
    StringBuffer sb = new StringBuffer(); 
    char[] aChars = aString.toCharArray(); 
    char[] bChars = bString.toCharArray(); 
    int aLength = aChars.length; 
    int bLength = bChars.length; 
    int length = aLength; 
    if (bLength < length) {
      length = bLength; 
    }
    for (int i = 0; i < length; i++) { 
      if ((aChars[i] == bChars[i]) ||
          ((aChars[i] == '\ufffd') && (bChars[i] == '\u001a')) ||
	  ((aChars[i] == '\u001a') && (bChars[i] == '\ufffd'))) { 
        sb.append(' '); 
      } else {  
        sb.append('|'); 
      }
    }
    
    return sb.toString(); 
  }

  public void testVariationUnicodeRange(String tableName, String columnDefinition, int unicodeStart, int unicodeEnd, int charsPerRow, boolean doubleByteCcsid) {
    StringBuffer sb = new StringBuffer();
    boolean passed  = true; 
    try { 
      // Check to see if something bad has close the connection.. If so
      // reopen it.
      if (connection_.isClosed()) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          // JCC doesn't have these properties
          connection_ = DriverManager.getConnection(baseURL_,
              systemObject_.getUserId(),  new String(PasswordVault.decryptPassword(encryptedPassword_)));

        } else {
          connection_ = DriverManager.getConnection(baseURL_
              + ";errors=full;lazy close=true", systemObject_.getUserId(),
               new String(PasswordVault.decryptPassword(encryptedPassword_)));
        }
      }

      passed = testUnicodeRange(connection_, tableName, columnDefinition, unicodeStart, unicodeEnd, charsPerRow, doubleByteCcsid, sb); 
          
      assertCondition(passed,sb) ;
      
    } catch (Exception e) { 
      failed(e, "Unexpected Exception "+sb.toString()); 
    }
  }
  
  
 /* Testing single byte CCSIDs */ 
  
  public void Var001() { testVariationUnicodeRange(collection_+".JDSSC290", "VARCHAR(200) CCSID 290", 0x0021, 0xD7FF, 0x20, false); }

  public void Var002() {testVariationUnicodeRange(collection_+".JDSSC37", "VARCHAR(200) CCSID 37", 0x0021, 0xD7FF,0x20, false);}
  public void Var003() {testVariationUnicodeRange(collection_+".JDSSC256", "VARCHAR(200) CCSID 256", 0x0021, 0xD7FF,0x20, false);}
  public void Var004() {testVariationUnicodeRange(collection_+".JDSSC273", "VARCHAR(200) CCSID 273", 0x0021, 0xD7FF,0x20, false);}
  public void Var005() {testVariationUnicodeRange(collection_+".JDSSC277", "VARCHAR(200) CCSID 277", 0x0021, 0xD7FF,0x20, false);}
  public void Var006() {testVariationUnicodeRange(collection_+".JDSSC278", "VARCHAR(200) CCSID 278", 0x0021, 0xD7FF,0x20, false);}
  public void Var007() {testVariationUnicodeRange(collection_+".JDSSC280", "VARCHAR(200) CCSID 280", 0x0021, 0xD7FF,0x20, false);}
  public void Var008() {testVariationUnicodeRange(collection_+".JDSSC284", "VARCHAR(200) CCSID 284", 0x0021, 0xD7FF,0x20, false);}
  public void Var009() {testVariationUnicodeRange(collection_+".JDSSC285", "VARCHAR(200) CCSID 285", 0x0021, 0xD7FF,0x20, false);}
  public void Var010() {testVariationUnicodeRange(collection_+".JDSSC290", "VARCHAR(200) CCSID 290", 0x0021, 0xD7FF,0x20, false);}
  public void Var011() {testVariationUnicodeRange(collection_+".JDSSC297", "VARCHAR(200) CCSID 297", 0x0021, 0xD7FF,0x20, false);}
  public void Var012() { 
    notApplicable("Skipping 420 Arabic"); 
    /* testVariationUnicodeRange(collection_+".JDSSC420", "VARCHAR(200) CCSID 420", 0x0021, 0xD7FF,0x20); */
  }
  public void Var013() {testVariationUnicodeRange(collection_+".JDSSC423", "VARCHAR(200) CCSID 423", 0x0021, 0xD7FF,0x20, false);}
  public void Var014() {
    notApplicable("Skipping 424 Hebrew"); 
    /* testVariationUnicodeRange(collection_+".JDSSC424", "VARCHAR(200) CCSID 424", 0x0021, 0xD7FF,0x20); */ 
  }
  public void Var015() {testVariationUnicodeRange(collection_+".JDSSC425", "VARCHAR(200) CCSID 425", 0x0021, 0xD7FF,0x20, false);}
  public void Var016() {testVariationUnicodeRange(collection_+".JDSSC500", "VARCHAR(200) CCSID 500", 0x0021, 0xD7FF,0x20, false);}
  public void Var017() {testVariationUnicodeRange(collection_+".JDSSC833", "VARCHAR(200) CCSID 833", 0x0021, 0xD7FF,0x20, false);}
  public void Var018() {testVariationUnicodeRange(collection_+".JDSSC836", "VARCHAR(200) CCSID 836", 0x0021, 0xD7FF,0x20, false);}
  public void Var019() {testVariationUnicodeRange(collection_+".JDSSC838", "VARCHAR(200) CCSID 838", 0x0021, 0xD7FF,0x20, false);}
  public void Var020() {testVariationUnicodeRange(collection_+".JDSSC870", "VARCHAR(200) CCSID 870", 0x0021, 0xD7FF,0x20, false);}
  public void Var021() {testVariationUnicodeRange(collection_+".JDSSC871", "VARCHAR(200) CCSID 871", 0x0021, 0xD7FF,0x20, false);}
  public void Var022() {testVariationUnicodeRange(collection_+".JDSSC875", "VARCHAR(200) CCSID 875", 0x0021, 0xD7FF,0x20, false);}
  public void Var023() {testVariationUnicodeRange(collection_+".JDSSC880", "VARCHAR(200) CCSID 880", 0x0021, 0xD7FF,0x20, false);}
  public void Var024() {testVariationUnicodeRange(collection_+".JDSSC905", "VARCHAR(200) CCSID 905", 0x0021, 0xD7FF,0x20, false);}
  public void Var025() {
    notApplicable("Skipping 918 URDU -- Missing many maps from 0621 to FF80, etc "); 
    /* testVariationUnicodeRange(collection_+".JDSSC918", "VARCHAR(200) CCSID 918", 0x0021, 0xD7FF,0x20); */
  }
  public void Var026() {testVariationUnicodeRange(collection_+".JDSSC924", "VARCHAR(200) CCSID 924", 0x0021, 0xD7FF,0x20, false);}
  public void Var027() {testVariationUnicodeRange(collection_+".JDSSC933", "VARCHAR(200) CCSID 933", 0x0021, 0xD7FF,0x20, false);}
  public void Var028() {testVariationUnicodeRange(collection_+".JDSSC1025", "VARCHAR(200) CCSID 1025", 0x0021, 0xD7FF,0x20, false);}
  public void Var029() {testVariationUnicodeRange(collection_+".JDSSC1026", "VARCHAR(200) CCSID 1026", 0x0021, 0xD7FF,0x20, false);}
  public void Var030() {testVariationUnicodeRange(collection_+".JDSSC1027", "VARCHAR(200) CCSID 1027", 0x0021, 0xD7FF,0x20, false);}
  public void Var031() {testVariationUnicodeRange(collection_+".JDSSC1047", "VARCHAR(200) CCSID 1047", 0x0021, 0xD7FF,0x20, false);}
  public void Var032() {
    notApplicable("Skipping 1097 FARSI -- Missing many maps from 0621 to FF80, etc "); 
    /* testVariationUnicodeRange(collection_+".JDSSC1097", "VARCHAR(200) CCSID 1097", 0x0021, 0xD7FF,0x20, false); */ 
  }
  public void Var033() {testVariationUnicodeRange(collection_+".JDSSC1112", "VARCHAR(200) CCSID 1112", 0x0021, 0xD7FF,0x20, false);}
  public void Var034() {testVariationUnicodeRange(collection_+".JDSSC1122", "VARCHAR(200) CCSID 1122", 0x0021, 0xD7FF,0x20, false);}
  public void Var035() {testVariationUnicodeRange(collection_+".JDSSC1123", "VARCHAR(200) CCSID 1123", 0x0021, 0xD7FF,0x20, false);}
  public void Var036() {testVariationUnicodeRange(collection_+".JDSSC1130", "VARCHAR(200) CCSID 1130", 0x0021, 0xD7FF,0x20, false);}
  public void Var037() {testVariationUnicodeRange(collection_+".JDSSC1132", "VARCHAR(200) CCSID 1132", 0x0021, 0xD7FF,0x20, false);}
  public void Var038() {testVariationUnicodeRange(collection_+".JDSSC1137", "VARCHAR(200) CCSID 1137", 0x0021, 0xD7FF,0x20, false);}
  public void Var039() {testVariationUnicodeRange(collection_+".JDSSC1140", "VARCHAR(200) CCSID 1140", 0x0021, 0xD7FF,0x20, false);}
  public void Var040() {testVariationUnicodeRange(collection_+".JDSSC1141", "VARCHAR(200) CCSID 1141", 0x0021, 0xD7FF,0x20, false);}
  public void Var041() {testVariationUnicodeRange(collection_+".JDSSC1142", "VARCHAR(200) CCSID 1142", 0x0021, 0xD7FF,0x20, false);}
  public void Var042() {testVariationUnicodeRange(collection_+".JDSSC1143", "VARCHAR(200) CCSID 1143", 0x0021, 0xD7FF,0x20, false);}
  public void Var043() {testVariationUnicodeRange(collection_+".JDSSC1144", "VARCHAR(200) CCSID 1144", 0x0021, 0xD7FF,0x20, false);}
  public void Var044() {testVariationUnicodeRange(collection_+".JDSSC1145", "VARCHAR(200) CCSID 1145", 0x0021, 0xD7FF,0x20, false);}
  public void Var045() {testVariationUnicodeRange(collection_+".JDSSC1146", "VARCHAR(200) CCSID 1146", 0x0021, 0xD7FF,0x20, false);}
  public void Var046() {testVariationUnicodeRange(collection_+".JDSSC1147", "VARCHAR(200) CCSID 1147", 0x0021, 0xD7FF,0x20, false);}
  public void Var047() {testVariationUnicodeRange(collection_+".JDSSC1148", "VARCHAR(200) CCSID 1148", 0x0021, 0xD7FF,0x20, false);}
  public void Var048() {testVariationUnicodeRange(collection_+".JDSSC1149", "VARCHAR(200) CCSID 1149", 0x0021, 0xD7FF,0x20, false);}
  public void Var049() {testVariationUnicodeRange(collection_+".JDSSC1153", "VARCHAR(200) CCSID 1153", 0x0021, 0xD7FF,0x20, false);}
  public void Var050() {testVariationUnicodeRange(collection_+".JDSSC1154", "VARCHAR(200) CCSID 1154", 0x0021, 0xD7FF,0x20, false);}
  public void Var051() {testVariationUnicodeRange(collection_+".JDSSC1155", "VARCHAR(200) CCSID 1155", 0x0021, 0xD7FF,0x20, false);}
  public void Var052() {testVariationUnicodeRange(collection_+".JDSSC1156", "VARCHAR(200) CCSID 1156", 0x0021, 0xD7FF,0x20, false);}
  public void Var053() {testVariationUnicodeRange(collection_+".JDSSC1157", "VARCHAR(200) CCSID 1157", 0x0021, 0xD7FF,0x20, false);}
  public void Var054() {testVariationUnicodeRange(collection_+".JDSSC1158", "VARCHAR(200) CCSID 1158", 0x0021, 0xD7FF,0x20, false);}
  public void Var055() {testVariationUnicodeRange(collection_+".JDSSC1160", "VARCHAR(200) CCSID 1160", 0x0021, 0xD7FF,0x20, false);}
  public void Var056() {testVariationUnicodeRange(collection_+".JDSSC1164", "VARCHAR(200) CCSID 1164", 0x0021, 0xD7FF,0x20, false);}
  public void Var057() {testVariationUnicodeRange(collection_+".JDSSC1166", "VARCHAR(200) CCSID 1166", 0x0021, 0xD7FF,0x20, false);}
  public void Var058() {
      testVariationUnicodeRange(collection_ + ".JDSSC1175",
          "VARCHAR(200) CCSID 1175", 0x0021, 0xD7FF, 0x20, false);
  }

  public void Var059() {testVariationUnicodeRange(collection_+".JDSSC1208", "VARCHAR(200) CCSID 1208", 0x0021, 0xD7FF,0x20, false);}
  public void Var060() {testVariationUnicodeRange(collection_+".JDSSC4971", "VARCHAR(200) CCSID 4971", 0x0021, 0xD7FF,0x20, false);}
  public void Var061() {testVariationUnicodeRange(collection_+".JDSSC5123", "VARCHAR(200) CCSID 5123", 0x0021, 0xD7FF,0x20, false);}
  public void Var062() {testVariationUnicodeRange(collection_+".JDSSC5233", "VARCHAR(200) CCSID 5233", 0x0021, 0xD7FF,0x20, false);}
  public void Var063() {
    notApplicable("Arabic 8612"); 
    /* testVariationUnicodeRange(collection_+".JDSSC8612", "VARCHAR(200) CCSID 8612", 0x0021, 0xD7FF,0x20); */ 
    }
  public void Var064() {testVariationUnicodeRange(collection_+".JDSSC9030", "VARCHAR(200) CCSID 9030", 0x0021, 0xD7FF,0x20, false);}
  public void Var065() {
    notApplicable("Arabic 12708"); 
    /* testVariationUnicodeRange(collection_+".JDSSC12708", "VARCHAR(200) CCSID 12708", 0x0021, 0xD7FF,0x20); */ 
    }
  public void Var066() {testVariationUnicodeRange(collection_+".JDSSC13121", "VARCHAR(200) CCSID 13121", 0x0021, 0xD7FF,0x20, false);}
  public void Var067() {testVariationUnicodeRange(collection_+".JDSSC13124", "VARCHAR(200) CCSID 13124", 0x0021, 0xD7FF,0x20, false);}
  public void Var068() {testVariationUnicodeRange(collection_+".JDSSC28709", "VARCHAR(200) CCSID 28709", 0x0021, 0xD7FF,0x20, false);}
  public void Var069() {
    notApplicable("SAP CCSID 57777"); 
    /* 
    testVariationUnicodeRange(collection_+".JDSSC57777", "VARCHAR(200) CCSID 57777", 0x0021, 0xD7FF,0x20);
    */ 
    }
  public void Var070() {
      notApplicable("CCSID 61175 not valid -- see issue 66267 "); 
    }
  public void Var071() {
    notApplicable("Hebrew 62211"); 
    /* testVariationUnicodeRange(collection_+".JDSSC62211", "VARCHAR(200) CCSID 62211", 0x0021, 0xD7FF,0x20); */ 
    }
  public void Var072() {
    notApplicable("Arabic 62224"); 
    /* testVariationUnicodeRange(collection_+".JDSSC62224", "VARCHAR(200) CCSID 62224", 0x0021, 0xD7FF,0x20); */ 
    }
  public void Var073() {
    notApplicable("Hebrew 62235"); 
    /* testVariationUnicodeRange(collection_+".JDSSC62235", "VARCHAR(200) CCSID 62235", 0x0021, 0xD7FF,0x20); */ 
    }
  public void Var074() {
    notApplicable("Hebrew 62245"); 
    /* testVariationUnicodeRange(collection_+".JDSSC62245", "VARCHAR(200) CCSID 62245", 0x0021, 0xD7FF,0x20); */
    }
  public void Var075() {
    notApplicable("Arabic 62224"); 

    /* testVariationUnicodeRange(collection_+".JDSSC62251", "VARCHAR(200) CCSID 62251", 0x0021, 0xD7FF,0x20) */ ;
    }
  public void Var076() {notApplicable();}
  public void Var077() {notApplicable();}
  public void Var078() {notApplicable();}
  public void Var079() {notApplicable();}
  public void Var080() {notApplicable();}
  /* Testing mixed CCSIDs */ 

  public void Var081() { 
    /* Upper unicode value is D7FF */ 
    testVariationUnicodeRange(collection_+".JDSSC930", "VARCHAR(200) CCSID 930", 0x0021, 0xD7FF, 0x20, false);
  }

    public void Var082() { 
      /* Upper unicode value is D7FF */ 
      testVariationUnicodeRange(collection_+".JDSSC935", "VARCHAR(200) CCSID 935", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var083() { 
      /* Upper unicode value is D7FF */ 
      testVariationUnicodeRange(collection_+".JDSSC937", "VARCHAR(200) CCSID 937", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var084() { 
      /* Upper unicode value is D7FF */ 
      testVariationUnicodeRange(collection_+".JDSSC939", "VARCHAR(200) CCSID 939", 0x0021, 0xD7FF, 0x20, false);
    }


    public void Var085() { 
      testVariationUnicodeRange(collection_+".JDSSC1364", "VARCHAR(200) CCSID 1364", 0x0021, 0x9fff, 0x20, false);
    }

    public void Var086() { 
      testVariationUnicodeRange(collection_+".JDSSC1371", "VARCHAR(200) CCSID 1371", 0x0021, 0xd7ff, 0x20, false);
    }
    
    public void Var087() { 
      testVariationUnicodeRange(collection_+".JDSSC1377", "VARCHAR(200) CCSID 1377", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var088() { 
      testVariationUnicodeRange(collection_+".JDSSC1388", "VARCHAR(200) CCSID 1388", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var089() {
	if (checkNotGroupTest()) { 
	    testVariationUnicodeRange(collection_+".JDSSC1399", "VARCHAR(200) CCSID 1399", 0x0021, 0xD7FF, 0x20, false);
	}
    }

    public void Var090() { 
      /* Note:   Currently does not map EURO 0x20ac correctly in single byte CCSID 290 */ 
      testVariationUnicodeRange(collection_+".JDSSC5026", "VARCHAR(200) CCSID 5026", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var091() { 
      testVariationUnicodeRange(collection_+".JDSSC5035", "VARCHAR(200) CCSID 5035", 0x0021, 0xD7FF, 0x20, false);
    }

    public void Var092() { 
      testVariationUnicodeRange(collection_+".JDSSC1371", "VARCHAR(200) CCSID 1371", 0xf900, 0xfdff, 0x20, false);
    }

    public void Var093() { 
      testVariationUnicodeRange(collection_+".JDSSC1371", "VARCHAR(200) CCSID 1371", 0xfe03, 0xffff, 0x20, false);
    }

    public void Var094() {
	if (checkNotGroupTest()) { 
	    testVariationUnicodeRange(collection_+".JDSSC1379", "VARCHAR(200) CCSID 1379", 0x0021, 0xD7FF, 0x20, false);
	}
    }

    public void Var095() { notApplicable();}
    public void Var096() { notApplicable();}
    public void Var097() { notApplicable();}
    public void Var098() { notApplicable();}
    public void Var099() { notApplicable();}
    public void Var100() { notApplicable();}

    /* Testing double byte ccsids */ 
    

    public void Var101() {testVariationUnicodeRange(collection_+".JDSSC300", "VARGRAPHIC(200) CCSID 300", 0x0021, 0xD7FF, 0x20, true); }
    public void Var102() {testVariationUnicodeRange(collection_+".JDSSC834", "VARGRAPHIC(200) CCSID 834", 0x0021, 0xD7FF, 0x20, true); }
    public void Var103() {testVariationUnicodeRange(collection_+".JDSSC835", "VARGRAPHIC(200) CCSID 835", 0x0021, 0xD7FF, 0x20, true); }
    public void Var104() {testVariationUnicodeRange(collection_+".JDSSC837", "VARGRAPHIC(200) CCSID 837", 0x0021, 0xD7FF, 0x20, true); }
    public void Var105() {testVariationUnicodeRange(collection_+".JDSSC1200", "VARGRAPHIC(200) CCSID 1200", 0x0021, 0xD7FF, 0x20, true); }
    public void Var106() {testVariationUnicodeRange(collection_+".JDSSC4396", "VARGRAPHIC(200) CCSID 4396", 0x0021, 0xD7FF, 0x20, true); }
    public void Var107() {testVariationUnicodeRange(collection_+".JDSSC4930", "VARGRAPHIC(200) CCSID 4930", 0x0021, 0xD7FF, 0x20, true); }
    public void Var108() {testVariationUnicodeRange(collection_+".JDSSC4933", "VARGRAPHIC(200) CCSID 4933", 0x0021, 0xD7FF, 0x20, true); }
    public void Var109() {testVariationUnicodeRange(collection_+".JDSSC13488", "VARGRAPHIC(200) CCSID 13488", 0x0021, 0xD7FF, 0x20, true); }
    public void Var110() {testVariationUnicodeRange(collection_+".JDSSC16684", "VARGRAPHIC(200) CCSID 16684", 0x0021, 0xD7FF, 0x20, true); }



  
}



