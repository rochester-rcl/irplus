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

package edu.ur.ir.web.action.item.metadata;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ExtentType;
import edu.ur.ir.item.ExtentTypeService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Interface component back end to allow a user to manage extent types.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageExtentTypes extends Pager implements Preparable, UserIdAware{
	
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -2058233996922258747L;

	/** extent type service */
	private ExtentTypeService extentTypeService;
	
	/**  Logger for managing extent types*/
	private static final Logger log = Logger.getLogger(ManageExtentTypes.class);
	
	/** Set of extent types for viewing the extent types */
	private Collection<ExtentType> extentTypes;
	
	private ExtentType extentType = new ExtentType();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the extent has been added*/
	private boolean added = false;
	
	/** Indicates the extent types have been deleted */
	private boolean deleted = false;
	
	/** id of the extent type  */
	private Long id;
	
	/** Set of extent type ids */
	private long[] extentTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of extent types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** User id who is making the changes */
	private Long userId;
	
	/** Service for checking user information */
	private UserService userService;
	
	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	

	/** Default constructor */
	public ManageExtentTypes()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new extent type.
	 * 
	 * Create a new extent type
	 */
	public String create()
	{
		log.debug("creating a extent type = " + extentType.getName());
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		added = false;
		ExtentType myExtentType = 
			extentTypeService.getExtentType(extentType.getName());
		if( myExtentType == null)
		{
		    extentTypeService.saveExtentType(extentType);
		    added = true;
		}
		else
		{
			message = getText("extentTypeAlreadyExists", 
					new String[]{extentType.getName()});
		}
        return "added";
	}
	
	/**
	 * Method to update an existing extent type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing extent type id = " + extentType.getId());
		added = false;

		ExtentType other = 
			extentTypeService.getExtentType(extentType.getName());
		

		
		// if the item is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(extentType.getId()))
		{
			IrUser user = userService.getUser(userId, false);
			extentTypeService.saveExtentType(extentType);
			institutionalItemVersionService.setAllVersionsAsUpdatedForExtentType(extentType, user, "extent type - " + extentType + " updated ");
			added = true;
		}
		else
		{
			message = getText("extentTypeAlreadyExists",
					new String[]{extentType.getName()});
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete extent types called");
		if( extentTypeIds != null )
		{
		    for(int index = 0; index < extentTypeIds.length; index++)
		    {
			    log.debug("Deleting extent type with id " + extentTypeIds[index]);
			    extentTypeService.deleteExtentType(extentTypeIds[index]);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Load a specified extent type.
	 * 
	 * return "get" 
	 */
	public String get()
	{
		extentType = extentTypeService.getExtentType(id, false);
		return "get";
	}
 	
	/**
	 * Get the extent types table data.
	 * 
	 * @return
	 */
	public String viewExtentTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		extentTypes = extentTypeService.getExtentTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = extentTypeService.getExtentTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
	
	/**
	 * Get the extent type service.
	 * 
	 * @return
	 */
	public ExtentTypeService getExtentTypeService() {
		return extentTypeService;
	}

	/**
	 * Set the extent type service.
	 * 
	 * @param extentTypeService
	 */
	public void setExtentTypeService(ExtentTypeService extentTypeService) {
		this.extentTypeService = extentTypeService;
	}
	
	/**
	 * List of extent types for display.
	 * 
	 * @return
	 */
	public Collection<ExtentType> getExtentTypes() {
		return extentTypes;
	}
	/**
	 * Set the list of extent types.
	 * 
	 * @param extentTypes
	 */
	public void setExtentTypes(Collection<ExtentType> extentTypes) {
		this.extentTypes = extentTypes;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getExtentTypeIds() {
		return extentTypeIds;
	}

	public void setExtentTypeIds(long[] extentTypeIds) {
		this.extentTypeIds = extentTypeIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	public void prepare() throws Exception {
		if( id != null)
		{
			extentType = extentTypeService.getExtentType(id, false);
		}
		
	}

	public ExtentType getExtentType() {
		return extentType;
	}

	public void setExtentType(ExtentType extentType) {
		this.extentType = extentType;
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

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

}
