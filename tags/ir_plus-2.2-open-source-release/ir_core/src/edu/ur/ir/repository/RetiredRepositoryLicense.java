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

package edu.ur.ir.repository;

import java.sql.Timestamp;
import java.util.Date;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * This represents a repository repository license.
 * 
 * Once a license version has been used by the repository,
 * it cannot be used again.
 * 
 * @author Nathan Sarr
 *
 */
public class RetiredRepositoryLicense extends BasePersistent{
	
	/** generated eclipse id */
	private static final long serialVersionUID = -3130233644813326651L;

	/**  Date the license was retired. */
	private Timestamp dateRetired;
	
	/** version of the license that was retired  */
	private LicenseVersion licenseVersion;
	
	/** Repository to retire against */
	private Repository repository;
	
	/** User who retired the license */
	private IrUser retiredBy;
	
	/** Versioning for the data in the database */
	private int version;
	
	
	/**
	 * Package protected constructor
	 */
	RetiredRepositoryLicense(){}
	
	/**
	 * Create the retired repository license.
	 * 
	 * @param repository - repository to retire against
	 * @param licenseVersion - license version to retire
	 */
	public RetiredRepositoryLicense(Repository repository, LicenseVersion licenseVersion, IrUser user)
	{
		this.licenseVersion = licenseVersion;
		dateRetired = new Timestamp(new Date().getTime());
		this.repository = repository;
		this.retiredBy = user;
	}

	/**
	 * Get date the license was retired.
	 * 
	 * @return 
	 */
	public Timestamp getDateRetired() {
		return dateRetired;
	}

	/**
	 * Set the date the repository was retired.
	 * 
	 * @param dateRetired
	 */
	void setDateRetired(Timestamp dateRetired) {
		this.dateRetired = dateRetired;
	}

	/**
	 * Get the license version that was retired.
	 * 
	 * @return
	 */
	public LicenseVersion getLicenseVersion() {
		return licenseVersion;
	}

	/**
	 * Set the license version.
	 * 
	 * @param licenseVersion
	 */
	void setLicenseVersion(LicenseVersion licenseVersion) {
		this.licenseVersion = licenseVersion;
	}

	/**
	 * Repository this retired license is attached to.
	 * 
	 * @return
	 */
	public Repository getRepository() {
		return repository;
	}

	/**
	 * Set the repository this retired license is attached to.
	 * 
	 * @param repository
	 */
	void setRepository(Repository repository) {
		this.repository = repository;
	}

	/**
	 * User who retired the license.
	 * 
	 * @return
	 */
	public IrUser getRetiredBy() {
		return retiredBy;
	}

	/**
	 * User who retired the license.
	 * 
	 * @param retiredBy
	 */
	void setRetiredBy(IrUser retiredBy) {
		this.retiredBy = retiredBy;
	}
	
	/**
	 * Hash code for the repository
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += repository == null ? 0 : repository.hashCode();
		value += licenseVersion == null ? 0 : licenseVersion.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof RetiredRepositoryLicense)) return false;

		final RetiredRepositoryLicense other = (RetiredRepositoryLicense) o;

		if( ( repository != null && !repository.equals(other.getRepository()) ) ||
			( repository == null && other.getRepository() != null ) ) return false;
		
		if( ( licenseVersion != null && !licenseVersion.equals(other.getLicenseVersion()) ) ||
			( licenseVersion == null && other.getLicenseVersion() != null ) ) return false;
		
		return true;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" license versin = ");
		sb.append(licenseVersion);
		sb.append(" retired by = ");
		sb.append(retiredBy);
		sb.append("]");
		return sb.toString();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
