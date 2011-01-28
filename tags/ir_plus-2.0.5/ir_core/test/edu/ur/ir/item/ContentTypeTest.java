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

package edu.ur.ir.item;

import org.testng.annotations.Test;

import edu.ur.ir.item.ContentType;

/**
 * Test the Item Content type Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ContentTypeTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		ContentType ct = new ContentType();
		ct.setName("ctName");
		ct.setDescription("ctDescription");
		ct.setId(55l);
		ct.setVersion(33);
		
		assert ct.getName().equals("ctName") : "Should equal ctName";
		assert ct.getDescription().equals("ctDescription") : "Shoud equal ctDescription";
		assert ct.getId().equals(55l) : "Should equal 55l";
		assert ct.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		ContentType ct1 = new ContentType();
		ct1.setName("ctName");
		ct1.setDescription("ctDescription");
		ct1.setId(55l);
		ct1.setVersion(33);
		
		ContentType ct2 = new ContentType();
		ct2.setName("ctName2");
		ct2.setDescription("ctDescription2");
		ct2.setId(55l);
		ct2.setVersion(33);

		
		ContentType ct3 = new ContentType();
		ct3.setName("ctName");
		ct3.setDescription("ctDescription");
		ct3.setId(55l);
		ct3.setVersion(33);
		
		assert ct1.equals(ct3) : "Contributor types should be equal";
		assert !ct1.equals(ct2) : "Contributor types should not be equal";
		
		assert ct1.hashCode() == ct3.hashCode() : "Hash codes should be the same";
		assert ct2.hashCode() != ct3.hashCode() : "Hash codes should not be the same";
	}
}
