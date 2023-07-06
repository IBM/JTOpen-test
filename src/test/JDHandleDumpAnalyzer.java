///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDHandleDumpAnalyzer.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;
import java.io.*;
import java.util.*;
import java.sql.*;

public class JDHandleDumpAnalyzer {

    private static int staticLastAllocatedHandles;
    ResultSet rs_ = null;
  BufferedReader reader_ = null;
  String line_ = null;


  // Analysis variables
  public int lastAllocatedHandles = 0;
  public Hashtable connectionsHashtable = new Hashtable();
  public Hashtable connectionToStatementVectorHashtable = new Hashtable();
  public Hashtable connectionToReusableVectorHashtable = new Hashtable();
  public Hashtable connectionToDescriptorVectorHashtable = new Hashtable();
  public Hashtable statementsHashtable = new Hashtable();
  public Hashtable reusableHashtable   = new Hashtable();
  public Hashtable descriptorsHashtable = new Hashtable();
  public Hashtable descriptorToStatementHashtable = new Hashtable();
  public Hashtable descriptorToReusableHashtable = new Hashtable();

  public int maxConnection = 0;
  public int minConnection = 200000;
  public int maxStatement = 0;
  public int minStatement = 200000;
  public int maxDescriptor = 0;
  public int minDescriptor = 200000;
  public int maxReusable = 0;
  public int minReusable = 200000;
  public int minHandle = 0;
  public int maxHandle = 200000;

  public int statementNoConnectionCount = 0;
  public int descriptorNoConnectionCount = 0;
  public int descriptorNoStatementCount = 0;
  public int descriptorNoStatementOrReusableCount = 0;
  public int badDescriptorCount = 0;

  public int descriptorFromStatementCount = 0;
  public int descriptorFromReusableCount = 0;
  public int statementsWithDescriptors = 0;
  public int statementsWithoutDescriptors = 0;

  public int connectionCount = 0;
  public int statementCount = 0;
  public int descriptorCount = 0;
  public int reusableCount = 0;

  public int statementsPerConnectionMax = 0;
  public int descriptorsPerConnectionMax = 0;
  public int reusablePerConnectionMax = 0;

  public JDHandleDumpAnalyzer(ResultSet rs) {
    rs_ = rs;
  }

    public JDHandleDumpAnalyzer(File f) throws Exception {

    // Check to see if file is unicode
    FileInputStream is = new FileInputStream(f);
    byte[] mark = new byte[2];
    is.read(mark);
    is.close();
    if (mark[1] == (byte) 0
        || (mark[0] == (byte) 0xff && mark[1] == (byte) 0xfe)) {
      reader_ = new BufferedReader(new InputStreamReader(
          new FileInputStream(f), "UTF-16LE"));
    } else {
      reader_ = new BufferedReader(new FileReader(f));
    }
  }

    public boolean next() throws SQLException {
	try { 
	    if (rs_ != null) {
		return rs_.next();
	    } else {
		line_ = reader_.readLine();
		if (line_ != null) {
		    return true;
		} else {
		    return false;
		}
	    }
	} catch (IOException e) {
	    throw new SQLException(e.toString()); 
	} 
  }

    public String getString(int col) throws SQLException {
    if (rs_ != null) {
      return rs_.getString(col);
    } else {
      return line_;
    }
  }

  public void close() throws SQLException {
      try { 
	  if (rs_ != null) {
	      rs_.close();
	  } else {
	      reader_.close();
	  }
      } catch (IOException ex) {
	  throw new SQLException(ex.toString()); 
      } 
  }


    public static void usage() {
    System.out.println("java JDHandleDumpAnyalzer <jobNumber>");
    System.out.println("java JDHandleDumpAnyalzer <dumpFile>");
  }

    public static String[] getColumns(String line, int columnCount) {
    String[] output = new String[columnCount];
    int i = 0;
    int spaceIndex = line.indexOf(" ");
    while (i < columnCount && spaceIndex > 0) {
      String part = line.substring(0, spaceIndex);
      output[i] = part;
      line = line.substring(spaceIndex).trim();
      spaceIndex = line.indexOf(" ");
      i++;
    }
    /* get the last column */
    if (i < columnCount) {
      output[i] = line;
      i++;
    }
    while (i < columnCount) {
      output[i] = "NoColumn" + (i + 1) + "of" + columnCount;
      i++;
    }
    return output;
  }




  public void processDump(PrintStream out) throws SQLException {
    int state = 0; /* looking for headers */
    int statementLowRange = 0;
    int statementHighRange = 0;
    while (next()) {
      boolean printLine = true;
      String line = getString(1);
      line = line.trim();
      if (state == 0) {
        if (line.indexOf("Environment") >= 0) {
          if (out != null)
            out.println("Moving to environment");
          next();
          next();
          // Read first handle
          line = getString(1).trim();
          state = 1; /* Processing environment */
        }
      }
      if (state == 1) { /* Proessing environment */
        int intValue = 0;
        try {
          intValue = Integer.parseInt(line);
        } catch (NumberFormatException nfe) {
          // Skip it..
        }
        if (intValue > 0) {
          /* record it */
        } else {
          if (line.indexOf("Connection") >= 0) {
            if (out != null)
              out.println(line);
            // Skip second header line
            next();
            line = getString(1).trim();
            if (out != null)
              out.println(line);
            if (out != null)
              out.println("Moving to Connections");
            // Read first Connection
            next();
            line = getString(1).trim();
            state = 2; /* Processing connection */

          }
        }
      }
      if (state == 2) { /* processing connections */

        if (line.indexOf("-") >= 0) {
          // End of connections.. Skip it
          next();
          line = getString(1).trim();
        }
        if (line.indexOf("Statement") >= 0) {
          if (out != null)
            out.println(line);
          // Skip next header
          line = getString(1).trim();
          if (out != null)
            out.println(line);
          if (out != null)
            out.println("Moving to Statements");
          next();
          // Read first statment
          next();
          line = getString(1).trim();
          state = 3; /* Processing statements */

        } else {
          String[] columns = getColumns(line, 3);
          // out.println("Processing connection "+columns[0]);
          connectionsHashtable.put(columns[0], line);

          int thisConnection = Integer.parseInt(columns[0]);
          if (thisConnection > maxConnection)
            maxConnection = thisConnection;
          if (thisConnection < minConnection)
            minConnection = thisConnection;

          printLine = false;
        }
      } /* state = 2 .. processing connections */

      if (state == 3) { /* processing statements */

        if (line.indexOf("-") >= 0) {
          // End of connections.. Skip it
          next();
          line = getString(1).trim();
        }
        if (line.indexOf("Descriptor") >= 0) {
          if (out != null)
            out.println("Stmt Range: " + statementLowRange + " - "
                + statementHighRange);

          if (out != null)
            out.println(line);
          next();
          line = getString(1).trim();
          if (out != null)
            out.println(line);
          if (out != null)
            out.println("Moving to Descriptors");
          // Skip next header
          // Read first desciptor
          next();
          line = getString(1).trim();
          state = 4; /* Processing descriptors */

        } else {
          // Process the statement information
          // 0 = statement
          // 1 = cursorName
          // 2 = connectionHandle
          // 3 = ImpRow Desc
          // 4 = Impparam Desc
          // 5 = AppRow Desc
          // 6 = AppParam Desc

          if (line.indexOf("SQLCURSOR") < 0) {
            // No cursor add one
            line = line.substring(0, 10) + "SQLCURSORNONE" + line.substring(20);
          }
          String[] columns = getColumns(line, 7);
          // out.println("Procssing statement "+columns[0]);
          statementsHashtable.put(columns[0], line);
          // Save descriptor information
          boolean statementHasDescriptor = false;
          if (Integer.parseInt(columns[3]) > 0) {
            descriptorToStatementHashtable.put(columns[3], columns[0]);
            descriptorFromStatementCount++;
            statementHasDescriptor = true;
          }
          if (Integer.parseInt(columns[4]) > 0) {
            descriptorToStatementHashtable.put(columns[4], columns[0]);
            descriptorFromStatementCount++;
            statementHasDescriptor = true;
          }
	  if ( columns[5].indexOf("NoColumn") < 0 ) { 
	      if (Integer.parseInt(columns[5]) > 0) {
		  descriptorToStatementHashtable.put(columns[5], columns[0]);
		  descriptorFromStatementCount++;
		  statementHasDescriptor = true;
	      }
	      if (Integer.parseInt(columns[6]) > 0) {
		  descriptorToStatementHashtable.put(columns[6], columns[0]);
		  descriptorFromStatementCount++;
		  statementHasDescriptor = true;
	      }
	  }

          if (statementHasDescriptor) {
            statementsWithDescriptors++;
          } else {
            statementsWithoutDescriptors++;
          }

          // Check for valid connection
          if (connectionsHashtable.get(columns[2]) == null) {
            if (out != null)
              out.println("Warning: connection " + columns[2]
                  + " not found for statement " + columns[0]);
          } else {
            Vector statementVector = (Vector) connectionToStatementVectorHashtable
                .get(columns[2]);
            if (statementVector == null) {
              statementVector = new Vector();
              connectionToStatementVectorHashtable.put(columns[2],
                  statementVector);
            }
            statementVector.addElement(columns[0]);
          }
          int thisStatement = Integer.parseInt(columns[0]);
          if (thisStatement > maxStatement)
            maxStatement = thisStatement;
          if (thisStatement < minStatement)
            minStatement = thisStatement;

          if (statementLowRange == 0) {
            statementLowRange = thisStatement;
          } else {
            if (statementHighRange == 0) {
              if (thisStatement < statementLowRange + 100) {
                statementHighRange = thisStatement;
              } else {
                statementHighRange = statementLowRange;
              }
            }

            if (thisStatement < statementHighRange + 100) {
              statementHighRange = thisStatement;
            } else {
              if (out != null)
                out.println("Stmt Range: " + statementLowRange + " - "
                    + statementHighRange);
              statementLowRange = thisStatement;
              statementHighRange = 0;
            }

          }

          printLine = false;
        }

      }

      if (state == 4) { /* processing descriptors */

        if (line.indexOf("-") >= 0) {
          // End of connections.. Skip it
          next();
          line = getString(1);
	  if (line != null) {
	      line = line.trim();
	  } 
          state = 5;
        }
	
        if (line != null && line.indexOf("Reusable") >= 0) {
          if (out != null)
            out.println(line);
          // Skip next header
          if (out != null)
            out.println("Moving to Reusable");
          next();
          // Read first statment
          next();
          line = getString(1).trim();
          state = 5; /* processing reusable */
        } else  if (line != null) {
          // Process the descriptor information
          String[] columns = getColumns(line, 7);

          descriptorsHashtable.put(columns[0], line);

          // check for valid connection
          if (connectionsHashtable.get(columns[1]) == null) {
            if (out != null)
              out.println("Warning: connection " + columns[1]
                  + " not found for descriptor " + columns[0]);
            descriptorNoConnectionCount++;
          } else {
            Vector descriptorVector = (Vector) connectionToDescriptorVectorHashtable
                .get(columns[1]);
            if (descriptorVector == null) {
              descriptorVector = new Vector();
              connectionToDescriptorVectorHashtable.put(columns[1],
                  descriptorVector);
            }
            descriptorVector.addElement(columns[0]);
          }
          // check for valid statement
          if (descriptorToStatementHashtable.get(columns[0]) == null) {
            // out.println("Warning:  statement not found for descriptor "+columns[0]+" on connection "+columns[1]);
            descriptorNoStatementCount++;
          }

          int thisDescriptor = Integer.parseInt(columns[0]);
          if (thisDescriptor > maxDescriptor)
            maxDescriptor = thisDescriptor;
          if (thisDescriptor < minDescriptor)
            minDescriptor = thisDescriptor;

          // Check for Descriptors
          try {
	    int column4=0;
	    int column5=0;
	    int column6=0;
	    try {
		column4 = Integer.parseInt(columns[4]); 
	    } catch(Exception e) {}
	    try {
		column5 = Integer.parseInt(columns[5]); 
	    } catch(Exception e) {}

	    try {
		column6 = Integer.parseInt(columns[6]); 
	    } catch(Exception e) {}

            if (column4 > 0
                || column5 > 0
                || column6 > 0) {
              if (out != null)
                out.println("BAD DESC: " + line);
              badDescriptorCount++;
            }
          } catch (Exception e) {
            if (out != null)
              out.println("Exception on line " + line);
            if (out != null)
              e.printStackTrace(out);
          }
          printLine = false;
        }

      }
      if (state == 5) {
        if (line != null && line.indexOf("-") >= 0) {
          // End of reusable . Skip it
          next();
          state = 6;
        } else if (line != null) {
          // Process the reusable information
          // 0 = statement
          // 1 = type
          // 2 = connectionHandle
          // 3 = ImpRow Desc
          // 4 = Impparam Desc
          // 5 = AppRow Desc
          // 6 = AppParam Desc

          String[] columns = getColumns(line, 7);
          // out.println("Procssing reusable "+columns[0]+","+columns[1]+","+columns[2]+","+columns[3]+","+columns[4]+","+columns[5]+","+columns[6]);
          reusableHashtable.put(columns[0], line);
          // Save descriptor information
          if (Integer.parseInt(columns[3]) > 0) {
            descriptorToReusableHashtable.put(columns[3], columns[0]);
            descriptorFromReusableCount++;
          }
          if (Integer.parseInt(columns[4]) > 0) {
            descriptorToReusableHashtable.put(columns[4], columns[0]);
            descriptorFromReusableCount++;
          }
          if (Integer.parseInt(columns[5]) > 0) {
            descriptorToReusableHashtable.put(columns[5], columns[0]);
            descriptorFromReusableCount++;
          }
          if (Integer.parseInt(columns[6]) > 0) {
            descriptorToReusableHashtable.put(columns[6], columns[0]);
            descriptorFromReusableCount++;
          }

          // Check for valid connection
          if (connectionsHashtable.get(columns[2]) == null) {
            if (out != null)
              out.println("Warning: connection " + columns[2]
                  + " not found for reusable " + columns[0]);
          } else {
            Vector reusableVector = (Vector) connectionToReusableVectorHashtable
                .get(columns[2]);
            if (reusableVector == null) {
              reusableVector = new Vector();
              connectionToReusableVectorHashtable.put(columns[2],
                  reusableVector);
            }
            reusableVector.addElement(columns[0]);
          }
          int thisReusable = Integer.parseInt(columns[0]);
          if (thisReusable > maxReusable)
            maxReusable = thisReusable;
          if (thisReusable < minReusable)
            minReusable = thisReusable;

          printLine = false;
        }

      }

      if (state == 6) {
        printLine = false;
      }
      if (printLine && out != null)
        out.println(line);
    } /* while next() */

    close();
    minHandle = minConnection;
    if (minStatement < minHandle)
      minHandle = minStatement;
    if (minDescriptor < minHandle)
      minHandle = minDescriptor;
    if (minReusable < minHandle)
      minHandle = minReusable;

    maxHandle = maxConnection;
    if (maxStatement > maxHandle)
      maxHandle = maxStatement;
    if (maxDescriptor > maxHandle)
      maxHandle = maxDescriptor;
    if (maxReusable > maxHandle)
      maxHandle = maxReusable;

    connectionCount = connectionsHashtable.size();
    statementCount = statementsHashtable.size();
    descriptorCount = descriptorsHashtable.size();
    reusableCount = reusableHashtable.size();

    if (out != null)
      out
          .println("CONNECTION | STMTCOUNT | REUSEABLECCOUNT | DESCRIPTOR COUNT | ESTIMATED DESCRIPTOR COUNT");
    Enumeration enumeration = connectionsHashtable.keys();
    while (enumeration.hasMoreElements()) {
      String connectionString = (String) enumeration.nextElement();
      int connectionStatementCount;
      int connectionReusableCount;
      Vector statementVector = (Vector) connectionToStatementVectorHashtable
          .get(connectionString);
      if (statementVector == null) {
        connectionStatementCount = 0;
      } else {
        connectionStatementCount = statementVector.size();
      }

      if (connectionStatementCount > statementsPerConnectionMax)
        statementsPerConnectionMax = connectionStatementCount;

      Vector reusableVector = (Vector) connectionToReusableVectorHashtable
          .get(connectionString);
      if (reusableVector == null) {
        connectionReusableCount = 0;
      } else {
        connectionReusableCount = reusableVector.size();
      }

      if (connectionReusableCount > reusablePerConnectionMax)
        reusablePerConnectionMax = connectionReusableCount;

      int connectionDescriptorCount;
      Vector descriptorVector = (Vector) connectionToDescriptorVectorHashtable
          .get(connectionString);
      if (descriptorVector == null) {
        connectionDescriptorCount = 0;
      } else {
        connectionDescriptorCount = descriptorVector.size();
      }
      if (connectionDescriptorCount > descriptorsPerConnectionMax)
        descriptorsPerConnectionMax = connectionDescriptorCount;

      if (out != null)
        out.println(connectionString + " | " + connectionStatementCount + " | "
            + connectionReusableCount + " | " + connectionDescriptorCount + " | " + ( 4 * (connectionStatementCount + connectionReusableCount))) ;

    }

    /*
     * Calculate descriptorNoStatementOrReusableCount
     */
    Enumeration descriptorEnumeration = descriptorsHashtable.keys();
    while (descriptorEnumeration.hasMoreElements()) {
      String descriptor = (String) descriptorEnumeration.nextElement();
      if ((descriptorToStatementHashtable.get(descriptor) == null)
          && (descriptorToReusableHashtable.get(descriptor) == null)) {
        if (out != null)
          out
              .println("Warning:  statement or reusable not found for descriptor "
                  + descriptor
                  + " Line="
                  + descriptorsHashtable.get(descriptor));
        descriptorNoStatementOrReusableCount++;
      }

    }
    lastAllocatedHandles     = (1 + connectionCount + statementCount + descriptorCount );
    staticLastAllocatedHandles = lastAllocatedHandles;
  }

    public static void processDump(PrintStream out, String job) {


	Connection conn = null;
	Statement stmt = null ;
	String sql="";
	try
	{
	    JDHandleDumpAnalyzer hda = null;
	    File f = new File(job);
	    if (f.exists()) {
		out.println("Processing "+f);
		hda  = new JDHandleDumpAnalyzer(f);
	    } else {
		out.println("Querying job "+job+" using native JDBC driver");
		Class.forName("com.ibm.db2.jdbc.app.DB2Driver");
		conn = DriverManager.getConnection("jdbc:db2://*local");

		stmt = conn.createStatement();

		/* Requires V7R1:SI46016 and V6R1:SI46017. */
		sql = "CALL QSYS.QCMDEXC('crtpf file(qtemp/x) rcdlen(200)  SIZE(10000 1000 30000)           ', 0000000060.00000)";
		stmt.executeUpdate(sql);
		sql = "CALL QSYS.QCMDEXC('ovrdbf file(QPRINT) tofile(qtemp/x) lvlchk(*NO)  OVRSCOPE(*JOB)         ', 0000000070.00000)";
		stmt.executeUpdate(sql);
		out.println("Dumping handles for "+job);
		sql = "CALL QSYS.QSQDMPHA ('"+job+"')";
		stmt.executeUpdate(sql);

		sql = "select CAST(X AS CHAR(200) CCSID 37) from qtemp.x";

		hda = new JDHandleDumpAnalyzer( stmt.executeQuery(sql));
	    }

	    hda.processDump(out);


	    out.println();
	    out.println("Maximum statements / connection = "+hda.statementsPerConnectionMax);
	    out.println("Maximum reusable / connection = "+hda.reusablePerConnectionMax);
	    out.println("Maximum descriptors / connection = "+hda.descriptorsPerConnectionMax);


	    out.println("CONNECTIONS = "+ hda.connectionCount);
	    out.println("       min  = "+ hda.minConnection);
	    out.println("       max  = "+ hda.maxConnection);
	    out.println("STATEMENTS  = "+ hda.statementCount);
	    out.println("       min  = "+ hda.minStatement);
	    out.println("       max  = "+ hda.maxStatement);
	    out.println("STMTS x 4   = "+ ( 4 * hda.statementCount));
	    out.println("DESCRIPTORS = "+ hda.descriptorCount);
	    out.println("       min  = "+ hda.minDescriptor);
	    out.println("       max  = "+ hda.maxDescriptor);
	    out.println("TOTAL       = "+ (1 + hda.connectionCount + hda.statementCount + hda.descriptorCount ));
	    out.println("       min  = "+ hda.minHandle);
	    out.println("       max  = "+ hda.maxHandle);
	    out.println("REUSABLE    = "+ hda.reusableCount);
	    out.println("       min  = "+ hda.minReusable);
	    out.println("       max  = "+ hda.maxReusable);

	    out.println("Statements with invalid connection = "+	hda.statementNoConnectionCount);
	    out.println("Statements with    descriptors = "+hda.statementsWithDescriptors);
	    out.println("Statements without descriptors = "+hda.statementsWithoutDescriptors);

	    out.println("Descriptors with invalid connection = "+ hda.descriptorNoConnectionCount);
	    out.println("Descriptors with no statements = "+hda.descriptorNoStatementCount);
	    out.println("Descriptors with no statements or reusable = "+hda.descriptorNoStatementOrReusableCount);
	    out.println("Descriptors from statement   ="+hda.descriptorFromStatementCount);
	    out.println("Descriptors from reusable    ="+hda.descriptorFromReusableCount);
	    out.println("Referenced descriptor total  ="+(hda.descriptorFromStatementCount + hda.descriptorFromReusableCount));
	    out.println("Bad descriptor count           = "+hda.badDescriptorCount+ "   x4="+(4*hda.badDescriptorCount));
	    out.println("TOTAL - statementFreeDescriptors = "+(hda.connectionCount + hda.statementCount + hda.descriptorCount - hda.descriptorNoStatementCount ));
	    out.println("STATEMENTS / CONNECTIONS = "+((double) hda.statementCount / (double) hda.connectionCount));

	}
	catch(Exception e)
	{
	    out.println("Unexpected Exception: " + e.getMessage());
	    out.println("sql="+sql);
	    e.printStackTrace(out);
	    usage();

	}

    }

    public static int getLastAllocatedHandles() {
      return staticLastAllocatedHandles;
    }

    public static void main(String[] args)   {
	if (args.length < 1) {
	    usage(); 
	} else {
	    processDump(System.out, args[0]);
	}


    }

}

