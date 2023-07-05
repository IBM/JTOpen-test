///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ObjectListTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.util.ArrayList;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectList;


/**
Testcase ObjectListTestcase.
**/
public class ObjectListTestcase extends Testcase {


    /**
    Successful construction of an ObjectList using the default constructor.
    ObjectList(AS400 system)
    **/
    public void Var001()
    {
        try {
            ObjectList objectList = new ObjectList(systemObject_);
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     * Default constructor
     * Ensure NullPointer is thrown if System parameter is null     *
     */
    public void Var002()
    {
        try {
            ObjectList objectList = new ObjectList(null);
            failed("Exception didn't occur.");
        } catch (Exception e) {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
    Successful construction of an ObjectList using the constructor.
    ObjectList(AS400 system,String ALL,String ALL,String ALL)
    **/
    public void Var003()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.ALL,ObjectList.ALL,ObjectList.ALL);
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    Ensure NullPointer is throw if the ObjectLybrary is null using the constructor.
    ObjectList(AS400 system,String ALL,String ALL,String ALL)
    **/
    public void Var004()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,null,ObjectList.ALL,ObjectList.ALL);
            failed("Exception didn't occur.");
        } catch (Exception e) {
            assertExceptionIs(e, "NullPointerException", "objectLibrary");
        }
    }

    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	ObjectList(AS400 system,String objectLibrary,String objectName,String objectType)
     */
    public void Var005()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }

    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	ObjectList(AS400 system,String objectLibrary,String objectName,String objectType)
     */
    public void Var006()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.CURRENT_LIBRARY ,ObjectList.LIBRARY_LIST ,"*FILE");
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	ObjectList(AS400 system,String objectLibrary,String objectName,String objectType)
     */
    public void Var007()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.LIBRARY_LIST ,"QSYS","*LIB");
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	ObjectList(AS400 system,String objectLibrary,String objectName,String objectType)
     */
    public void Var008()
    {
	
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.LIBRARY_LIST ,ObjectList.IBM,"*LIB");
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	ObjectList(AS400 system,String objectLibrary,String objectName,String objectType)
     */
    public void Var009()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.USER_LIBRARY_LIST ,ObjectList.LIBRARY_LIST,"*FILE");
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	public ObjectList(AS400 system,
     *				String objectLibrary,
     *				String objectName,
     *             String objectType,
     *             String aspDeviceName)
     */
    public void Var010()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB",ObjectList.ASP_NAME_ALL );
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }

    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	public ObjectList(AS400 system,
     *				String objectLibrary,
     *				String objectName,
     *             String objectType,
     *             String aspDeviceName)
     */
    public void Var011()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.CURRENT_LIBRARY ,ObjectList.LIBRARY_LIST ,"*FILE",ObjectList.ASP_NAME_ALLAVL);
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	public ObjectList(AS400 system,
     *				String objectLibrary,
     *				String objectName,
     *             String objectType,
     *             String aspDeviceName)
     */
    public void Var012()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.LIBRARY_LIST ,"QSYS","*LIB",ObjectList.ASP_NAME_CURASPGRP);
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Succesfull construction using system,objectLibrary,objectName,objectType 
     *	public ObjectList(AS400 system,
     *				String objectLibrary,
     *				String objectName,
     *             String objectType,
     *             String aspDeviceName)
     */
    public void Var013()
    {
        try {
            
            ObjectList objectList = new ObjectList(systemObject_,ObjectList.LIBRARY_LIST ,ObjectList.IBM,"*LIB",ObjectList.ASP_NAME_SYSBAS );
            succeeded();
        } catch (Exception e) {
            failed(e, "Unexpected exception.");
        }

    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Ensure Nullpointer is thrown if authority is null
     * 
     */
    public void Var014(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.addLibraryAuthorityCriteria(null);
            failed("Not Exception was thrown.");

        } catch (Exception e) {
            if (exceptionIs(e, "NullPointerException")) {
                succeeded();
            } else {
                failed(e, "Wrong exception info.");
            }
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, authority added 
     * 
     */
    public void Var015(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_ALL);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems to add authority criteria.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, authority changed 
     * 
     */
    public void Var016(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_CHANGE);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems changing authority criteria.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, use authority
     * 
     */
    public void Var017(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_USE);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *OBJOPR.
     * 
     */
    public void Var018(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_OBJECT_OPERATIONAL);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria, object operational.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *OBJMGT.
     * 
     */
    public void Var019(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_OBJECT_MANAGEMENT);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria, object management.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *OBJEXIST.
     * 
     */
    public void Var020(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_OBJECT_EXISTENCE);
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria, object existence.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *OBJALTER.
     * 
     */
    public void Var021(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_OBJECT_ALTER );
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria, object alter.");
        }
    }


    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *OBJREF.
     * 
     */
    public void Var022(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_OBJECT_REFERENCE );
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems use authority criteria, object reference.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *READ.
     * 
     */
    public void Var023(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_DATA_READ);
            succeeded();        

        } catch (Exception e) {
            failed(e, "ProblemS to set READ authority.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *ADD.
     * 
     */
    public void Var024(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_DATA_ADD);
            succeeded();        

        } catch (Exception e) {
            failed(e, "ProblemS to set ADD authority.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *UPDATE.
     * 
     */
    public void Var025(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_DATA_UPDATE);
            succeeded();        

        } catch (Exception e) {
            failed(e, "ProblemS to set UPDATE authority.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *DELETE.
     * 
     */
    public void Var026(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_DATA_DELETE );
            succeeded();        

        } catch (Exception e) {
            failed(e, "ProblemS to set DELETE authority.");
        }
    }
    /**
     * Method tested: addLibraryAuthorityCriteria(String authority)
     * Successfull, set an object authority of *EXECUTE.
     * 
     */
    public void Var027(){

        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addLibraryAuthorityCriteria(ObjectList.AUTH_DATA_EXECUTE );
            succeeded();        

        } catch (Exception e) {
            failed(e, "Problems to set EXECUTE authority.");
        }
    }

    /**
     * Method tested: addLibraryAuthorityCriteria(String authority);
     * An exception should be thrown if an invalid criteria is specified.
     */
    public void Var028(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            objList.addLibraryAuthorityCriteria("MY_CRITERIA");
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }


    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful ALLOW_CHANGE_BY_PROGRAM attribute added. 
     * 
     */
    public void Var029(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding ALLOW_CHANGE_BY_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful APAR attribute added. 
     * 
     */
    public void Var030(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.APAR);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding APAR attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful AUDITING attribute added. 
     * 
     */
    public void Var031(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.AUDITING);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding AUDITING attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful CHANGE_DATE attribute added. 
     * 
     */
    public void Var032(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.CHANGE_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CHANGE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful CHANGED_BY_PROGRAM attribute added. 
     * 
     */
    public void Var033(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.CHANGED_BY_PROGRAM);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CHANGED_BY_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful COMPILER attribute added. 
     * 
     */
    public void Var034(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.COMPILER);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding COMPILER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful COMPRESSION attribute added. 
     * 
     */
    public void Var035(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.COMPRESSION);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding COMPRESSION attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful CREATION_DATE attribute added. 
     * 
     */
    public void Var036(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.CREATION_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATION_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful CREATOR_SYSTEM attribute added. 
     * 
     */
    public void Var037(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.CREATOR_SYSTEM);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATOR_SYSTEM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful CREATOR_USER_PROFILE attribute added. 
     * 
     */
    public void Var038(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.CREATOR_USER_PROFILE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATOR_USER_PROFILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful DAYS_USED attribute added. 
     * 
     */
    public void Var039(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.DAYS_USED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DAYS_USED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful DIGITALLY_SIGNED attribute added. 
     * 
     */
    public void Var040(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.DIGITALLY_SIGNED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful DIGITALLY_SIGNED_MULTIPLE attribute added. 
     * 
     */
    public void Var041(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.DIGITALLY_SIGNED_MULTIPLE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED_MULTIPLE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful DIGITALLY_SIGNED_TRUSTED attribute added. 
     * 
     */
    public void Var042(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.DIGITALLY_SIGNED_TRUSTED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED_TRUSTED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful DOMAIN attribute added. 
     * 
     */
    public void Var043(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.DOMAIN);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DOMAIN attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful EXTENDED_ATTRIBUTE attribute added. 
     * 
     */
    public void Var044(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.EXTENDED_ATTRIBUTE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding EXTENDED_ATTRIBUTE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful JOURNAL attribute added. 
     * 
     */
    public void Var045(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.JOURNAL);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful JOURNAL_IMAGES attribute added. 
     * 
     */
    public void Var046(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.JOURNAL_IMAGES);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_IMAGES attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful JOURNAL_OMITTED_ENTRIES attribute added. 
     * 
     */
    public void Var047(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.JOURNAL_OMITTED_ENTRIES);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_OMITTED_ENTRIES attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful JOURNAL_START_DATE attribute added. 
     * 
     */
    public void Var048(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.JOURNAL_START_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_START_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful JOURNAL_STATUS attribute added. 
     * 
     */
    public void Var049(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.JOURNAL_STATUS);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_STATUS attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful LAST_USED_DATE attribute added. 
     * 
     */
    public void Var050(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.LAST_USED_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LAST_USED_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful LIBRARY attribute added. 
     * 
     */
    public void Var051(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.LIBRARY);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LIBRARY attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful LIBRARY_ASP_DEVICE_NAME attribute added. 
     * 
     */
    public void Var052(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.LIBRARY_ASP_DEVICE_NAME);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LIBRARY_ASP_DEVICE_NAME attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful LIBRARY_ASP_NUMBER attribute added. 
     * 
     */
    public void Var053(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.LIBRARY_ASP_NUMBER);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LIBRARY_ASP_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful LICENSED_PROGRAM attribute added. 
     * 
     */
    public void Var054(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.LICENSED_PROGRAM);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LICENSED_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful NAME attribute added. 
     * 
     */
    public void Var055(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.NAME);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding NAME attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OBJECT_ASP_DEVICE_NAME attribute added. 
     * 
     */
    public void Var056(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OBJECT_ASP_DEVICE_NAME);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_ASP_DEVICE_NAME attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OBJECT_ASP_NUMBER attribute added. 
     * 
     */
    public void Var057(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OBJECT_ASP_NUMBER);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_ASP_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OBJECT_LEVEL attribute added. 
     * 
     */
    public void Var058(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OBJECT_LEVEL);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_LEVEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OBJECT_SIZE attribute added. 
     * 
     */
    public void Var059(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OBJECT_SIZE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_SIZE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful ORDER_IN_LIBRARY_LIST attribute added. 
     * 
     */
    public void Var060(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.ORDER_IN_LIBRARY_LIST);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding ORDER_IN_LIBRARY_LIST attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OVERFLOWED_ASP attribute added. 
     * 
     */
    public void Var061(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OVERFLOWED_ASP);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OVERFLOWED_ASP attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful OWNER attribute added. 
     * 
     */
    public void Var062(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.OWNER);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OWNER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful PRIMARY_GROUP attribute added. 
     * 
     */
    public void Var063(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.PRIMARY_GROUP);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding PRIMARY_GROUP attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful PTF attribute added. 
     * 
     */
    public void Var064(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.PTF);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding PTF attribute.");
        }
    }

    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful RESET_DATE attribute added. 
     * 
     */
    public void Var065(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.RESET_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding RESET_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful RESTORE_DATE attribute added. 
     * 
     */
    public void Var066(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.RESTORE_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding RESTORE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_ACTIVE_DATE attribute added. 
     * 
     */
    public void Var067(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_ACTIVE_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_ACTIVE_DATE attribute.");
        }
    }

    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_COMMAND attribute added. 
     * 
     */
    public void Var068(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_COMMAND);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_COMMAND attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_DATE attribute added. 
     * 
     */
    public void Var069(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_DEVICE attribute added. 
     * 
     */
    public void Var070(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_DEVICE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_DEVICE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_FILE attribute added. 
     * 
     */
    public void Var071(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_FILE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_FILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_LABEL attribute added. 
     * 
     */
    public void Var072(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_LABEL);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_LABEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_SEQUENCE_NUMBER attribute added. 
     * 
     */
    public void Var073(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_SEQUENCE_NUMBER);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_SEQUENCE_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_SIZE attribute added. 
     * 
     */
    public void Var074(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_SIZE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_SIZE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SAVE_VOLUME_ID attribute added. 
     * 
     */
    public void Var075(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SAVE_VOLUME_ID);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_VOLUME_ID attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SOURCE_FILE attribute added. 
     * 
     */
    public void Var076(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SOURCE_FILE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SOURCE_FILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SOURCE_FILE_UPDATED_DATE attribute added. 
     * 
     */
    public void Var077(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SOURCE_FILE_UPDATED_DATE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SOURCE_FILE_UPDATED_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful STORAGE_STATUS attribute added. 
     * 
     */
    public void Var078(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.STORAGE_STATUS);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding STORAGE_STATUS attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful SYSTEM_LEVEL attribute added. 
     * 
     */
    public void Var079(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.SYSTEM_LEVEL);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SYSTEM_LEVEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful TEXT_DESCRIPTION attribute added. 
     * 
     */
    public void Var080(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.TEXT_DESCRIPTION);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding TEXT_DESCRIPTION attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful TYPE attribute added. 
     * 
     */
    public void Var081(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.TYPE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding TYPE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful USAGE_INFO_UPDATED attribute added. 
     * 
     */
    public void Var082(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.USAGE_INFO_UPDATED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USAGE_INFO_UPDATED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful USER_CHANGED attribute added. 
     * 
     */
    public void Var083(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.USER_CHANGED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USER_CHANGED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToRetrieve(int attribute)
     * Successful USER_DEFINED_ATTRIBUTE attribute added. 
     * 
     */
    public void Var084(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToRetrieve(ObjectDescription.USER_DEFINED_ATTRIBUTE);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USER_DEFINED_ATTRIBUTE attribute.");
        }
    }

    /**
     * Method tested:  addObjectAttributeToRetrieve(int attribute)
     * Should throw an exception when an invalid attribute is specified.
     */
    public void Var085() {
        try{
            ObjectList objList = new ObjectList(systemObject_);
            objList.addObjectAttributeToRetrieve(-98764);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful ALLOW_CHANGE_BY_PROGRAM attribute added. 
     * 
     */
    public void Var086(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.ALLOW_CHANGE_BY_PROGRAM,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding ALLOW_CHANGE_BY_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful APAR attribute added. 
     * 
     */
    public void Var087(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.APAR,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding APAR attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful AUDITING attribute added. 
     * 
     */
    public void Var088(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.AUDITING,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding AUDITING attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful CHANGE_DATE attribute added. 
     * 
     */
    public void Var089(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.CHANGE_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CHANGE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful CHANGED_BY_PROGRAM attribute added. 
     * 
     */
    public void Var090(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.CHANGED_BY_PROGRAM,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CHANGED_BY_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful COMPILER attribute added. 
     * 
     */
    public void Var091(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.COMPILER,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding COMPILER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful COMPRESSION attribute added. 
     * 
     */
    public void Var092(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.COMPRESSION,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding COMPRESSION attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful CREATION_DATE attribute added. 
     * 
     */
    public void Var093(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.CREATION_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATION_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful CREATOR_SYSTEM attribute added. 
     * 
     */
    public void Var094(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.CREATOR_SYSTEM,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATOR_SYSTEM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful CREATOR_USER_PROFILE attribute added. 
     * 
     */
    public void Var095(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.CREATOR_USER_PROFILE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding CREATOR_USER_PROFILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful DAYS_USED attribute added. 
     * 
     */
    public void Var096(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.DAYS_USED,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DAYS_USED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful DIGITALLY_SIGNED attribute added. 
     * 
     */
    public void Var097(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.DIGITALLY_SIGNED,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful DIGITALLY_SIGNED_MULTIPLE attribute added. 
     * 
     */
    public void Var098(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.DIGITALLY_SIGNED_MULTIPLE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED_MULTIPLE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful DIGITALLY_SIGNED_TRUSTED attribute added. 
     * 
     */
    public void Var099(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.DIGITALLY_SIGNED_TRUSTED,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DIGITALLY_SIGNED_TRUSTED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful DOMAIN attribute added. 
     * 
     */
    public void Var100(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.DOMAIN,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding DOMAIN attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful EXTENDED_ATTRIBUTE attribute added. 
     * 
     */
    public void Var101(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.EXTENDED_ATTRIBUTE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding EXTENDED_ATTRIBUTE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful JOURNAL attribute added. 
     * 
     */
    public void Var102(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.JOURNAL,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful JOURNAL_IMAGES attribute added. 
     * 
     */
    public void Var103(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.JOURNAL_IMAGES,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_IMAGES attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful JOURNAL_OMITTED_ENTRIES attribute added. 
     * 
     */
    public void Var104(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.JOURNAL_OMITTED_ENTRIES,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_OMITTED_ENTRIES attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful JOURNAL_START_DATE attribute added. 
     * 
     */
    public void Var105(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.JOURNAL_START_DATE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_START_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful JOURNAL_STATUS attribute added. 
     * 
     */
    public void Var106(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.JOURNAL_STATUS,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding JOURNAL_STATUS attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful LAST_USED_DATE attribute added. 
     * 
     */
    public void Var107(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.LAST_USED_DATE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LAST_USED_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Should throw an exception when LIBRARY attribute added. 
     * 
     */
    public void Var108(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.LIBRARY,false);
            failed("Didn't throw exception.");

        } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful LIBRARY_ASP_DEVICE_NAME attribute added. 
     * 
     */
    public void Var109(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.LIBRARY_ASP_DEVICE_NAME,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LIBRARY_ASP_DEVICE_NAME attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful LIBRARY_ASP_NUMBER attribute added. 
     * 
     */
    public void Var110(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.LIBRARY_ASP_NUMBER,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LIBRARY_ASP_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful LICENSED_PROGRAM attribute added. 
     * 
     */
    public void Var111(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.LICENSED_PROGRAM,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding LICENSED_PROGRAM attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Should throw an exception when NAME attribute added. 
     * 
     */
    public void Var112(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.NAME,false);
            failed("Didn't throw exception.");

        } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OBJECT_ASP_DEVICE_NAME attribute added. 
     * 
     */
    public void Var113(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OBJECT_ASP_DEVICE_NAME,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_ASP_DEVICE_NAME attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OBJECT_ASP_NUMBER attribute added. 
     * 
     */
    public void Var114(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OBJECT_ASP_NUMBER,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_ASP_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OBJECT_LEVEL attribute added. 
     * 
     */
    public void Var115(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OBJECT_LEVEL,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_LEVEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OBJECT_SIZE attribute added. 
     * 
     */
    public void Var116(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OBJECT_SIZE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OBJECT_SIZE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful ORDER_IN_LIBRARY_LIST attribute added. 
     * 
     */
    public void Var117(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.ORDER_IN_LIBRARY_LIST,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding ORDER_IN_LIBRARY_LIST attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OVERFLOWED_ASP attribute added. 
     * 
     */
    public void Var118(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OVERFLOWED_ASP,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OVERFLOWED_ASP attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful OWNER attribute added. 
     * 
     */
    public void Var119(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.OWNER,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding OWNER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful PRIMARY_GROUP attribute added. 
     * 
     */
    public void Var120(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.PRIMARY_GROUP,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding PRIMARY_GROUP attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful PTF attribute added. 
     * 
     */
    public void Var121(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.PTF,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding PTF attribute.");
        }
    }

    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful RESET_DATE attribute added. 
     * 
     */
    public void Var122(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.RESET_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding RESET_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful RESTORE_DATE attribute added. 
     * 
     */
    public void Var123(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.RESTORE_DATE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding RESTORE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_ACTIVE_DATE attribute added. 
     * 
     */
    public void Var124(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_ACTIVE_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_ACTIVE_DATE attribute.");
        }
    }

    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_COMMAND attribute added. 
     * 
     */
    public void Var125(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_COMMAND,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_COMMAND attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_DATE attribute added. 
     * 
     */
    public void Var126(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_DEVICE attribute added. 
     * 
     */
    public void Var127(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_DEVICE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_DEVICE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_FILE attribute added. 
     * 
     */
    public void Var128(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_FILE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_FILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_LABEL attribute added. 
     * 
     */
    public void Var129(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_LABEL,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_LABEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_SEQUENCE_NUMBER attribute added. 
     * 
     */
    public void Var130(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_SEQUENCE_NUMBER,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_SEQUENCE_NUMBER attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_SIZE attribute added. 
     * 
     */
    public void Var131(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_SIZE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_SIZE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SAVE_VOLUME_ID attribute added. 
     * 
     */
    public void Var132(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SAVE_VOLUME_ID,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SAVE_VOLUME_ID attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SOURCE_FILE attribute added. 
     * 
     */
    public void Var133(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SOURCE_FILE,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SOURCE_FILE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SOURCE_FILE_UPDATED_DATE attribute added. 
     * 
     */
    public void Var134(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SOURCE_FILE_UPDATED_DATE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SOURCE_FILE_UPDATED_DATE attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful STORAGE_STATUS attribute added. 
     * 
     */
    public void Var135(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.STORAGE_STATUS,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding STORAGE_STATUS attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful SYSTEM_LEVEL attribute added. 
     * 
     */
    public void Var136(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.SYSTEM_LEVEL,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding SYSTEM_LEVEL attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful TEXT_DESCRIPTION attribute added. 
     * 
     */
    public void Var137(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.TEXT_DESCRIPTION,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding TEXT_DESCRIPTION attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Should throw exception when TYPE attribute added. 
     * 
     */
    public void Var138(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.TYPE,true);
            failed("Didn't throw exception.");

        } catch (Exception e) {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful USAGE_INFO_UPDATED attribute added. 
     * 
     */
    public void Var139(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.USAGE_INFO_UPDATED,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USAGE_INFO_UPDATED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute,boolean sortOrder)
     * Successful USER_CHANGED attribute added. 
     * 
     */
    public void Var140(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.USER_CHANGED,true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USER_CHANGED attribute.");
        }
    }
    /**
     * Method tested: addObjectAttributeToSortOn(int attribute)
     * Successful USER_DEFINED_ATTRIBUTE attribute added. 
     * 
     */
    public void Var141(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAttributeToSortOn(ObjectDescription.USER_DEFINED_ATTRIBUTE,false);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding USER_DEFINED_ATTRIBUTE attribute.");
        }
    }  

    /**
     * Method tested:  addObjectAttributeToSortOn(int attribute)
     * Should thrown an exception when an invalid attribute is specified.
     */
    public void Var142(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            objList.addObjectAttributeToSortOn(-31765, false);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Ensure that NullPointerException is thrown.
     * 
     */
    public void Var143(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(null);
            failed("Exception didn't occur");

        } catch (Exception e) {
            if (exceptionIs(e, "NullPointerException")) {
                succeeded();
            } else {
                failed(e, "Wrong exception info.");
            }
        }
    }  
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_ALL authority added. 
     * 
     */
    public void Var144(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_ALL);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding AUTH_ALL attribute.");
        }
    }  
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_CHANGE authority added. 
     * 
     */
    public void Var145(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_CHANGE );
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding AUTH_CHANGE attribute.");
        }
    }  
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_USE authority added. 
     * 
     */
    public void Var146(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_USE );
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info, adding AUTH_USE attribute.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_LIST_MANAGEMENT  authority added. 
     * 
     */
    public void Var147(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_LIST_MANAGEMENT  );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding AUTH_LIST_MANAGEMENT  attribute, check object type *AUTL.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_OBJECT_OPERATIONAL  authority added. 
     * 
     */
    public void Var148(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_OBJECT_OPERATIONAL );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding AUTH_USE authority.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_OBJECT_MANAGEMENT   authority added. 
     * 
     */
    public void Var149(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_OBJECT_MANAGEMENT );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding AUTH_OBJECT_MANAGEMENT  authority.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful AUTH_OBJECT_EXISTENCE   authority added. 
     * 
     */
    public void Var150(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_OBJECT_EXISTENCE );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding AUTH_OBJECT_EXISTENCE  authority.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Successful added several authorities added. 
     * 
     */
    public void Var151(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_OBJECT_ALTER  );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_OBJECT_REFERENCE   );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_READ   );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_ADD  );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_UPDATE   );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_DELETE   );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_EXECUTE  );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding several authorities.");
        }
    }
    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * If AUTH_ANY authority is specified, no other values can be specified. @A2C
     * 
     */
    public void Var152(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_ANY );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_READ  );
            failed("Didn't throw exception.");

        } catch (Exception e) {
            succeeded();           
            //failed(e, "Failed, adding AUTH_ANY authority.");
        }
    }

    /**
     * Method tested:  addObjectAuthorityCriteria(String authority)
     * Should throw an exception when an invalid authority is specified.
     */
    public void Var153(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            objList.addObjectAuthorityCriteria("MY_CRITERIA");
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Successful STATUS_NO_AUTHORITY status added. 
     * 
     */
    public void Var154(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectSelectionCriteria(ObjectDescription.STATUS_NO_AUTHORITY);
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding STATUS_NO_AUTHORITY  status.");
        }
    }
    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Successful STATUS_DAMAGED status added. 
     * 
     */
    public void Var155(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.addObjectSelectionCriteria(ObjectDescription.STATUS_DAMAGED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding STATUS_DAMAGED status.");
        }
    }
    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Successful STATUS_LOCKED status added. 
     * 
     */
    public void Var156(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.addObjectSelectionCriteria(ObjectDescription.STATUS_LOCKED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding STATUS_LOCKED status.");
        }
    }
    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Successful STATUS_PARTIALLY_DAMAGED status added. 
     * 
     */
    public void Var157(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.addObjectSelectionCriteria(ObjectDescription.STATUS_PARTIALLY_DAMAGED);
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding STATUS_PARTIALLY_DAMAGED status.");
        }
    }
    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Successful STATUS_ANY status added. 
     * 
     */
    public void Var158(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.addObjectSelectionCriteria(ObjectList.STATUS_ANY );
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, adding STATUS_ANY status.");
        }
    }

    /**
     * Method tested: addObjectSelectionCriteria(byte status)
     * Should throw an exception when an invalid status is specified.
     */
    public void Var159(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            objList.addObjectSelectionCriteria((byte)0xFF);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     * Method tested: clearLibraryAuthorityCriteria()
     * Successful cleaning library authority criteria.
     * 
     */
    public void Var160(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.clearLibraryAuthorityCriteria();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, cleaning library authority criteria.");
        }
    }
    /**
     * Method tested: clearObjectAttributesToRetrieve()
     * Successful cleaning library authority criteria.
     * 
     */
    public void Var161(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.clearObjectAttributesToRetrieve();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, cleaning attributes to retrieve.");
        }
    }
    /**
     * Method tested: clearObjectAttributesToSortOn()
     * Successful cleaning library authority criteria.
     * 
     */
    public void Var162(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.clearObjectAttributesToSortOn();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, cleaning attributes to sort on.");
        }
    } 
    /**
     * Method tested: clearObjectAuthorityCriteria()
     * Successful cleaning library authority criteria.
     * 
     */
    public void Var163(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.clearObjectAuthorityCriteria();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, cleaning authority criteria.");
        }
    }
    /**
     * Method tested: clearObjectSelectionCriteria()
     * Successful cleaning library authority criteria.
     * 
     */
    public void Var164(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_);
            objList.clearObjectSelectionCriteria();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed, cleaning object selection criteria.");
        }
    }
    /**
     * Method tested: close()
     * Successful Closes the object list on the system. 
     * This releases any system resources previously in use by this object list.
     * 
     */
    public void Var165(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectSelectionCriteria(ObjectDescription.STATUS_LOCKED);
            objList.close();
            succeeded();

        } catch (Exception e) {
            failed(e, "Failed,problems to close object.");
        }
    }
    /**
     * Method tested: getAspDeviceName()
     * Successful getting AspDeviceName.
     * 
     */
    public void Var166(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            String aspDevice=objList.getAspDeviceName();
            if (aspDevice==null) {
                succeeded();
            }

        } catch (Exception e) {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     * Method tested:  getAspDeviceName()
     * Successful getting asp device name.
     */
    public void Var167(){
        try{
            ObjectList objList = new ObjectList(systemObject_, ObjectList.ALL_USER, ObjectList.ALL_USER, "*LIB", "MY_ASPNAME");
            String aspDevice = objList.getAspDeviceName();
            if(aspDevice.equals("MY_ASPNAME"))
                succeeded();
            else
                failed("MY_ASPNAME != " + aspDevice);
        }
        catch(Exception e){
            failed("Unexpected exception.");
        }
    }
    /**
     * Method tested: getAspSearchType()
     * Successful getting AspSearchType.
     * 
     */
    public void Var168(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.setAspSearchType(ObjectList.ASP_SEARCH_TYPE_ASPGRP);
            String aspSearchType=objList.getAspSearchType();
            if (aspSearchType==ObjectList.ASP_SEARCH_TYPE_ASPGRP) {
                succeeded();
            } else {
                failed("no ASP search type name has been set.");
            }

        } catch (Exception e) {
            if (exceptionIs(e, "ExtendedIllegalArgumentException")) {
                failed(e,"Failed to set ASPSearchType");
            } else {
                failed(e,"Failed trying to getAspSearchType.");
            }
        }
    }

    /**
     * Method tested:  getAspSearchType()
     * Successful getting Asp search type
     */
    public void Var169(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            String aspSearchType = objList.getAspSearchType();
            if(aspSearchType == ObjectList.ASP_SEARCH_TYPE_ASP)
                succeeded();
            else
                failed("Expected " + ObjectList.ASP_SEARCH_TYPE_ASP + " but received " + aspSearchType);
        }
        catch(Exception e){
            failed("Unexpected exception.");
        }
    }
    /**
     * Method tested: getLength()
     * Successful, Returns the number of objects in the object list.
     * 
     */
    public void Var170(){   
        if (TestDriverStatic.brief_)
        {
          notApplicable("Skipping long-running variation.");
          return;
        }
	if (checkNotGroupTest())
        try {
            // It takes too long to get all the objects on the system 
            // ObjectList objList=new ObjectList(systemObject_);
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL,ObjectList.ALL,"*LIB");

            int length=objList.getLength();
            if (length>0) {
                succeeded();
            } else {
                failed("Problems obtaining list of objects.");
            }

        } catch (Exception e) {
            failed(e,"Failed trying to get object list.");      
        }
    }
    /**
     * Method tested: getLibrary()
     * Successful, Returns the library used to filter this list.
     * 
     */
    public void Var171(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            String library=objList.getLibrary();
            if (library !=null) {
                succeeded();
            } else {
                failed("Problems obtaining library used to filter this list.");
            }

        } catch (Exception e) {
            failed(e,"Failed trying to get library used to filter this list.");         
        }
    }
    /**
     * Method tested: getName()
     * Successful, Returns the object name used to filter this list.
     * 
     */
    public void Var172(){   
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            String name=objList.getName();
            if (name !=null) {
                succeeded();
            } else {
                failed("Problems obtaining the object name used to filter this list.");
            }

        } catch (Exception e) {
            failed(e,"Failed trying to get the object name used to filter this list.");         
        }
    }
    /**
     * Method tested: getObjects()
     * Successful, Returns the list of objects in the object list.
     * 
     */
    public void Var173(){   
        try {
            

            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL,ObjectList.ALL,"*LIB");
            Enumeration enumeration=objList.getObjects();
            ArrayList objDecList=new ArrayList();
            while (enumeration.hasMoreElements()) {
                ObjectDescription objDesc=(ObjectDescription)enumeration.nextElement();
                objDecList.add(objDesc.getName());
            }
            if (objDecList!=null && objDecList.size()>0) {
                succeeded();
            } else {
                failed("No objects returned in the list");
            }

        } catch (Exception e) {
            failed(e,"Failed trying to get he list of objects in the object list.");        
        }
    }


    /**
     * Method tested: getObjects(int listOffset,int number)
     * Successful, Returns a subset of the list of objects.
     * 
     */
    public void Var174(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL,ObjectList.ALL,"*LIB");
            ObjectDescription[] objdes = objList.getObjects(-1, 1);
            if (objdes != null) {
                succeeded();
            }
            else
                failed("objs = null");
        } catch (Exception e) {
            failed(e,"Failed trying to get the subset list of objects.");       
        }
    }

    /**
     * Method tested:  getObjects(int listOffset, int number)
     * Successful, Returns a subset of the list of objects.
     */
    public void Var175(){
        try{
            ObjectList objList = new ObjectList(systemObject_, ObjectList.ALL, ObjectList.ALL, "*LIB");
            ObjectDescription[] objs = objList.getObjects(-1, -1);
            if(objs != null){
                  succeeded();
            }
            else
                failed("objs = null");
        }
        catch(Exception e){
            failed(e, "Unexpected exception.");
        }

    }

    /**
     * Method tested:  getObjects(int listOffset, int number).
     * Should throw an exception when an invalid listOffset is specified.
     */
    public void Var176(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            ObjectDescription[] objs = objList.getObjects(-2, 1);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     * Method tested:  getObjects(int listOffset, int number).
     * Should succeed when valid listOffset==0 is specified.
     */
    public void Var177(){
	if (checkNotGroupTest()) { 
	    try{
		// ObjectList objList = new ObjectList(systemObject_);
		ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL,ObjectList.ALL,"*LIB");
		ObjectDescription[] objs = objList.getObjects(0,1);
		succeeded(); // Should succeed for offSet==0            @A1A
	    }
	    catch(Exception e){
		failed(e, "Unexpected exception.");                   //@A1A
	    }
	}
    }

    /**
     * Method tested:  getObjects(int listOffset, int number).
     * Should throw an exception when an invalid number is specified.
     */
    public void Var178(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            ObjectDescription[] objs = objList.getObjects(0,-1);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     * Method tested:  getObjects(int listOffset, int number).
     * Should throw an exception when an invalid number is specified.
     */
    public void Var179(){
        try{
            ObjectList objList = new ObjectList(systemObject_);
            ObjectDescription[] objs = objList.getObjects(0,-99);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }
    /**
     * Method tested: getSystem()
     * Successful, Returns the system.
     * 
     */
    public void Var180(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            AS400 system=objList.getSystem();
            if (system != null) {
                succeeded();
            }
        } catch (Exception e) {
            failed(e,"Failed trying to get the system.");       
        }
    }
    /**
     * Method tested: getType()
     * Successful, Returns the object type used to filter this list. (For example: *LIB, *FILE, *OUTQ, etc)
     * 
     */
    public void Var181(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            String type=objList.getType();
            if (type != null && !type.equals("")) {
                succeeded();
            }
        } catch (Exception e) {
            failed(e,"Failed trying to get the type used to filter this list.");        
        }
    }
    /**
     * Method tested: load()
     * Successful,Loads the list of objects on the system.
     * 
     */
    public void Var182(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.load();
            succeeded();
        } catch (Exception e) {
            failed(e,"Failed trying to load the system objects.");      
        }
    }
    /**
     * Method tested: setAspSearchType(String aspSearchType)
     * Specifies the type of the search when a specific
     * auxiliary storage pool device name is specified for the ASP device name. 
     */
    public void Var183(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.setAspSearchType(ObjectList.ASP_SEARCH_TYPE_ASP);       
            if (objList.getAspSearchType().equalsIgnoreCase(ObjectList.ASP_SEARCH_TYPE_ASP)) {
                succeeded();
            } else {
                failed("AspSearchType obtained is not the expected.");
            }

        } catch (Exception e) {
            if (exceptionIs(e, "ExtendedIllegalArgumentException")) {
                failed(e,"An invalid search type is specified.");
            } else {
                failed(e, "Wrong exception info.");
            } 
        }
    }

/**
  * Method tested: setAspSearchType(String aspSearchType)
  * Make sure the exception is thrown is a null value is sent.
  */
    public void Var184(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.setAspSearchType(null);         
            failed("Didn't throw exception."); 


        } catch (Exception e) {
            if (exceptionIs(e, "NullPointerException")) {
                succeeded();
            } else if (exceptionIs(e, "ExtendedIllegalArgumentException")) {
                failed(e,"An invalid search type is specified.");    
            } else {
                failed(e, "Wrong exception info.");         
            }
        }
    }
    /**
    * Method tested: setObjectSelection(boolean select)
    * Sets whether or not the object selection criteria 
    * are used to include objects in the list or 
    * to omit them from the list.
    */
    public void Var185(){   
        try {
                  
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.setObjectSelection(true);
            succeeded();

        } catch (Exception e) {
            failed(e, "Wrong exception info.");        
        }
    }


    /**
     * Method tested:  ObjectDescription::exists().
     * Should not throw an exception for PGM that does not exist (i.e. CPF9811)
     */
    public void Var186(){
        try{
            ObjectDescription objDesc1 = new ObjectDescription(systemObject_, "/QSYS.LIB/QP0FPTOS.PGM");
            ObjectDescription objDesc2 = new ObjectDescription(systemObject_, "/QSYS.LIB/XYZ123.PGM");

            boolean exist1 =  objDesc1.exists();
            boolean exist2 =  objDesc2.exists();
            if ((exist1 != true) ||
                (exist2 != false))
            {
              System.out.println("exist1="+exist1+" should be true");
              System.out.println("exist2="+exist2+" should be false");
              failed("ObjectDescription::exists() failed.");
            }
            else 
              succeeded();
        }
        catch(Exception e){
          System.out.println("Message : "+ e.getMessage());
          failed(e, "Exception should NOT have been thrown.");        
        }
    }

    /**
     * Method tested:  getObjects(int listOffset, int number).
     * Should throw an exception when an invalid listOffset is specified.
     */
    public void Var187(){ // New variation for @A1A
        try{
            ObjectList objList = new ObjectList(systemObject_);
            ObjectDescription[] objs = objList.getObjects(-2,1);
            failed("Didn't throw exception.");
        }
        catch(Exception e){
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     * Method tested: addObjectAuthorityCriteria(String authority)
     * Copy of var152 with authorities added in swapped order.
     * If AUTH_ANY authority is specified, no other values can be specified.
     */
    public void Var188(){   // Added for @A2A
        try {
            
            ObjectList objList=new ObjectList(systemObject_,ObjectList.ALL_USER,ObjectList.ALL_USER,"*LIB");
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_DATA_READ  );
            objList.addObjectAuthorityCriteria(ObjectList.AUTH_ANY );
            failed("Didn't throw exception.");

        } catch (Exception e) {
            succeeded();           
            //failed(e, "Failed, adding AUTH_ANY authority.");
        }
    }

    /**
     * Method tested:  ObjectList::getObjects().          // Added for @A3A
     * Verify that getObjects() no longer throws java.lang.ArrayIndexOutOfBoundsException
     * Test fix for SourceForge.net Bug 1725312
     */
    public void Var189() // Added for @A3A
    {
	ObjectList myOL = new ObjectList(systemObject_, "QSYS",
					 ObjectList.ALL, "*PGM");
	myOL.addObjectAttributeToRetrieve(ObjectDescription.EXTENDED_ATTRIBUTE);
	try
	{
	    //System.out.println("myOL.len="+myOL.getLength());
	    ObjectDescription[] myODs = myOL.getObjects(1, 1);
            succeeded();
	}
	catch (Exception e)
	{
	    // e.printStackTrace();
	    System.out.println("Message : "+ e.getMessage());
	    failed(e, "Exception should NOT have been thrown.");        
	}
    }

}//End of ObjectListTestCase Class
