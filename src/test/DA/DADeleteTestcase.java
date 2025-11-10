///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DADeleteTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DA;

import java.math.BigDecimal;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.CharacterDataArea;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.DecimalDataArea;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.LogicalDataArea;
import com.ibm.as400.access.ObjectDoesNotExistException;

import test.Testcase;

/**
 Testcase DADeleteTestcase.
 <p>Test variations for the methods:
 <ul>
 <li>CharacterDataArea::delete()
 <li>DecimalDataArea::delete()
 <li>LogicalDataArea::delete()
 </ul>
 **/
public class DADeleteTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "DADeleteTestcase";
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
    String getPaddedUser()
    {
        String user = systemObject_.getUserId();
        int userLength = user.length();
        StringBuffer paddedUser = new StringBuffer(user);
        for (int x = userLength; x < 10; ++x)
        {
            paddedUser.append(' ');
        }
        return paddedUser.toString();
    }

    /**
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var001()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.delete();
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
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var002()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea();
            da.setSystem(systemObject_);
            da.delete();
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
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var003()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.delete();
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
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var004()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.delete();
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
     Verify valid usage of CharacterDataArea::delete().
     Try to delete a data area created using create().
     The method should return the proper data.
     **/
    public void Var005()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create(); // Default size of 32 chars.
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify valid usage of CharacterDataArea::delete().
     Try to delete a data area created using create(int,String,String,String).
     **/
    public void Var006()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create(10, "Initial", " ", "*USE"); // Default size of 32 chars.
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var007()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of CharacterDataArea::delete().
     Try to delete a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var008()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*CHAR) LEN(20)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of CharacterDataArea::delete().
     Try to delete a data area that was created using %LIBL%.
     **/
    public void Var009()
    {
        try
        {
            CharacterDataArea da = new CharacterDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/DELTEST.DTAARA");
            CharacterDataArea da2 = new CharacterDataArea(systemObject_, "/QSYS.LIB/%LIBL%.LIB/DELTEST.DTAARA");
            try
            {
                da.create();
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var010()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.delete();
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
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var011()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea();
            da.setSystem(systemObject_);
            da.delete();
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
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var012()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.delete();
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
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var013()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.delete();
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
     Verify valid usage of DecimalDataArea::delete().
     Try to delete a data area created using create().
     The method should return the proper data.
     **/
    public void Var014()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create();
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify valid usage of DecimalDataArea::delete().
     Try to delete a data area created using create(int,String,String,String).
     **/
    public void Var015()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create(10, 3, new BigDecimal("2.4"), " ", "*USE");
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var016()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of DecimalDataArea::delete().
     Try to delete a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var017()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*DEC)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of DecimalDataArea::delete().
     Try to delete a data area that was created using %LIBL%.
     **/
    public void Var018()
    {
        try
        {
            DecimalDataArea da = new DecimalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/DELTEST.DTAARA");
            DecimalDataArea da2 = new DecimalDataArea(systemObject_, "/QSYS.LIB/%LIBL%.LIB/DELTEST.DTAARA");
            try
            {
                da.create();
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a data area which has no system set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var019()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.delete();
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
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a data area which has no path set.
     The method should throw an ExtendedIllegalStateException.
     **/
    public void Var020()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea();
            da.setSystem(systemObject_);
            da.delete();
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
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a non-existent data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var021()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.delete();
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
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a deleted data area.
     The method should throw an ObjectDoesNotExistException.
     **/
    public void Var022()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            da.create();
            da2.delete();
            try
            {
                da2.delete();
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
     Verify valid usage of LogicalDataArea::delete().
     Try to delete a data area created using create().
     The method should return the proper data.
     **/
    public void Var023()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create();
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify valid usage of LogicalDataArea::delete().
     Try to delete a data area created using create(int,String,String,String).
     **/
    public void Var024()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/QTEMP.LIB/DELTEST.DTAARA");
            try
            {
                da.create(true, " ", "*USE");
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a data area to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var025()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " AUT(*READ *EXECUTE *OBJOPR)");
            da.getLength();
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+"/SECTST *DTAARA " + user + " *OBJOPR");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify invalid usage of LogicalDataArea::delete().
     Try to delete a data area in a library to which the user has no authority.
     The method should throw an AS400SecurityException.
     **/
    public void Var026()
    {
        CommandCall cmd = new CommandCall(pwrSys_);
        LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/"+daSecTest_+".LIB/SECTST.DTAARA");

        try
        {
            deleteLibrary(cmd,daSecTest_);
            if (!cmd.run("QSYS/CRTLIB "+daSecTest_))
            {
                failed("Unable to create library "+daSecTest_);
                return;
            }

            String user = systemObject_.getUserId();
            cmd.run("QSYS/CRTDTAARA "+daSecTest_+"/SECTST TYPE(*LGL)");
            cmd.run("QSYS/GRTOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE *READ)");
            cmd.run("QSYS/RVKOBJAUT "+daSecTest_+" *LIB " + user + " AUT(*EXECUTE)");
            try
            {
                da.delete();
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
                deleteLibrary(cmd,daSecTest_);
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Verify valid usage of LogicalDataArea::delete().
     Try to delete a data area that was created using %LIBL%.
     **/
    public void Var027()
    {
        try
        {
            LogicalDataArea da = new LogicalDataArea(systemObject_, "/QSYS.LIB/QGPL.LIB/DELTEST.DTAARA");
            LogicalDataArea da2 = new LogicalDataArea(systemObject_, "/QSYS.LIB/%LIBL%.LIB/DELTEST.DTAARA");
            try
            {
                da.create();
                da2.delete();
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
            try
            {
                da.read();
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
}
