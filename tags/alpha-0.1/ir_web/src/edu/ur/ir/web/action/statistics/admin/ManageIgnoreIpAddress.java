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


package edu.ur.ir.web.action.statistics.admin;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.statistics.IgnoreIpAddress;
import edu.ur.ir.statistics.IgnoreIpAddressService;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with ignore Ip Address.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageIgnoreIpAddress extends Pager implements  Preparable{
	
	/** generated version id. */
	private static final long serialVersionUID = -4532842741539307216L;

	/** ignore ip address service */
	private IgnoreIpAddressService ignoreIpAddressService;
	
	/**  Logger for managing ip addresses*/
	private static final Logger log = Logger.getLogger(ManageIgnoreIpAddress.class);
	
	/** Set of ip addresses for viewing the ip addresses */
	private Collection<IgnoreIpAddress> ignoreIpAddresses;
	
	/**  Ip address for loading  */
	private IgnoreIpAddress ignoreIpAddress;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the ipaddress has been added*/
	private boolean added = false;
	
	/** Indicates the ip addresses have been deleted */
	private boolean deleted = false;
	
	/** id of the ignore ip address  */
	private Long id;
	
	/** Set of ignore ip address ids */
	private long[] ignoreIpAddressIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";

	/** Total number of ip addresses to ignore  */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Default constructor */
	public  ManageIgnoreIpAddress()
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}


	/**
	 * Method to create a new ignore ip address.
	 * 
	 * Create a new ignore ip address
	 */
	public String create()
	{
		log.debug("creating a ignore ip address = " + ignoreIpAddress.getName());
		added = false;
		IgnoreIpAddress other = ignoreIpAddressService.getIgnoreIpAddress(ignoreIpAddress);
		if( other == null)
		{
		    ignoreIpAddressService.saveIgnoreIpAddress(ignoreIpAddress);
		    added = true;
		}
		else
		{
			message = getText("ignoreIpAddressError");
			addFieldError("ignoreIpAddressAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing ignore ip address.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updating ignore ip address id = " + ignoreIpAddress.getId());
		added = false;

		IgnoreIpAddress other = ignoreIpAddressService.getIgnoreIpAddress(ignoreIpAddress);
		
		if( other == null || other.getId().equals(ignoreIpAddress.getId()))
		{
			ignoreIpAddressService.saveIgnoreIpAddress(ignoreIpAddress);
			added = true;
		}
		else
		{
			message = getText("ignoreIpAddressError");
			
			addFieldError("ignoreIpAddressAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Removes the selected items and collections.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete ip addresses called");
		if( ignoreIpAddressIds != null )
		{
		    for(int index = 0; index < ignoreIpAddressIds.length; index++)
		    {
			    log.debug("Deleting ignore ip address with id " + ignoreIpAddressIds[index]);
			    ignoreIpAddressService.deleteIgnoreIpAddress(ignoreIpAddressIds[index]);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the ip address to edit
	 * @return String get 
	 */
	public String get()
	{
		ignoreIpAddress = ignoreIpAddressService.getIgnoreIpAddress(id, false);
		return "get";
	}
 

	
	/**
	 * Get the ip addresses table data.
	 * 
	 * @return
	 */
	public String viewIgnoreIpAddresses()
	{

		rowEnd = rowStart + numberOfResultsToShow;
	    
		ignoreIpAddresses = ignoreIpAddressService.getIgnoreIpAddressesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = ignoreIpAddressService.getIgnoreIpAddressesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	}
	
	/**
	 * Get the ignore ip address service.
	 * 
	 * @return
	 */
	public IgnoreIpAddressService getIgnoreIpAddressService() {
		return ignoreIpAddressService;
	}

	/**
	 * Set the ignore ip address service.
	 * 
	 * @param ignoreIpAddressService
	 */
	public void setIgnoreIpAddressService(IgnoreIpAddressService ignoreIpAddressService) {
		this.ignoreIpAddressService = ignoreIpAddressService;
	}
	
	/**
	 * List of ip addresses for display.
	 * 
	 * @return
	 */
	public Collection<IgnoreIpAddress> getIgnoreIpAddresses() {
		return ignoreIpAddresses;
	}
	/**
	 * Set the list of ip addresses.
	 * 
	 * @param ignoreIpAddresses
	 */
	public void setIgnoreIpAddresses(Collection<IgnoreIpAddress> ignoreIpAddresses) {
		this.ignoreIpAddresses = ignoreIpAddresses;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getIgnoreIpAddressIds() {
		return ignoreIpAddressIds;
	}

	public void setIgnoreIpAddressIds(long[] ignoreIpAddressIds) {
		this.ignoreIpAddressIds = ignoreIpAddressIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public IgnoreIpAddress getIgnoreIpAddress() {
		return ignoreIpAddress;
	}

	public void setIgnoreIpAddress(IgnoreIpAddress ignoreIpAddress) {
		this.ignoreIpAddress = ignoreIpAddress;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			ignoreIpAddress = ignoreIpAddressService.getIgnoreIpAddress(id, false);
		}
	}


	public String getSortType() {
		return sortType;
	}


	public void setSortType(String sortType) {
		this.sortType = sortType;
	}


	public int getTotalHits() {
		return totalHits;
	}


	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}


	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

}
