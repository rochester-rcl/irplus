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


package edu.ur.tag.base.html;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import edu.ur.tag.TagUtil;

/**
 * Implementation of the base tag
 * 
 * @author Nathan Sarr
 *
 */
public class BaseTag extends SimpleTagSupport{
	
	/** default on mouse over action */
	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();
		
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String basePath = request.getScheme() + "://";
		
		if (!TagUtil.isEmpty(request.getServerName())) {
			basePath += request.getServerName() + ":" 
			+ request.getServerPort();
		}
		
		basePath += request.getContextPath() + "/";

		try {
			o.print("<base href=\"" 
					+ basePath +
					"\" />");
		} catch (Exception e) {
			throw new JspException(e);
		}
	}


}
