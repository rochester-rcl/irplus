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

import java.io.File;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.researcher.Researcher;
import edu.ur.ir.researcher.ResearcherService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows news pictures to be uploaded.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class UploadResearcherImage extends ActionSupport implements UserIdAware{
	
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = -642221640484799568L;

	/** User trying to upload the file */
	private Long userId;
	
	/**  Id of the researcher to add the picture to */
	private Long researcherId;

	/** Class to create jpeg thumbnails. */
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Service for dealing with news information */
	private Researcher researcher ;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/** file uploaded */
	private File file;
	
	/**  File name uploaded from the file system */
	private String fileFileName;
		
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(UploadResearcherImage.class);
	
	/** Indicates if the picture is the primary picture  */
	private boolean primaryResearcherPicture = false;
	
	/** Indicates the file has been added. */
	private boolean added = false;
	
	/** Institutional Researcher service */
	private ResearcherService researcherService;

	
	/**
	 * Uploads a new image to the system.
	 * 
	 * @return
	 */
	public String addNewPicture() throws Exception
	{
		if( log.isDebugEnabled())
		{
			log.debug("add new picture called");
			log.debug("primaryResearcherPicture = " + primaryResearcherPicture);
		}
		
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		IrFile picture = null;
		researcher = researcherService.getResearcher(researcherId, false);
		
		
		// only a user can edit their own researcher page
		if( !researcher.getUser().getId().equals(userId))
		{
			return "accessDenied";
		}
		
		
		picture = repositoryService.createIrFile(repository, file, fileFileName, "primary news picture for researcher id = " 
				+ researcher.getId());
		
		if( primaryResearcherPicture )
		{
			IrFile primaryPicture = researcher.getPrimaryPicture();
			if( primaryPicture != null)
			{
				researcher.addPicture(primaryPicture);
			}
			
			researcher.setPrimaryPicture(picture);
			added = true;
		}
		else
		{
			researcher.addPicture(picture);
			added = true;
		}
		
		if( !added)
		{
			String message = getText("researcherPictureUploadError", 
					new String[]{fileFileName});
			addFieldError("researcherPictureUploadError", message);
		}
		else
		{
			researcherService.saveResearcher(researcher);
		}
		
		FileInfo fileInfo = picture.getFileInfo();
		
		log.debug("File info = " + fileInfo);
		String extension = fileInfo.getExtension();
		
		if(defaultThumbnailTransformer.canTransform(extension))
	    {
	    	log.debug("Creating transform of " + file.getAbsolutePath());
		    try
		    {
		        File tempFile = temporaryFileCreator.createTemporaryFile(extension);
		        defaultThumbnailTransformer.transformFile(file, extension, tempFile);
		        
		        if( tempFile != null && tempFile.exists() && tempFile.length() != 0l)
		        {
		            TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode("PRIMARY_THUMBNAIL");
		    
		            repositoryService.addTransformedFile(repository, 
		    		    picture, 
		    		    tempFile, 
		    		    "JPEG file", 
		    		    defaultThumbnailTransformer.getFileExtension(), 
		    		    transformedFileType);
		        }
		        else
			    {
			        	log.error("could not create thumbnail for file " + fileInfo);
			    }
		    
		    }
		    catch(Exception e)
		    {
			    log.error("Could not create thumbnail", e);
		    }
	    }
	    
	    log.debug("returning success");
	    return SUCCESS;
	}
	
	
	/**
	 * Get the user id uploading the image.
	 * 
	 * @return
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * Set the user id uploading the image.
	 * 
	 * @see edu.ur.ir.web.action.UserIdAware#setUserId(java.lang.Long)
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Transformer for the thumbnail.
	 * 
	 * @return the Jpeg thumbnail transformer
	 */
	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return defaultThumbnailTransformer;
	}

	/**
	 * Set the jpeg thumbnail transformer.
	 * 
	 * @param defaultThumbnailTransformer
	 */
	public void setDefaultThumbnailTransformer(
			BasicThumbnailTransformer defaultThumbnailTransformer) {
		this.defaultThumbnailTransformer = defaultThumbnailTransformer;
	}

	/**
	 * This allows a temporary file to be created.
	 * 
	 * @return
	 */
	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	/**
	 * Set the temporary file creator.
	 * 
	 * @param temporaryFileCreator
	 */
	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
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

	/**
	 * Get the file to be added.
	 * 
	 * @return
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Set the file to be added.
	 * 
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Get the file name uploaded by the user.
	 * 
	 * @return
	 */
	public String getFileFileName() {
		return fileFileName;
	}

	/**
	 * Set the file name uploaded by the user.
	 * 
	 * @param fileFileName
	 */
	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public Researcher getResearcher() {
		return researcher;
	}


	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	/**
	 * Indicates that this is the primary picture for the 
	 * researcher.
	 * 
	 * @return
	 */
	public boolean isPrimaryResearcherPicture() {
		return primaryResearcherPicture;
	}
	
	/**
	 * Indicates this is the primary picture for the researcher.
	 * 
	 * @return
	 */
	public boolean getPrimaryResearcherPicture()
	{
		return isPrimaryResearcherPicture();
	}


	/**
	 * Set to true if this is the primary picture for the researcher.
	 * 
	 * @param primaryResearcherPicture
	 */
	public void setPrimaryResearcherPicture(boolean primaryResearcherPicture) {
		this.primaryResearcherPicture = primaryResearcherPicture;
	}


	/**
	 * Id of the researcher to add the image to.
	 * 
	 * @return
	 */
	public Long getResearcherId() {
		return researcherId;
	}


	/**
	 * Id of the researcher to add the image to.
	 * 
	 * @param researcherId
	 */
	public void setResearcherId(Long researcherId) {
		this.researcherId = researcherId;
	}


	/**
	 * True if the image is added.
	 * 
	 * @return
	 */
	public boolean isAdded() {
		return added;
	}


	/**
	 * Set to true if the image is added.
	 * 
	 * @param added
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}


	public ResearcherService getResearcherService() {
		return researcherService;
	}


	public void setResearcherService(
			ResearcherService researcherService) {
		this.researcherService = researcherService;
	}

}
