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

package edu.ur.ir.statistics;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * 
 * Interface to save ip address to ignore
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface IgnoreIpAddressDAO extends CrudDAO<IgnoreIpAddress>, CountableDAO {

	/**
	 * Get the list of ip addresses.
	 * 
	 * 
	 * @param criteria - how to sort and filter the list
	 * @param rowStart - start position
	 * @param rowEnd - number of rows to grab.
	 * 
	 * @return list of ip addresses found.
	 */
	public List<IgnoreIpAddress> getIgnoreIpAddresses(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
	/**
	 * Get ip address for specified range
	 * 
	 * @param ignoreIpAddress Ip address range
	 * @return - the found ip address range or null if the ip address is not found.
	 */
	public IgnoreIpAddress getIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);
}
