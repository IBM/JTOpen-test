///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NLSNPTestcase.java
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

import com.ibm.as400.access.AFPResource;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DBCSOnlyFieldDescription;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.ObjectDescription; ///
import com.ibm.as400.access.ObjectDoesNotExistException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.Printer;
import com.ibm.as400.access.PrinterFile;
import com.ibm.as400.access.RecordFormat;
import com.ibm.as400.access.SCS5553Writer;
import com.ibm.as400.access.SpooledFileOutputStream;
import com.ibm.as400.access.WriterJob;

import test.NLSTest;
import test.Testcase;

/**
 *Testcase NLSNPTestcase.  This test class verifies the use of DBCS Strings
 *in selected Network Print and SCS testcase variations.
**/
public class NLSNPTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NLSNPTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NLSTest.main(newArgs); 
   }
  String printer_; // name of printer to test to

  String np_lib = getResource("NP_LIB");
  String np_printer = getResource("NP_PRINTER");
  String np_queue = getResource("NP_QUEUE");
  String np_dbcs_text = getResource("NP_DBCS_TEXT");
  String np_dbcs_textb = getResource("NP_DBCS_TEXTB");
  String np_form = getResource("NP_FORM");
  String np_file = getResource("NP_FILE");

  /**
  Constructor.  This is called from the NLSTest constructor.
  **/
  public NLSNPTestcase(AS400            systemObject,
                      Vector<String>         variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String           printer)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "NLSNPTestcase", 8,
          variationsToRun, runMode, fileOutputStream);

    if (printer != null)
    {
      printer_ = printer;
    }
    else
    {
      output_.println("Warning: NLSNPTestcase: Printer name not specified.");
    }
  }

  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    // Connect to the AS/400 for the record level access service
    try
    {
      systemObject_.connectService(AS400.RECORDACCESS);
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }

    try
    {
      setup();
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

    if ((allVariations || variationsToRun_.contains("7")) &&
        runMode_ != ATTENDED)
    {
      setVariation(7);
      Var007();
    }

    if ((allVariations || variationsToRun_.contains("8")) &&
        runMode_ != ATTENDED)
    {
      setVariation(8);
      Var008();
    }

    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      output_.println("Cleanup failed.");
    }

    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectService(AS400.RECORDACCESS);
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    // Make sure library np_lib exists on the system.  Copy form definition into it.
    try
    {
	CommandCall cmd = new CommandCall(NLSTest.PwrSys);
	deleteLibrary(cmd, np_lib);
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
    
    try
    {
      CommandCall cmd = new CommandCall(NLSTest.PwrSys, "QSYS/DLTDEVD DEVD("+np_printer+")");
      if (cmd.run()==false)
      {
        output_.println("Setup error: Could not delete device "+np_printer+".");
        output_.println(cmd.getMessageList()[0].getID() + ":" + cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }

    try
    {
      CommandCall cmd = new CommandCall(NLSTest.PwrSys, "QSYS/CRTLIB LIB("+np_lib+") AUT(*ALL)");
      if (cmd.run()==false)
      {
        output_.println("Setup failed: Could not create library.");
        output_.println(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
        throw new Exception("Setup failed:  Could not create library");
      }
      else
      {
        output_.println("Setup successfully created library "+np_lib+".\n");
        try
        {
          cmd.setCommand("QSYS/CRTDUPOBJ OBJ(F1A00010) FROMLIB(NPJAVA) OBJTYPE(*FORMDF) TOLIB("+np_lib+") NEWOBJ("+np_form+")");
          if (cmd.run()==false)
          {
            output_.println("Setup failed: Could not copy form definition into "+np_lib+".");
            output_.println(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
            throw new Exception("Setup failed:  Could not copy form definition");
          }
          else
          {
            output_.println("Setup successfully copied form definition into library "+np_lib+".\n");
            try
            {
              cmd.setCommand("QSYS/GRTOBJAUT OBJ("+np_lib+"/"+np_form+") OBJTYPE(*FORMDF) USER(*PUBLIC) AUT(*ALL)");
              if (cmd.run()==false)
              {
                output_.println("Setup failed: Could not set authorities on form definition "+np_form+" in "+np_lib+".\n");
                output_.println(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
                throw new Exception("Setup failed:  Could not set authorities on form definition");
              }
              else
              {
                output_.println("Setup successfully set authorities on form definition "+np_form+" in library "+np_lib+".\n");
              }
            }
            catch(Exception e)
            {
              e.printStackTrace(output_);
              throw e;
            }

          }
        }
        catch(Exception e)
        {
          e.printStackTrace(output_);
          throw e;
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void cleanup()
    throws Exception
  {
    try
    {
	deleteLibrary(np_lib);
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      throw e;
    }
  }

  private static String getTextDescription(ObjectDescription objDesc)
    throws AS400Exception, AS400SecurityException, ErrorCompletingRequestException, InterruptedException, IOException, ObjectDoesNotExistException
  {
    return (String)objDesc.getValue(ObjectDescription.TEXT_DESCRIPTION);
  }

  /**
   Create an OutputQueue using CommandCall with an NLS string
   for the text description. Retrieve the description attribute
   from the PrintObject.
   **/
  public void Var001()
  {
    StringBuffer failMsg = new StringBuffer();
    String cmdString = "QSYS/CRTOUTQ OUTQ("+np_lib+"/"+np_queue;
    cmdString += ") AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    String qname = "/QSYS.LIB/"+np_lib+".LIB/"+np_queue+".OUTQ";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    try
    {
      if (cmd.run()==false)
      {
        failMsg.append("Could not create an output queue.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
      else
      {
        OutputQueue q = new OutputQueue(systemObject_, qname);
        String attrText = q.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

        ObjectDescription npObj = new ObjectDescription(systemObject_, qname);
        String objDesc = getTextDescription(npObj);

        if (!attrText.equals(np_dbcs_text))
        {
          failMsg.append("Wrong description text returned.\n");
          failMsg.append("  Original  : "+np_dbcs_text+".\n");
          failMsg.append("  ObjectDesc: "+objDesc+".\n");
          failMsg.append("  Returned  : "+attrText+".\n");
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTOUTQ OUTQ("+np_lib+"/"+np_queue+")";
      cmd.setCommand(cmdString);
      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the output queue.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


  /**
   Create an OutputQueue using CommandCall with an NLS string
   for the text description. Call update() with a new text
   description. Retrieve the description attribute from the PrintObject.
   **/
  public void Var002()
  {
    StringBuffer failMsg = new StringBuffer();
    String np_queue = "NLSQ2";
    String cmdString = "QSYS/CRTOUTQ OUTQ("+np_lib+"/"+np_queue;
    cmdString += ") AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    String qname = "/QSYS.LIB/"+np_lib+".LIB/"+np_queue+".OUTQ";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    try
    {
      if (cmd.run()==false)
      {
        failMsg.append("Could not create an output queue.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
      else
      {
        OutputQueue q = new OutputQueue(systemObject_, qname);
        String attrText = q.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

        ObjectDescription npObj = new ObjectDescription(systemObject_, qname);
        String objDesc = getTextDescription(npObj);

        // change queue's description
        cmdString = "QSYS/CHGOBJD OBJ("+np_lib+"/"+np_queue+") OBJTYPE(*OUTQ) ";
        cmdString += "TEXT('"+np_dbcs_textb+"')";
        cmd.setCommand(cmdString);
        if (cmd.run()==false)
          failMsg.append("Could not change object text description.\n");

        q.update();
        String newText = q.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

        ObjectDescription npObj2 = new ObjectDescription(systemObject_, qname);
        String newDesc = getTextDescription(npObj2);

        if (!newText.equals(np_dbcs_textb))
        {
          failMsg.append("Wrong description text returned.\n");
          failMsg.append("  Original  : "+np_dbcs_textb+".\n");
          failMsg.append("  ObjectDesc: "+objDesc+".\n");
          failMsg.append("  Returned  : "+newText+".\n");
        }
        if (objDesc.equals(newDesc))
        {
          failMsg.append("Update did not occur: Object descriptions are equal.\n");
        }
        if (attrText.equals(newText))
        {
          failMsg.append("Update did not occur: Text descriptions are equal.\n");
        }
      }  // else
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTOUTQ OUTQ("+np_lib+"/"+np_queue+")";
      cmd.setCommand(cmdString);
      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the output queue.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


  /**
   Create a Printer using CommandCall with an NLS string
   for the text description. Retrieve the description attribute
   from the PrintObject.
   **/
  public void Var003()
  {
    StringBuffer failMsg = new StringBuffer();
 
    // Delete the printer if it exists.
    try
    {
      CommandCall cmd = new CommandCall(NLSTest.PwrSys, "QSYS/DLTDEVD "+np_printer);
      if (cmd.run()==false)
      {
        output_.println("Setup: Could not delete the printer device description.\n");
        output_.println(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    String cmdString = "QSYS/CRTDEVPRT DEVD("+np_printer+") DEVCLS(*LCL) TYPE(*IPDS) ";
    cmdString += "MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011) ";
    cmdString += "AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    Printer p = new Printer(systemObject_, np_printer);

    // Try to create the printer
    try
    {
      // command failed and it wasn't because device existed already (cpd2602)
      if (cmd.run()==false)
      {
        if (cmd.getMessageList()[0].getID().equals("CPD2602"))
        {
          failMsg.append("Printer device already existed.\n");
          failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
        }
        else
        {
          // CPF222E: Special authority required
          if (cmd.getMessageList()[0].getID().equals("CPF222E"))
          {
            failMsg.append(cmd.getMessageList()[0].getText()+"\n");
            failMsg.append("  Special authority is needed to create the printer device.\n");
            failMsg.append("  This must be done for this variation to succeed.\n");
          }
          else
          {
            failMsg.append("Could not create the printer device description.\n");
            failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
          }
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }


    try
    {

//  Cannot create the printer here because the JAVATEST userid
//  does not have the correct authority.
//  Instead, make sure the specified printer device already exists
//  and has an NLS text description.


      String attrText = new String();
      attrText = p.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      ObjectDescription npObj = new ObjectDescription(systemObject_, "/QSYS.LIB/"+np_printer+".DEVD");
      String objDesc = getTextDescription(npObj);

      if (!attrText.equals(np_dbcs_text))
      {
        failMsg.append("Wrong description text returned.\n");
        failMsg.append("  Original  : "+np_dbcs_text+".\n");
        failMsg.append("  ObjectDesc: "+objDesc+".\n");
        failMsg.append("  Returned  : "+attrText+".\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTDEVD "+np_printer;
      cmd.setCommand(cmdString);

// Need special authority in order to delete the printer object.
// See comments above.

      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the printer device description.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


  /**
   Create a Printer using CommandCall with an NLS string
   for the text description. Call update() with a new text
   description. Retrieve the description attribute from the PrintObject.
   **/
  public void Var004()
  {
    StringBuffer failMsg = new StringBuffer();

    // Delete the printer if it exists.
    try
    {
      CommandCall cmd = new CommandCall(NLSTest.PwrSys, "QSYS/DLTDEVD "+np_printer);
      if (cmd.run()==false)
      {
        output_.println("Setup: Could not delete the printer device description.\n");
        output_.println(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    String cmdString = "QSYS/CRTDEVPRT DEVD("+np_printer+") DEVCLS(*LCL) TYPE(*IPDS) ";
    cmdString += "MODEL(0) PORT(5) SWTSET(0) ONLINE(*NO) FONT(011) ";
    cmdString += "AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    Printer p = new Printer(systemObject_, np_printer);

    // Try to create the printer
    try
    {
      // command failed and it wasn't because device existed already (cpd2602)
      if (cmd.run()==false)
      {
        if (cmd.getMessageList()[0].getID().equals("CPD2602"))
        {
          failMsg.append("Printer device already existed.\n");
          failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
        }
        else
        {
          // CPF222E: Special authority required
          if (cmd.getMessageList()[0].getID().equals("CPF222E"))
          {
            failMsg.append(cmd.getMessageList()[0].getText()+"\n");
            failMsg.append("  Special authority is needed to create the printer device.\n");
            failMsg.append("  This must be done for this variation to succeed.\n");
          }
          else
          {
            failMsg.append("Could not create the printer device description.\n");
            failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
          }
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }


    try
    {

//  Cannot create the printer here because the JAVATEST userid
//  does not have the correct authority.
//  Instead, make sure the specified printer device already exists
//  and has an NLS text description.


      String attrText = new String();
      attrText = p.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      ObjectDescription npObj = new ObjectDescription(systemObject_, "/QSYS.LIB/"+np_printer+".DEVD");
      String objDesc = getTextDescription(npObj);

      // change printer's description
      cmdString = "QSYS/CHGDEVPRT DEVD("+np_printer+") ";
      cmdString += "TEXT('"+np_dbcs_textb+"')";
      cmd.setCommand(cmdString);
      if (cmd.run()==false)
        failMsg.append("Could not change object text description.\n");

      p.update();
      String newText = p.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      ObjectDescription npObj2 = new ObjectDescription(systemObject_, "/QSYS.LIB/"+np_printer+".DEVD");
      String newDesc = getTextDescription(npObj2);

      if (!newText.equals(np_dbcs_textb))
      {
        failMsg.append("Wrong description text returned.\n");
        failMsg.append("  Original  : "+np_dbcs_textb+".\n");
        failMsg.append("  ObjectDesc: "+objDesc+".\n");
        failMsg.append("  Returned  : "+newText+".\n");
      }
      if (objDesc.equals(newDesc))
      {
        failMsg.append("Update did not occur: Object descriptions are equal.\n");
      }
      if (attrText.equals(newText))
      {
        failMsg.append("Update did not occur: Text descriptions are equal.\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTDEVD "+np_printer;
      cmd.setCommand(cmdString);

// Need special authority in order to delete the printer object.
// See comments above.

      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the printer device description.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }

  /**
   Retrieve a text description from an AFPResource object (e.g. a form definition).
   **/
  public void Var005()
  {
      StringBuffer failMsg = new StringBuffer();

      try
          {
          // set the text description of the AFP resource 
          CommandCall cmd = new CommandCall(systemObject_);
          if (cmd.run("QSYS/CHGOBJD OBJ("+np_lib+"/"+np_form+") OBJTYPE(*FORMDF) TEXT('"+np_dbcs_text+"')") == false)
              {
              failMsg.append("Could not set an AFP resource description to an NLS string. \n");
              failMsg.append(cmd.getMessageList()[0].getID()+".\n");
              failMsg.append(": " + cmd.getMessageList()[0].getText()+".\n");
              failed(failMsg.toString());
              return;
              }

          AFPResource r = new AFPResource(systemObject_, "/QSYS.LIB/"+np_lib+".LIB/"+np_form+".FORMDF");

          String attrText = new String();
          r.update();
          attrText = r.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

          if (!attrText.equals(np_dbcs_text))
              {
              failMsg.append("Wrong description text returned.\n");
              failMsg.append("  Original  : "+np_dbcs_text+".\n");
              failMsg.append("  Returned  : "+attrText+".\n");
              }
          }
      catch(Exception e)
          {
          e.printStackTrace(output_);
          failMsg.append("Unexpected exception.\n");
          }

      if (failMsg.length() != 0)
          failed(failMsg.toString());
      else
          succeeded();

  }


  /**
   Create a PrinterFile using CommandCall with an NLS string
   for the text description. Retrieve the description attribute
   from the PrintObject.
   **/
  public void Var006()
  {
    StringBuffer failMsg = new StringBuffer();

    String printerStr = "/QSYS.LIB/"+np_lib+".LIB/"+np_file+".FILE";
    String cmdString = "QSYS/CRTPRTF FILE("+np_lib+"/"+np_file+") ";
    cmdString += "AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    PrinterFile pf = new PrinterFile(systemObject_, printerStr);

    // Try to create the printer file
    try
    {
      if (cmd.run()==false && !cmd.getMessageList()[0].getID().equals("CPD2602"))
      {
        failMsg.append("Could not create the printer file.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }


    try
    {
      String attrText = new String();
      attrText = pf.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      if (!attrText.equals(np_dbcs_text))
      {
        failMsg.append("Wrong description text returned.\n");
        failMsg.append("  Original  : "+np_dbcs_text+".\n");
        failMsg.append("  Returned  : "+attrText+".\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTF FILE("+np_lib+"/"+np_file+")";
      cmd.setCommand(cmdString);

      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the printer file.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }


  /**
   Create a PrinterFile using CommandCall with an NLS string
   for the text description. Call update() with a new text
   description. Retrieve the description attribute from the PrintObject.
   **/
  public void Var007()
  {
    StringBuffer failMsg = new StringBuffer();

    String printerStr = "/QSYS.LIB/"+np_lib+".LIB/"+np_file+".FILE";
    String cmdString = "QSYS/CRTPRTF FILE("+np_lib+"/"+np_file+") ";
    cmdString += "AUT(*ALL) TEXT('"+np_dbcs_text+"')";
    CommandCall cmd = new CommandCall(systemObject_, cmdString);

    
    PrinterFile pf = new PrinterFile(systemObject_, printerStr);

    // Try to create the printer file
    try
    {
      if (cmd.run()==false && !cmd.getMessageList()[0].getID().equals("CPD2602"))
      {
        failMsg.append("Could not create the printer file.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }


    try
    {
      String attrText = new String();
      attrText = pf.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      // change printer's description
      cmdString = "QSYS/CHGPRTF FILE("+np_lib+"/"+np_file+") ";
      cmdString += "TEXT('"+np_dbcs_textb+"')";
      cmd.setCommand(cmdString);
      if (cmd.run()==false)
        failMsg.append("Could not change object text description.\n"+attrText);

      pf.update();
      String newText = pf.getStringAttribute(PrintObject.ATTR_DESCRIPTION);

      if (!newText.equals(np_dbcs_textb))
      {
        failMsg.append("Wrong description text returned.\n");
        failMsg.append("  Original  : "+np_dbcs_textb+".\n");
        failMsg.append("  Returned  : "+newText+".\n");
      }
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      failMsg.append("Unexpected exception.\n");
    }

    // cleanup
    try
    {
      cmdString = "QSYS/DLTF FILE("+np_lib+"/"+np_file+")";
      cmd.setCommand(cmdString);

      if (cmd.run()==false)
      {
        failMsg.append("Could not delete the printer file.\n");
        failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
      }
    }
    catch(Exception e) {}

    if (failMsg.length() != 0)
      failed(failMsg.toString());
    else
      succeeded();
  }

  /**
   Create a spooled file and write NLS data to it using SpooledFileOutputStream.
   Verify the data will roundtrip by reading it back in using PrintObjectInputStream.
   **/
  @SuppressWarnings("deprecation")
  public void Var008()
  {
      StringBuffer failMsg = new StringBuffer();
      output_.println("Creating spool file.");
      
      try
          {
          
          // create an output queue
          CommandCall cmd = new CommandCall(systemObject_);
          
          if (cmd.run("QSYS/crtoutq OUTQ("+np_lib+"/"+np_queue+") AUT(*ALL)") == false)
              {
              failMsg.append("Could not create an output queue.\n");
              failMsg.append(cmd.getMessageList()[0].getID()+"\n");
              failMsg.append(": " + cmd.getMessageList()[0].getText()+"\n");
	      //return;
              }
            //  }
          
          
	   else
	      {
	   
          // create an output queue object
          OutputQueue outQ = new OutputQueue(systemObject_, "/QSYS.LIB/"+np_lib+".LIB/"+np_queue+".OUTQ");
	   
          // Create a spooled file to send
          SpooledFileOutputStream out = new SpooledFileOutputStream(systemObject_,
                                                                    null, null, outQ);
          SCS5553Writer wtr = new SCS5553Writer(out);
	  
          // Write the contents of the spool file.
          wtr.write(np_dbcs_text);
          wtr.write(np_dbcs_textb);
          wtr.endPage();
          wtr.close();
	  
          // end the normal writer to printer
          if (cmd.run("endwtr WTR("+printer_+") OPTION(*IMMED)") == false)
              {
              // if the error we get back was not no active writer message
              // exit
              if (!cmd.getMessageList()[0].getID().trim().equals("CPF3313"))
                  {
                  failed("Could not end the writer to "+printer_+". "
                         + cmd.getMessageList()[0].getID()
                         + ": " + cmd.getMessageList()[0].getText());
                  return;
                  }
              }
	   
          // go to sleep for a bit to allow the writer to stop
          Thread.sleep(4000,0);

          // create a printer device object using valid system name and printer name
          Printer prtD = new Printer(systemObject_, printer_);

          // start a writer job 
          WriterJob wrtJ = WriterJob.start(systemObject_, prtD, null, outQ);

          // go to sleep for a bit to allow the writer to start
          Thread.sleep(4000,0);
          
          // end the writer after the printing is done
          wrtJ.end("*IMMED");

          // go to sleep for a bit to allow the writer to stop
          Thread.sleep(12000,0);
          
          // clearing the output queue so we can delete it
          if (cmd.run("QSYS/CLROUTQ OUTQ("+np_lib+"/"+np_queue+")") == false)
             {
             failMsg.append("Could not clear the output queue.\n");
             failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
             }
             
          // go to sleep for a bit to allow the output queue to clear
          Thread.sleep(12000,0);
          
          // delete the output queue we created
          if (cmd.run("QSYS/DLTOUTQ OUTQ("+np_lib+"/"+np_queue+")") == false)
              {
              failMsg.append("Could not delete the output queue.\n");
              failMsg.append(cmd.getMessageList()[0].getID()+": "+cmd.getMessageList()[0].getText()+"\n");
              }
          } // end try block
          
          } // end else block
          
      catch(Exception e)
          {
          e.printStackTrace(output_);
          failMsg.append("Unexpected exception.\n");
          }

      if (failMsg.length() != 0)
          failed(failMsg.toString());
      else
          succeeded();
  }






  class NLSDBCSOnlyNoKeyFormat extends RecordFormat
  {
     private static final long serialVersionUID = 1L;

    NLSDBCSOnlyNoKeyFormat()
    {
      super("KEYFMT");
      addFieldDescription(new DBCSOnlyFieldDescription(new AS400Text(10), "field1"));
    }
  }

}
