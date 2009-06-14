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


package edu.ur.tag.table.simple;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import edu.ur.tag.TagUtil;
import edu.ur.tag.base.CommonBaseHtmlTag;

/**
 * Caption tag for a table
 * 
 * @author Nathan Sarr
 * 
 */
public class CaptionTag extends CommonBaseHtmlTag {

	/** align the caption tag - use styles instead */
	private String align;

	public void doTag() throws JspException {

		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			o.print("<caption ");
			o.print(getAttributes());
			o.print(">");
			if(body != null)
			{
			    body.invoke(null);
			}
			o.print("</caption>");

		} catch (Exception e) {
			throw new JspException(e);
		}
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
		if (!TagUtil.isEmpty(align)) {
			sb.append("align=\"" + align + "\" ");
		}
		sb.append(super.getAttributes());
		return sb;
	}

}
