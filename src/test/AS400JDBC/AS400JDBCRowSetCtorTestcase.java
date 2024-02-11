///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCRowSetCtorTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.AS400JDBC;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;

import javax.naming.*;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.AS400JDBCRowSet;
import com.ibm.as400.access.ExtendedIllegalStateException;

import test.PasswordVault;
import test.Testcase;

//import javasoft.sqe.tests.ibm.jdbc.JDSetupCollection;    //@A5A
import com.ibm.as400.access.CommandCall;                 //@A5A

/**
  Testcase AS400JDBCRowSetTestcase.
 **/
public class AS400JDBCRowSetCtorTestcase extends Testcase
{
    private static final String JNDI_FILE = "com.sun.jndi.fscontext.RefFSContextFactory";
    private static final String JNDI_LDAP = "com.ibm.jndi.LDAPCtxFactory";
    private static final String TEST_FILE_NAME = "/tmp/AS400JDBCRowSetCtorTestcase.file"; 
    private AS400JDBCDataSource dataSource_;
    private static final String logDirectory_ = "javatest";    //@A3A
    private String dataSourceName_ = logDirectory_ + "/myds";  //@A3C //"/jdbc/myds"
    private String collection_ = "ROWSET";
    private String dbaseName_ = collection_ + ".JTBOX"; //"QGPL.JTBOX";
    private Context context_;
    private PrintWriter print = null;
    private File testFile_;
    private File javatest_; //@A4A
    private String tag = null;
    private boolean rejectChange = false;
    private Random random_;
    private Blob blob_;
    private Clob clob_;
    private Ref ref_;
    private Array array_;
    private String jndiType_ = JNDI_FILE;
    private String authorityUsr_ = null;   //@A2A
    private String authorityPwd_ = null;   //@A2A

    private AS400JDBCRowSet rowset_;


    /**
      Constructor.  This is called from the AS400JDBCRowSetTestcase constructor.
     **/
    public AS400JDBCRowSetCtorTestcase(AS400 systemObject,
                                       Vector variationsToRun,
                                       int runMode,
                                       FileOutputStream fileOutputStream,
                                       
                                       String password,
                                       String jndiType,
                                       String authorityUsr,   //@A2A
                                       String authorityPwd)   //@A2A
    {
        super(systemObject, "AS400JDBCRowSetCtorTestcase", variationsToRun,
              runMode, fileOutputStream, password);
        if (jndiType == null)
            throw new NullPointerException("-misc parameter is missing.");

        if (jndiType.equals("file"))
            jndiType_ = JNDI_FILE;
        else if (jndiType.equals("ldap"))
            jndiType_ = JNDI_LDAP;
        else
            System.out.println("WARNING... Unknown jndi type '" + jndiType + "' using default.");
        authorityUsr_ = authorityUsr;  //@A2A
        authorityPwd_ = authorityPwd;  //@A2A
    }


    public void cleanup()
    {
        try
        {
            System.gc();  //@A4A Garbage collection so that file can be deleted.

            if (!testFile_.delete()) //@A4C
                System.out.println("WARNING... testcase cleanup could not delete: "+TEST_FILE_NAME); //@A4A
            if (!javatest_.delete())   //@A4A
                System.out.println("WARNING... testcase cleanup could not delete: " + logDirectory_); //@A4A         
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            dataSource_ = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
            Connection c = dataSource_.getConnection();

            Statement s = c.createStatement();
            s.executeUpdate("DROP TABLE " + dbaseName_);
            s.close();
            c.close();
        }
        catch (Exception e)
        {
            System.out.println("AS400JDBCRowSetTestcase cleanup FAILED!");
            e.printStackTrace();
        }
    }

    /**
    *  Testcase setup.
    **/
    public void setup()
    {
        // Make sure javatest dir exists.					          //@A3A
        javatest_ = new File(logDirectory_); //A4C                                         //@A3A
        if (!javatest_.exists())                                                           //@A3A
        {                                                                                  //@A3A
            System.out.println("Setup is creating '" + logDirectory_ + "' directory.");     //@A3A
            //@A3A
            if (!javatest_.mkdir())                                                          //@A3A
            {                                                                               //@A3A
                System.out.println("WARNING:  Setup could not create the '" + logDirectory_ + "' directory."); //@A3A
            }                                                                               //@A3A
        }

        // Get the JNDI Initial Context.
        Properties env = new Properties();

        if (jndiType_ == JNDI_LDAP)                                                          //@A2A
        {                                                                                    //@A2A
            env.put("java.naming.provider.url", "ldap://" + systemObject_.getSystemName());   //@A2A
            env.put("java.naming.security.principal", "cn=" + authorityUsr_);                      //@A2A
            env.put("java.naming.security.credentials", authorityPwd_);                            //@A2A
        }                                                                                    //@A2A
        else
        {
        	env.put(Context.PROVIDER_URL,"file:.");  //@pda for linux authority. default is "/" location of file created
            System.setProperty(Context.PROVIDER_URL,"file:.");  //@pda for linux authority. default is "/" location of file created
        }
        
        env.put(Context.INITIAL_CONTEXT_FACTORY, jndiType_);
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, jndiType_);  //@A1A
        

        try
        {
            context_ = new InitialContext(env);
        }
        catch (Exception cxt)
        {
            System.out.println("ERROR: testcase setup failed to initialize the JNDI context.");
            cxt.printStackTrace();
        }

        random_ = new Random();
        createTestDatabase();

        try
        {
            testFile_= new File(TEST_FILE_NAME);
            testFile_.createNewFile();
        }
        catch (IOException e)
        {
	    System.out.println("Exception creating "+TEST_FILE_NAME); 
            e.printStackTrace();
        }
    }

    /**
    *  Setup to create a Blob used by the testcases.
    **/
    private void setupCreateBlob()
    {
        blob_ = new TestBlob("TEST DATA IN BLOB");   
    }

    class TestBlob implements Blob
    {
        private String data_;

        public TestBlob(String data)
        {
            data_ = data;
        }
        public InputStream getBinaryStream()
        {
            return null;
        }
        public byte[] getBytes(long position, int length)
        {
            byte[] value = null;
            return value;
        }
        public long length()
        {
            Integer size = new Integer(data_.length());
            return size.longValue();
        }
        public long position(Blob pattern, long start)
        {
            return 0;   
        }
        public long position(byte[] pattern, long start)
        {
            return 0;
        }
        public int setBytes(long pos, byte[] bytes)                        //@B1A
        throws SQLException                                                //@B1A
        {                                                                  //@B1A
            // add code to test new methods                                //@B1A
            return 0;                                                      //@B1A
        }                                                                  //@B1A
        public int setBytes(long pos, byte[] bytes, int offest, int len)   //@B1A
        throws SQLException                                                //@B1A
        {                                                                  //@B1A
            // add code to test new methods                                //@B1A
            return 0;                                                      //@B1A
        }                                                                  //@B1A
        public OutputStream setBinaryStream(long pos)                      //@B1A
        throws SQLException                                                //@B1A
        {                                                                  //@B1A
            // add code to test new methods                                //@B1A
            return null;                                                   //@B1A
        }                                                                  //@B1A
        public void truncate(long len)                                     //@B1A
        throws SQLException                                                //@B1A
        {                                                                  //@B1A
            // add code to test new methods                                //@B1A
        }                                                                  //@B1A
        public void free() throws SQLException {
          // TODO Auto-generated method stub
          
        }
        public InputStream getBinaryStream(long pos, long length)
            throws SQLException {
          // TODO Auto-generated method stub
          return null;
        }

	
	
    }

    /**
    *  Setup to create a java.sql.Clob object used by the testcases.
    **/
    private void setupCreateClob()
    {
        clob_ = new TestClob("THIS IS A TEST CLOB WITH SOME MISC INFORMATION.");
    }
    class TestClob implements Clob
    {
        private String data_;

        public TestClob(String data)
        {
            data_ = data;
        }
        public InputStream getAsciiStream()
        {
            return null;
        }
        public Reader getCharacterStream()
        {
            return null;
        }
        public String getSubString(long position, int length)
        {
            return "";
        }
        public long length()
        {
            Integer size = new Integer(data_.length());
            return size.longValue();
        }
        public long position(Clob searchString, long start)
        {
            return 0;   
        }
        public long position(String searchString, long start)
        {
            return 0;
        }
        public int setString(long pos, String str)                        //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            return 0;                                                     //@B1A
        }                                                                 //@B1A
        public int setString(long pos, String str, int offest, int len)   //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
            return 0;                                                     //@B1A
        }                                                                 //@B1A
        public OutputStream setAsciiStream(long pos)                      //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
            return null;                                                  //@B1A
        }                                                                 //@B1A
        public Writer setCharacterStream(long pos)                        //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
            return null;                                                  //@B1A
        }                                                                 //@B1A
        public void truncate(long len)                                    //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
        }                                                                 //@B1A
        public void free() throws SQLException {
          // TODO Auto-generated method stub
          
        }
        public Reader getCharacterStream(long pos, long length)
            throws SQLException {
          // TODO Auto-generated method stub
          return null;
        }
    }

    /**
    *  Setup to create a java.sql.Ref object used by the testcases.
    **/
    private void setupCreateRef()
    {
        ref_ = new TestRef();
    }
    class TestRef implements Ref
    {
        public TestRef()
        {

        }
        public String getBaseTypeName()
        {
            return "Test Ref";
        }
        public java.lang.Object getObject(java.util.Map map)              //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
            return null;                                                  //@B1A
        }                                                                 //@B1A
        public java.lang.Object getObject()                               //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
            return null;                                                  //@B1A
        }                                                                 //@B1A
        public void setObject(java.lang.Object value)                     //@B1A
        throws SQLException                                               //@B1A
        {                                                                 //@B1A
            // add code to test new methods                               //@B1A
        }                                                                 //@B1A
    }

    /**
    *  Setup to create an java.sql.Array used by the testcases.
    **/
    private void setupCreateArray()
    {
        array_ = new TestArray();
    }
    class TestArray implements Array
    {
        public TestArray()
        {

        }
        public Object getArray()
        {
            return null;
        }
        public Object getArray(long pos, int count)
        {
            return null;
        }
        public Object getArray(long pos, int count, Map map)
        {
            return null;
        }
        public Object getArray(Map map)
        {
            return null;
        }
        public int getBaseType()
        {
            return Types.INTEGER;
        }
        public String getBaseTypeName()
        {
            return "java.lang.Integer";
        }
        public ResultSet getResultSet()
        {
            return null;
        }
        public ResultSet getResultSet(long pos, int count)
        {
            return null;
        }
        public ResultSet getResultSet(long pos, int count, Map map)
        {
            return null;
        }
        public ResultSet getResultSet(Map map)
        {
            return null;
        }
        public void free() throws SQLException {
          // TODO Auto-generated method stub
          
        }
    }

    /**
    *  Setup to create a test database used by the testcases.
    **/
    private void createTestDatabase()
    {
        String[] states = {"AL", "AK", "AS", "AZ", "CA", "CO", "CT", "DE", "FL", "GA", 
            "HI", "IA", "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", 
            "ME", "MI", "MN", "MO", "MS", "MT", "NC", "ND", "NE", "NH", 
            "NJ", "NM", "NV", "NY", "OH", "OR", "OK", "PA", "RI", "SC", 
            "SD", "TN", "TX", "UT", "VA", "VT", "WA", "WI", "WV", "WY"};

        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            dataSource_ = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);

            if (jndiType_ == JNDI_LDAP)                                      //@A2A
                dataSourceName_ = "cn=test123datasource, cn=users, ou=rochester,o=ibm,c=us";  //@A2A
            dataSource_.setDataSourceName(dataSourceName_);

            try
            {
                context_.bind(dataSourceName_, dataSource_);
            }
            catch (Exception be)
            { 
                context_.unbind(dataSourceName_);
                try
                {
                    context_.bind(dataSourceName_, dataSource_);
                }
                catch (Exception bee)
                {
                    System.out.println("Data source binding error.");
                    bee.printStackTrace();
                }
            }
            Connection c = dataSource_.getConnection();
            Statement s = c.createStatement();

            boolean created = false;                              //@A5A
            try {                                                                     //@A5A
                s.executeUpdate ("CREATE COLLECTION " + collection_);                  //@A5A
                created = true;                                                        //@A5A
            }                                                                         //@A5A
            catch (SQLException e) {                                                  //@A5A
                // The collection already exists.                                      //@A5A
                created = false;                                                       //@A5A
            }                                                                         //@A5A
            //@A5A
            // We need to grant object authority to the                               //@A5A
            // collection and the collection's journal so that                        //@A5A
            // other users may create tables in it.                                   //@A5A
            if (created) {                                                            //@A5A 
                AS400 authoritySys = new AS400(systemObject_.getSystemName(),     //@A5A
                                               authorityUsr_, authorityPwd_.toCharArray());     //@A5A
                CommandCall cmd = new CommandCall (authoritySys);                     //@A5A
                cmd.run ("GRTOBJAUT OBJ(QSYS/" + collection_ + ") OBJTYPE(*LIB) "     //@A5A
                         + "USER(*PUBLIC) AUT(*ALL)");                                //@A5A
                cmd.run ("GRTOBJAUT OBJ(" + collection_ + "/QSQJRN) OBJTYPE(*JRN) "   //@A5A
                         + "USER(*PUBLIC) AUT(*ALL)");                                //@A5A
            }                                                                         //@A5A

            //@A5Dtry
            //@A5D{
            //@A5D   s.executeUpdate("CREATE COLLECTION " + collection_);
            //@A5D}
            //@A5Dcatch (SQLException cc)
            //@A5D{
            //@A5D   /* ignore */
            //@A5D}
            s.executeUpdate("CREATE TABLE " + dbaseName_ + " (NAME VARCHAR(10), ID INTEGER, STATE VARCHAR(2), WORTH DOUBLE)");
            s.close();

            PreparedStatement ps = c.prepareStatement("INSERT INTO " + dbaseName_ + " (NAME, ID, STATE, WORTH) VALUES (?,?,?,?)");
            for (int i=0; i< 50; i++)
            {
                ps.setString(1, "Customer" + i);
                ps.setInt(2, 100000 + i);
                ps.setString(3, states[i]);
                ps.setDouble(4, getPurchases("10000000"));
                ps.executeUpdate();
            }
            ps.close();
            c.close();
        }
        catch (Exception e)
        {
            System.out.println("AS400JDBCRowSetCtorTestcase setup FAILED!");   //@A5C
            e.printStackTrace();
        }
    }


    private double getPurchases(String account)
    {
        Integer dollar = new Integer(random_.nextInt(new Integer(account).intValue()));
        Integer cents = new Integer(random_.nextInt(100));

        String purchases = dollar + "." + cents;
        return new Double(purchases).doubleValue();
    }

    /**
    *  Validates that AS400JDBCRowSet takes a JDBC url and can update the database.
    *
    *  AS400JDBCRowSet(String)
    *  AS400JDBCRowSet.setCommand()
    *  AS400JDBCRowSet.setConcurrency()
    *  AS400JDBCRowSet.execute()
    *  AS400JDBCRowSet.next()
    *  AS400JDBCRowSet.getString()
    *  AS400JDBCRowSet.getDouble()
    *  AS400JDBCRowSet.updateDouble()
    *  AS400JDBCRowSet.updateRow()
    **/
    public void Var001()
    {
       if(true){
            notApplicable("Broken again... todo when time permits.");
            return;
       }
        AS400JDBCRowSet rowset = null;
        try
        {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            rowset = new AS400JDBCRowSet("jdbc:as400://" + systemObject_.getSystemName() , systemObject_.getUserId() , charPassword);
   PasswordVault.clearPassword(charPassword);
            if (jndiType_ == JNDI_LDAP)     //@A2A
                rowset.setContext(context_); //@A2A

            // Set the command used to populate the list.
            rowset.setConcurrency(ResultSet.CONCUR_UPDATABLE);
            rowset.setCommand("SELECT * FROM " + dbaseName_ + " FOR UPDATE");

            // Populate the rowset.
            rowset.execute();

            // Update the customer balances.
            while (rowset.next())
            {
                double newBalance = rowset.getDouble("WORTH") + getPurchases(rowset.getString("ID"));
                rowset.updateDouble("WORTH", newBalance);
                rowset.updateRow();
            }
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception");
        }
        finally
        {
            try { 
                rowset.close();
            }
            catch (SQLException sq) { 
                sq.printStackTrace();
            }
        }
    }

    /**
    *  Validates that the RowSet can take a dataSource and execute a PreparedStatement.
    *
    *  AS400JDBCRowSet()
    *  AS400JDBCRowSet.setDataSource()
    *  AS400JDBCRowSet.setCommand()
    *  AS400JDBCRowSet.setString()
    *  AS400JDBDRowSet.setDouble()
    *  AS400JDBCRowSet.execute()
    **/
    public void Var002()
    {
        if(true){
            notApplicable("Broken again... todo when time permits.");
            return;
       }
        double MINIMUM_LIMIT = 1000.00;

        AS400JDBCRowSet rowset = null;
        try
        {
            rowset = new AS400JDBCRowSet();
            if (jndiType_ == JNDI_LDAP)     //@A2A
                rowset.setContext(context_); //@A2A
            rowset.setDataSourceName(dataSourceName_);
            rowset.setUsername(systemObject_.getUserId());
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
            rowset.setPassword(charPassword);
   PasswordVault.clearPassword(charPassword);

            // Set the prepared statement and initialize the parameters.
            rowset.setCommand("SELECT * FROM QIWS.QCUSTCDT WHERE STATE = ? AND BALDUE > ?");
            rowset.setString(1, "MN");
            rowset.setDouble(2, MINIMUM_LIMIT);

            // Populate the rowset.
            rowset.execute();

            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
        finally
        {
            try { 
                rowset.close();
            }
            catch (SQLException sq) { 
                sq.printStackTrace();
            }
        }
    }

    /**
    *  Validates that absolute(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var003()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.absolute(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that afterLast() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var004()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.afterLast();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that beforeFirst() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var005()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.beforeFirst();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that cancelRowUpdates() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var006()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.cancelRowUpdates();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getWarnings() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var007()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getWarnings();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that clearWarnings() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var008()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.clearWarnings();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that close() does not throw an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var009()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.close();         

            succeeded();     //@A1C Changed to not expect an exception.
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that deleteRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var010()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.deleteRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that findColumn(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var011()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            if (jndiType_ == JNDI_LDAP)     //@A2A
                rowset.setContext(context_); //@A2A
            rowset.findColumn("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that first() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var012()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            if (jndiType_ == JNDI_LDAP)     //@A2A
                rowset.setContext(context_); //@A2A
            rowset.first();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getArray(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var013()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getArray(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getArray(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var014()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getArray("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getAsciiStream(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var015()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getAsciiStream(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getAsciiStream(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var016()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getAsciiStream("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    /**
    *  Validates that getBigDecimal(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var017()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBigDecimal(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBigDecimal(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var018()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBigDecimal("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBigDecimal(int,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var019()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBigDecimal(1,1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBigDecimal(String,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var020()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBigDecimal("A",1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBinaryStream(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var021()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBinaryStream(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBinaryStream(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var022()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBinaryStream("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBlob(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var023()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBlob(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBlob(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var024()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBlob("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBoolean(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var025()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBoolean(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBoolean(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var026()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBoolean("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that getByte(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var027()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getByte(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getByte(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var028()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getByte("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBytes(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var029()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBytes(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getBytes(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var030()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getBytes("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getCharacterStream(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var031()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getCharacterStream(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getCharacterStream(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var032()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getCharacterStream("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getClob(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var033()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getClob(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getClob(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var034()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getClob("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getCursorName() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var035()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getCursorName();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDate(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var036()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDate(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDate(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var037()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDate("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDate(int,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var038()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDate(1, Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDate(String,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var039()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDate("A", Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDouble(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var040()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDouble(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getDouble(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var041()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getDouble("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getFloat(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var042()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getFloat(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getFloat(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var043()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getFloat("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getInt(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var044()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getInt(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getInt(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var045()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getInt("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getLong(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var046()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getLong(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getLong(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var047()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getLong("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getMetaData() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var048()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getMetaData();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getObject(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var049()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getObject(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getObject(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var050()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getObject("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getObject(int,Map) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var051()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getObject(1, new Hashtable());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getObject(String, Map) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var052()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getObject("A", new Hashtable());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getRef(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var053()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getRef(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getRef(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var054()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getRef("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var055()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that insertRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var056()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.insertRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getShort(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var057()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getShort(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getShort(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var058()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getShort("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getStatement() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var059()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getStatement();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getString(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var060()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getString(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getString(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var061()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getString("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTime(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var062()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTime(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTime(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var063()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTime("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTime(int,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var064()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTime(1, Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTime(String,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var065()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTime("A", Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTimestamp(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var066()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTimestamp(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTimestamp(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var067()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTimestamp("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTimestamp(int,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var068()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTimestamp(1, Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTimestamp(String,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var069()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getTimestamp("A", Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getUnicodeStream(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var070()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getUnicodeStream(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getUnicodeStream(String) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var071()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getUnicodeStream("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that isAfterLast() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var072()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.isAfterLast();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that isBeforeFirst() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var073()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.isBeforeFirst();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that isFirst() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var074()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.isFirst();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that isLast() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var075()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.isLast();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that last() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var076()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.last();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that moveToCurrentRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var077()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.moveToCurrentRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that moveToInsertRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var078()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.moveToInsertRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that next() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var079()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.next();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that previous() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var080()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.previous();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that refreshRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var081()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.refreshRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that relative(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var082()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.relative(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that rowDeleted() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var083()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.rowDeleted();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that rowInserted() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var084()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.rowInserted();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that rowUpdated() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var085()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.rowUpdated();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that updateAsciiStream(int,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var086()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateAsciiStream(1, new FileInputStream(testFile_),1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateAsciiStream(String,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var087()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateAsciiStream("A", new FileInputStream(testFile_),1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that updateBigDecimal(int,BigDecimal) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var088()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBigDecimal(1, new BigDecimal("10"));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBigDecimal(String,BigDecimal) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var089()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBigDecimal("A", new BigDecimal("10"));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBinaryStream(int,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var090()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBinaryStream(1, new FileInputStream(testFile_),1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBinaryStream(String,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var091()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBinaryStream("A", new FileInputStream(testFile_),1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBoolean(int,boolean) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var092()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBoolean(1, true);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBoolean(String,boolean) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var093()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateBoolean("A", false);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateByte(int,byte) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var094()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateByte(1, (byte)0x05);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateByte(String,byte) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var095()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateByte("A", (byte)0x09);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBytes(int,byte[]) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var096()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            byte[] b = {4,5,6,7};
            rowset.updateBytes(1, b);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateBytes(String,byte[]) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var097()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            byte[] b = {4,5,6,7};
            rowset.updateBytes("A", b);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that updateCharacterStream(int,Reader,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var098()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateCharacterStream(1, new BufferedReader(new FileReader(testFile_)), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateCharacterStream(String,Reader,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var099()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateCharacterStream("A", new BufferedReader(new FileReader(testFile_)), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateDate(int,Date) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var100()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateDate(1, new java.sql.Date(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateDate(String,Date) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var101()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateDate("A", new java.sql.Date(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateDouble(int,double) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var102()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateDouble(1, 10.25);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateDouble(String,double) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var103()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateDouble("A", 10.33);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateFloat(int,float) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var104()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateFloat(1, new Float(10.25).floatValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateFloat(String,float) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var105()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateFloat("A", new Float(10.33).floatValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateInt(int,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var106()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateInt(1, 10);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateInt(String,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var107()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateInt("A", 10);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateLong(int,long) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var108()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateLong(1, 10);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateLong(String,long) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var109()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateLong("A", 10);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateNull(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var110()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateNull(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateNull(String,long) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var111()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateNull("A");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that updateObject(int,Object) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var112()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateObject(1, new String(""));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateObject(String,Object) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var113()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateObject("A", new String(""));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateObject(int,Object,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var114()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateObject(1, new String(""), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateObject(String,Object,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var115()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateObject("A", new String(""), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateRow() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var116()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateRow();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateShort(int,short) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var117()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateShort(1, new Short("1").shortValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateShort(String,short) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var118()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateShort("A", new Short("1").shortValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateString(int,short) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var119()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateString(1, "test");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateString(String,short) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var120()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateString("A", "test");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateTime(int,Time) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var121()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateTime(1, new Time(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateTime(String,Time) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var122()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateTime("A", new Time(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateTimestamp(int,Timestamp) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var123()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateTimestamp(1, new Timestamp(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that updateTimestamp(String,Timestamp) throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var124()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.updateTimestamp("A", new Timestamp(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that wasNull() throws an ExtendedIllegalStateException if 
    *  the statement has not been executed.
    **/
    public void Var125()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.wasNull();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    /**
    *  Validates that setArray(int,Array) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var126()
    {
        if (array_ == null)
            setupCreateArray();

        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setArray(1, array_);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setAsciiStream(int,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var127()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setAsciiStream(1, new FileInputStream(testFile_), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBigDecimal(int,BigDecimal) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var128()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setBigDecimal(1, new BigDecimal("10"));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBinaryStream(int,InputStream,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var129()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setBinaryStream(1, new FileInputStream(testFile_), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBlob(int,Blob) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var130()
    {
        if (blob_ == null)
            setupCreateBlob();

        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setBlob(1, blob_);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBoolean(int,boolean) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var131()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setBoolean(1, false);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setByte(int,byte) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var132()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setByte(1, (byte)0x32);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setBytes(int,byte[]) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var133()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            byte[] bytes = { (byte)0x01, (byte)0x02, (byte)0x03};
            rowset.setBytes(1, bytes);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setCharacterStream(int,Reader,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var134()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setCharacterStream(1, new BufferedReader(new FileReader(testFile_)), 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setClob(int,Clob) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var135()
    {
        if (clob_ == null)
            setupCreateClob();

        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setClob(1, clob_);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDate(int,Date) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var136()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setDate(1, new java.sql.Date(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDate(int,Date,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var137()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setDate(1, new java.sql.Date(1), Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setDouble(int,double) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var138()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setDouble(1, 10.25);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setEscapeProcessing(boolean) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var139()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setEscapeProcessing(true);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setFetchDirection(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var140()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setFetchDirection(ResultSet.FETCH_FORWARD);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setFetchSize(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var141()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setFetchSize(1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setFloat(int,float) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var142()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setFloat(1, new Double(10.25).floatValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setInt(int,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var143()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setInt(1, 11);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setLong(int,Long) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var144()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setLong(1, new Long("10000").longValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setMaxFieldSize(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var145()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setMaxFieldSize(100);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setMaxRows(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var146()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setMaxRows(100);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setNull(int,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var147()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setNull(1, Types.INTEGER);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setNull(int,int,String) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var148()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setNull(1, Types.INTEGER, "java.lang.Integer");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setObject(int,Object) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var149()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setObject(1, new String("test"));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setObject(int,Object,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var150()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setObject(1, new String("test"), Types.CLOB);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setObject(int,Object,int,int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var151()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setObject(1, new String("test"), Types.CLOB, 1);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setQueryTimeout(int) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var152()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setQueryTimeout(100);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setRef(int,Ref) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var153()
    {
        if (ref_ == null)
            setupCreateRef();

        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setRef(1, ref_);

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setShort(int,short) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var154()
    {
        if (ref_ == null)
            setupCreateRef();

        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setShort(1, new Short("1").shortValue());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setString(int,String) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var155()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setString(1, "test");

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setTime(int,Time) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var156()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setTime(1, new Time(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setTime(int,Time,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var157()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setTime(1, new Time(1), Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setTimestamp(int,Timestamp) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var158()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setTimestamp(1, new Timestamp(1));

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that setTimestamp(int,Timestamp,Calendar) throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var159()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.setTimestamp(1, new Timestamp(1), Calendar.getInstance());

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that execute() throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var160()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.execute();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getCommand() return null if the command parameter is not set.
    **/
    public void Var161()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getCommand() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getConcurrency() initially returns ResultSet.CONCUR_READ_ONLY.
    **/
    public void Var162()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getConcurrency() == ResultSet.CONCUR_READ_ONLY);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getDataSourceName() return an empty String if the data source parameter is not set.
    **/
    public void Var163()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getDataSourceName() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getEscapeProcessing() initially returns true.
    **/
    public void Var164()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getEscapeProcessing() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getFetchDirection() initially returns ResultSet.FETCH_FORWARD.
    **/
    public void Var165()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getFetchDirection() == ResultSet.FETCH_FORWARD);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getFetchSize() initially returns zero.
    **/
    public void Var166()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getFetchSize() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getMaxFieldSize() initially returns zero.
    **/
    public void Var167()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getMaxFieldSize() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getMaxRows() initially returns zero.
    **/
    public void Var168()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getMaxRows() == 0);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getPassword() initially returns an empty String.
    **/
    public void Var169()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getPassword().equals(""));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getQueryTimeout() throws an ExtendedIllegalStateException if 
    *  the statement has not been created.
    **/
    public void Var170()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            rowset.getQueryTimeout();

            failed("Unexpected results.");
        }
        catch (ExtendedIllegalStateException is)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
    *  Validates that getTransactionIsolation() initially returns Connection.TRANSACTION_READ_UNCOMMITTED.
    **/
    public void Var171()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getType() initially returns ResultSet.TYPE_FORWARD_ONLY.
    **/
    public void Var172()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getType() == ResultSet.TYPE_FORWARD_ONLY);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getTypeMap() initially returns null.
    **/
    public void Var173()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getTypeMap() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getUrl() initially returns null.
    **/
    public void Var174()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getUrl() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that getUsername() initially returns null.
    **/
    public void Var175()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.getUsername() == null);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isReadOnly() initially returns false.
    **/
    public void Var176()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.isReadOnly() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isUseDataSource() initially returns true.
    **/
    public void Var177()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet();
            assertCondition (rowset.isUseDataSource() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(AS400JDBCDataSource) throws a NullPointerException
    *  if the data source property is null.
    **/
    public void Var178()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(null);

            failed("Unexpected results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(AS400JDBCDataSource) sets the data source.
    *  property as expected.
    **/
    public void Var179()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(dataSourceName_);

            assertCondition(rowset.getDataSourceName().equals(dataSourceName_));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isUseDataSource() returns true if the data source is set on the constructor.
    **/
    public void Var180()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(dataSourceName_);

            assertCondition(rowset.isUseDataSource() == true);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(String,String,String) throws a NullPointerException
    *  if the url parameter is null.
    **/
    public void Var181()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(null, "user", "pwd");

            failed("Unexpected Results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(String,String,String) throws a NullPointerException
    *  if the username parameter is null.
    **/
    public void Var182()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet("jdbc:as400:", null, "pwd");

            failed("Unexpected Results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(String,String,String) throws a NullPointerException
    *  if the password parameter is null.
    **/
    public void Var183()
    {
        try
        {
            AS400JDBCRowSet rowset = new AS400JDBCRowSet("jdbc:as400:", "user", (char[]) null);

            failed("Unexpected Results.");
        }
        catch (NullPointerException np)
        {
            succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that AS400JDBCRowSet(String,String,String) sets the url and username
    *  as expected.
    **/
    public void Var184()
    {
        try
        {
            String url = "jdbc:as400:";
            String username = "user";
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(url, username, "pwd");

            assertCondition(rowset.getUsername().equalsIgnoreCase(username) && rowset.getUrl().equals(url));
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }

    /**
    *  Validates that isUseDataSource() returns false if the constuctor AS400JDBCRowSet(String,String,String) is used.
    **/
    public void Var185()
    {
        try
        {
            String url = "jdbc:as400:";
            String username = "user";
            AS400JDBCRowSet rowset = new AS400JDBCRowSet(url, username, "pwd");

            assertCondition(rowset.isUseDataSource() == false);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected Exception.");
        }
    }
}
