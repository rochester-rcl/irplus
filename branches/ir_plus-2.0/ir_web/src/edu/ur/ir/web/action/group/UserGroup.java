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

package edu.ur.ir.web.action.group;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;

/**
 * Manage a particular group
 * 
 * @author Nathan Sarr
 *
 */
public class UserGroup extends ActionSupport implements  Preparable {

	/** Eclipse generated id */
	private static final long serialVersionUID = -4765855803011968189L;

	/** id of the group */
	private Long id;
	
	/** id of the user */
	private Long userId;
	
	/** Service for users  */
	private UserService userService;
	
	/** Service for user groups */
	private UserGroupService userGroupService;
	
	/** User to add to the group */
	private IrUser user;
	
	/** User group to add the user to */
	private IrUserGroup userGroup;
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(UserGroup.class);

	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		
		log.debug("prepare called user id = " + userId + " id = " + id);
		
		if( userId != null)
		{
			user = userService.getUser(userId, false);
		}
		
		if( id != null)
		{
		    userGroup = userGroupService.get(id, false);
		}
	}
	
	/**
	 * Add user.
	 * 
	 * @return
	 */
	public String addUser()
	{
		log.debug("add user called user group = " + userGroup + " user = " + user);
		if( !userGroup.getUsers().contains(user))
		{
			userGroup.addUser(user);
		}
		
		userGroupService.save(userGroup);
		return SUCCESS;
	}
	
	/**
	 * Remove user.
	 * 
	 * @return
	 */
	public String removeUser()
	{
		if( userGroup.getUsers().contains(user))
		{
			userGroup.removeUser(user);
		}
		
		userGroupService.save(userGroup);
		return SUCCESS;
	}
	
	/**
	 * Add user.
	 * 
	 * @return
	 */
	public String addAdmin()
	{
		log.debug("add admin called user group = " + userGroup + " user = " + user);
		if( !userGroup.getAdministrators().contains(user))
		{
			userGroup.addAdministrator(user);
		}
		
		userGroupService.save(userGroup);
		return SUCCESS;
	}
	
	/**
	 * Remove user.
	 * 
	 * @return
	 */
	public String removeAdmin()
	{
		if( userGroup.getAdministrators().contains(user))
		{
			userGroup.removeAdministrator(user);
		}
		
		userGroupService.save(userGroup);
		return SUCCESS;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserGroupService getUserGroupService() {
		return userGroupService;
	}

	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public IrUser getUser() {
		return user;
	}

	public void setUser(IrUser user) {
		this.user = user;
	}

	public IrUserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}
}
