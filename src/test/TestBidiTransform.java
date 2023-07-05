
/*
 * BIDI testcases originally from Gregory Brodsky
 *
 * To adapt for use in our testbucket, the following changes were made.
 *
 *  1.  Add the package test
 *  2.  Added errorLog_ buffer
 *  3.  Replace 'System.out.println("' with 'errorLog_.append("\n'
 *  4.  Create errorLog_ buffer in beginning of test method. 

 *
 * This testcase is driven through TestBidiConnection in JDBIDITestcase
 *  
 */ 



package test;

import com.ibm.as400.access.*;

public class TestBidiTransform {

	/**
	 * @param args
	 */
    static protected StringBuffer errorLog_ = new StringBuffer(); //for error messages
    
		public static int Arabic_CCSID[] = {420, 8612, 62224, 12708, 62233, 62234};
		public static int Hebrew_CCSID[] = {424, 62211, 62235, 62240, 62245, 8616, 12712, 62229}; 

		public static String Arabic_String [] = {" \ufe95 1234567890 1234567890 \ufefbA", 
				" \ufe95 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA",
				"\u0644\u0627 1234567890 1234567890 \u062AA",
				"\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA",
				" \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA",
				"\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 1234567890\u062AA",
				"\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA",
				" \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 1234567890 \ufefbA",
				" \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA",
				"\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\u062AA",
				"\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA",
				" \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA",
				" \ufe95 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA",
				"\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA",
		        " \ufe951234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\ufefbA",
		        "\u0644\u0627\u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660\u062AA",
		        "\u0644\u0627 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u062AA",
		        " \ufe95 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 \ufefbA",
		        "Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin",
		        "Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin",
		        "Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin",
		        "Latin 1234567890 \u0661\u0662\u0663\u0664\u0665\u0666\u0667\u0668\u0669\u0660 Latin",
		        "\uFEF0\uFE91\uFEAE\uFECB\u0020\u0074\u0068\u0069\u0073\u0020\u0069\u0073\u0020\u0074\u0065\u0073\u0074\u0020\uFE8E\uFEF4\uFEE7\uFE8E\uFE9B\u0020\uFEED\u0020\uFEFB\uFEED\uFE8D"
		};
		
		public static String Hebrew_String [] = //{"ABC \u05D0\u05D1\u05D2"};
		
		{
				" ABC \u05D0\u05D1\u05D2 123 456 \u05D3\u05D4\u05D5 ",
				" \u05D0\u05D1\u05D2 123 456 \u05D3\u05D4\u05D5 ABC ",
				"ABC \u05D0\u05D1\u05D2",
				"\u05D0\u05D1\u05D2 ABC",
				"ABC \u05D0\u05D1\u05D2 123",
				"\u05D0\u05D1\u05D2 ABC 123",
				"\u05D0\u05D1\u05D2 123 ABC",
				"A' \u05D0\u05D1\u05D2 123",
				"\u05D0'\u05D2 123'",
				"\u0041\u05D0"+"0123"				
		};

		public static void main(String[] args) {
		
		//test(420, Arabic_String, Arabic_CCSID);
		
		//test1(Hebrew_String, 8, 11);
		//test1(Hebrew_String, 6, 5);
		//test1(Hebrew_String, 10, 10);
		
		test(424, Hebrew_String, Hebrew_CCSID);
				
		
	}

	public static int test1(String[] str, int javaType, int as400Type){
		//int ccsid, javaType = 8, i = 0, j, as400Type = 5, error_count = 0;;
		int error_count = 0, i = 0;
		String src = str[0], res1, res2;
		AS400BidiTransform as400BidiTransform = new AS400BidiTransform(424);
		//tmpWrapper as400BidiTransform = new tmpWrapper();
		BidiConversionProperties properties = new BidiConversionProperties();
		properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_NOMINAL);		
		
		try{
			//bidiTransform.setAS400Ccsid(ccsid);
			//as400Type = bidiTransform.getAS400StringType();
			as400BidiTransform.setAS400StringType(as400Type);
			
			as400BidiTransform.setJavaStringType(javaType);
									
			errorLog_.append("\n^^^^^^^^^^^^^^^^");
			errorLog_.append("\nline index=" + i + " javaType=" + javaType + " as400Type=" + as400Type);

			res1 = as400BidiTransform.toJavaLayout(src);
			res2 = BidiEngineWrapper.bidiTransform(src, as400Type, javaType);
			if(!res1.equals(res2)){
				error_count++;
				errorLog_.append("\nError with toJavaLayout()");
				errorLog_.append("\n\t\u202D" + res1 + "\n\t\u202D" + res2);
			}

			res1 = as400BidiTransform.toAS400Layout(src);
			res2 = BidiEngineWrapper.bidiTransform(src, javaType, as400Type);
			if(!res1.equals(res2)){
				error_count++;
				errorLog_.append("\nError with toAS400Layout()");
				errorLog_.append("\n\t\u202D" + res1 + "\n\t\u202D" + res2);
			}
			//errorLog_.append("\n--------------------\n");

		}catch(Exception e){
			e.printStackTrace();
		}
		return error_count; 
	}
	
	public static boolean test(int initial_ccsid, String[] str, int [] ccsid_list){
	    errorLog_ = new StringBuffer();
	    errorLog_.append("\nText AS400BidiTransform, different combinations of CCSIDs and javaType");

		int  javaType, i, as400Type, error_count = 0;;
		String src, res1, res2;
		AS400BidiTransform as400BidiTransform = new AS400BidiTransform(initial_ccsid);
		//tmpWrapper as400BidiTransform = new tmpWrapper();
		BidiConversionProperties properties = new BidiConversionProperties();
		properties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_NOMINAL);
		//properties.setBidiNumericOrderingRoundTrip(true);
		//TODO - add that in my Bidi transform method...  check word break an other extra params...
		//as400BidiTransform.setBidiConversionProperties(properties);
		for(i = 0; i < str.length; i++){
			src = str[i];
			//for(j = 0; j < ccsid_list.length; j++){
			//	ccsid = ccsid_list[j];
			for(as400Type = 4; as400Type < 12; as400Type++){				
				for(javaType = 4; javaType < 12; javaType++){
					try{
						//bidiTransform.setAS400Ccsid(ccsid);
						//as400Type = bidiTransform.getAS400StringType();
						as400BidiTransform.setAS400StringType(as400Type);
						
						as400BidiTransform.setJavaStringType(javaType);
												
						//errorLog_.append("\n^^^^^^^^^^^^^^^^");
						//errorLog_.append("\nline index=" + i + " javaType=" + javaType + " as400Type=" + as400Type);

						res1 = as400BidiTransform.toJavaLayout(src);
						res2 = BidiEngineWrapper.bidiTransform(src, as400Type, javaType);
						if(!res1.equals(res2)){
							error_count++;
							errorLog_.append("\nError with toJavaLayout()");
							errorLog_.append("\nline index=" + i + " javaType=" + javaType + " as400Type=" + as400Type);
							errorLog_.append("\n\t\u202D" + res1 + "\n\t\u202D" + res2);
						}

						res1 = as400BidiTransform.toAS400Layout(src);
						res2 = BidiEngineWrapper.bidiTransform(src, javaType, as400Type);
						if(!res1.equals(res2)){
							error_count++;
							errorLog_.append("\nError with toAS400Layout()");
							errorLog_.append("\nline index=" + i + " javaType=" + javaType + " as400Type=" + as400Type);
							errorLog_.append("\n\t\u202D" + res1 + "\n\t\u202D" + res2);
						}
						//errorLog_.append("\n--------------------\n");

					}catch(Exception e){
						e.printStackTrace();
					}
				}

			}
		}
		if(error_count == 0)
			errorLog_.append("\nSUCCESS");
		else 
			errorLog_.append("\nFAILURE, " + error_count + " errors");
		
		return (error_count == 0);

	}
}
