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

package edu.ur.ir.file;

import org.testng.annotations.Test;


/**
 * Test the transfromed file type Class
 * 
 * @author Nathan Sarr
 * 
 */
@Test(groups = { "baseTests" }, enabled = true)
public class TransformedFileTypeTest {
	
	
	/**
	 * Test basic set and get methods
	 * 
	 * @param description
	 */
	public void testBasicSets() 
	{
		
		TransformedFileType transformedFileType = new TransformedFileType();
		
		transformedFileType.setDescription("description");
		transformedFileType.setName("name");
		transformedFileType.setId(44l);
		transformedFileType.setSystemCode("code");
		
		assert transformedFileType.getSystemCode().equals("code");
		assert transformedFileType.getName().equals("name");
		assert transformedFileType.getDescription().equals("description");
	}
	
	/**
	 * Test equals and hash code methods.
	 */
	public void testEquals()
	{
	
		TransformedFileType transformedType1 = new TransformedFileType();
		transformedType1.setName("transformedName");
		
		TransformedFileType transformedType2 = new TransformedFileType();
		transformedType2.setName("transformedName2");

		
		TransformedFileType transformedType3 = new TransformedFileType();
		transformedType3.setName("transformedName");
		
		assert transformedType1.equals(transformedType3) : "transfromed file types should be equal";
		assert !transformedType1.equals(transformedType2) : "transfromed file types should not be equal";
		
		assert transformedType1.hashCode() == transformedType3.hashCode() : "Hash codes should be the same";
		assert transformedType2.hashCode() != transformedType3.hashCode() : "Hash codes should not be the same";
	}

}
