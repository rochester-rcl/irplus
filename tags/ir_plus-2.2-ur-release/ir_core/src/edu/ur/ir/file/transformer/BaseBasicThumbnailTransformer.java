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

package edu.ur.ir.file.transformer;

import java.awt.Image;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;


/**
 * Implementation of a basic transformer.
 * 
 * Inspiration from: http://jcsnippets.atspace.com/java/gui-graphics/create-thumbnail.html
 * 
 * @author Nathan Sarr
 *
 */
public abstract class BaseBasicThumbnailTransformer implements BasicThumbnailTransformer,
NameAware, DescriptionAware{
	
    /** eclipse generated id */
	private static final long serialVersionUID = 1425035716125388179L;

	/** name of the transformer*/
    protected String name;
    
    /** description of the transformer */
    protected String description;
    
    /** Extension that the file should be given */
    protected String fileExtension;
	
	/** Indicates a vertical or horizontal maximum  */
    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
     
    /**  File extensions this creator expects */
    protected Set<String> acceptableFileTypeExtensions = new HashSet<String>();
    
    /**  Maximum size of the thumbnail  */
    protected int size;
    
    /** Direction of the thumbnail it will default to vertical  */
    protected int direction = VERTICAL;

    /**
    * Image.SCALE_AREA_AVERAGING: Use the Area Averaging image scaling algorithm 
    * Image.SCALE_DEFAULT: Use the default image-scaling algorithm 
    * Image.SCALE_FAST: Choose an image-scaling algorithm that gives higher priority to scaling speed than smoothness of the scaled image 
    * Image.SCALE_REPLICATE: Use the image scaling algorithm embodied in the ReplicateScaleFilter class 
    * Image.SCALE_SMOOTH: Choose an image-scaling algorithm that gives higher priority to image smoothness than scaling speed 
    */
    protected int scale = Image.SCALE_SMOOTH;
    
  
 	/* (non-Javadoc)
	 * @see edu.ur.ir.file.FileDeriver#getAcceptableFileTypes()
	 */
	public Set<String> getAcceptableFileTypeExtensions() {
		return acceptableFileTypeExtensions;
	}

	/**
	 * Direction to resize the thumbnail.
	 * 
	 * @return
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * Direction to resize the thumbnail.
	 * 
	 * @param direction
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Get the scale to use to scale the image.
	 * 
	 * @return
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Set the way to scale the image.
	 * 
	 * @param scale
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * Get the size.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Set the size.
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/** 
	 * Name of the transformer.
	 * 
	 * @see edu.ur.simple.type.NameAware#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name of the transformer.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the description of this transformer.
	 * 
	 * @see edu.ur.simple.type.DescriptionAware#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of this transformer.
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Very simple implementation which checks the file extensions to determine if
	 * it can transform the file.  
	 * 
	 */
	public boolean canTransform(String extension)
	{
		
		for( String ext : acceptableFileTypeExtensions)
		{
			if( ext.equalsIgnoreCase(extension))
			{
				return true;
			}
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.ur.ir.file.transformer.FileTransformer#getFileExtension()
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	/**
	 * Get an image reader instead of an extension.
	 * 
	 * @param extension
	 */
	public ImageReader getImageReader(String extension)
	{
		ImageReader reader = null;
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(extension);
		
		if(iter.hasNext())
		{
			return iter.next();
		}
		
		return reader;
	}
	
	/**
	 * Get an image reader based on an extension.
	 * 
	 * @param extension
	 */
	public ImageWriter getImageWriter(String extension)
	{
		ImageWriter writer = null;
		Iterator<ImageWriter> iter = ImageIO.getImageWritersBySuffix(extension);
		
		if(iter.hasNext())
		{
			return iter.next();
		}
		
		return writer;
	}
}
