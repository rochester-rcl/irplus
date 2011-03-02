package edu.ur.ir.file.transformer;

import java.io.Serializable;

import edu.ur.ir.file.IrFile;
import edu.ur.ir.repository.Repository;


/**
 * Service for creating thumbnails.  This simple interface only allows for one 
 * thumbnail transformer.
 * 
 * @author Nathan Sarr
 *
 */
public interface ThumbnailTransformerService  extends Serializable
{
	
    /**
     * Get the default thumbnail transformer.
     * 
     * @return the default thumbnail transformer.
     */
    public BasicThumbnailTransformer getDefaultThumbnailTransformer();
	
	/**
	 * Create a transformation of the given file and write it out to the transformed file.  Handling 
	 * failure output and notification is left to the service.  The thumbnail is attached to the inFile
	 * and stored in the specified repository.
	 * 
	 * @param repository - repository to store the transform file into
	 * @param inFile - file to make a transform of.
	 * 
	 * returns true if the transformation was successful.  
     *	
	 */
	public boolean transformFile(Repository repository, IrFile inFile);



}
