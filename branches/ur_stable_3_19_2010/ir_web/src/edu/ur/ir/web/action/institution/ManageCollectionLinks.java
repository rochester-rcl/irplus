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

package edu.ur.ir.web.action.institution;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionLink;
import edu.ur.ir.institution.InstitutionalCollectionService;

/**
 * Allows a user to mananage collection links
 * 
 * @author Nathan Sarr
 *
 */
public class ManageCollectionLinks extends ActionSupport {

	/** Eclipse generated id */
	private static final long serialVersionUID = -6528357591112809031L;
	
	/** Id for the collection to add the link to  */
	private Long collectionId;
	
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
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** collection  loaded to add the link to */
	private InstitutionalCollection collection;
	
	/**  link created or loaded */
	private InstitutionalCollectionLink link; 
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(ManageCollectionLinks.class);
	
	
	/**
	 * Create a new collection link.
	 * 
	 * @return
	 */
	public String create()
	{
		collection = institutionalCollectionService.getCollection(collectionId, false);
		log.debug("create link called for collection " + collection);
		try
		{
		    link = collection.addLink(linkName, linkUrl);
		    institutionalCollectionService.saveCollection(collection);
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
		
		collection = institutionalCollectionService.getCollection(collectionId, false);
		InstitutionalCollectionLink link = collection.getLink(linkId);
		
		log.debug("updateing link " + link);
		
		if( link != null)
		{
			InstitutionalCollectionLink other = collection.getLink(linkName);
			if( other == null || other.getId().equals(link.getId()))
			{
				link.setName(linkName);
				link.setDescription(linkDescription);
				link.setUrl(linkUrl);
				institutionalCollectionService.saveCollection(collection);
				linkAdded = true;
			}
			else
			{
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
		collection = institutionalCollectionService.getCollection(collectionId, false);
		InstitutionalCollectionLink link = collection.getLink(linkId);
		
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
		collection = institutionalCollectionService.getCollection(collectionId, false);
		link = collection.getLink(linkId);
		collection.moveLink(link, link.getOrder() - 1);
		institutionalCollectionService.saveCollection(collection);
		return SUCCESS;
	}
	
	/**
	 * Move the link down.
	 * 
	 * @return
	 */
	public String moveLinkDown()
	{
		collection = institutionalCollectionService.getCollection(collectionId, false);
		link = collection.getLink(linkId);
		collection.moveLink(link, link.getOrder() + 1);
		institutionalCollectionService.saveCollection(collection);
		return SUCCESS;
	}
	
	/**
	 * Delete a link from the collection
	 * 
	 * @return
	 */
	public String delete()
	{
		collection = institutionalCollectionService.getCollection(collectionId, false);
        collection.removLink(collection.getLink(linkId));
        institutionalCollectionService.saveCollection(collection);
		return SUCCESS;
	}
	
	
	public String view()
	{
		collection = institutionalCollectionService.getCollection(collectionId, false);
		return SUCCESS;
	}

	
	public Long getCollectionId() {
		return collectionId;
	}


	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
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


	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService insitutionalCollectionService) {
		this.institutionalCollectionService = insitutionalCollectionService;
	}


	public InstitutionalCollection getCollection() {
		return collection;
	}


	public void setCollection(InstitutionalCollection collection) {
		this.collection = collection;
	}

	public boolean isLinkAdded() {
		return linkAdded;
	}

	public void setLinkAdded(boolean linkAdded) {
		this.linkAdded = linkAdded;
	}

	public InstitutionalCollectionLink getLink() {
		return link;
	}

	public void setLink(InstitutionalCollectionLink link) {
		this.link = link;
	}

	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}

}
