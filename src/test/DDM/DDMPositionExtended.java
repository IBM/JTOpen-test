///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMPositionExtended.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.*;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 *Testcase DDMPositionExtended.  Verify valid usages of the positionCursor
 *methods for AS400File, KeyedFile and SequentialFile.
 *<ul compact>
 *<li>AS400File.positionCursorAfterLast()
 *<li>AS400File.positionCursorBeforeFirst()
 *<li>AS400File.positionCursorToFirst()
 *<li>AS400File.positionCursorToLast()
 *<li>AS400File.positionCursorToNext()
 *<li>AS400File.positionCursorToPrevious()
 *<li>KeyedFile.positionCursor(key)
 *<li>KeyedFile.positionCursor(key, searchType)
 *<li>KeyedFile.positionCursorAfter(key)
 *<li>KeyedFile.positionCursorBefore(key)
 *<li>SequentialFile.positionCursor(recordNumber)
 *<li>SequentialFile.positionCursorAfter(recordNumber)
 *<li>SequentialFile.positionCursorBefore(recordNumber)
 *</ul>
**/
public class DDMPositionExtended extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMPositionExtended";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMPositionExtended(AS400            systemObject,
                             Vector<String> variationsToRun,
                             int              runMode,
                             FileOutputStream fileOutputStream,  
                            String testLib,
                             int blockingFactor, AS400 pwrsys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMPositionExtended", 18,
          variationsToRun, runMode, fileOutputStream);
     testLib_ = testLib;
    pwrSys_ = pwrsys; // Added for @A1A

  }

  protected void setup() throws Exception {
    try {
      // Make sure ther user has access to the QIWS/QCUSTCDT file
      CommandCall c = new CommandCall(pwrSys_);
      c.run(" GRTOBJAUT OBJ(QIWS/QCUSTCDT) OBJTYPE(*FILE) USER("+systemObject_.getUserId()+") AUT(*USE)   "); 
      
      } catch (Exception e) {
      e.printStackTrace(output_);
      throw e;
    }
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);
    // Do any necessary setup work for the variations
    try {
      setup();
    } catch (Exception e) {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
      return;
    }

    if (runMode_ != ATTENDED)
    {
      // Unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
      {
        setVariation(1);
        Var001();
      }
      if (allVariations || variationsToRun_.contains("2"))
      {
        setVariation(2);
        Var002();
      }
      if (allVariations || variationsToRun_.contains("3"))
      {
        setVariation(3);
        Var003();
      }
      if (allVariations || variationsToRun_.contains("4"))
      {
        setVariation(4);
        Var004();
      }
      if (allVariations || variationsToRun_.contains("5"))
      {
        setVariation(5);
        Var005();
      }
      if (allVariations || variationsToRun_.contains("6"))
      {
        setVariation(6);
        Var006();
      }
      if (allVariations || variationsToRun_.contains("7"))
      {
        setVariation(7);
        Var007();
      }
      if (allVariations || variationsToRun_.contains("8"))
      {
        setVariation(8);
        Var008();
      }
      if (allVariations || variationsToRun_.contains("9"))
      {
        setVariation(9);
        Var009();
      }
      if (allVariations || variationsToRun_.contains("10"))
      {
        setVariation(10);
        Var010();
      }
      if (allVariations || variationsToRun_.contains("11"))
      {
        setVariation(11);
        Var011();
      }
      if (allVariations || variationsToRun_.contains("12"))
      {
        setVariation(12);
        Var012();
      }
      if (allVariations || variationsToRun_.contains("13"))
      {
        setVariation(13);
        Var013();
      }
      if (allVariations || variationsToRun_.contains("14"))
      {
        setVariation(14);
        Var014();
      }
      if (allVariations || variationsToRun_.contains("15"))
      {
        setVariation(15);
        Var015();
      }
      if (allVariations || variationsToRun_.contains("16"))
      {
        setVariation(16);
        Var016();
      }
      if (allVariations || variationsToRun_.contains("17"))
      {
        setVariation(17);
        Var017();
      }
      if (allVariations || variationsToRun_.contains("18"))
      {
        setVariation(18);
        Var018();
      }
    }
  }

  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 1.
  **/
  public void Var001()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }



  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 2.
  **/
  public void Var002()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 3.
  **/
  public void Var003()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 10.
  **/
  public void Var004()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 10, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 100.
  **/
  public void Var005()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 100, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor equal to the number of
   * records in the file.
  **/
  public void Var006()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor of 0.
  **/
  public void Var007()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor
   * equal to the number of records - 1.
  **/
  public void Var008()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length-1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a SequentialFile open for READ_ONLY with a blocking factor
   * equal to the number of records + 1.
  **/
  public void Var009()
  {
    try
    {
      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length+1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      sf.positionCursor(5);
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(3);
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(8);
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(2);
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(6);
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(5);
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  private void createKeyedQCUSTCDT() throws Exception
  {
    KeyedFile kf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE/MYFILE.MBR");
    try { kf.delete(); } catch(Exception e) { }
    
    AS400FileRecordDescription frd = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
    RecordFormat rf = frd.retrieveRecordFormat()[0];
    
    SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/QIWS.LIB/QCUSTCDT.FILE");
    sf.setRecordFormat(rf);

    rf.addKeyFieldDescription(1); // last name
    kf.create(rf, "Chris keyed QCUSTCDT");
    kf.open(AS400File.WRITE_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
    Record[] recs = sf.readAll();
    kf.write(recs);
    kf.close();
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 1.
  **/
  public void Var010()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 2.
  **/
  public void Var011()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 2, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 3.
  **/
  public void Var012()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 3, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

    /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 10.
  **/
  public void Var013()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 10, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 100.
  **/
  public void Var014()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 100, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to the number of records in the file.
  **/
  public void Var015()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to 0.
  **/
  public void Var016()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
  
  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to the number of records - 1.
  **/
  public void Var017()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length-1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }

  /**
   * Tests many possible cursor positions on a KeyedFile open for READ_ONLY with a blocking factor
   * equal to the number of records + 1.
  **/
  public void Var018()
  {
    try
    {
      createKeyedQCUSTCDT();
      KeyedFile sf = new KeyedFile(systemObject_, "/QSYS.LIB/QTEMP.LIB/MYCUSTCDT.FILE");
      sf.setRecordFormat();
      Record[] recs = sf.readAll();
      sf.open(AS400File.READ_ONLY, recs.length+1, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Object[] arr = recs[4].getKeyFields();
      sf.positionCursor(recs[4].getKeyFields());
      Record rec = sf.read();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[2].getKeyFields());
      rec = sf.readNext();
      if (!rec.equals(recs[3]))
      {
        failed("Records not equal (4):\n'"+rec+"' !=\n'"+recs[3]+"'");
        return;
      }
      sf.positionCursor(recs[7].getKeyFields());
      rec = sf.readPrevious();
      if (!rec.equals(recs[6]))
      {
        failed("Records not equal (7):\n'"+rec+"' !=\n'"+recs[6]+"'");
        return;
      }
      sf.positionCursorAfterLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (after last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorBeforeFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (before first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to first):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.read();
      if (!rec.equals(recs[recs.length-1]))
      {
        failed("Records not equal (to last):\n'"+rec+"' !=\n'"+recs[recs.length-1]+"'");
        return;
      }
      sf.positionCursorToFirst();
      rec = sf.readNext();
      if (!rec.equals(recs[1]))
      {
        failed("Records not equal (2):\n'"+rec+"' !=\n'"+recs[1]+"'");
        return;
      }
      sf.positionCursorToLast();
      rec = sf.readPrevious();
      if (!rec.equals(recs[recs.length-2]))
      {
        failed("Records not equal (to last - 1):\n'"+rec+"' !=\n'"+recs[recs.length-2]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.read();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to next 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readNext();
      if (!rec.equals(recs[7]))
      {
        failed("Records not equal (to next 8):\n'"+rec+"' !=\n'"+recs[7]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToNext();
      rec = sf.readPrevious();
      if (!rec.equals(recs[4]))
      {
        failed("Records not equal (to next 5):\n'"+rec+"' !=\n'"+recs[4]+"'");
        return;
      }
      sf.positionCursor(recs[1].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.read();
      if (!rec.equals(recs[0]))
      {
        failed("Records not equal (to previous 1):\n'"+rec+"' !=\n'"+recs[0]+"'");
        return;
      }
      sf.positionCursor(recs[5].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readNext();
      if (!rec.equals(recs[5]))
      {
        failed("Records not equal (to previous 6):\n'"+rec+"' !=\n'"+recs[5]+"'");
        return;
      }
      sf.positionCursor(recs[4].getKeyFields());
      sf.positionCursorToPrevious();
      rec = sf.readPrevious();
      if (!rec.equals(recs[2]))
      {
        failed("Records not equal (to previous 3):\n'"+rec+"' !=\n'"+recs[2]+"'");
        return;
      }
      sf.close();
      succeeded();
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
}
