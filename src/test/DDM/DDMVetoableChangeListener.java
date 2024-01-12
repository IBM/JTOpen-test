///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMVetoableChangeListener.java
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
 *VetoableChangeListener class for the DDM testcases that verify beans and
 *serialization.
**/
public class DDMVetoableChangeListener implements VetoableChangeListener
{
  public boolean vetoableChangeFired_ = false;
  public boolean vetoTheChange = false;
  public boolean vetoed_ = false;
  public PropertyChangeEvent e_ = null;
  public DDMVetoableChangeListener()
  {
  }

  public void vetoableChange(PropertyChangeEvent event)
    throws PropertyVetoException
  {
    if (vetoTheChange)
    {
      vetoed_ = true;
      e_ = null;
      throw new PropertyVetoException("", event);
    }
    vetoableChangeFired_ = true;
    e_ = event;
  }

  public void reset()
  {
    vetoableChangeFired_ = false;
    vetoTheChange = false;
    vetoed_ = false;
    e_ = null;
  }

  public String toString()
  {
    return e_.getOldValue() + ", " + e_.getNewValue() + ", " + e_.getPropertyName();
  }
}


