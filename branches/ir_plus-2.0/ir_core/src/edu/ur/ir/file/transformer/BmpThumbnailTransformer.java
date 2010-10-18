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


import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;

import org.apache.log4j.Logger;

/**
 * Creates a bitmap thumbnail.
 * 
 * @author Nathan Sarr
 *
 */
public class BmpThumbnailTransformer extends BaseBasicThumbnailTransformer{
	
	/** Logger */
	private static final Logger log = Logger.getLogger(BmpThumbnailTransformer.class);
	
    /** eclipse generated id */
	private static final long serialVersionUID = -1757483145237066322L;
	
    /**
     * Default constructor
     */
    public BmpThumbnailTransformer(){
    	setSize(100);
    	setFileExtension("bmp");
    	acceptableFileTypeExtensions.add("jpeg");
    	acceptableFileTypeExtensions.add("jpg");
    	acceptableFileTypeExtensions.add("bmp");
    	acceptableFileTypeExtensions.add("gif");
    	acceptableFileTypeExtensions.add("png");
    	acceptableFileTypeExtensions.add("tiff");
    	acceptableFileTypeExtensions.add("tif");
    	setName("Bitmap thubmnail creator");
    	setDescription("Creates a bitmap thumbnail out of the given file");
    }
 
    /**
     * Initialize the thumb nail creator to generate a thumb nail with a specific size.
     * 
     * @param size
     */
    public BmpThumbnailTransformer(int size)
    {
    	this();
    	setSize(size);
    }
    
    /**
     * Default constructor.
     * 
     * @param size
     * @param direction
     * @param scale
     */
    public BmpThumbnailTransformer(int size, int direction, int scale)
    {
    	this(size);
    	setDirection(direction);
    	setScale(scale);
    }
    
	/**
	 * Create a derived jpeg thumbnail.
	 * 
	 * @see edu.ur.ir.file.transformer.FileTransformer#transformFile(edu.ur.ir.file.IrFile, java.io.File)
	 */
	public void transformFile(File inFile, String inFileExtension, File transformFile) throws IOException {
		log.debug("transform file called");
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
		RenderedImage image = null;
		RenderedImage scale = null;
		try
		{
		    image = JAI.create("fileload", inFile.getAbsolutePath());
		    float xScale =  ((float)size)/((float)image.getWidth());
		    float yScale = ((float)size)/((float)image.getHeight());
		
		    ParameterBlock pb = new ParameterBlock();
		    pb.addSource(image); // The source image
		    pb.add(xScale);         // The xScale
		    pb.add(yScale);         // The yScale
		    pb.add(0.0F);           // The x translation
		    pb.add(0.0F);           // The y translation
		    pb.add(new InterpolationNearest()); // The interpolation 
			scale = JAI.create("scale", pb);
			JAI.create("filestore", scale, transformFile.getAbsolutePath(), "BMP");
			
		}
		catch(OutOfMemoryError oome)
		{
			image = null;
			scale = null;
			throw (oome);
		}
		finally
		{
			image = null;
			scale = null;
		}
	}

	public static void main(String[] args) throws Exception
	{
		if( args.length < 3 )
		{
			System.out.println("Usage is file to convert, file to convert extension,  new file location");
		}
		else
		{
		    File image = new File(args[0]);
		    File thumbnail = new File(args[1]);
		    String extension = args[2];
		    
		    FileTransformer deriver = new BmpThumbnailTransformer(100);
		    deriver.transformFile(image, extension, thumbnail);
		}
	}
}
