///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCPDSProperties.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////



package test.JD.CPDS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import javax.naming.*;
import java.util.Hashtable;
import java.util.Vector;
import java.io.PrintWriter;



/**
Testcase JDCPDSProperties.  This tests the following methods
of the JDBC ConnectionPoolDataSource class:

<ul>
<li>setDataSourceName()
<li>getDataSourceName()
<li>setDatabaseName()
<li>getDatabaseName()
<li>setDescription()
<li>getDescription()
<li>getLogWriter()
<li>setLogWriter()
<li>getLoginTimeout()
<li>setLoginTimeout()
<li>getAccess()
<li>setAccess()
<li>getDateFormat()
<li>setDateFormat()
<li>getDateSeparator()
<li>setDateSeparator()
<li>getDecimalSeparator()
<li>setDecimalSeparator()
<li>getLibraries()
<li>setLibraries()
<li>getNamingOption()
<li>setNamingOption()
<li>getPassword()
<li>setPassword()
<li>getTimeFormat()
<li>setTimeFormat()
<li>getTimeSeparator()
<li>setTimeSeparator()
<li>getTraceActive()
<li>setTraceActive()
<li>getTransactionIsolationLevel()
<li>setTransactionIsolationLevel()
<li>getTranslateBinary()
<li>setTranslateBinary()
<li>getUser()
<li>setUser()
<li>getUseBlocking()
<li>setUseBlocking()
<li>getBlockSize()
<li>setBlockSize()

</ul>
**/
public class JDCPDSProperties
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCPDSProperties";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCPDSTest.main(newArgs); 
   }


    // Private data.
    private Object dataSource_;
    private Context ctx_;
    private Hashtable<String, String> env;
    String clearPassword_; 
    protected static final String bindName_ = "JDCPDSPropertiesTest";

/**
Constructor.
**/
    public JDCPDSProperties (AS400 systemObject,
                             Hashtable<String,Vector<String>> namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDCPDSProperties",
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
            env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, 
                    "com.sun.jndi.fscontext.RefFSContextFactory");
            env.put(Context.PROVIDER_URL, "file:/tmp");

            JDReflectionUtil.callMethod_V(dataSource_,"setDataSourceName","local connection");
            JDReflectionUtil.callMethod_V(dataSource_,"setDescription","This is something cool");

	    clearPassword_ = PasswordVault.decryptPasswordLeak(encryptedPassword_);

            ctx_ = new InitialContext(env);
        }

    }

    // TO Test:
    // All the items we can get and set.
    // test them with defaults, with valid values, and with invalid values.
    // This will be the largest group of tests we have for data sources.

/* The proto for variations
   - set the property 
   - bind the context to the data source
   - get the property
   - unbind the context from the data source
*/


/**
getDataSourceName() - call getDataSourceName to check that the value set before is returned.
**/
    public void Var001 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDataSourceName").equals("local connection"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDataSourceName(),getDataSourceName() - set the dataSourceName property
and then verify it was set.
**/
    public void Var002 ()
    {

        if (checkJdbc20StdExt()) {
            try {
                String dsn = "MyDataSource";
                JDReflectionUtil.callMethod_V(dataSource_,"setDataSourceName",dsn);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDataSourceName").equals(dsn));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getDatabaseName() - call getDatabaseName to check that the value set before is returned.
**/
    public void Var003 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDatabaseName").equals("*LOCAL"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDatabaseName(),getDatabaseName() - set the databaseName property
and then verify it was set.
**/
    public void Var004 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                String dbn = system_;
                JDReflectionUtil.callMethod_V(dataSource_,"setDatabaseName",dbn);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDatabaseName").equals(dbn));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getDescription() - call getDescription to check that the value set before is returned.
**/
    public void Var005 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDescription").equals("This is something cool"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
              
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
setDescription(),getDescription() - set the Description property
and then verify it was set.
**/
    public void Var006 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                String desc = "Test data source for JDCPDSProperties test";
                JDReflectionUtil.callMethod_V(dataSource_,"setDescription",desc);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDescription").equals(desc));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getLogWriter() - call getLogWriter to check that the default value is returned.
**/
    public void Var007 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(null == JDReflectionUtil.callMethod_S(ds, "getLogWriter"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setLogWriter(),getLogWriter() - set the LogWriter property
and then verify it was set.
**/
    public void Var008 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                PrintWriter pw = new PrintWriter(System.out);
                JDReflectionUtil.callMethod_V(dataSource_,"setLogWriter",pw);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                //assertCondition(JDReflectionUtil.callMethod_S(ds, "getLogWriter").equals(pw));
                assertCondition(null == JDReflectionUtil.callMethod_S(ds, "getLogWriter"));  // may be a bug
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getLoginTimeout() - call getLoginTimeout to check that the default value is returned.
**/
    public void Var009 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(0 == JDReflectionUtil.callMethod_I(ds, "getLoginTimeout"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setLoginTimeout(),getLoginTimeout() - set the LoginTimeout property
and then verify it was set.
**/
    public void Var010 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setLoginTimeout",10);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(10 == JDReflectionUtil.callMethod_I(ds, "getLoginTimeout"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getAccess() - call getAccess to check that the default value is returned.
**/
    public void Var011 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getAccess").equals("all"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setAccess(),getAccess() - set the Access property
and then verify it was set.
**/
    public void Var012 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setAccess","read only");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getAccess").equals("read only"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setAccess(),getAccess() - set an invalid Access property. Should throw exception.
**/
    public void Var013 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                String invacc = "invalidaccessproperty";
                JDReflectionUtil.callMethod_V(dataSource_,"setAccess",invacc);
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getDateFormat() - call getDateFormat to check that the default value is returned.
**/
    public void Var014 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDateFormat").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDateFormat(),getDateFormat() - set the DateFormat property
and then verify it was set.
**/
    public void Var015 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDateFormat","usa");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDateFormat").equals("usa"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDateFormat(),getDateFormat() - set an invalid DateFormat property. Should throw exception.
**/
    public void Var016 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDateFormat","invaliddateproperty");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
getDateSeparator() - call getDateSeparator to check that the default value is returned.
**/
    public void Var017 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDateSeparator").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDateSeparator(),getDateSeparator() - set the DateSeparator property
and then verify it was set.
**/
    public void Var018 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDateSeparator","/");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDateSeparator").equals("/"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDateSeparator(),getDateSeparator() - set an invalid DateSeparator property. Should throw exception.
**/
    public void Var019 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDateSeparator","invaliddateproperty");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getDecimalSeparator() - call getDecimalSeparator to check that the default value is returned.
**/
    public void Var020 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDecimalSeparator").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setDecimalSeparator(),getDecimalSeparator() - set the DecimalSeparator property
and then verify it was set.
**/
    public void Var021 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDecimalSeparator",".");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getDecimalSeparator").equals("."));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setDecimalSeparator(),getDecimalSeparator() - set an invalid DecimalSeparator property. Should throw exception.
**/
    public void Var022 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setDecimalSeparator","invalidDecimalproperty");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getUser() - call getUSer to check that it is unset.
**/
    public void Var023 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getUser").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setUser(),getUser() - set the User property
and then verify it was set.
**/
    public void Var024 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setUser","JoeUser");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getUser").equals("JoeUser"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getPassword() - call getPassword to check that it is unset.
**/
    public void Var025 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                String password = JDReflectionUtil.callMethod_S(ds, "getPassword");
                assertCondition((password == null) || (password.equals("")), "Password is "+password+" instead of null or empty");
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setPassword(),getPassword() - set the Password property
and then verify it was set.
**/
    public void Var026 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setPassword","JoePassword");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getPassword").equals("JoePassword"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getTranslateBinary() - call getTranslateBinary to check the default.
**/
    public void Var027 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(false == JDReflectionUtil.callMethod_B(ds, "getTranslateBinary"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTranslateBinary(),getTranslateBinary() - set the TranslateBinary property
and then verify it was set.
**/
    public void Var028 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTranslateBinary",true);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_B(ds, "getTranslateBinary"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getTimeFormat() - call getTimeFormat to check that the default value is returned.
**/
    public void Var029 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTimeFormat").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTimeFormat(),getTimeFormat() - set the TimeFormat property
and then verify it was set.
**/
    public void Var030 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTimeFormat","usa");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTimeFormat").equals("usa"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimeFormat(),getTimeFormat() - set an invalid TimeFormat property. Should throw exception.
**/
    public void Var031 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTimeFormat","invalidTimeproperty");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/**
getTimeSeparator() - call getTimeSeparator to check that the default value is returned.
**/
    public void Var032 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTimeSeparator").equals(""));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTimeSeparator(),getTimeSeparator() - set the TimeSeparator property
and then verify it was set.
**/
    public void Var033 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTimeSeparator",":");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTimeSeparator").equals(":"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTimeSeparator(),getTimeSeparator() - set an invalid TimeSeparator property. Should throw exception.
**/
    public void Var034 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTimeSeparator","invalidTimeproperty");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getNamingOption() - call getNamingOption to check that it is default.
**/
    public void Var035 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getNamingOption").equals("sql"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setNamingOption(),getNamingOption() - set the naming property
and then verify it was set.
**/
    public void Var036 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setNamingOption","system");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getNamingOption").equals("system"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setNamingOption() - set the Naming property to an invalid value.
Exception should be thrown
**/
    public void Var037 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setNamingOption","invalidNamingOption");
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

/* for the Libraries property (which actually specifies the default lib) 
variations, these depend on the value of the "naming" property.
In the default case where "naming" is "sql", the default library is
the same name as the user profile. If "naming" is set to "system",
the default lib will be the top lib in the users library list.
*/

/**
getLibraries() - call getLibraries to check that the default value is returned.
**/
    public void Var038 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource_,"setPassword",clearPassword_);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getLibraries").equals(""));
                //assertCondition(JDReflectionUtil.callMethod_S(ds, "getLibraries").equals(userId_));   may be a bug
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getLibraries() - set the "naming" to "system", call getLibraries
to check that the correct (default) value is returned.
**/
    public void Var039 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setUser",userId_);
                JDReflectionUtil.callMethod_V(dataSource_,"setPassword",clearPassword_);
                JDReflectionUtil.callMethod_V(dataSource_,"setNamingOption","system");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getLibraries").equals(""));
                System.out.println(JDReflectionUtil.callMethod_S(ds, "getLibraries"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setLibraries(),getLibraries() - set the Libraries property
and then verify it was set.
**/
    public void Var040 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setLibraries","QUSRSYS");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getLibraries").equals("QUSRSYS"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getTraceActive() - call getTraceActive to check the default.
**/
    // v2kea447 - getTraceActive() was changed to getTrace()
    public void Var041 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(false == JDReflectionUtil.callMethod_B(ds, "getTrace"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTraceActive(),getTraceActive() - set the TraceActive property
and then verify it was set.
**/
    // v2kea447 - setTraceActive() and getTraceActive() changed to
    // setTrace() and getTrace()
    public void Var042 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTrace",true);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_B(ds, "getTrace"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
getTransactionIsolationLevel() - call getTransactionIsolationLevel
to check that the default value is returned.
**/
    public void Var043 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTransactionIsolationLevel").equals("none"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setTransactionIsolationLevel(),getTransactionIsolationLevel() - set the
TransactionIsolationLevel property and then verify it was set.
**/
    public void Var044 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setTransactionIsolationLevel","read committed");
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_S(ds, "getTransactionIsolationLevel").equals("read committed"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }

/**
setTransactionIsolationLevel(),getTransactionIsolationLevel() - set an invalid TransactionIsolationLevel property. Should throw exception.
**/
    public void Var045 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                String invtr = "invalidTransactionIsolationLevelproperty";
                JDReflectionUtil.callMethod_V(dataSource_,"setTransactionIsolationLevel",invtr);
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getUseBlocking() - call getUseBlocking to check the default.
**/
    public void Var046 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_B(ds, "getUseBlocking"));
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setUseBlocking(),getUseBlocking() - set the UseBlocking property
and then verify it was set.
**/
    public void Var047 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setUseBlocking",false);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_B(ds, "getUseBlocking") == false);
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getBlockSize() - call getBlockSize to check the default.
**/
    public void Var048 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_I(ds, "getBlockSize") == 32);
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setBlockSize(),getBlockSize() - set the BlockSize property
and then verify it was set.
**/
    public void Var049 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setBlockSize",16);
                ctx_.rebind(bindName_, dataSource_);
                Object ds = ctx_.lookup(bindName_);
                assertCondition(JDReflectionUtil.callMethod_I(ds, "getBlockSize") == 16);
                ctx_.unbind(bindName_);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
setBlockSize(),getBlockSize() - set the BlockSize property to
an invalid value. Exception should be thrown.
**/
    public void Var050 ()
    {
        if (checkJdbc20StdExt()) {
            try {
                JDReflectionUtil.callMethod_V(dataSource_,"setBlockSize",10);
                failed("Did not throw exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


    /**
    getDirectMap() - call getDirectMap
    **/
        public void Var051 ()
        {
            if (checkJdbc20StdExt()) {
              if ((getDriver()  == JDTestDriver.DRIVER_NATIVE)) { 
                try {
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition(!JDReflectionUtil.callMethod_B(ds, "getDirectMap"), "getDirectMap default not false added 12/30/2005 by native" );
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 12/30/2005");
                }
            } else {
              notApplicable("native V5R4 or later variation"); 
            }
            }
        }


    /**
    setDirectMap(),getDirectMap() - set the DirectMap property
    and then verify it was set.
    **/
        public void Var052 ()
        {
            if (checkJdbc20StdExt()) {
              if ((getDriver()  == JDTestDriver.DRIVER_NATIVE)) { 

                try {
                    JDReflectionUtil.callMethod_V(dataSource_, "setDirectMap", true); 
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition(JDReflectionUtil.callMethod_B(ds, "getDirectMap"), "getDirectMap returned false -- added by native 12/30/2005" ); 
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 12/30/2005");
                }
              } else {
                notApplicable("native V5R4 or later variation"); 
              }
            }
        }

    
    
    
    /**
    getQueryOptimizeGoal() - call getQueryOptimizeGoal
    **/
        public void Var053 ()
        {
            if (checkJdbc20StdExt()) {
              if ((getDriver()  == JDTestDriver.DRIVER_NATIVE)) { 
                try {
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition("2".equals(JDReflectionUtil.callMethod_S(ds, "getQueryOptimizeGoal")), "getQueryOptimizeGoal default not false added 12/30/2005 by native" );
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 12/30/2005");
                }
            } else {
              notApplicable("native V5R4 or later variation"); 
            }
            }
        }


    /**
    setQueryOptimizeGoal(),getQueryOptimizeGoal() - set the QueryOptimizeGoal property
    and then verify it was set.
    **/
        public void Var054 ()
        {
            if (checkJdbc20StdExt()) {
              if ((getDriver()  == JDTestDriver.DRIVER_NATIVE)) { 

                try {
		    JDReflectionUtil.callMethod_V(dataSource_, "setQueryOptimizeGoal", "1"); 
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition("1".equals(JDReflectionUtil.callMethod_S(ds, "getQueryOptimizeGoal")), "getQueryOptimizeGoal returned false -- added by native 12/30/2005" ); 
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 12/30/2005");
                }
              } else {
                notApplicable("native V5R4 or later variation"); 
              }
            }

        }

    
    
    /**
    getDecfloatRoundingMode() - call getDecfloatRoundingMode
    **/
        public void Var055 ()
        {
            if (checkDecFloatSupport()) {
                try {
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition("round half even".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode default not round half event added 11/21/2006 by native" );
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 11/21/2006");
                }
            }
        }


    /**
    setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
    and then verify it was set.
    **/
        public void Var056 ()
        {
            if (checkDecFloatSupport()) {

                try {
		    JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round half even"); 
                    ctx_.rebind(bindName_, dataSource_);
                    Object ds = ctx_.lookup(bindName_);
                    assertCondition("round half even".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                    ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                    failed (e, "Unexpected Exception added by native 11/21/2006");
                }
            }
        }

    
        /**
        setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
        and then verify it was set.
        **/
            public void Var057 ()
            {
                if (checkDecFloatSupport()) {

                    try {
                    JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round half up"); 
                        ctx_.rebind(bindName_, dataSource_);
                        Object ds = ctx_.lookup(bindName_);
                        assertCondition("round half up".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                        ctx_.unbind(bindName_);
                    }
                    catch (Exception e) {
                        failed (e, "Unexpected Exception added by native 11/21/2006");
                    }
                }
            }

        
            /**
             setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
             and then verify it was set.
             **/
            public void Var058 ()
            {
              if (checkDecFloatSupport()) {
                
                try {
                  JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round down"); 
                  ctx_.rebind(bindName_, dataSource_);
                  Object ds = ctx_.lookup(bindName_);
                  assertCondition("round down".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                  ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception added by native 11/21/2006");
                }
              }
            }
            
            
            /**
             setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
             and then verify it was set.
             **/
            public void Var059 ()
            {
              if (checkDecFloatSupport()) {
                
                try {
                  JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round ceiling"); 
                  ctx_.rebind(bindName_, dataSource_);
                  Object ds = ctx_.lookup(bindName_);
                  assertCondition("round ceiling".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                  ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception added by native 11/21/2006");
                }
              }
            }
            
            
            /**
             setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
             and then verify it was set.
             **/
            public void Var060 ()
            {
              if (checkDecFloatSupport()) {
                
                try {
                  JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round floor"); 
                  ctx_.rebind(bindName_, dataSource_);
                  Object ds = ctx_.lookup(bindName_);
                  assertCondition("round floor".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                  ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception added by native 11/21/2006");
                }
              }
            }
            
            
            /**
             setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
             and then verify it was set.
             **/
            public void Var061 ()
            {
              if (checkDecFloatSupport()) {
                
                try {
                  JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round half down"); 
                  ctx_.rebind(bindName_, dataSource_);
                  Object ds = ctx_.lookup(bindName_);
                  assertCondition("round half down".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                  ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception added by native 11/21/2006");
                }
              }
            }
            
            
            /**
             setDecfloatRoundingMode(),getDecfloatRoundingMode() - set the DecfloatRoundingMode property
             and then verify it was set.
             **/
            public void Var062 ()
            {
              if (checkDecFloatSupport()) {
                
                try {
                  JDReflectionUtil.callMethod_V(dataSource_, "setDecfloatRoundingMode", "round up"); 
                  ctx_.rebind(bindName_, dataSource_);
                  Object ds = ctx_.lookup(bindName_);
                  assertCondition("round up".equals(JDReflectionUtil.callMethod_S(ds, "getDecfloatRoundingMode")), "getDecfloatRoundingMode didn't return round half up -- added by native 11/21/2006" ); 
                  ctx_.unbind(bindName_);
                }
                catch (Exception e) {
                  failed (e, "Unexpected Exception added by native 11/21/2006");
                }
              }
            }
            
            
            
                            
    

}








