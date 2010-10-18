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

package edu.ur.file;

import java.util.LinkedList;
import java.util.List;

/**
 * Exception thrown when illegal characters are found within a 
 * specified name.
 * 
 * @author Nathan Sarr
 *
 */
public class IllegalFileSystemNameException extends Exception{
	

	
	/** Illegal characters in name */
	public static final char[] INVALID_CHARACTERS = {'\\','/', ':', '*', '?', '\"', '<', '>', '|'};
	
	/** eclipse generated id */
	private static final long serialVersionUID = -3278307555433489566L;

	/** The illegal character found */
	private List<Character> illegalCharactersFound;
	
	/** Name used that contains the illegal character */
	private String name;
	
	
	/**
	 * Returns the list of illegal characters found
	 * 
	 * @param name - name to check
	 * @return - the list of illegal characters found
	 */
	public static List<Character> nameHasIllegalCharacerter(String name)
	{
		LinkedList<Character> illegalCharactersFound = new LinkedList<Character>();
		
		for(int i = 0; i < IllegalFileSystemNameException.INVALID_CHARACTERS.length; i++) {
			if (name.contains(Character.toString(IllegalFileSystemNameException.INVALID_CHARACTERS[i]))) {
				illegalCharactersFound.add(IllegalFileSystemNameException.INVALID_CHARACTERS[i]);
			}
		}
		return illegalCharactersFound;
	}
	
	/**
	 * Throw the exception with the list of illegal characters found.
	 * 
	 * @param illegalCharacter
	 * @param name
	 */
	public IllegalFileSystemNameException(List<Character> illegalCharacters, String name)
	{
		this.illegalCharactersFound = illegalCharacters;
		this.name = name;
	}

	/**
	 * Get the list of illegal characters - this is the same as the static list.
	 * 
	 * @return
	 */
	public char[] getIllegalCharacters() {
		return INVALID_CHARACTERS;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Character> getIllegalCharactersFound() {
		return illegalCharactersFound;
	}

	public void setIllegalCharactersFound(List<Character> illegalCharactersFound) {
		this.illegalCharactersFound = illegalCharactersFound;
	}

}
