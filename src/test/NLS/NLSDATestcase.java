///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSDATestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.*;

import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DataArea;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.JTOpenTestEnvironment;
import test.Testcase;

import java.math.BigDecimal;

/**
 *Testcase NLSDATestcase.  This test class verifies the use of DBCS Strings
 *in selected DataArea testcase variations.
**/
public class NLSDATestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSDATestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  private String userSpacePathName_ = "/QSYS.LIB/DATEST.LIB/DANLSTEST.DTAARA";
  private String operatingSystem_;
  private boolean DOS_;

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50  = getResource("IFS_DBCS_STRING50");
  String dbcs_string50d = getResource("IFS_DBCS_STRING50") + " .. " + getResource("IFS_DBCS_STRING50");

  /**
  Constructor.
  **/
  public NLSDATestcase(AS400            systemObject,
                              Vector           variationsToRun,
                              int              runMode,
                              FileOutputStream fileOutputStream
                              )

  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSDATestcase", 46,
          variationsToRun, runMode, fileOutputStream);
  }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {

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

    try
    {
        CommandCall cmd = new CommandCall(systemObject_);
        if(cmd.run("CRTLIB LIB(DATEST)") == false)
        {
          AS400Message[] messageList = cmd.getMessageList();
          throw new Exception("Setup - CRTLIB DATEST - failed.  " + messageList[0].toString());
        }
    }
    catch(Exception e)
    {
        System.out.println("Setup failed.  Unexpected exception occurred." + e);
        throw e;
    }
  }

  /**
    Cleanup Test library.
   @exception  Exception  If an exception occurs.
  **/
  protected void cleanup()
    throws Exception
  {
     try
     {
        CommandCall cmd = new CommandCall(systemObject_);
        deleteLibrary(cmd, "DATEST");
     }
     catch(Exception e)
     {
        System.out.println("Cleanup failed. " + e);
        throw e;
     }
  }

  private static String getTextDescription(ObjectDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
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

    if ((allVariations || variationsToRun_.contains("15")) &&
        runMode_ != ATTENDED)
    {
      setVariation(15);
      Var015();
    }

    if ((allVariations || variationsToRun_.contains("16")) &&
        runMode_ != ATTENDED)
    {
      setVariation(16);
      Var016();
    }

    if ((allVariations || variationsToRun_.contains("17")) &&
        runMode_ != ATTENDED)
    {
      setVariation(17);
      Var017();
    }

    if ((allVariations || variationsToRun_.contains("18")) &&
        runMode_ != ATTENDED)
    {
      setVariation(18);
      Var018();
    }

    if ((allVariations || variationsToRun_.contains("19")) &&
        runMode_ != ATTENDED)
    {
      setVariation(19);
      Var019();
    }

    if ((allVariations || variationsToRun_.contains("20")) &&
        runMode_ != ATTENDED)
    {
      setVariation(20);
      Var020();
    }

    if ((allVariations || variationsToRun_.contains("21")) &&
        runMode_ != ATTENDED)
    {
      setVariation(21);
      Var021();
    }

    if ((allVariations || variationsToRun_.contains("22")) &&
        runMode_ != ATTENDED)
    {
      setVariation(22);
      Var022();
    }

    if ((allVariations || variationsToRun_.contains("23")) &&
        runMode_ != ATTENDED)
    {
      setVariation(23);
      Var023();
    }

    if ((allVariations || variationsToRun_.contains("24")) &&
        runMode_ != ATTENDED)
    {
      setVariation(24);
      Var024();
    }

    if ((allVariations || variationsToRun_.contains("25")) &&
        runMode_ != ATTENDED)
    {
      setVariation(25);
      Var025();
    }

    if ((allVariations || variationsToRun_.contains("26")) &&
        runMode_ != ATTENDED)
    {
      setVariation(26);
      Var026();
    }

    if ((allVariations || variationsToRun_.contains("27")) &&
        runMode_ != ATTENDED)
    {
      setVariation(27);
      Var027();
    }

    if ((allVariations || variationsToRun_.contains("28")) &&
        runMode_ != ATTENDED)
    {
      setVariation(28);
      Var028();
    }

    if ((allVariations || variationsToRun_.contains("29")) &&
        runMode_ != ATTENDED)
    {
      setVariation(29);
      Var029();
    }

    if ((allVariations || variationsToRun_.contains("30")) &&
        runMode_ != ATTENDED)
    {
      setVariation(30);
      Var030();
    }

    if ((allVariations || variationsToRun_.contains("31")) &&
        runMode_ != ATTENDED)
    {
      setVariation(31);
      Var031();
    }

    if ((allVariations || variationsToRun_.contains("32")) &&
        runMode_ != ATTENDED)
    {
      setVariation(32);
      Var032();
    }

    if ((allVariations || variationsToRun_.contains("33")) &&
        runMode_ != ATTENDED)
    {
      setVariation(33);
      Var033();
    }

    if ((allVariations || variationsToRun_.contains("34")) &&
        runMode_ != ATTENDED)
    {
      setVariation(34);
      Var034();
    }

    if ((allVariations || variationsToRun_.contains("35")) &&
        runMode_ != ATTENDED)
    {
      setVariation(35);
      Var035();
    }

    if ((allVariations || variationsToRun_.contains("36")) &&
        runMode_ != ATTENDED)
    {
      setVariation(36);
      Var036();
    }

    if ((allVariations || variationsToRun_.contains("37")) &&
        runMode_ != ATTENDED)
    {
      setVariation(37);
      Var037();
    }

    if ((allVariations || variationsToRun_.contains("38")) &&
        runMode_ != ATTENDED)
    {
      setVariation(38);
      Var038();
    }

    if ((allVariations || variationsToRun_.contains("39")) &&
        runMode_ != ATTENDED)
    {
      setVariation(39);
      Var039();
    }

    if ((allVariations || variationsToRun_.contains("40")) &&
        runMode_ != ATTENDED)
    {
      setVariation(40);
      Var040();
    }

    if ((allVariations || variationsToRun_.contains("41")) &&
        runMode_ != ATTENDED)
    {
      setVariation(41);
      Var041();
    }

    if ((allVariations || variationsToRun_.contains("42")) &&
        runMode_ != ATTENDED)
    {
      setVariation(42);
      Var042();
    }

    if ((allVariations || variationsToRun_.contains("43")) &&
        runMode_ != ATTENDED)
    {
      setVariation(43);
      Var043();
    }

    if ((allVariations || variationsToRun_.contains("44")) &&
        runMode_ != ATTENDED)
    {
      setVariation(44);
      Var044();
    }

    if ((allVariations || variationsToRun_.contains("45")) &&
        runMode_ != ATTENDED)
    {
      setVariation(45);
      Var045();
    }

    if ((allVariations || variationsToRun_.contains("46")) &&
        runMode_ != ATTENDED)
    {
      setVariation(46);
      Var046();
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
     * CharacterDataArea::create(int, String, String, String)
     * passing a value which is too long for the description argument.
     * An ExtendedIllegalArgumentException should be thrown.
     * <i>Taken from:</i> DACreateTestcase::Var023
     **/
    public void Var001()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        try
        {
          da.create(32, " ", dbcs_string50d, "*USE");
          failed("No exception.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
            succeeded();
          else
            failed(e, "Wrong exception info.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * DecimalDataArea::create(int, int, BigDecimal, String, String)
     * passing a value which is too long for the description argument.
     * An ExtendedIllegalArgumentException should be thrown.
     * <i>Taken from:</i> DACreateTestcase::Var024
     **/
    public void Var002()
    {
      try
      {
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        try
        {
          da.create(10, 5, new BigDecimal("0.0"), dbcs_string50d, "*USE");
          failed("No exception.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
            succeeded();
          else
            failed(e, "Wrong exception info.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * LogicalDataArea::create(boolean, String, String)
     * passing a value which is too long for the description argument.
     * An ExtendedIllegalArgumentException should be thrown.
     * <i>Taken from:</i> DACreateTestcase::Var025
     **/
    public void Var003()
    {
      try
      {
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        try
        {
          da.create(true, dbcs_string50d, "*USE");
          failed("No exception.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "textDescription", ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
            succeeded();
          else
            failed(e, "Wrong exception info.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Do a successful CharacterDataArea::create(int, String, String, String).
     * Verify the data area is created correctly and contains the specified
     * values for length, initial value, description, and authority.
     * <i>Taken from:</i> DACreateTestcase::Var054
     **/
    public void Var004()
    {
      try
      {
        boolean failed = false;  // Keeps track of failure in multi-part tests.
        String msg = "";      // Keeps track of reason for failure in multi-part tests.
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        da2.create(10, dbcs_string10, dbcs_string50, "*ALL");
        try
        {
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 10)
          {
            failed = true;
            msg += "Wrong length.\n";
          }
          if (!da.read().trim().equals(dbcs_string10))
          {
            failed = true;
            msg += "Wrong initial value.\n";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(dbcs_string50))
          {
            failed = true;
            msg += "Wrong text description.\n";
          }

          if (failed)
          {
            failed(msg);
          }
          else
          {
            succeeded();
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Do a successful DecimalDataArea::create(int, int, BigDecimal, String, String).
     * Verify the data area is created correctly and contains the specified
     * values for length, decimal positions, initial value, description, and authority.
     * <i>Taken from:</i> DACreateTestcase::Var055
     **/
    public void Var005()
    {
      try
      {
        boolean failed = false;  // Keeps track of failure in multi-part tests.
        String msg = "";      // Keeps track of reason for failure in multi-part tests.
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        da2.create(12, 6, new BigDecimal("654321.123456"), dbcs_string50, "*ALL");
        try
        {
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 12)
          {
            failed = true;
            msg += "Wrong length.\n";
          }
          if (da.getDecimalPositions() != 6)
          {
            failed = true;
            msg += "Wrong decimal positions.\n";
          }
          if (!da.read().equals(new BigDecimal("654321.123456")))
          {
            failed = true;
            msg += "Wrong initial value.\n";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(dbcs_string50))
          {
            failed = true;
            msg += "Wrong text description.\n";
          }

          if (failed)
          {
            failed(msg);
          }
          else
          {
            succeeded();
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Do a successful LogicalDataArea::create(boolean, String, String).
     * Verify the data area is created correctly and contains the specified
     * values for initial value, description, and authority.
     * <i>Taken from:</i> DACreateTestcase::Var056
     **/
    public void Var006()
    {
      try
      {
        boolean failed = false;  // Keeps track of failure in multi-part tests.
        String msg = "";      // Keeps track of reason for failure in multi-part tests.
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        da2.create(true, dbcs_string50, "*ALL");
        try
        {
          // Verify create.
          // Note that there is no way to easily verify the authority.
          if (da.getLength() != 1)
          {
            failed = true;
            msg += "Wrong length.\n";
          }
          if (da.read() != true)
          {
            failed = true;
            msg += "Wrong initial value.\n";
          }
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (!getTextDescription(obj).trim().equals(dbcs_string50))
          {
            failed = true;
            msg += "Wrong text description.\n";
          }

          if (failed)
          {
            failed(msg);
          }
          else
          {
            succeeded();
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify description values with boundary lengths using
     * CharacterDataArea::create(int, String, String, String). (length of 50)
     * <i>Taken from:</i> DACreateTestcase::Var078
     **/
    public void Var007()
    {
      try
      {
        String desc = dbcs_string50;
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        // true
        da2.create(80, "initial value", desc, "*USE");
        try
        {
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (getTextDescription(obj).equals(desc))
          {
            succeeded();
          }
          else
          {
            failed("Wrong description (length 50).");
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify description values with boundary lengths using
     * DecimalDataArea::create(int, int, BigDecimal, String, String). (length of 50)
     * <i>Taken from:</i> DACreateTestcase::Var079
     **/
    public void Var008()
    {
      try
      {
        String desc = dbcs_string50;
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        // true
        da2.create(12, 6, new BigDecimal("0.0"), desc, "*USE");
        try
        {
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (getTextDescription(obj).equals(desc))
          {
            succeeded();
          }
          else
          {
            failed("Wrong description (length 50).");
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify description values with boundary lengths using
     * LogicalDataArea::create(boolean, String, String). (length of 50)
     * <i>Taken from:</i> DACreateTestcase::Var080
     **/
    public void Var009()
    {
      try
      {
        String desc = dbcs_string50;
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
        // true
        da2.create(true, desc, "*USE");
        try
        {
          ObjectDescription obj = new ObjectDescription(systemObject_, "/QSYS.LIB/QTEMP.LIB/CRTTEST.DTAARA");
          if (getTextDescription(obj).equals(desc))
          {
            succeeded();
          }
          else
          {
            failed("Wrong description (length 50).");
          }
        }
        finally
        {
          da2.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Successful construction of a character data area using ctor with parms.
     * <i>Taken from:</i> DACtorTestcase::Var014
     **/
    public void Var010()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/"+dbcs_string10+".DTAARA");
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Successful construction of a decimal data area using ctor with parms.
     * <i>Taken from:</i> DACtorTestcase::Var015
     **/
    public void Var011()
    {
      try
      {
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/"+dbcs_string10+".DTAARA");
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Successful construction of a logical data area using ctor with parms.
     * <i>Taken from:</i> DACtorTestcase::Var017
     **/
    public void Var012()
    {
      try
      {
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/DATEST.LIB/"+dbcs_string10+".DTAARA");
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify usage of CharacterDataArea::getName().
     * Try to get the name of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var011
     **/
    public void Var013()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of CharacterDataArea::getName().
     * Try to get the name of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var012
     **/
    public void Var014()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea();
        da.setSystem(systemObject_);
        da.setPath("/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of CharacterDataArea::getPath().
     * Try to get the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var014
     **/
    public void Var015()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        CharacterDataArea da = new CharacterDataArea(systemObject_, path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of CharacterDataArea::getPath().
     * Try to get the path of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var015
     **/
    public void Var016()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        CharacterDataArea da = new CharacterDataArea();
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of CharacterDataArea::setPath().
     * Try to set the path of a data area which has no path set.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var021
     **/
    public void Var017()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea();
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of CharacterDataArea::setPath().
     * Try to set the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var022
     **/
    public void Var018()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        String path2 = "/QSYS.LIB/DATEST.LIB/"+dbcs_string5+".DTAARA";
        CharacterDataArea da = new CharacterDataArea(systemObject_, path);
        da.setPath(path2);
        if (da.getPath().equals(path2))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::getName().
     * Try to get the name of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var047
     **/
    public void Var019()
    {
      try
      {
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::getName().
     * Try to get the name of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var048
     **/
    public void Var020()
    {
      try
      {
        DecimalDataArea da = new DecimalDataArea();
        da.setSystem(systemObject_);
        da.setPath("/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::getPath().
     * Try to get the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var050
     **/
    public void Var021()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        DecimalDataArea da = new DecimalDataArea(systemObject_, path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::getPath().
     * Try to get the path of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var051
     **/
    public void Var022()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        DecimalDataArea da = new DecimalDataArea();
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::setPath().
     * Try to set the path of a data area which has no path set.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var057
     **/
    public void Var023()
    {
      try
      {
        DecimalDataArea da = new DecimalDataArea();
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of DecimalDataArea::setPath().
     * Try to set the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var058
     **/
    public void Var024()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        String path2 = "/QSYS.LIB/DATEST.LIB/"+dbcs_string5+".DTAARA";
        DecimalDataArea da = new DecimalDataArea(systemObject_, path);
        da.setPath(path2);
        if (da.getPath().equals(path2))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::getName().
     * Try to get the name of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var084
     **/
    public void Var025()
    {
      try
      {
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::getName().
     * Try to get the name of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var085
     **/
    public void Var026()
    {
      try
      {
        LogicalDataArea da = new LogicalDataArea();
        da.setSystem(systemObject_);
        da.setPath("/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA");
        if (da.getName().equals(dbcs_string10.toUpperCase()))
        {
          succeeded();
        }
        else
        {
          failed("Wrong name: "+da.getName());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::getPath().
     * Try to get the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var087
     **/
    public void Var027()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        LogicalDataArea da = new LogicalDataArea(systemObject_, path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::getPath().
     * Try to get the path of a data area when the path was set using setPath().
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var088
     **/
    public void Var028()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        LogicalDataArea da = new LogicalDataArea();
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::setPath().
     * Try to set the path of a data area which has no path set.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var094
     **/
    public void Var029()
    {
      try
      {
        LogicalDataArea da = new LogicalDataArea();
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        da.setPath(path);
        if (da.getPath().equals(path))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify usage of LogicalDataArea::setPath().
     * Try to set the path of a data area when the path was set on the constructor.
     * The method should return the proper data.
     * <i>Taken from:</i> DAGetSetTestcase::Var095
     **/
    public void Var030()
    {
      try
      {
        String path = "/QSYS.LIB/QTEMP.LIB/"+dbcs_string10+".DTAARA";
        String path2 = "/QSYS.LIB/DATEST.LIB/"+dbcs_string5+".DTAARA";
        LogicalDataArea da = new LogicalDataArea(systemObject_, path);
        da.setPath(path2);
        if (da.getPath().equals(path2))
        {
          succeeded();
        }
        else
        {
          failed("Wrong path: "+da.getPath());
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::read().
     * Try to read from a data area created using create(int,String,String,String).
     * The method should return the proper data.
     * <i>Taken from:</i> DAReadTestcase::Var015
     **/
    public void Var031()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String expected = dbcs_string5;
          int daLength = lengthInBytes(expected, systemObject_);
          da.create(daLength, expected, "DAReadTestcase", "*USE");
          String data = da2.read();
          if (expected.equals(data.trim()))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'" + data.length());
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::read(int,int).
     * Try to read from a data area created using create(int,String,String,String).
     * The method should return the proper data.
     * <i>Taken from:</i> DAReadTestcase::Var017
     **/
    public void Var032()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String expected = dbcs_string5;
          int daLength = lengthInBytes(expected, systemObject_);
          da.create(daLength, expected, "DAReadTestcase", "*USE");
          String data = da2.read(0,daLength);
          if (expected.equals(data.trim()))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data read:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var015
     **/
    public void Var033()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        int length = da.getLength();
        try
        {
          da.write(dbcs_string50d,1);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR))
            	  /*                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                  ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var016
     **/
    public void Var034()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create();
        int length = da.getLength();
        try
        {
          da.write(dbcs_string50d);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR))
            	  /*                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
                  ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var017
     **/
    public void Var035()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        da.create(); // Default size of 32 chars.
        try
        {
          da.write(dbcs_string5, 31);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "AS400Exception", "CPF1089", ErrorCompletingRequestException.AS400_ERROR))
        	  /*                if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
              ExtendedIllegalArgumentException.LENGTH_NOT_VALID))*/
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var018
     **/
    public void Var036()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          da.create(); // Default size of 32 chars.
          int length = da2.getLength();
          String expected = dbcs_string10+dbcs_string10;
          da.write(expected);
          int dataLength = lengthInBytes(expected, systemObject_);
          String data = da.read();
          if (expected.equals(data.trim()))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String).
     * Try to write to a data area created using create(int,String,String,String).
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var019
     **/
    public void Var037()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String expected = dbcs_string10;
          int daLength = lengthInBytes(expected, systemObject_);
          da.create(daLength, expected, "DAWriteTestcase", "*USE");
          da2.write(expected);
          String data = da2.read();
          if (expected.equals(data.trim()))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area created using create().
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var020
     **/
    public void Var038()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          da.create(); // Default size of 32 chars.
          int length = da2.getLength();
          String expected = dbcs_string10+dbcs_string10+dbcs_string5;
          // for (int i=expected.length(); i<length; ++i)
          for (int i=lengthInBytes(expected, systemObject_); i<length; i++)
            expected += " ";

          da.write(expected,0);
          String data = da2.read();
          if (data.equals(expected))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int).
     * Try to write to a data area created using create(int,String,String,String).
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var021
     **/
    public void Var039()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String initialValue = dbcs_string10;
          da.create(10, initialValue, "DAWriteTestcase", "*USE");
          String expected = dbcs_string10.substring(0,1)+dbcs_string5+dbcs_string10.substring(6,10);
          da.write(dbcs_string5, 1);
          String data = da2.read(0,10);
          if (expected.equals(data))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of CharacterDataArea::write(String,int) and
     * CharacterDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var022
     **/
    public void Var040()
    {
      try
      {
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/READTEST.DTAARA");
        try
        {
          String initialValue = dbcs_string10;
          da.create(10, initialValue, "DAWriteTestcase", "*USE");
          String expected = dbcs_string10.substring(0,1)+dbcs_string5+dbcs_string10.substring(6,10);
          da.write(dbcs_string5, 1);
          String data = da2.read(0,10);
          if (!expected.equals(data))
          {
            failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          da.write(initialValue);
          expected = dbcs_string5+dbcs_string5;
          da2.write(dbcs_string5+dbcs_string5, 0);
          data = da.read();
          if (!expected.equals(data))
          {
            failed("Incorrect data write(2):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          da2.write(initialValue);
          succeeded();
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
        finally
        {
          da.delete();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var053
     **/
    public void Var041()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int length = da.getLength();
        String toWrite = "";
        for (int i=0; i<205; ++i)
          toWrite += dbcs_string5;
        try
        {
          da.write(toWrite,0);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
              ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            if (exceptionIs(e, "AS400Exception") &&
                e.getMessage().startsWith("CPF1192"))
               succeeded();
            else
               failed(e, "Wrong exception info.");
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String).
     * Try to write to a data area, specifying an invalid dataLength. (too large)
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var054
     **/
    public void Var042()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int length = da.getLength();
        String toWrite = "";
        for (int i=0; i<205; ++i)
          toWrite += dbcs_string5;
        try
        {
          da.write(toWrite);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
              ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            if (exceptionIs(e, "AS400Exception") &&
                e.getMessage().startsWith("CPF1192"))
               succeeded();
            else
               failed(e, "Wrong exception info.");
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify invalid usage of LocalDataArea::write(String,int).
     * Try to write to a data area, specifying an invalid
     * combination of dataAreaOffset and dataLength.
     * The method should throw an ExtendedIllegalArgumentException.
     * <i>Taken from:</i> DAWriteTestcase::Var055
     **/
    public void Var043()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        int offset = da.getLength() - 1;
        try
        {
          da.write(dbcs_string5,offset);
          failed("Expected exception did not occur.");
        }
        catch(Exception e)
        {
          if (exceptionStartsWith(e, "ExtendedIllegalArgumentException", "data",
              ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
          {
            succeeded();
          }
          else
          {
            failed(e, "Wrong exception info.");
          }
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var056
     **/
    public void Var044()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          int length = da2.getLength();
          String expected = dbcs_string50+dbcs_string50+dbcs_string50+dbcs_string50;
          expected = expected+expected+expected+expected+expected; // 1000 chars

          // see testcase 45
          // for (int i=expected.length(); i<length; ++i)
          //   expected += " ";
          for (int i=lengthInBytes(expected, systemObject_); i<length; i++)
             expected += " ";

          da2.write(expected);
          String data = da.read();
          if (data.equals(expected))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String,int).
     * Try to write to a data area.
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var057
     **/
    public void Var045()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          int length = da2.getLength();
          String expected = dbcs_string50+dbcs_string50+dbcs_string50+dbcs_string50;
          expected = expected+expected+expected; // 600 chars

          // the following is supposed to pad to the length of the
          // lda (currently 1024).  It will not work for mixed byte
          // data, however since in mixed byte each character
          // can turn into more than one byte.  The new code is better.
          // for (int i=expected.length(); i<length; ++i)
          //    expected += " ";

          for (int i=lengthInBytes(expected, systemObject_); i<length; i++)
             expected += " ";

          da2.write(expected,0);
          String data = da.read();
          if (data.equals(expected))
          {
            succeeded();
          }
          else
          {
            failed("Incorrect data write:\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
          }
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }

    /**
     * Verify valid usage of LocalDataArea::write(String,int) and
     * LocalDataArea::write(String).
     * Try to write to a data area.
     * The method should return the proper data.
     * <i>Taken from:</i> DAWriteTestcase::Var058
     **/
    public void Var046()
    {
      try
      {
        LocalDataArea da = new LocalDataArea(systemObject_);
        LocalDataArea da2 = new LocalDataArea(systemObject_);
        try
        {
          int len = da.getLength();
          if (len != 1024)
          {
            failed("Wrong length: "+len);
            return;
          }
          String xValue = "";
          for (int i=0; i<180; ++i)
          {
            xValue += dbcs_string5;
          }
          // xValue += "    "; // 1024 chars
          for (int i=lengthInBytes(xValue, systemObject_); i<len; i++)
             xValue += " ";

          da.write(xValue);

          String expected = dbcs_string5+dbcs_string10+xValue.substring(15, len);
          da.write(dbcs_string10, 5);

          String data = da2.read(0,len);
          if (!expected.equals(data))
          {
            failed("Incorrect data write(1):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          da.write(xValue);
          expected = "";
          for (int i=0; i<204; ++i)
          {
            expected += dbcs_string5;
          }
          expected += "    "; // 1024 chars
          String toWrite = expected;
          da.write(toWrite);
          data = da.read();
          if (!expected.equals(data))
          {
            failed("Incorrect data write(4):\n  Expected: '"+expected+"'\n  Returned: '"+data+"'");
            return;
          }
          succeeded();
        }
        catch(Exception e)
        {
          failed(e, "Unexpected exception.");
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    int lengthInBytes(String data, AS400 system)
        throws UnsupportedEncodingException
    {
       CharConverter x = new CharConverter(system.getCcsid(), system);
       byte[] y = x.stringToByteArray(data);
       return y.length;
    }
}



