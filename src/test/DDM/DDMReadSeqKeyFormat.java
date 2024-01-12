///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DDMReadSeqKeyFormat.java
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
import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.BinaryFieldDescription;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;

class DDMReadSeqKeyFormat extends RecordFormat
{
  DDMReadSeqKeyFormat(AS400 sys)
  {
    super("Fld1Key1");
    addFieldDescription(new CharacterFieldDescription(new AS400Text(10, sys.getCcsid(), sys), "Field1"));
    addKeyFieldDescription(0);
  }
}

