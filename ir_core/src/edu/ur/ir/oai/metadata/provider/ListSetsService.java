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

package edu.ur.ir.oai.metadata.provider;

import java.io.Serializable;

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.oai.exception.BadArgumentException;
import edu.ur.ir.oai.exception.BadResumptionTokenException;
import edu.ur.ir.oai.exception.NoSetHierarchyException;

/**
 * @author Nathan Sarr
 *
 */
public interface ListSetsService extends Serializable{
	
	/**
	 * Returns the set spec structure for a given institutional collection.
	 * 
	 * @param institutionalCollection
	 * @return the set spec
	 */
	public String getSetSpec(InstitutionalCollection institutionalCollection);
	
	/**
	 * Get the set spec based on the institutional collection id.
	 * 
	 * @param institutionalCollectionId
	 * @return the set spec or null if no set spec exists
	 */
	public String getSetSpec(Long institutionalCollectionId);
	
	/**
	 * Return the xml body for listing sets.
	 * 
	 * @return string xml body for listing sets
	 * 
	 * @throws BadResumptionTokenException - if the resumption token is bad
	 * @throws NoSetHierarchyException - if sets are not supported
	 */
	public String listSets(String resumptionToken) throws BadArgumentException, BadResumptionTokenException, NoSetHierarchyException;

}
