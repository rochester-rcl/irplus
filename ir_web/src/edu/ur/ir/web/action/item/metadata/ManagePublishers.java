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

import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with publishers.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManagePublishers extends Pager implements Preparable{
	
	/** generated version id. */
	private static final long serialVersionUID = -8370650961037267346L;

	/** publisher service */
	private PublisherService publisherService;
	
	/**  Logger for managing publishers*/
	private static final Logger log = Logger.getLogger(ManagePublishers.class);
	
	/** Set of publishers for viewing the publishers */
	private Collection<Publisher> publishers;
	
	/**  publisher for loading  */
	private Publisher publisher;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the publisher has been added*/
	private boolean added = false;
	
	/** Indicates the publishers have been deleted */
	private boolean deleted = false;
	
	/** id of the publisher  */
	private Long id;
	
	/** Set of publisher ids */
	private long[] publisherIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of content types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public ManagePublishers() {
	
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Method to create a new publisher.
	 * 
	 * Create a new publisher
	 */
	public String create()
	{
		log.debug("creating a publisher = " + publisher.getName());
		added = false;
		Publisher other = publisherService.getPublisher(publisher.getName());
		if( other == null)
		{
		    publisherService.savePublisher(publisher);
		    added = true;
		}
		else
		{
			message = getText("publisherAlreadyExists", 
					new String[]{publisher.getName()});
			addFieldError("publisherAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing publisher.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing publisher id = " + publisher.getId());
		added = false;

		Publisher other = publisherService.getPublisher(publisher.getName());
		
		if( other == null || other.getId().equals(publisher.getId()))
		{
			publisherService.savePublisher(publisher);
			added = true;
		}
		else
		{
			message = getText("publisherAlreadyExists", 
					new String[]{publisher.getName()});
			
			addFieldError("publisherAlreadyExists", message);
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
		log.debug("Delete publishers called");
		if( publisherIds != null )
		{
		    for(int index = 0; index < publisherIds.length; index++)
		    {
			    log.debug("Deleting publisher with id " + publisherIds[index]);
			    publisherService.deletePublisher(publisherIds[index]);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the publisher.
	 * 
	 * @return
	 */
	public String get()
	{
		publisher = publisherService.getPublisher(id, false);
		return "get";
	}
 

	
	/**
	 * Get the publishers table data.
	 * 
	 * @return
	 */
	public String viewPublishers()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		publishers = publisherService.getPublishersOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = publisherService.getPublishersCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
} 
	
	/**
	 * Get the publisher service.
	 * 
	 * @return
	 */
	public PublisherService getPublisherService() {
		return publisherService;
	}

	/**
	 * Set the publisher service.
	 * 
	 * @param publisherService
	 */
	public void setPublisherService(PublisherService publisherService) {
		this.publisherService = publisherService;
	}
	
	/**
	 * List of publishers for display.
	 * 
	 * @return
	 */
	public Collection<Publisher> getPublishers() {
		return publishers;
	}
	/**
	 * Set the list of publishers.
	 * 
	 * @param publishers
	 */
	public void setPublishers(Collection<Publisher> publishers) {
		this.publishers = publishers;
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

	public long[] getPublisherIds() {
		return publisherIds;
	}

	public void setPublisherIds(long[] publisherIds) {
		this.publisherIds = publisherIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			publisher = publisherService.getPublisher(id, false);
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

}
