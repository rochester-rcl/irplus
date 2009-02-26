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

package edu.ur.ir.user;

import java.util.List;

import org.springframework.security.userdetails.UserDetailsService;


import edu.ur.dao.CountableDAO;
import edu.ur.dao.CriteriaHelper;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.ir.user.IrUser;

/**
 * User persistence.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrUserDAO extends CountableDAO, 
CrudDAO<IrUser>, NameListDAO, UniqueNameDAO<IrUser>, UserDetailsService
{
    /**
     * Get a count of users 
     *  
     * @param filters - list of criteria to apply to the object
     * 
     * @return - the number of users found
     */
    public Integer getUserCount(List<CriteriaHelper> criteria);
    
	/**
	 * Get users in the given list with the specified ids.  If the list
	 *  is empty, no users are returned.
	 * 
	 * @param list of user ids.
	 * 
	 * @return the found users
	 */
	public List<IrUser> getUsers(List<Long> userIds);
	
	/**
	 * Get a list of users 
	 * 
	 * @param criteriaHelpers - criteria to base the selection.
 	 * @param rowStart - start id.
	 * @param rowEnd - end id.
	 * 
	 * @return the list of users found.
	 */
	public List<IrUser> getUsers(List<CriteriaHelper> criteriaHelpers,
			int rowStart, int rowEnd);

	/**
	 * Load the ir user found by the token.  
	 * 
	 * @param token password token given to the user
	 */
	public IrUser getUserByToken(String token);
	
	/**
     * Get a count of users pending approval 
     *  
     * @return - the number of users found
     */
	public Long getUsersPendingAffiliationApprovalCount();
	

	/**
	 * Get users whose affiliation approval is pending 
	 * 
	 * @param rowStart - start position
	 * @param numberOfResultsToShow - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return the list of users found.
	 */

	public List<IrUser> getUsersPendingAffiliationApprovals(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) ;
	
	/**
	 * Get the user having the specified role Id
	 * 
	 * @param role role name of the user
	 * 
	 * @return List of users with the specified role
	 */
	public List<IrUser> getUserByRole(String roleName);	
	
	/**
	 * Get user having the specified person name authority
	 * 
	 * @param personNameAuthorityId Id of person name authority
	 * @return User
	 */
	public IrUser getUserByPersonNameAuthority(Long personNameAuthorityId);	
	
	/**
	 * Finds a user by their ldap user name.  Null is returned if the user
	 * does not have an ldap user name.
	 * 
	 * @param ldapUserName
	 * @return the user found or null if the ldap user name does not exist
	 */
	public IrUser findByLdapUserName(String ldapUserName);

	/**
	 * Get a list of users for a specified sort criteria.
	 * 
	 * @param rowStart - Start row to fetch the data from
	 * @param numberOfResultsToShow - maximum number of results to fetch
	 * @param sortElement - column to sort on 
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of institutional items
	 */
	public List<IrUser> getUsers(final int rowStart, 
    		final int numberOfResultsToShow, final String sortElement, final String sortType);
}