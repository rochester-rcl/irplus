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

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;



public class TableToolBarHeaderTag extends SimpleTagSupport{
	
	public void doTag() throws JspException {
	    BasicTableTag tableTag =
            (BasicTableTag)findAncestorWithClass(this,
              BasicTableTag.class);
	    
	    if(tableTag == null)
	    {
	    	throw new JspTagException("the <ur:toolbar> tag must"
	    			+ " be nested within a <ur:table> tag");
	    }
	    
	    TableRowTag rowTag = (TableRowTag) findAncestorWithClass(this,
				TableRowTag.class);
	    
	    if(rowTag == null)
	    {
	    	throw new JspTagException("the <ur:toolbar> tag must"
	    			+ " be nested within a <ur:row> tag");
	    }
	    JspFragment body = getJspBody();
	    PageContext pageContext = (PageContext) getJspContext();
	    JspWriter out = pageContext.getOut();
	    
	    try {
	    	if( body != null && rowTag.isProcessToolBarHeader())
			{
			    out.println("<tr class=\"toolbar\">");
			    out.println("<td colspan=\"" + rowTag.getColumnCount() + "\" align=\"left\">");
			    out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"1\">");
			    out.println("<tbody>");
			    out.println("<tr>");
			    
			    body.invoke(null);
			
			    out.println("</tr>");
			    out.println("</tbody>");
			    out.println("</table>");
	            out.println("</td>");
	            out.println("</tr>");
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspTagException(e);
		}
	    
		
	
	}

}
