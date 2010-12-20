/**  
   Copyright 2008-2010 University of Rochester

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

import java.io.File;
import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionIndexService;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.institution.ReIndexInstitutionalCollectionService;

/**
 * Default implementation for re-indexing institutional collections.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultReIndexInstitutionalCollectionService implements ReIndexInstitutionalCollectionService{
	
	/** eclipse generated id  */
	private static final long serialVersionUID = 6423450347907929999L;
	
	/* Service to get institutional collections */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/* Service to index insitutional collection information */
	private InstitutionalCollectionIndexService institutionalCollectionIndexService;

	public int reIndex(File index) {
		 List<InstitutionalCollection> collections = institutionalCollectionService.getAll();
		 institutionalCollectionIndexService.add(collections, index, true);
		 return collections.size();
	}
	
	/**
	 * Set the institutional collection service.
	 * 
	 * @param institutionalCollectionService
	 */
	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

	/**
	 * Set the institutional collection index service.
	 * 
	 * @param institutionalCollectionIndexService
	 */
	public void setInstitutionalCollectionIndexService(
			InstitutionalCollectionIndexService institutionalCollectionIndexService) {
		this.institutionalCollectionIndexService = institutionalCollectionIndexService;
	}

}
