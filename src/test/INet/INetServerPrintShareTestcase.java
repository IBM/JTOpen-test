////////////////////////////////////////////////////////////////////////
//
// File Name:  INetServerPrintShareTestcase.java
//
// Classes:  ISeriesNetServerPrintShare
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// $A0=PTR/DCR  Release  Date        Userid      Comments
//              v5r3     06/24/2003  jlee        Created
//
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////
package test.INet;


import java.io.*;
import com.ibm.as400.access.*;

import test.NetServerTest;
import test.Testcase;

import java.util.Hashtable;

/**
 Testcase INetServerPrintShareTestcase.
**/
public class INetServerPrintShareTestcase extends Testcase
{
  private ISeriesNetServer netserver_;
  private ISeriesNetServer netserverPwr_;
  private ISeriesNetServerPrintShare[] shareList_;


   ISeriesNetServerPrintShare share_;

   private static final boolean DEBUG = false;


   /**
    Constructor.
    **/
   public INetServerPrintShareTestcase(AS400 systemObject,
                                      Hashtable namesAndVars,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password,
                                      AS400 pwrSys)
   {
     super(systemObject, "INetServerPrintShareTestcase", namesAndVars, runMode, fileOutputStream, password);
     pwrSys_ = pwrSys;

     if(pwrSys == null || pwrSys.getSystemName().length() == 0 || pwrSys.getUserId().length() == 0)
       throw new IllegalStateException("ERROR: Please specify a power system via -pwrsys.");

     pwrSys_ = pwrSys;
   }


   /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
    protected void setup ()
    throws Exception
    {
      lockSystem("NETSVR", 600);
      super.setup();
       netserver_ = new ISeriesNetServer(systemObject_);
       netserverPwr_ = new ISeriesNetServer(pwrSys_);
       try {
         netserverPwr_.createPrintShare("PRTTOOLBOX", "QGPL", "QPRINT");
       }
       catch(AS400Exception e)
       {
         netserverPwr_.removeShare("PRTTOOLBOX");
         netserverPwr_.createPrintShare("PRTTOOLBOX", "QGPL", "QPRINT");
       }
       shareList_ = netserver_.listPrintShares();
       if (shareList_.length == 0)
          throw new IllegalStateException("No print shares were found.  Create at least one print share on " + systemObject_.getSystemName());

       ISeriesNetServerPrintShare[] shares = netserver_.listPrintShares("PRTTOOLBOX");
       share_ = shares[0];
    }

    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
      super.cleanup();
      unlockSystem();
      String shareName = share_.getName();
      netserverPwr_.removeShare(shareName);

      // Verify that the share was removed.
      ISeriesNetServerPrintShare[] shares = netserverPwr_.listPrintShares();
      for (int i=0; i<shares.length; i++) {
        if (shares[i].getName().equals(shareName)) {
          System.out.println("ERROR during cleanup: The share " + shareName + " was not deleted.");
          break;
        }
      }
    }


///   /**
///    Construct a ISeriesNetServerPrintShare with no parameters.
///
///    ISeriesNetServerPrintShare()
///    **/
///   public void Var001()
///   {
///      try
///      {
///         ISeriesNetServerPrintShare nsfs = new ISeriesNetServerPrintShare();
///         succeeded();
///      }
///      catch (Exception e)
///      {
///         failed(e, "Wrong exception info.");
///      }
///   }
///
///   /**
///   Construct a ISeriesNetServerPrintShare with parameters.
///
///   ISeriesNetServerPrintShare(sysem, String)
///   **/
///   public void Var002()
///   {
///      try
///      {
///         ISeriesNetServerPrintShare nsfs = new ISeriesNetServerPrintShare(systemObject_, "Toolbox");
///         succeeded();
///      }
///      catch (Exception e)
///      {
///         failed(e, "Wrong exception info.");
///      }
///   }
///
///   /**
///   Construct a ISeriesNetServerPrintShare passing a null for the system parm.
///   A NullPointerException should be thrown.
///
///   ISeriesNetServerPrintShare(null, String)
///   **/
///   public void Var003()
///   {
///      try
///      {
///         ISeriesNetServerPrintShare nsfs = new ISeriesNetServerPrintShare(null, "Toolbox");
///         failed("No exception");
///      }
///      catch (Exception e)
///      {
///         assertExceptionIs (e, "NullPointerException", "system");
///      }
///   }
///
///   /**
///   Construct a ISeriesNetServerPrintShare passing a null for the request parm.
///   A NullPointerException should be thrown.
///
///   ISeriesNetServerPrintShare(system, null)
///   **/
///   public void Var004()
///   {
///      try
///      {
///         ISeriesNetServerPrintShare nsfs = new ISeriesNetServerPrintShare(systemObject_, null);
///         failed("No exception");
///      }
///      catch (Exception e)
///      {
///         assertExceptionIs (e, "NullPointerException", "name");
///      }
///   }


///   /**
///   getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY) - For a ISeriesNetServerPrintShare this should return the
///    library that contains the output queue associated with a print share.
///   **/
///   public void Var005()
///   {
///      try
///      {
///         if (share_.getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY) != "/QGPL")
///            failed("ISeriesNetServerPrintShare output queue library invalid.");
///         else
///            succeeded();
///
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }
///
///   /**
///   getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME) - For a ISeriesNetServerPrintShare this should return the name
///    of the output queue associated with a print share.
///    **/
///   public void Var006()
///   {
///      try
///      {
///         if (share_.getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME) != "QPRINT")
///            failed("ISeriesNetServerPrintShare output queue name invalid.");
///         else
///            succeeded();
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }

   /**
   getOutputQueueName() - For a ISeriesNetServerPrintShare this should return the name
    of the output queue associated with a print share.
    **/
   public void Var001()
   {
      try
      {
         if (!share_.getOutputQueueName().equals("QPRINT"))
            failed("ISeriesNetServerPrintShare output queue name invalid.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

   /**
   getOutputQueueLibrary() - For a ISeriesNetServerPrintShare this should return the library
    of the output queue associated with a print share.
    **/
   public void Var002()
   {
      try
      {
         if (!share_.getOutputQueueLibrary().equals("QGPL"))
            failed("ISeriesNetServerPrintShare output queue library invalid.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   getPrintDriver() - For a ISeriesNetServerPrintShare this should return  the type of
    printer driver for a share.
    **/
   public void Var003()
   {
      try
      {
         if (share_.getPrintDriver() == null)
            failed("ISeriesNetServerPrintShare print driver type is null.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }


   /**
   getSpooledFileType() - For a ISeriesNetServerPrintShare this should return  the
    spooled file type for a share.
    **/
   public void Var004()
   {
      try
      {
         int type = share_.getSpooledFileType();

         if (///type != ISeriesNetServerPrintShare.USER_ASCII &&
             ///type != ISeriesNetServerPrintShare.AFP &&
             ///type != ISeriesNetServerPrintShare.SCS &&
             type != ISeriesNetServerPrintShare.AUTO_DETECT)
            failed("ISeriesNetServerPrintShare spooled file type is not valid.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

///   /**
///   getNumberOfConnectionUsers()
///    **/
///   public void Var005()
///   {
///      try
///      {
///         if (share_.getNumberOfConnectionUsers() == 0)
///            succeeded();
///         else
///            failed("ISeriesNetServerPrintShare user count is not valid.");
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }


   /**
   getDescription()
    **/
   public void Var005()
   {
      try
      {
         if (share_.getDescription() == null)
            failed("ISeriesNetServerPrintShare description is null.");
         else
            succeeded();
      }
      catch (Exception e)
      {
         failed (e, "Unexpected Exception");
      }
   }

   // TBD: Add variations for the setters.

///   /**
///   setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY)
///   **/
///   public void Var011()
///   {
///      try
///      {
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY, "/");
///
///         if ((share_.getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY)).equals("/"))
///            succeeded();
///         else
///            failed("ISeriesNetServerPrintShare output queue library is invalid.");
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_LIBRARY, "/QGPL");
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }
///
///
///   /**
///   setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME)
///   **/
///   public void Var012()
///   {
///      try
///      {
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME, "");
///
///         if ((share_.getAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME)).equals(""))
///            succeeded();
///         else
///            failed("ISeriesNetServerPrintShare output queue name is invalid.");
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.OUTPUT_QUEUE_NAME, "QPRINT");
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }


///   /**
///   setAttributeValue(ISeriesNetServerPrintShare.PRINT_DRIVER_TYPE)
///   **/
///   public void Var013()
///   {
///      try
///      {
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.PRINT_DRIVER_TYPE, "");
///
///         if ((share_.getAttributeValue(ISeriesNetServerPrintShare.PRINT_DRIVER_TYPE)).equals(""))
///            succeeded();
///         else
///            failed("ISeriesNetServerPrintShare print driver type is invalid.");
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }
///
///
///   /**
///   setAttributeValue(ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE)
///   **/
///   public void Var014()
///   {
///      try
///      {
///         Integer orig = (Integer)share_.getAttributeValue(ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE);
///         share_.setAttributeValue(ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE, ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE_USER_ASCII);
///
///         if (((Integer)share_.getAttributeValue(ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE)).intValue() != (ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE_USER_ASCII).intValue())
///            failed("ISeriesNetServerPrintShare spooled file type is invalid.");
///         else
///            succeeded();
///
///         share_.setAttributeValue(ISeriesNetServerPrintShare.SPOOLED_FILE_TYPE, orig);
///      }
///      catch (Exception e)
///      {
///         failed (e, "Unexpected Exception");
///      }
///   }


   /**
    Serialization.  Verify that the object is properly serialized
    when the OUTPUT_QUEUE_NAME attributes are not set.

    NetServer
   **/
   public void Var006()
   {
      try
      {
         ISeriesNetServerPrintShare share2 = (ISeriesNetServerPrintShare) NetServerTest.serialize (share_);
         assertCondition (share2.getName().equalsIgnoreCase("PRTTOOLBOX"));
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception");
      }
   }

///   /**
///     Verify that setName throws an exception
///     when the print share name is already set.
///
///     NetServer
///    **/
///   public void Var016()
///   {
///      try
///      {
///         share_.setName("PRINTTBX");
///         failed("No exception.");
///      }
///      catch (Exception e)
///      {
///         if (e instanceof com.ibm.as400.access.ExtendedIllegalStateException &&
///             e.getMessage().equals("propertiesFrozen: Property was not changed."))
///         {
///            succeeded();
///         }
///         else
///            failed("Wrong exception");
///      }
///   }


    private static boolean validateAttributeValues(ISeriesNetServerPrintShare share)
    {
      boolean ok = true;

      String shareName = share.getName();
      String desc = share.getDescription();
      String outQ = share.getOutputQueueName();
      String outQlib = share.getOutputQueueLibrary();
      String driver = share.getPrintDriver();
      String prtFile = share.getPrinterFileName();
      String prtFileLib = share.getPrinterFileLibrary();
      ///boolean published = share.isPublished();
      int splfType = share.getSpooledFileType();

      if (shareName.length() == 0 || shareName.length() > 12) {
        ok = false;
        System.out.println("Share name has invalid length: " + shareName.length());
      }
      if (shareName.charAt(0) == ' ') {
        ok = false;
        System.out.println("shareName starts with a blank: |" + shareName + "|");
      }

      if (desc.length() > 50) {
        ok = false;
        System.out.println("Description is longer than 50 chars");
      }
      if (outQ.trim().length() == 0) {
        ok = false;
        System.out.println("Zero-length output queue name");
      }
      if (outQ.charAt(0) == ' ') {
        ok = false;
        System.out.println("outQ starts with a blank: |" + outQ + "|");
      }
      if (outQlib.trim().length() == 0) {
        ok = false;
        System.out.println("Zero-length output queue library");
      }
      if (driver.length() > 50) {
        ok = false;
        System.out.println("Driver is longer than 50 chars");
      }
      if (prtFile.length() > 10) {
        ok = false;
        System.out.println("Printer file name is longer than 10 chars");
      }
      if (prtFileLib.length() > 10) {
        ok = false;
        System.out.println("Printer file library is longer than 10 chars");
      }
      if (splfType < ISeriesNetServerPrintShare.USER_ASCII ||
          splfType > ISeriesNetServerPrintShare.AUTO_DETECT) {
        ok = false;
        System.out.println("Invalid spooled file type: " + splfType);
      }

      return ok;
    }

    static void displayAttributeValues(ISeriesNetServerPrintShare share)
    {
      String shareName = share.getName();
      String desc = share.getDescription();
      String outQ = share.getOutputQueueName();
      String outQlib = share.getOutputQueueLibrary();
      String driver = share.getPrintDriver();
      String prtFile = share.getPrinterFileName();
      String prtFileLib = share.getPrinterFileLibrary();
      boolean published = share.isPublished();
      int splfType = share.getSpooledFileType();
      System.out.println("--------\n" +
                         "PRINT SHARE " + shareName + ":\n"+
                         "--------\n" +
                         "Description: "+desc+"\n"+
                         "OutQ: "+outQ+"\n"+
                         "OutQ library: "+outQlib+"\n"+
                         "Print driver: "+driver+"\n"+
                         "Printer file: "+prtFile+"\n"+
                         "Printer file lib: "+prtFileLib+"\n"+
                         "Published: "+published+"\n"+
                         "Spooled file type: "+splfType
                         );
    }

    /**
    Display and validate all attributes for all print shares.
    **/
    public void Var007()
    {
        try
        {
          for (int i=0; i<shareList_.length; i++)
          {
            ISeriesNetServerPrintShare share = shareList_[i];
            if (DEBUG) displayAttributeValues(share);
            if (!validateAttributeValues(share)) {
              failed("Share has invalid attribute value(s).");
              return;
            }
            netserver_.refresh(share);
            if (!validateAttributeValues(share)) {
              failed("Share has invalid attribute value(s) after refresh.");
              return;
            }
          }
          succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    For a specific print share, change all its changeable attributes to different values, and verify that the changes took effect.
    **/
    public void Var008()
    {
      boolean ok = true;

      // Get all of the current attribute values of the share.
      String name_orig = share_.getName();
      String desc_orig = share_.getDescription();
      String outq_orig = share_.getOutputQueueName();
      String outqLib_orig = share_.getOutputQueueLibrary();
      String driver_orig = share_.getPrintDriver();
      String prtFile_orig = share_.getPrinterFileName();
      String prtFileLib_orig = share_.getPrinterFileLibrary();
      boolean published_orig = share_.isPublished();
      int splfType_orig = share_.getSpooledFileType();

      if (DEBUG) {
        System.out.println("\nOriginal attributes:");
        displayAttributeValues(share_);
      }

      // Change the values to something else.

      // Note: The share name is not changeable.
      String desc_new = (desc_orig.equals("New desc") ? "Different desc" : "New Desc");
      String outq_new = (outq_orig.equals("QPRINT") ? "QPRINT0" : "QPRINT");
      String outqLib_new = (outqLib_orig.equals("QGPL") ? "QIBM" : "QGPL");
      String driver_new = (driver_orig.equals("") ? "Driver0" : "");
      String prtFile_new = (prtFile_orig.equals("PRTFILE0") ? "PRTFILE1" : "PRTFILE0");
      String prtFileLib_new = (prtFileLib_orig.equals("PRTLIB0") ? "PRTLIB1" : "PRTLIB0");
      boolean published_new = (published_orig == false ? true : false);
      int splfType_new = (splfType_orig == 4 ? 3 : 4);

      share_.setDescription(desc_new);
      share_.setOutputQueueName(outq_new);
      share_.setOutputQueueLibrary(outqLib_new);
      share_.setPrintDriver(driver_new);
      share_.setPrinterFileName(prtFile_new);
      share_.setPrinterFileLibrary(prtFileLib_new);
      share_.setSpooledFileType(splfType_new);

      try {
        ///System.out.println ("About to commitChanges.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
        netserverPwr_.commitChanges(share_);
      }
      catch (Exception e) {
        e.printStackTrace();
        ///System.out.println ("Caught exception.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
      }

      // Change the "publish print share" attribute separately, since it can throw a non-fatal exception if LDAP is not configured and we're trying to turn "publish" on.
      boolean canPublishShare = true;
      share_.setPublished(published_new);
      try {
        netserverPwr_.commitChanges(share_);
      }
      catch (Exception e) {
        if (DEBUG) e.printStackTrace();
        if (published_new == true) canPublishShare = false;
      }

      if (DEBUG) {
        System.out.println("\nAttributes after sets:");
        displayAttributeValues(share_);
      }

      // BLOCK 1.
      // Verify the changes.

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 1: name"); }
      if (!share_.getDescription().equals(desc_new)) { ok = false; println("Block 1: desc"); }
      if (!share_.getOutputQueueName().equals(outq_new)) { ok = false; println("Block 1: outq"); }
      if (!share_.getOutputQueueLibrary().equals(outqLib_new)) { ok = false; println("Block 1: outqLib"); }
      if (!share_.getPrintDriver().equals(driver_new)) { ok = false; println("Block 1: driver"); }
      if (!share_.getPrinterFileName().equals(prtFile_new)) { ok = false; println("Block 1: prtFile"); }
      if (!share_.getPrinterFileLibrary().equals(prtFileLib_new)) { ok = false; println("Block 1: prtFileLib"); }
      if (share_.isPublished() != published_new) { ok = false; println("Block 1: published"); }
      if (share_.getSpooledFileType() != splfType_new) { ok = false; println("Block 1: splfType"); }

      if (!canPublishShare) published_new = false;

      // BLOCK 2.
      // Do a refresh, and reverify the changes.

      try { netserverPwr_.refresh(share_); }
      catch (Exception e) { e.printStackTrace(); }

      if (DEBUG) {
        System.out.println("\nAttributes after refresh:");
        displayAttributeValues(share_);
      }

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 2: name"); }
      if (!share_.getDescription().equals(desc_new)) { ok = false; println("Block 2: desc"); }
      if (!share_.getOutputQueueName().equals(outq_new)) { ok = false; println("Block 2: outq"); }
      if (!share_.getOutputQueueLibrary().equals(outqLib_new)) { ok = false; println("Block 2: outqLib"); }
      if (!share_.getPrintDriver().equals(driver_new)) { ok = false; println("Block 2: driver"); }
      if (!share_.getPrinterFileName().equals(prtFile_new)) { ok = false; println("Block 2: prtFile"); }
      if (!share_.getPrinterFileLibrary().equals(prtFileLib_new)) { ok = false; println("Block 2: prtFileLib"); }
      if (share_.isPublished() != published_new) { ok = false; println("Block 2: published"); }
      if (share_.getSpooledFileType() != splfType_new) { ok = false; println("Block 2: splfType"); }


      // Reset the attributes back to their original values.

      share_.setDescription(desc_orig);
      share_.setOutputQueueName(outq_orig);
      share_.setOutputQueueLibrary(outqLib_orig);
      share_.setPrintDriver(driver_orig);
      share_.setPrinterFileName(prtFile_orig);
      share_.setPrinterFileLibrary(prtFileLib_orig);
      share_.setPublished(published_orig);
      share_.setSpooledFileType(splfType_orig);

      try {
        netserverPwr_.commitChanges(share_);
      }
      catch (Exception e) { e.printStackTrace(); }

      if (DEBUG) {
        System.out.println("\nAttributes after reset to original values:");
        displayAttributeValues(share_);
      }

      // BLOCK 3.
      // Verify the changes.

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 3: name"); }
      if (!share_.getDescription().equals(desc_orig)) { ok = false; println("Block 3: desc"); }
      if (!share_.getOutputQueueName().equals(outq_orig)) { ok = false; println("Block 3: outq"); }
      if (!share_.getOutputQueueLibrary().equals(outqLib_orig)) { ok = false; println("Block 3: outqLib"); }
      if (!share_.getPrintDriver().equals(driver_orig)) { ok = false; println("Block 3: driver"); }
      if (!share_.getPrinterFileName().equals(prtFile_orig)) { ok = false; println("Block 3: prtFile"); }
      if (!share_.getPrinterFileLibrary().equals(prtFileLib_orig)) { ok = false; println("Block 3: prtFileLib"); }
      if (share_.isPublished() != published_orig) { ok = false; println("Block 3: published"); }
      if (share_.getSpooledFileType() != splfType_orig) { ok = false; println("Block 3: splfType"); }

      // BLOCK 4.
      // Do a refresh, and reverify the changes.

      try { netserverPwr_.refresh(share_); }
      catch (Exception e) { e.printStackTrace(); }

      if (DEBUG) {
        System.out.println("\nAttributes after refresh:");
        displayAttributeValues(share_);
      }

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 4: name"); }
      if (!share_.getDescription().equals(desc_orig)) { ok = false; println("Block 4: desc"); }
      if (!share_.getOutputQueueName().equals(outq_orig)) { ok = false; println("Block 4: outq"); }
      if (!share_.getOutputQueueLibrary().equals(outqLib_orig)) { ok = false; println("Block 4: outqLib"); }
      if (!share_.getPrintDriver().equals(driver_orig)) { ok = false; println("Block 4: driver"); }
      if (!share_.getPrinterFileName().equals(prtFile_orig)) { ok = false; println("Block 4: prtFile"); }
      if (!share_.getPrinterFileLibrary().equals(prtFileLib_orig)) { ok = false; println("Block 4: prtFileLib"); }
      if (share_.isPublished() != published_orig) { ok = false; println("Block 4: published"); }
      if (share_.getSpooledFileType() != splfType_orig) { ok = false; println("Block 4: splfType"); }

      if (ok) succeeded();
      else failed();
    }

}
