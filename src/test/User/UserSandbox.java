///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  UserSandbox.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.User;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import java.net.InetAddress;
import java.util.Vector;

/**
 The UserSandbox class manages a sandbox for User and UserList testing.
 **/
public class UserSandbox
{
    /**
     Setting this to false can make testing much quicker, but it eliminates cleanup.
     **/
    public boolean cleanup = true;

    /**
     Debugging messages.
     **/
    public boolean debug = false;

    private static int count_ = 0;
    private static final String description_ = "-Dept 48T-JT400-User Testing";
    private Vector<String> directoryEntries_ = new Vector<String>();
    private Vector<String> groups_ = new Vector<String>();
    private String prefix_ = null;
    private AS400 system_ = null;
    private Vector<String> users_ = new Vector<String>();

    /**
     Constructor.
     **/
    public UserSandbox(AS400 system, String prefix)
    {
        system_ = system;
        prefix_ = prefix;
    }

    /**
     Cleans up after the variations.
     @exception  Exception  If an exception occurs.
     **/
    public void cleanup() throws Exception
    {
        if (cleanup)
        {
            for (int i = directoryEntries_.size() - 1; i >= 0; --i)
            {
                deleteDirectoryEntry(directoryEntries_.elementAt(i));
            }

            for (int i = users_.size() - 1; i >= 0; --i)
            {
                deleteUser(users_.elementAt(i));
            }

            for (int i = groups_.size() - 1; i >= 0; --i)
            {
                deleteUser(groups_.elementAt(i));
            }
        }
        else
        {
            System.out.println("Warning: No cleanup of users and groups was done.");
        }
    }

    /**
     Creates a group.
     @exception  Exception  If an exception occurs.
     **/
    public String createGroup() throws Exception
    {
        return createGroup(-1);
    }

    /**
     Creates a group.
     @exception  Exception  If an exception occurs.
     **/
    public String createGroup(long gid) throws Exception
    {
        boolean success = false;
        String name;
        name = getNextName();

        if (system_ == null)
        {
            throw new Exception("Power user not specified.");
        }

        if (debug)
        {
            System.out.println("Creating group " + name + ".");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("CRTUSRPRF USRPRF(");
        buffer.append(name);
        buffer.append(") PASSWORD(JTEAM1) TEXT('");
        buffer.append(name);  // Several variations depend on the description starting with the name.
        buffer.append(description_);
        buffer.append("') GID(");
        if (gid == -1)
        {
            buffer.append("*GEN");
        }
        else
        {
            buffer.append(gid);
        }
        buffer.append(")");

        CommandCall cmd = new CommandCall(system_, buffer.toString());
        try
        {
            success = cmd.run();
        }
        catch (Exception e)
        {
            success = false;
        }

        if (success == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            for (int i = 0; i < messageList.length; ++i)
            {
                System.out.println(messageList[i]);
            }
        }

        groups_.addElement(name);

        return name;
    }

    /**
     Creates a group and a number of users in it.
     @exception  Exception  If an exception occurs.
     **/
    public String[] createGroupAndUsers(int numberOfUsers) throws Exception
    {
        String groupName = createGroup();
        String[] groupAndUsers = new String[numberOfUsers + 1];
        groupAndUsers[0] = groupName;
        for (int i = 1; i <= numberOfUsers; ++i)
        {
            groupAndUsers[i] = createUser(groupName);
        }
        return groupAndUsers;
    }

    /**
     Creates a user.
     @exception  Exception  If an exception occurs.
     **/
    public String createUser() throws Exception
    {
        return createUser(null, false);
    }

    /**
     Creates a user.
     @exception  Exception  If an exception occurs.
     **/
    public String createUser(boolean directory) throws Exception
    {
        return createUser(null, directory, true);
    }

    public String createUser(boolean directory, boolean cleanupNeeded) throws Exception
    {
        return createUser((String)null, directory, cleanupNeeded);
    }

    /**
     Creates a user in a group.
     @exception  Exception  If an exception occurs.
     **/
    public String createUser(String groupName) throws Exception
    {
        return createUser(groupName, false, true);
    }

    /**
     Creates a user in a group.
     @exception  Exception  If an exception occurs.
     **/
    public String createUser(String groupName, boolean directory) throws Exception
    {
        return createUser(groupName, directory, true);
    }

    public String createUser(String groupName, boolean directory, boolean cleanupNeeded) throws Exception
    {
        String name = getNextName();

        if (system_ == null)
        {
            throw new Exception("Power user not specified.");
        }

        if (debug)
        {
            System.out.print("Creating user " + name);
            if (groupName != null) System.out.print(" in group " + groupName);
            System.out.println(".");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("CRTUSRPRF USRPRF(");
        buffer.append(name);
        buffer.append(") PASSWORD(JTEAM1) TEXT('");
        buffer.append(name);  // Several variations depend on the description starting with the name.
        buffer.append(description_);
        buffer.append("')");
        if (groupName != null)
        {
            buffer.append(" GRPPRF(");
            buffer.append(groupName);
            buffer.append(")");
        }

        CommandCall cmd = new CommandCall(system_, buffer.toString());
        boolean success;
        try
        {
            success = cmd.run();
        }
        catch (Exception e)
        {
            success = false;
        }

        if (success == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            for (int i = 0; i < messageList.length; ++i)
            {
                System.out.println(messageList[i]);
            }
            System.out.println("CRTUSRPRF failed to create user " + name + ".");
        }

        if (cleanupNeeded)
        {
            users_.addElement(name);
        }

        if (directory && success)
        {
            buffer = new StringBuffer();
            buffer.append("ADDDIRE USRID(");
            buffer.append(name);
            buffer.append(' ');
	    String hostName; 
            if (system_.isLocal())
            {
                hostName = InetAddress.getLocalHost().getHostName();
            }
            else
            {
                hostName = system_.getSystemName();
            }
	    int dot = hostName.indexOf('.');
	    if (dot >= 0) hostName = hostName.substring(0, dot);
	    buffer.append(hostName);

            buffer.append(") USRD('IBM Toolbox for Java Testing') USER(");
            buffer.append(name);
            buffer.append(")");

            cmd = new CommandCall(system_, buffer.toString());
            try
            {
                success = cmd.run();
            }
            catch (Exception e)
            {
                success = false;
            }

            if (success == false)
            {
                AS400Message[] messageList = cmd.getMessageList();
                for (int i = 0; i < messageList.length; ++i)
                {
                    System.out.println(messageList[i]);
                }
                System.out.println("ADDDIRE failed to create directory entry " + name + ".");
            }

            if (cleanupNeeded)
            {
                directoryEntries_.addElement(name);
            }
        }

        return name;
    }

    /**
     Deletes a directory entry.
     @exception  Exception  If an exception occurs.
     **/
    public void deleteDirectoryEntry(String name) throws Exception
    {
        if (system_ == null)
        {
            System.out.println("Power user not specified.");
            return;
        }

        if (debug)
        {
            System.out.println("Deleting user or group " + name + ".");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("RMVDIRE USRID(");
        buffer.append(name);
        buffer.append(' ');

        if (system_.isLocal())
        {
            String hostName = InetAddress.getLocalHost().getHostName();
            int dot = hostName.indexOf('.');
            if (dot >= 0) hostName = hostName.substring(0, dot);
            buffer.append(hostName);
        }
        else
        {
            buffer.append(system_.getSystemName());
        }
        buffer.append(")");

        CommandCall cmd = new CommandCall(system_, buffer.toString());
        boolean success;
        try
        {
            success = cmd.run();
        }
        catch (Exception e)
        {
            success = false;
        }

        if (success == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            for (int i = 0; i < messageList.length; ++i)
            {
                System.out.println(messageList[i]);
            }
            System.out.println("RMVDIRE failed to delete directory entry " + name + ".");
        }
    }

    /**
     Deletes a user.
     **/
    public void deleteUser(String name)
    {
        deleteUser(name, 0);
    }

    /**
     Deletes a user.
     **/
    public void deleteUser(String name, int deleteStatus)
    {
        if (system_ == null)
        {
            System.out.println("Power user not specified.");
            return;
        }

        if (debug)
        {
            System.out.println("Deleting user or group " + name + ".");
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("DLTUSRPRF USRPRF(");
        buffer.append(name);
        buffer.append(") OWNOBJOPT(*DLT)");

        CommandCall cmd = new CommandCall(system_, buffer.toString());
        boolean success;
        try
        {
            success = cmd.run();
        }
        catch (Exception e)
        {
            success = false;
        }

        if (success == false)
        {
            AS400Message[] messageList = cmd.getMessageList();
            for (int i = 0; i < messageList.length; ++i)
            {
                System.out.println(messageList[i]);
            }
            System.out.println("DLTUSRPRF failed to delete user " + name + ".");
        }

        switch (deleteStatus)
        {
            case 1:
                users_.removeElement(name);
        }
    }

    /**
     Gets the next user profile name.
     **/
    private String getNextName()
    {
        int nextCount = count_++;
        String suffix = "00" + nextCount;

        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix_);
        buffer.append(suffix.substring(suffix.length() - 3));

        return buffer.toString();
    }
}
