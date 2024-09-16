///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSSysvalTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;

import test.JTOpenTestEnvironment;
import test.NLSTest;
import test.Testcase;

import com.ibm.as400.access.ExtendedIllegalArgumentException;

/**
 *Testcase NLSSysvalTestcase.  This test class verifies the use of DBCS Strings
 *in selected SystemValue testcase variations.
**/
public class NLSSysvalTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSSysvalTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  AS400 PwrSys_ = NLSTest.PwrSys;

  Vector strings = null;
  Vector arrays = null;

  private String operatingSystem_;
  private boolean DOS_;

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = getResource("IFS_DBCS_STRING50");
  String dbcs_long = null;

  /**
  Constructor.
  **/
  public NLSSysvalTestcase(AS400            systemObject,
                              Vector           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream
                              )

  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSSysvalTestcase", 11,
          variationsToRun, runMode, fileOutputStream);
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    if (PwrSys_ == null)
    {
      throw new Exception("Power user not specified");
    }
    strings = new Vector();
    arrays = new Vector();
    dbcs_long = dbcs_string50+dbcs_string50+dbcs_string50+dbcs_string50;
    dbcs_long = dbcs_long+dbcs_long+dbcs_long+dbcs_long+dbcs_long;
    dbcs_long = dbcs_long+dbcs_long+dbcs_long+dbcs_long; // 4000 chars

    try
    {
        SystemValueList list = new SystemValueList(PwrSys_);
        Vector values = list.getGroup(SystemValueList.GROUP_ALL);
        for (int i=0; i<values.size(); ++i)
        {
          SystemValue sv = (SystemValue)values.elementAt(i);
          if (sv.getType() == SystemValueList.TYPE_STRING && !sv.isReadOnly())
          {
            strings.addElement(sv);
          }
          else if (sv.getType() == SystemValueList.TYPE_ARRAY && !sv.isReadOnly())
          {
            arrays.addElement(sv);
          }
        }
    }
    catch(Exception e)
    {
      output_.println("setup failed");
      throw e;
    }

    // Determine operating system we're running under
    operatingSystem_ = System.getProperty("os.name");
    if (JTOpenTestEnvironment.isWindows)
    {
      DOS_ = true;
    }
    else
    {
      DOS_ = false;
    }

    output_.println("Running under: " + operatingSystem_);
    output_.println("DOS-based file structure: " + DOS_);
    output_.println("Executing applet: " + isApplet_);
  }

  /**
    Cleanup Test library.
   @exception  Exception  If an exception occurs.
  **/
  protected void cleanup()
    throws Exception
  {
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.COMMAND);
    }
    catch(Exception e)
    {
      output_.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

    try
    {
      setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
      return;
    }

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

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.FILE);
      cleanup();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }
  }

    /**
    Tests a system value with DBCS characters by setting its value
    with a String of length getSize() and getting the value back.
    **/
    void testNLS(String name)
    {
      SystemValue sv = null;
      Object original = null;
      try
      {
        sv = new SystemValue(PwrSys_, name);
        original = sv.getValue();
        String toSet = dbcs_long.substring(0, sv.getSize());
        sv.setValue(toSet);
        sv.clear();
        String toGet = (String)sv.getValue();
        if (!toSet.toUpperCase().equals(toGet.toUpperCase()))
        {
          failed("Values do not match: '"+toSet+"' != '"+toGet+"'");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      try
      {
        sv.setValue(original);
      }
      catch(Exception e)
      {
        output_.println("ERROR: "+e.getMessage()+"\n  Unable to reset "+sv.getName()+" to '"+original.toString()+"'");
      }
    }


    /**
     * Tests setValue() and getValue() for all System Values of TYPE_STRING.
     **/
    void testStrings()
    {
      boolean allFailed = false;
      output_.println("Testing "+strings.size()+" system values of TYPE_STRING.");
      for (int i=0; i<strings.size(); ++i)
      {
        boolean failed = false;
        SystemValue sv = (SystemValue)strings.elementAt(i);
        Object toSet = dbcs_long.substring(0, sv.getSize());
        Object toGet = null;
        Object original = null;
        try
        {
          original = sv.getValue();
          sv.setValue(toSet);
          sv.clear();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure setting "+sv.getName()+" to value '"+toSet.toString()+"'");
          failed = true;
        }
        try
        {
          toGet = sv.getValue();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure getting value for "+sv.getName());
          failed = true;
        }
        if (!((String)toSet).toUpperCase().equals(((String)toGet).toUpperCase()))
        {
          output_.println("Returned data not equal to set data for "+sv.getName()+":");
          output_.println("   '"+toGet.toString()+"' != '"+toSet.toString()+"'");
          failed = true;
        }
        try
        {
          sv.setValue(original);
          sv.clear();
          toGet = sv.getValue();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure on reset of "+sv.getName());
          failed = true;
        }
        if (!((String)toGet).toUpperCase().equals(((String)original).toUpperCase()))
        {
          output_.println("Returned data not equal to original data for "+sv.getName());
          output_.println("   '"+toGet.toString()+"' != '"+original.toString()+"'");
          failed = true;
        }
        if (!failed)
          output_.println("Successful test of "+sv.getName());
        else
          allFailed = true;
      }
      if (allFailed)
        failed();
      else
        succeeded();
    }

    /**
     * Tests setValue() and getValue() for all System Values of TYPE_ARRAY.
     **/
    void testArrays()
    {
      boolean allFailed = false;
      output_.println("Testing "+arrays.size()+" system values of TYPE_ARRAY.");
      for (int i=0; i<arrays.size(); ++i)
      {
        try
        {
        boolean failed = false;
        SystemValue sv = (SystemValue)arrays.elementAt(i);
        Object[] toSet = new String[] { dbcs_string5, dbcs_string5 }; // 2 items, 5 chars
        Object[] toGet = null;
        Object[] original = null;
        try
        {
          original = (Object[])sv.getValue();
          sv.setValue(toSet);
          sv.clear();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure setting "+sv.getName()+" to value:");
          for (int j=0; j<toSet.length; ++j)
          {
            output_.println("  '"+toSet[j].toString()+"'");
          }
          failed = true;
        }
        try
        {
          toGet = (Object[])sv.getValue();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure getting value for "+sv.getName());
          failed = true;
        }
        for (int j=0; j<toSet.length; ++j)
        {
          if (!((String)toSet[j]).toUpperCase().equals(((String)toGet[j]).toUpperCase()))
          {
            output_.println("Returned data not equal to set data for "+sv.getName()+":");
            output_.println("   '"+toGet[j].toString()+"' != '"+toSet[j].toString()+"'");
            failed = true;
          }
        }
        try
        {
          sv.setValue(original);
          sv.clear();
          toGet = (Object[])sv.getValue();
        }
        catch(Exception e)
        {
          output_.println(e.getMessage());
          output_.println("  Failure on reset of "+sv.getName());
          failed = true;
        }
        for (int j=0; j<toGet.length; ++j)
        {
          if (!((String)toGet[j]).toUpperCase().equals(((String)original[j]).toUpperCase()))
          {
            output_.println("Returned data not equal to original data for "+sv.getName());
            output_.println("   '"+toGet[j].toString()+"' != '"+original[j].toString()+"'");
            failed = true;
          }
        }
        if (!failed)
          output_.println("Successful test of "+sv.getName());
        else
          allFailed = true;
        }
        catch(Exception x)
        {
          x.printStackTrace(output_);
        }
      }
      if (allFailed)
        failed();
      else
        succeeded();
    }

    /**
     * Tests DFTCNNLST with DBCS characters.
     **/
    public void Var001()
    {
      testNLS("DFTCNNLST");
    }


    /**
     * Tests DFTMODE with DBCS characters.
     **/
    public void Var002()
    {
      testNLS("DFTMODE");
    }


    /**
     * Tests LCLLOCNAME with DBCS characters.
     **/
    public void Var003()
    {
      testNLS("LCLLOCNAME");
    }


    /**
     * Tests NWSDOMAIN with DBCS characters.
     **/
    public void Var004()
    {
      testNLS("NWSDOMAIN");
    }


    /**
     * Tests QCURSYM with DBCS characters.
     **/
    public void Var005()
    {
      testNLS("QCURSYM");
    }


    /**
     * Tests QPRTDEV with DBCS characters.
     **/
    public void Var006()
    {
      testNLS("QPRTDEV");
    }


    /**
     * Tests QPRTTXT with DBCS characters.
     **/
    public void Var007()
    {
      testNLS("QPRTTXT");
    }


    /**
     * Tests QPWDLMTCHR with DBCS characters.
     **/
    public void Var008()
    {
      testNLS("QPWDLMTCHR");
    }


// ***** TYPE-ARRAY *****

    /**
     * Tests ALRBCKFP with DBCS characters.
     **/
    public void Var009()
    {
      String failMsg = "";
      SystemValue sv = null;
      String[] original = null;
      try
      {
        sv = new SystemValue(PwrSys_, "ALRBCKFP");
        original = (String[])sv.getValue();
        String[] toSet = { dbcs_long.substring(0,8), dbcs_long.substring(0,8) };
        sv.setValue(toSet);
        sv.clear();
        String[] toGet = (String[])sv.getValue();
        if (toGet.length == toSet.length)
        {
          for (int i=0; i<toGet.length; ++i)
          {
            if (!toSet[i].toUpperCase().equals(toGet[i].toUpperCase()))
            {
              failMsg += "\n  ["+i+"] Values do not match: '"+toSet[i]+"' != '"+toGet[i]+"'";
            }
          }
        }
        else
        {
          failMsg += "Lengths not equal: "+toGet.length+" != "+toSet.length;
        }
        if (failMsg.length() > 0)
          failed(failMsg);
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      try
      {
        sv.setValue(original);
      }
      catch(Exception e)
      {
        output_.println("ERROR: "+e.getMessage());
        for (int i=0; i<original.length; ++i)
        {
          output_.println("\n  ["+i+"] Unable to reset "+sv.getName()+" to '"+original[i]+"'");
        }
      }
    }


    /**
     * Tests ALRRQSFP with DBCS characters.
     **/
    public void Var010()
    {
      String failMsg = "";
      SystemValue sv = null;
      String[] original = null;
      try
      {
        sv = new SystemValue(PwrSys_, "ALRRQSFP");
        original = (String[])sv.getValue();
        String[] toSet = { dbcs_long.substring(0,8), dbcs_long.substring(0,8) };
        sv.setValue(toSet);
        sv.clear();
        String[] toGet = (String[])sv.getValue();
        if (toGet.length == toSet.length)
        {
          for (int i=0; i<toGet.length; ++i)
          {
            if (!toSet[i].toUpperCase().equals(toGet[i].toUpperCase()))
            {
              failMsg += "\n  ["+i+"] Values do not match: '"+toSet[i]+"' != '"+toGet[i]+"'";
            }
          }
        }
        else
        {
          failMsg += "Lengths not equal: "+toGet.length+" != "+toSet.length;
        }
        if (failMsg.length() > 0)
          failed(failMsg);
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      try
      {
        sv.setValue(original);
      }
      catch(Exception e)
      {
        output_.println("ERROR: "+e.getMessage());
        for (int i=0; i<original.length; ++i)
        {
          output_.println("\n  ["+i+"] Unable to reset "+sv.getName()+" to '"+original[i]+"'");
        }
      }
    }


    /**
     * Tests QIGCCDEFNT with DBCS characters.
     **/
    public void Var011()
    {
      String failMsg = "";
      SystemValue sv = null;
      String[] original = null;
      try
      {
        sv = new SystemValue(PwrSys_, "QIGCCDEFNT");
        original = (String[])sv.getValue();
        String[] toSet = { dbcs_long.substring(0,8), dbcs_long.substring(0,8) };
        sv.setValue(toSet);
        sv.clear();
        String[] toGet = (String[])sv.getValue();
        if (toGet.length == toSet.length)
        {
          for (int i=0; i<toGet.length; ++i)
          {
            if (!toSet[i].toUpperCase().equals(toGet[i].toUpperCase()))
            {
              failMsg += "\n  ["+i+"] Values do not match: '"+toSet[i]+"' != '"+toGet[i]+"'";
            }
          }
        }
        else
        {
          failMsg += "Lengths not equal: "+toGet.length+" != "+toSet.length;
        }
        if (failMsg.length() > 0)
          failed(failMsg);
        else
          succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
      try
      {
        sv.setValue(original);
      }
      catch(Exception e)
      {
        output_.println("ERROR: "+e.getMessage());
        for (int i=0; i<original.length; ++i)
        {
          output_.println("\n  ["+i+"] Unable to reset "+sv.getName()+" to '"+original[i]+"'");
        }
      }
    }




}



