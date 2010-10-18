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

import org.testng.annotations.Test;

/**
 * Test the Ignore ipaddress Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class IgnoreIpAddressTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		IgnoreIpAddress ip = new IgnoreIpAddress(123,1,1,5,10);
		ip.setId(55l);
		ip.setVersion(33);
		
		assert ip.getFromAddress1() == 123 : " From IpAddressPart1 Should equal 123";
		assert ip.getFromAddress2() ==1 : " From IpAddressPart2 Shoud equal 1";
		assert ip.getFromAddress3() == 1 : " From IpAddressPart3 Shoud equal 1";
		assert ip.getFromAddress4() == 5 : " From IpAddressPart4 Shoud equal 5";
		assert ip.getToAddress4() == 10 : " To IpAddressPart4 Shoud equal 10";
		assert ip.getId().equals(55l) : "Should equal 55l";
		assert ip.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		IgnoreIpAddress ip = new IgnoreIpAddress(123,1,1,5,10);
		ip.setId(55l);
		ip.setVersion(33);
		
		IgnoreIpAddress ip1 = new IgnoreIpAddress(123,1,1,5,15);
		ip1.setId(56l);
		ip1.setVersion(34);
		
		IgnoreIpAddress ip2 = new IgnoreIpAddress(123,1,1,5,10);
		ip2.setId(55l);
		ip2.setVersion(33);
		
		assert ip.equals(ip2) : "Ignore ipaddress should be equal";
		assert !ip.equals(ip1) : "Ignore ipaddress should not be equal";
		
		assert ip.hashCode() == ip2.hashCode() : "Hash codes should be the same";
		assert ip1.hashCode() != ip2.hashCode() : "Hash codes should not be the same";
	}
}
