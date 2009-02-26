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

import edu.ur.tag.base.html.ImageTag;

/**
 * Tag that displays the correct sort options
 * 
 * @author Nathan Sarr
 *
 */
public class SortableHeaderTableImageTag extends ImageTag{

	/** image to display if sorting ascending */
	protected String sortAscendingImage;
	
	/** image to display if sorting descending */
	protected String sortDescendingImage;
	
	/** ascending sort alt text */
	protected String sortAscendingAltText;
	
	/** descending sort alt text  */
	protected String sortDescendingAltText;
	
	@SuppressWarnings("unchecked")
	public void doTag() throws JspException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter o = pageContext.getOut();
		
		SortableTableDataHeadTag headTag = (SortableTableDataHeadTag) findAncestorWithClass(this,
				SortableTableDataHeadTag.class);

		String currentSortAction = headTag.getCurrentSortAction();
		
		try {
			
			if( currentSortAction.equalsIgnoreCase("none"))
			{
				
			}
			else if( currentSortAction.equalsIgnoreCase("asc"))
			{
				src = sortAscendingImage;
				alt = sortAscendingAltText;
				o.print("<img ");
				o.print(getAttributes());
				o.print("/>");
			}
			else if( currentSortAction.equalsIgnoreCase("desc"))
			{
				src = sortDescendingImage;
				alt = sortDescendingAltText;
				o.print("<img ");
				o.print(getAttributes());
				o.print("/>");
			}
			

		} catch (Exception e) {
			throw new JspException(e);
		}
	}

	public String getSortAscendingImage() {
		return sortAscendingImage;
	}

	public void setSortAscendingImage(String sortAscendingImage) {
		this.sortAscendingImage = sortAscendingImage;
	}

	public String getSortDescendingImage() {
		return sortDescendingImage;
	}

	public void setSortDescendingImage(String sortDecendingImage) {
		this.sortDescendingImage = sortDecendingImage;
	}

	public String getSortAscendingAltText() {
		return sortAscendingAltText;
	}

	public void setSortAscendingAltText(String sortAcendingAltText) {
		this.sortAscendingAltText = sortAcendingAltText;
	}

	public String getSortDescendingAltText() {
		return sortDescendingAltText;
	}

	public void setSortDescendingAltText(String sortDesendingAltText) {
		this.sortDescendingAltText = sortDesendingAltText;
	}
	


	
}
