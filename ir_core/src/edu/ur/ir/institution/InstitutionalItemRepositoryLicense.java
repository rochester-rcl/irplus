package edu.ur.ir.institution;

import edu.ur.ir.repository.LicenseVersion;
import edu.ur.persistent.BasePersistent;

/**
 * License attached to an institutional item.  The license is
 * always dated the same date as submitted date - the user
 * who submitted the publication(item) is the one who accepted the license.
 * This information can be found in the generic item and institutional item version.
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
    InstitutionalItemRepositoryLicense(InstitutionalItemVersion institutionalItemVersion, LicenseVersion licenseVersion)
    {
    	setInstitutionalItemVersion(institutionalItemVersion);
    	setLicenseVersion(licenseVersion);
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
	

}
