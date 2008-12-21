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

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import edu.ur.tag.TagUtil;
import edu.ur.tag.base.CommonBaseHtmlTag;

public class TableBodySectionTag extends CommonBaseHtmlTag {

	/** collection to create the table around */
	@SuppressWarnings("unchecked")
	private Collection collection;

	/** text alignment in cells */
	private String align;

	/** character to align text on */
	private String aChar;

	/** offset to the first character */
	private String charOff;

	/** vertical alignment */
	private String valign;

	/** variable to assign the value to */
	private String var;

	/** row index */
	private int rowIndex = 0;
	
	/** Variable to set the row status index value */
	private String rowStatus;

	/** class to give odd rows */
	private String oddRowClass;

	/** class to give even rows */
	private String evenRowClass;

	/** current row class */
	private String currentRowClassVar;

	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {

		if (collection == null) {
			collection = new LinkedList();
		}

		String currentClass = "";
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();

		try {
			o.print("<tbody ");
			o.print(getAttributes());
			o.print(">");

			for (Object object : collection) {
				if (rowIndex % 2 == 0) {
					currentClass = evenRowClass;
				} else {
					currentClass = oddRowClass;
				}
				
				getJspContext().setAttribute(var, object);
				
				getJspContext().setAttribute(currentRowClassVar, currentClass);
				
				if( rowStatus != null )
				{
				    getJspContext().setAttribute(rowStatus, rowIndex);
				}
				if( body != null)
				{
				    body.invoke(null);
				}
				rowIndex += 1;
			}
			o.print("</tbody>");

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

	@SuppressWarnings("unchecked")
	public Collection getCollection() {
		return collection;
	}

	@SuppressWarnings("unchecked")
	public void setCollection(Collection collection) {
		this.collection = collection;
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

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getOddRowClass() {
		return oddRowClass;
	}

	public void setOddRowClass(String oddRowClass) {
		this.oddRowClass = oddRowClass;
	}

	public String getEvenRowClass() {
		return evenRowClass;
	}

	public void setEvenRowClass(String evenRowClass) {
		this.evenRowClass = evenRowClass;
	}

	public String getCurrentRowClassVar() {
		return currentRowClassVar;
	}

	public void setCurrentRowClassVar(String rowClass) {
		this.currentRowClassVar = rowClass;
	}

	public String getRowStatus() {
		return rowStatus;
	}

	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}

}
