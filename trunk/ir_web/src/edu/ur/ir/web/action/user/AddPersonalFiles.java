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
import edu.ur.file.db.FileInfo;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
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
	
	/** the name the user wants to give the files */
	private String[] userFileName;
	
	/** description of the file  */
	private String[] userFileDescription;
	
	/** actual set of files uploaded */
	private File[] file;
	
	/** Original file name */
	private String[] fileFileName;
			
	/** Class to create jpeg thumbnails. */
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Repository service for placing information in the repostiroy */
	private RepositoryService repositoryService;
	
	/** User index service for indexing files */
	private UserWorkspaceIndexService userWorkspaceIndexService;
	
	/** Files not added due to errors */
	LinkedList<FileUploadInfo> filesNotAdded = new LinkedList<FileUploadInfo>();

	/** Files not added due to illegal file names */
	LinkedList<FileUploadInfo> illegalFileNames = new LinkedList<FileUploadInfo>();

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
		LinkedList<PersonalFile> addedFiles = new LinkedList<PersonalFile>();
		
		if( file != null)
		{
			Repository repository = 
				repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
			
			for( int index = 0; index < file.length; index++)
			{
				String theFileName = userFileName[index];
				PersonalFile pf = null;
				if(userFileName[index] == null || userFileName[index].trim().equals(""))
				{
					theFileName = FilenameUtils.getName(fileFileName[index]);
				}
				
				FileUploadInfo fileUploadInfo = new FileUploadInfo(theFileName, userFileName[index],
						userFileDescription[index]);
				
				// make sure we have a name if not do not upload!
				if( theFileName != null && !theFileName.trim().equals(""))
				{
				
				    if( file[index].length() > 0)
				    {
					    log.debug( "Creating non EMPTY file " + fileFileName[index]);
					    try {
							pf = createNonEmptyFile( repository,
									file[index],
									user, 
									theFileName,
									userFileDescription[index],
									fileFileName[index]);
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
							    theFileName,
							    userFileDescription[index],
							    fileFileName[index]);
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
					    			 theFileName,
									 userFileDescription[index],
									 fileFileName[index]);
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
			// index the added files
			indexFilesForUser(user, repository, addedFiles);
		}
		
		if( this.getAllFilesAdded() )
		{
		    return SUCCESS;
		}
		else
		{
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

	public String[] getUserFileName() {
		return userFileName;
	}

	public void setUserFileName(String[] userFileName) {
		this.userFileName = userFileName;
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

	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}

	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}

	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
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
			String aUserFileName, 
			String description, 
			String originalFileName) throws DuplicateNameException, IllegalFileSystemNameException
	{
		PersonalFile pf = null;
		if( personalFolder != null)
		{
		    pf = userFileSystemService.addFileToUser(repository, 
			    file, 
			    personalFolder,
			    aUserFileName,
			    description,
			    originalFileName);
		}
		else
		{
			pf = userFileSystemService.addFileToUser(repository, 
				file, 
				user,
				aUserFileName,
				description,
				originalFileName);
		}
		
		IrFile irFile = pf.getVersionedFile().getCurrentVersion().getIrFile();
		FileInfo fileInfo = irFile.getFileInfo();
		String extension = fileInfo.getExtension();
		
		log.debug("Extension = " + extension  + " can thumbnail = " +
		defaultThumbnailTransformer.canTransform(extension));
		
		// see if the file can be converted to a thumb nail.
		if(defaultThumbnailTransformer.canTransform(extension))
		{
			try
			{
			    File tempFile = temporaryFileCreator.createTemporaryFile(extension);
			    defaultThumbnailTransformer.transformFile(file, extension, tempFile);
			    TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode("PRIMARY_THUMBNAIL");
			    repositoryService.addTransformedFile(repository, 
			    		irFile, 
			    		tempFile, 
			    		"JPEG file", 
			    		defaultThumbnailTransformer.getFileExtension(), 
			    		transformedFileType);
			}
			catch(Exception e)
			{
			    log.error("Could not create thumbnail", e);
			}
		}
		return pf;
		
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
	
	/**
	 * Index the personal file information.
	 * 
	 * @param user
	 * @param repository
	 * @param personalFiles
	 */
	private void indexFilesForUser(IrUser user, Repository repository, 
			LinkedList<PersonalFile> personalFiles)
	{
		if( log.isDebugEnabled())
		{
		    log.debug("indexing new files for user");
		    log.debug("personalFiles size = " + personalFiles.size());
		}
		
		for( PersonalFile pf : personalFiles)
		{
			log.debug("adding file " + pf);
				userWorkspaceIndexService.addToIndex(repository, pf);
			
		}
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

}
