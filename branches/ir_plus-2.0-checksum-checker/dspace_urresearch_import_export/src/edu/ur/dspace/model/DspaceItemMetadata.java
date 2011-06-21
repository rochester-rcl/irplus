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

/**
 * Represents a singl label/value pair of data
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceItemMetadata {
	
	/** id for the dc value */
	public long dcValueId;
	
    /** label from dc type registry */
    public String label;
    
    /** text value from dc value table */
    public String value;
    
    /** item id for the metadata */
    public Long itemId;
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[ dc value id = ");
    	sb.append(dcValueId);
    	sb.append(" label = ");
    	sb.append(label);
    	sb.append(" value = ");
    	sb.append(value);
    	sb.append(" item id = ");
    	sb.append(itemId);
    	sb.append("]");
    	return sb.toString();
    }

}
