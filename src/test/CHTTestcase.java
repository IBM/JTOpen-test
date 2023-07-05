///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CHTTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import com.ibm.as400.access.*;

/**
 The CHTTestcase class tests all the methods of the ClusteredHashTable class:
 &lt;li&gt;open,
 &lt;li&gt;others.
 **/
public class CHTTestcase extends Testcase
{

    /**
     Constructor.
     **/
    public CHTTestcase(AS400 systemObject, 
		       Hashtable namesAndVars, 
		       int runMode, 
		       FileOutputStream fileOutputStream)
    {
        super(systemObject, "CHTTestcase", namesAndVars, runMode, fileOutputStream);
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
     Default ClusteredHashTable constructor.  Verify that getSystem and getName return null since not set.
     **/
    public void Var001()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
            assertCondition((cht.getSystem() == null) && (cht.getName() == null) );
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     ClusteredHashTable(AS400, String) constructor.  Pass null for system and expect an exception.
     **/
    public void Var002()
    {
        try
        {
	    AS400 sys = null;
	    String name = CHTTest.chtSvrName_;
	    ClusteredHashTable cht = new ClusteredHashTable(sys, name);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     ClusteredHashTable(AS400, String) constructor.  Pass null for name and expect an exception.
     **/
    public void Var003()
    {
        try
        {
	    AS400 sys = CHTTest.pwrSys_;
	    String name = null;
	    ClusteredHashTable cht = new ClusteredHashTable(sys, name);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     ClusteredHashTable(AS400, String) constructor.  Pass name greater than 10 characters and expect an exception.
     **/
    public void Var004()
    {
        try
        {
	    AS400 sys = CHTTest.pwrSys_;
	    String name = "VERYLONGNAME";
	    ClusteredHashTable cht = new ClusteredHashTable(sys, name);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     ClusteredHashTable(AS400, String) constructor.  Pass in valid parameters and verify that getSystem and getName return correct values.
     **/
    public void Var005()
    {
        try
        {
	    AS400 sys = CHTTest.pwrSys_;
	    String name = CHTTest.chtSvrName_;
	    ClusteredHashTable cht = new ClusteredHashTable(sys, name);
            assertCondition((cht.getSystem() == sys) && (cht.getName() == name) );
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     addPropertyChangeListener method.  Pass null for listener and expect an exception.
     **/
    public void Var006()
    {
        try
        {
	    PropertyChangeListener listener = null;
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.addPropertyChangeListener(listener);
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
    public void Var007()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String newName = "NEWNAME";
            CHTPropertyChangeListener listener = new CHTPropertyChangeListener();
	    cht.addPropertyChangeListener(listener);
	    cht.setName(newName);
	    assertCondition(listener.propertyName.equals("name") &&
			    listener.oldValue.equals(CHTTest.chtSvrName_) &&
			    listener.newValue.equals(newName));
        }
        catch (Exception e) 
        {
	    failed (e, "Unexpected Exception");
        }
    }

    /**
     close method.  No open has been done, no errors are expected.
     **/
    public void Var008()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.close();
	    succeeded();
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     close method.  Open has been done, but CHT was then deleted via the ENDCHTSVR command.  Expect an exception.
     **/
    public void Var009()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    cht.close();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     close method.  Open has been done, expect no exceptions.
     **/
    public void Var010()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    cht.close();
	    succeeded();
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     containsKey method.  Pass null for key parameter and expect an exception.
     **/
    public void Var011()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = null;
	    boolean rc;
	    rc = cht.containsKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     containsKey method.  System has not been set, so expect an exception.
     **/
    public void Var012()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    boolean rc;
	    rc = cht.containsKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     containsKey method.  Name has not been set, so expect an exception.
     **/
    public void Var013()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    boolean rc;
	    rc = cht.containsKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     containsKey method.  Server is not yet open.  Name has been set to an invalid CHT server so open will fail.  Expect an exception.
     **/
    public void Var014()
    {
        try
        {
	    String name = "WRONG";
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, name);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    boolean rc;
	    rc = cht.containsKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     containsKey method.  Server is not yet open.  The key does not exist.  Expect method to return False.
     **/
    public void Var015()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    boolean rc;
	    rc = cht.containsKey(key);
            assertCondition((rc == false));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     containsKey method.  Server is open but ENDCHTSVR command was run.  The get will fail and we expect an exception.
     **/
    public void Var016()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    boolean rc;
	    rc = cht.containsKey(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     containsKey method.  The key exists in the table and True will be returned from the method.
     **/
    public void Var017()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    boolean rc;
	    rc = cht.containsKey(key);
            assertCondition((rc == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     elements() method.  System has not been set and an exception is expected.
     **/
    public void Var018()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    ClusteredHashTableEntry[] list = cht.elements();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     elements() method.  The method works and no exception is expected.  Verify that list is not null since CHT should contain at least one entry.
     **/
    public void Var019()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    ClusteredHashTableEntry[] list = cht.elements();
            assertCondition((list != null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     elements method.  The userProfile parameter length is > 10 and an exception is expected.
     **/
    public void Var020()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String usrPrf = "TOOLONGUSER";
	    String lastMod = "LASTMOD";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     elements method.  The lastModifiedProfile parameter length is > 10 and an exception is expected.
     **/
    public void Var021()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String usrPrf = "USER";
	    String lastMod = "TOOLONGLASTMOD";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     elements method.  The status parameter is invalid and an exception is expected.
     **/
    public void Var022()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String usrPrf = "USER";
	    String lastMod = "LASTMOD";
	    int sts = 5;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     elements method.  The system has not been set and an exception is expected.
     **/
    public void Var023()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    String usrPrf = "USER";
	    String lastMod = "LASTMOD";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     elements method.  The name has not been set and an exception is expected.
     **/
    public void Var024()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    String usrPrf = "USER";
	    String lastMod = "LASTMOD";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     elements method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var025()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    String usrPrf = "USER";
	    String lastMod = "LASTMOD";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     elements method.  The server has not been opened.  No exception is expected.  Verify that list is null since server was just started.
     **/
    public void Var026()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String usrPrf = "*ALL";
	    String lastMod = "*ALL";
	    int sts = ClusteredHashTable.CONSISTENT_ENTRIES;
	    ClusteredHashTableEntry[] list = cht.elements(usrPrf, lastMod, sts);
            assertCondition((list.length == 0));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     generateKey method.  The system has not been set and an exception is expected.
     **/
    public void Var027()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     generateKey method.  The name has not been set and an exception is expected.
     **/
    public void Var028()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    byte[] key = cht.generateKey();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     generateKey method.  Server is not yet open.  Name has been set to an invalid CHT server so open will fail.  Expect an exception.
     **/
    public void Var029()
    {
        try
        {
	    String name = "WRONG";
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, name);
	    byte[] key = cht.generateKey();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     generateKey method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var030()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    byte[] key = cht.generateKey();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     generateKey method.  The server has not been opened.  No exception is expected.  Verify that key is not null.
     **/
    public void Var031()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
            assertCondition((key != null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     get method.  The key has not been set and an exception is expected.
     **/
    public void Var032()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = null;
	    ClusteredHashTableEntry results = cht.get(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     get method.  The system has not been set and an exception is expected.
     **/
    public void Var033()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    ClusteredHashTableEntry results = cht.get(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     get method.  The name has not been set and an exception is expected.
     **/
    public void Var034()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    ClusteredHashTableEntry results = cht.get(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     get method.  Server is not yet open.  Name has been set to an invalid CHT server so open will fail.  Expect an exception.
     **/
    public void Var035()
    {
        try
        {
	    String name = "WRONG";
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, name);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    ClusteredHashTableEntry results = cht.get(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     get method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var036()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    ClusteredHashTableEntry results = cht.get(key);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     get method.  The server has not been opened.  No exception is expected.  Verify that correct entry is returned.
     **/
    public void Var037()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    ClusteredHashTableEntry results = cht.get(key);
	    String outData = new String(results.getUserData());
            assertCondition((outData.equals(myData) == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getHandle method.  The server has not been opened.  No exception is expected.  Verify that the handle returned is null.
     **/
    public void Var038()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    String handle = cht.getHandle();
            assertCondition((handle == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getHandle method.  The server has been opened.  No exception is expected.  Verify that the handle returned is not null.
     **/
    public void Var039()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    String handle = cht.getHandle();
            assertCondition((handle != null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getName method.  The name has not been set.  No exception is expected.  Verify that the name returned is null.
     **/
    public void Var040()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    String name = cht.getName();
            assertCondition((name == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getName method.  The name has been set.  No exception is expected.  Verify that the name returned is correct.
     **/
    public void Var041()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    String name = cht.getName();
            assertCondition((name == CHTTest.chtSvrName_));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getSystem method.  The system has not been set.  No exception is expected.  Verify that the system returned is null.
     **/
    public void Var042()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    AS400 system = cht.getSystem();
            assertCondition((system == null));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     getSystem method.  The system has been set.  No exception is expected.  Verify that the system returned is correct.
     **/
    public void Var043()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    AS400 system = cht.getSystem();
            assertCondition((system == CHTTest.pwrSys_));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     isEmpty method.  The system has not been set and an exception is expected.
     **/
    public void Var044()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    boolean results = cht.isEmpty();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     isEmpty method.  The name has not been set and an exception is expected.
     **/
    public void Var045()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    boolean results = cht.isEmpty();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     isEmpty method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var046()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    boolean results = cht.isEmpty();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     isEmpty method.  The server has not been opened.  No exception is expected.  Verify that true is returned since server was just started.
     **/
    public void Var047()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    boolean results = cht.isEmpty();
            assertCondition((results == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     isEmpty method.  The server has been opened.  No exception is expected.  Add an entry to the table and verify that the result returned is false.
     **/
    public void Var048()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    boolean results = cht.isEmpty();
            assertCondition((results == false));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     open method.  The system has not been set and an exception is expected.
     **/
    public void Var049()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    cht.open();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     open method.  The name has not been set and an exception is expected.
     **/
    public void Var050()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    cht.open();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     open method.  The server has already been opened.  No exception is expected.
     **/
    public void Var051()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    cht.open();
	    succeeded();
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     open method.  An ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var052()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    CHTTest.endChtSvr();
	    cht.open();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     put method.  The system has not been set and an exception is expected.
     **/
    public void Var053()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     put method.  The name has not been set and an exception is expected.
     **/
    public void Var054()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     put method.  The entry has not been set and an exception is expected.
     **/
    public void Var055()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    ClusteredHashTableEntry myEntry = null;
	    cht.put(myEntry);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     put method.  Server is not yet open.  Name has been set to an invalid CHT server so open will fail.  Expect an exception.
     **/
    public void Var056()
    {
        try
        {
	    String name = "WRONG";
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, name);
	    byte[] key = new byte[16];
	    for (int j=0; j<16; ++j)
		key[j] = 0x01;
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     put method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var057()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    CHTTest.endChtSvr();
	    cht.put(myEntry);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     put method.  The server has not been opened.  No exception is expected.  Verify that correct data is returned.
     **/
    public void Var058()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    ClusteredHashTableEntry results = cht.get(key);
	    String newData = new String(results.getUserData());
            assertCondition((newData.equals(myData) == true));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     removePropertyChangeListener method.  Pass null for listener and expect an exception.
     **/
    public void Var059()
    {
        try
        {
	    PropertyChangeListener listener = null;
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.removePropertyChangeListener(listener);
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
    public void Var060()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
            CHTPropertyChangeListener listener = new CHTPropertyChangeListener();
	    cht.addPropertyChangeListener(listener);
	    cht.removePropertyChangeListener(listener);
	    cht.setName(CHTTest.chtSvrName_);
            assertCondition (listener.propertyName.equals(""));
        }
        catch (Exception e) 
        {
	    failed (e, "Unexpected Exception");
        }
    }

    /**
     setName method.  The name passed in is null and an exception is expected.
     **/
    public void Var061()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    String name = null;
	    cht.setName(name);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setName method.  The name passed in is invalid and an exception is expected.
     **/
    public void Var062()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    String name = "TOOLONGNAME";
	    cht.setName(name);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
        }
    }

    /**
     setName method.  The server has already been opened and an exception is expected.
     **/
    public void Var063()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    cht.setName(CHTTest.chtSvrName_);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     setName method.  Everything works and no exception is expected.  Verify the name is set correctly and that an open works..
     **/
    public void Var064()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    cht.setName(CHTTest.chtSvrName_);
	    cht.open();
            assertCondition((CHTTest.chtSvrName_ == cht.getName()));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     setSystem method.  The system passed in is null and an exception is expected.
     **/
    public void Var065()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    AS400 system = null;
	    cht.setSystem(system);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "java.lang.NullPointerException");
        }
    }

    /**
     setSystem method.  The server has already been opened and an exception is expected.
     **/
    public void Var066()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    cht.setSystem(CHTTest.pwrSys_);
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     setSystem method.  Everything works and no exception is expected.  Verify the system is set correctly and open works..
     **/
    public void Var067()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    cht.setSystem(CHTTest.pwrSys_);
	    cht.open();
            assertCondition((CHTTest.pwrSys_ == cht.getSystem()));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     size method.  The system has not been set and an exception is expected.
     **/
    public void Var068()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setName(CHTTest.chtSvrName_);
	    int results = cht.size();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     size method.  The name has not been set and an exception is expected.
     **/
    public void Var069()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable();
	    cht.setSystem(CHTTest.pwrSys_);
	    int results = cht.size();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalStateException");
        }
    }

    /**
     size method.  The server has been opened but an ENDCHTSVR command was done so an exception is expected.
     **/
    public void Var070()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    CHTTest.endChtSvr();
	    int results = cht.size();
	    failed("Did not throw exception.");
        }
        catch (Exception e)
        {
            assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
        }
    }

    /**
     size method.  The server has not been opened.  No exception is expected.  Verify that 0 is returned since server was just started.
     **/
    public void Var071()
    {
        try
        {
	    CHTTest.strChtSvr();
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    int results = cht.size();
            assertCondition((results == 0));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

    /**
     size method.  The server has been opened.  No exception is expected.  Add an entry to the table and verify that the result returned is 1.
     **/
    public void Var072()
    {
        try
        {
	    ClusteredHashTable cht = new ClusteredHashTable(CHTTest.pwrSys_, CHTTest.chtSvrName_);
	    cht.open();
	    byte[] key = cht.generateKey();
	    ClusteredHashTableEntry myEntry = null;
	    String myData = new String("This is my data");
	    myEntry = new ClusteredHashTableEntry(key,myData.getBytes(),2400,ClusteredHashTableEntry.ENTRY_AUTHORITY_ANY_USER,ClusteredHashTableEntry.DUPLICATE_KEY_FAIL);
	    cht.put(myEntry);
	    int results = cht.size();
            assertCondition((results == 1));
        }
        catch (Exception e)
        {
	    failed(e, "Unexpected exception");
        }
    }

}
