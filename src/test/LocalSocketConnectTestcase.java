///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  LocalSocketConnectTestcase.java
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
public class LocalSocketConnectTestcase extends Testcase
{
/**
  Constructor.  This is called from CmdTest::createTestcases().
  **/
  public LocalSocketConnectTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    // $$$ TO DO $$$
    // Replace the fourth parameter (7) with the total number of variations
    // in this testcase.
    super(systemObject, "LocalSocketConnectTestcase", 24,
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

  }

    private boolean isAS400()
    {
        try
        {
            String s = System.getProperty("os.name");
            if (s != null)
            {
                if (s.equalsIgnoreCase("OS/400"))
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
  * Use the default constructor, connect to file.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var001()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            sys.connectService(AS400.FILE);
            if (sys.isConnected(AS400.FILE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to file.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var002()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            sys.connectService(AS400.FILE);
            if (sys.isConnected(AS400.FILE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to file.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var003()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            sys.connectService(AS400.FILE);
            if (sys.isConnected(AS400.FILE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }


/**
  * Use the default constructor, connect to database.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var004()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            sys.connectService(AS400.DATABASE);
            if (sys.isConnected(AS400.DATABASE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to database.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var005()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            sys.connectService(AS400.DATABASE);
            if (sys.isConnected(AS400.DATABASE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to database.  Even though this does
  * not use local sockets, it should not prompt for system name, user id,
  * and password.
  **/
  public void Var006()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            sys.connectService(AS400.DATABASE);
            if (sys.isConnected(AS400.DATABASE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use the default constructor, setMustUseSockets to true, connect to dataqueue.
  **/
  public void Var007()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.DATAQUEUE);
            if (sys.isConnected(AS400.DATAQUEUE) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, setMustUseSockets to true, connect to dataqueue.
  **/
  public void Var008()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.DATAQUEUE);
            if (sys.isConnected(AS400.DATAQUEUE) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, setMustUseSockets to true, connect to dataqueue.
  **/
  public void Var009()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.DATAQUEUE);
            if (sys.isConnected(AS400.DATAQUEUE) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use the default constructor, connect to dataqueue.
  **/
  public void Var010()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            sys.connectService(AS400.DATAQUEUE);
            if (!sys.isConnected(AS400.DATAQUEUE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to dataqueue.
  **/
  public void Var011()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            sys.connectService(AS400.DATAQUEUE);
            if (!sys.isConnected(AS400.DATAQUEUE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to dataqueue.
  **/
  public void Var012()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            sys.connectService(AS400.DATAQUEUE);
            if (!sys.isConnected(AS400.DATAQUEUE))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use default constructor connect to command.
  **/
  public void Var013()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            sys.connectService(AS400.COMMAND);
            if (sys.isConnected(AS400.COMMAND))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to command.
  **/
  public void Var014()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            sys.connectService(AS400.COMMAND);
            if (sys.isConnected(AS400.COMMAND))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to command.
  **/
  public void Var015()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            sys.connectService(AS400.COMMAND);
            if (sys.isConnected(AS400.COMMAND))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use the default constructor, connect to print.
  **/
  public void Var016()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400();
            sys.connectService(AS400.PRINT);
            if (sys.isConnected(AS400.PRINT))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to print.
  **/
  public void Var017()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn);
            sys.connectService(AS400.PRINT);
            if (sys.isConnected(AS400.PRINT))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to print.
  **/
  public void Var018()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost");
            sys.connectService(AS400.PRINT);
            if (sys.isConnected(AS400.PRINT))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use the default constructor, setMustUseSockets to true, connect to record access.
  **/
  public void Var019()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost", "javatest", "jteam1");
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.RECORDACCESS);
            if (sys.isConnected(AS400.RECORDACCESS) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, setMustUseSockets to true, connect to record access.
  **/
  public void Var020()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn, "javatest", "jteam1");
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.RECORDACCESS);
            if (sys.isConnected(AS400.RECORDACCESS) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, setMustUseSockets to true, connect to record access.
  **/
  public void Var021()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost", "javatest", "jteam1");
	    sys.setMustUseSockets(true);
            sys.connectService(AS400.RECORDACCESS);
            if (sys.isConnected(AS400.RECORDACCESS) && sys.isMustUseSockets())
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Use the default constructor, connect to record access.
  **/
  public void Var022()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost", "javatest", "jteam1");
            sys.connectService(AS400.RECORDACCESS);
            if (!sys.isConnected(AS400.RECORDACCESS))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using the system name, connect to record access.
  **/
  public void Var023()
  {
    try
    {
        if (isAS400())
        {
            InetAddress ia = InetAddress.getLocalHost();
            String hn = ia.getHostName();
            AS400 sys = new AS400(hn, "javatest", "jteam1");
            sys.connectService(AS400.RECORDACCESS);
            if (!sys.isConnected(AS400.RECORDACCESS))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  * Construct using "localhost" as the system name, connect to record access.
  **/
  public void Var024()
  {
    try
    {
        if (isAS400())
        {
            AS400 sys = new AS400("localhost", "javatest", "jteam1");
            sys.connectService(AS400.RECORDACCESS);
            if (!sys.isConnected(AS400.RECORDACCESS))
            {
                succeeded();
            }
            else
            {
                failed("connect failed");
            }
            sys.disconnectAllServices();
        }
        else
        {
            failed("This variation must be run on an AS/400");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

}



