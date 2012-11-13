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
		IgnoreIpAddress ip = new IgnoreIpAddress("123.1.1.5");
		ip.setId(55l);
		ip.setVersion(33);
		
		assert ip.getAddress() == "123.1.1.5" : " From IpAddressPart1 Should equal 123.1.1.5";
		
		assert ip.getId().equals(55l) : "Should equal 55l";
		assert ip.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		IgnoreIpAddress ip =  new IgnoreIpAddress("123.1.1.5");
		ip.setId(55l);
		ip.setVersion(33);
		
		IgnoreIpAddress ip1 = new IgnoreIpAddress("123.1.1.6");
		ip1.setId(56l);
		ip1.setVersion(34);
		
		IgnoreIpAddress ip2 = new IgnoreIpAddress("123.1.1.5");
		ip2.setId(55l);
		ip2.setVersion(33);
		
		assert ip.equals(ip2) : "Ignore ipaddress should be equal";
		assert !ip.equals(ip1) : "Ignore ipaddress should not be equal";
		
		assert ip.hashCode() == ip2.hashCode() : "Hash codes should be the same";
		assert ip1.hashCode() != ip2.hashCode() : "Hash codes should not be the same";
	}
}
