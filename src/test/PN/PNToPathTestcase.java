///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PNToPathTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PN;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.QSYSObjectPathName;

import test.Testcase;

/**
 Testcase PNToPathTestcase.  Test the following methods:
 <ul>
 <li>QSYSObjectPathName.toPath(library, object, type)
 <li>QSYSObjectPathName.toPath(library, object, member, type)
 </ul>
 **/
public class PNToPathTestcase extends Testcase
{
    /**
     Use toPath(library, object, type), passing null for the library parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var001()
    {
        try
        {
            QSYSObjectPathName.toPath(null, "MYOBJ", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "libraryName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing null for the object parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var002()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", null, "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "objectName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing null for the type parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var003()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "objectType"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing null for the library parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var004()
    {
        try
        {
            QSYSObjectPathName.toPath(null, "MYOBJ", "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "libraryName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing null for the object parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var005()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", null, "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "objectName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing null for the type parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var006()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "MYMBR", null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "objectType"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing null for the member parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var007()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", null, "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "NullPointerException", "memberName"))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }



    /////// Invalid parms

    /**
     Use toPath(library, object, type), passing a library
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name
     **/
    public void Var008()
    {
        try
        {
            QSYSObjectPathName.toPath("LONGLIBRARY", "MYOBJ", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "libraryName (LONGLIBRARY)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing a object
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var009()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "2LONGOBJECT", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectName (2LONGOBJECT)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing a type
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var010()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "TOOLONG");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectType (TOOLONG)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a library
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var011()
    {
        try
        {
            QSYSObjectPathName.toPath("LONGLIBRARY", "MYOBJ", "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "libraryName (LONGLIBRARY)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a object
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var012()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "2LONGOBJECT", "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectName (2LONGOBJECT)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a type
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var013()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "MYMBR", "TOOLONG");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectType (TOOLONG)",
                                    ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a member
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var014()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "2LONGMEMBER", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "memberName (2LONGMEMBER)",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing a library
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name
     **/
    public void Var015()
    {
        try
        {
            QSYSObjectPathName.toPath("", "MYOBJ", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "libraryName ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing a object
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var016()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectName ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, type), passing a type
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var017()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectType ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a library
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var018()
    {
        try
        {
            QSYSObjectPathName.toPath("", "MYOBJ", "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "libraryName ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a object
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var019()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "", "MYMBR", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectName ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a type
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var020()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "MYMBR", "");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectType ()",
                                    ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a member
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var021()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "", "MBR");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "memberName ()",
                                    ExtendedIllegalArgumentException.LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use toPath(library, object, member, type), passing a type
     other than "MBR".
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var022()
    {
        try
        {
            QSYSObjectPathName.toPath("MYLIB", "MYOBJ", "MYMBR", "OBJ");
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "ExtendedIllegalArgumentException",
                                    "objectType (OBJ)",
                                    ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }



    /////// Successful

    /**
     Do a successful toPath(library, object, type).
     **/
    public void Var023()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("MyLib", "MyObj", "obj");
            if (!s.equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful toPath(library, object, member, type).
     **/
    public void Var024()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("MyLib", "MyObj", "MyMbr", "mbr");
            if (!s.equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }




    /////// Mixed case

    /**
     Do a successful toPath(library, object, type),
     passing quoted names in mixed case,.
     **/
    public void Var025()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("\"MyLib\"", "\"MyObj\"", "obj");
            if (!s.equals("/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".OBJ"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful toPath(library, object, member, type),
     passing quoted names in mixed case.
     **/
    public void Var026()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("\"MyLib\"", "\"MyObj\"", "\"MyMbr\"", "mbr");
            if (!s.equals("/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".FILE/\"MyMbr\".MBR"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }




    /////// Special values

    /**
     Do a successful toPath(library, object, type),
     using each of the special library values (like *LIBL).
     **/
    public void Var027()
    {
        String s1 = "/QSYS.LIB/%LIBL%.LIB/OBJ1.OBJ";
        String s2 = "/QSYS.LIB/%CURLIB%.LIB/OBJ1.OBJ";
        String s3 = "/QSYS.LIB/%USRLIBL%.LIB/OBJ1.OBJ";
        String s4 = "/QSYS.LIB/%ALL%.LIB/OBJ1.OBJ";
        String s5 = "/QSYS.LIB/%ALLUSR%.LIB/OBJ1.OBJ";

        boolean failed = true;
        try
        {
            String s = QSYSObjectPathName.toPath("*libl", "OBJ1", "OBJ");
            if (!s.equals(s1))
                failed("Mismatched path: " + s);
            else
                failed = false;
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*curlib", "OBJ1", "OBJ");
                if (!s.equals(s2))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*usrlibl", "OBJ1", "OBJ");
                if (!s.equals(s3))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*all", "OBJ1", "OBJ");
                if (!s.equals(s4))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*allusr", "OBJ1", "OBJ");
                if (!s.equals(s5))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
            succeeded();
    }


    /**
     Do a successful toPath(library, object, type),
     using each of the special object values (like *ALL).
     **/
    public void Var028()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/%ALL%.OBJ";
        try
        {
            String s = QSYSObjectPathName.toPath("MYLIB", "*all", "OBJ");
            if (!s.equals(s1))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful toPath(library, object, member, type),
     using each of the special library values (like %LIBL%).
     **/
    public void Var029()
    {
        String s1 = "/QSYS.LIB/%LIBL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s2 = "/QSYS.LIB/%CURLIB%.LIB/OBJ1.FILE/MBR1.MBR";
        String s3 = "/QSYS.LIB/%USRLIBL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s4 = "/QSYS.LIB/%ALL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s5 = "/QSYS.LIB/%ALLUSR%.LIB/OBJ1.FILE/MBR1.MBR";

        boolean failed = true;
        try
        {
            String s = QSYSObjectPathName.toPath("*libl", "OBJ1", "MBR1", "MBR");
            if (!s.equals(s1))
                failed("Mismatched path: " + s);
            else
                failed = false;
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*curlib", "OBJ1", "MBR1", "MBR");
                if (!s.equals(s2))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*usrlibl", "OBJ1", "MBR1", "MBR");
                if (!s.equals(s3))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*all", "OBJ1", "MBR1", "MBR");
                if (!s.equals(s4))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("*allusr", "OBJ1", "MBR1", "MBR");
                if (!s.equals(s5))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
            succeeded();
    }


    /**
     Do a successful toPath(library, object, member, type),
     using each of the special object values (like *ALL).
     **/
    public void Var030()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/%ALL%.FILE/MBR1.MBR";
        try
        {
            String s = QSYSObjectPathName.toPath("MyLib", "*all", "mbr1", "mbr");
            if (!s.equals(s1))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful toPath(library, object, member, type),
     using each of the special member values (like *FIRST).
     **/
    public void Var031()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%FIRST%.MBR";
        String s2 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%LAST%.MBR";
        String s3 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%FILE%.MBR";
        String s4 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%ALL%.MBR";
        boolean failed = true;
        try
        {
            String s = QSYSObjectPathName.toPath("MYLIB", "OBJ1", "*first", "MBR");
            if (!s.equals(s1))
                failed("Mismatched path: " + s);
            else
                failed = false;
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("MYLIB", "OBJ1", "*last", "MBR");
                if (!s.equals(s2))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("MYLIB", "OBJ1", "*file", "MBR");
                if (!s.equals(s3))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                String s = QSYSObjectPathName.toPath("MYLIB", "OBJ1", "*all", "MBR");
                if (!s.equals(s4))
                    failed("Mismatched path: " + s);
                else
                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
            }
        }

        if (!failed)
            succeeded();
    }



    /////// QSYS

    /**
     Do a successful toPath(library, object, type),
     passing "qsys" for the library.
     **/
    public void Var032()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("qsys", "MyObj", "obj");
            if (!s.equals("/QSYS.LIB/MYOBJ.OBJ"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful toPath(library, object, member, type),
     passing "qsys" for the library.
     **/
    public void Var033()
    {
        try
        {
            String s = QSYSObjectPathName.toPath("qsys", "MyObj", "MyMbr", "mbr");
            if (!s.equals("/QSYS.LIB/MYOBJ.FILE/MYMBR.MBR"))
                failed("Mismatched path: " + s);
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }



}



