///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  EVBasicTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.util.Enumeration;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.EnvironmentVariable;
import com.ibm.as400.access.EnvironmentVariableList;
import com.ibm.as400.access.SocketProperties;

import test.EVTest;
import test.Testcase;
import test.EVTest.PropertyChangeListener_;

/**
 Testcase EVBasicTestcase.  This tests the EnvironmentVariable class.
 **/
public class EVBasicTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "EVBasicTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.EVTest.main(newArgs); 
   }
    private static final int LOGIN_TIMEOUT = 2000; /* 2 seconds */ 



    /**
     constructor() with 0 parms - Should work.
     **/
    public void Var001()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            assertCondition((ev.getSystem() == null) && (ev.getName() == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass null for system.
     **/
    public void Var002()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(null, "CLASSPATH");
            failed("Didn't throw exception but got "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass null for name.
     **/
    public void Var003()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, (String) null); 
            failed("Didn't throw exception but got "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with a space for the first character.
     **/
    public void Var004()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, " BadName");
            failed("Didn't throw exception "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with a space as a middle character.
     **/
    public void Var005()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Bad Name");
            failed("Didn't throw exception "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with a space as a last character.
     **/
    public void Var006()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "BadName ");
            failed("Didn't throw exception "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with an equals for the first character.
     **/
    public void Var007()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "=BadName");
            failed("Didn't throw exception "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with an equals as a middle character.
     **/
    public void Var008()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Bad=Name");
            failed("Didn't throw exception "+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass a name with an equals as a last character.
     **/
    public void Var009()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "BadName=");
            failed("Didn't throw exception"+ev);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     constructor() with 2 parms - Pass invalid values.  This should work,
     because the constructor does not check the validity.
     **/
    public void Var010()
    {
        try
        {
            AS400 bogus = new AS400("bogus", "bogus", "bogus".toCharArray());
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            bogus.setSocketProperties(socketProperties ); 

            EnvironmentVariable ev = new EnvironmentVariable(bogus, "Bogus");
            assertCondition((ev.getSystem() == bogus) && (ev.getName().equals("Bogus")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass valid values.
     **/
    public void Var011()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "CLASSPATH");
            assertCondition((ev.getSystem() == pwrSys_) && (ev.getName().equals("CLASSPATH")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     constructor() with 2 parms - Pass a lowercase name.  Verify that it is used.
     **/
    public void Var012()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "LowerCase");
            assertCondition((ev.getSystem() == pwrSys_) && (ev.getName().equals("LowerCase")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     addPropertyChangeListener() - Pass null.
     **/
    public void Var013()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.addPropertyChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener() - Pass a listener.
     **/
    public void Var014()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition(pcl.eventCount_ == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     addPropertyChangeListener() - When the object is deserialized.
     **/
    public void Var015()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT4001");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev2.addPropertyChangeListener(pcl);
            ev2.setName("JT4002");
            assertCondition(pcl.eventCount_ == 1);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - When the system has not been set.
     **/
    public void Var016()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     delete() - When a bad system has been set.
     **/
    public void Var017()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 
            ev.setSystem(system);
            ev.setName("Hello");
            ev.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }

    /**
     delete() - When the name has not been set.
     **/
    public void Var018()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }

    /**
     delete() - When there is no environment variable with that name.
     **/
    public void Var019()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.delete();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
    }

    /**
     delete() - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var020()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400A");
            ev.setValue("Testing ev case");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "jt400A");
            Exception savedException = null;
            try
            {
                ev2.delete();
            }
            catch (Exception e)
            {
                savedException = e;
            }

            ev.delete(); // Cleanup.
            assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - When there is an environment variable with that name.
     **/
    public void Var021()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400B");
            ev.setValue("Testing ev delete");
            ev.delete();

            // Verify that the EV does not exist.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            Properties properties = evList.getProperties();
            boolean found = properties.containsKey("JT400B");

            assertCondition(found == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - When the object is derived from a list.
     **/
    public void Var022()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400C");
            ev.setValue("Testing ev delete from a list");

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("JT400C"))
                    ev2 = temp;
            }

            // tryit.
            if (ev2 != null)
                ev2.delete();

            // Verify that the EV does not exist.
            Properties properties = evList.getProperties();
            boolean found = properties.containsKey("JT400C");

            assertCondition((ev2 != null) && (found == false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     delete() - When the object is deserialized.
     **/
    public void Var023()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400D");
            ev.setValue("Testing ev delete when deserialized");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            ev2.delete();

            // Verify that the EV does not exist.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            Properties properties = evList.getProperties();
            boolean found = properties.containsKey("JT400C");

            assertCondition(found == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When the system has not been set.
     **/
    public void Var024()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.getCCSID();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     getCCSID() - When a bad system has been set.
     **/
    public void Var025()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogusbogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 

            ev.setSystem(system);
            ev.setName("Hello");
            ev.getCCSID();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();
        }
    }



    /**
     getCCSID() - When the name has not been set.
     **/
    public void Var026()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.getCCSID();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     getCCSID() - When there is no environment variable with that name.
     **/
    public void Var027()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.getCCSID();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
    }



    /**
     getCCSID() - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var028()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400E");
            ev.setValue("Testing ev getCCSID() case");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "jt400E");
            Exception savedException = null;
            try
            {
                ev2.getCCSID();
            }
            catch (Exception e)
            {
                savedException = e;
            }

            ev.delete(); // Cleanup.
            assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When there is an environment variable with that name, and the CCSID
     not explicitly set.
     **/
    public void Var029()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400F");
            ev.setValue("Testing ev getCCSID");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400F");
            int ccsid = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition(ccsid == pwrSys_.getCcsid());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When there is an environment variable with that name, and the CCSID
     explicitly set to 37.
     **/
    public void Var030()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400G");
            ev.setValue("Testing ev getCCSID", 37);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400G");
            int ccsid = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition(ccsid == 37);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When there is an environment variable with that name, and the CCSID
     explicitly set to something other than 37.
     **/
    public void Var031()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400H");
            ev.setValue("Testing ev getCCSID", 500);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400H");
            int ccsid = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition(ccsid == 500);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - Verify that the cached value is returned before a refresh.
     **/
    public void Var032()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400I");
            ev.setValue("Testing ev getCCSID", 37);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400I");
            int ccsid = ev2.getCCSID();

            ev.setValue("Testing refresh with ccsid", 835);
            int ccsid2 = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition((ccsid == 37) && (ccsid2 == 37));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - Verify that the refreshed value is returned after a refresh.
     **/
    public void Var033()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400J");
            ev.setValue("Testing ev getCCSID", 37);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400J");
            int ccsid = ev2.getCCSID();

            ev2.refreshValue();

            ev.setValue("Testing refresh with ccsid", 835);
            int ccsid2 = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition((ccsid == 37) && (ccsid2 == 37));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When the object is derived from a list.
     **/
    public void Var034()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400K");
            ev.setValue("Testing ev getCCSID from a list", 37);

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("JT400K"))
                    ev2 = temp;
            }

            // tryit.
            int ccsid = -1;
            if (ev2 != null)
                ccsid = ev2.getCCSID();

            ev.delete(); // Cleanup.

            assertCondition((ev2 != null) && (ccsid == 37));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getCCSID() - When the object is deserialized.
     **/
    public void Var035()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400L");
            ev.setValue("Testing ev getCCSID when deserialized", 37);
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            int ccsid = ev2.getCCSID();

            ev.delete(); // Cleanup.
            assertCondition(ccsid == 37);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getName() - When the name has not been set.
     **/
    public void Var036()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            assertCondition(ev.getName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getName() - When the name has been set.
     **/
    public void Var037()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "HiMom");
            assertCondition(ev.getName().equals("HiMom"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getName() - When the object is derived from a list.
     **/
    public void Var038()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400M");
            ev.setValue("Testing ev getName from a list", 37);

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("JT400M"))
                    ev2 = temp;
            }

            // tryit.
            String name = "";
            if (ev2 != null)
                name = ev2.getName();

            ev.delete(); // Cleanup.

            assertCondition((ev2 != null) && (name.equals("JT400M")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getName() - When the object is deserialized.
     **/
    public void Var039()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400N");
            ev.setValue("Testing ev getName when deserialized");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            String name = ev2.getName();

            ev.delete(); // Cleanup.
            assertCondition(name.equals("JT400N"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getSystem() - When the system has not been set.
     **/
    public void Var040()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            assertCondition(ev.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getSystem() - When the system has been set.
     **/
    public void Var041()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "HiMom");
            assertCondition((ev.getSystem().getSystemName().equals(pwrSys_.getSystemName()))
                            && (ev.getSystem().getUserId().equals(pwrSys_.getUserId())));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getSystem() - When the object is derived from a list.
     **/
    public void Var042()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400O");
            ev.setValue("Testing ev getName from a list", 37);

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("JT400O"))
                    ev2 = temp;
            }

            // tryit.
            AS400 system = null;
            if (ev2 != null)
                system = ev2.getSystem();

            ev.delete(); // Cleanup.
            if (system == null) system=new AS400("null400"); 
            assertCondition((ev.getSystem().getSystemName().equals(system.getSystemName()))
                            && (ev.getSystem().getUserId().equals(system.getUserId())));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getSystem() - When the object is deserialized.
     **/
    public void Var043()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400P");
            ev.setValue("Testing ev getSystem when deserialized");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            AS400 system = ev2.getSystem();

            ev.delete(); // Cleanup.
            assertCondition((ev.getSystem().getSystemName().equals(system.getSystemName()))
                            && (ev.getSystem().getUserId().equals(system.getUserId())));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When the system has not been set.
     **/
    public void Var044()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.getValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     getValue() - When a bad system has been set.
     **/
    public void Var045()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 

            ev.setSystem(system);
            ev.setName("Hello");
            ev.getValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();        // @A1C
        }
    }



    /**
     getValue() - When the name has not been set.
     **/
    public void Var046()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.getValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     getValue() - When there is no environment variable with that name.
     **/
    public void Var047()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.getValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
    }



    /**
     getValue() - When there was an environment variable with that name, the value was cached,
     but the environment variable was deleted.
     **/
    public void Var048()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400Y");
            ev.setValue("This will be deleted");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400Y");
            String value = ev.getValue();
            String value2 = null; 
            ev2.delete();

            Exception savedException = null;
            try
            {
                value2 = ev2.getValue();
            }
            catch (Exception e)
            {
                savedException = e;
            }
            if (savedException == null) { 
              assertCondition(false, "no saved exception but got value2="+value2+" value="+value); 
            } else { 
              assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var049()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400Q");
            ev.setValue("Testing ev getValue() case");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "jt400Q");
            Exception savedException = null;
            try
            {
                ev2.getValue();
            }
            catch (Exception e)
            {
                savedException = e;
            }

            ev.delete(); // Cleanup.
            assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When there is an environment variable with that name, and the CCSID
     not explicitly set.
     **/
    public void Var050()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400R");
            ev.setValue("This is a test of the emergency broadcast network");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400R");
            String value = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value.equals("This is a test of the emergency broadcast network"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When there is an environment variable with that name, and the CCSID
     explicitly set to 37.
     **/
    public void Var051()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400S");
            ev.setValue("CCSIDs are good");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400S");
            String value = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value.equals("CCSIDs are good"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When there is an environment variable with that name, and the CCSID
     explicitly set to something other than 37.
     **/
    public void Var052()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400T");
            ev.setValue("I LOVE CCSIds", 500);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400T");
            String value = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value.equals("I LOVE CCSIds"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - Verify that the cached value is returned without a refresh.
     **/
    public void Var053()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400U");
            ev.setValue("Value before refresh");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400U");
            String value = ev2.getValue();

            ev.setValue("Value after refresh");
            String value2 = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition((value.equals("Value before refresh") && (value2.equals("Value before refresh"))));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - Verify that the refreshed value is returned after a refresh.
     **/
    public void Var054()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400V");
            ev.setValue("Value before refresh");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "JT400V");
            String value = ev2.getValue();

            ev.setValue("Value after refresh");
            ev2.refreshValue();
            String value2 = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition((value.equals("Value before refresh") && (value2.equals("Value after refresh"))));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When the object is derived from a list.
     **/
    public void Var055()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400W");
            ev.setValue("This value comes from a list");

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("JT400W"))
                    ev2 = temp;
            }

            // tryit.
            String value = null;
            if (ev2 != null)
                value = ev2.getValue();

            ev.delete(); // Cleanup.
            if (value == null) value = "nullValue"; 
            assertCondition((ev2 != null) && (value.equals("This value comes from a list")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When the object is deserialized.
     **/
    public void Var056()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "JT400X");
            ev.setValue("This value was serialized");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            String value = ev2.getValue();

            ev.delete(); // Cleanup.
            assertCondition(value.equals("This value was serialized"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     getValue() - When the value length requires a second API call.  This happens when the
     environment variable length is greater than EnvironmentVariable.INITIAL_VALUE_SIZE_.
     **/
    public void Var057()
    {
        try
        {            
            // This creates a 2000 character string.
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < 200; ++i)
                buffer.append("1234567890");
            String value = buffer.toString();

            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX030");
            ev.setValue(value);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX030");
            String value2 = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value2.equals(value));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     refreshValue() - When the system has not been set.
     **/
    public void Var058()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.refreshValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     refreshValue() - When a bad system has been set.
     **/
    public void Var059()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 
            ev.setSystem(system);
            ev.setName("Hello");
            ev.refreshValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();        // @A1C
        }
    }



    /**
     refreshValue() - When the name has not been set.
     **/
    public void Var060()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.refreshValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     refreshValue() - When there is no environment variable with that name.
     **/
    public void Var061()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.refreshValue();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
    }



    /**
     refreshValue() - When there was an environment variable with that name, the value was cached,
     but the environment variable was deleted.
     **/
    public void Var062()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TOOLBOXA");
            ev.setValue("This will be deleted");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TOOLBOXA");
            ev2.refreshValue();
            ev.delete();

            Exception savedException = null;
            try
            {
                ev2.refreshValue();
            }
            catch (Exception e)
            {
                savedException = e;
            }

            assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     refreshValue() - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var063()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TOOLBOXB");
            ev.setValue("Testing ev refreshValue() case");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "toolBOXb");
            Exception savedException = null;
            try
            {
                ev2.refreshValue();
            }
            catch (Exception e)
            {
                savedException = e;
            }

            ev.delete(); // Cleanup.
            assertExceptionIsInstanceOf(savedException, "com.ibm.as400.access.ObjectDoesNotExistException");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     refreshValue() - When there is an environment variable with that name, and the 
     value has not changed.
     **/
    public void Var064()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TOOLBOXC");
            ev.setValue("Value hasn't changed");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TOOLBOXC");
            ev2.refreshValue();
            ev2.refreshValue();
            String value = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value.equals("Value hasn't changed"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     refreshValue() - When there is an environment variable with that name, and the 
     value has changed.
     **/
    public void Var065()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TOOLBOXD");
            ev.setValue("Value has not changed");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TOOLBOXD");
            ev2.refreshValue();
            ev.setValue("Value has changed");
            ev2.refreshValue();
            String value = ev2.getValue();

            ev.delete(); // Cleanup.

            assertCondition(value.equals("Value has changed"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     removePropertyChangeListener() - Pass null.
     **/
    public void Var066()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.removePropertyChangeListener(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



    /**
     removePropertyChangeListener() - Pass a listener.
     **/
    public void Var067()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            ev.removePropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition(pcl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     removePropertyChangeListener() - An object not previously added.
     **/
    public void Var068()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.removePropertyChangeListener(pcl);
            ev.setSystem(pwrSys_);
            assertCondition(pcl.eventCount_ == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setName() - Pass null for name.
     **/
    public void Var069()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



    /**
     setName() - Pass a name with a space for the first character.
     **/
    public void Var070()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName(" BadName");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a name with a space as a middle character.
     **/
    public void Var071()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName("Bad Name");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a name with a space as a last character.
     **/
    public void Var072()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName("BadName ");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a name with an equals for the first character.
     **/
    public void Var073()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName("=BadName");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a name with an equals as a middle character.
     **/
    public void Var074()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName("Bad=Name");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a name with an equals as a last character.
     **/
    public void Var075()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setName("BadName=");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     setName() - Pass a valid uppercase value.  Validate that it works.
     **/
    public void Var076()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TOOLBOXE");
            ev.setValue("Testing setName()");
            ev.refreshValue();
            String value = ev.getValue();
            ev.delete(); // Cleanup.
            assertCondition((ev.getName().equals("TOOLBOXE")) && (value.equals("Testing setName()")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setName() - Pass a valid  mixed case value.  Validate that it works.
     **/
    public void Var077()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "ToOlBoXf");
            ev.setValue("Testing setName() with mixed case name");
            ev.refreshValue();
            String value = ev.getValue();
            ev.delete(); // Cleanup.
            assertCondition((ev.getName().equals("ToOlBoXf")) && (value.equals("Testing setName() with mixed case name")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setName() - After a connection has been established.  Validate that it works.
     **/
    public void Var078()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX080");
            ev.setValue("The first value");
            ev.setName("TBX081");
            ev.setValue("The second value");
            ev.setName("TBX080");
            String value1 = ev.getValue();
            ev.setName("TBX081");
            String value2 = ev.getValue();
            ev.delete(); // Clean up.
            ev.setName("TBX080");
            ev.delete(); // Cleanup.
            assertCondition((value1.equals("The first value")) && (value2.equals("The second value")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setName() - Should fire property change event.
     **/
    public void Var079()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            ev.setName("Goodbye");
            assertCondition((pcl.eventCount_ == 1) 
                            && (pcl.event_.getPropertyName().equals("name"))
                            && (pcl.event_.getOldValue().equals("Hello"))
                            && (pcl.event_.getNewValue().equals("Goodbye")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
     setSystem() - Pass null for system.
     **/
    public void Var080()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setSystem(null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



    /**
     setSystem() - Pass a bogus system.  This should succeed, since validation is not
     yet done.
     **/
    public void Var081()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            ev.setSystem(new AS400("Bogus", "Bogus", "Bogus".toCharArray()));
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setSystem() - Pass a valid system. 
     **/
    public void Var082()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setSystem() - After a connection has been established. 
     **/
    public void Var083()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX040");
            ev.setValue("Testing123");
            ev.delete(); // Clean up.
            ev.setSystem(new AS400());
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     setSystem() - Should fire property change event.
     **/
    public void Var084()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "Hello");
            EVTest.PropertyChangeListener_ pcl = new EVTest.PropertyChangeListener_();
            ev.addPropertyChangeListener(pcl);
            AS400 other = new AS400();
            ev.setSystem(other);
            assertCondition((pcl.eventCount_ == 1) 
                            && (pcl.event_.getPropertyName().equals("system"))
                            && (pcl.event_.getOldValue().equals(pwrSys_))
                            && (pcl.event_.getNewValue().equals(other)));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When the system has not been set.
     **/
    public void Var085()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.setValue("Testing123");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     setValue() with 1 parameter - When a bad system has been set.
     **/
    public void Var086()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 

            ev.setSystem(system);
            ev.setName("Hello");
            ev.setValue("Testing123");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();        // @A1C
        }
    }



    /**
     setValue() with 1 parameter - When the name has not been set.
     **/
    public void Var087()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.setValue("Testing123");
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     setValue() with 1 parameter - Pass null for the value.
     **/
    public void Var088()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX000");
            ev.setValue((String)null);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



    /**
     setValue() with 1 parameter - When there is no environment variable with that name.
     This should create it!
     **/
    public void Var089()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.setValue("Testing123");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("Testing123"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When there is an environment variable with that name.
     This should change it!
     **/
    public void Var090()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX001");
            ev.setValue("Testing 123");
            ev.setValue("Testing 456");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX001");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("Testing 456"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - Set a value to the empty string.
     **/
    public void Var091()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX808");
            ev.setValue("Testing 123");
            ev.setValue("");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX808");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - Pass data that does not work with the job ccsid
     (assuming the job ccsid is 37).
     **/
    public void Var092()
    {
        try
        {
            if (pwrSys_.getCcsid() != 37) 
            {
                notApplicable("System CCSID not 37");
                return;
            }

            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX0011");
            ev.setValue("Testing 123 \u0393");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX0011");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.startsWith("Testing 123"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When there was an environment variable with that name, the value was cached,
     but the environment variable was deleted.
     **/
    public void Var093()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX002");
            ev.setValue("This will be deleted");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX002");
            ev2.delete();

            ev.setValue("This was deleted");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("This was deleted"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var094()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX003");
            ev.setValue("First try");

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "tBx003");
            ev2.setValue("Second try");

            String value1 = ev.getValue();
            String value2 = ev2.getValue();

            ev.delete(); // Cleanup.
            ev2.delete(); // Cleanup.
            assertCondition((value1.equals("First try")) && (value2.equals("Second try")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When the object is derived from a list.
     **/
    public void Var095()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX004");
            ev.setValue("This value comes from a list");

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("TBX004"))
                    ev2 = temp;
            }

            // tryit.
            if (ev2 != null)
                ev2.setValue("This value came from a list");
            ev.refreshValue();
            String value = ev.getValue();

            ev.delete(); // Cleanup.

            assertCondition((ev2 != null) && (value.equals("This value came from a list")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 1 parameter - When the object is deserialized.
     **/
    public void Var096()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX005");
            ev.setValue("This value was serialized");
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            ev2.setValue("This value was deserialized");

            ev.refreshValue();
            String value = ev.getValue();
            ev.delete(); // Cleanup.
            assertCondition(value.equals("This value was deserialized"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }




    /**
     setValue() with 2 parameters - When the system has not been set.
     **/
    public void Var097()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setName("Hello");
            ev.setValue("Testing123", 0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     setValue() with 2 parameters - When a bad system has been set.
     **/
    public void Var098()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            AS400 system = new AS400("bogus", "bogus", "bogus".toCharArray());
            system.setGuiAvailable(false);
            SocketProperties socketProperties = new SocketProperties();
            socketProperties.setLoginTimeout(LOGIN_TIMEOUT); 
            system.setSocketProperties(socketProperties ); 

            ev.setSystem(system);
            ev.setName("Hello");
            ev.setValue("Testing123", 0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            succeeded();        // @A1C
        }
    }



    /**
     setValue() with 2 parameters - When the name has not been set.
     **/
    public void Var099()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable();
            ev.setSystem(pwrSys_);
            ev.setValue("Testing123", 0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalStateException");
        }
    }



    /**
     setValue() with 2 parameters - Pass null for the value.
     **/
    public void Var100()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX000");
            ev.setValue((String)null, 0);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }



    /**
     setValue() with 2 parameters - When there is no environment variable with that name.
     This should create it!
     **/
    public void Var101()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            ev.setValue("Testing123", 0);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "NOTEXIST");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("Testing123"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - When there is an environment variable with that name.
     This should change it!
     **/
    public void Var102()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX001");
            ev.setValue("Testing 123", 0);
            ev.setValue("Testing 456", 0);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX001");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("Testing 456"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }


    /**
     setValue() with 2 parameters - Specify the job ccsid.
     **/
    public void Var103()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX020");
            int ccsid = pwrSys_.getCcsid();
            ev.setValue("Testing with the job ccsid", ccsid);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX020");
            int ccsid2 = ev2.getCCSID();
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition((ccsid != 0) && (ccsid2 == ccsid) &&
                            (value.equals("Testing with the job ccsid")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - Specify a valid ccsid other than 0 or the job ccsid.
     **/
    public void Var104()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX021");
            ev.setValue("Testing with ccsid 835", 500);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX021");
            int ccsid2 = ev2.getCCSID();
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition((ccsid2 == 500) &&
                            (value.equals("Testing with ccsid 835")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - Specify an invalid ccsid.
     **/
    public void Var105()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX021");
            ev.setValue("Testing with an invalid ccsid", 11835);
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.io.IOException");
        }
    }



    /**
     setValue() with 2 parameters - Pass data that does not work with the job ccsid
     (assuming the job ccsid is 37).
     **/
    public void Var106()
    {
        try
        {
            if (pwrSys_.getCcsid() != 37) 
            {
                notApplicable("System CCSID not 37");
                return;
            }

            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX0011");
            ev.setValue("Testing 123 \u0393", 0);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX0011");
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.startsWith("Testing 123"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - When there was an environment variable with that name, the value was cached,
     but the environment variable was deleted.
     **/
    public void Var107()
    {
        try
        {
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX002");
            ev.setValue("This will be deleted", 0);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "TBX002");
            ev2.delete();

            ev.setValue("This was deleted", 0);
            String value = ev2.getValue();

            ev.delete(); // Clean up.

            assertCondition(value.equals("This was deleted"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - When there is an environment variable with that name, but the wrong case.
     **/
    public void Var108()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX003");
            ev.setValue("First try", 0);

            EnvironmentVariable ev2 = new EnvironmentVariable(pwrSys_, "tBx003");
            ev2.setValue("Second try", 0);

            String value1 = ev.getValue();
            String value2 = ev2.getValue();

            ev.delete(); // Cleanup.
            ev2.delete(); // Cleanup.
            assertCondition((value1.equals("First try")) && (value2.equals("Second try")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - When the object is derived from a list.
     **/
    public void Var109()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX004");
            ev.setValue("This value comes from a list", 0);

            // Get it from a list.
            EnvironmentVariableList evList = new EnvironmentVariableList(pwrSys_);
            EnvironmentVariable ev2 = null;
            Enumeration enumeration = evList.getEnvironmentVariables();
            while(enumeration.hasMoreElements()) 
            {
                EnvironmentVariable temp = (EnvironmentVariable)enumeration.nextElement();
                if (temp.getName().equals("TBX004"))
                    ev2 = temp;
            }

            // tryit.
            if (ev2 != null)
                ev2.setValue("This value came from a list", 0);
            ev.refreshValue();
            String value = ev.getValue();

            ev.delete(); // Cleanup.

            assertCondition((ev2 != null) && (value.equals("This value came from a list")));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }



    /**
     setValue() with 2 parameters - When the object is deserialized.
     **/
    public void Var110()
    {
        try
        {            
            EnvironmentVariable ev = new EnvironmentVariable(pwrSys_, "TBX005");
            ev.setValue("This value was serialized", 0);
            EnvironmentVariable ev2 = (EnvironmentVariable)EVTest.serialize(ev, pwrSysEncryptedPassword_);
            ev2.setValue("This value was deserialized", 0);

            ev.refreshValue();
            String value = ev.getValue();
            ev.delete(); // Cleanup.
            assertCondition(value.equals("This value was deserialized"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
