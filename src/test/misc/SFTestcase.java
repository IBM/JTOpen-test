///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SFTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.misc;


import java.io.*;
import com.ibm.as400.access.*;
import com.ibm.as400.access.ObjectDescription;

import test.Testcase;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;

/**
 * Testcase SFTestcase.
 **/
public class SFTestcase extends Testcase {

  static boolean DEBUG = false;

  private static String savefileName1_ = "SAVFIL1"; // contains W95LIB as a
                                                    // library
  private static String savefileName2_ = "SAVFIL2"; // contains the files in
                                                    // W95LIB
  private static String savefileLib_ = "SAVFLIB";

  private static CommandCall cmd_;
  private static CommandCall pwrCmd_;
  private boolean brief_;

  static {
    if (System.getProperty("debug") != null) {
      DEBUG = true;
    }
  }
  
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SFTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SFTest.main(newArgs); 
   }


  /**
   * Constructor.
   **/
  public SFTestcase(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream,  AS400 pwrSys,
      boolean brief) {
    super(systemObject, "SFTestcase", namesAndVars, runMode, fileOutputStream);

    if (pwrSys == null || pwrSys.getSystemName().length() == 0
        || pwrSys.getUserId().length() == 0)
      throw new IllegalStateException(
          "ERROR: Please specify a power system via -pwrsys.");

    pwrSys_ = pwrSys;
    brief_ = brief;
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void setup() throws Exception {
    if (DEBUG)
      output_.println("Running testcase setup ...");
    try {
      cmd_ = new CommandCall(systemObject_);
      pwrCmd_ = new CommandCall(pwrSys_);
      SaveFile sf1 = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      SaveFile sf2 = new SaveFile(systemObject_, savefileLib_, savefileName2_);
      SaveFile sf1pwr = new SaveFile(pwrSys_, savefileLib_, savefileName1_);
      SaveFile sf2pwr = new SaveFile(pwrSys_, savefileLib_, savefileName2_);
      // if (DEBUG)
      // {
      // if (!sf1.exists()) {
      // createLibrary(savefileLib_);
      // sf1.create();
      // sf1.setDescription("Test save file for Toolbox SAVEFILE class");
      // cmd_.run("QSYS/SAVLIB LIB(W95LIB) DEV(*SAVF)
      // SAVF("+savefileLib_+"/"+savefileName1_+")");
      // }
      // if (!sf2.exists()) {
      // createLibrary(savefileLib_);
      // sf2.create();
      // sf2.setDescription("Test save file for Toolbox SAVEFILE class");
      // pwrCmd_.run("QSYS/SAVOBJ OBJ(*ALL) LIB(W95LIB) DEV(*SAVF)
      // SAVF("+savefileLib_+"/"+savefileName2_+")");
      // }
      // }
      // else
      {
        if (sf1pwr.exists())
          sf1pwr.delete();
        if (sf2pwr.exists())
          sf2pwr.delete();
        deleteLibrary(savefileLib_);
        createLibrary(savefileLib_);

        sf1.create();
        sf1.setDescription("Test save file for Toolbox SaveFile component");
        cmd_.run("QSYS/SAVLIB LIB(W95LIB) DEV(*SAVF) SAVF(" + savefileLib_ + "/"
            + savefileName1_ + ")");

        sf2.create();
        sf2.setDescription("Test save file for Toolbox SaveFile component");
        pwrCmd_.run("QSYS/SAVOBJ OBJ(*ALL) LIB(W95LIB) DEV(*SAVF) SAVF("
            + savefileLib_ + "/" + savefileName2_ + ")");
      }
    } catch (AS400Exception e) {
      output_.println("Testcase setup failed.");
      e.printStackTrace(output_);
      AS400Message[] msgList = e.getAS400MessageList();
      for (int i = 0; i < msgList.length; ++i) {
        output_.println(msgList[i].toString());
      }
    } catch (Exception e) {
      output_.println("Testcase setup failed.");
      e.printStackTrace(output_);
    }
  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  protected void cleanup() throws Exception {
    if (DEBUG)
      output_.println("Running testcase cleanup ...");
    if (!DEBUG) {
      SaveFile sf = new SaveFile(pwrSys_, savefileLib_, savefileName1_);
      if (sf.exists())
        sf.delete();
      if (super.getFailed() == 0) {
        deleteLibrary(pwrCmd_, savefileLib_);
      } else {
        output_.println("Failed Tests!!! Not deleting "+savefileLib_); 
      }
    }
  }

  private static boolean exists(SaveFile sf) {
    if (sf == null)
      return false;
    try {
      return sf.exists();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private static void delete(SaveFile sf) {
    if (sf == null)
      return;
    if (DEBUG)
      System.out.println("DEBUG: Deleting savefile: " + sf.getPath());
    try {
      sf.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void delete(String libName, String fileName) {
    try {
      if (!pwrCmd_.run("QSYS/DLTF FILE(" + libName + "/" + fileName + ")")) {
        if (DEBUG) {
          AS400Message[] msgList = pwrCmd_.getMessageList();
          for (int i = 0; i < msgList.length; ++i) {
            System.err.println(msgList[i].toString());
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean createLibrary(String libName) {
    boolean answer = true;
    String command; 
    try {
	command="QSYS/CRTLIB LIB(" + libName + ") AUT(*ALL)";
	if (!pwrCmd_.run(command)) {
	    System.out.println("Error FAILE: "+command); 
	    return false;
	}
    } catch (Exception e) {
      e.printStackTrace();
      answer = false;
    }
    try {
      pwrCmd_.run("GRTOBJAUT OBJ(" + libName + ") OBJTYPE(*LIB) USER(" + userId_
          + ") AUT(*ALL)");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return answer;
  }

  // Validate all of the attribute values of a SaveFile object.
  private static boolean validateAttributeValues(SaveFile sf) {
    boolean ok = true;

    try {
      AS400 system = sf.getSystem();
      String lib = sf.getLibrary();
      String name = sf.getName();
      String path = sf.getPath();
      boolean exists = sf.exists();
      SaveFileEntry[] entries = sf.listEntries();
      // Product[] prods = sf.listProducts();
      long numRecs = sf.getCurrentNumberOfRecords();
      long maxRecs = sf.getMaximumNumberOfRecords();
      int waitTime = sf.getWaitTime();
      // boolean shared = sf.isShared();
      String desc = sf.getDescription();
      long length = sf.getLength();
      ObjectDescription objDesc = sf.getObjectDescription();
      String sfAsString = sf.toString();

      if (system.getSystemName().length() == 0) {
        ok = false;
        System.out.println("System has zero-length name");
      }

      if (system.getUserId().length() == 0) {
        ok = false;
        System.out.println("System userid is zero-length");
      }

      if (!lib.equals(savefileLib_)) {
        ok = false;
        System.out.println("Library name is incorrect");
      }

      if (!name.equals(savefileName1_)) {
        ok = false;
        System.out.println("SaveFile name is incorrect");
      }

      String path0 = new QSYSObjectPathName(savefileLib_, savefileName1_,
          "FILE").getPath();

      if (!path.equals(path0)) {
        ok = false;
        System.out.println("SaveFile path is incorrect");
      }

      if (!exists) {
        ok = false;
        System.out.println("Existence is incorrect");
      }

      if (entries.length == 0) {
        ok = false;
        System.out.println("listEntries() reported no entries");
      }

      if (DEBUG)
        listEntries(sf);

      // Note: Not all savefiles contain product loads.

      if (numRecs <= 0L) {
        ok = false;
        System.out.println("Number of entries is invalid");
      }

      if (maxRecs < 0L) { // Note that 0 indicates "no maximum".
        ok = false;
        System.out.println("Max entries is invalid");
      }

      if (waitTime < -1 || waitTime > 32767) { // Note that -1 indicates
                                               // "immediate".
        ok = false;
        System.out.println("Wait time is invalid");
      }

      if (desc.length() > 50) {
        ok = false;
        System.out.println("Description is longer than 50 chars");
      }

      String descFromObjDesc = (String) objDesc
          .getValue(ObjectDescription.TEXT_DESCRIPTION);
      if (!desc.equals(descFromObjDesc)) {
        ok = false;
        System.out
            .println("Description is inconsistent with ObjectDescription");
      }

      if (length <= 0L || length > 1099511627776L) {
        ok = false;
        System.out.println("Length is incorrect");
      }

      String nameFromObjDesc = (String) objDesc
          .getValue(ObjectDescription.NAME);
      if (!name.equals(nameFromObjDesc)) {
        ok = false;
        System.out.println("Name is inconsistent with ObjectDescription");
      }

      String libFromObjDesc = (String) objDesc
          .getValue(ObjectDescription.LIBRARY);
      if (!lib.equals(libFromObjDesc)) {
        ok = false;
        System.out.println("Library is inconsistent with ObjectDescription");
      }

      if (sfAsString.trim().length() == 0) {
        ok = false;
        System.out.println("toString() returned a zero-length string");
      }
    } catch (Exception e) {
      e.printStackTrace();
      ok = false;
    }

    return ok;
  }

  // Verify that the attribute values of two SaveFile objects match.
  // Note: If matchAll==false, then certain attributes are not compared, because
  // they are expected to mismatch.
  private static boolean attributeValuesMatch(SaveFile sf1, SaveFile sf2,
      boolean matchAll) {
    boolean ok = true;

    try {
      AS400 system1 = sf1.getSystem();
      String lib1 = sf1.getLibrary();
      String name1 = sf1.getName();
      String path1 = sf1.getPath();
      boolean exists1 = sf1.exists();
      SaveFileEntry[] entries1 = sf1.listEntries();
      Product[] prods1 = sf1.listProducts();
      long numRecs1 = sf1.getCurrentNumberOfRecords();
      long maxRecs1 = sf1.getMaximumNumberOfRecords();
      int waitTime1 = sf1.getWaitTime();
      boolean shared1 = sf1.isShared();
      String desc1 = sf1.getDescription();
      long length1 = sf1.getLength();
      ObjectDescription objDesc1 = sf1.getObjectDescription();

      AS400 system2 = sf2.getSystem();
      String lib2 = sf2.getLibrary();
      String name2 = sf2.getName();
      String path2 = sf2.getPath();
      boolean exists2 = sf2.exists();
      SaveFileEntry[] entries2 = sf2.listEntries();
      Product[] prods2 = sf2.listProducts();
      long numRecs2 = sf2.getCurrentNumberOfRecords();
      long maxRecs2 = sf2.getMaximumNumberOfRecords();
      int waitTime2 = sf2.getWaitTime();
      boolean shared2 = sf2.isShared();
      String desc2 = sf2.getDescription();
      long length2 = sf2.getLength();
      ObjectDescription objDesc2 = sf2.getObjectDescription();
      // String sfAsString2 = sf2.toString();

      if (!system1.getSystemName().equals(system2.getSystemName())) {
        ok = false;
        System.out.println("Attribute mismatch: systemName ("
            + system1.getSystemName() + " vs " + system2.getSystemName() + ")");
      }

      if (!system1.getUserId().equals(system2.getUserId())) {
        ok = false;
        System.out.println("Attribute mismatch: userID (" + system1.getUserId()
            + " vs " + system2.getUserId() + ")");
      }

      if (!lib1.equals(lib2)) {
        ok = false;
        System.out
            .println("Attribute mismatch: lib (" + lib1 + " vs " + lib2 + ")");
      }

      if (matchAll && !name1.equals(name2)) {
        ok = false;
        System.out.println(
            "Attribute mismatch: name (" + name1 + " vs " + name2 + ")");
      }

      if (matchAll && !path1.equals(path2)) {
        ok = false;
        System.out.println(
            "Attribute mismatch: path (" + path1 + " vs " + path2 + ")");
      }

      if (exists1 != exists2) {
        ok = false;
        System.out.println(
            "Attribute mismatch: exists (" + exists1 + " vs " + exists2 + ")");
      }

      if (entries1.length != entries2.length) {
        ok = false;
        System.out.println("Attribute mismatch: entries (" + entries1.length
            + " vs " + entries2.length + ")");
      }

      if (prods1.length != prods2.length) {
        ok = false;
        System.out.println("Attribute mismatch: prods (" + prods1.length
            + " vs " + prods2.length + ")");
      }

      if (numRecs1 != numRecs2) {
        ok = false;
        System.out.println("Attribute mismatch: numRecs (" + numRecs1 + " vs "
            + numRecs2 + ")");
      }

      if (maxRecs1 != maxRecs2) {
        ok = false;
        System.out.println("Attribute mismatch: maxRecs (" + maxRecs1 + " vs "
            + maxRecs2 + ")");
      }

      if (waitTime1 != waitTime2) {
        ok = false;
        System.out.println("Attribute mismatch: waitTime (" + waitTime1 + " vs "
            + waitTime2 + ")");
      }

      if (shared1 != shared2) {
        ok = false;
        System.out.println(
            "Attribute mismatch: shared (" + shared1 + " vs " + shared2 + ")");
      }

      if (!desc1.equals(desc2)) {
        ok = false;
        System.out.println(
            "Attribute mismatch: desc (" + desc1 + " vs " + desc2 + ")");
      }

      if (system1.getVRM() == system2.getVRM()
          && system1.getVRM() <= 0x00050200) // only check length if v5r2 and
                                             // earlier
      {
        if (length1 != length2) {
          ok = false;
          System.out.println("Attribute mismatch: length (" + length1 + " vs "
              + length2 + ")");
        }
      }

      if (matchAll && !objDesc1.equals(objDesc2)) {
        ok = false;
        System.out.println("Attribute mismatch: objDesc (" + objDesc1 + " vs "
            + objDesc2 + ")");
      }

    } catch (Exception e) {
      e.printStackTrace();
      ok = false;
    }

    return ok;
  }

  // Verify that the contents of two libraries match.
  private boolean librariesMatch(String libName1, String libName2, StringBuffer sb) {
    boolean ok = true;

    try {
      String libPath1 = QSYSObjectPathName.toPath("QSYS", libName1, "LIB");
      String libPath2 = QSYSObjectPathName.toPath("QSYS", libName2, "LIB");

      IFSFile dir1 = new IFSFile(systemObject_, libPath1);
      IFSFile dir2 = new IFSFile(systemObject_, libPath2);

     
        if (dir1.exists())
          sb.append("\n " + dir1.getPath() + " exists.");
        else
          sb.append("\n " + dir1.getPath() + " not found.");
        if (dir2.exists())
          sb.append("\n " + dir2.getPath() + " exists.");
        else
          sb.append("\n " + dir2.getPath() + " not found.");
      
      // Get list of files in the two libraries.
      String[] list1 = dir1.list();
      String[] list2 = dir2.list();

      if (list1 == null) {
        sb.append("\n"+libName1 + ".list() returned null.");
        return false;
      }

      if (list2 == null) {
        sb.append("\n "+libName2 + ".list() returned null.");
        return false;
      }

      sb.append("\n " + libName1 + " contains " + list1.length + " files");
      sb.append("\n " + libName2 + " contains " + list2.length + " files");
      
      if (list1.length != list2.length) {
        sb.append("\nList lengths mismatch");
        ok = false;
      }

      // Put the file lists into vectors.
      Vector<String> vec1 = new Vector<String>(list1.length);
      for (int i = 0; i < list1.length; i++) {
        vec1.add(list1[i]);
      }
      Vector<String> vec2 = new Vector<String>(list2.length);
      for (int i = 0; i < list2.length; i++) {
        vec2.add(list2[i]);
      }
      
      Collections.sort(vec1);
      Collections.sort(vec2);

      if (!vec1.containsAll(vec2)) {
        sb.append("\n"+ libName1 + " does not contain all files in " + libName2);
        ok = false;
      }

      if (!vec2.containsAll(vec1)) {
        sb.append("\n"+libName2 + " does not contain all files in " + libName1);
        ok = false;
      }
      if (!ok) {
        int entryCount = list1.length; 
        if (list2.length > entryCount) {
          entryCount = list2.length; 
        }
        sb.append("\n "+libName1+","+libName2);
        for (int i = 0; i < entryCount; i++) { 
          sb.append("\n");
          if (i < list1.length) { 
            sb.append(vec1.elementAt(i));
          } else { 
            sb.append(" ------ ");
          }
          sb.append(","); 
          if (i < list2.length) { 
            sb.append(vec2.elementAt(i));
          } else { 
            sb.append(" ------ ");
          }
        }
      }
      
      
      
    } catch (Exception e) {
      printStackTraceToStringBuffer(e, sb); 
      
      ok = false;
    }

    return ok;
  }

  // Lists entries in a SaveFile object.
  private static void listEntries(SaveFile sf) {
    try {
      System.out.println("Entries in savefile:");
      SaveFileEntry[] entries = sf.listEntries();
      for (int i = 0; i < entries.length; i++) {
        SaveFileEntry entry = entries[i];
        System.out.println("library: " + entry.getLibrary() + " ; name: "
            + entry.getName() + " ; type: " + entry.getType());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Exercises the SaveFileEntry.compareTo() and .equals() methods.
  static boolean checkEqualsAndCompareTo(SaveFileEntry[] entries) {
    boolean ok = true;
    try {
      // Verify correct behavior of SaveFileEntry.compareTo() and .equals()
      SaveFileEntry priorEntry = entries[0];
      for (int i = 1; i < entries.length; i++) {
        SaveFileEntry thisEntry = entries[i];
        if (thisEntry.compareTo(priorEntry) == 0) {
          System.out.println("compareTo() returned 0 for different entries: "
              + thisEntry.toString() + " <--> " + priorEntry.toString());
          ok = false;
        }
        if (!thisEntry.equals(thisEntry)) {
          System.out.println(
              "equals() returned false for an entry compared with itself: "
                  + thisEntry.toString());
          ok = false;
        }
        priorEntry = thisEntry;
      }
    } catch (Exception e) {
      e.printStackTrace();
      ok = false;
    }

    return ok;
  }

  /**
   * Construct a SaveFile representing an existing savefile, and validate its
   * attributes.
   **/
  public void Var001() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      if (validateAttributeValues(sf))
        succeeded();
      else
        failed();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Pass null for first parm. A NullPointerException should be thrown.
   **/
  public void Var002() {
    try {
      SaveFile sf = new SaveFile(null, savefileLib_, savefileName1_);
      failed("No exception" + sf);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }

  /**
   * Pass null for second parm. A NullPointerException should be thrown.
   **/
  public void Var003() {
    try {
      SaveFile sf = new SaveFile(systemObject_, null, savefileName1_);
      failed("No exception" + sf);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "library");
    }
  }

  /**
   * Pass null for third parm. A NullPointerException should be thrown.
   **/
  public void Var004() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, null);
      failed("No exception" + sf);
    } catch (Exception e) {
      assertExceptionIs(e, "NullPointerException", "name");
    }
  }

  // Variations that test the setters:
  // setDescription(String description)
  // setMaximumNumberOfRecords(long maximumNumberOfRecords)
  // setShared(boolean shared)
  // setWaitTime(int seconds)

  /**
   * Verify that setDescription() works.
   **/
  public void Var005() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      String oldValue = sf.getDescription();
      String newValue = "New description ; Old: " + oldValue + ")";
      if (newValue.length() > 50)
        newValue = newValue.substring(50);
      sf.setDescription(newValue);
      sf.refresh();
      if (!sf.getDescription().equals(newValue)) {
        failed("First set failed");
        return;
      }
      sf.setDescription(oldValue);
      assertCondition(sf.getDescription().equals(oldValue));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Verify that setMaximumNumberOfRecords() works.
   **/
  public void Var006() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      long oldValue = sf.getMaximumNumberOfRecords();
      long newValue = (oldValue == SaveFile.NO_MAX ? 9999L : SaveFile.NO_MAX);
      sf.setMaximumNumberOfRecords(newValue);
      sf.refresh();
      if (sf.getMaximumNumberOfRecords() != newValue) {
        failed("First set failed");
        return;
      }
      sf.setMaximumNumberOfRecords(oldValue);
      assertCondition(sf.getMaximumNumberOfRecords() == oldValue);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Verify that setShared() works.
   **/
  public void Var007() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      boolean oldValue = sf.isShared();
      boolean newValue = (oldValue == true ? false : true);
      sf.setShared(newValue);
      sf.refresh();
      if (sf.isShared() != newValue) {
        failed("First set failed");
        return;
      }
      sf.setShared(oldValue);
      assertCondition(sf.isShared() == oldValue);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /**
   * Verify that setWaitTime() works.
   **/
  public void Var008() {
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      int oldValue = sf.getWaitTime();
      int newValue = (oldValue == SaveFile.IMMED ? SaveFile.CLS
          : SaveFile.IMMED);
      sf.setWaitTime(newValue);
      sf.refresh();
      if (sf.getWaitTime() != newValue) {
        failed("First set failed");
        return;
      }
      int newValue2 = (newValue == SaveFile.IMMED ? SaveFile.CLS
          : SaveFile.IMMED);
      sf.setWaitTime(newValue2);
      sf.refresh();
      if (sf.getWaitTime() != newValue2) {
        failed("Second set failed");
        return;
      }
      sf.setWaitTime(oldValue);
      assertCondition(sf.getWaitTime() == oldValue);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  // Public methods of the SaveFile class:

  /**
   * clear()
   **/
  public void Var009() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR009";
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      sf.copyTo(savefileLib_, newName);
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);
      if (!sf2.exists()) {
        failed("Variation setup failed: Failed to copy to second savefile.");
        return;
      }
      long numRecs = sf.getCurrentNumberOfRecords();
      long numRecs2 = sf2.getCurrentNumberOfRecords();
      if (numRecs != numRecs2) {
        failed(
            "Variation setup failed: Copied file has different number of records.");
        return;
      }
      sf2.clear();
      numRecs2 = sf2.getCurrentNumberOfRecords();
      assertCondition(numRecs2 == 0L);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * copyTo(String library, String name)
   **/
  public void Var010() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR010";
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      sf.copyTo(savefileLib_, newName);
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);

      String name2 = sf2.getName();
      String path2 = sf2.getPath();
      String expectedPath = new QSYSObjectPathName(savefileLib_, newName,
          "FILE").getPath();
      ObjectDescription objDesc2 = sf2.getObjectDescription();
      // String sfAsString2 = sf2.toString();

      // Check that the attribute values match (except for name, path, objDesc,
      // toString).
      assertCondition(name2.equals(newName) && path2.equals(expectedPath)
          && objDesc2.getSystem().equals(systemObject_)
          && objDesc2.getLibrary().equals(savefileLib_)
          && objDesc2.getType().equals(sf.getObjectDescription().getType())
          && attributeValuesMatch(sf, sf2, false));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * create() <-- exercised by setup()
   **/

  /**
   * create(long maxRecords, int asp, int waitTime, boolean shared, String
   * authority, String description)
   **/
  public void Var011() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR011";
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);
      sf2.create(999L, 1, 23, true, "*ALL", "Test savefile");

      // How check "authority" attribute?
      // Permission perm = new Permission(systemObject_, sf2.getPath());
      // UserPermission uPerm = perm.getUserPermission(xxx);

      // Check that the attribute values match are as expected.
      assertCondition(sf2.exists() && sf2.getSystem().equals(systemObject_)
          && sf2.getLibrary().equals(savefileLib_)
          && sf2.getName().equals(newName)
          && sf2.getMaximumNumberOfRecords() == 999L && sf2.getASP() == 1
          && sf2.getWaitTime() == 23 && sf2.isShared() == true &&
          // sf2.getAuthority().equals("*ALL") &&
          sf2.getDescription().equals("Test savefile"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * delete()
   **/
  public void Var012() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR012";
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);
      sf2.create();
      if (!sf2.exists()) {
        failed("Variation setup failed.");
        return;
      }

      sf2.delete();
      assertCondition(!sf2.exists());
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * boolean equals(Object obj)
   **/
  public void Var013() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR010";
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      sf.copyTo(savefileLib_, newName);
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);

      assertCondition(sf.equals(sf) && !sf.equals(sf2) && !sf.equals(newName)
          && !sf.equals(null));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * boolean exists() <--- already exercised in other variations
   **/

  /**
   * long getCurrentNumberOfRecords() <--- exercised by
   * validateAttributeValues()
   **/

  /**
   * String getDescription() <--- exercised by validateAttributeValues() and in
   * variation for setDescription()
   **/

  /**
   * Use streams to read and write SaveFiles
   **/
  public void Var014() {
    SaveFile sf2 = null;
    try {
      String newName = "VAR014";
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      sf.copyTo(savefileLib_, newName);
      sf2 = new SaveFile(systemObject_, savefileLib_, newName);

      IFSFile file = new IFSFile(systemObject_, sf2.getPath());
      long fileLength = file.length();

      if (fileLength >= sf2.getLength()) {
        System.out.println("Warning: Variation setup failed: fileLength >= SaveFile.getLength(): "
            + fileLength + ", " + sf2.getLength()+" sf.getLength="+sf.getLength());
      }
      IFSFileInputStream inStream = new IFSFileInputStream(systemObject_,
          sf2.getPath());
      int bytesAvail = inStream.available();
      if (bytesAvail != fileLength) {
        failed("Variation setup failed: bytesAvail != fileLength");
        inStream.close(); 
        return;
      }
      byte[] contents = new byte[bytesAvail];
      int bytesRead = inStream.read(contents);
      if (bytesRead != bytesAvail) {
        failed("Variation setup failed: bytesRead != bytesAvail");
        inStream.close(); 
        return;
      }
      inStream.close();
      sf2.clear();
      file.clearCachedAttributes();
      long fileLength2 = file.length();
      if (fileLength2 != 0) {
        failed("Variation setup failed: After clear(), fileLength == "
            + fileLength2);
        return;
      }
      IFSFileOutputStream outStream = new IFSFileOutputStream(systemObject_,
          sf2.getPath());
      outStream.write(contents);
      outStream.close();

      inStream = new IFSFileInputStream(systemObject_, sf2.getPath());
      bytesAvail = inStream.available();
      if (bytesAvail != fileLength) {
        failed("After clear and rewrite: bytesAvail != fileLength");
        inStream.close();
        return;
      }
      byte[] contents2 = new byte[bytesAvail];
      bytesRead = inStream.read(contents2);
      if (bytesRead != bytesAvail) {
        failed("After clear and rewrite: bytesRead != bytesAvail");
         inStream.close();
        return;
      }
      inStream.close();

      assertCondition(Arrays.equals(contents, contents2));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * long getLength() <--- exercised by validateAttributeValues() and other
   * variations
   **/

  /**
   * String getLibrary() <--- exercised by validateAttributeValues()
   **/

  /**
   * long getMaximumNumberOfRecords() <--- exercised by
   * validateAttributeValues()
   **/

  /**
   * String getName() <--- exercised by validateAttributeValues()
   **/

  /**
   * ObjectDescription getObjectDescription() <--- exercised by
   * validateAttributeValues()
   **/

  /**
   * OutputStream getOutputStream() <--- exercised by getInputStream variation
   **/

  /**
   * String getPath() <--- exercised by validateAttributeValues()
   **/

  /**
   * AS400 getSystem() <--- exercised by validateAttributeValues()
   **/

  /**
   * int getWaitTime() <--- exercised by validateAttributeValues()
   **/

  /**
   * boolean isShared() <--- exercised by validateAttributeValues()
   **/

  /**
   * SaveFileEntry[] listEntries() <--- exercised by validateAttributeValues()
   **/

  /**
   * SaveFileProductLoad[] listProductLoads() <--- exercised by
   * validateAttributeValues()
   **/

  /**
   * refresh() <--- exercised by other variations
   **/

  /**
   * renameTo(String name)
   **/
  public void Var015() {
    SaveFile sf2 = null;
    String oldName = "VAR015";
    String newName = "OTHERNAME";
    try {
      SaveFile sf = new SaveFile(systemObject_, savefileLib_, savefileName1_);
      sf.copyTo(savefileLib_, oldName);
      sf2 = new SaveFile(systemObject_, savefileLib_, oldName);

      sf2.renameTo(newName);
      sf2.refresh();

      if (!sf2.getName().equals(newName)) {
        failed("Variation setup failed: First rename failed");
        return;
      }

      sf2.renameTo(oldName);
      sf2.refresh();

      assertCondition(sf2.getName().equals(oldName));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      delete(savefileLib_, newName);
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * save(String libraryName)
   **/
  public void Var016() {
    StringBuffer sb = new StringBuffer(); 
    SaveFile sf2 = null;
    String libName = "LIB016";
    String sfName = libName;
    try {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);
      if (DEBUG)
        System.out.println("DEBUG: Copying library W95LIB to " + libName);
      if (!pwrCmd_.run(
          "QSYS/CPYLIB FROMLIB(W95LIB) TOLIB(" + libName + ") CRTLIB(*YES)")) {
        failed("Variation setup failed: CPYLIB to " + libName + " failed");
        return;
      }

      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 
      // Save the new library into a savefile, delete the library, then restore
      // the savefile.

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create(); // setDescription() needs the save file to exist
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");
      if (DEBUG)
        System.out.println("DEBUG: Saving library " + libName);
      sf2.save(libName);
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      if (deleteLibrary(libName) != null) {
        failed(
            "Variation setup failed: deleteLibrary of " + libName + " failed");
        return;
      }
      if (DEBUG) {
        System.out.println("DEBUG: Entries in savefile " + sf2.getPath() + ":");
        SaveFileEntry[] entries2 = sf2.listEntries();
        for (int i = 0; i < entries2.length; i++) {
          System.out.println(entries2[i].getName());
        }
      }
      if (DEBUG)
        System.out.println("DEBUG: Restoring library " + libName);
      sf2.restore(libName);
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 
      /* Make sure the user has access to the W95Lib */ 
      cmdRun("QSYS/GRTOBJAUT OBJ(W95LIB)       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ(W95LIB/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 
      
      assertCondition(librariesMatch("W95LIB", libName,sb),sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);
      if (!DEBUG && exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * save(String libraryName, String[] objectList)
   **/
  public void Var017() {
    boolean passed = true; 
    SaveFile sf2 = null;
    String sfName = "VAR017";
    try {
      // Save a specific object from library W95LIB into a savefile.

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create();
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");
      String[] objects = { "IRCTST02", "PROG10", "CLEANUP" };
      if (DEBUG)
        System.out.println("DEBUG: Saving 3 objects from W95LIB");
      sf2.save("W95LIB", objects);
      SaveFileEntry[] entries2 = sf2.listEntries();
      boolean okSoFar = checkEqualsAndCompareTo(entries2);
      Arrays.sort(entries2);
      passed = okSoFar && entries2.length == 3
          && entries2[0].getName().equals("CLEANUP")
          && entries2[0].getLibrary().equals("W95LIB")
          && entries2[1].getName().equals("IRCTST02")
          && entries2[1].getLibrary().equals("W95LIB")
          && entries2[2].getName().equals("PROG10")
          && entries2[2].getLibrary().equals("W95LIB");
      assertCondition(passed);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
      passed=false; 
    } finally {
      if (passed && !DEBUG && exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * save(String[] pathList)
   **/
  public void Var018() {
    SaveFile sf2 = null;
    String sfName = "VAR018";
    try {
      // Save a several files from IFS into a savefile.

      // Get a list of the files in /etc.
      IFSFile dir = new IFSFile(pwrSys_, "/etc");
      String[] names = dir.list();
      if (DEBUG) {
        System.out.println("Files in /etc :");
        for (int i = 0; i < names.length; i++) {
          System.out.println(names[i]);
        }
      }

      // Convert the filenames to fully-qualified pathnames.
      for (int i = 0; i < names.length; i++) {
        names[i] = "/etc/" + names[i];
      }

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create();
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");
      /// if (DEBUG) System.out.println("DEBUG: Saving object W95LIB/IRCTST02");
      sf2.save(names);

      // System.out.println ("View the contents of save file " + sf2.getPath() +
      // ". Press ENTER to continue."); try { System.in.read (); } catch
      // (Exception exc) {};
      //
      // listEntries(sf2);
      // SaveFileEntry[] entries2 = sf2.listEntries();
      // Arrays.sort(entries2);
      // if (entries2.length != names.length) {
      // failed("Array lengths mismatch. names[]: " + names.length + ";
      // entries2[]: " + entries2.length);
      // return;
      // }
      // boolean ok = true;
      // for (int i=0; i<entries2.length; i++) {
      // if (!entries2[i].getName().equals(names[i])) {
      // System.out.println("Mismatch: " + entries2[i].getName() + " != " +
      // names[i]);
      // ok = false;
      // }
      // }
      // assertCondition(ok == true);
      succeeded();
    } catch (AS400Exception e) {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().equals("CPF3838")) {
        // Perhaps not everything in /etc _can_ be saved into a save file.
        succeeded("Tolerating AS400Exception: " + msg.toString());
      } else {
        try {
          msg.load();
        } catch (Exception exc) {
          exc.printStackTrace();
        }
        System.out.println(msg.toString());
        System.out.println(msg.getHelp());
        failed(e, "Unexpected Exception");
      }
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * save(String[] pathList)
   **/
  public void Var019(int runMode) {
      notApplicable("Attended variation");
     }

  /**
   * save(Product), restore(Product), listProducts()
   **/
  public void Var020() {
    StringBuffer sb = new StringBuffer();
    boolean deleteSf2 = true;
    if (brief_) {
      notApplicable("Skipping long-running variation.");
      return;
    }
    SaveFile sf2 = null;
    String sfName = "VAR020";
    try {
      sb.append("Appending to save option 31 DNS .. If failure make sure system has option 31\n");
      // Save a product into a savefile, uninstall the product, then restore the
      // product from the savefile.

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create(); // setDescription() needs the save file to exist
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");

      String toolboxProdName = "NOT-SET";
      if (pwrSys_.getVRM() < 0x00060100)
        toolboxProdName = "5722SS1";
      else if (pwrSys_.getVRM() == 0x00060100)
        toolboxProdName = "5761SS1"; // changed product number in V6R1
      else if (pwrSys_.getVRM() >= 0x00070100)
        toolboxProdName = "5770SS1"; // changed product number in V7R1

      String option = "31"; // DNS
      Product product1 = new Product(pwrSys_, toolboxProdName, option);
      sb.append("Saving product/option " + toolboxProdName + "/" + option+"\n");
      sf2.save(product1);

      SaveFileEntry[] entries = sf2.listEntries();

      boolean okSoFar = checkEqualsAndCompareTo(entries);

      Product[] prods = sf2.listProducts();
      sb.append("Entries in savefile " + sf2.getPath() + ":\n");
      for (int i = 0; i < entries.length; i++) {
        sb.append(entries[i].getName());
        sb.append("\n");
      }
      sb.append("DEBUG: Products in savefile " + sf2.getPath() + ":\n");
      for (int i = 0; i < prods.length; i++) {
        sb.append(prods[i].getProductID() + "/" + prods[i].getProductOption());
        sb.append("\n");
      }

      Product product2 = new Product(pwrSys_, toolboxProdName, option);
      sb.append("Calling sf2.restore(product2)");
      sf2.restore(product2);
      product2.refresh();

      assertCondition(
          okSoFar && entries.length >= 5 && prods.length == 1
              && prods[0].getProductID().equals(toolboxProdName)
              && prods[0].getProductOption().equals("0031")
              && product2.isInstalled(),
          "okSorFar=" + okSoFar + "\n" + "entries.length=" + entries.length
              + "\n" + "prods[0].getProductID()=" + prods[0].getProductID()
              + " sb " + toolboxProdName + "\n" + "prods[0].getProductOption()="
              + prods[0].getProductOption() + "\n" + "product2.isInstalled()"
              + product2.isInstalled() + "\n" + "sb=" + sb.toString());
    } catch (AS400Exception e) {
      AS400Message[] msgs = e.getAS400MessageList();
      for (int i = 0; i < msgs.length; i++) {
        System.out.println(msgs[i].toString());
      }
      deleteSf2 = false;
      System.out.println("Not deleting " + savefileLib_ + "/" + sfName);
      failed(e, "Unexpected AS400Exception " + sb.toString());
    } catch (Exception e) {
      failed(e, "Unexpected Exception " + sb.toString());
    } finally {
      if (!DEBUG && exists(sf2)) {
        if (deleteSf2) {
          delete(sf2);
        }
      }
    }
  }

  /**
   * restore(String libraryName)
   **/
  public void Var021() {
    StringBuffer sb = new StringBuffer(); 
    SaveFile sf2 = null;
    String libName = "LIB021";
    String sfName = libName;
    try {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);
      if (DEBUG)
        System.out.println("DEBUG: Copying library W95LIB to " + libName);
      if (!pwrCmd_.run(
          "QSYS/CPYLIB FROMLIB(W95LIB) TOLIB(" + libName + ") CRTLIB(*YES)")) {
        failed("Variation setup failed: CPYLIB to " + libName + " failed");
        return;
      }

      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")      OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL) OBJTYPE(*ALL) USER("+userId_+")"); 

      // Save the new library into a savefile, delete the library, then restore
      // the savefile.

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create();
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");
      if (DEBUG)
        System.out.println("DEBUG: Saving library " + libName);
      if (!pwrCmd_.run("QSYS/SAVLIB LIB(" + libName + ") DEV(*SAVF) SAVF("
          + savefileLib_ + "/" + sfName + ")")) {
        failed("Variation setup failed: SAVLIB to " + sfName + " failed");
        return;
      }
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      if (deleteLibrary(libName) != null) {
        failed(
            "Variation setup failed: deleteLibrary of " + libName + " failed");
        return;
      }
      if (DEBUG) {
        System.out.println("DEBUG: Entries in savefile " + sf2.getPath() + ":");
        SaveFileEntry[] entries2 = sf2.listEntries();
        for (int i = 0; i < entries2.length; i++) {
          System.out.println(entries2[i].getName());
        }
      }
      if (DEBUG)
        System.out.println("DEBUG: Restoring library " + libName);
      sf2.restore(libName);
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 
      assertCondition(librariesMatch("W95LIB", libName, sb), sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    } finally {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);
      if (!DEBUG && exists(sf2)) {
        delete(sf2);
      }
    }
  }

  /**
   * restore(String libraryName, String[] objectList, null)
   **/
  public void Var022() {
      StringBuffer sb = new StringBuffer();
      sb.append("Note:  This test could fail is the permissions on the PROGS file are incorrect\n");
      sb.append("Run the test.SetupPgmCall test to correct"); 
      boolean passed = true; 
    SaveFile sf2 = null;
    String libName = "LIB022";
    String sfName = libName;
    try {
        sb.append("\nDEBUG: Deleting library " + libName);
      deleteLibrary(libName);

        sb.append("\nDEBUG: Copying library W95LIB to " + libName);
      if (!pwrCmd_.run(
          "QSYS/CPYLIB FROMLIB(W95LIB) TOLIB(" + libName + ") CRTLIB(*YES)")) {
        failed("Variation setup failed: CPYLIB to " + libName + " failed");
        return;
      }

      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")      OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL) OBJTYPE(*ALL) USER("+userId_+")"); 

      // Save the new library into a savefile, delete the library, then restore
      // the savefile.

      sf2 = new SaveFile(pwrSys_, savefileLib_, sfName);
      if (sf2.exists()) {
        sf2.delete();
      }
      sf2.create(); // The SAVLIB command requires that the save file exist.
      sf2.setDescription("Test save file for Toolbox SAVEFILE class");

        sb.append("\nDEBUG: Saving library " + libName);
      if (!pwrCmd_.run("QSYS/SAVLIB LIB(" + libName + ") DEV(*SAVF) SAVF("
          + savefileLib_ + "/" + sfName + ")")) {
        failed("Variation setup failed: SAVLIB to " + sfName + " failed");
        return;
      }

        sb.append("\nDEBUG: Deleting library " + libName);
      if (deleteLibrary(libName) != null) {
        failed(
            "Variation setup failed: deleteLibrary of " + libName + " failed");
        return;
      }

        sb.append("\nDEBUG: Entries in savefile " + sf2.getPath() + ":");
        SaveFileEntry[] entries2 = sf2.listEntries();
        for (int i = 0; i < entries2.length; i++) {
          sb.append("\n"+entries2[i].getName());
        }

        sb.append("\nDEBUG: Restoring library " + libName);
      String[] objectList = { "IRCTST02", "PROGS" };
      sf2.restore(libName, objectList, null);
      
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 

      String libPath1 = QSYSObjectPathName.toPath("QSYS", libName, "LIB");
      IFSFile dir1 = new IFSFile(systemObject_, libPath1);
        if (dir1.exists())
          sb.append("\n"+dir1.getPath() + " exists.");
        else
          sb.append("\n"+dir1.getPath() + " not found.");


      // Get list of files in the library.
      String[] list1 = dir1.list();
        sb.append("\nDEBUG: list1:");
        for (int i = 0; i < list1.length; i++)
          sb.append("\n"+list1[i]);
      Arrays.sort(list1);
      passed = (list1.length == 2) &&( list1[0].equals("IRCTST02.PGM"))
	&& (list1[1].equals("PROGS.FILE"));
      assertCondition(passed, sb);
    } catch (Exception e) {
	passed = false; 
      failed(e, "Unexpected Exception: "+sb.toString());
    } finally {
      if (passed && (!DEBUG) && exists(sf2)) {
	  deleteLibrary(libName);
	  delete(sf2);
      }
    }
  }

  /**
   * restore(String libraryName, String[] objectList, String toLibraryName)
   **/
  public void Var023() {
    SaveFile sf = null;
    String libName = "LIB023";
    try {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);

      if (DEBUG)
        System.out.println("DEBUG: Restoring library " + libName);
      sf = new SaveFile(pwrSys_, savefileLib_, savefileName2_);
      String[] objectList = { "IRCTST02", "PROGS" };
      sf.restore("W95LIB", objectList, libName);
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+")       AUT(*CHANGE)    OBJTYPE(*LIB) USER("+userId_+") ");
      cmdRun("QSYS/GRTOBJAUT OBJ("+libName+"/*ALL)  AUT(*CHANGE)    OBJTYPE(*ALL) USER("+userId_+")"); 

      String libPath1 = QSYSObjectPathName.toPath("QSYS", libName, "LIB");
      IFSFile dir1 = new IFSFile(systemObject_, libPath1);
      if (DEBUG) {
        if (dir1.exists())
          System.out.println(dir1.getPath() + " exists.");
        else
          System.out.println(dir1.getPath() + " not found.");
      }

      // Get list of files in the library.
      String[] list1 = dir1.list();
      if (DEBUG) {
        System.out.println("DEBUG: list1:");
        for (int i = 0; i < list1.length; i++)
          System.out.println(list1[i]);
      }
      Arrays.sort(list1);
      assertCondition(list1.length == 2 && list1[0].equals("IRCTST02.PGM")
          && list1[1].equals("PROGS.FILE"));
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
      listEntries(sf);
    } finally {
      if (DEBUG)
        System.out.println("DEBUG: Deleting library " + libName);
      deleteLibrary(libName);
    }
  }

  /**
   * serialize, deserialize
   **/
  public void Var024(int runMode) {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation");
      return; // This is an attended variation. The deserialized AS400 object
              // (inside the deserialized SaveFile) must prompt for a password.
    }
    String serializeFilename = "SFTestcase.ser";
    try {
      SaveFile sf1 = new SaveFile(systemObject_, savefileLib_, savefileName1_);

      FileOutputStream fos = new FileOutputStream(serializeFilename);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(sf1);
      oos.close();

      FileInputStream fis = new FileInputStream(serializeFilename);
      ObjectInputStream ois = new ObjectInputStream(fis);
      SaveFile sf2 = (SaveFile) ois.readObject();
      fis.close();

      assertCondition(attributeValuesMatch(sf1, sf2, true));
    } catch (Exception e) {
      failed(e);
    } finally {
      File f = new File(serializeFilename);
      f.delete();
    }
  }

}
