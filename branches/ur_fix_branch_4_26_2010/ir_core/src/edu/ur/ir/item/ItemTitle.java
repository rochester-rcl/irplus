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
 * Represents a sub titles for item
 *   
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 */
public class ItemTitle extends BasePersistent  {

	/**  Eclipse generated id */
	private static final long serialVersionUID = 7213668037628436533L;
	
	/** leading articles for the title  */
	private String leadingArticles;

	/** Item title  */
	private String title;
	
	/** lower case version of the title */
	private String lowerCaseTitle;
	
	/** first character of the name of this titles name  */
	private char titleFirstChar;
	
	/** The item this title belongs to. */
	private GenericItem item;

	
	/**
	 * Package protected constructor.
	 */
	ItemTitle(){};
	
	/**
	 * Create a item title 
	 * 
	 * @param linkVersion
	 */
	ItemTitle(GenericItem item, String title, String leadingArticles)
	{
		setItem(item);
		setTitle(title);
		setLeadingArticles(leadingArticles);
	}
	
	/**
	 * The item this title belongs to.
	 * 
	 * @return
	 */
	public GenericItem getItem() {
		return item;
	}

	/**
	 * Set the item this title belongs to.
	 * 
	 * @param item
	 */
	public void setItem(GenericItem item) {
		this.item = item;
	}
	
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int value = 0;
		value += title == null ? 0 : title.hashCode();
		value += leadingArticles == null ? 0 : leadingArticles.hashCode();
		value += item == null ? 0 : item.hashCode();
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("[Item Title id = ");
		sb.append(id);
		sb.append(" version = " );
		sb.append(version);
		sb.append(" title = " );
		sb.append(title);
		sb.append("]");
		
		return  sb.toString();
	}
	
	/**
	 * Check equality of the object
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ItemTitle)) return false;

		final ItemTitle other = (ItemTitle) o;

		if( ( title != null && !title.equals(other.getTitle()) ) ||
			( title == null && other.getTitle() != null ) ) return false;
		
		if( ( leadingArticles != null && !leadingArticles.equals(other.getLeadingArticles()) ) ||
			( leadingArticles == null && other.getLeadingArticles() != null ) ) return false;
		
		if( ( item != null && !item.equals(other.getTitle())) ||
			( item == null && other.getTitle() != null ) ) return false;

		return true;
	}

	/**
	 * Get item title 
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set item title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		if(title.length() > 0)
		{
		    this.titleFirstChar = Character.toLowerCase(title.charAt(0));
		}
		lowerCaseTitle = title.toLowerCase();
	}

	/**
	 * The leading articles for the item title - example A, an, d', de, the, ye
	 * 
	 * @return
	 */
	public String getLeadingArticles() {
		return leadingArticles;
	}
	
	/**
	 * Return the entire title including the articles
	 * @return
	 */
	public String getFullTitle()
	{
		if( leadingArticles != null )
		{
			return  leadingArticles + " " + title;
		}
		else
		{
			return title;
		}
	}

	/**
	 * The leading articles for the item title - example A, an, d', de, the, ye
	 * 
	 * @param leadingArticles
	 */
	public void setLeadingArticles(String leadingArticles) {
		this.leadingArticles = leadingArticles;
	}

	public String getLowerCaseTitle() {
		return lowerCaseTitle;
	}

	public char getTitleFirstChar() {
		return titleFirstChar;
	}



}
