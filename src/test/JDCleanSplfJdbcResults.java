///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDCleanSplfJdbcResult.java
// 
// Clean spools files using JDBC 
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;

public class JDCleanSplfJdbcResults {
  public long deleteCount;
  public long deleteBytes;
  public long keepCount;
  public long keepBytes;

  public long processedCount;
  public long processSeconds;

  public JDCleanSplfJdbcResults(long deleteCount, long deleteBytes, long keepCount, long keepBytes, long processedCount,
      long processSeconds) {
    this.deleteCount = deleteCount;
    this.deleteBytes = deleteBytes;
    this.keepCount = keepCount;
    this.keepBytes = keepBytes;
    this.processedCount = processedCount;
    this.processSeconds = processSeconds;
  }

  public JDCleanSplfJdbcResults(JDCleanSplfJdbcResults a, JDCleanSplfJdbcResults b) {
    this.deleteCount = a.deleteCount + b.deleteCount;
    this.deleteBytes = a.deleteBytes + b.deleteBytes;
    this.keepCount = a.keepCount + b.keepCount;
    this.keepBytes = a.keepBytes + b.keepBytes;
    this.processedCount = a.processedCount + b.processedCount;
    this.processSeconds = a.processSeconds + b.processSeconds;
  }

  public JDCleanSplfJdbcResults(JDCleanSplfJdbcResults a, JDCleanSplfJdbcResults b, JDCleanSplfJdbcResults c,
      JDCleanSplfJdbcResults d) {
    this.deleteCount = a.deleteCount + b.deleteCount + c.deleteCount + d.deleteCount;
    this.deleteBytes = a.deleteBytes + b.deleteBytes + c.deleteBytes + d.deleteBytes;
    this.keepCount = a.keepCount + b.keepCount + c.keepCount + d.keepCount;
    this.keepBytes = a.keepBytes + b.keepBytes + c.keepBytes + d.keepBytes;
    this.processedCount = a.processedCount + b.processedCount + c.processedCount + d.processedCount;
    this.processSeconds = a.processSeconds + b.processSeconds + c.processSeconds + d.processSeconds;
  }
}