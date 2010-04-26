package edu.ur.ir.file.transformer.service;

import java.io.File;

import org.apache.log4j.Logger;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.file.IrFile;
import edu.ur.ir.file.IrFileTransformationFailureRecord;
import edu.ur.ir.file.IrFileTransformationFailureRecordDAO;
import edu.ur.ir.file.TemporaryFileCreator;
import edu.ur.ir.file.TransformedFileType;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;
import edu.ur.ir.repository.Repository;
import edu.ur.ir.repository.RepositoryService;

/**
 * Default thumbnail service.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultThumbnailTransformerService implements ThumbnailTransformerService{
	
	/**  Thumbnailer to perform thumbnailing */
	private BasicThumbnailTransformer basicThumbnailTransformer;
	
	/** Service for emailing errors */
	private ErrorEmailService errorEmailService;
	
	/** data access for file transformation failure record data access  */
	private IrFileTransformationFailureRecordDAO irFileTransformationFailureRecordDAO;
	
	/**  Get the logger for this class */
	private static final Logger log = Logger.getLogger(DefaultThumbnailTransformerService.class);
	
	/** Temporary file creator to allow a temporary file to be created for processing */
	private TemporaryFileCreator temporaryFileCreator;
	
	/**  repository service */
	private RepositoryService repositoryService;


	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return basicThumbnailTransformer;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.file.transformer.ThumbnailTransformerService#transformFile(java.io.File, java.lang.String, java.io.File)
	 */
	public boolean transformFile(Repository repository, IrFile inFile) {
		
		boolean successful = false;
		
		
		FileInfo fileInfo = inFile.getFileInfo();
		
		log.debug("File info = " + fileInfo);
		String extension = fileInfo.getExtension();
		
		if(basicThumbnailTransformer.canTransform(extension))
	    {
			File file = new File(fileInfo.getFullPath());
	    	log.debug("Creating transform of " + file.getAbsolutePath());
		    try
		    {
		        File tempFile = temporaryFileCreator.createTemporaryFile(extension);
		        basicThumbnailTransformer.transformFile(file, extension, tempFile);
		    
		        if( tempFile != null && tempFile.exists() && tempFile.length() > 0l)
		        {
		        	TransformedFileType transformedFileType = repositoryService.getTransformedFileTypeBySystemCode(TransformedFileType.PRIMARY_THUMBNAIL);
		            repositoryService.addTransformedFile(repository, 
		    		    inFile, 
		    		    tempFile, 
		    		    "JPEG file", 
		    		    basicThumbnailTransformer.getFileExtension(), 
		    		    transformedFileType);
		            
		        	successful = true;
		        }
		        else
		        {
		        	log.error("could not create thumbnail for file " + fileInfo);
		        	IrFileTransformationFailureRecord failureRecord= new IrFileTransformationFailureRecord(inFile.getId(), "File of size 0 returned" );
					irFileTransformationFailureRecordDAO.makePersistent(failureRecord);
		        }
		    }
		    catch(Exception e)
		    {
			    log.error("Could not create thumbnail", e);
	        	IrFileTransformationFailureRecord failureRecord= new IrFileTransformationFailureRecord(inFile.getId(), e.toString() );
				irFileTransformationFailureRecordDAO.makePersistent(failureRecord);
				errorEmailService.sendError(e);
		    }
	    }
		return successful;
	}

	public BasicThumbnailTransformer getBasicThumbnailTransformer() {
		return basicThumbnailTransformer;
	}

	public void setBasicThumbnailTransformer(
			BasicThumbnailTransformer basicThumnailTransformer) {
		this.basicThumbnailTransformer = basicThumnailTransformer;
	}

	public ErrorEmailService getErrorEmailService() {
		return errorEmailService;
	}

	public void setErrorEmailService(ErrorEmailService errorEmailService) {
		this.errorEmailService = errorEmailService;
	}

	public IrFileTransformationFailureRecordDAO getIrFileTransformationFailureRecordDAO() {
		return irFileTransformationFailureRecordDAO;
	}

	public void setIrFileTransformationFailureRecordDAO(
			IrFileTransformationFailureRecordDAO irFileTransformationFailureRecordDAO) {
		this.irFileTransformationFailureRecordDAO = irFileTransformationFailureRecordDAO;
	}
	
	public TemporaryFileCreator getTemporaryFileCreator() {
		return temporaryFileCreator;
	}

	public void setTemporaryFileCreator(TemporaryFileCreator temporaryFileCreator) {
		this.temporaryFileCreator = temporaryFileCreator;
	}
	
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}


}
