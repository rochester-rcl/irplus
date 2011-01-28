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

/**
 * Sortable table tag
 * 
 * @author Nathan Sarr
 *
 */
public class SortableTableDataHeadTag extends TableDataTag {

	/** action to perform for an ascending sort */
	protected String ascendingSortAction;
	
	/** action to perform for a descending sort */
	protected String descendingSortAction;
	
	/**   Values should be [none | asc | desc] */
	protected String currentSortAction; 
	
	/** if set a href will be used */
	protected boolean useHref = false;
	
	/** value to set the href to */
	protected String hrefVar;
	


	/** default on mouse over action */
	public void doTag() throws JspException {
		JspFragment body = getJspBody();
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();
		
		if (TagUtil.isEmpty(onMouseOver)) 
		{  
			if( !useHref )
			{
			    onMouseOver = "onmouseover=this.style.cursor='pointer'\"";
			}
		}

		try {
			
			if( useHref )
			{
			    if( hrefVar == null || hrefVar.trim().equals(""))
			    {
			    	throw new IllegalStateException("hrefVar must be set if useHref is true");
			    }
				 
				String url = this.determineUrl();
				pageContext.setAttribute(hrefVar, url);
			}
			else
			{
			    determineOnCickSortAction();
			}
			o.print("<td ");
			o.print(getAttributes());
			o.print(">");
			if( body != null )
			{
				if( useHref )
				{
			        body.invoke(null);
				}
				else
				{
					 body.invoke(null);
				}
			}
			o.print("</td>");

		} catch (Exception e) {
			throw new JspException(e);
		}
	}
	
	/**
	 * Determines the sort action
	 */
	private void determineOnCickSortAction()
	{
		if( currentSortAction.equalsIgnoreCase("none"))
		{
		
			onClick = ascendingSortAction;
				
		}
		else if( currentSortAction.equalsIgnoreCase("asc"))
		{
			onClick = descendingSortAction;
		}
		else if( currentSortAction.equalsIgnoreCase("desc"))
		{
			onClick = ascendingSortAction;
		}
	}
	
	/**
	 * Determines the sort action
	 */
	private String determineUrl()
	{
		String url ="";
		if( currentSortAction.equalsIgnoreCase("none"))
		{
			url = ascendingSortAction;
		}
		else if( currentSortAction.equalsIgnoreCase("asc"))
		{
			url = descendingSortAction;
		}
		else if( currentSortAction.equalsIgnoreCase("desc"))
		{
			url = ascendingSortAction;
		}
		return url;
	}


	public String getAscendingSortAction() {
		return ascendingSortAction;
	}

	public void setAscendingSortAction(String ascendingSortAction) {
		this.ascendingSortAction = ascendingSortAction;
	}

	public String getDescendingSortAction() {
		return descendingSortAction;
	}

	public void setDescendingSortAction(String decendingSortAction) {
		this.descendingSortAction = decendingSortAction;
	}

	public String getCurrentSortAction() {
		return currentSortAction;
	}

	public void setCurrentSortAction(String currentSortAction) {
		this.currentSortAction = currentSortAction;
	}

	public boolean isUseHref() {
		return useHref;
	}

	public void setUseHref(boolean useHref) {
		this.useHref = useHref;
	}
	
	public String getHrefVar() {
		return hrefVar;
	}

	public void setHrefVar(String hrefVar) {
		this.hrefVar = hrefVar;
	}

	
}
