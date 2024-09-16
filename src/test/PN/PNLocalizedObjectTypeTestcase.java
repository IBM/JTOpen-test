///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PNLocalizedObjectTypeTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.PN;

import com.ibm.as400.access.QSYSObjectTypeTable;

import test.Testcase;

/**
 Testcase PNLocalizedObjectTypeTestcase.  This tests the following methods of the QSYSObjectTypeTable class:
 <ul>
 <li>getLocalizedObjectType()
 <li>getSupportedObjectTypes()
 <li>getSupportedExtendedAttributes()
 </ul>
 **/
public class PNLocalizedObjectTypeTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PNLocalizedObjectTypeTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PNTest.main(newArgs); 
   }
    // Indicates if an array contains a given element.
    // @param  array  The array.
    // @param  element  The element.
    // @return  true if the array contains the element, false otherwise.
    private static boolean arrayContains(Object[] array, Object element)
    {
        for (int i = 0; i < array.length; ++i)
        {
            if (array[i].equals(element)) return true;
        }
        return false;
    }

    /**
     getLocalizedObjectType() with 1 argument - Pass null.
     **/
    public void Var001()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    // @A1C
    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to an empty string.
     **/
    public void Var002()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a single *.
     **/
    public void Var003()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("*");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a bogus value.
     **/
    public void Var004()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("DARIN");
            assertCondition (loc.equals("DARIN"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a valid value,
     all uppercase, with a *.
     **/
    public void Var005()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("*FILE");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a valid value,
     all uppercase, without a *.
     **/
    public void Var006()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("MSGQ");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a valid value,
     mixed case, with a *.
     **/
    public void Var007()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("*pGM");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - When the object type is set to a valid value,
     mixed case, without a *.
     **/
    public void Var008()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("OutQ");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 1 argument - Try with every supported object type.
     **/
    public void Var009()
    {
        try {
            String[] supportedTypes = QSYSObjectTypeTable.getSupportedObjectTypes();
            String failed = null;
            for(int i = 0; i < supportedTypes.length; ++i) {
                String loc = QSYSObjectTypeTable.getLocalizedObjectType(supportedTypes[i]);
                if (loc == null)
                    failed = supportedTypes[i];
            }
            assertCondition(failed == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - Pass null for object type, but specify
     an attribute.
     **/
    public void Var010()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType(null, "SOMETHING");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - Pass null for object type and attribute.
     **/
    public void Var011()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType(null, null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that does not require an attribute, and don't pass one.
     **/
    public void Var012()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("OUTQ", null);
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that does not require an attribute, and we pass the empty string.
     **/
    public void Var013()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("OUTQ", "");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that does not require an attribute, and we pass one.
     **/
    public void Var014()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("OUTQ", "NONSENSE");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and don't pass one.
     **/
    public void Var015()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("FILE", null);
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and we pass the empty string.
     **/
    public void Var016()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("PGM", "");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    // @A1C
    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and it is not one that allows user-defined attributes 
     and we pass a bogus one.
     **/
    public void Var017()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("FILE", "SMITH");
            assertCondition (!loc.endsWith("SMITH"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    // @A1A
    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and it is one that allows user-defined attributes 
     and we pass a bogus one.
     **/
    public void Var018()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("USRIDX", "SMITH");
            assertCondition (loc.endsWith(" - SMITH"));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and we pass a valid one, all uppercase.
     **/
    public void Var019()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("PGM", "CLP");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - When the object type is set to a valid value,
     that can take an attribute, and we pass a valid one,  mixed case.
     **/
    public void Var020()
    {
        try {
            String loc = QSYSObjectTypeTable.getLocalizedObjectType("FILE", "sAVf");
            assertCondition (loc != null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getLocalizedObjectType() with 2 arguments - Try with every supported object type and attribute combination.
     **/
    public void Var021()
    {
        try {
            String[] supportedTypes = QSYSObjectTypeTable.getSupportedObjectTypes();
            String failed = null;
            for(int i = 0; i < supportedTypes.length; ++i) {
                String loc = QSYSObjectTypeTable.getLocalizedObjectType(supportedTypes[i], null);
                // System.out.println(":" + supportedTypes[i] + "=" + loc + ":");
                if (loc == null)
                    failed = supportedTypes[i];
                String[] extendedAttributes = QSYSObjectTypeTable.getSupportedAttributes(supportedTypes[i]);
                for(int j = 0; j < extendedAttributes.length; ++j) {
                    loc = QSYSObjectTypeTable.getLocalizedObjectType(supportedTypes[i], extendedAttributes[j]);
                    if (loc == null)
                        failed = supportedTypes[i] + "," + extendedAttributes[j];
                    // System.out.println(":" + supportedTypes[i] + "," + extendedAttributes[j] + "=" + loc + ":");
                }
            }
            assertCondition(failed == null);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getSupportedObjectTypes() - Should work.  Verify some favorite object types.
     **/
    public void Var022()
    {
        try {
            String[] supportedObjectTypes = QSYSObjectTypeTable.getSupportedObjectTypes();
            assertCondition((supportedObjectTypes.length > 25) // Don't hardcode a value, so that we
                            // don't have to update it every time
                            // we update the list.
                            && (arrayContains(supportedObjectTypes, "FILE"))
                            && (arrayContains(supportedObjectTypes, "DTAARA"))
                            && (!arrayContains(supportedObjectTypes, "POPSICLE")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getSupportedExtendedAttributes() - Should not work when passed null.  
     **/
    public void Var023()
    {
        try {
            String[] supportedEAs = QSYSObjectTypeTable.getSupportedAttributes(null);
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.NullPointerException");
        }
    }



    /**
     getSupportedExtendedAttributes() - Should return an empty array
     when passed something that is a type with no attributes.  
     **/
    public void Var024()
    {
        try {
            String[] supportedEAs = QSYSObjectTypeTable.getSupportedAttributes("DTAARA");
            assertCondition(supportedEAs.length == 0);
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getSupportedExtendedAttributes() - Should return an empty array
     when passed something that is not a type.  
     **/
    public void Var025()
    {
        try {
            String[] supportedEAs = QSYSObjectTypeTable.getSupportedAttributes("YOGURT");
            failed ("Didn't throw exception");
        }
        catch(Exception e) {
            assertExceptionIsInstanceOf (e, "java.lang.IllegalArgumentException");
        }
    }



    /**
     getSupportedExtendedAttributes() - Should work when passed "FILE".  
     Verify some favorite attributes.
     **/
    public void Var026()
    {
        try {
            String[] supportedEAs = QSYSObjectTypeTable.getSupportedAttributes("FILE");
            assertCondition((supportedEAs.length > 5) // Don't hardcode a value, so that we
                            // don't have to update it every time
                            // we update the list.
                            && (arrayContains(supportedEAs, "PF"))
                            && (arrayContains(supportedEAs, "PRTF"))
                            && (!arrayContains(supportedEAs, "ICECREAM")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



    /**
     getSupportedExtendedAttributes() - Should work when passed "PGM".  
     Verify some favorite attributes.
     **/
    public void Var027()
    {
        try {
            String[] supportedEAs = QSYSObjectTypeTable.getSupportedAttributes("PGM");
            assertCondition((supportedEAs.length > 5) // Don't hardcode a value, so that we
                            // don't have to update it every time
                            // we update the list.
                            && (arrayContains(supportedEAs, "PLI"))
                            && (arrayContains(supportedEAs, "C"))
                            && (!arrayContains(supportedEAs, "BOMBPOP")));
        }
        catch (Exception e) {
            failed (e, "Unexpected Exception");
        }
    }



}




