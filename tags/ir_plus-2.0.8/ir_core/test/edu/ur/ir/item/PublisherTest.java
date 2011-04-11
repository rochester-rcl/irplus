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

import edu.ur.ir.item.Publisher;

/**
 * Test the Publisher Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class PublisherTest {
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		Publisher publisher = new Publisher();
		publisher.setName("publisherName");
		publisher.setDescription("publisherDescription");
		publisher.setId(55l);
		publisher.setVersion(33);
		
		assert publisher.getName().equals("publisherName") : "Should equal publisherName";
		assert publisher.getDescription().equals("publisherDescription") : "Shoud equal publisherDescription";
		assert publisher.getId().equals(55l) : "Should equal 55l";
		assert publisher.getVersion() == 33 : "Should equal 33";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		Publisher publisher1 = new Publisher();
		publisher1.setName("publisherName");
		publisher1.setDescription("publisherDescription");
		publisher1.setId(55l);
		publisher1.setVersion(33);
		
		Publisher publisher2 = new Publisher();
		publisher2.setName("publisherName2");
		publisher2.setDescription("publisherDescription2");
		publisher2.setId(55l);
		publisher2.setVersion(33);

		
		Publisher publisher3 = new Publisher();
		publisher3.setName("publisherName");
		publisher3.setDescription("publisherDescription");
		publisher3.setId(55l);
		publisher3.setVersion(33);
		
		assert publisher1.equals(publisher3) : "Contributor types should be equal";
		assert !publisher1.equals(publisher2) : "Contributor types should not be equal";
		
		assert publisher1.hashCode() == publisher3.hashCode() : "Hash codes should be the same";
		assert publisher2.hashCode() != publisher3.hashCode() : "Hash codes should not be the same";
	}
}
