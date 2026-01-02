///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobAccess.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD.Lob;

import com.ibm.as400.access.AS400;

import test.JDLobTest;
import test.JDSetupProcedure;
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Statement;
import java.util.Hashtable; import java.util.Vector;
import java.sql.CallableStatement;



/**
Testcase JDLobAccess.  This tests how long a input locator is accessible.
**/
public class JDLobAccess
extends JDTestcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDLobAccess";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDLobTest.main(newArgs); 
   }



    // Private data.
    private Statement           statement_;
    private String              TABLE_;
    private String              PROC_;


    /**
    Constructor.
    **/
    public JDLobAccess (AS400 systemObject,
                      Hashtable<String,Vector<String>> namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      
                      String password)
    {
        super (systemObject, "JDLobAccess",
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
        TABLE_          = JDLobTest.COLLECTION + ".LOBACCESS";
        PROC_           = JDLobTest.COLLECTION + ".LOBILACCESS";
        if (isJdbc20 ()) {
            String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) ;
            connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
	    String QIWS = JDSetupProcedure.setupQIWS(systemObject_,  connection_, output_); 
            statement_ = connection_.createStatement();
	    JDTestDriver.dropTable(statement_, TABLE_);

            statement_.executeUpdate("CREATE TABLE " + TABLE_ + "(LOBS BLOB(5), COL2 INTEGER)");
            PreparedStatement ps = connection_.prepareStatement("INSERT INTO  " + TABLE_ + " VALUES(?, ?)");
            byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
            ps.setBytes(1, bytes);
            ps.setInt(2, 1);
            ps.executeUpdate();
            ps.setBytes(1, bytes);
            ps.setInt(2, 2);
            ps.executeUpdate();
            ps.close();
            JDTestDriver.dropProcedure(statement_, PROC_); 
            String sql = "CREATE PROCEDURE " + PROC_
                + "(IN P1 BLOB, OUT P2 BLOB, INOUT P3 BLOB)"
                + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + PROC_
                + " JDLOBILA: BEGIN"
 	        + "   DECLARE C1 CURSOR FOR SELECT * FROM "+QIWS+".QCUSTCDT;"
                + "   OPEN C1;"
                + "   SET RESULT SETS CURSOR C1;"
                + "   SET P2 = P1;"
                + "   SET P3 = P1;"
                + " END JDLOBILA";
            statement_.execute(sql);
        }
    }


    /**
    Performs cleanup needed after running variations.
    
    @exception Exception If an exception occurs.
    **/
    protected void cleanup ()
    throws Exception
    {
        if (isJdbc20 ()) {
            statement_.executeUpdate ("DROP TABLE " + TABLE_);
            statement_.executeUpdate ("DROP PROCEDURE " + PROC_);
            statement_.close ();
            connection_.close ();
        }
    }

    protected boolean compareBytes(byte[] array1, byte[] array2){
        if(array1.length != array2.length)
            return false;
        for(int i=0; i<array1.length; i++)
            if(array1[0] != array2[0])
                return false;
        return true;
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var001()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";lob threshold=1";
                c = testDriver_.getConnection(url,systemObject_.getUserId(), encryptedPassword_);

                c.setAutoCommit(false);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                assertCondition(count == 2);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that uses a input locator.
    **/
    public void Var002()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs = ps.executeQuery();
                while(rs.next())
                    count++;
                assertCondition(count == 4);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var003()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                assertCondition(count == 2);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that uses a locator.
    **/
    public void Var004()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs = ps.executeQuery();
                while(rs.next())
                    count++;
                assertCondition(count == 4);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var005()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                assertCondition(count == 2);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that uses a locator.
    **/
    public void Var006()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs = ps.executeQuery();
                while(rs.next())
                    count++;
                assertCondition(count == 4);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var007()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                assertCondition(count == 2);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a statement that uses a locator.
    **/
    public void Var008()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("SELECT * FROM " + TABLE_ + " WHERE LOBS=?");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                rs = ps.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs = ps.executeQuery();
                while(rs.next())
                    count++;
                assertCondition(count == 4);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    rs.close();
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var009()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                assertCondition(count == 1);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator.
    **/
    public void Var010()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)7};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                ps.setBytes(1, bytes);
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2 but received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var011()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                assertCondition(count == 1, "Expected 1 received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator.
    **/
    public void Var012()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2, received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

     /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var013()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                assertCondition(count == 1);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator.
    **/
    public void Var014()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)7};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                ps.setBytes(1, bytes);
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2 but received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    A locator should be accessible on a newly prepared statement.
    **/
    public void Var015()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                assertCondition(count == 1, "Expected 1 received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator.
    **/
    public void Var016()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2, received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to retrieve an output paramater after a stored procedure is called.
    **/
    public void Var017()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output paramamter after a stored procedure is called.
    **/
    public void Var018()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output paramter after the result set returned from a stored procedure is closed.
    **/
    public void Var019()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output paramater after the result set returned from a stored procedure is closed.
    **/
    public void Var020()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output parameter after a stored procedure is called.
    **/
    public void Var021()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output paramater after a stored procedure is called.
    **/
    public void Var022()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output parameter after the result set returned by a stored procedure is closed.
    **/
    public void Var023()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an output parameter after the result set returned by a stored procedure is closed.
    **/
    public void Var024()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout paramater after a stored procedure is called.
    **/
    public void Var025()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after a stored procedure is called.
    **/
    public void Var026()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after the result set returned by a stored procedure is closed.
    **/
    public void Var027()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after the result set returned by a stored procedure is closed.
    **/
    public void Var028()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after a stored procedure is called.
    **/
    public void Var029()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after a stored procedure is called.
    **/
    public void Var030()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout parameter after a result set returned by a stored procedure is closed.
    **/
    public void Var031()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

/**
    Should be able to retrieve an inout parameter after the result set returned by a stored procedure is closed.
    **/
    public void Var032()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var033()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var034()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var035()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1) && (count ==12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

/**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var036()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var037()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var038()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var039()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

/**
    Should be able to retrieve an inout locator parameter when a callable statement is re-executed.
    **/
    public void Var040()
    {
	if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                ResultSet rs = cs.executeQuery();
                int count = 0;
                while(rs.next())
                    count++;
                rs.close();
                byte[] result = cs.getBytes(3);
                byte[] bytes2 = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)6};
                cs.setBytes(1, bytes2);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result1 = cs.getBytes(3);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes2, result1) && (count == 12));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an out locator parameter twice in a row.
    **/
    public void Var041()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                byte[] result1 = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an out locator parameter twice in a row.
    **/
    public void Var042()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                byte[] result1 = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an out locator parameter twice in a row.
    **/
    public void Var043()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                byte[] result1 = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to retrieve an out locator parameter twice in a row.
    **/
    public void Var044()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                byte[] result1 = cs.getBytes(2);
                assertCondition(compareBytes(bytes, result) && compareBytes(bytes, result1));

            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should not be able to retrieve an out locator parameter after a commit is done.
    **/
    public void Var045()
    {
       if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                c.commit();
                byte[] result1 = cs.getBytes(2);
                failed("Didn't throw SQLException but got "+result+" and "+result1+" Should not be able to retrieve an out locator parameter after a commit is done. " );
            }
	    catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should not be able to retrieve an out locator parameter after a commit is done.
    **/
    public void Var046()
    {
       if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                c.commit();
                byte[] result1 = cs.getBytes(2);
                failed("Didn't throw SQLException but got "+result+" and "+result1+"  Should not be able to retrieve an out locator parameter after a commit is done. " );
            }
	    catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should not be able to retrieve an out locator parameter after a commit is done.
    **/
    public void Var047()
    {
       if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                c.commit();
                byte[] result1 = cs.getBytes(2);
                failed("Didn't throw SQLException but got "+result+" and "+result1+ " Should not be able to retrieve an out locator parameter after a commit is done. " );
            }
	    catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should not be able to retrieve an out locator parameter after a commit is done.
    **/
    public void Var048()
    {
       if (checkJdbc20 ()) {
            Connection c = null;
            CallableStatement cs = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                if(getDriver() == JDTestDriver.DRIVER_TOOLBOX)
                    url += ";hold input locators=false";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                cs = c.prepareCall("CALL " + PROC_ + "(?,?,?)");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                cs.setBytes(1, bytes);
                cs.setBytes(3, bytes);
                cs.registerOutParameter(2, java.sql.Types.BLOB);
                cs.registerOutParameter(3, java.sql.Types.BLOB);
                cs.execute();
                byte[] result = cs.getBytes(2);
                c.commit();
                byte[] result1 = cs.getBytes(2);
                failed("Didn't throw SQLException "+result+" and "+result1+"  Should not be able to retrieve an out locator parameter after a commit is done. " );
            }
	    catch (Exception e) {
                assertExceptionIsInstanceOf(e, "java.sql.SQLException");
            }
            finally
            {
                try{
                    cs.close();
                    c.close();
                }
                catch(Exception e1){
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator after a commit.
    **/
    public void Var049()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                c.setAutoCommit(false);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)7};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                c.commit();
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2 but received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should be able to re-execute a PreparedStatement that does an update and contains a locator after a commit.
    **/
    public void Var050()
    {
        if (checkJdbc20 ()) {
            Connection c = null;
            PreparedStatement ps = null;
            try
            {
                String url = baseURL_
                         + ";user=" + systemObject_.getUserId()
                         + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                         + ";lob threshold=1";
                c = DriverManager.getConnection(url);
                ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                ps.setBytes(1, bytes);
                int count = ps.executeUpdate();
                c.commit();
                count += ps.executeUpdate();
                assertCondition(count == 2, "Expected 2, received " + count);
            }
	    catch (Exception e) {
                failed(e, "Unexpected Exception");
            }
            finally
            {
                try{
                    ps.close();
                    c.close();
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
    Should not be able to re-execute a PreparedStatement that does an update and contains a locator after a commit
    if hold input locators is set to false.
    **/
    public void Var051()
    {
        if(getDriver() == JDTestDriver.DRIVER_TOOLBOX){
            if (checkJdbc20 ()) {
                Connection c = null;
                PreparedStatement ps = null;
                try
                {
                    String url = baseURL_
                             + ";user=" + systemObject_.getUserId()
                             + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                             + ";lob threshold=1" 
                             + ";hold input locators=false";
                    c = DriverManager.getConnection(url);
                    c.setAutoCommit(false);
                    ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                    byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)7};
                    ps.setBytes(1, bytes);
                    int count = ps.executeUpdate();
                    c.commit();
                    count += ps.executeUpdate();
                    failed("Didn't throw SQLException count="+count);
                }
	        catch (Exception e) {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                }
                finally
                {
                    try{
                        ps.close();
                        c.close();
                    }
                    catch(Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        }
        else
            notApplicable("Toolbox driver variation.");
    }

    /**
    Should not be able to re-execute a PreparedStatement that does an update and contains a locator after a commit
    if hold input locators is set to false.
    **/
    public void Var052()
    {
        if(getDriver() == JDTestDriver.DRIVER_TOOLBOX){
            if (checkJdbc20 ()) {
                Connection c = null;
                PreparedStatement ps = null;
                try
                {
                    String url = baseURL_
                             + ";user=" + systemObject_.getUserId()
                             + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                             + ";lob threshold=1"
                             + ";hold input locators=false";
                    c = DriverManager.getConnection(url);
                    ps = c.prepareStatement("UPDATE " + TABLE_ + " SET LOBS=? WHERE COL2=2");
                    byte[] bytes = new byte[] {(byte) 1, (byte)2, (byte)3, (byte)4, (byte)5};
                    ps.setBytes(1, bytes);
                    int count = ps.executeUpdate();
                    c.commit();
                    count += ps.executeUpdate();
                    failed("Didn't throw SQLException count="+count);
                }
    	        catch (Exception e) {
                    assertExceptionIsInstanceOf(e, "java.sql.SQLException");
                }
                finally
                {
                    try{
                        ps.close();
                        c.close();
                    }
                    catch(Exception e1){
                        e1.printStackTrace();
                    }
                }
            }
        }
        else
            notApplicable("Toolbox driver variation.");
    }
}
