package edu.ur.ir.repository;

import java.util.List;

import edu.ur.ir.user.IrUser;

/**
 * Interface for dealing with licenses for a repository
 * 
 * @author Nathan Sarr
 *
 */
public interface LicenseService {
	
	/**
	 * Create a versioned license.
	 * 
	 * @param creator - user creating the license
	 * @param licenseText - text to be in the license.
	 * @param name - name of the license
	 * 
	 * @return created versioned license.
	 */
	public VersionedLicense createLicense(IrUser creator, String licenseText, String name);
	
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

}
