///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDebugger.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
// 
// This code depends on the use of tools.jar from JDK 1.8 or earlier
// 
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.ClassType;
import com.sun.jdi.LocalVariable;
import com.sun.jdi.Location;
import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadGroupReference;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.event.BreakpointEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.ExceptionEvent;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.MethodExitEvent;
import com.sun.jdi.event.ThreadDeathEvent;
import com.sun.jdi.event.ThreadStartEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.ExceptionRequest;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.MethodExitRequest;
import com.sun.jdi.request.ThreadDeathRequest;
import com.sun.jdi.request.ThreadStartRequest;

//
// Debugger thread used by JDSQL400
// Note:  Needs tools.jar on classpath.
//        tools.jar is shipped with the jdk
//

public class JDDebugger extends Thread {
	public static int EVENT_TIMEOUT = 2500;
	boolean running;
	String command = null;
	PrintStream out = null;

	VirtualMachine virtualMachine;
	EventRequestManager eventRequestManager;
	EventQueue eventQueue;

	@SuppressWarnings("unchecked")
  public JDDebugger(int portNumber, PrintStream out) throws Exception {
		this.out = out;

		VirtualMachineManager virtualMachineManager = Bootstrap.virtualMachineManager();

		List<?> attachingConnectors = virtualMachineManager.attachingConnectors();
		Iterator<?> iterator = attachingConnectors.iterator();
		AttachingConnector socketAttachingConnector = null;
		while (iterator.hasNext() && (socketAttachingConnector == null)) {
			AttachingConnector attachingConnector = (AttachingConnector) iterator.next();
			if (attachingConnector.name().indexOf("Socket") != -1) {
				socketAttachingConnector = attachingConnector;
			}
		}

		@SuppressWarnings("rawtypes")
    Map map = socketAttachingConnector.defaultArguments();
		// out.println(map);

		Connector.Argument hostname = (Connector.Argument) map.get("hostname");
		hostname.setValue("localhost");
		Connector.Argument port = (Connector.Argument) map.get("port");
		port.setValue("" + portNumber);

		int retryCount = 10;
		long startTime = System.currentTimeMillis();
		while (retryCount > 0) {
			try {
				virtualMachine = socketAttachingConnector.attach(map);
				retryCount = 0;
			} catch (java.net.ConnectException ex) {
				retryCount--;
				if (retryCount == 0) {
					long attachTime = (System.currentTimeMillis() - startTime) / 1000;
					System.out.println("Debugger not able to attach in " + attachTime + " seconds");
					throw ex;
				} else {
					Thread.sleep(5000);
				}
			}
		}

		eventRequestManager = virtualMachine.eventRequestManager();
		eventQueue = virtualMachine.eventQueue();

	}

	public void stopDebugger() {
		synchronized (this) {
			running = false;
		}
		// Sleep so event wil timeout
		try {
			Thread.sleep(EVENT_TIMEOUT + 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
		synchronized (this) {
			notify();
		}

		try {
			Thread.sleep(EVENT_TIMEOUT + 100);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public synchronized void setThreadStartRequest() {
		ThreadStartRequest threadStartRequest = eventRequestManager.createThreadStartRequest();
		threadStartRequest.enable();
		out.println("DEBUGGER:  setThreadStartRequest");
		out.flush();

	}

	public synchronized void setThreadDeathRequest() {
		ThreadDeathRequest threadDeathRequest = eventRequestManager.createThreadDeathRequest();
		threadDeathRequest.enable();
		out.println("DEBUGGER:  setThreadStartRequest");
		out.flush();
	}

	public synchronized void showThreads(int maxCount) {
		// Show the threads associated with the JVM.
		List<?> threadList = virtualMachine.allThreads();
		Iterator<?> threadIterator = threadList.iterator();
		int count = 0;
		while (threadIterator.hasNext()) {
			ThreadReference threadReference = (ThreadReference) threadIterator.next();
			ThreadGroupReference threadGroupReference = (ThreadGroupReference) threadReference.threadGroup();
			if (count < maxCount) {
				count++;
				out.println(
						"Thread: " + threadReference.name() + " status:" + threadReference.status() + " suspendCount:"
								+ threadReference.suspendCount() + " threadGroup:" + threadGroupReference.name());
				out.flush();
			}
		}
		while (count < maxCount) {
			count++;
			out.println("");
			out.flush();
		}
	}

	public synchronized void showLineLocations(String showArgs) {
		String className = showArgs;

		//
		// Now find the class
		//
		List<?> classList = virtualMachine.classesByName(className);
		ClassType classType = null;
		if (!classList.isEmpty()) {
			classType = (ClassType) classList.get(0);
			try {
				List<?> lineList = classType.allLineLocations();
				if (!lineList.isEmpty()) {
					Iterator<?> iterator = lineList.iterator();
					while (iterator.hasNext()) {
						Location l = (Location) iterator.next();
						out.println("Location: " + l.sourceName() + ":" + l.lineNumber());
						out.flush();
					} /* while */
				} else {
					out.println("Unable to find lines ");
					out.flush();
				}
			} catch (AbsentInformationException aie) {
				out.println("Caught exception " + aie.toString());
				out.flush();
			}
		} else {
			out.println("Unable to find class " + className);
			out.flush();
		}

	}

	public synchronized void setBreakpoint(String breakArgs) {
		String className;
		String methodName;
		int lastDot = breakArgs.lastIndexOf('.');
		if (lastDot > 0) {
			className = breakArgs.substring(0, lastDot).trim();
			methodName = breakArgs.substring(lastDot + 1).trim();

			//
			// Now find the class
			//
			List<?> classList = virtualMachine.classesByName(className);
			ClassType classType = null;
			if (!classList.isEmpty()) {
				classType = (ClassType) classList.get(0);
				List<?> methodList = classType.methodsByName(methodName);
				if (!methodList.isEmpty()) {
					Iterator<?> iterator = methodList.iterator();
					while (iterator.hasNext()) {
						Method method = (Method) iterator.next();
						Location l = method.location();

						BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(l);
						breakpointRequest.enable();
						out.println("Enabled breakpoint for location " + l);
						out.flush();

					} /* while */
				} else {
					out.println("Unable to find method " + methodName);
					out.flush();

				}
			} else {
				out.println("Unable to find class " + className);
				out.flush();

			}

		} else {
			out.println("Unable to find class and method in " + breakArgs);
			out.flush();
		}
		if (eventRequestManager != null) {
			List<?> breakpointRequests = eventRequestManager.breakpointRequests();
			out.println("DEBUGGER:  current breakpointRequests are ");
			out.flush();
			Iterator<?> breakpointIterator = breakpointRequests.iterator();
			while (breakpointIterator.hasNext()) {
				out.println("   " + breakpointIterator.next().toString());
				out.flush();
			}
		}

	}

	public synchronized void setMethodEntry() {
		MethodEntryRequest methodEntryRequest = eventRequestManager.createMethodEntryRequest();
		// Filter out classes that we don't care to see
		methodEntryRequest.addClassExclusionFilter("java.*");
		methodEntryRequest.addClassExclusionFilter("com.ibm.db2.*");
		methodEntryRequest.addClassExclusionFilter("com.ibm.oti.*");
		methodEntryRequest.addClassExclusionFilter("com.ibm.jit.*");
		methodEntryRequest.addClassExclusionFilter("com.ibm.tools.*");
		methodEntryRequest.addClassExclusionFilter("sun.*");
		methodEntryRequest.addClassExclusionFilter("sqlj.*");
		methodEntryRequest.addClassExclusionFilter("jdk.internal.*");

		methodEntryRequest.enable();
		out.println("Enabled method entry request ");
		out.flush();

	}

	public synchronized void setMethodExit() {
		MethodExitRequest methodExitRequest = eventRequestManager.createMethodExitRequest();
		// Filter out classes that we don't care to see

		methodExitRequest.addClassExclusionFilter("java.*");
		methodExitRequest.addClassExclusionFilter("com.ibm.db2.*");
		methodExitRequest.addClassExclusionFilter("com.ibm.oti.*");
		methodExitRequest.addClassExclusionFilter("com.ibm.jit.*");
		methodExitRequest.addClassExclusionFilter("com.ibm.tools.*");
		methodExitRequest.addClassExclusionFilter("sun.*");
		methodExitRequest.addClassExclusionFilter("sqlj.*");
		methodExitRequest.addClassExclusionFilter("jdk.internal.*");
		methodExitRequest.enable();
		out.println("Enabled method exit request ");
		out.flush();

	}

	public synchronized void setUncaughtException() {
		ExceptionRequest exceptionRequest = eventRequestManager.createExceptionRequest(null, false, true);
		exceptionRequest.enable();
		out.println("Enabled any uncaught exception");
		out.flush();
	}

	public void run() {
		running = true;
		while (running) {
			try {
				EventSet eventSet = eventQueue.remove(EVENT_TIMEOUT);
				if (running && eventSet != null) {
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
						out.println("Event set is " + eventSetString);
						out.flush();

						//
						// If breakpoint hit, dump the local vars and the
						// stack
						//
						Iterator<?> eventIterator = eventSet.iterator();
						while (eventIterator.hasNext()) {
							Event event = (Event) eventIterator.next();
							if (event instanceof BreakpointEvent) {
								out.println("Hit breakpoint event " + event);
								out.flush();
								ThreadReference threadReference = ((BreakpointEvent) event).thread();
								List<?> framesList = threadReference.frames();
								Iterator<?> framesIterator = framesList.iterator();
								boolean first = true;
								while (framesIterator.hasNext()) {
									StackFrame stackFrame = (StackFrame) framesIterator.next();
									Location stackFrameLocation = stackFrame.location();
									if (first) {
										out.println("Stack vars");
										out.flush();
										try {
											List<?> variablesList = stackFrame.visibleVariables();
											Iterator<?> variablesIterator = variablesList.iterator();
											while (variablesIterator.hasNext()) {
												LocalVariable localVariable = (LocalVariable) variablesIterator.next();
												Value value = stackFrame.getValue(localVariable);
												if (value == null) {
													out.println(localVariable.typeName() + ":" + localVariable.name()
															+ "= null");
													out.flush();
												} else {
													out.println(localVariable.typeName() + ":" + localVariable.name()
															+ "=" + value.toString());
												}
											}
										} catch (AbsentInformationException aie) {
											out.println("AbsentInformationException caught");
											out.flush();
										}
										out.println("Stack trace");
										out.flush();
										first = false;
									}
									out.println("  " + stackFrameLocation.toString());
									out.flush();
								}
							} else if (event instanceof VMDeathEvent) {
								running = false;
							} else if (event instanceof VMDisconnectEvent) {
								running = false;
							} else if (event instanceof MethodEntryEvent) {
								MethodEntryEvent entryEvent = (MethodEntryEvent) event;
								Method m;
								try {
									m = entryEvent.method();
									String eventName = entryEvent.method().toString();
									out.println("Hit methodEntryEvent for  " + eventName);
									out.flush();

									// If event is jit event, don't dump any of the arguments
									if (eventName.indexOf("JITHelpers") > 0) {
										// Do not dump the details for this events that are ignored
									} else {
										List<?> argList = m.arguments();
										ThreadReference threadReference = entryEvent.thread();
										StackFrame stackFrame = threadReference.frame(0);
										Iterator<?> listIterator = argList.iterator();
										while (listIterator.hasNext()) {
											LocalVariable localVariable = (LocalVariable) listIterator.next();
											Value value = stackFrame.getValue(localVariable);
											if (value == null) {
												out.println(localVariable.typeName() + ":" + localVariable.name()
														+ "= null");
												out.flush();
											} else {
												out.println(localVariable.typeName() + ":" + localVariable.name() + "="
														+ value.toString());
												out.flush();
											}

										} /* while */
									}

								} catch (Exception e) {
									e.printStackTrace();
									out.println("Exception processing methodEntryEvent " + e.toString());
									out.flush();
								}
							} else if (event instanceof MethodExitEvent) {
								MethodExitEvent exitEvent = (MethodExitEvent) event;

								try {
									out.println("Hit methodExitEvent for  " + exitEvent.method().toString());
									out.flush();
								} catch (Exception e) {
									e.printStackTrace();
									out.println("EXCEPTION processing methodExitEvent " + e.toString());
									out.flush();
								}

							} else if (event instanceof ExceptionEvent) {
								ExceptionEvent exceptionEvent = (ExceptionEvent) event;

								ObjectReference exceptionRef = exceptionEvent.exception();
								out.println("Hit ExceptionEvent for  " + exceptionRef.toString());

								out.println("        catchLocation = " + exceptionEvent.catchLocation());
								out.flush();

							} else if (event instanceof ThreadStartEvent) {
								out.println("Hit event " + event);
								out.flush();
							} else if (event instanceof ThreadDeathEvent) {
								out.println("Hit event " + event);
								out.flush();
							} else {
								out.println("Hit event unrecognized event " + event);
								out.flush();
							}
						}

						eventSet.resume();
					} /* not from com.ibm.tools */
				} else {
					// Just continue and check if running
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (e instanceof com.sun.jdi.VMDisconnectedException) {
					running = false;
				}
			}
		}

		//
		// Mark that we are done
		//
		try {
			virtualMachine.dispose();
		} catch (Exception e) {
			// ignore any exceptions from dispose
		}

	}

}
