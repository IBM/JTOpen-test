///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDParmHelper.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////
//
//
//
//
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    JDParmHelper.java
//
// Classes:      JDParmHelper
//
////////////////////////////////////////////////////////////////////////
//
//
// 
//
////////////////////////////////////////////////////////////////////////

package test;

import java.sql.*;

/**
This function contains various helper classes used by the JDParm tests.
**/
public class JDParmHelper {

    public static boolean verifyString(String column, 
                                       String value,
                                       Connection connection) 
    {
	return verifyString(column,value,connection,JDParmTest.COLLECTION+".strings"); 
    }

    public static boolean verifyString(String column, 
                                       String value,
                                       Connection connection, String tablename) 
    {
	try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select " + column + " from " + tablename); // @KKASHIR: added space before the word from to avoid exceptions
            rs.next();
            String test = rs.getString(1);

            if (value == null) {
                if (test == null) {
                    return true;
                } else {
		    System.out.println("verifyString returning false:  expected null but value was \""+test+"\""); 
                    return false;
                }
	    } else {
		if (value.equals(test)) {
		    return true;
		} else {
		    if (test == null) {
			System.out.println("verifyString returning false:  expected nonNull but value was null"); 
		    } else {
			String printValue = value;
			String printTest = test;
			if (printValue.length() > 1000) {
			    printValue = "L="+value.length()+" : " +value.substring(0,100)+"..."; 
			} 
			if (printTest.length() > 1000) {
			    printTest = "L="+test.length()+" : " +test.substring(0,100)+"..."; 
			} 
			
			
			System.out.println("verifyString returning false:  expected \""+printValue+"\" but value was \""+printTest+"\""); 
			System.out.print("expected :");
			int i; 
			for (i = 0; i < value.length() && i < 1000; i++) {
			    System.out.print(" 0x"+Integer.toHexString(value.charAt(i)));
			}
			if ( i < value.length()) System.out.print(" ...");

			System.out.println(); 
			System.out.print("value    :");
			for (i = 0; i < test.length() && i < 1000; i++) {
			    System.out.print(" 0x"+Integer.toHexString(test.charAt(i)));
			}
			if ( i < test.length()) System.out.print(" ..."); 
			System.out.println();
			System.out.println("Expected length = "+value.length()+" retrieved length = "+test.length()); 
			String firstDifferenceFound = null; 
			for (i = 0; i < value.length() && i < test.length() ; i++ ) {
				if (value.charAt(i) == test.charAt(i)) {
				    if (i < 1000) { 
					System.out.print("     ");
				    }
				} else {
				    if (i < 1000) { 
					System.out.print(" XXXX");
				    }
				    if (firstDifferenceFound == null) {
					firstDifferenceFound = "First difference found at index "+i; 
				    } 
				}
			} 
			System.out.println();
			if (firstDifferenceFound != null) {
			    System.out.println(firstDifferenceFound); 
			} else {
			    System.out.println("No difference found"); 
			} 

			
		    }
                    return false;
		}
            }
	} catch (SQLException e) {
            e.printStackTrace();
	    return false;
        }
    }

    public static boolean verifyGraphic(String column, 
                                        String compare, 
                                        int    length, 
                                        Connection connection)
    {
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("select " + column + " from "+JDParmTest.COLLECTION+".strings");
            rs.next();
            String actual = rs.getString(1);


            if (compare == null) {
                if (actual == null) 
                    return true;
                 else 
                    return false;
            } else {
                // Create a partial value from the actual value that is the length of the 
                // compare value.
                String partialValue = actual.substring(0, compare.length());
                if ((compare.equals(partialValue)) && (actual.length() == length))
                    return true;
                else 
                    return false;
            }
        } catch (SQLException e) {
            return false;
            //System.out.println("Error during testcase verification");
            //e.printStackTrace();
        }
    }


    public static void purgeStringsTable(Connection connection) {
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("delete from "+JDParmTest.COLLECTION+".strings");
            s.close();
        } catch (SQLException e) {
            System.out.println("Table purge function filed.");
        }
    }

    public static void purgeStringsTable(Connection connection, String tablename) {
        try {
            Statement s = connection.createStatement();
            s.executeUpdate("delete from "+ tablename);
            s.close();
        } catch (SQLException e) {
            System.out.println("Table purge function filed.");
        }
    }


    public static void handleDropException(SQLException e) {
	String message = e.toString();
	if (message.indexOf("not found") > 0 &&
	    message.indexOf("type *FILE") > 0) {
	} else {
	    System.out.println("Warning .. could not delete strings");
	    e.printStackTrace();

	}

    }


}
