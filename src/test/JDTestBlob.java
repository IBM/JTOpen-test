///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDTestBlob.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;    
import java.io.Serializable;
import java.sql.Blob;



    public class JDTestBlob
    implements Blob, Serializable {
        /**
       * 
       */
      private static final long serialVersionUID = 1L;
        private byte[] data_;

        public JDTestBlob (byte[] data) { data_ = data;}

        // We need this function to work now.
        public InputStream getBinaryStream () {
            return new ByteArrayInputStream(data_);
        }

        public InputStream getBinaryStream (long pos, long length) {

            return new ByteArrayInputStream(data_, (int)pos, (int) length);
        }

        public long position (byte[] pattern, long start) { return -1;}
        public long position (Blob pattern, long start) { return -1;}

        public byte[] getBytes (long start, int length)
        {
            // Not correct, but good enough for here.
            return data_;
        }

        public long length ()
        {
            return data_.length;
        }
        public int setBytes(long pos, byte[] bytes)                        
        {                                                                  
            // add code to test new methods                                
            return 0;                                                      
        }                                                                  
        public int setBytes(long pos, byte[] bytes, int offest, int len)   
        {                                                                  
            // add code to test new methods                                
            return 0;                                                      
        }                                                                  
        public OutputStream setBinaryStream(long pos)                      
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



