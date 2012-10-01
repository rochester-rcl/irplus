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
 * Record for a dspace group
 * 
 * @author Nathan Sarr
 *
 */
public class DspaceGroup {
	
	/** Name of the group  */
	public String groupName;
	
	/** Id of the group */
	public Long groupId;
	
	/** Users in the group  */
	public List<Long> dspaceEpersonIds = new LinkedList<Long>();
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(groupId);
		sb.append(" group name = ");
		sb.append(groupName);
		sb.append("]");
		
		return sb.toString();
	}

}
