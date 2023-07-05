///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PNCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.QSYSObjectPathName;

/**
 Testcase PNCtorTestcase.  Test the following methods:
 <ul>
 <li>QSYSObjectPathName()
 <li>QSYSObjectPathName(path)
 <li>QSYSObjectPathName(library, object, type)
 <li>QSYSObjectPathName(library, object, member, type)
 </ul>
 **/
public class PNCtorTestcase extends Testcase
{
    /**
     Use QSYSObjectPathName(path), passing null for the path parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var001()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(null);
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
     Use QSYSObjectPathName(library, object, type), passing null for the library parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var002()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(null, "MYOBJ", "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing null for the object parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var003()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", null, "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing null for the type parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var004()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", null);
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
     Use QSYSObjectPathName(library, object, member, type), passing null for the library parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var005()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(null, "MYOBJ", "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing null for the object parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var006()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", null, "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing null for the type parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var007()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "MYMBR", null);
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
     Use QSYSObjectPathName(library, object, type, member), passing null for the member parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var008()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", null, "MBR");
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
     Use QSYSObjectPathName(path), passing a path which does not start
     with "/QSYS.LIB/".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var009()
    {
        String s = "/MYLIB.LIB/MYOBJ.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.QSYS_PREFIX_MISSING))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path in which the library
     is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var010()
    {
        String s = "/QSYS.LIB/LONGLIBRARY.LIB/MYOBJ.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path which a library qualifier
     other than ".LIB".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var011()
    {
        String s = "/QSYS.LIB/MYLIB.ME/MYOBJ.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.LIBRARY_SPECIFICATION_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path which has no library
     qualifier ".LIB".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var012()
    {
        String s = "/QSYS.LIB/MYLIB/MYOBJ.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.LIBRARY_SPECIFICATION_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path in which the object
     is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var013()
    {
        String s = "/QSYS.LIB/MYLIB.LIB/2LONGOBJECT.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.OBJECT_LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path in which the type
     is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var014()
    {
        String s = "/QSYS.LIB/MYLIB.LIB/MYOBJ.TOOLONG";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.TYPE_LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path which has no object type
     qualifier.
     An IllegalPathNameException should be thrown, verify the rc and
     parm name.
     **/
    public void Var015()
    {
        String s = "/QSYS.LIB/MYLIB.LIB/MYOBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.TYPE_LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path in which the member
     is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var016()
    {
        String s = "/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/2LONGMEMBER.MBR";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.MEMBER_LENGTH_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(path), passing a path in which is of type MBR,
     but which has no FILE.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var017()
    {
        String s = "/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ/MYMEMBER.MBR";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.MEMBER_WITHOUT_FILE))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Use QSYSObjectPathName(library, object, type), passing a library
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name
     **/
    public void Var018()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("LONGLIBRARY", "MYOBJ", "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing a object
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var019()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "2LONGOBJECT", "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing a type
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var020()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "TOOLONG");
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
     Use QSYSObjectPathName(library, object, member, type), passing a library
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var021()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("LONGLIBRARY", "MYOBJ", "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing a object
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var022()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "2LONGOBJECT", "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing a type
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var023()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "MYMBR", "TOOLONG");
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
     Use QSYSObjectPathName(library, object, member, type), passing a member
     that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var024()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "2LONGMEMBER", "MBR");
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
     Use QSYSObjectPathName(library, object, type), passing a library
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name
     **/
    public void Var025()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("", "MYOBJ", "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing a object
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var026()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "", "OBJ");
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
     Use QSYSObjectPathName(library, object, type), passing a type
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var027()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "");
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
     Use QSYSObjectPathName(library, object, member, type), passing a library
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var028()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("", "MYOBJ", "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing a object
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var029()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "", "MYMBR", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing a type
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var030()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "MYMBR", "");
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
     Use QSYSObjectPathName(library, object, member, type), passing a member
     that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var031()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "", "MBR");
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
     Use QSYSObjectPathName(library, object, member, type), passing a type
     other than "MBR".
     An ExtendedIllegalArgumentException should be thrown, verify the rc and
     parm name.
     **/
    public void Var032()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "MYOBJ", "MYMBR", "OBJ");
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
     Do a successful QSYSObjectPathName().
     Verify that all getters return an empty String.
     **/
    public void Var033()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        if (!pn.getLibraryName().equals(""))
            failed("Bad library value: " + pn.getLibraryName());
        if (!pn.getObjectName().equals(""))
            failed("Bad object value: " + pn.getObjectName());
        if (!pn.getObjectType().equals(""))
            failed("Bad type value: " + pn.getObjectType());
        if (!pn.getMemberName().equals(""))
            failed("Bad member value: " + pn.getMemberName());
        if (!pn.getPath().equals(""))
            failed("Bad path value: " + pn.getPath());
        else
            succeeded();
    }


    /**
     Do a successful QSYSObjectPathName(path), passing a path which represents a file.
     Verify object values by calling all getters.
     All values should be uppercased, getMemberName() should return an empty String.
     **/
    public void Var034()
    {
        try
        {
            String s = "/qsys.lib/MyLib.lib/MyFile.file";
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYFILE"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("FILE"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path), passing a path which represents a member.
     Verify object values by calling all getters.
     All values should be uppercased, getObjectType() should return "MBR".
     **/
    public void Var035()
    {
        try
        {
            String s = "/QSYS.LIB/MyLib.lib/MyFile.file/MyMember.mbr";
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYFILE"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MYMEMBER"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, type).
     Verify object values by calling all getters.
     All values should be uppercased, getMemberName() should return an empty String.
     **/
    public void Var036()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MyLib", "MyObj", "obj");
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.OBJ"))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, member, type).
     Verify object values by calling all getters.
     All values should be uppercased, getObjectType() should return "MBR".
     **/
    public void Var037()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MyLib", "MyObj", "MyMbr", "mbr");
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB.LIB/MYOBJ.FILE/MYMBR.MBR"))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MYMBR"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test boundary lengths on QSYSObjectPathName(path)
     with a path that represents a non-member.
     Verify object values by calling all getters.
     All values should be uppercased, getMemberName() should return "".
     **/
    public void Var038()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(
                                                           "/QSYS.LIB/MyLib12345.LIB/MyObj12345.MyType");
            QSYSObjectPathName pn2 = new QSYSObjectPathName(
                                                            "/QSYS.LIB/l.lib/o.t");
            if (!(pn.getLibraryName()).equals("MYLIB12345"))
                failed("Mismatched long library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ12345"))
                    failed("Mismatched long object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MYTYPE"))
                        failed("Mismatched long type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB12345.LIB/MYOBJ12345.MYTYPE"))
                            failed("Mismatched long path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals(""))
                                failed("Mismatched long member: " + pn.getMemberName());
                            else
                                if (!(pn2.getLibraryName()).equals("L"))
                                    failed("Mismatched short library: " + pn.getLibraryName());
                                else
                                    if (!(pn2.getObjectName()).equals("O"))
                                        failed("Mismatched short object: " + pn.getObjectName());
                                    else
                                        if (!(pn2.getObjectType()).equals("T"))
                                            failed("Mismatched short type: " + pn.getObjectType());
                                        else
                                            if (!(pn2.getPath()).equals("/QSYS.LIB/L.LIB/O.T"))
                                                failed("Mismatched short path: " + pn.getPath());
                                            else
                                                if (!(pn2.getMemberName()).equals(""))
                                                    failed("Mismatched short member: " + pn.getMemberName());
                                                else
                                                    succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test boundary lengths on QSYSObjectPathName(path)
     with a path that represents a member.
     Verify object values by calling all getters.
     All values should be uppercased, getObjectType() should return "MBR".
     **/
    public void Var039()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(
                                                           "/QSYS.LIB/MyLib12345.LIB/MyObj12345.file/MyMbr12345.mbr");
            QSYSObjectPathName pn2 = new QSYSObjectPathName(
                                                            "/QSYS.LIB/l.lib/o.file/m.mbr");
            if (!(pn.getLibraryName()).equals("MYLIB12345"))
                failed("Mismatched long library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ12345"))
                    failed("Mismatched long object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched long type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB12345.LIB/MYOBJ12345.FILE/MYMBR12345.MBR"))
                            failed("Mismatched long path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MYMBR12345"))
                                failed("Mismatched long member: " + pn.getMemberName());
                            else
                                if (!(pn2.getLibraryName()).equals("L"))
                                    failed("Mismatched short library: " + pn.getLibraryName());
                                else
                                    if (!(pn2.getObjectName()).equals("O"))
                                        failed("Mismatched short object: " + pn.getObjectName());
                                    else
                                        if (!(pn2.getObjectType()).equals("MBR"))
                                            failed("Mismatched short type: " + pn.getObjectType());
                                        else
                                            if (!(pn2.getPath()).equals("/QSYS.LIB/L.LIB/O.FILE/M.MBR"))
                                                failed("Mismatched short path: " + pn.getPath());
                                            else
                                                if (!(pn2.getMemberName()).equals("M"))
                                                    failed("Mismatched short member: " + pn.getMemberName());
                                                else
                                                    succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test boundary lengths on QSYSObjectPathName(library, object, type).
     Verify object values by calling all getters.
     All values should be uppercased, getMemberName() should return "".
     **/
    public void Var040()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(
                                                           "MyLib12345", "MyObj12345", "MyType");
            QSYSObjectPathName pn2 = new QSYSObjectPathName(
                                                            "l", "o", "t");
            if (!(pn.getLibraryName()).equals("MYLIB12345"))
                failed("Mismatched long library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ12345"))
                    failed("Mismatched long object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MYTYPE"))
                        failed("Mismatched long type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB12345.LIB/MYOBJ12345.MYTYPE"))
                            failed("Mismatched long path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals(""))
                                failed("Mismatched long member: " + pn.getMemberName());
                            else
                                if (!(pn2.getLibraryName()).equals("L"))
                                    failed("Mismatched short library: " + pn.getLibraryName());
                                else
                                    if (!(pn2.getObjectName()).equals("O"))
                                        failed("Mismatched short object: " + pn.getObjectName());
                                    else
                                        if (!(pn2.getObjectType()).equals("T"))
                                            failed("Mismatched short type: " + pn.getObjectType());
                                        else
                                            if (!(pn2.getPath()).equals("/QSYS.LIB/L.LIB/O.T"))
                                                failed("Mismatched short path: " + pn.getPath());
                                            else
                                                if (!(pn2.getMemberName()).equals(""))
                                                    failed("Mismatched short member: " + pn.getMemberName());
                                                else
                                                    succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Test boundary lengths on QSYSObjectPathName(library, object, member, type).
     Verify object values by calling all getters.
     All values should be uppercased, getObjectType() should return "MBR".
     **/
    public void Var041()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(
                                                           "MyLib12345", "MyObj12345", "MyMbr12345", "mbr");
            QSYSObjectPathName pn2 = new QSYSObjectPathName(
                                                            "l", "o", "m", "mbr");
            if (!(pn.getLibraryName()).equals("MYLIB12345"))
                failed("Mismatched long library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ12345"))
                    failed("Mismatched long object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched long type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/MYLIB12345.LIB/MYOBJ12345.FILE/MYMBR12345.MBR"))
                            failed("Mismatched long path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MYMBR12345"))
                                failed("Mismatched long member: " + pn.getMemberName());
                            else
                                if (!(pn2.getLibraryName()).equals("L"))
                                    failed("Mismatched short library: " + pn.getLibraryName());
                                else
                                    if (!(pn2.getObjectName()).equals("O"))
                                        failed("Mismatched short object: " + pn.getObjectName());
                                    else
                                        if (!(pn2.getObjectType()).equals("MBR"))
                                            failed("Mismatched short type: " + pn.getObjectType());
                                        else
                                            if (!(pn2.getPath()).equals("/QSYS.LIB/L.LIB/O.FILE/M.MBR"))
                                                failed("Mismatched short path: " + pn.getPath());
                                            else
                                                if (!(pn2.getMemberName()).equals("M"))
                                                    failed("Mismatched short member: " + pn.getMemberName());
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
     Do a successful QSYSObjectPathName(path),
     passing a quoted path in mixed case, representing an object that
     is not a member.
     Verify object values by calling all getters.
     All values should be as input, getMemberName() should return an empty String
     **/
    public void Var042()
    {
        try
        {
            String s = "/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".OBJ";
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("\"MyLib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"MyObj\""))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a quoted path in mixed case, representing an object that
     is a member.
     Verify object values by calling all getters.
     All values should be as input, getObjectType() should return "MBR".
     **/
    public void Var043()
    {
        try
        {
            String s = "/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".FILE/\"MyMbr\".MBR";
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("\"MyLib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"MyObj\""))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("\"MyMbr\""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, type),
     passing quoted names in mixed case,.
     Verify object values by calling all getters.
     All values should be as input, getMemberName() should return an empty String.
     **/
    public void Var044()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("\"MyLib\"", "\"MyObj\"", "OBJ");
            if (!(pn.getLibraryName()).equals("\"MyLib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"MyObj\""))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".OBJ"))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, member, type),
     passing quoted names in mixed case.
     Verify object values by calling all getters.
     All values should be as input, getObjectType() should return "MBR".
     **/
    public void Var045()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("\"MyLib\"", "\"MyObj\"", "\"MyMbr\"", "MBR");
            if (!(pn.getLibraryName()).equals("\"MyLib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"MyObj\""))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals("/QSYS.LIB/\"MyLib\".LIB/\"MyObj\".FILE/\"MyMbr\".MBR"))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("\"MyMbr\""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }




    /////// Names with characters that are special to parsing path names

    /**
     Do a successful QSYSObjectPathName(path),
     passing a path which names have embedded periods,
     representing an object that is not a member.
     Verify object values by calling all getters.
     **/
    public void Var046()
    {
        String s = "/QSYS.LIB/My.Library.LIB/my.object.OBJ";
        QSYSObjectPathName pn = new QSYSObjectPathName(s);
        try
        {
            if (!(pn.getLibraryName()).equals("MY.LIBRARY"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MY.OBJECT"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a path which names have embedded periods,
     representing an object that is a member.
     Verify object values by calling all getters.
     **/
    public void Var047()
    {
        String s = "/QSYS.LIB/My.libr.LIB/My.object.FILE/My.member.MBR";
        QSYSObjectPathName pn = new QSYSObjectPathName(s);
        try
        {
            if (!(pn.getLibraryName()).equals("MY.LIBR"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MY.OBJECT"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MY.MEMBER"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a path which names have embedded slashes,
     representing an object that is not a member.
     Verify object values by calling all getters.
     **/
    public void Var048()
    {
        String s = "/QSYS.LIB/My/Library.LIB/my/object.OBJ";
        QSYSObjectPathName pn = new QSYSObjectPathName(s);
        try
        {
            if (!(pn.getLibraryName()).equals("MY/LIBRARY"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MY/OBJECT"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!pn.getMemberName().equals(""))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a path which names have embedded slashes,
     representing an object that is a member.
     Verify object values by calling all getters.
     **/
    public void Var049()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/My/libr.LIB/My/object.FILE/My/member.MBR";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("MY/LIBR"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MY/OBJECT"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MY/MEMBER"))
                                failed("Mismatched member: " + pn.getMemberName());
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
     Do a successful QSYSObjectPathName(path),
     using each of the special library values (like %LIBL%).
     Verify object values by calling all getters.
     All values should be as input,
     getLibraryName() should return translated special values (like *LIBL).
     **/
    public void Var050()
    {
        String s1 = "/QSYS.LIB/%libl%.LIB/OBJ1.FILE/MBR1.MBR";
        String s2 = "/QSYS.LIB/%curlib%.LIB/OBJ1.FILE/MBR1.MBR";
        String s3 = "/QSYS.LIB/%usrlibl%.LIB/OBJ1.FILE/MBR1.MBR";
        String s4 = "/QSYS.LIB/%all%.LIB/OBJ1.FILE/MBR1.MBR";
        String s5 = "/QSYS.LIB/%allusr%.LIB/OBJ1.FILE/MBR1.MBR";
        boolean failed = true;
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s1);
            if (!(pn.getLibraryName()).equals("*LIBL"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("OBJ1"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MBR1"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                failed = false;
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }

        if (!failed)
        {
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s2);
                if (!(pn.getLibraryName()).equals("*CURLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s2.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s3);
                if (!(pn.getLibraryName()).equals("*USRLIBL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s3.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s4);
                if (!(pn.getLibraryName()).equals("*ALL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s4.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s5);
                if (!(pn.getLibraryName()).equals("*ALLUSR"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s5.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
     Do a successful QSYSObjectPathName(path),
     using each of the special object values (like %ALL%).
     Verify object values by calling all getters.
     All values should be as input,
     getObjectName() should return translated special values (like *ALL).
     **/
    public void Var051()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/%all%.FILE/MBR1.MBR";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s1);
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("*ALL"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MBR1"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     using each of the special member values (like %FIRST%).
     Verify object values by calling all getters.
     All values should be as input,
     getMemberName() should return translated special values (like *FIRST).
     **/
    public void Var052()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%first%.MBR";
        String s2 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%last%.MBR";
        String s3 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%file%.MBR";
        String s4 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%all%.MBR";
        String s5 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%none%.MBR";
        boolean failed = true;
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s1);
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("OBJ1"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1.toUpperCase()))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("*FIRST"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                failed = false;
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }

        if (!failed)
        {
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s2);
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s2.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*LAST"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s3);
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s3.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*FILE"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s4);
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s4.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*ALL"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
            failed = true;
            try
            {
                QSYSObjectPathName pn = new QSYSObjectPathName(s5);
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s5.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*NONE"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
     Do a successful QSYSObjectPathName(library, object, type),
     using each of the special library values (like *LIBL).
     Verify object values by calling all getters.
     getLibraryName() should return the values as input (uppercased).
     getPath() should contain the %..% values (like %LIBL%)
     **/
    public void Var053()
    {
        String s1 = "/QSYS.LIB/%LIBL%.LIB/OBJ1.OBJ";
        String s2 = "/QSYS.LIB/%CURLIB%.LIB/OBJ1.OBJ";
        String s3 = "/QSYS.LIB/%USRLIBL%.LIB/OBJ1.OBJ";
        String s4 = "/QSYS.LIB/%ALL%.LIB/OBJ1.OBJ";
        String s5 = "/QSYS.LIB/%ALLUSR%.LIB/OBJ1.OBJ";

        boolean failed = true;
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("*libl", "OBJ1", "OBJ");
            if (!(pn.getLibraryName()).equals("*LIBL"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("OBJ1"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1))
                            failed("Mismatched path: " + pn.getPath());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*curlib", "OBJ1", "OBJ");
                if (!(pn.getLibraryName()).equals("*CURLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("OBJ"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s2))
                                failed("Mismatched path: " + pn.getPath());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*usrlibl", "OBJ1", "OBJ");
                if (!(pn.getLibraryName()).equals("*USRLIBL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("OBJ"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s3))
                                failed("Mismatched path: " + pn.getPath());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*all", "OBJ1", "OBJ");
                if (!(pn.getLibraryName()).equals("*ALL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("OBJ"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s4))
                                failed("Mismatched path: " + pn.getPath());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*allusr", "OBJ1", "OBJ");
                if (!(pn.getLibraryName()).equals("*ALLUSR"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("OBJ"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s5))
                                failed("Mismatched path: " + pn.getPath());
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
     Do a successful QSYSObjectPathName(library, object, type),
     using each of the special object values (like *ALL).
     Verify object values by calling all getters.
     getObjectName() should return the values as input (uppercased).
     getPath() should contain the %..% values (like %ALL%)
     **/
    public void Var054()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/%ALL%.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "*all", "OBJ");
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("*ALL"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("OBJ"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, member, type),
     using each of the special library values (like *LIBL).
     Verify object values by calling all getters.
     getLibraryName() should return the values as input (uppercased).
     getPath() should contain the %..% values (like %LIBL%)
     **/
    public void Var055()
    {
        String s1 = "/QSYS.LIB/%LIBL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s2 = "/QSYS.LIB/%CURLIB%.LIB/OBJ1.FILE/MBR1.MBR";
        String s3 = "/QSYS.LIB/%USRLIBL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s4 = "/QSYS.LIB/%ALL%.LIB/OBJ1.FILE/MBR1.MBR";
        String s5 = "/QSYS.LIB/%ALLUSR%.LIB/OBJ1.FILE/MBR1.MBR";

        boolean failed = true;
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("*libl", "OBJ1", "MBR1", "MBR");
            if (!(pn.getLibraryName()).equals("*LIBL"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("OBJ1"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MBR1"))
                                failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*curlib", "OBJ1", "MBR1", "MBR");
                if (!(pn.getLibraryName()).equals("*CURLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s2))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*usrlibl", "OBJ1", "MBR1", "MBR");
                if (!(pn.getLibraryName()).equals("*USRLIBL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s3))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("*all", "OBJ1", "MBR1", "MBR");
                if (!(pn.getLibraryName()).equals("*ALL"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s4))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
                                else
                                    failed = false;
            }
            catch (Exception e)
            {
                failed(e, "Unexpected exception.");
                failed = true;
            }
        }

        if (!failed)
        {
            try
            {
                failed = true;
                QSYSObjectPathName pn = new QSYSObjectPathName("*allusr", "OBJ1", "MBR1", "MBR");
                if (!(pn.getLibraryName()).equals("*ALLUSR"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s5))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("MBR1"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
     Do a successful QSYSObjectPathName(library, object, member, type),
     using each of the special object values (like *ALL).
     Verify object values by calling all getters.
     getObjectName() should return the values as input (uppercased).
     getPath() should contain the %..% values (like %ALL%)
     **/
    public void Var056()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/%ALL%.FILE/MBR1.MBR";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "*all", "MBR1", "MBR");
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("*ALL"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("MBR1"))
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, member, type),
     using each of the special member values (like *FIRST).
     Verify object values by calling all getters.
     getMemberName() should return the values as input (uppercased).
     getPath() should contain the %..% values (like %FIRST%)
     **/
    public void Var057()
    {
        String s1 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%FIRST%.MBR";
        String s2 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%LAST%.MBR";
        String s3 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%FILE%.MBR";
        String s4 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%ALL%.MBR";
        String s5 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%NONE%.MBR";
        boolean failed = true;
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "OBJ1", "*first", "MBR");
            if (!(pn.getLibraryName()).equals("MYLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("OBJ1"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s1))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("*FIRST"))
                                failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "OBJ1", "*last", "MBR");
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s2))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*LAST"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "OBJ1", "*file", "MBR");
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s3))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*FILE"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "OBJ1", "*all", "MBR");
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s4))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*ALL"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
                QSYSObjectPathName pn = new QSYSObjectPathName("MYLIB", "OBJ1", "*none", "MBR");
                if (!(pn.getLibraryName()).equals("MYLIB"))
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("OBJ1"))
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s5))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                if (!(pn.getMemberName()).equals("*NONE"))
                                    failed("Mismatched member: " + pn.getMemberName());
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
     QSYSObjectPathName(path), passing a path representing an object that
     is not a member, using "/QSYS.LIB/QSYS.LIB"...
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var058()
    {
        String s = "/QSYS.LIB/QSYS.LIB/MYOBJ.OBJ";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.QSYS_SYNTAX_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     QSYSObjectPathName(path), passing a path representing an object that
     is a member, using "/QSYS.LIB/QSYS.LIB"...
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var059()
    {
        String s = "/QSYS.LIB/QSYS.LIB/MYOBJ.FILE/MYMBR.MBR";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            if (exceptionStartsWith(e, "IllegalPathNameException", s,
                                    IllegalPathNameException.QSYS_SYNTAX_NOT_VALID))
                succeeded();
            else
                failed(e, "Wrong exception info.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a valid path representing an object that is in QSYS and
     is not a member.
     Verify the call completes successfuly, and getters return
     correct values.
     **/
    public void Var060()
    {
        String s = "/Qsys.LIB/MyObj.obj";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("QSYS"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getMemberName()).equals(""))
                        failed("Mismatched member: " + pn.getMemberName());
                    else
                        if (!(pn.getObjectType()).equals("OBJ"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(path),
     passing a valid path representing an object that is in QSYS and
     is a member.
     Verify the call completes successfully, and getters return
     correct values.
     **/
    public void Var061()
    {
        String s = "/Qsys.LIB/MyObj.file/MyMbr.mbr";
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName(s);
            if (!(pn.getLibraryName()).equals("QSYS"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("MYOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getMemberName()).equals("MYMBR"))
                        failed("Mismatched member: " + pn.getMemberName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getPath()).equals(s.toUpperCase()))
                                failed("Mismatched path: " + pn.getPath());
                            else
                                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, type),
     passing "qsys" for the library.
     Verify the path is correct using getPath().
     **/
    public void Var062()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("qsys", "MYOBJ", "OBJ");
            if (!(pn.getPath()).equals("/QSYS.LIB/MYOBJ.OBJ"))
                failed("Mismatched path: " + pn.getPath());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Do a successful QSYSObjectPathName(library, object, member, type),
     passing "qsys" for the library.
     Verify the path is correct using getPath().
     **/
    public void Var063()
    {
        try
        {
            QSYSObjectPathName pn = new QSYSObjectPathName("qsys", "MYOBJ", "MYMBR", "MBR");
            if (!(pn.getPath()).equals("/QSYS.LIB/MYOBJ.FILE/MYMBR.MBR"))
                failed("Mismatched path: " + pn.getPath());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


}



