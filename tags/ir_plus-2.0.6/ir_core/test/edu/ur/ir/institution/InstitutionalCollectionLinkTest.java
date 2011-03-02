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

package edu.ur.ir.institution;

/**
 * Basic tests for links.
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalCollectionLinkTest {
	
	/**
	 *  Make sure equals works as expected
	 */
	public void testEquals()
	{
		InstitutionalCollection collection = new InstitutionalCollection();
		collection.setName("A collection");
		
		InstitutionalCollectionLink link1 = new InstitutionalCollectionLink("test1", "http://www.theserverside.com", 1, collection);
		InstitutionalCollectionLink link2 = new InstitutionalCollectionLink("test2", "http://www.theserverside.com", 1, collection);
		InstitutionalCollectionLink link3 = new InstitutionalCollectionLink("test1", "http://www.theserverside.com", 1, collection);
		
		assert !link1.equals(link2) : "link1 = " + link1 + " should not equal link2 = " + link2;
		assert link1.equals(link3) : "link3 = " + link1 + " should equal link1 " + link1;
		
		assert link1.hashCode() == link3.hashCode() : "hash codes should be equal";
	}


}
