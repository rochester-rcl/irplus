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


import edu.ur.ir.institution.InstitutionalItemVersion;

/**
 * Provides the XML metadata output for OAI
 * 
 * @author Nathan Sarr
 *
 */
public interface OaiMetadataProvider {
	
	/**
	 * Get the metadata prefix that this metadata provider provides
	 * 
	 * @return
	 */
	public String getMetadataPrefix();
	
	/**
	 * Returns true if this provider supports the given prefix
	 * 
	 * @param metadataPrefix
	 * @return
	 */
	public boolean supports(String metadataPrefix);
	
	/**
	 * Get the xml output for a given institutional item version.
	 * 
	 * @param institutionalItemVersion - the institutional item version to convert
	 * @return the XML 
	 */
	public String getXml(InstitutionalItemVersion institutionalItemVersion);

}
