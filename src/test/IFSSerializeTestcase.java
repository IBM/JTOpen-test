///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSSerializeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ExtendedIOException;
import com.ibm.as400.access.FileEvent;
import com.ibm.as400.access.FileListener;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;



public class IFSSerializeTestcase extends IFSGenericTestcase
{

  /**
   Constructor.
   **/
  public IFSSerializeTestcase (AS400 systemObject,
      String userid,
      String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSSerializeTestcase",
            namesAndVars, runMode, fileOutputStream, driveLetter, pwrSys);
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup(); 
  }



  class IFSFileListener implements FileListener
  {
    public boolean closed;
    public boolean created;
    public boolean deleted;
    public boolean modified;
    public boolean opened;

    /**
     Invoked when a file has been closed.
     @param event the file event.
     **/
    public void fileClosed(FileEvent event)
    {
      closed = true;
    }

    /**
     Invoked when a file has been created.
     @param event the file event.
     **/
    public void fileCreated(FileEvent event)
    {
      created = true;
    }

    /**
     Invoked when a file is deleted.
     @param event the file event.
     **/
    public void fileDeleted(FileEvent event)
    {
      deleted = true;
    }

    /**
     Invoked when a file has been modified.
     @param event the file event.
     **/
    public void fileModified(FileEvent event)
    {
      modified = true;
    }

    /**
     Invoked when a file has been opened.
     @param event the file event.
     **/
    public void fileOpened(FileEvent event)
    {
      opened = true;
    }

    public void reset()
    {
      closed = false;
      created = false;
      deleted = false;
      modified = false;
      opened = false;
    }
  }

  class IFSPropertyChangeListener implements PropertyChangeListener
  {
    public String propertyName = "";
    public Object oldValue;
    public Object newValue;

    public void propertyChange(PropertyChangeEvent event)
    {
      propertyName = event.getPropertyName();
      oldValue = event.getOldValue();
      newValue = event.getNewValue();
    }

    public void reset()
    {
      propertyName = "";
      oldValue = null;
      newValue = null;
    }
  }

  class IFSVetoableChangeListener implements VetoableChangeListener
  {
    public String propertyName = "";
    private String propertyToVeto = "";
    public Object oldValue;
    public Object newValue;

    public void vetoableChange(PropertyChangeEvent event)
      throws PropertyVetoException
    {
      propertyName = event.getPropertyName();
      oldValue = event.getOldValue();
      newValue = event.getNewValue();
      if (propertyToVeto.equals(propertyName))
        throw new PropertyVetoException("Vetoing", event);
    }

    public void reset()
    {
      propertyName = "";
      oldValue = null;
      newValue = null;
      propertyToVeto = "";
    }

    public void vetoNextChange(String propertyName)
    {
      propertyToVeto = propertyName;
    }
  }

  /**
   Ensure that IFSFile will correctly serialize and deserialize itself.
   Verify that system name and path name are preserved.  Verify that
   listeners aren't preserved.
   **/
  public void Var001(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    StringBuffer failMsg = new StringBuffer();
    try
    {
      IFSFile f1 = new IFSFile(systemObject_, "/IFSFile1");
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSFile f2 = (IFSFile) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      IFSFile file = new IFSFile(systemObject_, "/IFSFile2");
      boolean renamed = f2.renameTo(file);
      boolean deleted = f2.delete();

      if (!systemName1.equals(systemName2))
        failMsg.append("System name not preserved. " +
                       "Expected " + systemName1 +
                       ", got " + systemName2 + ".\n");
      if (!pathName1.equals(pathName2))
        failMsg.append("Path name not preserved. " +
                       "Expected " + pathName1 +
                       ", got " + pathName2 + ".\n deleted="+deleted);
      if (!renamed)
        failMsg.append("!renamed\n");
      if (fl1.closed)
        failMsg.append("fl1.closed\n");
      if (fl1.created)
        failMsg.append("fl1.created\n");
      if (fl1.deleted)
        failMsg.append("fl1.deleted\n");
      if (fl1.modified)
        failMsg.append("fl1.modified\n");
      if (fl1.opened)
        failMsg.append("fl1.opened\n");
      if (!vcl1.propertyName.equals(""))
        failMsg.append("vcl1.propertyName = " + vcl1.propertyName + "\n");
      if (!pcl1.propertyName.equals(""))
        failMsg.append("pcl1.propertyName = " + pcl1.propertyName + "\n");
      if (!fl2.deleted)
        failMsg.append("!fl2.deleted\n");
      if (!vcl2.propertyName.equals("path"))
        failMsg.append("vcl2.propertyName = " + vcl2.propertyName + "\n");
      if (!pcl2.propertyName.equals("path"))
        failMsg.append("pcl2.propertyName = " + pcl2.propertyName + "\n");

      //assertCondition(systemName1.equals(systemName2) && pathName1.equals(pathName2) &&
      //       renamed && !fl1.closed && !fl1.created && !fl1.deleted &&
      //       !fl1.modified && !fl1.opened && vcl1.propertyName.equals("") &&
      //       pcl1.propertyName.equals("") && fl2.deleted &&
      //       vcl2.propertyName.equals("path") &&
      //       pcl2.propertyName.equals("path"));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
    if (failMsg.length() == 0)
      succeeded();
    else
      failed(failMsg.toString());
  }

  /**
   Ensure that IFSFileInputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, and shareOption are preserved.
   Verify that listeners aren't preserved.  Perform the serialization when
   the stream is open.
   **/
  public void Var002(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    IFSFileInputStream file = null; 
    try
    {
      IFSFileInputStream f1 =
        new IFSFileInputStream(systemObject_, "/IFSFile1",
                               IFSFileInputStream.SHARE_NONE);
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSFileInputStream f2 = (IFSFileInputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      boolean sharing = true;
      try
      {
        file =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f1.removeFileListener(fl1);
      f1.close();
      f1.addFileListener(fl1);
      f2.read();
      f2.close();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing, "file="+file);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSFileInputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, and shareOption are preserved.
   Verify that listeners aren't preserved.  Perform the serialization when
   the stream is closed.
   **/
  public void Var003(int runMode)
  {
    IFSFileInputStream file = null; 
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSFileInputStream f1 =
        new IFSFileInputStream(systemObject_, "/IFSFile1",
                               IFSFileInputStream.SHARE_NONE);
      f1.read();
      f1.close();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSFileInputStream f2 = (IFSFileInputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f2.read();
      boolean sharing = true;
      try
      {
       file  =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.close();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing, "file ="+file);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSFileoutputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, append, and shareOption are
   preserved.  Verify that listeners aren't preserved.  Perform the
   serialization when the stream is open.
   **/
  public void Var004(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSFileInputStream f = null; 
      IFSFileOutputStream f1 =
        new IFSFileOutputStream(systemObject_, "/IFSFile1",
                                IFSFileOutputStream.SHARE_NONE, true);
      f1.write(1);
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSFileOutputStream f2 = (IFSFileOutputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      boolean sharing = true;
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f1.removeFileListener(fl1);
      f1.close();
      f1.addFileListener(fl1);
      f2.write(1);
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter > lengthBefore, 
               "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSFileoutputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, append, and shareOption are
   preserved.  Verify that listeners aren't preserved.  Perform the
   serialization when the stream is closed.
   **/
  public void Var005(int runMode)
  {
    IFSFileInputStream f = null; 
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSFileOutputStream f1 =
        new IFSFileOutputStream(systemObject_, "/IFSFile1",
                                IFSFileOutputStream.SHARE_NONE, true);
      f1.write(1);
      f1.close();
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSFileOutputStream f2 = (IFSFileOutputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f2.write(1);
      boolean sharing = true;
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter > lengthBefore, 
               "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }


  /**
   Ensure that IFSRandomAccessFile will correctly serialize and deserialize
   itself.  Verify that system name, path name, existenceOption, mode, and
   shareOption are preserved.  Verify that listeners aren't preserved.
   Perform serialization when the file is open.
   **/
  public void Var006(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSFileInputStream f = null; 
      IFSRandomAccessFile f1 =
        new IFSRandomAccessFile(systemObject_, "/IFSFile1", "rw",
                                IFSRandomAccessFile.SHARE_NONE,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      f1.write(1);
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSRandomAccessFile f2 = (IFSRandomAccessFile) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      boolean sharing = true;
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f1.removeFileListener(fl1);
      f1.close();
      f1.addFileListener(fl1);
      f2.write(1);
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter == lengthBefore, "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSRandomAccessFile will correctly serialize and deserialize
   itself.  Verify that system name, path name, existenceOption, mode, and
   shareOption are preserved.  Verify that listeners aren't preserved.
   Perform serialization when the file is closed.
   **/
  public void Var007(int runMode)
  {
    IFSFileInputStream f = null; 
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSRandomAccessFile f1 =
        new IFSRandomAccessFile(systemObject_, "/IFSFile1", "rw",
                                IFSRandomAccessFile.SHARE_NONE,
                                IFSRandomAccessFile.REPLACE_OR_CREATE);
      f1.write(1);
      f1.close();
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSRandomAccessFile f2 = (IFSRandomAccessFile) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f2.write(1);
      boolean sharing = true;
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter == lengthBefore, "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSTextFileInputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, and shareOption are preserved.
   Verify that listeners aren't preserved.  Perform the serialization when
   the stream is open.
   **/
  public void Var008(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSTextFileInputStream f1 =
        new IFSTextFileInputStream(systemObject_, "/IFSFile1",
                                   IFSFileInputStream.SHARE_NONE);
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSTextFileInputStream f2 = (IFSTextFileInputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      boolean sharing = true;
      IFSFileInputStream file = null; 
      try
      {
        file =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f1.removeFileListener(fl1);
      f1.close();
      f1.addFileListener(fl1);
      f2.read();
      f2.close();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing, "file="+file);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSTextFileInputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, and shareOption are preserved.
   Verify that listeners aren't preserved.  Perform the serialization when
   the stream is closed.
   **/
  public void Var009(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSTextFileInputStream f1 =
        new IFSTextFileInputStream(systemObject_, "/IFSFile1",
                               IFSFileInputStream.SHARE_NONE);
      f1.read();
      f1.close();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSTextFileInputStream f2 = (IFSTextFileInputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f2.read();
      boolean sharing = true;
      IFSFileInputStream file = null; 
      try
      {
        file =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.close();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing, "File = "+file);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSTextFileOutputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, append, and shareOption are
   preserved.  Verify that listeners aren't preserved.  Perform the
   serialization when the stream is open.
   **/
  public void Var010(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSTextFileOutputStream f1 =
        new IFSTextFileOutputStream(systemObject_, "/IFSFile1",
                                IFSFileOutputStream.SHARE_NONE, true, 0x01b5);
//      f1.setCCSID(0x01b5);
      f1.write(1);
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSTextFileOutputStream f2 = (IFSTextFileOutputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      boolean sharing = true;
      IFSFileInputStream f = null; 
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f1.removeFileListener(fl1);
      f1.close();
      f1.addFileListener(fl1);
      f2.write(1);
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) && f2.getCCSID() == 0x01b5 &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter > lengthBefore, "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }

  /**
   Ensure that IFSTextFileOutputStream will correctly serialize and deserialize
   itself.  Verify that system name, path name, append, and shareOption are
   preserved.  Verify that listeners aren't preserved.  Perform the
   serialization when the stream is closed.
   **/
  public void Var011(int runMode)
  {
    if (runMode != ATTENDED && runMode != BOTH) {
      notApplicable("Attended variation"); // the deserialized object contains a deserialized AS400 object, which prompts for password
      return;
    }
    createFile("/IFSFile1");
    createFile("/IFSFile2");
    try
    {
      IFSTextFileOutputStream f1 =
        new IFSTextFileOutputStream(systemObject_, "/IFSFile1",
                                IFSFileOutputStream.SHARE_NONE, true);
      f1.write(1);
      f1.close();
      IFSFile file = new IFSFile(systemObject_, f1.getPath());
      long lengthBefore = file.length();
      IFSFileListener fl1 = new IFSFileListener();
      f1.addFileListener(fl1);
      IFSVetoableChangeListener vcl1 = new IFSVetoableChangeListener();
      f1.addVetoableChangeListener(vcl1);
      IFSPropertyChangeListener pcl1 = new IFSPropertyChangeListener();
      f1.addPropertyChangeListener(pcl1);
      IFSFileOutputStream fos =
        new IFSFileOutputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(f1);
      fos.close();
      IFSFileInputStream fis =
        new IFSFileInputStream(systemObject_, "/IFSSerializeTestcase.ser");
      ObjectInputStream ois = new ObjectInputStream(fis);
      IFSTextFileOutputStream f2 = (IFSTextFileOutputStream) ois.readObject();
      fis.close();
      IFSFileListener fl2 = new IFSFileListener();
      f2.addFileListener(fl2);
      IFSVetoableChangeListener vcl2 = new IFSVetoableChangeListener();
      f2.addVetoableChangeListener(vcl2);
      IFSPropertyChangeListener pcl2 = new IFSPropertyChangeListener();
      f2.addPropertyChangeListener(pcl2);
      String systemName1 = f1.getSystem().getSystemName();
      String systemName2 = f2.getSystem().getSystemName();
      String pathName1 = f1.getPath();
      String pathName2 = f2.getPath();
      f2.setPath("/IFSFile2");
      f2.setPath(f1.getPath());
      f2.write(1);
      boolean sharing = true;
      IFSFileInputStream f = null; 
      try
      {
        f =
          new IFSFileInputStream(systemObject_, f2.getPath());
      }
      catch(Exception e)
      {
        sharing = !exceptionIs(e, "ExtendedIOException",
                              ExtendedIOException.SHARING_VIOLATION);
      }
      f2.close();
      long lengthAfter = file.length();
      assertCondition(systemName1.equals(systemName2) &&
             pathName1.equals(pathName2) &&
             !fl1.closed && !fl1.created && !fl1.deleted && !fl1.modified &&
             !fl1.opened && vcl1.propertyName.equals("") &&
             pcl1.propertyName.equals("") && fl2.opened && fl2.closed &&
             vcl2.propertyName.equals("path") &&
             pcl2.propertyName.equals("path") && !sharing &&
             lengthAfter > lengthBefore, "f="+f);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile("/IFSFile1");
    deleteFile("/IFSFile2");
    deleteFile("/IFSSerializeTestcase.ser");
  }


}



