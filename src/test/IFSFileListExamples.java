///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSFileListExamples.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;


public class IFSFileListExamples 
{
  public static void main(String args[]) 
  {
    try
    {
      // IFSFile.list(IFSFileFilter) and IFSFile.list(String) examples.

      // Create the AS400 and IFSFile objects.
      AS400 system = new AS400();
      IFSFile directory = new IFSFile(system, "/QSYS.LIB");

      // Generate a list of all subdirectories and print the names.
      String[] subDirNames = directory.list(new DirectoryFilter());
      if (subDirNames != null)
        for (int i = 0; i < subDirNames.length; i++)
          System.out.println(subDirNames[i]);
      else
        System.out.println("No sub-directories.");

      // Generate a list of all user profiles and print the names.
      String[] profileNames = directory.list("*.USRPRF");
      if (profileNames != null)
        for (int i = 0; i < profileNames.length; i++)
          System.out.println(profileNames[i]);
      else
        System.out.println("No user profiles.");

      // Generate a list of all directories whose name starts with Q.
      directory = new IFSFile(system, "/");
      String[] qSubDirs = directory.list(new DirectoryFilter(), "Q*");
      if (qSubDirs != null)
        for (int i = 0; i < qSubDirs.length; i++)
          System.out.println(qSubDirs[i]);
      else
        System.out.println("No Q sub-directories.");
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
  }
}


class DirectoryFilter implements IFSFileFilter
{
  public boolean accept(IFSFile file)
  {
    boolean isDirectory = false;
    try
    {
      isDirectory = file.isDirectory();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }

    return isDirectory;
  }
}

      



