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
 * Represents a permission given directly to an 
 * eperson.
 * 
 * @author Nathan Sarr
 *
 */
public class EpersonPermission {
	
	/** actions that can be performed*/
    public int action = -1;
    
    /** dspace id of the group  */
    public Long epersonId;
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[ action id = ");
    	sb.append(action);
    	sb.append(" action name = ");
    	sb.append(PermissionConstants.getActionName(action));
    	sb.append(" eperson id = ");
    	sb.append( epersonId );
    	sb.append("]");
    	return sb.toString();
    }


}
