///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  FTPBeans.java
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
import test.Testcase;

import java.beans.*;

import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.lang.Integer;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.awt.Image;


public class FTPBeans
             extends Testcase
             implements FTPListener, VetoableChangeListener, PropertyChangeListener

{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "FTPBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.FTPTest.main(newArgs); 
   }
    private String user_     = null;
    private String password_ = null;
    private String system_   = null;
    private String testDirectory = "FTPTestDir";
    private String initialToken_ = null;
    private boolean notWorthTrying = false;

    private static final int DETAILED = 1;
    private static final int NAME_ONLY = 0;

    private static final int ONE     =  1;
    private static final int TWO     =  2;
    private static final int FOUR    =  4;
    private static final int EIGHT   =  8;
    private static final int SIXTEEN = 16;
    private static final int TOTAL   = ONE + TWO + FOUR + EIGHT + SIXTEEN;

    private int connectCalled    = 0;
    private int disconnectCalled = 0;
    private int getCalled        = 0;
    private int putCalled        = 0;
    private int listCalled       = 0;
    private FTPEvent ftpEvent;
    private PropertyChangeEvent propertyChangeEvent;
    private PropertyChangeEvent vetoChangeEvent;
    private boolean veto = false;
    private String operatingSystem_;
    private boolean windows_;
    private boolean linux_;
    private boolean aix_;

    public FTPBeans (AS400 systemObject,
                           Hashtable namesAndVars,
                           int runMode,
                           FileOutputStream fileOutputStream,
                           
                           String password,
                           String userid,
                           String initialToken)
    {
        super (systemObject,
               "FTPBeans",
               namesAndVars,
               runMode,
               fileOutputStream,
               password);


        initialToken_ = initialToken;

        if (initialToken_ == null)
        {
           System.out.println("-directory is invalid, no test will be run");
           notWorthTrying = true;
           System.out.println();
        }
        else
        {
          if (FTPTest.DEBUG) {
            System.out.println("using initial token " + initialToken_);
            System.out.println();
          }
        }


        if (systemObject_ != null)
        {
           user_     = userid;
           password_ = password;
           system_   = systemObject_.getSystemName();
        }

        if (FTPTest.DEBUG) System.out.println();

        if ((user_ == null) || (user_.length() < 1))
           System.out.println("===> warning, variations will fail because no -uid specified");

        if ((password_ == null) || (password_.length() < 1))
           System.out.println("===> warning, variations will fail because no -password specified");

        if ((system_ == null) || (system_.length() < 1))
           System.out.println("===> warning, variations will fail because no -system specified");

        if (FTPTest.DEBUG) System.out.println();

    }


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
          try
          {
             beansCleanup();

             FTP ftp = new FTP("System", "User", "Password");
             ftp.addPropertyChangeListener(this);

             ftp.setUser("fred");

             if (propertyChangeEvent != null)
             {
                if (  (propertyChangeEvent.getPropertyName().equals("user"))
                    &&(propertyChangeEvent.getOldValue().equals("User"))
                    &&(propertyChangeEvent.getNewValue().equals("fred")))
                {
                   beansCleanup();
                   ftp.removePropertyChangeListener(this);

                   ftp.setUser("Martha");

                   if (propertyChangeEvent == null)
                   {
                      try
                      {
                         ftp.removePropertyChangeListener(null);
                         failed("no remove(null), no exception");
                      }
                      catch (Exception e)
                      {
                         if (exceptionIs(e, "NullPointerException"))
                         {
                            succeeded();
                         }
                         else
                         {
                            failed("remove(null), exception not correct");
                         }
                      }
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
    //  Property Change test cases - server
    // ---------------------------------------------------------------------
    public void Var002()
    {
          try
          {
             beansCleanup();

             FTP ftp = new FTP("System", "User", "Password");
             ftp.addPropertyChangeListener(this);

             ftp.setServer("fred");

             if (propertyChangeEvent != null)
             {
                if (  (propertyChangeEvent.getPropertyName().equals("server"))
                    &&(propertyChangeEvent.getOldValue().equals("System"))
                    &&(propertyChangeEvent.getNewValue().equals("fred")))
                {
                   beansCleanup();
                   ftp.removePropertyChangeListener(this);

                   ftp.setServer("Martha");

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

             FTP ftp = new FTP("System", "User", "Password");
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
          try
          {
             beansCleanup();

             FTP ftp = new FTP("System", "User", "Password");
             ftp.addVetoableChangeListener(this);

             ftp.setUser("fred");

             if (vetoChangeEvent != null)
             {
                if (  (vetoChangeEvent.getPropertyName().equals("user"))
                    &&(vetoChangeEvent.getOldValue().equals("User"))
                    &&(vetoChangeEvent.getNewValue().equals("fred")))
                {
                   veto = true;
                   try
                   {
                      ftp.setUser("Fred");
                      failed("veto = true, no exception");
                   }
                   catch (Exception e)
                   {
                      if (exceptionIs(e, "PropertyVetoException"))
                      {
                         beansCleanup();
                         ftp.removeVetoableChangeListener(this);

                         ftp.setUser("Martha");

                         if (vetoChangeEvent == null)
                         {
                            try
                            {
                               ftp.removeVetoableChangeListener(null);
                               failed("no remove(null), no exception");
                            }
                            catch (Exception e2)
                            {
                               if (exceptionIs(e2, "NullPointerException"))
                               {
                                  succeeded();
                               }
                               else
                               {
                                  failed("remove(null), exception not correct");
                               }
                            }
                         }
                         else
                         {
                            failed("listener not removed");
                         }
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
    //  Veto Change test cases - server
    // ---------------------------------------------------------------------
    public void Var006()
    {
          try
          {
             beansCleanup();

             FTP ftp = new FTP("System", "User", "Password");
             ftp.addVetoableChangeListener(this);

             ftp.setServer("fred");

             if (vetoChangeEvent != null)
             {
                if (  (vetoChangeEvent.getPropertyName().equals("server"))
                    &&(vetoChangeEvent.getOldValue().equals("System"))
                    &&(vetoChangeEvent.getNewValue().equals("fred")))
                {
                   veto = true;
                   try
                   {
                      ftp.setServer("Martha");
                      failed("veto = true, no exception");
                   }
                   catch (Exception e)
                   {
                      if (exceptionIs(e, "PropertyVetoException"))
                      {
                         if (ftp.getServer().equals("fred"))
                            succeeded();
                         else
                            failed("bad value after change vetoed");
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

             FTP ftp = new FTP("System", "User", "Password");
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

          FTP c = new FTP(system_, user_, password_);
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

          FTP c = new FTP(system_, user_, password_);
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
	if (checkNotGroupTest()) {
       try
       {
          beansCleanup();
	  System.out.println("FTPBeans.var11: creating FTP "); 
          FTP c = new FTP(system_, user_, password_);
          c.addFTPListener(this);
	  System.out.println("FTPBeans.var11: calling ls.  If this hangs, check QGPL for DDM files  "); 
          c.ls();
	  System.out.println("FTPBeans.var11: back from ls "); 
          if (ftpEvent != null)
          {
             if (ftpEvent.getID() == FTPEvent.FTP_LISTED)
             {
                beansCleanup();
		System.out.println("FTPBeans.var11: calling dir"); 
                c.dir();
		System.out.println("FTPBeans.var11:  back from dir"); 
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
    }



    // ---------------------------------------------------------------------
    //  Gif
    // ---------------------------------------------------------------------
    public void Var012()
    {
      if (onAS400_) {
	    notApplicable("-- Cannot load Icon when running natively");
      }
      else
      {
	StringBuffer sb = new StringBuffer(); 
        try
        {
          Class beanclass = Class.forName("com.ibm.as400.access.FTP");
          BeanInfo beaninfo = Introspector.getBeanInfo(beanclass);
          // Icons / GUI components no longer available in JTOpen 20.0.X


          EventSetDescriptor[] event = beaninfo.getEventSetDescriptors();
          PropertyDescriptor[] props = beaninfo.getPropertyDescriptors();

          Vector v = new Vector();
          v.addElement("port");
          v.addElement("server");
          v.addElement("user");
          v.addElement("password");
          v.addElement("bufferSize");
          // v.addElement("propertyChange");
          v.addElement("FTPEvent");
          v.addElement("vetoableChange");

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
          System.out.println("NOTE: If running on i5 Client verify onAS400_=true");
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

          FTP c = new FTP("SysA", "UsrB", "PwdC");
          c.setPort(100);
          c.addFTPListener(this);

          FileOutputStream f = new FileOutputStream("FtpClient.ser");
          ObjectOutput s = new ObjectOutputStream(f);
          s.writeObject(c);
          s.flush();

          FileInputStream in = new FileInputStream("FtpClient.ser");
          ObjectInputStream s2 = new ObjectInputStream(in);
          FTP c2 = (FTP) s2.readObject();

          if (c.getServer().equals(c2.getServer()))
             if (c.getUser().equals(c2.getUser()))
                if (c.getPort() == 100)
                {
                   succeeded();
                   c2.addFTPListener(this);
                   c2.addVetoableChangeListener(this);
                   c2.addPropertyChangeListener(this);
                }
                else
                   failed("port wrong " + c2.getPort());
             else
                failed("user wrong " + c2.getUser());
          else
             failed("system wrong " + c2.getServer());
       }
       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }

       try
       {
          File f = new File("FtpClient.ser");
          f.delete();
       }
       catch (Exception e) {}

    }



    // ---------------------------------------------------------------------
    //  Serialization
    // ---------------------------------------------------------------------
    public void Var014()
    {
       try
       {
          beansCleanup();

          FTP c = new FTP(system_, user_, password_);
          c.ls();

          FileOutputStream f = new FileOutputStream("FtpClient2.ser");
          ObjectOutput s = new ObjectOutputStream(f);
          s.writeObject(c);
          s.flush();

          FileInputStream in = new FileInputStream("FtpClient2.ser");
          ObjectInputStream s2 = new ObjectInputStream(in);
          FTP c2 = (FTP) s2.readObject();
          c2.setPassword(password_);

          c2.ls();
          succeeded();
       }
       catch(Exception e)
       {
          failed(e, "Unexpected exception occurred.");
       }

       try
       {
          File f = new File("FtpClient2.ser");
          f.delete();
       }
       catch (Exception e) {}
    }




    // ---------------------------------------------------------------------
    //  Property Change - bufferSize
    // ---------------------------------------------------------------------
    public void Var015()
    {
          try
          {
             beansCleanup();

             FTP ftp = new FTP("System", "User", "Password");
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

             FTP ftp = new FTP("System", "User", "Password");
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



