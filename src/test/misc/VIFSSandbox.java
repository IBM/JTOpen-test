
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  VIFSSandbox.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.misc;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ConvTableReader;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.IFSFileOutputStream;
import com.ibm.as400.access.IFSTextFileInputStream;
import com.ibm.as400.access.IFSTextFileOutputStream;



/**
The VIFSSandbox class manages a sandbox for IFS testing.
**/
public class VIFSSandbox
{



/**
Setting this to false can make testing much quicker, but it eliminates
cleanup.
**/
    public boolean              cleanup             = true;



/**
Debugging messages.
**/
    public boolean              debug               = false;
    public boolean              debugError          = false;



    // Private data.
    private Vector<IFSFile>     directories_        = new Vector<IFSFile> ();
    private Vector<IFSFile>     files_              = new Vector<IFSFile> ();
    private AS400               system_             = null;
    private AS400               pwrSys_             = null;
    private static final String topName_            = "vaccess";
    private IFSFile             top1_               = null;
    private IFSFile             top2_               = null;

    private String              defaultEncoding_;  // @A1a
    private int                 defaultCcsid_;     // @A1a


/**
Constructor.
**/
    public VIFSSandbox (AS400 system, String topLevel)
    {
        system_ = system;
        createTopLevelDirectories (topLevel);
    }

/**
Constructor.
**/
    public VIFSSandbox (AS400 system, AS400 pwrSystem, String topLevel)
    {
        system_ = system;
        pwrSys_ = pwrSystem;
        createTopLevelDirectories (topLevel);
    }



/**
Appends to a file.
**/
    public void appendFile (IFSFile file, int size)
    {
        try {
            IFSFileOutputStream out = new IFSFileOutputStream (system_,
                file.getPath (), IFSFileOutputStream.SHARE_ALL, true, defaultCcsid_);
            byte[] bytes = new byte[size];
            out.write (bytes);
            out.close ();
        }
        catch (Exception e) {
            if (debugError)
                System.out.println ("Could not append to file " + file.getPath () + ".");
        }
    }



/**
Appends to a file.
**/
    public void appendFile (IFSFile file, String text)
    {
        appendFile(file, text, defaultEncoding_);  // @A1c
// @A1d
///        try {
///            IFSTextFileOutputStream out = new IFSTextFileOutputStream (system_,
///                file.getPath (), IFSTextFileOutputStream.SHARE_ALL, true);
///            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (out));
///            writer.write (text);
///            writer.close ();
///            out.close ();
///        }
///        catch (Exception e) {
///            if (debugError)
///                System.out.println ("Could not append to file " + file.getPath () + ".");
///        }
    }



/**
Appends to a file.
**/
    public void appendFile (IFSFile file, String text, String encoding)
    {
        try {
            int ccsid = (new CharConverter(encoding)).getCcsid();
            IFSTextFileOutputStream out = new IFSTextFileOutputStream (system_,
                file.getPath (), IFSTextFileOutputStream.SHARE_ALL, true, ccsid);
            BufferedWriter writer = new BufferedWriter (new OutputStreamWriter (out,encoding));
            writer.write (text);
            writer.close ();
            out.close ();
        }
        catch (Exception e) {
            if (debugError)
                System.out.println ("Could not append to file " + file.getPath () + ".");
        }
    }



/**
Cleans up after the variations.
**/
    public void cleanup ()
    {
        if (cleanup) {
            for (int i = files_.size () - 1; i >= 0; --i)
                deleteFile ((IFSFile) files_.elementAt (i));

            for (int i = directories_.size () - 1; i >= 0; --i)
                deleteDirectory ((IFSFile) directories_.elementAt (i));
        }
        else
            System.out.println ("Warning: No cleanup of directory " + top2_.getPath()
                + " was done.");
    }



/**
Creates a directory.
**/
    public IFSFile createDirectory (String path)
    {
        return createDirectory (path, true);
    }



/**
Creates a directory.
**/
    public IFSFile createDirectory (String path, boolean cleanup)
    {
        IFSFile directory = new IFSFile (system_, top2_, path);
        createDirectory (directory, cleanup);
        return directory;
    }



/**
Creates a directory.
**/
    public void createDirectory (IFSFile directory)
    {
        createDirectory (directory, true);
    }



/**
Creates a directory.
**/
    public void createDirectory (IFSFile directory, boolean cleanup)
    {
        boolean success = true;
        try {
            if (! directory.exists ()) {

                if (debug)
                    System.out.println ("Creating directory " + directory.getPath () + ".");

                success = directory.mkdir ();
            }
        }
        catch (Exception e) {
            success = false;
        }

        if (cleanup == true) {
            if (! directories_.contains (directory))
                directories_.addElement (directory);
        }

        if (success == false)
            if (debugError)
                System.out.println ("Could not create directory " + directory.getPath () + ".");
    }



/**
Creates a file.
**/
    public IFSFile createFile (String path)
    {
        return createFile (path, true);
    }



/**
Creates a file.
**/
    public IFSFile createFile (String path, boolean cleanup)
    {
        IFSFile file = new IFSFile (system_, top2_, path);
        createFile (file, cleanup);
        return file;
    }



/**
Creates a file.
**/
    public void createFile (IFSFile file)
    {
        createFile (file, true);
    }



/**
Creates a file.
**/
    public void createFile (IFSFile file, boolean cleanup)
    {
        createFile (file, cleanup, defaultEncoding_);  // @A1c
// @A1d
///        boolean success = true;
///        try {
///            if (debug)
///                System.out.println ("Creating file " + file.getPath () + ".");
///
///            IFSFileOutputStream out = new IFSFileOutputStream (system_, file.getPath ());
///            OutputStreamWriter writer = new OutputStreamWriter (out);
///            writer.write ("");
///            out.close ();
///        }
///        catch (Exception e) {
///            success = false;
///        }
///
///        if (cleanup == true) {
///            if (! files_.contains (file))
///                files_.addElement (file);
///        }
///
///        if (success == false)
///            if (debugError)
///                System.out.println ("Could not create file " + file.getPath () + ".");
    }



/**
Creates a file.
**/
    public void createFile (IFSFile file, boolean cleanup, String encoding)
    {
        boolean success = true;
        try {
            if (debug)
                System.out.println ("Creating file " + file.getPath () + ".");

            deleteFile(file);
            int ccsid = (new CharConverter(encoding)).getCcsid();
            IFSFileOutputStream out = new IFSFileOutputStream (system_, file.getPath (), ccsid);
            OutputStreamWriter writer = new OutputStreamWriter (out,encoding);
            writer.write ("");
            out.close ();
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        if (cleanup == true) {
            if (! files_.contains (file))
                files_.addElement (file);
        }

        if (success == false)
            if (debugError)
                System.out.println ("Could not create file " + file.getPath () + ".");
    }



/**
Creates top-level directories.
**/
    void createTopLevelDirectories (String topLevel)
    {
      // Create top1 if necessary.
      top1_ = new IFSFile (system_, topName_);
      createDirectory (top1_, true);

      // Create top2.
      top2_ = new IFSFile (system_, top1_, topLevel);
      createDirectory (top2_);

      defaultEncoding_ = System.getProperty("file.encoding");  // @A1a
      try { defaultCcsid_ = (new CharConverter(defaultEncoding_)).getCcsid(); }
      catch (Exception e) { e.printStackTrace(); }
    }



/**
Creates directories with miscellaneouss subdirectories.
**/
    public IFSFile createMiscDirectories (String path)
    {
        IFSFile directory = createDirectory (path);
        String[] miscSubdirectories = { "Clif", "Raj", "Lisa", "Patty", "Scott", "Russ" };
        for (int i = 0; i < miscSubdirectories.length; ++i)
            createDirectory (new IFSFile (system_, directory, miscSubdirectories[i]));
        return directory;
    }



/**
Creates directories with numbered subdirectories.
**/
    public IFSFile createNumberedDirectories (String path, int count)
    {
        IFSFile directory = createDirectory (path);
        for (int i = 0; i < count; ++i)
            createDirectory (new IFSFile (system_, directory, Integer.toString (i)));
        return directory;
    }



/**
Creates directories with numbered subdirectories and files.
**/
    public IFSFile createNumberedDirectoriesAndFiles (String path, int directoryCount, int fileCount)
    {
        IFSFile directory = createDirectory (path);
        for (int i = 0; i < directoryCount; ++i)
            createDirectory (new IFSFile (system_, directory, "D" + Integer.toString (i)));
        for (int i = 0; i < fileCount; ++i)
            createFile (new IFSFile (system_, directory, "F" + Integer.toString (i)));
        return directory;
    }



/**
Deletes a directory.
**/
    public void deleteDirectory (IFSFile directory)
    {
        try {
            if (debug)
                System.out.println ("Deleting directory " + directory.getPath () + ".");

            directory.setReadOnly(false);
            directory.delete ();
        }
        catch (Exception e) {
            if (debugError)
                System.out.println ("Could not delete directory " + directory.getPath () + ".");
        }
    }



/**
Deletes a file.
**/
    public void deleteFile (String path)
    {
        AS400 sys = ( pwrSys_ == null ? system_ : pwrSys_);
        IFSFile file = new IFSFile (sys, top2_, path);
        deleteFile (file);
    }



/**
Deletes a file.
**/
    public void deleteFile (IFSFile file)
    {
        boolean success = true;
        boolean existed = false;
        boolean deleted = false;
        try {
            if (debug)
                System.out.println ("Deleting file " + file.getPath () + ".");

            existed = file.exists();
            if (debug)
              System.out.println ("File " + file.getPath () + (existed ? " existed." : " didn't exist."));
            file.setReadOnly(false);
            deleted = file.delete();
            if (existed && !deleted && (pwrSys_ != null)) {
              // Try using a more-powerful profile, to delete the file.
              if (debug)
                System.out.println ("Deleting file " + file.getPath () + " using profile " + pwrSys_.getUserId());
              IFSFile file2 = new IFSFile(pwrSys_, file.getPath());
              deleted = file2.delete();
            }
            success = !existed || deleted;
        }
        catch (Exception e) {
            success = false;
            if (debugError) e.printStackTrace();
        }

        if ((success == false) && (debugError))
                System.out.println ("Could not delete file " + file.getPath () + ".");
    }



/**
Returns the top level directory.
**/
    public String getTopLevel ()
    {
        return top2_.getPath ();
    }



/**
Reads a file.
**/
    public String readFile (IFSFile file)
    {
        StringBuffer text = new StringBuffer ();

        try {
            // Open the input stream.
            IFSTextFileInputStream input = new IFSTextFileInputStream (file.getSystem(),
                file, IFSTextFileInputStream.SHARE_ALL);
            ///BufferedReader reader = new BufferedReader (new InputStreamReader (input));  // @A1d
            ConvTableReader reader = new ConvTableReader (input,file.getCCSID()); // @A1c

            // Load the contents.
            char[] charArray = new char[512];
            int count = 0;
            // int position = 0;

            while (true) {
                count = reader.read (charArray, 0, 512);
                if (count > 0) {
                    text.append (new String (charArray, 0, count));
                    // position += count;
                }
                else
                    break;
            }

            // Close the input stream.
            reader.close ();
            input.close ();
        }
        catch (Exception e) {
            text.append ("Exception[" + e.getMessage() + "]");
        }

        return text.toString ();
    }



/**
Sets the access for a file.
**/
    public void setAccess (IFSFile file, boolean read, boolean write)
    {
        AS400 system = file.getSystem ();

        StringBuffer buffer = new StringBuffer ();
        buffer.append ("CHGAUT OBJ('");
        buffer.append (file.getPath ());
        buffer.append ("') USER(");
        buffer.append (system.getUserId ());
        buffer.append (") DTAAUT(");
        if (read) {
            if (write)
                buffer.append ("*RW");
            else
                buffer.append ("*R");
        }
        else {
            if (write)
                buffer.append ("*W");
            else
                buffer.append ("*NONE");
        }
        buffer.append (") OBJAUT(*OBJMGT)");

        CommandCall commandCall = new CommandCall (file.getSystem (),
            buffer.toString ());
        boolean success = true;
        try {
            success = commandCall.run ();
        }
        catch (Exception e) {
            success = false;
        }

        if (success == false) {
            if (debugError) {
                System.out.println ("Could not change access to file " + file.getPath () + ".");

                AS400Message[] messageList = commandCall.getMessageList ();
                for (int i = 0; i < messageList.length; ++i)
                    System.out.println (messageList[i]);
            }
        }
    }



}


