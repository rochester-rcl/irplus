/**  
   Copyright 2008 - 2011 University of Rochester

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
import edu.ur.dao.UniqueNameDAO;
import edu.ur.order.OrderType;

/**
 * Data access layer for the place of publication.
 * 
 * @author Nathan Sarr
 *
 */
public interface PlaceOfPublicationDAO extends CountableDAO, 
CrudDAO<PlaceOfPublication>, UniqueNameDAO<PlaceOfPublication>
{
	/**
	 * Get the list of language types order by name
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - order (asc/desc)
	 * 
	 * @return list of language types found.
	 */
	public List<PlaceOfPublication> getOrderByName(final int rowStart, final int numberOfResultsToShow, 
			final OrderType orderType);

    /**
     * Get the place of publication by letter code.
     * 
     * @param letterCode - letter code for the place of publication
     * @return the place of publication
     */
    public List<PlaceOfPublication> getByLetterCode(String letterCode);
  
}
