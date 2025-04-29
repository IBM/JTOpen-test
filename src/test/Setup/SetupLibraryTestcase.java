package test.Setup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.FTP;

import test.JTOpenTestSetup;
import test.PasswordVault;
import test.Testcase;

public abstract class SetupLibraryTestcase extends Testcase {

  public SetupLibraryTestcase() {
    super();
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, namesAndVars, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
      String password) {
    super(system, name, namesAndVars, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
      String password, AS400 pwrSys, String pwrSysPwd) {
    super(system, name, namesAndVars, runMode, fileOutputStream, password, pwrSys, pwrSysPwd);
  }

  public SetupLibraryTestcase(AS400 system, String name, Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
      AS400 pwrSys) {
    super(system, name, namesAndVars, runMode, fileOutputStream, pwrSys);
  }

  public SetupLibraryTestcase(AS400 system, String name, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, variationsToRun, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, Vector<String> variationsToRun, int runMode, FileOutputStream fileOutputStream,
      String password) {
    super(system, name, variationsToRun, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password, AS400 pwrSys) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password, pwrSys);
  }

  public SetupLibraryTestcase(AS400 system, String name, int totalVariations, Vector<String> variationsToRun, int runMode,
      FileOutputStream fileOutputStream, String password, String pwrSysUid, String pwrSysPwd) {
    super(system, name, totalVariations, variationsToRun, runMode, fileOutputStream, password, pwrSysUid, pwrSysPwd);
  }

  public void restoreLibrary(String saveFile, String library) throws Exception { 
     
     CommandCall cmd = new CommandCall(pwrSys_);
     

     String remoteName = ""; 
     int savfIndex = saveFile.indexOf(".savf"); 
     if (savfIndex > 0) { 
       remoteName=saveFile.substring(0,savfIndex).toUpperCase(); 
     } else { 
        throw new Exception("Unable to find .savf in "+saveFile); 
     }

     // Create the necessary save files in preparation
     // for object restoration.
     String command = "DLTF QGPL/"+remoteName; 
     cmd.setCommand(command);
     boolean success = cmd.run();
     if (!success) { 
       output_.println("Warning: Command failed "+command); 
     }
    
     output_.println("Creating master save file...");
     command = "CRTSAVF QGPL/"+remoteName;
     cmd.setCommand(command);
     success = cmd.run();
     if (!success) { 
       output_.println("Error: Command failed "+command); 
       throw new Exception("Command failed "+command);  
     }
  
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
     command = "RSTLIB SAVLIB("+library+") DEV(*SAVF) SAVF(QGPL/"+remoteName+")";
     output_.println("Restoring objects and libraries...");
     cmd.setCommand(command);
     boolean rc = cmd.run();
     if (rc == false) {
       System.out.println("Restore failed using "+command); 
       output_.println("Restore failed using "+command); 
       AS400Message[] list = cmd.getMessageList();
       for (int i = 0; i < list.length; i++) {
         System.out.println(list[i].getID()+":"+ list[i].getText());
       }
     } else { 
     // Delete the necessary save files.
     output_.println("Deleting master save file...");
     cmd.setCommand("DLTF QGPL/"+remoteName);
     cmd.run();
  
     output_.println("Restore of "+library+" on "+pwrSys_.getSystemName()+" is complete.");
     }  
     
   }

}