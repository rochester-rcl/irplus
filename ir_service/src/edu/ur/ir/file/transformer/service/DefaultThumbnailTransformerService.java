package edu.ur.ir.file.transformer.service;

import java.io.File;

import org.apache.log4j.Logger;

import edu.ur.ir.ErrorEmailService;
import edu.ur.ir.file.IrFileTransformationFailureRecordDAO;
import edu.ur.ir.file.transformer.BasicThumbnailTransformer;
import edu.ur.ir.file.transformer.ThumbnailTransformerService;

/**
 * Default thumbnail service.
 * 
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

	public BasicThumbnailTransformer getDefaultThumbnailTransformer() {
		return basicThumbnailTransformer;
	}

	public boolean transformFile(File inFile, String inFileExtension,
			File transformedFile) {
		
		boolean successful = false;
		
		if( basicThumbnailTransformer.canTransform(inFileExtension) )
		{
			try
			{
				basicThumbnailTransformer.transformFile(inFile, inFileExtension, transformedFile);
			}
			catch(Exception e)
			{
				
			    log.error(e);
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

}
