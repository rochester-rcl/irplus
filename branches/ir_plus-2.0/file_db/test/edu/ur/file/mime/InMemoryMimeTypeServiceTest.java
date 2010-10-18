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

import java.io.File;

import org.testng.annotations.Test;

import edu.ur.exception.DuplicateNameException;

@Test(groups = { "baseTests" }, enabled = true)
public class InMemoryMimeTypeServiceTest {
	

	/**
	 * Test getting the top media types
	 */
	@Test
	public void testGetTopMediaTypes() {
		InMemoryMimeTypeService immts = new InMemoryMimeTypeService();
		TopMediaType topMediaType = immts.createTopMediaType("top1", "description1");
		topMediaType.setId(44l);
		
		assert immts.getTopMediaTypes().contains(topMediaType) : "Should contain the top media type";
		
		TopMediaType topMediaType2 = immts.createTopMediaType("top1", "description1");
		
		assert topMediaType2.equals(topMediaType) : "The top media types should be the same";
		
		TopMediaType topMediaType3 = immts.createTopMediaType("top2", "description2");
		topMediaType3.setId(37l);
		
		assert immts.findTopMediaType("top1").equals(topMediaType) : "Top media types should be equal";
		assert immts.findTopMediaType(44l).equals(topMediaType) : "Top media types should be equal";
		
		assert immts.removeTopMediaType("top1") : "The top media type should be removed from the set";
		
		assert immts.findTopMediaType("top1") == null : "Should not find the mime top media type";
		assert immts.findTopMediaType(44l) == null : "Should not find the mime top media type";
		
		assert immts.removeTopMediaType(37l) : "Top media type should be removed";
		
	}
	
	/**
	 * Test finding mime type by using the extension 
	 * @throws DuplicateNameException 
	 */
	@Test
	public void findMimeTypeExtensionTest() throws DuplicateNameException {
		InMemoryMimeTypeService immts = new InMemoryMimeTypeService();
		
		TopMediaType topMediaType1 = immts.createTopMediaType("application", "description1");
		topMediaType1.setId(44l);
		
		SubType mimeSubType1 = topMediaType1.createSubType("pdf");
		mimeSubType1.setDescription("Portable document format");
		SubTypeExtension ext1 = mimeSubType1.createExtension("pdf");

		
		SubType mimeSubType2 = topMediaType1.createSubType("doc");
		mimeSubType2.setDescription("Microsoft word format");
		
		SubTypeExtension ext2 = mimeSubType2.createExtension("doc");
		
		assert immts.findExtension("doc").equals(ext2) : "Sub type extension should be found";
		assert immts.findExtension("pdf").equals(ext1) : "Sub type extension should be found";
		
		assert immts.findMimeType("doc").equals("application/doc") : "Mime type should be found";
		assert immts.findMimeType("pdf").equals("application/pdf") : "Mime type should be found";
		
		File f = new File("file.pdf");
		
		assert immts.findMimeType(f).equals("application/pdf") : "Mime type should be found";
		
		
	}

}
