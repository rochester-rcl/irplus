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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.PlaceOfPublicationService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Manage places of publication 
 * 
 * @author Nathan Sarr
 *
 */
public class ManagePlacesOfPublication  extends Pager implements Preparable, UserIdAware{
	
	// Eclipse generated id
	private static final long serialVersionUID = -5550537990846298852L;

	/** place of publication service */
	private PlaceOfPublicationService placeOfPublicationService;
	
	/**  Logger for managing place of publications*/
	private static final Logger log = LogManager.getLogger(ManagePlacesOfPublication.class);
	
	/** Set of place of publications for viewing the place of publications */
	private Collection<PlaceOfPublication> placesOfPublication;
	
	private PlaceOfPublication placeOfPublication = new PlaceOfPublication();
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the language has been added*/
	private boolean added = false;
	
	/** id of the place of publication  */
	private Long id;
	
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
	public ManagePlacesOfPublication()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new place of publication.
	 * 
	 * Create a new place of publication
	 */
	public String create()
	{
		log.debug("creating a place of publication = " + placeOfPublication.getName());
		added = false;
		PlaceOfPublication myPlaceOfPublication = 
			placeOfPublicationService.get(placeOfPublication.getName());
		if( myPlaceOfPublication == null)
		{
		    placeOfPublicationService.save(placeOfPublication);
		    added = true;
		}
		else
		{
			message = getText("placeOfPublicationAlreadyExists", 
					new String[]{placeOfPublication.getName()});
			addFieldError("placeOfPublicationAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Sets up page for editing a place of publication.
	 * 
	 * @return edit
	 */
	public String edit()
	{
		return "edit";
	}
	
	/**
	 * Method to update an existing place of publication.
	 * 
	 * @return
	 */
	public String save()
	{
		log.debug("updateing place of publication id = " + id);
		added = false;

		if(placeOfPublication.getName() == null || placeOfPublication.getName().trim().equals(""))
		{
			message = getText("placeOfPublicationMustHaveName");
			return INPUT;
		}
		
		PlaceOfPublication other = 
			placeOfPublicationService.get(placeOfPublication.getName());
		
		
		
		// if the language is found and the id's are not the same
		// then they are trying to rename it to the same name.
		if(other == null || other.getId().equals(placeOfPublication.getId()))
		{
			placeOfPublicationService.save(placeOfPublication);
		    IrUser user = userService.getUser(userId, false);
		    institutionalItemVersionService.setAllVersionsAsUpdatedForPlaceOfPublication(placeOfPublication ,user, "Place of publication - " + placeOfPublication + " Updated");			
			added = true;
		}
		else
		{
			message = getText("placeOfPublicationAlreadyExists",
					new String[]{placeOfPublication.getName()});
			addFieldError("placeOfPublicationAlreadyExists", message);
			return INPUT;
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
		log.debug("Delete place of publications called");
		if( id != null )
		{
	        placeOfPublicationService.delete(placeOfPublicationService.get(id, false));
		}
		return "deleted";
	}
	

	
	/**
	 * Get the place of publications table data.
	 * 
	 * @return
	 */
	public String viewPlacesOfPublication()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		placesOfPublication = placeOfPublicationService.getOrderByName(rowStart, 
	    		numberOfResultsToShow, OrderType.getOrderType(sortType));
	    totalHits = placeOfPublicationService.getCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}
	

	/**
	 * Set the place of publication service.
	 * 
	 * @param placeOfPublicationService
	 */
	public void setPlaceOfPublicationService(PlaceOfPublicationService placeOfPublicationService) {
		this.placeOfPublicationService = placeOfPublicationService;
	}
	
	/**
	 * List of place of publications for display.
	 * 
	 * @return
	 */
	public Collection<PlaceOfPublication> getPlacesOfPublication() {
		return placesOfPublication;
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public void prepare() throws Exception {
		log.debug("preparing place of publication with id " + id);
		if( id != null)
		{
			placeOfPublication = placeOfPublicationService.get(id, false);
			log.debug("loaded place of publication " + placeOfPublication);
		}
		
	}

	public PlaceOfPublication getPlaceOfPublication() {
		return placeOfPublication;
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
