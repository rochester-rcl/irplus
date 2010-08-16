/**  
   Copyright 2008-2010 University of Rochester

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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.order.OrderType;

/**
 * Helper to deal with the next random researcher picture.  
 * This will randomly select the first location and then rotate in order.
 * 
 * @author Nathan Sarr
 *
 */
public class RandomResearcherPictureHelper implements Serializable{
	
	/** eclipse generated id */
	private static final long serialVersionUID = 6799145112720922666L;

	/** determine what the user is trying to do */
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/** current researcher location */
	private int currentResearcherLocation = 0;
	
	/** count of available researchers  */
	private int researcherCount;
	
	


	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(RandomResearcherPictureHelper.class);
	
	/**
	 * Get the next researchers to be shown.
	 * 
	 * @param type - NEXT/PREV/INIT
	 * @param researcherService - service to help deal with researchers
	 * @param location - current location in the current list
	 * @return
	 */
	public List<Researcher> getResearchers(String type, ResearcherService researcherService, int location)
	{
		List<Researcher> researchers = new LinkedList<Researcher>();
	   	if( log.isDebugEnabled())
    	{
    		log.debug("Next researcher Picture");
    	}
	   	researcherCount = researcherService.getPublicResearcherCount().intValue();
    	log.debug("Getting researhcer picture researcher count = " + researcherCount);
    	if( researcherCount > 0 )
    	{
    		// we can grab them all at once
    		if(  researcherCount == 1 || researcherCount == 2)
    		{
    			location = 0;
    			researchers.addAll(researcherService.getPublicResearchersByLastFirstName(0, 2, OrderType.ASCENDING_ORDER));
    		}
    		else if ( type.equals(INIT))
     	    {
    			
     		    Random random = new Random();
     		    location = random.nextInt(researcherCount);
     		    
     		   log.debug( "init current Location = " + location  );
     		    
     		    // if past last position
         	    if ((location + 1) >= researcherCount) {
         	    	// get last researcher
         	    	log.debug( "init A fetching = " + location  );
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
         	    	
         	    	// reset current location to start
         	    	location = 0;
         	    	
         	    	log.debug( "init B fetching = " + location  );
         	    	// get first researcher
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
         	       
         	    } else {
         	    	// otherwise in middle of list
         	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
         	    	location = location + 1;
         	    }
         	   log.debug( "init final INIT Location = " + location  );
         	    
     	    }
    		else if( type.equals(NEXT))
    	    {
    			log.debug( "NEXT = " + location  );
    			// end of list - just showed last researcher
    			if( (location + 1) >= researcherCount )
    			{
    				location = 0;
    				log.debug( "NEXT A fetching = " + location  );
     			    researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
     		        location = location + 1;
    			}
    		    else
    		    {
    		    	log.debug("NEXT - 1 = " + location  );
    			    // can't grab both at once  one at very end and one at the beginning
    			    if( (location + 2) == researcherCount)
    			    {
    			    	location = location + 1;
    			    	log.debug( "NEXT B fetching = " + location  );
    			    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
    			    	location = 0;
    			    	log.debug( "NEXT C fetching = " + location  );
             	    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
    			    }
    			    else
    			    {
    			    	location = location + 1;
    			    	log.debug("NEXT D fetching = " + location  );
    			    	researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
    			    	location = location + 1;
    			    }
     		    }
    			log.debug( "NEXT End current = " + location  );
    	    }
    		else if( type.equals(PREV))
    	    {
    			log.debug( "PREV = " + location  );
    			
    			// at position 0 get - so very last and 0 are loaded
    			// get two before very last
    	    	if (location == 0) {
    	    		location = researcherCount - 3;
    	    		log.debug("PREV A fetching = " + location);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
    	    		location = location + 1;
    	    	} 
    	    	// position 0 and 1 are loaded - so get very last two
    	    	else if( location == 1)
    	    	{
    	    		location = researcherCount - 2;
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
    	    		location = location + 1;
    	    	}
    	    	// 1 and 2 are loaded - get very last and first
    	    	else if ( location ==  2 ) 
    	    	{
    	    		location = researcherCount - 1;
    	    		log.debug("PREV B fetching = " + location);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
    	    		location = 0;
    	    		log.debug("PREV C fetching = " + location);
    	    		researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 1, OrderType.ASCENDING_ORDER));
    	    	}
    	    	// at position grab last 2 from current positions
    	    	else 
    		    {
    			    location = location - 3;
    			    log.debug("PREV E fetching = " + location);
    			    //get the last 2
    			    researchers.addAll(researcherService.getPublicResearchersByLastFirstName(location, 2, OrderType.ASCENDING_ORDER));
    			    location = location + 1;
    		    }
    	    	log.debug( "PREV End current = " + location  );
    		   
    	    }
    	}
    	currentResearcherLocation = location;
		return researchers;
	}

	public int getCurrentResearcherLocation() {
		return currentResearcherLocation;
	}

	public int getResearcherCount() {
		return researcherCount;
	}

	
}
