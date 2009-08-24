/**  
   Copyright 2008 University of Rochester

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/  

package edu.ur.ir.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

/**
 * Test the File Donwload info Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class FileDownloadInfoTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 * @throws ParseException 
	 */
	public void testBasicSets() throws ParseException 
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d = simpleDateFormat.parse("12/1/2008");
	
		FileDownloadInfo downloadInfo = new FileDownloadInfo("123.1.1.5", 1l, d);
		downloadInfo.setDownloadCount(1);
		downloadInfo.setId(55l);
		downloadInfo.setVersion(33);
		
		assert downloadInfo.getIpAddress().equals("123.1.1.5") : "Should equal 123.1.1.5";
		assert downloadInfo.getIpAddressPart1() ==123 : "IpAddressPart1 Shoud equal 123";
		assert downloadInfo.getIpAddressPart2() == 1 : "IpAddressPart2 Shoud equal 1";
		assert downloadInfo.getIpAddressPart3() == 1 : "IpAddressPart3 Shoud equal 1";
		assert downloadInfo.getIpAddressPart4()== 5 : " IpAddressPart4 Shoud equal 5";
		assert downloadInfo.getDownloadCount() == 1 : "Count Shoud equal 1";
		assert downloadInfo.getDownloadDate().equals(d): "Shoud equal download date";
		assert downloadInfo.getId().equals(55l) : "Should equal 55l";
		assert downloadInfo.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 * @throws ParseException 
	 */
	public void testEquals() throws ParseException
	{
	
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date d = simpleDateFormat.parse("12/1/2008");
		FileDownloadInfo downloadInfo = new FileDownloadInfo("123.1.1.5", 1l, d);
		downloadInfo.setDownloadCount(1);
		downloadInfo.setId(55l);
		downloadInfo.setVersion(33);
		
		
		FileDownloadInfo downloadInfo1 = new FileDownloadInfo("123.1.1.9", 1l, d);
		downloadInfo1.setDownloadCount(2);
		downloadInfo1.setId(56l);
		downloadInfo1.setVersion(34);

		
		
		FileDownloadInfo downloadInfo2 = new FileDownloadInfo("123.1.1.5", 1l, d);
		downloadInfo2.setDownloadCount(1);
		downloadInfo2.setId(55l);
		downloadInfo2.setVersion(33);;
		
		assert downloadInfo.equals(downloadInfo2) : "Download info should be equal";
		assert !downloadInfo.equals(downloadInfo1) : "Download info should not be equal";
		
		assert downloadInfo.hashCode() == downloadInfo2.hashCode() : "Hash codes should be the same";
		assert downloadInfo1.hashCode() != downloadInfo2.hashCode() : "Hash codes should not be the same";
	}
}
