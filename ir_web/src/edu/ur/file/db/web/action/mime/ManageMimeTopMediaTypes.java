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

package edu.ur.file.db.web.action.mime;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.file.mime.MimeTypeService;
import edu.ur.file.mime.TopMediaType;
import edu.ur.ir.web.table.Pager;

/**
 * Allows for the management of media types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageMimeTopMediaTypes extends Pager implements  Preparable{
	
	/** generated serial id. */
	private static final long serialVersionUID = 3111192562008117181L;

	/** MIME type service */
	private MimeTypeService mimeTypeService;
	
	/**  Logger for managing top media types*/
	private static final Logger log = Logger.getLogger(ManageMimeTopMediaTypes.class);
	
	/** Set of top media types for viewing the top media types */
	private Collection<TopMediaType> topMediaTypes;
	
	/**  Top media type for loading  */
	private TopMediaType topMediaType;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the MIME has been added*/
	private boolean added = false;
	
	/** Indicates the top media types have been deleted */
	private boolean deleted = false;
	
	/** id of the top media type  */
	private Long id;
	
	/** Set of top media type ids */
	private long[] topMediaTypeIds;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of top media types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public   ManageMimeTopMediaTypes() {
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	
	/**
	 * Method to create a new top media type.
	 * 
	 * Create a new top media type
	 */
	public String create()
	{
		log.debug("creating a top media type = " + topMediaType);
		added = false;
		TopMediaType other = mimeTypeService.getTopMediaType(topMediaType.getName());
		if( other == null)
		{
		    mimeTypeService.saveTopMediaType(topMediaType);
		    added = true;
		}
		else
		{
			message = getText("topMediaTypeAlreadyExists", 
					new String[]{topMediaType.getName()});
		}
        return "added";
	}
	
	/**
	 * Method to update an existing top media type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing top media type id = " + topMediaType);
		added = false;

		TopMediaType other = mimeTypeService.getTopMediaType(topMediaType.getName());
		
		if( other == null || other.getId().equals(topMediaType.getId()))
		{
			mimeTypeService.saveTopMediaType(topMediaType);
			added = true;
		}
		else
		{
			message = getText("topMediaTypeAlreadyExists", 
					new String[]{topMediaType.getName()});
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return deleted if MIME types are deleted.
	 */
	public String delete()
	{
		log.debug("Delete top media types called");
		if( topMediaTypeIds != null )
		{
		    for(int index = 0; index < topMediaTypeIds.length; index++)
		    {
			    log.debug("Deleting top media type with id " + topMediaTypeIds[index]);
			    TopMediaType tmt = mimeTypeService.getTopMediaType(topMediaTypeIds[index], false);
			    mimeTypeService.deleteTopMediaType(tmt);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Loads the 
	 * @return
	 */
	public String get()
	{
		log.debug("get called");
	    return "get";
	}
	
	/**
	 * Get the top media types table data.
	 * 
	 * @return all top media types
	 */
	public String viewTopMediaTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		topMediaTypes = mimeTypeService.getTopMediaTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = mimeTypeService.getTopMediaTypeCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}

	/**
	 * Get the top media type service.
	 * 
	 * @return
	 */
	public MimeTypeService getMimeTypeService() {
		return mimeTypeService;
	}

	/**
	 * Set the top media type service.
	 * 
	 * @param mimeTypeService
	 */
	public void setMimeTypeService(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}
	
	/**
	 * List of top media types for display.
	 * 
	 * @return
	 */
	public Collection<TopMediaType> getTopMediaTypes() {
		return topMediaTypes;
	}
	/**
	 * Set the list of top media types.
	 * 
	 * @param topMediaTypes
	 */
	public void setTopMediaTypes(Collection<TopMediaType> topMediaTypes) {
		this.topMediaTypes = topMediaTypes;
	}

	/**
	 * Message to give to user if there is a problem.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Message to return to user if there is a problem
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Indicates the information was added.
	 * 
	 * @return true if the information was added.
	 */
	public boolean isAdded() {
		return added;
	}
	
	/**
	 * Indicates the information was added.
	 * 
	 * @param added
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * Get the id for modifying information.
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Set the id of the item to be modified.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Get a the list of top media types to be modified.
	 * 
	 * @return
	 */
	public long[] getTopMediaTypeIds() {
		return topMediaTypeIds;
	}
	
	/**
	 * Set the top media type ids to be modified.
	 * 
	 * @param topMediaTypeIds
	 */
	public void setTopMediaTypeIds(long[] topMediaTypeIds) {
		this.topMediaTypeIds = topMediaTypeIds;
	}

	/**
	 * Returns true if delete completed successfully.
	 * 
	 * @return
	 */
	public boolean getDeleted() {
		return deleted;
	}

	/**
	 * Set the information as deleted.
	 * 
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Get the top media type.
	 * 
	 * @return
	 */
	public TopMediaType getTopMediaType() {
		return topMediaType;
	}

	/**
	 * Set the top media type id.
	 * 
	 * @param topMediaType
	 */
	public void setTopMediaType(TopMediaType topMediaType) {
		this.topMediaType = topMediaType;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			topMediaType = mimeTypeService.getTopMediaType(id, false);
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
