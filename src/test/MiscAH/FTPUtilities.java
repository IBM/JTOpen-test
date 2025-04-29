///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPUtilities.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Enumeration;
import java.util.Vector;

import test.FTPTest;



/**
Test driver for the JarMaker class.
**/

public class FTPUtilities
{
    static final String FILE_SEPARATOR_STRING = System.getProperty ("file.separator");
    static final char FILE_SEPARATOR = FILE_SEPARATOR_STRING.charAt(0);
    static final String PATH_SEPARATOR = System.getProperty ("path.separator");

    private static final int DETAILED = 1;
    // private static final int NAME_ONLY = 0;

    private static final int ONE     =  1;
    private static final int TWO     =  2;
    private static final int FOUR    =  4;
    private static final int EIGHT   =  8;
    private static final int SIXTEEN = 16;


    /**
     *  Copies a file onto another file (replacing it if it exists).
    **/
    static boolean compareFile(String sourceFile, String targetFile)
                               throws Exception, IOException
    {
      if (sourceFile == null) throw new NullPointerException ("sourceFile");
      if (targetFile == null) throw new NullPointerException ("targetFile");

      BufferedInputStream source = null;
      BufferedInputStream target = null;

      int BUFF_SIZE = 2048;

      byte[] sourceBuffer = new byte[BUFF_SIZE];
      byte[] targetBuffer = new byte[BUFF_SIZE];

      boolean result = true;

      try
      {
         source = new BufferedInputStream (new FileInputStream (sourceFile), BUFF_SIZE);
         target = new BufferedInputStream (new FileInputStream (targetFile), BUFF_SIZE);

         boolean done = false;

         while (!done)
         {
            int sourceBytesRead = source.read (sourceBuffer);
            int targetBytesRead = target.read (targetBuffer);

            if ((sourceBytesRead == -1) || (targetBytesRead == -1))
            {
               done = true;
            }
            else if (sourceBytesRead != targetBytesRead)
            {
               done   = true;
               result = false;
            }
            else
            {
               for (int i=1; i<BUFF_SIZE; i++)
               {
                  if (sourceBuffer[i] != targetBuffer[i])
                  {
                     done   = true;
                     result = false;
                  }
               }
            }
         }
         source.close();
         target.close();
      }
      catch (Exception e)
      {
         System.out.println ("Exception in compareFile: " + e.getMessage ());
         e.printStackTrace ();
      }

      return result;
    }



/**
Create a file.
**/
  static boolean createFile (File file)
  {
    boolean succeeded = true;
    try
    {
      if (file.exists ())
        file.delete();
      RandomAccessFile raf = new RandomAccessFile (file, "rw");
      raf.close();
    }
    catch (Exception e) {
      succeeded = false;
      System.out.println (e.toString());
      e.printStackTrace ();
    }
    return succeeded;
  }



/**
Create a file.
**/
  static boolean createFile (File file, String data)
  {
    boolean succeeded = true;
    try
    {
      if (file.exists ())
        file.delete();
      RandomAccessFile raf = new RandomAccessFile (file, "rw");
      if (data != null) raf.writeUTF(data);
      raf.close();
    }
    catch (Exception e) {
      succeeded = false;
      System.out.println (e.toString());
      e.printStackTrace ();
    }
    return succeeded;
  }


  static boolean createFile (String fileName, String data)
  {
      return createFile (new File (fileName), data);
  }



/**
Delete a file.
**/
    static boolean deleteFile (File file)
    {
      if (file.exists ())
      {
        try { file.delete (); }
        catch (Exception e) {
          System.out.println ("Exception in deleteFile: " +
                              e.getMessage ());
          e.printStackTrace ();
        }
        if (file.exists ())
          System.out.println ("FTPUtilities.deleteFile(): Failed to delete file " +
                              file.getAbsolutePath ());
      }
      return (!file.exists ());
    }


    static boolean deleteFile (String fileName)
    {
      return deleteFile (new File (fileName));
    }





/**
Delete a directory and all its subdirectories.
**/
    static boolean deleteDirectory ( File dir )
    {
      try
      {
         if (dir.exists () && !dir.isDirectory ())
          System.out.println ("Not a directory: " + dir.getAbsolutePath ());
      }
      catch (Exception e)
      {
          System.out.println ("Exception in deleteDirectory: " +
                              e.getMessage());
          e.printStackTrace ();
      }
      deleteFileOrDir (dir);
      return (!dir.exists ());
    }


    public static boolean deleteDirectory ( String dirName )
    {
      return deleteDirectory (new File (dirName));
    }





/**
Delete a directory or file, and all its subdirectories (if it's a directory).
**/
    static void deleteFileOrDir ( File fileOrDir )
    {
      boolean isDirectory = false;
      boolean isFile = false;
      try {
        if (FTPTest.DEBUG) System.out.print("FTPUtilities.deleteFileOrDir("+fileOrDir.getPath()+"): ");
        if (fileOrDir.isDirectory ()) {
          isDirectory = true;
          if (FTPTest.DEBUG) System.out.println("It's a directory.");
        }
        else if (fileOrDir.isFile ()) {
          isFile = true;
          if (FTPTest.DEBUG) System.out.println("It's a file.");
        }
        else if (!fileOrDir.exists ()) {
          isFile = true;
          if (FTPTest.DEBUG) System.out.println("It doesn't exist.");
        }
        else {
          if (FTPTest.DEBUG) System.out.println("It's neither a file nor a directory.");
        }
      }
      catch (Exception e)
      {
          System.out.println ("Exception in deleteFileOrDir: " +
                              e.getMessage());
          e.printStackTrace ();
      }

      if (isFile) deleteFile (fileOrDir );
      else if (isDirectory)
      {
        String[] entriesInDir = null;

        try { entriesInDir = fileOrDir.list (); }
        catch (Exception e)
        {
            System.out.println ("Exception in deleteFileOrDir(2): " +
                                e.getMessage());
            e.printStackTrace ();
        }

        if (entriesInDir != null && entriesInDir.length != 0)
        {
          for (int i=0; i<entriesInDir.length; i++) {
            String name = entriesInDir[i];
            File thisFile = new File (fileOrDir, name);
            deleteFileOrDir (thisFile);
          }

          // Re-list (and delete) the contents of the directory, in case any temporary ".*" files got created and left behind.  (This has been observed on AFS.)
          try { entriesInDir = fileOrDir.list(); }
          catch (Exception e)
          {
            System.out.println ("Exception in deleteFileOrDir(3): " +
                                e.getMessage());
            e.printStackTrace ();
          }
          if (entriesInDir != null) {
            for (int i=0; i<entriesInDir.length; i++) {
              String name = entriesInDir[i];
              File thisFile = new File (fileOrDir, name);
              deleteFileOrDir (thisFile);
            }
          }
        }

        boolean worked = false;
        try { worked = fileOrDir.delete (); } // Now delete the empty directory
        catch (Exception e)
        {
            System.out.println ("Exception in deleteFileOrDir(4): " +
                                e.getMessage());
            e.printStackTrace ();
        }
        if (!worked) {
          System.out.println("Failed to delete directory " + fileOrDir);
        }
      }
    }






    /**
     *   Lists the files in (and under) the specified directory.
     *   In the returned list, files are represented as jar entrynames.
     **/
    public static Vector<String> listFiles (File file)
    {
       if (file == null)
       {
          System.out.println ("FTPUtilities: Argument is null.");
          return new Vector<String> ();
       }

       File parentDir = new File (file.getParent ());

       return listFiles (file, parentDir.getAbsolutePath ());
    }



    static Vector<String> listFiles (Vector<?> fileOrDirList, String basePath)
    {
       Vector<String> result = new Vector<String> ();
       Enumeration<?> e = fileOrDirList.elements ();

       while (e.hasMoreElements ())
       {
          File fileOrDir = (File)e.nextElement ();
          Vector<String> subList = listFiles (fileOrDir, basePath);
          copyList (subList, result);  // merge subList into result
       }

       return result;
    }




  private static Vector<String> listFiles (File fileOrDir, String basePath)
  {
    Vector<String> result = new Vector<String> ();
    try
    {
      ///File thisDir = new File (dir, subDir);
      if (fileOrDir.isDirectory ())
      {
        File thisDir = fileOrDir;  // reduce naming confusion
        String[] contents = thisDir.list ();  // Get contents of this directory
        for (int i=0; i<contents.length; i++)
        {
          String fileName = contents[i];
          File file = new File (thisDir, fileName);
          ///if (ifs.isAbsolute()) prtln("  >>(absolute)");
          ///if (ifs.isDirectory()) prtln("  >>(directory)");
          ///if (ifs.isFile()) prtln("  >>(file)");

          Vector<String> subList = listFiles (file, basePath);
          copyList (subList, result);  // merge subList into result
        }
      }
      ///else
        result.addElement (normalize (fileOrDir, basePath));
   }
    catch (Exception e)
    {
      System.out.println ("listFiles(File,String):" + e.getMessage());
      e.printStackTrace();
    }
    return result;
  }


   private static String normalize (File fileOrDir, String basePath)
   {
      String filePath = fileOrDir.getAbsolutePath ();
      String outString = null;

      if (!filePath.startsWith (basePath))
         System.out.println ("filePath doesn't start with basePath");
      else
      {
         String relPath = filePath.substring (basePath.length ());
         outString = relPath.replace (FILE_SEPARATOR, '/');
      }

      if (fileOrDir.isDirectory () && !outString.endsWith ("/"))
         outString = outString + "/";

      while (outString.length () > 1 && outString.startsWith ("/"))
         outString = outString.substring (1);

      if (FTPTest.DEBUG) System.out.println ("Normalized path: " + outString);

      return outString;
   }


   public static void copyList (Vector<String> newKids, Vector<String> theBlock)
   {
      Enumeration<String> e = newKids.elements ();
      while (e.hasMoreElements ())
         theBlock.addElement (e.nextElement ());
   }


   public static void copy(InputStream source, String target)
                      throws IOException
   {
      FileOutputStream f = new FileOutputStream(target);
      byte[] buffer = new byte[5 * 1024];
      int length = source.read(buffer);
      while (length > 0)
      {
         f.write(buffer, 0, length);
         length = source.read(buffer);
      }
      f.flush();
      f.close();
      source.close();
   }


   public static void copy(String source, OutputStream target)
                      throws IOException
   {
      FileInputStream i = new FileInputStream(source);
      byte[] buffer = new byte[2 * 1024];
      int length = i.read(buffer);
      while (length > 0)
      {
         target.write(buffer, 0, length);
         length = i.read(buffer);
      }
      i.close();
      target.flush();
      target.close();
   }



    // --------------------------------------------------------------------


    public static boolean checkTestDir(String[] listOfStrings, int level, int TOTAL)
    {
       // boolean result = true;
       int mask = 0;
 
       if (FTPTest.DEBUG) System.out.println("    Entering verify list");

       if (listOfStrings == null)
       {
          System.out.println("    list of strings is null");
          return false;
       }

       int numberOfItems = listOfStrings.length;

       if (numberOfItems < 5)
       {
          System.out.println("    list does not contain 5 items, number = " + numberOfItems);
          return false;
       }

       for (int i = 0; i < numberOfItems; i++)
       {
          String s = listOfStrings[i];
          if (FTPTest.DEBUG) System.out.println("    " + s);

          if (s.indexOf("a.a") >= 0)
          {
             if (level == DETAILED)
             {
                if (s.indexOf("36") >= 0)
                   mask = mask + ONE;
             }
             else
                mask = mask + ONE;
          }
          else if (s.indexOf("PureJava.html") >= 0)
          {
             if (level == DETAILED)
             {
                if (s.indexOf("484342") >= 0)
                   mask = mask + TWO;
             }
             else
                mask = mask + TWO;
          }
          else if (s.indexOf("javasp.savf") >= 0)
          {
             if (level == DETAILED)
             {
                if (s.indexOf("283008") >= 0)
                   mask = mask + FOUR;
             }
             else
                mask = mask + FOUR;
          }
          else if (s.indexOf("jt400.jar") >= 0)
          {
             if (level == DETAILED)
             {
                if (s.indexOf("2357538") >= 0)
                   mask = mask + EIGHT;
             }
             else
                mask = mask + EIGHT;
          }
          else if (s.indexOf("rootDir") >= 0)
          {
             mask = mask + SIXTEEN;
          }
       }

       if (FTPTest.DEBUG) System.out.println("    Mask is: " + mask);

       if (mask == TOTAL)
          return true;
       else
          return false;

    }

    // --------------------------------------------------------------------


    public static boolean checkForFile(String[] listOfStrings, String file)
    {
       boolean result = true;
 
       if (listOfStrings == null)
       {
          System.out.println("    list of strings is null");
          return false;
       }
       int numberOfItems = listOfStrings.length;

       for (int i = 0; i < numberOfItems; i++)
       {
          String s = listOfStrings[i];
          if (FTPTest.DEBUG) System.out.println("    " + s);

          if (s.indexOf(file) >= 0)
          {
             result = true;
          }
       }

       return result;

    }







}




