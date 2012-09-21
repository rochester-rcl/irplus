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

package edu.ur.ir.invite;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * Creates an invite token for invited users into the system.
 * 
 * @author Nathan Sarr
 *
 */
public class InviteToken extends BasePersistent{

	/* eclipse generated id. */
	private static final long serialVersionUID = -2315313433310261897L;

	/* Email id - to send the invitation to */
	private String email;
	
	/* Token sent to the user */
	private String token;

	/* Invite message */
	private String inviteMessage;

	/* User sending the invitation */
	private IrUser invitingUser;
	
	/* date the invite info was created */
	private Timestamp createdDate;	

	/* date the token expires */
	private Timestamp expirationDate;
	
	/**
	 * Default constructor
	 */
	InviteToken(){}
	
	/**
	 * Constructor for creating an invite token.
	 * 
	 * @param email
	 * @param token
	 * @param invitingUser
	 */
	public InviteToken(String email, String token, IrUser invitingUser)
	{
		this.email = email.trim().toLowerCase();
		this.token = token;
		this.invitingUser = invitingUser; 
		this.createdDate = new Timestamp(new Date().getTime());		
	}
	
	/**
	 * Get the invite message
	 * 
	 * @return invite message
	 */
	public String getInviteMessage() {
		return inviteMessage;
	}

	/**
	 * Set the invite message
	 * @param inviteMessage
	 */
	public void setInviteMessage(String inviteMessage) {
		this.inviteMessage = inviteMessage;
	}

	/**
	 * Get the expiration date.
	 * 
	 * @return
	 */
	public Timestamp getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Set the expiration 
	 * 
	 * @param experiationDate
	 */
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * Get the email for this token.
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get the token created for this invite.
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Get the inviting user.
	 * 
	 * @return - the inviting user.
	 */
	public IrUser getInvitingUser() {
		return invitingUser;
	}
	
	/**
	 * Get the created date.
	 * 
	 * @return - date this token was created.
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InviteToken)) return false;

		final InviteToken other = (InviteToken) o;

		if( ( token != null && !token.equals(other.getToken()) ) ||
			( token == null && other.getToken() != null ) ) return false;

		if( ( email != null && !email.equals(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;

		return true;
	}


	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += token == null ? 0 : token.hashCode();
		value += email == null ? 0 : email.hashCode();
		return value;
	}
	
	/**
	 * To string of the invite info.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ invite token id = ");
		sb.append(id);
		sb.append("email = ");
		sb.append(email);
		sb.append(" token = ");
		sb.append(token);
		sb.append(" invite message = ");
		sb.append(inviteMessage);
		sb.append("]");
		return sb.toString();
	}
}
