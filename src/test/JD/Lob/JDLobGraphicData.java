///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDLobGraphicData.java
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
import test.JDTestcase;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;                                      // @C1A
import java.io.OutputStream;                                // @C1A
import java.sql.Clob;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;



/**
Testcase JDLobGraphicData.  
**/

public class JDLobGraphicData
extends JDTestcase
{



    // Private data.
    private Connection  connection_;
    private Statement   statement_;
    private String      TABLE_;        
    private String      url_;
    
    private String  smallClob      = null;
    private String  mediumClob     = null;
    private String  medium2Clob    = null;
    private String  largeClob      = null;
    private String  evenLargerClob = null;
    private byte[]  smallBlob      = null;
    private byte[]  biggerBlob     = null;
    
       





/**
Constructor.
**/
    public JDLobGraphicData (AS400 systemObject,
                      Hashtable namesAndVars,
                      int runMode,
                      FileOutputStream fileOutputStream,
                      String password)
    {
        super (systemObject, 
               "JDLobGraphicData",
               namesAndVars, 
               runMode, 
               fileOutputStream, 
               
               password);
    }



/**
Performs setup needed before running variations.

@exception Exception If an exception occurs.
**/
    protected void setup ()
        throws Exception
    {
        TABLE_ = JDLobTest.COLLECTION + ".DATA"; 

        StringBuffer sb = new StringBuffer();                                                                                 

        for (int i = 1; i < 20; i++)
           sb = sb.append(i + " abcdefgh\n"); 
        sb.setLength(110);   
        smallClob = new String(sb);
      
        sb = new StringBuffer();   
        for (int i = 1; i < 2048; i++)
           sb = sb.append(i + " abcdefgh\n"); 
        sb.setLength(10*1024);   
        mediumClob = new String(sb);      
   
        sb = new StringBuffer();   
        for (int i = 1; i < 5*1024; i++)
           sb = sb.append(i + " abcdefgh\n"); 
        sb.setLength(30*1024);   
        medium2Clob = new String(sb);      
         
        sb = new StringBuffer();   
        for (int i = 1; i < 15*1024; i++)
           sb = sb.append(i + " abcdefgh\n"); 
        sb.setLength(100*1024);   
        largeClob = new String(sb);    
        
        sb = new StringBuffer();   
        for (int i = 1; i < 150*1024; i++)
           sb = sb.append(i + " abcdefgh\n"); 
        sb.setLength(1024 * 1024);   
        evenLargerClob = new String(sb);    

        smallBlob  = new byte[111];
        biggerBlob = new byte[49 * 1024];
        
        for (int i=0; i<smallBlob.length; i++)
           smallBlob[i] = (byte) (i % 10);
      
        for (int i=0; i<biggerBlob.length; i++)
           biggerBlob[i] = (byte) (i % 10);
      
       
        if (isJdbc20 ()) 
        {
           try
           {
           
              url_ = baseURL_;

              connection_ = testDriver_.getConnection(url_,systemObject_.getUserId(),encryptedPassword_);

              statement_ = connection_.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                                        ResultSet.CONCUR_READ_ONLY);
                              
              try
              {
                 statement_.executeUpdate ("CREATE TABLE " + TABLE_  + 
                           " (col1_int integer ,                       " +
                             "col2_smallClob clob(256) ,                 " +
                             "col3_smallDBClob DBCLOB(256) ccsid 13488 , " +
                             "col4_clob clob(110000) ,                   " +
                             "col5_dbclob DBCLOB(110000) CCSID 13488,    " +
                             "col6_blob BLOB(200),                       " +
                             "col7_varbin varchar(100) for bit data ,    " +
                             "col8_bin char(100) for bit data,           " +
                             "col9_BigBlob blob(65000) ,                 " +
                             "col10_Vargraphic vargraphic(10240) ccsid 13488 , " +
                             "col11_biggerClob clob(1049000) , " + 
                             "col12_biggerDBClob DBCLOB(1049000) CCSID 13488  )" );
              }
              catch (Exception e)
              {
                 System.out.println("   Setup warning, create table failed!!!!");
                 System.out.println("   " + e.getMessage());
              }   

              String str;
              
              str = "delete from " + TABLE_;
              statement_.executeUpdate(str);
              
              str = "insert into " + TABLE_ + " (col1_int, col2_smallClob) values(?,?)";
              PreparedStatement pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 1);
              pstmt.setString(2, smallClob);
              pstmt.executeUpdate();
              
              // put first one in twice to help make numbers line up better
              pstmt.setInt(1, 2);
              pstmt.setString(2, smallClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col3_smallDBClob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 3);
              pstmt.setString(2, smallClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col4_clob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 4);
              pstmt.setString(2, mediumClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col5_dbclob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 5);
              pstmt.setString(2, mediumClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col4_clob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 6);
              pstmt.setString(2, medium2Clob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col5_dbclob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 7);
              pstmt.setString(2, medium2Clob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col4_clob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 8);
              pstmt.setString(2, largeClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col5_dbclob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 9);
              pstmt.setString(2, largeClob);
              pstmt.executeUpdate();               
              
              str = "insert into " + TABLE_ + " (col1_int, col10_Vargraphic) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 10);
              pstmt.setString(2, smallClob);
              pstmt.executeUpdate();               
              
              str = "insert into " + TABLE_ + " (col1_int, col6_blob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 11);
              pstmt.setBytes(2, smallBlob);
              pstmt.executeUpdate();               
              
              str = "insert into " + TABLE_ + " (col1_int, col9_BigBlob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 12);
              pstmt.setBytes(2, biggerBlob);
              pstmt.executeUpdate();               
              
              str = "insert into " + TABLE_ + " (col1_int, col11_biggerClob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 13);
              pstmt.setString(2, evenLargerClob);
              pstmt.executeUpdate();
              
              str = "insert into " + TABLE_ + " (col1_int, col12_biggerDBClob) values(?,?)";
              pstmt = connection_.prepareStatement(str);
              pstmt.setInt(1, 14);
              pstmt.setString(2, evenLargerClob);
              pstmt.executeUpdate();               
              


              pstmt.close();

          }
          catch (Exception e)
          {
             System.out.println("Warning, setup failed!!!!");
             e.printStackTrace();
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
        if (isJdbc20 ()) 
        {
            statement_.close ();
            connection_.close ();

            Connection c = testDriver_.getConnection(url_,systemObject_.getUserId(), encryptedPassword_);

            Statement s = c.createStatement (ResultSet.TYPE_SCROLL_INSENSITIVE,
                                             ResultSet.CONCUR_READ_ONLY);
            s.executeUpdate ("DROP TABLE " + TABLE_);
            s.close();
            c.close();
        }
    }


    static boolean checkResults(String testString, String resultString)
    {
       if (!testString.equals(resultString))
       {
          System.out.println();
          System.out.println("   failed, strings do not match");
          System.out.println("   source string length: " + testString.length());
          System.out.println("   result string length: " + resultString.length());
          return false;
       }               
       return true;
    }





    static boolean checkResultsBytes(byte[] source, byte[] result)
    {      
       if (source.length == result.length)
       {
          boolean same = true;
          for (int i=0; i<result.length; i++)
             if (source[i] != result[i])
                same = false;
          
          if (same)
             return true;
          else
          {          
             System.out.println();
             System.out.println("   failed, byte arrays do not match");
             return false;                
          }   
       }               
       else
       {
          System.out.println();
          System.out.println("   lengths do not match.  Source byte array length: " + source.length + " Result byte array length: " + result.length);
          return false;
       }
    }






/**
   Tests 1 - 8 are from SAP "TestSetClob" testcase
**/
    public void Var001()
    {  
       assertCondition(doClobWork(smallClob, "col2_smallClob", 2));
    }

    public void Var002()
    {  
       assertCondition(doClobWork(smallClob, "col3_smallDBClob", 3));
    }

    public void Var003()
    {  
       assertCondition(doClobWork(mediumClob, "col4_Clob", 4));
    }

    public void Var004()
    {  
       assertCondition(doClobWork(mediumClob, "col5_DBClob", 5));
    }

    public void Var005()
    {  
       assertCondition(doClobWork(medium2Clob, "col4_Clob", 6));
    }

    public void Var006()
    {  
       assertCondition(doClobWork(medium2Clob, "col5_DBClob", 7));
    }

    public void Var007()
    {  
       assertCondition(doClobWork(largeClob, "col4_Clob", 8));
    }

    public void Var008()
    {  
       assertCondition(doClobWork(largeClob, "col5_DBClob", 9));
    }

    boolean doClobWork(String sourceString, String column, int row)
    {
       ResultSet rs = null;
       PreparedStatement pstmt  = null;
       PreparedStatement pstmt2 = null;
       Statement s = null;

       boolean returnValue = false;
      
       try
       {
          Clob clob = null;

          String str = "select " + column + " from " + TABLE_ + " where col1_int = ? ";
                                                    
          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, row);
          rs = pstmt.executeQuery();
          if (rs.next())
          {
             clob = rs.getClob(1);
             String value = clob.getSubString(1, (int) clob.length());
             if (!checkResults(sourceString, value))
             {
               System.out.println("Clob(1) does not contain original data.");
               return false;
             }
             str = "insert into " + TABLE_ + " (col1_int, " + column + ") values(?,?)";
             pstmt2 = connection_.prepareStatement(str);
             pstmt2.setInt(1, 100 + row);
             pstmt2.setClob(2, clob);
             pstmt2.executeUpdate();

             s = connection_.createStatement();
             rs = s.executeQuery("select " + column + " from " + TABLE_ + " where col1_int = " + (row + 100));
             rs.next();
             Clob myclob = rs.getClob(1);
             value = rs.getString(1);

             returnValue = checkResults(sourceString, value);
          }
          else
          {
             System.out.println("   No matching record for key: " + row);
             return false;
          }
       }
       catch (Exception e)
       {
          System.out.println("unexpected exception");
          e.printStackTrace();
       }     
       
       if (s != null) try { s.close(); } catch(Exception e) { System.out.println("warning, statement cleanup failed"); e.printStackTrace(); }
       if (pstmt  != null) try { pstmt.close();  } catch(Exception e) { System.out.println("warning, ps  cleanup failed"); e.printStackTrace(); }
       if (pstmt2 != null) try { pstmt2.close(); } catch(Exception e) { System.out.println("warning, ps2 cleanup failed"); e.printStackTrace(); }

       return returnValue;
    }










/**
   Tests 9 - 16 are from SAP "GetCharacterStream" testcase
**/
    public void Var009()
    {  
       assertCondition(doCharacterStreamWork(smallClob, "col2_smallClob", 2));
    }

    public void Var010()
    {  
       assertCondition(doCharacterStreamWork(smallClob, "col3_smallDBClob", 3));
    }

    public void Var011()
    {  
       assertCondition(doCharacterStreamWork(mediumClob, "col4_Clob", 4));
    }

    public void Var012()
    {  
       assertCondition(doCharacterStreamWork(mediumClob, "col5_DBClob", 5));
    }

    public void Var013()
    {  
       assertCondition(doCharacterStreamWork(medium2Clob, "col4_Clob", 6));
    }

    public void Var014()
    {  
       assertCondition(doCharacterStreamWork(medium2Clob, "col5_DBClob", 7));
    }

    public void Var015()
    {  
       assertCondition(doCharacterStreamWork(largeClob, "col4_Clob", 8));
    }

    public void Var016()
    {  
       assertCondition(doCharacterStreamWork(largeClob, "col5_DBClob", 9));
    }


    boolean doCharacterStreamWork(String sourceString, String column, int row)
    {
       ResultSet rs = null;
       PreparedStatement pstmt = null;
       boolean returnValue = false;
      
       try
       {
          Clob clob = null;

          String str = "select " + column + " from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, row);
          rs = pstmt.executeQuery();
          if (rs.next())
          {
             Reader resultReader = rs.getCharacterStream(1);
             int c;
             if (resultReader.ready()) 
             {
                StringBuffer buffer = new StringBuffer();
                try 
                {
                   while (0 <= (c = resultReader.read())) 
                   {
                            buffer.append((char)c);
                   }
                   returnValue = checkResults(sourceString, new String(buffer));
                }                                                     
                catch (Exception e)
                {
                   System.out.println("unexpected exception");
                   e.printStackTrace();
                }     
             }
             else
             {
                System.out.println("   Reader is not ready. ");
             }
          }
          else
          {
             System.out.println("   No matching record for key: " + row);
          }
       }
       catch (Exception e)
       {
          System.out.println("unexpected exception");
          e.printStackTrace();
       }     

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) {  System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
       
       return returnValue;
 
    }









/**
   Tests 17 - 24 are from SAP "GetClobGetCharacterStream" testcase
**/
    public void Var017()
    {  
       assertCondition(doGetClobCharacterStreamWork(smallClob, "col2_smallClob", 2));
    }

    public void Var018()
    {  
       assertCondition(doGetClobCharacterStreamWork(smallClob, "col3_smallDBClob", 3));
    }

    public void Var019()
    {  
       assertCondition(doGetClobCharacterStreamWork(mediumClob, "col4_Clob", 4));
    }

    public void Var020()
    {  
       assertCondition(doGetClobCharacterStreamWork(mediumClob, "col5_DBClob", 5));
    }

    public void Var021()
    {  
       assertCondition(doGetClobCharacterStreamWork(medium2Clob, "col4_Clob", 6));
    }

    public void Var022()
    {  
       assertCondition(doGetClobCharacterStreamWork(medium2Clob, "col5_DBClob", 7));
    }

    public void Var023()
    {  
       assertCondition(doGetClobCharacterStreamWork(largeClob, "col4_Clob", 8));
    }

    public void Var024()
    {  
       assertCondition(doGetClobCharacterStreamWork(largeClob, "col5_DBClob", 9));
    }



    boolean doGetClobCharacterStreamWork(String sourceString, String column, int row)
    {
       ResultSet rs = null;
       PreparedStatement pstmt = null;
       boolean returnValue = false;
      
       try
       {
          Clob clob = null;

          String str = "select " + column + " from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, row);
          rs = pstmt.executeQuery();
          if (rs.next())
          {
             Reader resultReader = rs.getClob(1).getCharacterStream();
             int c;
             if (resultReader.ready()) 
             {
                StringBuffer buffer = new StringBuffer();
                try 
                {
                   while (0 <= (c = resultReader.read())) 
                   {
                            buffer.append((char)c);
                   }
                   returnValue = checkResults(sourceString, new String(buffer));
                }                                                     
                catch (Exception e)
                {
                   System.out.println("unexpected exception");
                   e.printStackTrace();
                }     
             }
             else
             {
                System.out.println("   Reader is not ready. ");
             }
          }
          else
          {
             System.out.println("   No matching record for key: " + row);
          }
       }
       catch (Exception e)
       {
          System.out.println("unexpected exception");
          e.printStackTrace();
       }     

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) {  System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
       
       return returnValue;
 
    }




/**
   Tests 25 - 32 are from SAP "GetClobGetSubstringStream" testcase
**/
    public void Var025()
    {  
       assertCondition(doGetClobGetSubstringWork(smallClob, "col2_smallClob", 2));
    }

    public void Var026()
    {  
       assertCondition(doGetClobGetSubstringWork(smallClob, "col3_smallDBClob", 3));
    }

    public void Var027()
    {  
       assertCondition(doGetClobGetSubstringWork(mediumClob, "col4_Clob", 4));
    }

    public void Var028()
    {  
       assertCondition(doGetClobGetSubstringWork(mediumClob, "col5_DBClob", 5));
    }

    public void Var029()
    {  
       assertCondition(doGetClobGetSubstringWork(medium2Clob, "col4_Clob", 6));
    }

    public void Var030()
    {  
       assertCondition(doGetClobGetSubstringWork(medium2Clob, "col5_DBClob", 7));
    }

    public void Var031()
    {  
       assertCondition(doGetClobGetSubstringWork(largeClob, "col4_Clob", 8));
    }

    public void Var032()
    {  
       assertCondition(doGetClobGetSubstringWork(largeClob, "col5_DBClob", 9));
    }



    boolean doGetClobGetSubstringWork(String sourceString, String column, int row)
    {
       ResultSet rs = null;
       PreparedStatement pstmt = null;
       boolean returnValue = false;
      
       try
       {
          Clob clob = null;

          String str = "select " + column + " from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, row);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              clob = rs.getClob(1);             
              String result = clob.getSubString(1, (int)clob.length());
              returnValue = checkResults(sourceString, result);
          }
          else
          {
             System.out.println("   No matching record for key: " + row);
          }
       }
       catch (Exception e)
       {
          System.out.println("unexpected exception");
          e.printStackTrace();
       }     

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) {  System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
       
       return returnValue;
 
    }









/**
   Tests 33 - 40 are from SAP "GetString" testcase
**/
    public void Var033()
    {  
       assertCondition(doGetStringWork(smallClob, "col2_smallClob", 2));
    }

    public void Var034()
    {  
       assertCondition(doGetStringWork(smallClob, "col3_smallDBClob", 3));
    }

    public void Var035()
    {  
       assertCondition(doGetStringWork(mediumClob, "col4_Clob", 4));
    }

    public void Var036()
    {  
       assertCondition(doGetStringWork(mediumClob, "col5_DBClob", 5));
    }

    public void Var037()
    {  
       assertCondition(doGetStringWork(medium2Clob, "col4_Clob", 6));
    }

    public void Var038()
    {  
       assertCondition(doGetStringWork(medium2Clob, "col5_DBClob", 7));
    }

    public void Var039()
    {                
       assertCondition(doGetStringWork(largeClob, "col4_Clob", 8));
    }

    public void Var040()
    {  
       assertCondition(doGetStringWork(largeClob, "col5_DBClob", 9));
    }



    boolean doGetStringWork(String sourceString, String column, int row)
    {
       ResultSet rs = null;
       PreparedStatement pstmt = null;
       boolean returnValue = false;
      
       try
       {
          Clob clob = null;

          String str = "select " + column + " from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, row);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              String result = rs.getString(1);
              returnValue = checkResults(sourceString, result);
          }
          else
          {
             System.out.println("   No matching record for key: " + row);
          }
       }
       catch (Exception e)
       {
          System.out.println("unexpected exception");
          e.printStackTrace();
       }     

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
       
       return returnValue;
    }





/**
   Test 41 is a vargraphic test
**/
    public void Var041()
    {  
       ResultSet rs = null;
       PreparedStatement pstmt = null;
      
       try
       {

          String str = "select col10_Vargraphic from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 10);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              String result = rs.getString(1);
              assertCondition(checkResults(smallClob, result));
          }
          else
          {
             System.out.println("   No matching record for key: 10 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }







/**
   Test 42 - 47 are blob tests
**/
    public void Var042()
    {  
       ResultSet rs = null;
       PreparedStatement pstmt = null;
      
       try
       {

          String str = "select col6_blob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 11);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              byte[] result = rs.getBytes(1);
              assertCondition(checkResultsBytes(smallBlob, result));
          }
          else
          {
             System.out.println("   No matching record for key: 11 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }

    public void Var043()
    {  
       ResultSet rs = null;
       PreparedStatement pstmt = null;
      
       try
       {

          String str = "select col9_BigBlob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 12);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              byte[] result = rs.getBytes(1);
              assertCondition(checkResultsBytes(biggerBlob, result));
          }
          else
          {
             System.out.println("   No matching record for key: 12 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }

    public void Var044()
    {  
       ResultSet rs = null;
       PreparedStatement pstmt = null;
      
       try
       {

          String str = "select col6_blob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 11);
          rs = pstmt.executeQuery();
          if (rs.next())
          {                                
              Blob blob = rs.getBlob(1);
              byte[] result = blob.getBytes(1, (int) blob.length());
              assertCondition(checkResultsBytes(smallBlob, result));
          }
          else
          {
             System.out.println("   No matching record for key: 11 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }

    public void Var045()
    {  
       ResultSet rs = null;
       PreparedStatement pstmt = null;
      
       try
       {

          String str = "select col9_BigBlob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 12);
          rs = pstmt.executeQuery();
          if (rs.next())
          {   
              Blob blob = rs.getBlob(1);
              byte[] result = blob.getBytes(1, (int) blob.length());
              assertCondition(checkResultsBytes(biggerBlob, result));
          }
          else
          {
             System.out.println("   No matching record for key: 12 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }

    public void Var046()
    {  
       ResultSet rs  = null;
       ResultSet rs2 = null;
       PreparedStatement pstmt  = null;
       PreparedStatement pstmt2 = null;
      
       try
       {

          String str = "select col6_blob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 11);
          rs = pstmt.executeQuery();
          if (rs.next())
          {                                
              Blob blob = rs.getBlob(1);
              str = "insert into " + TABLE_ + " (col1_int, col6_blob) values(?,?)";
              pstmt2 = connection_.prepareStatement(str);
              pstmt2.setInt(1, 110);                     
              pstmt2.setBlob(2, blob);    
              pstmt2.executeUpdate();
              pstmt2.close();
              rs.close();

              pstmt.setInt(1, 110);
              rs = pstmt.executeQuery();
              if (rs.next())
              {                                                       
                 Blob blob2 = rs.getBlob(1);
                 byte[] result = blob2.getBytes(1, (int) blob2.length());
                 assertCondition(checkResultsBytes(smallBlob, result));
              }
              else
              {
                System.out.println("   No matching record for key: 110 ");
                failed();
              }
          }
          else
          {
             System.out.println("   No matching record for key: 11 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }

    public void Var047()
    {  
       ResultSet rs  = null;
       ResultSet rs2 = null;
       PreparedStatement pstmt  = null;
       PreparedStatement pstmt2 = null;
      
       try
       {

          String str = "select col9_BigBlob from " + TABLE_ + " where col1_int = ? ";

          pstmt = connection_.prepareStatement(str);
          pstmt.setInt(1, 12);
          rs = pstmt.executeQuery();
          if (rs.next())
          {                                
              Blob blob = rs.getBlob(1);
              str = "insert into " + TABLE_ + " (col1_int, col9_BigBlob) values(?,?)";
              pstmt2 = connection_.prepareStatement(str);
              pstmt2.setInt(1, 120);                     
              pstmt2.setBlob(2, blob);
              pstmt2.executeUpdate();
              pstmt2.close();
              rs.close();

              pstmt.setInt(1, 120);
              rs = pstmt.executeQuery();
              if (rs.next())
              {                                                       
                 Blob blob2 = rs.getBlob(1);
                 byte[] result = blob2.getBytes(1, (int) blob2.length());
                 assertCondition(checkResultsBytes(biggerBlob, result));
              }
              else
              {
                System.out.println("   No matching record for key: 120 ");
                failed();
              }
          }
          else
          {
             System.out.println("   No matching record for key: 11 ");
             failed();
          }
       }
       catch (Exception e)
       {
          failed(e, "unexpected exception");
       }

       if (pstmt != null) try { pstmt.close(); } catch(Exception e) { System.out.println("warning, ps cleanup failed"); e.printStackTrace(); }
    }







/**
   Test 48 - xx work with bigger clobs 
**/

    public void Var048()
    {  
       assertCondition(doClobWork(evenLargerClob, "col11_biggerClob", 13));
    }

    public void Var049()
    {  
       assertCondition(doClobWork(evenLargerClob, "col12_biggerDBClob", 14));
    }

    public void Var050()
    {  
       assertCondition(doCharacterStreamWork(evenLargerClob, "col11_biggerClob", 13));
    }

    public void Var051()
    {  
       assertCondition(doCharacterStreamWork(evenLargerClob, "col12_biggerDBClob", 14));
    }

    public void Var052()
    {  
       assertCondition(doGetClobCharacterStreamWork(evenLargerClob, "col11_biggerClob", 13));
    }

    public void Var053()
    {  
       assertCondition(doGetClobCharacterStreamWork(evenLargerClob, "col12_biggerDBClob", 14));
    }

    public void Var054()
    {  
       assertCondition(doGetClobGetSubstringWork(evenLargerClob, "col11_biggerClob", 13));
    }

    public void Var055()
    {  
       assertCondition(doGetClobGetSubstringWork(evenLargerClob, "col12_biggerDBClob", 14));
    }

    public void Var056()
    {                
       assertCondition(doGetStringWork(evenLargerClob, "col11_biggerClob", 13));
    }

    public void Var057()
    {  
       assertCondition(doGetStringWork(evenLargerClob, "col12_biggerDBClob", 14));
    }

}

