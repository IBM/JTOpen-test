///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9936798.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

import java.sql.*;

/**
 *Testcase DDMP9936798. Verify one of the many fixes in P9936798. This is to make sure that:
 *<OL>
 *<LI>An SQLException: Internal driver error.(CCSID -3312 does not occur when accessing a table
 *    with fields tag with CCSIDs greater than 32767 (in this case, 62224).
 *    This is actually a JDBC test, but since I have a test for it, it was easier to put it in
 *    the DDM bucket than to add it to the JDBC bucket. See Susan Funk for more information.
 *</OL>
**/
public class DDMP9936798 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DDMP9936798";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DDMTest.main(newArgs); 
   }
  String testLib_ = null;
  AS400 pwrSys_;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9936798(AS400            systemObject,
                         Vector<String> variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         String password,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9936798", 1,
          variationsToRun, runMode, fileOutputStream, password);
    testLib_ = testLib;
    pwrSys_ = pwrSys;
  }


  /**
    Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")) &&
        runMode_ != ATTENDED)
    {
      setVariation(1);
      Var001();
    }
    systemObject_.disconnectAllServices();
  }


  /**
   *Verify an SQLException is not thrown for field with CCSID 62224.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    // Setup... end commitment control
    try
    {
      AS400File.endCommitmentControl(pwrSys_);
    }
    catch(Exception e)
    {
      output_.println("Warning: Unable to end commitment control: "+e.toString());
    }
    try
    {
      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc, testLib_); 
      if (!cc.run("CRTLIB "+testLib_))
      {
        output_.println("Warning: Unable to create library "+testLib_+": "+cc.getMessageList()[0].toString());
      }

      if (! cc.run("GRTOBJAUT OBJ("+testLib_+") OBJTYPE(*LIB) USER(JAVA) AUT(*ALL)")) {
	  output_.println("Unable to grant permission to library "+testLib_+": "+cc.getMessageList()[0].toString());

      } 

      RecordFormat rf = new RecordFormat("MYRF");
      CharacterFieldDescription cfd = new CharacterFieldDescription(new AS400Text(10, 62224), "FLD1");
      cfd.setCCSID("62224");
      rf.addFieldDescription(cfd);

      SequentialFile sf = new SequentialFile(systemObject_, "/QSYS.LIB/"+testLib_+".LIB/P9936798.FILE/TESTPTF.MBR");
      sf.create(rf, "Testing V5R1 GA PTF fix");
      sf.close();
      sf.open();
      Record r = rf.getNewRecord();
      r.setField(0, "Test");
      sf.write(r);
      sf.close();

      AS400JDBCDriver driver = new AS400JDBCDriver();
      Connection c = driver.connect(systemObject_); 
      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT * FROM "+testLib_+".P9936798");

      // Get the data and the meta data. We better not get an exception!
      while(rs.next())
      {
        rs.getString(1);
      }
      ResultSetMetaData md = rs.getMetaData();
      for (int i=0; i<md.getColumnCount(); ++i)
      {
        md.getColumnName(i+1);
      }
      succeeded();
    }
    catch(SQLException e)
    {
      String s = e.getMessage().toUpperCase();
      if (s.indexOf("INTERNAL DRIVER ERROR") > -1 &&
          s.indexOf("CCSID") > -1)
      {
        failed(e, "Code is not fixed!");
      }
      else
      {
        failed(e, "Unable to verify existence of fix.");
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
}
