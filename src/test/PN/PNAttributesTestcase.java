///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PNAttributesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PN;

import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.IllegalPathNameException;
import com.ibm.as400.access.QSYSObjectPathName;

import test.Testcase;

/**
 Testcase PNAttributesTestcase.  Test the following methods:
 <ul>
 <li>QSYSObjectPathName.setPath
 <li>QSYSObjectPathName.setLibraryName
 <li>QSYSObjectPathName.setObjectName
 <li>QSYSObjectPathName.setObjectType
 <li>QSYSObjectPathName.setMemberName
 <li>QSYSObjectPathName.getPath
 <li>QSYSObjectPathName.getLibraryName
 <li>QSYSObjectPathName.getObjectName
 <li>QSYSObjectPathName.getObjectType
 <li>QSYSObjectPathName.getMemberName
 </ul>
 Note that many test of getters do not have their own variations, but are tested through verification of the setters.
 **/
public class PNAttributesTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PNAttributesTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PNTest.main(newArgs); 
   }
    /**
     Use setPath passing a null parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var001()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setPath(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "path");
        }
    }

    /**
     Use setLibraryName passing a null parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var002()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setLibraryName(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "libraryName");
        }
    }

    /**
     Use setObjectName passing a null parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var003()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setObjectName(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "objectName");
        }
    }

    /**
     Use setObjectType passing a null parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var004()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setObjectType(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "objectType");
        }
    }

    /**
     Use setMemberName passing a null parm.
     A NullPointerException should be thrown, identifying the null parm.
     **/
    public void Var005()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setMemberName(null);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionIs(e, "NullPointerException", "memberName");
        }
    }

    /**
     Use setPath passing a path which does not start with "/QSYS.LIB/".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var006()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/NOTQSYS.LIB/MyLib.lib/Obj.obj";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.QSYS_PREFIX_MISSING);
        }
    }

    /**
     Use setPath passing a path in which the library that is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var007()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/LONGLIBRARY.LIB/Object.obj";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.LIBRARY_LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path which a library qualifier other than ".LIB".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var008()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.ME/MYOBJ.OBJ";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.LIBRARY_SPECIFICATION_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path which has no library qualifier ".LIB".
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var009()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB/MyObj.OBJ";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.LIBRARY_SPECIFICATION_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path in which the object that is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var010()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.LIB/2LongObject.OBJ";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.OBJECT_LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path in which the type that is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var011()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.LIB/TRY.TOOLONG";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.TYPE_LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path which has no object type qualifier.
     An IllegalPathNameException should be thrown, verify the rc and parm name.
     **/
    public void Var012()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.LIB/MYOBJ";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.TYPE_LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path in which the member that is too long.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var013()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.LIB/MYFILE.FILE/2LongMember.MBR";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.MEMBER_LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path in which is of type MBR, but which has no FILE.
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var014()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_path = "/QSYS.LIB/MYLIB.LIB/MEMBER.MBR";
        try
        {
            pn.setPath(invalid_path);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "IllegalPathNameException", invalid_path, IllegalPathNameException.MEMBER_WITHOUT_FILE);
        }
    }

    /**
     Use setLibraryName passing a library that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var015()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_lib = "LongLibrary";
        try
        {
            pn.setLibraryName(invalid_lib);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "libraryName (" + invalid_lib + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setObjectName passing a object that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var016()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_obj = "THISIS2LONG";
        try
        {
            pn.setObjectName(invalid_obj);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "objectName (" + invalid_obj + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setObjectType passing a type that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var017()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_type = "TOOLONG";
        try
        {
            pn.setObjectType(invalid_type);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "objectType (" + invalid_type + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setMemberName passing a member that is too long.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var018()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_obj = "THISIS2LONG";
        try
        {
            pn.setMemberName(invalid_obj);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "memberName (" + invalid_obj + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setLibraryName passing a library that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var019()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_lib = "";
        try
        {
            pn.setLibraryName(invalid_lib);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "libraryName (" + invalid_lib + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setObjectName passing a object that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var020()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_obj = "";
        try
        {
            pn.setObjectName(invalid_obj);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "objectName (" + invalid_obj + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setObjectType passing a type that is too short.
     An ExtendedIllegalArgumentException should be thrown, verify the rc and parm name.
     **/
    public void Var021()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String invalid_type = "";
        try
        {
            pn.setObjectType(invalid_type);
            failed("Expected exception did not occur.");
        }
        catch (Exception e)
        {
            assertExceptionStartsWith(e, "ExtendedIllegalArgumentException", "objectType (" + invalid_type + ")", ExtendedIllegalArgumentException.LENGTH_NOT_VALID);
        }
    }

    /**
     Use setPath passing a path in mixed case, representing an object that is not a member.
     Verify object values by calling all getters.
     All values should be uppercased, getMemberName() should return an empty string.
     **/
    public void Var022()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/Mylib.LIB/myobj.FILE";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("MYLIB"))
            {
                failed("Mismatched library: " + pn.getLibraryName());
            }
            else
            {
                if (!(pn.getObjectName()).equals("MYOBJ"))
                {
                    failed("Mismatched object: " + pn.getObjectName());
                }
                else
                {
                    if (!(pn.getObjectType()).equals("FILE"))
                    {
                        failed("Mismatched type: " + pn.getObjectType());
                    }
                    else
                    {
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                        {
                            failed("Mismatched path: " + pn.getPath());
                        }
                        else
                        {
                            if (!pn.getMemberName().equals(""))
                            {
                                failed("Mismatched member: " + pn.getMemberName());
                            }
                            else
                            {
                                succeeded();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Use setPath passing a path in mixed case, representing an object that is a member.
     Verify object values by calling all getters.
     All values should be uppercased, getObjectType() should return "MBR".
     **/
    public void Var023()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/Mylib.LIB/myobj.FILE/Mymem.MBR";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("MYLIB"))
            {
                failed("Mismatched library: " + pn.getLibraryName());
            }
            else
            {
                if (!(pn.getObjectName()).equals("MYOBJ"))
                {
                    failed("Mismatched object: " + pn.getObjectName());
                }
                else
                {
                    if (!(pn.getObjectType()).equals("MBR"))
                    {
                        failed("Mismatched type: " + pn.getObjectType());
                    }
                    else
                    {
                        if (!(pn.getPath()).equals(s.toUpperCase()))
                        {
                            failed("Mismatched path: " + pn.getPath());
                        }
                        else
                        {
                            if (!(pn.getMemberName()).equals("MYMEM"))
                            {
                                failed("Mismatched member: " + pn.getMemberName());
                            }
                            else
                            {
                                succeeded();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Use setLibraryName passing a valid library, 10 characters long in mixed case.
     Verify the value by doing getLibraryName().
     Value should be as set, except uppercased.
     **/
    public void Var024()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "OneLibrary";
        try
        {
            pn.setLibraryName(s);
            if (!(pn.getLibraryName()).equals("ONELIBRARY"))
            {
                failed("Mismatched library: " + pn.getLibraryName());
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
     Use setObjectName passing a valid object, 10 characters long in mixed case.
     Verify the value by doing getObjectName().
     Value should be as set, except uppercased.
     **/
    public void Var025()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "ThisObject";
        try
        {
            pn.setObjectName(s);
            if (!(pn.getObjectName()).equals("THISOBJECT"))
            {
                failed("Mismatched object: " + pn.getObjectName());
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
     Use setObjectType passing a valid type, 6 characters long in mixed case.
     Verify the value by doing getObjectType().
     Value should be as set, except uppercased.
     **/
    public void Var026()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "ObjTyp";
        try
        {
            pn.setObjectType(s);
            if (!(pn.getObjectType()).equals("OBJTYP"))
            {
                failed("Mismatched type: " + pn.getObjectType());
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
     Use setMemberName passing a valid member, 10 characters long in mixed case.
     Verify  the value by doing getMemberName().
     Value should be as set, except uppercased.
     Do set on an object that is not a member.  After setMemberName(), verify getObjectType() returns "MBR".
     **/
    public void Var027()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "MemberName";
        try
        {
            pn.setObjectType("FILE"); // Set object as not a member
            pn.setMemberName(s);
            if (!(pn.getMemberName()).equals("MEMBERNAME"))
            {
                failed("Mismatched member: " + pn.getMemberName());
            }
            else
            {
                if (!(pn.getObjectType()).equals("MBR"))
                {
                    failed("Mismatched type: " + pn.getObjectType());
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
    }

    /**
     Use setLibraryName passing a valid library, 1 characters long in mixed case.
     Verify the value by doing getLibraryName().
     Value should be as set, except uppercased.
     **/
    public void Var028()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "a";
        try
        {
            pn.setLibraryName(s);
            if (!(pn.getLibraryName()).equals("A"))
            {
                failed("Mismatched library: " + pn.getLibraryName());
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
     Use setObjectName passing a valid object, 1 characters long in mixed case.
     Verify the value by doing getObjectName().
     Value should be as set, except uppercased.
     **/
    public void Var029()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "a";
        try
        {
            pn.setObjectName(s);
            if (!(pn.getObjectName()).equals("A"))
            {
                failed("Mismatched object: " + pn.getObjectName());
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
     Use setObjectType passing a valid type, 1 characters long in mixed case.
     Verify the value by doing getObjectType().
     Value should be as set, except uppercased.
     **/
    public void Var030()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "a";
        try
        {
            pn.setObjectType(s);
            if (!(pn.getObjectType()).equals("A"))
            {
                failed("Mismatched type: " + pn.getObjectType());
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
     Use setMemberName passing a valid member, 1 characters long in mixed case.
     Verify  the value by doing getMemberName().
     Value should be as set, except uppercased.
     Do set on an object that is not a member.  After setMemberName(), verify getObjectType() returns "MBR".
     **/
    public void Var031()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "a";
        try
        {
            pn.setObjectType("FILE"); // Set object as not a member
            pn.setMemberName(s);
            if (!(pn.getMemberName()).equals("A"))
            {
                failed("Mismatched member: " + pn.getMemberName());
            }
            else
            {
                if (!(pn.getObjectType()).equals("MBR"))
                {
                    failed("Mismatched type: " + pn.getObjectType());
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
    }

    /**
     Use setMemberName passing an empty string.
     getMemberName() should return an empty string.
     **/
    public void Var032()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setMemberName("");
            if (pn.getMemberName().equals(""))
            {
                succeeded();
            }
            else
            {
                failed("Mismatched member: " + pn.getMemberName());
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Use setPath passing a quoted path in mixed case, representing an object that is not a member.
     Verify object values by calling all getters.
     All values should be as input, getMemberName() should return an empty string.
     **/
    public void Var033()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/\"MyLib\".LIB/\"Myobj\".OBJ";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("\"MyLib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"Myobj\""))
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
     Use setPath passing a quoted path in mixed case, representing an object that
     is a member.
     Verify object values by calling all getters.
     All values should be as input, getObjectType() should return "MBR".
     **/
    public void Var034()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/\"Mylib\".LIB/\"myobj\".FILE/\"Mymem\".MBR";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("\"Mylib\""))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("\"myobj\""))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getPath()).equals(s))
                            failed("Mismatched path: " + pn.getPath());
                        else
                            if (!(pn.getMemberName()).equals("\"Mymem\""))
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
     Use setLibraryName passing a valid library, in quoted mixed case.
     Verify the value by doing getLibraryName().
     Value should be as set.
     **/
    public void Var035()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "\"TheLib\"";
        try
        {
            pn.setLibraryName(s);
            if (!(pn.getLibraryName()).equals(s))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Use setObjectName passing a valid object, in quoted mixed case.
     Verify the value by doing getObjectName().
     Value should be as set.
     **/
    public void Var036()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "\"TheObj\"";
        try
        {
            pn.setObjectName(s);
            if (!(pn.getObjectName()).equals(s))
                failed("Mismatched object: " + pn.getObjectName());
            else
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Use setMemberName passing a valid member, in quoted mixed case.
     Verify  the value by doing getMemberName().
     Value should be as set.
     Do set on an object that is not a member.  After setMemberName(), verify
     getObjectType() returns "MBR".
     **/
    public void Var037()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "\"TheMbr\"";
        try
        {
            pn.setObjectType("FILE"); // Set type as not a member
            pn.setMemberName(s);
            if (!(pn.getMemberName()).equals(s))
                failed("Mismatched member: " + pn.getMemberName());
            else
                if (!(pn.getObjectType()).equals("MBR"))
                    failed("Mismatched type: " + pn.getObjectType());
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
     Use setPath passing a path which names have embedded periods,
     representing an object that is not a member.
     Verify object values by calling all getters.
     **/
    public void Var038()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/My.Library.LIB/my.object.OBJ";
        try
        {
            pn.setPath(s);
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
     Use setPath passing a path which names have embedded periods,
     representing an object that is a member.
     Verify object values by calling all getters.
     **/
    public void Var039()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/My.libr.LIB/My.object.FILE/My.member.MBR";
        try
        {
            pn.setPath(s);
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
     Use setPath passing a path which names have embedded slashes,
     representing an object that is not a member.
     Verify object values by calling all getters.
     **/
    public void Var040()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/My/Library.LIB/my/object.OBJ";
        try
        {
            pn.setPath(s);
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
     Use setPath passing a path which names have embedded slashes,
     representing an object that is a member.
     Verify object values by calling all getters.
     **/
    public void Var041()
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
     Use setPath using each of the special library values (like %LIBL%).
     Verify object values by calling all getters.
     All values should be as input,
     getLibraryName() should return translated special values (like *LIBL).
     **/
    public void Var042()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s1 = "/QSYS.LIB/%libl%.LIB/OBJ1.FILE/MBR1.MBR";
        String s2 = "/QSYS.LIB/%curlib%.LIB/OBJ1.FILE/MBR1.MBR";
        String s3 = "/QSYS.LIB/%usrlibl%.LIB/OBJ1.FILE/MBR1.MBR";
        String s4 = "/QSYS.LIB/%all%.LIB/OBJ1.FILE/MBR1.MBR";
        String s5 = "/QSYS.LIB/%allusr%.LIB/OBJ1.FILE/MBR1.MBR";
        boolean failed = true;
        try
        {
            pn.setPath(s1);
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
                pn.setPath(s2);
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
                pn.setPath(s3);
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
                pn.setPath(s4);
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
                pn.setPath(s5);
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
     Use setPath using each of the special object values (like %ALL%).
     Verify object values by calling all getters.
     All values should be as input,
     getObjectName() should return translated special values (like *ALL).
     **/
    public void Var043()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s1 = "/QSYS.LIB/MYLIB.LIB/%all%.FILE/MBR1.MBR";
        try
        {
            pn.setPath(s1);
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
     Use setPath using each of the special member values (like %FIRST%).
     Verify object values by calling all getters.
     All values should be as input,
     getMemberName() should return translated special values (like *FIRST).
     **/
    public void Var044()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s1 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%first%.MBR";
        String s2 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%last%.MBR";
        String s3 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%file%.MBR";
        String s4 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%all%.MBR";
        String s5 = "/QSYS.LIB/MYLIB.LIB/OBJ1.FILE/%none%.MBR";
        boolean failed = true;
        try
        {
            pn.setPath(s1);
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
                pn.setPath(s2);
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
                pn.setPath(s3);
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
                pn.setPath(s4);
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
                pn.setPath(s5);
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
     Use setLibraryName passing each of the special library values (like *LIBL),
     Verify the value by doing getLibraryName().
     Value should be as set, but uppercased.
     getPath() should have the special value in the %...% form.
     **/
    public void Var045()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s1 = "*libl";
        String s2 = "*curlib";
        String s3 = "*usrlibl";
        String s4 = "*all";
        String s5 = "*allusr";
        boolean failed = true;
        try
        {
            pn.setPath("/qsys.lib/library.lib/object.obj");
            pn.setLibraryName(s1);
            if (!(pn.getLibraryName()).equals(s1.toUpperCase()))
                failed("Mismatched library: " + pn.getLibraryName());
            else if (!(pn.getPath()).equals("/QSYS.LIB/%LIBL%.LIB/OBJECT.OBJ"))
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
            failed = true;
            try
            {
                pn.setLibraryName(s2);
                if (!(pn.getLibraryName()).equals(s2.toUpperCase()))
                    failed("Mismatched library: " + pn.getLibraryName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/%CURLIB%.LIB/OBJECT.OBJ"))
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
            failed = true;
            try
            {
                pn.setLibraryName(s3);
                if (!(pn.getLibraryName()).equals(s3.toUpperCase()))
                    failed("Mismatched library: " + pn.getLibraryName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/%USRLIBL%.LIB/OBJECT.OBJ"))
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
            failed = true;
            try
            {
                pn.setLibraryName(s4);
                if (!(pn.getLibraryName()).equals(s4.toUpperCase()))
                    failed("Mismatched library: " + pn.getLibraryName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/%ALL%.LIB/OBJECT.OBJ"))
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
            failed = true;
            try
            {
                pn.setLibraryName(s5);
                if (!(pn.getLibraryName()).equals(s5.toUpperCase()))
                    failed("Mismatched library: " + pn.getLibraryName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/%ALLUSR%.LIB/OBJECT.OBJ"))
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
     Use setObjectName passing each of the special object values (like *ALL),
     Verify the value by doing getObjectName().
     Value should be as set, but uppercased.
     getPath() should have the special value in the %...% form.
     **/
    public void Var046()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setPath("/qsys.lib/library.lib/object.obj");
            pn.setObjectName("*all");
            if (!(pn.getObjectName()).equals("*ALL"))
                failed("Mismatched object: " + pn.getObjectName());
            else if (!(pn.getPath()).equals("/QSYS.LIB/LIBRARY.LIB/%ALL%.OBJ"))
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
     Use setMemberName passing each of the special member values (like *FIRST),
     Verify  the value by doing getMemberName().
     Value should be as set, but uppercased.
     getPath() should have the special value in the %...% form.
     Do set on an object that is not a member.  After setMemberName(), verify
     getObjectType() returns "MBR".
     **/
    public void Var047()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s1 = "*first";
        String s2 = "*last";
        String s3 = "*file";
        String s4 = "*all";
        boolean failed = true;
        try
        {
            pn.setPath("/qsys.lib/library.lib/object.obj");
            pn.setMemberName(s1);
            if (!(pn.getMemberName()).equals(s1.toUpperCase()))
                failed("Mismatched member: " + pn.getMemberName());
            else if (!(pn.getPath()).equals("/QSYS.LIB/LIBRARY.LIB/OBJECT.FILE/%FIRST%.MBR"))
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
            failed = true;
            try
            {
                pn.setMemberName(s2);
                if (!(pn.getMemberName()).equals(s2.toUpperCase()))
                    failed("Mismatched member: " + pn.getMemberName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/LIBRARY.LIB/OBJECT.FILE/%LAST%.MBR"))
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
            failed = true;
            try
            {
                pn.setMemberName(s3);
                if (!(pn.getMemberName()).equals(s3.toUpperCase()))
                    failed("Mismatched member: " + pn.getMemberName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/LIBRARY.LIB/OBJECT.FILE/%FILE%.MBR"))
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
            failed = true;
            try
            {
                pn.setMemberName(s4);
                if (!(pn.getMemberName()).equals(s4.toUpperCase()))
                    failed("Mismatched member: " + pn.getMemberName());
                else if (!(pn.getPath()).equals("/QSYS.LIB/LIBRARY.LIB/OBJECT.FILE/%ALL%.MBR"))
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



    /////// QSYS

    /**
     Use setPath passing a path representing an object that
     is not a member, using "/QSYS.LIB/QSYS.LIB"...
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var048()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/QSYS.LIB/MYOBJ.OBJ";
        try
        {
            pn.setPath(s);
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
     Use setPath passing a path representing an object that
     is a member, using "/QSYS.LIB/QSYS.LIB"...
     An IllegalPathNameException should be thrown, verify the rc.
     **/
    public void Var049()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/QSYS.LIB/MYOBJ.FILE/MYMBR.MBR";
        try
        {
            pn.setPath(s);
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
     Use setPath passing a valid path representing an object that is in QSYS and
     is not a member.
     Verify the call completes successfuly, and getPath and getLibrary return
     correct values.
     **/
    public void Var050()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/qsys.lib/myobj.obj";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("QSYS"))
                failed("Mismatched library: " + pn.getLibraryName());
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
     Use setPath passing a valid path representing an object that is in QSYS and
     is a member.
     Verify the call completes successfuly, and getPath and getLibrary return
     correct values.
     **/
    public void Var051()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/Qsys.LIB/MyObj.file/MYmbr.Mbr";
        try
        {
            pn.setPath(s);
            if (!(pn.getLibraryName()).equals("QSYS"))
                failed("Mismatched library: " + pn.getLibraryName());
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
     Use setLibraryName("qsys").
     Verify the path returned by getPath() is correct.
     **/
    public void Var052()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/qsys.lib/MyObj.file/MyMbr.mbr";
        try
        {
            pn.setPath(s);
            pn.setLibraryName("qsys");
            if ((pn.getPath()).equals("/QSYS.LIB/MYOBJ.FILE/MYMBR.MBR"))
                succeeded();
            else
                failed("Mismatched path: " + pn.getPath());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }




    /////// Incomplete paths

    /**
     Attempt getPath() when the library is not yet set.
     An empty string should be returned.
     **/
    public void Var053()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setObjectName("MYOBJ");
            pn.setObjectType("OBJ");
            if (pn.getPath().equals(""))
                succeeded();
            else
                failed("Wrong path returned: " + pn.getPath());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Attempt getPath() when the object is not yet set.
     An empty string should be returned.
     **/
    public void Var054()
    {
      QSYSObjectPathName pn = new QSYSObjectPathName();
      try
      {
        pn.setLibraryName("MYLIB");
        pn.setObjectType("OBJ");
        // 2008-04-15 @A1
        // Changed this variation due to changes in QSYSObjectPathName which will 
        // generate a path even though the objectName may not be specified.
        // In the case of this variation, it will generate "/QSYS.LIB/MYLIB.LIB"
        // This is to more compatible with what would happen if you performed the
        // following:
        //   pn1.setPath("/QSYS.LIB/XYZ.LIB");
        //   pn1.getPath()        ==> /QSYS.LIB/XYZ.LIB
        //   pn1.getLibraryName() ==> QSYS
        //   pn1.getObjectName()  ==> XYZ
        //   pn1.getObjectType()  ==> LIB
        if (!pn.getPath().equals(""))   //@A1 - Add NOT operator
          succeeded();
        else
          failed("Wrong path returned: " + pn.getPath());
      }
      catch (Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     Attempt getPath() when the type is MBR and member is not yet set.
     An empty string should be returned.
     **/
    public void Var055()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setLibraryName("MYLIB");
            pn.setObjectType("MBR");
            pn.setObjectName("MYOBJ");
            if (pn.getPath().equals(""))
                succeeded();
            else
                failed("Wrong path returned: " + pn.getPath());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }



    /////// Misc

    /**
     Verifying setting the object type to be something other than MBR will
     remove any member name.
     **/
    public void Var056()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setLibraryName("MYLIB");
            pn.setObjectType("MBR");
            pn.setObjectName("MYOBJ");
            pn.setMemberName("MYMBR");
            pn.setObjectType("OBJ");
            if (pn.getMemberName().equals(""))
                succeeded();
            else
                failed("Wrong member: " + pn.getMemberName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify that doing a setPath using a path which does not represent a
     member will remove any member name.
     **/
    public void Var057()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        try
        {
            pn.setLibraryName("MYLIB");
            pn.setObjectType("MBR");
            pn.setObjectName("MYOBJ");
            pn.setMemberName("MYMBR");

            pn.setPath("/QSYS.LIB/THELIB.LIB/THEOBJ.OBJ");

            if (pn.getMemberName().equals(""))
                succeeded();
            else
                failed("Wrong member: " + pn.getMemberName());
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify that doing a setPath on an object that is already 'complete'
     updates all the attributes, including the path.
     **/
    public void Var058()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/THElib.LIB/THEobj.File/THEmbr.mbr";
        try
        {
            pn.setLibraryName("DIFLIB");
            pn.setObjectType("mbr");
            pn.setObjectName("DIFOBJ");
            pn.setMemberName("DIFmbr");
            if (pn.getPath().equals("")) // Do a get path to ensure validity;
                failed("Setup failed");
            else
            {
                pn.setPath(s);
                if (!(pn.getLibraryName()).equals("THELIB")) // Originally DIFLIB
                    failed("Mismatched library: " + pn.getLibraryName());
                else
                    if (!(pn.getObjectName()).equals("THEOBJ"))  // Originally DIFOBJ
                        failed("Mismatched object: " + pn.getObjectName());
                    else
                        if (!(pn.getObjectType()).equals("MBR"))     // Originally OBJ
                            failed("Mismatched type: " + pn.getObjectType());
                        else
                            if (!(pn.getMemberName()).equals("THEMBR"))  // Originally null
                                failed("Mismatched member: " + pn.getMemberName());
                            else
                                if (!(pn.getPath()).equals(s.toUpperCase()))
                                    failed("Mismatched path: " + pn.getPath());
                                else
                                    succeeded();
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
     Verify that doing a setLibrary on an object that is already 'complete'
     updates all the attributes, including the path.
     **/
    public void Var059()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/THELIB.LIB/THEOBJ.FILE/THEMBR.MBR";
        try
        {
            pn.setPath(s);

            pn.setLibraryName("NEWLIB");
            if (!(pn.getLibraryName()).equals("NEWLIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("THEOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getMemberName()).equals("THEMBR"))
                            failed("Mismatched member: " + pn.getMemberName());
                        else
                            if (!(pn.getPath()).equals("/QSYS.LIB/NEWLIB.LIB/THEOBJ.FILE/THEMBR.MBR"))
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
     Verify that doing a setObject on an object that is already 'complete'
     updates all the attributes, including the path.
     **/
    public void Var060()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/THELIB.LIB/THEOBJ.FILE/THEMBR.MBR";
        try
        {
            pn.setPath(s);

            pn.setObjectName("NEWOBJ");
            if (!(pn.getLibraryName()).equals("THELIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("NEWOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getMemberName()).equals("THEMBR"))
                            failed("Mismatched member: " + pn.getMemberName());
                        else
                            if (!(pn.getPath()).equals("/QSYS.LIB/THELIB.LIB/NEWOBJ.FILE/THEMBR.MBR"))
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
     Verify that doing a setMember on an object that is already 'complete'
     updates all the attributes, including the path.
     **/
    public void Var061()
    {
        QSYSObjectPathName pn = new QSYSObjectPathName();
        String s = "/QSYS.LIB/THELIB.LIB/THEOBJ.FILE/THEMBR.MBR";
        try
        {
            pn.setPath(s);

            pn.setMemberName("NEWMBR");
            if (!(pn.getLibraryName()).equals("THELIB"))
                failed("Mismatched library: " + pn.getLibraryName());
            else
                if (!(pn.getObjectName()).equals("THEOBJ"))
                    failed("Mismatched object: " + pn.getObjectName());
                else
                    if (!(pn.getObjectType()).equals("MBR"))
                        failed("Mismatched type: " + pn.getObjectType());
                    else
                        if (!(pn.getMemberName()).equals("NEWMBR"))
                            failed("Mismatched member: " + pn.getMemberName());
                        else
                            if (!(pn.getPath()).equals("/QSYS.LIB/THELIB.LIB/THEOBJ.FILE/NEWMBR.MBR"))
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
