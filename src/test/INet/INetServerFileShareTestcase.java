////////////////////////////////////////////////////////////////////////
//
// File Name:  INetServerFileShareTestcase.java
//
// Classes:  NetServerFileShare
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// $A0=PTR/DCR  Release  Date        Userid      Comments
//              v5r3     06/23/2003  jlee        Created
//
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////
package test.INet;


import java.io.*;
import com.ibm.as400.access.*;

import test.Testcase;

import java.util.Hashtable; import java.util.Vector;

/**
 Testcase INetServerFileShareTestcase.
**/
public class INetServerFileShareTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "INetServerFileShareTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.INetServerTest.main(newArgs); 
   }
  ISeriesNetServer netserver_;
  private ISeriesNetServer netserverPwr_;
  private ISeriesNetServerFileShare[] shareList_;
  ISeriesNetServerFileShare share_;

  static final boolean DEBUG = false;

    /**
     Constructor.
     **/
    public INetServerFileShareTestcase(AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password,
                             AS400 pwrSys)
    {
        super(systemObject, "INetServerFileShareTestcase", namesAndVars, runMode, fileOutputStream, password);

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
         netserverPwr_.createFileShare("TOOLBOXTST", "/root");
       }
       catch(AS400Exception e)
       {
         netserverPwr_.removeShare("TOOLBOXTST");
         netserverPwr_.createFileShare("TOOLBOXTST", "/root");
       }
       shareList_ = netserverPwr_.listFileShares();
       if (shareList_.length == 0)
          throw new IllegalStateException("No file shares were found.  Create at least one file share on " + systemObject_.getSystemName());

       ISeriesNetServerFileShare[] shares = netserverPwr_.listFileShares("TOOLBOXTST");
       share_ = shares[0];
    }


    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
      String shareName = share_.getName();
      netserverPwr_.removeShare(shareName);

      // Verify that the share was removed.
      ISeriesNetServerFileShare[] shares = netserverPwr_.listFileShares();
      for (int i=0; i<shares.length; i++) {
        if (shares[i].getName().equals(shareName)) {
          System.out.println("ERROR during cleanup: The share " + shareName + " was not deleted.");
          break;
        }
      }
      super.cleanup();
      unlockSystem();
    }


///    /**
///     Construct a ISeriesNetServerFileShare with no parameters.
///
///     ISeriesNetServerFileShare()
///     **/
///    public void Var001()
///    {
///        try
///        {
///            ISeriesNetServerFileShare nsfs = new ISeriesNetServerFileShare();
///            succeeded();
///        }
///        catch (Exception e)
///        {
///           failed(e, "Wrong exception info.");
///        }
///    }
///
///    /**
///    Construct a ISeriesNetServerFileShare with parameters.
///
///    ISeriesNetServerFileShare(sysem, String)
///    **/
///    public void Var002()
///    {
///        try
///        {
///            ISeriesNetServerFileShare nsfs = new ISeriesNetServerFileShare(systemObject_, "Sam");
///            succeeded();
///        }
///        catch (Exception e)
///        {
///           failed(e, "Wrong exception info.");
///        }
///    }
///
///    /**
///    Construct a ISeriesNetServerFileShare passing a null for the system parm.
///    A NullPointerException should be thrown.
///
///    ISeriesNetServerFileShare(null, String)
///    **/
///    public void Var003()
///    {
///        try
///        {
///           ISeriesNetServerFileShare nsfs = new ISeriesNetServerFileShare(null, "Sam");
///           failed("No exception");
///        }
///        catch (Exception e)
///        {
///           assertExceptionIs (e, "NullPointerException", "system");
///        }
///    }
///
///    /**
///    Construct a ISeriesNetServerFileShare passing a null for the request parm.
///    A NullPointerException should be thrown.
///
///    ISeriesNetServerFileShare(system, null)
///    **/
///    public void Var004()
///    {
///        try
///        {
///           ISeriesNetServerFileShare nsfs = new ISeriesNetServerFileShare(systemObject_, null);
///           failed("No exception");
///        }
///        catch (Exception e)
///        {
///           assertExceptionIs (e, "NullPointerException", "name");
///        }
///    }


///    /**
///    getAttributeValue(NetServerFlieShare.PATH) - For a ISeriesNetServerFileShare this should return the integrated
///    file system path.
///    **/
///    public void Var005()
///    {
///        try
///        {
///            NetServer ns = new NetServer(systemObject_);
///            ResourceList shareList = ns.listFileShares();
///            shareList.waitForComplete();
///            ISeriesNetServerFileShare share = (ISeriesNetServerFileShare)shareList.resourceAt(0);
///
///            if ((String)share.getAttributeValue(ISeriesNetServerFileShare.PATH) != null)
///               succeeded();
///            else
///               failed("ISeriesNetServerFileShare path is null.");
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    getAttributeValue(NetServerFlieShare.MAXIMUM_USERS) - For a ISeriesNetServerFileShare this should return the
///    maximum number of users who can concurrently access this share. The default is -1.
///    **/
///    public void Var006()
///    {
///        try
///        {
///           NetServer ns = new NetServer(systemObject_);
///            ResourceList shareList = ns.listFileShares();
///            shareList.waitForComplete();
///            ISeriesNetServerFileShare share = (ISeriesNetServerFileShare)shareList.resourceAt(0);
///            if (((Integer)share.getAttributeValue(ISeriesNetServerFileShare.MAXIMUM_USERS)).intValue() != -1)
///               failed("ISeriesNetServerFileShare path is null.");
///            else
///               succeeded();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    setAttributeValue(NetServerFlieShare.MAXIMUM_USERS) - For a ISeriesNetServerFileShare this should return the
///    maximum number of users who can concurrently access this share.
///    **/
///    public void Var007()
///    {
///        try
///        {
///            NetServer ns = new NetServer(systemObject_);
///            ResourceList shareList = ns.listFileShares();
///            shareList.waitForComplete();
///            ISeriesNetServerFileShare share = (ISeriesNetServerFileShare)shareList.resourceAt(0);
///
///            share.setAttributeValue(ISeriesNetServerFileShare.MAXIMUM_USERS, new Integer(100));
///
///            if (((Integer)share.getAttributeValue(ISeriesNetServerFileShare.MAXIMUM_USERS)).intValue() != 100)
///               failed("ISeriesNetServerFileShare maximum users is invalid.");
///            else
///               succeeded();
///
///            share.setAttributeValue(ISeriesNetServerFileShare.MAXIMUM_USERS, new Integer(-1));
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    getAttributeValue(NetServerFlieShare.PERMISSION) - For a ISeriesNetServerFileShare this represents the permission
///     for a share. The default is read-write.
///    **/
///    public void Var008()
///    {
///        try
///        {
///            NetServer ns = new NetServer(systemObject_);
///            ResourceList shareList = ns.listFileShares();
///            shareList.waitForComplete();
///            ISeriesNetServerFileShare share = (ISeriesNetServerFileShare)shareList.resourceAt(0);
///
///            if (((Integer)share.getAttributeValue(ISeriesNetServerFileShare.PERMISSION)).intValue() != (ISeriesNetServerFileShare.PERMISSION_READ_WRITE).intValue() &&
///                ((Integer)share.getAttributeValue(ISeriesNetServerFileShare.PERMISSION)).intValue() != (ISeriesNetServerFileShare.PERMISSION_READ_ONLY).intValue() )
///               failed("ISeriesNetServerFileShare Permission is incorect.");
///            else
///               succeeded();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }


///    /**
///    setAttributeValue(NetServerFlieShare.PERMISSION) - For a ISeriesNetServerFileShare this represents the permission
///     for a share.
///    **/
///    public void Var009()
///    {
///        try
///        {
///            NetServer ns = new NetServer(systemObject_);
///            ResourceList shareList = ns.listFileShares();
///            shareList.waitForComplete();
///            ISeriesNetServerFileShare share = (ISeriesNetServerFileShare)shareList.resourceAt(0);
///
///            share.setAttributeValue(ISeriesNetServerFileShare.PERMISSION, ISeriesNetServerFileShare.PERMISSION_READ_ONLY);
///
///            if ((Integer)share.getAttributeValue(ISeriesNetServerFileShare.PERMISSION) != ISeriesNetServerFileShare.PERMISSION_READ_ONLY)
///               failed("ISeriesNetServerFileShare Permission is incorect.");
///            else
///               succeeded();
///
///            share.setAttributeValue(ISeriesNetServerFileShare.PERMISSION, ISeriesNetServerFileShare.PERMISSION_READ_WRITE);
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///    }
///
///    /**
///    add() - Adds the file server share to the NetServer.
///    **/
///    public void Var010()
///    {
///        ISeriesNetServerFileShare share = null;
///        try
///        {
///            share = new ISeriesNetServerFileShare(systemObject_, "Toolbox");
///            share.setAttributeValue(ISeriesNetServerFileShare.PATH, "/QIBM");
///            share.add();
///
///            succeeded();
///        }
///        catch (Exception e)
///        {
///            failed (e, "Unexpected Exception");
///        }
///        finally
///        {
///          if (share != null) try { share.remove(); } catch (Exception e) {}
///        }
///    }


    static void displayAttributeValues(ISeriesNetServerFileShare share)
    {
      System.out.print("-------------------------\n"+
                       "FILE SHARE " + share.getName() + ":\n"+
                       "-------------------------\n"+
                       "Descr: |" + share.getDescription() +"|\n"+
                       "Path:  |" + share.getPath() +"|\n"+
                       "Perms: " + share.getPermissions() +"\n"+
                       "MaxUs: " + share.getMaximumNumberOfUsers() +"\n"+
                       "CurUs: " + share.getCurrentNumberOfUsers() +"\n"+
                       "ConvE: " + share.getTextConversionEnablement() +"\n"+
                       "CCSID: " + share.getCcsidForTextConversion() +"\n"+
                       "Extns: ");
      String[] extensions = share.getFileExtensions();
      if (extensions.length == 0) System.out.println("(none)");
      else {
        for (int i=0; i<extensions.length; i++) {
          System.out.print(extensions[i] + ", ");
        }
        System.out.println();
      }
      System.out.println();
    }

    private static boolean validateAttributeValues(ISeriesNetServerFileShare share)
    {
      boolean ok = true;

      String shareName = share.getName();
      int maxUsers = share.getMaximumNumberOfUsers();
      int numUsers = share.getCurrentNumberOfUsers();
      String desc = share.getDescription();
      String path = share.getPath();
      int perms = share.getPermissions();
      int textConv = share.getTextConversionEnablement();
      int ccsid = share.getCcsidForTextConversion();
      String[] fileExts = share.getFileExtensions();

      if (shareName.trim().length() == 0 || shareName.length() > 12) {
        ok = false;
        System.out.println("Share name has invalid length: " + shareName.length());
      }
      if (shareName.charAt(0) == ' ') {
        ok = false;
        System.out.println("shareName starts with a blank: |" + shareName + "|");
      }

      if (maxUsers < -1) {
        ok = false;
        System.out.println("Max Users < -1");
      }

      if (numUsers < 0 && numUsers != ISeriesNetServerFileShare.UNKNOWN) {
        ok = false;
        System.out.println("Num Users < -1");
      }
      if (numUsers > 100) {
        ///ok = false;
        System.out.println("Warning: Num Users is questionable: " + numUsers);
      }
      if (numUsers == ISeriesNetServerFileShare.UNKNOWN) {
        ///ok = false;
        System.out.println("Warning: Num Users is unknown.");
      }

      if (desc.length() > 50) {
        ok = false;
        System.out.println("Description is longer than 50 chars");
      }

      if (path.trim().length() == 0 ||
          path.length() > 1024) {
        ok = false;
        System.out.println("Invalid path name length: " + path.length());
      }
      if (path.charAt(0) == ' ') {
        ok = false;
        System.out.println("path starts with a blank: |" + path + "|");
      }

      if (perms != ISeriesNetServerFileShare.READ_ONLY &&
          perms != ISeriesNetServerFileShare.READ_WRITE) {
        ok = false;
        System.out.println("Invalid permissions: " + perms);
      }
      if (textConv != ISeriesNetServerFileShare.ENABLED &&
          textConv != ISeriesNetServerFileShare.NOT_ENABLED &&
          textConv != ISeriesNetServerFileShare.ENABLED_AND_MIXED) {
        ok = false;
        System.out.println("Invalid textConv: " + textConv);
      }
      if (ccsid < 0) {
        ok = false;
        System.out.println("Invalid ccsid: " + ccsid);
      }
      for (int i=0; i<fileExts.length; i++) {
        if (fileExts[i].trim().length() == 0) {
          ok = false;
          System.out.println("Zero-length file extension");
        }
      }

      return ok;
    }


    /**
    Display and validate all attributes for all file shares.
    **/
    public void Var001()
    {
        try
        {
          for (int i=0; i<shareList_.length; i++)
          {
            ISeriesNetServerFileShare share = shareList_[i];
            if (DEBUG) displayAttributeValues(share);
            if (!validateAttributeValues(share)) {
              failed("Share has invalid attribute value(s).");
              return;
            }
            netserverPwr_.refresh(share);
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
    For a specific file share, change all its changeable attributes to different values, and verify that the changes took effect.
    **/
    public void Var002()
    {
      boolean ok = true;

      // Get all of the current attribute values of the share.
      String name_orig = share_.getName();
      String desc_orig = share_.getDescription();
      String path_orig = share_.getPath();
      int perm_orig = share_.getPermissions();
      int maxUsers_orig = share_.getMaximumNumberOfUsers();
      int curUsers_orig = share_.getCurrentNumberOfUsers();
      int convEnable_orig = share_.getTextConversionEnablement();
      int ccsid_orig = share_.getCcsidForTextConversion();
      String[] exts_orig = share_.getFileExtensions();

      if (DEBUG) {
        System.out.println("\nOriginal attributes:");
        displayAttributeValues(share_);
      }

      // Change the values to something else.

      // Note: The share name is not changeable.
      String desc_new = (desc_orig.equals("New desc") ? "Different desc" : "New Desc");
      String path_new = (path_orig.equals("/root") ? "/qibm" : "/root");
      int perm_new = (perm_orig == 1 ? 2 : 1);
      int maxUsers_new = (maxUsers_orig == -1 ? 1 : -1);
      int convEnable_new = (convEnable_orig == 0 ? 1 : 0);
      int ccsid_new = (ccsid_orig == 0 ? 37 : 0);

      String[] exts_new = new String[exts_orig.length+1];
      System.arraycopy(exts_orig, 0, exts_new, 0, exts_orig.length);
      exts_new[exts_new.length-1] = "BOOGIE";

      share_.setDescription(desc_new);
      share_.setPath(path_new);
      share_.setPermissions(perm_new);
      share_.setMaximumNumberOfUsers(maxUsers_new);
      share_.setTextConversionEnablement(convEnable_new);
      share_.setCcsidForTextConversion(ccsid_new);
      share_.setFileExtensions(exts_new);

      try {
        ///System.out.println ("About to commitChanges.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
        netserverPwr_.commitChanges(share_);
      }
      catch (Exception e) {
        e.printStackTrace();
        ///System.out.println ("Caught exception.  Press ENTER to continue."); try { System.in.read (); } catch (Exception exc) {};
      }

      if (DEBUG) {
        System.out.println("\nAttributes after sets:");
        displayAttributeValues(share_);
      }

      // BLOCK 1.
      // Verify the changes.

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 1: name"); }
      if (!share_.getDescription().equals(desc_new)) { ok = false; println("Block 1: desc"); }
      if (!share_.getPath().equals(path_new)) { ok = false; println("Block 1: path"); }
      if (share_.getPermissions() != perm_new) { ok = false; println("Block 1: perms"); }
      if (share_.getMaximumNumberOfUsers() != maxUsers_new) { ok = false; println("Block 1: maxUsers"); }
      if (share_.getCurrentNumberOfUsers() != curUsers_orig) { ok = false; println("Block 1: curUsers"); }
      if (share_.getTextConversionEnablement() != convEnable_new) { ok = false; println("Block 1: convEnable"); }
      if (share_.getCcsidForTextConversion() != ccsid_new) { ok = false; println("Block 1: ccsid"); }

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
      if (!share_.getPath().equals(path_new)) { ok = false; println("Block 2: path"); }
      if (share_.getPermissions() != perm_new) { ok = false; println("Block 2: perms"); }
      if (share_.getMaximumNumberOfUsers() != maxUsers_new) { ok = false; println("Block 2: maxUsers"); }
      if (share_.getCurrentNumberOfUsers() != curUsers_orig) { ok = false; println("Block 2: curUsers"); }
      if (share_.getTextConversionEnablement() != convEnable_new) { ok = false; println("Block 2: convEnable"); }
      if (share_.getCcsidForTextConversion() != ccsid_new) { ok = false; println("Block 2: ccsid"); }

      // Reset the attributes back to their original values.

      share_.setDescription(desc_orig);
      share_.setPath(path_orig);
      share_.setPermissions(perm_orig);
      share_.setMaximumNumberOfUsers(maxUsers_orig);
      share_.setTextConversionEnablement(convEnable_orig);
      share_.setCcsidForTextConversion(ccsid_orig);
      share_.setFileExtensions(exts_orig);

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
      if (!share_.getPath().equals(path_orig)) { ok = false; println("Block 3: path"); }
      if (share_.getPermissions() != perm_orig) { ok = false; println("Block 3: perms"); }
      if (share_.getMaximumNumberOfUsers() != maxUsers_orig) { ok = false; println("Block 3: maxUsers"); }
      if (share_.getCurrentNumberOfUsers() != curUsers_orig) { ok = false; println("Block 3: curUsers"); }
      if (share_.getTextConversionEnablement() != convEnable_orig) { ok = false; println("Block 3: convEnable"); }
      if (share_.getCcsidForTextConversion() != ccsid_orig) { ok = false; println("Block 3: ccsid"); }

      // BLOCK 4.
      // Do a refresh, and reverify the changes.

///      try { share_.refresh(); }
      try { netserverPwr_.refresh(share_); }
      catch (Exception e) { e.printStackTrace(); }

      if (DEBUG) {
        System.out.println("\nAttributes after refresh:");
        displayAttributeValues(share_);
      }

      if (!share_.getName().equals(name_orig)) { ok = false; println("Block 4: name"); }
      if (!share_.getDescription().equals(desc_orig)) { ok = false; println("Block 4: desc"); }
      if (!share_.getPath().equals(path_orig)) { ok = false; println("Block 4: path"); }
      if (share_.getPermissions() != perm_orig) { ok = false; println("Block 4: perms"); }
      if (share_.getMaximumNumberOfUsers() != maxUsers_orig) { ok = false; println("Block 4: maxUsers"); }
      if (share_.getCurrentNumberOfUsers() != curUsers_orig) { ok = false; println("Block 4: curUsers"); }
      if (share_.getTextConversionEnablement() != convEnable_orig) { ok = false; println("Block 4: convEnable"); }
      if (share_.getCcsidForTextConversion() != ccsid_orig) { ok = false; println("Block 4: ccsid"); }

      if (ok) succeeded();
      else failed();
    }

}
