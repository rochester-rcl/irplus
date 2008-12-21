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


package edu.ur.tag;

import java.util.Collection;

/**
 * Utility functions for collections
 * 
 * @author Nathan Sarr
 *
 */
public class CollectionUtil{
	
	/**
	 * Determines if a collection is empty
	 * 
	 * @param collection 
	 * @return true if the collection is empty
	 */
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Collection collection)
	{
		boolean empty = false;
		if( collection == null)
		{
			empty = true;
		}
		else if( collection.size() <= 0)
		{
			empty = true;
		}
		return empty;
	}
	
	/**
	 * Determine the size of a specified collection.
	 * 
	 * @param collection - collection to determine the size of
	 * @return size of the collection
	 */
	@SuppressWarnings("unchecked")
	public static int size(Collection collection)
	{
	    if( collection == null)
	    {
	    	return 0;
	    }
	    else
	    {
	    	return collection.size();
	    }
	}

}
