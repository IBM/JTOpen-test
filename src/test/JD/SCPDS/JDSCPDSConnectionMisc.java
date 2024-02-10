///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSCPDSConnectionMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDSCPDSConnectionMisc.java
//
// Classes:      JDSCPDSConnectionMisc
//
////////////////////////////////////////////////////////////////////////
//
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.SCPDS;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.util.Hashtable;
import java.sql.*;
import javax.sql.DataSource;



/**
Testcase JDSCPDSConnectionMisc.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>getCatalog()
<li>setCatalog()
<li>getMetaData()
<li>getTypeMap()
<li>setTypeMap()
<li>toString()
</ul>
**/
public class JDSCPDSConnectionMisc
extends JDTestcase {



    // Private data.
    private              Connection     connection_;
    private              Connection     closedConnection_;



/**
Constructor.
**/
    public JDSCPDSConnectionMisc (AS400 systemObject,
                                 Hashtable namesAndVars,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password)
    {
        super (systemObject, "JDSCPDSConnectionMisc",
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
            DataSource dataSource = (DataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.DB2StdConnectionPoool");
            JDReflectionUtil.callMethod_V(dataSource,"setDatabaseName",system_);
            JDReflectionUtil.callMethod_V(dataSource,"setUser",userId_);
            JDReflectionUtil.callMethod_V(dataSource,"setPassword",PasswordVault.decryptPasswordLeak(encryptedPassword_));


            connection_ = dataSource.getConnection ();
            closedConnection_ = dataSource.getConnection ();
            closedConnection_.close ();
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20StdExt()) {
            connection_.close ();
        }
    }



/**
Queries the relational database directory to get the catalog name for 
*LOCAL.

@exception SQLException If a database access error occurs.
**/
   protected String getRealCatalogName() 
   throws SQLException
   {
      Statement s = connection_.createStatement();
      ResultSet rs = s.executeQuery("SELECT VARCHAR(DBXRDBN, 128) FROM QSYS.QADBXRDBD WHERE DBXRMTN='*LOCAL'");
      rs.next();
      return rs.getString(1).trim();
   }




/**
getCatalog() - Returns the name of the AS/400 to which
we are connected.
**/
    public void Var001()
    {
        if (checkJdbc20StdExt()) {
            try {
                String catalog = connection_.getCatalog ();
                if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                    if ((system_.equalsIgnoreCase("localhost")) ||
                        (system_.equalsIgnoreCase("*LOCAL"))) {
                       assertCondition(catalog.equals(getRealCatalogName()));
                       return;
                    }
                }
                // Toolbox running local will be the Native JDBC driver...
                assertCondition ((catalog.equals (system_.toUpperCase ()))
                        || (catalog.equalsIgnoreCase("localhost"))
                        || (catalog.equalsIgnoreCase("*LOCAL")));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getCatalog() - Should throw an exception when the connection
is closed.
**/
    public void Var002()
    {
        if (checkJdbc20StdExt()) {
            try {
                String catalog = closedConnection_.getCatalog ();
                failed ("Did not throw exception. "+catalog);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
setCatalog() - This should have no effect, since we don't
support catalogs.
**/
    public void Var003()
    {
        if (checkJdbc20StdExt()) {
            try {
                connection_.setCatalog ("Sears");
                String catalog = connection_.getCatalog ();
                if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                    if ((system_.equalsIgnoreCase("localhost")) ||
                        (system_.equalsIgnoreCase("*LOCAL"))) {
                       assertCondition(catalog.equals(getRealCatalogName()));
                       return;
                    }
                }
                assertCondition ((catalog.equals (system_.toUpperCase ()))
                        || (catalog.equalsIgnoreCase("localhost"))
                        || (catalog.equalsIgnoreCase("*LOCAL")));   // @C1A
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
setCatalog() - Should throw an exception when the connection
is closed.
**/
    public void Var004()
    {
        if (checkJdbc20StdExt()) {
            try {
                closedConnection_.setCatalog ("JCPenney");
                failed ("Did not throw exception.");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getMetaData() - Returns the meta data when the connection
is open.
**/
    public void Var005()
    {
        if (checkJdbc20StdExt()) {
            try {
                DatabaseMetaData dmd = connection_.getMetaData ();
                assertCondition (dmd != null);
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
getMetaData() - Returns the meta data when the connection
is closed.
**/
    public void Var006()
    {
        if (checkJdbc20StdExt()) {
            try {
                DatabaseMetaData dmd = closedConnection_.getMetaData ();
                failed("Did not throw exception "+dmd);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getTypeMap() - Throws an exception, not supported.
**/
    public void Var007()
    {
        notApplicable ("Type map not supported.");
//*CUJO-Pulled JDBC 2.0         if(checkJdbc20StdExt()) { try {
//*CUJO-Pulled JDBC 2.0             connection_.getTypeMap ();
//*CUJO-Pulled JDBC 2.0             failed ("Did not throw exception.");
//*CUJO-Pulled JDBC 2.0         }
//*CUJO-Pulled JDBC 2.0         catch (Exception e) {
//*CUJO-Pulled JDBC 2.0             assertExceptionIsInstanceOf (e, "java.sql.SQLException");
//*CUJO-Pulled JDBC 2.0         }
    }



/**
setTypeMap() - Throws an exception, not supported.
**/
    public void Var008()
    {
        notApplicable ("Type map not supported.");       
        /* We can not compile this without JDK 1.2, and 
           we are currently compiling with JDK 1.1.6.
           No big deal, since we don't support this method
           anyway.
        */
//*CUJO-Pulled JDBC 2.0        if(checkJdbc20StdExt()) { try {
//*CUJO-Pulled JDBC 2.0            connection_.setTypeMap (new Hashtable ());
//*CUJO-Pulled JDBC 2.0            failed ("Did not throw exception.");
//*CUJO-Pulled JDBC 2.0        }
//*CUJO-Pulled JDBC 2.0        catch (Exception e) {
//*CUJO-Pulled JDBC 2.0            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
//*CUJO-Pulled JDBC 2.0        }
    }



/**
toString() - Returns the name of the AS/400 to which
we are connected.
**/
    public void Var009()
    {
        if (checkJdbc20StdExt()) {
            try {
                String s = connection_.toString ();
                if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                    if ((system_.equalsIgnoreCase("localhost")) ||
                        (system_.equalsIgnoreCase("*LOCAL"))) {
                       assertCondition(s.equals(getRealCatalogName()));
                       return;
                    }
                }
                assertCondition ((s.equals (system_.toUpperCase ()))
                        || (s.equalsIgnoreCase("localhost"))
                        || (s.equalsIgnoreCase("*LOCAL")));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



/**
toString() - Returns the name of the AS/400 when the connection
is closed.

SQL400 - The DB2ConnectionHandle object (which is the connection
object when using pooled connections) is invalidated when
the connection is closed. So actually toString returns null when the 
connection is closed
**/
    public void Var010()
    {
        if (checkJdbc20StdExt()) {
            try {
                String s = closedConnection_.toString ();
                if (getDriver () == JDTestDriver.DRIVER_NATIVE) {
                    assertCondition(s.equals(""));
                    return;
                }

                assertCondition (s.equals (system_.toUpperCase ()));
            }
            catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
        }
    }



}



