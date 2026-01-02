///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JobLogTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.Job;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList; 
import com.ibm.as400.access.AS400Bin2; 
import com.ibm.as400.access.AS400Bin4; 
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.JobLog;
import com.ibm.as400.access.QueuedMessage;

import test.JTOpenTestEnvironment;
import test.Testcase;

import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.CharConverter;
import java.io.UnsupportedEncodingException;

/**
 Testcase JobLogTestcase.
 <p>This tests the following JobLog methods:
 <ul>
 <li>ctors
 <li>addPropertyChangeListener
 <li>addVetoableChangeListener
 <li>getLength()
 <li>getMessages()
 <li>getName()
 <li>getNumber()
 <li>getSystem()
 <li>getUser()
 <li>removePropertyChangeListener
 <li>removeVetoableChangeListener
 <li>setName()
 <li>setNumber()
 <li>setSystem()
 <li>setUser()
 </ul>
 **/
public class JobLogTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JobLogTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JobTest.main(newArgs); 
   }
    private static final boolean DEBUG = false;

    private void printErrors(AS400Message[] msgs)
    {
        for (int i = 0; i < msgs.length; ++i)
        {
            output_.println(msgs[i]);
        }
    }

    /**
     Submits the CL program SNDMSGS on the server.
     **/
    private String[] startCLJob()
    {
        try
        {
            CommandCall c = new CommandCall(systemObject_);
            // Make sure the program exists.
            if (!c.run("CHKOBJ OBJ(JOBLOGTEST/SNDMSGS) OBJTYPE(*PGM)") || c.getMessageList().length > 0)
            {
                output_.println("Program JOBLOGTEST/SNDMSGS does not exist or user");
                output_.println(systemObject_.getUserId() + " does not have authority to it.");
                output_.println();
                output_.println("Run program SetupDriver specifying SetupJobLog for the -tc parameter.");
                return null;
            }

            if (!c.run("SBMJOB CMD(CALL PGM(JOBLOGTEST/SNDMSGS)) JOB(JOBLOGTEST) JOBQ(QSYS/QSYSNOMAX) LOGCLPGM(*NO)"))
            {
                printErrors(c.getMessageList());
                return null;
            }
            AS400Message[] msgs = c.getMessageList();
            // Extract the job name, number and user.
            String msg = msgs[0].getText();
            int index = msg.indexOf(" SUBMITTED");
            if (index < 0)
            {
                index = msg.indexOf(" submitted");
                if (index < 0)
                {
                    return null;
                }
            }
            StringTokenizer st = new StringTokenizer(msg.substring(4, index), "/");
            int count = 0;
            String[] s = new String[3];
            while (st.hasMoreTokens())
            {
                s[count++] = st.nextToken();
		//output_.println("startCLJob:: string["+count+"] = " +s[count]);
            }
            if (count == 3)
            {
                // Wait a bit before returning so the job can get off and running.
                Thread.sleep(15000);
                return s;
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace(output_);
            return null;
        }
    }

    /** 
     Validate getReplyStatus
     **/
    private boolean validateGetReplyStatus( Integer caseToTest, QueuedMessage m  )
    {
      if ( m == null ) return false;
      if( caseToTest.intValue() == 0 )
      {
         if (m.getReplyStatus().length() == 0) 
         {
             output_.println("Message 0:");
             output_.println("ReplyStatus is empty: " + m.getReplyStatus());
             return false;
         }
      }
      if(  caseToTest.intValue() == 1 )
      {
         if (m.getReplyStatus().compareTo( "N" ) != 0) 
         {
             output_.println("Message 0:");
             output_.println("ReplyStatus is incorrect: " + m.getReplyStatus());
             return false;
         }
      }
      return true;
    } 
    
    /** 
     Validate getLibraryName  
     **/
    private boolean validateGetLibraryName( Integer caseToTest, QueuedMessage m  )
    {
      if ( m == null ) return false;
      
      if( caseToTest.intValue() == 0 )
      {
         if (m.getLibraryName().length() == 0) 
         {
             output_.println("Message 0:");
             output_.println("Library Name is empty: " + m.getLibraryName());
             return false;
         }
      }
      
      if(  caseToTest.intValue() == 1 )
      {
         if (m.getLibraryName().compareTo( "QSHELL" ) != 0) 
         {
             output_.println("Message 0:");
             output_.println("Library Name is incorrect: " + m.getLibraryName());
             return false;
         }
      }
      return true;
    } 

    
    /** 
     Validate getProgramName  
     **/
     private boolean validateGetProgramName( Integer caseToTest, QueuedMessage m  )
     {
       return true;
     }

    /** Yank 27
    // Validate 
     **/
     private boolean validateGetText( Integer caseToTest, QueuedMessage m  )
     {
       if ( m == null ) return false;

       if( caseToTest.intValue() == 0)
       {
          if ( ( ( String ) m.getText()).length() == 0 ) 
          {
              output_.println("Message 0:");
              output_.println("GetText incorrect: " + m.getText());
              return false;
          }
       }	  
       if( caseToTest.intValue() == 1)
       {
          if ( !( ( String ) m.getText()).startsWith("Descriptor ") ||
               !( ( String ) m.getText()).endsWith(" not allocated in job."))
          {
              output_.println("Message 0:");
              output_.println("GetText incorrect: " + m.getText());
              return false;
          }
       }
       if(  caseToTest.intValue() == 2 )
       {
          if (m.getText().compareTo( "*NO" ) != 0) 
          {
              output_.println("Message 0:");
              output_.println("GetTextis incorrect: " + m.getText());
              return false;
          }
       }
       return true;
    }

    
    /** 
    // Validate getDefaultReply
     **/
     private boolean validateGetDefaultReply( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
    	 
	 if( caseToTest.intValue() == 0 )
    	 {
    	     if (((String)m.getDefaultReply()).length() != 0 ) 
             {
                  output_.println("Message 0:");
                  output_.println("DefaultReply is null: " + m.getDefaultReply());
              return false;
             }
        }
    	if(  caseToTest.intValue() == 1 )
    	{
            if (m.getDefaultReply().compareTo( "" ) != 0) 
            {
                output_.println("Message 0:");
                output_.println("DefaultReply is incorrect: " + m.getDefaultReply());
                return false;
            }
        }
    	return true;
    }

    /**
     Validate getSubstitutionData
     **/
     private boolean validateGetSubstitutionData( Integer caseToTest, QueuedMessage m  ) throws UnsupportedEncodingException
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //SubstitutionData should not be set
	    if (m.getSubstitutionData() != null) 
            {
                output_.println("Message 0:");
                output_.println("SubstitutionData is not null: " + m.getSubstitutionData());
                return false;
            }
         }

         // Test when set to "mySubstText"
	 if( caseToTest.intValue() == 1 )
	 {
	    //SubstitutionData should be set
            byte[] substData = m.getSubstitutionData();

            
	    if (substData != null) 
	    {
                String stringSubData =  ((String)CharConverter.byteArrayToString(systemObject_, substData)).trim();
	        if (stringSubData.compareTo( "mySubstText" ) != 0) 
                {
                    output_.println("Message 0:");
                    output_.println("SubstitutionData is not correct: " + stringSubData);
                    return false;
                }
	    }
	    else return false;
         }
	 return true;
     }
    
    
    /** 
    // Validate getHelp
     **/
     private boolean validateGetHelp( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
    	 if( caseToTest.intValue() == 0 )
    	 {
    	     if (((String) m.getHelp()).length() != 0 ) 
             {
                 output_.println("Message 0:");
                 output_.println("Help is null: " + m.getHelp());
                 return false;
             }
         }
    	 if(  caseToTest.intValue() == 1 )
    	 {
            if ((( ( String )m.getHelp() ).substring( 46, 236)).compareTo( "is not allocated in the job.  QSH requires that descriptor 0 is allocated for standard input, descriptor 1 is allocated for standard output, and descriptor 2 is allocated for standard error." ) != 0)
            {
                output_.println("Message 0:");
                output_.println("Help is incorrect: " + m.getHelp());
                return false;
            }
    	 }
    	 return true;
    }

    /**
     Validate GetAlertOption
     **/
     private boolean validateGetAlertOption( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //AlertOption should be set
	    if (m.getAlertOption().length() == 0) 
            {
                output_.println("Message 0:");
                output_.println("AlertOption is null: " + m.getAlertOption());
                return false;
            }
         }
	 if(  caseToTest.intValue() == 1 )
	 {
	    //AlertOption should be set
	    if (m.getAlertOption().compareTo( "*NO" ) != 0) 
            {
                output_.println("Message 0:");
                output_.println("AlertOption is incorrect: " + m.getAlertOption());
                return false;
            }

	 }
	 return true;
     }

    /**
     Validate getNumber_of_sending_statements
     **/
     private boolean validateGetNumberOfSendingStatements ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //Number_of_sending_statements should be null 
	    if ( (( String[] )m.getSendingStatementNumbers()).length != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfSendingStatements not null: " + m.getSendingStatementNumbers()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    // Checking to make sure data came back
	    String NumberOfSendingStatments [] = m.getSendingStatementNumbers();
	    if (NumberOfSendingStatments == null )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfSendingStatements is null: "); 
                return false;
	    }
            
	    //Length should be 1
	    if (NumberOfSendingStatments.length != 1 )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfSendingStatements does not have length of 1: "+ NumberOfSendingStatments.length); 
                return false;
	    }

            //Data should be either 156 or 158


         String value = ((String)NumberOfSendingStatments[0]).trim();
	    if ( !(value.equals("156") || value.equals("158")) )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfSendingStatements is incorrect :" + NumberOfSendingStatments[0] ); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getRequestStatus
     **/
     private boolean validateGetRequestStatus ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //RequestStatus should be null
	    if (m.getRequestStatus().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("RequestStatus not null: " + m.getRequestStatus()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getReceivingProcedureName
     **/
     private boolean validateGetReceivingProcedureName ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //ReceivingProcedureName should be null
	    if (m.getReceivingProcedureName().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingProcedureName not null: " + m.getReceivingProcedureName()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //ReceivingProcedureName should be null
	    if (m.getReceivingProcedureName().compareTo("CallProgram") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingProcedureName is incorrect: " + m.getReceivingProcedureName()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getReceivingModuleName
     **/
     private boolean validateGetReceivingModuleName ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //ReceivingModuleName should be null
	    if (m.getReceivingModuleName().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingModuleName not null: " + m.getReceivingModuleName()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //ReceivingModuleName should be null
	    if (m.getReceivingModuleName().compareTo("QZRCRPC") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingModuleName is incorrect: " + m.getReceivingModuleName()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getReceivingProgramName
     **/
     private boolean validateGetReceivingProgramName ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //ReceivingProgramName should be null
	    if (m.getReceivingProgramName().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingProgramName not null: " + m.getReceivingProgramName()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //ReceivingProgramName should be null
	    if (m.getReceivingProgramName().compareTo("QZRCSRVS") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingProgramName is incorrect: " + m.getReceivingProgramName()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getReceivingType
     **/
     private boolean validateGetReceivingType ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //ReceivingType should be null 
	    if (m.getReceivingType().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingType not null: " + m.getReceivingType()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //ReceivingType should be null 
	    if (m.getReceivingType().compareTo("1") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("ReceivingType is incorrect: " + m.getReceivingType()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getSendingUserProfile
     **/
     private boolean validateGetSendingUserProfile ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //SendingUserProfile
	    if (m.getSendingUserProfile().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingUserProfile not null: " + m.getSendingUserProfile()); 
                return false;
	    }
         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //SendingUserProfile
	    String expectedProfile = userId_.toUpperCase(); 
	    if (m.getSendingUserProfile().compareTo(expectedProfile) != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingUserProfile is incorrect: " + m.getSendingUserProfile()+" expected "+expectedProfile); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getSendingProcedureName
     **/
     private boolean validateGetSendingProcedureName( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //SendingProcedureName should be null
	    if (m.getSendingProcedureName().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingProcedureName not null: " + m.getSendingProcedureName()); 
                return false;
	    }

         }
	 if( caseToTest.intValue() == 1 )
	 {
	    //SendingProcedureName should be null
	    if (m.getSendingProcedureName().compareTo("CallProgram") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingProcedureName is incorrect: " + m.getSendingProcedureName()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getSendingModuleName
     **/
     private boolean validateGetSendingModuleName( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //SendingModuleName should be null
	    if (m.getSendingModuleName().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingModuleName not null: " + m.getSendingModuleName()); 
                return false;
	    }
         }
	 
	 // Check for QZRCRPC
	 if( caseToTest.intValue() == 1 )
	 {
	    //SendingModuleName should be null
	    if (m.getSendingModuleName().compareTo( "QZRCRPC" ) != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SendingModuleName is incorrect: " + m.getSendingModuleName()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getSenderType
     **/
     private boolean validateGetSenderType( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //SenderType should be null
	    if (m.getSenderType().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SenderType is incorrect: " + m.getSenderType()); 
                return false;
	    }
         }
	 
	 //Checking for 
	 if( caseToTest.intValue() == 1 )
	 {
	    //SenderType should be null
	    if (m.getSenderType().compareTo( "1" )  != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("SenderType not null: " + m.getSenderType()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getMessageHelpReplacementandFormat
     **/
     private boolean validateGetMessageHelpReplacementandFormat( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //MessageHelpReplacementandFormat should be null
	    if (m.getMessageHelpReplacementandFormat().length() != 0 ) 
	    {
                output_.println("Message 0:");
	        output_.println("MessageHelpReplacementandFormat not null: " + m.getMessageHelpReplacementandFormat()); 
                return false;
	    }
	 }   
	 if( caseToTest.intValue() == 1 )
	 {
	    // Checking to see it data is returned 
	    if (m.getMessageHelpReplacementandFormat().length() == 0 ) 
	    {
                output_.println("Message 0:");
	        output_.println("MessageHelpReplacementandFormat is null: " + m.getMessageHelpReplacementandFormat()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getMessageHelpReplacement
     **/
     private boolean validateGetMessageHelpReplacement ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    //MessageHelpReplacement should be null
	    if (m.getMessageHelpReplacement().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("MessageHelpReplacement not null: " + m.getMessageHelpReplacement()); 
                return false;
	    }
	 }
        
	 // Just checking to see it there is data returned.
	 if( caseToTest.intValue() == 1 )
	 {
	    //MessageHelpReplacement should be null
	    if (m.getMessageHelpReplacement().length() == 0 )
	    {
                output_.println("Message 0:");
	        output_.println("MessageHelpReplacement is null: " + m.getMessageHelpReplacement()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getMessageHelp
     **/
     private boolean validateGetHelpMessage ( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    // HelpMessage should be null 
	    if (m.getMessageHelp().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("HelpMessage not null: " + m.getMessageHelp()); 
                return false;
	    }
         }

	 if( caseToTest.intValue() == 1 )
	 {
	    // HelpMessage should be null 
	    if (m.getMessageHelp().compareTo("Cause . . . . . :   Descriptor &1 is not allocated in the job.  QSH requires that descriptor 0 is allocated for standard input, descriptor 1 is allocated for standard output, and descriptor 2 is allocated for standard error. Recovery  . . . :   Allocate the required descriptors and start QSH again.") != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("HelpMessage incorrect: " + m.getMessageHelp()); 
                return false;
	    }
         }
	 return true;
     }

    /**
     Validate getMessage
     **/
     private boolean validateGetMessage( Integer caseToTest, QueuedMessage m  )
     {
         if ( m == null ) return false;
	 
	 // Test default value 
	 if( caseToTest.intValue() == 0 )
	 {
	    // Message should be null
	    if (m.getMessage().length() != 0 )
	    {
                output_.println("Message 0:");
	        output_.println("Message not null: " + m.getMessage());
                return false;
	    }

         }
	 
	 //Test for ":Library &1 not found."
	 if( caseToTest.intValue() == 1 )
	 {
	    // Message should be null
	    if (!m.getMessage().startsWith("Descriptor ") ||
	        !m.getMessage().endsWith(" not allocated in job."))
	    {
                output_.println("Message 0:");
	        output_.println("Message incorrect: " + m.getMessage());
                return false;
	    }

         }



	 return true;
     }

    /**
     Validate getNumber_of_receiver_statements
     **/
    private boolean validateGetNumberOfReceiverStatements( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    //Number_of_receiver_statements should null 
	    if ( ( ( String[] ) m.getReceiverStatementNumbers()).length != 0 ) 
	    {
                output_.println("Message 0:");
	        output_.println("Number_of_receiver_statements not null: " + m.getReceiverStatementNumbers()); 
                return false;
	    }
	}
	if( caseToTest.intValue() == 1 )
	{
	    //Numberofreceiverstatements should not null 
	    String NumberOfReceiverStatements [] = m.getReceiverStatementNumbers();
	    if (NumberOfReceiverStatements == null ) 
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfReceiverStatements is null: " + m.getReceiverStatementNumbers()); 
                return false;
	    }
	    
	    //Length should be 1
	    if (NumberOfReceiverStatements.length != 1 )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfReceiverStatements does not have length of 1: "+ NumberOfReceiverStatements.length); 
                return false;
	    }

            //Data should be either 156 or 158
         String value = ((String)NumberOfReceiverStatements[0]).trim();
	    if ( !(value.equals("156") || value.equals("158")) )
	    {
                output_.println("Message 0:");
	        output_.println("NumberOfReceiverStatements is incorrect :" + NumberOfReceiverStatements[0] ); 
                return false;
	    }
	}
     	return true;
    }

    /**
     Validate 
     **/
    private boolean validateGetRequestLevel( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    //should null 
	    if (m.getRequestLevel() != null ) 
	    {
                output_.println("Message 0:");
	        output_.println("RequestLevel not null: " + m.getRequestLevel()); 
                return false;
	    }
	}
	if( caseToTest.intValue() == 1 )
	{
	    if ( ( (Integer) m.getRequestLevel()).intValue() != 0 ) 
	    {
                output_.println("Message 0:");
	        output_.println("RequestLevel is null: " + m.getRequestLevel()); 
                return false;
	    }
	}
     	return true;
    }
    
    /**
     Validate 
     **/
    private boolean validateGetCcsidForText( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    if ( m.getCcsidCodedCharacterSetIdentifierForText() != null ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidForText not null: " + m.getCcsidCodedCharacterSetIdentifierForText()); 
                return false;
	    }
	}
	if( caseToTest.intValue() == 1 )
	{
	    if ( ( (Integer) m.getCcsidCodedCharacterSetIdentifierForText()).intValue() != 65535 )
	    {
                output_.println("Message 0:");
	        output_.println("CcsidForText is incorrect: " + ( (Integer) m.getCcsidCodedCharacterSetIdentifierForText()).intValue()); 
                return false;
	    }
	}
     	return true;
    }
    /**
     Validate 
     **/
    private boolean validateGetCcsidConversionStatusText( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    //should null 
	    if (m.getCcsidConversionStatusIndicatorForText() != null ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidConversionStatusText not null: " + m.getCcsidConversionStatusIndicatorForText()); 
                return false;
	    }
	}
	if( caseToTest.intValue() == 1 )
	{
	    //
	    if ( ( (Integer) m.getCcsidConversionStatusIndicatorForText()) .intValue() != 1 ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidConversionStatusText is null: " + (( Integer )m.getCcsidConversionStatusIndicatorForText()).intValue()); 
                return false;
	    }
	}
     	return true;
    }

    /**
     Validate 
     **/
    private boolean validateGetCcsidForData( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    //should null 
	    if (m.getCcsidCodedCharacterSetIdentifierForData() != null ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidForData not null: " + m.getCcsidCodedCharacterSetIdentifierForData()); 
                return false;
	    }
	}

	if( caseToTest.intValue() == 1 )
	{
	    //
	    if ( (( Integer )m.getCcsidCodedCharacterSetIdentifierForData()).intValue() != 65535 ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidForData is null: " + (( Integer )m.getCcsidCodedCharacterSetIdentifierForData()).intValue()); 
                return false;
	    }
	}
     	return true;
    }
    /**
     Validate 
     **/
    private boolean validateGetCcsidConversionStatusData( Integer caseToTest, QueuedMessage m  )
    { 
        if ( m == null ) return false;
	    
	// Test default value 
	if( caseToTest.intValue() == 0 )
	{
	    //should null 
	    if (m.getCcsidconversionStatusIndicatorForData() != null ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidConversionStatusData not null: " + ((Integer)m.getCcsidconversionStatusIndicatorForData()).intValue()); 
                return false;
	    }
	}
	if( caseToTest.intValue() == 1 )
	{
	    //
	    if ( ((Integer)m.getCcsidconversionStatusIndicatorForData()).intValue() != 2 ) 
	    {
                output_.println("Message 0:");
	        output_.println("CcsidConversionStatusData is incorrect: " + ((Integer)m.getCcsidconversionStatusIndicatorForData()).intValue()); 
                return false;
	    }
	}
     	return true;
    }

    private boolean validateJobMessages( QueuedMessage message, Vector<Integer> keys ) 
    {
        // Check the input value 
        if ( message == null  || keys.isEmpty() ) 
	{
	    output_.println("message == null  || keys.isEmpty()" );
	    return false;
        }

	Enumeration<Integer> e = keys.elements(); 
        
	while ( e.hasMoreElements() ) 
	{ 
	    Integer field = (Integer)e.nextElement();
            switch ( field.intValue() )
	    {
                case 101:   
		    if( !validateGetAlertOption((Integer)e.nextElement(), message ) ) 
		        return false;
	        break;  
                case 201: 
		    try {
		        if( !validateGetSubstitutionData((Integer)e.nextElement(), message ) ) 
		            return false;
		    }
		    catch ( UnsupportedEncodingException ex ) { } 
                break;
	        case 301: 
		    if( !validateGetMessage((Integer)e.nextElement(), message ) ) 
		        return false;
                break;
                case 302:
		    if( !validateGetText((Integer)e.nextElement(), message ) ) 
		        return false;
                break;
                case 401: 
		    if( !validateGetHelpMessage((Integer)e.nextElement(), message ) ) 
		        return false;
                break;
	        case 402:
		    if( !validateGetMessageHelpReplacement((Integer)e.nextElement(), message ) ) 
		        return false;
	        break; 
	        case 403:
		    if( !validateGetMessageHelpReplacementandFormat((Integer)e.nextElement(), message ) ) 
		        return false;
	        break; 
                case 404:
		    if( !validateGetHelp((Integer)e.nextElement(), message ) ) 
		        return false;
                break;
	        case 501:
		    if( !validateGetDefaultReply((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 602:
		    if( !validateGetSenderType((Integer)e.nextElement(), message ) ) 
		        return false;
	        break; 
	        case 603: 
		    if( !validateGetProgramName((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 604:
		    if( !validateGetSendingModuleName((Integer)e.nextElement(), message ) ) 
		        return false;
	        break; 
	        case 605:
		    if( !validateGetSendingProcedureName((Integer)e.nextElement(), message ) ) 
		        return false;
	        break; 
	        case 606: 
		    if( !validateGetNumberOfSendingStatements((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 607:
		    if( !validateGetSendingUserProfile((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 702:
		    if( !validateGetReceivingType((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 703:
		    if( !validateGetReceivingProgramName((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 704:
		    if( !validateGetReceivingModuleName((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 705:
		    if( !validateGetReceivingProcedureName((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 706:
		    if( !validateGetNumberOfReceiverStatements((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
                case 801: 
		    if( !validateGetLibraryName((Integer)e.nextElement(), message ) ) 
		        return false;
                break;
	        case 1001:
		    if( !validateGetReplyStatus((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
	        case 1101:
		    if( !validateGetRequestStatus((Integer)e.nextElement(), message ) ) 
		        return false;
	        break;
		case 1201:
		    if( !validateGetRequestLevel((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
		case 1301:
		    if( !validateGetCcsidForText((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
		case 1302:
		    if( !validateGetCcsidConversionStatusText((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
		case 1303:
		    if( !validateGetCcsidForData((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
		case 1304:
		    if( !validateGetCcsidConversionStatusData((Integer)e.nextElement(), message ) ) 
		        return false;
		break;
                default:
	            return false;
	    }
	}
	return true; 
    }
    /**
     Verifies the default valid field identifiers.
     **/
    boolean verifyJobMessageDefaultsSet( QueuedMessage m )
    {
      if ( m == null) 
        return false;
      else return true;
    }
 
    /**
     Verifies the messages returned from getMessages() for the submitted job that called SNDMSGS.
     **/
    private boolean verifyCLJobMessages(Enumeration<QueuedMessage> enumeration, String[] job)
    {
        boolean passed = true; 
        try
        {
            int count = 0;
            while (enumeration.hasMoreElements())
            {
                QueuedMessage m = (QueuedMessage)enumeration.nextElement();
                
		// Check to see it the name is null
		if (m.getFromJobName().length() != 0)
                {
                    output_.println("Message["+count+"] " + String.valueOf(count) + ":");
                    output_.println("From job name not null: " + m.getFromJobName() + "...");
                    passed = false; 
                }

		//Check to see if the number is null
                if (m.getFromJobNumber().length() != 0)
                {
                    output_.println("Message["+count+"] " + String.valueOf(count) + ":");
                    output_.println("From job number not null: " + m.getFromJobNumber());
                    passed = false; 
                }

		// Key should be null
                if (m.getKey() == null)
                {
                    output_.println("Message["+count+"] " + String.valueOf(count) + ":");
                    output_.println("Key is null: ");
                    passed =  false;
                }
                  
                // Reply status should be null 
                if (!m.getReplyStatus().equals("N"))
                {
                    output_.println("Message["+count+"] " + String.valueOf(count) + ":");
                    output_.println("Reply status not null: " + m.getReplyStatus());
                    passed = false;
                }

		//User should be null
                if (m.getUser().length() != 0)
                {
                    output_.println("Message["+count+"] " + String.valueOf(count) + ":");
                    output_.println("User not null: " + m.getUser());
                    passed = false;
                }
                String pgm1 = "SNDMSGS";
                String pgm2 = "QWTSCSBJ";
                String pgm3 = "QWTPIIPP";
                String msgTxt1 = "New level of CSP/AE required for application.";
                String msgTxt2 = "Just an old-fashioned message";
                String msgTxt3 = "CALL PGM(JOBLOGTEST/SNDMSGS)";
                String msgTxt4 = "Job " + job[0] + "/" + job[1] + "/" + job[2] + " submitted.";
                String msgId1 = "CAE0002";
                String msgId2 = "";
                String msgId3 = "";
                String msgId4 = "CPI1125";
                
		switch(count)
                {
                    case 4:
                        if (!m.getFromProgram().equals(pgm1))
                        {
                            output_.println("Message["+count+"] :");
                            output_.println("From program incorrect: " + m.getFromProgram());
                            output_.println(" > Expected: " + pgm1);
                            passed =  false;
                        }
                        if (!m.getText().equalsIgnoreCase(msgTxt1))
                        {
                            output_.println("Message["+count+"] :");
                            output_.println("Message text incorrect: " + m.getText());
                            output_.println(" > Expected: " + msgTxt1);
                            passed = false;
                        }
                        if (!m.getID().equals(msgId1))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("Message id incorrect: " + m.getID());
                            output_.println(" > Expected: " + msgId1);
                            passed = false;
                        }
                        break;
                    case 3:
                        if (!m.getFromProgram().equals(pgm1))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("From program incorrect: " + m.getFromProgram());
                            output_.println(" > Expected: " + pgm1);
                            passed = false; 
                        }
                        if (!m.getText().equalsIgnoreCase(msgTxt2))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("Message text incorrect: " + m.getText());
                            output_.println(" > Expected: " + msgTxt2);
                            passed = false;
                        }
                        if (!m.getID().equals(msgId2))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("Message id incorrect: " + m.getID());
                            output_.println(" > Expected: " + msgId2);
                            passed = false;
                        }
                        break;
                    case 2:
                        if (!m.getFromProgram().equals(pgm2))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("From program incorrect: " + m.getFromProgram());
                            output_.println(" > Expected: " + pgm2);
                            passed = false;
                        }
                        if (!m.getText().equalsIgnoreCase(msgTxt3))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("Message text incorrect: " + m.getText());
                            output_.println(" > Expected: " + msgTxt3);
                            passed = false;
                        }
                        if (!m.getID().equals(msgId3))
                        {
                            output_.println("Message " + String.valueOf(count) + ":");
                            output_.println("Message id incorrect: " + m.getID());
                            output_.println(" > Expected: " + msgId3);
                            passed = false;
                        }
                        break;
                        
                         case 999993:   /* old code -- case 3  */ 
                         if (!m.getFromProgram().equals(pgm3))
                         {
                         output_.println("Message " + String.valueOf(count) + ":");
                         output_.println("From program incorrect: " + m.getFromProgram());
                         passed = false;
                         }
                         if (!m.getText().equalsIgnoreCase(msgTxt4))
                         {
                         output_.println("Message " + String.valueOf(count) + ":");
                         output_.println("Message text incorrect: " + m.getText());
                         passed = false;
                         }
                         if (!m.getID().equals(msgId4))
                         {
                         output_.println("Message " + String.valueOf(count) + ":");
                         output_.println("Message id incorrect: " + m.getID());
                         passed = false;
                         }
                         break;
                         
                }
                count++;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(output_);
            return false;
        }
        return passed;
    }

    /**
     ctor - Empty ctor should have default properties.
     **/
    public void Var001()
    {
        try
        {
            JobLog j = new JobLog();
            if (DEBUG)
            {
                output_.println("Name:   " + j.getName());
                output_.println("Number: " + j.getNumber());
                output_.println("System: " + j.getSystem());
                output_.println("User:   " + j.getUser());
            }
            assertCondition(j.getName().equals("*") && 
	                    j.getNumber().equals("") && 
			    j.getSystem() == null && 
			    j.getUser().equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     ctor - Passing null for system should throw an exception.
     **/
    public void Var002()
    {
        try
        {
            JobLog j = new JobLog(null);
            failed("Did not throw exception for "+j);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     ctor - Passing null for system should throw an exception.
     **/
    public void Var003()
    {
        try
        {
            JobLog j = new JobLog(null, "name", "user", "number");
            failed("Did not throw exception for "+j);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     ctor() - Passing a valid system should set the system.
     **/
    public void Var004()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            assertCondition(j.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     ctor() - Passing a valid system should set the system.
     **/
    public void Var005()
    {
        try
        {
            JobLog j = new JobLog(systemObject_, "name", "user", "number");
            assertCondition(j.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     ctor - Passing null for name should throw an exception.
     **/
    public void Var006()
    {
        try
        {
            JobLog j = new JobLog(systemObject_, null, "user", "number");
            failed("Did not throw exception."+j);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "name");
        }
    }

    /**
     ctor - Passing null for user should throw an exception.
     **/
    public void Var007()
    {
        try
        {
            JobLog j = new JobLog(systemObject_, "name", null, "number");
            failed("Did not throw exception."+j);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "user");
        }
    }

    /**
     ctor - Passing null for number should throw an exception.
     **/
    public void Var008()
    {
        try
        {
            JobLog j = new JobLog(systemObject_, "name", "user", null);
            failed("Did not throw exception."+j);
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "number");
        }
    }

    /**
     ctor() - Passing a valid name should set the name.
     **/
    public void Var009()
    {
        try
        {
            String theName = "name";
            JobLog j = new JobLog(systemObject_, theName, "user", "number");
            assertCondition(j.getName().equals(theName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     ctor() - Passing a valid user should set the user.
     **/
    public void Var010()
    {
        try
        {
            String theUser = "user";
            JobLog j = new JobLog(systemObject_, "name", theUser, "number");
            assertCondition(j.getUser().equals(theUser));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     ctor() - Passing a valid number should set the number.
     **/
    public void Var011()
    {
        try
        {
            String theNumber = "number";
            JobLog j = new JobLog(systemObject_, "name", "user", theNumber);
            assertCondition(j.getNumber().equals(theNumber));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getLength() - Should return 0 when no properties have been set.
     **/
    public void Var012()
    {
        try
        {
            JobLog j = new JobLog();
            j.getLength();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     getLength() - Should return 0 if getMessages has not been called.
     **/
    public void Var013()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            assertCondition(j.getLength() != 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getName() - Should return the name property that was set.
     **/
    public void Var014()
    {
        try
        {
            String theName = "name";
            JobLog j = new JobLog(systemObject_);
            j.setName(theName);
            assertCondition(j.getName().equals(theName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getName() - Should return a blank.  The name property has not been set.
     **/
    public void Var015()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            assertCondition(j.getName().equals("*"));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getNumber() - Should return the number property.
     **/
    public void Var016()
    {
        try
        {
            String theNumber = "1234";
            JobLog j = new JobLog(systemObject_);
            j.setNumber(theNumber);
            assertCondition(j.getNumber().equals(theNumber));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getUser() - Should return blank.  The user has not been set.
     **/
    public void Var017()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            assertCondition(j.getUser().equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getUser() - Should return the name.
     **/
    public void Var018()
    {
        try
        {
            String theName = "MadDogJohnson";
            JobLog j = new JobLog(systemObject_);
            j.setName(theName);
            assertCondition(j.getName().equals(theName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getSystem() - Should return null.  The system has not been set.
     **/
    public void Var019()
    {
        try
        {
            JobLog j = new JobLog();
            assertCondition(j.getSystem() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getSystem() - Should return the system.
     **/
    public void Var020()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            assertCondition(j.getSystem() == systemObject_);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     getMessages() - Should throw an exception.  The system is not set.
     **/
    public void Var021()
    {
        try
        {
            JobLog j = new JobLog();
            j.getMessages();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     getMessages() - Should throw an exception.  The job name is not set.
     **/
    public void Var022()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.getMessages();
            succeeded();  // This is now allowed... it gets the current job log.
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     getMessages() - Should throw an exception. The job number is not set.
     **/
    public void Var023()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setName("BLAH");
            j.setUser("aUser");
            j.getMessages();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     getMessages() - Should throw an exception.  The user is not set.
     **/
    public void Var024()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setName("BLAH");
            j.setNumber("000000");
            j.getMessages();
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     getMessages() - Should return the job's messages.
     **/
    public void Var025()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }
        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1], job[0]);
            if (DEBUG)
            {
                output_.println("Name = " + job[2]);
                output_.println("User = " + job[1]);
                output_.println("Number = " + job[0]);
            }

            Enumeration<QueuedMessage> e = j.getMessages();
            assertCondition(verifyCLJobMessages(e, job));
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
    }

    /**
     setName() - Verify that passing null causes an exception.
     **/
    public void Var026()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setName(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "name");
        }
    }

    /**
     setName() - When the name was not previously set, this should set the selection.
     **/
    public void Var027()
    {
        try
        {
            String theName = "Juicy Juice";
            JobLog j = new JobLog(systemObject_);
            j.setName(theName);
            assertCondition(j.getName().equals(theName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setName() - When the name was previously set, this should set the name to a new value.
     **/
    public void Var028()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setName("oldName");
            String theName = "The New Name";
            j.setName(theName);
            assertCondition(j.getName().equals(theName));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setNumber() - Verify that passing null causes an exception.
     **/
    public void Var029()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setNumber(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "number");
        }
    }

    /**
     setNumber() - When the number was not previously set, this should set the number.
     **/
    public void Var030()
    {
        try
        {
            JobLog j = new JobLog();
            String theNumber = "1122334";
            j.setNumber(theNumber);
            assertCondition(j.getNumber().equals(theNumber));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setNumber() - When the number was previously set, this should set the number to a new value.
     **/
    public void Var031()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setNumber("121212");
            String theNumber = "213245";
            j.setNumber(theNumber);
            assertCondition(j.getNumber().equals(theNumber));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setSystem() - Verify that passing null causes an exception.
     **/
    public void Var032()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setSystem(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     setSystem() - When the system was not previously set, this should set the system.
     **/
    public void Var033()
    {
        try
        {
            JobLog j = new JobLog();
            AS400 system2 = new AS400();
            j.setSystem(system2);
            assertCondition(j.getSystem() == system2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setSystem() - When the system was previously set, this should set the system to a new value.
     **/
    public void Var034()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            AS400 system2 = new AS400();
            j.setSystem(system2);
            assertCondition(j.getSystem() == system2);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setUser() - Verify that passing null causes an exception.
     **/
    public void Var035()
    {
        try
        {
            JobLog j = new JobLog(systemObject_);
            j.setUser(null);
            failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "user");
        }
    }

    /**
     setUser() - When the user was not previously set, this should set the user.
     **/
    public void Var036()
    {
        try
        {
            String theUser = "Fred Sanford";
            JobLog j = new JobLog(systemObject_);
            j.setUser(theUser);
            assertCondition(j.getUser().equals(theUser));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     setUser() - When the user was previously set, this should set the user to a new value.
     **/
    public void Var037()
    {
        try
        {
            String theUser = "John Madrigal";
            JobLog j = new JobLog(systemObject_);
            j.setUser("Old User");
            j.setUser(theUser);
            assertCondition(j.getUser().equals(theUser));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     job name is lower case.
     **/
    public void Var038()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }
        try
        {
            JobLog j = new JobLog(systemObject_, job[2].toLowerCase(), job[1], job[0]);

            Enumeration<QueuedMessage> e = j.getMessages();

            if (e.hasMoreElements())
            {
                assertCondition(verifyCLJobMessages(e, job));
            }
            else
            {
                failed("job not found");
            }
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
    }

    /**
     user name is lower case
     **/
    public void Var039()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }
        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);

            Enumeration<QueuedMessage> e = j.getMessages();

            if (e.hasMoreElements())
            {
                assertCondition(verifyCLJobMessages(e, job));
            }
            else
            {
                failed("job not found");
            }
        }
        catch (Exception e)
        {
            failed(e, "Exception occurred.");
        }
    }

    private final boolean isRunningNativelyAndThreadsafe()
    {
      String prop = getProgramCallThreadSafetyProperty(); // we only care about ProgramCall, not CommandCall
      if (JTOpenTestEnvironment.isOS400 && isNative_ && isLocal_ &&
          prop != null && prop.equals("true"))
      {
        return true;
      }
      else return false;
    }

    private final boolean usingNativeOptimizations()
    {
      if (!isRunningNativelyAndThreadsafe()) return false;

      else if (JTOpenTestEnvironment.isOS400 && isNative_ && isLocal_ )
      {
        return true;
      }

      else if (pwrSys_.canUseNativeOptimizations()) {
        return true;
      }

      else return false;
    }

    /**
     Verifies that the message id was written to the joblog, does not verify that the message ID was valid.
     If possible, pass the list of jobs to search.  Otherwise, all QZRCSRVS jobs will be search, possibly filling
     the pwrSys_ job with extra messages. 
     **/
    private boolean writeMessageSuccessful(String id, StringBuffer sb, Job[] jobs)
    {
        if (pwrSys_ == null)
        {
	    sb.append("writeMessageSuccessful:  FAILED:  Power user not specified.\n");
            output_.println("Power user not specified.");
            return false;
          }
          try {
            JobList jobList = new JobList(pwrSys_);
            if (usingNativeOptimizations()) {
              // When running on-thread, it's a bit more difficult to determine the name of
              // the job we're running in.
              output_.println("Running on-thread, so assuming the write message was successful.");
              return true;
            }
            Enumeration<?> list = null; 
            if (jobs == null) { 
              jobList.setName("QZRCSRVS");
              jobList.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
              list = jobList.getJobs();
            }
            boolean looping; 
            int i = 0; 
            if (list != null) { 
              looping = list.hasMoreElements(); 
            } else {
              looping = i < jobs.length; 
            }
            while (looping) {
              Job j;
              if (list != null) {
                j = (Job) list.nextElement();
              } else {
                j = jobs[i];
              }
              String compStatus = "NOTFOUND";
              try {
                compStatus = j.getCompletionStatus();
              } catch (Exception e) {
                // Ignore the job if it cannot be found
              }
              if (compStatus.equals(Job.COMPLETION_STATUS_NOT_COMPLETED)) {
                String lastId = "";
                JobLog jq = new JobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
                sb.append("Looking at job " + j.getName() + "." + j.getUser() + "." + j.getNumber() + "\n");
                Enumeration<?> e = jq.getMessages();
                while (e.hasMoreElements()) {
                  QueuedMessage message = (QueuedMessage) e.nextElement();
                  String thisId = message.getID();
                  if (!lastId.equals(thisId)) {
                    sb.append(" messageId=" + thisId + "\n");
                    lastId = thisId;
                  }
                  if (message.getID().equals(id.toUpperCase()))
                    return true;
                }
              }
              i++;
              if (list != null) { 
                looping = list.hasMoreElements(); 
              } else {
                looping = i < jobs.length; 
              }
              
            } /* looping */

          } catch (Exception e) {
            output_.println("Unexpected exception on writeMessageSuccessful(" + id + "): ");
            e.printStackTrace(output_);
        }
	sb.append("writeMessageSuccessful:  FAILED:  Did not find message "+id+" in jobs.\n");
        return false;
    }

    /**
     Sends a message of type *COMP.
     **/
    public void Var040()
    {
	StringBuffer sb = new StringBuffer(); 
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            String id = "CAE0009";
            JobLog.writeMessage(pwrSys_, id, AS400Message.COMPLETION);
	    boolean passed = writeMessageSuccessful(id,sb,pwrSys_.getJobs(AS400.COMMAND));
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a message of type *DIAG with null for substitutionText.
     **/
    public void Var041()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            String id = "CPFACDF";
            JobLog.writeMessage(pwrSys_, id, AS400Message.DIAGNOSTIC, (byte[])null);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,pwrSys_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a message of type *INFO.
     **/
    public void Var042()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            String id = "CAE0002";
            JobLog.writeMessage(systemObject_, id, AS400Message.INFORMATIONAL);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a message of type *ESCAPE which writes to the joblog and returns an IOException with the sent message.
     **/
    public void Var043()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        String id = "CAE0020";
        try
        {
            JobLog.writeMessage(systemObject_, id, AS400Message.ESCAPE);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends an invalid type, verify default return of ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID.
     **/
    public void Var044()
    {
        try
        {
            String id = "CAE0026";
            JobLog.writeMessage(systemObject_, id, 10);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     Sends invalid message ID, get back CPF2499 CPFAAA not allowed (IOException).
     **/
    public void Var045()
    {
        try
        {
            String id = "CPFAAA";
            JobLog.writeMessage(systemObject_, id, AS400Message.DIAGNOSTIC);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") && exceptionMsgHas(e, "CPF2499"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception.");
            }
        }
    }

    /**
     Returns an IOException, CPF24B6 Length of 32768, not valid for message text or data.
     For messages with replacement data the length must be 0 - 32767.
     **/
    public void Var046()
    {
        int ccsid = systemObject_.getCcsid();

        try
        {
            AS400Text textToLargeType = new AS400Text(32768, ccsid, systemObject_);
            byte[]subst = textToLargeType.toBytes("HELPER  ");
            JobLog.writeMessage(systemObject_, "CAE0023", AS400Message.INFORMATIONAL, subst);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "AS400Exception") && exceptionMsgHas(e, "CPF24B6"))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception");
            }
        }
    }

    /**
     Sends completion message with substitution text.
     **/
    public void Var047()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            int ccsid = systemObject_.getCcsid();
            AS400Text text1024Type = new AS400Text(1024, ccsid, systemObject_);
            byte[] subst = text1024Type.toBytes("THISTHAT      THE Other!");
            String id = "CAE9027";
            JobLog.writeMessage(systemObject_, id, AS400Message.COMPLETION,subst);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends an escape message using message object data.
     **/
    public void Var048()
    {
        try
        {
            CommandCall cmd = new CommandCall(systemObject_);
            // Make sure the program or library or both do not exist so that a message is returned.
            if (!cmd.run("CHKOBJ OBJ(QSYS/NOPGM) OBJTYPE(*PGM)") || cmd.getMessageList().length > 0)
            {
                AS400Message[] msg = cmd.getMessageList();
                JobLog.writeMessage(systemObject_, (String)msg[0].getID(), (int)msg[0].getType(), (String)msg[0].getPath(), (byte[])msg[0].getSubstitutionData());
                StringBuffer sb = new StringBuffer(); 
                boolean passed = writeMessageSuccessful(msg[0].getID(), sb,systemObject_.getJobs(AS400.COMMAND)); 
                assertCondition(passed, sb.toString());

            }
            else
            {
                failed("Command did not run as expected");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a completion message from a lowercase qualified MSGF in QSYS without substitution text
     **/
    public void Var049()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            String id = "CBE7016";
            JobLog.writeMessage(systemObject_, id, AS400Message.COMPLETION, "/qsys.lib/qcblmsge.msgf");
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a completion message from a specific MSGF not in QSYS with substitution text.
     **/
    public void Var050()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }

        int ccsid = systemObject_.getCcsid();
        AS400Text text1024Type = new AS400Text(1024, ccsid, systemObject_);
        byte[] subst = text1024Type.toBytes("mySubstText  ");
        try
        {
            String id = "QSH0101";
            JobLog.writeMessage(systemObject_, id, AS400Message.COMPLETION,"/QSYS.LIB/QSHELL.LIB/QZSHMSGF.MSGF", subst);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a completion message from a specific MSGF with bin4 substitution data.
     **/
    public void Var051()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            Integer intObject=Integer.valueOf(132);
            AS400Bin4 bin4Converter = new AS400Bin4();
            byte[] subst=bin4Converter.toBytes(intObject);
            String id = "CBE7001";
            JobLog.writeMessage(systemObject_, "CBE7001", AS400Message.COMPLETION,"/QSYS.LIB/QCBLMSGE.MSGF",subst);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Sends a completion message from a specific MSGF with bin2 substitution data.
     **/
    public void Var052()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }
        try
        {
            Short shortObject=Short.valueOf((short)15);
            AS400Bin2 bin2Converter = new AS400Bin2();
            byte[] subst=bin2Converter.toBytes(shortObject);
            String id = "CAE0027";
            JobLog.writeMessage(systemObject_, "CAE0027", AS400Message.COMPLETION,"/QSYS.LIB/QCPFMSG.MSGF",subst);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = writeMessageSuccessful(id, sb,systemObject_.getJobs(AS400.COMMAND)); 
            assertCondition(passed, sb.toString());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Checks error path of property not set when AS400 object is null.
     **/
    public void Var053()
    {
        try
        {
            String id = "CAE0009";
            JobLog.writeMessage(null, id, AS400Message.COMPLETION);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "system");
        }
    }

    /**
     Checks error path of property not set when message id is null.
     **/
    public void Var054()
    {
        try
        {
            JobLog.writeMessage(systemObject_, null, AS400Message.COMPLETION);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "messageID");
        }
    }

    /**
     Checks error path of property not set when path is null
     **/
    public void Var055()
    {
        try
        {
            String id = "CAE0027";
            JobLog.writeMessage(systemObject_, id, AS400Message.COMPLETION, (String)null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "messageFile");
        }
    }

    /**
     Returns an ExtendedIllegalArgumentException, javaValue (HELPER  ): Length is not valid.
     **/
    public void Var056()
    {
        try
        {
            int ccsid = systemObject_.getCcsid();

            AS400Text textToSmallType = new AS400Text(3, ccsid, systemObject_);
            byte[]subst = textToSmallType.toBytes("HELPER  ");
            JobLog.writeMessage(systemObject_, "CAE0023", AS400Message.INFORMATIONAL, subst);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "ExtendedIllegalArgumentException");
        }
    }

    /**
     Sends an immediate message. The ID is blank and the qualified message file name is ignored by the api.
     The substitution text is the immediate message text, the joblog displays *NONE in the message ID field.
     For immediate messages the type must be *INFO and the text length must be 1 to 6000.
     **/
    public void Var057()
    {
        try
        {
            int ccsid = systemObject_.getCcsid();

            AS400Text text6000Type = new AS400Text(6000, ccsid, systemObject_);
            byte[]subst = text6000Type.toBytes("*******This is an immediate message**********");
            JobLog.writeMessage(systemObject_, "       ", AS400Message.INFORMATIONAL, subst);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }


    /**
     Verify the default returns for Valid Field Identifiers.
    **/
    public void Var058()
    {
        // Keys to the tests to run 
        Vector<Integer> key = new Vector<Integer>( 2 );

	// Add all tests that need to run. This is the default test so add all
	// tests and set to 0
        key.add( Integer.valueOf(101) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(201) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(301) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(302) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(401) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(402) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(403) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(404) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(501) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(602) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(603) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(604) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(605) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(606) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(607) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(702) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(703) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(704) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(705) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(706) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(801) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1001) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1101) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1201) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1301) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1302) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1303) );key.add( Integer.valueOf(0) ); 
        key.add( Integer.valueOf(1304) );key.add( Integer.valueOf(0) ); 
                
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }
        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);
            Enumeration<?> e = j.getMessages();
	    if (e.hasMoreElements() )
                assertCondition(validateJobMessages( (QueuedMessage)e.nextElement(), key ));
        }
        catch (Exception e )
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Test to see if all fields return data when set
     **/ 
    public void Var059()
    {
        if (pwrSys_ == null)
        {
            failed("Power user not specified.");
            return;
        }

        int ccsid = systemObject_.getCcsid();
        AS400Text text1024Type = new AS400Text(1024, ccsid, systemObject_);
        byte[] subst = text1024Type.toBytes("mySubstText  ");
        
	try
        {   
            String id = "QSH0101";
	    JobLog.writeMessage(systemObject_, id, AS400Message.INFORMATIONAL,"/QSYS.LIB/QSHELL.LIB/QZSHMSGF.MSGF", subst);
            JobList jobList = new JobList(pwrSys_);
            jobList.setName("QZRCSRVS");
	    Enumeration<?> list = jobList.getJobs();

            while (list.hasMoreElements())
            {
		// Get the next job 
                Job j = (Job)list.nextElement();

                // Check to see if there is a job that is still a 
                String compStatus = j.getCompletionStatus();
                if (compStatus.equals(Job.COMPLETION_STATUS_NOT_COMPLETED))
		{
                    JobLog jq = new JobLog(pwrSys_, j.getName(), j.getUser(), j.getNumber());
	            
		    jq.addAttributeToRetrieve( JobLog.ALERT_OPTION );                                     //101 
		    jq.addAttributeToRetrieve( JobLog.REPLACEMENT_DATA );                                 //201 
	            jq.addAttributeToRetrieve( JobLog.MESSAGE  );                                         //301 
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_WITH_REPLACEMENT_DATA  );                   //302 
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_HELP );                                     //401
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_HELP_WITH_REPLACEMENT_DATA );               //402 
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_HELP_WITH_FORMATTING_CHARACTERS );         //403 
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_HELP_WITH_REPLACEMENT_DATA_AND_FORMATTING_CHARACTERS ); //404
	            jq.addAttributeToRetrieve( JobLog.DEFAULT_REPLY );                                    //501
	            jq.addAttributeToRetrieve( JobLog.SENDER_TYPE );                                      //602 
	            jq.addAttributeToRetrieve( JobLog.SENDING_PROGRAM_NAME );                             //603 
	            jq.addAttributeToRetrieve( JobLog.SENDING_MODULE_NAME );                              //604 
	            jq.addAttributeToRetrieve( JobLog.SENDING_PROCEDURE_NAME );                           //605 
	            jq.addAttributeToRetrieve( JobLog.SENDING_STATEMENT_NUMBERS);                     //606  
	            jq.addAttributeToRetrieve( JobLog.SENDING_USER_PROFILE );                             //607 
	            jq.addAttributeToRetrieve( JobLog.RECEIVING_TYPE );                                   //702 
	            jq.addAttributeToRetrieve( JobLog.RECEIVING_PROGRAM_NAME  );                          //703  
	            jq.addAttributeToRetrieve( JobLog.RECEIVING_MODULE_NAME );                            //704 
	            jq.addAttributeToRetrieve( JobLog.RECEIVING_PROCEDURE_NAME );                         //705  
	            jq.addAttributeToRetrieve( JobLog.RECEIVING_STATEMENT_NUMBERS);                    //706  
	            jq.addAttributeToRetrieve( JobLog.MESSAGE_FILE_LIBRARY_USED );                        //801  
	            jq.addAttributeToRetrieve( JobLog.REPLY_STATUS );                                     //1001 
	            jq.addAttributeToRetrieve( JobLog.REQUEST_STATUS );                                   //1101 
	            jq.addAttributeToRetrieve( JobLog.REQUEST_LEVEL );                                    //1201 
	            jq.addAttributeToRetrieve( JobLog.CCSID_FOR_TEXT );                                   //1301 
	            jq.addAttributeToRetrieve( JobLog.CCSID_CONVERSION_STATUS_TEXT );                     //1302 
	            jq.addAttributeToRetrieve( JobLog.CCSID_FOR_DATA  );                                  //1303 
	            jq.addAttributeToRetrieve( JobLog.CCSID_CONVERSION_STATUS_DATA );                     //1304 
               
        
	            
	            // Keys to the tests to run 
                    Vector<Integer> key = new Vector<Integer>( 2 );
		    key.add( Integer.valueOf(101) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(201) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(301) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(302) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(401) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(402) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(403) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(404) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(501) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(602) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(603) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(604) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(605) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(606) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(607) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(702) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(703) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(704) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(705) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(706) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(801) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1001) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1101) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1201) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1301) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1302) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1303) );key.add( Integer.valueOf(1) ); 
		    key.add( Integer.valueOf(1304) );key.add( Integer.valueOf(1) ); 
                
		    Enumeration<?> e = jq.getMessages();
                    
		    // Checking to see it the job is active and the ID equals
		    // the id sent in 
                    while (e.hasMoreElements())
		    {
                        QueuedMessage message = (QueuedMessage) e.nextElement();

                        if (message.getID().equals(id.toUpperCase())) 
			{
		            assertCondition(validateJobMessages( message, key ));
			    return; 
			}
		    }
		}
	    }
        }
        catch (Exception e )
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Test clearing the joblog fields and then call getMessages() 
     Should throw exception 
    **/
    public void Var060()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }

        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);
	    j.clearAttributesToRetrieve();
            j.getMessages();
            failed("Did not throw exception.");
        }
        catch (Exception e )
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }
    /**
     Clearing fields then adding a field
    **/
    public void Var061()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }

        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);
	    j.clearAttributesToRetrieve();
	    j.addAttributeToRetrieve( JobLog.REPLACEMENT_DATA );  //201 = 7
            Vector<Integer> key = new Vector<Integer>( 2 );
	    key.add( Integer.valueOf(201) );key.add( Integer.valueOf(1) ); 
            j.getMessages();
	    succeeded();
        }
        catch (Exception e )
        {
            failed(e, "Unexpected Exception.");
        }
    }

/*
    /**
     Clear the Attributes
    **/
    public void Var062()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }

        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);
	    j.clearAttributesToRetrieve();
	    succeeded();
        }
        catch (Exception e )
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
     Clear fields, set one field, and check it all other fields are empty
    **/
    public void Var063()
    {
        String[] job = startCLJob();
        if (job == null)
        {
            failed("CL job not submitted.");
            return;
        }

        try
        {
            JobLog j = new JobLog(systemObject_, job[2], job[1].toLowerCase(), job[0]);
	    j.clearAttributesToRetrieve();
	    
	    // Set just REPLACEMENT_DATA
	    j.addAttributeToRetrieve( JobLog.REPLACEMENT_DATA );  //201 = 7
            
	    Vector<Integer> key = new Vector<Integer>( 2 );
	    
	    key.add( Integer.valueOf(201) );key.add( Integer.valueOf(1) ); 

	    // make sure everything else is empty 
            key.add( Integer.valueOf(101) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(301) );key.add( Integer.valueOf(0) ); 
            key.add( Integer.valueOf(302) );key.add( Integer.valueOf(1) ); 
	    key.add( Integer.valueOf(401) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(402) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(403) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(404) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(602) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(603) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(604) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(605) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(606) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(607) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(702) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(703) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(704) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(705) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(706) );key.add( Integer.valueOf(0) ); 
            key.add( Integer.valueOf(801) );key.add( Integer.valueOf(1) ); 
	    key.add( Integer.valueOf(1201) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(1001) );key.add( Integer.valueOf(1) ); 
	    key.add( Integer.valueOf(1101) );key.add( Integer.valueOf(1) ); 
	    key.add( Integer.valueOf(1301) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(1302) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(1303) );key.add( Integer.valueOf(0) ); 
	    key.add( Integer.valueOf(1304) );key.add( Integer.valueOf(0) ); 
            j.getMessages();
	    succeeded();
        }
        catch (Exception e )
        {
            failed(e, "Unexpected Exception.");
        }
    }
}
