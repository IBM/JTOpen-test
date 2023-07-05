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
//////////////////////////////////////////////////////////////////////////////////
//
// Running this program displays the sign-on dialog.  It was written
// for translation test.
//
// Command syntax:
//    showSignon system
//
//////////////////////////////////////////////////////////////////////////////////
package test; 

import java.io.*;
import java.util.*;
import java.net.*;
import com.ibm.as400.access.*;

public class ShowSignon extends Object
{

   // Create a reader to get input from the user.

   static BufferedReader inputStream = new BufferedReader(new InputStreamReader(System.in),1);



   public static void main(String[] parameters)
   {

      System.out.println( " " );



      // if the system name was not specified, display help text and exit.

      String system = null;

      if (parameters.length >= 1)
         system = parameters[0];
      else
         system = "mySystem";


      AS400 as400 = new AS400(system);

      try
      {
         CommandCall crtlib = new CommandCall(as400);
         crtlib.run("CRTLIB JAVADEMO");
      }
      catch (Exception e)
      {
      }


      System.exit(0);
   }
}



