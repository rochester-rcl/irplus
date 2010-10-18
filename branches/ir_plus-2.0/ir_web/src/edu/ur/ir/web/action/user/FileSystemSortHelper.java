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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.ur.ir.FileSystem;
import edu.ur.ir.FileSystemAscendingNameComparator;
import edu.ur.ir.FileSystemAscendingTypeNameComparator;
import edu.ur.ir.FileSystemDescendingNameComparator;
import edu.ur.ir.FileSystemDescendingTypeNameComparator;

/**
 * Helper class to deal with sorting file system objects
 * 
 * @author Nathan Sarr
 *
 */
public class FileSystemSortHelper {
	
	public static final FileTypeSortOption NAME_ASC = 
		new FileTypeSortOption("Name Ascending", "name_asc", new FileSystemAscendingNameComparator());
	public static final FileTypeSortOption NAME_DESC = 
		new FileTypeSortOption("Name Descending", "name_desc", new FileSystemDescendingNameComparator());
	public static final FileTypeSortOption TYPE_ASC = 
		new FileTypeSortOption("Type Ascending", "type_asc", new FileSystemAscendingTypeNameComparator());
	public static final FileTypeSortOption TYPE_DESC = 
		new FileTypeSortOption("Type Descending", "type_desc", new FileSystemDescendingTypeNameComparator());
	
	/** the sort type  */
	private FileTypeSortOption selectedSortType;
	
	/** set of sort options to display to the user */
	private LinkedList<FileTypeSortOption> fileTypeSortOptions = 
		new LinkedList<FileTypeSortOption>();
	
	/** Default sort to use if selected sort type not found */
	private FileTypeSortOption defaultSort = TYPE_DESC;
	
	
	public FileSystemSortHelper()
	{
	    fileTypeSortOptions.add(NAME_ASC);
	    fileTypeSortOptions.add(NAME_DESC);
	    fileTypeSortOptions.add(TYPE_ASC);
	    fileTypeSortOptions.add(TYPE_DESC);
	}
		
	public void sort(List<FileSystem> fileSystem, FileTypeSortOption sortOption)
	{
		Collections.sort(fileSystem, sortOption.getComparator());
	}
	
	
	public LinkedList<FileTypeSortOption> getSortOptions()
	{
		return fileTypeSortOptions;
	}

	public FileTypeSortOption getSelectedSortType() {
		return selectedSortType;
	}

	public void setSelectedSortType(FileTypeSortOption selectedSortType) {
		this.selectedSortType = selectedSortType;
	}

	public LinkedList<FileTypeSortOption> getFileTypeSortOptions() {
		return fileTypeSortOptions;
	}

	public void setFileTypeSortOptions(
			LinkedList<FileTypeSortOption> fileTypeSortOptions) {
		this.fileTypeSortOptions = fileTypeSortOptions;
	}

	public FileTypeSortOption getDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(FileTypeSortOption defaultSort) {
		this.defaultSort = defaultSort;
	}

}
