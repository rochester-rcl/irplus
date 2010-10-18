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

package edu.ur.ir.user;


/**
 * This is thrown when a user who has deleted the publication is trying to be
 * deleted.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class UserDeletedPublicationException extends Exception{

	/** Eclipse generated id */
	private static final long serialVersionUID = 7901148282319949732L;

    public UserDeletedPublicationException(IrUser user)
    {
    	super("The user " + user + " has deleted publication.");
    }
 
}
