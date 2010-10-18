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

/**
 * Test the Series Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SeriesTest {
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		Series series = new Series();
		series.setName("seriesName");
		series.setDescription("seriesDescription");
		series.setNumber("10001");
		series.setId(55l);
		series.setVersion(33);
		
		assert series.getName().equals("seriesName") : "Should equal seriesName";
		assert series.getDescription().equals("seriesDescription") : "Shoud equal seriesDescription";
		assert series.getNumber().equals("10001") : "Shoud equal 10001";
		assert series.getId().equals(55l) : "Should equal 55l";
		assert series.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		Series series1 = new Series();
		series1.setName("seriesName");
		series1.setDescription("seriesDescription");
		series1.setNumber("10001");
		series1.setId(55l);
		series1.setVersion(33);
		
		Series series2 = new Series();
		series2.setName("seriesName2");
		series2.setDescription("seriesDescription2");
		series2.setNumber("10002");
		series2.setId(55l);
		series2.setVersion(33);

		
		Series series3 = new Series();
		series3.setName("seriesName");
		series3.setDescription("seriesDescription");
		series3.setNumber("10001");
		series3.setId(55l);
		series3.setVersion(33);
		
		assert series1.equals(series3) : "Series should be equal";
		assert !series1.equals(series2) : "Series should not be equal";
		
		assert series1.hashCode() == series3.hashCode() : "Hash codes should be the same";
		assert series2.hashCode() != series3.hashCode() : "Hash codes should not be the same";
	}
}
