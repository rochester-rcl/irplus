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
		this.email = email;
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
		this.email = email;
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

		if( ( email != null && !email.equals(other.getEmail()) ) ||
			( email == null && other.getEmail() != null ) ) return false;
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
    	hash += email == null ? 0 : email.hashCode();
    	hash += irUser== null ? 0 : irUser.hashCode();
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

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
