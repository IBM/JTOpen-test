/*
 * BIDI testcases originally from Gregory Brodsky
 *
 * To adapt for use in our testbucket, the following changes were made.
 *
 *  1.  Add the package test
 *  2.  Replace 'System.out.println("' with 'errorLog_.append("\n'
 *  3.  Replace 'System.out.println()' with 'errorLog_.append("\n)'
 *  4.  Replace 'System.out.print(' with 'errorLog_.append('
 *  5.  Replace reference to TESTLIB with "+schema_+"
 *  6.  Changed lines of form result &=
 *
 * This testcase is driven by JDBIDITestcase
 *  
 */ 

package test;

import java.sql.*;

import com.ibm.as400.access.*;


public class TestBidiMetaData extends TestBidiConnection {

	public TestBidiMetaData() {
		super();
		metadata_reordering = true;  		
		//package_ccsid = "13488";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		TestBidiMetaData t = new TestBidiMetaData();
		
		if(args.length < 3){
			//errorLog_.append("\nUSAGE: java TestFullSuite <host> <username> <password>");
			//return;						
			
			//t.schema_ = ""+schema_+"";
			//t.table_ = "BIDI_TEST_TABLE";
			t.table_ = "BIDI_TAB";
		} else {						
			t.host = args[0];
			t.username = args[1];
			t.password = args[2];
		}
		t.setHostCCSID(424);
		t.run();
		
		}
	/*
    public Connection getConnection(int param){    	
    	String url_parameters;
    	if(param >= 0)
    		url_parameters = ";bidi implicit reordering=true;package ccsid=system;bidi string type=" + param;
    	else
    		url_parameters = ";bidi implicit reordering=true;package ccsid=system";
    	return getConnection(url_parameters);    	
    }

	public Connection getConnection(String url_parameters){ 
		db2Conn = super.getConnection(url_parameters);
		try {
			RecreateTable(db2Conn);
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return db2Conn;
	}
	 */
	public boolean run(){		
		errorLog_.append("\n\nTestBidiMetaData host="+host+" package_ccsid="+package_ccsid + " user-profile CCSID = "  + user_profile_ccsid + " TABLE "+ this.schema_ + "." + this.table_ + " START");

		//int user_profile_ccsid = getHostCCSID();		
		
		if(!AS400BidiTransform.isVisual(user_profile_ccsid)){
			errorLog_.append("\nBidi meta-data not supported for Logical host CCSID");
			return true;
		}
		
		TestString = use_complex_strings? TestString1 :	TestString2;
		
		boolean result = true;
		try{
		    if (!CreateMultiColumnTable(true)) {
		      errorLog_.append("FAILURE in CreateMultiColumnTable\n"); 
		      result = false; 
		    }

                    if (!UseBidiTableName()) {
                      errorLog_.append("FAILURE in UseBidiTableName\n"); 
                      result = false; 
                    }

                    if (!testColumnName()) {
                      errorLog_.append("FAILURE in TestColumnName\n"); 
                      result = false; 
                    }
		}
		
		catch(Exception e){
			errorLog_.append("\n"+e);
			e.printStackTrace();
			result = false;
		}
		errorLog_.append("\n\nFinal Result of Bidi testing: " + (result ? "SUCCESS" : "FAILURE"));
		errorLog_.append("\n\nTestBidiMetaData host="+host+" package_ccsid="+package_ccsid + " user-profile CCSID = " + user_profile_ccsid +" END");
		return result;			
	}

	boolean CreateSampleTable() throws SQLException{
		boolean  result = true;		
		metadata_reordering = true;  		  
		table_name = //""+schema+".\"TABLE4_\u05D0\u05D1\u05D2\u05D3\"";
			""+schema_+".\"\u05D0\u05D1\u05D2\u05D3_41\"";
		RecreateTable(4);
		table_name = //""+schema+".\"TABLE5_\u05D0\u05D1\u05D2\u05D3\"";
			""+schema_+".\"\u05D0\u05D1\u05D2\u05D3_51\"";		
		RecreateTable(5);

		metadata_reordering = false;

		table_name = //""+schema+".\"TABLE4_\u05D0\u05D1\u05D2\u05D3\"";
			""+schema_+".\"\u05D0\u05D1\u05D2\u05D3_40\"";
		RecreateTable(4);
		table_name = //""+schema+".\"TABLE5_\u05D0\u05D1\u05D2\u05D3\"";
			""+schema_+".\"\u05D0\u05D1\u05D2\u05D3_50\"";		
		RecreateTable(5);		

		return result;
	}
	
	String local_column_name;
	
	boolean CreateMultiColumnTable(boolean testDmd) throws SQLException{
		errorLog_.append("\nTest CreateMultiColumnTable, create using ALTER, check using Connection.getMetaData()");
		boolean check, result = true;
		Statement st;
		int i,j;
		metadata_reordering = true;  
		String value, etalon;
		table_ = "METATEST"; 
		
		table_name = this.schema_ + "." + this.table_; 
		local_column_name = //"ABC"; 
			"ABC \u05D0\u05D1\u05D2";
		RecreateTable(5);
							
		for(i = 4; i < 12; i++){
			db2Conn = getConnection(i);
			st = db2Conn.createStatement();
			st.executeUpdate("ALTER TABLE " + table_name + " ADD \"" + local_column_name + (char)(0x41+i) + "\" CHAR");
			st.close();
			db2Conn.close();
		}
	
		String catalog = null;
		DatabaseMetaData md = null;
		ResultSet resultSet = null;
		ResultSetMetaData rm = null;
		
		for(j = 4; j < 12; j++){
			db2Conn = getConnection(j);
			
			st = db2Conn.createStatement();
			resultSet = st.executeQuery("SELECT * FROM "+table_name);
			rm = resultSet.getMetaData();
			for(i = 1; i < 4; i++) {
			    value = rm.getColumnName(i);
			    errorLog_.append("\nResultSetMetaData: getColumnName:" + value );
			    errorLog_.append("\nResultSetMetaData:  i="+i+" j= "+j); 
			}
			for(i = 4; i < 12; i++){
				value = rm.getColumnName(i);
				etalon = BidiEngineWrapper.bidiTransform(local_column_name+ (char)(0x41+i) , i, j);
				check = value.equals(etalon);
				if(!check) {
				    result = false;				
				    errorLog_.append("\nResultSetMetaData: getColumnName:" + value );
				    errorLog_.append("\nResultSetMetaData: bidiTransform:" + etalon );
				    errorLog_.append("\nResultSetMetaData: startName     " + (local_column_name+ (char)(0x41+i)));
				    errorLog_.append("\nResultSetMetaData:  i="+i+" j= "+j); 
				}
			}
			resultSet.close();
			st.close();
			
			md = db2Conn.getMetaData();
			
			resultSet = md.getCatalogs();
            while (resultSet.next()) {
           	 catalog = resultSet.getString("TABLE_CAT");
            }
            resultSet.close();
			
			resultSet = md.getColumns(catalog, this.schema_, this.table_, "%");
			resultSet.next();
			resultSet.next();
			resultSet.next();
			i = 4;
			if (testDmd) { 
			    while (resultSet.next()) {
				value = resultSet.getString("COLUMN_NAME");//
				etalon = BidiEngineWrapper.bidiTransform(local_column_name+ (char)(0x41+i) , i, j);
				check = value.equals(etalon);
				if(!check)
				    result = false;				
				errorLog_.append("\nDatabaseMetaData : " + value + " \u202d < \u202c " + etalon + (check ? " CORRECT " : " WRONG ") + " i="+i+" j="+j);						
				i++;
			    }
			}
			resultSet.close();		
			db2Conn.close();
		}				
		return result;
	}
	
	boolean UseBidiTableName() throws SQLException{
		errorLog_.append("\nTest possibility to use Hebrew table name with set metadata_reordering to false");
	
		metadata_reordering = false;  
		table_name = ""+schema_+".\"\u05D0\u05D1\u05D2\"";						
		RecreateTable(5);
		return Testcase_Insert();
	}

	boolean testColumnName() throws SQLException{
		errorLog_.append("\ntestColumnName(): create using ALTER, check using ResultSetMetaData");
		boolean check, limitation,   overall_result = true, overall_limitations = false;
		Statement st;
		metadata_reordering = true;  
		table_name = ""+schema_+".METATEST";
				
		String [] local_column_names1	= {
				
				"ABC \u05D0\u05D1\u05D2X",
				"ABC \u05D0\u05D1\u05D2", 
				"\u05D0\u05D1\u05D2 ABCD",
				"ABC 123 \u05D0\u05D1\u05D2", 
				"\u05D0\u05D1\u05D2 123 ABCD" 
		};
		String [] local_column_names2 = {
				"ABC \u05D0\u05D1\u05D2! X."
		};		
		String [] local_column_names = this.use_complex_strings? local_column_names1 : local_column_names2;

		String local_column_name1 = null;
		String result_column_name = null;
		
		int start = 4, end = 12; 

		for(int i = start; i < end; i++){			
			db2Conn = getConnection(i);
			RecreateTable(db2Conn);
			st = db2Conn.createStatement();
			st.executeUpdate("insert into "+table_name+" values(1, 1, 'X')"); 		
			for(int j = 0; j < local_column_names.length; j++ ){
				local_column_name1 = local_column_names[j];
				//local_column_name = local_column_names[1];
				
				//There are three columns in the table created by RecreateTable(), we create column #j
				st.executeUpdate("ALTER TABLE " + table_name + " ADD \"" + local_column_name1 + "\" CHAR");			
					
				ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);
				//errorLog_.append("\nthis " + resultSet.findColumn(this.column_name) + " local " + resultSet.findColumn(local_column_name));
				ResultSetMetaData rm = resultSet.getMetaData();	
				resultSet.next();
				result_column_name = rm.getColumnName(4 + j);
        limitation = false; 
				check = local_column_name1.equals(result_column_name);
				if(check){
					limitation = false;
				} else {
					errors_count++;
					if (db2Conn instanceof AS400JDBCConnection) {
					String probe = BidiEngineWrapper.getProbe(local_column_name1, (AS400JDBCConnection) db2Conn, i, i, false);
					limitation = probe.equals(result_column_name);
					
					//problem with '7' (Visual Contextual)
					if(i == 7){
						probe = BidiEngineWrapper.getProbe(local_column_name1, (AS400JDBCConnection) db2Conn, 4, 7, false);
						limitation = limitation || probe.equals(result_column_name);
						probe = BidiEngineWrapper.getProbe(local_column_name1, (AS400JDBCConnection) db2Conn, 8, 7, false);
						limitation = limitation || probe.equals(result_column_name);		
						//errorLog_.append("\nProblem with Visual Contextual");
					}
					
					limitation = limitation || BidiEngineWrapper.checkRoundTripLimitation(local_column_name1, result_column_name);
					if(limitation)
						overall_limitations = true;
					else
						overall_result = false;
					} else {
					  overall_result = false; 
					}
				}

				errorLog_.append("Line #" + j + ", Bidi String Type:" + i + " " + local_column_name1 + " VS " + result_column_name
						+ (check ? " CORRECT" : " WRONG"));				
				if(!check)
					errorLog_.append((limitation ? " (LIMITATION)" : "(error)"));


				if(check){
					try{
						resultSet.findColumn(local_column_name1);
						resultSet.getString(local_column_name1);
						errorLog_.append(", column FOUND");
					} catch (Exception e){
						errorLog_.append(", column NOT FOUND");
						check = false;
					}										
				} else
					overall_result = false;								
			  	errorLog_.append("\n");
				} 
			st.close();
		       db2Conn.close(); 
		}

		errorLog_.append("\n");
		errorLog_.append("\nFinal Result: " + (overall_result ? "SUCCESS" : "FAILURE") 
				+ (overall_limitations? " (with limitations)" : "") + ", number of discrepancies: " + errors_count);		
		return overall_result;
	}

	void extractColumnName() throws SQLException{
		
		Statement st;
		metadata_reordering = true;  
		table_name = ""+schema_+".METATEST";
		String column_name1 = "ABC \u05D0\u05D1\u05D2";		
		RecreateTable(4);
		db2Conn = getConnection(4);
		st = db2Conn.createStatement();
		st.executeUpdate("ALTER TABLE " + table_name + " ADD \"" + column_name1 + "\" CHAR");
		st.close();
		db2Conn.close();
		int i, j;
		String value;
		for(j = 4; j < 12; j++){			
			errorLog_.append("\n " + j + " ");
			db2Conn = getConnection(j);
			st = db2Conn.createStatement();
			ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);
			ResultSetMetaData rm = resultSet.getMetaData();	
			
			for(i = 1; i <= rm.getColumnCount(); i++){
				value = rm.getColumnName(i);
				value = "\u202d" + value + "\u202c";
				errorLog_.append(" " + value);		
			}
			db2Conn.close(); 
		}				

	}	
	 void getMetaData(int i){
		 db2Conn = getConnection(i);
		 DatabaseMetaData md = null;
		try {
			md = db2Conn.getMetaData();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return; 
		}
		try {
			errorLog_.append("\nCatalogTerm = "+ md.getCatalogTerm());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		 errorLog_.append("\n  - Catalogs               : ");
         try {
             ResultSet catalogs = md.getCatalogs();
             while (catalogs.next()) {
            	 errorLog_.append("\n    - " + catalogs.getString(1) );
             }
             catalogs.close();
         } catch (SQLException e) {
        	 errorLog_.append("\njava.sql.SQLException: Unsupported feature");
         }

         errorLog_.append("\n");
         
		     errorLog_.append("\n  - Columns               : ");
         try {
             ResultSet res = md.getColumns("IGSD1", this.schema_, this.table_, "%");//this.table_
             while (res.next()) {
            	 // int col_num = res.getMetaData().getColumnCount();
            	 //for(int j = 1; j <=col_num; j++)            	 
            	//	 errorLog_.append("    - " + res.getString(j) );
            	 errorLog_.append("    - " + res.getString(4) );//this is the column name
            	 errorLog_.append("\n");
             }
             res.close();
         } catch (SQLException e) {
        	 errorLog_.append("\njava.sql.SQLException: Unsupported feature");
         }

         errorLog_.append("\n");		 
         
	 }
}
