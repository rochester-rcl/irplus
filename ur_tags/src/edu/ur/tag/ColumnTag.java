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


import javax.el.ELException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Creates columns for a table
 * 
 * @author Nathan Sarr
 *
 */
public class ColumnTag extends SimpleTagSupport {

	/** Indicates if the column is filterable  */
	private boolean filter = false;
	
	/** Filter value*/
	private String filterValue ="";
	
	/** Default image */
	private String dropHandleImg = "page-resources/jmesa/droplistHandle.gif";

	/** property to sort and filter on */
	private String sortFilterProperty;
	
	/**  Writer for this tag  */
	private JspWriter o;
	
	/** Width the column should be */
	private String columnWidth;
	
	private TableRowTag rowTag;
	
	/**  Logger for column tag */
	private static final Logger log = LogManager.getLogger(ColumnTag.class);

	public void doTag() throws JspException {
		try {
			rowTag = (TableRowTag) findAncestorWithClass(this,
					TableRowTag.class);
			if (rowTag == null) {
				throw new JspTagException("the <ur:column> tag must"
						+ " be nested within a <ur:row> tag");
			}
			JspFragment body = getJspBody();
			PageContext context = (PageContext) getJspContext();
			o = context.getOut();

			BasicFormTag formTag =
	            (BasicFormTag)findAncestorWithClass(this,
	              BasicFormTag.class);
		    
		    if(formTag == null)
		    {
		    	throw new JspTagException("the <ur:column> tag must"
		    			+ " be nested within a <ur:form> tag");
		    }
		    
		    //initialize the count of columns
		    if(rowTag.isInitialize())
		    {
		    	rowTag.setColumnCount(rowTag.getColumnCount() + 1);
		    }
		    
		    // process the filter rows
			if (rowTag.isProcessFilter() ) {
				processFilter(context, formTag);
			}

			// process the header rows
			if( rowTag.isProcessHeader())
			{
				if( body != null)
				{
				    body.invoke(null);
				}
			}

			// process the columns
            if (rowTag.isProcessColumn()) 
            {
				if( columnWidth == null || columnWidth.trim().equals(""))
				{
				    o.print("<td>");
				}
				else
				{
					o.print("<td width=\"" + columnWidth + "\">");
				}
				if( body != null)
				{
				    body.invoke(null);
				}
				o.print("</td>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	}

	public String getDropHandleImg() {
		return dropHandleImg;
	}

	public void setDropHandleImg(String dropHandleImg) {
		this.dropHandleImg = dropHandleImg;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public String getSortFilterProperty() {
		return sortFilterProperty;
	}

	public void setSortFilterProperty(String sortFilterProperty) {
		this.sortFilterProperty = sortFilterProperty;
	}
	
	private void processFilter(PageContext context, BasicFormTag formTag) 
	throws IOException,  ELException
	{
		// pull the filter value out of the request.
		String filterId = formTag.getName() + "_filter_" + sortFilterProperty;
		
		if( context.getRequest().getAttribute(filterId) != null )
		{
			log.debug("Filter id = " + filterId);
			if( context.getRequest().getAttribute(filterId) instanceof String[])
			{
				
				String[] filters = (String[])context.getRequest().getAttribute(filterId);
				log.debug("Filter Length is " + filters.length);
				log.debug("Filter value is: " + filters[0]);

			}
			else
			{
		        filterValue = (String)context.getRequest().getAttribute(filterId);
			}
		}
		
		o.print("<td>");
		if(filter)
		{
			// create the hidden input value for filtering
			o.print("<input type=\"hidden\" id=\"" + filterId + "\" name=\"" +
					filterId + "\" value=\"" + filterValue + "\"/>");
			
		    o.print("<div onclick=\"new DynamicFilter(" + rowTag.getJavascriptObject() +","); 
		    o.print("this,");
		    o.print("'" + sortFilterProperty + "', '" + rowTag.getSubmitUrl() +"')\">");
		    
		    if( !TagUtil.isEmpty(filterValue))
		    {
		    	o.print(filterValue);
		    }
		    
		    o.print("<img src=\"");
		    o.print(TagUtil.fixRelativePath(dropHandleImg, context));
		    o.print("\" alt=\"filter\"/>");
		    o.print("</div>");
		}
		o.print("</td>\n");
	}

	public String getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(String columnWidth) {
		this.columnWidth = columnWidth;
	}

}
