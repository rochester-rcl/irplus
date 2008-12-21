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

import org.apache.log4j.Logger;

import edu.ur.tag.base.CommonBaseHtmlTag;
import edu.ur.tag.TagUtil;
import edu.ur.tag.html.HtmlTable;

/**
 * Simple table tag.
 * 
 * @author Nathan Sarr
 *
 */
public class TableTag extends CommonBaseHtmlTag implements HtmlTable {


	
	/** aligns the table - styles should be used instead */
	private String align;
	
	/** background color of the table - styles should be used instead*/
	private String bgColor;
	
	/** set the width of the border */
	private String border;
	
	/** space between cell walls  */
	private String cellPadding;
	
	/** specifies the space between cells  */
	private String cellSpacing;
	
	/** specifies the sides of the border surrounding ga table  */
	private String frame;
	
	/** sets the horizontal/vertical divider lines  */
	private String rules;
	
	/** summary for non-visual browsers */
	private String summary;
	
	/** width of the table   */
	private String width;

	/** number of columns to create */
	private int columnCount;
		
	/** Logger */
	private static final Logger log = Logger.getLogger(TableTag.class);

	/** table body class */
	private String tableBodyClass;
	
	/** number of rows in the table */
	private int rowCount;


	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		log.debug("do tag called");

		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			o.print("<table ");
			o.print(getHtmlTableAttributes());
			o.print(">");
			if( body != null )
			{
			    body.invoke(null);
			}
			o.print("</table>");

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

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getBorder() {
		return border;
	}

	public void setBorder(String border) {
		this.border = border;
	}

	public String getCellPadding() {
		return cellPadding;
	}

	public void setCellPadding(String cellPadding) {
		this.cellPadding = cellPadding;
	}

	public String getCellSpacing() {
		return cellSpacing;
	}

	public void setCellSpacing(String cellSpacing) {
		this.cellSpacing = cellSpacing;
	}

	public String getFrame() {
		return frame;
	}

	public void setFrame(String frame) {
		this.frame = frame;
	}

	public String getRules() {
		return rules;
	}

	public void setRules(String rules) {
		this.rules = rules;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public StringBuffer getHtmlTableAttributes() {
		StringBuffer sb = new StringBuffer();
		if (!TagUtil.isEmpty(align)) {
			sb.append("align=\"" + align + "\" ");
		}
		if (!TagUtil.isEmpty(bgColor)) {
			sb.append("bgcolor=\"" + bgColor + "\" ");
		}
		if (!TagUtil.isEmpty(border)) {
			sb.append("border=\"" + border + "\" ");
		}
		if (!TagUtil.isEmpty(cellPadding)) {
			sb.append("cellpadding=\"" + cellPadding + "\" ");
		}
		if (!TagUtil.isEmpty(cellSpacing)) {
			sb.append("cellspacing=\"" + cellSpacing + "\" ");
		}
		if (!TagUtil.isEmpty(frame)) {
			sb.append("frame=\"" + frame + "\" ");
		}
		if (!TagUtil.isEmpty(rules)) {
			sb.append("rules=\"" + rules + "\" ");
		}
		if (!TagUtil.isEmpty(summary)) {
			sb.append("summary=\"" + summary + "\" ");
		}
		if (!TagUtil.isEmpty(width)) {
			sb.append("width=\"" + width + "\" ");
		}

		sb.append(super.getAttributes());
		return sb;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public String getTableBodyClass() {
		return tableBodyClass;
	}

	public void setTableBodyClass(String tableBodyClass) {
		this.tableBodyClass = tableBodyClass;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
