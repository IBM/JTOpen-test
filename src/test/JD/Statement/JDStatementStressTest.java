///////////////////////////////////////////////////////////////////////////////
//
// JTOpen (IBM Toolbox for Java - OSS version)
//
// Filename:  JDStatementStressTest.java
//
// The source code contained herein is licensed under the IBM Public License
// Version 1.0, which has been approved by the Open Source Initiative.
// Copyright (C) 1997-2023 International Business Machines Corporation and
// others.  All rights reserved.
//
///////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////
//
// File Name:    JDStatementStressTest.java
//
// Classes:      JDStatementStressTest
//
////////////////////////////////////////////////////////////////////////

package test.JD.Statement;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ConvTable;

import test.JDJobName;
import test.JDSetupProcedure;
import test.JDStatementTest;
import test.JDTestDriver;
import test.JDTestcase;
import test.JTOpenTestEnvironment;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Testcase JDStatementStressTest. This tests multithreaded use of parts of the
 * JDBC driver.
 * 
 * This also stresses the use of different CCSIDs..
 * 
 **/
public class JDStatementStressTest extends JDTestcase {
  public static void main(String args[]) throws Exception {
    String[] newArgs = new String[args.length+2];
     newArgs[0] = "-tc";
     newArgs[1] = "JDStatementStressTest";
     for (int i = 0; i < args.length; i++) {
       newArgs[2+i]=args[i];
     }
     test.JDStatementTest.main(newArgs); 
   }

  class CCSIDTestData {
    public int ccsid;
    public String data;

    public CCSIDTestData(int ccsid, String data) {
      this.ccsid = ccsid;
      this.data = data;
    }
  }

  private static final int PROBABILITY = 3;
  private static final int AGE = 1;

  // Private data.
  protected Connection connection_;
  protected Statement statement_;
  Vector<Connection> badConnections = new Vector<Connection>();
  protected int ustNum = 20;
  protected int cstNum = 20;
  protected int sstNum = 20;
  protected long runMillis_ = 180000;
  protected int keyBlock = 50;

  public static int [] oneCcsid= {5473,13676,1175,1379,1371,935, 930, 1399, 5026, 5035, 61175, 939}; 

  public static String TABLE_ = JDStatementTest.COLLECTION + ".STRESS";
  public static String PROC_ = JDStatementTest.COLLECTION + ".STRPRC";
  public static String ccsidBase_ = JDStatementTest.COLLECTION + ".STRC";

  boolean runningJ9 = false;

  //
  // Set up the data to be tested. The data should be able to roundtrip
  // through the CCSID. In most cases, the data represent the
  // ebcdic characters for the hexidecimal values from 0x40 to 0xFE
  public CCSIDTestData[] ccsidTestData = { new CCSIDTestData(37,
      " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2.<(+|"
          + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);\u00ac"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "^\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u00af\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
          + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
          + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(256,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1[.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df]$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u20a7\u0192\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u2017"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u0131\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u2007STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(273, "hello this is code page 273"
          + " \u00a0\u00e2{\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00c4.<(+!"
          + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec~\u00dc$*);^"
          + "-/\u00c2[\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f6,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#\u00a7\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "\u00b5\u00dfstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2\u00a3\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
          + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
          + "\u00fcJKLMNOPQR\u00b9\u00fb}\u00f9\u00fa\u00ff"
          + "\u00d6\u00f7STUVWXYZ\u00b2\u00d4\\\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db]\u00d9\u00da"),
      new CCSIDTestData(277, "hello this is code page 277"
          + " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3}\u00e7\u00f1#.<(+!"
          + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u00a4\u00c5*);^"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f8,%_>\u003f"
          + "\u00a6\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:\u00c6\u00d8\u0027=\""
          + "@abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba{\u00b8[]"
          + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
          + "\u00e6ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
          + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
          + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(278, "hello this is code page 278"
          + " \u00a0\u00e2{\u00e0\u00e1\u00e3}\u00e7\u00f1\u00a7.<(+!"
          + "&`\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u00a4\u00c5*);^"
          + "-/\u00c2#\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f6,%_>\u003f"
          + "\u00f8\\\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00e9:\u00c4\u00d6\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6]"
          + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2\u00a3\u00a5\u00b7\u00a9[\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
          + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
          + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
          + "\u00c9\u00f7STUVWXYZ\u00b2\u00d4@\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(280, "hello this is code page 280"
          + " \u00a0\u00e2\u00e4{\u00e1\u00e3\u00e5\\\u00f1\u00b0.<(+!"
          + "&]\u00ea\u00eb}\u00ed\u00ee\u00ef~\u00df\u00e9$*);^"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f2,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00f9:\u00a3\u00a7\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "[jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "\u00b5\u00ecstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2#\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
          + "\u00e0ABCDEFGHI\u00ad\u00f4\u00f6\u00a6\u00f3\u00f5"
          + "\u00e8JKLMNOPQR\u00b9\u00fb\u00fc`\u00fa\u00ff"
          + "\u00e7\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(284, "hello this is code page 284"
          + " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00a6[.<(+|"
          + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df]$*);\u00ac"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7#\u00f1,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:\u00d1@\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "\u00b5\u00a8stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be^!\u00af~\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
          + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
          + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(285, "hello this is code page 285"
          + " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1$.<(+|"
          + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!\u00a3*);\u00ac"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "\u00b5\u00afstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2[\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be^]~\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
          + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
          + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(290, "hello this is code page 290"
          + " \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\uff69\u00a3.<(+|"
          + "&\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f \uff70 !\u00a5*);\u00ac"
          + "-/abcdefgh ,%_>\u003f" + "[ijklmnop`:#@\u0027=\""
          + "]\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79\uff7aq\uff7b\uff7c\uff7d\uff7e"
          + "\uff7f\uff80\uff81\uff82\uff83\uff84\uff85\uff86\uff87\uff88\uff89r \uff8a\uff8b\uff8c"
          + "~\u203e\uff8d\uff8e\uff8f\uff90\uff91\uff92\uff93\uff94\uff95s\uff96\uff97\uff98\uff99"
          + "^\u00a2\\tuvwxyz\uff9a\uff9b\uff9c\uff9d\uff9e\uff9f"
          + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "$ STUVWXYZ      "
          + "0123456789     "),
      new CCSIDTestData(297, "hello this is code page 297"
          + " \u00a0\u00e2\u00e4@\u00e1\u00e3\u00e5\\\u00f1\u00b0.<(+!"
          + "&{\u00ea\u00eb}\u00ed\u00ee\u00ef\u00ec\u00df\u00a7$*);^"
          + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f9,%_>\u003f"
          + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00b5:\u00a3\u00e0\u0027=\""
          + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
          + "[jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
          + "`\u00a8stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
          + "\u00a2#\u00a5\u00b7\u00a9]\u00b6\u00bc\u00bd\u00be\u00ac|\u00af~\u00b4\u00d7"
          + "\u00e9ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
          + "\u00e8JKLMNOPQR\u00b9\u00fb\u00fc\u00a6\u00fa\u00ff"
          + "\u00e7\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
          + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(420, "hello this is arabic 420 ..... "
          + "  \u00a0\u0634\"=\u0027@#:\u060c\u0633\u0633\u0632\u0631\u0630\u062f\u062e"
          + "\u062e\u062d\u003f<_%,\u00a6\u062d\u062c\u062c\u062b\u062b\u062a\u062a\u0629"
          + "/-\u00ac;(*$!\u0628\u0628\u0627\u0627\u0626  \u0624"
          + "\u0623&|+)>.\u00a2\u0623\u0622\u0622\u0621\u200b\u0640  "
          + "abcdefghi\u0638\u0637\u0636\u0636\u0635\u0635\u0634"
          + "jklmnopqr\u063a\u063a\u063a\u0639\u0639\u0639\u0639"
          + "\u00f7stuvwxyz\u061b\u0647\u0646\u0646\u0645\u0645\u0644"
          + "\u0644\u0627\ufefb  \ufef8\ufef7\ufef6\ufef5\u0644\u0643\u0643\u0642\u0642\u0641\u0641"
          + "\u063aABCDEFGHI\u00ad\u0648 \u0647 \u0647"
          + "\u061fJKLMNOPQR0\u064a\u064a\u064a\u0649\u0649"
          + "\u00d7 STUVWXYZ12 345" + "0123456789"),
      new CCSIDTestData(423,
          "hello this is greek  423 \u00e9\u00ea\u00eb\u00e8\u0396\u00ee\u00ef\u0397 "
              + " \u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399[.<(+!"
              + "&\u039a\u039b\u039c\u039d\u039e\u039f\u03a0\u03a1\u03a3]$*);^"
              + "-/\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9  |,%_>\u003f"
              + " \u0386\u0388\u0389\u00a0\u038a\u038c\u038e\u038f`:\u00a3\u00a7\u0027=\""
              + "\u00c4abcdefghi\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6"
              + "\u00d6jklmnopqr\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc"
              + "\u00dc\u00a8stuvwxyz\u03bd\u03be\u03bf\u03c0\u03c1\u03c3"
              + " \u03ac\u03ad\u03ae\u03ca\u03af\u03cc\u03cd\u03cb\u03ce\u03c2\u03c4\u03c5\u03c6\u03c7\u03c8"
              + "\u00b8ABCDEFGHI\u00ad\u03c9\u00e2\u00e0\u00e4\u00ea"
              + "\u00b4JKLMNOPQR\u00b1\u00e9\u00e8\u00eb\u00ee\u00ef"
              + "\u00b0 STUVWXYZ\u00bd\u00f6\u00f4\u00fb\u00f9\u00fc"
              + "0123456789\u00ff\u00e7\u00c7  "),
      new CCSIDTestData(424,
          "\u05e9\u05e8\u05e7\u05e6\u05e5\u05e4\u05e3\u05e2\u00ac"
              + "\u05e1\u05e0\u05df\u05de\u05dd\u05dc\u05db\u05da\u05d9"
              + "\u00a2\u05d8\u05d7\u05d6\u05d5\u05d4\u05d3\u05d2\u05d1\u05d0 "
              + "\u05D7 \u05ea " + "'hello this is hebrew 420 "
              + "english letters first " + "abcdefghijklmnopqrstuvwxyz "
              + "ABCDEFGHIJKLMNOPQRSTUVWXYZ " + "0123456789"
              + "Symbols '~^[]{}\\=@#:`<_%,;(*$!   &| /-+)>. '" + "Rest '"
      /*
       * Old not working stuff "\u200f\u00ab\u00bb\u00b1"+
       * "\u00b0\u20ac\u00b8\u20aa\u00a4"+ "\u00b5\u00ae"+
       * "\u00a3\u00a5\u2022\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u203e\u00a8\u00b4\u00d7"+
       * "\u00ad     "+ "\u00b9\u202d\u202e\u202c  "+ "\u00f7\u00b2     "+
       * "\u00b3\u202a\u202b\u200e \""+ "\u0027\u2017   \u00a0   \u003f"+
       * "\u00a6'"
       */ ),

      new CCSIDTestData(425,
          " \u00a0\u00e2\u060c\u00e0\u061b\u0640\u061f\u00e7\u0621\u0622.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u0623\u00ee\u00ef\u0624\u0625!$*);^"
              + "-/\u00c2\u0626\u00c0\u0627\u0628\u0629\u00c7\u062a\u062b,%_>\u003f"
              + "\u062c\u00c9\u00ca\u00cb\u00c8\u062d\u00ce\u00cf\u062e`:#@\u0027=\""
              + "\u062fabcdefghi\u00ab\u00bb\u0630\u0631\u0632\u0633"
              + "\u0634jklmnopqr\u0635\u0636\u00e6\u0637\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u0638\u0639\u063a[\u0641\u0642"
              + "\u0643\u0644\u0645\u0646\u00a9\u00a7\u0647\u0152\u0153\u0178\u0648\u0649\u064a]\u064b\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u064c\u064d\u064e\u064f"
              + "}JKLMNOPQR\u0650\u00fb\u00fc\u00f9\u0651\u00ff"
              + "\\\u00f7STUVWXYZ\u0652\u00d4\u200c\u200d\u200e\u200f"
              + "0123456789 \u00db\u00dc\u00d9\u00a4"),

      new CCSIDTestData(500,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1[.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df]$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(833, "hello this is korea 833"
          + "  \uffa0\uffa1\uffa2\uffa3\uffa4\uffa5\uffa6\uffa7\u00a2.<(+|"
          + "& \uffa8\uffa9\uffaa\uffab\uffac\uffad\uffae\uffaf!$*);\u00ac"
          + "-/\uffb0\uffb1\uffb2\uffb3\uffb4\uffb5\uffb6\uffb7\u00a6,%_>\u003f"
          + "[ \uffb8\uffb9\uffba\uffbb\uffbc\uffbd\uffbe`:#@\u0027=\""
          + "]abcdefghi\uffc2\uffc3\uffc4\uffc5\uffc6\uffc7"
          + " jklmnopqr\uffca\uffcb\uffcc\uffcd\uffce\uffcf"
          + "\u203e~stuvwxyz\uffd2\uffd3\uffd4\uffd5\uffd6\uffd7"
          + "^ \\       \uffda\uffdb\uffdc   " + "{ABCDEFGHI      "
          + "}JKLMNOPQR      " + "\u20a9 STUVWXYZ      " + "0123456789     "),
      new CCSIDTestData(836,
          "hello this is chinese 836" + "          \u00a3.<(+|"
              + "&         !\u00a5*);\u00ac" + "-/        \u00a6,%_>\u003f"
              + "         `:#@\u0027=\"" + " abcdefghi      "
              + " jklmnopqr      " + "~\u203estuvwxyz      "
              + "^ \\       []    " + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "$ STUVWXYZ      " + "0123456789     "),
      new CCSIDTestData(838,
          " \u00a0\u0e01\u0e02\u0e03\u0e04\u0e05\u0e06\u0e07[\u00a2.<(+|"
              + "&\u0e48\u0e08\u0e09\u0e0a\u0e0b\u0e0c\u0e0d\u0e0e]!$*);\u00ac"
              + "-/\u0e0f\u0e10\u0e11\u0e12\u0e13\u0e14\u0e15^\u00a6,%_>\u003f"
              + "\u0e3f\u0e4e\u0e16\u0e17\u0e18\u0e19\u0e1a\u0e1b\u0e1c`:#@\u0027=\""
              + "\u0e4fabcdefghi\u0e1d\u0e1e\u0e1f\u0e20\u0e21\u0e22"
              + "\u0e5ajklmnopqr\u0e23\u0e24\u0e25\u0e26\u0e27\u0e28"
              + "\u0e5b~stuvwxyz\u0e29\u0e2a\u0e2b\u0e2c\u0e2d\u0e2e"
              + "\u0e50\u0e51\u0e52\u0e53\u0e54\u0e55\u0e56\u0e57\u0e58\u0e59\u0e2f\u0e30\u0e31\u0e32\u0e33\u0e34"
              + "{ABCDEFGHI\u0e49\u0e35\u0e36\u0e37\u0e38\u0e39"
              + "}JKLMNOPQR\u0e3a\u0e40\u0e41\u0e42\u0e43\u0e44"
              + "\\\u0e4aSTUVWXYZ\u0e45\u0e46\u0e47\u0e48\u0e49\u0e4a"
              + "0123456789\u0e4b\u0e4c\u0e4d\u0e4b\u0e4c"),
      new CCSIDTestData(870,
          "hello this is multilingual 870 \u00e9\u016f\u00eb\u013e\u00ed\u00ee\u013a\u02dd\u00df"
              + " \u00a0\u00e2\u00e4\u0163\u00e1\u0103\u010d\u00e7\u0107[.<(+!"
              + "&\u00e9\u0119\u00eb\u016f\u00ed\u00ee\u013e\u013a\u00df]$*);^"
              + "-/\u00c2\u00c4\u02dd\u00c1\u0102\u010c\u00c7\u0106|,%_>\u003f"
              + "\u02c7\u00c9\u0118\u00cb\u016e\u00cd\u00ce\u013d\u0139`:#@\u0027=\""
              + "\u02d8abcdefghi\u015b\u0148\u0111\u00fd\u0159\u015f"
              + "\u00b0jklmnopqr\u0142\u0144\u0161\u00b8\u02db\u00a4"
              + "\u0105~stuvwxyz\u015a\u0147\u0110\u00dd\u0158\u015e"
              + "\u02d9\u0104\u017c\u0162\u017b\u00a7\u017e\u017a\u017d\u0179\u0141\u0143\u0160\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u0155\u00f3\u0151"
              + "}JKLMNOPQR\u011a\u0171\u00fc\u0165\u00fa\u011b"
              + "\\\u00f7STUVWXYZ\u010f\u00d4\u00d6\u0154\u00d3\u0150"
              + "0123456789\u010e\u0170\u00dc\u0164\u00da"),
      new CCSIDTestData(871,
          "hello this is iceland 871 \u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df "
              + " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00de.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u00c6$*);\u00d6"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00f0:#\u00d0\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb`\u00fd{\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba}\u00b8]\u00a4"
              + "\u00b5\u00f6stuvwxyz\u00a1\u00bf@\u00dd[\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\\\u00d7"
              + "\u00feABCDEFGHI\u00ad\u00f4~\u00f2\u00f3\u00f5"
              + "\u00e6JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\u00b4\u00f7STUVWXYZ\u00b2\u00d4^\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),
      new CCSIDTestData(875,
          "hello this is greece 875 \u039a\u039b\u039c\u039d\u039e\u039f\u03a0\u03a1\u03a3"
              + " \u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399[.<(+!"
              + "&\u039a\u039b\u039c\u039d\u039e\u039f\u03a0\u03a1\u03a3]$*);^"
              + "-/\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9\u03aa\u03ab|,%_>\u003f"
              + "\u00a8\u0386\u0388\u0389\u00a0\u038a\u038c\u038e\u038f`:#@\u0027=\""
              + "\u0385abcdefghi\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6"
              + "\u00b0jklmnopqr\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc"
              + "\u00b4~stuvwxyz\u03bd\u03be\u03bf\u03c0\u03c1\u03c3"
              + "\u00a3\u03ac\u03ad\u03ae\u03ca\u03af\u03cc\u03cd\u03cb\u03ce\u03c2\u03c4\u03c5\u03c6\u03c7\u03c8"
              + "{ABCDEFGHI\u00ad\u03c9\u0390\u03b0\u2018\u2015"
              + "}JKLMNOPQR\u00b1\u00bd \u0387\u2019\u00a6"
              + "\\ STUVWXYZ\u00b2\u00a7  \u00ab\u00ac"
              + "0123456789\u00b3\u00a9  \u00bb"),
      new CCSIDTestData(880,
          " \u00a0\u0452\u0453\u0451\u0454\u0455\u0456\u0457\u0458[.<(+!"
              + "&\u0459\u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402]$*);^"
              + "-/\u0403\u0401\u0404\u0405\u0406\u0407\u0408\u0409|,%_>\u003f"
              + "\u040a\u040b\u040c\u00ad\u040e\u040f\u044e\u0430\u0431`:#@\u0027=\""
              + "\u0446abcdefghi\u0434\u0435\u0444\u0433\u0445\u0438"
              + "\u0439jklmnopqr\u043a\u043b\u043c\u043d\u043e\u043f"
              + "\u044f~stuvwxyz\u0440\u0441\u0442\u0443\u0436\u0432"
              + "\u044c\u044b\u0437\u0448\u044d\u0449\u0447\u044a\u042e\u0410\u0411\u0426\u0414\u0415\u0424\u0413"
              + "{ABCDEFGHI\u0425\u0418\u0419\u041a\u041b\u041c"
              + "}JKLMNOPQR\u041d\u041e\u041f\u042f\u0420\u0421"
              + "\\\u00a4STUVWXYZ\u0422\u0423\u0416\u0412\u042c\u042b"
              + "0123456789\u0417\u0428\u042d\u0429\u0427"),
      new CCSIDTestData(905,
          " \u00a0\u00e2\u00e4\u00e0\u00e1 \u010b{\u00f1\u00c7.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u011e\u0130*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1 \u010a[\u00d1\u015f,%_>\u003f"
              + " \u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u0131:\u00d6\u015e\u0027=\u00dc"
              + "\u02d8abcdefghi\u0127\u0109\u015d\u016d |"
              + "\u00b0jklmnopqr\u0125\u011d\u0135\u00b8 \u00a4"
              + "\u00b5\u00f6stuvwxyz\u0126\u0108\u015c\u016c @"
              + "\u02d9\u00a3\u017c}\u017b\u00a7]\u00b7\u00bd$\u0124\u011c\u0134\u00a8\u00b4\u00d7"
              + "\u00e7ABCDEFGHI\u00ad\u00f4~\u00f2\u00f3\u0121"
              + "\u011fJKLMNOPQR`\u00fb\\\u00f9\u00fa "
              + "\u00fc\u00f7STUVWXYZ\u00b2\u00d4#\u00d2\u00d3\u0120"
              + "0123456789\u00b3\u00db\"\u00d9\u00da"),

      new CCSIDTestData(918,
          " \u00a0\u060c\u061b\u061f\ufe81\ufe8d\ufe8e\uf8fb\ufe8f[.<(+!"
              + "&\ufe91\ufb56\ufb58\ufe93\ufe95\ufe97\ufb66\ufb68\ufe99]$*);^"
              + "-/\ufe9b\ufe9d\ufe9f\ufb7a\ufb7c\ufea1\ufea3\ufea5`,%_>\u003f"
              + "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9:#@\u0027=\""
              + "\ufea7abcdefghi\ufea9\ufb88\ufeab\ufead\ufb8c\ufeaf"
              + "\ufb8ajklmnopqr\ufeb1\ufeb3\ufeb5\ufeb7\ufeb9\ufebb"
              + "\ufebd~stuvwxyz\ufebf\ufec3\ufec7\ufec9\ufeca\ufecb"
              + "\ufecc\ufecd\ufece\ufecf\ufed0\ufed1\ufed3\ufed5\ufed7\ufb8e\ufedb|\ufb92\ufb94\ufedd\ufedf"
              + "{ABCDEFGHI\u00ad\ufee0\ufee1\ufee3\ufb9e\ufee5"
              + "}JKLMNOPQR\ufee7\ufe85\ufeed\ufba6\ufba8\ufba9"
              + "\\\ufbaaSTUVWXYZ\ufe80\ufe89\ufe8a\ufe8b\ufbfc\ufbfd"
              + "0123456789\ufbfe\ufbb0\ufbae\ufe7c\ufe7d"),
      new CCSIDTestData(924,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00dd.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u0160,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u017e\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0[\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u0152\u0153\u0178\u00ac\u0161\u00af]\u017d\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      /* On 4/9/2008 removed EURO \u20ac from this. See CPS 78JKRU */
      new CCSIDTestData(930,
          " \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\uff69\u00a3.<(+|"
              + "&\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f \uff70 !\u00a5*);\u00ac"
              + "-/abcdefgh ,%_>\u003f" + "[ijklmnop`:#@\u0027=\""
              + "]\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79\uff7aq\uff7b\uff7c\uff7d\uff7e"
              + "\uff7f\uff80\uff81\uff82\uff83\uff84\uff85\uff86\uff87\uff88\uff89r \uff8a\uff8b\uff8c"
              + "~\u203e\uff8d\uff8e\uff8f\uff90\uff91\uff92\uff93\uff94\uff95s\uff96\uff97\uff98\uff99"
              + "^\u00a2\\tuvwxyz\uff9a\uff9b\uff9c\uff9d\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "$STUVWXYZ      "
              + "0123456789     "),
      new CCSIDTestData(933,
          "  \uffa0\uffa1\uffa2\uffa3\uffa4\uffa5\uffa6\uffa7\u00a2.<(+|"
              + "& \uffa8\uffa9\uffaa\uffab\uffac\uffad\uffae\uffaf!$*);\u00ac"
              + "-/\uffb0\uffb1\uffb2\uffb3\uffb4\uffb5\uffb6\uffb7\u00a6,%_>\u003f"
              + "[ \uffb8\uffb9\uffba\uffbb\uffbc\uffbd\uffbe`:#@\u0027=\""
              + "]abcdefghi\uffc2\uffc3\uffc4\uffc5\uffc6\uffc7"
              + " jklmnopqr\uffca\uffcb\uffcc\uffcd\uffce\uffcf"
              + "\u203e~stuvwxyz\uffd2\uffd3\uffd4\uffd5\uffd6\uffd7"
              + "^ \\       \uffda\uffdb\uffdc   " + "{ABCDEFGHI      "
              + "}JKLMNOPQR      " + "\u20a9 STUVWXYZ      "
              + "0123456789     "),
      new CCSIDTestData(935, "          \u00a3.<(+|"
          + "&         !\u00a5*);\u00ac" + "-/        \u00a6,%_>\u003f"
          + "         `:#@\u0027=\"" + " abcdefghi      " + " jklmnopqr      "
          + "~\u203estuvwxyz      " + "^ \\       []    " + "{ABCDEFGHI      "
          + "}JKLMNOPQR      " + "$ STUVWXYZ      " + "0123456789     "),

      new CCSIDTestData(937,
          "  \u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);\u00ac"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "^\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u00af\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData
      // CCSID 937 not working on PASE for accented characters (i.e. E2,E4)
      (610937, "\u00a2.<(+|" + "&!$*);\u00ac" + "-/\u00a6,%_>\u003f"
          + "`:#@\u0027=\"" + "abcdefghi\u00b1" + "\u00b0jklmnopqr"
          + "~stuvwxyz" + "^\u00b7\u00a7\u00b6[]\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI" + "}JKLMNOPQR" + "\\\u00f7STUVWXYZ" + "0123456789 "
          + " Now for now 835 stuff "
          + "\u5e02\u5e03\u5f17\u672A\u672B\u59cb\u500c\u8679\u9853\u8f44\u52f1"),

      /* On 4/9/2008 removed EURO \u20ac from this. See CPS 78JKRU */
      new CCSIDTestData(939,
          "  \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\u00a2.<(+|"
              + "&\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71!$*);\u00ac"
              + "-/\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79 ,%_>\u003f"
              + "\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82`:#@\u0027=\""
              + " abcdefghi\uff83\uff84\uff85\uff86\uff87\uff88"
              + " jklmnopqr\uff89\uff8a\uff8b\uff8c\uff8d\uff8e"
              + "\u203e~stuvwxyz\uff8f\uff90\uff91[\uff92\uff93"
              + "^\u00a3\u00a5\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d]\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "\\STUVWXYZ      "
              + "0123456789     "),
      new CCSIDTestData(1025,
          "hello this is cyrillic 1025 \u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402\u0403"
              + " \u00a0\u0452\u0453\u0451\u0454\u0455\u0456\u0457\u0458[.<(+!"
              + "&\u0459\u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402]$*);^"
              + "-/\u0403\u0401\u0404\u0405\u0406\u0407\u0408\u0409|,%_>\u003f"
              + "\u040a\u040b\u040c\u00ad\u040e\u040f\u044e\u0430\u0431`:#@\u0027=\""
              + "\u0446abcdefghi\u0434\u0435\u0444\u0433\u0445\u0438"
              + "\u0439jklmnopqr\u043a\u043b\u043c\u043d\u043e\u043f"
              + "\u044f~stuvwxyz\u0440\u0441\u0442\u0443\u0436\u0432"
              + "\u044c\u044b\u0437\u0448\u044d\u0449\u0447\u044a\u042e\u0410\u0411\u0426\u0414\u0415\u0424\u0413"
              + "{ABCDEFGHI\u0425\u0418\u0419\u041a\u041b\u041c"
              + "}JKLMNOPQR\u041d\u041e\u041f\u042f\u0420\u0421"
              + "\\\u00a7STUVWXYZ\u0422\u0423\u0416\u0412\u042c\u042b"
              + "0123456789\u0417\u0428\u042d\u0429\u0427"),
      new CCSIDTestData(1026,
          "hello this is latin turkey 1026 \u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df"
              + " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5{\u00f1\u00c7.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u011e\u0130*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5[\u00d1\u015f,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u0131:\u00d6\u015e\u0027=\u00dc"
              + "\u00d8abcdefghi\u00ab\u00bb}`\u00a6\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u00a4"
              + "\u00b5\u00f6stuvwxyz\u00a1\u00bf]$@\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e7ABCDEFGHI\u00ad\u00f4~\u00f2\u00f3\u00f5"
              + "\u011fJKLMNOPQR\u00b9\u00fb\\\u00f9\u00fa\u00ff"
              + "\u00fc\u00f7STUVWXYZ\u00b2\u00d4#\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\"\u00d9\u00da"),
      new CCSIDTestData(1027, "hello this is latin japan 1027"
          + "  \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\u00a2.<(+|"
          + "&\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71!$*);\u00ac"
          + "-/\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79 ,%_>\u003f"
          + "\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82`:#@\u0027=\""
          + " abcdefghi\uff83\uff84\uff85\uff86\uff87\uff88"
          + " jklmnopqr\uff89\uff8a\uff8b\uff8c\uff8d\uff8e"
          + "\u203e~stuvwxyz\uff8f\uff90\uff91[\uff92\uff93"
          + "^\u00a3\u00a5\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d]\uff9e\uff9f"
          + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "\\ STUVWXYZ      "
          + "0123456789     "),

      new CCSIDTestData(1097,
          " \u00a0\u060c\u064b\ufe81\ufe82\uf8fa\ufe8d\ufe8e\uf8fb\u00a4.<(+|"
              + "&\ufe80\ufe83\ufe84\uf8f9\ufe85\ufe8b\ufe8f\ufe91\ufb56!$*);\u00ac"
              + "-/\ufb58\ufe95\ufe97\ufe99\ufe9b\ufe9d\ufe9f\ufb7a\u061b,%_>\u003f"
              + "\ufb7c\ufea1\ufea3\ufea5\ufea7\ufea9\ufeab\ufead\ufeaf`:#@\u0027=\""
              + "\ufb8aabcdefghi\u00ab\u00bb\ufeb1\ufeb3\ufeb5\ufeb7"
              + "\ufeb9jklmnopqr\ufebb\ufebd\ufebf\ufec1\ufec3\ufec5"
              + "\ufec7~stuvwxyz\ufec9\ufeca\ufecb\ufecc\ufecd\ufece"
              + "\ufecf\ufed0\ufed1\ufed3\ufed5\ufed7\ufb8e\ufedb\ufb92\ufb94[]\ufedd\ufedf\ufee1\u00d7"
              + "{ABCDEFGHI\u00ad\ufee3\ufee5\ufee7\ufeed\ufee9"
              + "}JKLMNOPQR\ufeeb\ufeec\ufba4\ufbfc\ufbfd\ufbfe"
              + "\\\u061fSTUVWXYZ\u0640\u06f0\u06f1\u06f2\u06f3\u06f4"
              + "0123456789\u06f5\u06f6\u06f7\u06f8\u06f9"),

      new CCSIDTestData(1112,
          " \u00a0\u0161\u00e4\u0105\u012f\u016b\u00e5\u0113\u017e\u00a2.<(+|"
              + "&\u00e9\u0119\u0117\u010d\u0173\u201e\u201c\u0123\u00df!$*);\u00ac"
              + "-/\u0160\u00c4\u0104\u012e\u016a\u00c5\u0112\u017d\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u0118\u0116\u010c\u0172\u012a\u013b\u0122`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0101\u017c\u0144\u00b1"
              + "\u00b0jklmnopqr\u0156\u0157\u00e6\u0137\u00c6\u00a4"
              + "\u00b5~stuvwxyz\u201d\u017a\u0100\u017b\u0143\u00ae"
              + "^\u00a3\u012b\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u0179\u0136\u013c\u00d7"
              + "{ABCDEFGHI\u00ad\u014d\u00f6\u0146\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u0107\u00fc\u0142\u015b\u2019"
              + "\\\u00f7STUVWXYZ\u00b2\u014c\u00d6\u0145\u00d3\u00d5"
              + "0123456789\u00b3\u0106\u00dc\u0141\u015a"),

      new CCSIDTestData(1122,
          " \u00a0\u00e2{\u00e0\u00e1\u00e3}\u00e7\u00f1\u00a7.<(+!"
              + "&`\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u00a4\u00c5*);^"
              + "-/\u00c2#\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f6,%_>\u003f"
              + "\u00f8\\\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00e9:\u00c4\u00d6\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0161\u00fd\u017e\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6]"
              + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u0160\u00dd\u017d\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9[\u00b6\u00bc\u00bd\u00be\u00ac|\u00bc\u00a8\u00b4\u00d7"
              + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
              + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
              + "\u00c9\u00f7STUVWXYZ\u00b2\u00d4@\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1123,
          " \u00a0\u0452\u0491\u0451\u0454\u0455\u0456\u0457\u0458[.<(+!"
              + "&\u0459\u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402]$*);^"
              + "-/\u0490\u0401\u0404\u0405\u0406\u0407\u0408\u0409|,%_>\u003f"
              + "\u040a\u040b\u040c\u00ad\u040e\u040f\u044e\u0430\u0431`:#@\u0027=\""
              + "\u0446abcdefghi\u0434\u0435\u0444\u0433\u0445\u0438"
              + "\u0439jklmnopqr\u043a\u043b\u043c\u043d\u043e\u043f"
              + "\u044f~stuvwxyz\u0440\u0441\u0442\u0443\u0436\u0432"
              + "\u044c\u044b\u0437\u0448\u044d\u0449\u0447\u044a\u042e\u0410\u0411\u0426\u0414\u0415\u0424\u0413"
              + "{ABCDEFGHI\u0425\u0418\u0419\u041a\u041b\u041c"
              + "}JKLMNOPQR\u041d\u041e\u041f\u042f\u0420\u0421"
              + "\\\u00a7STUVWXYZ\u0422\u0423\u0416\u0412\u042c\u042b"
              + "0123456789\u0417\u0428\u042d\u0429\u0427"),

      new CCSIDTestData(1130,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u0103\u00e5\u00e7\u00f1[.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u0303\u00df]$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u0102\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u20ab`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0111\u0309\u0300\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u0152\u00c6\u00a4"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u0110\u0323\u0301\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u0153\u0178\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u01b0\u00f3\u01a1"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u01af\u00d3\u01a0"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1132,
          " \u00a0\u0e81\u0e82\u0e84\u0e87\u0e88\u0eaa\u0e8a[\u00a2.<(+|"
              + "& \u0e8d\u0e94\u0e95\u0e96\u0e97\u0e99\u0e9a]!$*);\u00ac"
              + "-/\u0e9b\u0e9c\u0e9d\u0e9e\u0e9f\u0ea1\u0ea2^\u00a6,%_>\u003f"
              + "k \u0ea3\u0ea5\u0ea7\u0eab\u0ead\u0eae `:#@\u0027=\""
              + " abcdefghi  \u0eaf\u0eb0\u0eb2\u0eb3"
              + " jklmnopqr\u0eb4\u0eb5\u0eb6\u0eb7\u0eb8\u0eb9"
              + " ~stuvwxyz\u0ebc\u0eb1\u0ebb\u0ebd  "
              + "\u0ed0\u0ed1\u0ed2\u0ed3\u0ed4\u0ed5\u0ed6\u0ed7\u0ed8\u0ed9 \u0ec0\u0ec1\u0ec2\u0ec3\u0ec4"
              + "{ABCDEFGHI \u0ec8\u0ec9\u0eca\u0ecb\u0ecc"
              + "}JKLMNOPQR\u0ecd\u0ec6 \u0edc\u0edd " + "\\ STUVWXYZ      "
              + "0123456789     "),

      new CCSIDTestData(1137,
          " \u00a0\u0901\u0902\u0903\u0905\u0906\u0907\u0908\u0909\u090a.<(+|"
              + "&\u090b\u090c\u090d\u090e\u090f\u0910\u0911\u0912\u0913!$*);^"
              + "-/\u0914\u0915\u0916\u0917\u0918\u0919\u091a\u091b\u091c,%_>\u003f"
              + "\u091d\u091e\u091f\u0920\u0921\u0922\u0923\u0924\u0925`:#@\u0027=\""
              + "\u0926abcdefghi\u0927\u0928\u092a\u092b\u092c\u092d"
              + "\u092ejklmnopqr\u092f\u0930\u0932\u0933\u0935\u0936"
              + "\u200c~stuvwxyz\u0937\u0938\u0939[\u093c\u093d"
              + "\u093e\u093f\u0940\u0941\u0942\u0943\u0944\u0945\u0946\u0947\u0948\u0949\u094a]\u094b\u094c"
              + "{ABCDEFGHI\u094d\u0950\u0951\u0952  "
              + "}JKLMNOPQR\u0960\u0961\u0962\u0963\u0964\u0965"
              + "\\\u200dSTUVWXYZ\u0966\u0967\u0968\u0969\u096a\u096b"
              + "0123456789\u096c\u096d\u096e\u096f\u0970"),

      new CCSIDTestData(1140,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00a2.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!$*);\u00ac"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "^\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u00af\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1141,
          " \u00a0\u00e2{\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00c4.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec~\u00dc$*);^"
              + "-/\u00c2[\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#\u00a7\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5\u00dfstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
              + "\u00fcJKLMNOPQR\u00b9\u00fb}\u00f9\u00fa\u00ff"
              + "\u00d6\u00f7STUVWXYZ\u00b2\u00d4\\\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db]\u00d9\u00da"),

      new CCSIDTestData(1142,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3}\u00e7\u00f1#.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u20ac\u00c5*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f8,%_>\u003f"
              + "\u00a6\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:\u00c6\u00d8\u0027=\""
              + "@abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba{\u00b8[]"
              + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e6ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1143,
          " \u00a0\u00e2{\u00e0\u00e1\u00e3}\u00e7\u00f1\u00a7.<(+!"
              + "&`\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u20ac\u00c5*);^"
              + "-/\u00c2#\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f6,%_>\u003f"
              + "\u00f8\\\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00e9:\u00c4\u00d6\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6]"
              + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9[\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
              + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
              + "\u00c9\u00f7STUVWXYZ\u00b2\u00d4@\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1144,

          " \u00a0\u00e2\u00e4{\u00e1\u00e3\u00e5\\\u00f1\u00b0.<(+!"
              + "&]\u00ea\u00eb}\u00ed\u00ee\u00ef~\u00df\u00e9$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f2,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00f9:\u00a3\u00a7\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "[jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5\u00ecstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2#\u00a5\u00b7\u00a9@\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e0ABCDEFGHI\u00ad\u00f4\u00f6\u00a6\u00f3\u00f5"
              + "\u00e8JKLMNOPQR\u00b9\u00fb\u00fc`\u00fa\u00ff"
              + "\u00e7\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1145,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00a6[.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df]$*);\u00ac"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7#\u00f1,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:\u00d1@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5\u00a8stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be^!\u00af~\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1146,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1$.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df!\u00a3*);\u00ac"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5\u00afstuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2[\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be^]~\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1147,

          " \u00a0\u00e2\u00e4@\u00e1\u00e3\u00e5\\\u00f1\u00b0.<(+!"
              + "&{\u00ea\u00eb}\u00ed\u00ee\u00ef\u00ec\u00df\u00a7$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00f9,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00b5:\u00a3\u00e0\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "[jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "`\u00a8stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2#\u00a5\u00b7\u00a9]\u00b6\u00bc\u00bd\u00be\u00ac|\u00af~\u00b4\u00d7"
              + "\u00e9ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "\u00e8JKLMNOPQR\u00b9\u00fb\u00fc\u00a6\u00fa\u00ff"
              + "\u00e7\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1148,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1[.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df]$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u00f0\u00fd\u00fe\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u00d0\u00dd\u00de\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u00f2\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1149,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5\u00e7\u00f1\u00de.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u00c6$*);\u00d6"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00f0:#\u00d0\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb`\u00fd{\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba}\u00b8]\u20ac"
              + "\u00b5\u00f6stuvwxyz\u00a1\u00bf@\u00dd[\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\\\u00d7"
              + "\u00feABCDEFGHI\u00ad\u00f4~\u00f2\u00f3\u00f5"
              + "\u00e6JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\u00b4\u00f7STUVWXYZ\u00b2\u00d4^\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"

      ),

      new CCSIDTestData(1153,

          " \u00a0\u00e2\u00e4\u0163\u00e1\u0103\u010d\u00e7\u0107[.<(+!"
              + "&\u00e9\u0119\u00eb\u016f\u00ed\u00ee\u013e\u013a\u00df]$*);^"
              + "-/\u00c2\u00c4\u02dd\u00c1\u0102\u010c\u00c7\u0106|,%_>\u003f"
              + "\u02c7\u00c9\u0118\u00cb\u016e\u00cd\u00ce\u013d\u0139`:#@\u0027=\""
              + "\u02d8abcdefghi\u015b\u0148\u0111\u00fd\u0159\u015f"
              + "\u00b0jklmnopqr\u0142\u0144\u0161\u00b8\u02db\u20ac"
              + "\u0105~stuvwxyz\u015a\u0147\u0110\u00dd\u0158\u015e"
              + "\u02d9\u0104\u017c\u0162\u017b\u00a7\u017e\u017a\u017d\u0179\u0141\u0143\u0160\u00a8\u00b4\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u0155\u00f3\u0151"
              + "}JKLMNOPQR\u011a\u0171\u00fc\u0165\u00fa\u011b"
              + "\\\u00f7STUVWXYZ\u010f\u00d4\u00d6\u0154\u00d3\u0150"
              + "0123456789\u010e\u0170\u00dc\u0164\u00da"),

      new CCSIDTestData(1154,
          " \u00a0\u0452\u0453\u0451\u0454\u0455\u0456\u0457\u0458[.<(+!"
              + "&\u0459\u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402]$*);^"
              + "-/\u0403\u0401\u0404\u0405\u0406\u0407\u0408\u0409|,%_>\u003f"
              + "\u040a\u040b\u040c\u00ad\u040e\u040f\u044e\u0430\u0431`:#@\u0027=\""
              + "\u0446abcdefghi\u0434\u0435\u0444\u0433\u0445\u0438"
              + "\u0439jklmnopqr\u043a\u043b\u043c\u043d\u043e\u043f"
              + "\u044f~stuvwxyz\u0440\u0441\u0442\u0443\u0436\u0432"
              + "\u044c\u044b\u0437\u0448\u044d\u0449\u0447\u044a\u042e\u0410\u0411\u0426\u0414\u0415\u0424\u0413"
              + "{ABCDEFGHI\u0425\u0418\u0419\u041a\u041b\u041c"
              + "}JKLMNOPQR\u041d\u041e\u041f\u042f\u0420\u0421"
              + "\\\u20acSTUVWXYZ\u0422\u0423\u0416\u0412\u042c\u042b"
              + "0123456789\u0417\u0428\u042d\u0429\u0427"),

      new CCSIDTestData(1155,
          " \u00a0\u00e2\u00e4\u00e0\u00e1\u00e3\u00e5{\u00f1\u00c7.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u011e\u0130*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u00c3\u00c5[\u00d1\u015f,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u0131:\u00d6\u015e\u0027=\u00dc"
              + "\u00d8abcdefghi\u00ab\u00bb}`\u00a6\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6\u20ac"
              + "\u00b5\u00f6stuvwxyz\u00a1\u00bf]$@\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e7ABCDEFGHI\u00ad\u00f4~\u00f2\u00f3\u00f5"
              + "\u011fJKLMNOPQR\u00b9\u00fb\\\u00f9\u00fa\u00ff"
              + "\u00fc\u00f7STUVWXYZ\u00b2\u00d4#\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\"\u00d9\u00da"),

      new CCSIDTestData(1156,
          " \u00a0\u0161\u00e4\u0105\u012f\u016b\u00e5\u0113\u017e\u00a2.<(+|"
              + "&\u00e9\u0119\u0117\u010d\u0173\u201e\u201c\u0123\u00df!$*);\u00ac"
              + "-/\u0160\u00c4\u0104\u012e\u016a\u00c5\u0112\u017d\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u0118\u0116\u010c\u0172\u012a\u013b\u0122`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0101\u017c\u0144\u00b1"
              + "\u00b0jklmnopqr\u0156\u0157\u00e6\u0137\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u201d\u017a\u0100\u017b\u0143\u00ae"
              + "^\u00a3\u012b\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u0179\u0136\u013c\u00d7"
              + "{ABCDEFGHI\u00ad\u014d\u00f6\u0146\u00f3\u00f5"
              + "}JKLMNOPQR\u00b9\u0107\u00fc\u0142\u015b\u2019"
              + "\\\u00f7STUVWXYZ\u00b2\u014c\u00d6\u0145\u00d3\u00d5"
              + "0123456789\u00b3\u0106\u00dc\u0141\u015a"),

      new CCSIDTestData(1157,

          " \u00a0\u00e2{\u00e0\u00e1\u00e3}\u00e7\u00f1\u00a7.<(+!"
              + "&`\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u00ec\u00df\u20ac\u00c5*);^"
              + "-/\u00c2#\u00c0\u00c1\u00c3$\u00c7\u00d1\u00f6,%_>\u003f"
              + "\u00f8\\\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u00cc\u00e9:\u00c4\u00d6\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0161\u00fd\u017e\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u00b8\u00c6]"
              + "\u00b5\u00fcstuvwxyz\u00a1\u00bf\u0160\u00dd\u017d\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9[\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u00a8\u00b4\u00d7"
              + "\u00e4ABCDEFGHI\u00ad\u00f4\u00a6\u00f2\u00f3\u00f5"
              + "\u00e5JKLMNOPQR\u00b9\u00fb~\u00f9\u00fa\u00ff"
              + "\u00c9\u00f7STUVWXYZ\u00b2\u00d4@\u00d2\u00d3\u00d5"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1158,
          " \u00a0\u0452\u0491\u0451\u0454\u0455\u0456\u0457\u0458[.<(+!"
              + "&\u0459\u045a\u045b\u045c\u045e\u045f\u042a\u2116\u0402]$*);^"
              + "-/\u0490\u0401\u0404\u0405\u0406\u0407\u0408\u0409|,%_>\u003f"
              + "\u040a\u040b\u040c\u00ad\u040e\u040f\u044e\u0430\u0431`:#@\u0027=\""
              + "\u0446abcdefghi\u0434\u0435\u0444\u0433\u0445\u0438"
              + "\u0439jklmnopqr\u043a\u043b\u043c\u043d\u043e\u043f"
              + "\u044f~stuvwxyz\u0440\u0441\u0442\u0443\u0436\u0432"
              + "\u044c\u044b\u0437\u0448\u044d\u0449\u0447\u044a\u042e\u0410\u0411\u0426\u0414\u0415\u0424\u0413"
              + "{ABCDEFGHI\u0425\u0418\u0419\u041a\u041b\u041c"
              + "}JKLMNOPQR\u041d\u041e\u041f\u042f\u0420\u0421"
              + "\\\u20acSTUVWXYZ\u0422\u0423\u0416\u0412\u042c\u042b"
              + "0123456789\u0417\u0428\u042d\u0429\u0427"),

      new CCSIDTestData(1160,
          " \u00a0\u0e01\u0e02\u0e03\u0e04\u0e05\u0e06\u0e07[\u00a2.<(+|"
              + "&\u0e48\u0e08\u0e09\u0e0a\u0e0b\u0e0c\u0e0d\u0e0e]!$*);\u00ac"
              + "-/\u0e0f\u0e10\u0e11\u0e12\u0e13\u0e14\u0e15^\u00a6,%_>\u003f"
              + "\u0e3f\u0e4e\u0e16\u0e17\u0e18\u0e19\u0e1a\u0e1b\u0e1c`:#@\u0027=\""
              + "\u0e4fabcdefghi\u0e1d\u0e1e\u0e1f\u0e20\u0e21\u0e22"
              + "\u0e5ajklmnopqr\u0e23\u0e24\u0e25\u0e26\u0e27\u0e28"
              + "\u0e5b~stuvwxyz\u0e29\u0e2a\u0e2b\u0e2c\u0e2d\u0e2e"
              + "\u0e50\u0e51\u0e52\u0e53\u0e54\u0e55\u0e56\u0e57\u0e58\u0e59\u0e2f\u0e30\u0e31\u0e32\u0e33\u0e34"
              + "{ABCDEFGHI\u0e49\u0e35\u0e36\u0e37\u0e38\u0e39"
              + "}JKLMNOPQR\u0e3a\u0e40\u0e41\u0e42\u0e43\u0e44"
              + "\\\u0e4aSTUVWXYZ\u0e45\u0e46\u0e47\u0e48\u0e49\u0e4a"
              + "0123456789\u0e4b\u0e4c\u0e4d\u0e4b\u20ac"),

      new CCSIDTestData(1164,

          " \u00a0\u00e2\u00e4\u00e0\u00e1\u0103\u00e5\u00e7\u00f1[.<(+!"
              + "&\u00e9\u00ea\u00eb\u00e8\u00ed\u00ee\u00ef\u0303\u00df]$*);^"
              + "-/\u00c2\u00c4\u00c0\u00c1\u0102\u00c5\u00c7\u00d1\u00a6,%_>\u003f"
              + "\u00f8\u00c9\u00ca\u00cb\u00c8\u00cd\u00ce\u00cf\u20ab`:#@\u0027=\""
              + "\u00d8abcdefghi\u00ab\u00bb\u0111\u0309\u0300\u00b1"
              + "\u00b0jklmnopqr\u00aa\u00ba\u00e6\u0152\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u00a1\u00bf\u0110\u0323\u0301\u00ae"
              + "\u00a2\u00a3\u00a5\u00b7\u00a9\u00a7\u00b6\u00bc\u00bd\u00be\u00ac|\u00af\u0153\u0178\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u00f6\u01b0\u00f3\u01a1"
              + "}JKLMNOPQR\u00b9\u00fb\u00fc\u00f9\u00fa\u00ff"
              + "\\\u00f7STUVWXYZ\u00b2\u00d4\u00d6\u01af\u00d3\u01a0"
              + "0123456789\u00b3\u00db\u00dc\u00d9\u00da"),

      new CCSIDTestData(1208, " !\"#$%&\u0027()*+,-./" + "0123456789:;<=>\u003f"
          + "@ABCDEFGHIJKLMNO" + "PQRSTUVWXYZ[\\]^_" + "`abcdefghijklmno"
          + "pqrstuvwxyz{|}~\u007f"
          + "\u00a0\u00a1\u00a2\u00a3\u00a4\u00a5\u00a6\u00a7\u00a8\u00a9\u00aa\u00ab\u00ac\u00ad\u00ae\u00af"
          + "\u00b0\u00b1\u00b2\u00b3\u00b4\u00b5\u00b6\u00b7\u00b8\u00b9\u00ba\u00bb\u00bc\u00bd\u00be\u00bf"
          + "\u00c0\u00c1\u00c2\u00c3\u00c4\u00c5\u00c6\u00c7\u00c8\u00c9\u00ca\u00cb\u00cc\u00cd\u00ce\u00cf"
          + "\u00d0\u00d1\u00d2\u00d3\u00d4\u00d5\u00d6\u00d7\u00d8\u00d9\u00da\u00db\u00dc\u00dd\u00de\u00df"
          + "\u00e0\u00e1\u00e2\u00e3\u00e4\u00e5\u00e6\u00e7\u00e8\u00e9\u00ea\u00eb\u00ec\u00ed\u00ee\u00ef"
          + "\u00f0\u00f1\u00f2\u00f3\u00f4\u00f5\u00f6\u00f7\u00f8\u00f9\u00fa\u00fb\u00fc\u00fd\u00fe"
          + " Here are some normal UCS-2 characters "
          + "\u0130\u01bb\u042F\u04dc\u0f56\u3020\u3042\u304e\u30ed\u3678\u500c\u52f1\u5378"
          + "\u8271\u8f44\u9853\ub5a0\uFF21\uFF22 "
          + " Here are some UCS-4 characters "
          + "\ud800\udf30\ud800\udf4a\ud835\udccf\ud835\udc03\ud869\uded6"),

      new CCSIDTestData(1364,
          "  \uffa0\uffa1\uffa2\uffa3\uffa4\uffa5\uffa6\uffa7\u00a2.<(+|"
              + "& \uffa8\uffa9\uffaa\uffab\uffac\uffad\uffae\uffaf!$*);\u00ac"
              + "-/\uffb0\uffb1\uffb2\uffb3\uffb4\uffb5\uffb6\uffb7\u00a6,%_>\u003f"
              + "[ \uffb8\uffb9\uffba\uffbb\uffbc\uffbd\uffbe`:#@\u0027=\""
              + "]abcdefghi\uffc2\uffc3\uffc4\uffc5\uffc6\uffc7"
              + " jklmnopqr\uffca\uffcb\uffcc\uffcd\uffce\uffcf"
              + "\u203e~stuvwxyz\uffd2\uffd3\uffd4\uffd5\uffd6\uffd7"
              + "^ \\       \uffda\uffdb\uffdc   " + "{ABCDEFGHI      "
              + "}JKLMNOPQR      " + "\u20a9 STUVWXYZ      "
              + "0123456789     "),

      new CCSIDTestData(1377,
          "          \u00a2.<(+|" + "&         !$*);\u00ac"
              + "-/        \u00a6,%_>?" + "         `:#@\u0027=\""
              + " abcdefghi      " + " jklmnopqr      " + "~stuvwxyz      "
              + "^        []    " + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "$ STUVWXYZ      " + "0123456789     "
              + "\u5e02\u5f17\u672a\u5378\u59cb" + "\u8679"

      ),

      new CCSIDTestData(1388, "          \u00a3.<(+|"
          + "&         !\u00a5*);\u00ac" + "-/        \u00a6,%_>\u003f"
          + "         `:#@\u0027=\"" + " abcdefghi      " + " jklmnopqr      "
          + "~\u203estuvwxyz      " + "^ \\       []    " + "{ABCDEFGHI      "
          + "}JKLMNOPQR      " + "$ STUVWXYZ      " + "0123456789     "),

      new CCSIDTestData(1399,
          "  \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\u00a2.<(+|"
              + "&\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71!$*);\u00ac"
              + "-/\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79 ,%_>\u003f"
              + "\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82`:#@\u0027=\""
              + " abcdefghi\uff83\uff84\uff85\uff86\uff87\uff88"
              + " jklmnopqr\uff89\uff8a\uff8b\uff8c\uff8d\uff8e"
              + "\u203e~stuvwxyz\uff8f\uff90\uff91[\uff92\uff93"
              + "^\u00a3\u00a5\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d]\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "\\\u20acSTUVWXYZ      " + "0123456789     "),

      new CCSIDTestData(4971,
          " \u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399[.<(+!"
              + "&\u039a\u039b\u039c\u039d\u039e\u039f\u03a0\u03a1\u03a3]$*);^"
              + "-/\u03a4\u03a5\u03a6\u03a7\u03a8\u03a9\u03aa\u03ab|,%_>\u003f"
              + "\u00a8\u0386\u0388\u0389\u00a0\u038a\u038c\u038e\u038f`:#@\u0027=\""
              + "\u0385abcdefghi\u03b1\u03b2\u03b3\u03b4\u03b5\u03b6"
              + "\u00b0jklmnopqr\u03b7\u03b8\u03b9\u03ba\u03bb\u03bc"
              + "\u00b4~stuvwxyz\u03bd\u03be\u03bf\u03c0\u03c1\u03c3"
              + "\u00a3\u03ac\u03ad\u03ae\u03ca\u03af\u03cc\u03cd\u03cb\u03ce\u03c2\u03c4\u03c5\u03c6\u03c7\u03c8"
              + "{ABCDEFGHI\u00ad\u03c9\u0390\u03b0\u2018\u2015"
              + "}JKLMNOPQR\u00b1\u00bd \u0387\u2019\u00a6"
              + "\\ STUVWXYZ\u00b2\u00a7  \u00ab\u00ac"
              + "0123456789\u00b3\u00a9\u20ac \u00bb"),

      /* On 4/9/2008 removed EURO \u20ac from this. See CPS 78JKRU */
      new CCSIDTestData(5026,
          " \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\uff69\u00a3.<(+|"
              + "&\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f \uff70 !\u00a5*);\u00ac"
              + "-/abcdefgh ,%_>\u003f" + "[ijklmnop`:#@\u0027=\""
              + "]\uff71\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79\uff7aq\uff7b\uff7c\uff7d\uff7e"
              + "\uff7f\uff80\uff81\uff82\uff83\uff84\uff85\uff86\uff87\uff88\uff89r \uff8a\uff8b\uff8c"
              + "~\u203e\uff8d\uff8e\uff8f\uff90\uff91\uff92\uff93\uff94\uff95s\uff96\uff97\uff98\uff99"
              + "^\u00a2\\tuvwxyz\uff9a\uff9b\uff9c\uff9d\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "$STUVWXYZ      "
              + "0123456789     "),

      /* On 4/9/2008 removed EURO \u20ac from this. See CPS 78JKRU */

      new CCSIDTestData(5035,
          "  \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\u00a2.<(+|"
              + "&\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71!$*);\u00ac"
              + "-/\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79 ,%_>\u003f"
              + "\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82`:#@\u0027=\""
              + " abcdefghi\uff83\uff84\uff85\uff86\uff87\uff88"
              + " jklmnopqr\uff89\uff8a\uff8b\uff8c\uff8d\uff8e"
              + "\u203e~stuvwxyz\uff8f\uff90\uff91[\uff92\uff93"
              + "^\u00a3\u00a5\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d]\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      " + "\\STUVWXYZ      "
              + "0123456789     "),

      new CCSIDTestData(5123,
          "  \uff61\uff62\uff63\uff64\uff65\uff66\uff67\uff68\u00a2.<(+|"
              + "&\uff69\uff6a\uff6b\uff6c\uff6d\uff6e\uff6f\uff70\uff71!$*);\u00ac"
              + "-/\uff72\uff73\uff74\uff75\uff76\uff77\uff78\uff79 ,%_>\u003f"
              + "\uff7a\uff7b\uff7c\uff7d\uff7e\uff7f\uff80\uff81\uff82`:#@\u0027=\""
              + " abcdefghi\uff83\uff84\uff85\uff86\uff87\uff88"
              + " jklmnopqr\uff89\uff8a\uff8b\uff8c\uff8d\uff8e"
              + "\u203e~stuvwxyz\uff8f\uff90\uff91[\uff92\uff93"
              + "^\u00a3\u00a5\uff94\uff95\uff96\uff97\uff98\uff99\uff9a\uff9b\uff9c\uff9d]\uff9e\uff9f"
              + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "\\\u20acSTUVWXYZ      " + "0123456789     "),

      /* old CCSID 1377 */
      new CCSIDTestData(5473,
          "          \u00a2.<(+|" + "&         !$*);\u00ac"
              + "-/        \u00a6,%_>?" + "         `:#@\u0027=\""
              + " abcdefghi      " + " jklmnopqr      " + "~stuvwxyz      "
              + "^        []    " + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "$ STUVWXYZ      " + "0123456789     "
              + "\u5e02\u5f17\u672a\u5378\u59cb" + "\u8679"

      ),

      /* On 4/9/2008 removed EURO \u20ac from this. See CPS 78JKRU */
      new CCSIDTestData(8612,
          " \u00a0\u0651 \u0640 \u0621\u0622 \u0623\u00a2.<(+|"
              + "& \u0624  \u0626\u0627 \u0628 !$*);\u00ac"
              + "-/\u0629\u062a \u062b \u062c \u062d\u00a6,%_>\u003f"
              + " \u062e \u062f\u0630\u0631\u0632\u0633 \u060c:#@\u0027=\""
              + "\u0634abcdefghi \u0635 \u0636 \u0637"
              + "\u0638jklmnopqr\u0639   \u063a "
              + " \u00f7stuvwxyz \u0641 \u0642 \u0643"
              + " \u0644         \u0645 \u0646 \u0647"
              + "\u061bABCDEFGHI\u00ad    \u0648"
              + "\u061fJKLMNOPQR\u0649 \u064a   " + "\u00d7 STUVWXYZ      "
              + "0123456789    "),

      new CCSIDTestData(9030,
          " \u00a0\u0e01\u0e02\u0e03\u0e04\u0e05\u0e06\u0e07[\u00a2.<(+|"
              + "&\u0e48\u0e08\u0e09\u0e0a\u0e0b\u0e0c\u0e0d\u0e0e]!$*);\u00ac"
              + "-/\u0e0f\u0e10\u0e11\u0e12\u0e13\u0e14\u0e15^\u00a6,%_>\u003f"
              + "\u0e3f\u0e4e\u0e16\u0e17\u0e18\u0e19\u0e1a\u0e1b\u0e1c`:#@\u0027=\""
              + "\u0e4fabcdefghi\u0e1d\u0e1e\u0e1f\u0e20\u0e21\u0e22"
              + "\u0e5ajklmnopqr\u0e23\u0e24\u0e25\u0e26\u0e27\u0e28"
              + "\u0e5b~stuvwxyz\u0e29\u0e2a\u0e2b\u0e2c\u0e2d\u0e2e"
              + "\u0e50\u0e51\u0e52\u0e53\u0e54\u0e55\u0e56\u0e57\u0e58\u0e59\u0e2f\u0e30\u0e31\u0e32\u0e33\u0e34"
              + "{ABCDEFGHI\u0e49\u0e35\u0e36\u0e37\u0e38\u0e39"
              + "}JKLMNOPQR\u0e3a\u0e40\u0e41\u0e42\u0e43\u0e44"
              + "\\\u0e4aSTUVWXYZ\u0e45\u0e46\u0e47\u0e48\u0e49\u0e4a"
              + "0123456789\u0e4b\u0e4c\u0e4d\u0e4b\u0e4c"),

      new CCSIDTestData(12708,

          "\u0662\u0661 9876543210\u0665\u0664\u0663"
              + "\u20ac\u0669\u0668\u0667\u0666ZYXWVUTS\u2007\u00d7\u0649"
              + "\u0649\u064a\u064a\u064a\u0660RQPONMLKJ\u061f\u0647"
              + " \u0647 \u0648\u00adIHGFEDCBA\u063a\u0641"
              + "\u0641\u0642\u0642\u0643\u0643\u0644\u0644\u0622\ufef6\ufef7\ufef8  \ufefb\ufefc\u0644"
              + "\u0645\u0645\u0646\u0646\u0647\u061bzyxwvuts\u00f7\u0639"
              + "\u0639\u0639\u0639\u063a\u063a\u063arqponmlkj\u0634"
              + "\u0635\u0635\u0636\u0636\u0637\u0638ihgfedcba\u0651"
              + "\u0651\u0640\u200b\u0621\u0622\u0622\u0623\u00a2.>)+|&\u0623\u0624"
              + "  \u0626\u0627\u0627\u0628\u0628!$*(;\u00ac-/\u0629"
              + "\u062a\u062a\u062b\u062b\u062c\u062c\u062d\u00a6,%_<\u003f\u062d\u062e\u062e"
              + "\u062f\u0630\u0631\u0632\u0633\u0633\u060c:#@\u0027=\"\u0634\u00a0"),

      new CCSIDTestData(622708, /*
                                 * 610000 + 12708
                                 */

          "\u0662\u0661\u0665\u0664\u0663"
              + "\u20ac\u0669\u0668\u0667\u0666\u2007\u00d7\u0649"
              + "\u0649\u064a\u064a\u064a\u0660\u061f\u0647"
              + " \u0647 \u0648\u00ad\u063a\u0641"
              + "\u0641\u0642\u0642\u0643\u0643\u0644\u0644\u0622\ufef6\ufef7\ufef8 \ufefb\ufefc\u0644"
              + "\u0645\u0645\u0646\u0646\u0647\u061b\u00f7\u0639"
              + "\u0639\u0639\u0639\u063a\u063a\u063a\u0634"
              + "\u0635\u0635\u0636\u0636\u0637\u0638\u0651"
              + "\u0651\u0640\u200b\u0621\u0622\u0622\u0623\u00a2.>)+|&\u0623\u0624"
              + "  \u0626\u0627\u0627\u0628\u0628!$*(;\u00ac-/\u0629"
              + "\u062a\u062a\u062b\u062b\u062c\u062c\u062d\u00a6,%_<\u003f\u062d\u062e\u062e"
              + "\u062f\u0630\u0631\u0632\u0633\u0633\u060c:#@\u0027=\"\u0634\u00a0"
              + " 9876543210ZYXWVUTSRPONMLKJIHGFEDCBAzyxwvutsrqponmlkjihgfedcba"),

      new CCSIDTestData(13121,

          "  \uffa0\uffa1\uffa2\uffa3\uffa4\uffa5\uffa6\uffa7\u00a2.<(+|"
              + "& \uffa8\uffa9\uffaa\uffab\uffac\uffad\uffae\uffaf!$*);\u00ac"
              + "-/\uffb0\uffb1\uffb2\uffb3\uffb4\uffb5\uffb6\uffb7\u00a6,%_>\u003f"
              + "[ \uffb8\uffb9\uffba\uffbb\uffbc\uffbd\uffbe`:#@\u0027=\""
              + "]abcdefghi\uffc2\uffc3\uffc4\uffc5\uffc6\uffc7"
              + " jklmnopqr\uffca\uffcb\uffcc\uffcd\uffce\uffcf"
              + "\u203e~stuvwxyz\uffd2\uffd3\uffd4\uffd5\uffd6\uffd7"
              + "^ \\       \uffda\uffdb\uffdc   " + "{ABCDEFGHI      "
              + "}JKLMNOPQR      " + "\u20a9 STUVWXYZ      "
              + "0123456789     "),

      new CCSIDTestData(13124, "          \u00a3.<(+|"
          + "&         !\u00a5*);\u00ac" + "-/        \u00a6,%_>\u003f"
          + "         `:#@\u0027=\"" + " abcdefghi      " + " jklmnopqr      "
          + "~\u203estuvwxyz      " + "^ \\       []    " + "{ABCDEFGHI      "
          + "}JKLMNOPQR      " + "$ STUVWXYZ      " + "0123456789     "),

      new CCSIDTestData /* Old ccsid 1388 */
      (13676, "          \u00a3.<(+|" + "&         !\u00a5*);\u00ac"
          + "-/        \u00a6,%_>\u003f" + "         `:#@\u0027=\""
          + " abcdefghi      " + " jklmnopqr      " + "~\u203estuvwxyz      "
          + "^ \\       []    " + "{ABCDEFGHI      " + "}JKLMNOPQR      "
          + "$ STUVWXYZ      " + "0123456789     "),

      new CCSIDTestData(28709,
          "          \u00a2.<(+|" + "&         !$*);\u00ac"
              + "-/        \u00a6,%_>\u003f" + "         `:#@\u0027=\""
              + " abcdefghi      " + " jklmnopqr      " + " ~stuvwxyz      "
              + "^         []    " + "{ABCDEFGHI      " + "}JKLMNOPQR      "
              + "\\ STUVWXYZ      " + "0123456789     "),

      new CCSIDTestData(62211, "hebrew code page 62211 \u05D7"
          + " \u05d0\u05d1\u05d2\u05d3\u05d4\u05d5\u05d6\u05d7\u05d8\u00a2.<(+|"
          + "&\u05d9\u05da\u05db\u05dc\u05dd\u05de\u05df\u05e0\u05e1!$*);\u00ac"
          + "-/\u05e2\u05e3\u05e4\u05e5\u05e6\u05e7\u05e8\u05e9\u00a6,%_>\u003f"
          + " \u05ea  \u00a0   \u2017`:#@\u0027=\""
          + " abcdefghi\u00ab\u00bb   \u00b1"
          + "\u00b0jklmnopqr  \u20ac\u00b8\u20aa\u00a4"
          + "\u00b5~stuvwxyz     \u00ae"
          + "^\u00a3\u00a5\u2022\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u203e\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad     " + "}JKLMNOPQR\u00b9\u202d\u202e\u202c  "
          + "\\\u00f7STUVWXYZ\u00b2     "
          + "0123456789\u00b3\u202a\u202b\u200e\u200f"),

      new CCSIDTestData(62224,
          "arabic code page 62224 \u0633"
              + " \u00a0\u0651 \u0640 \u0621\u0622 \u0623\u00a2.<(+|"
              + "& \u0624  \u0626\u0627 \u0628 !$*);\u00ac"
              + "-/\u0629\u062a \u062b \u062c \u062d\u00a6,%_>\u003f"
              + " \u062e \u062f\u0630\u0631\u0632\u0633 \u060c:#@\u0027=\""
              + "\u0634abcdefghi \u0635 \u0636 \u0637"
              + "\u0638jklmnopqr\u0639   \u063a "
              + " \u00f7stuvwxyz \u0641 \u0642 \u0643"
              + " \u0644         \u0645 \u0646 \u0647"
              + "\u061bABCDEFGHI\u00ad    \u0648"
              + "\u061fJKLMNOPQR\u0649 \u064a   " + "\u00d7 STUVWXYZ      "
              + "0123456789\u20ac    "),

      new CCSIDTestData(62235, "hebrew code page 62235 \u05D7"
          + " \u05d0\u05d1\u05d2\u05d3\u05d4\u05d5\u05d6\u05d7\u05d8\u00a2.<(+|"
          + "&\u05d9\u05da\u05db\u05dc\u05dd\u05de\u05df\u05e0\u05e1!$*);\u00ac"
          + "-/\u05e2\u05e3\u05e4\u05e5\u05e6\u05e7\u05e8\u05e9\u00a6,%_>\u003f"
          + " \u05ea  \u00a0   \u2017`:#@\u0027=\""
          + " abcdefghi\u00ab\u00bb   \u00b1"
          + "\u00b0jklmnopqr  \u20ac\u00b8\u20aa\u00a4"
          + "\u00b5~stuvwxyz     \u00ae"
          + "^\u00a3\u00a5\u2022\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u203e\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad     " + "}JKLMNOPQR\u00b9\u202d\u202e\u202c  "
          + "\\\u00f7STUVWXYZ\u00b2     "
          + "0123456789\u00b3\u202a\u202b\u200e\u200f"),

      new CCSIDTestData(62245, "hebrew code page 62245 \u05D7"
          + " \u05d0\u05d1\u05d2\u05d3\u05d4\u05d5\u05d6\u05d7\u05d8\u00a2.<(+|"
          + "&\u05d9\u05da\u05db\u05dc\u05dd\u05de\u05df\u05e0\u05e1!$*);\u00ac"
          + "-/\u05e2\u05e3\u05e4\u05e5\u05e6\u05e7\u05e8\u05e9\u00a6,%_>\u003f"
          + " \u05ea  \u00a0   \u2017`:#@\u0027=\""
          + " abcdefghi\u00ab\u00bb   \u00b1"
          + "\u00b0jklmnopqr  \u20ac\u00b8\u20aa\u00a4"
          + "\u00b5~stuvwxyz     \u00ae"
          + "^\u00a3\u00a5\u2022\u00a9\u00a7\u00b6\u00bc\u00bd\u00be[]\u203e\u00a8\u00b4\u00d7"
          + "{ABCDEFGHI\u00ad     " + "}JKLMNOPQR\u00b9\u202d\u202e\u202c  "
          + "\\\u00f7STUVWXYZ\u00b2     "
          + "0123456789\u00b3\u202a\u202b\u200e\u200f"),

      new CCSIDTestData(62251,
          " \u00a0\u00e2\u060c\u00e0\u061b\u0640\u061f\u00e7\u0621\u0622.<(+|"
              + "&\u00e9\u00ea\u00eb\u00e8\u0623\u00ee\u00ef\u0624\u0625!$*);^"
              + "-/\u00c2\u0626\u00c0\u0627\u0628\u0629\u00c7\u062a\u062b,%_>\u003f"
              + "\u062c\u00c9\u00ca\u00cb\u00c8\u062d\u00ce\u00cf\u062e`:#@\u0027=\""
              + "\u062fabcdefghi\u00ab\u00bb\u0630\u0631\u0632\u0633"
              + "\u0634jklmnopqr\u0635\u0636\u00e6\u0637\u00c6\u20ac"
              + "\u00b5~stuvwxyz\u0638\u0639\u063a[\u0641\u0642"
              + "\u0643\u0644\u0645\u0646\u00a9\u00a7\u0647\u0152\u0153\u0178\u0648\u0649\u064a]\u064b\u00d7"
              + "{ABCDEFGHI\u00ad\u00f4\u064c\u064d\u064e\u064f"
              + "}JKLMNOPQR\u0650\u00fb\u00fc\u00f9\u0651\u00ff"
              + "\\\u00f7STUVWXYZ\u0652\u00d4\u200c\u200d\u200e\u200f" + "X"
              + "0123456789 \u00db\u00dc\u00d9\u00a4"),

  };
  /*
   * from Gregory in Israel: (he recomends adding "X" I got realized what is a
   * problem here. This is the famous round-trip problem. This is the very same
   * problem that caused me to recommend Visual CCSID for Bidi (such as 420 for
   * Arabic) as "package ccsid" rather than 13488. In the problematic text
   * segment, strong RTL character '\u200f' (invisible RLM marker) is followed
   * by group of numerals. As a side effect of the reordering, it is moved to
   * position after these numerals. To solve a problem with this specific
   * testcase, I suggest to insert some Latin character after RLM, so the string
   * will be like:
   */
  private char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      'A', 'B', 'C', 'D', 'E', 'F' };
  
  /* Set to true to avoid call to stored procedure that determines valid ccsids */ 
  protected boolean skipValidCheck = false; 

  /**
   * Constructor.
   **/
  public JDStatementStressTest(AS400 systemObject, Hashtable<String,Vector<String>> namesAndVars,
      int runMode, FileOutputStream fileOutputStream, 
      String password, String miscParm) {
    super(systemObject, "JDStatementStressTest", namesAndVars, runMode,
        fileOutputStream, password);

    if (miscParm != null) {
      StringTokenizer tok = new StringTokenizer(miscParm, ",");
      if (tok.countTokens() == 3) {
        ustNum = new Integer(tok.nextToken()).intValue();
        cstNum = new Integer(tok.nextToken()).intValue();
        sstNum = new Integer(tok.nextToken()).intValue();
      }
    }
  }

  public JDStatementStressTest(AS400 systemObject, String testname,
      Hashtable<String,Vector<String>> namesAndVars, int runMode, FileOutputStream fileOutputStream,
       String password, String miscParm) {
    super(systemObject, testname, namesAndVars, runMode, fileOutputStream,
 password);

    if (miscParm != null) {
      StringTokenizer tok = new StringTokenizer(miscParm, ",");
      if (tok.countTokens() == 3) {
        ustNum = new Integer(tok.nextToken()).intValue();
        cstNum = new Integer(tok.nextToken()).intValue();
        sstNum = new Integer(tok.nextToken()).intValue();
      }
    }
  }

  /**
   * Performs setup needed before running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void setup() throws Exception {
    TABLE_ = JDStatementTest.COLLECTION + ".STRESS";
    PROC_ = JDStatementTest.COLLECTION + ".STRPRC";
    ccsidBase_ = JDStatementTest.COLLECTION + ".STRC";

    resetConnection();
    initTable(statement_, TABLE_,
        " (BID DECIMAL(8,0), BDATA VARCHAR(320) FOR BIT DATA)");

    // make some data
    byte b[] = new byte[320];
    for (int i = 0; i < 320; i++) {
      b[i] = (byte) (i % 0xff);
    }

    PreparedStatement ps = connection_.prepareStatement(
        "INSERT INTO " + TABLE_ + " (BID, BDATA) VALUES (1, ?)");

    ps.setBytes(1, b);
    ps.execute();
    ps.close();

    String QIWS = JDSetupProcedure.setupQIWS(systemObject_,
        connection_);

    String drop = "DROP PROCEDURE " + PROC_;
    try {
      Statement s3 = connection_.createStatement();
      s3.executeUpdate(drop);
      s3.close();

    } catch (Exception e) {

    }
    // create a procedure
    String proc = "CREATE PROCEDURE " + PROC_
        + " (IN_1 INTEGER) RESULT SET 1 LANGUAGE SQL " + "BEGIN  "
        + "   DECLARE C3 CURSOR FOR SELECT * FROM " + QIWS + ".QCUSTCDT ; "
        + "   OPEN C3 ; " + "   SET RESULT SETS CURSOR C3 ; " + "END  ";

    Statement s3 = connection_.createStatement();
    s3.executeUpdate(proc);
    s3.close();

    connection_.commit();

    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      String vmName = System.getProperty("java.vm.name");
      if (vmName == null) {
        runningJ9 = false;
      } else {
        if (vmName.indexOf("Classic VM") >= 0) {
          runningJ9 = false;
        } else {
          runningJ9 = true;
        }
      }
    }

  }

  public void resetConnection() throws Exception {
    if (connection_ != null) {
      connection_.close();
    }

    if (getDriver() == JDTestDriver.DRIVER_JCC) {
      // JCC doesn't have these properties
      connection_ = testDriver_.getConnection(baseURL_,systemObject_.getUserId(), encryptedPassword_);

    } else {
      connection_ = testDriver_.getConnection(baseURL_+ ";errors=full;lazy close=true",systemObject_.getUserId(), encryptedPassword_);
    }

    statement_ = connection_.createStatement();

  }

  /**
   * Performs cleanup needed after running variations.
   * 
   * @exception Exception
   *              If an exception occurs.
   **/
  public void cleanup() throws Exception {
    statement_ = connection_.createStatement();
    statement_.executeUpdate("DROP PROCEDURE " + PROC_);
    statement_.executeUpdate("DROP TABLE " + TABLE_);

    statement_.close();
    connection_.close();
  }

  /**
   * basic multithreaded case with Callable, Select, and Update
   **/
  public void Var001() {
  
    if (getSubDriver() == JDTestDriver.SUBDRIVER_JTOPENCA) {
      notApplicable("Multiple threads uses same connection causes problems for JDTOPENCA");
      return; 
    }
    
    if (getSubDriver() == JDTestDriver.SUBDRIVER_JTOPENSF) {
      notApplicable("Multiple threads uses same connection causes problems for JDTOPENSF");
      return; 
    }

    try {
      
      JDStatementStressUpdate ust[] = new JDStatementStressUpdate[2];
      for (int i = 1; i <= ust.length; i++) {
        ust[i - 1] = new JDStatementStressUpdate(testDriver_, connection_, i,
            JDStatementTest.COLLECTION);
        ust[i - 1].setName("JDStatementStressUpdate" + i);
        ust[i - 1].start();
      }

      JDStatementStressCallable cst[] = new JDStatementStressCallable[2];
      for (int i = 1; i <= cst.length; i++) {
        cst[i - 1] = new JDStatementStressCallable(connection_, i,
            JDStatementTest.COLLECTION);
        cst[i - 1].setName("JDStatementStressCallable" + i);
        cst[i - 1].start();
      }

      JDStatementStressSelect sst[] = new JDStatementStressSelect[2];
      for (int i = 1; i <= sst.length; i++) {
        sst[i - 1] = new JDStatementStressSelect(connection_, i,
            JDStatementTest.COLLECTION);
        sst[i - 1].setName("JDStatementStressSelect" + i);
        sst[i - 1].start();
      }

      while (isThreadStillAlive(ust, cst, sst)) {
        Thread.sleep(100);
      }
      StringBuffer sb = new StringBuffer();
      boolean success = wereThreadsSuccessful(ust, cst, sst, sb);
      assertCondition(success, sb);
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  protected boolean isThreadStillAlive(JDStatementStressUpdate ust[],
      JDStatementStressCallable cst[], JDStatementStressSelect sst[]) {
    if (ust != null) {
      for (int i = 0; i < ust.length; i++) {
        if (ust[i].isAlive()) {
          return true;
        }
      }
    }
    if (cst != null) {
      for (int i = 0; i < cst.length; i++) {
        if (cst[i].isAlive()) {
          return true;
        }
      }
    }
    if (sst != null) {
      for (int i = 0; i < sst.length; i++) {
        if (sst[i].isAlive()) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean wereThreadsSuccessful(JDStatementStressUpdate ust[],
      JDStatementStressCallable cst[], JDStatementStressSelect sst[],
      StringBuffer sb) {
    boolean passed = true;

    for (int i = 0; i < ust.length; i++) {
      if (!ust[i].getSuccessful()) {
        passed = false;
        ust[i].getErrorInfo(sb);
      }
    }
    for (int i = 0; i < cst.length; i++) {
      if (!cst[i].getSuccessful()) {
        passed = false;
        cst[i].getErrorInfo(sb);
      }
    }
    for (int i = 0; i < sst.length; i++) {
      if (!sst[i].getSuccessful()) {
        passed = false;
        sst[i].getErrorInfo(sb);
      }
    }
    return passed;
  }

  public void ccsidTest(int variation, int ccsid, String message) {
    boolean toolbox = isToolboxDriver();
    if (toolbox && !checkLongRunning("JDStatementStressTest", variation,
        PROBABILITY, AGE)) {
      return;
    }

    //
    // These tests are not valid for V5R2 (since we convert to/from job ccsid)
    //
    boolean result = false;
    try {
      result = stressCCSID(ccsid, message);
      if (result) {
        succeeded();
      } else {
        // failed has already been signaled by stressCCSID
      }
    } catch (Throwable e) {
      failed(e, "Unexpected Exception for " + ccsid + " : " + message);
    }
    if (toolbox) {
      finishLongRunning("JDStatementStressTest", variation, result);
    }

  }

  public void ccsidTestParm(int variation, int ccsid, String message) {
    //
    // These tests are not valid for V5R2 (since we convert to/from job ccsid)
    //
    boolean result = true;

    boolean toolbox = isToolboxDriver();
    if (toolbox && !checkLongRunning("JDStatementStressTest", variation,
        PROBABILITY, AGE)) {
      return;
    }

    try {
      result = stressCCSIDParm(ccsid, message);
      if (result) {
        succeeded();
      } else {
        // failed has already been signaled by stressCCSID
      }
    } catch (Throwable e) {
      failed(e, "Unexpected Exception for " + ccsid + " : " + message);
    }
    if (toolbox) {
      finishLongRunning("JDStatementStressTest", variation, result);
    }

  }

  /*
   * Created using grep '([0-9][0-9]*, *$' test/JDStatementStressTest.java | sed
   * 's/.*(\([0-9][0-9]**\),.*$/public void Var0() { ccsidTest(\1,
   * "added by native driver 08\/04\/2005");}/'
   */

  public void Var002() {
    ccsidTest(2, 37, "added by native driver 10/26/2005");
  }

  public void Var003() {
    ccsidTest(3, 256, "added by native driver 10/26/2005");
  }

  public void Var004() {
    ccsidTest(4, 273, "added by native driver 10/26/2005");
  }

  public void Var005() {
    ccsidTest(5, 277, "added by native driver 10/26/2005");
  }

  public void Var006() {
    ccsidTest(6, 278, "added by native driver 10/26/2005");
  }

  public void Var007() {
    ccsidTest(7, 280, "added by native driver 10/26/2005");
  }

  public void Var008() {
    ccsidTest(8, 284, "added by native driver 10/26/2005");
  }

  public void Var009() {
    ccsidTest(9, 285, "added by native driver 10/26/2005");
  }

  public void Var010() {
    ccsidTest(10, 290, "added by native driver 10/26/2005");
  }

  public void Var011() {
    ccsidTest(11, 297, "added by native driver 10/26/2005");
  }

  public void Var012() {
    if (isToolboxDriver())
      notApplicable();
    else
      ccsidTest(12, 420, "added by native driver 10/26/2005");
  }

  public void Var013() {
    ccsidTest(13, 423, "added by native driver 10/26/2005");
  }

  public void Var014() {
    ccsidTest(14, 424, "added by native driver 4/21/2010");

  }

  public void Var015() {
    ccsidTest(15, 425, "added by native driver 10/26/2005");
  }

  public void Var016() {
    ccsidTest(16, 500, "added by native driver 10/26/2005");
  }

  public void Var017() {
    ccsidTest(17, 833, "added by native driver 10/26/2005");
  }

  public void Var018() {
    ccsidTest(18, 836, "added by native driver 10/26/2005");
  }

  public void Var019() {
    ccsidTest(19, 838, "added by native driver 10/26/2005");
  }

  public void Var020() {
    ccsidTest(20, 870, "added by native driver 10/26/2005");
  }

  public void Var021() {
    ccsidTest(21, 871, "added by native driver 10/26/2005");
  }

  public void Var022() {
    ccsidTest(22, 875, "added by native driver 10/26/2005");
  }

  public void Var023() {
    ccsidTest(23, 880, "added by native driver 10/26/2005");
  }

  public void Var024() {
    ccsidTest(24, 905, "added by native driver 10/26/2005");
  }

  public void Var025() {
    if (isToolboxDriver()) {
      notApplicable(
          "TODO when time permits.  CCSID 918 Not working on due to InputStreamReader implementaion (on 7/28/2007)");
      return;
    }
    ccsidTest(25, 918, "added by native driver 10/26/2005");
  }

  public void Var026() {
    ccsidTest(26, 924, "added by native driver 10/26/2005");
  }

  public void Var027() {
    ccsidTest(27, 930, "added by native driver 10/26/2005");
  }

  public void Var028() {
    ccsidTest(28, 933, "added by native driver 10/26/2005");
  }

  public void Var029() {
    ccsidTest(29, 935, "added by native driver 10/26/2005");
  }

  public void Var030() {
    notApplicable();
  }

  public void Var031() {

    if (runningJ9) {
      ccsidTest(31, 610937, "added by native driver 10/26/2005");

    } else {
      ccsidTest(31, 937, "added by native driver 10/26/2005");
    }
  }

  public void Var032() {
    ccsidTest(32, 939, "added by native driver 10/26/2005");
  }

  public void Var033() {
    ccsidTest(33, 1025, "added by native driver 10/26/2005");
  }

  public void Var034() {
    ccsidTest(34, 1026, "added by native driver 10/26/2005");
  }

  public void Var035() {
    ccsidTest(35, 1027, "added by native driver 10/26/2005");
  }

  public void Var036() {
    if (isToolboxDriver()) {
      notApplicable(
          "TODO when time permits.  CCSID 1097 Not working on due to InputStreamReader implementaion (on 7/28/2007)");
      return;
    }
    ccsidTest(36, 1097, "added by native driver 10/26/2005");
  }

  public void Var037() {
    ccsidTest(37, 1112, "added by native driver 10/26/2005");
  }

  public void Var038() {
    ccsidTest(38, 1122, "added by native driver 10/26/2005");
  }

  public void Var039() {
    ccsidTest(39, 1123, "added by native driver 10/26/2005");
  }

  public void Var040() {
    ccsidTest(40, 1130, "added by native driver 10/26/2005");
  }

  public void Var041() {
    ccsidTest(41, 1132, "added by native driver 10/26/2005");
  }

  public void Var042() {
    ccsidTest(42, 1137, "added by native driver 10/26/2005");
  }

  public void Var043() {
    ccsidTest(43, 1140, "added by native driver 10/26/2005");
  }

  public void Var044() {
    ccsidTest(44, 1141, "added by native driver 10/26/2005");
  }

  public void Var045() {
    ccsidTest(45, 1142, "added by native driver 10/26/2005");
  }

  public void Var046() {
    ccsidTest(46, 1143, "added by native driver 10/26/2005");
  }

  public void Var047() {
    ccsidTest(47, 1144, "added by native driver 10/26/2005");
  }

  public void Var048() {
    ccsidTest(48, 1145, "added by native driver 10/26/2005");
  }

  public void Var049() {
    ccsidTest(49, 1146, "added by native driver 10/26/2005");
  }

  public void Var050() {
    ccsidTest(50, 1147, "added by native driver 10/26/2005");
  }

  public void Var051() {
    ccsidTest(51, 1148, "added by native driver 10/26/2005");
  }

  public void Var052() {
    ccsidTest(52, 1149, "added by native driver 10/26/2005");
  }

  public void Var053() {
    ccsidTest(53, 1153, "added by native driver 10/26/2005");
  }

  public void Var054() {
    ccsidTest(54, 1154, "added by native driver 10/26/2005");
  }

  public void Var055() {
    ccsidTest(55, 1155, "added by native driver 10/26/2005");
  }

  public void Var056() {
    ccsidTest(56, 1156, "added by native driver 10/26/2005");
  }

  public void Var057() {
    ccsidTest(57, 1157, "added by native driver 10/26/2005");
  }

  public void Var058() {
    ccsidTest(58, 1158, "added by native driver 10/26/2005");
  }

  public void Var059() {
    ccsidTest(59, 1160, "added by native driver 10/26/2005");
  }

  public void Var060() {
    ccsidTest(60, 1164, "added by native driver 10/26/2005");
  }

  public void Var061() {
    ccsidTest(61, 1208, "added by native driver 10/26/2005");
  }

  public void Var062() {
    ccsidTest(62, 1364, "added by native driver 10/26/2005");
  }

  public void Var063() {
    ccsidTest(63, 1388, "added by native driver 10/26/2005");
  }

  public void Var064() {
    ccsidTest(64, 1399, "added by native driver 10/26/2005");
  }

  public void Var065() {
    ccsidTest(65, 4971, "added by native driver 10/26/2005");
  }

  public void Var066() {
    ccsidTest(66, 5026, "added by native driver 10/26/2005");
  }

  public void Var067() {
    ccsidTest(67, 5035, "added by native driver 10/26/2005");
  }

  public void Var068() {
    ccsidTest(68, 5123, "added by native driver 10/26/2005");
  }

  public void Var069() {
    ccsidTest(69, 8612, "added by native driver 10/26/2005");
  }

  public void Var070() {
    ccsidTest(70, 9030, "added by native driver 10/26/2005");
  }

  public void Var071() {
    if (isToolboxDriver())
      notApplicable();
    else if (getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
      ccsidTest(71, 622708, "added by native driver 10/26/2005");
    } else {
      ccsidTest(71, 12708, "added by native driver 10/26/2005");
    }
  }

  public void Var072() {
    ccsidTest(72, 13121, "added by native driver 10/26/2005");
  }

  public void Var073() {
    ccsidTest(73, 13124, "added by native driver 10/26/2005");
  }

  public void Var074() {
    ccsidTest(74, 28709, "added by native driver 10/26/2005");
  }

  public void Var075() {
    ccsidTest(75, 62211, "added by native driver 10/26/2005");
  }

  public void Var076() {
    ccsidTest(76, 62224, "added by native driver 10/26/2005");
  }

  public void Var077() {
    ccsidTest(77, 62235, "added by native driver 10/26/2005");
  }

  public void Var078() {

    // 4/21/2010 added agian for toolbox
    ccsidTest(78, 62245, "added by native driver 10/26/2005");

  }

  public void Var079() {
    ccsidTest(79, 62251, "added by native driver 10/26/2005");
  }

  public void Var080() {
    ccsidTest(80, 1377, "added by toolbox driver 9/19/2017");
  }

  public void testOneCcsid(int ccsid) {
    boolean passed = true;
    StringBuffer sb = new StringBuffer();
    try {
      if (statement_ != null) {
        statement_.close();
      }
      statement_ = connection_.createStatement();

      if (!skipCcsid(ccsid)) {
        if (!testCcsid(ccsid, sb)) {
          passed = false;
        }

      } else {
        System.out.println("Warning: skipping ccsid " + ccsid);
      }
      assertCondition(passed, sb);
    } catch (Exception e) {

      failed(e, "Unexpected Exception " + sb.toString());

      // If failure, reset connection
      try {
        resetConnection();
      } catch (Exception e2) {
        System.out.println("Error resetting connection");
        e2.printStackTrace(System.out);
      }
    }

  }

  public void Var081() {
    testOneCcsid(1377);
  }

  public void Var082() {
    testOneCcsid(37);
  }

  public void Var083() {
    ccsidTest(83, 5473, "added 02/12/2021");
  }

  public void Var084() {
    ccsidTest(84, 13676, "added 02/12/2021");
  }

  public void Var085() {
    notApplicable();
  }

  public void Var086() {
    notApplicable();
  }

  public void Var087() {
    notApplicable();
  }

  public void Var088() {
    notApplicable();
  }

  public void Var089() {
    notApplicable();
  }

  public void Var090() {
    notApplicable();
  }

  public void Var091() {
    notApplicable();
  }

  public void Var092() {
    notApplicable();
  }

  public void Var093() {
    notApplicable();
  }

  public void Var094() {
    notApplicable();
  }

  public void Var095() {
    notApplicable();
  }

  public void Var096() {
    notApplicable();
  }

  public void Var097() {
    notApplicable();
  }

  public void Var098() {
    notApplicable();
  }

  public void Var099() {

    int ccsid = 62235;
    try {
      boolean result;
      result = stressCCSID(ccsid, "added by native driver 10/26/2005");
      if (result) {
        succeeded();
      } else {
        // failed has already been signaled by stressCCSID
      }
    } catch (Throwable e) {
      failed(e, "Unexpected Exception for " + ccsid);
    }
  }

  /**
   * Run all the CCSID tests
   */
  public void Var100() {

    if (isToolboxDriver()) {
      notApplicable();
    } else {

      int ccsid = 0;
      try {
        boolean result = true;

        for (int i = 0; result && i < ccsidTestData.length; i++) {
          ccsid = ccsidTestData[i].ccsid;
          if (ccsid == 424 && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
            // Skip
          } else if (ccsid == 12708
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          } else if (ccsid >= 610000
              && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
            // skip
          } else {
            result = stressCCSID(ccsid, "Testing CCSID " + ccsid
                + " added by native driver 10/26/2005");
          }
        }
        if (result) {
          succeeded();
        } else {
          // failed has already been signaled by stressCCSID
        }
      } catch (Throwable e) {
        failed(e, "Unexpected Exception with ccsid " + ccsid);
      }
    }
  }

  public void Var101() {
    notApplicable();
  }

  public void Var102() {
    ccsidTestParm(102, 37, "added by native driver 02/06/2006");
  }

  public void Var103() {
    ccsidTestParm(103, 256, "added by native driver 02/06/2006");
  }

  public void Var104() {
    ccsidTestParm(104, 273, "added by native driver 02/06/2006");
  }

  public void Var105() {
    ccsidTestParm(105, 277, "added by native driver 02/06/2006");
  }

  public void Var106() {
    ccsidTestParm(106, 278, "added by native driver 02/06/2006");
  }

  public void Var107() {
    ccsidTestParm(107, 280, "added by native driver 02/06/2006");
  }

  public void Var108() {
    ccsidTestParm(108, 284, "added by native driver 02/06/2006");
  }

  public void Var109() {
    ccsidTestParm(109, 285, "added by native driver 02/06/2006");
  }

  public void Var110() {
    ccsidTestParm(110, 290, "added by native driver 02/06/2006");
  }

  public void Var111() {
    ccsidTestParm(111, 297, "added by native driver 02/06/2006");
  }

  public void Var112() {
   if (isToolboxDriver())
      notApplicable();
    else
      ccsidTestParm(112, 420, "added by native driver 02/06/2006");
  }

  public void Var113() {
    ccsidTestParm(113, 423, "added by native driver 02/06/2006");
  }

  public void Var114() {

    ccsidTestParm(114, 424, "added by native driver 02/06/2006");

  }

  public void Var115() {
    ccsidTestParm(115, 425, "added by native driver 02/06/2006");
  }

  public void Var116() {
    ccsidTestParm(116, 500, "added by native driver 02/06/2006");
  }

  public void Var117() {
    ccsidTestParm(117, 833, "added by native driver 02/06/2006");
  }

  public void Var118() {
    ccsidTestParm(118, 836, "added by native driver 02/06/2006");
  }

  public void Var119() {
    ccsidTestParm(119, 838, "added by native driver 02/06/2006");
  }

  public void Var120() {
    ccsidTestParm(120, 870, "added by native driver 02/06/2006");
  }

  public void Var121() {
    ccsidTestParm(121, 871, "added by native driver 02/06/2006");
  }

  public void Var122() {
    ccsidTestParm(122, 875, "added by native driver 02/06/2006");
  }

  public void Var123() {
    ccsidTestParm(123, 880, "added by native driver 02/06/2006");
  }

  public void Var124() {
    ccsidTestParm(124, 905, "added by native driver 02/06/2006");
  }

  public void Var125() {
    if (isToolboxDriver()) {
      notApplicable(
          "TODO when time permits.  CCSID 918 Not working on due to InputStreamReader implementaion (on 7/28/2007)");
      return;
    }

    ccsidTestParm(125, 918, "added by native driver 02/06/2006");
  }

  public void Var126() {
    ccsidTestParm(126, 924, "added by native driver 02/06/2006");
  }

  public void Var127() {
    ccsidTestParm(127, 930, "added by native driver 02/06/2006");
  }

  public void Var128() {
    ccsidTestParm(128, 933, "added by native driver 02/06/2006");
  }

  public void Var129() {
    ccsidTestParm(129, 935, "added by native driver 02/06/2006");
  }

  public void Var130() {
    notApplicable();
  }

  public void Var131() {

    if (runningJ9) {
      ccsidTestParm(131, 610937, "added by native driver 02/06/2006");
    } else {
      ccsidTestParm(131, 937, "added by native driver 02/06/2006");
    }
  }

  public void Var132() {
    ccsidTestParm(132, 939, "added by native driver 02/06/2006");
  }

  public void Var133() {
    ccsidTestParm(133, 1025, "added by native driver 02/06/2006");
  }

  public void Var134() {
    ccsidTestParm(134, 1026, "added by native driver 02/06/2006");
  }

  public void Var135() {
    ccsidTestParm(135, 1027, "added by native driver 02/06/2006");
  }

  public void Var136() {
    if (isToolboxDriver()) {
      notApplicable(
          "TODO when time permits.  CCSID 1097 Not working on due to InputStreamReader implementaion (on 7/28/2007)");
      return;
    }

    ccsidTestParm(136, 1097, "added by native driver 02/06/2006");
  }

  public void Var137() {
    ccsidTestParm(137, 1112, "added by native driver 02/06/2006");
  }

  public void Var138() {
    ccsidTestParm(138, 1122, "added by native driver 02/06/2006");
  }

  public void Var139() {
    ccsidTestParm(139, 1123, "added by native driver 02/06/2006");
  }

  public void Var140() {
    ccsidTestParm(140, 1130, "added by native driver 02/06/2006");
  }

  public void Var141() {
    ccsidTestParm(141, 1132, "added by native driver 02/06/2006");
  }

  public void Var142() {
    ccsidTestParm(142, 1137, "added by native driver 02/06/2006");
  }

  public void Var143() {
    ccsidTestParm(143, 1140, "added by native driver 02/06/2006");
  }

  public void Var144() {
    ccsidTestParm(144, 1141, "added by native driver 02/06/2006");
  }

  public void Var145() {
    ccsidTestParm(145, 1142, "added by native driver 02/06/2006");
  }

  public void Var146() {
    ccsidTestParm(146, 1143, "added by native driver 02/06/2006");
  }

  public void Var147() {
    ccsidTestParm(147, 1144, "added by native driver 02/06/2006");
  }

  public void Var148() {
    ccsidTestParm(148, 1145, "added by native driver 02/06/2006");
  }

  public void Var149() {
    ccsidTestParm(149, 1146, "added by native driver 02/06/2006");
  }

  public void Var150() {
    ccsidTestParm(150, 1147, "added by native driver 02/06/2006");
  }

  public void Var151() {
    ccsidTestParm(151, 1148, "added by native driver 02/06/2006");
  }

  public void Var152() {
    ccsidTestParm(152, 1149, "added by native driver 02/06/2006");
  }

  public void Var153() {
    ccsidTestParm(153, 1153, "added by native driver 02/06/2006");
  }

  public void Var154() {
    ccsidTestParm(154, 1154, "added by native driver 02/06/2006");
  }

  public void Var155() {
    ccsidTestParm(155, 1155, "added by native driver 02/06/2006");
  }

  public void Var156() {
    ccsidTestParm(156, 1156, "added by native driver 02/06/2006");
  }

  public void Var157() {
    ccsidTestParm(157, 1157, "added by native driver 02/06/2006");
  }

  public void Var158() {
    ccsidTestParm(158, 1158, "added by native driver 02/06/2006");
  }

  public void Var159() {
    ccsidTestParm(159, 1160, "added by native driver 02/06/2006");
  }

  public void Var160() {
    ccsidTestParm(160, 1164, "added by native driver 02/06/2006");
  }

  public void Var161() {
    ccsidTestParm(161, 1208, "added by native driver 02/06/2006");
  }

  public void Var162() {
    ccsidTestParm(162, 1364, "added by native driver 02/06/2006");
  }

  public void Var163() {
    ccsidTestParm(163, 1388, "added by native driver 02/06/2006");
  }

  public void Var164() {
    ccsidTestParm(164, 1399, "added by native driver 02/06/2006");
  }

  public void Var165() {
    ccsidTestParm(165, 4971, "added by native driver 02/06/2006");
  }

  public void Var166() {
    ccsidTestParm(166, 5026, "added by native driver 02/06/2006");
  }

  public void Var167() {
    ccsidTestParm(167, 5035, "added by native driver 02/06/2006");
  }

  public void Var168() {
    ccsidTestParm(168, 5123, "added by native driver 02/06/2006");
  }

  public void Var169() {
    ccsidTestParm(169, 8612, "added by native driver 02/06/2006");
  }

  public void Var170() {
    ccsidTestParm(170, 9030, "added by native driver 02/06/2006");
  }

  public void Var171() {
    if (isToolboxDriver()) {
      notApplicable();
      return;
    }
    ccsidTestParm(171, 622708, "added by native driver 02/06/2006");
  }

  public void Var172() {
    ccsidTestParm(172, 13121, "added by native driver 02/06/2006");
  }

  public void Var173() {
    ccsidTestParm(173, 13124, "added by native driver 02/06/2006");
  }

  public void Var174() {
    ccsidTestParm(174, 28709, "added by native driver 02/06/2006");
  }

  public void Var175() {
    ccsidTestParm(175, 62211, "added by native driver 02/06/2006");
  }

  public void Var176() {
    ccsidTestParm(176, 62224, "added by native driver 02/06/2006");
  }

  public void Var177() {
    ccsidTestParm(177, 62235, "added by native driver 02/06/2006");
  }

  public void Var178() {

    if (isToolboxDriver())
      notApplicable();
    else
      ccsidTestParm(178, 62245, "added by native driver 02/06/2006");
  }

  public void Var179() {
    ccsidTestParm(179, 62251, "added by native driver 02/06/2006");
  }

  public void Var180() {
    notApplicable();
  }

  public void Var181() {
    notApplicable();
  }

  public void Var182() {
    notApplicable();
  }

  public void Var183() {
    notApplicable();
  }

  public void Var184() {
    notApplicable();
  }

  public void Var185() {
    notApplicable();
  }

  public void Var186() {
    notApplicable();
  }

  public void Var187() {
    notApplicable();
  }

  public void Var188() {
    notApplicable();
  }

  public void Var189() {
    notApplicable();
  }

  public void Var190() {
    notApplicable();
  }

  public void Var191() {
    notApplicable();
  }

  public void Var192() {
    notApplicable();
  }

  public void Var193() {
    notApplicable();
  }

  public void Var194() {
    notApplicable();
  }

  public void Var195() {
    notApplicable();
  }

  public void Var196() {
    notApplicable();
  }

  public void Var197() {
    notApplicable();
  }

  public void Var198() {
    notApplicable();
  }

  public void Var199() {

    int ccsid = 62235;
    try {
      boolean result;
      result = stressCCSID(ccsid, "added by native driver 02/06/2006");
      if (result) {
        succeeded();
      } else {
        // failed has already been signaled by stressCCSID
      }
    } catch (Throwable e) {
      failed(e, "Unexpected Exception for " + ccsid);
    }
  }

  /**
   * Run all the CCSID tests
   */
  public void Var200() {

    if (isToolboxDriver()) {
      notApplicable();
    } else {

      int ccsid = 0;
      try {
        boolean result = true;

        for (int i = 0; result && i < ccsidTestData.length; i++) {
          ccsid = ccsidTestData[i].ccsid;
          if (ccsid == 424 && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
            // Skip
          } else if (ccsid == 12708
              && getRelease() >= JDTestDriver.RELEASE_V7R1M0) {
          } else if (ccsid >= 610000
              && getRelease() < JDTestDriver.RELEASE_V7R1M0) {
            // skip
          } else {

            result = stressCCSIDParm(ccsid, "Testing CCSID " + ccsid
                + " added by native driver 02/06/2006");
          }
        }
        if (result) {
          succeeded();
        } else {
          // failed has already been signaled by stressCCSID
        }
      } catch (Throwable e) {
        failed(e, "Unexpected Exception with ccsid " + ccsid);
      }
    }
  }

  private String getDifferences(String a, String b) {
    StringBuffer sb = new StringBuffer();
    int aLength = a.length();
    int bLength = b.length();
    if (aLength != bLength) {
      sb.append("lengths are different " + aLength + " " + bLength + "\n");
    }
    int top = aLength;
    if (bLength > top) {
      top = bLength;
    }
    for (int i = 0; i < aLength; i++) {
      char aChar = 0;
      char bChar = 0;
      if (i < aLength) {
        aChar = a.charAt(i);
      }
      if (i < bLength) {
        bChar = b.charAt(i);
      }

      String aCharPrint;
      String bCharPrint;
      if (aChar != bChar) {
        if (aChar >= ' ' && aChar <= 0x7e) {
          aCharPrint = " " + aChar;
        } else {
          aCharPrint = "XX";
        }

        if (bChar >= ' ' && bChar <= 0x7e) {
          bCharPrint = " " + bChar;
        } else {
          bCharPrint = "XX";
        }

        if (i < bLength && i < aLength) {
          sb.append(
              "[" + i + "] " + aCharPrint + " " + Integer.toHexString(aChar)
                  + " != " + Integer.toHexString(bChar) + " " + bCharPrint);
        } else if (i < aLength) {
          sb.append("[" + i + "] " + aCharPrint + " 0x"
              + Integer.toHexString(aChar) + "!= NOT SET ");
        } else {
          sb.append("[" + i + "] NOT SET != " + Integer.toHexString(bChar) + " "
              + bCharPrint);
        }
        sb.append("\n");
      }
    }

    return sb.toString();
  }

  /**
   * Runs different tests using the specified CCSID. The goal is to verify that
   * data in that CCSID can be inserted and retrieved. Written by the native
   * driver after determining that CCSID 62235 has a problem in V5R3..
   */

  private boolean stressCCSID(int ccsid, String failedMessage) {
    String tablename = ccsidBase_ + ccsid;

    /*
     * Find the data to use for this test
     */

    CCSIDTestData testData = null;
    for (int i = 0; (testData == null) && (i < ccsidTestData.length); i++) {
      if (ccsidTestData[i].ccsid == ccsid) {
        testData = ccsidTestData[i];
      }
    }
    if (testData == null) {
      failed("CCSID " + ccsid + " not valid for test -- " + failedMessage);
      return false;
    } else {
      if (ccsid > 610000)
        ccsid = ccsid - 610000;
      try {
        // Check to see if something bad had close the connection.. If so
        // reopen it.
        if (connection_.isClosed()) {
          connection_ = null;
          resetConnection();

        }
        Statement stmt = connection_.createStatement();
        /*
         * try { stmt.executeUpdate("DROP TABLE "+tablename); } catch (Exception
         * e) { }
         */
        stmt.executeUpdate("CREATE OR REPLACE TABLE " + tablename
            + " ( c1 varchar(800) CCSID " + ccsid + ")");
        stmt.executeUpdate("DELETE FROM "  + tablename);
        PreparedStatement pstmt = connection_
            .prepareStatement("insert into " + tablename + " values (?)");
        pstmt.setString(1, testData.data);
        pstmt.executeUpdate();

        ResultSet rs = stmt.executeQuery(
            "SELECT c1, cast(c1 as VARGRAPHIC(1600) CCSID 1200) FROM "
                + tablename);
        rs.next();
        String value = rs.getString(1);
        String value1200 = rs.getString(2);
        stmt.executeUpdate("DROP TABLE " + tablename);

        stmt.close();
        pstmt.close();

        if (testData.data.equals(value)) {
          return true;
        } else {
          if (value1200.equals(value)) {
            failed("Invalid data returned (but matches 1200) for CCSID:" + ccsid
                + " -- <expected>!=<retrieved> "
                + getDifferences(testData.data, value) + " -- "
                + failedMessage);
            return false;

          } else {
            failed("Invalid data returned for CCSID:" + ccsid
                + " -- <expected>!=<retrieved> "
                + getDifferences(testData.data, value) + " -- "
                + failedMessage);
            return false;
          }
        }

      } catch (Exception e) {
        failed(e, "Exception found for CCSID:" + ccsid + "  " + failedMessage);
        return false;
      }
    }
  }

  /**
   * Runs different tests using the specified CCSID. The goal is to verify that
   * data in that CCSID can be inserted and retrieved. Written by the native
   * driver after determining that CCSID 62235 has a problem in V5R3..
   */

  private boolean stressCCSIDParm(int ccsid, String failedMessage) {
    String procedurename = ccsidBase_ + ccsid;

    /*
     * Find the data to use for this test
     */

    CCSIDTestData testData = null;
    for (int i = 0; (testData == null) && (i < ccsidTestData.length); i++) {
      if (ccsidTestData[i].ccsid == ccsid) {
        testData = ccsidTestData[i];
      }
    }
    if (testData == null) {
      failed("CCSID " + ccsid + " not valid for test -- " + failedMessage);
      return false;
    } else {
      if (ccsid > 610000)
        ccsid = ccsid - 610000;
      try {
        // Check to see if something bad had close the connection.. If so
        // reopen it.
        if (connection_.isClosed()) {
          connection_ = null;
          resetConnection();

        }

        //
        // Create the stroed procedureto use
        Statement stmt = connection_.createStatement();
        try {
          stmt.executeUpdate("DROP PROCEDURE " + procedurename);
        } catch (Exception e) {
        }
        stmt.executeUpdate(
            "CREATE PROCEDURE " + procedurename + " ( in p1 varchar(800) CCSID "
                + ccsid + ",out p2 varchar(800) CCSID " + ccsid + ") "
                + "language sql begin set p2=p1; end");
        CallableStatement cstmt = connection_
            .prepareCall("CALL " + procedurename + " (?, ?)");
        cstmt.setString(1, testData.data);
        cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
        cstmt.executeUpdate();

        String value = cstmt.getString(2);
        stmt.executeUpdate("DROP PROCEDURE " + procedurename);

        stmt.close();
        cstmt.close();

        if (testData.data.equals(value)) {
          return true;
        } else {

          failed("Invalid data returned for CCSID:" + ccsid + " -- "
              + getDifferences(testData.data, value) + " -- " + failedMessage);
          return false;
        }

      } catch (Exception e) {
        failed(e, "Exception found for CCSID:" + ccsid + "  " + failedMessage);
        return false;
      }
    }
  }

  /**
   * basic multithreaded case with Update only and different connections
   **/
  public void Var201() {
    notApplicable("Moved to JDStatementStressTest201");
  }

  /**
   * basic multithreaded case with Select only and different connections
   **/
  public void Var202() {
    notApplicable("Moved to JDStatementStressTest202");
  }

  int getStoragePoolSize() {
    int size = -1;
    try {
      Class<?> c = Class.forName("com.ibm.as400.access.DBDSPool");
      // lookup static variable storagePool_
      Field storagePoolField = c.getDeclaredField("storagePool_");
      storagePoolField.setAccessible(true);
      Object storagePool = storagePoolField.get(null);

      Class<?> dbStoragePoolClass = storagePool.getClass();
      Field poolField = dbStoragePoolClass.getDeclaredField("pool_");
      poolField.setAccessible(true);
      Object[] pool = (Object[]) poolField.get(storagePool);

      size = pool.length;
    } catch (Exception e2) {
      e2.printStackTrace(System.out);
    }
    return size;
  }

  int getDBReplyRequestedDSPoolSize() {
    int size = -1;
    try {
      Class<?> c = Class.forName("com.ibm.as400.access.DBDSPool");
      // lookup static variable storagePool_
      Field storagePoolField = c.getDeclaredField("dbreplyrequesteddsPool_");
      storagePoolField.setAccessible(true);
      Object[] storagePool = (Object[]) storagePoolField.get(null);

      size = storagePool.length;
    } catch (Exception e2) {
      e2.printStackTrace(System.out);
    }
    return size;

  }

  /*
   * Testcase to verify that DB storage objects are not leaked Also look at
   * local performance numbers
   */

  public void Var203() {

    if (isProxy()) {
      notApplicable("Don't test DBstorage objects for proxy");
      return;
    }

    int STATEMENTS_TO_TEST = 10000;

    if ((!isToolboxDriver())) {
      notApplicable();
    } else {
      boolean passed = true;
      StringBuffer sb = new StringBuffer();
      sb.append(" -- Added 12/30/2010 for CPS 8CJUL6\n");
      sb.append(
          "Testing that DBStorage object are not leaked for toolbox driver\n");
      try {
        boolean on400 = JTOpenTestEnvironment.isOS400; 
        String jobName;
        int collectionLength = collection_.length();
        String pexKey = "JDSSTZ"
            + collection_.substring(collectionLength - 4, collectionLength);
        PreparedStatement pexStmt = null;
        if (on400) {
          jobName = JDJobName.getJobName();
          sb.append("Job name is " + jobName);
          //
          // Note: Do not create this procedure in QSYS because
          // the default is "MODIFIES SQL DATA". This will
          // cause the SYSIBM.SQLCOLPRIV view to fail.
          // See issue 45473 for more information.
          //
          Statement s = connection_.createStatement();
          String sql = "DROP PROCEDURE " + collection_ + ".QCMDEXC";
          sb.append("SQL=" + sql + "\n");
          try {
            s.executeUpdate(sql);

          } catch (Exception e) {
          }
          sql = "CREATE PROCEDURE " + collection_
              + ".QCMDEXC(IN CMDSTR VARCHAR(1024),IN CMDLENGTH DECIMAL(15,5)) EXTERNAL NAME QSYS.QCMDEXC LANGUAGE C GENERAL ";
          sb.append("SQL=" + sql + "\n");
          try {
            s.executeUpdate(sql);
          } catch (Exception e) {
            sb.append("Exception:" + e + "\n");
          }
          pexStmt = connection_
              .prepareStatement("CALL " + collection_ + ".QCMDEXC(?,?)");
          executeCommand(pexStmt, "QSYS/RMVPEXDFN DFN(" + pexKey + ")", sb);
          executeCommand(pexStmt, "QSYS/ADDPEXDFN DFN(" + pexKey
              + ") TYPE(*TRACE) JOB((" + jobName
              + ")) MAXSTG(100000) INTERVAL(1) TRCTYPE(*SLTEVT) SLTEVT(*YES) MCHINST(*NONE) BASEVT((*PMCO *NONE *FORMAT2)) ",
              sb);
          executeCommand(pexStmt,
              "QSYS/STRPEX SSNID(" + pexKey + ") DFN(" + pexKey + ")", sb);
          executeCommand(pexStmt, "QSYS/CRTLIB " + pexKey, sb);
        }

        sb.append("Opening toolbox connection\n");

        sb.append("Looking at storage pool size\n");
        int beginPoolSize = getStoragePoolSize();
        sb.append("begin Pool size = " + beginPoolSize + "\n");
        sb.append(
            "Allocating and using " + STATEMENTS_TO_TEST + " statements\n");
        Statement[] stmt = new Statement[STATEMENTS_TO_TEST];
        int STATEMENTS_PER_CONNECTION = 500;
        Connection[] conn = new Connection[1
            + stmt.length / STATEMENTS_PER_CONNECTION];
        long lastPrintTime = System.currentTimeMillis();
        for (int i = 0; i < stmt.length; i++) {
          if ((i % 500) == 0) {
            long printTime = System.currentTimeMillis();
            int storagePoolSize = -1;
            int replyRequestedPoolSize = -1;
            try {
              storagePoolSize = getStoragePoolSize();
              replyRequestedPoolSize = getDBReplyRequestedDSPoolSize();
            } catch (Exception e) {
            }
            System.out.println(i + " of " + stmt.length
                + " storage pool size = " + storagePoolSize
                + " replyrequeested pool size = " + +replyRequestedPoolSize
                + " time=" + (printTime - lastPrintTime));

            lastPrintTime = printTime;
          }
          Connection c = conn[i / STATEMENTS_PER_CONNECTION];
          if (c == null) {
            c = testDriver_.getConnection(baseURL_, systemObject_.getUserId(),
                encryptedPassword_);
            conn[i % STATEMENTS_PER_CONNECTION] = c;
          }
          stmt[i] = c.createStatement();
          ResultSet rs = stmt[i].executeQuery("Select * from sysibm.sysdummy1");
          rs.close();
        }
        int afterAllocPoolSize = getStoragePoolSize();
        sb.append("afterAllocPool size = " + afterAllocPoolSize + "\n");

        sb.append(
            "Now allocating and freeing statements.  Allocated size should not grow\n");
        lastPrintTime = System.currentTimeMillis();
        for (int i = 0; i < stmt.length; i++) {
          if ((i % 500) == 0) {
            long printTime = System.currentTimeMillis();
            int storagePoolSize = -1;
            int replyRequestedPoolSize = -1;
            try {
              storagePoolSize = getStoragePoolSize();
              replyRequestedPoolSize = getDBReplyRequestedDSPoolSize();
            } catch (Exception e) {
            }
            System.out.println(i + " of " + stmt.length
                + " storage pool size = " + storagePoolSize
                + " replyrequeested pool size = " + +replyRequestedPoolSize
                + " time=" + (printTime - lastPrintTime));

            lastPrintTime = printTime;

          }
          Connection c = conn[i / STATEMENTS_PER_CONNECTION];
          if (c == null) {
            c = testDriver_.getConnection(baseURL_, systemObject_.getUserId(),
                encryptedPassword_);
            conn[i % STATEMENTS_PER_CONNECTION] = c;
          }
          Statement s = c.createStatement();
          ResultSet rs = s.executeQuery("Select * from sysibm.sysdummy1");
          rs.close();
          s.close();

        }
        int afterRunPoolSize = getStoragePoolSize();
        sb.append("afterRunPool size = " + afterRunPoolSize + "\n");

        passed = (afterRunPoolSize < 2 * beginPoolSize);
        if (!passed)
          sb.append("FAILED:afterRunPoolsize >= 2 * beginPoolSize");
        if (on400) {
          executeCommand(pexStmt, "ENDPEX  SSNID(" + pexKey + ") DTALIB("
              + pexKey + ")  RPLDTA(*YES) ", sb);

          executeCommand(pexStmt,
              "QSYS/PRTPEXRPT MBR(" + pexKey + ") LIB(" + pexKey
                  + ") TYPE(*PROFILE) PROFILEOPT(*SAMPLECOUNT *PROCEDURE) ",
              sb);
          executeCommand(pexStmt, "QSYS/RMVPEXDFN DFN(" + pexKey + ")", sb);
          String sql = "select count(*) from " + pexKey + ".qaypebase";
          sb.append("Executing " + sql + "\n");
          Statement s = connection_.createStatement();
          ResultSet rs = s.executeQuery(sql);
          rs.next();
          sb.append(
              "Running query to determine if any address has more than 6% of the time\n");
          int total = rs.getInt(1);
          sql = "select * from (select   QBSIAD, count(*) as c from " + pexKey
              + ".qaypebase  group by QBSIAD )  AS X where c > " + (total / 16)
              + " order by c";
          sb.append("Executing " + sql + "\n");
          rs = s.executeQuery(sql);
          boolean resultsFound = false;
          while (rs.next()) {
            passed = false;
            resultsFound = true;
            sb.append("FAILED: Address with more than 5% of time: "
                + rs.getString(1) + "," + rs.getString(2) + "\n");
          }

          if (resultsFound) {
            sb.append("Use  PRTPEXRPT MBR(" + pexKey + ") LIB(" + pexKey
                + ") TYPE(*PROFILE) for more details\n");

          }
        }
        assertCondition(passed, sb);
      } catch (Exception e) {
        failed(e, "Unexpected exception\n" + sb.toString());
      }
    }
  }

  void executeCommand(PreparedStatement pstmt, String command,
      StringBuffer sb) {
    try {
      sb.append(command);
      sb.append("\n");
      pstmt.setString(1, command);
      pstmt.setInt(2, command.length());
      pstmt.executeUpdate();
    } catch (Exception e) {
      sb.append("Caught Exception " + e + "\n");
    }

  }

  /**
   * Test the reuse section numbers. See issue 49054
   **/
  public void Var204() {
    String URL = baseURL_;
    String userid = systemObject_.getUserId();
    char[] encryptedPassword = encryptedPassword_; 
    
    try {
      Connection con = testDriver_.getConnection(URL, userid, encryptedPassword);
      PreparedStatement ps1 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps2 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps3 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps4 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps5 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps6 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps7 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps8 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps9 = con
          .prepareStatement("select * from sysibm.sysdummy1");
      PreparedStatement ps10 = con
          .prepareStatement("select * from sysibm.sysdummy1");

      ResultSet rs1 = ps1.executeQuery();
      ResultSet rs2 = ps2.executeQuery();
      ResultSet rs3 = ps3.executeQuery();
      ResultSet rs4 = ps4.executeQuery();
      ResultSet rs5 = ps5.executeQuery();
      ResultSet rs6 = ps6.executeQuery();
      ResultSet rs7 = ps7.executeQuery();
      ResultSet rs8 = ps8.executeQuery();
      ResultSet rs9 = ps9.executeQuery();
      ResultSet rs10 = ps10.executeQuery();

      rs1.getCursorName();
      rs2.getCursorName();
      rs3.getCursorName();
      rs4.getCursorName();
      rs5.getCursorName();
      rs6.getCursorName();
      rs7.getCursorName();
      rs8.getCursorName();
      rs9.getCursorName();
      rs10.getCursorName();

      PreparedStatement ps = null;
      for (int i = 0; i < 490; i++) {
        ps = con.prepareStatement("select * from sysibm.sysdummy1");
      }
      if (ps == null)
        throw new Exception("internal testcase error");
      ps.executeQuery();

      ps2.close();
      ps4.close();
      ps6.close();
      ps8.close();
      ps10.close();

      java.lang.System.gc();

      ps = con.prepareStatement("select * from sysibm.sysdummy1");
      ps.executeQuery();

      ps = con.prepareStatement("select * from sysibm.sysdummy1");
      ps.executeQuery();

      ps = con.prepareStatement("select * from sysibm.sysdummy1");
      ps.executeQuery();

      ps = con.prepareStatement("select * from sysibm.sysdummy1");
      ps.executeQuery();

      con.close();

      succeeded();
    } catch (Exception e) {
      failed(e, "Unexpected Exception");
    }
  }

  /* Test a CCSID. If the CCSID is not supported return true */
  /* Otherwise test that it can be converted */

  public boolean testCcsid(int ccsid, StringBuffer returnSb) {
    StringBuffer sb = new StringBuffer();
    String sql;
    try {
      boolean passed = true;
      String tableName = JDStatementTest.COLLECTION + ".JDSSTC" + ccsid;
      boolean singleByte = true;
      // These CCSIDs are currently not supported
      switch (ccsid) {
      /* 02/12/2021 added back case 1175: */ /*
                                              * Turkish EBCDIC -- download of
                                              * table fails
                                              */
      /* case 1377: */ /*
                        * Hong Kong Mixed CCSID -- unable to download base
                        * double byte -- 1377
                        */
      case 57777: /* SAP CCSID */
        System.out.println("Skipping unsupported CCSID " + ccsid);
        return true;

      }

      if (!skipValidCheck) { 
      if (!validColumnCcsid(ccsid)) { 
        return true; 
      }
      } 
      
      /*
       * try { sql = "DROP TABLE "+tableName; sb.append(sql+"\n");
       * statement_.executeUpdate(sql); } catch (Exception e) { String message =
       * e.toString(); if (message.indexOf("not found") < 0) {
       * e.printStackTrace(); } }
       */
      sql = "CREATE OR REPLACE TABLE " + tableName + " (C1 VARCHAR(3000) CCSID "
          + ccsid + ")";
      try {
        sb.append(sql + "\n");
        statement_.executeUpdate(sql);
        singleByte = true;
        sql = "DELETE FROM "  + tableName;
        sb.append(sql + "\n");
        statement_.executeUpdate(sql);

        /* create worked */
      } catch (Exception e) {
        String message = e.toString();
        if (message.indexOf("not valid") < 0) {
          e.printStackTrace(System.out);
        }
        String sql2 = null;
        try {
          sql2 = "CREATE OR REPLACE TABLE " + tableName
              + " (C1 VARGRAPHIC(3000) CCSID " + ccsid + ")";
          sb.append(sql2 + "\n");
          statement_.executeUpdate(sql2);
          singleByte = false;
          sql = "DELETE FROM "  + tableName;
          sb.append(sql + "\n");
          statement_.executeUpdate(sql);

        } catch (Exception e1) {
          String message1 = e1.toString();
          if (message1.indexOf("not valid") < 0) {
            System.out.println("Unable to create table using [" + sql
                + "] .... [" + sql2 + "]");
            e1.printStackTrace(System.out);
            return false;
          } else {
            return true;
          }
        }

      }

      System.out.print("Checking valid CCSID " + ccsid + "...\n");
      System.out.flush();

      String simpleExpected = "ABCDEFG";
      String insertValue = "'ABCDEFG'";
      switch (ccsid) {
      case 300:
      case 834:
      case 835:
      case 837:
      case 4396:
      case 4930:
      case 4933:
      case 16684:
        simpleExpected = "\uFF21\uFF22\uFF23\uFF24\uFF25\uFF26\uFF27";
        insertValue = "UX'FF21FF22FF23FF24FF25FF26FF27'";
        break;

      }
      sql = "INSERT INTO " + tableName + " VALUES(" + insertValue + ")";
      sb.append(sql + "\n");
      statement_.executeUpdate(sql);

      sql = "SELECT C1 FROM " + tableName;
      sb.append(sql + "\n");
      ResultSet rs = statement_.executeQuery(sql);
      rs.next();
      String s = rs.getString(1);
      if (!simpleExpected.equals(s)) {
        passed = false;
        sb.append("For CCSID " + ccsid + "\n got '" + s + "'("
            + showStringAsHex(s) + ") " + "\n sb  '" + simpleExpected + "' ("
            + showStringAsHex(simpleExpected) + ")\n");
      }

      /* Do not test these CCSIDs for the large test */
      if ((ccsid != 420) && (ccsid != 424) && (ccsid != 918) && (ccsid != 1097)
          && (ccsid != 8612) && (ccsid != 12708) && (ccsid != 62211)
          && (ccsid != 62224) && (ccsid != 62235) && (ccsid != 62245)
          && (ccsid != 62251) && singleByte) {
        sql = "DELETE FROM " + tableName;
        sb.append(sql + "\n");
        statement_.executeUpdate(sql);

        if (ccsid != 1208) {
          sql = "CALL QSYS2.QCMDEXC('CHGJOB CCSID(" + ccsid + ")')";
          sb.append(sql + "\n");
          try {
            statement_.executeUpdate(sql);
          } catch (Exception e) {
            sb.append(" Warning:  CHGJOB failed\n");
            printStackTraceToStringBuffer(e, sb);
          }

        }

        sql = "INSERT INTO " + tableName + " VALUES(CAST(X'"
            + "404142434445464748494a4b4c4d4e4f"
            + "505152535455565758595a5b5c5d5e5f"
            + "606162636465666768696a6b6c6d6e6f"
            + "707172737475767778797a7b7c7d7e7f"
            + "808182838485868788898a8b8c8d8e8f"
            + "909192939495969798999a9b9c9d9e9f"
            + "a0a1a2a3a4a5a6a7a8a9aaabacadaeaf"
            + "b0b1b2b3b4b5b6b7b8b9babbbcbdbebf"
            + "c0c1c2c3c4c5c6c7c8c9cacbcccdcecf"
            + "d0d1d2d3d4d5d6d7d8d9dadbdcdddedf"
            + "e0e1e2e3e4e5e6e7e8e9eaebecedeeef"
            + "f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff" + "' AS VARCHAR(3000) CCSID "
            + ccsid + "))";
        sb.append(sql + "\n");
        statement_.executeUpdate(sql);

        sql = "SELECT C1, CAST(C1 AS VARGRAPHIC(3000) CCSID 1200) FROM "
            + tableName;
        sb.append(sql + "\n");
        ResultSet rs2 = statement_.executeQuery(sql);
        rs2.next();
        String s1 = rs2.getString(1);
        String s2 = rs2.getString(2);

        if (!s1.equals(s2)) {
          // Check to see if replacing substitution character matches
          String s3 = s1.replace('\ufffd', '\u001a');
          String s4 = s2.replace('\ufffd', '\u001a');
          if (!s3.equals(s4)) {
            passed = false;
            sb.append("Retrieved hex strings from "+tableName+" did not match\n");
            sb.append("Retrieved raw  = " + showStringAsHex(s1) + "\n");
            sb.append("Retrieved 1200 = " + showStringAsHex(s2) + "\n");
            showDifferences(s3, s4, sb);
          }
        }
        
        // Check the round trip by updating the row. 
        sql = "UPDATE " + tableName + " SET C1 = ?";
        PreparedStatement ps = connection_.prepareStatement(sql); 
        ps.setString(1, s2);
        ps.executeUpdate(); 
        
        sql = "SELECT CAST(C1 AS VARGRAPHIC(3000) CCSID 1200) FROM "
            + tableName;
        sb.append(sql + "\n");
        ResultSet rs3 = statement_.executeQuery(sql);
        rs3.next();
        String s5 = rs3.getString(1);
        if (!s5.equals(s2)) {
          // Check to see if replacing substitution character matches
          String s6 = s5.replace('\ufffd', '\u001a');
          String s4 = s2.replace('\ufffd', '\u001a');
          if (!s6.equals(s4)) {
            passed = false;
            sb.append("Retrieved values from insert of "+tableName+" did not match\n");
            sb.append("Retrieved inserted = " + showStringAsHex(s5) + "\n");
            sb.append("Original  1200     = " + showStringAsHex(s2) + "\n");
            showDifferences(s6, s4, sb);
          }
        }

        if (ConvTable.isMixedCCSID(ccsid) && (ccsid != 1208)) {
          System.out.println("..Testing all mixed CCSID combinations"); 
          // Test all the mixed combinations.
          StringBuffer sqlBuffer = new StringBuffer();
          char[] hexDigits = new char[4];
          for (int prefix = 1; prefix < 0x100; prefix++) {
            if ((prefix != 0x0E) && (prefix != 0x0F)) {
              sql = "DELETE FROM " + tableName;
              sb.append(sql + "\n");
              statement_.executeUpdate(sql);

              sqlBuffer.setLength(0);
              sqlBuffer.append("INSERT INTO " + tableName + " VALUES(CAST(X'");
              for (int suffix = 0x00; suffix < 0x100; suffix++) {
                if (suffix == 0x0e || suffix == 0x0f) { 
                  // skip 
                } else {
                  hexDigits[0] = hexDigit[((prefix / 16) % 16)];
                  hexDigits[1] = hexDigit[(prefix % 16)];
                  hexDigits[2] = hexDigit[((suffix / 16) % 16)];
                  hexDigits[3] = hexDigit[(suffix % 16)];
                  appendEbcdicHex(sqlBuffer, hexDigits[0]);
                  appendEbcdicHex(sqlBuffer, hexDigits[1]);
                  appendEbcdicHex(sqlBuffer, hexDigits[2]);
                  appendEbcdicHex(sqlBuffer, hexDigits[3]);
                  sqlBuffer.append("7E");
                  sqlBuffer.append("0E");
                  sqlBuffer.append(hexDigits);
                  sqlBuffer.append("0F");
                  sqlBuffer.append("40");
                }
              }
              sqlBuffer.append("' AS VARCHAR(3000) CCSID " + ccsid + "))");
              sql = sqlBuffer.toString();
              sb.append(sql + "\n");
              statement_.executeUpdate(sql);

              sql = "SELECT C1, CAST(C1 AS VARGRAPHIC(3000) CCSID 1200) FROM "
                  + tableName;
              sb.append(sql + "\n");
              rs2 = statement_.executeQuery(sql);
              if (rs2.next()) {
                s1 = rs2.getString(1);
                s2 = rs2.getString(2);

                if (!s1.equals(s2)) {
                  // Check to see if replacing substitution character matches
                  String s3 = s1.replace('\ufffd', '\u001a');
                  String s4 = s2.replace('\ufffd', '\u001a');
                  if (!s3.equals(s4)) {
                    passed = false;
                    sb.append("Retrieved hex strings from "+tableName+" did not match\n");
                    sb.append("Retrieved raw  = " + showStringAsHex(s1) + "\n");
                    sb.append("Retrieved 1200 = " + showStringAsHex(s2) + "\n");
                    showDifferences(s3, s4, sb);
                  }
                }
              } else {
                passed = false;
                sb.append("Able to fetch row\n");
              }

              // Check the roundtrip by updating the row
              sql = "UPDATE " + tableName + " SET C1 = ?";

              sql = "Insert"; 
              ps.setString(1, s2);
              ps.executeUpdate(); 
              
              sql = "SELECT CAST(C1 AS VARGRAPHIC(3000) CCSID 1200) FROM "
                  + tableName;
              sb.append(sql + "\n");
              rs3 = statement_.executeQuery(sql);
              rs3.next();
              s5 = rs3.getString(1);
              if (!s5.equals(s2)) {
                // Check to see if replacing substitution character matches
                String s6 = s5.replace('\ufffd', '\u001a');
                String s4 = s2.replace('\ufffd', '\u001a');
                if (!s6.equals(s4)) {
                  passed = false;
                  sb.append("Retrieved values from insert into "+tableName+" did not match\n");
                  sb.append("Retrieved inserted = " + showStringAsHex(s5) + "\n");
                  sb.append("Original  1200     = " + showStringAsHex(s2) + "\n");
                  showDifferences(s6, s4, sb);
                }
              }

              
              
            }
          }
        }

      }

      sql = "DROP TABLE " + tableName;
      sb.append(sql + "\n");
      statement_.executeUpdate(sql);

      if (ccsid == 5473 || ccsid == 13676) {
        // Changing back to ccsid 37 fails for ccsid 5473 .. Just get a new
        // connection
        // Put the connection in never never land
        badConnections.add(connection_);
        connection_ = null;

        resetConnection();

      } else {
        sql = "CALL QSYS2.QCMDEXC('CHGJOB CCSID(37)')";
        sb.append(sql + "\n");
        statement_.executeUpdate(sql);
      }

      System.out.println("..Done\n");

      if (!passed) {
        System.out.println("CCSID " + ccsid + " Failed\n");
        System.out.println(sb.toString() + "\n");
        returnSb.append(sb);
      }
      return passed;
    } catch (Exception e) {
      sb.append("Unexpected error on ccsid " + ccsid + "\n");
      printStackTraceToStringBuffer(e, sb);

      System.out.println("CCSID " + ccsid + " Failed\n");
      System.out.println(sb.toString() + "\n");

      returnSb.append(sb);
      return false;
    }
  }

  char[] validCcsids = null; 
  
  // Code to reduce the number of calls need to determine if a CCSID is valid. 
  private boolean validColumnCcsid(int ccsid) throws SQLException {
    if (validCcsids == null) {
      Statement s = connection_.createStatement();
      try { 
	  s.executeUpdate("CREATE TYPE  "+collection_+".CHARARRAY AS CHAR(1) ARRAY[30000]");
      } catch (SQLException sqlex) {
	  // Just ignore
      } 
      s.executeUpdate("CREATE OR REPLACE PROCEDURE "+collection_+".VALIDCCSIDS "+
          " ( STARTCCSID INT, ENDCCSID INT, OUT  RESULTS "+collection_+".CHARARRAY) " +
          "LANGUAGE SQL " +
          "BEGIN "+
          "  DECLARE I INTEGER; "+ 
          "  DECLARE SQLTEXT VARCHAR(1000); " +
          "  DECLARE CONTINUE HANDLER FOR SQLEXCEPTION " +
          "        SET RESULTS[I]='0'; " +
          "  SET I = 1; " +
          "  WHILE STARTCCSID <= ENDCCSID DO " +
          "     SET SQLTEXT = 'CREATE TABLE QGPL.TESTCCSID (C1 CHAR(100) CCSID ' CONCAT " +
          "         STARTCCSID CONCAT ')'; " +
          "     SET RESULTS[I]='C'; " +
          "     PREPARE STMT FROM SQLTEXT; " +
          "     IF  RESULTS[I]='0' THEN  " +
          "       SET SQLTEXT = 'CREATE TABLE QGPL.TESTCCSID (C1 GRAPHIC(100) CCSID ' CONCAT " +
          "         STARTCCSID CONCAT ')'; " +
          "       SET RESULTS[I]='G'; " +
          "       PREPARE STMT FROM SQLTEXT; " +
          "     END IF; " +
          "     SET I = I + 1; " + 
          "     SET STARTCCSID = STARTCCSID +1; " + 
          "  END WHILE; " +
          "END" ); 

          
      CallableStatement cs = connection_.prepareCall("CALL "+collection_+".VALIDCCSIDS(?,?,?)"); 
      cs.registerOutParameter(3, Types.ARRAY);
      char[] newValidCcsids = new char[70001]; 
      for (int startCcsid = 1; startCcsid < 70000; startCcsid += 10000) { 
        cs.setInt(1, startCcsid);
        cs.setInt(2, startCcsid + 9999);
        cs.execute(); 
        Array sqlarrayout = cs.getArray(3);
        Object arrayout = sqlarrayout.getArray();
        if (arrayout instanceof String[]) { 
          String[] outStrings = (String[]) arrayout;
          
          for (int i = 0; i < outStrings.length; i++) { 
            newValidCcsids[startCcsid+i] = outStrings[i].charAt(0); 
          }
        }
      }
      
      validCcsids = newValidCcsids; 
      
    }
    
    if (validCcsids[ccsid]=='C' || validCcsids[ccsid]=='G' ) { 
      return true; 
    } else { 
      return false; 
    }
  }

  private void appendEbcdicHex(StringBuffer sb, char c) throws Exception {
    switch (c) {
    case '0':
      sb.append("F0");
      break;
    case '1':
      sb.append("F1");
      break;
    case '2':
      sb.append("F2");
      break;
    case '3':
      sb.append("F3");
      break;
    case '4':
      sb.append("F4");
      break;
    case '5':
      sb.append("F5");
      break;
    case '6':
      sb.append("F6");
      break;
    case '7':
      sb.append("F7");
      break;
    case '8':
      sb.append("F8");
      break;
    case '9':
      sb.append("F9");
      break;
    case 'A':
      sb.append("C1");
      break;
    case 'B':
      sb.append("C2");
      break;
    case 'C':
      sb.append("C3");
      break;
    case 'D':
      sb.append("C4");
      break;
    case 'E':
      sb.append("C5");
      break;
    case 'F':
      sb.append("C6");
      break;
    default:
      throw new Exception("invalid char : " + c);
    }

  }

  static final int CHARS_PER_LINE = 16;

  private void showDifferences(String s1, String s2, StringBuffer sb) {

    int minLength = s1.length();
    if (s2.length() < minLength)
      minLength = s2.length();
    int beginHex = 0x40;
    for (int i = 0; i < minLength; i += CHARS_PER_LINE) {
      int endIndex = i + CHARS_PER_LINE;
      if (endIndex > minLength)
        endIndex = minLength;
      appendHeader(sb, beginHex, CHARS_PER_LINE);
      sb.append("-----------------------------------------\n");
      sb.append(showStringAsHex(s1.substring(i, endIndex)) + "\n");
      for (int j = i; j < endIndex; j++) {
        if (s1.charAt(j) == s2.charAt(j)) {
          sb.append("   .");
        } else {
          sb.append("   X");
        }
      }
      sb.append("\n");
      sb.append(showStringAsHex(s2.substring(i, endIndex)) + "\n");
      sb.append("=========================================\n");
      beginHex = beginHex + CHARS_PER_LINE;
    }

  }

  public void appendHeader(StringBuffer sb, int beginIndex, int chars) {
    for (int i = 0; i < chars; i++) {
      int b = beginIndex + i;
      sb.append("00");
      sb.append(Integer.toHexString(b));
    }
    sb.append("\n");
  }

  /**
   * Test all the ccsids allowed on the system. Make sure we can translate from
   * them.
   **/
  public void Var205() {     notApplicable("Moved to JDStatementStressTest205");   }
  public void Var206() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var207() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var208() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var209() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var210() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var211() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var212() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var213() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var214() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var215() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var216() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var217() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var218() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var219() {     notApplicable("Moved to JDStatementStressTest206");   }
  public void Var220() {     notApplicable("Moved to JDStatementStressTest206");   }




  protected boolean skipCcsid(int ccsid) {
    if (getDriver() == JDTestDriver.DRIVER_NATIVE) {
      if (getRelease() >= JDTestDriver.RELEASE_V7R2M0
          && getRelease() < JDTestDriver.RELEASE_V7R4M0) {
        // The following CCSIDs are not supported by PASE
        switch (ccsid) {
        case 1047:
        case 1166:
        case 1371:
        case 5233:
        case 57777:
	case 61175:  /* Not supported -- issue 66267 */ 
        case 62211: /* Crashes JVM */
        case 62224: /* Crashes JVM */
        case 62235: /* Crashes JVM */
        case 62245: /* Crashes JVM */
        case 62251: /* Crashes JVM */

          return true;
        }
      }
    }
    return false;
  }

  /* New test for CCSID 300 and surrogate pairs */
  /*
   * B342 0002000B B346 00020089 B348 000200A2 B349 000200A4 B34E 000201A2 B353
   * 00020213 B35D 0002032B B360 00020371 B364 00020381 B367 000203F9 B368
   * 0002044A B36B 00020509 B370 000205D6 B373 00020628 B376 0002074F B379
   * 00020807 B37A 0002083A B380 000208B9 B382 0002097C B384 0002099D B388
   * 00020AD3 B389 00020B1D B38D 00020B9F B39B 00020D45 B39F 00020DE1 B3A2
   * 00020E64 B3A3 00020E6D B3A4 00020E95 B3A8 00020F5F B3B0 00021201 B3B1
   * 0002123D B3B3 00021255 B3B4 00021274 B3B5 0002127B B3B6 000212D7 B3B7
   * 000212E4 B3B8 000212FD B3BA 0002131B B3BD 00021336 B3BE 00021344 B3C0
   * 000213C4 B3C5 0002146D B3C6 0002146E B3CA 000215D7 B3D0 00021647 B3D1
   * 000216B4 B3D2 00021706 B3D6 00021742 B3D9 000218BD B3DA 000219C3 B3E4
   * 00021C56 B3E9 00021D2D B3EA 00021D45 B3EB 00021D62 B3EC 00021D78 B3EF
   * 00021D92 B3F0 00021D9C B3F2 00021DA1 B3F4 00021DB7 B3F5 00021DE0 B3F9
   * 00021E33 B3FA 00021E34 B444 00021F1E B446 00021F76 B449 00021FFA B450
   * 0002217B B452 00022218 B456 0002231E B45A 000223AD B462 000226F3 B468
   * 0002285B B469 000228AB B46C 0002298F B472 00022AB8 B475 00022B46 B477
   * 00022B4F B478 00022B50 B479 00022BA6 B47C 00022C1D B47D 00022C24 B480
   * 00022DE1 B491 000231B6 B493 000231C3 B494 000231C4 B496 000231F5 B49F
   * 00023372 B4A1 000233D0 B4A2 000233D2 B4A3 000233D3 B4A4 000233D5 B4A5
   * 000233DA B4A7 000233DF B4A8 000233E4 B4AF 0002344A B4B0 0002344B B4B1
   * 00023451 B4B2 00023465 B4BF 000234E4 B4C4 0002355A B4C5 00023594 B4CB
   * 000235C4 B4D2 00023638 B4D3 00023639 B4D4 0002363A B4D5 00023647 B4DC
   * 0002370C B4DD 0002371C B4DE 0002373F B4DF 00023763 B4E0 00023764 B4E3
   * 000237E7 B4E5 000237FF B4E7 00023824 B4E8 0002383D B4F0 00023A98 B4F6
   * 00023C7F B543 00023CFE B544 00023D00 B545 00023D0E B54F 00023D40 B555
   * 00023DD3 B557 00023DF9 B558 00023DFA B566 00023F7E B56C 00024096 B56E
   * 00024103 B573 000241C6 B574 000241FE B57F 000243BC B584 00024629 B586
   * 000246A5 B58E 000247F1 B591 00024896 B59A 00024A4D B59E 00024B56 B59F
   * 00024B6F B5A0 00024C16 B5A4 00024D14 B5AE 00024E0E B5B0 00024E37 B5B2
   * 00024E6A B5B4 00024E8B B5B9 0002504A B5BA 00025055 B5BB 00025122 B5BD
   * 000251A9 B5BE 000251CD B5C0 000251E5 B5C2 0002521E B5C4 0002524C B5C8
   * 0002542E B5CD 0002548E B5CF 000254D9 B5D0 0002550E B5D3 000255A7 B5E1
   * 00025771 B5E3 000257A9 B5E4 000257B4 B5E9 000259C4 B5EA 000259D4 B5EE
   * 00025AE3 B5EF 00025AE4 B5F0 00025AF1 B5F9 00025BB2 B5FC 00025C4B B5FD
   * 00025C64 B644 00025DA1 B647 00025E2E B648 00025E56 B649 00025E62 B64A
   * 00025E65 B64B 00025EC2 B64C 00025ED8 B64D 00025EE8 B64E 00025F23 B64F
   * 00025F5C B651 00025FD4 B652 00025FE0 B654 00025FFB B656 0002600C B657
   * 00026017 B659 00026060 B65E 000260ED B663 00026270 B664 00026286 B666
   * 0002634C B668 00026402 B672 0002667E B675 000266B0 B67B 0002671D B681
   * 000268DD B682 000268EA B685 00026951 B686 0002696F B689 000269DD B68A
   * 00026A1E B68E 00026A58 B691 00026A8C B692 00026AB7 B697 00026AFF B69E
   * 00026C29 B6A1 00026C73 B6A4 00026CDD B6AB 00026E40 B6AC 00026E65 B6B0
   * 00026F94 B6B1 00026FF6 B6B2 00026FF7 B6B3 00026FF8 B6B7 000270F4 B6B8
   * 0002710D B6BA 00027139 B6C5 000273DA B6C6 000273DB B6C7 000273FE B6C9
   * 00027410 B6CB 00027449 B6D1 00027614 B6D2 00027615 B6D4 00027631 B6D6
   * 00027684 B6D7 00027693 B6DA 0002770E B6DC 00027723 B6DD 00027752 B6E4
   * 00027985 B6E7 00027A84 B6EE 00027BB3 B6EF 00027BBE B6F0 00027BC7 B6F1
   * 00027CB8 B6F4 00027DA0 B6F7 00027E10 B6F9 00027FB7 B6FC 0002808A B6FD
   * 000280BB B743 00028277 B744 00028282 B747 000282F3 B74A 000283CD B74B
   * 0002840C B74E 00028455 B750 0002856B B751 000285C8 B752 000285C9 B754
   * 000286D7 B756 000286FA B759 00028946 B75A 00028949 B75D 0002896B B75E
   * 00028987 B75F 00028988 B760 000289BA B761 000289BB B764 00028A1E B765
   * 00028A29 B768 00028A43 B769 00028A71 B76A 00028A99 B76B 00028ACD B76C
   * 00028ADD B76D 00028AE4 B770 00028BC1 B771 00028BEF B773 00028D10 B774
   * 00028D71 B776 00028DFB B778 00028E1F B779 00028E36 B77A 00028E89 B77B
   * 00028EEB B77D 00028F32 B780 00028FF8 B787 000292A0 B788 000292B1 B78C
   * 00029490 B78F 000295CF B793 0002967F B798 000296F0 B799 00029719 B79A
   * 00029750 B79C 000298C6 B7A7 00029A72 B7AC 00029DDB B7AE 00029E15 B7AF
   * 00029E3D B7B0 00029E49 B7B2 00029E8A B7B3 00029EC4 B7B4 00029EDB B7B5
   * 00029EE9 B7B9 00029FCE B7BB 0002A01A B7BD 0002A02F B7BF 0002A082 B7C2
   * 0002A0F9 B7C6 0002A190 B7C9 0002A38C B7CC 0002A437 B7CE 0002A5F1 B7CF
   * 0002A602 B7D0 0002A61A B7D1 0002A6B2 ECC3 000000E6+00000300 ECC4
   * 00000254+00000300 ECC5 00000254+00000301 ECC8 00000259+00000300 ECC9
   * 00000259+00000301 ECCA 0000025A+00000300 ECCB 0000025A+00000301 ECC6
   * 0000028C+00000300 ECC7 0000028C+00000301 ECCD 000002E5+000002E9 ECCC
   * 000002E9+000002E5 ECB5 0000304B+0000309A ECB6 0000304D+0000309A ECB7
   * 0000304F+0000309A ECB8 00003051+0000309A ECB9 00003053+0000309A ECBA
   * 000030AB+0000309A ECBB 000030AD+0000309A ECBC 000030AF+0000309A ECBD
   * 000030B1+0000309A ECBE 000030B3+0000309A ECBF 000030BB+0000309A ECC0
   * 000030C4+0000309A ECC1 000030C8+0000309A ECC2 000031F7+0000309A
   */

}
