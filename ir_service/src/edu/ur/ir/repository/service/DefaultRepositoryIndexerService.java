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


package edu.ur.ir.repository.service;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ur.ir.institution.InstitutionalItem;
import edu.ur.ir.institution.InstitutionalItemIndexService;
import edu.ur.ir.institution.InstitutionalItemService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryIndexerService;
import edu.ur.order.OrderType;

/**
 * Default implementation of the repository indexer.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultRepositoryIndexerService implements RepositoryIndexerService{

	/**  Service for dealing with institutional items. */
	private InstitutionalItemService institutionalItemService;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultRepositoryIndexerService.class);
	
	/** index service for institutional items */
	private InstitutionalItemIndexService institutionalItemIndexService;
	
	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.RepositoryIndexerService#reIndexInstitutionalItems(edu.ur.ir.repository.Repository)
	 */
	public void reIndexInstitutionalItems(Repository repository, int batchSize)  {
		log.debug("re-indexing repository " + repository);
		int rowStart = 0;
		
		int numberOfItems = institutionalItemService.getCount(repository.getId()).intValue();
		
		log.debug("processing a total of " + numberOfItems);
		
		Long repositoryId = repository.getId();
		File folder = new File(repository.getInstitutionalItemIndexFolder());
		
		boolean overwriteExistingIndex = true;
		// boost the size of number of items one extra batchSize to make sure all items are 
		// processed
		while(rowStart <= (numberOfItems + batchSize))
		{
			log.debug("row start = " + rowStart);
			log.debug("batch size = " +  batchSize);
			
			// notice the minus one because we are starting at 0
			log.debug("processing " + rowStart + " to " + (rowStart + batchSize - 1) );
			
		    List<InstitutionalItem> items = institutionalItemService.getRepositoryItemsOrderByName(rowStart, batchSize, repositoryId, OrderType.DESCENDING_ORDER);
	
		    institutionalItemIndexService.addItems(items, folder, overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    
		    rowStart = rowStart + batchSize;
		}
	}

	public InstitutionalItemService getInstitutionalItemService() {
		return institutionalItemService;
	}

	public void setInstitutionalItemService(
			InstitutionalItemService institutionalItemService) {
		this.institutionalItemService = institutionalItemService;
	}

	public InstitutionalItemIndexService getInstitutionalItemIndexService() {
		return institutionalItemIndexService;
	}

	public void setInstitutionalItemIndexService(
			InstitutionalItemIndexService institutionalItemIndexService) {
		this.institutionalItemIndexService = institutionalItemIndexService;
	}

}
