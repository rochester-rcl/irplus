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
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.dspace.model.DspaceGroup;
import edu.ur.dspace.test.ContextHolder;

/**
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DspaceGroupExporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	/** Exporter for users */
	GroupExporter groupExporter = (GroupExporter)ctx.getBean("groupExporter");
	
	/**
	 * Try getting all the users from the database.
	 * 
	 * @throws IOException
	 */
	public void testGetGroups() throws IOException
	{
		List<DspaceGroup> groups = groupExporter.getGroups();
		
		for(DspaceGroup g : groups)
		{
			System.out.println("Group  g = " + g);
			for(Long userId : g.dspaceEpersonIds)
			{
				System.out.println("\t User id = " + userId);
			}
			
		}
		
		groupExporter.generateGroupXMLFile(new File("C:\\groups.xml"), groups);
	}

}
