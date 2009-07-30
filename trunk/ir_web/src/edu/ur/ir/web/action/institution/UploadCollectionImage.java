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

import java.io.File;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.institution.InstitutionalCollection;
import edu.ur.ir.institution.InstitutionalCollectionService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;
import edu.ur.ir.web.action.UserIdAware;

/**
 * Allows news pictures to be uploaded.
 * 
 * @author Nathan Sarr
 *
 */
public class UploadCollectionImage extends ActionSupport implements UserIdAware{
	
	
	/**  Eclipse generated id */
	private static final long serialVersionUID = 8172521971856582951L;

	/** User trying to upload the file */
	private Long userId;
	
	/**  Id of the collection to add the picture to */
	private Long collectionId;

	/** Class to create jpeg thumbnails. */
	private BasicThumbnailTransformer defaultThumbnailTransformer;
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/** Service for dealing with news information */
	private InstitutionalCollection institutionalCollection;
	
	/** Repository service */
	private RepositoryService repositoryService;
	
	/** file uploaded */
	private File file;
	
	/**  File name uploaded from the file system */
	private String fileFileName;
		
	/**  Logger for add personal folder action */
	private static final Logger log = Logger.getLogger(UploadCollectionImage.class);
	
	/** Indicates if the picture is the primary picture  */
	private boolean primaryCollectionPicture = false;
	
	/** Indicates the file has been added. */
	private boolean added = false;
	
	/** Institutional Collection service */
	private InstitutionalCollectionService institutionalCollectionService;

	
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
			log.debug("primaryCollectionPicture = " + primaryCollectionPicture);
		}
		
		Repository repository = 
			repositoryService.getRepository(Repository.DEFAULT_REPOSITORY_ID, false);
		
		IrFile picture = null;
		
		
		institutionalCollection = institutionalCollectionService.getCollection(collectionId, false);
		
		picture = repositoryService.createIrFile(repository, file, fileFileName, "primary news picture for collection id = " 
				+ institutionalCollection.getId());
		
		if( primaryCollectionPicture )
		{
			IrFile primaryPicture = institutionalCollection.getPrimaryPicture();
			if( primaryPicture != null)
			{
				repositoryService.deleteIrFile(primaryPicture);
			}
			
			institutionalCollection.setPrimaryPicture(picture);
			added = true;
		}
		else
		{
			institutionalCollection.addPicture(picture);
			added = true;
		}
		
		if( !added)
		{
			String message = getText("collectionPictureUploadError", 
					new String[]{fileFileName});
			addFieldError("collectionPictureUploadError", message);
		}
		else
		{
			institutionalCollectionService.saveCollection(institutionalCollection);
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
		        
		    
		        if( tempFile != null && tempFile.exists() && tempFile.length() > 0l)
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

	public InstitutionalCollection getInstitutionalCollectione() {
		return institutionalCollection;
	}


	public void setInstitutionalCollectione(InstitutionalCollection institutionalCollection) {
		this.institutionalCollection = institutionalCollection;
	}

	/**
	 * Indicates that this is the primary picture for the 
	 * collection.
	 * 
	 * @return
	 */
	public boolean isPrimaryCollectionPicture() {
		return primaryCollectionPicture;
	}
	
	/**
	 * Indicates this is the primary picture for the collection.
	 * 
	 * @return
	 */
	public boolean getPrimaryCollectionPicture()
	{
		return isPrimaryCollectionPicture();
	}


	/**
	 * Set to true if this is the primary picture for the collection.
	 * 
	 * @param primaryCollectionPicture
	 */
	public void setPrimaryCollectionPicture(boolean primaryCollectionPicture) {
		this.primaryCollectionPicture = primaryCollectionPicture;
	}


	/**
	 * Id of the collection to add the image to.
	 * 
	 * @return
	 */
	public Long getCollectionId() {
		return collectionId;
	}


	/**
	 * Id of the collection to add the image to.
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
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


	public InstitutionalCollectionService getInstitutionalCollectionService() {
		return institutionalCollectionService;
	}


	public void setInstitutionalCollectionService(
			InstitutionalCollectionService institutionalCollectionService) {
		this.institutionalCollectionService = institutionalCollectionService;
	}

}
