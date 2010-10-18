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

package edu.ur.dspace.export;



import java.io.File;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.dspace.test.ContextHolder;

/**
 * Class to help with testing community export
 * 
 * @author Nathan Sarr 
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DspaceCommunityExporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	CommunityExporter communityExporter = (CommunityExporter)ctx.getBean("communityExporter");
	
	/**
	 * Test generating the xml file
	 * 
	 * @throws IOException
	 */
	public void testGenerateCommunityXMLFile() throws IOException
	{
		communityExporter.generateCommunityXMLFile(new File("C:\\communities.xml"), communityExporter.getCommunities());
	}
	

}
