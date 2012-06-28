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


import edu.ur.persistent.BasePersistent;

/**
 * Email for the user.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class UserEmail extends BasePersistent {
	
	/**  Eclipse generated Id */
	private static final long serialVersionUID = 1942528147936453112L;

	/** Holds the user Email */
	private String email;
	
	/** lower case version of the email  */
	private String lowerCaseEmail;

	/**  IrUser */
	private IrUser irUser;

	/**  Indicates whether the email is verified or not */
	private boolean isVerified = false;
	
	/**  Assigns token for email verification */
	private String token;

	/**
	 * Default constructor
	 */
	UserEmail(){};

	/**
	 * Default constructor
	 */
	public UserEmail(String email){
		setEmail(email);
	};
	
	/**
	 * Get the user email
	 * 
	 * @return Email of the user
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email for the user
	 * 
	 * @param email Email id that has to be set
	 */
	public void setEmail(String email) {
		this.email = email.trim();
		this.lowerCaseEmail = email.toLowerCase(); 
	}
	

	/**
	 * Returns User
	 * @return user
	 */
	public IrUser getIrUser() {
		return irUser;
	}

	/**
	 * Sets the User
	 * @param irUser IrUser object to be set
	 */
	public void setIrUser(IrUser irUser) {
		this.irUser = irUser;
	}	

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof UserEmail)) return false;

		final UserEmail other = (UserEmail) o;

		if( ( lowerCaseEmail != null && !lowerCaseEmail.equals(other.getLowerCaseEmail()) ) ||
			( lowerCaseEmail == null && other.getLowerCaseEmail() != null ) ) return false;
		return true;
	}

    /**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += lowerCaseEmail == null ? 0 : lowerCaseEmail.hashCode();
    	return hash;
    }

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[User Email id = ");
		sb.append(getId());
		sb.append(" Email = ");
		sb.append(getEmail());
		sb.append(" User = ");
		sb.append(getIrUser());
		sb.append("]");
		
		return  sb.toString();
	}

	/**
	 * Set to true if the email is verified.
	 * 
	 * @return
	 */
	public boolean isVerified() {
		return isVerified;
	}

	/**
	 * Sets the email as verified. If the verified is set to true,
	 * the token will be set to null.
	 * 
	 * @param isVerified
	 */
	void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
	
	/**
	 * Set the is verified state to true and
	 * set the token value to null
	 */
	public void setVerifiedTrue()
	{
		this.isVerified = true;
		this.token = null;
	}
	
	/**
	 * Set the is verified state to false and set the token
	 * to the token value.
	 * 
	 * @param token
	 */
	public void setVerifiedFalse(String token)
	{
		this.isVerified = false;
		this.token = token;
	}

	/**
	 * Get the token to verify the email.
	 * 
	 * @return
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the email to verify the token.
	 * 
	 * @param token
	 */
	void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Get the lower case version of the email.
	 * 
	 * @return - lower case version of the email
	 */
	public String getLowerCaseEmail() {
		return lowerCaseEmail;
	}
	

}
