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
import java.util.Random;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.order.OrderType;

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
	
	/** determine what the user is trying to do */
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(NextResearcherPicture.class);
	
	/** Service for dealing with researcher */
	private ResearcherService researcherService;
	
	/**  Ir file that should be shown. */
	private List<Researcher> researchers = new LinkedList<Researcher>();
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**
	 * Current picture location
	 */
	private int currentLocation;
	
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
    	int researcherCount = researcherService.getPublicResearcherCount().intValue();
    	
    	log.debug("Getting researhcer picture researcher count = " + researcherCount);
    	if( researcherCount > 0 )
    	{
    		// we can grab them all at once
    		if(  researcherCount == 1 || researcherCount == 2)
    		{
    			currentLocation = 0;
    			researchers.addAll(researcherService.getPublicResearchersByLastFirstName(0, 2, OrderType.ASCENDING_ORDER));
    		}
    		else if ( type.equals(INIT))
     	    {
    			
     		    Random random = new Random();
     		    currentLocation = random.nextInt(researcherCount);
     		    
     		   log.debug( "init current Location = " + currentLocation  );
     		    
     		    // if past last position
         	    if ((currentLocation + 1) >= researcherCount) {
         	    	// get last researcher
         	    	log.debug( "init A fetching = " + currentLocation  );
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
         	    	
         	    	// reset current location to start
         	    	currentLocation = 0;
         	    	
         	    	log.debug( "init B fetching = " + currentLocation  );
         	    	// get first researcher
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
         	       
         	    } else {
         	    	// otherwise in middle of list
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
         	    	currentLocation = currentLocation + 1;
         	    }
         	   log.debug( "init final INIT Location = " + currentLocation  );
         	    
     	    }
    		else if( type.equals(NEXT))
    	    {
    			log.debug( "NEXT = " + currentLocation  );
    			// end of list - just showed last researcher
    			if( (currentLocation + 1) >= researcherCount )
    			{
    				currentLocation = 0;
    				log.debug( "NEXT A fetching = " + currentLocation  );
     			    researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
     		        currentLocation = currentLocation + 1;
    			}
    		    else
    		    {
    		    	log.debug("NEXT - 1 = " + currentLocation  );
    			    // can't grab both at once  one at very end and one at the beginning
    			    if( (currentLocation + 2) == researcherCount)
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	log.debug( "NEXT B fetching = " + currentLocation  );
    			    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
    			    	currentLocation = 0;
    			    	log.debug( "NEXT C fetching = " + currentLocation  );
             	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
    			    }
    			    else
    			    {
    			    	currentLocation = currentLocation + 1;
    			    	log.debug("NEXT D fetching = " + currentLocation  );
    			    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
    			    	currentLocation = currentLocation + 1;
    			    }
     		    }
    			log.debug( "NEXT End current = " + currentLocation  );
    	    }
    		else if( type.equals(PREV))
    	    {
    			log.debug( "PREV = " + currentLocation  );
    			
    			// at position 0 get - so very last and 0 are loaded
    			// get two before very last
    	    	if (currentLocation == 0) {
    	    		currentLocation = researcherCount - 3;
    	    		log.debug("PREV A fetching = " + currentLocation);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
    	    		currentLocation = currentLocation + 1;
    	    	} 
    	    	// position 0 and 1 are loaded - so get very last two
    	    	else if( currentLocation == 1)
    	    	{
    	    		currentLocation = researcherCount - 2;
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
    	    		currentLocation = currentLocation + 1;
    	    	}
    	    	// 1 and 2 are loaded - get very last and first
    	    	else if ( currentLocation ==  2 ) 
    	    	{
    	    		currentLocation = researcherCount - 1;
    	    		log.debug("PREV B fetching = " + currentLocation);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
    	    		currentLocation = 0;
    	    		log.debug("PREV C fetching = " + currentLocation);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 1, OrderType.ASCENDING_ORDER));
    	    	}
    	    	// at position grab last 2 from current positions
    	    	else 
    		    {
    			    currentLocation = currentLocation - 3;
    			    log.debug("PREV E fetching = " + currentLocation);
    			    //get the last 2
    			    researchers.addAll(researcherService.getPublicResearchersByLastFirstName(currentLocation, 2, OrderType.ASCENDING_ORDER));
    			    currentLocation = currentLocation + 1;
    		    }
    	    	log.debug( "PREV End current = " + currentLocation  );
    		   
    	    }
    	}
        
  
        return SUCCESS;
    }
   

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
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
}
