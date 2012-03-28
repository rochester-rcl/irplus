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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Action to display researcher page
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PublicResearcherPageView extends ActionSupport implements
Comparator<IrFile>, UserIdAware {

	//  Eclipse generated id 
	private static final long serialVersionUID = 5154383076462883720L;

	//  Logger
	private static final Logger log = Logger.getLogger(PublicResearcherPageView.class);
	
	// determine what the user is trying to do 
	
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	// Researcher 
	private Researcher researcher;
	
	// Id of researcher 
	private Long researcherId;
	
	// Service class for researcher 
	private ResearcherService researcherService;
	
	// Number of researcher pictures 
	private int numResearcherPictures;
	
	// Researcher picture IrFile 
	private IrFile irFile;
	
	// Represents the location of current picture displayed */
	private int currentLocation;
	
	// Type represents NEXT, PREVIOUS, INIT 
	private String type;
	
	// id of the user trying to access the data 
	private Long userId;
	
	/**
	 * View the researcher page.
	 * 
	 * @return
	 */
	public String view()
	{
		log.debug("View Researcher page:researcherId="+ researcherId);
		
		if( researcherId != null && researcherId > 0)
		{	
		    researcher = researcherService.getResearcher(researcherId, false);
		}

		if (researcher != null) {
			// if the researcher page is not public and the user accessing the file
			// is not the owner
		 	if( !researcher.isPublic() && !researcher.getUser().getId().equals(userId))
	    	{
	    		return "accessDenied";
	    	}
		}
		else
		{
			return "notFound";
		}

		
		return SUCCESS;
	}

	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String getPictures() {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next researcher Picture");
    	}
    	

    	researcher = researcherService.getResearcher(researcherId, false);
    	if( !researcher.isPublic() && !researcher.getUser().getId().equals(userId))
    	{
    		return "accessDenied";
    	}
	    
    	LinkedList<IrFile> pictures = new LinkedList<IrFile>();
	    
	    pictures.addAll(researcher.getPictures());
	    if (researcher.getPrimaryPicture() != null) {
	    	pictures.add(researcher.getPrimaryPicture());
	    }
	    numResearcherPictures = pictures.size();
        // sort the pictures to assure order
        Collections.sort(pictures, this);

        if( pictures != null && pictures.size() > 0 )
        {
    	    if( pictures.size() == 1 )
    	    {
    	    	currentLocation = 0;
    	    }
    	    else if ( type.equals(INIT))
    	    {
    	    	if (researcher.getPrimaryPicture() != null) {
    	    		currentLocation = pictures.indexOf(researcher.getPrimaryPicture());
    	    	}
    	
    	    	if (currentLocation < 0) {
    		    	currentLocation = 0;
    		    }
    		    
    	    }
       	    else if( type.equals(NEXT))
    	    {
    		    if( (currentLocation + 1) >= pictures.size())
    		    {
    			    currentLocation = 0;
    		    }
    		    else
    		    {
    			    currentLocation += 1;
    		    }
    	    }
    	    else if( type.equals(PREV))
    	    {
    		    if( (currentLocation -1 ) < 0 )
    		    {
    			    currentLocation = pictures.size() - 1;
    		    }
    		    else
    		    {
    			    currentLocation -= 1;
    		    }
    	    }
    	    irFile = pictures.get(currentLocation);
        }
        return SUCCESS;
    }
    
    
    /**
     * Simple comparison to assure order.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(IrFile o1, IrFile o2) {
    	if( o1.getId().equals(o2.getId())) return 0;
    	else if( o1.getId() > o2.getId() ) return 1;
    	else  return -1;
    		
    }
	/**
	 * Get researcher 
	 * 
	 * @return
	 */
	public Researcher getResearcher() {
		return researcher;
	}

	/**
	 * Get researcher id
	 * 
	 * @return
	 */
	public Long getResearcherId() {
		return researcherId;
	}

	/**
	 * Set researcher id
	 * 
	 * @param researcherId
	 */
	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}

	/**
	 * Set researcher service
	 * 
	 * @param researcherService
	 */
	public void setResearcherService(ResearcherService researcherService) {
		this.researcherService = researcherService;
	}


	/**
	 * Get the ir file 
	 * @return
	 */
	public IrFile getIrFile() {
		return irFile;
	}

	/**
	 * Get the current location in the list.
	 * @return
	 */
	public int getCurrentLocation() {
		return currentLocation;
	}

	/**
	 * Set the current location
	 * 
	 * @param currentLocation
	 */
	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
	}

	/**
	 * Get the type of action NEXT, PREVIOUS, INIT
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of action NEXT, PREVIOUS, INIT
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Set the user id.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#injectUserId(java.lang.Long)
	 */
	public void injectUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Get the number of researcher pictures.
	 * 
	 * @return
	 */
	public int getNumResearcherPictures() {
		return numResearcherPictures;
	}


}
