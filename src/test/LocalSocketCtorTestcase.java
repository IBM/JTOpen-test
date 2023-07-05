///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  LocalSocketCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.*;
import java.util.Date;
import java.net.*;

/**
  Testcase LocalSocketConnectTestcase.
  **/
public class LocalSocketCtorTestcase extends Testcase
{
/**
  Constructor.  This is called from CmdTest::createTestcases().
  **/
  public LocalSocketCtorTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter (7) with the total number of variations
    // in this testcase.
    super(systemObject, "LocalSocketCtorTestcase", 7,
          variationsToRun, runMode, fileOutputStream);
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);


    // $$$ TO DO $$$
    // Replace the following 'if' blocks.  You should have one 'if' block
    // for each variation in your testcase.  Make sure you correctly set
    // the variation number and runmode in the 'if' condition, and the
    // variation number in the setVariation call.
    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED) // Note: This is an unattended variation.
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)  // Note: This is an unattended variation.
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

  }

    private boolean isAS400()
    {
        try
        {
            String s = System.getProperty("os.name");
            if (s != null)
            {
                if (s.equalsIgnoreCase("SLIC"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
    }

/**
  * Create a default AS400 object.  Running on an AS/400, this should
  * result with user ID defaulting to *current.
  **/
  public void Var001()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            if (sys.getUserId().equalsIgnoreCase("*current"))
            {
                succeeded();
            }
            else
            {
                failed("incorrect user ID " + sys.getUserId());
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create an AS400 with name "localhost".  Running on an AS/400, this should
  * result with user ID defaulting to *current.
  **/
  public void Var002()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            if (sys.getUserId().equalsIgnoreCase("*current"))
            {
                succeeded();
            }
            else
            {
                failed("incorrect user ID " + sys.getUserId());
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create an AS400 with the system name.  Running on an AS/400, this should
  * result with user ID defaulting to *current.
  **/
  public void Var003()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            if (sys.getUserId().equalsIgnoreCase("*current"))
            {
                succeeded();
            }
            else
            {
                failed("incorrect user ID " + sys.getUserId());
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create an AS400 object to the local system, supplying the user ID.
  * Verify that the user ID is set correctly.
  **/
  public void Var004()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn, "testuid");

            if (sys.getUserId().equalsIgnoreCase("testuid"))
            {
                succeeded();
            }
            else
            {
                failed("user ID was changed " + sys.getUserId());
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create an AS400 object to the local system, supplying the user ID and password.
  * Verify that the user ID is set correctly.
  **/
  public void Var005()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn, "testuid", "testpw");

            if (sys.getUserId().equalsIgnoreCase("testuid"))
            {
                succeeded();
            }
            else
            {
                failed("user ID was changed " + sys.getUserId());
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create a default AS400 object.  Running on an AS/400, this should
  * result with guiAvailable set to false.
  **/
  public void Var006()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            if (!sys.isGuiAvailable())
            {
                succeeded();
            }
            else
            {
                failed("guiAvailable set incorrectly");
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Create an AS400 object to the local system, supplying the user ID.
  * Verify that the user ID is set correctly.
  **/
  public void Var007()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn, "testuid");

            if (!sys.isGuiAvailable())
            {
                succeeded();
            }
            else
            {
                failed("incorrect state for guiAvailable");
            }
        }
        else
        {
            failed("not running on AS/400, testcase must run on an AS/400");
        }

    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }


}


