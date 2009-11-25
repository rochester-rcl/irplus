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

/**
 * Interface to represent Date
 * 
 * @author Sharmila Ranganathan
 *
 */
public interface Date {
	
	/**
	 * Get day
	 * 
	 * @return
	 */
	public int getDay();
	
	/**
	 * Set day
	 * 
	 * @param day
	 */
	public void setDay(int day);
	
	/**
	 * Get month
	 * 
	 * @return
	 */
	public int getMonth();
	
	/**
	 * Set month
	 * 
	 * @param month
	 */
	public void setMonth(int month);
	
	/**
	 * Get year
	 * 
	 * @return
	 */
	public int getYear() ;
	
	/**
	 * Set year
	 * 
	 * @param year
	 */
	public void setYear(int year);
	
	/**
	 * Get raction Of Second
	 * 
	 * @return
	 */
	public int getFractionOfSecond();
	
	/**
	 * Set fraction Of Second
	 * 
	 * @param fractionOfSecond
	 */
	public void setFractionOfSecond(int fractionOfSecond) ;

	/**
	 * Get hours
	 * 
	 * @return
	 */
	public int getHours() ;

	/**
	 * Set hours
	 * 
	 * @param hours
	 */
	public void setHours(int hours) ;

	/**
	 * Get minutes
	 * 
	 * @return
	 */
	public int getMinutes() ;

	/**
	 * Set minutes
	 * 
	 * @param minutes
	 */
	public void setMinutes(int minutes) ;

	/**
	 * Get seconds
	 * 
	 * @return
	 */
	public int getSeconds() ;

	/**
	 * Set seconds
	 * 
	 * @param seconds
	 */
	public void setSeconds(int seconds);



}
