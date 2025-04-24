///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDASEnableCALTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////
//
//  File Name: AS400JDBCEnableCALTestcase.java
//  Classes:   AS400JDBCEnableCALTestcase
//
////////////////////////////////////////////////////////////////////////////
// 
////////////////////////////////////////////////////////////////////////////
package test.JD.AS;


import java.io.FileOutputStream;
import java.util.Hashtable; import java.util.Vector;
import java.util.Random;
import java.sql.*;

import com.ibm.as400.access.AS400;

import test.JDReflectionUtil;

/**
  Testcase AS400JDBCEnableCALTestcase.
 **/
public class JDASEnableCALTestcase extends JDASTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDASEnableCALTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDASTest.main(newArgs); 
   }
    public int RUN_SECONDS = 30; 
		/**
      Constructor.  This is called from the AS400JDBCEnableCALTest constructor.
     **/
    public JDASEnableCALTestcase(AS400 systemObject,
        Hashtable<String,Vector<String>> namesAndVars,
        int runMode,
        FileOutputStream fileOutputStream,
        
        String password, String pwrSysUserID, String pwrSysPassword)
 {
		super(systemObject, "JDASEnableCALTestcase", namesAndVars, runMode,
		    fileOutputStream, password, pwrSysUserID, pwrSysPassword);
		// originalPrintWriter_ = Trace.getPrintWriter();
	}

	public void setup() {
		String sql=""; 
		try {
			fixupSql(setupSql); 
			fixupSql(statementSql); 
			fixupSql(callableStatementSql); 
			fixupSql(cleanupSql); 
			
			if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
				url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
				    + systemObject_.getUserId() ;

				Connection connection = testDriver_.getConnection(url_,
				    systemObject_.getUserId(), encryptedPassword_);

				Statement s = connection.createStatement();
				for (int i = 0; i < setupSql.length; i++) {
					sql = setupSql[i]; 
    				s.execute(sql);
				}
				connection.close();

			}

		} catch (Exception e) {
			System.out.println("Setup error.");
			System.out.println("Last sql statement was the following"); 
			System.out.println(sql); 
			e.printStackTrace(System.out);
		}
	}


		/**
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {  
        try
        {
    			if (isToolboxFixDate(TOOLBOX_FIX_DATE)) {
    				url_ = "jdbc:as400://" + systemObject_.getSystemName() + ";user="
    				    + systemObject_.getUserId() ;

    				Connection connection = testDriver_.getConnection(url_,
    				    systemObject_.getUserId(), encryptedPassword_);

    				Statement s = connection.createStatement();
    				for (int i = 0; i < cleanupSql.length; i++) {
    					try { 
        				s.execute(cleanupSql[i]);
    					} catch (Exception e) { 
    						e.printStackTrace(System.out); 
    					}
    				}
    				connection.close();

    			}
        }
        catch (Throwable e) { e.printStackTrace(); }
    }

	/**
	 * Test a simple connect with enableClientAffinitiesList.
	 * Test statement objects. 
	 * Make sure the expected error is returned.
	 **/
	public void Var001() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test a simple connect with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				String sql;
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				Statement s = connection.createStatement();
				for (int i = 0; i < 40; i++) {
					sql = "Values 1";
					sb.append("General   connection is "
					    + getServerJobName(connection)
					    + "\n");
					Connection sc = s.getConnection();
					sb.append("Statement connection is "
					    + getServerJobName(sc) + "\n");
					for (int j = 0; j < 2; j++) {
						sb.append("Executing -- " + sql + "\n");
						ResultSet rs = s.executeQuery(sql);
						while (rs.next()) {
							rs.getString(1);
						}
						rs.close();
					}
					sql = "call qsys2.qcmdexc('endjob job(*) option(*immed)')";
					try {
						sb.append("Executing -- " + sql + "\n");
						s.executeUpdate(sql);
						throw new SQLException("Error -- exception not thrown");
					} catch (SQLException e) {
						int sqlcode = e.getErrorCode();
						if (sqlcode != -4498) {
							sb.append("Bad exception received\n");
							printStackTraceToStringBuffer(e, sb);
							throw e;
						} else {
							sb.append("Good exception received\n");
							printStackTraceToStringBuffer(e, sb);
						}
					}

				}

				connection.close(); 
				assertCondition(passed, sb);

			} else {
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	/**
	 * Test a simple connect with enableClientAffinitiesList.
	 * Use multiple statements and make sure the expected error is returned.
	 **/
	public void Var002() {
		int STATEMENT_COUNT = 10; 
		int LOOP_COUNT = 40; 
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		Random random = new Random(); 
		
		sb.append("Test a simple connect with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				String sql;
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				Statement[] s = new Statement[STATEMENT_COUNT];
				for (int i = 0; i < STATEMENT_COUNT; i++) { 
					s[i] = connection.createStatement();
				}
				for (int i = 0; i < LOOP_COUNT; i++) {
					sql = "Values 1";
					sb.append("General   connection is "
					    + getServerJobName(connection)
					    + "\n");
					for (int j = 0; j < STATEMENT_COUNT; j++) {
					Connection sc = s[j].getConnection();
					sb.append("Statement ["+j+"] connection is "
					    + getServerJobName(sc) + "\n");
						sb.append("Executing -- " + sql + "\n");
						ResultSet rs = s[j].executeQuery(sql);
						while (rs.next()) {
							rs.getString(1);
						}
						rs.close();
					}
					sql = "call qsys2.qcmdexc('endjob job(*) option(*immed)')";
					try {
						int j = random.nextInt(STATEMENT_COUNT); 
						sb.append("Executing ["+j+"]-- " + sql + "\n");
						s[j].executeUpdate(sql);
						throw new SQLException("Error -- exception not thrown");
					} catch (SQLException e) {
						int sqlcode = e.getErrorCode();
						if (sqlcode != -4498) {
							sb.append("Bad exception received\n");
							printStackTraceToStringBuffer(e, sb);
							throw e;
						} else {
							sb.append("Good exception received\n");
							printStackTraceToStringBuffer(e, sb);
						}
					}

				}
				connection.close(); 
				assertCondition(passed, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 **/
	public void Var003() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random(); 
		int killCount = 0; 
		sb.append("Test a connection with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		sb.append("It will be dropped randomly\n"); 
		
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				String sql;
				Connection killerConnection = testDriver_.getConnection(url_,
				    systemObject_.getUserId(), encryptedPassword_);
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				Statement s = connection.createStatement();
				// Start a thread to kill the connection
				String serverJobName = getServerJobName(connection); 
				KillThread killThread = new KillThread(killerConnection, serverJobName ,random.nextInt(500),sb ); 
			  long endMillis = System.currentTimeMillis() + RUN_SECONDS * 1000; 
			  killThread.start(); 
				while (System.currentTimeMillis() < endMillis ) {
					try {
						sql = statementSql[random.nextInt(statementSql.length)]; 
						synchronized(sb) { 
							sb.append("Executing -- " + sql + "\n");
						}
						boolean resultSetAvailable = s.execute(sql);
						if (resultSetAvailable) { 
						  ResultSet rs = s.getResultSet();
						while (rs.next()) {
							rs.getString(1);
						 }
					  	rs.close();
						}
						
					} catch (SQLException e) { 
						// System.out.println("-----------------"); 
						// e.printStackTrace(System.out); 
						int sqlcode = e.getErrorCode();
						killCount++; 
						if (sqlcode != -4498) {
							synchronized(sb) { 
							  sb.append("Bad exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
							throw e;
						} else {
							synchronized(sb){
							  sb.append("Good exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
						}
						// Run a query to make sure everything is set up
						sql = "VALUES "+random.nextInt(1000000000);
						synchronized(sb) { 
							sb.append("Executing -- " + sql + "\n");
						}
						ResultSet rs = s.executeQuery(sql);
						while (rs.next()) {
							rs.getString(1);
						}
						rs.close();
						// Start a thread to kill the connection 
						serverJobName = getServerJobName(connection); 
						killThread.reset(random.nextInt(500), serverJobName );
						killThread.setName("Killer-"+killCount); 
						
					}
					
				}
				killThread.shutdown(); 
				killThread.join(); 
				sb.append("Final killCount =" +killCount+"\n"); 
				System.out.println("Testcase done: killCount="+killCount);
				connection.close();
				killerConnection.close(); 
				assertCondition(killCount > 0, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	
	
	/**
	 * Test a simple connect with enableClientAffinitiesList.
	 * Test prepared statement objects. 
	 * Make sure the expected error is returned.
	 **/
	public void Var004() {
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		sb.append("Test a simple connect with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				PreparedStatement psQuery = connection.prepareStatement("Values 1");
				PreparedStatement psKill = connection.prepareStatement("call qsys2.qcmdexc('endjob job(*) option(*immed)')");
				for (int i = 0; i < 40; i++) {
					sb.append("General   connection is "
					    + getServerJobName(connection)
					    + "\n");
					Connection sc = psQuery.getConnection();
					sb.append("Statement connection is "
					    + getServerJobName(sc) + "\n");
					for (int j = 0; j < 2; j++) {
						sb.append("Executing PSQuery \n");
						ResultSet rs = psQuery.executeQuery();
						while (rs.next()) {
							rs.getString(1);
						}
						rs.close();
					}
					try {
						sb.append("Executing psKill \n");
						psKill.executeUpdate();
						throw new SQLException("Error -- exception not thrown");
					} catch (SQLException e) {
						int sqlcode = e.getErrorCode();
						if (sqlcode != -4498) {
							sb.append("Bad exception received\n");
							printStackTraceToStringBuffer(e, sb);
							throw e;
						} else {
							sb.append("Good exception received\n");
							printStackTraceToStringBuffer(e, sb);
						}
					}

				}

				connection.close(); 
				assertCondition(passed, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	
	
	
	/**
	 * Test a simple connect with enableClientAffinitiesList.
	 * Use multiple prepared statements and make sure the expected error is returned.
	 **/
	public void Var005() {
		int STATEMENT_COUNT = 10; 
		int LOOP_COUNT = 40; 
		boolean passed = true;
		StringBuffer sb = new StringBuffer();
		
		sb.append("Test a simple connect with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				PreparedStatement[] ps = new PreparedStatement[STATEMENT_COUNT];
				for (int i = 0; i < STATEMENT_COUNT; i++) { 
					ps[i] = connection.prepareStatement("Values 1");
				}
				PreparedStatement psKill = connection.prepareStatement("call qsys2.qcmdexc('endjob job(*) option(*immed)')");
				for (int i = 0; i < LOOP_COUNT; i++) {
					sb.append("General   connection is "
					    + getServerJobName(connection)
					    + "\n");
					for (int j = 0; j < STATEMENT_COUNT; j++) {
					Connection sc = ps[j].getConnection();
					sb.append("Statement ["+j+"] connection is "
					    + getServerJobName(sc) + "\n");
						sb.append("Executing -- Values 1 \n");
						ResultSet rs = ps[j].executeQuery();
						while (rs.next()) {
							rs.getString(1);
						}
						rs.close();
					}

					try {
						sb.append("Executing psKill  \n");
						psKill.executeUpdate();
						throw new SQLException("Error -- exception not thrown");
					} catch (SQLException e) {
						int sqlcode = e.getErrorCode();
						if (sqlcode != -4498) {
							sb.append("Bad exception received\n");
							printStackTraceToStringBuffer(e, sb);
							throw e;
						} else {
							sb.append("Good exception received\n");
							printStackTraceToStringBuffer(e, sb);
						}
					}

				}

				connection.close(); 
				assertCondition(passed, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements with no parameters. 
   */ 
	
	public void Var006() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random(); 
		int killCount = 0; 
		sb.append("Test a connection with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		sb.append("It will be dropped randomly\n"); 
		
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				Connection killerConnection = testDriver_.getConnection(url_,
				    systemObject_.getUserId(), encryptedPassword_);
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				PreparedStatement [] ps = new PreparedStatement[statementSql.length]; 
				for (int i = 0; i < statementSql.length; i++) { 
					ps[i] = connection.prepareStatement(statementSql[i]); 
				}
				// Start a thread to kill the connection
				String serverJobName = getServerJobName(connection); 
				KillThread killThread = new KillThread(killerConnection, serverJobName ,random.nextInt(500),sb ); 
			  long endMillis = System.currentTimeMillis() + RUN_SECONDS * 1000; 
			  killThread.start(); 
				while (System.currentTimeMillis() < endMillis) {
					try {
						int r = random.nextInt(statementSql.length); 
						synchronized(sb) { 
							sb.append("Executing -- " + statementSql[r] + "\n");
						}
						boolean resultSetAvailable = ps[r].execute();
						if (resultSetAvailable) { 
						  ResultSet rs = ps[r].getResultSet();
						while (rs.next()) {
							rs.getString(1);
						 }
					  	rs.close();
						}
						
					} catch (SQLException e) { 
						// System.out.println("-----------------"); 
						// e.printStackTrace(System.out); 
						int sqlcode = e.getErrorCode();
						killCount++; 
						if (sqlcode != -4498) {
							synchronized(sb) { 
							  sb.append("Bad exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
							throw e;
						} else {
							synchronized(sb){
							  sb.append("Good exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
						}
						// Run a query to make sure everything is set up
						int r = random.nextInt(statementSql.length); 
						synchronized(sb) { 
							sb.append("Executing -- " + statementSql[r] + "\n");
						}
						boolean resultSetAvailable = ps[r].execute();
						if (resultSetAvailable) { 
						  ResultSet rs = ps[r].getResultSet();
						while (rs.next()) {
							rs.getString(1);
						 }
					  	rs.close();
						}

						// Start a thread to kill the connection 
						serverJobName = getServerJobName(connection); 
						killThread.reset(random.nextInt(500),serverJobName );
						killThread.setName("Killer-"+killCount); 
						
					}
					
				}
				killThread.shutdown(); 
				killThread.join(); 
				sb.append("Final killCount =" +killCount+"\n"); 
				System.out.println("Testcase done: killCount="+killCount);
				connection.close();
				killerConnection.close(); 

				assertCondition(killCount > 0, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with int parameters. 
	 **/
	public void Var007() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psIntTransactions, psIntParms, JAVA_INT, url, null,RUN_SECONDS,null); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with small parameters. 
	 **/
	public void Var008() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psSmallintTransactions, psSmallintParms, JAVA_SHORT, url, null,RUN_SECONDS,null); 
	}


	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with bigint parameters. 
	 **/
	public void Var009() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psBigintTransactions, psBigintParms, JAVA_LONG, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with real parameters. 
	 **/
	public void Var010() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psRealTransactions, psRealParms, JAVA_FLOAT, url, null,RUN_SECONDS,null); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with float parameters. 
	 **/
	public void Var011() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psFloatTransactions, psFloatParms, JAVA_DOUBLE, url, null,RUN_SECONDS,null); 
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Double parameters. 
	 **/
	public void Var012() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psDoubleTransactions, psDoubleParms, JAVA_DOUBLE, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Decimal parameters. 
	 **/
	public void Var013() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psDecimalTransactions, psDecimalParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Numeric parameters. 
	 **/
	public void Var014() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psNumericTransactions, psNumericParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Decfloat parameters. 
	 **/
	public void Var015() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psDecfloatTransactions, psDecfloatParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}

	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Char parameters. 
	 **/
	public void Var016() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psCharTransactions, psCharParms, JAVA_STRING , url, null,RUN_SECONDS,null); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var017() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psVarcharTransactions, psVarcharParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Graphic parameters. 
	 **/
	public void Var018() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psGraphicTransactions, psGraphicParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var019() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psVargraphicTransactions, psVargraphicParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Clob parameters. 
	 **/
	public void Var020() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psClobTransactions, psClobParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with DBClob parameters. 
	 **/
	public void Var021() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psDBClobTransactions, psDBClobParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Binary parameters. 
	 **/
	public void Var022() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psBinaryTransactions, psBinaryParms, JAVA_BYTEARRAY, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varbinary parameters. 
	 **/
	public void Var023() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psVarbinaryTransactions, psVarbinaryParms, JAVA_BYTEARRAY, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Blob parameters. 
	 **/
	public void Var024() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psBlobTransactions, psBlobParms, JAVA_BYTEARRAY , url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var025() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psDateTransactions, psDateParms, JAVA_DATE , url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var026() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psTimeTransactions, psTimeParms, JAVA_TIME, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var027() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testPSTypeParameters(psTimestampTransactions, psTimestampParms, JAVA_TIMESTAMP, url, null,RUN_SECONDS,null); 
	}

	public void Var028() { notApplicable();}
	public void Var029() { notApplicable();}
	public void Var030() { notApplicable();}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests callableStatements with no parameters. 
	 **/	
	public void Var031() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random(); 
		int killCount = 0; 
		sb.append("Test a connection with enableClientAffinitiesList\n");
		sb.append("Make sure the connection is re-established after it drops\n");
		sb.append("It will be dropped randomly\n"); 
		
		try {

			if (checkToolboxFixDate(TOOLBOX_FIX_DATE)) {
				Connection killerConnection = testDriver_.getConnection(url_,
				    systemObject_.getUserId(), encryptedPassword_);
				String url = url_ + ";enableClientAffinitiesList=1";
				sb.append("Connecting to " + url_ + "\n");
				Connection connection = testDriver_.getConnection(url,
				    systemObject_.getUserId(), encryptedPassword_);
				CallableStatement [] cs = new CallableStatement[callableStatementSql.length]; 
				for (int i = 0; i < callableStatementSql.length; i++) { 
					cs[i] = connection.prepareCall(callableStatementSql[i]); 
				}
				// Start a thread to kill the connection
				String serverJobName = getServerJobName(connection); 
				KillThread killThread = new KillThread(killerConnection, serverJobName ,random.nextInt(500),sb ); 
			  long endMillis = System.currentTimeMillis() + RUN_SECONDS * 1000; 
			  killThread.start(); 
				while (System.currentTimeMillis() < endMillis) {
					try {
						int r = random.nextInt(callableStatementSql.length); 
						synchronized(sb) { 
							sb.append("Executing -- " + callableStatementSql[r] + "\n");
						}
						boolean resultSetAvailable = cs[r].execute();
						if (resultSetAvailable) { 
						  ResultSet rs = cs[r].getResultSet();
						while (rs.next()) {
							rs.getString(1);
						 }
					  	rs.close();
						}
						
					} catch (SQLException e) { 
						// System.out.println("-----------------"); 
						// e.printStackTrace(System.out); 
						int sqlcode = e.getErrorCode();
						killCount++; 
						if (sqlcode != -4498) {
							synchronized(sb) { 
							  sb.append("Bad exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
							throw e;
						} else {
							synchronized(sb){
							  sb.append("Good exception received\n");
							  printStackTraceToStringBuffer(e, sb);
							}
						}
						// Run a query to make sure everything is set up
						int r = random.nextInt(callableStatementSql.length); 
						synchronized(sb) { 
							sb.append("Executing -- " + callableStatementSql[r] + "\n");
						}
						boolean resultSetAvailable = cs[r].execute();
						if (resultSetAvailable) { 
						  ResultSet rs = cs[r].getResultSet();
						while (rs.next()) {
							rs.getString(1);
						 }
					  	rs.close();
						}

						// Start a thread to kill the connection 
						serverJobName = getServerJobName(connection); 
						killThread.reset(random.nextInt(500),serverJobName );
						killThread.setName("Killer-"+killCount); 
						
					}
					
				}
				killThread.shutdown(); 
				killThread.join(); 
				sb.append("Final killCount =" +killCount+"\n"); 
				System.out.println("Testcase done: killCount="+killCount); 
				connection.close(); 
				killerConnection.close(); 

				assertCondition(killCount > 0, sb);

			} else {
				
			}

		} catch (Exception e) {
			failed(e, "Unexpected Exception\n" + sb.toString());
		}
	}

	
	
	
	
	

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with int parameters. 
	 **/
	public void Var032() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csIntTransactions, csIntParms, JAVA_INT, url, null, RUN_SECONDS, null); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with small parameters. 
	 **/
	public void Var033() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csSmallintTransactions, csSmallintParms, JAVA_SHORT, url, null,RUN_SECONDS, null); 
	}


	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with bigint parameters. 
	 **/
	public void Var034() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csBigintTransactions, csBigintParms, JAVA_LONG, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with real parameters. 
	 **/
	public void Var035() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csRealTransactions, csRealParms, JAVA_FLOAT, url, null,RUN_SECONDS,null); 
	}

	
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with float parameters. 
	 **/
	public void Var036() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csFloatTransactions, csFloatParms, JAVA_DOUBLE, url, null,RUN_SECONDS,null); 
	}


	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Double parameters. 
	 **/
	public void Var037() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csDoubleTransactions, csDoubleParms, JAVA_DOUBLE, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Decimal parameters. 
	 **/
	public void Var038() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csDecimalTransactions, csDecimalParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Numeric parameters. 
	 **/
	public void Var039() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csNumericTransactions, csNumericParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Decfloat parameters. 
	 **/
	public void Var040() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csDecfloatTransactions, csDecfloatParms, JAVA_BIGDECIMAL, url, null,RUN_SECONDS,null); 
	}

	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Char parameters. 
	 **/
	public void Var041() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csCharTransactions, csCharParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var042() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csVarcharTransactions, csVarcharParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Graphic parameters. 
	 **/
	public void Var043() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csGraphicTransactions, csGraphicParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varchar parameters. 
	 **/
	public void Var044() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csVargraphicTransactions, csVargraphicParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Clob parameters. 
	 **/
	public void Var045() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csClobTransactions, csClobParms, JAVA_STRING , url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with DBClob parameters. 
	 **/
	public void Var046() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csDBClobTransactions, csDBClobParms, JAVA_STRING, url, null,RUN_SECONDS,null); 
	}
	

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Binary parameters. 
	 **/
	public void Var047() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csBinaryTransactions, csBinaryParms, JAVA_BYTEARRAY, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Varbinary parameters. 
	 **/
	public void Var048() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csVarbinaryTransactions, csVarbinaryParms, JAVA_BYTEARRAY , url, null,RUN_SECONDS,null); 
	}

	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Blob parameters. 
	 **/
	public void Var049() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csBlobTransactions, csBlobParms, JAVA_BYTEARRAY , url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var050() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csDateTransactions, csDateParms, JAVA_DATE, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var051() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csTimeTransactions, csTimeParms, JAVA_TIME, url, null,RUN_SECONDS,null); 
	}
	
	/**
	 * Run a test for 60 seconds where the connection is randomly killed. 
	 * Tests preparedStatements organized as transactions with Date parameters. 
	 **/
	public void Var052() {
		String url = url_ + ";enableClientAffinitiesList=1;transaction isolation=serializable";
		testCSTypeParameters(csTimestampTransactions, csTimestampParms, JAVA_TIMESTAMP, url, null,RUN_SECONDS,null); 
	}
	
	
	
	
	
	
  private String getServerJobName(Connection connection) throws Exception {
  	String rawJob = JDReflectionUtil.callMethod_S(connection, "getServerJobIdentifier"); 
  	return rawJob.substring(20,26)+"/" +
  			   rawJob.substring(10,20).trim() +"/"+
  			   rawJob.substring(0,10).trim(); 
  
  
  }
	
	
	
}
