///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PNBeansTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import com.ibm.as400.access.QSYSObjectPathName;

/**
 Testcase PNBeansTestcase.  Test all beans-related functions and methods, including:
 <ul>
 <li>throwing property change events
 <li>throwing and handling property veto events
 <li>throwing error events
 <li>serializing and deserializing
 <li>QSYSObjectPathName.addErrorListener
 <li>QSYSObjectPathName.addPropertyChangeListener
 <li>QSYSObjectPathName.addVetoableChangeListener
 <li>QSYSObjectPathName.removeErrorListener
 <li>QSYSObjectPathName.removePropertyChangeListener
 <li>QSYSObjectPathName.removeVetoableChangeListener
 </ul>
 <p>Note that the add...Listener methods do not have their own variations but are tested through other variations.
 **/
public class PNBeansTestcase extends Testcase implements PropertyChangeListener, VetoableChangeListener
{
    private Vector changeEvents = new Vector();

    public void propertyChange(PropertyChangeEvent event)
    {
        changeEvents.addElement(event);
    }

    private boolean gotChangeEvent()
    {
        if (changeEvents.size() == 0) return false;
        PropertyChangeEvent got = (PropertyChangeEvent)changeEvents.elementAt(0);
        if (got.getOldValue() != null && got.getNewValue() != null && got.getPropertyName() != null)
        {
            return true;
        }
        return false;
    }

    private void printChangeEvents(int numberExpected)
    {
        PropertyChangeEvent[] events = new PropertyChangeEvent[1];
        if (numberExpected == 1)
        {
            events[0] = new PropertyChangeEvent(this, null, null, null);
        }
        printEvents( events, changeEvents);
    }

    private void printEvents(PropertyChangeEvent[] events, Vector received)
    {
        int expectedLength = 0; 
        if (events != null) { 
          expectedLength = events.length;
          if (expectedLength == 1 && events[0] == null) {
            expectedLength = 0; 
          }
        }
        output_.println(" Expected " + expectedLength +  " events, including:");
        if ((expectedLength == 0) || events == null  )
        {
            output_.println("  nothing");
        }
        else
        {
            for (int i = 0; i < events.length; ++i)
            {
                if (events[i] != null) { 
                output_.print("  old = '" + events[i].getOldValue() + "', ");
                output_.print("new = '" + events[i].getNewValue() + "', ");
                output_.println("name = '" + events[i].getPropertyName() + "'");
                } else { 
                  output_.println("event is null"); 
                }
            }
        }
        output_.println(" Received:");
        if (received.size() == 0)
        {
            output_.println("  nothing");
        }
        for (int i = 0; i < received.size(); ++i)
        {
            PropertyChangeEvent got = (PropertyChangeEvent)received.elementAt(i);
            output_.print("  old = '" + got.getOldValue() + "', ");
            output_.print("new = '" + got.getNewValue() + "', ");
            output_.println("name = '" + got.getPropertyName() + "'");
        }
    }

    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setLibraryName passing a different library.
     **/
    public void Var001()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener(this);
        changeEvents.removeAllElements();
        try
        {
            pn.setLibraryName("newLibrary");
            if (!gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setLibraryName passing the same library.
     **/
    public void Var002()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener(this);
        try
        {
            pn.setLibraryName("newLibrary");
            changeEvents.removeAllElements();
            pn.setLibraryName("newLibrary");
            if (changeEvents.size() != 0)
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setObjectName passing a different object.
     **/
    public void Var003()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectName("newObject");
            if (!gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setObjectName passing the same object.
     **/
    public void Var004()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        try
        {
            pn.setObjectName("newObject");
            changeEvents.removeAllElements();
            pn.setObjectName("newObject");
            if (changeEvents.size() != 0)
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setMemberName passing a different member.
     **/
    public void Var005()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setMemberName("newMember");
            if (!gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setMemberName passing the same member.
     **/
    public void Var006()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        try
        {
            pn.setMemberName("newMember");
            changeEvents.removeAllElements();
            pn.setMemberName("newMember");
            if (changeEvents.size() != 0)
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setObjectType passing a different type.
     **/
    public void Var007()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectType("type2");
            if (!gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setObjectType passing the same type.
     **/
    public void Var008()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        try
        {
            pn.setObjectType("type2");
            changeEvents.removeAllElements();
            pn.setObjectType("type2");
            if (changeEvents.size() != 0)
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setPath passing a different path.
     **/
    public void Var009()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setPath("/QSYS.lib/library.lib/object.file/Old.mbr");
            if (!gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a single generic PropertyChangeEvent is thrown
     when doing setPath passing the same path.
     **/
    public void Var010()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/QSYS.lib/library.lib/object.file/Old.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setPath("/QSYS.lib/LIBRARY.lib/object.file/Old.mbr");
            if (changeEvents.size() != 0)
            {
                failed("Events not correct.");
                printChangeEvents(1);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }



    // ************ Constrained tests

    private Vector vetoEvents = new Vector();

    public void vetoableChange(PropertyChangeEvent event)
      throws PropertyVetoException
    {
        vetoEvents.addElement(event);
	if (event.getNewValue().equals("REJECT")) {
	    System.out.println("Throwing Property veto exception for REJECT"); 
            throw new PropertyVetoException("badegg", event);
	}
        if (((String)(event.getNewValue())).indexOf("REJECTPATH") > 1)
            throw new PropertyVetoException("veto path", event);
        if (((String)(event.getNewValue())).indexOf("RPATH") > 1)
            throw new PropertyVetoException("veto path", event);
    }

    private boolean gotVetoEvent(PropertyChangeEvent[] events)
    {
        int numFound = 0;
        PropertyChangeEvent event;
        for (int j=0; j<events.length; ++j)
        {
            event = events[j];
            for (int i=0; i<vetoEvents.size(); ++i)
            {
                PropertyChangeEvent got =
                  (PropertyChangeEvent)vetoEvents.elementAt(i);
                if (event.getOldValue().equals(got.getOldValue()) &&
                    event.getNewValue().equals(got.getNewValue()) &&
                    event.getPropertyName().equals(got.getPropertyName()))
                    numFound++;
            }
        }
        if (numFound == events.length)
            return true;
        return false;
    }

    private void printVetoEvents(PropertyChangeEvent[] events)
    {
        printEvents(events, vetoEvents);
    }


    /**
     Verify a library property change sends the correct veto events,
     and if the change is not rejected will change
     the library (and path), and change events will be fired.
     **/
    public void Var011()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setLibraryName("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/OTHER.LIB/MYOBJ.OBJ");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getLibraryName().equals("OTHER"))
                    failed("Library name not changed: " + pn.getLibraryName());
                else if (!pn.getPath().equals("/QSYS.LIB/OTHER.LIB/MYOBJ.OBJ"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a library property change in which the library change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     library (and path) unchanged, and no change events will be fired.
     **/
    public void Var012()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);

	PNBeansTestcase listener2 =  new PNBeansTestcase();
	listener2.output_ = output_; 
        pn.addPropertyChangeListener((PropertyChangeListener)listener2);
        pn.addVetoableChangeListener((VetoableChangeListener)listener2);

        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();

	
        try
        {
            pn.setLibraryName("reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB",
                                      "REJECT");
            PropertyChangeEvent expected2=
              new PropertyChangeEvent(pn, "libraryName", "REJECT",
                                      "MYLIB");
            PropertyChangeEvent[] expected;
            
            // in JDK 1.7, the event notification change so that
            // the "change" back notification is not sent if 
            // the change was not made. 
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[1];
              expected[0] = expected1;
            } else { 
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            }
            
	    System.out.println("-------------- INFO ----------------"); 
	    System.out.println("Veto events"); 
	    printVetoEvents( expected);
	    System.out.println("Change events"); 
	    printChangeEvents(1);

	    // Primed 5/30/2014 on fowgai using  616CA 
	    
	    PropertyChangeEvent[]  expectedListener2;
      if (isAtLeastJDK17) {
        expectedListener2 = new PropertyChangeEvent[0];
      } else { 	    
        expectedListener2 = new PropertyChangeEvent[1];
        expectedListener2[0] = expected2;
      }

	    System.out.println("Listener2 Veto events"); 
	    listener2.printVetoEvents( expectedListener2);
	    System.out.println("Listener2 Change events"); 
	    listener2.printChangeEvents(1);


            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                System.out.println("-------------FAILED---------------"); 
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                failed(e, "Wrong exception info.");
            else if (!pn.getLibraryName().equals("MYLIB"))
                failed("Library name changed to " + pn.getLibraryName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
            {
                if (gotChangeEvent())
                {
                    failed("Events not correct.");
                    printChangeEvents(0);
                }
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a library property change in which the path change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     library (and path) unchanged, and no change events will be fired.
     **/
    public void Var013()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener(this);
        pn.addVetoableChangeListener(this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setLibraryName("rejectpath");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verify veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB", "REJECTPATH");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            } else { 
            expected = new PropertyChangeEvent[3];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getLibraryName().equals("MYLIB"))
                failed("Library name changed to " + pn.getLibraryName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else if (gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(0);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setLibraryName that does not change the library value will
     not send any veto event.
     **/
    public void Var014()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setLibraryName("mylib");
            if (vetoEvents.size() != 0)
            {
                failed("Events not correct.");
                printVetoEvents( null);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setLibraryName that does not result in a complete path will
     not send a veto event for the path.
     **/
    public void Var015()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setLibraryName("mylib");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "", "MYLIB");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[1];
            expected[0] = expected1;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a object name property change sends the correct veto events,
     and if the change is not rejected will change
     the object name (and path), and change events will be fired.
     **/
    public void Var016()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectName("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/OTHER.OBJ");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getObjectName().equals("OTHER"))
                    failed("Object name not changed: " + pn.getObjectName());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/OTHER.OBJ"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a object name property change in which the object name change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     object name (and path) unchanged, and no change events will be fired.
     **/
    public void Var017()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectName("reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verify veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ",
                                      "REJECT");
            PropertyChangeEvent expected2=
              new PropertyChangeEvent(pn, "objectName", "REJECT",
                                      "MYOBJ");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[1];
              expected[0] = expected1;
            } else { 
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectName().equals("MYOBJ"))
                failed("Library name changed to " + pn.getObjectName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
            {
                if (gotChangeEvent())
                {
                    failed("Events not correct.");
                    printChangeEvents(0);
                }
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a object name property change in which the path change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     object name (and path) unchanged, and no change events will be fired.
     **/
    public void Var018()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectName("rejectpath");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ", "REJECTPATH");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/REJECTPATH.OBJ");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/REJECTPATH.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            
            } else { 
              expected = new PropertyChangeEvent[3];
              expected[0] = expected1;
              expected[1] = expected2;
              expected[2] = expected3;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectName().equals("MYOBJ"))
                failed("Object name changed to " + pn.getObjectName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else if (gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(0);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setObjectName that does not change the object name value will
     not send any veto event.
     **/
    public void Var019()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectName("myobj");
            if (vetoEvents.size() != 0)
            {
                failed("Events not correct.");
                printVetoEvents( null);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setObjectName that does not result in a complete path will
     not send a veto event for the path.
     **/
    public void Var020()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectName("myobj");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectName", "", "MYOBJ");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[1];
            expected[0] = expected1;
            if (vetoEvents.size() != 1 ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a member property change sends the correct veto events,
     and if the change is not rejected will change
     the member (and type and path), and change events will be fired.
     **/
    public void Var021()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setMemberName("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getMemberName().equals("OTHER"))
                    failed("Member name not changed: " + pn.getMemberName());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a member property change in which the member change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     member (and type and path) unchanged, and no change events will be fired.
     **/
    public void Var022()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setMemberName("reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "memberName", "",
                                      "REJECT");
            PropertyChangeEvent expected2=
              new PropertyChangeEvent(pn, "memberName", "REJECT",
                                      "");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[1];
              expected[0] = expected1;
              
            } else { 
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                failed(e, "Wrong exception info.");
            else if (!pn.getMemberName().equals(""))
                failed("Member name changed to " + pn.getMemberName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
            {
                if (gotChangeEvent())
                {
                    failed("Events not correct.");
                    printChangeEvents(0);
                }
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a member property change in which the path change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     member (and type and path) unchanged, and no change events will be fired.
     **/
    public void Var023()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setMemberName("rejectpath");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "FILE", "MBR");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "memberName", "", "REJECTPATH");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/REJECTPATH.MBR");
            PropertyChangeEvent expected4 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/REJECTPATH.MBR",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[3];
              expected[0] = expected2;
              expected[1] = expected1;
              expected[2] = expected3;
            
            } else { 
            expected = new PropertyChangeEvent[4];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            expected[3] = expected4;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectType().equals("FILE"))
                failed("Object type changed to " + pn.getObjectType());
            else if (!pn.getMemberName().equals(""))
                failed("Member name changed to " + pn.getMemberName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE"))
                failed("Path changed to " + pn.getPath());
            else if (gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(0);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setMemberName that does not change the member value will
     not send any veto event.
     **/
    public void Var024()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setMemberName("mymbr");
            if (vetoEvents.size() != 0)
            {
                failed("Events not correct.");
                printVetoEvents( null);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setMemberName that does not result in a complete path will
     not send a veto event for the path.
     **/
    public void Var025()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setMemberName("mymbr1"); // dummy first so don't get type event
            vetoEvents.removeAllElements();
            pn.setMemberName("mymbr");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR1", "MYMBR");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[1];
            expected[0] = expected1;
            if (vetoEvents.size() != 1 ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setMemberName on an object which is not a member will
     send a veto event for the type.
     **/
    public void Var026()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setMemberName("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "memberName", "", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "objectType", "FILE", "MBR");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[3];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            if (vetoEvents.size() != 3 ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getMemberName().equals("OTHER"))
                    failed("Member name not changed: " + pn.getMemberName());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR"))
                    failed("Path not changed: " + pn.getPath());
                else if (!pn.getObjectType().equals("MBR"))
                    failed("Object type not changed: " + pn.getObjectType());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setMemberName on an object which is a member will
     not send a veto event for the type.
     **/
    public void Var027()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setMemberName("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getMemberName().equals("OTHER"))
                    failed("Member name not changed: " + pn.getMemberName());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/OTHER.MBR"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a type property change sends the correct veto events,
     and if the change is not rejected will change
     the type (and member and path), and change events will be fired.
     **/
    public void Var028()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectType("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "OBJ", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getObjectType().equals("OTHER"))
                    failed("Type not changed: " + pn.getObjectType());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a type property change in which the type change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     type (and member and path) unchanged, and no change events will be fired.
     **/
    public void Var029()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectType("reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "OBJ",
                                      "REJECT");
            PropertyChangeEvent expected2=
              new PropertyChangeEvent(pn, "objectType", "REJECT",
                                      "OBJ");
            PropertyChangeEvent[] expected; 
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[1];
              expected[0] = expected1;
            
            } else { 
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectType().equals("OBJ"))
                failed("Object type changed to " + pn.getObjectType());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
            {
                if (gotChangeEvent())
                {
                    failed("Events not correct.");
                    printChangeEvents(0);
                }
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a type property change in which the path change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     type (and member and path) unchanged, and no change events will be fired.
     **/
    public void Var030()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectType("rpath");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "MBR", "RPATH");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.RPATH");
            PropertyChangeEvent expected4 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.RPATH",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR");
            PropertyChangeEvent[] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[3];
              expected[0] = expected2;
              expected[1] = expected1;
              expected[2] = expected3;
              
            } else { 
            expected = new PropertyChangeEvent[4];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            expected[3] = expected4;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectType().equals("MBR"))
                failed("Object type changed to " + pn.getObjectType());
            else if (!pn.getMemberName().equals("MYMBR"))
                failed("Member name changed to " + pn.getMemberName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR"))
                failed("Path changed to " + pn.getPath());
            else if (gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(0);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setObjectType that does not change the type value will
     not send any veto event.
     **/
    public void Var031()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectType("obj");
            if (vetoEvents.size() != 0)
            {
                failed("Events not correct.");
                printVetoEvents( null);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setObjectType that does not result in a complete path will
     not send a veto event for the path.
     **/
    public void Var032()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectType("type");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "", "TYPE");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[1];
            expected[0] = expected1;
            if (vetoEvents.size() != 1 ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a type property change sends a member veto event,
     if the object was a member and the new type is not "MBR".
     **/
    public void Var033()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectType("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "MBR", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[3];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            if (vetoEvents.size() != 3 ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getObjectType().equals("OTHER"))
                    failed("Type not changed: " + pn.getObjectType());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER"))
                    failed("Path not changed: " + pn.getPath());
                else if (!pn.getMemberName().equals(""))
                    failed("Member not changed: " + pn.getMemberName());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a type property change does not send a member veto event
     if the object was not a member.
     **/
    public void Var034()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectType("other");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "OBJ", "OTHER");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getObjectType().equals("OTHER"))
                    failed("Type not changed: " + pn.getObjectType());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OTHER"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a path property change sends the correct veto events,
     and if the change is not rejected will change
     the path (and other properties), and change events will be fired.
     **/
    public void Var035()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        changeEvents.removeAllElements();
        try
        {
            pn.setPath("/qsys.lib/newlib.lib/newobj.file/newmbr.mbr");
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB", "NEWLIB");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ", "NEWOBJ");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "memberName", "", "NEWMBR");
            PropertyChangeEvent expected4 =
              new PropertyChangeEvent(pn, "objectType", "OBJ", "MBR");
            PropertyChangeEvent expected5 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/NEWLIB.LIB/NEWOBJ.FILE/NEWMBR.MBR");
            PropertyChangeEvent expected[] = new PropertyChangeEvent[5];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            expected[3] = expected4;
            expected[4] = expected5;
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else
            {
                if (!pn.getLibraryName().equals("NEWLIB"))
                    failed("Library name not changed: " + pn.getLibraryName());
                if (!pn.getObjectName().equals("NEWOBJ"))
                    failed("Object name not changed: " + pn.getObjectName());
                if (!pn.getMemberName().equals("NEWMBR"))
                    failed("Member name not changed: " + pn.getMemberName());
                if (!pn.getObjectType().equals("MBR"))
                    failed("Object Type not changed: " + pn.getObjectType());
                else if (!pn.getPath().equals("/QSYS.LIB/NEWLIB.LIB/NEWOBJ.FILE/NEWMBR.MBR"))
                    failed("Path not changed: " + pn.getPath());
                else
                {
                    // get single change event regardless of # of changes
                    if (!gotChangeEvent())
                    {
                        failed("Events not correct.");
                        printChangeEvents(1);
                    }
                    else
                        succeeded();
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a path property change in which the path change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     path (and other properties) unchanged, and no change events will be fired.
     **/
    public void Var036()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setPath("/qsys.lib/rejectpath.lib/myobj.obj");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ");
            PropertyChangeEvent expected2=
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ");
            PropertyChangeEvent [] expected;
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[1];
              expected[0] = expected1;
            } else { 
            expected = new PropertyChangeEvent[2];
            expected[0] = expected1;
            expected[1] = expected2;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getLibraryName().equals("MYLIB"))
                failed("Library name changed to " + pn.getLibraryName());
            else if (!pn.getObjectName().equals("MYOBJ"))
                failed("Object name changed to " + pn.getObjectName());
            else if (!pn.getMemberName().equals(""))
                failed("Member name changed to " + pn.getMemberName());
            else if (!pn.getObjectType().equals("OBJ"))
                failed("Object type changed to " + pn.getObjectType());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
            {
                if (gotChangeEvent())
                {
                    failed("Events not correct.");
                    printChangeEvents(0);
                }
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a path property change in which the type change is rejected
     will generate the correct PropertyVetoException, send the correct
     extra 'rollback' PropertyVetoableChangeEvents, leave the
     path (and other properties) unchanged, and no change events will be fired.
     **/
    public void Var037()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setPath("/qsys.lib/newlib.lib/newobj.reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "MBR", "REJECT");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "objectType", "REJECT", "MBR");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "");
            PropertyChangeEvent expected4 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB", "NEWLIB");
            PropertyChangeEvent expected5 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ", "NEWOBJ");
            PropertyChangeEvent expected6 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/NEWLIB.LIB/NEWOBJ.REJECT");
            PropertyChangeEvent [] expected;
            if (isAtLeastJDK17) { 
              expected = new PropertyChangeEvent[5];
              expected[0] = expected6;
              expected[1] = expected4;
              expected[2] = expected5;
              expected[3] = expected3;
              expected[4] = expected1;
            
            } else { 
            expected = new PropertyChangeEvent[6];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            expected[3] = expected4;
            expected[4] = expected5;
            expected[5] = expected6;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                failed(e, "Wrong exception info.");
            else if (!pn.getObjectType().equals("MBR"))
                failed("Object type changed to " + pn.getObjectType());
            else if (!pn.getMemberName().equals("MYMBR"))
                failed("Member name changed to " + pn.getMemberName());
            else if (!pn.getObjectName().equals("MYOBJ"))
                failed("Object name changed to " + pn.getObjectName());
            else if (!pn.getLibraryName().equals("MYLIB"))
                failed("Library name changed to " + pn.getLibraryName());
            else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR"))
                failed("Path changed to " + pn.getPath());
            else if (gotChangeEvent())
            {
                failed("Events not correct.");
                printChangeEvents(0);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify a setPath that does not change the path value will
     not send any veto event.
     **/
    public void Var038()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.obj");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setPath("/qsys.lib/mylib.lib/myobj.obj");
            if (vetoEvents.size() != 0)
            {
                failed("Events not correct.");
                printVetoEvents( null);
            }
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify that vetos during rollback processing are ignored.
     **/
    public void Var039()
    {
        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/reject.lib/myobj.obj");
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        changeEvents.removeAllElements();
        vetoEvents.removeAllElements();
        try
        {
            pn.setLibraryName("rejectpath");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "libraryName", "REJECT",
                                      "REJECTPATH");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/REJECT.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/REJECTPATH.LIB/MYOBJ.OBJ",
                                      "/QSYS.LIB/REJECT.LIB/MYOBJ.OBJ");
            PropertyChangeEvent expected[];
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[2];
              expected[0] = expected1;
              expected[1] = expected2;
              
            } else { 
            expected = new PropertyChangeEvent[3];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events not correct.");
                printVetoEvents( expected);
            }
            else if (!exceptionIs(e, "PropertyVetoException", "veto path"))
                failed(e, "Wrong exception info.");
            else if (!pn.getLibraryName().equals("REJECT"))
                failed("Library name changed to " + pn.getLibraryName());
            else if (!pn.getPath().equals("/QSYS.LIB/REJECT.LIB/MYOBJ.OBJ"))
                failed("Path changed to " + pn.getPath());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify that vetos (normal and rollback) are sent correctly when there
     is more than one veto listener.
     **/
    public void Var040()
    {
        // Make inner class to be second listener.
        class MyListener implements VetoableChangeListener
        {
            public MyListener() {super();}

            public Vector vetoEvents2 = new Vector();

            public void vetoableChange(PropertyChangeEvent event)
              throws PropertyVetoException
            { vetoEvents2.addElement(event); }
        }

        QSYSObjectPathName pn = new
          QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
        MyListener listener2 = new MyListener();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        pn.addVetoableChangeListener((VetoableChangeListener)listener2);
        vetoEvents.removeAllElements();
        try
        {
            pn.setPath("/qsys.lib/newlib.lib/newobj.reject");
            failed("No exception");
        }
        catch (PropertyVetoException e)
        {
            // verfiy veto events
            PropertyChangeEvent expected1 =
              new PropertyChangeEvent(pn, "objectType", "MBR", "REJECT");
            PropertyChangeEvent expected2 =
              new PropertyChangeEvent(pn, "objectType", "REJECT", "MBR");
            PropertyChangeEvent expected3 =
              new PropertyChangeEvent(pn, "memberName", "MYMBR", "");
            PropertyChangeEvent expected4 =
              new PropertyChangeEvent(pn, "libraryName", "MYLIB", "NEWLIB");
            PropertyChangeEvent expected5 =
              new PropertyChangeEvent(pn, "objectName", "MYOBJ", "NEWOBJ");
            PropertyChangeEvent expected6 =
              new PropertyChangeEvent(pn, "path", "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR",
                                      "/QSYS.LIB/NEWLIB.LIB/NEWOBJ.REJECT");
            PropertyChangeEvent expected[];
            if (isAtLeastJDK17) {
              expected = new PropertyChangeEvent[5];
              expected[0] = expected6;
              expected[1] = expected4;
              expected[2] = expected5;
              expected[3] = expected3;
              expected[4] = expected1;
              
            } else { 
            expected = new PropertyChangeEvent[6];
            expected[0] = expected1;
            expected[1] = expected2;
            expected[2] = expected3;
            expected[3] = expected4;
            expected[4] = expected5;
            expected[5] = expected6;
            }
            if (vetoEvents.size() != expected.length ||
                !gotVetoEvent(expected))
            {
                failed("Events in object 1 not correct.");
                printVetoEvents( expected);
            }
            else
            {
                // verify veto events in second listener
                // Second listener will not have gotten the type events as they
                // would have been vetoed before getting to it.
                if (isAtLeastJDK17) { 
                  expected = new PropertyChangeEvent[4];
                  expected[0] = expected6;
                  expected[1] = expected4;
                  expected[2] = expected5;
                  expected[3] = expected3;
                  
                } else { 
                  expected = new PropertyChangeEvent[5];
                  expected[0] = expected2;
                  expected[1] = expected3;
                  expected[2] = expected4;
                  expected[3] = expected5;
                  expected[4] = expected6;
                }
                vetoEvents = listener2.vetoEvents2;
                if (vetoEvents.size() != expected.length ||
                    !gotVetoEvent(expected))
                {
                    failed("Events in object 2 not correct.");
                    printVetoEvents( expected);
                }
                else if (!exceptionIs(e, "PropertyVetoException", "badegg"))
                    failed(e, "Wrong exception info.");
                else if (!pn.getObjectType().equals("MBR"))
                    failed("Object type changed to " + pn.getObjectType());
                else if (!pn.getMemberName().equals("MYMBR"))
                    failed("Member name changed to " + pn.getMemberName());
                else if (!pn.getObjectName().equals("MYOBJ"))
                    failed("Object name changed to " + pn.getObjectName());
                else if (!pn.getLibraryName().equals("MYLIB"))
                    failed("Library name changed to " + pn.getLibraryName());
                else if (!pn.getPath().equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR"))
                    failed("Path changed to " + pn.getPath());
                else
                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    // ************ serialize tests

    /**
     Verify the properties are maintained when an object is serialized
     and deserialized.
     **/
    public void Var041()
    {
        try
        {
            QSYSObjectPathName old = new
              QSYSObjectPathName("/qsys.lib/mylib.lib/myobj.file/mymbr.mbr");
            // Serialize to file object.tmp
            FileOutputStream outfile = new FileOutputStream("object.tmp");
            {
            ObjectOutputStream outfile2 = new ObjectOutputStream(outfile);
            outfile2.writeObject(old);
            outfile2.flush();
            outfile2.close();
            }
            // Deserialize to new object
            FileInputStream infile = new FileInputStream("object.tmp");
            ObjectInputStream infile2 = new ObjectInputStream(infile);
            QSYSObjectPathName newobj = (QSYSObjectPathName)infile2.readObject();
            String msg = "";
            boolean failed = false;
            if (!newobj.getLibraryName().equals(old.getLibraryName()))
            {
                failed = true;
                msg += " Want library " + old.getLibraryName() +
                  " got library " + newobj.getLibraryName() + "\n";
            }
            if (!newobj.getObjectName().equals(old.getObjectName()))
            {
                failed = true;
                msg += " Want object " + old.getObjectName() +
                  " got object " + newobj.getObjectName() + "\n";
            }
            if (!newobj.getMemberName().equals(old.getMemberName()))
            {
                failed = true;
                msg += " Want member " + old.getMemberName() +
                  " got member " + newobj.getMemberName() + "\n";
            }
            if (!newobj.getObjectType().equals(old.getObjectType()))
            {
                failed = true;
                msg += " Want type " + old.getObjectType() +
                  " got type " + newobj.getObjectType() + "\n";
            }
            if (!newobj.getPath().equals(old.getPath()))
            {
                failed = true;
                msg += " Want path " + old.getPath() +
                  " got path " + newobj.getPath() + "\n";
            }
            if (failed)
                failed(msg);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        finally
        {
            // cleanup file
            (new File("object.tmp")).delete();
        }
    }


    /**
     Verify the listeners are not maintained when an object is serialized
     and deserialized.
     **/
    public void Var042()
    {
        try
        {
            QSYSObjectPathName old = new QSYSObjectPathName();
            // Add listeners
            old.addPropertyChangeListener((PropertyChangeListener)this);
            old.addVetoableChangeListener((VetoableChangeListener)this);
            // Serialize to file object.tmp
            FileOutputStream outfile = new FileOutputStream("object.tmp");
            ObjectOutputStream outfile2 = new ObjectOutputStream(outfile);
            outfile2.writeObject(old);
            outfile2.flush();
            old = null;
            // Deserialize to new object
            FileInputStream infile = new FileInputStream("object.tmp");
            ObjectInputStream infile2 = new ObjectInputStream(infile);
            QSYSObjectPathName newobj = (QSYSObjectPathName)infile2.readObject();

            // Verify we do not get events for new object.
            changeEvents.removeAllElements();
            vetoEvents.removeAllElements();
            try
            {
                newobj.setLibraryName("reject");
                if (gotChangeEvent())
                {
                    failed("Got change events.");
                    printChangeEvents(0);
                }
                else if (vetoEvents.size() != 0)
                {
                    failed("Got veto events.");
                    printVetoEvents( null);
                }
                else
                    succeeded();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        finally
        {
            // cleanup file
            (new File("object.tmp")).delete();
        }
    }


    // ************ remove listener functions


    /**
     Remove a property change listener passing null for the listener parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var043()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.removePropertyChangeListener(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "listener"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Remove a property veto listener passing null for the listener parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var044()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.removeVetoableChangeListener(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "listener"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Verify removing a property change listener will no longer send
     events to that listener.
     **/
    public void Var045()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addPropertyChangeListener((PropertyChangeListener)this);
        changeEvents.removeAllElements();
        try
        {
            pn.setObjectType("type");
            if (!gotChangeEvent())
            {
                failed("Setup failed, events not firing correctly.");
                printChangeEvents(1);
            }
            else
            {
                pn.removePropertyChangeListener((PropertyChangeListener)this);
                changeEvents.removeAllElements();
                pn.setObjectType("type2");
                if (gotChangeEvent())
                {
                    failed("Got event.");
                    printChangeEvents(0);
                }

                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify removing a property veto listener will no longer send
     events to that listener.
     **/
    public void Var046()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        pn.addVetoableChangeListener((VetoableChangeListener)this);
        vetoEvents.removeAllElements();
        try
        {
            pn.setObjectType("type");
            if (vetoEvents.size() != 1)
            {
                failed("Setup failed, events not firing correctly.");
                printVetoEvents( null);
            }
            else
            {
                pn.removeVetoableChangeListener((VetoableChangeListener)this);
                vetoEvents.removeAllElements();
                pn.setObjectType("type2");
                if (vetoEvents.size() != 0)
                {
                    failed("Got event.");
                    printVetoEvents( null);
                }

                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Add a property change listener passing null for the listener parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var047()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.addPropertyChangeListener(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "listener"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Add a property veto listener passing null for the listener parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var048()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.addVetoableChangeListener(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "listener"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

}



