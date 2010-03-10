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

package edu.ur.ir.repository;

import java.util.List;

import edu.ur.dao.CountableDAO;
import edu.ur.dao.CrudDAO;
import edu.ur.dao.UniqueNameDAO;

/**
 * Interface for persisting a repository.
 * 
 * @author Nathan Sarr
 *
 */
public interface RepositoryDAO extends CountableDAO, 
CrudDAO<Repository>, UniqueNameDAO<Repository>
{
	/**
	 * Get available licenses for a repository.
	 * 
	 * @return set of license versions.
	 */
	public List<LicenseVersion> getAvailableRepositoryLicenses(Long repositoryId);
	
	/**
	 * Get the download count for the repository.
	 * 
	 * @return
	 */
	public Long getDownloadCount();
}
