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


package edu.ur.hibernate;

import java.util.List;

public class HbHelper {
	
	/**
	 * To be used to retireve a single value.  This is a convience method
	 * and will return a runtime error if more than one object is found
	 * 
	 * @deprecated - use the hibernate session uniqueResult instead
	 * @param list
	 * @return The found object or null if nothing is in the list
	 */
	@SuppressWarnings("unchecked")
	public static Object getUnique(List list)
	{
		Object value = null;
		
		if( list.size()  > 1 )
    	{
    		throw new RuntimeException("Result size should be 1 but is: " + list.size());
    	}
    	else if ( list.size() == 1)
    	{
    		value = list.get(0);
    	}
    	
    	return value;
	}

}
