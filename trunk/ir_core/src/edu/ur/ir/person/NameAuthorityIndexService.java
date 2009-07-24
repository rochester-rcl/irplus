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

package edu.ur.ir.person;

import java.io.File;

import edu.ur.ir.NoIndexFoundException;


/**
 * Interface for indexing name information.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface NameAuthorityIndexService {
	
	/**
	 * Add the person name to the index.
	 * 
	 * @param personNameAuthority
	 * @param nameAuthorityIndex - index to add to
	 */
	public void addToIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndex) throws NoIndexFoundException;
	
	/**
	 * Update the person names in the index.
	 * 
	 * @param personNameAuthority
	 * @param nameAuthorityIndex - index to to update 
	 */
	public void updateIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndex) throws NoIndexFoundException;
	
	/**
	 * Delete the person names in the index.
	 * 
	 * @param personNameAuthority
	 * @param nameAuthorityIndex - index to delete from 
	 */
	public void deleteFromIndex(PersonNameAuthority personNameAuthority, File nameAuthorityIndex);
	
	/**
	 * Optimize the index.
	 * 
	 * @param name authority index
	 */
	public void optimize(File nameAuthorityIndex);
	
}
