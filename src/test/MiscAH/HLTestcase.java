///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  HLTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.ExtendedIllegalArgumentException;
import com.ibm.as400.access.HistoryLog;
import com.ibm.as400.access.Job;
import com.ibm.as400.access.JobList;
import com.ibm.as400.access.QueuedMessage;

import test.Testcase;

/**
 * Testcase HLTestcase.
 */
public class HLTestcase extends Testcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "HLTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.HLTest.main(newArgs); 
   }
	
	boolean compareObjects(Object object1, Object object2){
		if(object1 instanceof String[])
		{
			String[] o1 = (String[])object1;
			String[] o2 = (String[])object2;
			if(o1.length != o2.length)
				return false;
			for(int i=0; i<o1.length; i++)
				if(!o1[i].equals(o2[i]))
					return false;
			return true;
		}
		else if(object1 instanceof Job[])
		{
			Job[] o1 = (Job[])object1;
			Job[] o2 = (Job[])object2;
			if(o1.length != o2.length)
				return false;
			for(int i=0; i<o1.length; i++)
				if(!o1[i].getName().equals(o2[i].getName()) || !o1[i].getUser().equals(o2[i].getUser()) || !o1[i].getNumber().equals(o2[i].getNumber()))
					return false;
			return true;
		}
		return false;
	}
	
	/**
     * Converts the specified Date object to a String in the format CYYMMDDHHMMSS.
     * @param date
     * @return the date represented in the format CYYMMDD
     */
    private String dateToString(Date date){
    	StringBuffer buffer = new StringBuffer(13);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int year = calendar.get(Calendar.YEAR);
    	buffer.append((year<2000) ? '0' : '1');
    	buffer.append(twoDigits(year % 100));
    	buffer.append(twoDigits(calendar.get(Calendar.MONTH) + 1));
    	buffer.append(twoDigits(calendar.get(Calendar.DAY_OF_MONTH)));
    	buffer.append(twoDigits(calendar.get(Calendar.HOUR_OF_DAY)));
    	buffer.append(twoDigits(calendar.get(Calendar.MINUTE)));
    	buffer.append(twoDigits(calendar.get(Calendar.SECOND)));
    	return buffer.toString();
    }
    
    /**
    Returns a 2 digit String representation of the value.  
    The value will be 0-padded on the left if needed.

    @param value    The value.
    @return         The 2 digit String representation.
    **/
    private String twoDigits(int value)
    {
    	if (value > 99)
    		throw new ExtendedIllegalArgumentException("value", ExtendedIllegalArgumentException.PARAMETER_VALUE_NOT_VALID);
    	
    	String full = "00" + Integer.toString(value);
    	return full.substring(full.length() - 2);
    }
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::HistoryLog(AS400).</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct initial state of the object.</dd>
	 * </dl>
	 */
	public void Var001() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getSystem() == systemObject_);
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}

	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::HistoryLog(null).</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify that a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var002() {
		try {
			HistoryLog hl = new HistoryLog(null);
			failed("Did not throw exception."+hl);
		} catch (Exception e) {
			assertExceptionIs(e, "NullPointerException", "system");
		}
	}

	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with CURRENT_DATE.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date is set.</dd>
	 * </dl>
	 */
	public void Var003() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate(HistoryLog.CURRENT_DATE, HistoryLog.AVAIL);
			assertCondition(hl.getStartingDate() == null);
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}

	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with BEGIN.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date is set.</dd>
	 * </dl>
	 */
	public void Var004() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate(HistoryLog.BEGIN, HistoryLog.AVAIL);
			assertCondition(hl.getStartingDate() == null);
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a valid date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date is set.</dd>
	 * </dl>
	 */
	public void Var005() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 0, 0, 0);
			assertCondition(hl.getStartingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a valid date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date is set.</dd>
	 * </dl>
	 */
	public void Var006() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("1060625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(2006, 5, 25, 0, 0, 0);
			assertCondition(hl.getStartingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with AVAIL.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting time is set.</dd>
	 * </dl>
	 */
	public void Var007() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 0, 0, 0);
			assertCondition(hl.getStartingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a valid time.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting time is set.</dd>
	 * </dl>
	 */
	public void Var008() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", "062555");
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 6, 25, 55);
			assertCondition(hl.getStartingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
    /**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a date that is too long.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var009() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("08006259", "062555");
			failed("Did not throw exception.");
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a time that is too long.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var010() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", "05562555");
			failed("Did not throw exception.");
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a date is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var011() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("08006", "062555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a date is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var012() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("MYDATE", "062555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a time that is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var013() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", "2555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{ 
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(String, String) with a time that is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var014() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate("0800625", "MyTime");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(null)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify that a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var015() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setStartingDate(null);
			failed("No exception.");
		} catch (Exception e) {
			assertExceptionIs(e, "NullPointerException", "date");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setStartingDate(Date) with a valid Date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date and time is set.</dd>
	 * </dl>
	 */
	public void Var016() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 6, 25, 55);
			hl.setStartingDate(cal.getTime());
			assertCondition(hl.getStartingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getStartingDate() before a date has been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify null is returned.</dd>
	 * </dl>
	 */
	public void Var017() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getStartingDate() == null);
		}
		catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with CURRENT_DATE.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending date is set.</dd>
	 * </dl>
	 */
	public void Var018() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate(HistoryLog.CURRENT_DATE, HistoryLog.AVAIL);
			assertCondition(hl.getEndingDate() == null);
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}

	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with END.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending date is set.</dd>
	 * </dl>
	 */
	public void Var019() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate(HistoryLog.END, HistoryLog.AVAIL);
			assertCondition(hl.getEndingDate() == null);
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a valid date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending date is set.</dd>
	 * </dl>
	 */
	public void Var020() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 0, 0, 0);
			assertCondition(hl.getEndingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a valid date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending date is set.</dd>
	 * </dl>
	 */
	public void Var021() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("1060625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(2006, 5, 25, 0, 0, 0);
			assertCondition(hl.getEndingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with AVAIL.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending time is set.</dd>
	 * </dl>
	 */
	public void Var022() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", HistoryLog.AVAIL);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 0, 0, 0);
			assertCondition(hl.getEndingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a valid time.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct ending time is set.</dd>
	 * </dl>
	 */
	public void Var023() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", "062555");
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 6, 25, 55);
			assertCondition(hl.getEndingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
    /**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a date that is too long.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var024() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("08006259", "062555");
			failed("Did not throw exception.");
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a time that is too long.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var025() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", "05562555");
			failed("Did not throw exception.");
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a date is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var026() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("08006", "062555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a date is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var027() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("MYDATE", "062555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a time that is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var028() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", "2555");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(String, String) with a time that is not valid.</dd>
	 * <dd>Call HistoryLog::getMessages() to verify the API throws an exception.
	 * <dt>Result:</dt>
	 * <dd>Verify an AS400Exception is thrown.</dd>
	 * </dl>
	 */
	public void Var029() {
		try {
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate("0800625", "MyTime");
			Enumeration messages = hl.getMessages();
			failed("Did not throw exception."+messages);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		} catch (Exception e) {
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.AS400Exception");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(null)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify that a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var030() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setEndingDate(null);
			failed("No exception.");
		} catch (Exception e) {
			assertExceptionIs(e, "NullPointerException", "date");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setEndingDate(Date) with a valid Date.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct starting date and time is set.</dd>
	 * </dl>
	 */
	public void Var031() {
		try {
			HistoryLog hl = new HistoryLog(systemObject_);
			Calendar cal = Calendar.getInstance();
			cal.set(1980, 5, 25, 6, 25, 55);
			hl.setEndingDate(cal.getTime());
			assertCondition(hl.getEndingDate().toString().equals(cal.getTime().toString()));
		} catch (Exception e) {
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getEndingDate() before a date has been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify null is returned.</dd>
	 * </dl>
	 */
	public void Var032() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getEndingDate() == null);
		}
		catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(null)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var033() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDs(null);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIs(e, "NullPointerException", "ids");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with valid ids.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the message ids are set.</dd>
	 * </dl>
	 */
	public void Var034() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD1234", "CPD3344", "CPD4544"};
			hl.setMessageIDs(ids);
			String[] messageIds = hl.getMessageIDs();
			assertCondition(compareObjects(ids, messageIds));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with an array with a length greater than 100.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var035() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[101];
			hl.setMessageIDs(ids);
			failed("Did not throw exception.");
		}catch(Exception e)
		{
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is longer than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var036() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD12345"};
			hl.setMessageIDs(ids);
			failed("Did not throw exception.");
		}catch(Exception e)
		{
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is longer than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var037() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD12345", "CPD4567"};
			hl.setMessageIDs(ids);
			failed("Did not throw exception.");
		}catch(Exception e)
		{
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is longer than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var038() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD1234", "CPD12345"};
			hl.setMessageIDs(ids);
			failed("Did not throw exception.");
		}catch(Exception e)
		{
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is less than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify 0's are appended to the message id.</dd>
	 * </dl>
	 */
	public void Var039() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD"};
			hl.setMessageIDs(ids);
			assertCondition(compareObjects(ids, new String[] {"CPD0000"}));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is less than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify 0's are appended to the message id.</dd>
	 * </dl>
	 */
	public void Var040() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"CPD", "ABC1234"};
			hl.setMessageIDs(ids);
			assertCondition(compareObjects(ids, new String[] {"CPD0000", "ABC1234"}));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDs(String[]) with a message id that is less than 7 characters.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify 0's are appended to the message id.</dd>
	 * </dl>
	 */
	public void Var041() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = new String[] {"ABC1234", "ABC1245", "ABC4567", "CPD"};
			hl.setMessageIDs(ids);
			assertCondition(compareObjects(ids, new String[] {"ABC1234", "ABC1245", "ABC4567","CPD0000"}));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessageIDs() before any message ids have been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a zero length array is returned.</dd>
	 * </dl>
	 */
	public void Var042() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] ids = hl.getMessageIDs();
			assertCondition(ids.length == 0);
		}
		catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var043() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(HistoryLog.OMIT);
			assertCondition(hl.getMessageIDsListIndicator() == HistoryLog.OMIT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with SELECT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var044() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(HistoryLog.SELECT);
			assertCondition(hl.getMessageIDsListIndicator() == HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var045() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(-1);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var046() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(2);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var047() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(-156);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var048() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(245);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with SELECT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var049() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(0);
			assertCondition(hl.getMessageIDsListIndicator() == HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageIDsListIndicator(int) with OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var050() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageIDsListIndicator(1);
			assertCondition(hl.getMessageIDsListIndicator() == HistoryLog.OMIT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessageIDsListIndicator(int) before an indicator has been set. </dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the default of HistoryLog.SELECT is returned.</dd>
	 * </dl>
	 */
	public void Var051() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getMessageIDsListIndicator()==HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var052() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(HistoryLog.OMIT);
			assertCondition(hl.getMessageTypeListIndicator() == HistoryLog.OMIT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with SELECT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var053() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(HistoryLog.SELECT);
			assertCondition(hl.getMessageTypeListIndicator() == HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var054() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(-1);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var055() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(2);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var056() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(-156);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var057() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(245);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with SELECT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var058() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(0);
			assertCondition(hl.getMessageTypeListIndicator() == HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypeListIndicator(int) with OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct list indicator is set.</dd>
	 * </dl>
	 */
	public void Var059() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypeListIndicator(1);
			assertCondition(hl.getMessageTypeListIndicator() == HistoryLog.OMIT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessageTypeListIndicator(int) before an indicator has been set. </dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the default of HistoryLog.SELECT is returned.</dd>
	 * </dl>
	 */
	public void Var060() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getMessageTypeListIndicator()==HistoryLog.SELECT);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessageTypes() before any types have been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a zero length array is returned.</dd>
	 * </dl>
	 */
	public void Var061() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getMessageTypes().length == 0);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(null)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var062() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageTypes(null);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIs(e, "NullPointerException", "types");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with more than 10 types</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var063() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[11];
			hl.setMessageTypes(types);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with an invalid type</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var064() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"MY_TYPE"};
			hl.setMessageTypes(types);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with an invalid type</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var065() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_COMPLETION, "MY_TYPE"};
			hl.setMessageTypes(types);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with an invalid type</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var066() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"MY_TYPE", HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_COPY};
			hl.setMessageTypes(types);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_COMPLETION</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var067() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_COMPLETION};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_COPY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var068() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_COPY};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_DIAGNOSTIC</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var069() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_DIAGNOSTIC};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_ESCAPE</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var070() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_ESCAPE};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_INFORMATIONAL</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var071() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_INFORMATIONAL};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_INQUIRY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var072() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_INQUIRY};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_NOTIFY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var073() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_NOTIFY};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_REPLY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var074() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_REPLY};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_REQUEST</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var075() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_REQUEST};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_COMPLETION</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var076() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*COMP"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_COPY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var077() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*COPY"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_DIAGNOSTIC</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var078() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*DIAG"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_ESCAPE</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var079() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*ESCAPE"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_INFORMATIONAL</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var080() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*INFO"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_INQUIRY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var081() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*INQ"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_NOTIFY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var082() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_NOTIFY};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_REPLY</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var083() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*RPY"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with TYPE_REQUEST</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var084() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {"*RQS"};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with various valid types</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var085() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_INFORMATIONAL, HistoryLog.TYPE_NOTIFY, HistoryLog.TYPE_REQUEST};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with various valid types</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var086() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_COPY, HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_ESCAPE, HistoryLog.TYPE_INFORMATIONAL, HistoryLog.TYPE_INQUIRY, HistoryLog.TYPE_NOTIFY, HistoryLog.TYPE_REPLY, HistoryLog.TYPE_REQUEST};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with various valid types</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify correct type is set.</dd>
	 * </dl>
	 */
	public void Var087() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_COPY, HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_ESCAPE, HistoryLog.TYPE_INFORMATIONAL, HistoryLog.TYPE_INQUIRY, HistoryLog.TYPE_NOTIFY, HistoryLog.TYPE_REPLY, HistoryLog.TYPE_REQUEST};
			hl.setMessageTypes(types);
			assertCondition(compareObjects(types, hl.getMessageTypes()));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageTypes(String[]) with various valid types, but with more than 10 types</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var088() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			String[] types = new String[] {HistoryLog.TYPE_ESCAPE, HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_COPY, HistoryLog.TYPE_DIAGNOSTIC, HistoryLog.TYPE_ESCAPE, HistoryLog.TYPE_INFORMATIONAL, HistoryLog.TYPE_INQUIRY, HistoryLog.TYPE_NOTIFY, HistoryLog.TYPE_REPLY, HistoryLog.TYPE_REQUEST};
			hl.setMessageTypes(types);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessageSeverity() before a severity has been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the default value is returned.</dd>
	 * </dl>
	 */
	public void Var089() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getMessageSeverity() == 0);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var090() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(-1);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var091() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(-58976);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var092() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(100);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var093() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(101);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with an invalid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var094() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(67895);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with the default value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the appropriate severity is set.</dd>
	 * </dl>
	 */
	public void Var095() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(0);
			assertCondition(hl.getMessageSeverity() == 0);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with a valid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the appropriate severity is set.</dd>
	 * </dl>
	 */
	public void Var096() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(1);
			assertCondition(hl.getMessageSeverity() == 1);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with a valid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the appropriate severity is set.</dd>
	 * </dl>
	 */
	public void Var097() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(56);
			assertCondition(hl.getMessageSeverity() == 56);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with a valid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the appropriate severity is set.</dd>
	 * </dl>
	 */
	public void Var098() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(98);
			assertCondition(hl.getMessageSeverity() == 98);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setMessageSeverity(int) with a valid value.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the appropriate severity is set.</dd>
	 * </dl>
	 */
	public void Var099() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setMessageSeverity(99);
			assertCondition(hl.getMessageSeverity() == 99);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getJobs() before any jobs have been set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a zero length Job array is returned.</dd>
	 * </dl>
	 */
	public void Var100() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			assertCondition(hl.getJobs().length == 0);
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(null)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a NullPointerException is thrown.</dd>
	 * </dl>
	 */
	public void Var101() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			hl.setJobs(null);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIs(e, "NullPointerException", "jobs");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(Jobs[]) with more than 5 jobs</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var102() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			Job[] jobs = new Job[6];
			hl.setJobs(jobs);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(Jobs[]) with more than 5 jobs</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var103() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			Job[] jobs = new Job[789];
			hl.setJobs(jobs);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(Job[]) when one of the jobs is null.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a NullPointerException is thrown</dd>
	 * </dl>
	 */
	public void Var104() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			Job[] jobs = new Job[] {new Job(systemObject_), null};
			hl.setJobs(jobs);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIs(e, "NullPointerException", "job(1)");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(Job[]) when one of the jobs does not have a job name set.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify a NullPointerException is thrown</dd>
	 * </dl>
	 */
	public void Var105() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			Job[] jobs = new Job[] {new Job(systemObject_), new Job()};
			hl.setJobs(jobs);
			failed("Did not throw exception.");
		}catch(Exception e){
			assertExceptionIs(e, "NullPointerException", "job name(1)");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::setJobs(Job[])</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the correct jobs are set.</dd>
	 * </dl>
	 */
	public void Var106() {
		try{
			HistoryLog hl = new HistoryLog(systemObject_);
			Job[] jobs = new Job[] {new Job(systemObject_), new Job(systemObject_, Job.JOB_NAME_CURRENT, Job.USER_NAME_BLANK, Job.JOB_NUMBER_BLANK)};
			hl.setJobs(jobs);
			assertCondition(compareObjects(hl.getJobs(), jobs));
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getLength()</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify the number of messages</dd>
	 * </dl>
	 */
	public void Var107() {
		try{
			
		    if(systemObject_.getVRM() >= 0x00060100){

			systemObject_.connectService(AS400.SIGNON);
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Enumeration jl = null ;
			Job[] jobs;

					//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	

			if (jl.hasMoreElements()){

						//We just need the first job
			    jobs = list.getJobs(0,0);

			    hl.setJobs(jobs);


			    String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			    // Do the messages for the last hour 
			    // hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			    // Get message for the last 15 mintes
			    hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			    hl.setEndingDate(date, "*AVAIL");
			    assertCondition(hl.getLength() > 0);
			}

		    }else{
			notApplicable("V6R1 or later variation.");
		    }

		}catch(Exception e){
		    failed(e, "Unexpected exception.");

		}
	}
	

	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages()</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var108() {
		try{
                    
			
			if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
				//We get more than one job to compare different job names
				jobs = list.getJobs(0,3);
				hl.setJobs(jobs);

				// Limit the search
				hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
				hl.setEndingDate(new Date(System.currentTimeMillis())); 

				Enumeration messages = hl.getMessages();
	
				boolean success = true;
				while(messages.hasMoreElements())
				{
					QueuedMessage message = (QueuedMessage) messages.nextElement();
					//Verify the messages returned are from the jobs specified
					String jobName = message.getFromJobName();
					String jobNumber = message.getFromJobNumber();
					String jobUser = message.getUser();
					boolean found = false;
					for(int i=0; i<jobs.length; i++)
					{
						if(jobName.equals(jobs[i].getName()) && jobNumber.equals(jobs[i].getNumber()) && jobUser.equals(jobs[i].getUser()))
						found = true;
					}
					success = success && found;
				}
				hl.close();
				assertCondition(success);
				
			}
			
			}else{
				notApplicable("V6R1 or later variation.");
                    }
	
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	

	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where starting date is current date</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var109() {
	    StringBuffer sb = new StringBuffer(); 
	    try{
	      if(systemObject_.getVRM() >= 0x00060100){
			
	        HistoryLog hl = new HistoryLog(systemObject_);
	        JobList list = new JobList(systemObject_);
		/* reduce the size of the list to active jobs */
		list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
		list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
		list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

	        Job[] jobs;
	        Enumeration jl = null ;
            
	        //Add attribute so there are elements returned for the JobList
	        list.addJobAttributeToRetrieve(Job.USER_NAME);
	        jl = list.getJobs();
			
	        if (jl.hasMoreElements()){
			
	          //We get more than one job to compare different job names
	          jobs = list.getJobs(0,3);
	          hl.setJobs(jobs);        
            
            
	          // set the date to the current date
	          String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
	          hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
	          // Just get message for the last hour
	          hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
	          hl.setEndingDate(date, "*AVAIL");
	          Enumeration messages = hl.getMessages();
	          boolean success = true;
	          while(messages.hasMoreElements())
	          {
	            QueuedMessage message = (QueuedMessage) messages.nextElement();
	            //Verify the messages returned are from the jobs specified
	            String jobName = message.getFromJobName();
	            String jobNumber = message.getFromJobNumber();
	            String jobUser = message.getUser();
	            boolean found = false;
	            for(int i=0; i<jobs.length; i++)
	            {
	              if(jobName.equals(jobs[i].getName()) && jobNumber.equals(jobs[i].getNumber()) && jobUser.equals(jobs[i].getUser()))
	              {
	                sb.append("Looking at "+jobs[i].getNumber()+"/"+jobs[i].getUser()+"/"+jobs[i].getName()+"\n"); 
	                found = true;
	              }
	            }
	            if (!found) sb.append("FAILED:  Did not find job "+jobNumber+"/"+jobUser+"/"+jobName+"\n"); 
	            if(!dateToString((message.getDate().getTime())).substring(0, 7).equals(date)) {
	              sb.append("FAILED:  Dates did not match "+dateToString((message.getDate().getTime())).substring(0, 7) +
	                  " != "+date+"\n"); 
	              found = false;
	            }
					
	              success = success && found;
			}
			hl.close();
			assertCondition(success, sb.toString());
			
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
             
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message id was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var110() {
		try{
            
			if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPI000"});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned start with CPI
				if(!message.getID().startsWith("CPI"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
                    }else{
                        notApplicable("V6R1 or later variation");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a few specific message ids were set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var111() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
                    	
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);
			
			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
				//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
				
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPI000", "CPD1234"});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned start with CPI
				String id = message.getID();
				if(!id.startsWith("CPI") && !id.equals("CPD1234"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message id was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var112() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
                    	
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPIAD0B"});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned start with CPI
				if(!message.getID().equals("CPIAD0B"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message id was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var113() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
                    	
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
		
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"KIM8765"});
			Enumeration messages = hl.getMessages();
			assertCondition(hl.getLength() == 0, "Expected length=0, received length=" + hl.getLength()+ " "+messages);	// no messages should come back since this is invalid id
			}
			
			}else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message id was set, and message
	 * id list indicator is set to OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var114() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
                    	
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
				//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);

			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPI000"});
			hl.setMessageIDsListIndicator(HistoryLog.OMIT);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned do not start with CPI
				if(message.getID().startsWith("CPI"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a few specific message ids were set
	 * and message ids list indicator is set to omit.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var115() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);	
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPI000", "CPD1234"});
			hl.setMessageIDsListIndicator(HistoryLog.OMIT);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned do not start with CPI
				String id = message.getID();
				if(id.startsWith("CPI") || id.equals("CPD1234"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message id was set
	 * and message id indicator was set to OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var116() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);
			
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageIDs(new String[] {"CPIAD0B"});
			hl.setMessageIDsListIndicator(HistoryLog.OMIT);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned do not start with CPI
				if(message.getID().equals("CPIAD0B"))
					success = false;
			}
			hl.close();
			assertCondition(success);
			
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var117() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_INFORMATIONAL});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 4)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a few specific message types were set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var118() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);

			hl.setJobs(jobs);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_INFORMATIONAL, HistoryLog.TYPE_COMPLETION});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the types for the messages returned
				if(message.getType() != 4 && message.getType() != 1)
					success = false;
			}
			hl.close();
			assertCondition(success);
			
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var119() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);

			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration j = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			j = list.getJobs();	
			
			if (j.hasMoreElements()){
			
			//We get more than one job to compare different job messages
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);		
			
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_COMPLETION});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for each of the messages returned is COMPLETION
				if(message.getType() != AS400Message.COMPLETION)
					success = false;
			}
			hl.close();
			assertCondition(success);
			}
			}else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set, and message
	 * type indicator is set to OMIT</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var120() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_INFORMATIONAL});
			hl.setMessageTypeListIndicator(HistoryLog.OMIT);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned start with CPI
				if(message.getType() == 4)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a few specific message types were set
	 * and message type list indicator is set to omit.</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var121() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_COMPLETION, HistoryLog.TYPE_INFORMATIONAL});
			hl.setMessageTypeListIndicator(HistoryLog.OMIT);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the ids for the messages returned start with CPI
				if(message.getType() == 4 || message.getType() == 1)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var122() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_COMPLETION});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 1)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var123() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_COPY});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 6)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var124() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_DIAGNOSTIC});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 2)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var125() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_INQUIRY});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 5)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var126() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_NOTIFY});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 14)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() where a specific message type was set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var127() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			// set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageTypes(new String[] {HistoryLog.TYPE_ESCAPE});
			Enumeration messages = hl.getMessages();
			boolean success = true;
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getType() != 15)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() when a message severity has been set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var128() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
//			 set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageSeverity(3);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			System.out.println(hl.getLength());
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getSeverity() < 3)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages() when a message severity has been set</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var129() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
//			 set the date to the current date
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			hl.setMessageSeverity(1);
			Enumeration messages = hl.getMessages();
			boolean success = true;
			System.out.println(hl.getLength());
			while(messages.hasMoreElements())
			{
				QueuedMessage message = (QueuedMessage) messages.nextElement();
				//Verify the type for the messages returned is informational
				if(message.getSeverity() < 1)
					success = false;
			}
			hl.close();
			assertCondition(success);
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages(-2, 0)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var130() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration j = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			j = list.getJobs();	
			
			if (j.hasMoreElements()){
			jobs = list.getJobs(0,3);
			
			hl.setJobs(jobs);
			QueuedMessage[] messages = hl.getMessages(-2, 0);
			failed("Did not throw exception."+messages);
			
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages(2, -1)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify an ExtendedIllegalArgumentException is thrown.</dd>
	 * </dl>
	 */
	public void Var131() {
		try{
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration j = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			j = list.getJobs();	
			
			if (j.hasMoreElements()){
			jobs = list.getJobs(0,3);
	
			hl.setJobs(jobs);
			QueuedMessage[] messages = hl.getMessages(2, -1);
			failed("Did not throw exception."+messages); 
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			assertExceptionIsInstanceOf(e, "com.ibm.as400.access.ExtendedIllegalArgumentException");
		}
	}
	
	/**
	 * <dl>
	 * <dt>Test:</dt>
	 * <dd>Call HistoryLog::getMessages(int, int)</dd>
	 * <dt>Result:</dt>
	 * <dd>Verify messages that fit the appropriate criteria come back.</dd>
	 * </dl>
	 */
	public void Var132() {
		try{
		    StringBuffer sb = new StringBuffer(); 
                    if(systemObject_.getVRM() >= 0x00060100){
			HistoryLog hl = new HistoryLog(systemObject_);
			JobList list = new JobList(systemObject_);
			/* reduce the size of the list to active jobs */
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_ACTIVE, Boolean.TRUE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_JOBQ, Boolean.FALSE);
			list.addJobSelectionCriteria(JobList.SELECTION_PRIMARY_JOB_STATUS_OUTQ, Boolean.FALSE);

			Job[] jobs;
			Enumeration jl = null ;
			
			//Add attribute so there are elements returned for the JobList
			list.addJobAttributeToRetrieve(Job.USER_NAME);
			jl = list.getJobs();	
			
			if (jl.hasMoreElements()){
			
			//We get more than one job to compare different job names
			jobs = list.getJobs(0,3);
			hl.setJobs(jobs);		
			
			String date = dateToString(Calendar.getInstance().getTime()).substring(0, 7);
			hl.setStartingDate(date, "*AVAIL");	// get all messages for this day
			// Just get message for the last hour
			hl.setStartingDate(new Date(System.currentTimeMillis() - 900000)); 
			hl.setEndingDate(date, "*AVAIL");
			QueuedMessage[] messages = hl.getMessages(0, 2);
			//System.out.println(messages.length);
			boolean success = true;
			for(int i=0; i<messages.length; i++)
			{
//				Verify the messages returned are from the jobs specified
				String jobName = messages[i].getFromJobName();
				String jobNumber = messages[i].getFromJobNumber();
				String jobUser = messages[i].getUser();
				sb.append("Looking for  "+jobNumber+"/"+jobUser+"/"+jobName+"\n"); 

				boolean found = false;
				for(int j=0; j<jobs.length; j++)
				{
				    sb.append("Looking at job["+j+"] = "+jobs[j].getNumber()+"/"+jobs[j].getUser()+"/"+jobs[j].getName()+"\n");

					if(jobName.equals(jobs[j].getName()) && jobNumber.equals(jobs[j].getNumber()) && jobUser.equals(jobs[j].getUser()))
					{
						found = true;
					}
				}
				if (!found) sb.append("FAILED: Did not find job\n"); 
				if(!dateToString((messages[i].getDate().getTime())).substring(0, 7).equals(date)) {
				    sb.append("FAILED:  Dates not matching "+dateToString(messages[i].getDate().getTime()).substring(0, 7)+" != "+date+"\n"); 
				    found = false;
				}
				success = success && found;
			}
			hl.close();
			assertCondition(success, sb.toString());
			}
                    }else{
                        notApplicable("V6R1 or later variation.");
                    }
		}catch(Exception e){
			failed(e, "Unexpected exception.");
		}
	}
}
