///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCSGetURL.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////
//
// File Name:    JDCSGetURL.java
//
// Classes:      JDCSGetURL
//
////////////////////////////////////////////////////////////////////////

package test.JD.CS;

import com.ibm.as400.access.AS400;

import test.JDSetupProcedure;

import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.Types;
import java.util.Hashtable;
import java.net.URL;



/**
Testcase JDCSGetURL.  This tests the following
method of the JDBC CallableStatement class:

<ul>
<li>getURL()
</ul>
**/
public class JDCSGetURL
extends JDCSGetTestcase
{



 


/**
Constructor.
**/
   public JDCSGetURL (AS400 systemObject,
                         Hashtable namesAndVars,
                         int runMode,
                         FileOutputStream fileOutputStream,
                         
                         String password)
   {
      super (systemObject, "JDCSGetURL",
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
getURL() - Get parameter -1.
**/
   public void Var001()
   {
       if (checkJdbc30 ()) {
	   try {
	       csTypes_.execute ();
	       URL p = csTypes_.getURL (-1);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get parameter 0.
**/
   public void Var002()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (0);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Use a parameter that is too big.
**/
   public void Var003()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (35);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get a parameter when there are no parameters.
**/
   public void Var004()
   {
       if (checkJdbc30 ()) {    
      // I created a whole new Connection object here to work
      // around a server bug.
	   Connection c = null;
	   try {
	       c = testDriver_.getConnection (baseURL_
					      + ";errors=full", userId_, encryptedPassword_);
	       CallableStatement cs = c.prepareCall ("CALL "
						     + JDSetupProcedure.STP_CS0);
	       cs.execute ();
	       URL p = cs.getURL (1);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   } 

	   if (c != null) try { c.close(); } catch (Exception e) {}
       }
   }



/**
getURL() - Get a parameter that was not registered.
**/
   public void Var005()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	       cs.execute ();
	       URL p = cs.getURL (18);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get a parameter when the statement has not been
executed.
**/
   public void Var006()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	       cs.registerOutParameter (18, Types.DATALINK);
	       URL p = cs.getURL (18);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get a parameter when the statement is closed.
**/
   public void Var007()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESOUT, supportedFeatures_);
	       cs.registerOutParameter (18, Types.DATALINK);
	       cs.execute ();
	       cs.close ();
	       URL p = cs.getURL (18);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get an IN parameter that was correctly registered.
**/
   public void Var008()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESIN, supportedFeatures_);
	       JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESIN,
						    supportedFeatures_);
	       cs.registerOutParameter (18, Types.DATALINK);
	       cs.execute ();
	       URL p = cs.getURL (18);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter.
**/
   public void Var009()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
	       JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
						    supportedFeatures_);
	       JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
					  supportedFeatures_, getDriver());
	       cs.registerOutParameter (18, Types.DATALINK);
	       cs.execute ();
	       URL p = cs.getURL (18);
	       assertCondition (p.toString().equals ("http://www.sony.com/pix.html"));
	   } catch (Exception e)
	   {
	       failed (e, "Unexpected Exception");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a SMALLINT.
**/
   public void Var010()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (1);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an INTEGER.
**/
   public void Var011()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (2);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an REAL.
**/
   public void Var012()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (3);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an FLOAT.
**/
   public void Var013()
   {
       if (checkJdbc30 ()) {



	   try {
	       URL p = csTypes_.getURL (4);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an DOUBLE.
**/
   public void Var014()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (5);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }



/**
getURL() - Get a type that was registered as an DECIMAL.
**/
   public void Var015()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (6);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an NUMERIC.
**/
   public void Var016()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (8);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a CHAR(1).
**/
   public void Var017()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (10);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }   
       }
   }

/**
getURL() - Get a type that was registered as a VARCHAR(50).
**/
   public void Var018()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL(12);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }   
       }
   }

/**
getURL() - Get a type that was registered as a BINARY.
**/
   public void Var019()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (13);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a VARBINARY.
**/
   public void Var020()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (14);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a DATE.
**/
   public void Var021()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (15);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a TIME.
**/
   public void Var022()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (16);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var023()
   {
       if (checkJdbc30 ()) {
	   try {
	       URL p = csTypes_.getURL (17);
	       failed ("Didn't throw SQLException "+p);
	   } catch (Exception e) {
	       assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	   }
       }
   }


/**
getURL() - Get a type that was registered as an OTHER.
**/
   public void Var024()
   {
       if (checkJdbc30 ()) {
	   if (checkLobSupport ()) {
	       try {
		   URL p = csTypes_.getURL (18);
		   assertCondition (p.toString().equals ("http://w3.rchland.ibm.com/index.html"));
	       } catch (Exception e)
	       {
		   failed (e, "Unexpected Exception");
	       }
	   }
       }
   }


/**
getURL() - Get a type that was registered as a BLOB.
**/
   public void Var025()
   {
       if (checkJdbc30 ()) {
	   if (checkLobSupport ()) {
	       try {
		   URL p = csTypes_.getURL (19);
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getURL() - Get a type that was registered as a CLOB.

SQL400 - The native driver allows this test to work.
**/
   public void Var026()
   {
       if (checkJdbc30 ()) {
	   if (checkLobSupport ()) {
	       try {
		   URL p = csTypes_.getURL (20);
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getURL() - Get a type that was registered as a BIGINT.
**/
   public void Var027()
   {
       if (checkJdbc30 ()) {
	   if (checkBigintSupport()) {
	       try {
		   URL p = csTypes_.getURL (22);
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }
	   }
       }
   }


/**
getURL() - Get an INOUT parameter, where the OUT parameter is
longer than the IN parameter, when the output parameter is registered first.

SQL400 - We added this testcase because of a customer bug.
**/
   public void Var028()
   {
       if (checkJdbc30 ()) {
	   try {
	       CallableStatement cs = JDSetupProcedure.prepare (connection_,
								JDSetupProcedure.STP_CSTYPESINOUT, supportedFeatures_);
	       JDSetupProcedure.register (cs, JDSetupProcedure.STP_CSTYPESINOUT,
					  supportedFeatures_, getDriver());
	       cs.registerOutParameter (18, Types.DATALINK);
	       JDSetupProcedure.setTypesParameters (cs, JDSetupProcedure.STP_CSTYPESINOUT,
						    supportedFeatures_);
	       cs.execute ();
	       URL p = cs.getURL (18);
	       assertCondition (p.toString().equals ("http://www.sony.com/pix.html"));
	   } catch (Exception e)
	   {
	       failed (e, "Unexpected Exception");
	   }
       }
   }

// Named Parameters 

/**
getURL() - Get a named parameter that doesn't exist
**/
   public void Var029()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_Fake");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }
/**
getURL() - Get a type that was registered as a SMALLINT.
**/
   public void Var030()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_SMALLINT");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an INTEGER.
**/
   public void Var031()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_INTEGER");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an REAL.
**/
   public void Var032()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_REAL");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an FLOAT.
**/
   public void Var033()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_FLOAT");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an DOUBLE.
**/
   public void Var034()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_DOUBLE");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }


/**
getURL() - Get a type that was registered as an DECIMAL.
**/
   public void Var035()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_DECIMAL_50");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an NUMERIC.
**/
   public void Var036()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_NUMERIC_50");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }	
	}
   }

/**
getURL() - Get a type that was registered as a CHAR(1).
**/
   public void Var037()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_CHAR_1");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}   
	    }
	}
   }
/**
getURL() - Get a type that was registered as a VARCHAR(50).
**/
   public void Var038()
   {	
       if(checkNamedParametersSupport()) {
	   if (checkJdbc30 ()) {
	       try {
		   URL p = csTypes_.getURL("P_VARCHAR_50");
		   failed ("Didn't throw SQLException "+p);
	       } catch (Exception e) {
		   assertExceptionIsInstanceOf (e, "java.sql.SQLException");
	       }   
	   }
       }
   }
/**
getURL() - Get a type that was registered as a BINARY.
**/
   public void Var039()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_BINARY_20");
                    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a VARBINARY.
**/
   public void Var040()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_VARBINARY_20");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a DATE.
**/
   public void Var041()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_DATE");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a TIME.
**/
   public void Var042()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_TIME");
		    failed ("Didn't throw SQLException "+p);
		} catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a TIMESTAMP.
**/
   public void Var043()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		try {
		    URL p = csTypes_.getURL ("P_TIMESTAMP");
		    failed ("Didn't throw SQLException "+p);
		}catch (Exception e) {
		    assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as an OTHER.
**/
   public void Var044()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		if (checkLobSupport ()) {
		    try {
			URL p = csTypes_.getURL ("P_DATALINK");
			assertCondition (p.toString().equals ("http://w3.rchland.ibm.com/index.html"));
		    } catch (Exception e)
		    {
			failed (e, "Unexpected Exception");
		    }
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a BLOB.
**/
   public void Var045()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		if (checkLobSupport ()) {
		    try {
			URL p = csTypes_.getURL ("P_BLOB");
			failed ("Didn't throw SQLException "+p);
		    } catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a CLOB.

SQL400 - The native driver allows this test to work.
**/
   public void Var046()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		if (checkLobSupport ()) {
		    try {
			URL p = csTypes_.getURL ("P_CLOB");
			failed ("Didn't throw SQLException "+p);
		    } catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }
	}
   }

/**
getURL() - Get a type that was registered as a BIGINT.
**/
   public void Var047()
   {
        if(checkNamedParametersSupport()) {
	    if (checkJdbc30 ()) {
		if (checkBigintSupport()) {
		    try {
			URL p = csTypes_.getURL ("P_BIGINT");
			failed ("Didn't throw SQLException "+p);
		    } catch (Exception e) {
			assertExceptionIsInstanceOf (e, "java.sql.SQLException");
		    }
		}
	    }
	}
   }
   
   
   /**
   getURL() - Get a type that was registered as a BOOLEAN.
   **/
      
      public void Var048()
      {
         if (checkBooleanSupport()) {
           getTestFailed(csTypes_,"getURL",25,"Data type mismatch",""); 
          
         }
      }

      
      /**
      getURL() - Get a type that was registered as a BOOLEAN.
      **/
         
         public void Var049()
         {
            if (checkBooleanSupport()) {
              getTestFailed(csTypes_,"getURL","P_BOOLEAN","Data type mismatch",""); 
             
            }
         }

}
