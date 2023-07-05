///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PxEndReqCV.java
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
The PxEndReqCV class represents the
client view of a end request.
**/
class PxEndReqCV
extends PxReqCV
{



/**
Constructs a PxEndReqCV object.

@param endJVM true to end the JVM, false to leave JVM running.
**/
    public PxEndReqCV (boolean endJVM)
    {
        super (ProxyConstants.DS_END_REQ);
        addParm (new PxBooleanParm (endJVM));
    }



}
