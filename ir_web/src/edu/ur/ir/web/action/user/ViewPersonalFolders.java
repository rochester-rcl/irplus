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


package edu.ur.ir.web.action.user;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Validateable;

import edu.ur.ir.FileSystem;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.web.action.UserIdAware;


/**
 * Show a set of files and folders for a given user.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewPersonalFolders extends ActionSupport implements  
  UserIdAware, Validateable {
	
	/**Eclipse gernerated id */
	private static final long serialVersionUID = 6684102482237911784L;

	/**  Id of the suer who has folders  */
	private Long userId;
	
	/** The user who owns the folders  */
	private IrUser user;
	
	/**  User information data access  */
	private UserService userService;
	
	/** Service for dealing with user file system */
	private UserFileSystemService userFileSystemService;

	/** Service for dealing with indexing user information*/
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
    /** A collection of folders and files for a user in a given location of
        ther personal directory.*/
    private List<FileSystem> fileSystem;
    
    /** set of folders that are the path for the current folder */
    private Collection <PersonalFolder> folderPath;
	
	/** The folder that owns the listed files and folders */
	private Long parentFolderId;
	
	/** list of folder ids to perform actions on*/
	private Long[] folderIds;
	
	/** list of file ids to perform actions on*/
	private Long[] fileIds;
	
	/** True indicates the folders were deleted */
	private boolean foldersDeleted = true;
	
	/** Message used to indicate there is a problem with deleting the folders. */
	private String foldersDeletedMessage;
	
	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "desc";
	
	/** name of the element to sort on 
	 *   this is for incoming requests */
	private String sortElement = "type";
	
	/** use the type sort this is information for the page */
	private String folderTypeSort = "none";
	
	/** use the name sort this is information for the page */
	private String folderNameSort = "none";
	
	/** Size of file system for user */
	private Long fileSystemSize;
	
	/**  Logger for vierw workspace action */
	private static final Logger log = Logger.getLogger(ViewPersonalFolders.class);
	
	/** Quartz scheduler instance to schedule jobs  */
	private Scheduler quartzScheduler;

	/**
	 * Get folder table
	 * 
	 * @return
	 */
	public String getTable()
	{
		if(parentFolderId != null && parentFolderId > 0)
		{
		    PersonalFolder parent = userFileSystemService.getPersonalFolder(parentFolderId, false);
		    if( !parent.getOwner().getId().equals(userId))
		    {
		    	return "accessDenied";
		    }
		}
		log.debug("getTableCalled");
		createFileSystem();

		return SUCCESS;
	}
	
	/**
	 * Removes the select files and folders.
	 * 
	 * @return
	 */
	public String deleteFileSystemObjects()
	{
		log.debug("Delete folders called");
		user = userService.getUser(userId, false);
		
		if( folderIds != null )
		{
		    for(int index = 0; index < folderIds.length; index++)
		    {
			    log.debug("Deleting folder with id " + folderIds[index]);
			    PersonalFolder pf = userFileSystemService.getPersonalFolder(folderIds[index], false);
			    
			    if( !pf.getOwner().getId().equals(userId))
			    {
			    	return "accessDenied";
			    }
			    
			    //un-index all the files
			    List<PersonalFile> allFiles = 
			    	userFileSystemService.getAllFilesInFolderAndSubFolder(pf.getId(), pf.getOwner().getId());
			    
			    for(PersonalFile aFile : allFiles)
			    {
			    	deleteFileFromIndex(aFile, user);
			    }
			    
			    PersonalWorkspaceSchedulingIndexHelper schedulingHelper = new PersonalWorkspaceSchedulingIndexHelper();
				schedulingHelper.scheduleIndexingDelete(quartzScheduler, pf);
			   
			    userFileSystemService.deletePersonalFolder(pf);
		    }
		}
		
		if(fileIds != null)
		{
			for(int index = 0; index < fileIds.length; index++)
			{
				log.debug("Deleting file with id " + fileIds[index]);
				PersonalFile pf = userFileSystemService.getPersonalFile( fileIds[index], false);
				 if( !pf.getOwner().getId().equals(userId))
				 {
				   	return "accessDenied";
				 }
				deleteFileFromIndex(pf, user);
				userFileSystemService.deletePersonalFile(pf);
			}
		}
		createFileSystem();
		return SUCCESS;
	}
	
	/**
	 * Deletes the file from the users index.  If the user is the owner of the file
	 * the file is removed from the indexes of all users.
	 * 
	 * @param aFile - file to remove
	 * @param aUser - user to remove the file from
	 */
	private void deleteFileFromIndex(PersonalFile aFile, IrUser aUser)
	{
		if(aFile.getOwner().equals(aUser))
    	{
    		userWorkspaceIndexService.deleteFromAllIndexes(aFile);
    	}
    	else
    	{
    		PersonalWorkspaceSchedulingIndexHelper schedulingHelper = new PersonalWorkspaceSchedulingIndexHelper();
			schedulingHelper.scheduleIndexingDelete(quartzScheduler, aFile);
    	}
	}
	
	/**
	 * Create the file system to view.
	 */
	private void createFileSystem()
	{
		user = userService.getUser(userId, false);
		if(parentFolderId != null && parentFolderId > 0)
		{
		    folderPath = userFileSystemService.getPersonalFolderPath(parentFolderId);
		}
		Collection<PersonalFolder> myPersonalFolders = userFileSystemService.getPersonalFoldersForUser(userId, parentFolderId);
		Collection<PersonalFile> myPersonalFiles = userFileSystemService.getPersonalFilesInFolder(userId, parentFolderId);
		
	    fileSystem = new LinkedList<FileSystem>();
	    
	    fileSystem.addAll(myPersonalFolders);
	    fileSystem.addAll(myPersonalFiles);
	    
	    FileSystemSortHelper sortHelper = new FileSystemSortHelper();
	    if( sortElement.equals("type"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_ASC);
	    		folderTypeSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.TYPE_DESC);
	    		folderTypeSort = "desc";
	    	}
	    	else
	    	{
	    		folderTypeSort = "none";
	    	}
	    }
	    
	    if( sortElement.equals("name"))
	    {
	    	if(sortType.equals("asc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_ASC);
	    		folderNameSort = "asc";
	    	}
	    	else if( sortType.equals("desc"))
	    	{
	    		sortHelper.sort(fileSystem, FileSystemSortHelper.NAME_DESC);
	    		folderNameSort = "desc";
	    	}
	    	else
	    	{
	    		folderNameSort = "none";
	    	}
	    }
	    
		fileSystemSize = userFileSystemService.getFileSystemSizeForUser(getUser());

	}
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public Long getParentFolderId() {
		return parentFolderId;
	}

	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}

	public Long[] getFolderIds() {
		return folderIds;
	}

	public void setFolderIds(Long[] folderIds) {
		this.folderIds = folderIds;
	}

	public boolean isFoldersDeleted() {
		return foldersDeleted;
	}

	public void setFoldersDeleted(boolean foldersDeleted) {
		this.foldersDeleted = foldersDeleted;
	}

	public String getFoldersDeletedMessage() {
		return foldersDeletedMessage;
	}

	public void setFoldersDeletedMessage(String foldersDeletedMessage) {
		this.foldersDeletedMessage = foldersDeletedMessage;
	}
	
	
	@SuppressWarnings("unchecked")
	public Collection getFileSystem() {
		return fileSystem;
	}


	public Long[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(Long[] fileIds) {
		this.fileIds = fileIds;
	}
	
	public void validate()
	{
		log.debug("Validate called");
	}

	public Collection<PersonalFolder> getFolderPath() {
		return folderPath;
	}

	public IrUser getUser() {
		return user;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	public UserWorkspaceIndexService getUserWorkspaceIndexService() {
		return userWorkspaceIndexService;
	}

	public void setUserWorkspaceIndexService(UserWorkspaceIndexService userIndexService) {
		this.userWorkspaceIndexService = userIndexService;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortElement() {
		return sortElement;
	}

	public void setSortElement(String sortElement) {
		this.sortElement = sortElement;
	}

	public String getFolderTypeSort() {
		return folderTypeSort;
	}

	public void setFolderTypeSort(String typeHeaderSort) {
		this.folderTypeSort = typeHeaderSort;
	}

	public String getFolderNameSort() {
		return folderNameSort;
	}

	public void setFolderNameSort(String folderIdSort) {
		this.folderNameSort = folderIdSort;
	}

	public Long getFileSystemSize() {
		return fileSystemSize;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Scheduler getQuartzScheduler() {
		return quartzScheduler;
	}

	public void setQuartzScheduler(Scheduler quartzScheduler) {
		this.quartzScheduler = quartzScheduler;
	}
	

}
