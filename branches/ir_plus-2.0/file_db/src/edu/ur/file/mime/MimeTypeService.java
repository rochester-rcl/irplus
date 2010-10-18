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
package edu.ur.file.mime;

import java.util.List;


/**
 * Facade implementation of mime type management.
 * 
 * 
 * @author Nathan Sarr
 *
 */
public interface MimeTypeService extends BasicMimeTypeService {
	
	/**
	 * Create a top media type.  Adds the mime type to persistent
	 * storage
	 * 
	 * @param name - name of the top media type
	 * 
	 * @return the created top media type
	 */
	public TopMediaType createTopMediaType(String name);
	
	/**
	 * Create a top media type.  Adds the mime type to persistent storage.
	 * 
	 * @param name - name of the top media type
	 * @param name - description of the top media type
	 *  
	 * @return the created top media type
	 */
	public TopMediaType createTopMediaType(String name, String description);
	
	
	/**
     * Get the number of Top media types in the system
     * 
     * @return the number of top media types
     */
	public Long getTopMediaTypeCount();

    /**
     * Return all top media types by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
 	public List<TopMediaType> getAllTopMediaTypeNameOrder();

    /**
     * Get all top media types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<TopMediaType> getAllTopMediaTypeOrderByName(int startRecord, int numRecords);

	/** 
     * Find a file by it's file name
     * 
     * @param name - name of the top media type.
     * @return the top media type found
	 */

	public TopMediaType getTopMediaType(String name);
	
	/**
	 * Get all media types in the system.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAllTopMediaType();

	/**
	 * Get the top media type by id.
	 * 
	 * @see edu.ur.dao.CrudDAO#getById(java.lang.Long, boolean)
	 */
	public TopMediaType getTopMediaType(Long id, boolean lock);

	/**
	 * Make the top media type persistent.
	 * 
	 */
	public void saveTopMediaType(TopMediaType entity);

	/**
	 * Make the top media type transient.
	 * 
	 */
	public void deleteTopMediaType(TopMediaType entity);
	
	/**
     * Get the number of Mime Sub type extensions in the system
     * 
     * @return the number of sub types in the system
     */
	public Long getSubTypeExtensionCount() ;

    /**
     * Return all sub type extensions orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
	public List<SubTypeExtension> getAllSubTypeExtensionsNameOrder() ;

    /**
     * Get all Sub types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<SubTypeExtension> getAllSubTypeExtensionsOrderByName(int startRecord, 
			int numRecords);
	
	/** 
     * Find a sub type extension by it's name(extension)
     * 
     * @param name - name of the mime sub type.
     * @return the sub type found
	 */
	public SubTypeExtension getSubTypeExtension(String name);

	/**
	 * Get all subtype extensions.
	 * 
	 * @see edu.ur.dao.CrudDAO#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List getAllSubTypeExtensions();

	/**
	 * Get the sub type extension by id.
	 * 
	 * @param id
	 * @param lock
	 * @return
	 */
	public SubTypeExtension getSubTypeExtension(Long id, boolean lock);

	/**
	 * Make the subTypeExtension persistent.  This is for only pre-existing sub
	 * type extensions
	 * 
	 * @param entity
	 */
	public void saveSubTypeExtension(SubTypeExtension entity);

	/**
	 * Make the subtype transient.  This is for only pre - existing sub types.
	 * 
	 * @param entity
	 */
	public void deleteSubTypeExtension(SubTypeExtension entity);
	
    /**
     * Get the number of Mime Sub types in the system
     * 
     * @return the number of sub types in the system
     */
	public Long getSubTypeCount();

    /**
     * Return all sub types orderd by Name.  This list 
     * can be extreamly large and it is recomened that
     * paging is used instead
     * 
     * @return
     */
	public List<SubType> getAllSubTypesNameOrder();

    /**
     * Get all Sub types starting at the start record and get up to 
     * the numRecords - it will be ordered by name
     *  
     * @param startRecord - the index to start at
     * @param numRecords - the number of records to get
     * @return the records found
     */
	public List<SubType> getAllSubTypeOrderByName(int startRecord, int numRecords);

	/** 
     * Find a sub type by it's name and top media type id.
     * 
     * @param name - name of the mime sub type.
     * @return the sub type found
	 */

	public SubType getSubType(String name, Long topMediaTypeId);


	/**
	 * Get all sub types.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List getAllSubTypes();

	/**
	 * Get a sub type by id.
	 * 
	 * @param id
	 * @param lock
	 * @return
	 */
	public SubType getSubType(Long id, boolean lock);

	/**
	 * Make the sub type persistent.
	 * 
	 * @param entity
	 */
	public void saveSubType(SubType entity);

	/**
	 * Make the sub type transient.
	 * 
	 * @param entity
	 */
	public void deleteSubType(SubType entity);
	
	/**
	 * Get the the top media types that match the specified criteria.
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return - set of top media types
	 */
	public List<TopMediaType> getTopMediaTypesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);


	/**
	 * Get a count of top media types with the specified criteria.
	 * 
	 * @return set of found media types.
	 */
	public Long getTopMediaTypesCount();
	
	/**
	 * Get the sub type extension with for the specified sub type and search criteria.
	 * 
	 * @param subTypeId - id of the parent subtype
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return found sub type extensions.
	 */
	public List<SubTypeExtension> getSubTypeExtensionsOrderByName(Long subTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Get a count of the sub type extensions with the specified criteria.
	 *  
	 * @param subTypeId - id of the parent subtype
	 * 
	 * @return count of sub type extensions.
	 */
	public Long getSubTypeExtensionsCount(Long subTypeId);
	
	/**
	 * Get a list of sub types for the specified parent top media type that meet the 
	 * criteria.
	 * 
	 * @param topMediaTypeId - id of the parent media type 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * 
	 * @return set of sub types.
	 */
	public List<SubType> getSubTypesOrderByName(Long topMediaTypeId,
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Get a count of sub types that meet the specified criteria.
	 * 
	 * @param topMediaTypeId - id of the parent media type
	 * 
	 * @return
	 */
	public Long getSubTypesCount(final Long topMediaTypeId);

}
