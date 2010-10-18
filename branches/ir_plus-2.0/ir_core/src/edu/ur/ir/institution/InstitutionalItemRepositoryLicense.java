package edu.ur.ir.institution;

import java.sql.Timestamp;

import edu.ur.ir.repository.LicenseVersion;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;

/**
 * License attached to an institutional item.  
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemRepositoryLicense extends BasePersistent{
	
	/** eclipse generated serial id */
	private static final long serialVersionUID = -8825160866933302482L;

	/** institutional item version this license is attached to */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/** version of the license */
	private LicenseVersion licenseVersion;
	
	/** date the license was granted for the item  */
	private Timestamp dateGranted;
	
	/** User who granted the license */
	private IrUser grantedByUser;
	
	
	/** database version number */
	private int version;
	
    /**
     * Package protected constructor
     */
    InstitutionalItemRepositoryLicense(){}
    
    /**
     * Create an institutional item repository license with 
     * the specified institutional item version and the license version.
     * 
     * @param institutionalItemVersion - institutional item version this license is attached to
     * @param licenseVersion - repository license attached.
     */
    InstitutionalItemRepositoryLicense(InstitutionalItemVersion institutionalItemVersion, 
    		LicenseVersion licenseVersion, IrUser grantedByUser, Timestamp dateGranted)
    {
    	setInstitutionalItemVersion(institutionalItemVersion);
    	setLicenseVersion(licenseVersion);
    	setGrantedByUser(grantedByUser);
    	setDateGranted(dateGranted);
    }

	/**
	 * Get the institutional item version for this item license
	 * 
	 * @return
	 */
	public InstitutionalItemVersion getInstitutionalItemVersion() {
		return institutionalItemVersion;
	}

	/**
	 * Set the institutional item version.
	 * 
	 * @param institutionalItemVersion
	 */
	void setInstitutionalItemVersion(
			InstitutionalItemVersion institutionalItemVersion) {
		this.institutionalItemVersion = institutionalItemVersion;
	}

	/**
	 * Get the license version.
	 * 
	 * @return license version
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
	
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof InstitutionalItemRepositoryLicense)) return false;

		final InstitutionalItemRepositoryLicense other = (InstitutionalItemRepositoryLicense) o;
		
		if( ( getInstitutionalItemVersion() != null && !getInstitutionalItemVersion().equals(other.getInstitutionalItemVersion()) ) ||
		    ( getInstitutionalItemVersion() == null && other.getInstitutionalItemVersion() != null ) ) return false;

		if( ( getLicenseVersion() != null && !getLicenseVersion().equals(other.getLicenseVersion()) ) ||
			( getLicenseVersion() == null && other.getLicenseVersion() != null ) ) return false;
		
		if( ( getGrantedByUser() != null && !getGrantedByUser().equals(other.getGrantedByUser()) ) ||
			( getGrantedByUser() == null && other.getGrantedByUser() != null ) ) return false;

		return true;
	}
	
	/**
	 * Hash code is based on the path and name of
	 * the collection.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += getInstitutionalItemVersion() == null? 0 : getInstitutionalItemVersion().hashCode();
		value += getLicenseVersion() == null? 0 : getLicenseVersion().hashCode();
		value += getGrantedByUser() == null? 0 : getGrantedByUser().hashCode();
		return value;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[ id = ");
		sb.append(id);
		sb.append(" institutional item version = ");
		sb.append(institutionalItemVersion);
		sb.append(" license version = ");
		sb.append(licenseVersion);
		sb.append( " granted by = ");
		sb.append(grantedByUser);
		sb.append( " date granted = ");
		sb.append( dateGranted );
		sb.append("]");
		return sb.toString();
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Timestamp getDateGranted() {
		return dateGranted;
	}

	void setDateGranted(Timestamp dateGranted) {
		this.dateGranted = dateGranted;
	}

	public IrUser getGrantedByUser() {
		return grantedByUser;
	}

	void setGrantedByUser(IrUser grantedByUser) {
		this.grantedByUser = grantedByUser;
	}
	

}
