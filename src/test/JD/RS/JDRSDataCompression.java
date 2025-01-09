///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDRSDataCompression.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////


package test.JD.RS;

import com.ibm.as400.access.AS400;

import test.JDRSTest;
import test.JDTestcase;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;



/**
Testcase JDRSDataCompression.  This tests the following method
of the JDBC ResultSet class:

<ul>
<li>"data compression" property
</ul>
**/
public class JDRSDataCompression
extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDRSDataCompression";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDRSTest.main(newArgs); 
   }



    // Private data.
    private static  String TABLE_NAME      = JDRSTest.COLLECTION + ".DATACOMP";
    private static  String RLE_TABLE       = JDRSTest.COLLECTION + ".RLE";

    private static  int     nextId_         = 0;

    private byte[]              nonRepeatingBytes_;
    private String              nonRepeatingString_;
    private byte[]              repeatingBytes_;
    private String              repeatingString_;



/**
Constructor.
**/
    public JDRSDataCompression (AS400 systemObject,
                                Hashtable<String,Vector<String>> namesAndVars,
                                int runMode,
                                FileOutputStream fileOutputStream,
                                
                                String password)
    {
        super (systemObject, "JDRSDataCompression",
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
        String url = baseURL_;
        Connection c = testDriver_.getConnection (url,"JDRSDataCompression1");
        Statement s = c.createStatement ();
        TABLE_NAME      = JDRSTest.COLLECTION + ".DATACOMP";
        RLE_TABLE = JDRSTest.COLLECTION + ".RLE";

        initTable(s,  TABLE_NAME , " (COL0 INTEGER, COL1 VARCHAR(500), "
              + "COL2 VARCHAR(500) FOR BIT DATA)");
         
       

        PreparedStatement ps = c.prepareStatement("INSERT INTO " + TABLE_NAME + " (COL0, COL1, COL2) VALUES(?,?,?)");

        // Add a row with repeating data.
        StringBuffer buffer = new StringBuffer();
        repeatingBytes_ = new byte[500];
        for (int i = 1; i < 500; ++i) {
            buffer.append('r');
            repeatingBytes_[i] = 0x55;
        }
        repeatingString_ = buffer.toString();
        ps.setInt(1, 1);
        ps.setString(2, repeatingString_);
        ps.setBytes(3, repeatingBytes_);
        ps.executeUpdate();

        // Add a row with non-repeating data.
        buffer = new StringBuffer();
        buffer.append("akds094ir0ei43or9ieeokjom riomrdit0-45jv90j5vb949g"); // 50 characteters
        buffer.append(" kje i0- 5i05 909m0- [ htr lo4w,43-mv904b i0- i0-g"); // 50 characteters
        buffer.append("45vb09jnvb nlkh8vc09j8ce nlkljfd0i-fd jnlwr jk4wjl"); // 50 characteters
        buffer.append("59879873243450987609760979986584587458743764365785"); // 50 characteters
        buffer.append("    vmnf nfmn   oiufd   jmh  f n mn fd vckj  vc vc"); // 50 characteters
        buffer.append("RE95NJGLD JRE0-J9T059UT0-JU94T509JIURIU09590GN8G0F"); // 50 characteters
        buffer.append("&^t^*&vg%^$d#^%cr%^%*&g(*&b&*n&*(v^*&v%&^c$%c%&c^*"); // 50 characteters
        buffer.append("VG^*&V^V^*&&)(M*)_(K)_(K)(UU&%%$%F#@@#$%^F%&^^&*H&"); // 50 characteters
        buffer.append("pdldpdldpdldpekre949458586767676746543534232322313"); // 50 characteters
        buffer.append("';/.';/.';/lk;k;k';'./'.';/klnjbggdfsdsxsgdydiuitt"); // 50 characteters
        nonRepeatingString_ = buffer.toString();
        nonRepeatingBytes_ = new byte[500];
        for (int i = 1; i < 500; ++i) {
            nonRepeatingBytes_[i] = (byte)(i % 256);
        }
        ps.setInt(1, 2);
        ps.setString(2, nonRepeatingString_);
        ps.setBytes(3, nonRepeatingBytes_);
        ps.executeUpdate();

        ps.close();

        s.executeUpdate("CREATE TABLE " + RLE_TABLE + "(ID INTEGER, ODD VARCHAR(1999) FOR BIT DATA, " +
                        "EVEN VARCHAR(2000) FOR BIT DATA, EBCDIC VARCHAR(1500), UNICODE VARGRAPHIC(1500) CCSID 13488)");

        s.close();
        c.close();
    }



/**
Performs cleanup needed after running variations.

@exception Exception If an exception occurs.
**/
    protected void cleanup ()
    throws Exception
    {
        String url = baseURL_;
        Connection c = testDriver_.getConnection (url,"JDRSDataCompression2");
        Statement s = c.createStatement ();

        cleanupTable(s, TABLE_NAME);

        cleanupTable(s,  RLE_TABLE);
    }



    private boolean testDataCompression(String dc)
    throws Exception
    {
        String url = baseURL_;
        if (dc != null)
            url += ";data compression=" + dc;
        Connection c = testDriver_.getConnection (url,"JDRSDataCompression3");
        Statement s = c.createStatement ();

        ResultSet rs = s.executeQuery("SELECT * FROM " + TABLE_NAME);
        rs.next();
        boolean check = (rs.getInt(1) == 1);
        check = check && (rs.getString(2).equals(repeatingString_));
        check = check && (areEqual(rs.getBytes(3), repeatingBytes_));
        rs.next();
        check = check && (rs.getInt(1) == 2);
        check = check && (rs.getString(2).equals(nonRepeatingString_));
        check = check && (areEqual(rs.getBytes(3), nonRepeatingBytes_));

        rs.close();

        s.close();
        c.close();

        return check;
    }



    private boolean testRLECompression(String dc, byte[] odd, byte[] even, String text)
    throws Exception
    {
        String url = baseURL_;
        if (dc != null)
            url += ";data compression=" + dc;
        Connection c = testDriver_.getConnection (url,"JDRSDataCompression4");

        int id = nextId_++;
        PreparedStatement ps = c.prepareStatement("INSERT INTO " + RLE_TABLE + "(ID, ODD, EVEN, EBCDIC, UNICODE) VALUES(?, ?, ?, ?, ?)");
        ps.setInt(1, id);
        ps.setBytes(2, odd);
        ps.setBytes(3, even);
        ps.setString(4, text);
        ps.setString(5, text);
        ps.executeUpdate();
        ps.close();

        Statement s = c.createStatement ();
        ResultSet rs = s.executeQuery("SELECT * FROM " + RLE_TABLE + " WHERE ID=" + id);
        rs.next();

        byte[] odd2 = rs.getBytes(2);
        byte[] even2 = rs.getBytes(3);
        String text2 = rs.getString(4);
        String text3 = rs.getString(5);

        boolean check = true;
        check = check && (areEqual(odd, odd2));
        check = check && (areEqual(even, even2));
        check = check && (text.equals(text2));
        check = check && (text.equals(text3));
        check = check && (rs.next() == false);

        rs.close();
        s.close();
        c.close();

        return check;
    }



/**
"data compression" property - Leave the default.
**/
    public void Var001()
    {
        try {
            assertCondition(testDataCompression(null));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Set to true.
**/
    public void Var002()
    {
        try {
            assertCondition(testDataCompression("true"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Set to false.
**/
    public void Var003()
    {
        try {
            assertCondition(testDataCompression("false"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Set to a bogus value.
**/
    public void Var004()
    {
        try {
            assertCondition(testDataCompression("bogus"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
"data compression" property - Test RLE compression - all 0's and data compression on.
**/
    public void Var005()
    {
        try {
            assertCondition(testRLECompression("true", new byte[1999], new byte[2000], ""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
"data compression" property - Test RLE compression - all 0's and data compression off.
**/
    public void Var006()
    {
        try {
            assertCondition(testRLECompression("false", new byte[1999], new byte[2000], ""));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - all X's and data compression on.
**/
    public void Var007()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)'X';

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)'X';

            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < 1500; ++i)
                buffer.append('X');

            assertCondition(testRLECompression("true", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - all X's and data compression off.
**/
    public void Var008()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)'X';

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)'X';

            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < 1500; ++i)
                buffer.append('X');

            assertCondition(testRLECompression("false", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - all escapess and data compression on.
**/
    public void Var009()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)0x1B;

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)0x1B;

            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < 1500; ++i)
                buffer.append((char)0x1B);

            assertCondition(testRLECompression("true", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - all escapess and data compression off.
**/
    public void Var010()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)0x1B;

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)0x1B;

            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < 1500; ++i)
                buffer.append((char)0x1B);

            assertCondition(testRLECompression("false", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
"data compression" property - Test RLE compression - no repeats and data compression on.
**/
    public void Var011()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)i;

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)i;

            StringBuffer buffer = new StringBuffer();
            String pattern = "RLE compression works really great";
            for(int i = 0; i < 1400; i += pattern.length()) {
                buffer.append(pattern);
            }

            assertCondition(testRLECompression("true", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - no repeats and data compression off.
**/
    public void Var012()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length; ++i)
                odd[i] = (byte)i;

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length; ++i)
                even[i] = (byte)i;

            StringBuffer buffer = new StringBuffer();
            String pattern = "RLE compression works great";
            for(int i = 0; i < 1400; i += pattern.length()) {
                buffer.append(pattern);
            }

            assertCondition(testRLECompression("false", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }




/**
"data compression" property - Test RLE compression - a mix and data compression on.
**/
    public void Var013()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length - 16; i += 16) {
                odd[i] = (byte)i;
                odd[i+2] = (byte)0x45;
                odd[i+3] = (byte)0x45;
                odd[i+4] = (byte)0x45;
                odd[i+5] = (byte)0x45;
                odd[i+6] = (byte)0x45;
                odd[i+7] = (byte)0x45;
                odd[i+8] = (byte)0x45;
                odd[i+9] = (byte)0x45;
                odd[i+10] = (byte)0x55;
                odd[i+11] = (byte)0x43;
                odd[i+12] = (byte)0x1B;
                odd[i+13] = (byte)0x34;
                odd[i+14] = (byte)0x34;
                odd[i+15] = (byte)0x34;
            }

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length - 20; i += 20) {
                even[i] = (byte)i;
                even[i+1] = (byte)0x1B;
                even[i+2] = (byte)0x1B;
                even[i+3] = (byte)0x32;
                even[i+4] = (byte)0x40;
                even[i+5] = (byte)0x32;
                even[i+6] = (byte)0x40;
                even[i+7] = (byte)0x32;
                even[i+8] = (byte)0x40;
                even[i+9] = (byte)0x32;
                even[i+10] = (byte)0x40;
                even[i+11] = (byte)0x32;
                even[i+12] = (byte)0x40;
                even[i+13] = (byte)0x42;
                even[i+14] = (byte)0x30;
                even[i+15] = (byte)0x32;
                even[i+16] = (byte)0x1B;
                even[i+17] = (byte)0x00;
                even[i+18] = (byte)0x32;
                even[i+19] = (byte)0x40;
            }

            StringBuffer buffer = new StringBuffer();
            String pattern = "AAAAAAAAAAAAAlllllll work and no play";
            for(int i = 0; i < 1400; i += pattern.length() + 1) {
                buffer.append(pattern);
                buffer.append((char)0x1B);
            }

            assertCondition(testRLECompression("true", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



/**
"data compression" property - Test RLE compression - a mix and data compression off.
**/
    public void Var014()
    {
        try {
            byte[] odd = new byte[1999];
            for(int i = 0; i < odd.length - 16; i += 16) {
                odd[i] = (byte)i;
                odd[i+2] = (byte)0x45;
                odd[i+3] = (byte)0x45;
                odd[i+4] = (byte)0x45;
                odd[i+5] = (byte)0x45;
                odd[i+6] = (byte)0x45;
                odd[i+7] = (byte)0x45;
                odd[i+8] = (byte)0x45;
                odd[i+9] = (byte)0x45;
                odd[i+10] = (byte)0x55;
                odd[i+11] = (byte)0x43;
                odd[i+12] = (byte)0x1B;
                odd[i+13] = (byte)0x34;
                odd[i+14] = (byte)0x34;
                odd[i+15] = (byte)0x34;
            }

            byte[] even = new byte[2000];
            for(int i = 0; i < even.length- 2000; i += 20) {
                even[i] = (byte)i;
                even[i+1] = (byte)0x1B;
                even[i+2] = (byte)0x1B;
                even[i+3] = (byte)0x32;
                even[i+4] = (byte)0x40;
                even[i+5] = (byte)0x32;
                even[i+6] = (byte)0x40;
                even[i+7] = (byte)0x32;
                even[i+8] = (byte)0x40;
                even[i+9] = (byte)0x32;
                even[i+10] = (byte)0x40;
                even[i+11] = (byte)0x32;
                even[i+12] = (byte)0x40;
                even[i+13] = (byte)0x42;
                even[i+14] = (byte)0x30;
                even[i+15] = (byte)0x32;
                even[i+16] = (byte)0x1B;
                even[i+17] = (byte)0x00;
                even[i+18] = (byte)0x32;
                even[i+19] = (byte)0x40;
            }

            StringBuffer buffer = new StringBuffer();
            String pattern = "AAAAAAAAAAAAAlllllll work and no play";
            for(int i = 0; i < 1400; i += pattern.length() + 1) {
                buffer.append(pattern);
                buffer.append((char)0x1B);
            }

            assertCondition(testRLECompression("false", odd, even, buffer.toString()));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}



