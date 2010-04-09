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

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import edu.ur.dspace.model.BitstreamFileInfo;
import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceItemMetadata;
import edu.ur.dspace.test.ContextHolder;

/**
 * @author Nathan Sarr
 *
 */
@Test(groups = { "baseTests" }, enabled = true)
public class DspaceItemExporterTest {
	
	/** get the application context */
	ApplicationContext ctx = ContextHolder.getApplicationContext();
	
	ItemExporter itemExporter = (ItemExporter)ctx.getBean("itemExporter");
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DspaceItemExporterTest.class);
	
	public void testGetItems() throws IOException
	{
		
		int batchSize = 10;
		
		int maxItemId = itemExporter.getLastItemId();
		
		int start = 0;
		int stop = batchSize - 1;
		int counter = 1;
		
		while(stop < maxItemId)
		{
			log.debug("Processing records start = " + start + " stop = " + stop );
			List<DspaceItem> items = itemExporter.getItems(start, stop);
			for( DspaceItem i : items)
			{
				log.debug("Processing Item "  + i);
				for(DspaceItemMetadata metadata : i.metadata)
				{
					log.debug("Metadata = "  + metadata);
				}
			
				for(BitstreamFileInfo file : i.files)
				{
					log.debug("File = " + file);
				}
				
				for(Long id : i.collectionIds)
				{
					log.debug("Collection id = " + id);
				} 
			}
			counter = counter + 1;
			start = stop + 1;
			stop = stop + batchSize;
		}
	}
	
	/**
	 * Test building the xml file
	 * 
	 * @throws IOException
	 */
	public void testBuildXmlFile() throws IOException
	{
		
		List<DspaceItem> items = itemExporter.getItems(1000, 1500);
		itemExporter.generateItemXMLFile(new File("C:\\items.xml"), items);
	}

}
