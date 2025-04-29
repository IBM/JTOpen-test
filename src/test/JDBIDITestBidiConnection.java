
/*
 * BIDI testcases originally from Gregory Brodsky
 *
 * To adapt for use in our testbucket, the following changes were made.
 *
 *  1.  Add the package test
 *  2.  Realize that errorLog_ buffer is inherited from TestBidiBasic
 *  3.  Initialize schema to JDNLSTest.COLLECTION;
 *  4.  Replace 'System.out.println("' with 'errorLog_.append("\n'
 *  5.  Replace 'System.out.print(' with 'errorLog_.append('
 *
 * This testcase is driven by JDBIDITestcase
 *  
 */ 


package test;

import java.sql.*;
import java.util.Arrays;

import com.ibm.as400.access.*;


public class JDBIDITestBidiConnection extends JDBIDITestBidiBasic {
	protected String[] TestString;
	
	protected String[] TestString1 = {
		
		//check simple	
		"ABC \u05D0\u05D1\u05D2",
		"\u05D0\u05D1\u05D2 ABC",
		
		//check round trip
		"ABC 123 \u05D0\u05D1\u05D2",
		"ABC \u05D0\u05D1\u05D2 123",
		"\u05D0\u05D1\u05D2 ABC 123",
		"\u05D0\u05D1\u05D2 123 ABC",
		"123 \u05D0\u05D1\u05D2 ABC",
		"123 ABC \u05D0\u05D1\u05D2",
		
		//check apostrophes
		"A' \u05D0\u05D1\u05D2 123",
		"\u05D0'\u05D2 123'",
		"\u0041\u05D0"+"0123",
		
		//check neutrals
		"&*%$#",
		
		//check contextual
		"\u05D0\u05D1\u05D2 123 \u05D3\u05D4\u05D5",
		"ABC 123 DEF",

	};
	
	protected static String[] TestString2 = {
		"ABC \u05D0\u05D1\u05D2! X."
		//"\u202D AA 12 \u05D0\u05D1\u05D2 34 BB CC"
	};

	protected Connection db2Conn;		


	
	public JDBIDITestBidiConnection() {
		super();	  
		metadata_reordering = false;
		driverUrlBase="jdbc:as400://"; 
	}

	public JDBIDITestBidiConnection(String driverUrlBase, String host, String username, String password) {
		super();	  
		metadata_reordering = false;  
		this.host = host;
		this.username = username;
		this.encryptedPassword = PasswordVault.getEncryptedPassword(password);
		this.driverUrlBase = driverUrlBase; 
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {						
		

		if(args.length < 3){
			System.out.println("USAGE: java TestFullSuite <host> <username> <password>");
			return;
		}
		
		

		JDBIDITestBidiConnection t = new JDBIDITestBidiConnection("jdbc:as400://", args[0], args[1], args[2]);
		
		t.use_complex_strings = true;
		t.TestString = t.TestString1;
		t.package_ccsid = "13488";		
		int original_ccsid = t.getHostCCSID();
		errorLog_.append("\noriginal_ccsid="+original_ccsid);
		t.setHostCCSID(62235);//424
		t.printHostInfo();
		errorLog_.append("\nUsing table " + t.table_name + " column ccsid " + t.column_ccsid); 
				
		Trace.setTraceOn(false);
		try {
			t.RecreateTable();
			t.Delete();
			t.Testcase_Insert();
			//t.Testcase_CallableInsert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//t.run();		
		t.setHostCCSID(424);
		errorLog_.append("\nerrors_count = " +	t.errors_count);
	}
	

	public boolean Testcase_DataSource(){
		try{
		  char[] password = PasswordVault.decryptPassword(encryptedPassword); 
			AS400JDBCDataSource ds = new AS400JDBCDataSource(this.host,this.username,password);
			ds.setBidiStringType(5);
			ds.setPackageCcsid(424);
			
			AS400JDBCManagedDataSource ds1 = new AS400JDBCManagedDataSource(this.host,this.username,password);
			ds1.setBidiStringType(5);
			ds1.setPackageCcsid(424);
		        Arrays.fill(password, ' '); 
			
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean run(){
	  errorLog_ = new StringBuffer();
	  errorLog_.append("\n\nTestBidiConnection host="+host+" package_ccsid="+package_ccsid + " user-profile CCSID = " + user_profile_ccsid +" START");

	  
		boolean result = true;
				
		TestString = use_complex_strings? TestString1 : TestString2;
					
		try {
			RecreateTable();
		} catch (Exception e) {
			errorLog_.append("\nSome not Bidi specific problem occured, but we will try to continue the test...\n"+e);
			e.printStackTrace();
			return true;
		}
		
		try{
			//Trace.setTraceErrorOn(true);			
			//Trace.setTraceAllOn(true);						
			//Trace.setTraceOn(true);
			Trace.setTraceOn(false);
			
			//result &= Testcase_DataSource();
			//result &= t.Testcase_DefaultOptions1();
			
			//result &= Testcase_DefaultOptions2();
			result &= Testcase_Insert();							
			result &= Testcase_CallableInsert();						
			result &= Testcase_PreparedInsert();									
			result &= Testcase_Select();
			result &= Testcase_ResultSetUpdate();
			
			//Testcase_CompareInserts();
		}
		catch(Exception e){
			errorLog_.append("\n"+e+"\n");
			e.printStackTrace();
			result = false;
		}
		errorLog_.append("\nFinal Result of Bidi testing: " + (result ? "SUCCESS" : "FAILURE") + " , number of discrepancies: " + errors_count);
		errorLog_.append("\n\nTestBidiConnection host="+host+" package_ccsid="+package_ccsid + " user-profile CCSID = " + user_profile_ccsid +" END");
		return result;
	}		
	

	boolean Testcase_DefaultOptions1() throws SQLException {
		int j;
		errorLog_.append("\nTest Default Connection Options ");		
		boolean overall_result = true;
		db2Conn = getConnection("");
		Delete(db2Conn);
		Statement st = db2Conn.createStatement();
		//1st line does not cause round-trip problem, so it should work correct with default options.
		for(j = 0; j < TestString.length; j++ )
			st.executeUpdate("insert into "+table_name+" values("+0+", "+j+", '" 
					+ duplicateApostrophs(TestString[j]) + "')");
		
		ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);
		j = 0;
		while (resultSet.next()){			
			String value = resultSet.getString(3);  
			String s1 = BidiEngineWrapper.bidiTransform(value, 4, 5);
			String s2 = BidiEngineWrapper.bidiTransform(TestString[j], 4, 5);
			//boolean result = value.equals(TestString[j]);
			boolean result = s1.equals(s2);
			//value = "\u202d" + value + "\u202c";
			errorLog_.append("\n"+value + "\u2003 < " + TestString[j] +  "\u2003  " + (result ? "SUCCESS" : "FAILURE"));
			overall_result &= result;
			j++;
		}
		resultSet.close();
		st.close();
		db2Conn.close();
		db2Conn = null; 
		return overall_result;
	}
	
	boolean Testcase_DefaultOptions2() throws SQLException {
		int j;
		errorLog_.append("\nTest Default Connection Options");		
		boolean overall_result = true;
		db2Conn = getConnection("");//(";package ccsid=13488");
		Delete(db2Conn);
		Statement st = db2Conn.createStatement();
		//1st line does not cause round-trip problem, so it should work correct with default options.
		for(j = 0; j < 2; j++ )
			st.executeUpdate("insert into "+table_name+" values("+0+", "+j+", '" 
					+ duplicateApostrophs(TestString[j]) + "')");
		
		ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);
		j = 0;
		while (resultSet.next()){			
			String value = resultSet.getString(3);  
			String s1 = BidiEngineWrapper.bidiTransform(value, 10, 5);
			String s2 = BidiEngineWrapper.bidiTransform(TestString[j], 10, 5);
			//boolean result = value.equals(TestString[j]);
			boolean result = s1.equals(s2);
			//value = "\u202d" + value + "\u202c";
			errorLog_.append("\n"+value + "\u2003 < " + TestString[j] +  "\u2003  " + (result ? "SUCCESS" : "FAILURE"));
			overall_result &= result;
			j++;
		}
		resultSet.close();
		st.close();
		db2Conn.close();
		return overall_result;
	}

	
	boolean Testcase_Select() throws SQLException {		
		Statement st;
		int k = 4; //type of host DB ccsid
		int i = k,j;
		errorLog_.append("\nTest SELECT");			
		Connection db2Conn1 = getConnection(k);		
		Delete(db2Conn1);
		st = db2Conn1.createStatement();		
		CallableStatement cst;
		for(j = 0; j < TestString.length; j++ ){
		//	st.executeUpdate("insert into "+table_name+" values("+i+", "+j+", '" 
		//			+ duplicateApostrophs(TestString[j]) + "')");
			cst =  db2Conn1.prepareCall("insert into "+table_name+" values(?, ?, ?)");	    		
    		cst.setInt(1, i); 
    		cst.setInt(2, j); 
    		cst.setString(3, TestString[j]);
    		cst.executeUpdate();				
    		cst.close();
		}
				
		
		String probe = "";
		boolean overall_result = true;
		boolean limitation = false, overall_limitations = false;
		ResultSet resultSet;
		for(i = 4; i<12; i++ ){
			db2Conn = getConnection(i);
			st = db2Conn.createStatement();
			resultSet = st.executeQuery("SELECT * FROM "+table_name);
			String value;
			boolean check = false;
			errorLog_.append("\nBidi type = " + i);
			while (resultSet.next()){
				//i = resultSet.getInt(1);
				j = resultSet.getInt(2);
				value = resultSet.getString(3);    

				String etalon = BidiEngineWrapper.bidiTransform(TestString[j], k, i);				
				check = value.equals(etalon);
				
				limitation = false;
				if(!check){
					errors_count++;
					if ( db2Conn1 instanceof AS400JDBCConnection) { 
					probe = BidiEngineWrapper.getProbe(TestString[j], (AS400JDBCConnection) db2Conn1, k, i, true);
					
					limitation = !etalon.equals(probe);//limitation = value.equals(probe);//
					limitation = limitation || !value.equals(probe);
					
					if(!limitation)
						limitation = BidiEngineWrapper.bidiTransform(value, k, i).equals(BidiEngineWrapper.bidiTransform(probe, k, i));
													
					limitation = limitation || BidiEngineWrapper.checkRoundTripLimitation(value, probe);//Brodsky1
					//
					
					
					if(limitation)
						overall_limitations = true;
						else
						overall_result = false;
					} else {
					  overall_result = false; 
					}
				}

				value = "\u202d" + value + "\u202c";
				value.replaceAll("\n", "\u202c\n\u202d");

				etalon = "\u202d" + etalon + "\u202c";
				etalon.replaceAll("\n", "\u202c\n\u202d");

				probe = "\u202d" + probe + "\u202c";
				probe.replaceAll("\n", "\u202c\n\u202d");				
				
				errorLog_.append("String N" + j + " ");
				//errorLog_.append("\n"+value + " \u202d < \u202c " + etalon + (check ? " CORRECT" : " WRONG") + (limitation ? " (LIMITATION)" : "(error)"));
				errorLog_.append(value + " \u202d > \u202c " + etalon + (check ? " CORRECT" : " WRONG"));
				if(!check)
					errorLog_.append((limitation ? " (LIMITATION)" : "(error)"));
				errorLog_.append("\n");

			}
			errorLog_.append("\n");			

			st.close();
			db2Conn.close();
		}
		db2Conn1.close();
		errorLog_.append("\nFinal Relust: " + (overall_result ? "SUCCESS" : "FAILURE") + (overall_limitations? " (with limitations)" : ""));
		
		return overall_result;
	}


	boolean Testcase_ResultSetUpdate() throws SQLException {		
		Statement st;
		ResultSet resultSet;
		int i,j, i1;
		
		errorLog_.append("\nTest UPDATE using ResultSet()");
		db2Conn = getConnection("");
		Delete(db2Conn);	
		st = db2Conn.createStatement();
		for(i = 4; i<12; i++ ){
			for(j = 0; j < TestString.length; j++ ){					
				st.executeUpdate("insert into "+table_name+" values("+i+", "+j+", 'XXX')");						
			}
		}
		st.close();		
		db2Conn.close();

		for(i = 4; i<12; i++ ){
			db2Conn = getConnection(i);
			st = db2Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			resultSet = st.executeQuery("SELECT * FROM "+table_name);
			while(resultSet.next()){
				i1 = resultSet.getInt(1);
				j = resultSet.getInt(2);				
				if(i == i1){
					resultSet.updateString(3, TestString[j]);
					resultSet.updateRow();
				}													
			}
			st.close();
			db2Conn.close();			
		}		
		
		return GetVisualResult(true);		
	}


	
	public boolean Testcase_Insert() throws SQLException {		
		Statement st;
		int i,j;
		errorLog_.append("\nTest INSERT using createStatement()");
		int user_profile_ccsid1 = getHostCCSID();
		int actual_package_ccsid = user_profile_ccsid1;
		if(!package_ccsid.equals("system"))
			actual_package_ccsid = Integer.parseInt(package_ccsid);
		
		if(actual_package_ccsid != user_profile_ccsid1 
				&& !AS400BidiTransform.isVisual(actual_package_ccsid) 
				&& !AS400BidiTransform.isVisual(user_profile_ccsid1)){
			//they are different and both logical
			errorLog_.append("\nNot supported");
			//Brodsky? return true;
		}
		
		Delete();	
		for(i = 4; i<12; i++ ){
			db2Conn = getConnection(i);		
			for(j = 0; j < TestString.length; j++ ){
				
				st = db2Conn.createStatement();
				String statement = "insert into "+table_name+" values("+i+", "+j+", '" 
				+ duplicateApostrophs(TestString[j]) + "')"; 
				st.executeUpdate(statement);
				st.close();
			}
			db2Conn.close();
		}
		return GetVisualResult(true);
	}

	
	boolean Testcase_CallableInsert() throws SQLException {				
		CallableStatement st;
		int i,j;
		errorLog_.append("\nTest INSERT using prepareCall(), with CallableStatement");
		Delete();	
		for(i = 4; i < 12; i++ ){ //for all bidi options...			
			db2Conn = getConnection(i);
			for(j = 0; j < TestString.length; j++ ){								
				st =  db2Conn.prepareCall("insert into "+table_name+" values(?, ?, ?)");	    		
	    		st.setInt(1, i); 
	    		st.setInt(2, j); 
	    		st.setString(3, TestString[j]);
	    		st.executeUpdate();				
	    		
				st.close();				
			}
			db2Conn.close();
		}
		return GetVisualResult(false);
	}
	
	boolean Testcase_PreparedInsert() throws SQLException {				
		PreparedStatement st;
		int i,j;
		errorLog_.append("\nTest INSERT using prepareStatement(), with PreparedStatement");
		Delete();	
		for(i = 4; i < 12; i++ ){ //for all bidi options...
			db2Conn = getConnection(i);
			for(j = 0; j < TestString.length; j++ ){								
				
				st =  db2Conn.prepareStatement("insert into "+table_name+" values(?, ?, ?)");	    		
	    		st.setInt(1, i); 
	    		st.setInt(2, j); 
	    		st.setString(3, TestString[j]);
	    		st.executeUpdate();												
				st.close();				
			}
			db2Conn.close();
		}
		return GetVisualResult(false);
	}
	
	boolean Testcase_CompareInserts() throws SQLException {	
		Statement st1;
		CallableStatement st2;
		PreparedStatement st3;
		int i,j;
		
		int user_profile_ccsid1 = getHostCCSID();
		int actual_package_ccsid = user_profile_ccsid1;
		if(!package_ccsid.equals("system"))
			actual_package_ccsid = Integer.parseInt(package_ccsid);
		
		if(actual_package_ccsid != user_profile_ccsid1 
				&& !AS400BidiTransform.isVisual(actual_package_ccsid) 
				&& !AS400BidiTransform.isVisual(user_profile_ccsid1)){
			//they are different and both logical
			errorLog_.append("\nNot supported");
			return true;
		}		
		
		Delete();	
		for(i = 4; i < 12; i++ ){ //for all bidi options...
			db2Conn = getConnection(i);			
			for(j = 0; j < TestString.length; j++ ){								
				st1 = db2Conn.createStatement();
				st1.executeUpdate("insert into "+table_name+" values("+i+", "+j+", '" 
						+ "Bidi_type " + i + " string #" + j + "')");
				
				st1.executeUpdate("insert into "+table_name+" values("+i+", "+j+", '" 
						+ duplicateApostrophs(TestString[j]) + "')");
				st1.close();
								
				st2 =  db2Conn.prepareCall("insert into "+table_name+" values(?, ?, ?)");	    		
	    		st2.setInt(1, i); 
	    		st2.setInt(2, j); 
	    		st2.setString(3, TestString[j]);
	    		st2.executeUpdate();					    		
				st2.close();		
				
				st3 =  db2Conn.prepareStatement("insert into "+table_name+" values(?, ?, ?)");	    		
	    		st3.setInt(1, i); 
	    		st3.setInt(2, j); 
	    		st3.setString(3, TestString[j]);
	    		st3.executeUpdate();				
								
				st3.close();							
			}
			db2Conn.close();
		}
		
	return true;
	}
	
	private boolean GetVisualResult(boolean use_packageCCSID) throws SQLException {
		errorLog_.append("\nBelow is visual output that you can compare with a table as it looks in PCOMM");		
		int host_ccsid_type = AS400BidiTransform.getStringType(this.column_ccsid);
		
		db2Conn = getConnection(4);//get it Visual LTR
		Statement st = db2Conn.createStatement();
		int i,j;

		ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);
		String value, probe;
		boolean check = false;
		boolean overall_result = true, overall_limitations = false;
		boolean limitation = false;
		while (resultSet.next()){
			i = resultSet.getInt(1);
			j = resultSet.getInt(2);
			value = resultSet.getString(3);    

			String etalon = BidiEngineWrapper.bidiTransform(TestString[j], i, host_ccsid_type);			
			check = value.equals(etalon);
			
			limitation = false;
			if(!check){				
				errors_count++;
				if (db2Conn instanceof AS400JDBCConnection) { 
				    probe = BidiEngineWrapper.getProbe(TestString[j], (AS400JDBCConnection) db2Conn, i, host_ccsid_type, use_packageCCSID);
				    limitation = value.equals(probe) || etalon.equals(probe);
				//if(use_packageCCSID)
				//	limitation = limitation || BidiEngineWrapper.checkRoundTripLimitation(value, etalon);
				//if(BidiEngineWrapper.checkRoundTripLimitation(value, etalon))
				//	errorLog_.append("\npossible round-trip:");

				//if(!limitation)
				//	limitation = BidiEngineWrapper.checkRoundTrip(value, probe, (AS400JDBCConnection) db2Conn, i, host_ccsid_type, use_packageCCSID);


				    if(limitation)
					overall_limitations = true;
				    else
					overall_result = false;
				} else {
				    overall_result = false;
				}
			}
			value = "\u202d" + value + "\u202c";
			value.replaceAll("\n", "\u202c\n\u202d");

			etalon = "\u202d" + etalon + "\u202c";
			etalon.replaceAll("\n", "\u202c\n\u202d");

			
			errorLog_.append("Bidi type = " + i + " String N" + j + " ");
			
			errorLog_.append(value + " \u202d > \u202c " + etalon + (check ? " CORRECT" : " WRONG"));							
			if(!check)				errorLog_.append((limitation ? " (LIMITATION)" : "(error)"));
			
		/*
			//Brodsky start
			etalon = BidiEngineWrapper.bidiTransform(TestString[j], 10, host_ccsid_type);
			etalon = "\u202d" + etalon + "\u202c";
			errorLog_.append("\t Source \u202d" + TestString[j] + "\u202c\tResult " + value +"\tCompare "+ etalon + (etalon.equals(value) ?  " CORRECT" : " WRONG"));			
			//Brodsky end						
		*/	
			errorLog_.append("\n");
		}
		errorLog_.append("\n");
		errorLog_.append("\nFinal Result: " + (overall_result ? "SUCCESS" : "FAILURE") + (overall_limitations? " (with limitations)" : ""));
		
		st.close();
		db2Conn.close();
		
		return overall_result;
	}


	protected static String duplicateApostrophs(String txt){
		StringBuffer buf = new StringBuffer("");

		char c;
		for(int i=0; i<txt.length();i++){
			c = txt.charAt(i);
			if(c == '\'')
				buf.append("''");
			else
				buf.append(c);
		}
		return buf.toString();
	}

	public Connection getConnection(String url_parameters){     			
		Connection db2Conn1;
		//url_parameters = url_parameters + ";toolbox trace=all";
		//url_parameters = url_parameters;
		try {
			String conURL = driverUrlBase+host+url_parameters;
//			System.out.println("connection url = " + conURL + ", username = " + username ;+ ", password = " + password);
			String password = PasswordVault.decryptPasswordLeak(encryptedPassword,"JDiDITestBidiConnection.getConnection"); 
			db2Conn1 = DriverManager.getConnection(conURL,username, password);
			//errorLog_.append("\nConnect to "+host);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
		return db2Conn1;    	
    }
	
}

