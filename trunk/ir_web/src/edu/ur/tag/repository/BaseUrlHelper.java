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

package edu.ur.tag.repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * Class to help get the base url.
 * 
 * @author Nathan Sarr
 *
 */
public class BaseUrlHelper {

	/**
	 * Get the base URL.
	 * 
	 * @param context
	 * @return the base url.
	 */
	public static String getBaseUrl(PageContext context)
	{
		HttpServletRequest request = (HttpServletRequest)context.getRequest();
		String baseUrl =  request.getScheme() + "://";
		baseUrl += request.getServerName();
		if( request.getServerPort() != 80 && request.getServerPort() != 443)
		{
			baseUrl += ":" + request.getServerPort();
		}
			
		if( request.getContextPath() != null && !request.getContextPath().trim().equals(""))
		{
			baseUrl += 	request.getContextPath();
		}
		
		baseUrl +="/";
		
		return baseUrl;
	}
}
