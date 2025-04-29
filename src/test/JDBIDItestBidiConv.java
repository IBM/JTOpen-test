
package test;
import java.beans.PropertyVetoException;
import java.io.IOException;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.AS400Text;
import com.ibm.as400.access.BidiConversionProperties;
import com.ibm.as400.access.CharConverter;
import com.ibm.as400.access.CharacterFieldDescription;
import com.ibm.as400.access.ErrorCompletingRequestException;
import com.ibm.as400.access.Record;
import com.ibm.as400.access.RecordFormat;

/**
 * Why are marks not getting stripped
 *
 */
public class JDBIDItestBidiConv extends Object {
	public static void main(String[] args) throws AS400SecurityException, ErrorCompletingRequestException, IOException, InterruptedException, PropertyVetoException {
		AS400 system = new AS400("torasi5c","masri424","webface1".toCharArray());
		int ccsid = 424;

		// testing upload
		String logicalSource = "'\u05e7 \u200e1'";
		CharConverter converter = new CharConverter(ccsid, system);	
		BidiConversionProperties bidiProperties = new BidiConversionProperties();												//59731A
		bidiProperties.setBidiRemoveMarksOnImplicitToVisual(true);															//59731A
		bidiProperties.setBidiRemoveDirectionalMarks(true);																		//58349/59843A
		 byte[] stringToByteArray = converter.stringToByteArray(logicalSource, bidiProperties);	
		//System.out.println(Arrays.toString(stringToByteArray));
		System.out.println(new String(stringToByteArray));
		 if (stringToByteArray[3] == -3) {
			 System.out.println("What is this LRM doing in here?");
		 }
		 
		 //testing download
			String visualSource = "'\u05e7 1'";
			 int length = visualSource.length();
		 RecordFormat format = new RecordFormat();
		 format.addFieldDescription(new CharacterFieldDescription(new AS400Text(5,424),"testsource"));
		 Record record = format.getNewRecord(visualSource.getBytes("cp424"));
		 byte[] sourceBytes = record.getContents();

		 int offset = 0;
		String result;
		
		// Beginning of actual RDp code
//		if (AS400BidiTransform.isBidiCcsid(ccsid) && AS400BidiTransform.isVisual(ccsid)) {				//59731A
				bidiProperties = new BidiConversionProperties();									//59731A
				byte[] tempSource = new byte[length];																		//58349/59843A
	            System.arraycopy(sourceBytes, offset, tempSource, 0, length);													//58349/59843A
																															//59731A
//				if (leaveVisual && AS400BidiTransform.isVisual(ccsid))														//59731A
//					bidiProperties.setBidiStringType(BidiStringType.NONE);													//59731A
//				else {																										//59731A
//					tempSource = NlsUtil.massageLamAlefs(tempSource, ccsid);												//58349/59843A
//					bidiProperties.setBidiStringType(AS400BidiTransform.getStringType(ccsid));								//59731A
					bidiProperties.setBidiInsertDirectionalMarks(true);
					bidiProperties.setBidiNumeralShaping(BidiConversionProperties.NUMERALS_ANY);                            //67096A
//				}
				
//				if (AS400BidiTransform.isVisual(ccsid)) || !leaveVisual)														//59731A
					result = converter.byteArrayToString(tempSource, 0, tempSource.length, bidiProperties);					//59731A 58349/59843C
//				else {																										//59731A
//					StringNL bidi = new StringNL(converter.byteArrayToString(tempSource, 0, tempSource.length, bidiProperties), ccsid);	//59731A 58349/59843C
//					try {																									//59731A
//						return bidi.convertFromLogicalToVisual(true);														//59731A
//					} catch (UnsupportedEncodingException e) {																//59731A
//						//ISeriesSystemPlugin.logError("ISeriesCodePageConverter "+e, e);										//59731A
//						return bidi.toString();																				//59731A
//					}																										//59731A
//				}				
		// test end result
		
		if (result.equals(logicalSource)) {
			System.out.println("Good download?");
		} else {
			System.out.println("Bad download? "+result);

		}
	}

}
