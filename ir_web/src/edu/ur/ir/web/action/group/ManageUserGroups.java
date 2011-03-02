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

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.IrUserGroup;
import edu.ur.ir.user.UserGroupService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;



/**
 * Action to help manage user groups.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageUserGroups extends Pager implements Preparable, UserIdAware {

	/** generated serial id */
	private static final long serialVersionUID = 5414305662558717897L;

	/** User group to manage. */
	private IrUserGroup userGroup;
	
	/** Id for the user group */
	private Long id;
	
	/** Service for dealing with user groups */
	private UserGroupService userGroupService;
	
	/**  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageUserGroups.class);
	
	/** indicates the user group was added */
	private boolean added;
	
	/** indicates a user group delete action was successful */
	private boolean deleted;
	
	/** message to be displayed to the user */
	private String message;
	
	/** Set of content type ids */
	private long[] userGroupIds;
	
	/** Set of user groups for viewing */
	private Collection<IrUserGroup> userGroups;

	/** User id  */
	private Long userId;
	
	/** User service */
	private UserService userService;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of user groups  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageUserGroups() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	public String create()
	{
		log.debug("creating a user group type = " + userGroup.getName());
		added = false;
		IrUserGroup other = userGroupService.get(userGroup.getName());
		if( other == null)
		{
			IrUser user = userService.getUser(userId, false);
			userGroup.addAdministrator(user);
			userGroupService.save(userGroup);
		    added = true;
		}
		else
		{
			message = getText("userGroupAlreadyExists", 
					new String[]{userGroup.getName()});
			addFieldError("userGroupAlreadyExists", message);
		}
        
		return "added";
	}
	
	
	
	/**
	 * View the specified user group.
	 * 
	 * @return view user group
	 */
	public String view()
	{
		if( userGroup == null)
		{
			throw new IllegalStateException("user group is null");
		}
		return "view";
	}
	
	/**
	 * Method to update an existing user group
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing user group id = " + userGroup.getId());
		added = false;

		IrUserGroup other = userGroupService.get(userGroup.getName());
		
		if( other == null || other.getId().equals(userGroup.getId()))
		{
			userGroupService.save(userGroup);
			added = true;
		}
		else
		{
			message = getText("userGroupAlreadyExists", 
					new String[]{userGroup.getName()});
			
			addFieldError("userGroupAlreadyExists", message);
		}
        return "updated";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete user groups types called");
		if( userGroupIds != null )
		{
		    for(int index = 0; index < userGroupIds.length; index++)
		    {
			    log.debug("Deleting content type with id " + userGroupIds[index]);
			    IrUserGroup userGroup = userGroupService.get(userGroupIds[index], false);
			    userGroupService.delete(userGroup);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	
	public void prepare() throws Exception {
		if( id != null)
		{
			userGroup = userGroupService.get(id, false);
		}
		
	}

	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}
	
	public IrUserGroup getUserGroup() {
		return userGroup;
	}


	public void setUserGroup(IrUserGroup userGroup) {
		this.userGroup = userGroup;
	}


	public UserGroupService getUserGroupService() {
		return userGroupService;
	}


	public void setUserGroupService(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long userGroupId) {
		this.id = userGroupId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Get the content types table data.
	 * 
	 * @return
	 */
	public String viewUserGroups()
	{
	
		rowEnd = rowStart + numberOfResultsToShow;
	    
		userGroups = userGroupService.getUserGroupsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = userGroupService.getUserGroupsCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}


	public Collection<IrUserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Collection<IrUserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public long[] getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(long[] userGroupIds) {
		this.userGroupIds = userGroupIds;
	}



	public void setUserId(Long userId) {
		this.userId = userId;
	}



	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}


}
