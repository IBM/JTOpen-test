///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JMBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JM;

import utilities.*;
import com.ibm.as400.access.AS400;

import test.JMTest;
import test.Testcase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JMBeans.  This tests the following
methods of the ToolboxJarMaker class:

<ul compact>
<li>setComponents (Vector,boolean)
</ul>
**/

public class JMBeans
extends Testcase
{

  Vector allBeanFiles_;  // list of all BeanInfo files

/**
Constructor.
**/
    public JMBeans (AS400 systemObject,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream)
    {
        super (systemObject, "JMBeans",
            namesAndVars, runMode, fileOutputStream);
    }



/**
Performs setup needed before running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
     Vector c = new Vector (78);

     c.add ("com/ibm/as400/access/AFPResourceBeanInfo.class");
     c.add ("com/ibm/as400/access/AFPResourceListBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400ArrayBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400BeanInfo.class");
     c.add ("com/ibm/as400/access/AS400CertificateUserProfileUtilBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400CertificateUtilBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400CertificateVldlUtilBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400FileBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400FileRecordDescriptionBeanInfo.class");
     c.add ("com/ibm/as400/access/AS400StructureBeanInfo.class");
     c.add ("com/ibm/as400/access/BaseDataQueueBeanInfo.class");
     c.add ("com/ibm/as400/access/CommandCallBeanInfo.class");
     c.add ("com/ibm/as400/access/DataQueueAttributesBeanInfo.class");
     c.add ("com/ibm/as400/access/DataQueueBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSFileBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSFileInputStreamBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSFileOutputStreamBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSRandomAccessFileBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSTextFileInputStreamBeanInfo.class");
     c.add ("com/ibm/as400/access/IFSTextFileOutputStreamBeanInfo.class");
     c.add ("com/ibm/as400/access/KeyedDataQueueBeanInfo.class");
     c.add ("com/ibm/as400/access/KeyedFileBeanInfo.class");
     c.add ("com/ibm/as400/access/OutputQueueBeanInfo.class");
     c.add ("com/ibm/as400/access/OutputQueueListBeanInfo.class");
     c.add ("com/ibm/as400/access/PrinterBeanInfo.class");
     c.add ("com/ibm/as400/access/PrinterFileBeanInfo.class");
     c.add ("com/ibm/as400/access/PrinterFileListBeanInfo.class");
     c.add ("com/ibm/as400/access/PrinterListBeanInfo.class");
     c.add ("com/ibm/as400/access/PrintObjectBeanInfo.class");
     c.add ("com/ibm/as400/access/PrintObjectListBeanInfo.class");
     c.add ("com/ibm/as400/access/ProgramCallBeanInfo.class");
     c.add ("com/ibm/as400/access/ProgramParameterBeanInfo.class");
     c.add ("com/ibm/as400/access/QSYSObjectPathNameBeanInfo.class");
     c.add ("com/ibm/as400/access/RecordBeanInfo.class");
     c.add ("com/ibm/as400/access/RecordFormatBeanInfo.class");
     c.add ("com/ibm/as400/access/SequentialFileBeanInfo.class");
     c.add ("com/ibm/as400/access/SpooledFileListBeanInfo.class");
     c.add ("com/ibm/as400/access/UserSpaceBeanInfo.class");
     c.add ("com/ibm/as400/access/WriterJobListBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400DetailsModelBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400DetailsPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400ExplorerPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400ListModelBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400ListPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400TreeModelBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/AS400TreePaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/CommandCallButtonBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/CommandCallMenuItemBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/DataQueueDocumentBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/ErrorDialogAdapterBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/IFSTextFileDocumentBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/KeyedDataQueueDocumentBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/ProgramCallButtonBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/ProgramCallMenuItemBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/RecordListFormPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/RecordListTableModelBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/RecordListTablePaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLConnectionBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLQueryBuilderPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLResultSetFormPaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLResultSetTableModelBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLResultSetTablePaneBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLStatementButtonBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLStatementDocumentBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/SQLStatementMenuItemBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VActionAdapterBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VIFSDirectoryBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VJobBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VJobListBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VMessageListBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VMessageQueueBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VPrinterBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VPrinterOutputBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VPrintersBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VPropertiesActionBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/VUserListBeanInfo.class");
     c.add ("com/ibm/as400/vaccess/WorkingCursorAdapterBeanInfo.class");

     allBeanFiles_ = new Vector (c.size ());
     JMTest.copyList (c, allBeanFiles_);

    }



/**
Performs cleanup needed after running variations.
 @exception  Exception  If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
    }

/**
setBeanInfoIncluded() - Run makeJar (), after specifying a
component and "beans included" option.
Verify that the correct additional bean-related files are included.
 **/
    public void Var001 ()
    {
      printVariationStartTime (); // this var may be long-running
      ToolboxJarMaker jm = new ToolboxJarMaker ();
      try {
        Vector inList = new Vector (1);
        inList.add (ToolboxJarMaker.COMMAND_CALL);
        jm.setComponents (inList, true);
        // Remove the destination jar file if it exists.
        JMTest.deleteFile (JMTest.TOOLBOX_JAR_SMALL);
        // Make the jar.
        jm.makeJar (JMTest.TOOLBOX_JAR);

        // Verify that only the correct files got copied.
        Vector expected = new Vector ();
        expected.add ("com/ibm/as400/access/CommandCall.class");
        expected.add ("com/ibm/as400/access/CommandCallBeanInfo.class");
        Vector notExpected = new Vector (allBeanFiles_.size ());
        JMTest.copyList (allBeanFiles_, notExpected);
        notExpected.remove ("com/ibm/as400/access/CommandCall.class");
        notExpected.remove ("com/ibm/as400/access/CommandCallBeanInfo.class");
        if (JMTest.verifyJarContains (JMTest.TOOLBOX_JAR_SMALL,
                                      expected, true) &&
            JMTest.verifyJarNotContains (JMTest.TOOLBOX_JAR_SMALL,
                                         notExpected))
          succeeded ();
        else failed ();
      }
      catch (Exception e) {
        failed (e, "Unexpected Exception");
      }
    }

/**
setBeanInfoIncluded() - Run makeJar () with "no beans".
 Verify that beans files are NOT included.
 NOTE: This scenario gets tested in testcase "JMComp".
 **/

}
