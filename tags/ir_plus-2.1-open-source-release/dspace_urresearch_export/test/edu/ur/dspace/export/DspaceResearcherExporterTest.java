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

import edu.ur.dspace.model.DspaceResearcher;
import edu.ur.dspace.model.DspaceResearcherFolder;
import edu.ur.dspace.test.ContextHolder;

/**
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DspaceResearcherExporterTest {

	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	ResearcherExporter researcherExporter = (ResearcherExporter)ctx.getBean("researcherExporter");
	
	public void testGetResearchers() throws IOException
	{
		List<DspaceResearcher> researchers = researcherExporter.getResearchers();
		
		for( DspaceResearcher r : researchers)
		{
			System.out.println(r);
			System.out.println("Root = " + r.folder);
			printChildren(r.folder.children);
		}
		
	}
	
	public void testCreateXMLFile() throws IOException
	{
		researcherExporter.generateResearcherXMLFile(new File("C:\\researchers.xml"), researcherExporter.getResearchers());
	}
	
	private void printChildren(List<DspaceResearcherFolder> children)
	{
		
		    for(DspaceResearcherFolder child : children)
		    {
		    	System.out.print("child:" + child.title + " ");
		    }
		    System.out.print("\n");
		    for(DspaceResearcherFolder child : children)
		    {
		    	printChildren(child.children);
		    }
		
	}
}
