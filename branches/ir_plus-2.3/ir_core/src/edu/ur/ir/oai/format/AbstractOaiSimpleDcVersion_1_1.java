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

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract implementation where all return an empty linked list.
 * 
 * @author NathanS
 *
 */
public class AbstractOaiSimpleDcVersion_1_1 implements OaiSimpleDcVersion_1_1{

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getContributors()
	 */
	public List<String> getContributors() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getCoverages()
	 */
	public List<String> getCoverages() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getCreators()
	 */
	public List<String> getCreators() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getDates()
	 */
	public List<String> getDates() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getDescriptions()
	 */
	public List<String> getDescriptions() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getFormats()
	 */
	public List<String> getFormats() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getIdentifiers()
	 */
	public List<String> getIdentifiers() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getLanguages()
	 */
	public List<String> getLanguages() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getPublishers()
	 */
	public List<String> getPublishers() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getRelations()
	 */
	public List<String> getRelations() {
		return new LinkedList<String> ();
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getRights()
	 */
	public List<String> getRights() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getSources()
	 */
	public List<String> getSources() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getSubjects()
	 */
	public List<String> getSubjects() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getTitles()
	 */
	public List<String> getTitles() {
		return new LinkedList<String> ();
	}

	/**
	 * @see edu.ur.ir.oai.format.OaiSimpleDcVersion_1_1#getTypes()
	 */
	public List<String> getTypes() {
		return new LinkedList<String> ();
	}

}
