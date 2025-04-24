package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64.Encoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
// import javax.xml.bind.DatatypeConverter; 

public class HTTPSReader {
  public static boolean debug = false; 
  
  public static StringBuffer getUrlData(String urlString, String userid, String password ) throws IOException {

    StringBuffer outputBuffer = new StringBuffer(); 
    URL url = new URL(urlString); 
    URLConnection connection = url.openConnection();
    if (connection instanceof HttpsURLConnection) {
      HttpsURLConnection httpCon = (HttpsURLConnection) connection; 
      httpCon.addRequestProperty("User-Agent", "Mozilla/5.0");


      String authString = userid + ":" + password;
      if (debug) System.out.println("auth string: " + authString);
      Encoder encoder = java.util.Base64.getMimeEncoder(); 
      
      // String authStringEnc =  DatatypeConverter.printBase64Binary(authString.getBytes("UTF-8"));
      String authStringEnc = new String(encoder.encode(authString.getBytes("UTF-8"))); 
      
      
      
      if (debug) System.out.println("Base64 encoded auth string: " + authStringEnc);

      httpCon.setRequestProperty("Authorization", "Basic " + authStringEnc);

     
    }
    if (debug) System.out.println("Connection is "+connection);
    Class<?> cl = connection.getClass(); 
    if (debug) System.out.println(" class is "+cl); 
    Class<?>[] interfaces = cl.getInterfaces();
    if (debug) System.out.println("interface count is "+interfaces.length); 
    for (int i =0; i < interfaces.length; i++) {
      if (debug) System.out.println("Interface: "+interfaces[i]); 
    }
    Class<?> superClass = cl.getSuperclass(); 
    while (superClass != null) {
      String className = superClass.getName();
      if (debug) System.out.println("Superclass ="+className);
      if (className.indexOf("java.lang.Object") > 0) {
        superClass = null;  
      } else { 
        superClass = superClass.getSuperclass();
      }
    }
    
    InputStream is = connection.getInputStream();
    
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is)); 
    
    String line = bufferedReader.readLine(); 
    while (line != null) { 
      outputBuffer.append(line); 
      outputBuffer.append("\n"); 
      line = bufferedReader.readLine();
    }
    bufferedReader.close(); 
    
    if (debug) {
	System.out.println(" ---- read ----- ");
	System.out.println(outputBuffer);

    } 
    return outputBuffer; 
    
  }

  public static StringBuffer getUrlData(String urlString ) throws IOException {
    StringBuffer outputBuffer = new StringBuffer(); 
    URL url = new URL(urlString); 
    URLConnection connection = url.openConnection();
    if (connection instanceof HttpsURLConnection) {
      HttpsURLConnection httpCon = (HttpsURLConnection) connection; 
      httpCon.addRequestProperty("User-Agent", "Mozilla/5.0");


     
    }
    if (debug) System.out.println("Connection is "+connection);
    Class<?> cl = connection.getClass(); 
    if (debug) System.out.println(" class is "+cl); 
    Class<?>[] interfaces = cl.getInterfaces();
    if (debug) System.out.println("interface count is "+interfaces.length); 
    for (int i =0; i < interfaces.length; i++) {
      if (debug) System.out.println("Interface: "+interfaces[i]); 
    }
    Class<?> superClass = cl.getSuperclass(); 
    while (superClass != null) {
      String className = superClass.getName();
      if (debug) System.out.println("Superclass ="+className);
      if (className.indexOf("java.lang.Object") > 0) {
        superClass = null;  
      } else { 
        superClass = superClass.getSuperclass();
      }
    }
    
    InputStream is = connection.getInputStream();
    
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is)); 
    
    String line = bufferedReader.readLine(); 
    while (line != null) { 
      outputBuffer.append(line); 
      outputBuffer.append("\n"); 
      line = bufferedReader.readLine();
    }
    bufferedReader.close(); 
    
    // if (false) {
    // System.out.println(" ---- read ----- ");
    // System.out.println(outputBuffer);
    // } 
    return outputBuffer; 
    
  }
  



  public static StringBuffer toText(StringBuffer sb) {
    // 
    // Convert the output to text only 
    //
    StringBuffer sbIn = new StringBuffer(sb.toString());
    String[] replacePattern= {
        "<table[^>]*>",
        "</table[^>]*>",
        "<a [^>]*>",
        "</a[^>]*>",
        "<img[^>]*>",
        "<th[^>]*>",
        "</th[^>]*>",
      "<tr[^>]*>",
      "</tr[^>]*>",
      "<td[^>]*>",
      "</td>",
      "<font[^>]*>",
      "</font[^>]*>",
      "<script[^>]*>",
      "</script[^>]*>",
      "<style[^>]*>",
      "</style[^>]*>",
      "<!DOCTYPE[^>]*>",
      "<!doctype[^>]*>",
      "<!--\\n[^\\n]*\n-->",
      "<!--[^-]*-->",
      "<html[^>]*>",
      "<head>",
      "</head>",
      "<body[^>]*>",
      "</body>",
      "<title>",
      "</title>",
      "<link[^>]*>",
      "<meta[^>]*>",
      "<p[^>]*>",
      "<noscript>",
      "</noscript>",
      "<html>",
      "</html>",
      "<h1>",
      "</h1>",
      "<tbody>",
      "</tbody>",
      "<br>",
      "%<b>% %",
      "%</b>% %",
      "%   *% %",
      "%  *\\n%\n%",
      "%\\n\\n[\\n]*%\n%",
      "%[\\t]*&lt;%<%",
      "%[\\t]*&gt;%>%",
      "%td style=\"width: [0-9]*px\"%td%",
      "%<strong>%%",
      "%</strong>%%",
      "%<tr>\\n%<tr>%",
      "%</td>\\n%</td>%",
    }; 
    for (int i = 0; i < replacePattern.length; i++) { 
      String pattern = replacePattern[i];
      String replacement=""; 
      if (pattern.charAt(0)=='%') {
        pattern=pattern.substring(1); 
        int percentIndex = pattern.indexOf("%"); 
        if (percentIndex > 0) { 
          replacement = pattern.substring(percentIndex+1, pattern.length() -1 );
          pattern = pattern.substring(0,percentIndex); 
        }
      }
      Pattern p = Pattern.compile(pattern);
      Matcher m = p.matcher(sbIn);
      StringBuffer sbOut = new StringBuffer();
      while (m.find()) {
          m.appendReplacement(sbOut, replacement);
    }
    m.appendTail(sbOut);
    sbIn=sbOut; 
    }
    return sbIn;     
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    String urlString="https://w3-connections.ibm.com/wikis/basic/api/wiki/iMB/page/DB%20Minibuild%20Status/entry?acls=true&includeRecommendation=true&includeTags=true&inline=true"; 
    try { 
      String userid = args[0];
      String password = args[1];

      //urlString = "https://w3-connections.ibm.com/wikis/home";
      //urlString = "https://www-912.ibm.com/j_dir/JTOpen.nsf/%28$All%29?OpenView";
      StringBuffer sb = getUrlData(urlString, userid, password );  
      System.out.println(sb.toString()); 
      
      System.out.println("\n------------------- RAW TEXT --------------------------\n"); 
      System.out.println(toText(sb)); 
      
      
      
    } catch (Exception e) { 
      e. printStackTrace(System.out); 
      Throwable cause = e.getCause(); 
      while (cause != null) {
        System.out.println("Caused by "); 
        cause.printStackTrace(System.out); 
        cause = cause.getCause();
      }
    }
    
    
  }

}
