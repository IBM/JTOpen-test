///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PCMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PC;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.ProgramCall;
import com.ibm.as400.access.Trace;
import com.ibm.as400.data.ProgramCallDocument;

import test.JTOpenTestEnvironment;
import test.Testcase;

/**
 Testcase PCMiscTestcase.
 **/
public class PCMiscTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PCMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PCTest.main(newArgs); 
   }

    static boolean DEBUG = false;

    static TimeZone timezoneLocal_ = TimeZone.getDefault();
    static TimeZone timezoneGMT_ = TimeZone.getTimeZone("GMT-0");
    static SimpleDateFormat dateFormatterGMT_ = new SimpleDateFormat("yyyy-MM-dd");
    static {
      dateFormatterGMT_.setTimeZone(timezoneGMT_);
    }
    static SimpleDateFormat timeFormatterGMT_ = new SimpleDateFormat("HH:mm:ss");
    static boolean jdk15 = true; 
    static {
      timeFormatterGMT_.setTimeZone(timezoneGMT_);

    }


  // Uses the system classloader to copy a file (specified by resource name, e.g. "test/jungle.txt") to a destination file.  If the resource resides in a jar or zip file, then it is extracted to the destination.
  static boolean extractFile(String resourceName, File destination) throws FileNotFoundException, IOException
  {
    boolean succeeded = false;
    InputStream is = ClassLoader.getSystemResourceAsStream(resourceName);
    if (is == null) {
      System.out.println("Resource not found: |" + resourceName + "|");
    }
    else
    {
      BufferedOutputStream bos = null;
      try
      {
        File parent = destination.getParentFile();
        if (parent != null) parent.mkdirs();  // create parent directory(s) if needed
        bos = new BufferedOutputStream(new FileOutputStream(destination));
        byte[] buffer = new byte[1024];
        while (true)
        {
          int bytesRead = is.read(buffer);
          if (bytesRead == -1) break;
          bos.write(buffer, 0, bytesRead);
        }
        succeeded = true;
      }
      finally {
        if (is != null) try { is.close(); } catch (Throwable t) {}
        if (bos != null) try { bos.close(); } catch (Throwable t) {}
      }
    }
    return succeeded;
  }

    /**
     Test ProgramCallDocument.getProgramCall()
     **/
    public void Var001()
    {

        if (systemObject_.getProxyServer().length() > 0)
        {
            notApplicable("getServerJob() with proxy not supported");
            return;                                                  
        }

        try
        {
            ProgramCallDocument pcmlDoc = new ProgramCallDocument(systemObject_, "com.ibm.as400.resource.RUser");

            // Set input parameter value.
            String userName = systemObject_.getUserId();
            pcmlDoc.setValue("qsyrusri_usri0100.userProfileName", userName);

            ProgramCall pgm = pcmlDoc.getProgramCall();
            if (pgm != null)
            {
                failed("getProgramCall() returned initial non-null.");
                return;
            }

            boolean ok = false;
            String returnedName = null;

            pcmlDoc.setThreadsafeOverride("qsyrusri_usri0100", false);
            ok = pcmlDoc.callProgram("qsyrusri_usri0100");
            pgm = pcmlDoc.getProgramCall();
            if (ok)
            {
                returnedName = (String)pcmlDoc.getValue("qsyrusri_usri0100.receiverVariable.userProfileName");
                if (!userName.equalsIgnoreCase(returnedName))
                {
                    failed("First programCall returned wrong username: " + returnedName);
                    return;
                }
            }
            else
            {
                failed("First programCall failed.");
                if (pgm != null) {
                    AS400Message[] messagelist = pgm.getMessageList();
                    for (int i = 0; i < messagelist.length; ++i)
                    {
                        System.out.println(messagelist[i]);
                    }
                }
                return;
            }
            Job job1 = pgm.getServerJob();
            if (job1 == null)
            {
                failed("Null Job returned from first getServerJob()");
                return;
            }
            String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
            if (DEBUG)
                output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");

            pcmlDoc.setThreadsafeOverride("qsyrusri_usri0100", true);
            ok = pcmlDoc.callProgram("qsyrusri_usri0100");
            pgm = pcmlDoc.getProgramCall();
            if (ok)
            {
                returnedName = (String)pcmlDoc.getValue("qsyrusri_usri0100.receiverVariable.userProfileName");
                if (!userName.equalsIgnoreCase(returnedName))
                {
                    failed("Second programCall returned wrong username: " + returnedName);
                    return;
                }
            }
            else
            {
                failed("Second programCall failed.");
                if (pgm != null)
                {
                    AS400Message[] messagelist = pgm.getMessageList();
                    for (int i = 0; i < messagelist.length; ++i)
                    {
                        System.out.println(messagelist[i]);
                    }
                }
                return;
            }
            Job job2 = pgm.getServerJob();
            if (job2 == null)
            {
                failed("Null Job returned from second getServerJob()");
                return;
            }
            String job2ID = job2.getName() + "/" + job2.getUser() + "/" + job2.getNumber();
            if (DEBUG)
                output_.println("DEBUG job2 name/user/number: |" + job2ID + "|");

            pgm = null;
            pcmlDoc.setThreadsafeOverride("qsyrusri_usri0100", false);
            ok = pcmlDoc.callProgram("qsyrusri_usri0100");
            pgm = pcmlDoc.getProgramCall();
            if (ok)
            {
                returnedName = (String)pcmlDoc.getValue("qsyrusri_usri0100.receiverVariable.userProfileName");
                if (!userName.equalsIgnoreCase(returnedName))
                {
                    failed("Third programCall returned wrong username: " + returnedName);
                    return;
                }
            }
            else
            {
                failed("Third programCall failed.");
                if (pgm != null)
                {
                    AS400Message[] messagelist = pgm.getMessageList();
                    for (int i = 0; i < messagelist.length; ++i)
                    {
                        System.out.println(messagelist[i]);
                    }
                }
                return;
            }
            Job job3 = pgm.getServerJob();
            if (job3 == null)
            {
                failed("Null Job returned from third getServerJob()");
                return;
            }
            String job3ID = job3.getName() + "/" + job3.getUser() + "/" + job3.getNumber();
            if (DEBUG)
                output_.println("DEBUG job3 name/user/number: |" + job3ID + "|");

            // Note: Threadsafety features became available in V5R1.
            if (JTOpenTestEnvironment.isOS400 && isNative_ && systemObject_.getVersion() >= 5)
            {
                if (DEBUG) output_.println("DEBUG Running on an AS/400 (V5 or later)");
                assertCondition (!job1ID.equals(job2ID) &&
                                 job1ID.equals(job3ID),
				 "job1ID="+job1ID+" should not be job2Id="+job2ID+"\n"+
                                 "job1ID="+job1ID+" should be job3ID"+job3ID);
            }
            else
            {
                assertCondition (job1ID.equals(job2ID) &&
                                 job1ID.equals(job3ID));
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        {
            // JarMaker may have removed Job class.
            if (e.getMessage().indexOf("Job") != -1 ||
                e.getMessage().indexOf("IntegerHashtable") != -1)
            {
                failed("Job-related class not found: " + e.getMessage());
            }
            else failed(e, "Unexpected exception");
        }
    }

    /**
     Test ProgramCallDocument.callProgram(), where the source document is XPCML.
     **/
    public void Var002()
    {
      if (!jdk15) {
        notApplicable("Requires JDK 1.5 or later"); 
        return; 
      }
      File xpcmlSchemaFile = null;
      try
      {
        // Extract the XPCML XSD specification file (from the Toolbox jar) to the local directory, so that the XML parser can find it.
        xpcmlSchemaFile = new File("xpcml.xsd");
        extractFile ("com/ibm/as400/data/xpcml.xsd", xpcmlSchemaFile);

        ProgramCallDocument pcmlDoc = new ProgramCallDocument(systemObject_, "test.xpcml.qgyolaus.xpcml");

        // Set input parameter value.
        String userName = systemObject_.getUserId();
        final String pgmName = "qgyolaus_autu0150";

        pcmlDoc.setIntValue(pgmName+".numberOfRecordsToReturn", -1);
        pcmlDoc.setStringValue(pgmName+".selectionCriteria", "*USER", BidiStringType.DEFAULT);
        pcmlDoc.setStringValue(pgmName+".groupProfileName", "*NONE", BidiStringType.DEFAULT);
        pcmlDoc.setStringValue(pgmName+".profileName", userName);
        String val = pcmlDoc.getStringValue(pgmName+".profileName");
        if (!userName.equals(val)) {
          failed("getStringValue() returned unexpected value: |" + val + "|");
          return;
        }

        String returnedName = null;

        pcmlDoc.setThreadsafeOverride(pgmName, false);
        boolean ok = pcmlDoc.callProgram(pgmName);
        ProgramCall calledPgm = pcmlDoc.getProgramCall();
        if (!ok)
        {
          failed("First programCall failed.");
          if (calledPgm != null) {
            AS400Message[] messagelist = calledPgm.getMessageList();
            for (int i = 0; i < messagelist.length; ++i) {
              System.out.println(messagelist[i]);
            }
          }
          return;
        }

        // listInfo
        if (DEBUG) System.out.println("listInfo fields:");

        int totalRcds = pcmlDoc.getIntValue(pgmName+".listInformation.totalRcds", new int[] {0});
        if (DEBUG) System.out.println("totalRcds: " + totalRcds);

        int rcdsReturned = pcmlDoc.getIntValue(pgmName+".listInformation.rcdsReturned", new int[] {0});
        if (DEBUG) System.out.println("rcdsReturned: " + rcdsReturned);

        byte[] rqsHandle = (byte[])pcmlDoc.getValue(pgmName+".listInformation.rqsHandle", new int[] {0});
        if (DEBUG) System.out.println("rqsHandle: " +
                           ( rqsHandle==null ? "null" : Trace.toHexString(rqsHandle)));

        String infoComplete = pcmlDoc.getStringValue(pgmName+".listInformation.infoComplete", new int[] {0});
        if (DEBUG) System.out.println("infoComplete: " + infoComplete);

        Object dateCreated = pcmlDoc.getValue(pgmName+".listInformation.dateCreated", new int[] {0});
        if (DEBUG) System.out.println("dateCreated: " +
                           ( dateCreated==null ? "null" :
                                   dateFormatterGMT_.format(dateCreated)));
        Object timeCreated = pcmlDoc.getValue(pgmName+".listInformation.timeCreated", new int[] {0});

        if (DEBUG) System.out.println("timeCreated: " +
                           ( timeCreated==null ? "null" :
                                timeFormatterGMT_.format(timeCreated)));

        String listStatus = pcmlDoc.getStringValue(pgmName+".listInformation.listStatus", new int[] {0});
        if (DEBUG) System.out.println("listStatus: " + listStatus);

        int lengthOfInfo = pcmlDoc.getIntValue(pgmName+".listInformation.lengthOfInfo", new int[] {0});
        if (DEBUG) System.out.println("lengthOfInfo: " + lengthOfInfo);

        returnedName = (String)pcmlDoc.getValue(pgmName+".receiverVariable.profileName", new int[] {0});
        if (DEBUG) System.out.println("returnedName: " + returnedName);

        Job job1 = calledPgm.getServerJob();
        if (DEBUG && job1 != null) {
          String job1ID = job1.getName() + "/" + job1.getUser() + "/" + job1.getNumber();
          output_.println("DEBUG job1 name/user/number: |" + job1ID + "|");
        }

        assertCondition(totalRcds > 0 &&
                        rcdsReturned > 0 &&
                        rqsHandle != null &&
                        infoComplete.equals("C")  &&
                        dateCreated instanceof java.sql.Date &&
                        timeCreated instanceof java.sql.Time &&
                        listStatus.equals("2") &&
                        lengthOfInfo >= 62 &&
                        userName.equalsIgnoreCase(returnedName) &&
                        job1 != null
                        );
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception");
      }
      catch (NoClassDefFoundError e)
      {
        // JarMaker may have removed Job class.
        if (e.getMessage().indexOf("Job") != -1 ||
            e.getMessage().indexOf("IntegerHashtable") != -1)
        {
          failed("Job-related class not found: " + e.getMessage());
        }
        else failed(e, "Unexpected exception");
      }
      finally
      {
        if (xpcmlSchemaFile != null) try { xpcmlSchemaFile.delete(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
}
