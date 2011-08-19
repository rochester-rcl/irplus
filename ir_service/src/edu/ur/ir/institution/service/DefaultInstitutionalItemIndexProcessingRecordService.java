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


package edu.ur.ir.institution.service;

import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.item.ContentType;
import edu.ur.ir.item.IdentifierType;
import edu.ur.ir.item.LanguageType;
import edu.ur.ir.item.PlaceOfPublication;
import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.Series;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Implementation of the institutional item index processing record service.
 *  
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalItemIndexProcessingRecordService  implements InstitutionalItemIndexProcessingRecordService
{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 2975220442906453057L;

	/** Data access for institutional item processing records */
	private InstitutionalItemIndexProcessingRecordDAO processingRecordDAO;
	
	/** Service for dealing with institutional items */
	private InstitutionalItemService institutionalItemService;
	
	/** Service for dealing with processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultInstitutionalItemIndexProcessingRecordService.class);

	/**
	 * Delete the processing record.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#delete(edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord)
	 */
	public void delete(
			InstitutionalItemIndexProcessingRecord institutionalItemIndexProcessingRecord) {
		processingRecordDAO.makeTransient(institutionalItemIndexProcessingRecord);
	}

	/**
	 * Get the institutional item processing record by id.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#get(java.lang.Long, boolean)
	 */
	public InstitutionalItemIndexProcessingRecord get(Long id, boolean lock) {
		return processingRecordDAO.getById(id, lock);
	}

	/**
	 * Get all institutional item indexing processing record ordered by updated date descending.
	 * 
	 * @param rowStart - start position
	 * @param maxResults - maximum number of results to get.
	 *  
	 * @return all institutional item index processing records or an empty list if none found
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate(int rowStart, int maxResults) {
		return processingRecordDAO.getAllOrderByItemIdUpdatedDate(rowStart, maxResults);
	}
	
	
	/**
	 * Get all institutional item index processing records for a given index processing type ordered
	 * by updated date descending
	 * 
	 * @param processingTypeId - processing type id
	 * 
	 * @return list of records found
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllByProcessingTypeUpdatedDate(Long processingTypeId)
	{
		return processingRecordDAO.getAllByProcessingTypeUpdatedDate(processingTypeId);
	}


	/**
	 * Get the count of the processing records.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#getCount()
	 */
	public Long getCount() {
		return processingRecordDAO.getCount();
	}

	/**
	 * Save the institutional item index processing record.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#save(edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord)
	 */
	public void save(
			InstitutionalItemIndexProcessingRecord institutionalItemIndexProcessingRecord) {
		processingRecordDAO.makePersistent(institutionalItemIndexProcessingRecord);
	}
	
	public InstitutionalItemIndexProcessingRecordDAO getProcessingRecordDAO() {
		return processingRecordDAO;
	}

	public void setProcessingRecordDAO(
			InstitutionalItemIndexProcessingRecordDAO processingRecordDAO) {
		this.processingRecordDAO = processingRecordDAO;
	}

	
	/**
	 * Get the item index processing record by item id processing type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#get(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public InstitutionalItemIndexProcessingRecord get(Long itemId,
			IndexProcessingType processingType) {
		return processingRecordDAO.get(itemId, processingType);
	}
	
	
	/**
	 * Add all items within the collection to be processed.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#processItemsInCollection(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.index.IndexProcessingType)
	 */
	public void processItemsInCollection( InstitutionalCollection institutionalCollection,
			IndexProcessingType processingType)
	{
		log.debug("seting collection items for re-indexing  collection = " + institutionalCollection);
	    processingRecordDAO.insertAllItemsForCollection(institutionalCollection, processingType);
	}
	
	/**
	 * Add all items within the repository to be processed.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#processItemsInCollection(edu.ur.ir.institution.InstitutionalCollection, edu.ur.ir.index.IndexProcessingType)
	 */
	public void processItemsInRepository( IndexProcessingType processingType, boolean create)
	{
		if( log.isDebugEnabled() )
		{
		    log.debug("seting collection items for re-indexing  repository processing type = " +
		    		processingType + " create = " + create);
		}
		if( create )
		{
			IndexProcessingType deleteIndexType = indexProcessingTypeService.get(IndexProcessingTypeService.DELETE_INDEX);
			InstitutionalItemIndexProcessingRecord deleteIndex = new InstitutionalItemIndexProcessingRecord(-1l, deleteIndexType);
			processingRecordDAO.makePersistent(deleteIndex);
		}
		
	    processingRecordDAO.insertAllItemsForRepository(processingType);
	}

	
	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#save(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public InstitutionalItemIndexProcessingRecord save(Long itemId,
			IndexProcessingType processingType) {
		
		IndexProcessingType updateProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE);
		IndexProcessingType deleteProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.DELETE);
		IndexProcessingType insertProcessingType = indexProcessingTypeService.get(IndexProcessingTypeService.INSERT);
		
		
		InstitutionalItemIndexProcessingRecord record = this.get(itemId, processingType);
		
		
		if( record != null)
		{
		    // do nothing record is already set to be processed
		}
		else
		{
			
			if(processingType.equals(updateProcessingType))
			{
				// only update if there is no insert or delete processing type records
				if( get(itemId, insertProcessingType) == null &&  get(itemId, deleteProcessingType) == null)
				{
				    record = new InstitutionalItemIndexProcessingRecord(itemId, processingType);
				    save(record);
				}
				else
				{
					// do nothing already set to be inserted
				}

			}
			else if(processingType.equals(deleteProcessingType))
			{
				// deleting item - so all other processing types do not need to occur
				InstitutionalItemIndexProcessingRecord updateProcessing = this.get(itemId, updateProcessingType);
				if( updateProcessing != null )
				{
					delete(updateProcessing);
				}
				
				InstitutionalItemIndexProcessingRecord insertProcessing = this.get(itemId, insertProcessingType);
				if( insertProcessing != null )
				{
					delete(insertProcessing);
				}
				record = new InstitutionalItemIndexProcessingRecord(itemId, processingType);
				save(record);

			}
			else if(processingType.equals(insertProcessingType))
			{
				// there should never be an update, delete record existing for an insert
				if( get(itemId, updateProcessingType) == null &&  get(itemId, deleteProcessingType) == null )
				{
				    record = new InstitutionalItemIndexProcessingRecord(itemId, processingType);
				    save(record);
				}
				else
				{
					throw new IllegalStateException(" Insert entered with existing processing type record already existing item id = " + itemId);
				}
			}
			else
			{
				log.error("Could not identify processing type " + processingType);
			}
		}
		
		
		return record;
		
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

	/**
	 * Insert all institutional items with a current version that has the
	 * specified content type for re-indexing.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForContentType(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForContentType(ContentType contentType,
			IndexProcessingType processingType) {
        return processingRecordDAO.insertAllItemsForContentType(contentType, processingType);
	}

	/** Insert all institutional items with a current version that has the
	 * specified contributor type for re-indexing.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForContributorType(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForContributorType(ContributorType contributorType,
			IndexProcessingType processingType) {
		return  processingRecordDAO.insertAllItemsForContributorType(contributorType, processingType);
	}

	/**
	 * Insert all institutional items with a current version that has the specified identifier type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForIdentifierType(edu.ur.ir.item.IdentifierType, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForIdentifierType(IdentifierType identifierType,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForIdentifierType(identifierType, processingType);
	}

	/**
	 * Insert all items for language type.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForLangaugeType(edu.ur.ir.item.LanguageType, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForLangaugeType(LanguageType languageType,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForLanguageType(languageType, processingType);
	}
	
	/**
	 * Insert all items for place of publication
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForPlaceOfPublication(edu.ur.ir.item.PlaceOfPublication, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPlaceOfPublication(PlaceOfPublication placeOfPublication,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForPlaceOfPublication(placeOfPublication, processingType);
	}

	/**
	 * Insert all items for person name to be re-indexed.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForPersonName(edu.ur.ir.person.PersonName, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPersonName(PersonName personName,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForPersonName(personName, processingType);
	}

	/**
	 * Insert all items for publisher to be re-indexed.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForPublisher(edu.ur.ir.item.Publisher, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForPublisher(Publisher publisher,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForPublisher(publisher, processingType);
	}

	/**
	 * Insert all items for series to be re-indexed.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForSeries(edu.ur.ir.item.Series, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForSeries(Series series,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForSeries(series, processingType);
	}

	/**
	 *  Insert all items for sponsor to be re-indexed.
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#insertAllItemsForSponsor(edu.ur.ir.item.Sponsor, edu.ur.ir.index.IndexProcessingType)
	 */
	public Long insertAllItemsForSponsor(Sponsor sponsor,
			IndexProcessingType processingType) {
		return processingRecordDAO.insertAllItemsForSponsor(sponsor, processingType);
	}

}
