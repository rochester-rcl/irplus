/**  
   Copyright 2008-2010 University of Rochester

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
 * Interface for persisting folder invite information
 * 
 * @author Nathan Sarr
 *
 */
public interface FolderInviteInfoDAO extends CrudDAO<FolderInviteInfo>, CountableDAO {
	
		
		/**
		 * Find the folder Invite information for a specified email
		 * 
		 * @param email email address shared with
		 * @return List of folder invite information
		 */
		public List<FolderInviteInfo> getInviteInfoByEmail(String email);
			
		/**
		 * Get the list of folder invite infos ordered by inviteor
		 * 
		 * @param rowStart - start position in the list
		 * @param maxResults - maximum number of results to retrieve
		 * @param orderType - ascending/descending order
		 * 
		 * @return list of invite infos found
		 */
		public List<FolderInviteInfo> getInviteInfosOrderByInviteor(int rowStart,
				int maxResults, OrderType orderType);


}
