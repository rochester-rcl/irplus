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
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.LanguageTypeService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Interface component back end to allow a user to manage language types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageLanguageTypes extends Pager implements Preparable, UserIdAware{
	
	
	/**  Generated version id */
	private static final long serialVersionUID = -2827667086799910951L;

	/** language type service */
	private LanguageTypeService languageTypeService;
	
	/**  Logger for managing language types*/
	private static final Logger log = Logger.getLogger(ManageLanguageTypes.class);
	
	/** Set of language types for viewing the language types */
	private Collection<LanguageType> languageTypes;
	
	private LanguageType languageType = new LanguageType();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the language has been added*/
	private boolean added = false;
	
	/** Indicates the language types have been deleted */
	private boolean deleted = false;
	
	/** id of the language type  */
	private Long id;
	
	/** Set of language type ids */
	private long[] languageTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of content types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service for user information  */
	private UserService userService;
	
	/** id of the user making the change */
	private Long userId;
	
	/** Default constructor */
	public ManageLanguageTypes()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new language type.
	 * 
	 * Create a new language type
	 */
	public String create()
	{
		log.debug("creating a language type = " + languageType.getName());
		added = false;
		LanguageType myLanguageType = 
			languageTypeService.get(languageType.getName());
		if( myLanguageType == null)
		{
		    languageTypeService.save(languageType);
		    added = true;
		}
		else
		{
			message = getText("languageTypeAlreadyExists", 
					new String[]{languageType.getName()});
		}
        return "added";
	}
	
	/**
	 * Method to update an existing language type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing language type id = " + id);
		added = false;

		LanguageType other = 
			languageTypeService.get(languageType.getName());
		
		// if the language is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(languageType.getId()))
		{
			languageTypeService.save(languageType);
		    IrUser user = userService.getUser(userId, false);
		    institutionalItemVersionService.setAllVersionsAsUpdatedForLanguageType(languageType ,user, "Language Type - " + languageType + " Updated");			
			added = true;
		}
		else
		{
			message = getText("languageTypeAlreadyExists",
					new String[]{languageType.getName()});
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
		log.debug("Delete language types called");
		if( languageTypeIds != null )
		{
		    for(int index = 0; index < languageTypeIds.length; index++)
		    {
			    log.debug("Deleting language type with id " + languageTypeIds[index]);
			    languageTypeService.delete(languageTypeService.get(languageTypeIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the language type 
	 * 
	 * @return
	 */
	public String get()
	{
		languageTypeService.get(id, false);
		return "get";
	}
 

	
	/**
	 * Get the language types table data.
	 * 
	 * @return
	 */
	public String viewLanguageTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		languageTypes = languageTypeService.getLanguageTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = languageTypeService.getLanguageTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}
	
	/**
	 * Get the language type service.
	 * 
	 * @return
	 */
	public LanguageTypeService getLanguageTypeService() {
		return languageTypeService;
	}

	/**
	 * Set the language type service.
	 * 
	 * @param languageTypeService
	 */
	public void setLanguageTypeService(LanguageTypeService languageTypeService) {
		this.languageTypeService = languageTypeService;
	}
	
	/**
	 * List of language types for display.
	 * 
	 * @return
	 */
	public Collection<LanguageType> getLanguageTypes() {
		return languageTypes;
	}
	/**
	 * Set the list of language types.
	 * 
	 * @param languageTypes
	 */
	public void setLanguageTypes(Collection<LanguageType> languageTypes) {
		this.languageTypes = languageTypes;
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

	public long[] getLanguageTypeIds() {
		return languageTypeIds;
	}

	public void setLanguageTypeIds(long[] languageTypeIds) {
		this.languageTypeIds = languageTypeIds;
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
			languageType = languageTypeService.get(id, false);
		}
		
	}

	public LanguageType getLanguageType() {
		return languageType;
	}

	public void setLanguageType(LanguageType languageType) {
		this.languageType = languageType;
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
	 * id of the user making the change
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
