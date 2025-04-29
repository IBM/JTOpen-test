///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SBTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.misc;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.QSYSObjectPathName;
import com.ibm.as400.access.Subsystem;
import com.ibm.as400.access.SystemPool;

import test.Testcase;


/**
  Testcase SBTestcase.
**/
public class SBTestcase extends Testcase
{

    static final boolean DEBUG = false;

    private static final boolean REFRESHED = true;
    private static final boolean STARTED = true;

    private static String subsystemName1_ = "TESTSBS1";
    private static String subsystemName2_ = "TESTSBS2";
    private static String subsystemLib_ = "SBSLIB";

    private static CommandCall pwrCmd_;
   
    /**
     Constructor.
     **/
    public SBTestcase(AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             AS400    pwrSys,
                             boolean  brief)
    {
        super(systemObject, "SBTestcase", namesAndVars, runMode, fileOutputStream);

        if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

        pwrSys_ = pwrSys;
      
    }

    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
      if (DEBUG) output_.println("Running testcase setup ...");
      try
      {
        pwrCmd_ = new CommandCall(pwrSys_);
        Subsystem sbs1 = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        Subsystem sbs2 = new Subsystem(pwrSys_, subsystemLib_, subsystemName2_);
        {
          if (sbs1.exists()) { sbs1.endImmediately(); sbs1.delete(); }
          if (sbs2.exists()) { sbs2.endImmediately(); sbs2.delete(); }
          deleteLibrary(subsystemLib_);
          createLibrary(subsystemLib_);

          sbs1.create();
          sbs1.changeDescriptionText("Test subsystem 1 for Toolbox Subsystem component");

          sbs2.create();
          sbs2.changeDescriptionText("Test subsystem 2 for Toolbox Subsystem component");
        }
      }
      catch(AS400Exception e) {
        output_.println("Testcase setup failed.");
        e.printStackTrace(output_);
        AS400Message[] msgList = e.getAS400MessageList();
        for (int i = 0; i < msgList.length; ++i) {
          output_.println(msgList[i].getText());
        }
      }
      catch(Exception e) {
        output_.println("Testcase setup failed.");
        e.printStackTrace(output_);
      }
    }

    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
      if (DEBUG) output_.println("Running testcase cleanup ...");
      {
        Subsystem sbs1 = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        Subsystem sbs2 = new Subsystem(pwrSys_, subsystemLib_, subsystemName2_);
        if (sbs1.exists()) { sbs1.endImmediately(); sbs1.delete(); }
        if (sbs2.exists()) { sbs2.endImmediately(); sbs2.delete(); }
	deleteLibrary(pwrCmd_,subsystemLib_);
      }
    }

    static void delete(Subsystem sbs)
    {
      if (sbs == null) return;
      if (DEBUG) System.out.println("DEBUG: Deleting subsystem: " + sbs.getPath());
      try {
        sbs.delete();
        if (sbs.exists()) System.out.println("ERROR: Subsystem still exists after delete(): " + sbs.getName());
      }
      catch (Exception e) { e.printStackTrace(); }
    }

    static void delete(String libName, String fileName)
    {
      try {
        if (!pwrCmd_.run("QSYS/DLTF FILE(" + libName+"/"+fileName + ")")) {
        }
      }
      catch (Exception e) { e.printStackTrace(); }
    }

    private static boolean createLibrary(String libName)
    {
      try { if (!pwrCmd_.run("QSYS/CRTLIB LIB("+libName+")")) return false; }
      catch (Exception e) { e.printStackTrace(); return false; }
      return true;
    }



    // Validate all of the attribute values of a Subsystem object.
    private static boolean validateAttributeValues(Subsystem sbs, boolean refreshed, boolean started, String[] expectedStatuses)
    {
      return validateAttributeValues(sbs, refreshed, started, expectedStatuses, "SBSLIB", "TESTSBS1");
    }


    // Validate all of the attribute values of a Subsystem object.
    private static boolean validateAttributeValues(Subsystem sbs, boolean refreshed, boolean started, String[] expectedStatuses, String expectedLib, String expectedName)
    {
      boolean ok = true;

      try
      {
        if (DEBUG) display(sbs);

        AS400 system1 = sbs.getSystem();
        String lib1 = sbs.getLibrary();
        String name1 = sbs.getName();
        String path1 = sbs.getPath();
        String status1 = sbs.getStatus();
        int numJobs1 = sbs.getCurrentActiveJobs();
        int maxJobs1 = sbs.getMaximumActiveJobs();
        Job monitorJob1 = sbs.getMonitorJob();
        String desc1 = sbs.getDescriptionText();
        String dspFilePath1 = sbs.getDisplayFilePath();
        String langLib1 = sbs.getLanguageLibrary();
        ObjectDescription objDesc1 = sbs.getObjectDescription();
        String sbsAsString1 = sbs.toString();
        boolean exists1 = sbs.exists();

        if (system1.getSystemName().length() == 0) {
          ok = false;
          System.out.println("System has zero-length name");
        }

        if (system1.getUserId().length() == 0) {
          ok = false;
          System.out.println("System userid is zero-length");
        }

        if (!lib1.equals(expectedLib)) {
          ok = false;
          System.out.println("Library name is incorrect");
        }

        if (!name1.equals(expectedName)) {
          ok = false;
          System.out.println("Subsystem name is incorrect");
        }

        String pathQ = new QSYSObjectPathName(expectedLib, expectedName, "SBSD").getPath();

        if (!path1.equals(pathQ)) {
          ok = false;
          System.out.println("Subsystem path is incorrect");
        }

        if (expectedStatuses == null) {
          if (status1 != null) {
            ok = false;
            System.out.println("Status is not null");
          }
        }
        else {
          if (status1 == null) {
            ok = false;
            System.out.println("Status is null");
          }
          else {
            boolean matched = false;
            for (int i=0; i<expectedStatuses.length && !matched; i++) {
              if (status1.equals(expectedStatuses[i])) matched=true;
            }
            if (!matched) {
              ok = false;
              System.out.println("Status is not as expected.  Got: " + status1);
            }
          }
        }

        if (!exists1) {
          ok = false;
          System.out.println("Existence is incorrect");
        }

        if (numJobs1 < 0) {
          ok = false;
          System.out.println("Number of jobs is invalid (less than 0)");
        }

        if (numJobs1 > 100000) {
          ok = false;
          System.out.println("Number of jobs is invalid (greater than 100000)");
        }

        if (maxJobs1 < -1) { // Note that -1 indicates "no maximum".
          ok = false;
          System.out.println("Max jobs is invalid (less than -1)");
        }

        if (maxJobs1 > 100000) {
          ok = false;
          System.out.println("Max jobs is invalid (greater than 100000)");
        }

        String nameFromObjDesc = (String)objDesc1.getValue(ObjectDescription.NAME);
        if (!name1.equals(nameFromObjDesc)) {
          ok = false;
          System.out.println("Name is inconsistent with ObjectDescription");
        }

        String libFromObjDesc = (String)objDesc1.getValue(ObjectDescription.LIBRARY);
        if (!lib1.equals(libFromObjDesc)) {
          ok = false;
          System.out.println("Library is inconsistent with ObjectDescription");
        }

        if (sbsAsString1.trim().length() == 0) {
          ok = false;
          System.out.println("toString() returned a zero-length string");
        }

        if (refreshed)
        {
          if (desc1 == null) {
            ok = false;
            System.out.println("Description is null");
          }
          else if (desc1.length() > 50) {
            ok = false;
            System.out.println("Description is longer than 50 chars");
          }

          if (dspFilePath1 == null) {
            ok = false;
            System.out.println("Display file path is null");
          }

          if (langLib1 == null) {
            ok = false;
            System.out.println("Language library is null");
          }

          if (started)
          {
            if (monitorJob1 == null) {
              ok = false;
              System.out.println("Monitor job is null");
            }
            else if (monitorJob1.getName().trim().length() == 0) {
              ok = false;
              System.out.println("Monitor job has zero-length name");
            }
          }
          else // not started
          {
            if (monitorJob1 != null) {
              ok = false;
              System.out.println("Monitor job is not null");
            }
          }

          String descFromObjDesc = (String)objDesc1.getValue(ObjectDescription.TEXT_DESCRIPTION);
          if (desc1 != null && !desc1.equals(descFromObjDesc)) {
            ok = false;
            System.out.println("Description is inconsistent with ObjectDescription");
          }
        }

        else // not refreshed
        {
          if (desc1 != null) {
            ok = false;
            System.out.println("Description is not null");
          }

          if (monitorJob1 != null) {
            ok = false;
            System.out.println("Monitor job is not null");
          }

          if (dspFilePath1 != null) {
            ok = false;
            System.out.println("Display file path is not null");
          }

          if (langLib1 != null) {
            ok = false;
            System.out.println("Language library is not null");
          }


        }

      }
      catch (Exception e) { e.printStackTrace(); ok = false; }

      return ok;
    }


    // Verify that the attribute values of two Subsystem objects match.
    // Note: If matchAll==false, then certain attributes are not compared, because they are expected to mismatch.
    private static boolean attributeValuesMatch(Subsystem sbs1, Subsystem sbs2/*, boolean matchAll*/)
    {
      boolean ok = true;
      boolean matchAll = true;
      try
      {
        AS400 system1 = sbs1.getSystem();
        String lib1 = sbs1.getLibrary();
        String name1 = sbs1.getName();
        String path1 = sbs1.getPath();
        String status1 = sbs1.getStatus();
        int numJobs1 = sbs1.getCurrentActiveJobs();
        int maxJobs1 = sbs1.getMaximumActiveJobs();
        Job monitorJob1 = sbs1.getMonitorJob();
        String desc1 = sbs1.getDescriptionText();
        String dspFilePath1 = sbs1.getDisplayFilePath();
        // String langLib1 = sbs1.getLanguageLibrary();
        ObjectDescription objDesc1 = sbs1.getObjectDescription();
        // String sbsAsString1 = sbs1.toString();
        boolean exists1 = sbs1.exists();

        AS400 system2 = sbs2.getSystem();
        String lib2 = sbs2.getLibrary();
        String name2 = sbs2.getName();
        String path2 = sbs2.getPath();
        String status2 = sbs2.getStatus();
        int numJobs2 = sbs2.getCurrentActiveJobs();
        int maxJobs2 = sbs2.getMaximumActiveJobs();
        Job monitorJob2 = sbs2.getMonitorJob();
        String desc2 = sbs2.getDescriptionText();
        String dspFilePath2 = sbs2.getDisplayFilePath();
        // String langLib2 = sbs2.getLanguageLibrary();
        ObjectDescription objDesc2 = sbs2.getObjectDescription();
        // String sbsAsString2 = sbs2.toString();
        boolean exists2 = sbs2.exists();

        if (!system1.getSystemName().equals(system2.getSystemName())) {
          ok = false;
          System.out.println("Attribute mismatch: systemName ("+system1.getSystemName()+" vs " + system2.getSystemName() + ")");
        }

        if (!system1.getUserId().equals(system2.getUserId())) {
          ok = false;
          System.out.println("Attribute mismatch: userID ("+system1.getUserId()+" vs " + system2.getUserId() + ")");
        }

        if (!lib1.equals(lib2)) {
          ok = false;
          System.out.println("Attribute mismatch: lib ("+lib1+" vs " + lib2 + ")");
        }

        if (matchAll && !name1.equals(name2)) {
          ok = false;
          System.out.println("Attribute mismatch: name ("+name1+" vs " + name2 + ")");
        }

        if (matchAll && !path1.equals(path2)) {
          ok = false;
          System.out.println("Attribute mismatch: path ("+path1+" vs " + path2 + ")");
        }

        if (status1 == null) {
          if (status2 != null) {
            ok = false;
            System.out.println("Attribute mismatch: status ("+status1+" vs " + status2 + ")");
          }
        }
        else if (!status1.equals(status2)) {
          ok = false;
          System.out.println("Attribute mismatch: status ("+status1+" vs " + status2 + ")");
        }

        if (monitorJob1 == null) {
          if (monitorJob2 != null) {
            ok = false;
            System.out.println("Attribute mismatch: monitorJob ("+monitorJob1+" vs " + monitorJob2 + ")");
          }
        }
        else if (monitorJob2 == null) {
          ok = false;
          System.out.println("Attribute mismatch: monitorJob ("+monitorJob1.getNumber()+" vs " + monitorJob2 + ")");
        }
        else if (!monitorJob1.getNumber().equals(monitorJob2.getNumber())) {
          ok = false;
          System.out.println("Attribute mismatch: monitorJob ("+monitorJob1.getNumber()+" vs " + monitorJob2.getNumber() + ")");
        }

        if (exists1 != exists2) {
          ok = false;
          System.out.println("Attribute mismatch: exists ("+exists1+" vs " + exists2 + ")");
        }

        if (numJobs1 != numJobs2) {
          ok = false;
          System.out.println("Attribute mismatch: numJobs ("+numJobs1+" vs " + numJobs2 + ")");
        }

        if (maxJobs1 != maxJobs2) {
          ok = false;
          System.out.println("Attribute mismatch: maxJobs ("+maxJobs1+" vs " + maxJobs2 + ")");
        }

        if (desc1 == null) {
          if (desc2 != null) {
            ok = false;
            System.out.println("Attribute mismatch: desc ("+desc1+" vs " + desc2 + ")");
          }
        }
        else if (!desc1.equals(desc2)) {
          ok = false;
          System.out.println("Attribute mismatch: desc ("+desc1+" vs " + desc2 + ")");
        }

        if (dspFilePath1 == null) {
          if (dspFilePath2 != null) {
            ok = false;
            System.out.println("Attribute mismatch: dspFilePath ("+dspFilePath1+" vs " + dspFilePath2 + ")");
          }
        }
        else if (!dspFilePath1.equals(dspFilePath2)) {
          ok = false;
          System.out.println("Attribute mismatch: dspFilePath ("+dspFilePath1+" vs " + dspFilePath2 + ")");
        }

        if (matchAll && !objDesc1.equals(objDesc2)) {
          ok = false;
          System.out.println("Attribute mismatch: objDesc ("+objDesc1+" vs " + objDesc2 + ")");
        }

      }
      catch (Exception e) { e.printStackTrace(); ok = false; }

      return ok;
    }


    // Display all attributes of a Subsystem object.
    private static void display(Subsystem sbs)
    {
      if (sbs == null) {
        System.out.println("\n(null)");
        return;
      }
      try
      {
        AS400 system = sbs.getSystem();
        String lib = sbs.getLibrary();
        String name = sbs.getName();
        String path = sbs.getPath();
        String status = sbs.getStatus();
        int numJobs = sbs.getCurrentActiveJobs();
        int maxJobs = sbs.getMaximumActiveJobs();
        Job monitorJob = sbs.getMonitorJob();
        String desc = sbs.getDescriptionText();
        ObjectDescription objDesc = sbs.getObjectDescription();
        String dspFilePath = sbs.getDisplayFilePath();
        String langLib = sbs.getLanguageLibrary();
        String sbsAsString = sbs.toString();
        boolean exists = sbs.exists();
        SystemPool[] pools = sbs.getPools();

        System.out.println("\nSubsystem attribute values:");
        System.out.println("   path: " + path);
        System.out.println("   name: " + name);
        System.out.println("   library: " + lib);
        System.out.println("   desc: " + desc);
        System.out.println("   system: " + (system == null ? "null" : system.getSystemName()));
        System.out.println("   status: " + status);
        System.out.println("   monitorJob: " + (monitorJob == null ? "null" : monitorJob.getNumber()) );
        System.out.println("   numJobs: " + numJobs);
        System.out.println("   maxJobs: " + maxJobs);
        System.out.println("   exists: " + exists);
        System.out.println("   dspFilePath: " + dspFilePath);
        System.out.println("   langLib: " + langLib);
        System.out.println("   ObjectDescription: "+objDesc);
        System.out.println("   Pools: ");
        if (pools == null) System.out.println("      null list");
        else for (int i=0; i<pools.length; i++) {
          System.out.println("      " + (pools[i] == null ? "null" : pools[i].getName()));
        }
        System.out.println("   String representation: " + sbsAsString);
        System.out.println();
      }
      catch (Exception e) { e.printStackTrace(); }
    }

    static void display(SystemPool[] pools)
    {
      for (int i=0; i<pools.length; i++)
      {
        System.out.println("SBTestcase.display(pools["+i+"])");
        display(pools[i]);
      }
    }

    static void display(StringBuffer sb, SystemPool[] pools)
    {
      for (int i=0; i<pools.length; i++)
      {
	  sb.append("SBTestcase.display(pools["+i+"])\n");
	  display(sb, pools[i]);
      }
    }


    @SuppressWarnings("deprecation")
    static void display(SystemPool pool)
    {
      if (pool == null) {
        System.out.println("\n(null)");
        return;
      }
      try
      {
        System.out.println("\nAttributes of pool named |" + pool.getName() + "|");
        System.out.println("  ActiveToIneligible: " + pool.getActiveToIneligible());
        System.out.println("  ActiveToWait: " + pool.getActiveToWait());
        System.out.println("  DatabaseFaults: " + pool.getDatabaseFaults());
        System.out.println("  DatabasePages: " + pool.getDatabasePages());
        System.out.println("  Description: " + pool.getDescription());
        System.out.println("  MaximumActiveThreads: " + pool.getMaximumActiveThreads());
        System.out.println("  PoolActivityLevel: " + pool.getActivityLevel());
        System.out.println("  NonDatabaseFaults: " + pool.getNonDatabaseFaults());
        System.out.println("  NonDatabasePages: " + pool.getNonDatabasePages());
        System.out.println("  PagingOption: " + pool.getPagingOption());
        System.out.println("  PoolIdentifier: " + pool.getIdentifier());
        System.out.println("  PoolSize: " + pool.getSize());
        System.out.println("  ReservedSize: " + pool.getReservedSize());
        System.out.println("  SubsystemLibrary: " + pool.getSubsystemLibrary());
        System.out.println("  SubsystemName: " + pool.getSubsystemName());
        System.out.println("  getSystem: " + pool.getSystem().getSystemName());
        System.out.println("  WaitToIneligible: " + pool.getWaitToIneligible());
        System.out.println("  isCaching: " + pool.isCaching());
        System.out.println("  isShared: " + pool.isShared());
        System.out.println();
      }
      catch (Exception e) { e.printStackTrace(); }
    }


    @SuppressWarnings("deprecation")
    static void display(StringBuffer sb, SystemPool pool)
    {
      if (pool == null) {
        sb.append("\n(null)\n");
        return;
      }
      try
      {
        sb.append("\nAttributes of pool named |" + pool.getName() + "|\n");
        sb.append("  ActiveToIneligible: " + pool.getActiveToIneligible()+"\n");
        sb.append("  ActiveToWait: " + pool.getActiveToWait()+"\n");
        sb.append("  DatabaseFaults: " + pool.getDatabaseFaults()+"\n");
        sb.append("  DatabasePages: " + pool.getDatabasePages()+"\n");
        sb.append("  Description: " + pool.getDescription()+"\n");
        sb.append("  MaximumActiveThreads: " + pool.getMaximumActiveThreads()+"\n");
        sb.append("  PoolActivityLevel: " + pool.getActivityLevel()+"\n");
        sb.append("  NonDatabaseFaults: " + pool.getNonDatabaseFaults()+"\n");
        sb.append("  NonDatabasePages: " + pool.getNonDatabasePages()+"\n");
        sb.append("  PagingOption: " + pool.getPagingOption()+"\n");
        sb.append("  PoolIdentifier: " + pool.getIdentifier()+"\n");
        sb.append("  PoolSize: " + pool.getSize()+"\n");
        sb.append("  ReservedSize: " + pool.getReservedSize()+"\n");
        sb.append("  SubsystemLibrary: " + pool.getSubsystemLibrary()+"\n");
        sb.append("  SubsystemName: " + pool.getSubsystemName()+"\n");
        sb.append("  getSystem: " + pool.getSystem().getSystemName()+"\n");
        sb.append("  WaitToIneligible: " + pool.getWaitToIneligible()+"\n");
        sb.append("  isCaching: " + pool.isCaching()+"\n");
        sb.append("  isShared: " + pool.isShared()+"\n");
        sb.append("\n");
      }
      catch (Exception e) { e.printStackTrace(); }
    }



    /**
     Construct a Subsystem instance representing an existing (private) subsystem, and validate its attributes.
     **/
    public void Var001()
    {
      boolean ok = true;
      try
      {
        Subsystem sbs1 = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);

        String[] expectedStatuses = null;

        if (!validateAttributeValues(sbs1, !REFRESHED, !STARTED, expectedStatuses)) {
          ok = false;
          System.out.println("Attributes not valid before first refresh().");
        }

        sbs1.refresh();
        expectedStatuses = new String[] {"*INACTIVE"};
        if (!validateAttributeValues(sbs1, REFRESHED, !STARTED, expectedStatuses)) {
          ok = false;
          System.out.println("Attributes not valid after refresh() but before start().");
        }

        sbspwr.start();
        Thread.sleep(2000);  // give it time to start
        sbs1.refresh();
        expectedStatuses = new String[] {"*ACTIVE"};
        if (!validateAttributeValues(sbs1, REFRESHED, STARTED, expectedStatuses)) {
          ok = false;
          System.out.println("Attributes not valid after start().");
        }

        sbspwr.endImmediately();
        Thread.sleep(4000);  // give the subsys a few seconds to end
        sbs1.refresh();
        expectedStatuses = new String[] {"*INACTIVE", "*ENDING"};
        if (!validateAttributeValues(sbs1, REFRESHED, !STARTED, expectedStatuses)) {
          ok = false;
          System.out.println("Attributes not valid after endImmediately().");
        }

        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }


    /**
     Construct a Subsystem instance representing an existing standard subsystem, and validate its attributes.
     **/
    public void Var002()
    {
      boolean ok = true;
      String sbslib = "QSYS";
      String sbsName = "QSPL";
      try
      {
        Subsystem sbs1 = new Subsystem(systemObject_, sbslib, sbsName);

        String[] expectedStatuses = null;

        if (!validateAttributeValues(sbs1, !REFRESHED, STARTED, expectedStatuses, sbslib, sbsName)) {
          ok = false;
          System.out.println("Attributes not valid before first refresh().");
        }

        sbs1.refresh();
        expectedStatuses = new String[] {"*ACTIVE"};
        if (!validateAttributeValues(sbs1, REFRESHED, STARTED, expectedStatuses, sbslib, sbsName)) {
          ok = false;
          System.out.println("Attributes not valid after refresh() but before start().");
        }

        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }


    /**
     Construct two Subsystem instances representing the same existing subsystem, and verify that their attributes match.
     **/
    public void Var003()
    {
      boolean ok = true;
      try
      {
        Subsystem sbs1 = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbs2= new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);

        if (!attributeValuesMatch(sbs1, sbs2)) {
          ok = false;
          System.out.println("Attributes mismatch before first refresh().");
        }
        sbs1.refresh();
        sbs2.refresh();
        if (!attributeValuesMatch(sbs1, sbs2)) {
          ok = false;
          System.out.println("Attributes mismatch after refresh() but before start().");
        }

        sbspwr.start();
        Thread.sleep(2000);  // give it time to start
        sbs1.refresh();
        sbs2.refresh();
        if (!attributeValuesMatch(sbs1, sbs2)) {
          ok = false;
          System.out.println("Attributes mismatch after start().");
        }

        sbspwr.endImmediately();
        Thread.sleep(3000);  // give the subsys a few seconds to end
        sbs1.refresh();
        sbs2.refresh();
        if (!attributeValuesMatch(sbs1, sbs2)) {
          ok = false;
          System.out.println("Attributes mismatch after endImmediately().");
        }

        if (ok) succeeded();
        else failed();
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Pass null for first parm.
     A NullPointerException should be thrown.
     **/
    public void Var004()
    {
      try
      {
        Subsystem sbs = new Subsystem(null, subsystemLib_, subsystemName1_);
        failed("No exception"+sbs);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "system");
      }
    }

    /**
     Pass null for second parm.
     A NullPointerException should be thrown.
     **/
    public void Var005()
    {
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, null, subsystemName1_);
        failed("No exception"+sbs);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "library");
      }
    }

    /**
     Pass null for third parm.
     A NullPointerException should be thrown.
     **/
    public void Var006()
    {
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, null);
        failed("No exception"+sbs);
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "name");
      }
    }


    /**
     Verify that exists() returns false for a nonexistent subsystem, and true for one that exists().
     **/
    public void Var007()
    {
      boolean ok = true;
      try
      {
        Subsystem sbs1 = new Subsystem(systemObject_, subsystemLib_, "NONESUCH");
        if (sbs1.exists()) {
          System.out.println("exists() reported true for subsystem NONESUCH");
          ok = false;
        }

        Subsystem sbs2 = new Subsystem(systemObject_, "QSYS", "QINTER");
        if (!sbs2.exists()) {
          System.out.println("exists() reported false for subsystem QINTER");
          ok = false;
        }

        assertCondition(ok);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }


    /**
     Verify the equals() method.
     **/
    public void Var008()
    {
      try
      {
        Subsystem sbs1 = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbs2= new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbs3= new Subsystem(systemObject_, subsystemLib_, subsystemName2_);

        assertCondition(sbs1.equals(sbs1) &&
                        sbs1.equals(sbs2) &&
                        !sbs1.equals(sbs3));
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }



    // Variations that test the setters:
    //   public void changeDescriptionText(String text)
    //   public void setMaximumActiveJobs(int maxJobs)
    //   public void setDisplayFilePath(String library, String name)
    //   public void setLanguageLibrary(String library)


    /**
     Verify that changeDescriptionText() works.
     **/
    public void Var009()
    {
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        sbs.refresh();
        String oldValue = sbs.getDescriptionText();
        String newValue = "New description ; Old was: " + oldValue +")";
        if (newValue.length() > 50) newValue = newValue.substring(50);
        sbspwr.changeDescriptionText(newValue);
        sbs.refresh();
        if (!sbs.getDescriptionText().equals(newValue)) {
          failed("First set failed");
          return;
        }
        sbspwr.changeDescriptionText(oldValue);
        sbs.refresh();
        assertCondition(sbs.getDescriptionText().equals(oldValue));
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Verify that setMaximumActiveJobs() works.
     **/
    public void Var010()
    {
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        sbs.refresh();
        int oldValue = sbs.getMaximumActiveJobs();
        int newValue = ( oldValue == Subsystem.NO_MAX ? 999 : Subsystem.NO_MAX);
        sbspwr.changeMaximumActiveJobs(newValue);
        sbs.refresh();
        if (sbs.getMaximumActiveJobs() != newValue) {
          failed("First set failed");
          return;
        }
        sbspwr.changeMaximumActiveJobs(oldValue);
        sbs.refresh();
        assertCondition(sbs.getMaximumActiveJobs() == oldValue);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Verify that setDisplayFilePath() works.
     **/
    public void Var011()
    {
      boolean ok = true;
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        sbs.refresh();

        String pathA = "/QSYS.LIB/QDSIGNON.FILE";
        String pathB = "/QSYS.LIB/NONESUCH.FILE";

        String oldValue = sbs.getDisplayFilePath();
        String newValue = ( oldValue.equals(pathA) ? pathB : pathA);
        sbspwr.changeDisplayFilePath(newValue);
        sbs.refresh();
        if (!sbs.getDisplayFilePath().equals(newValue)) {
          System.out.println("First set failed");
          ok = false;
        }
        sbspwr.changeDisplayFilePath(oldValue);
        sbs.refresh();
        if (!sbs.getDisplayFilePath().equals(oldValue)) {
          System.out.println("Reset failed");
          ok = false;
        }

        assertCondition(ok);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Verify that setLanguageLibrary() works.
     **/
    public void Var012()
    {
      boolean ok = true;
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);
        sbs.refresh();
        String oldValue = sbs.getLanguageLibrary();
        String newValue = ( oldValue.equals("*NONE") ? subsystemLib_ : "*NONE");

        sbspwr.changeLanguageLibrary(newValue);
        sbs.refresh();
        if (!sbs.getLanguageLibrary().equals(newValue)) {
          System.out.println("First set failed");
          ok = false;
        }

        sbspwr.changeLanguageLibrary("");
        sbs.refresh();
        if (!sbs.getLanguageLibrary().equals("*NONE")) {
          System.out.println("Second set failed");
          ok = false;
        }

        sbspwr.changeLanguageLibrary(oldValue);
        sbs.refresh();
        if (!sbs.getLanguageLibrary().equals(oldValue)) {
          System.out.println("Third set failed");
          ok = false;
        }

        assertCondition(ok);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

    /**
     Verify that assignPool(int,int,int) works.  Also: assignPool(int,String).
     **/
    public void Var013()
    {
      StringBuffer sb = new StringBuffer(); 
      boolean ok = true;
      SystemPool sharedPool1 = null;
      int poolSize_old = 0;
      boolean changedSharedPoolAttributes = false;
      try
      {
        Subsystem sbs = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbspwr = new Subsystem(pwrSys_, subsystemLib_, subsystemName1_);

        SystemPool[] pools;

        // Add a private (subsystem) pool.
        sb.append("Adding private pool at position 2.\n");
        sbspwr.assignPool(2, 256, 5);

        // In order for SHRPOOL's to show up on WRKSYSSTS and WRKSBS, they need to be assigned some storage.  Use CHGSHRPOOL to do this.  First query their current settings (using the SystemPool class).
        sharedPool1 = new SystemPool(pwrSys_, "*SHRPOOL1");
        try {
          poolSize_old = sharedPool1.getSize();
        }
        catch (ObjectDoesNotExistException e) {
          // The shared pool wasn't noticed by QWCRSSTS, so assume it currently has no storage assigned.
          sb.append("*SHRPOOL1 not found by QWCRSSTS, so assume zero storage.\n");
          poolSize_old = 0;
        }

        sb.append("Original size of SHRPOOL1: " + poolSize_old);
        int NEWSIZE = 512;
        if (poolSize_old == 0)
        { // We need to assign some storage to the shared pool.
          sb.append("Resetting size of SHRPOOL1 to " + NEWSIZE);
          sharedPool1.setCaching(true);
          sharedPool1.setSize(NEWSIZE);
          sharedPool1.setActivityLevel(1);
          sharedPool1.commitCache();
          changedSharedPoolAttributes = true;
	} else {
	    NEWSIZE=poolSize_old; 
	}

        // Add a shared (system) pool.
        sb.append("Adding system pool *SHRPOOL1.\n");
        sbspwr.assignPool(3, "*SHRPOOL1");

        // Add another private pool.
        sb.append("Adding private pool at position 4.\n");
        sbspwr.assignPool(4, 1024, 6);

        // Start the subsystem, so that the private pools show up on WRKSYSSTS and WRKSBS.
        sbspwr.start();
        Thread.sleep(2000);  // give it time to start

        sbs.refresh();
        pools = sbs.getPools();
        display(sb, pools);

        if (pools.length != 10) {
          failed("getPools() returned incorrect number of pools.  Expected 10, got " + pools.length);
          return;
        }

        // Check attributes of pool #1.
        if (!pools[0].getName().equals("*BASE")) {
          System.out.println("[A] Name of pool 1 not correct.  Expected: *BASE ; Got: " + pools[0].getName());
          ok = false;
        }


        // Check attributes of pool #2.
        if (!pools[1].getName().equals("2")) {
          System.out.println("[A] Name of pool 2 not correct.  Expected: 2 ; Got: " + pools[1].getName());
          ok = false;
        }

        if (pools[1].getSize() != 256) {
          System.out.println("[A] Size of pool 2 not correct.  Expected: 256 ; Got: " + pools[1].getSize());
          ok = false;
        }

        if (pools[1].getActivityLevel() != 5) {
          System.out.println("[A] Activity level of pool 2 not correct.  Expected: 5 ; Got: " + pools[1].getActivityLevel());
          ok = false;
        }


        // Check attributes of pool #3.
        if (!pools[2].getName().equals("*SHRPOOL1")) {
          System.out.println("[A] Name of pool 3 not correct.  Expected: *SHRPOOL1 ; Got: " + pools[2].getName());
          ok = false;
        }

        // Note: For shared pools, the actually-allocated size is unpredictable (it depends on how much system storage is available).  Check that it's between 0 and the requested ("defined") size.
        if (pools[2].getSize() < 0 || pools[2].getSize() > NEWSIZE) {
          System.out.println("[A] Size of pool 3 not correct.  Expected: " + NEWSIZE + " ; Got: " + pools[2].getSize());
          ok = false;
        }

        if (pools[2].getActivityLevel() != 1 && changedSharedPoolAttributes) {
          System.out.println("[A] Activity level of pool 3 not correct.  Expected: 1 ; Got: " + pools[2].getActivityLevel());
          ok = false;
        }


        // Check attributes of pool #4.
        if (!pools[3].getName().equals("4")) {
          System.out.println("[A] Name of pool 4 not correct.  Expected: 4 ; Got: " + pools[3].getName());
          ok = false;
        }

        if (pools[3].getSize() != 1024) {
          System.out.println("[A] Size of pool 4 not correct.  Expected: 1024 ; Got: " + pools[3].getSize());
          ok = false;
        }

        if (pools[3].getActivityLevel() != 6) {
          System.out.println("[A] Activity level of pool 4 not correct.  Expected: 6 ; Got: " + pools[3].getActivityLevel());
          ok = false;
        }

        for (int i=4; i<10; i++) {
          if (pools[i] != null) {
            System.out.println("[A] pools["+i+"] is non-null.");
            ok = false;
          }
        }


        // Remove a pool.
        sbspwr.endImmediately();
        Thread.sleep(3000);    // give it time to end
        sbspwr.removePool(2);  // the private pool
        Thread.sleep(2000);    // give it time to be removed
        sbspwr.start();
        Thread.sleep(3000);    // give it time to start
        sbs.refresh();
        pools = sbs.getPools();
        display(sb, pools);


        // Check attributes of pool #1.
        if (!pools[0].getName().equals("*BASE")) {
          System.out.println("[B] Name of pool 1 not correct.  Expected: *BASE ; Got: " + pools[0].getName());
          ok = false;
        }


        // Check attributes of pool #2.
        if (pools[1] != null) {
          System.out.println("[B] Pool 2 not null.  Got: " + pools[1].getName());
          ok = false;
        }


        // Check attributes of pool #3.
        if (!pools[2].getName().equals("*SHRPOOL1")) {
          System.out.println("[B] Name of pool 3 not correct.  Expected: *SHRPOOL1 ; Got: " + pools[2].getName());
          ok = false;
        }

        // Note: For shared pools, the actually-allocated size is unpredictable (it depends on how much system storage is available).  Check that it's between 0 and the requested ("defined") size.
        if (pools[2].getSize() < 0 || pools[2].getSize() > NEWSIZE) {
          System.out.println("[B] Size of pool 3 not correct.  Expected: " + NEWSIZE + " ; Got: " + pools[2].getSize());
          ok = false;
        }

        if (pools[2].getActivityLevel() != 1 && changedSharedPoolAttributes) {
          System.out.println("[B] Activity level of pool 3 not correct.  Expected: 1 ; Got: " + pools[2].getActivityLevel());
          ok = false;
        }


        // Check attributes of pool #4.
        if (!pools[3].getName().equals("4")) {
          System.out.println("[B] Name of pool 4 not correct.  Expected: 4 ; Got: " + pools[3].getName());
          ok = false;
        }

        if (pools[3].getSize() != 1024) {
          System.out.println("[B] Size of pool 4 not correct.  Expected: 1024 ; Got: " + pools[3].getSize());
          ok = false;
        }

        if (pools[3].getActivityLevel() != 6) {
          System.out.println("[B] Activity level of pool 4 not correct.  Expected: 6 ; Got: " + pools[3].getActivityLevel());
          ok = false;
        }

        for (int i=4; i<10; i++) {
          if (pools[i] != null) {
            System.out.println("[B] pools["+i+"] is non-null.");
            ok = false;
          }
        }

        assertCondition(ok, sb.toString());
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
      finally {
        if (changedSharedPoolAttributes) {  // reset size of shared pool to original value
          try { sharedPool1.setSize(poolSize_old); } catch (Exception e) { e.printStackTrace(); }
        }
      }
    }

    /**
     Verify that listAllSubsystems() works.
     **/
    public void Var014()
    {
      try
      {
        Subsystem[] list = Subsystem.listAllSubsystems(systemObject_);
        if (DEBUG) {
          System.out.println("All subsystems on "+systemObject_.getSystemName()+":");
          for (int i=0; i<list.length; i++) {
            System.out.println("  "+list[i].getLibrary()+"/"+list[i].getName());
          }
        }

        // Check that the list includes both of the subsystems defined in this test bucket.

        Subsystem sbs1 = new Subsystem(systemObject_, subsystemLib_, subsystemName1_);
        Subsystem sbs2 = new Subsystem(systemObject_, subsystemLib_, subsystemName2_);
        Subsystem qspl = new Subsystem(systemObject_, "QSYS", "QSPL");

        boolean found1 = false;
        boolean found2 = false;
        boolean foundQspl = false;

        for (int i=0; i<list.length; i++) {
          if (list[i].equals(sbs1)) found1 = true;
          else if (list[i].equals(sbs2)) found2 = true;
          else if (list[i].equals(qspl)) foundQspl = true;
        }
        assertCondition(found1 && found2 && foundQspl);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

}
