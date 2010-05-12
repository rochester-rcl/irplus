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

package edu.ur.ir.oai.metadata.provider.service;

import java.util.List;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.oai.metadata.provider.ListSetsService;

/**
 * Default implementation for handling list sets service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultListSetsService implements ListSetsService {

	/** eclipse generated id */
	private static final long serialVersionUID = 3510610389163807643L;
	
	/** Service to deal with institutional collection information */
	private InstitutionalCollectionService institutionalCollectionService;
	

	/**
	 * Handles returning the correct set spec value
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.ListSetsService#getSetSpec(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public String getSetSpec(InstitutionalCollection collection) {
		 List<InstitutionalCollection> collections = institutionalCollectionService.getPath(collection);
		 
		 StringBuffer setSpecValue = new StringBuffer("");
		 for(InstitutionalCollection c : collections)
		 {
			 if(setSpecValue.length() > 0)
			 {
				setSpecValue.append(":"); 
			 }
		     setSpecValue.append(c.getId()); 
		    
		 }
		 return setSpecValue.toString();
	}
	
	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
