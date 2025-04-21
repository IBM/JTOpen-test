///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  IFSListFilesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.IFS;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.Enumeration;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Date;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileFilter;
import com.ibm.as400.access.IFSFileOutputStream;

import test.IFSTests;
import test.JTOpenTestEnvironment;
import test.TestDriverStatic;

/**
Test IFSFile.listFiles().
**/
public class IFSListFilesTestcase extends IFSGenericTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSListFilesTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  // Can use "/./QSYS.LIB" or "/." to test.
  private static String smallIfsDirName_ = "/QSYS.LIB/QSYSINC.LIB"; 
  private boolean brief_;
  private static String testDir="/Toolbox_mydir1";
/**
Constructor.
**/
  public IFSListFilesTestcase (AS400 systemObject,
      String userid, 
      String password,
                   Hashtable<String, Vector<String>> namesAndVars,
                   int runMode,
                   FileOutputStream fileOutputStream,
                   AS400    pwrSys)
    {
        super (systemObject, userid, password, "IFSListFilesTestcase",
            namesAndVars, runMode, fileOutputStream,  pwrSys);
        brief_ = TestDriverStatic.brief_;
    }


  /**
   @exception  Exception  If an exception occurs.
   **/
  public void setup()
    throws Exception
  {
    super.setup(); 
    
    ifsDirName_ = "/./QSYS.LIB"; // the . fixes Win95 JVM bug
    ifsPathName_ = ifsDirName_ + "/" + fileName_;

  }

  boolean var13Setup()
  {

    // If the file exists, go ahead and delete it
      try { 
      IFSFile aFile = new IFSFile(systemObject_, testDir+"/myfile");
      aFile.delete();
      } catch (Exception e) {}
      try {
      IFSFile aDir = new IFSFile(systemObject_, testDir);
      aDir.delete();
      } catch (Exception e) {}


    try
    {
      //testcase setup
      boolean code;
      IFSFile aDirectory = new IFSFile(systemObject_, testDir);
      code = aDirectory.mkdir();

      if (code)
      {
        IFSFileOutputStream aFile =
                     new IFSFileOutputStream(systemObject_,testDir+"/myfile");
        byte i = 123;
        aFile.write(i);
        aFile.close();
      }
      else
      {
        // If the object already exists, find out if it is a directory or
        // file, then display a message.
        if (aDirectory.exists())
        {
          if (aDirectory.isDirectory())
              System.out.println("Directory already exists");
          else
              System.out.println("File with this name already exists");
        }
        else
           System.out.println("Create directory failed");
      }
      return code;
    }
    catch (Exception e)
    {
      e.printStackTrace(output_);
      return false;
    }
  }//var13Setup

/**
Ensure that NullPointerException is NOT thrown if argument of
IFSFile.listFiles(IFSFileFilter) is null.
**/
  public void Var001()
  {
    ///setVariation(1);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.listFiles((IFSFileFilter) null);
      succeeded();
    }
    catch(Exception e)
    {
      failed (e, "Unexpected Exception");
    }
  }

/**
Ensure that NullPointerException is thrown by IFSFile.listFiles(String) if
argument is null.
**/
  public void Var002()
  {
    ///setVariation(2);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsDirName_);
      file.listFiles((String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertExceptionIs(e, "NullPointerException", "pattern");
    }
  }

/**
Ensure that NullPointerException is NOT thrown if argument one of
IFSFile.listFiles(IFSFileFilter, String) is null.
**/
  public void Var003()
  {
    ///setVariation(3);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.listFiles((IFSFileFilter) null, "*.exe");
      succeeded();
    }
    catch(Exception e)
    {
      failed (e, "Unexpected Exception");
    }
  }

/**
Ensure that NullPointerException is thrown if argument two of
IFSFile.listFiles(IFSFileFilter, String) is null.
**/
  public void Var004()
  {
    ///setVariation(4);
    try
    {
      IFSFile file = new IFSFile(systemObject_, ifsPathName_);
      file.listFiles(new IFSListFilesFilter83(output_), (String) null);
      failed("Exception didn't occur.");
    }
    catch(Exception e)
    {
      assertCondition(exceptionIs(e, "NullPointerException", "pattern"));
    }
  }


/**
Ensure that IFSFile.listFiles(IFSFileFilter) returns the same files
as IFSFile.list(IFSFileFilter) does.
**/
  public void Var005()
  {

    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSListFilesFilter83 filter = new IFSListFilesFilter83(output_);
      IFSFile[] list1 = file.listFiles(filter);
      String[] list2 = file.list(filter);

      if (!(list1.length > 0)) {
        failed("listFiles(filter).length <= 0");
	return; 
      }

      if (list1.length == list2.length) {
	boolean passed = true; 
        int i = 0;
        StringBuffer sb = new StringBuffer(); 
        while (i < list1.length) {
          if (!(list1[i].getName().equals(list2[i]))) {
            sb.append("\nBAD :listFiles(filter).getName()= "
                + list1[i].getName() + ", list(filter)= " + list2[i]);
            passed = false; 
          } else { 
            sb.append("\nGOOD :listFiles(filter).getName()= "
                + list1[i].getName() + ", list(filter)= " + list2[i]);
            
          }
          i++;
        }
        passed = (i == list1.length) && passed; 
        assertCondition(passed, "i=" + i + " list1.length="
            + list1.length+sb.toString());
      } else
        failed("listFiles(filter).length= " + list1.length
            + ", list(filter).length= " + list2.length);
    } catch (Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
Ensure that listFiles(IFSFileFilter, String) returns the same files
as list(IFSFileFilter, String).
**/
  public void Var006()
  {
    ///setVariation(6);

    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSListFilesFilter83 filter = new IFSListFilesFilter83(output_);
      IFSFile[] list1 = file.listFiles(filter, "*");
      String[] list2 = file.list(filter, "*");

      if (!(list1.length > 0))
          failed("listFiles(filter,\"*\").length <= 0");

      if (list1.length == list2.length)
      {
        int i = 0;
	boolean passed = true; 
        while (i < list1.length)
        {
          if (!(list1[i].getName().equals(list2[i])))
       {
             System.out.println("listFiles(filter,\"*\").getName()= " + list1[i].getName() +
                                ", list(filter,\"*\")= " + list2[i]);
             passed = false; 
          }
          i++;
        }
	passed = passed && (i == list1.length); 
      assertCondition (passed );
      }
      else
         failed("listFiles(filter,\"*\").length= " + list1.length +
                ", list(filter,\"*\").length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

/**
Ensure that IFSFile.listFiles("*") returns the same files as IFSFile.list().
**/
  public void Var007()
  {

    try
    {
      IFSFile dir1 = new IFSFile(systemObject_, smallIfsDirName_);
      IFSFile dir2 = new IFSFile(systemObject_, smallIfsDirName_);
      IFSFile[] list1 = dir1.listFiles("*");
      String[] list2 = dir2.list();

      if (!(list1.length > 0))
          failed("listFiles(\"*\").length <= 0");

      int i = 0;
      if (list1.length == list2.length)
      {
        while (i < list2.length)
        {
          IFSFile file = new IFSFile(systemObject_, smallIfsDirName_, list2[i]);
          if (!(list1[i].getName().equals(file.getName())))
       {
            failed("listFiles(\"*\").getName()= " + list1[i].getName() +
                   ", list().getName()= " + file.getName());
            break;
          }
          i++;
        }
        if (i == list1.length)
        {
           succeeded();
        }
      }
      else
         failed("listFiles(\"*\").length= " + list1.length +
                ", list().length= " + list2.length);
    }
    catch(Exception e)
    {
      failed (e, "Unexpected Exception");
    }
  }

/**
Ensure that listFiles(IFSFileFilter) returns the same files
as list(IFSFileFilter).
**/
  public void Var008()
  {

    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSListFilesFilter83 filter = new IFSListFilesFilter83(output_);
      IFSFile[] list1 = file.listFiles(filter);
      String[] list2 = file.list(filter);

      if (!(list1.length > 0))
          failed("listFiles(filter).length <= 0");

      if (list1.length == list2.length)
      {
        int i = 0;
	boolean passed = true; 
        while (i < list1.length)
        {
          if (!(list1[i].getName().equals(list2[i])))
       {
             System.out.println("listFiles(filter).getName()= " + list1[i].getName() +
                                ", list(filter)= " + list2[i]);
             break;
          }
          i++;
        }
	passed = passed && (i == list1.length); 
      assertCondition (passed); 
      }
      else
         failed("listFiles(filter).length= " + list1.length + ", list(filter).length= "
                + list2.length);
    }
    catch(Exception e)
    {
      failed (e, "Unexpected Exception");
    }
  }

/**
Ensure that listFiles(filter, "*") returns the same files as list(filter, "*").
**/
  public void Var009()
  {

    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSListFilesFilter83 filter = new IFSListFilesFilter83(output_);
      IFSFile[] list1 = file.listFiles(filter, "*");
      String[] list2 = file.list(filter, "*");

      if (!(list1.length > 0))
          failed("listFiles(filter, \"*\").length <= 0");

      if (list1.length == list2.length)
      {
        int i = 0;
	boolean passed = true; 
        while (i < list1.length)
        {
          if (!(list1[i].getName().equals(list2[i])))
       {
             System.out.println("listFiles(filter, \"*\").getName()= " + list1[i].getName() +
                                ", list.(filter, \"*\") " + list2[i]);
             passed =  false; 
          }
          i++;
        }
	passed = passed && (i == list1.length); 
      assertCondition (passed);
      }
      else
         failed("listFiles(filter, \"*\").length= " + list1.length +
                ", list.(filter, \"*\").length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


/**
Ensure that listFiles(filter, "Q*") returns the same files
as list(filter, "Q*").
**/
  public void Var010()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }

    try
    {
      IFSFile file = new IFSFile(systemObject_, "/");
      IFSListFilesFilter83 filter = new IFSListFilesFilter83(output_);
      IFSFile[] list1 = file.listFiles(filter, "Q*");
      String[] list2 = file.list(filter, "Q*");

      if (!(list1.length > 0))
          failed("listFiles(filter, \"Q*\").length <= 0");

      if (list1.length == list2.length)
      {
        int i = 0;
        while (i < list1.length)
        {
          if (!(list1[i].getName().equals(list2[i])))
       {
             System.out.println("listFiles(filter, \"Q*\").getName()= " + list1[i].getName() +
                                ", list(filter, \"Q*\")= " + list2[i]);
             break;
          }
          i++;
        }
      assertCondition (i == list1.length);
      }
      else
         failed("listFiles(filter, \"Q*\").length= " + list1.length +
                ", list(filter, \"Q*\").length= " + list2.length);
    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }


/**
Ensure that IFSFile.listFiles() returns the same files as IFSFile.list().
**/
  public void Var011()
  {
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }

    //
    // ifsDirName_ is /QSYS.LIB
    // This will cause this test to take a long time to run..
    // 12/9/2010.. Changed test to do something smaller -- like QGPL.LIB
    //

    try
    {
      IFSFile dir1 = new IFSFile(systemObject_, smallIfsDirName_);
      IFSFile[] list1 = dir1.listFiles();

      IFSFile dir2 = new IFSFile(systemObject_, smallIfsDirName_);
      String[] list2 = dir2.list();



      int i = 0;
      if (list1.length == list2.length)
      {
        while (i < list1.length)
        {
          IFSFile file = new IFSFile(systemObject_, smallIfsDirName_, list2[i]);
          if (!(list1[i].getName().equals(file.getName())))
          {
             System.out.println("listFiles().getName()= " + list1[i].getName() + " list().getName()= " + file.getName());
             break;
          }
          if (list1[i].getName().indexOf(IFSFile.separatorChar) != -1)
          {
            // There is a delimeter in the name... so, do NOT perform the remaining
            // tests in this variation as IFS code does not handle object
            // names with delimeter's within the name.  Someone likely created
            // objects on this i5 System with LIB commands (e.g. "Net=[]\/".LIB)

            //System.out.println("Path with Separator ="+list1[i].getName());
            i++;
            continue;
          }
	  boolean isDirectory = false;
	  boolean fileIsDirectory = false; 
	  try {
	      isDirectory =  list1[i].isDirectory();
	      fileIsDirectory = file.isDirectory(); 
	  } catch (com.ibm.as400.access.ExtendedIOException eioe) {
	      if (eioe.toString().indexOf("File in use") >= 0) {
		  // Just ignore the locked entry
		  i++;
		  continue; 
	      } else {
		  System.out.println("Warning:  Unexpected exception "+eioe);
		  eioe.printStackTrace(); 
	      } 
	  } 
          if (isDirectory != fileIsDirectory)
          {
             System.out.println("name: " + list1[i].getName());
             System.out.println("path: " + list1[i].getAbsolutePath());
             System.out.println("listFiles().isDirectory()= " + list1[i].isDirectory() + ", list().isDirectory()= " + file.isDirectory());
             break;
          }
          if (list1[i].isFile() != file.isFile())
          {
             System.out.println("name: " + list1[i].getName());
             System.out.println("path: " + list1[i].getAbsolutePath());
             System.out.println("listFiles().isFile()= " + list1[i].isFile() + ", list.File()= " + file.isFile());
             break;
          }
          if ((list1[i].isFile() == list1[i].isDirectory()) && list1[i].isFile())
          {
             System.out.println("listFiles()name: " + list1[i].getName());
             System.out.println("path: " + list1[i].getAbsolutePath());
             System.out.println("listFiles().isFile()= " + list1[i].isFile() + ",  listFiles().isDirectory()= " + list1[i].isDirectory());
             break;
          }
          if (!(list1[i].getPath().equals(file.getPath())))
          {
             System.out.println("name: " + list1[i].getName());
             System.out.println("listFiles.getPath()= " + list1[i].getPath() + ", list.getPath()= " + file.getPath());
             break;
          }
          i++;
        }
        assertCondition (i == list1.length);
      }//end if
      else //do if list lengths are not equal
      {
         failed("IFSFile listFiles.length= " + list1.length + ", IFSFile list.length= " + list2.length);
         Vector<String> listFilesNames = new Vector<String>(list1.length);
         for (int j = 0; j < list1.length; j++)
         {
             listFilesNames.addElement(list1[j].getName());
         }
         Vector<String> listNames = new Vector<String>(list2.length);
         for (int k = 0; k < list2.length; k++)
         {
             listNames.addElement(list2[k]);
         }
         boolean print = true;
         for (Enumeration<String> e = listFilesNames.elements(); e.hasMoreElements(); )
         {
             String name = (String)e.nextElement();
             if (!(listNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles() but not list():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration<String> f = listNames.elements(); f.hasMoreElements(); )
         {
             String name2 = (String)f.nextElement();
             if (!(listFilesNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in list() but not listFiles():");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else
      return;

    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

  public String[] removeUserProfiles (String[] in) {
      Vector<String> v = new Vector<String>();
      for (int i = 0; i < in.length; i++) {
	  if (in[i].indexOf(".USRPRF") < 0) {
	      v.addElement(in[i]); 
	  } 
      }
      in = new String[v.size()];
      for (int i = 0; i < in.length; i++) {
	  in[i] = (String) v.elementAt(i); 
      } 
      return in; 
  }
  
  
  public IFSFile[] removeUserProfiles (IFSFile[] in) {
    Vector<IFSFile> v = new Vector<IFSFile>();
    for (int i = 0; i < in.length; i++) {
        if (in[i].getName().indexOf(".USRPRF") < 0) {
            v.addElement(in[i]); 
        } 
    }
    in = new IFSFile[v.size()];
    for (int i = 0; i < in.length; i++) {
        in[i] = (IFSFile) v.elementAt(i); 
    } 
    return in; 
} 

/**
Ensure that IFSFile.listFiles() returns the same files as File.list().
**/
  public void Var012()
  {
    ///setVariation(12);
    if (brief_)
    {
      notApplicable("Skipping long-running variation.");
      return;
    }

    if (UNIX_ || JTOpenTestEnvironment.isWindows || IFSTests.IsRunningOnOS400)
    {
      notApplicable("On Sun, AIX, Linux, Windows, and OS/400," +
                    " java.io.File.list() reports many more files than IFSFile.listFiles().");
      return;
    }

    if (false /*isApplet_*/)
    {
      notApplicable();
      return;
    }

    // boolean success = true;
    try
    {
      IFSFile dir1 = new IFSFile(systemObject_, ifsDirName_);
      IFSFile[] list1 = dir1.listFiles();

      // String pcName = convertToPCName(ifsDirName_); 
      // File dir2 = new File(pcName);
      // String[] list2 = dir2.list();
      String[] list2 = listDirectory(ifsDirName_);


      list1 = removeUserProfiles(list1);
      list2 = removeUserProfiles(list2); 

      int i = 0;
      boolean passed = true; 
      if (list1.length == list2.length)
      {

	// To make the testcase go faster only check the 100 first entries
        // Checking 21445 entries took about 240260 ms.
	//
	int checkCount = list1.length;
	if (checkCount > 100) checkCount = 100; 
        while (i < checkCount)
        {
          IFSFile file = new IFSFile(systemObject_, ifsDirName_, list2[i]);
          if (!(list1[i].getName().equals(file.getName())))
       {
             System.out.println("Directory="+ifsDirName_+" listFiles().getName()= " + list1[i].getName() + " File.getName()= " + file.getName());
             passed=false; 
          }
	  try { 
	      if (list1[i].isDirectory() != file.isDirectory())
	      {
		  String name = list1[i].getName();
		  
		  // If the name has a \ then the File.isDirectory will not return the correct answer
		  if (name.indexOf("\\") >= 0) {
		      // Ignore this case 
		  } else { 

		      System.out.println("Directory="+ifsDirName_+" name: " + name);
		      System.out.println("listFiles().isDirectory()= " + list1[i].isDirectory() + ", File.isDirectory()= " + file.isDirectory());
		      passed = false;
		  }
	      }

	      if (list1[i].isFile() != file.isFile())
	      {
		  System.out.println("name: " + list1[i].getName());
		  System.out.println("listFiles().isFile()= " + list1[i].isFile() + ", File.isFile()= " + file.isFile());
		  passed = false; 
	      }
	      if ((list1[i].isFile() == list1[i].isDirectory()) && list1[i].isFile())
	      {
		  System.out.println("listFiles() name: " + list1[i].getName());
		  System.out.println("listFiles().isFile()= " + list1[i].isFile() + ",  listFiles.isDirectory()= " + list1[i].isDirectory());
		  passed = false; 
	      }
	      if (!(list1[i].getPath().equals(file.getPath())))
	      {
		  System.out.println("name: " + list1[i].getName());
		  System.out.println("listFiles().getPath()= " + list1[i].getPath() + ", File.getPath()= " + file.getPath());
		  passed = false; 
	      }

	  } catch (Exception e) {
	      String message = e.toString();
	      if (message.indexOf("File in use") >= 0) {
                 // ignore file in use errors 
	      } else {
		  System.err.println("Exception------------"); 
		  e.printStackTrace();
		  System.err.println("------------"); 
		  passed = false; 
	      }
	  } 

          i++;
        }//endwhile
        assertCondition (passed);
      }//endif
      else //do if list lengths are not equal
      {
         failed("IFSFile listFiles().length= " + list1.length + ", File list.length= " + list2.length);
         Vector<String> listFilesNames = new Vector<String>(list1.length);
         for (int j = 0; j < list1.length; j++)
         {
             listFilesNames.addElement(list1[j].getName());
         }
         Vector<String> listNames = new Vector<String>(list2.length);
         for (int k = 0; k < list2.length; k++)
         {
             listNames.addElement(list2[k]);
         }
         boolean print = true;
         for (Enumeration<String> e = listFilesNames.elements(); e.hasMoreElements(); )
         {
             String name = (String)e.nextElement();
             if (!(listNames.contains(name)))
             {
                 if (print)
                 {
                    System.out.println("Names in listFiles() but not list():");
                    print = false;
                 }
                 System.out.println(name);
             }
         }
         print = true;
         for (Enumeration<String> f = listNames.elements(); f.hasMoreElements(); )
         {
             String name2 = (String)f.nextElement();
             if (!(listFilesNames.contains(name2)))
             {
                 if (print)
                 {
                    System.out.println("Names in list() but not listFiles():");
                    print = false;
                 }
                 System.out.println(name2);
             }
         }
      }//end else
    }//end try
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }//endvar12


/**
Create a file and set its read only property to the opposite of its current value, then call
listFiles().  Change should be reflected.
**/
  public void Var013()
  {
    ///setVariation(13); 
    if (false /*isApplet_*/)
    {
      notApplicable();
      return;
    }

    try
    {
      var13Setup();
      IFSFile file = new IFSFile(systemObject_, testDir);
      IFSFile[] list1 = file.listFiles();

      int num = 0;
      boolean initialValue = list1[num].isReadOnly();
      list1[num].setReadOnly(!initialValue);
      list1 = file.listFiles();

      boolean passed =  list1[num].isReadOnly() == !initialValue;
      String message =  " list1[0]="+list1[0]+
		      " list1[0].isReadOnly()="+list1[0].isReadOnly()+
		      " expected "+ (!initialValue); 
      // 
      //cleanup
      if (passed) { 
	  list1[num].setReadOnly(initialValue);
	  IFSFile aFile = new IFSFile(systemObject_, testDir+"/myfile");
	  aFile.delete();
	  IFSFile aDir = new IFSFile(systemObject_,  testDir);
	  aDir.delete();
      }

      assertCondition(passed,
		      message);

    }
    catch(Exception e)
    {
      failed (e, "Unexpected Exception");
    }
  }



/**
Ensure listFiles caches creation and access date/times
@D1 new variation
**/
  public void Var014()
  {
    ///setVariation(14);

    try
    {
      IFSFileOutputStream os = new IFSFileOutputStream (systemObject_, "/JavaTest.aaa");
      os.write(0);
      os.close();

      IFSFile   file = new IFSFile(systemObject_, "/");
      IFSFile[] list = file.listFiles("*.aaa");

      if (list.length == 0)
         failed("nothing in the list");
      else
      {
         Date d1 = new Date(list[0].created());
         Date d2 = new Date(list[0].lastAccessed());

         IFSFile dltme = new IFSFile(systemObject_, "/JavaTest.aaa");
         dltme.delete();

         Thread.sleep(10 * 1000);

         IFSFileOutputStream os2 = new IFSFileOutputStream (systemObject_, "/JavaTest.aaa");
         os2.write(0);
         os2.write(0);
         os2.write(0);
         os2.close();

         Date d1a = new Date(list[0].created());
         Date d2a = new Date(list[0].lastAccessed());

         if  (d1.equals(d1a) && d2.equals(d2a))
         {
            IFSFile file2 = new IFSFile(systemObject_, "/");
            IFSFile[] list2 = file2.listFiles("*.aaa");
            Date d1b = new Date(list2[0].created());
            Date d2b = new Date(list2[0].lastAccessed());

            if  (d1.equals(d1b) && d2.equals(d2b))
            {
               failed("in new list dates do match");
               System.out.println("   " + d1  + "  " + d1.getTime());
               System.out.println("   " + d1b + "  " + d1b.getTime());
               System.out.println("   " + d2  + "  " + d2.getTime());
               System.out.println("   " + d2b + "  " + d2b.getTime());
            }
            else
               succeeded();
         }
         else
         {
            failed("dates do not match");
            System.out.println("   " + d1  + "  " + d1.getTime());
            System.out.println("   " + d1a + "  " + d1a.getTime());
            System.out.println("   " + d2  + "  " + d2.getTime());
            System.out.println("   " + d2a + "  " + d2a.getTime());
         }
      }


    }
    catch(Exception e)
    {
      failed(e, "Unexpected Exception");
    }
  }

}


class ListFilesFilter83
{
  protected boolean accept(String fileName)
  {
    // Accept names that follow the old DOS 8.3 file name structure.
    // 1-8 characters, [followed by a period, followed by a 1-3 character
    // suffix].  Break the file name into name and extension.  Only one
    // period is allowed.
    String extension = "";
    int index = fileName.indexOf('.');
    if (index != -1)
      if (index + 1 < fileName.length())
        if (fileName.indexOf('.', index + 1) != -1)
          return false; // Too many periods
        else
          extension = fileName.substring(index + 1, fileName.length());
      else
        return false; // Period with no extension.
    else
      index = fileName.length(); // No extension
    String name = fileName.substring(0, index);

    return (name.length() <= 8) && (extension.length() <= 3);
  }
}

class IFSListFilesFilter83 extends ListFilesFilter83 implements IFSFileFilter
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "IFSListFilesTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.IFSTests.main(newArgs); 
   }
  PrintWriter output_;

  public IFSListFilesFilter83(PrintWriter output)
  {
    output_ = output;
  }

  public boolean accept(IFSFile file)
  {
    return accept(file.getName());
  }
}
