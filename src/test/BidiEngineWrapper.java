//Bidi-HCG 
//This class allows testing applications to exploit Bidi engine shipped with the JT400 toolbox.
package test;

import java.sql.SQLException;

import com.ibm.as400.access.*;

public class BidiEngineWrapper {

	private static String LRO = "\u202D";
	private static String RLO = "\u202E";

	private static String LRE = "\u202A";
	private static String RLE = "\u202B";
	private static String PDF = "\u202C";

    private static final int ST1 = 1;
    private static final int ST2 = 2;
    private static final int ST3 = 3;
    private static final int ST4 = 4;
    private static final int ST5 = 5;
    private static final int ST6 = 6;
    private static final int ST7 = 7;
    private static final int ST8 = 8;
    private static final int ST9 = 9;
    private static final int ST10 = 10;
    private static final int ST11 = 11;
    private static final int ST12 = 12;
    private static final int ST13 = 13;
    private static final int ST14 = 14;
    
    //static BidiTransform bdx = new BidiTransform();
    
    public static String bidiTransform(String str, int inFormat, int outFormat){    	
    	if(inFormat == outFormat)
    		return str;
    	if(inFormat == BidiStringType.NONE || outFormat == BidiStringType.NONE)
    		return str;
    	
    	BidiTransform bdx = new BidiTransform();
    	BidiFlagSet flagsIn = initFlagSet(inFormat);
    	BidiFlagSet flagsOut = initFlagSet(outFormat);
    	
    	bdx.flags = flagsOut;
    	BidiText textIn = new BidiText(flagsIn,str);
        BidiText textOut = textIn.transform(bdx);
        String strOut = textOut.toString();
               
        
        //System.out.println("my bidiTransform, outFormat=" + outFormat + "  getNumerals=" + flagsOut.getNumerals().value);
        
        return strOut;    	
    }
    
    // Return BidiFlagSet according to string type.
    public static BidiFlagSet initFlagSet(int stringType)
    {
        switch (stringType)
        {
            case ST4:
                return new BidiFlagSet(BidiFlag.TYPE_VISUAL,
                                       BidiFlag.NUMERALS_NOMINAL,
                                       BidiFlag.ORIENTATION_LTR,
                                       BidiFlag.TEXT_SHAPED,
                                       BidiFlag.SWAP_NO);
            case ST5:
                return  new BidiFlagSet(BidiFlag.TYPE_IMPLICIT,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_LTR,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_YES);
            case ST6:
                return  new BidiFlagSet(BidiFlag.TYPE_IMPLICIT,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_RTL,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_YES);
            case ST7:
                return  new BidiFlagSet(BidiFlag.TYPE_VISUAL,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_CONTEXT_LTR,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_NO);
            case ST8:
                return  new BidiFlagSet(BidiFlag.TYPE_VISUAL,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_RTL,
                                        BidiFlag.TEXT_SHAPED,
                                        BidiFlag.SWAP_NO);
            case ST9:
                return  new BidiFlagSet(BidiFlag.TYPE_VISUAL,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_RTL,
                                        BidiFlag.TEXT_SHAPED,
                                        BidiFlag.SWAP_YES);
            case ST10:
                return  new BidiFlagSet(BidiFlag.TYPE_IMPLICIT,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_CONTEXT_LTR,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_YES);
            case ST11:
                return  new BidiFlagSet(BidiFlag.TYPE_IMPLICIT,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_CONTEXT_RTL,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_YES);
            default:
                return  new BidiFlagSet(BidiFlag.TYPE_IMPLICIT,
                                        BidiFlag.NUMERALS_NOMINAL,
                                        BidiFlag.ORIENTATION_CONTEXT_LTR,
                                        BidiFlag.TEXT_NOMINAL,
                                        BidiFlag.SWAP_YES);
        }
    }    
    public static void main(String[] args) {
    	String src = "\uFEF0\uFE91\uFEAE\uFECB\u0020\u0074\u0068\u0069\u0073\u0020\u0069\u0073\u0020\u0074\u0065\u0073\u0074\u0020\uFE8E\uFEF4\uFEE7\uFE8E\uFE9B\u0020\uFEED\u0020\uFEFB\uFEED\uFE8D";
    		//"\u05d0\u05d1 ABC \u05d2\u05d3";
    //		"\u06d0\u06d1 ABC \u06d2\u06d3";
    	/*
    	String res10 = bidiTransform(src, 10, 4);
    	String res11 = bidiTransform(src, 11, 4);
    	String res5 = bidiTransform(src, 5, 4);
    	String res6 = bidiTransform(src, 6, 4);
    	*/
    	/*
    	String res10 = bidiTransform(src, 4, 10);
    	String res11 = bidiTransform(src, 4, 11);
    	String res5 = bidiTransform(src, 4, 5);
    	String res6 = bidiTransform(src, 4, 6);

    	System.out.println("res10 vs res11 " + (res10.equals(res11)? "equal" : "not equal"));
    	System.out.println("res10 vs res5 " + (res10.equals(res5)? "equal" : "not equal"));
    	System.out.println("res10 vs res6 " + (res10.equals(res6)? "equal" : "not equal"));
    	*/
    	
    	char[] buf = {0x41, '(', 0x42, ')', 0x43, 0x20, 0x5D0, '(', 0x5D1, ')', 0x5D2};
    	String str = new String(buf);
    	
    	str = "002c3B2E"; 
    	System.out.println(bidiTransform(str, 6,9));//8?
    	//System.out.println("TransFromLogRTLtoVisRTLEx \n"+ RLE + str + PDF + "\n > \n" + RLO + bidiTransform(str, 6,9) + PDF);//8?
    }
   
	public static String getProbe(String value_, AS400JDBCConnection connection, int source_ccsid_type, int target_ccsid_type, boolean use_packageCCSID) throws SQLException{
    	
		JDProperties prop = connection.getProperties();        	       	        		
    	int host_bidi_format, host_ccsid, package_bidi_format, package_ccsid;
    	int bidi_format = source_ccsid_type;
		
    	host_ccsid = connection.getSystem().getCcsid();
		host_bidi_format = AS400BidiTransform.getStringType(host_ccsid);				

		package_ccsid = connection.getProperties().getInt(JDProperties.PACKAGE_CCSID);
		package_bidi_format = AS400BidiTransform.getStringTypeX(package_ccsid, connection.getSystem());
			//AS400BidiTransform.getStringType(package_ccsid);
    	/*
    	if(bidi_format != 0){
    		 if(prop.getString(JDProperties.BIDI_IMPLICIT_REORDERING).equalsIgnoreCase("true")){				       			
    		 	value_ = AS400BidiTransform.meta_data_reordering(value_, bidi_format, host_bidi_format);        			
    		 }
    		
    		value_ = bidiTransform(value_, bidi_format, host_bidi_format);
    		if(use_packageCCSID){
    			value_ = bidiTransform(value_, host_bidi_format, package_bidi_format);
    			value_ = bidiTransform(value_, package_bidi_format, host_bidi_format);
    		}

    	}	
    	return bidiTransform(value_, host_bidi_format, target_ccsid_type);	
    	*/
		value_ = bidiTransform(value_, source_ccsid_type, package_bidi_format);	
		value_ = bidiTransform(value_, package_bidi_format, target_ccsid_type);	
		return value_;
	}    

	public static boolean checkRoundTrip(String value_, String probe, AS400JDBCConnection connection, int source_ccsid_type, int target_ccsid_type, boolean use_packageCCSID) throws SQLException{
    	JDProperties prop = connection.getProperties();        	       	        		
    	int host_bidi_format, host_ccsid, package_bidi_format, package_ccsid;
    	int bidi_format = source_ccsid_type;
		
    	host_ccsid = connection.getSystem().getCcsid();
		host_bidi_format = AS400BidiTransform.getStringType(host_ccsid);				

		package_ccsid = connection.getProperties().getInt(JDProperties.PACKAGE_CCSID);
		package_bidi_format = AS400BidiTransform.getStringType(package_ccsid);

		if(use_packageCCSID){
			value_ = bidiTransform(value_, host_bidi_format, package_bidi_format);
			probe = bidiTransform(probe, host_bidi_format, package_bidi_format);
			if(value_.equals(probe))
				return true;
			
			value_ = bidiTransform(value_, package_bidi_format, host_bidi_format);
			probe = bidiTransform(probe, package_bidi_format, host_bidi_format);
			if(value_.equals(probe))
				return true;
		}    	
		
		value_ = bidiTransform(value_, host_bidi_format, bidi_format);
		probe = bidiTransform(probe, host_bidi_format, bidi_format);
		if(value_.equals(probe))
			return true;
		
		return false;
	}

	
	public static String getProbe(String value_, AS400JDBCConnection connection, int user_ccsid, int source_ccsid_type, int target_ccsid_type) throws SQLException{
		/*
		JDProperties prop = connection.getProperties(); 
    	int host_bidi_format, package_bidi_format, package_ccsid;
    	int bidi_format = source_ccsid_type;
		    	
		host_bidi_format = AS400BidiTransform.getStringType(user_ccsid);				

		package_ccsid = connection.getProperties().getInt(JDProperties.PACKAGE_CCSID);
		package_bidi_format = AS400BidiTransform.getStringType(package_ccsid);
    	
    	if(bidi_format != 0){
    		 if(prop.getString(JDProperties.BIDI_IMPLICIT_REORDERING).equalsIgnoreCase("true")){				       			
    		 	value_ = AS400BidiTransform.meta_data_reordering(value_, bidi_format, host_bidi_format);        			
    		 }
    		
    		value_ = bidiTransform(value_, bidi_format, host_bidi_format);
    		value_ = bidiTransform(value_, host_bidi_format, package_bidi_format);
    		value_ = bidiTransform(value_, package_bidi_format, host_bidi_format);
    	}	
    	return bidiTransform(value_, host_bidi_format, target_ccsid_type);	
    	*/
		return value_;
	}  

	public static String getProbe(String value_, AS400JDBCConnection connection, int user_ccsid, boolean use_packageCCSID) throws SQLException{
		/*
		JDProperties prop = connection.getProperties(); 
    	int host_bidi_format, package_bidi_format, package_ccsid;
    	int source_ccsid_type, target_ccsid_type, bidi_format;
		    	
    	bidi_format = target_ccsid_type = source_ccsid_type = host_bidi_format = AS400BidiTransform.getStringType(user_ccsid);				

		package_ccsid = connection.getProperties().getInt(JDProperties.PACKAGE_CCSID);
		package_bidi_format = AS400BidiTransform.getStringType(package_ccsid);
    	
    	if(bidi_format != 0){
    		 if(prop.getString(JDProperties.BIDI_IMPLICIT_REORDERING).equalsIgnoreCase("true")){				       			
    		 	value_ = AS400BidiTransform.meta_data_reordering(value_, bidi_format, host_bidi_format);        			
    		}
    		
    		value_ = AS400BidiTransform.SQL_statement_reordering(value_, bidi_format, host_bidi_format);
    		if(use_packageCCSID){
    			value_ = bidiTransform(value_, host_bidi_format, package_bidi_format);
    			value_ = bidiTransform(value_, package_bidi_format, host_bidi_format);
    		}
    	}	
    	return bidiTransform(value_, host_bidi_format, target_ccsid_type);	
    	*/
		return value_;
	}  
	

	
	private static boolean checkRoundTrip(String s1, String s2, int inFormat, int outFormat){
		if(bidiTransform(s1, inFormat, outFormat).equals(bidiTransform(s2, inFormat, outFormat)))
			return true;
		if(bidiTransform(s1,outFormat, inFormat).equals(bidiTransform(s2, outFormat, inFormat)))
			return true;
		
		return false;
	}
	
	public static boolean checkRoundTripLimitation(String s1, String s2){		
		if(checkRoundTrip(s1, s2, 5, 10))
			return true;
		if(checkRoundTrip(s1, s2, 6, 10))
			return true;
		
		
		for(int i=4;i<12;i++)
			for(int j=4;j<12;j++)
				if(checkRoundTrip(s1, s2, i, j))
					return true;
		/*
		if(checkRoundTrip(s1, s2, 4, 5))
			return true;
		if(checkRoundTrip(s1, s2, 6, 5))
			return true;
		 */
		//Brodsky1
		
		return false;		
	}
}
