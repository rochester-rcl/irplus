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

package edu.ur.ir.search;

import java.util.Comparator;

/**
 * Orders facet results by number of hits.
 * 
 * @author NathanS
 */
public class FacetResultHitComparator implements Comparator<FacetResult>{

	
	public int compare(FacetResult arg0, FacetResult arg1) {
		if (arg1.getHits() < arg0.getHits()) {
			return -1;
		} else if (arg1.getHits() > arg0.getHits()) {
			return 1;
		} else {
			return 0;
		}
	}

}
