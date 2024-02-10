///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.JTA;

import java.util.*;

import test.JTATest;

public class JTATestXid
implements javax.transaction.xa.Xid                           // @A1A
{
   protected final static int FMTID = 42;

   // Default to the NULL XID.
   protected int            formatId                   = -1;
   protected byte           gtrid[]                    = new byte[8];
   protected byte           bqual[]                    = new byte[28];
   protected long           myGtrid;
   protected long           myBqual;
   protected String         myColl;

   // These guys are used as a mechanism to generate new and unusued
   // transaction IDs.
   protected static Object       lockGen     = new Object();
   //
   // Use a GTRID that starts with a prefix
   //
   protected static long         gtridGen    =   1;
   // protected static long         bqualGen    = 1;

   /** The format ID used by all TestXid objects */
   public final static int theFormatId() {
      return FMTID;
   }

   public static void setGtridPrefix(long prefix)  {
       // Sets the first four bytes of the GTRID used by the generator to
       // a specific byte pattern
       long mask = 0xFFFFFFFF00000000L; 
       prefix = prefix & mask; 
       synchronized(lockGen) {
         gtridGen = gtridGen & mask; 
         gtridGen = gtridGen | prefix; 
       }
   } 


   private void setByteValues() {
      gtrid[0] = (byte)(0xFF & (myGtrid >> 56));
      gtrid[1] = (byte)(0xFF & (myGtrid >> 48));
      gtrid[2] = (byte)(0xFF & (myGtrid >> 40));
      gtrid[3] = (byte)(0xFF & (myGtrid >> 32));
      gtrid[4] = (byte)(0xFF & (myGtrid >> 24));
      gtrid[5] = (byte)(0xFF & (myGtrid >> 16));
      gtrid[6] = (byte)(0xFF & (myGtrid >>  8));
      gtrid[7] = (byte)(0xFF & (myGtrid >>  0));

      // The reset is needed for the collection option (and Crash2)
      for (int i = 0; i < bqual.length; i++)
         bqual[i] = 0;

      bqual[0] = (byte)(0xFF & (myBqual >> 56));
      bqual[1] = (byte)(0xFF & (myBqual >> 48));
      bqual[2] = (byte)(0xFF & (myBqual >> 40));
      bqual[3] = (byte)(0xFF & (myBqual >> 32));
      bqual[4] = (byte)(0xFF & (myBqual >> 24));
      bqual[5] = (byte)(0xFF & (myBqual >> 16));
      bqual[6] = (byte)(0xFF & (myBqual >>  8));
      bqual[7] = (byte)(0xFF & (myBqual >>  0));
      byte[] b = myColl.getBytes(); // use default encoding
      System.arraycopy(b, 0, bqual, 8, b.length); // append b to bqual

   }

   /**
    * Generate a new/unused transaction identifier
    */
   public JTATestXid(int f) {
      synchronized (lockGen) {
         myGtrid = gtridGen;
         // myBqual = bqualGen;
         ++gtridGen;
         // ++bqualGen;
         Date     time = new Date();
         myBqual = time.getTime();
         // Just a safeguard under protection of the lock so there is no
         // possible way to get two transactions with the same millisecond
         // branch qualifier.
         try {
            Thread.sleep(1);
         }
         catch (InterruptedException e) {
         }
      }
      if (f != -1) {
         formatId = f;
      }
      else {
         formatId = theFormatId();
      }
      myColl = JTATest.COLLECTION;
      setByteValues();
   }
   public JTATestXid() {
      this(theFormatId());
   }

/* This was created for JTACrash2Vars.java */

   public void setColl(String collection) {
      myColl = collection;
      setByteValues();
   }

   public void setFormatId(int f) {
      formatId = f;
   }
   public int getFormatId() {
      return formatId;
   }
   public byte[] getGlobalTransactionId() {
      return gtrid;
   }
   public byte[] getBranchQualifier() {
      return bqual;
   }


   public JTATestXid(String xidToString) {
      // constructs the xid from xid.toString() string
      // TestXid : L   fmt=42 gtrid=1 bqual=933669782566 coll=MYCOLL
      // TestXid : [b  fmt=0x0000002A gtrid=0x0000000000000001 bqual=0x000000D9630E4426
      if (xidToString.indexOf("fmt=") == -1)
         throw new IllegalArgumentException(xidToString);
      else {
         int f1 = xidToString.indexOf("fmt=") + 4;
         int f2 = xidToString.indexOf(" ", f1);
         String fidStr = xidToString.substring(f1,f2);
         formatId = Integer.parseInt(fidStr);

         if (xidToString.indexOf("gtrid=") == -1)
            throw new IllegalArgumentException(xidToString);
         else {
            int g1 = xidToString.indexOf("gtrid=") + 6;
            int g2 = xidToString.indexOf(" ", g1);
            String gidStr = xidToString.substring(g1,g2);
            myGtrid = Long.parseLong(gidStr);

            if (xidToString.indexOf("bqual=") == -1)
               throw new IllegalArgumentException(xidToString);
            else {
               int b1 = xidToString.indexOf("bqual=") + 6;
               int b2 = xidToString.indexOf(" ", b1);
               String bqStr = xidToString.substring(b1,b2);
               myBqual = Long.parseLong(bqStr);

               if (xidToString.indexOf("coll=") == -1)
                  throw new IllegalArgumentException(xidToString);
               else {
                  int c1 = xidToString.indexOf("coll=") + 5;
                  int c2 = xidToString.indexOf(" ", c1);
                  String coStr = xidToString.substring(c1,c2);
                  myColl = coStr;

                  setByteValues();
               }
            }
         }
      }
   }

   // Any changes made to toString() should be reflected in the TestXid(String) constructor

   public String toString() {
      return "TestXid : L  " +
            " fmt=" + formatId + " gtrid=" + myGtrid + " bqual=" + myBqual + " coll=" + myColl +
            " \n" +
            "TestXid : [b " +
            " fmt=" + asHex(formatId) +
            " gtrid=" + asHex(gtrid) +
            " bqual=" + asHex(bqual);
   }

   public boolean match(JTATransInfo xaInfo) {
      JTATest.verboseOut("Match   g=" + xaInfo.gtrid +
                         ", b=" + xaInfo.bqual);
      JTATest.verboseOut("This [b g=" + asHex(gtrid) +
                         ", b=" +
                         asHex(bqual));

      /* @A2 -- it is possible that we have a "none found" string stored in our global transaction id or brach qualifier variables
       *	     if this is the case, they will never match the ids that we create, it is also possible that the gtrid or bqual
       *	     strings are too large to be stored in a long primitive without an overflow occuring
       */
      long gt = 0,
           bq = 0;
      try{
	 gt = Long.parseLong(xaInfo.gtrid, 16);
	 bq = Long.parseLong(xaInfo.bqual, 16);
      } catch(NumberFormatException e){
	 //
	 // Detect if this come from another testcase we know about. If
         // so, return false
	 //
	  if (xaInfo.gtrid.indexOf("7478") == 0) {
	      // Silently return false 
	      return false; 
	  } else { 
	      System.out.println("xaInfo.gtrid: " + xaInfo.gtrid);
	      System.out.println("xaInfo.bqual: " + xaInfo.bqual);
	      System.out.println("Long.parseLong was unable to successfully parse xaInfo.gtrid or xaInfo.bqual strings -- returning false to indicate no match");
	      e.printStackTrace();	  
	      return false;
	  }
      }

      byte[] cl = new byte[40];
      int count = 0;
      for (int i = 0; (i < (xaInfo.collection.length()-1) && i < 40) ; i+=2) {
         String ss = xaInfo.collection.substring(i, i+2);
         byte b = Byte.parseByte(ss, 16);
         cl[count] = b;
         count++;
      }
      String convColl = new String(cl).trim();
      boolean matches = ((gt == myGtrid) && (bq == myBqual) && (convColl.equalsIgnoreCase(myColl)));
      return matches;
   }

   protected static String hexDigits = "0123456789ABCDEF";
   protected static void hex(StringBuffer buf, int n) {
      buf.append(hexDigits.charAt((int)((n & 0xF0000000) >> 28)));
      buf.append(hexDigits.charAt((int)((n & 0x0F000000) >> 24)));
      buf.append(hexDigits.charAt((int)((n & 0x00F00000) >> 20)));
      buf.append(hexDigits.charAt((int)((n & 0x000F0000) >> 16)));
      buf.append(hexDigits.charAt((int)((n & 0x0000F000) >> 12)));
      buf.append(hexDigits.charAt((int)((n & 0x00000F00) >>  8)));
      buf.append(hexDigits.charAt((int)((n & 0x000000F0) >>  4)));
      buf.append(hexDigits.charAt((int)((n & 0x0000000F)      )));
   }
   protected static void hex(StringBuffer buf, byte b) {
      buf.append(hexDigits.charAt((int)((b & 0xF0) >>  4)));
      buf.append(hexDigits.charAt((int)((b & 0x0F)      )));
   }
   /**
    * Return the hex representation of the integer 0xdddddddd
    */
   public static String asHex(int n) {
      StringBuffer      buf = new StringBuffer(10);
      buf.append('0');
      buf.append('x');
      hex(buf, n);
      return buf.toString();
   }
   /*
    * Return the hex representation of the byte array
    */
   public static String asHex(byte b[]) {
      return asHex(b, b.length);
   }
   /*
    * Return the hex representation of the byte array, stopping
    * at the length of the array or the length specified.
    */
   public static String asHex(byte b[], int maxLen) {
      int               len = b.length;
      if (maxLen < len) {
         len = maxLen;
      }
      StringBuffer      buf = new StringBuffer(2+maxLen*2);
      if (len > 0) {
         buf.append('0');
         buf.append('x');
      }
      for (int i=0; i<len; ++i) {
         hex(buf, b[i]);
      }
      return buf.toString();
   }
}
