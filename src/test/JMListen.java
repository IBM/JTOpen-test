///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMListen.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import utilities.*;
import com.ibm.as400.access.AS400;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
Testcase JMListen.  This tests the following
methods of the JarMaker class:

<ul compact>
<li>addJarMakerListener(JarMakerListener)
<li>removeJarMakerListener(JarMakerListener)
</ul>
It also tests all methods of the JarMakerEvent class,
and of the JarMakerListener interface.
**/

public class JMListen
extends Testcase
{

/**
Constructor.
**/
  public JMListen (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMListen",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
      JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
      if (!JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL))
        output_.println ("Failed to cleanup " +
                         JMTest.JUNGLE_JAR_SMALL.getAbsolutePath ());
    }


    void makeSmallJar (JarMaker jm)
    {
      try {
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.JUNGLE_JAR_SMALL);
        // Specify a required entry.
        Vector entryList = new Vector (1);
        entryList.add ("jungle/animal.jpg");
        jm.setRequiredFiles (entryList);
        // Make the jar.
        jm.makeJar (JMTest.JUNGLE_JAR);
      }
      catch (Exception e) {
        System.err.println ("Exception in makeSmallJar: " + e.getMessage ());
        e.printStackTrace ();
      }
    }


    boolean validateSmallJarListeners (Vector listeners)
    {
      boolean result = true;
      Enumeration e = listeners.elements ();
      for (int i=0; e.hasMoreElements (); i++)
      {
        JMJarListener jl = (JMJarListener)e.nextElement ();
        if ((jl.started.size () != 1) ||
            !listContainsEvent (jl.started, "jungle/animal.jpg"))
        {
          System.err.println ("Incorrect 'started' list for Listener " +
                              i + ":");
          printList (jl.started);
          result = false;
        }
        if ((jl.completed.size () != 1) ||
            !listContainsEvent (jl.completed, "jungle/animal.jpg"))
        {
          System.err.println ("Incorrect 'completed' list for Listener " +
                              i + ":");
          printList (jl.completed);
          result = false;
        }
      }
      return result;
    }

  boolean listContainsEvent (Vector eventList, String eventString)
  {
    Enumeration e = eventList.elements ();
    while (e.hasMoreElements ()) {
      JarMakerEvent listElement = (JarMakerEvent)e.nextElement ();
      if (listElement.toString ().equals (eventString))
        return true;
    }
    return false;
  }

  boolean listContainsEvent (Vector eventList, JarMakerEvent event)
  {
    Enumeration e = eventList.elements ();
    while (e.hasMoreElements ()) {
      JarMakerEvent listElement = (JarMakerEvent)e.nextElement ();
      if (listElement.toString ().equals (event.toString ()))
        return true;
    }
    return false;
  }

    void printList (Vector list)
    {
      if (list.size () == 0) System.err.println ("(empty list)");
      else {
        Enumeration e = list.elements ();
        while (e.hasMoreElements ()) {
          JarMakerEvent event = (JarMakerEvent)e.nextElement ();
          System.err.println (event.toString ());
        }
      }
    }


  /**
   *Verify invalid usage addJarMakerListener().
   *<ul compact>
   *<li>Add null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var001()
  {
    try
    {
      JarMaker jm = new JarMaker ();
      jm.addJarMakerListener (null);
      failed ("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs (e, "NullPointerException", "listener"))
      {
        failed (e, "wrong exception");
        return;
      }
    }
    succeeded ();
  }

  /**
   *Verify invalid usage removeJarMakerListener().
   *<ul compact>
   *<li>Remove null listener
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>NullPointerException indicating "listener"
   *</ul>
  **/
  public void Var002()
  {
    try
    {
      JarMaker jm = new JarMaker ();
      jm.removeJarMakerListener (null);
      failed ("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs (e, "NullPointerException", "listener"))
      {
        failed (e, "wrong exception");
        return;
      }
    }
    succeeded ();
  }


  /**
   *Verify valid usage of addJarMakerListener(), removeJarMakerListener().
   *<ul compact>
   *<li>Add a several listeners
   *<li>Remove one listener
   *<li>Invoke a function which fires an event
   *<li>Verify that the removed listener did not get the event
   *<li>Verify that the remaining listeners did get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The event will not be received by the listener that was removed and
   *will be received by the remaining listeners.
   *</ul>
  **/
public void Var003()
{
  try
  {
    JarMaker jm = new JarMaker ();
    // Create our jar listeners
    JMJarListener jl1 = new JMJarListener();
    JMJarListener jl2 = new JMJarListener();
    JMJarListener jl3 = new JMJarListener();

    // Verify three listeners
    jm.addJarMakerListener(jl1);
    jm.addJarMakerListener(jl2);
    jm.addJarMakerListener(jl3);

    // Cause some JarMakerEvents to be fired
    makeSmallJar (jm);

    // Verify that all the listeners got the right events
    Vector listeners = new Vector (3);
    listeners.add (jl1);
    listeners.add (jl2);
    listeners.add (jl3);
    if (!validateSmallJarListeners (listeners))
    {
      failed("three listeners");
      return;
    }

    // Reset listeners
    jl1.reset();
    jl2.reset();
    jl3.reset();

    // Remove jl2 from the listener list
    jm.removeJarMakerListener(jl2);

    // Cause some JarMakerEvents to be fired
    makeSmallJar (jm);

    // Verify that the remaining listeners got the right events
    listeners.remove (jl2);
    if (!validateSmallJarListeners (listeners))
    {
      failed("two listeners");
      return;
    }
    // Verify that the removed listener got no events
    assertCondition (jl2.started.size () == 0 &&
            jl2.completed.size () == 0);
  }
  catch(Exception e)
  {
    failed(e, "Unexpected exception");
    return;
  }
}


  /**
   *Verify that reset() removes listeners.
   *<ul compact>
   *<li>Add a several listeners
   *<li>Do a reset() of the JarMaker object
   *<li>Invoke a function which fires an event
   *<li>Verify that the listeners did not get the event
   *</ul>
   *Expected results:
   *<ul compact>
   *<li>The events will not be received by the listeners.
   *</ul>
  **/
public void Var004()
{
  try
  {
    JarMaker jm = new JarMaker ();
    // Create our jar listeners
    JMJarListener jl1 = new JMJarListener();
    JMJarListener jl2 = new JMJarListener();
    JMJarListener jl3 = new JMJarListener();

    // Verify three listeners
    jm.addJarMakerListener(jl1);
    jm.addJarMakerListener(jl2);
    jm.addJarMakerListener(jl3);
    jm.reset ();
    // Cause some JarMakerEvents to be fired
    makeSmallJar (jm);

    // Verify that all the listeners got no events
    assertCondition (jl1.started.size () == 0 &&
            jl1.completed.size () == 0 &&
            jl2.started.size () == 0 &&
            jl2.completed.size () == 0 &&
            jl3.started.size () == 0 &&
            jl3.completed.size () == 0);
  }
  catch(Exception e)
  {
    failed(e, "Unexpected exception");
    return;
  }
}

  /**
   *Construct a JarMakerEvent with null first arg.
   Should throw exception.
  **/
  public void Var005()
  {
    try
    {
      JarMakerEvent event = new JarMakerEvent (null, "someEntryName");
      failed ("No exception");
      return;
    }
    catch(Exception e)
    {
      //if (!exceptionIs (e, "IllegalArgumentException", "null source"))
      if (!exceptionIs (e, "IllegalArgumentException"))
      {
        failed (e, "wrong exception");
        return;
      }
    }
    succeeded ();
  }

  /**
   *Construct a JarMakerEvent with null second arg.
   Should throw exception.
  **/
  public void Var006()
  {
    try
    {
      JarMakerEvent event = new JarMakerEvent (this, null);
      failed ("No exception");
      return;
    }
    catch(Exception e)
    {
      if (!exceptionIs (e, "NullPointerException", "jarEntryName"))
      {
        failed (e, "wrong exception");
        return;
      }
    }
    succeeded ();
  }

}



