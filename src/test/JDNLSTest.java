package test;

import java.sql.Connection;

public class JDNLSTest extends JDTestDriver {
   
    public static String COLLECTION = "JDTESTNLS1";
    /**
    Run the test as an application.  This should be called
    from the test driver's main().

    @param  args        The command line arguments.

    @exception Exception If an exception occurs.
     **/
    public static void main(String args[]) throws Exception {
        runApplication(new JDNLSTest(args));
    }

    /**
     Constructs an object for applets.

     @exception Exception If an exception occurs.
     **/
    public JDNLSTest() throws Exception {
        super();
    }

    /**
     Constructs an object for testing applications.

     @param      args        The command line arguments.

     @exception Exception If an exception occurs.
     **/
    public JDNLSTest(String[] args) throws Exception {
        super(args);
    }

    /**
     Performs setup needed before running testcases.

     @exception Exception If an exception occurs.
     **/
    public void setup() throws Exception {
        // going to do setup in first testcase
        super.setup();

	if(testLib_ != null) {   // @E1A
	    COLLECTION = testLib_;
	}


        Connection connection = getConnection (getBaseURL (),
                systemObject_.getUserId (), encryptedPassword_);
        Connection connPwr = getConnection (getBaseURL (),
                pwrSysUserID_, pwrSysEncryptedPassword_);
        
        //dropCollection(connPwr, COLLECTION);
        
        JDSetupCollection.create (systemObject_, 
                connPwr, COLLECTION);
        
        connection.close();
    }

    /**
     Cleanup - - this does not run automatically - - it is called by JDCleanup.
     **/
    public static void dropCollections(Connection c) {
    }

    /**
     Performs setup needed after running testcases.

     @exception Exception If an exception occurs.
     **/
    public void cleanup() throws Exception {
    }

    
    public void createTestcases2() {
        addTestcase (new JDBIDITestcase (systemObject_,
                namesAndVars_, runMode_, fileOutputStream_, 
                password_, pwrSysUserID_, pwrSysPassword_));
         
    }

}
