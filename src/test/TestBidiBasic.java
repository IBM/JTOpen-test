/*
 * BIDI testcases originally from Gregory Brodsky
 *
 * To adapt for use in our testbucket, the following changes were made.
 *
 *  1.  Add the package test
 *  2.  Add the errorLog_ buffer.
 *  3.  Initialize schema to JDNLSTest.COLLECTION;
 *  4.  Replace 'System.out.println("' with 'errorLog_.append("\n'
 *  5.  Replace 'System.out.print' with 'errorLog_.append'
 *
 * This testcase is driven through TestBidiConnection in JDBIDITestcase
 *  
 */ 

package test;


import java.io.IOException;
import java.sql.*;

import com.ibm.as400.access.*;

public class TestBidiBasic{
        static protected StringBuffer errorLog_ = new StringBuffer(); //for error messages
	
	protected String mixed_string; //  "ABC \u05D0\u05D1\u05D2 123";
		//"A'' \u05D0\u05D1\u05D2 123";//!!
		//"A'''' \u05D0\u05D1\u05D2 123";
		//"'''' \u05D0\u05D1\u05D2 123";
	
	protected String table_ = "BIDI_TAB";//"JT400";
	protected String schema_ = JDNLSTest.COLLECTION;
	
	protected String table_name;  //"TESTLIB.JT400";
		//"\"QGPL\".\"TABLE_\u05D2\u05D1\u05D0\"";
		//"\"TABLE_\u05D0\u05D1\u05D2\"";
	
	protected String column_name;  //"WORD";
		//"\"COLUMN_\u05D2\u05D1\u05D0\"";
	protected int errors_count = 0;
	
	public boolean use_complex_strings = true;

	protected int column_ccsid = 424;
		
	public static void main_(String[] args) {
		 //System.out.println(AS400BidiTransform.SQL_statement_reordering("'\u05D0\u05D1\u05D2 \r\n \u05D3\u05D4\u05D5'",4,4));
		 //System.out.println(AS400BidiTransform.SQL_statement_reordering("'\u05D0\u05D1\u05D2 \r\n \u05D3\u05D4\u05D5'",5,4));
		 //System.out.println(AS400BidiTransform.SQL_statement_reordering("'\u05D0\u05D1\u05D2 \u05D3\u05D4\u05D5\r\n'",4,4));
		 //System.out.println(AS400BidiTransform.SQL_statement_reordering("'\u05D0\u05D1\u05D2 \u05D3\u05D4\u05D5\r\n'",5,4));
	 }
	 
	 protected  String host;
	 protected  String username; 
	 protected  String password;	 
	 protected  String package_ccsid = "system";//"13488";
	 protected  int user_profile_ccsid = 424;
	 
	 protected  boolean metadata_reordering = false;  

	String driverUrlBase = "jdbc:as400://"; 
 
	 public TestBidiBasic(){

	     schema_ = JDNLSTest.COLLECTION;
		 
		 mixed_string =  "ABC \u05D0\u05D1\u05D2 123";
				//"A'' \u05D0\u05D1\u05D2 123";//!!
				//"A'''' \u05D0\u05D1\u05D2 123";
				//"'''' \u05D0\u05D1\u05D2 123";
			 //"XYZ123";
		 table_name = this.schema_ + "." + this.table_; 
			 //"TESTLIB.JT400";
			 //"TESTLIB.\"TABLE_\u05D0\u05D1\u05D2\u05D3\"";
			 
			 //"\"QGPL\".\"TABLE_\u05D2\u05D1\u05D0\"";
				//"\"TABLE_\u05D0\u05D1\u05D2\"";
		 
		 column_name = "WORD";
				//"\"COLUMN_\u05D2\u05D1\u05D0\"";
		 
		 
		try {
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
		} catch (ClassNotFoundException e) {
			errorLog_.append("\nFATAL ERROR: CLASS com.ibm.as400.access.AS400JDBCDriver NOT FOUND");
			System.exit(0);
		}
		 
	 }
	 public void setTable(String table) {
	   this.table_ = table; 
           table_name = this.schema_ + "." + this.table_; 
	   
	 }
	 
    public static void main(String[] args) {
    	        	
        //for(int i=-1;i<12;i++)
        //    CoreTest(i);
            	    	    
    	//Delete();
    	//Insert(4);
    	//Insert(11);
    	
    		TestBidiBasic self = new TestBidiBasic();
    	
    	/*
    	
    	try {
    		self.RecreateTable();
    		self.InsertUpdateTest(4);
    		self.InsertUpdateTest(5);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	*/
    		/*
    	try {
    		self.L2L_test();
    	
    	} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    	
    	try {
			self.Delete();
			self.Insert(null, 4);
	    	self.Insert(null,5);
	    	self.DisplayVisualResult();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	


    	
    	//Insert(4);
    }
    
    public void RecreateTable()  throws SQLException {
    	//RecreateTable(-1);
    	RecreateTable(5);
    }

    public void RecreateTable(int param)  throws SQLException {
    	//errorLog_.append("\nRecreateTable()");
    	Connection conn = getConnection(param);
    	RecreateTable(conn);
    	/*
    	Statement st = conn.createStatement();
    	try{
    		st.executeUpdate("DROP TABLE  "+table_name);
    	} catch(Exception e){
    		errorLog_.append("\nHey, it's first time when you create the table");
    	}
		st.executeUpdate("CREATE TABLE "+table_name+" (I1 int, I2 int, "+column_name+" varchar(50) ccsid " + column_ccsid + ")");
		st.close();
		*/
    	conn.close();
    }
    
    public void RecreateTable(Connection conn)  throws SQLException {
    	//errorLog_.append("\nRecreateTable()");    	
    	Statement st = conn.createStatement();
    	try{
    		st.executeUpdate("DROP TABLE  "+table_name);
    	} catch(SQLException e){
    	  String message = e.getMessage(); 
    	  if (message.indexOf("not found")>= 0) {
    	    
    	  } else { 
    	     errorLog_.append("\nUnable to delete table : "+message);
    	     throw e; 
    	  }
    	}
	st.executeUpdate("CREATE TABLE "+table_name+" (I1 int, I2 int, "+column_name+" varchar(800) ccsid " + column_ccsid + ")");

	st.executeUpdate("GRANT ALL ON "+table_name+" TO PUBLIC");

	st.close();    	
    }
    
    
    public void InsertUpdateTest(int param) throws SQLException, ClassNotFoundException{
    	//Trace.setTraceConversionOn(true);
    	//Trace.setTraceInformationOn(true);
    	//Trace.setTracePCMLOn(true);    	
    	//Trace.setTraceDatastreamOn(true);
    	//Trace.setTraceOn(true);
		
    	//Trace.setTraceErrorOn(true);			
		//Trace.setTraceAllOn(true);						
		//Trace.setTraceOn(true);

    	errorLog_.append("\n******** param = "+param+" ************");
    	Connection conn = getConnection(param);
    	
    	Delete(conn);
    	Insert(conn, param);
    	PreparedInsert(conn, param);
    	CallableInsert(conn, param);    	
    	DisplayVisualResult();
    	
    	conn.close();
    	
    }
    
    public Connection getConnection(int param){    	    	
    	String url_parameters;
    	//if(param >= 0)
    		url_parameters = ";bidi implicit reordering="+metadata_reordering+";package ccsid="+package_ccsid+";bidi string type=" + param;
    	//else
    	//	url_parameters = ";bidi implicit reordering="+metadata_reordering+";package ccsid="+package_ccsid;
//        System.out.println("url parameters = " + url_parameters);
    	return getConnection(url_parameters);    	
    }
    
    public Connection getConnection(String url_parameters){
		Connection db2Conn;
		try {
			db2Conn = DriverManager.getConnection(driverUrlBase+host+url_parameters,username, password);
			//errorLog_.append("\nConnect to "+host);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}			
		return db2Conn;    	
    }
    

    public void SelectTest(int param){
    	SelectTest(param, false);
    } 
    
    public void Delete() throws SQLException{
    	Connection db2Conn = getConnection(5);
    	Delete(db2Conn);
    	db2Conn.close();
    }

    public void Delete(int param) throws SQLException{
    	Connection db2Conn = getConnection(param);
    	Delete(db2Conn);
    	db2Conn.close();
    }

    
    public void Delete(Connection db2Conn){
      	try
    	{
      		//errorLog_.append("\nDelete!!!");
    	    		
    		Statement st = db2Conn.createStatement();
    		st.executeUpdate("DELETE FROM "+table_name);
//    		System.out.println("SQL: " + "DELETE FROM "+table_name);
    		//st.executeUpdate("DELETE FROM "+table_name+" WHERE I1=1");
    		//st.executeUpdate("DELETE FROM "+table_name+" WHERE "+column_name+"='ABC рст'");
    		st.close();    		
    	}
    	catch (SQLException sqle)
    	{
    		sqle.printStackTrace();
    	}
    }

    public void DisplayVisualResult(){
    	boolean visual_output = true;
    	try
    	{
    		//errorLog_.append("\nDisplayResult!!! ");
    	
    		int i = 0,j;
    		String value = "";
    		int param = 4;
    		Connection db2Conn = getConnection(param);
    		
    		errorLog_.append("\n************ visual_output=" + visual_output+" ************");
    		
    		Statement st = db2Conn.createStatement();
    		
    		ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name);//+" WHERE I1=1");
    		//ResultSet resultSet = st.executeQuery("SELECT * FROM "+table_name+" WHERE "+column_name+"='ABC рст'"); 
            java.sql.ResultSetMetaData rsmd = resultSet.getMetaData();
            int col_num = rsmd.getColumnCount();
        	while (resultSet.next()){
        		i++;        		
        		errorLog_.append("#" + i + " ");
        		for(j=0;j<col_num;j++){  
        			//value = resultSet.getString(rsmd.getColumnName(j+1));                 
        			//l = resultSet.getLong(rsmd.getColumnLabel(j+1));                 
        			value = resultSet.getString(j+1);    
        			//dump(value.toCharArray(), 0, value.length());
        			if(visual_output){
        	        	  value = "\u202d" + value + "\u202c";
        	        	  value.replaceAll("\n", "\u202c\n\u202d");
        			}
        			errorLog_.append(value + " \u200E ");                
        		}
        		errorLog_.append("\n");
        	}               
            
            
    		st.close();
    		db2Conn.close();

    	}
    	
    	catch (SQLException sqle)
    	{
    		sqle.printStackTrace();
    	}
    	
    }

    
    public void CallableInsert(Connection db2Conn, int param){//test Insert & Update    	
    	try
    	{
    		//errorLog_.append("\nInsert!!! param="+param);
    		//Connection db2Conn = getConnection(param);
    		
    		//errorLog_.append("\n*** Insert/Update ***\nbidi string type=" + param);
    		
    		//Statement st = db2Conn.createStatement();
    		    		
    		CallableStatement st =  db2Conn.prepareCall("insert into "+table_name+" values(?, ?, ?)");
    		
    		st.setInt(1, 1); 
    		st.setInt(2, 4); 
    		st.setString(3, mixed_string);//"ABC рст"); 
    		st.executeUpdate();
            
    		st.close();
    		//db2Conn.close();

    	}
    	catch (SQLException sqle)
    	{
    		sqle.printStackTrace();
    	}
    }
    

    
    public void PreparedInsert(Connection db2Conn, int param){//test Insert & Update    	
    	try
    	{
    		//errorLog_.append("\nInsert!!! param="+param);
    		
    		//Connection db2Conn = getConnection(param);
    		
    		//errorLog_.append("\n*** Insert/Update ***\nbidi string type=" + param);
    		
    		//Statement st = db2Conn.createStatement();
    		    		
    		PreparedStatement st =  db2Conn.prepareStatement("insert into "+table_name+" values(?, ?, ?)");
    		
    		st.setInt(1, 1); 
    		st.setInt(2, 3); 
    		st.setString(3, mixed_string);//"ABC рст"); 
    		st.executeUpdate();
            
    		st.close();
    		//db2Conn.close();

    	}
    	catch (SQLException sqle)
    	{
    		sqle.printStackTrace();
    	}
    }
    
    
    public void Insert(Connection db2Conn, int param){//test Insert & Update    	
    	try
    	{
    		//errorLog_.append("\nInsert!!! param="+param);
    		
    		if(db2Conn == null)
    			db2Conn = getConnection(param);
    		
    		//errorLog_.append("\n*** Insert/Update ***\nbidi string type=" + param);
    		
    		Statement st = db2Conn.createStatement();
    		
    		//mixed_string
    		    		
    		//st.executeUpdate("insert into "+table_name+" values(1, 1, 'ABC рст')");
    		st.executeUpdate("insert into "+table_name+" values(1, 1, '" + mixed_string + "')");
    		st.executeUpdate("insert into "+table_name+" values(1, 2, 'xxx')");
    		//st.executeUpdate("UPDATE "+table_name+"  SET "+column_name+"='ABC рст' WHERE I2=2");    		    		
    		st.executeUpdate("UPDATE "+table_name+"  SET "+column_name+"='" + mixed_string + "' WHERE I2=2");
            
    		st.close();
    		//db2Conn.close();

    	}
    	catch (SQLException sqle)
    	{
    		sqle.printStackTrace();
    	}
    }
    
    public void SelectTest(int param, boolean visual_output)
    {
       try
       {
    	   errorLog_.append("\nDisplayResult!!! (test select)");
    	   
            Connection db2Conn = getConnection(param);
            
            
    	    //dbmd = db2Conn.getMetaData(); 
    	    //errorLog_.append("\nBefore Get Schemas");
        	//    ResultSet rsSchemas = dbmd.getSchemas();
    	    //errorLog_.append("\nAfter Get Schemas");

            // use a statement to gather data from the database
            Statement st = db2Conn.createStatement();
            //String myQuery = "SELECT * FROM q.staff where id=9"; 
            String myQuery = "SELECT * FROM TESTLIB.SHORT"; 

            // execute the query
            ResultSet resultSet = st.executeQuery(myQuery); 

            // java.sql.ResultSetMetaData rsmd=resultSet.getMetaData();
            // int col_num=rsmd.getColumnCount();
            //errorLog_.append("\nFound columns: "+ col_num + " ");  
            
            //errorLog_.append("\nColumn names");  
//            for(i=0; i<col_num; i++)
//                errorLog_.append(rsmd.getColumnName(i+1) + "\t");           
//            errorLog_.append();
            
            //errorLog_.append("\t\t");
            /*
            errorLog_.append("\nColumn labels");  
            for(i=0; i<col_num; i++)
                errorLog_.append(rsmd.getColumnLabel(i+1) + "\t"); 
            errorLog_.append();   

            errorLog_.append("\nColumn types");  
            for(i=0; i<col_num; i++)
                errorLog_.append(rsmd.getColumnTypeName(i+1) + "\t"); 
            errorLog_.append();   
            */
            
            DisplayResult1(resultSet, visual_output);

            //clean up resources
            resultSet.close();
            st.close();

           db2Conn.close();
       }
       
       catch (SQLException sqle)
       {
           sqle.printStackTrace();
       }
    }
    
    public static void DisplayResult(ResultSet resultSet, boolean visual_output) throws SQLException
    {             
        
        String value;
        
        while (resultSet.next()){
          
          /* entire row
          errorLog_.append("#" + i++ + " ");
          for(j=0;j<col_num;j++){  
             //value = resultSet.getString(rsmd.getColumnName(j+1));                 
             //l = resultSet.getLong(rsmd.getColumnLabel(j+1));                 
             value = resultSet.getString(j+1);
				*/                                
          
          value = resultSet.getString(2);//only one cell
          if(visual_output)
        	  value = "\u202d" + value + "\u202c";
          
          errorLog_.append(value + "\t\u200E\t");                
          //}
          errorLog_.append("\n");
        }               
    }
    
    public static void DisplayResult1(ResultSet resultSet, boolean visual_output) throws SQLException
    {             
    	int j,i = 0;
    	String value;
    	java.sql.ResultSetMetaData rsmd=resultSet.getMetaData();
        int col_num=rsmd.getColumnCount();
    	while (resultSet.next()){

    		i++;
    		if(i!=24)
    			continue;

    		errorLog_.append("#" + i + " ");
    		for(j=0;j<col_num;j++){  
    			//value = resultSet.getString(rsmd.getColumnName(j+1));                 
    			//l = resultSet.getLong(rsmd.getColumnLabel(j+1));                 
    			value = resultSet.getString(j+1);

    			if(visual_output)
    				value = "\u202d" + value + "\u202c";

    			errorLog_.append(value + "\u200E");                
    		}
    		errorLog_.append("\n");
    	}               
    }

    ///////////////
    
    public static void check1(){
    	AS400BidiTransform abt;
    	abt = new AS400BidiTransform(424);
    	String src = "ABC 123 рст";
    	String dst = abt.toAS400Layout(src);
    	errorLog_.append(dst+"\n");

    	// Specifying a new CCSID for an existing AS400BidiTransform object:
    	abt.setAS400Ccsid(62234);                    // 420 RTL //
    	dst = abt.toAS400Layout(src);
    	errorLog_.append(dst+"\n");

    	// Specifying a non-default string type for a given CCSID:
    	abt.setAS400StringType(BidiStringType.ST4);  // Vis LTR //
    	dst = abt.toAS400Layout(src);
    	errorLog_.append(dst+"\n");

    	// Specifying a non-default string type for Java data:
    	abt.setJavaStringType(BidiStringType.ST11);  // Imp Context LTR //
    	dst = abt.toAS400Layout(src);
    	errorLog_.append(dst+"\n");

    	// How to transform i5/OS data to Java layout:
    	abt.setJavaStringType(BidiStringType.ST6);   // Imp RTL //
    	dst = abt.toJavaLayout(src);
    	errorLog_.append(dst+"\n");
    	
    	abt.setJavaStringType(BidiStringType.ST7);   // Vis LTR //
    	dst = abt.toJavaLayout(src);
    	errorLog_.append(dst+"\n");
    	
    }
    /*
    public static void check2(){
    	AS400BidiTransform abt;
    	abt = new AS400BidiTransform(424);
    	String src = "ABC 123 рст";
    	errorLog_.append("\n\u202d"+src+"\n");
    	
    	String dst = abt.toAS400Layout(src);
    	errorLog_.append("\n\u202d"+dst);
    	BidiConversionProperties properties = new BidiConversionProperties();
    	
    	properties.setBidiStringType(5); //Logical LTR
    	abt.setBidiConversionProperties(properties);
    	dst = abt.toJavaLayout(src);
    	errorLog_.append("\n\u202d"+dst);
    	
    	properties.setBidiStringType(4);//Visual LTR
    	abt.setBidiConversionProperties(properties);
    	dst = abt.toJavaLayout(src);
    	errorLog_.append("\n\u202d"+dst);
    	
    }
    */
    static public void dump(char[] buffer, int start, int len){
        if(start<0) start=0;
        int end=start+len;
        end++;
        if(end>buffer.length) end=buffer.length;
        for(int i=start;i<end;i++){
            if(buffer[i]=='\0')
               errorLog_.append("   "+(int)buffer[i]+"="+Integer.toHexString((int)buffer[i]) );//both            
            else
            // Out(" "+buffer[i]+" "+(int)buffer[i]);//decimal
            //Out(" "+buffer[i]+" "+Integer.toHexString(buffer[i]));//hexadecimal
            	errorLog_.append(" "+buffer[i]+" "+(int)buffer[i]+"=0x"+Integer.toHexString((int)buffer[i]) );//both            
        }
        errorLog_.append("\n ");
    }    

    public void L2L_test() throws SQLException{
    	String[] TestString = {    			
    			"ABCD \u05D0\u05D1\u05D2\u05D3",
    			"\u05D0\u05D1\u05D2 ABC"    			
    		};
    	int i = 4, j = 0;
    	Connection db2Conn = getConnection(i);
    	Delete(db2Conn);
    	
    	//Trace.setTraceConversionOn(true);
    	//Trace.setTraceInformationOn(true);
    	//Trace.setTracePCMLOn(true);    	
    	//Trace.setTraceDatastreamOn(true);
    	//Trace.setTraceOn(true);
    	
    	errorLog_.append("\nGBR Simple statement");
    	
		Statement st = db2Conn.createStatement();
		st.executeUpdate("insert into "+table_name+" values("+i+", "+j+", '" 
				+ TestString[j] + "')");
		
		errorLog_.append("\nGBR CallableStatement statement");
		CallableStatement st1 =  db2Conn.prepareCall("insert into "+table_name+" values(?, ?, ?)");
		errorLog_.append("\nGBR : call prepared");
		st1.setInt(1, i); 
		st1.setInt(2, j);
		errorLog_.append("\nGBR : setString");
		st1.setString(3, TestString[j]);
		//st1.setString("x", "Y");
		errorLog_.append("\nGBR : executeUpdate");
		st1.executeUpdate();
		Trace.setTraceOn(false);
		errorLog_.append("\nClose all");
		st.close();
		st1.close();
		db2Conn.close();
		this.DisplayVisualResult();
    }

    public int getHostCCSID(){    		
    	try {
    		AS400 as400 = new AS400(host, username, password);    		 
    		user_profile_ccsid = as400.getCcsid();	
    		return user_profile_ccsid;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return -1;
    }

    public boolean setHostCCSID(int ccsid){
    	try {
    		AS400 as400 = new AS400(host, username, password);
    		User u = new User(as400, username);
    		u.setCCSID(ccsid);	
    		user_profile_ccsid = ccsid;
    		return true;
    	}		
    	catch (Exception e) {
    		//e.printStackTrace();
    		errorLog_.append("\nsetHostCCSID() FAILED: " + e);
    	}
    	return false;    	
    }
    
    public void printHostInfo(){
		AS400 as400 = new AS400(host, username, password);
		errorLog_.append("Host: " + host + " ");
		try {
			errorLog_.append("Version " + as400.getVersion() +" Release " + as400.getRelease() + " VRM " + as400.getVRM());
			errorLog_.append("\n Current CCSID " + as400.getCcsid()+ " Job CCSID " + as400.getJobCCSIDEncoding());
		} catch (AS400SecurityException e) {
			System.out.println(e);			
		} catch (IOException e) {
			System.out.println(e);			
		} catch (InterruptedException e) {
			System.out.println(e);
		}    	
		
    }
}
