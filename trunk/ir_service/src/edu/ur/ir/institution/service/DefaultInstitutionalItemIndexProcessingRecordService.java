package edu.ur.ir.institution.service;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.order.OrderType;

/**
 * Implementation of the institutional item index processing record service.
 *  
 * @author Nathan Sarr
 *
 */
public class DefaultInstitutionalItemIndexProcessingRecordService  implements InstitutionalItemIndexProcessingRecordService
{
	
	/** Data access for institutional item processing records */
	private InstitutionalItemIndexProcessingRecordDAO processingRecordDAO;
	
	/** Service for dealing with institutional items */
	private InstitutionalItemService institutionalItemService;
	
	/** batch size for processing collection items */
	private int collectionItemBatchSize = 100;
	
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
	 * Get all institutional item index processing records.
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<InstitutionalItemIndexProcessingRecord> getAll() {
		return processingRecordDAO.getAll();
	}

	/**
	 * Get all processing records ordered by item id and updated date
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#getAllOrderByItemIdUpdatedDate()
	 */
	public List<InstitutionalItemIndexProcessingRecord> getAllOrderByItemIdUpdatedDate() {
		return processingRecordDAO.getAllOrderByItemIdUpdatedDate();
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
		log.debug("re-indexing collection " + institutionalCollection);
		int rowStart = 0;
		
		int numberOfItems = institutionalItemService.getCountForCollectionAndChildren(institutionalCollection).intValue();
		
		log.debug("processing a total of " + numberOfItems);
		
		// add one batch size to the items to make sure all items are
		// processed.
		while(rowStart <= (numberOfItems + collectionItemBatchSize))
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  collectionItemBatchSize);
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + collectionItemBatchSize - 1) );
			List<InstitutionalItem> items = institutionalItemService.getCollectionItemsOrderByName(rowStart, collectionItemBatchSize, institutionalCollection, OrderType.DESCENDING_ORDER);
		
			for(InstitutionalItem i : items)
			{
				log.debug("re-indexing item " + i);
				save(i.getId(), processingType);
			}
		    rowStart = rowStart + collectionItemBatchSize;
		    
		}
	}

	
	/**
	 * 
	 * @see edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService#save(java.lang.Long, edu.ur.ir.index.IndexProcessingType)
	 */
	public InstitutionalItemIndexProcessingRecord save(Long itemId,
			IndexProcessingType processingType) {
		
		InstitutionalItemIndexProcessingRecord record = null;
		record = this.get(itemId, processingType);
		
		if( record != null)
		{
		    record.setUpdatedDate( new Timestamp(new Date().getTime()) );
		}
		else
		{
			record = new InstitutionalItemIndexProcessingRecord(itemId, processingType);
		}
		save(record);
		
		return record;
		
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public int getCollectionItemBatchSize() {
		return collectionItemBatchSize;
	}

	public void setCollectionItemBatchSize(int collectionItemBatchSize) {
		this.collectionItemBatchSize = collectionItemBatchSize;
	}

}
