package edu.ur.ir.institution.service;

import java.util.List;
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

import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecord;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordDAO;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;

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

}
