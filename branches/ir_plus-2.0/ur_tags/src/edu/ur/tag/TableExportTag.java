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
import javax.servlet.jsp.tagext.SimpleTagSupport;


/**
 * @author Nathan Sarr
 *
 */
public class TableExportTag extends SimpleTagSupport{
	
	private String exportImg;
	private String title;
	private String alt;
	private String href;
	
	public void doTag() throws JspException {
		PageContext context = (PageContext) getJspContext();
		JspWriter out = context.getOut();
		try {
			out.print("<td>");
		
		    out.print("<a href=\"");
		    out.print(href);
			out.print(">");

			out.print("<img src=\"");
			out.print(TagUtil.fixRelativePath(exportImg, context));
			out.print("\" style=\"border: 0pt none ;\" title=\"");
			out.print(title);
			out.print("\" alt=\"");
			out.print(alt);
			out.print("\"/> </a>");
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	}

}
