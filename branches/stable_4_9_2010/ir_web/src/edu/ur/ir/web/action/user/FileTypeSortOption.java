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


package edu.ur.ir.web.action.user;

import java.util.Comparator;

import edu.ur.ir.FileSystem;

/**
 * A file type sort option
 * 
 * @author Nathan Sarr
 *
 */
public class FileTypeSortOption {
	
	/** Name to display to the user */
	private String name;
	
	/** Value to send when chosen */
	private String value;
	
	/** Comparator to use when this option is chosen */
	private Comparator<FileSystem> comparator;
	
	/**
	 * Default constructor.
	 * 
	 * @param name
	 * @param value
	 */
	public FileTypeSortOption(String name, String value, Comparator<FileSystem> comparator)
	{
		setValue(value);
		setName(name);
		setComparator(comparator);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String id) {
		this.value = id;
	}

	public Comparator<FileSystem> getComparator() {
		return comparator;
	}

	public void setComparator(Comparator<FileSystem> comparator) {
		this.comparator = comparator;
	}

}
