/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.oai.format;

import java.util.List;


/**
 * This represents a container which will return simple Dublin Core metadata elements version 1.1
 * 
 * @author Nathan Sarr
 *
 */
public interface OaiSimpleDcVersion_1_1 {
	
	/**
	 * Title of the resource.
	 * 
	 * @return list of titles
	 */
	public List<String> getTitles();
	
	/**
	 * An entity primarily responsible for making the content of the resource.
	 * 
	 * @return list of creators
	 */
	public List<String> getCreators();
	
	/**
	 * The keywords for the resource.
	 * 
	 * @return list of subjects
	 */
	public List<String> getSubjects();
	
	/**
	 * Description of the content of the resource.
	 * 
	 * @return list of descriptions
	 */
	public List<String> getDescriptions();
	
	/**
	 * Responsible for making the resource available
	 * 
	 * @return list of publishers
	 */
	public List<String> getPublishers();
	
	/**
	 * List of dublin core contributors
	 * 
	 * @return contributors to be output
	 */
	public List<String> getContributors();
	
	/**
	 * The date the resource was created
	 * 
	 * @return dates to be output
	 */
	public List<String> getDates();
	
	
	/**
	 * The type of content
	 * 
	 * @return
	 */
	public List<String> getTypes();
	
	/**
	 * The format the resource is in
	 * 
	 * @return
	 */
	public List<String> getFormats();
	
	/**
	 * List of identifiers for the item.
	 * 
	 * @return
	 */
	public List<String> getIdentifiers();
	
	/**
	 * The sources for the resource
	 * @return
	 */
	public List<String> getSources();
	
	/**
	 * Languages used in the resource
	 * 
	 * @return
	 */
	public List<String> getLanguages();
	
	/**
	 * List of relationships for the resource
	 * 
	 * @return
	 */
	public List<String> getRelations();
	
	/**
	 * Coverages for the resource.
	 * 
	 * @return
	 */
	public List<String> getCoverages();

	
	/**
	 * Information rights for the resource.
	 * 
	 * @return
	 */
	public List<String> getRights();
	
	
}
