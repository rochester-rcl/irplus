package edu.ur.ir.repository;

import edu.ur.persistent.BasePersistent;

/**
 * Represents a particular version of the license.
 * 
 * @author Nathan Sarr
 *
 */
public class LicenseVersion extends BasePersistent{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -8344894631259790336L;

	/** Item this version points to. */
	private License license;
	
	/** Parent versioned license */
	private VersionedLicense versionedLicense;
	
	/** Version number this license represents */
	private int versionNumber;

	/**
	 * Package protected constructor
	 */
	LicenseVersion(){}
	
	/**
	 * Package protected constructor
	 * 
	 * @param item
	 * @param versionedItem
	 * @param versionNumber
	 */
	LicenseVersion(License license, VersionedLicense versionedLicense, int versionNumber)
	{
		if(versionedLicense == null )
		{
			throw new IllegalStateException("versionedItem cannot be null");
		}
		
		if( license == null )
		{
			throw new IllegalStateException("item cannot be null");
		}
		
		if( versionNumber <= 0 )
		{
			throw new IllegalStateException("Version number must be greater than 0 verion = " + versionNumber);
		}
		this.license = license;
		this.versionedLicense = versionedLicense;
		this.versionNumber = versionNumber;
	}

	/**
	 * Get the license
	 * 
	 * @return the license for this version
	 */
	public License getLicense() {
		return license;
	}

	/**
	 * Set the license for this version.
	 * 
	 * @param item
	 */
	void setLicense(License license) {
		this.license = license;
	}

	/**
	 * Get the versioned license for this version.
	 * 
	 * @return versioned license
	 */
	public VersionedLicense getVersionedLicense() {
		return versionedLicense;
	}

	/**
	 * Set the versioned license
	 * 
	 * @param versionedLicense
	 */
	void setVersionedItem(VersionedLicense versionedLicense) {
		this.versionedLicense = versionedLicense;
	}

	/**
	 * Get the version number for this instance
	 * 
	 * @return version number
	 */
	public int getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Set the version number
	 * 
	 * @param versionNumber
	 */
	void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append( " version number = ");
		sb.append(versionNumber);
		sb.append(" license = \n");
		sb.append(license);
		sb.append(" versionedLicnes = \n");
		sb.append(versionedLicense);
		sb.append("]");
		
		return sb.toString();
	}
	
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof LicenseVersion)) return false;

		final LicenseVersion other = (LicenseVersion) o;

		if( getVersionedLicense() != null && other.getVersionedLicense() == null) return false;
		if(!getVersionedLicense().equals(other.getVersionedLicense())) return false;
		
		if(getVersionNumber() != other.getVersionNumber()) return false;
		
		return true;
	}
	
	public int hashCode()
	{
		int value = 0;
		value += getVersionNumber();
		value += getLicense() == null ? 0 : getLicense().hashCode();
		return value;
	}
}