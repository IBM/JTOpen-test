///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDXMLErrorListener.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDXMLErrorListener.java
//
// Classes:      JDXMLErrorListener
//
////////////////////////////////////////////////////////////////////////


package test;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

public class JDXMLErrorListener implements ErrorListener {

  public void warning(TransformerException ex) throws TransformerException {
      throw ex; 
  }

  public void error(TransformerException ex) throws TransformerException {
    throw ex; 
  }

  public void fatalError(TransformerException ex) throws TransformerException {
    throw ex; 
  }
}
