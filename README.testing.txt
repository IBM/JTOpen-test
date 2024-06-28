This is the testbucket for JTOpen
-----------------------------------

The structure is that individual tests are named xxxTest.  Within tests, there are testcases that are run.  
These testcases are typically named xxxTestcase.

To run a test, you run the main test class and specify parameters that control how the test runs. 
For example, the following will run the AS400JPingTest test from a POSIX shell with the specified settings.

------------------------------------------------------------------------------------------------------------------

# This is the library on the IBM i that will be used for the tests
TESTLIBRARY=JDT7583A
# This is the name of the IBM i to be tested
SYSTEM=SYSTEMS
# This is the userid / password used to connect to the system
UID=JAVA
PWD=xxxxxxx
# This is the privileged userid / password user to connect to the system
PWRUID=JDPWRSYS
PWRPWD=xxxxxxx
# This is the test type.  Currently only toolbox tests are supported
TESTTYPE=toolbox
# This is the release of the IBM i in VRM format
RELEASE=v7r5m0
# This is the IASP to be tested on the IBM i 
IASP=IASP1

java -cp jt400.jar:JTOpen-test.jar  test.AS400JPingTest  -lib $TESTLIBRARY -system $SYSTEM -uid $UID -pwd $PWD -pwrSys ${PWRUID},${PWRPWD} -directory / -misc ${TESTTYPE},${RELEASE} -asp $IASP

--------------------------------------------------------------------------------------------------------------------------------------
The output of running this test will look like the following. 

NAME                                    SUCCEEDED  FAILED  NOT APPL  NOT ATT  TIME(S)
AS400JPingTestcase                         26        10        0        0      5.41 
_____________________________________________________________________________________
__________________________________________________
JAVA VERSION     = someJavaVersion
JAVA VENDOR      = someJavaVender
JAVA HOME        = someJavaHome
CLIENT NAME      = someClientName
CLIENT OS/VERSION= someClientOsVersion
CLIENT USER NAME = currentUserName
SERVER NAME/VRM  = serverTested/serverVRM
TOOLBOX VERSION  = 7.5.0.3 (JTOpen 20.0.0)
TOOLBOX JAR LOCATION = ..../jt400.jar
TOOLBOX BUILD DATE   = 2023-07-10 14:24 CDT
JDBC VERSION         = 4.0
TEST JAR LOCATION    = ../JTOpen-test.jar
TEST JAR BUILD DATE  = 2023-07-17 15:35 CDT
TC DRIVER        = test.AS400JPingTest
TC ARGS          = -lib someLibrary -system serverTested -uid someUserId -pwd xxxxxxxx -pwrSys secofrUerId,xxxxxxxx -directory / -misc toolbox,v7r5m0 -asp ASPName

Summary for the bucket
END DATE/TIME    = Mon Jul 17 11:12:51 CDT 2023
TOTAL TIME       = 00:00:05 sec
TOTAL VARIATIONS = 36
SUCCESS          = 26
FAILURE          = 10
NOT APPLICABLE   = 0
NOT ATTEMPTED    = 0
__________________________________________________






---------------------------------------------------------------
---------------------------------------------------------------
An alternate way of running the tests is to use the JDRunit program.

JDRunit setup. 
----------------------------

1.  Create a directory to be used for testing (i.e. /home/toolboxTest
2.  Set the JTOPEN_TEST_DIR environment variable to this directory.  This should be set as a system environment variable.
3.  Change your current directory into this directory
    cd $JTOPEN_TEST_DIR
4.  Copy JTOpen-test.jar into this directory. 
    (i.e. curl -L -o JTOpen-test.jar https://github.com/IBM/JTOpen-test/releases/download/v1.0.5/JTOpen-test-1.0.5.jar )
5.  Create a ini directory to hold configuration files
    mkdir ini
6.  Run the setup program to extract the sample .ini configuration files.  The files will need to be edited as described below. 
    java -cp JTOpen-test.jar test.JTOpenTestSetup 
    The setup will also make sure the latest release of JTOpen-test.jar is begin. 
    It will also check the test environment.   
7.  Edit the ini/netrc.ini file and add the appropriate credentials.
8.  Edit the ini/dropAuthority.ini file and make sure the USERID matches the TESTUSERID in the ini/netrc.ini file
9.  Edit the ini/systems.ini file and add information to the systems you are testing to. 
10. Edit the ini/notification.ini file to add e-mail addresses for e-mail notifications as well as the SMTP host. 
11. Copy the ini/runitxx8Sx.ini to a file that represents the JVM used for the test.  The 8S is a sample configuration for a Java 8 test on a PC. 
12. Download the latest JTOpen release. 
    java -cp JTOpen-test.jar test.JTOpenDownloadReleaseJars
13. For the IFS tests, get a copy of jcifs.jar and copy into the jar directory.   
14. Copy the java certificate store to /home/jdbctest/cacerts. Add the necessary certificates in order to establish 
    secure (TLS) connections to the server you are testing.   

*Note: to refresh the test jar, you can use
java -cp .:JTOpen-test.jar test.JTOpenTestSetup

* Additional windows setup
If running on windows, then CYGWIN needs to installed.
Set the CYGWIN_HOME environment variable to the directory where CYGWIN was installed. 

    
JDRunit running
-----------------------------

When running JDRunit, you specify initials that describe the testcase as well as the testcase to be run. 
The initials consist of 5 characters.  The first 2 specify the release of the IBM i (74, 75) , the next two indicate the JVM to be used, 
and the last character indicates the type of test (A=toolbox, T=toolbox JDBC).  

Here is an example of running the ping test with a runitxx8Ax.ini configuration with the details of an AIX JVM. 

java -cp JTOpen-test.jar:jt400.jar test.JDRunit 758AA AS400JPingTest

After the test completes, the results of the test are appended to a ct/runit758AA.out file 
and a file exists in ct/out/758AA that contains the output of the run. 

The JDReport program can generate a report of the test runs for the initials. 

java -cp JTOpen-test.jar:jt400.jar test.JDReport 758AA 

The report will be generated in ct/latest758AA.html

Tests supported by JDRunit are specified in the ini/testbase.ini file. 

Testcases run for regression purposes for JTOpen can be found in the following files: 
 ini/regressionBaseA.ini     -- toolbox tests
 ini/regressionBaseB.ini     -- toolbox native tests
 ini/regressionBaseT.ini     -- JDBC tests
 ini/regressionBaseU.ini     -- JDBC native tests
 
 --
 -- Scheduler 
 -- 
 
 The testbucket contains a scheduler to run the tests.  Tests are submitted to the scheduler, and the scheduler then runs the tests.
 
 -- Scheduler configuration
1.  Make sure the setup for the ini files for JDRunit was completed (see above). 
1.  Pick an ID to use for the scheduler. For example, pick 11
2.  Edit the ini/systems.ini and add a line in the following format, where localsystem is the name of the local system, 
    11 is the selected id, and ibmi is the name of the IBM i system. 

SCHEDULERID_localsystem=11
SCHEDULERDB_localsystem=ibmi
    If you don't know that name of the system, attempt to start the scheduler using the following
    java -cp JTOpen-test.jar:jar/jt400.jar test.JDSchedulerServer 11 SERVER
    You will see the error message with the name of the system
    java.lang.Exception: ID for SCHEDULERID_localsystem not found in ini/systems.ini
    
        

-- Start scheduler -- This will continue to run. 
java -cp JTOpen-test.jar test.JDSchedulerServer 11 SERVER
 
 
 -- Adding tests to the scheduler
 -- Add tests using the following command where PRIORITY is the PRIORITY in the queue, INITIALS are the initials for the test, and TEST is the testname. 
 java -cp 'JTOpen-test.jar:jars/jt400.jar' test.JDScheduler 11 ADD <PRIORITY> <INITIALS> <TEST>
 
 -- For example 
 java -cp 'JTOpen-test.jar:jars/jt400.jar' test.JDScheduler 11 ADD 20 758ST JDDriverAcceptsURL
 
 -- The priorities used when the scheduler schedulers new jobs are the following. 
PRIORITY 0 -- Run ASAP -- Reserved for runtime usage
PRIORITY 5 -- Run JOB  -- Used to preempt other jobs
PRIORITY 10 -- RERUNFAILED submitted jobs
PRIORITY 20 -- REGRESSION submitted jobs
PRIORITY 30 -- REGRESSION submission
PRIORITY 30 -- RERUNFAILED submission
PRIORITY 30 -- REPORT      submission
PRIORITY 30 -- EMAIL submission
 
-- The TEST can also be one of the following. 
REPORT -- generates a report for the initials.  Appropriate web pages are generated in the ct/ directory.
EMAIL  -- emails a report of the test using information in notifications.ini.  
          The ini/DOMAIN.ini file must be created with the following contents
          #
          # Mapping of system names to domain
          # The system name should be in lower case
          #
          localsystem=domain.for.local.system
          The mail.smtp.host property must also be added to the ini/notification.ini file 
          # Also mail.jar and activation.jar must be added to the jars/ directory. 
 
 -- Viewing currently scheduled tests
 java -cp 'JTOpen-test.jar:jars/jt400.jar' test.JDScheduler 11 LIST
 
This will display the running and scheduled tests, for example. 
----------------------------------------------------
LIST as of 2024-02-13 10:25:44.367204
----------------------------------------------------
----------------------------------------------------
RUNNING ITEMS (JDTESTINFO.SCRUN11)
----------------------------------------------------
PRI,                  ADDED_TS,  INIT, ACTION,                 STARTED_TS, RUN_SECONDS, AVG_RUN_SECONDS
----------------------------------------------------
SCHEDULED ITEMS (JDTESTINFO.SCHED11)
----------------------------------------------------
----------------------------------------------------
END
----------------------------------------------------

 
 
 

