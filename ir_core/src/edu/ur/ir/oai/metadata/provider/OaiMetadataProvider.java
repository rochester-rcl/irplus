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
package edu.ur.ir.oai.metadata.provider;


import java.io.Serializable;

import org.w3c.dom.Element;

import edu.ur.ir.institution.DeletedInstitutionalItemVersion;
import edu.ur.ir.institution.InstitutionalItemVersion;

/**
 * Provides the XML metadata output for OAI
 * 
 * @author Nathan Sarr
 *
 */
public interface OaiMetadataProvider extends Serializable{
	
	/**
	 * Get the metadata prefix that this metadata provider provides
	 * 
	 * @return
	 */
	public String getMetadataPrefix();
	
	/**
	 * Get the schema for the provider
	 * 
	 * @return the schema
	 */
	public String getSchema();
	
	/**
	 * Get the namespace for the oai metadata provider.
	 * 
	 * @return
	 */
	public String getMetadataNamespace();
	
	/**
	 * Returns true if this provider supports the given prefix
	 * 
	 * @param metadataPrefix
	 * @return
	 */
	public boolean supports(String metadataPrefix);
	
	/**
	 * Add the xml output for a given institutional item version.
	 * 
	 * @param xml record element to add the information to
	 * @param institutionalItemVersion - the institutional item version to convert
	 */
	public void addXml(Element record, InstitutionalItemVersion institutionalItemVersion);
	
	/**
	 * Add the xml output for a deleted institutional item version.
	 * 
	 * @param xml element to add the xml to
	 * @param institutionalItemVersion
	 */
	public void addXml(Element record, DeletedInstitutionalItemVersion institutionalItemVersion);

}
