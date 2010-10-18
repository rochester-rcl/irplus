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
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * @author Nathan Sarr
 * 
 * Document creator for a rich text document.
 *
 */
public class DefaultRtfTextExtractor implements FileTextExtractor {
	
	/** Extensions this extractor can extract text for. */
	private Set<String> acceptableFileTypeExtensions = new HashSet<String>();
	
	/**  Logger */
	private static final Logger log = Logger.getLogger(DefaultPlainTextExtractor.class);

	/**Maximum size of file this extractor will try to extract*/
	private long maxFileExtractSizeInBytes = 6000000l;


	/**
	 * Default constructor
	 */
	public DefaultRtfTextExtractor()
	{
		acceptableFileTypeExtensions.add("rtf");
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
	 * Extract text from the Rich text file document 
	 * @throws Exception 
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#getText(java.io.File)
	 */
	public String getText(File f) throws Exception {
		String text = null;
		// don't even try if the file is too large
		if( isFileTooLarge(f) || f.length() <= 0l)
		{
			return text;
		}
		DefaultStyledDocument styledDoc = new DefaultStyledDocument();
		RTFEditorKit editorKit = new RTFEditorKit();
		FileInputStream inputStream = null;
		
		try
		{
		    inputStream = new FileInputStream(f);
		    editorKit.read(inputStream, styledDoc, 0);
		    String myText = styledDoc.getText(0, styledDoc.getLength());
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
			log.error("could not get text for rich text document " + f.getAbsolutePath(), e);
			throw(e);
		}
		
		finally
		{
			closeInputStream(inputStream);
			editorKit = null;
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
	 * Close the input stream.
	 * 
	 * @param is - input of the stream to close
	 */
	private void closeInputStream(InputStream is)
	{
		try
		{
			if( is != null)
			{
				is.close();
				is = null;
			}
		}
		catch(Exception e)
		{
			log.error("could not close stream ", e);
			is = null;
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
