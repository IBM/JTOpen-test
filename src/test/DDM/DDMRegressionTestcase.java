///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMRegressionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FieldDescription;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;
import com.ibm.as400.access.SystemValue;

import test.Testcase;



/**
Test general usage of DDM for the regression bucket. Includes variations from other testcases as well.
<UL>
<LI>Vars 1 & 2 - Sanity checks.
<LI>Vars 2-18 - Selected DDMLocking variations.
<LI>Vars 19-21 - Selected DDMRecordDescription variations.
<LI>Var 22 - Combined variation from DDMPassword.
</UL>
**/
public class DDMRegressionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMRegressionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String fileName_ = null;


  /**
    Constructor.
  **/
  public DDMRegressionTestcase(AS400            systemObject,
                               Vector<String> variationsToRun,
                               int              runMode,
                               FileOutputStream fileOutputStream,
                               
                               String           testLib,
                               AS400            PwrSys)
  {
    super(systemObject, "DDMRegressionTestcase", 24, variationsToRun, runMode, fileOutputStream);
    setTestLib(testLib);
    pwrSys_ = PwrSys;
  }


  /**
    Run variations.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Do any necessary setup work for the variations
    try
    {
      output_.println("Performing any necessary cleanup from previous run...");
      cleanup();
    }
    catch(Exception e)
    {
      output_.println("Warning - cleanup failed.");
    }
    try
    {
      output_.println("Performing setup for testcase run...");
      setup();
      output_.println("Setup successful.");
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup testcase. Variations not run.");
    }

    if(runMode_ != ATTENDED)
    {
      if(allVariations || variationsToRun_.contains("1"))
        Var001();
      if(allVariations || variationsToRun_.contains("2"))
        Var002();
      if(allVariations || variationsToRun_.contains("3"))
        Var003();
      if(allVariations || variationsToRun_.contains("4"))
        Var004();
      if(allVariations || variationsToRun_.contains("5"))
        Var005();
      if(allVariations || variationsToRun_.contains("6"))
        Var006();
      if(allVariations || variationsToRun_.contains("7"))
        Var007();
      if(allVariations || variationsToRun_.contains("8"))
        Var008();
      if(allVariations || variationsToRun_.contains("9"))
        Var009();
      if(allVariations || variationsToRun_.contains("10"))
        Var010();
      if(allVariations || variationsToRun_.contains("11"))
        Var011();
      if(allVariations || variationsToRun_.contains("12"))
        Var012();
      if(allVariations || variationsToRun_.contains("13"))
        Var013();
      if(allVariations || variationsToRun_.contains("14"))
        Var014();
      if(allVariations || variationsToRun_.contains("15"))
        Var015();
      if(allVariations || variationsToRun_.contains("16"))
        Var016();
      if(allVariations || variationsToRun_.contains("17"))
        Var017();
      if(allVariations || variationsToRun_.contains("18"))
        Var018();
      if(allVariations || variationsToRun_.contains("19"))
        Var019();
      if(allVariations || variationsToRun_.contains("20"))
        Var020();
      if(allVariations || variationsToRun_.contains("21"))
        Var021();
      if(allVariations || variationsToRun_.contains("22"))
        Var022();
      if(allVariations || variationsToRun_.contains("23"))
        Var023();
      if(allVariations || variationsToRun_.contains("24"))
        Var024();
    }

    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectService(AS400.RECORDACCESS);
  }

  /**
   Performs setup needed before running testcases.
   @exception  Exception  If an exception occurs.
   **/
  protected void setup() throws Exception
  {
    try
    {
      // First, end commitment control.
      try
      {
        AS400File.endCommitmentControl(pwrSys_);
      }
      catch(Exception e)
      {
        System.out.println("Couldn't end commitment control: "+e);
      }
      try
      {
        AS400File.endCommitmentControl(systemObject_);
      }
      catch(Exception e)
      {
        System.out.println("Couldn't end commitment control: "+e);
      }

      // Delete and recreate library DDMTEST
      CommandCall c = new CommandCall(pwrSys_);
      deleteLibrary(c, testLib_);

      c = new CommandCall(systemObject_);
      c.run("QSYS/CRTLIB LIB(" + testLib_ + ") AUT(*ALL)");
      AS400Message[] msgs = c.getMessageList();
      if (!(msgs[0].getID().equals("CPF2111") || msgs[0].getID().equals("CPC2102")))
      {
        for (int i = 0; i < msgs.length; ++i)
        {
          System.out.println(msgs[i]);
        }
        throw new Exception("");
      }
      // Create the necessary files for this testcase
      fileName_ = "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR";
      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      f1.create(new DDMLockFormat(systemObject_), "DDMLocking file");

      // Create journal receiver and journal if it does not already exist
      String msg = runCommand("QSYS/CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')", systemObject_);
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRNRCV JRNRCV(QGPL/JT4DDMRCV) THRESHOLD(256000) AUT(*ALL) TEXT('Receiver for DDM test cases')'");
        output_.println(msg);
        f1.close(); 
        throw new Exception("");
      }
      msg = runCommand("QSYS/CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')", systemObject_);
      if (msg != null && !msg.equals("CPF7010"))
      {
        output_.println("Failure executing 'CRTJRN JRN(QGPL/JT4DDMJRN) JRNRCV(QGPL/JT4DDMRCV) MNGRCV(*SYSTEM) DLTRCV(*YES) AUT(*ALL) TEXT('DDM test case journal')'");
        output_.println(msg);
        f1.close(); 
        throw new Exception("");
      }

      // Start journaling
      msg = runCommand("QSYS/STRJRNPF FILE("+testLib_+"/DDMLOCK) JRN(QGPL/JT4DDMJRN)", systemObject_);
      if (msg != null)
      {
        output_.println("Failure executing 'STRJRNPF FILE("+testLib_+"/DDMLOCK) JRN(QGPL/JT4DDMJRN)'");
        output_.println(msg);
        f1.close(); 
        throw new Exception("");
      }
      f1.close(); 
    }
    catch(Exception e)
    {
      output_.println("Unexpected exception during setup.");
      e.printStackTrace(output_);
      throw e;
    }
  }


  String runCommand(String command, AS400 system)
  {
    String msg = null;
    CommandCall cmd_ = new CommandCall();
    try
    {
      cmd_.setSystem(system);
      // Run the command.
      cmd_.run(command);

      // If there are any messages then save the ones that potentially
      // indicate problems.
      AS400Message[] msgs = cmd_.getMessageList();
/*
    for (int i = 0; i < msgs.length; ++i)
    {
      output_.println(msgs[i]);
    }
*/
      if (msgs.length > 0 && msgs[0].getID().toUpperCase().startsWith("CPF"))
      {
        msg = msgs[0].getID().toUpperCase() + ": " + msgs[0].getText();
      }
    }
    catch (Exception e)
    {
      msg = e.toString();
      e.printStackTrace(output_);
    }

    return msg;
  }


  /**
   Performs cleanup needed after running testcases.
   @exception  Exception  If an exception occurs.
   **/
  protected void cleanup() throws Exception
  {
    boolean success = true;
    try
    {
      // Stop journaling
      output_.println("  Ending journaling...");
      String msg = runCommand("ENDJRNPF FILE("+testLib_+"/DDMLOCK) JRN(QGPL/JT4DDMJRN)", systemObject_);
      if (msg != null && !msg.startsWith("CPF9803") && !msg.startsWith("CPF9801") && !msg.startsWith("CPF7032")) //7032 because on cleanup, the system might think we are not actually journaling the file.
      {
        output_.println("Failure executing 'ENDJRNPF FILE("+testLib_+"/DDMLOCK) JRN(QGPL/JT4DDMJRN)'");
        output_.println(msg);
        success = false;
      }

      // Delete the files created during setup()
      output_.println("  Deleting file...");
      msg = runCommand("QSYS/DLTF FILE("+testLib_+"/DDMLOCK)", systemObject_);
      if (msg != null && msg.startsWith("CPF3202"))
      {
        output_.println("Error: File DDMLOCK in library "+testLib_+" is still locked by a DDM server job.");
        output_.println("       Please signon to "+systemObject_.getSystemName()+" and do a WRKOBJLCK OBJ("+testLib_+"/DDMLOCK) OBJTYPE(*FILE).");
        output_.println("       Type a '4' (End job) on the line containing the QRWTSRVR job for user QUSER.");
        output_.println("       After pressing Enter twice, use the F5 key to refresh the screen.");
        output_.println("       Verify that there are no more locks for that file.");
        output_.println("       Repeat the above procedure for WRKOBJLCK OBJ(QGPL/JT4DDMRCV) OBJTYPE(*JRNRCV).");
        output_.println("       Re-run this testcase.");
        throw new Exception("Delete file locks and re-run testcase.");
      }

      if (msg != null && !msg.startsWith("CPC2191") && !msg.startsWith("CPF2110") && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTF FILE("+testLib_+"/DDMLOCK)'");
        output_.println(msg);
        success = false;
      }
//      SequentialFile f1 = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
//      f1.delete();

      // Delete the DDMTest library and the journal file and receiver.
      output_.println("  Deleting library...");
      CommandCall cmd_ = new CommandCall(systemObject_); 

      msg = deleteLibrary(cmd_, testLib_); 
      if (msg != null && !msg.startsWith("CPF2110"))
      {
        output_.println("Failure executing 'deleteLibrary " + testLib_ + "'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting journal...");
      msg = runCommand("QSYS/DLTJRN QGPL/JT4DDMJRN", systemObject_);
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRN QGPL/JT4DDMJRN'");
        output_.println(msg);
        success = false;
      }
      output_.println("  Deleting receiver...");
      msg = runCommand("QSYS/DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV) DLTOPT(*IGNINQMSG)", systemObject_);
      if (msg != null && !msg.startsWith("CPF2105"))
      {
        output_.println("Failure executing 'DLTJRNRCV JRNRCV(QGPL/JT4DDMRCV) DLTOPT(*IGNINQMSG)'");
        output_.println(msg);
        success = false;
      }
    }
    catch (Exception e)
    {
      System.out.println("Cleanup unsuccessful. Delete file " + testLib_ + "/DDMLOCK if it exists.");
      e.printStackTrace(output_);
      throw e;
    }
    if (!success)
    {
      throw new Exception("Error: Cleanup was unsuccessful");
    }
    else
    {
      output_.println("Cleanup successful.");
    }
  }


  /**
   * Test retrieving a record format and then reading records with readAll() vs. readNext().
  **/
  public void Var001()
  {
      if (checkNotGroupTest()) { /* Skip long running test for group test */ 
	  setVariation(1);
	  SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
	  try
	  {
	      sf.setRecordFormat();
	      Record[] recs1 = sf.readAll();
	      sf.open();
	      Vector<Record> vec = new Vector<Record>();
	      Record rec = sf.readNext();
	      while (rec != null)
	      {
		  vec.addElement(rec);
		  rec = sf.readNext();
	      }
	      Record[] recs2 = new Record[vec.size()];
	      vec.copyInto(recs2);
	      if (recs1.length == recs2.length)
	      {
		  for (int i=0; i<recs1.length; ++i)
		  {
		      if (!recs1[i].toString().equals(recs2[i].toString()))
		      {
			  failed("Records do not match ("+i+"): '"+recs1[i].toString()+"' != '"+recs2[i].toString()+"'");
			  try { sf.close(); } catch(Exception e) {}
			  return;
		      }
		  }
	      }
	      else
	      {
		  failed("Number of records do not match: "+recs1.length+" != "+recs2.length);
		  try { sf.close(); } catch(Exception e) {}
		  return;
	      }
	      succeeded();
	  }
	  catch(Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
	  try { sf.close(); } catch(Exception e) {}
      }
  }


  /**
   * Test creating a file and writing records, then read them back.
  **/
  public void Var002()
  {
    setVariation(2);
    SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/RTEST.FILE/MBR1.MBR");
    SequentialFile sf2 = new SequentialFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/RTEST.FILE");

    try
    {
      try { sf.delete(); } catch(Exception e) {}

      RecordFormat rf = new RecordFormat("MYRF");
      rf.addFieldDescription(new CharacterFieldDescription(new AS400Text(20, systemObject_.getCcsid(), systemObject_), "F1"));
      sf.create(rf, "Regression test file.");
      sf.open();
      Record rec = rf.getNewRecord();
      rec.setField("F1", "This is record 1.");
      sf.write(rec);
      Record[] recs = new Record[999];
      for (int i=2; i<=1000; ++i)
      {
        recs[i-2] = rf.getNewRecord();
        recs[i-2].setField("F1", "This is record "+i+".");
      }
      sf.write(recs);
      sf.close();
      
      sf2.setRecordFormat();
      sf2.open(AS400File.READ_ONLY, 1000, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Vector<Record> vec = new Vector<Record>();
      sf2.positionCursorAfterLast();
      Record readRec = sf2.readPrevious();
      while (readRec != null)
      {
        vec.addElement(readRec);
        readRec = sf2.readPrevious();
      }
      Record[] readRecs = new Record[vec.size()];
      vec.copyInto(readRecs);
      if (readRecs.length == 1000)
      {
        for (int i=0; i<1000; ++i)
        {
          String cmp = "This is record "+(1000-i)+".";
          String r = readRecs[i].getField(0).toString().trim();
          if (!cmp.equals(r))
          {
            failed("Record verification failed ("+i+"): '"+cmp+"' != '"+r+"'");
            return;
          }
        }
      }
      else
      {
        failed("Number of records not equal: "+readRecs.length+" != 1000");
        //for (int i=0; i<readRecs.length; ++i) System.out.println(readRecs[i].getField(0));
        return;
      }
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    } finally {
      try { sf2.close(); } catch(Exception e) {}
    }
  }

  /**
   *Verify invalid use of AS400File.lock(int).
   *<ul compact>
   *<li>Specify an invalid lock level parameter.
   *<ul compact>
   *<li>Specify one less than the minimum lock value allowed.
   *<li>Specify one more than the maximum lock value allowed.
   *</ul>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>ExtendedIllegalArgumentException indicating parameter value is not valid.
   *</ul>
  **/
  public void Var003()
  {
    setVariation(3);
    SequentialFile file = null; 
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK - 1);
      failed("Exception didn't occur when specifying READ_ALLOWED_SHARED_WRITE_LOCK - 1.");
    }
    catch (Exception e)
    {
      if (exceptionIs(e, "ExtendedIllegalArgumentException", "lockToObtain: Parameter value is not valid."))
      {
        try
        {
          file.close(); 
          file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK + 1);
          failed("Exception didn't occur when specifying WRITE_EXCLUSIVE_LOCK + 1.");
        }
        catch (Exception e1)
        {
          if (!exceptionIs(e, "ExtendedIllegalArgumentException", "lockToObtain: Parameter value is not valid."))
          {
            failed(e1, "Wrong exception/text returned");
          }
          else
          {
            succeeded();
          }
        }
      }
      else
      {
        failed(e, "Wrong exception/text returned");
      } 
    } finally { 
      if (file != null ) try { 
        file.close(); 
      } catch (Exception e) { } 
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var004()
  {
      if (checkNotGroupTest()) { 
	  setVariation(4);
	  SequentialFile file = null;
	  SequentialFile violator = null;
	  try
	  {
	      file = new SequentialFile(systemObject_,
					"/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
	      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
	      AS400 newsys = new AS400(systemObject_);
	      newsys.setMustUseSockets(true);

	      violator = new SequentialFile(newsys, file.getPath());
	      violator.setRecordFormat(new DDMLockFormat(systemObject_));
	      try
	      {
		  violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failed("No exception with READ_ONLY");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      failed(e, "Wrong exception id with READ_ONLY.");
		  }
		  else
		  {
		      try
		      {
			  violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
			  failed("Exception didn't occur with READ_WRITE.");
		      }
		      catch (AS400Exception e1)
		      {
			  msg = e1.getAS400Message();
			  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
			  {
			      failed(e1, "Wrong exception id with READ_WRITE.");
			  }
			  else
			  {
			      try
			      {
				  violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
				  failed("Exception didn't occur with WRITE_ONLY.");
			      }
			      catch (AS400Exception e2)
			      {
				  msg = e.getAS400Message();
				  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
				  {
				      failed(e2, "Wrong exception id with WRITE_ONLY.");
				  }
				  else
				  {
				      succeeded();
				  }
			      }
			  }
		      }
		  }
	      }
	      catch (Exception e)
	      {
		  failed(e, "Incorrect exception.");
	      }
      // Disconnect from the system for violator.  This is to prevent the subsystem
      // from filling up with jobs.
	      violator.getSystem().disconnectService(AS400.RECORDACCESS);
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
    // Release the lock obtained
	  try
	  {
	      file.releaseExplicitLocks();
	  }
	  catch (Exception e)
	  {
	      System.out.println("Failed to release explicit locks for " + file.getPath());
	  }
      }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read only.
   *</ul>
   **/
  public void Var005()
  {
    setVariation(5);
    SequentialFile file = null;
    SequentialFile violator = null;
    try
    {
      file = new SequentialFile(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      violator = new SequentialFile(newsys, file.getPath());
      violator.setRecordFormat(file.getRecordFormat());
      violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      failed("Exception didn't occur.");
    }
    catch (AS400Exception e)
    {
      AS400Message msg = e.getAS400Message();
      if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
      {
        failed(e, "Wrong exception id.");
      }
      else
      {
        succeeded();
      }
    }
    catch (Exception e)
    {
      failed(e, "Incorrect exception.");
    }

    try
    {
      file.close();
    }
    catch (Exception e)
    {
    }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
    if (violator != null)
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var006()
  {
    setVariation(6);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);

      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned.");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }

    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var007()
  {
    setVariation(7);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);

      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();

      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file1.getRecordFormat());
      try
      {
        violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned.");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception occurred");
    }

    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var008()
  {
    setVariation(8);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    // Release the lock on the file
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of READ_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var009()
  {
    setVariation(9);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var010()
  {
      if (checkNotGroupTest()) { 

	  setVariation(10);
	  StringBuffer failMsg = new StringBuffer();
	  SequentialFile file = null;
	  SequentialFile violator = null;
	  try
	  {
	      file = new SequentialFile(systemObject_,
					"/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
	      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
	      AS400 newsys = new AS400(systemObject_);
	      newsys.setMustUseSockets(true);
	      violator = new SequentialFile(newsys, file.getPath());
	      violator.setRecordFormat(new DDMLockFormat(systemObject_));
	      try
	      {
		  violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with READ_ONLY.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with READ_ONLY.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with READ_ONLY.");
	      }
	      try
	      {
		  violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with READ_WRITE.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with READ_WRITE.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with READ_WRITE.");
	      }
	      try
	      {
		  violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with WRITE_ONLY.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with WRITE_ONLY.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with WRITE_ONLY.");
	      }
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
    // Release the lock obtained
	  try
	  {
	      file.releaseExplicitLocks();
	  }
	  catch (Exception e)
	  {
	  }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
	  if (violator != null)
	  {
	      violator.getSystem().disconnectService(AS400.RECORDACCESS);
	  }
	  if (failMsg.length() == 0)
	  {
	      succeeded();
	  }
	  else
	  {
	      failed(failMsg.toString());
	  }
      }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_EXCLUSIVE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should not be able to open the file for read, read/write or write.
   *</ul>
   **/
  public void Var011()
  {
      if (checkNotGroupTest()) { 

	  setVariation(11);
	  StringBuffer failMsg = new StringBuffer();
	  SequentialFile file = null;
	  SequentialFile violator = null;
	  try
	  {
	      file = new SequentialFile(systemObject_,
					"/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
	      file.setRecordFormat(new DDMLockFormat(systemObject_));
	      file.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
	      file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
	      AS400 newsys = new AS400(systemObject_);
	      newsys.setMustUseSockets(true);
	      violator = new SequentialFile(newsys, file.getPath());
	      violator.setRecordFormat(new DDMLockFormat(systemObject_));
	      try
	      {
		  violator.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with READ_ONLY.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with READ_ONLY.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with READ_ONLY.");
	      }
	      try
	      {
		  violator.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with READ_WRITE.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with READ_WRITE.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with READ_WRITE.");
	      }
	      try
	      {
		  violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
		  failMsg.append("Exception didn't occur with WRITE_ONLY.");
	      }
	      catch (AS400Exception e)
	      {
		  AS400Message msg = e.getAS400Message();
		  if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
		  {
		      e.printStackTrace(output_);
		      failMsg.append("Wrong exception id with WRITE_ONLY.");
		  }
	      }
	      catch (Exception e)
	      {
		  e.printStackTrace(output_);
		  failMsg.append("Incorrect exception with WRITE_ONLY.");
	      }
	  }
	  catch (Exception e)
	  {
	      failed(e, "Unexpected exception.");
	  }
    // Close the file
	  try
	  {
	      file.releaseExplicitLocks();
	      file.close();
	  }
	  catch (Exception e)
	  {
	  }
    // Disconnect from the system for violator.  This is to prevent the subsystem from filling
    // up with jobs.
	  if (violator != null)
	      violator.getSystem().disconnectService(AS400.RECORDACCESS);
	  if (failMsg.length() == 0)
	  {
	      succeeded();
	  }
	  else
	  {
	      failed(failMsg.toString());
	  }
      }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var012()
  {
    setVariation(12);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        violator.close();
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_READ_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for reading.
   *<li>Another connection should not be able to open the file for writing.
   *</ul>
   **/
  public void Var013()
  {
    setVariation(13);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    SequentialFile violator = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      AS400 vSys = new AS400(systemObject_); //@A1A
      vSys.setMustUseSockets(true); //@A1A
      violator = new SequentialFile(vSys, file1.getPath()); //@A1C
      violator.setRecordFormat(file2.getRecordFormat());
      try
      {
        violator.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
        violator.close();
        failed("Exception didn't occur.");
      }
      catch (AS400Exception e)
      {
        AS400Message msg = e.getAS400Message();
        if (msg.getID().toUpperCase().indexOf("CPF4128") == -1)
        {
          failed(e, "Wrong exception id returned");
        }
        else
        {
          succeeded();
        }
      }
      catch (Exception e)
      {
        failed(e, "Incorrect exception.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connections
    if (file2 != null)
    {
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
    }
    if (violator != null)
    {
      violator.getSystem().disconnectService(AS400.RECORDACCESS);
    }
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is closed.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
   **/
  public void Var014()
  {
    setVariation(14);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(new DDMLockFormat(systemObject_));
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.lock(int) with a lock type of WRITE_ALLOW_SHARED_WRITE_LOCK.
   *<ul compact>
   *<li>Set the lock when the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>Another connection should be able to open the file for read, read/write or write.
   *<li>No exceptions should occur.
   *</ul>
  **/
  public void Var015()
  {
    setVariation(15);
    SequentialFile file1 = null;
    SequentialFile file2 = null;
    try
    {
      file1 = new SequentialFile(systemObject_,
                                 "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file1.setRecordFormat(new DDMLockFormat(systemObject_));
      file1.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file1.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
      AS400 newsys = new AS400(systemObject_);
      newsys.setMustUseSockets(true);
      file2 = new SequentialFile(newsys, file1.getPath());
      file2.setRecordFormat(file1.getRecordFormat());
      file2.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      file2.open(AS400File.READ_WRITE, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file2.close();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }

    try
    {
      file1.releaseExplicitLocks();
      file1.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    // Disconnect from the other connection
    if (file2 != null)
      file2.getSystem().disconnectService(AS400.RECORDACCESS);
  }

  /**
   *Verify AS400File.getExplicitLocks().
   *<ul compact>
   *<li>With no locks set.
   *<li>With one lock set.
   *<li>With all locks set.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No locks set will return an int[] of size 0.
   *<li>One lock set will return an int[] of size one containing the lock that was set.
   *<li>All locks set will return an int[] of size six containing the locks that were set.
   *</ul>
   **/
  public void Var016()
  {
    setVariation(16);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      if (file.getExplicitLocks().length == 0)
      {
        file.lock(AS400File.READ_EXCLUSIVE_LOCK);
        int[] locks = file.getExplicitLocks();
        if (locks.length == 1 && locks[0] == AS400File.READ_EXCLUSIVE_LOCK)
        {
          file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
          locks = file.getExplicitLocks();
          Vector<Integer> v = new Vector<Integer>(locks.length);
          for (int i = 0; i < locks.length; i++)
          {
            v.addElement(Integer.valueOf(locks[i]));
          }
          assertCondition(locks.length == 6 &&
                 v.contains(Integer.valueOf(AS400File.READ_EXCLUSIVE_LOCK)) &&
                 v.contains(Integer.valueOf(AS400File.READ_ALLOW_SHARED_READ_LOCK)) &&
                 v.contains(Integer.valueOf(AS400File.READ_ALLOW_SHARED_WRITE_LOCK)) &&
                 v.contains(Integer.valueOf(AS400File.WRITE_EXCLUSIVE_LOCK)) &&
                 v.contains(Integer.valueOf(AS400File.WRITE_ALLOW_SHARED_READ_LOCK)) &&
                 v.contains(Integer.valueOf(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK)));
        }
        else
        {
          failed("Incorrect lock information.");
        }
      }
      else
      {
        failed("Incorrect lock information.");
      }
    }
    catch (Exception e)
    {
      failed(e);
    }
    try
    {
      file.releaseExplicitLocks();
      file.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   *Verify AS400File.releaseExplicitLocks().
   *<ul compact>
   *<li>When no locks are set and the file is closed.
   *<li>When no locks are set and the file is open.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>No exceptions are thrown.
   *</ul>
   **/
  public void Var017()
  {
    setVariation(17);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.releaseExplicitLocks();
      file.close();
      file.releaseExplicitLocks();
      succeeded();
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   *Verify AS400File.releaseExplicitLocks().
   *<ul compact>
   *<li>With one lock obtained and the file is closed.
   *<li>With one lock obtained and the file is open.
   *<li>With all locks obtained and the file is closed.
   *<li>With all locks obtained and the file is open.
   **/
  public void Var018()
  {
    setVariation(18);
    SequentialFile file = null;
    try
    {
      file = new SequentialFile(systemObject_,
                                "/QSYS.LIB/" + testLib_ + ".LIB/DDMLOCK.FILE/MBR1.MBR");
      file.setRecordFormat(new DDMLockFormat(systemObject_));
      file.lock(AS400File.READ_EXCLUSIVE_LOCK);
      file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      file.releaseExplicitLocks();
      if (file.getExplicitLocks().length == 0)
      {
        file.close();
        file.lock(AS400File.READ_EXCLUSIVE_LOCK);
        file.releaseExplicitLocks();
        if (file.getExplicitLocks().length == 0)
        {
          file.lock(AS400File.READ_EXCLUSIVE_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
          file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
          file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
          file.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
          file.releaseExplicitLocks();
          if (file.getExplicitLocks().length == 0)
          {
            file.close();
            file.lock(AS400File.READ_EXCLUSIVE_LOCK);
            file.lock(AS400File.READ_ALLOW_SHARED_READ_LOCK);
            file.lock(AS400File.READ_ALLOW_SHARED_WRITE_LOCK);
            file.lock(AS400File.WRITE_EXCLUSIVE_LOCK);
            file.lock(AS400File.WRITE_ALLOW_SHARED_READ_LOCK);
            file.lock(AS400File.WRITE_ALLOW_SHARED_WRITE_LOCK);
            file.releaseExplicitLocks();
            assertCondition(file.getExplicitLocks().length == 0);
          }
          else
          {
            failed("Incorrect lock information.");
          }
        }
        else
        {
          failed("Incorrect lock information.");
        }
      }
      else
      {
        failed("Incorrect lock information.");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }



  /**
   *Verify valid usage of AS400FileRecordDescription().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns an empty string
   *<li>getMemberName() returns an empty string
   *<li>getFileName() returns an empty string
   *<li>getSystem() returns null.
   *</ul>
  **/
  public void Var019()
  {
    setVariation(19);
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription();
      if (!rd.getPath().equals(""))
      {
        failed("Empty string not returned for getPath()");
        return;
      }
      if (!rd.getMemberName().equals(""))
      {
        failed("Empty string not returned for getMemberName()");
        return;
      }
      if (!rd.getFileName().equals(""))
      {
        failed("Empty string not returned for getFileName()");
        return;
      }
      if (rd.getSystem() != null)
      {
        failed("Null not returned for getSystem()");
        return;
      }
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify valid usage of AS400FileRecordDescription(system, name).
   *<ul compact>
   *<li>Pass in a name with no member
   *<li>Pass in a name containing special value %FILE% 
   *<li>Pass in a name with a member
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getSystem() will return the AS400 object passed in.
   *<li>getPath() will return the path name passed in.
   *<li>getMember() will return *FIRST when no member is specified
   *<li>getMember() will return the file name when %FILE% is specified
   *for the member name.
   *<li>getMember() will return the member name specified when a member
   *name is specified.
   *<li>getFileName() will return the file name specified.
   *</ul>
  **/
  public void Var020()
  {
    setVariation(20);
    try
    {
      AS400FileRecordDescription rd1 = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/MYFILE.FILE");
      AS400FileRecordDescription rd2 = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/" + testLib_ + ".LIB/MYFILE.FILE/%FILE%.MBR");
      AS400FileRecordDescription rd3 = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/MYFILE.FILE/MYMBR.MBR");

      if (systemObject_ != rd1.getSystem())
      {
        failed("Incorrect system object returned from getSystem()");
        return;
      }
      if (!rd2.getPath().equals("/QSYS.LIB/" + testLib_ + ".LIB/MYFILE.FILE/%FILE%.MBR"))
      {
        failed("Incorrect path returned from getPath()");
        return;
      }
      if (!rd1.getMemberName().equals("*FIRST"))
      {
        failed("Incorrect member name (rd1) returned from getMemberName()");
        return;
      }
      if (!rd2.getMemberName().equals("MYFILE"))
      {
        failed("Incorrect member name (rd2) returned from getMemberName()");
        return;
      }
      if (!rd3.getMemberName().equals("MYMBR"))
      {
        failed("Incorrect member name (rd3) returned from getMemberName()");
        return;
      }
      if (!rd1.getFileName().equals("MYFILE"))
      {
        failed("Incorrect file name (rd1) returned from getFileName()");
        return;
      }
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }

  /**
   *Verify invalid usage of AS400FileRecordDescription(system, name).
   *<ul compact>
   *<li>Specify null for <i>system</i>
   *<li>Specify null for <i>name</i>
   *<li>Specify invalid object type in <i>name</i>
   *<li>Specify invalid IFS name (member without file) for <i>name</i>
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "system"
   *<li>NullPointerException indicating "name"
   *<li>IllegalPathNameException indicating the name value and
   *OBJECT_TYPE_NOT_VALID
   *<li>IllegalPathNameException indicating the name value and
   *MEMBER_WITHOUT_FILE
   *</ul>
  **/
  public void Var021()
  {
    setVariation(21);
    try
    {
      AS400FileRecordDescription rd = null;
      String name = "/QSYS.LIB/FILE.FILE/MBR.MBR";
      String badObject = "/QSYS.LIB/FILE.DTAQ";
      String noFile = "/QSYS.LIB/MBR.MBR";
      try
      {
        rd = new AS400FileRecordDescription(null, name);
        failed("No exception specifying null for system"+rd);
        return;
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "system"))
        {
          failed(e, "Incorrect exception specifying null for system");
          return;
        }
      }
      try
      {
        rd = new AS400FileRecordDescription(systemObject_, null);
        failed("No exception specifying null for name");
        return;
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "NullPointerException", "name"))
        {
          failed(e, "Incorrect exception specifying null for name");
          return;
        }
      }
      try
      {
        rd = new AS400FileRecordDescription(systemObject_, badObject);
        failed("No exception specifying incorrect object type for name");
        return;
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "IllegalPathNameException", badObject, IllegalPathNameException.OBJECT_TYPE_NOT_VALID))
        {
          failed(e, "Incorrect exception specifying incorrect object type");
          return;
        }
      }
      try
      {
        rd = new AS400FileRecordDescription(systemObject_, noFile);
        failed("No exception specifying incorrect IFS name for name");
        return;
      }
      catch(Exception e)
      {
        if (!exceptionStartsWith(e, "IllegalPathNameException", noFile, IllegalPathNameException.MEMBER_WITHOUT_FILE))
        {
          failed(e, "Incorrect exception specifying incorrect IFS name");
          return;
        }
      }

      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }


  /**
   *Test connection to a system depending on its release level.
   *<ul compact>
   *<li>No exchange of security information is done in this case.
   *<li>No password substitution is done.
   *</ul>
   *
   *Expected results:
   *<ul compact>
   *<li>The connection will be successfully established.
   *</ul>
  **/
  public void Var022()
  {
    setVariation(22);
    try
    {
      // pre-V4R2
      if (systemObject_.getVRM() < AS400.generateVRM(4, 2, 0))
      {
        output_.println("Connecting to DDM server on pre-V4R2 system.");
        systemObject_.connectService(AS400.RECORDACCESS);
        systemObject_.disconnectService(AS400.RECORDACCESS);
      }
      else if (systemObject_.getVRM() < AS400.generateVRM(4, 4, 0) &&
          systemObject_.getVRM() >= AS400.generateVRM(4, 2, 0))
      {
        output_.println("Connecting to DDM server on V4R2 or V4R3 system.");
        systemObject_.connectService(AS400.RECORDACCESS);
        systemObject_.disconnectService(AS400.RECORDACCESS);
      }
      else if (systemObject_.getVRM() >= AS400.generateVRM(4, 4, 0) &&
          systemObject_.getVRM() < AS400.generateVRM(4, 6, 0))
      {
        output_.println("Connecting to DDM server on V4R4 or V4R5 system.");
        systemObject_.connectService(AS400.RECORDACCESS);
        systemObject_.disconnectService(AS400.RECORDACCESS);
      }
      else if (systemObject_.getVRM() >= AS400.generateVRM(5, 1, 0))
      {
        output_.println("Connecting to DDM server on V5R1 or higher system.");
        SystemValue sv = new SystemValue(systemObject_, "QPWDLVL");
        String val = sv.getValue().toString().trim();
        int i = Integer.parseInt(val);
        output_.println("System is at password level "+i+".");
        if (i == 0 || i == 1)
        {
          systemObject_.connectService(AS400.RECORDACCESS);
          systemObject_.disconnectService(AS400.RECORDACCESS);
        }
        else if (i == 2 || i == 3 || i == 4 || i == 5)
        {
          systemObject_.connectService(AS400.RECORDACCESS);
          systemObject_.disconnectService(AS400.RECORDACCESS);
        }
        else
        {
          failed("Unknown password level: "+i);
          return;
        }
      }
      else
      {
        failed("Unknown or unsupported release of OS/400: "+systemObject_.getVRM());
        return;
      }
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
    }
  }


  /**
   * Test getFieldAsBytes(int).
  **/
  public void Var023()
  {
    setVariation(23);
    SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
    boolean ok = true;
    try
    {
      sf.setRecordFormat();
      Record[] recs1 = sf.readAll();
      sf.open();
      Vector<Record> vec = new Vector<Record>();
      Record rec = sf.readNext();
      while (rec != null)
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FieldDescription[] fieldDescs = rec.getRecordFormat().getFieldDescriptions();
        for (int i=0; i<rec.getNumberOfFields(); i++)
        {
          byte[] rawBytes = rec.getFieldAsBytes(i);
          baos.write(rawBytes, 0, rawBytes.length);
          byte[] convertedBytes = fieldDescs[i].getDataType().toBytes(rec.getField(i));
          if (!Arrays.equals(rawBytes, convertedBytes))
          {
            System.out.println("Bytes mismatch for field " + i);
            ok = false;
          }
          ///else System.out.println("Bytes match"); //TBD temporary
        }
        rec.setContents(baos.toByteArray());
        vec.addElement(rec);
        rec = sf.readNext();
      }
      Record[] recs2 = new Record[vec.size()];
      vec.copyInto(recs2);
      if (recs1.length == recs2.length)
      {
        for (int i=0; i<recs1.length; ++i)
        {
          if (!recs1[i].toString().equals(recs2[i].toString()))
          {
            failed("Records do not match ("+i+"): '"+recs1[i].toString()+"' != '"+recs2[i].toString()+"'");
            try { sf.close(); } catch(Exception e) {}
            return;
          }
        }
      }
      else
      {
        failed("Number of records do not match: "+recs1.length+" != "+recs2.length);
        try { sf.close(); } catch(Exception e) {}
        return;
      }
      if (ok) succeeded();
      else failed();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    try { sf.close(); } catch(Exception e) {}
  }


  /**
   * Test getFieldAsBytes(String).
  **/
  public void Var024()
  {
    setVariation(24);
    SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
    boolean ok = true;
    try
    {
      sf.setRecordFormat();
      Record[] recs1 = sf.readAll();
      sf.open();
      Vector<Record> vec = new Vector<Record>();
      Record rec = sf.readNext();
      while (rec != null)
      {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FieldDescription[] fieldDescs = rec.getRecordFormat().getFieldDescriptions();
        String[] fieldNames = rec.getRecordFormat().getFieldNames();
        for (int i=0; i<rec.getNumberOfFields(); i++)
        {
          ///System.out.println("Field name: |" + fieldNames[i] +"|"); //TBD temporary
          byte[] rawBytes = rec.getFieldAsBytes(fieldNames[i]);
          baos.write(rawBytes, 0, rawBytes.length);
          byte[] convertedBytes = fieldDescs[i].getDataType().toBytes(rec.getField(i));
          if (!Arrays.equals(rawBytes, convertedBytes))
          {
            System.out.println("Bytes mismatch for field " + i);
            ok = false;
          }
          ///else System.out.println("Bytes match"); //TBD temporary
        }
        rec.setContents(baos.toByteArray());
        vec.addElement(rec);
        rec = sf.readNext();
      }
      Record[] recs2 = new Record[vec.size()];
      vec.copyInto(recs2);
      if (recs1.length == recs2.length)
      {
        for (int i=0; i<recs1.length; ++i)
        {
          if (!recs1[i].toString().equals(recs2[i].toString()))
          {
            failed("Records do not match ("+i+"): '"+recs1[i].toString()+"' != '"+recs2[i].toString()+"'");
            try { sf.close(); } catch(Exception e) {}
            return;
          }
        }
      }
      else
      {
        failed("Number of records do not match: "+recs1.length+" != "+recs2.length);
        try { sf.close(); } catch(Exception e) {}
        return;
      }
      if (ok) succeeded();
      else failed();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
    try { sf.close(); } catch(Exception e) {}
  }




}

