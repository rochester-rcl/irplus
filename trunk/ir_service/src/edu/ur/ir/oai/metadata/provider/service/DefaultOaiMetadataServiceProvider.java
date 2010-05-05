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
package edu.ur.ir.oai.metadata.provider.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.institution.InstitutionalItemVersion;
import edu.ur.ir.oai.exception.CannotDisseminateFormatException;
import edu.ur.ir.oai.metadata.provider.OaiMetadataProvider;
import edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider;

/**
 * Default Implementation for an oai metadata service provider.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultOaiMetadataServiceProvider implements OaiMetadataServiceProvider{
    
	/** eclipse generated id */
	private static final long serialVersionUID = 3096747633058885629L;
	
	/** List of oai metadata providers */
	private List<OaiMetadataProvider> providers = new LinkedList<OaiMetadataProvider>();
	
	/**
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#getProvider(java.lang.String)
	 */
	public OaiMetadataProvider getProvider(String metadataPrefix) {
		for( OaiMetadataProvider provider : providers)
		{
			if( provider.supports(metadataPrefix))
			{
				return provider;
			}
		}
		return null;
	}

	/**
	 * Returns an unmodifiable list of providers.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#getProviders()
	 */
	public List<OaiMetadataProvider> getProviders() {
		return Collections.unmodifiableList(providers);
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#getXml(java.lang.String)
	 */
	public String getXml(String metadataPrefix, InstitutionalItemVersion institutionalItemVersion)
			throws CannotDisseminateFormatException {
		
		if( !supports(metadataPrefix))
		{
			throw new CannotDisseminateFormatException("format " + metadataPrefix + "not supported");
		}
		for( OaiMetadataProvider provider : providers)
		{
			if( provider.supports(metadataPrefix))
			{
				return provider.getXml(institutionalItemVersion);
			}
		}
		return "";
		
	}

	/**
	 * Determine a metadata prefix is supported.
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#supports(java.lang.String)
	 */
	@Override
	public boolean supports(String metadataPrefix) {
		for( OaiMetadataProvider provider : providers)
		{
			if( provider.supports(metadataPrefix))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#addProvider(edu.ur.ir.oai.metadata.provider.OaiMetadataProvider)
	 */
	public void addProvider(OaiMetadataProvider provider) {
		providers.add(provider);
	}

	/**
	 * @see edu.ur.ir.oai.metadata.provider.OaiMetadataServiceProvider#removeProvider(edu.ur.ir.oai.metadata.provider.OaiMetadataProvider)
	 */
	public void removeProvider(OaiMetadataProvider provider) {
		providers.remove(provider);
	}
	
	/**
	 * Set the list of providers.
	 * 
	 * @param providers
	 */
	public void setProviders(List<OaiMetadataProvider> providers) {
		this.providers = providers;
	}
	
}
