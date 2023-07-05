//////////////////////////////////////////////////////////////////////
//
//////////////////////////////////////////////////////////////////////
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

import java.util.ResourceBundle;
import java.util.MissingResourceException;

public class ShowNPMRI
{


    public static void main (String[] args)
    {
        // load the NP MRI resource bundle
        ResourceBundle resources_ =
          ResourceBundle.getBundle("com.ibm.as400.access.NPMRI");

        System.out.println("\n-------------------------------------");
        System.out.println(  "Printer output (spoolfile) attributes");
        System.out.println(  "-------------------------------------");

        System.out.println("\nEnglish   : Copies");
        System.out.println("Translated: " + resources_.getString("ATTR_COPIES") );

        System.out.println("\nEnglish   : Date created");
        System.out.println("Translated: " + resources_.getString("ATTR_DATE") );

        System.out.println("\nEnglish   : Form type");
        System.out.println("Translated: " + resources_.getString("ATTR_FORMTYPE") );

        System.out.println("\nEnglish   : Job");
        System.out.println("Translated: " + resources_.getString("ATTR_JOBNAME") );

        System.out.println("\nEnglish   : Job number");
        System.out.println("Translated: " + resources_.getString("ATTR_JOBNUMBER") );

        System.out.println("\nEnglish   : Printer output number");
        System.out.println("Translated: " + resources_.getString("ATTR_SPLFNUM") );

        System.out.println("\nEnglish   : Printer output");
        System.out.println("Translated: " + resources_.getString("ATTR_SPOOLFILE") );

        System.out.println("\nEnglish   : Output queue");
        System.out.println("Translated: " + resources_.getString("ATTR_OUTQUE") );

        System.out.println("\nEnglish   : Output queue library");
        System.out.println("Translated: " + resources_.getString("ATTR_OUTQUELIB") );

        System.out.println("\nEnglish   : Total pages");
        System.out.println("Translated: " + resources_.getString("ATTR_PAGES") );

        System.out.println("\nEnglish   : Printer");
        System.out.println("Translated: " + resources_.getString("ATTR_PRINTER") );

        System.out.println("\nEnglish   : Priority");
        System.out.println("Translated: " + resources_.getString("ATTR_OUTPTY") );

        System.out.println("\nEnglish   : Status");
        System.out.println("Translated: " + resources_.getString("ATTR_SPLFSTATUS") );

        System.out.println("\nEnglish   : Time created");
        System.out.println("Translated: " + resources_.getString("ATTR_TIME") );

        System.out.println("\nEnglish   : User");
        System.out.println("Translated: " + resources_.getString("ATTR_JOBUSER") );

        System.out.println("\nEnglish   : User data");
        System.out.println("Translated: " + resources_.getString("ATTR_USERDATA") );

        System.out.println("\nEnglish   : Current page");
        System.out.println("Translated: " + resources_.getString("ATTR_CURPAGE") );

        System.out.println("\n-------------------");
        System.out.println(  "Dialog text strings");
        System.out.println(  "-------------------");

        System.out.println("\nEnglish   : Printer Output Attributes");
        System.out.println("Translated: " + resources_.getString("DLG_ATTR_TITLE") );

        System.out.println("\nEnglish   : Attributes available to select:");
        System.out.println("Translated: " + resources_.getString("DLG_ATTR_AVAILABLE_LABEL") );

        System.out.println("\nEnglish   : Attributes selected:");
        System.out.println("Translated: " + resources_.getString("DLG_ATTR_SELECTED_LABEL") );

        System.out.println("\nEnglish   : Attribute:");
        System.out.println("Translated: " + resources_.getString("DLG_ATTR_NAME_LABEL") );

        System.out.println("\nEnglish   : Value:");
        System.out.println("Translated: " + resources_.getString("DLG_ATTR_VALUE_LABEL") );

        System.out.println("\n--------------");
        System.out.println(  "Dialog buttons");
        System.out.println(  "--------------");

        System.out.println("\nEnglish   : Add");
        System.out.println("Translated: " + resources_.getString("DLG_ADD_BUTTON") );

        System.out.println("\nEnglish   : Cancel");
        System.out.println("Translated: " + resources_.getString("DLG_CANCEL_BUTTON") );

        System.out.println("\nEnglish   : Move up");
        System.out.println("Translated: " + resources_.getString("DLG_MOVE_UP_BUTTON") );

        System.out.println("\nEnglish   : Move down");
        System.out.println("Translated: " + resources_.getString("DLG_MOVE_DOWN_BUTTON") );

        System.out.println("\nEnglish   : OK");
        System.out.println("Translated: " + resources_.getString("DLG_OK_BUTTON") );

        System.out.println("\nEnglish   : Remove");
        System.out.println("Translated: " + resources_.getString("DLG_REMOVE_BUTTON") );

    }


}
