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

package edu.ur.ir.user;

import javax.naming.NamingException;

/**
 * This is a filter that can determine if a user should be added or removed from 
 * a given group.  It is up to the implementation of how to set the group and manage
 * all realated information.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserGroupMemberFilter {
	
	
	/**
	 * Get the group this is checking for.
	 * 
	 * @return the group this is a filter for.
	 */
	public IrUserGroup getUserGroup();
	
	/**
	 * Determines if the user is eligible to be a member of the group
	 * 
	 * @param user - user to check
	 * @param userGroup - user group to check
	 * @return true if the user is a valid member of the group.
	 * 
	 * @throws NamingException
	 */
	public boolean isEligibleGroupMember(IrUser user);

}
