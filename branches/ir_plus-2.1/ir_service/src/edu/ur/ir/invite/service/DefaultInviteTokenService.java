/**  
   Copyright 2008-2011 University of Rochester

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

package edu.ur.ir.invite.service;

import edu.ur.ir.invite.InviteToken;
import edu.ur.ir.invite.InviteTokenDAO;
import edu.ur.ir.invite.InviteTokenService;

/**
 * Service to help deal with token invite information.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultInviteTokenService implements InviteTokenService {

	/* eclipse generated id  */
	private static final long serialVersionUID = 325634482886350121L;
	
	/* data access for the invite token */
	private InviteTokenDAO inviteTokenDAO;

	/**
     * Get the invite token by it's token value.
     * 
     * @param token
     */
	public InviteToken getInviteToken(String token) {
		
		return inviteTokenDAO.getByToken(token);
	}
	
    /**
     * Get the invite token data access object.
     * 
     * @return
     */
    public InviteTokenDAO getInviteTokenDAO() {
		return inviteTokenDAO;
	}

	/**
	 * Set the invite token data access object.
	 * 
	 * @param inviteTokenDAO
	 */
	public void setInviteTokenDAO(InviteTokenDAO inviteTokenDAO) {
		this.inviteTokenDAO = inviteTokenDAO;
	}

    /**
     * Delete the invite token.
     * 
     * @param entity
     */
	public void delete(InviteToken entity) {
		inviteTokenDAO.makeTransient(entity);
	}	

}
