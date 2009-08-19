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

package edu.ur.ir.statistics;

import edu.ur.dao.CrudDAO;

/**
 * Interface for dealing with File Download rollup inforamtion.
 * 
 * @author Nathan Sarr 
 *
 */
public interface FileDownloadRollUpDAO extends CrudDAO<FileDownloadRollUp>
{

	/**
	 * Roll up the counts for all ir files.
	 * 
	 * @return number of ir files rolled up
	 */
	public Long rollUpCounts();
}
