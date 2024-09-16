///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSFileAttrTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.IFS;

import java.io.DataOutput;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.util.Hashtable;
import java.util.Date;
import com.ibm.as400.access.*;

import test.JCIFSUtility;

/**
Test file attribute methods such as length, modification date, data available,
and file pointer.
**/
public class IFSFileAttrTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSFileAttrTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }

/**
Constructor.
**/
  public IFSFileAttrTestcase (AS400 systemObject,
        String userid, 
        String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSFileAttrTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
    }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup(); 
  }




  void setPrivate(AS400 as400, String dirName)
  {
    String cmdString = "CHGAUT OBJ('"
                     + dirName
                     + "') USER(*PUBLIC) DTAAUT(*EXCLUDE) OBJAUT(*NONE)";

    String cmdString2 = "CHGAUT OBJ('"
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*NONE) OBJAUT(*NONE)";

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }
  }

  void setPublic(AS400 as400, String dirName)
  {
    String cmdString = "CHGAUT OBJ('"
                     + dirName
                     + "') USER(*PUBLIC) DTAAUT(*EXCLUDE) OBJAUT(*NONE)";

    String cmdString2 = "CHGAUT OBJ('"
                     + dirName
                     + "') USER("
                     + systemObject_.getUserId()
                     + ") DTAAUT(*RWX) OBJAUT(*ALL)";

    CommandCall cmd = new CommandCall(as400);
    try
    {
      cmd.setCommand(cmdString);
      cmd.run();
      cmd.setCommand(cmdString2);
      cmd.run();
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      output_.println("Unable to setup variation.");
    }
  }

/**
Ensure that IFSRandomAccessFile.getFilePointer() equals zero when a file is
opened, advances one position for every byte read, and equals the file length
at the end of file.
**/
  public void Var001()
  {
    byte[] data = { 0,1,2,3,4,5,6,7,8,9 };
    createFile(ifsPathName_, data);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "r");
      int bytesRead = 0;
      while (file.getFilePointer() == bytesRead)
      {
        int rc = file.read();
        if (rc == -1)
          break;
        else
          bytesRead++;
      }
      assertCondition(file.getFilePointer() == file.length());
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(ifsPathName_);
    }
  }

/**
Ensure that IFSRandomAccessFile.length() equals zero for an empty file and
increases by one for every byte written to the file.
**/
  public void Var002()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "a2";
    createFile(fileName);
    try
    {
    	
   	 StringBuffer sb = new StringBuffer(); 

     for (int i = 0; i < 32; i++) { 
           sb.append((char)('0'+i)); 
    }

     JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  fileName, sb.toString().getBytes("UTF-8")); 
     

      IFSRandomAccessFile file1 =
        new IFSRandomAccessFile(systemObject_, fileName, "r");
      
      assertCondition(file1.length() == 32, "length of "+fileName+" is "+file1.length());
      file1.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Ensure that IFSFile.length() equals zero for an empty file and increases by one
for every byte written to the file.
**/
  public void Var003()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }

    String fileName = ifsPathName_ + "a3";
    createFile(fileName);
    try
    {

      IFSFile file1 =
        new IFSFile(systemObject_, fileName);

   	 StringBuffer sb = new StringBuffer(); 

     for (int i = 0; i < 32; i++) { 
           sb.append((char)('0'+i)); 
    }

     JCIFSUtility.createFile(systemName_, userId_, encryptedPassword_,  
   		  fileName, sb.toString().getBytes("UTF-8")); 

      assertCondition(file1.length() == 32);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Ensure that IFSFileInputStream.available() equals the file length (for empty
files and non-empty files).
**/
  public void Var004()
  {
    if (isApplet_)
    {
      notApplicable();
      return;
    }
    String emptyName = ifsDirName_ + "Empty";
    createFile(emptyName);
    String nonEmptyName = ifsDirName_ + "NonEmpty";
    createFile(nonEmptyName, "abcdefg");
    try
    {

	boolean nonEmptyStream1AvailableFixed = false; 
      InputStream emptyStream1 =  getNonIFSInputStream(emptyName);
      IFSFileInputStream emptyStream2 =
        new IFSFileInputStream(systemObject_, emptyName);
      InputStream nonEmptyStream1 = getNonIFSInputStream(nonEmptyName);
      IFSFileInputStream nonEmptyStream2 =
        new IFSFileInputStream(systemObject_, nonEmptyName);
      int emptyStream1Available = emptyStream1.available();
      int emptyStream2Available = emptyStream2.available();
      int nonEmptyStream1Available = nonEmptyStream1.available();

      // The JDIFS has a query where it may return 0.  If this is the case, set the
      // value to 7, since we expected to see 7. 
      if (nonEmptyStream1Available == 0) {
	  nonEmptyStream1AvailableFixed = true; 
	  nonEmptyStream1Available = 7; 
      } 
      int nonEmptyStream2Available = nonEmptyStream2.available();
      emptyStream2.close();
      nonEmptyStream2.close();
      emptyStream1.close();
      nonEmptyStream1.close();

      assertCondition(  emptyStream1Available == emptyStream2Available  &&
             nonEmptyStream1Available == nonEmptyStream2Available, 
             "\nexpected emptyStream1Available == emptyStream2Available but got "+emptyStream1Available+"!="+emptyStream2Available+
             "\nexpected nonEmptyStream1Available == nonEmptyStream2Available but got "+nonEmptyStream1Available+"!="+nonEmptyStream2Available +
			" nonEmptyStream1AvailableFixed="+nonEmptyStream1AvailableFixed
             );
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(emptyName);
      deleteFile(nonEmptyName);
    }
  }

/**
Ensure that IFSFileInputStream.available() decreases as bytes are read from a
stream.
**/
  public void Var005()
  {
    String fileName = ifsPathName_ + "a5";
    createFile(fileName, "abcdefghijklmnopqrstuvwxyz");
    try
    {
      IFSFileInputStream in =
        new IFSFileInputStream(systemObject_, fileName);
      int availableBefore = 0;
      int availableAfter = 0;
      do
      {
        availableBefore = in.available();
        if (in.read() == -1)
          break;
        availableAfter = in.available();
      }
      while(availableBefore == availableAfter + 1);
      assertCondition(availableBefore == availableAfter &&
             availableBefore == 0);
      in.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

/**
Ensure that IFSFile.lastModified() is greater than zero and less than
the current time.
**/
  public void Var006()
  {
    String fileName = ifsPathName_ + "a6";
    createFile(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      long slack = 1000*60*2;  // allow for 2 minutes difference 

      // Use clock on server (getSysValTime) rather than client time
      long curSysValTime = getSysValTime(systemObject_); //Testcase.getSysValTime()
      long difference = Math.abs(file.lastModified() - curSysValTime);
      if ((file.lastModified() > 0) && (difference < slack))
         succeeded();
      else
         failed("incorrect lastModified value");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }


  // Ensure the read-only attribute is off on a new file
  public void Var007()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest7";
    createFile(fileName, "Data for IFSFileAttrTest7");
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.isReadOnly())
         failed("new file has read-only attribute on");
      else
         succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

   // Ensure we can set the read-only attribute
  public void Var008()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest8";
    IFSFile file  = null;
    IFSFile file2 = null;
    try
    {
      createFile(fileName, "Data for IFSFileAttrTest8");
      file  = new IFSFile(systemObject_, fileName);
      file2 = new IFSFile(systemObject_, fileName);
      System.out.println("Calling file.setReadOnly"); 
      file.setReadOnly();

      System.out.println("Calling file2.isReadOnly"); 
      if (file2.isReadOnly())
         succeeded();
      else
         failed("file "+fileName+" does not have read-only attribute set");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file != null) {
        try { file.setReadOnly(false); } catch (Exception e) {}
        deleteFile(fileName);
      }
    }
  }

  // Try to set the readonly attribute on a directory
  public void Var009()
  {
    String fileName  = ifsPathName_ + "IFSFileAttrTest9Dir";
    createDirectory(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.setReadOnly())
      {
         if (file.isReadOnly())
         {
            file.setReadOnly(false);
            if (file.isReadOnly())
               failed("could not turn readonly back off");
            else
               succeeded();
         }
         else
            failed("could not turn readonly on");
      }
      else
         failed("could not turn readonly on");
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
       IFSFile file2 = new IFSFile(systemObject_, fileName);
       file2.delete();
    }
    catch (Exception e) {System.out.println("cleanup failed"); e.printStackTrace(); }
  }

  // Turn the read-only attribute on then turn it back off
  public void Var010()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest10";
    createFile(fileName, "Data for IFSFileAttrTest10");
    try
    {
       IFSFile file1 = new IFSFile(systemObject_, fileName);
       IFSFile file2 = new IFSFile(systemObject_, fileName);
       IFSFile file3 = new IFSFile(systemObject_, fileName);

       file1.setReadOnly(true);
       if (file2.isReadOnly())
       {
          file1.setReadOnly(false);
          if (file3.isReadOnly())
             failed("could not turn off readonly bit");
          else
             succeeded();
       }
       else
          failed("initial set of readonly bit failed");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

  // Check the readonly attribute on something that does not exist
  public void Var011()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest11_isNotThere";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.isReadOnly())
         failed("file that does not exist has read-only attribute on");
      else
         succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }




   // Ensure the hidden attribute is off on a new file
  public void Var012()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest12";
    createFile(fileName, "Data for IFSFileAttrTest12");
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.isHidden())
         failed("new file has hidden attribute on");
      else
         succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

   // Ensure we can set the hidden attribute
  public void Var013()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest13";
    createFile(fileName, "Data for IFSFileAttrTest13");
    try
    {
      IFSFile file  = new IFSFile(systemObject_, fileName);
      IFSFile file2 = new IFSFile(systemObject_, fileName);
      file.setHidden();

      if (file2.isHidden())
         succeeded();
      else
         failed("file does not have hidden attribute set");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

  // Try to set the hidden attribute on a directory
  public void Var014()
  {
    String fileName  = ifsPathName_ + "IFSFileAttrTest14Dir";
    createDirectory(fileName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.setHidden())
      {
         if (file.isHidden())
         {
            file.setHidden(false);
            if (file.isHidden())
               failed("could not turn hidden back off");
            else
               succeeded();
         }
         else
            failed("could not turn hidden on");
      }
      else
         failed("could not turn hidden on");
    }
    catch(Exception e)
    {
      failed(e);
    }

    try
    {
       IFSFile file2 = new IFSFile(systemObject_, fileName);
       file2.delete();
    }
    catch (Exception e) {System.out.println("cleanup failed"); e.printStackTrace(); }
  }

  // Turn the hidden attribute on then turn it back off
  public void Var015()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest15";
    createFile(fileName, "Data for IFSFileAttrTest15");
    try
    {
       IFSFile file1 = new IFSFile(systemObject_, fileName);
       IFSFile file2 = new IFSFile(systemObject_, fileName);
       IFSFile file3 = new IFSFile(systemObject_, fileName);

       file1.setHidden(true);
       if (file2.isHidden())
       {
          file1.setHidden(false);
          if (file3.isHidden())
             failed("could not turn off hidden bit");
          else
             succeeded();
       }
       else
          failed("initial set of hidden bit failed");
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(fileName);
  }

  // Check the hidden attribute on something that does not exist
  public void Var016()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest16_isNotThere";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      if (file.isHidden())
         failed("file that does not exist has hidden attribute on");
      else
         succeeded();
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }


    // try isReadOnly() when not authorized to object
    public void Var017()
    {

      if (pwrSys_ == null)
         failed("Power user not specified so could not run not-authorized test case");
      else
      {
         try
         {
            String dirName  = ifsDirName_ + "IFSFileAttr17Dir";
            String fileName = dirName + "/IFSFileAttr17";
            createDirectory(pwrSys_, dirName);
            setPrivate(pwrSys_, dirName);

            try
            {
               IFSFile file = new IFSFile(systemObject_, fileName);
               file.isReadOnly();
               failed("no exception: isReadOnly when not authorized to object ");
            }
            catch (Exception e)
            {
               if (exceptionIs(e, "AS400SecurityException"))
                  succeeded();
               else
                  failed(e, "wrong exception");
            }
            setPublic(pwrSys_, dirName);
            deleteFile(fileName);
            deleteDirectory(dirName);
         }
         catch(Exception e)
         {
           failed(e);
         }
      }
    }


    // try setReadOnly() when not authorized to object
    public void Var018()
    {

      if (pwrSys_ == null)
         failed("Power user not specified so could not run not-authorized test case");
      else
      {
         try
         {
            String dirName  = ifsDirName_ + "IFSFileAttr18Dir";
            String fileName = dirName + "/IFSFileAttr18";
            createDirectory(pwrSys_, dirName);
            setPrivate(pwrSys_, dirName);

            try
            {
               IFSFile file = new IFSFile(systemObject_, fileName);
               file.setReadOnly(true);
               failed("no exception: isReadOnly when not authorized to object ");
            }
            catch (Exception e)
            {
               if (exceptionIs(e, "ExtendedIOException"))
                  succeeded();
               else
                  failed(e, "wrong exception");
            }
            setPublic(pwrSys_, dirName);
            deleteFile(fileName);
            deleteDirectory(dirName);
         }
         catch(Exception e)
         {
           failed(e);
         }
      }
    }

    // try isHidden() when not authorized to object
    public void Var019()
    {

      if (pwrSys_ == null)
         failed("Power user not specified so could not run not-authorized test case");
      else
      {
         try
         {
            String dirName  = ifsDirName_ + "IFSFileAttr19Dir";
            String fileName = dirName + "/IFSFileAttr19";
            createDirectory(pwrSys_, dirName);
            setPrivate(pwrSys_, dirName);

            try
            {
               IFSFile file = new IFSFile(systemObject_, fileName);
               file.isHidden();
               failed("no exception: isHidden when not authorized to object ");
            }
            catch (Exception e)
            {
               if (exceptionIs(e, "AS400SecurityException"))
                  succeeded();
               else
                  failed(e, "wrong exception trying isHidden when not authorized to object ");
            }
            setPublic(pwrSys_, dirName);
            deleteFile(fileName);
            deleteDirectory(dirName);
         }
         catch(Exception e)
         {
           failed(e);
         }
      }
    }


    // try setHidden() when not authorized to object
    public void Var020()
    {

      if (pwrSys_ == null)
         failed("Power user not specified so could not run not-authorized test case");
      else
      {
         try
         {
            String dirName  = ifsDirName_ + "IFSFileAttr20Dir";
            String fileName = dirName + "/IFSFileAttr20";
            createDirectory(pwrSys_, dirName);
            setPrivate(pwrSys_, dirName);

            try
            {
               IFSFile file = new IFSFile(systemObject_, fileName);
               file.setHidden(true);
               failed("no exception: isReadOnly when not authorized to object ");
            }
            catch (Exception e)
            {
               if (exceptionIs(e, "ExtendedIOException"))
                  succeeded();
               else
                  failed(e, "wrong exception setting hidden when not authorized to object ");
            }
            setPublic(pwrSys_, dirName);
            deleteFile(fileName);
            deleteDirectory(dirName);
         }
         catch(Exception e)
         {
           failed(e);
         }
      }
    }



  // Turn the hidden attribute, then then readonly attribute, then
  // make sure hidden is still on.
  public void Var021()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest21";
    IFSFile file1 = null;
    IFSFile file2 = null;
    IFSFile file3 = null;
    try
    {
       createFile(fileName, "Data for IFSFileAttrTest21");
       file1 = new IFSFile(systemObject_, fileName);
       file2 = new IFSFile(systemObject_, fileName);
       file3 = new IFSFile(systemObject_, fileName);

       file1.setHidden(true);
       if (file1.isHidden())
       {
          file2.setReadOnly(true);
          if (file2.isReadOnly())
          {
             if (file3.isHidden())
                succeeded();
             else
                failed("hidden bit no longer on");
          }
          else
             failed("initial set of readonly bit failed");
       }
       else
          failed("initial set of hidden bit failed");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file2 != null) {
        try { file2.setReadOnly(false); } catch (Exception e) {}
        deleteFile(fileName);
      }
    }
  }




  // Get readonly bit twice, the second time it should come out of the cache.
  // We know it will come out of the cache because we will set it
  // it a different object but the first object should not know about
  // the change.
  public void Var022()
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest22";
    createFile(fileName, "Data for IFSFileAttrTest22");
    try
    {
       IFSFile file1 = new IFSFile(systemObject_, fileName);
       IFSFile file2 = new IFSFile(systemObject_, fileName);

       if (! file1.isHidden())
       {
          file2.setHidden(true);
          if (file2.isHidden())
          {
             if (file1.isHidden())
                failed("hidden bit not taken out of cache");
             else
                succeeded();
          }
          else
             failed("could not turn hidden on");
       }
       else
          failed("hidden bit of new file on");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }



/**
Test creation and access date/times
@D1 new variation
**/
  public void Var023()
  {

    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream (systemObject_, "/JavaTest.aaa");
      os.write(0);
      os.close();

      IFSFile file = new IFSFile(systemObject_, "/JavaTest.aaa");
      Date d1 = new Date(file.created());
      Date d2 = new Date(file.lastAccessed());

      file.delete();

      Thread.sleep(10 * 1000);

      IFSFileOutputStream os2 = new IFSFileOutputStream (systemObject_, "/JavaTest.aaa");
      os2.write(0);
      os2.write(0);
      os2.write(0);
      os2.close();

      IFSFile file2 = new IFSFile(systemObject_, "/JavaTest.aaa");
      Date d1b = new Date(file2.created());
      Date d2b = new Date(file2.lastAccessed());

      if  (d1.equals(d1b) && d2.equals(d2b))
      {
         failed("in new list dates do match");
         System.out.println("   " + d1  + "  " + d1.getTime());
         System.out.println("   " + d1b + "  " + d1b.getTime());
         System.out.println("   " + d2  + "  " + d2.getTime());
         System.out.println("   " + d2b + "  " + d2b.getTime());
      }
      else
      {
         try
         {
            IFSFile f3 = new IFSFile();
            f3.created();
            failed("no Exception");
         }
         catch (Exception e24)
         {
            succeeded();
         }
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


/**
Ensure that IFSFile.getOwnerId() returns the owner of the file.
**/
  public void Var024()
  {
    String fileName = ifsPathName_ + "a24";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      file.createNewFile();
      int owner = file.getOwnerId();
      User user = new User(systemObject_, systemObject_.getUserId());
      int uid = user.getUserIDNumber();
      assertCondition(owner==uid);
    }
    catch(Exception e)
    {
      failed(e);
    }
    catch (NoClassDefFoundError e)
    { // We might be testing a stripped jar produced by JarMaker.
      if (e.getMessage().indexOf("User") != -1) {
        failed("Class not found: " + e.getMessage());
      }
      else failed(e, "Unexpected exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }


/**
Ensure that IFSFile.getOwnerId() returns -1 when invoked for a directory.
**/
  public void Var025()
  {
    String dirName  = ifsDirName_ + "IFSFileAttr25Dir";
    createDirectory(pwrSys_, dirName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      int owner = file.getOwnerId();
      assertCondition(owner == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteDirectory(dirName);
    }
  }


/**
Ensure that IFSFile.getOwnerId() returns -1 if file does not exist.
**/
  public void Var026()
  {
    String fileName = ifsPathName_ + "a24";
    try
    {
      deleteFile(fileName);
      IFSFile file = new IFSFile(systemObject_, fileName);
      int owner = file.getOwnerId();
      assertCondition(owner == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
Ensure that IFSFile.getOwnerUID() returns the owner of the file.
**/
  public void Var027()
  {
    if (!checkMethodExists("getOwnerUID", null)) {
      notApplicable("No method named getOwnerUID()");
      return;
    }

    String fileName = ifsPathName_ + "a27";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      file.createNewFile();
      long owner = file.getOwnerUID();
      User user = new User(systemObject_, systemObject_.getUserId());
      long uid = (long)user.getUserIDNumber() & 0x0FFFFFFFFL;
      // Note: This variation may fail when running from an AIX client.
      // For some reason, User.getUserIDNumber() wants to report the
      // local AIX uid, rather than the uid of the iSeries user profile.
      assertCondition(owner==uid);
    }
    catch(Exception e)
    {
      failed(e);
    }
    catch (NoClassDefFoundError e)
    { // We might be testing a stripped jar produced by JarMaker.
      if (e.getMessage().indexOf("User") != -1) {
        failed("Class not found: " + e.getMessage());
      }
      else failed(e, "Unexpected exception");
    }
    finally
    {
      deleteFile(fileName);
    }
  }


/**
Ensure that IFSFile.getOwnerUID() returns -1 when invoked for a directory.
**/
  public void Var028()
  {
    if (!checkMethodExists("getOwnerUID", null)) {
      notApplicable("No method named getOwnerUID()");
      return;
    }

    String dirName  = ifsDirName_ + "IFSFileAttr28Dir";
    createDirectory(pwrSys_, dirName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      long owner = file.getOwnerUID();
      assertCondition(owner == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteDirectory(dirName);
    }
  }


/**
Ensure that IFSFile.getOwnerUID() returns -1 if file does not exist.
**/
  public void Var029()
  {
    if (!checkMethodExists("getOwnerUID", null)) {
      notApplicable("No method named getOwnerUID()");
      return;
    }

    String fileName = ifsPathName_ + "a29";
    try
    {
      deleteFile(fileName);
      IFSFile file = new IFSFile(systemObject_, fileName);
      long owner = file.getOwnerUID();
      assertCondition(owner == -1);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }


/**
Ensure that IFSFile.length() returns 0 if file does not exist.
**/
  public void Var030()
  {
    String fileName = ifsPathName_ + "a30";
    try
    {
      deleteFile(fileName);
      IFSFile file = new IFSFile(systemObject_, fileName);
      long length = file.length();
      assertCondition(length == 0);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }



/**
Ensure that IFSFile.isSymbolicLink() returns true if file is symbolic link, false if not.
Note: If system is pre-V5R3, we expect isSymbolicLink() to always return false.
**/
  public void Var031()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

    String fileName = "/File31";
    String symlinkName = fileName + ".symlink";
    String dirName = "/Dir31";
    createFile(fileName, "Data for IFSFileAttrTest31");
    createDirectory(dirName);
    CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, fileName);
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
      IFSFile file2 = new IFSFile(systemObject_, symlinkName);
      IFSFile file3 = new IFSFile(systemObject_, dirName);
///      IFSFile file2 = new IFSFile(systemObject_, "/File31.symlink");
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///      if (DEBUG) output_.println("file1.isSymbolicLink(): " + file1.isSymbolicLink() +
///                                 " ; file2.isSymbolicLink(): " + file2.isSymbolicLink());
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file3.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file3.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() +","+
///                      file3.isDirectory() );
      if (getSystemVRM() >= VRM_V5R3M0) {  // system is V5R3 or later
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == true &&
                        file3.isSymbolicLink() == false &&
                        file1.isFile() == true &&
                        file2.isFile() == true &&
                        file3.isFile() == false &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false &&
                        file3.isDirectory() == true);
      }
      else { // system is pre-V5R3, so isSymbolicLink() always reports false.
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == false &&
                        file3.isSymbolicLink() == false &&
                        file1.isFile() == true &&
                        file2.isFile() == true &&
                        file3.isFile() == false &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false &&
                        file3.isDirectory() == true);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(symlinkName);
      deleteFile(fileName);
      deleteDirectory(dirName);
    }
  }



/**
Ensure that IFSFile.isSymbolicLink() returns true if file is symbolic link, false if not.
Note: If system is pre-V5R3, we expect isSymbolicLink() to always return false.
The linked-to file is a directory.
**/
  public void Var032()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

    String fileName = "/Dir32";
    String symlinkName = fileName + ".symlink";
    createDirectory(fileName);
    CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, fileName);
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
      IFSFile file2 = new IFSFile(systemObject_, symlinkName);
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
      ///boolean result = file1.isSymbolicLink();
      ///System.out.println(result);
///      if (DEBUG) output_.println("file1.isSymbolicLink(): " + file1.isSymbolicLink() +
///                                 " ; file2.isSymbolicLink(): " + file2.isSymbolicLink());
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() );
      if (getSystemVRM() >= VRM_V5R3M0) {  // system is V5R3 or later
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == true &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == true &&
                        file2.isDirectory() == true);
      }
      else { // system is pre-V5R3, so isSymbolicLink() always reports false.
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == false &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == true &&
                        file2.isDirectory() == true);
      }
      try
      {
        // RMVLNK - to clean up the link created earlier
        String cmdStr = "RMVLNK OBJLNK('" + symlinkName+ "')";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }

    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteDirectory(symlinkName);
      deleteDirectory(fileName);
    }
  }



/**
Ensure that IFSFile.isSymbolicLink() returns true if file is symbolic link, false if not.
Note: If system is pre-V5R3, we expect isSymbolicLink() to always return false.
Create the IFSFile object via IFSFile.listFiles() against the parent directory.
**/
  public void Var033()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

///    String filePath = ifsPathName_ + "IFSFileAttrTest33";
    String filePath = "/File33";
    String symlinkPath = filePath + ".symlink";
    createFile(filePath, "Data for IFSFileAttrTest33");
    CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, filePath);
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + filePath + "') NEWLNK('" + symlinkPath + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
      IFSFile rootDir = new IFSFile(systemObject_, "/");
      IFSFile[] files = rootDir.listFiles("File33*");
      ///System.out.println("IFSFileAttrTestcase.Var033: Number of files returned == " + files.length);
      IFSFile file2 = null;
      ///System.out.println("IFSFileAttrTestcase.Var033: Seeking path |" + symlinkPath + "| ...");
      for (int i=0; i<files.length && file2 == null; i++) {
        ///System.out.println("IFSFileAttrTestcase.Var033: files[i].getPath() == |" + files[i].getPath() + "|");
        if (files[i].getPath().equals(symlinkPath)) file2 = files[i];
      }
      if (file2 == null) {
        failed("File " + symlinkPath + " was not found by listFiles().");
        return;
      }
///      IFSFile file2 = new IFSFile(systemObject_, symlinkPath);
///      IFSFile file2 = new IFSFile(systemObject_, "/File33.symlin*");
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
      // boolean result = file2.isSymbolicLink();
///      if (DEBUG) output_.println("file1.isSymbolicLink(): " + file1.isSymbolicLink() +
///                                 " ; file2.isSymbolicLink(): " + file2.isSymbolicLink());
      ///System.out.println ("Press ENTER to continue"); try { System.in.read (); } catch (Exception e) {};
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() );
      if (getSystemVRM() >= VRM_V5R3M0) {  // system is V5R3 or later
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == true &&
                        file1.isFile() == true &&
                        file2.isFile() == true &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false);
      }
      else { // system is pre-V5R3, so isSymbolicLink() always reports false.
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == false &&
                        file1.isFile() == true &&
                        file2.isFile() == true &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(symlinkPath);
      deleteFile(filePath);
    }
  }



/**
For a nonexistent symlink, ensure that IFSFile.isSymbolicLink() returns false.
**/
  public void Var034()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

    String fileName = "/File34";
    String symlinkName = fileName + ".symlink";
    createFile(fileName, "Data for IFSFileAttrTest34");
    // CommandCall cmd = new CommandCall(systemObject_);
    try
    {
      IFSFile file1 = new IFSFile(systemObject_, fileName);
/*
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
*/
      IFSFile file2 = new IFSFile(systemObject_, symlinkName);
      ///boolean result = file2.isSymbolicLink();
      ///System.out.println(result);
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() );
      assertCondition(file1.isSymbolicLink() == false &&
                      file2.isSymbolicLink() == false &&
                      file1.isFile() == true &&
                      file2.isFile() == false &&
                      file1.isDirectory() == false &&
                      file2.isDirectory() == false);
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(symlinkName);
      deleteFile(fileName);
    }
  }



/**
For a symlink to a nonexistent file, ensure that IFSFile.isSymbolicLink() returns true.
Note: If system is pre-V5R3, we expect isSymbolicLink() to always return false.
**/
  public void Var035()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

    String fileName = "/File35";
    String symlinkName = fileName + ".symlink";
    createFile(fileName, "Data for IFSFileAttrTest35");
    CommandCall cmd = new CommandCall(systemObject_);
    IFSFile file1 = null;
    IFSFile file2 = null;
   try
    {
      file1 = new IFSFile(systemObject_, fileName);
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
      file2 = new IFSFile(systemObject_, symlinkName);

      // Delete the linked-to file.
      file1.delete();

      ///boolean result = file2.isSymbolicLink();
      ///System.out.println(result);
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() );
      if (getSystemVRM() >= VRM_V5R3M0) {  // system is V5R3 or later
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == true &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false);
      }
      else { // system is pre-V5R3, so isSymbolicLink() always reports false.
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == false &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == false &&
                        file2.isDirectory() == false);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file2 != null) try { file2.delete(); } catch (Exception e) {e.printStackTrace();}
      deleteFile(fileName);
    }
  }



/**
Ensure that IFSFile.isSymbolicLink() returns false if file is the root directory, true if file is symlink to root directory.
Note: If system is pre-V5R3, we expect isSymbolicLink() to always return false.
**/
  public void Var036()
  {
    if (!checkMethodExists("isSymbolicLink", null)) {
      notApplicable("No method named isSymbolicLink()");
      return;
    }

    String fileName = "/";
    String symlinkName = "/SymlinkToRoot";
    CommandCall cmd = new CommandCall(systemObject_);
    IFSFile file1 = null;
    IFSFile file2 = null;
    try
    {
      file1 = new IFSFile(systemObject_, fileName);
      // Create a symlink to the file.
      try
      {
        String cmdStr = "ADDLNK OBJ('" + fileName + "') NEWLNK('" + symlinkName + "') LNKTYPE(*SYMBOLIC)";
        cmd.run(cmdStr);
      }
      catch(Exception e)
      {
        e.printStackTrace(output_);
        failed("Unable to create symbolic link.");
        return;
      }
      file2 = new IFSFile(systemObject_, symlinkName);
///      System.out.println(file1.isSymbolicLink() +","+
///                      file2.isSymbolicLink() +","+
///                      file1.isFile() +","+
///                      file2.isFile() +","+
///                      file1.isDirectory() +","+
///                      file2.isDirectory() );
      if (getSystemVRM() >= VRM_V5R3M0) {  // system is V5R3 or later
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == true &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == true &&
                        file2.isDirectory() == true);
      }
      else { // system is pre-V5R3, so isSymbolicLink() always reports false.
        assertCondition(file1.isSymbolicLink() == false &&
                        file2.isSymbolicLink() == false &&
                        file1.isFile() == false &&
                        file2.isFile() == false &&
                        file1.isDirectory() == true &&
                        file2.isDirectory() == true);
      }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      if (file2 != null) try { file2.delete(); } catch (Exception e) {e.printStackTrace();}
    }
  }


/**
Ensure that IFSFile.getOwnerName() returns the owner of the file.
**/
  public void Var037()
  {
    if (!checkMethodExists("getOwnerName", null)) {
      notApplicable("No method named getOwnerName()");
      return;
    }

    String fileName = ifsPathName_ + "a37";
    try
    {
      IFSFile file = new IFSFile(systemObject_, fileName);
      file.createNewFile();
      String owner = file.getOwnerName();
      assertCondition(owner != null && owner.equals(systemObject_.getUserId()));
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }


/**
Ensure that IFSFile.getOwnerName() returns "" when invoked for a directory.
..
In April 2017 -- not returns name
**/
  public void Var038()
  {
    if (!checkMethodExists("getOwnerName", null)) {
      notApplicable("No method named getOwnerName()");
      return;
    }

    String dirName  = ifsDirName_ + "IFSFileAttr38Dir";
    createDirectory(pwrSys_, dirName);
    try
    {
      IFSFile file = new IFSFile(systemObject_, dirName);
      String owner = file.getOwnerName();
      assertCondition(owner != null && owner.equals(pwrSys_.getUserId()), "Update April 2017 -- owner is '"+owner+"' sb '"+systemObject_.getUserId()+"' for '"+dirName+"'");

      // assertCondition(owner == "", "owner is '"+owner+"' sb '' for '"+dirName+"'");
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteDirectory(dirName);
    }
  }


/**
Ensure that IFSFile.getOwnerName() throws exception if file does not exist.
**/
  public void Var039()
  {
    if (!checkMethodExists("getOwnerName", null)) {
      notApplicable("No method named getOwnerName()");
      return;
    }

    String fileName = ifsPathName_ + "a39xx";
    try
    {
      deleteFile(fileName);
      IFSFile file = new IFSFile(systemObject_, fileName);
      String owner = file.getOwnerName();
      failed("No exception: getOwnerName() called against a nonexistent file retrieve owner is "+owner+" for "+fileName);
    }
    catch (Exception e)
    {
      if (exceptionIs(e, "ExtendedIOException"))
        succeeded();
      else
        failed(e, "Wrong exception for getOwnerName() called against a nonexistent file");
    }
  }

  // Verify IFSFile.clearCachedAttributes() clears cache attributes @D2A
  public void Var040()                                           // @D2A
  {
    String fileName = ifsPathName_ + "IFSFileAttrTest40";
    createFile(fileName, "Data for IFSFileAttrTest40");
    try
    {
       IFSFile file1 = new IFSFile(systemObject_, fileName);
       IFSFile file2 = new IFSFile(systemObject_, fileName);

       boolean isHid1Before = file1.isHidden();        //Should be false
       boolean isHid2Before = file2.isHidden();        //Should be false

       file2.setHidden();

       // file1 still using cache values
       boolean isHid1After1 = file1.isHidden();        //Should be false
       boolean isHid2After1 = file2.isHidden();        //Should be true

       // Clear file1 cache so subsequent isHidden() will read from server
       file1.clearCachedAttributes();

       // Both file1 and file2 should now be true
       boolean isHid1After2 = file1.isHidden();        //Should be true
       boolean isHid2After2 = file2.isHidden();        //Should be true

       if ((isHid1Before == false) && (isHid2Before == false) &&
           (isHid1After1 == false) && (isHid2After1 == true) &&
           (isHid1After2 == true)  && (isHid2After2 == true))
       {
         succeeded();
       }
       else
       {
         System.out.println("isHid1Before = "+isHid1Before);
         System.out.println("isHid2Before = "+isHid2Before);
         System.out.println("isHid1After1 = "+isHid1After1);
         System.out.println("isHid2After1 = "+isHid2After1);
         System.out.println("isHid1After2 = "+isHid1After2);
         System.out.println("isHid2After2 = "+isHid2After2);
         failed("IFSFile.clearCachedAttributes may have failed");
       }
    }
    catch(Exception e)
    {
      failed(e);
    }
    finally
    {
      deleteFile(fileName);
    }
  }

  public void Var041()    {
	  String directoryName = ifsPathName_ + "IFSFileAttrTest41";
	  IFSFile file = null; 
	  try {
		  file = new IFSFile(pwrSys_, directoryName);
		  file.mkdir();
		  UserList userList = new UserList(pwrSys_);
		  String currentUser = pwrSys_.getUserId(); 
		  User[] users = userList.getUsers(0, 40);
		  Permission permision = file.getPermission();
		  for(int i =0; i<users.length; i++) {
		      String userName = users[i].getName(); 
		      if (!userName.equals(currentUser)) {
			  try { 
			      permision.addAuthorizedUser(userName);
			  } catch (com.ibm.as400.access.ExtendedIllegalArgumentException ez) {
			      System.out.println("Warning:  unable to add "+userName); 
			  } 
		      }
		  }
		  permision.commit();
		  FileAttributes m_fileAttributes = new FileAttributes(pwrSys_, directoryName, false);
		  int format = m_fileAttributes.getDirectoryFormat();
		  assertCondition(format==FileAttributes.DIRECTORY_FORMAT_TYPE2, "format is not type 2");
	  } catch (Exception e) {
		  failed(e);
	  } finally {
	      try { 
		  if (file != null) file.delete();
	      } catch (Exception e) {
		  System.out.println("Warning: exception deleting file");
		  e.printStackTrace(); 
	      } 
	  } 		  
  }
  


  // Verifies that a method exists in class IFSFile.  Returns false if not found.
  static boolean checkMethodExists(String methodName, Class[] parmTypes)
  {
    try {
      Class.forName("com.ibm.as400.access.IFSFile").getDeclaredMethod(methodName, parmTypes);
    }
    catch (NoSuchMethodException e) {
      return false;
    }
    catch (ClassNotFoundException e) { // this will never happen
      e.printStackTrace();
      return false;
    }
    return true;
  }

}



