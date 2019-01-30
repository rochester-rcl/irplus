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

package edu.ur.ir.web.action.person;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.ContributorTypeService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Interface component back end to allow a administrators to manage contributor types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageContributorTypes extends Pager implements Preparable, UserIdAware{
	
	/** id of the user making the change */
	private Long userId;
	
	/** eclipse generated id */
	private static final long serialVersionUID = 4662051034268805175L;

	/** contributor type service */
	private ContributorTypeService contributorTypeService;
	
	/**  Logger for managing contributor types*/
	private static final Logger log = Logger.getLogger(ManageContributorTypes.class);
	
	/** Set of contributor types for viewing the contributor types */
	private Collection<ContributorType> contributorTypes;
	
	private ContributorType contributorType = new ContributorType();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the contributor has been added*/
	private boolean added = false;
	
	/** Indicates the contributor types have been deleted */
	private boolean deleted = false;
	
	/** id of the contributor type */
	private Long id;
	
	/** Set of contributor type ids */
	private long[] contributorTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number ofcontributor types  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** determine if the contributor type is an authoring type */
	private boolean authoringType = false;

	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service for user information  */
	private UserService userService;
	
	/** Default constructor */
	public  ManageContributorTypes()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	/**
	 * Method to create a new contributor type.
	 * 
	 * Create a new contributor type
	 */
	public String create()
	{
		log.debug("creating a contributor type = " + contributorType.getName());
		added = false;
		ContributorType myContributorType = 
			contributorTypeService.get(contributorType.getName());
		if( myContributorType == null)
		{
			contributorType.setAuthorType(authoringType);
		    contributorTypeService.save(contributorType);
		    added = true;
		}
		else
		{
			message = getText("contributorTypeNameError", 
					new String[]{contributorType.getName()});
		}
        return "added";
	}
	
	/**
	 * Method to update an existing contributor type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing contributor type id = " + contributorType.getId());
		added = false;

		ContributorType other = 
			contributorTypeService.get(contributorType.getName());
	
		// if the item is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(contributorType.getId()))
		{
			contributorType.setAuthorType(authoringType);
			contributorTypeService.save(contributorType);
		    IrUser user = userService.getUser(userId, false);
		    institutionalItemVersionService.setAllVersionsAsUpdatedForContributorType(contributorType, user, "Contributor Type Updated");
			added = true;
		}
		else
		{
			message = getText("contributorTypeNameError",
					new String[]{contributorType.getName()});
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
		log.debug("Delete contributor types called");
		if( contributorTypeIds != null )
		{
		    for(int index = 0; index < contributorTypeIds.length; index++)
		    {
			    log.debug("Deleting contributor type with id " + contributorTypeIds[index]);
			    contributorTypeService.delete(contributorTypeService.get(contributorTypeIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the contributor type
	 * 
	 * @return get
	 */
	public String get()
	{
		contributorType = contributorTypeService.get(id, false);
		return "get";
	}
 

	
	/**
	 * Get the contributor types table data.
	 * 
	 * @return
	 */
	public String viewContributorTypes()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		contributorTypes = contributorTypeService.getContributorTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = contributorTypeService.getContributorTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}

	/**
	 * Get the contributor type service.
	 * 
	 * @return
	 */
	public ContributorTypeService getContributorTypeService() {
		return contributorTypeService;
	}

	/**
	 * Set the contributor type service.
	 * 
	 * @param contributorTypeService
	 */
	public void setContributorTypeService(ContributorTypeService contributorTypeService) {
		this.contributorTypeService = contributorTypeService;
	}
	
	/**
	 * List of contributor types for display.
	 * 
	 * @return
	 */
	public Collection<ContributorType> getContributorTypes() {
		return contributorTypes;
	}
	/**
	 * Set the list of contributor types.
	 * 
	 * @param contributorTypes
	 */
	public void setContributorTypes(Collection<ContributorType> contributorTypes) {
		this.contributorTypes = contributorTypes;
	}

	/**
	 * Message to be returned to UI.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Set the message to be returned to the UI.
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns true if the contributor type has been added.
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}
	
	/**
	 * Indicates if the contributor type has been added.
	 * 
	 * @param added
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Get the id for the contributor type.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id for the contributor type to be modified.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get the list of contributor type ids.
	 * 
	 * @return
	 */
	public long[] getContributorTypeIds() {
		return contributorTypeIds;
	}

	/**
	 * Set the list of contributor type ids.
	 * 
	 * @param contributorTypeIds
	 */
	public void setContributorTypeIds(long[] contributorTypeIds) {
		this.contributorTypeIds = contributorTypeIds;
	}

	/**
	 * Boolean that indicates if the selected contributor types have been
	 * deleted.
	 * 
	 * @return
	 */
	public boolean getDeleted() {
		return deleted;
	}

	/**
	 * Set boolean to indicate contributor types have been deleted.
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		if( id != null)
		{
			contributorType = contributorTypeService.get(id, false);
		}
		
	}

	/**
	 * Get the contributor type.
	 * 
	 * @return
	 */
	public ContributorType getContributorType() {
		return contributorType;
	}

	/**
	 * Set the contributor type.
	 * 
	 * @param contributorType
	 */
	public void setContributorType(ContributorType contributorType) {
		this.contributorType = contributorType;
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
	
	/**
	 * Set to true if the user is an authoring user
	 * @param authoringType
	 */
	public void setAuthoringType(boolean authoringType) {
		this.authoringType = authoringType;
	}

	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Set the user id
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
