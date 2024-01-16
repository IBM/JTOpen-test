# JTOpen-test
Test suite for JTOpen project

This is the testbucket for JTOpen
-----------------------------------

The structure is that individual tests are named xxxTest.  Within tests, there are testcases that are run.  These testcases are named xxxTestcase.

To run a test, you run the main test class and specify parameters that control how the test runs. 
For example, the following will run the AS400JPingTest.
```
java AS400JPingTest  -lib <libraryForTest> -system <systemName> -uid <userId> -pwd <password> -pwrSys <privilegedUserid,privilegedPassword> -directory / -misc <testtype>,<release> -asp <IASPname>
```
Here is an example of running the JDAcceptsURL testcase (replace the $... variables with the correct values). 
```
java -cp JTOpen-test.jar:jt400.jar test.JDDriverTest -tc JDDriverAcceptsURL -lib JDTEST -system $SYS -uid $UID -pwd $PWD -pwrSys $PWRUID,$PWRSYS -directory / -misc toolbox,v7r5m0 -asp IASP33 
```
The result of running the test will contain the following. 
```
_____________________________________________________________________________________
NAME                                    SUCCEEDED  FAILED  NOT APPL  NOT ATT  TIME(S)
JDDriverAcceptsURL                         29         0        2        0      0.037 
_____________________________________________________________________________________
```

