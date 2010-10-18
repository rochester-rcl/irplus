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

import edu.ur.ir.item.ItemLink;

/**
 * Test the Item Link Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemLinkTest {

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicItemLinkSets() throws Exception 
	{
		ItemLink ItemLink = new ItemLink();
		ItemLink.setName("ItemLinkName");
		ItemLink.setUrlValue("http://www.hotmail.com");
		ItemLink.setDescription("ItemLinkDescription");
		ItemLink.setId(55l);
		ItemLink.setVersion(33);
		
		assert ItemLink.getName().equals("ItemLinkName") : "Should equal ItemLinkName";
		assert ItemLink.getDescription().equals("ItemLinkDescription") : "Shoud equal ItemLinkDescription";
		assert ItemLink.getId().equals(55l) : "Should equal 55l";
		assert ItemLink.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals() throws Exception
	{
	
		ItemLink ItemLink1 = new ItemLink();
		ItemLink1.setName("ItemLinkName");
		ItemLink1.setUrlValue("http://www.hotmail.com");
		ItemLink1.setDescription("ItemLinkDescription");
		ItemLink1.setId(55l);
		ItemLink1.setVersion(33);
		
		ItemLink ItemLink2 = new ItemLink();
		ItemLink2.setName("ItemLinkName2");
		ItemLink2.setUrlValue("http://www.msnbc.com");
		ItemLink2.setDescription("ItemLinkDescription2");
		ItemLink2.setId(55l);
		ItemLink2.setVersion(33);

		
		ItemLink ItemLink3 = new ItemLink();
		ItemLink3.setName("ItemLinkName");
		ItemLink3.setUrlValue("http://www.hotmail.com");
		ItemLink3.setDescription("ItemLinkDescription");
		ItemLink3.setId(55l);
		ItemLink3.setVersion(33);
		
		assert ItemLink1.equals(ItemLink3) : "Contributor types should be equal";
		assert !ItemLink1.equals(ItemLink2) : "Contributor types should not be equal";
		
		assert ItemLink1.hashCode() == ItemLink3.hashCode() : "Hash codes should be the same";
		assert ItemLink2.hashCode() != ItemLink3.hashCode() : "Hash codes should not be the same";
	}

}
