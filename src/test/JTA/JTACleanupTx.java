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

import java.sql.*;
import java.util.*;

import javax.sql.XADataSource;

import java.io.*;
import java.net.InetAddress;


import com.ibm.as400.access.AS400JDBCXADataSource;

import test.JDReflectionUtil;

/**
 * Copied from JDBC Testbucket, test.TransInfo and test.TestXid
 */
class JTACleanupTxTransInfo {

  static boolean debug = false;

  // set default to null
  protected String state = null;
  protected String gtrid = null;
  protected String bqual = null;
  protected String collection = null; // this is actually in bqual in WRKCMTDFN

  /**
   * Generate a new/unused transaction identifier
   */
  public JTACleanupTxTransInfo() {
    if (System.getProperty("debug") != null) {
      debug = true;
    }
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
    return "Transaction :  " + " state=" + state + " gtrid=" + gtrid
        + " bqual=" + bqual + " coll=" + collection;
  }

  public static JTACleanupTxTransInfo[] getTransInfo() throws Exception {
    // int count = -1;

    if (System.getProperty("debug") != null) {
      debug = true;
    }

    Vector v = new Vector();

    String cmdA[] = new String[3];
    cmdA[0] = "/usr/bin/qsh";
    cmdA[1] = "-c";
    cmdA[2] = "system 'WRKCMTDFN JOB(*ALL) STATUS(*XOPEN) OUTPUT(*PRINT)'";
    System.out.println("Running: " + cmdA[0] + " " + cmdA[1] + " \"" + cmdA[2]
        + "\"");
    boolean end = false;
    Process p = Runtime.getRuntime().exec(cmdA);

    //
    // This won't end if the buffer fills up!!!!!!!
    // No need to wait.. Just read for the buffer. When the buffers empty,
    // the process will be done.
    // p.waitFor();

    InputStream is = p.getInputStream();
    InputStreamReader ir = new InputStreamReader(is, "Cp037");
    BufferedReader buf = new BufferedReader(ir);
    if (debug)
      System.out.println("..Reading lines from output");
    while (!end) {
      String s = buf.readLine();
      if (debug)
        System.out.println("Read line1:" + s);
      if (s == null)
        end = true;
      else {
        if (s.indexOf("X/Open global transaction") != -1) {
          s = buf.readLine();
          if (debug)
            System.out.println("Read line2:" + s);

          if ((s.indexOf("Transaction manager name") != -1)
              && ((s.indexOf("Q_UDB_JTA") != -1) || (s.indexOf("QZDATM") != -1))) { /*
                                                                                     * include
                                                                                     * toolbox
                                                                                     * XA
                                                                                     */
            int i;

            JTACleanupTxTransInfo match = new JTACleanupTxTransInfo();
            s = buf.readLine();
            if (debug)
              System.out.println("Read line3:" + s);
            {
              // count++;
            }
            if (s.indexOf("Relational database") >= 0) {
              /* Skip extra line added in V7R1 */
              s = buf.readLine();
              if (debug)
                System.out.println("Read line3:" + s);
            }

            if ((i = s.indexOf("Transaction branch state")) != -1) {
              // this line has state
              int j = s.indexOf(":  ", i);
              String state = s.substring(j + 3);
              state = state.trim();
              match.setState(state);
            } else {
              System.out
                  .println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for state: "
                      + s);
            }

            s = buf.readLine();
            if (debug)
              System.out.println("Read line4:" + s);
            if (s.indexOf("Format ID") != -1) {
              // Skip the format ID
              s = buf.readLine();
              if (debug)
                System.out.println("Read line5:" + s);
            }
            if ((i = s.indexOf("Global transaction identifier")) != -1) {
              // this line has gt id in this format.
              int startGtid = s.indexOf("X", i) + 2;
              int endGtid = startGtid + s.substring(startGtid).indexOf("'") - 1;
              if (endGtid < startGtid) {
                System.out
                    .println("WARNING getTransInfo() WRKCMTDFN: gtrid too "
                        + "long to handle simply in line: " + s);
                match.setGlobalTransactionId("none found");
              } else {
                // For Testcase XIDs, the GTRID is an 8 byte int.
                // We'll just take all of the data regardless of length.
                String gtid = s.substring(startGtid, endGtid + 1);
                match.setGlobalTransactionId(gtid);
              }
            } else {
              System.out
                  .println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for gtrid: "
                      + s);
            }

            s = buf.readLine();
            if (debug)
              System.out.println("Read line6:" + s);
            if ((i = s.indexOf("Branch qualifier")) != -1) {
              // this line has bqual
              int startBqual = s.indexOf("X", i) + 2;
              int endBqual = startBqual + s.substring(startBqual).indexOf("'")
                  - 1;
              if (endBqual < startBqual) {
                System.out
                    .println("WARNING getTransInfo() WRKCMTDFN: bqual too "
                        + "long to handle simply in line: " + s);
                match.setBranchQualifier("none found");
                match.setCollection("none found");
              } else {
                // For testcase XIDs, the BQUAL is an 8 byte int,
                // followed by the rest being the collection name.
                // If longer than 8 bytes (16 hex digits), we'll use
                // the rest as collection name.
                String fullbqual = s.substring(startBqual, endBqual + 1);
                if (fullbqual.length() > 16) {
                  match.setBranchQualifier(fullbqual.substring(0, 16));
                  match.setCollection(fullbqual.substring(16));
                } else {
                  match.setBranchQualifier(fullbqual);
                  match.setCollection("");
                }
              }
            } else {
              System.out
                  .println("WARNING getTransInfo() WRKCMTDFN: Unexpected line for bqual: "
                      + s);
            }

            // Add this TransInfo object to the vector
            v.addElement(match);

          }
        }
      }
    }
    if (debug)
      System.out.println("..Done reading lines from output");
    if (v.size() == 0) {
      return null;
    }
    // convert the vector into a TransInfo array and return it
    JTACleanupTxTransInfo[] tiArr = new JTACleanupTxTransInfo[v.size()];
    v.copyInto(tiArr);
    return tiArr;
  }
}

/**
 * NOTE NOTE NOTE: This Xid implementation should be compatible with the
 * test.TestXid class in such a way that the methods here provide behavior that
 * can identify a JTA testcase XID that was created as part of the JDBC/JTA test
 * bucket.
 */
class JTACleanupTxRecoveryTargeter {
  /**
   * The format ID currently used by all test.TestXid objects
   */
  protected final static int FMTID = 42;

  // Currently, all JTA Testcase Xid objects are targeted
  // by this tool
  protected int fmtid = FMTID;

  public JTACleanupTxRecoveryTargeter() {
  }

  public JTACleanupTxRecoveryTargeter(int targetID) {
    fmtid = targetID;
  }

  public void setTargetId(int targetID) {
    fmtid = targetID;
  }

  public boolean isATarget(Object xid) throws Exception {
    boolean aTarget = false;

    if (fmtid == -1) {
      aTarget = true;
    } else {
      if (JDReflectionUtil.callMethod_I(xid, "getFormatId") == fmtid) {
        aTarget = true;
      }
    }
    return aTarget;
  }

}

public class JTACleanupTx {

  public static void usage() {
    System.out
        .println("Usage: java JTACleanupTx <rdbname|localhost> 'ThisIsDangerous' [all]");
    System.out
        .println("  The parameter 'ThisIsDangerous' is required because this is\n"
            + "  a dangerous thing to do on a system that might have 'other than\n"
            + "  test' X/Open transactions on it\n"
            + "\n"
            + "  If the parameter 'all' is specified, then all transactions (not just\n"
            + "  transactions that appear to have been created by JTA testcases\n"
            + "  (currently identified by format ID == 42) are attempted to be rolled back\n"
            + "\n"
            + "  You can use WRKCMTDFN *ALL *XOPEN to look for JTA transaction \n"
            + "  that might be effected by this tool.\n"
            + "  Any transaction in an IDLE state will not be effected by this tool.\n"
            + "  Any transaction in a prepared or committed/rolledback state will be\n"
            + "  rolled back and/or forgotten about by this tool.\n");
    System.exit(1);
  }

  public static void main(String[] args) {
    try {
      System.out
          .println("Recover in-doubt or heuristically completed transactions");
      if (args.length < 2 || args.length > 3
          || !args[1].equals("ThisIsDangerous")) {
        usage();
      }

      if (args[0].equalsIgnoreCase("localhost")) {
        String localHost = InetAddress.getLocalHost().getHostName()
            .toLowerCase();
        int dotIndex = localHost.indexOf(".");
        if (dotIndex >= 0) {
          localHost = localHost.substring(0, dotIndex);
        }

        args[0] = localHost.toUpperCase();
      }
      boolean rollbackAll = false;
      if (args.length == 3) {
        if (!args[2].equals("all")) {
          usage();
        } else {
          rollbackAll = true;
        }
      }

      boolean masterDone = false;
      int resourceIndex = 0;
      do {

        Object xaConn = null;
        Object xaRes = null;
        // Connection conn;
        if (resourceIndex / 2 == 0) {
	    System.out
	      .println("Now, using NATIVE XAResource.recover() to find all "
		       + "transactions in-doubt transactions");

          System.out.println("resourceIndex="+resourceIndex+" using UDBXADataSource"); 
          XADataSource xaDs;
          xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");
          JDReflectionUtil.callMethod_V(xaDs,"setDatabaseName",args[0]);

          // Get the XAConnection.
          xaConn = xaDs.getXAConnection();
          xaDs = (XADataSource) JDReflectionUtil.createObject("com.ibm.db2.jdbc.app.UDBXADataSource");

        } else if ((resourceIndex / 2) == 1) {

	    System.out
	      .println("Now, using toolbox XAResource.recover() to find all "
		       + "transactions in-doubt transactions");

          System.out.println("resourceIndex="+resourceIndex+" using AS400JDBCXADataSource"); 

          AS400JDBCXADataSource xaDs = new AS400JDBCXADataSource();
          xaDs.setServerName(args[0]);

          // Get the XAConnection.
          xaConn = xaDs.getXAConnection();

        } else {
          System.out.println("resourceIndex="+resourceIndex+" DONE"); 
          masterDone = true;
        }
        resourceIndex ++; 
        
        if (!masterDone) {
          xaRes = JDReflectionUtil.callMethod_O(xaConn, "getXAResource");

          // conn =
          // (Connection)JDReflectionUtil.callMethod_O(xaConn,"getConnection");

          Object[] xids;

          xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes, "recover",
              javax.transaction.xa.XAResource.TMSTARTRSCAN);
          if (xids == null || xids.length == 0) {
            System.out
                .println("There are currently no In-Doubt transactions detected");
          } else {

            boolean done = false;
            // int count = 0;
            // It may be that the XIDs were heuristically rolled back or
            // committed
            // (If this testcase is being called with a 'doOnlyRecovery' value
            // of
            // true in order
            // to forget about existing transactions.
            boolean heuRollback = false;
            boolean heuCommit = false;
            boolean heuMix = false;
            // Construct a targeter to choose Xids that will be cleaned up
            JTACleanupTxRecoveryTargeter targeter = new JTACleanupTxRecoveryTargeter();
            if (rollbackAll) {
              targeter.setTargetId(-1);
            }

            while (!done) {
              System.out.println("Scan found " + xids.length
                  + " in-doubt transactions");
              // count += xids.length;
              for (int i = 0; i < xids.length; ++i) {
                // Skip any XIDs that are not targetted by this tool.
                if (!targeter.isATarget(xids[i])) {
                  System.out.println("Skipping non targeted Xid "
                      + xidToString(xids[i]));
                  continue;
                }
                // Attempt to roll back the thransaction.
                System.out.println("Rollback: " + xidToString(xids[i]));
                heuRollback = false;
                heuCommit = false;
                heuMix = false;
                try {
                  JDReflectionUtil.callMethod_V(xaRes, "rollback", xids[i]);
                  System.out.println("Rollback completed");
                } catch (Exception e) {
                  // Expect a couple of possible exceptions that aren't
                  // necessarily
                  // a bad thing.
                  System.out.println("Rollback rc="
                      + JDReflectionUtil.getField_I(e, "errorCode"));
                  if (JDReflectionUtil.getField_I(e, "errorCode") == javax.transaction.xa.XAException.XA_HEURCOM) {
                    System.out
                        .println("The transaction was already committed XA_HEURCOM");
                    heuCommit = true;
                  } else if (JDReflectionUtil.getField_I(e, "errorCode") == javax.transaction.xa.XAException.XA_HEURRB) {
                    System.out
                        .println("The transaction was already rolled back XA_HEURRB");
                    heuRollback = true;
                  } else if (JDReflectionUtil.getField_I(e, "errorCode") == javax.transaction.xa.XAException.XA_HEURMIX) {
                    System.out
                        .println("The transaction was already committed and rolled back XA_HEURMIX");
                    heuMix = true;
                  } else {
                    // This is a problem.
                    throw e;
                  }
                }
                if (heuRollback || heuCommit || heuMix) {
                  System.out
                      .println("Forget about heurisically completed transaction:"
                          + xidToString(xids[i]));
                  JDReflectionUtil.callMethod_V(xaRes, "forget", xids[i]);
                } else {
                  System.out.println("Skipped Forget");
                }
              }

              System.out.println("Finding more transactions");
              xids = (Object[]) JDReflectionUtil.callMethod_O(xaRes, "recover",
                  javax.transaction.xa.XAResource.TMNOFLAGS);
              if (xids == null || xids.length == 0) {
                System.out.println("End of in-doubt transactions");
                done = true;
              }
            }
          }
        }
      } while (!masterDone);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      e.printStackTrace();
    }

    System.out.println("Looking for more transactions");
    wrkcmtdfnBlock: try {
      JTACleanupTxTransInfo list[] = JTACleanupTxTransInfo.getTransInfo();
      if (list == null || list.length == 0) {
        System.out.println("No more transactions.");
        break wrkcmtdfnBlock;
      }
      System.out.println("Transactions still in flight:");
      for (int i = 0; i < list.length; ++i) {
        System.out.println(list[i]);
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      e.printStackTrace();
    }

    System.out.println("Done");
    /* System.exit(0); */


  }

  protected final static String hexDigits = "0123456789ABCDEF";

  protected final static void addHex(StringBuffer buf, byte b) {
    buf.append(hexDigits.charAt((int) ((b & 0xF0) >> 4)));
    buf.append(hexDigits.charAt((int) ((b & 0x0F))));
  }

  public final static String xidToString(Object x) throws Exception {
    int len = 64;
    int i;
    byte bqual[] = (byte[]) JDReflectionUtil.callMethod_O(x,
        "getBranchQualifier");
    byte gtrid[] = (byte[]) JDReflectionUtil.callMethod_O(x,
        "getGlobalTransactionId");
    int formatId = JDReflectionUtil.callMethod_I(x, "getFormatId");

    // Try to estimate the length of the string buffer
    if (bqual != null) {
      len += bqual.length * 2;
    }
    if (gtrid != null) {
      len += gtrid.length * 2;
    }
    StringBuffer id = new StringBuffer(len);
    id.append("Xid :");
    id.append("  Fmt=");
    id.append(formatId);
    id.append("  GTrid=");
    if (gtrid == null) {
      id.append("(null)");
    } else {
      id.append("0x");
      for (i = 0; i < gtrid.length; ++i) {
        addHex(id, gtrid[i]);
      }
    }
    id.append("  BQual=");
    if (bqual == null) {
      id.append("(null)");
    } else {
      id.append("0x");
      for (i = 0; i < bqual.length; ++i) {
        addHex(id, bqual[i]);
      }
    }
    return id.toString();
  }
}
