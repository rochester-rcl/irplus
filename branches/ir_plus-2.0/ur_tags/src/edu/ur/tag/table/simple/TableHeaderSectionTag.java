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
 * Table header tag
 * 
 * @author Nathan Sarr
 * 
 */
public class TableHeaderSectionTag extends CommonBaseHtmlTag {

	/** text alignment in cells */
	private String align;

	/** character to align text on */
	private String aChar;

	/** offset to the first character */
	private String charOff;

	/** vertical alignment */
	private String valign;

	public void doTag() throws JspException {

		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			o.print("<thead ");
			o.print(getAttributes());
			o.print(">");
			if( body != null)
			{
			    body.invoke(null);
			}
			o.print("</thead>");

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

	public String getAChar() {
		return aChar;
	}

	public void setAChar(String char1) {
		aChar = char1;
	}

	public String getCharOff() {
		return charOff;
	}

	public void setCharOff(String charOff) {
		this.charOff = charOff;
	}

	public String getValign() {
		return valign;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
		if (!TagUtil.isEmpty(align)) {
			sb.append("align=\"" + align + "\" ");
		}
		if (!TagUtil.isEmpty(aChar)) {
			sb.append("char=\"" + aChar + "\" ");
		}
		if (!TagUtil.isEmpty(charOff)) {
			sb.append("charoff=\"" + charOff + "\" ");
		}
		if (!TagUtil.isEmpty(valign)) {
			sb.append("valign=\"" + valign + "\" ");
		}
		sb.append(super.getAttributes());
		return sb;
	}

}
