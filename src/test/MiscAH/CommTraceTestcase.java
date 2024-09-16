 //////////////////////////////////////////////////////////////////////
 //
 ///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  CommTraceTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test.MiscAH;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Vector;
import java.util.Properties;
import com.ibm.as400.access.AS400;
import com.ibm.as400.util.commtrace.Format;
import com.ibm.as400.util.commtrace.FormatProperties;

import test.Testcase;

import com.ibm.as400.access.IFSFileInputStream;

/**
  Testcase ExampleTestcase.
  **/
public class CommTraceTestcase extends Testcase
{
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "CommTraceTestcase";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.CommTraceDriver.main(newArgs); 
   }
// $$$ TO DO $$$
// Replace "ExampleDriver" with the name of your test driver program.
/**
  Constructor.  This is called from ExampleDriver::createTestcases().
  **/
  public CommTraceTestcase(AS400            systemObject,
                         Vector           variationsToRun,
                         int              runMode,
                         FileOutputStream fileOutputStream)
  {
    super(systemObject, "CommTraceTestcase", 33,
          variationsToRun, runMode, fileOutputStream);
  }

/**
  Runs the variations requested.
  **/
  public void run()
  {
    boolean allVariations = (variationsToRun_.size() == 0);

    if ((allVariations || variationsToRun_.contains("1")))
    {
      setVariation(1);
      Var001();
    }
    
    if ((allVariations || variationsToRun_.contains("2")))
    {
      setVariation(2);
      Var002();
    }
    
    if ((allVariations || variationsToRun_.contains("3")))
    {
      setVariation(3);
      Var003();
    }
    
    if ((allVariations || variationsToRun_.contains("4")))
    {
      setVariation(4);
      Var004();
    }
    
    if ((allVariations || variationsToRun_.contains("5")))
    {
      setVariation(5);
      Var005();
    }
    
    if ((allVariations || variationsToRun_.contains("6")))
    {
      setVariation(6);
      Var006();
    }
    
    if ((allVariations || variationsToRun_.contains("7")))
    {
      setVariation(7);
      Var007();
    }
    
    if ((allVariations || variationsToRun_.contains("8")))
    {
      setVariation(8);
      Var008();
    }
    
    if ((allVariations || variationsToRun_.contains("9")))
    {
      setVariation(9);
      Var009();
    }
   
    if ((allVariations || variationsToRun_.contains("10")))
    {
      setVariation(10);
      Var010();
    }
    
    if ((allVariations || variationsToRun_.contains("11")))
    {
      setVariation(11);
      Var011();
    }
    
    if ((allVariations || variationsToRun_.contains("12")))
    {
      setVariation(12);
      Var012();
    }
    
    if ((allVariations || variationsToRun_.contains("13")))
    {
      setVariation(13);
      Var013();
    }
    
    if ((allVariations || variationsToRun_.contains("14")))
    {
      setVariation(14);
      Var014();
    }
    
    if ((allVariations || variationsToRun_.contains("15")))
    {
      setVariation(15);
      Var015();
    }
    
    if ((allVariations || variationsToRun_.contains("16")))
    {
      setVariation(16);
      Var016();
    }
    
    if ((allVariations || variationsToRun_.contains("17")))
    {
      setVariation(17);
      Var017();
    }
    
    if ((allVariations || variationsToRun_.contains("18")))
    {
      setVariation(18);
      Var018();
    }
    
    if ((allVariations || variationsToRun_.contains("19")))
    {
      setVariation(19);
      Var019();
    }
    
    if ((allVariations || variationsToRun_.contains("20")))
    {
      setVariation(20);
      Var020();
    }
    
    if ((allVariations || variationsToRun_.contains("21")))
    {
      setVariation(21);
      Var021();
    }
    
    if ((allVariations || variationsToRun_.contains("22")))
    {
      setVariation(22);
      Var022();
    }
    
    if ((allVariations || variationsToRun_.contains("23")))
    {
      setVariation(23);
      Var023();
    }
    
	if ((allVariations || variationsToRun_.contains("24")))
    {
      setVariation(24);
      Var024();
    }
    
    if ((allVariations || variationsToRun_.contains("25")))
    {
      setVariation(25);
      Var025();
    }
    
    if ((allVariations || variationsToRun_.contains("26")))
    {
      setVariation(26);
      Var026();
    }
    
    if ((allVariations || variationsToRun_.contains("27")))
    {
      setVariation(27);
      Var027();
    }
    
    if ((allVariations || variationsToRun_.contains("28")))
    {
      setVariation(28);
      Var028();
    }
    
    if ((allVariations || variationsToRun_.contains("29")))
    {
      setVariation(29);
      Var029();
    }
    
    if ((allVariations || variationsToRun_.contains("30")))
    {
      setVariation(30);
      Var030();
    }
    
    if ((allVariations || variationsToRun_.contains("31")))
    {
      setVariation(31);
      Var031();
    }
    
    if ((allVariations || variationsToRun_.contains("32")))
    {
      setVariation(32);
      Var032();
    }
    
    if ((allVariations || variationsToRun_.contains("33")))
    {
      setVariation(33);
      Var033();
    }
  }

/**
  ctor() - No parameters. Should initialize without any errors.
  **/
  public void Var001()
  {
  	try {
	    Format f = new Format();
	    assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
    }
  }

/**
 * ctor() - Invalid system. Should work fine since no validation is done in the ctor
  **/
  public void Var002()
  {
  	try {
	  	Format f = new Format(new AS400("bogus","bogus","bogus"));
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
    }
  }

/**
  ctor() - Invalid parameters. Should work since since the ctor will automatically create a new Properties object with default filtering values.
  **/
  public void Var003()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"bogus","bogus");
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
    }
  }

/**
 * ctor() - invalid command line arguments. Should work fine since the ctor ignores values that aren't set or that it doesn't understand.
  **/
  public void Var004()
  {
  	try {
  		String[] args = {"bogus","bogus"};
	  	Format f = new Format(args);
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	
  }

/**
  ctor() - Partially valid command line arguments. Should work fine since the ctor ignores values that aren't set or that it doesn't understand.
  **/
  public void Var005()
  {
  	try {
  		String[] args = {"-v", "true","bogus","bogus"};
	  	Format f = new Format(args);
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	  
  }

/**
  ctor() - Null String. Should work since the ctor will die silently when an invalid file is specified.
  **/
  public void Var006()
  {
  	try {
  		String file = null;
	  	Format f = new Format(file);
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	
  }

/**
  ctor() - Valid file String. Should work since the ctor will just open a stream on the file and format the prolog. And the prolog shouldn't contain invalid data.
  **/
  public void Var007()
  {
  	try {
  		String file = "c:\\java\\ping4wm";
	  	Format f = new Format(file);
	  	assertCondition(f.getProlog().invalidData()==false);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	
  }

/**
  ctor() - Null InputStream. Should work since the ctor will die silently when an invalid stream is specified.
  **/
  public void Var008()
  {
  	try {
  		IFSFileInputStream file = null;
	  	Format f = new Format(file);
	  	assertCondition(true==true);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	
  }

/**
 * openLclFile() - Invalid Construction. Should return error code
 **/
  public void Var009()
  {
  	try {
	  	Format f = new Format();
	  	String file = null;
	  	assertCondition(f.openLclFile(file)!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
	
/**
 * ctor() - Valid InputStream. Should work.
  **/
  public void Var010()
  {
  	try {
  		IFSFileInputStream file = new IFSFileInputStream(systemObject_,"/spisaacs/ping4wm");
	  	Format f = new Format(file);
	  	assertCondition(f.getProlog().invalidData()==false);
	  	f.close();
	  	file.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	  
  }
	
/**
 * toLclBinFile() - Invalid Construction. Should return an error code.
  **/
  public void Var011()
  {
  	try {
  		String file =null;
	  	Format f = new Format(file);
	  	assertCondition(f.toLclBinFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	  
  }	
 
/**
 * toLclBinFile() - Valid Construction. Shouldn't return an error code.
 **/
  public void Var012()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"c:\\java\\ping4wm.bin","c:\\java\\ping4wm");
	  	assertCondition(f.toLclBinFile()==0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	  
  }
  
/**
 * toIFSBinFile() - Invalid Construction. Should return an error code.
 **/
  public void Var013()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.toLclBinFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	  
  }  	
  
/**
 * toIFSBinFile() - Valid Construction. Shouldn't return an error code.
 **/
  public void Var014()
  {
  	try {
  		IFSFileInputStream file = new IFSFileInputStream(systemObject_,"/spisaacs/ping4wm");
	  	Format f = new Format(file);
	  	f.setOutFile("/spisaacs/ping4wm.bin");
	  	f.setSystem(systemObject_);
	  	assertCondition(f.toIFSBinFile()==0);
	  	f.close();
	  	file.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * toLclTxtFile() - Invalid Construction. Should return an error code.
 **/
  public void Var015()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.toLclTxtFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * toLclTxtFile() - Valid Construction. Shouldn't return an error code.
 **/
  public void Var016()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"c:\\java\\ping4wm.txt","c:\\java\\ping4wm");
	  	assertCondition(f.toLclTxtFile()==0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * toIFSTxtFile() - Invalid Construction. Should return an error code.
 **/
  public void Var017()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.toIFSTxtFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }  
  
/**
 * toIFSTxtFile() - Valid Construction. Shouldn't return an error code.
 **/
  public void Var018()
  {
  	try {
  		IFSFileInputStream file = new IFSFileInputStream(systemObject_,"/spisaacs/ping4wm");
	  	Format f = new Format(file);
	  	f.setSystem(systemObject_);
	  	f.setOutFile("/spisaacs/ping4wm.txt");
	  	assertCondition(f.toIFSTxtFile()==0);
	  	f.close();
	  	file.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * getNextRecord() - Invalid Construction. Should return null
 **/
  public void Var019()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.getNextRecord()==null);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * getNextRecord() - Valid Construction. Shouldn't return null
 **/
  public void Var020()
  {
  	try {
	  	Format f = new Format("c:\\java\\ping4wm");
	  	assertCondition(f.getNextRecord()!=null);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * openIFSFile() - Valid Construction. Shouldn't return error code
 **/
  public void Var021()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"/spisaacs/ping4wm.bin","/spisaacs/ping4wm");
	  	f.setSystem(systemObject_);
	  	assertCondition(f.openIFSFile()==0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * openIFSFile() - Valid Construction. Shouldn't return error code
 **/
  public void Var022()
  {
  	try {
	  	Format f = new Format();
	  	f.setSystem(systemObject_);
	  	assertCondition(f.openIFSFile("/spisaacs/ping4wm.bin")==0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * openLclFile() - Valid Construction. Shouldn't return error code
 **/
  public void Var023()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"c:\\java\\ping4wm.bin","c:\\java\\ping4wm");
	  	assertCondition(f.openLclFile()==0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * openIFSFile() - Invalid Construction. Should return error code
 **/
  public void Var024()
  {
  	try {
	  	Format f = new Format();
	  	f.setSystem(systemObject_);
	  	String file = null;
	  	assertCondition(f.openIFSFile(file)!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * openLclFile() - Invalid Construction. Should return error code
 **/
  public void Var025()
  {
  	try {
	  	Format f = new Format();
	  	String file = null;
	  	assertCondition(f.openLclFile(file)!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }  

/**
 * openLclFile() - Invalid Construction. Should return error code
 **/
  public void Var026()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.openLclFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  
  }  

/**
 * openIFSFile() - Invalid Construction. Should return error code
 **/
  public void Var027()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.openIFSFile()!=0);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * getNextRecord() - Valid Construction. Should not return null
 */  
  public void Var028()
  {
  	try {
	  	Format f = new Format(new FormatProperties(),"c:\\java\\ping4wm.bin","c:\\java\\ping4wm");
	  	assertCondition(f.getNextRecord()!=null);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * getRecFromFile() - Valid Construction. Should not return null
 */  
  public void Var029()
  {
  	try {
	  	Format f = new Format();
	  	f.openLclFile("c:\\java\\ping4wm.bin");
	  	assertCondition(f.getRecFromFile()!=null);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * getRecFromFile() - Invalid Construction. Should return null
 */  
  public void Var030()
  {
  	try {
	  	Format f = new Format();
	  	assertCondition(f.getRecFromFile()==null);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }

/**
 * getIntFromFile() - Valid Construction. Should not return -1
 */  
  public void Var031()
  {
  	try {
	  	Format f = new Format();
	  	f.openLclFile("c:\\java\\ping4wm.bin");
	  	f.getRecFromFile();
	  	assertCondition(f.getIntFromFile()!=-1);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	} 	  
  }
  
/**
 * getIntFromFile() - Invalid Construction. Should return -1
 */  
  public void Var032()
  {
  	try {
	  	Format f = new Format();
	  	f.getRecFromFile();
	  	assertCondition(f.getIntFromFile()==-1);
	  	f.close();
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}
  }

/**
 * close() - Valid construction. Should return 0
 */
  public void Var033()
  {
  	try {
  		String file = "c:\\java\\ping4wm";
	  	Format f = new Format(file);
	  	assertCondition(f.close()==0);
  	} catch (Exception e) {
   		failed (e, "Unexpected Exception");
  	}	  	
  }
}
