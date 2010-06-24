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
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.IdentifierTypeService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * User Interface component back end to allow a user to manage identifier types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageIdentifierTypes extends Pager implements  Preparable, UserIdAware{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 3855604743926070616L;

	/** identifier type service */
	private IdentifierTypeService identifierTypeService;

	/**  Logger for managing identifier types*/
	private static final Logger log = Logger.getLogger(ManageIdentifierTypes.class);
	
	/** Set of identifier types for viewing the identifier types */
	private Collection<IdentifierType> identifierTypes;
	
	private IdentifierType identifierType = new IdentifierType();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the identifier has been added*/
	private boolean added = false;
	
	/** Indicates the identifier types have been deleted */
	private boolean deleted = false;
	
	/** id of the identifier type  */
	private Long id;
	
	/** Set of identifier type ids */
	private long[] identifierTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of identifier types */
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
	public ManageIdentifierTypes() 
	{
	
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new identifier type.
	 * 
	 * Create a new identifier type
	 */
	public String create()
	{
		log.debug("creating a identifier type = " + identifierType.getName());
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		added = false;
		IdentifierType myIdentifierType = 
			identifierTypeService.get(identifierType.getName());
		if( myIdentifierType == null)
		{
		    identifierTypeService.save(identifierType);
		    added = true;
		}
		else
		{
			message = getText("identifierTypeAlreadyExists", 
					new String[]{identifierType.getName()});
		}
        return "added";
	}
	
	/**
	 * Get an identifier type
	 * 
	 * @return the identifier type
	 */
	public String get()
	{
		identifierType = identifierTypeService.get(id, false);
		return "get";
	}
	
	/**
	 * Method to update an existing identifier type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing identifier type id = " + identifierType.getId());
		added = false;

		IdentifierType other = 
			identifierTypeService.get(identifierType.getName());
		

		
		// if the item is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(identifierType.getId()))
		{
			identifierTypeService.save(identifierType);
		    IrUser user = userService.getUser(userId, false);
		    institutionalItemVersionService.setAllVersionsAsUpdatedForIdentifierType(identifierType ,user, "Identifier Type - " + identifierType + " Updated");
		    added = true;
		}
		else
		{
			message = getText("identifierTypeAlreadyExists",
					new String[]{identifierType.getName()});
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
		log.debug("Delete identifier types called");
		if( identifierTypeIds != null )
		{
		    for(int index = 0; index < identifierTypeIds.length; index++)
		    {
			    log.debug("Deleting identifier type with id " + identifierTypeIds[index]);
			    identifierTypeService.delete(identifierTypeService.get(identifierTypeIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
 

	
	/**
	 * Get the identifier types table data.
	 * 
	 * @return
	 */
	public String viewIdentifierTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		identifierTypes = identifierTypeService.getIdentifierTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = identifierTypeService.getIdentifierTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
		
	/**
	 * Get the identifier type service.
	 * 
	 * @return
	 */
	public IdentifierTypeService getIdentifierTypeService() {
		return identifierTypeService;
	}

	/**
	 * Set the identifier type service.
	 * 
	 * @param identifierTypeService
	 */
	public void setIdentifierTypeService(IdentifierTypeService identifierTypeService) {
		this.identifierTypeService = identifierTypeService;
	}
	
	/**
	 * List of identifier types for display.
	 * 
	 * @return
	 */
	public Collection<IdentifierType> getIdentifierTypes() {
		return identifierTypes;
	}
	/**
	 * Set the list of identifier types.
	 * 
	 * @param identifierTypes
	 */
	public void setIdentifierTypes(Collection<IdentifierType> identifierTypes) {
		this.identifierTypes = identifierTypes;
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

	public long[] getIdentifierTypeIds() {
		return identifierTypeIds;
	}

	public void setIdentifierTypeIds(long[] identifierTypeIds) {
		this.identifierTypeIds = identifierTypeIds;
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
			identifierType = identifierTypeService.get(id, false);
		}
		
	}

	public IdentifierType getIdentifierType() {
		return identifierType;
	}

	public void setIdentifierType(IdentifierType identifierType) {
		this.identifierType = identifierType;
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
