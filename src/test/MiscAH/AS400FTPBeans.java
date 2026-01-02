///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400FTPBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import com.ibm.as400.access.*;

import test.FTPTest;
import test.JTOpenTestEnvironment;
import test.PasswordVault;
import test.Testcase;

import java.beans.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.Integer;
import java.lang.String;


public class AS400FTPBeans
             extends Testcase
             implements FTPListener, VetoableChangeListener, PropertyChangeListener

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "AS400FTPBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private String system_   = null;
    public  String testDirectory = "FTPTestDir";
    private String initialToken_ = null;
    public boolean notWorthTrying = false;

  
  
    public  int connectCalled    = 0;
    public int disconnectCalled = 0;
    public int getCalled        = 0;
    public int putCalled        = 0;
    public  int listCalled       = 0;
    private FTPEvent ftpEvent;
    private PropertyChangeEvent propertyChangeEvent;
    private PropertyChangeEvent vetoChangeEvent;
    private boolean veto = false;

    public AS400FTPBeans (AS400 systemObject,
                     Hashtable<String,Vector<String>> namesAndVars,
                     int runMode,
                     FileOutputStream fileOutputStream,
                     String password,
                     String userid,
                     String initialToken)
    {
        super (systemObject,
               "AS400FTPBeans",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


        initialToken_ = initialToken;

        if (initialToken_ == null)
        {
           output_.println("-directory is invalid, no test will be run");
           notWorthTrying = true;
           output_.println();
        }
        else
        {
          if (FTPTest.DEBUG) {
            output_.println("using initial token " + initialToken_);
            System.out.println();
          }
        }


        if (systemObject_ != null)
        {
           user_     = userid;
           encryptedPassword_ = PasswordVault.getEncryptedPassword(password);
           system_   = systemObject_.getSystemName();
        }

        if (FTPTest.DEBUG) output_.println();

        if ((user_ == null) || (user_.length() < 1))
           output_.println("===> warning, variations will fail because no -uid specified");

        if ((password == null) || (password.length() < 1))
           output_.println("===> warning, variations will fail because no -password specified");

        if ((system_ == null) || (system_.length() < 1))
           output_.println("===> warning, variations will fail because no -system specified");

        if (FTPTest.DEBUG) output_.println();

    }


    /**
     @exception  Exception  If an exception occurs.
     **/
    protected void setup()
      throws Exception
    {
      lockSystem("FTP",600); 
   
    }


    /**
     Performs cleanup needed after running variations.
     @exception  Exception  If an exception occurs.
     **/
    protected void cleanup() throws Exception
    {
    
      unlockSystem(); 
    }






    public void beansCleanup()
    {
       connectCalled    = 0;
       disconnectCalled = 0;
       getCalled        = 0;
       putCalled        = 0;
       listCalled       = 0;

       ftpEvent = null;
       propertyChangeEvent = null;
       vetoChangeEvent = null;

       veto = false;
    }

    public void listed(FTPEvent e)
    {
       listCalled ++;
       ftpEvent = e;
    }

    public void connected(FTPEvent e)
    {
       connectCalled ++;
       ftpEvent = e;
    }

    public void disconnected(FTPEvent e)
    {
       disconnectCalled ++;
       ftpEvent = e;
    }

    public void retrieved(FTPEvent e)
    {
       putCalled ++;
       ftpEvent = e;
    }

    public void put(FTPEvent e)
    {
       putCalled ++;
       ftpEvent = e;
    }

    public void vetoableChange(PropertyChangeEvent e)
                throws PropertyVetoException
    {
       vetoChangeEvent = e;

       if (veto)
          throw new PropertyVetoException("Property vetoed", e);
    }

    public void propertyChange(PropertyChangeEvent e)
    {
       propertyChangeEvent = e;
    }


    // ---------------------------------------------------------------------
    //  Property Change test cases - user, null
    // ---------------------------------------------------------------------
    public void Var001()
    {
           // some test cases were left as place holders because
           // I was too lazy to renumber test cases after
           // deleting some.
       succeeded();
    }



    // ---------------------------------------------------------------------
    //  Property Change test cases - server
    // ---------------------------------------------------------------------
    public void Var002()
    {
          try
          {
             beansCleanup();

             AS400 system2 = new AS400();
             AS400 system3 = new AS400();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.addPropertyChangeListener(this);

             ftp.setSystem(system2);

             if (propertyChangeEvent != null)
             {
                if (  (propertyChangeEvent.getPropertyName().equals("system"))
                    &&(propertyChangeEvent.getOldValue() == systemObject_)
                    &&(propertyChangeEvent.getNewValue() == system2))
                {
                   beansCleanup();
                   ftp.removePropertyChangeListener(this);

                   ftp.setSystem(system3);

                   if (propertyChangeEvent == null)
                   {
                      succeeded();
                   }
                   else
                   {
                      failed("listener not removed");
                   }
                }
                else
                {
                   failed("property event bad");
                }
             }
             else
             {
                failed("no property change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }





    // ---------------------------------------------------------------------
    //  Property Change test cases - reconnect
    // ---------------------------------------------------------------------
    public void Var003()
    {
       succeeded();
    }



    // ---------------------------------------------------------------------
    //  Property Change test cases - port
    // ---------------------------------------------------------------------
    public void Var004()
    {
          try
          {
             beansCleanup();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.addPropertyChangeListener(this);

             ftp.setPort(100);


             if (propertyChangeEvent != null)
             {
                Integer ov = (Integer) propertyChangeEvent.getOldValue();
                Integer nv = (Integer) propertyChangeEvent.getNewValue();
                if (  (propertyChangeEvent.getPropertyName().equals("port"))
                    &&(ov.intValue() == 21)
                    &&(nv.intValue() == 100))
                {
                   beansCleanup();
                   ftp.removePropertyChangeListener(this);

                   ftp.setPort(200);

                   if (propertyChangeEvent == null)
                   {
                      succeeded();
                   }
                   else
                   {
                      failed("listener not removed");
                   }
                }
                else
                {
                   failed("property event bad");
                }
             }
             else
             {
                failed("no property change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }



    // ---------------------------------------------------------------------
    //  Veto Change test cases - user, null
    // ---------------------------------------------------------------------
    public void Var005()
    {
       succeeded();
    }




    // ---------------------------------------------------------------------
    //  Veto Change test cases - server
    // ---------------------------------------------------------------------
    public void Var006()
    {
          try
          {
             beansCleanup();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.addVetoableChangeListener(this);

             AS400 system2 = new AS400();
             AS400 system3 = new AS400();

             ftp.setSystem(system2);

             if (vetoChangeEvent != null)
             {
                if (  (vetoChangeEvent.getPropertyName().equals("system"))
                    &&(vetoChangeEvent.getOldValue() == systemObject_)
                    &&(vetoChangeEvent.getNewValue() == system2))
                {
                   veto = true;
                   try
                   {
                      ftp.setSystem(system3);
                      failed("veto = true, no exception");
                   }
                   catch (Exception e)
                   {
                      if (exceptionIs(e, "PropertyVetoException"))
                      {
                         if (ftp.getSystem() == system2)
                            succeeded();
                         else
                            failed("bad value after change vetoed");
                      }
                      else
                      {
                          failed(e, "veto event bad");
                      }
                   }
                }
                else
                {
                   failed("veto data bad 2");
                }
             }
             else
             {
                failed("no veto change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }



    // ---------------------------------------------------------------------
    //  Veto Change test cases - reconnect
    // ---------------------------------------------------------------------
    public void Var007()
    {
       succeeded();
    }





    // ---------------------------------------------------------------------
    //  Veto Change test cases - port
    // ---------------------------------------------------------------------
    public void Var008()
    {
          try
          {
             beansCleanup();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.setPort(200);
             ftp.addVetoableChangeListener(this);

             ftp.setPort(300);

             if (vetoChangeEvent != null)
             {
                Integer ov = (Integer) vetoChangeEvent.getOldValue();
                Integer nv = (Integer) vetoChangeEvent.getNewValue();
                if (  (vetoChangeEvent.getPropertyName().equals("port"))
                    &&(ov.intValue() == 200)
                    &&(nv.intValue() == 300))
                {
                   veto = true;
                   try
                   {
                      ftp.setPort(400);
                      failed("veto = true, no exception");
                   }
                   catch (Exception e)
                   {
                      if (exceptionIs(e, "PropertyVetoException"))
                      {
                         if (ftp.getPort() == 300)
                            succeeded();
                         else
                            failed("bad value after change vetoed" + ftp.getPort());
                      }
                      else
                      {
                          failed("veto event bad");
                      }
                   }
                }
                else
                {
                   failed("veto data bad");
                }
             }
             else
             {
                failed("no veto change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }





    // ---------------------------------------------------------------------
    //  Connect event
    // ---------------------------------------------------------------------
    public void Var009()
    {
       try
       {
          beansCleanup();

          AS400FTP c = new AS400FTP(systemObject_);
          c.addFTPListener(this);
          c.connect();

          if (ftpEvent != null)
          {
             if (ftpEvent.getID() == FTPEvent.FTP_CONNECTED)
             {
                c.disconnect();
                beansCleanup();
                c.removeFTPListener(this);
                c.connect();

                if (ftpEvent == null)
                   succeeded();
                else
                   failed("event still thrown after removing it from the list");
             }
             else
                failed("event incorrect");
          }
          else
            failed("No event");
       }

       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }
    }


    // ---------------------------------------------------------------------
    //  disonnect event
    // ---------------------------------------------------------------------
    public void Var010()
    {
       try
       {
          beansCleanup();

          AS400FTP c = new AS400FTP(systemObject_);
          c.connect();
          c.addFTPListener(this);
          c.disconnect();

          if (ftpEvent != null)
          {
             if (ftpEvent.getID() == FTPEvent.FTP_DISCONNECTED)
             {
                beansCleanup();
                c.removeFTPListener(this);
                c.connect();
                c.disconnect();

                if (ftpEvent == null)
                   succeeded();
                else
                   failed("event still thrown after removing it from the list");
             }
             else
                failed("event incorrect");
          }
          else
            failed("No event");
       }

       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }
    }



    // ---------------------------------------------------------------------
    //  list event
    // ---------------------------------------------------------------------
    public void Var011()
    {
       try
       {
          beansCleanup();

          AS400FTP c = new AS400FTP(systemObject_);
          c.addFTPListener(this);
          c.ls();

          if (ftpEvent != null)
          {
             if (ftpEvent.getID() == FTPEvent.FTP_LISTED)
             {
                beansCleanup();
                c.dir();

                if (ftpEvent != null)
                {
                   if (ftpEvent.getID() == FTPEvent.FTP_LISTED)
                   {
                      beansCleanup();
                      c.removeFTPListener(this);
                      c.ls();

                      if (ftpEvent == null)
                         succeeded();
                      else
                         failed("event still thrown after removing it from the list");
                   }
                   else
                      failed("event incorrect (dir)");
                }
                else
                   failed("No event (dir)");
             }
             else
                failed("event incorrect (ls)");
          }
          else
            failed("No event (ls)");
       }

       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }
    }



    // ---------------------------------------------------------------------
    //  Gif
    // ---------------------------------------------------------------------
    public void Var012()
    {
	
      if (!JTOpenTestEnvironment.isGuiAvailable) {
	    notApplicable("-- Cannot load Icon when running natively");
      }
      else
      {
	StringBuffer sb = new StringBuffer(); 
        try
        {
          Class<?> beanclass = Class.forName("com.ibm.as400.access.AS400FTP");
          BeanInfo beaninfo = Introspector.getBeanInfo(beanclass);

          // Icons / GUI components no longer available in JTOpen 20.0.X



          EventSetDescriptor[] event = beaninfo.getEventSetDescriptors();
          PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();

          Vector<String> v = new Vector<String>();
          v.addElement("system");
          v.addElement("port");
          v.addElement("server");
          v.addElement("user");
          v.addElement("password");
          v.addElement("bufferSize");
	  // v.addElement("propertyChange");
          v.addElement("FTPEvent");
          // v.addElement("vetoableChange");
          v.addElement("saveFilePublicAuthority");

	  sb.append("\n"); 
          for (int i = 0; i < props.length; i++)
          {
            PropertyDescriptor p = props[i];

            if (! p.isHidden())
            {
              sb.append("Removing property ["+i+"] "+p.getDisplayName()+" from vector\n"); 
              v.removeElement(p.getDisplayName());
	    } else {
		sb.append("Not removing hidden property ["+i+"] "+p.getDisplayName()+" from vector\n"); 

	    } 
          }

          for (int i = 0; i < event.length; i++)
          {
            EventSetDescriptor e = event[i];
	    sb.append("Removing event["+i+"] "+e.getDisplayName()+" from vector\n"); 
            v.removeElement(e.getDisplayName());
          }

          if (v.isEmpty())
            succeeded();
          else
          {
            while (! v.isEmpty())
            {
              sb.append("Remaining element  " + v.elementAt(0)+"\n");
              v.removeElementAt(0);
            }

            failed("vector is not empty: "+sb.toString());
          }
        }
        catch(Exception e)
        {
          output_.println("NOTE: If running on i5 Client verify onAS400_=true");
          failed(e, "Unexpected exception occurred.");
        }
      }
    }



    // ---------------------------------------------------------------------
    //  Serialization
    // ---------------------------------------------------------------------
    public void Var013()
    {
       try
       {
          beansCleanup();

          AS400 system2 = new AS400("SysA", "UsrB", "PwdC".toCharArray());
          AS400FTP c = new AS400FTP(system2);
          c.setPort(100);
          c.setSaveFilePublicAuthority("*CHANGE");
          c.addFTPListener(this);

          FileOutputStream f = new FileOutputStream("Ftp.ser");
          ObjectOutput s = new ObjectOutputStream(f);
          s.writeObject(c);
          s.flush();
          s.close(); 
          FileInputStream in = new FileInputStream("Ftp.ser");
          ObjectInputStream s2 = new ObjectInputStream(in);
          AS400FTP c2 = (AS400FTP) s2.readObject();
          s2.close(); 
          if (c.getSystem().getSystemName().equals(c2.getSystem().getSystemName()))
                if (c2.getPort() == 100)
                {
                   if (c2.getSaveFilePublicAuthority().equals("*CHANGE"))
                   {
                      succeeded();
                      c2.addFTPListener(this);
                      c2.addVetoableChangeListener(this);
                      c2.addPropertyChangeListener(this);
                   }
                   else
                      failed("authority wrong " + c2.getSaveFilePublicAuthority());
                }
                else
                   failed("port wrong " + c2.getPort());
          else
             failed("system wrong " + c2.getServer());
       }
       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }

       try
       {
          File f = new File("Ftp.ser");
          f.delete();
       }
       catch (Exception e) {}

    }



    // ---------------------------------------------------------------------
    //  Serialization
    // ---------------------------------------------------------------------
    public void Var014()
    {
       succeeded();
    }




    // ---------------------------------------------------------------------
    //  Property Change - bufferSize
    // ---------------------------------------------------------------------
    public void Var015()
    {
          try
          {
             beansCleanup();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.addPropertyChangeListener(this);

             if (ftp.getBufferSize() != 4096)
             {
                failed("default buffer size incorrect " + ftp.getBufferSize());
                return;
             }

             ftp.setBufferSize(200);


             if (propertyChangeEvent != null)
             {
                Integer ov = (Integer) propertyChangeEvent.getOldValue();
                Integer nv = (Integer) propertyChangeEvent.getNewValue();
                if (  (propertyChangeEvent.getPropertyName().equals("bufferSize"))
                    &&(ov.intValue() == 4096)
                    &&(nv.intValue() ==  200))
                {
                   beansCleanup();
                   ftp.removePropertyChangeListener(this);

                   ftp.setBufferSize(4000);

                   if (propertyChangeEvent == null)
                   {
                      succeeded();
                   }
                   else
                   {
                      failed("listener not removed");
                   }
                }
                else
                {
                   failed("property event bad");
                }
             }
             else
             {
                failed("no property change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }





    // ---------------------------------------------------------------------
    //  Veto Change - buffer size
    // ---------------------------------------------------------------------
    public void Var016()
    {
          try
          {
             beansCleanup();

             AS400FTP ftp = new AS400FTP(systemObject_);
             ftp.setBufferSize(5000);
             ftp.addVetoableChangeListener(this);

             ftp.setBufferSize(10000);

             if (vetoChangeEvent != null)
             {
                Integer ov = (Integer) vetoChangeEvent.getOldValue();
                Integer nv = (Integer) vetoChangeEvent.getNewValue();
                if (  (vetoChangeEvent.getPropertyName().equals("bufferSize"))
                    &&(ov.intValue() == 5000)
                    &&(nv.intValue() == 10000))
                {
                   veto = true;
                   try
                   {
                      ftp.setBufferSize(400);
                      failed("veto = true, no exception");
                   }
                   catch (Exception e)
                   {
                      if (exceptionIs(e, "PropertyVetoException"))
                      {
                         if (ftp.getBufferSize() == 10000)
                            succeeded();
                         else
                            failed("bad value after change vetoed " + ftp.getBufferSize());
                      }
                      else
                      {
                          failed("veto event bad 1");
                      }
                   }
                }
                else
                {
                   failed("veto data bad 2");
                }
             }
             else
             {
                failed("no veto change event ");
             }
          }
          catch(Exception e)
          {
             failed(e, "Unexpected exception occurred.");
          }
    }







}



