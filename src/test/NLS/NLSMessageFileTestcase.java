///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSMessageFileTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NLS;

import java.io.FileOutputStream;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 *Testcase NLSMessageFileTestcase.  This test class verifies the use of DBCS Strings
 *in selected MessageFile testcase variations.
**/
public class NLSMessageFileTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSMessageFileTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }

  String dbcs_string5 = getResource("IFS_DBCS_STRING5");
  String dbcs_string10 = getResource("IFS_DBCS_STRING10");
  String dbcs_string50 = getResource("IFS_DBCS_STRING50");

  /**
  Constructor.
  **/
  public NLSMessageFileTestcase(AS400            systemObject,
                                Vector<String>           variationsToRun,
                                int              runMode,
                                FileOutputStream fileOutputStream
                                )

  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSMessageFileTestcase", 2,
          variationsToRun, runMode, fileOutputStream);
  }


  void Setup()
  {
  }



  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    try
    {
      Setup();
    }
    catch (Exception e)
    {
      output_.println("Setup failed.");
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
  }





  /**
   Method tested: getMessage() with no substitution text,
   message ID has DBCS characters.
  **/
  public void Var001()
  {
     try
     {
        MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

        AS400Message m = aMessageFile.getMessage(dbcs_string5);

        failed("no exception"+m);
     }
     catch(Exception e)
     {
        if (exceptionIs(e, "AS400Exception"))
        {
           if (e.getMessage().startsWith("CPF2499"))
              succeeded();

           else
           {
              failed(e, "Unexpected exception occurred.");
           }
        }
        else
        {
           failed(e, "Unexpected exception occurred.");
        }
     }
  }


  /**
   Method tested: getMessage() with substitution text,
   Ensure that getMessage() with NLS substitution text works
  **/
  public void Var002()
  {
     try
     {
        MessageFile aMessageFile = new MessageFile(systemObject_, "/QSYS.LIB/QCPFMSG.MSGF");

        AS400Message m = aMessageFile.getMessage("CAE0080", dbcs_string5);

     // if (m.getText().equalsIgnoreCase(dbcs_string5 + " not valid hexadecimal data."))
        if (m.getText().indexOf(dbcs_string5) > -1)
        {
           succeeded();
        }
        else
          failed("wrong message text: " + m.getText());
     }
     catch(Exception e)
     {
        failed(e, "Unexpected exception occurred.");
     }
  }











}




