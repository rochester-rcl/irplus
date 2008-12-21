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
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.user.PersonalFile;
import edu.ur.ir.user.PersonalFolder;
import edu.ur.ir.user.UserFileSystemService;

/**
 * Loads a file for viewing.  This also loads the folder path for
 * the personal file.
 * 
 * @author Nathan Sarr
 *
 */
public class ViewPersonalFile extends ActionSupport implements Preparable{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = -897765911300935590L;

	/** Id of the file */
	private Long personalFileId;

	/** Personal file the user wishes to look at */
	private PersonalFile personalFile;
	
	private UserFileSystemService userFileSystemService;
	
    /** set of folders that are the path for the current file */
    private Collection <PersonalFolder> folderPath;
	
	public Collection<PersonalFolder> getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(Collection<PersonalFolder> folderPath) {
		this.folderPath = folderPath;
	}

	public void prepare() throws Exception {
	    personalFile = userFileSystemService.getPersonalFile(personalFileId, false);
	    PersonalFolder parentFolder = personalFile.getPersonalFolder();
	    if( parentFolder != null )
	    {
	    	folderPath = userFileSystemService.getPersonalFolderPath(parentFolder.getId());
	    }
	}
	
	public String execute()
	{
		return SUCCESS;
	}

	public Long getPersonalFileId() {
		return personalFileId;
	}

	public void setPersonalFileId(Long fileId) {
		this.personalFileId = fileId;
	}

	public PersonalFile getPersonalFile() {
		return personalFile;
	}

	public void setPersonalFile(PersonalFile personalFile) {
		this.personalFile = personalFile;
	}

	public UserFileSystemService getUserFileSystemService() {
		return userFileSystemService;
	}

	public void setUserFileSystemService(UserFileSystemService userFileSystemService) {
		this.userFileSystemService = userFileSystemService;
	}
}
