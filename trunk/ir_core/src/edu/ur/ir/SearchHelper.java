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

import java.util.StringTokenizer;


/**
 * Helps with fixing any search string for lucene.
 * 
 * @author Nathan Sarr
 *
 */
public class SearchHelper {
	
	
	/**
	 * Prepares a string for searching - this removes problamatic characters.
	 * 
	 * @param value - value to test
	 * @param wildCardTearms - escape wild card terms for example * and ?
	 * @return
	 */
	public static String prepareMainSearchString(String value, boolean wildCardTerms)
	{
		int count = count(value, "\"");
		
		// unbalanced quotes or no quotes
		if( count == 0 || ((count % 2) != 0) )
		{
			
			return fixMainSearchStringSegment(value, wildCardTerms);
		}
		else
		{
			return fixOnlyNonQuoted(value, wildCardTerms);
		}

		
	}
	
	
	/**
	 * Fixes a main search string segment.
	 * 
	 * @param value
	 * @return
	 */
	private static String fixMainSearchStringSegment(String value, boolean wildCardTerms)
	{
	    String newValue = value;
		
		newValue = fixWildCardCharacters(newValue, '*');
		newValue = fixWildCardCharacters(newValue, '?');
        
		// this must be the first one checked
        newValue = escapeSpecialCharacters(newValue, "\\");
        
      
		newValue = escapeSpecialCharacters(newValue, "+");
		newValue = escapeSpecialCharacters(newValue, "(");
		newValue = escapeSpecialCharacters(newValue, ")");
		newValue = escapeSpecialCharacters(newValue, "{");
		newValue = escapeSpecialCharacters(newValue, "}");
		newValue = escapeSpecialCharacters(newValue, "[");
		newValue = escapeSpecialCharacters(newValue, "]");
		
		newValue = escapeSpecialCharacters(newValue, "^");
		newValue = escapeSpecialCharacters(newValue, ":");
		
		newValue = escapeSpecialCharacters(newValue, "-");
		newValue = escapeSpecialCharacters(newValue, "&&");
		newValue = escapeSpecialCharacters(newValue, "||");
		newValue = escapeSpecialCharacters(newValue, "!");
		newValue = escapeSpecialCharacters(newValue, "~");
		newValue = escapeSpecialCharacters(newValue, "\"");
	
	
		
		//remove beginning and ending spaces
		newValue = newValue.trim();
		
		// single term wild card, no spaces in the term and not already wild card
		if( wildCardTerms &&
			!newValue.contains("."))
		{
			
			String[] values = newValue.split("\\s");
			if( values.length > 0 )
			{
			    String tempValue = "";
			    for(String v : values)
			    {
				    // cannot have " before wild card don't need to add another * 
			    	// if one already exists
			    	// do not apply * to any string less than 3 characters - otherwise two many clauses exception
				    if( v.lastIndexOf('"') != (v.length() -1)
					    && v.lastIndexOf('*') != (v.length() -1 )&& !containsSpecialCharacter(v) &&  v.length() >= 3)
				    {
					        tempValue = tempValue + " " + v + "*"; 
				    }
				    else
				    {
				    	tempValue = tempValue + " " + v;
				    }
			    }
			    newValue = tempValue.trim();
			   
			}
			else if( newValue.length() > 0 && newValue.lastIndexOf('*') != (newValue.length() -1 ) && !containsSpecialCharacter(newValue) )
			{
				newValue = newValue + "*";
			}
		}
		
		
		return newValue;
	}
	
	/**
	 * Determines if the string contains a special character.
	 * 
	 * @param value - value to check
	 * @return
	 */
	private static boolean containsSpecialCharacter(String value)
	{
		return  value.contains("?") ||
	    value.contains("\\") ||
	    value.contains("+") ||
	    value.contains("(") ||
	    value.contains(")") ||
	    value.contains("{") ||
	    value.contains("}") ||
	    value.contains("[") ||
	    value.contains("]") ||
	    value.contains("^") ||
	    value.contains(":") ||
	    value.contains("&&") ||
	    value.contains("||") ||
	    value.contains("!") ||
	    value.contains("~") ||
	    value.contains("/") ||
	    value.contains("-"); 
	
	}
	
	/**
	 * This only fixes non quoted text
	 * @param value
	 * @param wildCardTerms
	 * @return
	 */
	private static String fixOnlyNonQuoted(String value, boolean wildCardTerms )
	{
		final String quotes = "\"";
		String newValue = "";
		int beginIndex = 0;
		int toIndex = 0;
	
		
		// get the indexes of the quotes
		beginIndex = value.indexOf(quotes, beginIndex);
		
		if( beginIndex == -1)
		{
			newValue = value;
		}
		
		while( beginIndex != -1)
		{
			// get the end quote position
			toIndex = value.indexOf(quotes, beginIndex + 1);
			
			// set the newValue to this
			newValue = newValue + value.substring(beginIndex, toIndex + 1);
			
			// not at end of string  get segment to fix
			if( toIndex != (value.length() -1))
			{
				// get the next begin index (next start quotes)
				beginIndex = value.indexOf(quotes, toIndex + 1);
				
				String subString = "";
				if( beginIndex == -1)
				{
					subString = value.substring(toIndex + 1, value.length());
				}
				else
				{
					subString = value.substring(toIndex + 1, beginIndex);
				}
			
				String fixedValue = fixMainSearchStringSegment(subString, wildCardTerms);
				if( (subString.length() > 1))
				{
					if( subString.charAt(0) == ' ' )
					{
						fixedValue = " " + fixedValue;
					}
					if( subString.charAt(subString.length() - 1) == ' ' )
					{
					     fixedValue = fixedValue + " ";
					}
					
				}
				
				newValue = newValue + fixedValue;
			}
			else
			{
				beginIndex =  -1;
			}
			   
		}
		return newValue;
		
	}
	
	
	
	/**
	 * Count the number of times a character appears 
	 * 
	 * @param value
	 * @return number of times a character appears in the string
	 */
	public static int count(String value, String valueToCount)
	{
		int count = 0;
		int fromIndex = 0;
		
		fromIndex = value.indexOf(valueToCount,fromIndex );
		while( fromIndex != -1)
		{
			fromIndex = value.indexOf(valueToCount,fromIndex + 1);
			
			count = count + 1;
		}
		
		
		return count;
	}
	
	
	/**
	 * Prepares a string for searching - this removes problamatic characters.
	 * 
	 * @param value
	 * @return
	 */
	public static String prepareFacetSearchString(String value, boolean wildCardTerms)
	{
		return fixMainSearchStringSegment(value, wildCardTerms);
	}
	
	
	/**
	 * Fix wild card characters like * and ? 
	 * 
	 * @param str - string to deal with 
	 * @param wildcard - wild card character
	 */
	private static String fixWildCardCharacters(String str, char wildcard)
	{
		if( !str.contains(wildcard + ""))
		{
			return str;
		}
		// determine if the * is final character
		int lastIndex = str.lastIndexOf(" " + wildcard);
		if(lastIndex == (str.length() - 2))
		{
			str = str.substring(0, str.length() - 1);
		}
		
		// remove any leading star within the string 
		// for example "this is *a test"
		int index = str.indexOf(" " + wildcard);
		while( index != -1)
		{
			str = str.substring(0, index + 1) + str.substring(index+2, str.length()-1);
			index = str.indexOf(" " + wildcard);
		}
		

		// remove beginning *
		if(str.indexOf(wildcard) == 0)
		{
			str = str.substring(1);
		}
		
		return str;
		
	}
	
	/**
	 * Escape the special characters
	 * 
	 * @param str - sting that has special characters, the characters that are special
	 * @param theChars
	 * @return the string cleaned up
	 */
	private static String escapeSpecialCharacters(String str, String theChars )
	{
		if( !str.contains(theChars))
		{
			return str;
		}
		String finalString = "";
		//escapce all other instances of the character
		StringTokenizer tokenizer = new StringTokenizer(str, theChars, true);
		
       
		while( tokenizer.hasMoreElements())
		{
			String token = tokenizer.nextToken();
			if( token.equals(theChars))
			{
				finalString = finalString + "\\" + theChars;
			}
			else
			{
				finalString = finalString + token ;
			}
	
		}
		
		return finalString;
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(SearchHelper.prepareFacetSearchString(args[0], false));
	}

}
