///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSpecificAttributeAtoCTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.User;
import com.ibm.as400.resource.ResourceMetaData;

import test.Testcase;

import com.ibm.as400.resource.RUser;

/**
 Testcase UserSpecificAttributeAtoCTestcase.  This tests the following attributes of the RUser class:
 <ul>
 <li>ACCOUNTING_CODE 
 <li>ALLOW_SYNCHRONIZATION
 <li>ASSISTANCE_LEVEL
 <li>ATTENTION_KEY_HANDLING_PROGRAM
 <li>BUILDING
 <li>CC_MAIL_ADDRESS
 <li>CC_MAIL_COMMENT
 <li>CHARACTER_CODE_SET_ID
 <li>CHARACTER_IDENTIFIER_CONTROL
 <li>COMPANY
 <li>COUNTRY_ID
 <li>CURRENT_LIBRARY_NAME
 </ul>
 **/
@SuppressWarnings("deprecation")
public class UserSpecificAttributeAtoCTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "UserSpecificAttributeAtoCTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.UserTest.main(newArgs); 
   }
    private UserSandbox sandbox_;
    private String user_;

    /**
     Performs setup needed before running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void setup() throws Exception
    {
        sandbox_ = new UserSandbox(pwrSys_, "USAAC");
        user_ = sandbox_.createUser(true);
    }

    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        sandbox_.cleanup();
    }

    /**
     ACCOUNTING_CODE - Check the attribute meta data in the entire list.
     **/
    public void Var001()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ACCOUNTING_CODE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ACCOUNTING_CODE - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var002()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.ACCOUNTING_CODE);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ACCOUNTING_CODE, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ACCOUNTING_CODE - Get the attribute value without setting it first.
     **/
    public void Var003()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.ACCOUNTING_CODE);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ACCOUNTING_CODE - Set and get the attribute value.
     **/
    public void Var004()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ACCOUNTING_CODE, "Idaho");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ACCOUNTING_CODE);
            assertCondition(((String)value).equalsIgnoreCase("Idaho"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ACCOUNTING_CODE - Set and get the attribute value to an empty string.
     **/
    public void Var005()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ACCOUNTING_CODE, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ACCOUNTING_CODE);
            assertCondition(((String)value).equalsIgnoreCase(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ACCOUNTING_CODE - Set the attribute value to be too long.
     **/
    public void Var006()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ACCOUNTING_CODE, "Longer than 15 Y");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     ACCOUNTING_CODE - Get the attribute value with the backwards compatibility method.
     **/
    public void Var007()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ACCOUNTING_CODE, "Salt Lake City");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getAccountingCode();
            assertCondition(((String)value).equalsIgnoreCase("Salt Lake City"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ALLOW_SYNCHRONIZATION - Check the attribute meta data in the entire list.
     **/
    public void Var008()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ALLOW_SYNCHRONIZATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ALLOW_SYNCHRONIZATION - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var009()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.ALLOW_SYNCHRONIZATION);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ALLOW_SYNCHRONIZATION, Boolean.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ALLOW_SYNCHRONIZATION - Get the attribute value without setting it first.
     **/
    public void Var010()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.ALLOW_SYNCHRONIZATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ALLOW_SYNCHRONIZATION - Set and get the attribute value to true.
     **/
    public void Var011()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ALLOW_SYNCHRONIZATION, Boolean.TRUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ALLOW_SYNCHRONIZATION);
            assertCondition(((Boolean)value).booleanValue() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ALLOW_SYNCHRONIZATION - Set and get the attribute value to false.
     **/
    public void Var012()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ALLOW_SYNCHRONIZATION, Boolean.FALSE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ALLOW_SYNCHRONIZATION);
            assertCondition(((Boolean)value).booleanValue() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Check the attribute meta data in the entire list.
     **/
    public void Var013()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ASSISTANCE_LEVEL, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var014()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.ASSISTANCE_LEVEL);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ASSISTANCE_LEVEL, String.class, false, 4, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Get the attribute value without setting it first.
     **/
    public void Var015()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Set and get the attribute value to system value.
     **/
    public void Var016()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Set and get the attribute value to basic.
     **/
    public void Var017()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(((String)value).equals(RUser.ASSISTANCE_LEVEL_BASIC));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Set and get the attribute value to intermediate.
     **/
    public void Var018()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_INTERMEDIATE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(((String)value).equals(RUser.ASSISTANCE_LEVEL_INTERMEDIATE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Set and get the attribute value to advanced.
     **/
    public void Var019()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_ADVANCED);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ASSISTANCE_LEVEL);
            assertCondition(((String)value).equals(RUser.ASSISTANCE_LEVEL_ADVANCED));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ASSISTANCE_LEVEL - Set the attribute value to be a bogus string.
     **/
    public void Var020()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, "bogus");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     ASSISTANCE_LEVEL - Get the attribute value with the backward compatibility method.
     **/
    public void Var021()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ASSISTANCE_LEVEL, RUser.ASSISTANCE_LEVEL_BASIC);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getAssistanceLevel();
            assertCondition(value.equals(RUser.ASSISTANCE_LEVEL_BASIC));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Check the attribute meta data in the entire list.
     **/
    public void Var022()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ATTENTION_KEY_HANDLING_PROGRAM, String.class, false, 3, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var023()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.ATTENTION_KEY_HANDLING_PROGRAM, String.class, false, 3, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Get the attribute value without setting it first.
     **/
    public void Var024()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set and get the attribute value to system value.
     **/
    public void Var025()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set and get the attribute value to none.
     **/
    public void Var026()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, RUser.NONE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals(RUser.NONE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set and get the attribute value to assist.
     **/
    public void Var027()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, RUser.ATTENTION_KEY_HANDLING_PROGRAM_ASSIST);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals("/QSYS.LIB/QEZMAIN.PGM"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set and get the attribute value to a valid program .
     **/
    public void Var028()
    {
        try
        {
            String programName = "/QSYS.LIB/QWTCHGJB.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, programName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set the attribute value to be a valid program name that does not exist.
     **/
    public void Var029()
    {
        try
        {
            String programName = "/QSYS.LIB/NOTEXIST.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, programName);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM);
            assertCondition(((String)value).equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set the attribute value to be a invalid program name.
     **/
    public void Var030()
    {
        try
        {
            String programName = "//////";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, programName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Set the attribute value to be the empty string.
     **/
    public void Var031()
    {
        try
        {
            String programName = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, programName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     ATTENTION_KEY_HANDLING_PROGRAM - Get the attribute value with the backward compatibility method.
     **/
    public void Var032()
    {
        try
        {
            String programName = "/QSYS.LIB/QCMD.PGM";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.ATTENTION_KEY_HANDLING_PROGRAM, programName);
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getAttentionKeyHandlingProgram();
            assertCondition(value.equals(programName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Check the attribute meta data in the entire list.
     **/
    public void Var033()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.BUILDING, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var034()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.BUILDING);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.BUILDING, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Get the attribute value without setting it first.
     **/
    public void Var035()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.BUILDING);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Set and get the attribute value.
     **/
    public void Var036()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.BUILDING, "New Mexico");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.BUILDING);
            assertCondition(((String)value).equals("New Mexico"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Set and get the attribute value to an empty string.
     **/
    public void Var037()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.BUILDING, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.BUILDING);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Set and get the attribute value to *NONE.
     **/
    public void Var038()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.BUILDING, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.BUILDING);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     BUILDING - Set the attribute value to be too long.
     **/
    public void Var039()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.BUILDING, "Longer than 20 charss");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CC_MAIL_ADDRESS - Check the attribute meta data in the entire list.
     **/
    public void Var040()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CC_MAIL_ADDRESS, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var041()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.CC_MAIL_ADDRESS);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CC_MAIL_ADDRESS, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Get the attribute value without setting it first.
     **/
    public void Var042()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.CC_MAIL_ADDRESS);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Set and get the attribute value.
     **/
    public void Var043()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_ADDRESS, "Arizona");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_ADDRESS);
            assertCondition(((String)value).equalsIgnoreCase("Arizona"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Set and get the attribute value to an empty string.
     **/
    public void Var044()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_ADDRESS, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_ADDRESS);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Set and get the attribute value to *NONE.
     **/
    public void Var045()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_ADDRESS, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_ADDRESS);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_ADDRESS - Set the attribute value to be too long.
     **/
    public void Var046()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_ADDRESS, "Longer than 255 characters - This is a really long sentence -This first part has 100 characters hereABCDEFGHIJKLMNOPQRSTUVWXYZZxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxffglkgkTHis line should have 56 characters, 1 over the limit---");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CC_MAIL_COMMENT - Check the attribute meta data in the entire list.
     **/
    public void Var047()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CC_MAIL_COMMENT , String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var048()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.CC_MAIL_COMMENT);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CC_MAIL_COMMENT , String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Get the attribute value without setting it first.
     **/
    public void Var049()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.CC_MAIL_COMMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Set and get the attribute value.
     **/
    public void Var050()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_COMMENT , "Texas");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_COMMENT);
            assertCondition(((String)value).equalsIgnoreCase("Texas"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Set and get the attribute value to an empty string.
     **/
    public void Var051()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_COMMENT , "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_COMMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Set and get the attribute value to *NONE.
     **/
    public void Var052()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_COMMENT , "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CC_MAIL_COMMENT);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CC_MAIL_COMMENT - Set the attribute value to be too long.
     **/
    public void Var053()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CC_MAIL_COMMENT , "Longer than 126 characters - This is a really long sentence -This first part has 100 characters hereABCDEFGHIJKLMNOPQRSTUVWXYZZ");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Check the attribute meta data in the entire list.
     **/
    public void Var054()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CHARACTER_CODE_SET_ID , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var055()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.CHARACTER_CODE_SET_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CHARACTER_CODE_SET_ID , Integer.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Get the attribute value without setting it first.
     **/
    public void Var056()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.CHARACTER_CODE_SET_ID);
            assertCondition(((Integer)value).intValue() == -2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Set and get the attribute value.
     **/
    public void Var057()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(37));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CHARACTER_CODE_SET_ID);
            assertCondition(((Integer)value).intValue() == 37);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Set and get the attribute value to 0.
     **/
    public void Var058()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(0));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Set and get the attribute value to -1.
     **/
    public void Var059()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(-1));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Set and get the attribute value to 65535 (the maximum).
     **/
    public void Var060()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(65535));
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CHARACTER_CODE_SET_ID);
            assertCondition(((Integer)value).intValue() == 65535);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Set and get the attribute value to 65536 (too high).
     **/
    public void Var061()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(65536));
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CHARACTER_CODE_SET_ID - Get the attribute value with the backward compatibilty method.
     **/
    public void Var062()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_CODE_SET_ID , new Integer(37));
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            int value = u2.getCCSID();
            assertCondition(value == 37);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Check the attribute meta data in the entire list.
     **/
    public void Var063()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CHARACTER_IDENTIFIER_CONTROL, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var064()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.CHARACTER_IDENTIFIER_CONTROL);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CHARACTER_IDENTIFIER_CONTROL, String.class, false, 3, null, true, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Get the attribute value without setting it first.
     **/
    public void Var065()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Set and get the attribute value to system value.
     **/
    public void Var066()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Set and get the attribute value to CHARACTER_IDENTIFIER_CONTROL_DEVICE_DESCRIPTION.
     **/
    public void Var067()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL, RUser.CHARACTER_IDENTIFIER_CONTROL_DEVICE_DESCRIPTION);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL);
            assertCondition(((String)value).equals(RUser.CHARACTER_IDENTIFIER_CONTROL_DEVICE_DESCRIPTION));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Set and get the attribute value to CHARACTER_IDENTIFIER_CONTROL_JOB_CCSID.
     **/
    public void Var068()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL, RUser.CHARACTER_IDENTIFIER_CONTROL_JOB_CCSID);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL);
            assertCondition(((String)value).equals(RUser.CHARACTER_IDENTIFIER_CONTROL_JOB_CCSID));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Set the attribute value to be a bogus string.
     **/
    public void Var069()
    {
        try
        {
            String programName = "bogus";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL, programName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     CHARACTER_IDENTIFIER_CONTROL - Set the attribute value to be the empty string.
     **/
    public void Var070()
    {
        try
        {
            String programName = "";

            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CHARACTER_IDENTIFIER_CONTROL, programName);
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.IllegalArgumentException");
        }
    }

    /**
     COMPANY - Check the attribute meta data in the entire list.
     **/
    public void Var071()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.COMPANY, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var072()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.COMPANY);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.COMPANY, String.class, false, 0, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Get the attribute value without setting it first.
     **/
    public void Var073()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.COMPANY);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Set and get the attribute value.
     **/
    public void Var074()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COMPANY, "Oklahoma City");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.COMPANY);
            assertCondition(((String)value).equals("Oklahoma City"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Set and get the attribute value to an empty string.
     **/
    public void Var075()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COMPANY, "");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.COMPANY);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Set and get the attribute value to *NONE.
     **/
    public void Var076()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COMPANY, "*NONE");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.COMPANY);
            assertCondition(((String)value).equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COMPANY - Set the attribute value to be too long.
     **/
    public void Var077()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COMPANY, "Longer than 50 charsacters is this sentence yes it is");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     COUNTRY_ID - Check the attribute meta data in the entire list.
     **/
    public void Var078()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.COUNTRY_ID, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var079()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.COUNTRY_ID);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.COUNTRY_ID, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Get the attribute value without setting it first.
     **/
    public void Var080()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.COUNTRY_ID);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Set and get the attribute value to system value.
     **/
    public void Var081()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, RUser.SYSTEM_VALUE);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.COUNTRY_ID);
            assertCondition(((String)value).equals(RUser.SYSTEM_VALUE));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Set and get the attribute value to a valid country.
     **/
    public void Var082()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "GB");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.COUNTRY_ID);
            assertCondition(((String)value).equals("GB"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Set the attribute value to be a country that does not exist.
     **/
    public void Var083()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "JT");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     COUNTRY_ID - Set the attribute value to be a invalid country name with no spaces.
     **/
    public void Var084()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "ThisCountryNameIsWayTooLong");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     COUNTRY_ID - Set the attribute value to be a invalid country name with spaces.
     **/
    public void Var085()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "This Country Name Is Way Too Long");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     COUNTRY_ID - Set the attribute value to be the empty string.  Let it go.
     **/
    public void Var086()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "");
            u.commitAttributeChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     COUNTRY_ID - Get the attribute value with the backward compatibility method.
     **/
    public void Var087()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.COUNTRY_ID, "FR");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getCountryID();
            assertCondition(value.equals("FR"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Check the attribute meta data in the entire list.
     **/
    public void Var088()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData[] amd = u.getAttributeMetaData();
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CURRENT_LIBRARY_NAME, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Check the attribute meta data when retrieved for only this attribute.
     **/
    public void Var089()
    {
        try
        {
            RUser u = new RUser();
            ResourceMetaData amd = u.getAttributeMetaData(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(UserGenericAttributeTestcase.verifyAttributeMetaData(amd, RUser.CURRENT_LIBRARY_NAME, String.class, false, 1, null, false, false));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Get the attribute value without setting it first.
     **/
    public void Var090()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            Object value = u.getAttributeValue(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(((String)value).equals(RUser.CURRENT_LIBRARY_NAME_DEFAULT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set and get the attribute value to default.
     **/
    public void Var091()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, RUser.CURRENT_LIBRARY_NAME_DEFAULT);
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(((String)value).equals(RUser.CURRENT_LIBRARY_NAME_DEFAULT));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set and get the attribute value to a valid library.
     **/
    public void Var092()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "QGPL");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(((String)value).equals("QGPL"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set and get the attribute value to a valid library, specified in lower case.
     **/
    public void Var093()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "qiWs");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(((String)value).equals("QIWS"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set the attribute value to be a library that does not exist.
     **/
    public void Var094()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "NOTEXIST");
            u.commitAttributeChanges();

            RUser u2 = new RUser(pwrSys_, user_);
            Object value = u2.getAttributeValue(RUser.CURRENT_LIBRARY_NAME);
            assertCondition(((String)value).equals("NOTEXIST"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set the attribute value to be a invalid library name with no spaces.
     **/
    public void Var095()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "INVALIDLIBRARY");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set the attribute value to be a invalid library name with spaces.
     **/
    public void Var096()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "This Library Name Is Way Too Long");
            u.commitAttributeChanges();
            failed("Didn't throw exception");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.resource.ResourceException");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Set the attribute value to be the empty string.  Let it go.
     **/
    public void Var097()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "");
            u.commitAttributeChanges();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }

    /**
     CURRENT_LIBRARY_NAME - Get the attribute value with the backward compatibility method.
     **/
    public void Var098()
    {
        try
        {
            RUser u = new RUser(pwrSys_, user_);
            u.setAttributeValue(RUser.CURRENT_LIBRARY_NAME, "QIWS");
            u.commitAttributeChanges();

            User u2 = new User(pwrSys_, user_);
            String value = u2.getCurrentLibraryName();
            assertCondition(value.equals("QIWS"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
    }
}
