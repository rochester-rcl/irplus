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
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

public class TableRowTag extends SimpleTagSupport{
	
	private int columnCount;
	private boolean initialize;
	private boolean processFilter;
	private boolean processHeader;
	private boolean processColumn;
	private boolean processToolBarHeader;
	private boolean processToolBarFooter = true;
	/** Row with out the odd, even colors and mouse events */
	private boolean basicRow;
	private int totalNumberOfPages = 1;
	private int currentPage = 1;
	private int totalResults = 0;
	private int rowStart = 0;
	private int rowEnd = 0;

	/** Javascript object created to make calls on */
	private String javascriptObject;
	
	/** the div whose content will be replaced */
	private String divId;
	
	/** Url to be submitted to. */
	private String submitUrl;
	
	/**  Logger*/
	private static final Logger log = Logger.getLogger(TableRowTag.class);

	
	JspWriter o;

	
	private String var;
	private int index = 0;
	
	
	public String getVar() {
		return var;
	}


	public void setVar(String var) {
		this.var = var;
	}

	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
	    
		BasicFormTag formTag =
            (BasicFormTag)findAncestorWithClass(this,
              BasicFormTag.class);
	    
	    if(formTag == null)
	    {
	    	throw new JspTagException("the <ur:column> tag must"
	    			+ " be nested within a <ur:form> tag");
	    }

		
	    BasicTableTag tableTag =
            (BasicTableTag)findAncestorWithClass(this,
              BasicTableTag.class);
	    
	    if(tableTag == null)
	    {
	    	throw new JspTagException("the <ur:simpleTablePager> tag must"
	    			+ " be nested within a <ur:form> tag");
	    }
	    
	    Collection items = tableTag.getCollection();
	    
	    if( rowStart <= 0 && items.size() > 0)
	    {
	    	rowStart = 1;
	    }
	    
	    if( rowEnd <= 0)
	    {
	    	rowEnd = items.size();
	    }
	    
	    if( totalResults == 0 && items.size() > 0)
	    {
	    	totalResults = items.size();
	    }
	    
	    JspFragment body = getJspBody();
	    PageContext pageContext = (PageContext) getJspContext();
	    o = pageContext.getOut();
	    try {
	    	//handle adding the filter rows first
	    	o.print("<thead>\n");
	    	
	    	initialize = true;
	    	log.debug("column count before = " + columnCount);
	    	body.invoke(null);
	    	log.debug("Column count after = " + columnCount);
	    	initialize = false;
	    	
	    	processToolBarHeader = true;
	    	body.invoke(null);
	    	processToolBarHeader = false;
	    	
	    	processFilter = true;	
	    	o.print("<tr class=\"filter\">\n");
			body.invoke(null);
	    	o.print("</tr>\n");
			processFilter = false;
			
			// handle the header rows second
			processHeader = true;
	    	o.print("<tr class=\"header\">\n");
			body.invoke(null);
	    	o.print("</tr>\n");
			processHeader=false;
			
			
			o.print("</thead>\n");
			
			//process the column bodies
			processColumn=true;

			o.print("<tbody class=\"tbody\">\n");
			
			// process the items
			for(Object i : items)
			{
				if (!basicRow) {
					if( index % 2 == 0 )
					{
					     o.print("<tr id=\"folders_row" + index +"\"" + " class=\"odd\" onmouseover=\"this.className='highlight'\" onmouseout=\"this.className='odd'\">");
					}
					else
					{
						 o.print("<tr id=\"folders_row" + index +"\"" + " class=\"even\" onmouseover=\"this.className='highlight'\" onmouseout=\"this.className='even'\">");
					}
				} else {
					o.print("<tr id=\"folders_row" + index +"\">");					
				}
				getJspContext().setAttribute(var, i);
				getJspContext().setAttribute("index", index);
			    body.invoke(null);
			    index +=1;
			    
			    o.print("</tr>");
			}
			o.print("</tbody>\n");
			processColumn=false;

			if (processToolBarFooter) {
		    	o.print("<tbody>");
		    	o.print("<tr class=\"statusBar\">");
		    	o.print("<td colspan=\"" + getColumnCount() + "\">");
		    	o.print("Results " + rowStart + " - " + rowEnd + " of " + totalResults + " -- Page " + currentPage + " of " + totalNumberOfPages);
		    	o.print("</td>");
		    	o.print("</tr>");
		    	o.print("</tbody>");
			}
			
	    	
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	    
	}
	
	
	public boolean isProcessColumn() {
		return processColumn;
	}

	public boolean isProcessFilter() {
		return processFilter;
	}

	public boolean isProcessHeader() {
		return processHeader;
	}

	public int getIndex() {
		return index;
	}


	public boolean isProcessToolBarFooter() {
		return processToolBarFooter;
	}


	public void setProcessToolBarFooter(boolean processToolBarFooter) {
		this.processToolBarFooter = processToolBarFooter;
	}


	public boolean isProcessToolBarHeader() {
		return processToolBarHeader;
	}


	public void setProcessToolBarHeader(boolean processToolBarHeader) {
		this.processToolBarHeader = processToolBarHeader;
	}


	public int getRowEnd() {
		return rowEnd;
	}


	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}


	public int getRowStart() {
		return rowStart;
	}


	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}


	public int getTotalNumberOfPages() {
		return totalNumberOfPages;
	}


	public void setTotalNumberOfPages(int totalNumberOfPages) {
		this.totalNumberOfPages = totalNumberOfPages;
	}


	public int getTotalResults() {
		return totalResults;
	}


	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}


	public int getColumnCount() {
		return columnCount;
	}


	public void setColumnCount(int columnCount) {
		log.debug("Setting column count to " + columnCount);
		this.columnCount = columnCount;
	}


	public boolean isInitialize() {
		return initialize;
	}


	public void setInitialize(boolean initialize) {
		this.initialize = initialize;
	}


	public String getDivId() {
		return divId;
	}


	public void setDivId(String divId) {
		this.divId = divId;
	}


	public String getJavascriptObject() {
		return javascriptObject;
	}


	public void setJavascriptObject(String javascriptObject) {
		this.javascriptObject = javascriptObject;
	}


	public String getSubmitUrl() {
		return submitUrl;
	}


	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}

	public boolean isBasicRow() {
		return basicRow;
	}


	public void setBasicRow(boolean basicRow) {
		this.basicRow = basicRow;
	}
	
}
