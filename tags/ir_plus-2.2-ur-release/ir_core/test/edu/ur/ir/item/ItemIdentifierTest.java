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

import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemIdentifier;

/**
 * Test the Identifier type Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemIdentifierTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem item = new GenericItem("itemName");

		IdentifierType it = new IdentifierType();
		it.setName("itName");
		it.setDescription("itDescription");
		it.setId(55l);
		it.setVersion(33);

		ItemIdentifier itemIdentifier = new ItemIdentifier();
		
		itemIdentifier.setIdentifierType(it);
		itemIdentifier.setItem(item);
		itemIdentifier.setValue("12345");
		
		assert itemIdentifier.getItem().equals(item) : "Items should be equal";
		assert itemIdentifier.getValue().equals("12345") : "Values should be equal";
		assert itemIdentifier.getIdentifierType().equals(it) : "Identifier types should be equal";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		IdentifierType it1 = new IdentifierType();
		it1.setName("itName");
		it1.setDescription("itDescription");
		it1.setId(55l);
		it1.setVersion(33);
		
		ItemIdentifier itemIdentifier1 = new ItemIdentifier();
		itemIdentifier1.setIdentifierType(it1);
		itemIdentifier1.setValue("value1");
		
		IdentifierType it2 = new IdentifierType();
		it2.setName("itName2");
		it2.setDescription("itDescription2");
		it2.setId(55l);
		it2.setVersion(33);

		ItemIdentifier itemIdentifier2 = new ItemIdentifier();
		itemIdentifier2.setIdentifierType(it2);
		itemIdentifier2.setValue("value2");

		
		IdentifierType it3 = new IdentifierType();
		it3.setName("itName");
		it3.setDescription("itDescription");
		it3.setId(55l);
		it3.setVersion(33);

		ItemIdentifier itemIdentifier3 = new ItemIdentifier();
		itemIdentifier3.setIdentifierType(it3);
		itemIdentifier3.setValue("value1");
		
		assert itemIdentifier1.equals(itemIdentifier3) : "Item Identifiers should be equal";
		assert !itemIdentifier1.equals(itemIdentifier2) : "Item Identifier should not be equal";
		
		assert itemIdentifier1.hashCode() == itemIdentifier3.hashCode() : "Hash codes should be the same";
		assert it2.hashCode() != it3.hashCode() : "Hash codes should not be the same";
	}
}
