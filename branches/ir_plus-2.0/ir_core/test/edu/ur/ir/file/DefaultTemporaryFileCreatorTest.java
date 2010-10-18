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

import java.io.File;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Test;

/**
 * Tests creating temporary files.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DefaultTemporaryFileCreatorTest {
	
	/** Application context for loading bean specific information */
	ApplicationContext ctx = new ClassPathXmlApplicationContext(
			"applicationContext.xml");
	
	// get the temporary file creator
	TemporaryFileCreator temporaryFileCreator = 
		(TemporaryFileCreator)ctx.getBean("temporaryFileCreator");

	
	/**
	 * Make sure that a temporary file is created and can be used.
	 * 
	 * @throws IOException
	 */
	public void createTemporaryFileTest() throws IOException
	{
		File f = temporaryFileCreator.createTemporaryFile("jpg");
		assert f.exists() : "The file should exist";
		assert f.canRead() : "Should be able to read the file";
		assert f.canWrite() : "Should be able to wite to the directory";
		
		assert f.delete();
	}

}
