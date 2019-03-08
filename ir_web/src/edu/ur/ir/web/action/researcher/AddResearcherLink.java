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

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherFileSystemService;
import edu.ur.ir.researcher.ResearcherFolder;
import edu.ur.ir.researcher.ResearcherLink;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to add a link to the researcher page.
 * 
 * @author Sharmila Ranganarhan
 *
 */
public class AddResearcherLink extends ActionSupport implements UserIdAware{
	

	/** Researcher service. */
	private ResearcherService researcherService;
	
	/** Service for dealing with researcher file system information */
	private ResearcherFileSystemService researcherFileSystemService;
	
	/** the name of the folder to add */
	private String linkName;
	
	/** Description of the folder */
	private String linkDescription;
	
	/** id of the link  */
	private Long linkId;
	
	/** URL */
	private String linkUrl;
	
	/** Current folder the user is looking at  */
	private Long parentFolderId;
	
	/** loaded link */
	private ResearcherLink link;
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 1355765084143781189L;
	
	/**  Logger for add researcher folder action */
	private static final Logger log = LogManager.getLogger(AddResearcherLink.class);
	
	/**  Researcher object */
	private Long researcherId;

	/**  Indicates the folder has been added*/
	private boolean added = false;
	
	/** Message that can be displayed to the user. */
	private String message;
	
	/** id of the user making the changes */
	private Long userId;
	
	/**
	 * Get a link by it's id
	 * @return
	 */
	public String get()
	{
		Researcher researcher = null;
		link = researcherFileSystemService.getResearcherLink(linkId, false);
		if( link != null )
		{
		    researcher = link.getResearcher();
		}
		if( researcher == null || !researcher.getUser().getId().equals(userId) || !researcher.getUser().hasRole(IrRole.RESEARCHER_ROLE))
		{
			 return "accessDenied";
		}
		return SUCCESS;
	}
	
	/**
	 * Create the new link
	 */
	public String save()throws Exception
	{
		log.debug("creating a researcher link in parent folderId = " + parentFolderId);
		Researcher researcher = researcherService.getResearcher(researcherId, false);
		 
		if( !researcher.getUser().getId().equals(userId) || !researcher.getUser().hasRole(IrRole.RESEARCHER_ROLE))
		{
			 return "accessDenied";
		}
		added = false;
		// assume that if the current folder id is null or equal to 0
		// then we are adding a root folder to the user.
		if(parentFolderId == null || parentFolderId == 0)
		{
			 if( researcher.getRootLink(linkName) == null )
		     {
				 ResearcherLink researcherLink = researcher.createRootLink(linkUrl, linkName, linkDescription);
				 researcherFileSystemService.saveResearcherLink(researcherLink);
				 added = true;
	         } 
		}
		else
		{
			ResearcherFolder folder = researcherFileSystemService.getResearcherFolder(parentFolderId, true);
			
			if(!folder.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
			
			ResearcherLink researcherLink = folder.getResearcherLink(linkName);
			
			if( researcherLink == null )
		    {
			    researcherLink = folder.createLink(linkName, linkUrl, linkDescription);
			    researcherFileSystemService.saveResearcherFolder(folder);
			    added = true;

			}
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
		
		if(!researcher.getUser().getId().equals(userId) || !researcher.getUser().hasRole(IrRole.RESEARCHER_ROLE))
		{
			return "accessDenied";
		}
		 
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
			ResearcherFolder folder = researcherFileSystemService.getResearcherFolder(parentFolderId, false);
			other = folder.getResearcherLink(linkName);
			if( folder == null || !folder.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
		}
		
		// name has been changed and does not conflict
		if( other == null)
		{
			ResearcherLink existingLink = researcherFileSystemService.getResearcherLink(linkId, true);
			if( !existingLink.getResearcher().getId().equals(researcher.getId()))
			{
				return "accessDenied";
			}
			existingLink.setName(linkName);
			existingLink.setDescription(linkDescription);
			existingLink.setUrl(linkUrl);
			researcherFileSystemService.saveResearcherLink(existingLink);
			added = true;
		}
		// name has not been changed
		else if(other.getId().equals(linkId))
		{
			other.setDescription(linkDescription);
			other.setUrl(linkUrl);
			researcherFileSystemService.saveResearcherLink(other);
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

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public ResearcherLink getLink() {
		return link;
	}

	public void setLink(ResearcherLink link) {
		this.link = link;
	}
}
