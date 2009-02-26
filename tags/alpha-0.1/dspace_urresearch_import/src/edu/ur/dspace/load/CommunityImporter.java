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

package edu.ur.dspace.load;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.ur.dspace.model.Community;
import edu.ur.exception.DuplicateNameException;
import edu.ur.ir.IllegalFileSystemNameException;
import edu.ur.ir.repository.Repository;

/**
 * Interface for importing dspace communities into the UR Research System.
 * 
 * @author Nathan Sarr
 *
 */
public interface CommunityImporter {
	
	/**
	 * Import the communities listed in the file specified by the given path.
	 * 
	 * @param path - path to file that contains the zip file that contains the communities and related files.
	 */
	public void ImportCommunities(String zipfile, Repository repository) throws IOException, DuplicateNameException, IllegalFileSystemNameException  ;


	/**
	 * Get the communities out of the zip file.
	 * 
	 * @param zipFile
	 * @return
	 * @throws IOException
	 */
	public List<Community> getCommunities(File zipFile) throws IOException;
}
