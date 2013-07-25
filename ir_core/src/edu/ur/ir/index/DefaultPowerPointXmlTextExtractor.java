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

package edu.ur.ir.index;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;

public class DefaultPowerPointXmlTextExtractor implements FileTextExtractor{

	/** Extensions this extractor can extract text for. */
	private Set<String> acceptableFileTypeExtensions = new HashSet<String>();
	
	/**  Logger  */
	private static final Logger log = Logger.getLogger(DefaultPowerPointXmlTextExtractor.class);

	/**Maximum size of file this extractor will try to extract*/
	private long maxFileExtractSizeInBytes = 6000000l;
	
	/**
	 * Default constructor
	 */
	public DefaultPowerPointXmlTextExtractor()
	{
		acceptableFileTypeExtensions.add("pptx");
	}

	/**
	 * Return the file type extensions this extractor can handle
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#getAcceptableFileTypeExtensions()
	 */
	public Set<String> getAcceptableFileTypeExtensions() {
		return Collections.unmodifiableSet(acceptableFileTypeExtensions);
	}


	/**
	 * Extract text from a word 97-2003 document.
	 * @throws Exception 
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#getText(java.io.File)
	 */
	public String getText(File f) throws Exception {
		String text = null;
		if( isFileTooLarge(f) || f.length() <= 0l)
		{
			return text;
		}
		
		OPCPackage p = OPCPackage.open(f.getAbsolutePath(), PackageAccess.READ);
		try
		{
			XSLFSlideShow slideShow = new XSLFSlideShow(p);
			XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(slideShow);

			String myText = extractor.getText();
			if( myText != null && !myText.trim().equals(""))
			{
			    text = myText;
			}

			
		}
		catch(OutOfMemoryError oome)
		{
			text = null;
			log.error("could not extract text", oome);
			throw(oome);
		}
		catch(Exception e)
		{
			text = null;
			log.error("could not get text for word document " + f.getAbsolutePath(), e);
			throw(e);
		}
		finally
		{
			if(p!=null)
			{
				p.revert();
				p = null;
			}
		}
		return text;
	}
	
	/**
	 * Return true if this can handle the specified extension.
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#canExtractText(java.lang.String)
	 */
	public boolean canExtractText(String extension) {
		return acceptableFileTypeExtensions.contains(extension);
	}
	
	/**
	 * Returns true if this can extract text from from the file. 
	 * 
	 * Simply delegates to <code>canCreateDocument(String extension)</code>
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#canExtractText(java.io.File)
	 */
	public boolean canExtractText(File f) {
		return canExtractText(FilenameUtils.getExtension(f.getName())) &&
		!isFileTooLarge(f);
	}  
	
	public long getMaxFileExtractSizeInBytes() {
		return maxFileExtractSizeInBytes;
	}

	public void setMaxFileExtractSizeInBytes(long maxFileExtractSizeInBytes) {
		this.maxFileExtractSizeInBytes = maxFileExtractSizeInBytes;
	}
	
	/**
	 * Returns true if the file is too large to convert.
	 * 
	 * @param f - file to convert.
	 * @return
	 */
	public boolean isFileTooLarge(File f)
	{
		return f.length() > maxFileExtractSizeInBytes;
	}
	
	public static void main ( String[] args) throws Exception
	{
		if( args.length <= 0 )
		{
			System.out.println("Must have a file name with full path");
		}
		else
		{
		    File f = new File(args[0]);
		    System.out.println("File exists = " + f.exists());
		    DefaultPowerPointXmlTextExtractor extractor = new DefaultPowerPointXmlTextExtractor();
		    extractor.getText(f);
		}
	}

}
