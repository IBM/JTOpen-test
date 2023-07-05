///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSEventTestcase.java
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
import java.util.Hashtable;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.FileEvent;
import com.ibm.as400.access.FileListener;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileDescriptor;
import com.ibm.as400.access.IFSFileInputStream;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSRandomAccessFile;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;



public class IFSEventTestcase extends IFSGenericTestcase
{

  /**
   Constructor.
   **/
  public IFSEventTestcase (AS400 systemObject,
        String userid,
        String password,
                   Hashtable namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   
                   String   driveLetter,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSEventTestcase", 
            namesAndVars, runMode, fileOutputStream, driveLetter, pwrSys);
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  protected void setup()
    throws Exception
  {
    super.setup();

    // Setup the ifsPathName_ to test with
    fileName_ = "IFSEventTestcaseFile"; 
    ifsPathName_ = ifsDirName_ + fileName_;  

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
   Ensure that IFSFile.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var001()
  {
    try
    {
      createFile(ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    } 
    deleteFile(ifsPathName_); 
  }

  /**
   Ensure that IFSFile.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var002()
  {
    try
    {

      createFile(ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    deleteFile(ifsPathName_); 

  }

  /**
   Ensure that IFSFile.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var003()
  {

    try
    {
      createFile(ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    deleteFile(ifsPathName_); 

  }

  /**
   Ensure that IFSFile.delete() fires a FileEvent indicating
   FILE_DELETED if the file is successfully deleted.
   **/
  public void Var004()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      boolean deleted = file.delete();
      assertCondition(deleted && listener.deleted && !listener.opened &&
             !listener.closed && !listener.modified);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.delete() doesn't fire a FileEvent if the file
   can't be deleted.
   **/
  public void Var005()
  {

    deleteFile(ifsPathName_);

    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      boolean deleted = file.delete();
      assertCondition(!deleted && !listener.deleted && !listener.opened &&
             !listener.closed && !listener.modified,
		"deleted="+deleted+" listener.deleted="+listener.deleted+
		      " listener.opened="+listener.opened+" listener.closed="+listener.closed+" listener.modified="+listener.modified  );
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) fires a VetoableChangeEvent for
   path.  Validate the old and new values.
   **/
  public void Var006()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      IFSFile targetFile = new IFSFile(systemObject_, newName);
      file.renameTo(targetFile);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(oldName) &&
             listener.newValue.equals(newName));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
    deleteFile(ifsPathName_ + "1");
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) doesn't rename the file if the
   change is vetoed.
   **/
  public void Var007()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      IFSFile targetFile = new IFSFile(systemObject_, newName);
      boolean vetoed = false;
      boolean renamed = false;
      try
      {
        renamed = file.renameTo(targetFile);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && !renamed && file.exists() &&
             file.getPath().equals(oldName));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) fires a PropertyChangeEvent
   for path if successful.  Validate the old and new values.
   **/
  public void Var008()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      IFSFile targetFile = new IFSFile(systemObject_, newName);
      boolean renamed = file.renameTo(targetFile);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(oldName) &&
             listener.newValue.equals(newName), "renamed="+renamed);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
    deleteFile(ifsPathName_ + "1");
  }

  /**
   Ensure that IFSFile.renameTo(IFSFile) doesn't fire a PropertyChangeEvent
   if not successful.
   **/
  public void Var009()
  {
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      boolean renamed = false;
      try
      {
        IFSFile targetFile = new IFSFile(systemObject_, newName);
        renamed = file.renameTo(targetFile);
      }
      catch(Exception e) {}
      assertCondition(listener.propertyName.equals("") && !renamed);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
    deleteFile(ifsPathName_ + "1");
  }

  /**
   Ensure that IFSFile.setPath(String) fires a VetoableChangeEvent for
   path.  Validate the old and new values.
   **/
  public void Var010()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      file.setPath(newName);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(oldName) &&
             listener.newValue.equals(newName));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setPath(String) doesn't alter the path if the
   change is vetoed.
   **/
  public void Var011()
  {
    try
    {
      createFile(ifsPathName_);
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      boolean vetoed = false;
      try
      {
        file.setPath(newName);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(oldName));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var012()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      String oldName = file.getPath();
      String newName = oldName + "1";
      file.setPath(newName);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(oldName) &&
             listener.newValue.equals(newName));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setLastModified(long) fires a VetoableChangeEvent
   for lastModified.  Validate the new value.
   **/
  public void Var013()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Long modifiedTime = new Long(1230000);
      file.setLastModified(modifiedTime.longValue());
      assertCondition(listener.propertyName.equals("lastModified") &&
             listener.newValue.equals(modifiedTime));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setLastModified(long) doesn't alter the last
   modified time if the change is vetoed.
   **/
  public void Var014()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("lastModified");
      file.addVetoableChangeListener(listener);
      long modifiedTime = 1230000L;
      boolean vetoed = false;
      try
      {
        file.setLastModified(modifiedTime);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("lastModified"));
      }
      assertCondition(vetoed && file.lastModified() != modifiedTime);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setLastModified(long) fires a PropertyChangeEvent
   for lastModified if successful.  Validate the new value.
   **/
  public void Var015()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Long modifiedTime = new Long(1230000);
      file.setLastModified(modifiedTime.longValue());
      assertCondition(listener.propertyName.equals("lastModified") &&
             listener.newValue.equals(modifiedTime));
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.setLastModified(long) doesn't fire a
   PropertyChangeEvent if not successful.
   **/
  public void Var016()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);

      boolean modified = false;
      try
      {
        modified = file.setLastModified(-123456L);
      }
      catch(Exception e) {
	  e.printStackTrace(); 
      }
      assertCondition(listener.propertyName.equals("") && !modified, "listener.propertyName="+listener.propertyName+" modified="+modified);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.setLastModified(long) fires a FileEvent
   for lastModified indicating FILE_MODIFIED if successful.
   **/
  public void Var017()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      boolean modified = file.setLastModified(System.currentTimeMillis());
      assertCondition(modified && !listener.deleted && !listener.opened &&
             !listener.closed && listener.modified);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.setLastModified(long) doesn't fire a FileEvent
   if not successful.
   **/
  public void Var018()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      boolean modified = false;
      try
      {
        modified = file.setLastModified(-123456L);
      }
      catch(Exception e) {}
      assertCondition(!listener.deleted && !listener.opened &&
             !listener.closed && !listener.modified && !modified, "listener.deleted="+listener.deleted+
		      " listener.opened="+listener.opened+
		      " listener.closed="+listener.closed+
                      " listener.modified="+listener.modified+
                      " modified="+modified);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.setSystem(AS400) fires a VetoableChangeEvent for
   system.  Validate the old and new values.
   **/
  public void Var019()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setSystem(AS400) doesn't alter the system if the
   change is vetoed.
   **/
  public void Var020()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var021()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400("rchaslkb");
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var022()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFile.removePropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var023()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFile.removeVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var024()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFile.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var025()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      boolean modified = file.setLastModified(System.currentTimeMillis());
      assertCondition(modified && !listener2.deleted && !listener2.opened &&
             !listener2.closed && listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFile.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var026()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400("rchaslkb");
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFile.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var027()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var028()
  {
    createFile(ifsPathName_);
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream(systemObject_, ifsPathName_);
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", new StringBuffer("working on "+ifsPathName_));
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileInputStream.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var029()
  {
    createFile(ifsPathName_);
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream(systemObject_, ifsPathName_);
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileInputStream.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var030()
  {
    createFile(ifsPathName_);
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream(systemObject_, ifsPathName_);
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", new StringBuffer("accessing "+ifsPathName_));
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.available() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var031()
  {
    createFile(ifsPathName_);
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.available();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.close() fires a FileEvent indicating
   FILE_CLOSED when the file is open.
   **/
  public void Var032()
  {
    createFile(ifsPathName_);
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.close() doesn't fire a FileEvent when
   the file is already closed.
   **/
  public void Var033()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file =
        new IFSFileInputStream(systemObject_, ifsPathName_);
      file.close();
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.lock(int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var034()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.lock(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.read() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var035()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.read(byte[]) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var036()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read(new byte[1]);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.read(byte[], int, int) fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var037()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read(new byte[1], 0, 1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setFD(IFSFileDescriptor) fires a
   VetoableChangeEvent for FD.  Validate the old and new values.
   **/
  public void Var038()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setFD(IFSFileDescriptor) doesn't alter FD
   if the change is vetoed.
   **/
  public void Var039()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("FD");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setFD(newFD);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("FD"));
      }
      assertCondition(vetoed && file.getFD() == oldFD);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setFD(IFSFileDescriptor) fires a
   PropertyChangeEvent for FD.  Validate the old and new value.
   **/
  public void Var040()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.setPath(String) fires a VetoableChangeEvent
   for path.  Validate the old and new values.
   **/
  public void Var041()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setPath(String) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var042()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setPath(newPath);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(ifsPathName_));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var043()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setShareOption(int) fires a
   VetoableChangeEvent for shareOption.  Validate the old and new values.
   **/
  public void Var044()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      Integer oldShareOption = new Integer(IFSFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileInputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setShareOption(int) doesn't alter
   shareOption if the change is vetoed.
   **/
  public void Var045()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      Integer oldShareOption = new Integer(IFSFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("shareOption");
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileInputStream.SHARE_ALL);
      boolean vetoed = false;
      try
      {
        file.setShareOption(newShareOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("shareOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setShareOption(int) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var046()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      Integer oldShareOption = new Integer(IFSFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileInputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setSystem(AS400) fires a VetoableChangeEvent
   for system.  Validate the old and new values.
   **/
  public void Var047()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setSystem(AS400) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var048()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var049()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileInputStream.skip(long) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var050()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.skip(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileInputStream.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var051()
  {
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream();
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSFileInputStream.removePropertyChangeListener(PropertyChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var052()
  {
    IFSFileInputStream file = null;
    try
    {
      file = new IFSFileInputStream();
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSFileInputStream.removeVetoableChangeListener(VetoableChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var053()
  {
    IFSFileInputStream file = null;
    try
    {
       file = new IFSFileInputStream();
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}

  }

  /**
   Ensure that IFSFileInputStream.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var054()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      file.available();
      assertCondition(!listener2.deleted && listener2.opened &&
             !listener2.closed && !listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileInputStream.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var055()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that
   IFSFileInputStream.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var056()
  {
    try
    {
      IFSFileInputStream file = new IFSFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var057()
  {
    createFile(ifsPathName_);
    IFSFileOutputStream file = null;
    try
    {
      file = new IFSFileOutputStream(systemObject_, ifsPathName_);
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileOutputStream.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var058()
  {
    createFile(ifsPathName_);
    IFSFileOutputStream file = null;
    try
    {
       file =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileOutputStream.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var059()
  {
    createFile(ifsPathName_);
    IFSFileOutputStream file = null;
    try
    {
      file = new IFSFileOutputStream(systemObject_, ifsPathName_);
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.flush() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var060()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.flush();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.close() fires a FileEvent indicating
   FILE_CLOSED when the file is open.
   **/
  public void Var061()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.close() doesn't fire a FileEvent when
   the file is already closed.
   **/
  public void Var062()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file =
        new IFSFileOutputStream(systemObject_, ifsPathName_);
      file.close();
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.lock(int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var063()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.lock(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.write(int) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var064()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[]) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var065()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1]);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.write(byte[], int, int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var066()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1], 0, 1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setFD(IFSFileDescriptor) fires a
   VetoableChangeEvent for FD.  Validate the old and new values.
   **/
  public void Var067()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setFD(IFSFileDescriptor) doesn't alter FD
   if the change is vetoed.
   **/
  public void Var068()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("FD");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setFD(newFD);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("FD"));
      }
      assertCondition(vetoed && file.getFD() == oldFD);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setFD(IFSFileDescriptor) fires a
   PropertyChangeEvent for FD.  Validate the old and new value.
   **/
  public void Var069()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSFileOutputStream.setPath(String) fires a VetoableChangeEvent
   for path.  Validate the old and new values.
   **/
  public void Var070()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setPath(String) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var071()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setPath(newPath);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(ifsPathName_));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var072()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setShareOption(int) fires a
   VetoableChangeEvent for shareOption.  Validate the old and new values.
   **/
  public void Var073()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setShareOption(int) doesn't alter
   shareOption if the change is vetoed.
   **/
  public void Var074()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("shareOption");
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      boolean vetoed = false;
      try
      {
        file.setShareOption(newShareOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("shareOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setShareOption(int) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var075()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setSystem(AS400) fires a VetoableChangeEvent
   for system.  Validate the old and new values.
   **/
  public void Var076()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setSystem(AS400) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var077()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var078()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var079()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSFileOutputStream.removePropertyChangeListener(PropertyChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var080()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSFileOutputStream.removeVetoableChangeListener(VetoableChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var081()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSFileOutputStream.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var082()
  {
    createFile(ifsPathName_);
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      file.flush();
      assertCondition(!listener2.deleted && listener2.opened &&
             !listener2.closed && !listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSFileOutputStream.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var083()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that
   IFSFileOutputStream.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var084()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setAppend(boolean) fires a
   VetoableChangeEvent for path.  Validate the old and new values.
   **/
  public void Var085()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setAppend(newAppend.booleanValue());
      assertCondition(listener.propertyName.equals("append") &&
             listener.oldValue.equals(oldAppend) &&
             listener.newValue.equals(newAppend));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setAppend(boolean) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var086()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("append");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setAppend(newAppend.booleanValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("append"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSFileOutputStream.setAppend(boolean) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var087()
  {
    try
    {
      IFSFileOutputStream file = new IFSFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setAppend(newAppend.booleanValue());
      assertCondition(listener.propertyName.equals("append") &&
             listener.oldValue.equals(oldAppend) &&
             listener.newValue.equals(newAppend));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var088()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
       file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSRandomAccessFile.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var089()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
       file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSRandomAccessFile.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var090()
  {
    createFile(ifsPathName_);
    IFSRandomAccessFile file = null;
    try
    {
      file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that IFSRandomAccessFile.close() fires a FileEvent indicating
   FILE_CLOSED when the file is open.
   **/
  public void Var091()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.close() doesn't fire a FileEvent when
   the file is already closed.
   **/
  public void Var092()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      file.close();
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.flush() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var093()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.flush();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.length() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var094()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.length();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.lock(int, int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var095()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.lock(0, 1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.read() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var096()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.read();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[]) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var097()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.read(new byte[1]);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.read(byte[], int, int) fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var098()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.read(new byte[1], 0, 1);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readBoolean() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var099()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readBoolean();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readByte() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var100()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readByte();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readChar() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var101()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readChar();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readDouble() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var102()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readDouble();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readFloat() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var103()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readFloat();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[]) fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var104()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readFully(new byte[1]);
      Long newFilePointer = new Long(1);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened, "newFilePointer="+newFilePointer);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readFully(byte[], int, int) fires a
   FileEvent indicating FILE_OPENED.
   **/
  public void Var105()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.readFully(new byte[1], 0, 1);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
   }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readInt() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var106()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readInt();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readLine() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var107()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      Long newFilePointer = new Long(file.length());
      file.readLine();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened, "newFilePointer="+newFilePointer);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readLong() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var108()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readLong();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readShort() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var109()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readShort();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedByte() fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var110()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readUnsignedByte();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readUnsignedShort() fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var111()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readUnsignedShort();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.readUTF() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var112()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile raf =
        new IFSRandomAccessFile(systemObject_, ifsPathName_, "rw");
      raf.writeUTF("blah blah blah");
      raf.close();
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.readUTF();
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var113()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSRandomAccessFile.removePropertyChangeListener(PropertyChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var114()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSRandomAccessFile.removeVetoableChangeListener(VetoableChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var115()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSRandomAccessFile.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var116()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      file.length();
      assertCondition(!listener2.deleted && listener2.opened &&
             !listener2.closed && !listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSRandomAccessFile.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var117()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that
   IFSRandomAccessFile.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var118()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.seek(long) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var119()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.seek(1);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.setPath(String) fires a VetoableChangeEvent
   for path.  Validate the old and new values.
   **/
  public void Var120()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setPath(String) doesn't alter the path
   if the change is vetoed.
   **/
  public void Var121()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setPath(newPath);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(ifsPathName_));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var122()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) fires a
   VetoableChangeEvent for shareOption.  Validate the old and new values.
   **/
  public void Var123()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
   }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) doesn't alter
   shareOption if the change is vetoed.
   **/
  public void Var124()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("shareOption");
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      boolean vetoed = false;
      try
      {
        file.setShareOption(newShareOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("shareOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setShareOption(int) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var125()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldShareOption = new Integer(IFSFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newShareOption = new Integer(IFSFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setSystem(AS400) fires a VetoableChangeEvent
   for system.  Validate the old and new values.
   **/
  public void Var126()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setSystem(AS400) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var127()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var128()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setExistenceOption(int) fires a
   VetoableChangeEvent for existenceOption.  Validate the old and new values.
   **/
  public void Var129()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldExistenceOption =
        new Integer(IFSRandomAccessFile.OPEN_OR_CREATE);
      file.setExistenceOption(oldExistenceOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newExistenceOption =
        new Integer(IFSRandomAccessFile.REPLACE_OR_FAIL);
      file.setExistenceOption(newExistenceOption.intValue());
      assertCondition(listener.propertyName.equals("existenceOption") &&
             listener.oldValue.equals(oldExistenceOption) &&
             listener.newValue.equals(newExistenceOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setExistenceOption(int) doesn't alter
   existenceOption if the change is vetoed.
   **/
  public void Var130()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldExistenceOption =
        new Integer(IFSRandomAccessFile.OPEN_OR_CREATE);
      file.setExistenceOption(oldExistenceOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("existenceOption");
      file.addVetoableChangeListener(listener);
      Integer newExistenceOption =
        new Integer(IFSRandomAccessFile.REPLACE_OR_FAIL);
      boolean vetoed = false;
      try
      {
        file.setExistenceOption(newExistenceOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("existenceOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setExistenceOption(int) fires a
   PropertyChangeEvent for existenceOption.  Validate the old and new value.
   **/
  public void Var131()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      Integer oldExistenceOption =
        new Integer(IFSRandomAccessFile.OPEN_OR_CREATE);
      file.setExistenceOption(oldExistenceOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newExistenceOption =
        new Integer(IFSRandomAccessFile.REPLACE_OR_FAIL);
      file.setExistenceOption(newExistenceOption.intValue());
      assertCondition(listener.propertyName.equals("existenceOption") &&
             listener.oldValue.equals(oldExistenceOption) &&
             listener.newValue.equals(newExistenceOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setMode(String) fires a VetoableChangeEvent
   for mode.  Validate the old and new values.
   **/
  public void Var132()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String oldMode = "r";
      String newMode = "rw";
      file.setMode(oldMode);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setMode(newMode);
      assertCondition(listener.propertyName.equals("mode") &&
             listener.oldValue.equals(oldMode) &&
             listener.newValue.equals(newMode));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setMode(String) doesn't alter the mode
   if the change is vetoed.
   **/
  public void Var133()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String oldMode = "r";
      String newMode = "rw";
      file.setMode(oldMode);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("mode");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setMode(newMode);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("mode"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.setMode(String) fires a PropertyChangeEvent
   for mode.  Validate the old and new value.
   **/
  public void Var134()
  {
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      String oldMode = "r";
      String newMode = "rw";
      file.setMode(oldMode);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setMode(newMode);
      assertCondition(listener.propertyName.equals("mode") &&
             listener.oldValue.equals(oldMode) &&
             listener.newValue.equals(newMode));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSRandomAccessFile.skipBytes(int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var135()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener fileListener = new IFSFileListener();
      file.addFileListener(fileListener);
      file.skipBytes(1);
      assertCondition(!fileListener.closed && !fileListener.deleted &&
             !fileListener.modified && fileListener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.write(int) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var136()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[]) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var137()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1]);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.write(byte[]) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var138()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1], 0, 1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeBoolean(boolean) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var139()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeBoolean(true);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeByte(int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var140()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.writeByte(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeBytes(String) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var141()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.writeBytes("12345");
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeChar(int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var142()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.writeChar(65);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeChars(String) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var143()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeChars("1234");
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeDouble(double) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var144()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeDouble(1.0);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeFloat(float) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var145()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.writeFloat((float) 1.0);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeInt(int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var146()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      IFSPropertyChangeListener propertyListener =
        new IFSPropertyChangeListener();
      file.addPropertyChangeListener(propertyListener);
      file.writeInt(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeLong(long) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var147()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeLong(1L);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeShort(int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var148()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeShort(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSRandomAccessFile.writeUTF(String) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var149()
  {
    createFile(ifsPathName_);
    try
    {
      IFSRandomAccessFile file = new IFSRandomAccessFile();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      file.setMode("rw");
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.writeUTF("1234");
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }


  /**
   Ensure that IFSTextFileInputStream.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var150()
  {
    createFile(ifsPathName_);
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream(systemObject_, ifsPathName_);
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileInputStream.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var151()
  {
    createFile(ifsPathName_);
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream(systemObject_, ifsPathName_);
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileInputStream.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var152()
  {
    createFile(ifsPathName_);
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream(systemObject_, ifsPathName_);
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.available() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var153()
  {
    createFile(ifsPathName_);
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.available();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.close() fires a FileEvent indicating
   FILE_CLOSED when the file is open.
   **/
  public void Var154()
  {
    createFile(ifsPathName_);
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.close() doesn't fire a FileEvent when
   the file is already closed.
   **/
  public void Var155()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file =
        new IFSTextFileInputStream(systemObject_, ifsPathName_);
      file.close();
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.lock(int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var156()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.lock(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.read() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var157()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.read(byte[]) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var158()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read(new byte[1]);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.read(byte[], int, int) fires a FileEvent
   indicating FILE_OPENED.
   **/
  public void Var159()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.read(new byte[1], 0, 1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.setFD(IFSFileDescriptor) fires a
   VetoableChangeEvent for FD.  Validate the old and new values.
   **/
  public void Var160()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.setFD(IFSFileDescriptor) doesn't alter FD
   if the change is vetoed.
   **/
  public void Var161()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("FD");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setFD(newFD);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("FD"));
      }
      assertCondition(vetoed && file.getFD() == oldFD);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.setFD(IFSFileDescriptor) fires a
   PropertyChangeEvent for FD.  Validate the old and new value.
   **/
  public void Var162()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.setPath(String) fires a VetoableChangeEvent
   for path.  Validate the old and new values.
   **/
  public void Var163()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setPath(String) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var164()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setPath(newPath);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(ifsPathName_));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var165()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setShareOption(int) fires a
   VetoableChangeEvent for shareOption.  Validate the old and new values.
   **/
  public void Var166()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      Integer oldShareOption = new Integer(IFSTextFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileInputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setShareOption(int) doesn't alter
   shareOption if the change is vetoed.
   **/
  public void Var167()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      Integer oldShareOption = new Integer(IFSTextFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("shareOption");
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileInputStream.SHARE_ALL);
      boolean vetoed = false;
      try
      {
        file.setShareOption(newShareOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("shareOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setShareOption(int) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var168()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      Integer oldShareOption = new Integer(IFSTextFileInputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileInputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setSystem(AS400) fires a VetoableChangeEvent
   for system.  Validate the old and new values.
   **/
  public void Var169()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setSystem(AS400) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var170()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var171()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileInputStream.skip(long) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var172()
  {
    createFile(ifsPathName_, "some data");
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.skip(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileInputStream.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var173()
  {
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream();
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSTextFileInputStream.removePropertyChangeListener(PropertyChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var174()
  {
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream();
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
  }

  /**
   Ensure that
   IFSTextFileInputStream.removeVetoableChangeListener(VetoableChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var175()
  {
    IFSTextFileInputStream file = null;
    try
    {
      file = new IFSTextFileInputStream();
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}

  }

  /**
   Ensure that IFSTextFileInputStream.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var176()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      file.available();
      assertCondition(!listener2.deleted && listener2.opened &&
             !listener2.closed && !listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileInputStream.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var177()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that
   IFSTextFileInputStream.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var178()
  {
    try
    {
      IFSTextFileInputStream file = new IFSTextFileInputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.addFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var179()
  {
    createFile(ifsPathName_);
    IFSTextFileOutputStream file = null;
    try
    {
      file = new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      file.addFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileOutputStream.addPropertyChangeListener(PropertyChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var180()
  {
    createFile(ifsPathName_);
    IFSTextFileOutputStream file = null;
    try
    {
      file =
        new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      file.addPropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileOutputStream.addVetoableChangeListener(VetoableChangeListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var181()
  {
    createFile(ifsPathName_);
    IFSTextFileOutputStream file = null;
    try
    {
      file = new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      file.addVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
    try { file.close(); } catch(Exception e) {}
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.flush() fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var182()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.flush();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.close() fires a FileEvent indicating
   FILE_CLOSED when the file is open.
   **/
  public void Var183()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file =
        new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.close() doesn't fire a FileEvent when
   the file is already closed.
   **/
  public void Var184()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file =
        new IFSTextFileOutputStream(systemObject_, ifsPathName_);
      file.close();
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.close();
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             !listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.lock(int) fires a FileEvent indicating
   FILE_OPENED.
   **/
  public void Var185()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.lock(1);
      assertCondition(!listener.closed && !listener.deleted && !listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.write(int) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var186()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.write(byte[]) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var187()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1]);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.write(byte[], int, int) fires a FileEvent
   indicating FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var188()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write(new byte[1], 0, 1);
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.setFD(IFSFileDescriptor) fires a
   VetoableChangeEvent for FD.  Validate the old and new values.
   **/
  public void Var189()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.setFD(IFSFileDescriptor) doesn't alter FD
   if the change is vetoed.
   **/
  public void Var190()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("FD");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setFD(newFD);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("FD"));
      }
      assertCondition(vetoed && file.getFD() == oldFD);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.setFD(IFSFileDescriptor) fires a
   PropertyChangeEvent for FD.  Validate the old and new value.
   **/
  public void Var191()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      IFSFileDescriptor oldFD = new IFSFileDescriptor();
      IFSFileDescriptor newFD = new IFSFileDescriptor();
      file.setFD(oldFD);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setFD(newFD);
      assertCondition(listener.propertyName.equals("FD") &&
             listener.oldValue.equals(oldFD) &&
             listener.newValue.equals(newFD));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that IFSTextFileOutputStream.setPath(String) fires a VetoableChangeEvent
   for path.  Validate the old and new values.
   **/
  public void Var192()
  {
    createFile(ifsPathName_);

    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setPath(String) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var193()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("path");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setPath(newPath);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("path"));
      }
      assertCondition(vetoed && file.getPath().equals(ifsPathName_));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setPath(String) fires a PropertyChangeEvent
   for path.  Validate the old and new value.
   **/
  public void Var194()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      String newPath = ifsPathName_ + "1";
      file.setPath(ifsPathName_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setPath(newPath);
      assertCondition(listener.propertyName.equals("path") &&
             listener.oldValue.equals(ifsPathName_) &&
             listener.newValue.equals(newPath));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setShareOption(int) fires a
   VetoableChangeEvent for shareOption.  Validate the old and new values.
   **/
  public void Var195()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Integer oldShareOption = new Integer(IFSTextFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setShareOption(int) doesn't alter
   shareOption if the change is vetoed.
   **/
  public void Var196()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Integer oldShareOption = new Integer(IFSTextFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("shareOption");
      file.addVetoableChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileOutputStream.SHARE_ALL);
      boolean vetoed = false;
      try
      {
        file.setShareOption(newShareOption.intValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("shareOption"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setShareOption(int) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var197()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Integer oldShareOption = new Integer(IFSTextFileOutputStream.SHARE_NONE);
      file.setShareOption(oldShareOption.intValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      Integer newShareOption = new Integer(IFSTextFileOutputStream.SHARE_ALL);
      file.setShareOption(newShareOption.intValue());
      assertCondition(listener.propertyName.equals("shareOption") &&
             listener.oldValue.equals(oldShareOption) &&
             listener.newValue.equals(newShareOption));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setSystem(AS400) fires a VetoableChangeEvent
   for system.  Validate the old and new values.
   **/
  public void Var198()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setSystem(AS400) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var199()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("system");
      file.addVetoableChangeListener(listener);
      AS400 system = new AS400();
      boolean vetoed = false;
      try
      {
        file.setSystem(system);
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("system"));
      }
      assertCondition(vetoed && file.getSystem() == systemObject_);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setSystem(AS400) fires a PropertyChangeEvent
   for system.  Validate the old and new value.
   **/
  public void Var200()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener.propertyName.equals("system") &&
             listener.oldValue.equals(systemObject_) &&
             listener.newValue.equals(newSystem));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.removeFileListener(FileListener) throws
   NullPointerException if argument one is null.
   **/
  public void Var201()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.removeFileListener((FileListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSTextFileOutputStream.removePropertyChangeListener(PropertyChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var202()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.removePropertyChangeListener((PropertyChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that
   IFSTextFileOutputStream.removeVetoableChangeListener(VetoableChangeListener)
   throws NullPointerException if argument one is null.
   **/
  public void Var203()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.removeVetoableChangeListener((VetoableChangeListener) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException");
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.removeFileListener(FileListener) removes the
   specified listener.
   <ul>
   <li>Add two FileListeners
   <li>Remove one FileListener
   <li>Cause a FileEvent
   <li>Verify that the remaining FileListener recieves the event
   <li>Verify that the removed FileListener does not
   </ul>
   **/
  public void Var204()
  {
    createFile(ifsPathName_);
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener1 = new IFSFileListener();
      file.addFileListener(listener1);
      IFSFileListener listener2 = new IFSFileListener();
      file.addFileListener(listener2);
      file.removeFileListener(listener1);
      file.flush();
      assertCondition(!listener2.deleted && listener2.opened &&
             !listener2.closed && !listener2.modified &&
             !listener1.deleted && !listener1.opened &&
             !listener1.closed && !listener1.modified);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteFile(ifsPathName_);
  }

  /**
   Ensure that
   IFSTextFileOutputStream.removePropertyChangeListener(PropertyChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two PropertyChangeListener
   <li>Remove one PropertyChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining PropertyChangeListener recieves the event
   <li>Verify that the removed PropertyChangeListener does not
   </ul>
   **/
  public void Var205()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      IFSPropertyChangeListener listener1 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener1);
      IFSPropertyChangeListener listener2 = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener2);
      file.removePropertyChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that
   IFSTextFileOutputStream.removeVetoableChangeListener(VetoableChangeListener)
   removes the specified listener.
   <ul>
   <li>Add two VetoableChangeListener
   <li>Remove one VetoableChangeListener
   <li>Cause a PropertyChangeEvent
   <li>Verify that the remaining VetoableChangeListener recieves the event
   <li>Verify that the removed VetoableChangeListener does not
   </ul>
   **/
  public void Var206()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      IFSVetoableChangeListener listener1 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener1);
      IFSVetoableChangeListener listener2 = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener2);
      file.removeVetoableChangeListener(listener1);
      AS400 newSystem = new AS400();
      file.setSystem(newSystem);
      assertCondition(listener2.propertyName.equals("system") &&
             listener2.oldValue.equals(systemObject_) &&
             listener2.newValue.equals(newSystem) &&
             listener1.propertyName.equals(""));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setAppend(boolean) fires a
   VetoableChangeEvent for path.  Validate the old and new values.
   **/
  public void Var207()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      file.addVetoableChangeListener(listener);
      file.setAppend(newAppend.booleanValue());
      assertCondition(listener.propertyName.equals("append") &&
             listener.oldValue.equals(oldAppend) &&
             listener.newValue.equals(newAppend));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setAppend(boolean) doesn't alter the system
   if the change is vetoed.
   **/
  public void Var208()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSVetoableChangeListener listener = new IFSVetoableChangeListener();
      listener.vetoNextChange("append");
      file.addVetoableChangeListener(listener);
      boolean vetoed = false;
      try
      {
        file.setAppend(newAppend.booleanValue());
      }
      catch(PropertyVetoException e)
      {
        PropertyChangeEvent event = e.getPropertyChangeEvent();
        vetoed = (event.getPropertyName().equals("append"));
      }
      assertCondition(vetoed);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.setAppend(boolean) fires a
   PropertyChangeEvent for path.  Validate the old and new value.
   **/
  public void Var209()
  {
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      Boolean oldAppend = new Boolean(true);
      Boolean newAppend = new Boolean(!oldAppend.booleanValue());
      file.setAppend(oldAppend.booleanValue());
      IFSPropertyChangeListener listener = new IFSPropertyChangeListener();
      file.addPropertyChangeListener(listener);
      file.setAppend(newAppend.booleanValue());
      assertCondition(listener.propertyName.equals("append") &&
             listener.oldValue.equals(oldAppend) &&
             listener.newValue.equals(newAppend));
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
  }

  /**
   Ensure that IFSTextFileOutputStream.write(String) fires a FileEvent indicating
   FILE_OPENED and a FileEvent indicating FILE_MODIFIED.
   **/
  public void Var210()
  {
    createIFSFile(ifsPathName_);  // avoid UnsupportedEncodingException
    try
    {
      IFSTextFileOutputStream file = new IFSTextFileOutputStream();
      file.setSystem(systemObject_);
      file.setPath(ifsPathName_);
      IFSFileListener listener = new IFSFileListener();
      file.addFileListener(listener);
      file.write("The quick brown fox.");
      file.flush(); // need this to force write before the close
      assertCondition(!listener.closed && !listener.deleted && listener.modified &&
             listener.opened);
      file.close();
    }
    catch(Exception e)
    {
      failed(e);
    }
    deleteIFSFile(ifsPathName_);
  }
}





