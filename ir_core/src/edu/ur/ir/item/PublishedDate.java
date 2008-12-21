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

import edu.ur.ir.BasicDate;

/**
 * Represents the date, the publication has been previously published by a publisher
 * 
 * @author Sharmila Ranganathan
 *
 */
public class PublishedDate extends BasicDate{

	/** Eclipse generated id */
	private static final long serialVersionUID = -180796090221448084L;
	
	/** External Published Item the publish date belongs to */
	private ExternalPublishedItem externalPublishedItem;

	/**
	 * Default constructor
	 */
	public PublishedDate(){
		super();
	}
	
	/**
	 * Constructor to create published Date
	 * 
	 * @param month month of year
	 * @param day day of the month
	 * @param year year
	 */
	public PublishedDate(int month, int day, int year){
		super(month, day, year);
	}

	/**
	 * Constructor to create date with time
	 * 
	 * @param month month of year
	 * @param day day of the month
	 * @param year year
	 * @param hrs hour in the day
	 * @param mins minute in the hour
	 * @param secs seconds
	 * @param fSec Fraction of second
	 */
	public PublishedDate(int month, int day, int year, int hrs, int mins, int secs, int fSec){
		super(month, day, year, hrs, mins, secs, fSec);
		
	}

	/**
	 * Get externally published data
	 * 
	 * @return
	 */
	public ExternalPublishedItem getExternalPublishedItem() {
		return externalPublishedItem;
	}

	/**
	 * Set externally published data
	 * 
	 * @param externalPublishedItem
	 */
	public void setExternalPublishedItem(ExternalPublishedItem externalPublishedItem) {
		this.externalPublishedItem = externalPublishedItem;
	}

}
