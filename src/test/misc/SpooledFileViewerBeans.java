///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SpooledFileViewerBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.misc;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Exception;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.OutputQueue;
import com.ibm.as400.access.SCS3812Writer;
import com.ibm.as400.access.SpooledFile;
import com.ibm.as400.access.SpooledFileOutputStream;
import com.ibm.as400.vaccess.ErrorEvent;
import com.ibm.as400.vaccess.ErrorListener;
import com.ibm.as400.vaccess.SpooledFileViewer;

import test.Testcase;



/**
Testcase SpooledFileViewerBeans.

<p>This tests the following SpooledFileViewer methods:
<ul>
<li>serialization
<li>addErrorListener()
<li>removeErrorListener()
<li>addPropertyChangeListener()
<li>removePropertyChangeListener()
<li>addVetoableChangeListener()
<li>removeVetoableChangeListener()
<li>addWorkingListener()
<li>removeWorkingListener()
</ul>

**/
@SuppressWarnings("deprecation")

public class SpooledFileViewerBeans
extends Testcase
{


    // Private data.
    private static final int    variations_ = 28;
    private static String       outQName_   = null;


/**
Constructor.
**/
    public SpooledFileViewerBeans (AS400 systemObject,
                          Vector<String> variationsToRun,
                          int runMode,
                          FileOutputStream fileOutputStream)
    {
        super (systemObject, "SpooledFileViewerBeans", variations_,
            variationsToRun, runMode, fileOutputStream);
            outQName_ = "/QSYS.LIB/NPJAVA.LIB/ACTTST.OUTQ";
    }



/**
Constructor.
**/
    public SpooledFileViewerBeans (AS400 systemObject,
                          Vector<String> variationsToRun,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                                 String password)
    {
        super (systemObject, "SpooledFileViewerBeans", variations_,
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

    }



/**
Serialization - when no properties have been set.
**/
    public void Var001 ()
    {
    	notApplicable("Attended testcase");
    	
    }



/**
Serialization - when the properties have been set.
**/
    public void Var002 ()
    {
	    	notApplicable("Attended testcase");
	    		    
       
	}



/**
Listens for error events.
**/
    private class ErrorListener_
    implements ErrorListener
    {
        public ErrorEvent lastEvent_ = null;
        public void errorOccurred (ErrorEvent event)
        {
            lastEvent_ = event;
        }
    }



/**
addErrorListener() - Test that adding a null listener causes an
exception.
**/
    public void Var003 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.addErrorListener(null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addErrorListener() - Test that error events are received.
**/
    public void Var004 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            ErrorListener_ listener = new ErrorListener_();
            splfv.addErrorListener(listener);
            splfv.load();
            assertCondition (listener.lastEvent_ != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removeErrorListener() - Test that removing a null listener causes an
exception.
**/
    public void Var005 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.removeErrorListener(null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeErrorListener() - Test that error events are no longer received.
**/
    public void Var006 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            ErrorListener_ listener = new ErrorListener_();
            splfv.addErrorListener(listener);
            splfv.removeErrorListener(listener);
            splfv.setCurrentPage(100);
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
Listens for property change events.
**/
    private class PropertyChangeListener_
    implements PropertyChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void propertyChange (PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }



/**
addPropertyChangeListener() - Test that adding a null listener causes an
exception.
**/
    public void Var007 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.addPropertyChangeListener (null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the currentPage property is changed.
**/
    public void Var008 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf, 1);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            splfv.addPropertyChangeListener (listener);
            splfv.setCurrentPage(3);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("currentPage"))
                    && (listener.lastEvent_.getOldValue ().equals (new Integer(1)))
                    && (listener.lastEvent_.getNewValue ().equals (new Integer(3))));

            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the numberOfPages property is changed.
**/
    public void Var009 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase VNPSpooledFileViewerTestcase.
        assertCondition (true);
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the numberOfPagesEstimated property is changed.
**/
    public void Var010 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase VNPSpooledFileViewerTestcase.
        assertCondition (true);
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the spooledFile property is changed.
**/
    public void Var011 ()
    {
        try {
            SpooledFile sf = createSpooledFile();
            SpooledFile sf2 = createSpooledFileTwo();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            splfv.addPropertyChangeListener (listener);
            splfv.setSpooledFile(sf2);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("spooledFile"))
                    && (listener.lastEvent_.getOldValue ().equals (sf))
                    && (listener.lastEvent_.getNewValue ().equals (sf2)));
            sf.delete();  // cleanup
            sf2.delete(); // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the paperSize property is changed.
**/
    public void Var012 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            splfv.addPropertyChangeListener (listener);
            splfv.setPaperSize(SpooledFileViewer.A3);
            splfv.setPaperSize(SpooledFileViewer.A5);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("paperSize"))
                    && (listener.lastEvent_.getOldValue ().equals (new Integer(SpooledFileViewer.A3)))
                    && (listener.lastEvent_.getNewValue ().equals (new Integer(SpooledFileViewer.A5))));
            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addPropertyChangeListener() - Test that a property change event is received
when the viewingFidelity property is changed.
**/
    public void Var013 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            PropertyChangeListener_ listener = new PropertyChangeListener_ ();
            splfv.addPropertyChangeListener(listener);
            splfv.setViewingFidelity(SpooledFileViewer.CONTENT_FIDELITY);
            splfv.setViewingFidelity(SpooledFileViewer.ABSOLUTE_FIDELITY);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("viewingFidelity"))
                    && (listener.lastEvent_.getOldValue ().equals(new Integer(SpooledFileViewer.CONTENT_FIDELITY)))
                    && (listener.lastEvent_.getNewValue ().equals(new Integer(SpooledFileViewer.ABSOLUTE_FIDELITY))));
            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removePropertyChangeListener() - Test that removing a null listener causes an
exception.
**/
    public void Var014 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.removePropertyChangeListener(null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removePropertyChangeListener() - Test that property change events are no
longer received.
**/
    public void Var015 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

            SpooledFileViewer splfv = new SpooledFileViewer(sf, 1);
            PropertyChangeListener_ listener = new PropertyChangeListener_();
            splfv.addPropertyChangeListener(listener);
            splfv.removePropertyChangeListener(listener);
            splfv.setCurrentPage(5);
            assertCondition (listener.lastEvent_ == null);

            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Listens for vetoable change events.
**/
    private class VetoableChangeListener_
    implements VetoableChangeListener
    {
        private PropertyChangeEvent lastEvent_ = null;
        public void vetoableChange (PropertyChangeEvent event)
        {
            lastEvent_ = event;
        }
    }



/**
addVetoableChangeListener() - Test that adding a null listener causes an
exception.
**/
    public void Var016 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.addVetoableChangeListener(null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the currentPage property is changed.
**/
    public void Var017 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf, 1);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            splfv.addVetoableChangeListener (listener);
            splfv.setCurrentPage(3);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("currentPage"))
                    && (listener.lastEvent_.getOldValue ().equals (new Integer(1)))
                    && (listener.lastEvent_.getNewValue ().equals (new Integer(3))));

            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the numberOfPages property is changed.
**/
    public void Var018 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase SpooledFileViewerTestcase.
        assertCondition (true);
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the numberOfPagesEstimated property is changed.
**/
    public void Var019 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase SpooledFileViewerTestcase.
        assertCondition (true);
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the spooledFile property is changed.
**/
    public void Var020 ()
    {
        try {
            SpooledFile sf = createSpooledFile();
            SpooledFile sf2 = createSpooledFileTwo();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            splfv.addVetoableChangeListener (listener);
            splfv.setSpooledFile(sf2);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("spooledFile"))
                    && (listener.lastEvent_.getOldValue ().equals (sf))
                    && (listener.lastEvent_.getNewValue ().equals (sf2)));

            sf.delete();  // cleanup
            sf2.delete(); // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the paperSize property is changed.
**/
    public void Var021 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            splfv.addVetoableChangeListener (listener);
            splfv.setPaperSize(SpooledFileViewer.A3);
            splfv.setPaperSize(SpooledFileViewer.A5);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("paperSize"))
                    && (listener.lastEvent_.getOldValue ().equals (new Integer(SpooledFileViewer.A3)))
                    && (listener.lastEvent_.getNewValue ().equals (new Integer(SpooledFileViewer.A5))));

            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
addVetoableChangeListener() - Test that a vetoable change event is received
when the viewingFidelity property is changed.
**/
    public void Var022 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

	        SpooledFileViewer splfv = new SpooledFileViewer(sf);
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            splfv.addVetoableChangeListener(listener);
            splfv.setViewingFidelity(SpooledFileViewer.CONTENT_FIDELITY);
            splfv.setViewingFidelity(SpooledFileViewer.ABSOLUTE_FIDELITY);
            assertCondition ((listener.lastEvent_.getPropertyName ().equals ("viewingFidelity"))
                    && (listener.lastEvent_.getOldValue ().equals (new Integer(SpooledFileViewer.CONTENT_FIDELITY)))
                    && (listener.lastEvent_.getNewValue ().equals (new Integer(SpooledFileViewer.ABSOLUTE_FIDELITY))));

            sf.delete();  // cleanup
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
removeVetoableChangeListener() - Test that removing a null listener causes an
exception.
**/
    public void Var023 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer ();
            splfv.removeVetoableChangeListener (null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeVetoableChangeListener() - Test that vetoable change events are no
longer received.
**/
    public void Var024 ()
    {
        try {
            SpooledFile sf = createSpooledFile();

            SpooledFileViewer splfv = new SpooledFileViewer ();
            VetoableChangeListener_ listener = new VetoableChangeListener_ ();
            splfv.addVetoableChangeListener (listener);
            splfv.removeVetoableChangeListener (listener);
            splfv.setSpooledFile(sf);
            assertCondition (listener.lastEvent_ == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
Listens for working events.
**/
//    private class WorkingListener_
//    implements WorkingListener
//    {
//        public WorkingEvent lastEvent_ = null;
//        public void startWorking (WorkingEvent event)   { lastEvent_ = event; }
//        public void stopWorking (WorkingEvent event)    { lastEvent_ = event; }
//        
//    }



/**
addWorkingListener() - Test that adding a null listener causes an
exception.
**/
    public void Var025 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer();
            splfv.addWorkingListener (null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
addWorkingListener() - Test that working events are received.
**/
    public void Var026 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase.
        assertCondition (true);
    }



/**
removeWorkingListener() - Test that removing a null listener causes an
exception.
**/
    public void Var027 ()
    {
        try {
            SpooledFileViewer splfv = new SpooledFileViewer ();
            splfv.removeWorkingListener (null);
            failed ("Did not throw exception.");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



/**
removeWorkingListener() - Test that working events are no longer received.
**/
    public void Var028 ()
    {
        // We currently cannot test this directly because
        // there is no programmatic way to force it.
        // However, it is tested indirectly in the attended
        // testcase VNPOutputViewActionTestcase.
        assertCondition (true);
    }


/////////////////////
// Private methods //
/////////////////////

    // This method creates a spooled file with 5 pages
    private SpooledFile createSpooledFile()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {
        // create an output queue object using valid system name and output queue name
        OutputQueue outQ = new OutputQueue(systemObject_, outQName_);

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, outQ);

        // create the SCS writer
        SCS3812Writer scsWtr = new SCS3812Writer(outStream, 37);

        // Write the contents of the spool file.
        scsWtr.write("This is test page 1 (ONE)");
        scsWtr.endPage();
        scsWtr.write("This is test page 2 (TWO)");
        scsWtr.endPage();
        scsWtr.write("This is test page 3 (THREE)");
        scsWtr.endPage();
        scsWtr.write("This is test page 4 (FOUR)");
        scsWtr.endPage();
        scsWtr.write("This is test page 5 (FIVE)");
        scsWtr.endPage();

        // close the writer
        scsWtr.close();

        // return the new SpooledFile
        return outStream.getSpooledFile();

    } // end createSpooledFile



    // This method creates a spooled file with 2 pages
    private SpooledFile createSpooledFileTwo()
      throws InterruptedException,
             IOException,
             AS400Exception,
             AS400SecurityException,
             ErrorCompletingRequestException
    {

        // create a spooled file output stream
        SpooledFileOutputStream outStream = new SpooledFileOutputStream(systemObject_, null, null, null);

        // create the SCS writer
        SCS3812Writer scsWtr = new SCS3812Writer(outStream, 37);

        // Write the contents of the spool file.
        scsWtr.setLeftMargin(1.0);
        scsWtr.absoluteVerticalPosition(6);
        scsWtr.setFont(SCS3812Writer.FONT_COURIER_BOLD_5);
        scsWtr.write("                     Java Printing");
        scsWtr.newLine();
        scsWtr.newLine();
        scsWtr.setCPI(10);
        scsWtr.write("This document was created using the AS/400 Java Toolbox.");
        scsWtr.newLine();
        scsWtr.setFont(SCS3812Writer.FONT_COURIER_BOLD_10);
        scsWtr.newLine();
        scsWtr.write("This is test page 1 (ONE)");
        scsWtr.endPage();
        scsWtr.write("This is test page 2 (TWO)");
        scsWtr.endPage();

        // close the writer
        scsWtr.close();

        // return the new SpooledFile
        return outStream.getSpooledFile();

    } // end createSpooledFile

}
