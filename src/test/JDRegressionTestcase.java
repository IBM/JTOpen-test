///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRegressionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.FileOutputStream;

import java.util.Hashtable;
import java.util.Vector;
import com.ibm.as400.access.*;


public class JDRegressionTestcase extends Testcase
{

    String userid_;
    String password_;
    String pwrUID_ = null;     
    String pwrPwd_ = null;    

/**
Constructor.
**/
    public JDRegressionTestcase(AS400 systemObject,
                                Hashtable<String, Vector<String>> namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password,
                                String userid,
                                String pwrUID,  //@A1A
                                String pwrPwd)  //@A1A
    {
        super (systemObject, "JDRegressionTestcase",
               namesAndVars.get ("JDRegressionTestcase"),
               runMode, fileOutputStream,
               password);

        userid_   = userid;
        password_ = password;
        pwrUID_ = pwrUID;   //@A1A
        pwrPwd_ = pwrPwd;   //@A1A
    }


  public void Var001()
  {
    try
    {
        // First, cleanup any test tables and procedures, leftover from prior runs.
        // We use pwrUID_ to make sure that we have sufficient authority.
        String pwrURL = "jdbc:as400://"
                     + systemObject_.getSystemName()
                     + ";user="
                     + pwrUID_
                     + ";password="
                     + pwrPwd_
                     + ";errors=full;naming=sql;"  ;

        JDBVTTest.setUseNativeDriver(false);
        JDBVTTest.setURL(pwrURL);
        JDBVTTest.setShowException(true);

        if (! JDBVTTest.setup())
        {
            failed("setup failed");
            return;
        }

        if (! JDBVTTest.cleanupTestTable())
        {
            failed("setup failed");
            return;
        }

        // Proceed with the test, using the "regular" uid/pwd.
        String URL = "jdbc:as400://"
                     + systemObject_.getSystemName()
                     + ";user="
                     + userid_
                     + ";password="
                     + password_
                     + ";errors=full;naming=sql;"  ;

        JDBVTTest.setUseNativeDriver(false);
        JDBVTTest.setURL(URL);
        JDBVTTest.setShowException(true);

// We already did this.
//        if (! JDBVTTest.setup())
//        {
//            failed("setup failed");
//            return;
//        }

        if (! JDBVTTest.testConnection())
        {
            failed("connection test failed");
            return;
        }

        boolean failed = false;

        if (! JDBVTTest.testStatement())
           failed = true;

        if (! JDBVTTest.testPreparedStatement())
           failed = true;

        if (! JDBVTTest.testCallableStatement())
           failed = true;

        if (! JDBVTTest.testResultSet())
           failed = true;

        if (! JDBVTTest.testResultSetMetaData())
           failed = true;

        if (! JDBVTTest.testDatabaseMetaData())
           failed = true;

        if (failed)
           failed();
        else
           succeeded();
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

}
