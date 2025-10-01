///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSCursorSensitivity.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.*;

import test.JDRSTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;



/**
Testcase JDRSCursorSensitivity.  This tests the cursor sensitivity connection property along with the following
types of the JDBC ResultSet class:

<ul>
<li>TYPE_FORWARD_ONLY
<li>TYPE_SCROLL_INSENSITIVE
<li>TYPE_SCROLL_SENSITIVE
</ul>
**/
public class JDRSCursorSensitivity
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSCursorSensitivity";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private Connection          connectionDefault_;
    private Connection          connectionAsensitive_;
    private Connection          connectionSensitive_;
    private Connection          connectionInsensitive_;



/**
Constructor.
**/
    public JDRSCursorSensitivity (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDRSCursorSensitivity",
               namesAndVars, runMode, fileOutputStream,
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
    throws Exception
    {
        if (isJdbc20 ()) 
        {
            connectionDefault_ = testDriver_.getConnection (baseURL_ + ";data truncation=true", userId_, encryptedPassword_,"JDRSCursorSensitivity1");
            connectionAsensitive_ = testDriver_.getConnection (baseURL_ + ";data truncation=true;cursor sensitivity=asensitive", userId_, encryptedPassword_,"JDRSCursorSensitivity2");
            connectionSensitive_ = testDriver_.getConnection (baseURL_ + ";data truncation=true;cursor sensitivity=sensitive", userId_, encryptedPassword_,"JDRSCursorSensitivity3");
            connectionInsensitive_ = testDriver_.getConnection (baseURL_ + ";data truncation=true;cursor sensitivity=insensitive", userId_, encryptedPassword_,"JDRSCursorSensitivity4");
        }
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
      cleanupConnections(); 
    }

    public void cleanupConnections() throws Exception {
      
      if (isJdbc20 ()) 
      {
          connectionDefault_.close ();
          connectionAsensitive_.close();
          connectionSensitive_.close();
          connectionInsensitive_.close();
      }
    }

/**
The result set should be able to scroll forward if TYPE_FORWARD_ONLY
Note:  Toolbox opens a nonscrollable asensitive cursor
**/
    public void Var001 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                Statement statement_ = connectionDefault_.createStatement (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                
                boolean success1 = rs.next();
                boolean success2 = rs.next();
                rs.close ();
                statement_.close();
                assertCondition (success1 == true && success2 == true);
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
        }
    }

/**
Exeption should be thrown if try to jump ahead more than one row if TYPE_FORWARD_ONLY
**/
    public void Var002 ()
    {
	if (checkJdbc20()) {
	    Statement statement_ = null;
      ResultSet rs = null;
      try 
      {                      
          statement_ = connectionDefault_.createStatement (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
          rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );

          rs.next();
          rs.absolute(3);
          rs.close ();
          failed("Didn't throw SQLException - New testcase created by toolbox 5/15/2003");
      }
      catch (Exception e) 
      {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          try
          {
      	rs.close();
      	statement_.close();
          }
          catch (Exception c) {}
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable sensitive cursor
**/
    public void Var003 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(3);
          rs.getInt(1);

          Statement statement2_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(3);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(3);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
Note:  Toolbox opens a scrollable sensitive cursor
**/
    public void Var004 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(4);
          rs.getInt(1);

          Statement statement2_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(4);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(4);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Shouldn't show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
Note:  Toolbox opens a scrollable insensitive cursor
**/
    public void Var005 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(5);
          int before = rs.getInt(1);

          Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(5);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(5);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == before, "after is " + after + ", before is " + before + ", after should be " + before);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_READ_ONLY
**/
    public void Var006 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          // Note.. connection_ is default - which is asenstive.  Must use the sensitive one. 
          Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(6);
          rs.getInt(1);

          Statement statement2_ = connectionDefault_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(6);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(6);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be " + 154);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity connection property is equal to insensitive
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable sensitive cursor
**/
    public void Var007 ()
    {
	if (checkJdbc20()) {
	    try 
      {
          Statement statement_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(7);
          rs.getInt(1);

          Statement statement2_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(7);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(7);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Note:  Toolbox opens a scrollable sensitive cursor
**/
    public void Var008 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(8);
          rs.getInt(1);

          Statement statement2_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(8);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(8);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should throw an exception if try to see if changes are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Note:  Toolbox opens a non-scrollable insensitive cursor
**/
    public void Var009 ()
    {
	if (checkJdbc20 ()) {
	    Statement statement_ = null;
      ResultSet rs = null;
      try 
      {
          statement_ = connectionInsensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.next();
          rs.getInt(1);

          Statement statement2_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(1);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.next();
          failed("Didn't throw Exception - New testcase created by toolbox 5/15/2003.");
      }
      catch (Exception e) 
      {
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          try
          {
      	rs.close();
      	statement_.close();
          }
          catch (Exception c) {}
      }
	}
    }

/**
Shouldn't show changes that are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Toolbox opens a non-scrollable insensitive cursor
**/
    public void Var010 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionInsensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.next();
          int before = rs.getInt(1);

          Statement statement2_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(1);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          int after = rs.getInt(1);
          rs.close();
          statement_.close();
          assertCondition(after == before, "after = " + after + ", after should be " + before);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Might/Might Not show changes that are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to sensitive
Toolbox opens a non-scrollable asensitive cursor
**/
    public void Var011 ()
    {
        if (checkJdbc20 ())  {
            try 
            {
                Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                for(int i=0; i<11; i++)
                    rs.next();
                int before = rs.getInt(1);

                Statement statement2_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(1);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                int after = rs.getInt(1);
                rs.close();
                statement_.close();
                assertCondition(after == before || after == 154);
            }
            catch (Exception e) 
            {
                failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity connection property is equal to sensitive
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable sensitive cursor
**/
    public void Var012 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(12);
          rs.getInt(1);

          Statement statement2_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(12);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(12);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity property is equal to sensitive
Note:  Toolbox opens a scrollable sensitive cursor
**/
    public void Var013 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(13);
          rs.getInt(1);

          Statement statement2_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(13);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(13);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == 154, "after is " + after + ", after should be 154");
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Should throw an exception if try to see if changes are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to sensitive
Note:  Toolbox opens a non-scrollable asensitive cursor
**/
    public void Var014 ()
    {
        if (checkJdbc20 ()) 
        {
            Statement statement_ = null;
            ResultSet rs = null;
            try 
            {
                statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
                rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs.next();
                rs.getInt(1);

                Statement statement2_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(1);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                rs.beforeFirst();
                rs.next();
                failed("Didn't throw Exception - New testcase created by toolbox 5/15/2003.");
            }
            catch (Exception e) 
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                try {
                    rs.close();
                    statement_.close();
                }
                catch(Exception c) {
                }
            }
        }
    }

/**
Might/Might Not show changes that are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_READ_ONLY
cursor sensitivity property is equal to asensitive
Toolbox opens a non-scrollable asensitive cursor
**/
    public void Var015 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                Statement statement_ = connectionAsensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                for (int i =0; i<15 ; i++)
                    rs.next();
                int before = rs.getInt(1);

                Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(1);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                int after = rs.getInt(1);
                rs.close();
                statement_.close();
                assertCondition(after == before || after == 154);
            }
            catch (Exception e) 
            {
                failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
        }
    }

/**
Might/Might Not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity connection property is equal to asensitive
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable asensitive cursor
**/
    public void Var016 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                Statement statement_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs.absolute(16);
                int before = rs.getInt(1);

                Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(16);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                rs.beforeFirst();
                rs.absolute(16);
                int after = rs.getInt(1);
                rs.close ();
                statement_.close();
                assertCondition( after == 154 || after == before);
            }
            catch (Exception e) 
            {
                failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
        }
     }

/**
Might/Might Not show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity property is equal to asensitive
open -> CURSOR_SCROLLABLE_ASENSITIVE
**/
    public void Var017 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                Statement statement_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs.absolute(17);
                int before = rs.getInt(1);

                Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(17);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                rs.beforeFirst();
                rs.absolute(17);
                int after = rs.getInt(1);
                rs.close ();
                statement_.close();
                assertCondition( after == 154 || after == before);
            }
            catch (Exception e) 
            {
                failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
        }
    }

/**
Should throw an exception if try to see if changes are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to asensitive
Note:  Toolbox opens a nonscrollable asensitive cursor
**/
    public void Var018 ()
    {
        if (checkJdbc20 ()) 
        {
            Statement statement_ = null;
            ResultSet rs = null;
            try 
            {
                statement_ = connectionAsensitive_.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE); 
                rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                for(int i=0; i<18; i++)
                    rs.next();
                rs.getInt(1);

                Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
                ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                rs1.absolute(1);
                rs1.updateInt(1, 154);
                rs1.updateRow();
                rs1.close();
                statement2_.close();
                
                rs.beforeFirst();
                rs.next();
                failed("Didn't throw Exception - New testcase created by toolbox 5/15/2003.");
            }
            catch (Exception e) 
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                try
                {
                    rs.close();
                    statement_.close();
                }
                catch (Exception c) {}
            }
        }
    }

/**
Shouldn't show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity property is equal to asensitive
Toolbox opens a scrollable insensitive cursor
**/
    public void Var019 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(19);
          int before = rs.getInt(1);

          Statement statement2_ = connectionAsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(19);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(19);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == before , "after is " + after + ", after should be " + before);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }


/**
Shouldn't show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity connection property is equal to sensitive
Note:  Toolbox opens a scrollable insensitive cursor
**/
    public void Var020 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(20);
          int before = rs.getInt(1);

          Statement statement2_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(20);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(20);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == before, "after is " + after + ", after should be " + before);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Shouldn't show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity connection property is equal to insensitive
Note:  Toolbox opens a scrollable insensitive cursor
**/
    public void Var021 ()
    {
	if (checkJdbc20 ()) {
	    try 
      {
          Statement statement_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
          ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs.absolute(21);
          int before = rs.getInt(1);

          Statement statement2_ = connectionInsensitive_.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); 
          ResultSet rs1 = statement2_.executeQuery("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
          rs1.absolute(21);
          rs1.updateInt(1, 154);
          rs1.updateRow();
          rs1.close();
          statement2_.close();

          rs.beforeFirst();
          rs.absolute(21);
          int after = rs.getInt(1);
          rs.close ();
          statement_.close();
          assertCondition( after == before, "after is " + after + ", after should be " + before);
      }
      catch (Exception e) 
      {
          failed(e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
      }
	}
    }

/**
Exeption should be thrown if try to use absolute() to move from one row to the next row if TYPE_FORWARD_ONLY
**/
    public void Var022 ()
    {
        if (checkJdbc20 ()) 
        {
            Statement statement_ = null;
            ResultSet rs = null;
            try 
            {                      
                statement_ = connectionDefault_.createStatement (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                
                rs.absolute(1);
                rs.absolute(2);
                rs.close ();
                failed("Didn't throw SQLException - New testcase created by toolbox 5/15/2003");
            }
            catch (Exception e) 
            {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                try
                {
                    rs.close();
                    statement_.close();
                }
                catch (Exception c) {}
            }
        }
    }

/**
The result set should be able to scroll forward if TYPE_FORWARD_ONLY
Note:  Toolbox opens a nonscrollable asensitive cursor
**/
    public void Var023 ()
    {
        if (checkJdbc20 ()) 
        {
            try 
            {
                Statement statement_ = connectionDefault_.createStatement (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE );
                
                boolean success1 = rs.next();
                boolean success2 = rs.next();
                rs.close ();
                statement_.close();
                assertCondition (success1 == true && success2 == true);
            }
            catch (Exception e) 
            {
                failed (e, "Unexpected Exception - New testcase created by toolbox 5/15/2003");
            }
        }
     }

/**
Should throw an exception if try to see if changes are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Note:  Toolbox opens a non-scrollable asensitive cursor
**/
    public void Var024 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation");
	}
    }

/**
Might/Might not show changes that are made while the ResultSet is open if TYPE_FORWARD_ONLY and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Toolbox opens a non-scrollable asensitive cursor
**/
    public void Var025 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable asensitive cursor
**/
    public void Var026 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var027 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity connection property is equal to insensitive
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable asensitive cursor
**/
    public void Var028 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }


/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity connection property is equal to sensitive
Note:  If the concurrency is updateable, the toolbox automatically sets the type to TYPE_SCROLL_SENSITIVE
Toolbox opens a scrollable asensitive cursor
**/
    public void Var029 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity property is equal to asensitive
Toolbox opens a scrollable asensitive cursor
**/
    public void Var030 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity connection property is equal to sensitive
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var031 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY
cursor sensitivity connection property is equal to insensitive
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var032 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var033 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_READ_ONLY
Note:  If the concurrency is READ ONLY, the toolbox changes the TYPE to INSENSITVE and issues an SQLWarning
Toolbox opens a scrollable asensitive cursor
**/
    public void Var034 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");

	}
    }
/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity property is equal to insensitive
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var035 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Might/Might not show changes made to the database while ResultSet is open if TYPE_SCROLL_SENSITIVE and CONCUR_UPDATEABLE
cursor sensitivity property is equal to sensitive
Note:  Toolbox opens a scrollable asensitive cursor
**/
    public void Var036 ()
    {
	if (checkJdbc20 ()) {
	    notApplicable("V5R1 or earlier variation.");
	}
    }

/**
Test with use of the with NC clause for scroll insensitive cursors
**/
    public void Var037() {
	if (checkJdbc20 ())  {

	    try {
		Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE+" with nc" );
		rs.close();
	    // Just make sure the query succeeds 
		assertCondition(true); 
	    } catch (Exception e) 	{
		failed(e, "Unexpected Exception - New testcase created by native driver 10/22/2003");
	    }
	} 

    }


/**
Test with use of the with NC clause for scroll insensitive cursors
**/
	public void Var038() { 
	    if (checkJdbc20 ()) {
		try {
		    PreparedStatement stmt = connectionSensitive_.prepareStatement("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE+" with nc", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		    ResultSet rs = stmt.executeQuery ();
		    rs.close();
	    // Just make sure the query succeeds 
		    assertCondition(true); 
		} catch (Exception e) 	{
		    failed(e, "Unexpected Exception - New testcase created by native driver 10/22/2003");
		}
	    } 

	}

/**
Test with use of the with fetch first clause for scroll insensitive cursors
**/
    public void Var039() { 
	if (checkJdbc20 ())  {
	    try {
		Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE+" fetch first 10 rows only " );
		rs.close();
	    // Just make sure the query succeeds 
		assertCondition(true); 
	    } catch (Exception e) 	{
		failed(e, "Unexpected Exception - New testcase created by native driver 10/22/2003");
	    }
	} 
    }


/**
Test with use of the optimize for all rows for scroll insensitive cursors
**/
    public void Var040() {
	if (checkJdbc20 ())  {
	    try {
		Statement statement_ = connectionSensitive_.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		ResultSet rs = statement_.executeQuery ("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE+" optimize for all rows " );
		rs.close();
	    // Just make sure the query succeeds 
		assertCondition(true); 
	    } catch (Exception e) 	{
		failed(e, "Unexpected Exception - New testcase created by native driver 08/03/2004");
	    }
	} 
    }

/**
Test with use of the optimize for all rows for scroll insensitive cursors
**/
    public void Var041() { 
	if (checkJdbc20 ())  {
	    try {
		PreparedStatement statement_ = connectionSensitive_.prepareStatement("SELECT * FROM " + JDRSTest.RSTEST_SENSITIVE+" optimize for all rows ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); 
		ResultSet rs = statement_.executeQuery ();
		rs.close();
	    // Just make sure the query succeeds 
		assertCondition(true); 
	    } catch (Exception e) 	{
		failed(e, "Unexpected Exception - New testcase created by native driver 08/03/2004");
	    }
	} 
    }



} 
