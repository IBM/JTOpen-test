///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSetupProcedure.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCDriver;

import test.JD.JDSetupCollection;
import test.JD.JDTestUtilities;

import java.math.BigDecimal;
//import java.math.*;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Hashtable;

//import java.sql.*;

/**
 * The JDSetupProcedure class creates stored procedures for the JDBC test
 * drivers and testcases.
 **/
public class JDSetupProcedure {

  // Private data.
  public static String COLLECTION = "JDTESTSTP";

  private static boolean collectionCreated_ = false;

  // Stored procedures. @C1C
  public static String STP_RS0 = COLLECTION + ".JDRSNONE";
  public static String STP_RS1 = COLLECTION + ".JDRSONE";
  public static String STP_RS1UPD = COLLECTION + ".JDRSUPDT"; // for update
  public static String STP_RS1WH = COLLECTION + ".JDRSOWH"; // with hold
  public static String STP_RS1WOH = COLLECTION + ".JDRSOWOH"; // without hold
  public static String STP_RS3CUR = COLLECTION + ".JDRS3CUR"; // 3 cursors with
                                                              // various attrs
  public static String STP_RS1S = COLLECTION + ".JDRSONES";
  public static String STP_RS3 = COLLECTION + ".JDRSTHREE";

  public static String STP_CS0 = COLLECTION + ".JDCSNONE";
  public static String STP_CS1 = COLLECTION + ".JDCSONE"; // @E1A
  public static String STP_CSRV = COLLECTION + ".JDCSRV"; // @C2A
  public static String STP_CSPARMS = COLLECTION + ".JDCSPARMS";
  public static String STP_CSPARMSRS = COLLECTION + ".JDCSPARMSRS";
  public static String STP_CSPARMSRV = COLLECTION + ".JDCSPARMSRV"; // @C2A
  public static String STP_CSPARMSRSRV = COLLECTION + ".JDCSPARMSRSRV"; // @C2A
  public static String STP_CSTYPESOUT = COLLECTION + ".JDCSTYPESOUT";
  // With XML values
  public static String STP_CSTYPESOUTX = COLLECTION + ".JDCSTYPESOUTX";
  // Types out with compatible byte values
  public static String STP_CSTYPESOUTB = COLLECTION + ".JDCSTYPESOUTB";
  public static String STP_CSTYPESIN = COLLECTION + ".JDCSTYPESIN";
  public static String STP_CSTYPESINOUT = COLLECTION + ".JDCSTYPESIO";
  // Variation with XML compatible output in LOBS
  public static String STP_CSTYPESINOUTX = COLLECTION + ".JDCSTYPESIOX";
  public static String STP_CSTYPESINOUTX_CLOB_OUTPUT = "<d>Welcome</d>";
  public static String STP_CSTYPESINOUTX_DBCLOB_OUTPUT = "<d>Goodbye</d>";
  public static String STP_CSTYPESNULL = COLLECTION + ".JDCSTYPESNL";
  public static String STP_CSINOUT = COLLECTION + ".JDCSIO"; // @D1A
  public static String STP_CSNULLTEST = COLLECTION + ".JDCSNULLTEST";
  public static String STP_CSSRS = COLLECTION + ".JDCSSRS"; // @KBA
  public static String STP_CSMSRS = COLLECTION + ".JDCSMSRS"; // @KBA
  public static String STP_CSARRSUM = COLLECTION + ".JDCSARRSUM"; // arrays
  public static String STP_CSARRINT = COLLECTION + ".JDCSARRINT"; // arrays
  public static String STP_CSARRINT2 = COLLECTION + ".JDCSARRINT2"; // arrays
  public static String STP_CSARRINT2R = COLLECTION + ".JDCSARRINT2R"; // arrays

  public static String STP_CSARRINT3 = COLLECTION + ".JDCSARRINT3";
  public static String STP_CSARRINTN = COLLECTION + ".JDCSARRINTN";
  public static String STP_CSARRINT4 = COLLECTION + ".JDCSARRINT4";
  public static String STP_CSARRVCH3 = COLLECTION + ".JDCSARRVCH3";
  public static String STP_CSARRSIN = COLLECTION + ".JDCSARRSIN"; // array of
                                                                  // smallints
  public static String STP_CSARRIN = COLLECTION + ".JDCSARRIN"; // array of ints
  public static String STP_CSARRIN2 = COLLECTION + ".JDCSARRIN2"; // array of
                                                                  // ints and
                                                                  // non-array
                                                                  // parms
  public static String STP_CSARRBIN = COLLECTION + ".JDCSARRBIN"; // array of
                                                                  // bigints
  public static String STP_CSARRBOO = COLLECTION + ".JDCSARRBOO"; // array of
                                                                  // booleans
  public static String STP_CSARRREA = COLLECTION + ".JDCSARRREA"; // array of
                                                                  // read
  public static String STP_CSARRFLO = COLLECTION + ".JDCSARRFLO"; // array of
                                                                  // float
  public static String STP_CSARRDOU = COLLECTION + ".JDCSARRDOU"; // array of
                                                                  // double
  public static String STP_CSARRDEC = COLLECTION + ".JDCSARRDEC"; // array of
                                                                  // decimal
  public static String STP_CSARRNUM = COLLECTION + ".JDCSARRNUM"; // array of
                                                                  // numeric
  public static String STP_CSARRCH1 = COLLECTION + ".JDCSARRCH1"; // array of
                                                                  // char(1)
  public static String STP_CSARRCH50 = COLLECTION + ".JDCSARRCH50"; // array of
                                                                    // char(50)
  public static String STP_CSARRVCH = COLLECTION + ".JDCSARRVCH"; // array of
                                                                  // varchar(50)
  public static String STP_CSARRVCH2 = COLLECTION + ".JDCSARRVCH2"; // array of
                                                                    // varchar(50)
                                                                    // and
                                                                    // non-array
                                                                    // parms
  public static String STP_CSARRGR = COLLECTION + ".JDCSARRGR"; // array of
                                                                // graphic
  public static String STP_CSARRVGR = COLLECTION + ".JDCSARRVGR"; // array of
                                                                  // vargraphic
  public static String STP_CSARRCLO = COLLECTION + ".JDCSARRCLO"; // array of
                                                                  // CLOB(100)
  public static String STP_CSARRBLO = COLLECTION + ".JDCSARRBLO"; // array of
                                                                  // bLOB(100)
  public static String STP_CSARRDAT = COLLECTION + ".JDCSARRDAT"; // array of
                                                                  // date
  public static String STP_CSARRTIM = COLLECTION + ".JDCSARRTIM"; // array of
                                                                  // tim
  public static String STP_CSARRTS = COLLECTION + ".JDCSARRTS"; // array of
                                                                // timestame
  public static String STP_CSARRBY = COLLECTION + ".JDCSARRBY"; // array of
                                                                // binary
  public static String STP_CSARRVBY = COLLECTION + ".JDCSARRVBY"; // array of
                                                                  // varbinary
  public static String STP_CSARRXML = COLLECTION + ".JDCSARRXML"; // array of
                                                                  // xml

  public static Hashtable procedureDefinitions = new Hashtable();

  public static String getProcedureDefinition(String stp) {
    String def = (String) procedureDefinitions.get(stp);
    if (def == null)
      def = "NOT FOUND";
    return def;
  }
/* create a stored procedure
    @param  system      The system.
    @param  password    The password.
    @param  connection  The connection.
    @param  stp         The stored procedure.
    **/
    static void create (AS400 system,
                        
                        Connection connection,
                        String stp)
    throws Exception
    {
        JDSupportedFeatures supportedFeatures = new JDSupportedFeatures();
        create (system,  connection, stp, supportedFeatures);                       
    }

  /**
   * Determines if a connection is to an IASP
   **/
  public static boolean isIASP(Connection connection) {

    if (JDTestDriver.getDatabaseTypeStatic() != JDTestDriver.DB_SYSTEMI) {
      return false;
    }
    String sql = "NOT SET";
    try {
      Statement stmt = connection.createStatement();
      // If this query has rows then we're on a IASP
      sql = "SELECT distinct IASP_NUMBER FROM qsys2.systables "
          + "where IASP_NUMBER > 0 ";
      ResultSet rs = stmt.executeQuery(sql);
      boolean hasRows = rs.next();
      /*
       * if (hasRows) { System.out.println("System is an IASP with #"+
       * rs.getString(1)); } else { System.out.println("System is NOT IASP"); }
       */
      rs.close();
      stmt.close();

      return hasRows;

    } catch (Exception e) {
      System.out.println("WARNING:  Error running " + sql);
      System.out
          .println("This exception can be ignored if running to LUW system");
      e.printStackTrace();
      return false;
    }

  }

  /**
   * Determines if a connection is to LUW system
   **/
  static boolean isLUW(Connection connection) {

    if (JDTestDriver.getDatabaseTypeStatic() != JDTestDriver.DB_LUW) {
      return false;
    }
    return true;

  }

  /* */
  static String[][] QCUSTCDTrows = {
      /*
       * CUSNUM,LSTNAM,INIT,STREET,CITY,STATE,ZIPCOD,CDTLMT,CHGCOD,BALDUE,CDTDUE
       */
      { "938472", "Henning", "G K", "4859 Elm Ave", "Dallas", "TX", "75217",
          "5000", "3", "37.00", "0.00" },
      { "839283", "Jones", "B D", "21B NW 135 St", "Clay", "NY", "13041", "400",
          "1", "100.00", "0.00" },
      { "392859", "Vine", "S S", "PO Box 79", "Broton", "VT", "5046", "700",
          "1", "439.00", "0.00" },
      { "938485", "Johnson", "J A", "3 Alpine Way", "Helen", "GA", "30545",
          "9999", "2", "3987.50", "33.50" },
      { "397267", "Tyron", "W E", "13 Myrtle Dr", "Hector", "NY", "14841",
          "1000", "1", "0.00", "0.00" },
      { "389572", "Stevens", "K L", "208 Snow Pass", "Denver", "CO", "80226",
          "400", "1", "58.75", "1.50" },
      { "846283", "Alison", "J S", "787 Lake Dr", "Isle", "MN", "56342", "5000",
          "3", "10.00", "0.00" },
      { "475938", "Doe", "J W", "59 Archer Rd", "Sutter", "CA", "95685", "700",
          "2", "250.00", "100.00" },
      { "693829", "Thomas", "A N", "3 Dove Circle", "Casper", "WY", "82609",
          "9999", "2", "0.00", "0.00" },
      { "593029", "Williams", "E D", "485 SE 2 Ave", "Dallas", "TX", "75218",
          "200", "1", "25.00", "0.00" },
      { "192837", "Lee", "F L", "5963 Oak St", "Hector", "NY", "14841", "700",
          "2", "489.50", "0.50" },
      { "583990", "Abraham", "M T", "392 Mill St", "Isle", "MN", "56342",
          "9999", "3", "500.00", "0.00" } };

  /**
   * setup QIWS or equivlant for testcase
   */
  public static String setupQIWS(AS400 system,
      Connection connection) {
    String dataCollection = "QIWS";
    //
    // if running to an IASP then copy the necessary files into the IASP
    //
    if (isIASP(connection)) {
      try {
        Statement stmt = connection.createStatement();
        dataCollection = "QIWSIASP";
        JDSetupCollection.create(system,  connection, dataCollection);
        /* System.out.println(dataCollection + " created."); */
        String file = "QCUSTCDT";
        try {
          stmt.execute("SELECT * From " + dataCollection + "." + file);
          // already exists continue.
        } catch (Exception e) {
          // doesn't exit create.
          String sql = "CALL QSYS.QCMDEXC('CRTDUPOBJ OBJ(" + file
              + ") FROMLIB(QIWS) OBJTYPE(*FILE) TOLIB(" + dataCollection
              + ") ASPDEV(*SYSBAS) TOASPDEV(*CURASPGRP) DATA(*YES)                                          ', 0000000145.00000)";
          try {
            stmt.executeUpdate(sql);
            System.out.println(file + " copied to " + dataCollection);
          } catch (Exception e2) {
            e2.printStackTrace();
          }
        }
      } catch (Exception e0) {
        e0.printStackTrace();
      }

    }
    if (isLUW(connection)) {
      try {
        // If LUW, then we may need to create and populate the tables
        Statement stmt = connection.createStatement();
        dataCollection = "QIWS";
        String file = "QCUSTCDT";
        try {
          stmt.execute("SELECT * From " + dataCollection + "." + file);
          // already exists continue.
        } catch (Exception e) {
          // Make sure the schema exists
          try {
            stmt.executeUpdate("CREATE SCHEMA " + dataCollection);
          } catch (Exception e4) {
          }
          try {
            stmt.executeUpdate("DROP TABLE " + dataCollection + "." + file);
          } catch (Exception e4) {
          }

          stmt.executeUpdate("CREATE TABLE " + dataCollection + "." + file
              + " ( " + "CUSNUM NUMERIC(6,0)," + "LSTNAM CHAR(8),"
              + "INIT CHAR(3)," + "STREET CHAR(13)," + "CITY CHAR(6),"
              + "STATE CHAR (2)," + "ZIPCOD NUMERIC(5,0),"
              + "CDTLMT NUMERIC(4,0)," + "CHGCOD NUMERIC(1,0),"
              + "BALDUE NUMERIC(6,2)," + "CDTDUE NUMERIC(6,2)   )");

          PreparedStatement pstmt = connection.prepareStatement("INSERT INTO "
              + dataCollection + "." + file + " VALUES(?,?,?,?,?,?,?,?,?,?,?)");
          for (int i = 0; i < QCUSTCDTrows.length; i++) {
            String[] values = QCUSTCDTrows[i];
            for (int j = 0; j < values.length; j++) {
              pstmt.setString(j + 1, values[j]);
            }
            pstmt.executeUpdate();
          }

        }
      } catch (Exception e1) {
        e1.printStackTrace();
      }

    }

    return dataCollection;

  }

  /**
   * Resets the collection names
   */
  public static void resetCollection(String collection) {
    COLLECTION = collection;
    STP_RS0 = COLLECTION + ".JDRSNONE";
    STP_RS1 = COLLECTION + ".JDRSONE";
    STP_RS1UPD = COLLECTION + ".JDRSUPDT"; // for update
    STP_RS1WH = COLLECTION + ".JDRSOWH"; // with hold
    STP_RS1WOH = COLLECTION + ".JDRSOWOH"; // without hold
    STP_RS3CUR = COLLECTION + ".JDRS3CUR"; // 3 cursors with various attrs
    STP_RS1S = COLLECTION + ".JDRSONES";
    STP_RS3 = COLLECTION + ".JDRSTHREE";
    STP_CS0 = COLLECTION + ".JDCSNONE";
    STP_CS1 = COLLECTION + ".JDCSONE"; // @E1A
    STP_CSRV = COLLECTION + ".JDCSRV"; // @C2A
    STP_CSPARMS = COLLECTION + ".JDCSPARMS";
    STP_CSPARMSRS = COLLECTION + ".JDCSPARMSRS";
    STP_CSPARMSRV = COLLECTION + ".JDCSPARMSRV"; // @C2A
    STP_CSPARMSRSRV = COLLECTION + ".JDCSPARMSRSRV"; // @C2A
    STP_CSTYPESOUT = COLLECTION + ".JDCSTYPESOUT";
    STP_CSTYPESOUTX = COLLECTION + ".JDCSTYPESOUTX";
    STP_CSTYPESOUTB = COLLECTION + ".JDCSTYPESOUTB";
    STP_CSTYPESIN = COLLECTION + ".JDCSTYPESIN";
    STP_CSTYPESINOUT = COLLECTION + ".JDCSTYPESIO";
    STP_CSTYPESINOUTX = COLLECTION + ".JDCSTYPESIOX";
    STP_CSTYPESNULL = COLLECTION + ".JDCSTYPESNL";
    STP_CSINOUT = COLLECTION + ".JDCSIO"; // @D1A
    STP_CSNULLTEST = COLLECTION + ".JDCSNULLTEST";
    STP_CSSRS = COLLECTION + ".JDCSSRS"; // @KBA
    STP_CSMSRS = COLLECTION + ".JDCSMSRS"; // @KBA
    STP_CSARRSUM = COLLECTION + ".JDCSARRSUM";
    STP_CSARRINT = COLLECTION + ".JDCSARRINT";
    STP_CSARRINT2 = COLLECTION + ".JDCSARRINT2";
    STP_CSARRINT2R = COLLECTION + ".JDCSARRINT2R";
    STP_CSARRINT3 = COLLECTION + ".JDCSARRINT3";
    STP_CSARRINTN = COLLECTION + ".JDCSARRINTN";
    STP_CSARRINT4 = COLLECTION + ".JDCSARRINT4";
    STP_CSARRVCH3 = COLLECTION + ".JDCSARRVCH3";
    STP_CSARRSIN = COLLECTION + ".JDCSARRSIN"; // array of smallints
    STP_CSARRIN = COLLECTION + ".JDCSARRIN"; // array of ints
    STP_CSARRIN2 = COLLECTION + ".JDCSARRIN2"; // array of smallints and
                                               // non-array parm
    STP_CSARRBIN = COLLECTION + ".JDCSARRBIN"; // array of bigints
    STP_CSARRBOO = COLLECTION + ".JDCSARRBOO"; // array of booleans
    STP_CSARRREA = COLLECTION + ".JDCSARRREA"; // array of read
    STP_CSARRFLO = COLLECTION + ".JDCSARRFLO"; // array of float
    STP_CSARRDOU = COLLECTION + ".JDCSARRDOU"; // array of double
    STP_CSARRDEC = COLLECTION + ".JDCSARRDEC"; // array of decimal
    STP_CSARRNUM = COLLECTION + ".JDCSARRNUM"; // array of numeric
    STP_CSARRCH1 = COLLECTION + ".JDCSARRCH1"; // array of char(1)
    STP_CSARRCH50 = COLLECTION + ".JDCSARRCH50"; // array of char(50)
    STP_CSARRVCH = COLLECTION + ".JDCSARRVCH"; // array of varchar(50)
    STP_CSARRVCH2 = COLLECTION + ".JDCSARRVCH2"; // array of varchar(50) and
                                                 // non-array parms
    STP_CSARRGR = COLLECTION + ".JDCSARRGR"; // array of graphic
    STP_CSARRVGR = COLLECTION + ".JDCSARRVGR"; // array of vargraphic
    STP_CSARRCLO = COLLECTION + ".JDCSARRCLO"; // array of CLOB(100)
    STP_CSARRBLO = COLLECTION + ".JDCSARRBLO"; // array of bLOB(100)
    STP_CSARRDAT = COLLECTION + ".JDCSARRDAT"; // array of date
    STP_CSARRTIM = COLLECTION + ".JDCSARRTIM"; // array of tim
    STP_CSARRTS = COLLECTION + ".JDCSARRTS"; // array of timestame
    STP_CSARRBY = COLLECTION + ".JDCSARRBY"; // array of binary
    STP_CSARRVBY = COLLECTION + ".JDCSARRVBY"; // array of varbinary
    STP_CSARRXML = COLLECTION + ".JDCSARRXML"; // array of xml

    // If running to Z/OS -- limit names to 8 characters
    if (JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_ZOS) {
      STP_RS3 = COLLECTION + ".JDRSTHRE";
      STP_CSPARMS = COLLECTION + ".JDCSPMS";
      STP_CSPARMSRS = COLLECTION + ".JDCSPMSR";
      STP_CSPARMSRV = COLLECTION + ".JDCSPSRV"; // @C2A
      STP_CSPARMSRSRV = COLLECTION + ".JDCSPSRS"; // @C2A
      STP_CSTYPESOUT = COLLECTION + ".JDCSTOUT";
      STP_CSTYPESOUTX = COLLECTION + ".JDCSTOUX";
      STP_CSTYPESOUTB = COLLECTION + ".JDCSTOUB";
      STP_CSTYPESIN = COLLECTION + ".JDCSTIN";
      STP_CSTYPESINOUT = COLLECTION + ".JDCSTIO";
      STP_CSTYPESINOUTX = COLLECTION + ".JDCSTIOX";
      STP_CSTYPESNULL = COLLECTION + ".JDCSTNL";
      STP_CSNULLTEST = COLLECTION + ".JDCSNULT";

    }

    /* System.out.println("COLLECTION changed to " + COLLECTION); */

  }

  /**
   * @C1A Creates a stored procedure.
   * 
   * @param system
   *          The system.
   * @param password
   *          The password.
   * @param connection
   *          The connection.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *          What features are supported? 
   * @param collection
   *          Lib to create the stored procs in
   **/
  public static void create(AS400 system,  Connection connection,
      String stp, JDSupportedFeatures supportedFeatures, String collection)
      throws Exception {
    if (collection == null) collection = COLLECTION; 
    resetCollection(collection);
    String proc = stp.substring(stp.indexOf(".") + 1);
    String newstp = COLLECTION + "." + proc;
    create(system,  connection, newstp, supportedFeatures);
  }

  /**
   * Creates a stored procedure.
   * 
   * @param system
   *          The system.
   * @param connection
   *          The connection.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *           What features are supported
   **/
  public static void create(AS400 system, Connection connection, String stp, 
      JDSupportedFeatures supportedFeatures) throws Exception {
    boolean ZOS;
    boolean isLUW;
    ZOS = JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_ZOS;
    isLUW = JDTestDriver.getDatabaseTypeStatic() == JDTestDriver.DB_LUW;
    // Setup a special collection for all stored procedures.
    if (!collectionCreated_) {
      JDSetupCollection.create(system,  connection, COLLECTION);
      System.out.println(COLLECTION + " created.");
      collectionCreated_ = true;
    }

    String dataCollection = "QIWS";
    String sql = null;

    dataCollection = setupQIWS(system,  connection);

    // Stored procedure returns 0 result sets.
    if (stp.equals(STP_RS0)) {
      sql = "CREATE PROCEDURE " + stp + " LANGUAGE SQL READS SQL DATA SPECIFIC "
          + stp // @A2C
          + " JDRS0X: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
          + dataCollection + ".QCUSTCDT;" + "   OPEN C1;" + "   CLOSE C1;"
          + " END JDRS0X";
    }

    // Stored procedure returns 1 result set.
    else if (stp.equals(STP_RS1)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT WITH UR;" + "   OPEN C1;"
            + " END JDRS1X";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT WITH UR;" + "   OPEN C1;"
            + "   SET RESULT SETS CURSOR C1;" + " END JDRS1X";
      }
    }

    // Stored procedure returns 1 result set for update.
    else if (stp.equals(STP_RS1UPD)) {

      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp + " RESULT SET 1 LANGUAGE SQL SPECIFIC "
            + stp // @A2C
            + " JDRS1X: BEGIN" + "   DECLARE C1 CURSOR   FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT for update;" + "   OPEN C1;"
            + " END JDRS1X";

      } else {
        sql = "CREATE PROCEDURE " + stp + " RESULT SET 1 LANGUAGE SQL SPECIFIC "
            + stp // @A2C
            + " JDRS1X: BEGIN" + "   DECLARE C1 CURSOR   FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT for update;" + "   OPEN C1;"
            + "   SET RESULT SETS CURSOR C1;" + " END JDRS1X";
      }
    }

    // Stored procedure returns 1 result set without hold.
    else if (stp.equals(STP_RS1WOH)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN"
            + "   DECLARE C1 CURSOR WITHOUT HOLD FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;" + "   OPEN C1;" + " END JDRS1X";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN"
            + "   DECLARE C1 CURSOR WITHOUT HOLD FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;" + "   OPEN C1;"
            + "   SET RESULT SETS CURSOR C1;" + " END JDRS1X";
      }
    }

    // Stored procedure returns 1 result set with hold.
    else if (stp.equals(STP_RS1WH)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN"
            + "   DECLARE C1 CURSOR WITH HOLD FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;" + "   OPEN C1;" + " END JDRS1X";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRS1X: BEGIN"
            + "   DECLARE C1 CURSOR WITH HOLD FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;" + "   OPEN C1;"
            + "   SET RESULT SETS CURSOR C1;" + " END JDRS1X";
      }
    }

    // Stored procedure returns 1 result set with a scrollable cursor.
    else if (stp.equals(STP_RS1S)) {
      if (isLUW) {
        // LUW does not allow SCROLL CURSOR
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp
            + " JDRS1X: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;" + "   OPEN C1;" + " END JDRS1X";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp
            + " JDRS1X: BEGIN"
            + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT;" + "   OPEN C1;" + "   SET RESULT SETS CURSOR C1;"
            + " END JDRS1X";
      }
    }

    // Stored procedure returns 3 result sets.
    else if (stp.equals(STP_RS3)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 3 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRSXX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM SYSIBM.SYSROUTINES "
            + "      WHERE ROUTINESCHEMA='" + COLLECTION + "';"
            + "   DECLARE C3 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT;"
            // @C3D QSYS2.SYSPARMS WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            // Above will return empty result set if stored procedure does not
            // have any
            // parameters. Changed to test something that will always return a
            // result set,
            // else JDStatementTest.JDStatementResults var 13,14 fail after tc
            // cleanup run.
            + "   OPEN C1;" + "   OPEN C2;" + "   OPEN C3;" + " END JDRSXX";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 3 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRSXX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM QSYS2.SYSPROCS "
            + "      WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            + "   DECLARE C3 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT;"
            // @C3D QSYS2.SYSPARMS WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            // Above will return empty result set if stored procedure does not
            // have any
            // parameters. Changed to test something that will always return a
            // result set,
            // else JDStatementTest.JDStatementResults var 13,14 fail after tc
            // cleanup run.
            + "   OPEN C1;" + "   OPEN C2;" + "   OPEN C3;"
            + "   SET RESULT SETS CURSOR C1, CURSOR C2, CURSOR C3;"
            + " END JDRSXX";
      }
    }
    // 3 cursors with some diff cursor attrs
    else if (stp.equals(STP_RS3CUR)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 3 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRSXX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM QSYS2.SYSROUTINES "
            + "      WHERE ROUTINESCHEMA='" + COLLECTION + "';"
            + "   DECLARE C3 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT;" + "   OPEN C1;" + "   OPEN C2;" + "   OPEN C3;"
            + " END JDRSXX";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " RESULT SET 3 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
            + " JDRSXX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM QSYS2.SYSPROCS "
            + "      WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            + "   DECLARE C3 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT;" + "   OPEN C1;" + "   OPEN C2;" + "   OPEN C3;"
            + "   SET RESULT SETS CURSOR C1, CURSOR C2, CURSOR C3;"
            + " END JDRSXX";
      }
    }

    // Stored procedure takes no parameters.
    else if (stp.equals(STP_CS0)) {
      sql = "CREATE PROCEDURE " + stp + " LANGUAGE SQL READS SQL DATA SPECIFIC "
          + stp // @A2C
          + " JDCS0X: BEGIN" + "   DECLARE DUMMY INTEGER;" + "   SET DUMMY = 5;"
          + " END JDCS0X";
    }

    // @E1 Stored procedure that takes 1 parameter.
    else if (stp.equals(STP_CS1)) {
      sql = "CREATE PROCEDURE " + stp + " (IN P1 INTEGER)"
          + " LANGUAGE SQL SPECIFIC " + stp + " JDCS1X: BEGIN"
          + "   INSERT INTO " + COLLECTION + ".SINSERT VALUES(P1);"
          + " END JDCS1X";
    }

    // @C2A
    // Stored procedure that returns a return value.
    else if (stp.equals(STP_CSRV)) {
      sql = "CREATE PROCEDURE " + stp + " LANGUAGE SQL SPECIFIC " + stp
          + " JDCSRVX: BEGIN" + "   RETURN 1976;" + " END JDCSRVX";
    }

    // @KBA
    // Stored procedure that returns a scrollable ResultSet
    else if (stp.equals(STP_CSSRS)) // @KBA
    { // @KBA
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp // @KBA
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @KBA
            + " BEGIN " // @KBA
            + "   DECLARE C1 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT ORDER BY LSTNAM;" // @KBA
            + "   OPEN C1;" // @KBA
            + " END  "; // @KBA

      } else {
        sql = "CREATE PROCEDURE " + stp // @KBA
            + " RESULT SET 1 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @KBA
            + " BEGIN " // @KBA
            + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT ORDER BY LSTNAM;" // @KBA
            + "   OPEN C1;" // @KBA
            + "   SET RESULT SETS CURSOR C1;" // @KBA
            + " END  "; // @KBA
      }
    } // @KBA

    // @KBA
    // Stored procedure that returns two scrollable Result Sets
    else if (stp.equals(STP_CSMSRS)) // @KBA
    { // @KBA
      if (isLUW) {
        // LUW doesn't permit the specification of scrollable
        sql = "CREATE PROCEDURE " + stp // @KBA
            + " RESULT SETS 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @KBA
            + " BEGIN " // @KBA
            + "   DECLARE C1 CURSOR FOR SELECT * FROM SYSIBM.SYSROUTINES;" // @KBA
            + "   DECLARE C2 CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT ORDER BY LSTNAM;" // @KBA
            + "   OPEN C1;" // @KBA
            + "   OPEN C2;" // @KBA
            + " END  "; // @KBA

      } else {
        sql = "CREATE PROCEDURE " + stp // @KBA
            + " RESULT SETS 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @KBA
            + " BEGIN " // @KBA
            + "   DECLARE C1 SCROLL CURSOR FOR SELECT * FROM QSYS2.SYSPROCS;" // @KBA
            + "   DECLARE C2 SCROLL CURSOR FOR SELECT * FROM " + dataCollection
            + ".QCUSTCDT ORDER BY LSTNAM;" // @KBA
            + "   OPEN C1;" // @KBA
            + "   OPEN C2;" // @KBA
            + "   SET RESULT SETS CURSOR C1, CURSOR C2;" // @KBA
            + " END  "; // @KBA
      }
    } // @KBA

    // Stored procedure has an IN, OUT, and INOUT parameter.
    else if (stp.equals(STP_CSPARMS)) {
      if (ZOS) {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " LANGUAGE SQL BEGIN" + "   SET P2 = P1 + 1;"
            + "   SET P3 = P3 + 1;" + " END ";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " LANGUAGE SQL SPECIFIC " + stp // @A2C
            + " JDCSPARMSX: BEGIN" + "   SET P2 = P1 + 1;"
            + "   SET P3 = P3 + 1;" + " END JDCSPARMSX";
      }
    }

    // Stored procedure has IN, OUT, and INOUT parameters
    // and returns 2 result sets.
    else if (stp.equals(STP_CSPARMSRS)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " RESULT SET 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
                                                                          // @B4C
            + " JDCSPRSX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM SYSIBM.SYSROUTINES "
            + "      WHERE ROUTINESCHEMA='" + COLLECTION + "';" + "   OPEN C1;"
            + "   OPEN C2;" + "   SET P2 = P1 + 1;" + "   SET P3 = P3 + 1;"
            + " END JDCSPRSX";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " RESULT SET 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp // @A2C
                                                                          // @B4C
            + " JDCSPRSX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM QSYS2.SYSPROCS "
            + "      WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            + "   OPEN C1;" + "   OPEN C2;"
            + "   SET RESULT SETS CURSOR C1, CURSOR C2;" + "   SET P2 = P1 + 1;"
            + "   SET P3 = P3 + 1;" + " END JDCSPRSX";
      }
    }

    // @C2A
    // Stored procedure has an IN, OUT, and INOUT parameter
    // and returns a return value.
    else if (stp.equals(STP_CSPARMSRV)) {
      sql = "CREATE PROCEDURE " + stp
          + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
          + " LANGUAGE SQL SPECIFIC " + stp + " JDCSPRVX: BEGIN"
          + "   SET P2 = P1 + 1;" + "   SET P3 = P3 + 1;" + "   RETURN P1;"
          + " END JDCSPRVX";
    }

    // @C2A
    // Stored procedure has an IN, OUT, and INOUT parameter
    // and returns a return value and a result set.
    else if (stp.equals(STP_CSPARMSRSRV)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " RESULT SET 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp
            + " JDCSPRVRSX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM SYSIBM.SYSROUTINES "
            + "      WHERE ROUTINESCHEMA='" + COLLECTION + "';" + "   OPEN C1;"
            + "   OPEN C2;" + "   SET P2 = P1 + 1;" + "   SET P3 = P3 + 1;"
            + "   RETURN P1;" + " END JDCSPRVRSX";

      } else {
        sql = "CREATE PROCEDURE " + stp
            + " (IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER)"
            + " RESULT SET 2 LANGUAGE SQL READS SQL DATA SPECIFIC " + stp
            + " JDCSPRVRSX: BEGIN" + "   DECLARE C1 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT;"
            + "   DECLARE C2 CURSOR FOR SELECT * FROM QSYS2.SYSPROCS "
            + "      WHERE SPECIFIC_SCHEMA='" + COLLECTION + "';"
            + "   OPEN C1;" + "   OPEN C2;"
            + "   SET RESULT SETS CURSOR C1, CURSOR C2;" + "   SET P2 = P1 + 1;"
            + "   SET P3 = P3 + 1;" + "   RETURN P1;" + " END JDCSPRVRSX";
      }
    }

    // Stored procedure has IN, OUT, and INOUT parameters
    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSTYPESOUT)) {
      StringBuffer buffer = new StringBuffer();
      buffer
          .append("CREATE PROCEDURE " + stp + " (OUT P_SMALLINT       SMALLINT," // 1
              + "  OUT P_INTEGER        INTEGER," // 2
              + "  OUT P_REAL           REAL," // 3
              + "  OUT P_FLOAT          FLOAT," // 4
              + "  OUT P_DOUBLE         DOUBLE," // 5
              + "  OUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  OUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  OUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  OUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  OUT P_CHAR_1         CHAR," // 10
              + "  OUT P_CHAR_50        CHAR(50)," // 11
              + "  OUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  OUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  OUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  OUT P_DATE           DATE," // 15
              + "  OUT P_TIME           TIME," // 16
              + "  OUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  OUT P_DATALINK        VARCHAR(200), "// 18
            + "  OUT P_BLOB           BLOB(200)," // 19
            + "  OUT P_CLOB           CLOB(200)," // 20
            + "  OUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  OUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  OUT P_DECFLOAT16     DECFLOAT(16)"); // 23
        buffer.append(",  OUT P_DECFLOAT34     DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  OUT P_BOOLEAN      BOOLEAN");        // 25
      }

      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " SET OPTION TIMFMT=*JIS, DATFMT=*ISO " + " JDCSTYPESX: BEGIN"
          + "   SET P_SMALLINT        = 123;"
          + "   SET P_INTEGER         = -456;"
          + "   SET P_REAL            = 789.54;"
          + "   SET P_FLOAT           = 253.1027;"
          + "   SET P_DOUBLE          = -987.3434;"
          + "   SET P_DECIMAL_50      = 54362;"
          + "   SET P_DECIMAL_105     = -94732.12345;"
          + "   SET P_NUMERIC_50      = -1112;"
          + "   SET P_NUMERIC_105     = 19.98765;"
          + "   SET P_CHAR_1          = 'C';"
          + "   SET P_CHAR_50         = 'Jim';"
          + "   SET P_VARCHAR_50      = 'Charlie';"
          + "   SET P_BINARY_20       = X'4D75726368202020202020202020202020202020';"
          + "   SET P_VARBINARY_20    =  X'446176652057616C6C';"
          + "   SET P_DATE            =  DATE('1998-04-15');"
          + "   SET P_TIME            =  TIME('08:42:30');"
          + "   SET P_TIMESTAMP       =  TIMESTAMP('2001-11-18 13:42:22.123456');");
      if (supportedFeatures.lobSupport)
        buffer.append(
            "   SET P_DATALINK        = 'https://github.com/IBM/JTOpen-test/blob/main/README.md';"
                + "   SET P_BLOB            = BLOB(X'446176652045676C65');" // @B1C
                + "   SET P_CLOB            = CLOB('Chris Smith');" // @B1C
                + "   SET P_DBCLOB          = CLOB('Jeff Lee');"); // @B1C
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("   SET P_BIGINT = 987662234567;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("   SET P_DECFLOAT16 = 1234567890123456;");
        buffer.append(
            "   SET P_DECFLOAT34 = 1234567890123456789012345678901234;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("   SET P_BOOLEAN = true;");
      }
      buffer.append(" END JDCSTYPESX");
      sql = buffer.toString();
      System.out.println("JDSetupProcdure:  SQL=" + sql);
    }

    else if (stp.equals(STP_CSTYPESOUTX)) {
      StringBuffer buffer = new StringBuffer();
      buffer
          .append("CREATE PROCEDURE " + stp + " (OUT P_SMALLINT       SMALLINT," // 1
              + "  OUT P_INTEGER        INTEGER," // 2
              + "  OUT P_REAL           REAL," // 3
              + "  OUT P_FLOAT          FLOAT," // 4
              + "  OUT P_DOUBLE         DOUBLE," // 5
              + "  OUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  OUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  OUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  OUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  OUT P_CHAR_1         CHAR," // 10
              + "  OUT P_CHAR_50        CHAR(50)," // 11
              + "  OUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  OUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  OUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  OUT P_DATE           DATE," // 15
              + "  OUT P_TIME           TIME," // 16
              + "  OUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  OUT P_DATALINK        VARCHAR(200), "// 18
            + "  OUT P_BLOB           BLOB(200)," // 19
            + "  OUT P_CLOB           CLOB(200)," // 20
            + "  OUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  OUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  OUT P_DECFLOAT16     DECFLOAT(16)"); // 23
        buffer.append(",  OUT P_DECFLOAT34     DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  OUT P_BOOLEAN    BOOLEAN"); // 25

      }

      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " SET OPTION TIMFMT=*JIS, DATFMT=*ISO " + " JDCSTYPESX: BEGIN"
          + "   SET P_SMALLINT        = 123;"
          + "   SET P_INTEGER         = -456;"
          + "   SET P_REAL            = 789.54;"
          + "   SET P_FLOAT           = 253.1027;"
          + "   SET P_DOUBLE          = -987.3434;"
          + "   SET P_DECIMAL_50      = 54362;"
          + "   SET P_DECIMAL_105     = -94732.12345;"
          + "   SET P_NUMERIC_50      = -1112;"
          + "   SET P_NUMERIC_105     = 19.98765;"
          + "   SET P_CHAR_1          = 'C';"
          + "   SET P_CHAR_50         = '<d>Jim</d>';"
          + "   SET P_VARCHAR_50      = '<d>Charlie</d>';"
          + "   SET P_BINARY_20       = '<d>Murch</d>';"
          + "   SET P_VARBINARY_20    = '<d>Dave Wall</d>';"
          + "   SET P_DATE            =  DATE('1998-04-15');"
          + "   SET P_TIME            =  TIME('08:42:30');"
          + "   SET P_TIMESTAMP       =  TIMESTAMP('2001-11-18 13:42:22.123456');");
      if (supportedFeatures.lobSupport)
        buffer.append(
            "   SET P_DATALINK        = 'https://github.com/IBM/JTOpen-test/blob/main/README.md';"
                + "   SET P_BLOB            = BLOB('<d>Dave Egle</d>');" // @B1C
                + "   SET P_CLOB            = CLOB('<d>Chris Smith</d>');" // @B1C
                + "   SET P_DBCLOB          = CLOB('<d>Jeff Lee</d>');"); // @B1C
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("   SET P_BIGINT = 987662234567;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("   SET P_DECFLOAT16 = 1234567890123456;");
        buffer.append(
            "   SET P_DECFLOAT34 = 1234567890123456789012345678901234;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("   SET P_BOOLEAN = true;");
      }
      buffer.append(" END JDCSTYPESX");
      sql = buffer.toString();
      System.out.println("JDSetupProcdure:  SQL=" + sql);
    }

    // Stored procedure has IN, OUT, and INOUT parameters
    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSTYPESOUTB)) {
      StringBuffer buffer = new StringBuffer();
      buffer
          .append("CREATE PROCEDURE " + stp + " (OUT P_SMALLINT       SMALLINT," // 1
              + "  OUT P_INTEGER        INTEGER," // 2
              + "  OUT P_REAL           REAL," // 3
              + "  OUT P_FLOAT          FLOAT," // 4
              + "  OUT P_DOUBLE         DOUBLE," // 5
              + "  OUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  OUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  OUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  OUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  OUT P_CHAR_1         CHAR," // 10
              + "  OUT P_CHAR_50        CHAR(50)," // 11
              + "  OUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  OUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  OUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  OUT P_DATE           DATE," // 15
              + "  OUT P_TIME           TIME," // 16
              + "  OUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  OUT P_DATALINK        VARCHAR(200), "// 18
            + "  OUT P_BLOB           BLOB(200)," // 19
            + "  OUT P_CLOB           CLOB(200)," // 20
            + "  OUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  OUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  OUT P_DECFLOAT16     DECFLOAT(16)"); // 23
        buffer.append(",  OUT P_DECFLOAT34     DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  OUT P_BOOLEAN        BOOLEAN"); // 25
      }

      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + "  SET OPTION TIMFMT=*JIS, DATFMT=*ISO "
          + " JDCSTYPESY: BEGIN" + "   SET P_SMALLINT        = 1;"
          + "   SET P_INTEGER         = 2;" + "   SET P_REAL            = 3.3;"
          + "   SET P_FLOAT           = 4.4;"
          + "   SET P_DOUBLE          = 5.5;" + "   SET P_DECIMAL_50      = 6;"
          + "   SET P_DECIMAL_105     = 7;" + "   SET P_NUMERIC_50      = 8;"
          + "   SET P_NUMERIC_105     = 9;" + "   SET P_CHAR_1          = '0';"
          + "   SET P_CHAR_50         = '11';"
          + "   SET P_VARCHAR_50      = '12';"
          + "   SET P_BINARY_20       = '13';"
          + "   SET P_VARBINARY_20    = '14';"
          + "   SET P_DATE            = DATE('1998-04-15');"
          + "   SET P_TIME            = TIME('08:42:30');"
          + "   SET P_TIMESTAMP       = TIMESTAMP('2001-11-18 13:42:22.123456');");
      if (supportedFeatures.lobSupport)
        buffer.append(
            "   SET P_DATALINK        = 'https://github.com/IBM/JTOpen-test/blob/main/README.md';"
                + "   SET P_BLOB            = BLOB('18');" // @B1C
                + "   SET P_CLOB            = CLOB('19');" // @B1C
                + "   SET P_DBCLOB          = CLOB('20');"); // @B1C
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("   SET P_BIGINT = 21;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("   SET P_DECFLOAT16 = 22;");
        buffer.append("   SET P_DECFLOAT34 = 23;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("   SET P_BOOLEAN = false;");
      }
      buffer.append(" END JDCSTYPESY");
      sql = buffer.toString();
    }

    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSTYPESIN)) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("CREATE PROCEDURE " + stp
          + " (IN P_SMALLINT       SMALLINT," + "  IN P_INTEGER        INTEGER,"
          + "  IN P_REAL           REAL," + "  IN P_FLOAT          FLOAT,"
          + "  IN P_DOUBLE         DOUBLE,"
          + "  IN P_DECIMAL_50     DECIMAL(5,0),"
          + "  IN P_DECIMAL_105    DECIMAL(10,5),"
          + "  IN P_NUMERIC_50     NUMERIC(5,0),"
          + "  IN P_NUMERIC_105    NUMERIC(10,5),"
          + "  IN P_CHAR_1         CHAR," + "  IN P_CHAR_50        CHAR(50),"
          + "  IN P_VARCHAR_50     VARCHAR(50),"
          + "  IN P_BINARY_20      CHAR(20) FOR BIT DATA,"
          + "  IN P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"
          + "  IN P_DATE           DATE," + "  IN P_TIME           TIME,"
          + "  IN P_TIMESTAMP      TIMESTAMP");
      if (supportedFeatures.lobSupport) {
        if (isLUW) {
          buffer.append(",  IN P_DATALINK VARCHAR(200),");
        } else {
          // Datalink parameters not working for either driver
          // Ignore for now.
          // 8/6/2014
          // buffer.append (", IN P_DATALINK DATALINK,");
          buffer.append(",  IN P_DATALINK VARCHAR(200),");
        }
        buffer.append("  IN P_BLOB           BLOB(200),"
            + "  IN P_CLOB           CLOB(200),"
            + "  IN P_DBCLOB         CLOB(200)");
      }
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  IN P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  IN P_DECFLOAT16    DECFLOAT(16)");
        buffer.append(",  IN P_DECFLOAT34    DECFLOAT(34)");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  IN P_BOOLEAN       BOOLEAN");
      }
      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " JDCSTYPESINX: BEGIN" + "   DECLARE DUMMY INTEGER;"
          + "   SET DUMMY = 5;" + " END JDCSTYPESINX");
      sql = buffer.toString();
    }

    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSTYPESINOUT)) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(
          "CREATE PROCEDURE " + stp + " (INOUT P_SMALLINT       SMALLINT," // 1
              + "  INOUT P_INTEGER        INTEGER," // 2
              + "  INOUT P_REAL           REAL," // 3
              + "  INOUT P_FLOAT          FLOAT," // 4
              + "  INOUT P_DOUBLE         DOUBLE," // 5
              + "  INOUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  INOUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  INOUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  INOUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  INOUT P_CHAR_1         CHAR," // 10
              + "  INOUT P_CHAR_50        CHAR(50)," // 11
              + "  INOUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  INOUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  INOUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  INOUT P_DATE           DATE," // 15
              + "  INOUT P_TIME           TIME," // 16
              + "  INOUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  INOUT P_DATALINK  VARCHAR(200)," // 18
            + "  INOUT P_BLOB           BLOB(200)," // 19
            + "  INOUT P_CLOB           CLOB(200)," // 20
            + "  INOUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  INOUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  INOUT P_DECFLOAT16     DECFLOAT(16)"); // 23
        buffer.append(",  INOUT P_DECFLOAT34     DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  INOUT P_BOOLEAN        BOOLEAN"); // 25
      }
      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " SET OPTION TIMFMT=*JIS, DATFMT=*ISO " + " JDCSTYPESA: BEGIN"
          + "   SET P_SMALLINT        = P_SMALLINT + 23;"
          + "   SET P_INTEGER         = P_INTEGER * -2;"
          + "   SET P_REAL            = -P_REAL;"
          + "   SET P_FLOAT           = P_FLOAT + 543.2;"
          + "   SET P_DOUBLE          = P_DOUBLE - 54.54;"
          + "   SET P_DECIMAL_50      = P_DECIMAL_50 - 3;"
          + "   SET P_DECIMAL_105     = P_DECIMAL_105 + 30000.00003;"
          + "   SET P_NUMERIC_50      = P_NUMERIC_50 + 1;"
          + "   SET P_NUMERIC_105     = P_NUMERIC_105 - 1;"
          + "   SET P_CHAR_1          = 'C';"
          + "   SET P_CHAR_50         = 'Jim';"
          + "   SET P_VARCHAR_50      = P_VARCHAR_50 || 'JDBC';"
          + "   SET P_BINARY_20       = X'4D75726368202020202020202020202020202020';"
          + "   SET P_VARBINARY_20    =  X'446176652057616C6C' || P_VARBINARY_20;"
          + "   SET P_DATE            = DATE '1998-04-15';"
          + "   SET P_TIME            = '08:42:30';"
          + "   SET P_TIMESTAMP       = '2001-11-18-13.42.22.123456';");
      if (supportedFeatures.lobSupport)
        buffer.append(
            "   SET P_DATALINK           = 'http://www.sony.com/pix.html';"
                + "   SET P_BLOB            = BLOB('Hello there');" // @B1C
                + "   SET P_CLOB            = CLOB('Welcome');" // @B1C
                + "   SET P_DBCLOB          = CLOB('Goodbye');"); // @B1C
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("   SET P_BIGINT = 987662234567;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("   SET P_DECFLOAT16 = P_DECFLOAT16 + 1;");
        buffer.append("   SET P_DECFLOAT34 = P_DECFLOAT34 + 1;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("IF ( P_BOOLEAN = true ) THEN  SET P_BOOLEAN = false ; ELSE SET P_BOOLEAN=true ; END IF;"); 
      }
      buffer.append(" END JDCSTYPESA");
      sql = buffer.toString();
    }

    else if (stp.equals(STP_CSTYPESINOUTX)) {
      StringBuffer buffer = new StringBuffer();
      buffer.append(
          "CREATE PROCEDURE " + stp + " (INOUT P_SMALLINT       SMALLINT," // 1
              + "  INOUT P_INTEGER        INTEGER," // 2
              + "  INOUT P_REAL           REAL," // 3
              + "  INOUT P_FLOAT          FLOAT," // 4
              + "  INOUT P_DOUBLE         DOUBLE," // 5
              + "  INOUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  INOUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  INOUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  INOUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  INOUT P_CHAR_1         CHAR," // 10
              + "  INOUT P_CHAR_50        CHAR(50)," // 11
              + "  INOUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  INOUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  INOUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  INOUT P_DATE           DATE," // 15
              + "  INOUT P_TIME           TIME," // 16
              + "  INOUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  INOUT P_DATALINK  VARCHAR(200)," // 18
            + "  INOUT P_BLOB           BLOB(200)," // 19
            + "  INOUT P_CLOB           CLOB(200)," // 20
            + "  INOUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  INOUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  INOUT P_DECFLOAT16     DECFLOAT(16)"); // 23
        buffer.append(",  INOUT P_DECFLOAT34     DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  INOUT P_BOOLEAN        BOOLEAN"); // 25
      }
      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " SET OPTION TIMFMT=*JIS, DATFMT=*ISO " + " JDCSTYPESA: BEGIN"
          + "   SET P_SMALLINT        = P_SMALLINT + 23;"
          + "   SET P_INTEGER         = P_INTEGER * -2;"
          + "   SET P_REAL            = -P_REAL;"
          + "   SET P_FLOAT           = P_FLOAT + 543.2;"
          + "   SET P_DOUBLE          = P_DOUBLE - 54.54;"
          + "   SET P_DECIMAL_50      = P_DECIMAL_50 - 3;"
          + "   SET P_DECIMAL_105     = P_DECIMAL_105 + 30000.00003;"
          + "   SET P_NUMERIC_50      = P_NUMERIC_50 + 1;"
          + "   SET P_NUMERIC_105     = P_NUMERIC_105 - 1;"
          + "   SET P_CHAR_1          = 'C';"
          + "   SET P_CHAR_50         = 'Jim';"
          + "   SET P_VARCHAR_50      = P_VARCHAR_50 || 'JDBC';"
          + "   SET P_BINARY_20       = X'4D75726368202020202020202020202020202020';"
          + "   SET P_VARBINARY_20    =  X'446176652057616C6C' || P_VARBINARY_20;"
          + "   SET P_DATE            = DATE '1998-04-15';"
          + "   SET P_TIME            = '08:42:30';"
          + "   SET P_TIMESTAMP       = '2001-11-18-13.42.22.123456';");
      if (supportedFeatures.lobSupport)
        buffer.append(
            "   SET P_DATALINK           = 'http://www.sony.com/pix.html';"
                + "   SET P_BLOB            = BLOB('<d>Hello there</d>');" // @B1C
                + "   SET P_CLOB            = CLOB('"
                + STP_CSTYPESINOUTX_CLOB_OUTPUT + "');" // @B1C
                + "   SET P_DBCLOB          = CLOB('"
                + STP_CSTYPESINOUTX_DBCLOB_OUTPUT + "');"); // @B1C
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("   SET P_BIGINT = 987662234567;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("   SET P_DECFLOAT16 = P_DECFLOAT16 + 1;");
        buffer.append("   SET P_DECFLOAT34 = P_DECFLOAT34 + 1;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("IF ( P_BOOLEAN = true ) THEN  SET P_BOOLEAN = false ; ELSE SET P_BOOLEAN=true ; END IF;"); 
      }
      buffer.append(" END JDCSTYPESA");
      sql = buffer.toString();
    }

    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSTYPESNULL)) {
      StringBuffer buffer = new StringBuffer();
      buffer
          .append("CREATE PROCEDURE " + stp + " (OUT P_SMALLINT       SMALLINT," // 1
              + "  OUT P_INTEGER        INTEGER," // 2
              + "  OUT P_REAL           REAL," // 3
              + "  OUT P_FLOAT          FLOAT," // 4
              + "  OUT P_DOUBLE         DOUBLE," // 5
              + "  OUT P_DECIMAL_50     DECIMAL(5,0)," // 6
              + "  OUT P_DECIMAL_105    DECIMAL(10,5)," // 7
              + "  OUT P_NUMERIC_50     NUMERIC(5,0)," // 8
              + "  OUT P_NUMERIC_105    NUMERIC(10,5)," // 9
              + "  OUT P_CHAR_1         CHAR," // 10
              + "  OUT P_CHAR_50        CHAR(50)," // 11
              + "  OUT P_VARCHAR_50     VARCHAR(50)," // 12
              + "  OUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13
              + "  OUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14
              + "  OUT P_DATE           DATE," // 15
              + "  OUT P_TIME           TIME," // 16
              + "  OUT P_TIMESTAMP      TIMESTAMP"); // 17
      if (supportedFeatures.lobSupport)
        // Note: it turns out to be illegal to have an OUT datalink
        // parameter, so we don't need to test that. I will
        // leave it here as a varchar, so we don't throw the
        // parameter indices off.
        buffer.append(",  OUT P_DATALINK      VARCHAR(200)," // 18
            + "  OUT P_BLOB           BLOB(200)," // 19
            + "  OUT P_CLOB           CLOB(200)," // 20
            + "  OUT P_DBCLOB         CLOB(200)"); // 21
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append(",  OUT P_BIGINT         BIGINT"); // 22 // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  OUT P_DECFLOAT16      DECFLOAT(16)"); // 23
        buffer.append(",  OUT P_DECFLOAT34      DECFLOAT(34)"); // 24
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  OUT P_BOOLEAN         BOOLEAN"); // 25
      }

      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @A2C
          + " JDCSTYPESX: BEGIN" + "   SET P_SMALLINT        = NULL;"
          + "   SET P_INTEGER         = NULL;"
          + "   SET P_REAL            = NULL;"
          + "   SET P_FLOAT           = NULL;"
          + "   SET P_DOUBLE          = NULL;"
          + "   SET P_DECIMAL_50      = NULL;"
          + "   SET P_DECIMAL_105     = NULL;"
          + "   SET P_NUMERIC_50      = NULL;"
          + "   SET P_NUMERIC_105     = NULL;"
          + "   SET P_CHAR_1          = NULL;"
          + "   SET P_CHAR_50         = NULL;"
          + "   SET P_VARCHAR_50      = NULL;"
          + "   SET P_BINARY_20       = NULL;"
          + "   SET P_VARBINARY_20    = NULL;"
          + "   SET P_DATE            = NULL;"
          + "   SET P_TIME            = NULL;"
          + "   SET P_TIMESTAMP       = NULL;");
      if (supportedFeatures.lobSupport)
        buffer.append("   SET P_DATALINK           = NULL;"
            + "   SET P_BLOB            = NULL;"
            + "   SET P_CLOB            = NULL;"
            + "   SET P_DBCLOB          = NULL;");
      if (supportedFeatures.bigintSupport) // @B0A
        buffer.append("  SET P_BIGINT = NULL;"); // @B0A
      if (supportedFeatures.decfloatSupport) {
        buffer.append("  SET P_DECFLOAT16 = NULL;");
        buffer.append("  SET P_DECFLOAT34 = NULL;");
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append("  SET P_BOOLEAN = NULL;");
      }
      buffer.append(" END JDCSTYPESX");
      sql = buffer.toString();
    }

    // Stored procedure has parameters for each type.
    else if (stp.equals(STP_CSINOUT)) // @D1A
    { // @D1A
      StringBuffer buffer = new StringBuffer(); // @D1A
      buffer.append("CREATE PROCEDURE " + stp // @D1A
          + " (INOUT P_SMALLINT       SMALLINT," // 1 //@D1A
          + "  INOUT P_INTEGER        INTEGER," // 2 //@D1A
          + "  INOUT P_REAL           REAL," // 3 //@D1A
          + "  INOUT P_FLOAT          FLOAT," // 4 //@D1A
          + "  INOUT P_DOUBLE         DOUBLE," // 5 //@D1A
          + "  INOUT P_DECIMAL_50     DECIMAL(5,0)," // 6 //@D1A
          + "  INOUT P_DECIMAL_105    DECIMAL(10,5)," // 7 //@D1A
          + "  INOUT P_NUMERIC_50     NUMERIC(5,0)," // 8 //@D1A
          + "  INOUT P_NUMERIC_105    NUMERIC(10,5)," // 9 //@D1A
          + "  INOUT P_CHAR_1         CHAR," // 10 //@D1A
          + "  INOUT P_CHAR_50        CHAR(50)," // 11 //@D1A
          + "  INOUT P_VARCHAR_50     VARCHAR(50)," // 12 //@D1A
          + "  INOUT P_BINARY_20      CHAR(20) FOR BIT DATA," // 13 //@D1A
          + "  INOUT P_VARBINARY_20   VARCHAR(20) FOR BIT DATA,"// 14 //@D1A
          + "  INOUT P_DATE           DATE," // 15 //@D1A
          + "  INOUT P_TIME           TIME," // 16 //@D1A
          + "  INOUT P_TIMESTAMP      TIMESTAMP"); // 17 //@D1A
                                                   // @D1A
      if (supportedFeatures.lobSupport) // @D1A
      { // @D1A
        buffer.append(",  INOUT P_DATALINK  VARCHAR(200)," // 18 //@D1A
            + "  INOUT P_BLOB           BLOB(200)," // 19 //@D1A
            + "  INOUT P_CLOB           CLOB(200)," // 20 //@D1A
            + "  INOUT P_DBCLOB         CLOB(200)"); // 21 //@D1A
      } // @D1A
      if (supportedFeatures.bigintSupport) // @D1A
        buffer.append(",  INOUT P_BIGINT         BIGINT"); // 22 //@D1A

      buffer.append(",   INOUT \"p_lower\"       VARCHAR(50)," // 23 //@D1A
          + "  INOUT \"P_mIxEd\"       VARCHAR(50)"); // 24 //@D1A
      if (supportedFeatures.decfloatSupport) {
        buffer.append(",  INOUT P_DECFLOAT16      DECFLOAT(16)"); // 25
        buffer.append(",  INOUT P_DECFLOAT34      DECFLOAT(34)"); // 26
      }
      if (supportedFeatures.booleanSupport) {
        buffer.append(",  INOUT P_BOOLEAN     BOOLEAN"); // 27
      }
      buffer.append(") LANGUAGE SQL SPECIFIC " + stp // @D1A
          + " JDCSTYPES: BEGIN" // @D1A
          + "   DECLARE DUMMY INTEGER;" // @D1A
          + "   SET DUMMY = 5;"); // @D1A
      buffer.append(" END JDCSTYPES"); // @D1A
      sql = buffer.toString(); // @D1A
                               // @D1A
    }

    // Stored procedure has an IN, OUT, and INOUT parameter.
    else if (stp.equals(STP_CSNULLTEST)) {
      if (isLUW) {
        sql = "CREATE PROCEDURE " + stp + " ( IN P1 INTEGER, "
            + "   IN P2 INTEGER, " + "   IN P3 INTEGER, " + "   IN P4 INTEGER, "
            + "   IN P5 VARCHAR(20)) " + "RESULT SET 1 LANGUAGE SQL "
            + "BEGIN  " + "   DECLARE C3 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT ; " + "   OPEN C3 ; " + "END  ";

      } else {
        sql = "CREATE PROCEDURE " + stp + " ( IN P1 INTEGER, "
            + "   IN P2 INTEGER, " + "   IN P3 INTEGER, " + "   IN P4 INTEGER, "
            + "   IN P5 VARCHAR(20)) " + "RESULT SET 1 LANGUAGE SQL "
            + "BEGIN  " + "   DECLARE C3 CURSOR FOR SELECT * FROM "
            + dataCollection + ".QCUSTCDT ; " + "   OPEN C3 ; "
            + "   SET RESULT SETS CURSOR C3 ; " + "END  ";
      }
    }

    // Stored procedure of Array of ints
    else if (stp.equals(STP_CSARRSUM)) {
      // helper SP
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure " + stp + " (in numList " + COLLECTION
          + ".JDINTARRAY, out total integer) " + " begin  "
          + " declare i, n integer;   " +

          " set n = CARDINALITY(numList); " +

          " set i = 1;  " + " set total = 0;  " +

          " while (i <= n) do  " + " set total = total + numList[i];  "
          + " set i = i + 1;  " + " end while; " + " end   ";
    }
    // Stored procedure of Array of ints 1 int input and arrout outp8ut
    else if (stp.equals(STP_CSARRINT4)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure  " + stp + "  (in total integer, out numList "
          + COLLECTION + ".JDINTARRAY)  " + " begin  "
          + " set numList = ARRAY[1,2,3,4,5,6];   " + " end ";
    }
    // Stored procedure of Array of ints
    else if (stp.equals(STP_CSARRINT)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure  " + stp + "  (out total integer, out numList "
          + COLLECTION + ".JDINTARRAY)  " + " begin  "
          + " set numList = ARRAY[1,22,23,4,5,6];   " + " call " + COLLECTION
          + ".JDCSARRSUM(numList, total) ; " + " end ";
    }
    // Stored procedure of Array of ints 2
    else if (stp.equals(STP_CSARRINT2)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure   " + stp + "  (in numListIn " + COLLECTION
          + ".JDINTARRAY, out numListOut  " + COLLECTION + ".JDINTARRAY)  "
          + " begin  " + "DECLARE var1 INTEGER; "
          + "set numListOut[1] = numListIn[4]; "
          + "set numListOut[2] = numListIn[3]; "
          + "set numListOut[3] = numListIn[2]; "
          + "set numListOut[4] = numListIn[1]; " + " end ";
    }

    // Stored procedure of Array of ints 2
    else if (stp.equals(STP_CSARRINT2R)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure   " + stp + "  (out numListOut  " + COLLECTION
          + ".JDINTARRAY, in numListIn " + COLLECTION + ".JDINTARRAY )  "
          + " begin  " + "DECLARE var1 INTEGER; "
          + "set numListOut[1] = numListIn[4]; "
          + "set numListOut[2] = numListIn[3]; "
          + "set numListOut[3] = numListIn[2]; "
          + "set numListOut[4] = numListIn[1]; " + " end ";
    }

    // Stored procedure of Array of ints 3 (just 1 input array for ZDA)
    else if (stp.equals(STP_CSARRINT3)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure " + stp + "  (in numListIn " + COLLECTION
          + ".JDINTARRAY )  " + " begin  " + "DECLARE var1 INTEGER; " + " end ";
    }
    // Stored procedure of Array of ints 3 (1 in and 1 out for testing null
    // parms)
    else if (stp.equals(STP_CSARRINTN)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure " + stp + "  (in numListIn " + COLLECTION
          + ".JDINTARRAY , out numListOut " + COLLECTION + ".JDINTARRAY)  "
          + " begin  " + "DECLARE var1 INTEGER; "
          + " set numListOut = numListIn; " + " end ";
    }
    // Stored procedure of Array of VARCHARS (just 1 input array for ZDA) and
    // one VARCHAR
    else if (stp.equals(STP_CSARRVCH3)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDVCHARRAY as VARCHAR(50) array[100] ");

      } catch (Exception e) {
      }
      st.close();

      sql = "create procedure " + stp + "  (in P1 " + COLLECTION
          + ".JDVCHARRAY, in P2 VARCHAR(50), in P3 " + COLLECTION
          + ".JDVCHARRAY, in P4 VARCHAR(50) )  " + " begin  "
          + "DECLARE var1 VARCHAR(50); " + " end ";
    }
    // Stored procedure of Array of ints and non-array parms
    else if (stp.equals(STP_CSARRIN2)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".JDINTARRAY as integer array[100] ");

      } catch (Exception e) {
      }
      st.close();
      // proc that just switches arrays p3->p5 and p1 -> p3 (and same with
      // integer parms)

      sql = "create procedure " + stp + "  (in p1 " + COLLECTION
          + ".JDINTARRAY, in p2 INTEGER, inout p3 " + COLLECTION
          + ".JDINTARRAY, inout p4 INTEGER, out p5 " + COLLECTION
          + ".JDINTARRAY, out p6 INTEGER )  " + " begin  "
          + "set p5[1] = p3[1]; " + "set p5[2] = p3[2]; "
          + "set p5[3] = p3[3]; " + "set p5[4] = p3[4]; "
          + "set p3[1] = p1[1]; " + "set p3[2] = p1[2]; "
          + "set p3[3] = p1[3]; " + "set p3[4] = p1[4]; " +

          "set p6 = p4; " + "set p4 = p2; " + " end ";
    }
    // Stored procedure of Array of varchars and non-array parms
    else if (stp.equals(STP_CSARRVCH2)) {
      Statement st = connection.createStatement();
      try {
        st.execute("create type " + JDSetupProcedure.COLLECTION
            + ".ARRVCH as VARCHAR(50) array[100] ");

      } catch (Exception e) {
      }
      st.close();
      // proc that just switches arrays p3->p5 and p1 -> p3 (and same with
      // integer parms)

      sql = "create procedure " + stp + "  (in p1 " + COLLECTION
          + ".ARRVCH, in p2 VARCHAR(50), inout p3 " + COLLECTION
          + ".ARRVCH, inout p4 VARCHAR(50), out p5 " + COLLECTION
          + ".ARRVCH, out p6 VARCHAR(50) )  " + " begin  "
          + "set p5[1] = p3[1]; " + "set p5[2] = p3[2]; "
          + "set p5[3] = p3[3]; " + "set p5[4] = p3[4]; "
          + "set p3[1] = p1[1]; " + "set p3[2] = p1[2]; "
          + "set p3[3] = p1[3]; " + "set p3[4] = p1[4]; " +

          "set p6 = p4; " + "set p4 = p2; " + " end ";
    }

    // Stored procedure of Array of smallints
    else if (stp.equals(STP_CSARRSIN)) {

      String typeName = "SMALLINT";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of ints
    else if (stp.equals(STP_CSARRIN)) {
      String typeName = "INTEGER";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of booleans
    else if (stp.equals(STP_CSARRBOO)) {
      String typeName = "BOOLEAN";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of bigints
    else if (stp.equals(STP_CSARRBIN)) {
      String typeName = "BIGINT";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRREA)) {
      String typeName = "REAL";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRFLO)) {
      String typeName = "FLOAT";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRDOU)) {
      String typeName = "DOUBLE";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRDEC)) {
      String typeName = "DECIMAL(10,5)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRNUM)) {
      String typeName = "NUMERIC(10,5)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRCH1)) {
      // ???luw does not like char(1) com.ibm.db2.jcc.a.rm: DB2 SQL Error:
      // SQLCODE=-601, SQLSTATE=42710, SQLERRMC=PAULDEV.ARRCH1;DATA TYPE,
      // DRIVER=4.0.100
      // but I think it is valid
      String typeName = "CHAR(1)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRCH50)) {
      String typeName = "CHAR(50)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRVCH)) {
      String typeName = "VARCHAR(50)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRGR)) {
      String typeName = "GRAPHIC ccsid 1200";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRVGR)) {
      String typeName = "VARGRAPHIC(50) ccsid 1200";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRCLO)) {
      String typeName = "CLOB(100)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRBLO)) {
      String typeName = "BLOB(100)";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRDAT)) {
      String typeName = "DATE";
      sql = getProcArray(stp, typeName, connection);
    } // Stored procedure of Array of
    else if (stp.equals(STP_CSARRTIM)) {
      String typeName = "TIME";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRTS)) {
      String typeName = "TIMESTAMP";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRBY)) {
      String typeName = "BINARY(50)";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRVBY)) {
      String typeName = "VARBINARY(50)";
      sql = getProcArray(stp, typeName, connection);
    }
    // Stored procedure of Array of
    else if (stp.equals(STP_CSARRXML)) {
      String typeName = "XML";
      sql = getProcArray(stp, typeName, connection);
    }

    // Throw an exception if a bad stored procedure id
    // was passed.
    else {
      throw new Exception("Bad stored procedure: " + stp + ".");
    }

    // Create the stored procedure. If an error occurs,
    // just assume it was already created.
    boolean created = false;
    Statement s = connection.createStatement();
    try {
      s.executeUpdate(sql);
      procedureDefinitions.put(stp, sql);
      created = true;
    } catch (SQLException e) {
      System.out.print("Unable to create procedure using: " + sql + "\n");
      String sqlState = e.getSQLState(); // @A1A
      if (sqlState == null)
        sqlState = "";
      if ((!sqlState.equals("42733")) // @A1C
          && (!sqlState.equals("42723")) // @A1A
          && (!sqlState.equals("42710"))) { // @A2A
        System.out.println("SQLState is " + sqlState);
        JDTestcase.dumpServerJobLog(connection);

        e.printStackTrace(System.out);
      } else {
        System.out.println(" ignored states 42733,42723,42710");
      }
      created = false;
    }

    // We need to grant object authority to the
    // program so that other users may call it.

    // currently there is a problem in Native driver with GRANT ALL PRIVILEGES
    String driverName = "Unknown";
    try {
      driverName = connection.getMetaData().getDriverName();
    } catch (Exception e) {
    }

    if (driverName.equals("DB2 for OS/400 JDBC Driver"))
      created = false;
    // LUW doesn't like grant all. Fails with SQL0557
    if (isLUW)
      created = false;

    if (created) {
      try {
        s.executeUpdate(
            "GRANT ALL PRIVILEGES ON PROCEDURE " + stp + " TO PUBLIC");
      } catch (Exception e) {
        System.out.println("Exception using driver " + driverName);
        e.printStackTrace();
      }
    }

    s.close();
  }

  public static void dropCollections(Connection c) {
    dropCollection(c, COLLECTION);
  }

  public static void dropCollection(Connection c, String collection) {
    try {
      System.out.println("Dropping collection " + collection + ".");

      Statement s = c.createStatement();
      try {
	  s
	    .executeUpdate("CALL QSYS2.QCMDEXC('CHGJOB INQMSGRPY(*SYSRPYL)')");
      } catch (SQLException e) {
	  e.printStackTrace();
      }

      s.executeUpdate("DROP COLLECTION " + collection);
      s.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  // @B3A
  /**
   * Drops a procedure created by the test suite.
   **/
  public static void dropProcedure(Connection c, String stp) {
    try {
      System.out.println("Dropping procedure " + stp + ".");

      Statement s = c.createStatement();
      s.executeUpdate("DROP PROCEDURE " + stp);
      s.close();
    } catch (SQLException e) {
      if (JDCleanup.isImportantException(e)) { 
        System.out.println(e.getMessage());
      }
    }
  }

  // @B3A
  /**
   * Drops all procedures created by the test suite.
   **/
  public static void dropProcedures(Connection c) {
    dropProcedure(c, STP_RS0);
    dropProcedure(c, STP_RS1);
    dropProcedure(c, STP_RS1S);
    dropProcedure(c, STP_RS3);
    dropProcedure(c, STP_CS0);
    dropProcedure(c, STP_CS1); // @E1A
    dropProcedure(c, STP_CSRV); // @C2A
    dropProcedure(c, STP_CSPARMS);
    dropProcedure(c, STP_CSPARMSRS);
    dropProcedure(c, STP_CSPARMSRV); // @C2A
    dropProcedure(c, STP_CSPARMSRSRV); // @C2A
    dropProcedure(c, STP_CSTYPESOUT);
    dropProcedure(c, STP_CSTYPESOUTX);
    dropProcedure(c, STP_CSTYPESOUTB);
    dropProcedure(c, STP_CSTYPESIN);
    dropProcedure(c, STP_CSTYPESINOUT);
    dropProcedure(c, STP_CSTYPESINOUTX);
    dropProcedure(c, STP_CSTYPESNULL);
    dropProcedure(c, STP_CSINOUT); // @D1A
    dropProcedure(c, STP_CSNULLTEST);
    dropProcedure(c, STP_CSSRS); // @KBA
    dropProcedure(c, STP_CSMSRS); // @KBA
    /*
     * No need to drop, these are created/droped in TC dropProcedure(c,
     * STP_CSARRSUM); dropProcedure(c, STP_CSARRINT); dropProcedure(c,
     * STP_CSARRINT2); dropProcedure(c, STP_CSARRINT3); dropProcedure(c,
     * STP_CSARRVCH3 ); //array of varchar(50) and non-array type
     * 
     * dropProcedure(c, STP_CSARRSIN); //array of smallints dropProcedure(c,
     * STP_CSARRIN ); //array of ints dropProcedure(c, STP_CSARRIN2 ); //array
     * of ints 2 dropProcedure(c, STP_CSARRBIN ); //array of bigints
     * dropProcedure(c, STP_CSARRREA ); //array of read dropProcedure(c,
     * STP_CSARRFLO ); //array of float dropProcedure(c, STP_CSARRDOU ); //array
     * of double dropProcedure(c, STP_CSARRDEC ); //array of decimal
     * dropProcedure(c, STP_CSARRNUM ); //array of numeric dropProcedure(c,
     * STP_CSARRCH1 ); //array of char(1) dropProcedure(c, STP_CSARRCH50 );
     * //array of char(50) dropProcedure(c, STP_CSARRVCH ); //array of
     * varchar(50) dropProcedure(c, STP_CSARRVCH2 ); //array of varchar(50) and
     * non-array type dropProcedure(c, STP_CSARRGR ); //array of graphic
     * dropProcedure(c, STP_CSARRVGR ); //array of vargraphic dropProcedure(c,
     * STP_CSARRCLO ); //array of CLOB(100) dropProcedure(c, STP_CSARRBLO );
     * //array of bLOB(100) dropProcedure(c, STP_CSARRDAT ); //array of date
     * dropProcedure(c, STP_CSARRTIM ); //array of tim dropProcedure(c,
     * STP_CSARRTS ); //array of timestame dropProcedure(c, STP_CSARRBY );
     * //array of binary dropProcedure(c, STP_CSARRVBY ); //array of varbinary
     * dropProcedure(c, STP_CSARRXML );
     */

  }

  /**
   * Drops all procedures created by the test suite, created in a specific
   * collection @C1A
   **/
  public static void dropProcedures(Connection c, String Collection) {
    COLLECTION = Collection;
    STP_RS0 = COLLECTION + ".JDRSNONE";
    STP_RS1 = COLLECTION + ".JDRSONE";
    STP_RS1S = COLLECTION + ".JDRSONES";
    STP_RS3 = COLLECTION + ".JDRSTHREE";
    STP_CS0 = COLLECTION + ".JDCSNONE";
    STP_CS1 = COLLECTION + ".JDCSONE"; // @E1A
    STP_CSRV = COLLECTION + ".JDCSRV"; // @C2A
    STP_CSPARMS = COLLECTION + ".JDCSPARMS";
    STP_CSPARMSRS = COLLECTION + ".JDCSPARMSRS";
    STP_CSPARMSRV = COLLECTION + ".JDCSPARMSRV"; // @C2A
    STP_CSPARMSRV = COLLECTION + ".JDCSPARMSRSRV"; // @C2A
    STP_CSTYPESOUT = COLLECTION + ".JDCSTYPESOUT";
    STP_CSTYPESOUTX = COLLECTION + ".JDCSTYPESOUTX";
    STP_CSTYPESOUTB = COLLECTION + ".JDCSTYPESOUTB";
    STP_CSTYPESIN = COLLECTION + ".JDCSTYPESIN";
    STP_CSTYPESINOUT = COLLECTION + ".JDCSTYPESIO";
    STP_CSTYPESINOUTX = COLLECTION + ".JDCSTYPESIOX";
    STP_CSTYPESNULL = COLLECTION + ".JDCSTYPESNL";
    STP_CSINOUT = COLLECTION + ".JDCSIO"; // @D1A
    STP_CSNULLTEST = COLLECTION + ".JDCSNULLTEST";
    STP_CSSRS = COLLECTION + ".JDCSSRS"; // @KBA
    STP_CSMSRS = COLLECTION + ".JDCSMSRS"; // @KBA
    /*
     * No need to drop, these are created/droped in TC STP_CSARRSUM = COLLECTION
     * + ".JDCSARRSUM"; STP_CSARRINT = COLLECTION + ".JDCSARRINT"; STP_CSARRINT2
     * = COLLECTION + ".JDCSARRINT2"; STP_CSARRINT3 = COLLECTION +
     * ".JDCSARRINT3"; STP_CSARRVCH3 = COLLECTION + ".JDCSARRVCH3"; //array of
     * varchar(50) and non-array type STP_CSARRSIN = COLLECTION + ".JDCSARRSIN";
     * //array of smallints STP_CSARRIN = COLLECTION + ".JDCSARRIN"; //array of
     * ints STP_CSARRIN2 = COLLECTION + ".JDCSARRIN2"; //array of ints 2
     * STP_CSARRBIN = COLLECTION + ".JDCSARRBIN"; //array of bigints
     * STP_CSARRREA = COLLECTION + ".JDCSARRREA"; //array of read STP_CSARRFLO =
     * COLLECTION + ".JDCSARRFLO"; //array of float STP_CSARRDOU = COLLECTION +
     * ".JDCSARRDOU"; //array of double STP_CSARRDEC = COLLECTION +
     * ".JDCSARRDEC"; //array of decimal STP_CSARRNUM = COLLECTION +
     * ".JDCSARRNUM"; //array of numeric STP_CSARRCH1 = COLLECTION +
     * ".JDCSARRCH1"; //array of char(1) STP_CSARRCH50 = COLLECTION +
     * ".JDCSARRCH50"; //array of char(50) STP_CSARRVCH = COLLECTION +
     * ".JDCSARRVCH"; //array of varchar(50) STP_CSARRVCH2 = COLLECTION +
     * ".JDCSARRVCH2"; //array of varchar(50) and non-array type STP_CSARRGR =
     * COLLECTION + ".JDCSARRGR"; //array of graphic STP_CSARRVGR = COLLECTION +
     * ".JDCSARRVGR"; //array of vargraphic STP_CSARRCLO = COLLECTION +
     * ".JDCSARRCLO"; //array of CLOB(100) STP_CSARRBLO = COLLECTION +
     * ".JDCSARRBLO"; //array of bLOB(100) STP_CSARRDAT = COLLECTION +
     * ".JDCSARRDAT"; //array of date STP_CSARRTIM = COLLECTION + ".JDCSARRTIM";
     * //array of tim STP_CSARRTS = COLLECTION + ".JDCSARRTS"; //array of
     * timestame STP_CSARRBY = COLLECTION + ".JDCSARRBY"; //array of binary
     * STP_CSARRVBY = COLLECTION + ".JDCSARRVBY"; //array of varbinary
     * STP_CSARRXML = COLLECTION + ".JDCSARRXML"; //array of xml
     */
    dropProcedure(c, STP_RS0);
    dropProcedure(c, STP_RS1);
    dropProcedure(c, STP_RS1S);
    dropProcedure(c, STP_RS3);
    dropProcedure(c, STP_CS0);
    dropProcedure(c, STP_CS1); // @E1A
    dropProcedure(c, STP_CSRV); // @C2A
    dropProcedure(c, STP_CSPARMS);
    dropProcedure(c, STP_CSPARMSRS);
    dropProcedure(c, STP_CSPARMSRV); // @C2A
    dropProcedure(c, STP_CSPARMSRSRV); // @C2A
    dropProcedure(c, STP_CSTYPESOUT);
    dropProcedure(c, STP_CSTYPESOUTX);
    dropProcedure(c, STP_CSTYPESOUTB);
    dropProcedure(c, STP_CSTYPESIN);
    dropProcedure(c, STP_CSTYPESINOUT);
    dropProcedure(c, STP_CSTYPESINOUTX);
    dropProcedure(c, STP_CSTYPESNULL);
    dropProcedure(c, STP_CSINOUT); // @D1A
    dropProcedure(c, STP_CSNULLTEST); // @D1A
    dropProcedure(c, STP_CSSRS); // @KBA
    dropProcedure(c, STP_CSMSRS); // @KBA
    dropProcedure(c, STP_CSARRSUM);
    dropProcedure(c, STP_CSARRINT);
    dropProcedure(c, STP_CSARRINT4);
    dropProcedure(c, STP_CSARRINT2);
    dropProcedure(c, STP_CSARRINT3);
    dropProcedure(c, STP_CSARRINTN);
    dropProcedure(c, STP_CSARRVCH3); // array of varchar(50) and non=arraytype

    dropProcedure(c, STP_CSARRSIN); // array of smallints
    dropProcedure(c, STP_CSARRIN); // array of ints
    dropProcedure(c, STP_CSARRIN2); // array of ints 2
    dropProcedure(c, STP_CSARRBIN); // array of bigints
    dropProcedure(c, STP_CSARRBOO); // array of booleans
    dropProcedure(c, STP_CSARRREA); // array of read
    dropProcedure(c, STP_CSARRFLO); // array of float
    dropProcedure(c, STP_CSARRDOU); // array of double
    dropProcedure(c, STP_CSARRDEC); // array of decimal
    dropProcedure(c, STP_CSARRNUM); // array of numeric
    dropProcedure(c, STP_CSARRCH1); // array of char(1)
    dropProcedure(c, STP_CSARRCH50); // array of char(50)
    dropProcedure(c, STP_CSARRVCH); // array of varchar(50)
    dropProcedure(c, STP_CSARRVCH2); // array of varchar(50) and non=arraytype
    dropProcedure(c, STP_CSARRGR); // array of graphic
    dropProcedure(c, STP_CSARRVGR); // array of vargraphic
    dropProcedure(c, STP_CSARRCLO); // array of CLOB(100)
    dropProcedure(c, STP_CSARRBLO); // array of bLOB(100)
    dropProcedure(c, STP_CSARRDAT); // array of date
    dropProcedure(c, STP_CSARRTIM); // array of tim
    dropProcedure(c, STP_CSARRTS); // array of timestame
    dropProcedure(c, STP_CSARRBY); // array of binary
    dropProcedure(c, STP_CSARRVBY); // array of varbinary
    dropProcedure(c, STP_CSARRXML);

  }

  // 1 create array type of type typeName called arrayTypeName
  // 2 generate sql to create proc
  // proc simply reverses order of an input array and returns it as output parm
  public static String getProcArray(String stp, String typeName,
      Connection con) {
    // array type called COLLECTION (same stp collection) + "ARR" +
    // type-initials
    String arrayTypeName = stp.substring(0, stp.indexOf("JDCSARR"))
        + stp.substring(stp.indexOf("JDCSARR") + 4);// "ARR" + type-initials

    // remove this later
    try {
      Statement st = con.createStatement();
      try {
        st.execute("drop type " + arrayTypeName);
      } catch (Exception e) {
        String message = e.toString();
        if ((message.indexOf("type *SQLUDT not found") >= 0)
            || (message.indexOf("-204") >= 0)) {
          // type is not found
        } else {
          System.out.println("WARNING: Error dropping type " + arrayTypeName);
          e.printStackTrace();
        }
      }
      try {
        st.execute("drop procedure " + stp);
      } catch (Exception e) {

        String message = e.toString();
        if ((message.indexOf("not found") >= 0)
            || (message.indexOf("-204") >= 0)) {
          // procedure is not found
        } else {
          System.out.println("WARNING: Error dropping procedure " + stp);
          e.printStackTrace();
        }
      }
      st.close();
    } catch (Exception e) {
    }

    // reverse array proc
    try {
      Statement st = con.createStatement();
      st.execute(
          "create type " + arrayTypeName + " as " + typeName + " array[100] ");
      st.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "create procedure " + stp + "  (in arrIn " + arrayTypeName
        + ", out arrOut  " + arrayTypeName + ") " + " begin  " + "DECLARE var1 "
        + typeName + "; " + "DECLARE len, i, j INTEGER; "
        + "set len = CARDINALITY(arrIn); " +

        /* This next logic needed in order to return 0 length arrays */
        "if len < 1 then " + "set arrOut = arrIn; " + "else " + "set i = 1; "
        + "set j = len; " + "WHILE i <= len DO " + "set arrOut[i] = arrIn[j]; "
        + "set i = i + 1; " + "set j = j - 1; " + "END WHILE; " + "END IF; "
        + " end ";
  }

  /**
   * Returns a stored procedure call SQL string.
   * 
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures.lobSupport
   *          Are lobs supported?
   * @param supportedFeatures.bigintSupport
   *          Are BIGINTs supported? // @B0A
   * 
   * @return The SQL string.
   **/
  public static String getSQLString(String stp, JDSupportedFeatures supportedFeatures)
      throws Exception {
    String sql = null;

    // Stored procedure has parameters for each type.
    if ((stp.equals(STP_CSTYPESOUT)) || (stp.equals(STP_CSTYPESOUTB))
        || (stp.equals(STP_CSTYPESOUTX)) || (stp.equals(STP_CSTYPESIN))
        || (stp.equals(STP_CSTYPESINOUT)) || (stp.equals(STP_CSTYPESINOUTX))
        || (stp.equals(STP_CSTYPESNULL))) {
      StringBuffer buffer = new StringBuffer();
      buffer.append("CALL " + stp
          + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
      if (supportedFeatures.lobSupport)
        buffer.append(", ?, ?, ?, ?");
      if (supportedFeatures.bigintSupport)
        buffer.append(",?"); // @B0A
      if (supportedFeatures.decfloatSupport)
        buffer.append(",?,?"); // @B0A
      if (supportedFeatures.booleanSupport)
        buffer.append(",?");
      buffer.append(")");
      sql = buffer.toString();
    }

    // Throw an exception if a bad stored procedure id
    // was passed.
    else {
      throw new Exception("Bad stored procedure: " + stp + ".");
    }

    return sql;
  }

  // @B0A
  /**
   * Runs this class as an application. It will simply drop all procedures
   * listed in the catalog for this schema.
   * 
   * @B2AC - make it run with native driver also
   **/
  public static void main(String[] args) {
    if ((args.length != 3) && (args.length != 4)) {
      System.out
          .println("Usage: java JDSetupProcedure systemName userid password");
      System.exit(0);
    }

    String systemName = args[0];
    String userid = args[1];
    String password = args[2];
    Connection connection = null;
    boolean nativeDriver = false;

    if ((args.length == 4) && (args[3].equalsIgnoreCase("native")))
      nativeDriver = true;

    try {
      if (nativeDriver) {
        connection = DriverManager.getConnection("jdbc:db2:" + systemName,
            userid, password);
      } else {
        DriverManager.registerDriver(new AS400JDBCDriver());
        connection = DriverManager.getConnection("jdbc:as400:" + systemName,
            userid, password);
      }
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery(
          "SELECT SPECIFIC_NAME FROM QSYS2.SYSPROCS WHERE SPECIFIC_SCHEMA='"
              + COLLECTION + "'");
      Statement statement2 = connection.createStatement();
      while (rs.next()) {
        String stp = rs.getString(1);
        System.out.println("Dropping stored procedure " + stp + ".");
        try {
          statement2.executeUpdate("DROP PROCEDURE " + COLLECTION + "." + stp);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      statement2.close();
      rs.close();
      statement.close();
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace(System.out);
    }
  }

  /**
   * Prepares a stored procedure call.
   * 
   * @param connection
   *          The connection.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *         What features are supported? 
   * 
   * @return The callable statement.
   **/
  public static CallableStatement prepare(Connection connection, String stp,
      JDSupportedFeatures supportedFeatures) throws Exception {
    return connection.prepareCall(getSQLString(stp, supportedFeatures));
  }

 
 
  /**
   * Registers out parameters for a stored procedure call.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *          What features are supported? 
   * @param driver
   *          Which driver is being used
   **/

  public static void register(CallableStatement cs, String stp, JDSupportedFeatures supportedFeatures, int driver) throws Exception {
    if ((stp.equals(STP_CSTYPESOUT)) || (stp.equals(STP_CSTYPESOUTB))
        || (stp.equals(STP_CSTYPESOUTX)) || (stp.equals(STP_CSINOUT))
        || (stp.equals(STP_CSTYPESINOUT)) || (stp.equals(STP_CSTYPESINOUTX))
        || (stp.equals(STP_CSTYPESNULL))) {
      cs.registerOutParameter(1, Types.SMALLINT);
      cs.registerOutParameter(2, Types.INTEGER);
      cs.registerOutParameter(3, Types.REAL);
      // jcc requires this to be a float
      if (driver == JDTestDriver.DRIVER_JCC) {
        // System.out.println("Registering parm 4 as Types.REAL for "+stp);
        cs.registerOutParameter(4, Types.REAL);
      } else {
        cs.registerOutParameter(4, Types.DOUBLE);
      }
      cs.registerOutParameter(5, Types.DOUBLE);
      cs.registerOutParameter(6, Types.DECIMAL);
      cs.registerOutParameter(7, Types.DECIMAL);
      cs.registerOutParameter(8, Types.NUMERIC);
      cs.registerOutParameter(9, Types.NUMERIC);
      cs.registerOutParameter(10, Types.CHAR);
      cs.registerOutParameter(11, Types.CHAR);
      cs.registerOutParameter(12, Types.VARCHAR);
      cs.registerOutParameter(13, Types.BINARY);
      cs.registerOutParameter(14, Types.VARBINARY);
      cs.registerOutParameter(15, Types.DATE);
      cs.registerOutParameter(16, Types.TIME);
      cs.registerOutParameter(17, Types.TIMESTAMP);
      if (supportedFeatures.lobSupport) {
          cs.registerOutParameter(18, Types.VARCHAR);
          if (driver == JDTestDriver.DRIVER_JCC) {
            cs.registerOutParameter(19, Types.VARBINARY);
            cs.registerOutParameter(20, Types.VARCHAR);
            cs.registerOutParameter(21, Types.VARCHAR);
          } else {
            cs.registerOutParameter(19, Types.BLOB);
            cs.registerOutParameter(20, Types.CLOB);
            cs.registerOutParameter(21, Types.CLOB);
          }
      }
      if (supportedFeatures.bigintSupport) // @B0A
        cs.registerOutParameter(22, Types.BIGINT); // @B0A
      if (stp.equals(STP_CSINOUT)) // @D1A
      { // @D1A
        cs.registerOutParameter(23, Types.VARCHAR); // @D1A
        cs.registerOutParameter(24, Types.VARCHAR); // @D1A
        if (supportedFeatures.decfloatSupport) {
          // Register as Types.VARCHAR since we want to get a string from it
          cs.registerOutParameter(25, Types.VARCHAR);
          cs.registerOutParameter(26, Types.VARCHAR);
        }
        if (supportedFeatures.booleanSupport) {
          // Register as Types.VARCHAR since we want to get a string from it
          cs.registerOutParameter(27, Types.BOOLEAN);
        }
      } else { // @D1A
        if (supportedFeatures.decfloatSupport) {
          // Register as Types.VARCHAR since we want to get a string from it
          cs.registerOutParameter(23, Types.VARCHAR);
          cs.registerOutParameter(24, Types.VARCHAR);
        }
        if (supportedFeatures.booleanSupport) {
          // Register as Types.VARCHAR since we want to get a string from it
          cs.registerOutParameter(25, Types.BOOLEAN);
        }

      }
    }
  }

  /**
   * Sets the parameters for the STP_CSTYPES stored procedure.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   * @param  supportedFeatures
   *          What features are supported
   **/
  public static void setTypesParameters(CallableStatement cs, String stp,
      JDSupportedFeatures supportedFeatures) throws Exception {
    if ((stp.equals(STP_CSTYPESIN)) || (stp.equals(STP_CSINOUT))
        || (stp.equals(STP_CSTYPESINOUT)) || (stp.equals(STP_CSTYPESINOUTX)))
      cs.setShort(1, (short) 1);
    cs.setInt(2, 51);
    cs.setFloat(3, 3.3f);
    cs.setFloat(4, 4.4f);
    cs.setDouble(5, 5.5);
    cs.setBigDecimal(6, new BigDecimal("6"));
    cs.setBigDecimal(7, new BigDecimal("777.777"));
    cs.setBigDecimal(8, new BigDecimal("8888"));
    cs.setBigDecimal(9, new BigDecimal("-9999.99999"));
    cs.setString(10, "H");
    cs.setString(11, "Hola");
    cs.setString(12, "Mi");
    cs.setBytes(13, new byte[] { (byte) 'J' });
    cs.setBytes(14, new byte[] { (byte) 'B', (byte) 'o', (byte) 'n', (byte) 'j',
        (byte) 'o', (byte) 'u', (byte) 'r' });
    cs.setDate(15, new Date(121));
    cs.setTime(16, new Time(2123));
    cs.setTimestamp(17, new Timestamp(32342));
    if (supportedFeatures.lobSupport) {
      cs.setString(18, "http://www.as400.ibm.com/toolbox");
      cs.setBytes(19, new byte[] { (byte) 'A', (byte) 'l', (byte) 'o',
          (byte) 'h', (byte) 'a' });
      cs.setString(20, "Tourists");
      cs.setString(21, "Natives");
    }
    if (supportedFeatures.bigintSupport) // @B0A
      cs.setLong(22, 45632); // @B0A
    if (stp.equals(STP_CSINOUT)) // @D1A
    { // @D1A
      cs.setString(23, "Buenes"); // @D1A
      cs.setString(24, "Diaz"); // @D1A
      if (supportedFeatures.decfloatSupport) {
        cs.setString(25, "1234561234567890");
        cs.setString(26, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        cs.setBoolean(27, true);
      }
    } else { // @D1A
      if (supportedFeatures.decfloatSupport) {
        cs.setString(23, "1234561234567890");
        cs.setString(24, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        cs.setBoolean(25, true);
      }

    }
  }

  /**
   * Sets the parameters for the STP_CSTYPES stored procedure.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *          What features are supported? 
   * @param except
   *          Don't set value for this column
   **/
  public static void setTypesParametersButForOne(CallableStatement cs, String stp,
      JDSupportedFeatures supportedFeatures, int except) throws Exception {

    if (except != 1)
      if ((stp.equals(STP_CSTYPESIN)) || (stp.equals(STP_CSINOUT))
          || (stp.equals(STP_CSTYPESINOUT)) || (stp.equals(STP_CSTYPESINOUTX)))
        cs.setObject(1, new Short("-33"));
    if (except != 2)
      cs.setObject(2, new Integer(52));
    if (except != 3)
      cs.setObject(3, new Float(3.4f));
    if (except != 4)
      cs.setObject(4, new Float(4.5f));
    if (except != 5)
      cs.setObject(5, new Double(5.6));
    if (except != 6)
      cs.setObject(6, new BigDecimal("7"));
    if (except != 7)
      cs.setObject(7, new BigDecimal("777.778"));
    if (except != 8)
      cs.setObject(8, new BigDecimal("8889"));
    if (except != 9)
      cs.setObject(9, new BigDecimal("-9999.99998"));
    if (except != 10)
      cs.setObject(10, "I");
    if (except != 11)
      cs.setObject(11, "Holb");
    if (except != 12)
      cs.setObject(12, "Mj");
    if (except != 13)
      cs.setObject(13,
          new JDLobTest.JDTestBlob((new String("Murck")).getBytes()));
    if (except != 14)
      cs.setObject(14,
          new JDLobTest.JDTestBlob(new byte[] { (byte) 'B', (byte) 'o',
              (byte) 'n', (byte) 'j', (byte) 'o', (byte) 'u', (byte) 's' }));
    if (except != 15)
      cs.setObject(15, Date.valueOf("1980-06-25"));
    if (except != 16)
      cs.setObject(16, new Time(14, 11, 19));
    if (except != 17)
      cs.setObject(17, new Timestamp(32343));
    if (supportedFeatures.lobSupport) {
      if (except != 18)
        cs.setObject(18, "http://www.as400.ibm.com/native");
      if (except != 19)
        cs.setObject(19, new JDLobTest.JDTestBlob(new byte[] { (byte) 'A',
            (byte) 'l', (byte) 'o', (byte) 'h', (byte) 'b' }));
      if (except != 20)
        cs.setObject(20, "Touristt");
      if (except != 21)
        cs.setObject(21, "Nativet");
    }
    if (except != 22)
      if (supportedFeatures.bigintSupport)
        cs.setObject(22, new Long((long) 45633));
    if (stp.equals(STP_CSINOUT)) {
      if (except != 23)
        cs.setObject(23, "Buenet");
      if (except != 24)
        cs.setObject(24, "Diba");
      if (supportedFeatures.decfloatSupport) {
        if (except != 25)
          cs.setString(25, "1234561234567890");
        if (except != 26)
          cs.setString(26, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        if (except != 27)
          cs.setBoolean(27, true);
      }
    } else {
      if (supportedFeatures.decfloatSupport) {
        if (except != 23)
          cs.setString(23, "1234561234567890");
        if (except != 24)
          cs.setString(24, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        if (except != 25) {
          cs.setBoolean(25, true);
        }
      }
    }
  }

  /**
   * Sets the parameters for the STP_CSTYPES stored procedure.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   *          
   * @param supportedFeatures
   *          Features supported by database 
   * @param accept
   *          Set value only for this column
   **/
  public static void setTypesParametersForOne(CallableStatement cs, String stp,
      JDSupportedFeatures supportedFeatures, int accept) throws Exception {

    if (accept == 1)
      if ((stp.equals(STP_CSTYPESIN)) || (stp.equals(STP_CSINOUT))
          || (stp.equals(STP_CSTYPESINOUT))
          || (stp.equals(STP_CSTYPESINOUTX))) {
        cs.setObject(1, new Short("-34"));
        return;
      }
    if (accept == 2) {
      cs.setObject(2, new Integer(53));
      return;
    }
    if (accept == 3) {
      cs.setObject(3, new Float(3.5f));
      return;
    }
    if (accept == 4) {
      cs.setObject(4, new Float(4.6f));
      return;
    }
    if (accept == 5) {
      cs.setObject(5, new Double(5.7));
      return;
    }
    if (accept == 6) {
      cs.setObject(6, new BigDecimal("8"));
      return;
    }
    if (accept == 7) {
      cs.setObject(7, new BigDecimal("777.779"));
      return;
    }
    if (accept == 8) {
      cs.setObject(8, new BigDecimal("8899"));
      return;
    }
    if (accept == 9) {
      cs.setObject(9, new BigDecimal("-9999.9998"));
      return;
    }
    if (accept == 10) {
      cs.setObject(10, "J");
      return;
    }
    if (accept == 11) {
      cs.setObject(11, "Holc");
      return;
    }
    if (accept == 12) {
      cs.setObject(12, "Mk");
      return;
    }
    if (accept == 13) {
      cs.setObject(13,
          new JDLobTest.JDTestBlob((new String("Murcl")).getBytes()));
      return;
    }
    if (accept == 14) {
      cs.setObject(14,
          new JDLobTest.JDTestBlob(new byte[] { (byte) 'B', (byte) 'o',
              (byte) 'n', (byte) 'j', (byte) 'o', (byte) 'u', (byte) 't' }));
      return;
    }
    if (accept == 15) {
      cs.setObject(15, Date.valueOf("1980-06-26"));
      return;
    }
    if (accept == 16) {
      cs.setObject(16, new Time(14, 11, 20));
      return;
    }
    if (accept == 17) {
      cs.setObject(17, new Timestamp(32353));
      return;
    }
    if (supportedFeatures.lobSupport) {
      if (accept == 18) {
        cs.setObject(18, "http://www.as400.ibm.com/komal");
        return;
      }
      if (accept == 19) {
        cs.setObject(19, new JDLobTest.JDTestBlob(new byte[] { (byte) 'A',
            (byte) 'l', (byte) 'o', (byte) 'g', (byte) 'b' }));
        return;
      }
      if (accept == 20) {
        cs.setObject(20, "TourIndia");
        return;
      }
      if (accept == 21) {
        cs.setObject(21, "Hyderabad");
        return;
      }
    }
    if (accept == 22)
      if (supportedFeatures.bigintSupport) {
        cs.setObject(22, new Long(1411948972002L));
        return;
      }
    if (stp.equals(STP_CSINOUT)) {
      if (accept == 23) {
        cs.setObject(23, "yaad");
        return;
      }
      if (accept == 24) {
        cs.setObject(24, "work");
        return;
      }
      if (supportedFeatures.decfloatSupport) {
        if (accept == 25)
          cs.setString(25, "1234561234567890");
        if (accept == 26)
          cs.setString(26, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        if (accept == 27) {
          cs.setBoolean(27, true);
        }
      }
    } else {
      if (supportedFeatures.decfloatSupport) {
        if (accept == 23)
          cs.setString(23, "1234561234567890");
        if (accept == 24)
          cs.setString(24, "1234123456789012345678901234567890");
      }
      if (supportedFeatures.booleanSupport) {
        if (accept == 25) {
          cs.setBoolean(25, true);
        }
      }

    }
  }

  /**
   * Checks the parameters for the STP_CSTYPES stored procedure.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures.lobSupport
   *          Are lobs supported?
   * @param supportedFeatures
   *          What features are supported by the database
   * @param except
   *          Do not check for this parameter
   **/
  public static boolean checkAllButForOne(CallableStatement cs, String stp,
      JDSupportedFeatures supportedFeatures, int except) throws Exception {

    int count = 26;
    if (!stp.equals(STP_CSTYPESINOUT) && (!stp.equals(STP_CSTYPESINOUTX))) {
      System.out.println("checkAll2 defined only for " + STP_CSTYPESINOUT);
      return false;
    }
    String checkString[] = new String[count];
    boolean check[] = new boolean[count];
    for (int i = 0; i < check.length; i++)
      check[i] = true;

    if (except != 1)
      check[1] = comp(((Integer) cs.getObject(1)).intValue(), -33 + 23);
    if (except != 2)
      check[2] = comp(((Integer) cs.getObject(2)).intValue(), 52 * -2);
    if (except != 3)
      check[3] = comp(((Float) cs.getObject(3)).floatValue(), 3.4f * -1);
    if (except != 4)
      check[4] = comp(((Double) cs.getObject(4)).doubleValue(), 4.5 + 543.2);
    if (except != 5)
      check[5] = comp(((Double) cs.getObject(5)).doubleValue(), 5.6 - 54.54);
    if (except != 6)
      check[6] = comp(((BigDecimal) cs.getObject(6)).doubleValue(), 7 - 3);
    if (except != 7)
      check[7] = comp(((BigDecimal) cs.getObject(7)).doubleValue(),
          30777.77803);
    if (except != 8)
      check[8] = comp(((BigDecimal) cs.getObject(8)).doubleValue(), 8889 + 1);
    if (except != 9)
      check[9] = comp(((BigDecimal) cs.getObject(9)).doubleValue(),
          -9999.99998 - 1);
    if (except != 10)
      check[10] = comp((String) cs.getObject(10), "C");
    if (except != 11)
      check[11] = comp((String) cs.getObject(11),
          "Jim                                               ");

    if (except != 12)
      check[12] = comp((String) cs.getObject(12), "MjJDBC");

    // TODO: Murck is in EBCDIC coding and not ASCII coding, so we need to check
    // somehow differently
    // if(accept == 13)
    // check[13] = comp(new String((byte[])cs.getObject(13)), "Murck");

    if (except != 14) {
      byte[] bytes = (byte[]) cs.getObject(14);
      String check14 = new String(bytes, "UTF-8"); // default is platform
                                                   // charset
      // TODO: returned string is "Dave WallBonjous", the 'Dave Wall' part is in
      // EBCDIC coding and so
      // we need to figure out a way to check for it
      // check[14] = check14.equals("Dave WallBonjous");
      // check[14] = check14.endsWith("Bonjous");
      String byteString = JDTestUtilities.dumpBytes(bytes);
      checkString[14] = "String='" + check14 + "' HEX='" + byteString + "'";
      check[14] = check14.endsWith("onjous"); // ~B is not a utf-8 char
      // If this happened to fail, then check the byteString
      if (!check[14]) {
        check[14] = "c481a58540e6819393426f6e6a6f7573".equals(byteString);
      }

    }
    if (except != 15)
      check[15] = comp(((Date) cs.getObject(15)).toString(), "1998-04-15");
    if (except != 16)
      check[16] = comp(((Time) cs.getObject(16)).toString(), "08:42:30");
    if (except != 17)
      check[17] = comp(((Timestamp) cs.getObject(17)).toString(),
          "2001-11-18 13:42:22.123456");

    if (supportedFeatures.lobSupport) {
      if (except != 18)
        check[18] = cs.getObject(18).equals("http://www.sony.com/pix.html");

      if (except != 19) {
        /* Blob b = (Blob) */ cs.getObject(19);
        // TODO: its EBCDIC coding (same as 13,14)
        // check[19] = comp(new String(b.getBytes(1, (int)b.length())), "Hello
        // there");
      }
      if (except != 20) {
        Clob c = (Clob) cs.getObject(20);
        check[20] = comp(c.getSubString(1, (int) c.length()), "Welcome");
      }
      if (except != 21) {
        Clob c = (Clob) cs.getObject(21);
        check[21] = comp(c.getSubString(1, (int) c.length()), "Goodbye");
      }
    }

    if (except != 22)
      if (supportedFeatures.bigintSupport)
        check[22] = comp(((Long) cs.getObject(22)).longValue(),
            987662234567.00);

    if (except != 23)
      if (supportedFeatures.decfloatSupport)
        check[23] = comp(cs.getString(23), "1234561234567891");

    if (except != 24)
      if (supportedFeatures.decfloatSupport)
        check[24] = comp(cs.getString(24),
            "1234123456789012345678901234567891");

    if (except != 25)
      if (supportedFeatures.booleanSupport)
        check[25] = comp(cs.getString(25), "0");

    boolean success = true;
    for (int i = 0; i < check.length; i++)
      if (!check[i]) {
        success = false;
        String stringValue;
        try {
          if (checkString[i] != null) {
            stringValue = checkString[i];
          } else {
            stringValue = cs.getString(i);
          }
        } catch (Exception e) {
          e.printStackTrace();
          stringValue = "'Exception caught: " + e.toString() + "'";
        }
        System.out.println("Failed on parameter (1-based #): " + i
            + " string = " + stringValue);
      }

    return success;
  }

  /**
   * Checks the parameters for the STP_CSTYPES stored procedure.
   * 
   * @param cs
   *          The callable statement.
   * @param stp
   *          The stored procedure.
   * @param supportedFeatures
   *          What features are supported?
   **/
  public static boolean checkAllForOne(CallableStatement cs, String stp,
      JDSupportedFeatures supportedFeatures, int accept) throws Exception {

    if (!stp.equals(STP_CSTYPESINOUT) && (!stp.equals(STP_CSTYPESINOUTX))) {
      System.out.println("checkAll2 defined only for " + STP_CSTYPESINOUT);
      return false;
    }

    if (accept == 1)
      return comp(((Integer) cs.getObject(1)).intValue(), -34 + 23);
    if (accept == 2)
      return comp(((Integer) cs.getObject(2)).intValue(), 53 * -2);
    if (accept == 3)
      return comp(((Float) cs.getObject(3)).floatValue(), 3.5f * -1);
    if (accept == 4)
      return comp(((Double) cs.getObject(4)).doubleValue(), 4.6 + 543.2,
          0.00001);
    if (accept == 5)
      return comp(((Double) cs.getObject(5)).doubleValue(), 5.7 - 54.54);
    if (accept == 6)
      return comp(((BigDecimal) cs.getObject(6)).doubleValue(), 8 - 3);
    if (accept == 7)
      return comp(((BigDecimal) cs.getObject(7)).doubleValue(), 30777.77903);
    if (accept == 8)
      return comp(((BigDecimal) cs.getObject(8)).doubleValue(), 8899 + 1);
    if (accept == 9)
      return comp(((BigDecimal) cs.getObject(9)).doubleValue(), -9999.9998 - 1);
    if (accept == 10)
      return comp((String) cs.getObject(10), "C");
    if (accept == 11)
      return comp((String) cs.getObject(11),
          "Jim                                               ");

    if (accept == 12)
      return comp((String) cs.getObject(12), "MkJDBC");

    // TODO: Murck is in EBCDIC coding and not ASCII coding, so we need to check
    // somehow differently
    // if(accept == 13)
    // return comp(new String((byte[])cs.getObject(13)), "Murcl");

    if (accept == 14) {

      byte[] bytes = (byte[]) cs.getObject(14);
      String check14 = new String(bytes, "UTF-8"); // default is platform
                                                   // charset

      String byteString = JDTestUtilities.dumpBytes(bytes);

      // String check14 = new String((byte[])cs.getObject(14), "UTF-8");
      // TODO: returned string is "Dave WallBonjout", the 'Dave Wall' part is in
      // EBCDIC coding and so
      // we need to figure out a way to check for it
      // return check14.equals("Dave WallBonjout");
      // return check14.endsWith("onjout"); //~B is not a utf-8 char

      boolean ok = check14.endsWith("onjout"); // ~B is not a utf-8 char
      // If this happened to fail, then check the byteString
      if (!ok) {
        ok = "c481a58540e6819393426f6e6a6f7574".equals(byteString);
      }

      return ok;

    }
    if (accept == 15)
      return comp(((Date) cs.getObject(15)).toString(), "1998-04-15");
    if (accept == 16)
      return comp(((Time) cs.getObject(16)).toString(), "08:42:30");
    if (accept == 17)
      return comp(((Timestamp) cs.getObject(17)).toString(),
          "2001-11-18 13:42:22.123456");

    if (supportedFeatures.lobSupport) {
      if (accept == 18)
        return cs.getObject(18).equals("http://www.sony.com/pix.html");

      if (accept == 19) {
        /* Blob b = (Blob) */ cs.getObject(19);
        // TODO: Identify a way to compute value for check[19]
        // return comp(new String(b.getBytes(1, (int)b.length())), "Hello
        // there");
      }

      if (accept == 20) {
        Clob c = (Clob) cs.getObject(20);
        return comp(c.getSubString(1, (int) c.length()), "Welcome");
      }
      if (accept == 21) {
        Clob c = (Clob) cs.getObject(21);
        return comp(c.getSubString(1, (int) c.length()), "Goodbye");
      }
    }

    if (accept == 22)
      if (supportedFeatures.bigintSupport)
        return comp(((Long) cs.getObject(22)).longValue(), 987662234567.00);

    if (accept == 23)
      if (supportedFeatures.decfloatSupport)
        return comp(cs.getString(23), "1234561234567891");

    if (accept == 24)
      if (supportedFeatures.decfloatSupport)
        return comp(cs.getString(24), "1234123456789012345678901234567891");

    if (accept == 25)
      if (supportedFeatures.booleanSupport)
        return comp(cs.getString(25), "true");

    return true;

  }

  public static boolean comp(double actual, double expected) {
    if (actual != expected) {
      System.out.println(actual + " sb " + expected);
      return false;
    }
    return true;
  }

  public static boolean comp(double actual, double expected,
      double acceptableDiff) {
    if (actual != expected) {
      if (!(Math.abs(actual - expected) <= Math.abs(acceptableDiff))) {
        System.out.println(actual + " sb " + expected);
        return false;
      }
    }
    return true;
  }

  public static boolean comp(String actual, String expected) {
    if (!actual.equals(expected)) {
      System.out.println(actual + " sb " + expected);
      return false;
    }
    return true;
  }

}
