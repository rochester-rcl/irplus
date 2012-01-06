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


package edu.ur.tag.repository;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.item.ItemFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.security.IrAcl;
import edu.ur.ir.security.SecurityService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;

/**
 * Utility functions.
 * 
 * @author Nathan Sarr
 *
 */
public class FileWebUtilFunctions {
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -8130846946682991084L;
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(FileWebUtilFunctions.class);
	
	/** Security service  */
	private static SecurityService mySecurityService = null;
	
	/** service to deal with repository information */
	private static RepositoryService repositoryService = null;
	
	/**
	 * Security service for dealing with secure actions.
	 * 
	 * @param securityService
	 */
	public void setSecurityService(SecurityService securityService) {
		mySecurityService = securityService;
	}

	/**
	 * This checks to make sure the specified user is the owner of the 
	 * versioned file.
     * 
	 * @param user - user who we want to check is the owner
	 * @param versionedFile - versioned file 
	 * 
	 * @return true if the user is the owner of the versioned file
	 */
	public static boolean isOwner(IrUser user, VersionedFile versionedFile)
	{
		if( versionedFile == null || versionedFile.getOwner() == null)
		{
			return false;
		}
		
		return versionedFile.getOwner().equals(user);
	}
	
	/**
	 * Determine if this person is the one who has locked the file
	 * 
	 * @param user - user to check and see if the user is the one who locked
	 * the file.
	 * 
	 * @param versionedFile - the versioned file to look at. 
	 * 
	 * @return true if the file is locked.
	 */
	public static boolean isLocker(IrUser user, VersionedFile versionedFile)
	{
		if( versionedFile == null || !versionedFile.isLocked() || 
				versionedFile.getLockedBy() == null )
		{
			return false;
		}
		
		return versionedFile.getLockedBy().equals(user);
	}

	/**
	 * Determine if the contents can be moved into the specified location.  This is true 
	 * if the destination is not equal to the current destination.  A collection cannot be 
	 * moved into itself;
	 * 
	 * @param objectsToMove - set of information to be moved
	 * @param destination - destination  to move to
	 * @return true if the set of information can be moved into the specified location
	 */
	public static boolean canMoveToFolder(Collection<FileSystem> objectsToMove, FileSystem destination)
	{
		for(FileSystem fileSystemObject : objectsToMove)
		{
			// same type and id means they are equal
		    if( fileSystemObject.getFileSystemType().equals(destination.getFileSystemType()) &&
		    	fileSystemObject.getId().equals(destination.getId()) )
			{
		        return false;
			}
		}
		return true;
	}
	

	/**
	 * Determine if the destination  is one of the file that has to be moved
	 * 
	 * @param objectsToMove - set of information to be moved
	 * @param object - current object 
	 * @return true if the destination file is one of the files to be moved
	 */
	public static boolean isFileToBeMoved(Collection<FileSystem> objectsToMove, FileSystem object)
	{
		for(FileSystem fileSystemObject : objectsToMove)
		{
		    if( fileSystemObject.getFileSystemType().equals(object.getFileSystemType()) && 
			fileSystemObject.getId().equals(object.getId())	)
			{
				    	return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Security service for dealing with if a user can lock a file.
	 * 
	 * @param user
	 * @param versionedFile
	 * @return
	 */
	public static boolean canLockFile(IrUser user, VersionedFile versionedFile)
	{
		boolean canLockFile = false;
		
		 if( versionedFile == null || versionedFile.isLocked()  
		    			|| versionedFile.getOwner() == null )
		{
		    canLockFile = false;
		}
		else if ( versionedFile.getOwner() != null &&  versionedFile.getOwner().equals(user) )
		{
		    canLockFile = true;
		}
		else 
		{
	         IrAcl acl = mySecurityService.getAcl(versionedFile, user);
			 if (acl != null) {
			    if (acl.isGranted(VersionedFile.EDIT_PERMISSION, user, false)) {
			    	canLockFile = true;
				}
			}
		}
		 
		 return canLockFile;
	}
	
	/**
	 * Security service for dealing with if a user can share a file.
	 * 
	 * @param user
	 * @param versionedFile
	 * @return
	 */
	public static boolean canShareFile(IrUser user, VersionedFile versionedFile)
	{
		boolean canShareFile = false;
		if ( versionedFile.getOwner() != null &&  versionedFile.getOwner().equals(user) )
		{
		    canShareFile = true;
		}
		else 
		{
			log.debug("checking has share permission");
	        
	         IrAcl acl = mySecurityService.getAcl(versionedFile, user);
			 if (acl != null) {
			    if (acl.isGranted(VersionedFile.SHARE_PERMISSION, user, false)) {
			    	canShareFile = true;
				}
			}
		}
		 
		 return canShareFile;
	}
	
	/**
	 * Security service for dealing with if a user can edit a file.
	 * 
	 * @param user
	 * @param versionedFile
	 * @return
	 */
	public static boolean canEditFile(IrUser user, VersionedFile versionedFile)
	{
		boolean canEdit = false;
		if (versionedFile.getOwner() != null && 
				versionedFile.getOwner().equals(user)) {
			canEdit = true;
			log.debug("is owner");
		} 
		else if ( versionedFile.getLockedBy() != null &&
				versionedFile.getLockedBy().equals(user))
		{
			log.debug( "locker of file");
		    canEdit = true;	
		}
		else 
		{
	         IrAcl acl = mySecurityService.getAcl(versionedFile, user);
			 if (acl != null) {
			    if (acl.isGranted(VersionedFile.EDIT_PERMISSION, user, false)) {
			    	canEdit = true;
				}
			}
		}
		 
		 return canEdit;
	}
	
	/**
	 * Security service for dealing with if a user can break the lock on a file.
	 * 
	 * @param user
	 * @param versionedFile
	 * @return
	 */
	public static boolean canBreakLock(IrUser user, VersionedFile versionedFile)
	{
		boolean canBreakLock = false;
		if (versionedFile == null || !versionedFile.isLocked() ) {
			canBreakLock = false;
		} 
		else if (versionedFile.getOwner() != null && 
				versionedFile.getOwner().equals(user)) {
			canBreakLock = true;
		} 
		else  
		{
	         IrAcl acl = mySecurityService.getAcl(versionedFile, user);
			 if (acl != null) {
			    if (acl.isGranted(VersionedFile.MANAGE_PERMISSION, user, false)) {
			    	canBreakLock = true;
				}
			}
		}
		 
		 return canBreakLock;
	}
	
	/**
	 * Return true if the file has the transformed file for the
	 * specified system code.
	 * 
	 * @param irFile - ir file to check
	 * @param systemCode - system code to look for
	 * @return - true if the file has the system code
	 */
	public static boolean hasTransformedFile(IrFile irFile, String systemCode)
	{
		boolean hasTransformedFile = false;
		if( irFile != null && systemCode != null)
		{
			if(irFile.getTransformedFileBySystemCode(systemCode) != null)
			{
				hasTransformedFile = true;
			}
		}
		
		return hasTransformedFile;
	}
	
	/**
	 * Determine if the file exists in the item
	 * @param personalFile - personal file to check for
	 * @param item - item to check
	 * 
	 * @return true if the personal file exists in the item
	 */
	public static boolean fileExistsInItem(PersonalFile personalFile, GenericItem item )
	{
		boolean fileExist = false;
		
		Set<FileVersion> versions = personalFile.getVersionedFile().getVersions();
		
		for(ItemFile itemFile:item.getItemFiles()) {
			
			for (FileVersion fileVersion:versions) {
				if (fileVersion.getIrFile().equals(itemFile.getIrFile())) {
					return true;
				}
			}
		}
		
		return fileExist;
	}
	
	/**
	 * Determine if the user can read the file.
	 * 
	 * @param user - user to check
	 * @param versionedFile - versioned file to use
	 * 
	 * @return true if teh user can read the file otherwise false
	 */
	public static boolean canReadFile(IrUser user, VersionedFile versionedFile)
	{
		boolean canRead = false;
	    IrAcl acl = mySecurityService.getAcl(versionedFile, user);
		if (acl != null) {
		    if (acl.isGranted(VersionedFile.VIEW_PERMISSION, user, false)) {
			    canRead = true;
			}
		}
		return canRead;
	}

	/**
	 * Checks whether the IrFile has thumbnail
	 * 
	 * @param irFile file to check for thumbnail
	 * @return
	 */
	public static boolean hasThumbnail(IrFile irFile) {
		if( irFile == null )
		{
			return false;
		}
		return repositoryService.getTransformByIrFileSystemCode(irFile.getId(), TransformedFileType.PRIMARY_THUMBNAIL) != null ? true: false;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		FileWebUtilFunctions.repositoryService = repositoryService;
	}

	

}
