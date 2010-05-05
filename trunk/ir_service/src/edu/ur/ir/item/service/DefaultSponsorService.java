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

import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.item.Sponsor;
import edu.ur.ir.item.SponsorDAO;
import edu.ur.ir.item.SponsorService;
import edu.ur.order.OrderType;

/**
 * Default service for dealing with sponsor.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class DefaultSponsorService implements SponsorService {
	
	/** eclipse generated id */
	private static final long serialVersionUID = -1849663074170285890L;
	
	/**  sponsor data access. */
	private SponsorDAO sponsorDAO;


	/**
	 * Delete a sponsor 
	 * 
	 */
	public void delete(Sponsor sponsor) {
		sponsorDAO.makeTransient(sponsor);
	}

	/**
	 * Get the sponsor with the name.
	 * 
	 * @see edu.ur.ir.item.SponsorService#get(java.lang.String)
	 */
	public Sponsor get(String name) {
		return sponsorDAO.findByUniqueName(name);
	}

	/**
	 * Get the sponsor by id.
	 * 
	 * @see edu.ur.ir.item.SponsorService#get(java.lang.Long, boolean)
	 */
	public Sponsor get(Long id, boolean lock) {
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
	public void save(Sponsor sponsor) {
		sponsorDAO.makePersistent(sponsor);
	}

	/**
	 * Get all sponsor.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getAllSponsor()
	 */
	@SuppressWarnings("unchecked")
	public List<Sponsor> getAll() { 
		return (List<Sponsor>) sponsorDAO.getAll();
	}

	/**
	 * Get the sponsors order by name
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsorsOrderByName(int, int, String)
	 */
	public List<Sponsor> getOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final OrderType orderType) {
		return sponsorDAO.getSponsorsOrderByName(rowStart, 
	    		numberOfResultsToShow, orderType);
	}
	
	/**
	 * Get the sponsors count
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsorsCount()
	 */
	public Long getCount() {
		return sponsorDAO.getCount();
	}

	
	/**
	 * Get sponsors by the first character in their name.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getByNameFirstChar(int, int, char, edu.ur.order.OrderType)
	 */
	public List<Sponsor> getByNameFirstChar(int rowStart, int maxResults,
			char firstChar, OrderType orderType) {
		return sponsorDAO.getByNameFirstChar(rowStart, maxResults, firstChar, orderType);
	}

	/**
	 * Get count of sponsors with the given first character in their name
	 * 
	 * @see edu.ur.ir.item.SponsorService#getCount(char)
	 */
	public Long getCount(char nameFirstChar) {
		return sponsorDAO.getCount(nameFirstChar);
	}

	/**
	 * Get a count of sponsors that have a first character in the
	 * specified range.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getCount(char, char)
	 */
	public Long getCount(char firstCharRange, char lastCharRange) {
		return sponsorDAO.getCount(firstCharRange, lastCharRange);
	}

	/**
	 * Get the sponsors that have a first character in the given range.
	 * 
	 * @see edu.ur.ir.item.SponsorService#getSponsorsByNameBetweenChar(int, int, char, char, edu.ur.order.OrderType)
	 */
	public List<Sponsor> getSponsorsByNameBetweenChar(int rowStart,
			int maxResults, char firstChar, char lastChar,
			OrderType orderType) {
		return sponsorDAO.getSponsorsByNameBetweenChar(rowStart, maxResults, firstChar, lastChar, orderType);
	}

	/**
	 * 
	 * @see edu.ur.ir.item.SponsorService#getCollectionSponsorsBetweenChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, char, edu.ur.order.OrderType)
	 */
	public List<Sponsor> getCollectionSponsorsBetweenChar(int rowStart,
			int maxResults, InstitutionalCollection collection,
			char firstChar, char lastChar, OrderType orderType) {
		return sponsorDAO.getCollectionSponsorsBetweenChar(rowStart, maxResults, collection, firstChar, lastChar, orderType);
	}

	
	/**
	 * @see edu.ur.ir.item.SponsorService#getCollectionSponsorsOrderByLastName(int, int, edu.ur.ir.institution.InstitutionalCollection, edu.ur.order.OrderType)
	 */
	public List<Sponsor> getCollectionSponsorsOrderByName(int rowStart,
			int maxResults, InstitutionalCollection collection,
			OrderType orderType) {
		return sponsorDAO.getCollectionSponsorsByName(rowStart, maxResults, collection, orderType);
	}

	
	/**
	 * @see edu.ur.ir.item.SponsorService#getCount(edu.ur.ir.institution.InstitutionalCollection, char)
	 */
	public Long getCount(InstitutionalCollection collection, char nameFirstChar) {
		return sponsorDAO.getCount(collection, nameFirstChar);
	}

	
	/**
	 * @see edu.ur.ir.item.SponsorService#getCount(edu.ur.ir.institution.InstitutionalCollection, char, char)
	 */
	public Long getCount(InstitutionalCollection collection,
			char nameFirstCharRange, char nameLastCharRange) {
		return sponsorDAO.getCount(collection, nameFirstCharRange, nameLastCharRange);
	}

	
	/**
	 * @see edu.ur.ir.item.SponsorService#getSponsorsByChar(int, int, edu.ur.ir.institution.InstitutionalCollection, char, edu.ur.order.OrderType)
	 */
	public List<Sponsor> getCollectionSponsorsByChar(int rowStart, int maxResults,
			InstitutionalCollection institutionalCollection, char firstChar,
			OrderType orderType) {
		return sponsorDAO.getCollectionSponsorsByChar(rowStart, maxResults, institutionalCollection, firstChar, orderType);
	}

	
	/**
	 * 
	 * @see edu.ur.ir.item.SponsorService#getCount(edu.ur.ir.institution.InstitutionalCollection)
	 */
	public Long getCount(InstitutionalCollection collection) {
		return sponsorDAO.getCount(collection);
	}


}
