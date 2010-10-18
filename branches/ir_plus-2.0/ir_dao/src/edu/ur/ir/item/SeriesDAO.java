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


/**
 * Series data access.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface SeriesDAO extends CountableDAO, 
CrudDAO<Series>, NameListDAO, UniqueNameDAO<Series>
{

	/**
	 * Get the list of Series.
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - Order (Desc/Asc) 
	 * 
	 * @return list of Series found.
	 */
	public List<Series> getSeriesOrderByName(
			final int rowStart, final int numberOfResultsToShow, final String sortType);
}
