///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  PgmBeans.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.Pgm;

import java.io.*;
import java.beans.*;
import com.ibm.as400.access.*;

import test.Testcase;

/**
 Testcase PgmBeans.
 <ul>
 </ul>
 **/
public class PgmBeans extends Testcase
implements PropertyChangeListener, VetoableChangeListener,
ActionCompletedListener
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "PgmBeans";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.PgmTest.main(newArgs); 
   }
    String propertyName;
    Object oldValue;
    Object newValue;
    Object source;
    boolean veto_ = false;
    String propName;
    Object oValue;
    Object nValue;
    Object src;
    Object asource;
    PropertyChangeEvent propChange;
    PropertyChangeEvent vetoChange;
    PropertyChangeEvent vetoRefire;

    String goodPgm_ = "/QSYS.LIB/W95LIB.LIB/PROG3.PGM";
    String Prog25_ = "/QSYS.LIB/W95LIB.LIB/PROG25.PGM";
    String result3 = "Testing 3 Parameters";
    ProgramParameter[] parmlist_;
    byte[] data0;
    byte[] data1;
    ProgramParameter parm0, parm1, parm2;
    AS400Bin2 int16 = new AS400Bin2();

    ProgramParameter[] buildParms()
    {
        parmlist_ = new ProgramParameter[3];

        data0 = new byte[5];
        data0[0] = 7;
        data0[1] = 6;
        parmlist_[0] = parm0 = new ProgramParameter(data0, 500);

        data1 = new byte[2];
        data1[0] = (byte)4;
        data1[1] = (byte)2;
        parmlist_[1] = parm1 = new ProgramParameter(data1);

        parmlist_[2] = parm2 = new ProgramParameter(2);

        return parmlist_;
    }

    void verifyProg3( ProgramCall pgm )
    {
        // Verify no messages returned
        AS400Message[] msglist = pgm.getMessageList();
        if (msglist.length!=0 )
        {
            failed("message received " + msglist[0]); return;
        }

        // Verify parameter list
        ProgramParameter[] parmlist = pgm.getParameterList();
        if (parmlist != parmlist_)
        {
            failed("parameter list changed" ); return;
        }
        if (parmlist[0] != parm0)
        {
            failed("parameter 0 changed" ); return;
        }
        if (parmlist[1] != parm1)
        {
            failed("parameter 1 changed" ); return;
        }
        if (parmlist[2] != parm2)
        {
            failed("parameter 2 changed" ); return;
        }

        // Verify each parm
        // parm 0 still inout
        if (parmlist[0].getInputData() != data0)
        {
            failed("parameter 0 data changed" ); return;
        }
        if (parmlist[0].getOutputDataLength() != 500)
        {
            failed("parameter 0 output data length changed"
                   + parmlist[0].getOutputDataLength()); return;
        }
        // parm 0 result string
        AS400Text xlater = new AS400Text( result3.length(), 37, systemObject_ );
        String retp0 =
          (String)xlater.toObject(pgm.getParameterList()[0].getOutputData(), 0 );
        if (!retp0.equals("Testing 3 Parameters"))
        {
            failed("parameter 0 wrong value " + retp0 ); return;
        }

        // parm 1 still input
        if (parmlist[1].getInputData() != data1)
        {
            failed("parameter 1 data changed" ); return;
        }
        if (parmlist[1].getOutputDataLength() != 0)
        {
            failed("parameter 1 output data length set"
                   + parmlist[1].getOutputDataLength()); return;
        }
        if (parmlist[1].getOutputData() != null
            && parmlist[1].getOutputData().length != 0)
        {
            failed("parameter 1 output data set" ); return;
        }

        // parm 2 still output
        if (parmlist[2].getInputData() != null
            && parmlist[2].getInputData().length != 0)
        {
            failed("parameter 2 data changed" ); return;
        }
        if (parmlist[2].getOutputDataLength() != 2)
        {
            failed("parameter 2 output data length changed"
                   + parmlist[2].getOutputDataLength()); return;
        }
        // parm 2 = parm1+1
        if (1027 != int16.toShort( pgm.getParameterList()[2].getOutputData(), 0))
        {
            failed("parameter 2 wrong value " + int16.toShort( pgm.getParameterList()[2].getOutputData(), 0) ); return;
        }

        // Verify system not changed
        if (pgm.getSystem()!=systemObject_)
        {
            failed("system changed " +pgm.getSystem().getSystemName() ); return;
        }

        // Verify program not changed
        if (false==pgm.getProgram().equals(goodPgm_))
        {
            failed("program changed " +pgm.getProgram() ); return;
        }
        succeeded();
    }

    void resetValues()
    {
        veto_ = false;
        propChange = null;
        vetoChange = null;
        vetoRefire = null;

        propertyName = null;
        oldValue = null;
        newValue = null;
        source = null;
        propName = null;
        oValue = null;
        nValue = null;
        src = null;
        asource = null;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        if (propChange!=null)
            System.out.println( "propertyChange refired!" );
        propChange = e;
        propertyName = e.getPropertyName();
        oldValue = e.getOldValue();
        newValue = e.getNewValue();
        source = e.getSource();
    }

    public void vetoableChange(PropertyChangeEvent e)
      throws PropertyVetoException
    {
        if (vetoChange!=null)
        {
            if (vetoRefire!=null)
                System.out.println( "vetoableChange refired!" );
            else
                vetoRefire = e;
        }
        else
            vetoChange = e;
        propName = e.getPropertyName();
        oValue = e.getOldValue();
        nValue = e.getNewValue();
        src = e.getSource();

        if (veto_)
        {
            throw new PropertyVetoException("Property vetoed", e);
        }
    }

    public void actionCompleted(ActionCompletedEvent e)
    {
        if (asource!=null)
            System.out.println( "actionCompleted refired!" );
        asource = e.getSource();
    }

    /**
     Create a program object,
     run with program and parameters,
     verify parameter list values are returned correctly,
     no messages are returned,
     and system and program are not changed.
     **/
    public void Var001()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.addActionCompletedListener( this );

            resetValues();
            if (!pgm.run(goodPgm_, buildParms() ))
            {
                failed("program failed " + pgm.getMessageList()[0].getText() ); return;
            }
            // verify event fired
            if (asource!=pgm)
            {
                failed("actionCompleted not called with source "+asource);
                return;
            }

            verifyProg3( pgm );
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Run an invalid program and make sure an exception is returned
     and event not fired.
     **/
    public void Var002()
    {
        ProgramCall pgm = new ProgramCall(systemObject_);;
        pgm.addActionCompletedListener( this );

        resetValues();
        try
        {
            pgm.run("/QSYS.LIB/W95LIB.LIB/xxxx.PGM", buildParms());
            failed("program returned");
        }
        catch (Exception e)
        {
            if (exceptionIs(e, "ObjectDoesNotExistException"))
            {
                if (pgm==null
                    ||  pgm.getMessageList()==null
                    ||  pgm.getMessageList().length != 1)  // @B1C
                {
                    failed( "incorrect number of messages returned" ); return;
                }
                else if (asource!=null)
                {
                    failed("actionCompleted called with source "+asource);
                    return;
                }
                succeeded();
            }
            else
            {
                failed(e, "Incorrect exception");
            }
        }
    }

    /*
     Run program with bad system, verify AS400SecurityException
     and event not fired.
     */
    public void Var003()
    {
        try
        {
            ///AS400 as400 = new AS400();
            AS400 as400 = null;
            if (System.getProperty("os.name").equals("OS/400"))
            {
                as400 = new AS400(systemObject_.getSystemName(), "dummyUser",
                                  "dummyPass".toCharArray());
            }
            else // not native AS/400 JVM
            {
                as400 = new AS400();
            }
            as400.setGuiAvailable(false);
            ProgramCall pgm = new ProgramCall(as400, goodPgm_, buildParms() );
            pgm.addActionCompletedListener( this );
            resetValues();
            pgm.run();
            failed("security exception not thrown");
        }
        catch (Exception e)
        {   
            if (!exceptionIs(e, "AS400SecurityException"))
            {
                failed("AS400Security exception not fired");
                return;
            }
            // verify event not fired
            if (propChange!=null)
            {
                failed("actionCompleted called with source "+asource);
                return;
            }
            succeeded();
        }
    }

    /**
     PROPERTY CHANGE TESTING
     **/
    public boolean baseVerifyPropChange( String prop,
                                         Object oldV,
                                         Object newV,
                                         Object sourceV )
    {
        if (null==propChange.getPropertyName())
            failed("propertyName is null");
        else if (false==propChange.getPropertyName().equals(prop))
            failed("propertyName " + prop
                   + ", expected " + propChange.getPropertyName());
        else if (oldV!=propChange.getOldValue())
            failed("old value " + oldV
                   + ", expected " + propChange.getOldValue());
        else if (newV!=propChange.getNewValue())
            failed("new value " + newV
                   + ", expected " + propChange.getNewValue());
        else if (sourceV!=propChange.getSource())
            failed("source " + sourceV
                   + ", expected " + propChange.getSource());
        else
            return true;
        return false;
    }
    public boolean verifyPropChange( String prop,
                                     Object oldV,
                                     Object newV,
                                     Object sourceV,
                                     Object curV)
    {
        if (true==baseVerifyPropChange( prop, oldV, newV, sourceV ))
        {
            if (newV!=curV)
                failed("changed value " + curV
                       + ", expected " + newV);
            else
                return true;
        }
        return false;
    }

    /**
     setProgram
     **/
    public void Var004()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addPropertyChangeListener( this );
        resetValues();
        try
        {
            pgm.setProgram( Prog25_ );
            if (verifyPropChange( "program", goodPgm_, Prog25_, pgm, pgm.getProgram() ))
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
    /**
     setSystem
     **/
    public void Var005()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addPropertyChangeListener( this );
        resetValues();
        try
        {
            AS400 newsys = new AS400();
            pgm.setSystem( newsys );
            if (verifyPropChange( "system", systemObject_, newsys, pgm, pgm.getSystem() )==true)
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }


    /**
     setParameterList
     **/
    public void Var006()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addPropertyChangeListener( this );
        resetValues();
        try
        {
            ProgramParameter[] newparms = new ProgramParameter[4];
            pgm.setParameterList( newparms );
            if (verifyPropChange( "parameterList", parmlist_, newparms, pgm, pgm.getParameterList() )==true)
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     VETOABLE CHANGE TESTING
     **/
    public boolean verifyVetoChange( String prop,
                                     Object oldV,
                                     Object newV,
                                     Object sourceV,
                                     Object curV)
    {
        if (vetoChange==null)
        {
            failed("no veto change event");
            return false;
        }
        else if (propChange!=null)
        {
            failed("property change fired as well as veto");
            return false;
        }
        propChange=vetoChange;
        if (true==baseVerifyPropChange( prop, oldV, newV, sourceV ))
        {
            Object checkV = (veto_ ? oldV : newV );
            if (checkV!=curV)
                failed("changed value " + curV
                       + ", expected " + checkV);
            else
                return true;
        }
        return false;
    }

    // *
    // * don't veto
    // *
    /**
     setProgram
     **/
    public void Var007()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        try
        {
            pgm.setProgram( Prog25_ );
            if (verifyVetoChange( "program", goodPgm_, Prog25_, pgm, pgm.getProgram() ))
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
    /**
     setSystem
     **/
    public void Var008()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        try
        {
            AS400 newsys = new AS400();
            pgm.setSystem( newsys );
            if (verifyVetoChange( "system", systemObject_, newsys, pgm, pgm.getSystem() )==true)
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }
    /**
     setParameterList
     **/
    public void Var009()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        try
        {
            ProgramParameter[] newparms = new ProgramParameter[4];
            pgm.setParameterList( newparms );
            if (verifyVetoChange( "parameterList", parmlist_, newparms, pgm, pgm.getParameterList() )==true)
                succeeded();
        }
        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    // *
    // * veto
    // *
    /**
     setProgram
     **/
    public void Var010()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        veto_ = true;
        try
        {
            pgm.setProgram( Prog25_ );
        }
        catch (Exception e)
        {
            if (verifyVetoChange( "program", goodPgm_, Prog25_, pgm, pgm.getProgram() ))
                succeeded();
        }
    }
    /**
     setSystem
     **/
    public void Var011()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        veto_ = true;
        AS400 newsys = new AS400();
        try
        {
            pgm.setSystem( newsys );
        }
        catch (Exception e)
        {
            if (verifyVetoChange( "system", systemObject_, newsys, pgm, pgm.getSystem() )==true)
                succeeded();
        }
    }
    /**
     setParameterList
     **/
    public void Var012()
    {
        ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms() );
        pgm.addVetoableChangeListener( this );
        resetValues();
        veto_ = true;
        ProgramParameter[] newparms = new ProgramParameter[4];
        try
        {
            pgm.setParameterList( newparms );
        }
        catch (Exception e)
        {
            if (verifyVetoChange( "parameterList", parmlist_, newparms, pgm, pgm.getParameterList() )==true)
                succeeded();
        }
    }


    /**
     Test serialization
     **/
    public void Var013()
    {
        try
        {
            {   
                ProgramCall pgm = new ProgramCall(systemObject_, goodPgm_, buildParms());
                pgm.addActionCompletedListener( this );

                // Serialize cmd to a file.
                FileOutputStream f = new FileOutputStream("pgm.ser");
                ObjectOutput  s  =  new  ObjectOutputStream(f);
                s.writeObject( pgm );
                s.flush();
                s.close(); 
                // Deserialize a string and date from a file.
                FileInputStream in = new FileInputStream("pgm.ser");
                ObjectInputStream s2 = new ObjectInputStream(in);
                ProgramCall pgm2 = (ProgramCall)s2.readObject();
                s2.close(); 
                if (false==pgm2.getProgram().equals( pgm.getProgram() ) )
                {
                    failed( "Program changed to " + pgm2.getProgram() );
                    return;
                }
                else if (pgm2.getParameterList().length !=
                         pgm.getParameterList().length )
                {
                    failed( "Parameter list length changed to " + pgm2.getParameterList().length );
                    return;
                }
                else if (false==pgm2.getSystem().getSystemName()
                         .equals( pgm2.getSystem().getSystemName() ))
                {
                    failed( "system changed to " + pgm2.getSystem().getSystemName() );
                    return;
                }
                else if (false==pgm2.getSystem().getUserId()
                         .equals( pgm2.getSystem().getUserId() ))
                {
                    failed( "user changed to " + pgm2.getSystem().getUserId() );
                    return;
                }
                pgm = pgm2;
                pgm.setSystem( systemObject_ ); // need pwd
                // for verifyProg3
                parmlist_ = pgm.getParameterList();
                parm0 = parmlist_[0];
                parm1 = parmlist_[1];
                parm2 = parmlist_[2];
                data0 = parm0.getInputData();
                data1 = parm1.getInputData();

                resetValues();
                if (!pgm.run())
                {
                    failed("program failed " + pgm.getMessageList()[0].getText() ); return;
                }
                // verify event *NOT* fired
                if (asource!=null)
                {
                    failed("actionCompleted called with source "+asource);
                    return;
                }

                verifyProg3( pgm );
            }
        }

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        catch (NoClassDefFoundError e)
        { // Tolerate missing RJob class.
            if (e.getMessage().indexOf("RJob") != -1) {
                failed("Class not found: RJob");
            }
            else failed(e, "Unexpected exception");
        }

    }

    /**
     Create a program object,
     Add a Action Complete Listener
     Remove that same Action Completed Listener
     **/

    public void Var014()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.addActionCompletedListener( this );
            pgm.removeActionCompletedListener( this );
            succeeded();
        }

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Removes a Property Change Listener from the Jelly Beans list.
     **/

    public void Var015()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.addPropertyChangeListener( this );
            pgm.removePropertyChangeListener( this );
            succeeded();
        }

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Removes a Vetoable Change Listener from the Jelly Beans list.
     **/

    public void Var016()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.addVetoableChangeListener( this );
            pgm.removeVetoableChangeListener( this );
            succeeded();
        }

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
    }

    /**
     Get the object String
     **/

    public void Var017()
    {
        try
        {
            ProgramCall pgm = new ProgramCall(systemObject_);
            pgm.toString();
            succeeded();

        }

        catch (Exception e)
        {
            failed(e, "Unexpected exception");
        }
        finally
        {
            try {
                File f = new File( "pgm.ser" );
                f.delete();
            } catch (Throwable e) {}
        }
    }
}
