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

package edu.ur.ir.institution;

import java.sql.Timestamp;

import edu.ur.ir.user.IrUser;

/**
 * Represents a modification by a specific user to an institutional item version.  
 * This is used to track the history of modifications made to an item
 * 
 * @author Nathan Sarr
 *
 */
public class InstitutionalItemVersionModification {
	
	/**  Institutional item version modified */
	private InstitutionalItemVersion institutionalItemVersion;
	
	/** user making the modification */
	private IrUser user;
	
	/** note about the modification */
	private String modificationNote;
	
	/** date the modification was made */
	private Timestamp dateModified;

}
