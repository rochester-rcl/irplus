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


package edu.ur.ir.item.service;

import java.util.List;

import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesDAO;
import edu.ur.ir.item.SeriesService;

/**
 * Default service for dealing with series.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class DefaultSeriesService implements SeriesService{
	
	/**  series data access. */
	private SeriesDAO seriesDAO;


	/**
	 * Delete a series with the specified id.
	 * 
	 * @see edu.ur.ir.item.SeriesService#deleteSeries(java.lang.Long)
	 */
	public boolean deleteSeries(Long id) {
		Series series  = this.getSeries(id, false);
		if( series  != null)
		{
			seriesDAO.makeTransient(series);
		}
		return true;
	}

	/**
	 * Delete a series with the specified name.
	 * 
	 * @see edu.ur.ir.item.SeriesService#deleteSeries(java.lang.String)
	 */
	public boolean deleteSeries(String name) {
		Series series = this.getSeries(name);
		if( series != null)
		{
			seriesDAO.makeTransient(series);
		}
		return true;
	}

	/**
	 * Get the series with the name.
	 * 
	 * @see edu.ur.ir.item.SeriesService#getSeries(java.lang.String)
	 */
	public Series getSeries(String name) {
		return seriesDAO.findByUniqueName(name);
	}

	/**
	 * Get the series by id.
	 * 
	 * @see edu.ur.ir.item.SeriesService#getSeries(java.lang.Long, boolean)
	 */
	public Series getSeries(Long id, boolean lock) {
		return seriesDAO.getById(id, lock);
	}

	/**
	 * Get the series  order by name
	 * 
	 * @see edu.ur.ir.item.SeriesService#getSeriesOrderByName(int, int, String)
	 */
	public List<Series> getSeriesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType) {
		return seriesDAO.getSeriesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	}
	
	/**
	 * Get the series count
	 * 
	 * @see edu.ur.ir.item.SeriesService#getSeriessCount()
	 */
	public Long getSeriesCount() {
		return seriesDAO.getCount();
	}

	/**
	 * Series data access.
	 * 
	 * @return
	 */
	public SeriesDAO getSeriesDAO() {
		return seriesDAO;
	}

	/**
	 * Set the series data access.
	 * 
	 * @param seriesDAO
	 */
	public void setSeriesDAO(SeriesDAO seriesDAO) {
		this.seriesDAO = seriesDAO;
	}

	/**
	 * Save the series .
	 * 
	 * @see edu.ur.ir.item.SeriesService#saveSeries(edu.ur.ir.item.Series)
	 */
	public void saveSeries(Series series) {
		seriesDAO.makePersistent(series);
	}

	/**
	 * Get all series.
	 * 
	 * @see edu.ur.ir.item.SeriesService#getAllSeries()
	 */
	@SuppressWarnings("unchecked")
	public List<Series> getAllSeries() { 
		return (List<Series>) seriesDAO.getAll();
	}

}
