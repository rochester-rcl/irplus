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


package edu.ur.ir.statistics.service;

import java.util.List;

import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressDAO;
import edu.ur.ir.statistics.IgnoreIpAddressService;

/**
 * Default service for dealing with ip addresses to be ignored.
 * 
 * @author Sharmila Ranganathan 
 *
 */
public class DefaultIgnoreIpAddressService implements IgnoreIpAddressService{
	
	/** Ignore ip addresses  data access. */
	private IgnoreIpAddressDAO ignoreIpAddressDAO;


	/**
	 * Delete a  ip address with the specified id.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#deleteIgnoreIpAddress(java.lang.Long)
	 */
	public void deleteIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
			ignoreIpAddressDAO.makeTransient(ignoreIpAddress);
	}

	/**
	 * Get the ip address by id.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#getIgnoreIpAddress(java.lang.Long, boolean)
	 */
	public IgnoreIpAddress getIgnoreIpAddress(Long id, boolean lock) {
		return ignoreIpAddressDAO.getById(id, lock);
	}

	/**
	 * Get the ip addreses based order by name
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#getIgnoreIpAddre(int, int, String)
	 */
	public List<IgnoreIpAddress> getIgnoreIpAddressesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return ignoreIpAddressDAO.getIgnoreIpAddresses(rowStart, numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the ip addresses based on the criteria.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#getIgnoreIpAddreCount()
	 */
	public Long getIgnoreIpAddressesCount() {
		return ignoreIpAddressDAO.getCount();
	}

	/**
	 * Ignore ip address  data access.
	 * 
	 * @return
	 */
	public IgnoreIpAddressDAO getIgnoreIpAddressDAO() {
		return ignoreIpAddressDAO;
	}

	/**
	 * Set the Ignore ip address data access.
	 * 
	 * @param ignoreIpAddressDAO
	 */
	public void setIgnoreIpAddressDAO(IgnoreIpAddressDAO ignoreIpAddressDAO) {
		this.ignoreIpAddressDAO = ignoreIpAddressDAO;
	}

	/**
	 * Save the Ignore ip address.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#saveIgnoreIpAddress(edu.ur.ir.statistics.IgnoreIpAddress)
	 */
	public void saveIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		ignoreIpAddressDAO.makePersistent(ignoreIpAddress);
	}

	/**
	 * Get all Ignore ip addresses.
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#getAllIgnoreIpAddress()
	 */
	@SuppressWarnings("unchecked")
	public List<IgnoreIpAddress> getAllIgnoreIpAddress() { 
		return (List<IgnoreIpAddress>) ignoreIpAddressDAO.getAll();
	}

	/**
	 * Get ip address for specified range
	 * 
	 * @see edu.ur.ir.statistics.IgnoreIpAddressService#getIgnoreIpAddress(IgnoreIpAddress)
	 */
	public IgnoreIpAddress getIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		return ignoreIpAddressDAO.getIgnoreIpAddress(ignoreIpAddress);
	}
}
