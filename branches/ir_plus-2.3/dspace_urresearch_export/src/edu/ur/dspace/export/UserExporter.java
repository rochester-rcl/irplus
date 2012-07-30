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

import edu.ur.dspace.model.DspaceUser;

/**
 * Export DSpace user information for importing into the urresearch system.
 * 
 * @author Nathan Sarr
 *
 */
public interface UserExporter {
	
	/**
	 * the file name for users
	 */
	public static final String XML_FILE_NAME = "users.xml";
	
	/**
	 * Get the dspace users from the dspace database
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<DspaceUser> getUsers();
	
	/**
	 * Generate an xml file with the specified dspace users.
	 * 
	 * @param f - file to write the XML for
	 * @param users - set of users to add to the xml file
	 * @throws IOException
	 */
	public void generateUserXMLFile(File f, Collection<DspaceUser> users) throws IOException;
	

	/**
	 * For all users, use the specified password - this is good for testing and loading data while still protecting
	 * user password data.
	 * 
	 * @param f- file to write the XML for
	 * @param users - collection of users
	 * @param testDataPassword
	 * @throws IOException
	 */
	public void generateUserXMLFile(File f, Collection<DspaceUser> users, String testDataPassword) throws IOException;

	


}
