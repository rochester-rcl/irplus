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

package edu.ur.ir;

/**
 * Exception thrown when illegal characters are found within a 
 * specified name.
 * 
 * @author Nathan Sarr
 *
 */
public class IllegalFileSystemNameException extends Exception{
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3278307555433489566L;

	/**  Set of characters that are not allowed */
	private char[] illegalCharacters;
	
	/** The illegal character found */
	private char illegalCharacter;
	
	/** Name used that contains the illegal character */
	private String name;
	
	public IllegalFileSystemNameException(char[] illegalCharacters, char illegalCharacter, String name)
	{
		this.illegalCharacters = illegalCharacters;
		this.illegalCharacter = illegalCharacter;
		this.name = name;
	}

	public char[] getIllegalCharacters() {
		return illegalCharacters;
	}

	public void setIllegalCharacters(char[] illegalCharacters) {
		this.illegalCharacters = illegalCharacters;
	}

	public char getIllegalCharacter() {
		return illegalCharacter;
	}

	public void setIllegalCharacter(char illegalCharacter) {
		this.illegalCharacter = illegalCharacter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
