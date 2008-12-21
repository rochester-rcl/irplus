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

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.mime.MimeTypeService;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.SubTypeExtension;
import edu.ur.file.mime.TopMediaType;
import edu.ur.ir.web.table.Pager;

/**
 * @author Nathan Sarr
 *
 */
public class ManageSubTypeExtensions extends Pager implements  Preparable{

	/** eclipse generated subTypeId */
	private static final long serialVersionUID = 7516613838709776386L;

	/** MIME type service */
	private MimeTypeService mimeTypeService;
	
	/**  Logger for managing top media types*/
	private static final Logger log = Logger.getLogger(ManageSubTypeExtensions.class);
	
	/** Set of top media types for viewing the top media types */
	private Collection<SubTypeExtension> subTypeExtensions;
	
	/**  Top media type for loading  */
	private SubType subType;
	
	/** parent top media type */
	private TopMediaType topMediaType;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the content has been added*/
	private boolean added = false;
	
	/** Indicates the top media types have been deleted */
	private boolean deleted = false;
	
	/** subTypeId of the sub type  */
	private Long subTypeId;
	
	/** subTypeId of the sub type extension */
	private Long subTypeExtensionId;
	
	/** Set of sub type extension ids */
	private long[] subTypeExtensionIds;
	
	/** name to give the sub type extension */
	private String name;
	
	/** description to give the extension */
	private String description;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of sub type extensions */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public ManageSubTypeExtensions() {
		
		numberOfResultsToShow = 50;
		numberOfPagesToShow = 10;
	}


	
	/**
	 * Initializes the page with all the data needed on
	 * initial load - the rest will be ajax calls.
	 * 
	 * @return
	 */
	public String init()
	{
		return "init";
	}
	
	/**
	 * Get a single sub type extension
	 * @return
	 */
	public String get()
	{
		SubTypeExtension extension = mimeTypeService.getSubTypeExtension(subTypeExtensionId, false);
		subType = extension.getSubType();
		name = extension.getName();
		description = extension.getDescription();
		return "get";
	}
	
	/**
	 * Method to create a new top media type.
	 * 
	 * Create a new top media type
	 */
	public String create()
	{
		
		log.debug("creating a sub type extension with name = " + name +
				" for sub type " + subType);
		added = false;
		SubTypeExtension subTypeExtension;
		try {
			subTypeExtension = subType.createExtension(name);
			subTypeExtension.setDescription(description);
			mimeTypeService.saveSubTypeExtension(subTypeExtension);
			added = true;
		} catch (DuplicateNameException e) {
			message = getText("subTypeExtensionAlreadyExists", 
					new String[]{name});
			this.addFieldError("subTypeExtensionAlreadyExists", message);
		}
		
		return "added";
	}
	
	/**
	 * Method to update an existing sub type extension.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing sub type extension = " + name);
		added = false;
		
		SubTypeExtension subTypeExtension = subType.getExtension(subTypeExtensionId);
		SubTypeExtension other = subType.getExtension(name);
		log.debug( " other = " + other);
		
		if( other == null || other.getId().equals(subTypeExtension.getId()))
		{
			subTypeExtension.setName(name);
			subTypeExtension.setDescription(description);
			log.debug("Saving extension " + subTypeExtension);
			mimeTypeService.saveSubTypeExtension(subTypeExtension);
			added = true;
		}
		else
		{
			message = getText("subTypeExtensionAlreadyExists", 
					new String[]{subTypeExtension.getName()});
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
		if( subTypeExtensionIds != null )
		{
		    for(int index = 0; index < subTypeExtensionIds.length; index++)
		    {
			    log.debug("Deleting sub type extension with subTypeId " + subTypeExtensionIds[index]);
			    SubTypeExtension subTypeExtension = mimeTypeService.getSubTypeExtension(subTypeExtensionIds[index], false);
			    mimeTypeService.deleteSubTypeExtension(subTypeExtension);
		    }
		}
		deleted = true;
		return "deleted";
	}
	

	
	/**
	 * Get the sub types table data.
	 * 
	 * @return all sub types
	 */
	public String all()
	{
		
		log.debug("subTypeId = " + subTypeId);
		subType = mimeTypeService.getSubType(subTypeId, false);
		
		rowEnd = rowStart + numberOfResultsToShow;
	    
		subTypeExtensions = mimeTypeService.getSubTypeExtensionsOrderByName(subTypeId, rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = mimeTypeService.getSubTypeExtensionsCount(subTypeId).intValue();
		
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
	public Collection<SubTypeExtension> getSubTypeExtensions() {
		return subTypeExtensions;
	}
	/**
	 * Set the list of sub types.
	 * 
	 * @param subTypeExtensions
	 */
	public void setSubTypeExtensions(Collection<SubTypeExtension> subTypeExtensions) {
		this.subTypeExtensions = subTypeExtensions;
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
	 * Get the subTypeId for modifying information.
	 * 
	 * @return
	 */
	public Long getSubTypeId() {
		return subTypeId;
	}

	/**
	 * Set the subTypeId of the item to be modified.
	 * 
	 * @param subTypeId
	 */
	public void setSubTypeId(Long id) {
		this.subTypeId = id;
	}

	/**
	 * Set the sub type ids to be modified.
	 * 
	 * @param subTypeExtensionIds
	 */
	public void setSubTypeExtensionIds(long[] subTypeExtensionIds) {
		this.subTypeExtensionIds = subTypeExtensionIds;
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
	 * Get the sub type 
	 * 
	 * @return
	 */
	public SubType getSubType() {
		return subType;
	}

	/**
	 * Set the sub type subTypeId.
	 * 
	 * @param topMediaType
	 */
	public void setSubType(SubType subType) {
		this.subType = subType;
	}

	public void prepare() throws Exception {
		log.debug( "Preparing subTypeId " + subTypeId);
		if( subTypeId != null)
		{
			subType = mimeTypeService.getSubType(subTypeId, false);
			topMediaType = subType.getTopMediaType();
		}
	}
	public void setSubTypeExtensionId(Long subTypeExtensionId) {
		this.subTypeExtensionId = subTypeExtensionId;
	}

	public TopMediaType getTopMediaType() {
		return topMediaType;
	}

	public void setTopMediaType(TopMediaType topMediaType) {
		this.topMediaType = topMediaType;
	}

	public Long getSubTypeExtensionId() {
		return subTypeExtensionId;
	}

	public long[] getSubTypeExtensionIds() {
		return subTypeExtensionIds;
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
