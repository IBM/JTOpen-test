
///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  AS400JDBCDataSourcePropertiesTestcase.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////
package test;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400JDBCConnection;
import com.ibm.as400.access.AS400JDBCDataSource;
import com.ibm.as400.access.BidiStringType;  //@A4A

/**
  Testcase AS400JDBCDataSourcePropertiesTestcase.
 **/
public class AS400JDBCDataSourcePropertiesTestcase extends Testcase
{
    private static String logFileName = "/tmp/AS400JDBCDSPT.log"; 
   private AS400JDBCDataSource dataSource_;
   private DataSourcePropertyListener dataSourcePropertyListener_;

   /**
     Constructor.  This is called from the AS400JDBCDataSourcePropertiesTest constructor.
    **/
   public AS400JDBCDataSourcePropertiesTestcase(AS400 systemObject,
                                 Vector variationsToRun,
                                 int runMode,
                                 FileOutputStream fileOutputStream,
                                 
                                 String password)
   {
      super(systemObject, "AS400JDBCDataSourcePropertiesTestcase", variationsToRun,
            runMode, fileOutputStream, password);
   }

   /**
      Validates that setAccess(String) notifies the PropertyChange listeners.
   **/
   public void Var001()
   {
      try
      {
         dataSource_.setAccess("read only");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setBigDecimal(boolean) notifies the PropertyChange listeners.
   **/
   public void Var002()
   {
      try
      {
         dataSource_.setBigDecimal(false);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setBlockCriteria(int) notifies the PropertyChange listeners.
   **/
   public void Var003()
   {
      try
      {
         dataSource_.setBlockCriteria(0);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setBlockSize(int) notifies the PropertyChange listeners.
   **/
   public void Var004()
   {
      try
      {
         dataSource_.setBlockSize(256);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDatabaseName(String) notifies the PropertyChange listeners.
   **/
   public void Var005()
   {
      try
      {
         dataSource_.setDatabaseName("myDatabase");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDataCompression(boolean) notifies the PropertyChange listeners.
   **/
   public void Var006()
   {
      try
      {
         dataSource_.setDataCompression(false);     //@A4C

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDataSourceName(String) notifies the PropertyChange listeners.
   **/
   public void Var007()
   {
      try
      {
         dataSource_.setDataSourceName("myDataSource");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDataTruncation(boolean) notifies the PropertyChange listeners.
   **/
   public void Var008()
   {
      try
      {
         dataSource_.setDataTruncation(false);     //@A3A Default was changed.

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDateFormat(String) notifies the PropertyChange listeners.
   **/
   public void Var009()
   {
      try
      {
         dataSource_.setDateFormat("ymd");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDateSeparator(String) notifies the PropertyChange listeners.
   **/
   public void Var010()
   {
      try
      {
         dataSource_.setDateSeparator(",");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setDecimalSeparator(String) notifies the PropertyChange listeners.
   **/
   public void Var011()
   {
      try
      {
         dataSource_.setDecimalSeparator(",");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setDescription(String) notifies the PropertyChange listeners.
   **/
   public void Var012()
   {
      try
      {
         dataSource_.setDescription("myDescription");
         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setDriver(String) notifies the PropertyChange listeners.
   **/
   public void Var013()
   {
      try
      {
         dataSource_.setDriver("native");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setErrors(String) notifies the PropertyChange listeners.
   **/
   public void Var014()
   {
      try
      {
         dataSource_.setErrors("full");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setExtendedDynamic(boolean) notifies the PropertyChange listeners.
   **/
   public void Var015()
   {
      try
      {
         dataSource_.setExtendedDynamic(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setLazyClose(boolean) notifies the PropertyChange listeners.
   **/
   public void Var016()
   {
      try
      {
         dataSource_.setLazyClose(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setLibraries(String) notifies the PropertyChange listeners.
   **/
   public void Var017()
   {
      try
      {
         dataSource_.setLibraries("myLibrary");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setLobThreshold(int) notifies the PropertyChange listeners.
   **/
   public void Var018()
   {
      try
      {
         dataSource_.setLobThreshold(256);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setNaming(String) notifies the PropertyChange listeners.
   **/
   public void Var019()
   {
      try
      {
         dataSource_.setNaming("system");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackage(String) notifies the PropertyChange listeners.
   **/
   public void Var020()
   {
      try
      {
         dataSource_.setPackage("myPackage");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackageAdd(boolean) notifies the PropertyChange listeners.
   **/
   public void Var021()
   {
      try
      {
         dataSource_.setPackageAdd(false);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackageCache(boolean) notifies the PropertyChange listeners.
   **/
   public void Var022()
   {
      try
      {
         dataSource_.setPackageCache(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackageClear(boolean) notifies the PropertyChange listeners.
   **/
  public void Var023()
   {
     assertCondition(true, "Do not test deprecated API"); 
   }

   /**
      Validates that setPackageCriteria(String) notifies the PropertyChange listeners.
   **/
   public void Var024()
   {
      try
      {
         dataSource_.setPackageCriteria("select");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackageError(String) notifies the PropertyChange listeners.
   **/
   public void Var025()
   {
      try
      {
         dataSource_.setPackageError("exception");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPackageLibrary(String) notifies the PropertyChange listeners.
   **/
   public void Var026()
   {
      try
      {
         dataSource_.setPackageLibrary("QGPL");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPrefetch(boolean) notifies the PropertyChange listeners.
   **/
   public void Var027()
   {
      try
      {
         dataSource_.setPrefetch(false);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setPrompt(boolean) notifies the PropertyChange listeners.
   **/
   public void Var028()
   {
      try
      {
         dataSource_.setPrompt(true);                               //@A2C

         boolean firstRun = dataSourcePropertyListener_.isChangeNotified();        //@A2C
      dataSourcePropertyListener_.reset();                          //@A2A
      dataSource_.setPrompt(false);                                 //@A2A
      boolean secondRun = dataSourcePropertyListener_.isChangeNotified();     //@A2A
      assertCondition (firstRun && secondRun);                          //@A2A
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setProxyServer(String) notifies the PropertyChange listeners.
   **/
   public void Var029()
   {
      try
      {
         dataSource_.setProxyServer("myProxy");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setRemarks(String) notifies the PropertyChange listeners.
   **/
   public void Var030()
   {
      try
      {
         dataSource_.setRemarks("sql");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setSecondaryUrl(String) notifies the PropertyChange listeners.
   **/
   public void Var031()
   {
      try
      {
         dataSource_.setSecondaryUrl("myUrl");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setSecure(boolean) notifies the PropertyChange listeners.
   **/
   public void Var032()
   {
      try
      {
         dataSource_.setSecure(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setServerName(String) notifies the PropertyChange listeners.
   **/
   public void Var033()
   {
      try
      {
         dataSource_.setServerName("myServer");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setSort(String) notifies the PropertyChange listeners.
   **/
   public void Var034()
   {
      try
      {
         dataSource_.setSort("table");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
      Validates that setSortLanguage(String) notifies the PropertyChange listeners.
   **/
   public void Var035()
   {
      try
      {
         dataSource_.setSortLanguage("enu");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setSortTable(String) notifies the PropertyChange listeners.
   **/
   public void Var036()
   {
      try
      {
         dataSource_.setSortTable("myTable");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setSortWeight(String) notifies the PropertyChange listeners.
   **/
   public void Var037()
   {
      try
      {
         dataSource_.setSortWeight("unique");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setTimeFormat(String) notifies the PropertyChange listeners.
   **/
   public void Var038()
   {
      try
      {
         dataSource_.setTimeFormat("eur");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setTimeSeparator(String) notifies the PropertyChange listeners.
   **/
   public void Var039()
   {
      try
      {
         dataSource_.setTimeSeparator(",");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setThreadUsed(boolean) notifies the PropertyChange listeners.
   **/
   public void Var040()
   {
      try
      {
         dataSource_.setThreadUsed(false);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setTrace(boolean) notifies the PropertyChange listeners.
   **/
   public void Var041()
   {
      try
      {
         dataSource_.setTrace(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSource_.setTrace(false);
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setTransactionIsolation(String) notifies the PropertyChange listeners.
   **/
   public void Var042()
   {
      try
      {
         dataSource_.setTransactionIsolation("none");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setTranslateBinary(boolean) notifies the PropertyChange listeners.
   **/
   public void Var043()
   {
      try
      {
         dataSource_.setTranslateBinary(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that setUser(String) notifies the PropertyChange listeners.
   **/
   public void Var044()
   {
      try
      {
         dataSource_.setUser("myUser");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   /**
   *  Validates that listeners can be removed.
   **/
   public void Var045()
   {
      try
      {
         dataSource_.removePropertyChangeListener(dataSourcePropertyListener_);

         dataSource_.setUser("myUser");

         assertCondition(!dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSource_.addPropertyChangeListener(dataSourcePropertyListener_);
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that multiple PropertyChange listeners are notified.
   **/
   public void Var046()
   {
      try
      {
         DataSourcePropertyListener secondListener = new DataSourcePropertyListener();
         dataSource_.addPropertyChangeListener(secondListener);
         dataSource_.setUser("myUser");

         assertCondition(dataSourcePropertyListener_.isChangeNotified() &&
                secondListener.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
      Validates that removing a PropertyChange listener doesn't affect the
      notification of other listeners.
   **/
   public void Var047()
   {
      try
      {
         DataSourcePropertyListener secondListener = new DataSourcePropertyListener();
         dataSource_.addPropertyChangeListener(secondListener);

         dataSource_.removePropertyChangeListener(dataSourcePropertyListener_);

         dataSource_.setUser("myUser");

         assertCondition(secondListener.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
         dataSource_.addPropertyChangeListener(dataSourcePropertyListener_);
      }
   }

   /**
   *  Validates that setTransactionIsolation throws a NullPointerException.
   **/
   public void Var048()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setTransactionIsolation(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that addPropertyChangeListener(null) throws a NullPointerException.
   **/
   public void Var049()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.addPropertyChangeListener(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that getConnection(String,String)
   *  v6r1:  security update to not allow "" id/passwords.  To get old behavior and use default id apps have to pass in null.  (on i5)
   **/
   public void Var050()
   {
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCDataSource ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         Connection c = ds.getConnection(null, "passwd".toCharArray());

         failed("Unexpected results."+c);
      }
      catch (SQLException s)
      {  
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that getConnection(String,String) 
   *  v6r1:  security update to not allow "" id/passwords.  To get old behavior and use default id apps have to pass in null.  (on i5)
   **/
   public void Var051()
   {
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         AS400JDBCDataSource ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         Connection c = ds.getConnection("username", (char []) null);

         failed("Unexpected results."+c);
      }
      catch (SQLException s)
      {  
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that removePropertyChangeListener(null) throws a NullPointerException.
   **/
   public void Var052()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.removePropertyChangeListener(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setAccess(null) throws a NullPointerException.
   **/
   public void Var053()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setAccess(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that setCursorHold(true) sets the property as expected.
   **/
   public void Var054()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setCursorHold(true);

         assertCondition(ds.isCursorHold());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that setDatabaseName(null) throws a NullPointerException.
   **/
   public void Var055()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDatabaseName(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that setDataCompression(false) sets the property as expected.
   **/
   public void Var056()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDataCompression(false);

         assertCondition(!ds.isDataCompression());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setDataSourceName(null) throws a NullPointerException.
   **/
   public void Var057()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDataSourceName(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }


   /**
   *  Validates that setDateFormat(null) throws a NullPointerException.
   **/
   public void Var058()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDateFormat(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setDateSeparator(null) throws a NullPointerException.
   **/
   public void Var059()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDateSeparator(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setDecimalSeparator(null) throws a NullPointerException.
   **/
   public void Var060()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDecimalSeparator(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }
   /**
   *  Validates that setDescription(null) throws a NullPointerException.
   **/
   public void Var061()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDescription(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setSort(null) throws a NullPointerException.
   **/
   public void Var062()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setSort(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setErrors(null) throws a NullPointerException.
   **/
   public void Var063()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setErrors(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setExtendedDyanmic(false) sets the property as expected.
   **/
   public void Var064()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setExtendedDynamic(false);

         assertCondition(!ds.isExtendedDynamic());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setLazyClose(true) sets the property as expected.
   **/
   public void Var065()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setLazyClose(true);

         assertCondition(ds.isLazyClose());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setLibraries(null) throws a NullPointerException.
   **/
   public void Var066()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setLibraries(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setNaming(null) throws a NullPointerException.
   **/
   public void Var067()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setNaming(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackage(null) throws a NullPointerException.
   **/
   public void Var068()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackage(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackageAdd(true) sets the property as expected.
   **/
   public void Var069()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageAdd(true);

         assertCondition(ds.isPackageAdd());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackageClear(false) sets the property as expected.
   **/
   public void Var070()
   {
     assertCondition(true, "Do not test deprecated API"); 
   }

   /**
   *  Validates that setPackageCriteria(null) throws a NullPointerException.
   **/
   public void Var071()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageCriteria(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackageError(null) throws a NullPointerException.
   **/
   public void Var072()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageError(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackageLibrary(null) throws a NullPointerException.
   **/
   public void Var073()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageLibrary(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setProxyServer(null) throws a NullPointerException.
   **/
   public void Var074()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setProxyServer(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setRemarks(null) throws a NullPointerException.
   **/
   public void Var075()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setRemarks(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setSecondaryUrl(null) throws a NullPointerException.
   **/
   public void Var076()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setSecondaryUrl(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setServerName(null) throws a NullPointerException.
   **/
   public void Var077()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setServerName(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setDriver(null) throws a NullPointerException.
   **/
   public void Var078()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setDriver(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setSortLanguage(null) throws a NullPointerException.
   **/
   public void Var079()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setSortLanguage(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setSortTable(null) throws a NullPointerException.
   **/
   public void Var080()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setSortTable(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setSortWeight(null) throws a NullPointerException.
   **/
   public void Var081()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setSortWeight(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setTimeFormat(null) throws a NullPointerException.
   **/
   public void Var082()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setTimeFormat(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setTimeSeparator(null) throws a NullPointerException.
   **/
   public void Var083()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setTimeSeparator(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
   *  Validates that setPackageCache(false) sets the property as expected.
   **/
   public void Var084()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageCache(false);

         assertCondition(!ds.isPackageCache());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }

   /**
      Validates that setTrace(boolean) logs properties.
   **/
   public void Var085()
   {
      
      AS400JDBCDataSource ds = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setTrace(true);
         PrintWriter writer = new PrintWriter(new FileWriter(logFileName));
         ds.setLogWriter(writer);
         ds.setExtendedDynamic(true);

         assertCondition(ds.isExtendedDynamic());
      writer.close(); //@A1A  Close writer so file can be cleaned up below.
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         ds.setTrace(false);
      File cleanupFile = new File(logFileName); //@A1A
      if (!cleanupFile.delete()) //@A1A
            System.out.println("WARNING... testcase cleanup could not delete: "+logFileName); //@A1A
      }
   }

   /**
      Validates that setTrace(boolean) logs properties.
   **/
   public void Var086()
   {
     

      AS400JDBCDataSource ds = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setTrace(true);

         ds.setTrace(true);

         assertCondition(ds.isTrace());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         ds.setTrace(false);
      }
   }

   //@A4A
   /**
      Validates that setBidiStringType(int) notifies the PropertyChange listeners.
   **/
   public void Var087()
   {
      try
      {
         dataSource_.setBidiStringType(BidiStringType.ST10);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }



   /**
      Validates that setServerTrace(int) notifies the PropertyChange listeners.
   **/
   public void Var088()
   {
      try
      {
         dataSource_.setServerTraceCategories(3);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }


   //@W1A
   /**
      Validates that setfullOpen(boolean) notifies the PropertyChange listeners.
   **/
   public void Var089()
   {
      try
      {
         dataSource_.setFullOpen(true);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }



   /**
   *  Validates that setServerTraceCategories(int) sets the property as expected.
   **/
   public void Var090()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setServerTraceCategories(123);
         if (ds.getServerTraceCategories() == 123)
            succeeded();
         else
            failed("expected 123, received " + ds.getServerTraceCategories());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception.");
      }
   }




   /**
      @K1A
      Validates that setToolboxTraceCategory(String) notifies the PropertyChange listeners.
   **/
   public void Var091()
   {
      try
      {
         dataSource_.setToolboxTraceCategory("jdbc");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/10/2003 by Toolbox to check toolbox trace property.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   /**
   *  @K1A
   *  Validates that setToolboxTraceCategory(String) sets the property as expected.
   **/
   public void Var092()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setToolboxTraceCategory("jdbc");
         if (ds.getToolboxTraceCategory().equals("jdbc"))
            succeeded();
         else
            failed("expected jdbc, received " + ds.getToolboxTraceCategory());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/10/2003 by Toolbox to check toolbox trace property.");
      }
   }

   /**
      @K1A
      Validates that setToolboxTraceCategory(String) logs properties.
   **/
   public void Var093()
   {
      

      AS400JDBCDataSource ds = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setToolboxTraceCategory("jdbc");
         PrintWriter writer = new PrintWriter(new FileWriter(logFileName));
         ds.setLogWriter(writer);
         ds.setExtendedDynamic(true);

         assertCondition(ds.isExtendedDynamic());
      writer.close(); //@K1A  Close writer so file can be cleaned up below.
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/10/2003 by Toolbox to check toolbox trace property.");
      }
      finally
      {
         ds.setToolboxTraceCategory("none");
      File cleanupFile = new File(logFileName); //@K1A
      if (!cleanupFile.delete()) //@K1A
            System.out.println("WARNING... testcase cleanup could not delete: "+logFileName); //@K1A
      }
   }

   //K1A
   /**
      Validates that setToolboxTraceCategory(String) logs properties.
   **/
   public void Var094()
   {
      

      AS400JDBCDataSource ds = null;
      try
      {
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
         ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
   PasswordVault.clearPassword(charPassword);
         ds.setToolboxTraceCategory("jdbc");

         ds.setToolboxTraceCategory("jdbc");

         assertCondition(ds.getToolboxTraceCategory().equals("jdbc"));
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/10/2003 by Toolbox to check toolbox trace property.");
      }
      finally
      {
         ds.setTrace(false);
      }
   }

   //@K2A
   /**
      Validates that setMinimumDivideScale(int) notifies the PropertyChange listeners.
   **/
   public void Var095()
   {
      try
      {
         dataSource_.setMinimumDivideScale(1);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception.");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var096()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(9);
         if (ds.getMinimumDivideScale() == 9)
            succeeded();
         else
            failed("expected 9, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int)");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var097()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(1);
         if (ds.getMinimumDivideScale() == 1)
            succeeded();
         else
            failed("expected 1, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var098()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(2);
         if (ds.getMinimumDivideScale() == 2)
            succeeded();
         else
            failed("expected 2, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var099()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(3);
         if (ds.getMinimumDivideScale() == 3)
            succeeded();
         else
            failed("expected 3, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var100()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(4);
         if (ds.getMinimumDivideScale() == 4)
            succeeded();
         else
            failed("expected 4, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var101()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(5);
         if (ds.getMinimumDivideScale() == 5)
            succeeded();
         else
            failed("expected 5, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var102()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(6);
         if (ds.getMinimumDivideScale() == 6)
            succeeded();
         else
            failed("expected 6, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var103()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(7);
         if (ds.getMinimumDivideScale() == 7)
            succeeded();
         else
            failed("expected 7, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMinimumDivideScale(int) sets the property as expected.
   **/
   public void Var104()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMinimumDivideScale(8);
         if (ds.getMinimumDivideScale() == 8)
            succeeded();
         else
            failed("expected 8, received " + ds.getMinimumDivideScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMinimumDivideScale(int).");
      }
   }

   //@K2A
   /**
      Validates that setMaximumScale(int) notifies the PropertyChange listeners.
   **/
   public void Var105()
   {
      try
      {
         dataSource_.setMaximumScale(63);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumScale(int).");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K2A
   /**
   *  Validates that setMaximumScale(int) sets the property as expected.
   **/
   public void Var106()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMaximumScale(63);
         if (ds.getMaximumScale() == 63)
            succeeded();
         else
            failed("expected 63, received " + ds.getMaximumScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumScale(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMaximumScale(int) sets the property as expected.
   **/
   public void Var107()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMaximumScale(31);
         if (ds.getMaximumScale() == 31)
            succeeded();
         else
            failed("expected 31, received " + ds.getMaximumScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumScale(int).");
      }
   }

   //@K2A
   /**
      Validates that setMaximumPrecision(int) notifies the PropertyChange listeners.
   **/
   public void Var108()
   {
      try
      {
         dataSource_.setMaximumPrecision(63);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumPrecision(int).");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K2A
   /**
   *  Validates that setMaximumPrecision(int) sets the property as expected.
   **/
   public void Var109()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMaximumPrecision(63);
         if (ds.getMaximumPrecision() == 63)
            succeeded();
         else
            failed("expected 63, received " + ds.getMaximumScale());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumPrecision(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMaximumPrecision(int) sets the property as expected.
   **/
   public void Var110()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setMaximumPrecision(31);
         if (ds.getMaximumPrecision() == 31)
            succeeded();
         else
            failed("expected 31, received " + ds.getMaximumPrecision());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumPrecision(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setMaximumScale(int) sets the property as expected.
   **/
   public void Var111()
   {
      try
      {
          boolean fail = false;
          String failedMessage = "";
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         //check that it works for integers 0 - 63
         for(int i = 0; i<64; i++)
         {
             ds.setMaximumScale(i);
             if( ds.getMaximumScale() != i )
             {
                 fail = true;
                 failedMessage = failedMessage + "expected " + i + " recieved " + ds.getMaximumScale();
             }

         }
         if(fail != true)
            succeeded();
         else
             failed(failedMessage);
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setMaximumScale(int).");
      }
   }

   //@K2A
   /**
      Validates that setPackageCCSID(int) notifies the PropertyChange listeners.
   **/
   public void Var112()
   {
      try
      {
         dataSource_.setPackageCCSID(1200);

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K2A
   /**
   *  Validates that setPackageCCSID(int) sets the property as expected.
   **/
   public void Var113()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageCCSID(1200);
         if (ds.getPackageCCSID() == 1200)
            succeeded();
         else
            failed("expected 1200, received " + ds.getPackageCCSID());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
      }
   }

   //@K2A
   /**
   *  Validates that setPackageCCSID(int) sets the property as expected.
   **/
   public void Var114()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setPackageCCSID(13488);
         if (ds.getPackageCCSID() == 13488)
            succeeded();
         else
            failed("expected 13488, received " + ds.getPackageCCSID());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setPackageCCSID(int).");
      }
   }

   //@K2A
   /**
      Validates that setTranslateHex(String) notifies the PropertyChange listeners.
   **/
   public void Var115()
   {
      try
      {
         dataSource_.setTranslateHex("binary");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setTranslateHex(String).");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K2A
   /**
   *  Validates that setTranslateHex(String) sets the property as expected.
   **/
   public void Var116()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setTranslateHex("binary");
         if (ds.getTranslateHex().equals("binary"))
            succeeded();
         else
            failed("expected binary, received " + ds.getTranslateHex());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setTranslateHex(String).");
      }
   }

   //@K2A
   /**
   *  Validates that setTranslateHex(String) sets the property as expected.
   **/
   public void Var117()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setTranslateHex("character");
         if (ds.getTranslateHex().equals("character"))
            succeeded();
         else
            failed("expected character, received " + ds.getTranslateHex());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 3/26/2003 by Toolbox to test setTranslateHex(String).");
      }
   }

   //@K3A
   /**
      Validates that setQaqqiniLibrary(String) notifies the PropertyChange listeners.
   **/
   public void Var118()
   {
      try
      {
         dataSource_.setQaqqiniLibrary("test");

         assertCondition(dataSourcePropertyListener_.isChangeNotified());
      }
      catch(Exception e)
      {
         failed(e, "Unexpected exception:  Added 4/7/2003 by Toolbox to test setQaqqiniLibrary(String).");
      }
      finally
      {
         dataSourcePropertyListener_.reset();
      }
   }

   //@K3A
   /**
   *  Validates that setQaqqiniLibrary(null) throws a NullPointerException.
   **/
   public void Var119()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setQaqqiniLibrary(null);

         failed("Unexpected results.");
      }
      catch (NullPointerException np)
      {
         succeeded();
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 4/7/2003 by Toolbox to test setQaqqiniLibrary(null).");
      }
   }

   //@K3A
   /**
   *  Validates that setQaqqiniLibrary(String) sets the property as expected.
   **/
   public void Var120()
   {
      try
      {
         AS400JDBCDataSource ds = new AS400JDBCDataSource();
         ds.setQaqqiniLibrary("test");
         if (ds.getQaqqiniLibrary().equals("test"))
            succeeded();
         else
            failed("expected test, received " + ds.getQaqqiniLibrary());
      }
      catch (Exception e)
      {
         failed(e, "Unexpected exception:  Added 4/7/2003 by Toolbox to test setQaqqiniLibrary(String).");
      }
   }
   
   /**
    *   setConcurrentAccessResolution(), getConcurrentAccessResolution()  
    **/
   public void Var121()
   {
    
       
       try
       { 
           if (systemObject_.getVRM() <   AS400.generateVRM (7, 1, 0)) {
               notApplicable("v7r1 only");
               return;
           }
           
           
           AS400JDBCDataSource ds = new AS400JDBCDataSource();
           ds.setConcurrentAccessResolution(1);
           if (ds.getConcurrentAccessResolution() == 1)
               succeeded();
           else
               failed("expected 1, received " + ds.getConcurrentAccessResolution());
       }
       catch (Exception e)
       {
           failed(e, "Unexpected exception:  Added 10/2/2008 by Toolbox.");
       }
   }

   /**
    *   override in connection of ds setConcurrentAccessResolution(), getConcurrentAccessResolution() and url
    **/
   public void Var122()
   {
       
   
       try
       {
           if (systemObject_.getVRM() <   AS400.generateVRM (7, 1, 0)) {
               notApplicable("v7r1 only");
               return;
           }
           Class.forName("com.ibm.as400.access.AS400JDBCDriver");
           
           
           
   char[] charPassword = PasswordVault.decryptPassword(encryptedPassword_);
           AS400JDBCDataSource ds = new AS400JDBCDataSource(systemObject_.getSystemName(), systemObject_.getUserId(), charPassword);
        
           //ds.setConcurrentAccessResolution(1);
           AS400JDBCConnection c = (AS400JDBCConnection) ds.getConnection(systemObject_.getUserId(), charPassword);
           int i1 = c.getConcurrentAccessResolution();  //should be 0 default, never been set
           
           ds.setConcurrentAccessResolution(1);
           int i2 = c.getConcurrentAccessResolution();  //should still be 0 with no change from datasource setting
           
           AS400JDBCConnection c2 = (AS400JDBCConnection) ds.getConnection(systemObject_.getUserId(), charPassword);
           int i3 = c2.getConcurrentAccessResolution();  //should be 1, from ds
   PasswordVault.clearPassword(charPassword);
           
           c.setConcurrentAccessResolution(2);  
           int i4 = c2.getConcurrentAccessResolution();  //should be 1
           int i5 = c.getConcurrentAccessResolution();  //should be 2
           
           
           if (i1 == 0 && i2 == 0 && i3 == 1 && i4 == 1 && i5 == 2 )
               succeeded();
           else
               failed();
       }
       catch (Exception e)
       {
           failed(e, "Unexpected exception:  Added 10/2/2008 by Toolbox.");
       }
   }


   /**
    * Place holders
    */
   public void Var123()
   {
       assertCondition(true);
   }
   public void Var124()
   {
       assertCondition(true);
   }


   
   /**
   Performs setup needed before running testcases.
    @exception  Exception  If an exception occurs.
   **/
   public void setup() throws Exception
   {
      dataSourcePropertyListener_ = new DataSourcePropertyListener();

      dataSource_ = new AS400JDBCDataSource();
      dataSource_.addPropertyChangeListener(dataSourcePropertyListener_);
   }

   class DataSourcePropertyListener implements PropertyChangeListener
   {
      private boolean propertyChange_;

      public DataSourcePropertyListener()
      {
         propertyChange_ = false;
      }
      public void propertyChange(PropertyChangeEvent event)
      {
         propertyChange_ = true;
      }

      public boolean isChangeNotified()
      {
         return propertyChange_;
      }

      public void reset()
      {
         propertyChange_ = false;
      }
   }
}
