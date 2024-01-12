///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMAS400FileRecordDescriptionListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DDM;

import com.ibm.as400.access.AS400FileRecordDescriptionListener;
import com.ibm.as400.access.AS400FileRecordDescriptionEvent;

public class DDMAS400FileRecordDescriptionListener implements AS400FileRecordDescriptionListener
{
  public boolean retrieved;
  public boolean created;

/**
 Invoked when a record format has been retrieved.
 @param event the file event.
**/
  public void recordFormatRetrieved(AS400FileRecordDescriptionEvent event)
  {
    retrieved = true;
  }

/**
 Invoked when a file has been created.
 @param event the file event.
 **/
  public void recordFormatSourceCreated(AS400FileRecordDescriptionEvent event)
  {
    created = true;
  }

  public void reset()
  {
    retrieved = false;
    created = false;
  }
}
