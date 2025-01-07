///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPHMiscTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.EventSetDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.security.auth.ProfileHandleCredential;
import com.ibm.as400.security.auth.ProfileHandleCredentialBeanInfo;

import test.JDJobName;
import test.JDReflectionUtil;
import test.SecAuthTest;
import test.Testcase;

/**
 Testcase SecPHMiscTestcase contains miscellaneous tests for the ProfileHandleCredential object.
 <p>Test variations cover the following:
 <ul>
 <li>serialization.
 <li>bean behavior.
 </ul>
 **/
public class SecPHMiscTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPHMiscTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
    AS400 nativeSystemObject = null;
    private boolean profileHandleImplNativeAvailable = false; 

    String testUser = "JTOPENSEC1";
    String description = "Test profile to SecPHMiscTestcase";
    char[] testPass = null ;
    private CommandCall pwrCall; 
    
    public void setup() {
     
	if (isNative_) {
	    nativeSystemObject = new AS400(); 
	    System.out.println("nativeSystemObject created"); 
	}

	try {
	    Class.forName("com.ibm.as400.access.ProfileHandleImplNative"); 
	    profileHandleImplNativeAvailable=true; 
	} catch (Exception e) {
	    profileHandleImplNativeAvailable=false; 
	} 

	/* create profiles for native swap method */ 
	if (isNative_) { 
	  pwrCall  = new CommandCall(pwrSys_);
	  Random random = new Random(); 
	  String password = "PASS"+random.nextInt(1000000); 
	  String command = "CRTUSRPRF USRPRF("+testUser+") PASSWORD("+password+") TEXT('"+description+"')   ";
          boolean ran;
          try {
            ran = pwrCall.run(command);
            if (!ran) {
              System.out.println("Warning: COMMAND FAILED -- " + command);
            }
          } catch (Exception e) {
            System.out.println("Exception running command " + command);
            e.printStackTrace(System.out);
          }
          testPass = password.toCharArray();
        }
      }

      public void cleanup() {
        if (isNative_) {
          String command = "DLTUSRPRF USRPRF(" + testUser + ")   ";
          try {
            boolean ran = pwrCall.run(command);
            if (!ran) {
              System.out.println("Warning: COMMAND FAILED -- " + command);
            }
          } catch (Exception e) {
            System.out.println("Exception running command " + command);
            e.printStackTrace(System.out);
          }

        }
      
    }
    /**
     Test serialization and restoration of an uninitialized profile handle.
     **/
    public void Var001()
    {
        try
        {
            ProfileHandleCredential ph1 = null;
            ProfileHandleCredential ph2 = null;

            // Create a ProfileHandleCredential.
            ph1 = new ProfileHandleCredential();

            // Serialize the handle.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("phandle.dat"));
            out.writeObject(ph1);
            out.close();

            // Deserialize the token.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("phandle.dat"));
            ph2 = (ProfileHandleCredential)in.readObject();
            in.close();

            // Test token attributes.
            assertCondition(ph2.equals(ph1) && ph2.getSystem() == null && ph2.getPrincipal() == null && ph2.getHandle() == null, "Unexpected attribute value.");

            File fd = new File("phandle.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test serialization and restoration of an active profile handle.
     Only applicable if running on the local host.
     **/
    public void Var002()
    {
        AS400 sys = null;
        if (isLocal_)
        {
            sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
        }
        else
        {
            sys = new AS400(systemObject_.getSystemName(), SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
        }
        try
        {
            if (!isNative_ || !profileHandleImplNativeAvailable )
            {
                notApplicable();
                sys.close(); 
                return;
            }
            // Create objects.
            ProfileHandleCredential ph1 = null;
            ProfileHandleCredential ph2 = null;

            // Create a ProfileHandleCredential.
            ph1 = new ProfileHandleCredential();
            ph1.setSystem(sys);
            ph1.setHandle();

            // Serialize the handle.
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("phandle.dat"));
            out.writeObject(ph1);
            out.close();

            // Deserialize the handle.
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("phandle.dat"));
            ph2 = (ProfileHandleCredential)in.readObject();
            in.close();

            // Reset the system user and password (not serialized).
            if (isLocal_)
            {
                ph2.getSystem().setUserId("*CURRENT");
                ph2.getSystem().setPassword("*CURRENT".toCharArray());
            }
            else
            {
                ph2.getSystem().setUserId(SecAuthTest.uid1);
                ph2.getSystem().setPassword(SecAuthTest.pwd1.toCharArray());
            }

            // Test validity; compare the handle bytes.
            assertCondition(ph2.equals(ph1), "Unexpected attribute value.");

            File fd = new File("phandle.dat");
            fd.delete();
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            sys.disconnectAllServices();
        }
    }

    /**
     Verify the list of bean info events.
     **/
    public void Var003()
    {
        try
        {
            String[] names =
            {
                "propertyChange",
                "propertyChange",
                "as400Credential"
            };
            EventSetDescriptor[] descs = new ProfileHandleCredentialBeanInfo().getAdditionalBeanInfo()[0].getEventSetDescriptors();
            assertCondition(SecAuthTest.verifyDescriptors(names, descs), "Expected event not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify the list of bean info properties.
     **/
    public void Var004()
    {
        try
        {
            String[] names1 =
            {
                "current",
                "destroyed",
                "renewable",
                "timed",
                "timeToExpiration",
                "system",
                "principal"
            };
            PropertyDescriptor[] descs1 = new ProfileHandleCredentialBeanInfo().getAdditionalBeanInfo()[0].getPropertyDescriptors();
            String[] names2 =
            {
                "handle"
            };
            PropertyDescriptor[] descs2 = new ProfileHandleCredentialBeanInfo().getPropertyDescriptors();

            assertCondition(SecAuthTest.verifyDescriptors(names1, descs1) && SecAuthTest.verifyDescriptors(names2, descs2), "Expected property not found.");
        }
        catch (Throwable e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    // Test using native method to do a swap 
    /* Create profile handle with additional authentication information. 
     * This method is only available after IBM 7.5. 
     * This profile handle should be released when the AS400 object is done with it.  */
    
   
    /* Swap to the user associated with swapToPH.  The swapFromPH is the handle for the original user, 
     * which is used with swapping back using swapBackAndReleaseNative.
     * This method is only available after IBM 7.5. 
      */ 
    // static native void swapToProfileHandleNative(byte[] swapToPH, byte[] swapFromPH) throws NativeException;

    /* Swap back to the original profile handle and free it.. This method is only available after IBM 7.5.  */ 
    // static native void swapBackAndReleaseNative(byte[] swapFromPH) throws NativeException;

  
    /* Release the originally allocated handle.  This method is only available after IBM 7.5.   */ 
    // static native void releaseProfileHandleNative(byte[] profileHandle) throws NativeException;

  

    public void Var005() {
      if (checkNative() && check750plus()) {

        StringBuffer sb = new StringBuffer();
        boolean passed = true;
        try {
          byte[] profileHandle = new byte[12]; 
          byte[] originalProfileHandle = new byte[12]; 
          
          String jobname = JDJobName.getJobName();
          sb.append("Current job is "+jobname+"\n"); 

          AS400JDBCDriver driver = new AS400JDBCDriver(); 
          Connection connection = driver.connect(pwrSys_);
          Statement s = connection.createStatement(); 
          String query = "select V_AUTHORIZATION_NAME from table(qsys2.get_JOB_INFO(V_JOB_NAME=>'"+jobname+"'))";
          sb.append("running query "+query+"\n"); 
          ResultSet rs = s.executeQuery(query);
          rs.next(); 
          String originalUser = rs.getString(1); 
          rs.close(); 
          
          sb.append("Attempting to create profile handle\n"); 
         // static native void createProfileHandle2Native(byte[] profileHandle, 
         //     String userId, char[] password, char[] additionalAuthenticationFactor,
         //     String verificationId, String remoteIpAddress, int jRemotePort,   String localIpAddress, int jLocalPort ) throws NativeException;
          Class<?>[] argTypes = new Class[9];
          Object[] args = new Object[9]; 
          args[0] = profileHandle; argTypes[0] = args[0].getClass();           /* profile handle */
          args[1] = testUser;      argTypes[1] = args[1].getClass();           /* userid */
          args[2] = testPass;      argTypes[2] = args[2].getClass();           /* password */ 
          args[3] = null;          argTypes[3] = args[2].getClass();           /* AAF */ 
          args[4] = null;          argTypes[4] = args[1].getClass();           /* verification id */ 
          args[5] = null ;          argTypes[5] = args[1].getClass();           /* Remote id */ 
          args[6] = new Integer(0);  argTypes[6] = Integer.TYPE;               /* Remote port */  
          args[7] = null ;          argTypes[7] = args[1].getClass();           /* local id */ 
          args[8] = new Integer(0);  argTypes[8] = Integer.TYPE;               /* local port */  
          
          JDReflectionUtil.callStaticMethod_V("com.ibm.as400.access.AS400ImplNative","createProfileHandle2Native", argTypes, args);
         
          sb.append("profile handle created, attempting to swap to it\n"); 

          argTypes = new Class[2]; 
          args = new Object[2]; 
          args[0] = profileHandle; argTypes[0] = args[0].getClass();  
          args[1] = originalProfileHandle; argTypes[1] = args[1].getClass();  
          
          // static native void swapToProfileHandleNative(byte[] swapToPH, byte[] swapFromPH) throws NativeException;
          JDReflectionUtil.callStaticMethod_V("com.ibm.as400.access.AS400ImplNative",
              "swapToProfileHandleNative", argTypes, args);
          
          sb.append("swap completed -- checking id\n"); 
          query = "select V_AUTHORIZATION_NAME from table(qsys2.get_JOB_INFO(V_JOB_NAME=>'"+jobname+"'))";
          sb.append("running query "+query+"\n"); 
          rs = s.executeQuery(query);
          rs.next(); 
          String swappedUser = rs.getString(1); 
          rs.close(); 
          if (!testUser.equals(swappedUser)) { 
            passed = false;
            sb.append("FAILED:   testUser="+testUser+" swapped user is "+swappedUser+"\n"); 
          }
          
          sb.append("swapping back to original and destroying temp handle\n "); 
          
          argTypes = new Class[1]; 
          args = new Object[1]; 
          args[0]=originalProfileHandle; argTypes[0] = originalProfileHandle.getClass(); 
          JDReflectionUtil.callStaticMethod_V("com.ibm.as400.access.AS400ImplNative",
              "swapBackAndReleaseNative", argTypes, args);

          
          query = "select V_AUTHORIZATION_NAME from table(qsys2.get_JOB_INFO(V_JOB_NAME=>'"+jobname+"'))";
          sb.append("running query "+query+"\n"); 
          rs = s.executeQuery(query);
          rs.next(); 
          String afterSwapUser = rs.getString(1); 
          rs.close(); 
          if (!originalUser.equals(afterSwapUser)) { 
            passed = false;
            sb.append("FAILED:   originalUser="+originalUser+" after swapbackuser is "+afterSwapUser+"\n"); 
          }

          
          
          sb.append("releasing swap to handle\n"); 
          // static native void releaseProfileHandleNative(byte[] profileHandle) throws NativeException;
          args[0] = profileHandle; 
          JDReflectionUtil.callStaticMethod_V("com.ibm.as400.access.AS400ImplNative",
              "releaseProfileHandleNative", argTypes, args);

          connection.close(); 
          assertCondition(passed, sb);
        } catch (Throwable e) {
          failed(e, sb);
        }
      }
    }
  }
