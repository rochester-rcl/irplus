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


package edu.ur.tag.repository;


import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;

/**
 * Functions for dealing with groups
 * 
 * @author Nathan Sarr
 *
 */
public class GroupWebUtilFunctions {
	
	
	/**
	 * Returns true if the user is part of the specified group.
	 * 
	 * @param user - user to check
	 * @param userGroup - user group to check.
	 * 
	 * @return true if the user is found within the group.
	 */
	public static boolean isInGroup(IrUser user, IrUserGroup userGroup)
	{
		return userGroup.getUsers().contains(user);
	}
	
	/**
	 * Returns true if the user is an admin of the specified group.
	 * 
	 * @param user  - user to check to see if they are and administrator
	 * @param userGroup - user group to check
	 * @return - true if the user is an admin of the group.
	 */
	public static boolean isAdminOfGroup(IrUser user, IrUserGroup userGroup)
	{
		return userGroup.getAdministrators().contains(user);
	}

}
