///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSCmdTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ObjectDescription;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.Testcase;

/**
 *Testcase NLSCmdTestcase.  This test class verifies the use of DBCS Strings
 *in selected Cmd testcase variations.
**/
public class NLSCmdTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSCmdTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  long start;
  long time;

  String cmd_string = getResource("CMD_STRING");
  String cmd_library_desc = getResource("CMD_LIBRARY_DESC");

  String cmd_lib_name = "NLCMDJTST";
  String cmd_crt_lib = "CRTLIB LIB(" + cmd_lib_name + ") AUT(*ALL) TEXT('" + cmd_library_desc + "')";
  String cmd_path_name = "/QSYS.LIB/"+cmd_lib_name+".LIB";


  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public NLSCmdTestcase(AS400            systemObject,
                      Vector<String>           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream
                      )
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSCmdTestcase", 6,
          variationsToRun, runMode, fileOutputStream);
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      systemObject_.connectService(AS400.COMMAND);
    }
    catch(Exception e)
    {
      System.out.println("Unable to connect to the AS/400");
      e.printStackTrace();
      return;
    }

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }

    if ((allVariations || variationsToRun_.contains("2")) &&
        runMode_ != ATTENDED)
    {
      setVariation(2);
      Var002();
    }

    if ((allVariations || variationsToRun_.contains("3")) &&
        runMode_ != ATTENDED)
    {
      setVariation(3);
      Var003();
    }

    if ((allVariations || variationsToRun_.contains("4")) &&
        runMode_ != ATTENDED)
    {
      setVariation(4);
      Var004();
    }

    if ((allVariations || variationsToRun_.contains("5")) &&
        runMode_ != ATTENDED)
    {
      setVariation(5);
      Var005();
    }

    if ((allVariations || variationsToRun_.contains("6")) &&
        runMode_ != ATTENDED)
    {
      setVariation(6);
      Var006();
    }

    // Disconnect from the AS/400
    try
    {
      systemObject_.disconnectService(AS400.COMMAND);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      output_.println("Could not disconnect from the 400.");
      return;
    }
  }

  void cleanup( CommandCall cmd )
  {
    try
    {
        deleteLibrary(cmd,cmd_lib_name);
    }
    catch (Exception e) {}
  }

  private static String getTextDescription(ObjectDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
  }


/**
  *Create a valid command call object with a command.  Verify getCommand.
  <i>Taken from:</i> CmdConstructor.java::Var004
  **/
  public void Var001()
  {
    try
    {
        String cmdStr = cmd_string;
        CommandCall cmd = new CommandCall(systemObject_, cmdStr);
        String str = cmd.getCommand();
        if (str == cmdStr)
        {
            succeeded();
        }
        else
        {
            failed("Command not set");
        }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

/**
  *Create a command call object with a command, run it,
  *and verify that the right message was returned.
  <i>Taken from:</i> CmdRunTestcase.java::Var001
  **/
  public void Var002()
  {
    CommandCall cmd = new CommandCall(systemObject_, cmd_crt_lib);
    try
    {
      if (cmd.run()==false)
      {
          failed("command failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText());
      }
      else
      {
        AS400Message[] msglist = cmd.getMessageList();
        if (msglist.length == 1)
        {
            // System.out.println(msglist[0].getID().equals("CPC2102"));
            // System.out.println(msglist[0].getType()==1);
            // System.out.println(msglist[0].getFileName().trim().equals("QCPFMSG"));
            // System.out.println(msglist[0].getLibraryName().trim().startsWith("QSYS"));
            // System.out.println(msglist[0].getSeverity()==0);
            // System.out.println((msglist[0].getText().indexOf(cmd_lib_name) > -1));
            // System.out.println(msglist[0].getText());
            // System.out.println(cmd_lib_name);

            String msgid = msglist[0].getID();
            if (msgid.equals("CPC2102")
                && msglist[0].getType()==1
                && msglist[0].getFileName().trim().equals("QCPFMSG")
                && msglist[0].getLibraryName().trim().startsWith("QSYS")
                && msglist[0].getSeverity()==0
                && (msglist[0].getText().indexOf(cmd_lib_name) > -1)
               )
            {
                if (!cmd.getCommand().equals(cmd_crt_lib))
                {
                    failed("command reset");
                }
                else if (cmd.getSystem() != systemObject_)
                {
                    failed("system reset");
                }
                else
                  {
                  ObjectDescription objDesc = new ObjectDescription(systemObject_, cmd_path_name);
                  String libDesc = getTextDescription(objDesc).trim();
                  if (libDesc.equals(cmd_library_desc))
                  {
                    succeeded();
                  }
                  else
                  {
                    String failMsg = "Object description failure.\n";
                    failMsg += "  Original: "+cmd_library_desc+".\n";
                    failMsg += "  Returned: "+libDesc+".\n";
                    failed(failMsg);
                  }
                }
            }
            else
            {
                failed("unexpected message: " + msgid);
            }
        }
        else
        {
            failed("more than 1 message received");
        }
      }
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
    finally
    {
        cleanup( cmd );
    }
  }

/**
  *Create a command call object, run it with a command,
  *and verify that the right message was returned.
  <i>Taken from:</i> CmdRunTestcase.java::Var003
  **/
  public void Var003()
  {
    try
    {
        CommandCall cmd = new CommandCall(systemObject_);
        if (cmd.run(cmd_crt_lib)==false)
        {
            failed("command failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText());
            return;
        }
        AS400Message[] msglist = cmd.getMessageList();
        if (msglist.length == 1)
        {
            String msgid = msglist[0].getID();
            if (msgid.compareTo("CPC2102") == 0)
            {
                if (!cmd.getCommand().equals(cmd_crt_lib))
                {
                    failed("command reset");
                }
                else if (cmd.getSystem() != systemObject_)
                {
                    failed("system reset");
                }
                else
                  {
                  ObjectDescription objDesc = new ObjectDescription(systemObject_, cmd_path_name);
                  String libDesc = getTextDescription(objDesc).trim();
                  if (libDesc.equals(cmd_library_desc))
                  {
                    succeeded();
                  }
                  else
                  {
                    String failMsg = "Object description failure.\n";
                    failMsg += "  Original: "+cmd_library_desc+".\n";
                    failMsg += "  Returned: "+libDesc+".\n";
                    failed(failMsg);
                  }
                }
            }
            else
            {
                failed("unexpected message: " + msgid);
            }
        }
        else
        {
            failed("more than 1 message received");
        }

        cleanup( cmd );
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }

// Test setCommand

/**
  *Create a command call object,
  *set the command,
  *run it,
  *and verify that the right message was returned.
  <i>Taken from:</i> CmdRunTestcase.java::Var008
  **/
  public void Var004()
  {
    try
    {
        CommandCall cmd = new CommandCall(systemObject_);
        cmd.setCommand(cmd_crt_lib);
        if (cmd.run()==false)
        {
            failed("command failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText());
            return;
        }
        AS400Message[] msglist = cmd.getMessageList();
        if (msglist.length == 1)
        {
            String msgid = msglist[0].getID();
            if (msgid.compareTo("CPC2102") == 0)
            {
                if (!cmd.getCommand().equals(cmd_crt_lib))
                {
                    failed("command reset");
                }
                else if (cmd.getSystem() != systemObject_)
                {
                    failed("system reset");
                }
                else
                  {
                  ObjectDescription objDesc = new ObjectDescription(systemObject_, cmd_path_name);
                  String libDesc = getTextDescription(objDesc).trim();
                  if (libDesc.equals(cmd_library_desc))
                  {
                    succeeded();
                  }
                  else
                  {
                    String failMsg = "Object description failure.\n";
                    failMsg += "  Original: "+cmd_library_desc+".\n";
                    failMsg += "  Returned: "+libDesc+".\n";
                    failed(failMsg);
                  }
                  }
            }
            else
            {
                failed("unexpected message: " + msgid);
            }
        }
        else
        {
            failed("more than 1 message received");
        }

        cleanup( cmd );
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception");
    }
  }


/**
  *Create a command call object that creates an IFS directory
  *with NLS characters in the pathname.
  **/
  public void Var005()
  {
    StringBuffer failMsg = new StringBuffer();
    String baseDir = "/QOpenSys/NLSTest";
    String extDir = baseDir+"/"+cmd_string;
    // Create base directory
    try
    {
      CommandCall cmd = new CommandCall(systemObject_,
                                        "mkdir dir('"+baseDir+"')");
      cmd.run();
    }
    catch(Exception e)
    {
      failMsg.append("Failed to setup directory.\n");
    }

    try
    {
        CommandCall cmd = new CommandCall(systemObject_,
                                          "mkdir dir('"+extDir+"')");
        if (cmd.run()==false)
        {
            failMsg.append("command failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
    }
    catch (Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception.\n");
    }

    // Cleanup
    try
    {
      CommandCall cmd = new CommandCall(systemObject_,
                                        "rmdir dir('"+extDir+"')");
      cmd.run();
    }
    catch(Exception e) {}
    try
    {
      CommandCall cmd = new CommandCall(systemObject_,
                                        "rmdir dir('"+baseDir+"')");
      cmd.run();
    }
    catch(Exception e) {}

    if (failMsg.toString().length() != 0)
      failed("IFS directory creation failed.\n"+failMsg.toString());
    else
      succeeded();
  }


/**
  *Run several commands using the same CommandCall object.
  <i>Taken from:</i> CmdRunTestcase.java::Var008
  **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();
    String baseDir = "/QOpenSys/NLSTest";
    String extDir = baseDir+"/"+cmd_string;
    try
    {
        CommandCall cmd = new CommandCall(systemObject_);
        cmd.setCommand("mkdir dir('" + baseDir + "')");
        if (cmd.run()==false)
        {
            failMsg.append("command1 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 1 of 7 completed.");

        cmd.setCommand("mkdir dir('" + extDir + "')");
        if (cmd.run()==false)
        {
            failMsg.append("command2 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 2 of 7 completed.");

        cmd.setCommand("rmdir dir('" + extDir + "')");
        if (cmd.run()==false)
        {
            failMsg.append("command3 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 3 of 7 completed.");

        cmd.setCommand("rmdir dir('" + baseDir + "')");
        if (cmd.run()==false)
        {
            failMsg.append("command4 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 4 of 7 completed.");

        cmd.setCommand("mkdir dir('blah')");
        if (cmd.run()==false)
        {
            failMsg.append("command5 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 5 of 7 completed.");

        cmd.setCommand("rmdir dir('blah')");
        if (cmd.run()==false)
        {
            failMsg.append("command6 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 6 of 7 completed.");

        cmd.setCommand(cmd_crt_lib);
        if (cmd.run()==false)
        {
            failMsg.append("command7 failed: "+cmd.getMessageList()[0].getID()+" "+cmd.getMessageList()[0].getText()+"\n");
        }
        System.out.println("Command 7 of 7 completed.");

        AS400Message[] msglist = cmd.getMessageList();
        if (msglist.length == 1)
        {
            String msgid = msglist[0].getID();
            if (msgid.compareTo("CPC2102") == 0)
            {
                if (!cmd.getCommand().equals(cmd_crt_lib))
                {
                    failMsg.append("command reset.\n");
                }
                else if (cmd.getSystem() != systemObject_)
                {
                    failMsg.append("system reset.\n");
                }
                else
                  {
                  ObjectDescription objDesc = new ObjectDescription(systemObject_, cmd_path_name);
                  String libDesc = getTextDescription(objDesc).trim();
                  if (!libDesc.equals(cmd_library_desc))
                  {
                    failMsg.append("Object description failure.\n");
                    failMsg.append("  Original: "+cmd_library_desc+".\n");
                    failMsg.append("  Returned: "+libDesc+".\n");
                  }
                  }
            }
            else
            {
                failMsg.append("unexpected message: " + msgid + ".\n");
            }
        }
        else
        {
            failMsg.append("more than 1 message received.\n");
        }

        cleanup( cmd );
    }
    catch (Exception e)
    {
        e.printStackTrace(output_);
        failMsg.append("Unexpected exception.\n");
    }

    if (failMsg.toString().length() != 0)
      failed("Multiple commands failed.\n"+failMsg.toString());
    else
      succeeded();
  }
}



