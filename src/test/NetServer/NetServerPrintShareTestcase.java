///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NetServerPrintShareTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.NetServer;


import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.NetServerPrintShare;
import com.ibm.as400.resource.ResourceException;

import test.NetServerTest;
import test.Testcase;

/** 
 Testcase NetServerPrintShareTestcase.
**/
@SuppressWarnings("deprecation")
public class NetServerPrintShareTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NetServerPrintShareTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }

   NetServerPrintShare pshare_;


   /**
    Constructor.
    **/
   public NetServerPrintShareTestcase(AS400 systemObject, 
                                      Hashtable<String,Vector<String>> namesAndVars, 
                                      int runMode, 
                                      FileOutputStream fileOutputStream, 
                                      
                                      String password,
                                      AS400 pwrSys)
   {
      super(systemObject, "NetServerPrintShareTestcase", namesAndVars, runMode, fileOutputStream, password);
      pwrSys_ = pwrSys;
      
      if(pwrSys == null)
         throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");
   }


   /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
       try
       {
         lockSystem("NETSVR", 600);
         super.setup();
          pshare_ = new NetServerPrintShare(pwrSys_, "PRTTOOLBOX");
          pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME, "QPRINT");
          pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY, "/QGPL");
          pshare_.add();
       }
       catch(ResourceException e)
       {
          pshare_.remove();
          pshare_.add();
       }
    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
       pshare_.remove();
       super.cleanup();
       unlockSystem();
    }


   /**
    Construct a NetServerPrintShare with no parameters.
    
    NetServerPrintShare() 
    **/
   public void Var001()
   {
      try
      {
         NetServerPrintShare nsfs = new NetServerPrintShare();
         succeeded("nsfs="+nsfs);
      }
      catch (Exception e)
      {
         failed(e, "Wrong exception info.");
      }
   }

   /**
   Construct a NetServerPrintShare with parameters.
    
   NetServerPrintShare(sysem, String) 
   **/
   public void Var002()
   {
      try
      {
         NetServerPrintShare nsfs = new NetServerPrintShare(systemObject_, "Toolbox");
         succeeded("nsfs="+nsfs);
      }
      catch (Exception e)
      {
         failed(e, "Wrong exception info.");
      }
   }

   /**
   Construct a NetServerPrintShare passing a null for the system parm.
   A NullPointerException should be thrown.

   NetServerPrintShare(null, String)
   **/
   public void Var003()
   {
      try
      {
         NetServerPrintShare nsfs = new NetServerPrintShare(null, "Toolbox");
         failed("No exception"+nsfs);
      }
      catch (Exception e)
      {
         assertExceptionIs (e, "NullPointerException", "system");
      }
   }

   /**
   Construct a NetServerPrintShare passing a null for the request parm.
   A NullPointerException should be thrown.

   NetServerPrintShare(system, null)
   **/
   public void Var004()
   {
      try
      {
         NetServerPrintShare nsfs = new NetServerPrintShare(systemObject_, null);
         failed("No exception"+nsfs);
      }
      catch (Exception e)
      {
         assertExceptionIs (e, "NullPointerException", "name");
      }
   }

   /**
   getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY) - For a NetServerPrintShare this should return the
    library that contains the output queue associated with a print share.
   **/
   public void Var005()
   {
      try
      {
         if (pshare_.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY) != "/QGPL")
            failed("NetServerPrintShare output queue library invalid.");
         else
            succeeded();
         
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

   /**
   getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME) - For a NetServerPrintShare this should return the name
    of the output queue associated with a print share.
    **/
   public void Var006()
   {
      try
      {
         if (pshare_.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME) != "QPRINT")
            failed("NetServerPrintShare output queue name invalid.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   getAttributeValue(NetServerPrintShare.PRINT_DRIVER_TYPE) - For a NetServerPrintShare this should return  the type of
    printer driver for a share. 
    **/
   public void Var007()
   {
      try
      {
         if (pshare_.getAttributeValue(NetServerPrintShare.PRINT_DRIVER_TYPE) == null)
            failed("NetServerPrintShare print driver type is null.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   getAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE) - For a NetServerPrintShare this should return  the
    spooled file type for a share.  
    **/
   public void Var008()
   {
      try
      {
         int type = ((Integer)pshare_.getAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE)).intValue();

         if (type != (NetServerPrintShare.SPOOLED_FILE_TYPE_USER_ASCII).intValue() &&
             type != (NetServerPrintShare.SPOOLED_FILE_TYPE_AFP).intValue() &&
             type != (NetServerPrintShare.SPOOLED_FILE_TYPE_SCS).intValue() &&
             type != (NetServerPrintShare.SPOOLED_FILE_TYPE_AUTO_DETECT).intValue())
            failed("NetServerPrintShare spooled file type is not valid.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

   /**
   getAttributeValue(NetServerPrintShare.USER_COUNT) 
    **/
   public void Var009()
   {
      try
      {
        int userCount = ((Integer)pshare_.getAttributeValue(NetServerPrintShare.USER_COUNT)).intValue(); 
         if ( userCount == 0)
            succeeded();
         else {
            failed("NetServerPrintShare user count is not valid ("+userCount+").");
         }
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   getAttributeValue(NetServerPrintShare.DESCRIPTION) 
    **/
   public void Var010()
   {
      try
      {
         if (pshare_.getAttributeValue(NetServerPrintShare.DESCRIPTION) == null)
            failed("NetServerPrintShare description is null.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY)  
   **/
   public void Var011()
   {
      try
      {
         
         pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY, "/");

         if ((pshare_.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY)).equals("/"))
            succeeded();
         else
            failed("NetServerPrintShare output queue library is invalid.");

         pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_LIBRARY, "/QGPL");
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME)  
   **/
   public void Var012()
   {
      try
      {
         
         pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME, "");

         if ((pshare_.getAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME)).equals(""))
            succeeded();
         else      
            failed("NetServerPrintShare output queue name is invalid.");

         pshare_.setAttributeValue(NetServerPrintShare.OUTPUT_QUEUE_NAME, "QPRINT");
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   setAttributeValue(NetServerPrintShare.PRINT_DRIVER_TYPE)  
   **/
   public void Var013()
   {
      try
      {
         
         pshare_.setAttributeValue(NetServerPrintShare.PRINT_DRIVER_TYPE, "");

         if ((pshare_.getAttributeValue(NetServerPrintShare.PRINT_DRIVER_TYPE)).equals(""))
            succeeded();
         else
            failed("NetServerPrintShare print driver type is invalid.");
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   setAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE)  
   **/
   public void Var014()
   {
      try
      {
         Integer orig = (Integer)pshare_.getAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE);
         pshare_.setAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE, NetServerPrintShare.SPOOLED_FILE_TYPE_USER_ASCII);

         if (((Integer)pshare_.getAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE)).intValue() != (NetServerPrintShare.SPOOLED_FILE_TYPE_USER_ASCII).intValue())
            failed("NetServerPrintShare spooled file type is invalid.");
         else
            succeeded();

         pshare_.setAttributeValue(NetServerPrintShare.SPOOLED_FILE_TYPE, orig);
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
    Serialization.  Verify that the object is properly serialized
    when the OUTPUT_QUEUE_NAME attributes are not set.
   
    NetServer
   **/
   public void Var015()
   {
      try
      {
         NetServerPrintShare share2 = (NetServerPrintShare) NetServerTest.serialize (pshare_);
         assertCondition (share2.getName().equalsIgnoreCase("PRTTOOLBOX"));
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception");
      }
   }

   /**
     Verify that setName throws and exception
     when the print share name is already set.
     
     NetServer
    **/
   public void Var016()
   {
      try
      {
         pshare_.setName("PRINTTBX");
         failed("No exception.");
      }
      catch (Exception e)
      {
         if (e instanceof com.ibm.as400.access.ExtendedIllegalStateException &&
             e.getMessage().equals("propertiesFrozen: Property was not changed."))
         {
            succeeded();
         }
         else
            failed("Wrong exception");
      }
   }
   
}
