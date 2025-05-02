///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDJobName.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

import java.io.*; 

public class JDJobName {
    /* Change this name each time the nativeSource is changed */
    /* #11 - added getLoggingText */ 
    static String  srvpgm= "JDJOBNM011";

    static boolean loaded = false; 
    static boolean debug = false; 
    static String[] nativeSource = {
        "#include <jni.h>",
        "#include <time.h>",
        "#include <sys/time.h>",
        "#include <unistd.h>",
        /* remove the next line to compile on V5R1 */ 
        "/* #include <sys/resource.h> */ ",
        "#include <errno.h> ",
	"#include <qusrjobi.h> ",
	"#include <stdio.h>",
	"#include <stdlib.h>",
        "#include <ctype.h>",
        "#include <qwtchgjb.h>",
        "#include <qusec.h> ",
        "#include <qmhsndpm.h>",

        
        "#include <qsygetph.h>        /* QSYGETPH() */",
        "#include <qwtsetp.h>         /* QWTSETP() */",
        "#include <qsyrlsph.h>        /* QSYRLSPH() */",

	"#define NAMESIZE 30",
	"#ifdef  __ILEC400__",
	"      #pragma linkage(JDSETIGC,OS,nowiden)",
	" #else",
	"       extern \"OS\"",
	" #endif",
	"void JDSETIGC();",
        "", 
        "      char pex[NAMESIZE+1];",
        "      char ascpex[NAMESIZE+1];",

	"",
        "/* define etoa table */",
        "char etoa[] = {",
        "",
        "0x00,0x01,0x02,0x03,0x1A,0x09,0x1A,0x7F,0x1A,0x1A,0x1A,0x0B,0x0C,0x0D,0x0E,0x0F,",
        "0x10,0x11,0x12,0x13,0x1A,0x1A,0x08,0x1A,0x18,0x19,0x1A,0x1A,0x1C,0x1D,0x1E,0x1F,",
        "0x1A,0x1A,0x1C,0x1A,0x1A,0x0A,0x17,0x1B,0x1A,0x1A,0x1A,0x1A,0x1A,0x05,0x06,0x07,",
        "0x1A,0x1A,0x16,0x1A,0x1A,0x1E,0x1A,0x04,0x1A,0x1A,0x1A,0x1A,0x14,0x15,0x1A,0x1A,",
        "",
        "0x20,0xA6,0xE1,0x80,0xEB,0x90,0x9F,0xE2,0xAB,0x8B,0x9B,0x2E,0x3C,0x28,0x2B,0x7C,",
        "0x26,0xA9,0xAA,0x9C,0xDB,0xA5,0x99,0xE3,0xA8,0x9E,0x21,0x24,0x2A,0x29,0x3B,0x5E,",
        "0x2D,0x2F,0xDF,0xDC,0x9A,0xDD,0xDE,0x98,0x9D,0xAC,0xBA,0x2C,0x25,0x5F,0x3E,0x3F,",
        "0xD7,0x88,0x94,0xB0,0xB1,0xB2,0xFC,0xD6,0xFB,0x60,0x3A,0x23,0x40,0x27,0x3D,0x22,",
        "0xF8,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x96,0xA4,0xF3,0xAF,0xAE,0xC5,",
        "0x8C,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0x97,0x87,0xCE,0x93,0xF1,0xFE,",
        "0xC8,0x7E,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0xEF,0xC0,0xDA,0x5B,0xF2,0xF9,",
        "0xB5,0xB6,0xFD,0xB7,0xB8,0xB9,0xE6,0xBB,0xBC,0xBD,0x8D,0xD9,0xBF,0x5D,0xD8,0xC4,",
        "0x7B,0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0xCB,0xCA,0xBE,0xE8,0xEC,0xED,",
        "0x7D,0x4A,0x4B,0x4C,0x4D,0x4E,0x4F,0x50,0x51,0x52,0xA1,0xAD,0xF5,0xF4,0xA3,0x8F,",
        "/*           S    T    U    V    W    X    Y    Z    */",
        "0x5C,0xE7,0x53,0x54,0x55,0x56,0x57,0x58,0x59,0x5A,0xA0,0x85,0x8E,0xE9,0xE4,0xD1,",
        "/* 0    1    2    3    4    5    6    7    8    9    */",
        "0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0xB3,0xF7,0xF0,0xFA,0xA7,0xFF,",
        "};",
        "/* defined atoe table */",
        "char atoe[] = {",
        "0x00,0x01,0x02,0x03,0x37,0x2D,0x2E,0x2F,0x16,0x05,0x25,0x0B,0x0C,0x0D,0x0E,0x0F,",
        "0x10,0x11,0x12,0x13,0xB6,0xB5,0x32,0x26,0x18,0x19,0x1C,0x27,0x3F,0x1D,0x1E,0x1F,",
        "0x40,0x5A,0x7F,0x7B,0x5B,0x6C,0x50,0x7D,0x4D,0x5D,0x5C,0x4E,0x6B,0x60,0x4B,0x61,",
        "0xF0,0xF1,0xF2,0xF3,0xF4,0xF5,0xF6,0xF7,0xF8,0xF9,0x7A,0x5E,0x4C,0x7E,0x6E,0x6F,",
        "0x7C,0xC1,0xC2,0xC3,0xC4,0xC5,0xC6,0xC7,0xC8,0xC9,0xD1,0xD2,0xD3,0xD4,0xD5,0xD6,",
        "0xD7,0xD8,0xD9,0xE2,0xE3,0xE4,0xE5,0xE6,0xE7,0xE8,0xE9,0xBA,0xE0,0xBB,0xB0,0x6D,",
        "0x79,0x81,0x82,0x83,0x84,0x85,0x86,0x87,0x88,0x89,0x91,0x92,0x93,0x94,0x95,0x96,",
        "0x97,0x98,0x99,0xA2,0xA3,0xA4,0xA5,0xA6,0xA7,0xA8,0xA9,0xC0,0x4F,0xD0,0xA1,0xFF,",
        "0x68,0xDC,0x51,0x42,0x43,0x44,0x47,0x48,0x52,0x53,0x54,0x57,0x56,0x58,0x63,0x67,",
        "0x71,0x9C,0x9E,0xCB,0xCC,0xCD,0xDB,0xDD,0xDF,0xEC,0xFC,0x4A,0xB1,0xB2,0xBF,0x07,",
        "0x45,0x55,0xCE,0xDE,0x49,0x69,0x9A,0x9B,0xAB,0xAF,0x5F,0xB8,0xB7,0xAA,0x8A,0x8B,",
        "0x2B,0x2C,0x09,0x21,0x28,0x65,0x62,0x64,0xB4,0x38,0x31,0x34,0x33,0x70,0x80,0x24,",
        "0x22,0x17,0x29,0x06,0x20,0x2A,0x46,0x66,0x1A,0x35,0x08,0x39,0x36,0x30,0x3A,0x9F,",
        "0x8C,0xAC,0x72,0x73,0x74,0x0A,0x75,0x76,0x77,0x23,0x15,0x14,0x04,0x6A,0x78,0x3B,",
        "0xEE,0x59,0xEB,0xED,0xCF,0xEF,0xA0,0x8E,0xAE,0xFE,0xFB,0xFD,0x8D,0xAD,0xBC,0xBE,",
        "0xCA,0x8F,0x1B,0xB9,0x3C,0x3D,0xE1,0x9D,0x90,0xBD,0xB3,0xDA,0xFA,0xEA,0x3E,0x41",
        "};",
        "",
	"jint Java_test_JDJobName_setIGCnative(JNIEnv * env, jobject obj, jint ccsid )",
	"{",
        "    Qus_Job_Change_Information_t * jobI;",
	"    Qus_JOBC0100_t * jobC;",
        "    char * data; ",
        "    char buffer[200]; ",
        "    Qus_EC_t errorCode;",

        "    memset(&errorCode, 0, sizeof(errorCode));", 
	"    jobI = (Qus_Job_Change_Information_t *) buffer;",
        "    jobC = (Qus_JOBC0100_t *) (buffer + sizeof(Qus_Job_Change_Information_t));",
        "    data = (char *)    (jobC+1); ", 
        "    jobI->Number_Fields_Enterd = 1; ",
	"    jobC->Length_Field_Info_=  4+ sizeof(Qus_JOBC0100_t); ",
        "    jobC->Key_Field = 302 ;",
        "    jobC->Type_Of_Data = 'B'; ",
        "    memset(jobC->Reserved,' ',3);",
        "    jobC->Length_Data = 4;",
        "    * ((int *)data) = ccsid; ", 
        "",
        /* I tried using a CL program that called SETIGC */
	/* but that didn't seem to help.  Stick with other way */ 
        /* "    JDSETIGC();", */ 
	"    system(\"QBLDSYS/SETIGC CHANGE(*WCB) VALUE(*YES)\");",  
        "    /* Use QWTCHGJB api */ ",
        "    errorCode.Bytes_Provided = sizeof(Qus_EC_t);", 
	"    QWTCHGJB(\"*                           \",",
        "              \"                    \",        ",
        "              \"JOBC0100\",                    ",
        "              jobI,                            ",
        "              &errorCode);                     ",
	"    sprintf(buffer, \"CHGJOB CCSID(%d)\", ccsid);", 
        "    /* system(buffer); */",
	"    sprintf(buffer, \"CHGJOB CCSID(%d)\", ccsid+1);", 
        "    /* system(buffer); */ ",
	"    return (jint) 0; ",
	"}",

        "", 
	"jint Java_test_JDJobName_getJobMemNative(JNIEnv * env)",
	"{",
	"    Qwc_JOBI0150_t jobi;",
	"    QUSRJOBI(&jobi,",
	"             sizeof(jobi),",
	"             \"JOBI0150\",",
	"             \"*                         \",",
	"             \"                \");",
	"    return (jint) jobi.Temp_Storage_Used; ",
	"}",
	"", 
	"jint Java_test_JDJobName_setJobLogOptionNative(JNIEnv * env)",
	"{",
	"    system(\"QSYS/CHGJOB LOG(4 00 *SECLVL)\");",
	"    return (jint) 0; ",
	"}",
	"", 
        "", 
	"jint Java_test_JDJobName_getJobCCSIDNative(JNIEnv * env)",
	"{",
	"    Qwc_JOBI0400_t jobi;",
	"    QUSRJOBI(&jobi,",
	"             sizeof(jobi),",
	"             \"JOBI0400\",",
	"             \"*                         \",",
	"             \"                \");",
	"    return (jint) jobi.Coded_Char_Set_ID; ",
	"}",
	"", 
        "jstring Java_test_JDJobName_getJobNameNative(",
        "  JNIEnv *env,",
        "  jobject obj)",
        "",
        "{",
        "      int i;", 
        "      char jobname[NAMESIZE+1];",
	"      char * dest; ", 
	"      Qwc_JOBI0100_t jobi;",
	"      QUSRJOBI(&jobi,",
	"               sizeof(jobi),",
	"               \"JOBI0100\",",
	"               \"*                         \",",
	"               \"                \");",
	"       dest = jobname;",
	"       for (i = 0; i < 6; i++) {",
	"          *dest = etoa[jobi.Job_Number[i]];",
	"          if (*dest != 0x20) dest++; ",
	"       }",
	"       *dest = etoa['/']; dest++; ",
	"       for (i = 0; i < 10; i++) {",
	"         *dest = etoa[jobi.User_Name[i]];",
	"          if (*dest != 0x20) dest++; ",
	"       }",
	"       *dest = etoa['/']; dest++; ",
	"       for (i = 0; i < 10; i++) {",
	"         *dest = etoa[jobi.Job_Name[i]];",
	"          if (*dest != 0x20) dest++; ",
	"       } ",
	"       *dest = 0; ",
	"", 
        "       /* Create java string */ ",
	"       return (*env)->NewStringUTF(env, jobname);", 
        "}",
        "",
        "jstring Java_test_JDJobName_getSubsystemNameNative(",
        "  JNIEnv *env,",
        "  jobject obj)",
        "",
        "{",
        "      int i;", 
        "      char subsystemname[11];",
	"      char * dest; ", 
	"      Qwc_JOBI0200_t jobi;",
	"      QUSRJOBI(&jobi,",
	"               sizeof(jobi),",
	"               \"JOBI0200\",",
	"               \"*                         \",",
	"               \"                \");",
	"       dest = subsystemname;",
	"       for (i = 0; i < 10; i++) {",
	"          *dest = etoa[jobi.Subsys_Name[i]];",
	"          if (*dest != 0x20) dest++; ",
	"       }",
	"       *dest = 0; ",
	"", 
        "       /* Create java string */ ",
	"       return (*env)->NewStringUTF(env, subsystemname);", 
        "}",
        "",
        "jstring Java_test_JDJobName_getLoggingTextNative(",
        "  JNIEnv *env,",
        "  jobject obj)",
        "",
        "{",
        "      int i;", 
        "      char loggingText[11];",
	"      char * dest; ", 
	"      Qwc_JOBI0500_t jobi;",
	"      QUSRJOBI(&jobi,",
	"               sizeof(jobi),",
	"               \"JOBI0500\",",
	"               \"*                         \",",
	"               \"                \");",
	"       dest = loggingText;",
	"       for (i = 0; i < 10; i++) {",
	"          *dest = etoa[jobi.Log_Text[i]];",
	"          if (*dest != 0x20) dest++; ",
	"       }",
	"       *dest = 0; ",
	"", 
        "       /* Create java string */ ",
	"       return (*env)->NewStringUTF(env, loggingText);", 
        "}",
        "",
        "jstring Java_test_JDJobName_startPexNative(",
        "  JNIEnv *env,",
        "  jobject obj)",
        "",
        "{",
        "      int i;",
        "      char buffer[4096]; ",
        "      char jobname[NAMESIZE+1];",
	"      char * dest; ",
	"      char * pascpex; ",
        "      char * ppex;", 
	"      Qwc_JOBI0100_t jobi;",
	"      QUSRJOBI(&jobi,",
	"               sizeof(jobi),",
	"               \"JOBI0100\",",
	"               \"*                         \",",
	"               \"                \");",
	"       dest = jobname;",
	"       pascpex = ascpex;",
        "       ppex=pex;",
	"       *ppex = 'P';",
        "       *pascpex = etoa['P'];",
	"       ppex++; pascpex++; ",
	"       *ppex = 'E';",
        "       *pascpex = etoa['E'];",
	"       ppex++; pascpex++; ",
	"       *ppex = 'X';",
        "       *pascpex = etoa['X'];",
	"       ppex++; pascpex++; ",

	"       for (i = 0; i < 6; i++) {",
	"          *pascpex = etoa[jobi.Job_Number[i]];",
	"          *ppex = jobi.Job_Number[i];",
	"           ppex++; pascpex++; ",
	"          *dest = jobi.Job_Number[i];",
        "           dest++;", 
	"       }",
	"       *dest = '/'; dest++; ",
	"       for (i = 0; i < 10; i++) {",
	"         *dest = jobi.User_Name[i];",
	"          if (*dest != 0x40) dest++; ",
	"       }",
	"       *dest = '/'; dest++; ",
	"       for (i = 0; i < 10; i++) {",
	"         *dest = jobi.Job_Name[i];",
	"          if (*dest != 0x40) dest++; ",
	"       } ",
	"       *dest = 0; ",
	"       *pascpex = 0; ",
	"       *ppex = 0; ",
	"",
        "       sprintf(buffer, ",
        "               \"QSYS/ADDPEXDFN DFN(%s) TYPE(*PROFILE)  PRFTYPE(*JOB)  JOB((%s))  MAXSTG(100000) INTERVAL(0.1)\",",
        "               pex,",
        "               jobname);",
	"       printf(\"%s\\n\", buffer);", 
	"       system(buffer);",
        "       sprintf(buffer, ",
	"              \"QSYS/STRPEX SSNID(%s) DFN(%s)\", ",
        "              pex, pex);",
  	"       system(buffer);",
        "       /* Create java string */ ",
	"       return (*env)->NewStringUTF(env, ascpex);", 
        "}",
        "",
	"",
        "jstring Java_test_JDJobName_endPexNative(",
        "  JNIEnv *env,",
        "  jobject obj)",
        "",
        "{",
        "    char buffer[200]; ",
        "       sprintf(buffer, ",
	"              \"QSYS/ENDPEX SSNID(%s) \", ",
        "              pex);",
  	"       system(buffer);",
        "       /* Create java string */ ",
	"       return (*env)->NewStringUTF(env, ascpex);", 
        "}",
        "",
	"",

        "#define NAME_SIZE 11",
        "#define NULL_PH \"\0\0\0\0\0\0\0\0\0\0\0\0\"",
        "#define PH_SIZE 12",
	"char server_ph[PH_SIZE+1] = NULL_PH; /* Server's profile handle */",
	"char client_ph[PH_SIZE+1] = NULL_PH; /* Client's profile handle */",
	"char server_profile[NAME_SIZE] = \"*CURRENT  \";",
	"char no_pwd[NAME_SIZE]         = \"*NOPWD    \";",

        "jstring errorString(JNIEnv * env, int rc, Qus_EC_t * error) {",
        "   char errorMsg[80];",
        "   int i; ",
        "   errorMsg[0]= 0x30 + rc; ", 
        "   errorMsg[1]=0x20;",
        "   for (i = 0; i < 7; i++) {",
        "      errorMsg[i+2] = etoa[error->Exception_Id[i]];",
        "   }",
        "   errorMsg[i+2]=0;",
        "   return (*env)->NewStringUTF(env, errorMsg);", 
        "}",
        "", 
	"jstring Java_test_JDJobName_swapToNative(JNIEnv * env, jobject obj, ",
	"                                                    jstring userid, ",
        "                                                    jstring password  )",
	"{",
        "  char pad1[80]; ",
        "  Qus_EC_t error = { sizeof(Qus_EC_t)+80, 0 }; /* Error code for SPIs */",
        "  char pad2[80]; ",
        "  char sy_profile[NAME_SIZE]; ",
        "  char sy_password[NAME_SIZE];",
        "  jboolean isCopy; ",
        "  int i; ",
        "  char * utfString; ",
        "  int strLen;",
        "  strLen =       (*env)->GetStringUTFLength(env, userid);",
        "  utfString = (char *) ((*env)->GetStringUTFChars(env, userid, &isCopy));  ",
        "  for (i = 0; i < NAME_SIZE; i++) { ",
        "     if ( i < strLen ) {",
        "       sy_profile[i] = atoe[utfString[i]];",
        "       if (islower(sy_profile[i])) {",
        "         sy_profile[i] = toupper(sy_profile[i]);",
        "       }",
        "     } else {",
        "       sy_profile[i]=' ';",
        "     }",
        "  }",
        "  sy_profile[NAME_SIZE-1]='\0';",
        "  (*env)->ReleaseStringUTFChars(env, userid, utfString); ",
        "",
        "  strLen =    (*env)->GetStringUTFLength(env, password);",
        "  utfString = (char *) ( (*env)->GetStringUTFChars(env, password, &isCopy));  ",
        "  for (i = 0; i < NAME_SIZE; i++) { ",
        "     if ( i < strLen ) {",
        "       sy_password[i] = atoe[utfString[i]];",
        "     } else {",
        "       sy_password[i]=' ';",
        "     }",
        "  }",
        "  sy_password[NAME_SIZE-1]='\0';",
        "  (*env)->ReleaseStringUTFChars(env, password, utfString); ",
        
        "",
        "  /* Initialize the server's profile handle. */",
        "  QSYGETPH(server_profile, no_pwd, server_ph, &error);",
        "  if (error.Bytes_Available != 0) {",
        "    return errorString(env, 1, &error); ",
        "  }",
        "",
        "  /* Get the profile handle for the client's user profile. */",
        "  QSYGETPH(sy_profile, sy_password, client_ph, &error, strlen(sy_password), 37 /* CCSID */ ); ",
        "  if (error.Bytes_Available != 0) {",
        "      return errorString(env, 2, &error); ",
        "  }",
        "",
        "  /* Switch to client's user profile. */",
        "  QWTSETP(client_ph, &error);",
        "  if (error.Bytes_Available != 0) {",
        "      QSYRLSPH(client_ph, NULL);",
        "      return errorString(env, 3, &error);",
        "  }",
        "",
        "  return 0;",
        "}",
        "", 
	"jstring Java_test_JDJobName_swapBackNative(JNIEnv * env, jobject obj) ",
	"{",
        "  Qus_EC_t error = { sizeof(Qus_EC_t), 0 }; /* Error code for SPIs */",
        "",
        "      /* Switch back to the server's user profile. */",
        "      QWTSETP(server_ph, &error);",
        "      if (error.Bytes_Available != 0) {",
        "	  return errorString(env,4,&error); ",
        "      }",
        "",
        "      /* Release the client's profile handle. */",
        "      QSYRLSPH(client_ph, &error);",
        "      if (error.Bytes_Available != 0) {",
        "	  return errorString(env,5,&error); ",
        "      }",
        "      return 0; ",
        "}",
	"jstring Java_test_JDJobName_setaspgrpNative(JNIEnv * env, jobject obj, ",
	"                                                    jstring aspgrp  )",
	"{",
        "  char command[160]; ", 
        "  char sy_aspgrp[80]; ",
        "  jboolean isCopy; ",
        "  int i; ",
        "  char * utfString; ",
        "  int strLen;",
        "  strLen =       (*env)->GetStringUTFLength(env, aspgrp);",
        "  utfString = (char *) ((*env)->GetStringUTFChars(env, aspgrp, &isCopy));  ",
        "  for (i = 0; i < NAME_SIZE; i++) { ",
        "     if ( i < strLen ) {",
        "       sy_aspgrp[i] = atoe[utfString[i]];",
        "       if (islower(sy_aspgrp[i])) {",
        "         sy_aspgrp[i] = toupper(sy_aspgrp[i]);",
        "       }",
        "     } else {",
        "       sy_aspgrp[i]=' ';",
        "     }",
        "  }",
        "  sy_aspgrp[NAME_SIZE-1]='\0';",
        "  (*env)->ReleaseStringUTFChars(env, aspgrp, utfString); ",
        "   sprintf(command, \"SETASPGRP %s\", sy_aspgrp); ",
	"   system(command); ", 
        "",
        "  return 0;",
        "}",
        "", 

        "",
	"jstring Java_test_JDJobName_systemNative(JNIEnv * env, jobject obj, ",
        "                                                    jstring command  )",
	"{",
        "  jstring rc = 0;", 
        "  char ebcdicCommand [512]; ",
        "  jboolean isCopy; ",
        "  char * utfString; ",
        "  int strLen;",
        "  int i; ",
        "  int rc2; ", 
        "  strLen =       (*env)->GetStringUTFLength(env, command);",
	"  if (strLen > sizeof(ebcdicCommand)) { ",
	"#pragma convert(819)",
	"    return (*env)->NewStringUTF(env, \"ERROR:  Length of command too long\");",
	"#pragma convert(0)",
	"  } ", 
        "  utfString = (char *) ((*env)->GetStringUTFChars(env, command, &isCopy));  ",
        "  for (i = 0; i < strLen; i++) { ",
        "       ebcdicCommand[i] = atoe[utfString[i]];",
        "  }",
        "  ebcdicCommand[i]=0;",
        "  rc2 = system(ebcdicCommand);",
	"  if (rc2 != 0) { ",
        "   char ebcdicErrorMessage[512];", 
        "   char errorMsg[512];",
        "   sprintf(ebcdicErrorMessage, \"system failed with rc=%d for '%s'\",rc2,ebcdicCommand); ", 
        "   errorMsg[1]=0x20;",
        "   for (i = 0; i < strlen(ebcdicErrorMessage); i++) {",
        "      errorMsg[i] = etoa[ebcdicErrorMessage[i]];",
        "   }",
        "   errorMsg[i]=0;",
        "   rc = (*env)->NewStringUTF(env, errorMsg);", 

	"  } ", 
        "  (*env)->ReleaseStringUTFChars(env, command, utfString); ",
        "",
        "  return rc;",
        "}",
        "", 


        "",
        "jstring Java_test_JDJobName_sendProgramMessageNative(JNIEnv * env, jobject obj, ",
        "                                                    jstring message  )",
        "{",
        "  jstring rc = 0;", 
        "  char ebcdicmessage [512]; ",
        "  jboolean isCopy; ",
        "  char * utfString; ",
        "  int strLen;",
        "  int i; ",
        "  int rc2; ", 
        "  Qus_EC_t   err_code = { sizeof(Qus_EC_t), 0 }; /* Error code for SPIs */",
        "  char       msg_key [4];",
        "  strLen =       (*env)->GetStringUTFLength(env, message);",
        "  if (strLen > sizeof(ebcdicmessage)) { ",
        "#pragma convert(819)",
        "    return (*env)->NewStringUTF(env, \"ERROR:  Length of message too long\");",
        "#pragma convert(0)",
        "  } ", 
        "  utfString = (char *) ((*env)->GetStringUTFChars(env, message, &isCopy));  ",
        "  for (i = 0; i < strLen; i++) { ",
        "       ebcdicmessage[i] = atoe[utfString[i]];",
        "  }",
        "  ebcdicmessage[i]=0;",
        "  err_code.Bytes_Provided = 16; ",
        "  QMHSNDPM (",
        "   \"CPF9898\",                /* message id                  */",
        "   \"QCPFMSG   *LIBL     \",   /* Qualified message file name */  ",    
        "   ebcdicmessage,                  /* Message data                */",      
        "   strlen(ebcdicmessage),          /* Length of message data      */  ",    
        "   \"*DIAG     \",             /* Message type                */      ",
        "   \"*         \",             /* Call stack entry            */      ",
        "   0,                        /* Call stack counter          */      ",
        "   msg_key,                  /* Message key                 */      ",
        "   &err_code                 /* Error code                  */      ",
        ");",
        "  (*env)->ReleaseStringUTFChars(env, message, utfString); ",
        "  if (err_code.Bytes_Available != 0) {",
        "      return errorString(env, 2, &err_code); ",
        "  }",
        "",
        "  return rc;",
        "}",
        "", 


        
    };

     public static char EtoChar[] = {
(char)0x00,(char)0x01,(char)0x02,(char)0x03,       ' ',      '\t',       ' ',(char)0x7f, /* 00-07 */
       ' ',       ' ',       ' ',(char)0x0b,      '\f',      '\r',(char)0x0e,(char)0x0f, /* 08-0f */
(char)0x10,(char)0x11,(char)0x12,(char)0x13,       ' ',      '\n',      '\b',       ' ', /* 10-17 */
(char)0x18,(char)0x19,       ' ',       ' ',       ' ',(char)0x1d,(char)0x1e,(char)0x1f, /* 18-1f */
       ' ',       ' ',(char)0x1c,       ' ',       ' ',      '\n',(char)0x17,(char)0x1b, /* 20-27 */
       ' ',       ' ',       ' ',       ' ',       ' ',(char)0x05,(char)0x06,(char)0x07, /* 28-2f */
       ' ',       ' ',(char)0x16,       ' ',       ' ',       ' ',       ' ',(char)0x04, /* 30-37 */
       ' ',       ' ',       ' ',       ' ',(char)0x14,(char)0x15,       ' ',(char)0x1a, /* 38-3f */
       ' ',       ' ',(char)0x83,(char)0x84,(char)0x85,(char)0xa0,(char)0xc6,(char)0x86, /* 40-47 */
(char)0x87,(char)0xa4,(char)0xbd,       '.',(char)0x3c,       '(',       '+',(char)0x7c, /* 48-4f */
       '&',(char)0x82,(char)0x88,(char)0x89,(char)0x8a,(char)0xa1,(char)0x8c,(char)0x8b, /* 50-57 */
(char)0x8d,(char)0xe1,       '!',       '$',       '*',       ')',       ';',(char)0xaa, /* 58-5f */
       '-',       '/',(char)0xb6,(char)0x8e,(char)0xb7,(char)0xb5,(char)0xc7,(char)0x8f, /* 60-67 */
(char)0x80,(char)0xa5,(char)0xdd,       ',',       '%',       '_',       '>',       '?', /* 68-6f */
(char)0x9b,(char)0x90,(char)0xd2,(char)0xd3,(char)0xd4,(char)0xd6,(char)0xd7,(char)0xd8, /* 70-77 */
(char)0xde,       '`',       ':',       '#',       '@',      '\'',       '=',       '"', /* 78-7f */
(char)0x9d,       'a',       'b',       'c',       'd',       'e',       'f',       'g', /* 80-87 */
       'h',       'i',(char)0xae,(char)0xaf,(char)0xd0,(char)0xec,(char)0xe7,(char)0xf1, /* 88-8f */
(char)0xf8,       'j',       'k',       'l',       'm',       'n',       'o',       'p', /* 90-97 */
       'q',       'r',(char)0xa6,(char)0xa7,(char)0x91,(char)0xf7,(char)0x92,(char)0xcf, /* 98-9f */
(char)0xe6,       '~',       's',       't',       'u',       'v',       'w',       'x', /* a8-a7 */
       'y',       'z',(char)0xad,(char)0xa8,(char)0xd1,(char)0xed,(char)0xe8,(char)0xa9, /* a8-af */
       '^',(char)0x9c,(char)0xbe,(char)0xfa,(char)0xb8,(char)0x15,(char)0x14,(char)0xac, /* b0-b7 */
(char)0xab,(char)0xf3,(char)0x5b,(char)0x5d,(char)0xee,(char)0xf9,(char)0xef,(char)0x9e, /* b8-bf */
(char)0x7b,       'A',       'B',       'C',       'D',       'E',       'F',       'G', /* c0-c7 */
       'H',       'I',(char)0xf0,(char)0x93,(char)0x94,(char)0x95,(char)0xa2,(char)0xe4, /* c8-cf */
(char)0x7d,       'J',       'K',       'L',       'M',       'N',       'O',       'P', /* d0-d7 */
       'Q',       'R',(char)0xfb,(char)0x96,(char)0x81,(char)0x97,(char)0xa3,(char)0x98, /* d8-df */
      '\\',(char)0xf6,       'S',       'T',       'U',       'V',       'W',       'X', /* e0-e7 */
       'Y',       'Z',(char)0xfc,(char)0xe2,(char)0x99,(char)0xe3,(char)0xe0,(char)0xe5, /* e8-ef */
       '0',       '1',       '2',       '3',       '4',       '5',       '6',       '7', /* f0-f7 */
       '8',       '9',(char)0xfd,(char)0xea,(char)0x9a,(char)0xeb,(char)0xe9,(char)0xff  /* f8-ff */
     };


		static {

			//
			// Only build on iSeries machine
			//
			if (JTOpenTestEnvironment.isOS400) {

				String debugString = System.getProperty("debug");
				if (debugString != null)
					debug = true;

				//
				// Make sure the Library exists QGPL/CURJOBNAME
				// If it doesn't then build it
				//
				String libraryName = "/QSYS.LIB/QGPL.LIB/" + srvpgm + ".SRVPGM";

				File serviceProgramFile = new File(libraryName);
				if (!serviceProgramFile.exists()) {
					try {
						Runtime runtime;
						Process process;
						runtime = Runtime.getRuntime();

						String command = "rm -f /tmp/JOBNAME.c";
						if (debug)
							System.out.println("JobName.debug: " + command);
						process = runtime.exec(command);
						if (debug)
							showProcessOutput(process, null);
						process.waitFor();

						command = "touch -C 819 /tmp/JOBNAME.c";
						if (debug)
							System.out.println("JobName.debug: " + command);
						process = runtime.exec(command);
						if (debug)
							showProcessOutput(process, null);
						process.waitFor();

						PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/JOBNAME.c")));

						for (int i = 0; i < nativeSource.length; i++) {
							writer.println(nativeSource[i]);
						}
						writer.close();

						command = "system  CRTCMOD MODULE(QGPL/" + srvpgm
								+ ") SRCSTMF('/tmp/JOBNAME.c')  DBGVIEW(*ALL) TERASPACE(*YES) STGMDL(*INHERIT) ";
						if (debug)
							System.out.println("JobName.debug: " + command);
						process = runtime.exec(command);
						if (debug)
							showProcessOutput(process, null);
						process.waitFor();

						command = "system  CRTSRVPGM SRVPGM(QGPL/" + srvpgm + ") MODULE(QGPL/" + srvpgm
								+ ") EXPORT(*ALL) STGMDL(*INHERIT) ";
						if (debug)
							System.out.println("JobName.debug: " + command);
						process = runtime.exec(command);
						if (debug)
							showProcessOutput(process, null);
						process.waitFor();

					} catch (Exception e) {
						System.out.println("Exception caught building service program for JobName");
						e.printStackTrace();
					}
				}
				System.out.println("Calling System.load(" + libraryName + ")");
				System.load(libraryName);
				System.out.println("Completed System.load(" + libraryName + ")");
				loaded = true;

			}

		}

	public static void showProcessOutput(Process p, String outfile) throws Exception {
		PrintWriter writer = null;
		try {

			if (outfile != null) {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(outfile)));
			}

			int[] buffer = new int[4096];
			int bufferCount = 0;
			int asciiCount = 0;
			int ebcdicCount = 0;
			InputStream iStream = p.getInputStream();
			int readByte;
			int outByte;
			if (debug)
				System.out.println("JobName:debug: ");
			readByte = iStream.read();
			while (readByte != -1) {
				//
				// If the character appears to be EBCDIC convert it to ascii
				//
				outByte = readByte;
				if ((writer != null) || debug) {

					if (readByte > 128) {
						outByte = EtoChar[readByte];
						ebcdicCount++;
					} else {
						//
						// Handle more EBCDIC characters if you think the output is
						// EBCDIC
						//
						if ((readByte == 0x40) || (readByte == 0x15) || (ebcdicCount > asciiCount)) {
							outByte = EtoChar[readByte];
						} else {
							asciiCount++;
						}
					}
					if (bufferCount < buffer.length) {
						buffer[bufferCount] = readByte;
						bufferCount++;
					}
				}
				if (debug)
					System.out.write((char) outByte);
				if (writer != null)
					writer.write((char) outByte);
				readByte = iStream.read();
			}

			// System.out.println();
			if (debug) {
				if (bufferCount > 0) {
					System.out.println("JobName:debug: ");
					System.out.println("JobName:debug: Dumping the stdout buffer (in hex)");
					System.out.println("JobName:debug: ");
					for (int i = 0; i < bufferCount; i++) {
						System.out.print(" " + Integer.toHexString(buffer[i]));
					}
					System.out.println();
					System.out.println("JobName:debug: ");
				}
			}

			//
			// Also dump the error stream
			//
			bufferCount = 0;
			ebcdicCount = 0;
			asciiCount = 0;
			iStream = p.getErrorStream();
			if (debug)
				System.out.println("JobName:debug: ");
			readByte = iStream.read();
			while (readByte != -1) {
				outByte = readByte;
				//
				// If the character appears to be EBCDIC convert it to ascii
				//
				if ((writer != null) || debug) {

					if (readByte > 128) {
						outByte = EtoChar[readByte];
						ebcdicCount++;
					} else {
						if ((readByte == 0x40) || (readByte == 0x15) || (ebcdicCount > asciiCount)) {
							outByte = EtoChar[readByte];
						} else {
							outByte = readByte;
							asciiCount++;
						}

					}
					if (bufferCount < buffer.length) {
						buffer[bufferCount] = readByte;
						bufferCount++;
					}
				}

				if (debug)
					System.out.write((char) outByte);
				if (writer != null)
					writer.write((char) outByte);

				readByte = iStream.read();
			}
			if (debug) {
				if (bufferCount > 0) {
					System.out.println();
					System.out.println("JobName:debug: ");
					System.out.println("JobName:debug: Dumping the stderr buffer (in hex)");
					System.out.println("JobName:debug: ");
					for (int i = 0; i < bufferCount; i++) {
						System.out.print(" " + Integer.toHexString(buffer[i]));
					}
					System.out.println();
					System.out.println("JobName:debug: ");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (writer != null)
			writer.close();
	}


    public static native String getJobNameNative();

	public static String getJobName() {
		if (loaded) {
			try {
				return getJobNameNative();
			} catch (UnsatisfiedLinkError e) {
				System.out.println("UnsatisfiedLinkError in JDJobName");
				e.printStackTrace(System.out);
				return "99999/ERRROR/ERROR";

			}
		} else {
			try {
				Object processHandle = JDReflectionUtil.callStaticMethod_O("java.lang.ProcessHandle", "current");
				return processHandle.toString() + "/PROCESS/HANDLE";
			} catch (Exception e) {
				e.printStackTrace(System.out);
				return "not Available because native method did not load ";
			}
		}
	}

    public static native String getSubsystemNameNative();

	public static String getSubsystemName() {
		if (loaded) {
			return getSubsystemNameNative();
		} else {
			return "not Available because native method did not load";
		}
	}

    public static native String getLoggingTextNative();

	public static String getLoggingText() {
		try {
			if (loaded) {
				return getLoggingTextNative();
			} else {
				return "not Available because native method did not load";
			}
		} catch (UnsatisfiedLinkError e) {
			System.out.println("UnsatisfiedLinkError in JDJobName.getLoggingText");
			e.printStackTrace(System.out);
			return "UNSATISIFED_LINK_ERRORR";

		}
	}


    public static native int getJobMemNative();

	public static int getJobMem() {
		if (loaded) {
			return getJobMemNative();
		} else {
			return -1;
		}
	}

    public static native int getJobCCSIDNative();

	public static int getJobCCSID() {
		if (loaded) {
			return getJobCCSIDNative();
		} else {
			return -1;
		}
	}

    public static native int setIGCnative(int ccsid);

	public static int setIGC(int ccsid) {
		if (loaded) {
			return setIGCnative(ccsid);
		} else {
			return -1;
		}
	}

    public static native int setJobLogOptionNative();

	public static int setJobLogOption() {
		if (loaded) {
			return setJobLogOptionNative();
		} else {
			return -1;
		}
	}

    public static native String startPexNative(); 

	public static String startPex() {
		if (loaded) {
			return startPexNative();
		} else {
			return "notAvailable";
		}
	}

    public static native String endPexNative();

	public static String endPex() {
		if (loaded) {
			return endPexNative();
		} else {
			return "notAvailable";
		}
	}

    /**
     * swap to a new profile.
     * throws exception if not successful.
     */ 

    public static native String swapToNative(String userID, String password);

	public static void swapTo(String userID, String password) throws Exception {
		if (loaded) {
			String rc = swapToNative(userID, password);
			if (rc != null) {
				throw new Exception("Unable to swap profile to " + userID + ", errorcode = " + rc
						+ " \nExamples of possibleErrors: CPF4AB8(Insufficient authority for user profile) CPF22E2(PASSWORD("
						+ password + ") NOT CORRECT)");
			}
		} else {
			throw new Exception("Error.. native method not loaded");
		}
	}

    public static native String swapBackNative();

	public static void swapBack() throws Exception {
		if (loaded) {
			String rc = swapBackNative();
			if (rc != null) {
				throw new Exception("Unable to swap back, errorcode = " + rc);
			}
		} else {
			throw new Exception("Error.. native method not loaded");
		}
	}

    //
    // Set the ASP group 

    public static native String setaspgrpNative(String aspgrp);

	public static void setaspgrp(String aspgrp) throws Exception {
		if (loaded) {
			String rc = setaspgrpNative(aspgrp);
			if (rc != null) {
				throw new Exception("Unable to setaspgrp, errorcode = " + rc + " aspgrp=" + aspgrp);
			}
		} else {
			throw new Exception("Error.. native method not loaded");
		}
	}

    //
    // Run a system command
    //

    public static native String systemNative(String command);


	public static void system(String command) throws Exception {
		try {
			if (loaded) {
				String rc = systemNative(command);
				if (rc != null) {
					throw new Exception("system command '" + command + "' errored with " + rc);
				}
			} else {
				throw new Exception("Error.. native method not loaded");
			}
		} catch (UnsatisfiedLinkError e) {
			System.out.println("Warning:  UnsatisfiedLinkError in JDJobName.system(" + command + ")");
			e.printStackTrace(System.out);

		}
	}

    //
    // Run a system command
    //

    public static native String sendProgramMessageNative(String message);

	public static void sendProgramMessage(String message) {
		if (JTOpenTestEnvironment.isOS400) {
			try {
				if (loaded) {
					String rc = sendProgramMessageNative(message);
					if (rc != null) {
						throw new Exception("sendProgramMessage('" + message + "') errored with " + rc);
					}
				} else {
					throw new Exception("Error.. native method not loaded");
				}
			} catch (Throwable ex) {
				System.out.println("Warning... error in JDJobName.sendProgramMessage");
				ex.printStackTrace();
			}
		} else {
			/* No job log to log to if not OS/400 */
		}
	}




    public static void main(String args[]) {

	try { 
	    System.out.println("Running");
	    System.out.println("Current job  is "+ getJobName());
	    System.out.println("Current job  is "+ getJobName());
	    System.out.println("Current job  is "+ getJobName());
	    System.out.println("Current job  is "+ getJobName());
	    System.out.println("Current subsystem is "+ getSubsystemName()); 
	    System.out.println("Current memory usage is "+getJobMem()+" kilobytes");
	    System.out.println("The ccsid is "+getJobCCSID());

	    System.out.println("Calling setIGC to set CCSID to 5035");
	    setIGC(5035);
	    System.out.println("CCSID set ..  ");
	    System.out.println("The ccsid is "+getJobCCSID()); 

	    
	    System.out.println("LoggingText is '"+JDJobName.getLoggingText()+"'");
	    System.out.println("Calling setJobLogOption");
	    JDJobName.setJobLogOption();
/* 
	    System.out.println("Calling JDJobName.startPex");
	    System.out.println("JDJobName.startPex returned "+JDJobName.startPex());

	    System.out.println("Calling JDJobName.endPex");
	    System.out.println("JDJobName.startPex returned "+JDJobName.endPex());
*/ 

	    try { 
		System.out.println("Calling SwapTo JJAPAN/j8p8npass"); 
		JDJobName.swapTo("JJAPAN", "j8p8npass");
		System.out.println("SwapTo complete"); 

		System.out.println("Calling SwapBack"); 
		JDJobName.swapBack();
		System.out.println("SwapBack complete"); 

	    } catch (Exception e) {
		e.printStackTrace(); 
	    }

	    System.out.println("LoggingText is '"+JDJobName.getLoggingText()+"'");
	    try { 
	     String command = "QSYS/CHGJOB LOG(4 00 *SECLVL)  ";
	     System.out.println("Running command "+command); 
	     JDJobName.system(command);   
             	    } catch (Exception e) {
		e.printStackTrace(); 
	    }
	    System.out.println("LoggingText is '"+JDJobName.getLoggingText()+"'");

	    try { 

	     String message =  " This is a test  ";     
	     System.out.println("Sending message  "+message); 
	     JDJobName.sendProgramMessage(message);
	    } catch (Exception e) {
		e.printStackTrace(); 
	    }


	    
	} catch (Exception e) {
	    e.printStackTrace(); 
	} 

    }
}
