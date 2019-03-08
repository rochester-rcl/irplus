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
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author Nathan Sarr
 * 
 * Document creator for a plain text document.
 *
 */
public class DefaultPlainTextExtractor implements FileTextExtractor{
	
	/** Extensions this extractor can extract text for. */
	private Set<String> acceptableFileTypeExtensions = new HashSet<String>();
	
	/**  Logger */
	private static final Logger log = LogManager.getLogger(DefaultPlainTextExtractor.class);
	
	/**  Buffer size of 1MB. */
	public static final int bufferSize = 1024;
	
	/**Maximum size of file this extractor will try to extract*/
	private long maxFileExtractSizeInBytes = 6000000l;
	
	/**
	 * Default constructor
	 */
	public DefaultPlainTextExtractor()
	{
		acceptableFileTypeExtensions.add("txt");
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
	 * Extract text from the plain text document
	 * @throws Exception 
	 * 
	 * @see edu.ur.ir.index.FileTextExtractor#getText(java.io.File)
	 */
	public String getText(File f) throws Exception{
		String text = null;
		// don't even try if the file is too large
		if( isFileTooLarge(f) )
		{
			return text;
		}
		FileReader reader  = null;
		StringBuffer sb = new StringBuffer();
		char[] buffer = new char[1024];
		
		if( f.exists() && f.isFile() && (f.length() > 0) )
		{
			try
			{
				reader = new FileReader(f);
				int count = 0;

				while (count != -1)
				{
					count = reader.read(buffer, 0, bufferSize);
					// write out those same bytes
					if( count > 0 )
					{
						sb.append(buffer, 0, count);
					}
				}

				if(sb != null && !sb.toString().trim().equals(""))
				{
					text = sb.toString();
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
				log.error("could not create document", e);
				throw(e);
			}
			
			finally
			{
				if( reader != null )
				{
					try
					{
						reader.close();
						reader = null;
					}
					catch(Exception e)
					{
						log.error("could not close reader", e);
						reader = null;
					}
				}
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
	Returns true if this can extract text from from the file. 
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


}
