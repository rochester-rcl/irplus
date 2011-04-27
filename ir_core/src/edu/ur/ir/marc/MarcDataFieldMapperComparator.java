/**  
   Copyright 2008-2011 University of Rochester

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


package edu.ur.ir.marc;

import java.util.Comparator;

/**
 * Implments the comparison between  tow marc data field mappers by data field code.
 * 
 * @author Nathan Sarr
 *
 */
public class MarcDataFieldMapperComparator implements Comparator<MarcDataFieldMapper>{

	
	public int compare(MarcDataFieldMapper o1, MarcDataFieldMapper o2) {
		return o1.getMarcDataField().getCode().compareToIgnoreCase(o2.getMarcDataField().getCode());
	}

}
