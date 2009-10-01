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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.RepositoryService;

public class NextCollectionPicture extends ActionSupport implements
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
	private static final Logger log = Logger.getLogger(NextCollectionPicture.class);
	
	/**  The institutional collection*/
	private InstitutionalCollection institutionalCollection;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/** id of the collection we want to look at  */
	private Long collectionId;
	
	/**  Ir file that should be shown. */
	private IrFile irFile;
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current picture location */
	private int currentLocation;
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** number of collection pictures */
	private int numCollectionPictures;

	/**
     * Gets the next ir file to be downloaded
     *
     * @return {@link #SUCCESS}
     */
    public String execute() throws Exception {
    	
    	if( log.isDebugEnabled())
    	{
    		log.debug("Next Collection Picture");
    	}
	  
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
       
		if( institutionalCollection != null )
		{
		    LinkedList<IrFile> pictures = new LinkedList<IrFile>(institutionalCollection.getPictures());
        
		   
		    
            // sort the pictures to assure order
            Collections.sort(pictures, this);
        
            // always put the primary picture first
            IrFile primaryPicture = institutionalCollection.getPrimaryPicture();
		    if(primaryPicture!= null)
		    {
		        pictures.addFirst(primaryPicture);
		    }
		    numCollectionPictures = pictures.size();
		    
            if( pictures != null && pictures.size() > 0 )
            {
        	    if( pictures.size() == 1 )
        	    {
        		    currentLocation = 0;
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


	public Long getCollectionId() {
		return collectionId;
	}


	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public int getNumCollectionPictures() {
		return numCollectionPictures;
	}


	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
