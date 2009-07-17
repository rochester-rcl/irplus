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

package edu.ur.ir.web.action.repository;

import java.io.File;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.IllegalFileSystemNameException;
import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * This allows a user to upload an image for the repository.
 * 
 * @author Nathan Sarr
 *
 */
public class AddRepositoryPicture extends ActionSupport implements UserIdAware{

	
	/** Eclipse generated id */
	private static final long serialVersionUID = 8373415416864866677L;
	
	/** User trying to upload the file */
	private Long userId;

	/** Class to create thumbnails. */
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Repository service for placing information in the repository */
	private RepositoryService repositoryService;
	
	/** actual set of files uploaded */
	private File file;
	
	/**  File name uploaded from the file system */
	private String fileFileName;
	
	/** description of the file  */
	private String userFileDescription;
	
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(AddRepositoryPicture.class);
	
	/**
	 * Uploads a new image to the system.
	 * 
	 * @return
	 * @throws IllegalFileSystemNameException 
	 */
	public String addNewPicture() throws IllegalFileSystemNameException
	{
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		IrFile irFile = repositoryService.addRepositoryPicture(repository, 
				file, fileFileName, userFileDescription);
		
		FileInfo fileInfo = irFile.getFileInfo();
		String extension = fileInfo.getExtension();
		
	    if(defaultThumbnailTransformer.canTransform(extension))
	    {
		    try
		    {
		        File tempFile = temporaryFileCreator.createTemporaryFile(extension);
		        defaultThumbnailTransformer.transformFile(file, extension, tempFile);
		        
		        if( tempFile != null && tempFile.exists() && tempFile.length() != 0l)
		        {
		            TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode("PRIMARY_THUMBNAIL");
		    
		            repositoryService.addTransformedFile(repository, 
		    		    irFile, 
		    		    tempFile, 
		    		    "JPEG file", 
		    		    defaultThumbnailTransformer.getFileExtension(), 
		    		    transformedFileType);
		        }
		        else
		        {
		        	log.error("could not create thumbnail for file " + fileInfo);
		        }
		    }
		    catch(Exception e)
		    {
			    log.error("Could not create thumbnail", e);
		    }
	    }
	    
	    return SUCCESS;
	}
	
	
	/**
	 * Get the user id uploading the image.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user id uploading the image.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Transformer for the thumbnail.
	 * 
	 * @return the Jpeg thumbnail transformer
	 */
	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}

	/**
	 * Set the jpeg thumbnail transformer.
	 * 
	 * @param defaultThumbnailTransformer
	 */
	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}

	/**
	 * This allows a temporary file to be created.
	 * 
	 * @return
	 */
	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	/**
	 * Set the temporary file creator.
	 * 
	 * @param temporaryFileCreator
	 */
	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}

	/**
	 * Repository service for adding files.
	 * 
	 * @return
	 */
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	/**
	 * Set the repository service 
	 * 
	 * @param repositoryService
	 */
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	/**
	 * Get the file to be added.
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set the file to be added.
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Get the file name uploaded by the user.
	 * 
	 * @return
	 */
	public String getFileFileName() {
		return fileFileName;
	}

	/**
	 * Set the file name uploaded by the user.
	 * 
	 * @param fileFileName
	 */
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	/**
	 * Description uploaded by the user.
	 * 
	 * @return
	 */
	public String getUserFileDescription() {
		return userFileDescription;
	}

	/**
	 * Set the user file description.
	 * 
	 * @param userFileDescription
	 */
	public void setUserFileDescription(String userFileDescription) {
		this.userFileDescription = userFileDescription;
	}

}
