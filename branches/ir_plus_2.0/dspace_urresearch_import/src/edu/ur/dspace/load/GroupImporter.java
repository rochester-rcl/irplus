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
import java.text.ParseException;
import java.util.List;

import org.w3c.dom.DOMException;

import edu.ur.dspace.model.DspaceGroup;

/**
 * Interface for importing groups.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupImporter {
	
	/**
	 * Import users in the XML file
	 *  
	 * @param xmlFile - file containing the user data.
	 */
	public void importGroups(File xmlFile) throws DOMException, ParseException, IOException;
	
	/**
	 * Get the users out of the xml file.
	 * 
	 * @param xmlFile
	 * @return
	 */
	public List<DspaceGroup> getGroups(File xmlFile) throws DOMException, ParseException, IOException;

}
