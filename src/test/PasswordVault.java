///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PasswordVault.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

/* Password value stored encrypted copies of password and returns them as */
/* character arrays.  The character arrays should be cleared after the password */
/* is used. */
/* For now, simple bit twiddling is used to prevent passwords from being */
/* exposed in memory.  In the future, encryption should be used. */

public class PasswordVault {
  static Hashtable<String, Hashtable<String, char[]>> systemToUser = new Hashtable<String, Hashtable<String, char[]>>();
  static Hashtable<String, char[]> encryptedHashtable = new Hashtable<String, char[]>(); 
  static boolean passwordDebug = false;
  public static String globalString = null; 
  static {
      String property = System.getProperty("passwordDebug");
      if (property != null) {
	  passwordDebug = true;
	  System.out.println("JDRunit: passwordDebug is on");
      }
  }

  public static void setPassword(String system, String userid, char[] password) {
    char[] encryptedPassword = encryptPassword(password);
    Hashtable<String, char[]> userToPassword = systemToUser.get(system);
    if (userToPassword == null) {
      userToPassword = new Hashtable<String, char[]>();
      systemToUser.put(system, userToPassword);
    }
    userToPassword.put(userid, encryptedPassword);
  }

  public static void setPasswordFromIniFile(String system, String userid,
      String passwordFile) throws Exception {
    char[] encryptedPassword = getEncryptedPassword(passwordFile); 
    char[] password = decryptPassword(encryptedPassword); 
    setPassword(system, userid, password);
    clearPassword(password); 
  }

  /* Portability for use to get old password or new password from file */
  public static char[] getEncryptedPassword(String passwordName) {
    char[] encryptedPassword;
    if (passwordName == null)
      return null;
    
    encryptedPassword = encryptedHashtable.get(passwordName);
    if (encryptedPassword == null) {
      char[] password = null;
      if (passwordName.endsWith(".txt")) {
        try {
          File file = new File("ini/" + passwordName);
          if (!file.exists()) {
            throw new Exception("Password file " + file.getAbsolutePath() + " does not exist");
          }

          // Note: do not use a FileReader as it uses buffers that are not cleared.
          FileInputStream fileInputStream = new FileInputStream(file);
          char[] buffer = new char[80];
          int len = 0;

          int character = fileInputStream.read();
          while (character >= 0x20 && len < buffer.length) {
            buffer[len] = (char) character;
            len++;
            character = fileInputStream.read();
          }
          fileInputStream.close();

          password = new char[len];
          for (int i = 0; i < len; i++) {
            password[i] = buffer[i];
          }
          clearPassword(buffer);
        } catch (Exception e) {
          e.printStackTrace(System.out);
          password = passwordName.toCharArray();
        }
      } else {
        password = passwordName.toCharArray();
      }
      encryptedPassword = encryptPassword(password);
      encryptedHashtable.put(passwordName, encryptedPassword);
      if (passwordDebug) System.out.println("passwordDebug: encryptedHashtable.put("+passwordName+") for "+ new String(password)); 
      clearPassword(password);
    } else {
      if (passwordDebug) System.out.println("passwordDebug: encryptedHashtable.get("+passwordName+") for "+ new String( decryptPassword(encryptedPassword))); 
    }
    return encryptedPassword;
  }

  public static char[] getPassword(String system, String userid) {
    Hashtable<?,?> userToPassword = (Hashtable<?,?>) systemToUser.get(system);
    if (userToPassword == null) {
      return null;
    } else {
      char[] encryptedPassword = (char[]) userToPassword.get(userid);
      if (encryptedPassword == null) {
        return null;
      } else {
        return decryptPassword(encryptedPassword);
      }
    }
  }

  public static char[] getEncryptedPassword(String system, String userid) {
    Hashtable<?, ?> userToPassword = systemToUser.get(system);
    if (userToPassword == null) {
      return null;
    } else {
      return (char[]) userToPassword.get(userid);
    }
  }

  public static void clearPassword(char[] password) {
     if (password == null) return ; 
    for (int i = 0; i < password.length; i++) {
      password[i] = ' ';
    }
  }

  public static char[] encryptPassword(char[] password) {
    if (password == null) return null; 
    char[] encryptedPassword = new char[password.length];
    for (int i = 0; i < password.length; i++) {
      encryptedPassword[i] = (char) (password[i] ^ 0x0055);
    }
    return encryptedPassword;
  }

  public static char[] decryptPassword(char[] encryptedPassword) {
    if (encryptedPassword == null) return null; 
    char[] password = new char[encryptedPassword.length];
    for (int i = 0; i < encryptedPassword.length; i++) {
      password[i] = (char) (encryptedPassword[i] ^ 0x0055);
    }
    return password;
  }

  public static String decryptPasswordLeak(char[] encryptedPassword) { 
    return decryptPasswordLeak(encryptedPassword, getDescriptionFromStack()); 
  }
  
  private static String getDescriptionFromStack() {
    Exception e = new Exception(); 
    
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    e.printStackTrace(printWriter);
    String exception = stringWriter.toString();   
    int lastLineBreak = exception.lastIndexOf('\n');
    int previousLineBreak = exception.lastIndexOf('\n', lastLineBreak-1);
    return exception.substring(previousLineBreak+1, lastLineBreak); 
  }

  /* Leak password a string -- needed for several JDBC properties testcases */ 
  public static String decryptPasswordLeak(char[] encryptedPassword, String description)  {
      try { 
	  if (TestDriver.checkPasswordLeak) {
	      throw new IllegalArgumentException("decryptPasswordLeak called when TestDriver.checkPasswordLeak enabled"); 
	  }
      } catch (NoClassDefFoundError error) {
	  // Just ignore error 
      }
      if (encryptedPassword == null) return null; 
    String returnString; 
    String infoString; 
    char[] passwordChars = decryptPassword(encryptedPassword);
    returnString = new String(passwordChars); 
    infoString = returnString+":PasswordLeaked:"+description; 
    globalString  = infoString; 
    return returnString; 
    
  }
  public static void main(String args[]) {
    String[] testStrings = { "passw0rd", "this!isaTest09",
        "whatMoreCanIDoForthis?!@#$" };

    int passedCount = 0;
    int failedCount = 0;
    for (int i = 0; i < testStrings.length; i++) {
      try {
        // Reset the cache
        encryptedHashtable.clear(); 
        char[] chars = testStrings[i].toCharArray();
        setPassword("as400", "JAVA", chars);
        String backPassword = new String(getPassword("as400", "JAVA"));
        if (!testStrings[i].equals(backPassword)) {
          System.out
              .println(" Error: " + testStrings[i] + " became " + backPassword);
          failedCount++;
        } else {
          passedCount++;
        }

        String iniFileName = "testPass.txt";
        File f = new File("ini/" + iniFileName);

        FileWriter writer = new FileWriter(f);
        for (int j = 0; j < chars.length; j++) {
          writer.append(chars[j]);
        }
        writer.append('\n');
        writer.close();

        setPasswordFromIniFile("as400b", "JAVA", iniFileName);
        backPassword = new String(getPassword("as400b", "JAVA"));
        if (!testStrings[i].equals(backPassword)) {
          System.out.println(" For IniFile: Error: " + testStrings[i]
              + " became " + backPassword);
          failedCount++;
        } else {
          passedCount++;
        }

      } catch (Exception e) {
        e.printStackTrace(System.out);
        failedCount++;
      }

    }

    System.out.println(
        "DONE:  passedCount=" + passedCount + " failedCount=" + failedCount);

  }

}
