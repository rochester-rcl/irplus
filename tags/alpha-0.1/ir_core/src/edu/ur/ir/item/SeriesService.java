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

package edu.ur.ir.item;

import java.util.List;

/**
 * Series service interface for creating and getting series.
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface SeriesService {
	
	/**
	 * Get series sorted according to the sort information .  
	 * 
	 * @param rowStart - start position in paged set
	 * @param numberOfResultsToShow - number of results to get
	 * @param sortType - Order by asc/desc
	 * 
	 * @return List of series.
	 */
	public List<Series> getSeriesOrderByName(final int rowStart, 
    		final int numberOfResultsToShow, final String sortType);
	
    /**
     * Get a count of series .
     *  
     * @return - the number of collections found
     */
    public Long getSeriesCount();
    
    /**
     * Delete a series with the specified name.
     * 
     * @param id
     */
    public boolean deleteSeries(Long id);
    
    /** 
     * Delete the series with the specified name.
     * 
     * @param name
     */
    public boolean deleteSeries(String name);
    
    /**
     * Get a series by name.
     * 
     * @param name - name of the series .
     * @return - the found series or null if the series is not found.
     */
    public Series getSeries(String name);
    
    /**
     * Get a series by id
     * 
     * @param id - unique id of the series .
     * @param lock - upgrade the lock on the data
     * @return - the found series or null if the series is not found.
     */
    public Series getSeries(Long id, boolean lock);
    
    /**
     * Save the series .
     * 
     * @param series
     */
    public void saveSeries(Series series );
 
	/**
	 * Get all series.
	 * 
	 * @return List of all series 
	 */
	public List<Series> getAllSeries();

}
