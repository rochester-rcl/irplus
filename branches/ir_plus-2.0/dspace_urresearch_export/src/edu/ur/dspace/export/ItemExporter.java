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
import java.util.Collection;
import java.util.List;

import edu.ur.dspace.model.DspaceItem;

/**
 * Interface for exporting items.
 * 
 * @author Nathan Sarr
 *
 */
public interface ItemExporter {
	
	/**
	 * the file name for items
	 */
	public static final String XML_FILE_NAME = "item.xml";
	
	/**
	 * Get the items from the dspace database
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<DspaceItem> getItems() throws IOException;
	
	/**
	 * Get the items from the dspace database within the specified range
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<DspaceItem> getItems(int startId, int stopId) throws IOException;
	
	/**
	 * Generate an xml file with the specified items
	 * 
	 * @param f
	 * @param communities
	 * @throws IOException
	 */
	public void generateItemXMLFile(File f, Collection<DspaceItem> items) throws IOException;
	
	/**
	 * Export the communities and logos into a zip file
 	 *  
	 * @param zipFileName - name of the zip file.
	 * @param  directory to store the xmlFile in 
	 * 
	 * @return the created zip file.
	 * 
	 * @throws IOException
	 */
	public void exportItems(String zipFileName, String xmlFileDirectory, Collection<DspaceItem> items) throws IOException;
	
	/**
	 * Get the final item id.
	 * 
	 * @return
	 */
	public int getLastItemId();
	


}
