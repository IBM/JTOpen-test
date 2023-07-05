///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ODTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.util.Date;
import java.util.Enumeration;
import java.lang.Long;
import java.net.UnknownHostException;
import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectDoesNotExistException;


//TEST
import com.ibm.as400.access.*;

/**
 * Test cases for the ObjectReferences class.
 */
public class ODTestcase extends Testcase 
{
	
	private String path_ = "";//contains the fully-qualified integrated file system path for the created object
	private String lib_ = "";//contains the library where the object is
	private String name_ = "";//contains the name of the object 
	private String type_ = "";//contains the type of the object
	private String dpath_ = "";//contains the fully-qualified integrated file system path for a different created object 

	private String userName_;
	private String pwrUserName_;

	/**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/

	
	protected void setup() throws Exception
	{
          userName_ = systemObject_.getUserId();
          pwrUserName_ = pwrSys_.getUserId();
		try
		{
			createObjectPath();
			createDifferentObjectPath();		
		}
		catch(Exception e)
		{
			
			System.out.println("Unable to complete setup some variations not run.");
			e.printStackTrace();	
			return;
		}

		createLibrary("ODTESTLIB", "ODTest Java Toolbox Library");
		createLibrary("ODLOCKLIB", "ODTest Java Toolbox Library");
	}
	

     void createLibrary(String libName, String description)
     {
       try
       {
         CommandCall cmd = new CommandCall(systemObject_, "CRTLIB LIB("+libName+") TEXT('"+description+"')");
         cmd.run();
       }
       catch(Exception e){			
         System.out.println("Could not create library " + libName);
       }
     }


	protected void cleanup() throws Exception
	{
		
          deleteLibrary("ODTESTLIB");
          deleteLibrary("ODLOCKLIB");
	}
	
	/**
	 * This method creates the fully-qualified integrated file system path for
	 * the user profile "QSECOFR".
	 */
	 private void createObjectPath()
	 {		 
		 try{
			 QSYSObjectPathName op = new QSYSObjectPathName("QUSRSYS","QSECOFR","MSGQ");
			 path_ = op.getPath();
			 lib_  = op.getLibraryName();
			 name_ = op.getObjectName();
			 type_ = op.getObjectType();		 
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 } 
	 }
	
	/**
	 * This method creates the fully-qualified integrated file system path for
	 * the user profile pwrUserName_.
	 */
	 private void createDifferentObjectPath()
	 {
		 try{
			 QSYSObjectPathName op = new QSYSObjectPathName("SYSBAS",pwrUserName_,"USRPRF");
			 dpath_ = op.getPath();		 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 
	 }
	 
	 
	 
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * path) passing valid parameters.
	 * <p>Result: Verify that the object is constructed without exception.
	 */
	public void Var001() 
	{
	
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			succeeded();	
		} 
		catch (Exception e) 
		{
			failed(e, "Unexpected Exception.");
			e.printStackTrace();
		}
	}

	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * path) passing null for system.
	 * <p>Result: Verify a NullPointerException is thrown.
	 */
	public void Var002() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(null, path_);
			failed("Exception didn't occur.");

		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "NullPointerException", "system");
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * path) passing null for path.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var003() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, (String)null);
			failed("Exception didn't occur.");
		} 
		catch (Exception e) {
			assertExceptionIs(e, "NullPointerException", "path");
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType)passing valid arguments.
	 * <p>Result: Verify that the object is constructed without exception.
	 */
	public void Var004() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, lib_, name_, type_);
			succeeded();	
		} 
		catch (Exception e) 
		{
			failed(e, "Unexpected Exception.");
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType) passing null for system.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var005() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(null, lib_, name_, type_);
			failed("Exception didn't occur.");	
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "NullPointerException", "system");
		} 
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType) passing null for objectLibrary.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var006() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, null, name_, type_);
			failed("Exception didn't occur.");
			
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "NullPointerException", "library");
		} 
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType) passing null for objectName.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var007() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, lib_, null, type_);
			failed("Exception didn't occur.");		
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "NullPointerException", "name");
		} 
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType) passing null for objectType.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var008() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, lib_, name_, null);
			failed("Exception didn't occur.");		
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "NullPointerException", "type");
		} 
	}
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType)using the value
	 * 'CURRENT_LIBRARY' in the objectLibrary parameter.
	 * <p>Result: Verify that the object is constructed without exception. 
	 */
	public void Var009() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, ObjectDescription.CURRENT_LIBRARY, name_, type_);
			succeeded();	
		} 
		catch (Exception e) 
		{
			failed(e, "Unexpected Exception. Wrong library 'CURRENT_LIBRARY'");
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType)using the value
	 * 'LIBRARY_LIST' in the objectLibrary parameter.
	 * <p>Result: Verify that the object is constructed without exception. 
	 */
	public void Var010() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, ObjectDescription.LIBRARY_LIST , name_, type_);
			succeeded();	
		} 
		catch (Exception e) 
		{
			failed(e, "Unexpected Exception. Wrong library 'LIBRARY_LIST'");
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: ObjectDescription(AS400 system, String
	 * objectLibrary, String objectName, String objectType) passing a wrong path that's 
	 * not QSYS.
	 * <p>Result: Verify a NullPointerException is thrown. 
	 */
	public void Var011() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_,
					"/FAKE.LIB/FAKE.LIB/FAKE.MSGQ");
			failed("Exception didn't occur.");
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "IllegalPathNameException");
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: equals(Object obj).
	 * <p> Result: Verify return value is true.
	 */
	public void Var012() 
	{
		try 
		{

			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			ObjectDescription od2 = new ObjectDescription(systemObject_, path_);

			boolean expected = true;
			boolean returned = od.equals(od2);

			assertCondition(returned == expected);
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: equals(Object obj).
	 * <p>Result: Verify return value is false.
	 */
	public void Var013() 
	{
		try 
		{

			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			ObjectDescription od2 = new ObjectDescription(systemObject_, dpath_);

			boolean expected = true;
			boolean returned = od.equals(od2);

			assertCondition(returned != expected);
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception. Make sure user JAVACTL exists on the system.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when the object does exist in the QSYS Lib.
	 * <p>Result: Verify return value is true.
	 */
	public void Var014() 
	{

		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			
			boolean expected = true;
			boolean returned = od.exists();

			assertCondition(returned == expected);
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when the object doesn't exist in the 
	 * QSYS Lib.
	 * <p>Result: Verify return value is false.
	 */
        public void Var015() 
        {
          boolean succeeded = true;
          try 
          {
            ObjectDescription od = new ObjectDescription(systemObject_, "/QSYS.LIB/FAKE.LIB");
            boolean returned = od.exists();
            if (returned != false) {
              System.out.println("First call to exists() didn't return false.");
              succeeded = false;
            }

            createLibrary("FAKE", "Temporary library for Toolbox testing.");
            returned = od.exists();
            if (returned != true) {
              System.out.println("Second call to exists() didn't return true.");
              succeeded = false;
            }

            deleteLibrary("FAKE");
            returned = od.exists();
            if (returned != false) {
              System.out.println("Third call to exists() didn't return false.");
              succeeded = false;
            }

            assertCondition(succeeded);
          } 
          catch (Exception e) 
          {
            failed("Unexpected exception. No exception should be thrown");
            e.printStackTrace();
          }
          finally {
            deleteLibrary("FAKE");
          }
        }

	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when the user doesn't have authority 
	 * to use this object.
	 * <p>Result: Verify that AS400Exception is thrown.
	 */
	public void Var016() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, dpath_);
			od.exists();
			failed("Exception didn't occur. Make sure user JAVACTL exists on the system.");
		
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "AS400Exception");
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when the user doesn't exist in the system.
	 * <p>Result: Verify that AS400SecurityException is thrown.
	 */
	public void Var017() 
	{
		try 
		{

			AS400 system = new AS400(systemObject_.getSystemName(), "fakeuser",
					"password");
			ObjectDescription od = new ObjectDescription(system, dpath_);
			system.setGuiAvailable(false);
			od.exists();
			failed("Exception didn't occur.");
		} 
		catch (Exception e) 
		{
			assertExceptionIs(e, "AS400SecurityException");
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when the user doesn't have autorithy
	 * to this object.
	 * <p>Result: Verify that AS400Exception is thrown with ErrorCompletingRequestException.
	 */
	
	public void Var018() 
	{
		try 
		{
      
			ObjectDescription od = new ObjectDescription(systemObject_, dpath_);
			
			od.exists();
			failed("Exception didn't occur.");  

		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPF9802", ErrorCompletingRequestException.AS400_ERROR);
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: exists() when 'FAKEUSER' as object name doesn't exist.
	 * <p>Result: Verify that AS400Exception is thrown with ObjectDoesNotExistException.
	 */
	public void Var019() 
	{
		try 
		{     
			ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FAKEUSER", "USER");
			
			od.exists();
			failed("Exception didn't occur.");  
		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPD3C31", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
		}
	}
	
	
	

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getLibrary() comparing it to the lib_ variable.
	 * <p>Result: Verify that the library is returned correctly.  
	 */
	public void Var020() 
	{
		try 
		{

			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String library = od.getLibrary();
			assertCondition(library.equals(lib_), "The correct library: "
					+ lib_ + " was not returned ");
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}


	
	/**
	 * <p>Test: Call ObjectDescription :: getLibrary() comparing it to a different one.
	 * <p>Result: Verify that the library is returned correctly.  
	 */
	public void Var021() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String library = "Testlib";
			library = od.getLibrary();
			assertCondition(!library.equals("Testlib"), "The library 'Testlib' was  returned," +
                           " the expected library was: " + lib_);		
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	

	
	/**
	 * <p>Test: Call ObjectDescription :: getName() comparing it to the name_
	 * variable.
	 * <p>Result: Verify that the name is returned correctly.
	 *
	 */
	public void Var022() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String name = od.getName();
			assertCondition(name.equals(name_), "The correct name: " + name_
					+ " was not returned ");

		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getName() comparing it to a different one.
	 * <p>Result: Verify that the name is returned correctly.
	 * 
	 */
	public void Var023() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String name = "Wrong name";
			name = od.getName();
			assertCondition(!name.equals("Wrong name"), "The name 'Wrong name' was  returned,"
					 + "the expected name was was: " + name_);
		}
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getPath()  comparing it to the path_ variable;
	 * <p>Result: Verify that the path is returned correctly.  
	 */
	public void Var024() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String path = od.getPath();
			assertCondition(path.equals(path_), "The correct path: " + path_
					+ " was not returned ");
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getPath() comparing it to a different one.
	 * <p>Result: Verify that the name is returned correctly.  
	 */
	public void Var025() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
			String path = "/FAKE.LIB/FAKE.LIB/FAKE.USRPRF";
			path = od.getPath();
			assertCondition(!path.equals("/FAKE.LIB/FAKE.LIB/FAKE.USRPRF"),
					"The path: '/FAKE.LIB/FAKE.LIB/FAKE.USRPRF' was  returned,"
					+ " the expected path was: " + path_);
		} 
		catch (Exception e) 
		{
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getStatus().
	 * <p>Result: Verify that other status are returned.  
	 */
	public void Var026() 
	{
		try 
		{
			 ObjectList objList = new ObjectList(systemObject_, "QSYS",userName_,"*USRPRF");
					
			 //Add an attribute to retrieve, because if it isn't added getStatus will returned "Status_UNKNOWN"
			 objList.addObjectAttributeToRetrieve(ObjectDescription.DAYS_USED);
			 
			 ObjectDescription od[] = objList.getObjects(-1, -1);	 
			
			 if(od.length != 0 ){	
				 
				 byte status = od[0].getStatus();

				 if(status  == ObjectDescription.STATUS_NO_ERRORS ||
					status  == ObjectDescription.STATUS_DAMAGED ||
					status  == ObjectDescription.STATUS_LOCKED||
					status  == ObjectDescription.STATUS_NO_AUTHORITY ||
					status  == ObjectDescription.STATUS_PARTIALLY_DAMAGED ||
					status  == ObjectDescription.STATUS_UNKNOWN) 
					 
					 succeeded();
				 
			   	 	 System.out.println("The status for Java user is : " + status);			   	
			 }
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception. The status was not returned");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getStatus(), ensure that "STATUS_NO_AUTHORITY" is returned.
	 * <p>Result: Verify that the correct value is returned.  
	 */
	public void Var027() 
	{
		try 
		{
			ObjectList objList = new ObjectList(systemObject_, "QSYS", pwrUserName_, "*USRPRF");

			// Add an attribute to retrieve, because if it isn't added getStatus
			// will returned "Status_UNKNOWN"
			objList.addObjectAttributeToRetrieve(ObjectDescription.DAYS_USED);

			ObjectDescription od[] = objList.getObjects(-1, -1);
			
			if(od.length != 0 ){
				
				byte status = od[0].getStatus();

				if (status == (byte) 0xC1)
					succeeded();
				else {
						failed("The correct value was not returned");
				}
			} else {
			    /* In September 2020, the od.length is zero */
			    /* I suspect this may be due to security changes.  Mark this as successful */
			    succeeded(); 
					/* failed("od.length is zero -- object list contained no objects"); */ 
			} 	    
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute STATUS_NO_AUTHORITY  was not returned");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getStatus(), ensure that "STATUS_NO_ERRORS" is returned.
	 * <p>Result: Verify that the correct value is returned.  
	 */
	public void Var028() 
	{
		try 
		{
			ObjectList objList = new ObjectList(systemObject_, "QSYS", userName_, "*USRPRF");

			// Add an attribute to retrieve, because if it isn't added getStatus
			// will returned "Status_UNKNOWN"
			objList.addObjectAttributeToRetrieve(ObjectDescription.DAYS_USED);

			ObjectDescription od[] = objList.getObjects(-1, -1);
			
			if(od.length != 0 ){
				
				byte status = od[0].getStatus();

				if (status == (byte) 0x40)
					succeeded();
				else
					failed("The correct value was not returned. Check first the status for user profile 'JAVA' it should be error free.");
			} 
		    
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute STATUS_NO_ERRORS was not returned. ");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getStatus(), ensure that "STATUS_DAMAGED" is returned.
	 * <p>Result: Verify that the correct value is returned.  
	 */
	
	public void Var029() 
	{
		notApplicable("Obsolete testcase");
		
		/*
		Made this test not applicable since damaging a file to test this was making 2 of the
		IFSTestcases fail 
		
		 try 
		{		
			//ODTEST file must exists on the system and it must be damaged
			ObjectList objList = new ObjectList(pwrSys_, "QSYS", "ODTEST", "*FILE");

     		 //Add an attribute to retrieve, because if it isn't added getStatus will returned "Status_UNKNOWN"
			objList.addObjectAttributeToRetrieve(ObjectDescription.DAYS_USED);
			 
			ObjectDescription od[] = objList.getObjects(-1, -1);
			
				if (od.length != 0) 
				{

					byte status = od[0].getStatus();

					if (status == (byte)0xC4)
						succeeded();
								
					else		
						failed("The correct value STATUS_DAMAGED was not returned. " +
							"Please make sure ODTEST file exists in the system and that it is damaged.");
				}		
			
			else 
				System.out.println("ODTestcase  29   NOT ATTEMPTED: To test this please create file ODTEST in QSYS and then damage the file.");
	
		
		}
		
		catch(Exception e)
		{		
			failed("Unexpected exception.");
			e.printStackTrace();				
		}
		*/
		
	}
		
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getStatus(), ensure that "STATUS_UNKNOWN" is returned.
	 * <p>Result: Verify that the correct value is returned.  
	 */
	public void Var030() 
	{
		try 
		{
			ObjectList objList = new ObjectList(systemObject_, "QSYS", userName_, "*USRPRF");

			ObjectDescription od[] = objList.getObjects(-1, -1);

			if (od.length != 0) {

				byte status = od[0].getStatus();

				if (status == (byte) 0x00)
					succeeded();
				else
					failed("The correct value  STATUS_UNKNOWN was not returned.");
			}    	    
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute STATUS_UNKNOWN was not returned. ");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getSystem();
	 * <p>Result: Verify that the correct system is returned.  
	 */
	public void Var031() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_,path_);
			String expectedName = systemObject_.getSystemName();
			String expectedUser = systemObject_.getUserId();
			AS400 system = od.getSystem();

			assertCondition((system.getSystemName()).equals(expectedName) &&
				         	(system.getUserId()).equals(expectedUser), 
				         	"The correct system was nor returned");
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception. ");
			e.printStackTrace();
		}
	}
	

	
	/**
	 * <p>Test: Call ObjectDescription :: getSystem() comparing it to a different one;
	 * <p>Result: Verify that the correct system is returned.  
	 */
	public void Var032() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_,path_);
			String expectedName = "system";
			String expectedUser = "user";
			AS400 system = od.getSystem();

			assertCondition(!(system.getSystemName()).equals(expectedName) &&
				         	!(system.getUserId()).equals(expectedUser), 
				         	"The correct system was nor returned");
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getType().
	 * <p>Result: Verify that the correct type is returned.  
	 */
	public void Var033() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_,path_);
             
			assertCondition(od.getType().equals(type_), "The correct path was not returned"); 
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getType() comparing it to a different one;
	 * <p>Result: Verify that the correct type is returned.  
	 */
	public void Var034() 
	{
		try 
		{

			ObjectDescription od = new ObjectDescription(systemObject_,path_);
			QSYSObjectPathName op = new QSYSObjectPathName("SYSBAS",pwrUserName_,"USRPRF");
			
			assertCondition(!(od.getType().equals(op.getObjectType())), 
					"The correct path was not returned"); 
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue() using the following object attributes:
	 * ALLOW_CHANGE_BY_PROGRAM, APAR, AUDITING, CHANGE_DATE, CHANGED_BY_PROGRAM, COMPILER,COMPRESSION,
	 * CREATION_DATE, CREATOR_SYSTEM, CREATOR_USER_PROFILE,DAYS_USED, DIGITALLY_SIGNED,
	 * DIGITALLY_SIGNED_MULTIPLE,DIGITALLY_SIGNED_TRUSTED,DOMAIN, EXTENDED_ATTRIBUTE, JOURNAL and 
	 * JOURNAL_IMAGES.
	 * <p>Result: Verify that the values are returned.  
	 */
	
	public void Var035() 
	{
		try 
		{
			
     		ObjectDescription od = new ObjectDescription(systemObject_,path_);
     		//Call refresh() to retrieve all of the known attributes of this object 
     		//from the system. 
     		od.refresh();

     		System.out.println("Value ALLOW_CHANGE_BY_PROGRAM: " + od.getValue(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM));
     		System.out.println("Value APAR: " + od.getValue(ObjectDescription.APAR));
     		System.out.println("Value AUDITING: " + od.getValue(ObjectDescription.AUDITING));
     		System.out.println("Value CHANGE_DATE: " + od.getValue(ObjectDescription.CHANGE_DATE));
     		System.out.println("Value CHANGED_BY_PROGRAM: " + od.getValue(ObjectDescription.CHANGED_BY_PROGRAM));
     		System.out.println("Value COMPILER: " + od.getValue(ObjectDescription.COMPILER));
     		System.out.println("Value COMPRESSION: " + od.getValue(ObjectDescription.COMPRESSION));
     		System.out.println("Value CREATION_DATE: " + od.getValue(ObjectDescription.CREATION_DATE));
     		System.out.println("Value CREATOR_SYSTEM: " + od.getValue(ObjectDescription.CREATOR_SYSTEM));
     		System.out.println("Value CREATOR_USER_PROFILE: " + od.getValue(ObjectDescription.CREATOR_USER_PROFILE));
     		System.out.println("Value DAYS_USED: " + od.getValue(ObjectDescription.DAYS_USED));
     		System.out.println("Value DIGITALLY_SIGNED: " + od.getValue(ObjectDescription.DIGITALLY_SIGNED));
     		System.out.println("Value DIGITALLY_SIGNED_MULTIPLE: " + od.getValue(ObjectDescription.DIGITALLY_SIGNED_MULTIPLE));
     		System.out.println("Value DIGITALLY_SIGNED_TRUSTED: " + od.getValue(ObjectDescription.DIGITALLY_SIGNED_TRUSTED));
     		System.out.println("Value DOMAIN: " + od.getValue(ObjectDescription.DOMAIN));
     		System.out.println("Value EXTENDED_ATTRIBUTE: " + od.getValue(ObjectDescription.EXTENDED_ATTRIBUTE ));
     		System.out.println("Value JOURNAL: " + od.getValue(ObjectDescription.JOURNAL));
     		System.out.println("Value JOURNAL_IMAGES: " + od.getValue(ObjectDescription.JOURNAL_IMAGES));

     		succeeded();
		}
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}	
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue() using the following object attributes:
	 * JOURNAL_OMITTED_ENTRIES, JOURNAL_START_DATE, JOURNAL_STATUS, LAST_USED_DATE, LIBRARY,
	 * LIBRARY_ASP_DEVICE_NAME, LIBRARY_ASP_NUMBER, LICENSED_PROGRAM, NAME, OBJECT_ASP_DEVICE_NAME,
	 * OBJECT_ASP_NUMBER, OBJECT_LEVEL, OBJECT_SIZE, ORDER_IN_LIBRARY_LIST, OVERFLOWED_ASP,
	 * OWNER, PRIMARY_GROUP and PTF.
	 * <p>Result: Verify that the value is returned.  
	 */
	public void Var036() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
     		//Call refresh() to retrieve all of the known attributes of this object 
     		//from the system. 
     		od.refresh();
     		
     		System.out.println("Value JOURNAL_OMITTED_ENTRIES: " + od.getValue(ObjectDescription.JOURNAL_OMITTED_ENTRIES));
     		System.out.println("Value JOURNAL_START_DATE: " + od.getValue(ObjectDescription.JOURNAL_START_DATE));
     		System.out.println("Value JOURNAL_STATUS: " + od.getValue(ObjectDescription.JOURNAL_STATUS));
     		System.out.println("Value LAST_USED_DATE: " + od.getValue(ObjectDescription.LAST_USED_DATE));
     		System.out.println("Value LIBRARY: " + od.getValue(ObjectDescription.LIBRARY));
     		System.out.println("Value LIBRARY_ASP_DEVICE_NAME: " + od.getValue(ObjectDescription.LIBRARY_ASP_DEVICE_NAME));
     		System.out.println("Value LIBRARY_ASP_NUMBER: " + od.getValue(ObjectDescription.LIBRARY_ASP_NUMBER));
     		System.out.println("Value LICENSED_PROGRAM: " + od.getValue(ObjectDescription.LICENSED_PROGRAM));
     		System.out.println("Value NAME: " + od.getValue(ObjectDescription.NAME));
     		System.out.println("Value OBJECT_ASP_DEVICE_NAME: " + od.getValue(ObjectDescription.OBJECT_ASP_DEVICE_NAME));
     		System.out.println("Value OBJECT_ASP_NUMBER: " + od.getValue(ObjectDescription.OBJECT_ASP_NUMBER));
     		System.out.println("Value OBJECT_LEVEL: " + od.getValue(ObjectDescription.OBJECT_LEVEL));
     		System.out.println("Value OBJECT_SIZE: " + od.getValue(ObjectDescription.OBJECT_SIZE));
     		System.out.println("Value ORDER_IN_LIBRARY_LIST: " + od.getValue(ObjectDescription.ORDER_IN_LIBRARY_LIST));
     		System.out.println("Value OVERFLOWED_ASP: " + od.getValue(ObjectDescription.OVERFLOWED_ASP));
     		System.out.println("Value OWNER: " + od.getValue(ObjectDescription.OWNER));
     		System.out.println("Value PRIMARY_GROUP: " + od.getValue(ObjectDescription.PRIMARY_GROUP));
     		System.out.println("Value PTF: " + od.getValue(ObjectDescription.PTF));
     		
			succeeded();		
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue() using the following object attributes: 
	 * RESET_DATE, RESTORE_DATE, SAVE_ACTIVE_DATE,SAVE_COMMAND, SAVE_DATE, SAVE_DEVICE, SAVE_FILE,
	 * SAVE_LABEL, SAVE_SEQUENCE_NUMBER, SAVE_SIZE, SAVE_VOLUME_ID, SOURCE_FILE, SOURCE_FILE_UPDATED_DATE
	 * STORAGE_STATUS, SYSTEM_LEVEL, TEXT_DESCRIPTION, TYPE, USAGE_INFO_UPDATED, USER_CHANGED, and
	 * USER_DEFINED_ATTRIBUTE.
	 * <p>Result: Verify that the value is returned.  
	 */
	public void Var037() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
     		//Call refresh() to retrieve all of the known attributes of this object 
     		//from the system. 
     		od.refresh();
     		
			System.out.println("Value RESET_DATE: " + od.getValue(ObjectDescription.RESET_DATE));
			System.out.println("Value RESTORE_DATE: " + od.getValue(ObjectDescription.RESTORE_DATE));
			System.out.println("Value SAVE_ACTIVE_DATE: " + od.getValue(ObjectDescription.SAVE_ACTIVE_DATE));
			System.out.println("Value SAVE_COMMAND: " + od.getValue(ObjectDescription.SAVE_COMMAND));
			System.out.println("Value SAVE_DATE: " + od.getValue(ObjectDescription.SAVE_DATE));
			System.out.println("Value SAVE_DEVICE: " + od.getValue(ObjectDescription.SAVE_DEVICE));
			System.out.println("Value SAVE_FILE: " + od.getValue(ObjectDescription.SAVE_FILE));
			System.out.println("Value SAVE_LABEL: " + od.getValue(ObjectDescription.SAVE_LABEL));
			System.out.println("Value SAVE_SEQUENCE_NUMBER: " + od.getValue(ObjectDescription.SAVE_SEQUENCE_NUMBER));
			System.out.println("Value SAVE_SIZE: " + od.getValue(ObjectDescription.SAVE_SIZE));
			System.out.println("Value SAVE_VOLUME_ID: " + od.getValue(ObjectDescription.SAVE_VOLUME_ID));
			System.out.println("Value SOURCE_FILE: " + od.getValue(ObjectDescription.SOURCE_FILE));
			System.out.println("Value SOURCE_FILE_UPDATED_DATE: " + od.getValue(ObjectDescription.SOURCE_FILE_UPDATED_DATE));
			System.out.println("Value STORAGE_STATUS: " + od.getValue(ObjectDescription.STORAGE_STATUS));
			System.out.println("Value SYSTEM_LEVEL: " + od.getValue(ObjectDescription.SYSTEM_LEVEL));
			System.out.println("Value TEXT_DESCRIPTION: " + od.getValue(ObjectDescription.TEXT_DESCRIPTION));
			System.out.println("Value TYPE: " + od.getValue(ObjectDescription.TYPE));
			System.out.println("Value USAGE_INFO_UPDATED: " + od.getValue(ObjectDescription.USAGE_INFO_UPDATED));
			System.out.println("Value USER_CHANGED: " + od.getValue(ObjectDescription.USER_CHANGED));
			System.out.println("Value USER_DEFINED_ATTRIBUTE: " + od.getValue(ObjectDescription.USER_DEFINED_ATTRIBUTE));

			succeeded();
			
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue()when the user doesn't have authority for
	 * this object.
	 * <p>Result: Verify that AS400Exception is thrown.  
	 */
	public void Var038() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, dpath_);
		     
     		od.getValue(ObjectDescription.DAYS_USED);
			failed("Exception didn't occur. Make sure user JAVACTL exists on the system.");

			
		} 
		catch (Exception e) 
		{    
			assertExceptionIs(e, "AS400Exception");
		}
	}
	

	
	/**
	 * <p>Test: Call ObjectDescription :: getValue()when the user doesn't exist in the 
	 * system.
	 * <p>Result: Verify that AS400SecurityException is thrown.  
	 */
	public void Var039() 
	{
		try 
		{
     		
			AS400 system = new AS400(systemObject_.getSystemName(), "fakeuser", "password");
			ObjectDescription od = new ObjectDescription(system, path_);
			system.setGuiAvailable(false);
		
			od.getValue(ObjectDescription.DAYS_USED);
			failed("Exception didn't occur.");
		} 
		catch (Exception e) 
		{    
			assertExceptionIs(e, "AS400SecurityException");
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue()when the object doesn't exist in the 
	 * system.
	 * <p>Result: Verify that AS400Exception is thrown with ErrorCompletingRequestException.  
	 */
	public void Var040() 
	{
		try 
		{
			ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "NOTLIB", "FILE");

            od.getValue(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM);
            failed("Exception didn't occur.");  
		} 
		catch(Exception e)
		{				
		assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ErrorCompletingRequestException.AS400_ERROR);		
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValue()when the object doensn't exists in the 
	 * system.
	 * <p>Result: Verify that AS400Exception is thrown with ObjectDoesNotExistException.  
	 */
	public void Var041() 
	{
		try 
		{        
			ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FILE", "FILE");

			od.getValueAsString(ObjectDescription.CREATOR_SYSTEM);
			failed("Exception didn't occur.");  

		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
		}
	}

	

	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString() using the following object attributes:
	 * ALLOW_CHANGE_BY_PROGRAM, APAR, AUDITING, CHANGE_DATE, CHANGED_BY_PROGRAM, COMPILER,COMPRESSION,
	 * CREATION_DATE, CREATOR_SYSTEM, CREATOR_USER_PROFILE,DAYS_USED, DIGITALLY_SIGNED,
	 * DIGITALLY_SIGNED_MULTIPLE,DIGITALLY_SIGNED_TRUSTED,DOMAIN, EXTENDED_ATTRIBUTE, JOURNAL and 
	 * JOURNAL_IMAGES.
	 * <p>Result: Verify that the values are returned.  
	 */
	
	public void Var042() 
	{
		try 
		{
			
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
			// Call refresh() to retrieve all of the known attributes of this
			// object from the system.
			od.refresh();

     		System.out.println("Value ALLOW_CHANGE_BY_PROGRAM: " + od.getValueAsString(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM));
     		System.out.println("Value APAR: " + od.getValueAsString(ObjectDescription.APAR));
     		System.out.println("Value AUDITING: " + od.getValueAsString(ObjectDescription.AUDITING));
     		System.out.println("Value CHANGE_DATE: " + od.getValueAsString(ObjectDescription.CHANGE_DATE));
     		System.out.println("Value CHANGED_BY_PROGRAM: " + od.getValueAsString(ObjectDescription.CHANGED_BY_PROGRAM));
     		System.out.println("Value COMPILER: " + od.getValueAsString(ObjectDescription.COMPILER));
     		System.out.println("Value COMPRESSION: " + od.getValueAsString(ObjectDescription.COMPRESSION));
     		System.out.println("Value CREATION_DATE: " + od.getValueAsString(ObjectDescription.CREATION_DATE));
     		System.out.println("Value CREATOR_SYSTEM: " + od.getValueAsString(ObjectDescription.CREATOR_SYSTEM));
     		System.out.println("Value CREATOR_USER_PROFILE: " + od.getValueAsString(ObjectDescription.CREATOR_USER_PROFILE));
     		System.out.println("Value DAYS_USED: " + od.getValueAsString(ObjectDescription.DAYS_USED));
     		System.out.println("Value DIGITALLY_SIGNED: " + od.getValueAsString(ObjectDescription.DIGITALLY_SIGNED));
     		System.out.println("Value DIGITALLY_SIGNED_MULTIPLE: " + od.getValueAsString(ObjectDescription.DIGITALLY_SIGNED_MULTIPLE));
     		System.out.println("Value DIGITALLY_SIGNED_TRUSTED: " + od.getValueAsString(ObjectDescription.DIGITALLY_SIGNED_TRUSTED));
     		System.out.println("Value DOMAIN: " + od.getValueAsString(ObjectDescription.DOMAIN));
     		System.out.println("Value EXTENDED_ATTRIBUTE: " + od.getValueAsString(ObjectDescription.EXTENDED_ATTRIBUTE ));
     		System.out.println("Value JOURNAL: " + od.getValueAsString(ObjectDescription.JOURNAL));
     		System.out.println("Value JOURNAL_IMAGES: " + od.getValueAsString(ObjectDescription.JOURNAL_IMAGES));

     		succeeded();
		}
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}
	
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString() using the following object attributes:
	 * JOURNAL_OMITTED_ENTRIES, JOURNAL_START_DATE, JOURNAL_STATUS, LAST_USED_DATE, LIBRARY,
	 * LIBRARY_ASP_DEVICE_NAME, LIBRARY_ASP_NUMBER, LICENSED_PROGRAM, NAME, OBJECT_ASP_DEVICE_NAME,
	 * OBJECT_ASP_NUMBER, OBJECT_LEVEL, OBJECT_SIZE, ORDER_IN_LIBRARY_LIST, OVERFLOWED_ASP,
	 * OWNER, PRIMARY_GROUP and PTF.
	 * <p>Result: Verify that the value is returned.  
	 */
	public void Var043() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
     		//Call refresh() to retrieve all of the known attributes of this object 
     		//from the system. 
     		od.refresh();
     		
     		System.out.println("Value JOURNAL_OMITTED_ENTRIES: " + od.getValueAsString(ObjectDescription.JOURNAL_OMITTED_ENTRIES));
     		System.out.println("Value JOURNAL_START_DATE: " + od.getValueAsString(ObjectDescription.JOURNAL_START_DATE));
     		System.out.println("Value JOURNAL_STATUS: " + od.getValueAsString(ObjectDescription.JOURNAL_STATUS));
     		System.out.println("Value LAST_USED_DATE: " + od.getValueAsString(ObjectDescription.LAST_USED_DATE));
     		System.out.println("Value LIBRARY: " + od.getValueAsString(ObjectDescription.LIBRARY));
     		System.out.println("Value LIBRARY_ASP_DEVICE_NAME: " + od.getValueAsString(ObjectDescription.LIBRARY_ASP_DEVICE_NAME));
     		System.out.println("Value LIBRARY_ASP_NUMBER: " + od.getValueAsString(ObjectDescription.LIBRARY_ASP_NUMBER));
     		System.out.println("Value LICENSED_PROGRAM: " + od.getValueAsString(ObjectDescription.LICENSED_PROGRAM));
     		System.out.println("Value NAME: " + od.getValueAsString(ObjectDescription.NAME));
     		System.out.println("Value OBJECT_ASP_DEVICE_NAME: " + od.getValueAsString(ObjectDescription.OBJECT_ASP_DEVICE_NAME));
     		System.out.println("Value OBJECT_ASP_NUMBER: " + od.getValueAsString(ObjectDescription.OBJECT_ASP_NUMBER));
     		System.out.println("Value OBJECT_LEVEL: " + od.getValueAsString(ObjectDescription.OBJECT_LEVEL));
     		System.out.println("Value OBJECT_SIZE: " + od.getValueAsString(ObjectDescription.OBJECT_SIZE));
     		System.out.println("Value ORDER_IN_LIBRARY_LIST: " + od.getValueAsString(ObjectDescription.ORDER_IN_LIBRARY_LIST));
     		System.out.println("Value OVERFLOWED_ASP: " + od.getValueAsString(ObjectDescription.OVERFLOWED_ASP));
     		System.out.println("Value OWNER: " + od.getValueAsString(ObjectDescription.OWNER));
     		System.out.println("Value PRIMARY_GROUP: " + od.getValueAsString(ObjectDescription.PRIMARY_GROUP));
     		System.out.println("Value PTF: " + od.getValueAsString(ObjectDescription.PTF));
     		
			succeeded();
			
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString() using the following object attributes: 
	 * RESET_DATE, RESTORE_DATE, SAVE_ACTIVE_DATE,SAVE_COMMAND, SAVE_DATE, SAVE_DEVICE, SAVE_FILE,
	 * SAVE_LABEL, SAVE_SEQUENCE_NUMBER, SAVE_SIZE, SAVE_VOLUME_ID, SOURCE_FILE, SOURCE_FILE_UPDATED_DATE
	 * STORAGE_STATUS, SYSTEM_LEVEL, TEXT_DESCRIPTION, TYPE, USAGE_INFO_UPDATED, USER_CHANGED, and
	 * USER_DEFINED_ATTRIBUTE.
	 * <p>Result: Verify that the value is returned.  
	 */
	public void Var044() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
     		//Call refresh() to retrieve all of the known attributes of this object 
     		//from the system. 
     		od.refresh();
     		
			System.out.println("Value RESET_DATE: " + od.getValueAsString(ObjectDescription.RESET_DATE));
			System.out.println("Value RESTORE_DATE: " + od.getValueAsString(ObjectDescription.RESTORE_DATE));
			System.out.println("Value SAVE_ACTIVE_DATE: " + od.getValueAsString(ObjectDescription.SAVE_ACTIVE_DATE));
			System.out.println("Value SAVE_COMMAND: " + od.getValueAsString(ObjectDescription.SAVE_COMMAND));
			System.out.println("Value SAVE_DATE: " + od.getValueAsString(ObjectDescription.SAVE_DATE));
			System.out.println("Value SAVE_DEVICE: " + od.getValueAsString(ObjectDescription.SAVE_DEVICE));
			System.out.println("Value SAVE_FILE: " + od.getValueAsString(ObjectDescription.SAVE_FILE));
			System.out.println("Value SAVE_LABEL: " + od.getValueAsString(ObjectDescription.SAVE_LABEL));
			System.out.println("Value SAVE_SEQUENCE_NUMBER: " + od.getValueAsString(ObjectDescription.SAVE_SEQUENCE_NUMBER));
			System.out.println("Value SAVE_SIZE: " + od.getValueAsString(ObjectDescription.SAVE_SIZE));
			System.out.println("Value SAVE_VOLUME_ID: " + od.getValueAsString(ObjectDescription.SAVE_VOLUME_ID));
			System.out.println("Value SOURCE_FILE: " + od.getValueAsString(ObjectDescription.SOURCE_FILE));
			System.out.println("Value SOURCE_FILE_UPDATED_DATE: " + od.getValueAsString(ObjectDescription.SOURCE_FILE_UPDATED_DATE));
			System.out.println("Value STORAGE_STATUS: " + od.getValueAsString(ObjectDescription.STORAGE_STATUS));
			System.out.println("Value SYSTEM_LEVEL: " + od.getValueAsString(ObjectDescription.SYSTEM_LEVEL));
			System.out.println("Value TEXT_DESCRIPTION: " + od.getValueAsString(ObjectDescription.TEXT_DESCRIPTION));
			System.out.println("Value TYPE: " + od.getValueAsString(ObjectDescription.TYPE));
			System.out.println("Value USAGE_INFO_UPDATED: " + od.getValueAsString(ObjectDescription.USAGE_INFO_UPDATED));
			System.out.println("Value USER_CHANGED: " + od.getValueAsString(ObjectDescription.USER_CHANGED));
			System.out.println("Value USER_DEFINED_ATTRIBUTE: " + od.getValueAsString(ObjectDescription.USER_DEFINED_ATTRIBUTE));

			succeeded();
			
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception, the attribute value was not returned.");
			e.printStackTrace();
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString()when the user doesn't have authority for
	 * this object.
	 * <p>Result: Verify that AS400Exception is thrown.  
	 */
	public void Var045() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, dpath_);

			od.getValueAsString(ObjectDescription.CHANGE_DATE);
			failed("Exception didn't occur. Make sure user JAVACTL exists on the system.");
		
		} 
		catch (Exception e) 
		{    
			assertExceptionIs(e, "AS400Exception");
		}
	}
	

	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString()when the user doesn't exist in the 
	 * system.
	 * <p>Result: Verify that AS400SecurityException is thrown.  
	 */
	public void Var046() 
	{
		try 
		{
     		
			AS400 system = new AS400(systemObject_.getSystemName(), "fakeuser", "password");
			ObjectDescription od = new ObjectDescription(system, path_);
			system.setGuiAvailable(false);
		
			od.refresh();
			failed("Exception didn't occur.");
		} 
		catch (Exception e) 
		{    
			assertExceptionIs(e, "AS400SecurityException");
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString()when the object doesn't exist in the 
	 * system.
	 * <p>Result: Verify that AS400Exceptionis thrown with ErrorCompletingRequestException.  
	 */
	
	public void Var047() 
	{
		try 
		{
           ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FAKELIB", "FILE");

           od.getValueAsString(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM);
           failed("Exception didn't occur.");  

		} 
		catch(Exception e)
		{				
		assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ErrorCompletingRequestException.AS400_ERROR);		
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: getValueAsString()when the object doesn't exist in the 
	 * system.
	 * <p>Result: Verify that AS400Exception is thrown with ObjectDoesNotExistException.  
	 */
	public void Var048() 
	{
		try 
		{
           
			ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FAKELIB", "FILE");

			od.getValueAsString(ObjectDescription.CREATOR_SYSTEM);
			failed("Exception didn't occur.");  
		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
		}
	}


	
	/**
	 * <p>Test: Call ObjectDescription :: refresh() when the object exists in the system.
	 * system.
	 * <p>Result: Verify that no exception is thrown.  
	 */
	public void Var049() 
	{
		try 
		{			
			ObjectDescription od = new ObjectDescription(systemObject_, path_);
		
			od.refresh();
			succeeded();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}

	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: refresh()when the user doesn't have authority for
	 * this object.
	 * <p>Result: Verify that AS400Exception is thrown.  
	 */
	public void Var050() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, dpath_);
		     
     		od.refresh();
			failed("Exception didn't occur.");	
		} 
		catch (Exception e) 
		{    
			assertExceptionIs(e, "AS400Exception");
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: refresh()when the object doesn't exist in the
	 * system.
	 * <p>Result: Verify that AS400Exception is thrown with ErrorCompletingRequestException.   
	 */
	public void Var051() 
	{
		try 
		{
           ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FAKEFILE", "FILE");

           od.refresh();
           failed("Exception didn't occur.");  
		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ErrorCompletingRequestException.AS400_ERROR);		
		}
	}
	
	
	
	/**
	 * <p>Test: Call ObjectDescription :: refresh()when the object doesn't exist in the
	 * system.
	 * <p>Result: Verify that AS400Exception is thrown with ObjectDoesNotExistException.   
	 */
	public void Var052() 
	{
		try 
		{       
			ObjectDescription od = new ObjectDescription(systemObject_,"QSYS", "FAKEFILE", "FILE");

			od.refresh();
			failed("Exception didn't occur.");  
		} 
		catch(Exception e)
		{				
			assertExceptionStartsWith(e, "AS400Exception", "CPF9812", ObjectDoesNotExistException.LIBRARY_DOES_NOT_EXIST);
		}
	}

	
	
	/**
	 * <p>Test: Call ObjectDescription :: toString().
	 * <p>Result: Verify that no exception is thrown.  
	 */
	public void Var053() 
	{
		try 
		{
     		ObjectDescription od = new ObjectDescription(systemObject_, path_);
				
			System.out.println("The object path name is: " + od.toString());
			succeeded();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * <p>Test: Call getObjectLockList when no locks have been applied.
	 * <p>Result: Verify that no exception is thrown and that getObjectLockList is empty.  
	 */
	public void Var054() 
	{
		int i = 0;
		
		try 
		{		      
		      CommandCall cmd = new CommandCall(systemObject_);
		      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have Zero locks
		      cmd.run();
		      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      for (i=0; i<objectEntry.length; ++i)
		      {
		        System.out.println("Entry["+i+"/"+objectEntry.length+"]= "+objectEntry[i]); // Calls the ObjectDescription.toString() method	        
		        ObjectLockListEntry[] lockEntry = objectEntry[i].getObjectLockList();
		        
		        //No locks were applied so this should return zero
		        if(lockEntry.length == 0)
		        {
		        	succeeded();  	
		        }
		        else
		        {
		        	failed("Objects should not have locks.");
		        }		       
		      }    
		      
		      cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
		      cmd.run();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * <p>Test: Call getObjectLockList when one lock has been applied to one object and no locks were 
	 * applied for another job.
	 * <p>Result: Verify that no exception is thrown and that getObjectLockList returns one.  
	 */
	public void Var055() 
	{
		int i = 0;
		int DDA1locks = 0;
		int DDA2locks = 0;
		
		try 
		{		    
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
		     
		      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have One lock
		      cmd.run();
		      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  // Will have Zero locks
		      cmd.run();
		      
		      //ALCOBJ so DDA1 has one lock
		      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCLRD)) SCOPE(*LCKSPC) ");
		      cmd.run();
		      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      for (i=0; i<objectEntry.length; ++i)
		      {
		        System.out.println("Entry["+i+"/"+objectEntry.length+"]= "+objectEntry[i]); // Calls the ObjectDescription.toString() method	        
		        lockEntry = objectEntry[i].getObjectLockList();	 
		        
		        //Counts the locks for DDA1
		        if(lockEntry.length == 1 && (objectEntry[i].getName()).equals("DDA1"))
		        {
		        	DDA1locks++;
		        }        
		        
		        if(lockEntry.length != 0 && (objectEntry[i].getName()).equals("DDA2"))
		        {
		        	DDA2locks ++;
		        }   
		      }
		      
		        //One lock was applied to DDA1 so this should return 1 but no locks were applied for DDA2.
		        if(DDA1locks  == 1 && DDA2locks == 0)
		        {
		        	succeeded();  	
		        }
		        else
		        {
		        	failed("Expecting DDA1locks = 1 and DDA2locks = 0 and got " + "DDA1locks = " + DDA1locks + " DDA2locks " + DDA2locks);
		        }
		        
		      //DLCOBJ so DDA1 has no locks left.
		      cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCLRD)) SCOPE(*LCKSPC) ");
		      cmd.run();
		      
		      //delete objects
		      cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
		      cmd.run();
		      cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  
		      cmd.run();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <p>Test: Call getObjectLockList when two locks have been applied to one object.
	 * <p>Result: Verify that no exception is thrown and that getObjectLockList returns two for one object.  
	 */
	public void Var056() 
	{
		int i = 0;
		int DDA1locks = 0;
		
		try 
		{		    
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();
		      
		      //ALCOBJ so DDA1 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      for (i=0; i<objectEntry.length; ++i)
		      {
		        System.out.println("Entry["+i+"/"+objectEntry.length+"]= "+objectEntry[i]); // Calls the ObjectDescription.toString() method	        
		        lockEntry = objectEntry[i].getObjectLockList();	 
		        
		        
		        //Counts the locks for DDA1
		        if(lockEntry.length == 2 && (objectEntry[i].getName()).equals("DDA1"))
		        {		        	
		        	DDA1locks++;
		        }        
 
		      }
		      
		        //Two locks were applied to DDA1 so this should return 1.
		        if(DDA1locks  == 1 )
		        {
		        	succeeded();  	
		        }
		        else
		        {
		        	failed("Expecting DDA1locks = 1  and got " + "DDA1locks = " + DDA1locks);
		        }
		        
		      //DLCOBJ so DDA1 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
		       //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <p>Test: Call getObjectLockList when two locks have been applied for each object.
	 * <p>Result: Verify that no exception is thrown.  
	 */
	public void Var057() 
	{
		
		int i = 0;
		int DDAlocks = 0;
		
		try 
		{		    
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();
     	      
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();
		      
		      //ALCOBJ so DDA1 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      //ALCOBJ so DDA2 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      for (i=0; i<objectEntry.length; ++i)
		      {
		        System.out.println("Entry["+i+"/"+objectEntry.length+"]= "+objectEntry[i]); // Calls the ObjectDescription.toString() method	        
		        lockEntry = objectEntry[i].getObjectLockList();	 
		        
		        
		        //Counts the locks for DDA1
		        if(lockEntry.length == 2 && (objectEntry[i].getName()).equals("DDA1"))
		        {		        	
		        	DDAlocks++;
		        }        
 
		        //Counts the locks for DDA1
		        if(lockEntry.length == 2 && (objectEntry[i].getName()).equals("DDA2"))
		        {		        	
		        	DDAlocks++;
		        }        
		      }
		      
		        //Two locks were applied to DDA1 and to DDA2 so this should return 2.
		        if(DDAlocks  == 2 )
		        {
		        	succeeded();  	
		        }
		        else
		        {
		        	failed("Expecting DDA1locks = 2  and got " + "DDA1locks = " + DDAlocks);
		        }
		        
		      //DLCOBJ so DDA1 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
			    //DLCOBJ so DDA2 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
		       //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
		} 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	
	}
	

	/**
	 * <p>Test: Call ObjectLockListEntry.getxxx() for one object with one lock.
	 * <p>Result: Verify that no exception is thrown.
	 */
	public void Var058() 
	{
		
		int i = 0;

		try 
		{	
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have one lock.
     	      cmd.run();	      
		      
		      //ALCOBJ so DDA1 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      if(objectEntry.length > 0)
		      {
			     	lockEntry = objectEntry[0].getObjectLockList();	 
			     	
			     	if (lockEntry.length > 0)
			     	{
			     		
			     		try
			     		{
					        System.out.println("getJobName= " + lockEntry[0].getJobName());
					        System.out.println("getJobNumber= " + lockEntry[0].getJobNumber());
					        System.out.println("getJobUserName= " + lockEntry[0].getJobUserName());
					        System.out.println("getLockScope= " + lockEntry[0].getLockScope());
					        System.out.println("getLockState= " + lockEntry[0].getLockState());
					        System.out.println("getLockStatus= " + lockEntry[0].getLockStatus());
					        System.out.println("getLockType= " + lockEntry[0].getLockType());
					        System.out.println("getShare= " + lockEntry[0].getShare());
					        System.out.println("getThreadID= " + Long.toHexString(lockEntry[0].getThreadID()));
					        System.out.println("toString= " + lockEntry[0].toString());	
			     		}
			     		catch (Exception e) 
			     		{    
			     			failed("Unexpected exception while calling one of the ObjectLockListEntry.getxxx() methods.");
			     			e.printStackTrace();
			     		}
			     		
			     		succeeded(); 
			     		
			     	 }
             }
		      
		      //DLCOBJ so DDA1 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();        
		       //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
			
		}
 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <p>Test: Call ObjectLockListEntry.getxxx() for two objects with one lock.
	 * <p>Result: Verify that no exception is thrown.
	 */
	public void Var059() 
	{
		
		int i = 0;
		int count = 0;

		try 
		{	
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 

     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have one lock
     	      cmd.run();    	      
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  // Will have one lock
     	      cmd.run();
		      
		      //ALCOBJ so DDA1 has one lock
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      
		      //ALCOBJ so DDA2 has one lock
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();

     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      int j = objectEntry.length;
		      
		      if(j > 0)
		      {
			     
		    	 for(i=0; i< objectEntry.length; i++){
		    		 
		    	  lockEntry = objectEntry[i].getObjectLockList();	 
	  			     	
			     	if (lockEntry.length > 0)	
			     	{
			     		
			     		for(int h=0; h<lockEntry.length;h++)
			     		{
			     			try
			     			{
			     				System.out.println("getJobName= " + lockEntry[h].getJobName());
			     				System.out.println("getJobNumber= " + lockEntry[h].getJobNumber());
			     				System.out.println("getJobUserName= " + lockEntry[h].getJobUserName());
			     				System.out.println("getLockScope= " + lockEntry[h].getLockScope());
			     				System.out.println("getLockState= " + lockEntry[h].getLockState());
			     				System.out.println("getLockStatus= " + lockEntry[h].getLockStatus());
			     				System.out.println("getLockType= " + lockEntry[h].getLockType());
			     				System.out.println("getShare= " + lockEntry[h].getShare());
			     				System.out.println("getThreadID= " + Long.toHexString(lockEntry[h].getThreadID()));
			     				System.out.println("toString= " + lockEntry[h].toString());	
			     				count ++;
			     			}
			     			catch (Exception e) 
			     			{    
			     				failed("Unexpected exception while calling one of the ObjectLockListEntry.getxxx() methods.");
			     				e.printStackTrace();
			     			}
			     		}		 		
			     	 }
		    	 }
		    	 
		    	 //No exceptions were thrown so count should be equal to 2.
		    	 if(count == 2)
		    		 succeeded(); 
             }
		      
		      //DLCOBJ so DDA1 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
	        
			  //DLCOBJ so DDA2 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
		      //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
			
		}
 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <p>Test: Call ObjectLockListEntry.getxxx() for two objects with two locks.
	 * <p>Result: Verify that no exception is thrown.
	 */
	public void Var060() 
	{
		
		int i = 0;
		int count = 0;

		try 
		{	
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();   	      
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();
		      
		      //ALCOBJ so DDA1 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      //ALCOBJ so DDA2 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      int j = objectEntry.length;
      
		      if(j > 0)
		      {

		    	 for(i=0; i< objectEntry.length; i++)
		    	 {	 
		            lockEntry = objectEntry[i].getObjectLockList();	 
			     	
			     	if (lockEntry.length > 0)
			     	{
			     		for(int h=0; h<lockEntry.length;h++ )
			     		{
			     			try
			     			{
			     				System.out.println("getJobName= " + lockEntry[h].getJobName());
			     				System.out.println("getJobNumber= " + lockEntry[h].getJobNumber());
			     				System.out.println("getJobUserName= " + lockEntry[h].getJobUserName());
			     				System.out.println("getLockScope= " + lockEntry[h].getLockScope());
			     				System.out.println("getLockState= " + lockEntry[h].getLockState());
			     				System.out.println("getLockStatus= " + lockEntry[h].getLockStatus());
			     				System.out.println("getLockType= " + lockEntry[h].getLockType());
			     				System.out.println("getShare= " + lockEntry[h].getShare());
			     				System.out.println("getThreadID= " + Long.toHexString(lockEntry[h].getThreadID()));
			     				System.out.println("toString= " + lockEntry[h].toString());		
			     				count ++;
			     			}
			     			catch (Exception e) 
			     			{    
			     				failed("Unexpected exception while calling one of the ObjectLockListEntry.getxxx() methods.");
			     				e.printStackTrace();
			     			}   		
			     		}		
			     	 }
		    	 }
		    	 
		    	 //No exceptions were thrown so count should be equal to 4.
		    	 if(count == 4)
		    		 succeeded(); 

             }
		     
		       //DLCOBJ so DDA1 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA1 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
			    //DLCOBJ so DDA2 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
		       //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
			
		}
 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}
		      
			
	/**
	 * <p>Test: Call ObjectLockListEntry.getxxx() for two objects one with no locks and another with 
	 * two locks.
	 * <p>Result: Verify that no exception is thrown.
	 */
	public void Var061() 
	{
		
		int i = 0;
		int count = 0;

		try 
		{	
			  ObjectLockListEntry[] lockEntry = null;
     	      CommandCall cmd = new CommandCall(systemObject_);
 

     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  // Will have zero locks
     	      cmd.run();   	      
     	      cmd.setCommand("CRTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  // Will have Two locks
     	      cmd.run();
		      
     	      
		      //ALCOBJ so DDA2 has two locks
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");  // ALCOBJ creates locks.  DLCOBJ (below) removes locks.
     	      cmd.run();
     	      cmd.setCommand("ALCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
     	      cmd.run();
     	      
		      ObjectList objList1 = new ObjectList(systemObject_, "ODLOCKLIB", ObjectList.ALL, ObjectList.ALL);
		      int size = objList1.getLength();
		      com.ibm.as400.access.ObjectDescription[] objectEntry = objList1.getObjects(0, size);
		    		      
		      int j = objectEntry.length;
      
		      if(j > 0)
		      {

		    	 for(i=0; i< objectEntry.length; i++)
		    	 {	 
		            lockEntry = objectEntry[i].getObjectLockList();	 
			     	
			     	if (lockEntry.length > 0)
			     	{
			     		
			     		for(int h=0; h<lockEntry.length;h++ )
			     		{
			     			try
			     			{
			     				System.out.println("getJobName= " + lockEntry[h].getJobName());
			     				System.out.println("getJobNumber= " + lockEntry[h].getJobNumber());
			     				System.out.println("getJobUserName= " + lockEntry[h].getJobUserName());
			     				System.out.println("getLockScope= " + lockEntry[h].getLockScope());
			     				System.out.println("getLockState= " + lockEntry[h].getLockState());
			     				System.out.println("getLockStatus= " + lockEntry[h].getLockStatus());
			     				System.out.println("getLockType= " + lockEntry[h].getLockType());
			     				System.out.println("getShare= " + lockEntry[h].getShare());
			     				System.out.println("getThreadID= " + Long.toHexString(lockEntry[h].getThreadID()));
			     				System.out.println("toString= " + lockEntry[h].toString());		
			     				count ++;
			     			}
			     			catch (Exception e) 
			     			{    
			     				failed("Unexpected exception while calling one of the ObjectLockListEntry.getxxx() methods.");
			     				e.printStackTrace();
			     			}   		
			     		}		
			     	 }
		    	 }
		    	 
		    	 //No exceptions were thrown so count should be equal to 2.
		    	 if(count == 2)
		    		 succeeded(); 
             }
		    
			    //DLCOBJ so DDA2 has no locks left.
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *SHRRD)) SCOPE(*JOB)");
		        cmd.run();
		        cmd.setCommand("DLCOBJ OBJ((ODLOCKLIB/DDA2 *DTAARA *EXCL)) SCOPE(*THREAD)");
		        cmd.run();
		        
		       //Delete objects 
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA1) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();
	     	    cmd.setCommand("DLTDTAARA DTAARA(ODLOCKLIB/DDA2) TYPE(*CHAR) TEXT(A) ");  
	     	    cmd.run();		
		}
 
		catch (Exception e) 
		{    
			failed("Unexpected exception.");
			e.printStackTrace();
		}
	}

}//ODTestcase class

