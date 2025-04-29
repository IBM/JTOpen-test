///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PasswordLeakTool.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.crypto.*;
import javax.crypto.spec.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCDriver;

public class PasswordLeakTool {

  static boolean ibmJVM = false;
  static String javaHome;
  static String osName;
  static String reversePassword;
  static String userid; 
  static String jdmpviewPath;
  static String leakString; 
  static int osType = 0;
  public static final int OS_WINDOWS = 1;
  public static final int OS_IBM_I = 2;
  public static final int OS_AIX = 3;
  static boolean debug = false;

  static {
    String debugString = System.getProperty("debug");
    if (debugString != null) {
      debug = true;
    }
    javaHome = System.getProperty("java.home");
    if (debug) {
	System.out.println("DEBUG: java.home is "+javaHome); 
    }
    if (JTOpenTestEnvironment.isWindows) {
      osType = OS_WINDOWS;
    } else if (JTOpenTestEnvironment.isOS400) {
      osType = OS_IBM_I;
    } else if (JTOpenTestEnvironment.isAIX) {
      osType = OS_AIX;
    } else {
      System.out.println("PasswordLeakTool: unknown OS=" + JTOpenTestEnvironment.osVersion);
    }
    if (javaHome.indexOf("/QOpenSys/QIBM/ProdData/JavaVM") >= 0) {
      ibmJVM = true;
    } else {
      String javaVendor = System.getProperty("java.vendor");
      if ("IBM Corporation".equals(javaVendor)) {
        ibmJVM = true;
      }
    }

    if (ibmJVM) {
      switch (osType) {
      case OS_WINDOWS:
        jdmpviewPath = javaHome + "\\bin\\jdmpview.exe";
        File f = new File(jdmpviewPath); 
        if (! f.exists()) { 
          jdmpviewPath = javaHome + "\\..\\bin\\jdmpview.exe";
        }
        break;
      case OS_IBM_I:
      case OS_AIX: 
        jdmpviewPath = javaHome + "/bin/jdmpview";
        break;
      default:
        jdmpviewPath = "UNKNOWN PATH";

      }

    }
  }

  /**
   * Force a JVM dump to file. Will be a no-op if not in a IBM JVM. Will
   * overwrite the file if it already exists
   */

  public static void forceDump(String filename) {
    if (!ibmJVM) {
      System.out.println("forceDump(" + filename + ") no op");
      System.out.println("Not ibmJVM:  properties are");
      Properties p = System.getProperties();
      Enumeration<?> e = p.keys();
      while (e.hasMoreElements()) {
        String key = (String) e.nextElement();
        String value = p.getProperty(key);
        System.out.println(" " + key + ":" + value);
      }
      return;
    }
    //
    // com.ibm.jvm.Dump.systemDumpToFile("/tmp/passwordDump")
    //

    try {
      boolean deleted = false;
      String osFilename = getOsFilename(filename);
      File dumpFile = new File(osFilename);
      if (dumpFile.exists()) {
        deleted = dumpFile.delete();
        if (!deleted) {
          System.out.println("Warning: Unabled to delete "+dumpFile.getCanonicalPath()); 
        }
      }
      long startMillis = System.currentTimeMillis();
      String realDumpFile = (String) JDReflectionUtil.callStaticMethod_O(
          "com.ibm.jvm.Dump", "systemDumpToFile", osFilename);
      long millis = System.currentTimeMillis() - startMillis;
      System.out.println("dump " + realDumpFile + "(" + filename
          + ") created in " + (millis / 1000.0) + " seconds ");
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }
  }

  private static String getOsFilename(String filename) {

    String osFilename = filename;
    if (osType == OS_WINDOWS) {
      osFilename = filename.replace('/', '\\');
      if (osFilename.indexOf(":") < 0) {
        osFilename = "C:" + osFilename;
      }
    }

    return osFilename;
  }

  /**
   * Scan a dump file for a password.
   * 
   * @param filename
   * @param password
   * @return null if password not found -- otherwise, details returned.
   */
  public static String scanDumpForPasswords(String filename,
      String[] passwords) {
    StringBuffer sb = new StringBuffer();
    Runtime runtime = Runtime.getRuntime();
    String[] cmdarray = new String[3];
    cmdarray[0] = jdmpviewPath;
    cmdarray[1] = "-core";
    cmdarray[2] = filename;

    System.out.println("PasswordLeakTool.scanDumpForPasswords: running"); 
    try {
      System.out.println("PasswordLeakTool.scanDumpForPasswords Running :"+cmdarray[0]+" "+cmdarray[1]+" "+cmdarray[2]); 
      Process p = runtime.exec(cmdarray);
      OutputStream outputStream = p.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream);
      StringBuffer outputBuffer = new StringBuffer();
      JDJSTPOutputThread cmdStdoutThread = new JDJSTPOutputThread(
          p.getInputStream(), outputBuffer, null,
          JDJSTPOutputThread.ENCODING_ASCII);
      JDJSTPOutputThread stderrThread = new JDJSTPOutputThread(
          p.getErrorStream(), outputBuffer, null,
          JDJSTPOutputThread.ENCODING_ASCII);
      cmdStdoutThread.start();
      stderrThread.start();
      printWriter.println("readyForCommand");
      printWriter.flush();
      String data = waitForPrompt(outputBuffer);
      if (debug) {
	  System.out.println("DEBUG: readForCommand returned "+data); 
      }

      printWriter.println("info heap *");
      printWriter.println("readyForCommand");
      printWriter.flush();

      data = waitForPrompt(outputBuffer);
      if (debug) {
        System.out.println("DEBUG: --------------heap info----------------");
        System.out.println(data);
        System.out.println("DEBUG: --------------heap info----------------");
      }
      // Done, cleanup
      String[][] heapInfo = getHeapInfo(data);
      dumpHeapInfo(heapInfo); 
      int memoryBoundary = 1;
      int bytesToPrint = 16;
      int matchesToDisplay = 16;
      Exception foundException = null; 
      for (int k = 0; k < passwords.length; k++) {
        String password = passwords[k];
        bytesToPrint = password.length() * 2;
        String[] searchHexStrings = new String[3];
        searchHexStrings[0] = "0x" + hexUnicode(password);
        searchHexStrings[1] = "0x" + hexUtf8(password);
        searchHexStrings[2] = "0x" + hexEbcdic(password);

        for (int i = 0; i < heapInfo.length; i++) {
          for (int j = 0; j < searchHexStrings.length; j++) {
            /* Add 32 to see if purposely leaked */
            bytesToPrint = (searchHexStrings[j].length() - 2) / 2 + 32;
            String findCommand = "find " + searchHexStrings[j] + "," + heapInfo[i][0] + "," + heapInfo[i][1] + ","
                + memoryBoundary + "," + bytesToPrint + "," + matchesToDisplay;

            System.out.println("PasswordLeakTool.scanDumpForPasswords: running " + findCommand);
            if (foundException == null) {
              try {
                printWriter.println(findCommand);
                printWriter.println("readyForCommand");
                printWriter.flush();

                data = waitForPrompt(outputBuffer);
                if (debug) {
                  System.out.println("DEBUG: -----------------------------");
                  System.out.println(data);
                  System.out.println("DEBUG: -----------------------------");
                }
              } catch (Exception e) {
                data = "#0  Exception encountered"; 
                System.out.println("Exception found.. going to generate all search strings");
                e.printStackTrace(System.out);
                foundException = e;
              }

              if (data.indexOf("#0") >= 0) {
                String info = "Found " + searchHexStrings[j] + " in heap #" + i + " searchstring #" + j + " password #"
                    + k + "\n";
                System.out.println("PasswordLeakTool.scanDumpForPasswords: " + info);
                sb.append(info);
                sb.append("Find command=" + findCommand + "\n");
                sb.append(data);
              }
            }
          }
        }

      }

      printWriter.println("exit");
      printWriter.flush();
      Thread.sleep(250);
      System.out.println("PasswordLeakTool.scanDumpForPasswords: Destroying process");
      p.destroy();
      cmdStdoutThread.join();
      stderrThread.join();

      /*
       * You can then start QSH and use the jdmpview tool.
       * 
       * /qopensys/QIBM/ProdData/JavaVM/jdk80/64bit/jre/bin/jdmpview -core
       * /tmp/passwordDump
       * 
       * Use "info heap *" to see the heap information.
       * 
       * This is what I saw.
       * 
       * info heap *
       * 
       * Heap #1: Generational@1806db8b0 Section #1: Heap extent at 0x80000000
       * (0x600000 bytes) Size: 6291456 bytes Shared: false Executable: true
       * Read Only: false Section #2: Heap extent at 0xffde0000 (0xb0000 bytes)
       * Size: 720896 bytes Shared: false Executable: true Read Only: false
       * Section #3: Heap extent at 0xffe90000 (0x170000 bytes) Size: 1507328
       * bytes Shared: false Executable: true Read Only: false
       * 
       * 
       * You can then use the find command to search the heap memory (using the
       * values above). In this case, I was searching for j8va
       * 
       * 
       * > find 0x006A003800760061,0x80000000,0x80600000,1,16,10 No matches
       * found. > find 0x006A003800760061,0xffde0000,0xffea0000,1,16,10 No
       * matches found. > find 0x006A003800760061,0xffe90000,0xffffffff,1,16,10
       * #0: 0xffed67f8 #1: 0xffed6838 #2: 0xffed7418 #3: 0xffed7438 #4:
       * 0xffed74a8 #5: 0xffef0460 #6: 0xffef0480 #7: 0xffef04f0 #8: 0xfff2aff8
       * 
       * fff2aff8: 006a0038 00760061 00740065 0061006d |.j.8.v.a.x.x.x.x|
       * 
       */

    } catch (Exception e) {
      System.out.println("Exception caught!!!!");
      e.printStackTrace();
    }

    if (sb.length() > 0) {
      return sb.toString();
    } else {
      return null;
    }
  }

  private static String hexUnicode(String password) {
    StringBuffer sb = new StringBuffer();
    char[] charArray = password.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
      char c = charArray[i];
      sb.append(hexDigit((c >> 12) & 0x0f));
      sb.append(hexDigit((c >> 8) & 0x0f));
      sb.append(hexDigit((c >> 4) & 0x0f));
      sb.append(hexDigit(c & 0x0f));

    }

    return sb.toString();

  }

  private static String hexUtf8(String password)
      throws UnsupportedEncodingException {
    StringBuffer sb = new StringBuffer();
    byte[] byteArray = password.getBytes("UTF-8");
    for (int i = 0; i < byteArray.length; i++) {
      byte c = byteArray[i];
      sb.append(hexDigit((c >> 4) & 0x0f));
      sb.append(hexDigit(c & 0x0f));
    }

    return sb.toString();

  }

  private static String hexEbcdic(String password)
      throws UnsupportedEncodingException {
    StringBuffer sb = new StringBuffer();
    byte[] byteArray = password.getBytes("Cp037");
    for (int i = 0; i < byteArray.length; i++) {
      byte c = byteArray[i];
      sb.append(hexDigit((c >> 4) & 0x0f));
      sb.append(hexDigit(c & 0x0f));
    }

    return sb.toString();

  }

  private static char hexDigit(int i) {
    switch (i) {
    case 0:
      return '0';
    case 1:
      return '1';
    case 2:
      return '2';
    case 3:
      return '3';
    case 4:
      return '4';
    case 5:
      return '5';
    case 6:
      return '6';
    case 7:
      return '7';
    case 8:
      return '8';
    case 9:
      return '9';
    case 10:
      return 'a';
    case 11:
      return 'b';
    case 12:
      return 'c';
    case 13:
      return 'd';
    case 14:
      return 'e';
    case 15:
      return 'f';
    }
    return '?';
  }

  public static void dumpHeapInfo(String[][] heapInfo) {
    System.out.println("There are " + heapInfo.length + " heaps");
    for (int i = 0; i < heapInfo.length; i++) {
      System.out
          .println("Heap " + i + " : " + heapInfo[i][0] + " " + heapInfo[i][1]);
    }
  }

  /* Wait for the unrecognized command to show up. An invalid command is */
  /* always issued so that we know when a command completes */
  public static final String heapExtentString = "Heap extent at 0x";
  public static final String heapStartEndString = " (0x";
  public static final String heapLengthEndString = " bytes)";

  private static String[][] getHeapInfo(String data) {
    Vector<String[]> v = new Vector<String[]>();
    int heapExtentIndex = data.indexOf(heapExtentString);
    while (heapExtentIndex > 0) {
      int heapStartBeginIndex = heapExtentIndex + heapExtentString.length();
      int heapStartEndIndex = data.indexOf(heapStartEndString,
          heapStartBeginIndex);
      int heapLengthBeginIndex = heapStartEndIndex
          + heapStartEndString.length();
      int heapLengthEndIndex = data.indexOf(heapLengthEndString,
          heapLengthBeginIndex);
      String heapBeginString = data.substring(heapStartBeginIndex,
          heapStartEndIndex);

      String heapLengthString = data.substring(heapLengthBeginIndex,
          heapLengthEndIndex);

      /* System.out.println("Found "+heapBeginString+":"+heapLengthString); */
      String[] heapInfo = new String[2];
      heapInfo[0] = heapBeginString;
      heapInfo[1] = heapLengthString;
      v.add(heapInfo);
      heapExtentIndex = data.indexOf(heapExtentString, heapLengthEndIndex);
    }

    int heapCount = v.size();
    String[][] heapInfo = new String[heapCount][];
    for (int i = 0; i < heapCount; i++) {
      heapInfo[i] = new String[2];
      long heapStart = Long.parseLong(((String[]) v.elementAt(i))[0], 16);
      long heapLength = Long.parseLong(((String[]) v.elementAt(i))[1], 16);
      long heapEnd = heapStart + heapLength - 1;

      heapInfo[i][0] = "0x" + Long.toHexString(heapStart);
      heapInfo[i][1] = "0x" + Long.toHexString(heapEnd);
    }

    return heapInfo;
  }

  final static int PROMPT_WAIT_SECONDS = 300; 
  private static String waitForPrompt(StringBuffer outputBuffer)
      throws Exception {
    long endMillis = System.currentTimeMillis() +PROMPT_WAIT_SECONDS * 1000;
    int promptIndex = outputBuffer.indexOf("Unrecognised command:");
    while (promptIndex < 0 && System.currentTimeMillis() < endMillis) {
      Thread.sleep(2000);
      promptIndex = outputBuffer.indexOf("Unrecognised command:");
    }
    if (promptIndex < 0) {
      System.out.println("--------------- Got ----------------------\n");
      System.out.println(outputBuffer.toString());
      System.out.println("------------------------------------------\n");
      throw new Exception("Unable to get prompt in "+PROMPT_WAIT_SECONDS+" seconds");
    }

    String data = outputBuffer.toString();
    promptIndex = data.indexOf("Unrecognised command:");
    String returnData = data.substring(0, promptIndex);
    outputBuffer.setLength(0);
    return returnData;
  }

  public static void usage() {
    System.out.println(
        "java test.PasswordLeakTool [USER userid] [REVPASS reversedPassword] [AS400JAVACONNECT system] [AS400JAVADATASOURCE system]  " +
        "[ENCRYPTPASSWORD] [DECRYPTPASSWORD] [LEAKSTRINGPASSWORD] [PBKEYSPEC]"
        + "[DUMP dumpfile]* [SCAN dumpfile reversedPassword]* ");
    System.out.println(
        "                    i.e.  USERID user1 REVPASS dr0wssap AS400JAVACONNECT    system  DUMP /tmp/dumpFile.txt SCAN /tmp/dumpFile.txt dr0wssap   ");
    System.out.println(
        "                    i.e.  USERID user1 REVPASS dr0wssap AS400JAVADATASOURCE system  DUMP /tmp/dumpFile.txt SCAN /tmp/dumpFile.txt dr0wssap  ");
    System.out.println(
        "                    i.e.  USERID user1 REVPASS dr0wssap AS400JAVAJDBC       system  DUMP /tmp/dumpFile.txt SCAN /tmp/dumpFile.txt dr0wssap ");
    System.out.println(
        "                    i.e.  USERID user1 REVPASS dr0wssap DB2JAVAJDBC         system  DUMP /tmp/dumpFile.txt SCAN /tmp/dumpFile.txt dr0wssap  ");

    System.out.println(
        "                    i.e.  REVPASS dr0wssap ENCRYPTPASSWORD DECRYPTPASSWORD DUMP /tmp/dumpFile.txt  SCAN /tmp/dumpFile.txt   ");
    System.out.println(); 
    System.out.println(
        "        Recommended JVM flags:   -D-XX:+UseNoGC -D-verbose:gc -D-Xgcpolicy:nogc -D-Xnocompressedrefs ");

  }

  public static AS400JDBCDataSource ds = null;
  private static byte[] outputBytes;
  private static char[] outputChars; 
  public static void main(String[] args) {
    
    String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    String ALGORITHM = "AES";

    byte[] encryptedPassword = null;
    Cipher cipher = null;
    IvParameterSpec iv  = null;
    SecretKey secretKey  = null;

    if (args.length == 0) {
      usage();
    }
    int i = 0;
    try {
      while (i < args.length) {
        String command = args[i];
        if (command.equalsIgnoreCase("USERID")) {
          i++;
          userid = args[i]; 
        } else if (command.equalsIgnoreCase("REVPASS")) {
            i++;
            reversePassword = args[i]; 
        } else if (command.equalsIgnoreCase("LEAKSTRINGPASSWORD")) {
          char[] passwordArray=getPasswordArray(); 
          leakString = new String(passwordArray); 
          Arrays.fill(passwordArray, '\0');
        } else if (command.equalsIgnoreCase("DUMP")) {
          i++;
          System.out.println("Forcing dump to " + args[i]);
          forceDump(args[i]);
        } else if (command.equalsIgnoreCase("SCAN")) {
          i++;
          String dumpFile = args[i];
          i++;
          String[] passwords = new String[1];
          passwords[0] = new String(reverseString(args[i]));
          
          System.out.println("Scanning dump (" + dumpFile + ") for password ("
              + passwords[0] + ")");

          String output = scanDumpForPasswords(dumpFile, passwords);
          if (output == null) {
            System.out.println("password not found");
          } else {
            System.out.println("password found");
            System.out.println(output);

          }
        } else if (command.equalsIgnoreCase("AS400JAVACONNECT")) {
          i++;
          if (userid == null) throw new Exception ("USERID not set");
          char[] passwordArray = getPasswordArray(); 
          
          AS400 as400 = new AS400(args[i], userid, passwordArray);
          Arrays.fill(passwordArray, '\0');

          as400.passwordLevel();
          as400.passwordLevel();
          as400.passwordLevel();
          as400.disconnectAllServices(); 
          as400.close(); 
        } else if (command.equalsIgnoreCase("AS400JAVADATASOURCE")) {
          i++;
          if (userid == null) throw new Exception ("USERID not set");
          char[] passwordArray=getPasswordArray(); 

          ds = new AS400JDBCDataSource(args[i], userid, passwordArray);
          Arrays.fill(passwordArray, '\0');
 
        } else if (command.equalsIgnoreCase("AS400JAVAJDBC")) {
          i++;
          if (userid == null) throw new Exception ("USERID not set");
          char[] passwordArray=getPasswordArray(); 

          AS400JDBCDriver driver = new AS400JDBCDriver(); 
          Connection c = driver.connect("jdbc:as400:"+args[i],  userid, passwordArray);
          Arrays.fill(passwordArray, '\0');
          Statement s = c.createStatement(); 
          ResultSet rs = s.executeQuery("VALUES CURRENT USER"); 
          rs.next(); 
          String currentUser = rs.getString(1); 
          System.out.println("JDBC connection to "+args[i]+" created and CURRENT USER is "+currentUser); 
          
        } else if (command.equalsIgnoreCase("DB2JAVAJDBC")) {
          i++;
          if (userid == null) throw new Exception ("USERID not set");
          char[] passwordArray=getPasswordArray(); 

          Object driver = JDReflectionUtil.callStaticMethod_O("com.ibm.db2.jdbc.app.DB2Driver", "getDriver");
          Connection c = (Connection) JDReflectionUtil.callMethod_O(driver, "connect", "jdbc:db2:"+args[i],  userid, passwordArray);
          Arrays.fill(passwordArray, '\0');
          Statement s = c.createStatement(); 
          ResultSet rs = s.executeQuery("VALUES CURRENT USER"); 
          rs.next(); 
          String currentUser = rs.getString(1); 
          System.out.println("JDBC connection to "+args[i]+" created and CURRENT USER is "+currentUser); 

        } else if (command.equalsIgnoreCase("ENCRYPTPASSWORD")) {
          char[] passwordArray=getPasswordArray(); 

          KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
          secretKey = keyGenerator.generateKey();
          byte[] seed = { 0x12, 0x34, 0x56, 0x78, (byte) 0x9a, (byte) 0xbc, (byte) 0xcd, (byte) 0xde, 0x12, 0x34, 0x56,
              0x78, (byte) 0x9a, (byte) 0xbc, (byte) 0xcd, (byte) 0xde };
          iv = new IvParameterSpec(seed);
          cipher = Cipher.getInstance(TRANSFORMATION);
          cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
          byte[] input = new byte[passwordArray.length * 2];
          for (int j = 0; j < passwordArray.length; j++) {
            input[j * 2] = (byte) (passwordArray[j] >> 8);
            input[j * 2 + 1] = (byte) (passwordArray[j] & 0xFF);
          }
          encryptedPassword = cipher.doFinal(input);
          Arrays.fill(input, (byte) 0); /* Clear intermediary storage */          
          Arrays.fill(passwordArray, '\0');

        }  else if (command.equalsIgnoreCase("DECRYPTPASSWORD")) {

          cipher = Cipher.getInstance(TRANSFORMATION);
          cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
          outputBytes = cipher.doFinal(encryptedPassword);
          outputChars = new char[outputBytes.length / 2];
          for (int j = 0; j < outputChars.length; j++) {
                  outputChars[j] = (char) ((outputBytes[2 * j] << 8) + outputBytes[2 * j + 1]);
          }
          Arrays.fill(outputBytes, (byte) 0); /* Clear intermediary storage */
          Arrays.fill(outputChars, '\0');     /* Clean encrypted output */ 
          

        }  else if (command.equalsIgnoreCase("PBEKEYSPEC")) {
          char[] passwordArray=getPasswordArray(); 

          byte[] salt = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f,
              0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1a,0x1b,0x1c,0x1d,0x1e,0x1f}; 
          
          final PBEKeySpec spec = new PBEKeySpec(passwordArray, salt, 10022, 64 * 8); // takes a bit length so *8
          Arrays.fill(passwordArray, ' ');
          spec.clearPassword();

        }  else if (command.equalsIgnoreCase("GENERATESECRET")) {
          SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

          char[] passwordArray=getPasswordArray(); 

          byte[] salt = {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0a,0x0b,0x0c,0x0d,0x0e,0x0f,
              0x10,0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,0x19,0x1a,0x1b,0x1c,0x1d,0x1e,0x1f}; 
          
          final PBEKeySpec spec = new PBEKeySpec(passwordArray, salt, 10022, 64 * 8); // takes a bit length so *8
          SecretKey secret = factory.generateSecret(spec);
          
          Arrays.fill(passwordArray, ' ');
          spec.clearPassword();
          secret.destroy();
          
          
       
          
        } else {
          System.out.println("Invalid command " + command);
        }

        i++;
      }
    } catch (Exception e) {     
      e.printStackTrace();
      usage();
    }
  }

  private static char[] getPasswordArray() throws Exception {
    if (reversePassword == null) throw new Exception("REVPASS not set"); 
    return reverseString(reversePassword); 
  }

  private static char[] reverseString(String s) { 
    int len = s.length(); 
    char[] passwordArray = new char[len]; 
    for (int i = 0; i < len; i++) { 
      passwordArray[len-1-i] = s.charAt(i); 
    }
    
    return passwordArray;
    
  }
}
