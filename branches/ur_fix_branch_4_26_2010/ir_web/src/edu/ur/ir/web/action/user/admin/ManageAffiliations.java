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


package edu.ur.ir.web.action.user.admin;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.Affiliation;
import edu.ur.ir.user.AffiliationService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with affiliations.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageAffiliations extends Pager implements   Preparable{
	
	/** generated version id. */
	private static final long serialVersionUID = -3229962214403823020L;
	
	/** affiliation service */
	private AffiliationService affiliationService;
	
	/**  Logger for managing affiliations*/
	private static final Logger log = Logger.getLogger(ManageAffiliations.class);
	
	/** Set of affiliations for viewing the affiliations */
	private Collection<Affiliation> affiliations;
	
	/**  Affiliation for loading  */
	private Affiliation affiliation;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the affiliation has been added*/
	private boolean added = false;
	
	/** Indicates the affiliations have been deleted */
	private boolean deleted = false;
	
	/** id of the affiliation  */
	private Long id;
	
	/** Set of affiliation ids */
	private long[] affiliationIds;

	/**  Indicates whether the affiliation has a author permissions */
	private boolean author = false;

	/**  Indicates whether the affiliation has a researcher permissions */
	private boolean researcher = false;

	/**  Indicates whether the affiliation needs to be approved by admin */
	private boolean needsApproval = false;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of affiliations  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageAffiliations()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new affiliation.
	 * 
	 * Create a new affiliation
	 */
	public String create()
	{
		log.debug("creating a affiliation = " + affiliation.getName());
		added = false;
		Affiliation other = affiliationService.getAffiliation(affiliation.getName());
		if( other == null)
		{
			affiliation.setAuthor(author);
			affiliation.setResearcher(researcher);
			affiliation.setNeedsApproval(needsApproval);
		    affiliationService.makeAffiliationPersistent(affiliation);
		    added = true;
		}
		else
		{
			message = getText("affiliationAlreadyExists", 
					new String[]{affiliation.getName()});
			addFieldError("affiliationAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing affiliation.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing affiliation id = " + affiliation.getId());
		added = false;

		Affiliation other = affiliationService.getAffiliation(affiliation.getName());
		
		if( other == null || other.getId().equals(affiliation.getId()))
		{
			affiliation.setAuthor(author);
			affiliation.setResearcher(researcher);
			affiliation.setNeedsApproval(needsApproval);
			affiliationService.makeAffiliationPersistent(affiliation);
			added = true;
		}
		else
		{
			message = getText("affiliationAlreadyExists", 
					new String[]{affiliation.getName()});
			
			addFieldError("affiliationAlreadyExists", message);
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
		log.debug("Delete affiliations called");
		if( affiliationIds != null )
		{
		    for(int index = 0; index < affiliationIds.length; index++)
		    {
			    log.debug("Deleting affiliation with id " + affiliationIds[index]);
			    affiliationService.deleteAffiliation(affiliationService.getAffiliation(affiliationIds[index], false));
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the affiliation
	 * 
	 * @return
	 */
	public String get()
	{
	    affiliation = affiliationService.getAffiliation(id, false);
	    return "get";
	}
 

	
	/**
	 * Get the affiliations table data.
	 * 
	 * @return
	 */
	public String viewAffiliations()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		affiliations = affiliationService.getAffiliationsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = affiliationService.getAffiliationCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}


	/**
	 * Get the affiliation service.
	 * 
	 * @return
	 */
	public AffiliationService getAffiliationService() {
		return affiliationService;
	}

	/**
	 * Set the affiliation service.
	 * 
	 * @param affiliationService
	 */
	public void setAffiliationService(AffiliationService affiliationService) {
		this.affiliationService = affiliationService;
	}
	
	/**
	 * List of affiliations for display.
	 * 
	 * @return
	 */
	public Collection<Affiliation> getAffiliations() {
		return affiliations;
	}
	/**
	 * Set the list of affiliations.
	 * 
	 * @param affiliations
	 */
	public void setAffiliations(Collection<Affiliation> affiliations) {
		this.affiliations = affiliations;
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

	public long[] getAffiliationIds() {
		return affiliationIds;
	}

	public void setAffiliationIds(long[] affiliationIds) {
		this.affiliationIds = affiliationIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			affiliation = affiliationService.getAffiliation(id, false);
		}
	}

	public boolean getAuthor() {
		return author;
	}

	public void setAuthor(boolean author) {
		this.author = author;
	}

	public boolean getResearcher() {
		return researcher;
	}

	public void setResearcher(boolean researcher) {
		this.researcher = researcher;
	}

	public boolean isNeedsApproval() {
		return needsApproval;
	}

	public void setNeedsApproval(boolean needsApproval) {
		this.needsApproval = needsApproval;
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
