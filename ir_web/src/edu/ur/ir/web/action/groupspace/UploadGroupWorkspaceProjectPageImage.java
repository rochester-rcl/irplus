/**  
   Copyright 2008 - 2012 University of Rochester

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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.groupspace.GroupWorkspace;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPage;
import edu.ur.ir.groupspace.GroupWorkspaceProjectPageImage;
import edu.ur.ir.groupspace.GroupWorkspaceService;
import edu.ur.ir.groupspace.GroupWorkspaceUser;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Uploads a group workspace project page image.
 * 
 * @author Nathan Sarr
 *
 */
public class UploadGroupWorkspaceProjectPageImage extends ActionSupport implements UserIdAware{

	// eclipse generated id
	private static final long serialVersionUID = -6524925469514461430L;

	// User trying to upload the file 
	private Long userId;
	
	//  Id of the group workspace to add the picture to */
	private Long groupWorkspaceId;

	// service to create thumbnails  */
	private ThumbnailTransformerService thumbnailTransformerService;
		
	// Repository service */
	private RepositoryService repositoryService;
	
	// file uploaded */
	private File file;
	
	//  File name uploaded from the file system */
	private String fileFileName;
		
	//  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(UploadGroupWorkspaceProjectPageImage.class);
	
	// Indicates the file has been added. */
	private boolean added = false;
	
	// group workspace service
	private GroupWorkspaceService groupWorkspaceService;
	
	// service to deal with user information
	private UserService userService;

	/**
	 * Uploads a new image to the system.
	 * 
	 * @return
	 */
	public String addNewPicture() throws Exception
	{
		if( log.isDebugEnabled())
		{
			log.debug("add new picture called");
		}
		
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		IrFile picture = null;
		GroupWorkspace groupWorkspace = groupWorkspaceService.get(groupWorkspaceId, false);
		IrUser user = userService.getUser(userId, false);
		GroupWorkspaceUser groupWorkspaceUser = groupWorkspace.getUser(user);
		
		// only a group workspace owner can edit the project page
		if( groupWorkspaceUser == null || !groupWorkspaceUser.isOwner())
		{
			return "accessDenied";
		}
		
		picture = repositoryService.createIrFile(repository, file, fileFileName, "group workspace project page picture workspace id = " 
				+ groupWorkspace.getId());
		
		
		GroupWorkspaceProjectPage groupWorkspaceProjectPage = groupWorkspace.getGroupWorkspaceProjectPage();
		GroupWorkspaceProjectPageImage image = groupWorkspaceProjectPage.addImage(picture);
	    if( image != null )
	    {
		    added = true;
	    }
		
		if( !added)
		{
			String message = getText("groupWorkspaceProjectPageImageUploadError", 
					new String[]{fileFileName});
			addFieldError("groupWorkspaceProjectPageImageUploadError", message);
		}
		else
		{
			groupWorkspaceService.save(groupWorkspace);
		}
		
		thumbnailTransformerService.transformFile(repository, picture);
	    
	    log.debug("returning success");
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
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Repository service 
	 * 
	 * @return the repository service
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
	 * Id of the group workspace to add the image to.
	 * 
	 * @return
	 */
	public Long getGroupWorkspaceId() {
		return groupWorkspaceId;
	}

	/**
	 * Id of the group workspace to add the image to.
	 * 
	 * @param groupWorkspaceId
	 */
	public void setGroupWorkspaceId(Long groupWorkspaceId) {
		this.groupWorkspaceId = groupWorkspaceId;
	}

	/**
	 * True if the image is added.
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
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
	 * Set the thumbnail transformer service.
	 * 
	 * @param thumbnailTransformerService
	 */
	public void setThumbnailTransformerService(
			ThumbnailTransformerService thumbnailTransformerService) {
		this.thumbnailTransformerService = thumbnailTransformerService;
	}
	
	/**
	 * Set the user service.
	 * 
	 * @param userService
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
