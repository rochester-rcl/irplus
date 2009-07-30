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
 * Creates a PNG thumbnail from the given image.
 * 
 * @author Nathan Sarr
 *
 */
public class PngThumbnailTransformer extends BaseBasicThumbnailTransformer{
	
    /** eclipse generated id. */
	private static final long serialVersionUID = 4177646177173397839L;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(PngThumbnailTransformer.class);
	
    public PngThumbnailTransformer(){ 
    	setSize(100);
    	setFileExtension("png");
    	acceptableFileTypeExtensions.add("jpeg");
    	acceptableFileTypeExtensions.add("jpg");
    	acceptableFileTypeExtensions.add("bmp");
    	acceptableFileTypeExtensions.add("gif");
    	acceptableFileTypeExtensions.add("png");
    	acceptableFileTypeExtensions.add("tiff");
    	acceptableFileTypeExtensions.add("tif");
    	setName("PNG thubmnail creator");
    	setDescription("Creates a png thumbnail out of the given file");

    }
    
    /**
     * Initalize the thumbnail creator to generate a thumbnail with a specific size.
     * 
     * @param size
     */
    public PngThumbnailTransformer(int size)
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
    public PngThumbnailTransformer(int size, int direction, int scale)
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
		    float xScale = ((float)size)/((float)image.getWidth());
		    float yScale = ((float)size)/((float)image.getHeight());
		
		    ParameterBlock pb = new ParameterBlock();
		    pb.addSource(image); // The source image
		    pb.add(xScale);         // The xScale
		    pb.add(yScale);         // The yScale
		    pb.add(0.0F);           // The x translation
		    pb.add(0.0F);           // The y translation
		    pb.add(new InterpolationNearest()); // The interpolation 

		    scale = JAI.create("scale", pb);
		    JAI.create("filestore", scale, transformFile.getAbsolutePath(), "PNG");
		}
		catch(OutOfMemoryError oome)
		{
			image = null;	
		    scale = null;
		    throw(oome);
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
			System.out.println("Usage is file to convert new file location");
		}
		else
		{
			System.out.println(" arg 0 = " + args[0]);
			System.out.println(" arg 1 = " + args[1]);
			System.out.println(" arg 2 = " + args[2]);

			File image = new File(args[0]);
		    String extension = args[1];
		    File thumbnail = new File(args[2]);

		    
		    FileTransformer deriver = new PngThumbnailTransformer(100);
		    deriver.transformFile(image, extension, thumbnail);
		}
	}


}
