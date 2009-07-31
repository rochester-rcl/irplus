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
import java.util.Set;


/**
 * Interface for extracting text from a file.
 * 
 * @author Nathan Sarr
 *
 */
public interface FileTextExtractor {
	
	/**
	 * Set of file types that this extractor will accept.  It is
	 * expected that the file contains the correct content
	 * 
	 * @return
	 */
	public Set<String> getAcceptableFileTypeExtensions();
	
	/**
	 * Extract text from the file
	 * 
	 * @param f - file to create a document for.
	 * 
	 * @return the text or null if the file had no text
	 */
	public String getText(File f);
	
	
	/**
	 * Returns true if the text extractor can extract text from a file
	 * with the extension assuming the file contains content matching
	 * the specified extension.
	 * 
	 * @param extension - extension of the file.
	 */
	public boolean canExtractText(String extension);
	
	/**
	 * Determines if extractor can extract text from the file.  
	 * This can be as simple as using the extension or as complex as looking 
	 * at content within the file.
	 * 
	 * @param f - file that text is to be extracted from
	 * @return - true if the text can extracted
	 */
	public boolean canExtractText(File f);
	
	/**
	 * Returns the max file size this extractor will 
	 * accept and try to process.  If the file is larger than
	 * this size it will not even try to process the file.
	 * 
	 * @return max file extract size in bytes;
	 */
	public long getMaxFileExtractSizeInBytes();

}
