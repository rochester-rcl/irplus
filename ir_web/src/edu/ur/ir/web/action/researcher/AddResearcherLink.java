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

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherService;

/**
 * Action to add a link to the researcher page.
 * 
 * @author Sharmila Ranganarhan
 *
 */
public class AddResearcherLink extends ActionSupport {
	

	/** User file system service. */
	private ResearcherService researcherService;
	
	/** the name of the folder to add */
	private String linkName;
	
	/** Description of the folder */
	private String linkDescription;
	
	/** URL */
	private String linkUrl;
	
	/** Current folder the user is looking at  */
	private Long parentFolderId;
	
	/** Id of the folder to update for updating  */
	private Long updateLinkId;
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 1355765084143781189L;
	
	/**  Logger for add researcher folder action */
	private static final Logger log = Logger.getLogger(AddResearcherLink.class);
	
	/**  Researcher object */
	private Long researcherId;

	/**  Indicates the folder has been added*/
	private boolean added = false;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/**
	 * Create the new link
	 */
	public String save()throws Exception
	{
		log.debug("creating a researcher link in parent folderId = " + parentFolderId);
		added = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			added = addRootLink();
		}
		else
		{
			added = addLinkToFolder();
		}
		
		if (!added) {
			message = getText("researcherLinkNameAlreadyExists", new String[]{linkName});
		}

        return SUCCESS;
	}
	
	/**
	 * Update a link with the given information.
	 * 
	 * @return success if the link is updated.
	 * 
	 */
	public String updateLink()
	{
		log.debug("updating a researcher folder parent folderId = " + parentFolderId);
		added = false;

		Researcher researcher = researcherService.getResearcher(researcherId, false);
		 
		ResearcherLink other = null;
		
		// check the name.  This makes sure that 
		// if the name has been changed, it does not conflict
		// with a folder already in the folder system.
		if( parentFolderId == null || parentFolderId == 0)
		{
			other = researcher.getRootLink(linkName);
		}
		else
		{
			other = researcherService.getResearcherLink(linkName, parentFolderId);
		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			ResearcherLink existingLink = researcherService.getResearcherLink(updateLinkId, true);
			existingLink.setName(linkName);
			existingLink.setDescription(linkDescription);
			existingLink.setLink(linkUrl);
			researcherService.saveResearcherLink(existingLink);
			added = true;
		}
		// name has not been changed
		else if(other.getId().equals(updateLinkId))
		{
			other.setDescription(linkDescription);
			other.setLink(linkUrl);
			researcherService.saveResearcherLink(other);
			added = true;
		}

		if( !added) 
		{
			message = getText("researcherLinkAlreadyExists", new String[]{linkName});
		}
		
			
	    return SUCCESS;
		
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
	 * Creates a new root link
	 *  
	 *  
	 */
	private boolean addRootLink()  
	{
		 boolean added = false;
		 
		 Researcher researcher = researcherService.getResearcher(researcherId, false);

		 if( researcher.getRootLink(linkName) == null )
	     {
			 ResearcherLink researcherLink = researcher.createRootLink(linkUrl, linkName, linkDescription);
			 researcherService.saveResearcherLink(researcherLink);
			 added = true;
         } 
		 return added;
	}
	
	/**
	 * Adds a link to an existing folder
	 * @throws DuplicateNameException 
	 * 
	 *  
	 */
	private boolean addLinkToFolder() throws DuplicateNameException 
	{
		boolean added = false;
		
		ResearcherFolder folder = researcherService.getResearcherFolder(parentFolderId, true);
		
		ResearcherLink researcherLink = folder.getResearcherLink(linkName);
		
		if( researcherLink == null )
	    {
		    researcherLink = folder.createLink(linkName, linkUrl, linkDescription);
		    researcherService.saveResearcherFolder(folder);
		    added = true;

		}
		return added;
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
		return linkDescription;
	}

	/**
	 * Description of the folder.
	 * 
	 * @param linkDescription
	 */
	public void setFolderDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}

	public Long getUpdateLinkId() {
		return updateLinkId;
	}

	public void setUpdateLinkId(Long updateLinkId) {
		this.updateLinkId = updateLinkId;
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

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkDescription() {
		return linkDescription;
	}

	public void setLinkDescription(String linkDescription) {
		this.linkDescription = linkDescription;
	}
}
