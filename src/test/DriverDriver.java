 //////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DriverDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Vector;

/**
This class invokes all the various component TestDrivers in succession.
<p>
Use the following parameter tags and values:
<pre>
     -ifsdir     - A local directory which is IFS-mounted to an AS/400.
     -installdir - A local directory to which the Install component
                   can write files.
     -printer    - The name of a printer for testing.

Any additional parameters are passed on to the individual TestDrivers
as appropriate.

Examples:
   java DriverDriver -system rchas57a
   java DriverDriver -system rchas57a -uid tester -pwd secret -run B
     -ifsdir K: -installdir C:\install -printer MYPRINTER
     -trace diagnostic, error, information, jdbc, warning
</pre>
If system or uid or pwd are not specified, you will be prompted for a system name or a userid or password, even if running only unattended variations.

**/
public class DriverDriver
{
  protected static String ifsDir_ = null;     // local directory for IFSTests
  protected static String installDir_ = null; // local directory for InstallTest
  protected static String printer_ = null;    // printer for NPPrintTest
  protected static String misc_ = null;       // '-misc' argument passed in
  protected static Vector<String> common_args_;       // everything the caller specified
                                              // for options, other than the above

  public static void main(String[] args) 
  {
    try
    {
      parseParms(args);

      // Set up argument lists for the various test drivers.
      String[] standard_args = setupArgs(common_args_, misc_);      // generic
      String[] ifs_args = setupArgs(common_args_, ifsDir_);         // for IFSTests
      String[] install_args = setupArgs(common_args_, installDir_); // for InstallTest
      String[] printer_args = setupArgs(common_args_, printer_);    // for NPPrintTest

      String misc;  // local copy of misc_ that we can manipulate
      if (misc_ == null || misc_.trim().length() == 0) misc = new String(",");
      else misc = new String(misc_);
      // String no_clean = new String(misc + ",NOCLEAN");
      String no_drop  = new String(misc + ",NODROP");
      String coverage  = new String("COVERAGE");
      // String[] noclean_args  = setupArgs(common_args_, no_clean);  // for JD*Test
      String[] nodrop_args   = setupArgs(common_args_, no_drop);   // for DQTest
      String[] coverage_args = setupArgs(common_args_, coverage);  // for ConvTest

      // Run all the test drivers.

      TestDriver drv = null;

      drv = new CmdTest(standard_args);
      runDriver(drv);

      ///drv = new ConvTest(standard_args);
      ///drv = new com.ibm.as400.access.ConvTest(standard_args);
      //drv = new com.ibm.as400.access.ConvTest(coverage_args);
      drv = new ConvTest(coverage_args);
      runDriver(drv);

      drv = new DDMTest(standard_args);
      runDriver(drv);

      drv = new DQTest(nodrop_args);
      runDriver(drv);

      drv = new DTTest(standard_args);
      runDriver(drv);

      drv = new FDTest(standard_args);
      runDriver(drv);

      ///drv = new IFSStressTests(standard_args);
      ///runDriver(drv);

      drv = new IFSTests(ifs_args);
      runDriver(drv);

      drv = new InstallTest(install_args);
      runDriver(drv);


      ///drv = new JDCallStmtTest(standard_args);
      ///runDriver(drv);

      ///drv = new JDConnectionTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDDMDTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDDriverTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDPrepStmtTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDRSMDTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDRSTest(noclean_args);
      ///runDriver(drv);

      ///drv = new JDStmtTest(noclean_args);
      ///runDriver(drv);


      ///drv = new LocalSocketTest(standard_args);
      ///runDriver(drv);

      ///drv = new NLSTest(standard_args);
      ///runDriver(drv);

      drv = new NPPrintTest(printer_args);
      runDriver(drv);

      drv = new PNTest(standard_args);
      runDriver(drv);

      drv = new PgmTest(standard_args);
      runDriver(drv);

      drv = new RFTest(standard_args);
      runDriver(drv);

      drv = new SecTest(standard_args);
      runDriver(drv);

      drv = new ThreadTest(standard_args);
      runDriver(drv);

      drv = new TraceTests(standard_args);
      runDriver(drv);


    }
    catch(Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }
    catch(Error e)
    {
      System.out.println("Program terminated abnormally with an error.");
      e.printStackTrace();
    }

    System.exit(0);
  }

  // Hide the constructors.
  protected DriverDriver() {}
  protected DriverDriver(String[] args) {}


/**
Parse parameters.
@exception Exception various possible problems
**/
  public static void parseParms(String args[])
    throws Exception
  {
    // States used when parsing input parameters.
    // final int FINISH = 0;
    final int START = 1;
    final int PARSE_IFSDIR = 2;
    final int PARSE_INSTALLDIR = 3;
    final int PARSE_PRINTER = 4;
    final int PARSE_MISCELLANEOUS = 5;

    // Reset values to defaults.
    ifsDir_ = null;
    installDir_ = null;
    printer_ = null;
    misc_ = null;
    common_args_ = new Vector<String>();

    // Parse the command line parameters.
    int state = START;

    for (int i = 0; i < args.length; i++)
    {
      // Get the next arg
      String arg = args[i].toLowerCase();

      switch(state)
      {
        case START:
          if (arg.equals("-ifsdir"))
            state = PARSE_IFSDIR;
          else if (arg.equals("-installdir"))
            state = PARSE_INSTALLDIR;
          else if (arg.equals("-printer"))
            state = PARSE_PRINTER;
          else if (arg.equals("-misc"))
            state = PARSE_MISCELLANEOUS;
          else
            common_args_.addElement(args[i]);  // preserve case
          break;

        case PARSE_IFSDIR:
          ifsDir_ = args[i];
          state = START;
          break;

        case PARSE_INSTALLDIR:
          installDir_ = args[i];
          state = START;
          break;

        case PARSE_PRINTER:
          printer_ = args[i];
          state = START;
          break;

        case PARSE_MISCELLANEOUS:
          misc_ = args[i];
          state = START;
          break;

        default:
          throw new Exception("Internal error: Unrecognized state.");
      }
    }

    // Throw an exception if an argument specification is incomplete.
    switch (state)
    {
      case PARSE_IFSDIR:
        throw new Exception("Incomplete IFS directory specification.");
      case PARSE_INSTALLDIR:
        throw new Exception("Incomplete Install directory specification.");
      case PARSE_PRINTER:
        throw new Exception("Incomplete Printer specification.");
      case PARSE_MISCELLANEOUS:
        throw new Exception("Incomplete miscellaneous specification.");
      default:
        // no problem
    }
  }


/**
Run a driver.
**/
  public static void runDriver(TestDriver driver)
  {
    try
    {
      driver.init();
      driver.start();
      driver.stop();
      driver.destroy();
    }
    catch(Exception e)
    {
      System.out.println("Program terminated abnormally.");
      e.printStackTrace();
    }
  }


/**
Set up arguments for a Driver.
**/
  public static String[] setupArgs(Vector<String> base_args, String misc_val)
  {
    if (base_args == null)
      base_args = new Vector<String>();
    String[] combined_args = null;
    if (misc_val != null)
    {
      @SuppressWarnings("unchecked")
      Vector<String> temp_args = (Vector<String>)base_args.clone();
      temp_args.addElement("-misc");
      temp_args.addElement(misc_val);
      combined_args = new String[temp_args.size()];
      temp_args.copyInto(combined_args);
    }
    else
    {
      combined_args = new String[base_args.size()];
      base_args.copyInto(combined_args);
    }
    return combined_args;
  }

}
