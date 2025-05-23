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

public class ShowHTMLMRI extends Object
{

  public static void main(String[] parameters)
  {

    System.out.println("\nMRI from HMRI.class");
    System.out.println(  "-------------------");
    System.out.println(" ");

    try
    {
       ResourceBundle englishResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.util.html.HMRI", Locale.ENGLISH);

       ResourceBundle countryResourceBundle =
                  ResourceBundle.getBundle("com.ibm.as400.util.html.HMRI");

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
}  // End of ShowHTMLMRI testcase

