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

package edu.ur.ir;

import edu.ur.persistent.BasePersistent;

/**
 * Basic implementation for Date
 * 
 * @author Sharmila Ranganathan
 *
 */
public class BasicDate extends BasePersistent implements Date  {
	
	/**	 Eclipse generated id */
	private static final long serialVersionUID = -6935937743336824323L;

	/** Day of the month */
	private int day = 0;
	
	/** Month of the year */
	private int month = 0;
	
	/** year in the date */
	private int year = 0;
	
	/** Hour of the date */
	private int hours = 0;
	
	/** Minute of the hour */
	private int minutes = 0;
	
	/** Second of the minute */
	private int seconds = 0;
	
	/** Fraction of the second */
	private int fractionOfSecond = 0;
	
	/**
	 * Default constructor
	 * 
	 */
	public BasicDate(){};
	
	/**
	 * Constructor to create Date
	 * 
	 * @param month month of year
	 * @param day day of the month
	 * @param year year
	 */
	public BasicDate(int month, int day, int year){
		this.month = month;
		this.day = day;
		this.year = year;
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
	public BasicDate(int month, int day, int year, int hrs, int mins, int secs, int fSec){
		this.month = month;
		this.day = day;
		this.year = year;
		this.hours = hrs;
		this.minutes = mins;
		this.seconds = secs;
		this.fractionOfSecond = fSec;
	}
	
	/**
	 * Get day
	 * 
	 * @return
	 */
	public int getDay() {
		return day;
	}
	
	/**
	 * Set day
	 * 
	 * @param day
	 */
	public void setDay(int day) {
		this.day = day;
	}
	
	/**
	 * Get month
	 * 
	 * @return
	 */
	public int getMonth() {
		return month;
	}
	
	/**
	 * Set month
	 * 
	 * @param month
	 */
	public void setMonth(int month) {
		this.month = month;
	}
	
	/**
	 * Get year
	 * 
	 * @return
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Set year
	 * 
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * Get raction Of Second
	 * 
	 * @return
	 */
	public int getFractionOfSecond() {
		return fractionOfSecond;
	}
	
	/**
	 * Set fraction Of Second
	 * 
	 * @param fractionOfSecond
	 */
	public void setFractionOfSecond(int fractionOfSecond) {
		this.fractionOfSecond = fractionOfSecond;
	}

	/**
	 * Get hours
	 * 
	 * @return
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Set hours
	 * 
	 * @param hours
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * Get minutes
	 * 
	 * @return
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Set minutes
	 * 
	 * @param minutes
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	/**
	 * Get seconds
	 * 
	 * @return
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Set seconds
	 * 
	 * @param seconds
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{

		if (this == o) return true;

		if (!(o instanceof BasicDate)) return false;

		final BasicDate other = (BasicDate) o;
		
		if( ( day != 0 && day != other.getDay() ) ||
				( day == 0 && other.getDay() != 0 ) ) return false;
		
		if( ( month != 0 && month != other.getMonth() ) ||
				( month == 0 && other.getMonth() != 0 ) ) return false;

		if( ( year != 0 && year != other.getYear() ) ||
				( year == 0 && other.getYear() != 0 ) ) return false;
		
		if( ( hours != 0 && hours != other.getHours() ) ||
				( hours == 0 && other.getHours() != 0 ) ) return false;
		
		if( ( minutes != 0 && minutes != other.getMinutes() ) ||
				( minutes == 0 && other.getMinutes() != 0 ) ) return false;
		
		if( ( seconds != 0 && seconds != other.getSeconds() ) ||
				( seconds == 0 && other.getSeconds() != 0 ) ) return false;
		
		if( ( fractionOfSecond != 0 && fractionOfSecond != other.getFractionOfSecond() ) ||
				( fractionOfSecond == 0 && other.getFractionOfSecond() != 0 ) ) return false;
		
		return true;
	}
	
	/**
	 * To string method
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("[Date id = ");
		sb.append(id);
		sb.append(" day = ");
		sb.append(day);
		sb.append(" month = ");
		sb.append(month);
		sb.append(" year = ");
		sb.append(year);
		sb.append(" hour = ");
		sb.append(hours);
		sb.append(" min = ");
		sb.append(minutes);
		sb.append(" sec = ");
		sb.append(seconds);
		sb.append(" fraction of sec = ");
		sb.append(fractionOfSecond);
		sb.append("]");
		return sb.toString();		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += day;
		value += month;
		value += year;
		value += hours;
		value += minutes;
		value += seconds;
		value += fractionOfSecond;

		return value;
	}

}
