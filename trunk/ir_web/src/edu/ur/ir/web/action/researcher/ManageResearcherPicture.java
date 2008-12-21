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
import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;

/**
 * Manage the researcher pictures.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageResearcherPicture extends ActionSupport implements Preparable {

	/** Eclipse generated id */
	private static final long serialVersionUID = 3980266197680971615L;
	
	/**  Logger. */
	private static final Logger log = Logger.getLogger(ManageResearcherPicture.class);

	/** Repository service */
	private ResearcherService researcherService;
	
	/** Researcher */
	private Researcher researcher;
	
	/** Id for the researcher to load */
	private Long researcherId;
	
	/** determine if the primary picture should be removed */
	private boolean primaryResearcherPicture;
	
	/** picture to remove*/
	private Long pictureId;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	
	
	/**
	 * Load the news service.
	 * 
	 * @see com.opensymphony.xwork2.Preparable#prepare()
	 */
	public void prepare() throws Exception {
		researcher = researcherService.getResearcher(researcherId, false);
	}
	
	/**
	 * Delete a picture
	 */
	public String delete()
	{
		if( log.isDebugEnabled())
		{
		    log.debug("execute delete");
		}
		
		if( primaryResearcherPicture)
		{
			if( log.isDebugEnabled())
			{
			   log.debug("delete primary picture");
			}
			IrFile primaryPicture = researcher.getPrimaryPicture();
			researcher.setPrimaryPicture(null);
			repositoryService.deleteIrFile(primaryPicture);
		}
		else
		{
			if( log.isDebugEnabled())
			{
			   log.debug("delete regular picture");
			}
			IrFile picture = repositoryService.getIrFile(pictureId, false);
			if(researcher.removePicture(picture) )
			{
				repositoryService.deleteIrFile(picture);
			}
		}
		
		researcherService.saveResearcher(researcher);
		return SUCCESS;
	}

	/**
	 * Set primary picture for researcher
	 * 
	 */
	public String setDefaultPicture()
	{

		if( log.isDebugEnabled())
		{
		    log.debug("setDefaultPicture");
		}
		
		// If there is a primary picturee, move it to collection of pictures 
		IrFile primaryPicture = researcher.getPrimaryPicture();
		if (primaryPicture != null) {
			researcher.addPicture(primaryPicture);
		}
		
		// Set the primary picture
		IrFile picture = repositoryService.getIrFile(pictureId, false);
		researcher.removePicture(picture);
		researcher.setPrimaryPicture(picture);
		
		researcherService.saveResearcher(researcher);
		
		return SUCCESS;
	}
	
	/**
	 * Set to true if the picture to be deleted is the 
	 * primary picture.
	 * 
	 * @return
	 */
	public boolean isPrimaryResearcherPicture() {
		return primaryResearcherPicture;
	}

	/**
	 * Set to true if the picture to be deleted is the primary picture.
	 * 
	 * @param primaryNewsPicture
	 */
	public void setPrimaryResearcherPicture(boolean primaryResearcherPicture) {
		this.primaryResearcherPicture = primaryResearcherPicture;
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

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(
			Researcher researcher) {
		this.researcher = researcher;
	}

	public Long getResearcherId() {
		return researcherId;
	}

	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}
	
	public int getNumberOfResearcherPictures() {
		return researcher.getPictures().size();
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(
			ResearcherService researcherService) {
		this.researcherService = researcherService;
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
}
