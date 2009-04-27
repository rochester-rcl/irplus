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
		int rowEnd = batchSize;
		
		int numberOfItems = institutionalItemService.getCount(repository.getId()).intValue();
		
		log.debug("processing a total of " + numberOfItems);
		
		boolean overwriteExistingIndex = true;
		while(rowStart < numberOfItems)
		{
			log.debug("row start = " + rowStart);
			log.debug("row end = " + rowEnd);
			
		    List<InstitutionalItem> items = institutionalItemService.getRepositoryItemsOrderByName(rowStart, rowEnd, repository.getId(), "desc");
		
		    institutionalItemIndexService.addItems(items, new File(repository.getInstitutionalItemIndexFolder()), overwriteExistingIndex);
		    overwriteExistingIndex = false;
		    
		    rowStart = rowEnd;
		    rowEnd = rowEnd + batchSize;
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
