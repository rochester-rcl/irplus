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

import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemVersionService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with content types.
 * 
 * @author Nathan Sarr
 *
 */
public class ManageContentTypes extends Pager implements Preparable, UserIdAware {
	
	/** generated version id. */
	private static final long serialVersionUID = -7954124847449231029L;
	
	/** content type service */
	private ContentTypeService contentTypeService;
	
	/**  Logger for managing content types*/
	private static final Logger log = Logger.getLogger(ManageContentTypes.class);
	
	/** Set of content types for viewing the content types */
	private Collection<ContentType> contentTypes;
	
	/**  Content type for loading  */
	private ContentType contentType;
	
	/** id of the user making the changes  */
	private Long userId;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the content has been added*/
	private boolean added = false;
	
	/** Indicates the content types have been deleted */
	private boolean deleted = false;
	
	/** id of the content type  */
	private Long id;
	
	/** Set of content type ids */
	private long[] contentTypeIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of content types */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/**  Service for processing indexing institutional items */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;
	
	/** Service for dealing with processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** Service for dealing with institutional item version services */
	private InstitutionalItemVersionService institutionalItemVersionService;
	
	/** Service for user information  */
	private UserService userService;





	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/** Default constructor */
	public  ManageContentTypes() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}

	/**
	 * Method to create a new content type.
	 * 
	 * Create a new content type
	 */
	public String create()
	{
		log.debug("creating a content type = " + contentType.getName());
		added = false;
		ContentType other = contentTypeService.getContentType(contentType.getName());
		if( other == null)
		{
		    contentTypeService.saveContentType(contentType);
		    added = true;
		}
		else
		{
			message = getText("contentTypeNameError", 
					new String[]{contentType.getName()});
			addFieldError("contentTypeAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing content type.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing content type id = " + contentType.getId());
		added = false;

		ContentType other = contentTypeService.getContentType(contentType.getName());
		
		if( other == null || other.getId().equals(contentType.getId()))
		{
			contentTypeService.saveContentType(contentType);
		    Long indexCount = institutionalItemIndexProcessingRecordService.insertAllItemsForContentType(contentType.getId(), indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE));
		    IrUser user = userService.getUser(userId, false);
		    Long updatedCount = institutionalItemVersionService.setAllVersionsAsUpdatedForContentType(contentType ,user, "Content Type Updated");
			log.debug("Total number of records set for re-indxing = " + indexCount);
			log.debug("Total number of records set as updated = " + updatedCount);
		    added = true;
		}
		else
		{
			message = getText("contentTypeNameError", 
					new String[]{contentType.getName()});
			
			addFieldError("contentTypeAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Get a content type
	 * 
	 * @return the content type
	 */
	public String get()
	{
		contentType = contentTypeService.getContentType(id, false);	
		return "get";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete content types called");
		if( contentTypeIds != null )
		{
		    for(int index = 0; index < contentTypeIds.length; index++)
		    {
			    log.debug("Deleting content type with id " + contentTypeIds[index]);
			    contentTypeService.deleteContentType(contentTypeIds[index]);
		    }
		}
		deleted = true;
		return "deleted";
	}
 
	/**
	 * Get the content types table data.
	 * 
	 * @return
	 */
	public String viewContentTypes()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		contentTypes = contentTypeService.getContentTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = contentTypeService.getContentTypesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;

	}

	/**
	 * Get the content type service.
	 * 
	 * @return
	 */
	public ContentTypeService getContentTypeService() {
		return contentTypeService;
	}

	/**
	 * Set the content type service.
	 * 
	 * @param contentTypeService
	 */
	public void setContentTypeService(ContentTypeService contentTypeService) {
		this.contentTypeService = contentTypeService;
	}
	
	/**
	 * List of content types for display.
	 * 
	 * @return
	 */
	public Collection<ContentType> getContentTypes() {
		return contentTypes;
	}
	/**
	 * Set the list of content types.
	 * 
	 * @param contentTypes
	 */
	public void setContentTypes(Collection<ContentType> contentTypes) {
		this.contentTypes = contentTypes;
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

	public long[] getContentTypeIds() {
		return contentTypeIds;
	}

	public void setContentTypeIds(long[] contentTypeIds) {
		this.contentTypeIds = contentTypeIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			contentType = contentTypeService.getContentType(id, false);
		}
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	public int getTotalHits() {
		return totalHits;
	}

	/**
	 * User making the change.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	public Long getUserId() {
		return userId;
	}
	
	public void setInstitutionalItemVersionService(
			InstitutionalItemVersionService institutionalItemVersionService) {
		this.institutionalItemVersionService = institutionalItemVersionService;
	}
}
