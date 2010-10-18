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
 * Determine if the user is an administator
 * @author nathans
 *
 */
public class DefaultDspaceAdminDeterminer implements DspaceAdminDeterminer{

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
	
	private String adminGroup = "Administrator";
	
	
	public boolean isAdmin(DspaceUser dspaceUser) {
		String select = "select count(*) from epersongroup, epersongroup2eperson" + 
		" where epersongroup.eperson_group_id = epersongroup2eperson.eperson_group_id " +
		" and epersongroup2eperson.eperson_id = " + dspaceUser.dspaceId + 
		" and epersongroup.name = '" + adminGroup + "'" ;
		
		int count = jdbcTemplate.queryForInt(select);
		
		return count == 1;
	}


	public String getAdminGroup() {
		return adminGroup;
	}


	public void setAdminGroup(String adminGroup) {
		this.adminGroup = adminGroup;
	}

}
