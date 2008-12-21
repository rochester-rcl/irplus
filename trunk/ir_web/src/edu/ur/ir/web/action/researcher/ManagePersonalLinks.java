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
import edu.ur.ir.researcher.ResearcherPersonalLink;
import edu.ur.ir.researcher.ResearcherService;

/**
 * Class to manage personal links for a researcher.
 * 
 * @author Nathan Sarr
 *
 */
public class ManagePersonalLinks extends ActionSupport {

	
	/** Eclipse generated id */
	private static final long serialVersionUID = 5753317145744387553L;

	/** Id for the collection to add the link to  */
	private Long researcherId;
	
	/** id of the link */
	private Long linkId;
	
	/** URL for the link */
	private String linkUrl;
	
	/** Name for the link */
	private String linkName;
	
	/** Determine if the link was added */
	private boolean linkAdded = false;
	
	/** description of the link */
	private String linkDescription;
	
	/** Service for dealing with institutional collection information   */
	private ResearcherService researcherService;
	
	/** researcher loaded to add the link to */
	private Researcher researcher;
	
	/**  link created or loaded */
	private ResearcherPersonalLink link; 
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(ManagePersonalLinks.class);
	
	
	/**
	 * Create a new collection link.
	 * 
	 * @return
	 */
	public String create()
	{
		researcher = researcherService.getResearcher(researcherId, false);
		log.debug("create link called for collection " + researcher);
		try
		{
		    link = researcher.addPersonalLink(linkName, linkUrl);
		    researcherService.saveResearcher(researcher);
		    linkAdded = true;
		}
		catch(DuplicateNameException dne)
		{
			String linkMessage = getText("linkNameAlreadyExists", new String[]{linkName});
			addFieldError("linkNameAlreadyExists", linkMessage);
		}
		return SUCCESS;
	}
	
	/**
	 * @return
	 */
	public String update()
	{
		
		researcher = researcherService.getResearcher(researcherId, false);
		ResearcherPersonalLink link = researcher.getPersonalLink(linkId);
		
		log.debug("updateing link " + link);
		
		if( link != null)
		{
			try {
				researcher.renamePersonalLink(link.getName(), linkName);
			    link.setDescription(linkDescription);
			    link.setUrl(linkUrl);
			    researcherService.saveResearcher(researcher);
				linkAdded = true;
			} catch (DuplicateNameException e) {
				String linkMessage = getText("linkNameAlreadyExists", new String[]{linkName});
				addFieldError("linkNameAlreadyExists", linkMessage);
			}
		}
	
		return SUCCESS;
	}
	
	/**
	 * Allow the link information to be viewed.
	 * 
	 * @return
	 */
	public String viewLink()
	{
		researcher =  researcherService.getResearcher(researcherId, false);
		ResearcherPersonalLink link = researcher.getPersonalLink(linkId);
		
		if( link != null)
		{
		    linkName = link.getName();
		    linkDescription = link.getDescription();
		    linkId = link.getId();
		    linkUrl = link.getUrl();
		}
		return SUCCESS;
	}
	
	/**
	 * Move the specified link up 1 position.
	 * 
	 * @return
	 */
	public String moveLinkUp()
	{
		researcher =  researcherService.getResearcher(researcherId, false);
		link = researcher.getPersonalLink(linkName);
		researcher.movePersonalLink(link, link.getOrder() - 1);
		researcherService.saveResearcher(researcher);
		return SUCCESS;
	}
	
	/**
	 * Move the link down.
	 * 
	 * @return
	 */
	public String moveLinkDown()
	{
		researcher =  researcherService.getResearcher(researcherId, false);
		link = researcher.getPersonalLink(linkName);
		researcher.movePersonalLink(link, link.getOrder() + 1);
		researcherService.saveResearcher(researcher);
		return SUCCESS;
	}
	
	/**
	 * Delete a link from the collection
	 * 
	 * @return
	 */
	public String delete()
	{
		researcher =  researcherService.getResearcher(researcherId, false);
        researcher.removPersonalLink(researcher.getPersonalLink(linkName));
        researcherService.saveResearcher(researcher);
		return SUCCESS;
	}
	
	
	public String view()
	{
		researcher = researcherService.getResearcher(researcherId, false);
		return SUCCESS;
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


	public boolean isLinkAdded() {
		return linkAdded;
	}

	public void setLinkAdded(boolean linkAdded) {
		this.linkAdded = linkAdded;
	}


	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public ResearcherPersonalLink getLink() {
		return link;
	}

	public void setLink(ResearcherPersonalLink link) {
		this.link = link;
	}


}
