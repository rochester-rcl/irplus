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
 * Test the external published item Class
 * 
 * @author Sharmila Ranganathan
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class ExternalPublishedItemTest {
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		ExternalPublishedItem e = new ExternalPublishedItem();
		e.setCitation("citation");
		PublishedDate publishedDate = e.updatePublishedDate(12, 30, 1990);
		e.setId(55l);
		e.setVersion(33);
		
		Publisher p = new Publisher();
		p.setName("name");
		e.setPublisher(p);
		
		
		assert e.getPublishedDate().equals(publishedDate) : "Date Should be equal to date ";
		assert e.getCitation().equals("citation") : "Shoud equal citation";
		assert e.getId().equals(55l) : "Should equal 55l";
		assert e.getVersion() == 33 : "Should equal 33";
		assert e.getPublisher().equals(p) : "Should equal to publisher";
	}
	
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		ExternalPublishedItem e1 = new ExternalPublishedItem();
		e1.setCitation("citation1");
		e1.updatePublishedDate(12, 30, 1990);
		e1.setId(55l);
		e1.setVersion(33);
		
		Publisher p1 = new Publisher();
		p1.setName("name1");
		e1.setPublisher(p1);

		
		ExternalPublishedItem e2 = new ExternalPublishedItem();
		e2.setCitation("citation2");
		e2.updatePublishedDate(12, 30, 1995);
		e2.setId(56l);
		e2.setVersion(34);
		
		Publisher p2 = new Publisher();
		p2.setName("name2");
		e2.setPublisher(p2);

		
		ExternalPublishedItem e3 = new ExternalPublishedItem();
		e3.setCitation("citation1");
		e3.updatePublishedDate(12, 30, 1990);
		e3.setId(57l);
		e3.setVersion(35);
		
		Publisher p3 = new Publisher();
		p3.setName("name1");
		e3.setPublisher(p3);

		assert e1.equals(e3) : "External published item data should be equal";
		assert !e1.equals(e2) : "External published item data should not be equal";
		
		assert e1.hashCode() == e3.hashCode() : "Hash codes should be the same";
		assert e2.hashCode() != e3.hashCode() : "Hash codes should not be the same";
	}
}
