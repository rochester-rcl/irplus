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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.ContentTypeDAO;
import edu.ur.ir.item.ContentTypeService;

/**
 * Default service for dealing with  content types.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultContentTypeService implements ContentTypeService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -7895868786287923787L;
	
	/**  Content type data access. */
	private ContentTypeDAO contentTypeDAO;


	/**
	 * Delete a content type with the specified id.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#deleteContentType(java.lang.Long)
	 */
	public boolean deleteContentType(Long id) {
		ContentType contentType = this.getContentType(id, false);
		if( contentType != null)
		{
			contentTypeDAO.makeTransient(contentType);
		}
		return true;
	}

	/**
	 * Delete a content type with the specified name.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#deleteContentType(java.lang.String)
	 */
	public boolean deleteContentType(String name) {
		ContentType contentType = this.getContentType(name);
		if( contentType != null)
		{
			contentTypeDAO.makeTransient(contentType);
		}
		return true;
	}

	/**
	 * Get the content type with the name.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getContentType(java.lang.String)
	 */
	public ContentType getContentType(String name) {
		return contentTypeDAO.findByUniqueName(name);
	}

	/**
	 * Get the content type by id.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getContentType(java.lang.Long, boolean)
	 */
	public ContentType getContentType(Long id, boolean lock) {
		return contentTypeDAO.getById(id, lock);
	}

	/**
	 * Get content types order by name
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getContentTypesOrderByName(int, int, String)
	 */
	public List<ContentType> getContentTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return contentTypeDAO.getContentTypesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the content types count.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getContentTypesCount()
	 */
	public Long getContentTypesCount() {
		return contentTypeDAO.getCount();
	}

	/**
	 * Content type data access.
	 * 
	 * @return
	 */
	public ContentTypeDAO getContentTypeDAO() {
		return contentTypeDAO;
	}

	/**
	 * Set the content type data access.
	 * 
	 * @param contentTypeDAO
	 */
	public void setContentTypeDAO(ContentTypeDAO contentTypeDAO) {
		this.contentTypeDAO = contentTypeDAO;
	}

	/**
	 * Save the content type.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#saveContentType(edu.ur.ir.item.ContentType)
	 */
	public void saveContentType(ContentType contentType) {
		contentTypeDAO.makePersistent(contentType);
	}

	/**
	 * Get all content types.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getAllContentType()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentType> getAllContentType() { 
		return (List<ContentType>) contentTypeDAO.getAll();
	}

	/**
	 * Get all content types by name asc order.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getAllContentTypeByNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentType> getAllContentTypeByNameOrder() { 
		return (List<ContentType>) contentTypeDAO.getAllNameOrder();
	}

	/**
	 * Get the content type by unique system code.
	 * 
	 * @see edu.ur.ir.item.ContentTypeService#getByUniqueSystemCode(java.lang.String)
	 */
	public ContentType getByUniqueSystemCode(String systemCode) {
		return contentTypeDAO.getByUniqueSystemCode(systemCode);
	}
}
