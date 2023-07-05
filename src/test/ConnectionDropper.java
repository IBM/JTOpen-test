///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConnectionDropper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

/**
 Helper class for testing the effect of communications errors which cause the ConnectionDroppedException during various operations.
 **/
public class ConnectionDropper extends Thread
{
    AS400 system_;
    int service_;
    int wait_;

    public ConnectionDropper(AS400 system, int service, int wait)
    {
        super();
        system_ = system;
        service_ = service;
        wait_ = wait;
    }

    public void run()
    {
        synchronized (this)
        {
            try
            {
                wait(wait_);
            }
            catch (InterruptedException e)
            {
            }
        }
	// System.out.println("ConnectionDropper:	Dropping service "+service_); 
        system_.disconnectService(service_);
	// System.out.println("ConnectionDropper:  Service dropped  "+service_); 
    }
}
