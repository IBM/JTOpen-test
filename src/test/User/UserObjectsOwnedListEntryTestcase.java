///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserObjectsOwnedListEntryTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;


import com.ibm.as400.access.AS400;
import com.ibm.as400.access.UserObjectsOwnedList;
import com.ibm.as400.access.UserObjectsOwnedListEntry;

import test.PasswordVault;
import test.Testcase;


/**
 * Test cases for the ObjectReferences class.
 */
public class  UserObjectsOwnedListEntryTestcase extends Testcase 
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserObjectsOwnedListEntryTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }

  private static final boolean DEBUG = false;

  /**
   * <p>Test: Successful invocation.
   * <p>Result: Test must succeed.
   */
  public void Var001()
  {

 
   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 

    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      System.out.println("Print out entries1 list: ");
      System.out.println(list1);
      if (DEBUG)
      {
        for (int i=0; i<entries1.length; ++i)
        {
          System.out.println("Entry[" + i + "/" + entries1.length + "]= " + entries1[i]); // Calls the UserObjAuthOwnEntry.toString() method
        }
      }
      succeeded();	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getAspDeviceNameOfLibrary with SELECTION_FILE_SYSTEM_LIBRARY, and verify that a 
   * string is returned.
   * <p>Result: The string with the auxiliary storage pool (ASP) device name where the object's 
   * library is stored is returned.
   */
  public void Var002()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String pool = entries1[0].getAspDeviceNameOfLibrary();

      if(pool != null)
      {
        System.out.println(pool);
        succeeded();	
      }
      else
        failed("Pool string should not be null");	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }



  /**
   * <p>Test: Use getAspDeviceNameOfLibrary with SELECTION_FILE_SYSTEM_DIRECTORY and verify that 
   * null is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */	
  public void Var003()
  {

 
   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword);
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String pool = entries1[0].getAspDeviceNameOfLibrary();

      if(pool == null)
      {
        System.out.println(pool);
        succeeded();
      }
      else
      {
        System.out.println(pool);
        failed("Should return null.");
      }	 
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getAspDeviceNameOfObject and verify that a string is returned.
   * <p>Result: The string with the auxiliary storage pool (ASP) device name where the object's 
   * library is stored is returned.
   */
  public void Var004()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String pool = entries1[0].getAspDeviceNameOfObject();

      if(pool != null)
      {
        System.out.println(pool);
        succeeded();	
      }
      else
        failed("Pool string getAspDeviceNameOfObject() should not be null");	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getAttribute and verify that a string is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var005()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED_OR_AUTHORIZED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String attribute = entries1[0].getAttribute();

      if(attribute != null)
      {
        System.out.println(attribute);
        succeeded();	
      }
      else
        failed("getAttribute() Returned null");	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getAuthorityHolder and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown. 
   */
  public void Var006()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean holder = entries1[0].getAuthorityHolder();

      System.out.println(holder);
      succeeded();	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getAuthorityListManagement and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var007()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean authority = entries1[0].getAuthorityListManagement();

      System.out.println(authority);
      succeeded();	

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }




  /**
   * <p>Test: Use getAuthorityValue and verify that a string is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown .
   */
  public void Var008()
  {

    char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
     AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String authority = entries1[0].getAuthorityValue();

      System.out.println(authority);
      succeeded();	 
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }




  /**
   * <p>Test: Use getDataAdd and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var009()
  {
      char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_); 
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword   );
    PasswordVault.clearPassword(charPassword);    
  
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean data = entries1[0].getDataAdd();

      System.out.println(data);
      succeeded();


    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }




  /**
   * <p>Test: Use getDataDelete and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var010()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean data = entries1[0].getDataDelete();

      System.out.println(data);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getDataExecute and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var011()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword);
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean data = entries1[0].getDataExecute();

      System.out.println(data);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getDataRead and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var012()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean data = entries1[0].getDataRead();

      System.out.println(data);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getDataUpdate and verify that a value is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var013()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean data = entries1[0].getDataUpdate();

      System.out.println(data);
      succeeded();


    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getLibraryName and verify that a string is returned.
   * <p>Result: Verify that null is not returned and verify that no exception is thrown.
   */
  public void Var014()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String library = entries1[0].getLibraryName();

      if(library != null)
      {
        System.out.println(library);
        succeeded();
      }
      else
      {
        System.out.println(library);
        failed("getLibraryName() Returned null.");
      }	 
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getLibraryName and verify that a null is returned.
   * <p>Result: Verify that null is returned.
   */
  public void Var015()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String library = entries1[0].getLibraryName();

      if(library == null)
      {
        System.out.println(library);
        succeeded();
      }
      else
      {
        System.out.println(library);
        failed("Should return null.");
      }	 
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getObjectAlter and verify that a value is returned.
   * <p>Result: Verify that a value is returned and verify that no exception is thrown.
   */
  public void Var016()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean alter = entries1[0].getObjectAlter();

      System.out.println(alter);
      succeeded();
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getObjectExistence and verify that a value is returned.
   * <p>Result: Verify that a value is returned and verify that no exception is thrown.
   */
  public void Var017()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean existence = entries1[0].getObjectExistence();

      System.out.println(existence);
      succeeded();
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }




  /**
   * <p>Test: Use getObjectManagement and verify that a value is returned.
   * <p>Result: Verify that a value is returned and verify that no exception is thrown.
   */
  public void Var018()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean management = entries1[0].getObjectManagement();

      System.out.println(management);
      succeeded();
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getObjectName and verify that a value is returned.
   * <p>Result: Verify that a value is returned and verify that no exception is thrown.
   */
  public void Var019()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String name = entries1[0].getObjectName();

      if(name != null)
      {
        System.out.println(name);
        succeeded();
      }
      else
      {
        failed("getObjectName() Returned null.");	
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getObjectName and verify that a string is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */
  public void Var020()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String name = entries1[0].getObjectName();

      if(name == null)
      {
        System.out.println(name);
        succeeded();
      }
      else
      {
        failed("Should return null.");	
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }






  /**
   * <p>Test: Use getObjectOperational and verify that a value is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */
  public void Var021()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean operational = entries1[0].getObjectOperational();

      System.out.println(operational);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getObjectReference and verify that a value is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */
  public void Var022()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      boolean reference = entries1[0].getObjectReference();

      System.out.println(reference);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }






  /**
   * <p>Test: Use getObjectType and verify that a string is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */
  public void Var023()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String type = entries1[0].getObjectType();

      System.out.println(type);
      succeeded();
    }
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getOwnership and verify that a string is returned.
   * <p>Result: Verify that null is returned and verify that no exception is thrown.
   */
  public void Var024()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String ownship = entries1[0].getOwnership();


      System.out.println(ownship);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getPathName and verify that a string is returned.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var025()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String path = entries1[0].getPathName();

      if(path != null)
      {	     
        System.out.println(path);
        succeeded();
      }
      else
      {
        failed("getPathName() Returned null.");
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getPathName with SELECTION_FILE_SYSTEM_LIBRARY and verify that 
   * null is returned.
   * <p>Result: Verify that null is returned and that no exception is thrown.
   */
  public void Var026()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String path = entries1[0].getPathName();

      if(path == null)
      {	     
        System.out.println(path);
        succeeded();
      }
      else
      {
        failed("Should return null.");
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use getTextDescription and verify that a string is returned.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var027()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String desc = entries1[0].getTextDescription();

      System.out.println(desc);
      succeeded();

    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use isQSYSObjectEntry and verify that true is returned.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var028()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }

      if(entries1[0].isQSYSObjectEntry() ==  true)
      {	     
        succeeded();
      }
      else
      {
        failed("Should return true.");
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use isQSYSObjectEntry and verify that false is returned.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var029()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_DIRECTORY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }

      if(entries1[0].isQSYSObjectEntry() ==  false)
      {	     
        succeeded();
      }
      else
      {
        failed("Should return false.");
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }





  /**
   * <p>Test: Use toString and verify that a string is returned.
   * <p>Result: Verify that no exception is thrown.
   */
  public void Var030()
  {

   char[] charPassword = PasswordVault.decryptPassword(pwrSysEncryptedPassword_);
    AS400 system = new AS400(pwrSys_.getSystemName(), pwrSys_.getUserId(), charPassword );
    PasswordVault.clearPassword(charPassword); 
    UserObjectsOwnedList list1 = new UserObjectsOwnedList(system, "JAVA", UserObjectsOwnedList.SELECTION_FILE_SYSTEM_LIBRARY, UserObjectsOwnedList.SELECTION_OBJECT_RELATION_OWNED);

    try
    {
      UserObjectsOwnedListEntry[] entries1 = list1.getObjectList();
      if (entries1.length == 0) {
        notApplicable("No objects returned from UserObjectsOwnedList.getObjectList().");
        return;
      }
      String object = entries1[0].toString();

      if(object != null)
      {	     
        System.out.println(object);
        succeeded();
      }
      else
      {
        failed("Should return false.");
      }
    } 
    catch (Exception e) 
    {
      failed(e, "Unexpected Exception.");
      e.printStackTrace();
    } 		
  }

}

	
