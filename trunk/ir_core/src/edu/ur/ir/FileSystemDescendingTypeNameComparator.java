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

package edu.ur.ir;

import java.io.Serializable;
import java.util.Comparator;

import edu.ur.order.DescendingOrderComparator;

/**
 * Sort according type then name decending
 * @author Nathan Sarr
 *
 */
public class FileSystemDescendingTypeNameComparator implements Comparator<FileSystem>, Serializable{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -852137103828556951L;
	
	private DescendingOrderComparator descendingComparator = new DescendingOrderComparator();

	public int compare(FileSystem arg0, FileSystem arg1) {
		
	    if ( arg1.getFileSystemType().equals(arg0.getFileSystemType()) )
	    {
	    	return arg1.getName().compareToIgnoreCase(arg0.getName());
	    }
	    else
	    {
	        return descendingComparator.compare(arg0.getFileSystemType(), arg1.getFileSystemType());	
	    }
	    
	}


}
