///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  NPAFPRListResourceFilterTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.NP;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Vector;

import com.ibm.as400.access.AFPResource;
import com.ibm.as400.access.AFPResourceList;
import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.IllegalPathNameException;

import test.Testcase;

/**
 Testcase NPAFPRListResourceFilterTestcase.
 **/
public class NPAFPRListResourceFilterTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "NPAFPRListResourceFilterTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.NPPrintTest.main(newArgs); 
   }
    // was the listener invoked?
    boolean listenerInvoked;

    /**
     Constructor.  This is called from NPPrintTest::createTestcases().
     **/
    public NPAFPRListResourceFilterTestcase(AS400            systemObject,
                                            Vector<String> variationsToRun,
                                            int              runMode,
                                            FileOutputStream fileOutputStream)
    {
	// $$$ TO DO $$$
	// Replace the third parameter with the total number of variations
	// in this testcase.
        super(systemObject, "NPAFPRListResourceFilterTestcase", 26,
	      variationsToRun, runMode, fileOutputStream);
    }

    /**
     Runs the variations requested.
     **/
    public void run()
    {
        output_.println("NPPrintTest::NPAFPRListResourceFilterTestcase");
	boolean allVariations = (variationsToRun_.size() == 0);

	try
	{
	    // run variation 1
	    if ((allVariations || variationsToRun_.contains("1")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(1);
		Var001();
	    }

	    // run variation 2
	    if ((allVariations || variationsToRun_.contains("2")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(2);
		Var002();
	    }

	    // run variation 3
	    if ((allVariations || variationsToRun_.contains("3")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(3);
		Var003();
	    }

	    // run variation 4
	    if ((allVariations || variationsToRun_.contains("4")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(4);
		Var004();
	    }

	    // run variation 5
	    if ((allVariations || variationsToRun_.contains("5")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(5);
		Var005();
	    }

	    // run variation 6
	    if ((allVariations || variationsToRun_.contains("6")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(6);
		Var006();
	    }

	    // run variation 7
	    if ((allVariations || variationsToRun_.contains("7")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(7);
		Var007();
	    }

	    // run variation 8
	    if ((allVariations || variationsToRun_.contains("8")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(8);
		Var008();
	    }

	    // run variation 9
	    if ((allVariations || variationsToRun_.contains("9")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(9);
		Var009();
	    }

	    // run variation 10
	    if ((allVariations || variationsToRun_.contains("10")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(10);
		Var010();
	    }

	    // run variation 11
	    if ((allVariations || variationsToRun_.contains("11")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(11);
		Var011();
	    }

	    // run variation 12
	    if ((allVariations || variationsToRun_.contains("12")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(12);
		Var012();
	    }

	    // run variation 13
	    if ((allVariations || variationsToRun_.contains("13")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(13);
		Var013();
	    }

	    // run variation 14
	    if ((allVariations || variationsToRun_.contains("14")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(14);
		Var014();
	    }

	    // run variation 15
	    if ((allVariations || variationsToRun_.contains("15")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(15);
		Var015();
	    }

	    // run variation 16
	    if ((allVariations || variationsToRun_.contains("16")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(16);
		Var016();
	    }

	    // run variation 17
	    if ((allVariations || variationsToRun_.contains("17")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(17);
		Var017();
	    }

	    // run variation 18
	    if ((allVariations || variationsToRun_.contains("18")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(18);
		Var018();
	    }

	    // run variation 19
	    if ((allVariations || variationsToRun_.contains("19")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(19);
		Var019();
	    }

	    // run variation 20
	    if ((allVariations || variationsToRun_.contains("20")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(20);
		Var020();
	    }

	    // run variation 21
	    if ((allVariations || variationsToRun_.contains("21")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(21);
		Var021();
	    }

            // run variation 22
            if ((allVariations || variationsToRun_.contains("22")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(22);
                Var022();
	    }

            // run variation 23
            if ((allVariations || variationsToRun_.contains("23")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(23);
                Var023();
	    }

            // run variation 24
            if ((allVariations || variationsToRun_.contains("24")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(24);
                Var024();
	    }

            // run variation 25
            if ((allVariations || variationsToRun_.contains("25")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(25);
                Var025();
	    }

            // run variation 26
            if ((allVariations || variationsToRun_.contains("26")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
                setVariation(26);
                Var026();
	    }

	    // $$$ TO DO $$$
	    // Add an 'if' block using the following as a template for each variation.
	    // Make sure you correctly set the variation number
	    // and runmode in the 'if' condition, and the
	    // variation number in the setVariation call.
	    //
	    // replace the X with the variation number you are adding
	    //
/* $$$ TO DO $$$ - delete this line
	    if ((allVariations || variationsToRun_.contains("X")) &&
		runMode_ != ATTENDED) // Note: This is an unattended variation.
	    {
		setVariation(X);
		VarXXX();
	    }
$$$ TO DO $$$ - delete this line */

	} // end try block

	catch( Exception e )
	{
	    failed(e, "Unexpected exception");
	}

    } // end run method

    /**
     * Tests setting then getting the AFPResourceList resourceFilter
     **/
    public void Var001()
    {
        try
            {
            // create an AFPResourceList object using default constructor
            AFPResourceList list = new AFPResourceList();

            // Set the resourceFilter
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            if (list.getResourceFilter().trim().equals("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC")) succeeded();
            else failed("Could not set/get AFPResourceList resourceFilter.");
            list.close(); 
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var001

    /**
     * Tests setting the AFPResourceList resourceFilter to an invalid type.
     **/
    public void Var002()
    {
        try
            {
            // create an AFPResourceList object using default constructor
            AFPResourceList list = new AFPResourceList();

            // Set the resourceFilter, OUTQ is not valid
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.OUTQ");
            list.close(); 
            failed("Could set an invalid AFP Resource Type");
            }

        catch (IllegalPathNameException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var002

    /**
     * Tests setting the AFPResourceList resourceFilter,
     * then removing the filter using an empty string.
     **/
    public void Var003()
    {
        try
            {
            // create an AFPResourceList object using default constructor
            AFPResourceList list = new AFPResourceList();

            // Set the resourceFilter
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // Now remove the filter
            list.setResourceFilter("");

            if (list.getResourceFilter().trim().equals("")) succeeded();
            else failed("Could not remove AFPResourceList resourceFilter.");
            list.close(); 
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var003

    /**
     * Tests setting the AFPResourceList's resourceFilter to null.
     **/
    public void Var004()
    {
        try
            {
            // create an AFPResourceList object using default constructor
            AFPResourceList list = new AFPResourceList();

            list.setResourceFilter(null);
            list.close(); 
            failed("Could set the resourceFilter to null");
            }

        catch (NullPointerException e)
            {
            succeeded();
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var004

    /**
     * Tests retrieving the AFPResource List's resourceFilter
     * before it is set.
     **/
    public void Var005()
    {
        try
            {
            // create an AFPResourceList object using default constructor
            AFPResourceList list = new AFPResourceList();

            if( list.getResourceFilter().length() == 0 )
                {
                succeeded();
                }
            else
                {
                failed("resourceFilter was not set, expecting empty string");
                }
            list.close(); 
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var005

    /**
     * Lists AFP Resource(s) synchronously filtering by specific library name
     **/
    public void Var006()
    {
        try
            {
            // create an AFP Resource List object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by a specific library name
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by specific library name");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var006

    /**
     * Lists AFP Resource(s) synchronously filtering %ALL% library name
     **/
    public void Var007()
    {
	succeeded();
	/*
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by %ALL% library name
            resList.setResourceFilter("/QSYS.LIB/%ALL%.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();

            // check to see if we got some resources
            // since there are 22 resources in NPJAVA library we should have gotten
            // at least 22 back in the list
            if (resList.size() >= 22)
                {
                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                succeeded();
                }
            else
                {
                failed("Could not list resources by %ALL% library name. Only "+resList.size()+" resources listed.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }
	*/

    } // end Var007

    /**
     * Lists AFP Resource(s) synchronously filtering %ALLUSR% library name
     **/
    public void Var008()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by %ALLUSR% library name
            resList.setResourceFilter("/QSYS.LIB/%ALLUSR%.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();

            // check to see if we got some resources
            // since there are 22 resources in NPJAVA library we should have gotten
            // at least 22 back in the list
            if (resList.size() >= 22)
                {
                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed. e="+e);

                succeeded();
                }
            else
                {
                failed("Could not list resources by %ALLUSR% library name.  Only "+resList.size()+" resources listed.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var008

    /**
     * Lists AFP Resource(s) synchronously filtering %CURLIB% library name
     * ASSUMPTION: QGPL is CURLIB, by default job description.  If someone
     * has changed the default job description to not have curlib be qgpl then
     * this variation will fail.
     **/
    public void Var009()
    {
      boolean fSuccess = true;
      String resPath = null;
      String sResourceName = null;

      try
      {
        // need to copy an AFP font resource from NPJAVA to QGPL
        AFPResourceList resList1 = new AFPResourceList(systemObject_);
        resList1.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");
        resList1.openSynchronously();
        Enumeration<AFPResource> e1 = resList1.getObjects();
        if (resList1.size() > 0)
        {
          AFPResource res1 = e1.nextElement();
          sResourceName = res1.getName();
          
          try
          {
            // use CRTDUPOBJ to copy the font resource object to QGPL
            CommandCall cmd = new CommandCall(systemObject_);
            if (cmd.run("QSYS/CRTDUPOBJ OBJ("+ sResourceName +") FROMLIB(NPJAVA) OBJTYPE(*FNTRSC) TOLIB(QGPL) NEWOBJ(*SAME)") == false)
            {
              output_.println("Could not copy a font resource object to QGPL");
            }
          } // end try block
          
          catch( Exception ex )
          {
            failed(ex, "Unexpected exception");
            resList1.close(); 
            return;
          }
        }
        else
        {
          output_.println("Failed to move font resource from NPJAVA to QGPL");
        }
        
        // close the list
        resList1.close();

        // filter by %CURLIB% library
        AFPResourceList resList = new AFPResourceList(systemObject_);
        resList.setResourceFilter("/QSYS.LIB/%CURLIB%.LIB/%ALL%.%ALL%");
        
        // now try to build resource list synchronously
        resList.openSynchronously();
        
        // check the first object returned and make sure that 
        // curlib points to QGPL
        Enumeration<AFPResource> e = resList.getObjects();
        
        // check to see if we got some resources
        if (resList.size() > 0)
        {
          while(e.hasMoreElements() )
          {
            AFPResource res = (AFPResource)e.nextElement();
            resPath = res.getPath();
            // verify library is our curlib = QGPL
            if (resPath.startsWith("/QSYS.LIB/QGPL.LIB"))
            {
              fSuccess = true;
            }
            else
            {
              output_.println("Bad resource:"+resPath);
              fSuccess = false;
              break;
            }
          } // end while
          
          // indicate how many resources were listed
          output_.println(resList.size() + " AFP Resources listed.");
          
          // check to see if we were successful
          if (fSuccess == true)
          {
            succeeded();
          }
          else
          {
            failed("Could not list resources by %CURLIB% library name");
          }
        } // if size
        // if we did not get back any resources then they don't have the correct
        // version of the NPJAVA library on their system.
        else 
        {
          failed("No AFP Resources found in %CURLIB% Library.");
        }
        
        resList.close();
      } 
      
      catch (Exception e)
      {
        // we need to check for RequestNotSupportedException and say successful since
        // we can't get an accurate version/release from the AS/400
        if (exceptionIs(e, "RequestNotSupportedException"))
        {
          output_.println("Correct OS/400 level is required for AFP resources.");
          succeeded();
        }
        else failed(e, "Unexpected exception");
      }
      
      // clean up
      if (sResourceName != null && !sResourceName.equals(""))
      {
        try
        {
          // use DLTFNTRSC to delete the font resource object we put in QGPL
          CommandCall cmd = new CommandCall(systemObject_);
          if (cmd.run("QSYS/DLTFNTRSC FNTRSC(QGPL/" + sResourceName +")") == false)
          {
            output_.println("We could not delete the font resource we put in QGPL");
          }
        } // end try block
        
        catch( Exception ex )
        {
          failed(ex, "Unexpected exception");
          return;
        }
      }

      System.gc();
      
    } // end Var009
    
    /**
     * Lists AFP Resource(s) synchronously filtering %LIBL% library name
     **/
    public void Var010()
    {
      try
      {
        // create an AFP Resource list object
        AFPResourceList resList = new AFPResourceList(systemObject_);
        
        // filter by %LIBL% library
        resList.setResourceFilter("/QSYS.LIB/%LIBL%.LIB/%ALL%.%ALL%");
        
        // now try to build resource list synchronously
        resList.openSynchronously();
        
        Enumeration<AFPResource> e = resList.getObjects();
        
        // check to see if we got some resources
        // since there are 22 resources in NPJAVA library we should have gotten
        // at least 22 back in the list
        if (resList.size() >= 22)
        {
          // indicate how many resources were listed
          output_.println(resList.size() + " AFP Resources listed."+e);
          
          succeeded();
        }
        else
        {
          failed("Could not list resources by %LIBL% library name.  Only "+resList.size()+" resources listed.");
        }
        
        resList.close();
        System.gc();
        
      } // end try block
      
      catch (Exception e)
      {
        // we need to check for RequestNotSupportedException and say successful since
        // we can't get an accurate version/release from the AS/400
        if (exceptionIs(e, "RequestNotSupportedException"))
        {
          output_.println("Correct OS/400 level is required for AFP resources.");
          succeeded();
        }
        else failed(e, "Unexpected exception");
      }
      
    } // end Var010

    /**
     * Lists AFP Resource(s) synchronously filtering %USRLIBL% library name
     **/
    public void Var011()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by %USRLIBL% library
            resList.setResourceFilter("/QSYS.LIB/%USRLIBL%.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();

            // there is no way to tell how many resources would be in the
            // %USRLBL% list on a normal system, so we will just print
            // the number we find.

            // indicate how many resources were listed
            output_.println(resList.size() + " AFP Resources listed."+e);

            succeeded();

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var011

    /**
     * Lists AFP Resource(s) synchronously filtering specific resource name
     **/
    public void Var012()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by specific resource name
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            // now try to build resource list synchronously
            resList.openSynchronously();

            // retrieve the first resource
            AFPResource res = (AFPResource)resList.getObject(0);

            // check to see if we got one resource and verify name
            if ( (resList.size() == 1) &&
                 (res.getPath().trim().equals("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC")) )
                {
                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                succeeded();
                }
            else
                {
                failed("Could not list by a specific resource name. Resource was " + res.getPath()
                       + " Only "+resList.size()+" resources listed.");
                }

            resList.close();
            System.gc();
            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var012

   /**
     * Lists AFP Resource(s) synchronously filtering generic resource name
    **/
    public void Var013()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by generic resource name
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/P1*.PAGDFN");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB/P1"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by generic resource name");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var013

   /**
     * Lists AFP Resource(s) synchronously filtering %ALL% resource name
    **/
    public void Var014()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by %ALL% resource name
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FORMDF");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".FORMDF"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by %ALL% resource name");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var014

    /**
     * Lists AFP Resource(s) synchronously filtering %ALL% type
     **/
    public void Var015()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by %ALL% resource type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.%ALL%");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().startsWith("/QSYS.LIB/NPJAVA.LIB"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by %ALL% resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var015

   /**
     * Lists AFP Resource(s) synchronously filtering FNTRSC type
    **/
    public void Var016()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by FNTRSC type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FNTRSC");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".FNTRSC"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by FNTRSC resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var016

   /**
     * Lists AFP Resource(s) synchronously filtering FORMDF type
    **/
    public void Var017()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by FORMDF type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.FORMDF");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".FORMDF"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by FORMDF resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var017

   /**
     * Lists AFP Resource(s) synchronously filtering OVL type
    **/
    public void Var018()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by OVL type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OVL");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".OVL"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by OVL resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var018

   /**
     * Lists AFP Resource(s) synchronously filtering PAGDFN type
    **/
    public void Var019()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by PAGDFN type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.PAGDFN");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".PAGDFN"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by PAGDFN resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var019

   /**
     * Lists AFP Resource(s) synchronously filtering PAGSEG type
    **/
    public void Var020()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            // filter by PAGESEG type
            resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.PAGSEG");

            // now try to build resource list synchronously
            resList.openSynchronously();

            Enumeration<AFPResource> e = resList.getObjects();
            String resPath = null;

            // check to see if we got some resources
            if (resList.size() > 0)
                {
                boolean fSuccess = false;
                while(e.hasMoreElements() )
                    {
                    AFPResource res = (AFPResource)e.nextElement();
                    resPath = res.getPath();
                    if (res.getPath().endsWith(".PAGSEG"))
                        {
                        fSuccess = true;
                        }
                    else
                        {
                        output_.println("Bad resource:"+resPath);
                        fSuccess = false;
                        break;
                        }
                    } // end while

                // indicate how many resources were listed
                output_.println(resList.size() + " AFP Resources listed.");

                // check to see if we were successful
                if (fSuccess == true)
                    {
                    succeeded();
                    }
                else
                    {
                    failed("Could not list resources by PAGSEG resource type");
                    }
                } // if size
            // if we did not get back any resources then they don't have the correct
            // version of the NPJAVA library on their system.
            else 
                {
                failed("No AFP Resources found in NPJAVA Library.");
                }

            resList.close();
            System.gc();

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var020

   /**
    * Lists AFP Resource(s) synchronously filtering invalid type.
    * Expects IllegalPathNameException.
    **/
    public void Var021()
    {
        try
            {
            // create an AFP Resource list object
            AFPResourceList resList = new AFPResourceList(systemObject_);

            try
                {
                // filter by invalid type
                resList.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/%ALL%.OUTQ");
                resList.close(); 
                failed("Could filter by invalid type.");
                }
            catch (Exception e)
                {
                if (exceptionIs(e, "IllegalPathNameException")) succeeded();
                else failed(e, "Unexpected exception");
                }

            } // end try block

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else failed(e, "Unexpected exception");
            }

    } // end Var021

    /**
     * Tests that a property change event is fired when the
     * AFPResourceList's resourceFilter is set.
     **/
    public void Var022()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true; 
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("resourceFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        PropertyListener propertyListener = new PropertyListener();
        AFPResourceList list = null; 
        try
            {
            // create an AFP Resource List object using the default constructor
            list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // set the resourceFilter
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            if( !listenerInvoked )
                {
                failed("PropertyChange Listener was not invoked");
                }

            list.removePropertyChangeListener(propertyListener);
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                failed(e, "Unexpected exception");
            } finally { 
              if (list != null) list.close(); 
            }

    } // end Var022

    /**
     * Tests that a vetoable change event is fired when the
     * AFPResourceList's resourceFilter is set.
     **/
    public void Var023()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException   
            {
                listenerInvoked = true;
                boolean succeeded = true;
                String reason = "";

                if( !(evt.getPropertyName().equals("resourceFilter")) )
                    {
                    succeeded = false;
                    reason = "Problem with property name";
                    }

                if( !(((String)evt.getOldValue()).equals("")) )
                    {
                    succeeded = false;
                    reason = "Problem with old value";
                    }

                if( !(((String)evt.getNewValue()).equals(
                                  "/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC")) )
                    {
                    succeeded = false;
                    reason = "Problem with new value";
                    }

                if( succeeded )
                    {
                    succeeded();
                    }
                else
                    {
                    failed(reason);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        AFPResourceList list  = null; 
        try
            {
            // create an AFP Resource List object using the default constructor
            list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the resourceFilter
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            if( !listenerInvoked )
                {
                failed("VetoableChange Listener was not invoked");
                }

            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                failed(e, "Unexpected exception");
            } finally { 
              if (list != null) list.close(); 
            }

    } // end Var023

    /**
     * Tests that a property change event is NOT fired when the
     * AFPResourceList's resourceFilter property change is vetoed.
     **/
    public void Var024()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                // If this listener gets invoked the veto was ignored.
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events.
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // Check the property name.
                if( evt.getPropertyName().equals("resourceFilter") )
                    {
                    throw new PropertyVetoException("I veto", evt);
                    }
            }
        }
        VetoableListener vetoableListener = new VetoableListener();
        AFPResourceList list = null; 
        try
            {
            // create an AFP Resource List object using the default constructor
            list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change listener
            list.addPropertyChangeListener(propertyListener);

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the resourceFilter, consume PropertyVetoException
            try
                {
                list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");
                }
            catch(PropertyVetoException e)
                {
                // For this variation, consume the PropertyVetoException
                }

            if( listenerInvoked )
                {
                failed("PropertyChange event fired after property change vetoed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception" );
                }
    } finally { 
      if (list != null) list.close(); 
    }

    } // end Var024

    /**
     * Tests that a PropertyVetoException is thrown when the 
     * AFPResourceList's resourceFilter property change is vetoed.
     **/
    public void Var025()
    {
        // Define inner class to listen for vetoable change events
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                // always veto the property change
                throw new PropertyVetoException("I veto", evt);
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        AFPResourceList list = null;
        try
            {
            // create an AFP Resource List object using the default constructor
            list = new AFPResourceList();

            // add the vetoable change listener
            list.addVetoableChangeListener(vetoableListener);

            // set the system, a PropertyVetoException should be thrown
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            failed("Expecting PropertyVetoException");
            }

        catch( PropertyVetoException e )
            {
            succeeded();
            }

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                failed(e, "Unexpected exception" );
            }

        finally
            {
            // remove the listener
          if (list != null) { 
            list.removeVetoableChangeListener(vetoableListener);
            list.close(); 
          }
           
            }

    } // end Var025

    /**
     * Tests that the property change and vetoable change listeners               
     * are actually removed.
     **/
    public void Var026()
    {
        // Define inner class to listen for property change events
        class PropertyListener implements java.beans.PropertyChangeListener
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                listenerInvoked = true;
            }
        }
        PropertyListener propertyListener = new PropertyListener();

        // Define inner class to listen for vetoable change events 
        class VetoableListener implements java.beans.VetoableChangeListener
        {
            public void vetoableChange(PropertyChangeEvent evt)
              throws PropertyVetoException
            {
                listenerInvoked = true;
            }
        }
        VetoableListener vetoableListener = new VetoableListener();

        AFPResourceList list = null;
        try
            {
            // create an AFP Resource List object using the default constructor
            list = new AFPResourceList();

            // reset our flag
            listenerInvoked = false;

            // add the property change and vetoable change listeners
            list.addPropertyChangeListener(propertyListener);
            list.addVetoableChangeListener(vetoableListener);

            // remove the property change and vetoable change listeners
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);

            // set the resourceFilter 
            list.setResourceFilter("/QSYS.LIB/NPJAVA.LIB/C0D0GB10.FNTRSC");

            if( listenerInvoked )
                {
                failed("Listener was not removed");
                }
            else
                {
                succeeded();
                }

            // remove the listeners again, this should be OK.
            list.removePropertyChangeListener(propertyListener);
            list.removeVetoableChangeListener(vetoableListener);
            } 

        catch (Exception e)
            {
            // we need to check for RequestNotSupportedException and say successful since
            // we can't get an accurate version/release from the AS/400
            if (exceptionIs(e, "RequestNotSupportedException"))
                {
                output_.println("Correct OS/400 level is required for AFP resources.");
                succeeded();
                }
            else
                {
                failed(e, "Unexpected exception" );
                }
            } finally { 
              if (list != null) list.close(); 
            }

    } // end Var026

    // $$$ TO DO $$$
    // Create a VarXXX() method using the following as a template.
    // You should have a method for each variation in your testcase.

    /**
     Some description here.  This will becaome part of the testplan,
     so be descriptive and complete!  Include high-level description
     of what is being tested and expected results.
     **/

/* $$$ TO DO $$$ - delete this line
    public void VarXXX()
    {
	try
	{
	    // $$$ TO DO $$$
	    // Put your test variation code here.
	    succeeded();  // Note: This variation will be successful.
	} // end try block

	catch (Exception e)
	{
	    failed(e, "Unexpected exception");
	}

    } // end VarXXX
$$$ TO DO $$$ - delete this line */

} // end NPAFPRListResourceFilterTestcase class


