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


package edu.ur.file.db.service;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import edu.ur.file.mime.MimeTypeService;
import edu.ur.file.mime.SubType;
import edu.ur.file.mime.SubTypeDAO;
import edu.ur.file.mime.SubTypeExtension;
import edu.ur.file.mime.SubTypeExtensionDAO;
import edu.ur.file.mime.TopMediaType;
import edu.ur.file.mime.TopMediaTypeDAO;

/**
 * Default implementation for the mime type information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultMimeTypeService implements MimeTypeService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 7576783157449909674L;

	/** Top media type persistance.  */
	private TopMediaTypeDAO topMediaTypeDAO;
	
	/** Sub type persistence.  */
	private SubTypeDAO subTypeDAO;

	/** Sub type extension persistence  */
	private SubTypeExtensionDAO subTypeExtensionDAO;

	/**
	 * If top media type does not exist, a new one is created otherwise, the existing
	 * top media type is returned
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#createTopMediaType(java.lang.String)
	 */
	public TopMediaType createTopMediaType(String name) {
		
		TopMediaType topMediaType = topMediaTypeDAO.findByUniqueName(name);
		if( topMediaType == null)
		{
		    topMediaType = new TopMediaType(name); 
		    topMediaTypeDAO.makePersistent(topMediaType);
		}
		
		return topMediaType;
	}

	/**
	 * If top media type does not exist, a new one is created otherwise, the existing
	 * top media type is returned
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#createTopMediaType(java.lang.String, java.lang.String)
	 */
	public TopMediaType createTopMediaType(String name, String description) {
		TopMediaType topMediaType = topMediaTypeDAO.findByUniqueName(name);
		if( topMediaType == null)
		{
		    topMediaType = new TopMediaType(name, description); 
		    topMediaTypeDAO.makePersistent(topMediaType);
		}
		
		return topMediaType;
	}

	/** 
	 * Find a sub type by uniqe name.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubType(java.lang.String, java.lang.Long)
	 */
	public SubType getSubType(String name, Long topMediaTypeId) {
		return subTypeDAO.findByUniqueName(name, topMediaTypeId);
	}

	/**
	 * Find the sub type extension by unique name.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubTypeExtension(java.lang.String)
	 */
	public SubTypeExtension getSubTypeExtension(String name) {
		return subTypeExtensionDAO.findByUniqueName(name);
	}

	/**
	 * Returns the found top media type.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getTopMediaType(java.lang.String)
	 */
	public TopMediaType getTopMediaType(String name) {
		return topMediaTypeDAO.findByUniqueName(name);
	}

	/**
	 * Get all sub type extensions name order.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllSubTypeExtensionsNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<SubTypeExtension> getAllSubTypeExtensionsNameOrder() {
		return subTypeExtensionDAO.getAllNameOrder();
	}

	/**
	 * Get all sub type extensions ordered by name.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllSubTypeExtensionsOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<SubTypeExtension> getAllSubTypeExtensionsOrderByName(int startRecord, int numRecords) {
		return subTypeExtensionDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get all sub types - starting at the given start position and include 
	 * up-to the specified number of records.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllSubTypeOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<SubType> getAllSubTypeOrderByName(int startRecord, int numRecords) {
		return subTypeDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Get all sub types ordered by name.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllSubTypesNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<SubType> getAllSubTypesNameOrder() {
		return subTypeDAO.getAllNameOrder();
	}


	/**
	 * Get all top media types in name order.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllTopMediaTypeNameOrder()
	 */
	@SuppressWarnings("unchecked")
	public List<TopMediaType> getAllTopMediaTypeNameOrder() {
		return topMediaTypeDAO.getAllNameOrder();
	}

	/**
	 * Get all top media types within the specified range.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getAllTopMediaTypeOrderByName(int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<TopMediaType> getAllTopMediaTypeOrderByName(int startRecord, int numRecords) {
		return topMediaTypeDAO.getAllOrderByName(startRecord, numRecords);
	}

	/**
	 * Find a sub type by it's id.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubType(java.lang.Long, boolean)
	 */
	public SubType getSubType(Long id, boolean lock) {
		return subTypeDAO.getById(id, lock);
	}

	/**
	 * Get a count of all sub types.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubTypeCount()
	 */
	public Long getSubTypeCount() {
		return subTypeDAO.getCount();
	}

	/**
	 * Get sub type extension by id.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubTypeExtension(java.lang.Long, boolean)
	 */
	public SubTypeExtension getSubTypeExtension(Long id, boolean lock) {
		return subTypeExtensionDAO.getById(id, lock);
	}

	/**
	 * Get the sub type extension count.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getSubTypeExtensionCount()
	 */
	public Long getSubTypeExtensionCount() {
		return subTypeExtensionDAO.getCount();
	}

	/**
	 * Get the top media type by id.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getTopMediaType(java.lang.Long, boolean)
	 */
	public TopMediaType getTopMediaType(Long id, boolean lock) {
		return topMediaTypeDAO.getById(id, lock);
	}

	/**
	 * Get a count of all top media types.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#getTopMediaTypeCount()
	 */
	public Long getTopMediaTypeCount() {
		return topMediaTypeDAO.getCount();
	}

	/**
	 * Make the sub type extension persistent.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#saveSubTypeExtension(edu.ur.file.mime.SubTypeExtension)
	 */
	public void saveSubTypeExtension(SubTypeExtension entity) {
		subTypeExtensionDAO.makePersistent(entity);
	}

	/**
	 * Make the sub type extension transient.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#deleteSubTypeExtension(edu.ur.file.mime.SubTypeExtension)
	 */
	public void deleteSubTypeExtension(SubTypeExtension entity) {
		SubType subType = entity.getSubType();
		subType.removeExtension(entity.getId());
		subTypeExtensionDAO.makeTransient(entity);
	}

	/**
	 * Make the sub type persistent.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#saveSubType(edu.ur.file.mime.SubType)
	 */
	public void saveSubType(SubType entity) {
		subTypeDAO.makePersistent(entity);
	}

	/**
	 * Make the sub type transient.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#deleteSubType(edu.ur.file.mime.SubType)
	 */
	public void deleteSubType(SubType entity) {
		subTypeDAO.makeTransient(entity);
	}

	/**
	 * Save the top media type to persistent storage.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#saveTopMediaType(edu.ur.file.mime.TopMediaType)
	 */
	public void saveTopMediaType(TopMediaType entity) {
		topMediaTypeDAO.makePersistent(entity);
	}

	/**
	 * Remove the top media type from persistent storage.
	 * 
	 * @see edu.ur.file.db.service.MimeTypeService#deleteTopMediaType(edu.ur.file.mime.TopMediaType)
	 */
	public void deleteTopMediaType(TopMediaType entity) {
		topMediaTypeDAO.makeTransient(entity);
	}

	/**
	 * Returns the mime type in the form of topMediaType/subType.  If the 
	 * extension is not found, null is returned.
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#findMimeType(java.lang.String)
	 */
	public String findMimeType(String extension) {
		SubTypeExtension myExtension = subTypeExtensionDAO.findByUniqueName(extension);
		String mimeType = null;
		
		if( myExtension != null)
		{
			mimeType = myExtension.getMimeType();
		}
		return mimeType;
	}

	/**
	 * Look at the file for the mime type.  This method simply takes the extension
	 * of the file.
	 * 
	 * @see edu.ur.file.mime.BasicMimeTypeService#findMimeType(java.io.File)
	 */
	public String findMimeType(File f) {
		String extension = FilenameUtils.getExtension(f.getName());
		return findMimeType(extension);
	}

	/**
	 * Get the data access for a sub type.
	 * 
	 * @return 
	 */
	public SubTypeDAO getSubTypeDAO() {
		return subTypeDAO;
	}

	/**
	 * Set the data access for sub type information.
	 * 
	 * @param subTypeDAO
	 */
	public void setSubTypeDAO(SubTypeDAO subTypeDAO) {
		this.subTypeDAO = subTypeDAO;
	}

	/**
	 * Set data access for a sub type extension.
	 * 
	 * @return
	 */
	public SubTypeExtensionDAO getSubTypeExtensionDAO() {
		return subTypeExtensionDAO;
	}

	/**
	 * Set the sub type extension data access.
	 * 
	 * @param subTypeExtensionDAO
	 */
	public void setSubTypeExtensionDAO(SubTypeExtensionDAO subTypeExtensionDAO) {
		this.subTypeExtensionDAO = subTypeExtensionDAO;
	}

	/**
	 * Get the top media type data.
	 * 
	 * @return
	 */
	public TopMediaTypeDAO getTopMediaTypeDAO() {
		return topMediaTypeDAO;
	}

	/**
	 * Set the top media type data access.
	 * 
	 * @param topMediaTypeDAO
	 */
	public void setTopMediaTypeDAO(TopMediaTypeDAO topMediaTypeDAO) {
		this.topMediaTypeDAO = topMediaTypeDAO;
	}

	
	/**
	 * 
	 * @see edu.ur.file.mime.MimeTypeService#getSubTypeExtensions(java.lang.Long, int, int, String)
	 */
	public List<SubTypeExtension> getSubTypeExtensionsOrderByName(final Long subTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return subTypeExtensionDAO.getSubTypeExtensions(subTypeId, 
				rowStart, 
				numberOfResultsToShow,
				sortType);
	}

	
	/**
	 * Get the count of sub type extensions.
	 * 
	 * @see edu.ur.file.mime.MimeTypeService#getSubTypeExtensionsCount(java.lang.Long)
	 */
	public Long getSubTypeExtensionsCount(final Long subTypeId) {
		return subTypeExtensionDAO.getSubTypeExtensionsCount(subTypeId);
	}

	
	/**
	 * @see edu.ur.file.mime.MimeTypeService#getSubTypes(java.lang.Long, int, int, String)
	 */
	public List<SubType> getSubTypesOrderByName(final Long topMediaTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return subTypeDAO.getSubTypes(topMediaTypeId, 
				rowStart, numberOfResultsToShow, sortType);
	}

	
	/**
	 * @see edu.ur.file.mime.MimeTypeService#getSubTypesCount(java.lang.Long)
	 */
	public Long getSubTypesCount(final Long topMediaTypeId) {
		return subTypeDAO.getSubTypesCount(topMediaTypeId);
	}

	
	/**
	 * @see edu.ur.file.mime.MimeTypeService#getTopMediaTypes(int, int, String)
	 */
	public List<TopMediaType> getTopMediaTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return topMediaTypeDAO.getTopMediaTypes( 
				rowStart, numberOfResultsToShow, sortType);
	}
	
	/**
	 * top media types.
	 * 
	 * @see edu.ur.file.mime.MimeTypeService#getTopMediaTypesCount()
	 */
	public Long getTopMediaTypesCount() {
		return topMediaTypeDAO.getCount();
	}

}
