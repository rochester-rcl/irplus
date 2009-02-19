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


package edu.ur.ir.web.table;

import com.opensymphony.xwork2.ActionSupport;

/**
 * This is an abstract class for Pager.
 * 
 * Number of pages to be displayed and number of rows in a page can be customized
 * by overriding these parameters in the extending class.
 * 
 * @author Sharmila Ranganathan
 *
 */
public abstract class Pager extends ActionSupport {

	/** Eclipse generated Id */
	private static final long serialVersionUID = -3953265958290535965L;

	/** Starting row number to get the result */
	protected int rowStart = 0;
	
	/** number of results to show per page */
	protected int numberOfResultsToShow = 25;
	
	/** number of pages to show  */
	protected int numberOfPagesToShow = 5;
	
	/** Current page number this is displayed */
	protected int currentPageNumber = 1;
	
	/** The page number to start the display */
	protected int startPageNumber = 1;

	/**
	 *  Get the total number of rows.
	 *  This method has to be implemented.
	 *  
	 * @return
	 */
	public abstract int getTotalHits() ;
	
	/**
	 * Get the start row number to get the data
	 *  
	 * @return
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * Set the start row number to get the data
	 * 
	 * @param rowStart
	 */
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	/**
	 * Get the number of results to show per page
	 * 
	 * @return
	 */
	public int getNumberOfResultsToShow() {
		return numberOfResultsToShow;
	}

	/**
	 * Get the number of pages to show
	 * 
	 * @return
	 */
	public int getNumberOfPagesToShow() {
		return numberOfPagesToShow;
	}

	/**
	 * Get current page number
	 * 
	 * @return
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * Set current page number
	 * 
	 * @param currentPageNumber
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * Get page number to start the display with
	 * 
	 * @return
	 */
	public int getStartPageNumber() {
		return startPageNumber;
	}

	/**
	 * Set page number to start the display with
	 * 
	 * @param startPageNumber
	 */
	public void setStartPageNumber(int startPageNumber) {
		this.startPageNumber = startPageNumber;
	}

	
	
}
