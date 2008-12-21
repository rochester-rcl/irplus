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

package edu.ur.ir.news;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.ur.file.db.FileInfo;
import edu.ur.ir.file.IrFile;
import edu.ur.persistent.BasePersistent;


/**
 * This represents a news item
 * 
 * @author Sharmila Ranganathan
 * @author Nathan Sarr
 *
 */
public class NewsItem extends BasePersistent{
	
	/** Eclipse generated id */
	private static final long serialVersionUID = 5982891542735462561L;

	/**  Name of the news item. */
	private String name;
	
	/** Quick description of the news */
	private String description;
	
	/**  file that contains the news information */
	private FileInfo article;

	/**  Date news item has to be made available. */
	private Date dateAvailable;

	/**  Date news item has to be removed. */
	private Date dateRemoved;
	
	/**  Pictures for the news item. */
	private Set<IrFile> pictures = new HashSet<IrFile>();
	
	/** main picture to be shown as a thumbnail to users */
	private IrFile primaryPicture;
	
	/**
	 * Package protected news item
	 */
	NewsItem(){};
	
	/**
	 * Create a news item with the specified name.
	 * 
	 * @param name - name of the news item
	 */
	public NewsItem(String name)
	{
		setName(name);
	}

	/**
     * Get news item name
     * 
     * @return the name
     */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the news item name
	 * 
	 * @param name news item name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get the news article description
	 * 
	 * @return the article
	 */
	public FileInfo getArticle() {
		return article;
	}
	
	/**
	 * Set the news article
	 * 
	 * @param article the news article
	 */
	public void setArticle(FileInfo article) {
		this.article = article;
	}
	
	/**
	 * Get the news item available date
	 *  
	 * @return available date
	 */
	public Date getDateAvailable() {
		return dateAvailable;
	}
	
	/**
	 * Set the news item available date
	 *  
	 * @param dateAvailable Date of news item availability
	 */
	public void setDateAvailable(Date dateAvailable) {
		this.dateAvailable = dateAvailable;
	}
	
	/**
	 * Get the date to remove news item
	 * 
	 * @return date to remove this news item
	 */
	public Date getDateRemoved() {
		return dateRemoved;
	}
	
	/**
	 * Set the date to remove news item
	 * 
	 * @param dateRemoved date to remove news item
	 */
	public void setDateRemoved(Date dateRemoved) {
		this.dateRemoved = dateRemoved;
	}
	
    /**
     * Picture for this news item.
     * 
     * @return
     */
    public Set<IrFile> getPictures() {
		return Collections.unmodifiableSet(pictures);
	}
    
    /**
     * Remove the picture for the specified set of pictures.
     * 
     * @return ture if the picture is removed.
     */
    public boolean removePicture(IrFile picture)
    {
    	return pictures.remove(picture);
    }
    
    /**
     * Add the picture to the set of the pictures.
     * 
     * @param picture - picture to add
     */
    public void addPicture(IrFile picture)
    {
    	pictures.add(picture);
    }
    
    /**
     * Get the ir file with the speicified id.
     * 
     * @param irFileId - id of the ir file
     * @return - the picture.
     */
    public IrFile getPicture(Long irFileId)
    {
    	for( IrFile f : pictures)
    	{
    		if(f.getId().equals(irFileId))
    		{
    			return f;
    		}
    	}
    	return null;
    }

	/**
	 * Set the picture for this news item.
	 * 
	 * @param picture
	 */
	public void setPictures(Set<IrFile> pictures) {
		this.pictures = pictures;
	}

	/**
     * Get the hash code.
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
    	int hash = 0;
    	hash += name == null ? 0 : name.hashCode();
    	hash += dateAvailable == null ? 0 : dateAvailable.hashCode();
    	hash += dateRemoved == null ? 0 : dateRemoved.hashCode();
    	return hash;
    }
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer("[News item id = ");
		sb.append(id);
		sb.append(" name = ");
		sb.append(name);
		sb.append(" description = ");
		sb.append(description);
		sb.append(" date available = ");
		sb.append(dateAvailable);
		sb.append("date removed = ");
		sb.append(dateRemoved);
		sb.append("]");
		return sb.toString();
    }
    
    /**
     * Person Name Equals.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
    	if (this == o) return true;
		if (!(o instanceof NewsItem)) return false;

		final NewsItem other = (NewsItem) o;

		if( ( name != null && !name.equals(other.getName()) ) ||
			( name == null && other.getName() != null ) ) return false;
		
		if( ( dateAvailable != null && !dateAvailable.equals(other.getDateAvailable()) ) ||
			( dateAvailable == null && other.getDateAvailable() != null ) ) return false;
		
		if( ( dateRemoved != null && !dateRemoved.equals(other.getDateRemoved()) ) ||
			( dateRemoved == null && other.getDateRemoved() != null ) ) return false;

		return true;
    }

	/**
	 * Get the quick description for the news.
	 * 
	 * @return the description for the news
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description.
	 * 
	 * @param description - description of the news item
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the primary picture for the news item.
	 * 
	 * @return - the primary picture
	 */
	public IrFile getPrimaryPicture() {
		return primaryPicture;
	}

	/**
	 * Set the primary picture.
	 * 
	 * @param primaryPicture
	 */
	public void setPrimaryPicture(IrFile primaryPicture) {
		this.primaryPicture = primaryPicture;
	}
}
