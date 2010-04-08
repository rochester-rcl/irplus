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


/**
 * Sorts file system objects in asending name order.
 * 
 * @author Nathan Sarr
 *
 */
public class FileSystemAscendingNameComparator implements Comparator<FileSystem>, Serializable{

	/** Eclipse genrated id */
	private static final long serialVersionUID = 5167580184926839029L;

	public int compare(FileSystem arg0, FileSystem arg1) {
		return arg0.getName().compareToIgnoreCase(arg1.getName());
	}

}
