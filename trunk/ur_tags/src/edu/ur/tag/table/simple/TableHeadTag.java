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

public class TableHeadTag extends CommonBaseHtmlTag {

	/** abbreviated version of the content in a cell  */
	protected String abbr;
	
	/** horizontal alignment in cells */
	protected String align;
	
	/** name for a cell */
	protected String axis;

	/** background color of the table */
	protected String bgcolor;

	/** character to align text on */
	protected String aChar;

	/** offset to the first character */
	protected String charOff;
	
	/** number of columns this cell should span */
	protected String colspan;
	
	/** id's that supply header information for the cell */
	protected String headers;
	
	/** height of the table cell - should use sytles instead */
	protected String height;
	
	/** allow or disallow text wrapping */
	protected String nowrap;
	
	/** number of rows this cell should span */
	protected String rowspan;
	
	/** specifies header information scope*/
	protected String scope;

	/** vertical alignment */
	protected String valign;
	
	/** specifies width of the table cells */
	protected String width;

	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			o.print("<th ");
			o.print(getAttributes());
			o.print(">");
			if( body != null )
			{
			    body.invoke(null);
			}
			o.print("</th>");

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
		if (!TagUtil.isEmpty(abbr)) { sb.append("abbr=\"" + abbr + "\" "); }

		if (!TagUtil.isEmpty(align)) { sb.append("align=\"" + align + "\" "); }

		if (!TagUtil.isEmpty(axis)) { sb.append("axis=\"" + axis + "\" "); }

		if (!TagUtil.isEmpty(bgcolor)) { sb.append("bgcolor=\"" + bgcolor + "\" "); }

		if (!TagUtil.isEmpty(aChar)) {
			sb.append("char=\"" + aChar + "\" ");
		}
		if (!TagUtil.isEmpty(charOff)) {
			sb.append("charoff=\"" + charOff + "\" ");
		}
		
		if (!TagUtil.isEmpty(colspan)) { sb.append("colspan=\"" + colspan + "\" "); }
		if (!TagUtil.isEmpty(headers)) { sb.append("headers=\"" + headers + "\" "); }
		if (!TagUtil.isEmpty(height)) { sb.append("height=\"" + height + "\" "); }
		if (!TagUtil.isEmpty(nowrap)) { sb.append("nowrap=\"" + nowrap + "\" "); }
		if (!TagUtil.isEmpty(rowspan)) { sb.append("rowspan=\"" + rowspan + "\" "); }
		if (!TagUtil.isEmpty(scope)) { sb.append("scope=\"" + scope + "\" "); }
		if (!TagUtil.isEmpty(valign)) { sb.append("valign=\"" + valign + "\" "); }
		if (!TagUtil.isEmpty(width)) { sb.append("width=\"" + width + "\" "); }

		
		if (!TagUtil.isEmpty(valign)) {
			sb.append("valign=\"" + valign + "\" ");
		}

		sb.append(super.getAttributes());
		return sb;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getAxis() {
		return axis;
	}

	public void setAxis(String axis) {
		this.axis = axis;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getHeaders() {
		return headers;
	}

	public void setHeaders(String headers) {
		this.headers = headers;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getNowrap() {
		return nowrap;
	}

	public void setNowrap(String nowrap) {
		this.nowrap = nowrap;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

}
