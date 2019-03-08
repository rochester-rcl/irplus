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


package edu.ur.ir.web.action;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;

/**
 * Generates HTML for the next picture to be displayed.  This does
 * not pass the file for download only information to allow the file
 * to be downloaded.
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class NextResearcherPicture extends ActionSupport {
	
	/** Eclipse generated Id */
	private static final long serialVersionUID = 4772718072456323110L;
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(NextResearcherPicture.class);
	
	/** Service for dealing with researcher */
	private ResearcherService researcherService;
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current picture location */
	private int currentResearcherLocation;
	
	/** helper for dealing with researcher information */
	private RandomResearcherPictureHelper researcherPictureHelper = new RandomResearcherPictureHelper();
	
	/** researchers found */
	private List<Researcher> researchers = new LinkedList<Researcher>();
	
	/** total number of researchers in the system */
	private Long researcherCount;

	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next researcher Picture");
    	}
    	
    	// get the first set of researchers
        researchers = researcherPictureHelper.getResearchers(type, researcherService, currentResearcherLocation);
        currentResearcherLocation = researcherPictureHelper.getCurrentResearcherLocation();
        researcherCount = researcherService.getPublicResearcherCount();
        return SUCCESS;
    }
   

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrentResearcherLocation() {
		return currentResearcherLocation;
	}

	public void setCurrentResearcherLocation(int currentLocation) {
		this.currentResearcherLocation = currentLocation;
	}

	public ResearcherService getResearcherService() {
		return researcherService;
	}

	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

	public List<Researcher> getResearchers() {
		return researchers;
	}

	public void setResearchers(List<Researcher> researchers) {
		this.researchers = researchers;
	}

	public int  getResearchersCount() {
		return researchers.size();
	}
	
	public Long getResearcherCount() {
		return researcherCount;
	}

}
