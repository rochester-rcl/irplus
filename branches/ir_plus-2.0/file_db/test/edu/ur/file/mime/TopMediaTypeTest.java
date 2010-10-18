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
package edu.ur.file.mime;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;

/**
 * Test the Mime top media type
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class TopMediaTypeTest {

	private TopMediaType t1;
	private TopMediaType t2;
	private TopMediaType t3;

	@BeforeTest
	public void setUp() {
		t1 = new TopMediaType("type1", "description1");
		t2 = new TopMediaType("type2", "description2");
		t3 = new TopMediaType("type1", "description1");
	}

	/**
	 * Set the description of the mime type
	 * 
	 */
	public void testSetDescription() {
		t2.setDescription("anotherDescription");
		assert t2.getDescription().equals("anotherDescription") : "expected description got: "
				+ t2.getDescription();
	}

	/**
	 * Set the name of the mime type
	 * 
	 */
	public void testSetName() {
		t2.setName("anotherName");
		assert t2.getName().equals("anotherName") : "expected description got: "
				+ t2.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public void testHashCode() {
		assert t1.hashCode() == t3.hashCode() : "exepected has codes are equals";
		assert t1.hashCode() != t2.hashCode() : "expected hash codes are different";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public void testEquals() {
		assert t1.equals(t3) : "expected t1 and t3 are the same";
		assert !t1.equals(t2) : "expected t1 and t2 are not the same";

	}
	
	/**
	 * Test adding and removing sub types 
	 * @throws DuplicateNameException 
	 */
	public void testMimeSubTypes() throws DuplicateNameException
	{
		SubType subType1 = t3.createSubType("sub1");
		subType1.setDescription("desc1");
		
		assert subType1.getTopMediaType() == t3 : "Parent media type should be t3";
		
		assert t3.getSubType("sub1").equals(subType1) : "Should find the sub type 1";
		assert t3.getSubType("sub2") == null : "Should not find the doc extension";
		
		assert t3.removeSubType(subType1.getName()) : "Should be able to remove subType1";
		assert t3.getSubType("sub2") == null : "Should not find sub type 2";
		
		SubType subType2 = t3.createSubType("sub2");
		subType2.setDescription("desc2");
		
		assert t3.getSubType("sub2").equals(subType2) : "Should find sub type 2";
		
		t3.getSubType("sub2").setId(1L);
		assert t3.getSubType(1L).equals(subType2): "Should be able to find subType 2 by id";
		assert t3.removeSubType(1l): "Should be able to remove subType 2 by id";
		
		SubType subType3 = t3.createSubType("sub3");
		subType3.setDescription("desc3");
		assert t3.getSubType("sub3").equals(subType3) : "Should find the subType3 extension";
	}
}
