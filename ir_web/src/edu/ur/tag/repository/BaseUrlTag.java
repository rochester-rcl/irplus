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

package edu.ur.tag.repository;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Tag that outputs the base url for the web application.  This assumes
 * that the base url is the same for the request object.
 * 
 * @author Nathan Sarr
 *
 */
public class BaseUrlTag extends SimpleTagSupport{
	
	
	public void doTag() throws JspException
	{
		JspWriter out = this.getJspContext().getOut();
		String baseUrl = "";
		
		PageContext context = (PageContext) getJspContext();
		HttpServletRequest request = (HttpServletRequest)context.getRequest();
		baseUrl =  request.getScheme() + "://";
		baseUrl += request.getServerName();
		if( request.getServerPort() != 80 )
		{
			baseUrl += ":" + request.getServerPort();
		}
			
		if( request.getContextPath() != null && !request.getContextPath().trim().equals(""))
		{
			baseUrl += 	request.getContextPath();
		}
		
		baseUrl +="/";
		
	    
	    try {
			out.write(baseUrl);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

}
