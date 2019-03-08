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

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.ur.ir.FacetSearchHelper;
import edu.ur.ir.search.FacetFilter;
import edu.ur.ir.search.FacetResult;

/**
 * Functions to help with searching
 * 
 * @author Nathan Sarr
 *
 */
public class SearchUtilFunctions {
	
	/**  Get the logger for this class */
	private static final Logger log = LogManager.getLogger(SearchUtilFunctions.class);
	
	/**
	 * Returns true if the facet has been selected by the user
	 * 
	 * @return
	 */
	public static boolean facetSelected(FacetSearchHelper searchHelper, FacetResult facet)
	{
		List<FacetFilter> facetTrail =  searchHelper.getFacetTrail();
		log.debug("facet trail size = " + facetTrail.size());
		for(FacetFilter f : facetTrail)
		{
			log.debug("Comparing filter field " + f.getField().trim() + " to facet " + facet.getField().trim());
			if( f.getField().trim().equals(facet.getField().trim()))
			{
				log.debug("Comparing filter query " + f.getQuery().trim() + " to facet name " + facet.getFacetName().trim());
				if(f.getQuery().trim().equals(facet.getFacetName().trim()))
				{
					return true;
				}
			}
		}
		return false;
	}


}
