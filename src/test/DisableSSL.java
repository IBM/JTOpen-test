///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  DisableSSL.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

package test;
import javax.net.ssl.*;
import java.security.cert.*;

public class DisableSSL implements X509TrustManager, HostnameVerifier {
    
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];     }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {    }
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
            String authType) {  }
    public boolean verify(String string, SSLSession ssls) {
	return true;
    }

    public static boolean sslDisabled = false; 

  public static void go() throws Exception {
    if (!sslDisabled) {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[] { new DisableSSL() },
          new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new DisableSSL());
      sslDisabled=true; 
    }

  }

}
