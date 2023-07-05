//////////////////////////////////////////////////////////////////////
//
////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  ShowAccessMRI
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test; 

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.*;

public class ShowAccessMRI extends Object
{

  public static void main(String[] parameters)
  {

    System.out.println("\nMRI from MRI.properties");
    System.out.println(  "-----------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.MRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.MRI");

       Enumeration keys = englishResourceBundle.getKeys();

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


    

    System.out.println("\nMRI from JDMRI.properties");
    System.out.println(  "-------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.JDMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.JDMRI");

       Enumeration keys = englishResourceBundle.getKeys();

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



    
    System.out.println("\nMRI from MRI2.properties");
    System.out.println(  "-----------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.MRI2", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.MRI2");

       Enumeration keys = englishResourceBundle.getKeys();

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




    System.out.println("\nMRI from JDMRI2.properties");
    System.out.println(  "-------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.JDMRI2", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.JDMRI2");

       Enumeration keys = englishResourceBundle.getKeys();

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




    System.out.println("\nMRI from CoreMRI.properties");
    System.out.println(  "-------------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.CoreMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.access.CoreMRI");

       Enumeration keys = englishResourceBundle.getKeys();

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
}  // End of ShowAccessMRI testcase

