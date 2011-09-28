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

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.ir.repository.LicenseVersion;
import edu.ur.persistent.BasePersistent;

/**
 * Represents an accepted repository license by a user.
 * 
 * @author Nathan Sarr
 *
 */
public class UserRepositoryLicense extends BasePersistent{

	/** eclipse generated id */
	private static final long serialVersionUID = 3955508520575658475L;
	
	/** Date the user accepted the license */
	private Timestamp dateAccepted;
	
	/** Version of the license accepted by the user */
	private LicenseVersion licenseVersion;
	
	/** User who accepted the license */
	private IrUser user;
	
	
	/** Package protected version */
	UserRepositoryLicense(){};
	
	public UserRepositoryLicense(LicenseVersion licenseVersion, IrUser user)
	{
		dateAccepted = new Timestamp(new Date().getTime());
		this.licenseVersion = licenseVersion;
		this.user = user;
	}

	/**
	 * Date the user accepted the license.
	 * 
	 * @return date accepted
	 */
	public Timestamp getDateAccepted() {
		return dateAccepted;
	}

	/**
	 * Version of the license accepted.
	 * @return
	 */
	public LicenseVersion getLicenseVersion() {
		return licenseVersion;
	}

	/**
	 * User who accepted the license.
	 * 
	 * @return
	 */
	public IrUser getUser() {
		return user;
	}
	
	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += licenseVersion == null ? 0 : licenseVersion.hashCode();
		value += user == null? 0 : user.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof UserRepositoryLicense)) return false;

		final UserRepositoryLicense other = (UserRepositoryLicense) o;

		if( ( user != null && !user.equals(other.getUser()) ) ||
			( user == null && other.getUser() != null ) ) return false;
		
		if( ( licenseVersion != null && !licenseVersion.equals(other.getLicenseVersion()) ) ||
			( licenseVersion == null && other.getLicenseVersion() != null ) ) return false;

		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " dateAccepted = " );
		sb.append(dateAccepted);
		sb.append(" user = ");
		sb.append(user);
		sb.append("]");
		return sb.toString();
	}

}
