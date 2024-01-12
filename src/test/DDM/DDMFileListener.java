///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMFileListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DDM;

import com.ibm.as400.access.FileListener;
import com.ibm.as400.access.FileEvent;

public class DDMFileListener implements FileListener
{
  public boolean closed;
  public boolean created;
  public boolean deleted;
  public boolean modified;
  public boolean opened;

/**
 Invoked when a file has been closed.
 @param event the file event.
**/
  public void fileClosed(FileEvent event)
  {
    closed = true;
  }

/**
 Invoked when a file has been created.
 @param event the file event.
 **/
  public void fileCreated(FileEvent event)
  {
    created = true;
  }

/**
 Invoked when a file is deleted.
 @param event the file event.
**/
  public void fileDeleted(FileEvent event)
  {
    deleted = true;
  }

/**
 Invoked when a file has been modified.
 @param event the file event.
**/
  public void fileModified(FileEvent event)
  {
    modified = true;
  }

/**
 Invoked when a file has been opened.
 @param event the file event.
**/
  public void fileOpened(FileEvent event)
  {
    opened = true;
  }

  public void reset()
  {
    closed = false;
    created = false;
    deleted = false;
    modified = false;
    opened = false;
  }
}
