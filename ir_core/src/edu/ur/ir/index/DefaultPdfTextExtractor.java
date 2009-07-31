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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * @author Nathan Sarr
 * 
 * Document creator for a pdf document.
 *
 */
public class DefaultPdfTextExtractor implements FileTextExtractor{
	
	/**  Logger for file upload */
	private static final Logger log = Logger.getLogger(DefaultPdfTextExtractor.class);
	
	/** Extensions this extractor can extract text for. */
	private Set<String> acceptableFileTypeExtensions = new HashSet<String>();
	
	/**Maximum size of file this extractor will try to extract*/
	private long maxFileExtractSizeInBytes = 6000000l;
	
	/**
	 * Default constructor
	 */
	public DefaultPdfTextExtractor()
	{
		acceptableFileTypeExtensions.add("pdf");
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
	 * Extract text from the PDF document
	 * @throws Exception 
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#getText(java.io.File)
	 */
	public String getText(File f) throws Exception {
		String text = null;
		
		// don't even try if the file is too large
		if( isFileTooLarge(f))
		{
			return text;
		}
		PDDocument pdDoc = null;
		try
		{
			pdDoc = PDDocument.load(f);
			
			// don't do anything with decripted docs
			if( !pdDoc.isEncrypted())
			{
			    PDFTextStripper stripper = new PDFTextStripper();
			    String myText = stripper.getText(pdDoc);
			    
			    if( myText != null && !myText.trim().equals(""))
			    {
			        text = myText;
			    }
			  
			}
			else
			{
				log.error("pdf " + f.getAbsolutePath() + " is encrypted and " +
						" cannot be decrypted because we don't have a password");
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
			log.error("could not extract text with other error", e);
			text = null;
			throw(e);
		}
		finally
		{
			closePDDocument(pdDoc);
			pdDoc = null;
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
	
	
	/**
	 * Close the PD document
	 * 
	 * @param cosDoc
	 */
	private void closePDDocument(PDDocument pdDoc)
	{
		if( pdDoc != null )
		{
			try
			{
				pdDoc.close();
				pdDoc = null;
			}
			catch(Exception e)
			{
				log.error("could not close PDDocument", e);
				pdDoc = null;
			}
		}
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

}
