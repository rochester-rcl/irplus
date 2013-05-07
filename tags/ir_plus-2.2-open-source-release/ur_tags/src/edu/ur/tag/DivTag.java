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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

/**
 * Implementation of the div tag.
 * 
 * @author Nathan Sarr
 *
 */
public class DivTag extends KeyMouseCommonTag {
	
	/**  Writer for this tag  */
	private JspWriter o;
	
	/**
	 * Create the form tag.
	 * 
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	public void doTag() throws JspException {
		
		PageContext context = (PageContext) getJspContext();
		o = context.getOut();
		JspFragment body = getJspBody();
		try
		{
		    o.print("<div " + getAttributes().toString() + ">");
		    if(body != null)
		    {
		    	body.invoke(null);
		    }
		    o.print("</div>");
		    
		}
		catch(Exception e)
		{
			throw new JspException("error occured in DivTag", e);
		}
		
	
	}
	
	public StringBuffer getAttributes() {
		return super.getAttriubes();
	} 

}
