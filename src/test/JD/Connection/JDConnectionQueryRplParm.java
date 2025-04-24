///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDConnectionQueryRplParm.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDConnectionQueryRplParm.java
//
// Classes:      JDConnectionQueryRplParm
//
////////////////////////////////////////////////////////////////////////

package test.JD.Connection;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;

import test.JDConnectionTest;
import test.JDTestcase;
import test.JD.JDSetupCollection;


/**
Testcase JDConnectionQueryReplaceTruncatedParameter.
This tests the "query replace truncated parameter" property.
**/
public class JDConnectionQueryRplParm
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDConnectionQueryRplParm";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDConnectionTest.main(newArgs); 
   }

    Connection normalConnection = null;
    Connection replaceConnection = null;
    String collection = "JSCQRPLF"; 

	String iniLibrary = null; 

	/**
	Constructor.
	**/
	public JDConnectionQueryRplParm (AS400 systemObject,
								  Hashtable<String,Vector<String>> namesAndVars,
								  int runMode,
								  FileOutputStream fileOutputStream,
								  
								  String password,
                                                                  String pwrUserId,
                                                                  String pwrPwd)                //@C1C
	{
		super (systemObject, "JDConnectionQueryRplParm",
			   namesAndVars, runMode, fileOutputStream,
			   password);
	}



	/**
	Performs setup needed before running variations.

	@exception Exception If an exception occurs.
	**/
	protected void setup ()
	throws Exception
	{
	    collection =  JDConnectionTest.COLLECTION; 
	    normalConnection = testDriver_.getConnection (baseURL_, userId_, encryptedPassword_);
	    replaceConnection = testDriver_.getConnection (baseURL_+";query replace truncated parameter=@@@@@@@@", userId_, encryptedPassword_);

	    JDSetupCollection.create (systemObject_,  normalConnection,  collection); 
	}

	protected void cleanup () {
	    try {
		normalConnection.close();
		replaceConnection.close(); 
	    } catch (Exception e) {
		System.out.println("Exception during cleanup");
		e.printStackTrace(System.out); 
	    } 
	} 
	

	/* Check a string based data type */ 
	public void testStringSet(int varNumber,
				 String dataTypeDefinition,
				 String insertedValue,
				 String queryValue,
				 String replaceValue, 
				 int expectedNormalCount, 
				 int  expectedReplaceCount) {

	    StringBuffer sb = new StringBuffer("Testing type "+dataTypeDefinition+"\n"); 
		String sql = null; 
		try {
		boolean passed = true;

		Statement stmt = normalConnection.createStatement();
		ResultSet rs = null; 
			String tablename = collection+".JDCQRP"+varNumber;
		try {
		    sql = "DROP TABLE "+tablename;
		    sb.append("Executing "+sql+"\n"); 
		    stmt.executeUpdate(sql);

		} catch (Exception e) {
		    sb.append("Ignored exception on "+sql+"\n"); 
		}
		sql = "CREATE TABLE "+tablename+" (C1 "+dataTypeDefinition+")";
		sb.append("Executing "+sql+"\n"); 
		stmt.executeUpdate(sql); 

		sql = "INSERT INTO "+tablename+" VALUES(?)";
		sb.append("Executing "+sql+"\n"); 
		PreparedStatement ps = normalConnection.prepareStatement(sql);
		sb.append("Inserting '"+insertedValue+"'\n"); 
		ps.setString(1, insertedValue);
		ps.executeUpdate();
		ps.close();
		ps = null; 

		sql = "SELECT * from "+tablename+" WHERE C1=?";
		sb.append("preparing "+sql+"\n"); 
		PreparedStatement normalPrepare = normalConnection.prepareStatement(sql);
		sb.append("Setting parameter '"+queryValue+"'\n"); 
		normalPrepare.setString(1,queryValue);
		SQLWarning prepareWarning = normalPrepare.getWarnings();
		rs = normalPrepare.executeQuery();
		int normalCount = 0; 
		while(rs.next()) {
		    normalCount++; 
		}
		sb.append("Normal count is "+normalCount+"\n"); 
		if (normalCount == expectedNormalCount) {
		    sb.append("normalCount matches\n"); 
		} else {
		    sb.append("ERROR:  expected count "+expectedNormalCount+" got "+normalCount+" from normal query\n");
		    if (prepareWarning == null) {
			sb.append("... No warnings\n");
		    } else {
			sb.append("...Warning\n"); 
			printStackTraceToStringBuffer(prepareWarning, sb); 
		    } 
		    passed = false; 
		} 
		rs.close();

		if (replaceConnection != null) {
		    replaceConnection.close();
		    replaceConnection = null; 
		}
		String replaceUrl = baseURL_+";query replace truncated parameter="+replaceValue;
		sb.append("Connecting using '"+replaceUrl+"'\n"); 
		replaceConnection = testDriver_.getConnection (replaceUrl, userId_, encryptedPassword_);


		sb.append("replaceConnection: preparing "+sql+"\n"); 
		PreparedStatement replacePrepare = replaceConnection.prepareStatement(sql);
		sb.append("Setting parameter '"+queryValue+"'\n"); 
		replacePrepare.setString(1,queryValue);
		SQLWarning replacePrepareWarning = replacePrepare.getWarnings(); 
		rs = replacePrepare.executeQuery();
		int replaceCount = 0;
		while(rs.next()) {
		    replaceCount ++;
		}
		sb.append("replaceCount="+replaceCount+"\n"); 
		if (replaceCount != expectedReplaceCount) {
		    sb.append("ERROR:  got count="+replaceCount+" sb "+expectedReplaceCount+" from replace query\n");
		    if (replacePrepareWarning == null) {
			sb.append("... No warnings\n");
		    } else {
			sb.append("...Warning\n"); 
			printStackTraceToStringBuffer(replacePrepareWarning, sb); 
		    } 

		    passed = false; 
		} else {
		    sb.append("replaceCount matches\n");
		} 
		rs.close();
		replacePrepare.close();
		normalPrepare.close(); 

		assertCondition(passed,sb); 

	    }  catch (Exception e) {
		failed(e, "Unexpected exception sql="+sql+"\n"+sb.toString());
	    }
	}


  /* CHAR: test where truncation returns a value with normal connection */
	/* but does not return a value with the property set */ 
	public void Var001()
	{
	    testStringSet(1, /* varNumber */ 
			  "CHAR(3)", /*datatypeDefinition */ 
			  "ABC",     /*insertedValue*/ 
			  "ABCD",    /*queryValue*/
			  "@@@@@@@@",          /*replaceValue*/
			  1,	/* expectedNormalCount */ 
			  0);   /* expectedReplaceCount */ 
	}

	/* CHAR: make sure replace value not used for no truncation */ 
	 public void Var002()
	  {
	      testStringSet(2, /* varNumber */ 
	        "CHAR(3)", /*datatypeDefinition */ 
	        "ABC",     /*insertedValue*/ 
	        "ABC",    /*queryValue*/
	        "@@@@@@@@",          /*replaceValue*/
	        1,  /* expectedNormalCount */ 
	        1);   /* expectedReplaceCount */ 
	  }

	  /* CHAR: make sure replace value not used for no truncation */ 
   public void Var003()
   {
       testStringSet(3, /* varNumber */ 
         "CHAR(3)", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* CHAR: Make sure replace value used */ 
   public void Var004()
   {
       testStringSet(4, /* varNumber */ 
         "CHAR(3)", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* CHAR: make sure replace value is actually used  */ 
  public void Var005()
  {
      testStringSet(5, /* varNumber */ 
        "CHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* CHAR: Make sure replace value used */ 
  public void Var006()
  {
      testStringSet(6, /* varNumber */ 
        "CHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  
  /* VARCHAR: test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var007()
  {
      testStringSet(7, /* varNumber */ 
        "VARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* VARCHAR: make sure replace value not used for no truncation */ 
   public void Var008()
    {
        testStringSet(8, /* varNumber */ 
          "VARCHAR(3)", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* VARCHAR: make sure replace value not used for no truncation */ 
   public void Var009()
   {
       testStringSet(9, /* varNumber */ 
         "VARCHAR(3)", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARCHAR: Make sure replace value used */ 
   public void Var010()
   {
       testStringSet(10, /* varNumber */ 
         "VARCHAR(3)", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARCHAR: make sure replace value is actually used  */ 
  public void Var011()
  {
      testStringSet(11, /* varNumber */ 
        "VARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* VARCHAR: Make sure replace value used */ 
  public void Var012()
  {
      testStringSet(12, /* varNumber */ 
        "VARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  
  
  /* CLOB: test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var013()
  {
      testStringSet(13, /* varNumber */ 
        "CLOB(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* CLOB: make sure replace value not used for no truncation */ 
   public void Var014()
    {
        testStringSet(14, /* varNumber */ 
          "CLOB(3)", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* CLOB: make sure replace value not used for no truncation */ 
   public void Var015()
   {
       testStringSet(15, /* varNumber */ 
         "CLOB(3)", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* CLOB: Make sure replace value used */ 
   public void Var016()
   {
       testStringSet(16, /* varNumber */ 
         "CLOB(3)", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* CLOB: make sure replace value is actually used  */ 
  public void Var017()
  {
      testStringSet(17, /* varNumber */ 
        "CLOB(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* CLOB: Make sure replace value used */ 
  public void Var018()
  {
      testStringSet(18, /* varNumber */ 
        "CLOB(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

    
  /* VARGRAPHIC : test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var019()
  {
      testStringSet(19, /* varNumber */ 
        "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* VARGRAPHIC : make sure replace value not used for no truncation */ 
   public void Var020()
    {
        testStringSet(20, /* varNumber */ 
          "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* VARGRAPHIC : make sure replace value not used for no truncation */ 
   public void Var021()
   {
       testStringSet(21, /* varNumber */ 
         "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARGRAPHIC : Make sure replace value used */ 
   public void Var022()
   {
       testStringSet(22, /* varNumber */ 
         "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARGRAPHIC : make sure replace value is actually used  */ 
  public void Var023()
  {
      testStringSet(23, /* varNumber */ 
        "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* VARGRAPHIC : Make sure replace value used */ 
  public void Var024()
  {
      testStringSet(24, /* varNumber */ 
        "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

    
  

  
  /* GRAPHIC : test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var025()
  {
      testStringSet(19, /* varNumber */ 
        "GRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* GRAPHIC : make sure replace value not used for no truncation */ 
   public void Var026()
    {
        testStringSet(26, /* varNumber */ 
          "GRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* GRAPHIC : make sure replace value not used for no truncation */ 
   public void Var027()
   {
       testStringSet(27, /* varNumber */ 
         "GRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* GRAPHIC : Make sure replace value used */ 
   public void Var028()
   {
       testStringSet(28, /* varNumber */ 
         "GRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* GRAPHIC : make sure replace value is actually used  */ 
  public void Var029()
  {
      testStringSet(29, /* varNumber */ 
        "GRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* GRAPHIC : Make sure replace value used */ 
  public void Var030()
  {
      testStringSet(30, /* varNumber */ 
        "GRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }
  

  
  /* VARGRAPHIC : test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var031()
  {
      testStringSet(31, /* varNumber */ 
        "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* VARGRAPHIC : make sure replace value not used for no truncation */ 
   public void Var032()
    {
        testStringSet(32, /* varNumber */ 
          "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* VARGRAPHIC : make sure replace value not used for no truncation */ 
   public void Var033()
   {
       testStringSet(33, /* varNumber */ 
         "VARGRAPHIC (3) CCSID 1200", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARGRAPHIC : Make sure replace value used */ 
   public void Var034()
   {
       testStringSet(34, /* varNumber */ 
         "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* VARGRAPHIC : make sure replace value is actually used  */ 
  public void Var035()
  {
      testStringSet(35, /* varNumber */ 
        "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* VARGRAPHIC : Make sure replace value used */ 
  public void Var036()
  {
      testStringSet(36, /* varNumber */ 
        "VARGRAPHIC (3)  CCSID 1200", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  
  /* NCHAR: test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var037()
  {
      testStringSet(37, /* varNumber */ 
        "NCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* NCHAR: make sure replace value not used for no truncation */ 
   public void Var038()
    {
        testStringSet(38, /* varNumber */ 
          "NCHAR(3)", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* NCHAR: make sure replace value not used for no truncation */ 
   public void Var039()
   {
       testStringSet(39, /* varNumber */ 
         "NCHAR(3)", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* NCHAR: Make sure replace value used */ 
   public void Var040()
   {
       testStringSet(40, /* varNumber */ 
         "NCHAR(3)", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* NCHAR: make sure replace value is actually used  */ 
  public void Var041()
  {
      testStringSet(41, /* varNumber */ 
        "NCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* NCHAR: Make sure replace value used */ 
  public void Var042()
  {
      testStringSet(42, /* varNumber */ 
        "NCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  
  /* NVARCHAR: test where truncation returns a value with normal connection */
  /* but does not return a value with the property set */ 
  public void Var043()
  {
      testStringSet(43, /* varNumber */ 
        "NVARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABCD",    /*queryValue*/
        "@@@@@@@@",          /*replaceValue*/
        1,  /* expectedNormalCount */ 
        0);   /* expectedReplaceCount */ 
  }

  /* NVARCHAR: make sure replace value not used for no truncation */ 
   public void Var044()
    {
        testStringSet(44, /* varNumber */ 
          "NVARCHAR(3)", /*datatypeDefinition */ 
          "ABC",     /*insertedValue*/ 
          "ABC",    /*queryValue*/
          "@@@@@@@@",          /*replaceValue*/
          1,  /* expectedNormalCount */ 
          1);   /* expectedReplaceCount */ 
    }

    /* NVARCHAR: make sure replace value not used for no truncation */ 
   public void Var045()
   {
       testStringSet(45, /* varNumber */ 
         "NVARCHAR(3)", /*datatypeDefinition */ 
         "AB",     /*insertedValue*/ 
         "AB",    /*queryValue*/
         "@@@@@@@@",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* NVARCHAR: Make sure replace value used */ 
   public void Var046()
   {
       testStringSet(46, /* varNumber */ 
         "NVARCHAR(3)", /*datatypeDefinition */ 
         "ABC",     /*insertedValue*/ 
         "ABCD",    /*queryValue*/
         "ABC",          /*replaceValue*/
         1,  /* expectedNormalCount */ 
         1);   /* expectedReplaceCount */ 
   }

   /* NVARCHAR: make sure replace value is actually used  */ 
  public void Var047()
  {
      testStringSet(47, /* varNumber */ 
        "NVARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXX",    /*queryValue*/
        "ABC",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  /* NVARCHAR: Make sure replace value used */ 
  public void Var048()
  {
      testStringSet(48, /* varNumber */ 
        "NVARCHAR(3)", /*datatypeDefinition */ 
        "ABC",     /*insertedValue*/ 
        "ABXXXX",    /*queryValue*/
        "ABCDEFG",          /*replaceValue*/
        0,  /* expectedNormalCount */ 
        1);   /* expectedReplaceCount */ 
  }

  

  
}












