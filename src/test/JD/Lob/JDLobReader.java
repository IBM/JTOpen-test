///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobReader.java
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
import test.JDTestDriver;
import test.JDTestcase;
import test.PasswordVault;

import java.io.FileOutputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobReader.  This tests the following method
of the JDBC Reader class:

<ul>
<li>close()
<li>mark()
<li>markSupported()
<li>read()
<li>read(char[],int,int)
<li>read(char[])
<li>ready()
<li>reset()
<li>skip()
</ul>
**/
public class JDLobReader
extends JDTestcase
{



    // Private data.
    private Connection          connection_;
    private Statement           statement_;
    private ResultSet           rs_;
    
    public static String TABLE_          = JDLobTest.COLLECTION + ".READER";
    public static final String  MEDIUM_         = "This is a test of the Emergency Broadcast Network.  Take cover!";
    public static       char[]  LARGE_          = null; // final
    public static final int     WIDTH_          = 30000;
        


/**
Static initializer.
**/
    static 
    {
        int actualLength = WIDTH_ - 2;
        LARGE_ = new char[actualLength];
        for (int i = 0; i < actualLength; ++i)
            LARGE_[i] = ')';

        //Set a few locations in the array for testing to make sure we get the correct characters
        LARGE_[0] = 'A';                    //@K1A
        LARGE_[1000] = 'B';                 //@K1A
        LARGE_[25000]= 'C';                 //@K1A
        LARGE_[actualLength-1] = 'D';       //@K1A
    }



/**
Constructor.
**/
    public JDLobReader (AS400 systemObject,
                                    Hashtable namesAndVars,
                                    int runMode,
                                    FileOutputStream fileOutputStream,
                                    String password)
    {
        super (systemObject, "JDLobReader",
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
        TABLE_          = JDLobTest.COLLECTION + ".READER";

        if (isJdbc20 ()) {
        if (areLobsSupported ()) {
           String url = baseURL_
                + ";user=" + systemObject_.getUserId()
                + ";password=" + PasswordVault.decryptPasswordLeak(encryptedPassword_) 
                + ";lob threshold=1";
            connection_ = testDriver_.getConnection(url,systemObject_.getUserId(),encryptedPassword_);
            connection_.setAutoCommit(false);
            connection_.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                      ResultSet.CONCUR_READ_ONLY);
    
            statement_.executeUpdate ("CREATE TABLE " + TABLE_ 
                + "(C_CLOB CLOB(" + WIDTH_ + "))");
    
            PreparedStatement ps = connection_.prepareStatement ("INSERT INTO " 
                + TABLE_ + " (C_CLOB) VALUES (?)");

	    // First row is empty 
            ps.setString (1, "");
            ps.executeUpdate ();

	    // Second row is medium 
            ps.setString (1, MEDIUM_);
            ps.executeUpdate ();

	    // Third row is large 
            ps.setString (1, new String (LARGE_));
            ps.executeUpdate ();
            ps.close ();
            
            rs_ = statement_.executeQuery ("SELECT * FROM " + TABLE_);
        }
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
        if (areLobsSupported ()) {
            statement_.executeUpdate ("DROP TABLE " + TABLE_);
    
            statement_.close ();
            connection_.close ();
        }
        }
    }

/**
close() - When the reader is empty.
**/
    public void Var001()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
close() - When the reader is not empty.
**/
    public void Var002()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
close() - When the reader is already closed.
**/
    public void Var003()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                v.close ();
                succeeded ();
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
mark() - Should throw an exception.

SQL400 - the native driver supports this function.
**/
    public void Var004()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.mark (1);

                if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                   succeeded ();
                else
                   failed ("Didn't throw Exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }


/**
markSupported() - Should return false.

SQL400 - The native driver supports the mark() function.
**/
    public void Var005()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");

                if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                   assertCondition (v.markSupported () == true);
                else
                   assertCondition (v.markSupported () == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the lob is empty and nothing read.
**/
    public void Var006()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
		int readResult = v.read(); 
                assertCondition (readResult == -1, "Calling read method returned "+readResult+" should have returned -1 for empty lob");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the lob is not empty and nothing read.
**/
    public void Var007()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
		int readResult = v.read(); 
                assertCondition (readResult == MEDIUM_.charAt (0), "Read "+readResult+" expected "+MEDIUM_.charAt (0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the lob is not empty and 1 byte
has been read.
**/
    public void Var008()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.read ();
                assertCondition (v.read() == MEDIUM_.charAt (1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the lob is not empty and all but 1 byte
have been read.
**/
    public void Var009()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = MEDIUM_.length ();
                char[] p = new char[length - 1];
                v.read (p);
                assertCondition (v.read() == MEDIUM_.charAt(length-1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the lob is not empty and all bytes
have been read.
**/
    public void Var010()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = MEDIUM_.length ();
                char[] p = new char[length];
                v.read (p);
                assertCondition (v.read() == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read() - When the reader is closed.
**/
    public void Var011()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                v.read ();
                failed ("Didn't throw SQLException");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is empty and nothing read,
reading 0 bytes.
**/
    public void Var012()
    {
        if (checkJdbc20 ()) 
        {
        if (checkLobSupport ()) 
        {
            try 
            {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];                
                int bytesRead = v.read(p, 0, 0);     
                if  ((bytesRead == 0 ) ||
                     (bytesRead == -1))
                   succeeded();
                else
                   failed("Bytes read is wrong, received: " + bytesRead);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is empty and nothing read,
reading more than 0 bytes.
**/
    public void Var013()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];                
                assertCondition (v.read(p, 0, 5) == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and nothing read, reading
0 bytes.
**/
    public void Var014()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[0];
                assertCondition (v.read(p, 0, 0) == 0);
            }
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  (exception.indexOf("ExtendedIllegalArgumentException") > 0)
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the start of the array.
**/
    public void Var015()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                int count = v.read(p, 0, 1);
                assertCondition ((count == 1) && (p[0] == MEDIUM_.charAt(0)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the middle of the array.
**/
    public void Var016()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                int count = v.read(p, 5, 1);
                assertCondition ((count == 1) && (p[5] == MEDIUM_.charAt (0)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and nothing read, reading
1 byte into the end of the array.
**/
    public void Var017()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                int count = v.read(p, 9, 1);
                assertCondition ((count == 1) && (p[9] == MEDIUM_.charAt(0)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the start of the array.
**/
    public void Var018()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 0, 1);
                assertCondition ((count == 1) && (p[0] == MEDIUM_.charAt(10)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and some have been read, reading
1 byte into the middle of the array.
**/
    public void Var019()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 5, 1);
                assertCondition ((count == 1) && (p[5] == MEDIUM_.charAt(10)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the start of the array.
**/
    public void Var020()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 0, 5);
                assertCondition ((count == 5) && (p[0] == MEDIUM_.charAt(10)) && (p[4] == MEDIUM_.charAt(14)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the middle of the array.
**/
    public void Var021()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 3, 5);
                assertCondition ((count == 5) && (p[3] == MEDIUM_.charAt(10)) && (p[7] == MEDIUM_.charAt(14)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and some have been read, reading
several bytes into the end of the array.
**/
    public void Var022()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 5, 5);
                assertCondition ((count == 5) && (p[5] == MEDIUM_.charAt(10)) && (p[9] == MEDIUM_.charAt(14)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and reading the entire
lob.
**/
    public void Var023()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = MEDIUM_.length();
                char[] p = new char[length];
                int count = v.read(p, 0, p.length);
                assertCondition ((count == length) && (p[0] == MEDIUM_.charAt(0)) && (p[length-1] == MEDIUM_.charAt(length-1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - When the lob is not empty and reading after the
the entire lob has been read.
**/
    public void Var024()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[MEDIUM_.length()];
                v.read(p, 0, p.length);
                int count = v.read (p, 0, p.length);
                assertCondition (count == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - Passing null for the byte array.
**/
    public void Var025()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(null, 3, 5);
                failed ("Didn't throw SQLException got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
        }
        }
    }


/**
read(char[],int,int) - Passing -1 for offset.
**/
    public void Var026()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, -1, 5);
                failed ("Didn't throw Exception got "+count);
            }
            catch (Exception e) 
            {                                                                             
                String exception = e.getClass().getName();
                
                if ((exception.indexOf("IndexOutOfBoundsException") > 0) ||
                    (exception.indexOf("ExtendedIllegalArgumentException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - Passing offset greater than length of the array.
**/
    public void Var027()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 12, 5);
                failed ("Didn't throw Exception got "+count);
            }
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if ((exception.indexOf("IndexOutOfBoundsException") > 0) ||
                    (exception.indexOf("ExtendedIllegalArgumentException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - Passing -1 for length.
**/
    public void Var028()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 3, -1);
                failed ("Didn't throw Exception got "+count);
            }
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if ((exception.indexOf("IndexOutOfBoundsException") > 0) ||
                    (exception.indexOf("ExtendedIllegalArgumentException") > 0))
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[],int,int) - Passing length that extends past the end of the array.
**/
    public void Var029()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p, 4, 7);
                failed ("Didn't throw Exception got "+count);
            }
            catch (Exception e)
            {
              if (getDriver() == JDTestDriver.DRIVER_NATIVE)
              {
                assertExceptionIsInstanceOf(e, "java.lang.IndexOutOfBoundsException");
              }
              else
              {
                assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
              }
            }
        }
        }
    }


/**
read(char[],int,int) - When the reader is closed.
**/
    public void Var030()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                char[] p = new char[10];
                int count = v.read(p, 5, 5);
                failed ("Didn't throw Exception got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }


/**
read(char[]) - When the lob is empty and nothing read,
reading 0 bytes.
**/
    public void Var031()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[0]; 
                int length = v.read(p);
                if ((length == 0 ) ||
                    (length == -1))
                   succeeded();
                else
                   failed("wrong lenght returned, bad value is " + length);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and nothing read, reading
0 bytes.
**/
    public void Var032()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[0];
                assertCondition (v.read(p) == 0);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and nothing read, reading
1 byte.
**/
    public void Var033()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[1];
                int count = v.read(p);
                assertCondition ((count == 1) && (p[0] == MEDIUM_.charAt(0)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and some have been read, reading
1 byte.
**/
    public void Var034()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[1];
                v.read (p);
                int count = v.read(p);
                assertCondition ((count == 1) && (p[0] == MEDIUM_.charAt(1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and some have been read, reading
several bytes.
**/
    public void Var035()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read(p);
                assertCondition ((count == 10) && (p[0] == MEDIUM_.charAt(10)) && (p[9] == MEDIUM_.charAt(19)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and some have been read, reading
several bytes when not all are available.
**/
    public void Var036()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[40];
                v.read (p);
                int count = v.read(p);
                assertCondition ((count == 23) && (p[0] == MEDIUM_.charAt(40)) && (p[22] == MEDIUM_.charAt(MEDIUM_.length()-1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and reading the entire
lob.
**/
    public void Var037()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = MEDIUM_.length();
                char[] p = new char[length];
                int count = v.read(p);
                assertCondition ((count == length) && (p[0] == MEDIUM_.charAt(0)) && (p[length-1] == MEDIUM_.charAt(length-1)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - When the lob is not empty and reading after the
the entire lob has been read.
**/
    public void Var038()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[MEDIUM_.length()];
                v.read(p);
                int count = v.read (p);
                assertCondition (count == -1);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
read(char[]) - Passing null for the byte array.
**/
    public void Var039()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                int count = v.read((char[])null);
                failed ("Didn't throw SQLException got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
            }
        }
        }
    }


/**
read(char[]) - When the reader is closed.
**/
    public void Var040()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                char[] p = new char[10];
                int count = v.read(p);
                failed ("Didn't throw SQLException got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }

/**
ready() - When the lob is empty and nothing read.
**/
    public void Var041()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                assertCondition (v.ready() == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
ready() - When the lob is not empty and nothing read.

SQL400 - There is data available to be read so the native driver
         expects the value to be returned to be true here.
**/
    public void Var042()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                assertCondition (v.ready() == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
ready() - When the lob is not empty and 1 byte
has been read.
**/
    public void Var043()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.read ();
                assertCondition (v.ready() == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
ready() - When the lob is not empty and all but 1 byte
have been read.
**/
    public void Var044()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[MEDIUM_.length() - 1];
                v.read (p);
                assertCondition (v.ready() == true);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
ready() - When the lob is not empty and all bytes
have been read.
**/
    public void Var045()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[MEDIUM_.length()];
                v.read (p);
                assertCondition (v.ready() == false);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
ready() - When the reader is closed.
**/
    public void Var046()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                v.ready ();
                failed ("Didn't throw Exception");
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }



/**
reset() - Should have no effect.

SQL400 - description says one thing and tests the other.
         The native driver expects this to work.
**/
    public void Var047()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.reset ();

                if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                   succeeded ();
                else
                   failed ("Didn't throw Exception");

            }
            catch (Exception e) {

               if (getDriver () == JDTestDriver.DRIVER_NATIVE)
                  failed (e, "Unexpected Exception");
               else
                  assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }



/**
skip() - When the lob is empty and nothing read,
skipping 0 bytes.
**/
    public void Var048()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (1);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                long count = v.skip (0);                
                int r = v.read();
                assertCondition ((r == -1) && (count == 0));
            }
            catch (Exception e) 
            {
                String exception = e.getClass().getName();
                
                if  (exception.indexOf("IOException") > 0)
                   succeeded();
                else
                   failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and nothing read, skipping
0 bytes.
**/
    public void Var049()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                long count = v.skip (0);
                assertCondition ((v.read() == MEDIUM_.charAt(0)) && (count == 0));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and nothing read, skipping
1 byte.
**/
    public void Var050()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                long count = v.skip(1);
                assertCondition ((v.read () == MEDIUM_.charAt(1)) && (count == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and some have been read, reading
1 byte.
**/
    public void Var051()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                long count = v.skip (1);
                assertCondition ((v.read () == MEDIUM_.charAt(11)) && (count == 1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and some have been read, skipping
several bytes.
**/
    public void Var052()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                long count = v.skip(5);
                assertCondition ((count == 5) && (v.read () == MEDIUM_.charAt(15)));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and some have been read, skipping
more bytes than are ready.
**/
    public void Var053()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[30];
                v.read (p);
                long count = v.skip (62);
                assertCondition (count == MEDIUM_.length() - 30);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and skipping the entire
lob.
**/
    public void Var054()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = MEDIUM_.length();
                // char[] p = new char[length];
                long count = v.skip (length);
                assertCondition ((count == length) && (v.read() == -1));
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - When the lob is not empty and skipping after the
the entire lob has been read.
**/
    public void Var055()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[MEDIUM_.length()];
                v.read(p);
                long count = v.skip (15);
                if ((count == 0) || (count == -1))
                   succeeded();
                else
                   failed("count incorrect, bad value is " + count);
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception");
            }
        }
        }
    }


/**
skip() - Passing -1 for the argument.
**/
    public void Var056()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                char[] p = new char[10];
                v.read (p);
                long count = v.skip (-1);
                failed ("Didn't throw SQLException got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
            }
        }
        }
    }


/**
skip() - When the reader is closed.
**/
    public void Var057()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (2);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                v.close ();
                long count = v.skip (4);
                failed ("Didn't throw SQLException got "+count);
            }
            catch (Exception e) {
                assertExceptionIsInstanceOf (e, "java.io.IOException");
            }
        }
        }
    }

//@K1A
/**
read(char[]) - When the lob is not empty and reading the entire
lob.
**/
    public void Var058()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (3);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = LARGE_.length;
                char[] p = new char[length];
                int count = v.read(p);          //count contains the  number of characters read
                boolean successful = true;
                int failedAt = -1;
                for(int i=0; i<count; i++)     
                {
                    if(LARGE_[i] != p[i])
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }

		//@K2
		String message = "";
		if(failedAt != -1)
		    message = "Found character '" + p[failedAt] + "' at position " + failedAt + " sb '" + LARGE_[failedAt] +"'";
		    
                assertCondition (successful, message + " Added by toolbox 2/25/2004");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
        }
        }
    }

    //@K1A
/**
read(char[], int, int) - When the lob is not empty and reading the entire
lob.
**/
    public void Var059()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (3);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = LARGE_.length;
                char[] p = new char[length];
                int readOffset = 0;
                int readLengthRemaining = length;
                while (readLengthRemaining > 0)
                {
                    int amountRead = v.read(p, readOffset, readLengthRemaining);
                    if (amountRead == -1) break;
                    readOffset += amountRead;
                    readLengthRemaining -= amountRead;
                }
                boolean successful = true;
                int failedAt = -1;
                for(int i=0; i<length; i++)
                {
                    if(LARGE_[i] != p[i])
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }

		//@K2
		String message = "";
		if(failedAt != -1)
		    message = "Found character '" + p[failedAt] + "' at position " + failedAt + " sb '" + LARGE_[failedAt] +"'";

		assertCondition (readLengthRemaining == 0 && successful, message + " Added by Toolbox 2/25/2004");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
        }
        }
    }

//@K1A
/**
read() - When the lob is not empty and reading the entire
lob.
**/
    public void Var060()
    {
        if (checkJdbc20 ()) {
        if (checkLobSupport ()) {
            try {
                rs_.absolute (3);
                Reader v = rs_.getCharacterStream ("C_CLOB");
                int length = LARGE_.length;
                boolean successful = true;
                int failedAt = -1;
                for(int i=0; i<length; i++)
                {
                    if(LARGE_[i] != v.read())
                    {
                        successful = false;
                        failedAt = i;
                        break;  //Exit after first wrong character
                    }
                }
                assertCondition (successful, "Found wrong character at position " + failedAt + ", Added by Toolbox 2/25/2004");
            }
            catch (Exception e) {
                failed (e, "Unexpected Exception, Added by Toolbox 2/25/2004");
            }
        }
        }
    }
}



