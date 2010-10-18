package edu.ur.ir.repository;

import java.io.Serializable;
import java.util.List;

import edu.ur.ir.user.IrUser;

/**
 * Interface for dealing with licenses for a repository
 * 
 * @author Nathan Sarr
 *
 */
public interface LicenseService  extends Serializable{
	
	/**
	 * Create a versioned license.
	 * 
	 * @param creator - user creating the license
	 * @param licenseText - text to be in the license.
	 * @param name - name of the license
	 * 
	 * @return created versioned license.
	 */
	public VersionedLicense createLicense(IrUser creator, String licenseText, String name, String description);
	
	/**
	 * Save the specifed versioned license.
	 * 
	 * @param versionedLicense
	 */
	public void save(VersionedLicense versionedLicense);
	
	/**
	 * Delete the specified versioned license.
	 * 
	 * @param versionedLicense
	 */
	public void delete(VersionedLicense versionedLicense);
	
	/**
	 * Get the versioned license.
	 * 
	 * @param id - id of the versioned license.
	 * 
	 * @param lock - lock 
	 * @return the versioned license found or null if versioned license is not found.
	 */
	public VersionedLicense get(Long id, boolean lock);
	
	/**
	 * Get all versioned licenses in the system.
	 * 
	 * @return the set of versioned licenses
	 */
	public List<VersionedLicense> getAll();
	
	/**
	 * Get all license versions 
	 * 
	 * @return the list of all license versions across all versiond licenses
	 */
	public List<LicenseVersion> getAllLicenseVersions();
	
	/**
	 * Get the specified license version.
	 * 
	 * @param id - id of the license version
	 * @param lock - true to upgrade the lock mode
	 * 
	 * @return - the found license version
	 */
	public LicenseVersion getLicenseVersion(Long id, boolean lock);

}
