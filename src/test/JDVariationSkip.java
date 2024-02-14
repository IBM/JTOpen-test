///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDSchedulerServer.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

/**
 * This class provides a way to cause a variation to be skipped without
 * updating the testcase.  This was used to quickly remove testcases that 
 * are failing for a known reason. 
 *
 * This is also useful to see which problems are present in a release 
 */
public class JDVariationSkip {

    public static boolean debug = false; 
  /*
   * Add testcases here.  An ALL-N variation number means all variations should be
   *                      skipped, where N is the maximum variation number
   */
  static String[][] skipInformation = {

    { "toolbox",     "JDConnectionCursorHoldability",  "750","28", "Cursor Holdability not working in V7R1/V7R2 for toolbox driver"},
    { "toolbox",     "JDConnectionCursorHoldability",  "750","35", "Cursor Holdability not working in V7R1/V7R2 for toolbox driver"},

    { "toolbox",     "JDCSGetDate2",            "750", "23", "Julian date test not working in V7R3 and earlier"},

 
    { "toolbox",    "JDCSSetArray",                 "750", "52", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "93", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "116", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "140", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "154", "CLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "155", "BLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "161", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "172", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArray",                 "750", "181", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArray",                 "750", "189-272", "Time format change not working in V7R4"}, 
    { "toolbox",    "JDCSSetArray",                 "750", "302", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArray",                 "750", "343", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArray",                 "750", "366", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArray",                 "750", "390", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArray",                 "750", "404", "CLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "405", "BLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "411", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArray",                 "750", "413", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArray",                 "750", "422", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArray",                 "750", "431", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArray",                 "750", "439-522", "Time format change not working in V7R4"}, 
    { "toolbox",    "JDCSSetArrayN",                 "750", "52", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "93", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "116", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "140", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "154", "CLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "155", "BLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "161", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "172", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "181", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "189-272", "Time format change not working in V7R4"}, 
    { "toolbox",    "JDCSSetArrayN",                 "750", "302", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "343", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "366", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "390", "No XML (locators) arrays supported in TB"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "404", "CLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "405", "BLOB arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "411", "XML arrays cannot be support in JDBC (too large)"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "413", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "422", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "431", "Time format USA not working in V7R4"},
    { "toolbox",    "JDCSSetArrayN",                 "750", "439-522", "Time format change not working in V7R4"},
    { "toolbox",    "JDCSSetObject3",               "750", "73", "Datalink type not working for toolbox driver"},
    { "toolbox",    "JDCSSetObject3SQLType",        "750", "73", "Datalink type not working for toolbox driver"},
    { "toolbox",    "JDCSSetObject4",               "750", "73", "Datalink type not working for toolbox driver"}, 
    { "toolbox",    "JDCSSetObject4",               "750", "122", "Datalink type not working for toolbox driver"}, 
    { "toolbox",    "JDCSSetObject4SQLType",        "750", "73", "Datalink type not working for toolbox driver"}, 
    { "toolbox",    "JDCSSetObject4SQLType",        "750", "122", "Datalink type not working for toolbox driver"}, 

    { "toolbox",    "JDDSProperties",                 "999", "27-28", "getTranslateBinary not on Toolbox datasource"},
    { "toolbox",    "JDDSProperties",                 "999", "41-42", "getTrace not on Toolbox datasource"},

    { "toolbox",   "JDLobAccess",                  "750", "45-48",  "Lob locator is available after commit"}, 
    { "toolbox",   "JDLobBlobLocator",             "750", "111",  "Lob locator is available after rs.close"},
    { "toolbox",   "JDLobClobLocator",             "750", "243",  "Lob locator is available after rs.close"},
    { "toolbox",   "JDLobClobLocator",             "750", "256",  "Lob locator is available after rs.close"},
    { "toolbox",   "JDLobNClobLocator",            "750", "243",  "Lob locator is available after rs.close"},
    { "toolbox",   "JDLobNClobLocator",            "750", "256",  "Lob locator is available after rs.close"},
    { "toolbox",   "JDPSSetNString",               "999","70",   "Package cache and XML failing"},

    { "toolbox",   "JDRSMDGetColumnLabel",         "999", "44",  "Labels from stored procedure call incorrect"}, 


    { "jtopenPX",    "JDCSGetObject3",               "999", "25", "Proxy gets java.io.NotSerializableException: com.ibm.as400.access.AS400JDBCClobLocator"},
    { "jtopenPX",    "JDCSGetByte",                  "999", "59", "Proxy not throwing truncation"}, 
    { "jtopenPX",    "JDRSGetObject",               "999", "31-47", "Proxy gets java.io.NotSerializableException: com.ibm.as400.access.AS400JDBCClobLocator"},
    { "jtopenPX",    "JDStatementGetGeneratedKeys",  "999", "115-116", "Proxy does not support generatedKeysExecute"},
    { "jtopenPX",    "JDStatementGetGeneratedKeys",  "999", "118-119", "Proxy does not support generatedKeysExecute"},
    { "jtopenPX",    "JDStatementGetGeneratedKeys",  "999", "122-123", "Proxy does not support generatedKeysExecute"},
    { "jtopenPX",    "JDStatementGetGeneratedKeys",  "999", "125-126", "Proxy does not support generatedKeysExecute"},
    { "jtopenPX",    "JDConnectionCharacterTruncation", "999", "101-142", "Proxy get NOT_SERIALIZABLE: java.io.StringReader"},
    { "jtopenPX",    "JDConnectionCharacterTruncation", "999", "401-442", "Proxy get NOT_SERIALIZABLE: java.io.StringReader"},
    { "jtopenPX",    "JDConnectionNetworkTimeout",      "999", "3-15", "Proxy gets protocol error"},
    { "jtopenPX",    "JDLobGraphicData",                "999", "1-8", "JDClobProxy not serialiable"},
    { "jtopenPX",    "JDLobGraphicData",                "999", "46-59", "JDBlobProxy not serialiable"},
    { "jtopenPX",    "JDConnectionLibraries",           "999", "4-5", "Proxy gets wrong error message"},
    { "jtopenPX",    "JDConnectionLibraries",           "999", "9", "Proxy gets wrong error message"}, 
    { "jtopenPX",    "JDConnectionLibraries",           "999", "11", "Proxy gets wrong error message"},
    { "jtopenPX",    "JDConnectionLibraries",           "999", "14", "Proxy gets wrong error message"},
    { "jtopenPX",    "JDConnectionLibraries",           "999", "50-53", "Proxy gets wrong error message"},
    { "jtopenPX",    "JDConnectionLibraries",           "999", "57-58", "Proxy gets wrong error message"},
    { "jtopenPX",    "JDCSArrays",                      "999", "1-8",   "Arrays not working on proxy"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "3",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "9",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "15",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "21",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "27",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "33",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "39",  "Proxy does not return truncation"},
    { "jtopenPX",     "JDPSDataTruncation",             "999", "45",  "Proxy does not return truncation"},

   { "jtopenPX",     "JDLobNClobLocator",               "999", "240",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobNClobLocator",               "999", "241",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobNClobLocator",               "999", "242",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobNClobLocator",               "999", "249",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobNClobLocator",               "999", "257",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobNClobLocator8",              "999", "1-999",  "Proxy does not support locators"},

   { "jtopenPX",     "JDLobClobLocator",               "999", "240",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobClobLocator",               "999", "241",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobClobLocator",               "999", "242",  "Proxy does not support locators"},
   { "jtopenPX",     "JDLobClobLocator",               "999", "257",  "Proxy does not support locators"},
   { "jtopenPX",     "JDRSUpdateCharacterStream",      "999", "70",  "Length parameter (6) does not match actual length of buffer (0)"},
   { "jtopenPX",     "JDRSUpdateCharacterStream",      "999", "71",  "Updateable RS not supported"},
   { "jtopenPX",     "JDRSUpdateNCharacterStream",     "999", "70",  "Length parameter (6) does not match actual length of buffer (0)"},
   { "jtopenPX",     "JDRSUpdateNCharacterStream",     "999", "71",  "Updateable RS not supported"},
   { "jtopenPX",     "JDRSMDIsAutoIncrement",          "999", "27",  "Performance not good for proxy"},
   { "jtopenPX",     "JDStatementMisc",                "999", "25",  "Proxy Cancel did not work"},
   { "jtopenPX",     "JDConnectionAbort",              "999", "1-999", "parameters for Abort not Serializable"},
   { "jtopenPX",     "JDCSSetArrayN",                  "999", "1-999", "Proxy does not support arrays"},
   { "jtopenPX",     "JDCSSetArray",                   "999", "1-999", "Proxy does not support arrays"},
   { "jtopenPX",     "JDCSGetArray",                   "999", "51-999", "Proxy does not support arrays"},
   { "jtopenPX",     "JDConnectionCreateArrayOf",      "999", "1-999", "Proxy does not support arrays"},
   { "jtopenPX",     "JDSavepointsTestcase",           "999", "1-999", "Proxy does not support setSavepoint"},
   { "jtopenPX",     "JDPSSetBinaryStream",            "999", "79",    "Proxy does not support input stream with 0 bytes"},
   { "jtopenPX",     "JDPSSetBinaryStream",            "999", "80",    "Proxy does not support input stream with 0 bytes"},
   { "jtopenPX",     "JDPSSetBinaryStream",            "999", "142",   "Proxy does not support locators"},
   { "jtopenPX",     "JDPSSetBinaryStream",            "999", "143",   "Proxy does not support locators"},
   { "jtopenPX",     "JDStatementResults",             "999", "46",    "Proxy does not support getMoreResults"},
   { "jtopenPX",     "JDStatementResults",             "999", "63",    "Proxy does not support getMoreResults"},
   { "jtopenPX",     "JDStatementResults",             "999", "80",    "Proxy does not support getMoreResults"},
   { "jtopenPX",     "JDStatementResults",             "999", "94",    "Proxy does not support getMoreResults"},
   { "jtopenPX",     "JDStatementExceptions",          "999",  "11",    "Wrong exception returned"},
   { "jtopenPX",     "JDStatementExceptions",          "999",  "12",    "Wrong exception returned"},
   { "jtopenPX",     "JDPSSetInt",                     "999",  "37",    "Proxy not ignoring truncation warning"},
   { "jtopenPX",     "JDStatementWrapper",             "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDStatementWrapper",             "999",  "8",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDRSWrapper",                    "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDRSWrapper",                    "999",  "8",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDRSMDWrapper",                  "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDRSMDWrapper",                  "999",  "8",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDPMDWrapper",                   "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDPMDWrapper",                   "999",  "8",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDDMDWrapper",                   "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDDMDWrapper",                   "999",  "8",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDConnectionWrapper",            "999",  "4",    "Proxy not handling wrapper"},
   { "jtopenPX",     "JDConnectionWrapper",            "999",  "9",    "Proxy not handling wrapper"},



  };

  static Hashtable<String,String[][]>[] driverTestcases = null;    /* Table table from testcase to array of JDVariationSkip */
  static String[]    driverName = null;
  /* loads the driverTestcases hashtable from the skipInformation */

  private static Hashtable<String, String[]> testcaseInfoHash;

  private static void loadTestcaseInfoHash() { 
    testcaseInfoHash = new Hashtable<String, String[]>();
    
    String[] line = null;

    for (int i = 1; i < skipInformation.length; i++) {
      try {
        line = skipInformation[i];
        String driverString = line[0];
        String testcaseString = line[1];
        String releaseString  = line[2];
        String variationString = line[3];
        int firstVariation=0;
        int lastVariation=0;
        if (variationString.indexOf("ALL-") == 0) {
          firstVariation=1;
          variationString = variationString.substring(4);
          lastVariation = Integer.parseInt(variationString);
        } else {
          int dashIndex = variationString.indexOf("-");
          if (dashIndex > 0) {
            String firstString = variationString.substring(0,dashIndex);
            String lastString  = variationString.substring(dashIndex+1);
            firstVariation = Integer.parseInt(firstString);
            lastVariation  = Integer.parseInt(lastString);
          } else {
            firstVariation = Integer.parseInt(variationString);
            lastVariation  = firstVariation;
          }
        }
        
        String reasonString    = line[4];

        String[] variationInfo = testcaseInfoHash.get(testcaseString); 
        if (variationInfo == null) { 
          variationInfo = new String[lastVariation+1]; 
          testcaseInfoHash.put(testcaseString, variationInfo); 
        } else {
          if (lastVariation+1 > variationInfo.length) {
            String [] newVariationInfo = new String[lastVariation+1]; 
            for (int j = 0; j < variationInfo.length; j++) {
                newVariationInfo[j] = variationInfo[j]; 
            }
            variationInfo = newVariationInfo; 
            testcaseInfoHash.put(testcaseString, variationInfo); 
          }
        } 

        for (int variation = firstVariation; variation <= lastVariation; variation++) {
          if (variation > 0) {
            String thisInfo = "\n"+driverString+":"+releaseString+":"+reasonString; 
            if (variationInfo[variation] == null)  {
              variationInfo[variation] = thisInfo; 
            } else {
              variationInfo[variation] += thisInfo; 
            }
          }
        }
      } catch (Exception e) {
        System.out.flush();
        System.err.flush();
        System.out.println("WARNING:  JDVariationSkip caught exception processing " + dumpLine(line));
        e.printStackTrace();
        System.out.flush();
        System.err.flush();
      }
    } /* for i */


  }
  
  private  static void loadDriverTestcases() {
    driverTestcases = new Hashtable[ JDTestDriver.DRIVER_COUNT+JDTestDriver.SUB_DRIVER_COUNT];
    driverName      = new String[ JDTestDriver.DRIVER_COUNT+JDTestDriver.SUB_DRIVER_COUNT ];

    String[] line = null;

    for (int i = 1; i < skipInformation.length; i++) {
        try {
            line = skipInformation[i];
            String driverString = line[0];
            String testcaseString = line[1];
            String releaseString  = line[2];
            String variationString = line[3];
            int firstVariation=0;
            int lastVariation=0;
            if (variationString.indexOf("ALL-") == 0) {
                firstVariation=1;
                variationString = variationString.substring(4);
                lastVariation = Integer.parseInt(variationString);
            } else {
                int dashIndex = variationString.indexOf("-");
                if (dashIndex > 0) {
                    String firstString = variationString.substring(0,dashIndex);
                    String lastString  = variationString.substring(dashIndex+1);
                    firstVariation = Integer.parseInt(firstString);
                    lastVariation  = Integer.parseInt(lastString);
                } else {
                    firstVariation = Integer.parseInt(variationString);
                    lastVariation  = firstVariation;
                }
            }

            String reasonString    = line[4];

            int driver = JDTestDriver.getDriverFromString(driverString);
            int subDriver  = JDTestDriver.getSubDriverFromString(driverString);
        
            if (driver == JDTestDriver.DRIVER_NONE) {
                System.out.println("WARNING:  JDVariationSkip unable to interpret driver in " + dumpLine(line));
            } else {

                if (subDriver > 0 && subDriver != driver) driver = subDriver;

                if (driverName[driver] == null) driverName[driver]=driverString;
                Hashtable<String, String[][]> thisHash = driverTestcases[driver];
                if (thisHash == null) {
                    thisHash = new Hashtable<String, String[][]>();
                    driverTestcases[driver] = thisHash;
                }

                String[][] releaseArray = thisHash.get(testcaseString);

                if (releaseArray == null) {
                    releaseArray = new String[MAX_RELEASE_COUNT][];
                    thisHash.put(testcaseString, releaseArray);
                }

                String[] reasonArray = releaseArray[getReleaseIndex(releaseString)];
                if (reasonArray == null) {
                    reasonArray = new String[lastVariation+10];
                    releaseArray[getReleaseIndex(releaseString)] = reasonArray;
                }


          if (lastVariation > reasonArray.length) {
            String[] newReasonArray = new String[lastVariation+10];
            for (int j = 0; j < reasonArray.length; j++) {
              newReasonArray[j] = reasonArray[j];
            }
            reasonArray = newReasonArray;
            releaseArray[getReleaseIndex(releaseString)] = reasonArray;
          }
          for (int variation = firstVariation; variation <= lastVariation; variation++) {
              if (variation > 0) {
                  reasonArray[variation-1] = reasonString;
              }
          }

      }
      } catch (Exception e) {
        System.out.flush();
        System.err.flush();
        System.out.println("WARNING:  JDVariationSkip caught exception processing " + dumpLine(line));
        e.printStackTrace();
        System.out.flush();
        System.err.flush();
      }
    } /* for i */


  }
  private static String dumpLine(String[] line) {
    StringBuffer sb = new StringBuffer();
    sb.append("{\"");
    for (int i = 0; i < line.length; i++) {
      if (i > 0) sb.append(",\"");
      sb.append(line[i]);
      sb.append("\"");
    }
    sb.append("}");
    return sb.toString();
  }


  /**
   * Returns information about when the testcase is skipped. 
   * returns null if it is never skipped.
   */

  public static String skipInformation(String name, int variation) {
    if (testcaseInfoHash == null) {
      loadTestcaseInfoHash();
    }
    String[] testcaseInfo = testcaseInfoHash.get(name);
    if (testcaseInfo != null) { 
        if (variation < testcaseInfo.length) {
            return testcaseInfo[variation];
        } else {
            return null;
        }
    } else {
        return null; 
    } 
  }

  /**
   * Returns the reason why a variation should be skipped.
   * Returns null if the variation should be run.
   * @param driver_   : driver being used
   * @param name      : testcase name
   * @param variation : variation number
   * @return
   */
  public static String skipReason(int driver, int subDriver, int release, String name, int variation) {
      try {
          if (driverTestcases == null) {
              loadDriverTestcases();
          }
          if (debug) System.out.println("JDVariationSkip: skipReason Driver/subDriver=" + driver + "/" + subDriver ); 
          Hashtable<String, String[][]> thisHash = driverTestcases[driver];
          if (thisHash != null )  {
              String[][] releaseInformation = thisHash.get(name);
              if (releaseInformation != null) {
                  int startIndex = getReleaseIndex(release);
                  for (int i = startIndex; i < releaseInformation.length; i++) {
                      String[] reasonInformation = releaseInformation[i];
                      if (reasonInformation != null) {
                          if (variation <= reasonInformation.length) {
                              String thisReason = reasonInformation[variation-1];
                              if (thisReason != null) {
                                  if (debug) System.out.println("JDVariationSkip: Reason: "+thisReason); 
                                  return "JDVariationSkip:"+driverName[driver] +":"+thisReason;
                              } else {
                                  // Keep looking for later release 
                              }
                          } else {
                              if (debug) System.out.println("JDVariationSkip: Variation "+variation +">"+reasonInformation.length); 
                          }
                      } else {
                          if (debug) System.out.println("JDVariationSkip: No entry found for releaseIndex: "+i+" from "+startIndex); 
                      }
                  } /* for i */ 
              } else {
                  if (debug) System.out.println("JDVariationSkip: No entry found for test: "+name); 
              }
          } else {
              if (debug) System.out.println("JDVariationSkip: No entry found for driver: "+driver); 
          }
          if (subDriver > 0 && (subDriver != driver)) {
              thisHash = driverTestcases[subDriver];
              if (thisHash != null )  {
                  String[][] releaseInformation = thisHash.get(name);
                  if (releaseInformation != null) {
                      int startIndex = getReleaseIndex(release);
                      for (int i = startIndex; i < releaseInformation.length; i++) {
                          String[] reasonInformation = releaseInformation[i];
                          if (reasonInformation != null) {
                              if (variation <= reasonInformation.length) {
                                  String thisReason = reasonInformation[variation-1];
                                  if (thisReason != null) {
                                      return "JDVariationSkip:"+driverName[driver] +":"+thisReason;
                                  } else {
                                      return null;
                                  }
                              }
                          }
                      }
                  }
              }
      
          }

      } catch (Exception e) {
          System.err.flush();
          System.out.flush();
          System.out.println("------------------------------------------------");
          System.out.println("Warning:  exception in JDVariationSkip.skipReason");
          System.out.println("------------------------------------------------");         System.err.flush();
          System.out.flush();
          e.printStackTrace();
          System.err.flush();
          System.out.flush();
          System.out.println("------------------------------------------------");
          System.err.flush();
          System.out.flush();

      }
    return null;
  };


  //
  public static int MAX_RELEASE_COUNT=10;
  public static int getReleaseIndex(String  releaseString) throws Exception {
      return getReleaseIndex(Integer.parseInt(releaseString));
  }
  public static int getReleaseIndex(int release) throws Exception {
      switch (release) {
          case 540: return 0;
          case 610: return 1;
          case 710: return 2;
          case 720: return 3;
          case 730: return 4;
          case 740: return 5;
          case 750: return 6;
          case 760: return 7;
          case 999: return 8; /* should be MAX_RELEASE_COUNT-1 */ 
          default:
            throw new Exception("Invalid release "+release);
      }
  }


  //
  // instance information
  //


  public static void main(String[] args) {
    System.out.println("Collecting summary of testcases skipped");

    loadDriverTestcases();
    for (int k = 0; k < driverTestcases.length; k++) {
      Hashtable<String, String> reasonHashtable = new Hashtable<String, String>();
      Hashtable h = driverTestcases[k];
      if (h != null) {
        System.out.println("Skipped reasons for driver " + driverName[k]);

        /* The keys are the test names */
        Enumeration keys = h.keys();
        while (keys.hasMoreElements()) {

          String[][] releaseInformation = (String[][]) h
              .get(keys.nextElement());
          if (releaseInformation != null) {
            int startIndex = 0;
            for (int i = startIndex; i < releaseInformation.length; i++) {
              String[] reasonInformation = releaseInformation[i];
              if (reasonInformation != null) {
              for (int j = 0; j < reasonInformation.length; j++) {
                    String thisReason = reasonInformation[j];
                    if (thisReason != null) {
                      thisReason = thisReason.trim();
                      reasonHashtable.put(thisReason, thisReason);
                    } /* thisReason != null */
                }/* for j */
              } /* reasonInformation != null */
            }/* for i */

          }/* releaseInformation != null */

        } /* while keys.hasMoreElements */
        Set<String> keySet = reasonHashtable.keySet();
        Object[] elements = keySet.toArray();
        Arrays.sort(elements);
        System.out.println("Reason count = "+elements.length);
        System.out.println("-------------------------------------");
        for (int i = 0; i < elements.length; i++) {
            System.out.println(elements[i]);
        }
        System.out.println("-------------------------------------");

      } /* h != null */
    }

  }


}

