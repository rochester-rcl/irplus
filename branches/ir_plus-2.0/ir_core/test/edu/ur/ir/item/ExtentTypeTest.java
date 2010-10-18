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
 * Test the Extent type Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExtentTypeTest {

	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		ExtentType it = new ExtentType();
		it.setName("itName");
		it.setDescription("itDescription");
		it.setId(55l);
		it.setVersion(33);
		
		assert it.getName().equals("itName") : "Should equal itName";
		assert it.getDescription().equals("itDescription") : "Shoud equal itDescription";
		assert it.getId().equals(55l) : "Should equal 55l";
		assert it.getVersion() == 33 : "Should equal 33";
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
		
		ExtentType it2 = new ExtentType();
		it2.setName("itName2");
		it2.setDescription("itDescription2");
		it2.setId(55l);
		it2.setVersion(33);

		ExtentType it3 = new ExtentType();
		it3.setName("itName");
		it3.setDescription("itDescription");
		it3.setId(55l);
		it3.setVersion(33);
		
		assert it1.equals(it3) : "Extent types should be equal";
		assert !it1.equals(it2) : "Extent types should not be equal";
		
		assert it1.hashCode() == it3.hashCode() : "Hash codes should be the same";
		assert it2.hashCode() != it3.hashCode() : "Hash codes should not be the same";
	}

}
