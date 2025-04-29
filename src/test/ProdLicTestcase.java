///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ProdLicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.ProductLicense;
import com.ibm.as400.access.LicenseException;

/**
 The ProdLicTestcase class tests the following methods of the ProdLicTestcase class:
 <li>constructors,
 <li>get/set methods,
 <li>request(),
 <li>release().
 **/
public class ProdLicTestcase extends Testcase
{
    // the product ID for Web Access is 5722XH1
    private static final String  prodLicTestProductID_ = "5722XH1";
    // the product feature ID for Web Access is 5050 
    private static final String  prodLicTestFeatureID_ = "5050";
    // we don't want to specify a release because we are skip ship
    private static final String  prodLicTestRelease_   = "      ";
    // the product license used for testing
    private static ProductLicense prodLicTestLicense_;
    // the product license used for testing
    private static ProductLicense prodLicTestDftLicense_;

    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        prodLicTestLicense_ = new ProductLicense(systemObject_, 
                                                 prodLicTestProductID_, 
                                                 prodLicTestFeatureID_, 
                                                 prodLicTestRelease_);
        prodLicTestDftLicense_ = new ProductLicense();
    }

    /**
     Default constructor.  Verify that neither the system or command are set.
     **/
    public void Var001()
    {
        try
        {
            ProductLicense licObj = new ProductLicense();
            assertCondition((licObj.getSystem() == null) && 
                            (licObj.getProductID() == null) && 
                            (licObj.getFeature() == null) &&
                            (licObj.getReleaseLevel() == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Standard constructor.  Pass null system and expect an exception.
     **/
    public void Var002()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(null, 
                                                       prodLicTestProductID_, 
                                                       prodLicTestFeatureID_, 
                                                       prodLicTestRelease_);
            failed("No exception."+licObj);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Standard constructor.  Pass null product ID and expect an exception.
     **/
    public void Var003()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       null, 
                                                       prodLicTestFeatureID_, 
                                                       prodLicTestRelease_);
            failed("No exception."+licObj);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Standard constructor.  Pass null feature ID and expect an exception.
     **/
    public void Var004()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       prodLicTestProductID_, 
                                                       null, 
                                                       prodLicTestRelease_);
            failed("No exception."+licObj);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Standard constructor.  Pass null release level and expect an exception.
     **/
    public void Var005()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       prodLicTestProductID_, 
                                                       prodLicTestFeatureID_, 
                                                       null);
            assertCondition(licObj.getReleaseLevel() == "      ");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Standard constructor.  Pass a valid parms.
     **/
    public void Var006()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       prodLicTestProductID_, 
                                                       prodLicTestFeatureID_, 
                                                       prodLicTestRelease_);
            assertCondition((licObj.getSystem().getSystemName() == systemObject_.getSystemName()) && 
                            (licObj.getProductID() == prodLicTestProductID_) && 
                            (licObj.getFeature() == prodLicTestFeatureID_) &&
                            (licObj.getReleaseLevel() == prodLicTestRelease_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check the setSystem().  Pass a null parms.
     **/
    public void Var007()
    {
        try
        {
            prodLicTestDftLicense_.setSystem(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Check the setSystem().  Pass a good parms.
     **/
    public void Var008()
    {
        try
        {
            prodLicTestDftLicense_.setSystem(systemObject_);
            assertCondition(prodLicTestDftLicense_.getSystem().getSystemName() == systemObject_.getSystemName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check the setProductID().  Pass a null parms.
     **/
    public void Var009()
    {
        try
        {
            prodLicTestDftLicense_.setProductID(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Check the setProductID().  Pass a good parms.
     **/
    public void Var010()
    {
        try
        {
            prodLicTestDftLicense_.setProductID(prodLicTestProductID_);
            assertCondition(prodLicTestDftLicense_.getProductID() == prodLicTestProductID_);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check the setFeature().  Pass a null parms.
     **/
    public void Var011()
    {
        try
        {
            prodLicTestDftLicense_.setFeature(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Check the setFeature().  Pass a good parms.
     **/
    public void Var012()
    {
        try
        {
            prodLicTestDftLicense_.setFeature(prodLicTestFeatureID_);
            assertCondition(prodLicTestDftLicense_.getFeature() == prodLicTestFeatureID_);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check the setReleaseLevel().  Pass a null parms.
     **/
    public void Var013()
    {
        try
        {
            prodLicTestDftLicense_.setReleaseLevel(null);
            failed("No exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     Check the setReleaseLevel().  Pass a good parms.
     **/
    public void Var014()
    {
        try
        {
            prodLicTestDftLicense_.setReleaseLevel(prodLicTestRelease_);
            assertCondition(prodLicTestDftLicense_.getReleaseLevel() == prodLicTestRelease_);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check request(). 
     **/
    public void Var015()
    {
        try
        {
            int rc_ = prodLicTestLicense_.request();
            assertCondition((rc_ == ProductLicense.CONDITION_OK) || 
                            (rc_ == ProductLicense.CONDITION_EXCEEDED_OK) || 
                            (rc_ == ProductLicense.CONDITION_EXCEEDED_GRACE_PERIOD) ||
                            (rc_ == ProductLicense.CONDITION_GRACE_PERIOD_EXPIRED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check getCondition().
     **/
    public void Var016()
    {
        try
        {
            int condition_ = prodLicTestLicense_.getCondition();
            assertCondition((condition_ == ProductLicense.CONDITION_OK) || 
                            (condition_ == ProductLicense.CONDITION_EXCEEDED_OK) || 
                            (condition_ == ProductLicense.CONDITION_EXCEEDED_GRACE_PERIOD) ||
                            (condition_ == ProductLicense.CONDITION_GRACE_PERIOD_EXPIRED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }       

    /**
     Check getComplianceType().  
     **/
    public void Var017()
    {
        try
        {
            int complianceType_ = prodLicTestLicense_.getComplianceType();
            assertCondition((complianceType_ == ProductLicense.COMPLIANCE_OPERATOR_ACTION) || 
                            (complianceType_ == ProductLicense.COMPLIANCE_WARNING) || 
                            (complianceType_ == ProductLicense.COMPLIANCE_KEYED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check getLicenseTerm().           
     **/
    public void Var018()
    {
        try
        {
            prodLicTestLicense_.getLicenseTerm();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check getUsageCount().  
     **/
    public void Var019()
    {
        try
        {
            int usageCount_ = prodLicTestLicense_.getUsageCount();
            assertCondition(usageCount_ > 0);

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Check getUsageLimit(). 
     **/
    public void Var020()
    {
        try
        {
            int usageLimit_ = prodLicTestLicense_.getUsageLimit();
            assertCondition(usageLimit_ >= -1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    } 

    /**
     Check getUsageType(). 
     **/
    public void Var021()
    {
        try
        {
            int usageType_ = prodLicTestLicense_.getUsageType();
            assertCondition((usageType_ == ProductLicense.USAGE_CONCURRENT) || 
                            (usageType_ == ProductLicense.USAGE_REGISTERED));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    } 

    /**
     Check request(). Fail because of invalid product ID and expect an exception.
     **/
    public void Var022()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       "myProduct", 
                                                       prodLicTestFeatureID_, 
                                                       prodLicTestRelease_);
            licObj.request();
            failed("No exception.");
        }
        catch (LicenseException le)
        {
            // System.out.println("return code: " + le.getReturnCode());
            assertExceptionIsInstanceOf(le, "com.ibm.as400.access.LicenseException");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.LicenseException");
        }
    }

    /**
     Check request(). Fail because of non-existing product ID and expect an exception.
     **/
    public void Var023()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       "1111XXX",
                                                       prodLicTestFeatureID_, 
                                                       prodLicTestRelease_);
            licObj.request();
            failed("No exception.");
        }
        catch (LicenseException le)
        {
            // System.out.println("return code: " + le.getReturnCode());
            assertExceptionIsInstanceOf(le, "com.ibm.as400.access.LicenseException");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.LicenseException");
        }
    }

    /**
     Check request(). Fail because of non-existing release level and expect an exception.
     **/

    public void Var024()
    {
        try
        {
            ProductLicense licObj = new ProductLicense(systemObject_, 
                                                       prodLicTestProductID_, 
                                                       prodLicTestFeatureID_, 
                                                       "123456");
            licObj.request();
            failed("No exception.");
        }
        catch (LicenseException le)
        {
            assertExceptionIsInstanceOf(le, "com.ibm.as400.access.LicenseException");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.LicenseException");
        }
    }
}
