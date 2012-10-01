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

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;

/**
 * Interface for persisting an person name title or other
 * word values.
 * 
 * @author Nathan Sarr
 *
 */
public interface PersonNameTitleDAO extends CountableDAO, 
CrudDAO<PersonNameTitle> {

	/**
	 * Returns all person name title with a person name id like the specified value.
	 * 
	 * @param personNameId
	 * @param startRecord
	 * @param numRecords
	 */
	public List<PersonNameTitle> findPersonNameTitleForPersonNameID(final int personNameId, final int startRecord, final int numRecords);
	

}
