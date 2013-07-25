/**  
   Copyright 2008- 2011 University of Rochester

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


package edu.ur.ir.invite;

import java.util.List;

import edu.ur.dao.CrudDAO;

/**
 * Data access interface for the invite token
 * 
 * @author Nathan Sarr
 *
 */
public interface InviteTokenDAO extends CrudDAO<InviteToken>
{
	/**
	 * Get the invite token by the token id.
	 * 
	 * @param token - token sent to user
	 * @return Invite token if found otherwise null
	 */
	public InviteToken getByToken(String token);
	
	/**
	 * Get the invite tokens by email.
	 * 
	 * @param email - the email value 
	 * @return - the invite tokens found
	 */
	public List<InviteToken> getByEmail(String email);
}
