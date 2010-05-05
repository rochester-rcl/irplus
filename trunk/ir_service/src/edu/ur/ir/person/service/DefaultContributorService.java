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


package edu.ur.ir.person.service;

import java.util.List;

import edu.ur.ir.person.Contributor;
import edu.ur.ir.person.ContributorDAO;
import edu.ur.ir.person.ContributorService;
import edu.ur.ir.person.ContributorType;
import edu.ur.ir.person.PersonName;

/**
 * Implementation of the contributor service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultContributorService implements ContributorService{

	/** eclipse generated id */
	private static final long serialVersionUID = -7403936054618550292L;

	/**  Contributor data access object */
	private ContributorDAO contributorDAO;
	
	/**
	 * Get the contributor for the person name contributor type combination.  Returns null
	 * if no contributor is found.
	 *  
	 * @see edu.ur.ir.person.ContributorService#get(edu.ur.ir.person.PersonName, edu.ur.ir.person.ContributorType)
	 */
	public Contributor get(PersonName personName,
			ContributorType contributorType) 
	{
		return contributorDAO.findByNameType(personName.getId(), contributorType.getId());
	}
	
	/**
	 * Get the contributors for the person name
	 *  
	 *  
	 *  @return List of contributors for the person name
	 */
	public List<Contributor> get(PersonName personName)
	{
		return contributorDAO.getAllForName(personName.getId());
	}

	public ContributorDAO getContributorDAO() {
		return contributorDAO;
	}

	public void setContributorDAO(ContributorDAO contributorDAO) {
		this.contributorDAO = contributorDAO;
	}

	
	/**
	 * Delete the contributor.
	 * 
	 * @see edu.ur.ir.person.ContributorService#delete(edu.ur.ir.person.Contributor)
	 */
	public void delete(Contributor contributor) {
		contributorDAO.makeTransient(contributor);
	}

	
	/**
	 * 
	 * @see edu.ur.ir.person.ContributorService#save(edu.ur.ir.person.Contributor)
	 */
	public void save(Contributor contributor) {
		contributorDAO.makePersistent(contributor);
	}

    /**
     * Get a contributor type by id
     * 
     * @see edu.ur.ir.person.ContributorService#getContributor(Long, boolean)
     */
    public Contributor getContributor(Long id, boolean lock) {
    	return contributorDAO.getById(id, lock);
    	
    }
}
