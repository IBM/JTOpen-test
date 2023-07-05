///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  SysvalGetSetTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;


import java.io.FileOutputStream;
import java.util.Vector;
import java.util.ResourceBundle;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.SystemValue;
import com.ibm.as400.access.SystemValueList;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.ExtendedIllegalStateException;
import com.ibm.as400.access.RequestNotSupportedException;

/**
 * Testcase SysvalGetSetTestcase.
 *
 * Test variations for the methods:
 * <ul>
 * <li>SystemValue::clear()
 * <li>SystemValue::getDescription()
 * <li>SystemValue::getGroup()
 * <li>SystemValue::getName()
 * <li>SystemValue::getRelease()
 * <li>SystemValue::getSize()
 * <li>SystemValue::getSystem()
 * <li>SystemValue::getType()
 * <li>SystemValue::getValue()
 * <li>SystemValue::isReadOnly()
 * <li>SystemValue::setName(String)
 * <li>SystemValue::setSystem(AS400)
 * <li>SystemValue::setValue(Object)
 * </ul>
 **/
public class SysvalGetSetTestcase extends Testcase
{
    /**
     * Runs the variations requested.
     **/
    public void run()
    {
        boolean allVariations = (variationsToRun_.size() == 0);

        if ((allVariations || variationsToRun_.contains("1")) && runMode_ != ATTENDED)
        {
            setVariation(1);
            Var001();
        }

        if ((allVariations || variationsToRun_.contains("2")) && runMode_ != ATTENDED)
        {
            setVariation(2);
            Var002();
        }

        if ((allVariations || variationsToRun_.contains("3")) && runMode_ != ATTENDED)
        {
            setVariation(3);
            Var003();
        }

        if ((allVariations || variationsToRun_.contains("4")) && runMode_ != ATTENDED)
        {
            setVariation(4);
            Var004();
        }

        if ((allVariations || variationsToRun_.contains("5")) && runMode_ != ATTENDED)
        {
            setVariation(5);
            Var005();
        }

        if ((allVariations || variationsToRun_.contains("6")) && runMode_ != ATTENDED)
        {
            setVariation(6);
            Var006();
        }

        if ((allVariations || variationsToRun_.contains("7")) && runMode_ != ATTENDED)
        {
            setVariation(7);
            Var007();
        }

        if ((allVariations || variationsToRun_.contains("8")) && runMode_ != ATTENDED)
        {
            setVariation(8);
            Var008();
        }

        if ((allVariations || variationsToRun_.contains("9")) && runMode_ != ATTENDED)
        {
            setVariation(9);
            Var009();
        }

        if ((allVariations || variationsToRun_.contains("10")) && runMode_ != ATTENDED)
        {
            setVariation(10);
            Var010();
        }

        if ((allVariations || variationsToRun_.contains("11")) && runMode_ != ATTENDED)
        {
            setVariation(11);
            Var011();
        }

        if ((allVariations || variationsToRun_.contains("12")) && runMode_ != ATTENDED)
        {
            setVariation(12);
            Var012();
        }

        if ((allVariations || variationsToRun_.contains("13")) && runMode_ != ATTENDED)
        {
            setVariation(13);
            Var013();
        }

        if ((allVariations || variationsToRun_.contains("14")) && runMode_ != ATTENDED)
        {
            setVariation(14);
            Var014();
        }

        if ((allVariations || variationsToRun_.contains("15")) && runMode_ != ATTENDED)
        {
            setVariation(15);
            Var015();
        }
        if ((allVariations || variationsToRun_.contains("16")) && runMode_ != ATTENDED)
        {
            setVariation(16);
            Var016();
        }

        if ((allVariations || variationsToRun_.contains("17")) && runMode_ != ATTENDED)
        {
            setVariation(17);
            Var017();
        }

        if ((allVariations || variationsToRun_.contains("18")) && runMode_ != ATTENDED)
        {
            setVariation(18);
            Var018();
        }

        if ((allVariations || variationsToRun_.contains("19")) && runMode_ != ATTENDED)
        {
            setVariation(19);
            Var019();
        }

        if ((allVariations || variationsToRun_.contains("20")) && runMode_ != ATTENDED)
        {
            setVariation(20);
            Var020();
        }

        if ((allVariations || variationsToRun_.contains("21")) && runMode_ != ATTENDED)
        {
            setVariation(21);
            Var021();
        }

        if ((allVariations || variationsToRun_.contains("22")) && runMode_ != ATTENDED)
        {
            setVariation(22);
            Var022();
        }

        if ((allVariations || variationsToRun_.contains("23")) && runMode_ != ATTENDED)
        {
            setVariation(23);
            Var023();
        }

        if ((allVariations || variationsToRun_.contains("24")) && runMode_ != ATTENDED)
        {
            setVariation(24);
            Var024();
        }

        if ((allVariations || variationsToRun_.contains("25")) && runMode_ != ATTENDED)
        {
            setVariation(25);
            Var025();
        }

        if ((allVariations || variationsToRun_.contains("26")) && runMode_ != ATTENDED)
        {
            setVariation(26);
            Var026();
        }

        if ((allVariations || variationsToRun_.contains("27")) && runMode_ != ATTENDED)
        {
            setVariation(27);
            Var027();
        }

        if ((allVariations || variationsToRun_.contains("28")) && runMode_ != ATTENDED)
        {
            setVariation(28);
            Var028();
        }

        if ((allVariations || variationsToRun_.contains("29")) && runMode_ != ATTENDED)
        {
            setVariation(29);
            Var029();
        }

        if ((allVariations || variationsToRun_.contains("30")) && runMode_ != ATTENDED)
        {
            setVariation(30);
            Var030();
        }

        if ((allVariations || variationsToRun_.contains("31")) && runMode_ != ATTENDED)
        {
            setVariation(31);
            Var031();
        }

        if ((allVariations || variationsToRun_.contains("32")) && runMode_ != ATTENDED)
        {
            setVariation(32);
            Var032();
        }

        if ((allVariations || variationsToRun_.contains("33")) && runMode_ != ATTENDED)
        {
            setVariation(33);
            Var033();
        }

        if ((allVariations || variationsToRun_.contains("34")) && runMode_ != ATTENDED)
        {
            setVariation(34);
            Var034();
        }

        if ((allVariations || variationsToRun_.contains("35")) && runMode_ != ATTENDED)
        {
            setVariation(35);
            Var035();
        }

        if ((allVariations || variationsToRun_.contains("36")) && runMode_ != ATTENDED)
        {
            setVariation(36);
            Var036();
        }

        if ((allVariations || variationsToRun_.contains("37")) && runMode_ != ATTENDED)
        {
            setVariation(37);
            Var037();
        }

        if ((allVariations || variationsToRun_.contains("38")) && runMode_ != ATTENDED)
        {
            setVariation(38);
            Var038();
        }

        if ((allVariations || variationsToRun_.contains("39")) && runMode_ != ATTENDED)
        {
            setVariation(39);
            Var039();
        }

        if ((allVariations || variationsToRun_.contains("40")) && runMode_ != ATTENDED)
        {
            setVariation(40);
            Var040();
        }
        if ((allVariations || variationsToRun_.contains("41")) && runMode_ != ATTENDED)
        {
            setVariation(41);
            Var041();
        }

        if ((allVariations || variationsToRun_.contains("42")) && runMode_ != ATTENDED)
        {
            setVariation(42);
            Var042();
        }

        if ((allVariations || variationsToRun_.contains("43")) && runMode_ != ATTENDED)
        {
            setVariation(43);
            Var043();
        }

        if ((allVariations || variationsToRun_.contains("44")) && runMode_ != ATTENDED)
        {
            setVariation(44);
            Var044();
        }

        if ((allVariations || variationsToRun_.contains("45")) && runMode_ != ATTENDED)
        {
            setVariation(45);
            Var045();
        }

        if ((allVariations || variationsToRun_.contains("46")) && runMode_ != ATTENDED)
        {
            setVariation(46);
            Var046();
        }

        if ((allVariations || variationsToRun_.contains("47")) && runMode_ != ATTENDED)
        {
            setVariation(47);
            Var047();
        }

        if ((allVariations || variationsToRun_.contains("48")) && runMode_ != ATTENDED)
        {
            setVariation(48);
            Var048();
        }

        if ((allVariations || variationsToRun_.contains("49")) && runMode_ != ATTENDED)
        {
            setVariation(49);
            Var049();
        }

        if ((allVariations || variationsToRun_.contains("50")) && runMode_ != ATTENDED)
        {
            setVariation(50);
            Var050();
        }

        if ((allVariations || variationsToRun_.contains("51")) && runMode_ != ATTENDED)
        {
            setVariation(51);
            Var051();
        }

        if ((allVariations || variationsToRun_.contains("52")) && runMode_ != ATTENDED)
        {
            setVariation(52);
            Var052();
        }

        if ((allVariations || variationsToRun_.contains("53")) && runMode_ != ATTENDED)
        {
            setVariation(53);
            Var053();
        }

        if ((allVariations || variationsToRun_.contains("54")) && runMode_ != ATTENDED)
        {
            setVariation(54);
            Var054();
        }

        if ((allVariations || variationsToRun_.contains("55")) && runMode_ != ATTENDED)
        {
            setVariation(55);
            Var055();
        }

        if ((allVariations || variationsToRun_.contains("56")) && runMode_ != ATTENDED)
        {
            setVariation(56);
            Var056();
        }
    }


    /**
     * Test valid usage of SystemValue::clear().
     * Try to call clear() on a system value that was created
     * using the default constructor.
     **/
    public void Var001()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.clear();
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Test valid usage of SystemValue::clear().
     * Try to call clear() on a system value that was created
     * with a system and name specified.
     **/
    public void Var002()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        sv.clear();
        succeeded();
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Test valid usage of SystemValue::clear().
     * Verify that the system value's cache is cleared
     * when using getValue().
     **/
    public void Var003()
    {
      String failMsg = "";
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qyear");
        Object obj = sv.getValue();
        // At this point, we should have the data cached.
        Object obj2 = sv.getValue();
        if (!obj.equals(obj2))
        {
          failMsg += "Initial objects are not equal.";
        }
        else
        {
          SystemValue sv2 = new SystemValue(pwrSys_, "qyear");
          sv2.setValue("07");
          // We should still be caching the original value
          Object obj3 = sv.getValue();
          if (!obj3.equals(obj))
          {
            failMsg += "Failed to cache value.";
          }
          else
          {
            // Now clear the cache to see if we retrieve the real value
            sv.clear();
            Object obj4 = sv.getValue();
            if (!((String)obj4).trim().equals("07"))
            {
              failMsg += "Failed to retrieve real value.";
            }
          }
        }
        // Reset value back to original
        sv.setValue(obj);
        SystemValue newsv = new SystemValue(pwrSys_, "QYEAR");
        Object check = newsv.getValue();
        if (!((String)check).trim().equals(((String)obj).trim()))
        {
          failMsg += " Failed to reset value.";
        }
      }
      catch(Exception e)
      {
        failMsg += " Unexpected exception.";
        failed(e, failMsg);
        return;
      }
      if (failMsg.length() > 0)
        failed(failMsg);
      else
        succeeded();
    }


    /**
     * Verify invalid usage of SystemValue::getDescription().
     * Try to get the description of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var004()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getDescription();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getDescription().
     * Try to get the description of a system value which has the
     * name set on the constructor.
     **/
    public void Var005()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "QDAY");
        ResourceBundle res = ResourceBundle.getBundle("com.ibm.as400.access.SVMRI");
        if (!sv.getDescription().equals(((String)res.getString("QDAY_DES")).trim()))
        {
          failed("Incorrect description.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getDescription().
     * Try to get the description of a system value which has the
     * name set using setName().
     **/
    public void Var006()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("QDAY");
        ResourceBundle res = ResourceBundle.getBundle("com.ibm.as400.access.SVMRI");
        if (!sv.getDescription().equals(((String)res.getString("QDAY_DES")).trim()))
        {
          failed("Incorrect description.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getDescription().
     * Try to get the description of a system value which has the
     * name set using setName() after the name was already set.
     **/
    public void Var007()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "QCHRID");
        sv.setName("QACGLVL");
        ResourceBundle res = ResourceBundle.getBundle("com.ibm.as400.access.SVMRI");
        if (!sv.getDescription().equals(((String)res.getString("QACGLVL_DES")).trim()))
        {
          failed("Incorrect description.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getGroup().
     * Try to get the group of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var008()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getGroup();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getGroup().
     * Try to get the group of a system value which has the
     * name set on the constructor.
     **/
    public void Var009()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "QDAY");
        if (sv.getGroup() != SystemValueList.GROUP_DATTIM)
        {
          failed("Incorrect group.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getGroup().
     * Try to get the group of a system value which has the
     * name set using setName().
     **/
    public void Var010()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("QDAY");
        if (sv.getGroup() != SystemValueList.GROUP_DATTIM)
        {
          failed("Incorrect group.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getGroup().
     * Try to get the group of a system value which has the
     * name set using setName() after the name was already set.
     **/
    public void Var011()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "QCHRID");
        sv.setName("QACGLVL");
        if (sv.getGroup() != SystemValueList.GROUP_MSG)
        {
          failed("Incorrect group.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getName().
     * Try to get the name of a system value created using
     * the default constructor.
     **/
    public void Var012()
    {
      try
      {
        SystemValue sv = new SystemValue();
        if (sv.getName() != null)
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getName().
     * Try to get the name of a system value which has the
     * name set on the constructor.
     **/
    public void Var013()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (!sv.getName().equals("QDAY"))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getName().
     * Try to get the name of a system value which has the
     * name set using setName().
     **/
    public void Var014()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("Qday");
        if (!sv.getName().equals("QDAY"))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getName().
     * Try to get the name of a system value which has the
     * name set using setName() after the name was already set.
     **/
    public void Var015()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("Qacglvl");
        if (!sv.getName().equals("QACGLVL"))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getRelease().
     * Try to get the release of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var016()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getRelease();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getRelease().
     * Try to get the release of a system value which has the
     * release set on the constructor.
     **/
    public void Var017()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (sv.getRelease() != AS400.generateVRM(4,2,0))
        {
          failed("Incorrect release.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getRelease().
     * Try to get the release of a system value which has the
     * release set using setName().
     **/
    public void Var018()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("Qday");
        if (sv.getRelease() != AS400.generateVRM(4,2,0))
        {
          failed("Incorrect release.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getRelease().
     * Try to get the release of a system value which has the
     * release set using setName() after the name was already set.
     **/
    public void Var019()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("qchridctl");
        if (sv.getRelease() != AS400.generateVRM(4,3,0))
        {
          failed("Incorrect release.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getType().
     * Try to get the return type of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var020()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getType();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getType().
     * Try to get the return type of a system value which has the
     * return type set on the constructor.
     **/
    public void Var021()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (sv.getType() != SystemValueList.TYPE_STRING)
        {
          failed("Incorrect return type.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getType().
     * Try to get the return type of a system value which has the
     * return type set using setName().
     **/
    public void Var022()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("Qday");
        if (sv.getType() != SystemValueList.TYPE_STRING)
        {
          failed("Incorrect return type.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getType().
     * Try to get the return type of a system value which has the
     * return type set using setName() after the name was already set.
     **/
    public void Var023()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("qadlactj");
        if (sv.getType() != SystemValueList.TYPE_INTEGER)
        {
          failed("Incorrect return type.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getSize().
     * Try to get the size of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var024()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getSize();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSize().
     * Try to get the size of a system value.
     **/
    public void Var025()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (sv.getSize() != 3)
        {
          failed("Incorrect size.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSize().
     * Try to get the size of a system value which was
     * set using setName().
     **/
    public void Var026()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("Qday");
        if (sv.getSize() != 3)
        {
          failed("Incorrect size.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSize().
     * Try to get the size of a system value which has the
     * size set using setName() after the name was already set.
     **/
    public void Var027()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("qadlactj");
        if (sv.getSize() != 4)
        {
          failed("Incorrect size.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSize().
     * Try to get the size of a system value that is of type TYPE_ARRAY.
     **/
    public void Var028()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "ALRBCKFP");
        if (sv.getSize() != 16)
        {
          failed("Incorrect size: "+sv.getSize());
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSystem().
     * Try to get the system of a system value created using
     * the default constructor.
     **/
    public void Var029()
    {
      try
      {
        SystemValue sv = new SystemValue();
        if (sv.getSystem() != null)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSystem().
     * Try to get the system of a system value which has the
     * system set on the constructor.
     **/
    public void Var030()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSystem().
     * Try to get the system of a system value which has the
     * system set using setSystem().
     **/
    public void Var031()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getSystem().
     * Try to get the system of a system value which has the
     * system set using setSystem() after the name was already set.
     **/
    public void Var032()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setSystem(systemObject_);
        if (sv.getSystem() != systemObject_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getValue().
     * Try to get the value of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var033()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.getValue();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getValue().
     * Try to get the value of a system value created with no
     * name set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var034()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        sv.getValue();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::getValue().
     * Try to get the value of a system value that is not at the
     * correct release level as the system.
     * The method should throw a RequestNotSupportedException.
     **/
    public void Var035()
    {
      try
      {
        if (pwrSys_.getVRM() >= AS400.generateVRM(4,3,0))
        {
          notApplicable();
          return;
        }
        SystemValue sv = new SystemValue(pwrSys_, "qchridctl");
        sv.getValue();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "RequestNotSupportedException", "QCHRIDCTL",
                        RequestNotSupportedException.SYSTEM_LEVEL_NOT_CORRECT))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getValue().
     * Try to get the value of a system value which has the
     * name set on the constructor.
     **/
    public void Var036()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qabnormsw"); // is a readonly value
        String ret = ((String)sv.getValue()).trim();
        if (!ret.equals("0") && !ret.equals("1"))
        {
          failed("Unexpected value retrieved for QABNORMSW: '"+ ret + "'.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getValue().
     * Try to get the value of a system value which has the
     * name set using setName().
     **/
    public void Var037()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        sv.setName("Qconsole"); // is a readonly value
        if (!((String)sv.getValue()).trim().equals("DSP01"))
        {
          failed("Unexpected value retrieved. Manually verify QCONSOLE is '"+((String)sv.getValue()).trim()+"'.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::getValue().
     * Try to get the value of a system value which has the
     * name set using setName() after the name was already set.
     **/
    public void Var038()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("qabnormsw"); // is a readonly value
        String ret = ((String)sv.getValue()).trim();
        if (!ret.equals("0") && !ret.equals("1")) // should be qabnormsw, not qchrid
        {
          failed("Unexpected value retrieved for QABNORMSW: '"+ ret + "'.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::isReadOnly().
     * Try to get the read-only status of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var039()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.isReadOnly();
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::isReadOnly().
     * Try to get the read-only status of a system value which has the
     * name set on the constructor.
     **/
    public void Var040()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        if (sv.isReadOnly() != false)
        {
          failed("Incorrect read-only status.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::isReadOnly().
     * Try to get the read-only status of a system value which has the
     * name set using setName().
     **/
    public void Var041()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("Qday");
        if (sv.isReadOnly() != false)
        {
          failed("Incorrect read-only status.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::isReadOnly().
     * Try to get the read-only status of a system value which has the
     * name set using setName() after the name was already set.
     **/
    public void Var042()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "Qchrid");
        sv.setName("qigc"); // is a readonly value
        if (sv.isReadOnly() != true)
        {
          failed("Incorrect read-only status.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify valid usage of SystemValue::setName().
     * Try to set the name of a system value created using
     * the default constructor.
     **/
    public void Var043()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName("qday");
        if (!sv.getName().equals("QDAY"))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setName().
     * Try to set the name of a system value by passing in null.
     * A NullPointerException should be thrown.
     **/
    public void Var044()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setName(null);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "name"))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::setName().
     * Try to set the name of a system value which has the
     * name already set.
     **/
    public void Var045()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qchrid");
        sv.setName("Qday");
        if (!sv.getName().equals("QDAY"))
        {
          failed("Incorrect name.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setName().
     * Try to set the name of a system value which has the
     * name already set and is already connected.
     * An ExtendedIllegalStateException should be thrown.
     **/
    public void Var046()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qchrid");
        sv.getValue(); // causes a connection
        sv.setName("Qday");
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::setSystem().
     * Try to set the system of a system value created using
     * the default constructor.
     **/
    public void Var047()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        if (sv.getSystem() != pwrSys_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setSystem().
     * Try to set the system of a system value by passing in null.
     * A NullPointerException should be thrown.
     **/
    public void Var048()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(null);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "system"))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::setSystem().
     * Try to set the system of a system value which has the
     * system already set.
     **/
    public void Var049()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qchrid");
        sv.setSystem(systemObject_);
        if (sv.getSystem() != systemObject_)
        {
          failed("Incorrect system.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setSystem().
     * Try to set the system of a system value which has the
     * system already set and has already connected.
     * An ExtendedIllegalStateException should be thrown.
     **/
    public void Var050()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qchrid");
        sv.getValue(); // causes a connection
        sv.setSystem(systemObject_);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_CHANGED))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setValue().
     * Try to set the value of a system value created using
     * the default constructor.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var051()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setValue(" ");
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "system",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setValue().
     * Try to set the value of a system value created with no
     * name set.
     * The method should throw an ExtendedIllegalStateException.
     **/
    public void Var052()
    {
      try
      {
        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        sv.setValue(" ");
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "name",
                        ExtendedIllegalStateException.PROPERTY_NOT_SET))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setValue().
     * Try to set the value of a system value that is not at the
     * correct release level as the system.
     * The method should throw a RequestNotSupportedException.
     **/
    public void Var053()
    {
      try
      {
        if (pwrSys_.getVRM() >= AS400.generateVRM(4,3,0))
        {
          notApplicable();
          return;
        }
        SystemValue sv = new SystemValue(pwrSys_, "qchridctl");
        sv.setValue(new Integer(1));
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "RequestNotSupportedException", "QCHRIDCTL",
                        RequestNotSupportedException.SYSTEM_LEVEL_NOT_CORRECT))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setValue().
     * Try to set the value of a system value which is read-only.
     * An ExtendedIllegalStateException should be thrown.
     **/
    public void Var054()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qabnormsw"); // is a readonly value
        sv.setValue(new Boolean(true));
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionStartsWith(e, "ExtendedIllegalStateException", "QABNORMS",
                        ExtendedIllegalStateException.OBJECT_IS_READ_ONLY))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify invalid usage of SystemValue::setValue().
     * Try to set the value of a system value to null.
     * An NullPointerException should be thrown.
     **/
    public void Var055()
    {
      try
      {
        SystemValue sv = new SystemValue(pwrSys_, "qday");
        sv.setValue(null);
        failed("No exception.");
      }
      catch(Exception e)
      {
        if (exceptionIs(e, "NullPointerException", "value"))
          succeeded();
        else
          failed(e, "Wrong exception info.");
      }
    }


    /**
     * Verify valid usage of SystemValue::setValue().
     * Try to set the value of a system value which has the
     * name set using setName().
     **/
    public void Var056()
    {
      try
      {
        // Get the original value
        SystemValue old = new SystemValue(pwrSys_, "qyear");
        Object obj = old.getValue();

        SystemValue sv = new SystemValue();
        sv.setSystem(pwrSys_);
        sv.setName("qyear");
        sv.setValue("07");
        Object ret = sv.getValue();

        // Set the value back
        old.setValue(obj);

        SystemValue sv2 = new SystemValue(pwrSys_, "qyear");
        Object cur = sv2.getValue();

        if (!((String)ret).trim().equals("07"))
        {
          failed("Incorrect value.");
        }
        else if (!((String)cur).trim().equals(((String)obj).trim()))
        {
          failed("Value not reset.");
        }
        else
        {
          succeeded();
        }
      }
      catch(Exception e)
      {
        failed(e, "Unexpected exception.");
      }
    }
}
