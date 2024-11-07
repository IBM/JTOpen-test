///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SecPTActionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.beans.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ExtendedIllegalArgumentException;   
import com.ibm.as400.access.Job;
import com.ibm.as400.security.auth.*;

import test.PasswordVault;
import test.SecAuthTest;
import test.Testcase;

/**
 Testcase SecPTActionTestcase.
 <p>Test variations for the following actions:
 <ul>
 <li>refresh
 <li>startAutomaticRefresh
 <li>stopAutomaticRefresh
 <li>swap
 <li>swapNoException
 <li>destroy
 </ul>
 <p>Test variations for the following related events:
 <ul>
 <li>created
 <li>refreshed
 <li>swapped
 <li>destroyed
 </ul>
 **/
public class SecPTActionTestcase extends Testcase implements AS400CredentialListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "SecPTActionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.SecAuthTest.main(newArgs); 
   }
    private static final boolean DEBUG = false;
    private static int refreshCount_ = 0;
    private static AS400CredentialEvent latestEvent_ = null;

    AS400 nativeSystemObject = null;
    private boolean profileHandleImplNativeAvailable = false; 

    /**
     Invoked when a create has been performed.
     @param  event  The credential event.
     **/
    public void created(AS400CredentialEvent event)
    {
        latestEvent_ = event;
    }

    /**
     Invoked when a destroy has been performed.
     @param  event  The credential event.
     **/
    public void destroyed(AS400CredentialEvent event)
    {
        latestEvent_ = event;
    }

    /**
     Invoked when a refresh has been performed.
     @param  event  The credential event.
     **/
    public void refreshed(AS400CredentialEvent event)
    {
        latestEvent_ = event;
        refreshCount_++;
    }

    /**
     Reset all internally maintained state indicators.
     **/
    private void resetState()
    {
        latestEvent_ = null;
        refreshCount_ = 0;
    }

    /**
     Invoked when a credential has been used to change the OS/400 thread identity.
     @param  event  The credential event.
     **/
    public void swapped(AS400CredentialEvent event)
    {
        latestEvent_ = event;
    }


    public void setup() throws Exception {


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
	SecAuthTest.createProfiles(pwrSys_); 
    } 

    public void cleanup() throws Exception {
	SecAuthTest.deleteProfiles(pwrSys_); 
    } 
    /**
     Test ignored refresh of a non-renewable token.
     **/
    public void Var001()
    {
	String uid = null;
	String pwd = null; 
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
	    uid = SecAuthTest.uid1;
	    pwd = SecAuthTest.pwd1; 
            pt.setTokenExtended(uid, pwd.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            int t = pt.getTimeToExpiration();
            Thread.sleep(2000);
            pt.refresh();
            assertCondition(pt.getTimeToExpiration() < t && latestEvent_ == null, "Time to expiration not decreased.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception. uid="+uid+" pwd="+pwd);
        }
    }

    /**
     Test successful refresh of a multi-use renewable token.
     **/
    public void Var002()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            Thread.sleep(5000);
            int t = pt.getTimeToExpiration();
            pt.refresh();
            assertCondition(pt.getTimeToExpiration() > t && latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_REFRESH, "Refresh not successful.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful refresh of a multi-use renewable token to single-use token with different timeout.
     **/
    public void Var003()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTimeoutInterval(100);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.refresh(ProfileTokenCredential.TYPE_SINGLE_USE, 3600);
            assertCondition(pt.getTimeToExpiration() > 3000 && pt.getTokenType() == ProfileTokenCredential.TYPE_SINGLE_USE && latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_REFRESH, "Refresh not successful.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range token type for refresh (low end).
     **/
    public void Var004()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.refresh(0, 3600);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range token type for refresh (high end).
     **/
    public void Var005()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.refresh(4, 3600);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range timeout for refresh (low end).
     **/
    public void Var006()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.refresh(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 0);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range timeout for refresh (high end).
     **/
    public void Var007()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.refresh(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE, 3601);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed refresh of a token already removed from the system.
     **/
    public void Var008()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            try
            {
                pt.refresh();
                failed("Expected exception not signalled.");
            }
            catch (RefreshFailedException rfe)
            {
                assertCondition(latestEvent_ == null && rfe.getAS400Message().getID().equals("CPF2274"), "Expected message not returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed autorefresh of a non-renewable token.
     **/
    public void Var009()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.startAutomaticRefresh(60, 3);
                failed("Expected exception not signalled.");
            }
            catch (IllegalStateException ise)
            {
                assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_NOT_VALID && latestEvent_ == null && refreshCount_ == 0, "Unexpected refresh event signalled.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful autorefresh specifying a finite number of refresh attempts for a renewable token; automatic stop.
     **/
    public void Var010()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTimeoutInterval(60);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.startAutomaticRefresh(1, 3);
            Thread.sleep(10000);
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_STOPPED && latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_REFRESH && refreshCount_ == 3, "Refresh not successful.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed autorefresh of a token previously removed from the system.
     **/
    public void Var011()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            pt.startAutomaticRefresh(1, 3);
            Thread.sleep(10000);
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_FAILED && pt.getAutomaticRefreshFailure() != null, "Auto refresh did not fail as expected.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test autorefresh stop when system services are disconnected.
     **/
    public void Var012()
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
            // Connect a service.
            sys.connectService(AS400.COMMAND);
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(sys);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.startAutomaticRefresh(60, -1);
            Thread.sleep(5000);
            sys.disconnectAllServices();
            Thread.sleep(5000);
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_STOPPED, "Auto refresh not stopped as expected.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            sys.disconnectAllServices();
        }
    }

    /**
     Test autorefresh explicit stop.
     **/
    public void Var013()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.addCredentialListener(this);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.startAutomaticRefresh(60, -1);
            Thread.sleep(5000);
            pt.stopAutomaticRefresh();
            assertCondition(pt.getAutomaticRefreshStatus() == AS400Credential.CR_AUTO_REFRESH_STOPPED, "Auto refresh not stopped as expected.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range maxRefreshes for autorefresh (low end).
     **/
    public void Var014()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.startAutomaticRefresh(60, -2);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test out of range interval for autorefresh (low end).
     **/
    public void Var015()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            try
            {
                pt.startAutomaticRefresh(0, -1);
                failed("Expected exception not signalled.");
            }
            catch (ExtendedIllegalArgumentException iae)
            {
                assertCondition(iae.getReturnCode() == ExtendedIllegalArgumentException.RANGE_NOT_VALID, "Unexpected return code >> " + iae.getReturnCode());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test ignored stop of autorefresh for a non-renewable token.
     **/
    public void Var016()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.stopAutomaticRefresh();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test ignored stop of autorefresh for a renewable token for which autorefresh is not in progress.
     **/
    public void Var017()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.stopAutomaticRefresh();
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap if running on the local host; otherwise verify the exception.  Use a return credential to swap back to the original identity.
     **/
    public void Var018()
    {
        try
        {
          
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
	    boolean canUseNativeOptimizations; 
	    if (isLocal_) { 
        if (sameSys_ == null) {
          failed("sameSys_ = null, current user not same and passed user"); 
          return; 
        }
		pt.setSystem(sameSys_);
		canUseNativeOptimizations = sameSys_.canUseNativeOptimizations();  
	    } else {
		pt.setSystem(systemObject_);
		canUseNativeOptimizations = systemObject_.canUseNativeOptimizations();
	    }
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());

            // Reset internal state.
            resetState();

            // Perform test.
            try
            {
                String originalID = null;
                String swapUid = null;
                String swappedBackUid = null;
                int swapEvt = 0;
                AS400 swappedConnection = null;
		
                if (canUseNativeOptimizations)
                {
                  originalID = new AS400().getUserId();
                  // Swap.
                  AS400Credential cr = pt.swap(true);
                  swappedConnection = new AS400();

                  if (DEBUG)
                  {
                    System.out.println("userID: " + swappedConnection.getUserId());
                    CommandCall cmd = new CommandCall(swappedConnection);
                    cmd.setThreadSafe(true);
                    Job job = cmd.getServerJob();
                    System.out.println("Server job: " + job.toString());
                  }

                  swapUid = swappedConnection.getUserId();
                  swapEvt = latestEvent_ == null ? 0 : latestEvent_.getID();
                  // Swap back.
                  resetState();
                  cr.addCredentialListener(this);
                  cr.swap(false);
                  swappedBackUid = swappedConnection.getUserId(true); // force refresh of userID info
                }

                else  // running remotely
                {
                  // First create a swap-back credential.
                  ProfileTokenCredential cr = new ProfileTokenCredential();
		  if (isLocal_) { 
		      cr.setSystem(sameSys_);
		  } else {
		      cr.setSystem(systemObject_);
		  } 
                  cr.addCredentialListener(this);
                  cr.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
                   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
                  cr.setTokenExtended(userId_, charPassword);
                  PasswordVault.clearPassword(charPassword);
		  if (isLocal_) { 
		      swappedConnection = new AS400(sameSys_);
		  } else {
		      swappedConnection = new AS400(systemObject_);
		  } 
                  originalID = swappedConnection.getUserId();
                  Swapper.swap(swappedConnection, pt);  // swap to SecAuthTest.uid2

                  // CommandCall cmd = new CommandCall(swappedConnection);
                  //cmd.setThreadSafe(true);  // threadsafety is irrelevant when running remotely

                  Job[] jobs = swappedConnection.getJobs(AS400.COMMAND);
                  if (jobs != null && jobs.length != 0) {
                    swapUid = jobs[0].getStringValue(Job.CURRENT_USER);
                  }
                  swapEvt = latestEvent_ == null ? 0 : latestEvent_.getID();
                  // Swap back.
                  resetState();
                  Swapper.swap(swappedConnection, cr);
                  jobs = swappedConnection.getJobs(AS400.COMMAND);
                  if (jobs != null && jobs.length != 0) {
                    swappedBackUid = jobs[0].getStringValue(Job.CURRENT_USER);
                  }
                }

                // Test results.
                if (DEBUG) {
                  System.out.println("originalID == |" + originalID + "|; SecAuthTest.uid2 == |" + SecAuthTest.uid2 + "|; swapUid == |" + swapUid + "|; swappedBackUid == |" + swappedBackUid + "|; swapEvt == " + swapEvt);
                }
                if (SecAuthTest.uid2.equalsIgnoreCase(swapUid) && swapEvt == AS400CredentialEvent.CR_SWAP)
                {
                  assertCondition(originalID.equalsIgnoreCase(swappedBackUid) && latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_SWAP, "Swap back failed");
                }
                else
                {
                  failed("Swap failed.");
                }
            }
            catch (Exception e)
            {
              failed(e, "Unexpected exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap (generic/no parms) if running on the local host; otherwise verify the return value.
     **/
    public void Var019()
    {
        try
        {
            // Create objects.
            ProfileHandleCredential ph = new ProfileHandleCredential();
	    if (isLocal_) { 
        if (sameSys_ == null) {
          failed("sameSys_ = null, current user not same and passed user"); 
        }
		ph.setSystem(sameSys_);
	    } else {
		ph.setSystem(systemObject_);
	    } 
            ProfileTokenCredential pt = new ProfileTokenCredential();
	    if (isLocal_) { 
		pt.setSystem(sameSys_);
	    } else {
		pt.setSystem(systemObject_);
	    } 
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());

            // Reset internal state.
            resetState();

            // Perform test.
            try
            {
                if (isNative_ && profileHandleImplNativeAvailable) ph.setHandle();
                // Swap.
                pt.swap();
                String swapUid = new AS400().getUserId();
                int swapEvt = latestEvent_ == null ? 0 : latestEvent_.getID();
                // Swap back.
                if (isNative_ && profileHandleImplNativeAvailable)
                {
                    ph.swap();
                }
                // Test results.
                assertCondition(SecAuthTest.uid1.equals(swapUid) && swapEvt == AS400CredentialEvent.CR_SWAP, "Unexpected result.");
            }
            catch (SwapFailedException sfe)
            {
                assertCondition(!isNative_ || !profileHandleImplNativeAvailable, "Unexpected swap exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed swap for a token that was previously removed from the system if running on the local host; otherwise verify the exception.
     **/
    public void Var020()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
	    if (isLocal_) { 
        if (sameSys_ == null) {
          failed("sameSys_ = null, current user not same and passed user"); 
          return; 
        }
		pt.setSystem(sameSys_);
	    } else {
		pt.setSystem(systemObject_);
	    }
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());

            // Reset internal state.
            resetState();

            // Perform test.
            String originalID = null;
            if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            try
            {
                pt.swap(false);
                failed("Expected exception not signalled.");
            }
            catch (SwapFailedException sfe)
            {
                if ((!isNative_ || !profileHandleImplNativeAvailable) && sfe.getReturnCode() == AS400SecurityException.REQUEST_NOT_SUPPORTED)
                {
                    succeeded();
                }
                else
                {
                    assertCondition((originalID != null) && originalID.equals(new AS400().getUserId()) && latestEvent_ == null && sfe.getAS400Message() != null && sfe.getAS400Message().getID().equals("CPF2274"), "Expected message not returned.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed swap (generic/no parms) for a token that was previously removed from the system.
     **/
    public void Var021()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
	    if (isLocal_) { 
        if (sameSys_ == null) {
          failed("sameSys_ = null, current user not same and passed user"); 
          return; 
        }
		pt.setSystem(sameSys_);
	    } else {
		pt.setSystem(systemObject_);
	    } 
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());

            // Reset internal state.
            resetState();

            // Perform test.
            String originalID = null;
            if (isNative_ && profileHandleImplNativeAvailable) originalID = new AS400().getUserId();
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            try
            {
                pt.swap();
            }
            catch (SwapFailedException sfe)
            {
                assertCondition((!isNative_ || !profileHandleImplNativeAvailable || ((originalID != null) && originalID.equals(new AS400().getUserId()))) && latestEvent_ == null, "Unexpected swap exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful swap if running on the local host; otherwise not applicable.  Specify the host as "localhost" instead of by name.
     **/
    public void Var022()
    {
        try
        {
            // Check if system where test profiles were created is local.
            if (!isLocal_ || !profileHandleImplNativeAvailable)
            {
                notApplicable("local jt400native.jar testcase");
                return;
            }
            // Create objects.
            AS400 sys = new AS400("localhost", "*CURRENT", "*CURRENT".toCharArray());
            ProfileHandleCredential ph = new ProfileHandleCredential();
            ph.setSystem(sys);
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(sys);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);

            ph.setHandle();
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());

            // Reset internal state.
            resetState();

            // Perform test.
            try
            {
                // Swap.
                AS400Credential cr = pt.swap(false);
                String swap1uid = new AS400().getUserId();
                int swap1evt = latestEvent_ == null ? 0 : latestEvent_.getID();
                // Swap back.
                ph.swap();
                // Test results.
                assertCondition(SecAuthTest.uid2.equals(swap1uid) && swap1evt == AS400CredentialEvent.CR_SWAP && cr == null, "Unexpected result.");
            }
            catch (SwapFailedException sfe)
            {
                failed(sfe, "Unexpected swap exception.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful destroy/remove for an existing token.
     **/
    public void Var023()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_NON_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.destroy();
            assertCondition(pt.isDestroyed() && latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_DESTROY, "Unexpected failure.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed destroy/remove for a token previously removed from the system.
     **/
    public void Var024()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenExtended(SecAuthTest.uid2, SecAuthTest.pwd2.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            SecAuthTest.removeToken(pt.getSystem(), pt.getToken());
            try
            {
                pt.destroy();
                failed("Expected exception not signalled.");
            }
            catch (DestroyFailedException dfe)
            {
                assertCondition(latestEvent_ == null && dfe.getAS400Message() != null && dfe.getAS400Message().getID().equals("CPF2274"), "Unexpected failure.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful destroy/remove for a non-initialized (already destroyed) token.
     **/
    public void Var025()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.addCredentialListener(this);
            // Reset internal state.
            resetState();
            // Perform test.
            pt.destroy();
            assertCondition(latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_DESTROY, "Expected event not signalled.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test failed autorefresh of a token for which autorefresh is already started.
     **/
    public void Var026()
    {
        try
        {
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Reset internal state.
            resetState();
            // Perform test.
            pt.startAutomaticRefresh(60, 2);
            try
            {
                pt.startAutomaticRefresh(60, 1);
                failed("AutoRefresh did not fail as expected, current status is >> " + pt.getAutomaticRefreshStatus());
            }
            catch (IllegalStateException ise)
            {
                succeeded();
            }
            // Cleanup.
            pt.stopAutomaticRefresh();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test successful create & corresponding event.
     **/
    public void Var027()
    {
        try
        {
            // Reset internal state.
            resetState();
            // Create objects.
            ProfileTokenCredential pt = new ProfileTokenCredential();
            pt.setSystem(systemObject_);
            pt.addCredentialListener(this);
            pt.setTokenType(ProfileTokenCredential.TYPE_MULTIPLE_USE_RENEWABLE);
            // Perform test.
            pt.setTokenExtended(SecAuthTest.uid1, SecAuthTest.pwd1.toCharArray());
            // Perform test.
            assertCondition(latestEvent_ != null && latestEvent_.getID() == AS400CredentialEvent.CR_CREATE, "Expected event not signalled.");
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test addition of null property change listener.
     **/
    public void Var028()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            PropertyChangeListener l = null;
            try
            {
                pt.addPropertyChangeListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null property change listener.
     **/
    public void Var029()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            PropertyChangeListener l = null;
            pt.removePropertyChangeListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test additional of null vetoable change listener.
     **/
    public void Var030()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            VetoableChangeListener l = null;
            try
            {
                pt.addVetoableChangeListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null vetoable change listener.
     **/
    public void Var031()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            VetoableChangeListener l = null;
            pt.removeVetoableChangeListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test addition of null credential listener.
     **/
    public void Var032()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            AS400CredentialListener l = null;
            try
            {
                pt.addCredentialListener(l);
                failed("Expected exception not signalled.");
            }
            catch (NullPointerException npe)
            {
                succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Test removal of null credential listener.
     **/
    public void Var033()
    {
        try
        {
            ProfileTokenCredential pt = new ProfileTokenCredential();
            AS400CredentialListener l = null;
            pt.removeCredentialListener(l);
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
