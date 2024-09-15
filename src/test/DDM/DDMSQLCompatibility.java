///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMSQLCompatibility.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.*;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.JDJobName;
import test.JTOpenTestEnvironment;
import test.Testcase;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Date;

/**
 *Testcase DDMSQLCompatibility.  This test class verifies DDM compatibility with SQL tables, primarily the field (column) default values.
 *<ul compact>
 *<li>record formats
 *<li>field descriptions
 *</ul>
**/
public class DDMSQLCompatibility extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMSQLCompatibility";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  long start;
  long time;
  String testLib_ = "DDMSQLT";
  String[] fileName_ = new String[4];
  boolean qigc_ = true; // Is DBCS installed on the 400? See setup().

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMSQLCompatibility(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMSQLCompatibility", 4, variationsToRun, runMode,
          fileOutputStream);
  }

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMSQLCompatibility(AS400            systemObject,
                      Vector           variationsToRun,
                      int              runMode,
                      FileOutputStream fileOutputStream,
                      
                      String testLib,
                      String password,
                      AS400 PwrSys)
  {
    // The third parameter is the total number of variations in this class.
    super(systemObject, "DDMSQLCompatibility", 4, variationsToRun, runMode,
          fileOutputStream, password, PwrSys);
//    if (testLib != null)
//    {
//      testLib_ = testLib;
//    }
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
      System.out.println("Unable to connect to the AS/400");
      return;
    }


    // Do any necessary setup work for the variations
    try
    {
      setup();
    }
    catch (Exception e)
    {
      // Testcase setup did not complete successfully
      System.out.println("Unable to complete setup; variations not run");
      return;
    }

    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("1"))
        Var001();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("2"))
        Var002();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("3"))
        Var003();
    }
    if (runMode_ != ATTENDED)
    {
      // Run all unattended variations.
      if (allVariations || variationsToRun_.contains("4"))
        Var004();
    }


    // Do any necessary cleanup work for the variations
    try
    {
      cleanup();
    }
    catch (Exception e)
    {
      System.out.println("Unable to complete cleanup.");
    }

    // Disconnect from the AS/400 for record the record level access service
    systemObject_.disconnectAllServices();
  }

  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {


    // This testcase requires the CCSID to be 65535 for the test user
    // Change it for this test, then change back to 37.
    //
    if (systemName_ == null) { 
      systemName_ = systemObject_.getSystemName(); 
    }
    if (userId_ == null) { 
      userId_ = systemObject_.getUserId(); 
    }
    
    {
      try { 
    CommandCall c = new CommandCall();
    c.setSystem(pwrSys_);
    String command = "CHGUSRPRF "+userId_+" CCSID(65535)";
    System.out.println("Running "+command); 
    boolean worked = c.run(command);
    if (!worked) { 
       System.out.println("Failed to "+command); 
    }
      } catch (Exception e) { 
        e.printStackTrace(); 
      }
    }
    systemObject_ = new AS400(systemObject_); ;



    try
    {
	if (JTOpenTestEnvironment.isOS400)
	{
  	   // CCSID of CURRENT job must be 65535
	    System.out.println("Native test so setting job ccsid to 65535"); 
	   JDJobName.setIGC(65535); 
	}


      boolean proxy = false;

      try
      {
         Class.forName("com.ibm.as400.access.SystemValue");
      }
      catch (Throwable e)
      {
         output_.println("DDMSQLCompatibility -- could not load SystemValue (running proxy?).  Will run test anyway. ");
         proxy = true;
         qigc_ = true;
      }

      if (! proxy)
      {
         // First check to see if DBCS is installed on the system;
         // if not, we might as well not even run this testcase.
         SystemValue sv = new SystemValue(systemObject_, "QIGC");
         String val = ((String)sv.getValue()).trim();
         if (val.equals("0"))
         {
           qigc_ = false;

	   output_.println("DDMCheckFields:  QIGC indicates DBCS not installed on "
			   + systemObject_.getSystemName()
			   + ". Changing system.");

	   CommandCall cmd = new CommandCall(pwrSys_);


					/* SetIGC  */ 
	   try 
	   {

	       cmd.run("SETIGC CHANGE(*BOTH) VALUE(*YES)");
	   } catch (Exception e) 
	   {
	       System.out.println("Could not change SETIGC to *YES ");
	       e.printStackTrace();
	   }


         }
         else
         {
           output_.println("DDMSQLCompatibility -- QIGC indicates DBCS is installed on "+systemObject_.getSystemName()+". Running variations.");
           qigc_ = true;
         }
      }

      CommandCall cmd = new CommandCall(systemObject_);

      // Create the library
      try
      {
        cmd.run("CRTLIB LIB("+testLib_+")");
      }
      catch(Exception x) { x.printStackTrace(output_); }

      fileName_[0] = new String("/QSYS.LIB/"+testLib_+".LIB/COMPAT.FILE/%FILE%.MBR");
      fileName_[1] = new String("/QSYS.LIB/"+testLib_+".LIB/COMPATN.FILE/%FILE%.MBR");
      fileName_[2] = new String("/QSYS.LIB/"+testLib_+".LIB/COMPATD.FILE/%FILE%.MBR");
      fileName_[3] = new String("/QSYS.LIB/"+testLib_+".LIB/COMPATC.FILE/%FILE%.MBR");

      SequentialFile f = null;

      // Delete the files
      for (int i=0; i<4; ++i)
      {
        f = new SequentialFile(pwrSys_, fileName_[i]);
        try
        {
          f.delete();
        }
        catch(Exception e) {} // may not exist, so ignore faliures
      }

      // Create the SQL tables with all possible record formats and default values
      AS400JDBCDriver driver = new AS400JDBCDriver();
      Connection connection_ = driver.connect(systemObject_); 

      Statement s = connection_.createStatement();

      String crt1 = "CREATE TABLE "+testLib_+".COMPAT ("; // no defaults
      String crt2 = "CREATE TABLE "+testLib_+".COMPATN ("; // null defaults
      String crt3 = "CREATE TABLE "+testLib_+".COMPATD ("; // 'value' defaults
      String crt4 = "CREATE TABLE "+testLib_+".COMPATC ("; // current defaults (date, time, timestamp)

      crt1 += "test integer, ";
      crt1 += "col1 integer, ";
      crt1 += "col2 smallint, ";
      crt1 += "col3 decimal(10), ";
      crt1 += "col4 numeric(10), ";
      crt1 += "col5 float(4), ";
      crt1 += "col6 real, ";
      crt1 += "col7 double, ";
      crt1 += "col8 char(12), ";
      crt1 += "col9 char(12) for bit data, ";
      crt1 += "col10 char(12) for sbcs data, ";
      crt1 += "col11 char(12) for mixed data, ";
      crt1 += "col12 varchar(20), ";
      crt1 += "col13 graphic(16), ";
      crt1 += "col14 vargraphic(30), ";
      crt1 += "col15 date, ";
      crt1 += "col16 time, ";
      crt1 += "col17 timestamp";
      crt1 += ")";

      crt2 += "test integer, ";
      crt2 += "col1 integer with default null, ";
      crt2 += "col2 smallint with default null, ";
      crt2 += "col3 decimal(10) with default null, ";
      crt2 += "col4 numeric(10) with default null, ";
      crt2 += "col5 float(4) with default null, ";
      crt2 += "col6 real with default null, ";
      crt2 += "col7 double with default null, ";
      crt2 += "col8 char(12) with default null, ";
      crt2 += "col9 char(12) for bit data with default null, ";
      crt2 += "col10 char(12) for sbcs data with default null, ";
      crt2 += "col11 char(12) for mixed data with default null, ";
      crt2 += "col12 varchar(20) with default null, ";
      crt2 += "col13 graphic(16) with default null, ";
      crt2 += "col14 vargraphic(30) with default null, ";
      crt2 += "col15 date with default null, ";
      crt2 += "col16 time with default null, ";
      crt2 += "col17 timestamp with default null";
      crt2 += ")";

      crt3 += "test integer, ";
      crt3 += "col1 integer with default +3, ";
      crt3 += "col2 smallint with default -2, ";
      crt3 += "col3 decimal(10) with default 4, ";
      crt3 += "col4 numeric(10) with default 70000, ";
      crt3 += "col5 float(4) with default 3.14, ";
      crt3 += "col6 real with default 3.1416, ";
      crt3 += "col7 double with default 3.141592654, ";
      crt3 += "col8 char(12) with default 'Default Char', ";
//      crt3 += "col9 char(12) for bit data with default 'BIT data', ";
      crt3 += "col9 char(12) for bit data, ";
      crt3 += "col10 char(12) for sbcs data with default 'Sbcs Data', ";
      crt3 += "col11 char(12) for mixed data with default 'MiXeD DaTa', ";
      crt3 += "col12 varchar(20) with default 'Variable Char', ";
//      crt3 += "col13 graphic(16) with default 'Default Graphic', ";
      crt3 += "col13 graphic(16), ";
//      crt3 += "col14 vargraphic(30) with default 'Variable Graphic', ";
      crt3 += "col14 vargraphic(30), ";
      crt3 += "col15 date with default '1975-07-18', ";
      crt3 += "col16 time with default '06.30.00', ";
      crt3 += "col17 timestamp with default '1998-05-09-14.00.00.000'";
      crt3 += ")";

      crt4 += "test integer, ";
      crt4 += "col1 date with default current_date, ";
      crt4 += "col2 time with default current_time, ";
      crt4 += "col3 timestamp with default current_timestamp";
      crt4 += ")";

      s.executeUpdate(crt1);
      s.executeUpdate(crt2);
      s.executeUpdate(crt3);
      s.executeUpdate(crt4);

      // Insert 1 test record into each table
      s.executeUpdate("INSERT INTO "+testLib_+".COMPAT (test) values (1)");
      s.executeUpdate("INSERT INTO "+testLib_+".COMPATN (test) values (1)");
      s.executeUpdate("INSERT INTO "+testLib_+".COMPATD (test) values (1)");
      s.executeUpdate("INSERT INTO "+testLib_+".COMPATC (test) values (1)");

      s.close();
      connection_.close();
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
      CommandCall cmd = new CommandCall(pwrSys_);

      // Delete the library
      deleteLibrary(cmd, testLib_); 
    }
    catch(Exception e)
    {
      e.printStackTrace(output_);
      System.out.println("Error occurred during cleanup of testcase.");
    }

    try {
    CommandCall c = new CommandCall();
    c.setSystem(pwrSys_);
    String command = "CHGUSRPRF "+userId_+" CCSID(37)";
    System.out.println("Running "+command); 
    c.run(command); 
    } catch (Exception e) { 
      e.printStackTrace(); 
    }

		/* Restore value back */ 
    if (qigc_ == false) {
	try 
	{
	    CommandCall cmd = new CommandCall(pwrSys_);
	    cmd.run("SETIGC CHANGE(*BOTH) VALUE(*NO)");
	} catch (Exception e) 
	{
	    System.out.println("Could not change SETIGC back to *NO ");
	    e.printStackTrace();
	}

    }



  }

  /**
   *Verify record format for table COMPAT.
   *<ul compact>
   *<li>Get the record format from the file.
   *<li>Read a record from the file.
   *<li>Write a record to the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format and corresponding fields will be correct.
   *<li>The data in the read record will be compatible with the fields in the record format.
   *<li>The data in the written record will be compatible with the columns in the table.
   *</ul>
  **/
  public void Var001()
  {
    // NOTE TO TESTER:
    // On IBM i, DBCS (graphic and vargraphic datatypes) defaults to CCSID 835.
    // When reading data from a RLA file, DDM converts from the file/table/column CCSID to the job user profile CCSID.
    // So beware - If your profile uses a CCSID like 37, then DDM will fail when converting to 835 to 37.  The error as seen in our testcase is:
    // "com.ibm.as400.access.AS400Exception: CPF4248 File COMPAT in library DDMSQLT not opened."
    // To fix this situation, simply change the user profile CCSID to 65535 to avoid conversion by DDM. 

    setVariation(1);
    SequentialFile f = null;
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, fileName_[0]);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      f = new SequentialFile(systemObject_, fileName_[0]);
      if (rfs.length != 1)
      {
        f.delete();
        failed("Wrong number of record formats returned: "+rfs.length);
        return;
      }
      if (rfs[0].getNumberOfFields() != 18)
      {
        f.delete();
        failed("Wrong number of fields in format: "+rfs[0].getNumberOfFields());
        return;
      }

      f.setRecordFormat(rfs[0]);

      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec = f.read(1);

      f.close();
      f.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record writeRec = rfs[0].getNewRecord();
      writeRec.setField("TEST", (new Integer(1)));
      f.write(writeRec);

      f.close();
      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec2 = f.read(2);
      f.close();
      f.delete();
      if (!readRec.toString().equals(readRec2.toString()))
      {
        failed("Incorrect format written.");
        return;
      }
    }
    catch(Exception e)
    {
      if (e instanceof AS400Exception)
      {
        AS400Message msg = ((AS400Exception)e).getAS400Message();
        if (msg.getID().equals("CPF4248"))
        {
          output_.println("Suggestion: Change the CCSID of user profile " + systemObject_.getUserId() + " to 65535, to prevent character conversion by DDM.");
        }
      }
      failed(e, "Unexpected exception");
      try
      {
        if (f != null) { 
          f.close();
          f.delete();
        }
      }
      catch(Exception e1) {}
      return;
    }
    succeeded();
  }

  /**
   *Verify record format for table COMPATN.
   *<ul compact>
   *<li>Get the record format from the file.
   *<li>Read a record from the file.
   *<li>Write a record to the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format and corresponding fields will be correct.
   *<li>The data in the read record will be compatible with the fields in the record format.
   *<li>The data in the written record will be compatible with the columns in the table.
   *</ul>
  **/
  public void Var002()
  {
    setVariation(2);
    SequentialFile f = null;
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, fileName_[1]);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      f = new SequentialFile(systemObject_, fileName_[1]);
      if (rfs.length != 1)
      {
        f.delete();
        failed("Wrong number of record formats returned: "+rfs.length);
        return;
      }
      if (rfs[0].getNumberOfFields() != 18)
      {
        f.delete();
        failed("Wrong number of fields in format: "+rfs[0].getNumberOfFields());
        return;
      }

      f.setRecordFormat(rfs[0]);

      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec = f.read(1);

      f.close();
      f.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record writeRec = rfs[0].getNewRecord();
      writeRec.setField("TEST", (new Integer(1)));
      f.write(writeRec);

      f.close();
      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec2 = f.read(2);
      f.close();
      f.delete();
      if (!readRec.toString().equals(readRec2.toString()))
      {
        failed("Incorrect format written.");
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        if (f!= null) { 
          f.close();
          f.delete();
        }
      }
      catch(Exception e1) {}
      return;
    }
    succeeded();
  }

  /**
   *Verify record format for table COMPATD.
   *<ul compact>
   *<li>Get the record format from the file.
   *<li>Read a record from the file.
   *<li>Write a record to the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format and corresponding fields will be correct.
   *<li>The data in the read record will be compatible with the fields in the record format.
   *<li>The data in the written record will be compatible with the columns in the table.
   *</ul>
  **/
  public void Var003()
  {
    setVariation(3);
    SequentialFile f = null;
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, fileName_[2]);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      f = new SequentialFile(systemObject_, fileName_[2]);
      if (rfs.length != 1)
      {
        f.delete();
        failed("Wrong number of record formats returned: "+rfs.length);
        return;
      }
      if (rfs[0].getNumberOfFields() != 18)
      {
        f.delete();
        failed("Wrong number of fields in format: "+rfs[0].getNumberOfFields());
        return;
      }

      f.setRecordFormat(rfs[0]);

      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec = f.read(1);

      f.close();
      f.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record writeRec = rfs[0].getNewRecord();
      writeRec.setField("TEST", (new Integer(1)));
      f.write(writeRec);

      f.close();
      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec2 = f.read(2);
      f.close();
      f.delete();

      if (!readRec.toString().equals(readRec2.toString()))
      {
        failed("Incorrect format written:\n  "+readRec+" !=\n  "+readRec2);
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        if (f != null) { 
          f.close();
          f.delete();
        }
      }
      catch(Exception e1) {}
      return;
    }
    succeeded();
  }
  /**
   *Verify record format for table COMPATC. Note that the time and timestamp
   *fields contain current values. These current values must fall within
   *10 minutes of each other across records for this variation to succeed.
   *<ul compact>
   *<li>Get the record format from the file.
   *<li>Read a record from the file.
   *<li>Write a record to the file.
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The record format and corresponding fields will be correct.
   *<li>The data in the read record will be compatible with the fields in the record format.
   *<li>The data in the written record will be compatible with the columns in the table.
   *</ul>
  **/
  public void Var004()
  {
    setVariation(4);
    SequentialFile f = null;
    boolean failed = false;
    String msg = "";
    try
    {
      AS400FileRecordDescription rd = new AS400FileRecordDescription(systemObject_, fileName_[3]);
      RecordFormat[] rfs = rd.retrieveRecordFormat();
      f = new SequentialFile(systemObject_, fileName_[3]);
      if (rfs.length != 1)
      {
        f.delete();
        failed("Wrong number of record formats returned: "+rfs.length);
        return;
      }
      if (rfs[0].getNumberOfFields() != 4)
      {
        f.delete();
        failed("Wrong number of fields in format: "+rfs[0].getNumberOfFields());
        return;
      }

      f.setRecordFormat(rfs[0]);

      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec = f.read(1);

      f.close();
      f.open(AS400File.READ_WRITE, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record writeRec = rfs[0].getNewRecord();
      writeRec.setField("TEST", (new Integer(1)));
      f.write(writeRec);

      f.close();
      f.open(AS400File.READ_ONLY, 0, AS400File.COMMIT_LOCK_LEVEL_NONE);
      Record readRec2 = f.read(2);
      f.close();
      f.delete();

      // Since these are "current" fields, if the times are close, that's
      // good enough.
      // col2 = date, col3 = time, col4 = timestamp
      Object dateData1 = readRec.getField("COL1");
      Object timeData1 = readRec.getField("COL2");
      Object timestampData1 = readRec.getField("COL3");
      String newDateString1 = dateData1.toString();
      String newTimeString1 = timeData1.toString();
      String newTimestampString1 = timestampData1.toString();

      Object dateData2 = readRec2.getField("COL1");
      Object timeData2 = readRec2.getField("COL2");
      Object timestampData2 = readRec2.getField("COL3");
      String newDateString2 = dateData2.toString();
      String newTimeString2 = timeData2.toString();
      String newTimestampString2 = timestampData2.toString();

      SimpleDateFormat timef = new SimpleDateFormat("HH.mm.ss");
      // Don't bother with milliseconds (capital S) in the format since we're checking
      // to see if the timestamps are within 10 minutes anyway.
      SimpleDateFormat timestampf = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");

      Date time1 = timef.parse(newTimeString1, new ParsePosition(0));
      Date time2 = timef.parse(newTimeString2, new ParsePosition(0));
      Date timestamp1 = timestampf.parse(newTimestampString1, new ParsePosition(0));
      Date timestamp2 = timestampf.parse(newTimestampString2, new ParsePosition(0));
      long millis1 = time1.getTime();
      long millis2 = time2.getTime();
      long millis3 = timestamp1.getTime();
      long millis4 = timestamp2.getTime();
      long timedif = java.lang.Math.abs(millis1-millis2);
      long timestampdif = java.lang.Math.abs(millis3-millis4);

      // Date: Must be equal
      if (!newDateString1.equals(newDateString2))
      {
        msg += "Incorrect date format: "+newDateString1+" != "+newDateString2+"\n";
        failed = true;
      }
      // Time: Within 30 minutes
      if (timedif > 1800000)
      {
        msg += "Incorrect time format ("+timedif+" ms): "+newTimeString1+" != "+newTimeString2+"\n";
        failed = true;
      }
      // Timestamp: Within 30 minutes
      if (timestampdif > 1800000)
      {
        msg += "Incorrect timestamp format ("+timestampdif+" ms): "+newTimestampString1+" != "+newTimestampString2+"\n";
        failed = true;
      }
      if (failed)
      {
        failed(msg);
        return;
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception");
      try
      {
        if (f != null) { 
          f.close();
          f.delete();
        }
      }
      catch(Exception e1) {}
      return;
    }
    succeeded();
  }
}


