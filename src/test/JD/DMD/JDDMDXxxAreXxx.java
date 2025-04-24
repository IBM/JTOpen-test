///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDXxxAreXxx.java
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
 // File Name:    JDDMDXxxAreXxx.java
 //
 // Classes:      JDDMDXxxAreXxx
 //
 ////////////////////////////////////////////////////////////////////////
 //
 //
 //
 //
 //     
 // 
 //
 ////////////////////////////////////////////////////////////////////////

package test.JD.DMD;

import com.ibm.as400.access.AS400;

import test.JDTestDriver;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;

import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDDMDXxxAreXxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>deletesAreDetected() 
<li>insertsAreDetected()      
<li>othersDeletesAreVisible()    
<li>othersInsertsAreVisible()       
<li>othersUpdatesAreVisible()          
<li>ownDeletesAreVisible()                
<li>ownInsertsAreVisible()                   
<li>ownUpdatesAreVisible()                      
<li>updatesAreDetected()                            
</ul>
**/
public class JDDMDXxxAreXxx
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDXxxAreXxx";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }
    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;



/**
Constructor.
**/
    public JDDMDXxxAreXxx (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDXxxAreXxx",
            namesAndVars, runMode, fileOutputStream,
            password);
    }




/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup() throws Exception {
    String url;
    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      url = "jdbc:db2://" + systemObject_.getSystemName() + ":446/*LOCAL"; 
      
      connection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
      dmd_ = connection_.getMetaData();

      closedConnection_ = testDriver_.getConnection(url, systemObject_.getUserId(), encryptedPassword_);
      dmd2_ = closedConnection_.getMetaData();
      closedConnection_.close();
        
        } else {
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            url = "jdbc:as400://" + systemObject_.getSystemName()
                
                ;
        else if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE)
            url = "jdbc:jtopenlite://" + systemObject_.getSystemName()
                
                ;
        else
            url = "jdbc:db2://" + systemObject_.getSystemName()
                
                ;


        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
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
        connection_.close ();
    }



/**
deletesAreDetected() - Should throw an exception when the
type is not valid.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.deletesAreDetected (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
deletesAreDetected() - Should return the correct values when
type is forward only.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.deletesAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
deletesAreDetected() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.deletesAreDetected (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
deletesAreDetected() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.deletesAreDetected (ResultSet.TYPE_SCROLL_SENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
deletesAreDetected() - Should return the correct value
on a closed connection.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.deletesAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
insertsAreDetected() - Should throw an exception when the
type is not valid.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.insertsAreDetected (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
insertsAreDetected() - Should return the correct values when
type is forward only.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.insertsAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
insertsAreDetected() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.insertsAreDetected (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
insertsAreDetected() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.insertsAreDetected (ResultSet.TYPE_SCROLL_SENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
insertsAreDetected() - Should return the correct value
on a closed connection.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.insertsAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersDeletesAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.othersDeletesAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
othersDeletesAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersDeletesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersDeletesAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersDeletesAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersDeletesAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersDeletesAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersDeletesAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.othersDeletesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersInsertsAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.othersInsertsAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
othersInsertsAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersInsertsAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersInsertsAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersInsertsAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersInsertsAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersInsertsAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersInsertsAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var020()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.othersInsertsAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersUpdatesAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var021()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.othersUpdatesAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
othersUpdatesAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersUpdatesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersUpdatesAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var023()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersUpdatesAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
othersUpdatesAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.othersUpdatesAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
othersUpdatesAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.othersUpdatesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
ownDeletesAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.ownDeletesAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
ownDeletesAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var027()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownDeletesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownDeletesAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownDeletesAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownDeletesAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var029()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownDeletesAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
ownDeletesAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var030()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.ownDeletesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
ownInsertsAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var031()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.ownInsertsAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
ownInsertsAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var032()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownInsertsAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownInsertsAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var033()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownInsertsAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownInsertsAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var034()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownInsertsAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
ownInsertsAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var035()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.ownInsertsAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownUpdatesAreVisible() - Should throw an exception when the
type is not valid.
**/
    public void Var036()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.ownUpdatesAreVisible (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
ownUpdatesAreVisible() - Should return the correct values when
type is forward only.
**/
    public void Var037()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownUpdatesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownUpdatesAreVisible() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var038()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownUpdatesAreVisible (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
ownUpdatesAreVisible() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var039()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.ownUpdatesAreVisible (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
ownUpdatesAreVisible() - Should return the correct value
on a closed connection.
**/
    public void Var040()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.ownUpdatesAreVisible (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updatesAreDetected() - Should throw an exception when the
type is not valid.
**/
    public void Var041()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.updatesAreDetected (-99);
                failed ("Didn't throw SQLException");
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
updatesAreDetected() - Should return the correct values when
type is forward only.
**/
    public void Var042()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.updatesAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updatesAreDetected() - Should return the correct values when
type is scroll insensitive.
**/
    public void Var043()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.updatesAreDetected (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
updatesAreDetected() - Should return the correct values when
type is scroll sensitive.
**/
    public void Var044()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.updatesAreDetected (ResultSet.TYPE_SCROLL_SENSITIVE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
updatesAreDetected() - Should return the correct value
on a closed connection.
**/
    public void Var045()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.updatesAreDetected (ResultSet.TYPE_FORWARD_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }


}

