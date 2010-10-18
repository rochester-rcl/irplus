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
 * Test the Mime Sub Type
 * 
 * @author Nathan Sarr
 * 
 */

@Test(groups = { "baseTests" }, enabled = true)
public class SubTypeTest {

	private SubType t1;
	private SubType t2;
	private SubType t3;

	private TopMediaType top1;
	private TopMediaType top2;
	private TopMediaType top3;

	@BeforeTest
	public void setUp() throws DuplicateNameException {
		
		top1 = new TopMediaType("topType1", "topDescrption1");
		top2 = new TopMediaType("topType2", "topDescrption2");
		top3 = new TopMediaType("topType3", "topDescrption3");
		
		t1 = top1.createSubType("type1");
		t1.setDescription("description1");
		
		t2 = top2.createSubType("type2"); 
		t2.setDescription("description2");
		
		t3 = top3.createSubType("type1");
		t3.setDescription("description1");
	}

	/**
	 * Set the description of the mime sub type
	 * 
	 */
	public void testSetDescription() {
		t2.setDescription("anotherDescription");
		assert t2.getDescription().equals("anotherDescription") : "expected description got: "
				+ t2.getDescription();
	}

	/**
	 * Set the name of the mime sub type
	 * 
	 */
	public void testSetName() {
		t2.setName("anotherName");
		assert t2.getName().equals("anotherName") : "expected description got: "
				+ t2.getName();
	}
	
	/**
	 * Set the top media type
	 */
	public void testSetTopMediaType() {
		t2.setTopMediaType(top3);
		assert t2.getTopMediaType().equals(top3) : "media type should be the same";
	}
	
	/**
	 * test adding and removing extensions
	 * @throws DuplicateNameException 
	 *
	 */
	public void testExtensions() throws DuplicateNameException
	{
		SubTypeExtension ext1 = t3.createExtension("pdf");
		
		assert ext1.getSubType() == t3 : "Parent subtype should be t3";
		assert t3.getExtension("pdf").equals(ext1) : "Should find the pdf extension";
		assert t3.getExtension("doc") == null : "Should not find the doc extension";
		assert t3.removeExtension(ext1.getName()) : "Should be able to remove pdf extension";
		assert ext1.getSubType() == null : "Parent sub type should be null";
		assert t3.getExtension("pdf") == null : "Should not find the pdf extension";
		
		SubTypeExtension ext2 = t3.createExtension("doc");
		
		assert t3.getExtension("doc").equals(ext2) : "Should find the doc extension";
		
		t3.getExtension("doc").setId(1L);
		assert t3.getExtension(1L).equals(ext2): "Should be able to find ext2 by id";
		assert t3.removeExtension(1l): "Should be able to remove ext2 by id";
		assert ext2.getSubType() == null : "Parent sub type should be null";
		
		SubTypeExtension ext3 =t3.createExtension("xls");

		assert t3.getExtension("xls").equals(ext3) : "Should find the xls extension";
		assert t3.removeExtension("xls") : "Should be able to remove extension by name";
	}	

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public void testHashCode() {
		assert t1.hashCode() == t3.hashCode() : "exepected has codes are equals t1 = " + t1 + " t3 = " + t3;
		assert t1.hashCode() != t2.hashCode() : "expected hash codes are different";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public void testEquals() {
		assert t1.equals(t3) : "expected t1 and t3 are the same " + " t1 = " + t1 + " t3 = " + t3;
		assert !t1.equals(t2) : "expected t1 and t2 are not the same";

	}
}
