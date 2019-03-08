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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.news.NewsItem;
import edu.ur.ir.repository.RepositoryService;

/**
 * Delete the institutional collection picture.
 * 
 * @author Nathan Sarr
 *
 */
public class DeleteInstitutionalCollectionPicture extends ActionSupport implements Preparable {

	/** Eclipse generated id */
	private static final long serialVersionUID = 3980266197680971615L;
	
	/**  Logger. */
	private static final Logger log = LogManager.getLogger(DeleteInstitutionalCollectionPicture.class);

	/** Repository service */
	private RepositoryService repositoryService;
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** Institutional collection */
	private InstitutionalCollection collection;
	
	/** News item  to remove the picture from*/
	private NewsItem newsItem;
	
	/** Id for the collection to load */
	private Long collectionId;
	
	/** determine if the primary picture should be removed */
	private boolean primaryCollectionPicture;
	
	/** picture to remove*/
	private Long pictureId;		
	
	/**
	 * Load the news service.
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		collection = institutionalCollectionService.getCollection(collectionId, false);
	}
	
	/**
	 * Execute the delete.
	 * 
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	public String execute()
	{
		if( log.isDebugEnabled())
		{
		    log.debug("execute delete");
		}
		
		if( primaryCollectionPicture)
		{
			if( log.isDebugEnabled())
			{
			   log.debug("delete primary picture");
			}
			IrFile primaryPicture = collection.getPrimaryPicture();
			collection.setPrimaryPicture(null);
			repositoryService.deleteIrFile(primaryPicture);
		}
		else
		{
			if( log.isDebugEnabled())
			{
			   log.debug("delete regular picture");
			}
			IrFile picture = repositoryService.getIrFile(pictureId, false);
			if(collection.removePicture(picture) )
			{
				repositoryService.deleteIrFile(picture);
			}
		}
		
		institutionalCollectionService.saveCollection(collection);
		return SUCCESS;
	}

	/**
	 * Get the news item to delete the picture from.
	 * 
	 * @return
	 */
	public NewsItem getNewsItem() {
		return newsItem;
	}

	/**
	 * Set the news item to delete the picture from.
	 * 
	 * @param newsItem
	 */
	public void setNewsItem(NewsItem newsItem) {
		this.newsItem = newsItem;
	}



	/**
	 * Set to true if the picture to be deleted is the 
	 * primary picture.
	 * 
	 * @return
	 */
	public boolean isPrimaryCollectionPicture() {
		return primaryCollectionPicture;
	}

	/**
	 * Set to true if the picture to be deleted is the primary picture.
	 * 
	 * @param primaryNewsPicture
	 */
	public void setPrimaryCollectionPicture(boolean primaryCollectionPicture) {
		this.primaryCollectionPicture = primaryCollectionPicture;
	}

	/**
	 * Get the picture id to be deleted.
	 * 
	 * @return
	 */
	public Long getPictureId() {
		return pictureId;
	}

	/**
	 * Set the picture id to be deleted.
	 * 
	 * @param pictureId
	 */
	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public InstitutionalCollection getCollection() {
		return collection;
	}


	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}
	

	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}

	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
