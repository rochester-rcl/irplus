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


import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * Externally published item  data access.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface ExternalPublishedItemDAO extends CountableDAO, 
CrudDAO<ExternalPublishedItem>
{
	/**
	 * Get a count of external published items with the given publisher id.
	 * 
	 * @param publisherId - if of the publisher
	 * @return - count of external published items with the given sponsor.
	 */
	public Long getCountForPublisher(Long publisherId);
}
