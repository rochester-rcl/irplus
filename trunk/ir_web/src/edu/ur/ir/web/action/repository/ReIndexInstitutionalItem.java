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

package edu.ur.ir.web.action.repository;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.index.IndexProcessingType;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.institution.InstitutionalItemIndexProcessingRecordService;

import org.apache.log4j.Logger;
/**
 * This will schedule the immediate action of re-indexing institutional items.
 * 
 * @author Nathan Sarr
 *
 */
public class ReIndexInstitutionalItem extends ActionSupport{
	

	/** eclipse generated id */
	private static final long serialVersionUID = 673513182573887635L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(ReIndexInstitutionalItem.class);
	
	/** processing service for indexing institutional items */
	private InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService;
	
	/** Service for dealing with index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	public String execute()
	{
		log.debug("re index institutional items called");
		IndexProcessingType updateType = indexProcessingTypeService.get(IndexProcessingTypeService.UPDATE);
		institutionalItemIndexProcessingRecordService.processItemsInRepository(updateType, true);
		return SUCCESS;
	}

	public InstitutionalItemIndexProcessingRecordService getInstitutionalItemIndexProcessingRecordService() {
		return institutionalItemIndexProcessingRecordService;
	}

	public void setInstitutionalItemIndexProcessingRecordService(
			InstitutionalItemIndexProcessingRecordService institutionalItemIndexProcessingRecordService) {
		this.institutionalItemIndexProcessingRecordService = institutionalItemIndexProcessingRecordService;
	}

	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}

	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}

}
