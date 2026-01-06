package test;

public class ObjectListTest extends TestDriver{

	 /**
    Run the test as an application.
    @param  args  The command line arguments.
    **/
   public static void main(String args[])
   {
       try
       {
           TestDriver.runApplication(new ObjectListTest(args));
       }
       catch (Exception e)
       {
           System.out.println("Program terminated abnormally.");
           e.printStackTrace();
       }

           System.exit(0);
   }

   /**
    Constructs an object for applets.
    @exception  Exception  If an exception occurs.
    **/
   public ObjectListTest() throws Exception
   {
       super();
   }

   /**
    Constructs an object for testing applications.
    @param  args  The command line arguments.
    @exception  Exception  If an exception occurs.
    **/
   public ObjectListTest(String[] args) throws Exception
   {
       super(args);
   }

   /**
    Creates the testcases.
    **/
   public void createTestcases()
   {
       Testcase[] testcases =
       {
           new ObjectListTestcase()
       };

       for (int i = 0; i < testcases.length; ++i)
       {
           testcases[i].setTestcaseParameters(systemObject_, pwrSys_, systemName_, userId_, password_, proxy_, mustUseSockets_, isNative_, isLocal_, onAS400_, namesAndVars_, runMode_, fileOutputStream_, pwrSysUserID_, pwrSysPassword_);
           testcases[i].setProxy5(proxy5_); 
           addTestcase(testcases[i]);
       }
   }

}
