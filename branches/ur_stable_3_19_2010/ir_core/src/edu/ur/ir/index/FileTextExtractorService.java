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
import java.util.List;

/**
 * Service class for using and managing text extractors
 * 
 * @author Nathan Sarr
 *
 */
public interface FileTextExtractorService {
	
	
	/**
	 * Get the text extractors managed by this service.
	 * 
	 * @return the list of file document creators.
	 */
	public List<FileTextExtractor> getFileTextExtractors();
	
	
	/**
	 * Get a text extractor for the specified extension.
	 * 
	 * @param extension - extension for the document type.
	 * @return the document creator for the specified file document or
	 * null if a creator could not be found.
	 */
	public FileTextExtractor getFileTextExtractor(String extension);
	
	
	/**
	 * Get the text extractor for the specified file.
	 * 
	 * @param f - file to get the extractor for.
	 * @return - the extractor null if an extractor could not be found.
	 */
	public FileTextExtractor getFileTextExtractor(File f);

}
