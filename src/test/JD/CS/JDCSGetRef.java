///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetRef.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.Ref;
import java.sql.SQLException;

import java.sql.Types;
import java.util.Hashtable; import java.util.Vector;



/**
Testcase JDCSGetRef.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getRef()
</ul>
**/
public class JDCSGetRef
extends JDCSGetTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDCSGetRef";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDCSTest.main(newArgs); 
   }






/**
Constructor.
**/
    public JDCSGetRef (AS400 systemObject,
                                    Hashtable<String,Vector<String>> namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    
                                    String password)
    {
        super (systemObject, "JDCSGetRef",
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
super.setup();    }



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
getRef() - Get parameter -1.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            try {
                csTypes_.execute ();
                Ref p = csTypes_.getRef (-1);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get parameter 0.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (0);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Use a parameter that is too big.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (35);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a parameter when there are no parameters.
**/
    public void Var004()
    {        
        if (checkJdbc20 ()) {
            // I created a whole new Connection object here to work
            // around a server bug.
            Connection c = null;
            try {
                c = testDriver_.getConnection (baseURL_
                    + ";errors=full", userId_, encryptedPassword_);
                CallableStatement cs = c.prepareCall ("CALL "
                        + JDSetupProcedure.STP_CS0);
                cs.execute ();
                Ref p = cs.getRef (1);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
            finally {
                try {
                    c.close ();
                }
                catch (SQLException e) {
                    // Ignore.
                }
            }
        }
    }



/**
getRef() - Get a parameter that was not registered.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                    JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.execute ();
                Ref p = cs.getRef (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a parameter when the statement has not been
executed.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                    JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                 cs.registerOutParameter (12, Types.REF);
                Ref p = cs.getRef (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a parameter when the statement is closed.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                    JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
                cs.registerOutParameter (12, Types.REF);
                cs.execute ();
                cs.close ();
                Ref p = cs.getRef (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get an IN parameter that was correctly registered.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            try {
                CallableStatement cs = JDSetupProcedure.prepare (connection_,
                    JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
                JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
                                                     supportedFeatures_);
                cs.registerOutParameter (12, Types.REF);
                cs.execute ();
                Ref p = cs.getRef (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }


/**
getRef() - Get a type that was registered as a SMALLINT.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (1);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an INTEGER.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (2);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an REAL.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (3);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an FLOAT.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (4);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an DOUBLE.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (5);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an DECIMAL.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (6);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an NUMERIC.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (8);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a CHAR(1).
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (10);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
     }



/**
getRef() - Get a type that was registered as a CHAR(50).
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (11);
                failed ("Didn't throw SQLException "+p);
           }
           catch(Exception e) {
               assertExceptionIsInstanceOf (e, "java.sql.SQLException");
           }
        }
     }



/**
getRef() - Get a type that was registered as a VARCHAR(50).
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (12);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a BINARY.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (13);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a VARBINARY.
**/
    public void Var020()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (14);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a DATE.
**/
    public void Var021()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (15);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a TIME.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (16);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }




/**
getRef() - Get a type that was registered as a TIMESTAMP.
**/
    public void Var023()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (17);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as an OTHER.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (18);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a BLOB.
**/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (19);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a CLOB.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            try {
                Ref p = csTypes_.getRef (20);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



/**
getRef() - Get a type that was registered as a BIGINT.
**/
    public void Var027()
    {
        if (checkJdbc20 ()) {
        if (checkBigintSupport()) {
            try {
                Ref p = csTypes_.getRef (22);
                failed ("Didn't throw SQLException "+p);
            }
            catch(Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
        }
    }

    /**
     * getRef() - Get a type that was registered as a BOOLEAN.
     **/

    public void Var028() {
      if (checkJdbc40()) {

        if (checkBooleanSupport()) {
          getTestFailed(csTypes_, "getRef", 25, "Data type mismatch", "");

        }
      }
    }

    /**
     * getRef() - Get a type that was registered as a BOOLEAN.
     **/

    public void Var029() {
      if (checkJdbc40()) {

        if (checkBooleanSupport()) {
          getTestFailed(csTypes_, "getRef", "P_BOOLEAN", "Data type mismatch",
              "");

        }
      }
    }


}



