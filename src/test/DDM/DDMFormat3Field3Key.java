///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMFormat3Field3Key.java
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

class DDMFormat3Field3Key extends RecordFormat
{
  private static final long serialVersionUID = 1L;

  DDMFormat3Field3Key(AS400 sys)
  {
    super("Fld3Key3");
    AS400Text txt = new AS400Text(132, sys.getCcsid(), sys);
    addFieldDescription(new CharacterFieldDescription(txt, "Field1"));
    addFieldDescription(new CharacterFieldDescription(txt, "Field2"));
    addFieldDescription(new CharacterFieldDescription(txt, "Field3"));
    addKeyFieldDescription(0);
    addKeyFieldDescription(1);
    addKeyFieldDescription(2);
  }
}

