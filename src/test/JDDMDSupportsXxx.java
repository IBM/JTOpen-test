///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDDMDSupportsXxx.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDDMDSupportsXxx.java
//
// Classes:      JDDMDSupportsXxx
//
////////////////////////////////////////////////////////////////////////
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.Hashtable;



/**
Testcase JDDMDSupportsXxx.  This tests the following methods
of the JDBC DatabaseMetaData class:

<ul>
<li>supportsAlterTableWithAddColumn()
<li>supportsAlterTableWithDropColumn()
<li>supportsANSI92EntryLevelSQL()
<li>supportsANSI92FullSQL()
<li>supportsANSI92IntermediateSQL()
<li>supportsBatchUpdates()
<li>supportsCatalogsInDataManipulation()
<li>supportsCatalogsInIndexDefinitions()
<li>supportsCatalogsInPrivilegeDefinitions()
<li>supportsCatalogsInProcedureCalls()
<li>supportsCatalogsInTableDefinitions()
<li>supportsColumnAliasing()
<li>supportsConvert()
<li>supportsCoreSQLGrammar()
<li>supportsCorrelatedSubqueries()
<li>supportsDataDefinitionAndDataManipulationTransactions()
<li>supportsDataManipulationTransactionsOnly()
<li>supportsDifferentTableCorrelationNames()
<li>supportsExpressionsInOrderBy()
<li>supportsExtendedSQLGrammar()
<li>supportsFullOuterJoins()
<li>supportsGroupBy()
<li>supportsGroupByBeyondSelect()
<li>supportsGroupByUnrelated()
<li>supportsIntegrityEnhancementFacility()
<li>supportsLikeEscapeClause()
<li>supportsLimitedOuterJoins()
<li>supportsMinimumSQLGrammar()
<li>supportsMixedCaseIdentifiers()
<li>supportsMixedCaseQuotedIdentifiers()
<li>supportsMultipleResultSets()
<li>supportsMultipleTransactions()
<li>supportsNonNullableColumns()
<li>supportsOpenCursorsAcrossCommit()
<li>supportsOpenCursorsAcrossRollback()
<li>supportsOpenStatementsAcrossCommit()
<li>supportsOpenStatementsAcrossRollback()
<li>supportsOrderByUnrelated()
<li>supportsOuterJoins()
<li>supportsPositionedDelete()
<li>supportsPositionedUpdate()
<li>supportsResultSetConcurrency()
<li>supportsResultSetType()
<li>supportsSchemasInDataManipulation()
<li>supportsSchemasInIndexDefinitions()
<li>supportsSchemasInPrivilegeDefinitions()
<li>supportsSchemasInProcedureCalls()
<li>supportsSchemasInTableDefinitions()
<li>supportsSelectForUpdate()
<li>supportsStoredProcedures()
<li>supportsSubqueriesInComparisons()
<li>supportsSubqueriesInExists()
<li>supportsSubqueriesInIns()
<li>supportsSubqueriesInQuantifieds()
<li>supportsTableCorrelationNames()
<li>supportsTransactionIsolationLevel(int)
<li>supportsTransactions()
<li>supportsUnion()
<li>supportsUnionAll()
<li>supportsSavepoints()                    //@C1A
<li>supportsNamedParameters()               //@C1A
<li>supportsMultipleOpenResults()           //@C1A
<li>supportsGetGeneratedKeys()              //@C1A
<li>supportsResultSetHoldability()          //@C1A
</ul>
**/
public class JDDMDSupportsXxx
extends JDTestcase
{
    // Private data.
    private Connection          connection_;
    private Connection          closedConnection_;
    private DatabaseMetaData    dmd_;
    private DatabaseMetaData    dmd2_;



    /**
    Constructor.
    **/
    public JDDMDSupportsXxx (AS400 systemObject,
                             Hashtable namesAndVars,
                             int runMode,
                             FileOutputStream fileOutputStream,
                             
                             String password)
    {
        super (systemObject, "JDDMDSupportsXxx",
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
        String url;

        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          url = "jdbc:db2://" + systemObject_.getSystemName()+":446/*LOCAL"; 

          connection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);
          dmd_ = connection_.getMetaData ();

          closedConnection_ = testDriver_.getConnection (url, systemObject_.getUserId(), encryptedPassword_);
          dmd2_ = closedConnection_.getMetaData ();
          closedConnection_.close ();
          
        } else {
        
        if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)
            url = "jdbc:as400://" + systemObject_.getSystemName()
                  
                  ;
        else if (getDriver () == JDTestDriver.DRIVER_JTOPENLITE)
            url = "jdbc:jtopenlite://" + systemObject_.getSystemName()
                  
                  ;
        else
            url = "jdbc:db2://" + systemObject_.getSystemName()
                  
                  ;


        connection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        dmd_ = connection_.getMetaData ();

        closedConnection_ = testDriver_.getConnection (url,systemObject_.getUserId(), encryptedPassword_);
        dmd2_ = closedConnection_.getMetaData ();
        closedConnection_.close ();
        }
    }



    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        connection_.close ();
    }



    /**
    supportsAlterTableWithAddColumn() - Should return the correct value
    when the connection is open.
    **/
    public void Var001()
    {
        try {
            assertCondition (dmd_.supportsAlterTableWithAddColumn () == true); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsAlterTableWithAddColumn() - Should return the correct value
    when the connection is closed.
    **/
    public void Var002()
    {
        try {
            assertCondition (dmd2_.supportsAlterTableWithAddColumn () == true); // @A1C
        }
        catch (Exception e)  {
            if (getDriver() == JDTestDriver.DRIVER_JCC) {
              // JCC throws exception on closed connection
              assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            } else { 
              failed (e, "Unexpected Exception");
            }
        }
    }




    /**
    supportsAlterTableWithDropColumn() - Should return the correct value
    when the connection is open.
    **/
    public void Var003()
    {
        try {
            assertCondition (dmd_.supportsAlterTableWithDropColumn () == true); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsAlterTableWithDropColumn() - Should return the correct value
    when the connection is closed.
    **/
    public void Var004()
    {
        try {
            assertCondition (dmd2_.supportsAlterTableWithDropColumn () == true); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }




    /**
    supportsANSI92EntryLevelSQL() - Should return the correct value
    when the connection is open.
    **/
    public void Var005()
    {
        try {
            assertCondition (dmd_.supportsANSI92EntryLevelSQL () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsANSI92EntryLevelSQL() - Should return the correct value
    when the connection is closed.
    **/
    public void Var006()
    {
        try {
            assertCondition (dmd2_.supportsANSI92EntryLevelSQL () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }




    /**
    supportsANSI92FullSQL() - Should return the correct value
    when the connection is open.
    **/
    public void Var007()
    {
        try {
            assertCondition (dmd_.supportsANSI92FullSQL () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsANSI92FullSQL() - Should return the correct value
    when the connection is closed.
    **/
    public void Var008()
    {
        try {
            assertCondition (dmd2_.supportsANSI92FullSQL () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }




    /**
    supportsANSI92IntermediateSQL() - Should return the correct value
    when the connection is open.
    **/
    public void Var009()
    {
        try {
            assertCondition (dmd_.supportsANSI92IntermediateSQL () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsANSI92IntermediateSQL() - Should return the correct value
    when the connection is closed.
    **/
    public void Var010()
    {
        try {
            assertCondition (dmd2_.supportsANSI92IntermediateSQL () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }




    /**
    supportsBatchUpdates() - Should return the correct value
    when the connection is open.
    **/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsBatchUpdates () == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsBatchUpdates() - Should return the correct value
    when the connection is closed.
    **/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.supportsBatchUpdates () == true);
            }
            catch (Exception e)  {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else { 
                failed (e, "Unexpected Exception");
              }
            }
        }
    }




    /**
    supportsCatalogsInDataManipulation() - Should return the correct value
    when the connection is open.
    **/
    public void Var013()
    {
        try {
            assertCondition (dmd_.supportsCatalogsInDataManipulation () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCatalogsInDataManipulation() - Should return the correct value
    when the connection is closed.
    **/
    public void Var014()
    {
        try {
            assertCondition (dmd2_.supportsCatalogsInDataManipulation () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCatalogsInIndexDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var015()
    {
        try {
            assertCondition (dmd_.supportsCatalogsInIndexDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCatalogsInIndexDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var016()
    {
        try {
            assertCondition (dmd2_.supportsCatalogsInIndexDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCatalogsInPrivilegeDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var017()
    {
        try {
            assertCondition (dmd_.supportsCatalogsInPrivilegeDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCatalogsInPrivilegeDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var018()
    {
        try {
            assertCondition (dmd2_.supportsCatalogsInPrivilegeDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCatalogsInProcedureCalls() - Should return the correct value
    when the connection is open.
    **/
    public void Var019()
    {
        try {
            assertCondition (dmd_.supportsCatalogsInProcedureCalls () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCatalogsInProcedureCalls() - Should return the correct value
    when the connection is closed.
    **/
    public void Var020()
    {
        try {
            assertCondition (dmd2_.supportsCatalogsInProcedureCalls () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCatalogsInTableDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var021()
    {
        try {
            assertCondition (dmd_.supportsCatalogsInTableDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCatalogsInTableDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var022()
    {
        try {
            assertCondition (dmd2_.supportsCatalogsInTableDefinitions () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsColumnAliasing() - Should return the correct value
    when the connection is open.
    **/
    public void Var023()
    {
        try {
            assertCondition (dmd_.supportsColumnAliasing () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsColumnAliasing() - Should return the correct value
    when the connection is closed.
    **/
    public void Var024()
    {
        try {
            assertCondition (dmd2_.supportsColumnAliasing () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsConvert() - Should return the correct value
    when the connection is open.
    **/
    public void Var025()
    {
        try {
            assertCondition (dmd_.supportsConvert () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsConvert() - Should return the correct value
    when the connection is closed.
    **/
    public void Var026()
    {
        try {
            assertCondition (dmd2_.supportsConvert () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsConvert() - Should return the correct value
    when the connection is open.
    **/
    public void Var027()
    {
        try {
            // This is just a random sampling.
            assertCondition ((dmd_.supportsConvert (Types.BIGINT, Types.BINARY) == false)
                             && (dmd_.supportsConvert (Types.CHAR, Types.VARCHAR) == false)
                             && (dmd_.supportsConvert (Types.TIMESTAMP, Types.VARCHAR) == false));
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsConvert() - Should return the correct value
    when the connection is closed.
    **/
    public void Var028()
    {
        try {
            // This is just a random sampling.
            assertCondition ((dmd2_.supportsConvert (Types.DECIMAL, Types.NUMERIC) == false)
                             && (dmd2_.supportsConvert (Types.SMALLINT, Types.INTEGER) == false)
                             && (dmd2_.supportsConvert (Types.SMALLINT, Types.CHAR) == false));
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCoreSQLGrammar() - Should return the correct value
    when the connection is open.
    **/
    public void Var029()
    {
        try {
            assertCondition (dmd_.supportsCoreSQLGrammar () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCoreSQLGrammar() - Should return the correct value
    when the connection is closed.
    **/
    public void Var030()
    {
        try {
            assertCondition (dmd2_.supportsCoreSQLGrammar () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsCorrelatedSubqueries() - Should return the correct value
    when the connection is open.
    **/
    public void Var031()
    {
        try {
            assertCondition (dmd_.supportsCorrelatedSubqueries () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsCorrelatedSubqueries() - Should return the correct value
    when the connection is closed.
    **/
    public void Var032()
    {
        try {
            assertCondition (dmd2_.supportsCorrelatedSubqueries () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsDataDefinitionAndDataManipulationTransactions() - Should return the correct value
    when the connection is open.
    **/
    public void Var033()
    {
        try {
            assertCondition (dmd_.supportsDataDefinitionAndDataManipulationTransactions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsDataDefinitionAndDataManipulationTransactions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var034()
    {
        try {
            assertCondition (dmd2_.supportsDataDefinitionAndDataManipulationTransactions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsDataManipulationTransactionsOnly() - Should return the correct value
    when the connection is open.
    **/
    public void Var035()
    {
        try {
            assertCondition (dmd_.supportsDataManipulationTransactionsOnly () == false); // @A1C
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsDataManipulationTransactionsOnly() - Should return the correct value
    when the connection is closed.
    **/
    public void Var036()
    {
        try {
            assertCondition (dmd2_.supportsDataManipulationTransactionsOnly () == false); // @A1C
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsDifferentTableCorrelationNames() - Should return the correct value
    when the connection is open.
    **/
    public void Var037()
    {
        try {
            assertCondition (dmd_.supportsDifferentTableCorrelationNames () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsDifferentTableCorrelationNames() - Should return the correct value
    when the connection is closed.
    **/
    public void Var038()
    {
        try {
            assertCondition (dmd2_.supportsDifferentTableCorrelationNames () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsExpressionsInOrderBy() - Should return the correct value
    when the connection is open.
    **/
    public void Var039()
    {

	if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	    getDriverFixLevel() < 47828) {
	    notApplicable("Fix for native in PTF SI47828");
	    return; 
	}

        try {
            assertCondition (dmd_.supportsExpressionsInOrderBy () == true, "Changed to true Aug 2012 fixLevel is"+getDriverFixLevel());
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsExpressionsInOrderBy() - Should return the correct value
    when the connection is closed.
    **/
    public void Var040()
    {
       if (getDriver() == JDTestDriver.DRIVER_NATIVE &&
	   getDriverFixLevel() < 47828) {
	   notApplicable("Fix for native in PTF SI47828");
	   return; 
       }

        try {
            assertCondition (dmd2_.supportsExpressionsInOrderBy () == true, "Changed to true on Aug 2012");
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsExtendedSQLGrammar() - Should return the correct value
    when the connection is open.
    **/
    public void Var041()
    {
        try {
            assertCondition (dmd_.supportsExtendedSQLGrammar () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsExtendedSQLGrammar() - Should return the correct value
    when the connection is closed.
    **/
    public void Var042()
    {
        try {
            assertCondition (dmd2_.supportsExtendedSQLGrammar () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsFullOuterJoins() - Should return the correct value
    when the connection is open.
    **/
    public void Var043()
    {
        try {
        	

        	if ((systemObject_.getVersion()) >= 6 )
        		assertCondition (dmd_.supportsFullOuterJoins () == true);
        	
        	else 
        		assertCondition (dmd_.supportsFullOuterJoins () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsFullOuterJoins() - Should return the correct value
    when the connection is closed.
    **/
    public void Var044()
    {
        try {
        	
          	if ((systemObject_.getVersion()) >= 6 )
        		assertCondition (dmd_.supportsFullOuterJoins () == true);
        	
        	else 
        		assertCondition (dmd_.supportsFullOuterJoins () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }


    /**
    supportsGroupBy() - Should return the correct value
    when the connection is open.
    **/
    public void Var045()
    {
        try {
            assertCondition (dmd_.supportsGroupBy () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsGroupBy() - Should return the correct value
    when the connection is closed.
    **/
    public void Var046()
    {
        try {
            assertCondition (dmd2_.supportsGroupBy () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsGroupByBeyondSelect() - Should return the correct value
    when the connection is open.
    **/
    public void Var047()
    {
        try {
            assertCondition (dmd_.supportsGroupByBeyondSelect () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsGroupByBeyondSelect() - Should return the correct value
    when the connection is closed.
    **/
    public void Var048()
    {
        try {
            assertCondition (dmd2_.supportsGroupByBeyondSelect () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsGroupByUnrelated() - Should return the correct value
    when the connection is open.
    **/
    public void Var049()
    {
        try {
            assertCondition (dmd_.supportsGroupByUnrelated () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsGroupByUnrelated() - Should return the correct value
    when the connection is closed.
    **/
    public void Var050()
    {
        try {
            assertCondition (dmd2_.supportsGroupByUnrelated () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsIntegrityEnhancementFacility() - Should return the correct value
    when the connection is open.
    **/
    public void Var051()
    {
        try {
            assertCondition (dmd_.supportsIntegrityEnhancementFacility () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsIntegrityEnhancementFacility() - Should return the correct value
    when the connection is closed.
    **/
    public void Var052()
    {
        try {
            assertCondition (dmd2_.supportsIntegrityEnhancementFacility () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
          }
    }



    /**
    supportsLikeEscapeClause() - Should return the correct value
    when the connection is open.
    **/
    public void Var053()
    {
        try {
            assertCondition (dmd_.supportsLikeEscapeClause () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsLikeEscapeClause() - Should return the correct value
    when the connection is closed.
    **/
    public void Var054()
    {
        try {
            assertCondition (dmd2_.supportsLikeEscapeClause () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsLimitedOuterJoins() - Should return the correct value
    when the connection is open.
    **/
    public void Var055()
    {
        try {
            assertCondition (dmd_.supportsLimitedOuterJoins () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsLimitedOuterJoins() - Should return the correct value
    when the connection is closed.
    **/
    public void Var056()
    {
        try {
            assertCondition (dmd2_.supportsLimitedOuterJoins () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 
            failed (e, "Unexpected Exception");
          }
          }
    }



    /**
    supportsMinimumSQLGrammar() - Should return the correct value
    when the connection is open.
    **/
    public void Var057()
    {
        try {
            assertCondition (dmd_.supportsMinimumSQLGrammar () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsMinimumSQLGrammar() - Should return the correct value
    when the connection is closed.
    **/
    public void Var058()
    {
        try {
            assertCondition (dmd2_.supportsMinimumSQLGrammar () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsMixedCaseIdentifiers() - Should return the correct value
    when the connection is open.
    **/
    public void Var059()
    {
        try {
            assertCondition (dmd_.supportsMixedCaseIdentifiers () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsMixedCaseIdentifiers() - Should return the correct value
    when the connection is closed.
    **/
    public void Var060()
    {
        try {
            assertCondition (dmd2_.supportsMixedCaseIdentifiers () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsMixedCaseQuotedIdentifiers() - Should return the correct value
    when the connection is open.
    **/
    public void Var061()
    {
        try {
            assertCondition (dmd_.supportsMixedCaseQuotedIdentifiers () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsMixedCaseQuotedIdentifiers() - Should return the correct value
    when the connection is closed.
    **/
    public void Var062()
    {
        try {
            assertCondition (dmd2_.supportsMixedCaseQuotedIdentifiers () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
          
    }



    /**
    supportsMultipleResultSets() - Should return the correct value
    when the connection is open.
    **/
    public void Var063()
    {
        try {
            assertCondition (dmd_.supportsMultipleResultSets () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsMultipleResultSets() - Should return the correct value
    when the connection is closed.
    **/
    public void Var064()
    {
        try {
            assertCondition (dmd2_.supportsMultipleResultSets () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsMultipleTransactions() - Should return the correct value
    when the connection is open.
    **/
    public void Var065()
    {
        try {
            assertCondition (dmd_.supportsMultipleTransactions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsMultipleTransactions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var066()
    {
        try {
            assertCondition (dmd2_.supportsMultipleTransactions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsNonNullableColumns() - Should return the correct value
    when the connection is open.
    **/
    public void Var067()
    {
        try {
            assertCondition (dmd_.supportsNonNullableColumns () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsNonNullableColumns() - Should return the correct value
    when the connection is closed.
    **/
    public void Var068()
    {
        try {
            assertCondition (dmd2_.supportsNonNullableColumns () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsOpenCursorsAcrossCommit() - Should return the correct value
    when the connection is open.
    **/
    public void Var069()
    {
        try {
            assertCondition (dmd_.supportsOpenCursorsAcrossCommit () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOpenCursorsAcrossCommit() - Should return the correct value
    when the connection is closed.
    **/
    public void Var070()
    {
        try {
            assertCondition (dmd2_.supportsOpenCursorsAcrossCommit () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else { 

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsOpenCursorsAcrossRollback() - Should return the correct value
    when the connection is open.
    **/
    public void Var071()
    {
        try {
            assertCondition (dmd_.supportsOpenCursorsAcrossRollback () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOpenCursorsAcrossRollback() - Should return the correct value
    when the connection is closed.
    **/
    public void Var072() {
    try {
      assertCondition(dmd2_.supportsOpenCursorsAcrossRollback() == true);
    } catch (Exception e) {
      if (getDriver() == JDTestDriver.DRIVER_JCC) {
        // JCC throws exception on closed connection
        assertExceptionIsInstanceOf(e, "java.sql.SQLException");
      } else {

        failed(e, "Unexpected Exception");
      }
    }
  }



    /**
     * supportsOpenStatementsAcrossCommit() - Should return the correct value
     * when the connection is open.
     */
    public void Var073()
    {
        try {
            assertCondition (dmd_.supportsOpenStatementsAcrossCommit () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOpenStatementsAcrossCommit() - Should return the correct value
    when the connection is closed.
    **/
    public void Var074()
    {
        try {
            assertCondition (dmd2_.supportsOpenStatementsAcrossCommit () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsOpenStatementsAcrossRollback() - Should return the correct value
    when the connection is open.
    **/
    public void Var075()
    {
        try {
            assertCondition (dmd_.supportsOpenStatementsAcrossRollback () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOpenStatementsAcrossRollback() - Should return the correct value
    when the connection is closed.
    **/
    public void Var076()
    {
        try {
            assertCondition (dmd2_.supportsOpenStatementsAcrossRollback () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsOrderByUnrelated() - Should return the correct value
    when the connection is open.
    **/
    public void Var077()
    {
        try {
            assertCondition (dmd_.supportsOrderByUnrelated () == false);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOrderByUnrelated() - Should return the correct value
    when the connection is closed.
    **/
    public void Var078()
    {
        try {
            assertCondition (dmd2_.supportsOrderByUnrelated () == false);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsOuterJoins() - Should return the correct value
    when the connection is open.
    **/
    public void Var079()
    {
        try {
            assertCondition (dmd_.supportsOuterJoins () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsOuterJoins() - Should return the correct value
    when the connection is closed.
    **/
    public void Var080()
    {
        try {
            assertCondition (dmd2_.supportsOuterJoins () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsPositionedDelete() - Should return the correct value
    when the connection is open.
    **/
    public void Var081()
    {
        try {
            assertCondition (dmd_.supportsPositionedDelete () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsPositionedDelete() - Should return the correct value
    when the connection is closed.
    **/
    public void Var082()
    {
        try {
            assertCondition (dmd2_.supportsPositionedDelete () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsPositionedUpdate() - Should return the correct value
    when the connection is open.
    **/
    public void Var083()
    {
        try {
            assertCondition (dmd_.supportsPositionedUpdate () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsPositionedUpdate() - Should return the correct value
    when the connection is closed.
    **/
    public void Var084()
    {
        try {
            assertCondition (dmd2_.supportsPositionedUpdate () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {

            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsResultSetConcurrency() - Should throw an exception when the
    type is not valid.
    **/
    public void Var085()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.supportsResultSetConcurrency (-99, ResultSet.CONCUR_READ_ONLY);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should throw an exception when the
    concurrency is not valid.
    **/
    public void Var086()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.supportsResultSetConcurrency (ResultSet.TYPE_FORWARD_ONLY, -99);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is forward only and concurrency is read only.
    **/
    public void Var087()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is forward only and concurrency is updatable.
    **/
    public void Var088()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is scroll insensitive and concurrency is read only.
    **/
    public void Var089()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is scroll insensitive and concurrency is updatable.
    **/
    public void Var090()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is scroll sensitive and concurrency is read only.
    **/
    public void Var091()
    {
        if (checkJdbc20 ()) {
            try {
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    assertCondition(dmd_.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY));   //allow sensitive read only cursors
                else
                    assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY) == false);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct values when
    type is scroll sensitive and concurrency is updatable.
    **/
    public void Var092()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetConcurrency (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetConcurrency() - Should return the correct value
    on a closed connection.
    **/
    public void Var093()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd2_.supportsResultSetConcurrency (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY) == true);
            }
            catch (Exception e)  {
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else {

                failed (e, "Unexpected Exception");
              }
            }
        }
    }



    /**
    supportsResultSetType() - Should throw an exception when the
    type is not valid.
    **/
    public void Var094()
    {
        if (checkJdbc20 ()) {
            try {
                dmd_.supportsResultSetType (-99);
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.sql.SQLException");
            }
        }
    }



    /**
    supportsResultSetType() - Should return the correct value
    when the type is forward only.
    **/
    public void Var095()
    {
        if (checkJdbc20 ()) {
            try {
                assertCondition (dmd_.supportsResultSetType (ResultSet.TYPE_FORWARD_ONLY) == true);
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetType() - Should return the correct value
    when the type is scroll insensitive.
    **/
    public void Var096()
    {
        if (checkJdbc20 ()) {
            try {
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    assertCondition (dmd_.supportsResultSetType (ResultSet.TYPE_SCROLL_INSENSITIVE) == false);
		} else { 
		    assertCondition (dmd_.supportsResultSetType (ResultSet.TYPE_SCROLL_INSENSITIVE) == true);
		}
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetType() - Should return the correct value
    when the type is scroll sensitive.
    **/
    public void Var097()
    {
        if (checkJdbc20 ()) {
            try {
		if (getDriver() == JDTestDriver.DRIVER_JTOPENLITE) {
		    assertCondition (dmd_.supportsResultSetType (ResultSet.TYPE_SCROLL_SENSITIVE) == false);
		} else { 
		    assertCondition (dmd_.supportsResultSetType (ResultSet.TYPE_SCROLL_SENSITIVE) == true);
		}
            }
            catch (Exception e)  {
                failed (e, "Unexpected Exception");
            }
        }
    }



    /**
    supportsResultSetType() - Should return the correct value
    on a closed connection.
    **/
    public void Var098() {
    if (checkJdbc20()) {
      try {
        assertCondition(dmd2_
            .supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY) == true);
      } catch (Exception e) {
        if (getDriver() == JDTestDriver.DRIVER_JCC) {
          // JCC throws exception on closed connection
          assertExceptionIsInstanceOf(e, "java.sql.SQLException");
        } else {
          failed(e, "Unexpected Exception");
        }
      }
    }
  }



    /**
     * supportsSchemasInDataManipulation() - Should return the correct value
     * when the connection is open.
     */
    public void Var099()
    {
        try {
            assertCondition (dmd_.supportsSchemasInDataManipulation () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSchemasInDataManipulation() - Should return the correct value
    when the connection is closed.
    **/
    public void Var100()
    {
        try {
            assertCondition (dmd2_.supportsSchemasInDataManipulation () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSchemasInIndexDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var101()
    {
        try {
            assertCondition (dmd_.supportsSchemasInIndexDefinitions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSchemasInIndexDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var102()
    {
        try {
            assertCondition (dmd2_.supportsSchemasInIndexDefinitions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSchemasInPrivilegeDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var103()
    {
        try {
            assertCondition (dmd_.supportsSchemasInPrivilegeDefinitions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSchemasInPrivilegeDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var104()
    {
        try {
            assertCondition (dmd2_.supportsSchemasInPrivilegeDefinitions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSchemasInProcedureCalls() - Should return the correct value
    when the connection is open.
    **/
    public void Var105()
    {
        try {
            assertCondition (dmd_.supportsSchemasInProcedureCalls () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSchemasInProcedureCalls() - Should return the correct value
    when the connection is closed.
    **/
    public void Var106()
    {
        try {
            assertCondition (dmd2_.supportsSchemasInProcedureCalls () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSchemasInTableDefinitions() - Should return the correct value
    when the connection is open.
    **/
    public void Var107()
    {
        try {
            assertCondition (dmd_.supportsSchemasInTableDefinitions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSchemasInTableDefinitions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var108()
    {
        try {
            assertCondition (dmd2_.supportsSchemasInTableDefinitions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSelectForUpdate() - Should return the correct value
    when the connection is open.
    **/
    public void Var109()
    {
        try {
            assertCondition (dmd_.supportsSelectForUpdate () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSelectForUpdate() - Should return the correct value
    when the connection is closed.
    **/
    public void Var110()
    {
        try {
            assertCondition (dmd2_.supportsSelectForUpdate () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsStoredProcedures() - Should return the correct value
    when the connection is open.
    **/
    public void Var111()
    {
        try {
            assertCondition (dmd_.supportsStoredProcedures () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsStoredProcedures() - Should return the correct value
    when the connection is closed.
    **/
    public void Var112()
    {
        try {
            assertCondition (dmd2_.supportsStoredProcedures () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
           failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSubqueriesInComparisons() - Should return the correct value
    when the connection is open.
    **/
    public void Var113()
    {
        try {
            assertCondition (dmd_.supportsSubqueriesInComparisons () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSubqueriesInComparisons() - Should return the correct value
    when the connection is closed.
    **/
    public void Var114()
    {
        try {
            assertCondition (dmd2_.supportsSubqueriesInComparisons () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
           failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSubqueriesInExists() - Should return the correct value
    when the connection is open.
    **/
    public void Var115()
    {
        try {
            assertCondition (dmd_.supportsSubqueriesInExists () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSubqueriesInExists() - Should return the correct value
    when the connection is closed.
    **/
    public void Var116()
    {
        try {
            assertCondition (dmd2_.supportsSubqueriesInExists () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
           failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSubqueriesInIns() - Should return the correct value
    when the connection is open.
    **/
    public void Var117()
    {
        try {
            assertCondition (dmd_.supportsSubqueriesInIns () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSubqueriesInIns() - Should return the correct value
    when the connection is closed.
    **/
    public void Var118()
    {
        try {
            assertCondition (dmd2_.supportsSubqueriesInIns () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsSubqueriesInQuantifieds() - Should return the correct value
    when the connection is open.
    **/
    public void Var119()
    {
        try {
            assertCondition (dmd_.supportsSubqueriesInQuantifieds () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsSubqueriesInQuantifieds() - Should return the correct value
    when the connection is closed.
    **/
    public void Var120()
    {
        try {
            assertCondition (dmd2_.supportsSubqueriesInQuantifieds () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsTableCorrelationNames() - Should return the correct value
    when the connection is open.
    **/
    public void Var121()
    {
        try {
            assertCondition (dmd_.supportsTableCorrelationNames () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTableCorrelationNames() - Should return the correct value
    when the connection is closed.
    **/
    public void Var122()
    {
        try {
            assertCondition (dmd2_.supportsTableCorrelationNames () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should throw an exception when the
    value is not valid.
    **/
    public void Var123()
    {
        try {
            dmd_.supportsTransactionIsolationLevel (-99);
            failed ("Didn't throw SQLException");
        }
        catch (Exception e) {
            assertExceptionIsInstanceOf (e, "java.sql.SQLException");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return the correct
    value for none.
    **/
    public void Var124()
    {
        try {
            if(getDriver() == JDTestDriver.DRIVER_NATIVE || getDriver() == JDTestDriver.DRIVER_JTOPENLITE)
                assertCondition (dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_NONE) == true);
            else
            {
                if(dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_NONE) == false)
                    succeeded();
                else
                    failed("Unexpected Results:  Expected false for supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE), recieved " + dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_NONE)); 
            }
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return the correct
    value for read uncommitted.
    **/
    public void Var125()
    {
        try {
            assertCondition (dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_READ_UNCOMMITTED) == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return the correct
    value for read committed.
    **/
    public void Var126()
    {
        try {
            assertCondition (dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_READ_COMMITTED) == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return the correct
    value for repeatable read.
    **/
    public void Var127()
    {
        try {
            assertCondition (dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_REPEATABLE_READ) == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return the correct
    value for serializable.
    **/
    public void Var128()
    {
        try {
            assertCondition (dmd_.supportsTransactionIsolationLevel (Connection.TRANSACTION_SERIALIZABLE) == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactionIsolationLevel() - Should return true for on a closed connection.
    **/
    public void Var129()
    {
        try {
            assertCondition (dmd2_.supportsTransactionIsolationLevel (Connection.TRANSACTION_READ_COMMITTED) == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsTransactions() - Should return the correct value
    when the connection is open.
    **/
    public void Var130()
    {
        try {
            assertCondition (dmd_.supportsTransactions () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsTransactions() - Should return the correct value
    when the connection is closed.
    **/
    public void Var131()
    {
        try {
            assertCondition (dmd2_.supportsTransactions () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsUnion() - Should return the correct value
    when the connection is open.
    **/
    public void Var132()
    {
        try {
            assertCondition (dmd_.supportsUnion () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsUnion() - Should return the correct value
    when the connection is closed.
    **/
    public void Var133()
    {
        try {
            assertCondition (dmd2_.supportsUnion () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
           failed (e, "Unexpected Exception");
          }
        }
    }



    /**
    supportsUnionAll() - Should return the correct value
    when the connection is open.
    **/
    public void Var134()
    {
        try {
            assertCondition (dmd_.supportsUnionAll () == true);
        }
        catch (Exception e)  {
            failed (e, "Unexpected Exception");
        }
    }



    /**
    supportsUnionAll() - Should return the correct value
    when the connection is closed.
    **/
    public void Var135()
    {
        try {
            assertCondition (dmd2_.supportsUnionAll () == true);
        }
        catch (Exception e)  {
          if (getDriver() == JDTestDriver.DRIVER_JCC) {
            // JCC throws exception on closed connection
            assertExceptionIsInstanceOf(e, "java.sql.SQLException");
          } else {
            failed (e, "Unexpected Exception");
          }
        }
    }

    /**
    supportsSavepoints() - Should return the correct value
    when the connection is open.
    **/
    public void Var136()                                                                    
    {                                                           
        if (getJdbcLevel() == 3)                                   
        {                                                         
            try 
            {                                                         
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
                  assertCondition (dmd_.supportsSavepoints () == false);  
               else
                  assertCondition (dmd_.supportsSavepoints () == true);  
            }                                             
            catch (Exception e)  {                        
                failed (e, "Unexpected Exception");             
            }                                                                             
        }   
        else
           notApplicable("Savepoints Variation."); 
    }                                                   



    /**
    supportsSavepoints() - Should return the correct value
    when the connection is closed.
    **/
    public void Var137()                                                                    
    {                                                                                                   
        if (getJdbcLevel() == 3){                                                       
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)
                  assertCondition (dmd2_.supportsSavepoints () == false);  
               else
                  assertCondition (dmd2_.supportsSavepoints () == true);  
            }                                                                               
            catch (Exception e)  {                                                          
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else {
                failed (e, "Unexpected Exception");   
              }
            }                                               
        }
        else
           notApplicable("Savepoints Variation."); 
    }               

    /**
    supportsNamedParameters() - Should return the correct value
    when the connection is open.                                                           
    **/                                                                                    
    public void Var138()                                                                   
    {                                                  
        if (getJdbcLevel() == 3){                 
            try {                                                
                assertCondition (dmd_.supportsNamedParameters () == true);  
            }                                                             
            catch (Exception e)  {                                      
                failed (e, "Unexpected Exception");                       
            }                                                                 
        }
        else
           notApplicable("Named Parameter Variation."); 
    }



    /**
    supportsNamedParameters() - Should return the correct value
    when the connection is closed.
    **/
    public void Var139()                                                             
    {                                                                              
        if (getJdbcLevel() == 3){                                 
            try {                                                                 
                assertCondition (dmd2_.supportsNamedParameters () == true);         
            }                                                                        
            catch (Exception e)  {                                                          
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else {
                failed (e, "Unexpected Exception");
              }
            }                                                                               
        }     
        else
           notApplicable("Named Parameter Variation."); 
    }                                                                                       


    /**
    supportsMultipleOpenResults() - Should return the correct value
    when the connection is open.
    **/
    public void Var140()                                                                        
    {                                                                                           
        if (getJdbcLevel() == 3)                                                                       
        {                                                                                                                                    
                try {                                                                           
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)                            
                        assertCondition (dmd_.supportsMultipleOpenResults () == false);          
                    else                                                                        
                        assertCondition (dmd_.supportsMultipleOpenResults () == true);         
                }                                                                               
                catch (Exception e)  {                                                          
                    failed (e, "Unexpected Exception");                                         
                }                                                                                                    
        } 
        else
           notApplicable("Multiple Open Result Set Variation."); 
    }                                                                                           


    /**
    supportsMultipleOpenResults() - Should return the correct value
    when the connection is closed.
    **/
    public void Var141()                                                                        
    {                                                                                                                                                                
            if (getJdbcLevel() == 3)                                                 
            {                                                                                   
                try {                                                                           
                    if (getDriver () == JDTestDriver.DRIVER_TOOLBOX)                            
                        assertCondition (dmd2_.supportsMultipleOpenResults () == false);         
                    else                                                                        
                        assertCondition (dmd2_.supportsMultipleOpenResults () == true);        
                }                                                                               
                catch (Exception e)  {                                                          
                  if (getDriver() == JDTestDriver.DRIVER_JCC) {
                    // JCC throws exception on closed connection
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                  } else {
                    failed (e, "Unexpected Exception");    
                  }
                }                                                                               
            }  
           else
           notApplicable("Multiple Open Result Set Variation.");                                                                                 
    }                                                                                           

    /**
    supportsGetGeneratedKeys() - Should return the correct value
    when the connection is open.
    **/
    public void Var142()                                                                        
    {                                                                                                   
        if (getJdbcLevel() == 3)
        {
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)   
                  assertCondition (dmd_.supportsGetGeneratedKeys () == false);
               else
                  assertCondition (dmd_.supportsGetGeneratedKeys () == true);
            }
            catch (Exception e)  {                                                          
                failed (e, "Unexpected Exception");                                         
            }
        }
        else
            notApplicable("Generated Keys Variation");                                                                                                                   
    }                                                                                           



    /**
    supportsGetGeneratedKeys() - Should return the correct value
    when the connection is closed.
    **/
    public void Var143()                                                                        
    {                                                                                           
        if (getJdbcLevel() == 3)
        {                                               
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)                   
                 assertCondition (dmd2_.supportsGetGeneratedKeys () == false);                
               else
                 assertCondition (dmd2_.supportsGetGeneratedKeys () == true);                
            }
            catch (Exception e)  {                                                          
                failed (e, "Unexpected Exception");                                         
            }
        }
        else
            notApplicable("Generated Keys Variation");                            
    }                                                                                           

    /**
    supportsResultSetHoldability()- Should return the correct value
    when the connection is open.
    **/
    public void Var144()                                                                    
    {                                                                                       
        if (getJdbcLevel() == 3){                                                
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)                   
                  assertCondition (dmd_.supportsResultSetHoldability (ResultSet.HOLD_CURSORS_OVER_COMMIT) == false);                 
               else
                  assertCondition (dmd_.supportsResultSetHoldability (ResultSet.HOLD_CURSORS_OVER_COMMIT) == true);                 
            }
            catch (Exception e)  {                                                          
                failed (e, "Unexpected Exception");                                         
            }
        }
        else
            notApplicable("Cursor Holdability Variation");  
    }                                                                                       



    /**
    supportsResultSetHoldability() - Should return the correct value
    when the connection is closed.
    **/
    public void Var145()                                                                    
    {                                                                                             
        if (getJdbcLevel() == 3){                                                
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)                   
                  assertCondition (dmd2_.supportsResultSetHoldability (ResultSet.HOLD_CURSORS_OVER_COMMIT) == false);                 
               else
                  assertCondition (dmd2_.supportsResultSetHoldability (ResultSet.HOLD_CURSORS_OVER_COMMIT) == true);                 
            }
            catch (Exception e)  {  
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else {

                failed (e, "Unexpected Exception");     
              }
            }
        }
      else
            notApplicable("Cursor Holdability Variation");  
    } 
        

    /**
    supportsResultSetHoldability()- Should return the correct value
    when the connection is open.
    **/
    public void Var146()                                                                    
    {                                                                                       
        if (getJdbcLevel() == 3){                                                
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)                   
                  assertCondition (dmd_.supportsResultSetHoldability (ResultSet.CLOSE_CURSORS_AT_COMMIT) == false);                 
               else
                  assertCondition (dmd_.supportsResultSetHoldability (ResultSet.CLOSE_CURSORS_AT_COMMIT) == true);                 
            }
            catch (Exception e)  {                                                          
                failed (e, "Unexpected Exception");                                         
            }
        }
     else
            notApplicable("Cursor Holdability Variation");  
    }                                                                                       



    /**
    supportsResultSetHoldability() - Should return the correct value
    when the connection is closed.
    **/
    public void Var147()                                                                    
    {                                                                                       
        if (getJdbcLevel() == 3){                                                
            try 
            {                                                                           
               if (testDriver_.getRelease() < JDTestDriver.RELEASE_V5R2M0)                   
                 assertCondition (dmd2_.supportsResultSetHoldability (ResultSet.CLOSE_CURSORS_AT_COMMIT) == false);                 
               else
                 assertCondition (dmd2_.supportsResultSetHoldability (ResultSet.CLOSE_CURSORS_AT_COMMIT) == true);                 
            }
            catch (Exception e)  {                                                          
              if (getDriver() == JDTestDriver.DRIVER_JCC) {
                // JCC throws exception on closed connection
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
              } else {
                failed (e, "Unexpected Exception");    
              }
            }
        }
        else
            notApplicable("Cursor Holdability Variation");  
    }                                                                                       
}
