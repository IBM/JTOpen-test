///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  MessageFileTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Message;

import java.util.Date;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.BidiStringType;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.MessageFile;

import test.JDReflectionUtil;
import test.JDTestDriver;
import test.Testcase;

/**
 Test file and directory create/delete operations on UserSpace.
 **/
public class MessageFileTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "MessageFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.MessageTest.main(newArgs); 
   }
    private final static String messageFilePathName = "/QSYS.LIB/QCPFMSG.MSGF";
    private final static int LEFT_TO_RIGHT = BidiStringType.DEFAULT;
    private final static int CCSID_OF_JOB = MessageFile.CCSID_OF_JOB;
    private final static int CCSID_EBCDIC = 500;  // CCSID for "International EBCDIC"
    String testlib; 
    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        testlib = "MFTST"+super.testLib_.substring(super.testLib_.length()-1);
        cmdRun("QSYS/CRTLIB "+testlib+"");
        cmdRun("QSYS/GRTOBJAUT OBJ("+testlib+")      OBJTYPE(*LIB) USER("+userId_ +") ");

        cmdRun("QSYS/CRTMSGF "+testlib+"/"+testlib+" AUT(*EXCLUDE)");
        cmdRun("QSYS/MRGMSGF QSYS/QTCPMSG "+testlib+"/"+testlib+"");
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
	deleteLibrary(""+testlib+"");
    }

    /**
     Ctor(), Ensure that NullPointerException is thrown if the message file path is not set.
     **/
    public void Var001()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, null);
            failed("Exception didn't occur."+aMessageFile);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     Ctor(), Ensure that NullPointerException is thrown if the system is not set.
     **/
    public void Var002()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(null, messageFilePathName);
            failed("Exception didn't occur."+aMessageFile);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Method tested: setSystem().
     Ensure that the System can be set after creating a MessageFile with the default constructor.
     **/
    public void Var003()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();
            AS400 as400 = new AS400();
            aMessageFile.setSystem(as400);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be set after creating a MessageFile with the default constructor.
     **/
    public void Var004()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();
            aMessageFile.setPath(messageFilePathName);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after creating a User Space with the default constructor.
     **/
    public void Var005()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();
            AS400 as400 = new AS400();
            aMessageFile.setSystem(as400);
            // reset system to a different AS400.
            AS400 newAS400 = new AS400();
            aMessageFile.setSystem(newAS400);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be reset after creating a User Space with the default constructor.
     **/
    public void Var006()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();
            aMessageFile.setPath(messageFilePathName);
            // reset the path to a different AS400.
            aMessageFile.setPath("/QSYS.LIB/USTEST.LIB/ABC.MSGF");
            assertCondition(aMessageFile.getPath().equals("/QSYS.LIB/USTEST.LIB/ABC.MSGF"), "Path is incorrect");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after creating a MessageFile object.
     **/
    public void Var007()
    {
        try
        {
            AS400 as400 = new AS400();
            MessageFile aMessageFile = new MessageFile(as400, messageFilePathName);

            // reset system to a different AS400.
            AS400 newAS400 = new AS400();
            aMessageFile.setSystem(newAS400);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path can be reset after creating a MessageFile object.
     **/
    public void Var008()
    {
        try
        {
            AS400 as400 = new AS400();
            MessageFile aMessageFile = new MessageFile(as400, messageFilePathName);

            // reset the path to a different AS400.
            aMessageFile.setPath("/QSYS.LIB/ABC.MSGF");

            assertCondition(aMessageFile.getPath().equals("/QSYS.LIB/ABC.MSGF"), "Path is incorrect");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System can be reset after a AS400 signon.
     **/
    public void Var009()
    {
        try
        {
            // AS400 as400 = new AS400();
            MessageFile aMessageFile = new MessageFile(systemObject_, messageFilePathName);

            // reset system to a different AS400.
            AS400 newAS400 = new AS400();
            aMessageFile.setSystem(newAS400);

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: setSystem()
     Ensure that the System cannot be reset after a connection.
     **/
    public void Var010()
    {
        try
        {
            AS400 as400 = new AS400();
            MessageFile aMessageFile = new MessageFile(systemObject_, messageFilePathName);
            AS400Message m = aMessageFile.getMessage("CPD0170");

            // Reset system to a different AS400.
            AS400 newAS400 = new AS400();
            aMessageFile.setSystem(newAS400);

            failed("Exception did not occur." +as400+" "+m);
            as400.close(); 
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
        }
    }

    /**
     Method tested: setPath()
     Ensure that the Path cannot be reset after a connection.
     **/
    public void Var011()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, messageFilePathName);
            AS400Message m = aMessageFile.getMessage("CPD0170");

            // reset the path to a different AS400.
            aMessageFile.setPath("/QSYS.LIB/ABC.MSGF");
            failed("Exception did not occur."+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_CHANGED);
        }
    }

    /**
     Method tested: ctor()
     Ensure that IllegalPathException is thrown for MessageFile(AS400, path) if an invalid QSYSObjectPathName is specified for the path.
     **/
    public void Var012()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "USOBJECT.USRSPC");
            failed("Exception did not occur."+aMessageFile);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "IllegalPathNameException");
        }
    }

    /**
     Method tested: create()
     Ensure that IOException is thrown if the library does not exist.
     **/
    public void Var013()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/USBADLIB.LIB/ABC.MSGF");
            AS400Message m = aMessageFile.getMessage("CPD0170");
            failed("Exception did not occur."+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2407", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: delete()
     Ensure that NullPointerException is thrown if the message file path is not set during delete.
     **/
    public void Var014()
    {
        try
        {
            // Connect to the AS400.  This connection will be used for all variations.
            MessageFile aMessageFile = new MessageFile();
            aMessageFile.setSystem(systemObject_);

            AS400Message m = aMessageFile.getMessage("CPD0170");
            failed("Exception didn't occur."+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "path: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Method tested: delete()
     Ensure that NullPointerException is thrown if the system is not set during delete.
     **/
    public void Var015()
    {
        try
        {
            // Connect to the AS400.  This connection will be used for all variations.
            MessageFile aMessageFile = new MessageFile();
            aMessageFile.setPath(messageFilePathName);

            AS400Message m = aMessageFile.getMessage("CPD0170");
            failed("Exception didn't occur."+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalStateException", "system: ", ExtendedIllegalStateException.PROPERTY_NOT_SET);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that IOException is thrown if the user does not have authority to message file.
     **/
    public void Var016()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("TCP1A05");

            failed("No exception occurred.  Make sure uid/pwd on command line does not have all authority."+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2411", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message out of a user message file.
     **/
    public void Var017()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("TCP1A73");

            assertCondition(m.getText().equalsIgnoreCase("Internal Object Damaged."), "Wrong message: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getMessage()
     Ensure that getMessage() returns correct data.
     **/
    public void Var018()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            AS400Message m = aMessageFile.getMessage("CAE0031");

            if (m.getText().equalsIgnoreCase("Call to &1 failed due to an error."))
            {
                if (m.getSeverity() == 40)
                {
                    if (m.getID().equalsIgnoreCase("CAE0031"))
                    {
                        if (m.getDefaultReply().equalsIgnoreCase(""))
                        {
                            String helpText =
                              "Cause . . . . . :   The CALL statement to the application indicated failed with an error. " +
                              "Application processing ends. message CAE0021, issued when the application ended, " +
                              "gives the number of the statement causing the error. " +
                              "Recovery  . . . :   Make sure the called application or program exists within your current " +
                              "library list. See any preceding messages for more information on the failure. " +
                              "Contact the application developer to correct the problem.";
                            if (m.getHelp().equalsIgnoreCase(helpText))
                                succeeded();
                            else
                                failed("wrong help: \n" + m.getHelp() + "\n" + helpText);
                        }
                        else
                            failed("wrong default replay: " + m.getDefaultReply());
                    }
                    else
                        failed("wrong ID: " + m.getID());
                }
                else
                    failed("wrong severity:" + m.getSeverity());
            }
            else
                failed("wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getMessage() with substitution text.
     Ensure that getMessage() with null substitution text works.
     **/
    public void Var019()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            AS400Message m = aMessageFile.getMessage("CAE0080", (String) null);

            assertCondition(m.getText().equalsIgnoreCase("&1 not valid hexadecimal data."), "Wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getMessage() with substitution text.
     Ensure that getMessage() with substitution text works.
     **/
    public void Var020()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            AS400Message m = aMessageFile.getMessage("CAE0080", "abcdefg");

            assertCondition(m.getText().equalsIgnoreCase("abcdefg not valid hexadecimal data."), "Wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message w/sub with ID=null throws exception.
     **/
    public void Var021()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage(null);

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "ID");
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message with ID=null throws exception.
     **/
    public void Var022()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage(null, "abcdefg");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "ID");
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message with ID=emptyString throws exception.
     **/
    public void Var023()
    {

        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2419", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message w/ sub with ID=emptyString throws exception.
     **/
    public void Var024()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("", "abcdefg");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2419", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message with ID=notFound throws exception.
     **/
    public void Var025()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("DAW0001");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2419", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message w/ sub with ID=notThere throws exception.
     **/
    public void Var026()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("DAW0001", "abcdefg");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "AS400Exception", "CPF2419", ErrorCompletingRequestException.AS400_ERROR);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message with ID=toLong throws exception.
     **/
    public void Var027()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("tooooLongggg");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "ID (tooooLongggg): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Method tested: getMessage()
     Ensure that can get message w/ sub with ID=toLong throws exception.
     **/
    public void Var028()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("tooooLongggg", "abcdefg");

            failed("no exception thrown"+m);
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "ID (tooooLongggg): ", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Ctor(), Ensure that NullPointerException is thrown if the system is not set.
     **/
    public void Var029()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(null);

            failed("Exception didn't occur."+aMessageFile);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Ctor(), Ensure that ctor with system works.
     **/
    public void Var030()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_);
            aMessageFile.setPath("/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("TCP1A73");
            assertCondition(m.getText().equalsIgnoreCase("Internal Object Damaged."), "Wrong message: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Ctor(), Ensure that getSystem() works.
     **/
    public void Var031()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(pwrSys_);
            aMessageFile.setPath("/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

            AS400Message m = aMessageFile.getMessage("TCP1A73");

            if (m.getText().equalsIgnoreCase("Internal Object Damaged."))
            {
                assertCondition(aMessageFile.getSystem() == pwrSys_, "Wrong system: " + aMessageFile.getSystem());
            }
            else
            {
                failed("Wrong message: " + m.getText());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: get and set helpTextFormatting -- valid values.
     **/
    public void Var032()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();

            if (aMessageFile.getHelpTextFormatting() != MessageFile.NO_FORMATTING)
            {
                failed("Default value wrong.");
                return;
            }

            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            if (aMessageFile.getHelpTextFormatting() != MessageFile.RETURN_FORMATTING_CHARACTERS)
            {
                failed("Could not set 1.");
                return;
            }

            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);
            aMessageFile.setHelpTextFormatting(MessageFile.SUBSTITUTE_FORMATTING_CHARACTERS);

            if (aMessageFile.getHelpTextFormatting() != MessageFile.SUBSTITUTE_FORMATTING_CHARACTERS)
            {
                failed("Could not set 2.");
                return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: get and set helpTextFormatting -- invalid values
     **/
    public void Var033()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile();

            try
            {
                aMessageFile.setHelpTextFormatting(-1);
                failed("No exception: -1.");
                return;
            }
            catch (Exception e)
            {
                if (!exceptionIs(e, "ExtendedIllegalArgumentException"))
                {
                    failed(e, "Wrong exception: -1.");
                    return;
                }
            }

            try
            {
                aMessageFile.setHelpTextFormatting(3);
                failed("No exception: 3.");
                return;
            }
            catch (Exception e)
            {
                if (!exceptionIs(e, "ExtendedIllegalArgumentException"))
                {
                    failed(e, "Wrong exception: 3.");
                    return;
                }
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: helpTextFormatting, leave characters.
     **/
    public void Var034()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680");

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680.");
                return;
            }

            m = aMessageFile.getMessage("CPD0095");

            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &";
            
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &";
            }
            // Just check the first few words.  The message changes from release to release.

//            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText_v7r1 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the Information Center.";

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            m = aMessageFile.getMessage("CPI8E7E");

            helpText = "&N Cause . . . . . :   This status information is reported by the token-ring adapter card when the ring is establishing an active monitor.  This is a normal condition.  The line is still operational. &N Technical description . . . . . . . . :   &P If this condition continues, a token-ring LAN manager may be used to determine the cause and source of this condition. &P &P - Processor Resource ....: &26 &P - Adapter Resource ......: &27 &P - Port Resource .........: &28";

            if (!m.getHelp().equalsIgnoreCase(helpText))
            {
                failed("Wrong help for CPI8E7E: \n" + m.getHelp() + "\n" + helpText);
                return;
            }

            succeeded();

        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: helpTextFormatting, substitute.
     **/
    public void Var035()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.SUBSTITUTE_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680");

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680.");
                return;
            }

            m = aMessageFile.getMessage("CPD0095");

            String helpText =
              "\nCause . . . . . :   The numeric value specified parameter &2, which is defined as type &" ;
            // Just check the first few words.  The message changes from release to release.
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "\nCause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }

//            String helpText =
//              "\nCause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow:" +
//              "\n      -- *INT2: The maximum value is 32767." +
//              "\n      -- *INT4: The maximum value is 2147483647." +
//              "\n      -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object." +
//              "\n      -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits." +
//              "\nRecovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual." ;
//
//            String helpText2 =
//              "\nCause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow:" +
//              " \n       -- *INT2:  The range of values is -32768 to 32767." +
//              " \n       -- *INT4:  The range of values is -2147483648 to 2147483647." +
//              " \n       -- *UINT2: The range of values is 0 to 65535." +
//              " \n       -- *UINT4: The range of values is 0 to 4294967295." +
//              " \n       -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object." +
//              " \n       -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits." +
//              " \n Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual." ;

            // Help changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            m = aMessageFile.getMessage("CPI8E7E");

            helpText =
              "\nCause . . . . . :   This status information is reported by the token-ring adapter card when the ring is establishing an active monitor.  This is a normal condition.  The line is still operational." +
              " \n Technical description . . . . . . . . :  " +
              " \n       If this condition continues, a token-ring LAN manager may be used to determine the cause and source of this condition." +
              " \n      " +
              " \n       - Processor Resource ....: &26" +
              " \n       - Adapter Resource ......: &27" +
              " \n       - Port Resource .........: &28";

            if (!m.getHelp().equalsIgnoreCase(helpText))
            {
                output_.println(m.getHelp().length());
                output_.println(helpText.length());
                String fred = m.getHelp().replace(' ', '$');
                failed("wrong help CPI8E7E: \n" + m.getHelp() + "\n" + fred + "\n" + helpText);
                return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: Replacement Text (byte array).
     **/
    public void Var036()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680", (byte[]) null);

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680");
                return;
            }

            m = aMessageFile.getMessage("CPD0095", (byte[]) null);

            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &";
            // Just check the first few words.  The message changes from release to release.
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }

//            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            byte[] x = new byte[0];

            m = aMessageFile.getMessage("CPD0095", x);

            helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &";
            // Just check the first few words.  The message changes from release to release.
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            CommandCall c = new CommandCall(systemObject_);
            c.run("QSYS/CRTDTAQ DTAQ(LIB/DQ) MAXLEN(1234567)");
            AS400Message msg = c.getMessageList()[0];
            AS400Message m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData());
            String realMsg = m2.getHelp();
            helpText = "&N Cause . . . . . :   The specified value must be greater than or equal to 1 and less than or equal to 64512. &N Recovery  . . . :   Enter a value that is within the range defined for the parameter or one of the special values that is defined for the parameter. More information on parameters and special values can be found in the ";
            // Just check the first few words.  The message changes from release to release.

            if (!realMsg.startsWith(helpText))
            {
                failed("Wrong help for CRTDTAQ: \n[Got:]\n" + realMsg + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            c = new CommandCall(pwrSys_);
            c.run("QSYS/CLROUTQ QEZJOBLOG");
            msg = c.getMessageList()[0];
            m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData());
            realMsg = m2.getHelp();
            String helpText1 = "&N Cause . . . . . :   ";
	    String helpText2 = "entries from the output queue QEZJOBLOG in library QUSRSYS may not have been cleared for one of the following reasons: &P -- The output queue entry represents an open file. &P -- The output queue entry represents a file to which a writer is active. &P -- Job related resources cannot be allocated. &P -- The queue was allocated by another job during the clear and this process was not able to allocate the queue again to finish the clearing operation. &N Recovery  . . . :   Do one of the following and try the request again: &P -- If the file is open, the file must be closed.  Either wait for the file to be closed by the job or end the job (ENDJOB command) that is creating the file. &P -- If the writer is active, the writer could be ended. &P -- If the resources cannot be allocated or the output queue was allocated by another job, either wait for the resources to become available or determine the process that has the resources allocated and end that process to release the resources.";

            if (realMsg.indexOf(helpText1) < 0 || 
		realMsg.indexOf(helpText2) < 0)
            {
                failed("Wrong help: \n"+
		       "Got:      "+ realMsg + "\n" +
		       "Expected: "+helpText1+".."+helpText2+"\n"+
		       "Note:  Some entries may still be open.. The entries must be deleted manually before running this test\n");
                return;
            }

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: getMessage()
     Ensure that getMessage() returns correct data
     **/
    public void Var037()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            AS400Message m = aMessageFile.getMessage("CAE0031", LEFT_TO_RIGHT);

            if (m.getText().equalsIgnoreCase("Call to &1 failed due to an error."))
            {
                if (m.getSeverity() == 40)
                {
                    if (m.getID().equalsIgnoreCase("CAE0031"))
                    {
                        if (m.getDefaultReply().equalsIgnoreCase(""))
                        {
                            String helpText =
                              "Cause . . . . . :   The CALL statement to the application indicated failed with an error. " +
                              "Application processing ends. message CAE0021, issued when the application ended, " +
                              "gives the number of the statement causing the error. " +
                              "Recovery  . . . :   Make sure the called application or program exists within your current " +
                              "library list. See any preceding messages for more information on the failure. " +
                              "Contact the application developer to correct the problem.";
                            if (m.getHelp().equalsIgnoreCase(helpText))
                                succeeded();
                            else
                                failed("wrong help: \n" + m.getHelp() + "\n" + helpText);
                        }
                        else
                            failed("wrong default replay: " + m.getDefaultReply());
                    }
                    else
                        failed("wrong ID: " + m.getID());
                }
                else
                    failed("wrong severity:" + m.getSeverity());
            }
            else
                failed("wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: getMessage() with substitution text.
     Ensure that getMessage() with substitution text works.
     **/
    public void Var038()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            AS400Message m = aMessageFile.getMessage("CAE0080", "abcdefg", LEFT_TO_RIGHT);

            assertCondition(m.getText().equalsIgnoreCase("abcdefg not valid hexadecimal data."), "Wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: Replacement Text (byte array)
     **/
    public void Var039()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680", (byte[]) null, LEFT_TO_RIGHT);

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680.");
                return;
            }

            m = aMessageFile.getMessage("CPD0095", (byte[]) null, LEFT_TO_RIGHT);

            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &";
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }
            // Just check the first few words.  The message changes from release to release.

//            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            byte[] x = new byte[0];

            m = aMessageFile.getMessage("CPD0095", x, LEFT_TO_RIGHT);

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            CommandCall c = new CommandCall(systemObject_);
            c.run("QSYS/CRTDTAQ DTAQ(LIB/DQ) MAXLEN(1234567)");
            AS400Message msg = c.getMessageList()[0];
            AS400Message m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT);
            String realMsg = m2.getHelp();
            helpText = "&N Cause . . . . . :   The specified value must be greater than or equal to 1 and less than or equal to 64512. &N Recovery  . . . :   Enter a value that is within the range defined for the parameter or one of the special values that is defined for the parameter. More information on parameters and special values can be found in the ";
            // Just check the first few words.  The message changes from release to release.

            if (!realMsg.startsWith(helpText))
            {
                failed("Wrong help: \n" + realMsg + "\n" + helpText);
                return;
            }

            c = new CommandCall(pwrSys_);
            c.run("QSYS/CLROUTQ QEZJOBLOG");
            msg = c.getMessageList()[0];
            m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT);
            realMsg = m2.getHelp();
            String helpText1 = "&N Cause . . . . . :   ";
	    String helpText2 = "entries from the output queue QEZJOBLOG in library QUSRSYS may not have been cleared for one of the following reasons: &P -- The output queue entry represents an open file. &P -- The output queue entry represents a file to which a writer is active. &P -- Job related resources cannot be allocated. &P -- The queue was allocated by another job during the clear and this process was not able to allocate the queue again to finish the clearing operation. &N Recovery  . . . :   Do one of the following and try the request again: &P -- If the file is open, the file must be closed.  Either wait for the file to be closed by the job or end the job (ENDJOB command) that is creating the file. &P -- If the writer is active, the writer could be ended. &P -- If the resources cannot be allocated or the output queue was allocated by another job, either wait for the resources to become available or determine the process that has the resources allocated and end that process to release the resources.";

            if (realMsg.indexOf(helpText1) < 0 || 
		realMsg.indexOf(helpText2) < 0)
            {
                failed("Wrong help: \n"+
		       "Got:      "+ realMsg + "\n" +
		       "Expected: "+helpText1+".."+helpText2+"\n"+
		       "Note:  Some entries may still be open.. The entries must be deleted manually before running this test\n");
                return;
            }


            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: substituteFormattingCharacters().
     Ensure that formatting characters get substituted correctly.
     **/
    public void Var040()
    {
        try
        {
            String text = "Now is the time&Bfor all good people&Bto come&Nto the aid&Nof their country,&Pdon't you think?&P";

            String textOut = MessageFile.substituteFormattingCharacters(text);
            assertCondition(textOut.equals("Now is the time\n    for all good people\n    to come\nto the aid\nof their country,\n      don't you think?\n      "), "Formatting characters not substituted correctly: " + textOut);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

//---------------------------------------------------------------------------


    /**
     Method tested: Replacement Text (byte array, ccsid).
     **/
    public void Var041()
    {
        try
        {
            int ccsid = systemObject_.getCcsid();
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680", (byte[]) null);

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680");
                return;
            }

            m = aMessageFile.getMessage("CPD0095", (byte[]) null, LEFT_TO_RIGHT, ccsid, CCSID_OF_JOB);

            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &";
            // Just check the first few words.  The message changes from release to release.

//            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            byte[] x = new byte[0];
            m = aMessageFile.getMessage("CPD0095", x, LEFT_TO_RIGHT, ccsid, CCSID_EBCDIC);

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095 (EBCDIC): \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }


            byte[] substData = CharConverter.stringToByteArray(ccsid, "prm1parameter2parm3prm4parameter5");
            m = aMessageFile.getMessage("CPD0095", substData, LEFT_TO_RIGHT, ccsid, CCSID_OF_JOB);

            helpText  = "&N Cause . . . . . :   The numeric value specified parameter parameter2, which is defined as type parm3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(prm4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter parameter2, which is defined as type parameter5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(prm4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
            String helpText_v7r1 = "&N Cause . . . . . :   The numeric value specified parameter parameter2, which is defined as type parameter5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(prm4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the Information Center.";
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText_v7r1  = "&N Cause . . . . . :   The numeric value specified for parameter parameter2, which is defined as type parameter5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(prm4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &P -- *X:  This error occurred for the CALL or CALLPRC command or for a command parameter defined with type *X where the passed parameter has more than 10 significant integer digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the Information Center.";
            }
            // Message changed in v4r5.
            if (!m.getHelp().equalsIgnoreCase(helpText) &&
                !m.getHelp().equalsIgnoreCase(helpText2) &&
                !m.getHelp().equalsIgnoreCase(helpText_v7r1))
            {
                failed("Wrong help for CPD0095 (Job CCSID): \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText);
                return;
            }



            CommandCall c = new CommandCall(systemObject_);
            c.run("QSYS/CRTDTAQ DTAQ(LIB/DQ) MAXLEN(1234567)");
            AS400Message msg = c.getMessageList()[0];
            AS400Message m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT, ccsid, CCSID_OF_JOB);
            String realMsg = m2.getHelp();
            helpText = "&N Cause . . . . . :   The specified value must be greater than or equal to 1 and less than or equal to 64512. &N Recovery  . . . :   Enter a value that is within the range defined for the parameter or one of the special values that is defined for the parameter. More information on parameters and special values can be found in the ";

            if (!realMsg.startsWith(helpText))
            {
                failed("Wrong help: \n" + realMsg + "\n" + helpText);
                return;
            }

            c = new CommandCall(pwrSys_);
            c.run("QSYS/CLROUTQ QEZJOBLOG");
            msg = c.getMessageList()[0];
            m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT, ccsid, ccsid);
            realMsg = m2.getHelp();
	    String helpText1 = "&N Cause . . . . . :   ";
	    helpText2 = "entries from the output queue QEZJOBLOG in library QUSRSYS may not have been cleared for one of the following reasons: &P -- The output queue entry represents an open file. &P -- The output queue entry represents a file to which a writer is active. &P -- Job related resources cannot be allocated. &P -- The queue was allocated by another job during the clear and this process was not able to allocate the queue again to finish the clearing operation. &N Recovery  . . . :   Do one of the following and try the request again: &P -- If the file is open, the file must be closed.  Either wait for the file to be closed by the job or end the job (ENDJOB command) that is creating the file. &P -- If the writer is active, the writer could be ended. &P -- If the resources cannot be allocated or the output queue was allocated by another job, either wait for the resources to become available or determine the process that has the resources allocated and end that process to release the resources.";

            if (realMsg.indexOf(helpText1) < 0 || 
		realMsg.indexOf(helpText2) < 0)
            {
                failed("Wrong help: \n"+
		       "Got:      "+ realMsg + "\n" +
		       "Expected: "+helpText1+".."+helpText2+"\n"+
		       "Note:  Some entries may still be open.. The entries must be deleted manually before running this test\n");
                return;
            }


            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }

    /**
     Method tested: getMessage(..., ccsid, byte[]) with substitution text.
     Ensure that getMessage() with substitution text works.
     **/
    public void Var042()
    {
        try
        {
            int ccsid = 37;
            byte[] substitutionData = CharConverter.stringToByteArray(ccsid, "abcdefg");
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            AS400Message m = aMessageFile.getMessage("CAE0080", substitutionData, LEFT_TO_RIGHT, ccsid, CCSID_OF_JOB);

            assertCondition(m.getText().equalsIgnoreCase("abcdefg not valid hexadecimal data."), "Wrong message text: " + m.getText());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    /**
     Method tested: Replacement Text (ccsid, byte array)
     **/
    public void Var043()
    {
        try
        {
            int ccsid = systemObject_.getCcsid();
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");
            aMessageFile.setHelpTextFormatting(MessageFile.RETURN_FORMATTING_CHARACTERS);

            AS400Message m = aMessageFile.getMessage("CPIB680", (byte[]) null, LEFT_TO_RIGHT, ccsid, ccsid);

            if (m.getHelp().length() != 0)
            {
                failed("Wrong help: CPIB680.");
                return;
            }

            m = aMessageFile.getMessage("CPD0095", (byte[]) null, LEFT_TO_RIGHT, ccsid, CCSID_EBCDIC);

            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &";
            if (getRelease() > JDTestDriver.RELEASE_V7R5M0) {
              helpText  = "&N Cause . . . . . :   The numeric value specified for parameter &2, which is defined as type &";
            }
            // Just check the first few words.  The message changes from release to release.

//            String helpText  = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &3 in the command definition object, is too large.  The parameter types and their allowed maximum values follow: &P -- *INT2: The maximum value is 32767. &P -- *INT4: The maximum value is 2147483647. &P -- *DEC:  The maximum value for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";
//            String helpText2 = "&N Cause . . . . . :   The numeric value specified parameter &2, which is defined as type &5 in the command definition object, is not valid.  The parameter types and their allowed values follow: &P -- *INT2:  The range of values is -32768 to 32767. &P -- *INT4:  The range of values is -2147483648 to 2147483647. &P -- *UINT2: The range of values is 0 to 65535. &P -- *UINT4: The range of values is 0 to 4294967295. &P -- *DEC:  The range of values for this parameter is limited by its defined length.  It is defined with LEN(&4) in the command definition object. &P -- *N:  This error occurred in an expression with a numeric value with more than 15 significant digits. &N Recovery  . . . :   Enter a numeric value that is valid for the parameter, or enter one of the special values that is defined for the parameter. More information on parameters and special values can be found in the CL Reference manual.";

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help for CPD0095: \n[Got:]\n" + m.getHelp() + "\n[Expected:]\n" + helpText + " ...");
                return;
            }

            byte[] x = new byte[0];

            m = aMessageFile.getMessage("CPD0095", x, LEFT_TO_RIGHT, ccsid, ccsid);

            // Message changed in v4r5.
            if (!m.getHelp().startsWith(helpText))
            {
                failed("Wrong help: \n" + m.getHelp() + "\n" + helpText);
                return;
            }

            CommandCall c = new CommandCall(systemObject_);
            c.run("QSYS/CRTDTAQ DTAQ(LIB/DQ) MAXLEN(1234567)");
            AS400Message msg = c.getMessageList()[0];
            AS400Message m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT, ccsid, ccsid);
            String realMsg = m2.getHelp();
            helpText = "&N Cause . . . . . :   The specified value must be greater than or equal to 1 and less than or equal to 64512. &N Recovery  . . . :   Enter a value that is within the range defined for the parameter or one of the special values that is defined for the parameter. More information on parameters and special values can be found in the ";

            if (!realMsg.startsWith(helpText))
            {
                failed("Wrong help: \n" + realMsg + "\n" + helpText);
                return;
            }

            c = new CommandCall(pwrSys_);
            c.run("QSYS/CLROUTQ QEZJOBLOG");
            msg = c.getMessageList()[0];
            m2  = aMessageFile.getMessage(msg.getID(), msg.getSubstitutionData(), LEFT_TO_RIGHT, ccsid, CCSID_EBCDIC);
            realMsg = m2.getHelp();
            String helpText1 = "&N Cause . . . . . :   ";
	    String helpText2 = "entries from the output queue QEZJOBLOG in library QUSRSYS may not have been cleared for one of the following reasons: &P -- The output queue entry represents an open file. &P -- The output queue entry represents a file to which a writer is active. &P -- Job related resources cannot be allocated. &P -- The queue was allocated by another job during the clear and this process was not able to allocate the queue again to finish the clearing operation. &N Recovery  . . . :   Do one of the following and try the request again: &P -- If the file is open, the file must be closed.  Either wait for the file to be closed by the job or end the job (ENDJOB command) that is creating the file. &P -- If the writer is active, the writer could be ended. &P -- If the resources cannot be allocated or the output queue was allocated by another job, either wait for the resources to become available or determine the process that has the resources allocated and end that process to release the resources.";

            if (realMsg.indexOf(helpText1) < 0 || 
		realMsg.indexOf(helpText2) < 0)
            {
                failed("Wrong help: \n"+
		       "Got:      "+ realMsg + "\n" +
		       "Expected: "+helpText1+".."+helpText2+"\n"+
		       "Note:  Some entries may still be open.. The entries must be deleted manually before running this test\n");
                return;
            }


            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred");
        }
    }


    /**
     Method tested: getMessage(FIRST), getMessage(NEXT).
     Ensure that getMessage() returns correct data.
     **/
    public void Var044()
    {
        try
        {
            MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

            // Get the first message in the message file.

            AS400Message m = aMessageFile.getMessage(MessageFile.FIRST);

            if (m.getID().equals("CAE0002"))
            {
                if (m.getSeverity() == 40)
                {
                  // success
                }
                else
                {
                    failed("FIRST: wrong severity:" + m.getSeverity());
                    return;
                }
            }
            else
            {
                failed("FIRST: wrong ID: " + m.getID());
                return;
            }


            // Get the next message in the message file.

            m = aMessageFile.getMessage(MessageFile.NEXT);

            if (m.getID().equals("CAE0005"))
            {
                if (m.getSeverity() == 30)
                {
                  // success
                }
                else
                {
                    failed("NEXT: wrong severity:" + m.getSeverity());
                    return;
                }
            }
            else
            {
                failed("NEXT: wrong ID: " + m.getID());
                return;
            }


            // Get the next message in the message file.

            m = aMessageFile.getMessage(MessageFile.NEXT);

            if (m.getID().equals("CAE0009"))
            {
                if (m.getSeverity() == 40)
                {
                  // success
                }
                else
                {
                    failed("NEXT2: wrong severity:" + m.getSeverity());
                    return;
                }
            }
            else
            {
                failed("NEXT2: wrong ID: " + m.getID());
                return;
            }

            succeeded();

            
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception occurred.");
        }
    }

    
    /**
    Method tested: getMessage()/getCreateDate()/getModificationDate()
    **/
   public void Var045()
   {
     String added = " -- added 2/11/2011 to test message CreateDate/Modification date"; 
       try
       {
           MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

           AS400Message m = aMessageFile.getMessage("TCP1A73");

           Date nowDate = new Date(); 
           Date createDate = (Date) JDReflectionUtil.callMethod_O(m, "getCreateDate"); 
           //output_.println("createDate is "+createDate); 
           Date modifiedDate = (Date) JDReflectionUtil.callMethod_O(m, "getModificationDate");
           //output_.println("modifiedDate is "+modifiedDate); 
           
           assertCondition(nowDate.compareTo(createDate) > 0 && 
                           nowDate.compareTo(modifiedDate) > 0, 
                           "nowDate("+nowDate+") is not after"+
                           "createDate("+createDate+") and modifiedDate("+modifiedDate+")"+added); 
           
       }
       catch (Exception e)
       {
           failed(e, "Unexpected exception occurred.");
       }
   }

   /**
   Method tested: getMessage()/getCreateDate()/getModificationDate()
   **/
  public void Var046()
  {
    String added = " -- added 2/11/2011 to test message CreateDate/Modification date"; 
      try
      {
        cmdRun("QSYS/CHGMSGD MSGID(TCP1AA0) MSGF("+testlib+"/"+testlib+") SECLVL(DUH)"); 
          
          MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

          AS400Message m = aMessageFile.getMessage("TCP1AA0");

          Date yesterdayDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); 
          
          Date createDate = (Date) JDReflectionUtil.callMethod_O(m, "getCreateDate"); 
          //output_.println("createDate is "+createDate); 
          Date modifiedDate = (Date) JDReflectionUtil.callMethod_O(m, "getModificationDate");
          //output_.println("modifiedDate is "+modifiedDate); 
          
          assertCondition(yesterdayDate.compareTo(createDate) > 0 && 
                          yesterdayDate.compareTo(modifiedDate) < 0, 
                          "yesterdayDat("+yesterdayDate+") is not after"+
                          "createDate("+createDate+") and not before modifiedDate("+modifiedDate+")"+added); 
          
      }
      catch (Exception e)
      {
          failed(e, "Unexpected exception occurred.");
      }
  }

  /**
  Method tested: getMessage()/getCreateDate()/getModificationDate()
  **/
 public void Var047()
 {
   String added = " -- added 2/11/2011 to test message CreateDate/Modification date"; 
     try
     {
         cmdRun("ADDMSGD MSGID(A111112) MSGF("+testlib+"/"+testlib+") MSG('Testing 1') SECLVL('Testing')"); 
         
         MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

         AS400Message m = aMessageFile.getMessage("A111112");

         Date yesterdayDate = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); 
         
         Date createDate = (Date) JDReflectionUtil.callMethod_O(m, "getCreateDate"); 
         //output_.println("createDate is "+createDate); 
         Date modifiedDate = (Date) JDReflectionUtil.callMethod_O(m, "getModificationDate");
         //output_.println("modifiedDate is "+modifiedDate); 
         
         assertCondition(yesterdayDate.compareTo(createDate) < 0 && 
                         yesterdayDate.compareTo(modifiedDate) < 0, 
                         "yesterdayDate("+yesterdayDate+") is not before"+
                         "createDate("+createDate+") and not before modifiedDate("+modifiedDate+")"+added); 
         
     }
     catch (Exception e)
     {
         failed(e, "Unexpected exception occurred.");
     }
 }

 /**
 Method tested: getMessage()/getCreateDate()/setCreateDate()
 **/
public void Var048()
{
  String added = " -- added 2/11/2011 to test message CreateDate/Modification date"; 
    try
    {
        MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

        AS400Message m = aMessageFile.getMessage("TCP1A73");

        Date nowDate = new Date(); 
        Date createDate = (Date) JDReflectionUtil.callMethod_O(m, "getCreateDate"); 
        //output_.println("createDate is "+createDate);
        JDReflectionUtil.callMethod_V(m, "setCreateDate", nowDate); 
        Date changedCreateDate = (Date) JDReflectionUtil.callMethod_O(m, "getCreateDate");
        //output_.println("changedCreateDate is "+changedCreateDate); 
        
        assertCondition(nowDate.compareTo(createDate) > 0 && 
                        nowDate.compareTo(changedCreateDate) == 0, 
                        "nowDate("+nowDate+") is not after"+
                        "createDate("+createDate+") or not equal to changedCreateDate("+changedCreateDate+")"+added); 
        
    }
    catch (Exception e)
    {
        failed(e, "Unexpected exception occurred.");
    }
}

/**
Method tested: getMessage()/getCreateDate()/setCreateDate()
**/
public void Var049()
{
  String added = " -- added 2/11/2011 to test message CreateDate/Modification date"; 
   try
   {
       MessageFile aMessageFile = new MessageFile(pwrSys_, "/QSYS.LIB/"+testlib+".LIB/"+testlib+".MSGF");

       AS400Message m = aMessageFile.getMessage("TCP1A73");

       Date nowDate = new Date(); 
       Date modificationDate = (Date) JDReflectionUtil.callMethod_O(m, "getModificationDate"); 
       //output_.println("modificationDate is "+modificationDate);
       JDReflectionUtil.callMethod_V(m, "setModificationDate", nowDate); 
       Date changedModificationDate = (Date) JDReflectionUtil.callMethod_O(m, "getModificationDate");
       //output_.println("changedmodificationDate is "+changedmodificationDate); 
       
       assertCondition(nowDate.compareTo(modificationDate) > 0 && 
                       nowDate.compareTo(changedModificationDate) == 0, 
                       "nowDate("+nowDate+") is not after"+
                       "modificationDate("+modificationDate+") or not equal to changedModificationDate("+changedModificationDate+")"+added); 
       
   }
   catch (Exception e)
   {
       failed(e, "Unexpected exception occurred.");
   }
}



}
