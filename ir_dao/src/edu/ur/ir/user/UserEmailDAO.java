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

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CriteriaHelper;
import edu.ur.dao.CrudDAO;

/**
 * User Email persistence
 *  
 * @author Sharmila Ranganathan
 *
 */
public interface UserEmailDAO extends CrudDAO<UserEmail>, CountableDAO {

	/**
	 * Get the user for the specified email
	 * 
	 * @param email Email id to find the user
	 */
	public UserEmail getUserByEmail(String email);
	
    /**
     * Get a count of emails 
     *  
     * @param userId id of the user
     * @param filters - list of criteria to apply to the object
     * 
     * @return - the number of emails found
     */
    public Integer getEmailCount(Long userId, List<CriteriaHelper> criteria);
    
	/**
	 * Get emails in the given list with the specified ids.  If the list
	 *  is empty, no emails are returned.
	 * 
	 * @param list of email ids.
	 * 
	 * @return the found emails
	 */
	public List<UserEmail> getEmails(List<Long> emailIds);
	
	/**
	 * Get a list of emails 
	 * 
	 * @param userId - id of the user
	 * @param criteriaHelpers - criteria to base the selection.
 	 * @param rowStart - start id.
	 * @param rowEnd - end id.
	 * 
	 * @return the list of emails found.
	 */
	public List<UserEmail> getEmails(Long userId, List<CriteriaHelper> criteriaHelpers,
			int rowStart, int rowEnd);

	/**
	 * Get the user email for the specified token
	 * 
	 * @param token Token to verify the user
	 * 
	 * @return Email having the token
	 */
	public UserEmail getUserEmailByToken(String token); 
}
