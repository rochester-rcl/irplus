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

import java.util.Comparator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.web.action.institution.InstitutionalCollectionPictureHelper.PictureFileLocation;

/**
 * Get the next picture in the set of pictures.
 * 
 * @author Nathan Sarr
 *
 */
public class NextCollectionPicture extends ActionSupport implements
Comparator<IrFile>{
	
	/** Eclipse generated Id */
	private static final long serialVersionUID = 470760718471391384L;
	
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(NextCollectionPicture.class);

	/** id of the collection we want to look at  */
	private Long collectionId;
	
	/**  Ir file that should be shown for picture. */
	private IrFile irPictureFile;
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current picture location */
	private int currentPictureLocation;
	


	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;
	
	/** number of collection pictures */
	private int numCollectionPictures = 0;

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
	  
		InstitutionalCollection institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
        InstitutionalCollectionPictureHelper institutionalCollectionPictureHelper = new InstitutionalCollectionPictureHelper();
        PictureFileLocation locationInfo = institutionalCollectionPictureHelper.nextPicture(institutionalCollection, currentPictureLocation, type);
        if( locationInfo != null )
        {
        	numCollectionPictures = locationInfo.getNumPictures();
        	irPictureFile = locationInfo.getIrFile();
        	currentPictureLocation = locationInfo.getCurrentLocation();
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

	public void setType(String type) {
		this.type = type;
	}

	public int getCurrentPictureLocation() {
		return currentPictureLocation;
	}
	
	public void setCurrentPictureLocation(int currentPictureLocation) {
		this.currentPictureLocation = currentPictureLocation;
	}
	
	public IrFile getIrPictureFile() {
		return irPictureFile;
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
