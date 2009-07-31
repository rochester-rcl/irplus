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
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
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

	/** service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;
		
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
		log.debug("add new picture");
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		IrFile irFile = repositoryService.addRepositoryPicture(repository, 
				file, fileFileName, userFileDescription);
		
		thumbnailTransformerService.transformFile(repository, irFile);
	    
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
	
	public ThumbnailTransformerService getThumbnailTransformerService() {
		return thumbnailTransformerService;
	}


	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}


}
