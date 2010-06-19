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


import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileDatabase;
import edu.ur.file.db.FileInfo;
import edu.ur.file.db.FolderInfo;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.item.GenericItem;
import edu.ur.ir.user.IrUser;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * Class that manages repositories in memory.  Once the class is
 * destroyed so is all repository information.  This class is for testing
 * ONLY.  NOT all methods have been implemented.
 * 
 * @author Nathan Sarr
 *
 */
public class InMemoryRepositoryService implements RepositoryService{

	
	/** eclipse generated id */
	private static final long serialVersionUID = 7283528544966743132L;

	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(InMemoryRepositoryService.class);
	
	/** Set of repositories in the system */
	private Set<Repository> repositories = new HashSet<Repository>();
	
	/**
	 * Get the set of repositories
	 * 
	 * @return
	 */
	Set<Repository> getRepositories() {
		return repositories;
	}

	/**
	 * Get the set of repositories for ur published.
	 * 
	 * @param repositories
	 */
	void setRepositories(Set<Repository> repositories) {
		this.repositories = repositories;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.RepositoryService#createRepository(java.lang.String, edu.ur.file.db.FileDatabase)
	 */
	public Repository createRepository(String repositoryName, FileDatabase fileDatabase) {
		log.debug("create repository called");
		    
		Repository r = new Repository();
		r.setName(repositoryName);
		r.setFileDatabase(fileDatabase);
		    
		repositories.add(r);
		
		return r;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.RepositoryService#deleteRepository(java.lang.String)
	 */
	public boolean deleteRepository(String name) {
		boolean removed = false;
		for(Repository r: repositories)
		{
			if(r.getName().equals(name))
			{
				removed = repositories.remove(r);
			}
		}
		return removed;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.RepositoryService#getRepository(java.lang.String)
	 */
	public Repository getRepository(String name) {
		for(Repository r: repositories)
		{
			if(r.getName().equals(name))
			{
				return r;
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.repository.RepositoryService#getRepository(java.lang.Long)
	 */
	public Repository getRepository(Long id, boolean lock) {
		for(Repository r: repositories)
		{
			if(r.getId().equals(id))
			{
				return r;
			}
		}
		return null;
	}

	public void addNewFileToVersionedFile(Repository repository,
			VersionedFile versionedFile, File f, String originalFileName,
			IrUser versionCreator) {
		// TODO Auto-generated method stub
		
	}

	public void addNewFileToVersionedFile(Repository repository,
			VersionedFile versionedFile, File f, String originalFileName,
			String description, IrUser versionCreator) {
		// TODO Auto-generated method stub
		
	}

	public IrFile addRepositoryPicture(Repository repository, File f,
			String name, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	public void addTransformedFile(Repository repository, IrFile irFile,
			File f, String transformedFileName,
			String transformedFileExtension,
			TransformedFileType transformedFileType) {
		// TODO Auto-generated method stub
		
	}

	public boolean canLockVersionedFile(VersionedFile versionedFile, IrUser user) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canUnlockFile(VersionedFile versionedFile, IrUser user) {
		// TODO Auto-generated method stub
		return false;
	}

	public FileInfo createFileInfo(Repository repository, File f,
			String fileName, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo createFileInfo(Repository repository, File f,
			String fileName, String originalFileName, String description) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo createFileInfo(Repository repository, File f,
			String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo createFileInfo(Repository repository, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public FolderInfo createFolderInfo(Repository repository, String folderName) {
		// TODO Auto-generated method stub
		return null;
	}

	public IrFile createIrFile(Repository repository, File f, String fileName,
			String originalFileName, String description)
			throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public IrFile createIrFile(Repository repository, File f, String fileName,
			String description) throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile createVersionedFile(IrUser user,
			Repository repository, File f, String fileName, String description)
			throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile createVersionedFile(IrUser user,
			Repository repository, File f, String fileName, String description,
			String originalFileName) throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile createVersionedFile(IrUser user,
			Repository repository, String fileName, String description)
			throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile createVersionedFile(IrUser user,
			Repository repository, String fileName, String description,
			String originalFileName) throws IllegalFileSystemNameException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteFileInfo(FileInfo fileInfo) {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteFolderInfo(FolderInfo folderInfo) {
		// TODO Auto-generated method stub
		
	}

	public boolean deleteIrFile(IrFile irFile) {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteRepository(Repository repository) {
		// TODO Auto-generated method stub
		
	}

	public boolean deleteRepositoryPicture(Repository repository, IrFile irFile) {
		// TODO Auto-generated method stub
		return false;
	}

	public void deleteVersionedFile(VersionedFile versionedFile) {
		// TODO Auto-generated method stub
		
	}

	public Long getFileSystemSizeForUser(IrUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileVersion getFileVersion(Long id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	public FolderInfo getFolderInfo(String name, Long fileDatabaseId) {
		// TODO Auto-generated method stub
		return null;
	}

	public IrFile getIrFile(Long id, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileInfo getTransformByIrFileSystemCode(Long irFileId,
			String systemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public TransformedFileType getTransformedFileTypeBySystemCode(
			String systemCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile getVersionedFile(Long versionedFileId, boolean lock) {
		// TODO Auto-generated method stub
		return null;
	}

	public VersionedFile getVersionedFileByIrFile(IrFile irFile) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VersionedFile> getVersionedFilesForItem(GenericItem item) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean lockVersionedFile(VersionedFile versionedFile, IrUser user) {
		// TODO Auto-generated method stub
		return false;
	}

	public void saveRepository(Repository repository) {
		// TODO Auto-generated method stub
		
	}

	public boolean unlockVersionedFile(VersionedFile versionedFile, IrUser user) {
		// TODO Auto-generated method stub
		return false;
	}

	
	public List<LicenseVersion> getAvailableRepositoryLicenses(Long repositoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(IrFile irFile) {
		// TODO Auto-generated method stub
		
	}

	public boolean getExternalAuthenticationEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isExternalAuthenticationEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
