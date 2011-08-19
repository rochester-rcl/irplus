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

import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemExtent;

/**
 * Test the extent type Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemExtentTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem item = new GenericItem("itemName");

		ExtentType it = new ExtentType();
		it.setName("itName");
		it.setDescription("itDescription");
		it.setId(55l);
		it.setVersion(33);

		ItemExtent itemExtent = new ItemExtent();
		
		itemExtent.setExtentType(it);
		itemExtent.setItem(item);
		itemExtent.setValue("12345");
		
		assert itemExtent.getItem().equals(item) : "Items should be equal";
		assert itemExtent.getValue().equals("12345") : "Values should be equal";
		assert itemExtent.getExtentType().equals(it) : "Extent types should be equal";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		ExtentType it1 = new ExtentType();
		it1.setName("itName");
		it1.setDescription("itDescription");
		it1.setId(55l);
		it1.setVersion(33);
		
		ItemExtent itemExtent1 = new ItemExtent();
		itemExtent1.setExtentType(it1);
		itemExtent1.setValue("value1");
		
		ExtentType it2 = new ExtentType();
		it2.setName("itName2");
		it2.setDescription("itDescription2");
		it2.setId(55l);
		it2.setVersion(33);

		ItemExtent itemExtent2 = new ItemExtent();
		itemExtent2.setExtentType(it2);
		itemExtent2.setValue("value2");

		
		ExtentType it3 = new ExtentType();
		it3.setName("itName");
		it3.setDescription("itDescription");
		it3.setId(55l);
		it3.setVersion(33);

		ItemExtent itemExtent3 = new ItemExtent();
		itemExtent3.setExtentType(it3);
		itemExtent3.setValue("value1");
		
		assert itemExtent1.equals(itemExtent3) : "Item Extents should be equal";
		assert !itemExtent1.equals(itemExtent2) : "Item Extent should not be equal";
		
		assert itemExtent1.hashCode() == itemExtent3.hashCode() : "Hash codes should be the same";
		assert it2.hashCode() != it3.hashCode() : "Hash codes should not be the same";
	}
}
