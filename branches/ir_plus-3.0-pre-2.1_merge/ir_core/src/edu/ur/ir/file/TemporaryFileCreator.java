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

package edu.ur.ir.file;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * This is an interface for creating temporary files.  The
 * files crated by this class should be set to be deleted on
 * exit of the JVM.  The user should also be able to delete the
 * files if they so choose after they are done using them.  
 * 
 * @author Nathan Sarr
 *
 */
public interface TemporaryFileCreator extends Serializable {
	
	
	/**
	 * Set the directory of where the temporary directory will
	 * be located.
	 * 
	 * @param directory
	 */
	public void setTemporaryDirectory(String directory) ;
	
	/**
	 * Get the temporary directory used by this temporary file creator.
	 * 
	 * @return
	 */
	public String getTemporaryDirectory();
	
	
	/**
	 * Creates a temporary file.  The client should delete this 
	 * file once they are done using it
	 * 
	 * @param extension - extension you wish to give the file.  If it
	 * is null .tmp is used.
	 * 
	 * @return
	 * @throws IOException
	 */
	public File createTemporaryFile(String extension)throws IOException;

}
