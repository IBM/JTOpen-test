///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSThread.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Thread;

import java.io.PipedInputStream;
import java.io.PrintWriter;
import com.ibm.as400.access.*;

import test.ComponentThread;
import test.Testcase;
import test.ThreadedTestcase;

import java.util.*;
import java.io.*;


class IFSThread
  extends ComponentThread
{
  // Functions that this thread can perform.  One is specified at object construction.
  public static final int LIST            = 0;    // lists content if dir.
  public static final int FILTERED_LIST   = 1;    // lists only part of dir.
  public static final int LOCKED_RD_WR    = 2;    // read/write to locked block of file.
  public static final int INSPECT         = 3;    // inspect file (exists?, directory?, last modification?).
  public static final int DIR_MANIP       = 4;    // manipulate directories.
  public static final int LS_LONG         = 5;    // do 'ls -l" on object.

  private IFSFile     ifsFile      = null;
  private String[]    origListing  = null;  // used for comparison of list().
  private String      filter       = null;  // filter to use during list().
  private String      path         = null;  // pathname.
  private AS400       system       = null;
  private long        modification = 0;     // time ifs object was last modified.
  private long        length       = 0;     // length of a file.


 /**
  * Constructor
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase,  int function)
  {
    super(pipeReader, output, testcase, function);
  }


 /**
  * Constructor for LIST function
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                   ThreadedTestcase testcase, int function,
                   IFSFile file, String[] origListing)
  {
    super(pipeReader, output, testcase, function);
    ifsFile      = file;
    this.origListing = origListing;
  }


 /**
  * Constructor for FILTERED_LIST function
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                  ThreadedTestcase testcase, int function, IFSFile file,
                  String[] origListing, String filter)
  {
    super(pipeReader, output, testcase, function);
    ifsFile          = file;
    this.origListing = origListing;
    this.filter      = filter;
  }


 /**
  * Constructor for LOCKED_RD_WR and DIR_MANIP functions
  * filePath arg should be a unique path of directory to create
  * for DIR_MANIP, while for LOCKED_RD_WR it should be the path
  * to the file to share for reads and writes.
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                  ThreadedTestcase testcase, int function,
                  String filePath, AS400 system)
  {
    super(pipeReader, output, testcase, function);
    this.path        = filePath;
    this.system      = system;
  }


 /**
  * Constructor for INSPECT function on directories.
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                  ThreadedTestcase testcase, int function,
                  IFSFile file, long modification)
  {
    super(pipeReader, output, testcase, function);
    ifsFile           = file;
    this.modification = modification;
  }


 /**
  * Constructor for INSPECT function on files.
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                  ThreadedTestcase testcase, int function,
                  IFSFile file, long modification, long length)
  {
    super(pipeReader, output, testcase, function);
    ifsFile           = file;
    this.modification = modification;
    this.length       = length;
  }


 /**
  * Constructor for LS_LONG.
  */
  public IFSThread(PipedInputStream pipeReader, PrintWriter output,
                  ThreadedTestcase testcase, int function, IFSFile file)
  {
    super(pipeReader, output, testcase, function);
    ifsFile           = file;
  }


  public void run()
  {
    // notify testcase that we are ready to start.
    testcase_.ready();

    // perform the task specified by function, numLoops
    // times unless the stop flag is set.
    for (int i = 0; i < numLoops_ && !stop_ ; i++)
    {
      // don't be a selfish thread...
      try { sleep(1); } catch (InterruptedException e) {}

      switch (function_)
      {
        case LIST:
          performList();
          break;
        case FILTERED_LIST:
          performFilteredList();
          break;
        case LOCKED_RD_WR:
          performLockedRdWr();
          break;
        case INSPECT:
          performInspect();
          break;
        case DIR_MANIP:
          performDirManip();
          break;
        case LS_LONG:
          performLsLong();
          break;
      }
    }

    // done: close our end of pipe and stop.
    this.kill();
  }


  private void performList()
  {
    try
    {
      String[] listing = ifsFile.list();
      if( listing == null )
      {
        if(origListing != null)
          error("IFSList() before and after don't match");
        // don't process null array...
        return;
      }

      if (listing.length != origListing.length)
      {
        error("IFSList() before and after don't match");
        return;
      }

      for (int i=0; i < listing.length; i++)
        if( ! listing[i].equals(origListing[i]) )
          error("IFSList() before and after don't match");
    }
    catch(Exception e)
    {
      error("Unexpected Exception", e);
    }
  }


  private void performFilteredList()
  {
    try
    {
      String[] listing = ifsFile.list(filter);
      if( listing == null )
      {
        if(origListing != null)
          error("IFSList(String) before and after don't match");
        // don't process null array...
        return;
      }

      if (listing.length != origListing.length)
      {
        error("IFSList() before and after don't match");
        return;
      }

      for (int i=0; i < listing.length; i++)
        if( ! listing[i].equals(origListing[i]) )
          error("IFSList(String) before and after don't match");
    }
    catch(Exception e)
    {
      error("Unexpected Exception", e);
    }
  }


  private void performLockedRdWr()
  {
    // loop until we are able to obtain a lock on the required bytes.
    boolean isLockViolation = false;
    do
    {
      try
      {
        // create input & output Streams to file pointed to by path.
        IFSRandomAccessFile raf = new IFSRandomAccessFile(system, path, "rw");
        byte[] data             = getName().getBytes();
        // move to a random location in the first 128 bytes of the file.
        int offset = (int) java.lang.Math.random() * 128;
        raf.seek(offset);
        // lock the file for lengthInBytes bytes starting at offset.
        IFSKey key = raf.lock(offset, data.length);
        raf.write(data);
        // pause to increase chance that another thread
        // attempts to access our location in the file.
        try { sleep(50); } catch (InterruptedException e) {}
        // read back the data and verify it is unchanged
        raf.seek(offset);
        byte[] readData = new byte[data.length];
        raf.read(readData);
        raf.unlock(key);
        if ( ! test.Testcase.isEqual(data, readData) )
          error("Lock in file " + path + " was broken");
        raf.close();
        // no lock violation occurred, note this so we can exit the loop.
        isLockViolation = false;
      }
      catch(ExtendedIOException e)
      {
        if(e.getReturnCode() == ExtendedIOException.LOCK_VIOLATION)
        {
          isLockViolation = true;
          // allow time for file lock to be released.
          try { sleep(50); } catch (InterruptedException ie) {}
        }
        else
          error("ExtendedIOException Thrown", e);
      }
      catch(Exception e)
      {
        isLockViolation = false;
        error("Unexpected Exception", e);
      }
    }
    while (isLockViolation);
  }


  private void performInspect()
  {
    try
    {
      // assume that the object exists.
      if( ifsFile.exists() )
      {
        // if working on a file
        if( length > 0 )  // length set in constructor if working on files.
        {
          if( ifsFile.isFile() )
          {
            if( ifsFile.length() != length )
              error("File lengths don't match");
          }
          else
            error("IFSFile.isFile() returned false on a file");
        }

        // verify modification date.
        if( ifsFile.lastModified() <= (modification - 1500) ||
            ifsFile.lastModified() >= (modification + 1500) )
          error("Modification dates don't match");
      }
      else
       error("IFS Object didn't exist");
    }
    catch( Exception e )
    {
      error("Unexpected Exception", e);
    }
  }


 /**
  * Use the IFSPath object to perform a unix-like
  * "ls -l" listing on the object.  This gives you a
  * listing like:
  * drwxrwxrwx 1 0 owner 1024 May 28 07:48 Filename_of_object
  */
  private void performLsLong()
  {
    try
    {
      StringBuffer listing = new StringBuffer();

      listing.append( ifsFile.isDirectory()?"d":"-" );  // directory or file?
      listing.append( ifsFile.canRead()?"r":"-" );      // read user?
      listing.append( ifsFile.canWrite()?"w":"-" );     // write user?
      listing.append( "------" );                       // no group/all on as400
      listing.append( " " );
      listing.append( "0 0 " );                       // arbitrary major/minor #
      listing.append( "someone " );                   // arbitrary owner
      listing.append( ifsFile.length() );              // object size
      listing.append( " " );
      Date modification = new Date(ifsFile.lastModified());
      listing.append( modification.toString() );
      listing.append( " " );
      listing.append( ifsFile.getName() );

      // print out listing
      output_.println( listing.toString() );
    }
    catch (Exception e)
    {
      error("Unexpected Exception" + e, e);
    }
  }


 /**
  * Attempt to create a directory then rename and set its modification.
  * This is all done using a IFSFile object that is local to this Thread.
  */
  private void performDirManip()
  {
    try
    {
      IFSFile dir = new IFSFile(system, path);
      // delete the directory if it already exists
      if( dir.exists() )
        dir.delete();

      if( ! dir.mkdir() )
      {
        error("Unable to create directory");
        return;
      }

      // perform a unix-like touch command to set the modification time
      long newMod = System.currentTimeMillis();
      dir.setLastModified(newMod);

      if (dir.lastModified() != newMod)
      {
        error("Modified time incorrect");
        return;
      }

      // build the new pathName and rename dir
      StringBuffer newPath = new StringBuffer("/");
      StringTokenizer st   = new StringTokenizer(dir.getPath(), "/");
      // reconstruct path minus old name.
      String token         = st.nextToken();
      while( st.hasMoreTokens() )
      {
        newPath.append( token );
        newPath.append( "/" );
        token = st.nextToken();
      }
      // tak on new dirName.
      newPath.append( getName() );
      newPath.append( ".lib" );

//# CHECK NEW DIR NAME
output_.println(newPath.toString());

      // create a new IFSFile object to rename dir.
      IFSFile newDir = new IFSFile(system, newPath.toString());
      // make sure the new dir doesn't already exist
      if ( newDir.exists() )
        newDir.delete();

      if( ! dir.renameTo(newDir) )
        error("Unable to rename directory to " + getName());

      // clean-up
      newDir.delete();
    }
    catch (Exception e)
    {
      error("Unexpected Exception"+e, e);
    }
  }
}


