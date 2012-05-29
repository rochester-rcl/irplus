package edu.ur.ir.web.action.groupspace;

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
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFile;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemLink;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageFolder;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageInstitutionalItem;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPagePublication;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageService;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * @author Nathan Sarr
 *
 */
public class AddGroupWorkspaceProjectPageFile  extends ActionSupport implements UserIdAware{

	/*  Eclipse generated id */
	private static final long serialVersionUID = 24813010098426817L;

	/*  Logger for add files to item action */
	private static final Logger log = Logger.getLogger(AddGroupWorkspaceProjectPageFile.class);
	
	/* Service for project page  */
	private GroupWorkspaceProjectPageService groupWorkspaceProjectPageService;

	/* Service for dealing with group workspace project page file system */
	private GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService;
	
	/* File system service for group workspace */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;
	
	/* Service to deal with group workspace information */
	private GroupWorkspaceService groupWorkspaceService;
	
	/* Service for dealing with user information */
	private UserService userService;

	/* Repository system service for files */
	private RepositoryService repositoryService;
	
	/* File id to add / remove files action*/
	private Long versionedFileId;
	
	/* Group workspace project page being edited */
	private GroupWorkspaceProjectPage groupWorkspaceProjectPage;
		
	/* Group workspace project page folder id */
	private Long parentFolderId;
	
	 /* A collection of file system objects in the group workspace.*/
	private Collection<FileSystem> groupWorkspaceFileSystem;
	
	/* A collection of folders and files for a user in a given location of
    their personal directory.*/
	List<GroupWorkspaceProjectPageFileSystemVersion> groupWorkspaceProjectPageFileSystemVersions = new LinkedList<GroupWorkspaceProjectPageFileSystemVersion>();
	
	/* set of folders that are the path for the current folder */
	private Collection <GroupWorkspaceProjectPageFolder> groupWorkspaceProjectPageFolderPath;

	/* set of folders that are the path for the current folder */
	private Collection <GroupWorkspaceFolder> groupWorkspaceFolderPath;

	/* Id of the parent group workspace */
	private Long parentGroupWorkspaceFolderId;	
	
	/* description for file */
	private String description;

	/* Id of the group workspace project page file */
	private Long groupWorkspaceProjectPageFileId;
	
	/* Id of the file version */
	private Long fileVersionId;
	
	/* id of the user accessing the information */
	private Long userId;
	
	/* id of the group workspace */
	private Long groupWorkspaceId;
	
	private Long groupWorkspaceProjectPageId;
	

	
	/**
	 * Create the file system to view group workspace folders and files.
	 */
	public String getGroupWorkspaceFolders()
	{

		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		if(parentGroupWorkspaceFolderId != null && parentGroupWorkspaceFolderId > 0)
		{
			GroupWorkspaceFolder groupFolder = groupWorkspaceFileSystemService.getFolder(parentGroupWorkspaceFolderId, false);
			
			
		    groupWorkspaceFolderPath = groupWorkspaceFileSystemService.getFolderPath(groupFolder);
		}
		
		
		Collection<GroupWorkspaceFolder> groupWorkspaceFolders = groupWorkspaceFileSystemService.getFolders(groupWorkspaceId, parentGroupWorkspaceFolderId);
		Collection<GroupWorkspaceFile> groupWorkspaceFiles = groupWorkspaceFileSystemService.getFiles(groupWorkspaceId, parentGroupWorkspaceFolderId);
		
	    groupWorkspaceFileSystem = new LinkedList<FileSystem>();
	    
	    for(GroupWorkspaceFolder o : groupWorkspaceFolders)
	    {
	    	 groupWorkspaceFileSystem.add(o);
	    }
	    
	    for(GroupWorkspaceFile o: groupWorkspaceFiles)
	    {
	    	 groupWorkspaceFileSystem.add(o);
	    }
	    
	    return SUCCESS;
	}
	
	/**
	 * Add file to group workspace project page
	 */
	public String addFile() {
		
		log.debug("Add file versionedFileId = " + versionedFileId + " to group workspace project page " + 
				groupWorkspaceProjectPageId);
		IrUser user = userService.getUser(userId, false);
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		
		VersionedFile vf = repositoryService.getVersionedFile(versionedFileId, false);
		
		GroupWorkspaceFile file = groupWorkspaceFileSystemService.getGroupWorkspaceFileWithVersionedFile(groupWorkspaceId, vf.getId());
		
		// file must be found
		if( file == null )
		{
			return "notFound";
		}
		// user must be the owner of the group workspace file
		else if( !file.getGroupWorkspace().getId().equals(groupWorkspace.getId()))
		{
			return "accessDenied";
		}
				
		if (parentFolderId != null && parentFolderId > 0) {
			
			GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPageFileSystemService.getFolder(parentFolderId, false);
			groupWorkspaceProjectPageFileSystemService.addFile(folder, vf.getCurrentVersion().getIrFile(), vf.getLargestVersion());
		} else {
			groupWorkspaceProjectPage.createRootFile(vf.getCurrentVersion().getIrFile(), vf.getLargestVersion());
			groupWorkspaceProjectPageService.save(groupWorkspaceProjectPage);
		}
		
		return SUCCESS;
	}
	
	/**
	 * Get researcher file system
	 * 
	 */
	public String getGroupWorkspaceProjectPageFolders() {
		IrUser user = userService.getUser(userId, false);
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		if(parentFolderId != null && parentFolderId > 0)
		{
			GroupWorkspaceProjectPageFolder folder = groupWorkspaceProjectPageFileSystemService.getFolder(parentFolderId, false);
			if(!folder.getGroupWorkspaceProjectPage().getGroupWorkspace().getId().equals(groupWorkspace.getId()))
			{
				return "accessDenied";
			}
			groupWorkspaceProjectPageFolderPath = groupWorkspaceProjectPageFileSystemService.getFolderPath(parentFolderId);
		}
		
		Collection<GroupWorkspaceProjectPageFolder> folders = groupWorkspaceProjectPageFileSystemService.getFolders(groupWorkspaceProjectPage.getId(), parentFolderId);
		log.debug("folders = " + folders.size());
		Collection<GroupWorkspaceProjectPageFile> files = groupWorkspaceProjectPageFileSystemService.getFiles(groupWorkspaceProjectPage.getId(), parentFolderId);
		log.debug("files = " +  files.size());
		Collection<GroupWorkspaceProjectPagePublication> publications = groupWorkspaceProjectPageFileSystemService.getPublications(groupWorkspaceProjectPage.getId(), parentFolderId);
		log.debug("publications = " +  publications.size());
		Collection<GroupWorkspaceProjectPageFileSystemLink> links = groupWorkspaceProjectPageFileSystemService.getLinks(groupWorkspaceProjectPage.getId(), parentFolderId);
		log.debug("links = " +  links.size());
		Collection<GroupWorkspaceProjectPageInstitutionalItem> institutionalItems = groupWorkspaceProjectPageFileSystemService.getInstitutionalItems(groupWorkspaceProjectPage.getId(), parentFolderId);
		log.debug("institutional items = " +  institutionalItems.size());
		Collection<FileSystem> groupWorkspaceProjectPageFileSystem = new LinkedList<FileSystem>();
		
		
		groupWorkspaceProjectPageFileSystem.addAll(folders);
		groupWorkspaceProjectPageFileSystem.addAll(files);
		groupWorkspaceProjectPageFileSystem.addAll(publications);
		groupWorkspaceProjectPageFileSystem.addAll(links);
		groupWorkspaceProjectPageFileSystem.addAll(institutionalItems);
    	
		log.debug("total size = " +  groupWorkspaceProjectPageFileSystem.size());
    	createFileSystemForDisplay(groupWorkspaceProjectPageFileSystem);
	    
		return SUCCESS;
	}
	
	/**
	 * Execute action
	 * 
	 */
	public String execute() { 
		IrUser user = userService.getUser(userId, false);
		groupWorkspaceProjectPage = groupWorkspaceProjectPageService.getById(groupWorkspaceProjectPageId, false);
		if( groupWorkspaceProjectPage == null )
		{
			return "notFound";
		}
		GroupWorkspace groupWorkspace = groupWorkspaceProjectPage.getGroupWorkspace();
        
		groupWorkspaceId = groupWorkspace.getId();
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		if( workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		getGroupWorkspaceFolders();
		getGroupWorkspaceProjectPageFolders();
		return SUCCESS;
	}	


	/*
	 * Retrieves the file version of the IrFile for the display of versions
	 */
	private void createFileSystemForDisplay(Collection<FileSystem> pageFileSystem) {
		
		groupWorkspaceProjectPageFileSystemVersions = new LinkedList<GroupWorkspaceProjectPageFileSystemVersion>(); 
			
		for (FileSystem fileSystem : pageFileSystem) {
			GroupWorkspaceProjectPageFileSystemVersion groupWorkspaceFileSystemVersion = null;
			
			if (fileSystem.getFileSystemType().equals(FileSystemType.GROUP_WORKSPACE_PROJECT_PAGE_FILE)) {
				VersionedFile vf = repositoryService.getVersionedFileByIrFile(((GroupWorkspaceProjectPageFile)fileSystem).getIrFile());
				
				Set<FileVersion> fileVersions = null;
				
				if (vf != null) {
					fileVersions = vf.getVersions();
				}
				
				groupWorkspaceFileSystemVersion = new GroupWorkspaceProjectPageFileSystemVersion(fileSystem, fileVersions);
			} else {
				groupWorkspaceFileSystemVersion = new GroupWorkspaceProjectPageFileSystemVersion(fileSystem, null);
			}
			groupWorkspaceProjectPageFileSystemVersions.add(groupWorkspaceFileSystemVersion);
		}
	}

	/**
	 * Change version of file in group workspace project page
	 * 
	 */
	public String changeFileVersion() {
		IrUser user = userService.getUser(userId, false);
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		GroupWorkspaceUser workspaceUser = groupWorkspace.getUser(user);
		if(workspaceUser == null || !workspaceUser.isOwner())
		{
			return "accessDenied";
		}
		groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		FileVersion fileVersion = repositoryService.getFileVersion(fileVersionId, false);
		
		VersionedFile vf = fileVersion.getVersionedFile();
		
		GroupWorkspaceFile file = groupWorkspaceFileSystemService.getGroupWorkspaceFileWithVersionedFile(groupWorkspaceId, vf.getId());
		
		// file must be found
		if( file == null )
		{
			return "notFound";
		}
		// user must be the owner of the group workspace file and file must be part of the group workspace
		else if( !file.getGroupWorkspace().getId().equals(groupWorkspace.getId()))
		{
			return "accessDenied";
		}
		
		GroupWorkspaceProjectPageFile pageFile = groupWorkspaceProjectPageFileSystemService.getFile(groupWorkspaceProjectPageFileId, false);
		// researchers can only access their own information
		if(pageFile == null )
		{
			return "notFound";
		}
		else if(!pageFile.getGroupWorkspaceProjectPage().getGroupWorkspace().getId().equals(groupWorkspaceId))
		{
			return "accessDenied";
		}
		
		
		pageFile.setIrFile(fileVersion.getIrFile());
		pageFile.setVersionNumber(fileVersion.getVersionNumber());

		groupWorkspaceProjectPageFileSystemService.save(pageFile);
		
		return SUCCESS;
	}
	
	/**
	 * Simple class to help with the display of the 
	 * group workspace project page file and the file versions available.
	 * 
	 * @author Nathan Sarr
	 *
	 */
	public static class GroupWorkspaceProjectPageFileSystemVersion
	{
		private FileSystem groupWorkspaceProjectPageFileSystem;
		
		private Set<FileVersion> versions = new HashSet<FileVersion>();
		
		public GroupWorkspaceProjectPageFileSystemVersion(FileSystem groupWorkspaceProjectPageFileSystem, Set<FileVersion> versions)
		{
			this.groupWorkspaceProjectPageFileSystem = groupWorkspaceProjectPageFileSystem;
			this.versions= versions;
		}

		public FileSystem getFileSystem() {
			return groupWorkspaceProjectPageFileSystem;
		}

		public void setResearcherFileSystem(FileSystem groupWorkspaceProjectPageFileSystem) {
			this.groupWorkspaceProjectPageFileSystem = groupWorkspaceProjectPageFileSystem;
		}

		public Set<FileVersion> getVersions() {
			return versions;
		}

		public void setVersions(Set<FileVersion> versions) {
			this.versions = versions;
		}
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
	 * Get the parent group workspace folder id.
	 * 
	 * @return id of the parent group workspace folder
	 */
	public Long getParentGroupWorkspaceFolderId() {
		return parentGroupWorkspaceFolderId;
	}

	/**
	 * Set the parent group workspace folder id.
	 * 
	 * @param parentGroupWorkspaceFolderId
	 */
	public void setParentGroupWorkspaceFolderId(Long parentGroupWorkspaceFolderId) {
		this.parentGroupWorkspaceFolderId = parentGroupWorkspaceFolderId;
	}

	/**
	 * Get the group workspace project page file id.
	 * 
	 * @return group workspace project page file id.
	 */
	public Long getGroupWorkspaceProjectPageFileId() {
		return groupWorkspaceProjectPageFileId;
	}

	/**
	 * Set the group workspace project page file id.
	 * 
	 * @param groupWorkspaceProjectPageFileId
	 */
	public void setGroupWorkspaceProjectPageFileId(
			Long groupWorkspaceProjectPageFileId) {
		this.groupWorkspaceProjectPageFileId = groupWorkspaceProjectPageFileId;
	}

	/**
	 * Get the group worksapce id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Set the group workspace id.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * Get the group workspace project page id.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceProjectPageId() {
		return groupWorkspaceProjectPageId;
	}

	/**
	 * Set the group workspace project page id.
	 * 
	 * @param groupWorkspaceProjectPageId
	 */
	public void setGroupWorkspaceProjectPageId(Long groupWorkspaceProjectPageId) {
		this.groupWorkspaceProjectPageId = groupWorkspaceProjectPageId;
	}

	/**
	 * Get the group workspace project page.
	 * 
	 * @return
	 */
	public GroupWorkspaceProjectPage getGroupWorkspaceProjectPage() {
		return groupWorkspaceProjectPage;
	}

	/**
	 * Get the group workspace project page file system versions.
	 * 
	 * @return
	 */
	public List<GroupWorkspaceProjectPageFileSystemVersion> getGroupWorkspaceProjectPageFileSystemVersions() {
		return groupWorkspaceProjectPageFileSystemVersions;
	}

	/**
	 * Get the list of folders in the path of the project page
	 * 
	 * @return
	 */
	public Collection<GroupWorkspaceProjectPageFolder> getGroupWorkspaceProjectPageFolderPath() {
		return groupWorkspaceProjectPageFolderPath;
	}

	/**
	 * Get the group workspace folder path.
	 * 
	 * @return
	 */
	public Collection<GroupWorkspaceFolder> getGroupWorkspaceFolderPath() {
		return groupWorkspaceFolderPath;
	}

	/**
	 * Set the group workspace project page service.
	 * 
	 * @param groupWorkspaceProjectPageService
	 */
	public void setGroupWorkspaceProjectPageService(
			GroupWorkspaceProjectPageService groupWorkspaceProjectPageService) {
		this.groupWorkspaceProjectPageService = groupWorkspaceProjectPageService;
	}

	/**
	 * Set the group workspace project page file system service.
	 * 
	 * @param groupWorkspaceProjectPageFileSystemService
	 */
	public void setGroupWorkspaceProjectPageFileSystemService(
			GroupWorkspaceProjectPageFileSystemService groupWorkspaceProjectPageFileSystemService) {
		this.groupWorkspaceProjectPageFileSystemService = groupWorkspaceProjectPageFileSystemService;
	}

	/**
	 * Set the group workspace file system service.
	 * 
	 * @param groupWorkspaceFileSystemService
	 */
	public void setGroupWorkspaceFileSystemService(
			GroupWorkspaceFileSystemService groupWorkspaceFileSystemService) {
		this.groupWorkspaceFileSystemService = groupWorkspaceFileSystemService;
	}

	/**
	 * Set the group workspace service.
	 * 
	 * @param groupWorkspaceService
	 */
	public void setGroupWorkspaceService(GroupWorkspaceService groupWorkspaceService) {
		this.groupWorkspaceService = groupWorkspaceService;
	}

	/**
	 * Get the group workspace file system.
	 * 
	 * @return
	 */
	public Collection<FileSystem> getGroupWorkspaceFileSystem() {
	    return groupWorkspaceFileSystem;
	}

	

}
