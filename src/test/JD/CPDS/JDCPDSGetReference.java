///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSGetReference.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCPDSGetReference.java
//
// Classes:      JDCPDSGetReference
//
////////////////////////////////////////////////////////////////////////

package test.JD.CPDS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestcase;

import java.io.FileOutputStream;
import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

import java.util.Hashtable;
import java.util.Vector;


/**
Testcase JDCPDSGetReference.  This tests the following methods
of the JDBC Driver class:

<ul>
<li>getReference() 
<li>getObjectInstance() 
</ul>
**/

public class JDCPDSGetReference
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSGetReference";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }



    // Private data.
    private Object dataSource_;
    private ObjectFactory dataSourceFactory_;


/**
Constructor.
**/
    public JDCPDSGetReference (AS400 systemObject,
                               Hashtable<String,Vector<String>> namesAndVars,
                               int runMode,
                               FileOutputStream fileOutputStream,
                               
                               String password)
    {
        super (systemObject, "JDCPDSGetReference",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        if (isJdbc20StdExt()) {
            dataSource_ =  JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource");
            dataSourceFactory_ = (ObjectFactory) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2DataSourceFactory");
        }
    }


    // TO Test:
    // getReference()
    // This bucket will be pretty smalle - no more than a handful of tests
    // that I can think of.

/**
getReference() - Make sure there is a getReference method which creates a reference.
**/
    public void Var001 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Reference refds =  (Reference) JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                assertCondition(true, "ref is "+refds);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }


/**
getReference() - Check that the correct class name is in the Reference.
**/
    public void Var002 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Reference refds = (Reference) JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                String className = refds.getClassName();
                assertCondition(className.equals("com.ibm.db2.jdbc.app.DB2ConnectionPoolDataSource"));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }


/**
getReference() - Check that the correct factory class name is in the Reference.
**/
    public void Var003 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Reference refds =  (Reference)JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                String factclassName = refds.getFactoryClassName();
                assertCondition(factclassName.equals("com.ibm.db2.jdbc.app.DB2DataSourceFactory"));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }


/**
getReference() - Check that the factory class location is null in the Reference.
**/
    public void Var004 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Reference refds = (Reference) JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                assertCondition(null == refds.getFactoryClassLocation());
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }


/**
getReference(), getObjectInstance - create a reference and then call getObjectInstance
to get the object. This object should be the same as the original.
The dataSource object does not have any properties set.
**/
    public void Var005 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                Reference refds = (Reference) JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                Object ds1 =  dataSourceFactory_.getObjectInstance(refds,null,null,null); 
                //assertCondition(dataSource_.equals(ds1));
                assertCondition(dataSource_.toString().equals(ds1.toString()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }

/**
getReference(), getObjectInstance - create a reference and then call getObjectInstance
to get the object. This object should have the same properties as the original.
**/
    public void Var006 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",system_);
                JDReflectionUtil.callMethod_V(dataSource_,"setDataSourceName","MyDataSource");
                Reference refds =  (Reference) JDReflectionUtil.callMethod_O(dataSource_,"getReference");
                Object ds1 =  dataSourceFactory_.getObjectInstance(refds,null,null,null); 
                assertCondition((JDReflectionUtil.callMethod_S(ds1, "getDatabaseName").equals(system_)) &&
                       (JDReflectionUtil.callMethod_S(ds1, "getDataSourceName").equals("MyDataSource")));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }
}




