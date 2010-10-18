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
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;
import edu.ur.order.OrderType;

/**
 * Action to deal with sponsors.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageSponsors extends Pager implements Preparable, UserIdAware{
	
	/** generated version id. */
	private static final long serialVersionUID = -8370650961037267346L;

	/** sponsor service */
	private SponsorService sponsorService;
	
	/**  Logger for managing sponsors*/
	private static final Logger log = Logger.getLogger(ManageSponsors.class);
	
	/** Set of sponsors for viewing the sponsors */
	private Collection<Sponsor> sponsors;
	
	/**  sponsor for loading  */
	private Sponsor sponsor;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the sponsor has been added*/
	private boolean added = false;
	
	/** Indicates the sponsors have been deleted */
	private boolean deleted = false;
	
	/** id of the sponsor  */
	private Long id;
	
	/** Set of sponsor ids */
	private long[] sponsorIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of sponsor */
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
	public ManageSponsors()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new sponsor.
	 * 
	 * Create a new sponsor
	 */
	public String create()
	{
		log.debug("creating a sponsor = " + sponsor.getName());
	
		IrUser user = userService.getUser(userId, false);
		if( user == null || !(user.hasRole(IrRole.AUTHOR_ROLE) || user.hasRole(IrRole.ADMIN_ROLE)) )
		{
		    return "accessDenied";	
		}
		
		added = false;
		Sponsor other = sponsorService.get(sponsor.getName());
		if( other == null)
		{
		    sponsorService.save(sponsor);
		    added = true;
		}
		else
		{
			message = getText("sponsorAlreadyExists", 
					new String[]{sponsor.getName()});
			addFieldError("sponsorAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing sponsor.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing sponsor id = " + sponsor.getId());
		added = false;

		Sponsor other = sponsorService.get(sponsor.getName());
		
		if( other == null || other.getId().equals(sponsor.getId()))
		{
			sponsorService.save(sponsor);
			IrUser user = userService.getUser(userId, false);
			institutionalItemVersionService.setAllVersionsAsUpdatedForSponsor(sponsor, user, "sponsor - " + sponsor + " Updated");
			added = true;
		}
		else
		{
			message = getText("sponsorAlreadyExists", 
					new String[]{sponsor.getName()});
			
			addFieldError("sponsorAlreadyExists", message);
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
		log.debug("Delete sponsors called");
		if( sponsorIds != null )
		{
		    for(int index = 0; index < sponsorIds.length; index++)
		    {
			    log.debug("Deleting sponsor with id " + sponsorIds[index]);
			    Sponsor s = sponsorService.get(sponsorIds[index], false);
			    sponsorService.delete(s);
		    }
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the sponsor 
	 * 
	 * @return
	 */
    public String get()
    {
    	sponsor = sponsorService.get(id, false);
    	return "get";
    }
	
	/**
	 * Get the sponsors table data.
	 * 
	 * @return
	 */
	public String viewSponsors()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		sponsors = sponsorService.getOrderByName(rowStart, 
	    		numberOfResultsToShow, OrderType.getOrderType(sortType));
	    totalHits = sponsorService.getCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	} 
	
	/**
	 * Get the sponsor service.
	 * 
	 * @return
	 */
	public SponsorService getSponsorService() {
		return sponsorService;
	}

	/**
	 * Set the sponsor service.
	 * 
	 * @param sponsorService
	 */
	public void setSponsorService(SponsorService sponsorService) {
		this.sponsorService = sponsorService;
	}
	
	/**
	 * List of sponsors for display.
	 * 
	 * @return
	 */
	public Collection<Sponsor> getSponsors() {
		return sponsors;
	}
	/**
	 * Set the list of sponsors.
	 * 
	 * @param sponsors
	 */
	public void setSponsors(Collection<Sponsor> sponsors) {
		this.sponsors = sponsors;
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

	public long[] getSponsorIds() {
		return sponsorIds;
	}

	public void setSponsorIds(long[] sponsorIds) {
		this.sponsorIds = sponsorIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Sponsor getSponsor() {
		return sponsor;
	}

	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			sponsor = sponsorService.get(id, false);
		}
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

	
	public void setUserId(Long userId) {
		this.userId = userId;	
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userSerice) {
		this.userService = userSerice;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}

}
