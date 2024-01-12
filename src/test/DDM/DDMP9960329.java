///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMP9960329.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import java.io.FileOutputStream;

import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

import java.sql.*;

/**
 *Testcase DDMP9960329. Verify the fix in P9960329. This is to make sure that:
 *<OL>
 *<LI>Test to make sure that if the DDM server is set to *KERBEROS
 *    and the client is not using Kerberos that an ArrayIndexOutOfBoundsException	
 *    is not thrown and a more "graceful" one is.
 *</OL>
**/
public class DDMP9960329 extends Testcase implements Runnable
{
  String testLib_ = null;

  /**
  Constructor.  This is called from the DDMTest constructor.
  **/
  public DDMP9960329(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream,
                         
                         String testLib,
                         String password,
                         AS400 pwrSys)
  {
    // Replace the third parameter (3) with the total number of variations
    // in this testcase.
    super(systemObject, "DDMP9960329", 1,
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
   *Verify no exceptions are thrown with a changed RCDLEN on CRTSRCPF.
   *Expected results:
   *<ul compact>
   *<li>The variation should return success.
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      if (pwrSys_ == null)
      {
        output_.println("pwrSys not specified.");
        return;
      }
      if (pwrSys_.getVRM() < AS400.generateVRM(5,2,0))
      {
        notApplicable("System must be V5R2 or higher.");
        return;
      }

      pwrSys_.connectService(AS400.RECORDACCESS);
      pwrSys_.disconnectService(AS400.RECORDACCESS);

      CommandCall cc = new CommandCall(pwrSys_);
      deleteLibrary(cc, testLib_);

      cc.run("CRTLIB "+testLib_);

      // Don't know of a way to retrieve the current DDM TCP attributes,
      // but it is unlikely that our test systems will ever be set to 
      // something other than *YES, so that's what we'll assume

      //
      // The following query will received the DDMTCPA
      //
      String oldDdmSetting="*UNKNOWN";
      AS400JDBCDriver d = new AS400JDBCDriver();
      Connection c = d.connect(pwrSys_); 

      Statement s = c.createStatement();
      ResultSet rs = s.executeQuery("SELECT CASE  WHEN LAND(DBXRSEC,X'E0') = X'00' THEN '*USRID'  WHEN LAND(DBXRSEC,X'E0') = X'20' THEN '*VLDONLY'  WHEN LAND(DBXRSEC,X'E0') = X'40' THEN '*USRIDPWD'  WHEN LAND(DBXRSEC,X'E0') = X'C0' THEN '*USRENCPWD' WHEN LAND(DBXRSEC,X'E0') = X'80' THEN '*ENCUSRPWD' WHEN LAND(DBXRSEC,X'E0') = X'A0' THEN '*KERBEROS'  ELSE '*UNKNOWN' END FROM qsys.qadbxrdbd WHERE DBXRMTN = '*LOCAL' ");
      if (rs.next()) {
	  oldDdmSetting = rs.getString(1);
	  System.out.println("INFO:  OLD DDMTCPA setting is "+oldDdmSetting); 
      }
      c.close();

      if (!cc.run("CHGDDMTCPA AUTOSTART(*SAME) PWDRQD(*KERBEROS)"))
      {
        AS400Message[] msgs = cc.getMessageList();
        for (int i=0; i<msgs.length; ++i)
        {
          output_.println(msgs[i]);
        }
        failed("CHGDDMTCPA command failed.");
        return;
      }
      try
      {
        pwrSys_.connectService(AS400.RECORDACCESS);
        failed("Expected exception did not occur.");
      }
      catch(ArrayIndexOutOfBoundsException aioobe)
      {
        failed(aioobe, "Fix didn't work, exception still occurred.");
      }
      catch(ServerStartupException sse)
      {
        succeeded();
      }
      catch(Exception f)
      {
        failed(f, "Wrong exception.");
      }
      finally
      {
        try { cc.run("CHGDDMTCPA AUTOSTART(*SAME) PWDRQD("+oldDdmSetting+")"); } catch(Exception x) {}
      }
    }
    catch(Exception e)
    {
      failed(e, "Unexpected exception.");
    }
  }
}
