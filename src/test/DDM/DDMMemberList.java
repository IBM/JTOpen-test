///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMMemberList.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DDM;


import java.io.*;
import com.ibm.as400.access.*;

import test.Testcase;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable; import java.util.Vector;


/**
  Testcase DDMMemberList.
**/
public class DDMMemberList extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMMemberList";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

    static final boolean DEBUG = false;
    private static DateFormat dateFormatter_ = SimpleDateFormat.getDateTimeInstance();
    private static final String LIBRARY = "W95LIB";
    private static final String FILENAME = "QCLSRC";

    // private boolean brief_;

	/**
	Constructor.  This is called from the DDMTest constructor.
	 **/
  public DDMMemberList(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
			int runMode, FileOutputStream fileOutputStream,
			 String testLib, String password, AS400 pwrSys) {
		// Replace the third parameter (3) with the total number of variations
		// in this testcase.
		super(systemObject, "DDMMemberList", namesAndVars, runMode,
				fileOutputStream, password);
		pwrSys_ = pwrSys;


		// Make sure QCLSRC exists 
		try {
		    CommandCall cmd = new CommandCall(pwrSys_);
		    String command = "QSYS/CRTSRCPF "+LIBRARY+"/"+FILENAME;
		    try { 
			cmd.run(command);
		    } catch (Exception e) {
			e.printStackTrace(); 
		    }


		} catch (Exception e) {
		    e.printStackTrace(); 
		} 
  }

///    private static boolean isNumeric(String stringVal)
///    {
///      boolean isDigits = true;
///      for (int i=0; i<stringVal.length() && isDigits; i++)
///      {
///        char ch = stringVal.charAt(i);
///        if (!Character.isDigit(ch)) {
///          isDigits = false;
///        }
///      }
///      return isDigits;
///    }
///
///    private static boolean validate(PTF ptf, PTF priorPTF, String groupName, int groupStatus) throws AS400Exception
///    {
///      boolean succeeded = true;
///      try
///      {
///        String action = ptf.getActionRequired();
///        int action_int = Integer.parseInt(action);
///        if (action_int < 0 || action_int > 2) {
///          succeeded = false;
///          System.out.println("PTFGroup.getActionRequired() returned " + action);
///        }
///
///        String[] apars = ptf.getAPARNumbers();
///        if (apars == null) {
///          succeeded = false;
///          System.out.println("PTFGroup.getAPARNumbers() returned null.");
///        }
///
///        PTFCoverLetter letter0 = ptf.getCoverLetter();
///
///        PTFCoverLetter letter1 = ptf.getCoverLetter(java.util.Locale.getDefault());
///
///        PTFCoverLetter[] letters = ptf.getCoverLetters();
///        if (letters == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getCoverLetters() returned null.");
///        }
///
///        Date created = ptf.getCreationDate();  // this can return null
///        //if (created == null) {
///        //  succeeded = false;
///        //  System.out.println("ERROR: PTFGroup.getCreationDate() returned null.");
///        //}
///
///        String iplSource = ptf.getCurrentIPLSource();
///        if (!iplSource.equals(PTF.IPL_SOURCE_A) &&
///            !iplSource.equals(PTF.IPL_SOURCE_B) &&
///            !iplSource.equals(PTF.IPL_SOURCE_UNKNOWN))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getCurrentIPLSource() returned " + iplSource);
///        }
///
///        PTF[] deps = ptf.getDependentPTFs();
///        if (deps == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getDependentPTFs() returned null.");
///        }
///
///        PTFExitProgram[] exits = ptf.getExitPrograms();
///        if (exits == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getExitPrograms() returned null.");
///        }
///
///        String ptfId = ptf.getID();
///        if (ptfId == null || ptfId.length() != 7 || !isNumeric(ptfId.substring(2)))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getID() returned |" + ptfId + "|");
///        }
///
///        int iplAction = ptf.getIPLAction();
///        if (iplAction != PTF.IPL_ACTION_NONE &&
///            iplAction != PTF.IPL_ACTION_APPLY_TEMPORARY &&
///            iplAction != PTF.IPL_ACTION_REMOVE_TEMPORARY &&
///            iplAction != PTF.IPL_ACTION_APPLY_PERMANENT &&
///            iplAction != PTF.IPL_ACTION_REMOVE_PERMANENT)
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getIPLAction() returned " + iplAction);
///        }
///
///        String iplReq = ptf.getIPLRequired();
///        if (!iplReq.equals(PTF.PTF_TYPE_DELAYED) &&
///            !iplReq.equals(PTF.PTF_TYPE_IMMEDIATE) &&
///            !iplReq.equals(PTF.PTF_TYPE_UNKNOWN))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getIPLRequired() returned " + iplReq);
///        }
///
///        String licGroup = ptf.getLICGroup();
///        if (licGroup == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getLICGroup() returned null.");
///        }
///
///        String loaded = ptf.getLoadedStatus();
///        if (!loaded.equals(PTF.STATUS_NOT_LOADED) &&
///            !loaded.equals(PTF.STATUS_LOADED) &&
///            !loaded.equals(PTF.STATUS_APPLIED) &&
///            !loaded.equals(PTF.STATUS_APPLIED_PERMANENT) &&
///            !loaded.equals(PTF.STATUS_REMOVED_PERMANENT) &&
///            !loaded.equals(PTF.STATUS_DAMAGED) &&
///            !loaded.equals(PTF.STATUS_SUPERSEDED))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getLoadedStatus() returned " + loaded);
///        }
///
///        String maxLevel = ptf.getMaximumLevel();
///        if (maxLevel == null || maxLevel.length() == 1 || maxLevel.length() > 2) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getMaximumLevel() returned |" + maxLevel + "|");
///        }
///
///        String minLevel = ptf.getMinimumLevel();
///        if (minLevel == null || minLevel.length() == 1 || minLevel.length() > 2) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getMinimumLevel() returned |" + minLevel + "|");
///        }
///
///        String feature = ptf.getProductFeature();
///        if (feature == null || feature.length() == 0 || feature.length() > 4 || !isNumeric(feature))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getProductFeature() returned |" + feature + "|");
///        }
///
///        String prodID = ptf.getProductID();
///        if (prodID == null || prodID.length() != 7) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getProductID() returned |" + prodID + "|");
///        }
///
///        String prodOpt = ptf.getProductOption();
///        if (prodOpt == null || prodOpt.length() == 0 || prodOpt.length() > 4 || !isNumeric(prodOpt))
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getProductOption() returned |" + prodOpt + "|");
///        }
///
///        String relation = null;
///        if (priorPTF != null)
///        {
///          relation = ptf.getRelationship(priorPTF);
///          if (!relation.equals(PTF.RELATIONSHIP_PREREQ) &&
///              !relation.equals(PTF.RELATIONSHIP_COREQ) &&
///              !relation.equals(PTF.RELATIONSHIP_DEPEND) &&
///              !relation.equals(PTF.RELATIONSHIP_SAME) &&
///              !relation.equals(PTF.RELATIONSHIP_NONE))
///          {
///            succeeded = false;
///            System.out.println("ERROR: PTFGroup.getRelationship() returned " + relation);
///          }
///        }
///
///        String release = ptf.getReleaseLevel();
///        if (release == null || release.length() == 0 || release.length() > 7 || !release.startsWith("V")) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getReleaseLevel() returned " + release);
///        }
///
///        if (DEBUG)
///        {
///          System.out.println("  -------");
///          System.out.println("  PTF id: " + ptfId);
///          System.out.println("  PTF action: " + action);
///          System.out.println("  PTF letter0: " + letter0);
///          System.out.println("  PTF letter1: " + letter1);
///          System.out.println("  PTF created: " + created);
///          System.out.println("  PTF iplSource: " + iplSource);
///          System.out.println("  PTF iplAction: " + iplAction);
///          System.out.println("  PTF iplReq: " + iplReq);
///          System.out.println("  PTF licGroup: " + licGroup);
///          System.out.println("  PTF loaded: " + loaded);
///          System.out.println("  PTF maxLevel: " + maxLevel);
///          System.out.println("  PTF minLevel: " + minLevel);
///          System.out.println("  PTF feature: " + feature);
///          System.out.println("  PTF prodID: " + prodID);
///          System.out.println("  PTF prodOpt: " + prodOpt);
///          System.out.println("  PTF relation: " + relation);
///          System.out.println("  PTF release: " + release);
///        }
///
///      }
///      catch (AS400Exception e)
///      {
///        AS400Message msg = e.getAS400Message();
///        System.out.println("ERROR: AS400Exception from query of PTF " + ptf.getID() + " in group " + groupName + " (status="+groupStatus+"): " + msg.getID() + ": " + msg.getText());
///        // tolerate this error
///      }
///      catch (Exception e) {
///        succeeded = false;
///        e.printStackTrace();
///      }
///
///      return succeeded;
///    }
///
///    private static boolean validate(PTFGroup group) throws AS400Exception
///    {
///      boolean succeeded = true;
///      try
///      {
///        boolean relatedIncl = group.areRelatedPTFGroupsIncluded();
///
///        String desc = group.getPTFGroupDescription();
///        if (desc == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getPTFGroupDescription() returned " + desc);
///        }
///
///        int level = group.getPTFGroupLevel();
///        if (level < 0) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getPTFGroupLevel() returned " + level);
///        }
///
///        String groupName = group.getPTFGroupName();
///        if (groupName == null || groupName.length() == 0) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getPTFGroupName() returned " + groupName);
///        }
///
///        int groupStatus = group.getPTFGroupStatus();
///        if (groupStatus < 0 || groupStatus > 9) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getPTFGroupStatus() returned " + groupStatus);
///        }
///
///        AS400 sys = group.getSystem();
///        if (sys == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getSystem() returned " + sys);
///        }
///
///        PTFGroup[] relatedGroups = group.getRelatedPTFGroups();
///        if (relatedGroups == null) {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroup.getRelatedPTFGroups() returned " + relatedGroups);
///        }
///
///        if (DEBUG)
///        {
///          System.out.println("-----");
///          System.out.println("Group name: " + groupName);
///          System.out.println("RelatedIncl: " + relatedIncl);
///          System.out.println("Desc: " + desc);
///          System.out.println("Level: " + level);
///          System.out.println("Status: " + groupStatus);
///          System.out.println("System: " + sys);
///          System.out.println("Number of related PTF groups: " + relatedGroups.length);
///        }
///
///        PTF[] ptfs = group.getPTFs();
///        if (ptfs == null) {
///          //succeeded = false;
///          System.out.println("PTFGroup.getPTFs() returned null.");
///          return false;
///        }
///        if (ptfs.length == 0) {
///          succeeded = false;
///          System.out.println("PTFGroup.getPTFs() returned an empty list.");
///        }
///        PTF priorPTF = null;
///        for (int j=0; j<ptfs.length && j<10; j++)
///        {
///          PTF ptf = ptfs[j];
///          if (!validate(ptf, priorPTF, groupName, groupStatus)) succeeded = false;
///          priorPTF = ptf;
///        }
///      }
///      catch (AS400Exception e) { throw e; }
///      catch (Exception e) {
///        succeeded = false;
///        e.printStackTrace();
///      }
///
///      return succeeded;
///    }
///
///    private static boolean validate(PTFGroupList list) throws AS400Exception
///    {
///      boolean succeeded = true;
///
///      try
///      {
///        AS400 system = list.getSystem();
///        if (system == null)
///        {
///          succeeded = false;
///          System.out.println("ERROR: PTFGroupList.getSystem() returned null.");
///        }
///
///        PTFGroup[] groups = list.getPTFGroup();
///        if (groups == null || groups.length == 0) {
///          System.out.println("ERROR: No PTF groups were returned by getPTFGroup().");
///          return false;
///        }
///
///        for (int i=0; i<groups.length && i<10; i++)
///        {
///          PTFGroup group = groups[i];
///          if (!validate(group)) succeeded = false;
///        }
///      }
///      catch (AS400Exception e) { throw e; }
///      catch (Exception e) {
///        succeeded = false;
///        e.printStackTrace();
///      }
///
///      return succeeded;
///    }


    private final boolean validateAttributes(MemberDescription desc, AS400 system)
      throws Exception
    {
      boolean succeeded = true;

      String libraryName = (String)desc.getValue(MemberDescription.LIBRARY_NAME);
      String objectName = (String)desc.getValue(MemberDescription.FILE_NAME);
      String memberName = (String)desc.getValue(MemberDescription.MEMBER_NAME);

      MemberDescription desc1 = new MemberDescription(system, libraryName, objectName, memberName);

      String text = (String)desc1.getValue(MemberDescription.MEMBER_TEXT_DESCRIPTION);
      String memberName1 = (String)desc1.getValue(MemberDescription.MEMBER_NAME);
      String sourceType = (String)desc1.getValue(MemberDescription.SOURCE_TYPE);
      String sqlFileType = (String)desc1.getValue(MemberDescription.SQL_FILE_TYPE);
      String accPathMaint = (String)desc1.getValue(MemberDescription.ACCESS_PATH_MAINTENANCE);
      Boolean allowRead = (Boolean)desc1.getValue(MemberDescription.ALLOW_READ_OPERATION);
      Date created = (Date)desc1.getValue(MemberDescription.CREATION_DATE_TIME);
      Integer numRecs = (Integer)desc1.getValue(MemberDescription.CURRENT_NUMBER_OF_RECORDS);

      if (DEBUG)
      {
        output_.println("Member name: " + memberName);
        output_.println("  Text desc: " + text);
        output_.println("  Member name (1): " + memberName1);
        output_.println("  Source type: " + sourceType);
        output_.println("  SQL file type: " + sqlFileType);
        output_.println("  Access path maint: " + accPathMaint);
        output_.println("  Allow read operation: " + allowRead);
        output_.println("  Date created: " + (created == null ? null : dateFormatter_.format(created)));
        output_.println("  Current number of records " + numRecs.toString());
      }

      /// TBD - perform some validation.

      if (libraryName == null || libraryName.length() == 0)
      {
        output_.println("ERROR: getValue(LIBRARY_NAME) returned null or blank: " + libraryName);
        succeeded = false;
      }

      if (objectName == null || objectName.length() == 0)
      {
        output_.println("ERROR: getValue(FILE_NAME) returned null or blank: " + objectName);
        succeeded = false;
      }

      if (memberName == null || memberName.length() == 0)
      {
        output_.println("ERROR: getValue(MEMBER_NAME) returned null or blank: " + memberName);
        succeeded = false;
      }

      if (memberName1 == null || !memberName1.equals(memberName))
      {
        output_.println("ERROR: Second getValue(MEMBER_NAME) returned unexpected value: " + memberName1);
        succeeded = false;
      }

      long createdTime = (created == null ? 0L : created.getTime());
      long now = new Date().getTime();
      if (created == null || createdTime <= 0L || createdTime >= now)
      {
        output_.println("ERROR: getValue(CREATION_DATE_TIME) returned unexpected value: " + (created == null ? null : dateFormatter_.format(created)));
        succeeded = false;
      }

      if (numRecs == null || numRecs.intValue() < 0)
      {
        output_.println("ERROR: getValue(CURRENT_NUMBER_OF_RECORDS) returned null or negative: " + numRecs);
        succeeded = false;
      }

      return succeeded;
    }


    /**
     Construct a MemberList by specifying library/name, and validate its attributes.
     **/
    public void Var001()
    {
      boolean succeeded = true;
      try
      {
        MemberList mList = new MemberList(systemObject_, LIBRARY, FILENAME);
        ///mList.addAttribute(MemberDescription.ALL_MEMBER_INFORMATION);
        if (DEBUG) output_.println("\nLoading MemberList...");  ///
        mList.load();

        MemberDescription[] desc = mList.getMemberDescriptions();
        if (desc == null) {
          output_.println("ERROR: getMemberDescriptions() returned null.");
          succeeded = false;
        }
        else if (desc.length == 0) {
          output_.println("ERROR: getMemberDescriptions() returned no descriptions.");
        }
        else
        {
          for (int i=0; i<desc.length; i++) {
            if (!validateAttributes(desc[i], systemObject_)) {
              succeeded = false;
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
     Construct a MemberList by specifying QSYSObjectPathName, and validate its attributes.
     **/
    public void Var002()
    {
      boolean succeeded = true;
      try
      {
        QSYSObjectPathName qPath = new QSYSObjectPathName(LIBRARY, FILENAME, "FILE");
        MemberList mList = new MemberList(systemObject_, qPath);
        ///mList.addAttribute(MemberDescription.ALL_MEMBER_INFORMATION);
        if (DEBUG) output_.println("\nLoading MemberList...");  ///
        mList.load();

        MemberDescription[] desc = mList.getMemberDescriptions();
        if (desc == null) {
          output_.println("ERROR: getMemberDescriptions() returned null.");
          succeeded = false;
        }
        else if (desc.length == 0) {
          output_.println("ERROR: getMemberDescriptions() returned no descriptions.");
          succeeded = false;
        }
        else
        {
          for (int i=0; i<desc.length; i++) {
            if (!validateAttributes(desc[i], systemObject_)) {
              succeeded = false;
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
     Construct a MemberList by specifying AS400File, and validate its attributes.
     **/
    public void Var003()
    {
      boolean succeeded = true;
      try
      {
        QSYSObjectPathName qPath = new QSYSObjectPathName(LIBRARY, FILENAME, "FILE");
        SequentialFile seqFile = new SequentialFile(systemObject_, qPath.getPath());
        MemberList mList = new MemberList(seqFile);
        ///mList.addAttribute(MemberDescription.ALL_MEMBER_INFORMATION);
        if (DEBUG) output_.println("\nLoading MemberList...");  ///
        mList.load();

        MemberDescription[] desc = mList.getMemberDescriptions();
        if (desc == null) {
          output_.println("ERROR: getMemberDescriptions() returned null.");
          succeeded = false;
        }
        else if (desc.length == 0) {
          output_.println("ERROR: getMemberDescriptions() returned no descriptions.");
          succeeded = false;
        }
        else
        {
          for (int i=0; i<desc.length; i++) {
            if (!validateAttributes(desc[i], systemObject_)) {
              succeeded = false;
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
     Construct a MemberList by specifying a generic member name, and validate its attributes.
     Based on unit test program "MemberListTestFormat100" contributed by Mihael Schmidt.
     **/
    public void Var004()
    {
      boolean succeeded = true;
      try
      {
        MemberList list = new MemberList(systemObject_, new QSYSObjectPathName("/QSYS.LIB/QSYSINC.LIB/QRPGLESRC.FILE/Q*.MBR"));
        list.addAttribute(MemberDescription.MEMBER_NAME);
        list.load();

        MemberDescription[] desc = list.getMemberDescriptions();
        if (desc == null) {
          output_.println("ERROR: getMemberDescriptions() returned null.");
          succeeded = false;
        }
        else if (desc.length == 0) {
          output_.println("ERROR: getMemberDescriptions() returned no descriptions.");
          succeeded = false;
        }
        else
        {
          if (DEBUG) output_.println("Member count: " + desc.length);
          for (int i = 0; i < desc.length; i++)
          {
            String name = (String)desc[i].getValue(MemberDescription.MEMBER_NAME);
            if (DEBUG) output_.println(name);
            if (name == null || name.length() == 0)
            {
              output_.println("ERROR: getValue(MEMBER_NAME) returned null or blank: " + name);
              succeeded = false;
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
     Construct a MemberList by specifying a QSYSObjectPathName, and validate its attributes (format 200).
     Based on unit test program "MemberListTestFormat200Basic" contributed by Mihael Schmidt.
     **/
    public void Var005()
    {
      boolean succeeded = true;
      try
      {
        MemberList list = new MemberList(systemObject_, new QSYSObjectPathName("/QSYS.LIB/QSYSINC.LIB/QRPGLESRC.FILE"));
        list.addAttribute(MemberDescription.MEMBER_NAME);
        list.addAttribute(MemberDescription.SOURCE_TYPE);
        list.addAttribute(MemberDescription.MEMBER_TEXT_DESCRIPTION);
        list.load();

        MemberDescription[] desc = list.getMemberDescriptions();
        if (desc == null) {
          output_.println("ERROR: getMemberDescriptions() returned null.");
          succeeded = false;
        }
        else if (desc.length == 0) {
          output_.println("ERROR: getMemberDescriptions() returned no descriptions.");
          succeeded = false;
        }
        else
        {
          if (DEBUG) output_.println("Member count: " + desc.length);
          for (int i = 0; i < desc.length; i++)
          {
            String name = (String)desc[i].getValue(MemberDescription.MEMBER_NAME);
            String type = (String)desc[i].getValue(MemberDescription.SOURCE_TYPE);
            String text = (String)desc[i].getValue(MemberDescription.MEMBER_TEXT_DESCRIPTION);
            if (DEBUG)
            {
              output_.println(
                              name + "." +
                              type + " - " +
                              text);
            }
            if (name == null || name.length() == 0)
            {
              output_.println("ERROR: getValue(MEMBER_NAME) returned null or blank string: " + name);
              succeeded = false;
            }
            if (type == null)
            {
              output_.println("ERROR: getValue(SOURCE_TYPE) returned null");
              succeeded = false;
            }
            if (text == null)
            {
              output_.println("ERROR: getValue(MEMBER_TEXT_DESCRIPTION) returned null");
              succeeded = false;
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
     Construct a MemberDescription, and validate its attributes (format 100).
     Based on unit test program "MemberDescriptionTestFormat100" contributed by Mihael Schmidt.
     **/
    public void Var006()
    {
      boolean succeeded = true;
      try
      {
        MemberDescription member = new MemberDescription(systemObject_, new QSYSObjectPathName("/QSYS.LIB/QSYSINC.LIB/QRPGLESRC.FILE/QUSEC.MBR"));
        String lib  = (String)member.getValue(MemberDescription.LIBRARY_NAME);
        String name = (String)member.getValue(MemberDescription.FILE_NAME);
        String mem  = (String)member.getValue(MemberDescription.MEMBER_NAME);
        String type = (String)member.getValue(MemberDescription.SOURCE_TYPE);
        Date changed = (Date)member.getValue(MemberDescription.LAST_SOURCE_CHANGE_DATE);
        if (DEBUG)
        {
          output_.println(
                          lib + "/" +
                          name + "," +
                          mem + "." +
                          type + " - " +
                          changed == null ? null : dateFormatter_.format(changed));
        }
        if (mem == null || mem.length() == 0)
        {
          output_.println("ERROR: getValue(MEMBER_NAME) returned null or blank string: " + mem);
          succeeded = false;
        }

        assertCondition(succeeded);
      }
      catch (Exception e) {
        failed(e, "Unexpected Exception");
      }
    }

}
