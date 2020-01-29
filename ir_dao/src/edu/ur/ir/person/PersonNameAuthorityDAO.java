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

package edu.ur.ir.person;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.ir.item.IdentifierType;
import edu.ur.order.OrderType;

import java.util.List;


/**
 * Interface for persisting base person information.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonNameAuthorityDAO extends CountableDAO, 
CrudDAO<PersonNameAuthority>{
	
	/**
	 * Gets a list of all users with in authoritative name order.
	 * 
	 * @param startRecord
	 * @param numRecords
	 * 
	 * @return users in given order
	 */
	public List<PersonNameAuthority> getAllAuthoritativeNameAsc(int startRecord, int numRecords);
	

	/**
	 * Get person names by last name
	 * 
	 * @param rowStart - start position 
	 * @param maxResults - maximum number of results
	 * @param orderType - order (asc or desc)
	 * 
	 * @return - all person name authorities at row start up max results.
	 */
	public List<PersonNameAuthority> getPersonNameAuthorityByLastName(int rowStart,
			int maxResults, OrderType orderType);
	
	/**
	 * Get a list of identifier types that can be applied to this
	 * person name authority. Identifier types that have already been used will not
	 * be returned.
	 * 
	 * @param authorityId the person name authority to get possible identifier types for.
	 * @return
	 */
	public List<PersonNameAuthorityIdentifierType> getPossibleIdentifierTypes(Long authorityId);

}
