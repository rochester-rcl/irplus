
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


import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.media.jai.JAI;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




/**
 * Creates a jpeg thumbnail.
 * 
 * @author Nathan Sarr
 *
 */
public class JpegThumbnailTransformer extends BaseBasicThumbnailTransformer{
	
	/** Logger */
	private static final Logger log = LogManager.getLogger(JpegThumbnailTransformer.class);

    /** eclipse generated id */
	private static final long serialVersionUID = 3747655438497116611L;
	
    
    public JpegThumbnailTransformer(){ 
    	setSize(100);
    	setFileExtension("jpeg");
    	acceptableFileTypeExtensions.add("jpeg");
    	acceptableFileTypeExtensions.add("jpg");
    	acceptableFileTypeExtensions.add("bmp");
    	acceptableFileTypeExtensions.add("gif");
    	acceptableFileTypeExtensions.add("png");
    	acceptableFileTypeExtensions.add("tiff");
    	acceptableFileTypeExtensions.add("tif");
       	setName("JPEG thubmnail creator");
    	setDescription("Creates a jpeg thumbnail out of the given file");
 
    }
 
    /**
     * Initalize the thumbnail creator to generate a thumbnail with a specific size.
     * 
     * @param size
     */
    public JpegThumbnailTransformer(int size)
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
    public JpegThumbnailTransformer(int size, int direction, int scale)
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
		
		ImageWriter writer = null;
		RenderedImage image = null;
		if( inFileExtension.equalsIgnoreCase("gif"))
		{
			handleGif(inFile, transformFile);
		}
		else
		{
			try
			{
		        System.setProperty("com.sun.media.jai.disableMediaLib", "true");
		        ImageReader reader = getImageReader(inFileExtension);
		        image = null;
		        if( reader != null )
		        {
			        log.debug("using reader from ImageIO");
			        image = ImageIO.read(inFile);
		        }
		        else 
		        {
			        log.debug("using reader from JAI");
			        //only use the file load if there is not an ImageIO reader
		            image = JAI.create("fileload", inFile.getAbsolutePath());
		        }
		
		        writer = getImageWriter(inFileExtension);
		
		        if( writer != null )
		        {
		            writeWithImageIO(image, transformFile);
		            writer.dispose();
		        }
		        else
		        {
                    writeWithJAI(image, transformFile);
		        }
		    }
			catch(OutOfMemoryError oome)
			{
				writer.dispose();
				image = null;
				throw(oome);
			}
			finally
			{
			    if(writer != null)
			    {
			    	writer.dispose();
			    }
			    image = null;
			    
			}
		}
		
	}
	
	
	/**
	 * Handles processing a gif file.
	 * 
	 * @param inFile
	 * @param transformFile
	 * @throws IOException
	 */
	private void handleGif(File inFile, File transformFile) throws IOException {

		ImageIcon image = new ImageIcon(inFile.getAbsolutePath());
		ImageIcon thumb;
		Graphics g = null;
		if (direction == HORIZONTAL) 
		{
			thumb = new ImageIcon(image.getImage().getScaledInstance(size, -1,
					    scale));
		} else {
			thumb = new ImageIcon(image.getImage().getScaledInstance(-1, size,
					scale));
		}
		if (thumb != null) {
		    try
		    {
			    BufferedImage bi = new BufferedImage(thumb.getIconWidth(), thumb
				    .getIconHeight(), BufferedImage.TYPE_INT_RGB);
			    g = bi.getGraphics();
			    g.drawImage(thumb.getImage(), 0, 0, null);

			    ImageIO.write(bi, fileExtension, transformFile);
			    g.dispose();
			    g = null;
		    }
		    finally
		    {
		    	if( g!= null)
		    	{
		    		g.dispose();
		    		g = null;
		    	}
		    	thumb = null;
		    }

		} else {
			image = null;
			throw new RuntimeException("Could not create thumbnail");
		}
	}
	
	
	/**
	 * Write the image using imageIO libraries.
	 * 
	 * @param image
	 * @param transformFile
	 * @throws IOException 
	 */
	private void writeWithImageIO(RenderedImage image, File transformFile) throws IOException
	{
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		
		int componentWidth = size;
		int componentHeight = size;
		double scale = -1;
		if ( imageWidth == componentWidth && imageHeight == componentHeight)
		{
		    scale = 1;
		}
		else if ( imageWidth <= componentWidth && imageHeight <= componentHeight)
		{
		    // set scale to 1 if we don't want to scale up - otherwise use code below...
		    scale = 1;
		    // double heightScale = ((double)componentWidth) / ((double)imageWidth);
		    // double widthScale = ((double)componentHeight) / ((double)imageHeight);
		    // if ( heightScale < widthScale ) scale = heightScale;
		    // else scale = widthScale;
		}
		else if ( imageWidth > componentWidth && imageHeight <= componentHeight)
		{
		    double heightScale = ((double)componentWidth) / ((double)imageWidth);
		    scale = heightScale;
		}
		else if ( imageWidth <= componentWidth && imageHeight > componentHeight)
		{
		    double widthScale = ((double)componentHeight) / ((double)imageHeight);
		    scale = widthScale;
		}
		else
		{
		    double heightScale = ((double)componentWidth) / ((double)imageWidth);
		    double widthScale = ((double)componentHeight) / ((double)imageHeight);
		    int scaledWidth = (int)(((double)imageWidth) * widthScale);
		    if ( scaledWidth <= componentWidth )
		    {
		    	scale = widthScale;
		    }
		    else
		    {
		    	scale = heightScale;
		    }
		}
		// Now create thumbnail
		AffineTransform affineTransform = null;
		AffineTransformOp affineTransformOp = null;
		BufferedImage scaledBufferedImage = null;
		try
		{
		    affineTransform = AffineTransform.getScaleInstance(scale,scale);
		    affineTransformOp = new AffineTransformOp(affineTransform,null);
		    scaledBufferedImage = affineTransformOp.filter((BufferedImage)image,null);
		    // Now do fix to get rid of silly spurious line
		    int scaledWidth = scaledBufferedImage.getWidth();
		    int scaledHeight = scaledBufferedImage.getHeight();
		    int expectedWidth = (int)(imageWidth * scale);
		    int expectedHeight = (int)(imageHeight * scale);
		    if ( scaledWidth > expectedWidth || scaledHeight > expectedHeight )
		    scaledBufferedImage = scaledBufferedImage.getSubimage(
		    0,0,expectedWidth,expectedHeight);
		
		    // Now write out scaled image to file
		    ImageIO.write(scaledBufferedImage,"JPG",new File(transformFile.getAbsolutePath()));
		    scaledBufferedImage.flush();
		}
		finally
		{
			affineTransform = null;
		    affineTransformOp = null;
		    scaledBufferedImage = null;
		}
	}
	
	/**
	 * Write the file using the JAI libraries.
	 * 
	 * @param image
	 * @param transformFile
	 */
	private void writeWithJAI(RenderedImage image, File transformFile)
	{
		// determine the scale for the x and y of the image
		float xScale = ((float)size)/((float)image.getWidth());
		float yScale = ((float)size)/((float)image.getHeight());
		

		log.debug("xScale = " + xScale + " yScale = " + yScale);
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image); // The source image
		pb.add(xScale);         // The xScale
		pb.add(yScale);         // The yScale
		pb.add(0.0F);           // The x translation
		pb.add(0.0F);           // The y translation
		
		RenderedImage scale = null;
		
		try
		{
		    scale = JAI.create("scale", pb);
		    JAI.create("filestore", scale, transformFile.getAbsolutePath(), "JPEG");
		}
		finally
		{
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

		    JpegThumbnailTransformer deriver = new JpegThumbnailTransformer(100);
		    deriver.setDirection(1);
		    deriver.transformFile(image, extension, thumbnail);
		}
	}

}
