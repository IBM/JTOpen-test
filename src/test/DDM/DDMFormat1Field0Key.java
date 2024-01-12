///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMFormat1Field0Key.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DDM;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.RecordFormat;

public class DDMFormat1Field0Key extends RecordFormat
{
  public DDMFormat1Field0Key(AS400 sys)
  {
    super("Fld1Key0");
    addFieldDescription(new CharacterFieldDescription(new AS400Text(132, sys.getCcsid(), sys), "Field1"));
  }
}

