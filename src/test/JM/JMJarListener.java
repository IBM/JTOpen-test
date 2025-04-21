///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMJarListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JM;

import utilities.*;
import java.util.Enumeration;
import java.util.Vector;

/**
   The JMJarListener interface provides a listener interface for receiving
   JarMaker events.
**/

public class JMJarListener implements JarMakerListener
{
  public Vector<JarMakerEvent> started = new Vector<JarMakerEvent> ();
  public Vector<JarMakerEvent> completed = new Vector<JarMakerEvent> ();

  /**
   * Invoked at the start of dependency analysis on a class file.
   * @param event The JarMaker event.
   **/
  public void dependencyAnalysisStarted (JarMakerEvent event)
  {
    started.add (event);
  }

  /**
   * Invoked upon completion of dependency analysis on a class file.
   * @param event The JarMaker event.
   **/
  public void dependencyAnalysisCompleted (JarMakerEvent event)
  {
    if (!listContainsEvent(started, event))
      System.err.println ("JMJarListener: Event completed but not started: " + event.toString ());
    else started.remove (event);
    completed.add (event);
  }

  boolean listContainsEvent (Vector<JarMakerEvent> eventList, JarMakerEvent event)
  {
    Enumeration<JarMakerEvent> e = eventList.elements ();
    while (e.hasMoreElements ()) {
      JarMakerEvent listElement = (JarMakerEvent)e.nextElement ();
      if (listElement.toString ().equals (event.toString ()))
        return true;
    }
    return false;
  }

  public void reset()
  {
    started.removeAllElements ();
    completed.removeAllElements ();
  }

}
