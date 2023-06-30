///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JPingTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JPing;

/**
 Testcase AS400JPingTestcase.
 **/
public class AS400JPingTestcase extends Testcase
{
    boolean deleteFile(String fileName)
    {
        try
        {
            File file = new File(fileName);
            if (!file.delete())
            {
                output_.println("Could not delete " + fileName );
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            output_.println("Could not delete " + fileName );
            e.printStackTrace(output_);
            return false;
        }
    }

    /**
     Successful construction of an AS400JPing using the default ctor.
     AS400JPing(String)
     **/
    public void Var001()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var002()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.COMMAND);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var003()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.DATABASE);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var004()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.DATAQUEUE);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var005()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.RECORDACCESS);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var006()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.CENTRAL);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var007()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.FILE);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var008()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.PRINT);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name and service.
     AS400JPing(String, int)
     **/
    public void Var009()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400JPing.ALL_SERVICES);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Successful construction of an AS400JPing using the system name, service, and ssl parm.
     AS400JPing(String, int, boolean)
     **/
    public void Var010()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.SIGNON, false);
            succeeded("ping is "+ping);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Unsuccessful construction of an AS400JPing using the system name and an invalid service.
     AS400JPing(String, int)
     **/
    public void Var011()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), 66);
            failed("No exception for "+ping);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalArgumentException", "service (66): Parameter value is not valid."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Unsuccessful construction of an AS400JPing using a null system name.
     AS400JPing(null, int)
     **/
    public void Var012()
    {
        try
        {
            AS400JPing ping = new AS400JPing(null, AS400.COMMAND);
            failed("No exception for "+ping);
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "systemName"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping() to verify all the services.
     AS400JPing(String)
     AS400JPing::ping()
     **/
    public void Var013()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping();

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping an AS/400 service.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var014()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.CENTRAL);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping Central Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var015()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.COMMAND);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping Command Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var016()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.DATABASE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping Database Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var017()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.DATAQUEUE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping DataQueue Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var018()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.SIGNON);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping Signon Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var019()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.FILE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping File Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var020()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.PRINT);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping Print Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var021()
    {
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            boolean success = ping.ping(AS400.RECORDACCESS);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                succeeded("Unable to successfully ping DDM Server.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the PrintWriter to null.  A NullPointerException should be recieved.
     AS400JPing(String)
     **/
    public void Var022()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());
            ping.setPrintWriter(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "stream"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the timeout.
     AS400JPing(String)
     **/
    public void Var023()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());
            ping.setTimeout(5000);
            succeeded();
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the print writer.
     AS400JPing(String)
     **/
    public void Var024()
    {
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());
            ping.setPrintWriter(System.out);
            succeeded();
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the print writer.
     Log AS400JPing messages.
     AS400JPing(String)
     **/
    public void Var025()
    {
        FileOutputStream stream = null;
        RandomAccessFile file = null;
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            stream = new FileOutputStream("PingLog");
            ping.setPrintWriter(stream);
            ping.ping(AS400.COMMAND);

            String data = "";
            String data2 = null;
            file = new RandomAccessFile("PingLog", "r");
            while ((data2 = file.readLine()) != null)
            { 
                data += data2;
            }

            assertCondition(data.indexOf("Successfully connected to server application:  as-rmtcmd") != -1);
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
        finally
        {
          try { if (stream != null) stream.close(); } catch (Throwable t) { t.printStackTrace(); }
          try { if (file != null) file.close(); } catch (Throwable t) { t.printStackTrace(); }
          deleteFile("PingLog");
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the print writer.
     Set the service to Record Level Access.
     Log AS400JPing messages.
     AS400JPing(String)
     **/
    public void Var026()
    {
        FileOutputStream stream = null;
        RandomAccessFile file = null;
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName());

            stream = new FileOutputStream("PingLog");
            ping.setPrintWriter(stream);
            ping.ping(AS400.RECORDACCESS);

            String data = "";
            String data2 = null;
            file = new RandomAccessFile("PingLog", "r");
            while ((data2 = file.readLine()) != null)
            { 
                data += data2;
            }

            assertCondition(data.indexOf("Successfully connected to server application:  as-ddm") != -1);
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
        finally
        {
          try { if (stream != null) stream.close(); } catch (Throwable t) { t.printStackTrace(); }
          try { if (file != null) file.close(); } catch (Throwable t) { t.printStackTrace(); }
          deleteFile("PingLog");
        }
    }




    /**
     SSL variation
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var027()
    {

	// SSL does not work with JDK 1.4 due to insecure older SSL protocol
	if (isJDK14) {
	    notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
	    return; 
	} 

        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.CENTRAL, true);

            boolean success = ping.ping(AS400.CENTRAL);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping Central Server using SSL");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL 
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var028()
    {
      // SSL does not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 

      try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.COMMAND, true);

            boolean success = ping.ping(AS400.COMMAND);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping Command Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var029()
    {
      // SSL does not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.DATABASE, true);

            boolean success = ping.ping(AS400.DATABASE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping Database Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var030()
    {
      // SSL does not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.DATAQUEUE, true);

            boolean success = ping.ping(AS400.DATAQUEUE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping DataQueue Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var031()
    {
      // SSL does not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.SIGNON, true);

            boolean success = ping.ping(AS400.SIGNON);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping Signon Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL 
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var032()
    {
      // SSL do not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.FILE, true );

            boolean success = ping.ping(AS400.FILE);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping File Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL 
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var033()
    {
      // SSL do not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.PRINT, true);

            boolean success = ping.ping(AS400.PRINT);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping Print Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     SSL 
     Construct an AS400JPing with the system name parm.
     Use ping(int) to verify a specific as/400 service.
     AS400JPing(String)
     AS400JPing::ping(int)
     **/
    public void Var034()
    {
      // SSL do not work with JDK 1.4 due to insecure older SSL protocol
      if (isJDK14) {
          notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
          return; 
      } 
        try
        {
            // create a AS400JPing
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.RECORDACCESS, true);

            boolean success = ping.ping(AS400.RECORDACCESS);

            if (success == true)
            {
                succeeded();
            }
            else if (success == false)
            {   
                failed("Unable to successfully ping DDM Server using SSL.");
            }
            else
                failed("Unexpected return value.");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     SSL
     Construction of an AS400JPing object.
     Set the print writer.
     Log AS400JPing messages.
     AS400JPing(String)
     **/
    public void Var035()
    {
        FileOutputStream stream = null;
        RandomAccessFile file = null;
        // SSL do not work with JDK 1.4 due to insecure older SSL protocol
        if (isJDK14) {
            notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
            return; 
        } 
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.COMMAND, true);

            stream = new FileOutputStream("PingLog");
            ping.setPrintWriter(stream);
            ping.ping(AS400.COMMAND);

            String data = "";
            String data2 = null;
            file = new RandomAccessFile("PingLog", "r");
            while ((data2 = file.readLine()) != null)
            { 
                data += data2;
            }

            assertCondition(data.indexOf("Successfully connected to server application:  as-rmtcmd") != -1, "SSL failed:  Data string returned is "+data);
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
        finally
        {
          try { if (stream != null) stream.close(); } catch (Throwable t) { t.printStackTrace(); }
          try { if (file != null) file.close(); } catch (Throwable t) { t.printStackTrace(); }
          deleteFile("PingLog");
        }
    }

    /**
     Construction of an AS400JPing object.
     Set the print writer.
     Set the service to Record Level Access.
     Log AS400JPing messages.
     AS400JPing(String)
     **/
    public void Var036()
    {
        FileOutputStream stream = null;
        RandomAccessFile file = null;
        // SSL do not work with JDK 1.4 due to insecure older SSL protocol
        if (isJDK14) {
            notApplicable("SSL not working for JDK 1.4 due to insecure older SSL protocol");
            return; 
        } 
        try
        {
            AS400JPing ping = new AS400JPing(systemObject_.getSystemName(), AS400.RECORDACCESS, true );

            stream = new FileOutputStream("PingLog");
            ping.setPrintWriter(stream);
            ping.ping(AS400.RECORDACCESS);

            String data = "";
            String data2 = null;
            file = new RandomAccessFile("PingLog", "r");
            while ((data2 = file.readLine()) != null)
            { 
                data += data2;
            }

            assertCondition(data.indexOf("Successfully connected to server application:  as-ddm") != -1, "SSL failed:  data string returned is "+data);
        }
        catch (Exception e)
        {
            failed("Unexpected exception.");
        }
        finally
        {
          try { if (stream != null) stream.close(); } catch (Throwable t) { t.printStackTrace(); }
          try { if (file != null) file.close(); } catch (Throwable t) { t.printStackTrace(); }
          deleteFile("PingLog");
        }
    }





}


