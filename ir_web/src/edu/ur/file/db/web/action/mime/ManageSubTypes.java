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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.mime.MimeTypeService;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.TopMediaType;
import edu.ur.ir.web.table.Pager;

/**
 * This class allows a user to view the subTypes for a given 
 * MIME type as well as edit MIME top media type information and 
 * add new sub type information.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageSubTypes extends Pager implements  Preparable{
	
	/** generated serial id. */
	private static final long serialVersionUID = 3111192562008117181L;

	/** MIME type service */
	private MimeTypeService mimeTypeService;
	
	/**  Logger for managing top media types*/
	private static final Logger log = LogManager.getLogger(ManageMimeTopMediaTypes.class);
	
	/** Set of top media types for viewing the top media types */
	private Collection<SubType> subTypes;
	
	/**  Top media type for loading  */
	private TopMediaType topMediaType;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the content has been added*/
	private boolean added = false;
	
	/** Indicates the top media types have been deleted */
	private boolean deleted = false;
	
	/** id of the top media type  */
	private Long id;
	
	/** id of the subtype */
	private Long subTypeId;
	
	/** Set of top media type ids */
	private long[] subTypeIds;
	
	/** Name to give the sub type  */
	private String name;
	
	/** description to give the sub type */
	private String description;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of sub types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageSubTypes() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Initializes the page with all the data needed
	 * 
	 * @return
	 */
	public String init()
	{
		return "init";
	}
	
	/**
	 * Method to create a new top media type.
	 * 
	 * Create a new top media type
	 */
	public String create()
	{
		log.debug("creating a sub type = " + name);
		added = false;
		
		try {
			SubType subType = topMediaType.createSubType(name);
			subType.setDescription(description);
			mimeTypeService.saveSubType(subType);
			added = true;
		} catch (DuplicateNameException e) {
			message = getText("subTypeAlreadyExists", 
					new String[]{name});
			this.addFieldError("subTypeAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing sub type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing sub type id = " + subTypeId);
		added = false;
		
		
		SubType subType = mimeTypeService.getSubType(subTypeId, false);
		topMediaType = subType.getTopMediaType();
		SubType other = topMediaType.getSubType(name);
		id = topMediaType.getId();
		
		if( other == null || other.getId().equals(subType.getId()))
		{
			subType.setName(name);
			subType.setDescription(description);
			log.debug( "saving sub type " + subType.getName() + 
					" description = " + subType.getDescription());
			mimeTypeService.saveSubType(subType);
			added = true;
		}
		else
		{
			message = getText("subTypeAlreadyExists", 
					new String[]{subType.getName()});
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
		log.debug("Delete sub types called");
		if( subTypeIds != null )
		{
		    for(int index = 0; index < subTypeIds.length; index++)
		    {
			    log.debug("Deleting sub type with id " + subTypeIds[index]);
			    SubType subType = mimeTypeService.getSubType(subTypeIds[index], false);
			    mimeTypeService.deleteSubType(subType);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	public String get()
	{
		SubType subType = mimeTypeService.getSubType(subTypeId, false);
		name = subType.getName();
		description = subType.getDescription();
		return "get";
	}
	
	/**
	 * Get the sub types table data.
	 * 
	 * @return all sub types
	 */
	public String all()
	{
	
		log.debug("mime top media type id = " + id);
		topMediaType = mimeTypeService.getTopMediaType(id, false);
		
		rowEnd = rowStart + numberOfResultsToShow;
	    
		subTypes = mimeTypeService.getSubTypesOrderByName(id, rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = mimeTypeService.getSubTypesCount(id).intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return "all";

	}

	/**
	 * Get the sub type service.
	 * 
	 * @return
	 */
	public MimeTypeService getMimeTypeService() {
		return mimeTypeService;
	}

	/**
	 * Set the sub type service.
	 * 
	 * @param mimeTypeService
	 */
	public void setMimeTypeService(MimeTypeService mimeTypeService) {
		this.mimeTypeService = mimeTypeService;
	}
	
	/**
	 * List of sub types for display.
	 * 
	 * @return
	 */
	public Collection<SubType> getSubTypes() {
		return subTypes;
	}
	/**
	 * Set the list of sub types.
	 * 
	 * @param subTypes
	 */
	public void setSubTypes(Collection<SubType> subTypes) {
		this.subTypes = subTypes;
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
	 * Get a the list of sub types to be modified.
	 * 
	 * @return
	 */
	public long[] getSubTypeIds() {
		return subTypeIds;
	}

	/**
	 * Set the sub type ids to be modified.
	 * 
	 * @param subTypeIds
	 */
	public void setSubTypeIds(long[] subTypeIds) {
		this.subTypeIds = subTypeIds;
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
	 * Set the sub type id.
	 * 
	 * @param topMediaType
	 */
	public void setTopMediaType(TopMediaType topMediaType) {
		this.topMediaType = topMediaType;
	}

	public void prepare() throws Exception {
		log.debug("prepare called id = " + id + " subTypeId = " + subTypeId);
		if( id != null)
		{
			topMediaType = mimeTypeService.getTopMediaType(id, false);
		}
		

	}

	/**
	 * Get the top media type.
	 * 
	 * @return
	 */
	public TopMediaType getTopMediaType() {
		return topMediaType;
	}

	public Long getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(Long subTypeId) {
		this.subTypeId = subTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
