///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PSJdbcDriversSampleDriver.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test.PS;

import com.ibm.as400.access.AS400JDBCDriver;
import java.sql.DriverManager;
import java.sql.SQLException;



/**
The PSJdbcDriversSampleDriver class is a sample
JDBC driver for use in testing the PS.
**/
    public class PSJdbcDriversSampleDriver extends AS400JDBCDriver
    {
        static {
            try {
                DriverManager.registerDriver (new PSJdbcDriversSampleDriver ());
            }
            catch (SQLException e) {
                // Ignore.
            }
        }
    }
        

