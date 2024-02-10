///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PTFGroupTestcase.java
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

import test.Testcase;

import java.util.Date;
import java.util.Hashtable;


/**
  Testcase PTFGroupTestcase.
**/
public class PTFGroupTestcase extends Testcase
{

    static final boolean DEBUG = false;

    private boolean brief_;

    /**
     Constructor.
     **/
    public PTFGroupTestcase(AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             AS400    pwrSys,
                             boolean  brief,
                             boolean  isLocal,
                             boolean  isNative)
    {
        super(systemObject, "PTFGroupTestcase", namesAndVars, runMode, fileOutputStream);

        if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
            throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

        pwrSys_ = pwrSys;
        brief_ = brief;
        isNative_ = isNative;
        isLocal_ = isLocal;
    }

    private static boolean isNumeric(String stringVal)
    {
      boolean isDigits = true;
      for (int i=0; i<stringVal.length() && isDigits; i++)
      {
        char ch = stringVal.charAt(i);
        if (!Character.isDigit(ch)) {
          isDigits = false;
        }
      }
      return isDigits;
    }

    private static boolean validate(PTF ptf, PTF priorPTF, String groupName, int groupStatus) throws AS400Exception
    {
      boolean succeeded = true;
      try
      {
        String action = ptf.getActionRequired();
        int action_int = Integer.parseInt(action);
        if (action_int < 0 || action_int > 2) {
          succeeded = false;
          System.out.println("PTFGroup.getActionRequired() returned " + action);
        }

        String[] apars = ptf.getAPARNumbers();
        if (apars == null) {
          succeeded = false;
          System.out.println("PTFGroup.getAPARNumbers() returned null.");
        }

        PTFCoverLetter letter0 = ptf.getCoverLetter();

        PTFCoverLetter letter1 = ptf.getCoverLetter(java.util.Locale.getDefault());

        PTFCoverLetter[] letters = ptf.getCoverLetters();
        if (letters == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getCoverLetters() returned null.");
        }

        Date created = ptf.getCreationDate();  // this can return null
        //if (created == null) {
        //  succeeded = false;
        //  System.out.println("ERROR: PTFGroup.getCreationDate() returned null.");
        //}

        String iplSource = ptf.getCurrentIPLSource();
        if (!iplSource.equals(PTF.IPL_SOURCE_A) &&
            !iplSource.equals(PTF.IPL_SOURCE_B) &&
            !iplSource.equals(PTF.IPL_SOURCE_UNKNOWN))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getCurrentIPLSource() returned " + iplSource);
        }

        PTF[] deps = ptf.getDependentPTFs();
        if (deps == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getDependentPTFs() returned null.");
        }

        PTFExitProgram[] exits = ptf.getExitPrograms();
        if (exits == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getExitPrograms() returned null.");
        }

        String ptfId = ptf.getID();
        if (ptfId == null || ptfId.length() != 7 || !isNumeric(ptfId.substring(2)))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getID() returned |" + ptfId + "|");
        }

        int iplAction = ptf.getIPLAction();
        if (iplAction != PTF.IPL_ACTION_NONE &&
            iplAction != PTF.IPL_ACTION_APPLY_TEMPORARY &&
            iplAction != PTF.IPL_ACTION_REMOVE_TEMPORARY &&
            iplAction != PTF.IPL_ACTION_APPLY_PERMANENT &&
            iplAction != PTF.IPL_ACTION_REMOVE_PERMANENT)
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getIPLAction() returned " + iplAction);
        }

        String iplReq = ptf.getIPLRequired();
        if (!iplReq.equals(PTF.PTF_TYPE_DELAYED) &&
            !iplReq.equals(PTF.PTF_TYPE_IMMEDIATE) &&
            !iplReq.equals(PTF.PTF_TYPE_UNKNOWN))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getIPLRequired() returned " + iplReq);
        }

        String licGroup = ptf.getLICGroup();
        if (licGroup == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getLICGroup() returned null.");
        }

        String loaded = ptf.getLoadedStatus();
        if (!loaded.equals(PTF.STATUS_NOT_LOADED) &&
            !loaded.equals(PTF.STATUS_LOADED) &&
            !loaded.equals(PTF.STATUS_APPLIED) &&
            !loaded.equals(PTF.STATUS_APPLIED_PERMANENT) &&
            !loaded.equals(PTF.STATUS_REMOVED_PERMANENT) &&
            !loaded.equals(PTF.STATUS_DAMAGED) &&
            !loaded.equals(PTF.STATUS_SUPERSEDED))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getLoadedStatus() returned " + loaded);
        }

        String maxLevel = ptf.getMaximumLevel();
        if (maxLevel == null || maxLevel.length() == 1 || maxLevel.length() > 2) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getMaximumLevel() returned |" + maxLevel + "|");
        }

        String minLevel = ptf.getMinimumLevel();
        if (minLevel == null || minLevel.length() == 1 || minLevel.length() > 2) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getMinimumLevel() returned |" + minLevel + "|");
        }

        String feature = ptf.getProductFeature();
        if (feature == null || feature.length() == 0 || feature.length() > 4 || !isNumeric(feature))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getProductFeature() returned |" + feature + "|");
        }

        String prodID = ptf.getProductID();
        if (prodID == null || prodID.length() != 7) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getProductID() returned |" + prodID + "|");
        }

        String prodOpt = ptf.getProductOption();
        if (prodOpt == null || prodOpt.length() == 0 || prodOpt.length() > 4 || !isNumeric(prodOpt))
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getProductOption() returned |" + prodOpt + "|");
        }

        String relation = null;
        if (priorPTF != null)
        {
          relation = ptf.getRelationship(priorPTF);
          if (!relation.equals(PTF.RELATIONSHIP_PREREQ) &&
              !relation.equals(PTF.RELATIONSHIP_COREQ) &&
              !relation.equals(PTF.RELATIONSHIP_DEPEND) &&
              !relation.equals(PTF.RELATIONSHIP_SAME) &&
              !relation.equals(PTF.RELATIONSHIP_DSTREQ) &&
              !relation.equals(PTF.RELATIONSHIP_NONE))
          {
            succeeded = false;
            System.out.println("ERROR: PTFGroup.getRelationship() returned " + relation);
          }
        }

        String release = ptf.getReleaseLevel();
        if (release == null || release.length() == 0 || release.length() > 7 || !release.startsWith("V")) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getReleaseLevel() returned " + release);
        }

        if (DEBUG)
        {
          System.out.println("  -------");
          System.out.println("  PTF id: " + ptfId);
          System.out.println("  PTF action: " + action);
          System.out.println("  PTF letter0: " + letter0);
          System.out.println("  PTF letter1: " + letter1);
          System.out.println("  PTF created: " + created);
          System.out.println("  PTF iplSource: " + iplSource);
          System.out.println("  PTF iplAction: " + iplAction);
          System.out.println("  PTF iplReq: " + iplReq);
          System.out.println("  PTF licGroup: " + licGroup);
          System.out.println("  PTF loaded: " + loaded);
          System.out.println("  PTF maxLevel: " + maxLevel);
          System.out.println("  PTF minLevel: " + minLevel);
          System.out.println("  PTF feature: " + feature);
          System.out.println("  PTF prodID: " + prodID);
          System.out.println("  PTF prodOpt: " + prodOpt);
          System.out.println("  PTF relation: " + relation);
          System.out.println("  PTF release: " + release);
        }

      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        System.out.println("ERROR: AS400Exception from query of PTF " + ptf.getID() + " in group " + groupName + " (status="+groupStatus+"): " + msg.getID() + ": " + msg.getText());
        // tolerate this error
      }
      catch (Exception e) {
        succeeded = false;
        e.printStackTrace();
      }

      return succeeded;
    }

    private static boolean validate(PTFGroup group) throws AS400Exception
    {
      boolean succeeded = true;
      try
      {
        boolean relatedIncl = group.areRelatedPTFGroupsIncluded();

        String desc = group.getPTFGroupDescription();
        if (desc == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getPTFGroupDescription() returned " + desc);
        }

        int level = group.getPTFGroupLevel();
        if (level < 0) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getPTFGroupLevel() returned " + level);
        }

        String groupName = group.getPTFGroupName();
        if (groupName == null || groupName.length() == 0) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getPTFGroupName() returned " + groupName);
        }

        int groupStatus = group.getPTFGroupStatus();
        if (groupStatus < 0 || groupStatus > 9) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getPTFGroupStatus() returned " + groupStatus);
        }

        AS400 sys = group.getSystem();
        if (sys == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getSystem() returned " + sys);
        }

        PTFGroup[] relatedGroups = group.getRelatedPTFGroups();
        if (relatedGroups == null) {
          succeeded = false;
          System.out.println("ERROR: PTFGroup.getRelatedPTFGroups() returned " + relatedGroups);
        }

        if (DEBUG)
        {
          System.out.println("-----");
          System.out.println("Group name: " + groupName);
          System.out.println("RelatedIncl: " + relatedIncl);
          System.out.println("Desc: " + desc);
          System.out.println("Level: " + level);
          System.out.println("Status: " + groupStatus);
          System.out.println("System: " + sys);
          System.out.println("Number of related PTF groups: " + relatedGroups.length);
        }

        PTF[] ptfs = group.getPTFs();
        if (ptfs == null) {
          //succeeded = false;
          System.out.println("PTFGroup.getPTFs() returned null.");
          return false;
        }
        if (ptfs.length == 0) {
          succeeded = false;
          System.out.println("PTFGroup.getPTFs() returned an empty list.");
        }
        PTF priorPTF = null;
        for (int j=0; j<ptfs.length && j<10; j++)
        {
          PTF ptf = ptfs[j];
          if (!validate(ptf, priorPTF, groupName, groupStatus)) succeeded = false;
          priorPTF = ptf;
        }
      }
      catch (AS400Exception e) { throw e; }
      catch (Exception e) {
        succeeded = false;
        e.printStackTrace();
      }

      return succeeded;
    }

    private static boolean validate(PTFGroupList list) throws AS400Exception
    {
      boolean succeeded = true;

      try
      {
        AS400 system = list.getSystem();
        if (system == null)
        {
          succeeded = false;
          System.out.println("ERROR: PTFGroupList.getSystem() returned null.");
        }

        PTFGroup[] groups = list.getPTFGroup();
        if (groups == null || groups.length == 0) {
          System.out.println("ERROR: No PTF groups were returned by getPTFGroup().");
          return false;
        }

        for (int i=0; i<groups.length && i<10; i++)
        {
          PTFGroup group = groups[i];
          if (!validate(group)) succeeded = false;
        }
      }
      catch (AS400Exception e) { throw e; }
      catch (Exception e) {
        succeeded = false;
        e.printStackTrace();
      }

      return succeeded;
    }


    /**
     Construct a PTFGroupList, and validate its attributes.
     **/
    public void Var001()
    {
      boolean succeeded = true;
      try
      {
        // Note: We must specify a profile with authorization to WRKPTFGRP.
        PTFGroupList list = new PTFGroupList(pwrSys_);
        if (!validate(list)) succeeded = false;

        // If running natively, try using the *CURRENT user profile.
        if (onAS400_ && isLocal_ && isNative_)
        {
          AS400 localSys = new AS400();  // use *CURRENT
          if (DEBUG) System.out.println("Local username: " + localSys.getUserId());
          PTFGroupList list2 = new PTFGroupList(localSys);
          try
          {
            if (!validate(list2)) succeeded = false;
          }
          catch (AS400Exception e)
          {
            // See if error is "not authorized to QSYS/WRKPTFGRP"
            AS400Message msg = e.getAS400Message();
            if (msg.getID().equals("CPF9802"))
            {
              System.out.println("Tolerating \"not authorized\" error for user " + localSys.getUserId() + ": " + msg.toString());
            }
          }
        }

        assertCondition(succeeded);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }


    /**
     Verify that includeRelatedPTFGroups() works.
     **/
    public void Var002()
    {
      boolean succeeded = true;
      try
      {
        // Note: We must specify a profile with authorization to WRKPTFGRP.
        PTFGroupList list = new PTFGroupList(pwrSys_);
        PTFGroup[] groups = list.getPTFGroup();
	if (groups.length == 0) {
	    assertCondition(false, "No PTF groups reported on system");
	    return; 
	} 
        PTFGroup group = groups[0];

        if (group.areRelatedPTFGroupsIncluded())
        {
          succeeded = false;
          System.out.println("Error: Initial value returned by areRelatedPTFGroupsIncluded() is: true");
        }

        group.includeRelatedPTFGroups(true);

        if (!group.areRelatedPTFGroupsIncluded())
        {
          succeeded = false;
          System.out.println("Error: Second value returned by areRelatedPTFGroupsIncluded() is: false");
        }

        group.includeRelatedPTFGroups(false);

        if (group.areRelatedPTFGroupsIncluded())
        {
          succeeded = false;
          System.out.println("Error: Third value returned by areRelatedPTFGroupsIncluded() is: true");
        }

        assertCondition(succeeded);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

}
