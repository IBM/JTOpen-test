///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9907036.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400File;
import com.ibm.as400.access.AS400FileRecordDescription;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMP9907036.  Verify fix for P9907036. This is to make sure that:
 *<OL>
 *<LI>The AS400Text's CCSID does not change when used with a RecordFormat.
 *<LI>The AS400File.read(key) method operates consistently with past releases
 * when reading using a String key on a fixed length key field.
 *</OL>
**/
public class DDMP9907036 extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9907036";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9907036(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9907036", 2,
          variationsToRun, runMode, fileOutputStream);
    setTestLib(testLib);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }
    systemObject_.disconnectAllServices();
  }

  /**
   *Verify the AS400Text's ccsid does not change.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      CommandCall cc = new CommandCall(systemObject_);
      deleteLibrary(cc, testLib_); 
      cc.run("QSYS/CRTLIB "+testLib_);
      cc.run("QSYS/DLTF "+testLib_+"/TEST7036");

      int ccsid = 835;
      AS400Text t = new AS400Text(10, ccsid, systemObject_);
      RecordFormat rf = new RecordFormat("MYRF");
      CharacterFieldDescription cfd = new CharacterFieldDescription(t, "FLD1");
      rf.addFieldDescription(cfd);

      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/TEST7036.FILE/TEST7036.MBR");
      sf.create(rf, "Testing P9907036.");

      int newCcsid = t.getCcsid();
      if (newCcsid != ccsid)
      {
        failed("CCSIDs not equal: "+newCcsid+" != "+ccsid);
      }
      else
      {
        succeeded();
      }
      sf.close(); 
      sf.delete();
      deleteLibrary(cc, testLib_);

    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }


  /**
   *Verify AS400File.read(key) works as expected.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var002()
  {
    int length = 10;
    int ccsid = 37;
    try
    {
      CommandCall cc = new CommandCall(systemObject_);
      cc.run("QSYS/CRTLIB "+testLib_);
      cc.run("QSYS/DLTF "+testLib_+"/TEST7036");
      KeyedFile f = new KeyedFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/TEST7036.FILE/TEST7036.MBR");
      RecordFormat rf = new RecordFormat("MYRF");
      AS400Text t;
      try
      {
        t = new AS400Text(length, ccsid, systemObject_);
      }
      catch(NoSuchMethodError nsme)
      {
        t = new AS400Text(length, ccsid);
      }
      rf.addFieldDescription(new BinaryFieldDescription(new AS400Bin4(), "MARKER"));
      CharacterFieldDescription cfd = new CharacterFieldDescription(t, "FLD1");
      rf.addFieldDescription(cfd);
      rf.addKeyFieldDescription("FLD1");

      f.create(rf, "Testing P9907036.");

      f.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record rec = rf.getNewRecord();
      rec.setField("MARKER", Integer.valueOf(1));
      rec.setField("FLD1", "Fred");
      f.write(rec);
      rec = rf.getNewRecord();
      rec.setField("MARKER", Integer.valueOf(2));
      rec.setField("FLD1", "Fred ");
      f.write(rec);
      rec = rf.getNewRecord();
      rec.setField("MARKER", Integer.valueOf(3));
      rec.setField("FLD1", "Fred      ");
      f.write(rec);
      f.close();
      
      f = new KeyedFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/TEST7036.FILE/TEST7036.MBR");
      AS400FileRecordDescription frd = new AS400FileRecordDescription(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/TEST7036.FILE/TEST7036.MBR");
      f.setRecordFormat(frd.retrieveRecordFormat()[0]);
      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      rec = f.read(new Object[] { "Fred" });
      rec = f.read(new Object[] { "Fred" });
      if (rec != null)
      {
        Integer i = (Integer)rec.getField("MARKER");
        if (i.intValue() != 1)
        {
          failed("Wrong record returned: "+i.intValue()+" != 1");
        }
        else
        {
          succeeded();
        }
      }
      else
      {
        failed("Null record returned.");
      }
      f.close();
      f.delete();
      deleteLibrary(cc, testLib_); 
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}
