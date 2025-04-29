///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPPrtObjTransInStrTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.PrintObject;
import com.ibm.as400.access.PrintObjectTransformedInputStream;
import com.ibm.as400.access.PrintParameterList;
import com.ibm.as400.access.RequestNotSupportedException;
import com.ibm.as400.access.SCS3812Writer;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileOutputStream;

import test.Testcase;

/**
This testcase requires an OS/400 V4R4 (or higher) system.

Testcase NPPrtObjTransInStrTestcase

Dependencies:
    The following objects must exist on the TEST AS/400 system:
<ul>
    <li> Workstation customizing object QSYS/QWPGIF.
    <li> Output queue NPJAVA/ACTTST.
</ul>

<p>This testcase verifies the following public
PrintObjectTransformedInputStream methods:
<ul>
<li> PrintObjectTransformedInputStream(SpooledFile, PrintParameterList)
<li> available()
<li> close()
<li> markSupported()
<li> read()
<li> read(byte[])
<li> read(byte[],int,int)
<li> skip(long)
</ul>
**/

public class NPPrtObjTransInStrTestcase
extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPPrtObjTransInStrTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }


    // Private data.
    private static final int    variations_ = 15;
    private static String       outQName_   = null;



/**
Constructor.
**/
    public NPPrtObjTransInStrTestcase (AS400 systemObject,
                                      Vector<String> variationsToRun,
                                      int runMode,
                                      FileOutputStream fileOutputStream)
    {
        super (systemObject, "NPPrtObjTransInStrTestcase", variations_,
            variationsToRun, runMode, fileOutputStream);
        outQName_ = "/QSYS.LIB/NPJAVA.LIB/ACTTST.OUTQ";
    }



/**
Constructor.
**/
    public NPPrtObjTransInStrTestcase (AS400 systemObject,
                                      Vector<String> variationsToRun,
                                      int runMode,
                                      FileOutputStream fileOutputStream,
                                      
                                      String password)
    {
        super (systemObject, "NPPrtObjTransInStrTestcase", variations_,
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

        if ((allVariations || variationsToRun_.contains ("15"))) {
            setVariation (15);
            Var015 ();
        }
    }



/**
Tests the available() method on a transformed input stream.
**/
    public void Var001()
    {
        try
        {
            // create 1 page spooled file
            SpooledFile splF = createSpooledFile(1,3000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    if (avail <= 0) {
                        failed("Expected positive number from available() but got back " + avail);
                    }
                    else {
                        // read avail - 100 bytes from the file and check available again
                        byte buf[] = new byte[avail - 100];
                        int read = is.read(buf);
                        int availNow = is.available();
                        if (read+availNow != avail) {
                            failed(" available() returning " + availNow + " instead of " + (avail-read));
                        }
                        else {
                            byte buf2[] = new byte[availNow];
                            read = is.read(buf2);
                            availNow = is.available();
                            if (availNow != 0) {
                                failed("Expected 0 from available() but got back " + avail);
                            }
                            else {
                                succeeded();
                            }
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(null) at this level");
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
Tests the close() method on a transformed input stream.
**/
    public void Var002()
    {
        try
        {
            // create 7 page spooled file
            SpooledFile splF = createSpooledFile(7,2000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // attempt to close the input stream
                    is.close();
                    succeeded();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests the close() method on an input stream that was already closed.
**/
    public void Var003()
    {
        try
        {
            // create 2 page spooled file
            SpooledFile splF = createSpooledFile(2,1500);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

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
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests the markSupported() method.  This method should always return false.
**/
    public void Var004()
    {
        try
        {
            // create 1 page spooled file
            SpooledFile splF = createSpooledFile(1,100);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get 'markSupported' value
                    boolean markS = is.markSupported();
                    if (markS) {
                        failed("markSupported returned true!");
                    }
                    else {
                        succeeded();
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var005()
    {
        try
        {
            // create 2 page spooled file
            SpooledFile splF = createSpooledFile(2,1234);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // read 1 byte
                    int i = 0;
                    i = is.read();

                    if (i == 71) {   // 71 represents ASCII 'G' for 'GIF'
                        succeeded();
                    }
                    else {
                        failed("Did not read the correct byte (G)");
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var006()
    {
        try
        {
            // create 4 page spooled file
            SpooledFile splF = createSpooledFile(4,222);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    byte[] buf = new byte[avail];
                    int read;

                    // read the entire page
                    read = is.read(buf,0, avail);

                    if (read != avail) {
                        failed("Did not read " + avail + " bytes as expected. Read " + read + " bytes instead");
                    }
                    else {
                        String s = new String(buf,0,3);
                        if (s.equals("GIF")) {
                           succeeded();
                        }
                        else {
                           failed("Did not transform data to GIF");
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var007()
    {
        try
        {
            // create 4 page spooled file
            SpooledFile splF = createSpooledFile(4,2323);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

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

                    if ((buf[10] == 71) && (success)) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var008()
    {
        try
        {
            // create 4 page spooled file
            SpooledFile splF = createSpooledFile(4,100);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPDEFAULT.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    int read;

                    // get the number of available bytes
                    int avail = is.available();
                    byte[] buf = new byte[10000];

                    // read the entire transformed spooled file
                    while (avail > 0) {
                        read = is.read(buf,0, avail);
                        avail = is.available();
                    }

                    // assertCondition: avail = 0

                    // try reading a byte using the 3 read methods...
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
Tests the read(byte[], int, int) method, trying to read more than is there.
**/
    public void Var009()
    {
        try
        {
            // create 4 page spooled file
            SpooledFile splF = createSpooledFile(4,222);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    byte[] buf = new byte[(2 * avail)];
                    int read;

                    // read the entire page
                    read = is.read(buf,0, (2 * avail));

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
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests generation of blocks of transformed data.
**/
    public void Var010()
    {
        try
        {
            // create 3 page spooled file
            SpooledFile splF = createSpooledFile(3,1200);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    int read;

                    // get the number of available bytes
                    int avail = is.available();
                    byte[] buf = new byte[avail];

                    // read all but one byte
                    read = is.read(buf, 0, avail - 1);
                    avail = is.available();

                    if (avail != 1) {
                       failed("Available returned " + avail + ": expecting 1. read="+read);
                    }
                    else {
                        int onebyte = is.read();

                        // should make next "block" transform
                        avail = is.available();
                        if (avail == 0) {
                            failed("Available returned 0: expecting > 0.");
                        }
                        else {
                            onebyte = is.read();
                            if (onebyte == 71) { // 71 represents ASCII 'G' for 'GIF'
                                succeeded();
                            }
                            else {
                                failed("Next block of data did not transform.");
                            }
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests the skip() method on a spooled file using a negative skip value.
**/
    public void Var011()
    {
        try
        {
            // create 1 page spooled file
            SpooledFile splF = createSpooledFile(1,555);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // skip -55 bytes
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
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests the skip() method on a spooled file, skipping past the end of file.
**/

    public void Var012()
    {
        try
        {
            // create 4 page spooled file
            SpooledFile splF = createSpooledFile(4,1000);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {
                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    if ( avail <= 0 ) {
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
                            skipped = is.skip(avail);
                            if (skipped != (avail -55)) {
                                failed(" Did not skip " + (avail - 55) + " bytes! Skipped " + skipped + " bytes instead!");
                            } else {
                                succeeded();
                            }
                        }
                    }

                    // close the input stream
                    is.close();
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var013()
    {
        try
        {
            // create 2 page spooled file
            SpooledFile splF = createSpooledFile(2,100);

            PrintParameterList printParms = new PrintParameterList();
            printParms.setParameter(PrintObject.ATTR_WORKSTATION_CUST_OBJECT, "/QSYS.LIB/QWPGIF.WSCST");
            printParms.setParameter(PrintObject.ATTR_MFGTYPE, "*WSCST");

            if (splF == null) {
                failed("Could not create spooled file.");
            }
            else {
                try {

                    // get a transformed input stream from the spooled file
                    PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);

                    // get the number of bytes available
                    int avail = is.available();

                    // close the input stream
                    is.close();

                    try {
                        is.skip(3333);
                        failed(" Did not generate IOException as expected on skip() after close() avail="+avail);
                    }
                    catch (IOException e) {
                        succeeded();
                    }

                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
    public void Var014()
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
                        PrintObjectTransformedInputStream is = splF.getTransformedInputStream(printParms);
                        failed("Constructor did not generate AS400Exception!");
                        is.close(); 
                    }
                    catch (AS400Exception e) {
                        succeeded();
                    }
                }
                catch (RequestNotSupportedException e) {
                    failed(e, "Network Print Server doesn't support getTransformedInputStream(printParms) at this level");
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
Tests ability to read and transform a *USERASCII spooled file.
**/
    public void Var015()
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
        byte[] buf = new byte[80];

        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, outQName_);

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

            // create the SCS writer
            SCS3812Writer scsWtr = new SCS3812Writer(outStream, 37, systemObject_); // @B1C

            // create SCS pages
            for (int i = 0; i < pages; i++) {
                for (int j = 0; j < 80; j++) {
                    buf[j] = (byte)'R';
                }

                buf[0] = (byte)'P';
                buf[1] = (byte)'a';
                buf[2] = (byte)'g';
                buf[3] = (byte)'e';
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



     SpooledFile createASCIISpooledFile(int pages, byte data[])
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {


        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

  
            // write some data
            outStream.write(data);

            // close the spooled file
            outStream.close();

            // return the new SpooledFile
            return outStream.getSpooledFile();
    }


}
