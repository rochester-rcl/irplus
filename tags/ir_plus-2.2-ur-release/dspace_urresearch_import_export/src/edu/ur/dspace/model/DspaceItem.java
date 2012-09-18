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

package edu.ur.dspace.model;

import java.util.LinkedList;
import java.util.List;



/**
 * Represents an item in dspace
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceItem {
	
	/** Dspace item id */
	public long itemId;
	
	/** indicates if the item is withdrawn */
	public boolean withdrawn;
	
	/** id of the submitter */
	public long submitterId;
	
	/** boolean indicating if the item is in the archive */
	public boolean inArchive;
	
	/** Metadata for the dspace item */
	public List<DspaceItemMetadata> metadata = new LinkedList<DspaceItemMetadata>();
	
	/** files for this item  */
	public List<BitstreamFileInfo> files = new LinkedList<BitstreamFileInfo>();
	
	/** list of collections for the item */
	public List<Long> collectionIds = new LinkedList<Long>();
	
	/** Group permissions for this item */
	public List<GroupPermission> groupPermissions = new LinkedList<GroupPermission>();
	
	/** Eperson permissions for this item */
	public List<EpersonPermission> epersonPermissions = new LinkedList<EpersonPermission>();
	
	/**
	 * Get the handle for the item else return null if not found.
	 * @return the found handle string in the form 
	 * 
	 * http://hdl.handle.net/[name-authority]/[local-name]
	 */
	public String getHandle()
	{
		List<String> uris = getMultipleDataForLabel(DspaceMetadataLabel.URI);
		
		for(String uri : uris)
		{
			//we've found a handle
			// it should be in the form: http://hdl.handle.net/[name-authority]/[local-name]
			if( uri.indexOf("http://hdl.handle.net/") > -1 )
			{
				return uri;
			}
		}
		return null;
	}
		
	/**
	 * This should be used for data that should be singular in the system.
	 * This returns the data for the found information or null if not found.
	 * 
	 * @param item - item to retrieve the data out of
	 * @param label - label for the data
	 * 
	 * @return the found data or null if nothing found for the specified label.
	 */
	public String getSingleDataForLabel(String label)
	{
		for(DspaceItemMetadata meta : metadata)
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
	public List<String> getMultipleDataForLabel( String label)
	{
		LinkedList<String> values = new LinkedList<String>();
			
		for(DspaceItemMetadata meta : metadata)
		{
			if( label.equalsIgnoreCase(meta.label))
			{
				values.add(meta.value);
			}
		}
			
		return values;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ item id = ");
		sb.append(itemId);
		sb.append(" withdrawn = ");
		sb.append(withdrawn);
		sb.append(" submitter id = ");
		sb.append(submitterId);
		sb.append(" in Archive = ");
		sb.append(inArchive);
		sb.append("]");
		return sb.toString();
	}

}
