///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TestDriverI.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Vector;

/**
 Represents a test program or applet for a single component.  Each component will create a subclass of this class.  Test drivers are called in the following manner:
 <p>Use the following parameter tags and values:
 <pre>
 * -system    - The iSeries server with which to connect.
 * -file      - The file in which to write results.  Results are always written to standard out, if this tag is specified, results are also written to this file.
 * -run       - The run mode.  Values are 'u' for unattended variations only, 'a' for attended variations only, or 'b' for both.  The default value is 'u'.
 * -uid       - The user profile name to use to authenticate to the server.
 * -pwd       - The user profile password to use to authenticate to the server.
 * -proxy     - The proxy server system name with which to connect.
 * -tc        - Testcase to be run.  If not specififed, all testcases will be run.  This token can be specified several times.
 * -vars      - Variations to be run.  Several values can be specified by separating each value with a comma.  This token must be specified immediately after a -tc token, it applies to the testcase which it follows.  If not specified, all variations will be run.
 * -misc      - Miscellaneous information that is passed to all testcases.
 * -trace     - Categories to trace.  Multiple categories can be specified by separating each value with a comma.  The categories are datastream, diagnostic, error, information, jdbc, warning, conversion, pcml.  Category names are not case sensitive.
 * -lib       - The name of the test library to create files, etc. in.  This option is used (initially) by Record-level Access.  This field can now be specified by the user as the place to put the tables/procs etc.
 * -ssl       - Connect using secure sockets connections.
 * -socks     - Set the system object's mustUseSockets property to true.
 * -pwrSys    - A userID, password combination with *SECOFR authority.
 * -nothreads - Set the system object's isThreadUsed property to false.
 * -printer   - The name of the printer to use for the NPPrint testcases.
 * -profileToken - Generate and use a profile token for authentication.
 * -serialize - Indicates that the AS400 object is to be deserialized/serialized.  A file named "as400.ser" is sought and, if found, deserialized into systemObject_.  Upon testcase completion, the file is (re)generated.
 * -kerberos  - Use Kerberos for authentication.
 * -native    - The native optimizations classes are available.
 * -local     - The target system is the same system we are running on.
 * -onAS400   - The client is an iSeries server.
 * -brief     - Skip long-running variations.
 * -duration  - Number of seconds to run testcase.
 * -pause     - Waits for the user to press enter before running the variations to obtain the jobs running on the server, depending on the class type.
 * -directory - The directory where some test driver variations point to.
 * -jndi      - The type of jndi used for some jdbc tests.
 * -cleanup   - NLSTests can have cleanup, this flags indicates if it is needed.
 * -asp       - Indicates the ASP to use for the test
 * -extendedDynamic - Indicates that extended dynamic should be used for the toolbox driver
 *Examples:
 *   java ExampleDriver
 *   java ExampleDriver -system rchas57a
 *   java ExampleDriver -system rchas57a -file example.out -run B -uid tester -pwd secret -tc testcase1
 *   java ExampleDriver -system rchas57a -file example.out -run B -tc testcase1 -vars 1,2,3  -tc testcase2 -vars 3,9 -tc testcase3
 *   java ExampleDriver -system rchas57a -tc testcase1 -vars 3,12:25     // runs variations 3 and 12 thru 25.
 </pre>
 If system or uid or pwd are not specified, you will be prompted for a system name or a userid or password, even if running only unattended variations.
 **/
public interface TestDriverI  
{


    // States used when parsing input parameters.
    static final int FINISH = 0;
    static final int START = 1;
     static final int PARSE_SYSTEM = 2;
     static final int PARSE_RDB = 3;
     static final int PARSE_FILE = 4;
     static final int PARSE_RUN_MODE = 5;
     static final int PARSE_TESTCASE = 6;
     static final int PARSE_USERID = 7;
     static final int PARSE_PASSWORD = 8;
     static final int CHECK_FOR_VARIATIONS = 9;
     static final int PARSE_VARIATIONS = 10;
     static final int PARSE_MISCELLANEOUS = 11;
     static final int PARSE_TRACE_CATEGORY = 12;
     static final int PARSE_LIB_CATEGORY = 13;
     static final int PARSE_PWR_SYS = 14;
     static final int PARSE_CONN_TYPE = 15;
     static final int PARSE_MAPPED_DRIVE = 16;
     static final int PARSE_PRINTER = 17;
     static final int PARSE_PROXY = 18;
     static final int PARSE_DURATION = 19;
            static final int PARSE_PAUSE = 20;
     static final int PARSE_DIRECTORY = 21;
     static final int PARSE_JNDI = 22;
     static final int PARSE_ASP = 23;
     static final int PARSE_PROXY5 = 24;



   /**
     Adds a testcase.
     @param  testcase  The testcase object.
     **/
     void addTestcase(Testcase testcase);

    /**
     Adds a skip testcase.
     @param string testcase name
    */

     void addSkipTestcase(String testcaseName );


    /**
     Performs cleanup needed after running testcases.
     @exception  Exception  If an exception occurs.
     **/
     void cleanup() throws Exception;

    /**
     Fills in the testcases_ array.
     **/
      void createTestcases();

    /**
     Applet destroy.
     **/
    public void destroy();


    /**
     Return the system name before it changes into "localhost".
     **/
    String getSystemName();

    /**
     Return the rdbname -- null means RDB was not set
     **/
    String getRdbName() ;

    /**
     Performs any initialization work.
     **/
    public void init();

    /**
     Parse parameters.
     @exception  Exception  Incorrect arguments will cause an exception.
     **/
    public void parseParms(String args[]) throws Exception;

    /**
     Run the tests.
     **/
    public void run();



    /**
     Performs setup needed before running testcases.
     @exception  Exception  If an exception occurs.
     **/
     void setup() throws Exception;

    /**
     Runs all (all that were specified) testcases in this test program.
     **/
    public void start();

    /**
     Display usage information for the Testcase.  This method never returns.
     **/
    public void usage();

    /**
     Output results summary for the test bucket.
     **/
    public void outputSummary()  ;

    /**
     Returns the results for the bucket in an integer array.
     Used by JDBCNativeTest for counting the results from the entire CRT.
     **/
    public int[] getResults();

    /**
     Utility method to run commands under -pwrSys authority.
     @param  commmand  Command to run.
     @return true if command was successful; false otherwise.
     **/
    public boolean cmdRun(String command);
    public boolean cmdRun(String command, String expectedMessage);
    boolean isExtendedDynamic() ;
    public Vector<String[]> getTestcaseResults() ;

    /** @C1A
    Returns the AS/400 library which is set as the default for the testcase run.

    @return The AS/400 library.
    **/
    public String getTestLib();

    /* Stop method.. For applet cases causes applet superclass */ 
    public void stop();

}

