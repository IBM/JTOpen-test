///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RFRecordDescriptionListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import com.ibm.as400.access.*;

/**
 *FileListener class for the DDM testcases that verify beans and
 *serialization.
**/
public class RFRecordDescriptionListener implements RecordDescriptionListener
{
  boolean fieldDescriptionAddedFired_;
  boolean keyFieldDescriptionAddedFired_;
  boolean fieldModifiedFired_;

  public RFRecordDescriptionListener()
  {
  }

  public void fieldDescriptionAdded(RecordDescriptionEvent event)
  {
    fieldDescriptionAddedFired_ = true;
  }

  public void keyFieldDescriptionAdded(RecordDescriptionEvent event)
  {
    keyFieldDescriptionAddedFired_ = true;
  }

  public void fieldModified(RecordDescriptionEvent event)
  {
    fieldModifiedFired_ = true;
  }

  public void reset()
  {
    fieldModifiedFired_ = false;
    fieldDescriptionAddedFired_ = false;
    keyFieldDescriptionAddedFired_ = false;
  }
}



