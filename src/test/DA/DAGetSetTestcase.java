///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DAGetSetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;

import java.math.BigDecimal;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.LocalDataArea;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.Testcase;

/**
 Testcase DAGetSetTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::getLength()
 <li>CharacterDataArea::getName()
 <li>CharacterDataArea::getPath()
 <li>CharacterDataArea::getSystem()
 <li>CharacterDataArea::refreshAttributes()
 <li>CharacterDataArea::setPath()
 <li>CharacterDataArea::setSystem()
 <li>DecimalDataArea::getDecimalPositions()
 <li>DecimalDataArea::getLength()
 <li>DecimalDataArea::getName()
 <li>DecimalDataArea::getPath()
 <li>DecimalDataArea::getSystem()
 <li>DecimalDataArea::refreshAttributes()
 <li>DecimalDataArea::setPath()
 <li>DecimalDataArea::setSystem()
 <li>LocalDataArea::getLength()
 <li>LocalDataArea::getName()
 <li>LocalDataArea::getPath()
 <li>LocalDataArea::getSystem()
 <li>LocalDataArea::refreshAttributes()
 <li>LocalDataArea::setSystem()
 <li>LogicalDataArea::getLength()
 <li>LogicalDataArea::getName()
 <li>LogicalDataArea::getPath()
 <li>LogicalDataArea::getSystem()
 <li>LogicalDataArea::refreshAttributes()
 <li>LogicalDataArea::setPath()
 <li>LogicalDataArea::setSystem()
 </ul>
 **/
public class DAGetSetTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DAGetSetTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.DATest.main(newArgs); 
   }
  
  String daSecTest_ ="DASECTST"; 
  
  protected void setup() { 
    if (testLib_ != null ) { 
      int len = testLib_.length(); 
      if (len >= 5) { 
        daSecTest_ = "DASEC"+testLib_.substring(len-5); 
      }
    }

  }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.getLength();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.getLength();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of CharacterDataArea::getLength().
     Try to get the length of a data area created using create().
     The method should return the proper data.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                if (da2.getLength() != 32)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify valid usage of CharacterDataArea::getLength().
     Try to get the length of a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(10, "Initial", " ", "*USE"); // Default size of 32 chars.
                if (da2.getLength() != 10)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var007()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*EXECUTE *OBJOPR)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::getLength().
     Try to get the length of a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var008()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of CharacterDataArea::getLength().
     Try to get the length of a changed data area.
     The method should return the proper data.
     **/
    public void Var009()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(12, "Initial", " ", "*USE");
                if (da.getLength() != 12)
                {
                    failed("Incorrect original length: "+da.getLength());
                }
                else
                {
                    da2.delete();
                    da2.create(14, "Initial2", " ", "*USE");
                    da.refreshAttributes();
                    if (da.getLength() != 14)
                    {
                        failed("Incorrect refreshed length: "+da.getLength());
                    }
                    else
                    {
                        succeeded();
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getName().
     Try to get the name of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var010()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            if (da.getName() != null)
            {
                failed("Wrong name: "+da.getName());
            }
            else
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
     Verify usage of CharacterDataArea::getName().
     Try to get the name of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var011()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getName().
     Try to get the name of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var012()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.setPath("/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");        
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getPath().
     Try to get the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var013()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            if (da.getPath() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getPath().
     Try to get the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var014()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getPath().
     Try to get the path of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var015()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            CharacterDataArea da = new CharacterDataArea();
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getSystem().
     Try to get the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var016()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            if (da.getSystem() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getSystem().
     Try to get the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var017()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::getSystem().
     Try to get the system of a data area when the system was set using setSystem().
     The method should return the proper data.
     **/
    public void Var018()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::refreshAttributes().
     The method should return the proper data.
     **/
    public void Var019()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create(10, "Initial", " ", "*USE");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da2.delete();
            da2.create(20, "Initial2", "desc", "*USE");
            da.refreshAttributes();
            if (da.getLength() != da2.getLength())
            {
                failed("Wrong lengths: "+da.getLength()+" != "+da2.getLength());
            }
            else
            {
                succeeded();
            }
            try
            {
                da.delete();
            }
            catch (Exception f) {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setPath().
     Try to set the path of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var020()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setPath(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setPath().
     Try to set the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var021()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setPath().
     Try to set the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var022()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            da.setPath(path2);        
            if (da.getPath().equals(path2))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setPath().
     Try to set the path of a data area after a connection has been made.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var023()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            da.create();
            try
            {
                da.setPath(path2);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception f)
        {
            failed(f, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setSystem().
     Try to set the system of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var024()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setSystem().
     Try to set the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var025()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of CharacterDataArea::setSystem().
     Try to set the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var026()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
            da.setSystem(newSys);
            if (da.getSystem() == newSys)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::setSystem().
     Try to set the system of a data area after a connection has
     already been established.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var027()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            CharacterDataArea da = new CharacterDataArea(systemObject_, path);
            da.create();
            try
            {
                AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
                da.setSystem(newSys);
                failed("Expected exception did not occur.");
            }
            catch (Exception f)
            {
                if (exceptionStartsWith(f, "ExtendedIllegalStateException", "system",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                    succeeded();
                else
                    failed(f, "Wrong exception info.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var028()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.getDecimalPositions();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var029()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.getDecimalPositions();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var030()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.getDecimalPositions();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var031()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.getDecimalPositions();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area created using create().
     The method should return the proper data.
     **/
    public void Var032()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(); // Default of 5 decimal positions.
                if (da2.getDecimalPositions() != 5)
                {
                    failed("Incorrect decimal positions: "+da2.getDecimalPositions());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify valid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var033()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(12, 3, new BigDecimal("8.8"), " ", "*USE");
                if (da2.getDecimalPositions() != 3)
                {
                    failed("Incorrect decimal positions: "+da2.getDecimalPositions());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var034()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*EXECUTE *OBJOPR)");
            try
            {
                da.getDecimalPositions();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var035()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.getDecimalPositions();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of DecimalDataArea::getDecimalPositions().
     Try to get the decimal positions of a changed data area.
     The method should return the proper data.
     **/
    public void Var036()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(8, 2, new BigDecimal("0.9"), " ", "*USE");
                if (da.getDecimalPositions() != 2)
                {
                    failed("Incorrect original decimal positions: "+da.getDecimalPositions());
                }
                else
                {
                    da2.delete();
                    da2.create(11, 7, new BigDecimal("11.7"), " ", "*USE");
                    da.refreshAttributes();
                    if (da.getDecimalPositions() != 7)
                    {
                        failed("Incorrect refreshed decimal positions: "+da.getDecimalPositions());
                    }
                    else
                    {
                        succeeded();
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var037()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var038()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var039()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.getLength();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var040()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.getLength();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of DecimalDataArea::getLength().
     Try to get the length of a data area created using create().
     The method should return the proper data.
     **/
    public void Var041()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(); // Default size of 15.
                if (da2.getLength() != 15)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify valid usage of DecimalDataArea::getLength().
     Try to get the length of a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var042()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(9, 4, new BigDecimal("0.0"), " ", "*USE");
                if (da2.getLength() != 9)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var043()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*EXECUTE *OBJOPR)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::getLength().
     Try to get the length of a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var044()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of DecimalDataArea::getLength().
     Try to get the length of a changed data area.
     The method should return the proper data.
     **/
    public void Var045()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(7, 1, new BigDecimal("2.2"), " ", "*USE");
                if (da.getLength() != 7)
                {
                    failed("Incorrect original length: "+da.getLength());
                }
                else
                {
                    da2.delete();
                    da2.create(10, 0, new BigDecimal("1"), " ", "*USE");
                    da.refreshAttributes();
                    if (da.getLength() != 10)
                    {
                        failed("Incorrect refreshed length: "+da.getLength());
                    }
                    else
                    {
                        succeeded();
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getName().
     Try to get the name of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var046()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            if (da.getName() != null)
            {
                failed("Wrong name: "+da.getName());
            }
            else
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
     Verify usage of DecimalDataArea::getName().
     Try to get the name of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var047()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getName().
     Try to get the name of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var048()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.setPath("/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");        
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getPath().
     Try to get the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var049()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            if (da.getPath() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getPath().
     Try to get the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var050()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getPath().
     Try to get the path of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var051()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            DecimalDataArea da = new DecimalDataArea();
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getSystem().
     Try to get the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var052()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            if (da.getSystem() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getSystem().
     Try to get the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var053()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::getSystem().
     Try to get the system of a data area when the system was set using setSystem().
     The method should return the proper data.
     **/
    public void Var054()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::refreshAttributes().
     The method should return the proper data.
     **/
    public void Var055()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create(11, 3, new BigDecimal("11.3"), " ", "*USE");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da2.delete();
            da2.create(12, 4, new BigDecimal("12.4"), "desc", "*USE");
            da.refreshAttributes();
            if (da.getLength() != da2.getLength())
            {
                failed("Wrong lengths: "+da.getLength()+" != "+da2.getLength());
            }
            else if (da.getDecimalPositions() != da2.getDecimalPositions())
            {
                failed("Wrong decimal positions: "+da.getDecimalPositions()+" != "+da2.getDecimalPositions());
            }
            else
            {
                succeeded();
            }
            try
            {
                da.delete();
            }
            catch (Exception f) {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setPath().
     Try to set the path of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var056()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setPath(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setPath().
     Try to set the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var057()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setPath().
     Try to set the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var058()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            da.setPath(path2);        
            if (da.getPath().equals(path2))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setPath().
     Try to set the path of a data area after a connection has been made.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var059()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            da.create();
            try
            {
                da.setPath(path2);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception f)
        {
            failed(f, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setSystem().
     Try to set the system of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var060()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setSystem().
     Try to set the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var061()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of DecimalDataArea::setSystem().
     Try to set the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var062()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
            da.setSystem(newSys);
            if (da.getSystem() == newSys)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::setSystem().
     Try to set the system of a data area after a connection has
     already been established.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var063()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            DecimalDataArea da = new DecimalDataArea(systemObject_, path);
            da.create();
            try
            {
                AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
                da.setSystem(newSys);
                failed("Expected exception did not occur.");
            }
            catch (Exception f)
            {
                if (exceptionStartsWith(f, "ExtendedIllegalStateException", "system",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                    succeeded();
                else
                    failed(f, "Wrong exception info.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of LocalDataArea::getLength().
     Try to get the length of a local data area.
     The method should return the proper data.
     **/
    public void Var064()
    {
        try
        {
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            try
            {
                if (da2.getLength() != 1024)
                {
                    failed("Incorrect length: "+da2.getLength());
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
        succeeded();
    }

    /**
     Verify usage of LocalDataArea::getName().
     Try to get the name of a data area.
     The method should return the proper data.
     **/
    public void Var065()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            if (da.getName().equals("*LDA"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LocalDataArea::getSystem().
     Try to get the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var066()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            if (da.getSystem() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LocalDataArea::getSystem().
     Try to get the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var067()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LocalDataArea::getSystem().
     Try to get the system of a data area when the system was set using setSystem().
     The method should return the proper data.
     **/
    public void Var068()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LocalDataArea::refreshAttributes().
     The method should return the proper data.
     **/
    public void Var069()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            LocalDataArea da2 = new LocalDataArea(systemObject_);
            da.refreshAttributes();
            if (da.getLength() != da2.getLength())
            {
                failed("Wrong lengths: "+da.getLength()+" != "+da2.getLength());
            }
            else
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
     Verify usage of LocalDataArea::setSystem().
     Try to set the system of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var070()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.setSystem(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of LocalDataArea::setSystem().
     Try to set the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var071()
    {
        try
        {
            LocalDataArea da = new LocalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LocalDataArea::setSystem().
     Try to set the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var072()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
            da.setSystem(newSys);
            if (da.getSystem() == newSys)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LocalDataArea::setSystem().
     Try to set the system of a data area after a connection has
     already been established.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var073()
    {
        try
        {
            LocalDataArea da = new LocalDataArea(systemObject_);
            da.read();
            try
            {
                AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
                da.setSystem(newSys);
                failed("Expected exception did not occur.");
            }
            catch (Exception f)
            {
                if (exceptionStartsWith(f, "ExtendedIllegalStateException", "system",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                    succeeded();
                else
                    failed(f, "Wrong exception info.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var074()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "System",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var075()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.getLength();
            failed("No exception.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalStateException", "Path",
                                    ExtendedIllegalStateException.PROPERTY_NOT_SET))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var076()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.getLength();
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException",
                            ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
            {
                succeeded();
            }
            else
            {
                failed(e, "Wrong exception info.");
            }
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var077()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.getLength();
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "ObjectDoesNotExistException",
                                ObjectDoesNotExistException.OBJECT_DOES_NOT_EXIST))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of LogicalDataArea::getLength().
     Try to get the length of a data area created using create().
     The method should return the proper data.
     **/
    public void Var078()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(); // Default size of 1.
                if (da2.getLength() != 1)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify valid usage of LogicalDataArea::getLength().
     Try to get the length of a data area created using create(int,String,String,String).
     The method should return the proper data.
     **/
    public void Var079()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(true, " ", "*USE"); // Default size of 1 char.
                if (da2.getLength() != 1)
                {
                    failed("Incorrect length: "+da2.getLength());
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        succeeded();
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var080()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*EXECUTE *OBJOPR)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.OBJECT_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::getLength().
     Try to get the length of a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var081()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,""+daSecTest_+"");
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_+""))
            {
                failed("Unable to create library "+daSecTest_+"");
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.getLength();
                failed("No exception.");
            }
            catch (Exception e)
            {
                if (exceptionIs(e, "AS400SecurityException", AS400SecurityException.LIBRARY_AUTHORITY_INSUFFICIENT))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                cmd.run("QSYS/DLTDTAARA "+daSecTest_+"/SECTST");
                deleteLibrary(cmd,""+daSecTest_+"");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of LogicalDataArea::getLength().
     Try to get the length of a changed data area.
     The method should return the proper data.
     **/
    public void Var082()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            try
            {
                da.create(true, " ", "*USE");
                if (da.getLength() != 1)
                {
                    failed("Incorrect original length: "+da.getLength());
                }
                else
                {
                    da2.delete();
                    da2.create(false, " ", "*USE");
                    da.refreshAttributes();
                    if (da.getLength() != 1)
                    {
                        failed("Incorrect refreshed length: "+da.getLength());
                    }
                    else
                    {
                        succeeded();
                    }
                }
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getName().
     Try to get the name of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var083()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            if (da.getName() != null)
            {
                failed("Wrong name: "+da.getName());
            }
            else
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
     Verify usage of LogicalDataArea::getName().
     Try to get the name of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var084()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getName().
     Try to get the name of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var085()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.setPath("/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA");        
            if (da.getName().equals("ATTRTEST"))
            {
                succeeded();
            }
            else
            {
                failed("Wrong name: "+da.getName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getPath().
     Try to get the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var086()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            if (da.getPath() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getPath().
     Try to get the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var087()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getPath().
     Try to get the path of a data area when the path was set using setPath().
     The method should return the proper data.
     **/
    public void Var088()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            LogicalDataArea da = new LogicalDataArea();
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getSystem().
     Try to get the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var089()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            if (da.getSystem() == null)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getSystem().
     Try to get the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var090()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::getSystem().
     Try to get the system of a data area when the system was set using setSystem().
     The method should return the proper data.
     **/
    public void Var091()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::refreshAttributes().
     The method should return the proper data.
     **/
    public void Var092()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da.create(true, " ", "*USE");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/ATTRTEST.DTAARA");
            da2.delete();
            da2.create(false, "desc", "*USE");
            da.refreshAttributes();
            if (da.getLength() != da2.getLength())
            {
                failed("Wrong lengths: "+da.getLength()+" != "+da2.getLength());
            }
            else
            {
                succeeded();
            }
            try
            {
                da.delete();
            }
            catch (Exception f) {}
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setPath().
     Try to set the path of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var093()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setPath(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "path"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setPath().
     Try to set the path of a data area which has no path set.
     The method should return the proper data.
     **/
    public void Var094()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            da.setPath(path);
            if (da.getPath().equals(path))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setPath().
     Try to set the path of a data area when the path was set on the constructor.
     The method should return the proper data.
     **/
    public void Var095()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            da.setPath(path2);        
            if (da.getPath().equals(path2))
            {
                succeeded();
            }
            else
            {
                failed("Wrong path: "+da.getPath());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setPath().
     Try to set the path of a data area after a connection has been made.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var096()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            String path2 = "/QSYS.LIB/DATEST.LIB/PATHTEST.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            da.create();
            try
            {
                da.setPath(path2);
                failed("Expected exception did not occur.");
            }
            catch (Exception e)
            {
                if (exceptionStartsWith(e, "ExtendedIllegalStateException", "path",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                {
                    succeeded();
                }
                else
                {
                    failed(e, "Wrong exception info.");
                }
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception f)
        {
            failed(f, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setSystem().
     Try to set the system of a data area to be null.
     The method should throw a NullPointerException.
     **/
    public void Var097()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "system"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setSystem().
     Try to set the system of a data area which has no system set.
     The method should return the proper data.
     **/
    public void Var098()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            if (da.getSystem() == systemObject_)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify usage of LogicalDataArea::setSystem().
     Try to set the system of a data area when the system was set on the constructor.
     The method should return the proper data.
     **/
    public void Var099()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
            da.setSystem(newSys);
            if (da.getSystem() == newSys)
            {
                succeeded();
            }
            else
            {
                failed("Wrong system: "+da.getSystem().toString());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::setSystem().
     Try to set the system of a data area after a connection has
     already been established.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var100()
    {
        try
        {
            String path = "/QSYS.LIB/QTEMP.LIB/attrtest.DTAARA";
            LogicalDataArea da = new LogicalDataArea(systemObject_, path);
            da.create();
            try
            {
                AS400 newSys = new AS400("TEST400", "TESTID", "TESTPASS".toCharArray());
                da.setSystem(newSys);
                failed("Expected exception did not occur.");
            }
            catch (Exception f)
            {
                if (exceptionStartsWith(f, "ExtendedIllegalStateException", "system",
                                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
                    succeeded();
                else
                    failed(f, "Wrong exception info.");
            }
            finally
            {
                da.delete();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

}
