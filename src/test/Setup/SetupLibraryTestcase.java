package test.Setup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;

import test.JTOpenTestSetup;
import test.PasswordVault;
import test.Testcase;

public abstract class SetupLibraryTestcase extends Testcase {

  public SetupLibraryTestcase() {
    super();
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable namesAndVars, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, namesAndVars, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
      String password) {
    super(system, name, namesAndVars, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
      String password, AS400 pwrSys, String pwrSysPwd) {
    super(system, name, namesAndVars, runMode, fileOutputStream, password, pwrSys, pwrSysPwd);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable namesAndVars, int runMode, FileOutputStream fileOutputStream,
      AS400 pwrSys) {
    super(system, name, namesAndVars, runMode, fileOutputStream, pwrSys);
  }

  public SetupLibraryTestcase(AS400 system, String name, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, variationsToRun, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, Vector variationsToRun, int runMode, FileOutputStream fileOutputStream,
      String password) {
    super(system, name, variationsToRun, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password, AS400 pwrSys) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password, pwrSys);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password, String pwrSysUid, String pwrSysPwd) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password, pwrSysUid, pwrSysPwd);
  }

  public void restoreLibrary(String saveFile, String library) throws Exception { 
     
     CommandCall cmd = new CommandCall(pwrSys_);
     
     // Create the necessary save files in preparation
     // for object restoration.
     output_.println("Creating master save file...");
     cmd.setCommand("CRTSAVF QGPL/"+saveFile);
     cmd.run();
  
     // FTP files from GIT on to the 400
     output_.println("Starting ftp...");
  
     File directory = new File("savf"); 
     if (!directory.exists()) { 
       directory.mkdir(); 
     }
     
     String localFilename = "savf/"+saveFile; 
     File localFile = new File(localFilename) ;
     
     if (!localFile.exists()) { 
       /* extract using the classloader if it doesn't exist */ 
       // Extract from the Jar file and note that it will need updated
       InputStream is = JTOpenTestSetup.loadResource(localFilename);
       if (is == null) {
           System.out.println("WARNING:  Unable to get "+localFilename+" from classpath");
       } else {
               // Make a copy of the model one 
           FileOutputStream fos = new FileOutputStream(localFilename);
           byte [] buffer = new byte[4096]; 
           int bytesRead = is.read(buffer); 
           while (bytesRead >= 0) {
               if (bytesRead > 0) {
                   fos.write(buffer,0,bytesRead); 
               }
               bytesRead = is.read(buffer); 
           }
           fos.close();
           is.close();
       }
     }
     
     String remoteName = ""; 
     int savfIndex = saveFile.indexOf(".savf"); 
     if (savfIndex > 0) { 
       remoteName=saveFile.substring(0,savfIndex).toUpperCase(); 
     } else { 
        throw new Exception("Unable to find .savf in "+saveFile); 
     }
     
     FTP os400 = new FTP(pwrSys_.getSystemName(), pwrSys_.getUserId(), PasswordVault.decryptPasswordLeak(pwrSysEncryptedPassword_));
     os400.cd("QGPL");
     os400.setDataTransferType(FTP.BINARY);
     boolean ok = os400.put(localFilename, saveFile );
     if (!ok ) { 
       assertCondition(false, "Transfer from "+localFilename+" to "+saveFile+" failed"); 
       return; 
     }
     os400.disconnect();
   
     // Restore objects from save files to appropriate locations
     output_.println("Restoring objects and libraries...");
     cmd.setCommand("RSTLIB SAVLIB("+library+") DEV(*SAVF) SAVF(QGPL/"+remoteName+")");
     cmd.run();
  
     // Delete the necessary save files.
     output_.println("Deleting master save file...");
     cmd.setCommand("DLTF QGPL/"+remoteName);
     cmd.run();
  
     output_.println("Restore of "+library+" on "+pwrSys_.getSystemName()+" is complete.");
     
   }

}