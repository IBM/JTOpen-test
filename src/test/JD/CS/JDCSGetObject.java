///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetObject.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetObject.java
//
// Classes:      JDCSGetObject
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;

import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;
import java.util.Map; 



/**
Testcase JDCSGetObject.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getObject()
<li>getObject() with typeMap arg
</ul>
**/
public class JDCSGetObject
extends JDCSGetTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetObject";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }



 


/**
Constructor.
**/
    public JDCSGetObject (AS400 systemObject,
                          Hashtable<String,Vector<String>> namesAndVars,
                          int runMode,
                          FileOutputStream fileOutputStream,
                          
                          String password)
    {
        super (systemObject, "JDCSGetObject",
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
       super.setup(); 
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        super.cleanup();
    }







/**
getObject() - Get parameter -1.
**/
    public void Var001()
    {
        try {
            csTypes_.execute ();
            Object p = csTypes_.getObject (-1);
            failed ("Didn't throw SQLException but got "+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Get parameter 0.
**/
    public void Var002()
    {
        try {
            Object p = csTypes_.getObject (0);
            failed ("Didn't throw SQLException but got "+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Use a parameter that is too big.
**/
    public void Var003()
    {
        try {
            Object p = csTypes_.getObject (35);
            failed ("Didn't throw SQLException "+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Get a parameter when there are no parameters.
**/
    public void Var004()
    {        
        // I created a whole new Connection object here to work
        // around a server bug.
        Connection c = null;
        try {
            c = testDriver_.getConnection (baseURL_
                                           + ";errors=full", userId_, encryptedPassword_);
            CallableStatement cs = c.prepareCall ("CALL "
                                                  + JDSetupProcedure.STP_CS0);
            cs.execute ();
            Object p = cs.getObject (1);
            failed ("Didn't throw SQLException"+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
  
        if (c != null) try { c.close(); } catch (Exception e) {} 
    }



/**
getObject() - Get a parameter that was not registered.
**/
    public void Var005()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT,supportedFeatures_);
            cs.execute ();
            Object p = cs.getObject (12);
            failed ("Didn't throw SQLException"+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Get a parameter when the statement has not been
executed.
**/
    public void Var006()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT,
							     supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            Object p = cs.getObject (12);
            failed ("Didn't throw SQLException"+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Get a parameter when the statement is closed.
**/
    public void Var007()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            cs.close ();
            Object p = cs.getObject (12);
            failed ("Didn't throw SQLException"+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



/**
getObject() - Get an IN parameter that was correctly registered.
**/
    public void Var008()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            Object p = cs.getObject (12);
            failed ("Didn't throw SQLException"+p);
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


/**
getObject() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
    public void Var009()
    {
        try {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (12, Types.VARCHAR);
            cs.execute ();
            Object p = cs.getObject (12);
            assertCondition (p.equals ("MiJDBC"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }


/**
getObject() with typeMap arg - Get a value with a null type map.
**/
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                Short p = (Short) csTypes_.getObject (1, (Map) null);
                failed ("Didn't throw SQLException"+p);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getObject() with typeMap arg - Get a type with a type map.
**/
    public void Var011()
    {
        try {
            /* We won't test this for now, until the testers
               step up to using JDK 1.2.
               
            Short p = (Short) csTypes_.getObject (1, new HashMap ());
            assertCondition (p.shortValue() == 123);
            */
            notApplicable ();
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a SMALLINT.
              In v5r2, getObject was changed to return an Integer instead of a Short.
**/
    public void Var012()
    {
        try {
            Integer p = (Integer) csTypes_.getObject (1);
            assertCondition (p.intValue() == 123);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an INTEGER.
**/
    public void Var013()
    {
        try {
            Integer p = (Integer) csTypes_.getObject (2);
            assertCondition (p.intValue() == -456);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an REAL.
**/
    public void Var014()
    {
        try {
            Float p = (Float) csTypes_.getObject (3);
            assertCondition (p.floatValue() == 789.54f);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an FLOAT.
**/
    public void Var015()
    {
        try {
            Double p = (Double) csTypes_.getObject (4);
            assertCondition (p.doubleValue() == 253.1027);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an DOUBLE.
**/
    public void Var016()
    {
        try {
            Double p = (Double) csTypes_.getObject (5);
            assertCondition (p.doubleValue() == -987.3434);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an DECIMAL.
**/
    public void Var017()
    {
        try {
            BigDecimal p = (BigDecimal) csTypes_.getObject (7);
            assertCondition (p.doubleValue() == -94732.12345);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an NUMERIC.
**/
    public void Var018()
    {
        try {
            BigDecimal p = (BigDecimal) csTypes_.getObject (9);
            assertCondition (p.doubleValue() == 19.98765);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a CHAR.
**/
    public void Var019()
    {
        try {
            String p = (String) csTypes_.getObject (11);
            assertCondition (p.equals ("Jim                                               "));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a VARCHAR.
**/
    public void Var020()
    {
        try {
            String p = (String) csTypes_.getObject (12);
            assertCondition (p.equals ("Charlie"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a BINARY.
**/
    public void Var021()
    {
        try {
            byte[] p = (byte[]) csTypes_.getObject (13);
            byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r',
                (byte) 'c', (byte) 'h', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' '};     
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            assertCondition (areEqual (p,check));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a VARBINARY.
**/
    public void Var022()
    {
        try {
            byte[] p = (byte[]) csTypes_.getObject (14);
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W',
                (byte) 'a', (byte) 'l', (byte) 'l'};     
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            assertCondition (areEqual (p, check));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a DATE.
**/
    public void Var023()
    {
        try {
            Date p = (Date) csTypes_.getObject (15);
            // The following allows leeway of a day, to account for conversion
            // anomalies by the JVM.
            assertCondition (p.getTime () - 892616400000L < 86400000);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a TIME.
**/
    public void Var024()
    {
        try {
            Time p = (Time) csTypes_.getObject (16);
            assertCondition (p.toString ().equals ("08:42:30"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var025()
    {
        try {
            Timestamp p = (Timestamp) csTypes_.getObject (17);
            assertCondition(p.toString().equals("2001-11-18 13:42:22.123456"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
getObject() - Get a type that was registered as an OTHER.
**/
    public void Var026()
    {
       if (checkLobSupport ()) {
        try {
            Object p = csTypes_.getObject (18);
            assertCondition (p.equals ("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
       }
    }



/**
getObject() - Get a type that was registered as a BLOB.
**/
    public void Var027()
    {
        if (checkLobSupport ()) {
            try {
               Object p = csTypes_.getObject(19);
               if (isJdbc20()) 
                  assertCondition (JDCSGetBlob.compare((Blob) p, JDCSGetBlob.expected));
               else
                  assertCondition(areEqual ((byte[]) p, JDCSGetBlob.expected));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get a type that was registered as a CLOB.
**/
    public void Var028()
    {
        if (checkLobSupport ()) {
            try {
                Object p = csTypes_.getObject(20);
                if (isJdbc20()) 
                   assertCondition (compare ((Clob)p, "Chris Smyth"));
                else
                   assertCondition (((String) p).equals ("Chris Smyth"));

            }
            catch (Exception e) {
               failed (e, "Unexpected Exception");
            }
        }
    }



/**
getObject() - Get a type that was registered as a BIGINT.
**/
    public void Var029()
    {
        if (checkBigintSupport()) {
            try {
                Long p = (Long) csTypes_.getObject (22);
                assertCondition (p.longValue() == 987662234567l);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
    }


/**
getObject() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
    public void Var030()
    {
         try {
             CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                              JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
             JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                        supportedFeatures_, getDriver());
             cs.registerOutParameter (12, Types.VARCHAR);
             JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                  supportedFeatures_);
             cs.execute ();
             Object p = cs.getObject (12);
             assertCondition (p.equals ("MiJDBC"));
         }
         catch (Exception e) {
             failed (e, "Unexpected Exception");
         }
    }


/**
 * Common code used for getObject tests
 * Added for J2EE CTS 
**/

    public void getNullObject(int variation, int registerType) {

      try {
	  String databaseType = "NUMERIC"; 
	  Statement stmt = connection_.createStatement();
	  
	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Proc"); 
	  } catch (Exception e) {}

	  stmt.executeUpdate("create table "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Tab( NULL_VAL "+databaseType+" )"); 
	  stmt.executeUpdate("create procedure "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Proc ( out NULL_PARAM "+databaseType+") LANGUAGE SQL begin select NULL_VAL  into NULL_PARAM from "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Tab; end");

	  stmt.executeUpdate("insert into "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Tab values(null)");
	  CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Proc(?)}");

		//register the output parameters
	  cstmt.registerOutParameter(1,registerType);

		//execute the procedure
	  cstmt.execute();

		//invoke getByte method
	  Object xRetVal = cstmt.getObject(1);
	  boolean condition =  (xRetVal == null); 
	  if (!condition) {
	      System.out.println("xRetVal ("+xRetVal+") should have been null");
	  }

	  assertCondition(condition); 

	  try {
	      stmt.executeUpdate("drop table "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_tab");
	  } catch (Exception e) {}
	  try {
	      stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".JDCSGOBJ"+variation+"_Proc"); 
	  } catch (Exception e) {}

      } catch (Exception e) {
         failed (e, "Unexpected Exception");
      }
    }

// Named Parameter
/**
getObject() - Get a type that was registered as a SMALLINT.
              In v5r2, getObject was changed to return an Integer instead of a Short.
**/
    public void Var031()
    {
	if(checkNamedParametersSupport())  {
	    try {
		Integer p = (Integer) csTypes_.getObject ("P_SMALLINT");
		assertCondition (p.intValue() == 123);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }

/**
getObject() - Get a type that was registered as an INTEGER.
**/
    public void Var032()
    {
	if(checkNamedParametersSupport()) {
	    try {
		Integer p = (Integer) csTypes_.getObject ("P_INTEGER");
		assertCondition (p.intValue() == -456);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an REAL.
**/
    public void Var033()
    {
	if(checkNamedParametersSupport()){
	    try {
		Float p = (Float) csTypes_.getObject ("P_REAL");
		assertCondition (p.floatValue() == 789.54f);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an FLOAT.
**/
    public void Var034()
    {
	if(checkNamedParametersSupport()) {
	    try {
		Double p = (Double) csTypes_.getObject ("P_FLOAT");
		assertCondition (p.doubleValue() == 253.1027);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an DOUBLE.
**/
    public void Var035()
    {
	if(checkNamedParametersSupport()) {
	    try {
		Double p = (Double) csTypes_.getObject ("P_DOUBLE");
		assertCondition (p.doubleValue() == -987.3434);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an DECIMAL.
**/
    public void Var036()
    {
	if(checkNamedParametersSupport()) {
	    try {
		BigDecimal p = (BigDecimal) csTypes_.getObject ("P_DECIMAL_105");
		  assertCondition (p.doubleValue() == -94732.12345);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an NUMERIC.
**/
    public void Var037()
    {
	if(checkNamedParametersSupport()) {
	    try {
		BigDecimal p = (BigDecimal) csTypes_.getObject ("P_NUMERIC_105");
		assertCondition (p.doubleValue() == 19.98765);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a CHAR.
**/
    public void Var038()
    {
	if(checkNamedParametersSupport()) {	
	    try {
		String p = (String) csTypes_.getObject ("P_CHAR_50");
		assertCondition (p.equals ("Jim                                               "));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a VARCHAR.
**/
    public void Var039()
    {
	if(checkNamedParametersSupport()) {
	    try {
		String p = (String) csTypes_.getObject ("P_VARCHAR_50");
		assertCondition (p.equals ("Charlie"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a BINARY.
**/
    public void Var040()
    {
	if(checkNamedParametersSupport()) {
	    try {
		byte[] p = (byte[]) csTypes_.getObject ("P_BINARY_20");
		byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r',
		(byte) 'c', (byte) 'h', (byte) ' ',
		(byte) ' ', (byte) ' ', (byte) ' ',     
		(byte) ' ', (byte) ' ', (byte) ' ',     
		(byte) ' ', (byte) ' ', (byte) ' ',     
		(byte) ' ', (byte) ' ', (byte) ' ',     
		(byte) ' ', (byte) ' '};     
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            assertCondition (areEqual (p,check));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a VARBINARY.
**/
    public void Var041()
    {
	if(checkNamedParametersSupport()) {
	    try {
		byte[] p = (byte[]) csTypes_.getObject ("P_VARBINARY_20");
		byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
		(byte) 'e', (byte) ' ', (byte) 'W',
		(byte) 'a', (byte) 'l', (byte) 'l'};     
	    // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
	    // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
		assertCondition (areEqual (p, check));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }	
	}
    }


/**
getObject() - Get a type that was registered as a DATE.
**/
    public void Var042()
    {
	if(checkNamedParametersSupport()) {	
	    try {
		Date p = (Date) csTypes_.getObject ("P_DATE");
	    // The following allows leeway of a day, to account for conversion
	    // anomalies by the JVM.
		assertCondition (p.getTime () - 892616400000L < 86400000);
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a TIME.
**/
    public void Var043()
    {
	if(checkNamedParametersSupport()) {
	    try {
		Time p = (Time) csTypes_.getObject ("P_TIME");
		assertCondition (p.toString ().equals ("08:42:30"));
	    }
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var044()
    {
	if(checkNamedParametersSupport()) {
	    try {	
		Timestamp p = (Timestamp) csTypes_.getObject ("P_TIMESTAMP");
		assertCondition(p.toString().equals("2001-11-18 13:42:22.123456"));
	    }	
	    catch (Exception e) {
		failed (e, "Unexpected Exception");
	    }
	}
    }


/**
getObject() - Get a type that was registered as an OTHER.
**/
    public void Var045()
    {
	if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try{
		    Object p = csTypes_.getObject ("P_DATALINK");
		    assertCondition (p.equals ("https://github.com/IBM/JTOpen-test/blob/main/README.md"));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
getObject() - Get a type that was registered as a BLOB.
**/
    public void Var046()
    {
	if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    Object p = csTypes_.getObject("P_BLOB");
		    if (isJdbc20()) 
			assertCondition (JDCSGetBlob.compare((Blob) p, JDCSGetBlob.expected));
		    else
			assertCondition(areEqual ((byte[]) p, JDCSGetBlob.expected));
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
getObject() - Get a type that was registered as a CLOB.
**/
    public void Var047()
    {
	if(checkNamedParametersSupport()) {
	    if (checkLobSupport ()) {
		try {
		    Object p = csTypes_.getObject("P_CLOB");
		    if (isJdbc20()) 
			assertCondition (compare ((Clob)p, "Chris Smyth"));
		    else
			assertCondition (((String) p).equals ("Chris Smyth"));

		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }


/**
getObject() - Get a type that was registered as a BIGINT.
**/
    public void Var048()
    {
	if(checkNamedParametersSupport()) {
	    if (checkBigintSupport()) {
		try {
		    Long p = (Long) csTypes_.getObject ("P_BIGINT");
		    assertCondition (p.longValue() == 987662234567l);
		}
		catch (Exception e) {
		    failed (e, "Unexpected Exception");
		}
	    }
	}
    }
/**
 * getObject()  return an OUTPUT null parameter for type SMALLINT return null
 */
    public void Var049() {
	getNullObject(31, Types.SMALLINT);
    }


/**
 * getObject()  return an OUTPUT null parameter for type INTEGER return null
 */
    public void Var050() {
	getNullObject(32, Types.INTEGER);
    }

/**
 * getObject()  return an OUTPUT null parameter for type BIGINT return null
 */
    public void Var051() {
	getNullObject(33, Types.BIGINT);
    }

/**
 * getObject()  return an OUTPUT null parameter for type FLOAT return null
 */
    public void Var052() {
	getNullObject(34, Types.FLOAT);
    }

/**
 * getObject()  return an OUTPUT null parameter for type REAL return null
 */
    public void Var053() {
	getNullObject(35, Types.REAL);
    }

/**
 * getObject()  return an OUTPUT null parameter for type DOUBLE return null
 */
    public void Var054() {
	getNullObject(36, Types.DOUBLE);
    }

/**
 * getObject() return the return value for the form ?=CALL(?...?)
 */ 
    public void Var055()  {
	String added = " -- added 07/11/2007 by native driver for testing ?=CALL(?,?,?) "; 
	try {
	    // STP_CSPARMSRV defined as
	    // CREATE PROCEDURE ...  (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)
	    // LANGUAGE SQL JDCSPRVX: BEGIN
	    // SET P2 = P1 + 1;
	    // SET P3 = P3 + 1;
	    // RETURN P1;
            // END JDCSPRVX

	    CallableStatement cs = connection_.prepareCall("?=CALL " + JDSetupProcedure.STP_CSPARMSRV + "(?,?,?)");

	    cs.registerOutParameter(1, Types.INTEGER);
	    cs.registerOutParameter(3, Types.INTEGER);
	    cs.registerOutParameter(4, Types.INTEGER);
	    cs.setInt(2,100);
	    cs.setInt(4,10);
	    cs.execute();
	    String o1 = ""+cs.getObject(1);
	    String o3 = ""+cs.getObject(3);
	    String o4 = ""+cs.getObject(4);
	    assertCondition(o1.equals("100") &&
			    o3.equals("101") &&
			    o4.equals("11"),
			    "\n"+
			    "o1="+o1+" sb 100\n"+
			    "o3="+o3+" sb 101\n"+
			    "o4="+o4+" sb 11\n"+ added); 
	    

	} catch (Exception e) {
	    failed(e, added); 
	} 
    } 


/**
 * getObject() return a string output parameter from ?=CALL(?)
 */ 
    public void Var056()  {
	String added = " -- added 07/11/2007 by native driver for testing ?=CALL(?) ";
        String step=""; 
	try {
	    String procedureName = JDCSTest.COLLECTION+".JDCSGOBJ56_Proc";
	    Statement stmt = connection_.createStatement();

	    try {
		stmt.executeUpdate("DROP PROCEDURE "+procedureName);
	    } catch (Exception e) {
	    }
            step = "CREATE PROCEDURE "+procedureName+" (OUT P1 VARCHAR(80)) LANGUAGE SQL JDCSGO55: BEGIN  SET P1='OK'; RETURN 7; END JDCSGO55";

	    stmt.executeUpdate(step); 
	    
            step="?=CALL " + procedureName + "(?)"; 
	    CallableStatement cs = connection_.prepareCall(step);
	    step="registerOut(1, Types.INTEGER)"; 
	    cs.registerOutParameter(1, Types.INTEGER);
            step="registerOut(2,Types.VARCHAR)";
	    cs.registerOutParameter(2, Types.VARCHAR);
            step="cs.execute";     
	    cs.execute();
            step="cs.getObject(2)";
	    String o2 = ""+cs.getObject(2);
	    // System.out.println("o2="+o2); 
            step="cs.getObject(1)";
	    String o1 = ""+cs.getObject(1);
	    // System.out.println("o1="+o1);
	    
            step="DROP PROCEDURE "+procedureName;
	    stmt.executeUpdate(step);
	    assertCondition(o1.equals("7") &&
			    o2.equals("OK"),
			    "\n"+
			    "o1="+o1+" sb 7\n"+
			    "o2="+o2+" sb OK\n"+ added); 
	    

	} catch (Exception e) {
	    failed(e, "Exception at step \""+step+"\"" + added); 
	} 
    } 

    /**
     * getObject () - Get a type that was registered as a BOOLEAN.
     **/
    public void Var057() {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypes_, "getObject", 25, "true",info);
      }
    }


    /**
     * getObject() - Get a type that was registered as a BOOLEAN that returns
     * false.
     **/
    public void Var058() {
      if (checkBooleanSupport()) { 
        String info = " -- Test added 2021/01/06 for boolean";
        getTestSuccessful(csTypesB_, "getObject", 25, "false", info);
      }
    }
    
    /**
     * getObject() - Get a type that was registered as a BOOLEAN that returns
     * null.
     **/
    public void Var059() {
      if (checkBooleanSupport()) {
        String info = " -- Test added 2021/01/06 for boolean";
             getTestSuccessful(csTypesNull_, "getObject", 25, "null",info);
      }
    }

    


}
