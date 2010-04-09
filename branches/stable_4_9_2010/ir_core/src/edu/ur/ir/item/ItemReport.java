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

import edu.ur.persistent.BasePersistent;

/**
 * Report number for the item
 * 
 * @author Sharmila Ranganathan
 *
 */
public class ItemReport extends BasePersistent {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 3938834417427937175L;
	
	/**  Series the report belongs too */
	private Series series;
	
	/**  Item the report belongs to */
	private GenericItem item;
	
	/**  Report number */
	private String reportNumber;

	/** Package protected constructor  */
	ItemReport(){}
    
    /**
     * Create a link between an item and item report.
     * 
     * @param item item to link 
     * @param series Series the report belongs to
     * @param reportNumber Report number
     */
    public ItemReport(GenericItem item, Series series, String reportNumber)
    {
    	setItem(item);
    	setSeries(series);
    	setReportNumber(reportNumber);
    }
    
	/**
	 * Get the series report belongs to
	 * 
	 * @return
	 */
	public Series getSeries() {
		return series;
	}

	/**
	 * Set the series report belongs to
	 * 
	 * @param series
	 */
	public void setSeries(Series series) {
		this.series = series;
	}

	/**
	 * Get the item report belongs to 
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item report belongs to 
	 * 
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}

	/**
	 * Get the report number
	 * 
	 * @return
	 */
	public String getReportNumber() {
		return reportNumber;
	}

	/**
	 * Set the report number
	 * 
	 * @param reportNumber
	 */
	public void setReportNumber(String reportNumber) {
		this.reportNumber = reportNumber;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += reportNumber == null ? 0 : reportNumber.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer(" [ id = ");
		sb.append(id);
		sb.append(" reportNumber = " );
		sb.append(reportNumber);
		sb.append(" item = ");
		sb.append(item);
		sb.append(" series = ");
		sb.append(series);
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemReport)) return false;

		final ItemReport other = (ItemReport) o;

		if( ( reportNumber != null && !reportNumber.equals(other.getReportNumber()) ) ||
			( reportNumber == null && other.getReportNumber() != null ) ) return false;
		
		if( ( series != null && !series.equals(other.getSeries()) ) ||
				( series == null && other.getSeries() != null ) ) return false;
		
		if( ( item != null && !item.equals(other.getItem()) ) ||
				( item == null && other.getItem() != null ) ) return false;
		
		return true;
	}
}
