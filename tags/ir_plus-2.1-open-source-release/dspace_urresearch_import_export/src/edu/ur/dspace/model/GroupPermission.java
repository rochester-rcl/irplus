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
 * Represents a dspace permission
 * 
 * @author Nathan Sarr
 *
 */
public class GroupPermission {
	
    /** actions that can be performed*/
    public int action = -1;
    
     /** dspace id of the group  */
     public Long groupId;
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[ action id = ");
    	sb.append(action);
    	sb.append(" action name = ");
    	sb.append(PermissionConstants.getActionName(action));
    	sb.append(" group id = ");
    	sb.append( groupId );
    	sb.append("]");
    	return sb.toString();
    }

}
