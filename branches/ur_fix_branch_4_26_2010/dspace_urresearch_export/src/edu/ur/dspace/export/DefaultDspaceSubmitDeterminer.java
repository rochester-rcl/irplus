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

package edu.ur.dspace.export;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.ur.dspace.model.DspaceUser;

/**
 * Determine if the user can submit to collections.  This does not 
 * determine which collections can be submitted to.
 * 
 * @author Nathan Sarr
 *
 */
public class DefaultDspaceSubmitDeterminer implements DspaceSubmitDeterminer{

	/** jdbc template */
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Datasource for accessing the database.
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
	        this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public boolean canSubmit(DspaceUser user) {
		
		// determine if the user can submit through a group
		String submitCount = "select count(*) from epersongroup, epersongroup2eperson, resourcepolicy" + 
		" where epersongroup.eperson_group_id = epersongroup2eperson.eperson_group_id" +
		" and epersongroup2eperson.eperson_id = " + user.dspaceId +
		" and epersongroup.eperson_group_id = resourcepolicy.epersongroup_id" +
		" and resourcepolicy.resource_type_id = 3" +
		" and action_id = 3";
		
		int count = jdbcTemplate.queryForInt(submitCount);
		
		return count > 0;
	}

}
