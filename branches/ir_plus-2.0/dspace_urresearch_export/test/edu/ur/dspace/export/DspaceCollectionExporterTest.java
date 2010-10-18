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

import edu.ur.dspace.model.DspaceCollection;
import edu.ur.dspace.test.ContextHolder;

/**
 * Class to help with testing collection export.
 * 
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DspaceCollectionExporterTest {
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	CollectionExporter collectionExporter = (CollectionExporter)ctx.getBean("collectionExporter");
	
	public void testGetCollections() throws IOException
	{
		List<DspaceCollection> collections = collectionExporter.getCollections();
		
		for( DspaceCollection c : collections)
		{
			System.out.println(c);
		}
	}
	
	public void testGenerateCollectionXMLFile() throws IOException
	{
		collectionExporter.generateCollectionXMLFile(new File("C:\\collections.xml"), collectionExporter.getCollections());
	}
	
}
