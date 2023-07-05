///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JTATest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


public class TransInfo {

    // set default to null
   protected String state = null;
   protected String gtrid = null;
   protected String bqual = null;
   protected String collection = null; // this is actually in bqual in WRKCMTDFN


   /**
    * Generate a new/unused transaction identifier
    */
   public TransInfo() {
   }


   public void setState(String State) {
      state = State;
   }

   public void setGlobalTransactionId(String Gtrid) {
      gtrid = Gtrid;
   }

   public void setBranchQualifier(String Bqual) {
      bqual = Bqual;
   }

   public void setCollection(String Coll) {
      collection = Coll;
   }

   public String getState() {
      return state;
   }

   public String getGlobalTransactionId() {
      return gtrid;
   }
   
   public String getBranchQualifier() {
      return bqual;
   }
   
   public String getCollection() {
      return collection;
   }



   public String toString() {
      return "Transaction : L  " +
            " state=" + state + " gtrid=" + gtrid + " bqual=" + bqual + " coll=" + collection;
   }

}
