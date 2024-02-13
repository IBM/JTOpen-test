///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDriverAcceptsURL.java
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
 // File Name:    JDDriverAcceptsURL.java
 //
 // Classes:      JDDriverAcceptsURL
 //
 ////////////////////////////////////////////////////////////////////////
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.Driver;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Hashtable;



/**
Testcase JDDriverAcceptsURL.  This tests the following method
of the JDBC Driver class:

<ul>
<li>acceptsURL()
</ul>
**/
public class JDDriverAcceptsURL
extends JDTestcase
{



    // Private data.
    private Driver      driver_;
    private String      subprotocol_;



/**
Constructor.
**/
    public JDDriverAcceptsURL (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    String password)
    {
        super (systemObject, "JDDriverAcceptsURL",
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
        driver_ = DriverManager.getDriver (baseURL_);

	if (isToolboxDriver()) {
	    subprotocol_ = "as400"; 
	} else if (getDriver () == JDTestDriver.DRIVER_NATIVE || getDriver () == JDTestDriver.DRIVER_JCC ) { 
           subprotocol_ = "db2";
	} else if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE ) {
	   subprotocol_ =  "jtopenlite";
	}
    }



/**
acceptsURL() - Should return false when URL is null.
**/
    public void Var001()
    {
        try {
            assertCondition (driver_.acceptsURL (null) == false);
        }
        catch (Exception e) {
            // The JCC driver throws a NPE.  This is probably wrong. 
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
               if (e instanceof NullPointerException ) {
                 succeeded("JCC driver throws NPE when null passed to acceptsURL"); 
               } else { 
                 failed(e, "Unexpected exception");
               }
            } else { 
               failed(e, "Unexpected exception");
            }   
        }
    }



/**
acceptsURL() - Should return false when URL is empty.
**/
    public void Var002()
    {
        try {
            assertCondition (driver_.acceptsURL ("") == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should return false when URL is just colons.
**/
    public void Var003()
    {
        try {
            assertCondition (driver_.acceptsURL ("::") == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should return false when URL has no protocol.
**/
    public void Var004()
    {
        try {
            assertCondition (driver_.acceptsURL (":" + subprotocol_ + "://" + system_) == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should return false when URL has different protocol.
**/
    public void Var005()
    {
        try {
            assertCondition (driver_.acceptsURL ("hola:" + subprotocol_ + "://" + system_) == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should return false when URL has no subprotocol.
**/
    public void Var006()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:://" + system_) == false);
          
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should return false when URL has different subprotocol.
**/
    public void Var007()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:odbc://" + system_) == false);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(no colon or slashes).
**/
    public void Var008()
    {
        try {
          boolean result = driver_.acceptsURL ("jdbc:" + subprotocol_);  
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            if (result == false) {
              succeeded("JCC driver does not accept a URL with no system");
            } else {
              succeeded("JCC now like toolbox and native");
            }
          } else { 
            assertCondition (result == true);
          }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(a colon but no slashes).
**/
    public void Var009()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(a colon and one slash).
**/
    public void Var010()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":/") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(a colon and 2 slashes).
**/
    public void Var011()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(a colon and 3 slashes).
**/
    public void Var012()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":///") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no system specified
(with properties specified).
**/
  public void Var013() {
    try {
      // changed by native since DB2Driver cannot use startsWith() when checking
      // the url subprotocol
      // if startsWith() is used the Cloudscape driver will not work
      if ((getDriver() == JDTestDriver.DRIVER_NATIVE)) {
        assertCondition(driver_.acceptsURL("jdbc:" + subprotocol_
            + ":errors=full") == true);
      } else {
        boolean result = driver_.acceptsURL("jdbc:" + subprotocol_
            + ";errors=full");
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
           if (result == false) {
             succeeded("JCC driver does not accept a URL with no system specified");
           } else {
             succeeded("JCC driver now accepts a URL with no system specified");
            
           }
        } else {
           assertCondition(result  == true);
        }       
      }
    } catch (Exception e) {
      failed(e, "Unexpected exception");
    }
  }



/**
acceptsURL() - Should accept a valid URL with just a system
specified (no slashes).
**/
    public void Var014()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":" + system_) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with just a system
specified (1 slash).
**/
    public void Var015()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":/" + system_) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with just a system
specified (2 slashes).
**/
    public void Var016()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with just a system
specified (3 slashes).
**/
    public void Var017()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":///" + system_) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with just a system
specified (and a slash after but no default schema).
**/
    public void Var018()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_ + "/") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a default
schema specified (no slashes).
**/
    public void Var019()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":" + system_
                + "/QIWS") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a default
schema specified (1 slash).
**/
    public void Var020()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":/" + system_
                + "/QIWS") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a default
schema specified (2 slashes).
**/
    public void Var021()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + "/QIWS") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a default
schema specified (3 slashes).
**/
    public void Var022()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + ":///" + system_
                + "/QIWS") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a port number
specified.
**/
    public void Var023()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ":50") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a port number
and default schema specified.
**/
    public void Var024()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ":50/QIWS") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with no properties
specified but a semicolon is there.
**/
    public void Var025()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ";") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a property
specified (no trailing semicolon).
**/
    public void Var026()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ";user=" + userId_) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with a property
specified (1 trailing semicolon).
**/
    public void Var027()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ";user=" + userId_ + ";") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
acceptsURL() - Should accept a valid URL with 2 properties
specified.
**/
    public void Var028()
    {
    	if (checkPasswordLeak()) { 
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ";user=" + userId_ + ";password=" + 
                PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDDriverAcceptsURL.28")) == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    	}
    }



/**
acceptsURL() - Should accept a valid URL with an unrecognized property
specified.
**/
    public void Var029()
    {
        try {
            assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_ + "://" + system_
                + ";himom=lookatme") == true);
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }

/**
acceptsURL() - Should not accept a URL with suprotocol db2j -- Cloudscape driver
(no colon or slashes).
**/
    public void Var030()
    {
	if ((getDriver() == JDTestDriver.DRIVER_NATIVE)) {
	    try {
		assertCondition (!driver_.acceptsURL ("jdbc:" + subprotocol_ + "j"), "Native driver accepted subprotocol: " + subprotocol_ + "j -- added by native 3/3/2004");
	    }
	    catch (Exception e) {
		failed(e, "Unexpected exception -- added by native 3/3/2004");
	    }
	} else {
	    notApplicable("V5R3 Native driver variation -- added by native 3/3/2004");
	}
    }

/**
acceptsURL() - Should accept a URL with suprotocol DB2
(no colon or slashes).
**/
    public void Var031()
    {
	if ( (getDriver() == JDTestDriver.DRIVER_NATIVE)) {
	    try {
		assertCondition (driver_.acceptsURL ("jdbc:" + subprotocol_.toUpperCase()), "Native driver did not accepted subprotocol: " + subprotocol_.toUpperCase() + " -- added by native 3/3/2004");
	    }
	    catch (Exception e) {
		failed(e, "Unexpected exception -- added by native 3/3/2004");
	    }
	} else {
	    notApplicable("V5R3 Native driver variation -- added by native 3/3/2004");
	}
    }



}
