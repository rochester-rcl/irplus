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

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.exception.DuplicateNameException;
import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.index.IndexProcessingTypeService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserWorkspaceIndexProcessingRecordService;
import edu.ur.ir.user.UserWorkspaceIndexService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.util.FileUploadInfo;

import org.apache.commons.io.FilenameUtils;

/**
 * Add personal files to the specified folder.
 * 
 * @author Nathan Sarr
 *
 */
public class AddPersonalFiles extends ActionSupport implements UserIdAware, Preparable{

	/**Eclipse generated id */
	private static final long serialVersionUID = 8046524638321276009L;

	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddPersonalFiles.class);
	
	/** Personal Folder the user will be adding files to */
	PersonalFolder personalFolder;
	
	/** The id of the user to add files to  */
	private Long userId;
	
	/** the users folder to add the files to */
	private Long folderId;
	
	/** Service for dealing with user information  */
	private UserService userService;
	
	/**  Service for dealing with user file systems */
	private UserFileSystemService userFileSystemService;

	/** description of the file  */
	private String[] userFileDescription;
	
	/** characters that cannot exist in file names */
	private String illegalFileNameCharacters = "";
	
	/** actual set of files uploaded */
	private File[] file;
	
	/** Original file name */
	private String[] fileFileName;
				
	/** Repository service for placing information in the repostiroy */
	private RepositoryService repositoryService;
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** Files not added due to errors */
	LinkedList<FileUploadInfo> filesNotAdded = new LinkedList<FileUploadInfo>();

	/** Files not added due to illegal file names */
	LinkedList<FileUploadInfo> illegalFileNames = new LinkedList<FileUploadInfo>();
	
	/** process for setting up personal workspace information to be indexed */
	private UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService;
	
	/** service for accessing index processing types */
	private IndexProcessingTypeService indexProcessingTypeService;
	
	/** service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;



	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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
		
		if( personalFolder != null && !personalFolder.getOwner().getId().equals(userId))
    	{
			//destination does not belong to user
    		log.error("user does not own folder = " + personalFolder + " user = " + user);
    		return("accessDenied");
    		
    	}
		
		//only authoring roles can add personal files
		if( !user.hasRole(IrRole.AUTHOR_ROLE)  )
		{
			for( int index = 0; index < file.length; index++)
			{
				
				
				String theFileName = FilenameUtils.getName(fileFileName[index]);
				
				FileUploadInfo fileUploadInfo = new FileUploadInfo(theFileName, 
						userFileDescription[index]);
				filesNotAdded.add(fileUploadInfo);
			}
			return INPUT;
		}
			
		
		LinkedList<PersonalFile> addedFiles = new LinkedList<PersonalFile>();
		
		if( file != null)
		{
			Repository repository = 
				repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			
			for( int index = 0; index < file.length; index++)
			{
				
				PersonalFile pf = null;
				
				FileUploadInfo fileUploadInfo = new FileUploadInfo(fileFileName[index], 
						userFileDescription[index]);
				
				// make sure we have a name if not do not upload!
				if( fileFileName[index] != null && !fileFileName[index].trim().equals(""))
				{
				
				    if( file[index].length() > 0)
				    {
					    log.debug( "Creating non EMPTY file " + fileFileName[index]);
					    try {
							pf = createNonEmptyFile( repository,
									file[index],
									user, 
									fileFileName[index],
									userFileDescription[index]);
							addedFiles.add(pf);
						} catch (DuplicateNameException e) {
							
							filesNotAdded.add(fileUploadInfo);
						} catch (IllegalFileSystemNameException ifsne) {
							illegalFileNames.add(fileUploadInfo);
				    	}
				    }
				    else
				    {
					    log.debug( "Creating EMPTY file " + fileFileName[index]);
					    if( personalFolder == null)
					    {
					        try
					        {
					            pf = userFileSystemService.addFileToUser(repository, 
						        user, 
						        fileFileName[index],
						        userFileDescription[index] );
					            addedFiles.add(pf);
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
					    	     pf = userFileSystemService.addFileToUser(repository,
					    			 personalFolder,
					    			 fileFileName[index],
									 userFileDescription[index]);
					    	     addedFiles.add(pf);
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
			
			for( PersonalFile personalFile : addedFiles)
			{
				userWorkspaceIndexProcessingRecordService.saveAll(personalFile, 
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

	public UserService getUserService() {
		return userService;
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

	public void prepare() throws Exception {
		if( folderId != null && folderId > 0 )
		{
		    personalFolder = userFileSystemService.getPersonalFolder(folderId, false);
		}
		
	}

	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}

	public void setPersonalFolder(PersonalFolder personalFolder) {
		this.personalFolder = personalFolder;
	}

	public String[] getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String[] fileName) {
		this.fileFileName = fileName;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	private PersonalFile createNonEmptyFile(
			Repository repository,
			File file, 
			IrUser user, 
			String fileName, 
			String description) throws DuplicateNameException, IllegalFileSystemNameException
	{
		PersonalFile pf = null;
		if( personalFolder != null)
		{
		    pf = userFileSystemService.addFileToUser(repository, 
			    file, 
			    personalFolder,
			    fileName,
			    description);
		}
		else
		{
			pf = userFileSystemService.addFileToUser(repository, 
				file, 
				user,
				fileName,
				description );
		}
		
		IrFile irFile = pf.getVersionedFile().getCurrentVersion().getIrFile();
	    thumbnailTransformerService.transformFile(repository, irFile);			    
		return pf;
		
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
	
	public String getIllegalFileNameCharacters()
	{
		return illegalFileNameCharacters;
	}


	public UserWorkspaceIndexProcessingRecordService getUserWorkspaceIndexProcessingRecordService() {
		return userWorkspaceIndexProcessingRecordService;
	}


	public void setUserWorkspaceIndexProcessingRecordService(
			UserWorkspaceIndexProcessingRecordService userWorkspaceIndexProcessingRecordService) {
		this.userWorkspaceIndexProcessingRecordService = userWorkspaceIndexProcessingRecordService;
	}


	public IndexProcessingTypeService getIndexProcessingTypeService() {
		return indexProcessingTypeService;
	}


	public void setIndexProcessingTypeService(
			IndexProcessingTypeService indexProcessingTypeService) {
		this.indexProcessingTypeService = indexProcessingTypeService;
	}
	
	public ThumbnailTransformerService getThumbnailTransformerService() {
		return thumbnailTransformerService;
	}


	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}


}
