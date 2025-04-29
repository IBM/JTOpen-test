///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMChar10NullKeyFormat.java
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

class DDMChar10NullKeyFormat extends RecordFormat
{
  private static final long serialVersionUID = 1L;

  DDMChar10NullKeyFormat(AS400 sys)
  {
    super("KEYFMT");
    CharacterFieldDescription cfd = new CharacterFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "field1");
    cfd.setALWNULL(true);
    addFieldDescription(cfd);
    addKeyFieldDescription("field1");
  }
}
