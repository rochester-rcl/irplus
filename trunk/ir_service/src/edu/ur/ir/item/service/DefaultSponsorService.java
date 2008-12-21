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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;
import edu.ur.ir.item.SponsorService;

/**
 * Default service for dealing with sponsor.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultSponsorService implements SponsorService {
	
	/**  sponsor data access. */
	private SponsorDAO sponsorDAO;


	/**
	 * Delete a sponsor with the specified id.
	 * 
	 * @see edu.ur.ir.item.SponsorService#deleteSponsor(java.lang.Long)
	 */
	public boolean deleteSponsor(Long id) {
		Sponsor sponsor  = this.getSponsor(id, false);
		if( sponsor  != null)
		{
			sponsorDAO.makeTransient(sponsor);
		}
		return true;
	}

	/**
	 * Delete a sponsor with the specified name.
	 * 
	 * @see edu.ur.ir.item.SponsorService#deleteSponsor(java.lang.String)
	 */
	public boolean deleteSponsor(String name) {
		Sponsor sponsor = this.getSponsor(name);
		if( sponsor != null)
		{
			sponsorDAO.makeTransient(sponsor);
		}
		return true;
	}

	/**
	 * Get the sponsor with the name.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsor(java.lang.String)
	 */
	public Sponsor getSponsor(String name) {
		return sponsorDAO.findByUniqueName(name);
	}

	/**
	 * Get the sponsor by id.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsor(java.lang.Long, boolean)
	 */
	public Sponsor getSponsor(Long id, boolean lock) {
		return sponsorDAO.getById(id, lock);
	}

	/**
	 * Sponsor data access.
	 * 
	 * @return
	 */
	public SponsorDAO getSponsorDAO() {
		return sponsorDAO;
	}

	/**
	 * Set the sponsor data access.
	 * 
	 * @param sponsorDAO
	 */
	public void setSponsorDAO(SponsorDAO sponsorDAO) {
		this.sponsorDAO = sponsorDAO;
	}

	/**
	 * Save the sponsor .
	 * 
	 * @see edu.ur.ir.item.SponsorService#saveSponsor(edu.ur.ir.item.Sponsor)
	 */
	public void saveSponsor(Sponsor sponsor) {
		sponsorDAO.makePersistent(sponsor);
	}

	/**
	 * Get all sponsor.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getAllSponsor()
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getAllSponsor() { 
		return (List<Sponsor>) sponsorDAO.getAll();
	}

	/**
	 * Get the sponsors order by name
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsorsOrderByName(int, int, String)
	 */
	public List<Sponsor> getSponsorsOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return sponsorDAO.getSponsorsOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the sponsors count
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsorsCount()
	 */
	public Long getSponsorsCount() {
		return sponsorDAO.getCount();
	}


}
