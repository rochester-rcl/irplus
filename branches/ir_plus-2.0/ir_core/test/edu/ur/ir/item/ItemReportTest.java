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

import edu.ur.ir.item.GenericItem;

/**
 * Test the Item report Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ItemReportTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		GenericItem item = new GenericItem("itemName");
		
		Series series = new Series("series", "1001");

		ItemReport itemReport = new ItemReport();
		
		itemReport.setReportNumber("123");
		itemReport.setSeries(series);
		itemReport.setItem(item);
		
		assert itemReport.getItem().equals(item) : "Items should be equal";
		assert itemReport.getReportNumber().equals("123") : "report number should be equal";
		assert itemReport.getSeries().equals(series) : "series should be equal";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		GenericItem item1 = new GenericItem("itemName1");
		
		Series series1 = new Series("series1", "1001");

		ItemReport itemReport1 = new ItemReport();
		
		itemReport1.setReportNumber("1");
		itemReport1.setSeries(series1);
		itemReport1.setItem(item1);

		GenericItem item2 = new GenericItem("itemName2");
		
		Series series2 = new Series("series2", "1002");

		ItemReport itemReport2 = new ItemReport();
		
		itemReport2.setReportNumber("2");
		itemReport2.setSeries(series2);
		itemReport2.setItem(item2);

		GenericItem item3 = new GenericItem("itemName1");
		
		Series series3 = new Series("series1", "1001");

		ItemReport itemReport3 = new ItemReport();
		
		itemReport3.setReportNumber("1");
		itemReport3.setSeries(series3);
		itemReport3.setItem(item3);

		assert itemReport1.equals(itemReport3) : "Item Identifiers should be equal";
		assert !itemReport1.equals(itemReport2) : "Item Identifier should not be equal";
		
		assert itemReport1.hashCode() == itemReport3.hashCode() : "Hash codes should be the same";
		assert itemReport2.hashCode() != itemReport3.hashCode() : "Hash codes should not be the same";
	}
}
