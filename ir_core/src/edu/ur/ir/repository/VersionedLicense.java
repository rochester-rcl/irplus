package edu.ur.ir.repository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.ur.ir.user.IrUser;
import edu.ur.persistent.CommonPersistent;

/**
 * Represents a license that has more than one version
 * 
 * @author Nathan Sarr
 * 
 */
public class VersionedLicense extends CommonPersistent {

	/** Eclipse generated id */
	private static final long serialVersionUID = -2006630716900759279L;

	/** Starting number for all file versions */
	public static final int INITIAL_LICENSE_VERSION = 1;

	/** The current higest file version number */
	private int maxVersion = INITIAL_LICENSE_VERSION;

	/** The set of versions for this license */
	private Set<LicenseVersion> versions = new HashSet<LicenseVersion>();

	/** The user who has locked this license */
	private IrUser lockedBy;

	/** Current version for the set of versions */
	private LicenseVersion currentVersion;

	/** Package protected versioned item constructor. */
	VersionedLicense() {}

	/**
	 * Create a new versioned license with the initial version
	 * 
	 * @param irFile
	 */
	public VersionedLicense(IrUser creator, String licenseText, String name) {
		setMaxVersion(INITIAL_LICENSE_VERSION);
		License license = new License(name, licenseText, creator);
		LicenseVersion version = new LicenseVersion(license, this, maxVersion);
		setName(name);
		currentVersion = version;
		versions.add(version);
	}

	/**
	 * Add a new version of the license. This version becomes the new current
	 * version.
	 * 
	 * @param fileInfo
	 */
	public LicenseVersion addNewVersion(String licenseText, IrUser creator) {
		setMaxVersion(maxVersion + 1);
		License license = new License(name, licenseText, creator);
		license.setCreator(creator);
		LicenseVersion version = new LicenseVersion(license, this, maxVersion);
		versions.add(version);
		currentVersion = version;
		return version;
	}

	/**
	 * Returns the current license version.
	 * 
	 * @return the version with the highest version number.
	 */
	public LicenseVersion getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * Changes the current license version if the version exists otherwise, the
	 * current version is not changed. This ALWAYS creates a new version in the
	 * series of versions.
	 * 
	 * @param int version
	 * 
	 * @return the new version or null if the new version is not found
	 */
	public LicenseVersion changeCurrentVersion(int myVersion) {

		// the max version is always the current version
		if (myVersion == maxVersion)
			return this.getCurrentVersion();

		for (LicenseVersion version : versions) {
			if (version.getVersionNumber() == myVersion) {
				maxVersion = maxVersion + 1;
				LicenseVersion newVersion = new LicenseVersion(version
						.getLicense(), this, maxVersion);
				this.versions.add(newVersion);
				currentVersion = newVersion;
				return newVersion;
			}
		}
		return null;
	}

	/**
	 * Get the license version with the given version number. If it doesn't
	 * exist, an exception is thrown.
	 * 
	 * @param versionNumber
	 * @return the license version if found otherwise null.
	 */
	public LicenseVersion getByVersionNumber(int versionNumber) {
		for (LicenseVersion iv : versions) {
			if (iv.getVersionNumber() == versionNumber) {
				return iv;
			}
		}

		return null;
	}

	/**
	 * The versions of this item.
	 * 
	 * @return the versions
	 */
	public Set<LicenseVersion> getItemVersions() {
		return Collections.unmodifiableSet(versions);
	}

	/**
	 * Set the versions for this ir file.
	 * 
	 * @param versions
	 */
	void setLicenseVersions(Set<LicenseVersion> versions) {
		this.versions = versions;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[VersionedItem id = ");
		sb.append(getId());
		sb.append(" largestVersion = ");
		sb.append(getLargestVersion());
		sb.append(" name = ");
		sb.append(name);
		sb.append("]");

		return sb.toString();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VersionedLicense))
			return false;

		final VersionedLicense other = (VersionedLicense) o;

		if ((name == null && other.getName() != null)
				|| (name != null && !name.equals(other.getName())))
			return false;

		// normally we do not want to use an id - however, it would have to be
		// this or
		// some other globally unique id.
		if ((id != null && !id.equals(other.getId()))
				|| (id == null && other.getId() != null))
			return false;

		return true;
	}

	/**
	 * Get the current largest ir version number.
	 * 
	 * @return
	 */
	public int getLargestVersion() {
		return maxVersion;
	}

	/**
	 * Set the largest ir version number.
	 * 
	 * @param largestIrVersion
	 */
	void setLargestVersion(int largestVersion) {
		this.maxVersion = largestVersion;
	}

	/**
	 * Lock this versioned file.
	 * 
	 * @param user
	 * @return
	 */
	public boolean lock(IrUser user) {
		if (lockedBy == null) {
			lockedBy = user;
			return true;
		} else if (lockedBy.equals(user)) {
			return true;
		}
		return false;
	}

	/**
	 * Unlock the versioned file
	 */
	public void unLock() {
		lockedBy = null;
	}

	/**
	 * Determine if this versioned file is locked.
	 * 
	 * @return true if the versioned file is locked
	 */
	public boolean isLocked() {
		return lockedBy == null;
	}

	/**
	 * Return true if the versioned file is locked.
	 * 
	 * @return
	 */
	public boolean getLocked() {
		return isLocked();
	}

	/**
	 * Return the user who has locked the versioned license or null if no one
	 * has locked the license.
	 * 
	 * @return user who has locked the versioned license
	 */
	public IrUser getLockedBy() {
		return lockedBy;
	}

	/**
	 * Set the max version.
	 * 
	 * @param maxVersion
	 */
	private void setMaxVersion(int maxVersion) {
		this.maxVersion = maxVersion;
	}

}
