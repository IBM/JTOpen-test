///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtObjPageInStrTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.awt.FlowLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import com.ibm.as400.access.*;

import test.Testcase;

import javax.swing.*;

/**
This testcase requires an OS/400 V4R4 (or higher) system.

Testcase NPPrtObjPageInStrTestcase

Dependencies:
    The following objects must exist on the TEST AS/400 system:
<ul>
    <li> Workstation customizing object QSYS/QWPTXT.
    <li> Output queue NPJAVA/ACTTST.
</ul>

<p>This testcase verifies the following public
PrintObjectPageInputStream methods:
<ul>
<li> PrintObjectPageInputStream(SpooledFile, PrintParameterList)
<li> available()
<li> close()
<li> getNumberOfPages()
<li> isPagesEstimated()
<li> mark(int)
<li> markSupported()
<li> nextPage()
<li> previousPage();
<li> read()
<li> read(byte[])
<li> read(byte[],int,int)
<li> reset()
<li> selectPage(int)
<li> skip(long)
</ul>
**/

public class NPPrtObjPageInStrTestcase
extends Testcase
{


    // Private data.
    private static final int    variations_ = 35;
    private static String       outQName_   = null;



/**
Constructor.
**/
    public NPPrtObjPageInStrTestcase (AS400 systemObject,
                                      Vector variationsToRun,
                                      int runMode,
                                      FileOutputStream fileOutputStream)
    {
        super (systemObject, "NPPrtObjPageInStrTestcase", variations_,
            variationsToRun, runMode, fileOutputStream);
        outQName_ = "/QSYS.LIB/NPJAVA.LIB/ACTTST.OUTQ";
    }



/**
Constructor.
**/
    public NPPrtObjPageInStrTestcase (AS400 systemObject,
                                      Vector variationsToRun,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "NPPrtObjPageInStrTestcase", variations_,
            variationsToRun, runMode, fileOutputStream,
            password);
        outQName_ = "/QSYS.LIB/NPJAVA.LIB/ACTTST.OUTQ";
    }



/**
Runs the variations.
**/
    public void run ()
    {
        boolean allVariations = (variationsToRun_.size () == 0);

        if ((allVariations || variationsToRun_.contains ("1")) && runMode_ != ATTENDED) {
            setVariation (1);
            Var001 ();
        }

        if ((allVariations || variationsToRun_.contains ("2")) && runMode_ != ATTENDED) {
            setVariation (2);
            Var002 ();
        }

        if ((allVariations || variationsToRun_.contains ("3")) && runMode_ != ATTENDED) {
            setVariation (3);
            Var003 ();
        }

        if ((allVariations || variationsToRun_.contains ("4")) && runMode_ != ATTENDED) {
            setVariation (4);
            Var004 ();
        }

        if ((allVariations || variationsToRun_.contains ("5")) && runMode_ != ATTENDED) {
            setVariation (5);
            Var005 ();
        }

        if ((allVariations || variationsToRun_.contains ("6")) && runMode_ != ATTENDED) {
            setVariation (6);
            Var006 ();
        }

        if ((allVariations || variationsToRun_.contains ("7")) && runMode_ != ATTENDED) {
            setVariation (7);
            Var007 ();
        }

        if ((allVariations || variationsToRun_.contains ("8")) && runMode_ != ATTENDED) {
            setVariation (8);
            Var008 ();
        }

        if ((allVariations || variationsToRun_.contains ("9")) && runMode_ != ATTENDED) {
            setVariation (9);
            Var009 ();
        }

        if ((allVariations || variationsToRun_.contains ("10")) && runMode_ != ATTENDED) {
            setVariation (10);
            Var010 ();
        }

        if ((allVariations || variationsToRun_.contains ("11")) && runMode_ != ATTENDED) {
            setVariation (11);
            Var011 ();
        }

        if ((allVariations || variationsToRun_.contains ("12")) && runMode_ != ATTENDED) {
            setVariation (12);
            Var012 ();
        }

        if ((allVariations || variationsToRun_.contains ("13")) && runMode_ != ATTENDED) {
            setVariation (13);
            Var013 ();
        }

        if ((allVariations || variationsToRun_.contains ("14")) && runMode_ != ATTENDED) {
            setVariation (14);
            Var014 ();
        }

        if ((allVariations || variationsToRun_.contains ("15")) && runMode_ != ATTENDED) {
            setVariation (15);
            Var015 ();
        }

        if ((allVariations || variationsToRun_.contains ("16")) && runMode_ != ATTENDED) {
            setVariation (16);
            Var016 ();
        }

        if ((allVariations || variationsToRun_.contains ("17")) && runMode_ != ATTENDED) {
            setVariation (17);
            Var017 ();
        }

        if ((allVariations || variationsToRun_.contains ("18")) && runMode_ != ATTENDED) {
            setVariation (18);
            Var018 ();
        }

        if ((allVariations || variationsToRun_.contains ("19")) && runMode_ != ATTENDED) {
            setVariation (19);
            Var019 ();
        }

        if ((allVariations || variationsToRun_.contains ("20")) && runMode_ != ATTENDED) {
            setVariation (20);
            Var020 ();
        }

        if ((allVariations || variationsToRun_.contains ("21")) && runMode_ != ATTENDED) {
            setVariation (21);
            Var021 ();
        }

        if ((allVariations || variationsToRun_.contains ("22")) && runMode_ != ATTENDED) {
            setVariation (22);
            Var022 ();
        }

        if ((allVariations || variationsToRun_.contains ("23")) && runMode_ != ATTENDED) {
            setVariation (23);
            Var023 ();
        }

        if ((allVariations || variationsToRun_.contains ("24")) && runMode_ != ATTENDED) {
            setVariation (24);
            Var024 ();
        }

        if ((allVariations || variationsToRun_.contains ("25")) && runMode_ != ATTENDED) {
            setVariation (25);
            Var025 ();
        }

        if ((allVariations || variationsToRun_.contains ("26")) && runMode_ != ATTENDED) {
            setVariation (26);
            Var026 ();
        }

        if ((allVariations || variationsToRun_.contains ("27")) && runMode_ != ATTENDED) {
            setVariation (27);
            Var027 ();
        }

        if ((allVariations || variationsToRun_.contains ("28")) && runMode_ != ATTENDED) {
            setVariation (28);
            Var028 ();
        }

        if ((allVariations || variationsToRun_.contains ("29")) && runMode_ != ATTENDED) {
            setVariation (29);
            Var029 ();
        }

        if ((allVariations || variationsToRun_.contains ("30")) && runMode_ != ATTENDED) {
            setVariation (30);
            Var030 ();
        }

        if ((allVariations || variationsToRun_.contains ("31")) && runMode_ != ATTENDED) {
            setVariation (31);
            Var031 ();
        }

        if ((allVariations || variationsToRun_.contains ("32")) && runMode_ != ATTENDED) {
            setVariation (32);
            Var032 ();
        }

        if ((allVariations || variationsToRun_.contains ("33")) && runMode_ != ATTENDED) {
            setVariation (33);
            Var033 ();
        }

        if ((allVariations || variationsToRun_.contains ("34")) && runMode_ != ATTENDED) {
            setVariation (34);
            Var034 ();
        }

        if ((allVariations || variationsToRun_.contains ("35")) ) {
            setVariation (35);
            Var035 ();
        }
    }



/**
Tests the available() method on a one page input stream.
**/
    public void Var001()
    {
        try
        {
            // create 1 page, 3000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,3000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    if (avail != 3000) {
                        failed("Expected 3000 from available() but got back " + avail);
                    }
                    else {
                        // read 1000 bytes from the file and check available again
                        byte buf[] = new byte[1000];
                        int read = is.read(buf);
                        int availNow = is.available();
                        if (read+availNow != avail) {
                            failed(" available() returning " + availNow + " instead of " + (avail-read));
                        }
                        else {
                            succeeded();
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(null) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the available() method for a multiple-page input stream.
**/
    public void Var002()
    {
        try
        {
            // create 3 page, 4000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,4000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();
                    if (avail != 4000) {
                        failed("Expected 4000 from available() but got back " + avail);
                    }
                    else {

                        // select page two from input stream
                        is.selectPage(2);

                        // read 2565 bytes from the file and check available again
                        byte buf[] = new byte[2565];
                        int read = is.read(buf);
                        int availNow = is.available();
                        if (read+availNow != avail) {
                            failed(" available() returning " + availNow + " instead of " + (avail-read));
                        }
                        else {
                            succeeded();
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(null) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the close() method on a page input stream.
**/
    public void Var003()
    {
        try
        {
            // create 7 page, 12000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(7,12000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // attempt to close the input stream
                    is.close();
                    succeeded();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the close() method on a page input stream that was already closed.
**/
    public void Var004()
    {
        try
        {
            // create 5 page, 1500 bytes per page spooled file
            SpooledFile splF = createSpooledFile(5,1500);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // attempt the close
                    is.close();

                    // try to close it a second time, should get IOException
                    try {
                        is.close();
                        failed("No IOException when close() called twice!");
                    }
                    catch (IOException e) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the mark() method.
**/
    public void Var005()
    {
        try
        {
            // create 3 page, 1687 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,1687);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    byte[] buf1 = new byte[20];
                    byte[] buf2 = new byte[20];

                    // get number of bytes available for the page
                    int avail = is.available();

                    // mark it here
                    is.mark(avail);

                    // read the first 20 bytes
                    int read1 = is.read(buf1);

                    // reset the read pointer
                    is.reset();

                    // read the first 20 bytes again
                    int read2 = is.read(buf2);
                    if (read1 != read2) {
                        failed(" Did not read the same amount twice!");
                    }
                    else {
                        boolean success = true;
                        for (int i = 0; i<read1; i++) {
                            if (buf1[i] != buf2[i]) {
                                failed(" Did not read same bytes!");
                                success = false;
                                break;
                            }
                        }
                        if (success) {
                            succeeded();
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the markSupported() method.  This method should always return true.
**/
    public void Var006()
    {
        try
        {
            // create 2 page, 3000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,3000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get 'markSupported' value
                    boolean markS = is.markSupported();
                    if (markS) {
                        succeeded();
                    }
                    else {
                        failed("markSupported returned false!");
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests nextPage() method for a multiple-page input stream.
**/
    public void Var007()
    {
        try
        {
            // create 4 page, 538 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,538);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    byte buf[] = new byte[5];

                    // go to next page
                    try {
                        is.nextPage();
                    }
                    catch (IOException e) {
                        failed("nextPage() did not get to Page 2 data!");
                    }

                    // read the first 8 bytes
                    is.read(buf);

                    String pageNum = new String(buf);
                    if (pageNum.equals("Page2")) {
                        succeeded();
                    }
                    else {
                        failed("nextPage() did not get to Page 2 data!");
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests nextPage() method for a one page input stream.
**/
    public void Var008()
    {
        try
        {
            // create 1 page, 1100 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,1100);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");


            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    try
                    {
                        boolean worked = is.nextPage();
                        if (worked == false)
                           succeeded();
                        else
                           failed("nextPage() did not return false!");
                    }
                    catch (IOException e) {
                        failed(e, "Unexpected IOException generated by nextPage() method");
                    }
                    finally {
                        // close the input stream
                        is.close();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests nextPage() method for a closed input stream.
**/
    public void Var009()
    {
        try
        {
            // create 1 page, 1100 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,1100);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // close the input stream
                    is.close();

                    try {
                        is.nextPage();
                        failed("nextPage() did not generate IOException!");
                    }
                    catch (IOException e) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests previousPage() for a multiple-page input stream.
**/
    public void Var010()
    {
        try
        {
            // create 4 page, 500 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,500);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    byte buf[] = new byte[5];

                    // select page four of the input stream
                    is.selectPage(4);

                    try {
                        is.previousPage();
                    }
                    catch (IOException e) {
                        failed("previousPage() did not get to Page 3 data!");
                    }

                    // read the first 10 bytes
                    
                    is.read(buf);

                    String pageNum = new String(buf);
                    if (pageNum.equals("Page3")) {
                        succeeded();
                    }
                    else {
                        failed("previousPage() did not get to Page 3 data!");
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests previousPage() for a one page input stream.
**/
    public void Var011()
    {
        try
        {
            // create 1 page, 987 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,987);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    try
                    {
                        boolean worked = is.previousPage();
                        if (worked == false)
                           succeeded();
                        else
                           failed("previousPage() did not return false!");
                    }
                    catch (IOException e) {
                        failed(e, "Unexpected IOException generated by previousPage() method");
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests previousPage() for a closed input stream.
**/
    public void Var012()
    {
        try
        {
            // create 1 page, 987 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,987);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);
                    is.close();

                    try {
                        is.previousPage();
                        failed("previousPage() did not generate IOException!");
                    }
                    catch (IOException e) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the read() method.
**/
    public void Var013()
    {
        try
        {
            // create 2 page, 1234 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,1234);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available now
                    int avail = is.available();

                    if (avail != 1234) {
                        failed(" Did not get correct available amount of 1234; got " + avail);
                    }
                    else {
                        // read 1 byte
                        int i = is.read();
                        if (i == (int)'P') {
                            succeeded();
                        }
                        else {
                            failed(" Did not read the correct byte");
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the read(byte[]) method.
**/
    public void Var014()
    {
        try
        {
            // create 4 page, 2122 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,2122);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    byte[] buf = new byte[avail+avail];
                    int read;

                    // read the entire page
                    read = is.read(buf);
                    if (read != avail) {
                        failed("Did not read " + avail + " bytes as expected. Read " + read + " bytes instead");
                    }
                    else {
                        succeeded();
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the read(byte[],int,int) method.
**/
    public void Var015()
    {
        try
        {
            // create 4 page, 2323 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,2323);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available now
                    int avail = is.available();
                    if ( avail != 2323 ) {
                        failed(" Did not get correct available amount of 2323; got " + avail);
                    }
                    else {
                        // read 32 bytes into middle of buffer
                        byte[] buf = new byte[96];
                        int read = is.read(buf, 10, 32);

                        // close the input stream
                        is.close();

                        boolean success = true;
                        if (read != 32) {
                            failed("Did not read 32 bytes as expected! Read " + read + " bytes instead!");
                            success = false;
                        }
                        for (int i=0; success && i<10; i++) {
                            if (buf[i] != 0) {
                                failed("Buffer overwritten at byte " + i + " with character " + buf[i] );
                                success = false;
                                break;
                            }
                        }
                        for (int i=10+read; success && i<buf.length; i++) {
                            if (buf[i] != 0) {
                                failed("Buffer overwritten at byte " + i + " with character " + buf[i] );
                                success = false;
                                break;
                            }
                        }

                        if (success) {
                            succeeded();
                        }
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests all 3 read() methods return -1 at end of spooled file
**/
    public void Var016()
    {
        try
        {
            // create 4 page, 1200 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,1200);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    int read;

                    // get the number of available bytes
                    int avail = is.available();
                    byte[] buf = new byte[avail];

                    // read the entire resource
                    read = is.read(buf);
                    read = is.read();

                    if (read == -1) {
                        read = is.read(buf);
                        if (read == -1) {
                            read = is.read(buf, 1, 1);
                        }
                    }

                    if (read != -1) {
                        failed("Expected to read -1 but got back " + read + " instead");
                    }
                    else {
                        succeeded();
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Test the reset() method.
**/
    public void Var017()
    {
        try
        {
            // create 1 page, 4343 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,4343);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    int avail = is.available();
                    if ( avail != 4343 ) {
                        failed(" Did not get correct available amount of 4343; got " + avail);
                    }
                    else {
                        // mark the stream here
                        is.mark(64);

                        // read 64 bytes
                        byte[] buf = new byte[64];
                        is.read(buf);

                        // reset back to beginning
                        is.reset();

                        // check available is back to what it was
                        int availNow = is.available();

                        if (availNow != avail) {
                            failed(" Did not get back same value for available!");
                        }
                        else {
                            succeeded();
                        }
                    }
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the reset() methods by calling it after 'mark' has been invalidated
due to ReadLimit.
**/
    public void Var018()
    {
        try
        {
            // create 3 page, 2520 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,2520);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    int avail = is.available();
                    if ( avail != 2520 ) {
                        failed(" Did not get correct available amount of 2520; got " + avail);
                    }
                    else {
                        // mark the stream here
                        is.mark(40);

                        // read 64 bytes
                        byte[] buf = new byte[64];
                        is.read(buf);

                        try {
                           // reset back to beginning - should give an IOException
                            is.reset();
                            failed("reset() did not generate an IOException for an invalidated mark!");
                        }
                        catch (IOException e) {
                            succeeded();
                        }
                    }
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the reset() methods by calling it after 'mark' has been invalidated
due to a new page being selected.
**/
    public void Var019()
    {
        try
        {
            // create 3 page, 2520 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,2520);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    int avail = is.available();
                    if ( avail != 2520 ) {
                        failed(" Did not get correct available amount of 2520; got " + avail);
                    }
                    else {
                        // mark the stream here
                        is.mark(40);

                        // advance page
                        is.nextPage();

                        try {
                           // reset back to beginning - should give an IOException
                            is.reset();
                            failed("reset() did not generate an IOException for an invalidated mark!");
                        }
                        catch (IOException e) {
                            succeeded();
                        }

                        // close the input stream
                        is.close();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the reset() method by calling it after close() method has been invoked.
**/
    public void Var020()
    {
        try
        {
            // create 6 page, 1000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(6,1000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    byte[] buf = new byte[20];

                    // get an input stream for it
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the size of this resource
                    int avail = is.available();

                    // mark it here
                    is.mark(avail);

                    // read the first 20 bytes
                    is.read(buf);

                    // close the input stream
                    is.close();

                    try {
                        // reset the read pointer - should give us an IO Exception here
                        is.reset();
                        failed("reset() did not generate an IOException after close()");
                    }
                    catch (IOException e) {
                        succeeded();
                    }

                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the reset() method by calling it before a mark has been set.
**/
    public void Var021()
    {
        try
        {
            // create 3 page, 1687 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,1687);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    byte[] buf = new byte[20];

                    // get an input stream for it
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of available bytes
                    int avail = is.available();

                    int read;
                    // read the first 20 bytes
                    read = is.read(buf);

                    try {
                        // reset the read pointer - should give us an IO Exception here
                        is.reset();
                        failed("No IOException generated on reset() after close()");
                    }
                    catch (IOException e) {
                        succeeded(); // " Got this expected IOException: " + e);
                    }

                    // close the stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the selectPage() method.
**/
    public void Var022()
    {
        try
        {
            // create 4 page, 123 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,123);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    byte[] buf = new byte[5];
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    is.selectPage(3);

                    // read the first 10 bytes
                    int readBuf = is.read(buf);

                    String pageNum = new String(buf);
                    if (pageNum.equals("Page3")) {
                        succeeded();
                    }
                    else {
                        failed("Select Page did not get to Page 3 data!");
                    }

                    // close the input stream
                    is.close();

                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the selectPage() method by selecting a page out-of-range.
**/
    public void Var023()
    {
        try
        {
            // create 2 page, 2000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,2000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else
            {
                try
                {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    try
                    {
                        boolean worked = is.selectPage(5);
                        if (worked == false)
			    try {
                                is.selectPage(-4);
                                failed("Select negative page number did not generate IllegalArgumentException!");
                            }
                            catch (IllegalArgumentException iae) {
                                succeeded();
                            }
                        else
                            failed("selectPage() did not return false!");
                    } 
                    catch (IOException e) {
                        failed(e, "Unexpected IOException generated by selectPage() method");
                    }
                    finally {
                        // close the input stream
                        is.close();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the selectPage() method by calling it on a closed input stream.
**/
    public void Var024()
    {
        try
        {
            // create 4 page, 12000 bytes per page spooled file
            SpooledFile splF = createSpooledFile(3,3000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // close the input stream
                    is.close();

                    try {
                        is.selectPage(1);
                        failed("Select page for closed spooled file did not generate IOException!");
                    }
                    catch (IOException e) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the getNumberOfPages() method.
**/
    public void Var025()
    {
        try
        {
            // create 8 page, 998 bytes per page spooled file
            SpooledFile splF = createSpooledFile(8,998);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // retrieve the number of pages
                    int pages = is.getNumberOfPages();

                    if (pages != 8) {
                        failed("getNumberOfPages returned incorrect value!");
                    }
                    else {
                        succeeded();
                    }

                    // close the input stream
                    is.close();

                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the getNumberOfPages() method by calling it after the input stream has
been closed.
**/
    public void Var026()
    {
        try
        {
            // create 2 page, 130 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,130);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                 failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // close the input stream
                    is.close();

                    int pages = is.getNumberOfPages();
                    if (pages == 2) succeeded();
                    else failed("Number of pages returned incorrect");
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the skip() method on a spooled file.
**/
    public void Var027()
    {
        try
        {
            // create 1 page, 555 bytes per page spooled file
            SpooledFile splF = createSpooledFile(1,555);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // skip ahead 55 bytes
                    long skipped = is.skip(-55);
                    if (skipped != 0) {
                        failed(" Did not return 0 for negative skip! Skipped " + skipped + " bytes instead!");
                    }
                    else {
                        succeeded();
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }




/**
Tests the skip() method on a spooled file.
**/
    public void Var028()
    {
        try
        {
            // create 4 page, 5555 bytes per page spooled file
            SpooledFile splF = createSpooledFile(4,5555);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    if ( avail != 5555 ) {
                        failed(" Did not get correct available amount of 5555; got " + avail);
                    }
                    else {
                        // skip ahead 55 bytes
                        long skipped = is.skip(55);
                        if (skipped != 55) {
                            failed(" Did not skip 55 bytes! Skipped " + skipped + " bytes instead!");
                        }
                        else {
                            // skip beyond the end of the file
                            skipped = is.skip(6000);
                            if (skipped != 5500) {
                                failed(" Did not skip 5500 bytes! Skipped " + skipped + " bytes instead!");
                            } else {
                                succeeded();
                            }
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the skip() method.  The skip will be tested by skipping
beyond the end of the file and then tested by skipping only
a few bytes.
**/
    public void Var029()
    {
        try
        {
            // create 9 page, 500 bytes per page spooled file
            SpooledFile splF = createSpooledFile(9,500);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of available bytes
                    int avail = is.available();
                    long skipped;

                    // skip beyond the end of the page
                    skipped = is.skip(avail+1);

                    // close the input stream
                    is.close();

                    if (skipped != avail) {
                        failed("Did not skip " + avail + " bytes! Skipped " + skipped + " bytes instead!");
                    }
                    else {
                        is = splF.getPageInputStream(printParms);
                        skipped = is.skip(10);           // assumes resource is more than 10 bytes large
                        if (skipped != 10) {
                            failed("Did not skip 10 bytes! Skipped " + skipped + " bytes instead!");
                        }
                    }
                    try {
                        // reset the read pointer - should give us an IO Exception here
                        is.reset();
                        failed("No IOException generated on reset() after close()");
                    }
                    catch (IOException e) {
                        succeeded();
                    }
		    finally  {
                        is.close();
		    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the skip() method to generate an IOException by calling it
after the inputstream has been closed.
**/
    public void Var030()
    {
        try
        {
            // create 2 page, 4444 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,4444);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    // close the input stream
                    is.close();
                    if ( avail != 4444 ) {
                        failed(" Did not get correct available amount of 4444; got " + avail);
                    } else {
                        try {
                            is.skip(3333);
                            failed(" Did not generate IOException as expected on skip() after close()");
                        }
                        catch (IOException e) {
                            succeeded();
                        }
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests the isPagesEsimated() method when the number of pages is valid, not estimated.
**/
    public void Var031()
    {
        try
        {
            // create 2 page, 422 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,422);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    // is the number of pages estimated?
                    boolean estimated = is.isPagesEstimated();

                    // close the input stream
                    is.close();

                    if ( estimated == true ) {
                        failed("Did not get correct value of false for pages estimated");
                    }
                    else {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests 'finally' block of constructor when an exception is thrown.
**/
    public void Var032()
    {
        try
        {
            // create 2 page, 422 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,422);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QNOTTHERE.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    try {
                        // get a page input stream from the spooled file
                        PrintObjectPageInputStream is = splF.getPageInputStream(printParms);
                        failed("Constructor did not generate AS400Exception!");
                    }
                    catch (AS400Exception e) {
                        succeeded();
		    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests mark() when attempting to set readLimit beyond page size.
**/
    public void Var033()
    {
        try
        {
            // create 2 page, 422 bytes per page spooled file
            SpooledFile splF = createSpooledFile(2,422);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    try {
                        int avail = is.available();
                        is.mark(avail + 100);
                        is.reset();
                        byte[] buf = new byte[avail + 100];
                        int readbytes = is.read(buf, 0, avail + 100);
                        is.reset();
                        is.close();
                        if (readbytes != 422)
                            failed("Read incorrect number of bytes!");
                        else
                            succeeded();
                    }
                    catch (IOException ie) {
                      failed(ie, "Exception occurred using mark() and reset()");
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests getCurrentPageNumber().
**/
    public void Var034()
    {
        try
        {
            // create 6 page, 821 bytes per page spooled file
            SpooledFile splF = createSpooledFile(6,821);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPTXT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a page input stream from the spooled file
                    PrintObjectPageInputStream is = splF.getPageInputStream(printParms);

                    int page1 = is.getCurrentPageNumber();
                    is.selectPage(4);
                    is.nextPage();
                    int page2 = is.getCurrentPageNumber();
                    is.close();
                    if ((page1 == 1) && (page2 == 5))
                        succeeded();
                    else
                        failed("Failed! getCurrentPageNumber() did not return correct numbers.");

                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getPageInputStream(printParms) at this level");
                }
                finally {
                    splF.delete(); // delete spooled file
                }
            }
        }
        catch (Exception e) {
            failed(e, "Unexpected exception");
        }
    }



/**
Tests isPagesEstimated() and selectPage() method with an AFP spooled file
when number of pages are estimated.
**/
    public void Var035()
    {

	    notApplicable("Attended testcase");
    }



/////////////////////
// Private methods //
/////////////////////

    // This method creates an SCS spooled file
    private SpooledFile createSpooledFile(int pages, int pageSize)
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        int pagesRemaining = pages;
        byte[] buf = new byte[80];

        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, outQName_);

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

        if (outStream != null) {
            // create the SCS writer
            SCS3812Writer scsWtr = new SCS3812Writer(outStream, 37, systemObject_); // @B1C

            // create SCS pages
            for (int i = 0; i < pages; i++) {
                for (int j = 0; j < 80; j++) {
                    buf[j] = (byte)'R'; //@A1C
                }

                buf[0] = (byte)'P'; //@A1C
                buf[1] = (byte)'a'; //@A1C
                buf[2] = (byte)'g'; //@A1C
                buf[3] = (byte)'e'; //@A1C
                String s = new String(String.valueOf(i+1));
                buf[4] = (byte)s.charAt(0);

                int complines = pageSize/80;
                for (int k = 0; k < complines; k++) {
                    scsWtr.write(new String(buf));
                    scsWtr.newLine();
                }


                int val = pageSize - (complines * 80);
                scsWtr.write(new String(buf, 0, val)) ;
                scsWtr.newLine();
                scsWtr.endPage();
            }

            // close the writer
            scsWtr.close();

            // return the new SpooledFile
            return outStream.getSpooledFile();
        }
        else {
            return (SpooledFile)null;
        }
    }

}


