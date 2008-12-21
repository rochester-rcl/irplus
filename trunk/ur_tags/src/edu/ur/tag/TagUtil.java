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


package edu.ur.tag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;


/**
 * Helper html utilities.
 * 
 * @author Nathan Sarr
 *
 */
public class TagUtil {
	
	/**
	 * Check and see if the value is empty.  Returns
	 * true if the value is null or is an empty string.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value)
	{
		if( value == null  || value.trim().equals("") )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Determine if the value is relative
	 * @param value
	 * @return
	 */
	public static boolean isRelative(String value)
	{
		boolean relative = true;
		
		if( value.startsWith("http:") ||
			value.startsWith("https:") || 
			value.startsWith("ftp:") ||
			value.startsWith("javascript:"))
		{
			
			relative = false;
		}
		
		
		return relative;
	}
	
	/**
	 * If this is a relative path then 
	 * @param context
	 * @param value
	 * @return
	 * @throws ELException 
	 */
	public static String fixRelativePath(String value, PageContext pc) throws ELException
	{
		if(isRelative(value))
		{
			if( value.charAt(0) != '/')
			{
			    return getPageContextPath(pc) + "/" + value;
			}
			else
			{
				return getPageContextPath(pc) + value;
			}
		}
		return value;
	}
	
	/**
	 * Get the context path for the web application.
	 * 
	 * @param pc - page context to use
	 * @return - the context path
	 * 
	 * @throws ELException
	 */
	public static String getPageContextPath(PageContext pc) throws ELException
	{
		String contextPath = (String) pc.getExpressionEvaluator().evaluate("${pageContext.request.contextPath}", 
				String.class, pc.getVariableResolver(), 
				new DefaultFunctionMapper());
		
		return contextPath;
	}
	


}
