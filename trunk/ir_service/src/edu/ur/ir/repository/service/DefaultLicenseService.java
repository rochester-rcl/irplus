package edu.ur.ir.repository.service;

import java.util.List;

import edu.ur.ir.repository.LicenseService;
import edu.ur.ir.repository.VersionedLicense;
import edu.ur.ir.repository.VersionedLicenseDAO;
import edu.ur.ir.user.IrUser;

/**
 * Default service for dealing with repository licenses.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultLicenseService implements LicenseService{
	
	/**Class for versioned license persistence */
	private VersionedLicenseDAO versionedLicenseDAO;

	
	/**
	 * Create a versioned license.
	 * 
	 * @see edu.ur.ir.repository.LicenseService#createLicense(edu.ur.ir.user.IrUser, java.lang.String, java.lang.String)
	 */
	public VersionedLicense createLicense(IrUser creator, String licenseText,
			String name) {
		
		VersionedLicense versionedLicense = new VersionedLicense(creator, licenseText, name);
		versionedLicenseDAO.makePersistent(versionedLicense);
		return versionedLicense;
	}

	
	/**
	 * Delete the versioned license.
	 * @see edu.ur.ir.repository.LicenseService#delete(edu.ur.ir.repository.VersionedLicense)
	 */
	public void delete(VersionedLicense versionedLicense) {
		versionedLicenseDAO.makeTransient(versionedLicense);
	}

	
	/**
	 * Get the versioned license by id.
	 * @see edu.ur.ir.repository.LicenseService#get(java.lang.Long, boolean)
	 */
	public VersionedLicense get(Long id, boolean lock) {
		return versionedLicenseDAO.getById(id, lock);
	}

	
	/**
	 * Get all versioned licenses.
	 * @see edu.ur.ir.repository.LicenseService#getAll()
	 */
	@SuppressWarnings("unchecked")
	public List<VersionedLicense> getAll() {
		return versionedLicenseDAO.getAll();
	}

	
	/**
	 * Save the versioned license to persistent storage.
	 * @see edu.ur.ir.repository.LicenseService#save(edu.ur.ir.repository.VersionedLicense)
	 */
	public void save(VersionedLicense versionedLicense) {
		versionedLicenseDAO.makePersistent(versionedLicense);
	}


	public VersionedLicenseDAO getVersionedLicenseDAO() {
		return versionedLicenseDAO;
	}


	public void setVersionedLicenseDAO(VersionedLicenseDAO versionedLicenseDAO) {
		this.versionedLicenseDAO = versionedLicenseDAO;
	}

}
