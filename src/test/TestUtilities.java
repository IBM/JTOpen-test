///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  TestUtilities.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;

import com.ibm.as400.access.AS400;
//import javax.swing.JFrame;
//import javax.swing.JOptionPane;
//import java.awt.BorderLayout;
//import java.awt.Component;
//import java.awt.Frame;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Enumeration;



/**
The TestUtilities class provides some common routines for
testing visual components.
**/
public class TestUtilities
{



    // MRI.
    private static final String cancelException_        = "Attended variation cancelled.";
    private static final String testInstructions_       = "Test instructions";
    private static final String title_                  = "AS/400 Toolbox for Java";



    // Private data.
/// private static JFrame       defaultFrame_           = new JFrame (); // This is needed to fix a Swing 0.7 bug,
                                                                         // where a null parent frame is not allowed.
    private static final String serializeFilename_      = "TestUtilities.ser";



/**
Indicates if two arrays are equal.

@param  array1      One array.
@param  array2      The other array.
@return             true if the arrays are equals, false otherwise.
**/
    public static boolean arrayCompare (Object[] array1, Object[] array2)
    {
        if (array1.length != array2.length)
            return false;

        for (int i = 0; i < array1.length; ++i)
            if (! (array1[i].equals (array2[i])))
                return false;

        return true;
    }



/**
Indicates if two arrays are equal.

@param  array1      One array.
@param  array2      The other array.
@return             true if the arrays are equals, false otherwise.
**/
    public static boolean arrayCompare (byte[] array1, byte[] array2)
    {
        if (array1.length != array2.length)
            return false;

        for (int i = 0; i < array1.length; ++i)
            if (array1[i] != array2[i])
                return false;

        return true;
    }



/**
Indicates if an element is contained in an array.

@param  array       The array.
@param  element     The element.
@return             true if the element is contained in the array,
                    false otherwise.
**/
    public static boolean arrayContains (Object[] array, Object element)
    {
        for (int i = 0; i < array.length; ++i)
            if (array[i] == element)
                return true;
        return false;
    }



/**
Displays some test instructions for a question and waits
for either the Yes or No button.

@param instructions The question.
@return             true for Yes, false for No.
**/
/*  public static boolean ask (String question)
        throws Exception
    {
        int response = JOptionPane.showConfirmDialog (defaultFrame_,
            formatInstructions (question), testInstructions_,
            JOptionPane.YES_NO_CANCEL_OPTION);
        switch (response) {
            case JOptionPane.YES_OPTION:
                return true;
            case JOptionPane.NO_OPTION:
                return false;
            default:
                throw new Exception (cancelException_);
        }
*/      /*
        TestInstructions ti = new TestInstructions (question,
            TestInstructions.YES_NO_CANCEL);
        int response = ti.display ();
        switch (response) {
            case TestInstructions.YES_PRESSED:
                return true;
            case TestInstructions.NO_PRESSED:
                return false;
            default:
                throw new Exception (cancelException_);
        }
        */
/// }



/**
Checks an enumeration against an array.

@param  e       The enumeration.
@param  array   The array.
@return         true if the enumeration is the same as the array.
**/
    public static boolean checkEnumeration (Enumeration e,
                                            Object[] array)
    {
        for (int i = 0; i < array.length; ++i) {
            if (e.hasMoreElements () == false)
                return false;
            if (e.nextElement () != array[i])
                return false;
        }
        return true;
    }



/**
Creates a frame with a component in it.

@param component The component.
@return          The frame.
**/
/*  public static Frame createFrame (Component component)
    {
        JFrame frame = new JFrame (title_);
        final JFrame frame2 = frame;
        frame.addWindowListener (new WindowAdapter () {
            public void windowClosing (WindowEvent event) {
                frame2.dispose ();
            }});
        frame.getContentPane ().setLayout (new BorderLayout ());
        frame.getContentPane ().add ("Center", component);
        frame.pack ();
        frame.show ();

        // This is a great opportunity to set the parent frame
        // for test instruction dialogs.  Swing says that it
        // should place such dialogs so they don't overlap the
        // parent frame, but instead it centers it on top of the
        // parent frame.  So for now, we leave the default frame
        // to be null.
        //
        // defaultFrame_ = frame;

        return frame;
    } */



/**
Adds \n's at the right places so that strings don't get too long
(and dialogs don't get too wide).
**/
    private static String formatInstructions (String input)
    {
        StringBuffer output = new StringBuffer (input);

        final int width = 80;
        int current = 0;
        int i = width;

        while (i < output.length ()) {
            if (current >= width) {
                while (output.charAt (i) != ' ')
                    --i;
                output.setCharAt (i, '\n');
                current = 0;
            }

            else if (output.charAt (i) == '\n') {
                current = 0;
            }

            ++current;
            ++i;
        }

        return output.toString ();
    }



/**
Displays some test instructions and waits for the Ok button.

@param instructions The test instructions.
**/
/*  public static void instruct (String instructions)
        throws Exception
    {
        int response = JOptionPane.showConfirmDialog (defaultFrame_,
            formatInstructions (instructions), testInstructions_,
            JOptionPane.OK_CANCEL_OPTION);
        if (response != JOptionPane.OK_OPTION)
            throw new Exception (cancelException_);
*/      /*
        TestInstructions ti = new TestInstructions (instructions
            + "\n\nClick Ok when you are done.",
            TestInstructions.OK);
        ti.display ();
        */
/// }



/**
Creates a one element array.

@param element  The element.
@return         The array.
**/
    public static boolean[] makeArray (boolean element)
    {
        boolean[] array = { element };
        return array;
    }



/**
Creates a one element array.

@param element  The element.
@return         The array.
**/
    public static Object[] makeArray (Object element)
    {
        Object[] array = { element };
        return array;
    }



/**
Serializes and deserializes an object.

@param  object  The object.
@return         The deserialized object.
**/
    public static Object serialize (Object object)
        throws Exception
    {
	    // Serialize.
	    ObjectOutput out = new ObjectOutputStream (new FileOutputStream (serializeFilename_));
	    out.writeObject (object);
	    out.flush ();

        // Deserialize.
        Object object2 = null;
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream (serializeFilename_));
            object2 = in.readObject ();
        }
   	    finally {
       		File f = new File (serializeFilename_);
        	f.delete();
   	    }

   	    return object2;
   	}


}



