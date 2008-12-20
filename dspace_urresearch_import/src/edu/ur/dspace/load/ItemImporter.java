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

import edu.ur.dspace.model.DspaceItem;
import edu.ur.ir.NoIndexFoundException;
import edu.ur.ir.repository.Repository;

/**
 * Class to import items from an existing dspace system.
 * 
 * @author Nathan Sarr
 *
 */
public interface ItemImporter {
	
	/**
	 * Import the items listed in the file specified by the given path.
	 * 
	 * @param zipfile - zip file containting the items
	 * @param repository - repository to put the items in
	 * @param publicOnly - only load public items and public files
	 * 
	 * @throws IOException - if reading the file or writing data fails
	 */
	public void importItems(String zipfile, Repository repository, boolean publicOnly) throws IOException,  NoIndexFoundException ;
	

	/**
	 * Get the items out of the zip file.
	 * 
	 * @param zipFile
	 * @return
	 * @throws IOException
	 */
	public List<DspaceItem> getItems(File zipFile) throws IOException;

}
