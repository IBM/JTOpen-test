///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CHTEntryTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ClusteredHashTable;
import com.ibm.as400.access.ClusteredHashTableEntry;

import test.CHTTest;
import test.Testcase;

/**
 The CHTEntryTestcase class tests all the methods of the ClusteredHashTable class.
 **/
public class CHTEntryTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CHTEntryTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CHTTest.main(newArgs); 
   }

    /**
     Constructor.
     **/
    public CHTEntryTestcase(AS400 systemObject, 
			    Hashtable<String, Vector<String>> namesAndVars, 
			    int runMode, 
			    FileOutputStream fileOutputStream)
    {
        super(systemObject, "CHTEntryTestcase", namesAndVars, runMode, fileOutputStream);
    }

  class CHTPropertyChangeListener implements PropertyChangeListener
  {
    public String propertyName = "";
    public Object oldValue;
    public Object newValue;

    public void propertyChange(PropertyChangeEvent event)
    {
      propertyName = event.getPropertyName();
      oldValue = event.getOldValue();
      newValue = event.getNewValue();
    }

    public void reset()
    {
      propertyName = "";
      oldValue = null;
      newValue = null;
    }
  }

    /**
     Default ClusteredHashTableEntry constructor.  Verify that getKey returns null since not set.
     **/
    public void Var001()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getKey() == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass null for key and expect an exception.
     **/
    public void Var002()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = null;
	    String myData = new String("This is my data");
	    int ttl = 2400;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass null for data and expect an exception.
     **/
    public void Var003()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    byte[] myData = null;
	    int ttl = 2400;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    myEntry = new ClusteredHashTableEntry(key, myData, ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass data with length of 0 and expect an exception.
     **/
    public void Var004()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("");
	    int ttl = 2400;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass time to live of 0 and expect an exception.
     **/
    public void Var005()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("This is my data.");
	    int ttl = 0;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass invalid authority and expect an exception.
     **/
    public void Var006()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("This is my data.");
	    int ttl = 2400;
	    int auth = 2;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass invalid update option and expect an exception.
     **/
    public void Var007()
    {
        try
        {
	    ClusteredHashTableEntry myEntry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("This is my data.");
	    int ttl = 2400;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = 2;
	    myEntry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    failed("Did not throw exception."+myEntry);
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     ClusteredHashTableEntry(key, ...) constructor.  Pass valid parameters and expect no exception.  Verify that key is set correctly.
     **/
    public void Var008()
    {
        try
        {
	    ClusteredHashTableEntry entry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("This is my data.");
	    int ttl = 2400;
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_FAIL;
	    entry = new ClusteredHashTableEntry(key, myData.getBytes(), ttl, auth, upd);
	    byte[] rtnKey = entry.getKey();
	    boolean same = true;
	    for (int i=0; i<16; ++i)
	    {
		if (rtnKey[i] != key[i])
		    same = false;
	    }
            assertCondition((same == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     addPropertyChangeListener method.  Pass null for listener and expect an exception.
     **/
    public void Var009()
    {
        try
        {
	    PropertyChangeListener listener = null;
	    ClusteredHashTableEntry entry = null;
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    String myData = new String("This is my data");
	    entry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    entry.addPropertyChangeListener(listener);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     addPropertyChangeListener method.  Ensure that an event is received when the system property is changed.
     **/
    public void Var010()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            CHTPropertyChangeListener listener = new CHTPropertyChangeListener();
	    entry.addPropertyChangeListener(listener);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x02;
	    entry.setKey(key);
	    assertCondition(listener.propertyName.equals("key"));
        }
        catch (Exception e) 
        {
	    failed (e, "Unexpected Exception");
        }
    }

    /**
     removePropertyChangeListener method.  Pass null for listener and expect an exception.
     **/
    public void Var011()
    {
        try
        {
	    PropertyChangeListener listener = null;
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    entry.removePropertyChangeListener(listener);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     removePropertyChangeListener method.  Ensure that events are no longer received.
     **/
    public void Var012()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            CHTPropertyChangeListener listener = new CHTPropertyChangeListener();
	    entry.addPropertyChangeListener(listener);
	    entry.removePropertyChangeListener(listener);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x02;
	    entry.setKey(key);
            assertCondition (listener.propertyName.equals(""));
        }
        catch (Exception e) 
        {
	    failed (e, "Unexpected Exception");
        }
    }

    /**
     getEntryAuthority method.  The entry authority field has not been set so the default value will be returned.  No exception is expected.  Verify that the status returned is correct.
     **/
    public void Var013()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getEntryAuthority() == ClusteredHashTableEntry.ENTRY_AUTHORITY_LAST_USER));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setEntryAuthority method.  The authority passed in is invalid and an exception is expected.
     **/
    public void Var014()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    entry.setEntryAuthority(-1);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

     /**
     get/setEntryAuthority methods.  The entry authority is successfully set and retrieved.  No exception is expected.  Verify that the authority returned is correct.
     **/
    public void Var015()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    int auth = ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER;
	    entry.setEntryAuthority(auth);
            assertCondition((entry.getEntryAuthority() == auth));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getKey method.  The key field has not been set so null will be returned.  No exception is expected.  Verify that the key returned is null.
     **/
    public void Var016()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getKey() == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setKey method.  The key passed in is null and an exception is expected.
     **/
    public void Var017()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    byte[] key = null;
	    entry.setKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

     /**
     get/setKey methods.  The key is successfully set and retrieved.  No exception is expected.  Verify that the key returned is correct.
     **/
    public void Var018()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x02;
	    entry.setKey(key);
	    byte[] rtnKey = entry.getKey();
	    boolean same = true;
	    for (int i=0; i<16; ++i)
	    {
		if (rtnKey[i] != key[i])
		    same = false;
	    }
            assertCondition((same == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getTimeToLive method.  The time to live field has not been set so the default value will be returned.  No exception is expected.  Verify that the key returned is correct.
     **/
    public void Var019()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getTimeToLive() == 60));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setTimeToLive method.  The time to live passed in is invalid and an exception is expected.
     **/
    public void Var020()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    entry.setTimeToLive(0);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

     /**
     get/setTimeToLive methods.  The time to live is successfully set and retrieved.  No exception is expected.  Verify that the time to live returned is correct.
     **/
    public void Var021()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    int ttl = 64;
	    entry.setTimeToLive(ttl);
            assertCondition((entry.getTimeToLive() == ttl));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getUpdateOption method.  The update option field has not been set so the default value will be returned.  No exception is expected.  Verify that the update option returned is correct.
     **/
    public void Var022()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getUpdateOption() == ClusteredHashTableEntry.DUPLICATE_KEY_FAIL));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setUpdateOption method.  The update option passed in is invalid and an exception is expected.
     **/
    public void Var023()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    entry.setUpdateOption(-1);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

     /**
     get/setUpdateOption methods.  The update option is successfully set and retrieved.  No exception is expected.  Verify that the update option returned is correct.
     **/
    public void Var024()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    int upd = ClusteredHashTableEntry.DUPLICATE_KEY_UPDATE;
	    entry.setUpdateOption(upd);
            assertCondition((entry.getUpdateOption() == upd));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getUserData method.  The user data field has not been set so null will be returned.  No exception is expected.  Verify that the update option returned is null.
     **/
    public void Var025()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getUserData() == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setUserData method.  The user data passed in is null and an exception is expected.
     **/
    public void Var026()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    byte[] data = null;
	    // Changed 3/3/2010 to pass null data to method
            // Before that the test was verifying that data.getBytes() was
	    // thowing an exception
	    entry.setUserData(data);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setUserData method.  The length of the user data passed in is invalid and an exception is expected.
     **/
    public void Var027()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    String data = "";
	    entry.setUserData(data.getBytes());
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

     /**
     get/setUserData methods.  The user data is successfully set and retrieved.  No exception is expected.  Verify that the user data returned is correct.
     **/
    public void Var028()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
	    String data = "This data must be correct.";
	    entry.setUserData(data.getBytes());
	    String newData = new String(entry.getUserData());
            assertCondition((newData.equals(data) == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getEntryStatus method.  The entry status field has not been set so the default value will be returned.  No exception is expected.  Verify that the status returned is correct.
     **/
    public void Var029()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getEntryStatus() == ClusteredHashTableEntry.ENTRY_STATUS_CONSISTENT));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getEntryStatus method.  The entry status is set by retrieving an entry from the CHT.  No exception is expected.  Verify that the status returned is correct.
     **/
    public void Var030()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    ClusteredHashTableEntry results = cht.get(key);
            assertCondition((results.getEntryStatus() == ClusteredHashTableEntry.ENTRY_STATUS_CONSISTENT));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getOwnerProfile method.  The owner profile field has not been set so null will be returned.  No exception is expected.  Verify that the profile returned is null.
     **/
    public void Var031()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getOwnerProfile() == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getOwnerProfile method.  The owner profile is set by retrieving an entry from the CHT.  No exception is expected.  Verify that the profile returned is not null.
     **/
    public void Var032()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    ClusteredHashTableEntry results = cht.get(key);
            assertCondition((results.getOwnerProfile() != null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getModifiedProfile method.  The modified profile field has not been set so null will be returned.  No exception is expected.  Verify that the profile returned is null.
     **/
    public void Var033()
    {
        try
        {
	    ClusteredHashTableEntry entry = new ClusteredHashTableEntry();
            assertCondition((entry.getModifiedProfile() == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getModifiedProfile method.  The modified profile is set by retrieving an entry from the CHT.  No exception is expected.  Verify that the profile returned is not null.
     **/
    public void Var034()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    ClusteredHashTableEntry results = cht.get(key);
            assertCondition((results.getModifiedProfile() != null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

}
