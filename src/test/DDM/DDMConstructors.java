///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMConstructors.java
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
import com.ibm.as400.access.KeyedFile;
import com.ibm.as400.access.SequentialFile;

import test.Testcase;

/**
 *Testcase DDMConstructors.  Verify valid and invalid usages of the constructors
 *for KeyedFile and SequentialFile.  The ability to specify special values for
 *the library and member is tested under the methods to which they apply.
 *E.g. we would verify the ability to open a file which had %LIBL% specified
 *on the constructor in the DDMOpenClose testcase.
**/
public class DDMConstructors extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMConstructors";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMConstructors(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMConstructors", 14,
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

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    if ((allVariations || variationsToRun_.contains("9")) &&
        runMode_ != ATTENDED)
    {
      setVariation(9);
      Var009();
    }

    if ((allVariations || variationsToRun_.contains("10")) &&
        runMode_ != ATTENDED)
    {
      setVariation(10);
      Var010();
    }

    if ((allVariations || variationsToRun_.contains("11")) &&
        runMode_ != ATTENDED)
    {
      setVariation(11);
      Var011();
    }

    if ((allVariations || variationsToRun_.contains("12")) &&
        runMode_ != ATTENDED)
    {
      setVariation(12);
      Var012();
    }

    if ((allVariations || variationsToRun_.contains("13")) &&
        runMode_ != ATTENDED)
    {
      setVariation(13);
      Var013();
    }

    if ((allVariations || variationsToRun_.contains("14")) &&
        runMode_ != ATTENDED)
    {
      setVariation(14);
      Var014();
    }
  }

  /**
   *Verify SequentialFile(AS400, String) constructor.
   *<ul compact>
   *<li>Specifying no member: /qsys.lib/" + testLib_ + ".lib/file2.file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns: /qsys.lib/" + testLib_ + ".lib/file2.file
   *<li>getFileName() returns: FILE2
   *<li>getMemberName() returns: *FIRST
   *<li>getSystem() returns: the AS400 object that was passed in
   *</ul>
  **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      String fileName = "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE";
      SequentialFile f1 = new SequentialFile(systemObject_, fileName);
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!(f1.getPath().equals(fileName)))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!(f1.getFileName().equals("FILE2")))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!(f1.getMemberName().equals("*FIRST")))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != systemObject_)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      f1.close(); 
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception");
      output_.println(e.getMessage());
      e.printStackTrace();
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

  /**
   *Verify SequentialFile(AS400, String) constructor.
   *<ul compact>
   *<li>Specifying a member: /qsys.lib/file2.file/mbr.mbr
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns: /qsys.lib/file2.file/mbr.mbr
   *<li>getFileName() returns: FILE2
   *<li>getMemberName() returns: MBR
   *<li>getSystem() returns: the AS400 object that was passed in
   *</ul>
  **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      String fileName = "/QSYS.LIB/FILE2.FILE/MBR.MBR";
      SequentialFile f1 = new SequentialFile(systemObject_, fileName);
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!(f1.getPath().equals(fileName)))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!(f1.getFileName().equals("FILE2")))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!(f1.getMemberName().equals("MBR")))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != systemObject_)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      f1.close(); 
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception");
      output_.println(e.getMessage());
      e.printStackTrace();
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

  /**
   *Verify invalid use of SequentialFile(AS400, String).
   *<ul compact>
   *<li>Specifying null for the AS400 parameter.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() = "system"
   *</ul>
  **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      SequentialFile f1 = new SequentialFile(null, "/QSYS.LIB/FILE2.FILE/FILE2.MBR");
      f1.close(); 
      failMsg.append("\nNo exception when null passed for AS400 parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of SequentialFile(AS400, String).
   *<ul compact>
   *<li>Specifying null for the String parameter.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() = "name"
   *</ul>
  **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_, null);
      f1.close(); 
      failMsg.append("\nNo exception when null passed for name parm.\n"+f1);
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of SequentialFile(AS400, String).
   *<ul compact>
   *<li>Specifying invalid pathname for the String parameter. Member length
   *too long.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>IllegalPathNameException with getMessage() =
   *"Length of the member name is not valid."
   *</ul>
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    String fileName = "/QSYS.LIB/FILE.FILE/TOOLONGMEMBER.MBR";
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_, fileName);
      f1.close(); 
      failMsg.append("\nNo exception when invalid IFS name passed for name parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "IllegalPathNameException", fileName + ": Length of the member name is not valid."))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of SequentialFile(AS400, String).
   *<ul compact>
   *<li>Specifying invalid pathname for the String parameter. Invlid object
   *type.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>IllegalPathNameException with getMessage() =
   *"Object type is not valid"
   *</ul>
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    String fileName = "/QSYS.LIB/FILE.DTAQ";
    try
    {
      SequentialFile f1 = new SequentialFile(systemObject_, fileName);
      f1.close(); 
      failMsg.append("\nNo exception when invalid IFS object type passed for name parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "IllegalPathNameException", fileName + ": Object type is not valid."))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify null constructor SequentialFile().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getSystem() returns null.
   *<li>getPath() returns empty string.
   *<li>getFileName() returns empty string.
   *<li>getMemberName() returns empty string.
   *<li>delete() throws ExtendedIllegalStateException.
   *</ul>
  **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      SequentialFile f1 = new SequentialFile();
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!f1.getPath().equals(""))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!f1.getFileName().equals(""))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!f1.getMemberName().equals(""))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != null)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      try
      {
        f1.delete();
        failMsg.append("\nNo exception attempting delete\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException"))
        {
          failMsg.append("\nWrong exception/text\n");
          output_.println(e.getMessage());
          e.printStackTrace();
        }
      }
      f1.close(); 
    }
    catch(Exception e)
    {
        failMsg.append("\nUnexpected exception\n");
        e.printStackTrace();
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

  /**
   *Verify KeyedFile(AS400, String) constructor.
   *<ul compact>
   *<li>Specifying no member: /qsys.lib/" + testLib_ + ".lib/file2.file
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns: /qsys.lib/" + testLib_ + ".lib/file2.file
   *<li>getFileName() returns: FILE2
   *<li>getMemberName() returns: *FIRST
   *<li>getSystem() returns: the AS400 object that was passed in
   *</ul>
  **/
  public void Var008()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      String fileName = "/QSYS.LIB/" + testLib_ + ".LIB/FILE2.FILE";
      KeyedFile f1 = new KeyedFile(systemObject_, fileName);
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!(f1.getPath().equals(fileName)))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!(f1.getFileName().equals("FILE2")))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!(f1.getMemberName().equals("*FIRST")))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != systemObject_)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      f1.close(); 
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception");
      output_.println(e.getMessage());
      e.printStackTrace();
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

  /**
   *Verify KeyedFile(AS400, String) constructor.
   *<ul compact>
   *<li>Specifying a member: /qsys.lib/file2.file/mbr.mbr
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>getPath() returns: /qsys.lib/file2.file/mbr.mbr
   *<li>getFileName() returns: FILE2
   *<li>getMemberName() returns: MBR
   *<li>getSystem() returns: the AS400 object that was passed in
   *</ul>
  **/
  public void Var009()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      String fileName = "/QSYS.LIB/FILE2.FILE/MBR.MBR";
      KeyedFile f1 = new KeyedFile(systemObject_, fileName);
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!(f1.getPath().equals(fileName)))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!(f1.getFileName().equals("FILE2")))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!(f1.getMemberName().equals("MBR")))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != systemObject_)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      f1.close(); 
    }
    catch(Exception e)
    {
      failMsg.append("Unexpected exception");
      output_.println(e.getMessage());
      e.printStackTrace();
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

  /**
   *Verify invalid use of KeyedFile(AS400, String).
   *<ul compact>
   *<li>Specifying null for the AS400 parameter.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() = "system"
   *</ul>
  **/
  public void Var010()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      KeyedFile f1 = new KeyedFile(null, "/QSYS.LIB/FILE2.FILE/FILE2.MBR");
      f1.close(); 
      failMsg.append("\nNo exception when null passed for AS400 parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "system"))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of KeyedFile(AS400, String).
   *<ul compact>
   *<li>Specifying null for the String parameter.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException with getMessage() = "name"
   *</ul>
  **/
  public void Var011()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_, null);
      f1.close(); 
      failMsg.append("\nNo exception when null passed for name parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "NullPointerException", "name"))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of KeyedFile(AS400, String).
   *<ul compact>
   *<li>Specifying invalid pathname for the String parameter. Member length
   *too long.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>IllegalPathNameException with getMessage() =
   *"Length of the member name is not valid."
   *</ul>
  **/
  public void Var012()
  {
    StringBuffer failMsg = new StringBuffer();
    String fileName = "/QSYS.LIB/FILE.FILE/TOOLONGMEMBER.MBR";
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_, fileName);
      f1.close(); 
      failMsg.append("\nNo exception when invalid IFS name passed for name parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "IllegalPathNameException", fileName + ": Length of the member name is not valid."))
      {
        failMsg.append("\nWrong exception/text\n");
        e.printStackTrace();
      }
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

  /**
   *Verify invalid use of KeyedFile(AS400, String).
   *<ul compact>
   *<li>Specifying invalid pathname for the String parameter. Invalid object
   *type.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>IllegalPathNameException with getMessage() =
   *"Object type is not valid"
   *</ul>
  **/
  public void Var013()
  {
    StringBuffer failMsg = new StringBuffer();
    String fileName = "/QSYS.LIB/FILE.DTAQ";
    try
    {
      KeyedFile f1 = new KeyedFile(systemObject_, fileName);
      f1.close();
      failMsg.append("\nNo exception when invalid IFS object type passed for name parm.\n");
    }
    catch(Exception e)
    {
      if (!exceptionIs(e, "IllegalPathNameException", fileName + ": Object type is not valid."))
      {
        failMsg.append("\nWrong exception/text\n");
        output_.println(e.getMessage());
        e.printStackTrace();
      }
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

  /**
   *Verify null constructor, KeyedFile().
   *<br>
   *Expected results:
   *<ul compact>
   *<li>getSystem() returns null.
   *<li>getPath() returns an empty string.
   *<li>getFileName() returns an empty string.
   *<li>getMemberName() returns an empty string.
   *<li>delete() throws ExtendedIllegalStateException.
   *</ul>
  **/
  public void Var014()
  {
    StringBuffer failMsg = new StringBuffer();
    try
    {
      KeyedFile f1 = new KeyedFile();
      AS400 a = f1.getSystem();

      // Verify the name and system
      if (!f1.getPath().equals(""))
      {
        failMsg.append("\nWrong pathname returned.\n");
      }
      if (!f1.getFileName().equals(""))
      {
        failMsg.append("\nWrong file name returned.\n");
      }
      if (!f1.getMemberName().equals(""))
      {
        failMsg.append("\nWrong member name returned.\n");
      }
      if (a != null)
      {
        failMsg.append("\nWrong AS400 object returned.\n");
      }
      try
      {
        f1.delete();
        failMsg.append("\nNo exception attempting delete\n");
      }
      catch(Exception e)
      {
        if (!exceptionIs(e, "ExtendedIllegalStateException"))
        {
          failMsg.append("\nWrong exception/text\n");
          e.printStackTrace();
        }
      }
      f1.close(); 
    }
    catch(Exception e)
    {
        failMsg.append("\nUnexpected exception\n");
        e.printStackTrace();
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
