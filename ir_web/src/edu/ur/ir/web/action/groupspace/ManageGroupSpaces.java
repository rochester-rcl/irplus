/**  
   Copyright 2008-2010 University of Rochester

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
package edu.ur.ir.web.action.groupspace;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.groupspace.GroupSpace;
import edu.ur.ir.groupspace.GroupSpaceService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Manage a group spaces.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageGroupSpaces extends Pager implements UserIdAware {

	/** eclipse generated id */
	private static final long serialVersionUID = 5292140116837950036L;
	
	/* Name for the content type */
	private String name;

	/* description for the content type */
	private String description;
	
	/* Logger */
	private static final Logger log = Logger.getLogger(ManageGroupSpaces.class);
	
	/* service for dealing with group space information */
	private GroupSpaceService groupSpaceService;

	/* id of the user trying to make changes */
	private Long userId;

	/* Service for dealing with group space information */
	private UserService userService;
	
	/* Group space object */
	private GroupSpace groupSpace;

	/* indicates successful action */
	private boolean success = true;
	
	/* list of groupspaces */
	private List<GroupSpace> groupSpaces;

	/* id of the groupspace to edit */
	private Long id;

	/** Message that can be displayed to the user. */
	private String message;

	public ManageGroupSpaces()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Initial load of all group spaces
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		groupSpaces = groupSpaceService.getGroupspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);
		return SUCCESS;
	}
	
	/**
	 * Get a specific group space.
	 * 
	 * @return
	 */
	public String get()
	{
		groupSpace = groupSpaceService.get(id, false);
	    return "get";
	}
	
	/**
	 * Delete the specified group space.
	 * 
	 * @return
	 */
	public String delete()
	{
		groupSpace = groupSpaceService.get(id,false);
		groupSpaceService.delete(groupSpace);
		groupSpaces = groupSpaceService.getGroupspacesNameOrder(rowStart, numberOfResultsToShow, OrderType.ASCENDING_ORDER);
		return "deleted";
	}
	
	/**
	 * Create a new user group.
	 * 
	 * @return
	 */
	public String create()
	{
		log.debug("creating a group space with name = " + name);
		IrUser user = userService.getUser(userId, false);
		
		if( user == null || !user.hasRole(IrRole.ADMIN_ROLE) || user.hasRole(IrRole.GROUP_SPACE_ROLE) )
		{
			return "accessDenied";
		}

		groupSpace = new GroupSpace(name, description);
		try 
		{
		    groupSpaceService.save(groupSpace);
		} 
		catch (DuplicateNameException e) 
		{
			success = false;
			message = getText("groupSpaceNameError", 
					new String[]{groupSpace.getName()});
			addFieldError("groupSpaceAlreadyExists", message);
		}
        return "added";
	}
	
	public String update()
	{
		log.debug("updating group space with name = " + name);
		return SUCCESS;

	}
	

	/**
	 * Get the total hits.
	 * 
	 * @see edu.ur.ir.web.table.Pager#getTotalHits()
	 */
	public int getTotalHits() {
		return groupSpaceService.getCount().intValue();
	}

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	/**
	 * Get the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set the group space service
	 * 
	 * @param groupSpaceService
	 */
	public void setGroupSpaceService(GroupSpaceService groupSpaceService) {
		this.groupSpaceService = groupSpaceService;
	}

	/**
	 * Indicates a successful action
	 * 
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	/**
	 * Get a list of the groupspaces.
	 * 
	 * @return
	 */
	public List<GroupSpace> getGroupSpaces() 
	{
		return groupSpaces;
	}
	
	/**
	 * Get the group space
	 * 
	 * @return
	 */
	public GroupSpace getGroupSpace() {
		return groupSpace;
	}

	/**
	 * Set the id of the group space to load.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}



}
