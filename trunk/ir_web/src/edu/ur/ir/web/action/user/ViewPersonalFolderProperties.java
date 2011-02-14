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

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Loads a folder for viewing.  This also loads the folder path for
 * the personal folder.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ViewPersonalFolderProperties extends ActionSupport implements UserIdAware {
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -3200848236612485328L;	

	/** Id of the folder */
	private Long personalFolderId;

	/** Personal folder the user wishes to look at */
	private PersonalFolder personalFolder;
	
	private UserFileSystemService userFileSystemService;
	
    /** set of folders that are the path for the current folder */
    private Collection <PersonalFolder> folderPath;
    
    /** Id of user */
    private Long userId;
    
    /** User service */
    private UserService userService;
    
    /** Size of folder in bytes */
    private Long folderSize;
    
    /** No. of files in folder */
    private int filesCount;
    
	
	public String execute()
	{
	    personalFolder = userFileSystemService.getPersonalFolder(personalFolderId, false);
	    
	    if( !personalFolder.getOwner().getId().equals(userId))
	    {
	    	return "accessDenied";
	    }
	    
	    PersonalFolder parentFolder = personalFolder.getParent();
	    if( parentFolder != null )
	    {
	    	folderPath = userFileSystemService.getPersonalFolderPath(parentFolder.getId());
	    }
	    
	    IrUser user = userService.getUser(userId, false);
	    
	    folderSize = userFileSystemService.getFolderSize(user, personalFolder);
	    
	    filesCount = personalFolder.getFiles().size();
	    
		return SUCCESS;
	}

	public Long getPersonalFolderId() {
		return personalFolderId;
	}

	public void setPersonalFolderId(Long folderId) {
		this.personalFolderId = folderId;
	}

	public PersonalFolder getPersonalFolder() {
		return personalFolder;
	}

	public void setPersonalFolder(PersonalFolder personalFolder) {
		this.personalFolder = personalFolder;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}

	
	public Collection<PersonalFolder> getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(Collection<PersonalFolder> folderPath) {
		this.folderPath = folderPath;
	}

	public Long getUserId() {
		return userId;
	}

	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getFolderSize() {
		return folderSize;
	}

	public int getFilesCount() {
		return filesCount;
	}
}
