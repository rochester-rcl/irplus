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


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class NextRepositoryPicture extends ActionSupport {
	
	/** Eclipse generated Id */
	private static final long serialVersionUID = 470760718471391384L;
		
	/**  Logger for file upload */
	private static final Logger log = LogManager.getLogger(NextRepositoryPicture.class);
	
	/**  The repository to get the pictures from */
	private Repository repository;

	/** Service for dealing with repository */
	private RepositoryService repositoryService;
	
	/**  Ir file that should be shown. */
	private IrFile repositoryImageFile;
	
	/**  number of pictures */
	private int numRepositoryPictures;
	
	/**
	 * Determine if the user is initializing wants the next or previous
	 * picture
	 */
	private String type;
	
	/**  Current picture location */
	private int currentRepositoryPictureLocation;
	
	/**  Helper to get the next repository picture */
	private RandomRepositoryPictureHelper repositoryPictureHelper = new RandomRepositoryPictureHelper();
	
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
		repositoryImageFile = repositoryPictureHelper.getNextPicture(type, repository, currentRepositoryPictureLocation);
        currentRepositoryPictureLocation = repositoryPictureHelper.getCurrentRepositoryPictureLocation();
        numRepositoryPictures = repositoryPictureHelper.getNumRepositoryPictures();

        return SUCCESS;
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

	public int getCurrentRepositoryPictureLocation() {
		return currentRepositoryPictureLocation;
	}

	public void setCurrentRepositoryPictureLocation(int currentLocation) {
		this.currentRepositoryPictureLocation = currentLocation;
	}


	public IrFile getRepositoryImageFile() {
		return repositoryImageFile;
	}

	public void setRepositoryImageFile(IrFile irFile) {
		this.repositoryImageFile = irFile;
	}
	
	public int getNumRepositoryPictures() {
		return numRepositoryPictures;
	}

	public void setNumRepositoryPictures(int numPictures) {
		this.numRepositoryPictures = numPictures;
	}

}
