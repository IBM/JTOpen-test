///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDGetTablePrivileges3.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.DMD;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDDMDTest;
import test.JDTestDriver;
import test.JDTestcase;



/**
Testcase JDDMDGetTablePrivileges3.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>getTablePrivileges()
</ul>
**/
public class JDDMDGetTablePrivileges3
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDDMDGetTablePrivileges3";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDDMDTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    StringBuffer message = new StringBuffer();



/**
Constructor.
**/
    public JDDMDGetTablePrivileges3 (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDDMDGetTablePrivileges3",
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
         connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);

        Statement s = connection_.createStatement ();
	String[] dropTables = {
	    JDDMDTest.COLLECTION + ".TPRIVS ",
	    JDDMDTest.COLLECTION + ".TPRIVS1 ",
	    JDDMDTest.COLLECTION + ".TPRIVS2 ",
	    JDDMDTest.COLLECTION + ".TPRIVSXX ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS3 ",
	    JDDMDTest.COLLECTION2 + ".TPRIVS4 ",
	    JDDMDTest.COLLECTIONXX + ".TPRIVSXX ",
	};

	for (int i = 0; i < dropTables.length; i++) {
	    try {
		s.executeUpdate("DROP TABLE "+dropTables[i]);
	    } catch (SQLException e) {
		int errorCode = e.getErrorCode();
		if (errorCode == -204) {
		} else {
		    System.out.println("Unexpected Exception errorCode="+errorCode);
		    e.printStackTrace();
		}
	    }
	}
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS1 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS2 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVSXX (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS3 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS4 (NAME INTEGER)");
        s.executeUpdate ("CREATE TABLE " + JDDMDTest.COLLECTIONXX
            + ".TPRIVSXX (NAME INTEGER)");

        // @128sch
        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0)
        {
            String sql = "CREATE TABLE " + JDDMDTest.SCHEMAS_LEN128
                + ".TABLE1 (COL1 CHAR(15) DEFAULT 'DEFAULTVAL',"
                + " COL2 CHAR(15) )";
            try{
                s.executeUpdate(sql);
            }
            catch(Exception e){
                System.out.println("Warning.. create table failed " + sql);
                e.printStackTrace();
            }
        }

        closedConnection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        closedConnection_.close ();

    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
        throws Exception
    {
        Statement s = connection_.createStatement ();

        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS1");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVS2");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION
            + ".TPRIVSXX");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS3");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTION2
            + ".TPRIVS4");
        s.executeUpdate ("DROP TABLE " + JDDMDTest.COLLECTIONXX
            + ".TPRIVSXX");

        if(getRelease() >= JDTestDriver.RELEASE_V7R1M0) //@128sch
        {
            try{
                s.executeUpdate("DROP TABLE " + JDDMDTest.SCHEMAS_LEN128 + ".TABLE1");
            }
            catch(Exception e){
                System.out.println("Warning.. drop table failed");
                e.printStackTrace();
            }
        }

        s.close ();
        connection_.close ();
    }



    public boolean check( StringBuffer message1, String info, String a, String b ) {
      boolean result;
      if (a == null) {
          result = ( b == null);
      } else {
          result = a.equals(b);
      }
      if (!result) {
          message1.append(info+"=\""+a+"\" sb \""+b+"\" \n");
      }
      return result;
}




    public void Var001() { notApplicable(); }
    public void Var002() { notApplicable(); }


/**
getTablePrivileges() - Specify all null parameters.  Verify that all tables
come back in the library list are returned.

Note:  When calling the SYSIBM procedures, this test now takes a long time
to run (about 20 minutes on V6R1).  Don't run for native driver..

**/
    public void Var003()
    {
	if (checkNotGroupTest())
        try {

            Connection c;
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              c = testDriver_.getConnection (baseURL_,
                  userId_, encryptedPassword_);

            } else {
              c = testDriver_.getConnection (baseURL_
                + ";libraries=*LIBL,QIWS," + JDDMDTest.COLLECTION + ","
                + JDDMDTest.COLLECTION2 + "," + JDDMDTest.COLLECTIONXX,
                userId_, encryptedPassword_);
            }
            DatabaseMetaData dmd = c.getMetaData ();
            ResultSet rs = dmd.getTablePrivileges (null, null, null);

            // It is impossible to check that all tables come back,
            // so we just check that at least some of them come back.
            boolean check1 = false;
            boolean check2 = false;
            boolean check3 = false;
            int rows = 0;
            while (rs.next ()) {
                ++rows;
                String table = rs.getString ("TABLE_SCHEM")
                    + "." + rs.getString ("TABLE_NAME");
                if (table.equals (JDDMDTest.COLLECTION + ".TPRIVS"))
                    check1 = true;
                else if (table.equals (JDDMDTest.COLLECTION2 + ".TPRIVS3"))
                    check2 = true;
                else if (table.equals ("QIWS.QCUSTCDT"))
                    check3 = true;
            }

            rs.close ();
            c.close ();
            //@C0
            //With null passed in for the tablenamepattern the native driver
            //will not find any matches
            if (getDriver () == JDTestDriver.DRIVER_NATIVE && ! isSysibmMetadata() )
            {
                assertCondition (rows == 0, "null passed for tablename pattern, but rows found");
            }
            else
            {
            assertCondition (check1 && check2 && check3);
        }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



}





