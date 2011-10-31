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

package edu.ur.ir.file;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;


import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.user.FileInviteInfo;
import edu.ur.ir.user.IrUser;
import edu.ur.persistent.BasePersistent;
import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Represents a file in the repository that can have 
 * multiple versions.  The current version is the version
 * with the largest version number.
 * 
 * @author Nathan Sarr
 *
 */
public class VersionedFile extends BasePersistent implements NameAware, DescriptionAware  {
	
	/**  Eclipse generated id. */
	private static final long serialVersionUID = -6516732383193851069L;

	/**  Starting number for all file versions */
	public static final int INITIAL_FILE_VERSION = 0;
		
	/**  The current highest file version number */
	private int maxVersion = INITIAL_FILE_VERSION;
	
	/** Name for the object - the name is very important and should generally not change
	 *  over the life of the object  - however there is the ability to rename - this should
	 *  remain independent of the file name */
	protected String name;
	
	/** Generic description - this is a performance enhancement and should always be a copy
	 * of the current ir file description */
	protected String description;
	
	/**  The set of versions for this Versioned Ir File */
	private Set<FileVersion> versions = new HashSet<FileVersion>();
	
	/**  Current version of the file */
	private FileVersion currentVersion;
	
	/** extension for the current version. */
	private String extension;
	
	/**
	 * Owner of the versioned file.  This person owns the file and 
	 * all related versions.  
	 */
	private IrUser owner;
	
	/**  The user who has locked this versioned file  */
	private IrUser lockedBy;
	
	/** collaborators who are allowed to work on the file */
	private Set<FileCollaborator> collaborators = new HashSet<FileCollaborator>();

	/** Invitees who are not part of the system and yet to create an account */
	private Set<FileInviteInfo> invitees = new HashSet<FileInviteInfo>();
	
	/** the file size of the current version */
	private Long currentFileSizeBytes = 0L;
	
	/** total size used for all versions */
	private Long totalSizeForAllFilesBytes = 0L;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(VersionedFile.class);
	
	/* allows a user to view/read the file */
	public static String VIEW_PERMISSION = "VIEW";
	
	/* allows user to edit the file versions and delete */
	public static String EDIT_PERMISSION = "EDIT";
	
	/* allows user to edit the file versions and share with others */
	public static String SHARE_PERMISSION = "SHARE";
	
	/**
	 * Package protected versioned file constructor.
	 */
	VersionedFile(){}
	
	/**
	 * Create a new ir file with the initial version  
	 * 
	 * @param info
	 */
	public VersionedFile(IrUser owner, FileInfo info, String name) throws IllegalFileSystemNameException
	{
		setOwner(owner);
		setName(name);
		addNewVersion(info, owner);
		currentFileSizeBytes = info.getSize();
		totalSizeForAllFilesBytes = info.getSize();
	}
	
	/**
	 * Add a new version of the file info.  This version becomes
	 * the new current version.
	 * 
	 * The FileInfo object display name cannot be NULL - otherwise an IllegalStateException is
	 * thrown.
	 * 
	 * @param fileInfo
	 */ 
	public FileVersion addNewVersion(FileInfo fileInfo, IrUser versionCreator) 
	{
		if (owner.equals(versionCreator) || isUserACollaborator(versionCreator)) {
			maxVersion = maxVersion + 1;
			IrFile irFile = null;
			try {
				if( fileInfo.getDisplayName() == null )
				{
					throw new IllegalStateException("The info display name cannot be null " + fileInfo);
				}
				irFile = new IrFile(fileInfo, fileInfo.getDisplayName());
			} catch(IllegalFileSystemNameException e) {
				// This Exception will not happen here since the name is passed from VersionedFile which is 
				// already checked for illegal characters. So just catching and logging and not throwing.
				log.error("The IrFile name contains illegal special characters - " + e.getName());
			}
			irFile.setOwner(owner);
			FileVersion version = new FileVersion(irFile, this, maxVersion, versionCreator);
			versions.add(version);
			extension = fileInfo.getExtension();
			currentVersion = version;
			currentFileSizeBytes = fileInfo.getSize();
			totalSizeForAllFilesBytes += fileInfo.getSize();
			setDescription(irFile.getDescription());
			return version;
		} else {
			throw new IllegalStateException("The user must be Owner or collaborator to add a new version. User :" + versionCreator.toString());
		} 
	}
	
	/**
	 * Get the current version.
	 * 
	 * @return the current file version.
	 */
	public FileVersion getCurrentVersion()
	{
		return currentVersion;
	}

	/**
	 * Changes the current ir file version.  if the version exists otherwise,
	 * the current version is not changed.  This ALWAYS creates a new version.
	 * 
	 * This does not change the status of published vs unpublished.
	 * 
	 * @param irFile
	 */
	public boolean changeCurrentIrVersion(int myVersion, IrUser versionCreator) {
		
		//the max version is always the current version
		if( myVersion == maxVersion ) return true;
		
		for(FileVersion version: versions)
		{
			if( version.getVersionNumber() == myVersion)
			{
				addNewVersion(version.getIrFile().getFileInfo(), versionCreator);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The versions of this ir file.  This returns an unmodifiable set.
	 * 
	 * @return the versions
	 */
	public Set<FileVersion> getVersions() {
		return Collections.unmodifiableSet(versions);
	}

	/**
	 * Set the versions for this ir file.
	 * 
	 * @param versions
	 */
	void setVersions(Set<FileVersion> versions) {
		this.versions = versions;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += name == null ? 0 : name.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[VersionedFile id = ");
		sb.append(getId());
		sb.append( " largestVersion = ");
		sb.append(getLargestVersion());
		sb.append( " name = ");
		sb.append(name);
		sb.append( " description = ");
		sb.append(description);
		sb.append("]");
		
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof VersionedFile)) return false;

		final VersionedFile other = (VersionedFile) o;

		if( ( getNameWithExtension() != null && !getNameWithExtension().equals(other.getNameWithExtension()) ) ||
			( getNameWithExtension() == null && other.getNameWithExtension() != null ) ) return false;
		
		if( ( id != null && !id.equals(other.getId()) ) ||
			( id == null && other.getId() != null ) ) return false;
		
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
	 * Owner of the versioned file.
	 * 
	 * @return the user who owns the file
	 */
	public IrUser getOwner() {
		return owner;
	}

	/**
	 * Owner of the versioned file.
	 * 
	 * @param owner
	 */
	public void setOwner(IrUser owner) {
		this.owner = owner;
	}
	
	/**
	 * Change the owner for this versioned item 
	 * 
	 * @param newOwner
	 */
	public void changeOwner(IrUser newOwner)
	{
		addCollaborator(owner);
		setOwner(newOwner);
		removeCollaborator(getCollaborator(newOwner));
	}
	
	/**
	 * Get the Version in the versions if it exists
	 * 
	 * @param version
	 * @return The IrFile or null if it does not exist
	 */
	public FileVersion getVersion(int version)
	{
		FileVersion fileVersion  = null;
		for( FileVersion v : versions)
		{
			if( v.getVersionNumber() == version)
			{
				fileVersion = v;
			}
		}
		
		return fileVersion;
	}
	
	/**
	 * Lock this set of files for a given user.  The 
	 * file cannot be locked for a lock to be obtained.  
	 * This does not check who locked the file.  It is 
	 * up to the client to make sure that those who lock 
	 * this file are allowed to lock it.
	 * 
	 * @param user - user who locked this file
	 * @return - true if the file is locked
	 */
	public boolean lock(IrUser user)
	{
		if(!this.isLocked())
		{
		    lockedBy = user;
		    return true;
		}
		return false;
	}
	
	/**
	 * Unlock the versioned file.  There is no check
	 * it is up to the client to determine if the file 
	 * can be unlocked.
	 */
	public void unLock()
	{
		lockedBy = null;
	}

	/**
	 * Determine if this versioned file is locked.
	 * 
	 * @return true if the versioned file is locked
	 */
	public boolean isLocked() {
		return lockedBy != null;
	}
	
	/**
	 * Return true if the versioned file is locked.
	 * 
	 * @return
	 */
	public boolean getLocked()
	{
		return isLocked();
	}

	/**
	 * Return the user who has locked the versioned file
	 * or null if no one has locked the file.
	 * 
	 * @return user who has locked the versioned file
	 */
	public IrUser getLockedBy() {
		return lockedBy;
	}

	/**
	 * Get the current max version of the file. 
	 * This represents the newest version of the file.
	 * 
	 * @return
	 */
	public int getMaxVersion() {
		return maxVersion;
	}

	/**
	 * The max version of the file
	 * 
	 * @param maxVersion
	 */
	void setMaxVersion(int maxVersion) {
		this.maxVersion = maxVersion;
	}

	/**
	 * Add collaborator who is invited to work on the versioned file.
	 * 
	 * @param collaborator User who is invited to collaborate
	 */
	public FileCollaborator addCollaborator(IrUser collaborator) {
		FileCollaborator fileCollaborator = new FileCollaborator(collaborator, this); 
		collaborators.add(fileCollaborator);
		return fileCollaborator;
		
	}

	/**
	 * Add invitee who is invited to work on the versioned file but doesnot 
	 * exist in the system.
	 * 
	 * @param inviteInfo invite information
	 */
	public void addInvitee(FileInviteInfo inviteInfo) {
		
		invitees.add(inviteInfo);
	}

	/**
	 * Returns the list of invitee emails
	 * 
	 * @return Returns the list of invitee emails
	 */
	public List<String> getInviteeEmails() {
		List<String> inviteeEmails = new ArrayList<String>();
		
		for (FileInviteInfo inviteInfo:invitees) {
			inviteeEmails.add(inviteInfo.getInviteToken().getEmail());
		}
		
		return inviteeEmails;
	}
	
	/**
	 * Remove collaborator from the collaborator list.
	 * 
	 * @param fileCollaborator collaborator that has to be removed
	 * @return true if removed else return false
	 */
	public boolean removeCollaborator(FileCollaborator fileCollaborator) {
		// if the collaborator being removed owns the file
		// then remove the lock
		if( lockedBy != null && lockedBy.equals(fileCollaborator.getCollaborator()))
		{
			lockedBy = null;
		}
		return collaborators.remove(fileCollaborator);
	}

	/**
	 * Remove invitee from the invitees list.
	 * 
	 * @param inviteInfo invitee that has to be removed
	 * @return true if removed else return false
	 */
	public boolean removeInvitee(FileInviteInfo inviteInfo) {
		return invitees.remove(inviteInfo);
	}

	/**
	 * Set of collaborators invited to help work on the versioned file.
	 * 
	 * @return unmodifiable set of people allowed to work on the versioned file.
	 */
	public Set<FileCollaborator> getCollaborators() {
		return Collections.unmodifiableSet(collaborators);
	}

	/**
	 * Get the collaborator for specified  user
	 * 
	 * @return file collaborator for given user
	 */
	public FileCollaborator getCollaborator(IrUser user) {
		
		for (FileCollaborator c:collaborators){
			if (c.getCollaborator().equals(user)) {
				return c;
			}
		}
		return null;
		
	}

	/**
	 * Get the collaborator for specified  id
	 * 
	 * @return file collaborator for given id
	 */
	public FileCollaborator getCollaboratorById(Long collaboratorId) {
		
		for (FileCollaborator c:collaborators){
			if (c.getId().equals(collaboratorId)) {
				return c;
			}
		}
		return null;
		
	}
	
	/**
	 * Set of collaborators who are invited to work on the versioned file.
	 * 
	 * @param collaborators
	 */
	void setCollaborators(Set<FileCollaborator> collaborators) {
		this.collaborators = collaborators;
	}
	
	/**
	 * Returns true if the file is shared with other users
     *
     * @return true if file is shared
	 */
	public boolean isShared() {
		return collaborators.size() > 0;
	}
	
	
	/**
	 * Determine if the user is a collaborator on the files.
	 * 
	 * @param user - user to check 
	 * @return true if the user is a collaborator
	 */
	public boolean isUserACollaborator(IrUser user) {
		for (FileCollaborator c:collaborators){
			if (c.getCollaborator().equals(user)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Set the current file version.
	 * 
	 * @param currentVersion - current version
	 */
	void setCurrentVersion(FileVersion currentVersion) {
		this.currentVersion = currentVersion;
	}

	/**
	 * Get the file extension.
	 * 
	 * @return
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Set the extension.
	 * 
	 * @param extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	/**
	 * Returns the name with the extension.  If there
	 * is no extension only the name is returned.
	 * 
	 * @return
	 */
	public String getNameWithExtension()
	{
		String nameWithExtension = name;
		if( extension != null &&
				!extension.trim().equals(""))
		{
			nameWithExtension = name + "." + extension; 
		}
		
		return nameWithExtension;
	}

	/**
	 * Get invite information that is pending to be shared with the user 
	 *
	 * @return Set of invite information
	 */
	public Set<FileInviteInfo> getInvitees() {
		return invitees;
	}

	/**
	 * Set invite information that is pending to be shared with the user 
	 *
	 * @param Set of invite information
	 */
	public void setInvitees(Set<FileInviteInfo> invitees) {
		this.invitees = invitees;
	}

	/**
	 * Return the size of the current version file
	 * 
	 * @return current version size
	 */
	public Long getCurrentFileSizeBytes() {
		return currentFileSizeBytes;
	}

	/**
	 * Set the current file size in bytes.
	 * 
	 * @param currentFileSizeBytes
	 */
	void setCurrentFileSizeBytes(Long currentFileSizeBytes) {
		this.currentFileSizeBytes = currentFileSizeBytes;
	}

	/**
	 * Get the total size for all files in bytes.
	 * 
	 * @return total size for all versions of the file
	 */
	public Long getTotalSizeForAllFilesBytes() {
		return totalSizeForAllFilesBytes;
	}

	/**
	 * Set the total size for all files in bytes.
	 * 
	 * @param totalSizeForAllFilesBytes
	 */
	void setTotalSizeForAllFilesBytes(Long totalSizeForAllFilesBytes) {
		this.totalSizeForAllFilesBytes = totalSizeForAllFilesBytes;
	}

	/* (non-Javadoc)
	 * @see edu.ur.common.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		currentVersion.getIrFile().getFileInfo().setDescription(description);
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see edu.ur.common.NameAware#getName()
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set the name - package protected.
	 * 
	 * @param name
	 */
	void setName(String name) throws IllegalFileSystemNameException 
	{
		if( name == null )
		{
			throw new IllegalStateException("versioned file name cannot be null");
		}
		
		List<Character> illegalCharacters = IllegalFileSystemNameException.nameHasIllegalCharacerter(name);
		if( illegalCharacters.size() > 0 )
		{
			throw new IllegalFileSystemNameException(illegalCharacters, name);
		}

		this.name = name;
	}
	
	/**
	 * Rename the file - assumes that the last "." with following characters are the extension.  Will
	 * update the name of the versioned file and the current name of the underlying current version
	 * name and extension.
	 * 
	 * @param nameWithExtension - full name with extension.
	 * @throws IllegalFileSystemNameException 
	 */
	public void reName(String nameWithExtension) throws IllegalFileSystemNameException
	{
		FileVersion currentVersion = getCurrentVersion();
		FileInfo file = currentVersion.getIrFile().getFileInfo();
		String baseName = FilenameUtils.getBaseName(nameWithExtension);
		setName(baseName);
		file.setDisplayName(baseName);
		
		String extension = FilenameUtils.getExtension(nameWithExtension);
		if( extension.equals(""))
		{
			extension = null;
		}
		
		setExtension(extension);
		file.setExtension(extension);
	}
}
