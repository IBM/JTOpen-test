package test.misc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;

import test.Testcase;

public class TableCompare {
  public static void usage() {
    System.out.println("Usage:  TableCompare <jdbcUrl> <TABLE1> <TABLE2>");
  }

  public static void main(String args[]) {
      
    StringBuffer outputBuffer = new StringBuffer(); 
    StringBuffer successOutputBuffer = new StringBuffer(); 
    long mismatchCount = 0;
    if (args.length < 3) {
      usage();
    } else {
      Connection connection = null;
      try {
        String jdbcUrl = args[0];
        String table1 = args[1];
        String table2 = args[2];
        System.out.println("Connecting to " + jdbcUrl);
        connection = DriverManager.getConnection(jdbcUrl);

        
        compareTables(successOutputBuffer, outputBuffer, connection, table1, table2);
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
      System.out.println(outputBuffer.toString()); 
    }
    System.exit(0);
  }

      
      
  /* Returns the number of miscompares */     
  public static int compareTables(StringBuffer successOutputBuffer, StringBuffer errorOutputBuffer, Connection connection, String table1, String table2)  {
    try { 
        long startTime = System.currentTimeMillis(); 
        
        int mismatchCount = 0; 
        String query1 = createQuery(connection, table1, errorOutputBuffer);
        String query2 = createQuery(connection, table2, errorOutputBuffer);

        Statement statement1 = connection.createStatement();
        Statement statement2 = connection.createStatement();
        try { 
          statement1.executeUpdate(table1.replace('.','_')+"_NOW_COMPARING"); 
        } catch (Exception e) { 
          
        }
        errorOutputBuffer.append("Query1=" + query1+"\n");
        ResultSet rs1 = statement1.executeQuery(query1);
        ResultSetMetaData rsmd1 = rs1.getMetaData();
        errorOutputBuffer.append("Query2=" + query2+"\n");
        ResultSet rs2 = statement2.executeQuery(query2);
        ResultSetMetaData rsmd2 = rs2.getMetaData();
        int rowCount = 0;
        while (rs1.next()) {
          rowCount++;
          if (rs2.next()) {
            if (rs1.getLong(1) != rs2.getLong(1)) {
              boolean mismatch = true;
              if (table1.indexOf(".SYS") > 0) {
                mismatch = !sysRowCompare(rs1, rsmd1, rs2, rsmd2); 
              }
              if (mismatch) { 
                mismatchCount++;
                errorOutputBuffer.append("Mismatch #"+mismatchCount+" on row " + rowCount+"\n");
                errorOutputBuffer.append("T1:" + showRow(rs1, rsmd1)+"\n");
                errorOutputBuffer.append("T2:" + showRow(rs2, rsmd2)+"\n");
              }
            } else {
              // Match -- keep going
            }
          } else {
            mismatchCount++;
            errorOutputBuffer.append("Extra row [" + rowCount + "] in " + table1
                + ": " + showRow(rs1, rsmd1)+"\n");
          }
        }
        while (rs2.next()) {
          mismatchCount++;
          errorOutputBuffer.append("Extra row [" + rowCount + "]  in " + table2
              + ": " + showRow(rs2, rsmd2)+"\n");
        }

        long endTime = System.currentTimeMillis(); 
        successOutputBuffer.append("  Compare took "+(endTime - startTime)+" milliseconds\n");
    successOutputBuffer.append("  Compare done with " + mismatchCount + " mismatches and "+rowCount+" rows \n");
    statement1.close(); 
    statement2.close();
    
    return mismatchCount; 
    } catch (Exception e) { 
     errorOutputBuffer.append("Exception on compare "); 
      Testcase.printStackTraceToStringBuffer(e, errorOutputBuffer);
      return 1; 
    }
   }
  
  /* Compare two rows of a system table.  View as match if timestamps are close enough */ 
  /* Returns true if the rows are the same */ 
  
  private static boolean sysRowCompare(ResultSet rs1, ResultSetMetaData rsmd1,
      ResultSet rs2, ResultSetMetaData rsmd2) throws SQLException {
    
    int columnCount1 = rsmd1.getColumnCount();
    int columnCount2 = rsmd2.getColumnCount();
    if (columnCount1 != columnCount2) {
      return false; 
    }
    /* Start with row 2 */ 
    for (int i = 2; i <= columnCount1; i++) {
      int type1 = rsmd1.getColumnType(i); 
      int type2 = rsmd2.getColumnType(i);
      if (type1 != type2) {
        return false; 
      }
      switch (type1) {
        case Types.TIMESTAMP:
        { 
          Timestamp value1 = rs1.getTimestamp(i); 
          Timestamp value2 = rs2.getTimestamp(i);
          if (value1 == null) { 
            if (value2 != null) return false;
          } if (value2 == null) { 
            if (value1 != null) return false;
          } else {
            long time1 = value1.getTime();
            long time2 = value2.getTime(); 
            long diff = time1 - time2; 
            if (diff < 0) diff = -diff; 
            /* if difference more than 12 hours */ 
            if (diff > 12 * 3600000) {  
              return false; 
            }
          }
           break;
        }
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.CLOB:
        { 
          byte[] value1 = rs1.getBytes(i); 
          byte[] value2 = rs2.getBytes(i);
          if (value1 == null) { 
            if (value2 != null) return false;
          } if (value2 == null) { 
            if (value1 != null) return false;
          } else {
            int len1 = value1.length; 
            int len2 = value2.length;
            if (len1 != len2) {
              return false; 
            }
            for (int j = 0; j < len1 ; j++) { 
              if (value1[j] != value2[j]) { 
                return false; 
              }
            }
            
          }
           break;
        }
        default:
        {
          String value1 = rs1.getString(i); 
          String value2 = rs2.getString(i);
          if (value1 == null) { 
            if (value2 != null) return false; 
          } else {
            if (! value1.equals(value2)) {
              return false; 
            }
          }
        }
        
      }
      
    }
      
      
    return true ;
  }

  private static String showRow(ResultSet rs, ResultSetMetaData rsmd)
      throws SQLException {
    StringBuffer rowBuffer = new StringBuffer();
    int columnCount = rsmd.getColumnCount();
    for (int i = 1; i <= columnCount; i++) {
      if (i > 1) {
        rowBuffer.append(",");
      }
      int columnType = rsmd.getColumnType(i);
      switch (columnType) {
      case Types.BINARY:
      case Types.VARBINARY:
      case Types.BLOB:
        byte[] bytes = rs.getBytes(i);
        addBytes(rowBuffer, bytes);
        break;
      default:
        rowBuffer.append(rs.getString(i));
      }
    }

    return rowBuffer.toString();
  }

  private static void addBytes(StringBuffer rowBuffer, byte[] bytes) {
    if (bytes == null) {
      rowBuffer.append("null");
    } else {
      rowBuffer.append("X'");
      for (int i = 0; i < bytes.length; i++) {
        String hex = Integer.toHexString(bytes[i] & 0xFF);
        if (bytes[i] >= 0 && bytes[i] <= 0x0f) {
          rowBuffer.append("0");
        }
        rowBuffer.append(hex);
      }
      rowBuffer.append("'");
    }
  }

  static String addToOrderByClause(String clause, String key, boolean addToFirst ) {
      // The SRCDAT key is sometimes bad and cannot be used
      // for a sort
      if (key.equals("SRCDAT")) {
	  return clause; 
      } else { 
	  if (clause == null || clause.length() == 0 ) {
	      return " ORDER BY "+key; 
	  } else {
	      if (addToFirst) {
		  return " ORDER BY " + key+", "+clause.substring(10);
	      } else {
		  return clause+", "+key;
	      }
	  }
      }
  } 
  private static String createQuery(Connection connection, String table, StringBuffer sb)
      throws SQLException {
    
    String keyOrderByClause=null; 
    
    sb.append("..Generating query for "+table+"\n"); 
    StringBuffer query = new StringBuffer();
    StringBuffer hashQuery = new StringBuffer();
    PreparedStatement ps = connection
        .prepareStatement("Select * from " + table);
    ResultSetMetaData rsmd = ps.getMetaData();
    int columnCount = rsmd.getColumnCount();
    for (int i = 1; i <= columnCount; i++) {
      int columnSize = rsmd.getPrecision(i); 
      if (i > 1) {
        hashQuery.append("+");
      }
      String columnName = rsmd.getColumnName(i);
      if (columnName.indexOf(' ') > 0) {
        columnName = "\"" + columnName+"\""; 
      }
      int columnType = rsmd.getColumnType(i);
      String columnTypeName = rsmd.getColumnTypeName(i);
      
      sb.append("...C#"+i+" name="+columnName+" type="+columnType+" size="+columnSize+" typeName="+columnTypeName+"\n"); 
      
      // if the column name has a . then it is a user defined type and 
      // HASH_VALUES will not work on it. Handle like binary 
      if (columnTypeName.indexOf(".") > 0) {
        columnType = Types.BINARY; 
      }
      
      query.append(",");
      switch (columnType) {
      case Types.CHAR:
      case Types.VARCHAR:
        if (columnSize > 10000) { 
          query.append("CAST("+columnName+" AS CLOB(40000) CCSID 1208)");
        } else {
          query.append(columnName);
        }

        break; 
      case Types.BINARY:
        if (columnSize > 16000) { 
          query.append("CAST("+columnName+" AS BLOB(40000))");
        } else {
          query.append(columnName);
        }
        break; 
        
      default: 
        query.append(columnName);
      }
      
      switch (columnType) {
      case Types.TIME:
      case Types.DATE:
        hashQuery.append("IFNULL(INTEGER(");
        hashQuery.append(columnName);
        hashQuery.append("),0)");
        keyOrderByClause = addToOrderByClause(keyOrderByClause, columnName, true); 
        break;
      case Types.TIMESTAMP:
        hashQuery.append("IFNULL(JULIAN_DAY(");
        hashQuery.append(columnName);
        hashQuery.append("),0)");
        hashQuery.append("+");
        hashQuery.append("IFNULL(MICROSECOND(");
        hashQuery.append(columnName);
        hashQuery.append("),0)");
        keyOrderByClause = addToOrderByClause(keyOrderByClause, columnName, true); 
        break;
      case Types.FLOAT:
      case Types.DOUBLE:
      case Types.REAL:
      case Types.DATALINK:
        hashQuery.append("IFNULL(HASH_VALUES(HEX(");
        hashQuery.append(columnName);
        hashQuery.append(")),0)");
        keyOrderByClause = addToOrderByClause(keyOrderByClause, columnName, false); 
        break;
      case Types.BINARY:
      case -8:   /* rowId */ 
        hashQuery.append("IFNULL(HASH_VALUES(HASH(");
        hashQuery.append(columnName);
        hashQuery.append(")),0)");
        break;
        
      case Types.CLOB:
      case Types.BLOB:
      case 2011:    /* NCLOB */ 
        hashQuery.append("IFNULL(HASH_VALUES(HASH(");
        hashQuery.append(columnName);
        hashQuery.append(")),0)");
        break;
      case 2009: /* Types.SQLXML */
        hashQuery.append("IFNULL(HASH_VALUES(XMLSERIALIZE(");
        hashQuery.append(columnName);
        hashQuery.append(" AS CLOB(2G) CCSID 1208)),0)");

        break;
      case Types.CHAR:
        /* 61625 -   CPF4318 from QQQVFORIG with HASH_VALUES and CHAR(32741) */ 
        
        if (columnSize > 15000) {
          hashQuery.append("IFNULL(HASH_VALUES(HASH(" + columnName + " )),0)");
        } else {
          hashQuery.append("IFNULL(HASH_VALUES(" + columnName + "),0)");
        }
        keyOrderByClause = addToOrderByClause(keyOrderByClause, columnName, false); 
        break; 
        
      default:
        hashQuery.append("IFNULL(HASH_VALUES(" + columnName + "),0)");
        keyOrderByClause = addToOrderByClause(keyOrderByClause, columnName, false); 
      }
    }

    String orderByClause = getOrderByClause(table); 
    if (orderByClause.length() == 0) {
      if (keyOrderByClause != null) { 
        orderByClause = keyOrderByClause; 
      }
    }
    return "SELECT " + hashQuery.toString() + query.toString() + " FROM "
        + table + orderByClause;
  }

  static Hashtable orderByHashtable = null; 
  private static String getOrderByClause(String table) {
    String[][] orderByClauses = {
        
        { "SYSCHKCST",  " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME " },
        { "SYSCOLUMNS", " order by TABLE_SCHEMA, TABLE_NAME,ORDINAL_POSITION" }, 
        { "SYSCST",     " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME,CONSTRAINT_TYPE" },
        { "SYSCSTCOL",  " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME " },
        { "SYSCSTDEP",  " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME" },
        { "SYSFIELDS",  " order by TABLE_SCHEMA,TABLE_NAME,ORDINAL_POSITION" }, 
        { "SYSINDEXES", " order by INDEX_SCHEMA,INDEX_NAME" },
        { "SYSKEYCST",  " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME" },
        { "SYSKEYS",    " order by INDEX_SCHEMA,INDEX_NAME" },
        { "SYSPACKAGE", " order by PACKAGE_SCHEMA,PACKAGE_NAME" },
        { "SYSREFCST",  " order by CONSTRAINT_SCHEMA,CONSTRAINT_NAME" },
        { "SYSTABLEDEP", " order by TABLE_SCHEMA,TABLE_NAME" },
        { "SYSTABLES", " order by TABLE_SCHEMA,TABLE_NAME" },
        { "SYSTRIGCOL", " order by TRIGGER_SCHEMA,TRIGGER_NAME,COLUMN_NAME" }, 
        { "SYSTRIGDEP", " order by TRIGGER_SCHEMA,TRIGGER_NAME" },
        { "SYSTRIGGERS", " order by TRIGGER_SCHEMA,TRIGGER_NAME" }, 
        { "SYSTRIGUPD", " order by TRIGGER_SCHEMA,TRIGGER_NAME" },
        { "SYSVIEWDEP", " order by VIEW_SCHEMA,VIEW_NAME" }, 
        { "SYSVIEWS", " order by SYSTEM_VIEW_SCHEMA,SYSTEM_VIEW_NAME" },

    };
    
    if (orderByHashtable == null) {
      orderByHashtable = new Hashtable(); 
      for (int i = 0; i < orderByClauses.length; i++) {
        orderByHashtable.put(orderByClauses[i][0],orderByClauses[i][1]);  
      }
    }
    String clause = null; 
  
    int dotIndex = table.lastIndexOf('.'); 
    if (dotIndex > 0) {
      table = table.substring(dotIndex+1); 
    }
  

    clause = (String) orderByHashtable.get(table); 
  
    if (clause == null) { 
      clause=""; 
    }
    return clause; 
  }
}
