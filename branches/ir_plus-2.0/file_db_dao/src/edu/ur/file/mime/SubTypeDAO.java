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

package edu.ur.file.mime;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;

/**
 * Interface for saving a subtype.
 * 
 * @author Nathan Sarr
 *
 */
public interface SubTypeDAO extends CountableDAO, 
CrudDAO<SubType>, NameListDAO{
	
	/**
	 * Find the media type by the unique name and top media type.
	 * 
	 * @param name
	 * @param topMediaTypeId
	 * @return
	 */
	public SubType findByUniqueName(String name, Long topMediaTypeId);
	
	/**
	 * Get the list of sub types.
	 * 
	 * @param topMediaTypeId - the parent sub type 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return list of personal folders found.
	 */
	public List<SubType> getSubTypes( Long topMediaTypeId, 
			final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Get the count of sub media types based on the filter criteria
	 * 
	 * @param topMediaTypeId - the parent top sub type.
	 * @return count of sub types for the given criteria
	 */
	public Long getSubTypesCount(Long topMediaTypeId);
	
}
