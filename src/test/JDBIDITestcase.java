package test;

import test.TestBidiMetaData;

import java.awt.TextArea;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet; 

import java.util.Hashtable;
 
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.User;
import com.ibm.as400.access.AS400;

/**
 * 
 * @author ibm
 * Testcase to test Gregory Brodsky's bidi code in toolbox
 */
public class JDBIDITestcase extends JDTestcase
{

    // Constants.
  
 
    // Private data.
    // private Connection connection_;
 
    TestBidiConnection t1;
    TestBidiMetaData t2;
    
    TestBidiConnection[] tBidiConn_ = new TestBidiConnection[4];
    TestBidiMetaData[] tBidiMetaData_ = new TestBidiMetaData[4];
    
    boolean isBidiUser_ = true;

    String[] Hebrew_CCSIDs = {"424", "62211", "62235", "62245"};

    public JDBIDITestcase(AS400 systemObject,
            Hashtable namesAndVars,
            int runMode,
            FileOutputStream fileOutputStream,
       
            String password,
  	    String powerUserID,
	    String powerPassword)
    {
       
        super (systemObject, "JDBIDITestcase",
                namesAndVars, runMode, fileOutputStream, 
                password, powerUserID, powerPassword);



    }
    

       public void createProfile(String ccsid, Connection con){
        	String user = "JD" + ccsid;
        	String pwd = "PASS" + ccsid + "X";
        	String cmdCrtUsr = "CRTUSRPRF USRPRF("+user+") PASSWORD("+pwd+") USRCLS(*SECADM) SPCAUT(*ALLOBJ)  TEXT(''Toolbox testing profile'') ACGCDE(514648897) CCSID("+ccsid+")                                                                       ";
        	String sqlCrtUsr = "CALL QSYS.QCMDEXC('" + cmdCrtUsr + "',   0000000160.00000)"; 
        	String cmdChgPwd = "CHGUSRPRF USRPRF("+user+") PASSWORD(BOGUS7)  PWDEXP(*NO)                                                                        ";
        	String sqlChgPwd = "CALL QSYS.QCMDEXC('" + cmdChgPwd + "',   0000000072.00000)"; 
        	String cmdEnableUsr = "CHGUSRPRF USRPRF("+user+") PASSWORD("+pwd+")  STATUS(*ENABLED) PWDEXP(*NO)                                                                                                     ";
        	String sqlEnableUsr = "CALL QSYS.QCMDEXC('" + cmdEnableUsr + "',   0000000092.00000)";
        	try {
				Statement stmt = con.createStatement();
				System.out.println("SQL to create a user profile: " + sqlCrtUsr);
				try { 
				    stmt.executeUpdate(sqlCrtUsr);
				} catch (Exception e) {
				    System.out.println("Create profile failed with "+e.toString()); 
				} 
				System.out.println("SQL to change the user password: " + sqlChgPwd);
				stmt.executeUpdate(sqlChgPwd);
				System.out.println("SQL to enable the user and change password back: " + sqlEnableUsr);
				stmt.executeUpdate(sqlEnableUsr);
			} catch (SQLException e) {
				// ignore an error 
			}
        }


       public void deleteProfile(String ccsid, Connection con){
        	String user = "JD" + ccsid;
        	String cmdDltUsr = "DLTUSRPRF USRPRF("+user+") OWNOBJOPT(*DLT)                                                   "; 
        	String sqlDltUsr = "CALL QSYS.QCMDEXC('" + cmdDltUsr + "',   0000000085.00000)"; 
        	try {
				Statement stmt = con.createStatement();
				System.out.println("SQL to delete a user profile: " + sqlDltUsr);
				stmt.executeUpdate(sqlDltUsr);
			} catch (SQLException e) {
				// ignore an error 
			}
        }


        public TestBidiConnection constructBidiConn(String ccsid){
        	TestBidiConnection t = new TestBidiConnection();
        	t.schema_ = JDNLSTest.COLLECTION;
        	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
        	   t.driverUrlBase = "jdbc:db2:";
        	}
        	t.host = system_;
        	t.username = "JD" + ccsid;
        	t.password = "PASS" + ccsid + "X";
        	t.setTable("BIDIC"+ccsid); 
        	return t;
        }
        public TestBidiMetaData constructMetaData(String ccsid){
        	TestBidiMetaData t = new TestBidiMetaData();
        	t.schema_ = JDNLSTest.COLLECTION;
        	if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
            t.driverUrlBase = "jdbc:db2:";
          }
        	
        	t.host = system_;
        	t.username = "JD" + ccsid;
        	t.password = "PASS" + ccsid + "X";
                t.setTable( "BIDIM"+ccsid); 
        	
        	return t;
        }



    /**
    Performs setup needed before running variations.

    @exception Exception If an exception occurs.
    **/
	protected void setup ()
	  throws Exception {
	    Connection con = testDriver_.getConnection (baseURL_,  pwrSysUserID_, pwrSysEncryptedPassword_, "JDBIDITestcase");

	    // CreateProfile should tolerate the case where the profile already exists.
            // At the end of the test, we should delete the profiles.
	    // We always want to go through the createProfile to ensure
            // that the profiles are there. 

	    for(int i = 0; i < Hebrew_CCSIDs.length; i++) {
		createProfile(Hebrew_CCSIDs[i], con);
	    }
	    
        	for(int i = 0; i < Hebrew_CCSIDs.length; i++) {
        		tBidiConn_[i] = constructBidiConn(Hebrew_CCSIDs[i]);
        	}

        	for(int i = 0; i < Hebrew_CCSIDs.length; i++) {
        		tBidiMetaData_[i] = constructMetaData(Hebrew_CCSIDs[i]);
	    } 

	    con.close(); 
        	
	    }

	private boolean isBidiUser(String ccsid) {
	    boolean isBidiUser = true;
	    String user = "JD" + ccsid;
	    String pwd = "PASS" + ccsid + "X";
	    String url = baseURL_ + ";user="+user+";password="+pwd;
	    AS400JDBCConnection conn = null;
	    try {
		Connection connection =  testDriver_.getConnection (url, "JDBIDITestcase.isBidiUser");
		if (connection instanceof AS400JDBCConnection) { 
		    conn = (AS400JDBCConnection) connection; 
		    User u = new User(conn.getSystem(), user);
		    if (u.getCCSID()!= Integer.parseInt(ccsid)) {
			System.out.println("user is "+user+", ccsid is "+ u.getCCSID()); 
			isBidiUser = false;
		    }
		} else {
		    Statement stmt = connection.createStatement();
		    stmt.execute("CALL QSYS2.QCMDEXC('DSPUSRPRF USRPRF("+user+") OUTPUT(*OUTFILE) OUTFILE(QTEMP/DSPUSRPRF)')");
		    ResultSet rs = stmt.executeQuery("SELECT UPCCSI FROM qtemp.dspusrprf  ");
		    rs.next();
		    String retrievedCcsid = rs.getString(1);
		    if (!retrievedCcsid.equals(ccsid)) {
			System.out.println("user is "+user+", retrievedCcsid is "+ retrievedCcsid); 
			isBidiUser = false;

		    } 
		} 
	    } catch (Exception e) {
		e.printStackTrace();
		isBidiUser = false;
	    } finally {
	    	try {
		    if (conn != null) conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
	    }

	    return isBidiUser;
	}


    /**
    Performs cleanup needed after running variations.

    @exception Exception If an exception occurs.
    **/
        protected void cleanup ()
            throws Exception
        {

	    // Cleanup the profiles that were created.

	    Connection con = testDriver_.getConnection (baseURL_,  pwrSysUserID_, pwrSysEncryptedPassword_,"JDBIDITestcase.cleanup");

	    for(int i = 0; i < Hebrew_CCSIDs.length; i++) {
		deleteProfile(Hebrew_CCSIDs[i], con);
	    }
	    con.close(); 

      //  	if(connection_!=null && !connection_.isClosed())
      //      connection_.close ();
        }


    /**getMetaData() - Should throw an exception on a closed prepared statement.**/

    private void testcaseBidiConnection(String varID, TestBidiConnection t, String userCCSID) {
        System.gc();
        
    	System.out.println("TestBidiConnection host ccsid=" + t.host + ", package ccsid=" + t.package_ccsid + ", user ccsid=" + userCCSID);

    	if (!isBidiUser(userCCSID)) {
			notApplicable("user ccsid " +  userCCSID + " is unsupported in this testcase");
                return;
            }
                try {
		    long start = System.currentTimeMillis();
		    boolean result = true;
		    TestBidiConnection.errorLog_.setLength(0); 
		    t.TestString = t.use_complex_strings? t.TestString1 : TestBidiConnection.TestString2;
		    t.RecreateTable();
		    t.setHostCCSID(Integer.parseInt(userCCSID));
		
		if (!t.Testcase_Insert()) {
		    result = false; 
		}

		if ( ! t.Testcase_CallableInsert()) {
		    result=false; 
		}					

		if (!t.Testcase_PreparedInsert()){
		    result = false;
		}

		if (!t.Testcase_Select()){
		    result = false;
		}

		if (!t.Testcase_ResultSetUpdate()) {
		    result = false; 
		} 
			long end = System.currentTimeMillis();
			System.out.println("var" + varID + " SQL execute time : " + (end - start) / 1000 + " s");
	                assertCondition(result, TestBidiConnection.errorLog_.toString());
                    
		} catch (Exception e) {
                     failed(e, TestBidiConnection.errorLog_.toString());
                }
            
        }
    
    private void testcaseBidiMetaData(String varID, TestBidiMetaData t, String userCCSID) {
	
        System.gc(); 
    	System.out.println("TestBidiMetaData host ccsid=" + t.host + ", package ccsid=" + t.package_ccsid + ", user ccsid=" + userCCSID);

    	if (!isBidiUser(userCCSID)) {
			notApplicable("user ccsid " +  userCCSID + " is unsupported in this testcase");
                return;
            }
                try {
		    t.setHostCCSID(Integer.parseInt(userCCSID));
                  TestBidiMetaData.errorLog_.setLength(0); 
		  t.TestString = t.use_complex_strings? t.TestString1 : TestBidiConnection.TestString2;
                  long start = System.currentTimeMillis();
			boolean result = true;
			// Note:  We cannot test the DMD unless we are on a BIDI system
			boolean testDmd = false; 
			if (!t.CreateMultiColumnTable(testDmd)){
			    result = false;
			    System.out.println("create multi column table NOT successful");
			} else { 
			    System.out.println("create multi column table successful");
			}
			if (!t.UseBidiTableName()) {
			    result = false;
			    System.out.println("use bidi table name NOT successful");
			} else { 
			    System.out.println("use bidi table name successful");
			}
			if (!t.testColumnName()) {
			    result = false;
			    System.out.println("test column name NOT successful");
			} else { 
			    System.out.println("test column name successful");
			}
			long end = System.currentTimeMillis();
			System.out.println("var" + varID + " SQL execute time : " + (end - start) / 1000 + " s");
	                assertCondition(result, TestBidiConnection.errorLog_.toString());

                
                
                } catch (Exception e) {
                     failed(e, TestBidiConnection.errorLog_.toString());
                }
            
        }


    public void Var001() {
		TestBidiConnection t424 = tBidiConn_[0];
		t424.package_ccsid = "system";
		t424.use_complex_strings = false;
		testcaseBidiConnection("001", t424, "424");
            }

    public void Var002() {
		TestBidiConnection t62211 = tBidiConn_[1];
		t62211.package_ccsid = "system";
		t62211.use_complex_strings = false;
		testcaseBidiConnection("002", t62211, "62211");
                }
            
    public void Var003() {
		TestBidiConnection t62235 = tBidiConn_[2];
		t62235.package_ccsid = "system";
		t62235.use_complex_strings = false;
		testcaseBidiConnection("003", t62235, "62235");
        }

    public void Var004() {
    	TestBidiConnection t62245 = tBidiConn_[3];
		t62245.package_ccsid = "system";
		t62245.use_complex_strings = false;
		testcaseBidiConnection("004", t62245, "62245");
            }
            

    public void Var005() {
		TestBidiConnection t424 = tBidiConn_[0];
		t424.package_ccsid = "424";
		t424.use_complex_strings = false;
		testcaseBidiConnection("005", t424, "424");
                }
    
    public void Var006() {
		TestBidiConnection t62211 = tBidiConn_[1];
		t62211.package_ccsid = "424";
		t62211.use_complex_strings = false;
		testcaseBidiConnection("006", t62211, "62211");
                }
            
    public void Var007() {
		TestBidiConnection t62235 = tBidiConn_[2];
		t62235.package_ccsid = "424";
		t62235.use_complex_strings = false;
		testcaseBidiConnection("007", t62235, "62235");
        }

    public void Var008() {
    	TestBidiConnection t62245 = tBidiConn_[3];
		t62245.package_ccsid = "424";
		t62245.use_complex_strings = false;
		testcaseBidiConnection("008", t62245, "62245");
            }
            

                    
    public void Var009() {
		TestBidiConnection t424 = tBidiConn_[0];
		t424.package_ccsid = "62211";
		t424.use_complex_strings = false;
		testcaseBidiConnection("009", t424, "424");
                }
    
    public void Var010() {
		TestBidiConnection t62211 = tBidiConn_[1];
		t62211.package_ccsid = "62211";
		t62211.use_complex_strings = false;
		testcaseBidiConnection("010", t62211, "62211");
                }
            
    public void Var011() {
    	TestBidiConnection t62235 = tBidiConn_[2];
		t62235.package_ccsid = "62211";
		t62235.use_complex_strings = false;
		testcaseBidiConnection("011", t62235, "62235");
        }
    
    public void Var012() {
		TestBidiConnection t62245 = tBidiConn_[3];
		t62245.package_ccsid = "62211";
		t62245.use_complex_strings = false;
		testcaseBidiConnection("012", t62245, "62245");
	    }

    public void Var013() {
		TestBidiConnection t424 = tBidiConn_[0];
		t424.package_ccsid = "13488";
		t424.use_complex_strings = false;
		testcaseBidiConnection("013", t424, "424");
	}
            
    public void Var014() {
		TestBidiConnection t62211 = tBidiConn_[1];
		t62211.package_ccsid = "13488";
		t62211.use_complex_strings = false;
		testcaseBidiConnection("013", t62211, "62211");
            }

    public void Var015() {
    	TestBidiConnection t62235 = tBidiConn_[2];
		t62235.package_ccsid = "13488";
		t62235.use_complex_strings = false;
		testcaseBidiConnection("015", t62235, "62235");
	}
                          
    public void Var016() {
    	TestBidiConnection t62245 = tBidiConn_[3];
		t62245.package_ccsid = "13488";
		t62245.use_complex_strings = false;
		testcaseBidiConnection("016", t62245, "62245");
	}
                
    public void Var017() {
		TestBidiMetaData t424 = tBidiMetaData_[0];
		t424.package_ccsid = "system";
		t424.use_complex_strings = false;
		testcaseBidiMetaData("017", t424, "424");
                }
    
    public void Var018() {
    	TestBidiMetaData t62211 = tBidiMetaData_[1];
		t62211.package_ccsid = "system";
		t62211.use_complex_strings = false;
		testcaseBidiMetaData("018", t62211, "62211");
                }
            
    public void Var019() {
    	TestBidiMetaData t62235 = tBidiMetaData_[2];
		t62235.package_ccsid = "system";
		t62235.use_complex_strings = false;
		testcaseBidiMetaData("019", t62235, "62235");
        }
    
    public void Var020() {
    	TestBidiMetaData t62245 = tBidiMetaData_[3];
		t62245.package_ccsid = "system";
		t62245.use_complex_strings = false;
		testcaseBidiMetaData("020", t62245, "62245");
	    }

    public void Var021() {
    	TestBidiMetaData t424 = tBidiMetaData_[0];
		t424.package_ccsid = "424";
		t424.use_complex_strings = false;
		testcaseBidiMetaData("021", t424, "424");
	}
            
    public void Var022() {
	
    	TestBidiMetaData t62211 = tBidiMetaData_[1];
		t62211.package_ccsid = "424";
		t62211.use_complex_strings = false;
		testcaseBidiMetaData("022", t62211, "62211");
            }

    public void Var023() {
    	TestBidiMetaData t62235 = tBidiMetaData_[2];
		t62235.package_ccsid = "424";
		t62235.use_complex_strings = false;
		testcaseBidiMetaData("023", t62235, "62235");
                }
            
    public void Var024() {
    	TestBidiMetaData t62245 = tBidiMetaData_[3];
		t62245.package_ccsid = "62211";
		t62245.use_complex_strings = false;
		testcaseBidiMetaData("024", t62245, "62245");
        }

    public void Var025() {
    	TestBidiMetaData t424 = tBidiMetaData_[0];
		t424.package_ccsid = "62211";
		t424.use_complex_strings = false;
		testcaseBidiMetaData("025", t424, "424");
                }
            
    public void Var026() {
    	TestBidiMetaData t62211 = tBidiMetaData_[1];
		t62211.package_ccsid = "62211";
		t62211.use_complex_strings = false;
		testcaseBidiMetaData("026", t62211, "62211");
        }

    public void Var027() {
    	TestBidiMetaData t62235 = tBidiMetaData_[2];
		t62235.package_ccsid = "62211";
		t62235.use_complex_strings = false;
		testcaseBidiMetaData("023", t62235, "62235");
            }

    public void Var028() {
    	TestBidiMetaData t62245 = tBidiMetaData_[3];
		t62245.package_ccsid = "62211";
		t62245.use_complex_strings = false;
		testcaseBidiMetaData("029", t62245, "62245");
                }
            
    public void Var029() {
    	TestBidiMetaData t424 = tBidiMetaData_[0];
		t424.package_ccsid = "13488";
		t424.use_complex_strings = false;
		testcaseBidiMetaData("029", t424, "424");
        }

    public void Var030() {
    	TestBidiMetaData t62211 = tBidiMetaData_[1];
		t62211.package_ccsid = "13488";
		t62211.use_complex_strings = false;
		testcaseBidiMetaData("030", t62211, "62211");
	    }

    public void Var031() {
    	TestBidiMetaData t62235 = tBidiMetaData_[2];
		t62235.package_ccsid = "13488";
		t62235.use_complex_strings = false;
		testcaseBidiMetaData("031", t62235, "62235");
                }
            
    public void Var032() {
    	TestBidiMetaData t62245 = tBidiMetaData_[3];
		t62245.package_ccsid = "13488";
		t62245.use_complex_strings = false;
		testcaseBidiMetaData("032", t62245, "62245");
        }
        
    public void Var033() {
    	TestBidiConnection t424 = tBidiConn_[0];
		t424.package_ccsid = "system";
		t424.use_complex_strings = true;
		testcaseBidiConnection("033", t424, "424");
	    }

    public void Var034() {
    	boolean result = TestBidiTransform.test(420, TestBidiTransform.Arabic_String, TestBidiTransform.Arabic_CCSID);	
    	assertCondition(result, TestBidiConnection.errorLog_.toString());
                }
            
    public void Var035() {
    	boolean result = TestBidiTransform.test(424, TestBidiTransform.Hebrew_String, TestBidiTransform.Hebrew_CCSID);
    	assertCondition(result, TestBidiConnection.errorLog_.toString());
        }
         
    
}
