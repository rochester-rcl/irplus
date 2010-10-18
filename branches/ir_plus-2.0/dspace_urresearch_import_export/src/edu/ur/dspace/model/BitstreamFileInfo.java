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
 * Simple bitstream record.
 * 
 * @author Nathan Sarr
 *
 */
public class BitstreamFileInfo {
	
	/** store number used in determining location */
	public int storeNumber;
	
	/** internal id used in determining location  */
	public String internal_id;
	
	/** file name including path in dspace  */
	public String dspaceFileName;
	
	/** new name to give to the file  */
	public String newFileName;
	
	/** file extension for the bitstream */
	public String extension;
	
	/** id of the bitstream */
	public Long bitstreamId;
	
	/** Description of the bitstream file */
	public String description;
	
	/** original name of the file when uploaded*/
	public String originalFileName;
	
	/** Group permissions for this community */
	public List<GroupPermission> groupPermissions = new LinkedList<GroupPermission>();
	
	/** Eperson permissions for this community */
	public List<EpersonPermission> epersonPermissions = new LinkedList<EpersonPermission>();
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ storeNumber = ");
		sb.append(storeNumber);
		sb.append(" internal_id = ");
		sb.append(internal_id);
		sb.append(" dspaceFileName = ");
		sb.append(dspaceFileName);
		sb.append(" bitstreamId = ");
		sb.append(bitstreamId);
		sb.append(" extension = ");
		sb.append(extension);
		sb.append(" original file name = ");
		sb.append(originalFileName);
		sb.append(" new file name = ");
		sb.append(newFileName);
		sb.append(" description = ");
		sb.append(description);
		sb.append("]");
		return sb.toString();
	}
}
