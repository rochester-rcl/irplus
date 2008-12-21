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
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

/**
 * Header tag for dealing with the header of a table.
 * 
 * @author Nathan Sarr
 *
 */
public class ColumnHeaderTag extends SimpleTagSupport{
	
	/**  Logger for column tag */
	private static final Logger log = Logger.getLogger(ColumnHeaderTag.class);
	
	/** Indicates if the filter is sortable */
	private boolean sort = false;
	
	/** indicates the sort is case insensitive */
	private boolean ignoreCaseSort = false;
	
	/**  Writer for this tag  */
	private JspWriter o;
	
	/**  Row tag to help with processing  */
	private TableRowTag rowTag;
	
	/** Type of sort - asc, desc or none  */
	private String sortType;
	
	/**  parent column tag */
	private ColumnTag columnTag;
	
	/** Sort order in relation to all other columns when applied to the database  */
	private int sortOrder;
	
	/** Default sort ascending image */
	private String sortAscImage = "page-resources/jmesa/sortAsc.gif";

	/** Default sort ascending image */
	private String sortDescImage = "page-resources/jmesa/sortDesc.gif";

	
	public void doTag() throws JspException {
		try {
			rowTag = (TableRowTag) findAncestorWithClass(this,
					TableRowTag.class);
			
			columnTag = (ColumnTag)findAncestorWithClass(this,
					ColumnTag.class);
			
			if (columnTag == null) {
				throw new JspTagException("the <ur:columnHeader> tag must"
						+ " be nested within a <ur:column> tag");
			}
			
			BasicFormTag formTag =
	            (BasicFormTag)findAncestorWithClass(this,
	              BasicFormTag.class);
		 
			
			PageContext context = (PageContext) getJspContext();
			o = context.getOut();



            // process the header information
			if (rowTag.isProcessHeader()) {
				o.print("<td");
				if(sort)
				{
					o.print(" onmouseover=\"this.style.cursor='pointer'\"");
					processHeaderSort(context, formTag);
				}
				else
				{
					o.print(">");
				}
				JspFragment body = getJspBody();
				if( body != null)
				{
				    body.invoke(null);
				}
				
				if( sort && !TagUtil.isEmpty(sortType) )
				{
				   showSortImage(context);	
				}
				o.print("</td>\n");

			// process the columns
			} 
 
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspException(e);
		}
	}
	
	/**
	 * Set the sort ordering in the form.  This either creates a sort value
	 * or reads the current set sort value and sets the table up to show the
	 * current sort setting.
	 * 
	 * @param context
	 * @param formTag
	 * @param out
	 * @throws IOException
	 */
	private void processHeaderSort(PageContext context, BasicFormTag formTag) 
	throws IOException 
	{
		String sortId = formTag.getName() + "_sort_" + columnTag.getSortFilterProperty();
		
		if( context.getRequest().getAttribute(sortId) != null )
		{
			log.debug("Sort id = " + sortId);
			if(context.getRequest().getAttribute(sortId) instanceof String[])
			{
			     String[] sorts = 	(String[])context.getRequest().getAttribute(sortId);
			     log.debug("Sorts length is " + sorts.length);
			     log.debug("Sort value is " + sorts[0]);
			}
			else
			{
		        String sortValue = (String)context.getRequest().getAttribute(sortId);
		        log.debug("sortValue = " + sortValue);
			    int charIndex = sortValue.indexOf('_');
			    sortType = sortValue.substring(0, charIndex);
			    
			    String remainder = sortValue.substring(charIndex + 1);
			    charIndex = remainder.indexOf('_');
			    
			    sortOrder = new Integer(remainder.substring(0, charIndex));
			    
			    ignoreCaseSort = new Boolean(remainder.substring(charIndex + 1)).booleanValue();
			}
		}

		// we want ascending order
		if( sortType == null || sortType.trim().equals(""))
		{
			this.printSortOnClick("asc");
		}
		
		// we want decending order
		else if( sortType.equals("asc"))
		{
			this.printSortOnClick("desc");
		}
		
		// remove the ordering
		else if( sortType.equals("desc"))
		{
			this.printSortOnClick("");
		}
		
		// place a hidden input type in the form.  We need to place
		// blank one just in case someone clicks on it, otherwise, 
		// there well be no id to set.
		if(TagUtil.isEmpty(sortType))
		{
			
		    o.print("<input type=\"hidden\" id=\"" +  sortId + "\" name=\"" +
		    		sortId + "\" value=\"\"/>");
		}
		else
		{
			o.print("<input type=\"hidden\" id=\"" + formTag.getName() 
					+ "_sort_" + columnTag.getSortFilterProperty()  + "\" name=\"" +
					 sortId + "\" value=\"" + sortType +
					 "_" + sortOrder + "_" + ignoreCaseSort +"\"/>");
		}
	}
	
	private void showSortImage(PageContext context) throws IOException, ELException
	{
		o.print("&nbsp;<img src=\"");
		if(sortType.equals("asc"))
		{
		    o.print(TagUtil.fixRelativePath(sortAscImage, context));
		}
		else if (sortType.equals("desc"))
		{
			o.print(TagUtil.fixRelativePath(sortDescImage, context));
		}
		o.print("\" style=\"border: 0pt none ;\" alt=\"Arrow\"/>");
	}
	
	private void printSortOnClick(String order) throws IOException
	{
		o.print("onclick=\"" + rowTag.getJavascriptObject() +".addSort('");
        o.print( sortOrder + "', '");
        o.print(columnTag.getSortFilterProperty());
        o.print("', '" + order +"', '" + rowTag.getSubmitUrl() + "', '" + ignoreCaseSort + "');\">");	
    }

	public boolean isSort() {
		return sort;
	}

	public void setSort(boolean sort) {
		this.sort = sort;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getSortAscImage() {
		return sortAscImage;
	}

	public void setSortAscImage(String sortAscImage) {
		this.sortAscImage = sortAscImage;
	}

	public String getSortDescImage() {
		return sortDescImage;
	}

	public void setSortDescImage(String sortDescImage) {
		this.sortDescImage = sortDescImage;
	}

	public boolean getIgnoreCaseSort() {
		return ignoreCaseSort;
	}

	public void setIgnoreCaseSort(boolean ignoreCaseSort) {
		this.ignoreCaseSort = ignoreCaseSort;
	}
	
	

}
