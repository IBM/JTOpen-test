///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TestStatementHelper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import com.ibm.as400.access.*;

public class TestStatementHelper
{
  public static void addListener(AS400JDBCStatementListener listener)
  {
    JDSQLStatement.addStatementListener(listener);
  }

  public static void removeListener(AS400JDBCStatementListener listener)
  {
    JDSQLStatement.removeStatementListener(listener);
  }
}
