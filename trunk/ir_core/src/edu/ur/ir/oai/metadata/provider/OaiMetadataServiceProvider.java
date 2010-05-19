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
import java.util.List;

import org.w3c.dom.Element;

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;

/**
 * Interface for an oai provider service
 * 
 * @author Nathan Sarr
 *
 */
public interface OaiMetadataServiceProvider extends Serializable{
	
	/**
	 * List of providers.
	 * 
	 * @return
	 */
	public List<OaiMetadataProvider> getProviders();
	
	/**
	 * Determine if this supports the given metadata prefix.
	 * 
	 * @param metadataPrfix
	 * @return
	 */
	public boolean supports(String metadataPrefix);
	
	/**
	 * Get the provider for the given metadata prefix if one exists otherwise
	 * returns null.
	 * 
	 * @param metadataPrefix - metadata provider 
	 * @return the found provider or null if no provider exists
	 */
	public OaiMetadataProvider getProvider(String metadataPrefix);
	
	/**
	 * Get the given metadata for the given prefix.
	 * 
	 * @param metadataPrefix - metadata prefix
	 * @param record - record element to add the xml to
	 * @param institutionalItemVersion - institutional item version to get the metadata for
	 * @return the metadata
	 * 
	 * @throws CannotDisseminateFormatException if the metadata is not supported
	 */
	public void addXml(String metadataPrefix, Element record, InstitutionalItemVersion institutionalItemVersion) throws CannotDisseminateFormatException;
  
	/**
	 * Add a provider to the list of providers.
	 * 
	 * @param provider
	 */
	public void addProvider(OaiMetadataProvider provider);
	
	/**
	 * Remove a provider from the list.
	 * 
	 * @param provider
	 */
	public void removeProvider(OaiMetadataProvider provider);
	


}
