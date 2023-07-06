///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JVMRunUtility.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.VMDeathRequest;

/**
 * The JVMRunUtility run a programs in a JVM and provides information about the resources used by
 * that program.   The running program should call System.exit() to end running and allocated resources should
 * still be referenced for that they can be counted. 
 * @author jeber
 *
 */
public class JVMRunUtility extends Thread {

    static boolean debug = false; 
    static { 
      String debugProperty = System.getProperty("debug"); 
      if (debugProperty != null) { 
        if (debugProperty.equalsIgnoreCase("false")) { 
          debug = false; 
        } else {
          debug = true; 
        }
      }
    }
    
    private static final long EVENT_TIMEOUT = 2500;
    VirtualMachine virtualMachine_ = null; 
    String mainClassname_ = "";
    private StringBuffer outputStringBuffer;
    private JDJSTPOutputThread outputThread; 
    
    private StringBuffer errorStringBuffer;
    private JDJSTPOutputThread errorThread;
    private EventRequestManager eventRequestManager;
    private EventQueue eventQueue;
    private Process process_;
    private int topListSize_;
    private TopListEntry[] topList_; 
    
    private Object runningLock_ = new Object(); 
    boolean running_ = true;
    boolean exitCalled_ = false;
    private String args_; 

    
    public JVMRunUtility(String mainClassname, String args, int size) {
      mainClassname_=mainClassname;
      topListSize_ = size;
      args_ = args; 
    }
  
    public void startJVM() throws Exception {
      VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager(); 
      LaunchingConnector launchingConnector = virtualMachineManager.defaultConnector();
      Map defaultArguments = launchingConnector.defaultArguments();
      
      Connector.Argument mainArgument = (Connector.Argument) defaultArguments.get("main"); 
      mainArgument.setValue(mainClassname_+" "+args_); 
      
      Connector.Argument optionsArgument = (Connector.Argument) defaultArguments.get("options"); 
      optionsArgument.setValue("-Dcom.ibm.as400.access.AS400.guiAvailable=false -cp "+System.getProperty("java.class.path")); 
      
      
      Set keySet = defaultArguments.keySet(); 
      Iterator iterator = keySet.iterator();
      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        Connector.Argument argument = (Connector.Argument) defaultArguments.get(key);
        System.out.println(key+","+argument); 
      }
      
      
      virtualMachine_ = launchingConnector.launch(defaultArguments); 
      process_ = virtualMachine_.process(); 
      // TODO:  We must start the threads to read the input streams
      InputStream inputStream = process_.getInputStream();
      InputStream errorStream = process_.getErrorStream(); 

      outputStringBuffer = new StringBuffer(); 
      outputThread = new JDJSTPOutputThread(inputStream, outputStringBuffer,null,JDJSTPOutputThread.ENCODING_UNKNOWN);
      outputThread.setName("JDJSTPOutputThread"); 
      outputThread.start(); 
      
      errorStringBuffer = new StringBuffer(); 
      errorThread = new JDJSTPOutputThread(errorStream, outputStringBuffer,null,JDJSTPOutputThread.ENCODING_UNKNOWN);
      outputThread.setName("JDJSTPErrorOutputThread"); 
      errorThread.start(); 
      
      System.out.println("Virtual Machine is "+virtualMachine_); 
      System.out.println("Process is "+process_); 

      eventQueue = virtualMachine_.eventQueue();
      eventRequestManager = virtualMachine_.eventRequestManager(); 
     
      VMDeathRequest vmDeathRequest = eventRequestManager.createVMDeathRequest();
      vmDeathRequest.enable(); 

      MethodEntryRequest methodEntryRequest = eventRequestManager.createMethodEntryRequest(); 
      methodEntryRequest.addClassFilter("java.lang.System"); 
      
      methodEntryRequest.enable();
      
      virtualMachine_.resume();
      this.setName("JVMRunUtilityThread"); 
      this.start(); 
    }

    public String getOutput() {
      if (outputStringBuffer != null) {
        return outputStringBuffer.toString(); 
      } else {
        return "NONE"; 
      }
    }

    public String getError() {
      if (errorStringBuffer != null) {
        return errorStringBuffer.toString(); 
      } else {
        return "NONE"; 
      }
    }

    public class TopListEntry {
	public String className;
	public long   count; 
    };

  public String[] getTopList() {
    if (topList_ != null) {
      String[] stringTopList = new String[topListSize_];
      for (int i = 0; i < topListSize_; i++) {
        if (topList_[i] != null) {
          stringTopList[i] = topList_[i].className + "=" + topList_[i].count;
        } else {
          stringTopList[i] = "NONE=0";
        }
      }
      return stringTopList;
    } else {
      return null;
    }
    }

    public void createTopList() throws Exception  {

        long minCount  = 0;

	topList_ = new TopListEntry[topListSize_]; 

	for (int j = 0; j < topListSize_; j++) {
	    topList_[j] = new TopListEntry(); 
	    topList_[j].count = 0;
	    topList_[j].className="NONE"; 
	}
	minCount = 0; 
	List allClasses = virtualMachine_.allClasses();
	// Note:  instanceCounts() is only supported JDK 1.6 and later
	// We must sure reflection to call it.
	long[] instanceCounts;
  try { 
    instanceCounts = (long[]) JDReflectionUtil.callMethod_O(virtualMachine_, "instanceCounts", 
	    Class.forName("java.util.List"), allClasses);
  } catch (Exception e) {
    System.out.println("Error:  unable to get instance count.  Using JDK 1.6 tools.jar?"); 
    e.printStackTrace(System.out); 
    instanceCounts = new long[allClasses.size()]; 
  }

	int i = 0;
	Iterator iterator = allClasses.listIterator();
	
	while(iterator.hasNext()) {
	    ReferenceType referenceType = (ReferenceType) iterator.next();
	    long count = instanceCounts[i];
	    if (count > minCount) {
		boolean found = false; 
		for (int j = 0; !found && j  < topListSize_; j++) {
		    if (count > topList_[j].count) {
			// Slide every thing down
			for (int k = topListSize_ - 1; k > j; k--) {
			    topList_[k] = topList_[k-1]; 
			}
			topList_[j] = new TopListEntry();
			topList_[j].count = count;
			topList_[j].className =  referenceType.name(); 
			found = true;
			minCount = topList_[topListSize_-1].count; 
		    }
		}
	    }

	    i++; 
	}
    }

  public void run() {
      long nextOutputTime = 0; 
    while (running_) {
      try {
        EventSet eventSet = eventQueue.remove(EVENT_TIMEOUT);
        if (running_ && eventSet != null) {
          String eventSetString = "UNKNOWN";
          try {
            eventSetString = "" + eventSet;
          } catch (Exception e) {
            e.printStackTrace();
            eventSetString = "Exception caught: " + e.toString();
          }
          // Don't display events from the
          // com.ibm.tools.attach package
          if (eventSetString.indexOf("com.ibm.tools") > 0) {
            // just ignore this
          } else {
            if (debug) System.out.println("Event set is " + eventSetString);
            if (debug) System.out.flush();

            //
            // If breakpoint hit, dump the local vars and the
            // stack
            //
            Iterator eventIterator = eventSet.iterator();
            while (eventIterator.hasNext()) {
              Event event = (Event) eventIterator.next();
              if (event instanceof VMDeathEvent) {
                if (debug) System.out.println("Hit VMDeathEvent setting running_=false"); 
                synchronized(runningLock_) {
                   running_ = false;
                   exitCalled_  = true; 
                   runningLock_.notifyAll(); 
                   
                }
              } else if (event instanceof VMStartEvent) {
              } else if (event instanceof VMDisconnectEvent) {
                if (debug) System.out.println("Hit VMDisconnectEvent setting running_=false"); 
                synchronized(runningLock_) {
                  running_ = false;
                  exitCalled_  = true; 
                  runningLock_.notifyAll();
                }
              } else if (event instanceof MethodEntryEvent) {
                MethodEntryEvent entryEvent = (MethodEntryEvent) event;
                Method m;
                try {
                  m = entryEvent.method();
                  String methodInfo = m.toString(); 
                  if (debug) { 
                     System.out.println("Hit methodEntryEvent for  " + methodInfo);
                     System.out.flush();
                  }
                  if (methodInfo.indexOf("java.lang.System.exit") >= 0) {
                    createTopList(); 
                    synchronized(runningLock_) {
                      exitCalled_  = true;
                      runningLock_.notifyAll(); 
                   }
                  }
                  

                } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("Exception processing methodEntryEvent "
                      + e.toString());
                  System.out.flush();
                }
              } else if (event instanceof MethodExitEvent) {
                MethodExitEvent exitEvent = (MethodExitEvent) event;

                try {
                  System.out.println("Hit methodExitEvent for  "
                      + exitEvent.method().toString());
                  System.out.flush();
                } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("EXCEPTION processing methodExitEvent "
                      + e.toString());
                  System.out.flush();
                }

              } else {
                System.out.println("Hit event unrecognized event " + event);
                System.out.flush();
              }
            }

            eventSet.resume();

          } /* not from com.ibm.tools */
        } else {
          // Just continue and check if running
          if (running_) {
            try {
              int rc = process_.exitValue();
              if (debug) System.out.println("***** process_.exitValue() = "+rc); 

              // If we got an exit value, then the process terminated. 
              synchronized (runningLock_) {
                running_ = false;
                exitCalled_  = true; 
                runningLock_.notifyAll();
              }

            } catch (IllegalThreadStateException ex) {
              // Still alive
		if (debug) {
		    System.out.println("process_.exitValue() returned exception");
		    if (System.currentTimeMillis() > nextOutputTime) {
			System.out.println("stdout") ;
			System.out.println(getOutput());
			System.out.println("stderr") ;
			System.out.println(getError());
			System.out.println("---------------------------------------------"); 
			nextOutputTime = System.currentTimeMillis() + 30000; 
		    } 
		}

            }
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        if (e instanceof com.sun.jdi.VMDisconnectedException) {
          synchronized(runningLock_) {
             running_ = false;
             exitCalled_  = true; 
             runningLock_.notifyAll(); 

          }
        }
      }
    }
    if (debug) System.out.println("JVMRunUtility thread completed running_ = "+running_);
    synchronized(runningLock_) {
      exitCalled_ = true; 
    }
  }
  
  public void waitForExit() {
    synchronized(runningLock_) {
      while (!exitCalled_) {
        try {
          runningLock_.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
          exitCalled_ = true; 
        } 
      }
    }
  }
    
    // Unit test code. 
    public static void main(String[] args) {
      try { 
        System.out.println("Usage:  java test.JVMRunUtility <class> <args> <top#>"); 
        int topNumber =  Integer.parseInt(args[2]); 
        if (topNumber == 0) {
          System.out.println("Invalid top number"); 
        }
        JVMRunUtility runUtility = new JVMRunUtility(args[0], args[1],topNumber ); 
        runUtility.startJVM();
        runUtility.waitForExit(); 
        String[] topList = runUtility.getTopList();
        System.out.println("topList is ");
        if (topList == null) { 
          System.out.println("Error:  topList is null.  was exit called?"); 
        } else { 
          for (int i = 0; i < topList.length; i++) {
          System.out.println((i+1)+": "+topList[i]); 
          }
        }
        System.out.println("OUTPUT"); 
        System.out.println(runUtility.getOutput()); 
        System.out.println("ERROR"); 
        System.out.println(runUtility.getError()); 
      } catch (Exception e) { 
        e.printStackTrace(); 
      }
    }

}
