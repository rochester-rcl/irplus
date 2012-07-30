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
 * Test the Mime Sub Type Extension
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class SubTypeExtensionTest 
{

	private SubTypeExtension ext1;
	private SubTypeExtension ext2;
	private SubTypeExtension ext3;
	
	@BeforeTest
	public void setUp() throws DuplicateNameException {
		TopMediaType top1 = new TopMediaType("topType1", "topDescription1");
		SubType mimeSubType = top1.createSubType("subType1");
		mimeSubType.setDescription("description");
		ext1 = mimeSubType.createExtension("pdf");
		ext2 = mimeSubType.createExtension("doc");
		
		// create another set with the same data for 
		// comparison reasons
		TopMediaType top2 = new TopMediaType("topType1", "topDescription1");
		SubType mimeSubType2 = top2.createSubType("subType1");
		ext3 = mimeSubType2.createExtension("pdf");
	}

	/**
	 * Set the extension
	 * 
	 */
	@Test
	public void testSetExtension() {
		ext2.setName("blah");
		assert ext2.getName().equals("blah") : "expected blah got: "
				+ ext2.getName();
		
		assert ext1.getMimeType().equals("topType1/subType1") : "Expected topType1/subType1 but received " 
			+ ext1.getMimeType();
	}
	
	/**
	 * Test setting the mime sub type parent
	 * @throws DuplicateNameException 
	 */
	@Test
	public void testSetParent() throws DuplicateNameException
	{
		TopMediaType top1 = new TopMediaType("topType1", "topDescription1");
		SubType mimeSubType = top1.createSubType("subType1");
		mimeSubType.setDescription("description");
		
		ext3.setSubType(mimeSubType);
		assert ext3.getSubType().equals(mimeSubType) : "Parent sub type should be set";
	}

	/**
	 * Make sure hash code works properly
	 */
	@Test
	public void testHashCode() {
		assert ext1.hashCode() == ext3.hashCode() : "exepected has codes are equals";
		assert ext1.hashCode() != ext2.hashCode() : "expected hash codes are different";
	}

	
	/**
	 *Make sure equals works properly 
	 */
	@Test
	public void testEquals() {
		assert ext1.equals(ext3) : "expected ext1 and ext3 are the same";
		assert !ext1.equals(ext2) : "expected ext1 and ext2 are not the same";
	}
	
}
