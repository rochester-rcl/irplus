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


package edu.ur.ir.security;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.order.OrderType;

/**
 * Represents an external account type data access.
 * 
 * @author Nathan Sarr
 *
 */
public interface ExternalAccountTypeDAO extends CountableDAO, 
CrudDAO<ExternalAccountType>, NameListDAO, UniqueNameDAO<ExternalAccountType>{
	
	/**
	 * Get the list of external account types by name
	 * 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param orderType - Order (Desc/Asc) 
	 * 
	 * @return list of external account types found.
	 */
	public List<ExternalAccountType> getOrderByName(
			final int rowStart, final int numberOfResultsToShow, final OrderType orderType);

}
