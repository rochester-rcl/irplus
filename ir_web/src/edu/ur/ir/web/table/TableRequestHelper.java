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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import edu.ur.dao.CriteriaHelper;

/**
 * Helps to process the table data and place it back on the request for the
 * table to be drawn.
 * 
 * @author Nathan Sarr
 *
 */
public class TableRequestHelper {
	
	/** Sort order for the table  */
	private int nextAvailableSortOrder;
	
	/** Current page the user would like to be on */
	private int currentPage = 1;
	
	/** totalNumberOfResults available */
	private int totalNumberOfResults;
	
	/** total number of rows to show  */
	private int maxResultsPerPage;
	
	/** where the offset should start  */
	private int rowStart;
	
	/** where the offset should end */
	private int rowEnd;
	
	/** Name of the form  */
	private String formName;
	
	/**  Logger for table request helper action */
	private static final Logger log = Logger.getLogger(TableRequestHelper.class);
	
	/** List of criteria helpers  */
	private List<CriteriaHelper> criteriaHelpers;
	
	/** A comma seperated list of increments  */
	private String maxResultsPerPageIncrements;
	
	/**  List of increments */
	private LinkedList<String> increments  = new LinkedList<String>();
	
	/**
	 * Determines paging and sort information.  Puts table information
	 * on the request.
	 * 
	 * @param request - where values will be placed
	 * @param converter - convert values to their needed state for searching.
	 * @param collectionInfo - information about collection
	 */
	@SuppressWarnings("unchecked")
	public void processTableData(HttpServletRequest request, PropertyConverter converter, 
			TableCollectionInfo collectionInfo)
	{
		loadPropertiesFile();
		if( request.getParameter("currentPage") != null )
		{
			currentPage = new Integer(request.getParameter("currentPage")).intValue();
		}
		
		if( request.getParameter("maxResultsPerPage") != null)
		{
		    maxResultsPerPage = new Integer(request.getParameter("maxResultsPerPage")).intValue();
		}
		
		formName = request.getParameter("formName");
		Hashtable<String, CriteriaHelper> criterias = new Hashtable<String, CriteriaHelper>();
		TableUtil.getFilterValues(request, converter, criterias);
		TableUtil.getSortValues(request, criterias);
		
		criteriaHelpers = new LinkedList<CriteriaHelper>(criterias.values());
		
		
		
		if( criteriaHelpers == null)
		{
			criteriaHelpers = new LinkedList<CriteriaHelper>();
		}
		
		log.debug("Criteria Helper size is " + criteriaHelpers.size());
		
		// make sure the sort order is set correctly
		CriteriaHelper lastSort = null;
		if( criterias.size() > 0)
		{
			Collections.sort(criteriaHelpers);
		    lastSort = (CriteriaHelper) Collections.max(criteriaHelpers);
		}
		
		if(lastSort == null)
		{
			nextAvailableSortOrder = 0;
		}
		else
		{
			nextAvailableSortOrder = lastSort.getOrder() + 1;
		}
		
		totalNumberOfResults = collectionInfo.getTotalNumberOfResults(criteriaHelpers);
		
		// indicates there has been a change in the total number
		// of results 
		if( currentPage > TableUtil.getTotalNumberOfPages(totalNumberOfResults, maxResultsPerPage))
		{
			currentPage = 1;
		}
		
		rowStart = TableUtil.getRowStart(currentPage, totalNumberOfResults, maxResultsPerPage);
		rowEnd = TableUtil.getRowEnd(currentPage, maxResultsPerPage, totalNumberOfResults);
		
		// what is shown to the user is off by 1 for the row start
		int displayRowStart = rowStart + 1;
		int displayRowEnd = rowEnd;
		
		log.debug("formName = " + formName);
		log.debug("total rows = " + totalNumberOfResults);
		
		TableUtil.putFilterValuesInRequest(request);
		TableUtil.putSortValuesInRequest(request);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("nextAvailableSortOrder", nextAvailableSortOrder);
		request.setAttribute("totalNumberOfResults", totalNumberOfResults);
		request.setAttribute("maxResultsPerPage", maxResultsPerPage);
		request.setAttribute("displayRowStart", displayRowStart);
		request.setAttribute("displayRowEnd", displayRowEnd);
		request.setAttribute("totalNumberOfPages", TableUtil.getTotalNumberOfPages(totalNumberOfResults, maxResultsPerPage));
	    request.setAttribute("maxResultChoices", increments);
	}


	/**
	 * The sort order allows the sorts to be ordered based on the
	 * way the user chose them.
	 * 
	 * @return
	 */
	public int getNextAvailableSortOrder() {
		return nextAvailableSortOrder;
	}


	/**
	 * 
	 * The sort order allows the sorts to be ordered based on the
	 * way the user chose them.
	 * 
	 * @param nextAvailableSortOrder
	 */
	public void setNextAvailableSortOrder(int nextAvailableSortOrder) {
		this.nextAvailableSortOrder = nextAvailableSortOrder;
	}


	/**
	 * Get the current page the user wants to be on.
	 * 
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}


	/**
	 * Get the current page the user wants to be on.
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * Get the name of the form that surrounds the form.
	 * 
	 * @return
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * Set the name of the form that surrounds the form.
	 * 
	 * @param formName
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * Get the maximum number of results to be shown
	 * per page.
	 * 
	 * @return
	 */
	public int getMaxResultsPerPage() {
		return maxResultsPerPage;
	}

	/**
	 * Set the maximum number of results to be shown
	 * per page.
	 * 
	 * @param maxResultsPerPage
	 */
	public void setMaxResultsPerPage(int maxResultsPerPage) {
		this.maxResultsPerPage = maxResultsPerPage;
	}

	/**
	 * The end row for the set of data.
	 * 
	 * @return
	 */
	public int getRowEnd() {
		return rowEnd;
	}

	/**
	 * The row end 
	 * 
	 * @param rowEnd
	 */
	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	/**
	 * The row start for the set of data.
	 * 
	 * @return
	 */
	public int getRowStart() {
		return rowStart;
	}

	/**
	 * Set the row start for the set of data.
	 * 
	 * @param rowStart
	 */
	public void setRowStart(int rowStart) {
		this.rowStart = rowStart;
	}

	/**
	 * The total number of results found.
	 * 
	 * @return
	 */
	public int getTotalNumberOfResults() {
		return totalNumberOfResults;
	}

	/**
	 * Set the total number of results.
	 * 
	 * @param totalNumberOfResults
	 */
	public void setTotalNumberOfResults(int totalNumberOfResults) {
		this.totalNumberOfResults = totalNumberOfResults;
	}

	/**
	 * The set of filters to be applied to the data.
	 * 
	 * @return
	 */
	public List<CriteriaHelper> getCriteriaHelpers() {
		return criteriaHelpers;
	}

	private void loadPropertiesFile()
	{
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream("urtable.properties");
		
	    Properties properties = new Properties();
        try {
            properties.load(is);
            maxResultsPerPageIncrements = properties.getProperty("maxResultsPerPageIncrements");
            maxResultsPerPage = new Integer(properties.getProperty("maxResultsPerPageDefault"));
            
            StringTokenizer tokenizer = new StringTokenizer(maxResultsPerPageIncrements, ",");
            while( tokenizer.hasMoreElements() )
            {
            	increments.add(tokenizer.nextToken().trim());
            }
            
        } catch (IOException e) {
        	throw new IllegalStateException("Could not read testing.properties file");
        }
	}
	
}
