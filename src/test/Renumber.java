// Created 07/21/1998 by C. R. Smith
// 07/21/2000 - CRS - Updated to handle core-reflected files automagically.

package test; 

import java.io.*;

public class Renumber
{
  public static int totalVars;
  public static int subTotalCallSection;
  public static int subTotalMethodSection;
  public static boolean totalSet;
  public static int varNumber;
  public static String varNumStr;
  public static String varNumPadStr;
  public static boolean validFile;
  public static Runtime rt = Runtime.getRuntime();
  
  public static void main(String[] args)
  {
    // Verify parms
    if (args.length < 1)
    {
      usage();
      System.exit(0);
    }

    for (int n=0; n<args.length; ++n)
    {
      String filename = args[n];
      int ret = renum(filename);
      switch(ret)
      {
        case 0:
          System.out.println(filename+" was processed, but had no variations.");
          break;
        case -1:
          System.out.println("ERROR: "+filename+" does not exist.");
          break;
        case -2:
          System.out.println("ERROR: "+filename+" contains more than one class.");
          break;
        case -3:
          System.out.println("ERROR: "+filename+" does not extend a known testcase superclass.");
          break;
        case -4:
          System.out.println("ERROR: "+filename+" broke the parser.");
          break;
        case -5:
          System.out.println("ERROR: "+filename+" is not in a valid testcase format.");
          break;
        case -6:
          System.out.println("ERROR: "+filename+" has mismatched variations.");
          break;
        case -7:
          System.out.println(filename+" processed succesfully. Unable to remove "+filename+".renum");
          break;
        case -8:
          System.out.println("ERROR: "+filename+" has multiple run() methods.");
          break;
        default:
          System.out.println(filename+" with "+ret+" variations was processed successfully.");
      }
    }
  }

  @SuppressWarnings("resource")
  public static int renum(String filename)
  {
    totalVars = -1;
    subTotalCallSection = -1;
    subTotalMethodSection = -1;
    totalSet = false;

    BufferedReader in = null;
    BufferedWriter out = null;
    try
    {
      // Error checking
      File input = new File(filename);
      if (!input.exists())
      {
        return -1;
      }
  
      // Check to make sure that we are parsing the right file
      // - Check for multiple class statements
      // - Check for "extends Testcase" or "extends JDTestcase"
      //
      // Re-number the testcase
      // 3 sections:
      //  - Handle the total number of variations (do this at the end)
      //    a. super(..., "name", N, ......)
      //  - Handle the if block statements
      //    a. if (... variationsToRun_.contains("N")...)
      //    b. setVariation(N);
      //    c. VarNNN();
      //  - Handle the variation numbers on each method name
      //    a. ... void VarNNN() ...

      in = new BufferedReader(new FileReader(filename));
      out = new BufferedWriter(new FileWriter(filename+".renum"));
      varNumber = 0;
      String line = null;
      StringBuffer varLine = null;
      varNumStr = null;
      varNumPadStr = null;
      // int index = 0;
      boolean inMethodSection = false;
      boolean inClass = false;
      validFile = false;
      boolean inRun = false;
      for (line = in.readLine(); line != null; line = in.readLine())
      {
        // Check for more than one class definition in the file
        if (begins(line, "public class") != -1)
        {
          if (inClass)
          {
            return -2;
          }
          inClass = true;
          varLine = new StringBuffer(line);
        }

        // Check for correct superclass
        int extendIndex = line.indexOf("extends");
        if (inClass && extendIndex != -1 &&
            (begins(line, "public class") != -1 ||
             begins(line, "extends") != -1))
        {
          String extendStr = line.substring(extendIndex+7);
          if (begins(extendStr, "Testcase") == -1 &&
              begins(extendStr, "JDTestcase") == -1)
          {
            return -3;
          }
          validFile = true;
          varLine = new StringBuffer(line);
        }

        // Check for run() method
        else if (validFile && check(line, "public void run", "()", true) != -1)
        {
          if (inRun)
          {
            return -8;
          }
          inRun = true;
          varLine = new StringBuffer(line);
        }

        // Check for variation numbers

        // Occurrences of this are in the if block
        else if (!inMethodSection && validFile &&
            check(line, "variationsToRun_.contains", "))", false) != -1)
        {
          increment();
          varLine = set(line, "variationsToRun_.contains", "))", "(\""+varNumStr+"\"", false);
        }

        // Occurrences of this are in the if block or the method block
        else if (validFile &&
                 check(line, "setVariation", ");", true) != -1)
        {
          varLine = set(line, "setVariation", ");", "("+varNumStr, true);
        }

        // Occurrences of this are in the if block
        // Formats differ: Var001(); or variation01();
        else if (!inMethodSection && validFile && 
                 check(line, "Var", "();", true) != -1 &&
                 check(line, "Var", "();", true) < 8)
        {
          varLine = set(line, "Var", "();", varNumPadStr, true);
        }
        else if (!inMethodSection && validFile && 
                 check(line, "variation", "();", true) != -1 &&
                 check(line, "variation", "();", true) < 8)
        {
          varLine = set(line, "variation", "();", varNumPadStr, true);
        }

        // Occurrences of this are in the method block
        // Formats differ: Var001(); or variation01();
        else if (validFile &&
                 check(line, "public void Var", "()", true) != -1)
        {
          if (!inMethodSection)
          {
            subTotalCallSection = varNumber;
            varNumber = 0;
            inMethodSection = true;
            inRun = false;
          }
          increment();
          varLine = set(line, "public void Var", "()", varNumPadStr, true);
        }
        else if (validFile &&
                 check(line, "public void variation", "()", true) != -1)
        {
          if (!inMethodSection)
          {
            subTotalCallSection = varNumber;
            varNumber = 0;
            inMethodSection = true;
            inRun = false;
          }
          increment();
          varLine = set(line, "public void variation", "()", varNumPadStr, true);
        }

        // None found, just write out the line.
        else
        {
          varLine = new StringBuffer(line);
        }

        out.write(varLine.toString());
        out.newLine();
      }
      subTotalMethodSection = varNumber;
      out.flush();
    }
    catch(Exception e)
    {
      System.out.println("Exception occurred:");
      e.printStackTrace();
      System.out.println("Input file not overwritten. Output in 'renum.tmp'");
      try { 
      in.close(); 
      out.close(); 
      } catch (Exception e2) { }
      return -4;
    }
    try
    {
      in.close();
    }
    catch(Exception e)
    {
    }
    try
    {
      out.close();
    }
    catch(Exception e)
    {
    }

    if (!validFile)
    {
      return -5;
    }
    if (subTotalCallSection != subTotalMethodSection && subTotalCallSection > 0)
    {
      return -6;
    }
    else
    {
      totalVars = subTotalMethodSection;
    }


    // Write the temp file over the input file
    try
    {
      in = new BufferedReader(new FileReader(filename+".renum"));
      out = new BufferedWriter(new FileWriter(filename));
      String line = null;
      for (line = in.readLine(); line != null; line = in.readLine())
      {
        // Adjust the total variations line
        if (begins(line, "super(") != -1)
        {
          int secondParmIndex = line.indexOf(",")+1;
          String secondParm = line.substring(secondParmIndex);
          int thirdParmIndex = secondParm.indexOf(",") + secondParmIndex +1;
          String thirdParm = line.substring(thirdParmIndex);
          int firstDigit = -1;
          for (int k=0; k<10; ++k)
          {
            if (begins(thirdParm, String.valueOf(k)) != -1 && firstDigit == -1)
            {
              firstDigit = k;
            }
          }
          if (firstDigit != -1)
          {
            int superTotalLast = thirdParm.indexOf(",")+thirdParmIndex;
            int superTotalFirst = begins(thirdParm, String.valueOf(firstDigit))+thirdParmIndex;
            StringBuffer totalLine = new StringBuffer(line.substring(0, superTotalFirst));
            totalLine.append(String.valueOf(totalVars));
            totalLine.append(line.substring(superTotalLast));
            out.write(totalLine.toString());
          }
          else
          {
            out.write(line);
          }
        }
        else
        {
          out.write(line);
        }
        out.newLine();
      }
      out.flush();
    }
    catch(Exception e)
    {
      System.out.println("Exception occurred:");
      e.printStackTrace();
    }
    try
    {
      in.close();
    }
    catch(Exception e)
    {
    }
    try
    {
      out.close();
    }
    catch(Exception e)
    {
    }

    // Delete the temp file
    try
    {
      File toDelete = new File(filename+".renum");
      toDelete.delete();
    }
    catch(Exception e)
    {
      return -7;
    }

    return totalVars;
  }

  public static int begins(String line, String pattern)
  {
    int index = line.indexOf(pattern);
    if (index == -1)
      return -1;
    for (int i=0; i<index; ++i)
    {
      if (line.charAt(i) != ' ' && line.charAt(i) != '\t')
        return -1;
    }
    return index;
  }

  public static void increment()
  {
    varNumber++;
    varNumStr = new String(String.valueOf(varNumber));
    varNumPadStr = new String(((varNumber < 100) ? "0" : "") + 
                              ((varNumber < 10) ? "0" : "") + 
                               varNumStr);
  }

  public static void usage()
  {
    System.out.println("Usage: java Renumber [file_spec]");
    System.out.println("  file_spec  The testcase or testcases that need to be re-numbered.");
    System.out.println("\nRenumber will overwrite the files if they are parsed successfully.");
    System.out.println("If an error should occur, the original file will not be overwritten");
    System.out.println("and the parser's output will be directed to \"FILENAME.renum\".");
  }

  public static int check(String line, String startPattern, String endPattern, boolean begin)
  {
    // StringBuffer newLine = null;
    // startPattern is at beginning of line, ignoring leading spaces
    // startIndex is the point at whith startPattern is found in line
    int startIndex = -1;
    if (begin)
      startIndex = begins(line, startPattern);
    else
      startIndex = line.indexOf(startPattern);
    int endIndex = -1;
    // endIndex is the point at which endPattern is found in line after startPattern
    if (startIndex != -1)
      endIndex = ((line.substring(startIndex)).indexOf(endPattern))+startIndex;
    // patternDistance is the distance between the end of startPattern and the beginning of endPattern in line
    int patternDistance = endIndex - startIndex - startPattern.length();
    if (startIndex != -1 && endIndex != -1 && patternDistance >= 0)
      return patternDistance;
    else
      return -1;
  }

  public static StringBuffer set(String line, String startPattern, String endPattern, String replace, boolean begin)
  {
    if (line == null || startPattern == null || endPattern == null || replace == null)
      throw new NullPointerException("null parms");

    StringBuffer newLine = null;
    // startPattern is at beginning of line, ignoring leading spaces
    // startIndex is the point at whith startPattern is found in line
    int startIndex = -1;
    if (begin)
      startIndex = begins(line, startPattern);
    else
      startIndex = line.indexOf(startPattern);
    int endIndex = -1;
    // endIndex is the point at which endPattern is found in line after startPattern
    if (startIndex != -1)
      endIndex = ((line.substring(startIndex)).indexOf(endPattern))+startIndex;
    // patternDistance is the distance between the end of startPattern and the beginning of endPattern in line
    int patternDistance = endIndex - startIndex - startPattern.length();
    if (startIndex != -1 && endIndex != -1)
    {
      newLine = new StringBuffer(line.substring(0, startIndex));
      newLine.append(startPattern);
      newLine.append(replace);
      newLine.append(line.substring(startIndex + startPattern.length() + patternDistance));
      return newLine;
    }
    else
    {
      throw new NullPointerException("no check");
    }
  }

}
