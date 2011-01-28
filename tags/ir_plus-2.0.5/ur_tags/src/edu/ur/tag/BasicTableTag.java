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

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;

import edu.ur.tag.html.HtmlTable;

/**
 * Creates a table
 * 
 * @author Nathan Sarr
 *
 */
public class BasicTableTag extends CommonSimpleTag implements HtmlTable {
	
	@SuppressWarnings("unchecked")
	private Collection collection;
	private String align;
	private String bgColor;
	private String border;
	private String cellPadding;
	private String cellSpacing;
	private String frame;
	private String rules;
	private String summary;
	private String width;
	private String caption;
	
	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		
		   if(collection == null)
		   {
		       collection = new LinkedList();
		   }
			  
		   
		   JspFragment body = getJspBody();
	       PageContext pageContext = (PageContext) getJspContext();
	       JspWriter out = pageContext.getOut();
	       
	       try {
	    	   out.print("<table ");
	    	   out.print(getAttributes());
	    	   out.print(">");
	    	  
	    	   if(!TagUtil.isEmpty(getCaption()))
	    	   {
	    		   out.print("<caption>");
	    		   out.print(getCaption());
	    		   out.print("</caption>");
	    	   }
	    	   out.print("\n");
	    	   
	    	   if( body!= null )
	    	   {
	    	       body.invoke(null);
	    	   }
	    	   
	    	   out.print("</table>");
	    	   
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

	public StringBuffer getAttributes() {
		StringBuffer sb = new StringBuffer();
    	if(!TagUtil.isEmpty(align)) { sb.append("align=\"" + align + "\" "); }
    	if(!TagUtil.isEmpty(bgColor)) { sb.append("bgcolor=\"" + bgColor + "\" "); }
    	if(!TagUtil.isEmpty(border)) { sb.append("border=\"" + border + "\" "); }  
    	if(!TagUtil.isEmpty(cellPadding)) { sb.append("cellpadding=\"" + cellPadding + "\" "); }  
    	if(!TagUtil.isEmpty(cellSpacing)) { sb.append("cellspacing=\"" + cellSpacing + "\" "); }  
    	if(!TagUtil.isEmpty(frame)) { sb.append("frame=\"" + frame + "\" "); }  
    	if(!TagUtil.isEmpty(rules)) { sb.append("rules=\"" + rules + "\" "); }  
    	if(!TagUtil.isEmpty(summary)) { sb.append("summary=\"" + summary + "\" "); } 
    	if(!TagUtil.isEmpty(width)) { sb.append("width=\"" + width + "\" "); } 
    	
    	sb.append(getAllSimpleTagAttributes());
		return sb;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	@SuppressWarnings("unchecked")
	public Collection getCollection() {
		return collection;
	}

	@SuppressWarnings("unchecked")
	public void setCollection(Collection collection) {
		this.collection = collection;
	}
}
