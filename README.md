# JTOpen-test
Test suite for JTOpen project

This is the testbucket for JTOpen
-----------------------------------

The structure is that individual tests are named xxxTest.  Within tests, there are testcases that are run.  These testcases are named xxxTestcase.

To run a test, you run the main test class and specify parameters that control how the test runs. 
For example, the following will run the AS400JPingTest.

java AS400JPingTest  -lib <libraryForText> -system <systemName> -uid <userId> -pwd <password> -pwrSys <privilegedUserid,privilegedPassword> -directory / -misc <testtype>,<release> -asp <IASPname>

