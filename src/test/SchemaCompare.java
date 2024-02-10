package test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import test.misc.TableCompare;

public class SchemaCompare {
  public static void usage() {
    System.out.println("Usage: SchemaCompare <jdbcurl> <remoteRDB> <schema>");
  }

  public static void main(String args[]) {
    
    Vector errorList = new Vector(); 
    long totalErrorCount = 0; 
    StringBuffer outputBuffer = new StringBuffer();
    StringBuffer successOutputBuffer = new StringBuffer();
    long mismatchCount = 0;
    if (args.length < 3) {
      usage();
    } else {
      int sqlSystemErrorCount = 0;
      Connection connection = null;
      try {
        String jdbcUrl = args[0];
        String remoteRDB = args[1];
        String schema = args[2];
        System.out.println("Connecting to " + jdbcUrl);
        connection = DriverManager.getConnection(jdbcUrl);

        JDReflectionUtil.callMethod_V(connection, "setClientInfo",
            "CLIENT_APPLNAME", "test/SchemaCompare");
        // Change the default wait time so that the job does not wait long for a
        // lock
        Statement s = connection.createStatement();
        s.execute("CALL QSYS2.QCMDEXC('CHGJOB DFTWAIT(2) ')");
        s.close();
        Vector tableNames = new Vector();
        Vector tableTypes = new Vector();
        DatabaseMetaData dmd = connection.getMetaData();
        int rowCount = 0;
        {
          ResultSet rs = dmd.getTables(null, schema, "%", null);
          while (rs.next()) {
	      String tableName = rs.getString(3);
            String tableType = rs.getString(4);
	    if ("VIEW".equals(tableType)) {
		// Skip the views 
	    } else { 
		tableNames.add(tableName);
		tableTypes.add(tableType);
		rowCount++;

	    }
          }
          rs.close();
        }
        int currentRow = 0;
        long startTime = System.currentTimeMillis();
        for (currentRow = 0; currentRow < rowCount; currentRow++) {
          if (currentRow > 0) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            double timePerRow = (double) elapsedTime / currentRow;
            int rowsLeft = rowCount - currentRow;
            double estimateSecondsLeft = (timePerRow * rowsLeft) / 1000.0;
            System.out.println(" Estimating " + estimateSecondsLeft
                + " seconds left");
          }
          String tableName = (String) tableNames.elementAt(currentRow);
          if (tableName.indexOf(' ') > 0) {
            tableName = "\""+tableName+"\"";
          }
          String tableType = (String) tableTypes.elementAt(currentRow);
          
          System.out.println("Processing " + (currentRow +1 )+ "/" + rowCount + " "
              + schema+"."+ tableName + " type " + tableType);
          outputBuffer.setLength(0);
          successOutputBuffer.setLength(0); 
          int errorCount = TableCompare.compareTables(successOutputBuffer, outputBuffer, connection,
              schema + "." + tableName, remoteRDB + "." + schema + "."
                  + tableName);
          if (errorCount > 0) {
            totalErrorCount += errorCount; 
            String errorInfo = outputBuffer.toString();
            errorList.add(tableName); 
            System.out.println("================== ERROR ON " + tableName);
            System.out.println(errorInfo);
            System.out
                .println("==========================================================");
            if (errorInfo.indexOf("SQL system error") > 0) {
              sqlSystemErrorCount++;

              if (sqlSystemErrorCount >= 10) {
                System.out
                    .println("10  SQL system errors have been found.  Reconnecting to "
                        + jdbcUrl);
                connection = DriverManager.getConnection(jdbcUrl);
                JDReflectionUtil.callMethod_V(connection, "setClientInfo",
                    "CLIENT_APPLNAME", "test/SchemaCompare");
                sqlSystemErrorCount=0; 

              }
            }
          } else {
            System.out.println(successOutputBuffer.toString());
          }

        }

        System.out.println("test.SchemaCompare completed with "+totalErrorCount+" total Errors"); 
        long currentTime = System.currentTimeMillis();
        double  elapsedSeconds = (currentTime - startTime) / 1000.0 ;
        System.out.println("The comparision took " +elapsedSeconds+" seconds"); 
        System.out.println("The following "+errorList.size()+" tables had errors "); 
        Enumeration e = errorList.elements();
        while (e.hasMoreElements()) {
          String file = (String) e.nextElement();
          System.out.println(file); 
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    System.exit(0);
  }

}
