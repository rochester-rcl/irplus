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
import edu.ur.dao.CrudDAO;
import edu.ur.order.OrderType;


/**
 * Interface for persisting invite information
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface FileInviteInfoDAO extends CrudDAO<FileInviteInfo>, CountableDAO {
	
	/**
	 * Find the Invite information for a specified token
	 * 
	 * @param token user token
	 * @return User token information
	 */
	public FileInviteInfo findInviteInfoForToken(String token);
	
	/**
	 * Find the Invite information for a specified email
	 * 
	 * @param email email address shared with
	 * @return List of invite information
	 */
	public List<FileInviteInfo> getInviteInfoByEmail(String email);
		
	/**
	 * Get the list of invite infos ordered by inviteor
	 * 
	 * @param rowStart - start position in the list
	 * @param maxResults - maximum number of results to retrieve
	 * @param orderType - ascending/descending order
	 * 
	 * @return list of invite infos found
	 */
	public List<FileInviteInfo> getInviteInfosOrderByInviteor(int rowStart,
			int maxResults, OrderType orderType);
	
	/**
	 * Get the invites made by a particular user.
	 * 
	 * @param user - invites made by a given user
	 * @return - all invites made by the user or an empty list if no invites found
	 */
	public List<FileInviteInfo> getInvitesMadeByUser(IrUser user);
	
	/**
	 * Find the Invite information for all invites that contain the specified versioned file ids and the 
	 * given emails.
	 * 
	 * @param versionedFileIds - list of versioned file ids the invite should have
	 * @param email - email the invite should have
	 * @return list of invite infos found
	 */
	public List<FileInviteInfo> getInviteInfosWithVersionedFilesAndEmail(List<Long> versionedFileIds, 
			String email);
}
