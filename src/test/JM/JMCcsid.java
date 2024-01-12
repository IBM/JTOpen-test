///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMCcsid.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JM;

import utilities.*;
import com.ibm.as400.access.AS400;

import test.JMTest;
import test.Testcase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMCcsid.  This tests the following
methods of the ToolboxJarMaker class:

<ul compact>
<li>getCCSIDs
<li>getCCSIDsExcluded
<li>setCCSIDs
<li>setCCSIDsExcluded
</ul>
**/

public class JMCcsid
extends Testcase
{
  private static final boolean DEBUG = false;

  Vector allCCSIDs_;  // list of all CCSID conversion tables

/**
Constructor.
**/
    public JMCcsid (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMCcsid",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
     Vector c = new Vector (22);
     c.add ("com/ibm/as400/access/ConvTable1027.class");
     c.add ("com/ibm/as400/access/ConvTable1130.class");
     c.add ("com/ibm/as400/access/ConvTable1132.class");
     c.add ("com/ibm/as400/access/ConvTable13488.class");
     c.add ("com/ibm/as400/access/ConvTable1388.class");
     c.add ("com/ibm/as400/access/ConvTable28709.class");
     c.add ("com/ibm/as400/access/ConvTable290.class");
     c.add ("com/ibm/as400/access/ConvTable300.class");
     c.add ("com/ibm/as400/access/ConvTable423.class");
     c.add ("com/ibm/as400/access/ConvTable4933.class");
     c.add ("com/ibm/as400/access/ConvTable5026.class");
     c.add ("com/ibm/as400/access/ConvTable5035.class");
     c.add ("com/ibm/as400/access/ConvTable61952.class");
     c.add ("com/ibm/as400/access/ConvTable833.class");
     c.add ("com/ibm/as400/access/ConvTable834.class");
     c.add ("com/ibm/as400/access/ConvTable835.class");
     c.add ("com/ibm/as400/access/ConvTable836.class");
     c.add ("com/ibm/as400/access/ConvTable837.class");
     c.add ("com/ibm/as400/access/ConvTable880.class");
     c.add ("com/ibm/as400/access/ConvTable930.class");
     c.add ("com/ibm/as400/access/ConvTable933.class");
     c.add ("com/ibm/as400/access/ConvTable937.class");
     allCCSIDs_ = new Vector (c.size ());
     JMTest.copyList (c, allCCSIDs_);
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
    }


//--------------------------------------------------------------------
// Variations for setCCSIDs, getCCSIDs :


/**
 setCCSIDs - Arg is null. Should throw an exception.
 **/
    public void Var001 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.setCCSIDs (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "ccsidList");
      }
    }

/**
 setCCSIDs - List contains a null entry. Should throw an exception.
 **/
    public void Var002 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (null);
        jm.setCCSIDs (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "CCSID");
      }
    }

/**
 getCCSIDs - No prior setCCSIDs. Should return an empty list.
 **/
    public void Var003 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector outList = jm.getCCSIDs ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDs - Zero-length list. getCCSIDs should return an empty list.
 **/
    public void Var004 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector ();
        jm.setCCSIDs (inList);
        Vector outList = jm.getCCSIDs ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDs - List contains a non-numeric entry. Should throw an exception.
 **/
    public void Var005 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add ("12345");  // String instead of Integer
        jm.setCCSIDs (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "CCSID (object of class java.lang.String)");
      }
    }

/**
 setCCSIDs - List contains a negative entry. Should throw an exception.
 **/
    public void Var006 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (-4));
        jm.setCCSIDs (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException", "CCSID (-4)");
      }
    }

/**
 setCCSIDs - List contains an existing CCSID and a nonexistent CCSID.
 Only the ConvTable's for the existing CCSID should get included.
 **/
    public void Var007 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (1027));
        inList.add (new Integer (9999));
        jm.setCCSIDs (inList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        output_.println ("(NOTE TO TESTER: Please ignore any warnings about CCSID 9999)");
        jm.makeJar (JMTest.TOOLBOX_JAR);
        // Verify that only the correct files got copied.
        entryList.add ("com/ibm/as400/access/ConvTable1027.class");
        entryList.add ("com/ibm/as400/access/ConvTable13488.class");
        Vector notExpected = new Vector (allCCSIDs_.size ());
        JMTest.copyList (allCCSIDs_, notExpected);
        notExpected.remove ("com/ibm/as400/access/ConvTable1027.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable13488.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          entryList, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDs - List contains two existing CCSIDs.
 Only the ConvTable's for the two CCSIDs should get included.
 **/
    public void Var008 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (930));  //@A1c
        inList.add (new Integer (933));  //@A1c
        jm.setCCSIDs (inList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.
        // Note that ccsid 930 has associated ccsids 290 and 300.    @A1a
        // Note that ccsid 933 has associated ccsids 833 and 834.    @A1a
        entryList.add ("com/ibm/as400/access/ConvTable930.class");
        entryList.add ("com/ibm/as400/access/ConvTable290.class");
        entryList.add ("com/ibm/as400/access/ConvTable300.class");
        entryList.add ("com/ibm/as400/access/ConvTable933.class");
        entryList.add ("com/ibm/as400/access/ConvTable833.class");
        entryList.add ("com/ibm/as400/access/ConvTable834.class");
        entryList.add ("com/ibm/as400/access/ConvTable13488.class");

        Vector notExpected = new Vector (allCCSIDs_.size ());
        JMTest.copyList (allCCSIDs_, notExpected);
        notExpected.remove ("com/ibm/as400/access/ConvTable930.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable290.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable300.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable933.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable833.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable834.class");
        notExpected.remove ("com/ibm/as400/access/ConvTable13488.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          entryList, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }




//---------------------------------------------------------------------
// Variations for setCCSIDsExcluded, getCCSIDsExcluded :


/**
 setCCSIDsExcluded - Arg is null. Should throw an exception.
 **/
    public void Var009 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        jm.setCCSIDsExcluded (null);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "ccsidList");
      }
    }

/**
 setCCSIDsExcluded - List contains a null entry. Should throw an exception.
 **/
    public void Var010 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (null);
        jm.setCCSIDsExcluded (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "NullPointerException", "CCSID");
      }
    }

/**
 getCCSIDsExcluded - No prior setCCSIDsExcluded. Should return an empty list.
 **/
    public void Var011 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector outList = jm.getCCSIDsExcluded ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - Zero-length list.
 getCCSIDsExcluded should return an empty list.
 **/
    public void Var012 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector ();
        jm.setCCSIDsExcluded (inList);
        Vector outList = jm.getCCSIDsExcluded ();
        assertCondition (outList.size () == 0);
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - List contains a non-numeric entry. Should throw an exception.
 **/
    public void Var013 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add ("12345");  // String instead of Integer
        jm.setCCSIDsExcluded (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException",
                           "CCSID (object of class java.lang.String)");
      }
    }

/**
 setCCSIDsExcluded - List contains a negative entry. Should throw an exception.
 **/
    public void Var014 ()
    {
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (-4));
        jm.setCCSIDsExcluded (inList);
        failed ("Didn't throw exception.");
      }
      catch (Exception e) {
        assertExceptionIs (e, "IllegalArgumentException", "CCSID (-4)");
      }
    }

/**
 setCCSIDsExcluded - List contains an existing CCSID and a nonexistent CCSID.
 Only the ConvTable's for the existing CCSID should get excluded.
 **/
    public void Var015 ()
    {
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (1027));
        inList.add (new Integer (9999));
        jm.setCCSIDsExcluded (inList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        output_.println ("(NOTE TO TESTER: Please ignore any warnings about CCSIDs 1027 and 9999)");
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got excluded.
        Vector expected = new Vector (allCCSIDs_.size ());
        JMTest.copyList (allCCSIDs_, expected);
        expected.remove ("com/ibm/as400/access/ConvTable1027.class");
        Vector notExpected = new Vector (1);
        notExpected.add ("com/ibm/as400/access/ConvTable1027.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          expected, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - List contains two existing CCSIDs.
 Only the ConvTable's for the two CCSIDs should get excluded.
 **/
    public void Var016 ()
    {
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (new Integer (1027));
        inList.add (new Integer (835));
        jm.setCCSIDsExcluded (inList);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        output_.println ("(NOTE TO TESTER: Please ignore any warnings about CCSIDs 1027 and 835)");
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got excluded.
        Vector expected = new Vector (allCCSIDs_.size ());
        JMTest.copyList (allCCSIDs_, expected);
        expected.remove ("com/ibm/as400/access/ConvTable1027.class");
        expected.remove ("com/ibm/as400/access/ConvTable835.class");
        Vector notExpected = new Vector (2);
        notExpected.add ("com/ibm/as400/access/ConvTable1027.class");
        notExpected.add ("com/ibm/as400/access/ConvTable835.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          expected, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - Prior setCCSIDs with no entries in common.
 Only the ConvTable's for the included CCSIDs should get excluded.
 **/
    public void Var017 ()
    {
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList1 = new Vector (1);
        inList1.add (new Integer (1027));
        jm.setCCSIDs (inList1);
        Vector inList2 = new Vector (1);
        inList2.add (new Integer (835));
        jm.setCCSIDsExcluded (inList2);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got excluded.
        Vector expected = new Vector (1);
        expected.add ("com/ibm/as400/access/ConvTable1027.class");
        Vector notExpected = new Vector (1);
        notExpected.add ("com/ibm/as400/access/ConvTable835.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          expected, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - Prior setCCSIDs with one entry in common.
 The common CCSID should end up getting included.
 **/
    public void Var018 ()
    {
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList1 = new Vector (2);
        inList1.add (new Integer (1027));
        inList1.add (new Integer (61952));
        jm.setCCSIDs (inList1);
        Vector inList2 = new Vector (2);
        inList2.add (new Integer (835));
        inList2.add (new Integer (61952));
        output_.println ("(NOTE TO TESTER: Please ignore warning about CCSID 61952)");
        jm.setCCSIDsExcluded (inList2);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("com/ibm/as400/access/ConvTable.class");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got excluded.
        Vector expected = new Vector (2);
        expected.add ("com/ibm/as400/access/ConvTable1027.class");
        expected.add ("com/ibm/as400/access/ConvTable61952.class");
        Vector notExpected = new Vector (1);
        notExpected.add ("com/ibm/as400/access/ConvTable835.class");
        assertCondition (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                          expected, true) &&
                JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                             notExpected));
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDsExcluded - Prior setCCSIDs with one entry in common.
 Should see a warning message that a CCSID was specified on both lists.
 **/
    public void Var019 (int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) return;
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList1 = new Vector (2);
        inList1.add (new Integer (1027));
        inList1.add (new Integer (61952));
        jm.setCCSIDs (inList1);
        Vector inList2 = new Vector (2);
        inList2.add (new Integer (835));
        inList2.add (new Integer (61952));
        jm.setCCSIDsExcluded (inList2);

          succeeded ();
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
 setCCSIDs - Prior setCCSIDsExcluded with one entry in common.
 Should see a warning message that a CCSID was specified on both lists.
 **/
    public void Var020 (int runMode)
    {
      if (runMode != ATTENDED && runMode != BOTH) return;
      if (DEBUG) printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList2 = new Vector (2);
        inList2.add (new Integer (835));
        inList2.add (new Integer (61952));
        Vector inList1 = new Vector (2);
        inList1.add (new Integer (1027));
        inList1.add (new Integer (61952));
        jm.setCCSIDs (inList1);
        jm.setCCSIDsExcluded (inList2);

        
          succeeded ();
        
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

}



