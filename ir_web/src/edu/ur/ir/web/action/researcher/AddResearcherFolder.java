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


package edu.ur.ir.web.action.researcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add a folder to the researcher page.
 * 
 * @author Sharmila Ranganarhan
 *
 */
public class AddResearcherFolder extends ActionSupport implements UserIdAware{
	
	/** Researcher service. */
	private ResearcherService researcherService;
	
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** the name of the folder to add */
	private String folderName;
	
	/** Description of the folder */
	private String folderDescription;
	
	/** Current folder the user is looking at  */
	private Long parentFolderId;
	
	/** Id of the folder to update for updating  */
	private Long updateFolderId;
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -6343965003122766186L;
	
	/**  Logger for add researcher folder action */
	private static final Logger log = LogManager.getLogger(AddResearcherFolder.class);
	
	/**  Researcher object */
	private Long researcherId;

	/**  Indicates the folder has been added*/
	private boolean added = false;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/** id of the user making changes */
	private Long userId;
	
	/**
	 * Create the new folder
	 */
	public String save()throws Exception
	{
		log.debug("creating a researcher folder parent folderId = " + parentFolderId);
		Researcher researcher = researcherService.getResearcher(researcherId, false);
		
		if( !researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		
		added = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			 if( researcher.getRootFolder(folderName) == null )
		     {
				 ResearcherFolder researcherFolder = null;
				 try {
					researcherFolder = researcher.createRootFolder(folderName);
					researcherFolder.setDescription(folderDescription);
					researcherFileSystemService.saveResearcherFolder(researcherFolder);
					added = true;
				 } catch (DuplicateNameException e) {
					throw new RuntimeException("Fix this save error");
				 }
	         }
		}
		else
		{
			ResearcherFolder folder = researcherFileSystemService.getResearcherFolder(parentFolderId, true);
			if( !folder.getResearcher().getUser().getId().equals(userId))
			{
				return "accessDenied";
			}
		
			try
			{
			    ResearcherFolder researcherFolder = folder.createChild(folderName);
			    researcherFolder.setDescription(folderDescription);
			    researcherFileSystemService.saveResearcherFolder(folder);
			    added = true;
			}
			catch(DuplicateNameException e)
			{
				added = false;
			}
			
		}
		
		if( !added)
		{
			message = getText("researcherFolderAlreadyExists", new String[]{folderName});
		}
        return SUCCESS;
	}
	
	/**
	 * Update a folder with the given information.
	 * 
	 * @return success if the folder is updated.
	 * @throws Exception
	 */
	public String updateFolder()throws Exception
	{
		
		log.debug("updating a researcher folder parent folderId = " + parentFolderId);
		added = false;

		ResearcherFolder other = null;
		
		// check the name.  This makes sure that 
		// if the name has been changed, it does not conflict
		// with a folder already in the folder system.
		if( parentFolderId == null || parentFolderId == 0)
		{
			other = researcherFileSystemService.getRootResearcherFolder(folderName, researcherId);
		}
		else
		{
			other = researcherFileSystemService.getResearcherFolder(folderName, parentFolderId);
		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			ResearcherFolder existingFolder = researcherFileSystemService.getResearcherFolder(updateFolderId, true);
			if( !existingFolder.getResearcher().getUser().getId().equals(userId))
			{
				return "accessDenied";
			}
			existingFolder.setName(folderName);
			existingFolder.setDescription(folderDescription);
			researcherFileSystemService.saveResearcherFolder(existingFolder);
			added = true;
		}
		// name has not been changed
		else if(other.getId().equals(updateFolderId))
		{
			if( !other.getResearcher().getUser().getId().equals(userId) )
			{
				return "accessDenied";
			}
			other.setDescription(folderDescription);
			researcherFileSystemService.saveResearcherFolder(other);
			added = true;
		}

		if( !added)
		{
			message = getText("researcherFolderAlreadyExists", new String[]{folderName});
		}
		
			
	    return SUCCESS;
		
	}

	/**
	 * Get the name of the folder to add.
	 * 
	 * @return
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * Set the name of the folder to add.
	 * 
	 * @param folderName
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * Current folder the user is looking at.
	 * 
	 * @return
	 */
	public Long getParentFolderId() {
		return parentFolderId;
	}

	/**
	 * The current folder the user is looking at.
	 * 
	 * @param currentFolderId
	 */
	public void setParentFolderId(Long currentFolderId) {
		this.parentFolderId = currentFolderId;
	}
	
	/**
	 * Indicates if the folder has been added 
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * Get the folder added message.
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Description of the folder.
	 * 
	 * @return
	 */
	public String getFolderDescription() {
		return folderDescription;
	}

	/**
	 * Description of the folder.
	 * 
	 * @param folderDescription
	 */
	public void setFolderDescription(String folderDescription) {
		this.folderDescription = folderDescription;
	}

	public Long getUpdateFolderId() {
		return updateFolderId;
	}

	public void setUpdateFolderId(Long updateFolderId) {
		this.updateFolderId = updateFolderId;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public ResearcherFileSystemService getResearcherFileSystemService() {
		return researcherFileSystemService;
	}

	public void setResearcherFileSystemService(
			ResearcherFileSystemService researcherFileSystemService) {
		this.researcherFileSystemService = researcherFileSystemService;
	}
}
