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


package edu.ur.tag.pager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

/**
 * Simple pager tag.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PagerTag extends SimpleTagSupport {
		
	/** Logger */
	private static final Logger log = Logger.getLogger(PagerTag.class);

	/** The page number to start the display */
	private int startPageNumber;
	
	/** The page number to end the display with   */
	private int  endPageNumber;
	
	/** Page number that is currently displayed */	
	private int currentPageNumber;

	/** Total number of pages */
	private int totalPageNumber;
	
	/** Total number of rows */
	private int totalHits;
	
	/** number of results to show per page */
	private int numberOfResultsToShow;
	
	/** number of pages to show  */
	private int numberOfPagesToShow;
	
	/** String to represent more pages */
	private String morePages = "....";

	public void doTag() throws JspException {
		log.debug("do tag called");

		JspFragment body = getJspBody();

		if (totalHits % numberOfResultsToShow == 0) {
			totalPageNumber = totalHits / numberOfResultsToShow;
		} else {
			totalPageNumber = (totalHits / numberOfResultsToShow) + 1;
		}
		
		if( totalPageNumber == 0)
		{
			totalPageNumber = 1;
		}
		
		
		if ((startPageNumber + numberOfPagesToShow - 1) <= totalPageNumber) {
			endPageNumber = startPageNumber + numberOfPagesToShow - 1;
		} else {
			endPageNumber =totalPageNumber;
		}
	
		log.debug("total page number = " + totalPageNumber + " endPageNumber = " + endPageNumber);
		
		getJspContext().setAttribute("totalPageNumber", totalPageNumber);
		getJspContext().setAttribute("endPageNumber", endPageNumber);
		try {
			if( body != null )
			{
			    body.invoke(null);
			}

		} catch (Exception e) {
			throw new JspException(e);
		}

	}

	public int getStartPageNumber() {
		return startPageNumber;
	}

	public void setStartPageNumber(int startPageNumber) {
		this.startPageNumber = startPageNumber;
	}

	public int getEndPageNumber() {
		return endPageNumber;
	}

	public void setEndPageNumber(int endPageNumber) {
		this.endPageNumber = endPageNumber;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}

	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}

	public String getMorePages() {
		return morePages;
	}

	public void setMorePages(String morePages) {
		this.morePages = morePages;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getNumberOfResultsToShow() {
		return numberOfResultsToShow;
	}

	public void setNumberOfResultsToShow(int numberOfResultsToShow) {
		this.numberOfResultsToShow = numberOfResultsToShow;
	}

	public int getNumberOfPagesToShow() {
		return numberOfPagesToShow;
	}

	public void setNumberOfPagesToShow(int numberOfPagesToShow) {
		this.numberOfPagesToShow = numberOfPagesToShow;
	}


}
