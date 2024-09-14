///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvPSA89229.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase ConvPSA89229. Verify fix for PSA89229. This is to make sure that:
 <ol>
 <li>Fault-tolerant conversion occurs when enabled. Since this fix was put into a
 mod3 PTF, and the conversion code changed in mod4, this testcase is really just
 testing the methods added to CharConverter, because in the new converter scheme
 fault-tolerant conversion always occurs.
 </ol>
 **/
public class ConvPSA89229 extends Testcase implements Runnable
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvPSA89229";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
        systemObject_.disconnectAllServices();
    }

    /**
     Verify CharConverter.setFaultTolerantConversion() and isFaultTolerantConversion().
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var001()
    {
        try
        {
            if (CharConverter.isFaultTolerantConversion())
            {
                failed("Default should be false.");
                return;
            }
            CharConverter.setFaultTolerantConversion(true);
            if (!CharConverter.isFaultTolerantConversion())
            {
                failed("Set fault-tolerance failed.");
                return;
            }
            CharConverter.setFaultTolerantConversion(false);
            if (CharConverter.isFaultTolerantConversion())
            {
                failed("2nd set of fault-tolerance failed.");
                return;
            }
            succeeded();
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }

    /**
     Verify CharConverter's fault-tolerant conversion when set to true for a mixed-byte CCSID.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var002()
    {
        try
        {
	    boolean passed = true;
	    StringBuffer sb = new StringBuffer(); 
            byte[] normalMixedByteEbcdic = { (byte)0x40, (byte)0x0E, (byte)0x45, (byte)0x5C, (byte)0x45, (byte)0x5C, (byte)0x0F };
            byte[] faultyMixedByteEbcdic = { (byte)0x40, (byte)0x0E, (byte)0x45, (byte)0x5C, (byte)0x45, (byte)0x5C, (byte)0x45 };

            CharConverter cc = new CharConverter(939); // mixed byte table
            CharConverter.setFaultTolerantConversion(true);

            String s1 = cc.byteArrayToString(normalMixedByteEbcdic);
            String s2 = cc.byteArrayToString(faultyMixedByteEbcdic);
            String s3 = cc.byteArrayToString(faultyMixedByteEbcdic);
            String s4 = cc.byteArrayToString(normalMixedByteEbcdic);
            String s5 = cc.byteArrayToString(normalMixedByteEbcdic);
            String s6 = cc.byteArrayToString(faultyMixedByteEbcdic);
	    if (!s1.equals(s4)) {
		sb.append("!s1[UX'"+showStringAsHex(s1)+"'].equals(s4[UX'"+showStringAsHex(s4)+"'])\n"); 
		passed = false; 
	    }
	    if (!s1.equals(s5)) {
		sb.append("!s1.equals(s5)\n"); 
		passed = false; 

	    }
	    if (!s2.equals(s3)) {
		sb.append("!s2.equals(s3)\n"); 
		passed = false; 
	    }
	    if (!s2.equals(s6)) {
		sb.append("!s2.equals(s6)\n"); 
		passed = false; 
	    }
	    if (!s1.equals(s2)) {
		sb.append("!s1[UX'"+showStringAsHex(s1)+"'].equals(s2['"+showStringAsHex(s2)+"'])\n"); 
		passed = false; 
	    }

	    assertCondition(passed, sb); 
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            failed(e, "Array index out of bounds occurred; conversion not fault-tolerant.");
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }

    /**
     Verify CharConverter's fault-tolerant conversion when set to false for a mixed-byte CCSID.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var003()
    {
        try
        {
            byte[] normalMixedByteEbcdic = { (byte)0x40, (byte)0x0E, (byte)0x45, (byte)0x5C, (byte)0x0F, (byte)0x40, (byte)0x40 };
            byte[] faultyMixedByteEbcdic = { (byte)0x40, (byte)0x0E, (byte)0x45, (byte)0x5C, (byte)0x45, (byte)0x5C, (byte)0x45 };
            byte[] expectedMixedByteEbcdic = { (byte)0x40, (byte)0x0E, (byte)0x45, (byte)0x5C, (byte)0x45, (byte)0x5c, (byte)0xFE, (byte) 0xFE, (byte) 0x0F };

            CharConverter cc = new CharConverter(939); // mixed byte table
            CharConverter.setFaultTolerantConversion(false);

            String s1 = cc.byteArrayToString(normalMixedByteEbcdic);
            try
            {
                String s2 = cc.byteArrayToString(faultyMixedByteEbcdic);
                String s3 = cc.byteArrayToString(expectedMixedByteEbcdic);
                // Rather than fail with exception, we now use a replacement character 
                
                assertCondition(s2.equals(s3), "Testcase updated 9/11/2108 for expected equality s2="+s2+" s3="+s3);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                succeeded(); // mod4
            }
            catch (ExtendedIllegalArgumentException e)
            {
                if (e.getReturnCode() == ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID)
                {
                    succeeded(); // mod3
                }
                else
                {
                    failed(e, "Incorrect exception info.");
                }
            }
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }

    /**
     Verify CharConverter's fault-tolerant conversion when set to true for a double-byte CCSID.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var004()
    {
        try
        {
            byte[] normalDoubleByteEbcdic = { (byte)0x9E, (byte)0xB9, (byte)0x9E, (byte)0xBA, (byte)0x44, (byte)0x4C };
            byte[] faultyDoubleByteEbcdic = { (byte)0x9E, (byte)0xB9, (byte)0x9E, (byte)0xBA, (byte)0x44, (byte)0x4C, (byte)0x9E };

            CharConverter cc = new CharConverter(61952); // double byte table
            CharConverter.setFaultTolerantConversion(true);

            String s1 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s2 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            String s3 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            String s4 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s5 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s6 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            if (s1.equals(s4) && s1.equals(s5) && s2.equals(s3) && s2.equals(s6) && s1.equals(s2))
            {
                succeeded();
            }
            else
            {
                failed("Strings not equal.");
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            failed(e, "Array index out of bounds occurred; conversion not fault-tolerant.");
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }

    /**
     Verify CharConverter's fault-tolerant conversion when set to false for a double-byte CCSID.
     Expected results:
     <ul compact>
     <li>The variation should succeed.
     </ul>
     **/
    public void Var005()
    {
        try
        {
            byte[] normalDoubleByteEbcdic = { (byte)0x9E, (byte)0xB9, (byte)0x9E, (byte)0xBA, (byte)0x44, (byte)0x4C };
            byte[] faultyDoubleByteEbcdic = { (byte)0x9E, (byte)0xB9, (byte)0x9E, (byte)0xBA, (byte)0x44, (byte)0x4C, (byte)0x9E };

            CharConverter cc = new CharConverter(61952); // double byte table
            CharConverter.setFaultTolerantConversion(false);

            // Note that for double-byte ccsids in mod4, faulty conversion works the same as normal conversion,
            // so there's no exception to check for when fault tolerance is disabled.

            String s1 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s2 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            String s3 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            String s4 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s5 = cc.byteArrayToString(normalDoubleByteEbcdic);
            String s6 = cc.byteArrayToString(faultyDoubleByteEbcdic);
            if (s1.equals(s4) && s1.equals(s5) && s2.equals(s3) && s2.equals(s6) && s1.equals(s2))
            {
                succeeded();
            }
            else
            {
                failed("Strings not equal.");
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            failed(e, "Array index out of bounds occurred; conversion not fault-tolerant.");
        }
        catch (Throwable t)
        {
            failed(t, "Unexpected exception.");
        }
    }
}
