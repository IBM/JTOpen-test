///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RSoftwareResourceBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.resource.RSoftwareResource;

import java.io.FileOutputStream;
import java.util.Hashtable;



/**
Testcase RSoftwareResourceBasicTestcase.  This tests the following methods
of the RSoftwareResource class:

<ul>
<li>constructors
<li>serialization
<li>getLoadID()
<li>getProductID()
<li>getProductOption()
<li>getReleaseLevel()
<li>getSystem()
<li>setLoadID()
<li>setProductID()
<li>setProductOption()
<li>setReleaseLevel()
<li>setSystem()
</ul>
**/
public class RSoftwareResourceBasicTestcase
extends Testcase {



    // Constants.



    // Private data.
    private String          JC1ProductID_ = RSoftwareTest.JC1ProductID_;
    private String          releaseLevel_;
    AS400           pwrSys_;



/**
Constructor.
**/
    public RSoftwareResourceBasicTestcase(AS400 systemObject,
                              Hashtable namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password,
                              AS400 pwrSys)
    {
        super (systemObject, "RSoftwareResourceBasicTestcase",
               namesAndVars, runMode, fileOutputStream,
               password);
        pwrSys_ = pwrSys;

        if (pwrSys == null)
            throw new IllegalStateException("ERROR: Please specify a power system.");
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
      JC1ProductID_ = RSoftwareTest.JC1ProductID_;
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
    }



    private String makeReleaseLevel()
    {
        if (releaseLevel_ == null) {
            try {
                StringBuffer buffer = new StringBuffer("V");
                buffer.append(systemObject_.getVersion());
                buffer.append("R");
                buffer.append(systemObject_.getRelease());
                buffer.append("M");
                buffer.append(systemObject_.getModification());
                releaseLevel_ = buffer.toString();
            }
            catch(Exception e) {
                e.printStackTrace();
                releaseLevel_ = "ERROR";
            }
        }
        return releaseLevel_;
    }


/**
constructor() with 0 parms - Should work.
**/
    public void Var001()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition ((u.getSystem() == null) 
                    && (u.getLoadID().equals(RSoftwareResource.LOAD_ID_CODE))
                    && (u.getProductID().equals(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM))
                    && (u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (u.getReleaseLevel().equals(RSoftwareResource.RELEASE_LEVEL_CURRENT)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass null for system.
**/
    public void Var002()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(null, JC1ProductID_);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Pass null for product ID.
**/
    public void Var003()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, null);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 2 parms - Pass invalid values.  This should work,
because the constructor does not check the validity.
**/
    public void Var004()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RSoftwareResource u = new RSoftwareResource(bogus, "BadRSoftwareResource");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getProductID().equals("BadRSoftwareResource")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 2 parms - Pass valid values.  Verify that the values are used.
**/
    public void Var005()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getProductID().equals(JC1ProductID_))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 4 parms - Pass null for system.
**/
    public void Var006()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(null, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 4 parms - Pass null for product ID.
**/
    public void Var007()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, null, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 4 parms - Pass null for release level.
**/
    public void Var008()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, null, RSoftwareResource.PRODUCT_OPTION_BASE);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 4 parms - Pass null for product option.
**/
    public void Var009()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), null);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }




/**
constructor() with 4 parms - Pass invalid values.  This should work,
because the constructor does not check the validity.
**/
    public void Var010()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RSoftwareResource u = new RSoftwareResource(bogus, "BadRSoftwareResource", "BadAgain", "BadOnceMore");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getProductID().equals("BadRSoftwareResource"))
                    && (u.getReleaseLevel().equals("BadAgain"))
                    && (u.getProductOption().equals("BadOnceMore")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 4 parms - Pass valid values.  Verify that the values are used.
**/
    public void Var011()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getProductID().equals(JC1ProductID_))
                    && (u.getReleaseLevel().equals(makeReleaseLevel()))
                    && (u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 5 parms - Pass null for system.
**/
    public void Var012()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(null, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 5 parms - Pass null for product ID.
**/
    public void Var013()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, null, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 5 parms - Pass null for release level.
**/
    public void Var014()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, null, RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
constructor() with 5 parms - Pass null for product option.
**/
    public void Var015()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), null, "5050");
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }




/**
constructor() with 5 parms - Pass null for load ID.
**/
    public void Var016()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, null);
            failed ("Didn't throw exception"+u);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }




/**
constructor() with 5 parms - Pass invalid values.  This should work,
because the constructor does not check the validity.
**/
    public void Var017()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RSoftwareResource u = new RSoftwareResource(bogus, "BadRSoftwareResource", "BadAgain", "BadOnceMore", "BadLoad");
            assertCondition ((u.getSystem() == bogus) 
                    && (u.getProductID().equals("BadRSoftwareResource"))
                    && (u.getReleaseLevel().equals("BadAgain"))
                    && (u.getProductOption().equals("BadOnceMore"))
                    && (u.getLoadID().equals("BadLoad")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
constructor() with 5 parms - Pass valid values.  Verify that the values are used.
**/
    public void Var018()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getSystem() == systemObject_) 
                    && (u.getProductID().equals(JC1ProductID_))
                    && (u.getReleaseLevel().equals(makeReleaseLevel()))
                    && (u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (u.getLoadID().equals("5050"))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
serialization - Verify that the properties are set and verify that its usable.
This variation is tagged as "attended", since a login dialog to pops up during deserialization.
**/
    public void Var019(int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) {
        notApplicable("Attended variation");
        return;
      }
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            RSoftwareResource u2 = (RSoftwareResource)RSoftwareTest.serialize(u);
            String loadType2 = (String)u2.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u2.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u2.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (u2.getProductID().equals(JC1ProductID_))
                    && (u2.getReleaseLevel().equals(makeReleaseLevel()))
                    && (u2.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (u2.getLoadID().equals("5050"))
                    && (loadType2.equals(RSoftwareResource.LOAD_TYPE_CODE)), "loadtype="+loadType);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLoadID() - Should return null when the name has not been set.
**/
    public void Var020()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition (u.getLoadID().equals(RSoftwareResource.LOAD_ID_CODE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getLoadID() - Should return the name when the name has been set.
**/
    public void Var021()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            assertCondition (u.getLoadID().equals("5050"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProductID() - Should return the OS when the name has not been set.
**/
    public void Var022()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition (u.getProductID().equals(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProductID() - Should return the name when the name has been set.
**/
    public void Var023()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            assertCondition (u.getProductID().equals(JC1ProductID_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProductOption() - Should return null when the name has not been set.
**/
    public void Var024()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition (u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getProductOption() - Should return the name when the name has been set.
**/
    public void Var025()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            assertCondition (u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getReleaseLevel() - Should return null when the name has not been set.
**/
    public void Var026()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition (u.getReleaseLevel().equals(RSoftwareResource.RELEASE_LEVEL_CURRENT));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getReleaseLevel() - Should return the name when the name has been set.
**/
    public void Var027()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_, makeReleaseLevel(), RSoftwareResource.PRODUCT_OPTION_BASE, "5050");
            assertCondition (u.getReleaseLevel().equals(makeReleaseLevel()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return null when the system has not been set.
**/
    public void Var028()
    {
        try {
            RSoftwareResource u = new RSoftwareResource();
            assertCondition (u.getSystem() == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getSystem() - Should return the system when the system has been set.
**/
    public void Var029()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "newhampshire");
            assertCondition (u.getSystem().equals(systemObject_));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLoadID() - Should throw an exception if null is passed.
**/
    public void Var030()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setLoadID() - Set to an invalid name.  Should be reflected by getLoadID(),
since the validity is not checked here.
**/
    public void Var031()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID("rhodeisland");
            assertCondition (u.getLoadID().equals("rhodeisland"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLoadID() - Set to a valid name.  Should be reflected by getLoadID() and
verify that it is used.
**/
    public void Var032()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setLoadID("2924");
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getLoadID().equals("2924"))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_LANGUAGE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setLoadID() - Set to a valid name after the RSoftwareResource has made a connection.
**/
    public void Var033()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.setLoadID("2924");
            failed ("Didn't throw exception"+loadType);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setLoadID() - Should fire a property change event.
**/
    public void Var034()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "massachusetts");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setLoadID("2924");
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("loadID"))
                    && (pcl.event_.getOldValue().equals(RSoftwareResource.LOAD_ID_CODE))
                    && (pcl.event_.getNewValue().equals("2924")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
setProductID() - Should throw an exception if null is passed.
**/
    public void Var035()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductID(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setProductID() - Set to an invalid name.  Should be reflected by getProductID(),
since the validity is not checked here.
**/
    public void Var036()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductID("rhodeisland");
            assertCondition (u.getProductID().equals("rhodeisland"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setProductID() - Set to a valid name.  Should be reflected by getProductID() and
verify that it is used.
**/
    public void Var037()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductID(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getProductID().equals(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setProductID() - Set to a valid name after the RSoftwareResource has made a connection.
**/
    public void Var038()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.setProductID(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            failed ("Didn't throw exception"+loadType);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setProductID() - Should fire a property change event.
**/
    public void Var039()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "massachusetts");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setProductID(JC1ProductID_);
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("productID"))
                    && (pcl.event_.getOldValue().equals("massachusetts"))
                    && (pcl.event_.getNewValue().equals(JC1ProductID_)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setProductOption() - Should throw an exception if null is passed.
**/
    public void Var040()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductOption(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setProductOption() - Set to an invalid name.  Should be reflected by getProductOption(),
since the validity is not checked here.
**/
    public void Var041()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductOption("rhodeisland");
            assertCondition (u.getProductOption().equals("rhodeisland"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setProductOption() - Set to a valid name.  Should be reflected by getProductOption() and
verify that it is used.
**/
    public void Var042()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setProductOption(RSoftwareResource.PRODUCT_OPTION_BASE);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getProductOption().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setProductOption() - Set to a valid name after the RSoftwareResource has made a connection.
**/
    public void Var043()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.setProductOption(RSoftwareResource.PRODUCT_ID_OPERATING_SYSTEM);
            failed ("Didn't throw exception"+loadType);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setProductOption() - Should fire a property change event.
**/
    public void Var044()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "massachusetts");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setProductOption("1");
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("productOption"))
                    && (pcl.event_.getOldValue().equals(RSoftwareResource.PRODUCT_OPTION_BASE))
                    && (pcl.event_.getNewValue().equals("1")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setReleaseLevel() - Should throw an exception if null is passed.
**/
    public void Var045()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setReleaseLevel(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setReleaseLevel() - Set to an invalid name.  Should be reflected by getReleaseLevel(),
since the validity is not checked here.
**/
    public void Var046()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setReleaseLevel("rhodeisland");
            assertCondition (u.getReleaseLevel().equals("rhodeisland"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setReleaseLevel() - Set to a valid name.  Should be reflected by getProductID() and
verify that it is used.
**/
    public void Var047()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setReleaseLevel(RSoftwareResource.RELEASE_LEVEL_CURRENT);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getReleaseLevel().equals(RSoftwareResource.RELEASE_LEVEL_CURRENT))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setReleaseLevel() - Set to a valid name after the RSoftwareResource has made a connection.
**/
    public void Var048()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.setReleaseLevel(RSoftwareResource.RELEASE_LEVEL_CURRENT);
            failed ("Didn't throw exception"+loadType);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setReleaseLevel() - Should fire a property change event.
**/
    public void Var049()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, "massachusetts");
            RSoftwareTest.PropertyChangeListener_ pcl = new RSoftwareTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setReleaseLevel(makeReleaseLevel());
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("releaseLevel"))
                    && (pcl.event_.getOldValue().equals(RSoftwareResource.RELEASE_LEVEL_CURRENT))
                    && (pcl.event_.getNewValue().equals(makeReleaseLevel())));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Should throw an exception if null is passed.
**/
    public void Var050()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setSystem(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
setSystem() - Set to an invalid system.  Should be reflected by getSystem(),
since the validity is not checked here.
**/
    public void Var051()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            u.setSystem(bogus);
            assertCondition ((u.getSystem().getSystemName().equals("bogus"))
                    && (u.getSystem().getUserId().equalsIgnoreCase("bogus")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name.  Should be reflected by getSystem() and
verify that it is used.
**/
    public void Var052()
    {
        try {
            AS400 bogus = new AS400("bogus", "bogus", "bogus");
            RSoftwareResource u = new RSoftwareResource(bogus, JC1ProductID_);
            u.setSystem(systemObject_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            assertCondition ((u.getSystem().getSystemName().equals(systemObject_.getSystemName()))
                    && (u.getSystem().getUserId().equals(systemObject_.getUserId()))
                    && (loadType.equals(RSoftwareResource.LOAD_TYPE_CODE)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
setSystem() - Set to a valid name after the RSoftwareResource object has made a connection.
**/
    public void Var053()
    {
        try {
            RSoftwareResource u = new RSoftwareResource(systemObject_, JC1ProductID_);
            String loadType = (String)u.getAttributeValue(RSoftwareResource.LOAD_TYPE);
            u.setSystem(systemObject_);
            failed ("Didn't throw exception"+loadType);
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalStateException");
        }
    }



/**
setSystem() - Should fire a property change event.
**/
    public void Var054()
    {
        try {
            AS400 temp1 = new AS400("temp1", "temp1", "temp1");
            AS400 temp2 = new AS400("temp2", "temp2", "temp2");
            RSoftwareResource u = new RSoftwareResource(temp1, JC1ProductID_);
            UserTest.PropertyChangeListener_ pcl = new UserTest.PropertyChangeListener_();
            u.addPropertyChangeListener(pcl);
            u.setSystem(temp2);
            assertCondition ((pcl.eventCount_ == 1)
                    && (pcl.event_.getPropertyName().equals("system"))
                    && (pcl.event_.getOldValue().equals(temp1))
                    && (pcl.event_.getNewValue().equals(temp2)));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


    // Dummy
    public void Var055() {
	assertCondition(true); 
    }


}




