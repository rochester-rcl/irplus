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

package edu.ur.ir.web.action.item.metadata;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.Preparable;

import edu.ur.ir.item.Series;
import edu.ur.ir.item.SeriesService;
import edu.ur.ir.user.IrRole;
import edu.ur.ir.user.IrUser;
import edu.ur.ir.user.UserService;
import edu.ur.ir.web.action.UserIdAware;
import edu.ur.ir.web.table.Pager;

/**
 * Action to deal with Series.
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ManageSeries extends Pager implements Preparable, UserIdAware{
	
	/** generated version id. */
	private static final long serialVersionUID = -8370650961037267346L;

	/** series service */
	private SeriesService seriesService;
	
	/**  Logger for managing series*/
	private static final Logger log = Logger.getLogger(ManageSeries.class);
	
	/** Set of series for viewing the series */
	private Collection<Series> seriesList;
	
	/**  series for loading  */
	private Series series;

	/** Message that can be displayed to the user. */
	private String message;
	
	/**  Indicates the series has been added*/
	private boolean added = false;
	
	/** Indicates the series have been deleted */
	private boolean deleted = false;
	
	/** id of the series  */
	private Long id;
	
	/** id of user making changes */
	private Long userId;
	
	/** Set of series ids */
	private long[] seriesIds;

	/** type of sort [ ascending | descending ] 
	 *  this is for incoming requests */
	private String sortType = "asc";
	
	/** Total number of series */
	private int totalHits;
	
	/** Row End */
	private int rowEnd;
	
	/** Service for managing user data  */
	private UserService userService;
	
	/** Default constructor */
	public ManageSeries() 
	{
		numberOfResultsToShow = 25;
		numberOfPagesToShow = 10;
	}
	
	/**
	 * Method to create a new series.
	 * 
	 * Create a new series
	 */
	public String create()
	{
		IrUser user = userService.getUser(userId, false);
		
		if( user == null || (!user.hasRole(IrRole.AUTHOR_ROLE) && !user.hasRole(IrRole.AUTHOR_ROLE)) )
		{
		     return "accessDenied";	
		}
		
		log.debug("creating a series = " + series.getName());
		added = false;
		Series other = seriesService.getSeries(series.getName());
				
		if( other == null || !other.equals(series) )
		{
		    seriesService.saveSeries(series);
		    added = true;
		}
		else
		{
			message = getText("seriesAlreadyExists", 
					new String[]{series.getName() + " - " + series.getNumber()});
			addFieldError("seriesAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Method to update an existing series.
	 * 
	 * @return
	 */
	public String update()
	{
		log.debug("updateing series id = " + series.getId());
		added = false;

		Series other = seriesService.getSeries(series.getName());
		
		if( other == null || other.getId().equals(series.getId()))
		{
			seriesService.saveSeries(series);
			added = true;
		}
		else
		{
			message = getText("seriesAlreadyExists", 
					new String[]{series.getName()});
			
			addFieldError("seriesAlreadyExists", message);
		}
        return "added";
	}
	
	/**
	 * Removes the selected series.
	 * 
	 * @return
	 */
	public String delete()
	{
		log.debug("Delete series called");
		if( seriesIds != null )
		{
		    for(int index = 0; index < seriesIds.length; index++)
		    {
			    log.debug("Deleting series with id " + seriesIds[index]);
			    seriesService.deleteSeries(seriesIds[index]);
		    }
		}
		deleted = true;
		return "deleted";
	}
	
	/**
	 * Get the series
	 * 
	 * @return
	 */
	public String get()
	{
		series = seriesService.getSeries(id, false);
		return "get";
	}
	
	/**
	 * Get the series table data.
	 * 
	 * @return
	 */
	public String viewSeries()
	{
		rowEnd = rowStart + numberOfResultsToShow;
	    
		seriesList = seriesService.getSeriesOrderByName(rowStart, 
	    		numberOfResultsToShow, sortType);
	    totalHits = seriesService.getSeriesCount().intValue();
		
		if(rowEnd > totalHits)
		{
			rowEnd = totalHits;
		}
	
		return SUCCESS;
	} 
	
	/**
	 * Get the series service.
	 * 
	 * @return
	 */
	public SeriesService getSeriesService() {
		return seriesService;
	}

	/**
	 * Set the series service.
	 * 
	 * @param seriesService
	 */
	public void setSeriesService(SeriesService seriesService) {
		this.seriesService = seriesService;
	}
	
	/**
	 * List of series for display.
	 * 
	 * @return
	 */
	public Collection<Series> getSeriesList() {
		return seriesList;
	}
	/**
	 * Set the list of series.
	 * 
	 * @param seriesList
	 */
	public void setSeriesList(Collection<Series> seriesList) {
		this.seriesList = seriesList;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isAdded() {
		return added;
	}
	public void setAdded(boolean added) {
		this.added = added;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long[] getSeriesIds() {
		return seriesIds;
	}

	public void setSeriesIds(long[] seriesIds) {
		this.seriesIds = seriesIds;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Series getSeries() {
		return series;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public void prepare() throws Exception {
		if( id != null)
		{
			series = seriesService.getSeries(id, false);
		}
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public int getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(int totalHits) {
		this.totalHits = totalHits;
	}

	public int getRowEnd() {
		return rowEnd;
	}

	public void setRowEnd(int rowEnd) {
		this.rowEnd = rowEnd;
	}

	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
