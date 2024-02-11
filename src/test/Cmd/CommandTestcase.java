///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommandTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Cmd;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.Command;
import com.ibm.as400.access.Command;
import com.ibm.as400.access.MessageFile;


import test.Testcase;

/**
  Testcase CommandTestcase.
 **/
public class CommandTestcase extends Testcase
{
    //private boolean rejectChange = false;
    private final String NONE = "*NONE";
    private final boolean crtusrprf_allowsLimitedUser = false;
    private final int crtusrprf_ccsid = 37;
    private final String crtusrprf_cmdProcessingProgram = "/QSYS.LIB/QSYUP.PGM";
    private final String crtusrprf_currentLibrary = "*NOCHG";
    private final String crtusrprf_description = "CREATE USER PROFILE";
    private final String crtusrprf_helpIdentifier = "CRTUSRPRF";
    private final String crtusrprf_helpSearchIndex = NONE;
    private final int crtusrprf_maximumPositionalParameters = 3;
    private final String crtusrprf_productLibrary = "*NOCHG";
    private final String crtusrprf_promptOverrideProgram = NONE;
    private final String crtusrprf_restrictedRelease = "";
    //private final byte crtusrprf_threadSafety = 0x0000;
    private final String crtusrprf_validityCheckProgram = "/QSYS.LIB/ISAVLDCK.PGM";
    private final String crtusrprf_whereAllowedToRun = "111111111000000";


    /**
      Constructor.  This is called from the HTMLTest constructor.
     **/
    public CommandTestcase(AS400 systemObject,
                           Vector variationsToRun,
                           int runMode,
                           FileOutputStream fileOutputStream)
    {
        super(systemObject, "CommandTestcase", variationsToRun,
              runMode, fileOutputStream);
    }


    /**
      The PropertyChangeListener_ class is for testing.
     **/
    private class PropertyChangeListener_
    implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_;

        public void propertyChange (PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }

        public PropertyChangeEvent getLastEvent ()
        {
            return lastEvent_;
        }
    }


    /**
      Successful construction of an Command using the default ctor.
 
      Command()
     **/
    public void Var001()
    {
        try
        {
            Command cmd = new Command();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
      Successful construction of an Command using the system and path parameters..
 
      Command(AS400, String)
     **/
    public void Var002()
    {
        try
        {
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
      Construct an Command with the default ctor.
      Set the path and rretrieve it to verify setting.
      
      Command()
      Command::setPath(String)
      Command::getPath()
     **/
    public void Var003()
    {
        try
        {
            Command cmd = new Command();
            cmd.setPath("/QSYS.LIB/CRTUSRPRF.CMD");
            if (cmd.getPath().equals("/QSYS.LIB/CRTUSRPRF.CMD"))
            {
                succeeded();
            }
            else
                failed("Incorrect path returned.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
      Construct an Command with the default ctor.
      Set the system and retrieve it to verify setting.
      
      Command()
      Command::setSystem(AS400)
      Command::getSystem()
     **/
    public void Var004()
    {
        try
        {
            Command cmd = new Command();
            cmd.setSystem(new AS400("test", "uid", "pwd"));
            if (cmd.getSystem().getSystemName().equals("test"))
            {
                succeeded();
            }
            else
                failed("Incorrect system returned.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command with the default ctor.
      Set the path with a null parm.
      A NullPointerException should be thrown
      
      Command()
      Command::setPath(null)
      **/
    public void Var005()
    {
        try
        {
            Command cmd = new Command();
            cmd.setPath(null);
            failed("No Exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
      Construct an Command with the default ctor.
      Set the system with a null value.
      A NullPointerException should be thrown
      
      Command()
      Command::setSystem(null)
     **/
    public void Var006()
    {
        try
        {
            Command cmd = new Command();
            cmd.setSystem(null);
            failed("No Exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }


    /**
      Serialization.  Verify that the object is properly serialized
      when the path and system, are not set.
     
      Command
     **/
    public void Var007()                            
    {
        try
        {
            Command cmd = new Command ();
            Command cmd2 = (Command) serialize (cmd);
            assertCondition ((cmd2.getPath() == null)
                             && (cmd.getPath() == null)
                             && (cmd.getSystem() == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
      Serialization.  Verify that the object is properly serialized
      when the data, cmd, language, direction, and attributes are set.
      
      Command
     **/
    public void Var008()            
    {
        try
        {
            Command cmd = new Command();
            cmd.setPath("/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.setSystem(new AS400("blah", "blah", "blah"));
            Command cmd2 = (Command) serialize (cmd);
            assertCondition ((cmd2.getPath().equals("/QSYS.LIB/CRTUSRPRF.CMD"))
                             && (cmd2.getSystem().getSystemName().equals("blah"))  );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }                 



    /**
      Command::addPropertyChangeListener().  Add null.
      Verify a NullPointerException is thrown.
     **/
    public void Var009()
    {
        try
        {
            Command cmd = new Command ();
            cmd.addPropertyChangeListener(null);
            failed ("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    /**
      Command::addPropertyChangeListener().  Add a valid listener.
      Verify that an event gets fired when setPath() is called.
     **/
    public void Var010()
    {
        try
        {
            Command cmd = new Command ();
            PropertyChangeListener_ l = new PropertyChangeListener_ ();
            cmd.addPropertyChangeListener (l);
            PropertyChangeEvent before = l.getLastEvent ();
            cmd.setPath("/QSYS.LIB/CRTUSRPRF.CMD");
            PropertyChangeEvent after = l.getLastEvent ();
            assertCondition ((before == null) && (after.getSource () == cmd)
                             && (after.getPropertyName ().equals ("path"))
                             && (after.getOldValue () == null)
                             && (after.getNewValue ().equals("/QSYS.LIB/CRTUSRPRF.CMD") ));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
      Command::addPropertyChangeListener().  Add a valid listener.
      Verify that an event gets fired when setSystem() is called.
     **/
    public void Var011()
    {
        try
        {
            Command cmd = new Command ();
            PropertyChangeListener_ l = new PropertyChangeListener_ ();
            cmd.addPropertyChangeListener (l);
            PropertyChangeEvent before = l.getLastEvent ();
            AS400 sys = new AS400("blah", "blah", "blah");
            cmd.setSystem(sys);
            PropertyChangeEvent after = l.getLastEvent ();
            assertCondition ((before == null) && (after.getSource () == cmd)
                             && (after.getPropertyName ().equals ("system"))
                             && (after.getOldValue () == null)
                             && (after.getNewValue () == sys) );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
      Command::removePropertyChangeListener().  Remove null.
     **/
    public void Var012()
    {
        try
        {
            Command cmd = new Command ();
            cmd.removePropertyChangeListener (null);
            failed ("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    /**
      Command::removePropertyChangeListener().  Remove a valid listener.
      Verify that no events get fired when setPath() is called.
     **/
    public void Var013()
    {
        try
        {
            Command cmd = new Command ();
            PropertyChangeListener_ l = new PropertyChangeListener_ ();
            cmd.addPropertyChangeListener (l);
            cmd.removePropertyChangeListener (l);
            PropertyChangeEvent before = l.getLastEvent ();
            cmd.setPath("/QSYS.LIB/CRTUSRPRF.CMD");
            PropertyChangeEvent after = l.getLastEvent ();
            assertCondition ((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
      Command::removePropertyChangeListener().  Remove a valid listener.
      Verify that no events get fired when setSystem() is called.
     **/
    public void Var014()
    {
        try
        {
            Command cmd = new Command ();
            PropertyChangeListener_ l = new PropertyChangeListener_ ();
            cmd.addPropertyChangeListener (l);
            cmd.removePropertyChangeListener (l);
            PropertyChangeEvent before = l.getLastEvent ();
            cmd.setSystem(new AS400("blah", "blah", "blah"));
            PropertyChangeEvent after = l.getLastEvent ();
            assertCondition ((before == null) && (after == null));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }



    /**
      Construct an Command with the default ctor.
      Get the xml of a command, without setting the parms.
      A ExtendedIllegalStateException should be thrown.
      
      Command()
     **/
    public void Var015()
    {
        try
        {
            Command cmd = new Command();
            cmd.getXML();
            failed("No Exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException", "system: Property is not set."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }


    /**
      Construct an Command with the default ctor.
      Get the XML of a command, without setting the parms.
      A ExtendedIllegalStateException should be thrown.
      
      Command()
     **/
    public void Var016()
    {
        try
        {
            Command cmd = new Command();
            cmd.setSystem(systemObject_);
            cmd.getXML();
            failed("No Exception.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ExtendedIllegalStateException", "path: Property is not set."))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::allowsLimitedUser()
     **/
    public void Var017()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.allowsLimitedUser() == crtusrprf_allowsLimitedUser)
                succeeded();
            else
                failed("Invalid command property: " + cmd.allowsLimitedUser());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getCCSID()
     **/
    public void Var018()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getCCSID() == crtusrprf_ccsid)
                succeeded();
            else
                failed("Invalid command property: " + cmd.getCCSID());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getCommandProcessingProgram()
     **/
    public void Var019()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getCommandProcessingProgram().equals(crtusrprf_cmdProcessingProgram))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getCommandProcessingProgram()+" expected:"+crtusrprf_cmdProcessingProgram+" Updated:5/27/2015 from '/QSYS.LIB/QSYUP.LIB/QSYS.PGM'");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getCommandProcessingState()
     **/
    public void Var020()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getCommandProcessingState().equals(Command.SYSTEM_STATE) || cmd.getCommandProcessingState().equals(Command.USER_STATE))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getCommandProcessingState());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getCurrentLibrary()
     **/
    public void Var021()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getCurrentLibrary().equals(crtusrprf_currentLibrary))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getCurrentLibrary());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getDescription()
     **/
    public void Var022()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getDescription().equalsIgnoreCase(crtusrprf_description))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getDescription());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getHelpIdentifier()
     **/
    public void Var023()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getHelpIdentifier().equals(crtusrprf_helpIdentifier))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getHelpIdentifier());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getHelpPanelGroup()
     **/
    public void Var024()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getHelpPanelGroup() != null)
                succeeded();
            else
                failed("Panel Group is null");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getHelpSearchIndex()
     **/
    public void Var025()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getHelpSearchIndex().equals(crtusrprf_helpSearchIndex))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getHelpSearchIndex());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }         


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getMaximumPositionalParameters()
     **/
    public void Var026()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getMaximumPositionalParameters() == crtusrprf_maximumPositionalParameters)
                succeeded();
            else
                failed("Invalid command property: " + cmd.getMaximumPositionalParameters());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getMessageFile()
     **/
    public void Var027()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getMessageFile() != null)
                succeeded();
            else
                failed("MessageFile is null");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getMultithreadedJobAction()
     **/
    public void Var028()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getMultithreadedJobAction() == Command.ACTION_SYSTEM_VALUE ||
                cmd.getMultithreadedJobAction() == Command.ACTION_NO_MESSAGE ||
                cmd.getMultithreadedJobAction() == Command.ACTION_INFO_MESSAGE ||
                cmd.getMultithreadedJobAction() == Command.ACTION_ESCAPE_MESSAGE )
                succeeded();
            else
                failed("Invalid command property: " + cmd.getMultithreadedJobAction());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getProductLibrary()
     **/
    public void Var029()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getProductLibrary().equals(crtusrprf_productLibrary))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getProductLibrary());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getPromptMessageFile()
     **/
    public void Var030()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getPromptMessageFile() != null)
                succeeded();
            else
                failed("Prompt messagefile is null");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getPromptOverrideProgram()
     **/
    public void Var031()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getPromptOverrideProgram().equals(crtusrprf_promptOverrideProgram))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getPromptOverrideProgram());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getPromptOverrideState()
     **/
    public void Var032()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getPromptOverrideState().equals(Command.SYSTEM_STATE) ||
                cmd.getPromptOverrideState().equals(Command.USER_STATE) )
                succeeded();
            else
                failed("Invalid command property: " + cmd.getPromptOverrideState());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getRestrictedRelease()
     **/
    public void Var033()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getRestrictedRelease().equals(crtusrprf_restrictedRelease))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getRestrictedRelease());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getSourceFile()
     **/
    public void Var034()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getSourceFile().indexOf("/QSYS.LIB/QTEMP.LIB")  != -1 &&
                cmd.getSourceFile().indexOf("CRTUSRPRF.MBR") != -1)
                succeeded();
            else
                failed("Invalid command property: " + cmd.getSourceFile());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getThreadSafety()
     **/
    public void Var035()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getThreadSafety() == Command.THREADSAFE_NO ||
                cmd.getThreadSafety() == Command.THREADSAFE_YES ||
                cmd.getThreadSafety() == Command.THREADSAFE_CONDITIONAL)
                succeeded();
            else
                failed("Invalid command property: " + cmd.getThreadSafety());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getValidityCheckProgram()
     **/
    public void Var036()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getValidityCheckProgram().equals(crtusrprf_validityCheckProgram) ||
                cmd.getValidityCheckProgram().equals(NONE))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getValidityCheckProgram());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }



    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getValidityCheckState()
     **/
    public void Var037()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getValidityCheckState().equals(Command.SYSTEM_STATE) ||
                cmd.getValidityCheckState().equals(Command.USER_STATE) )
                succeeded();
            else
                failed("Invalid command property: " + cmd.getValidityCheckState());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getWhereAllowedToRun()
     **/
    public void Var038()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.getWhereAllowedToRun().equals(crtusrprf_whereAllowedToRun))
                succeeded();
            else
                failed("Invalid command property: " + cmd.getWhereAllowedToRun());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::getXML()
     **/
    public void Var039()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            String xml = cmd.getXML();

            if (xml != null && xml.length() > 0)
                succeeded();
            else
                failed("invalid XML returned");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_BATCH_PROGRAM)
     **/
    public void Var040()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_BATCH_PROGRAM) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_BATCH_PROGRAM));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_INTERACTIVE_PROGRAM)
     **/
    public void Var041()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_PROGRAM) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_PROGRAM));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_EXEC)
     **/
    public void Var042()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_EXEC) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_EXEC));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_INTERACTIVE_JOB)
     **/
    public void Var043()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_JOB) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_JOB));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_BATCH_JOB)
     **/
    public void Var044()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_BATCH_JOB) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_BATCH_JOB));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_BATCH_REXX_PROCEDURE)
     **/
    public void Var045()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_BATCH_REXX_PROCEDURE) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_BATCH_REXX_PROCEDURE));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_INTERACTIVE_REXX_PROCEDURE)
     **/
    public void Var046()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_REXX_PROCEDURE) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_INTERACTIVE_REXX_PROCEDURE));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRun(ALLOW_ALL)
     **/
    public void Var047()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRun(Command.ALLOW_ALL) ==  true)
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRun(Command.ALLOW_ALL));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRunBatch()
     **/
    public void Var048()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRunBatch() == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRunBatch());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isAllowedToRunInteractive()
     **/
    public void Var049()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isAllowedToRunInteractive() == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isAllowedToRunInteractive());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isEnabledForGUI()
     **/
    public void Var050()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isEnabledForGUI() == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isEnabledForGUI());

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isOperatingMode(MODE_PRODUCTION)
     **/
    public void Var051()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isOperatingMode(Command.MODE_PRODUCTION) == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isOperatingMode(Command.MODE_PRODUCTION));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isOperatingMode(MODE_SERVICE)
     **/
    public void Var052()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isOperatingMode( Command.MODE_SERVICE) == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isOperatingMode(Command.MODE_SERVICE));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isOperatingMode(MODE_DEBUG)
     **/
    public void Var053()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isOperatingMode(Command.MODE_DEBUG) == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isOperatingMode(Command.MODE_DEBUG));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::isOperatingMode(MODE_ALL)
     **/
    public void Var054()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();

            if (cmd.isOperatingMode( Command.MODE_ALL) == true )
                succeeded();
            else
                failed("Invalid command property: " + cmd.isOperatingMode(Command.MODE_ALL));

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Construct an Command.
      
      Command(AS400, String)
      Command::toString()
     **/
    public void Var055()
    {
        try
        {
            // create a Command
            Command cmd = new Command(systemObject_, "/QSYS.LIB/CRTUSRPRF.CMD");
            cmd.refresh();
            String cs = cmd.toString();

            if (cs.length() != 0)
                succeeded();
            else
                failed("toString length is zero");

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
      Serializes and deserializes the Command object.
     **/
    private Command serialize (Command cmd)
    throws Exception
    {
        // Serialize.
        String serializeFileName = "Commandbeans.ser";
        ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFileName));
        out.writeObject (cmd);
        out.flush ();

        // Deserialize.
        Command head2 = null;
        try
        {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFileName));
            head2 = (Command) in.readObject ();
        }
        finally
        {
            File f = new File (serializeFileName);
            f.delete();
        }
        return head2;
    }
}



