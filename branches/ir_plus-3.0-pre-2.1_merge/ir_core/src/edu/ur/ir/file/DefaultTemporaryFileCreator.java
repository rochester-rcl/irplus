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
import org.apache.commons.io.FileUtils;;

/**
 * Default implementation for creating temporary files for use in the system for
 * processing.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultTemporaryFileCreator implements TemporaryFileCreator{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -2342604088046253377L;
	
	/** temporary directory to create */
	private String temporaryDirectory;
	
	/**
	 * Creates a temporary file and set the file to be deleted on exit.
	 * 
	 * @see edu.ur.ir.file.TemporaryFileCreator#createTemporaryFile(java.lang.String)
	 */
	public File createTemporaryFile(String extension) throws IOException {
		File tempDir = new File(temporaryDirectory);
		
		if( !tempDir.exists())
		{
			FileUtils.forceMkdir(tempDir);
		}

		if( !tempDir.isDirectory())
		{
			throw new RuntimeException("Temp directory is not a directory " + tempDir.getAbsolutePath());
		}
			
		if( !tempDir.canWrite() )
		{
			throw new RuntimeException("Could not write to directory " + tempDir.getAbsolutePath());
		}
			
		if( !tempDir.canRead())
		{
			throw new RuntimeException("Could not read temp directory " + tempDir.getAbsolutePath());
		}
		File f =  File.createTempFile("ur-temp", extension, tempDir);
		f.deleteOnExit();
		return f;
	}

	/**
	 * Get the temporary directory location as a string.
	 * 
	 * @see edu.ur.ir.file.TemporaryFileCreator#getTemporaryDirectory()
	 */
	public String getTemporaryDirectory() {
		return temporaryDirectory;
	}

	/**
	 * Creates a temporary directory for 
	 * 
	 * @see edu.ur.ir.file.TemporaryFileCreator#setTemporaryDirecotry(java.lang.String)
	 */
	public void setTemporaryDirectory(String directory) {
		this.temporaryDirectory = directory;
	}

}
