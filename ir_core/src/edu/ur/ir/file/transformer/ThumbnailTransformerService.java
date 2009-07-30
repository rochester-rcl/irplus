package edu.ur.ir.file.transformer;

import java.io.File;


/**
 * Service for creating thumbnails.  This simple interface only allows for one 
 * thumbnail transformer.
 * 
 * @author Nathan Sarr
 *
 */
public interface ThumbnailTransformerService 
{
	
    /**
     * Get the default thumbnail transformer.
     * 
     * @return the default thumbnail transformer.
     */
    public BasicThumbnailTransformer getDefaultThumbnailTransformer();
	
	/**
	 * Create a transformation of the given file and write it out to the transformed file.  Handling 
	 * failure output and notification is left to the service.
	 * 
	 * @param inFile - file to make a transform of.
	 * @param transformedFile - the file to save the transformed file to
	 * 
	 * returns true if the transformation was successful.  
     *	
	 */
	public boolean transformFile(File inFile, String inFileExtension, File transformedFile);



}
