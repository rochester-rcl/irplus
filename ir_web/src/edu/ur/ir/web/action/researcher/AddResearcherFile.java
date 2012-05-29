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


package edu.ur.ir.web.action.researcher;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemType;
import edu.ur.ir.file.FileVersion;
import edu.ur.ir.file.VersionedFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFile;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherInstitutionalItem;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherPublication;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add file to researcher page
 * 
 * @author Sharmila Ranganathan
 *
 */
public class AddResearcherFile extends ActionSupport implements UserIdAware{

	/**  Eclipse generated id */
	private static final long serialVersionUID = 3846183502445990945L;

	/**  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(AddResearcherFile.class);
	
	/** Service for item.  */
	private ResearcherService researcherService;
	
	/** Service for dealing with researcher file system */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** File system service for user */
	private UserFileSystemService userFileSystemService;
	
	/** Service for dealing with user information */
	private UserService userService;

	/** File system service for files */
	private RepositoryService repositoryService;
	
	/** File id to add / remove files action*/
	private Long versionedFileId;
	
	/** User logged in */
	private Researcher researcher;
		
	/**  Personal folder id */
	private Long parentFolderId;
	
	 /** A collection of folders and files for a user in a given location of
    their personal directory.*/
	private Collection<FileSystem> personalFileSystem;
	
	 /** A collection of folders and files for a user in a given location of
    their personal directory.*/
	List<ResearcherFileSystemVersion> researcherFileSystemVersions = new LinkedList<ResearcherFileSystemVersion>();
	
	/** set of folders that are the path for the current folder */
	private Collection <ResearcherFolder> researcherFolderPath;

	/** set of folders that are the path for the current folder */
	private Collection <PersonalFolder> personalFolderPath;

	/** Id of the parent collection */
	private Long parentPersonalFolderId;	
	
	/** description for file */
	private String description;

	/** Id of the researcher file */
	private Long researcherFileId;
	
	/** Id of the file version */
	private Long fileVersionId;
	
	/** id of the user accessing the information */
	private Long userId;
	
	
	/**
	 * Prepare for action
	 */
	public void loadResearcher() {
		log.debug("load researcher user Id:"+ userId);
		IrUser user = userService.getUser(userId, false);
		if(user != null)
		{
			researcher = user.getResearcher();
		}
	}

	/**
	 * Create the file system to view personal folders and files.
	 */
	public String getPersonalFolders()
	{

		if(parentPersonalFolderId != null && parentPersonalFolderId > 0)
		{
			PersonalFolder personalFolder = userFileSystemService.getPersonalFolder(parentPersonalFolderId, false);
			if(personalFolder == null || !personalFolder.getOwner().getId().equals(userId))
			{
				return "accessDenied";
			}
		    personalFolderPath = userFileSystemService.getPersonalFolderPath(parentPersonalFolderId);
		}
		
		Collection<PersonalFolder> myPersonalFolders = userFileSystemService.getPersonalFoldersForUser(userId, parentPersonalFolderId);
		Collection<PersonalFile> myPersonalFiles = userFileSystemService.getPersonalFilesInFolder(userId, parentPersonalFolderId);
		
	    personalFileSystem = new LinkedList<FileSystem>();
	    
	    for(PersonalFolder o : myPersonalFolders)
	    {
	    	personalFileSystem.add(o);
	    }
	    
	    for(PersonalFile o: myPersonalFiles)
	    {
	    	personalFileSystem.add(o);
	    }
	    
	    return SUCCESS;
	}
	
	/**
	 * Add file to researcher
	 * 
	 */
	public String addResearcherFile() {
		
		log.debug("Add file versionedFileId = " + versionedFileId);
		loadResearcher();	
		
		if( researcher == null )
		{
			return "accessDenied";
		}
		
		VersionedFile vf = repositoryService.getVersionedFile(versionedFileId, false);
		
		// user must be the owner or collaborator of the versioned file
		if( !vf.getOwner().getId().equals(userId) && !vf.isUserACollaborator(researcher.getUser()))
		{
			return "accessDenied";
		}
				
		if (parentFolderId != null && parentFolderId > 0) {
			
			ResearcherFolder parentFolder = researcherFileSystemService.getResearcherFolder(parentFolderId, false);
			researcherFileSystemService.addFileToResearcher(parentFolder, vf.getCurrentVersion().getIrFile(), vf.getLargestVersion());
		} else {
			researcher.createRootFile(vf.getCurrentVersion().getIrFile(), vf.getLargestVersion());
			researcherService.saveResearcher(researcher);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Get researcher file system
	 * 
	 */
	public String getResearcherFolders() {
		loadResearcher();
		if( researcher == null )
		{
			return "accessDenied";
		}
		if(parentFolderId != null && parentFolderId > 0)
		{
			ResearcherFolder reseacherFolder = researcherFileSystemService.getResearcherFolder(parentFolderId, false);
			if(!reseacherFolder.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
			researcherFolderPath = researcherFileSystemService.getResearcherFolderPath(parentFolderId);
		}
		
		Collection<ResearcherFolder> myResearcherFolders = researcherFileSystemService.getFoldersForResearcher(researcher.getId(), parentFolderId);
		Collection<ResearcherFile> myResearcherFiles = researcherFileSystemService.getResearcherFiles(researcher.getId(), parentFolderId);
		Collection<ResearcherPublication> myResearcherPublications = researcherFileSystemService.getResearcherPublications(researcher.getId(), parentFolderId);
		Collection<ResearcherLink> myResearcherLinks = researcherFileSystemService.getResearcherLinks(researcher.getId(), parentFolderId);
		Collection<ResearcherInstitutionalItem> myResearcherInstitutionalItems = researcherFileSystemService.getResearcherInstitutionalItems(researcher.getId(), parentFolderId);
		Collection<FileSystem> researcherFileSystem = new LinkedList<FileSystem>();

    	researcherFileSystem.addAll(myResearcherFolders);
    	researcherFileSystem.addAll(myResearcherFiles);
    	researcherFileSystem.addAll(myResearcherPublications);
    	researcherFileSystem.addAll(myResearcherLinks);
    	researcherFileSystem.addAll(myResearcherInstitutionalItems);
    	
    	createResearcherFileSystemForDisplay(researcherFileSystem);
	    
		return SUCCESS;
	}

	/*
	 * Retrieves the file version of the IrFile for the display of versions
	 */
	private void createResearcherFileSystemForDisplay(Collection<FileSystem> researcherFileSystem) {
		
		researcherFileSystemVersions = new LinkedList<ResearcherFileSystemVersion>(); 
			
		for (FileSystem fileSystem:researcherFileSystem) {
			ResearcherFileSystemVersion researcherFileSystemVersion = null;
			
			if (fileSystem.getFileSystemType().equals(FileSystemType.RESEARCHER_FILE)) {
				VersionedFile vf = repositoryService.getVersionedFileByIrFile(((ResearcherFile)fileSystem).getIrFile());
				
				Set<FileVersion> fileVersions = null;
				
				if (vf != null) {
					fileVersions = vf.getVersions();
				}
				
				researcherFileSystemVersion = new ResearcherFileSystemVersion(fileSystem, fileVersions);
			} else {
				researcherFileSystemVersion = new ResearcherFileSystemVersion(fileSystem, null);
			}
			researcherFileSystemVersions.add(researcherFileSystemVersion);
		}
	}

	/**
	 * Change version of file in researcher
	 * 
	 */
	public String changeFileVersion() {
		loadResearcher();
		if( researcher == null )
		{
			return "accessDenied";
		}
		FileVersion fileVersion = repositoryService.getFileVersion(fileVersionId, false);
		
		VersionedFile vf = fileVersion.getVersionedFile();
		
		// user must be the owner or collaborator of the versioned file
		if( !vf.getOwner().getId().equals(userId) && !vf.isUserACollaborator(researcher.getUser()))
		{
			return "accessDenied";
		}
		
		ResearcherFile researcherFile = researcherFileSystemService.getResearcherFile(researcherFileId, false);
		// researchers can only access their own information
		if(!researcherFile.getResearcher().getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		
		
		researcherFile.setIrFile(fileVersion.getIrFile());
		researcherFile.setVersionNumber(fileVersion.getVersionNumber());

		researcherFileSystemService.saveResearcherFile(researcherFile);
		
		return SUCCESS;
	}
	
	/**
	 * Simple class to help with the display of the 
	 * researcher file and the file versions available.
	 * 
	 * @author Sharmila Ranganathan
	 *
	 */
	public static class ResearcherFileSystemVersion
	{
		private FileSystem researcherFileSystem;
		
		private Set<FileVersion> versions = new HashSet<FileVersion>();
		
		public ResearcherFileSystemVersion(FileSystem researcherFileSystem, Set<FileVersion> versions)
		{
			this.researcherFileSystem = researcherFileSystem;
			this.versions= versions;
		}

		public FileSystem getResearcherFileSystem() {
			return researcherFileSystem;
		}

		public void setResearcherFileSystem(FileSystem researcherFileSystem) {
			this.researcherFileSystem = researcherFileSystem;
		}

		public Set<FileVersion> getVersions() {
			return versions;
		}

		public void setVersions(Set<FileVersion> versions) {
			this.versions = versions;
		}
	}
	
	/**
	 * Execute action
	 * 
	 */
	public String execute() { 
		
		loadResearcher();
		if( researcher == null )
		{
			return "accessDenied";
		}
		
		return SUCCESS;
	}	

	/**
	 * Get the parent folder id
	 * 
	 * @return parent folder id 
	 */
	public Long getParentPersonalFolderId() {
		return parentPersonalFolderId;
	}

	/**
	 * Set the parent folder id
	 * 
	 * @param parentPersonalFolderId parent folder id
	 */
	public void setParentPersonalFolderId(Long parentPersonalFolderId) {
		this.parentPersonalFolderId = parentPersonalFolderId;
	}

	/**
	 * Get the file id
	 * 
	 * @return versioned file id
	 */
	public Long getVersionedFileId() {
		return versionedFileId;
	}

	/**
	 * Set versioned file id
	 * 
	 * @param versionedFileId file id
	 */
	public void setVersionedFileId(Long versionedFileId) {
		this.versionedFileId = versionedFileId;
	}

	/**
	 * Get the researcher
	 * 
	 * @return researcher
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Get user file system service
	 * 
	 * @return
	 */
	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	/**
	 * Set user file system service
	 * 
	 * @param userFileSystemService
	 */
	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	/**
	 * Set researcher service
	 * 
	 * @param researcherService
	 */
	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	/**
	 * Get parent collection id
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}
	

	/**
	 * Service for dealing with user information.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Set parent collection id
	 * 
	 * @param parentCollectionId
	 */
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	/**
	 * Set the repository service.
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get the description
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the researcher folder path.
	 * 
	 * @return
	 */
	public Collection<ResearcherFolder> getResearcherFolderPath() {
		return researcherFolderPath;
	}

	/**
	 * Get the personal folder path.
	 * 
	 * @return
	 */
	public Collection<PersonalFolder> getPersonalFolderPath() {
		return personalFolderPath;
	}

	/**
	 * Get the personal file system.
	 * 
	 * @return
	 */
	public Collection<FileSystem> getPersonalFileSystem() {
		return personalFileSystem;
	}

	/**
	 * Get the researcher file system versions.
	 * 
	 * @return
	 */
	public List<ResearcherFileSystemVersion> getResearcherFileSystemVersions() {
		return researcherFileSystemVersions;
	}

	/**
	 * Get the researcher file id.
	 * 
	 * @return
	 */
	public Long getResearcherFileId() {
		return researcherFileId;
	}

	/**
	 * Set the researcher file id.
	 * 
	 * @param researcherFileId
	 */
	public void setResearcherFileId(Long researcherFileId) {
		this.researcherFileId = researcherFileId;
	}

	/**
	 * Get the file version id.
	 * 
	 * @return
	 */
	public Long getFileVersionId() {
		return fileVersionId;
	}

	/**
	 * Set the file version id.
	 * 
	 * @param fileVersionId
	 */
	public void setFileVersionId(Long fileVersionId) {
		this.fileVersionId = fileVersionId;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;	
	}

	/**
	 * Set the researcher file system service.
	 * 
	 * @param researcherFileSystemService
	 */
	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}

}
