/**  
   Copyright 2008 - 2011 University of Rochester

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


package edu.ur.ir.web.action.groupspace;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceFile;
import edu.ur.ir.groupspace.GroupWorkspaceFileSystemService;
import edu.ur.ir.groupspace.GroupWorkspaceFolder;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.FileUploadInfo;

/**
 * Add files to a group workspace.
 * 
 * @author Nathan Sarr
 *
 */
public class AddGroupWorkspaceFiles extends ActionSupport implements UserIdAware{

	/* eclipse generated id  */
	private static final long serialVersionUID = -1222831812927650995L;

	/*  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddGroupWorkspaceFiles.class);
	
	/* Personal Folder the user will be adding files to */
	GroupWorkspaceFolder groupWorkspaceFolder;

	/* The id of the user to add files to  */
	private Long userId;
	
	/* the users folder to add the files to */
	private Long folderId = 0l;
	
	/* Service for dealing with user information  */
	private UserService userService;
	
	/*  Service for dealing with user file systems */
	private GroupWorkspaceFileSystemService groupWorkspaceFileSystemService;

	/* description of the file  */
	private String[] userFileDescription;
	
	/* characters that cannot exist in file names */
	private String illegalFileNameCharacters = "";
	
	/* actual set of files uploaded */
	private File[] file;
	
	/* Original file name */
	private String[] fileFileName;
				
	/* Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/* Files not added due to errors */
	LinkedList<FileUploadInfo> filesNotAdded = new LinkedList<FileUploadInfo>();

	/* Files not added due to illegal file names */
	LinkedList<FileUploadInfo> illegalFileNames = new LinkedList<FileUploadInfo>();
	
	/* process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/* service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/* service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;

	/* Service to deal with group workspace information. */
	private GroupWorkspaceService groupWorkspaceService;

 
	/* Group workspace being worked in */
    private GroupWorkspace groupWorkspace;
    
  
	/* id of the group workspace */
    private Long groupWorkspaceId;
    
 

	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}
	
	public String execute()
	{
		IrUser user = userService.getUser(userId, false);
		//only authoring roles can add personal files
		if( !user.hasRole(IrRole.AUTHOR_ROLE))
		{
			return("accessDenied");
		}
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		if( folderId != null )
		{
			groupWorkspaceFolder = groupWorkspaceFileSystemService.getFolder(folderId, false);
		}
		else
		{
			folderId = 0l;
		}
		return SUCCESS;
	}
	
	/**
	 * Upload the specified files.
	 * 
	 * @return
	 * @throws IOException
	 */
	public String uploadFiles() throws IOException
	{
		
		log.debug("Upload files called");
		IrUser user = userService.getUser(userId, false);
		
		
		//only authoring roles can add personal files
		if( !user.hasRole(IrRole.AUTHOR_ROLE))
		{
			return("accessDenied");
		}
		
		groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		
		if( groupWorkspaceFolder != null && !groupWorkspaceFolder.getOwner().getId().equals(userId))
    	{
			//destination does not belong to user
    		log.error("user does not own folder = " + groupWorkspaceFolder + " user = " + user);
    		return("accessDenied");
    		
    	}
		
		LinkedList<GroupWorkspaceFile> addedFiles = new LinkedList<GroupWorkspaceFile>();
		
		if( file != null)
		{
			Repository repository = 
				repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			
			for( int index = 0; index < file.length; index++)
			{
				
				GroupWorkspaceFile gf = null;
				
				FileUploadInfo fileUploadInfo = new FileUploadInfo(fileFileName[index], 
						userFileDescription[index]);
				
				// make sure we have a name if not do not upload!
				if( fileFileName[index] != null && !fileFileName[index].trim().equals(""))
				{
				
				    if( file[index].length() > 0)
				    {
					    log.debug( "Creating non EMPTY file " + fileFileName[index]);
					    try {
							gf = createNonEmptyFile( repository,
									file[index],
									user, 
									fileFileName[index],
									userFileDescription[index]);
							addedFiles.add(gf);
						} catch (DuplicateNameException e) {
							
							filesNotAdded.add(fileUploadInfo);
						} catch (IllegalFileSystemNameException ifsne) {
							illegalFileNames.add(fileUploadInfo);
				    	}
				    }
				    else
				    {
					    log.debug( "Creating EMPTY file " + fileFileName[index]);
					    if( groupWorkspaceFolder == null)
					    {
					        try
					        {
					            gf = groupWorkspaceFileSystemService.addFile(repository,
					            groupWorkspace,
						        user, 
						        fileFileName[index],
						        userFileDescription[index] );
					            addedFiles.add(gf);
					        }
					        catch(DuplicateNameException e)
					        {
					    	    filesNotAdded.add(fileUploadInfo);
					        } catch (IllegalFileSystemNameException ifsne) {
					    	    illegalFileNames.add(fileUploadInfo);
					        }
					    }
					    else
					    {
					    	try
					    	{
					    	     gf = groupWorkspaceFileSystemService.addFile(repository,
					    			 groupWorkspaceFolder,
					    			 user,
					    			 fileFileName[index],
									 userFileDescription[index]);
					    	     addedFiles.add(gf);
					    	     
					    	     
					    	}
					    	catch(DuplicateNameException e)
					    	{
					    		filesNotAdded.add(fileUploadInfo);
					    	} catch (IllegalFileSystemNameException ifsne) {
					    		illegalFileNames.add(fileUploadInfo);
					    	}
					    }
				    }
				}
			}
			
			for( GroupWorkspaceFile groupFile : addedFiles)
			{
				userWorkspaceIndexProcessingRecordService.saveAll(groupFile, 
		    			indexProcessingTypeService.get(IndexProcessingTypeService.INSERT));
			}
		}
		
		if( this.getAllFilesAdded() )
		{
		    return SUCCESS;
		}
		else
		{
			if( illegalFileNames.size() > 0 )
			{
			    char[] invalidCharacters = IllegalFileSystemNameException.INVALID_CHARACTERS;
			    for(char ch : invalidCharacters )
			    {
				    illegalFileNameCharacters = illegalFileNameCharacters + " " + ch;
			    }
			}
			return INPUT;
		}
	}
	

	public String[] getUserFileDescription() {
		return userFileDescription;
	}

	public void setUserFileDescription(String[] fileDescriptons) {
		this.userFileDescription = fileDescriptons;
	}

	public File[] getFile() {
		return file;
	}

	public void setFile(File[] files) {
		this.file = files;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	
	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileName) {
		this.fileFileName = fileName;
	}


	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	private GroupWorkspaceFile createNonEmptyFile(
			Repository repository,
			File file, 
			IrUser user, 
			String fileName, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException
	{
		GroupWorkspaceFile gf = null;
		if( groupWorkspaceFolder != null)
		{
		    gf = groupWorkspaceFileSystemService.addFile(repository, 
		    	groupWorkspaceFolder,
		    	user,
			    file, 
			    fileName,
			    description);
		}
		else
		{
			gf = groupWorkspaceFileSystemService.addFile(repository,
				groupWorkspace,
				user,
				file, 
				fileName,
				description );
		}
		
		IrFile irFile = gf.getVersionedFile().getCurrentVersion().getIrFile();
	    thumbnailTransformerService.transformFile(repository, irFile);			    
		return gf;
		
	}



	
	/**
	 * Determine if all the files were added
	 * 
	 * @return true if all files have been added
	 */
	public boolean getAllFilesAdded()
	{
		return (filesNotAdded.size() == 0) && (illegalFileNames.size() == 0);
	}


	/**
	 * Get the files that were not added.
	 * 
	 * @return
	 */
	public LinkedList<FileUploadInfo> getFilesNotAdded() {
		
		log.debug("get files not added called");
		
		for( FileUploadInfo info : filesNotAdded)
		{
			log.debug("info " + info + " found");
		}
		return filesNotAdded;
	}


	public LinkedList<FileUploadInfo> getIllegalFileNames() {
		return illegalFileNames;
	}
	
	/**
	 * Get the illegal file name charachers.
	 * 
	 * @return
	 */
	public String getIllegalFileNameCharacters()
	{
		return illegalFileNameCharacters;
	}

	/**
	 * Set the user workspace index processing service.
	 * 
	 * @param userWorkspaceIndexProcessingRecordService
	 */
	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}


	/**
	 * Set the index processing type service.
	 * 
	 * @param indexProcessingTypeService
	 */
	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	

	/**
	 * Set the thumbnail service.
	 * 
	 * @param thumbnailTransformerService
	 */
	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}

	/**
	 * Get the group workspace id.
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
	 * Get the group workspace.
	 * 
	 * @return
	 */
	public GroupWorkspace getGroupWorkspace() {
	    return groupWorkspace;
	}
	
	/**
	 * Get the group workspace folder
	 * @return
	 */
	public GroupWorkspaceFolder getGroupWorkspaceFolder() {
		return groupWorkspaceFolder;
	}


}
