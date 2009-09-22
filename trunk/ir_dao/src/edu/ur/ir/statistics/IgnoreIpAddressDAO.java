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
	
	/**
	 * Get the count of times the ip address shows up in ignore addresses.  A count of 0 means that
	 * it should not be ignored.
	 * 
	 * @param ipAddress
	 * @return the count of times this address was found to be within a given ignore range
	 */
	public Long getIgnoreCountForIp(String ipAddress);
	
	/**
	 * Get the count of times the ip address shows up in ignore addresses.  A count of 0 means that
	 * it should not be ignored.
	 * 
	 * the address is expected in the format XXX.XXX.XXX.XXX
	 * one example would be 192.9.44.23
	 * 
	 * @param part1 - first part of the ip address (192)
	 * @param part2 - second part of the ip address (9)
	 * @param part3 - third part of the ip address  (44)
	 * @param part4 - forth part of the ip address (23)
	 * @return
	 */
	public Long getIgnoreCountForIp(final Integer part1, 
			final Integer part2, 
			final Integer part3, 
			final Integer part4);

}
