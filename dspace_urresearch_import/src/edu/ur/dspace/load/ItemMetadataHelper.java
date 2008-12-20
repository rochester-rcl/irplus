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

package edu.ur.dspace.load;

import java.util.LinkedList;
import java.util.List;

import edu.ur.dspace.model.DspaceItem;
import edu.ur.dspace.model.DspaceItemMetadata;

/**
 * Helps retrieve item metadata based from an item
 * 
 * @author Nathan Sarr.
 *
 */
public class ItemMetadataHelper {
	
	/**
	 * This should be used for data that should be singular in the system.
	 * This returns the data for the found information or null if not found.
	 * 
	 * @param item - item to retrieve the data out of
	 * @param label - label for the data
	 * 
	 * @return the found data or null if nothing found for the specified label.
	 */
	public static String getSingleDataForLabel(DspaceItem item, String label)
	{
		for(DspaceItemMetadata meta : item.metadata)
		{
			if( label.equalsIgnoreCase(meta.label))
			{
				return meta.value;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list of data of all metadata found with the specified label.  
	 * 
	 * @param item - item that has the data
	 * @param label - lable the metatadata must have.
	 * 
	 * @return - list of data for the found metadata or an empty list if no data
	 * with the label is found.
	 */
	public static List<String> getMultipleDataForLabel(DspaceItem item, String label)
	{
		LinkedList<String> values = new LinkedList<String>();
		
		for(DspaceItemMetadata meta : item.metadata)
		{
			if( label.equalsIgnoreCase(meta.label))
			{
				values.add(meta.value);
			}
		}
		
		return values;
	}

}
