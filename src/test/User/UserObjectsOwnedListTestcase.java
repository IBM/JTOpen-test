///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserObjectsOwnedListTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import java.util.Random;

import com.ibm.as400.access.*;

import test.PasswordVault;
import test.Testcase;


/**
 * Test cases for the ObjectReferences class.
 */
public class UserObjectsOwnedListTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserObjectsOwnedListTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
  private static final boolean DEBUG = false;

  private int numObjectsCreated_ = 0;//random number to create objects


  /**
   Performs setup needed before running variations.

   @exception Exception If an exception occurs.
   **/


  protected void setup() throws Exception
  {
    AS400 system = null;
    try
    {

      /*Create a user profile and some random number of objects that belong
       to the profile */

      CommandCall cmd = new CommandCall(pwrSys_);
      system = new AS400(systemObject_.getSystemName(), "USRTEST" , "user23ts".toCharArray());
      cmd.run("QSYS/CRTUSRPRF USRPRF(USRTEST)  PASSWORD(user23ts)  USRCLS(*USER)");
      cmd.run("QSYS/CHGAUT OBJ('/') USER(USRTEST) DTAAUT(*RWX) OBJAUT(*ALL)   "); 
      IFSRandomAccessFile raf1 = new IFSRandomAccessFile(system, "/usertest1.txt", "rw");
      IFSRandomAccessFile raf2 = new IFSRandomAccessFile(system, "/usertest2.txt", "rw");
      IFSRandomAccessFile raf3 = new IFSRandomAccessFile(system, "/usertest3.txt", "rw");

      raf1.close(); 
      raf2.close(); 
      raf3.close(); 
      
      CommandCall cmd1 = new CommandCall(system);

      Random rand = new Random();
      numObjectsCreated_ = rand.nextInt(5);

      int i = 0;
      for(i=0 ; i<numObjectsCreated_ ;i++)
      {
        cmd1.run("QSYS/CRTLIB LIB(USRLBTS" + Integer.toString(i)+ ") TEXT('UserObjectsOwnedListTestcase lib')");
      }

    }
    catch(Exception e)
    {
      System.out.println("Unable to complete setup some variations will fail.");
      e.printStackTrace();
      return;
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }

  }

  protected void cleanup() throws Exception
  {
    // Delete the library and the files
    try
    {
      CommandCall cmd = new CommandCall(pwrSys_);

      for(int i=0 ; i<=numObjectsCreated_ ;i++)
      {
	  deleteLibrary(cmd,"USRLBTS" + Integer.toString(i)); 
      }

      IFSFile file1 = new IFSFile(pwrSys_, "/usertest1.txt");
      file1.delete();
      IFSFile file2 = new IFSFile(pwrSys_, "/usertest2.txt");
      file2.delete();
      IFSFile file3 = new IFSFile(pwrSys_, "/usertest3.txt");
      file3.delete();

      if (!cmd.run("QSYS/DLTUSRPRF USRPRF(USRTEST) OWNOBJOPT(*DLT)"))
      {
        AS400Message[] msgs = cmd.getMessageList();
        for (int i=0; i<msgs.length; i++)
        {
          System.out.println(msgs[i].getText());
        }
        System.out.println("Failed to delete profile USRTEST.  Manually delete profile on system.");
      }
      else System.out.println("Deleted profile USRTEST, cleanup done.");
    }
    catch (Exception e)
    {
      System.out.println("Cleanup failed.");
      e.printStackTrace();
    }

  }



  /**
   * <p>Test: Successful invocation as shown on the example code in the javadoc.
   *
   * <p>Result: Test must succeed.
   */
  public void Var001()
  {
    AS400 system = null;
    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    try
    {
      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
 PasswordVault.clearPassword(charPassword); 
 UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      System.out.println(list1);

      if (DEBUG)
      {
        for (int i=0; i<entries1.length; ++i)
        {
          System.out.println("Entry[" + i + "/"+entries1.length + "]= " + entries1[i]);
        }
      }

      list1.setSelectionObjectRelation(UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);
      entries1 = list1.getObjectList();
      System.out.println(list1);

      if (DEBUG)
      {
        for (int i=0; i<entries1.length; ++i)
        {
          System.out.println("Entry[" + i + "/" + entries1.length + "]= " + entries1[i]);
        }
      }

      succeeded();

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }


  /**
   * <p>Test: Call  UserObjectsOwnedList ::  UserObjectsOwnedList(null, String userName)
   * with invalid parameter 'null'.
   *
   * <p>Result: Verify a NullPointerException is thrown.
   */
  public void Var002()
  {
    try
    {

      UserObjectsOwnedList list1 = new UserObjectsOwnedList(null, "JAVA");
      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }


  /**
   * <p>Test: Call  UserObjectsOwnedList ::  UserObjectsOwnedList(AS400 system, null)
   * with invalid parameter 'null'.
   *
   * <p>Result: Verify a NullPointerException is thrown.
   */
  public void Var003()
  {
    AS400 system = null;
    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    try
    {
      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
 PasswordVault.clearPassword(charPassword); 
      UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, null);

      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "userName");
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }


  /**
   * <p>Test: Call  UserObjectsOwnedList :: UserObjectsOwnedList(null, String userName, int selectionFileSystem, int selectionObjectRelation)
   * with invalid parameter 'null'.
   *
   * <p>Result: Verify a NullPointerException is thrown.
   */
  public void Var004()
  {
    try
    {

      UserObjectsOwnedList list1 = new UserObjectsOwnedList(null, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "system");
    }
  }



  /**
   * <p>Test: Call  UserObjectsOwnedList :: UserObjectsOwnedList(AS400 system, null, int selectionFileSystem, int selectionObjectRelation)
   * with invalid parameter 'null'.
   *
   * <p>Result: Verify a NullPointerException is thrown.
   */
  public void Var005()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {

      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
      UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, null, UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "userName");
    }
    finally
    {
 PasswordVault.clearPassword(charPassword); 
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList :: UserObjectsOwnedList(AS400 system, String userName, int selectionFileSystem, int selectionObjectRelation)
   *  using SELECTION_FILE_SYSTEM_DIRECTORY and  SELECTION_OBJECT_RELATION_AUTHORIZED.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var006()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {

      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
  PasswordVault.clearPassword(charPassword); 
     UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED);
      succeeded("list1="+list1);

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList :: UserObjectsOwnedList(AS400 system, String userName, int selectionFileSystem, int selectionObjectRelation)
   *  using SELECTION_FILE_SYSTEM_LIBRARY and SELECTION_OBJECT_RELATION_OWNED.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var007()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {

      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ ,charPassword );
   PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);
      succeeded("list1="+list1);

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList ::  UserObjectsOwnedList(AS400 system, String userName, int selectionFileSystem, int selectionObjectRelation)
   *  using SELECTION_FILE_SYSTEM_DIRECTORY  and SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var008()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {

      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
   PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY , UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      succeeded("list1="+list1);
    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList ::  UserObjectsOwnedList(AS400 system, String userName, int selectionFileSystem, int selectionObjectRelation)
   *  using an invalid int value for selectionFileSystem.
   * <p>Result: Verify that exception is thrown.
   */
  public void Var009()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {

      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ ,charPassword );
      UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", 23, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException");
    }
    finally
    {
       PasswordVault.clearPassword(charPassword); 

      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList :: UserObjectsOwnedList(AS400 system, String userName, int selectionFileSystem, int selectionObjectRelation)
   *  using an invalid int value for selectionObjectRelation.
   * <p>Result: Verify that exception is thrown.
   */
  public void Var010()
  {
    AS400 system = null;
        char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);

    try
    {
      system = new AS400(systemObject_.getSystemName(), pwrSysUserID_ , charPassword );
      UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, 23);
      failed("Exception didn't occur."+list1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException");
    }
    finally
    {
       PasswordVault.clearPassword(charPassword); 

      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }

//  private final boolean isRunningNativelyAndThreadsafe()
//  {
//    String prop = getProgramCallThreadSafetyProperty(); // we only care about ProgramCall, not CommandCall
//
//    if (prop == null || !prop.equals("true")) return false;
//    else if (systemObject_.canUseNativeOptimizations()) return true;
//    else return false;
//  }

  /**
   * <p>Test: Call  UserObjectsOwnedList and verify that a certain number of objects (QSYS) is returned.
   * <p>Result: Verify that no exception is thrown and that the correct amount of objects is
   * returned.
   */
  public void Var011()
  {
    AS400 system = null;
    try
    {
      system = new AS400(systemObject_.getSystemName(), "USRTEST" , "user23ts".toCharArray());
 
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      UserObjectsOwnedListEntry[] entries1 = list.getObjectList();
      System.out.println(list);

      if (DEBUG)
      {
        for (int i=0; i<entries1.length; ++i)
        {
          System.out.println("Entry[" + i + "/" + entries1.length + "]= " + entries1[i]);
        }
      }

      // The length should be x (number of objects created),
      // plus 4 (objects that belong to the user profile on the system:
      // - QSYS/USRTEST     (the USRTEST user profile)
      // - QUSRSYS/USRTEST  (message queue associated with USRTEST profile)
      // - QTEMP/JT4SYLOBJA (temp userspace created by UserObjectsOwnedList class)
      // - QTEMP/ZRCRPCSPC  (user space created by the Remote Command Host Server)  (NOTE: This is NOT reported if running natively, since no Host Server job.)
      int numExpected = numObjectsCreated_ + 4;

      //if (isRunningNativelyAndThreadsafe()) numExpected -= 1;  // no ZRCRPCSPC
      if (systemObject_.canUseNativeOptimizations()) numExpected -= 1;  // no ZRCRPCSPC

      if (entries1.length == numExpected) {
        succeeded();
      }
      else {
        failed("Wrong number of objects. Expected: " + numExpected + "; got: " + entries1.length);
        for (int i=0; i<entries1.length; i++)
        {
          UserObjectsOwnedListEntry entry = entries1[i];
          System.out.println(entry.getLibraryName()+"/"+entry.getObjectName());
        }
      }

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList getSelectionFileSystem() and verify that the selection criteria for the file system
   * is the correct one.
   * <p>Result: Verify that no exception is thrown and that the correct selection criteria
   * returned.
   */
  public void Var012()
  {
    AS400 system = null;
    try
    {
      system = new AS400(systemObject_.getSystemName(), "USRTEST" , "user23ts".toCharArray());
 
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      UserObjectsOwnedList list2 = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);


      if ((list.getSelectionFileSystem() == UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY) &
          (list2.getSelectionFileSystem() == UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY))
        succeeded();
      else
        failed("Wrong selection criteria returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList getSelectionObjectRelation() and verify that the selection criteria for the object
   * selection is the correct one.
   * <p>Result: Verify that no exception is thrown and that the correct selection criteria
   * returned.
   */
  public void Var013()
  {
    AS400 system = null;
    try
    {
      system = new AS400(systemObject_.getSystemName(), "USRTEST" , "user23ts".toCharArray());
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      UserObjectsOwnedList list2 = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED );
      UserObjectsOwnedList list3 = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED);


      if ((list.getSelectionObjectRelation() == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED) &
          (list2.getSelectionObjectRelation()  == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED) &
          (list3.getSelectionObjectRelation() == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED))
        succeeded();

      else
        failed("Wrong selection criteria returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList
   * <p>Result: Verify that no exception is thrown and that the correct selection criteria
   * returned.
   */
  public void Var014()
  {
    AS400 system = null;
    try
    {
      system = new AS400(systemObject_.getSystemName(), "USRTEST" , "user23ts".toCharArray());
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

      if ((list.getSelectionObjectRelation() != UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED) &
          (list.getSelectionObjectRelation() != UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED) &
          (list.getSelectionFileSystem() != UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY))
        succeeded();

      else
        failed("Wrong selection criteria returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList getSystem() and verify that the correct system is returned.
   * <p>Result: Verify that no exception is thrown and that the correct selection criteria.
   * returned.
   */
  public void Var015()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId(), UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);
      AS400 name = list.getSystem();

      if (name.getSystemName() == systemObject_.getSystemName())
        succeeded();

      else
        failed("Wrong system returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList getUserName() and verify that the correct system is returned.
   * <p>Result: Verify that no exception is thrown and that the correct user is returned.
   */
  public void Var016()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId(), UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

      if (list.getUserName() == systemObject_.getUserId())
        succeeded();

      else
        failed("Wrong system returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList setSelectionFileSystem(int selectionFileSystem)  and verify that the correct
   * file system was selected.
   * <p>Result: Verify that no exception is thrown and that the correct file system is returned.
   */
  public void Var017()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());
      list.setSelectionFileSystem(UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY);
      UserObjectsOwnedList list2 = new UserObjectsOwnedList(system, systemObject_.getUserId());
      list2.setSelectionFileSystem(UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY);

      if ((list.getSelectionFileSystem() == UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY) &
          (list2.getSelectionFileSystem() == UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY)  )
        succeeded();

      else
        failed("Wrong system returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }






  /**
   * <p>Test: Call  UserObjectsOwnedList setSelectionFileSystem(int selectionFileSystem) using an invalid parameter
   * and verify that an exception is thrown.
   * <p>Result: Verify that the correct exception is thrown.
   */
  public void Var018()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      list.setSelectionFileSystem(23);
      failed("No Exception was thrown.");

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException");

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList setSelectionObjectRelation(int selectionObjectRelation) and verify
   * that the correct object relation was selected.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var019()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());
      list.setSelectionObjectRelation(UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED);
      UserObjectsOwnedList list2 = new UserObjectsOwnedList(system, systemObject_.getUserId());
      list2.setSelectionObjectRelation(UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);
      UserObjectsOwnedList list3 = new UserObjectsOwnedList(system, systemObject_.getUserId());
      list3.setSelectionObjectRelation(UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);


      if((list.getSelectionObjectRelation() == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_AUTHORIZED) &
         (list2.getSelectionObjectRelation() == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED) &
         (list3.getSelectionObjectRelation() == UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED))
      {
        succeeded();
      }
      else
      {
        failed("Wrong selection object relation was returned");
      }
    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call UserObjectsOwnedList setSelectionObjectRelation(int selectionObjectRelation) using an invalid parameter
   * and verify that an exception is thrown.
   * <p>Result: Verify that the correct exception is thrown.
   */
  public void Var020()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      list.setSelectionObjectRelation(23);
      failed("No Exception was thrown.");

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "ExtendedIllegalArgumentException");

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList setSystem(AS400 system) and verify that the correct
   * system is returned.
   * <p>Result: Verify that no exception is thrown and that the correct system is returned.
   */
  public void Var021()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      AS400 fake = new AS400 ("fakesystem","fakeuser","fakepassword".toCharArray());
      UserObjectsOwnedList list = new UserObjectsOwnedList(fake, systemObject_.getUserId());

      //Set the system with the one sent in the parameters.
      list.setSystem(system);

      if (list.getSystem() == system)
        succeeded();

      else
        failed("Wrong system returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call UserObjectsOwnedList setSystem(AS400 system) using an invalid parameter
   * and verify that an exception is thrown.
   * <p>Result: Verify that the correct exception is thrown.
   */
  public void Var022()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      list.setSystem(null);
      failed("No Exception was thrown.");

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException");

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList setUserName(String userName) and verify that the correct
   * userID is returned.
   * <p>Result: Verify that no exception is thrown and that the correct userID is returned.
   */
  public void Var023()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(),systemObject_.getUserId(),charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      list.setUserName("USRTEST");

      if (list.getUserName() == "USRTEST")
        succeeded();

      else
        failed("Wrong userID returned.");

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call UserObjectsOwnedList setUserName(String userName) using an invalid parameter
   * and verify that an exception is thrown.
   * <p>Result: Verify that the correct exception is thrown.
   */
  public void Var024()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      list.setUserName(null);
      failed("No Exception was thrown.");

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "NullPointerException");

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call  UserObjectsOwnedList toString() and verify that a String is returned.
   * <p>Result: Verify that no exception is thrown and that the String is returned.
   */
  public void Var025()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      System.out.println(list.toString());
      succeeded();

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList toString() and verify that a String is returned.
   * <p>Result: Verify that no exception is thrown and that the String is returned.
   */
  public void Var026()
  {
    AS400 system = null;
    try
    {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
      system = new AS400(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, systemObject_.getUserId());

      System.out.println(list.toString());
      succeeded();

    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }




  /**
   * <p>Test: Call  UserObjectsOwnedList and verify that a certain number of objects (IFS) is returned.
   * <p>Result: Verify that no exception is thrown and that the correct number of objects is
   * returned.
   */
  public void Var027()
  {
    AS400 system = null;
    try
    {

      system = new AS400(systemObject_.getSystemName(),"USRTEST" ,"user23ts".toCharArray());
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "USRTEST", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);
      UserObjectsOwnedListEntry[] entries1 = list.getObjectList();
      System.out.println(list);

      if (DEBUG || (entries1.length != 3))
      {
	  System.out.println("entries1.length="+entries1.length); 
        for (int i=0; i < entries1.length; ++i)
        {
          System.out.println("Entry[" + i + "/" + entries1.length + "]= " + entries1[i]);
        }
      }

      if(entries1.length == 3)
        succeeded();

      else
        failed("Wrong number of objects returned");


    }
    catch (Exception e)
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();

    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }





  /**
   * <p>Test: Call getObjectList with a profile that has no authority and verify that a AS400Exception
   * is thrown.
   * <p>Result: Verify that the exception is thrown.
   */
  public void Var028()
  {
    AS400 system = null;
    try
    {

      system = new AS400(systemObject_.getSystemName(), "USRTEST" ,"user23ts".toCharArray());
      UserObjectsOwnedList list = new UserObjectsOwnedList(system, "JAVACTL", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);
      UserObjectsOwnedListEntry[] entries1 = list.getObjectList();
      failed("Exception didn't occur."+entries1);

    }
    catch (Exception e)
    {
      assertExceptionIs(e, "AS400Exception");
    }
    finally
    {
      if (system != null) {
        try { system.disconnectAllServices(); } catch (Throwable t) { t.printStackTrace(); }
      }
    }
  }

}//UserObjectsOwnedListTestcase class

