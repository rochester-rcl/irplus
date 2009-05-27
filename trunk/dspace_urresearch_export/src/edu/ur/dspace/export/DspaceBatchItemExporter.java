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

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Batch exporter for dspace items.
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceBatchItemExporter {
	
	public static void main(String[] args) throws IOException
	{
		// name of the zip file to create for example "items" this will then
		// use this to crate files like 
		String zipFileName = args[0];
		
	     
		// path where the xml and zip files will be created
		String filePath = args[1];
		
		// batch size
		int batchSize = new Integer(args[2]).intValue();
		
		System.out.println("zip file name = " + zipFileName + " filePath = " + filePath + " batchSize = " + batchSize);
		
		/** get the application context */
		ApplicationContext ctx  = new ClassPathXmlApplicationContext(
		"applicationContext.xml");
		
		ItemExporter itemExporter = (ItemExporter)ctx.getBean("itemExporter");
		
		int maxItemId = itemExporter.getLastItemId();
		
		System.out.println("Max item id = " + maxItemId);
		
		boolean done = false;
		
		int start = 0;
		int stop = batchSize - 1;
		int counter = 1;
		
		if( maxItemId <= 0 )
		{
			done = true;
		}
		
		while(!done)
		{
			String newZipFileName = filePath + zipFileName +"_" + counter + ".zip";
			System.out.println("Processing records start = " + start + " stop = " + stop + " to file " + newZipFileName);
			itemExporter.exportItems(newZipFileName, filePath, itemExporter.getItems(start, stop));
			
			if(stop > maxItemId )
			{
				done = true;
			}
			
			counter = counter + 1;
			start = stop + 1;
			stop = stop + batchSize;
			
		}
	}

}
