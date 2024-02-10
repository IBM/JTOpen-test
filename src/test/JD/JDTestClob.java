///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestClob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.JD;

import java.io.InputStream;
import java.io.OutputStream;    
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.Writer;          
import java.sql.Clob;





    /**
    Simple implementation of Clob used for testing.
    **/
    public  class JDTestClob
    implements Clob, Serializable {
        /**
       * 
       */
      private static final long serialVersionUID = 1L;
        private String data_;

        public JDTestClob (String data) { data_ = data;}
        public InputStream getAsciiStream () { return null;}

        // We need this function to work now.
        public Reader getCharacterStream () {
            return new StringReader(data_);
        }


        public Reader getCharacterStream (long pos, long length) {
            return new StringReader(data_.substring((int)pos, (int)( pos + length)));
        }

        public long position (String pattern, long start) { return -1;}
        public long position (Clob pattern, long start) { return -1;}

        // Lob indexes are 1 based.  The string that is used to
        // represent the data is 0 based.  Account for that when
        // returning a substring.
        public String getSubString (long start, int length)
        {
            return data_.substring ((int) start - 1, (int) start + length - 1);
        }

        public long length ()
        {
            return data_.length ();
        }
        public int setString(long pos, String str)                        
        {                                                                 
            return 0;                                                     
        }                                                                 
        public int setString(long pos, String str, int offest, int len)   
        {                                                                 
            // add code to test new methods                               
            return 0;                                                     
        }                                                                 
        public OutputStream setAsciiStream(long pos)                      
        {                                                                 
            // add code to test new methods                               
            return null;                                                  
        }                                                                 
        public Writer setCharacterStream(long pos)                        
        {                                                                 
            // add code to test new methods                               
            return null;                                                  
        }                                                                 
        public void truncate(long len)                                    
        {                                                                 
            // add code to test new methods                               
        }                                                                 
	public void free() {} 
    }



