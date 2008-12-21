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
 * Represents the date the publication can be made available to public
 * 
 * @author Sharmila Ranganathan
 *
 */
public class OriginalItemCreationDate extends BasicDate{

	/** Eclipse generated id */
	private static final long serialVersionUID = -7904658027002257474L;
	
	/** Item the release date belongs to */
	private GenericItem item;

	/**
	 * Default constructor
	 */
	public OriginalItemCreationDate() {
		super();
	}
	
	/**
	 * Constructor to create Release Date
	 * 
	 * @param month month of year
	 * @param day day of the month
	 * @param year year
	 */
	public OriginalItemCreationDate(int month, int day, int year){
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
	public OriginalItemCreationDate(int month, int day, int year, int hrs, int mins, int secs, int fSec){
		super(month, day, year, hrs, mins, secs, fSec);
		
	}

	/**
	 * Get item
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set item
	 * 
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}

}
