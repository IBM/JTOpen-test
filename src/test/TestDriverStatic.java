///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestDriverStatic.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Vector;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.Copyright;

/**
 TestDriverStatic contains the static methods currently in TestDriver
 **/
public class TestDriverStatic  
{

    /**
   *
   */
  static final long serialVersionUID = 1L;

  static TestDriverTimeoutThread timeoutThread = null;

  static Vector staticTestcaseResults = new Vector();
    protected static String proxy_;
    static {
      String propVal = System.getProperty("com.ibm.as400.access.AS400.proxyServer");
      if (propVal != null && propVal.length() != 0) {
        proxy_ = propVal;
      }
      else proxy_ = "";
    }
    protected static AS400 pwrSysStatic_ = null;
    static String SERIAL_FILE_NAME = "as400.ser";
     static boolean onAS400_ = false;
     static SecurityManager defaultSecurityManager_; 
    static {
      try { 
      if (JTOpenTestEnvironment.isOS400) onAS400_ = true; // make sure this flag gets set
      
      // Setting a security manager is deprecated.  Remove. 
      // defaultSecurityManager_ = System.getSecurityManager(); 
      // SecurityManager sm = new TestDriverSecurityManager();
      // Do not print this out.  Causes some comparision testcases to fail
      // System.out.println("TestDriverStatic: Setting security manager to "+sm); 
      // System.setSecurityManager(sm);
      
      } catch (Throwable e) { 
        System.err.println("TestDriverStatic: Exception in static initializer"); 
        e.printStackTrace(System.err); 
      }
    }
    public static boolean brief_ = false;
    public static int duration_ = 0;  // number of seconds to run
    public static boolean pause_ = false;
    public  static String implementationVersion_ = null;
    public  static String specificationVersion_ = null;
    protected static boolean servlet_ = false;
    static boolean systemExitDisabled_ = false;

    static SimpleDateFormat timeStampFormatter_ = new SimpleDateFormat( "yyyy-MM-dd HH:mm z" );

    static void init() {
      // Make sure the static constructor above is called to load the 
      // security manager 
    }
    
    /**
     Return the "implementation version" for the Toolbox 'access' package.
     For example:  "JTOpen 6.2".
     **/
    static String getToolboxImplementationVersion()
    {
      if (implementationVersion_ == null) {
        Package toolboxPkg = Package.getPackage("com.ibm.as400.access");
        if (toolboxPkg != null) {
          implementationVersion_ = toolboxPkg.getImplementationVersion();
        }
      }
      return (implementationVersion_ == null ? "" : implementationVersion_);
    }

    /**
     Return the "specification version" for the Toolbox 'access' package.
     For example:  "6.1.0.4" would indicate V6R1M0, PTF 4.
     **/
    static String getToolboxSpecificationVersion()
    {
      if (specificationVersion_ == null) {
        Package toolboxPkg = Package.getPackage("com.ibm.as400.access");
        if (toolboxPkg != null) {
          specificationVersion_ = toolboxPkg.getSpecificationVersion();
        }
      }
      
      if (specificationVersion_ == null) {
        try { 
          File loadSource = TestDriver.getLoadSource("com.ibm.as400.access.AS400JDBCDriver"); 
          if (loadSource.isDirectory()) { 
            // This means we did not load from a jar 
            // Manufacture a version from the copyright file. 
            String jtopenName = Copyright.JTOpenName; 
            int spaceIndex = jtopenName.indexOf(" "); 
            int dotIndex = jtopenName.indexOf("."); 
            if (spaceIndex >= 0 && dotIndex >= 0) { 
				String version = jtopenName.substring(spaceIndex + 1, dotIndex);
				String version2 = jtopenName.substring(dotIndex + 1);
				int intVersion = Integer.parseInt(version);

				switch (intVersion) {
				case 9:
					version = "7.3.0";
					break;
				case 10:
					version = "7.4.0";
				}
				return version + "." + version2;
			} else {
				return jtopenName;
			}
          } else {
             return "NOT AVAILABLE BUT LOAD SOURCE IS NOT DIRECTORY " ;
          }
        } catch (Exception e) { 
          return "EXCEPTION GETTING SPEC VERSION "+e.toString(); 
        }
      } else { 
        return specificationVersion_;
      }
    }

    /**
     Run the test as an application.  This should be called from the test driver's main().
     @param  testDriver  A test driver object.
     **/
    public static void runApplication(TestDriverI testDriver)
    {

        try
        {
	    String timeout = System.getProperty("timeout");
	    if (timeout != null) {
		try {
		    long longTimeout = Long.parseLong(timeout);
		    if (longTimeout > 0) {
			System.out.println("Starting timeout thread for "+longTimeout+" ms ");
			timeoutThread = new TestDriverTimeoutThread(longTimeout);
			timeoutThread.start();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

	    }
	    /* CHANGES TO USE PEX... on Front end */
	    String pex = System.getProperty("pex");
	    if (pex != null) {
		System.out.println("Starting PEX "+JDJobName.startPex());
	    }

            testDriver.init();
            testDriver.start();
            testDriver.stop();
            staticTestcaseResults = testDriver.getTestcaseResults();
	    if (pex != null) {
		System.out.println("Ending PEX "+JDJobName.endPex());
	    }


            testDriver.destroy();
        }
        catch (Exception e)
        {
            System.out.println("Program terminated abnormally.");
            e.printStackTrace();
        }
	finally {

	    if (timeoutThread != null) {
		timeoutThread.markDone();
	    }

	}

        if (!servlet_ && !systemExitDisabled_)
        {
	    TestDriverSecurityManager.setAllowExit(true); 
            System.exit(0);
        }
    }

    /**
     Set a flag that tells the runApplication method above whether or not to call System.exit().
     **/
    public static void setSystemExitDisabled(boolean systemExit)
    {
        systemExitDisabled_ = systemExit;
    }

    static String arrayToString(String[] strings)
    {
      if (strings == null) return "null";
      if (strings.length == 0) return "";
      StringBuffer buf = new StringBuffer(strings[0]);
      for (int i=1; i<strings.length; i++)
      {
        buf.append(" " + strings[i]);
      }
      return buf.toString();
    }


    // Replace passwords by "xxxxxxxx".
    static String obfuscatePasswords(String args[])
    {
      boolean priorArgWasPwdOption = false;
      boolean priorArgWasPwrSysOption = false;
      StringBuffer newArgs = new StringBuffer();
      for (int i=0; i<args.length; i++)
      {
        String arg = args[i];
        if (priorArgWasPwdOption) {
          arg = "xxxxxxxx";
          priorArgWasPwdOption = false;
        }
        else if (priorArgWasPwrSysOption) {
          int commaPos = arg.indexOf(',');
          if (commaPos != -1) {
            arg = arg.substring(0,commaPos+1) + "xxxxxxxx";
          }
          priorArgWasPwrSysOption = false;
        }
        else {
          if (arg.equalsIgnoreCase("-pwd")) {
            priorArgWasPwdOption = true;
          }
          else if (arg.equalsIgnoreCase("-pwrSys")) {
            priorArgWasPwrSysOption = true;
          }
        }
        newArgs.append(arg);
        newArgs.append(' ');
      }
      return newArgs.toString();
    }

    public static File getLoadSource(String className)
    {
      if (className == null) throw new NullPointerException("className");
      File loadedFromFile = null;
      try
      {
	ClassLoader loader  = null; 
	try {
	    loader =  Class.forName(className).getClassLoader();
	} catch(Exception e) {
	    e.printStackTrace(); 
	}
        if (loader == null) { 
            loader = ClassLoader.getSystemClassLoader(); 
        }
        if (loader != null)  {
          String fileName = className.replace('.', '/') + ".class";
          URL jarUrl = loader.getResource(fileName);
          if (jarUrl != null)    {
            String classLoadedFromPath = jarUrl.getPath();
            if (classLoadedFromPath != null) {
              String jarDirPath = classLoadedFromPath.substring(0, classLoadedFromPath.length() - fileName.length()); // strip filename from end of path
              if (jarDirPath.startsWith("file:")) {
                jarDirPath = jarDirPath.substring(5); // strip "file:" prefix
              }
              if (jarDirPath.endsWith("/") || jarDirPath.endsWith("\\")) {
                jarDirPath = jarDirPath.substring(0, jarDirPath.length()-1); // strip final char
              }
              if (jarDirPath.endsWith("!") || jarDirPath.endsWith("|")) {
                jarDirPath = jarDirPath.substring(0, jarDirPath.length()-1); // strip final char
              }
              if (jarDirPath.length() != 0) {
                loadedFromFile = new File(jarDirPath);
              }
            } else {
              System.out.println("...Warning..getLoadSource():  classLoadedFromPath is null"); 
            }
          } else {
            System.out.println("...Warning..getLoadSource():  jarUrl is null"); 
          }
        } else {
          System.out.println("...Warning..getLoadSource():  loader is null"); 
        }
      }
      catch (Exception e) {
          System.out.println("Exception determining jar source"); 
          e.printStackTrace(System.out); 
      }
      return loadedFromFile;
    }


    public static String deleteLibrary(String library, AS400 system) {

        try
        {
            CommandCall cmd = new CommandCall(system);
	    return TestDriver.deleteLibrary(cmd, library); 
        }
        catch (Throwable e)
        {
            System.out.println("Exception deleting library: " + library);
            e.printStackTrace(System.out);
            return e.toString(); 
        }


    } 
    public static boolean cmdRun(String command, AS400 system, PrintWriter out, String expectedMessage)
    {
        try
        {
            CommandCall cmd = new CommandCall(system);
            boolean ret = cmd.run(command);
            if (!ret)
            {
                AS400Message[] msg = cmd.getMessageList();
                if (expectedMessage != null && msg.length == 1 && expectedMessage.equals(msg[0].getID()))
                {
                    return true;
                }
                out.println("Error running command: " + command);
                for (int i = 0; i  < msg.length; ++i)
                {
                    out.println(msg[i].getID() + " " + msg[i].getText());
                }
            }
            return ret;
        }
        catch (Throwable e)
        {
            out.println("Exception running command: " + command);
            e.printStackTrace(out);
        }
        return false;
    }


    public static AS400 getPwrSys()
    {
        return pwrSysStatic_;
    }

    /**
     *
     * @return A vector containing the results.  Each result is a String[] with
     * 0-Name, 1-success, 2-failed, 3-notappli, 4-time.
     */
    static public Vector getStaticTestcaseResults() {
      return staticTestcaseResults;
    }

    public static void restoreSecurityManager() {
      System.setSecurityManager(defaultSecurityManager_); 
    }

}

