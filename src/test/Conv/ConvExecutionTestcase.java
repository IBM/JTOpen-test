///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ConvExecutionTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Conv;

import java.util.Locale;
import test.Testcase;
import com.ibm.as400.access.*; 

/**
 Testcase ConvExecutionTestcase.
 **/
public class ConvExecutionTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "ConvExecutionTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.ConvTest.main(newArgs); 
   }
    /**
     Call getBestGuessAS400Ccsid(): Locale in table.
     **/
    public void Var001()
    {

        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("ar", ""));
            int ret1 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret1 != 420)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("fr", "CH"));
            int ret2 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret2 != 500)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Call getBestGuessAS400Ccsid(): Less specific Locale in table.
     **/
    public void Var002()
    {
        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("bg", "XX", "SOMETHING"));
            int ret1 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret1 != 1025)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("nl", "BE", "SOMETHING"));
            int ret3 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret3 != 500)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Call getBestGuessAS400Ccsid(): Locale not in table.
     **/
    public void Var003()
    {
        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("ww", "XX", "SOMETHING"));
            int ret1 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret1 != 37)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("ww", "XX"));
            int ret2 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret2 != 37)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("ww", ""));
            int ret3 = ExecutionEnvironment.getBestGuessAS400Ccsid();
            if (ret3 != 37)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Call getCcsid.
     **/
    public void Var004()
    {
        try
        {
            int ret = ExecutionEnvironment.getCcsid();
            if (ret == 13488)
            {
                succeeded();
            }
            else
            {
                failed("Unexpected value");
            }
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
    }

    /**
     Call getNlv(): Locale in table.
     **/
    public void Var005()
    {
        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("sh", ""));
            String ret1 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret1.compareTo("2912") != 0)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("zh", "TW"));
            String ret2 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret2.compareTo("2987") != 0)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception.");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Call getNlv(): Less specific Locale in table.
     **/
    public void Var006()
    {
        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("ko", "XX", "SOMETHING"));
            String ret1 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret1.compareTo("2986") != 0)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("hu", "XX"));
            String ret2 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret2.compareTo("2976") != 0)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("pt", "PT", "SOMETHING"));
            String ret3 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret3.compareTo("2922") != 0)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }

    /**
     Call getNlv(): Locale not in table.
     **/
    public void Var007()
    {
        Locale saveDefault = Locale.getDefault();
        try
        {
            boolean valid = true;

            Locale.setDefault(new Locale("ww", "XX", "SOMETHING"));
            String ret1 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret1.compareTo("2924") != 0)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("ww", "XX"));
            String ret2 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret2.compareTo("2924") != 0)
            {
                valid = false;
            }

            Locale.setDefault(new Locale("ww", ""));
            String ret3 = ExecutionEnvironment.getNlv(Locale.getDefault());
            if (ret3.compareTo("2924") != 0)
            {
                valid = false;
            }

            assertCondition(valid);
        }
        catch (Exception e)
        {
            failed(e, "Incorrect exception");
        }
        finally
        {
            Locale.setDefault(saveDefault);
        }
    }
}
