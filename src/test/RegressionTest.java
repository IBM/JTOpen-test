///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RegressionTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.UserSpace;

import test.Cmd.CmdConstructor;
import test.Cmd.CmdOnThreadTestcase;
import test.Cmd.CmdRunTestcase;
import test.Conv.ConvConverterTestcase;
import test.Conv.ConvExecutionTestcase;
import test.Conv.ConvPSA89229;
import test.Conv.ConvTableCtorTestcase;
import test.Conv.ConvTableReaderTestcase;
import test.Conv.ConvTableWriterTestcase;
import test.Conv.ConversionMapTestcase;
import test.DA.DAClearTestcase;
import test.DA.DACreateTestcase;
import test.DA.DADeleteTestcase;
import test.DA.DAReadTestcase;
import test.DA.DAWriteTestcase;
import test.DDM.DDMCaching;
import test.DDM.DDMCommitmentControl;
import test.DDM.DDMConstructors;
import test.DDM.DDMCreateAndAdd;
import test.DDM.DDMDelete;
import test.DDM.DDMDeletedRecords;
import test.DDM.DDMGetSet;
import test.DDM.DDMMultipleFormat;
import test.DDM.DDMOpenClose;
import test.DDM.DDMP3666842;
import test.DDM.DDMP3696575;
import test.DDM.DDMP9907036;
import test.DDM.DDMReadKey;
import test.DDM.DDMReadRN;
import test.DDM.DDMReadSeq;
import test.DDM.DDMRegressionTestcase;
import test.DDM.DDMUpdate;
import test.DDM.DDMWrite;
import test.DT.DTArrayTestcase;
import test.DT.DTBin2Testcase;
import test.DT.DTBin4Testcase;
import test.DT.DTBin8Testcase;
import test.DT.DTByteArrayTestcase;
import test.DT.DTFloat4Testcase;
import test.DT.DTFloat8Testcase;
import test.DT.DTPackedDoubleTestcase;
import test.DT.DTPackedTestcase;
import test.DT.DTStructureTestcase;
import test.DT.DTTextTestcase;
import test.DT.DTUnsignedBin2Testcase;
import test.DT.DTUnsignedBin4Testcase;
import test.DT.DTZonedDoubleTestcase;
import test.DT.DTZonedTestcase;
import test.IFS.IFSCrtDltTestcase;
import test.IFS.IFSCtorTestcase;
import test.IFS.IFSFileAttrTestcase;
import test.IFS.IFSFileDescriptorTestcase;
import test.IFS.IFSMiscTestcase;
import test.IFS.IFSPropertyTestcase;
import test.IFS.IFSReadTestcase;
import test.IFS.IFSWriteTestcase;
import test.Message.MessageFileTestcase;
import test.MiscAH.FDConstructAndGet;
import test.MiscAH.FDInvUsage;
import test.MiscAH.FDSet;
import test.PN.PNAttributesTestcase;
import test.PN.PNCtorTestcase;
import test.PN.PNLocalizedObjectTypeTestcase;
import test.PN.PNToPathTestcase;
import test.Pgm.PgmConstructor;
import test.Pgm.PgmOnThreadTestcase;
import test.Pgm.PgmParmTestcase;
import test.Pgm.PgmRLETestcase;
import test.Pgm.PgmRunTestcase;
import test.RF.RFEvents;
import test.RF.RFMisc;
import test.RF.RFNewRecord;
import test.RF.RFRecord;
import test.RF.RFRecordMisc;
import test.ServiceProgram.ServiceProgramCallTestcase;
import test.ServiceProgram.ServiceProgramCallUnattendedTestcase;
import test.UserSpace.UserSpaceBeans;
import test.UserSpace.UserSpaceChgAttrTestcase;
import test.UserSpace.UserSpaceCrtDltTestcase;
import test.UserSpace.UserSpacePgmCallTestcase;
import test.UserSpace.UserSpaceReadTestcase;
import test.UserSpace.UserSpaceWriteTestcase;
import test.misc.TraceMiscTestcase;



public class RegressionTest extends TestDriver
{
    static boolean usingSockets = false;
    static boolean needToCleanupJarMakerTestFiles_ = false;




/**
Main for running standalone application tests.
**/
  public static void main(String args[])
  {
	int failureCount = 0; 
    TestDriverStatic.brief_ = true;  // Skip long-running variations.
    try
    {
       System.out.println("      ");
       System.out.println("Reminder: -tc can be:");
       System.out.println("      ");
       System.out.println("   1) Omitted (run entire bucket)");
       System.out.println("   2) An existing test case.  For example, -tc CmdRunTestcase");
       System.out.println("   3) A test group.  For example, -tc JDBC.  Valid groups are:");
       System.out.println("      ProgramCall");
       System.out.println("      CommandCall");
       System.out.println("      UserSpace");
       // System.out.println("      FTP");
       System.out.println("      Message");
       System.out.println("      Trace");
       System.out.println("      QSYSObjectPathName");
       System.out.println("      FD");
       System.out.println("      DT");
       System.out.println("      RF");
       System.out.println("      Conv");
       System.out.println("      DDM");
       System.out.println("      IFS");
       System.out.println("      JarMaker");
       System.out.println("      DataArea");
       System.out.println("      JDBC");
       System.out.println();
       System.out.println();
       System.out.println("To get the JavaProgramCall test cases to run cleanly:");
       System.out.println("   1. The AS/400 must have a JVM");
       System.out.println("   2. Directory /home/JavaTest on the AS/400 must have test programs.");
       System.out.println("The test programs are in file JavaPgmCallTestPrograms.zip in the ");
       System.out.println("test directory.  Copy these programs to the AS/400 before running the ");
       System.out.println("test cases.");

       System.out.println();




       RegressionTest me = new RegressionTest(args);
       me.init();
       me.start();
       me.stop();
       failureCount = me.totalFail_; 
       me.destroy();
       me.cleanup(true);
    }
    catch (Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }

    // Needed to make the virtual machine quit.
       System.exit(failureCount);
  }





/**
This ctor used for applets.
@exception Exception Initialization errors may cause an exception.
**/
  public RegressionTest()
       throws Exception
  {
    super();
  }




/**
This ctor used for applications.
@param args the array of command line arguments
@exception Exception Incorrect arguments will cause an exception
**/
  public RegressionTest(String[] args)
       throws Exception
  {
    super(args);
  }





/**
Cleanup some of the AS400 objects created during the test.
**/
  public void cleanup(boolean dspmsg)
  {
  }





/**
Creates Testcase objects for all the testcases in this component.
**/
  public void createTestcases()
  {
    // Instantiate all testcases to be run.
    boolean testAllComponents = (namesAndVars_.size() == 0);

    // do some setup
 // if (pwrsys_ == null)
 // {
 //    StringTokenizer miscTok = new StringTokenizer(pwrsys_, ",");
 //    String uid = miscTok.nextToken();
 //    String pwd = miscTok.nextToken();
 //
 //    // Create power user for object creation and deletion.
 //    pwrsys_ = new AS400( systemObject_.getSystemName(), uid, pwd );
 //
 //    try
 //    {
 //       pwrsys_.setGuiAvailable(false);
 //    }
 //    catch (Exception e) {}
 // }

    cleanup(false);


    Testcase[] testcases = null;

    if (testAllComponents || namesAndVars_.containsKey("ProgramCall"))
    {
       Vector vars;
       namesAndVars_.put("PgmConstructor", new Vector());
       namesAndVars_.put("PgmParmTestcase", new Vector());
       namesAndVars_.put("PgmRunTestcase", new Vector());
       namesAndVars_.put("PgmOnThreadTestcase", new Vector());
       namesAndVars_.put("PgmRLETestcase", new Vector());
       namesAndVars_.put("ServiceProgramCallTestcase", new Vector());
       namesAndVars_.put("ServiceProgramCallUnattendedTestcase", new Vector());

       vars = makeVector( "1,2,4,7,113" );
       namesAndVars_.put("JavaAppTestcase", vars);

       namesAndVars_.remove("ProgramCall");

       testcases = new Testcase[]
       {
         new PgmConstructor(),
         new PgmParmTestcase(),
         new PgmRunTestcase(),
         new PgmOnThreadTestcase(),
         new PgmRLETestcase(),
         new ServiceProgramCallTestcase(),
         new ServiceProgramCallUnattendedTestcase(),
         new JavaAppTestcase()
       };
       for (int i = 0; i < testcases.length; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_,  pwrSysUserID_, pwrSysPassword_);
         addTestcase(testcases[i]);
       }
    }

    if (testAllComponents || namesAndVars_.containsKey("CommandCall"))
    {
       namesAndVars_.put("CmdConstructor", new Vector());
       namesAndVars_.put("CmdRunTestcase", new Vector());
       namesAndVars_.put("CmdOnThreadTestcase", new Vector());
       namesAndVars_.remove("CommandCall");

       testcases = new Testcase[]
       {
         new CmdConstructor(),
         new CmdRunTestcase(),
         new CmdOnThreadTestcase(),
       };
       for (int i = 0; i < testcases.length; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
         addTestcase(testcases[i]);
       }
    }


    if (testAllComponents || namesAndVars_.containsKey("UserSpace"))
    {
       setupUSExisting();  // create a test user space

       Vector vars;

       //namesAndVars_.put("UserSpaceRegressionTestcase", new Vector());

       vars = makeVector( "1:18" );
       namesAndVars_.put("UserSpaceBeans", vars);

       vars = makeVector( "1,3,14,24" );
       namesAndVars_.put("UserSpaceChgAttrTestcase", vars);

       vars = makeVector( "5,37,57" );
       namesAndVars_.put("UserSpaceCrtDltTestcase", vars);

       vars = makeVector( "10,19,26,31,32,34,68,77,78,125" );
       namesAndVars_.put("UserSpacePgmCallTestcase", vars);

       vars = makeVector( "9,28,33" );
       namesAndVars_.put("UserSpaceReadTestcase", vars);

       vars = makeVector( "34,58" );
       namesAndVars_.put("UserSpaceWriteTestcase", vars);

       namesAndVars_.remove("UserSpace");

       testcases = new Testcase[]
       {
         //new UserSpaceRegressionTestcase(),
         new UserSpaceBeans(),
         new UserSpaceChgAttrTestcase(),
         new UserSpaceCrtDltTestcase(),
         new UserSpacePgmCallTestcase(),
         new UserSpaceReadTestcase(),
         new UserSpaceWriteTestcase()
       };
       for (int i = 0; i < testcases.length; ++i)
         ///for (int i = 4; i < 5; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
         addTestcase(testcases[i]);
       }
       //((UserSpacePgmCallTestcase)testcases[0]).setRegressionTest(true);
       //((UserSpaceRegressionTestcase)testcases[1]).setRegressionTest(true);
    }




    // See FTPTest for requirement for running the FTP testcases.  Files
    // must exist on both client and server before running the test.
    //if (testAllComponents || namesAndVars_.containsKey("FTP"))
    //{
    //   namesAndVars_.put("FTPQuickVerification", new Vector());
    //   namesAndVars_.remove("FTP");
    //
    //    addTestcase(new FTPQuickVerification(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_, systemObject_.getUserId(), "/", pwrSys_));
    // }




    if (testAllComponents || namesAndVars_.containsKey("Message"))
    {
       namesAndVars_.put("MessageFileTestcase", new Vector());
       namesAndVars_.remove("Message");

       MessageFileTestcase mftc = new MessageFileTestcase();
       mftc.setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
       addTestcase(mftc);
    }




    if (testAllComponents || namesAndVars_.containsKey("Trace"))
    {
       namesAndVars_.put("TraceMiscTestcase", new Vector());
       namesAndVars_.remove("Trace");

       addTestcase(new TraceMiscTestcase(systemObject_, (Vector) namesAndVars_.get("TraceMiscTestcase"), runMode_, fileOutputStream_));
    }




    if (testAllComponents || namesAndVars_.containsKey("QSYSObjectPathName"))
    {
       namesAndVars_.put("PNAttributesTestcase", new Vector());
       namesAndVars_.put("PNCtorTestcase", new Vector());
       namesAndVars_.put("PNToPathTestcase", new Vector());
       namesAndVars_.put("PNLocalizedObjectTypeTestcase", new Vector());
       namesAndVars_.remove("QSYSObjectPathName");

       testcases = new Testcase[]
       {
         new PNAttributesTestcase(),
         new PNCtorTestcase(),
         new PNToPathTestcase(),
         new PNLocalizedObjectTypeTestcase()
       };

       for (int i = 0; i < testcases.length; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
         addTestcase(testcases[i]);
       }
    }


    if (testAllComponents || namesAndVars_.containsKey("FD"))
    {
       namesAndVars_.put("FDConstructAndGet", new Vector());
       namesAndVars_.put("FDInvUsage", new Vector());
       namesAndVars_.put("FDSet", new Vector());
       namesAndVars_.remove("FD");

       addTestcase(new FDConstructAndGet(systemObject_, (Vector) namesAndVars_.get("FDConstructAndGet"), runMode_, fileOutputStream_));
       addTestcase(new FDInvUsage(systemObject_, (Vector) namesAndVars_.get("FDInvUsage"), runMode_, fileOutputStream_));
       addTestcase(new FDSet(systemObject_, (Vector) namesAndVars_.get("FDSet"), runMode_, fileOutputStream_));
    }



    if (testAllComponents || namesAndVars_.containsKey("DT"))
    {
       namesAndVars_.put("DTArrayTestcase", new Vector());
       namesAndVars_.put("DTBin2Testcase", new Vector());
       namesAndVars_.put("DTBin4Testcase", new Vector());
       namesAndVars_.put("DTBin8Testcase", new Vector());
       namesAndVars_.put("DTByteArrayTestcase", new Vector());
       namesAndVars_.put("DTFloat4Testcase", new Vector());
       namesAndVars_.put("DTFloat8Testcase", new Vector());
       namesAndVars_.put("DTPackedTestcase", new Vector());
       namesAndVars_.put("DTPackedDoubleTestcase", new Vector());
       namesAndVars_.put("DTStructureTestcase", new Vector());
       namesAndVars_.put("DTTextTestcase", new Vector());
       namesAndVars_.put("DTUnsignedBin2Testcase", new Vector());
       namesAndVars_.put("DTUnsignedBin4Testcase", new Vector());
       namesAndVars_.put("DTZonedTestcase", new Vector());
       namesAndVars_.put("DTZonedDoubleTestcase", new Vector());
       namesAndVars_.remove("DT");

       testcases = new Testcase[]
       {
         new DTArrayTestcase(),
         new DTBin2Testcase(),
         new DTBin4Testcase(),
         new DTBin8Testcase(),
         new DTByteArrayTestcase(),
         new DTFloat4Testcase(),
         new DTFloat8Testcase(),
         new DTPackedTestcase(),
         new DTPackedDoubleTestcase(),
         new DTStructureTestcase(),
         new DTTextTestcase(),
         new DTUnsignedBin2Testcase(),
         new DTUnsignedBin4Testcase(),
         new DTZonedTestcase(),
         new DTZonedDoubleTestcase()
       };

       for (int i = 0; i < testcases.length; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
         addTestcase(testcases[i]);
       }
    }


    if (testAllComponents || namesAndVars_.containsKey("RF"))
    {
       namesAndVars_.put("RFMisc", new Vector());
       namesAndVars_.put("RFNewRecord", new Vector());
       namesAndVars_.put("RFRecord", new Vector());
       namesAndVars_.put("RFRecordMisc", new Vector());
       namesAndVars_.put("RFEvents", new Vector());
       namesAndVars_.remove("RF");

       addTestcase(new RFMisc(systemObject_, (Vector) namesAndVars_.get("RFMisc"), runMode_, fileOutputStream_));
       addTestcase(new RFNewRecord(systemObject_, (Vector) namesAndVars_.get("RFNewRecord"), runMode_, fileOutputStream_));
       addTestcase(new RFRecord(systemObject_, (Vector) namesAndVars_.get("RFRecord"), runMode_, fileOutputStream_));
       addTestcase(new RFRecordMisc(systemObject_, (Vector) namesAndVars_.get("RFRecordMisc"), runMode_, fileOutputStream_));
       addTestcase(new RFEvents(systemObject_, (Vector) namesAndVars_.get("RFEvents"), runMode_, fileOutputStream_));
    }



    if (testAllComponents || namesAndVars_.containsKey("Conv"))
    {
       namesAndVars_.put("ConvTableCtorTestcase", new Vector());
       namesAndVars_.put("ConvExecutionTestcase", new Vector());
       namesAndVars_.put("ConvConverterTestcase", new Vector());
       namesAndVars_.put("ConversionMapTestcase", new Vector());
       namesAndVars_.put("ConvPSA89229", new Vector());
       namesAndVars_.put("ConvTableReaderTestcase", new Vector());
       namesAndVars_.put("ConvTableWriterTestcase", new Vector());
       namesAndVars_.remove("Conv");

       testcases = new Testcase[]
       {
         new ConvTableCtorTestcase(),
         new ConvExecutionTestcase(),
         new ConvConverterTestcase(),
         new ConversionMapTestcase(),
         new ConvPSA89229(),
         new ConvTableReaderTestcase(),
         new ConvTableWriterTestcase()
       };

       for (int i = 0; i < testcases.length; ++i)
       {
         testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
         addTestcase(testcases[i]);
       }
    }


    if (testAllComponents || namesAndVars_.containsKey("DDM"))
    {
       Vector vars;

       namesAndVars_.put("DDMConstructors", new Vector());

       vars = makeVector( "1:3,16:20,22:24" );
       namesAndVars_.put("DDMRegressionTestcase", vars);

       namesAndVars_.put("DDMOpenClose", new Vector());
       namesAndVars_.put("DDMCreateAndAdd", new Vector());
       namesAndVars_.put("DDMDelete", new Vector());
       namesAndVars_.put("DDMCommitmentControl", new Vector());
       namesAndVars_.put("DDMReadSeq", new Vector());
       namesAndVars_.put("DDMReadKey", new Vector());
       namesAndVars_.put("DDMReadRN", new Vector());
       namesAndVars_.put("DDMDeletedRecords", new Vector());
       namesAndVars_.put("DDMWrite", new Vector());
       namesAndVars_.put("DDMUpdate", new Vector());
       namesAndVars_.put("DDMGetSet", new Vector());
       namesAndVars_.put("DDMCaching", new Vector());
       namesAndVars_.put("DDMMultipleFormat", new Vector());
       namesAndVars_.put("DDMP3666842", new Vector());
       namesAndVars_.put("DDMP3696575", new Vector());
       namesAndVars_.put("DDMP9907036", new Vector());
       // DDMLocking, DDMRecordDescription, and DDMPassword variations are included in DDMRegressionTestcase.
       namesAndVars_.remove("DDM");

       String testLib = "DDMRTEST";

       addTestcase(new DDMConstructors(systemObject_, (Vector) namesAndVars_.get("DDMConstructors"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMRegressionTestcase(systemObject_, (Vector) namesAndVars_.get("DDMRegressionTestcase"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMOpenClose(systemObject_, (Vector) namesAndVars_.get("DDMOpenClose"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMCreateAndAdd(systemObject_, (Vector) namesAndVars_.get("DDMCreateAndAdd"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMDelete(systemObject_, (Vector) namesAndVars_.get("DDMDelete"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMCommitmentControl(systemObject_, (Vector) namesAndVars_.get("DDMCommitmentControl"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMReadSeq(systemObject_, (Vector) namesAndVars_.get("DDMReadSeq"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMReadKey(systemObject_, (Vector) namesAndVars_.get("DDMReadKey"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMReadRN(systemObject_, (Vector) namesAndVars_.get("DDMReadRN"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMDeletedRecords(systemObject_, (Vector) namesAndVars_.get("DDMDeletedRecords"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMWrite(systemObject_, (Vector) namesAndVars_.get("DDMWrite"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMUpdate(systemObject_, (Vector) namesAndVars_.get("DDMUpdate"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMGetSet(systemObject_, (Vector) namesAndVars_.get("DDMGetSet"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMCaching(systemObject_, (Vector) namesAndVars_.get("DDMCaching"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMMultipleFormat(systemObject_, (Vector) namesAndVars_.get("DDMMultipleFormat"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMP3666842(systemObject_, (Vector) namesAndVars_.get("DDMP3666842"), runMode_, fileOutputStream_, testLib));
       addTestcase(new DDMP3696575(systemObject_, (Vector) namesAndVars_.get("DDMP3696575"), runMode_, fileOutputStream_, testLib, pwrSys_));
       addTestcase(new DDMP9907036(systemObject_, (Vector) namesAndVars_.get("DDMP9907036"), runMode_, fileOutputStream_, testLib));
    }



      if (testAllComponents || namesAndVars_.containsKey("IFS"))
      {
        Vector vars;

        vars = makeVector( "3,8,10,12" );
        namesAndVars_.put("IFSCrtDltTestcase", vars);

        vars = makeVector( "6,12,16,22,30,44,51,54,62,72,79,93,143,151,163,172,175,184,193,200" );
        namesAndVars_.put("IFSCtorTestcase", vars);

        vars = makeVector( "1:6" );
        namesAndVars_.put("IFSFileAttrTestcase", vars);

        vars = makeVector( "1:7" );
        namesAndVars_.put("IFSFileDescriptorTestcase", vars);

        vars = makeVector( "2,4,6,8,10,12,15,16,18,19,20,21,23,28,31,32,34,35" );
        namesAndVars_.put("IFSMiscTestcase", vars);

        vars = makeVector( "13,14,16,17,18,19,49,50,69,70,89,90,203,207,211,213,219" );
        namesAndVars_.put("IFSPropertyTestcase", vars);

        vars = makeVector( "3,9,18,21,27,36,39,42,45,48,51,56,64,67,70,73,76,79,82,88,94" );
        namesAndVars_.put("IFSReadTestcase", vars);

        vars = makeVector( "1,4,9,10,13,18,19,20,22,23,25,26,27,28,29,30,33,34,36,37" );
        namesAndVars_.put("IFSWriteTestcase", vars);


        namesAndVars_.remove("IFS");

        try
        {
          addTestcase (new IFSCrtDltTestcase (systemObject_, userId_, password_,
                                              namesAndVars_, runMode_,
                                              fileOutputStream_,  pwrSys_));
          addTestcase (new IFSCtorTestcase (systemObject_, userId_, password_,
                                            namesAndVars_, runMode_,
                                            fileOutputStream_,  pwrSys_));
          addTestcase (new IFSFileAttrTestcase (systemObject_, userId_, password_,
                                                namesAndVars_, runMode_,
                                                fileOutputStream_,  pwrSys_));
          addTestcase (new IFSFileDescriptorTestcase (systemObject_, userId_, password_,
                                                      namesAndVars_, runMode_,
                                                      fileOutputStream_,  pwrSys_));
          addTestcase (new IFSMiscTestcase (systemObject_, userId_, password_,
                                            namesAndVars_, runMode_,
                                            fileOutputStream_,  pwrSys_));
          addTestcase (new IFSPropertyTestcase (systemObject_, userId_, password_,
                                                namesAndVars_, runMode_,
                                                fileOutputStream_,  pwrSys_));
          addTestcase (new IFSReadTestcase (systemObject_, userId_, password_,
                                            namesAndVars_, runMode_,
                                            fileOutputStream_,  pwrSys_));
          addTestcase (new IFSWriteTestcase (systemObject_, userId_, password_,
                                             namesAndVars_, runMode_,
                                             fileOutputStream_,  pwrSys_));

        }
        catch(Exception e)
        {
          e.printStackTrace();
        }
      }




   



      if (testAllComponents || namesAndVars_.containsKey("DataArea"))
      {
        Vector vars;

        vars = makeVector( "38,39,40,41,53,54" );
        namesAndVars_.put("DACreateTestcase", vars);

        vars = makeVector( "5,6,14,15,23,24" );
        namesAndVars_.put("DADeleteTestcase", vars);

        vars = makeVector( "14,15,26,27,34,35,45,46" );
        namesAndVars_.put("DAReadTestcase", vars);

        vars = makeVector( "18,19,32,33,40,41,55,56" );
        namesAndVars_.put("DAWriteTestcase", vars);

        vars = makeVector( "5,6,13,14,21,22,25,26" );
        namesAndVars_.put("DAClearTestcase", vars);


        namesAndVars_.remove("DataArea");

        testcases = new Testcase[]
        {
          new DACreateTestcase(),
          new DADeleteTestcase(),
          new DAReadTestcase(),
          new DAWriteTestcase(),
          new DAClearTestcase()
        };

        for (int i = 0; i < testcases.length; ++i)
        {
          testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_);
          addTestcase(testcases[i]);
        }
      }


    if (testAllComponents || namesAndVars_.containsKey("JDBC"))
    {
       namesAndVars_.put("JDRegressionTestcase", new Vector());
       namesAndVars_.remove("JDBC");
       addTestcase(new JDRegressionTestcase(systemObject_, namesAndVars_, runMode_, fileOutputStream_, password_, systemObject_.getUserId(), pwrSysUserID_, pwrSysPassword_));
    }




    // Put out error message for each invalid testcase name.
    for (Enumeration e = namesAndVars_.keys(); e.hasMoreElements();)
    {
      System.out.println("Testcase " + e.nextElement() + " not found.");
    }
  }





/**
Convert a String of the form "1,2,3" to a Vector { "1", "2", "3" }.
Also accepts range patterns such as "1:3,5", which it expands to { "1", "2", "3", "5" }.
**/
  static Vector makeVector(String list)
  {
    Vector variations = new Vector();
    StringTokenizer vars = new StringTokenizer(list, ",");
    while (vars.hasMoreTokens())
    {
      String token = vars.nextToken();
      StringTokenizer range = new StringTokenizer(token, ":");
      if (range.countTokens() == 2)
      {
        int start = new Integer(range.nextToken()).intValue();
        int end = new Integer(range.nextToken()).intValue();
        if (end < start) end = start;
        for (int num=start; num<=end; num++)
          variations.addElement(new Integer(num).toString());
      }
      else
        variations.addElement(token);
    }
    return variations;
  }


  private static final String pre_existingUserSpace_ = "/QSYS.LIB/USTEST.LIB/PREEXIST.USRSPC";
  private static final byte pre_existingByteValue_ = (byte)0x00;
  private static final int pre_existingLengthValue_ = 11000;

  /*
  Method: setupUSExisting()
  Description: Create a user space on the server to be used by testcases needing an existing user space.
  */
  private void setupUSExisting()
    ///throws Exception
  {
    try
    {
       CommandCall cmd = new CommandCall(systemObject_);
       if (cmd.run("CRTLIB LIB(USTEST)") != true)
       {
           AS400Message[] messageList = cmd.getMessageList();
           System.out.println(messageList[0].toString());
       }

       // Create a user space to use test on an pre-existing user space.
       UserSpace aUserSpace = new UserSpace(systemObject_, pre_existingUserSpace_);
       aUserSpace.setMustUseProgramCall(false);
       aUserSpace.create(pre_existingLengthValue_, true, " ", pre_existingByteValue_, "CRTDLT UserSpace", "*ALL");
       aUserSpace.close();
    }
    catch(Exception e)
    {
       System.out.println("Setup failed, could not create ustest.lib/preexist.usrspc");
       System.out.println(e);
       ///throw e;
    }
  }

}

