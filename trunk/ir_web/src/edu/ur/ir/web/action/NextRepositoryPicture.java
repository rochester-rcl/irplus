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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;


import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Generates HTML for the next picture to be displayed.  This does
 * not pass the file for download only information to allow the file
 * to be downloaded.
 * 
 * @author Nathan Sarr
 *
 */
public class NextRepositoryPicture extends ActionSupport implements
Comparator<IrFile>{
	
	/** Eclipse generated Id */
	private static final long serialVersionUID = 470760718471391384L;
	
	/** determine what the user is trying to do */
	
	// this if when the page is first initalized
	public static final String INIT = "INIT";
	
	// get the next picture
	public static final String NEXT = "NEXT";
	
	// get the previous picture
	public static final String PREV = "PREV";
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(NextRepositoryPicture.class);
	
	/**  The repository to get the pictures from */
	private Repository repository;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/**  Ir file that should be shown. */
	private IrFile irFile;
	
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
    		log.debug("Next Repository Picture");
    	}
	  
		repository = repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
       
		if( repository != null )
		{
		    LinkedList<IrFile> pictures = new LinkedList<IrFile>(repository.getPictures());
        
		    
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
        		    Random random = new Random();
        		    currentLocation = random.nextInt(pictures.size());
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

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
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


	public IrFile getIrFile() {
		return irFile;
	}


	public void setIrFile(IrFile irFile) {
		this.irFile = irFile;
	}

}
