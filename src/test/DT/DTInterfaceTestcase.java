///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DTInterfaceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.DT;

import java.math.BigDecimal;

import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase DTInterfaceTestcase.
 **/
public class DTInterfaceTestcase extends Testcase
{
  // New method was added in JTOpen 6.3: AS400DataType.getJavaType()
  private static boolean isGetJavaTypeMethodDefined_ = true;
  static {
    try {
      isGetJavaTypeMethodDefined_ = TOOLBOX_PACKAGE.isCompatibleWith("6.1.0.6");
      // V6R1M0 PTF 6 (JTOpen 6.3)
    }
    catch (NumberFormatException e) {
      System.out.println("DTInterfaceTestcase is unable to determine version of Toolbox package.  Error message is: " + e.getMessage());
      System.out.println("Suggestion: Remove local Toolbox .class files from classpath.");
      isGetJavaTypeMethodDefined_ = true; // assume we're using a current Toolbox jar
    }
  }


    public void Var001()
    {
        try
        {
            AS400Array arr = new AS400Array();
            if (arr.getInstanceType() == AS400DataType.TYPE_ARRAY)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var002()
    {
        try
        {
            AS400Bin2 arr = new AS400Bin2();
            if (arr.getInstanceType() == AS400DataType.TYPE_BIN2)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var003()
    {
        try
        {
            AS400Bin4 arr = new AS400Bin4();
            if (arr.getInstanceType() == AS400DataType.TYPE_BIN4)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var004()
    {
        try
        {
            AS400Bin8 arr = new AS400Bin8();
            if (arr.getInstanceType() == AS400DataType.TYPE_BIN8)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var005()
    {
        try
        {
            AS400ByteArray arr = new AS400ByteArray(1);
            if (arr.getInstanceType() == AS400DataType.TYPE_BYTE_ARRAY)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var006()
    {
        try
        {
            AS400Float4 arr = new AS400Float4();
            if (arr.getInstanceType() == AS400DataType.TYPE_FLOAT4)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var007()
    {
        try
        {
            AS400Float8 arr = new AS400Float8();
            if (arr.getInstanceType() == AS400DataType.TYPE_FLOAT8)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var008()
    {
        try
        {
            AS400PackedDecimal arr = new AS400PackedDecimal(2,1);
            if (arr.getInstanceType() == AS400DataType.TYPE_PACKED)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var009()
    {
        try
        {
            AS400Structure arr = new AS400Structure();
            if (arr.getInstanceType() == AS400DataType.TYPE_STRUCTURE)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var010()
    {
        try
        {
            AS400Text arr = new AS400Text(1);
            if (arr.getInstanceType() == AS400DataType.TYPE_TEXT)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var011()
    {
        try
        {
            AS400UnsignedBin2 arr = new AS400UnsignedBin2();
            if (arr.getInstanceType() == AS400DataType.TYPE_UBIN2)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var012()
    {
        try
        {
            AS400UnsignedBin4 arr = new AS400UnsignedBin4();
            if (arr.getInstanceType() == AS400DataType.TYPE_UBIN4)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }


    public void Var013()
    {
        try
        {
            AS400ZonedDecimal arr = new AS400ZonedDecimal(2,1);
            if (arr.getInstanceType() == AS400DataType.TYPE_ZONED)
            {
                succeeded();
            }
            else
            {
                failed("Wrong type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var014()
    {
        try
        {
        	AS400Array arr = new AS400Array();

            if (arr.getJavaType() == Object[].class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
  
    
    public void Var015()
    {
        try
        {
        	AS400Bin2 arr = new AS400Bin2();
            
            if (arr.getJavaType() == Short.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var016()
    {
        try
        {
        	AS400Bin4 arr = new AS400Bin4();
            
            if (arr.getJavaType() == Integer.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var017()
    {
        try
        {
        	AS400Bin8 arr = new AS400Bin8();	
            
            if (arr.getJavaType() == Long.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var018()
    {
        try
        {
        	AS400ByteArray arr = new AS400ByteArray(1);

            if (arr.getJavaType() == byte[].class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
 
    public void Var019()
    {
        try
        {
        	
        	AS400DecFloat arr = new AS400DecFloat(16);
            
            if (arr.getJavaType() == BigDecimal.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var020()
    {
        try
        {
        	AS400Float4 arr = new AS400Float4();

            if (arr.getJavaType() == Float.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var021()
    {
        try
        {
        	AS400Float8 arr = new AS400Float8();
            
            if (arr.getJavaType() == Double.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var022()
    {
        try
        {
        	AS400PackedDecimal arr = new AS400PackedDecimal(1,1);
            
            if (arr.getJavaType() == BigDecimal.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var023()
    {
        try
        {
        	AS400Structure arr = new AS400Structure();
            
            if (arr.getJavaType() == Object[].class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var024()
    {
        try
        {
        	AS400Text arr = new AS400Text(1);
            
            if (arr.getJavaType() == String.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
     
    public void Var025()
    {
        try
        {
        	AS400UnsignedBin2 arr = new AS400UnsignedBin2();

            if (arr.getJavaType() == Integer.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var026()
    {
        try
        {
        	AS400UnsignedBin4 arr = new AS400UnsignedBin4();
            
            if (arr.getJavaType() == Long.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
    
    
    public void Var027()
    {
        try
        {
        	AS400ZonedDecimal arr = new AS400ZonedDecimal(1,1);
            
            if (arr.getJavaType() == BigDecimal.class)
            {
                succeeded();
            }
            else
            {
                failed("Wrong class type returned.");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }
}
