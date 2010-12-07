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

import org.springframework.security.core.userdetails.UserDetailsService;


import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.ListAllDAO;
import edu.ur.dao.NameListDAO;
import edu.ur.dao.UniqueNameDAO;
import edu.ur.ir.user.IrUser;
import edu.ur.order.OrderType;

/**
 * User persistence.
 * 
 * @author Nathan Sarr
 *
 */
public interface IrUserDAO extends CountableDAO, 
CrudDAO<IrUser>, NameListDAO, UniqueNameDAO<IrUser>, UserDetailsService, ListAllDAO
{
 
    
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
	 * @param maxResults - number of rows to grab.
	 * @param sortType - sort order(Asc/desc)
	 * 
	 * @return the list of users found.
	 */

	public List<IrUser> getUsersPendingAffiliationApprovals(final int rowStart, 
    		final int maxResults, final String sortType) ;
	
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
	 * Get users by last name order.
	 * 
	 * @param rowStart - Start row to fetch the data
	 * @param maxResults - maximum number of results to fetch
	 * @param orderType - ascending/descending order
	 * 
	 * @return users last name order
	 */
	public List<IrUser> getUsersByLastNameOrder(final int rowStart, 
    		final int maxResults, final OrderType orderType);
	

	/**
	 * Get users by user name order
	 * 
	 * @param rowStart Start row to fetch the data
	 * @param maxResults - maximum number of results to fetch
	 * @param orderType - order ascending/descending
	 * @return
	 */
	public List<IrUser> getUsersByUsernameOrder(final int rowStart, 
    		final int maxResults, final OrderType orderType);
	
	/**
	 * Get users by email order
	 * 
	 * @param rowStart Start row to fetch the data
	 * @param maxResults - maximum number of results to fetch
	 * @param orderType - order ascending/descending
	 * @return
	 */
	public List<IrUser> getUsersByEmailOrder(final int rowStart, 
    		final int maxResults, final OrderType orderType);

	/**
	 * Get a count of users with a specified role.
	 * 
	 * @param roleId -  the role id
	 * @return count of users by role
	 */
	public Long getUserByRoleCount(Long roleId);


	/**
	 * Get a list of users ordered by last name, first name by role
	 * 
	 * @param roleId - the role id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleFullNameOrder(Long roleId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered with a specified role by Username
	 * 
	 * @param roleId - the role id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleUsernameOrder(Long roleId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered by username for the specified role
	 * 
	 * @param roleId - the role id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleEmailOrder(Long roleId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a count of users with a specified affiliation.
	 * 
	 * @param affiliationId -  the affiliation id
	 * @return count of users by affiliation
	 */
	public Long getUserByAffiliationCount(Long affilationId);


	/**
	 * Get a list of users ordered by last name, first name by affiliation
	 * 
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByAffiliationFullNameOrder(Long affiliationId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered with a specified role by Username by affiliation
	 * 
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByAffiliationUsernameOrder(Long affiliationId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered by username for the specified affiliation
	 * 
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByAffiliationEmailOrder(Long affiliationId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	
	/**
	 * Get a count of users with a specified affiliation and role.
	 * 
	 * @param roleId - the role id
	 * @param affiliationId -  the affiliation id
	 * @return count of users by affiliation
	 */
	public Long getUserByRoleAffiliationCount(Long roleId, Long affilationId);


	/**
	 * Get a list of users ordered by last name, first name by role and affiliation
	 * 
	 * @param roleId - the role id
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleAffiliationFullNameOrder(Long roleId, Long affiliationId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered with a specified role by Username by role and affiliation
	 * 
	 * @param roleId - the role id
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleAffiliationUsernameOrder(Long roleId, Long affiliationId, int rowStart, 
    		int maxResults, OrderType orderType);
	
	/**
	 * Get a list of users ordered by username for the specified role affiliation
	 * 
	 * @param roleId - the role id
	 * @param affiliationId - the affiliation id
	 * @param rowStart - Start row to fetch the data from
	 * @param maxResults - maximum number of results to fetch
	 * @param sortType - The order to sort by (ascending/descending)
	 * 
	 * @return List of users
	 */
	public List<IrUser> getUsersByRoleAffiliationEmailOrder(Long roleId, Long affiliationId, int rowStart, 
			int maxResults, OrderType orderType);
}