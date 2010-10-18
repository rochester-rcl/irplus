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

import java.io.Serializable;
import java.util.List;

/**
 * Ip adddress ignore service interface for adding ip address to be ignored.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface IgnoreIpAddressService extends Serializable{
	
	/**
	 * Get ip addresses sorting according to the sort  information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - end position in paged set
	 * @param sortType - Order by (asc/desc)
	 * @return List of ip addresses containing the specified information.
	 */
	public List<IgnoreIpAddress> getIgnoreIpAddressesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);

    /**
     * Get a count of  ip addresses.
     *  
     * @return - the number of collections found
     */
    public Long getIgnoreIpAddressesCount();
    
    /**
     * Delete a  ip address with the specified name.
     * 
     * @param id
     */
    public void deleteIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);
    
    /**
     * Get a ip addresses by id
     * 
     * @param id - unique id of the  ip address.
     * @param lock - upgrade the lock on the data
     * @return - the found ip address range or null if the ip address is not found.
     */
    public IgnoreIpAddress getIgnoreIpAddress(Long id, boolean lock);
    
    /**
     * Save the  ip address.
     * 
     * @param ignoreIpAddress
     */
    public void saveIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);
 
	/**
	 * Get all  ip addresses.
	 * 
	 * @return List of all  ip addresses
	 */
	public List<IgnoreIpAddress> getAllIgnoreIpAddress();

	/**
	 * Get ip address for specified range
	 * 
	 * @param ignoreIpAddress Ip address range
	 * @return - the found ip address range or null if the ip address is not found.
	 */
	public IgnoreIpAddress getIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress);
}
