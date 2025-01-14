///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AuthExit.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    AuthExit.java
//
// Classes:      AuthExit
//
////////////////////////////////////////////////////////////////////////

package test.Sec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AuthExit {

  // Private data.
  String letter_;
  int jdk_;
  static boolean exitProgramChecked_ = false;
  static boolean exitProgramEnabled_ = false;
  static boolean skipExitCleanup = false;

  public static void cleanup(Connection c) throws Exception {
    if (!skipExitCleanup) {
      cleanupExitProgam(c);
      cleanupExitProgramFiles(c);
      exitProgramChecked_ = false;
    }
  }

  /*
   * Creates and registers the AUTHEXIT program, if it has not yet been done. The
   * caller is responsible for determining that the connected system supports the
   * AUTHEXIT. Registers the userid to use MFA and the exit program.
   */

  public static void assureExitProgramExists(Connection pwrConnection, String userid) throws Exception {
    if (!exitProgramChecked_) {
      Statement stmt = pwrConnection.createStatement();
      /* Check the exit point program */
      String sql = "select EXIT_PROGRAM_LIBRARY, EXIT_PROGRAM " + "from qsys2.exit_program_info "
          + "WHERE EXIT_POINT_NAME='QIBM_QSY_AUTH' " + "ORDER BY EXIT_PROGRAM_NUMBER fetch first 1 rows only";
      String exitProgramLibrary = null;
      String exitProgram = null;
      ResultSet rs = stmt.executeQuery(sql);
      if (rs.next()) {
        exitProgramLibrary = rs.getString(1);
        exitProgram = rs.getString(2);
      }
      rs.close();
      if ((exitProgramLibrary != null && !exitProgramLibrary.equals("JDTESTINFO"))
          || (exitProgram != null && !exitProgram.equals("AUTHEXIT"))) {
        stmt.close();
        throw new SQLException(
            "INVALID EXIT PROGRAM " + exitProgramLibrary + "/" + exitProgram + " FOR QIBM_QSY_AUTH .. please remove");
      }
      if (exitProgramLibrary == null) {
        /* Create and install the exit program */
        String[] exitProgramText = { "#include <qusec.h>           ", "#include <stdio.h> ", "#include <qusrjobi.h>  ",
            "#include <stdlib.h> ", "#include <eauth1.h>          ", "#include <sys/stat.h>", "#include <qmhsndpm.h>",

            " int main (int argc, char *argv[]) {", "    int rc2;", "    Qus_EC_t errorCode;",
            "    Qwc_JOBI0600_t jobi;", "    char * dest;", "    char jobname[30];", "    char filename[80];",
            "    char ipAddress[50]; ", "    char * wrkFile; ", "    FILE * fp;", "    char       msg_key [4];",
            "    Qsy_Authentication_Info_t * info= (  Qsy_Authentication_Info_t *) argv[1];",
            "    int * rc = (int *) argv[2]; ", "    int i; ", "    QUSRJOBI(&jobi,", "             sizeof(jobi),",
            "             \"JOBI0600\",", "             \"*                         \",",
            "             \"                \");", "", "    strcpy(filename,\"/tmp/authexit\");",
            "    mkdir(filename, S_IRWXU | S_IRWXG | S_IRWXO );", "    wrkFile = filename+strlen(filename);",
            "    *wrkFile='/';", "    wrkFile++;", "", "     dest = jobname;", "    for (i = 0; i < 6; i++) {",
            "       *dest = jobi.Job_Number[i];", "       *wrkFile =  jobi.Job_Number[i];",
            "       if (*dest != ' ') {", "           dest++;", "           wrkFile++; ", "       }", "    }",
            "    *dest = '/'; dest++;", "    *wrkFile='.'; wrkFile++; ", "    for (i = 0; i < 10; i++) {",
            "      *dest = jobi.User_Name[i];", "      *wrkFile=jobi.User_Name[i];", "      if (*dest != ' ')  {",
            "          dest++;", "          wrkFile++; ", "      }", "    }", "    *dest = '/'; dest++;",
            "    *wrkFile = '.'; wrkFile++; ", "    for (i = 0; i < 10; i++) {", "      *dest = jobi.Job_Name[i];",
            "      *wrkFile = jobi.Job_Name[i];", "      if (*dest != ' ') {", "          dest++;",
            "          wrkFile++;", "      }", "    }", "    strcpy(wrkFile,\".txt\"); ", "    *dest = '\0'; ",
            "    memset(&errorCode,0,sizeof(errorCode)); ",

            "    QMHSNDPM (", "              \"CPF9898\",                /* message id                  */",
            "              \"QCPFMSG   *LIBL     \",   /* Qualified message file name */",
            "              filename,                  /* Message data                */",
            "              strlen(filename),          /* Length of message data      */",
            "              \"*DIAG     \",             /* Message type                */",
            "              \"*         \",             /* Call stack entry            */",
            "              0,                        /* Call stack counter          */",
            "              msg_key,                  /* Message key                 */",
            "              &errorCode                 /* Error code                  */", "              );",

            "     fp = fopen(filename,\"w\");", "    fprintf(fp,\"Exit_Point_Name=%20.20s\\n\",info->Exit_Point_Name);",
            "    fprintf(fp,\"Exit_Point_Format_Name=%8.8s\\n\",info->Exit_Point_Format_Name);",
            "    fprintf(fp,\"User_Profile_Name=%8.8s\\n\",info->User_Profile_Name);",
            "    fprintf(fp,\"Remote_Port=%d\\n\",info->Authentication_Caller.Remote_Port);",
            "    fprintf(fp,\"Local_Port=%d\\n\",info->Authentication_Caller.Local_Port);", "    strncpy(ipAddress,",
            "            info->Authentication_Caller.Remote_IPAddress,",
            "            info->Authentication_Caller.Remote_IPAddress_Len);",
            "    ipAddress[info->Authentication_Caller.Remote_IPAddress_Len]='\0';",
            "    fprintf(fp,\"Remote_IPAddress=%s\\n\",ipAddress);", "    strncpy(ipAddress,",
            "            info->Authentication_Caller.Local_IPAddress,",
            "            info->Authentication_Caller.Local_IPAddress_Len);",
            "    ipAddress[info->Authentication_Caller.Local_IPAddress_Len]='\0';",
            "    fprintf(fp,\"Local_IPAddress=%s\\n\",ipAddress);",

            "     fprintf(fp,\"Verification_ID=%.30s\\n\",",
            "            info->Authentication_Caller.Verification_ID);",

            "     fflush(fp); ", "    fclose(fp); ",

            "     return(0); ", "}", };
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < exitProgramText.length; i++) {
          sb.append(exitProgramText[i]);
          sb.append("\n");
        }
        sql = "CALL IFS_WRITE_UTF8(PATH_NAME=>?,LINE=>?,OVERWRITE=>'REPLACE')";
        PreparedStatement ps = pwrConnection.prepareStatement(sql);
        ps.setString(1, "/tmp/authexit.c");
        ps.setString(2, sb.toString());
        ps.execute();
        ps.close();

        sql = "CALL QSYS2.QCMDEXC('CRTCMOD MODULE(JDTESTINFO/AUTHEXIT) "
            + "SRCSTMF(''/tmp/authexit.c'') DBGVIEW(*ALL) SYSIFCOPT(*IFSIO)') ";
        stmt.execute(sql);

        sql = "CALL QSYS2.QCMDEXC('CRTPGM PGM(JDTESTINFO/AUTHEXIT) MODULE(JDTESTINFO/AUTHEXIT)')";
        stmt.execute(sql);

        sql = "CALL QSYS2.QCMDEXC('ADDEXITPGM EXITPNT(QIBM_QSY_AUTH) FORMAT(AUTH0100) PGMNBR(1) "
            + "PGM(JDTESTINFO/AUTHEXIT) THDSAFE(*YES)')";
        stmt.execute(sql);
        
        /* Need to make sure exit program is accessible  */ 
        sql = "CALL QSYS2.QCMDEXC('CHGAUT OBJ(''/qsys.lib/jdtestinfo.lib'') USER(*PUBLIC) DTAAUT(*RX) OBJAUT(*NONE) ')"; 
        stmt.execute(sql);
        sql = "CALL QSYS2.QCMDEXC('CHGAUT OBJ(''/qsys.lib/jdtestinfo.lib/authexit.pgm'') USER(*PUBLIC) DTAAUT(*RX) OBJAUT(*NONE) ')"; 
        stmt.execute(sql); 
        
      } /* exit program library is null */

      sql = "CALL QSYS2.QCMDEXC('CHGUSRPRF USRPRF(" + userid + ") AUTHMTH(*TOTP *REGFAC)')";
      stmt.execute(sql);
      stmt.close();
      exitProgramChecked_ = true;
      exitProgramEnabled_ = true;
    } /* exitProgramChecked_ */
  }

  public static void cleanupExitProgam(Connection c) throws SQLException {
    if (exitProgramEnabled_) {
      Statement stmt = c.createStatement();
      String sql = "CALL QSYS2.QCMDEXC('RMVEXITPGM EXITPNT(QIBM_QSY_AUTH) FORMAT(AUTH0100) PGMNBR(1)')";
      stmt.execute(sql);
      stmt.close();

    }
  }

  /* Remove the old exit program files */
  public static void cleanupExitProgramFiles(Connection conn) throws SQLException {
    Statement stmt = conn.createStatement();
    String sql = "select SYSTOOLS.IFS_UNLINK(PATH_NAME) " + "from table(qsys2.IFS_OBJECT_STATISTICS('/tmp/authexit')) "
        + "WHERE OBJECT_TYPE='*STMF'";
    ResultSet rs = stmt.executeQuery(sql);
    while (rs.next()) {

    }
    rs.close();
    stmt.close();
  }

}
