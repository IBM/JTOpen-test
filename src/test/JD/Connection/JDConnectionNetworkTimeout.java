///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionNetworkTimeout.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionNetworkTimeout.java
//
// Classes:      JDConnectionNetworkTimeout
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDReflectionUtil;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDConnectionNetworkTimeout.  his tests the following methods
of the JDBC Connection class:

<ul>
<li>setNetworkTimeout()</li>
<li>getNetworkTimeout()</li>

</ul>
**/
public class JDConnectionNetworkTimeout extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionNetworkTimeout";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }



    // Private data.
  
    private Connection conn_ = null;
    private String baseCollection_ = ""; 
    StringBuffer sb = new StringBuffer(); 


/**
Constructor.
**/
    public JDConnectionNetworkTimeout (AS400 systemObject,
                              Hashtable<String,Vector<String>> namesAndVars,
                              int runMode,
                              FileOutputStream fileOutputStream,
                              
                              String password)
    {
        super (systemObject, "JDConnectionNetworkTimeout",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup () throws Exception  {

        conn_ = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);

	// Create schemas A B C D
	baseCollection_ = JDConnectionTest.COLLECTION;
	if (baseCollection_.length() > 9) {
	    throw new Exception("collection '"+baseCollection_+"' is too long"); 
	}
        conn_.commit();
        
 
        
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
	conn_.close(); 
    }


    private void useConnection(Connection c, int repeats, int callDelaySeconds, int sleepDelaySeconds) throws Exception {
      String sqlText = "CALL QSYS.QCMDEXC('DLYJOB DLY("+callDelaySeconds+")                                    ', 0000000040.00000)";
      sb.append("Preparing "+sqlText+"\n"); 
      PreparedStatement ps = c.prepareStatement(sqlText); 

      for (int i = 0; i < repeats; i++) {
        sb.append("Executing \n"); 
        ps.execute();
        sb.append("Sleeping for "+sleepDelaySeconds+" seconds \n"); 
        Thread.sleep(1000 * sleepDelaySeconds); 
      }
      ps.close(); 
    }

    
    
    
/**
getNetworkTimeout -- Get default value -- should be zero 
**/

    public void Var001 ()
    {
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {

	    try {
		sb.setLength(0);
		int value = JDReflectionUtil.callMethod_I(conn_, "getNetworkTimeout"); 
		

		assertCondition (false, "Native driver should thrown unsupported exception but value = "+value);

	    } catch (Exception e) {
		assertExceptionContains(e, "The driver does not support this function", sb);
	    }

	} else { 
	    try {
		sb.setLength(0);
		int value = JDReflectionUtil.callMethod_I(conn_, "getNetworkTimeout"); 


		assertCondition (value == 0, "getNetworkTimeout returned "+value+" sb 0"); 

	    } catch (Exception e) {
		failed(e, "Unexpected Exception");
	    }
	}
    }



/**
getNetworkTimeout -- Get on closed connection -- should throw exception.
**/
    public void Var002 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
        try {
            sb.setLength(0);
            sb.append(" -- Calling getNetworkTimeout on closed connection"); 
            Connection c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            c.close(); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            
            assertCondition (false, "got "+value+sb.toString()); 

        } catch (Exception e) {
            assertExceptionContains(e, "The connection does not exist", sb);
           
        }
    }

    void callAbort(Connection connection, Object thisExecutor, StringBuffer sb1) throws Exception {
      sb1.append("Call abort \n"); 
      {
        Class<?>[] argTypes = new Class[1]; 
        Object[] args = new Object[1]; 
        argTypes[0] = Class.forName("java.util.concurrent.Executor"); 
        args[0] = thisExecutor; 
        JDReflectionUtil.callMethod_V(connection, "abort", argTypes, args);
      }
    }

    
    void callSetNetworkTimeout(Connection connection, Object thisExecutor, int milliseconds, StringBuffer sb1) throws Exception {
      sb1.append("Call setNetworkTimeout("+milliseconds+" ms) \n"); 
      {
        Class<?>[] argTypes = new Class[2]; 
        Object[] args = new Object[2]; 
        argTypes[0] = Class.forName("java.util.concurrent.Executor"); 
        argTypes[1] = Integer.TYPE; 
        args[0] = thisExecutor; 
        args[1] = new Integer(milliseconds); 
        JDReflectionUtil.callMethod_V(connection, "setNetworkTimeout", argTypes, args);
      }
    }



/**
getNetworkTimeout -- Get on aborted connection -- should throw exception.
**/
    public void Var003 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
        if (checkJdbc40()) { 
        try {
          sb.setLength(0);
          sb.append(" -- Calling getNetworkTimeout on aborted connection\n"); 
          
          Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);

          
            Connection c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            callAbort(c, thisExecutor, sb); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            
            assertCondition (false, "got "+value+sb.toString()); 

        } catch (Exception e) {
            assertExceptionContains(e, "connection does not exist", sb);
           
        }
        }
    }


/**
getNetworkTimeout -- Get value that was set. 
**/

    public void Var004 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
      if (checkJdbc41()) {
        try {
            sb.setLength(0);
            
            sb.append(" -- Calling getNetworkTimeout to get value that was set\n"); 
            Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);
            
            Connection c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            
            callSetNetworkTimeout(c, thisExecutor, 6000, sb); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            assertCondition (value == 6000, "getNetworkTimeout returned "+value+" sb 6000"+sb.toString()); 

        } catch (Exception e) {
            failed(e, "Unexpected Exception"+sb.toString());
        }
      }
    }

/**
setNetworkTimeout -- Set negative value -- should throw exception
**/
    public void Var005 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
      Connection c = null; 
      if (checkJdbc41()) {
        try {
            sb.setLength(0);
            
            sb.append(" -- Calling setNetworkTimeout to set negative value\n"); 
            Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);
            
            c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            
            callSetNetworkTimeout(c, thisExecutor, -1, sb); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            assertCondition (false, "Got "+value+" Exception not thrown "+sb.toString()); 

        } catch (Exception e) {
          assertExceptionContains(e, "Parameter type not valid", sb);
        } finally {
          try {
            if (c != null) c.close(); 
          } catch (Exception e) {} 
        }
      }
    }
    

/**
setNetworkTimeout -- Set null executor --  should throw exception
**/
    
    public void Var006 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
      Connection c = null; 
      if (checkJdbc41()) {
        try {
            sb.setLength(0);
            
            sb.append(" -- Calling setNetworkTimeout to set null executor value\n"); 
            Object thisExecutor = null; 
            
            c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            
            callSetNetworkTimeout(c, thisExecutor, 600, sb); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            assertCondition (false, "Got "+value+" Exception not thrown "+sb.toString()); 

        } catch (Exception e) {
          assertExceptionContains(e, "Parameter type not valid. (executor=null)", sb);
        } finally {
          try {
            if (c != null) c.close(); 
          } catch (Exception e) {} 
        }
      }
    }
    

/**
setNetworkTimeout -- Call on closed connection -- should throw exception
**/
    public void Var007 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
      Connection c = null; 
      if (checkJdbc41()) {
        try {
            sb.setLength(0);
            
            sb.append(" -- Calling setNetworkTimeout on closed connection \n"); 
            Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);
            
            c = testDriver_.getConnection (baseURL_+";thread used=false", userId_, encryptedPassword_);
            c.close(); 
            callSetNetworkTimeout(c, thisExecutor, 600, sb); 
            int value = JDReflectionUtil.callMethod_I(c, "getNetworkTimeout"); 
            
            assertCondition (false, "Got "+value+" Exception not thrown "+sb.toString()); 

        } catch (Exception e) {
          assertExceptionContains(e, "connection does not exist", sb);
        }
      }
    }


/**
setNetworkTimeout -- Call when security manager does not allow access -- should throw exception. 
**/
    public void Var008() {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
      assertCondition(true); 
      // Security manager deprecated. 
  }

    /**
    setNetworkTimeout -- Call when security manager does not allow access -- should throw exception. 
    **/
        public void Var009() {
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable("network timeout not implemented for native driver"); 
            return; 
          }
          assertCondition(true); 
          // Security manager deprecated. 
      }

    
/**
setNetworkTimeout -- Set zero value with security manager present -- Make sure connection can be used
**/
        public void Var010() {
          if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            notApplicable("network timeout not implemented for native driver"); 
            return; 
          }
          assertCondition(true); 
          // Security manager deprecated. 
        }





        
/**
setNetworkTimeout -- Set zero value with no security manager present -- Make sure connection can be used
**/

	public void Var011() {
	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	        notApplicable("network timeout not implemented for native driver"); 
	        return; 
	      }
	    Connection c;
	    if (checkJdbc41()) {
		sb.setLength(0);
		sb.append(" -- Calling setNetworkTimeout whenno  security manager present\n");


		try {

		    Object thisExecutor = createExecutor(
							 "java.util.concurrent.ThreadPoolExecutor", sb);

		    c = testDriver_.getConnection(baseURL_+";thread used=false", userId_, encryptedPassword_);
		    callSetNetworkTimeout(c, thisExecutor, 0, sb);
		    int callDelaySeconds = 1;
		    int sleepDelaySeconds = 11; 
		    int repeats = 6; 
		    useConnection(c, repeats, callDelaySeconds, sleepDelaySeconds);

		    assertCondition(true,sb);


		} catch (Exception e) {
		    failed(e, "Unexpected Exception "+sb.toString());
		}

	    }
	}


/**
setNetworkTimeout -- Set 10 second value  -- Make sure connection can be used
**/

	public void Var012() {
	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	        notApplicable("network timeout not implemented for native driver"); 
	        return; 
	      }
	    Connection c;
	    if (checkJdbc41()) {
		sb.setLength(0);
		sb.append(" -- Calling setNetworkTimeout with 10 second timeout\n");


		try {

		    Object thisExecutor = createExecutor(
							 "java.util.concurrent.ThreadPoolExecutor", sb);

		    c = testDriver_.getConnection(baseURL_+";thread used=false", userId_, encryptedPassword_);
		    callSetNetworkTimeout(c, thisExecutor, 10000, sb);
		    int callDelaySeconds = 1;
		    int sleepDelaySeconds = 11; 
		    int repeats = 6; 
		    useConnection(c, repeats, callDelaySeconds, sleepDelaySeconds);

		    assertCondition(true,sb);


		} catch (Exception e) {
		    failed(e, "Unexpected Exception "+sb.toString());
		}

	    }
	}


/**
setNetworkTimeout -- Set 10 second value -- Use connection with 20 second delay -- exception should be thrown */
	public void Var013() {
	      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	        notApplicable("network timeout not implemented for native driver"); 
	        return; 
	      }
	      if (toolboxNative) {
	        notApplicable("network timeout not implemented for toolbox native driver"); 
	        return; 
	      } 
	    Connection c;
	    
	    if (checkJdbc41()) {
	    	boolean passed=true; 
		sb.setLength(0);
		sb.append(" -- Calling setNetworkTimeout with 5 second but 20 second call delay \n");


		try {

		    Object thisExecutor = createExecutor(
							 "java.util.concurrent.ThreadPoolExecutor", sb);

		    c = testDriver_.getConnection(baseURL_+";thread used=false", userId_, encryptedPassword_);
		    callSetNetworkTimeout(c, thisExecutor, 5000, sb);
		    int callDelaySeconds = 20;
		    int sleepDelaySeconds = 1; 
		    int repeats = 6;
		    try { 
		    	useConnection(c, repeats, callDelaySeconds, sleepDelaySeconds);
		    	sb.append("Connection was did not timeout\n");
		    	passed = false; 
		    } catch (SQLException e) {
              assertExceptionContains(e, "Communication link failure", sb);
              return; 
		    }

		    assertCondition(passed,sb);


		} catch (Exception e) {
		    failed(e, "Unexpected Exception "+sb.toString());
		}

	    }
	}

	/* Make sure fails fast with default "thread used" setting */
/**
getNetworkTimeout -- Get on connection with default "thread used" setting -- should throw exception.
**/
    public void Var014 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
        if (checkJdbc40()) { 
        try {
          sb.setLength(0);
          sb.append(" -- Calling getNetworkTimeout on aborted connection\n"); 
          
          Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);

          
            Connection c = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

            callSetNetworkTimeout(c, thisExecutor, 600, sb);


            
            assertCondition (false, "exception not found "+sb.toString()); 

        } catch (Exception e) {
            assertExceptionContains(e, "The driver does not support this function. (thread used != false)", sb);
           
        }
        }
    }


/**
getNetworkTimeout -- Get on connection with   "thread used=true" setting -- should throw exception.
**/
    public void Var015 ()
    {
      if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        notApplicable("network timeout not implemented for native driver"); 
        return; 
      }
        if (checkJdbc40()) { 
        try {
          sb.setLength(0);
          sb.append(" -- Calling getNetworkTimeout on aborted connection\n"); 
          
          Object thisExecutor = createExecutor("java.util.concurrent.ThreadPoolExecutor", sb);

          
            Connection c = testDriver_.getConnection (baseURL_+";thread used=true", userId_, encryptedPassword_);
	    callSetNetworkTimeout(c, thisExecutor, 600, sb);

            
            
            assertCondition (false, "Exception not thrown "+sb.toString()); 

        } catch (Exception e) {
            assertExceptionContains(e, "The driver does not support this function. (thread used != false)", sb);
           
        }
        }
    }




	/** repeat tests with default "thread used" setting */
	
	
	/** repeat tests with "thread used=true" setting */  
	
	


}


