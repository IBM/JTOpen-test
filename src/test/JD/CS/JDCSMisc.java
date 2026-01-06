///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSMisc.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSMisc.java
//
// Classes:      JDCSMisc
//
////////////////////////////////////////////////////////////////////////
//
//
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCCallableStatement;

import test.JDCSTest;
import test.JDReflectionUtil;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.JD.JDSetupPackage;
import test.JD.JDTestUtilities;



/**
Testcase JDCSMisc.  This tests the following
methods of the JDBC CallableStatement class:

<ul>
<li>clearParameters()
<li>OUT parameters and result sets
<li>getDB2ParameterName
</ul>
**/
public class JDCSMisc
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSMisc";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



    // Private data.
    private Connection          connection2_;  //@PDA
    
    private String TABLE1 = collection_ + ".JDCSMISCT1";
    private String SPBase = collection_ + ".JDCSMISCSP";

    private int spCount = 5;
    String SP1 = SPBase + "1"; //time="usa" date="usa"
    String SP2 = SPBase + "2"; //"iso" "iso"
    String SP3 = SPBase + "3"; //"eur" "eur"
    String SP4 = SPBase + "4"; //"jis" "jis"
    String SP5 = SPBase + "5"; //"hms" "iso"
    String[] SPFormats = new String[6];

/**
Constructor.
**/
    public JDCSMisc (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCSMisc",
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

	collection_ = JDCSTest.COLLECTION;

	TABLE1 = collection_ + ".JDCSMISCT1";
	SPBase = collection_ + ".JDCSMISCSP";

	SP1 = SPBase + "1"; //time="usa" date="usa"
	SP2 = SPBase + "2"; //"iso" "iso"
	SP3 = SPBase + "3"; //"eur" "eur"
	SP4 = SPBase + "4"; //"jis" "jis"
	SP5 = SPBase + "5"; //"hms" "iso"


        connection_ = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
        connection2_ = testDriver_.getConnection (baseURL_, pwrSysUserID_, pwrSysEncryptedPassword_);  //@PDA


        //@PDA table and SPs for testing date/time formats in SP with
        // formats in connection props
        Statement st = connection_.createStatement();
        try {
          st.execute("DROP TABLE " + TABLE1 );
        } catch (Exception e) {
        }
        st.execute("CREATE TABLE " + TABLE1 + " (COLTIME TIME, COLDATE DATE)");
        st.execute("insert into " + TABLE1 + " values ( '00:00:00', '12/01/1999')"); //00am //dec-01 or jan-12
        st.execute("insert into " + TABLE1 + " values ( '01:00:00', '01/12/2000')"); //dec-01 or jan-12
        st.execute("insert into " + TABLE1 + " values ( '12:00:00', '01/01/1900')");
        st.execute("insert into " + TABLE1 + " values ( '13:00:00', '01/01/2030')");
        st.execute("insert into " + TABLE1 + " values ( '24:00:00', '01/01/2030')"); //12am
        st.execute("insert into " + TABLE1 + " values ( '00:15:00', '01/15/2030')"); //12:15 AM
        st.execute("insert into " + TABLE1 + " values ( '12:15:00', '01/15/2030')"); //12:15 PM

        //two parrellel arrays of matching formats except no hms date
        String[] tmpTimeFmt = { "usa", "iso", "eur", "jis", "hms" }; //not valid in SP "mdy", "dmy", "ymd", , "julian"
        String[] tmpDateFmt = { "usa", "iso", "eur", "jis", "iso" };
        //create x stored procs with each of the dat/time formats
        for(int x = 0; x< 5; x++)
        {
            SPFormats[x] = "timFMT=*" + tmpTimeFmt[x] + ", datFMT=*" + tmpDateFmt[x];

              try {
                st.execute("DROP PROCEDURE " + SPBase + Integer.toString(x+1) );
              } catch (Exception e) {
              }

            st.execute("CREATE PROCEDURE " + SPBase + Integer.toString(x+1) + " ( ) RESULT SETS 1 "
                + " LANGUAGE SQL SET OPTION timFMT=*" + tmpTimeFmt[x] + ", datFMT=*" + tmpDateFmt[x] + " BEGIN "
                + " DECLARE sqlStatement varchar(1000); "
                + " DECLARE c1 CURSOR FOR pStmt; "
                + " SET sqlStatement = 'SELECT coltime, coldate from " + TABLE1 + "' ; "
                + " PREPARE pStmt FROM sqlStatement; "
                + " OPEN c1 ; " + " SET RESULT SETS CURSOR c1; " + " END ");
        }
        st.close();

    }



/**
 * Performs cleanup needed after running variations.
 *
 * @exception Exception
 *                If an exception occurs.
 */
    protected void cleanup ()
        throws Exception
    {
        //@PDA table and SPs for testing date/time formats in SP with
        // formats in connection props
        Statement st = connection_.createStatement();
        output_.println("st.execute(DROP TABLE " + TABLE1 );

        for(int x = 0; x < 5; x++)
        {
            st.execute("DROP PROCEDURE " + SPBase + Integer.toString(x+1) );
        }
        st.close();


        connection_.close ();
        connection_ = null; 

    }



/**
Compares two integer arrays.
**/
    boolean compare(int[] expectedArray, int[] retreivedArray, StringBuffer sb)
    {
        boolean answer = true;
        if(expectedArray.length != retreivedArray.length) {
            sb.append("expectedArray.length("+expectedArray.length+") != retreivedArray.length("+retreivedArray.length+")\n");
            answer = false;
            return false;
        }
        int top = expectedArray.length;
        if (retreivedArray.length < top) {
          top = retreivedArray.length;
        }
        for(int i=0; i<top; i++)
        {
            if(expectedArray[i] != retreivedArray[i]) {
                sb.append("i="+i+" expectedArray[i]="+expectedArray[i]+" retreivedArray[i]="+retreivedArray[i]+"\n");
                answer = false;
            }
        }

        return answer;
    }

/**
clearParameters() - Register a parameter, then clear parameters.
Check that the parameter is still registered.  This can be
checked by trying to execute the statement.
**/                                                                             // @C1C
    public void Var001()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.registerOutParameter (2, Types.INTEGER);
            cs.registerOutParameter (3, Types.INTEGER);
            cs.clearParameters ();
            cs.setInt (1, 1);
            cs.setInt (3, 3);
            boolean success = true;                                             // @C1C
            try {
                cs.execute ();
            }
            catch (SQLException e) {
                success = false;                                                // @C1C
            }
            cs.close ();
            assertCondition (success);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
clearParameters() - Clear parameters when thge statement is closed.
An exception should be thrown.
**/
    public void Var002()
    {
        try {
            CallableStatement cs = connection_.prepareCall ("CALL "
                + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
            cs.close ();
            cs.registerOutParameter (2, Types.INTEGER);
            cs.clearParameters ();
            failed ("Didn't throw SQLException");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
Call a stored procedure with OUT parameters and
result sets.
**/
  public void Var003()
  {
    try {
        CallableStatement cs = connection_.prepareCall ("CALL "
            + JDSetupProcedure.STP_CSPARMSRS + " (?, ?, ?)");
        cs.setInt (1, 5);
        cs.registerOutParameter (2, Types.INTEGER);
        cs.setInt (3, 2);
        cs.registerOutParameter (3, Types.INTEGER);
        boolean result = cs.execute ();
        int p2 = cs.getInt (2);
        int p3 = cs.getInt (3);
        ResultSet rs = cs.getResultSet ();
        rs.next ();
        rs.close ();
        boolean check1 = cs.getMoreResults ();
        rs = cs.getResultSet ();
        rs.next ();
        rs.close ();
        boolean check2 = cs.getMoreResults ();
        cs.close ();
        assertCondition ((result == true) && (check1 == true) && (check2 == false)
                && (p2 == 6) && (p3 == 3));
    }
    catch(Exception e) {
        failed (e, "Unexpected Exception");
    }
  }



/**
Call a stored procedure with OUT parameters and
result sets, when the call comes from the package
cache.
**/
  public void Var004()
  {
    try {
        String call = "CALL "
            + JDSetupProcedure.STP_CSPARMSRS + " (?, ?, ?)";
        String packageName = "JDCSMISC";
        String packageLib = collection_;

        if (isToolboxDriver())
          
          JDSetupPackage.prime (systemObject_, packageName, packageLib, call);
        else
           JDSetupPackage.prime (systemObject_, encryptedPassword_,
               packageName, packageLib, call, "", getDriver());

        Connection c2 = testDriver_.getConnection (
            baseURL_ + ";extended dynamic=true;package="
            + packageName + ";package library=" + packageLib
            + ";package cache=true", userId_, encryptedPassword_);

        CallableStatement cs = c2.prepareCall (call);
        cs.setInt (1, 5);
        cs.registerOutParameter (2, Types.INTEGER);
        cs.setInt (3, 2);
        cs.registerOutParameter (3, Types.INTEGER);
        boolean result = cs.execute ();
        int p2 = cs.getInt (2);
        int p3 = cs.getInt (3);
        ResultSet rs = cs.getResultSet ();
        rs.next ();
        rs.close ();
        boolean check1 = cs.getMoreResults ();
        rs = cs.getResultSet ();
        rs.next ();
        rs.close ();
        boolean check2 = cs.getMoreResults ();
        cs.close ();
        c2.close ();
        assertCondition ((result == true) && (check1 == true) && (check2 == false)
                && (p2 == 6) && (p3 == 3));
    }
    catch(Exception e) {
        failed (e, "Unexpected Exception");
    }
  }





/**
 * Call a CL stored procedure
 */

  public void Var005()
  {
      String clProgram[] = {
           "PGM PARM(&JOBNAME)                                               ",
           "DCL VAR(&JOBNAME) TYPE(*CHAR) LEN(28)                            ",
           "                                                                 ",
           "DCL VAR(&JOB) TYPE(*CHAR) LEN(10)                                ",
           "DCL VAR(&JOBUSER) TYPE(*CHAR) LEN(10)                            ",
           "DCL VAR(&JOBNBR) TYPE(*CHAR) LEN(6)                              ",
           "                                                                 ",
           "RTVJOBA    JOB(&JOB) USER(&JOBUSER) NBR(&JOBNBR)                 ",
           "                                                                 ",
           "CHGVAR VAR(&JOBNAME) VALUE(&JOBNBR *CAT '/' *CAT +               ",
           "                          &JOBUSER *CAT '/' *CAT &JOB)           ",
           "ENDPGM                                                           ",
	   ""
      };


      try {

	  stringArrayToSourceFile(connection_, clProgram, ""+collection_+"", "JDCSCLPGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTCLPGM PGM("+collection_+"/JDCSCLPGM) SRCFILE("+collection_+"/JDCSCLPGM)";
	  cmd.setString(1, command );
	  cmd.execute();


	  String sql = "create procedure "+collection_+".JDCSCLPGM(out name char(28)) "+
	               "language CL parameter style general " +
	               "external name '"+collection_+"/JDCSCLPGM'";

	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".jdcsclpgm");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);

	  CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSCLPGM(?)");
	  cs.registerOutParameter(1, Types.CHAR);
	  cs.execute();
	  String answer = cs.getString(1);
	  String jobName;
	  if  ( getDriver() == JDTestDriver.DRIVER_NATIVE ) {
	      jobName = "QSQSRVR";
	  } else {
	      jobName = "QZDASOINIT";
	  }

	  // cleanup
	  stmt.executeUpdate("Drop procedure "+collection_+".JDCSCLPGM");
	  stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSCLPGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  assertCondition (answer.indexOf(jobName) >= 0, "Job name "+jobName+" not found in "+answer+" -- new testcase added by native driver 12/10/03 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- new testcase added by native driver 12/10/03 ");
    }
  }


/**
 * Call a COBOL LE stored procedure
 */

  public void Var006()
  {
      if (isToolboxDriver())
      {
          //too many random failures due to misc language issues
          notApplicable();
          return;
      }
      String cobolProgram[] = {
        "      ******************************************************",
        "      *   DESCRIPTION:  This COBOL program is used as an   *",
        "      *       external procedure with parameters in.       *",
        "      ******************************************************",

        "      ******************************************************",
        "      *                  MAIN PROGRAM                      *",
        "      ******************************************************",

        "       IDENTIFICATION DIVISION.",
        "       PROGRAM-ID.  JDCSCBLPR.",
        "       ENVIRONMENT DIVISION.",
        "       CONFIGURATION SECTION.",
        "       SOURCE-COMPUTER. IBM-AS400.",
        "       OBJECT-COMPUTER. IBM-AS400.",

        "       INPUT-OUTPUT SECTION.",

        "       FILE-CONTROL.",

        "       DATA DIVISION.",

        "       FILE SECTION.",

        "       WORKING-STORAGE SECTION.",

        "            EXEC SQL INCLUDE SQLCA",
        "            END-EXEC.",
        "            EXEC SQL INCLUDE SQLDA",
        "            END-EXEC.",
        "            EXEC SQL WHENEVER SQLERROR CONTINUE",
        "            END-EXEC.",

        "       LINKAGE SECTION.",
        "       01  IN1               PIC S9(18) COMP-4.",
        "       01  IN2               PIC X(20).",


        "       PROCEDURE DIVISION",
        "             USING IN1",
        "                   IN2.",

        "              PERFORM 10-MYPROC.",
        "              GO TO 999-PGM-END.",

        "       10-MYPROC.",
        "             EXEC SQL DELETE FROM "+collection_+"/JDCSCBLTAB END-EXEC.",

        "             EXEC SQL INSERT INTO "+collection_+"/JDCSCBLTAB VALUES (",
        "                      \"PROCEDURE JDCSCBL RAN\",",
        "                      CURRENT_TIMESTAMP) END-EXEC.",

        "             EXEC SQL DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
        "                      "+collection_+"/JDCSCBLTAB END-EXEC.",

        "             EXEC SQL OPEN C1 END-EXEC.",
        "       999-PGM-END.",
        "            EXIT.",
	""
      };


      try {

	  stringArrayToSourceFile(connection_,cobolProgram, ""+collection_+"", "JDCSCBLPGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCBLI "+collection_+"/JDCSCBLPGM "+collection_+"/JDCSCBLPGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+collection_+".JDCSCBLPGM (IN P1 BIGINT, IN P2 CHAR(20)) "+
	               "dynamic result sets 1 "+
	               "EXTERNAL NAME '"+collection_+"/JDCSCBLPGM' "+
                       "LANGUAGE COBOLLE GENERAL ";


	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".JDCSCBLPGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);
	  try {
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSCLBTAB");
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table  "+collection_+".JDCSCLBTAB (info varchar(80), ts timestamp)");

	  CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSCBLPGM(1, 'hi')");
	  cs.execute();

	  // cleanup
	  stmt.executeUpdate("Drop table     "+collection_+".JDCSCLBTAB");
	  stmt.executeUpdate("Drop procedure "+collection_+".JDCSCBLPGM");
	  stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSCBLPGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  assertCondition (true, "-- new testcase added by native driver 12/10/03 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception  -- new testcase added by native driver 12/10/03 ");
    }
  }


/**
 * Call a C++ stored procedure
 */

  public void Var007()
  {

      String cppProgram[] = {
         "/********************************************************************/",
         "/* DESCRIPTION:  This C++ program is used as an external procedure  */",
         "/*               with parameters in, out, inout.                    */",
         "/*                                                                  */",
         "/********************************************************************/",
         "",
         "#include <string.h>  /* used for strcpy()                           */",
         "#include <stdlib.h>  /* standard library                            */",
         "#include <stdio.h>   /* standard I/O functions                      */",
         "",
         "main(int argc, char *argv[])",
         "{",
         "  exec sql include sqlca;",
         "  exec sql begin declare section;",
         "  short outp1;                   /* OUT PARM     */",
         "  long inp2;                     /* IN PARM      */",
         "  long long inoutp3;             /* INOUT PARM   */",
         "  short inoutp4;                 /* INOUT PARM   */",
         "  long long inp5;                /* IN PARM      */",
         "  short outp6;                   /* OUT PARM     */",
         "  exec sql end declare section;",
         "",
         "  /* assign incoming arguments */",
         "  outp1    = *(short *) argv[1];",
         "  inp2     = *(long *) argv[2];",
         "  inoutp3  = *(long long *) argv[3];",
         "  inoutp4  = *(short *) argv[4];",
         "  inp5     = *(long long *) argv[5];",
         "  outp6    = *(short *) argv[6];",
         "",
         "  exec sql delete from "+collection_+"/JDCSCPPTAB;",
         "",
         "  outp1 = 123;",
         "  inoutp3 = inoutp3 + 5;",
         "  inoutp4 = inoutp4 - 10;",
         "  outp6 = 321;",
         "",
         "  exec sql INSERT INTO "+collection_+"/JDCSCPPTAB VALUES ('PROCEDURE JDCSCPPPGM RAN',",
         "           CURRENT_TIMESTAMP);",
         "  exec sql DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
         "           "+collection_+"/JDCSCPPTAB;",
         "  exec sql OPEN C1;",
         "",
         "  exec sql DECLARE C2 CURSOR WITH RETURN FOR SELECT * FROM",
         "           QSYS2/SYSROUTINES;",
         "  exec sql OPEN C2;",
         "",
         "  /* assign outgoing arguments */",
         "  *(short *) argv[1]     = outp1;",
         "  *(long *) argv[2]      = inp2;",
         "  *(long long *) argv[3] = inoutp3;",
         "  *(short *) argv[4]     = inoutp4;",
         "  *(long long *) argv[5] = inp5;",
         "  *(short *) argv[6]     = outp6;",
         "}",
	 ""
      };


      String command = "";
      String sql = "";
      try {

	  stringArrayToSourceFile(connection_,cppProgram, ""+collection_+"", "JDCSCPPPGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  command = "QSYS/CRTSQLCPPI OBJ("+collection_+"/JDCSCPPPGM) SRCFILE("+collection_+"/JDCSCPPPGM) COMMIT(*NONE) " +
	                   " TOSRCFILE("+collection_+"/JDCSC2PGM) ";
	  cmd.setString(1, command );
	  cmd.execute();

	  command = "QSYS/CRTBNDCPP PGM("+collection_+"/JDCSCPPPGM) SRCFILE("+collection_+"/JDCSC2PGM)";
	  cmd.setString(1, command );
	  cmd.execute();

	  command = "";

	  sql = "CREATE PROCEDURE "+collection_+".JDCSCPPPGM " +
                       "(OUT P1 smallINT, in P2 int, INOUT P3 bigint, "+
	               " inout p4 smallint, in p5 bigint, out p6 smallint) "+
                       " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+collection_+"/JDCSCPPPGM' " +
	               "LANGUAGE C++ GENERAL ";




	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".JDCSCPPPGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);
	  try {
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSCPPTAB");
	  } catch (Exception e) {}

	  sql = "create table  "+collection_+".JDCSCPPTAB (info varchar(80), ts timestamp)";
	  stmt.executeUpdate(sql);

	  sql = "call "+collection_+".JDCSCPPPGM(?, ?, ?, ?, ?, ?)";
	  CallableStatement cs = connection2_.prepareCall(sql);
	  cs.registerOutParameter(1, Types.SMALLINT);
	  cs.registerOutParameter(3, Types.BIGINT);
	  cs.registerOutParameter(4, Types.SMALLINT);
	  cs.registerOutParameter(6, Types.SMALLINT);

	  cs.setInt(2,2);
	  cs.setLong(3,3);
	  cs.setInt(4,4);
	  cs.setLong(5,5);

	  cs.execute();

	  int p1 = cs.getShort(1);
	  long p3 = cs.getLong(3);
	  int p4 = cs.getShort(4);
	  int p6 = cs.getShort(6);


	  // cleanup
	  sql = "Drop table     "+collection_+".JDCSCPPTAB";
	  stmt.executeUpdate(sql);
	  sql = "Drop procedure "+collection_+".JDCSCPPPGM";
	  stmt.executeUpdate(sql);
	  sql = "DROP TABLE     "+collection_+".JDCSCPPPGM";
	  stmt.executeUpdate(sql);
	  sql = "";

	  cs.close();
	  cmd.close();
	  stmt.close();

	  assertCondition ( p1 == 123 &&
			    p3 == 8 &&
			    p4 == -6 &&
			    p6 == 321,
			    "Output parameters("+p1+","+p3+","+p4+","+p6+") are not " +
			    "(123,8,-6,321" +
			    "-- new testcase added by native driver 12/10/03 "
);

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- new testcase added by native driver 12/10/03 command='"+command+"' sql='"+sql+"'");
    }
  }



/**
 * Call a C stored procedure
 */

  public void Var008()
  {

      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C++ program is used as an external procedure  */",
       "/*               with parameters in, out, inout.                    */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  long inp1;                     /* IN PARM      */",
       "  short outp2;                   /* OUT PARM     */",
       "  long long inoutp3;             /* INOUT PARM   */",
       "",
       "  /* assign incoming arguments */",
       "  inp1 = *(long *) argv[1];",
       "  outp2 = *(short *) argv[2];",
       "  inoutp3 = *(long long *) argv[3];",
       "",
       "  exec sql delete from "+collection_+"/JDCSCTAB;",
       "",
       "  outp2 = 123;",
       "  inoutp3 = inoutp3 + 5;",
       "",
       "  exec sql INSERT INTO "+collection_+"/JDCSCTAB VALUES ('PROCEDURE JDCSCTAB RAN',",
       "           CURRENT_TIMESTAMP);",
       "  exec sql DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
       "           "+collection_+"/JDCSCTAB;",
       "  exec sql OPEN C1;",
       "",
       "  /* assign outgoing arguments */",
       "  *(long *) argv[1] = inp1;",
       "  *(short *) argv[2] = outp2;",
       "  *(long long *) argv[3] = inoutp3;",
       "}",
       ""
      };


      try {

	  stringArrayToSourceFile(connection_,cProgram, ""+collection_+"", "JDCSCPGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+collection_+"/JDCSCPGM "+collection_+"/JDCSCPGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+collection_+"/JDCSCPGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+collection_+".JDCSCPGM " +
                       "(IN P1 INT, OUT P2 smallint, INOUT P3 bigint) "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+collection_+"/JDCSCPGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".JDCSCPGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);
	  try {
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSCTAB");
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table  "+collection_+".JDCSCTAB (info varchar(80), ts timestamp)");

	  CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSCPGM(?, ?, ?)");
	  cs.registerOutParameter(2, Types.SMALLINT);
	  cs.registerOutParameter(3, Types.BIGINT);

	  cs.setInt(1,1);
	  cs.setLong(3,3);

	  cs.execute();

	  int p2 = cs.getShort(2);
	  long p3 = cs.getLong(3);


	  // cleanup
	  stmt.executeUpdate("Drop table     "+collection_+".JDCSCTAB");
	  stmt.executeUpdate("Drop procedure "+collection_+".JDCSCPGM");
	  stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSCPGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  assertCondition ( p2 == 123 &&
			    p3 == 8,
			    "Output parameters("+p2+","+p3+") are not " +
			    "(123,8)" +
			    "-- new testcase added by native driver 12/10/03 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- new testcase added by native driver 12/10/03 ");
    }
  }


/**
 * Call a RPG stored procedure
 */

  public void Var009()
  {
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "     H**************************************************************",
	      "     H*  DESCRIPTION:  This RPG program is used as an external",
	      "     H*                procedure with parameters in, out,",
	      "     H*                inout.",
	      "     H**************************************************************",
	      "     D*",
	      "     D INP1            S             10I 0",
	      "     D OUTP2           S             20A",
	      "     D INOUTP3         S             15P 5",
	      "     C*",
	      "     C     *ENTRY        PLIST",
	      "     C                   PARM                    INP1",
	      "     C                   PARM                    OUTP2",
	      "     C                   PARM                    INOUTP3",
	      "     C*",
	      "     C***********************************************************",
	      "     C*",
	      "     C*",
	      "     C/EXEC SQL",
	      "     C+ DELETE FROM SHENLIB/SHENTAB",
	      "     C/END-EXEC",
	      "     C*",
	      "      /FREE",
	      "             OUTP2 = 'RPG WORKED';",
	      "             INOUTP3 = INOUTP3 + 5;",
	      "      /END-FREE",
	      "     C*",
	      "     C/EXEC SQL",
	      "     C+ INSERT INTO "+collection_+"/JDCSRPGTAB VALUES ('PROCEDURE RPGPGM RAN',",
	      "     C+ CURRENT_TIMESTAMP)",
	      "     C/END-EXEC",
	      "     C/EXEC SQL",
	      "     C+ DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
	      "     C+ "+collection_+"/JDCSRPGTAB",
	      "     C/END-EXEC",
	      "     C/EXEC SQL",
	      "     C+ OPEN C1",
	      "     C/END-EXEC",
	      "     C                   RETURN",
	      "     C*",
	      "     C*",
	      "",
	      ""
	  };

	  String sql = "";
	  try {

	      stringArrayToSourceFile(connection_,rpmProgram, ""+collection_+"", "JDCSRPGPGM");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+collection_+"/JDCSRPGPGM "+collection_+"/JDCSRPGPGM COMMIT(*NONE)";
	      sql=command;
	      cmd.setString(1, command );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+collection_+".JDCSRPGPGM " +
		"(IN P1 INT, OUT P2 CHAR(20), INOUT P3 DECIMAL(15,5)) "+
		" dynamic result sets 1  "+
		" EXTERNAL NAME '"+collection_+"/JDCSRPGPGM' "+
		" LANGUAGE RPGLE GENERAL ";


	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+collection_+".JDCSRPGPGM");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      try {
		  stmt.executeUpdate("Drop table     "+collection_+".JDCSRPGTAB");
	      } catch (Exception e) {}

	      stmt.executeUpdate("create table  "+collection_+".JDCSRPGTAB (info varchar(80), ts timestamp)");

	      CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSRPGPGM(?, ?, ?)");
	      cs.registerOutParameter(2, Types.CHAR);
	      cs.registerOutParameter(3, Types.DECIMAL);

	      cs.setInt(1,1);
	      cs.setInt(3,3);

	      cs.execute();

	      String p2 = cs.getString(2);
	      long p3 = cs.getBigDecimal(3).longValue();

	      cs.close();

	  // cleanup
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSRPGTAB");
	      stmt.executeUpdate("Drop procedure "+collection_+".JDCSRPGPGM");
	      stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSRPGPGM");

	      cmd.close();
	      stmt.close();

	      assertCondition ( "RPG WORKED          ".equals(p2) &&
				p3 == 8,
				"Output parameters("+p2+","+p3+") are not " +
				"('RPG WORKED',8" +
				"-- new testcase added by native driver 12/10/03 ");

	  } catch(Exception e) {
	      failed (e, "Unexpected Exception -- new testcase added by native driver 12/10/03 ");
	  }

      }
  }


/**
 * Call a C++ stored procedure with result set returned
 */

  public void Var010()
  {

      String cppProgram[] = {
         "/********************************************************************/",
         "/* DESCRIPTION:  This C++ program is used as an external procedure  */",
         "/*               with parameters in, out, inout.                    */",
         "/*                                                                  */",
         "/********************************************************************/",
         "",
         "#include <string.h>  /* used for strcpy()                           */",
         "#include <stdlib.h>  /* standard library                            */",
         "#include <stdio.h>   /* standard I/O functions                      */",
         "",
         "main(int argc, char *argv[])",
         "{",
         "  exec sql include sqlca;",
         "  exec sql begin declare section;",
         "  short outp1;                   /* OUT PARM     */",
         "  long inp2;                     /* IN PARM      */",
         "  long long inoutp3;             /* INOUT PARM   */",
         "  short inoutp4;                 /* INOUT PARM   */",
         "  long long inp5;                /* IN PARM      */",
         "  short outp6;                   /* OUT PARM     */",
         "  exec sql end declare section;",
         "",
         "  /* assign incoming arguments */",
         "  outp1    = *(short *) argv[1];",
         "  inp2     = *(long *) argv[2];",
         "  inoutp3  = *(long long *) argv[3];",
         "  inoutp4  = *(short *) argv[4];",
         "  inp5     = *(long long *) argv[5];",
         "  outp6    = *(short *) argv[6];",
         "",
         "  exec sql delete from "+collection_+"/JDCSCPPTAB;",
         "",
         "  outp1 = 123;",
         "  inoutp3 = inoutp3 + 5;",
         "  inoutp4 = inoutp4 - 10;",
         "  outp6 = 321;",
         "",
         "  exec sql INSERT INTO "+collection_+"/JDCSCPPTAB VALUES ('PROCEDURE JDCSCPPPGM RAN',",
         "           CURRENT_TIMESTAMP);",
         "  exec sql DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
         "           "+collection_+"/JDCSCPPTAB;",
         "  exec sql OPEN C1;",
         "",
         "  exec sql DECLARE C2 CURSOR WITH RETURN FOR SELECT * FROM",
         "           QSYS2/SYSROUTINES;",
         "  exec sql OPEN C2;",
         "",
         "  /* assign outgoing arguments */",
         "  *(short *) argv[1]     = outp1;",
         "  *(long *) argv[2]      = inp2;",
         "  *(long long *) argv[3] = inoutp3;",
         "  *(short *) argv[4]     = inoutp4;",
         "  *(long long *) argv[5] = inp5;",
         "  *(short *) argv[6]     = outp6;",
         "}",
	 ""
      };


      try {

	  stringArrayToSourceFile(connection_,cppProgram, ""+collection_+"", "JDCSCP2PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCPPI OBJ("+collection_+"/JDCSCP2PGM) SRCFILE("+collection_+"/JDCSCP2PGM) COMMIT(*NONE) " +
	                   " TOSRCFILE("+collection_+"/JDCSC10PGM) ";
	  cmd.setString(1, command );
	  cmd.execute();

	  command = "QSYS/CRTCPPMOD MODULE(JDCSCP2PGM) SRCFILE("+collection_+"/JDCSC10PGM)";
	  cmd.setString(1, command );
	  cmd.execute();

	  command = "QSYS/CRTPGM PGM("+collection_+"/JDCSCP2PGM) MODULE("+collection_+"/JDCSCP2PGM) ACTGRP(*CALLER)";
	  cmd.setString(1, command );
	  cmd.execute();


	  String sql = "CREATE PROCEDURE "+collection_+".JDCSCP2PGM " +
                       "(OUT P1 smallINT, in P2 int, INOUT P3 bigint, "+
	               " inout p4 smallint, in p5 bigint, out p6 smallint) "+
                       " dynamic result sets 2  "+
                       " EXTERNAL NAME '"+collection_+"/JDCSCP2PGM' " +
	               "LANGUAGE C++ GENERAL ";




	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".JDCSCP2PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);
	  try {
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSCPPTAB");
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table  "+collection_+".JDCSCPPTAB (info varchar(80), ts timestamp)");

	  CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSCP2PGM(?, ?, ?, ?, ?, ?)");
	  cs.registerOutParameter(1, Types.SMALLINT);
	  cs.registerOutParameter(3, Types.BIGINT);
	  cs.registerOutParameter(4, Types.SMALLINT);
	  cs.registerOutParameter(6, Types.SMALLINT);

	  cs.setInt(2,2);
	  cs.setLong(3,3);
	  cs.setInt(4,4);
	  cs.setLong(5,5);

	  cs.execute();

	  int p1 = cs.getShort(1);
	  long p3 = cs.getLong(3);
	  int p4 = cs.getShort(4);
	  int p6 = cs.getShort(6);



	  cs.close();
	  cmd.close();

	  // cleanup
	  stmt.executeUpdate("Drop table     "+collection_+".JDCSCPPTAB");
	  stmt.executeUpdate("Drop procedure "+collection_+".JDCSCP2PGM");
	  stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSCP2PGM");
          stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSC10PGM");
	  stmt.close();




	  assertCondition ( p1 == 123 &&
			    p3 == 8 &&
			    p4 == -6 &&
			    p6 == 321,
			    "Output parameters("+p1+","+p3+","+p4+","+p6+") are not " +
			    "(123,8,-6,321" +
			    "-- new testcase added by native driver 10/05/05 "
);

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- new testcase added by native driver 10/05/05 ");
    }
  }



/**
 * Call a C stored procedure
 */

  public void Var011()
  {

      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C++ program is used as an external procedure  */",
       "/*               with parameters in, out, inout.                    */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  long inp1;                     /* IN PARM      */",
       "  short outp2;                   /* OUT PARM     */",
       "  long long inoutp3;             /* INOUT PARM   */",
       "",
       "  /* assign incoming arguments */",
       "  inp1 = *(long *) argv[1];",
       "  outp2 = *(short *) argv[2];",
       "  inoutp3 = *(long long *) argv[3];",
       "",
       "  exec sql delete from "+collection_+"/JDCSCTAB;",
       "",
       "  outp2 = 123;",
       "  inoutp3 = inoutp3 + 5;",
       "",
       "  exec sql INSERT INTO "+collection_+"/JDCSCTAB VALUES ('PROCEDURE JDCSCTAB RAN',",
       "           CURRENT_TIMESTAMP);",
       "  exec sql DECLARE C1 CURSOR WITH RETURN FOR SELECT * FROM",
       "           "+collection_+"/JDCSCTAB;",
       "  exec sql OPEN C1;",
       "",
       "  /* assign outgoing arguments */",
       "  *(long *) argv[1] = inp1;",
       "  *(short *) argv[2] = outp2;",
       "  *(long long *) argv[3] = inoutp3;",
       "}",
       ""
      };


      try {

	  stringArrayToSourceFile(connection_,cProgram, ""+collection_+"", "JDCSC11PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+collection_+"/JDCSC11PGM "+collection_+"/JDCSC11PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+collection_+"/JDCSC11PGM ACTGRP(*CALLER)";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+collection_+".JDCSC11PGM " +
                       "(IN P1 INT, OUT P2 smallint, INOUT P3 bigint) "+
	               " dynamic result sets 1  "+
                       " EXTERNAL NAME '"+collection_+"/JDCSC11PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+collection_+".JDCSC11PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);
	  try {
	      stmt.executeUpdate("Drop table     "+collection_+".JDCSCTAB");
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table  "+collection_+".JDCSCTAB (info varchar(80), ts timestamp)");

	  CallableStatement cs = connection2_.prepareCall("call "+collection_+".JDCSC11PGM(?, ?, ?)");
	  cs.registerOutParameter(2, Types.SMALLINT);
	  cs.registerOutParameter(3, Types.BIGINT);

	  cs.setInt(1,1);
	  cs.setLong(3,3);

	  cs.execute();

	  int p2 = cs.getShort(2);
	  long p3 = cs.getLong(3);


	  // cleanup

	  cs.close();
	  cmd.close();

	  stmt.executeUpdate("Drop table     "+collection_+".JDCSCTAB");
	  stmt.executeUpdate("Drop procedure "+collection_+".JDCSC11PGM");
	  stmt.executeUpdate("DROP TABLE     "+collection_+".JDCSC11PGM");

	  stmt.close();

	  assertCondition ( p2 == 123 &&
			    p3 == 8,
			    "Output parameters("+p2+","+p3+") are not " +
			    "(123,8)" +
			    "-- new testcase added by native driver 10/05/05 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- new testcase added by native driver 10/05/05 ");
    }
  }



/**
 * Call a RPG stored procedure that returns a date
 */

  public void Var012()
  {
      String added = "-- New testcase added by native driver 08/09/2006";
      String sql = "";
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "     H**************************************************************",
	      "     H*  DESCRIPTION:  This RPG program is used as an external",
	      "     H*                procedure with parameters in, out,",
	      "     H*                inout.",
	      "     H**************************************************************",
	      "     D*",
	      "     D INP1            S             10I 0",
	      "     D OUTP2           S               D",
	      "     C*",
	      "     C     *ENTRY        PLIST",
	      "     C                   PARM                    INP1",
	      "     C                   PARM                    OUTP2",
	      "     C*",
	      "     C***********************************************************",
	      "     C*",
	      "     C*",
/*
	      "     C/EXEC SQL",
	      "     C+ set :outp2 = current_time",
	      "     C/END-EXEC",
*/

	      "      /FREE",
	      "             OUTP2 = D'1987-10-12';",
	      "      /END-FREE",

	      "     C*",
	      "     C                   RETURN",
	      "     C*",
	      "     C*",


	      ""
	  };


	  try {

	      stringArrayToSourceFile(connection_,rpmProgram, ""+collection_+"", "JDCSRPGDAT");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+collection_+"/JDCSRPGDAT "+collection_+"/JDCSRPGDAT  DATFMT(*ISO) COMMIT(*NONE)";
	      sql = command;

	      cmd.setString(1, command );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+collection_+".JDCSRPGDAT " +
		"(IN P1 INT, OUT P2 DATE) "+
		" EXTERNAL NAME '"+collection_+"/JDCSRPGDAT' "+
		" LANGUAGE RPGLE GENERAL ";


	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+collection_+".JDCSRPGDAT");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);

	      sql="call "+collection_+".JDCSRPGDAT(?, ?)";
	      CallableStatement cs = connection2_.prepareCall(sql);
	      cs.registerOutParameter(2, Types.DATE);

	      cs.setInt(1,1);

	      cs.execute();

	      java.sql.Date p2 = cs.getDate(2);
	      String dateString = p2.toString();
	      cs.close();
	      sql="Drop procedure "+collection_+".JDCSRPGDAT";
	      stmt.executeUpdate(sql);

	      cmd.close();
	      stmt.close();

	      assertCondition ( "1987-10-12".equals(dateString), "Date is <"+p2+"> <"+dateString+"> sb 1987-10-12" +added);

	  } catch(Exception e) {
	      failed (e, "Unexpected Exception "+sql+" "+added);
	  }

      }
  }



    /**
     * test isPoolable when not set.  Should return true for callable statement
     *
     * @since 1.6 (JDBC 4.0)
     */
    public void Var013()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.prepareCall ("CALL "
					 + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable");
		s.close ();
        if (isToolboxDriver())
            assertCondition (poolable==false, "isPoolable returned "+poolable+" sb false");
        else
		    assertCondition (poolable==true, "isPoolable returned "+poolable+" sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var014()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.prepareCall ("CALL "
					 + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		s.close ();
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable");
		failed("Didn't throw SQLException" +poolable );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}

    }

    /**
     * setPoolable() Set true and check
     *
     * @since 1.6 (JDBC 4.0)
     */
    public void Var015()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.prepareCall ("CALL "
					 + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		JDReflectionUtil.callMethod_V(s,"setPoolable", true);
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable");
		s.close ();
		assertCondition (poolable ==true, "isPoolable returned false sb true");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }

    /**
     * setPoolable() Set false and check
     *
     * @since 1.6 (JDBC 4.0)
     */
    public void Var016()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.prepareCall ("CALL "
					 + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		JDReflectionUtil.callMethod_V(s,"setPoolable", false);
		boolean poolable = JDReflectionUtil.callMethod_B(s,"isPoolable");
		s.close ();
		assertCondition (poolable==false, "isPoolable returned true sb false");
	    }
	    catch(Exception e)
	    {
		failed (e, "Unexpected Exception");
	    }
	}
    }


    /**
    isPoolable() - isPoolable on a closed statement.  Should throw an exception.
    **/
    public void Var017()
    {
	if (checkJdbc40()) {
	    try
	    {
		Statement s = connection_.prepareCall ("CALL "
					 + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)");
		s.close ();
		JDReflectionUtil.callMethod_V(s,"setPoolable", true);
		failed("Didn't throw SQLException"  );
	    }
	    catch(Exception e)
	    {
		assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	    }
	}
    }


    //@E1A Test to ensure executeBatch uses the correct parameter values.
    public void Var018()
    {
	if (checkJdbc20()) {
	    Statement s = null;
	    CallableStatement cs = null; 
	    try{
	    // create the table needed by the stored procedure
		s = connection_.createStatement();
		s.execute("CREATE TABLE "+JDSetupProcedure.COLLECTION+".SINSERT(COL1 INTEGER)");

		cs = connection_.prepareCall ("CALL "
								+ JDSetupProcedure.STP_CS1 + " (?)");

		for(int i=1; i<=5; i++) {
		    cs.setInt(1, i);
		    cs.addBatch();
		}
		cs.executeBatch();

		for(int i=6; i<=10; i++) {
		    cs.setInt(1, i);
		    cs.addBatch();
		}
		cs.executeBatch();

		for(int i=11; i<=15; i++) {
		    cs.setInt(1, i);
		    cs.addBatch();
		}
		cs.executeBatch();
		cs.close();
		cs = null; 
		
		ResultSet rs = s.executeQuery("SELECT * FROM "+JDSetupProcedure.COLLECTION+".SINSERT");
		int[] expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		int[] values = new int[15];
		int count = 0;
		while(rs.next()) {
		    values[count] = rs.getInt(1);
		    count++;
		}
		rs.close();
		StringBuffer sb = new StringBuffer();
		boolean answer = compare(expected, values, sb);
		assertCondition(answer, sb);
	    }
	    catch(Exception e){
		failed(e, "Unexpected exception.  Added by Toolbox 5/1/2006.");
	    }
	    finally
	    {
		try{
		  if (s != null) { 
		    s.execute("DROP TABLE "+JDSetupProcedure.COLLECTION+".SINSERT");
		    s.close();
		  }
		  if (cs != null) { 
		     cs.close(); 
		  }
		}
		catch(Exception e){
		// do nothing
		}
	    }
	}
    }


/**
 * Call a RPG stored procedure that returns a time
 */

  public void Var019()
  {
      String added = "-- New testcase added by native driver 08/09/2006";
      String sql = "";
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "     H**************************************************************",
	      "     H*  DESCRIPTION:  This RPG program is used as an external",
	      "     H*                procedure with parameters in, out,",
	      "     H*                inout.",
	      "     H**************************************************************",
	      "     D*",
	      "     D INP1            S             10I 0",
	      "     D OUTP2           S               T",
	      "     C*",
	      "     C     *ENTRY        PLIST",
	      "     C                   PARM                    INP1",
	      "     C                   PARM                    OUTP2",
	      "     C*",
	      "     C***********************************************************",
	      "     C*",
	      "     C*",
/*
	      "     C/EXEC SQL",
	      "     C+ set :outp2 = current_time",
	      "     C/END-EXEC",
*/

	      "      /FREE",
	      "             OUTP2 = T'11.12.13';",
	      "      /END-FREE",

	      "     C*",
	      "     C                   RETURN",
	      "     C*",
	      "     C*",


	      ""
	  };


	  try {

	      stringArrayToSourceFile(connection_,rpmProgram, ""+collection_+"", "JDCSRPGTIM");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      //
	      // Note:  For the TIMSEP parameter..
              //        This parameter applies only when *HMS is
              //        specified for the Time format (TIMFMT) parameter.
              //
	      String command = "QSYS/CRTSQLRPGI "+collection_+"/JDCSRPGTIM "+collection_+"/JDCSRPGTIM  TIMFMT(*ISO) TIMSEP(':') COMMIT(*NONE)";
	      sql = command;

	      cmd.setString(1, command );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+collection_+".JDCSRPGTIM " +
		"(IN P1 INT, OUT P2 TIME) "+
		" EXTERNAL NAME '"+collection_+"/JDCSRPGTIM' "+
		" LANGUAGE RPGLE GENERAL ";


	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+collection_+".JDCSRPGTIM");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);

	      sql="call "+collection_+".JDCSRPGTIM(?, ?)";
	      CallableStatement cs = connection2_.prepareCall(sql);
	      cs.registerOutParameter(2, Types.VARCHAR);

	      cs.setInt(1,1);

	      cs.execute();

	      String timeString = cs.getString(2);
	      cs.close();
	      sql="Drop procedure "+collection_+".JDCSRPGTIM";
	      stmt.executeUpdate(sql);

	      cmd.close();
	      stmt.close();

              if(isToolboxDriver())
                  assertCondition ( "11:12:13".equals(timeString), "Time is "+timeString+"> sb 11:12:13" +added);   // Default is : time separator
              else
                  assertCondition ( "11.12.13".equals(timeString), "Time is "+timeString+"> sb 11.12.13" +added);

	  } catch(Exception e) {
	      failed (e, "Unexpected Exception "+sql+" "+added);
	  }

      }
  }



  public void testReturnFormats(String connectionProperty, String[] expectedTime, String[] expectedDate) {
        String added = "-- New testcase added by toolbox driver 08/09/2006";
        String sql = "";
	boolean passed = true;
        String failStr = "";
        if (true) {
        Connection conn = null;

        try {
            conn = testDriver_.getConnection (baseURL_ + connectionProperty, userId_, encryptedPassword_);

            //call each of the SPs.  should get back same format per conn property, not per SP def
            for (int i = 0; i < spCount; i++)
            {
                boolean[] foundIndex = new boolean[expectedDate.length];
		for (int j = 0; j < expectedDate.length; j++) {
		    foundIndex[j] = false;
		}

                //iterate and call each of the different format SPs
                //in all cases, the expected formats should be the same due to the connection properties format
		failStr+="\nCalling "+ SPBase + (i + 1)+ " declared with "+SPFormats[i] ;

                CallableStatement stmt = conn.prepareCall("{ call " + SPBase + Integer.toString(i + 1) + " (  ) }");
                ResultSet rs = stmt.executeQuery();

                //find returned time/dates in arrays
                while (rs.next())
                {
                    String t = rs.getString(1);
                    String d = rs.getString(2);
                    boolean foundIt = false;
                    for (int x = 0; x < expectedDate.length; x++)
                    {
                        if (t.equals(expectedTime[x]) && d.equals(expectedDate[x]))
                        {
                            foundIndex[x] = true;
                            foundIt = true;
                            break;
                        }
                    }
                    if (foundIt == false)
                    {
                        failStr = failStr + "\ntime-date not expected=" + t + "-" + d;
			passed=false;
                    }
                }

                //now see which in arrays were not returned
                for (int x = 0; x < foundIndex.length; x++)
                {
		    if (foundIndex[x] == false) {
                        failStr = failStr + "\ndid not get back time-date " + expectedTime[x] + "-" + expectedDate[x];
			passed=false;
		    }
                }

                stmt.close();
            }

            conn.close();
            assertCondition(passed, failStr + " " + added);

        } catch (Exception e)
        {
            failed(e, "Unexpected Exception " + sql + "\n" +failStr +" " + added);
        }
        }
        


  }

   /**
   * Call a stored procedures that returns a time and date in all various formats with
   * connection props "time format=usa" and "date format=usa"
   */

    public void Var020()
    {

	    //SP1 time="usa" date="usa"
	    //SP2 "iso" "iso"
	    //SP3 "eur" "eur"
	    //SP4 "jis" "jis"
	    //SP5 "hms" "iso"
	    //st.execute("insert into " + TABLE1 + " values ( '00:00:00', '12/01/1999')"); 00am dec-01 or jan-12
	    //st.execute("insert into " + TABLE1 + " values ( '01:00:00', '01/12/2000')"); //dec-01 or jan-12
	    //st.execute("insert into " + TABLE1 + " values ( '12:00:00', '01/01/1900')");
	    //st.execute("insert into " + TABLE1 + " values ( '13:00:00', '01/01/2030')");
	    //st.execute("insert into " + TABLE1 + " values ( '24:00:00', '01/01/2030')"); //12am
	    //st.execute("insert into " + TABLE1 + " values ( '00:15:00', '01/15/2030')"); //12:15 AM
	String[] expectedTime = { "00:00 AM",   "01:00 AM",   "12:00 PM",   "01:00 PM",   "12:00 AM",
                                  "12:15 AM", "12:15 PM" };
	String[] expectedDate = { "12/01/1999", "01/12/2000", "01/01/1900", "01/01/2030", "01/01/2030",
                                  "01/15/2030", "01/15/2030"};

	testReturnFormats(";date format=usa;time format=usa", expectedTime, expectedDate);

    }


    /**
     * Call a stored procedures that returns a time and date in all various
     * formats with connection props "time format=iso" and "date format=iso"
     */

     public void Var021()
     {
	 String connectionProperties =  ";date format=iso;time format=iso";
	 String[] expectedTime = { "00.00.00", "01.00.00", "12.00.00", "13.00.00", "24.00.00", "00.15.00", "12.15.00" };
	 String[] expectedDate = { "1999-12-01", "2000-01-12", "1900-01-01", "2030-01-01", "2030-01-01", "2030-01-15", "2030-01-15" };
	 testReturnFormats(connectionProperties, expectedTime, expectedDate);

     }


     /**
      * Call a stored procedures that returns a time and date in all various
      * formats with connection props "time format=eur" and "date format=eur"
      */

     public void Var022()
     {
       String connectionProperties = ";date format=eur;time format=eur";
       String[] expectedTime = { "00.00.00", "01.00.00", "12.00.00", "13.00.00", "24.00.00", "00.15.00", "12.15.00" };
       String[] expectedDate = { "01.12.1999", "12.01.2000", "01.01.1900", "01.01.2030", "01.01.2030", "15.01.2030", "15.01.2030" };

       testReturnFormats(connectionProperties, expectedTime, expectedDate);
     }



      /**
       * Call a stored procedures that returns a time and date in all various
       * formats with connection props "time format=jis" and "date format=jis"
       */

       public void Var023()
       {
           String connectionProperties = ";date format=jis;time format=jis";
           String[] expectedTime = { "00:00:00", "01:00:00", "12:00:00", "13:00:00", "24:00:00", "00:15:00", "12:15:00" };
           String[] expectedDate = { "1999-12-01", "2000-01-12", "1900-01-01", "2030-01-01", "2030-01-01","2030-01-15","2030-01-15" };
           testReturnFormats(connectionProperties, expectedTime, expectedDate);
       }


       /**
        * Call a stored procedures that returns a time and date in all various
        * formats with connection props "time format=hms" and "date format=iso"
        */

       public void Var024()
       {
         String connectionProperties = ";date format=iso;time format=hms";
         String[] expectedTime = { "00:00:00", "01:00:00", "12:00:00", "13:00:00", "24:00:00","00:15:00","12:15:00" };
         String[] expectedDate = { "1999-12-01", "2000-01-12", "1900-01-01", "2030-01-01", "2030-01-01", "2030-01-15", "2030-01-15"};
         testReturnFormats(connectionProperties, expectedTime, expectedDate);
       }



/**
 * Call a RPG stored procedure that returns array result set.  Check for resource leak by running for 500 iterations.
 */

  public void Var025()
  {
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "      *  DVDSSERV -- Business Logic Service Program",
	      "      *",
	      "     H NOMAIN",
	      "     H AlwNull(*UsrCtl)",
	      "",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PR                  ",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "",
	      "     D earlySql        PR",
	      " ",
	      "",
	      "      *****************************************************",
	      "      * earlySql(): Sql init",
	      "      *****************************************************",
	      "     P earlySql        B",
	      "     D earlySql        PI",
	      "      /free",
	      "        // DatFmt must be first statement in module"     ,
	      "        EXEC SQL SET OPTION DatFmt = *ISO;",
	      "        // *CHG was *NONE",
	      "        exec SQL set option naming=*SYS,commit=*NONE;",
	      "      /end-free",
	      "     P                 E",
	      "",
	      "      *****************************************************",
	      "      *  ORDER_save_Purchase2_sp(): Routine to save purchase",
	      "      *    (Stored procedure interface for ORDER_save_Purchase2)",
	      "      *",
	      "      *    INCOME    = (input) income",
	      "      *    CUSTID    = customer id",
	      "      *    ORDERID   = order id",
	      "      *    ORDERDATE = order date",
	      "      *    ITEMID    = item number",
	      "      *    PRODID    = prod id",
	      "      *    QTY       = quantity",
	      "      * ",
	      "      *  Returns a result set with one row:",
	      "      *****************************************************",
	      "     P ORDER_save_Purchase2_sp...",
	      "     P                 B                   export",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PI",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "     ",
	      "     D Result6         ds                  qualified occurs(1)",
	      "     D  QTY                           9b 0 inz",
	      "     D  MsgId                         7a   inz",
	      "     D  Msg                          80a   inz",
	      "      /free",
	      "       %occur(RESULT6) = 1;",
	      "     ",
	      "       RESULT6.QTY        = QTY;",
	      "       RESULT6.MsgId      = 'ok';",
	      "       RESULT6.Msg        = 'Never better';",
	      "     ",
	      "     ",
	      "       exec SQL set Result sets Array :RESULT6 for 1 Rows;",
	      "      /end-free",
	      "     P                 E",
	      "     ",
	      "     "
	  };

	  String expectedC1 = "1";
	  String expectedC2 = "ok     ";
	  String expectedC3 = "Never better                                                                    "; 
	  String sql = "";
          StringBuffer sb = new StringBuffer();
	  boolean passed = true; 
	  try {
	      String lib = collection_;
	      stringArrayToSourceFile(connection_,rpmProgram, lib, "JDCSRPGPG2");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+lib+"/JDCSRPGPG2 "+lib+"/JDCSRPGPG2 OBJTYPE(*MODULE) DBGVIEW(*SOURCE) ";

	      sql=command;
	      cmd.setString(1, command );
	      cmd.execute();

	      sql = "QSYS/CRTSRVPGM SRVPGM("+lib+"/JDCSRPGPG2)              MODULE("+lib+"/JDCSRPGPG2) EXPORT(*ALL)  ACTGRP(*CALLER) ";

	      cmd.setString(1, sql );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG2 " +
		"(IN CUSTID D    ECIMAL(12,0), "+
                " IN ORDERID     DECIMAL(12,0), "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
                " IN QTY        DECIMAL(12,0)) "+
		" dynamic result sets 1  "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPGM' "+
		" LANGUAGE RPGLE GENERAL ";

	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG2(  " +
                " IN CUSTID      DECIMAL(12,0),  "+
                " IN ORDERID     DECIMAL(12,0),  "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
		" IN QTY         DECIMAL(12,0)) "+
		"   DYNAMIC RESULT SETS 1 "+
		" LANGUAGE RPGLE "+
		" NOT DETERMINISTIC "+
		" MODIFIES SQL DATA "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPG2(ORDER_SAVE_PURCHASE2_SP)' "+
		" PARAMETER STYLE GENERAL";

	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+lib+".JDCSRPGPG2");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);
	      for (int i =0 ; i < 500; i++) {
		  sql = "call "+lib+".JDCSRPGPG2(?,?,?, ?, ?, ?)";
		  CallableStatement cs = connection2_.prepareCall(sql);

		  cs.setInt(1,1);
		  cs.setInt(2,1);
		  cs.setDate(3,new java.sql.Date(System.currentTimeMillis()));
		  cs.setInt(4,1);
		  cs.setInt(5,1);
		  cs.setInt(6,1);

		  cs.execute();
		  ResultSet rs = cs.getResultSet();
		  rs.next();
		  String c1 = rs.getString(1);
		  String c2 = rs.getString(2);
		  String c3 = rs.getString(3);
		  if (!expectedC1.equals(c1)) {
		      passed=false;
		      sb.append("For C1 expected '"+expectedC1+"' got '"+c1+"'\n"); 
		  }
		  if (!expectedC2.equals(c2)) {
		      passed=false;
		      sb.append("For C2 expected '"+expectedC2+"' got '"+c2+"'\n"); 
		  }

		  if (!expectedC3.equals(c3)) {
		      passed=false;
		      sb.append("For C3 expected '"+expectedC3+"' got '"+c3+"'\n"); 
		  }

		  sb.append("Pass "+i+" Returned '"+c1+","+c2+","+c3+"\n");
		  cs.close();
	      }


   	       // cleanup
	      /* stmt.executeUpdate("Drop procedure "+lib+".JDCSRPGPG2"); */ 

	      cmd.close();
	      stmt.close();

	      assertCondition ( passed, sb);

	  } catch(Exception e) {
              output_.println(sb.toString());
	      output_.println("Exception SQL = "+sql);
	      if (e instanceof SQLException) {
		  SQLException sqlex = (SQLException) e;
                  output_.println("SQLCODE = "+sqlex.getErrorCode());
                  output_.println("SQLSTATE = "+sqlex.getSQLState());
                  output_.println("SQLMESSAGE = "+sqlex.getMessage());
	      }
	      failed (e, "Unexpected Exception -- new testcase added by native driver 06/16/08 ");
	  }

      }
  }



/**
 * Call a C stored procedure that returns zero length ebcdic data
 */

  public void Var026()
  {

      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C program is used as an external procedure  */",
       "/*               with out char parameter with invalid data       */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  char * outp1;          /* OUT PARM     */",
       "",
       "  /* assign incoming arguments */",
       "  outp1 =  argv[1];",
       "  strcpy(outp1, ",
       "  \"\\x01\\x02\\x03\\x04\\x05\\x06\\x07\\x08\" ",
       "  \"\\x09\\x0a\\x0b\\x0c\\x0d\\x0e\\x0f\" ",
       "  \"\\x10\\x11\\x12\\x13\\x14\\x15\\x16\\x17\" ",
       "  \"\\x18\\x19\\x1a\\x1b\\x1c\\x1d\\x1e\\x1f\" ",
       "  \"\\x20\\x21\\x22\\x23\\x24\\x25\\x26\\x27\" ",
       "  \"\\x28\\x29\\x2a\\x2b\\x2c\\x2d\\x2e\\x2f\" ",
       "  \"\\x30\\x31\\x32\\x33\\x34\\x35\\x36\\x37\" ",
       "  \"\\x38\\x39\\x3a\\x3b\\x3c\\x3d\\x3e\\x3f\" ",
       "  \"\\x41\\xFF\\x0f\\x51\\x0e\\x51\\x0f\");",
       "  strcpy(outp1, \"\"); ",
       "}",
       ""
      };


      try {
	  String lib = collection_;
	  stringArrayToSourceFile(connection_,cProgram, lib, "JDCSC26PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+lib+"/JDCSC26PGM  "+lib+"/JDCSC26PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+lib+"/JDCSC26PGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+lib+".JDCSC26PGM " +
                       "(OUT P1 VARCHAR(300)) "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+lib+"/JDCSC26PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+lib+".JDCSC26PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);


	  CallableStatement cs = connection2_.prepareCall("call "+lib+".JDCSC26PGM(?)");
	  cs.registerOutParameter(1, Types.VARCHAR);

	  cs.execute();

	  String p1 = cs.getString(1);


	  // cleanup
	  stmt.executeUpdate("Drop procedure "+lib+".JDCSC26PGM");
	  stmt.executeUpdate("DROP TABLE     "+lib+".JDCSC26PGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  String expected = "";
	  assertCondition ( expected.equals(p1),
			    "Output parameter ("+p1+") is not  " + expected +
			    " -- test zero length output  -- new testcase added by native driver 7/27/09 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- test zero length output --  new testcase added by native driver 7/27/09 ");
    }
  }



/**
 * Call a C stored procedure that returns garbage ebcdic data
 */

  public void Var027()
  {

      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C program is used as an external procedure  */",
       "/*               with out char parameter with invalid data       */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  char * outp1;          /* OUT PARM     */",
       "",
       "  /* assign incoming arguments */",
       "  outp1 =  argv[1];",
       "  strcpy(outp1, ",
       "  \"\\x01\\x02\\x03\\x04\\x05\\x06\\x07\\x08\" ",
       "  \"\\x09\\x0a\\x0b\");",
       "}",
       ""
      };


      try {

	  String lib = collection_;
	  stringArrayToSourceFile(connection_,cProgram, lib, "JDCSC27PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+lib+"/JDCSC27PGM "+lib+"/JDCSC27PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+lib+"/JDCSC27PGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+lib+".JDCSC27PGM " +
                       "(OUT P1 VARCHAR(300)) "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+lib+"/JDCSC27PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+lib+".JDCSC27PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);


	  CallableStatement cs = connection2_.prepareCall("call "+lib+".JDCSC27PGM(?)");
	  cs.registerOutParameter(1, Types.VARCHAR);

	  cs.execute();

	  String p1 = cs.getString(1);


	  // cleanup
	  stmt.executeUpdate("Drop procedure "+lib+".JDCSC27PGM");
	  stmt.executeUpdate("DROP TABLE     "+lib+".JDCSC27PGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  String expected = "\u0001\u0002\u0003\u009c\u0009\u0086\u007f\u0097\u008d\u008e\u000b";
	  assertCondition ( expected.equals(p1),
			    "Output parameter ("+p1+"=\n"+JDTestUtilities.dumpBytes(p1)+") is not  "
			    +	expected + "=\n"+JDTestUtilities.dumpBytes(expected)+
			    " -- test zero length output  -- new testcase added by native driver 7/28/09 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- test zero length output --  new testcase added by native driver 7/27/09 ");
    }
  }



/**
 * Call a C stored procedure that returns on byte garbage ebcdic data
 */

  public void Var028()
  {

      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C program is used as an external procedure  */",
       "/*               with out char parameter with invalid data       */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  char * outp1;          /* OUT PARM     */",
       "",
       "  /* assign incoming arguments */",
       "  outp1 =  argv[1];",
       "  outp1[0] = '\\x0f';",
       "}",
       ""
      };


      try {
	  String lib = collection_; 
	  stringArrayToSourceFile(connection_,cProgram, lib, "JDCSC28PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+lib+"/JDCSC28PGM "+lib+"/JDCSC28PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+lib+"/JDCSC28PGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  String sql = "CREATE PROCEDURE "+lib+".JDCSC28PGM " +
                       "(OUT P1 CHAR(1)) "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+lib+"/JDCSC28PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+lib+".JDCSC28PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);


	  CallableStatement cs = connection2_.prepareCall("call "+lib+".JDCSC28PGM(?)");
	  cs.registerOutParameter(1, Types.VARCHAR);

	  cs.execute();

	  String p1 = cs.getString(1);


	  // cleanup
	  stmt.executeUpdate("Drop procedure "+lib+".JDCSC28PGM");
	  stmt.executeUpdate("DROP TABLE     "+lib+".JDCSC28PGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  String expected = "\u000f";
	  assertCondition ( expected.equals(p1),
			    "Output parameter ("+p1+"=\n"+JDTestUtilities.dumpBytes(p1)+") is not  "
			    +	expected + "=\n"+JDTestUtilities.dumpBytes(expected)+
			    " -- test zero length output  -- new testcase added by native driver 7/28/09 ");

    } catch(Exception e) {
        failed (e, "Unexpected Exception -- test zero length output --  new testcase added by native driver 7/28/09 ");
    }
  }


/**
 * Call a C stored procedure that returns multiple byte garbage ebcdic data
 */

  public void Var029()
  {

      if(isToolboxDriver())
      {
          notApplicable();
          return;
      }

      //
      // The classic JVM fails on iconv and so appears like truncation.
      // Jdbc: ..: jdbcUtil.iconv_open -- opening for 937->1200 -> at 0x4b0
      // Jdbc: ..: jdbcUtil.iconv failed with rc = -1, errno = 3028 inbytesleft = 4
      // Jdbc: ..: Input bytes
      // Jdbc: ..:  0f40400f
      //
      // Jdbc: ..: jdbcUtil.throwException(): entry, sqlState=22524
      // Jdbc: ..:   DB2JDBCException@849495216 DB2JDBCException(22524) Character conversion resulted in truncation.

      String vmName = System.getProperty("java.vm.name");
      if (vmName.indexOf("Classic VM") >= 0) {
	  notApplicable("Garbage mixed data test only applicable for J9 JVM");
	  return;
      }


      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C program is used as an external procedure  */",
       "/*               with out char parameter with invalid data       */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "",
       "main(int argc, char *argv[])",
       "{",
       "  exec sql include sqlca;",
       "  char * outp1;          /* OUT PARM     */",
       "",
       "  /* assign incoming arguments */",
       "  outp1 =  argv[1];",
       "  outp1[0] = '\\x0f';",
       "  outp1[1] = '\\x40';",
       "  outp1[2] = '\\x40';",
       "  outp1[3] = '\\x0f';",
       "  outp1[4] = '\\x00';",
       "}",
       ""
      };


      try {
	  String lib = collection_; 
	  stringArrayToSourceFile(connection_,cProgram, lib, "JDCSC29PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+lib+"/JDCSC29PGM "+lib+"/JDCSC29PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+lib+"/JDCSC29PGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  /* note:  the length must be at least 4 */
	  /* when I do the creatof CHAR(1) I get a  SQL0604  */
	  /* If the FOR MIXED DATA clause or a mixed CCSID is specified,  */
          /* the length cannot be less than 4 */
	  String sql = "CREATE PROCEDURE "+lib+".JDCSC29PGM " +
                       "(OUT P1 CHAR(4) CCSID 937) "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+lib+"/JDCSC29PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+lib+".JDCSC29PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);


	  CallableStatement cs = connection2_.prepareCall("call "+lib+".JDCSC29PGM(?)");
	  cs.registerOutParameter(1, Types.VARCHAR);

	  cs.execute();

	  String p1 = cs.getString(1);


	  // cleanup
	  stmt.executeUpdate("Drop procedure "+lib+".JDCSC29PGM");
	  stmt.executeUpdate("DROP TABLE     "+lib+".JDCSC29PGM");

	  cs.close();
	  cmd.close();
	  stmt.close();

	  String expected = "\ufffd\u0020\u0020\ufffd";
	  assertCondition ( expected.equals(p1),
			    "Output parameter ("+p1+"=\n"+JDTestUtilities.dumpBytes(p1)+") is not  "
			    +	expected + "=\n"+JDTestUtilities.dumpBytes(expected)+
			    " -- test mixed byte garbage data  -- new testcase added by native driver 7/29/09 ");

    } catch(Exception e) {
	// With pushing translation to database, native JDDBC now reports an error
	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
	    String message = e.toString();
	    if (message.indexOf("Mixed data or UTF-8 data not properly formed") >= 0) {
		assertCondition(true,"");
		return; 
	    } 
	}  
        failed (e, "Unexpected Exception -- test mixed byte garbage data --  new testcase added by native driver 7/29/09 ");
    }
  }



  public boolean verifyAndCloseRs(boolean passed, ResultSet rs, String expected, StringBuffer sb) throws SQLException {

      if (rs != null && rs.next()) {
	  String value = rs.getString(1);
	  if (!expected.equals(value)) {
	      sb.append("Got "+value+" expected "+expected+"\n");
	      passed =false;
	  }
	  while (rs.next()) {

	  }
      } else {
	  sb.append("Unable to fetch from resultSet rs="+rs+" looking for "+expected+"\n");
	  passed = false;
      }
      if (rs != null) {
	  rs.close();
      }

      return passed;
  }

  public void Var030()
  {
      if (isToolboxDriver()) {
	  notApplicable("Toolbox does not support multiple concurrent RS");
	  return;
      }
      if (checkJdbc30()) {
	      StringBuffer sb = new StringBuffer();
	      sb.append("Test that works with multiple current results sets \n");
	      sb.append("Opens 3 then closes while opening.. Written for CPS 8LXQRN\n");

	  try {
	      String procedureName = collection_ + ".JDCSMISCM9";
	      boolean passed = true;
	      String sql;

	      ResultSet[] rs = new ResultSet[10];

	      Statement stmt = connection_.createStatement();
	      try {
		  sql = "DROP PROCEDURE "+procedureName;
		  sb.append("CALLING "+sql+"\n");
		  stmt.executeUpdate(sql);
	      } catch (Exception e) {
		  sb.append("Exception "+e.toString()+" dropping procedure ");
	      }

	  /* Create procedure that return 9 results sets */
	      sql =
		"CREATE PROCEDURE " + procedureName+"() "+
		"LANGUAGE SQL " +
		"DYNAMIC RESULT SETS 9 " +
		"BEGIN " +
		" " +
		"DECLARE C1 CURSOR FOR SELECT '1' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C2 CURSOR FOR SELECT '2' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C3 CURSOR FOR SELECT '3' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C4 CURSOR FOR SELECT '4' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C5 CURSOR FOR SELECT '5' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C6 CURSOR FOR SELECT '6' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C7 CURSOR FOR SELECT '7' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C8 CURSOR FOR SELECT '8' FROM SYSIBM.SYSDUMMY1; " +
		"DECLARE C9 CURSOR FOR SELECT '9' FROM SYSIBM.SYSDUMMY1; " +
		" " +
		"OPEN C1; " +
		"OPEN C2; " +
		"OPEN C3; " +
		"OPEN C4; " +
		"OPEN C5; " +
		"OPEN C6; " +
		"OPEN C7; " +
		"OPEN C8; " +
		"OPEN C9; " +
		"" +
		"SET RESULT SETS CURSOR C1, CURSOR C2, CURSOR C3, CURSOR C4, CURSOR C5," +
		"CURSOR C6, CURSOR C7, CURSOR C8, CURSOR C9; " +
		"" +
		"END ";


	      sb.append("CALLING "+sql+"\n");
	      stmt.executeUpdate(sql);

	      sql = "CALL "+procedureName+"()";

	      sb.append("PREPARING "+sql+"\n");
	      CallableStatement cstmt = connection_.prepareCall(sql);
	      sb.append("EXECUTING "+sql+"\n");
	      cstmt.execute();
	      rs[1] = cstmt.getResultSet();
  	      /* fetch the additional result sets #2 and #3 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[2] = cstmt.getResultSet();
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[3] = cstmt.getResultSet();
   	      /* close #2 */
	      passed = verifyAndCloseRs(passed, rs[2], "2", sb);
  	      /* fetch #4 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[4] = cstmt.getResultSet();

	  /* close #3 */
	      passed = verifyAndCloseRs(passed, rs[3], "3", sb);


	  /* fetch #5 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[5] = cstmt.getResultSet();

	  /* close #4 */
	      passed = verifyAndCloseRs(passed, rs[4], "4", sb);
	  /* fetch #6 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[6] = cstmt.getResultSet();

	  /* close #5 */
	      passed = verifyAndCloseRs(passed, rs[5], "5", sb);
	  /* fetch #7 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[7] = cstmt.getResultSet();

	  /* close #6 */
	      passed = verifyAndCloseRs(passed, rs[6], "6", sb);
	  /* fetch #8 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[8] = cstmt.getResultSet();

	  /* close #7 */
	      passed = verifyAndCloseRs(passed, rs[7], "7", sb);
	  /* fetch #9 */
	      cstmt.getMoreResults(Statement.KEEP_CURRENT_RESULT);
	      rs[9] = cstmt.getResultSet();

	  /* close #8 */
	      passed = verifyAndCloseRs(passed, rs[8], "8", sb);
	  /* close #9 */
	      passed = verifyAndCloseRs(passed, rs[9], "9", sb);

          /* close #1 */
	      passed = verifyAndCloseRs(passed, rs[1], "1", sb);

		assertCondition(passed, sb);

	  } catch(Exception e) {
	      failed (e, "Unexpected Exception "+sb.toString());
	  }
      }
  }



/**
 * Call a stored procedure that executes an embedded SELECT INTO, then indicates an error using
 *  SNDPGMMSG API of QMHSNDPM.
 * A customer hit a problem where native JDBC thought this was a combined open fetch and the
 * error was not reported.  See CPS 8P2SD3
 */


  public void Var031()
  {

      String info = " -- Call a stored procedure that executes an embedded SELECT INTO, then indicates an error using  SNDPGMMSG API of QMHSNDPM -- Fixed in V6R1 using SI46132";
      String cProgram[] = {
       "/********************************************************************/",
       "/* DESCRIPTION:  This C program is to run an embedded select into   */",
       "/*               and then issue and error using SNDPGMMSG           */",
       "/*                                                                  */",
       "/********************************************************************/",
       "",
       "#include <qmhsndpm.h>",
       "#include <string.h>  /* used for strcpy()                           */",
       "#include <stdlib.h>  /* standard library                            */",
       "#include <stdio.h>   /* standard I/O functions                      */",
       "#include <qusec.h>",
       "",
       "main(int argc, char *argv[])",
       "{",
       " Qus_EC_t errorCode;     ",
       " char msgkey[4];",
       "  exec sql include sqlca;",
       "  exec sql CREATE TABLE "+collection_+"/JDCSCTAB31 (C1 INT);",

       "  exec sql INSERT INTO "+collection_+"/JDCSCTAB31 SELECT '1' FROM SYSIBM/SYSDUMMY1;",

       "  exec sql DROP TABLE "+collection_+"/JDCSCTAB31 ;",


       " memset(&errorCode, 0, sizeof(errorCode)); ",

       " QMHSNDPM ",
       "    (\"CPF9897\",                    /* Message identifier         */ ",
       "    \"QCPFMSG   *LIBL     \",/* Qual. message file name    */ ",
       "    \"Invalid Date Error  \",/* Message data or text       */ ",
       "    20,                      /* Length of message data     */ ",
       "   \"*ESCAPE   \",           /* Message type               */ ",
       "   \"*PGMBDY\",              /* Call stack entry           */ ",
       "   1,                        /* Call stack counter         */ ",
       "   &msgkey,                  /* Message key                */ ",
       "     (void *)&errorCode,    /* Error code                 */ ",
       "      7,                     /* Length of call stack entry */ ",
       "      \"*NONE     *NONE     \",/* Call stack entry           */ ",
       "      -1,                    /* Display program messages   */ ",
       "      \"*CHAR     \",          /* Call stack entry data type */ ",
       "      0);                                                     ",


       "}",
       ""
      };

      String exMessage = "NO EXCEPTION";
      String expectedMessage = "Invalid Date Error";

      try {
	  String lib = collection_; 
	  stringArrayToSourceFile(connection_,cProgram, lib, "JDCSC31PGM");

	  CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	  String command = "QSYS/CRTSQLCI "+lib+"/JDCSC31PGM "+lib+"/JDCSC31PGM COMMIT(*NONE)";
	  cmd.setString(1, command );
	  cmd.execute();


	  command = "QSYS/CRTPGM "+lib+"/JDCSC31PGM ";
	  cmd.setString(1, command );
	  cmd.execute();

	  /* note:  the length must be at least 4 */
	  /* when I do the creatof CHAR(1) I get a  SQL0604  */
	  /* If the FOR MIXED DATA clause or a mixed CCSID is specified,  */
          /* the length cannot be less than 4 */
	  String sql = "CREATE PROCEDURE "+lib+".JDCSC31PGM " +
                       "() "+
	               " dynamic result sets 0  "+
                       " EXTERNAL NAME '"+lib+"/JDCSC31PGM' "+
	               " LANGUAGE C GENERAL ";



	  Statement stmt = connection2_.createStatement();
	  try {
	      stmt.executeUpdate("drop procedure "+lib+".JDCSC31PGM");
	  } catch (Exception e) {
	  }
	  stmt.executeUpdate(sql);


	  CallableStatement cs = connection2_.prepareCall("call "+lib+".JDCSC31PGM()");

	  try {
	      cs.execute();
	  } catch (Exception e) {
	      exMessage = e.toString();
	      e.printStackTrace();
	  }

	  // cleanup
	  stmt.executeUpdate("Drop procedure "+lib+".JDCSC31PGM");
	  stmt.executeUpdate("DROP TABLE     "+lib+".JDCSC31PGM");

	  cs.close();
	  cmd.close();
	  stmt.close();


	  assertCondition ( exMessage.indexOf(expectedMessage) >= 0,
			    "Got "+exMessage+" expected "+expectedMessage+info);

    } catch(Exception e) {
        failed (e, "Unexpected Exception "+info);
    }
  }




/**
 * Call a RPG stored procedure that returns array result set with the JOB CCSID set to 65535.
 * Should not get a truncation error. 
 */

  public void Var032()
  {
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "      *  DVDSSERV -- Business Logic Service Program",
	      "      *",
	      "     H NOMAIN",
	      "     H AlwNull(*UsrCtl)",
	      "",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PR                  ",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "",
	      "     D earlySql        PR",
	      " ",
	      "",
	      "      *****************************************************",
	      "      * earlySql(): Sql init",
	      "      *****************************************************",
	      "     P earlySql        B",
	      "     D earlySql        PI",
	      "      /free",
	      "        // DatFmt must be first statement in module"     ,
	      "        EXEC SQL SET OPTION DatFmt = *ISO;",
	      "        // *CHG was *NONE",
	      "        exec SQL set option naming=*SYS,commit=*NONE;",
	      "      /end-free",
	      "     P                 E",
	      "",
	      "      *****************************************************",
	      "      *  ORDER_save_Purchase2_sp(): Routine to save purchase",
	      "      *    (Stored procedure interface for ORDER_save_Purchase2)",
	      "      *",
	      "      *    INCOME    = (input) income",
	      "      *    CUSTID    = customer id",
	      "      *    ORDERID   = order id",
	      "      *    ORDERDATE = order date",
	      "      *    ITEMID    = item number",
	      "      *    PRODID    = prod id",
	      "      *    QTY       = quantity",
	      "      * ",
	      "      *  Returns a result set with one row:",
	      "      *****************************************************",
	      "     P ORDER_save_Purchase2_sp...",
	      "     P                 B                   export",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PI",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "     ",
	      "     D Result6         ds                  qualified occurs(1)",
	      "     D  QTY                           9b 0 inz",
	      "     D  MsgId                         7a   inz",
	      "     D  Msg                          80a   inz",
	      "      /free",
	      "       %occur(RESULT6) = 1;",
	      "     ",
	      "       RESULT6.QTY        = QTY;",
	      "       RESULT6.MsgId      = 'ok';",
	      "       RESULT6.Msg        = 'Never better';",
	      "     ",
	      "     ",
	      "       exec SQL set Result sets Array :RESULT6 for 1 Rows;",
	      "      /end-free",
	      "     P                 E",
	      "     ",
	      "     "
	  };

	  String expectedC1 = "1";
	  String expectedC2 = "ok     ";
	  String expectedC3 = "Never better                                                                    ";
	  String sql = "";
          StringBuffer sb = new StringBuffer();
	  sb.append("  --  new testcase added by native driver 06/16/2014\n"); 
	  boolean passed = true; 
	  try {

	      // For the toolbox driver, this cames back as hex and toolbox converts to hex values in string
	      if (getDriver() == JDTestDriver.DRIVER_TOOLBOX) {

		  expectedC2 = "96924040404040"; 
		  expectedC3 = "D585A58599408285A3A385994040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040404040"; 

	      } 



	      String lib = collection_; 
	      stringArrayToSourceFile(connection_,rpmProgram, lib, "JDCSRPGPG2");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+lib+"/JDCSRPGPG2 "+lib+"/JDCSRPGPG2 OBJTYPE(*MODULE) DBGVIEW(*SOURCE) ";

	      sql=command;
	      cmd.setString(1, command );
	      cmd.execute();

	      sql = "QSYS/CRTSRVPGM SRVPGM("+lib+"/JDCSRPGPG2)              MODULE("+lib+"/JDCSRPGPG2) EXPORT(*ALL)  ACTGRP(*CALLER) ";

	      cmd.setString(1, sql );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG2 " +
		"(IN CUSTID D    ECIMAL(12,0), "+
                " IN ORDERID     DECIMAL(12,0), "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
                " IN QTY        DECIMAL(12,0)) "+
		" dynamic result sets 1  "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPGM' "+
		" LANGUAGE RPGLE GENERAL ";

	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG2(  " +
                " IN CUSTID      DECIMAL(12,0),  "+
                " IN ORDERID     DECIMAL(12,0),  "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
		" IN QTY         DECIMAL(12,0)) "+
		"   DYNAMIC RESULT SETS 1 "+
		" LANGUAGE RPGLE "+
		" NOT DETERMINISTIC "+
		" MODIFIES SQL DATA "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPG2(ORDER_SAVE_PURCHASE2_SP)' "+
		" PARAMETER STYLE GENERAL";

	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+lib+".JDCSRPGPG2");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);

	      Connection conn65535 =  testDriver_.getConnection (baseURL_ , userId_, encryptedPassword_);

	      Statement s65535 = conn65535.createStatement();
	      s65535.executeUpdate("CALL QSYS2.QCMDEXC ('CHGJOB CCSID(65535)') ");
	      s65535.close(); 

	      for (int i =0 ; i < 10; i++) {
		  sql = "call "+lib+".JDCSRPGPG2(?,?,?, ?, ?, ?)";
		  CallableStatement cs = conn65535.prepareCall(sql);

		  cs.setInt(1,1);
		  cs.setInt(2,1);
		  cs.setDate(3,new java.sql.Date(System.currentTimeMillis()));
		  cs.setInt(4,1);
		  cs.setInt(5,1);
		  cs.setInt(6,1);

		  cs.execute();
		  ResultSet rs = cs.getResultSet();
		  rs.next();
		  String c1 = rs.getString(1);
		  String c2 = rs.getString(2);
		  String c3 = rs.getString(3);
		  if (!expectedC1.equals(c1)) {
		      passed=false;
		      sb.append("For C1 expected '"+expectedC1+"' got '"+c1+"'\n"); 
		  }
		  if (!expectedC2.equals(c2)) {
		      passed=false;
		      sb.append("For C2 expected '"+expectedC2+"' got '"+c2+"'\n"); 
		  }

		  if (!expectedC3.equals(c3)) {
		      passed=false;
		      sb.append("For C3 expected '"+expectedC3+"' got '"+c3+"'\n"); 
		  }

		  sb.append("Pass "+i+" Returned '"+c1+","+c2+","+c3+"\n");
		  cs.close();
	      }

	      conn65535.close();

   	       // cleanup
	      stmt.executeUpdate("Drop procedure "+lib+".JDCSRPGPG2");

	      cmd.close();
	      stmt.close();

	      assertCondition ( passed, sb);

	  } catch(Exception e) {
              output_.println(sb.toString());
	      output_.println("Exception SQL = "+sql);
	      if (e instanceof SQLException) {
		  SQLException sqlex = (SQLException) e;
                  output_.println("SQLCODE = "+sqlex.getErrorCode());
                  output_.println("SQLSTATE = "+sqlex.getSQLState());
                  output_.println("SQLMESSAGE = "+sqlex.getMessage());
	      }
	      failed (e, "Unexpected Exception -- new testcase added by native driver 06/16/2014 ");
	  }

      }
  }



/**
 * Call a RPG stored procedure that returns array result set with the JOB CCSID set to 65535, but the procedure specifies the CCSID on the declaration
 * Note:  You can only specify the CCSID on the declaration in V7R2. 
 */

  public void Var033()
  {
     
      if (checkJdbc20 ()) {

	  String rpmProgram[] = {
	      "      *  DVDSSERV -- Business Logic Service Program",
	      "      *",
	      "     H NOMAIN",
	      "     H AlwNull(*UsrCtl)",
/*              "     H CCSID(*CHAR : 37 )",  */ 
	      "",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PR                  ",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "",
	      "     D earlySql        PR",
	      " ",
	      "",
	      "      *****************************************************",
	      "      * earlySql(): Sql init",
	      "      *****************************************************",
	      "     P earlySql        B",
	      "     D earlySql        PI",
	      "      /free",
	      "        // DatFmt must be first statement in module"     ,
	      "        EXEC SQL SET OPTION DatFmt = *ISO;",
	      "        // *CHG was *NONE",
	      "        exec SQL set option naming=*SYS,commit=*NONE;",
	      "      /end-free",
	      "     P                 E",
	      "",
	      "      *****************************************************",
	      "      *  ORDER_save_Purchase2_sp(): Routine to save purchase",
	      "      *    (Stored procedure interface for ORDER_save_Purchase2)",
	      "      *",
	      "      *    INCOME    = (input) income",
	      "      *    CUSTID    = customer id",
	      "      *    ORDERID   = order id",
	      "      *    ORDERDATE = order date",
	      "      *    ITEMID    = item number",
	      "      *    PRODID    = prod id",
	      "      *    QTY       = quantity",
	      "      * ",
	      "      *  Returns a result set with one row:",
	      "      *****************************************************",
	      "     P ORDER_save_Purchase2_sp...",
	      "     P                 B                   export",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PI",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "     ",
	      "     D Result6         ds                  qualified occurs(1)",
	      "     D  QTY                           9b 0 inz",
	      "     D  MsgId                         7a   inz CCSID(37)",
	      "     D  Msg                          80a   inz CCSID(37)",
	      "      /free",
	      "       %occur(RESULT6) = 1;",
	      "     ",
	      "       RESULT6.QTY        = QTY;",
	      "       RESULT6.MsgId      = 'ok';",
	      "       RESULT6.Msg        = 'Never better';",
	      "     ",
	      "     ",
	      "       exec SQL set Result sets Array :RESULT6 for 1 Rows;",
	      "      /end-free",
	      "     P                 E",
	      "     ",
	      "     "
	  };

	  String expectedC1 = "1";
	  String expectedC2 = "ok     ";
	  String expectedC3 = "Never better                                                                    "; 
	  String sql = "";
          StringBuffer sb = new StringBuffer();
	  boolean passed = true; 
	  try {
	      String lib = collection_; 
	      stringArrayToSourceFile(connection_,rpmProgram, lib, "JDCSRPGPG3");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+lib+"/JDCSRPGPG3 "+lib+"/JDCSRPGPG3 OBJTYPE(*MODULE) DBGVIEW(*SOURCE) ";

	      sql=command;
	      cmd.setString(1, command );
	      cmd.execute();

	      sql = "QSYS/CRTSRVPGM SRVPGM("+lib+"/JDCSRPGPG3)              MODULE("+lib+"/JDCSRPGPG3) EXPORT(*ALL)  ACTGRP(*CALLER) ";

	      cmd.setString(1, sql );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG3 " +
		"(IN CUSTID D    ECIMAL(12,0), "+
                " IN ORDERID     DECIMAL(12,0), "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
                " IN QTY        DECIMAL(12,0)) "+
		" dynamic result sets 1  "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPGM' "+
		" LANGUAGE RPGLE GENERAL ";

	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG3(  " +
                " IN CUSTID      DECIMAL(12,0),  "+
                " IN ORDERID     DECIMAL(12,0),  "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
		" IN QTY         DECIMAL(12,0)) "+
		"   DYNAMIC RESULT SETS 1 "+
		" LANGUAGE RPGLE "+
		" NOT DETERMINISTIC "+
		" MODIFIES SQL DATA "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPG3(ORDER_SAVE_PURCHASE2_SP)' "+
		" PARAMETER STYLE GENERAL";

	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+lib+".JDCSRPGPG3");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);

	      Connection conn65535 =  testDriver_.getConnection (baseURL_ , userId_, encryptedPassword_);

	      Statement s65535 = conn65535.createStatement();
	      s65535.executeUpdate("CALL QSYS2.QCMDEXC ('CHGJOB CCSID(65535)') ");
	      s65535.close(); 

	      for (int i =0 ; i < 10; i++) {
		  sql = "call "+lib+".JDCSRPGPG3(?,?,?, ?, ?, ?)";
		  CallableStatement cs = conn65535.prepareCall(sql);

		  cs.setInt(1,1);
		  cs.setInt(2,1);
		  cs.setDate(3,new java.sql.Date(System.currentTimeMillis()));
		  cs.setInt(4,1);
		  cs.setInt(5,1);
		  cs.setInt(6,1);

		  cs.execute();
		  ResultSet rs = cs.getResultSet();
		  rs.next();
		  String c1 = rs.getString(1);
		  String c2 = rs.getString(2);
		  String c3 = rs.getString(3);
		  if (!expectedC1.equals(c1)) {
		      passed=false;
		      sb.append("For C1 expected '"+expectedC1+"' got '"+c1+"'\n"); 
		  }
		  if (!expectedC2.equals(c2)) {
		      passed=false;
		      sb.append("For C2 expected '"+expectedC2+"' got '"+c2+"'\n"); 
		  }

		  if (!expectedC3.equals(c3)) {
		      passed=false;
		      sb.append("For C3 expected '"+expectedC3+"' got '"+c3+"'\n"); 
		  }

		  sb.append("Pass "+i+" Returned '"+c1+","+c2+","+c3+"\n");
		  cs.close();
	      }

	      conn65535.close();

   	       // cleanup
	      stmt.executeUpdate("Drop procedure "+lib+".JDCSRPGPG3");

	      cmd.close();
	      stmt.close();

	      assertCondition ( passed, sb);

	  } catch(Exception e) {
              output_.println(sb.toString());
	      output_.println("Exception SQL = "+sql);
	      if (e instanceof SQLException) {
		  SQLException sqlex = (SQLException) e;
                  output_.println("SQLCODE = "+sqlex.getErrorCode());
                  output_.println("SQLSTATE = "+sqlex.getSQLState());
                  output_.println("SQLMESSAGE = "+sqlex.getMessage());
	      }
	      failed (e, "Unexpected Exception -- new testcase added by native driver 06/16/08 ");
	  }

      }
  }


/**
 * Call a RPG stored procedure that returns array result set with the JOB CCSID set to 65535, but the procedure specifies the CCSID in the H spec
 */

  public void Var034()
  {

     

      if (checkJdbc20 ()) {
 
	  String rpmProgram[] = {
	      "      *  DVDSSERV -- Business Logic Service Program",
	      "      *",
	      "     H NOMAIN",
	      "     H AlwNull(*UsrCtl)",
              "     H CCSID(*CHAR : 37 )",   
	      "",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PR                  ",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "",
	      "     D earlySql        PR",
	      " ",
	      "",
	      "      *****************************************************",
	      "      * earlySql(): Sql init",
	      "      *****************************************************",
	      "     P earlySql        B",
	      "     D earlySql        PI",
	      "      /free",
	      "        // DatFmt must be first statement in module"     ,
	      "        EXEC SQL SET OPTION DatFmt = *ISO;",
	      "        // *CHG was *NONE",
	      "        exec SQL set option naming=*SYS,commit=*NONE;",
	      "      /end-free",
	      "     P                 E",
	      "",
	      "      *****************************************************",
	      "      *  ORDER_save_Purchase2_sp(): Routine to save purchase",
	      "      *    (Stored procedure interface for ORDER_save_Purchase2)",
	      "      *",
	      "      *    INCOME    = (input) income",
	      "      *    CUSTID    = customer id",
	      "      *    ORDERID   = order id",
	      "      *    ORDERDATE = order date",
	      "      *    ITEMID    = item number",
	      "      *    PRODID    = prod id",
	      "      *    QTY       = quantity",
	      "      * ",
	      "      *  Returns a result set with one row:",
	      "      *****************************************************",
	      "     P ORDER_save_Purchase2_sp...",
	      "     P                 B                   export",
	      "     D ORDER_save_Purchase2_sp...",
	      "     D                 PI",
	      "     D  CUSTID                       12p 0 const",
	      "     D  ORDERID                      12p 0 const",
	      "     D  ORDERDATE                      d   const",
	      "     D  ITEMID                       12p 0 const",
	      "     D  PRODID                       12p 0 const",
	      "     D  QTY                          12p 0 const",
	      "     ",
	      "     D Result6         ds                  qualified occurs(1)",
	      "     D  QTY                           9b 0 inz",
	      "     D  MsgId                         7a   inz ",
	      "     D  Msg                          80a   inz ",
	      "      /free",
	      "       %occur(RESULT6) = 1;",
	      "     ",
	      "       RESULT6.QTY        = QTY;",
	      "       RESULT6.MsgId      = 'ok';",
	      "       RESULT6.Msg        = 'Never better';",
	      "     ",
	      "     ",
	      "       exec SQL set Result sets Array :RESULT6 for 1 Rows;",
	      "      /end-free",
	      "     P                 E",
	      "     ",
	      "     "
	  };

	  String expectedC1 = "1";
	  String expectedC2 = "ok     ";
	  String expectedC3 = "Never better                                                                    "; 
	  String sql = "";
          StringBuffer sb = new StringBuffer();
	  boolean passed = true; 
	  try {

	      String lib = collection_; 
	      stringArrayToSourceFile(connection_,rpmProgram, lib, "JDCSRPGPG4");

	      CallableStatement cmd = connection2_.prepareCall("call QSYS2.QCMDEXC(?)");
	      String command = "QSYS/CRTSQLRPGI "+lib+"/JDCSRPGPG4 "+lib+"/JDCSRPGPG4 OBJTYPE(*MODULE) DBGVIEW(*SOURCE) ";

	      sql=command;
	      cmd.setString(1, command );
	      cmd.execute();

	      sql = "QSYS/CRTSRVPGM SRVPGM("+lib+"/JDCSRPGPG4)              MODULE("+lib+"/JDCSRPGPG4) EXPORT(*ALL)  ACTGRP(*CALLER) ";

	      cmd.setString(1, sql );
	      cmd.execute();


	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG4 " +
		"(IN CUSTID D    ECIMAL(12,0), "+
                " IN ORDERID     DECIMAL(12,0), "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
                " IN QTY        DECIMAL(12,0)) "+
		" dynamic result sets 1  "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPGM' "+
		" LANGUAGE RPGLE GENERAL ";

	      sql = "CREATE PROCEDURE "+lib+".JDCSRPGPG4(  " +
                " IN CUSTID      DECIMAL(12,0),  "+
                " IN ORDERID     DECIMAL(12,0),  "+
                " IN ORDERDATE   DATE, "+
                " IN ITEMID      DECIMAL(12,0), "+
                " IN PRODID      DECIMAL(12,0), "+
		" IN QTY         DECIMAL(12,0)) "+
		"   DYNAMIC RESULT SETS 1 "+
		" LANGUAGE RPGLE "+
		" NOT DETERMINISTIC "+
		" MODIFIES SQL DATA "+
		" EXTERNAL NAME '"+lib+"/JDCSRPGPG4(ORDER_SAVE_PURCHASE2_SP)' "+
		" PARAMETER STYLE GENERAL";

	      Statement stmt = connection2_.createStatement();
	      try {
		  stmt.executeUpdate("drop procedure "+lib+".JDCSRPGPG4");
	      } catch (Exception e) {
	      }
	      stmt.executeUpdate(sql);

	      Connection conn65535 =  testDriver_.getConnection (baseURL_ , userId_, encryptedPassword_);

	      Statement s65535 = conn65535.createStatement();
	      s65535.executeUpdate("CALL QSYS2.QCMDEXC ('CHGJOB CCSID(65535)') ");
	      s65535.close(); 

	      for (int i =0 ; i < 10; i++) {
		  sql = "call "+lib+".JDCSRPGPG4(?,?,?, ?, ?, ?)";
		  CallableStatement cs = conn65535.prepareCall(sql);

		  cs.setInt(1,1);
		  cs.setInt(2,1);
		  cs.setDate(3,new java.sql.Date(System.currentTimeMillis()));
		  cs.setInt(4,1);
		  cs.setInt(5,1);
		  cs.setInt(6,1);

		  cs.execute();
		  ResultSet rs = cs.getResultSet();
		  rs.next();
		  String c1 = rs.getString(1);
		  String c2 = rs.getString(2);
		  String c3 = rs.getString(3);
		  if (!expectedC1.equals(c1)) {
		      passed=false;
		      sb.append("For C1 expected '"+expectedC1+"' got '"+c1+"'\n"); 
		  }
		  if (!expectedC2.equals(c2)) {
		      passed=false;
		      sb.append("For C2 expected '"+expectedC2+"' got '"+c2+"'\n"); 
		  }

		  if (!expectedC3.equals(c3)) {
		      passed=false;
		      sb.append("For C3 expected '"+expectedC3+"' got '"+c3+"'\n"); 
		  }

		  sb.append("Pass "+i+" Returned '"+c1+","+c2+","+c3+"\n");
		  cs.close();
	      }

	      conn65535.close();

   	       // cleanup
	      stmt.executeUpdate("Drop procedure "+lib+".JDCSRPGPG4");

	      cmd.close();
	      stmt.close();

	      assertCondition ( passed, sb);

	  } catch(Exception e) {
              output_.println(sb.toString());
	      output_.println("Exception SQL = "+sql);
	      if (e instanceof SQLException) {
		  SQLException sqlex = (SQLException) e;
                  output_.println("SQLCODE = "+sqlex.getErrorCode());
                  output_.println("SQLSTATE = "+sqlex.getSQLState());
                  output_.println("SQLMESSAGE = "+sqlex.getMessage());
	      }
	      failed (e, "Unexpected Exception -- new testcase added by native driver 06/16/08 ");
	  }

      }
  }

  public void testGetDB2ParameterName(String sql, String[] parameters) {
    StringBuffer sb = new StringBuffer(" -- added by Toolbox 1/21/2015\n");
    try {
      boolean passed = true;

      sb.append("Preparing "+sql+"\n"); 
      CallableStatement ps = connection_.prepareCall(sql);
      if (ps instanceof AS400JDBCCallableStatement) {
        for (int i = 0; i < parameters.length; i++) {
          String parameterName = ((AS400JDBCCallableStatement) ps)
              .getDB2ParameterName(1 + i);
          if (!parameters[i].equals(parameterName)) {
            sb.append("For parameter " + (i + 1) + " got '" + parameterName
                + "' sb '" + parameters[i] + "'\n");
            passed = false;
          }
        }
      }
      ps.close();
      ps = null;
      assertCondition(passed, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception. Added by Toolbox 1/21/2015 " +sb.toString());
    }
  }

   public void Var035() { 
     String sql = "CALL " + JDSetupProcedure.STP_CSPARMS + " (?, ?, ?)"; 
     String[] parameters = {
         "P1", "P2", "P3"
     }; 
         
     testGetDB2ParameterName(sql, parameters);
   }

   public void Var036() { 
     String sql = "CALL " + JDSetupProcedure.STP_CSPARMS + " (P3=>?,P2=>?,P1=>?)"; 
     String[] parameters = {
         "P3", "P2", "P1"
     }; 
         
     testGetDB2ParameterName(sql, parameters);
   }

   public void Var037() { 
     String sql = "CALL " + JDSetupProcedure.STP_CSPARMS + " (P3=>?,P1=>?,P2=>?)"; 
     String[] parameters = {
         "P3", "P1", "P2"
     }; 
         
     testGetDB2ParameterName(sql, parameters);
   }

   
   public void Var038() { 
     String sql = "CALL " + JDSetupProcedure.STP_CSTYPESOUT + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
    String[] parameters = { "P_SMALLINT", // 1
        "P_INTEGER", // 2
        "P_REAL", // 3
        "P_FLOAT", // 4
        "P_DOUBLE", // 5
        "P_DECIMAL_50", // 6
        "P_DECIMAL_105", // 7
        "P_NUMERIC_50", // 8
        "P_NUMERIC_105", // 9
        "P_CHAR_1", // 10
        "P_CHAR_50", // 11
        "P_VARCHAR_50", // 12
        "P_BINARY_20", // 13
        "P_VARBINARY_20",// 14
        "P_DATE", // 15
        "P_TIME", // 16
        "P_TIMESTAMP", // 17
        "P_DATALINK",// 18
        "P_BLOB", // 19
        "P_CLOB", // 20
        "P_DBCLOB", // 21
        "P_BIGINT", // 22 // @B0A
        "P_DECFLOAT16", // 23
        "P_DECFLOAT34", // 24
    };
    
    String sql2 = "CALL " + JDSetupProcedure.STP_CSTYPESOUT + " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
    String[] parameters2 = { "P_SMALLINT", // 1
        "P_INTEGER", // 2
        "P_REAL", // 3
        "P_FLOAT", // 4
        "P_DOUBLE", // 5
        "P_DECIMAL_50", // 6
        "P_DECIMAL_105", // 7
        "P_NUMERIC_50", // 8
        "P_NUMERIC_105", // 9
        "P_CHAR_1", // 10
        "P_CHAR_50", // 11
        "P_VARCHAR_50", // 12
        "P_BINARY_20", // 13
        "P_VARBINARY_20",// 14
        "P_DATE", // 15
        "P_TIME", // 16
        "P_TIMESTAMP", // 17
        "P_DATALINK",// 18
        "P_BLOB", // 19
        "P_CLOB", // 20
        "P_DBCLOB", // 21
        "P_BIGINT", // 22 // @B0A
        "P_DECFLOAT16", // 23
        "P_DECFLOAT34", // 24
        "P_BOOLEAN"
    };
         
    if (areBooleansSupported()) {
      sql = sql2; 
      parameters = parameters2; 
    }
     testGetDB2ParameterName(sql, parameters);
   }

   
}



