///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMPropertyChangeListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.DDM;

import java.beans.*;

/**
 *PropertyChangeListener class for the DDM testcases that verify beans and
 *serialization.
**/
public class DDMPropertyChangeListener implements PropertyChangeListener
{
  public boolean propertyChangeFired_;
  public PropertyChangeEvent e_ = null;
  public DDMPropertyChangeListener()
  {
  }

  public void propertyChange(PropertyChangeEvent event)
  {
    propertyChangeFired_ = true;
    e_ = event;
  }

  public void reset()
  {
    propertyChangeFired_ = false;
    e_ = null;
  }

  public String toString()
  {
    return e_.getOldValue() + ", " + e_.getNewValue() + ", " + e_.getPropertyName();
  }
}
