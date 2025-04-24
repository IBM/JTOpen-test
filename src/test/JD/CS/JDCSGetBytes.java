///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetBytes.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetBytes.java
//
// Classes:      JDCSGetBytes
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDCSTest;
import test.JDSetupProcedure;
import test.JDTestDriver;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDCSGetBytes.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getBytes()
</ul>
**/
public class JDCSGetBytes
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetBytes";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }





    /**
    Constructor.
    **/
    public JDCSGetBytes (AS400 systemObject,
                         Hashtable<String,Vector<String>> namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
    {
        super (systemObject, "JDCSGetBytes",
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
    getBytes() - Get parameter -1.
    **/
    public void Var001()
    {
        try
        {
            csTypes_.execute ();
            byte[] p = csTypes_.getBytes (-1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get parameter 0.
    **/
    public void Var002()
    {
        try
        {
            byte[] p = csTypes_.getBytes (0);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Use a parameter that is too big.
    **/
    public void Var003()
    {
        try
        {
            byte[] p = csTypes_.getBytes (35);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a parameter when there are no parameters.
    **/
    public void Var004()
    {
        // I created a whole new Connection object here to work
        // around a server bug.
        Connection c = null;
        try
        {
            c = testDriver_.getConnection (baseURL_ + ";errors=full", userId_, encryptedPassword_);
            CallableStatement cs = c.prepareCall ("CALL " + JDSetupProcedure.STP_CS0);
            cs.execute ();
            byte[] p = cs.getBytes (1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }

        if(c != null) try
            {
                c.close();
            }
            catch(Exception e)
            {
            }
    }



    /**
    getBytes() - Get a parameter that was not registered.
    **/
    public void Var005()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.execute ();
            byte[] p = cs.getBytes (12);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a parameter when the statement has not been
    executed.
    **/
    public void Var006()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (14, Types.VARBINARY);
            byte[] p = cs.getBytes (14);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a parameter when the statement is closed.
    **/
    public void Var007()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
            cs.registerOutParameter (14, Types.VARBINARY);
            cs.execute ();
            cs.close ();
            byte[] p = cs.getBytes (14);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get an IN parameter that was correctly registered.
    **/
    public void Var008()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                 supportedFeatures_);
            cs.registerOutParameter (14, Types.VARBINARY);
            cs.execute ();
            byte[] p = cs.getBytes (14);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }


    /**
    getBytes() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter.
    **/
    public void Var009()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (14, Types.VARBINARY);
            cs.execute ();
            byte[] p = cs.getBytes (14);
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W',
                (byte) 'a', (byte) 'l', (byte) 'l',
                (byte) 'B', (byte) 'o', (byte) 'n',
                (byte) 'j', (byte) 'o', (byte) 'u',
                (byte) 'r'};
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            assertCondition(passed,sb); 
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getBytes() - Get a type that was registered as a SMALLINT.
    **/
    public void Var010()
    {
        try
        {
            byte[] p = csTypes_.getBytes (1);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an INTEGER.
    **/
    public void Var011()
    {
        try
        {
            byte[] p = csTypes_.getBytes (2);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an REAL.
    **/
    public void Var012()
    {
        try
        {
            byte[] p = csTypes_.getBytes (3);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an FLOAT.
    **/
    public void Var013()
    {
        try
        {
            byte[] p = csTypes_.getBytes (4);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an DOUBLE.
    **/
    public void Var014()
    {
        try
        {
            byte[] p = csTypes_.getBytes (5);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an DECIMAL.
    **/
    public void Var015()
    {
        try
        {
            byte[] p = csTypes_.getBytes (6);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an NUMERIC.
    **/
    public void Var016()
    {
        try
        {
            byte[] p = csTypes_.getBytes (8);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as a CHAR.
    **/
    public void Var017()
    {
        try
        {
            byte[] p = csTypes_.getBytes (11);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as a VARCHAR.
    **/
    public void Var018()
    {
        try
        {
            byte[] p = csTypes_.getBytes (12);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as a BINARY.
    **/
    public void Var019()
    {
        try
        {
            byte[] p = csTypes_.getBytes (13);
            byte[] check = new byte[] {
		(byte) 'M', (byte) 'u', (byte) 'r',
                (byte) 'c', (byte) 'h', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' '};     
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            assertCondition(passed,sb); 
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBytes() - Get a type that was registered as a BINARY, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var020()
    {
        try
        {
            csTypes_.setMaxFieldSize (0);
            csTypes_.clearWarnings();
            byte[] p = csTypes_.getBytes (13);
            byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r',
                (byte) 'c', (byte) 'h', (byte) ' ',
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' ', (byte) ' ',     
                (byte) ' ', (byte) ' '};     
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            if (csTypes_.getWarnings() != null) {
              passed = false; 
              sb.append("Warning returned: "+csTypes_.getWarnings()); 
            }
            assertCondition(passed,sb); 

        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBytes() - Get a type that was registered as a BINARY, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var021()
    {
        try
        {
            csTypes_.setMaxFieldSize (3);
            csTypes_.clearWarnings();
            byte[] p = csTypes_.getBytes (13);
            byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r'};     
            DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
            csTypes_.setMaxFieldSize (0);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            if ((csTypes_.getWarnings() != null)) {
              passed = false; 
              sb.append("Warning = "+dt); 
            }
            assertCondition(passed,sb); 
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBytes() - Get a type that was registered as a VARBINARY.
    **/
    public void Var022()
    {
        try
        {
            byte[] p = csTypes_.getBytes (14);            
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W',
                (byte) 'a', (byte) 'l', (byte) 'l'};     
            // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            assertCondition(passed,sb); 

          
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }


    /**
    getBytes() - Get a type that was registered as a VARBINARY, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var023()
    {
        try
        {
            csTypes_.setMaxFieldSize (0);
            csTypes_.clearWarnings();
            byte[] p = csTypes_.getBytes (14);
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W',
                (byte) 'a', (byte) 'l', (byte) 'l'};     
            
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb  ) && (csTypes_.getWarnings() == null);
            assertCondition(passed,sb); 

	}
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    getBytes() - Get a type that was registered as a VARBINARY, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var024()
    {
        try
        {
            csTypes_.setMaxFieldSize (6);
            csTypes_.clearWarnings();
            byte[] p = csTypes_.getBytes (14);
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W'};     
            csTypes_.setMaxFieldSize (0);
            
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb) && (csTypes_.getWarnings() == null);
            assertCondition(passed,sb); 

         }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }




    /**
    getBytes() - Get a type that was registered as a DATE.
    **/
    public void Var025()
    {
        try
        {
            byte[] p = csTypes_.getBytes (15);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as a TIME.
    **/
    public void Var026()
    {
        try
        {
            byte[] p = csTypes_.getBytes (16);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var027()
    {
        try
        {
            byte[] p = csTypes_.getBytes (17);
            failed ("Didn't throw SQLException "+p);
        }
        catch(Exception e)
        {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    getBytes() - Get a type that was registered as an OTHER.
    **/
    public void Var028()
    {
        if(checkLobSupport ())
        {
            try
            {
                byte[] p = csTypes_.getBytes (18);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Get a type that was registered as a BLOB.
    **/
    public void Var029()
    {
        if(checkLobSupport ())
        {
            try
            {
              StringBuffer sb = new StringBuffer(); 
                
                byte[] p = csTypes_.getBytes (19);

                byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                    (byte) 'e', (byte) ' ', (byte) 'E',
                    (byte) 'g', (byte) 'g', (byte) 'e'};     // Should be 'Dave Egge'
                boolean passed = areEqual (p, check, sb);
                assertCondition(passed,sb);
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    getBytes() - Get a type that was registered as a CLOB.
    **/
    public void Var030()
    {
        if(checkLobSupport ())
        {
            try
            {
                byte[] p = csTypes_.getBytes (20);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    getBytes() - Get a type that was registered as a BIGINT.
    **/
    public void Var031()
    {
        if(checkBigintSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes(22);
		//
		// Updated 12/22/2011 --
		// Toolbox driver should also throw exception 
                    failed("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
		assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }


    /**
    getBytes() - Get an INOUT parameter, where the OUT parameter is
    longer than the IN parameter, when the output parameter is registered first.
    
    SQL400 - We added this testcase because of a customer bug.
    **/
    public void Var032()
    {
        try
        {
            CallableStatement cs = JDSetupProcedure.prepare (connection_,
                                                             JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
            JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                       supportedFeatures_, getDriver());
            cs.registerOutParameter (14, Types.VARBINARY);
            JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
                                                 supportedFeatures_);
            cs.execute ();
            byte[] p = cs.getBytes (14);
            byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                (byte) 'e', (byte) ' ', (byte) 'W',
                (byte) 'a', (byte) 'l', (byte) 'l',
                (byte) 'B', (byte) 'o', (byte) 'n',
                (byte) 'j', (byte) 'o', (byte) 'u',
                (byte) 'r'};
            //for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
            //for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
            StringBuffer sb = new StringBuffer(); 
            boolean passed = areEqual(p, check, sb);
            assertCondition(passed,sb); 

    
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception");
        }
    }
    // - NAMED PARAMETERS
    /**
    getBytes() - Get a type that was registered as a SMALLINT.
    **/
    public void Var033()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_SMALLINT");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an INTEGER.
    **/
    public void Var034()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_INTEGER");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an REAL.
    **/
    public void Var035()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_REAL");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an FLOAT.
    **/
    public void Var036()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_FLOAT");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an DOUBLE.
    **/
    public void Var037()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_DOUBLE");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an DECIMAL.
    **/
    public void Var038()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_DECIMAL_50");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an DECIMAL.
    **/
    public void Var039()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_DECIMAL_105");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an NUMERIC.
    **/
    public void Var040()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_NUMERIC_50");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an NUMERIC.
    **/
    public void Var041()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_NUMERIC_105");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a CHAR.
    **/
    public void Var042()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_CHAR_1");
                if(getDriver() == JDTestDriver.DRIVER_NATIVE)
                    failed ("Didn't throw SQLException "+p);
                else
                {
                    if(p[0] == 12)
                        succeeded();
                    else
                        failed(p[0] + " != 12");
                }
            }
            catch(Exception e)
            {
                if(getDriver() == JDTestDriver.DRIVER_NATIVE)
                    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
                else
                    failed(e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a CHAR.
    **/
    public void Var043()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_CHAR_50");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a VARCHAR.
    **/
    public void Var044()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_VARCHAR");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a BINARY.
    **/
    public void Var045()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_BINARY_20");
                byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r',
                    (byte) 'c', (byte) 'h', (byte) ' ',
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' '};     
                // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
                // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb);
                assertCondition(passed,sb); 

       
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a BINARY, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var046()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                byte[] p = csTypes_.getBytes ("P_BINARY_20");
                byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r',
                    (byte) 'c', (byte) 'h', (byte) ' ',
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' ', (byte) ' ',     
                    (byte) ' ', (byte) ' '};     
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb) && (csTypes_.getWarnings() == null) ;
                assertCondition(passed,sb); 

            
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a BINARY, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var047()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (3);
                csTypes_.clearWarnings();
                byte[] p = csTypes_.getBytes ("P_BINARY_20");
                byte[] check = new byte[] { (byte) 'M', (byte) 'u', (byte) 'r'};     
                DataTruncation dt = (DataTruncation) csTypes_.getWarnings();
                csTypes_.setMaxFieldSize (0);
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb)  && (csTypes_.getWarnings() == null) ;
		
                assertCondition(passed,  "warning = "+dt + sb.toString()); 

                                         
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a VARBINARY.
    **/
    public void Var048()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_VARBINARY_20");            
                byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                    (byte) 'e', (byte) ' ', (byte) 'W',
                    (byte) 'a', (byte) 'l', (byte) 'l'};     
                // for (int i = 0; i < p.length; ++i) System.out.println ("p[" + i + "]=" + p[i]);
                // for (int i = 0; i < check.length; ++i) System.out.println ("check[" + i + "]=" + check[i]);
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb);
                assertCondition(passed,sb); 

           
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a VARBINARY, with maxFieldSize
    set to 0.  Check that no DataTruncation warning is posted
    and the string is not trucated.
    **/
    public void Var049()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (0);
                csTypes_.clearWarnings();
                byte[] p = csTypes_.getBytes ("P_VARBINARY_20");
                byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                    (byte) 'e', (byte) ' ', (byte) 'W',
                    (byte) 'a', (byte) 'l', (byte) 'l'};    
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb)  && (csTypes_.getWarnings() == null);
                assertCondition(passed,sb); 

       
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a VARBINARY, with maxFieldSize
    set to 5.  Check that no DataTruncation warning is posted
    and the string is truncated.
    **/
    public void Var050()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                csTypes_.setMaxFieldSize (6);
                csTypes_.clearWarnings();
                byte[] p = csTypes_.getBytes ("P_VARBINARY_20");
                byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                    (byte) 'e', (byte) ' ', (byte) 'W'};     
                csTypes_.setMaxFieldSize (0);
                StringBuffer sb = new StringBuffer(); 
                boolean passed = areEqual(p, check, sb)  && (csTypes_.getWarnings() == null);
                assertCondition(passed,sb); 

           
            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a DATE.
    **/
    public void Var051()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_DATE");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as a TIME.
    **/
    public void Var052()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_TIME");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }

    }
    /**
    getBytes() - Get a type that was registered as a TIMESTAMP.
    **/
    public void Var053()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_TIMESTAMP");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }
    /**
    getBytes() - Get a type that was registered as an OTHER.
    **/
    public void Var054()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_DATALINK");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    /**
    getBytes() - Get a type that was registered as a BLOB.
    **/
    public void Var055()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_BLOB");

                byte[] check = new byte[] { (byte) 'D', (byte) 'a', (byte) 'v',
                    (byte) 'e', (byte) ' ', (byte) 'E',
                    (byte) 'g', (byte) 'g', (byte) 'e'};     // Should be 'Dave Egge'
		    StringBuffer sb = new StringBuffer();
		    boolean passed = areEqual (p, check, sb); 
                assertCondition (passed,sb);

            }
            catch(Exception e)
            {
                failed (e, "Unexpected Exception");
            }
        }
    }

    /**
    getBytes() - Get a type that was registered as a CLOB.
    **/
    public void Var056()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_CLOB");
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }

    /**
    getBytes() - Get a type that was registered as a BIGINT.
    **/
    public void Var057()
    {
        if(checkNamedParametersSupport())
        {
            try
            {
                byte[] p = csTypes_.getBytes ("P_BIGINT");
		failed("Didn't throw SQLException "+p);
            }
            catch(Exception e)
            {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
        }
    }


    /**
    getByte() -- Get a type registered as VARCHAR for BIT data, but containing all spaces.
    Added to expose native bug..
    **/
    public void Var058()
    {
        try
        {


	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ALLSPVB (OUT B VARCHAR(80) FOR BIT DATA) LANGUAGE SQL SPECIFIC ALLSPVB JDCSALLSPVB: BEGIN SET B=X'0000000000000000'; END JDCSALLSPVB" ;

	    Statement stmt = connection_.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ALLSPVB");
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ALLSPVB (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.execute();

	    byte [] check = { (byte) 0 , (byte) 0, (byte) 0, (byte) 0, (byte) 0 , (byte) 0, (byte) 0, (byte) 0 };
	    byte [] p  = cstmt.getBytes(1);

      StringBuffer sb = new StringBuffer("-- Added by native driver 04/20/2004\n"); 
      boolean passed = areEqual(p, check, sb);
      assertCondition(passed,sb); 

  
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception -- Added by native driver 04/20/2004");
        }
    }

    /**
    getByte() -- Get a type registered as VARCHAR for BIT data, but containing all spaces.
    Added to expose native bug..
    **/
    public void Var059()
    {
        try
        {


	    String  sql = "CREATE PROCEDURE "+JDCSTest.COLLECTION+".ALLSPVB (OUT B VARCHAR(80) FOR BIT DATA) LANGUAGE SQL SPECIFIC ALLSPVB JDCSALLSPVB: BEGIN SET B=X'4040404040404040'; END JDCSALLSPVB" ;

	    Statement stmt = connection_.createStatement ();
	    try {
              stmt.executeUpdate("drop procedure "+JDCSTest.COLLECTION+".ALLSPVB");
            } catch (Exception e) {}    	    
	    stmt.executeUpdate(sql);
	    CallableStatement cstmt = connection_.prepareCall("{call "+JDCSTest.COLLECTION+".ALLSPVB (?)}");
	    cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);
	    cstmt.execute();

	    byte [] check = { (byte) 0x40 , (byte) 0x40, (byte) 0x40, (byte) 0x40, (byte) 0x40 , (byte) 0x40, (byte) 0x40, (byte) 0x40 };
	    byte [] p  = cstmt.getBytes(1);

      StringBuffer sb = new StringBuffer("-- Added by native driver 04/20/2004\n"); 
      boolean passed = areEqual(p, check, sb);
      assertCondition(passed,sb); 

      
        }
        catch(Exception e)
        {
            failed (e, "Unexpected Exception -- Added by native driver 04/20/2004");
        }
    }

    
    /**
    getBytes() - Get a type that was registered as a BOOLEAN.
    **/
       
       public void Var060()
       {
          if (checkBooleanSupport()) {
            getTestFailed(csTypes_,"getBytes",25,"Data type mismatch",""); 
           
          }
       }

       
       /**
       getBytes() - Get a type that was registered as a BOOLEAN.
       **/
          
          public void Var061()
          {
             if (checkBooleanSupport()) {
               getTestFailed(csTypes_,"getBytes","P_BOOLEAN","Data type mismatch",""); 
              
             }
          }


}
