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

package edu.ur.dspace.export;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import edu.ur.dspace.model.DspaceGroup;

/**
 * Class for exporting groups.
 * 
 * @author Nathan Sarr
 *
 */
public interface GroupExporter {
	
	/**  the file name for groups */
	public static final String XML_FILE_NAME = "groups.xml";
	
	/**
	 * Get the dspace groups
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<DspaceGroup> getGroups();
	
	/**
	 * Generate an xml file with the specified dspace groups
	 * 
	 * @param f - file to write the XML for
	 * @param users - set of users to add to the xml file
	 * @throws IOException
	 */
	public void generateGroupXMLFile(File f, Collection<DspaceGroup> groups) throws IOException;
	

	

}
