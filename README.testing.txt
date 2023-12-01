This is the testbucket for JTOpen
-----------------------------------

The structure is that individual tests are named xxxTest.  Within tests, there are testcases that are run.  These testcases are typically named xxxTestcase.

To run a test, you run the main test class and specify parameters that control how the test runs. 
For example, the following will run the AS400JPingTest toolbox from a POSIX shell with the specified settings.

------------------------------------------------------------------------------------------------------------------

# This is the library on the IBM i that will be used for the tests
TESTLIBRARY=JDT7583A
# This is the name of the IBM i to be tested
SYSTEM=SQ750
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

1.  Create a directory to be used for testing (i.e. /home/toolboxTest
2.  Copy JTOpen-test.jar into this directory. 
    (i.e. curl -L -o JTOpen-test.jar https://github.com/IBM/JTOpen-test/releases/download/v1.0.3/JTOpen-test-1.0.3.jar )
3.  Copy a version of jt400.jar into this directory. 
    (i.e. curl -L -o jt400.jar https://github.com/IBM/JTOpen/releases/download/v20.0.6/jt400-20.0.6-java8.jar )
4.  Change your current directory into this directory
    cd /home/toolboxTest
5.  Create a ini directory to hold configuration files
    mkdir ini
6.  Extract the sample config files into this jar file
    jar xvf JTOpen-test.jar ini/netrc.ini ini/systems.ini ini/notification.ini ini/dropAuthority.ini ini/runitxx8Sx.ini
7.  Edit the ini/netrc.ini file and add the appropriate credentials.
8.  Edit the ini/dropAuthority.ini file and make sure the USERID matches the TESTUSERID in the ini/netrc.ini file
9.  Edit the ini/system.ini file and add information to the systems you are testing to. 
10. Edit the ini/notification.ini file to add e-mail addresses for e-mail notifications as well as the SMTP host. 
11. Copy the ini/runitxx8Sx.ini to a file that represents the JVM used for the test.  The 8S is a sample configuration for a Java 8 test on a PC. 
12. Make a jars directory to contain accessor jar files
    mkdir jar
13. For the IFS tests, get a copy jcifs.jar and copy into the jar directory.     

    
JDRunit running
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

Testcases run for regression purposes for toobox can be found in the ini/regressionBaseA.ini file

