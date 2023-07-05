///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvPSE04743.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.*;
import java.sql.*;

/**
 Testcase ConvPSE04743. Verify fix for PSE04743. This is to make sure that:
 <ol>
 <li>SQLChar correctly converts Unicode wide spaces 0x3000 for double-byte CCSIDs.
 </ol>
 **/
public class ConvPSE04743 extends Testcase implements Runnable
{
    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        systemObject_.disconnectAllServices();
    }

    /**
     Verify SQLChar correctly converts Unicode wide spaces.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var001()
    {
        try
        {
            AS400JDBCDriver d = new AS400JDBCDriver();
            Connection c = d.connect(pwrSys_);
            Statement s = c.createStatement();
            CommandCall cc = new CommandCall(pwrSys_);
	    deleteLibrary(cc,"TEST04743");
            if (!cc.run("CRTLIB TEST04743"))
            {
                failed("Can't create TEST04743 library: " + cc.getMessageList()[0]);
                return;
            }
            s.execute("CREATE TABLE TEST04743.DBCSFILE (C1 GRAPHIC(4) CCSID 300 NOT NULL WITH DEFAULT)");
            PreparedStatement ps = c.prepareStatement("INSERT INTO TEST04743.DBCSFILE VALUES(?)");
            ps.setString(1, "\u6F22\u5B57");
            ps.execute();
            ResultSet rs = s.executeQuery("SELECT * FROM TEST04743.DBCSFILE");
            rs.next();
            String dataString = rs.getString(1);
            char[] data = dataString.toCharArray();
            if (data.length == 4)
            {
                if (data[0] == '\u6F22' && data[1] == '\u5B57')
                {
                    if (data[2] == '\u3000' && data[3] == '\u3000')
                    {
                        succeeded();
                    }
                    else
                    {
                        failed("Space characters not correct: " + Integer.toHexString(0x00FFFF & data[2]));
                    }
                }
                else
                {
                    failed("Conversion not correct: " + Integer.toHexString(0x00FFFF & data[0]));
                }
            }
            else
            {
                failed("Length not correct: " + data.length);
            }
            // Cleanup.
            try
            {
                rs.close();
                ps.close();
		deleteLibrary(cc,"TEST04743");
                c.close();
            }
            catch (Exception e)
            {
            }
        }
        catch (Exception t)
        {
            failed(t, "Unexpected exception.");
        }
    }
}
