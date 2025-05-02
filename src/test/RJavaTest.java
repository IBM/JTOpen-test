///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  RJavaTest.java
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.Job;
import com.ibm.as400.resource.ResourceEvent;
import com.ibm.as400.resource.ResourceListener;

import test.RJava.RJavaProgramBasicTestcase;
import test.RJava.RJavaProgramBeanInfoTestcase;
import test.RJava.RJavaProgramBufferedResourceTestcase;
import test.RJava.RJavaProgramGenericAttributeTestcase;
import test.RJava.RJavaProgramSpecificAttributeTestcase;



/**
Test driver for the RJavaProgram classes.
**/
@SuppressWarnings("deprecation")
public class RJavaTest
extends TestDriver
{


    public static String classFilePathNoJvapgm_;
    public static String classFilePath_;
    public static String jarFilePath_;

    // Private data.
    private static  String directoryName_          = "/home/RJavaTest";
    private static final String sourceFileName_         = "HelloWorld.java";
    private static final String sourceFileNameNoJvaPgm_ = "HelloWorld2.java";
    private static final String serializeFilename_      = "RJavaTest.ser";




/**
Run the test as an application.  This should be called
from the test driver's main().

@param  args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public static void main(String args[])
        throws Exception
    {
        runApplication(new RJavaTest(args));
    }



/**
Constructs an object for applets.
 @exception  Exception  If an exception occurs.
**/
    public RJavaTest()
        throws Exception
    {
        super();
    }



/**
Constructs an object for testing applications.

@param      args        The command line arguments.
 @exception  Exception  If an exception occurs.
**/
    public RJavaTest(String[] args)
        throws Exception
    {
        super(args);
    }



/**
Creates the testcases.
**/
    public void createTestcases ()
    {
    	if(TestDriverStatic.pause_)
    	{ 
      		  	try 
      		  	{						
      		  		systemObject_.connectService(AS400.COMMAND);
      			}
      	     	catch (AS400SecurityException e) 
      	     	{
      	     		// TODO Auto-generated catch block
      				e.printStackTrace();
      			} 
      	     	catch (IOException e) 
      	     	{
      				// TODO Auto-generated catch block
      	     	    e.printStackTrace();
      			}
      				 	 	   
      	     	try
      	     	{
      	     	    Job[] jobs = systemObject_.getJobs(AS400.COMMAND);
      	     	    System.out.println("Host Server job(s): ");

      	     	    	for(int i = 0 ; i< jobs.length; i++)
      	     	    	{   	    	
      	     	    		System.out.println(jobs[i]);
      	     	    	}    	    
      	     	 }
      	     	 catch(Exception exc){}
      	     	    
      	     	 try 
      	     	 {
      	     	    	System.out.println ("Toolbox is paused. Press ENTER to continue.");
      	     	    	System.in.read ();
      	     	 } 
      	     	 catch (Exception exc) {};   	   
    	}  
    	
        // boolean allTestcases = (namesAndVars_.size() == 0);

        // Test the RJavaProgram class.
        addTestcase(new RJavaProgramBasicTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RJavaProgramBeanInfoTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RJavaProgramBufferedResourceTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RJavaProgramGenericAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
        addTestcase(new RJavaProgramSpecificAttributeTestcase(systemObject_, namesAndVars_, runMode_, 
                                          fileOutputStream_, password_, pwrSys_));
    }


/**
Serializes and deserializes an object.

@param  object  The object.
@return         The deserialized object.
 @exception  Exception  If an exception occurs.
**/
    public static Object serialize (Object object)
        throws Exception
    {
	    // Serialize.
	    ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFilename_));
	    out.writeObject (object);
	    out.flush ();
	    out.close(); 
        // Deserialize.
        Object object2 = null;
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
            in.close(); 
        }
   	    finally {
       		File f = new File (serializeFilename_);
        	f.delete();
   	    }

   	    return object2;
   	}



    /**
     @exception  Exception  If an exception occurs.
     **/
    public void setup()
    throws Exception
    {


	directoryName_          = "/home/RJavaTest."+systemObject_.getUserId(); 
        // Create the directory.
        System.out.println("Creating directory " + directoryName_ + ".");
        IFSFile directory = new IFSFile(systemObject_, directoryName_);
        directory.mkdirs();

        // Determine the class name.
        IFSFile sourceFileNoJvaPgm = new IFSFile(systemObject_, directory, sourceFileNameNoJvaPgm_);
        int dot = sourceFileNameNoJvaPgm_.indexOf('.');
        String classNameNoJvaPgm = sourceFileNameNoJvaPgm_.substring(0, dot);
        String classFileNameNoJvaPgm = classNameNoJvaPgm + ".class";
        IFSFile classFileNoJvaPgm = new IFSFile(systemObject_, directory, classFileNameNoJvaPgm);

        // Create the source file.
        System.out.println("Generating source file " + sourceFileNameNoJvaPgm_ + ".");
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new IFSFileOutputStream(systemObject_, sourceFileNoJvaPgm.getPath()))));
        out.println("public class " + classNameNoJvaPgm);
        out.println("{");
        out.println("public static void main(String[] args)");
        out.println("{");
        out.println("System.out.println(\"Hello World\");");
        out.println("}");
        out.println("}");
        out.close();

        // Compile it.
        System.out.println("Compiling " + sourceFileNameNoJvaPgm_ + ".");
        CommandCall cc = new CommandCall(systemObject_);
        boolean success = cc.run("QSYS/QSH CMD('javac " + sourceFileNoJvaPgm.getPath() + "') "); 
        AS400Message[] messageList = cc.getMessageList();
        for(int i = 0; i < messageList.length; ++i)
            System.out.println(messageList[i].getText());
        if (! success)
            System.out.println("Class file " + classFileNameNoJvaPgm + " not created.  Testcases are likely to fail.");
        else
            System.out.println("Class file " + classFileNameNoJvaPgm + " created.");
        classFilePathNoJvapgm_ = classFileNoJvaPgm.getPath();

        // Determine the class name.
        IFSFile sourceFile = new IFSFile(systemObject_, directory, sourceFileName_);
        dot = sourceFileName_.indexOf('.');
        String className = sourceFileName_.substring(0, dot);
        String classFileName = className + ".class";
        IFSFile classFile = new IFSFile(systemObject_, directory, classFileName);

        // Create the source file.
        System.out.println("Generating source file " + sourceFileName_ + ".");
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new IFSFileOutputStream(systemObject_, sourceFile.getPath()))));
        out.println("public class " + className);
        out.println("{");
        out.println("public static void main(String[] args)");
        out.println("{");
        out.println("System.out.println(\"Hello World\");");
        out.println("}");
        out.println("}");
        out.close();

        // Compile it.
        System.out.println("Compiling " + sourceFileName_ + ".");
        cc = new CommandCall(systemObject_);
        success = cc.run("QSYS/QSH CMD('javac " + sourceFile.getPath() + "') "); 
        messageList = cc.getMessageList();
        for(int i = 0; i < messageList.length; ++i)
            System.out.println(messageList[i].getText());
        if (! success)
            System.out.println("Class file " + classFileName + " not created.  Testcases are likely to fail.");
        else
            System.out.println("Class file " + classFileName + " created.");
        classFilePath_ = classFile.getPath();

        // Jar it.
        String jarFileName = className + ".jar";
        IFSFile jarFile = new IFSFile(systemObject_, directory, jarFileName);
        jarFilePath_ = jarFile.getPath();
        success = cc.run("QSYS/QSH CMD('jar cvf " + jarFilePath_ + " " + classFilePath_ + "') "); 
        messageList = cc.getMessageList();
        for(int i = 0; i < messageList.length; ++i)
            System.out.println(messageList[i].getText());
        if (! success)
            System.out.println("Jar file " + jarFileName + " not created.  Testcases are likely to fail.");
        else
            System.out.println("Jar file " + jarFileName + " created.");


    }





/**
Sample property change listener.
**/
    public static class PropertyChangeListener_
    implements PropertyChangeListener
    {
        public int                  eventCount_ = 0;
        public PropertyChangeEvent  event_ = null;

        public void propertyChange(PropertyChangeEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }



/**
Sample resource listener.
**/
    public static class ResourceListener_
    implements ResourceListener
    {
        public int                  eventCount_ = 0;
        public ResourceEvent        event_ = null;

        public void attributeChangesCanceled(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeChangesCommitted(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeValuesRefreshed(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void attributeValueChanged(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void resourceCreated(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }

        public void resourceDeleted(ResourceEvent event)
        {
            ++eventCount_;
            event_ = event;
        }
    }



}


