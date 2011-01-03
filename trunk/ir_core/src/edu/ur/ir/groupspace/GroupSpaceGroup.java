/**  
   Copyright 2008-2010 University of Rochester

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

package edu.ur.ir.groupspace;

import java.util.List;

import edu.ur.ir.user.IrUser;
/**
 * Represents a group of users within a group space.
 * 
 * @author Nathan Sarr
 *
 */
public class GroupSpaceGroup {
	
	/* list of users in the group */
	private List<IrUser> users;

	public List<IrUser> getUsers() {
		return users;
	}

	void setUsers(List<IrUser> users) {
		this.users = users;
	}

}
