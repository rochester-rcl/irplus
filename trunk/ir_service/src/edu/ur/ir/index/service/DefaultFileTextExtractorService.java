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


package edu.ur.ir.index.service;


import java.io.File;
import java.util.List;

import edu.ur.ir.index.FileTextExtractor;
import edu.ur.ir.index.FileTextExtractorService;

/**
 * Default service for managing text extractors.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultFileTextExtractorService implements FileTextExtractorService {

	/** List of extractors to deal with text */
	private List<FileTextExtractor> fileTextExtractors;
	
	/**
	 * Returns the first extractor found that can extract the file based
	 * on extension.
	 * 
	 * @see edu.ur.ir.index.FileTextExtractorService#getFileTextExtractor(java.lang.String)
	 */
	public FileTextExtractor getFileTextExtractor(String extension) {
		for( FileTextExtractor fte : fileTextExtractors)
		{
			if( fte.canExtractText(extension))
			{
				return fte;
			}
		}
		
		return null;
	}

	/**
	 * Get the file text extractor based on the file.
	 * 
	 * @see edu.ur.ir.index.FileTextExtractorService#getFileTextExtractor(java.io.File)
	 */
	public FileTextExtractor getFileTextExtractor(File f) {
		for( FileTextExtractor fte : fileTextExtractors)
		{
			if( fte.canExtractText(f))
			{
				return fte;
			}
		}
		return null;
	}

	/**
	 * Get the list of file extractors.
	 * 
	 * @see edu.ur.ir.index.FileTextExtractorService#getFileTextExtractors()
	 */
	public List<FileTextExtractor> getFileTextExtractors() {
		return fileTextExtractors;
	}

	/**
	 * Set the list of file extractors.
	 * 
	 * @param fileTextExtractors
	 */
	public void setFileTextExtractors(List<FileTextExtractor> fileTextExtractors) {
		this.fileTextExtractors = fileTextExtractors;
	}



}
