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

import java.io.File;
import java.io.Serializable;
import java.util.Set;

import edu.ur.simple.type.DescriptionAware;
import edu.ur.simple.type.NameAware;

/**
 * Interface for transforming a given file into another file.  
 * 
 * @author Nathan Sarr
 *
 */
public interface FileTransformer extends DescriptionAware, NameAware, Serializable{
	
	
	/**
	 * The extension that is normally associated with the 
	 * type of file this transformer will create.
	 * 
	 * @return
	 */
	public String getFileExtension();
	
	/**
	 * Set of file types that this file transformer will accept.
	 * 
	 * @return
	 */
	public Set<String> getAcceptableFileTypeExtensions();
	
	/**
	 * Create a transformation of the given file and write it out to the transformed file.
	 * 
	 * @param inFile - file to make a transform of.
	 * @param transformedFile - the file to save the transformation to
     *	
	 */
	public void transformFile(File inFile, String inFileExtension, File transformedFile) throws Exception;
	
	
	/**
	 * Indicates that this file transformer can transform the file if it
	 * contents match the indicated extension.
	 * 
	 * @param extension for the file
	 * @return true if the file can be transformed
	 */
	public boolean canTransform(String extension);

}
