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

package edu.ur.ir.item;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.dao.UniqueSystemCodeDAO;

/**
 * Data access interface for identifier types.
 * 
 * @author Nathan Sarr
 *
 */
public interface IdentifierTypeDAO extends CountableDAO, 
CrudDAO<IdentifierType>, NameListDAO, UniqueNameDAO<IdentifierType>,UniqueSystemCodeDAO<IdentifierType>
{
	/**
	 * Get the list of identifier types.
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - Order (Desc/Asc) 
	 * 
	 * @return list of identifiers found.
	 */
	public List<IdentifierType> getIdentifierTypesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType);
}