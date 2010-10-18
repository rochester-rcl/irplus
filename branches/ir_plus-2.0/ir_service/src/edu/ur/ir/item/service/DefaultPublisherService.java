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

import edu.ur.ir.item.Publisher;
import edu.ur.ir.item.PublisherDAO;
import edu.ur.ir.item.PublisherService;

/**
 * Default service for dealing with publisher.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultPublisherService implements PublisherService{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 5187522152567883857L;
	
	/**  publisher data access. */
	private PublisherDAO publisherDAO;

	/**
	 * Delete a publisher with the specified id.
	 * 
	 * @see edu.ur.ir.item.PublisherService#deletePublisher(java.lang.Long)
	 */
	public boolean deletePublisher(Long id) {
		Publisher publisher  = this.getPublisher(id, false);
		if( publisher  != null)
		{
			publisherDAO.makeTransient(publisher);
		}
		return true;
	}

	/**
	 * Get the publisher by id.
	 * 
	 * @see edu.ur.ir.item.PublisherService#getPublisher(java.lang.Long, boolean)
	 */
	public Publisher getPublisher(Long id, boolean lock) {
		return publisherDAO.getById(id, lock);
	}

	/**
	 * Get the publisher order by name
	 * 
	 * @see edu.ur.ir.item.PublisherService#getPublishersOrderByName(int, int, String)
	 */
	public List<Publisher> getPublishersOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return publisherDAO.getPublishersOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the publisher count
	 * 
	 * @see edu.ur.ir.item.PublisherService#getPublishersCount()
	 */
	public Long getPublishersCount() {
		return publisherDAO.getCount();
	}

	/**
	 * Publisher data access.
	 * 
	 * @return
	 */
	public PublisherDAO getPublisherDAO() {
		return publisherDAO;
	}

	/**
	 * Set the publisher data access.
	 * 
	 * @param publisherDAO
	 */
	public void setPublisherDAO(PublisherDAO publisherDAO) {
		this.publisherDAO = publisherDAO;
	}

	/**
	 * Save the publisher .
	 * 
	 * @see edu.ur.ir.item.PublisherService#savePublisher(edu.ur.ir.item.Publisher)
	 */
	public void savePublisher(Publisher publisher) {
		publisherDAO.makePersistent(publisher);
	}

	/**
	 * Get all publisher.
	 * 
	 * @see edu.ur.ir.item.PublisherService#getAllPublisher()
	 */
	@SuppressWarnings("unchecked")
	public List<Publisher> getAllPublisher() { 
		return (List<Publisher>) publisherDAO.getAll();
	}

	/**
	 * Delete a publisher with the specified name.
	 * 
	 * @see edu.ur.ir.item.PublisherService#deletePublisher(java.lang.String)
	 */
	public boolean deletePublisher(String name) {
		Publisher publisher = this.getPublisher(name);
		if( publisher != null)
		{
			publisherDAO.makeTransient(publisher);
		}
		return true;
	}

	/**
	 * Get the publisher with the name.
	 * 
	 * @see edu.ur.ir.item.PublisherService#getPublisher(java.lang.String)
	 */
	public Publisher getPublisher(String name) {
		return publisherDAO.findByUniqueName(name);
	}


}
