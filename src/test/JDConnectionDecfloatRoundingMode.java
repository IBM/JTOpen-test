///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionDecfloatRoundingMode.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

 ////////////////////////////////////////////////////////////////////////
 //
 // File Name:    JDConnectionDecfloatRoundingMode.java
 //
 // Classes:      JDConnectionDecfloatRoundingMode
 //
 ////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import java.util.Properties;


/**
Testcase JDConnectionDecfloatRoundingMode.  This tests the following methods
of the JDBC Connection class:

<ul>
<li>"decfloat rounding mode" property
</ul>
**/
public class JDConnectionDecfloatRoundingMode
extends JDTestcase
{

    // Private data.

    private static  String         table16_ = JDConnectionTest.COLLECTION + ".JDCDFPP16";
    private static  String         table34_ = JDConnectionTest.COLLECTION + ".JDCDFPP34";

    private Connection connection_; 

    
    static String[][] DFP16= {
	{"A",
	"B",
	"value", 
	"round half even", 
	"round half up", 
	"round down", 
	"round ceiling", 
	"round floor", 
	"round half down", 
	"round up"},	
	
	{"5.234567890123455",    /* A */ 
	"5.234567890123400",     /* B */ 
	"10.469135780246855",    /* value */ 
	"10.46913578024686",     /* round half even */ 
	"10.46913578024686",     /* round half up   */ 
	"10.46913578024685",     /* round down      */ 
	"10.46913578024686",     /* round ceiling   */ 
	"10.46913578024685",     /* round floor     */ 
	"10.46913578024685",     /* round half down */ 
	"10.46913578024686"},    /* round up        */ 
  
	{"-5.234567890123455",    /* A */ 
 	 "-5.234567890123400",    /* B */ 
	"-10.469135780246855",    /* value */ 
	"-10.46913578024686",     /* round half even */ 
	"-10.46913578024686",     /* round half up   */ 
	"-10.46913578024685",     /* round down      */ 
	"-10.46913578024685",     /* round ceiling   */ 
	"-10.46913578024686",     /* round floor     */ 
	"-10.46913578024685",     /* round half down */ 
	"-10.46913578024686"},    /* round up        */ 
  
	{"5.234567890123445",    /* A */ 
	"5.234567890123400",     /* B */ 
	"10.469135780246845",    /* value */ 
	"10.46913578024684",     /* round half even */ 
	"10.46913578024685",     /* round half up   */ 
	"10.46913578024684",     /* round down      */ 
	"10.46913578024685",     /* round ceiling   */ 
	"10.46913578024684",     /* round floor     */ 
	"10.46913578024684",     /* round half down */ 
	"10.46913578024685"},    /* round up        */ 

	{"-5.234567890123445",    /* A */ 
	"-5.234567890123400",     /* B */ 
	"-10.469135780246845",    /* value */ 
	"-10.46913578024684",     /* round half even */ 
	"-10.46913578024685",     /* round half up   */ 
	"-10.46913578024684",     /* round down      */ 
	"-10.46913578024684",     /* round ceiling   */ 
	"-10.46913578024685",     /* round floor     */ 
	"-10.46913578024684",     /* round half down */ 
	"-10.46913578024685"},    /* round up        */ 

    }; 


    static String[][] DFP34= {
	{"A",
	"B",
	"value", 
	"round half even", 
	"round half up", 
	"round down", 
	"round ceiling", 
	"round floor", 
	"round half down", 
	"round up"},	
	
	{"5.000000000000000000234567890123455",
	"5.000000000000000000234567890123400",
	"10.000000000000000000469135780246855",
	"10.00000000000000000046913578024686",
	"10.00000000000000000046913578024686",
	"10.00000000000000000046913578024685",
	"10.00000000000000000046913578024686",
	"10.00000000000000000046913578024685",
	"10.00000000000000000046913578024685",
	"10.00000000000000000046913578024686"},
  
	{"-5.000000000000000000234567890123455",
	"-5.000000000000000000234567890123400",
	"-10.000000000000000000469135780246855",
	"-10.00000000000000000046913578024686",
	"-10.00000000000000000046913578024686",
	"-10.00000000000000000046913578024685",
	"-10.00000000000000000046913578024685",
	"-10.00000000000000000046913578024686",
	"-10.00000000000000000046913578024685",
	"-10.00000000000000000046913578024686"},
  
	{"5.000000000000000000234567890123445",
	"5.000000000000000000234567890123400",
	"10.000000000000000000469135780246845",
	"10.00000000000000000046913578024684",
	"10.00000000000000000046913578024685",
	"10.00000000000000000046913578024684",
	"10.00000000000000000046913578024685",
	"10.00000000000000000046913578024684",
	"10.00000000000000000046913578024684",
	"10.00000000000000000046913578024685"},

	{"-5.000000000000000000234567890123445",
	"-5.000000000000000000234567890123400",
	"-10.000000000000000000469135780246845",
	"-10.00000000000000000046913578024684",
	"-10.00000000000000000046913578024685",
	"-10.00000000000000000046913578024684",
	"-10.00000000000000000046913578024684",
	"-10.00000000000000000046913578024685",
	"-10.00000000000000000046913578024684",
	"-10.00000000000000000046913578024685"},

    }; 


/**
Constructor.
**/
    public JDConnectionDecfloatRoundingMode (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDConnectionDecfloatRoundingMode",
            namesAndVars, runMode, fileOutputStream,
            password);
    }


/**
Setup.

@exception Exception If an exception occurs.
**/
    protected void setup ()
      throws Exception
    {
	// Create the table.
	table16_  = JDConnectionTest.COLLECTION + ".JDCDFPP16";
	table34_  = JDConnectionTest.COLLECTION + ".JDCDFPP34";

	connection_  = testDriver_.getConnection (baseURL_ , userId_ , encryptedPassword_ );
	if (getRelease() >=  JDTestDriver.RELEASE_V5R5M0) { 
	    Statement s = connection_.createStatement ();
	    try {
		s.executeUpdate("Drop table "+table16_); 
	    } catch (Exception e) {
	    }
	    try {
		s.executeUpdate("Drop table "+table34_ ); 
	    } catch (Exception e) {
	    } 

	    s.executeUpdate ("CREATE TABLE " + table16_  + " (C1 DECFLOAT(16), C2 DECFLOAT(16))");
	    s.executeUpdate ("CREATE TABLE " + table34_  + " (C1 DECFLOAT(34), C2 DECFLOAT(34))");

	    connection_ .commit();
	    s.close ();
	}
    }



/**
Cleanup.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
      throws Exception
    {
	// Drop the table.
	Statement s = connection_ .createStatement ();
	if (getRelease() >=  JDTestDriver.RELEASE_V5R5M0) { 
	    s.executeUpdate ("DROP TABLE " + table16_ );
	    s.executeUpdate ("DROP TABLE " + table34_ );
	}
	s.close ();
	connection_ .commit();
	connection_ .close ();
    }






    public void testRoundingMode16(String roundingMode)  { 

	if (checkDecFloatSupport()) {

        String roundingModeProp = roundingMode;
        if (isToolboxDriver())
            roundingModeProp = roundingMode.substring(6, roundingMode.length());
        
	    StringBuffer sb = new StringBuffer(); 
	    try {
		boolean success = true; 
		int expectedColumn=0;

		Properties properties = new Properties ();
		properties.put ("user", userId_ );
		properties.put ("password",  PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionDecfloatRounding.p1") );
		properties.put ("decfloat rounding mode", roundingModeProp);
		Connection connection = testDriver_ .getConnection (baseURL_ , properties);

		Statement s = connection.createStatement();
		s.executeUpdate ("DELETE FROM " + table16_ );
		PreparedStatement ps = connection.prepareStatement("INSERT INTO "+table16_ +" VALUES(?,?)");
		for (int i = 1; i < DFP16.length; i++) {
		    ps.setString(1, DFP16[i][0]);
		    ps.setString(2, DFP16[i][1]);
		    ps.executeUpdate();
		}


		for (int i = 0; expectedColumn == 0 && i < DFP16[0].length; i++) {
		    if (DFP16[0][i].equals(roundingMode)) {
			expectedColumn = i; 
		    } 
		} 
		if (expectedColumn == 0) { 
                   expectedColumn = 3; /* default rounding mode -- round half even*/
                }
		sb.append("\nUsing mode '"+DFP16[0][expectedColumn]+"' for test mode '"+roundingMode+"'");
		sb.append("\nQuery is Select C1+C2 from "+table16_); 
		ResultSet rs = s.executeQuery("Select C1+C2 from "+table16_);
		int i = 1; 
		while (rs.next() && i < DFP16.length) {
		    String out = rs.getString(1);
		    if (!DFP16[i][expectedColumn].equals(out)) {
			success=false;
			sb.append("\nExpected "+DFP16[i][expectedColumn]+
				  " but got "+out+" for "+ DFP16[i][2]); 
		    } 
		    i++; 
		} 
		rs.close();
		s.close();
		ps.close(); 
		connection.close ();

		assertCondition(success,sb);

	    } catch(Exception e) {
		failed(e, "Unexpected Exception "+sb.toString());
	    }
	}
    }


    public void testRoundingMode34(String roundingMode)  { 

	if (checkDecFloatSupport()) {
        String roundingModeProp = roundingMode;
        if (isToolboxDriver())
            roundingModeProp = roundingMode.substring(6, roundingMode.length());
	    StringBuffer sb = new StringBuffer(); 
	    try {
		boolean success = true; 
		int expectedColumn=0;

		Properties properties = new Properties ();
		properties.put ("user", userId_ );
		properties.put ("password",  PasswordVault.decryptPasswordLeak(encryptedPassword_, "JDConnectionDecfloatRounding.p2") );
		properties.put ("decfloat rounding mode", roundingModeProp);
		Connection connection = testDriver_ .getConnection (baseURL_ , properties);

		Statement s = connection.createStatement();
		s.executeUpdate ("DELETE FROM " + table34_ );
		PreparedStatement ps = connection.prepareStatement("INSERT INTO "+table34_ +" VALUES(?,?)");
		for (int i = 1; i < DFP34.length; i++) {
		    ps.setString(1, DFP34[i][0]);
		    ps.setString(2, DFP34[i][1]);
		    ps.executeUpdate();
		}
		
		for (int i = 0; expectedColumn == 0 && i < DFP34[0].length; i++) {
		    if (DFP34[0][i].equals(roundingMode)) {
			expectedColumn = i; 
		    } 
		} 
                if (expectedColumn == 0) { 
		   expectedColumn = 3; /* default rounding mode */
                }
                
		sb.append("Using mode '"+DFP34[0][expectedColumn]+"' for test mode '"+roundingMode+"'"); 
		ResultSet rs = s.executeQuery("Select C1+C2 from "+table34_);
		int i = 1; 
		while (rs.next() && i < DFP34.length) {
		    String out = rs.getString(1);
		    if (!DFP34[i][expectedColumn].equals(out)) {
			success=false;
			sb.append("\nExpected "+DFP34[i][expectedColumn]+
				  " but got "+out+" for "+ DFP34[i][2]); 
		    } 
		    i++; 
		} 
		rs.close();
		s.close();
		ps.close(); 
		connection.close ();

		assertCondition(success,sb);

	    } catch(Exception e) {
		failed(e, "Unexpected Exception "+sb.toString());
	    }
	}
    }


 
/**
"decfloat rounding mode" property with DECFLOAT 16  - Specify an invalid value make sure behavior is default behavior
**/
    public void Var001() { testRoundingMode16("invalid"); }

/**
"decfloat rounding mode" property with DECFLOAT 16  - And different rounding modes 
**/
    public void Var002()  { testRoundingMode16("round half even"); }
    public void Var003()  { testRoundingMode16("round half up"); }
    public void Var004()  { testRoundingMode16("round down"); }
    public void Var005()  { testRoundingMode16("round ceiling"); }
    public void Var006()  { testRoundingMode16("round floor"); }
    public void Var007()  { testRoundingMode16("round half down"); }
    public void Var008()  { testRoundingMode16("round up"); } 

/**
"decfloat rounding mode" property with DECFLOAT 34  - Specify an invalid value make sure behavior is default behavior
**/
    public void Var009() { testRoundingMode34("invalid"); }

/**
"decfloat rounding mode" property with DECFLOAT 34  - And different rounding modes 
**/
    public void Var010()  { testRoundingMode34("round half even"); }
    public void Var011()  { testRoundingMode34("round half up"); }
    public void Var012()  { testRoundingMode34("round down"); }
    public void Var013()  { testRoundingMode34("round ceiling"); }
    public void Var014()  { testRoundingMode34("round floor"); }
    public void Var015()  { testRoundingMode34("round half down"); }
    public void Var016()  { testRoundingMode34("round up"); } 


}



