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


package edu.ur.tag.repository;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;


import edu.ur.ir.search.FacetResult;

/**
 * Tag to output a specified facet.
 * 
 * @author Nathan Sarr
 *
 */
public class FacetTag extends SimpleTagSupport{
	
	/** Name for the facet  */
	private String key;
	
	/** variable to set with the collection of values  */
	private String var;
	
	/** Set of facets to iterate through */
	private HashMap<String, Collection<FacetResult>> facets;

	
	public void doTag() throws JspException {
		Collection<FacetResult> facetSet = null;
		if( facets != null )
		{
		    facetSet = facets.get(key);
            PageContext pageContext = (PageContext)this.getJspContext();
            pageContext.setAttribute(var, facetSet);
		}
		
	    try
		{
	    	if( getJspBody() != null )
	    	{
		        getJspBody().invoke(null);
	    	}
		}
		catch(Exception e)
		{
			throw new JspException("could not invoke body",e);
		}
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getVar() {
		return var;
	}


	public void setVar(String var) {
		this.var = var;
	}


	public HashMap<String, Collection<FacetResult>> getFacets() {
		return facets;
	}


	public void setFacets(HashMap<String, Collection<FacetResult>> facets) {
		this.facets = facets;
	}

}
