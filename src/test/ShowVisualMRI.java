//////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

public class ShowVisualMRI extends Object
{

  public static void main(String[] parameters)
  {

    System.out.println("\nMRI from VMRI.properties");
    System.out.println(  "------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VMRI");

       Enumeration<?> keys = englishResourceBundle.getKeys();

       while (keys.hasMoreElements())
       {
          String key = (String) keys.nextElement();
          System.out.println("English:     " + englishResourceBundle.getString(key));
          System.out.println("Translated:  " + countryResourceBundle.getString(key));
          System.out.println("");
       }
    }
    catch (Exception e)
    {
       System.out.println(e);
    }





    System.out.println("\nMRI from VNPMRI.properties");
    System.out.println(  "------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VNPMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VNPMRI");

       Enumeration<?> keys = englishResourceBundle.getKeys();

       while (keys.hasMoreElements())
       {
          String key = (String) keys.nextElement();
          System.out.println("English:     " + englishResourceBundle.getString(key));
          System.out.println("Translated:  " + countryResourceBundle.getString(key));
          System.out.println("");
       }
    }
    catch (Exception e)
    {
       System.out.println(e);
    }





    System.out.println("\nMRI from VQRYMRI.properties");
    System.out.println(  "------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VQRYMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.vaccess.VQRYMRI");

       Enumeration<?> keys = englishResourceBundle.getKeys();

       while (keys.hasMoreElements())
       {
          String key = (String) keys.nextElement();
          System.out.println("English:     " + englishResourceBundle.getString(key));
          System.out.println("Translated:  " + countryResourceBundle.getString(key));
          System.out.println("");
       }
    }
    catch (Exception e)
    {
       System.out.println(e);
    }



  }
}  // End of ShowVisalMRI testcase

